package com.naruto.dispatchersample;

import android.util.Log;

import com.naruto.connall.ByteTransUtil;
import com.naruto.connall.oksocket.ConnSocketClient;
import com.xuhao.didi.core.protocol.IReaderProtocol;

import java.nio.ByteOrder;

public class SocketClientSample {
    public static final String CODE_TYPE = "utf-8";
    private static final String TAG = SocketClientSample.class.getSimpleName();
    private ConnSocketClient socketClient;
    //=================================================================
    private static SocketClientSample _instance = null;
    public static synchronized SocketClientSample getInstance() {
        if (_instance == null) {
            _instance = new SocketClientSample();
        }
        return _instance;
    }
    //=================================================================
    /**
     * 启动连接
     */
    public void reStartConn() {
        if(socketClient ==null) socketClient = new ConnSocketClient("烟度计socket");
        Log.e(TAG, "socket，重连");
        socketClient.resetConn("192.168.0.1",10086,readerProtocol);
        socketClient.setOnSocketClientReadListener(onSmokeSocketClientReadListener);
    }
    private IReaderProtocol readerProtocol= new IReaderProtocol() {
        @Override
        public int getHeaderLength() {
            return 2;
        }

        @Override
        public int getBodyLength(byte[] header, ByteOrder byteOrder) {
            //06 41 15 0102030405060708190001020304050607080903EC06            06 01 AA 4F
//            if((int) (header[0]&0xff) !=6)return 0;
            return (1+header[1]&0xff);//去掉头的长度
        }
    } ;
    public void sendMessageSocket(String hexStr) {
        if (socketClient == null) return;
        socketClient.sendMessage(hexStr);
    }
    ConnSocketClient.OnSocketClientReadListener onSmokeSocketClientReadListener = new ConnSocketClient.OnSocketClientReadListener() {
        @Override
        public void onRead(byte[] datas) {
            String string = ByteTransUtil.bytesToHexString(datas);
//            Log.e(TAG, "收到烟机数据:" + string);
            string = string.substring(4,string.length()-2);
            byte[] returns = ByteTransUtil.hexStringToBytes(string);
//            OCtrlSmokeCT.getInstance().processResult(returns);
        }
    };



}