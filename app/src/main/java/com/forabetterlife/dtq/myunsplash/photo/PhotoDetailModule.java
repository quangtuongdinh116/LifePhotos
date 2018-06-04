package com.forabetterlife.dtq.myunsplash.photo;

import com.forabetterlife.dtq.myunsplash.di.ActivityScoped;
import com.forabetterlife.dtq.myunsplash.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

import static com.forabetterlife.dtq.myunsplash.photo.PhotoDetailActivity.EXTRA_PHOTO;

/**
 * Created by DTQ on 4/29/2018.
 */

@Module
public abstract class PhotoDetailModule {


    @FragmentScoped
    @ContributesAndroidInjector
    abstract PhotoDetailFragment photoDetailFragment();

    @ActivityScoped
    @Binds
    abstract PhotoDetailContract.Presenter statitsticsPresenter(PhotoDetailPresenter presenter);

    @Provides
    @ActivityScoped
    static String providePhotoJsonString(PhotoDetailActivity activity) {
        return activity.getIntent().getStringExtra(EXTRA_PHOTO);
    }
}
