package com.forabetterlife.dtq.myunsplash.photo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.forabetterlife.dtq.myunsplash.R;
import com.forabetterlife.dtq.myunsplash.utils.ThemeUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;


public class PhotoDetailActivity extends DaggerAppCompatActivity {
    public static final String EXTRA_PHOTO = "com.forabetterlife.dtq.myunsplash.photo.extra_photo";

    @Inject
    PhotoDetailFragment injectedFragment;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        switch (ThemeUtils.getTheme(this)) {
            case ThemeUtils.Theme.DARK_GREEN:

                setTheme(R.style.AppTheme_NoActionBar);
                break;
            case ThemeUtils.Theme.BLACK:

                setTheme(R.style.AppTheme_Black);
                break;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (getIntent() == null || getIntent().getStringExtra(EXTRA_PHOTO) == null) {
            Toast.makeText(this, "Can not load information about this photo", Toast.LENGTH_LONG).show();
            finish();
        }

        PhotoDetailFragment photoDetailFragment = (PhotoDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (photoDetailFragment == null) {
            photoDetailFragment = injectedFragment;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, photoDetailFragment)
                    .commit();
        }
    }
}
