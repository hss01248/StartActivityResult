[![](https://jitpack.io/v/hss01248/StartActivityResult.svg)](https://jitpack.io/#hss01248/StartActivityResult)
# TransparentFragment
use a transparent fragment to handle callbacks of activity in listener style api

![image-20201112195140088](https://gitee.com/hss012489/picbed/raw/master/picgo/1605181900175-image-20201112195140088.jpg)

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
   implementation 'com.github.hss01248.StartActivityResult:activityresult:1.0.1'//依赖这一个即可
   implementation   'com.github.hss01248.StartActivityResult:transfrag:1.0.1'
   
   com.github.hss01248.StartActivityResult:activityresult:1.1.2
com.github.hss01248.StartActivityResult:transactivity:1.1.2 //使用dialog样式的activity
com.github.hss01248.StartActivityResult:transfrag:1.1.2
   
}
```

# BaseTransFragment

define a class extends BaseTransFragment

define your own api in the fragment,and use it

just like: 

```
new GoOutOfAppForResultFragment(fragment,info).goOutApp(listener);
```

[基于透明fragment的长流程封装技巧](https://juejin.im/post/5c2f0a0951882524661d1252)



# startActivity and getResult

start a activity and set data in it's onCreate listener, rather then send data by parcelable

and get result in listener, not in activity's onActivityResult callback

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



### start a intent to open a third party app and recheck result in callback:

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



