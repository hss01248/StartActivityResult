package com.hss01248.activityresult2.androidx;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.util.Log;


import com.hss01248.activityresult2.ActivityResultCallback;

import java.util.HashMap;

public class StartActivityResultHelper {

    public static boolean debugable;
    static boolean hasInit;

    static void init(ApplicationInfo info) {
        if(hasInit){
            return;
        }
        debugable = isAppDebugable(info);
        hasInit = true;
    }

    static boolean isAppDebugable(ApplicationInfo info) {
        try {
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // 静态回调引用
    private  static HashMap<Integer, ActivityResultCallback> callbackMap = new HashMap<>();

    // 启动流程的入口方法

    /**
     * Consider adding a <queries> declaration to your manifest when calling this method; see https://g.co/dev/packagevisibility for details
     * @param context
     * @param targetIntent
     * @param callback
     */
    public static void startForResult(Context context, Intent targetIntent, ActivityResultCallback callback) {
        init(context.getApplicationInfo());
        ComponentName componentName = targetIntent.resolveActivity(context.getPackageManager());
        if (componentName == null) {
            //找不到对应的activity
            callback.onActivityNotFound(new Throwable("Activity not found"));
            return;
        }
        if (componentName.getPackageName().equals(context.getPackageName())) {
            // 启动的activity是本app的
            if(debugable){
                Log.d("ActivityResultx","启动的activity是本app的,"+componentName.getClassName());
            }
        }else {
            if(debugable){
                Log.d("ActivityResultx","启动的activity是第三方的,"+componentName.getPackageName()+","+componentName.getClassName());
            }
        }
        callbackMap.put(targetIntent.hashCode(),callback);
        // 启动透明Activity
        if(debugable){
            Log.v("ActivityResultx","intent hash0,"+targetIntent.hashCode());
        }
        if(Looper.myLooper() == Looper.getMainLooper()){
            startActi(context, targetIntent);
        }else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    startActi(context, targetIntent);
                }
            });
        }
    }

    private static void startActi(Context context, Intent targetIntent) {
        try{
            Intent intent = new Intent(context, TransparentActivity.class);
            intent.putExtra("target_intent", targetIntent);
            intent.putExtra("target_intent_hash", targetIntent.hashCode());
            context.startActivity(intent);
            clearPendingTransition(context);
        }catch (Throwable throwable){
            throwable.printStackTrace();
            try{
                Intent intent = new Intent(context, TransparentActivity.class);
                intent.putExtra("target_intent", targetIntent);
                intent.putExtra("target_intent_hash", targetIntent.hashCode());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                clearPendingTransition(context);
            }catch (Throwable throwable2){
                throwable2.printStackTrace();
                ActivityResultCallback remove = callbackMap.remove(targetIntent.hashCode());
                if(remove != null){
                    remove.onActivityNotFound(throwable2);
                }else {
                    if(debugable){
                        Log.w("ActivityResultx","ActivityResultListener callback is null,"+ targetIntent.hashCode());
                    }
                }
            }
        }
    }

    private static void clearPendingTransition(Context context) {
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }


    // 供TransparentActivity调用的回调方法
    static void onActivityResult(int resultCode, Intent data,int intentHashCode) {
        if(debugable){
            Log.v("ActivityResultx","intent hash1,"+intentHashCode);
        }
        ActivityResultCallback remove = callbackMap.remove(intentHashCode);

        if (remove == null){
            if(debugable){
                Log.w("ActivityResultx","ActivityResultListener callback is null,"+intentHashCode);
            }
            return;
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                remove.onActivityResult(resultCode,data);
            }
        },1000);
    }
    /**
     * 创建一个与原Intent数据相同但完全独立的新Intent（Parcel 深拷贝，含 ClipData 等全部字段）
     * @param originalIntent 原始Intent
     * @return 复制后的新Intent；原为 null 时返回 null
     */
    public static Intent forkIntent(Intent originalIntent) {
        if (originalIntent == null) {
            return null;
        }
        Parcel parcel = Parcel.obtain();
        try {
            originalIntent.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            return Intent.CREATOR.createFromParcel(parcel);
        } finally {
            parcel.recycle();
        }
    }
}
