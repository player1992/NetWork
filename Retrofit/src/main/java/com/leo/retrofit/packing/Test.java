package com.leo.retrofit.packing;

import android.widget.GridView;

import com.leo.retrofit.bean.Game;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * <p>Date:2019/5/13.5:31 PM</p>
 * <p>Author:niu bao</p>
 * <p>Desc:</p>
 */
public class Test {
    public static void main(String[] args) {
        ServiceLoader loader = new ServiceLoader();
        loader.getGameListRx().subscribe(new Consumer<List<Game.ResultBean.ListBean>>() {
            @Override
            public void accept(List<Game.ResultBean.ListBean> listBeans) throws Exception {
                System.out.println(listBeans.size());
            }
        });
    }
}
