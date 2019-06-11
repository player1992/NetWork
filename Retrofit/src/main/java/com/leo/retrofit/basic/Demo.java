package com.leo.retrofit.basic;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.leo.retrofit.GitHubService;
import com.leo.retrofit.bean.Game;
import com.leo.retrofit.bean.Repo;
import com.leo.retrofit.bean.Team;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
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
public class Demo {
    public static void main(String [] args){
        //这些操作都是异步的
        getUserRepos();

        getGames();

        getGamesRx();//RxJava: NBA赛事

        getTeamGames();
//OutPut:
//        Ultra Pull to Refresh for Android. Support all the views.
//        An amazing and convenient Android image slider.
//        A android router middleware that help app navigating to activities and custom services.
//        :books:A pure reading App based on Material Design + MVP + RxJava + Retrofit + Dagger2 + Realm + Glide
//        A Java serialization/deserialization library to convert Java Objects into JSON and back
//        gphs.leo
//        常用类库
//        null
//        Python学习
//        An android library for section headers that stick to the top
//        null
//        niubility
//                ---------------
//                RxJava: NBA赛事
//                ---------------
//                NBA赛事
//                        ---------------
//                火箭_赛程赛果_球队阵容
//                        ---------------
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
                    System.out.println(repo.getDescription());
                }
                System.out.println("---------------");
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private static void getGames(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.avatardata.cn/Nba/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GitHubService service = retrofit.create(GitHubService.class);
        service.listGames().enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                Game body = response.body();
                System.out.println(body.getResult().getTitle());
                System.out.println("---------------");
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private static void getGamesRx(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.avatardata.cn/Nba/")
                //需要告诉Retrofit是RxJava2的版本
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GitHubService service = retrofit.create(GitHubService.class);
        service.listGamesRx()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Game>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Game game) {
                        System.out.println("RxJava: "+game.getResult().getTitle());
                        System.out.println("---------------");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    private static void getTeamGames(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.avatardata.cn/Nba/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("key","98de4264fdd54966bc87c81564fe96c7");
        hashMap.put("team","火箭");
        GitHubService service = retrofit.create(GitHubService.class);
        service.listTeamGames(hashMap).enqueue(new Callback<Team>() {
            @Override
            public void onResponse(Call<Team> call, Response<Team> response) {
                Team body = response.body();
                System.out.println(body.getResult().getTitle());
                System.out.println("---------------");
                //output:火箭_赛程赛果_球队阵容
            }

            @Override
            public void onFailure(Call<Team> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
