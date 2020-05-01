package com.hss01248.activityresult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * time:2020/4/30
 * author:hss
 * desription:
 */
public  class TheActivityListener<T extends Activity> implements OutActivityResultListener{



    protected void onActivityCreated(@NonNull T activity, @Nullable Bundle savedInstanceState) {

    }


    protected void onActivityStarted(@NonNull T activity) {

    }


    protected void onActivityResumed(@NonNull T activity) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK){
            onResultOK(data);
        }else if(resultCode == Activity.RESULT_CANCELED){
            onResultCancel(data);
        }else {
            onResultOther(data,resultCode);
        }
    }

    protected void onResultOther(Intent data, int resultCode) {

    }

    protected void onResultCancel(Intent data) {

    }

    protected void onResultOK(Intent data) {

    }


    @Override
    public void onActivityNotFound(Throwable e) {


    }
}
