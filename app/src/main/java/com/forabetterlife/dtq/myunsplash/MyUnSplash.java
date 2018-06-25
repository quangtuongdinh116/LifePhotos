package com.forabetterlife.dtq.myunsplash;

import android.app.Application;
import android.content.Context;

import com.forabetterlife.dtq.myunsplash.data.PhotoRepository;
import com.forabetterlife.dtq.myunsplash.di.DaggerAppComponent;


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

    public static final double CAN_BE_WALLPAPER = 0.83;

    public static final int DEFAULT_PER_PAGE = 50;

    public static final String KEY_WALLPAPER_TYPE = "KEY_WALLPAPER_TYPE";

    public static final String TAG_OUTPUT = "OUTPUT";

    // The name of the image manipulation work
    public static final String CHANGE_WALLPAPER_WORK_NAME = "change_wallpaper_work_name";

    @Override
    public void onCreate() {
        super.onCreate();
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
}
