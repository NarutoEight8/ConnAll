package com.naruto.dispatchersample;

import android.content.Context;

import com.naruto.connall.serial.OneSerial;
import com.naruto.connall.serial.SerialManager;

/**
 * 控制板接6口；HC/CO传感器接5口；NO传感器接4口 ；上位机可以用3口测试
 */
public class SerialConnSample extends SerialManager {
    // ========================out======================
    private static SerialConnSample _instance;
    public static SerialConnSample getInstance() {
        if (_instance == null)
            _instance = new SerialConnSample();
        return _instance;
    }
    // ========================out======================


    @Override
    public void closeSerialPort() {
        super.closeSerialPort();
    }

    public void restartSerial(String fromWhere){
        /**GlobalInitBase.onRestartSerial();//此处重设定串口指令发送,口改了指令也会不同**/
//        OCtrlSmokeCT.getInstance().changeCheckValueHex();
//        OCtrlContrlBoard.getInstance().changeCheckValueHex();
//        OCtrlNO.getInstance().changeCheckValueHex();
//        OCtrlCO2.getInstance().changeCheckValueHex();
        new Thread(new Runnable() {
            @Override
            public void run() {
//                changeSerial(0,"烟度计",  OCtrlSmokeCT.getBaudrate(), 80L, new OneSerial.OnSerialListener() {
//                    @Override
//                    public void onDataBack(byte[] data) {
//                        OCtrlSmokeCT.getInstance().processResult(data);
//                    }
//                });
//                //1口无效
//                changeSerial(1,"无效",  OCtrlSmokeCT.getBaudrate(), 80L, null);
//                changeSerial(2,"混柴或NO2_2300",  9600, 200L, new OneSerial.OnSerialListener() {
//                    @Override
//                    public void onDataBack(byte[] data) {
//                        //混检柴油口与Gasboard_2300使用同一个串口
//                        if(GlobalInitBase.getCurrentFAVOR().contains("hqw530") || GlobalInitBase.getCurrentFAVOR().contains("CTS") ) {
//                            if(ManagerMixQiCai.getOnPortInType() == PORTINTYPE_CAI2)OCtrlUPMachine.getInstance().processResult(data, PORTINTYPE_CAI2);//混检机在此协议辨识
//                        }else{
//                            OCtrlNO2.getInstance().processResult(data);
//                        }
//                    }
//                });
//                changeSerial(3,"CO2",  OCtrlCO2.getBaudrate(), 200L, new OneSerial.OnSerialListener() {
//                    @Override
//                    public void onDataBack(byte[] data) {
//                        OCtrlCO2.getInstance().processResult(data);
//                    }
//                });
//                changeSerial(4,"NO",  OCtrlNO.getBaudrate(), 200L, new OneSerial.OnSerialListener() {
//                    @Override
//                    public void onDataBack(byte[] data) {
//                        OCtrlNO.getInstance().processResult(data);
////                Log.e("TAG", "Serial NO data back");
//                    }
//                });
//                changeSerial(5,"上位机",  OCtrlUPMachine.baudrate, 80L, new OneSerial.OnSerialListener() {
//                    @Override
//                    public void onDataBack(byte[] data) {
//                        if(GlobalInitBase.getCurrentFAVOR().contains("hqw530") || GlobalInitBase.getCurrentFAVOR().contains("CTS")) {
//                            if(ManagerMixQiCai.getOnPortInType() == PORTINTYPE_QI5)OCtrlUPMachine.getInstance().processResult(data, PORTINTYPE_QI5);
//                        }else{
//                            OCtrlUPMachine.getInstance().processResult(data, PORTINTYPE_QI5);
//                        }
//                    }
//                });
//                changeSerial(6,"控制板",  OCtrlContrlBoard.baudrate, 80L, new OneSerial.OnSerialListener() {
//                    @Override
//                    public void onDataBack(byte[] data) {
//                        OCtrlContrlBoard.getInstance().processResult(data);
//                    }
//                });
//                super.restartSerial(GlobalInitBase.getContext());//此处会重置
//                OCtrlCO2.getInstance().ccmd_SetBaudRate();//初始化波特率
            }
        }).start();
    }

    // ========================out======================
    public void sendSerialSmoke(String hexStr) {
//        Log.e("SerialSmoke", hexStr);
        sendMessage(0,hexStr);
    }
    public void sendMessageSerialUPMachine(String hexStr) {
//        long prePortType = ManagerMixQiCai.getOnPortInType();
//        int port = prePortType == PORTINTYPE_QI5 ? 5 : 2;
//        sendMessage(port,hexStr);
//        LogMe.showInDebug("发包上位机："+hexStr);
    }
    public void sendMessageSerialNO2(String hexStr) {
        sendMessage(2,hexStr);
    }
    public void sendMessageSerialCO2(String hexStr) {
//        Log.e("sendMessageSerialCO2", hexStr);
        sendMessage(3,hexStr);
    }
    public void sendMessageSerialNO(String hexStr) {
        sendMessage(4,hexStr);
//        Log.e("TAG", "Serial NO data send:"+hexStr);
    }
    public void sendMessageSerialControl(String hexStr) {
        sendMessage(6,hexStr);
    }

}
