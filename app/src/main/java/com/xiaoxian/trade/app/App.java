package com.xiaoxian.trade.app;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.xiaoxian.trade.di.component.AppComponent;
import com.xiaoxian.trade.di.component.DaggerAppComponent;
import com.xiaoxian.trade.di.module.AppModule;

import cn.bmob.v3.Bmob;

public class App extends Application {
    AppComponent mAppComponent;

    public static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        initMap();
        initBmob();
        initShare();
        //实例化AppComponent，传入Module实例，将目标类依赖实例注入目标类中
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public void initMap() {
        SDKInitializer.initialize(this);
    }

    public void initBmob() {
        Bmob.initialize(this, Constants.BMOB_APP_ID);
    }

    //微博分享（后期加入）
    public void initShare() {
        Config.REDIRECT_URL = Constants.SINA_URL;
        PlatformConfig.setSinaWeibo(Constants.SINA_APP_KEY, Constants.SINA_SECRET);
    }

    //向外界依赖提供该AppComponent（只初始化一次）
    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
