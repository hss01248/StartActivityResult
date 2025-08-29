package com.hss01248.activityresult.androidx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ActivityResultHelper {
    // 静态回调引用
    private static ActivityResultCallback sCallback;

    // 启动流程的入口方法
    public static void startForResult(Context context, Intent targetIntent, ActivityResultCallback callback) {
        sCallback = callback;
        
        // 启动透明Activity
        Intent intent = new Intent(context, TransparentActivity.class);
        intent.putExtra("target_intent", targetIntent);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // 回调接口定义
    public interface ActivityResultCallback {
        void onSuccess(Intent data);
        void onFailure(int resultCode, Intent data);
    }

    // 供TransparentActivity调用的回调方法
    static void onActivityResult(int resultCode, Intent data) {
        if (sCallback == null) return;
        
        if (resultCode == Activity.RESULT_OK) {
            sCallback.onSuccess(data);
        } else {
            sCallback.onFailure(resultCode, data);
        }
        
        // 清空回调防止内存泄漏
        sCallback = null;
    }
}
