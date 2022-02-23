package com.hss01248.activityresult;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.hss01248.transfrag.BaseTransFragment;

import java.util.Random;

/**
 * time:2020/5/1
 * author:hss
 * desription:
 */
 public class InAppResultFragment extends BaseTransFragment<Intent> {
    public InAppResultFragment(FragmentActivity activity, Intent intent) {
        super(activity, intent);
    }
    ActivityResultListener listener;
    int requestCode;
    public void startActivityForResult(ActivityResultListener listener){
        try {
            this.listener = listener;
            requestCode = new Random().nextInt(589);
            if(!listener.onInterceptStartIntent(this,bean,requestCode)){
                startActivityForResult(bean,requestCode);
            }

        }catch (Throwable e){
            listener.onActivityNotFound(e);
            if(StartActivityUtil.debugable){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (StartActivityUtil.debugable) {
            Log.i("onActivityResult", "req:" + requestCode + ",result:" + resultCode + ",data:" + data);
        }
        listener.onActivityResult(requestCode,resultCode,data);
        finish();
        if(requestCode == this.requestCode){
            //listener.onActivityResult(requestCode,resultCode,data);
            //finish();
        }else {
            if (StartActivityUtil.debugable) {
                Log.w("onActivityResult", "reqcode not same:" + requestCode + ",this.requestCode:" + this.requestCode );
            }
        }


    }
}
