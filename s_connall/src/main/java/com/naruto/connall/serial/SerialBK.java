package com.naruto.connall.serial;

/**
 * 控制板接6口；HC/CO传感器接5口；NO传感器接4口 ；上位机可以用3口测试
 */
@Deprecated
public class SerialBK {
//    private SerialPortUtil serialPortUtil;
//    private SerialRead serialReadNO, serialReadCO2,serialContrlBoard,serialUPMachine,serialUPMachine2Ko,serialSmoke;
//    private final static OSingleThreadPool threadPoolNO = new OSingleThreadPool();
//    private final static OSingleThreadPool threadPoolNO2 = new OSingleThreadPool();
//    private final static OSingleThreadPool threadPoolCO2 = new OSingleThreadPool();
//    private final static OSingleThreadPool threadPoolConB = new OSingleThreadPool();
//    private final static OSingleThreadPool threadPoolUPMch = new OSingleThreadPool();
//    private final static OSingleThreadPool threadPoolSmoke = new OSingleThreadPool();
//    // ========================out======================
//    private static OCtrlSerial _instance;
//
//    public static OCtrlSerial getInstance() {
//        if (_instance == null)
//            _instance = new OCtrlSerial();
//        return _instance;
//    }
//
//    // ========================out======================
//    public void checkSerialList() {
//        SerialPortFinder serialPortFinder = new SerialPortFinder();
//        String[] devices = serialPortFinder.getAllDevicesPath();
//        if (devices != null) for (String str : devices) {
//            Log.e("TAG", str);
//        }
//    }
//
//    public void closeSerialPort() {
//        if(serialReadNO!=null)serialReadNO.unRegisterListener();
//        serialReadNO = null;
//        if(serialReadCO2 !=null) serialReadCO2.unRegisterListener();
//        serialReadCO2 = null;
//        if(serialContrlBoard !=null) serialContrlBoard.unRegisterListener();
//        serialContrlBoard = null;
//        if(serialUPMachine!=null)serialUPMachine.unRegisterListener();
//        serialUPMachine = null;
//        if(serialUPMachine2Ko != null) serialUPMachine2Ko.unRegisterListener();
//        serialUPMachine2Ko = null;
//        if(serialSmoke != null) serialSmoke.unRegisterListener();
//        serialSmoke = null;
//
//        if (serialPortUtil != null) serialPortUtil.unBindService();
//        serialPortUtil = null;
//    }
//
//    /**NO:4,CO2:3,Board:6,UP:5,Smoke:0*/
//    public void linkSerialPort() {
//        //配置串口参数
//        serialPortUtil = new SerialPortUtil(GlobalContext.getContext()
//                , new SerialPortConfig("/dev/ttyS0", OCtrlSmokeCT.getBaudrate())//port0 0口烟度计
//                , new SerialPortConfig("/dev/ttyS1", 9600)//port1 1口无效
//                , new SerialPortConfig("/dev/ttyS2", 9600)//port2 2口混柴或Gasboard_2300
//                , new SerialPortConfig("/dev/ttyS3", OCtrlCO2.getBaudrate()) //port3 3口CO2
//                , new SerialPortConfig("/dev/ttyS4", OCtrlNO.getBaudrate()) //port4 4口NO
//                , new SerialPortConfig("/dev/ttyS5", OCtrlUPMachine.baudrate)//port5 5口上位机
//                , new SerialPortConfig("/dev/ttyS6", OCtrlContrlBoard.baudrate)//port6 6口控制板
//        );
//        //设置为调试模式，打印收发数据
//        serialPortUtil.setDebug(false);
//        //绑定串口服务
//        serialPortUtil.bindService();
//
//        serialSmoke = new SerialRead(GlobalContext.getContext());
//        serialSmoke.registerListener(0, new SerialRead.ReadDataListener() {
//            @Override
//            public void onReadData(byte[] data) {
//                OCtrlSmokeCT.getInstance().processResult(data);
//            }
//        });
//        serialUPMachine2Ko = new SerialRead(GlobalContext.getContext());
//        serialUPMachine2Ko.registerListener(2, new SerialRead.ReadDataListener() {
//            @Override
//            public void onReadData(byte[] data) {
//                //混检柴油口与Gasboard_2300使用同一个串口
//                if(BuildConfig.FLAVOR.contains("hqw530")) {
//                    if(OCtrlUPMachine.getOnPortInType() == PORTINTYPE_CAI2)
//                        OCtrlUPMachine.getInstance().processResult(data,OCtrlUPMachine.PORTINTYPE_CAI2);//混检机在此协议辨识
//                }else{
//                    OCtrlNO2.getInstance().processResult(data);
//                }
//            }
//        });
//        serialReadCO2 = new SerialRead(GlobalContext.getContext());
//        serialReadCO2.registerListener(3, new SerialRead.ReadDataListener() {
//            @Override
//            public void onReadData(byte[] data) {
////                if(BuildConfig.DEBUG)Log.e("SerialCO2","收到"+ByteHelper.bytesToHexString(data));
//                OCtrlCO2.getInstance().processResult(data);
//            }
//        });
//        serialReadNO = new SerialRead(GlobalContext.getContext());
//        serialReadNO.registerListener(4, new SerialRead.ReadDataListener() {
//            @Override
//            public void onReadData(byte[] data) {
//                OCtrlNO.getInstance().processResult(data);
//            }
//        });
//        serialUPMachine = new SerialRead(GlobalContext.getContext());
//        serialUPMachine.registerListener(5, new SerialRead.ReadDataListener() {
//            @Override
//            public void onReadData(byte[] data) {
//                if(BuildConfig.FLAVOR.contains("hqw530")) {
//                    if(OCtrlUPMachine.getOnPortInType() == PORTINTYPE_QI5)
//                        OCtrlUPMachine.getInstance().processResult(data, PORTINTYPE_QI5);
//                }else{
//                    OCtrlUPMachine.getInstance().processResult(data, PORTINTYPE_QI5);
//                }
//            }
//        });
//        serialContrlBoard = new SerialRead(GlobalContext.getContext());
//        serialContrlBoard.registerListener(6, new SerialRead.ReadDataListener() {
//            @Override
//            public void onReadData(byte[] data) {
//                OCtrlContrlBoard.getInstance().processResult(data);
//            }
//        });
////        OCtrlUPMachine.init();
//        OCtrlCO2.getInstance().ccmd_SetBaudRate();//初始化波特率
//        Log.e("TAG", "connect");
//    }
//    public void sendSerialSmoke(final String hexStr) {
//        threadRun(threadPoolSmoke,0,hexStr,80L);
//    }
//    public void sendMessageSerialUPMachine(final String hexStr) {
//        long prePortType = OCtrlUPMachine.getOnPortInType();
//        int port = prePortType == PORTINTYPE_QI5 ? 5 : 2;
//        if(ViewTestFunc.SAVE_UPMACHINE_INFO)LogMe.putLogFull(GlobalContext.getContext(), "__SEND:"+hexStr);
////        SerialWrite.sendData(GlobalContext.getContext(), port, ByteHelper.hexStringToBytes(hexStr));
//        threadRun(threadPoolUPMch,port,hexStr,80L);
//    }
//    public void sendMessageSerialNO2(final String hexStr) {
//        threadRun(threadPoolNO2,2,hexStr,200L);
//    }
//    public void sendMessageSerialCO2(String hexStr) {
////        if(BuildConfig.DEBUG)Log.e("SerialCO2","发出"+hexStr);
//        threadRun(threadPoolCO2,3,hexStr,200L);
//    }
//    public void sendMessageSerialNO(String hexStr) {
//        threadRun(threadPoolNO,4,hexStr,200L);
//    }
//    public void sendMessageSerialControl(final String hexStr) {
//        threadRun(threadPoolConB,6,hexStr,80L);
//    }
//    private void threadRun(OSingleThreadPool singleThreadPool,final int port,final String hexStr,final long deley){
//        if(singleThreadPool == null || TextUtils.isEmpty(hexStr))return;
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(deley);//加上延时防止指令冲突
//                    SerialWrite.sendData(GlobalContext.getContext(), port, ByteHelper.hexStringToBytes(hexStr));
////                    if(port==6)Log.e("Serial","控制泵 size:"+threadPoolConB.getPoolSize()+"   发出:"+hexStr);
//                }catch (Exception e){
//                    Log.e("ExceptionTread","threadPoolControl"+e.toString()+"\n"+e.getMessage());
//                    return;
//                }
//            }
//        };
//        singleThreadPool.putRunnable(runnable);
//    }
}
