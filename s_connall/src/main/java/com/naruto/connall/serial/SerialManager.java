package com.naruto.connall.serial;

import android.content.Context;
import android.util.Log;


import cn.shorr.serialport.SerialPortFinder;
import cn.shorr.serialport.SerialPortUtil;


/**
 * 控制板接6口；HC/CO传感器接5口；NO传感器接4口 ；上位机可以用3口测试
 */
public class SerialManager {
    private String TAG = SerialManager.class.getSimpleName();
    private SerialPortUtil serialPortUtil;
    private OneSerial[] oneSerials;
    private Context context;
    public static long restartTime = 0;
    // ========================out======================
    private static SerialManager _instance;
    public static SerialManager getInstance() {
        if (_instance == null)
            _instance = new SerialManager();
        return _instance;
    }
    // ========================out======================
    private void init(){
        oneSerials = new OneSerial[7];
        for (int i = 0; i < 7; i++) {
            oneSerials[i]= new OneSerial(i);
        }
    }
    public void changeSerial(int port,String serialName,int baut, long deley, OneSerial.OnSerialListener listener) {
        if(oneSerials == null)init();
        oneSerials[port].changeSerial(serialName,baut,deley,listener);
    }
    public void sendMessage(int port,String hexStr){
        oneSerials[port].sendMessage(context,hexStr);
    }

    public void closeSerialPort() {
        if(oneSerials == null)return;
        for (int i = 0; i < oneSerials.length; i++) {
            oneSerials[i].removeReadListener();
        }
        if (serialPortUtil != null) serialPortUtil.unBindService();
        serialPortUtil = null;
    }
    public void restartSerial(Context context){
        this.context = context;
        if(oneSerials == null)init();
        restartTime = System.currentTimeMillis()+2000;
        GlobalInitBase.onRestartSerial();//此处重设定串口指令发送,口改了指令也会不同

        closeSerialPort();
        //配置串口参数
        serialPortUtil = new SerialPortUtil(SerialManager.this.context
                , oneSerials[0].getSerialConfig()//port0 0口烟度计
                , oneSerials[1].getSerialConfig()//port1 1口无效
                , oneSerials[2].getSerialConfig()//port2 2口混柴或Gasboard_2300
                , oneSerials[3].getSerialConfig() //port3 3口CO2
                , oneSerials[4].getSerialConfig() //port4 4口NO
                , oneSerials[5].getSerialConfig()//port5 5口上位机
                , oneSerials[6].getSerialConfig()//port6 6口控制板
        );
        //设置为调试模式，打印收发数据
        serialPortUtil.setDebug(false);
        //绑定串口服务
        serialPortUtil.bindService();
        //先绑后监听
        for (int i = 0; i < oneSerials.length; i++) {
            oneSerials[i].resetRead(SerialManager.this.context);
        }
//        LogMe.showInDebug(TAG+ "serial ReConnOK");
    }
    public void checkSerialList() {
        SerialPortFinder serialPortFinder = new SerialPortFinder();
        String[] devices = serialPortFinder.getAllDevicesPath();
        if (devices != null) for (String str : devices) {
            Log.e(TAG, str);
        }
    }
}
