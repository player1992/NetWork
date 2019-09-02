package com.leo.okhttp.demo;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * <p>Date:2019-08-29.19:51</p>
 * <p>Author:niu bao</p>
 * <p>Desc:上传API</p>
 */
public class UploadApi {

    public static void main(String[] args) {
        upload();
    }

    private static void upload() {
        File file = new File("/Users/lihua/Desktop","flist.md");
        Request request = new Request.Builder().url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MediaType.parse("text/x-markdown;charset=utf-8"), file))
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
                System.out.println(response.body().string());
            }
        });
    }
}
