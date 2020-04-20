package com.leo.okhttp.demo;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Date:2020-04-18.20:46</p>
 * <p>Author:niu bao</p>
 * <p>Desc:证书签名验证
 * </p>
 */
public class CertificateTest {
    public static void main(String [] args){
        String hostname = "www.baidu.com/";

        CertificatePinner certificatePinner = new CertificatePinner.Builder()
                .add(hostname, "sha256/YBo/npMPiC3PCrMqVUOvC+PT5L9wfJ9iwLSapvdzSs4=")
                .add(hostname, "sha256/IQBnNBEiFuhj+8x6X8XLgh01V9Ic5/V3IRQLNFFc7v4=")
                .add(hostname, "sha256/K87oWBWM9UZfyddvDfoxL+8lpNyoUB2ptGtn0fv6G2Q=")
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .certificatePinner(certificatePinner)
                .build();

        Request request = new Request.Builder()
                .url("https://" + hostname)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Success");
            }
        });
    }
}
