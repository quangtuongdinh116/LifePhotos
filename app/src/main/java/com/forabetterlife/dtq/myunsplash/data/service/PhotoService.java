package com.forabetterlife.dtq.myunsplash.data.service;

import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.data.PhotoDataSource;
import com.forabetterlife.dtq.myunsplash.data.model.PhotoResponse;
import com.forabetterlife.dtq.myunsplash.data.remote.PhotoApi;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DTQ on 3/22/2018.
 */

public class PhotoService {

    private Call call;

    public static PhotoService getService() {
        return new PhotoService();
    }

    public void requestPhotos(int page, int per_page,String access_key, final OnRequestPhotosListener l, final PhotoDataSource.LoadAllPhotosCallback callback, String sort) {
        Call<List<PhotoResponse>> getPhotos = buildApi(buildClient()).getPhotos(page, per_page,access_key, sort);
        getPhotos.enqueue(new Callback<List<PhotoResponse>>() {
            @Override
            public void onResponse(Call<List<PhotoResponse>> call, Response<List<PhotoResponse>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response,callback);
                }
            }

            @Override
            public void onFailure(Call<List<PhotoResponse>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t,callback);
                }
            }
        });
        call = getPhotos;
    }

    public void requestCuratedPhotos(int page, int per_page,String access_key, final OnRequestPhotosListener l, final PhotoDataSource.LoadAllPhotosCallback callback, String sort) {
        Call<List<PhotoResponse>> getPhotos = buildApi(buildClient()).getCuratedPhotos(page, per_page,access_key, sort);
        getPhotos.enqueue(new Callback<List<PhotoResponse>>() {
            @Override
            public void onResponse(Call<List<PhotoResponse>> call, Response<List<PhotoResponse>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response,callback);
                }
            }

            @Override
            public void onFailure(Call<List<PhotoResponse>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t,callback);
                }
            }
        });
        call = getPhotos;
    }

    public interface OnRequestPhotosListener {
        void onRequestPhotosSuccess(Call<List<PhotoResponse>> call, Response<List<PhotoResponse>> response, PhotoDataSource.LoadAllPhotosCallback callback);
        void onRequestPhotosFailed(Call<List<PhotoResponse>> call, Throwable t,PhotoDataSource.LoadAllPhotosCallback callback);
    }

    private PhotoApi buildApi(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(MyUnSplash.UNSPLASH_API_BASE_URL)
                .client(client)
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new GsonBuilder()
                                        .setDateFormat(MyUnSplash.DATE_FORMAT)
                                        .create()))
                .build()
                .create((PhotoApi.class));
    }

    private OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    public void reportDownload(String id, final OnReportDownloadListener l) {
        Call<ResponseBody> reportDownload = buildApi(buildClient()).reportDownload(id);
        reportDownload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (l != null) {
                    l.onReportDownloadSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (l != null) {
                    l.onReportDownloadFailed(call, t);
                }
            }
        });
    }

    public interface OnReportDownloadListener {
        void onReportDownloadSuccess(Call<ResponseBody> call, Response<ResponseBody> response);
        void onReportDownloadFailed(Call<ResponseBody> call, Throwable t);
    }
}
