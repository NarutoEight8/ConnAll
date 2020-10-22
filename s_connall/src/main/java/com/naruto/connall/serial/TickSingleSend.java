package com.naruto.connall.serial;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 模拟单片机发送数据的类
 */

public abstract class TickSingleSend {
    private static final int LISTSIZE = 20;
    private String normalCheckValueHexStr="";//一直发的取数指令
    private List<String> sendQueue = new ArrayList<>(LISTSIZE);//需要发送的队列

    /**设置默认值 每秒Tick发这个**/
    public void setCheckValueHex(String hexStr){
        if(TextUtils.isEmpty(hexStr))return;
        normalCheckValueHexStr = hexStr;
    }
    /**获取的同时就删除掉了，同消息队列**/
    protected String getClearHexStrToSend(){
        if(sendQueue == null || sendQueue.size() == 0)return normalCheckValueHexStr;
        String string = sendQueue.get(0).replace(" ","");
        sendQueue.remove(0);
//        LogMe.showInDebug("获取发送的数据:"+string);
        return string;
    }

    /**设置加入的消息，发包后就没了**/
    public void putTickMessge(String hexStr){
        if(TextUtils.isEmpty(hexStr))return;
        sendQueue.add(hexStr);
    }
    public abstract void registerThisOnChangeSerialOrHttp();
    public abstract void changeCheckValueHex();
    public abstract void sendMessageOnTick();
    public abstract void processResult(byte[] data);
}
