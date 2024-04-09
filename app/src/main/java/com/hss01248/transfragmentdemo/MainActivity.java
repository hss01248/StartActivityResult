package com.hss01248.transfragmentdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hss01248.activityresult.ActivityResultListener;
import com.hss01248.activityresult.StartActivityUtil;
import com.hss01248.activityresult.TheActivityListener;
import com.hss01248.image.dataforphotoselet.ImgDataSeletor;
import com.hss01248.permission.MyPermissions;
import com.hss01248.transactivity.DialogPriorityUtil;
import com.hss01248.transactivity.TransActivity;

import org.devio.takephoto.wrap.TakeOnePhotoListener;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


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
        findViewById(R.id.btn_pick_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
            }
        });
    }

    private void pickPhoto() {
        Intent intent =  new Intent();
        //intent.setType("video/*;image/*");//同时选择视频和图片
        intent.setType("image/*");//
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        //打开方式有两种action，1.ACTION_PICK；2.ACTION_GET_CONTENT 区分大意为：
        // ACTION_PICK 为打开特定数据一个列表来供用户挑选，其中数据为现有的数据。而 ACTION_GET_CONTENT 区别在于它允许用户创建一个之前并不存在的数据。
        intent.setAction(Intent.ACTION_PICK);
        StartActivityUtil.goOutAppForResult(this,intent, new ActivityResultListener() {


            @Override
            public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                String data2 = data==null ? "": data.getDataString();
                Toast.makeText(MainActivity.this,"resultCode:"+resultCode+", "+data2,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onActivityNotFound(Throwable e) {

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

    public void notification2(View view) {
        Intent intent =  getNotificationIntent();
        StartActivityUtil.startActivity(this,null,intent,true, new TheActivityListener<Activity>(){
            @Override
            public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                boolean hasPermission =   NotificationManagerCompat.from(MainActivity.this).areNotificationsEnabled();
                Toast.makeText(MainActivity.this,"通知栏权限:"+hasPermission,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void activityAsDialog(View view) {
        TransActivity.start(this, new TransActivity.ITransActivityConfig() {
            @Override
            public View initView(Activity activity) {
                return activity.getLayoutInflater().inflate(R.layout.activity_main, (ViewGroup) activity.findViewById(android.R.id.content),false);
            }

            @Override
            public boolean canceledOnTouchOutside() {
                return true;
            }

            @Override
            public boolean cancelable() {
                return false;
            }

            @Override
            public float forceHeight() {
                return 0.65f;
            }

        });
    }

    public void dialogAsToast(View view) {
       // ViewTestToastBinding mainBinding = ViewTestToastBinding.inflate(getLayoutInflater(),findViewById(android.R.id.content),false);
        View view1 = View.inflate(this,R.layout.view_test_toast,null);
        DialogPriorityUtil.showViewAsDialogWithPriority(this,view1,9);


        View view2 = View.inflate(this,R.layout.view_test_toast2,null);
       /* Dialog dialog = new Dialog(this);
        dialog.setContentView(view2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(3);
        }

        dialog.show();*/

       /* AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view2)
                .create();*/
        Dialog alertDialog = new Dialog(this);
        alertDialog.setContentView(view2);
        alertDialog.show();
        Log.w("type",alertDialog.getWindow().getAttributes().type+"  ....");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.w("type2",alertDialog.getWindow().getAttributes().type+"  ....");
            }
        },1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            //alertDialog.getWindow().setElevation(0);
        }


    }

    public void pickAudio(View view) {

       // Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        Intent intent = new Intent();
        intent.setType("audio/*");//
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        //打开方式有两种action，1.ACTION_PICK；2.ACTION_GET_CONTENT 区分大意为：
        // ACTION_PICK 为打开特定数据一个列表来供用户挑选，其中数据为现有的数据。而 ACTION_GET_CONTENT 区别在于它允许用户创建一个之前并不存在的数据。
        intent.setAction(Intent.ACTION_PICK);
        StartActivityUtil.goOutAppForResult(this, intent, new ActivityResultListener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                LogUtils.w(data);
            }

            @Override
            public void onActivityNotFound(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    public void pickVideo(View view) {
        MyPermissions.requestByMostEffort(false, true,
                new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(@NonNull List<String> granted) {
                        Intent intent = new Intent();
                        intent.setType("video/*");//
                        //intent.setAction(Intent.ACTION_GET_CONTENT);
                        //打开方式有两种action，1.ACTION_PICK；2.ACTION_GET_CONTENT 区分大意为：
                        // ACTION_PICK 为打开特定数据一个列表来供用户挑选，其中数据为现有的数据。而 ACTION_GET_CONTENT 区别在于它允许用户创建一个之前并不存在的数据。
                        intent.setAction(Intent.ACTION_PICK);
                        LogUtils.w("topActivity: "+ActivityUtils.getTopActivity());
                        StartActivityUtil.goOutAppForResult(ActivityUtils.getTopActivity(), intent, new ActivityResultListener() {
                            @Override
                            public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                                LogUtils.w(data);
                            }

                            @Override
                            public void onActivityNotFound(Throwable e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onDenied(@NonNull List<String> deniedForever, @NonNull List<String> denied) {
                        Toast.makeText(MainActivity.this, "[read external storage] permission denied", Toast.LENGTH_LONG).show();
                    }
                }, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public void halfDialog(View view) {
        ImgDataSeletor.startPickOneWitchDialog(this, new TakeOnePhotoListener() {
            @Override
            public void onSuccess(String path) {
               /* File file = new File(path);
                boolean delete = file.delete();
                ToastUtils.showLong("是否删除成功:"+ delete);*/
                //即使是true,也会被小米系统拦截
                FileDeleteUtil.deleteImage(path,true, new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Boolean aBoolean) {
                        ToastUtils.showLong("是否删除成功:"+ aBoolean);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

            }

            @Override
            public void onFail(String path, String msg) {

            }

            @Override
            public void onCancel() {

            }
        });
    }
}
