package com.leo.okhttp.demo;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * <p>Date:2019-08-30.15:24</p>
 * <p>Author:niu bao</p>
 * <p>Desc:401认证处理</p>
 */
public class Authorization {

    public static void main(String [] args){

        OkHttpClient httpClient;
        //处理401未验证,

        httpClient = new OkHttpClient.Builder().authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                //会一直重试
                System.out.println("authenticate ： " + response);
                System.out.println("challenges ： " + response.challenges());
                // 可以使用 response.priorResponse() 计算次数 或者直接返回null处理
                if (responseCount(response) > 3) {
                    return null;
                }
                //authenticate ： Response{protocol=http/1.1, code=401, message=Unauthorized, url=https://publicobject.com/secrets/hellosecret.txt}
                //challenges ：[Basic realm="OkHttp Secrets" charset="ISO-8859-1"]
                String basic = Credentials.basic("jesse", "password");
                return response
                        .request()
                        .newBuilder()
                        .header("Authorization", basic)
                        .build();
            }
        }).build();
        Request request = new Request.Builder()
                .url("http://publicobject.com/secrets/hellosecret.txt")
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("----------");
                System.out.println(response.body().string());
            }
        });
    }

    private static int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }

}
