package org.cuieney.videolife;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.admodule.AdModule;
import com.admodule.admob.AdMobBanner;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.tencent.bugly.crashreport.CrashReport;

import org.cuieney.videolife.di.component.AppComponent;
import org.cuieney.videolife.di.component.DaggerAppComponent;
import org.cuieney.videolife.di.module.AppModule;
import org.cuieney.videolife.di.module.RetrofitModule;
import org.cuieney.videolife.ui.act.MainActivity;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by cuieney on 17/2/21.
 */

public class App extends Application {

    private AppComponent appComponent;
    private static App app;
    private Set<Activity> allActivities;

    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;
    public static float DIMEN_RATE = -1.0F;
    public static int DIMEN_DPI = -1;

    public static App getInstance() {
        return app;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void getScreenSize() {
        WindowManager windowManager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        DIMEN_RATE = dm.density / 1.0F;
        DIMEN_DPI = dm.densityDpi;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if(SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
    }

    public static void addShortcut(Context context, Class clazz, String appName, int ic_launcher) {
        // 安装的Intent
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.putExtra("tName", appName);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
        shortcutIntent.setClassName(context, clazz.getName());
        //        shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // 快捷名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(R.string.app_name));
        // 快捷图标是否允许重复
        shortcut.putExtra("duplicate", false);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        // 快捷图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, ic_launcher);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        // 发送广播
        context.sendBroadcast(shortcut);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initAppComponent();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("addShortcut", false)) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("addShortcut", true).apply();
            addShortcut(app, MainActivity.class, getString(R.string.app_name), R.mipmap.ic_launcher);
        }
//        AutoLayoutConifg.getInstance().useDeviceSize();
//        getScreenSize();

//        //内存泄漏检测
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
//        BlockCanary.install(this, new AppBlockCanaryContext()).start();

//        CrashHandler.getInstance().initCrashHandler(this);


        AdModule.init(new AdModule.AdCallBack() {
            @Override
            public Application getApplication() {
                return App.app;
            }

            @Override
            public String getAppId() {
                return "ca-app-pub-9880857526519562~2748080363";
            }

            @Override
            public boolean isAdDebug() {
                return false;
            }

            @Override
            public boolean isLogDebug() {
                return false;
            }

            @Override
            public String getAdMobNativeAdId() {
                return null;
            }

            @Override
            public String getBannerAdId() {
                return "ca-app-pub-9880857526519562/3592180757";
            }

            @Override
            public String getInterstitialAdId() {
                return "ca-app-pub-9880857526519562/7423614552";
            }

            @Override
            public String getTestDevice() {
                return null;
            }

            @Override
            public String getRewardedVideoAdId() {
                return null;
            }

            @Override
            public String getFBNativeAdId() {
                return "146773445984805_146773862651430";
            }
        });

        AdModule.getInstance().getAdMob().initInterstitialAd();
        AdModule.getInstance().getAdMob().requestNewInterstitial();

        sIsCoolStart = true;

        CrashReport.initCrashReport(getApplicationContext(), "c55a94a9a6", false);
    }

    public static boolean sIsCoolStart;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .retrofitModule(new RetrofitModule(this))
                .build();
    }
}
