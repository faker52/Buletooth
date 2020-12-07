package com.example.buletooth;

import android.app.Application;

import android.bluetooth.BluetoothSocket;


public class Mysocket extends Application {
    BluetoothSocket socket = null;
    public BluetoothSocket getSocket(){
        return socket;
    }
    public void setSocket(BluetoothSocket socket){
        this.socket = socket;
    }
}