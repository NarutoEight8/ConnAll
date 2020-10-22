package com.naruto.connall.blue;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * 放到onCreate里就好
 * BluePermission.checkBlueFirstInPermission(this);
 */
public class BluePermission {

    private static BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //获取本地蓝牙适配器，即蓝牙设备
    private static final int GRANTED = PackageManager.PERMISSION_GRANTED;
    public static final int MY_PERMISSIONS_REQUEST_CODE = 10001;


    /**初进Activity直接取权限，权限检测完了要bluetoothAdapter.enable()**/
    public static void checkBlueFirstInPermission(Activity context) {  //检查蓝牙权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) != GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) != GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != GRANTED) {
                ActivityCompat.requestPermissions((Activity) context
                        , new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}
                        , MY_PERMISSIONS_REQUEST_CODE);
            }
        }

        //如果打开本地蓝牙设备不成功，提示信息，结束程序
        if (_bluetooth == null){
            Toast.makeText(context, "无法打开手机蓝牙，请确认手机是否有蓝牙功能！", Toast.LENGTH_LONG).show();
            return;
        }

        // 设置设备可以被搜索
        new Thread(){
            public void run(){
                if(_bluetooth.isEnabled()==false){
                    _bluetooth.enable();
                }
            }
        }.start();
    }

}
