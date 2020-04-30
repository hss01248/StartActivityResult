package com.hss01248.transfrag;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 *
 * @param <Bean>
 */
public  class BaseTransFragment<Bean> extends Fragment {

    protected FragmentActivity activity;


    public void setBean(Bean bean) {
        this.bean = bean;
    }

    protected Bean bean;
    private boolean hasResume;

    public void setHostActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if(StartActivityUtil.debugable){
            Log.d("frag","onCreate:"+this);
        }
    }

    @Override
    public void onResume() {
        if(StartActivityUtil.debugable){
            Log.d("frag","onresume:"+this);
        }
        super.onResume();
        if(!hasResume){
            hasResume = true;
        }else {
            onNotFirstResume();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(StartActivityUtil.debugable){
            Log.d("frag","onActivityResult:"+this);
        }
    }

    protected void onNotFirstResume(){

    }

    public void finish(){
        try {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commitNow();
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }


}
