# TransparentFragment
use a transparent fragment to handle callbacks of activity in listener style api



# gradle

Add the JitPack repository in your build.gradle (top level module):

```
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

And add next dependencies in the build.gradle of the module:

```
dependencies {
    implementation 'com.github.hss01248:transfragment'
   
}
```

# BaseTransFragment

define a class extends BaseTransFragment

define your own api in the fragment,and use it

just like: 

```
new GoOutOfAppForResultFragment(fragment,info).goOutApp(listener);
```



# startActivity and getCallback

```
StartActivityUtil.startActivity(this, ActivityDemo2.class, null,true,
        new TheActivityListener<ActivityDemo2>() {
            @Override
            public void onActivityCreated(@NonNull ActivityDemo2 activity, @Nullable Bundle savedInstanceState) {
            //这里设置数据,然后在具体activityOnCreate()方法的super.onCreate()之后直接拿数据
                activity.setData(666);
                Toast.makeText(activity, "activity oncreate 回调", Toast.LENGTH_LONG).show();
            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                Toast.makeText(MainActivity.this, "activity onActivityResult 回调", Toast.LENGTH_LONG).show();
            }
        });
```

# 

### start a intent to open a third party app and get result in callback:

```
StartActivityUtil.goOutAppForResult(this, intent, new OutActivityResultListener() {


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        boolean hasPermission =   NotificationManagerCompat.from(MainActivity.this).areNotificationsEnabled();
        Toast.makeText(MainActivity.this,"通知栏权限:"+hasPermission,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityNotFound(Throwable e) {

    }
});
```



