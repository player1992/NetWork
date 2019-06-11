package com.leo.retrofit.packing;

import io.reactivex.Observable;

/**
 * 重复操作封装
 */
public class ObjectLoader {
 /**
   * 
   * @param observable     
   * @param <T>   
   * @return    
   */   
 protected  <T> Observable<T> observe(Observable<T> observable){
    return observable
            //Java测试需要注释
//      .subscribeOn(Schedulers.io())
//      .unsubscribeOn(Schedulers.io())
//      .observeOn(AndroidSchedulers.mainThread())
            ;
  }
}