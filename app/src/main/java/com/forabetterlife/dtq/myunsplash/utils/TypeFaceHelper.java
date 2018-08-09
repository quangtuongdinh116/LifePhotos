package com.forabetterlife.dtq.myunsplash.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;


public class TypeFaceHelper {

    private static Typeface typeFace;

    public static void generateTypeface(Context context) {
        typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/app_font.ttf");
    }

    public static void applyTypeface(TextView textView) {
        textView.setTypeface(typeFace);
    }

    public static Typeface getTypeface() {
        return typeFace;
    }
}
