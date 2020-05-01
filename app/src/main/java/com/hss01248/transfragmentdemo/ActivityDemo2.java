package com.hss01248.transfragmentdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * time:2020/4/30
 * author:hss
 * desription:
 */
public class ActivityDemo2 extends AppCompatActivity {

    public void setData(int data) {
        this.data = data;
    }

    int data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onacreate-data",data+"");
        setContentView(R.layout.activity2);
        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void back() {
        Intent intent = new Intent();
        intent.putExtra("名字","李雷");
        setResult(RESULT_OK,intent);
        finish();
    }
}
