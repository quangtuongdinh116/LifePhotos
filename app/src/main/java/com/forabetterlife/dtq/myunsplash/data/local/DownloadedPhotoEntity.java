package com.forabetterlife.dtq.myunsplash.data.local;

import android.support.annotation.NonNull;

/**
 * Created by DTQ on 4/16/2018.
 */

public class DownloadedPhotoEntity {

    @NonNull
    private String mId;

    @NonNull
    private String mRaWUrl;

    @NonNull
    private String mFullUrl;

    @NonNull
    private String mRegularUrl;

    @NonNull
    private String mSmallUrl;

    @NonNull
    private String mThumbUrl;

    @NonNull
    private String mArtistName;

    public DownloadedPhotoEntity() {
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
}
