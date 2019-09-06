package com.leo.retrofit.service;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;

/**
 * <p>Date:2019-09-03.14:58</p>
 * <p>Author:niu bao</p>
 * <p>Desc:</p>
 */
public interface UploadService {
    @Multipart
    @POST("users/photo")
    Call<String> uploadFile(@Part MultipartBody.Part file,@Part("description") RequestBody desc);

    @Streaming
    @GET
    Call<ResponseBody> download();
}

