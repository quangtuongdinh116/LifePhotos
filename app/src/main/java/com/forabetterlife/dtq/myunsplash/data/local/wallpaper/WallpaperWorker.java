package com.forabetterlife.dtq.myunsplash.data.local.wallpaper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.prod.Inject;
import com.forabetterlife.dtq.myunsplash.utils.Preferences;

import androidx.work.Worker;

public class WallpaperWorker extends Worker {
    private static final String TAG = "WallpaperWorker";

    private static final Object LOCK = new Object();

    private WallpaperWorkerHelper mWallpaperHelper;

    @NonNull
    @Override
    public WorkerResult doWork() {

        synchronized (LOCK) {
            if (Preferences.isChangeOngoing(MyUnSplash.getInstance())) {
                return WorkerResult.FAILURE;
            }
        }

        Preferences.changeOnChanging(MyUnSplash.getInstance(), true);

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
            return WorkerResult.RETRY;
        }
    }


}
