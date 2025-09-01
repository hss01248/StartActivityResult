package com.hss01248.activityresult2;

import android.content.Intent;

import androidx.annotation.Nullable;

/**
 * time:2020/4/30
 * author:hss
 * desription:
 */
public interface ActivityResultCallback {

     void onActivityResult(int resultCode, @Nullable Intent data);


     void onActivityNotFound(Throwable e) ;
}
