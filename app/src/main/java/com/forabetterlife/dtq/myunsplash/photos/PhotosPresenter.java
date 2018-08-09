package com.forabetterlife.dtq.myunsplash.photos;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.forabetterlife.dtq.myunsplash.data.PhotoDataSource;
import com.forabetterlife.dtq.myunsplash.data.PhotoRepository;
import com.forabetterlife.dtq.myunsplash.data.local.DownloadedPhotoEntity;
import com.forabetterlife.dtq.myunsplash.data.local.FavoriteEntity;
import com.forabetterlife.dtq.myunsplash.data.model.FilterOptionsModel;
import com.forabetterlife.dtq.myunsplash.data.model.PhotoResponse;
import com.forabetterlife.dtq.myunsplash.data.model.SearchPhotoResponse;
import com.forabetterlife.dtq.myunsplash.data.model.Urls;
import com.forabetterlife.dtq.myunsplash.data.model.User;
import com.forabetterlife.dtq.myunsplash.di.ActivityScoped;
import com.forabetterlife.dtq.myunsplash.utils.PhotoCategory;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by DTQ on 3/22/2018.
 */


@ActivityScoped
public class PhotosPresenter implements PhotosContract.Presenter {
    private static final String TAG = "PhotosPresenter";

    private int mCurrentPage = 1;

//    private int mCurrentPageSearch = 1;

    @NonNull
    private PhotosContract.View mView;

    @NonNull
    private PhotoRepository mRepository;

    @NonNull
    private PhotoCategory PHOTO_CATEGORY;

    @Nullable
    private String mSearchQuery;

    private ConnectivityManager mConnectivityManager;

    boolean isFirstPage = true;

    boolean isSearchNew = true;

    boolean isSearching = false;

    @com.evernote.android.state.State FilterOptionsModel filterOptions = new FilterOptionsModel();

    @Inject
    PhotosPresenter(PhotoRepository photoRepository) {
        mRepository = photoRepository;
    }



    @Override
    public void loadPhotos(ConnectivityManager connectivityManager) {
        mConnectivityManager = connectivityManager;
        if (!isNetworkAvailableAndConnected(connectivityManager)) {
            mView.showNoInternetError();
            return;
        }

        if (isFirstPage) {

            mCurrentPage = 1;
        } else {
            mCurrentPage++;
        }
        if (mView != null && isFirstPage) {

            mView.setLoadingIndicator(true);
        }



        //load photos based on category begins
        if (PHOTO_CATEGORY == PhotoCategory.SHOW_ALL) {

            mRepository.loadAllPhotos(new PhotoDataSource.LoadAllPhotosCallback() {
                @Override
                public void onLoadSuccess(List<PhotoResponse> photoResponseList) {
                    if (mView == null || !mView.isActive()) {
                        return;
                    }
                    mView.showAllPhotos(photoResponseList, mRepository.getPhotoShowingQuality(), isFirstPage);
                    isFirstPage = false;
                }

                @Override
                public void onLoadFail() {
                    if (mView == null || !mView.isActive()) {
                        return;
                    }
                    mView.showLoadAllPhotosError();
                }
            }, mCurrentPage, filterOptions);
        } else if (PHOTO_CATEGORY == PhotoCategory.SHOW_FAVORITE) {
            if (mView != null) {
                mView.setLoadingIndicator(true);
            }

            mRepository.loadFavorites(new PhotoDataSource.LoadFavoritesCallback() {
                @Override
                public void onLoadSuccess(List<FavoriteEntity> favoriteList) {

                    List<PhotoResponse> photoResponseList = new ArrayList<>();
                    for (FavoriteEntity favoriteEntity : favoriteList){
                        PhotoResponse photoResponse = new PhotoResponse();
                        photoResponse.setId(favoriteEntity.getId());
                        Urls urls = new Urls();
                        urls.setRaw(favoriteEntity.getRaWUrl());
                        urls.setRegular(favoriteEntity.getRegularUrl());
                        urls.setFull(favoriteEntity.getFullUrl());
                        urls.setSmall(favoriteEntity.getSmallUrl());
                        urls.setThumb(favoriteEntity.getThumbUrl());
                        photoResponse.setUrls(urls);
                        User user = new User();
                        user.setName(favoriteEntity.getArtistName());
                        photoResponse.setUser(user);
                        photoResponseList.add(photoResponse);
                    }

                    if (mView == null || !mView.isActive()) {
                        return;
                    }
                    mView.showAllPhotos(photoResponseList, mRepository.getPhotoShowingQuality(), isFirstPage);
                    isFirstPage = false;
                }

                @Override
                public void onLoadFail() {

                }
            });
        } else if (PHOTO_CATEGORY == PhotoCategory.SHOW_WANTED) {
            Log.i(TAG, "inside photo category == show wanted with search query ==" + mSearchQuery);
            mSearchQuery = getSearchQueryWantedPhoto();
            if (mSearchQuery != null && !mSearchQuery.isEmpty()) {
                Log.i(TAG, "inside mSearchQuery != null");
                searchPhotoByQuery(mSearchQuery);
            } else {
                Log.i(TAG, "inside mSearchQuery == null");
                mView.showTurnOnWantedFunction();
            }
        } else if (PHOTO_CATEGORY == PhotoCategory.SHOW_DOWNLOADED) {

            mRepository.loadDownloadedPhotos(new PhotoDataSource.LoadDownloadedPhotosCallback() {
                @Override
                public void onLoadSuccess(List<DownloadedPhotoEntity> downloadedList) {

                    List<PhotoResponse> photoResponseList = new ArrayList<>();
                    for (DownloadedPhotoEntity downloadedEntity : downloadedList){
                        PhotoResponse photoResponse = new PhotoResponse();
                        photoResponse.setId(downloadedEntity.getId());
                        Urls urls = new Urls();
                        urls.setRaw(downloadedEntity.getRaWUrl());
                        urls.setRegular(downloadedEntity.getRegularUrl());
                        urls.setFull(downloadedEntity.getFullUrl());
                        urls.setSmall(downloadedEntity.getSmallUrl());
                        urls.setThumb(downloadedEntity.getThumbUrl());
                        photoResponse.setUrls(urls);
                        User user = new User();
                        user.setName(downloadedEntity.getArtistName());
                        photoResponse.setUser(user);
                        photoResponseList.add(photoResponse);
                    }

                    if (mView == null || !mView.isActive()) {
                        return;
                    }
                    mView.showAllPhotos(photoResponseList, mRepository.getPhotoShowingQuality(), isFirstPage);
                }

                @Override
                public void onLoadFail() {

                }
            });
        }
        if (mView == null || !mView.isActive()) {
            return;
        }
        showCategoryTitle();
    }



