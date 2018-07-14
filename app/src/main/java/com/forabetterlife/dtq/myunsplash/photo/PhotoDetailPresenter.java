package com.forabetterlife.dtq.myunsplash.photo;

import android.app.DownloadManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.data.PhotoDataSource;
import com.forabetterlife.dtq.myunsplash.data.PhotoRepository;
import com.forabetterlife.dtq.myunsplash.data.service.PhotoService;
import com.forabetterlife.dtq.myunsplash.data.local.FavoriteEntity;
import com.forabetterlife.dtq.myunsplash.data.model.PhotoResponse;
import com.forabetterlife.dtq.myunsplash.utils.PhotoDetailAction;
import com.forabetterlife.dtq.myunsplash.utils.Utils;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by DTQ on 3/23/2018.
 */

public class PhotoDetailPresenter implements PhotoDetailContract.Presenter {

    private static final String TAG = "PhotoDetailPresenter";

    @NonNull
    PhotoDetailContract.View mView;

    @NonNull
    PhotoRepository mRepository;

    @NonNull
    String photoJsonString;

    @NonNull
    PhotoResponse mPhotoResponse;

    boolean mIsFavorite = false;

    private PhotoDetailAction mAction = PhotoDetailAction.DOWNLOAD_ONLY;

    private DownloadManager downloadManager;

    private long downloadReference;

    public long getDownloadReference() {
        return downloadReference;
    }

    public void setDownloadReference(long downloadReference) {
        this.downloadReference = downloadReference;
    }

//    public PhotoDetailPresenter(@NonNull PhotoDetailContract.View view, @NonNull PhotoRepository repository, @NonNull String photoJsonString) {
//        mView = view;
//        mRepository = repository;
//        this.photoJsonString = photoJsonString;
//        mPhotoResponse = new Gson().fromJson(photoJsonString,PhotoResponse.class);
//
//        mView.setPresenter(this);
//    }

    @Inject
    PhotoDetailPresenter(@Nullable String photoJsonString,
                        PhotoRepository photoRepository ) {
        mRepository = photoRepository;
        this.photoJsonString = photoJsonString;
        mPhotoResponse = new Gson().fromJson(photoJsonString,PhotoResponse.class);
    }

    @Override
    public void loadImageInformation() {
        if(Strings.isNullOrEmpty(photoJsonString)) {
            mView.showLoadDetaiError();
            return;
        }
        final PhotoResponse photoResponse = new Gson().fromJson(photoJsonString, PhotoResponse.class);
        final String imageUrl = photoResponse.getUrls().getRaw();
        mRepository.isFavorite(photoResponse.getId(), new PhotoDataSource.CheckFavoriteStatusCallback() {
                    @Override
                    public void onCheckSuccess(boolean isFavorite) {
                        mIsFavorite = isFavorite;
                        Log.i(TAG, "favorite is: " + mIsFavorite);
                        String photoQuality = mRepository.getPhotoShowingQuality();
                        mView.showInformationAboutPhoto(photoResponse, mIsFavorite, photoQuality);
                    }
        });

    }

    @Override
    public boolean isPhotoNull() {
        return Strings.isNullOrEmpty(photoJsonString);
    }

