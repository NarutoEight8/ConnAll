package com.naruto.connall.retrofit;

import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * http://域名/项目名/common?funid=x&rd=系统时间毫秒数
 * funid：接口id【生成规则：模块编号（两位数字，从10开始）+接口编号（两位数字，从00开始）】
 */
@Deprecated
public class HttpConnBK {
//    private static final String BASEURL = "http://192.168.1.102:8077/";
//
//    private static HttpConnBK             sClient;
//    private OkHttpClient client;
//    //=============================================
//    public static HttpConnBK getInstance() {
//        if (sClient == null) {
//            sClient = new HttpConnBK();
//        }
//        return sClient;
//    }
//    //=============================================
//    private String ip;
//    private int port;
//    private int cmd;
//    private String user;
//    private long time;
//    private String jsonConv;
//    public void sendMessage(String ip,int port,int cmd,String user,long time, String jsonConv) {
//        this.ip = ip;
//        this.port = port;
//        this.cmd = cmd;
//        this.user = user;
//        this.time = time;
//        this.jsonConv = jsonConv;
//        new HttpThread().start();
//    }
//    private class HttpThread extends Thread {
//
//        public void run() {
////            String subscription = "common?funid=" + protocol + "&rd=" + System.currentTimeMillis();
//            String useUrl       = BASEURL.concat("");
//            try {
//                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
//                OkHttpClient mOkHttpClient = new OkHttpClient();
////                String da = URLEncoder.encode(params.toString(),"utf-8");
//                RequestBody formBody = new FormBody.Builder()
//                        .add("cmd", String.valueOf(cmd))
//                        .add("user", user)
//                        .add("time", String.valueOf(time))
//                        .add("jsonConv", jsonConv)
//                        .build();
//                Request request = new Request.Builder()
//                        .url(useUrl)
//                        .post(formBody)
//                        .build();
//                okhttp3.Call call = mOkHttpClient.newCall(request);
//                call.enqueue(new okhttp3.Callback() {
//                    @Override
//                    public void onFailure(okhttp3.Call call, IOException e) {
//                        Log.e("HTTP", "onFailure:" + e.toString());
//                    }
//                    @Override
//                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
//                        if (response != null && response.body() != null) {
//                            try {
//                                String resultBack = response.body().string();
//                                Log.e("<<<HTTP>>>", "收包>>>>: body:" + resultBack);
//                            } catch (Exception e) {
//                                if(BuildConfig.DEBUG) Log.e("HTTP", "onResponse ERROR:" + e.toString());
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                });
//            } catch (Exception e) {
//                Log.e("HTTP", "onFailure:" + e.toString());
//            }
//
//        }
//    }


}

