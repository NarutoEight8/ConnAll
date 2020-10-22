package com.naruto.connall.retrofit;

import android.util.Log;

import com.google.gson.JsonElement;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * http:// ip:port/Ashx/VehicleInsAccess.ashx。其中IP标识Web服务器地址；port对应系统应用服务的端口号，启用80的不填写。
 请求参数:
 token:令牌
 jkid:接口ID
 data:请求json内容

 返回数据
 {" jkid ": "接口id",
 "code":"1",
 "message":"信息",
 "data": "JSON数据"}
 返回说明:
 jkid:接口ID,请求的接口id
 code:代号(1:成功,0:失败)
 message:具体说明
 所有字段类型为字符串

 All Retrofit is Sample
 */
public class RetrofitCheChiPao {
    private static String IP = "";
    private static int PORT = 0;
    private static String PAGE = "/Ashx/VehicleInsAccess.ashx";
    private static String TOKEN= "";
    private  static RetrofitCheChiPao _instance;

    //=============================================
    public static RetrofitCheChiPao getInstance() {
        if (_instance == null) {
            _instance = new RetrofitCheChiPao();
        }
        return _instance;
    }
    public void init(String IP, int PORT, String token){
        this.IP = IP;
        this.PORT = PORT;
        this.TOKEN = token;
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


    public void sendMessageComp(String jkid,String jsonData){
        try {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+IP+":"+PORT+PAGE)////base的网络地址  baseUrl不能为空,且强制要求必需以 / 斜杠结尾
                .addConverterFactory(GsonConverterFactory.create()) //gson转换器
                .callbackExecutor(Executors.newSingleThreadExecutor())//使用单独的线程处理 (这很重要,一般网络请求如果不设置可能不会报错,但是如果是下载文件就会报错)
                .build();

        retrofit.create(ApiServiceCheChiPao.class)
                .sendMessage(TOKEN,jkid, URLEncoder.encode(jsonData,"utf-8"))
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
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
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
