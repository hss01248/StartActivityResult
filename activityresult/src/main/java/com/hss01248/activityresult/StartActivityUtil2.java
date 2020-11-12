package com.hss01248.activityresult;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.FragmentActivity;


/**
 * 使用Androidx里的api实现
 */
public class StartActivityUtil2 {

    public static <T extends Activity> void startActivity(@NonNull final Activity activity,
                                                          @Nullable Class<T> targetClazz,
                                                          @Nullable Intent intent,
                                                          boolean needResult,
                                                          @NonNull final TheActivityListener<T> listener){
        StartActivityUtil.init(activity.getApplication());
        if(targetClazz == null && intent == null){
            listener.onActivityNotFound(new Throwable("targetClazz and intent can not be  null at same time"));
            return;
        }
        if(targetClazz != null){
            StartActivityUtil.registerCallback(activity.getApplication(), targetClazz, listener);
        }
        if (intent == null) {
            intent = new Intent(activity, targetClazz);
        }
        if (!needResult) {
            try {
                activity.startActivity(intent);
            } catch (Throwable throwable) {
                if (StartActivityUtil.debugable) {
                    throwable.printStackTrace();
                }
                listener.onActivityNotFound(throwable);
            }
            return;
        }
        if(activity instanceof FragmentActivity){
            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            EmptyFragment emptyFragment = new EmptyFragment(fragmentActivity,intent);
            emptyFragment.listener = listener;
            //必须在oncreate里registerForActivityResult,否则报错,所以,依然没什么鸟用,得用fragment
            //IllegalStateException: LifecycleOwner com.hss01248.transfragmentdemo.MainActivity@acf9b248
            // is attempting to register while current state is RESUMED. LifecycleOwners must call register before they are STARTED.
           /* fragmentActivity.registerForActivityResult(new ActivityResultContract<Object, Intent>() {
                @NonNull
                @Override
                public Intent createIntent(@NonNull Context context, Object input) {
                    return finalIntent;
                }

                @Override
                public Intent parseResult(int resultCode, @Nullable Intent intent) {
                    listener.onActivityResult(0,resultCode,intent);
                    return intent;
                }
            }, new ActivityResultCallback<Intent>() {
                @Override
                public void onActivityResult(Intent result) {
                    //Toast.makeText(activity,result.toString(),Toast.LENGTH_SHORT).show();

                }
            }).launch("");*/
        }else {
            activity.startActivityForResult(intent,898);
        }

    }
}
