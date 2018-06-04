package com.forabetterlife.dtq.myunsplash.data;

import android.content.Context;

import com.forabetterlife.dtq.myunsplash.data.local.DownloadedPhotoEntity;
import com.forabetterlife.dtq.myunsplash.data.local.FavoriteEntity;
import com.forabetterlife.dtq.myunsplash.data.model.PhotoResponse;
import com.forabetterlife.dtq.myunsplash.data.model.SearchPhotoResponse;
import com.forabetterlife.dtq.myunsplash.data.service.PhotoService;
import com.forabetterlife.dtq.myunsplash.utils.WallpaperType;

import java.util.List;

/**
 * Created by DTQ on 3/22/2018.
 */

public interface PhotoDataSource {
    void loadAllPhotos(LoadAllPhotosCallback callback, int page);
    void searchPhotoByQuery(String query, SearchPhotoByQueryCallback callback, int page);
    void reportDownload(String id, PhotoService.OnReportDownloadListener callback);
    void isFavorite(String id, CheckFavoriteStatusCallback callback);
    void changeFavoriteStatus(FavoriteEntity photo);
    void loadFavorites(LoadFavoritesCallback callback);
    void loadDownloadedPhotos(LoadDownloadedPhotosCallback callback);
    String getPhotoDownloadQuality();
    String getPhotoShowingQuality();
    String getSearchQueryWantedPhoto();
    void changeWantedPhotoServiceStatus(boolean turnOn, PhotoDataSource.ScheduleFetchNewWantedPhoto callback, Context context);
    void saveLastSearchWantedPhotoId(String lastSearchId);
    void changeWallpaperStatus(long duration, String type, Context context,PhotoDataSource.ScheduleChangeWallpaper callback);
    void loadWallpaperStatus(LoadWallpaperStatus callback);

    public interface LoadAllPhotosCallback {
        void onLoadSuccess(List<PhotoResponse> photoResponseList);
        void onLoadFail();
    }

    public interface SearchPhotoByQueryCallback {
        void onLoadSuccess(SearchPhotoResponse searchPhotoResponse);
        void onLoadFail();
    }

    public interface CheckFavoriteStatusCallback {
        void onCheckSuccess(boolean isFavorite);
    }

    public interface LoadFavoritesCallback {
        void onLoadSuccess(List<FavoriteEntity> favoriteList);
        void onLoadFail();
    }

    public interface LoadDownloadedPhotosCallback {
        void onLoadSuccess(List<DownloadedPhotoEntity> downloadedList);
        void onLoadFail();
    }

    public interface LoadWallpaperStatus {
        void onLoadSuccess(String type, boolean isOn, long duration);
        void onLoadFail();
    }

    public interface ScheduleFetchNewWantedPhoto {
        void onScheduleSuccess();
        void onScheduleFail();
        void onStopSuccess();
        void onStopFail();
    }

    public interface ScheduleChangeWallpaper {
        void onScheduleSuccess();
        void onScheduleFail();
        void onStopSuccess();
        void onStopFail();
    }
}
