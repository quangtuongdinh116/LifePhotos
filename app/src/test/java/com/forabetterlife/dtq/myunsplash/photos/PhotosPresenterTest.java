package com.forabetterlife.dtq.myunsplash.photos;

import com.forabetterlife.dtq.myunsplash.data.PhotoDataSource;
import com.forabetterlife.dtq.myunsplash.data.PhotoRepository;
import com.forabetterlife.dtq.myunsplash.data.local.FavoriteEntity;
import com.forabetterlife.dtq.myunsplash.data.model.PhotoResponse;
import com.forabetterlife.dtq.myunsplash.data.model.Urls;
import com.forabetterlife.dtq.myunsplash.data.model.User;
import com.forabetterlife.dtq.myunsplash.utils.PhotoCategory;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * Created by DTQ on 4/30/2018.
 */

public class PhotosPresenterTest {
    private static List<PhotoResponse> PHOTOS;

    private static List<FavoriteEntity> FAVORITEPHOTOS;

    @Mock
    private PhotoRepository mPhotosRepository;

    @Mock
    private PhotosContract.View mPhotosView;

    @Captor
    private ArgumentCaptor<PhotoDataSource.LoadAllPhotosCallback> mLoadPhotosCallbackCaptor;

    @Captor
    private ArgumentCaptor<PhotoDataSource.LoadFavoritesCallback> mLoadFavoriteCallbackCaptor;

    private PhotosPresenter mPhotosPresenter;



    @Before
    public void setupPhotosPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mPhotosPresenter = new PhotosPresenter(mPhotosRepository);
        mPhotosPresenter.takeView(mPhotosView);

        //ALL PHOTOS
        PhotoResponse photoResponse1 = new PhotoResponse();
        photoResponse1.setId("1");
        Urls urls = new Urls();
        urls.setRaw("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=00442529804c1bb132ac5daad093c03b");
        urls.setRegular("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026cs=tinysrgb\\u0026w=1080\\u0026fit=max\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=9195ee18a00eb05b2d9bd0fc6b580382");
        urls.setFull("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026q=85\\u0026fm=jpg\\u0026crop=entropy\\u0026cs=srgb\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=f58f9219a066facf0d98ed20b0191572");
        urls.setSmall("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026cs=tinysrgb\\u0026w=400\\u0026fit=max\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=0792fcaf3d6ca39b8c4f2714700da014");
        urls.setThumb("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026cs=tinysrgb\\u0026w=200\\u0026fit=max\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=809b5396809068fdd6d2f06a850e9c90");
        photoResponse1.setUrls(urls);
        User user = new User();
        user.setName("DTQ");
        photoResponse1.setUser(user);

        PhotoResponse photoResponse2 = new PhotoResponse();
        photoResponse2.setId("1");
        Urls urls2 = new Urls();
        urls2.setRaw("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=00442529804c1bb132ac5daad093c03b");
        urls2.setRegular("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026cs=tinysrgb\\u0026w=1080\\u0026fit=max\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=9195ee18a00eb05b2d9bd0fc6b580382");
        urls2.setFull("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026q=85\\u0026fm=jpg\\u0026crop=entropy\\u0026cs=srgb\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=f58f9219a066facf0d98ed20b0191572");
        urls2.setSmall("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026cs=tinysrgb\\u0026w=400\\u0026fit=max\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=0792fcaf3d6ca39b8c4f2714700da014");
        urls2.setThumb("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026cs=tinysrgb\\u0026w=200\\u0026fit=max\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=809b5396809068fdd6d2f06a850e9c90");
        photoResponse2.setUrls(urls2);
        User user2 = new User();
        user2.setName("Teo");
        photoResponse2.setUser(user2);

        PHOTOS = Lists.newArrayList(photoResponse1,photoResponse2);


        //FAVORITE PHOTOS
        FavoriteEntity favoriteEntity1 = new FavoriteEntity();
        favoriteEntity1.setArtistName("TG");
        favoriteEntity1.setId("1");

