package com.hss01248.activityresult.androidx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class TransparentActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 注册ActivityResultLauncher
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // 回调结果给工具类
                        ActivityResultHelper.onActivityResult(result.getResultCode(), result.getData());
                        // 处理完成后关闭透明Activity
                        finish();
                    }
                }
        );

        // 获取目标Intent并启动
        Intent targetIntent = getIntent().getParcelableExtra("target_intent");
        if (targetIntent != null) {
            launcher.launch(targetIntent);
        } else {
            // 没有目标Intent时直接失败
            ActivityResultHelper.onActivityResult(Activity.RESULT_CANCELED, null);
            finish();
        }
    }
}
