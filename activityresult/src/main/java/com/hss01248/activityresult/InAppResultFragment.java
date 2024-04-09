package com.hss01248.activityresult;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.hss01248.transfrag.BaseTransFragment;

import java.util.Random;

/**
 * time:2020/5/1
 * author:hss
 * desription:
 */
 public class InAppResultFragment extends Fragment {

    protected FragmentActivity activity;
    protected Intent bean;
    boolean firstResume = true;
    public static boolean debugable = true;

    public InAppResultFragment() {
        super();
    }

    public InAppResultFragment(FragmentActivity activity, Intent intent) {
        this();
        this.activity = activity;
        this.bean = intent;
        BaseTransFragment.startFragmentTransaction(activity,this);
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
        if(listener != null){
            listener.onActivityResult(requestCode,resultCode,data);
        }
        BaseTransFragment.finish(this);
        if(requestCode == this.requestCode){
            //listener.onActivityResult(requestCode,resultCode,data);
            //finish();
        }else {
            if (StartActivityUtil.debugable) {
                Log.w("onActivityResult", "reqcode not same:" + requestCode + ",this.requestCode:" + this.requestCode );
            }
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if(debugable){
            Log.d("frag","onCreate:"+this);
        }
        // firstResume = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(debugable){
            Log.d("frag","onStart:"+this);
        }
    }

    @Override
    public void onResume() {
        if(debugable){
            Log.d("frag","onresume:"+this);
        }
        super.onResume();
        if(firstResume){
            onFirstResume();
            firstResume = false;
        }
    }


    protected void onFirstResume() {

    }

    @Override
    public void onPause() {
        super.onPause();
        if(debugable){
            Log.d("frag","onPause:"+this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(debugable){
            Log.d("frag","onStop:"+this);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(debugable){
            Log.d("frag","onDestroy:"+this);
        }
        BaseTransFragment.finish(this);
    }
}
