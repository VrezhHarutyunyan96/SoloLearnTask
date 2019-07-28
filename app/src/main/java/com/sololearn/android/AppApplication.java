package com.sololearn.android;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sololearn.android.handler.appstate.AppLifeCycleListener;
import com.sololearn.android.handler.appstate.AppLifecycleHandler;

public class AppApplication extends Application implements AppLifeCycleListener {
    public static AppApplication appApplication;
    public static boolean isBackground = false;

    @Override
    public void onCreate() {
        super.onCreate();
        appApplication = this;
        AppLifecycleHandler lifeCycleHandler = new AppLifecycleHandler(this);
        registerLifecycleHandler(lifeCycleHandler);
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    @Override
    public void onBackground() {
        isBackground = true;
    }

    @Override
    public void onForeground() {
        isBackground = false;
    }

    private void registerLifecycleHandler(AppLifecycleHandler lifeCycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler);
        registerComponentCallbacks(lifeCycleHandler);
    }
}
