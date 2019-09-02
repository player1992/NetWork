package com.leo.okhttp.demo;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * <p>Date:2019-08-30.11:24</p>
 * <p>Author:niu bao</p>
 * <p>Desc:</p>
 */
public class OtherSetting {
    static ScheduledExecutorService executorService;

    static {

        executorService = Executors.newScheduledThreadPool(1);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            cancel();
        }
    }

    private static void cancel() {
        String url = "http://www.baidu.com";

        Request request = new Request.Builder().url(url)
                .cacheControl(CacheControl.FORCE_NETWORK)//每次请求都会走网络
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        final Call call = okHttpClient.newCall(request);

        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                call.cancel();
            }
        }, 150, TimeUnit.MILLISECONDS);//时间如果短一点，每次请求就会取消了

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure :" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != response.cacheResponse()) {
                    String cache = response.cacheResponse().toString();
                    System.out.println("-------cache-------");
                    System.out.println(cache);
                } else {
                    System.out.println("-------network-------");
                    String string = response.networkResponse().toString();
                    System.out.println(string);
                }
            }
        });
    }

    /**
     * 设置缓存时间
     */
    public static void setConnectionTime() {
        File externalCache = new File("");
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectionSpecs
                        (Arrays.asList(ConnectionSpec.MODERN_TLS,
                                ConnectionSpec.COMPATIBLE_TLS))
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(externalCache, cacheSize));
        OkHttpClient okHttpClient = builder.build();

    }

    public void cancelRequest() {


    }
}
