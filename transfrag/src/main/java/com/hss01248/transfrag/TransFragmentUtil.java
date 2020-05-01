package com.hss01248.transfrag;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * time:2020/4/30
 * author:hss
 * desription:
 */
public class TransFragmentUtil<Frag extends BaseTransFragment, Bean> {
    Bean bean;
    FragmentActivity fragmentActivity;
    String fragmentTag;
    Class<Frag> clazz;

    public TransFragmentUtil(FragmentActivity fragmentActivity,Class<Frag> clazz, Bean bean) {
        this.bean = bean;
        this.fragmentActivity = fragmentActivity;
        this.fragmentTag = UUID.randomUUID().toString();
        this.clazz = clazz;

    }

    public Frag getFragment() {
        return getTransFragment(fragmentActivity, fragmentTag,
                new Callable<Frag>() {
                    @Override
                    public Frag call() throws Exception {
                        return newInstance(bean);
                    }
                });
    }

    private Frag getTransFragment(FragmentActivity fragmentActivity,
                                  String fragmentTag, Callable<Frag> newIntance) {
        try {
            FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
            Frag fragment = findFragment(fragmentManager, fragmentTag);
            boolean isNewInstance = fragment == null;
            if (isNewInstance) {
                fragment = newIntance.call();
                fragmentManager.beginTransaction()
                        .add(fragment, fragmentTag)
                        .commitNowAllowingStateLoss();
                //fragmentManager.executePendingTransactions();
            }
            fragment.setHostActivity(fragmentActivity);
            fragment.setBean(bean);
            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return newIntance.call();
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
        }

    }

    private Frag findFragment(FragmentManager fragmentManager, String fragmentTag) {
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        if (fragment == null) {
            return null;
        }
        return (Frag) fragment;
    }

    private <Frag extends BaseTransFragment> Frag newInstance(Bean bean) {
        /*Bundle args = new Bundle();
        String json = JsonParser.getInstance().toStr(info);
        args.putString(VENDOR_PAY_BUNDLE_KEY, json);
        VendorPayTransFragment fragment = new VendorPayTransFragment();
        fragment.setArguments(args);*/
        Frag fragment = null;
        try {
            fragment = (Frag) clazz.newInstance();
            fragment.setBean(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fragment;
    }

    private <Frag> Frag getNewInstance(Object object, int i) {
        if (object != null) {
            try {
                Type types = object.getClass().getGenericSuperclass();

                Type[] genericType = ((ParameterizedType) types).getActualTypeArguments();
                for (Type t : genericType) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        System.out.println(t.getTypeName());
                        System.out.println(t);
                    }
                }
                return ((Class<Frag>) genericType[i]).newInstance();






                /*return ((Class<Frag>) ((ParameterizedType) (object.getClass()
                        .getGenericSuperclass())).getActualTypeArguments()[i])
                        .newInstance();*/
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;

    }
}
