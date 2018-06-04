package com.forabetterlife.dtq.myunsplash.photo;

import android.app.DownloadManager;
import android.net.Uri;

import com.forabetterlife.dtq.myunsplash.data.model.PhotoResponse;
import com.forabetterlife.dtq.myunsplash.utils.PhotoDetailAction;

/**
 * Created by DTQ on 3/23/2018.
 */

public interface PhotoDetailContract {
    public interface Presenter {
        void loadImageInformation();
        boolean isPhotoNull();
        void downloadImage(DownloadManager downloadManager, boolean visibleDownloadUi);
        void removeDownloadReference();
        void handleDownloadResult(long downloadReference);
        void handleFavoriteClick();
        void setAction(PhotoDetailAction action);
        void dropView();
        void takeView(PhotoDetailContract.View view);
    }
    public interface View {
        void setPresenter(PhotoDetailContract.Presenter presenter);
        void showLoadDetaiError();
        void showInformationAboutPhoto(PhotoResponse photo, boolean isFavorite, String photoQuality);
        void sendBroadcast(long reference, DownloadManager downloadManager);
        void startActivityWallpaper(Uri uri);
        void startActivityWallpapeWhenExceptionOccured(Uri uri);
        void setDownloadFinish();
        void dismissWallPaperDialog();
        void showFavoriteStatus(boolean isFavorite);

    }
}
