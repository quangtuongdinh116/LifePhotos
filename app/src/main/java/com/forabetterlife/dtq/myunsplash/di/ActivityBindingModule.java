package com.forabetterlife.dtq.myunsplash.di;

import com.forabetterlife.dtq.myunsplash.photo.PhotoDetailActivity;
import com.forabetterlife.dtq.myunsplash.photo.PhotoDetailModule;
import com.forabetterlife.dtq.myunsplash.photos.PhotosActivity;
import com.forabetterlife.dtq.myunsplash.photos.PhotosModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by DTQ on 4/29/2018.
 */

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = PhotosModule.class)
    abstract PhotosActivity photosActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PhotoDetailModule.class)
    abstract PhotoDetailActivity photoDetailActivity();
}
