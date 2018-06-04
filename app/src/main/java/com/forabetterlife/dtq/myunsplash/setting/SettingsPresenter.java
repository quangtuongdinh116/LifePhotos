package com.forabetterlife.dtq.myunsplash.setting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.forabetterlife.dtq.myunsplash.data.PhotoDataSource;
import com.forabetterlife.dtq.myunsplash.data.PhotoRepository;

/**
 * Created by DTQ on 3/29/2018.
 */

public class SettingsPresenter implements SettingsContract.Presenter {
    private static final String TAG = "SettingsPresenter";

    @NonNull
    private PhotoRepository mRepository;

    @NonNull SettingsContract.View mView;

    public SettingsPresenter(@NonNull PhotoRepository repository, @NonNull SettingsContract.View view) {
        mRepository = repository;
        mView = view;
    }


    @Override
    public void changeWantedPhotoServiceStatus(boolean turnOn, Context context) {
        mRepository.changeWantedPhotoServiceStatus(turnOn, new PhotoDataSource.ScheduleFetchNewWantedPhoto() {
            @Override
            public void onScheduleSuccess() {
                Log.i(TAG, "inside changeWantedPhotoServiceStatus onScheduleSuccess ");
                mView.showScheduleSuccessMessage();
            }

            @Override
            public void onScheduleFail() {
                Log.i(TAG, "inside changeWantedPhotoServiceStatus onScheduleFail ");
                mView.showScheduleFailMessage();
            }

            @Override
            public void onStopSuccess() {

            }

            @Override
            public void onStopFail() {

            }
        },context);
    }
}
