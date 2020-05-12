package com.hss01248.activityresult;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.hss01248.transfrag.BaseTransFragment;

import java.util.Random;

/**
 * time:2020/4/30
 * author:hss
 * desription:
 */
 public class GoOutOfAppForResultFragment extends BaseTransFragment<Intent> {

    int requestCode;
    ActivityResultListener listener;
    boolean startWaitingResult;
    boolean consumed;

    public GoOutOfAppForResultFragment(FragmentActivity activity, Intent intent) {
        super(activity, intent);
    }

    public void goOutApp(ActivityResultListener listener){
        requestCode = new Random().nextInt(8799);
        this.listener = listener;
        try {
            startActivityForResult(bean,requestCode);
        }catch (Throwable throwable){
            listener.onActivityNotFound(throwable);
            finish();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        startWaitingResult = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(startWaitingResult){
            onStartOfResultBack();
            startWaitingResult = false;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(startWaitingResult){
            consumed = true;
            listener.onActivityResult(requestCode,resultCode,data);
            if (StartActivityUtil.debugable) {
                Log.i("onActivityResult", "req:" + requestCode + ",result:" + resultCode + ",data:" + data);
            }
            //不会在回来时回调,而是一跳出去就回调,resultcode是cancel,所以这个方法不能用
        }
        //consumed = true;
        //listener.onActivityResult(requestCode,resultCode,data);
        //不会在回来时回调,而是一跳出去就回调,resultcode是cancel,所以这个方法不能用

    }

    protected void onStartOfResultBack() {
        listener.onActivityResult(requestCode,66,null);
        if (StartActivityUtil.debugable) {
            Log.i("onActivityResult2", "req:" + requestCode + ",result:onStartOfResultBack,data:null" );
        }
        finish();

    }

}
