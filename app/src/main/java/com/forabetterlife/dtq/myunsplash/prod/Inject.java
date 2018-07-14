package com.forabetterlife.dtq.myunsplash.prod;

import android.app.job.JobScheduler;
import android.content.Context;
import android.preference.PreferenceManager;

import com.forabetterlife.dtq.myunsplash.data.PhotoRepository;
import com.forabetterlife.dtq.myunsplash.data.remote.wantedphoto.SearchWantedPhotoService;
import com.forabetterlife.dtq.myunsplash.data.service.PhotoService;
import com.forabetterlife.dtq.myunsplash.data.service.SearchService;
import com.forabetterlife.dtq.myunsplash.data.local.FavoriteDao;
import com.forabetterlife.dtq.myunsplash.data.local.LocalDataSource;
import com.forabetterlife.dtq.myunsplash.data.local.MyUnsplashDatabase;
import com.forabetterlife.dtq.myunsplash.data.remote.RemoteDataSource;
import com.forabetterlife.dtq.myunsplash.utils.AppExecutors;

/**
 * Created by DTQ on 3/22/2018.
 */

public class Inject {

    public static PhotoRepository provideRepository(Context context) {
        MyUnsplashDatabase database = MyUnsplashDatabase.getInstance(context);
        FavoriteDao dao = database.favoriteDao();
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        return PhotoRepository.getInstance(LocalDataSource.getInstance(dao,new AppExecutors(),
                PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()),jobScheduler),RemoteDataSource.getInstance(PhotoService.getService(),SearchService.getService()));
    }

    public static SearchWantedPhotoService provideWantedPhotoService() {
        return SearchWantedPhotoService.getInstance(SearchService.getService());
    }
}
