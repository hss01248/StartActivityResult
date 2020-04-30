package com.hss01248.transfrag;

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
public  class TheActivityListener {



    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }


    public void onActivityStarted(@NonNull Activity activity) {

    }


    public void onActivityResumed(@NonNull Activity activity) {

    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK){
            onResultOK(data);
        }else if(resultCode == Activity.RESULT_CANCELED){
            onResultCancel(data);
        }else {
            onResultOther(data);
        }
    }

    public void onResultOther(Intent data) {

    }

    public void onResultCancel(Intent data) {

    }

    public void onResultOK(Intent data) {

    }


    public void onActivityNotFound(Throwable e) {
        if(e != null){
            e.printStackTrace();
        }

    }
}
