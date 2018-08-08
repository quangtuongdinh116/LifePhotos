package com.forabetterlife.dtq.myunsplash.data.service;

import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.data.PhotoDataSource;
import com.forabetterlife.dtq.myunsplash.data.model.SearchPhotoResponse;
import com.forabetterlife.dtq.myunsplash.data.remote.SearchApi;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DTQ on 3/23/2018.
 */

public class SearchService {



    public interface OnRequestPhotosListener {
        void onRequestPhotosSuccess(Call<SearchPhotoResponse> call, Response<SearchPhotoResponse> response, PhotoDataSource.SearchPhotoByQueryCallback callback);
        void onRequestPhotosFailed(Call<SearchPhotoResponse> call, Throwable t,PhotoDataSource.SearchPhotoByQueryCallback callback);
    }


    public void requestPhotos(int page, String query, String access_key, final SearchService.OnRequestPhotosListener l, final PhotoDataSource.SearchPhotoByQueryCallback callback) {
        Call<SearchPhotoResponse> getPhotos = buildApi(buildClient()).searchPhotoByQuery(page,query ,access_key);
        getPhotos.enqueue(new Callback<SearchPhotoResponse>() {
            @Override
            public void onResponse(Call<SearchPhotoResponse> call, Response<SearchPhotoResponse> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response,callback);
                }
            }

            @Override
            public void onFailure(Call<SearchPhotoResponse> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t,callback);
                }
            }
        });

    }

    private OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    private SearchApi buildApi(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(MyUnSplash.UNSPLASH_API_BASE_URL)
                .client(client)
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
