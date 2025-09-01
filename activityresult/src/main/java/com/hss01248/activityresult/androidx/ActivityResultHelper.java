package com.hss01248.activityresult.androidx;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.blankj.utilcode.util.LogUtils;
import com.hss01248.activityresult.ActivityResultListener;

import java.util.HashMap;
import java.util.Set;
import java.util.WeakHashMap;

public class ActivityResultHelper {
    // 静态回调引用
    private static HashMap<Integer,ActivityResultListener> callbackMap = new HashMap<>();

    // 启动流程的入口方法
    public static void startForResult(Context context, Intent targetIntent, ActivityResultListener callback) {

        ComponentName componentName = targetIntent.resolveActivity(context.getPackageManager());
        if (componentName == null) {
            //找不到对应的activity
            callback.onActivityNotFound(new Throwable("Activity not found"));
            return;
        }
        if (componentName.getPackageName().equals(context.getPackageName())) {
            // 启动的activity是本app的
            LogUtils.d("启动的activity是本app的",componentName.getClassName());
        }else {
            LogUtils.d("启动的activity是第三方的",componentName.getPackageName(),componentName.getClassName());
        }
        callbackMap.put(targetIntent.hashCode(),callback);
        // 启动透明Activity
        LogUtils.d("intent hash0",targetIntent.hashCode());
        Intent intent = new Intent(context, TransparentActivity.class);
        intent.putExtra("target_intent", targetIntent);
        intent.putExtra("target_intent_hash", targetIntent.hashCode());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    // 供TransparentActivity调用的回调方法
    static void onActivityResult(int resultCode, Intent data,int intentHashCode) {
        LogUtils.d("intent hash1",intentHashCode);
        ActivityResultListener remove = callbackMap.remove(intentHashCode);

        if (remove == null){
            LogUtils.w("ActivityResultListener callback is null",intentHashCode);
            return;
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                remove.onActivityResult(0,resultCode,data);
            }
        },1000);

    }
    /**
     * 创建一个与原Intent数据相同但完全独立的新Intent
     * @param originalIntent 原始Intent
     * @return 复制后的新Intent
     */
    public static Intent forkIntent(Intent originalIntent) {
        if (originalIntent == null) {
            return new Intent();
        }

        // 创建新的Intent
        Intent newIntent = new Intent();

        // 复制动作
        newIntent.setAction(originalIntent.getAction());

        // 复制数据和类型
        newIntent.setDataAndType(originalIntent.getData(), originalIntent.getType());

        // 复制类别
        Set<String> categories = originalIntent.getCategories();
        if (categories != null) {
            for (String category : categories) {
                newIntent.addCategory(category);
            }
        }

        // 复制Extras数据
        Bundle extras = originalIntent.getExtras();
        if (extras != null) {
            // 使用clone()确保数据完全独立
            newIntent.putExtras((Bundle) extras.clone());
        }

        // 复制flags
        newIntent.setFlags(originalIntent.getFlags());

        // 复制组件信息（可选，根据需要决定是否保留）
        newIntent.setComponent(originalIntent.getComponent());

        return newIntent;
    }
}
