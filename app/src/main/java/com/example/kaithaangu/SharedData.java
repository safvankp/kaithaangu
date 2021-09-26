package com.example.kaithaangu;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedData {

    private static  SharedData sharedData;
    private final SharedPreferences sharedPreferences;
    private final Context context;

    public SharedData(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }
    public static  SharedData getInstance(Context ctx) {
        if (sharedData == null) {
            sharedData = new SharedData(ctx);
        }
        return sharedData;
    }

    public SharedPreferences getPref() {
        return sharedPreferences;
    }
    public SharedPreferences.Editor edit() {
        return getPref().edit();
    }
    public String get(String key, String defaultVal) {
        return getPref().getString(key, defaultVal);
    }
    public Boolean getBoolean(String key,boolean defaultval) {
        return getPref().getBoolean(key, defaultval);
    }


    public void putBoolean(String key, boolean val) {
        getPref().edit().putBoolean(key, val).apply();
    }
    public void put(String key, String val) {
        getPref().edit().putString(key, val).apply();
    }

}
