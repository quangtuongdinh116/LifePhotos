package com.forabetterlife.dtq.myunsplash.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.forabetterlife.dtq.myunsplash.data.local.wallpaper.WallpaperDao;

import javax.inject.Singleton;

/**
 * Created by DTQ on 3/24/2018.
 */


@Database(entities = {FavoriteEntity.class}, version = 3)
public abstract class MyUnsplashDatabase extends RoomDatabase {
    private static MyUnsplashDatabase INSTANCE;


    public abstract FavoriteDao favoriteDao();
//    public abstract WallpaperDao wallpaperDao();
    private static final Object mObjectLock = new Object();

    public static MyUnsplashDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (mObjectLock) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),MyUnsplashDatabase.class
                ,"LifePhotoss.db").build();
            }
        }
        return INSTANCE;
    }
}


