package com.forabetterlife.dtq.myunsplash.photos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.R;
import com.forabetterlife.dtq.myunsplash.about.AboutFragment;
import com.forabetterlife.dtq.myunsplash.data.model.Photo;

import com.forabetterlife.dtq.myunsplash.setting.SettingsPrefActivity;
import com.forabetterlife.dtq.myunsplash.setting.SettingsPresenter;
import com.forabetterlife.dtq.myunsplash.utils.PhotoCategory;
import com.forabetterlife.dtq.myunsplash.utils.WallpaperType;
import com.forabetterlife.dtq.myunsplash.wallpaper.WallpaperFragment;
import com.forabetterlife.dtq.myunsplash.wallpaper.WallpaperPresenter;

import java.util.List;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import retrofit2.Call;
import retrofit2.Response;

import javax.inject.Inject;

public class PhotosActivity extends DaggerAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = "PhotosActivity";
    private static final String CURRENT_CATEGORY_KEY = "CURRENT_CATEGORY_KEY";
    public static final String CATEGORY_INTENT_KEY = "intent_key";
    public static final String CATEGORY_INTENT_VALUE = "intent_key";

    Toolbar toolbar;

    @Inject
    PhotosContract.Presenter mPresenter;
    @Inject
    Lazy<PhotosFragment> photoFragmentProvider;

    private NavigationView navigationView;

    public static Intent newIntent(Context context) {
        return new Intent(context, PhotosActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize default values of settings
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.pref_main, false);

        //Set up the navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        handleIntent();

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
            PhotoCategory currentCategory =
                    (PhotoCategory) savedInstanceState.getSerializable(CURRENT_CATEGORY_KEY);
            mPresenter.setCategory(currentCategory);
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "inside onResume");
        super.onResume();

        //highlight item in navigation view menu
        if (navigationView != null) {
            if (mPresenter != null) {
                Log.i(TAG, "inside mPresenter != null");
                Log.i(TAG, "CATEGORY IS: " + mPresenter.getCategory());
               int id = R.id.nav_all_photos;
                switch (mPresenter.getCategory()) {
                    case SHOW_ALL:
                        break;
                    case SHOW_WANTED:
                        id = R.id.nav_wanted_photo;
                        break;
                    case SHOW_FAVORITE:
                        id = R.id.nav_favorite;
                        break;
                    case SHOW_DOWNLOADED:
                        id = R.id.nav_downloaded_photo;
                        break;
                    default:
                        break;
                }
                navigationView.setCheckedItem(id);
            }
        }

    }

    public void handleIntent() {

        //when user click wanted photo notification
        if (getIntent() != null && getIntent().getStringExtra(CATEGORY_INTENT_KEY) != null && getIntent().getStringExtra(CATEGORY_INTENT_KEY).equals(CATEGORY_INTENT_VALUE)) {
            addFragmentToActivity(PhotoCategory.SHOW_WANTED);
            String searchKeyWantedPhoto = mPresenter.getSearchQuery();
            mPresenter.setSearchQuery(searchKeyWantedPhoto);
        } else {
            restoreToNewState();
            addFragmentToActivity(PhotoCategory.SHOW_ALL);
            mPresenter.resetToFirstPage();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_CATEGORY_KEY, mPresenter.getCategory());
        super.onSaveInstanceState(outState);
    }

    private void addFragmentToActivity(PhotoCategory category) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentFrame);

//        PhotosFragment photosFragment = (PhotosFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
//            photosFragment = PhotosFragment.newInstance();
            PhotosFragment photosFragment = photoFragmentProvider.get();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame,photosFragment)
                    .commit();
        } else {
            PhotosFragment photosFragment = PhotosFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFrame,photosFragment)
                    .commit();
        }
//        mPresenter = new PhotosPresenter(photosFragment,Inject.provideRepository(getApplicationContext()), category);
        mPresenter.setCategory(category);
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_all_photos) {
            restoreToNewState();
            addFragmentToActivity(PhotoCategory.SHOW_ALL);
            mPresenter.resetToFirstPage();
        } else if (id == R.id.nav_wallpaper) {
            WallpaperFragment wallpaperFragment = new WallpaperFragment();
            WallpaperPresenter presenter = new WallpaperPresenter(com.forabetterlife.dtq.myunsplash.prod.Inject.provideRepository(this),
                    wallpaperFragment, WallpaperType.FAVORITE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFrame, wallpaperFragment)
                    .commit();
        } else if (id == R.id.nav_favorite) {
            restoreToNewState();
            addFragmentToActivity(PhotoCategory.SHOW_FAVORITE);
        } else if (id == R.id.nav_settings) {
            restoreToNewState();
            startActivity(new Intent(PhotosActivity.this, SettingsPrefActivity.class));
        } else if (id == R.id.nav_wanted_photo) {
            restoreToNewState();
            String searchKeyWantedPhoto = mPresenter.getSearchQueryWantedPhoto();
            Log.i(TAG, "SEARCH QUERY IS: " + searchKeyWantedPhoto);
            addFragmentToActivity(PhotoCategory.SHOW_WANTED);
            mPresenter.setSearchQuery(searchKeyWantedPhoto);
        }  else if (id == R.id.nav_downloaded_photo) {
            restoreToNewState();
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + MyUnSplash.DOWNLOAD_PATH);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri,"resource/folder");
            if ( intent.resolveActivity(getPackageManager()) != null) {
                Log.i(TAG, "app found");
                startActivity(Intent.createChooser(intent,"Choose app to open"));
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage("Your photos are located at directory /Pictures/LifePhotos. You can use file manager or photo gallery app to open.")
                        .setCancelable(true);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        } else if (id == R.id.nav_about) {
            restoreToNewState();
            AboutFragment aboutFragment = new AboutFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFrame, aboutFragment)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void restoreToNewState() {
        mPresenter.clearMemory(this);
        mPresenter.setIsNewStatus();
        mPresenter.resetToFirstPage();
        mPresenter.setIsSearching(false);
    }
}
