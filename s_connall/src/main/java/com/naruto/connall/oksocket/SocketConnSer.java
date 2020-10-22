package com.naruto.connall.oksocket;

import android.util.Log;


import com.naruto.connall.ByteTransUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 这是TCP底层SocketChannel连接，服务端不支持tcpClient阻塞连接和WEbSocket连接
 * socket链接 cmd1: cmd2:心跳 cmd3:服务器回包
 */
@Deprecated
public class SocketConnSer {
    private static int     CONN_TIMEOUT    = 13000;
    private Selector        mSelector;//信道选择器
    private SocketChannel   mChannel;//信道

    private ReceiveWatchThread readThread;

    //=================================================================
    private static SocketConnSer _instance     = null;
    public static synchronized SocketConnSer getInstance() {
        if (_instance == null) {
            _instance = new SocketConnSer();
        }
        return _instance;
    }

    /**
     * Socket连接是否是正常的
     */
    public boolean isConnected() {//是正在连接，连接上的
        boolean isConnect = false;
        if (mChannel!=null)isConnect = mChannel.isConnected();
        return isConnect;
    }
    private synchronized boolean repareRead() {
        boolean bRes = false;
        if(mChannel!=null) {
            try {
                mSelector = Selector.open();//打开并注册选择器到信道
                mChannel.register(mSelector, SelectionKey.OP_READ);
                bRes = true;
            } catch (Exception e) {
                Log.e("Socket", "repareRead Exception:" + e.toString());
            }
        }
        return bRes;
    }
    public void close() {
        try {
            if (mSelector != null)mSelector.close();
            if (mChannel != null)mChannel.close();
        } catch (Exception e) {
            Log.e("Socket","close Exception:"+e.toString());
        }
    }
//=============================initOK==============================
    /**
     * 关闭socket 重新连接,试试能不能walklock
     */
    private static long preReConnTime = 0;
    public void reConnect(final String fromInfo) {
        if(System.currentTimeMillis() - preReConnTime<500L)return;
        preReConnTime = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("SocketReConn", "1-------重连启动:"+fromInfo);
                close();//isConnected = false;
                boolean done = false;
                // 打开监听信道并设置为非阻塞模式
                try {
                    mChannel = SocketChannel.open(new InetSocketAddress("192.168.1.7", 26));
//                    Log.e("SocketReConn", "2-------通道开启成功");
                } catch (IOException e) {
                    Log.e("SocketReConn", "2-------通道开启失败"+e.toString());//一般是网络切换，网络不良
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    reConnect("重连通道开启失败");
                    return;
                }
                try {
                    if (mChannel != null) {
                        mChannel.configureBlocking(false);//非阻塞
                        mChannel.socket().setTcpNoDelay(false);
                        mChannel.socket().setKeepAlive(true);
                        //设置超时时间
                        mChannel.socket().setSoTimeout(CONN_TIMEOUT);

                        // 打开并注册选择器到信道
//                        Log.e("SocketReConn", "3-------打开选择器");
                        Selector selector = Selector.open();
                        if (selector != null) {
                            mChannel.register(selector, SelectionKey.OP_READ);
                            done = true;
//                            Log.e("SocketReConn", "4-------重连完成，准备收发包 repareRead");
                            if (!repareRead()) {//reg mSelector
//                                Log.e("SocketReConn", "4-------重连完成，准备收发包 repareRead 失败");
                                close();
                            Log.e("SocketReConn", "4-------连接失败");
                                return;
                            }
                            readThread = new ReceiveWatchThread(mSelector, System.currentTimeMillis());
                            readThread.start();
                        }else{
                            Log.e("ServiceC", "3-------重连失败 selector null");
                        }
                    }
                }catch (Exception e) {
                    Log.e("ServiceC", "3-------重连失败:"+e.toString());
                } finally {
                    if (!done)close();
                }
            }
        }).start();
    }

    public void sendMessage(final String hexstr) {
//        if(isNetConnChanged(context)){reConnect("change wifi");return;}
        new Thread(new Runnable() {
            @Override
            public void run() {
//                if(cmd == 4)cacheMessage = str;//发指令不要发二次
                if (!isConnected()) {
                    reConnect("Socket发送指令需要重连");
                    return;
                }
                try {
                    ByteBuffer buffer = ByteBuffer.allocate(1024 * 64);
                    buffer.put(ByteTransUtil.hexStringToBytes(hexstr));
                    buffer.flip();
                    int nCount = mChannel.write(buffer);
                    mChannel.socket().setKeepAlive(true);
                } catch (SocketException e) {
                    reConnect("Socket发送指令异常,需要重连1");
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    reConnect("Socket发送指令异常,需要重连2");
                    e.printStackTrace();
                } catch (IOException e) {
                    reConnect("Socket发送指令异常,需要重连3");
                    e.printStackTrace();
                } catch (Exception e) {
                    reConnect("Socket发送指令异常,需要重连4");
                    e.printStackTrace();
                }

            }
        }).start();
    }

    // ================================read Thread===================================

    private static long ReceiveWatchThread_singleId;

    class ReceiveWatchThread extends Thread {
        private Selector mSelector;
        private long     thread_singleId_check;

        public ReceiveWatchThread(Selector selector, final long time) {
            mSelector = selector;
            ReceiveWatchThread_singleId = time;
            thread_singleId_check = time;
        }
        public void run() {
            try {
//                Log.e("SocketConn", "ReceiveWatchThread--->");
                while (thread_singleId_check == ReceiveWatchThread_singleId && isConnected() && mSelector.select() > 0) {
                    for (SelectionKey sk : mSelector.selectedKeys()) {
                        if (sk.isReadable()) {
                            SocketChannel sc         = (SocketChannel) sk.channel();
                            int           lengthTemp = 0;
                            ByteBuffer    buffer     = ByteBuffer.allocate(1024 * 64);
                            while (-1 != (lengthTemp = sc.read(buffer))) { // read方法并不保证一次能读取1024*64个字节
                                if (lengthTemp == 0) break;
                                buffer.flip();
                                byte[] enData = new byte[lengthTemp];
                                buffer.get(enData, 0, lengthTemp);
                                buffer.clear();//重置
                                //读完一组
                                Log.d("TAG", "收到数据"+ ByteTransUtil.bytesToHexString(enData));
                            }
                            // 为下一次读取作准备
                            sk.interestOps(SelectionKey.OP_READ);
                        }
                        // 删除正在处理的SelectionKey
                        mSelector.selectedKeys().remove(sk);
                    }
                }
            } catch (Exception e) {
//                Log.e("SocketConn receive", e.toString());
                reConnect("SocketConn receive exception"+e.toString());
            }
        }
    }

    // ======================================================
    // ===================================================================
}