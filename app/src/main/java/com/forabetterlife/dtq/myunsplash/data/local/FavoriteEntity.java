package com.forabetterlife.dtq.myunsplash.data.local;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DTQ on 3/24/2018.
 */

@Entity(tableName = "Favorites")
public final class FavoriteEntity {



    @PrimaryKey
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

    @ColumnInfo(name = "width")
    private Integer width;

    @ColumnInfo(name = "height")
    private Integer height;

    public FavoriteEntity() {
    }

    @NonNull
    public String getArtistName() {
        return mArtistName;
    }

    public void setArtistName(@NonNull String artistName) {
        mArtistName = artistName;
    }

    @NonNull
    public String getId() {
        return mId;
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

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}



