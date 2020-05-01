package com.hss01248.transfrag;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.UUID;

/**
 *
 * @param <Bean>
 */
public  class BaseTransFragment<Bean> extends Fragment {

    protected FragmentActivity activity;
    protected Bean bean;
    boolean firstResume = true;
    public static boolean debugable = true;

    public BaseTransFragment(FragmentActivity activity, Bean bean) {
        this.activity = activity;
        this.bean = bean;
        startFragmentTransaction();
    }


    private void startFragmentTransaction() {
        try {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(this, UUID.randomUUID().toString())
                        .commitNowAllowingStateLoss();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if(debugable){
            Log.d("frag","onCreate:"+this);
        }
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
        if(debugable)
        Log.d("frag","onPause:"+this);

    }

    @Override
    public void onStop() {
        super.onStop();
        if(debugable)
        Log.d("frag","onStop:"+this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(debugable){
            Log.d("frag","onActivityResult:"+this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(debugable){
            Log.d("frag","onDestroy:"+this);
        }
    }


    public void finish(){
        try {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }


}
