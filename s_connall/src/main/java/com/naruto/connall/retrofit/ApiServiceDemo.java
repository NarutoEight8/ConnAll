package com.naruto.connall.retrofit;

import android.app.Activity;
import android.database.Observable;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by xiaoyehai on 2018/4/4 0004.
 * 定义一个接口（封装URL地址和数据请求）
 * <p>
 * 1. @FormUrlEncoded:表示请求实体是一个Form表单，每个键值对需要使用@Field注解
 * 2. @Multipart：表示请求实体是一个支持文件上传的Form表单，需要配合使用@Part,适用于 有文件 上传的场景
 * 3. @Streaming:表示响应体的数据用流的方式返回，适用于返回的数据比较大，该注解在在下载大文件的特别有用
 * 4. @HTTP：通用注解,可以替换以上所有的注解，其拥有三个属性：method，path，hasBody
 * <p>
 * 5. @Query	用于Get中指定参数
 * 6. @QueryMap	和Query使用类似
 * 7. @Path	用于url中的占位符
 * 8. @Url	指定请求路径
 * <p>
 * 9. @Filed	多用于post请求中表单字段,Filed和FieldMap需要FormUrlEncoded结合使用
 * 10. @FiledMap	和@Filed作用一致，用于不确定表单参数
 * 11. @Body	多用于post请求发送非表单数据,比如想要以post方式传递json格式数据
 * <p>
 * 12.@Part	用于表单字段,Part和PartMap与Multipart注解结合使用,适合文件上传的情况
 * 13.@PartMap	用于表单字段,默认接受的类型是Map<String,REquestBody>，可用于实现多文件上传
 * <p>
 * 14 @Headers	用于添加固定请求头，可以同时添加多个。通过该注解添加的请求头不会相互覆盖，而是共同存在
 * 15 @Header	作为方法的参数传入，用于添加不固定值的Header，该注解会更新已有的请求头
 */


