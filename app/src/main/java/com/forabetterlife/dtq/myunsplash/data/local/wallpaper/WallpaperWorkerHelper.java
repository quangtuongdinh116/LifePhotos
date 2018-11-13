package com.forabetterlife.dtq.myunsplash.data.local.wallpaper;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;


import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import com.forabetterlife.dtq.myunsplash.data.model.FilterOptionsModel;
import com.forabetterlife.dtq.myunsplash.utils.GlideApp;
import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.data.PhotoDataSource;
import com.forabetterlife.dtq.myunsplash.data.PhotoRepository;
import com.forabetterlife.dtq.myunsplash.data.local.FavoriteEntity;
import com.forabetterlife.dtq.myunsplash.data.model.PhotoResponse;
import com.forabetterlife.dtq.myunsplash.data.model.SearchPhotoResponse;
import com.forabetterlife.dtq.myunsplash.utils.Utils;
import com.google.common.base.Strings;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

public class WallpaperWorkerHelper {

    private static final String TAG = "WallpaperWorkerHelper";

    private WallpaperWorker mWorker;

    @NonNull
    private PhotoRepository mRepository;

    DownloadManager mDownloadManager;

    private long downloadReference;

    private static final int REQUEST_CODE_NOTI = 1;

    private Context context;

    Target target;

    public WallpaperWorkerHelper(Context context, @NonNull PhotoRepository mRepository) {
        this.context = context;
        this.mRepository = mRepository;
    }

    public void changeWallpaper(String type) {

        if (type.equals(MyUnSplash.FAVORITE)) {
            changeFavoriteWallpapers();
        } else if (type.equals(MyUnSplash.WANTED_PHOTO)) {
            changeWantedWallpapers();
        } else if (type.equals(MyUnSplash.RANDOM_PHOTO)) {
            changeRandomWallpapers();
        }
    }

    private void changeRandomWallpapers() {


        int randomPage = Utils.generateRandomNumber(100);

        mRepository.loadAllPhotos(new PhotoDataSource.LoadAllPhotosCallback() {
            @Override
            public void onLoadSuccess(List<PhotoResponse> photoResponseList) {
                if (photoResponseList == null || photoResponseList.size() == 0) {
                    return;
                }

                List<PhotoResponse> finalList = getPhotosCanBeWallpaper(photoResponseList);
                int size = finalList.size();
                Log.i(TAG, "SIZE IS: " + String.valueOf(size));
                if (size == 0 ){
                    return;
                }
                int randomPosition = Utils.generateRandomNumber(size);
                String url = finalList.get(randomPosition).getUrls().getRegular();
                setWallpaper(url);
            }

            @Override
            public void onLoadFail() {

            }
        },randomPage, new FilterOptionsModel());
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

    private void changeFavoriteWallpapers() {

        mRepository.loadFavorites(new PhotoDataSource.LoadFavoritesCallback() {
            @Override
            public void onLoadSuccess(List<FavoriteEntity> favoriteList) {


                checkNotNull(favoriteList);
                int size = favoriteList.size();
                if (size == 0 ) {
                    Log.i(TAG, "INSIDE changeFavoriteWallpapers size == 0 ");
                    sendErrorNotification(context,
                            "Error happened when changed wallpaper",
                            "Your favorite contains no photo!");

                    return;
                }


                int randomPosition = Utils.generateRandomNumber(size);

                List<FavoriteEntity> finalList = getFavotiteCanBeWallpaper(favoriteList);
                if (favoriteList.size() == 0) {
                    return;
                }

                FavoriteEntity favoriteEntity = finalList.get(randomPosition);

                final String photoUrl = favoriteEntity.getRegularUrl();

                String photoId = favoriteEntity.getId();
                setWallpaper(photoUrl);
            }

            @Override
            public void onLoadFail() {

            }
        });
    }

    private void changeWantedWallpapers() {
        String query = mRepository.getSearchQueryWantedPhoto();
        if (Strings.isNullOrEmpty(query)) {
            sendErrorNotification(context,"Error happened when changed wallpaper",
                    "You have not set keyword for wanted photo yet!");
            return;
        }
        Random rand = new Random();
        int randomPage = rand.nextInt(10);

        mRepository.searchPhotoByQuery(query, randomPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchPhotoResponse -> {
                    List<PhotoResponse> photoResponseList = searchPhotoResponse.getResults();

                    if (photoResponseList == null || photoResponseList.size() == 0) {
                        sendErrorNotification(context,"Error happened when changed wallpaper",
                                "No photos found for your wanted photo search keyword!");
                        return;
                    } else {

                        List<PhotoResponse> listOfWallpapers = new ArrayList<>();
                        for (PhotoResponse photoResponse : photoResponseList) {
                            if (photoResponse.getHeight()/photoResponse.getWidth() > MyUnSplash.CAN_BE_WALLPAPER) {
                                listOfWallpapers.add(photoResponse);
                            }
                        }
                        if (listOfWallpapers.size() > 0) {
                            int size = listOfWallpapers.size();
                            Random random = new Random();
                            int randomPosition = random.nextInt(size);
                            PhotoResponse chosenPhotoResponse = listOfWallpapers.get(randomPosition);
                            String url = chosenPhotoResponse.getUrls().getRegular();
                            setWallpaper(url);
                        } else {
                            //do nothing
                        }
                    }
                }, throwable -> {

                });

    }


    private void setWallpaper(final String url) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        GlideApp.with(context)
                .asBitmap().load(url)
                .override(width,height)
                .centerCrop()
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Bitmap> target, boolean isFirstResource) {
                        // Log the GlideException here (locally or with a remote logging framework):


                        // You can also log the individual causes:
                        for (Throwable t : e.getRootCauses()) {

                        }
                        // Or, to log all root causes locally, you can use the built in helper method:
                        e.logRootCauses(TAG);

                        return false; // Allow calling onLoadFailed on the Target.
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, com.bumptech.glide.request.target.Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        Log.i(TAG, "Load success");
                        return false;
                    }
                })
                .into(new com.bumptech.glide.request.target.Target<Bitmap>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        Log.i(TAG, "INSIDE onLoadStarted");
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Log.i(TAG, "INSIDE onLoadFailed");
                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Log.i(TAG, "INSIDE onResourceReady");
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
                        try {
                            wallpaperManager.setBitmap(resource);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void getSize(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void setRequest(@Nullable Request request) {

                    }

                    @Nullable
                    @Override
                    public Request getRequest() {
                        return null;
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }
                });
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

    public void sendErrorNotification(Context context, String title, String content) {
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle("Error happen when changing wallpaper")
                .setContentText("Your favorite contains no photo!")
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(REQUEST_CODE_NOTI,notification);
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
