package com.leo.okhttp.demo;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Date:2019-08-29.19:15</p>
 * <p>Author:niu bao</p>
 * <p>Desc:OkHttp 的Get请求</p>
 */
public class GetApi {
    private static final String url = "http://10.1.133.96/integral-api/park_coupon/v1/benefit_set";
    public static void main(String [] args){
        getTest();
    }
    public static void getTest(){

        Request.Builder builder = new Request.Builder().url(url);
        builder.method("GET",null);//相当于 builder.get();

        Request request = builder.build();
        OkHttpClient okHttpClient = new OkHttpClient();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println(result);
            }
        });
    }
}
