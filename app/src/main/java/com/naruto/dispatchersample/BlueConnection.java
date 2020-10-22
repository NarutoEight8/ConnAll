package com.naruto.dispatchersample;

import com.naruto.connall.blue.blue_2_0.BlueConn_2_0;

public class BlueConnection extends BlueConn_2_0 {
    //=================================================================
    private static BlueConnection _instance     = null;
    public static synchronized BlueConnection getInstance() {
        if (_instance == null) {
            _instance = new BlueConnection();
        }
        return _instance;
    }
    //=================================================================
    @Override
    public void init(String UUID) {
        super.init(UUID);
    }

    //======================设侦听器=======================
    public interface OnBlueStateChangeListener{
        void onConnectOK(String name,String address);
        void onConnectFail(String reason);
        void onDisConnected();
        void onDataBack(byte[] bytes);
    }
    private OnBlueStateChangeListener onBlueStateChangeListener;
    public void setOnBlueStateChangeListener(OnBlueStateChangeListener listener){
        onBlueStateChangeListener = listener;
    }
    @Override
    protected void onConnectOK(String name, String address) {
        if(onBlueStateChangeListener!=null)onBlueStateChangeListener.onConnectOK(name, address);
    }

    @Override
    protected void onConnectFail(String reason) {
        if(onBlueStateChangeListener!=null)onBlueStateChangeListener.onConnectFail(reason);
    }

    @Override
    protected void onDisConnected() {
        if(onBlueStateChangeListener!=null)onBlueStateChangeListener.onDisConnected();
    }

    @Override
    protected void onDataBack(byte[] bytes) {
        if(onBlueStateChangeListener!=null)onBlueStateChangeListener.onDataBack(bytes);
    }

    @Override
    protected void sendMessage(byte[] bytes) {
        super.sendMessage(bytes);
    }

    @Override
    public boolean isBlueEnable() {
        return super.isBlueEnable();
    }

    @Override
    public boolean isBlueConnected() {
        return super.isBlueConnected();
    }

    @Override
    public void closeConn() {
        super.closeConn();
    }

    @Override
    public void onScanedDevice(String address) {
        super.onScanedDevice(address);
    }
}
