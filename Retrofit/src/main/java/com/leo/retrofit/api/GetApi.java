package com.leo.retrofit.api;

import com.leo.retrofit.bean.Leo;
import com.leo.retrofit.service.LeoServiceForGet;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <p>Date:2019-09-03.09:59</p>
 * <p>Author:niu bao</p>
 * <p>Desc:GetApi使用</p>
 */
public class GetApi {

    private static String url ="http://10.1.133.96/leo/";

    public static void main(String [] args){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LeoServiceForGet service = retrofit.create(LeoServiceForGet.class);

        getUserInfo(service);
        getUserInfoWithPath(service);
        getUserInfoWithQuery(service);
        getUserInfoWithQueryMap(service);

    }
    private static void getUserInfo(LeoServiceForGet service){
        service.getUserInfo().enqueue(new Callback<Leo>() {
            @Override
            public void onResponse(Call<Leo> call, Response<Leo> response) {
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<Leo> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private static void getUserInfoWithQuery(LeoServiceForGet service){
        service.getUserInfoWithQuery("leo").enqueue(new Callback<Leo>() {
            @Override
            public void onResponse(Call<Leo> call, Response<Leo> response) {
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<Leo> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private static void getUserInfoWithQueryMap(LeoServiceForGet service){
        Map<String,String> map = new HashMap<>();
        map.put("id","leo");
        service.getUserInfoWithQueryMap(map,"2dfire").enqueue(new Callback<Leo>() {
            @Override
            public void onResponse(Call<Leo> call, Response<Leo> response) {
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<Leo> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private static void getUserInfoWithPath(LeoServiceForGet service){
        service.getUserInfoWithPath("get").enqueue(new Callback<Leo>() {
            @Override
            public void onResponse(Call<Leo> call, Response<Leo> response) {
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<Leo> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
