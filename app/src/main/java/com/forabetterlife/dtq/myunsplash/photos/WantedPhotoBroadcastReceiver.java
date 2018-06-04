package com.forabetterlife.dtq.myunsplash.photos;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.forabetterlife.dtq.myunsplash.data.remote.wantedphoto.WantedPhotoRemote;

//import com.forabetterlife.dtq.myunsplash.data.local.WantedPhotoSyncTask;

/**
 * Created by DTQ on 3/31/2018.
 */

public class WantedPhotoBroadcastReceiver extends BroadcastReceiver {
   private static final String TAG = "receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "receive new intent with action: " + intent.getAction());
        if (intent != null) {
            if(intent.getIntExtra(WantedPhotoRemote.BROADCAST_INTENT_EXTRA_REQUEST_CODE,0) == 0) {
                if (getResultCode() == Activity.RESULT_OK) {
                    Log.i(TAG, "inside result ok");
                    Notification notification = (Notification) intent.getParcelableExtra(WantedPhotoRemote.BROADCAST_INTENT_EXTRA_NOTI);
                    if (notification == null) {
                        Log.i(TAG, "notification is null");
                    } else {
                        Log.i(TAG, "notification is not null");
                    }
                    int requestCode = intent.getIntExtra(WantedPhotoRemote.BROADCAST_INTENT_EXTRA_REQUEST_CODE,0);
                    Log.i(TAG, "to string is" + notification.toString());





                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                    notificationManagerCompat.notify(requestCode,notification);
                } else {
                    Log.i(TAG, "inside result not ok");
                }

            }
        }
    }
}
