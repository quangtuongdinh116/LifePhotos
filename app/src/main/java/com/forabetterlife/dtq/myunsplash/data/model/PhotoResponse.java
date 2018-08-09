package com.forabetterlife.dtq.myunsplash.data.model;

/**
 * Created by DTQ on 3/22/2018.
 */

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.bumptech.glide.signature.ObjectKey;
import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.R;
import com.forabetterlife.dtq.myunsplash.utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.items.AbstractItem;

public class PhotoResponse extends AbstractItem<PhotoResponse, PhotoResponse.PhotoViewHolder> {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("width")
    @Expose
    private int width;
    @SerializedName("height")
    @Expose
    private int height;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("categories")
    @Expose
    private List<Object> categories = null;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("urls")
    @Expose
    private Urls urls;
    @SerializedName("links")
    @Expose
    private Links_ links;
    @SerializedName("liked_by_user")
    @Expose
    private Boolean likedByUser;
    @SerializedName("sponsored")
    @Expose
    private Boolean sponsored;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("current_user_collections")
    @Expose
    private List<Object> currentUserCollections = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public List<Object> getCategories() {
        return categories;
    }

    public void setCategories(List<Object> categories) {
        this.categories = categories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public Links_ getLinks() {
        return links;
    }

    public void setLinks(Links_ links) {
        this.links = links;
    }

    public Boolean getLikedByUser() {
        return likedByUser;
    }

    public void setLikedByUser(Boolean likedByUser) {
        this.likedByUser = likedByUser;
    }

    public Boolean getSponsored() {
        return sponsored;
    }

    public void setSponsored(Boolean sponsored) {
        this.sponsored = sponsored;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public List<Object> getCurrentUserCollections() {
        return currentUserCollections;
    }

    public void setCurrentUserCollections(List<Object> currentUserCollections) {
        this.currentUserCollections = currentUserCollections;
    }

    private String quality;

    public PhotoResponse withQuality(String quality) {
        this.quality = quality;
        return this;
    }



    @NonNull
    @Override
    public PhotoViewHolder getViewHolder(View v) {
        return new PhotoViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.photo_item_container;
    }

    @Override
    public int getLayoutRes() {
       return R.layout.photo_item;
    }

    @Override
    public void bindView(PhotoViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        DisplayMetrics displaymetrics = MyUnSplash.getInstance().getResources().getDisplayMetrics();
        float finalHeight = displaymetrics.widthPixels / ((float)width/(float)height);

        ViewPropertyTransition.Animator fadeAnimation = new ViewPropertyTransition.Animator() {
            @Override
            public void animate(View view) {
                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                fadeAnim.setDuration(500);
                fadeAnim.start();
            }
        };

        RequestOptions requestOptions = new RequestOptions().signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(holder.mImageView.getContext())
                .load(Utils.getPhotoUrlBaseOnQuality(quality,this))
                .transition(GenericTransitionOptions.with(fadeAnimation))
                .into(holder.mImageView);
        holder.mImageView.setMinimumHeight((int) finalHeight);

    }

    protected static class PhotoViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private PhotoResponse mPhoto;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.photo_image);
        }


        public void recycle() {
//            Glide.get(mImageView.getContext())
//                    .clearMemory();
//            mPresenter.clearMemory(mImageView.getContext());
        }
    }

}
