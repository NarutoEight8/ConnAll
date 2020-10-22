package com.naruto.dispatchersample;

import android.text.TextUtils;
import android.util.Log;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.naruto.connall.serial.TickSingleSend;
import com.nathanli.managers.ManagerCommon;
import com.nathanli.managers.ManagerMixQiCai;
import com.nathanli.managers.ManagerSmoke;
import com.nathanli.managers.ManagerTips;
import com.nathanli.managers.data.DataMuti;
import com.nathanli.myretrofit.BuildConfig;
import com.nathanli.protocols.CtrlMoniSingleSend;
import com.nathanli.protocols.SerialInterface;
import com.nathanli.protocols.road.SocketClientSmoke;
import com.nathanli.toolshelper.GlobalInitBase;
import com.nathanli.toolshelper.TimeDelayTask;
import com.nathanli.toolshelper.datatypetrans.ByteHelper;
import com.nathanli.toolshelper.fileoperate.LogMe;

import static com.nathanli.managers.ManagerMixQiCai.PORTINTYPE_QI5;


/**
 * 烟度下位机
 */

public class OCtrlSerialTickSample extends TickSingleSend{
    // ========================out======================
    private static OCtrlSerialTickSample _instance;
    public static OCtrlSerialTickSample getInstance() {
        if (_instance == null)
            _instance = new OCtrlSerialTickSample();
        return _instance;
    }

    @Override
    public void registerThisOnChangeSerialOrHttp() {

    }

    // ========================out======================
    @Override
    public void changeCheckValueHex(){
//        if (ManagerCommon.getSysSetup().typeSmoke == 10000) {
//            setCheckValueHex(CommSmokeM202.getInstance().getCheckValueHex());
//        }else if (ManagerCommon.getSysSetup().typeSmoke == 10001) {
//            setCheckValueHex(CommSmokeH601.getInstance().getCheckValueHex());
//        }else if (ManagerCommon.getSysSetup().typeSmoke == 10002) {
//            setCheckValueHex(CommSmokeF100.getInstance().getCheckValueHex());
//        }else if (ManagerCommon.getSysSetup().typeSmoke == 10003) {
//            setCheckValueHex(CommSmokeM200.getInstance().getCheckValueHex());
//        }
    }
    @Override
    public void sendMessageOnTick(){
        String sendStr= getClearHexStrToSend();
        if(TextUtils.isEmpty(sendStr)){
            Log.d("TAG", "sendMessageOnTick: 发包未设数据");
            return;
        }
        if(false){//是路检的机socket
//            SocketClientSmoke.getInstance().sendMessageSocket(sendStr);
        }else{//是自身的串口
            SerialConnSample.getInstance().sendSerialSmoke(sendStr);
        }
    }
    // ========================out======================
    @Override
    public void processResult(byte[] data) {
        if (data == null) return;
//        if (ManagerCommon.getSysSetup().typeSmoke == 10000) {//M
//            CommSmokeM202.getInstance().onReceiveData(data);
//        } else if (ManagerCommon.getSysSetup().typeSmoke == 10001) {//H
//            CommSmokeH601.getInstance().onReceiveData(data);
//        } else if (ManagerCommon.getSysSetup().typeSmoke == 10002) {//F
//            CommSmokeF100.getInstance().onReceiveData(data);
//        } else if (ManagerCommon.getSysSetup().typeSmoke == 10003) {//M
//            CommSmokeM200.getInstance().onReceiveData(data);
//        }
    }
    // ========================out======================

    public void ccmd_test(String hexStr){
        putTickMessge(hexStr);
    }

}
