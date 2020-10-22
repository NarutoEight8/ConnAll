package com.naruto.connall.oksocket;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.naruto.connall.ByteTransUtil;
import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.protocol.IReaderProtocol;
import com.xuhao.didi.socket.client.impl.client.action.ActionDispatcher;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import cn.shorr.serialport.BuildConfig;


/**
 * 如果您是 Android 请关注下权限
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 * 在Module的build.gradle文件中添加依赖配置
 * dependencies {
 *     //基础的 OkSocket 功能集成包.您的Socket开发无论是客户端还是Java,都需要此包 (必须集成)
 * 	api 'com.tonystark.android:socket:4.1.0'
 * 	//如果您需要使用 OkSocketServer 功能在客户端或者Java程序,您还需要依赖下面的Server插件包和上面的一起依赖.
 * 	api 'com.tonystark.android:socket-server:4.1.0'
 * }
 * OkSocket 支持 JCenter 仓库
 * allprojects {
 *     repositories {
 *         jcenter()
 *     }
 * }
 */
public class ConnSocketClient{
    private String TAG = ConnSocketClient.class.getSimpleName();
    private ConnectionInfo mInfo = new ConnectionInfo("104.238.184.237", 8080);
    private IConnectionManager mManager;
    private String name="";
    private OnSocketClientReadListener onSocketClientReadListener;
    public interface OnSocketClientReadListener{
        void onRead(byte[] datas);
    }

    public void setOnSocketClientReadListener(OnSocketClientReadListener onSocketClientReadListener) {
        this.onSocketClientReadListener = onSocketClientReadListener;
    }
    public ConnSocketClient(String name){
        this.name = name;
    }
    //=============================================
    /***
    private IReaderProtocol readerProtocol= new IReaderProtocol() {
        @Override
        public int getHeaderLength() {
            return 3;
        }

        @Override
        public int getBodyLength(byte[] header, ByteOrder byteOrder) {
            //06 41 15 0102030405060708190001020304050607080903EC06
            return (1+header[2]&0xff);
        }
    } ;
     ***/
    public void resetConn(final String ip,final int port,final IReaderProtocol readerProtocol){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                final Handler handler = new Handler();
                mInfo = new ConnectionInfo(ip, port);
                if(BuildConfig.DEBUG)Log.e(TAG,"reconn:"+ip+" "+port);
                OkSocketOptions mOkOptions = new OkSocketOptions.Builder()
                        .setReconnectionManager(OkSocketOptions.getDefault().getReconnectionManager())
                        .setConnectTimeoutSecond(10)
                        .setCallbackThreadModeToken(new OkSocketOptions.ThreadModeToken() {
                            @Override
                            public void handleCallbackEvent(ActionDispatcher.ActionRunnable runnable) {
                                handler.post(runnable);
                            }
                        })
                        .setReaderProtocol(readerProtocol)
                        .build();
                mManager = OkSocket.open(mInfo).option(mOkOptions);
                mManager.registerReceiver(adapter);
                mManager.connect();
                Looper.loop();
            }
        }).start();
    }

    private SocketActionAdapter adapter = new SocketActionAdapter() {

        @Override
        public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
            checkConnTime = System.currentTimeMillis()+3000L;
            if(BuildConfig.DEBUG)Log.e(TAG,"连接成功(Connecting Successful)");
        }
        @Override
        public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
            if (e != null) {
                if (e instanceof RedirectException) {
                    if(BuildConfig.DEBUG)Log.e(TAG,"正在重定向连接(Redirect Connecting)...");
//                    stopConn();
                    mManager.switchConnectionInfo(((RedirectException) e).redirectInfo);
                    new MyReconnThread().start();
                } else {
                    if(BuildConfig.DEBUG)Log.e(TAG,"异常断开(Disconnected with exception):" + e.getMessage());
//                    stopConn();
                    mManager.switchConnectionInfo(info);
                    new MyReconnThread().start();
                }
            } else {
                if(BuildConfig.DEBUG)Log.e(TAG,"正常断开(Disconnect Manually)");
            }
        }

        @Override
        public void onSocketConnectionFailed(final ConnectionInfo info, String action, Exception e) {
            if(BuildConfig.DEBUG)Log.e(TAG,"连接失败(Connecting Failed)"+ConnSocketClient.this.name);
//            stopConn();
            checkConnTime = Long.MAX_VALUE;
            mManager.switchConnectionInfo(info);
            new MyReconnThread().start();
        }

        @Override
        public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData originalData) {
            byte[] bytes = ByteTransUtil.bytesMege(originalData.getHeadBytes(),originalData.getBodyBytes());
            if(onSocketClientReadListener!=null)onSocketClientReadListener.onRead(bytes);
            if(BuildConfig.DEBUG)Log.e(TAG, " 接收到:" + " HEAD:" + ByteTransUtil.bytesToHexString(originalData.getHeadBytes())+" BODY:"+ ByteTransUtil.bytesToHexString(originalData.getBodyBytes()));
        }

        @Override
        public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
            byte[] bytes = data.parse();
            if(BuildConfig.DEBUG)Log.e(TAG,"发送了:"+ByteTransUtil.bytesToHexString(bytes));
        }

        @Override
        public void onPulseSend(ConnectionInfo info, IPulseSendable data) {
//            byte[] bytes = data.parse();
//            JsonParser().parse(str).getAsJsonObject();
        }
    };
    private long checkConnTime = Long.MAX_VALUE;

    public void startConn(){
        if(mManager == null)return;
        if (!mManager.isConnect()) {
            mManager.connect();
        }
    }
    public void stopConn(){
        if(mManager == null)return;
        if (mManager.isConnect()) {
            mManager.disconnect();
        }
    }
    public void sendMessage(final String hexStr){
        if(mManager == null || mInfo == null)return;
        if (!mManager.isConnect())return;
        if(checkConnTime == Long.MAX_VALUE)return;
        if(checkConnTime > System.currentTimeMillis())return;
//        if(BuildConfig.DEBUG)Log.e(TAG,"正在发送:"+hexStr);
        mManager.send(new ISendable() {
            @Override
            public byte[] parse() {
                return ByteTransUtil.hexStringToBytes(hexStr);
            }
        });
    }
    public void sendMessage(final byte[] datas){
        if(mManager == null || mInfo == null)return;
        if (!mManager.isConnect())return;
        if(checkConnTime == Long.MAX_VALUE)return;
        if(checkConnTime > System.currentTimeMillis())return;
//        if(BuildConfig.DEBUG)Log.e(TAG,"正在发送:datas");
        mManager.send(new ISendable() {
            @Override
            public byte[] parse() {
                return datas;
            }
        });
    }
    public void onDestroy() {
        if (mManager != null) {
            mManager.disconnect();
            mManager.unRegisterReceiver(adapter);
        }
    }


    private static long Thread_singleId;
    class MyReconnThread extends Thread {
        private long thread_singleId_check;
        public MyReconnThread() {
            long now = System.currentTimeMillis();
            Thread_singleId = now;
            thread_singleId_check = now;
        }
        public void run() {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(thread_singleId_check != Thread_singleId)return;
            if(mManager.isConnect())return;
            if(mManager == null)return;
            mManager.connect();
            if(BuildConfig.DEBUG)Log.e(TAG,"断线重连");
        }
    }

}
