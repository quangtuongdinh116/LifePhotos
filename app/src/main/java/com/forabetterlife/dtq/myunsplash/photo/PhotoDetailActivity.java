package com.forabetterlife.dtq.myunsplash.photo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.forabetterlife.dtq.myunsplash.R;
import com.forabetterlife.dtq.myunsplash.utils.ThemeUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;


public class PhotoDetailActivity extends DaggerAppCompatActivity {
    public static final String EXTRA_PHOTO = "com.forabetterlife.dtq.myunsplash.photo.extra_photo";

    private SlidrConfig mConfig;

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
                setTheme(R.style.AppTheme_NoActionBar_Detail);
                break;
            case ThemeUtils.Theme.BLACK:

                setTheme(R.style.AppTheme_Black_Detail);
                break;
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        mConfig = new SlidrConfig.Builder()
                .position(SlidrPosition.LEFT)
                .velocityThreshold(2400)
//                .distanceThreshold(.25f)
//                .edge(true)
                .build();


        Slidr.attach(this, mConfig);

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
