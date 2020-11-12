package com.hss01248.activityresult;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * time:2020/4/30
 * author:hss
 * desription:
 */
public interface ActivityResultListener {

    /**
     * 在里面自己处理
     * @param requestCode
     * @param resultCode
     * @param data
     */
     void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);





     void onActivityNotFound(Throwable e) ;
}
