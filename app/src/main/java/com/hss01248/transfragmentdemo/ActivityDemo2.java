package com.hss01248.transfragmentdemo;

import android.os.Bundle;
import android.util.Log;

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
    }
}
