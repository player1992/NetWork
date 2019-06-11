package com.leo.retrofit.packing;

import com.leo.retrofit.GitHubService;
import com.leo.retrofit.bean.Game;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * <p>Date:2019/5/13.5:21 PM</p>
 * <p>Author:niu bao</p>
 * <p>Desc:</p>
 */
public class ServiceLoader extends ObjectLoader {
    private GitHubService mService;

    public ServiceLoader() {
        mService = RetrofitServiceManager.getInstance().createService(GitHubService.class);
    }


    public Observable<List<Game.ResultBean.ListBean>> getGameListRx() {
        return
                observe(mService.listGamesRx())
                        .map(new Function<Game, List<Game.ResultBean.ListBean>>() {
                            @Override
                            public List<Game.ResultBean.ListBean> apply(Game game){
                                return game.getResult().getList();
                            }
                        });

    }
}
