package com.leo.okhttp.demo;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * <p>Date:2019-08-29.19:44</p>
 * <p>Author:niu bao</p>
 * <p>Desc:Post API</p>
 */
public class PostApi {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MD = MediaType.parse("text/x-markdown; charset=utf-8");
    static String url = "http://10.1.133.96/leo/post/getUser";

    public static void main(String [] args){
        postTest();
    }

    private static void postTest() {
        String postData = "json markdown";
        RequestBody requestBody = RequestBody.create(JSON, postData);
        //POST请求的builder
        FormBody formBody = new FormBody.Builder()
                .add("id", "leo")
                .build();
        //封装请求头 请求方法 URL 缓存策略
        Request request = new Request.Builder()
                //请求地址
                .url(url)
                //请求头
                .header("User-Agent","leoHttp")
                .header("CacheControl","no-cache")
                //请求方法
                .post(formBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                System.out.println("Date:"+response.header("Date"));
                System.out.println("Content-Length:"+response.header("Content-Length"));
                System.out.println("Connection:"+response.header("Connection"));
                System.out.println("Content-Type:"+response.header("Content-Type"));
                System.out.println(response.body().string());
            }
        });
    }
}
