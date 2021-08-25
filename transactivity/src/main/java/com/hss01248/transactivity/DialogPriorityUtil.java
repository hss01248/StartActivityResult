package com.hss01248.transactivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
@Deprecated
public class DialogPriorityUtil {

    /**
     * 测试方法,不可使用
     * @param activity
     * @param view
     * @param priority
     */
    public static void showViewAsDialogWithPriority(Activity activity,View view, int priority){

        try {
            WindowManager windowManager = (WindowManager)activity.getSystemService(Context.WINDOW_SERVICE);


            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            //params.dimAmount = 0.5f;
            //params.type = WindowManager.LayoutParams.LAST_SUB_WINDOW;
            //params.type = WindowManager.LayoutParams.TYPE_BASE_APPLICATION;
           // params.type = WindowManager.LayoutParams.TYPE_BASE_APPLICATION;
            //params.type = WindowManager.LayoutParams.LAST_APPLICATION_WINDOW;
            params.type = WindowManager.LayoutParams.FIRST_SUB_WINDOW;
            //dialog 的type = 2; 但始终后弹出的会盖住

            // 设置flag+聚焦,才能响应back键
            int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.requestFocus();

            // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
            params.flags = flags;
            // 不设置这个弹出框的透明遮罩显示为黑色
            params.format = PixelFormat.TRANSLUCENT;
            // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
            // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
            // 不设置这个flag的话，home页的划屏会有问题
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;


            // 点击back键可消除
            view.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                           windowManager.removeViewImmediate(view);
                            return true;
                        default:
                            return false;
                    }
                }
            });

            windowManager.addView(view,params);

           // Window window = new

           // windowManager.removeView();


            //.setType(WindowManager.LayoutParams.TYPE_TOAST);
            // dialog.getWindow().setType(WindowManager.LayoutParams.LAST_SUB_WINDOW);
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            }else {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
            }*/

            /*Dialog dialog = new Dialog(activity);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                dialog.getWindow().setElevation(9);
            }
            ///IllegalArgumentException: Window type can not be changed after the window is added
            dialog.setContentView(view);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            //configView(binding,dialog,activity);
            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = getScreenWidth(view.getContext());
            attributes.height = (int) (getScreenHeight(view.getContext()) * 0.65f);
            window.setAttributes(attributes);*/
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }

    }


    public  static int getScreenHeight(Context context) {
        try {
            int height = getWindowManager2(context).getDefaultDisplay().getHeight();
            return height;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    public static WindowManager getWindowManager2(Context context) {
        return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public static int getScreenWidth(Context context) {
        try {
            int width = getWindowManager2(context).getDefaultDisplay().getWidth();
            return width;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
