package com.leo.network;

import com.squareup.otto.Bus;

/**
 * <p>Date:2019-09-06.14:26</p>
 * <p>Author:niu bao</p>
 * <p>Desc:</p>
 */
public class OttoBus extends Bus {
    private static volatile OttoBus mInstance;
    private OttoBus() {
    }

    public static OttoBus getInstance(){
        if (mInstance == null){
            synchronized (OttoBus.class){
                if (mInstance == null){
                    mInstance = new OttoBus();
                }
            }
        }
        return mInstance;
    }

}
