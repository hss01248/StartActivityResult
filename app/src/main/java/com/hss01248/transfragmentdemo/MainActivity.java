package com.hss01248.transfragmentdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.hss01248.activityresult.ActivityResultListener;
import com.hss01248.activityresult.StartActivityUtil;
import com.hss01248.activityresult.StartActivityUtil2;
import com.hss01248.activityresult.TheActivityListener;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_lifecycle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allCallback();
            }
        });
        findViewById(R.id.btn_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNotification();
            }
        });
    }

    private void goNotification() {
       Intent intent =  getNotificationIntent();
        StartActivityUtil.goOutAppForResult(this,intent, new ActivityResultListener() {


            @Override
            public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                boolean hasPermission =   NotificationManagerCompat.from(MainActivity.this).areNotificationsEnabled();
                Toast.makeText(MainActivity.this,"通知栏权限:"+hasPermission,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onActivityNotFound(Throwable e) {

            }
        });
       /**/
      /* StartActivityUtil2.startActivity(this,null,intent,true, new TheActivityListener<Activity>(){
           @Override
           public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
               boolean hasPermission =   NotificationManagerCompat.from(MainActivity.this).areNotificationsEnabled();
               Toast.makeText(MainActivity.this,"通知栏权限:"+hasPermission,Toast.LENGTH_SHORT).show();
           }
       });*/
    }

    private Intent getNotificationIntent() {
        Intent localIntent = new Intent();
        //直接跳转到应用通知设置的代码：
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0及以上
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0以上到8.0以下
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("app_package", getPackageName());
            localIntent.putExtra("app_uid", getApplicationInfo().uid);
        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {//4.4
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + getPackageName()));
        } else {
            //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
            }
        }

        //原文链接：https://blog.csdn.net/aiynmimi/article/details/102740139
        return localIntent;
    }

    private void allCallback() {
        StartActivityUtil.startActivity(this, ActivityDemo2.class, null,true,
                new TheActivityListener<ActivityDemo2>() {
                    @Override
                    public void onActivityCreated(@NonNull ActivityDemo2 activity, @Nullable Bundle savedInstanceState) {
                       //可以在这里传递数据
                        activity.setData(666);
                        Toast.makeText(activity, "activity oncreate 回调", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                        super.onActivityResult(requestCode, resultCode, data);
                        Toast.makeText(MainActivity.this, "activity onActivityResult 回调", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void inner2(View view) {
        StartActivityUtil2.startActivity(this, ActivityDemo2.class, null,true,
                new TheActivityListener<ActivityDemo2>() {
                    @Override
                    public void onActivityCreated(@NonNull ActivityDemo2 activity, @Nullable Bundle savedInstanceState) {
                        //可以在这里传递数据
                        activity.setData(666);
                        Toast.makeText(activity, "activity oncreate 回调", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                        super.onActivityResult(requestCode, resultCode, data);
                        Toast.makeText(MainActivity.this, "activity onActivityResult 回调", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void notification2(View view) {
        Intent intent =  getNotificationIntent();
        StartActivityUtil2.startActivity(this,null,intent,true, new TheActivityListener<Activity>(){
            @Override
            public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                boolean hasPermission =   NotificationManagerCompat.from(MainActivity.this).areNotificationsEnabled();
                Toast.makeText(MainActivity.this,"通知栏权限:"+hasPermission,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
