package com.hss01248.transactivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class DialogPriorityUtil {

    /**
     * 测试方法,不可使用
     * @param activity
     * @param view
     * @param priority
     */
    public static void showViewAsDialogWithPriority(Activity activity,View view, int priority){

        try {
            Dialog dialog = new Dialog(activity);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                dialog.getWindow().setElevation(9);
            }
            //.setType(WindowManager.LayoutParams.TYPE_TOAST);
           // dialog.getWindow().setType(WindowManager.LayoutParams.LAST_SUB_WINDOW);
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            }else {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
            }*/


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
            window.setAttributes(attributes);
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
