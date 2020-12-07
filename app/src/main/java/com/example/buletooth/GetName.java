package com.example.buletooth;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class GetName extends AppCompatActivity {

    Button getmge;
    EditText editText;
    TextView textview;
    private BluetoothSocket socket8051;
    private BufferedOutputStream out8051;
    private BufferedReader in8051;
    byte[] array_51;
    private int num=0;
    private Handler handler;
    private String data;
//00
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getname);

        initial();
        th th1 = new th();
        th1.start();
        socket8051= ((Mysocket)getApplication()).getSocket();
        try{
            out8051 = new BufferedOutputStream(socket8051.getOutputStream());
            //in8051  = new BufferedInputStream(socket8051.getInputStream());
        }
        catch(IOException E){
        }//初始化输入流的socket


        handler = new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0x125:
                        textview.append(data);
                        textview.append("\n");
                        break;
                }

            }


        };

        getmge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();

                Toast.makeText(GetName.this,str,Toast.LENGTH_LONG).show();
                switch (str){
                    case "num": num=0; break;
                    case "name": num=1; break;
                    case "sex": num=2; break;
                    case "age": num=3; break;
                }
                array_51 = BigInteger.valueOf(256+num).toByteArray();
                Log.e("array", "sendmessage: "+array_51[0]+","+array_51[1]);
                try{
                    out8051.write(array_51);
                    out8051.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void initial(){
        getmge = (Button) findViewById(R.id.get_message);
        editText = (EditText) findViewById(R.id.edittext);
        textview = (TextView) findViewById(R.id.textview);

    }

    class th extends Thread{
        byte[] buffer = new byte[1024];
        int count =0;

        @Override
        public void run() {
            Log.e("run", "sendmessage: ok");
                try {
                    while (true) {

                        if (socket8051.getInputStream().available() > 0 == false) {//等待数据过来
                            continue;
                        } else {
                            Thread.sleep(200);//接到首数据等待0.2秒，否则会出现数据不全
                        }
                        count = socket8051.getInputStream().read(buffer);
                        if (count > 0) {//必须判断大于0 ,不然读取出错
                            Log.e("run", "shoudao "+count+":"+buffer[0]+":"+buffer[1]);
                            Message msg = new Message();
                            msg.obj = new String(buffer, 0, count, "utf-8");
                            data = (String.valueOf(msg.obj));
                            Log.e("run", "sendmessage: "+data);
                            handler.sendEmptyMessage(0x125);

                        }
                    }
                } catch (Exception e) {

                }
            }
        }


}


