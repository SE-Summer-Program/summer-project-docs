package com.sjtubus;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.mob.MobSDK;
import com.sjtubus.activity.MainActivity;
import com.sjtubus.user.ReminderManager;
import com.sjtubus.user.UserChangeEvent;
import com.sjtubus.user.UserManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

//import com.avos.avoscloud.AVException;
//import com.avos.avoscloud.AVInstallation;
//import com.avos.avoscloud.AVOSCloud;
//import com.avos.avoscloud.PushService;
//import com.avos.avoscloud.SaveCallback;
//import com.mcxiaoke.packer.helper.PackerNg;
//import com.squareup.leakcanary.LeakCanary;
//import com.squareup.leakcanary.RefWatcher;
//import com.tencent.bugly.Bugly;
//import com.tencent.bugly.crashreport.CrashReport;
//import com.tencent.stat.StatConfig;

/**
 * Created by Allen on 2018/7/3.
 */

public class App extends Application {

    @SuppressWarnings("StaticFieldLeak")
    private static Context context;
    final private static String SharedPrefsCookie = "buscookie";
    final private static String SharedPrefsReminder = "busreminder";
    final private static String AppVersionName = "1.1";

    private String installationId;

    public String getInstallationId() {
        return installationId;
    }

    public static Context getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        EventBus.getDefault().register(this);
        UserManager.init();
        MobSDK.init(this);
        initLeanCloud();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void initReminderManager(UserChangeEvent event) {
        if (UserManager.getInstance().isLogin()) {
            ReminderManager manager = new ReminderManager();
            manager.synchronize();
        }
    }

    public static SharedPreferences getSharedPrefsCookie() {
        return context.getSharedPreferences(SharedPrefsCookie, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSharedPrefsReminder() {
        return context.getSharedPreferences(SharedPrefsReminder, Context.MODE_PRIVATE);
    }

    public String getUserAgent() {
        String packageName = context.getPackageName();
        String versionName;
        try {
            versionName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionName = "UNKNOWN";
        }
        String userAgent = "Android" + "/" + versionName;
        return userAgent;
    }

    private void initLeanCloud() {
        final String appID = "UTfPOKjAoRvO8m7Gux0964oT-gzGzoHsz";
        final String appKey = "tnpkj8g2EyCFWydpDrbcXj3X";
        AVOSCloud.initialize(this, appID, appKey);

        // 启用崩溃错误统计
        AVAnalytics.enableCrashReport(this.getApplicationContext(), true);
        AVOSCloud.setLastModifyEnabled(true);
        AVOSCloud.setDebugLogEnabled(true);

        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // 保存成功
                    installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                    // 关联  installationId 到用户表等操作……
                    Log.i("LEANCLOUD",installationId);
                } else {
                    // 保存失败，输出错误信息
                }
            }
        });
        PushService.setDefaultPushCallback(this, MainActivity.class);
        // 订阅频道，当该频道消息到来的时候，打开对应的 Activity
        PushService.subscribe(this, "public", MainActivity.class);
    }

//    protected RefWatcher setupLeakCanary() {
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return RefWatcher.DISABLED;
//        }
//        return LeakCanary.install(this);
//    }

//    protected void initMta() {
//        Schedule channel = PackerNg.getChannel(getApplicationContext());
//        if (channel == null) channel = "";
//        StatConfig.setInstallChannel(channel);
//    }

    //    private void initBugly() {
//        final Schedule buglyId = "900018050";
//        final Boolean debug = false;
//        Bugly.init(getApplicationContext(), buglyId, debug);
//        CrashReport.setAppVersion(getApplicationContext(), AppVersionName);
//    }

    public static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(statusColor);
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    public static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        Window window = activity.getWindow();
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (hideStatusBarBackground) {
            //如果为全透明模式，取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置状态栏为透明
            window.setStatusBarColor(Color.TRANSPARENT);
            //设置window的状态栏不可见
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            //如果为半透明模式，添加设置Window半透明的Flag
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置系统状态栏处于可见状态
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
        //view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }
}