public interface ApiServiceDemo {
//    /**
//     * 1.get无参请求
//     * https://api.github.com/users/basil2style
//     */
//    @GET("basil2style")
//    Call<ResponseBody> getbasil2style();
//
//    //Call<String> getbasil2style2();
//
//    /**
//     * 2.get有参请求
//     * http://qt.qq.com/php_cgi/news/php/varcache_getnews.php?id=12&page=0&plat=android&version=9724
//     */
//    @GET("news/php/varcache_getnews.php")
//    Call<ResponseBody> getNewsInfo(@Query("id") String id,
//                                   @Query("page") String page,
//                                   @Query("plat") String plat,
//                                   @Query("version") String version);
//
//    /**
//     * 3.gson转换器
//     * http://qt.qq.com/php_cgi/news/php/varcache_getnews.php?id=12&page=0&plat=android&version=9724
//     */
//
//    @GET("news/php/varcache_getnews.php")
//    Call<BaseEntity<List<NewsInfo>>> getNewsInfo2(@Query("id") String id,
//                                                  @Query("page") String page,
//                                                  @Query("plat") String plat,
//                                                  @Query("version") String version);
//
//    /**
//     * 4.@Path:用于url中的占位符,所有在网址中的参数（URL的问号前面）
//     * http://qt.qq.com/php_cgi/news/php/varcache_getnews.php?id=12&page=0&plat=android&version=9724
//     */
//    @GET("news/{php}/varcache_getnews.php")
//    Call<BaseEntity<List<NewsInfo>>> getNewsInfoPath(@Path("php") String php,
//                                                     @Query("id") String id,
//                                                     @Query("page") String page,
//                                                     @Query("plat") String plat,
//                                                     @Query("version") String version);
//
//    /**
//     * 5.@QueryMap:参数太多时可以用@QueryMap封装参数,相当于多个@Query
//     * http://qt.qq.com/php_cgi/news/php/varcache_getnews.php?id=12&page=0&plat=android&version=9724
//     */
//
//    @GET("news/php/varcache_getnews.php")
//    Call<BaseEntity<List<NewsInfo>>> getNewsInfo3(@QueryMap Map<String, String> map);
//
//
//    /**
//     * 6.post请求;
//     * url：http://zhushou.72g.com/app/game/game_list/
//     * params:platform=2&page=1
//     * FieldMap：多个参数时可以使用，类型@QueryMap
//     * FormUrlEncoded：表示请求实体是一个Form表单，每个键值对需要使用@Field注解
//     * http://qt.qq.com/php_cgi/news/php/varcache_getnews.php?id=12&page=0&plat=android&version=9724
//     */
//    @FormUrlEncoded
//    @POST("app/game/game_list/")
//    Call<NewsInfo> getGameInfo(@Field("platform") String platform,
//                               @Field("page") String page);
//
//    /**
//     * 7.body注解：上传json格式的数据
//     *
//     * @param url
//     * @param Body
//     * @return
//     */
//    @POST()
//    Call<ResponseBody> getNewsInfoByBody(@Url String url, @Body RequestBody Body);
//
//    /**
//     * 直接传入实体,它会自行转化成Json，这个转化方式是GsonConverterFactory定义的。
//     *
//     * @param url
//     * @param newsInfo
//     * @return
//     */
//    @POST("api/{url}/newsList")
//    Call<BaseEntity<NewsInfo>> login(@Path("url") String url, @Body NewsInfo newsInfo);
//
//    //上传json格式的数据,也可以使用Map集合，加上body注解
//    @POST("users/update")
//    Observable<NewsInfo> updateUserInfo(@Header("token") String token, @Body Map<String, Object> map);
//
//    /**
//     * 8.若需要重新定义接口地址，可以使用@Url，将地址以参数的形式传入即可。如
//     *
//     * @param url
//     * @param map
//     * @return
//     */
//    @GET
//    Call<List<Activity>> getActivityList(@Url String url, @QueryMap Map<String, String> map);
//
//    /**
//     * 9.使用@Headers添加多个请求头
//     * 用于添加固定请求头，可以同时添加多个。通过该注解添加的请求头不会相互覆盖，而是共同存在
//     *
//     * @param url
//     * @param map
//     * @return
//     */
//    @Headers({
//            "User-Agent:android",
//            "apikey:123456789",
//            "Content-Type:application/json",
//    })
//    @POST()
//    Call<BaseEntity<NewsInfo>> post(@Url String url, @QueryMap Map<String, String> map);
//
//
//    /**
//     * 10.@Header注解：
//     * 作为方法的参数传入，用于添加不固定值的Header，该注解会更新已有的请求头
//     *
//     * @param token
//     * @param activeId
//     * @return
//     */
//    @GET("mobile/active")
//    Call<BaseEntity<NewsInfo>> get(@Header("token") String token, @Query("id") int activeId);
//
//
//    /**
//     * 11.@HTTP注解：
//     * method 表示请求的方法，区分大小写
//     * path表示路径
//     * hasBody表示是否有请求体
//     */
//    @HTTP(method = "GET", path = "blog/{id}", hasBody = false)
//    Call<ResponseBody> getBlog(@Path("id") int id);
//
//    /**
//     * 12.Streaming注解:表示响应体的数据用流的方式返回，适用于返回的数据比较大，该注解在在下载大文件的特别有用
//     */
//    @Streaming
//    @GET
//    Call<BaseEntity<NewsInfo>> downloadPicture(@Url String fileUrl);
//
//
//    ///////上传单张图片//////
//
//    /**
//     * Multipart：表示请求实体是一个支持文件上传的Form表单，需要配合使用@Part,适用于 有文件 上传的场景
//     * Part:用于表单字段,Part和PartMap与Multipart注解结合使用,适合文件上传的情况
//     * PartMap:用于表单字段,默认接受的类型是Map<String,REquestBody>，可用于实现多文件上传
//     * Part 后面支持三种类型，{@link RequestBody}、{@link okhttp3.MultipartBody.Part} 、任意类型；
//     *
//     * @param file 服务器指定的上传图片的key值
//     * @return
//     */
//
//    @Multipart
//    @POST("upload/upload")
//    Call<BaseEntity<NewsInfo>> upload1(@Part("file" + "\";filename=\"" + "test.png") RequestBody file);
//
//    @Multipart
//    @POST("xxxxx")
//    Call<BaseEntity<NewsInfo>> upload2(@Part MultipartBody.Part file);
//
//    //////////上传多张图片/////////
//
//    /**
//     * @param map
//     * @return
//     */
//    @Multipart
//    @POST("upload/upload")
//    Call<BaseEntity<NewsInfo>> upload3(@PartMap Map<String, RequestBody> map);
//
//    @Multipart
//    @POST("upload/upload")
//    Call<BaseEntity<NewsInfo>> upload4(@PartMap Map<String, MultipartBody.Part> map);
//
//
//    //////图文混传/////
//
//    /**
//     * @param params
//     * @param files
//     * @return
//     */
//    @Multipart
//    @POST("upload/upload")
//    Call<BaseEntity<NewsInfo>> upload5(@FieldMap() Map<String, String> params,
//                                       @PartMap() Map<String, RequestBody> files);
//
//    /**
//     * Part 后面支持三种类型，{@link RequestBody}、{@link okhttp3.MultipartBody.Part} 、任意类型；
//     *
//     * @param userName
//     * @param passWord
//     * @param file
//     * @return
//     */
//    @Multipart
//    @POST("xxxxx")
//    Call<BaseEntity<NewsInfo>> upload6(@Part("username") RequestBody userName,
//                                       @Part("password") RequestBody passWord,
//                                       @Part MultipartBody.Part file);
//
//
//    /**
//     * 15.rxjava
//     * http://qt.qq.com/php_cgi/news/php/varcache_getnews.php?id=12&page=0&plat=android&version=9724
//     */
//
//    @GET("news/php/varcache_getnews.php")
//    Observable<BaseEntity<List<NewsInfo>>> getNewsInfoByRxJava(@Query("id") String id,
//                                                               @Query("page") String page,
//                                                               @Query("plat") String plat,
//                                                               @Query("version") String version);

}
