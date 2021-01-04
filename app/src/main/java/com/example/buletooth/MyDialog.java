package com.example.buletooth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MyDialog extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //构建Dialog建造者                                   //上下文环境
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                Intent intent = new Intent(MyDialog.this,LastActivity.class);
                intent.putExtra("状态","确定");
                startActivity(intent);
            }
        });
        //设置取消按钮及其点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MyDialog.this,LastActivity.class);
                intent.putExtra("状态","取消");
                startActivity(intent);
            }
        });
        //设置中立按钮及其点击事件

        //使用Dialog构造器，创造出来一个Dialog
        Dialog dialog = builder.create();
        //显示
        dialog.show();

    }
}

