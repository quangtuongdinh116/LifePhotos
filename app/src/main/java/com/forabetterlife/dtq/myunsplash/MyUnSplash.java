package com.forabetterlife.dtq.myunsplash;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.forabetterlife.dtq.myunsplash.data.PhotoRepository;
import com.forabetterlife.dtq.myunsplash.di.DaggerAppComponent;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by DTQ on 3/22/2018.
 */

public class MyUnSplash extends DaggerApplication {
    @Inject
    PhotoRepository photoRepository;


    public static final String UNSPLASH_API_BASE_URL = "https://api.unsplash.com/";
    public static final String DATE_FORMAT = "yyyy/MM/dd";
    public static final String DOWNLOAD_PHOTO_FORMAT = ".jpg";
    public static final String DOWNLOAD_PATH = "/Pictures/LifePhotos/";

    public static final String FAVORITE = "Favorites";
    public static final String WANTED_PHOTO = "Wanted Photos";
    public static final String RANDOM_PHOTO = "500.000 Photos";

    public static final String UNSPLASH_UTM_PARAMETERS = "?utm_source=resplash&utm_medium=referral&utm_campaign=api-credit";

    public static final double CAN_BE_WALLPAPER = 0.83;

    public static final int DEFAULT_PER_PAGE = 30;

    public static final String KEY_WALLPAPER_TYPE = "KEY_WALLPAPER_TYPE";

    public static final String TAG_OUTPUT = "OUTPUT";

    // The name of the image manipulation work
    public static final String CHANGE_WALLPAPER_WORK_NAME = "change_wallpaper_work_name";

    private Drawable drawable;

    private RefWatcher refWatcher;

//    public static RefWatcher getRefWatcher(Context context) {
//        MyUnSplash application = (MyUnSplash) context.getApplicationContext();
//        return application.refWatcher;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
//        refWatcher = LeakCanary.install(this);
        initialize();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    public static String getAppId(Context c) {
           return BuildConfig.DEV_APP_ID;
    }

    private static MyUnSplash instance;


    public static MyUnSplash getInstance() {
        return instance;
    }


    private void initialize() {
        instance = this;
    }

    public PhotoRepository getPhotoRepository() {
        return photoRepository;
    }

    public void setDrawable(Drawable d) {
        this.drawable = d;
    }

    public Drawable getDrawable() {
        return drawable;
    }
}
