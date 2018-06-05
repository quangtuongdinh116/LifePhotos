package com.forabetterlife.dtq.myunsplash.photos;

import android.content.Context;
import android.net.ConnectivityManager;

import com.forabetterlife.dtq.myunsplash.data.model.PhotoResponse;
import com.forabetterlife.dtq.myunsplash.utils.PhotoCategory;

import java.util.List;

/**
 * Created by DTQ on 3/22/2018.
 */

public interface PhotosContract {
    public interface Presenter {
        void loadPhotos(ConnectivityManager connectivityManager);
        void searchPhotoByQuery(String query);
        void moveToDetailPage(PhotoResponse photoResponse);
        void setCategory(PhotoCategory category);
        PhotoCategory getCategory();
        void setSearchQuery(String query);
        String getSearchQueryWantedPhoto();
        void dropView();
        void takeView(PhotosContract.View view);
        void nextPageAllPhotos();
        void nextPageSearchPhotos();
        void setIsNewStatus();
        void setIsSearching(boolean isSearching);
        void resetToFirstPage();
        boolean isSearching();
        String getSearchQuery();
        void clearMemory(Context context);
    }
    public interface View {
        void setPresenter(PhotosContract.Presenter presenter);
        void showAllPhotos(List<PhotoResponse> list, String photoQuality, boolean isNew);
        void showLoadAllPhotosError();
        void moveToPhotoDetailPage(String photoJson);
        void showCategoryTitle();
        void showNoInternetError();
        void showTurnOnWantedFunction();
        void setLoadingIndicator(boolean active);
        boolean isActive();
    }
}