    @Override
    public void downloadImage(DownloadManager downloadManager, boolean visibleDownloadUi) {
        checkNotNull(downloadManager);
        this.downloadManager = downloadManager;

        mRepository.reportDownload(mPhotoResponse.getId(),mReportDownloadListener);
        String filename = mPhotoResponse.getId() + "_" + "raw" + MyUnSplash.DOWNLOAD_PHOTO_FORMAT;
        String qualityOfPhoto = mRepository.getPhotoDownloadQuality();
        Log.i(TAG,"Quality of photo is: " + qualityOfPhoto);
        String urlOfPhoto = Utils.getPhotoUrlBaseOnQuality(qualityOfPhoto, mPhotoResponse);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlOfPhoto))
                .setTitle(filename)
                .setDestinationInExternalPublicDir(MyUnSplash.DOWNLOAD_PATH, filename)
                .setVisibleInDownloadsUi(visibleDownloadUi)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED );

        request.allowScanningByMediaScanner();

        downloadReference = downloadManager.enqueue(request);
    }



    @Override
    public void removeDownloadReference() {
        downloadManager.remove(downloadReference);
    }

    @Override
    public void handleDownloadResult(long downloadReference) {
        switch (getAction()) {
            case DOWNLOAD_THEN_SET_WALLPAPER:
                if (this.downloadReference == downloadReference) {
                    DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadReference);
                    Cursor cursor = downloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        int status  = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        switch (status) {
                            case DownloadManager.STATUS_SUCCESSFUL:
//                        getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, downloadManager.getUriForDownloadedFile(downloadReference)));
                                mView.sendBroadcast(downloadReference,downloadManager);
                                Uri uri = downloadManager.getUriForDownloadedFile(downloadReference);
                                try {
//                            Log.d(TAG, "Crop and Set: " + uri.toString());
//                            Intent wallpaperIntent = WallpaperManager.getInstance(context).getCropAndSetWallpaperIntent(uri);
//                            wallpaperIntent.setDataAndType(uri, "image/jpg");
//                            wallpaperIntent.putExtra("mimeType", "image/jpg");
//                            startActivityForResult(wallpaperIntent, 13451);
                                    mView.startActivityWallpaper(uri);

                                } catch (Exception e) {
//                            Log.d(TAG, "Chooser: " + uri.toString());
//                            Intent wallpaperIntent = new Intent(Intent.ACTION_ATTACH_DATA);
//                            wallpaperIntent.setDataAndType(uri, "image/jpg");
//                            wallpaperIntent.putExtra("mimeType", "image/jpg");
//                            wallpaperIntent.addCategory(Intent.CATEGORY_DEFAULT);
//                            wallpaperIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            wallpaperIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                            wallpaperIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                            startActivity(Intent.createChooser(wallpaperIntent, getString(R.string.set_as_wallpaper)));
                                    mView.startActivityWallpapeWhenExceptionOccured(uri);

                                }
//                        wallpaperDialog.setDownloadFinished(true);
                                mView.setDownloadFinish();

                                break;
                            default:
                                break;
                        }
                    }

//                wallpaperDialog.dismiss();
                    mView.dismissWallPaperDialog();

                    cursor.close();
                }
                break;
            case DOWNLOAD_ONLY:

                break;
        }
    }

    @Override
    public void handleFavoriteClick() {
        FavoriteEntity favoriteEntity = new FavoriteEntity();
        favoriteEntity.setId(mPhotoResponse.getId());
        favoriteEntity.setFullUrl(mPhotoResponse.getUrls().getFull());
        favoriteEntity.setRaWUrl(mPhotoResponse.getUrls().getRaw());
        favoriteEntity.setRegularUrl(mPhotoResponse.getUrls().getRegular());
        favoriteEntity.setSmallUrl(mPhotoResponse.getUrls().getSmall());
        favoriteEntity.setThumbUrl(mPhotoResponse.getUrls().getThumb());
        favoriteEntity.setArtistName(mPhotoResponse.getUser().getName());

        favoriteEntity.setHeight(mPhotoResponse.getHeight());
        favoriteEntity.setWidth(mPhotoResponse.getWidth());

        mRepository.changeFavoriteStatus(favoriteEntity);
        mIsFavorite = !mIsFavorite;
        mView.showFavoriteStatus(mIsFavorite);
    }


    PhotoService.OnReportDownloadListener mReportDownloadListener = new PhotoService.OnReportDownloadListener() {

        @Override
        public void onReportDownloadSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {

        }

        @Override
        public void onReportDownloadFailed(Call<ResponseBody> call, Throwable t) {

        }
    };

    public PhotoDetailAction getAction() {
        return mAction;
    }

    public void setAction(PhotoDetailAction action) {
        mAction = action;
    }

    @Override
    public void dropView() {
        mView = null;
    }

    @Override
    public void takeView(PhotoDetailContract.View view) {
        mView = view;
    }

    @NonNull
    public PhotoResponse getmPhotoResponse() {
        return mPhotoResponse;
    }
}
