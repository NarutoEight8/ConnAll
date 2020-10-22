package com.naruto.connall.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 云端通讯
 */
public interface ApiServiceCheChiPao {

    @POST("ashx/TmriOutAccess.ashx")
    Call<ResponseBody> sendMessage(@Query("token") String token, @Query("jkid") String jkid, @Query("data") String data);

}
