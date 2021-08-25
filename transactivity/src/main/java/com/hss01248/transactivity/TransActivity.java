package com.hss01248.transactivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.hss01248.activityresult.StartActivityUtil;
import com.hss01248.activityresult.TheActivityListener;


public class TransActivity extends AppCompatActivity {

    public static void start(Activity activity, ITransActivityConfig config){
        StartActivityUtil.startActivity(activity,TransActivity.class,null,false,
                new TheActivityListener<TransActivity>(){
                    @Override
                    protected void onActivityCreated(@NonNull TransActivity activity, @Nullable Bundle savedInstanceState) {
                        super.onActivityCreated(activity, savedInstanceState);
                        try {
                            activity.init(config);
                        }catch (Throwable throwable){
                            throwable.printStackTrace();
                            activity.finish();
                        }

                    }
                });

    }

    public interface ITransActivityConfig {
        View initView(Activity activity);
        //dialog.setCancelable(false);
        //            dialog.setCanceledOnTouchOutside(false);
       default boolean cancelable(){
            return true;
        }
        default boolean canceledOnTouchOutside(){
            return true;
        }

       default int gravity(){
           return Gravity.BOTTOM;
        }

        default float forceHeight(){
           return 0;
        }
        default float forceWidth(){
            return 0;
        }
    }

    ITransActivityConfig config;

    //FrameLayout container;
    RelativeLayout rlRoot;


    public void init(ITransActivityConfig transActivityConfig){

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_trans_dialog_v2);
       // container = findViewById(R.id.fl_container);


       /* ViewGroup.LayoutParams layoutParams = rlRoot.getLayoutParams();
        layoutParams.height = getScreenHeight();
        Log.w("height",layoutParams.height+"px");
        rlRoot.setLayoutParams(layoutParams);
        View contentview = findViewById(android.R.id.content);
        Log.w("height",contentview.getMeasuredHeight()+"px");*/


        //也可以用下面的办法,隐藏标题栏
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.hide();
        }



        this.config = transActivityConfig;
        View view = config.initView(this);
        setLayoutParams(view,config);

        rlRoot = findViewById(R.id.rl_root);
        rlRoot.addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(config.canceledOnTouchOutside()){
                    finish();
                }
            }
        });





    }

    private void setLayoutParams(View container, ITransActivityConfig config) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //宽高:
        if(config.forceWidth() >0){
            params.width = (int) (config.forceWidth() * getScreenWidth());
        }
        if(config.forceHeight() >0){
            params.height = (int) (config.forceHeight() * getScreenHeight());
        }


        if(config.gravity() == Gravity.BOTTOM){
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        }else if(config.gravity() == Gravity.TOP){
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        }else if(config.gravity() == Gravity.CENTER){
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
        }


        container.setLayoutParams(params);


    }

    public  int getScreenHeight() {
        try {
            int height = getWindowManager2().getDefaultDisplay().getHeight();
            return height;
        } catch (Exception e) {
          e.printStackTrace();
            return 0;
        }

    }

    public  WindowManager getWindowManager2() {
        return (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    }

    public  int getScreenWidth() {
        try {
            int width = getWindowManager2().getDefaultDisplay().getWidth();
            return width;
        } catch (Exception e) {
           e.printStackTrace();
            return 0;
        }
    }



    private void transStatusbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 全透明实现
            //getWindow.setStatusBarColor(Color.TRANSPARENT)
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |                 View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4 全透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onBackPressed() {
        if(config != null){
            if(!config.cancelable()){
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        rlRoot.setBackgroundColor(Color.TRANSPARENT);
        super.finish();
        overridePendingTransition(0, R.anim.anim_bottom_out_trans_activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.anim_bottom_in_trans_activity, R.anim.anim_bottom_out_trans_activity);
        super.onCreate(savedInstanceState);
        transStatusbar();
       /* rlRoot.setBackgroundColor(Color.TRANSPARENT);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                rlRoot.setBackgroundColor(Color.TRANSPARENT);
            }
        },200);*/
    }

}
