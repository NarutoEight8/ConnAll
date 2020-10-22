package com.naruto.connall.retrofit;

import android.net.Uri;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 打印到电脑通讯格式 http post
 *
 * >>>>1001 检测结果数据上传
 *
 *    发送 :
 *    cmd : 1001 (int)此命令号表示：仅上传数据，有可能已上传过，需要覆盖修改
 *    user: imei号(string) 用来区分哪个用户的数据，后期可能要做登录,目前使用imei设备唯一序列号代替
 *    time: 时间戳 (long)当前时间的豪秒数 用来确定上传时间，纠错使用
 *    data: (jsonArray) 所有的数据,是一个列表，里面包括一个或多个jsonObject,去掉即时数据(目前只能发一条)
 *
 *    接收:
 *    cmd : 1001 (int)此命令号表示：仅上传数据，有可能已上传过，需要覆盖修改
 *    code: 0 成功 1失败
 *    message: 时间戳 (long)当前时间的豪秒数 用来确定上传时间，纠错使用
 *    data:
 *
 */

/**
 * 查询电脑已保存数据通讯格式
 *
 * >>>>1002 查询电脑已保存数据
 * 发送 ：
 *
 * cmd : 1002 (int)此命令号表示：查询电脑已保存数据
 * user: imei号(string) 用来区分哪个用户的数据，后期可能要做登录,目前使用imei设备唯一序列号代替
 * time: 时间戳 (long)当前时间的豪秒数 用来确定上传时间，纠错使用
 * data: (jsonObject) 所有的数据
 *       data jsonObject说明:只有二条数据 serialStart(string) + serialEnd(string)
 *
 * 接收:
 *
 * cmd : 1002 (int)此命令号表示：查询电脑已保存数据
 * user: imei号(string) 用来区分哪个用户的数据，后期可能要做登录,目前使用imei设备唯一序列号代替
 * time: 时间戳 (long)当前时间的豪秒数 用来确定上传时间，纠错使用
 * data: (JsonArray) 所有的数据
 *
 * 接收data JsonArray 说明：
 *
 * 是一个列表，里面包括一个或多个jsonObject
 * jsonObject 数据内容为:
 *            "serialNumber":"1594889064468"(数据实际上是时间戳long)
 *            "numberOrder":"20200904210860"(监测单单号)
 *            "numberReport":"FDL2020001"(监测报告单号)
 */

/**
 * 请求电脑生成报告单号,一般用于PAD无报告单号，需要打印报告单时请求生成单号，以提供打印小票
 * >>>>1003
 *
 * 发送 ：
 *
 * cmd : 1003 (int)此命令号表示：查询电脑已保存数据
 * user: imei号(string) 用来区分哪个用户的数据，后期可能要做登录,目前使用imei设备唯一序列号代替
 * time: 时间戳 (long)当前时间的豪秒数 用来确定上传时间，纠错使用
 * data: (jsonObject) 所有的数据
 *   data jsonObject说明:"serialNumber":"1594889064468"(数据实际上是时间戳long)
 *
 * 接收：
 *
 * cmd : 1003 (int)此命令号表示：查询电脑已保存数据
 * user: imei号(string) 用来区分哪个用户的数据，后期可能要做登录,目前使用imei设备唯一序列号代替
 * time: 时间戳 (long)当前时间的豪秒数 用来确定上传时间，纠错使用
 * data: (jsonObject) 所有的数据
 *   data jsonObject说明:
 *            "serialNumber":"1594889064468"(数据实际上是时间戳long)
 *            "numberReport":"FDL2020001"(监测报告单号)
 *
 All Retrofit is Sample
 */
public class RetrofitCompPrinter {
    private  static RetrofitCompPrinter _instance;
    //=============================================
    public static RetrofitCompPrinter getInstance() {
        if (_instance == null) {
            _instance = new RetrofitCompPrinter();
        }
        return _instance;
    }
    //=============================================
    public interface onResponseListener{
        void onFailure(Throwable t);
        void onResultBack(ResponseBody responseBody);
    }
    private onResponseListener onResponseListener;
    public void setOnResponseListener(onResponseListener listener){
        onResponseListener = listener;
    }


    public void sendMessageComp(String ip,int port,int cmd,String user,long time, String jsonConv) throws UnsupportedEncodingException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ip+":"+port+"/")////base的网络地址  baseUrl不能为空,且强制要求必需以 / 斜杠结尾
                .addConverterFactory(GsonConverterFactory.create()) //gson转换器
                .callbackExecutor(Executors.newSingleThreadExecutor())//使用单独的线程处理 (这很重要,一般网络请求如果不设置可能不会报错,但是如果是下载文件就会报错)
                .build();

        retrofit.create(ApiServiceCompPrinter.class)
                .sendMessage(cmd,user,time, URLEncoder.encode(jsonConv,"utf-8"))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response != null && response.body() != null) {
                            if(onResponseListener!=null)onResponseListener.onResultBack(response.body());
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if(onResponseListener!=null)onResponseListener.onFailure(t);
                    }
                });
    }

    public static String jsonConvertString(JsonElement jsonele){
        if(jsonele == null)return "";
        String result = null;
        try{
            result = new String(jsonele.toString().getBytes("utf-8"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("RetrofitManager","convertString JsonObject->不支持的编码:"+jsonele.toString());
        }
        return result;
    }

}
