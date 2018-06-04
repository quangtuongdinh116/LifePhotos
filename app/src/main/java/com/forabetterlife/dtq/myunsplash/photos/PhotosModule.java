package com.forabetterlife.dtq.myunsplash.photos;

import com.forabetterlife.dtq.myunsplash.di.ActivityScoped;
import com.forabetterlife.dtq.myunsplash.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by DTQ on 4/29/2018.
 */

@Module
public abstract class PhotosModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract PhotosFragment photosFragment();

    @ActivityScoped
    @Binds
    abstract PhotosContract.Presenter photosPresenter(PhotosPresenter presenter);
}
