package com.leo.okhttp.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Date:2019-08-29.19:51</p>
 * <p>Author:niu bao</p>
 * <p>Desc:下载文件</p>
 */
public class DownloadApi {

    public static void main(String[] args) {
        download();
    }

    private static void download() {
        String url = "http://localhost:8088/demo.gif";
        Request request = new Request.Builder().url(url).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                File file = new File("/Users/lihua/Desktop", "download.gif");
                    FileOutputStream fileOutputStream;
                if (file != null){
                    fileOutputStream = new FileOutputStream(file);
                    byte[] bytes = new byte[2048];
                    int len =0;
                    while ((len = inputStream.read(bytes))!= -1){
                        fileOutputStream.write(bytes,0,len);
                    }
                    fileOutputStream.flush();
                }
            }
        });
    }
}
