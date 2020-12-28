package com.example.buletooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class LastActivity extends AppCompatActivity implements View.OnClickListener{

    private Button up,left,down,right,start;
    private BluetoothSocket socket8051;
    private BufferedOutputStream out8051;
    byte[] array_51;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);
        initial();
        socket8051= ((Mysocket)getApplication()).getSocket();
        try{
            out8051 = new BufferedOutputStream(socket8051.getOutputStream());
            //in8051  = new BufferedInputStream(socket8051.getInputStream());
        }
        catch(IOException E){
        }//初始化输入流的socket
    }

    private  void initial(){
        up = (Button) findViewById(R.id.up);
        left = (Button) findViewById(R.id.left);
        down = (Button) findViewById(R.id.down);
        right = (Button) findViewById(R.id.right);
        start = (Button) findViewById(R.id.start);
        up.setOnClickListener(this);
        left.setOnClickListener(this);
        down.setOnClickListener(this);
        right.setOnClickListener(this);
        start.setOnClickListener(this);

    }

    private void move(int temp){
        array_51 = BigInteger.valueOf(256+0).toByteArray();
        Log.e("array", "sendmessage: "+array_51[0]+","+array_51[1]);
        try{
            out8051.write(array_51);
            out8051.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.up:
                move(0);
                break;
            case R.id.left:
                move(1);
                break;
            case R.id.down:
                move(2);
                break;
            case R.id.right:
                move(3);
            default:
                break;
        }
    }
}