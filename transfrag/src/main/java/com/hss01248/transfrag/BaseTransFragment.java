package com.hss01248.transfrag;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

    public void setCallback(IEventCallback callback) {
        this.callback = callback;
    }

    protected IEventCallback callback;

    public BaseTransFragment(FragmentActivity activity, Bean bean) {
        this.activity = activity;
        this.bean = bean;
        startFragmentTransaction();
    }


    /**
     * 使用 commitAllowingStateLoss / commitNowAllowingStateLoss 则可以避开这个检测，
     * 虽然这样可以避免崩溃，但是会出现另外一个问题，系统提供的 requestPermissions API 在 Activity 不可见时调用也不会弹出授权对话框，
     * XXPermissions 的解决方式是将 requestPermissions 时机从 create 转移到了 resume，因为 Activity 和
     * Fragment 的生命周期方法是捆绑在一起的，如果 Activity 是不可见的，那么就算创建了 Fragment 也只会调用 onCreate 方法，
     * 而不会去调用它的 onResume 方法，最后当 Activity 从后台返回到前台时，不仅会触发 Activity.onResume 方法，
     * 同时也会触发 PermissionFragment 的 onResume 方法，在这个方法申请权限就可以保证最终 requestPermissions 申请的时机是在 Activity 处于可见状态的情况下。
     */
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
        if(callback != null){
            callback.onStart();
        }

    }

    @Override
    public void onResume() {
        if(debugable){
            Log.d("frag","onresume:"+this);
        }
        super.onResume();
        if(callback != null){
            callback.onResume();
        }
        if(firstResume){
            onFirstResume();
            firstResume = false;
            if(callback != null){
                callback.onFirstResume();
            }
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

        if(callback != null){
            callback.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(debugable){
            Log.d("frag","onStop:"+this);
        }
        if(callback != null){
            callback.onStop();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(debugable){
            Log.d("frag","onActivityResult:"+this);
        }
        if(callback != null){
            callback.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(debugable){
            Log.d("frag","onRequestPermissionsResult:"+this);
        }
        if(callback != null){
            callback.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(debugable){
            Log.d("frag","onDestroy:"+this);
        }
        if(callback != null){
            callback.onDestroy();
        }
    }


    public void finish(){
        try {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }

    public static BaseTransFragment startTransFragment(FragmentActivity activity,IEventCallback callback){
        BaseTransFragment fragment = new BaseTransFragment(activity,"");
        fragment.setCallback(callback);
        return fragment;
    }


}
