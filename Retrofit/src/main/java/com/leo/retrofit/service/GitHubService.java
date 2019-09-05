package com.leo.retrofit.service;

import com.leo.retrofit.bean.Repo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * <p>Date:2019/5/13.2:31 PM</p>
 * <p>Author:niu bao</p>
 * <p>Desc:在这里创建接口</p>
 */
public interface GitHubService {


    @DELETE
    //URL修改，user是占位符
    @GET("users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);

}
