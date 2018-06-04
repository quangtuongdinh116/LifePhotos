package com.forabetterlife.dtq.myunsplash.photos;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.forabetterlife.dtq.myunsplash.R;
import com.forabetterlife.dtq.myunsplash.data.model.PhotoResponse;
import com.forabetterlife.dtq.myunsplash.di.ActivityScoped;
import com.forabetterlife.dtq.myunsplash.photo.PhotoDetailActivity;
import com.forabetterlife.dtq.myunsplash.utils.PhotoCategory;
import com.forabetterlife.dtq.myunsplash.utils.Utils;
import com.inthecheesefactory.thecheeselibrary.widget.AdjustableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by DTQ on 3/22/2018.
 */

@ActivityScoped
public class PhotosFragment extends PhotosVisibleFragment implements PhotosContract.View  {
    private static final String TAG = "PhotosFragment";

    @Inject
    PhotosContract.Presenter mPresenter;

    OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onPhotoItemClick(PhotoResponse photoResponse) {
            mPresenter.moveToDetailPage(photoResponse);
        }
    };

    private boolean isLoadingMorePage = false;

    private RecyclerView mRecyclerView;

    private LinearLayout mErrorContainer;

    private TextView mErrorMessageTV;

    private RecyclerView.LayoutManager mLayoutManager;

    private PhotosAdapter mPhotosAdapter;

    private Toolbar mToolbar;

    private Menu mMenu;

    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;

    public static PhotosFragment newInstance() {
        return new PhotosFragment();
    }

    @Inject
    public PhotosFragment() {
        // Requires empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "INSIDE onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPhotosAdapter = new PhotosAdapter(new ArrayList<PhotoResponse>());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "INSIDE onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_all_photos, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.all_photos_recycler_view);
        mErrorContainer = (LinearLayout) rootView.findViewById(R.id.error_container);
        mErrorMessageTV = (TextView) rootView.findViewById(R.id.error_message);

        setUpRecyclerView();

        mToolbar = getActivity().findViewById(R.id.toolbar);

        //Set up progress indicator
//        final SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mPresenter.takeView(PhotosFragment.this);
//                if (mPresenter.isSearching()) {
//                    mPresenter.setIsSearching(false);
//                    mPresenter.setIsNewStatus();
//                    loadPhotos();
////                    mPresenter.searchPhotoByQuery(mPresenter.getSearchQuery());
//                    closeKeyboard();
//                } else {
//                    loadPhotos();
//                }
//            }
//        });

        mPresenter.takeView(this);
//        loadPhotos();

        return rootView;
    }

    @Override
    public void onResume() {
        Log.i(TAG, "INSIDE onResume");
        super.onResume();
        mPresenter.takeView(this);
//        if (mPresenter.getCategory() == PhotoCategory.SHOW_FAVORITE || mPresenter.getCategory() == PhotoCategory.SHOW_WANTED) {
            loadPhotos();
//        }

        closeKeyboard();
    }

    public void loadPhotos() {
        mPresenter.loadPhotos((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
        if (mPresenter.getCategory() == PhotoCategory.SHOW_ALL) {
            inflater.inflate(R.menu.fragment_photos,menu);
            MenuItem searchItem = menu.findItem(R.id.menu_item_search);
            final SearchView searchView = (SearchView) searchItem.getActionView();



            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    mPhotosAdapter = new PhotosAdapter(new ArrayList<PhotoResponse>());
                    mRecyclerView.setAdapter(mPhotosAdapter);
                    mPresenter.setIsSearching(true);
                    mPresenter.setIsNewStatus();
                    mPresenter.searchPhotoByQuery(query);
                    closeKeyboard();
                    searchView.onActionViewCollapsed();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }



    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getContext());

//        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mPhotosAdapter);


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                Log.i(TAG, "INSIDE onScrollStateChanged");
//                super.onScrollStateChanged(recyclerView, newState);
//                if (isLoadingMorePage) {
//                    return;
//                }
//                if (mPresenter.getCategory() == PhotoCategory.SHOW_FAVORITE) {
//                    return;
//                }
//                if (!mRecyclerView.canScrollVertically(1) && !isLoadingMorePage) {
//                    isLoadingMorePage = true;
//                    Log.i(TAG, "cr7");
//                    mRecyclerView.getRecycledViewPool().clear();
//                    if (mPresenter.isSearching()) {
//                        Log.i(TAG, "inside addOnScrollListener isSearching");
//                        mPresenter.nextPageSearchPhotos();
//                    } else {
//                        Log.i(TAG, "inside addOnScrollListener isSearching else");
//                        mPresenter.nextPageAllPhotos();
//                    }
//                } else {
//                    Log.i(TAG, "INSIDE mRecyclerView.canScrollVertically ELSE");
//                }
//            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.i(TAG, "INSIDE onScrolled");
                if (isLoadingMorePage || dy < 0) {
                    Log.i(TAG, "isLoadingMorePage || dy < 0");
                    return;
                }
                if (mPresenter.getCategory() == PhotoCategory.SHOW_FAVORITE) {
                    return;
                }
                if (!mRecyclerView.canScrollVertically(1) && !isLoadingMorePage) {
                    isLoadingMorePage = true;
                    Log.i(TAG, "cr7");
                    mRecyclerView.getRecycledViewPool().clear();
                    if (mPresenter.isSearching()) {
                        Log.i(TAG, "inside addOnScrollListener isSearching");
                        mPresenter.nextPageSearchPhotos();
                    } else {
                        Log.i(TAG, "inside addOnScrollListener isSearching else");
                        mPresenter.nextPageAllPhotos();
                    }
                    isLoadingMorePage = false;
                } else {
                    Log.i(TAG, "INSIDE mRecyclerView.canScrollVertically ELSE");
                }
            }
        });


    }

    @Override
    public void setPresenter(PhotosContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showAllPhotos(List<PhotoResponse> list,String photoQuality, boolean isNew) {
        Log.i(TAG, "inside showAllPhotos with list size = " + list.size());
        if (list != null && list.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mErrorContainer.setVisibility(View.VISIBLE);
            mErrorMessageTV.setText("NO PHOTOS");
        } else if (list != null && list.size() != 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mErrorContainer.setVisibility(View.GONE);
            if (isNew) {
                Log.i(TAG, "inside isNew");
                mPhotosAdapter.setList(list, photoQuality);
            } else {
                Log.i(TAG, "inside isNew else");
                mPhotosAdapter.addItems(list);
            }
        }
    }

    @Override
    public void showLoadAllPhotosError() {
//        Snackbar.make(getView(),"Error occured when loaded photos",Snackbar.LENGTH_LONG).show();
        Log.i(TAG, "inside show load photos error");
    }

    @Override
    public void moveToPhotoDetailPage(String photoJson) {
        Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
        intent.putExtra(PhotoDetailActivity.EXTRA_PHOTO, photoJson);
        startActivity(intent);
    }

    @Override
    public void showCategoryTitle() {
        PhotoCategory category = mPresenter.getCategory();
        switch (category) {
            case SHOW_ALL:
                mToolbar.setTitle(getString(R.string.app_name));
                break;
            case SHOW_FAVORITE:
                mToolbar.setTitle(getString(R.string.favorite_toolbar_title));
                break;
            case SHOW_WANTED:
                mToolbar.setTitle(getString(R.string.wanted_photo_toolbar_title));
                break;
            default:
                mToolbar.setTitle(getString(R.string.all_photo_toolbar_title));
        }
    }

    @Override
    public void showNoInternetError() {
        mRecyclerView.setVisibility(View.GONE);
        mErrorContainer.setVisibility(View.VISIBLE);
        mErrorMessageTV.setText("Please connect to Internet to load photos");
    }

    @Override
    public void showTurnOnWantedFunction() {

        mRecyclerView.setVisibility(View.GONE);
        mErrorContainer.setVisibility(View.VISIBLE);
        mErrorMessageTV.setText("You can add keyword for wanted photos in settings");
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
//        if (getView() == null) {
//            return;
//        }
//        final SwipeRefreshLayout srl = getView().findViewById(R.id.swipe_refresh_layout);
//
//        srl.post(new Runnable() {
//            @Override
//            public void run() {
//                srl.setRefreshing(active);
//            }
//        });
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


    public class PhotosAdapter extends RecyclerView.Adapter<PhotoViewHolder> {
        private List<PhotoResponse> mPhotoList;

        private String mPhotoQuality;

        public PhotosAdapter(List<PhotoResponse> photos) {
            mPhotoList = photos;
        }

        public void setList(List<PhotoResponse> photoList, String photoQuality) {
            Log.i(TAG, "INSIDE setList");
            mPhotoList = photoList;
            mPhotoQuality = photoQuality;
            notifyDataSetChanged();
        }

        public void addItems(List<PhotoResponse> photos) {
            Log.i(TAG, "INSIDE addItems");
            int lastPos = mPhotoList.size() - 1;
            mPhotoList.addAll(photos);
            notifyItemRangeInserted(lastPos, photos.size());
        }

        @NonNull
        @Override
        public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.i(TAG, "INSIDE onCreateViewHolder");
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.photo_item, parent, false);
            return new PhotoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
            Log.i(TAG, "INSIDE onBindViewHolder");
            PhotoResponse photo = mPhotoList.get(position);
            holder.bind(photo, mPhotoQuality);
        }

        @Override
        public int getItemCount() {
            return mPhotoList == null? 0 : mPhotoList.size();
        }
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private PhotoResponse mPhoto;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.photo_image);
            itemView.setOnClickListener(this);
        }

        public void bind(PhotoResponse photo, String photoQuality) {
            mPhoto = photo;
            String photoUrl = Utils.getPhotoUrlBaseOnQuality(photoQuality,photo);
            Log.i(TAG, "photoUrl is: " + photoUrl);

//            Glide.with(getContext())
//                    .load(photoUrl)
//                    .into(mImageView);

//            Picasso.with(getContext())
//                    .setLoggingEnabled(true);
//            Picasso.with(getActivity())
//                    .load(photoUrl)
//                    .into(mImageView);

            Glide.with(getContext())
                    .load(photoUrl)
                    .into(mImageView);

//            Picasso.get().load(photoUrl).into(mImageView);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onPhotoItemClick(mPhoto);
        }
    }

    public interface OnItemClickListener {
        void onPhotoItemClick(PhotoResponse photoResponse);
    }


}
