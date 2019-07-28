package com.sololearn.android.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.sololearn.android.AppApplication;

public class SharedHelper {
    private static final String TAG = SharedHelper.class.getName();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static void putKey(Context context, String Key, String Value) {
        if (context == null) {
            context = AppApplication.appApplication;
        }
        sharedPreferences = context.getSharedPreferences("soloShared", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(Key, Value);
        editor.apply();
    }

    public static String getKey(Context contextGetKey, String Key) {
        if (contextGetKey == null) {
            contextGetKey = AppApplication.appApplication;
        }
        sharedPreferences = contextGetKey.getSharedPreferences("soloShared", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(Key, "");
        return value;
    }

    public static void clearSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("soloShared", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    public static void removeData(Context context, String key) {
        editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

}
