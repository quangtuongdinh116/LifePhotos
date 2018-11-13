package com.forabetterlife.dtq.myunsplash.data.local.wallpaper;

import android.app.DownloadManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.util.Log;

import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.data.PhotoRepository;
import com.forabetterlife.dtq.myunsplash.data.local.LocalDataSource;
import com.forabetterlife.dtq.myunsplash.prod.Inject;

/**
 * Created by DTQ on 5/27/2018.
 */

public class WallpaperService extends JobService {
    private static final String TAG ="WallpaperService";

    private WallpaperHelper mWallpaperHelper;

    @Override
    public boolean onStartJob(JobParameters params) {

        if (mWallpaperHelper == null) {
            mWallpaperHelper = new WallpaperHelper(this, Inject.provideRepository(this));
            mWallpaperHelper.changeWallpaper(params);
            return true;
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        if (mWallpaperHelper != null) {
            mWallpaperHelper = null;
        }
        return true;
    }


}
