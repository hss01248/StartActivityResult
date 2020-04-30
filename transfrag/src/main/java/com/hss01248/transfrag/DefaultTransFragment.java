package com.hss01248.transfrag;

import android.content.Intent;

import androidx.annotation.Nullable;

import java.util.Random;

/**
 * time:2020/4/30
 * author:hss
 * desription:
 */
public class DefaultTransFragment extends BaseTransFragment<Intent> {

    int requestCode;
    boolean consumed;
    OutActivityResultListener listener;
    public void goOutApp(OutActivityResultListener listener){
        requestCode = new Random().nextInt(8799);
        this.listener = listener;
        try {
            startActivityForResult(bean,requestCode);
        }catch (Throwable throwable){
            listener.onActivityNotFound(throwable);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        consumed = true;
        listener.onActivityResult(requestCode,resultCode,data);

    }

    @Override
    protected void onNotFirstResume() {
        super.onNotFirstResume();
        if(!consumed){
            listener.onActivityResult(requestCode,66,null);
        }
    }

}
