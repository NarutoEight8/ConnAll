package com.naruto.connall.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 云端通讯
 */
public interface ApiServiceCompPrinter {

    @POST("ashx/TmriOutAccess.ashx")
    Call<ResponseBody> sendMessage(@Query("cmd") int cmd, @Query("user") String user, @Query("time") long time, @Query("data") String data);

}
