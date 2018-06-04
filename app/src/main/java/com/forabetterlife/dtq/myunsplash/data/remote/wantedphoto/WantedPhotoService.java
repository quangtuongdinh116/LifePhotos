package com.forabetterlife.dtq.myunsplash.data.remote.wantedphoto;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.forabetterlife.dtq.myunsplash.data.local.LocalDataSource;
import com.forabetterlife.dtq.myunsplash.prod.Inject;

/**
 * Created by DTQ on 3/30/2018.
 */

public class WantedPhotoService extends JobService {
    private static final String TAG = "WantedPhotoService";

//    private WantedPhotoSyncTask mAsyncTask = null;

    private WantedPhotoRemote mWantedPhotoRemote;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "on start job: " + params.getJobId());
        Log.i(TAG, "query " + params.getExtras().getString(LocalDataSource.BUNDLE_KEY_SEARCH_QUERY));
        Log.i(TAG, "getId " + params.getExtras().getString(LocalDataSource.BUNDLE_KEY_SEARCH_ID));
//        if (mAsyncTask == null) {
//            mAsyncTask = new WantedPhotoSyncTask(this, Inject.provideWantedPhotoService());
//            mAsyncTask.execute(params);
//            return true;
//        }
        if (mWantedPhotoRemote == null) {
            mWantedPhotoRemote = new WantedPhotoRemote(this,Inject.provideWantedPhotoService() );
           mWantedPhotoRemote.searchPhotoAndNotify(params);
            return true;
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if(mWantedPhotoRemote != null) {

            mWantedPhotoRemote = null;
        }
        return true;
    }
}
