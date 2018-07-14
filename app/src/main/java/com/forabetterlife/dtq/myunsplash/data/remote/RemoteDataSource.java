package com.forabetterlife.dtq.myunsplash.data.remote;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.data.service.PhotoService;
import com.forabetterlife.dtq.myunsplash.data.PhotoDataSource;
import com.forabetterlife.dtq.myunsplash.data.service.SearchService;
import com.forabetterlife.dtq.myunsplash.data.local.FavoriteEntity;
import com.forabetterlife.dtq.myunsplash.data.model.PhotoResponse;
import com.forabetterlife.dtq.myunsplash.data.model.SearchPhotoResponse;
import com.forabetterlife.dtq.myunsplash.utils.WallpaperType;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.work.WorkStatus;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by DTQ on 3/22/2018.
 */

@Singleton
public class RemoteDataSource implements PhotoDataSource {
    private static final String TAG = "RemoteDataSource";

    private static RemoteDataSource INSTANCE;

    @NonNull
    private PhotoService mPhotoService;

    @NonNull
    private SearchService mSearchService;


    @Inject
    public RemoteDataSource(@NonNull PhotoService photoService, @NonNull SearchService searchService) {
        mPhotoService = photoService;
        mSearchService = searchService;
    }

    public static RemoteDataSource getInstance(PhotoService photoService,SearchService searchService) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource(photoService,searchService);
        }
        return INSTANCE;
    }


    @Override
    public void loadAllPhotos(LoadAllPhotosCallback callback, int page) {
        mPhotoService.requestPhotos(page,MyUnSplash.DEFAULT_PER_PAGE, MyUnSplash.getAppId(MyUnSplash.getInstance()),mPhotoRequestListener,callback);
    }

    @Override
    public void searchPhotoByQuery(String query, SearchPhotoByQueryCallback callback, int page) {
        mSearchService.requestPhotos(page,query,MyUnSplash.getAppId(MyUnSplash.getInstance()),mSearchPhotoListener,callback);
    }

    @Override
    public void reportDownload(String id, PhotoService.OnReportDownloadListener callback) {
        mPhotoService.reportDownload(id,callback);
    }

    @Override
    public void isFavorite(String id, PhotoDataSource.CheckFavoriteStatusCallback callback) {

    }

    @Override
    public void changeFavoriteStatus(FavoriteEntity photo) {

    }

    @Override
    public void loadFavorites(LoadFavoritesCallback callback) {

    }

    @Override
    public void loadDownloadedPhotos(LoadDownloadedPhotosCallback callback) {

    }

    @Override
    public String getPhotoDownloadQuality() {
        return null;
    }

    @Override
    public String getPhotoShowingQuality() {
        return null;
    }

    @Override
    public String getSearchQueryWantedPhoto() {
        return null;
    }

    @Override
    public void changeWantedPhotoServiceStatus(boolean turnOn, ScheduleFetchNewWantedPhoto callback, Context context) {

    }

    @Override
    public void changeWallpaperStatusAPIBelow21(long duration, String type, Context context, ScheduleChangeWallpaper callback) {

    }

    @Override
    public void saveLastSearchWantedPhotoId(String lastSearchId) {

    }

    @Override
    public void changeWallpaperStatus(long duration, String type, Context context, PhotoDataSource.ScheduleChangeWallpaper callback) {

    }

    @Override
    public void loadWallpaperStatus(LoadWallpaperStatus callback) {

    }

    @Override
    public void clearMemory(Context context) {

    }

    @Override
    public LiveData<List<WorkStatus>> getScheduleStatus() {
        return null;
    }


    SearchService.OnRequestPhotosListener mSearchPhotoListener = new SearchService.OnRequestPhotosListener() {
        @Override
        public void onRequestPhotosSuccess(Call<SearchPhotoResponse> call, Response<SearchPhotoResponse> response, SearchPhotoByQueryCallback callback) {
            Log.i(TAG, "response code is: " + response.code());
            if (response.code() == 200) {
                SearchPhotoResponse searchPhotoResponse = response.body();
                callback.onLoadSuccess(searchPhotoResponse);
            } else {
                callback.onLoadFail();
            }
        }

        @Override
        public void onRequestPhotosFailed(Call<SearchPhotoResponse> call, Throwable t, SearchPhotoByQueryCallback callback) {
            callback.onLoadFail();
        }
    };


    PhotoService.OnRequestPhotosListener mPhotoRequestListener = new PhotoService.OnRequestPhotosListener() {

        @Override
        public void onRequestPhotosSuccess(Call<List<PhotoResponse>> call, Response<List<PhotoResponse>> response, LoadAllPhotosCallback callback) {
            Log.i(TAG, "response code is: " + response.code());
            if (response.code() == 200) {
                List<PhotoResponse> responseList = response.body();
                callback.onLoadSuccess(responseList);
            } else {
                callback.onLoadFail();
            }
        }

        @Override
        public void onRequestPhotosFailed(Call<List<PhotoResponse>> call, Throwable t, LoadAllPhotosCallback callback) {
            callback.onLoadFail();
        }
    };
}
