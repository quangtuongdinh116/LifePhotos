package com.forabetterlife.dtq.myunsplash.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.forabetterlife.dtq.myunsplash.data.local.FavoriteEntity;
import com.forabetterlife.dtq.myunsplash.data.model.PhotoResponse;

import java.util.Random;

/**
 * Created by DTQ on 3/23/2018.
 */

public class Utils {

    public static boolean isStoragePermissionGranted(Activity activity) {
        Log.i("Utils","Build.VERSION.SDK_INT is:" + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }

    public static String getPhotoUrlBaseOnQuality(String quality, PhotoResponse mPhotoResponse) {
        String urlOfPhoto = mPhotoResponse.getUrls().getRaw();
        switch (quality) {
            case "RAW":
                urlOfPhoto = mPhotoResponse.getUrls().getRaw();
                break;
            case "FULL":
                urlOfPhoto = mPhotoResponse.getUrls().getFull();
                break;
            case "REGULAR":
                urlOfPhoto = mPhotoResponse.getUrls().getRegular();
                break;
            case "SMALL":
                urlOfPhoto = mPhotoResponse.getUrls().getSmall();
                break;
            case "THUMB":
                urlOfPhoto = mPhotoResponse.getUrls().getThumb();
                break;
            default:
                urlOfPhoto = mPhotoResponse.getUrls().getRaw();
                break;
        }
        return urlOfPhoto;
    }


    public static String getPhotoUrlBaseOnQuality(String quality, FavoriteEntity favoriteEntity) {
        String urlOfPhoto = favoriteEntity.getRaWUrl();
        switch (quality) {
            case "RAW":
                urlOfPhoto = favoriteEntity.getRaWUrl();
                break;
            case "FULL":
                urlOfPhoto = favoriteEntity.getFullUrl();
                break;
            case "REGULAR":
                urlOfPhoto = favoriteEntity.getRegularUrl();
                break;
            case "SMALL":
                urlOfPhoto = favoriteEntity.getSmallUrl();
                break;
            case "THUMB":
                urlOfPhoto = favoriteEntity.getThumbUrl();
                break;
            default:
                urlOfPhoto = favoriteEntity.getRaWUrl();
                break;
        }
        return urlOfPhoto;
    }

    public static int generateRandomNumber(int number) {
        Random random = new Random();
        return random.nextInt(number);
    }
}
