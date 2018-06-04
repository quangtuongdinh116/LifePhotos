package com.forabetterlife.dtq.myunsplash.data.local.wallpaper;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.WallpaperManager;
import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.R;
import com.forabetterlife.dtq.myunsplash.data.PhotoDataSource;
import com.forabetterlife.dtq.myunsplash.data.PhotoRepository;
import com.forabetterlife.dtq.myunsplash.data.local.FavoriteEntity;
import com.forabetterlife.dtq.myunsplash.data.local.LocalDataSource;
import com.forabetterlife.dtq.myunsplash.data.model.PhotoResponse;
import com.forabetterlife.dtq.myunsplash.data.model.SearchPhotoResponse;
import com.forabetterlife.dtq.myunsplash.data.service.PhotoService;
import com.forabetterlife.dtq.myunsplash.utils.Utils;
import com.google.common.base.Strings;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by DTQ on 5/28/2018.
 */

public class WallpaperHelper {
    private static final String TAG = "WallpaperHelper";

    private WallpaperService mService;

    @NonNull
    private PhotoRepository mRepository;

    DownloadManager mDownloadManager;

    private long downloadReference;

    private static final int REQUEST_CODE_NOTI = 1;

    Target target;

    public WallpaperHelper(WallpaperService service, @NonNull PhotoRepository repository) {
        mService = service;
        mRepository = repository;
    }

    public void changeWallpaper(final JobParameters jobParameters) {
        Log.i(TAG, "INSIDE changeWallpaper");
        String type = jobParameters.getExtras().getString(LocalDataSource.BUNDLE_KEY_TYPE_WALLPAPER, MyUnSplash.FAVORITE);
        Log.i(TAG, "type is: " + type);
        if (type.equals(MyUnSplash.FAVORITE)) {
          changeFavoriteWallpapers(jobParameters);
        } else if (type.equals(MyUnSplash.WANTED_PHOTO)) {
            changeWantedWallpapers(jobParameters);
        } else if (type.equals(MyUnSplash.RANDOM_PHOTO)) {
            changeRandomWallpapers(jobParameters);
        }
    }

    private void changeRandomWallpapers(final JobParameters jobParameters) {

        int randomPage = Utils.generateRandomNumber(100);

        mRepository.loadAllPhotos(new PhotoDataSource.LoadAllPhotosCallback() {
            @Override
            public void onLoadSuccess(List<PhotoResponse> photoResponseList) {
                if (photoResponseList == null || photoResponseList.size() == 0) {
                    mService.jobFinished(jobParameters, true);
                    return;
                }

                List<PhotoResponse> finalList = getPhotosCanBeWallpaper(photoResponseList);
                int size = finalList.size();
                if (size == 0 ){
                    mService.jobFinished(jobParameters, true);
                    return;
                }
                int randomPosition = Utils.generateRandomNumber(size);
                String url = finalList.get(randomPosition).getUrls().getRegular();
                setWallpaper(url,jobParameters);
            }

            @Override
            public void onLoadFail() {

            }
        },randomPage);
    }

    private List<PhotoResponse> getPhotosCanBeWallpaper(List<PhotoResponse> sourceList) {
        List<PhotoResponse> finalList = new ArrayList<>();
        for (PhotoResponse photoResponse : sourceList) {
            if (photoResponse.getHeight()/photoResponse.getWidth() > MyUnSplash.CAN_BE_WALLPAPER) {
                finalList.add(photoResponse);
            }
        }
        return finalList;
    }

    private List<FavoriteEntity> getFavotiteCanBeWallpaper(List<FavoriteEntity> sourceList) {
        List<FavoriteEntity> finalList = new ArrayList<>();
        for (FavoriteEntity favoriteEntity : sourceList) {
            if (favoriteEntity.getHeight()/favoriteEntity.getWidth() > MyUnSplash.CAN_BE_WALLPAPER) {
                finalList.add(favoriteEntity);
            }
        }
        return finalList;
    }

