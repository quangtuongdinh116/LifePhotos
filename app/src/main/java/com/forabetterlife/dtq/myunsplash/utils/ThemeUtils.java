package com.forabetterlife.dtq.myunsplash.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ThemeUtils {

    private final static String TAG = "ThemeUtils";

    public @StringDef({Theme.DARK_GREEN, Theme.BLACK})
    @Retention(RetentionPolicy.SOURCE)
    @interface Theme {
        String DARK_GREEN = "DARK GREEN";
        String BLACK = "BLACK";
    }

    public static @Theme String getTheme(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("theme_key", Theme.DARK_GREEN);
    }

    @ColorInt
    public static int getThemeAttrColor(Context context, @AttrRes int colorAttr) {
        TypedArray array = context.obtainStyledAttributes(null, new int[]{colorAttr});
        try {
            return array.getColor(0, 0);
        } finally {
            array.recycle();
        }
    }

    @DrawableRes
    public static int getThemeAttrDrawable(Context context, @AttrRes int drawableAttr) {
        TypedArray array = context.obtainStyledAttributes(null, new int[]{drawableAttr});
        try {
            return array.getResourceId(0, 0);
        } finally {
            array.recycle();
        }
    }
}
