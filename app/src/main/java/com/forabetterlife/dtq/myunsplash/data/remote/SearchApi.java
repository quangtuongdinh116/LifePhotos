package com.forabetterlife.dtq.myunsplash.data.remote;

import com.forabetterlife.dtq.myunsplash.data.model.SearchPhotoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DTQ on 3/23/2018.
 */

public interface SearchApi {

    @GET("search/photos")
    Call<SearchPhotoResponse> searchPhotoByQuery(@Query("page") int page,
                                                 @Query("per_page") int perpage,
                                                 @Query("query") String query,
                                                 @Query("client_id") String access_token);
}