    private void changeFavoriteWallpapers(final JobParameters jobParameters) {
        mRepository.loadFavorites(new PhotoDataSource.LoadFavoritesCallback() {
            @Override
            public void onLoadSuccess(List<FavoriteEntity> favoriteList) {
                Log.i(TAG, "INSIDE onLoadSuccess");
                checkNotNull(favoriteList);
                int size = favoriteList.size();
                if (size == 0 ) {
                    sendErrorNotification(mService,
                            "Error happened when changed wallpaper",
                            "Your favorite contains no photo!");
                    mService.jobFinished(jobParameters,true);
                    return;
                }
                Log.i(TAG, "size = " + size);

                int randomPosition = Utils.generateRandomNumber(size);
                Log.i(TAG, "randomPosition = " + String.valueOf(randomPosition));
                List<FavoriteEntity> finalList = getFavotiteCanBeWallpaper(favoriteList);
                if (favoriteList.size() == 0) {
                    mService.jobFinished(jobParameters, true);
                    return;
                }

                FavoriteEntity favoriteEntity = finalList.get(randomPosition);

                final String photoUrl = favoriteEntity.getRegularUrl();
                Log.i(TAG, "photoUrl = " + photoUrl);
                String photoId = favoriteEntity.getId();
                setWallpaper(photoUrl, jobParameters);
            }

            @Override
            public void onLoadFail() {

            }
        });

    }

    private void changeWantedWallpapers(final JobParameters jobParameters) {
        String query = mRepository.getSearchQueryWantedPhoto();
        if (Strings.isNullOrEmpty(query)) {
            sendErrorNotification(mService,"Error happened when changed wallpaper",
                    "You have not set keyword for wanted photo yet!");
            mService.jobFinished(jobParameters,true);
            return;
        }
        Random rand = new Random();
        int randomPage = rand.nextInt(10);
        mRepository.searchPhotoByQuery(query, new PhotoDataSource.SearchPhotoByQueryCallback() {
            @Override
            public void onLoadSuccess(SearchPhotoResponse searchPhotoResponse) {
                List<PhotoResponse> photoResponseList = searchPhotoResponse.getResults();

                if (photoResponseList == null || photoResponseList.size() == 0) {
                    Log.i(TAG, "inside photoResponseList == null || photoResponseList.size() == 0");
                    sendErrorNotification(mService,"Error happened when changed wallpaper",
                            "No photos found for your wanted photo search keyword!");
                    mService.jobFinished(jobParameters,true);
                    return;
                } else {
                    Log.i(TAG, "inside photoResponseList != null || photoResponseList.size() != 0");
                    Log.i(TAG, "SIZE IS: " + photoResponseList.size());
                    List<PhotoResponse> listOfWallpapers = new ArrayList<>();
                    for (PhotoResponse photoResponse : photoResponseList) {

                        if (photoResponse.getHeight()/photoResponse.getWidth() > 0.83) {
                            Log.i(TAG, "photoResponse.getHeight() : " + String.valueOf(photoResponse.getHeight()));
                            listOfWallpapers.add(photoResponse);
                        }
                    }

                    if (listOfWallpapers.size() > 0) {
                        int size = listOfWallpapers.size();
                        Random random = new Random();
                        int randomPosition = random.nextInt(size);
                        PhotoResponse chosenPhotoResponse = listOfWallpapers.get(randomPosition);
                        int chosenHeight = chosenPhotoResponse.getHeight();
                        int chosenWidth = chosenPhotoResponse.getWidth();
                        Log.i(TAG, "chosenHeight IS: " + String.valueOf(chosenHeight));
                        Log.i(TAG, "chosenWidth IS: " + String.valueOf(chosenWidth));

                        String url = chosenPhotoResponse.getUrls().getRegular();
                        setWallpaper(url,jobParameters);
                    } else {
                        Log.i(TAG, "ilistOfWallpapers.size() < 0");
                        mService.jobFinished(jobParameters, true);
                    }
                }
            }

            @Override
            public void onLoadFail() {
                Log.i(TAG, "INSIDE onLoadFail");
                mService.jobFinished(jobParameters, true);

            }
        },randomPage);

    }

