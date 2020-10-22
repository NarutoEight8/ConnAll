package com.naruto.connall.oksocket;

import android.util.Log;

import com.naruto.connall.ByteTransUtil;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.protocol.IReaderProtocol;
import com.xuhao.didi.core.utils.SLog;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClient;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClientIOCallback;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClientPool;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IServerManager;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IServerShutdown;
import com.xuhao.didi.socket.server.action.ServerActionAdapter;
import com.xuhao.didi.socket.server.impl.OkServerOptions;

import java.nio.ByteOrder;

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
public class ConnSocketServer implements IClientIOCallback {
    private static int LISTENPORT = 4499776;
    private IServerManager mServerManager;
    private OnSocketServerReadListener onSocketServerReadListener;
    public interface OnSocketServerReadListener{
        void onRead(byte[] datas);
    }
    public ConnSocketServer(OnSocketServerReadListener listener){
        this.onSocketServerReadListener = listener;
    }
    //=============================================
//    private static ConnSocketServer      _instance;
//    public static ConnSocketServer getInstance() {
//        if (_instance == null)
//            _instance = new ConnSocketServer();
//        return _instance;
//    }
    //=============================================
    public void resetPort(int usePort){
        LISTENPORT = usePort;
    }
    public void restartServer(){
        OkServerOptions.setIsDebug(true);
        OkSocketOptions.setIsDebug(true);
        SLog.setIsDebug(true);
        stopServer();
        mServerManager = OkSocket.server(LISTENPORT).registerReceiver(adapter);
        startServer();
    }
    ServerActionAdapter adapter = new ServerActionAdapter() {
        @Override
        public void onServerListening(int serverPort) {
            if(BuildConfig.DEBUG)Log.e("ServerCallback", Thread.currentThread().getName() + " onServerListening成功,serverPort:" + serverPort);
        }

        @Override
        public void onClientConnected(IClient client, int serverPort, IClientPool clientPool) {
            if(BuildConfig.DEBUG)Log.e("ServerCallback", Thread.currentThread().getName() + " onClientConnected成功,serverPort:" + serverPort + "--ClientNums:" + clientPool.size() + "--ClientTag:" + client.getUniqueTag());
            client.setReaderProtocol(new IReaderProtocol() {
                @Override
                public int getHeaderLength() {
                    return 3;
                }

                @Override
                public int getBodyLength(byte[] header, ByteOrder byteOrder) {
//                    String hexHead = ByteHelper.bytesToHexString(header);
//                    if("2124".equals(hexHead.substring(0,4))){//是修改的协议错了06-07
//                        return 2+(header[2]&0xff);
//                    }
                    return 1+(header[2]&0xff);
                }
            });
            client.addIOCallback(ConnSocketServer.this);
        }

        @Override
        public void onClientDisconnected(IClient client, int serverPort, IClientPool clientPool) {
            if(BuildConfig.DEBUG)Log.e("ServerCallback", Thread.currentThread().getName() + " onClientDisconnected,serverPort:" + serverPort + "--ClientNums:" + clientPool.size() + "--ClientTag:" + client.getUniqueTag());
            client.removeIOCallback(ConnSocketServer.this);
        }

        @Override
        public void onServerWillBeShutdown(int serverPort, IServerShutdown shutdown, IClientPool clientPool, Throwable throwable) {
            if(BuildConfig.DEBUG)Log.e("ServerCallback", Thread.currentThread().getName() + " onServerWillBeShutdown,serverPort:" + serverPort + "--ClientNums:" + clientPool.size());
            try {
                if(shutdown!=null)shutdown.shutdown();
            }catch (Exception e){
                if(BuildConfig.DEBUG)Log.e("WillBeShutdown",e.toString());
            }
        }

        @Override
        public void onServerAlreadyShutdown(int serverPort) {
            if(BuildConfig.DEBUG)Log.e("ServerCallback", Thread.currentThread().getName() + " onServerAlreadyShutdown,serverPort:" + serverPort);
        }
    };

    private void startServer(){
        if(mServerManager == null)return;
        if (!mServerManager.isLive()) {
            mServerManager.listen();
        }
    }
    public void stopServer(){
        if(mServerManager == null)return;
        if (mServerManager.isLive()) {
            mServerManager.shutdown();
        }
    }
    public void sendMessage(final byte[] datas){
        mServerManager.getClientPool().sendToAll(new ISendable() {
            @Override
            public byte[] parse() {
                return datas;
            }
        });
    }

    @Override
    public void onClientRead(OriginalData originalData, IClient client, IClientPool<IClient, String> clientPool) {
        if(onSocketServerReadListener!=null){
            byte[] bytes = ByteTransUtil.bytesMege(originalData.getHeadBytes(),originalData.getBodyBytes());
            onSocketServerReadListener.onRead(bytes);
        }
//        if(BuildConfig.DEBUG)Log.e("onClientIOServer", " 接收到:" + client.getHostIp() +" HEAD:" + bytesToHexString(originalData.getHeadBytes())+" BODY:"+ bytesToHexString(originalData.getBodyBytes()));
    }

    @Override
    public void onClientWrite(ISendable sendable, IClient client, IClientPool<IClient, String> clientPool) {
        byte[] bytes = sendable.parse();
        if(BuildConfig.DEBUG)Log.e("onClientIOServer","Write:"+ByteTransUtil.bytesToHexString(bytes));
    }

}
