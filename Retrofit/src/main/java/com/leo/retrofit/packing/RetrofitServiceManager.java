package com.leo.retrofit.packing;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <p>Date:2019/5/13.4:50 PM</p>
 * <p>Author:niu bao</p>
 * <p>Desc:API的统一获取</p>
 */
public class RetrofitServiceManager {

    private static final int DEFAULT_TIME_OUT = 5;//超时时间 5s    
    private static final int DEFAULT_READ_TIME_OUT = 10;

    private Retrofit mRetrofit;

    private RetrofitServiceManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);
        HttpCommonInterceptor commonInterceptor = new HttpCommonInterceptor.Builder()
                .addHeaderParams("platform", "Android")
                .addHeaderParams("user", "leo")
                .addHeaderParams("token", "nb")
                .build();
        builder.addInterceptor(commonInterceptor);

        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://api.avatardata.cn/Nba/")
                .client(builder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitServiceManager getInstance() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        public static final RetrofitServiceManager INSTANCE = new RetrofitServiceManager();
    }

    public <T> T createService(Class<T> service){
        return mRetrofit.create(service);
    }
}