    private void setWallpaper(final String url, final JobParameters jobParameters) {

        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.i(TAG, "inside  onBitmapLoaded");
                Log.i(TAG, "url is: " + url);
                Log.i(TAG, "BITMAP SIZE height: " + bitmap.getHeight() + "bitmap width: " + bitmap.getWidth());
                if (bitmap.getHeight() < 1000) {
                    mService.jobFinished(jobParameters, true);
                    return;
                }
                DisplayMetrics displayMetrics = new DisplayMetrics();
                    WindowManager windowManager = (WindowManager) mService.getSystemService(Context.WINDOW_SERVICE);
                    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
                    int height = displayMetrics.heightPixels;

                    int width = displayMetrics.widthPixels;
                Log.i(TAG, " height: " + String.valueOf(height));
                Log.i(TAG, " width: " + String.valueOf(width));
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(mService);
                try {
//                    DisplayMetrics displayMetrics = new DisplayMetrics();
//                    WindowManager windowManager = (WindowManager) mService.getSystemService(Context.WINDOW_SERVICE);
//                    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
//                    int height = displayMetrics.heightPixels;
//                    int width = displayMetrics.widthPixels;
//                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

//                    WallpaperManager wm = WallpaperManager.getInstance(mService);
//                    int w = bitmap.getWidth();
//                    int h = bitmap.getHeight();
//                    wallpaperManager.clear();
                    wallpaperManager.setBitmap(bitmap);
//                    wallpaperManager.suggestDesiredDimensions(w, h);
                    mService.jobFinished(jobParameters,true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.i(TAG, "INSIDE onBitmapFailed");
                mService.jobFinished(jobParameters, false);
            }


            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.i(TAG, "INSIDE onPrepareLoad");
            }
        };
        Picasso.Builder builder = new Picasso.Builder(mService);
//        Picasso.get().load(url)
//                .into(target);
        Picasso.with(mService.getApplicationContext())
                .setLoggingEnabled(true);
        Picasso.with(mService.getApplicationContext())
                .load(url)
                .into(target);

    }

    private Bitmap getScaledBitmap(Bitmap bitmap) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) mService.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int displayHeight  = displayMetrics.heightPixels;
        int displayWidth  = displayMetrics.widthPixels;

        final int originalWidth = bitmap.getWidth();
        final int originalHeight = bitmap.getHeight();
        // Obtain the horizontal and vertical scale factors
        final float horizontalScaleFactor = (float) originalWidth / (float) displayWidth;
        final float verticalScaleFactor = (float) originalHeight / (float) displayHeight;

        // Get the biggest scale factor to use in order to maintain original image's aspect ratio
        final float scaleFactor = Math.max(verticalScaleFactor, horizontalScaleFactor);
        final int finalWidth = (int) (originalWidth / scaleFactor);
        final int finalHeight = (int) (originalHeight / scaleFactor);

        // Create the final bitmap
        final Bitmap wallpaperBmp = Bitmap.createScaledBitmap(
                bitmap, finalWidth, finalHeight, true);

        // Recycle the original bitmap
//        bitmap.recycle();

        return wallpaperBmp;
    }

    private void downloadPhoto(String photoUrl, String photoId, String photoQuality) {

        mDownloadManager = (DownloadManager) mService.getSystemService(Context.DOWNLOAD_SERVICE);

        mRepository.reportDownload(photoId,mReportDownloadListener);
        String filename = photoId + "_" + "regular" + MyUnSplash.DOWNLOAD_PHOTO_FORMAT;

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(photoUrl))
                .setTitle(filename)
                .setDestinationInExternalPublicDir(MyUnSplash.DOWNLOAD_PATH, filename)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED );

        request.allowScanningByMediaScanner();

        downloadReference = mDownloadManager.enqueue(request);

    }





    PhotoService.OnReportDownloadListener mReportDownloadListener = new PhotoService.OnReportDownloadListener() {

        @Override
        public void onReportDownloadSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {

        }

        @Override
        public void onReportDownloadFailed(Call<ResponseBody> call, Throwable t) {

        }
    };

    public void sendErrorNotification(Context context, String title, String content) {
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle("Error happen when changing wallpaper")
                .setContentText("Your favorite contains no photo!")
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(REQUEST_CODE_NOTI,notification);
    }
}
