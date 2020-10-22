package com.naruto.connall.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 云端通讯
 */
public interface ApiServiceCloud {

    String HOST = "https://abc.carloginfo.com/";
//    String HOST = "http://abc.carloginfo.com/ashx/TmriOutAccess.ashx";

    @POST("ashx/TmriOutAccess.ashx")
    Call<ResponseBody> sendMessage(@Query("jkid") String jkid,@Query("token") String token,@Query("imei") String imei,@Query("guid") String guid,@Query("data") String data);

    @POST("ashx/TmriOutAccess.ashx")
    Call<ResponseBody> sendMessageWithProduct(@Query("jkid") String jkid,@Query("token") String token,@Query("imei") String imei,@Query("guid") String guid,@Query("data") String data,@Query("productno") String productno,@Query("no2adjval") String no2adjval);

    @FormUrlEncoded
    @POST("ashx/TmriOutAccess.ashx")
    Call<ResponseBody> sendMessageWeightBoard(@Query("jkid") String jkid,@Query("imei") String imei,@Query("mac") String mac,@Query("appid") String appid,@Query("guid") String guid,@Field("data") String data);//@Field表单数据，Query是拼进url
}
