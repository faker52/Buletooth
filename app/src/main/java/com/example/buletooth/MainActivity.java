package com.example.buletooth;



import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private BluetoothSocket socket8051;
    private BufferedOutputStream out8051;
    private int outnum=255;
    private Button end,sendout,water;
    private TextView textView;
    //private EditText editOutNum;
    private CheckBox D0,D1,D2,D3,D4,D5,D6,D7;
    byte[] array_51;
    private final UUID UUID8051=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//；每一个蓝牙特有的uuid，调用


    private static final int REQUEST_ENABLE_BULETOOTH=2;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        findiId();
        socket8051= ((Mysocket)getApplication()).getSocket();

        // if(bluetoothAdapter==null){
 //     Toast.makeText(this,"不支持蓝牙",Toast.LENGTH_LONG).show();
 //     finish();
 // }
 // else if(!bluetoothAdapter.isEnabled()){
 //     Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
 //     startActivityForResult(intent,REQUEST_ENABLE_BULETOOTH);
 //     Toast.makeText(this,"启动蓝牙中",Toast.LENGTH_LONG).show();
 // }


 // BluetoothDevice device =bluetoothAdapter.getRemoteDevice("20:19:09:26:40:74");
  try{
 //     socket8051 =device.createRfcommSocketToServiceRecord(UUID8051);
 //     socket8051.connect();
        out8051 = new BufferedOutputStream(socket8051.getOutputStream());
 //     array_51 = BigInteger.valueOf(512+127).toByteArray();
 //     out8051.write(array_51);
 //     Log.e("array", "onClick: "+array_51[0]+","+array_51[1]);
       // out8051.flush();
    }
  catch(IOException E){

  }

water.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        array_51 = BigInteger.valueOf(128+outnum).toByteArray();
        Log.e("array", "onClick1: "+array_51[0]+","+array_51[1]);
        try{
            out8051.write(array_51);
            out8051.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
});




     sendout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             //outnum = new Integer(editOutNum.getText().toString());
             array_51 = BigInteger.valueOf(512+outnum).toByteArray();
              Log.e("array", "onClick: "+array_51[0]+","+array_51[1]);
             try{
                 out8051.write(array_51);
                 out8051.flush();
             }catch (Exception e){
                 e.printStackTrace();
             }
         }
       });


     end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(socket8051!=null){
                    try{
                        out8051.close();
                        socket8051.close();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,"还没有链接成功",Toast.LENGTH_LONG).show();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        });
    }



    private void findiId(){
        sendout = (Button)findViewById(R.id.sendout);
        end = (Button)findViewById(R.id.end);
        water = (Button)findViewById(R.id.water);


        D0= (CheckBox) findViewById(R.id.D0);
        D1= (CheckBox) findViewById(R.id.D1);
        D2= (CheckBox) findViewById(R.id.D2);
        D3= (CheckBox) findViewById(R.id.D3);
        D4= (CheckBox) findViewById(R.id.D4);
        D5= (CheckBox) findViewById(R.id.D5);
        D6= (CheckBox) findViewById(R.id.D6);
        D7= (CheckBox) findViewById(R.id.D7);


        D0.setOnCheckedChangeListener(this);
        D1.setOnCheckedChangeListener(this);
        D2.setOnCheckedChangeListener(this);
        D3.setOnCheckedChangeListener(this);
        D4.setOnCheckedChangeListener(this);
        D5.setOnCheckedChangeListener(this);
        D6.setOnCheckedChangeListener(this);
        D7.setOnCheckedChangeListener(this);



    }



    @Override
    public void onCheckedChanged(CompoundButton checkBox, boolean checked) {
        // TODO Auto-generated method stub
        switch (checkBox.getId()) {
            case R.id.D0:
                if (checked) {
                    outnum-=128;
                } else {
                   outnum+=128;
                }
                break;
            case R.id.D1:
                if (checked) {
                   outnum-=64;
                } else {
                    outnum+=64;
                }
                break;
            case R.id.D2:
                if (checked) {
                    outnum-=32;
                } else {
                    outnum+=32;
                }
                break;
            case R.id.D3:
                if (checked) {
                    outnum-=16;
                } else {
                    outnum+=16;
                }
                break;
            case R.id.D4:
                if (checked) {
                    outnum-=8;
                } else {
                    outnum+=8;
                }
                break;

            case R.id.D5:
                if (checked) {
                    outnum-=4;
                } else {
                    outnum+=4;
                }
                break;
            case R.id.D6:
                if (checked) {
                    outnum-=2;
                } else {
                    outnum+=2;
                }
                break;
            case R.id.D7:
                if (checked) {
                    outnum-=1;
                } else {
                    outnum+=1;
                }
                break;


            default:
                break;
        }
    }



}
