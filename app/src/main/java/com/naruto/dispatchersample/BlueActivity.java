package com.naruto.dispatchersample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.naruto.connall.blue.BluePermission;
import com.naruto.connall.blue.blue_2_0.BlueConn_2_0;
import com.naruto.connall.blue.blue_2_0.DeviceDiscoveryActivity;
import com.naruto.dispatchersample.databinding.ActivityBlueBinding;


public class BlueActivity extends AppCompatActivity{
    public static final int REQUEST_BLUE_DEVICE = 52368;
    private ActivityBlueBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBlueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//R.layout.activity_blue
        BluePermission.checkBlueFirstInPermission(this);
        initViews();
        initEvents();
    }
    public void initViews() {
        BlueConnection.getInstance().setOnBlueStateChangeListener(onBlueStateChangeListener);
    }

    public void initEvents() {
        binding.btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BlueConnection.getInstance().isBlueEnable()) {  //如果蓝牙服务不可用则提示
                    Log.d("TAG", "打开蓝牙中...");
                    return;
                }
                //如未连接设备则打开DeviceListActivity进行设备搜索
                if (!BlueConnection.getInstance().isBlueConnected()) {
                    Intent serverIntent = new Intent(BlueActivity.this, DeviceDiscoveryActivity.class); //跳转程序设置
                    startActivityForResult(serverIntent, REQUEST_BLUE_DEVICE);  //设置返回宏定义
                } else {//是点了断开
                    BlueConnection.getInstance().closeConn();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        BlueConnection.getInstance().closeConn();
        super.onDestroy();
    }
    //接收活动结果，响应startActivityForResult()  不要回调去activity

    /***
     1，接收回调的Activity 的启动模式是singleTask,接受onActivityResult 的时候会有问题。
     2，在fragment启动Activity 时调用了getActivity().startSctivityForResult,然后会回调到fragment 所在的activity 的onActivityResult。
     3，在fragment中调用startActivityForResult,当fragment有多层嵌套的时候也会回调到宿主activity 的nActivityResult 。
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_BLUE_DEVICE:     //连接结果
                // 响应返回结果
                if (resultCode == Activity.RESULT_OK) {   //连接成功，由DeviceListActivity设置返回
                    // MAC地址，由DeviceListActivity设置返回
                    String address = data.getExtras().getString(DeviceDiscoveryActivity.EXTRA_DEVICE_ADDRESS);
                    BlueConnection.getInstance().onScanedDevice(address);
                }
                break;
        }
    }


    //==============================================================

    BlueConnection.OnBlueStateChangeListener onBlueStateChangeListener = new BlueConnection.OnBlueStateChangeListener() {
        @Override
        public void onConnectOK(final String name, final String address) {
            BlueActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.txtBlueName.setText(name);
                    binding.txtAddress.setText(address);
                }
            });
        }

        @Override
        public void onConnectFail(final String reason) {
            BlueActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.txtReceive.setText(reason);
                    binding.txtBlueName.setText("");
                    binding.txtAddress.setText("");
                }
            });
        }

        @Override
        public void onDisConnected() {
            BlueActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.txtBlueName.setText("");
                    binding.txtAddress.setText("");
                }
            });
        }

        @Override
        public void onDataBack(byte[] bytes) {
//            W1= 32766##
//            W1=     W2= 25923##
//            W1=
            String result = new String(bytes);
            result = result.replaceAll("[ #\r\n]", "");
            String[] arr = result.split("W");
            for (int i = 0; i < arr.length; i++) {
                String line = arr[i];
                String[] arrLine = line.split("=");
                if (arrLine.length == 2 && !TextUtils.isEmpty(arrLine[0]) && !TextUtils.isEmpty(arrLine[1])) {
                    int num = Integer.valueOf(arrLine[0]);
                    int value = Integer.valueOf(arrLine[1]);
//                    if (num == 1) ManagerWeightBoard.cachePort1 = value;
//                    if (num == 2) ManagerWeightBoard.cachePort2 = value;
//                    LogMe.showInDebug("num:" + num + " value:" + value);
                }
            }
        }
    };
}
