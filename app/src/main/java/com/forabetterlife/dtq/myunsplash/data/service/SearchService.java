package com.forabetterlife.dtq.myunsplash.data.service;

import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.data.PhotoDataSource;
import com.forabetterlife.dtq.myunsplash.data.model.SearchPhotoResponse;
import com.forabetterlife.dtq.myunsplash.data.remote.SearchApi;
import com.google.gson.GsonBuilder;


import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DTQ on 3/23/2018.
 */

public class SearchService {

    public interface OnRequestPhotosListener {
        void onRequestPhotosSuccess(Call<SearchPhotoResponse> call, Response<SearchPhotoResponse> response, PhotoDataSource.SearchPhotoByQueryCallback callback);
        void onRequestPhotosFailed(Call<SearchPhotoResponse> call, Throwable t,PhotoDataSource.SearchPhotoByQueryCallback callback);
    }


    public Observable<SearchPhotoResponse> requestPhotos(int page, String query, String access_key) {
        return buildApi(buildClient()).searchPhotoByQuery(page,query,access_key);
    }

    private OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    private SearchApi buildApi(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(MyUnSplash.UNSPLASH_API_BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new GsonBuilder()
                                        .setDateFormat(MyUnSplash.DATE_FORMAT)
                                        .create()))
                .build()
                .create((SearchApi.class));
    }

    public static SearchService getService() {
        return new SearchService();
    }

}
