package com.forabetterlife.dtq.myunsplash.data.local;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by DTQ on 5/28/2018.
 */

@Entity(tableName = "Wallpaper")
public final class WallpaperEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "entryid")
    private String mId;

    @NonNull
    @ColumnInfo(name = "raw_url")
    private String mRaWUrl;

    @NonNull
    @ColumnInfo(name = "full_url")
    private String mFullUrl;

    @NonNull
    @ColumnInfo(name = "regular_url")
    private String mRegularUrl;

    @NonNull
    @ColumnInfo(name = "small_url")
    private String mSmallUrl;

    @NonNull
    @ColumnInfo(name = "thumb_url")
    private String mThumbUrl;

    @NonNull
    @ColumnInfo(name = "artist_name")
    private String mArtistName;

    public WallpaperEntity() {
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        mId = id;
    }

    @NonNull
    public String getRaWUrl() {
        return mRaWUrl;
    }

    public void setRaWUrl(@NonNull String raWUrl) {
        mRaWUrl = raWUrl;
    }

    @NonNull
    public String getFullUrl() {
        return mFullUrl;
    }

    public void setFullUrl(@NonNull String fullUrl) {
        mFullUrl = fullUrl;
    }

    @NonNull
    public String getRegularUrl() {
        return mRegularUrl;
    }

    public void setRegularUrl(@NonNull String regularUrl) {
        mRegularUrl = regularUrl;
    }

    @NonNull
    public String getSmallUrl() {
        return mSmallUrl;
    }

    public void setSmallUrl(@NonNull String smallUrl) {
        mSmallUrl = smallUrl;
    }

    @NonNull
    public String getThumbUrl() {
        return mThumbUrl;
    }

    public void setThumbUrl(@NonNull String thumbUrl) {
        mThumbUrl = thumbUrl;
    }

    @NonNull
    public String getArtistName() {
        return mArtistName;
    }

    public void setArtistName(@NonNull String artistName) {
        mArtistName = artistName;
    }

    public void setId(int id) {
        this.id = id;
    }
}
