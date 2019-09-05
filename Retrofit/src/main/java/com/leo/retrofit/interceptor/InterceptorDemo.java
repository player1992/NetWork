package com.leo.retrofit.interceptor;

import com.leo.retrofit.service.GitHubService;
import com.leo.retrofit.bean.Repo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <p>Date:2019/5/13.11:31 AM</p>
 * <p>Author:niu bao</p>
 * <p>Desc:基本使用</p>
 */
public class InterceptorDemo {
    public static void main(String [] args){
        //这些操作都是异步的
        getUserRepos();

    }

    //结合OKHTTP的公共参数使用
    private void common(){
        // 创建 OKHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);//连接超时时间
        builder.writeTimeout(10,TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(10,TimeUnit.SECONDS);//读操作超时时间

        // 添加公共参数拦截器
        BasicParamsInterceptor basicParamsInterceptor = new BasicParamsInterceptor.Builder()
                .addHeaderParam("userName","")//添加公共参数
                .addHeaderParam("device","")
                .build();
        builder.addInterceptor(basicParamsInterceptor);
    }

    private static void getUserRepos(){
        //创建实例和配置
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GitHubService service = retrofit.create(GitHubService.class);
        service.listRepos("player1992").enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                List<Repo> body = response.body();
                for (Repo repo : body) {
                    System.out.println(repo.getName());
                }
                System.out.println("---------------");
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
