package com.hss01248.activityresult;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.hss01248.transfrag.BaseTransFragment;

import java.util.Random;

/**
 * time:2020/4/30
 * author:hss
 * desription:
 *
 * 2023-06-02 09:51:49.244 3295-3295/com.hss01248.transfragmentdemo D/frag: onStop:GoOutOfAppForResultFragment{993ff35} (579cc2d3-2ea6-4aea-93c0-e75343cdeccc tag=25328484-3fe6-4b3c-9061-9be1fbc79070)
 * 2023-06-02 09:51:54.925 3295-22616/com.hss01248.transfragmentdemo D/AppScoutStateMachine: 3295-ScoutStateMachinecreated
 * 2023-06-02 09:51:54.926 3295-3295/com.hss01248.transfragmentdemo D/frag: onActivityResult:GoOutOfAppForResultFragment{993ff35} (579cc2d3-2ea6-4aea-93c0-e75343cdeccc tag=25328484-3fe6-4b3c-9061-9be1fbc79070)
 * 2023-06-02 09:51:54.927 3295-3295/com.hss01248.transfragmentdemo I/onActivityResult: frag req:1066,result:-1,data:Intent { dat=content://com.miui.gallery.open/... typ=video/mp4 flg=0x1 }
 * 2023-06-02 09:51:54.929 3295-3295/com.hss01248.transfragmentdemo W/MainActivity:
 *     ┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────
 *     │ main, com.hss01248.transfragmentdemo.MainActivity$12$1.onActivityResult(MainActivity.java:290)
 *     ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 *     │ Intent { dat=content://com.miui.gallery.open/raw/%2Fstorage%2Femulated%2F0%2FPictures%2FWeiXin%2Fmmexport1685491665767.mp4 typ=video/mp4 flg=0x1 }
 *     └────────────────────────────────────────────────────────────────────────────────────────────────────────────────
 * 2023-06-02 09:51:54.929 3295-3295/com.hss01248.transfragmentdemo I/onActivityResult2: frag req:1066,result:onStartOfResultBack,data:null
 * 2023-06-02 09:51:54.975 3295-3295/com.hss01248.transfragmentdemo D/frag: onDestroy:GoOutOfAppForResultFragment{993ff35} (579cc2d3-2ea6-4aea-93c0-e75343cdeccc tag=25328484-3fe6-4b3c-9061-9be1fbc79070)
 * 2023-06-02 09:51:55.025 3295-3295/com.hss01248.transfragmentdemo D/DecorView[]: onWindowFocusChanged hasWindowFocus true
 */
 public class GoOutOfAppForResultFragment extends BaseTransFragment<Intent> {

    int requestCode;
    ActivityResultListener listener;
    boolean startWaitingResult;
    boolean consumed;

    public GoOutOfAppForResultFragment() {
        super();
    }

    public GoOutOfAppForResultFragment(FragmentActivity activity, Intent intent) {
        super(activity, intent);
    }

    public void goOutApp(ActivityResultListener listener){
        requestCode = new Random().nextInt(8799);
        this.listener = listener;
        if (StartActivityUtil.debugable) {
            Log.i("startActivityForResult", "frag req:" + requestCode );
        }
        try {
            if(!listener.onInterceptStartIntent(this,bean,requestCode)){
                startActivityForResult(bean,requestCode);
            }
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

    //todo 一些半屏activity,只触发fragment的onPause, 不触发onStop
   /* @Override
    public void onPause() {
        super.onPause();
        startWaitingResult = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(startWaitingResult && !consumed){
            onStartOfResultBack(requestCode,66,null);
            startWaitingResult = false;
        }
    }*/


    /**
     * 如果真有,那么比onStart()先执行
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(startWaitingResult){
            consumed = true;
            if (StartActivityUtil.debugable) {
                Log.i("onActivityResult", "frag req:" + requestCode + ",result:" + resultCode + ",data:" + data);
            }
            onStartOfResultBack(requestCode,resultCode,data);

            //不会在回来时回调,而是一跳出去就回调,resultcode是cancel,所以这个方法不能用
        }
        //consumed = true;
        //listener.onActivityResult(requestCode,resultCode,data);
        //不会在回来时回调,而是一跳出去就回调,resultcode是cancel,所以这个方法不能用

    }

    @Override
    public void onStart() {
        super.onStart();
        if (StartActivityUtil.debugable) {
            Log.i("frag", "on start frag req:" + requestCode + ","+this.toString() );
        }
        //有时,onStart会在onActivityResult之前走,所以要等一会儿
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isDetached()){
                    if (StartActivityUtil.debugable) {
                        Log.i("frag", "on start frag req: isDetached" +","+this.toString() );
                    }
                    return;
                }
                if(startWaitingResult && !consumed){
                    onStartOfResultBack(requestCode,66,null);
                    startWaitingResult = false;
                }
            }
        },500);

    }



    protected void onStartOfResultBack(int requestCode, int resultCode, @Nullable Intent data) {
        if(listener != null){
            listener.onActivityResult(requestCode,resultCode,data);
        }

        if (StartActivityUtil.debugable) {
            Log.i("onActivityResult2", "frag req:" + requestCode + ",result:onStartOfResultBack,data:"+data );
        }
        finish();

    }

}
