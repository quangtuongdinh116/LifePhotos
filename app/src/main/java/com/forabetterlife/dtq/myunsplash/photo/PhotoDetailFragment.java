package com.forabetterlife.dtq.myunsplash.photo;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.R;
import com.forabetterlife.dtq.myunsplash.data.model.Photo;
import com.forabetterlife.dtq.myunsplash.data.model.PhotoResponse;
import com.forabetterlife.dtq.myunsplash.di.ActivityScoped;
import com.forabetterlife.dtq.myunsplash.utils.PhotoDetailAction;
import com.forabetterlife.dtq.myunsplash.utils.ThemeUtils;
import com.forabetterlife.dtq.myunsplash.utils.Utils;
import com.inthecheesefactory.thecheeselibrary.widget.AdjustableImageView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * Created by DTQ on 3/23/2018.
 */

@ActivityScoped
public class PhotoDetailFragment extends DaggerFragment implements PhotoDetailContract.View {
    private static final String TAG = "PhotoDetailFragment";

    private static final String BUNDLE_KEY_PHOTO = "bundle_key_photo";

    @Inject
    PhotoDetailContract.Presenter mPresenter;

    private WallpaperDialog wallpaperDialog;

    AdjustableImageView photoIV;
    TextView artistNameTV;

    FloatingActionButton favoriteFab;

    @Inject
    public PhotoDetailFragment() {

    }

    public static PhotoDetailFragment getInstance(String jsonPhotoString) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_PHOTO, jsonPhotoString);
        PhotoDetailFragment photoDetailFragment = new PhotoDetailFragment();
        photoDetailFragment.setArguments(bundle);
        return photoDetailFragment;
    }

    @Override
    public void setPresenter(PhotoDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_detail,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_item_set_wallpaper) {
            Utils.isStoragePermissionGranted(getActivity());
            if (Utils.isStoragePermissionGranted(getActivity()) && !mPresenter.isPhotoNull()) {
                wallpaperDialog = new WallpaperDialog();
                wallpaperDialog.setListener(new WallpaperDialog.WallpaperDialogListener() {
                    @Override
                    public void onCancel() {
                        mPresenter.removeDownloadReference();
                    }
                });
                wallpaperDialog.show(getActivity().getSupportFragmentManager(), null);
                mPresenter.downloadImage((DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE),false);
                mPresenter.setAction(PhotoDetailAction.DOWNLOAD_THEN_SET_WALLPAPER);
            }
            return true;
        } else if (itemId == R.id.menu_item_download) {
            mPresenter.downloadImage((DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE),true);
            mPresenter.setAction(PhotoDetailAction.DOWNLOAD_ONLY);
            return true;
        } else if (itemId == R.id.menu_item_share) {
            shareTextUrl();
            return true;
        } else if (itemId == R.id.menu_item_web) {
            if(!mPresenter.isPhotoNull()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPresenter.getmPhotoResponse().getLinks().getHtml() + MyUnSplash.UNSPLASH_UTM_PARAMETERS));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                    startActivity(Intent.createChooser(intent, "Choose application to open"));
                else
                    Toast.makeText(getActivity(), "You do not have browser app to view", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareTextUrl() {
        if(!mPresenter.isPhotoNull()) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.unsplash_image));
            share.putExtra(Intent.EXTRA_TEXT,  mPresenter.getmPhotoResponse().getLinks().getHtml() + MyUnSplash.UNSPLASH_UTM_PARAMETERS);

            startActivity(Intent.createChooser(share, getString(R.string.share_via)));
        }
    }

    IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            mPresenter.handleDownloadResult(reference);
        }
    };

    @Override
    public void showLoadDetaiError() {
        Toast.makeText(getContext(), "Can not load inforamtion about this photo", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showInformationAboutPhoto(PhotoResponse photo, boolean isFavorite, String photoQuality) {
        String photoUrl = Utils.getPhotoUrlBaseOnQuality(photoQuality, photo);

        if (MyUnSplash.getInstance().getDrawable() != null) {
            photoIV.setImageDrawable(MyUnSplash.getInstance().getDrawable());
            MyUnSplash.getInstance().setDrawable(null);
        } else {
            Glide.with(getContext())
                    .load(photoUrl)
                    .into(photoIV);
        }
        artistNameTV.setText(artistNameTV.getContext().getString(R.string.photo_by,photo.getUser().getName()));
        if(isFavorite) {
            favoriteFab.setImageDrawable(getResources().getDrawable(ThemeUtils.getThemeAttrDrawable(getContext(),R.attr.heartIcon)));
        } else {
            favoriteFab.setImageDrawable(getResources().getDrawable(ThemeUtils.getThemeAttrDrawable(getContext(),R.attr.heartBoderIcon)));
        }
    }



    @Override
    public void sendBroadcast(long reference, DownloadManager downloadManager) {
        getActivity().getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, downloadManager.getUriForDownloadedFile(reference)));
    }

    @Override
    public void startActivityWallpaper(Uri uri) {
        Log.d(TAG, "Crop and Set: " + uri.toString());
        Intent wallpaperIntent = WallpaperManager.getInstance(getContext()).getCropAndSetWallpaperIntent(uri);
        wallpaperIntent.setDataAndType(uri, "image/jpg");
        wallpaperIntent.putExtra("mimeType", "image/jpg");
        startActivityForResult(wallpaperIntent, 13451);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getView() == null)
            return;
        if (requestCode == 12345 && resultCode == Activity.RESULT_OK) {
            Snackbar.make(getView(), "Wallpaper has changed!", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void startActivityWallpapeWhenExceptionOccured(Uri uri) {
        Log.d(TAG, "Chooser: " + uri.toString());
        Intent wallpaperIntent = new Intent(Intent.ACTION_ATTACH_DATA);
        wallpaperIntent.setDataAndType(uri, "image/jpg");
        wallpaperIntent.putExtra("mimeType", "image/jpg");
        wallpaperIntent.addCategory(Intent.CATEGORY_DEFAULT);
        wallpaperIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        wallpaperIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        wallpaperIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivity(Intent.createChooser(wallpaperIntent, "Choose application"));

    }

    @Override
    public void setDownloadFinish() {
        wallpaperDialog.setDownloadFinished(true);
    }

    @Override
    public void dismissWallPaperDialog() {
        wallpaperDialog.dismiss();
    }

    @Override
    public void showFavoriteStatus(boolean isFavorite) {

        if(isFavorite) {
            favoriteFab.setImageDrawable(getResources().getDrawable(ThemeUtils.getThemeAttrDrawable(getContext(),R.attr.heartIcon)));
        } else {
            favoriteFab.setImageDrawable(getResources().getDrawable(ThemeUtils.getThemeAttrDrawable(getContext(),R.attr.heartBoderIcon)));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photo_detail, container, false);

        photoIV = (AdjustableImageView) rootView.findViewById(R.id.photo_image_detail);
        artistNameTV = (TextView) rootView.findViewById(R.id.artist_name);

        favoriteFab = (FloatingActionButton) getActivity().findViewById(R.id.fab_favorite);
        favoriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.handleFavoriteClick();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);
        mPresenter.loadImageInformation();
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onDestroy() {
        mPresenter.dropView();
        super.onDestroy();
    }
}
