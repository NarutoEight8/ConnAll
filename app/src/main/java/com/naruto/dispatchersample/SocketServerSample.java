package com.naruto.dispatchersample;

import android.util.Log;

import com.naruto.connall.ByteTransUtil;
import com.naruto.connall.oksocket.ConnSocketServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;

/**
 * 在设置里启用网口,已测试过，
 * SocketServer.getInstance().startService(false);
 */
public class SocketServerSample {
    public static final String CODE_TYPE = "utf-8";
    private static final int PORT = 49600;
    private static final String TAG = SocketServerSample.class.getSimpleName();
    private ServerSocket serverSocket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private ConnSocketServer mySocketServer;
    //=================================================================
    private static SocketServerSample _instance = null;

    public static synchronized SocketServerSample getInstance() {
        if (_instance == null) {
            _instance = new SocketServerSample();
        }
        return _instance;
    }
    //=================================================================

    /**
     * 启动服务监听，等待客户端连接
     */
    public void startService() {
        if(mySocketServer == null){
            Log.e(TAG, "服务器，NEW");
            mySocketServer = new ConnSocketServer(readListener);
        }
        Log.e(TAG, "服务器，重连1");
        mySocketServer.resetPort(10086);
        Log.e(TAG, "开启服务器，监听端口:" + 10086);
        mySocketServer.restartServer();
    }
    public void sendMessage(byte[] datas) {
        if (mySocketServer == null) return;
//        if (BuildConfig.DEBUG) Log.e(TAG, "server发送数据：" + hexStr);
        mySocketServer.sendMessage(datas);
    }

    private ConnSocketServer.OnSocketServerReadListener readListener = new ConnSocketServer.OnSocketServerReadListener() {
        @Override
        public void onRead(byte[] datas) {
            //读完一组
            Log.e(TAG, "server收到客户端数据:" + ByteTransUtil.bytesToHexString(datas));
//            OCtrlUPMachine.getInstance().processResult(datas, ManagerMixQiCai.PORTINTYPE_NET);
        }
    };


}

