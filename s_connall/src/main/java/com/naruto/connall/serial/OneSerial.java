package com.naruto.connall.serial;

import android.content.Context;
import android.text.TextUtils;

import com.naruto.connall.ByteTransUtil;

import cn.shorr.serialport.SerialPortConfig;
import cn.shorr.serialport.SerialRead;
import cn.shorr.serialport.SerialWrite;

public class OneSerial {
    private String thisFavor;
    private String      serialName = "";
    private int         port = 0;//"/dev/ttyS0"
    private int         baudrate = 9600;
    private long        deley = 80;
//    private OSingleThreadPool threadPool;
    private SerialRead  serialRead;
    private OnSerialListener onSerialListener;
    public interface OnSerialListener{
        void onDataBack(byte[] data);
    }

    public OneSerial(int portNum) {
        this.port = portNum;
    }

    public void changeSerial(String serialName,int baut,long deley,OnSerialListener listener) {
        this.serialName = serialName;
        this.baudrate = baut;
        this.deley = deley;
        this.onSerialListener = listener;
    }

    public SerialPortConfig getSerialConfig() {
        SerialPortConfig config = new SerialPortConfig("/dev/ttyS"+port, baudrate);
        return config;
    }
    public void removeReadListener(){
        if(serialRead!=null)serialRead.unRegisterListener();
        serialRead = null;
    }
    public void resetRead(Context context){
        serialRead = new SerialRead(context);
        serialRead.registerListener(port, new SerialRead.ReadDataListener() {
            @Override
            public void onReadData(byte[] data) {
                if(onSerialListener!=null)onSerialListener.onDataBack(data);
            }
        });
    }
    public void sendMessage(final Context context,final String hexStr) {
        if(serialRead==null)return;
        SerialWrite.sendData(context, port, ByteTransUtil.hexStringToBytes(hexStr));
    }
}
