package com.naruto.connall.retrofit;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 All Retrofit is Sample
 */
public class RetrofitManager {
    private  static RetrofitManager _instance;
    //=============================================
    public static RetrofitManager getInstance() {
        if (_instance == null) {
            _instance = new RetrofitManager();
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


    public void sendMessageCloud(String jkid,String token,String imei,String guid, JsonObject data,String productno,String no2adjval){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(com.naruto.connall.retrofit.ApiServiceCloud.HOST)////base的网络地址  baseUrl不能为空,且强制要求必需以 / 斜杠结尾
                .addConverterFactory(GsonConverterFactory.create()) //gson转换器
                .callbackExecutor(Executors.newSingleThreadExecutor())//使用单独的线程处理 (这很重要,一般网络请求如果不设置可能不会报错,但是如果是下载文件就会报错)
                .build();

        retrofit.create(com.naruto.connall.retrofit.ApiServiceCloud.class)
                .sendMessageWithProduct(jkid,token,imei,guid,convertString(data),productno,no2adjval)
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

    public void sendMessageWeightBoard(String jkid,String imei,String mac,String appid,String guid, JsonObject data){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(com.naruto.connall.retrofit.ApiServiceCloud.HOST)////base的网络地址  baseUrl不能为空,且强制要求必需以 / 斜杠结尾
                .addConverterFactory(GsonConverterFactory.create()) //gson转换器
                .callbackExecutor(Executors.newSingleThreadExecutor())//使用单独的线程处理 (这很重要,一般网络请求如果不设置可能不会报错,但是如果是下载文件就会报错)
                .build();

        retrofit.create(com.naruto.connall.retrofit.ApiServiceCloud.class)
                .sendMessageWeightBoard(jkid,imei,mac,appid,guid,convertString(data))
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

    public static String convertString(JsonObject jsonObject){
        if(jsonObject == null)return "";
        String result = null;
        try{
            result = new String(jsonObject.toString().getBytes("utf-8"), "utf-8");
//            result = URLEncoder.encode(jsonObject.toString(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("RetrofitManager","convertString JsonObject->不支持的编码:"+jsonObject.toString());
        }
        return result;
    }

}
