package com.sololearn.android;

import android.app.Application;

import com.sololearn.android.handler.appstate.AppLifeCycleListener;

public class AppApplication extends Application implements AppLifeCycleListener {
    public static AppApplication appApplication;
    public static boolean isBackground = false;

    @Override
    public void onCreate() {
        super.onCreate();
        appApplication = this;
    }

    @Override
    public void onBackground() {
        isBackground = true;
    }

    @Override
    public void onForeground() {
        isBackground = false;
    }
}
