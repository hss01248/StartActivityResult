package com.hss01248.transfrag;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * time:2020/4/30
 * author:hss
 * desription:
 */
public abstract class OutActivityResultListener {

     void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         if (StartActivityUtil.debugable) {
             Log.i("onActivityResult", "req:" + requestCode + ",result:" + resultCode + ",data:" + data);
         }
        if(resultCode == Activity.RESULT_OK){
            onResultOK(data);
        }else if(resultCode == Activity.RESULT_CANCELED){
            onResultCancel(data);
        }else {
            onResultOther(data,resultCode);
        }
    }

    protected abstract void onResultOther(Intent data, int resultCode);

    protected void onResultCancel(Intent data) {
        onResultOther(data,Activity.RESULT_CANCELED);
    }

    protected abstract void onResultOK(Intent data);

    protected void onActivityNotFound(Throwable e) {

    }
}
