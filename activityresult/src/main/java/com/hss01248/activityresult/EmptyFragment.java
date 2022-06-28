package com.hss01248.activityresult;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
/*
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContract;*/
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.hss01248.transfrag.BaseTransFragment;

public class EmptyFragment extends BaseTransFragment<Intent> {

    public EmptyFragment() {
        super();
    }

    ActivityResultListener listener;
    public EmptyFragment(FragmentActivity activity, Intent o) {
        super(activity, o);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* registerForActivityResult(new ActivityResultContract<Object, Intent>() {
            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, Object input) {
                return bean;
            }

            @Override
            public Intent parseResult(int resultCode, @Nullable Intent intent) {
                listener.onActivityResult(0,resultCode,intent);
                finish();
                return intent;
            }
        }, new ActivityResultCallback<Intent>() {
            @Override
            public void onActivityResult(Intent result) {
                //Toast.makeText(activity,result.toString(),Toast.LENGTH_SHORT).show();

            }
        }).launch("");*/
    }
}
