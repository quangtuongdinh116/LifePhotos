package com.forabetterlife.dtq.myunsplash.data.remote;

import com.forabetterlife.dtq.myunsplash.data.model.PhotoResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by DTQ on 3/22/2018.
 */

public interface PhotoApi {

    @GET("photos")
    Call<List<PhotoResponse>> getPhotos(@Query("page") int page,
                                        @Query("per_page") int per_page,
                                        @Query("client_id") String access_key,
                                        @Query("order_by") String order_by);

    @GET("photos/curated")
    Call<List<PhotoResponse>> getCuratedPhotos(@Query("page") int page,
                                       @Query("per_page") int per_page,
                                               @Query("client_id") String access_key,
                                       @Query("order_by") String order_by);


    @GET("photos/{id}/download")
    Call<ResponseBody> reportDownload(@Path("id") String id);

}
