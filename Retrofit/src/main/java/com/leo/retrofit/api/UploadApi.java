package com.leo.retrofit.api;

import com.leo.retrofit.service.UploadService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <p>Date:2019-09-03.14:57</p>
 * <p>Author:niu bao</p>
 * <p>Desc:</p>
 */
public class UploadApi {
    public static void main(String [] args){
        File file = new File("/Users/lihua/Desktop","new.png");

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);

        MultipartBody.Part data = MultipartBody.Part.createFormData("photos", file.getName(), requestBody);
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UploadService uploadService = retrofit.create(UploadService.class);
        Call<String> call = uploadService.uploadFile(data,description);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("upload success");
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("error :"+t.getMessage());
            }
        });
    }
}
