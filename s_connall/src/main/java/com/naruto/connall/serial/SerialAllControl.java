package com.naruto.connall.serial;

import android.text.TextUtils;
import android.util.Log;

import cn.shorr.serialport.SerialPortConfig;
import cn.shorr.serialport.SerialPortFinder;
import cn.shorr.serialport.SerialPortUtil;
import cn.shorr.serialport.SerialRead;
import cn.shorr.serialport.SerialWrite;

/**
 * 控制板接6口；HC/CO传感器接5口；NO传感器接4口 ；上位机可以用3口测试
 */
@Deprecated
public class SerialAllControl {
//    private String thisFavor;
//    private SerialPortUtil serialPortUtil;
//    private Long[] delayMsArr = new Long[7];
//    private String[] nameArr = new String[7];
//    private SerialPortConfig[] serialPortConfigsArr = new SerialPortConfig[7];
//    private OnSerialListener[] onSerialListenerArr = new OnSerialListener[7];
//
////    private OSingleThreadPool[] threadPoolArr = new OSingleThreadPool[7];
//    private SerialRead[] serialReadArr = new SerialRead[7];
//    public interface OnSerialListener{
//        void onDataBack(byte[] data);
//    }
//    // ========================out======================
//    private static SerialAllControl _instance;
//    public static SerialAllControl getInstance() {
//        if (_instance == null)
//            _instance = new SerialAllControl();
//        return _instance;
//    }
//
//    // ========================out======================
//    public void checkSerialList() {
//        SerialPortFinder serialPortFinder = new SerialPortFinder();
//        String[] devices = serialPortFinder.getAllDevicesPath();
//        if (devices != null) for (String str : devices) {
//            Log.e("TAG", str);
//        }
//    }
//
//    private void closeSerialPort() {
//        if(serialReadArr != null) {
//            for (int i = 0; i < serialReadArr.length; i++) {
//                if (serialReadArr[i] != null) serialReadArr[i].unRegisterListener();
//                serialReadArr[i] = null;
//            }
//        }
//        if (serialPortUtil != null) serialPortUtil.unBindService();
//        serialPortUtil = null;
//    }
//    public void changeSerial(int port,String serialName,int baudrate, long deley, OnSerialListener listener) {
//        SerialPortConfig config = new SerialPortConfig("/dev/ttyS"+port, baudrate);
//        serialPortConfigsArr[port] = config;
//        onSerialListenerArr[port] = listener;
//        delayMsArr[port] = deley;
//        nameArr[port] =  serialName;
//    }
//
//    /**NO:4,CO2:3,Board:6,UP:5,Smoke:0*/
//    private long restartTime = 0;
//    public void restartSerial() {
//        closeSerialPort();
//        //配置串口参数
//        serialPortUtil = new SerialPortUtil(GlobalInitBase.getContext()
//                , serialPortConfigsArr[0]//port0 0口烟度计
//                , serialPortConfigsArr[1]//port1 1口无效
//                , serialPortConfigsArr[2]//port2 2口混柴或Gasboard_2300
//                , serialPortConfigsArr[3]//port3 3口CO2
//                , serialPortConfigsArr[4] //port4 4口NO
//                , serialPortConfigsArr[5]//port5 5口上位机
//                , serialPortConfigsArr[6]//port6 6口控制板
//        );
//        //设置为调试模式，打印收发数据
//        serialPortUtil.setDebug(true);
//        //绑定串口服务
//        serialPortUtil.bindService();
//
//        for (int i = 0; i < serialReadArr.length; i++) {
//            if(i == 1)continue;
//            serialReadArr[i] = new SerialRead(GlobalInitBase.getContext());
//            final int finalI = i;
//            serialReadArr[i].registerListener(new SerialRead.ReadDataListener() {
//                @Override
//                public void onReadData(byte[] data) {
//                    LogMe.showInDebug("串口回包"+nameArr[finalI]);
//                    if(onSerialListenerArr[finalI]!=null)onSerialListenerArr[finalI].onDataBack(data);
//                }
//            });
//        }
//        Log.e("TAG", "connect");
//        restartTime = System.currentTimeMillis()+2000L;
//    }
//
//    public void sendMessage(final int port, final String hexStr) {
//        if(restartTime == 0 || System.currentTimeMillis()<restartTime)return;
//        if(TextUtils.isEmpty(thisFavor))thisFavor = GlobalInitBase.getCurrentFAVOR();
//        if(thisFavor.equals("padasm"))return;
//        if(serialReadArr[port]==null)return;
//        if(threadPoolArr[port] == null)threadPoolArr[port] = new OSingleThreadPool();
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(delayMsArr[port]);//加上延时防止指令冲突
//                    SerialWrite.sendData(GlobalInitBase.getContext(), port, ByteHelper.hexStringToBytes(hexStr));
//                }catch (Exception e){
//                    Log.e("ExceptionTread","threadPoolControl"+e.toString()+"\n"+e.getMessage());
//                    return;
//                }
//            }
//        };
//        threadPoolArr[port].putRunnable(runnable);
//    }
}
