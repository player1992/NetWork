package com.leo.retrofit;

import com.leo.retrofit.bean.Game;
import com.leo.retrofit.bean.Repo;
import com.leo.retrofit.bean.Team;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * <p>Date:2019/5/13.2:31 PM</p>
 * <p>Author:niu bao</p>
 * <p>Desc:在这里创建接口</p>
 */
public interface GitHubService {

    //基本操作
    @GET("NomalRace?key=98de4264fdd54966bc87c81564fe96c7")
    Call<Game> listGames();

    //URL修改，user是占位符
    @GET("users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);

    //RxJava结合的API  1
    @GET("NomalRace?key=98de4264fdd54966bc87c81564fe96c7")
    Observable<Game> listGamesRx();

    //请求参数封装进Map
    @GET("Team")
    Call<Team> listTeamGames(@QueryMap Map<String,String> options);
}
