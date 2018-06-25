package com.forabetterlife.dtq.myunsplash.data.local.wallpaper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.prod.Inject;

import androidx.work.Worker;

public class WallpaperWorker extends Worker {
    private static final String TAG = "WallpaperWorker";

    private WallpaperWorkerHelper mWallpaperHelper;

    @NonNull
    @Override
    public WorkerResult doWork() {
        String type = getInputData().getString(MyUnSplash.KEY_WALLPAPER_TYPE, null);
        Log.i(TAG, String.format("INSIDE doWork with type is %s",type));

        try {
            if (mWallpaperHelper == null) {
                mWallpaperHelper = new WallpaperWorkerHelper(getApplicationContext(), Inject.provideRepository(getApplicationContext()));
            }
            mWallpaperHelper.changeWallpaper(type);
            return WorkerResult.SUCCESS;

        } catch (Throwable throwable) {
            Log.e(TAG, "Error dowWork inside WallpaperWorker", throwable);
            return WorkerResult.FAILURE;
        }
    }


}
