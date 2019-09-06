package com.leo.retrofit.service;

import com.leo.retrofit.bean.Leo;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * <p>Date:2019-09-03.09:56</p>
 * <p>Author:niu bao</p>
 * <p>Desc:API</p>
 */
public interface LeoServiceForGet {
    @GET("get/getUser?id=leo")
    Call<Leo> getUserInfo();

    @GET("get/getUser?id=leo")
    Observable<Leo> getUserInfoRx();

    @GET("get/getUser")//请求入参可以由Query注解指定，参数传入即可
    @Headers({
            "User-Agent: Retrofit"
    })//多个Header添加
    Call<Leo> getUserInfoWithQuery(@Query("id") String id);

    @GET("get/getUser")//多个入参可以使用QueryMap，简洁
    //单个Header动态添加
    Call<Leo> getUserInfoWithQueryMap(@QueryMap Map<String,String> options,@Header("Location") String loacation);


    @GET("{apiPath}/getUser?id=leo")//{apiPath}的值要动态替换，由传入的值替换
    Call<Leo> getUserInfoWithPath(@Path("apiPath") String path);
}
