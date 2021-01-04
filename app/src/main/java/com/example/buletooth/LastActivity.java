package com.example.buletooth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class LastActivity extends AppCompatActivity implements View.OnClickListener{

    private Button up,left,down,right,start;
    private SeekBar seekBar;
    private TextView textView1,textView2;
    private BluetoothSocket socket8051;
    private BufferedOutputStream out8051;
    byte[] array_51;
    Handler handler;
    String data;


    private int hard=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);
        initial();
        LastActivity.th th1 = new LastActivity.th();
        th1.start();

        socket8051= ((Mysocket)getApplication()).getSocket();
        try{
            out8051 = new BufferedOutputStream(socket8051.getOutputStream());
            //in8051  = new BufferedInputStream(socket8051.getInputStream());
        }
        catch(IOException E){
        }//初始化输入流的socket


      /*  Intent intentget = new Intent();
        String str =intentget.getStringExtra("状态");
        if(str==null);
        else if(str.equals("取消")){
            this.finish();
        }
        else if(str.equals("确定")){
            array_51 = BigInteger.valueOf(256+15).toByteArray();
            try{
                out8051.write(array_51[1]);
                out8051.flush();
            }catch (Exception e){
                e.printStackTrace();
            }
        }*/
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("游戏结束");
        //设置图标
        builder.setIcon(R.mipmap.ic_launcher);
        //设置Message
        builder.setMessage("游戏失败了，点击确认继续游戏，取消退出游戏");


        //设置确定按钮及其点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                seekBar.setProgress(4);
                array_51 = BigInteger.valueOf(256+15).toByteArray();//发送的数据，转化为byte数组
                Log.e("send","qxx"+array_51[1]);
                try{
                    out8051.write(array_51[1]);//发送到串口
                    out8051.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        //设置取消按钮及其点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                array_51 = BigInteger.valueOf(256+5).toByteArray();
                Log.e("send","qxx"+array_51[1]);
                try{
                    out8051.write(array_51[1]);
                    out8051.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }


                LastActivity.this.finish();
            }
        });
        //设置中立按钮及其点击事件




        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int max = seekBar.getMax();
                String s = i + "/" + max;
                hard=i;
                textView1.setText("正在拖动");
                textView2.setText("当前难度" + s);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                textView1.setText("开始拖动");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView1.setText("停止拖动");
                array_51 = BigInteger.valueOf(2049+hard+6).toByteArray();
                Log.e("array", "sendmessage: "+array_51[0]+","+array_51[1]);
                try{
                    out8051.write(array_51[1]);
                    out8051.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        handler = new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0x125:
                        //使用Dialog构造器，创造出来一个Dialog
                        Dialog dialog = builder.create();
                        //显示
                        dialog.show();
                        Log.e("run","成功收到");
                        break;
                }

            }


        };





    }
    private  void initial(){
        up = (Button) findViewById(R.id.up);
        left = (Button) findViewById(R.id.left);
        down = (Button) findViewById(R.id.down);
        right = (Button) findViewById(R.id.right);
        start = (Button) findViewById(R.id.start);
        textView1 = (TextView) findViewById(R.id.tv1);
        textView2 = (TextView) findViewById(R.id.tv2);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        up.setOnClickListener(this);
        left.setOnClickListener(this);
        down.setOnClickListener(this);
        right.setOnClickListener(this);
        start.setOnClickListener(this);

    }

    private void move(int temp){
        array_51 = BigInteger.valueOf(256+temp).toByteArray();
        Log.e("array", "sendmessage: "+array_51[0]+","+array_51[1]);
        try{
            out8051.write(array_51[1]);
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
                break;
            default:
                move(5);
                break;
        }
    }



    class th extends Thread{
        byte[] buffer = new byte[2];
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
                        if(buffer[0]==-52)
                        handler.sendEmptyMessage(0x125);

                    }
                }
            } catch (Exception e) {

            }
        }
    }
}