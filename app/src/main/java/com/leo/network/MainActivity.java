package com.leo.network;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.leo.network.intercept.LoggingInterceptor;
import com.squareup.otto.Subscribe;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextEvent;
    private TextView mRegister;
    private TextView mJumo,mJumpAnno;

    private OttoBus instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mTextEvent = findViewById(R.id.text_event);
        mRegister = findViewById(R.id.register);
        mJumo = findViewById(R.id.jump);
        mJumpAnno = findViewById(R.id.jumpAnno);
        mRegister.setOnClickListener(this);
        mJumo.setOnClickListener(this);
        mJumpAnno.setOnClickListener(this);
        instance = OttoBus.getInstance();
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.jump) {
            startActivity(new Intent(this, EventActivity.class));
        } else if (vId == R.id.register) {
            instance.register(this);
        }else if (vId == R.id.jumpAnno) {
            startActivity(new Intent(this, AnnotationEventActivity.class));
        }
    }

    @Subscribe
    public void setEventMsg(BusData data){
        mTextEvent.setText(data.getMsg());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance.unregister(this);
    }


    public void intercept(View view) {
        getTest();
    }

    public static void getTest(){
        String url = "http://www.publicobject.com/helloworld.txt";
        Request.Builder builder = new Request.Builder().url(url);
        builder.method("GET",null);//相当于 builder.get();

        Request request = builder.build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //作用域有差异，作用次数不同
//                .addInterceptor(new LoggingInterceptor())
                .addNetworkInterceptor(new LoggingInterceptor())
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println(result);
            }
        });
    }
}
