package com.forabetterlife.dtq.myunsplash.wallpaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.data.PhotoDataSource;
import com.forabetterlife.dtq.myunsplash.data.PhotoRepository;
import com.forabetterlife.dtq.myunsplash.utils.WallpaperType;

import javax.annotation.Nullable;

/**
 * Created by DTQ on 5/27/2018.
 */

public class WallpaperPresenter implements WallpaperContract.Presenter {
    private static final String TAG = "WallpaperPresenter";

    @NonNull
    PhotoRepository mRepository;

    @Nullable
    WallpaperContract.View mView;

    @NonNull
    WallpaperType mType;

    @NonNull
    private long mDuration;


    public WallpaperPresenter(@NonNull PhotoRepository repository, WallpaperContract.View view, @NonNull WallpaperType type) {
        mRepository = repository;
        mView = view;
        mType = type;
        mView.setPresenter(this);
    }

    @Override
    public void changeAutoWallpaperStatus(long duration, String type, Context context) {
//        WallpaperType wallpaperType = WallpaperType.FAVORITE;
//        switch (type) {
//            case MyUnSplash.FAVORITE:
//                wallpaperType = WallpaperType.FAVORITE;
//                break;
//            case MyUnSplash.WANTED_PHOTO:
//                wallpaperType =WallpaperType.WANTED;
//                break;
//            default:
//                wallpaperType = WallpaperType.FAVORITE;
//                break;
//        }
        Log.i(TAG, "TYPE IS: " + type);
       mRepository.changeWallpaperStatus(duration, type, context, new PhotoDataSource.ScheduleChangeWallpaper() {
           @Override
           public void onScheduleSuccess() {
               if (mView != null) {
                   mView.showScheduleSuccess();
               }

           }

           @Override
           public void onScheduleFail() {
               if (mView != null) {
                   mView.showScheduleFail();
               }

           }

           @Override
           public void onStopSuccess() {
               if (mView != null) {
                   mView.showStopSuccess();
               }
           }

           @Override
           public void onStopFail() {
               if (mView != null) {
                   mView.showStopFail();
               }
           }
       });
    }

    @Override
    public void takeView(WallpaperContract.View view) {
        mView = view;
    }

    @Override
    public void dropView(WallpaperContract.View view) {
        mView = null;
    }

    @Override
    public void loadStatus() {
        mRepository.loadWallpaperStatus(new PhotoDataSource.LoadWallpaperStatus() {
            @Override
            public void onLoadSuccess(String type, boolean isOn, long duration) {
//                String wallpaperStyle = MyUnSplash.FAVORITE;
//                switch (type) {
//                    case FAVORITE:
//                        wallpaperStyle = MyUnSplash.FAVORITE;
//                        break;
//                    case WANTED:
//                        wallpaperStyle = MyUnSplash.WANTED_PHOTO;
//                        default:
//                            wallpaperStyle = MyUnSplash.FAVORITE;
//                            break;
//                }
                Log.i(TAG, "duration is: " + String.valueOf(duration));
                setDuration(duration);
                mView.showStatus(type, isOn, duration);

            }

            @Override
            public void onLoadFail() {

            }
        });

    }

    @Override
    public void setDuration(long duration) {
        mDuration = duration;
    }

    @NonNull
    public long getDuration() {
        return mDuration;
    }
}