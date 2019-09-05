package com.leo.retrofit.service;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * <p>Date:2019-09-03.14:58</p>
 * <p>Author:niu bao</p>
 * <p>Desc:</p>
 */
public interface UploadService {
    @Multipart
    @POST("users/photo")
    Call<String> uploadFile(@Part MultipartBody.Part file,@Part("description") RequestBody desc);
}
