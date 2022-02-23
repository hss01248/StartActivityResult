package com.hss01248.activityresult;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * time:2020/4/30
 * author:hss
 * desription:
 */
public interface ActivityResultListener {

    /**
     *
     * @param fragment
     * @param intent
     * @return 返回值代表是否拦截原默认行为. 如果
     * 调用处为: if(!listener.onInterceptStartIntent(this,bean)){
     *                 startActivityForResult(bean,requestCode);
     *             }
     */
    default boolean onInterceptStartIntent(@NonNull Fragment fragment, @Nullable Intent intent, int requestCode){
        return false;
    }

    /**
     * 在里面自己处理
     * @param requestCode
     * @param resultCode
     * @param data
     */
     void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);





     void onActivityNotFound(Throwable e) ;
}
