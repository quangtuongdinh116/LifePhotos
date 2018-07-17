package com.forabetterlife.dtq.myunsplash.setting;

import android.content.Context;

/**
 * Created by DTQ on 3/29/2018.
 */

public interface SettingsContract {
    public interface Presenter {
        void changeWantedPhotoServiceStatus(boolean turnOn, Context context);

    }
    public interface View {
        void setPresenter(SettingsContract.Presenter presenter);
        void showScheduleSuccessMessage();
        void showScheduleFailMessage();
        void showStopSuccessMessage();
        void showStopFailMessage();
        void showSnackBar(String message);
    }
}
