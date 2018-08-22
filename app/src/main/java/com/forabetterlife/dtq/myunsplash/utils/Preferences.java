package com.forabetterlife.dtq.myunsplash.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.forabetterlife.dtq.myunsplash.R;

public class Preferences {


    public static String getString(Context context, String key, String defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, defValue);
    }

    public static  String getString(Context context, int resourceId, String defValue) {
        return getString(context, context.getString(resourceId), defValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    public static Boolean getBoolean(Context context, String key, boolean defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(key, defValue);
    }

    public static Boolean getWallpaperIsOnStatus(Context context) {
        return getBoolean(context, context.getString(R.string.preference_key_is_on_wallpaper),false);
    }

    public static void changeWallpaperIsOnStatus(Context context, Boolean value) {
        putBoolean(context, context.getResources().getString(R.string.preference_key_is_on_wallpaper),value);
    }
}
