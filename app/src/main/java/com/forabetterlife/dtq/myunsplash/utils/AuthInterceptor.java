package com.forabetterlife.dtq.myunsplash.utils;

import com.forabetterlife.dtq.myunsplash.MyUnSplash;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by DTQ on 3/22/2018.
 */

public class AuthInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request;

            request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Client-ID" + MyUnSplash.getAppId(MyUnSplash.getInstance()))
                    .build();

        return chain.proceed(request);
    }
}
