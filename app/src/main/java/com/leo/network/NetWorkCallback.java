package com.leo.network;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * <p>Date:2019-10-08.10:30</p>
 * <p>Author:niu bao</p>
 * <p>Desc:4.4以上的网络状态监听</p>
 */
//4.4+
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetWorkCallback extends ConnectivityManager.NetworkCallback {

    @Override
    public void onLosing(Network network, int maxMsToLive) {
        super.onLosing(network, maxMsToLive);
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                System.out.println("Wifi 已连接");
            } else {
                System.out.println("其他网络");
            }
        }
    }

    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        System.out.println("onAvailable : " + network.toString());
    }

    @Override
    public void onUnavailable() {
        super.onUnavailable();
        System.out.println("onUnavailable");
    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        System.out.println("onLost : " + network.toString());
    }

}
