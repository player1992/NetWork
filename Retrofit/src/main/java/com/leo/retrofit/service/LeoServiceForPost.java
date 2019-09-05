package com.leo.retrofit.service;

import com.leo.retrofit.bean.Id;
import com.leo.retrofit.bean.Leo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * <p>Date:2019-09-03.09:56</p>
 * <p>Author:niu bao</p>
 * <p>Desc:API</p>
 */
public interface LeoServiceForPost {

    @FormUrlEncoded
    @POST("post/getUser")
    //Field必须和FormUrlEncoded一起使用，表名是一次表单提交
    Call<Leo> getUserInfo(@Field("id") String id);

    @POST("post/getUser")//Retrofit 会把ID转换为String
    Call<Leo> getUserInfoWithBody(@Body Id id);
}
