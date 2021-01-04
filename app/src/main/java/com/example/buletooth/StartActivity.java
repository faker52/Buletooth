package com.example.buletooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.UUID;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawer;
    private Button mBtnOne;
    private Button mBtnTwo;
    private Button mBtnThr;
    private ActionBar actionBar;
    private Toolbar mNormalToolbar;
    private final UUID UUID8051=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//每个蓝牙特有的uuid
    private static final int REQUEST_ENABLE_BULETOOTH=2;//最大连接数
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket8051;//socket实例
    private BufferedOutputStream out8051;
    byte[] array_51;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.left);
        initView();
        initBluetooth();
    }


    private void initBluetooth() {

        bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null){
            Toast.makeText(this,"不支持蓝牙",Toast.LENGTH_LONG).show();
            finish();
        }
        else if(!bluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,REQUEST_ENABLE_BULETOOTH);
            Toast.makeText(this,"启动蓝牙中",Toast.LENGTH_LONG).show();
        }//判断蓝牙是否链接

        BluetoothDevice device =bluetoothAdapter.getRemoteDevice("20:19:09:26:40:74");//创建device对象

        try{
            socket8051 =device.createRfcommSocketToServiceRecord(UUID8051);
            socket8051.connect();//进行连接
            ((Mysocket)getApplication()).setSocket(socket8051);//设置单例模式
            out8051 = new BufferedOutputStream(socket8051.getOutputStream());
            array_51 = BigInteger.valueOf(512+127).toByteArray();
            out8051.write(array_51);
            Log.e("array", "onClick: "+array_51[0]+","+array_51[1]);
            out8051.flush();
        }
        catch(IOException E){

        }
    }



    private void initView() {

        mNormalToolbar=findViewById(R.id.toolbar);
        mNormalToolbar.setTitle("Bluetooth");
        setSupportActionBar(mNormalToolbar);
        actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.more);
        }
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawer = findViewById(R.id.drawer);
        mBtnOne = findViewById(R.id.btn_one);
        mBtnTwo = findViewById(R.id.btn_two);
        mBtnThr = findViewById(R.id.btn_three);

        mBtnOne.setOnClickListener(this);
        mBtnTwo.setOnClickListener(this);
        mBtnThr.setOnClickListener(this);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menutoolbar, menu);
        //getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        //getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(mDrawer);
                break;
            default:
                break;
        }

        return true;
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_one:
                replaceFragment(1);
                break;
            case R.id.btn_two:
                replaceFragment(2);
                break;
            case R.id.btn_three:
                replaceFragment(3);
                break;
        }
    }

    private void replaceFragment(int id){
        //FragmentManager fm = getSupportFragmentManager();
        Intent intent1 = new Intent(StartActivity.this,MainActivity.class);
        Intent intent2 = new Intent(StartActivity.this,GetName.class);
        Intent intent3 = new Intent(StartActivity.this,LastActivity.class);

        switch (id){
            case 1:
                Toast.makeText(StartActivity.this,"点击第一个",Toast.LENGTH_LONG).show();
                startActivity(intent1);
                //fm.beginTransaction().replace(R.id.fragment_container,new FirstFragment()).com0mit();
                break;
            case 2:
                startActivity(intent2);
                Toast.makeText(StartActivity.this,"点击第2个",Toast.LENGTH_LONG).show();
                //fm.beginTransaction().replace(R.id.fragment_container,new SecondFragment()).commit();
                break;
            case 3:
                startActivity(intent3);
                //fm.beginTransaction().replace(R.id.fragment_container,new ThirdFragment()).commit();
                break;
        }
        mDrawerLayout.closeDrawer(mDrawer);
    }
}
