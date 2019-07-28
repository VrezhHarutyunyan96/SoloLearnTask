package com.sololearn.android.handler.appstate;

public interface AppLifeCycleListener {
    void onBackground();

    void onForeground();
}
