package com.leo.network;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

/**
 * <p>Date:2019-10-08.10:36</p>
 * <p>Author:niu bao</p>
 * <p>Desc:</p>
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            NetWorkCallback netWorkCallback = new NetWorkCallback();
            NetworkRequest request = new NetworkRequest.Builder().build();
            ConnectivityManager cmgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            cmgr.registerNetworkCallback(request,netWorkCallback);
        }
    }
}
