package com.forabetterlife.dtq.myunsplash.data.local;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.data.PhotoDataSource;
import com.forabetterlife.dtq.myunsplash.data.local.wallpaper.WallpaperService;
import com.forabetterlife.dtq.myunsplash.data.local.wallpaper.WallpaperWorker;
import com.forabetterlife.dtq.myunsplash.data.remote.wantedphoto.WantedPhotoService;
import com.forabetterlife.dtq.myunsplash.data.service.PhotoService;
import com.forabetterlife.dtq.myunsplash.utils.AppExecutors;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.work.Constraints;
import androidx.work.Data;

import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.State;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

/**
 * Created by DTQ on 3/22/2018.
 */

@Singleton
public class LocalDataSource implements PhotoDataSource {
    private static final String TAG = "LocalDataSource";

    private static final String WANTED_PHOTO_LAST_SEARCH_KEY_PREF = "WANTED_PHOTO_LAST_SEARCH_KEY";

    public static final String BUNDLE_KEY_SEARCH_ID = "key_persistence_bundle_search_id";
    public static final String BUNDLE_KEY_SEARCH_QUERY = "key_persistence_bundle_search_query";

    public static final String BUNDLE_KEY_TYPE_WALLPAPER = "key_persistence_bundle_type_wallpaper";

    public static final String PREFENCE_KEY_DURATION = "preference_key_duration";
    public static final String PREFERENCE_KEY_WALLPAPER_TYPE = "preference_key_wallpaper_type";
    public static final String PREFERENCE_KEY_IS_ON_WALLPAPER = "preference_key_is_on_wallpaper";

    private static final int jobIdWantedPhoto = "LocalDataSource".hashCode();
    private static final int jobIdWallpaper = "LocalDataSources".hashCode();
    private static final long PERIODIC = TimeUnit.MINUTES.toMillis(1L);

    private static LocalDataSource INSTANCE;

    @NonNull
    private FavoriteDao mFavoriteDao;

    @NonNull
    private AppExecutors mAppExecutors;

    @NonNull
    private SharedPreferences mSharedPreferences;

    @NonNull
    private JobScheduler mJobScheduler;

    @NonNull
    private WorkManager mWorkManager;

    private boolean isFavorite = false;

    private LiveData<List<WorkStatus>> mScheduleStatus;

    @Inject
    public LocalDataSource(@NonNull FavoriteDao favoriteDao, @NonNull AppExecutors appExecutors, @NonNull SharedPreferences sharedPreferences) {
        mFavoriteDao = favoriteDao;
        mAppExecutors = appExecutors;
        mSharedPreferences = sharedPreferences;
        mWorkManager = WorkManager.getInstance();
        mScheduleStatus = mWorkManager.getStatusesByTag(MyUnSplash.TAG_OUTPUT);
    }

