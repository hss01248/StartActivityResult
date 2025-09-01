package com.hss01248.activityresult.androidx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;

public class TransparentActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> launcher;
    int intentHashCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 注册ActivityResultLauncher
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        LogUtils.i(result);
                        // 回调结果给工具类
                        // 创建一个复制的Intent
                        Intent forkedIntent = ActivityResultHelper.forkIntent(result.getData());
                        ActivityResultHelper.onActivityResult(result.getResultCode(), forkedIntent,intentHashCode);
                    // 先关闭透明Activity. 没用,匿名内部类持有该activity的引用,会导致一直无法回调onDestory,一定是上面执行完后才销毁,
                        // intent本身也持有着该activity的引用,导致无法销毁
                        finish();
                    }
                }
        );

        // 获取目标Intent并启动
        Intent targetIntent = getIntent().getParcelableExtra("target_intent");
        intentHashCode = getIntent().getIntExtra("target_intent_hash",0);
        if (targetIntent != null) {
            try{
                launcher.launch(targetIntent);
            }catch (Throwable e){
                e.printStackTrace();
                ActivityResultHelper.onActivityResult(Activity.RESULT_CANCELED, null,intentHashCode);
                finish();
            }
        } else {
            // 没有目标Intent时直接失败
            ActivityResultHelper.onActivityResult(Activity.RESULT_CANCELED, null,intentHashCode);
            finish();
        }
    }
}
