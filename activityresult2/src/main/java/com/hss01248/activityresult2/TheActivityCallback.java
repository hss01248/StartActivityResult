package com.hss01248.activityresult2;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hss01248.activityresult2.androidx.StartActivityResultHelper;

/**
 * time:2020/4/30
 * author:hss
 * desription:
 */
public  class TheActivityCallback implements ActivityResultCallback {


    @Override
    public void onActivityResult( int resultCode, @Nullable Intent data) {
        if (StartActivityResultHelper.debugable) {
            String dataStr = "intent.getData:";
            if(data!=null){
                dataStr += data.getDataString()+",";
                if(data.getExtras() != null){
                    dataStr += data.getExtras().toString();
                }
            }

            Log.i("ActivityResultx", "onActivityResult:"  + ",result:" + resultCode + ",data:" + data+", "+dataStr);
        }
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
