package com.forabetterlife.dtq.myunsplash.photos;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.R;
import com.forabetterlife.dtq.myunsplash.data.model.PhotoResponse;
import com.forabetterlife.dtq.myunsplash.di.ActivityScoped;
import com.forabetterlife.dtq.myunsplash.photo.PhotoDetailActivity;
import com.forabetterlife.dtq.myunsplash.utils.PhotoCategory;
import com.forabetterlife.dtq.myunsplash.utils.Utils;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;
import com.squareup.leakcanary.RefWatcher;

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

            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.photo_item, null);

            ImageView imageView;
            imageView = (ImageView) view.findViewById(R.id.photo_image);
            if (imageView.getDrawable() != null)
                MyUnSplash.getInstance().setDrawable(imageView.getDrawable());
            mPresenter.moveToDetailPage(photoResponse);
        }
    };

    private boolean isLoadingMorePage = false;

    private RecyclerView mRecyclerView;

    private LinearLayout mMessageContainer;

    private TextView mMessageTV;

    private RecyclerView.LayoutManager mLayoutManager;

//    private PhotosAdapter mPhotosAdapter;

    private Toolbar mToolbar;

    private Menu mMenu;

    private FastItemAdapter<PhotoResponse> mPhotoAdapter;
    private ItemAdapter mFooterAdapter;

    private SwipeRefreshLayout mSwipeContainer;

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

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        mPhotosAdapter = new PhotosAdapter(new ArrayList<PhotoResponse>());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_all_photos, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.all_photos_recycler_view);
        mMessageContainer = (LinearLayout) rootView.findViewById(R.id.message_container);
        mMessageTV = (TextView) rootView.findViewById(R.id.message);
        mSwipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                restoreToNewState();
                loadPhotos();
            }
        });

        setUpRecyclerView();

        mToolbar = getActivity().findViewById(R.id.toolbar);

        mPresenter.takeView(this);
        loadPhotos();

        return rootView;
    }

    @Override
    public void onResume() {

        super.onResume();
        mPresenter.takeView(this);

        if (mPresenter.getCategory() == PhotoCategory.SHOW_FAVORITE || mPresenter.getCategory() == PhotoCategory.SHOW_WANTED) {
            setUpRecyclerView();
            loadPhotos();
        }

        closeKeyboard();
    }

    public void loadPhotos() {
        mPresenter.loadPhotos((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
//        RefWatcher refWatcher = MyUnSplash.getRefWatcher(getActivity());
//        refWatcher.watch(this);

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

                    setUpRecyclerView();
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

        mPhotoAdapter = new FastItemAdapter<>();
        mFooterAdapter = new ItemAdapter();
        mPhotoAdapter.addAdapter(1, mFooterAdapter);
        mPhotoAdapter.withOnClickListener(onClickListener);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        mRecyclerView.setItemViewCacheSize(5);

        mRecyclerView.setAdapter(mPhotoAdapter);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mFooterAdapter) {
            @Override
            public void onLoadMore(int currentPage) {

                mFooterAdapter.clear();
                mFooterAdapter.add(new ProgressItem().withEnabled(false));
                loadMore();
            }
        });

    }

    private void loadMore() {

        if (mPresenter.getCategory() == PhotoCategory.SHOW_FAVORITE) {
            mFooterAdapter.clear();
            return;
        }
        if (mPresenter.isSearching()) {

            mPresenter.nextPageSearchPhotos();
        } else {

            mPresenter.nextPageAllPhotos();
        }
    }

    @Override
    public void setPresenter(PhotosContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showAllPhotos(List<PhotoResponse> list,String photoQuality, boolean isNew) {
        mFooterAdapter.clear();

        if (list != null && list.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mMessageContainer.setVisibility(View.VISIBLE);
            mMessageTV.setVisibility(View.VISIBLE);
            mMessageTV.setText("NO PHOTOS");
        } else if (list != null && list.size() != 0) {

            mMessageContainer.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            List<PhotoResponse> finalList = new ArrayList<>();
            for(PhotoResponse photoResponse : list) {
                photoResponse.withQuality(photoQuality);
                finalList.add(photoResponse);
            }

            if (mSwipeContainer.isRefreshing()) {
                mSwipeContainer.setRefreshing(false);
            }
            if(mPresenter.getCategory().equals(PhotoCategory.SHOW_FAVORITE)) {
                //do nothing
                mPhotoAdapter.clear();
                mPhotoAdapter.add(finalList);
            } else {
                mPhotoAdapter.add(finalList);
            }

            setLoadingIndicator(false);
        }
    }

    @Override
    public void showLoadAllPhotosError() {
        if (getView() == null)
            return;
        Snackbar.make(getView(),"Error occured when loaded photos",Snackbar.LENGTH_LONG).show();

        if (mSwipeContainer.isRefreshing()) {
            mSwipeContainer.setRefreshing(false);
        }
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
        mMessageContainer.setVisibility(View.VISIBLE);
        mMessageTV.setText("Please connect to Internet to load photos");
        if (mSwipeContainer.isRefreshing()) {
            mSwipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void showTurnOnWantedFunction() {

        mRecyclerView.setVisibility(View.GONE);
        mMessageContainer.setVisibility(View.VISIBLE);
        mMessageTV.setVisibility(View.VISIBLE);
        mMessageTV.setText("You can add keyword for wanted photos in settings");
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (mMessageTV != null && active) {

            mRecyclerView.setVisibility(View.GONE);
            mMessageContainer.setVisibility(View.VISIBLE);
            mMessageTV.setVisibility(View.VISIBLE);
            mMessageTV.setText("LOADING...");
        }
        if (mMessageTV != null && !active) {

            mRecyclerView.setVisibility(View.VISIBLE);
            mMessageContainer.setVisibility(View.GONE);
            mMessageTV.setVisibility(View.GONE);
        }

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

            mPhotoList = photoList;
            mPhotoQuality = photoQuality;
            notifyDataSetChanged();
        }

        public void addItems(List<PhotoResponse> photos) {

            int lastPos = mPhotoList.size() - 1;
            mPhotoList.addAll(photos);
            notifyItemRangeInserted(lastPos, photos.size());
        }

        @NonNull
        @Override
        public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.photo_item, parent, false);
            return new PhotoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
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


            Glide.with(mImageView.getContext())
                    .load(photoUrl)
                    .into(mImageView);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onPhotoItemClick(mPhoto);
        }

    }

    public interface OnItemClickListener {
        void onPhotoItemClick(PhotoResponse photoResponse);
    }

    private OnClickListener<PhotoResponse> onClickListener = new OnClickListener<PhotoResponse>() {
        @Override
        public boolean onClick(@javax.annotation.Nullable View v, IAdapter<PhotoResponse> adapter, PhotoResponse item, int position) {
            mOnItemClickListener.onPhotoItemClick(item);
            return false;
        }
    };

    public void restoreToNewState() {
        mPresenter.setIsNewStatus();
        mPresenter.resetToFirstPage();
        mPresenter.setIsSearching(false);
    }


}