    public static LocalDataSource getInstance(@NonNull FavoriteDao favoriteDao,
                                              @NonNull AppExecutors appExecutors,
                                              @NonNull SharedPreferences sharedPreferences) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(favoriteDao,appExecutors,sharedPreferences);
        }
        return INSTANCE;
    }


    @Override
    public void loadAllPhotos(LoadAllPhotosCallback callback, int page) {

    }

    @Override
    public void searchPhotoByQuery(String query, SearchPhotoByQueryCallback callback, int page) {

    }

    @Override
    public void reportDownload(String id, PhotoService.OnReportDownloadListener callback) {

    }

    @Override
    public void isFavorite(final String id, final PhotoDataSource.CheckFavoriteStatusCallback callback) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final FavoriteEntity photo = mFavoriteDao.findPhoto(id);
                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(photo == null) {
                            callback.onCheckSuccess(false);
                        } else {
                            callback.onCheckSuccess(true);
                        }
                    }
                });
            }
        };
        mAppExecutors.getDiskIO().execute(runnable);
    }

    public boolean returnResult(boolean result) {
        return result;
    }

    @Override
    public void changeFavoriteStatus(final FavoriteEntity photo) {
        Log.i(TAG, "inside changeFavoriteStatus");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "inside run");
                FavoriteEntity image = mFavoriteDao.findPhoto(photo.getId());
                Log.i(TAG, "after run");
                if(image == null) {
                    Log.i(TAG, "inside photo = null, insert new photo");
                    mFavoriteDao.insertPhoto(photo);
                } else {
                    Log.i(TAG, "inside photo != null, remove photo");
                    mFavoriteDao.deletePhotoById(photo.getId());
                }
            }
        };
        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void loadFavorites(final LoadFavoritesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<FavoriteEntity> favoriteList = mFavoriteDao.loadFavorites();
                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (favoriteList == null) {
                            callback.onLoadFail();
                        } else {
                            callback.onLoadSuccess(favoriteList);
                        }
                    }
                });
            }
        };
        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void loadDownloadedPhotos(final LoadDownloadedPhotosCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //load list of downloaded photos
                final List<DownloadedPhotoEntity> downloadedList = new ArrayList<>();
                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (downloadedList == null) {
                            callback.onLoadFail();
                        } else {
                            callback.onLoadSuccess(downloadedList);
                        }
                    }
                });
            }
        };
        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public String getPhotoDownloadQuality() {
        return mSharedPreferences.getString("download_quality_list_key","RAW");
    }

    @Override
    public String getPhotoShowingQuality() {
        return mSharedPreferences.getString("show_quality_list_key","REGULAR");
    }

    @Override
    public String getSearchQueryWantedPhoto() {
        return mSharedPreferences.getString("photo_wanted_edit_text_preference_key","");
    }

    @Override
    public void changeWantedPhotoServiceStatus(boolean turnOn, ScheduleFetchNewWantedPhoto callback, Context context) {
        Log.i(TAG, "INSIDE changeWantedPhotoServiceStatus");

        String searchQuery = getSearchQueryWantedPhoto();
        if (Strings.isNullOrEmpty(searchQuery)) {
            return;
        }

        boolean trueOrFalseInReference = mSharedPreferences.getBoolean("photo_wanted_switch_preference_key",false);
        boolean isOnInPreference = !trueOrFalseInReference;

        boolean needTurnOff = true;
        boolean needTurnOn = false;
        if (isOnInPreference) {
            needTurnOn = true;
            needTurnOff = false;
        } else {
            needTurnOff = true;
            needTurnOn = false;
        }

        if (needTurnOn) {
            mJobScheduler.cancel(jobIdWantedPhoto);
            int result = mJobScheduler.schedule(getJobInfoWantedPhoto(context));
            if (result == JobScheduler.RESULT_SUCCESS) {
                callback.onScheduleSuccess();
            } else {
                callback.onScheduleFail();
            }
        } else if(needTurnOff) {
            mJobScheduler.cancel(jobIdWantedPhoto);
            callback.onStopSuccess();
        }



    }



    @Override
    public void saveLastSearchWantedPhotoId(String lastSearchId) {
        mSharedPreferences.edit().
                putString(WANTED_PHOTO_LAST_SEARCH_KEY_PREF, lastSearchId)
                .apply();
    }

