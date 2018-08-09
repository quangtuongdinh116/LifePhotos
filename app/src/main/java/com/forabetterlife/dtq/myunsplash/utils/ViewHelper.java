package com.forabetterlife.dtq.myunsplash.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

public class ViewHelper {

    @SuppressWarnings("ConstantConditions") public static boolean isTablet(@NonNull Context context) {
        return context != null && isTablet(context.getResources());
    }

    private static boolean isTablet(@NonNull Resources resources) {
        return (resources.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void tintDrawable(@NonNull Drawable drawable, @ColorInt int color) {
        drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @ColorInt public static int getTertiaryTextColor(@NonNull Context context) {
        return getColorAttr(context, android.R.attr.textColorTertiary);
    }

    @ColorInt private static int getColorAttr(@NonNull Context context, int attr) {
        Resources.Theme theme = context.getTheme();
        TypedArray typedArray = theme.obtainStyledAttributes(new int[]{attr});
        final int color = typedArray.getColor(0, Color.LTGRAY);
        typedArray.recycle();
        return color;
    }
}
