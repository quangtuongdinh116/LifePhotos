package com.forabetterlife.dtq.myunsplash.data.remote.wantedphoto;

import android.support.annotation.NonNull;


import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.data.PhotoDataSource;
import com.forabetterlife.dtq.myunsplash.data.model.SearchPhotoResponse;
import com.forabetterlife.dtq.myunsplash.data.service.SearchService;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by DTQ on 3/30/2018.
 */

public class SearchWantedPhotoService {

    private static SearchWantedPhotoService INSTANCE;

    @NonNull
    private SearchService mSearchService;

    public SearchWantedPhotoService(@NonNull SearchService searchService) {
        mSearchService = searchService;
    }

    public static SearchWantedPhotoService getInstance(SearchService searchService) {
        if (INSTANCE == null) {
            INSTANCE = new SearchWantedPhotoService(searchService);
        }
        return INSTANCE;
    }

    public void searchPhotoByQuery(String query, PhotoDataSource.SearchPhotoByQueryCallback callback) {
        mSearchService.requestPhotos(1,query, MyUnSplash.getAppId(MyUnSplash.getInstance()),mSearchPhotoListener,callback);
    }

    SearchService.OnRequestPhotosListener mSearchPhotoListener = new SearchService.OnRequestPhotosListener() {
        @Override
        public void onRequestPhotosSuccess(Call<SearchPhotoResponse> call, Response<SearchPhotoResponse> response, PhotoDataSource.SearchPhotoByQueryCallback callback) {

            if (response.code() == 200) {
                SearchPhotoResponse searchPhotoResponse = response.body();
                callback.onLoadSuccess(searchPhotoResponse);
            } else {
                callback.onLoadFail();
            }
        }

        @Override
        public void onRequestPhotosFailed(Call<SearchPhotoResponse> call, Throwable t, PhotoDataSource.SearchPhotoByQueryCallback callback) {
            callback.onLoadFail();
        }
    };
}
