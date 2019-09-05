package com.leo.retrofit.api;

import com.leo.retrofit.bean.Id;
import com.leo.retrofit.bean.Leo;
import com.leo.retrofit.service.LeoServiceForPost;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <p>Date:2019-09-03.14:02</p>
 * <p>Author:niu bao</p>
 * <p>Desc:</p>
 */
public class PostApi {
    static String url ="http://10.1.133.96/leo/";

    public static void main(String [] args){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LeoServiceForPost service = retrofit.create(LeoServiceForPost.class);
//        getUSerInfoPost(service);
        getUSerInfoPostBody(service);

    }

    private static void getUSerInfoPost(LeoServiceForPost serviceForPost){
        serviceForPost.getUserInfo("leo").enqueue(new Callback<Leo>() {
            @Override
            public void onResponse(Call<Leo> call, Response<Leo> response) {
                System.out.println(response.body().toString());
            }

            @Override
            public void onFailure(Call<Leo> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private static void getUSerInfoPostBody(LeoServiceForPost serviceForPost){
        serviceForPost.getUserInfoWithBody(new Id("leo")).enqueue(new Callback<Leo>() {
            @Override
            public void onResponse(Call<Leo> call, Response<Leo> response) {
                System.out.println(response.body().toString());
            }

            @Override
            public void onFailure(Call<Leo> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