        favoriteEntity1.setRaWUrl("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=00442529804c1bb132ac5daad093c03b");
        favoriteEntity1.setRegularUrl("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026cs=tinysrgb\\u0026w=1080\\u0026fit=max\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=9195ee18a00eb05b2d9bd0fc6b580382");
        favoriteEntity1.setFullUrl("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026q=85\\u0026fm=jpg\\u0026crop=entropy\\u0026cs=srgb\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=f58f9219a066facf0d98ed20b0191572");
        favoriteEntity1.setSmallUrl("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026cs=tinysrgb\\u0026w=400\\u0026fit=max\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=0792fcaf3d6ca39b8c4f2714700da014");
        favoriteEntity1.setThumbUrl("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026cs=tinysrgb\\u0026w=200\\u0026fit=max\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=809b5396809068fdd6d2f06a850e9c90");

        FavoriteEntity favoriteEntity2 = new FavoriteEntity();
        favoriteEntity2.setArtistName("NP");
        favoriteEntity2.setId("2");

        favoriteEntity2.setRaWUrl("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=00442529804c1bb132ac5daad093c03b");
        favoriteEntity2.setRegularUrl("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026cs=tinysrgb\\u0026w=1080\\u0026fit=max\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=9195ee18a00eb05b2d9bd0fc6b580382");
        favoriteEntity2.setFullUrl("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026q=85\\u0026fm=jpg\\u0026crop=entropy\\u0026cs=srgb\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=f58f9219a066facf0d98ed20b0191572");
        favoriteEntity2.setSmallUrl("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026cs=tinysrgb\\u0026w=400\\u0026fit=max\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=0792fcaf3d6ca39b8c4f2714700da014");
        favoriteEntity2.setThumbUrl("https://images.unsplash.com/photo-1524934992636-ce0dc0e253bc?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026cs=tinysrgb\\u0026w=200\\u0026fit=max\\u0026ixid=eyJhcHBfaWQiOjIzMTc4fQ\\u0026s=809b5396809068fdd6d2f06a850e9c90");

        FAVORITEPHOTOS = Lists.newArrayList(favoriteEntity1,favoriteEntity2);
    }

    @Test
    public void loadAllPhotosFromApiAndLoadIntoView() {

        mPhotosPresenter.setCategory(PhotoCategory.SHOW_ALL);
        mPhotosPresenter.loadPhotos();

        verify(mPhotosRepository).loadAllPhotos(mLoadPhotosCallbackCaptor.capture());
        mLoadPhotosCallbackCaptor.getValue().onLoadSuccess(PHOTOS);

        ArgumentCaptor<List> showPhotosArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mPhotosView).showAllPhotos(showPhotosArgumentCaptor.capture(),anyString());
        assertTrue(showPhotosArgumentCaptor.getValue().size() == 2);
    }

    @Test
    public void loadFavoritePhotosFromApiAndLoadIntoView() {

        mPhotosPresenter.setCategory(PhotoCategory.SHOW_FAVORITE);
        mPhotosPresenter.loadPhotos();

        verify(mPhotosRepository).loadFavorites(mLoadFavoriteCallbackCaptor.capture());
        mLoadFavoriteCallbackCaptor.getValue().onLoadSuccess(FAVORITEPHOTOS);

        ArgumentCaptor<List> showPhotosArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mPhotosView).showAllPhotos(showPhotosArgumentCaptor.capture(),anyString());
        assertTrue(showPhotosArgumentCaptor.getValue().size() == 2);
    }

    @Test
    public void clickOnPhoto_ShowsDetailUi() {

        PhotoResponse photoResponse = PHOTOS.get(0);

        mPhotosPresenter.moveToDetailPage(photoResponse);

        verify(mPhotosView).moveToPhotoDetailPage(anyString());
    }
}
