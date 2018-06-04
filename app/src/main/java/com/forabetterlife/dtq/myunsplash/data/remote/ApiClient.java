package com.forabetterlife.dtq.myunsplash.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DTQ on 3/22/2018.
 */

public class ApiClient {
    private static Retrofit sRetrofit = null;
    private static final String BASE_URL = "https://api.unsplash.com/";

    public static Retrofit getClient() {
        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sRetrofit;
    }
}
