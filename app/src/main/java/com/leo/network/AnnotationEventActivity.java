package com.leo.network;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.squareup.otto.Produce;

/**
 * <p>Date:2019-09-06.14:20</p>
 * <p>Author:niu bao</p>
 * <p>Desc:使用注解发送事件，需要注册和反注册当前页面</p>
 */
public class AnnotationEventActivity extends Activity {

    private OttoBus instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        instance = OttoBus.getInstance();
        instance.register(this);
    }

    public void send(View view) {
        finish();
    }

    @Produce
    public BusData setInitData(){
        return new BusData("msg set by annotation method");

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance.unregister(this);
    }
}