//    @Override
//    public void changeWallpaperStatus(long duration, String type, Context context,PhotoDataSource.ScheduleChangeWallpaper callback) {
//        boolean isOnInPreference = mSharedPreferences.getBoolean(PREFERENCE_KEY_IS_ON_WALLPAPER, false);
//        Log.i(TAG, "isOnInPreference is " + String.valueOf(isOnInPreference));
//        Log.i(TAG, "TYPE IS : " + type);
//        boolean needTurnOffNow = true;
//        boolean needTurnOnNow = false;
//        if (isOnInPreference) {
//            needTurnOffNow = true;
//            needTurnOnNow = false;
//        } else if (!isOnInPreference) {
//            needTurnOffNow = false;
//            needTurnOnNow = true;
//        }
//        if (needTurnOffNow) {
//            Log.i(TAG, "inside needTurnOffNow");
//            //turn off
//            mJobScheduler.cancel(jobIdWallpaper);
//            mSharedPreferences.edit()
//                    .putBoolean(PREFERENCE_KEY_IS_ON_WALLPAPER, false).apply();
//            callback.onStopSuccess();
//        } else if (needTurnOnNow) {
//            Log.i(TAG, "inside needTurnOnNow");
//            //turn on
//            mJobScheduler.cancel(jobIdWallpaper);
////            mSharedPreferences.edit()
////                    .putBoolean(PREFERENCE_KEY_IS_ON_WALLPAPER, false).apply();
//            int result = mJobScheduler.schedule(getJobInfoWallpaper(context,type,duration));
//            if (result == JobScheduler.RESULT_SUCCESS) {
//                Log.i(TAG, "inside needTurnOnNow RESULT_SUCCESS ");
//                mSharedPreferences.edit()
//                        .putBoolean(PREFERENCE_KEY_IS_ON_WALLPAPER, true).apply();
//                callback.onScheduleSuccess();
//            } else {
//                Log.i(TAG, "inside needTurnOnNow RESULT_FAIL ");
//                mSharedPreferences.edit()
//                        .putBoolean(PREFERENCE_KEY_IS_ON_WALLPAPER, false).apply();
//                callback.onScheduleFail();
//            }
//        }
//        mSharedPreferences.edit()
//                .putString(PREFERENCE_KEY_WALLPAPER_TYPE, type).apply();
//        mSharedPreferences.edit()
//                .putLong(PREFENCE_KEY_DURATION, duration).apply();
//
////        //test after
////        boolean isOnInPreferenceAfter = mSharedPreferences.getBoolean(PREFERENCE_KEY_IS_ON_WALLPAPER, false);
////        Log.i(TAG, "isOnInPreferenceAfter: " + String.valueOf(isOnInPreferenceAfter));
//    }


    @Override
    public void changeWallpaperStatus(long duration, String type, Context context,PhotoDataSource.ScheduleChangeWallpaper callback) {
        boolean isOnInPreference = mSharedPreferences.getBoolean(PREFERENCE_KEY_IS_ON_WALLPAPER, false);
        Log.i(TAG, "isOnInPreference is " + String.valueOf(isOnInPreference));
        Log.i(TAG, "TYPE IS : " + type);
        boolean needTurnOffNow = true;
        boolean needTurnOnNow = false;
        if (isOnInPreference) {
            needTurnOffNow = true;
            needTurnOnNow = false;
        } else if (!isOnInPreference) {
            needTurnOffNow = false;
            needTurnOnNow = true;
        }
        if (needTurnOffNow) {
            Log.i(TAG, "inside needTurnOffNow");
            //turn off
            mWorkManager.cancelAllWorkByTag(MyUnSplash.CHANGE_WALLPAPER_WORK_NAME);
            mSharedPreferences.edit()
                    .putBoolean(PREFERENCE_KEY_IS_ON_WALLPAPER, false).apply();
            callback.onStopSuccess();
        } else if (needTurnOnNow) {
            Log.i(TAG, "inside needTurnOnNow");
            //turn on
            mWorkManager.cancelAllWorkByTag(MyUnSplash.TAG_OUTPUT);

//            mWorkManager.enqueue(MyUnSplash.CHANGE_WALLPAPER_WORK_NAME,
//                    ExistingPeriodicWorkPolicy.REPLACE,
//                    getRequest(context, type, duration));
            mWorkManager.enqueue(getRequest(context, type, duration));
            mSharedPreferences.edit()
                        .putBoolean(PREFERENCE_KEY_IS_ON_WALLPAPER, true).apply();
            callback.onScheduleSuccess();

//            if (mScheduleStatus.getValue() != null && !mScheduleStatus.getValue().isEmpty()) {
//                Log.i(TAG, "inside mScheduleStatus.getValue() != null && !mScheduleStatus.getValue().isEmpty()");
//                WorkStatus workStatus = mScheduleStatus.getValue().get(0);
//                if (workStatus.equals(State.ENQUEUED)) {
//                    Log.i(TAG, "inside workStatus.equals(State.ENQUEUED)");
//                    mSharedPreferences.edit()
//                        .putBoolean(PREFERENCE_KEY_IS_ON_WALLPAPER, true).apply();
//                callback.onScheduleSuccess();
//                } else {
//                    Log.i(TAG, "inside workStatus.equals(State.ENQUEUED) else");
//                    mSharedPreferences.edit()
//                        .putBoolean(PREFERENCE_KEY_IS_ON_WALLPAPER, false).apply();
//                callback.onScheduleFail();
//                }
//            }

//            if (workStatuses != null && workStatuses.getValue() != null && workStatuses.getValue().size() > 0) {
//                WorkStatus workStatus = mScheduleStatus.getValue().get(0);
//                switch (workStatus.getState()) {
//                    case ENQUEUED:
//                        Log.i(TAG, "ENQUEUE");
//                        break;
//                    case FAILED:
//                        Log.i(TAG, "FAILED");
//                        break;
//                    case BLOCKED:
//                        Log.i(TAG, "BLOCKED");
//                        break;
//                    case RUNNING:
//                        Log.i(TAG, "RUNNING");
//                        break;
//                    case SUCCEEDED:
//                        Log.i(TAG, "SUCCEEDED");
//                        break;
//                    case CANCELLED:
//                        Log.i(TAG, "CANCELLED");
//                        break;
//                }
//            } else {
//                Log.i(TAG, "SIZE IS 0");
//            }




//            int result = mJobScheduler.schedule(getJobInfoWallpaper(context,type,duration));
//            if (result == JobScheduler.RESULT_SUCCESS) {
//                Log.i(TAG, "inside needTurnOnNow RESULT_SUCCESS ");
//                mSharedPreferences.edit()
//                        .putBoolean(PREFERENCE_KEY_IS_ON_WALLPAPER, true).apply();
//                callback.onScheduleSuccess();
//            } else {
//                Log.i(TAG, "inside needTurnOnNow RESULT_FAIL ");
//                mSharedPreferences.edit()
//                        .putBoolean(PREFERENCE_KEY_IS_ON_WALLPAPER, false).apply();
//                callback.onScheduleFail();
//            }
        }
        mSharedPreferences.edit()
                .putString(PREFERENCE_KEY_WALLPAPER_TYPE, type).apply();
        mSharedPreferences.edit()
                .putLong(PREFENCE_KEY_DURATION, duration).apply();

//        //test after
//        boolean isOnInPreferenceAfter = mSharedPreferences.getBoolean(PREFERENCE_KEY_IS_ON_WALLPAPER, false);
//        Log.i(TAG, "isOnInPreferenceAfter: " + String.valueOf(isOnInPreferenceAfter));
    }

    @Override
    public void loadWallpaperStatus(LoadWallpaperStatus callback) {
        Boolean isOn = mSharedPreferences.getBoolean(PREFERENCE_KEY_IS_ON_WALLPAPER, false);
        Log.i(TAG, "isOn: " + String.valueOf(isOn));
        String type = mSharedPreferences.getString(PREFERENCE_KEY_WALLPAPER_TYPE, MyUnSplash.FAVORITE);
        Log.i(TAG, "type: " + String.valueOf(type));
        long duration = mSharedPreferences.getLong(PREFENCE_KEY_DURATION, 0);
        callback.onLoadSuccess(type, isOn, duration);
    }

    @Override
    public void clearMemory(final Context context) {
        Log.i(TAG, "inside changeFavoriteStatus");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Glide.get(context)
                        .clearDiskCache();
            }
        };
        mAppExecutors.getDiskIO().execute(runnable);

    }

    @Override
    public LiveData<List<WorkStatus>> getScheduleStatus() {
        return mScheduleStatus;
    }

    public String getWantedPhotoSearchQuery() {
        return mSharedPreferences.getString("photo_wanted_edit_text_preference_key","");
    }

    private JobInfo getJobInfoWantedPhoto(Context context) {
        PersistableBundle persistableBundle = new PersistableBundle();
        String lastSearchId = mSharedPreferences.getString(WANTED_PHOTO_LAST_SEARCH_KEY_PREF,"");
        Log.i(TAG, "LAST SEARCH ID: " + lastSearchId);
        String searchQuery = getWantedPhotoSearchQuery();
        Log.i(TAG, "search query: " + searchQuery);
        persistableBundle.putString(BUNDLE_KEY_SEARCH_ID,lastSearchId);

        persistableBundle.putString(BUNDLE_KEY_SEARCH_QUERY,searchQuery);
        JobInfo jobInfo = new JobInfo.Builder(jobIdWantedPhoto, new ComponentName(context,WantedPhotoService.class))
                .setPeriodic(PERIODIC)
                .setPersisted(true)
                .setExtras(persistableBundle)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();
        return jobInfo;
    }

    private JobInfo getJobInfoWallpaper(Context context, String type, long duration) {
        Log.i(TAG, "INSIDE getJobInfoWallpaper type is" + type);


        mSharedPreferences.edit()
                .putLong(PREFENCE_KEY_DURATION, duration);
        mSharedPreferences.edit()
                .putString(PREFERENCE_KEY_WALLPAPER_TYPE, type);

        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putString(BUNDLE_KEY_TYPE_WALLPAPER, type);

        JobInfo jobInfo = new JobInfo.Builder(jobIdWallpaper, new ComponentName(context, WallpaperService.class))
                .setPersisted(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setMinimumLatency(0)
                .setPeriodic(duration)
                .setExtras(persistableBundle)
                .build();

        return jobInfo;
    }

    private PeriodicWorkRequest getRequest(Context context, String type, long duration) {
        Log.i(TAG, String.format("inside getRequest with duration is %s", String.valueOf(duration)));

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresBatteryNotLow(false)
                .setRequiresStorageNotLow(false)
                .build();


        PeriodicWorkRequest.Builder builder =
                new PeriodicWorkRequest.Builder(WallpaperWorker.class, duration, TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                        .setInputData(createInputDataForType(type))
                .addTag(MyUnSplash.TAG_OUTPUT);

        PeriodicWorkRequest request = builder.build();
        return request;
    }

    private Data createInputDataForType(String type) {
        Data.Builder builder = new Data.Builder();
        if (!Strings.isNullOrEmpty(type)) {
            builder.putString(MyUnSplash.KEY_WALLPAPER_TYPE, type);
        }
        return builder.build();
    }


}
