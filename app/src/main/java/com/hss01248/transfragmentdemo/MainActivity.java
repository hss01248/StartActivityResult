package com.hss01248.transfragmentdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hss01248.transfrag.TheActivityListener;
import com.hss01248.transfrag.StartActivityUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartActivityUtil.init(getApplication());
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_lifecycle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allCallback();
            }
        });
    }

    private void allCallback() {
        //new TransFragmentUtil<AllListenerFragment,String>(this,"").getFragment();
        StartActivityUtil.startActivity(this, ActivityDemo2.class, null,true,
                new TheActivityListener() {
                    @Override
                    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                        Toast.makeText(activity, "activity oncreate 回调", Toast.LENGTH_SHORT).show();
                    }

                });
    }
}
