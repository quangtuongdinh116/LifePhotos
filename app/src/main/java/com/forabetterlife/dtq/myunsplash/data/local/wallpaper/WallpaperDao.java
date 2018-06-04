package com.forabetterlife.dtq.myunsplash.data.local.wallpaper;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.forabetterlife.dtq.myunsplash.data.local.WallpaperEntity;

import java.util.List;

/**
 * Created by DTQ on 5/28/2018.
 */

@Dao
public interface WallpaperDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWallpaper(WallpaperEntity wallpaperEntity);

    @Query("SELECT * FROM Wallpaper WHERE id = :wallpaperId")
    WallpaperEntity getWallpaperById(String wallpaperId);

    @Query("SELECT * FROM Wallpaper")
    List<WallpaperEntity> getAllWallpapers();
}
