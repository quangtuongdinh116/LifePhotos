package com.forabetterlife.dtq.myunsplash.photos;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import com.forabetterlife.dtq.myunsplash.data.remote.wantedphoto.WantedPhotoRemote;

import dagger.android.support.DaggerFragment;

//import com.forabetterlife.dtq.myunsplash.data.local.WantedPhotoSyncTask;

/**
 * Created by DTQ on 3/31/2018.
 */

public class PhotosVisibleFragment extends DaggerFragment {
    IntentFilter intentFilter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentFilter = new IntentFilter(WantedPhotoRemote.BROADCAST_ACTION);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mBroadcastReceiver,intentFilter,WantedPhotoRemote.PERMISSION,null);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if(intent.getIntExtra(WantedPhotoRemote.BROADCAST_INTENT_EXTRA_REQUEST_CODE,0) == 0) {
                    Snackbar.make(getView(),"You have new wanted photo!",Snackbar.LENGTH_LONG).show();
                    setResultCode(Activity.RESULT_CANCELED);
                }
            }
        }
    };
}
