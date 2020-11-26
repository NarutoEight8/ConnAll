package com.naruto.connall.blue.blue_2_0;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import androidx.annotation.CallSuper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**蓝牙2.0通讯**/

/**
 * Step1: onCreate BluePermission.checkBlueFirstInPermission(this);
 * Step2: startActivityForResult(DeviceDiscoveryActivity, REQUEST_CONNECT_DEVICE);
 * Step3: onActivityResult getAddress
 * Step4: onScanedDevice
 * Step5: show conn State
 */
public abstract class BlueConn_2_0 {
    private String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP服务UUID号
    private BluetoothSocket _socket;      //蓝牙通信socket
    private BluetoothDevice _device;     //蓝牙设备
    private InputStream _inStr;   //读数据流
    private boolean _readThreadOn;//读线程关闭和打开
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //获取本地蓝牙适配器，即蓝牙设备

    //======================初始化=======================
    @CallSuper
    public void init(String UUID){
        MY_UUID = UUID;
    }
    //======================设侦听器=======================
    protected abstract void onConnectOK(String name, String address);
    protected abstract void onConnectFail(String reason);
    protected abstract void onDisConnected();
    protected abstract void onDataBack(byte[] bytes);
    //=============================================
    /**数据发送**/
    @CallSuper
    protected void sendMessage(byte[] bytes){
        if(_socket == null)return;
        try {
            OutputStream os = _socket.getOutputStream();
            os.write(bytes);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**是否蓝牙打开，用于判断是否正在打开蓝牙**/
    @CallSuper
    public boolean isBlueEnable(){
        return _bluetooth.isEnabled();
    }
    /**是否蓝牙已连接**/
    @CallSuper
    public boolean isBlueConnected(){
        return !(_socket==null);
    }
    /**断开连接,程序退出一定要断开**/
    @CallSuper
    public void closeConn(){
        try{
            _readThreadOn = false;
            Thread.sleep(2000);

            if(_inStr!=null)_inStr.close();
            if(_socket!=null)_socket.close();
            _socket = null;
            onDisConnected();
        }catch(IOException e){}
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**onActivityResult得到扫秒的蓝牙后，执行连接动作，接收结果**/
    //接收活动结果，响应startActivityForResult()
    @CallSuper
    public void onScanedDevice(String address) {
        // 得到蓝牙设备句柄
        _device = _bluetooth.getRemoteDevice(address);
        // 用服务号得到socket
        try{
            if(_socket == null) Log.d("TAG", "onScanedDevice: "+_device.getName());
            _socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
        }catch(IOException e){
            onConnectFail("连接失败！socket错");
        }
        //连接socket
        try{
            _socket.connect();
            onConnectOK(_device.getName(),_device.getAddress());
        }catch(IOException e){
            try{
                onConnectFail("连接失败！socket关闭");
                _socket.close();
                _socket = null;
            }catch(IOException ee){
                onConnectFail("连接失败！socket关闭错"+ee.toString());
            }
            return;
        }

        //打开接收线程
        try{
            _inStr = _socket.getInputStream();   //得到蓝牙数据输入流
        }catch(IOException e){
            onConnectFail("读取数据流失败！"+e.toString());
            return;
        }
        if(!_readThreadOn){
            new ReadThread().start();
        }
    }



    //接收数据线程
    class ReadThread extends Thread{
        public void run(){
            _readThreadOn = true;
            //接收线程
            while(_readThreadOn){
                try{
                    if(_inStr.available()==0)break;
                    int           lengthTemp = 0;
                    byte[] buffer = new byte[1024];
                    while (-1 != (lengthTemp = _inStr.read(buffer))) { // read方法并不保证一次能读取1024*64个字节
                        byte[] result = new byte[lengthTemp];
                        System.arraycopy(buffer, 0, result, 0, lengthTemp);
                        onDataBack(result);
                    }
                }catch(IOException e){
                    onConnectFail("数据流解析失败！"+e.toString());
                }
            }
        }
    };

}
