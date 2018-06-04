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
//        registerReceiver(receiver, filter);
        Log.i(TAG, "inside on StartJob");

        int jobId = params.getJobId();
        Log.i(TAG, "jobId is: " + jobId);
        String type = params.getExtras().getString(LocalDataSource.BUNDLE_KEY_TYPE_WALLPAPER, MyUnSplash.FAVORITE);
        Log.i(TAG, "type is: " + type);
        if (mWallpaperHelper == null) {
            mWallpaperHelper = new WallpaperHelper(this, Inject.provideRepository(this));
            mWallpaperHelper.changeWallpaper(params);
            return true;
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
//        unregisterReceiver(receiver);
        Log.i(TAG, "inside on StopJob");
        if (mWallpaperHelper != null) {
            mWallpaperHelper = null;
        }
        return false;
    }

//    IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//
//    BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//            mWallpaperHelper.handleDownloadResult(reference);
//        }
//    };
}