    private void showCategoryTitle() {
        mView.showCategoryTitle();
    }

    @Override
    public void searchPhotoByQuery(String query) {

        if(Strings.isNullOrEmpty(query))
            return;
        setSearchQuery(query);
        if (isFirstPage) {
            mCurrentPage = 1;
        } else {
            mCurrentPage++;
        }

            if (mView != null && isFirstPage) {
                Log.i(TAG, "inside mView != null");
                mView.setLoadingIndicator(true);
            }

        mRepository.searchPhotoByQuery(query, new PhotoDataSource.SearchPhotoByQueryCallback() {
            @Override
            public void onLoadSuccess(SearchPhotoResponse searchPhotoResponse) {
                List<PhotoResponse> photoResponseList = searchPhotoResponse.getResults();
                if(getCategory() == PhotoCategory.SHOW_WANTED) {
                    if (photoResponseList != null) {
                        String lastSearchId = photoResponseList.get(0).getId();
                        saveLastSearchWantedPhotoId(lastSearchId);
                    }

                }
                mView.showAllPhotos(photoResponseList, mRepository.getPhotoShowingQuality(), isFirstPage);

            }

            @Override
            public void onLoadFail() {
                mView.showLoadAllPhotosError();
            }
        }, mCurrentPage);
    }

    private void saveLastSearchWantedPhotoId(String lastSearchId) {
        mRepository.saveLastSearchWantedPhotoId(lastSearchId);

    }

    @Override
    public void moveToDetailPage(PhotoResponse photoResponse) {
        checkNotNull(photoResponse);
        String photoJson = new Gson().toJson(photoResponse);
        mView.moveToPhotoDetailPage(photoJson);
    }

    @Override
    public void setCategory(PhotoCategory category) {
        checkNotNull(category);
        PHOTO_CATEGORY = category;
    }

    @Override
    public PhotoCategory getCategory() {
        return PHOTO_CATEGORY;
    }

    @Override
    public void setSearchQuery(String query) {
        mSearchQuery = query;
    }

    @Override
    public String getSearchQueryWantedPhoto() {
        return mRepository.getSearchQueryWantedPhoto();
    }

    @Override
    public void dropView() {
        mView = null;

    }

    @Override
    public void takeView(PhotosContract.View view) {
        mView = view;
    }

    @Override
    public void nextPageAllPhotos() {
        isFirstPage = false;
            if (mConnectivityManager != null) {
                loadPhotos(mConnectivityManager);
            }
    }

    @Override
    public void nextPageSearchPhotos() {
        isFirstPage = false;
        if (mSearchQuery != null) {
            searchPhotoByQuery(mSearchQuery);
        }
    }

    @Override
    public void setIsNewStatus() {
        isFirstPage = true;
    }

    @Override
    public void setIsSearching(boolean isSearching) {
        this.isSearching = isSearching;
    }

    @Override
    public void resetToFirstPage() {
        mCurrentPage = 1;
    }

    @Override
    public boolean isSearching() {
        return isSearching;
    }

    @Override
    public String getSearchQuery() {
        return mSearchQuery;
    }

    @Override
    public void clearMemory(Context context) {
        mRepository.clearMemory(context);
    }

    @Override
    public void onTypeSelected(String type) {
        filterOptions.setType(type);
    }

    @Override
    public void onSortSelected(String sort) {
        filterOptions.setSort(sort);
    }

    public FilterOptionsModel getFilterOptions() {
        return filterOptions;
    }

    @Override
    public void onFilterApply() {
        loadPhotos(mConnectivityManager);

    }

    private boolean isNetworkAvailableAndConnected(ConnectivityManager connectivityManager) {

        boolean isNetworkAvailable = connectivityManager.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                connectivityManager.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }


}
