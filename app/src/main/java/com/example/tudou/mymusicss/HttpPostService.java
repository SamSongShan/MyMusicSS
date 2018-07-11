package com.example.tudou.mymusicss;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 测试接口service-post相关
 * Created by WZG on 2016/12/19.
 * <p>
 * 第一类：HTTP请求方法
 * GET POST PUT DELETE PATCH HEAD OPTIONS
 * 分别对应HTTP的请求方法，都接受一个字符串，
 * 表示接口path与baseUrl组成完整的Url,
 * 不过也可以不指定，结合下面的@Url注解使用，
 * url中可以使用变量如{id}，并使用@Path("id")为{id}提供值
 * HTTP
 * HTTP以外都对应了HTTP标准中的请求方法，而HTTP注解则可以代替以上方法中的任意一个注解,有3个属性：method、path,hasBody
 * public interface BlogService {
 * /**
 * method 表示请求的方法，区分大小写
 * path表示路径
 * hasBody表示是否有请求体
 *
 * @HTTP(method = "GET", path = "blog/{id}", hasBody = false)
 * Call<ResponseBody> getBlog(@Path("id") int id);
 * }
 * 第二类：标记类
 * FormUrlEncoded 表示请求体是一个Form表单
 * Multipart 表示请求体是一个支持文件上传的Form表单
 * Streaming 表示响应体的数据用流的形式返回
 * 如果没有 默认会把数据全部加载到内存，
 * 之后从内存中读数据  （下载必写）
 *
 * 第三类：参数类
 * Headers 用于添加请求头
 * Header  用于添加不固定值得header
 * Body    用于非表单请求体
 *
 * Field FieldMap Part PartMap
 * 用于表单字段
 * Field 和FieldMap 与FormUrlEncoded 注解配合
 * Part 和PartMap与 Multipart注解配合 适合有文件上传的情况
 * FieldMap的接受类型是Map<String, String>
 * 非String 类型会调用其tostring 方法
 * PartMap 的默认接受类型是Map<String,RequestBody>
 * 非RequestBody类型会通过Converter转换
 *
 * Path Query QueryMap Url
 * 用于URL
 * Query 和QueryMap 与 Field 和FieldMap功能一样
 * 不同的是 Query 和QueryMap中的数据体现在Url上
 * 而Field 和FieldMap 的数据是请求体 但生成的数据形式是一样的
 *
 *
 */


public interface HttpPostService {
    @FormUrlEncoded
    @POST("Login")
    Call<String> getAllVedio(@Field("InJson")String json );
    @FormUrlEncoded
    @POST("Login")
    Observable<String> getAllVedioBy(@Field("InJson")String json);

    @FormUrlEncoded
    @POST("Login")
    Observable<String> getAllVedioBys(@Field("InJson")String json);

    /*上传文件*/
    @Multipart
    @POST("UpLoadImg")
    Observable<String> uploadImage(@Part("EnCode")String json, @Part MultipartBody.Part file);

}
