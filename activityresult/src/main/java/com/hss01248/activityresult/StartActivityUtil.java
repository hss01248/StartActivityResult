package com.hss01248.activityresult;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;



/**
 * time:2020/4/30
 * author:hss
 * desription:
 *
 * @author hss
 */
public class StartActivityUtil {


    public static boolean debugable;
    static boolean hasInit;

     static void init(Application application) {
         if(hasInit){
             return;
         }
        debugable = isAppDebugable(application);
         hasInit = true;
    }

     static boolean isAppDebugable(Application application) {
        try {
            ApplicationInfo info = application.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressLint("CheckResult")
    public static void goOutAppForResult(@NonNull Activity activity, @NonNull  Intent intent,
                                         @NonNull final ActivityResultListener listener){
        init(activity.getApplication());
        new GoOutOfAppForResultFragment((FragmentActivity) activity,intent).goOutApp(listener);
    }

    /**
     * @param activity
     * @param targetClazz
     * @param intent
     * @param needResult
     * @param listener
     */
    @SuppressLint("CheckResult")
    public static <T extends Activity> void startActivity(@NonNull final Activity activity,
                                     @Nullable Class<T> targetClazz,
                                     @Nullable Intent intent,
                                     boolean needResult,
                                     @NonNull final TheActivityListener<T> listener) {
        init(activity.getApplication());
        if(targetClazz == null && intent == null){
            listener.onActivityNotFound(new Throwable("targetClazz and intent can not be  null at same time"));
            return;
        }
        if(targetClazz != null){
            registerCallback(activity.getApplication(), targetClazz, listener);
        }
        if (intent == null) {
            intent = new Intent(activity, targetClazz);
        }
        if (!needResult) {
            try {
                activity.startActivity(intent);
            } catch (Throwable throwable) {
                if (debugable) {
                    throwable.printStackTrace();
                }
                listener.onActivityNotFound(throwable);
            }
            return;
        }
        if(activity instanceof FragmentActivity){
            new InAppResultFragment((FragmentActivity) activity,intent).startActivityForResult(listener);
        }else {
            activity.startActivityForResult(intent,898);
        }
    }

     static <T extends Activity> void registerCallback(final Application application, final Class<T> targetClazz,
                                         final TheActivityListener<T> listener) {

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            boolean hasonActivityCreated;
            boolean hasonActivityStarted;
            boolean hasonActivityResumed;

            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                if (targetClazz.equals(activity.getClass())) {
                    if (!hasonActivityCreated) {
                        hasonActivityCreated = true;
                        if (debugable) Log.i("callback", "onActivityCreated");
                        listener.onActivityCreated((T) activity, savedInstanceState);
                    } else {
                        if (debugable) Log.w("has", "hasonActivityCreated");
                    }
                }
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                if (targetClazz.equals(activity.getClass())) {
                    if (!hasonActivityStarted) {
                        hasonActivityStarted = true;
                        if (debugable) Log.i("callback", "onActivityStarted");
                        listener.onActivityStarted((T) activity);
                    } else {
                        if (debugable) Log.w("has", "hasonActivityStarted");
                    }
                }
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                if (targetClazz.equals(activity.getClass())) {
                    if (!hasonActivityResumed) {
                        hasonActivityResumed = true;
                        if (debugable) Log.i("callback", "onActivityResumed");
                        application.unregisterActivityLifecycleCallbacks(this);
                        listener.onActivityResumed((T) activity);
                    } else {
                        if (debugable) Log.w("has", "hasonActivityResumed");
                    }
                }
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }


}
