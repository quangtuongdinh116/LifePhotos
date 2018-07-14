package com.forabetterlife.dtq.myunsplash.data;

import android.app.Application;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.forabetterlife.dtq.myunsplash.data.local.FavoriteDao;
import com.forabetterlife.dtq.myunsplash.data.local.LocalDataSource;
import com.forabetterlife.dtq.myunsplash.data.local.MyUnsplashDatabase;
import com.forabetterlife.dtq.myunsplash.data.remote.RemoteDataSource;
import com.forabetterlife.dtq.myunsplash.data.service.PhotoService;
import com.forabetterlife.dtq.myunsplash.data.service.SearchService;
import com.forabetterlife.dtq.myunsplash.utils.AppExecutors;
import com.forabetterlife.dtq.myunsplash.utils.DiskIO;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DTQ on 4/29/2018.
 */

@Module
public class PhotoRepositoryModule {

    private static final int THREAD_COUNT = 3;

    @Singleton
    @Provides
    @Local
    PhotoDataSource providePhotosLocalDataSource(FavoriteDao favoriteDao, AppExecutors appExecutors, SharedPreferences sharedPreferences, JobScheduler jobScheduler) {
        return new LocalDataSource(favoriteDao, appExecutors, sharedPreferences, jobScheduler);
    }


    @Singleton
    @Provides
    @Remote
    PhotoDataSource providePhotosRemoteDataSource(PhotoService photoService, SearchService searchService) {
        return new RemoteDataSource(photoService, searchService);
    }

    @Singleton
    @Provides
    PhotoService providePhotoService() {
        return PhotoService.getService();
    }

    @Singleton
    @Provides
    SearchService provideSearchService() {
        return SearchService.getService();
    }

    @Singleton
    @Provides
    JobScheduler provideJobScheduler(Application context) {
        return (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences(Application context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }


    @Singleton
    @Provides
    AppExecutors provideAppExecutors() {
        return new AppExecutors(new DiskIO(),
                Executors.newFixedThreadPool(THREAD_COUNT),
                new AppExecutors.MainThreadExecutor());
    }

    @Singleton
    @Provides
    FavoriteDao provideTasksDao(MyUnsplashDatabase db) {
        return db.favoriteDao();
    }

    @Singleton
    @Provides
    MyUnsplashDatabase provideDb(Application context) {
        return  MyUnsplashDatabase.getInstance(context);
    }
}
