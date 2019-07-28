package com.sololearn.android;

import android.app.Application;

import com.sololearn.android.handler.appstate.AppLifeCycleListener;

public class AppApplication extends Application implements AppLifeCycleListener {
    public static AppApplication appApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        appApplication = this;
    }

    @Override
    public void onBackground() {

    }

    @Override
    public void onForeground() {

    }
}
