package com.forabetterlife.dtq.myunsplash.data;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.forabetterlife.dtq.myunsplash.data.local.FavoriteEntity;
import com.forabetterlife.dtq.myunsplash.data.local.LocalDataSource;
import com.forabetterlife.dtq.myunsplash.data.remote.RemoteDataSource;
import com.forabetterlife.dtq.myunsplash.data.service.PhotoService;
import com.forabetterlife.dtq.myunsplash.utils.WallpaperType;
import com.google.common.base.Strings;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.work.WorkStatus;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by DTQ on 3/22/2018.
 */

@Singleton
public class PhotoRepository implements PhotoDataSource {
    private static PhotoRepository INSTANCE;

    @NonNull
    private LocalDataSource mLocalDataSource;

    @NonNull
    private RemoteDataSource mRemoteDataSource;

    @Inject
    PhotoRepository(@NonNull LocalDataSource localDataSource, @NonNull RemoteDataSource remoteDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }

    public static PhotoRepository getInstance(@NonNull LocalDataSource localDataSource, @NonNull RemoteDataSource remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new PhotoRepository(localDataSource, remoteDataSource);
        }
        return INSTANCE;
    }


    @Override
    public void loadAllPhotos(LoadAllPhotosCallback callback, int page) {
        mRemoteDataSource.loadAllPhotos(callback, page);
    }

    @Override
    public void searchPhotoByQuery(String query, SearchPhotoByQueryCallback callback, int page) {
        if(Strings.isNullOrEmpty(query))
            return;
        mRemoteDataSource.searchPhotoByQuery(query, callback, page);

    }

    @Override
    public void reportDownload(String id, PhotoService.OnReportDownloadListener callback) {
        if(Strings.isNullOrEmpty(id))
            return;
        mRemoteDataSource.reportDownload(id, callback);
    }

    @Override
    public void isFavorite(String id,PhotoDataSource.CheckFavoriteStatusCallback callback) {
        if (Strings.isNullOrEmpty(id))
            return;
        mLocalDataSource.isFavorite(id,callback);
    }

    @Override
    public void changeFavoriteStatus(FavoriteEntity photo) {
        checkNotNull(photo);
        mLocalDataSource.changeFavoriteStatus(photo);
    }

    @Override
    public void loadFavorites(LoadFavoritesCallback callback) {
        mLocalDataSource.loadFavorites(callback);
    }

    @Override
    public void loadDownloadedPhotos(LoadDownloadedPhotosCallback callback) {
        mLocalDataSource.loadDownloadedPhotos(callback);
    }

    @Override
    public String getPhotoDownloadQuality() {
        return mLocalDataSource.getPhotoDownloadQuality();
    }

    @Override
    public String getPhotoShowingQuality() {
        return mLocalDataSource.getPhotoShowingQuality();
    }

    @Override
    public String getSearchQueryWantedPhoto() {
        return mLocalDataSource.getSearchQueryWantedPhoto();
    }

    @Override
    public void changeWantedPhotoServiceStatus(boolean turnOn, ScheduleFetchNewWantedPhoto callback, Context context) {
       mLocalDataSource.changeWantedPhotoServiceStatus(turnOn, callback,context);

    }

    @Override
    public void saveLastSearchWantedPhotoId(String lastSearchId) {
        mLocalDataSource.saveLastSearchWantedPhotoId(lastSearchId);
    }

    @Override
    public void changeWallpaperStatus(long duration, String type, Context context,PhotoDataSource.ScheduleChangeWallpaper callback) {
        mLocalDataSource.changeWallpaperStatus(duration, type, context,callback);
    }

    @Override
    public void loadWallpaperStatus(LoadWallpaperStatus callback) {
        mLocalDataSource.loadWallpaperStatus(callback);

    }

    @Override
    public void clearMemory(Context context) {
        mLocalDataSource.clearMemory(context);
    }

    @Override
    public LiveData<List<WorkStatus>> getScheduleStatus() {
        return mLocalDataSource.getScheduleStatus();
    }


}
