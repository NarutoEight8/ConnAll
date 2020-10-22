package com.naruto.connall.retrofit;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.ResponseBody;
@Deprecated
public class HttpConn {
//    //=============================================
//    public static void sendMessage(String guid,JsonObject params) {
//        if(!"gettoken".equals(guid) && TextUtils.isEmpty(OCtrlCloud.getTOKEN())){
//            Log.e("<<<Http>>>", "发包>>>>  TOKEN:isEmpty");
//            OCtrlCloud.getInstance().ccmd_TokenGet();
//            return;
//        }
//        if (params == null) params = new JsonObject();
//        try {
//            String da = URLEncoder.encode(params.toString(),"utf-8");
//            Log.e("<<<Http>>>", "发包>>>>  guid:"+guid+" token:"+OCtrlCloud.getTOKEN()+"  params:"+params.toString());
//            RetrofitManager.getInstance().sendMessageCloud(guid,OCtrlCloud.getTOKEN(),SystemMe.getMac(GlobalContext.getContext()),guid,da);
//            RetrofitManager.getInstance().setOnResponseListener(new RetrofitManager.onResponseListener() {
//                @Override
//                public void onFailure(Throwable t) {
//                    ToastTextShow.show("HTTP超时,请检查是否连接网络，及安装天线!");
//                }
//
//                @Override
//                public void onResultBack(ResponseBody responseBody) {
//                    try {
//                        String resultBack = responseBody.string();
//                        OCtrlCloud.getInstance().processResult(resultBack);
//                        Log.e("<<<HTTP>>>", "收包>>>>: body:" + resultBack);
//                    } catch (Exception e) {
//                        if(com.nathanli.myretrofit.BuildConfig.DEBUG) Log.e("HTTP", "onResponse ERROR:" + e.toString());
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//    }





}

