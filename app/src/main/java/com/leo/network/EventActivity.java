package com.leo.network;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * <p>Date:2019-09-06.14:20</p>
 * <p>Author:niu bao</p>
 * <p>Desc:直接用Bus.post方法发送事件</p>
 */
public class EventActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
    }

    public void send(View view) {
        OttoBus.getInstance().post(new BusData("send msg from event page"));
        finish();
    }

}
