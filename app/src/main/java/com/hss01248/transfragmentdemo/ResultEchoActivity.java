package com.hss01248.transfragmentdemo;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 用于验证 activityresult2 forkIntent 是否完整保留 result Intent 字段。
 */
public class ResultEchoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_echo);
    }

    public void returnFullIntent(View view) {
        Intent result = new Intent("com.hss01248.transfragmentdemo.ACTION_ECHO");
        result.setDataAndType(Uri.parse("content://com.hss01248.transfragmentdemo/echo/item1"), "image/jpeg");
        result.addCategory(Intent.CATEGORY_DEFAULT);
        result.putExtra("echo_name", "李雷");
        result.putExtra("echo_score", 99);
        result.putExtra("echo_flag", true);
        result.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        ClipData clipData = new ClipData(
                "echo",
                new String[]{"image/jpeg", "text/plain"},
                new ClipData.Item(Uri.parse("content://com.hss01248.transfragmentdemo/echo/item1"))
        );
        clipData.addItem(new ClipData.Item(Uri.parse("content://com.hss01248.transfragmentdemo/echo/item2")));
        clipData.addItem(new ClipData.Item("plain-clip-text"));
        result.setClipData(clipData);
        result.setPackage(getPackageName());
        setResult(RESULT_OK, result);
        finish();
    }

    public void returnEmptyIntent(View view) {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    public void returnCancelNull(View view) {
        setResult(RESULT_CANCELED, null);
        finish();
    }
}
