package com.forabetterlife.dtq.myunsplash.data.remote.wantedphoto;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.forabetterlife.dtq.myunsplash.R;
import com.forabetterlife.dtq.myunsplash.data.PhotoDataSource;
import com.forabetterlife.dtq.myunsplash.data.local.LocalDataSource;
import com.forabetterlife.dtq.myunsplash.data.model.PhotoResponse;
import com.forabetterlife.dtq.myunsplash.data.model.SearchPhotoResponse;
import com.forabetterlife.dtq.myunsplash.photos.PhotosActivity;

import java.util.List;

/**
 * Created by DTQ on 3/31/2018.
 */

public class  WantedPhotoRemote {
    private static final String TAG = "WantedPhotoRemote";

    private WantedPhotoService mJobService;

    private SearchWantedPhotoService mSearchService;


    public static String BROADCAST_ACTION = "com.forabetterlife.dtq.myunsplash.photos.BROADCAST_ACTION";

    public static String PERMISSION = "com.forabetterlife.dtq.myunsplash.photos.PRIVATE_PERMISSON";

    public static String BROADCAST_INTENT_EXTRA_NOTI = "com.forabetterlife.dtq.myunsplash.photos.EXTRA_NOTI";

    public static String BROADCAST_INTENT_EXTRA_REQUEST_CODE = "com.forabetterlife.dtq.myunsplash.photos.EXTRA_REQUEST_CODE";

    private String mNewPhotoId;

    private String mOldPhotoId;

    private boolean mHasNewPhoto;

    public WantedPhotoRemote(WantedPhotoService jobService, SearchWantedPhotoService searchService) {
        mJobService = jobService;
        mSearchService = searchService;
    }

    public  void searchPhotoAndNotify(JobParameters jobParameters) {

        mOldPhotoId = jobParameters.getExtras().getString(LocalDataSource.BUNDLE_KEY_SEARCH_ID);
        Log.i(TAG, "mOldPhotoId: " + mOldPhotoId);
        String searchQuery = jobParameters.getExtras().getString(LocalDataSource.BUNDLE_KEY_SEARCH_QUERY);
        Log.i(TAG, "searchQuery: " + searchQuery);
        final Result<JobParameters> result = new Result<>();
        mSearchService.searchPhotoByQuery(searchQuery, new PhotoDataSource.SearchPhotoByQueryCallback() {
            @Override
            public void onLoadSuccess(SearchPhotoResponse searchPhotoResponse) {
                Log.i(TAG, "inside onLoadSuccess: ");
                List<PhotoResponse> photoResponseList = searchPhotoResponse.getResults();
                if (photoResponseList != null) {
                    mNewPhotoId = photoResponseList.get(0).getId();
                    Log.i(TAG, "mNewPhotoId: " + mNewPhotoId);
                    if(mNewPhotoId.equals(mOldPhotoId)) {
//                        mHasNewPhoto = false;
                    } else {
//                        mHasNewPhoto = true;
                        Log.i(TAG, "inside new photo");
                        Intent intent = new Intent(mJobService, PhotosActivity.class);
                        intent.putExtra(PhotosActivity.CATEGORY_INTENT_KEY, PhotosActivity.CATEGORY_INTENT_VALUE);

                        PendingIntent pendingIntent = PendingIntent.getActivity(mJobService,0,intent,0);

                        Notification notification = new NotificationCompat.Builder(mJobService)
                                .setContentTitle("You have new photo for Wanted Photo!")
                                .setContentText("Click this notification to open")
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .setSmallIcon(R.drawable.ic_favorite_black_24dp)
                                .build();

                        Intent broadcastIntent = new Intent(BROADCAST_ACTION);
                        broadcastIntent.putExtra(BROADCAST_INTENT_EXTRA_NOTI,notification);
                        broadcastIntent.putExtra(BROADCAST_INTENT_EXTRA_REQUEST_CODE,0);


                        mJobService.sendOrderedBroadcast(broadcastIntent,PERMISSION,null,null, Activity.RESULT_OK,null,null);
                    }

//                    result.result = jobParameters[0];
//                    result.hasNewPhoto = mHasNewPhoto;

                }

            }

            @Override
            public void onLoadFail() {
                Log.i(TAG, "inside onLoadFail: ");


            }
        });
        mJobService.jobFinished(jobParameters,false);

    }
}
