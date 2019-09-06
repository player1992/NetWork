package com.leo.network;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

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


}
