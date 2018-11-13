package com.forabetterlife.dtq.myunsplash.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;


/**
 * Created by DTQ on 3/24/2018.
 */

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPhoto(FavoriteEntity photo);

    @Query("SELECT * FROM Favorites WHERE entryid = :id")
    FavoriteEntity findPhoto(String id);

    @Query("DELETE FROM Favorites WHERE entryid = :id")
    int deletePhotoById(String id);

    @Query("SELECT * FROM Favorites")
    List<FavoriteEntity> loadFavorites();
}






























