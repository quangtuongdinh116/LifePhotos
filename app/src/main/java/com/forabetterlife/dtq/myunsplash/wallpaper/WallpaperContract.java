package com.forabetterlife.dtq.myunsplash.wallpaper;

import android.content.Context;

import com.forabetterlife.dtq.myunsplash.data.PhotoDataSource;
import com.forabetterlife.dtq.myunsplash.photos.PhotosContract;

/**
 * Created by DTQ on 5/27/2018.
 */

public interface WallpaperContract {

    public interface Presenter {
        public void changeAutoWallpaperStatus(long duration, String type, Context context);
        void takeView(WallpaperContract.View view);
        void dropView(WallpaperContract.View view);
        void loadStatus();
        void setDuration(long duration);
        long getDuration();

    }

    public interface View {
        void setPresenter(WallpaperContract.Presenter presenter);
        void showStatus(String type, boolean isOn, long duration);
        void showScheduleSuccess();
        void showScheduleFail();
        void showStopSuccess();
        void showStopFail();

    }
}
