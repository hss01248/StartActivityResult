package com.hss01248.transfrag;

import android.os.Bundle;


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

    public void setHostActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void finish(){
        try {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commitNow();
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }


}
