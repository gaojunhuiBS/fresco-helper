package com.facebook.fresco.helper.photoview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import com.facebook.fresco.helper.photoview.anim.ViewOptionsCompat;
import com.facebook.fresco.helper.photoview.entity.PhotoInfo;

import java.util.ArrayList;

/**
 * Created by android_ls on 16/9/19.
 */
public class PictureBrowse {

    public static final String PHOTO_LIST_KEY = "photo_list";
    public static final String PHOTO_CURRENT_POSITION_KEY = "photo_current_position";
    public static final String PHOTO_IS_ANIMATION_KEY = "isAnimation";
    public static final String PHOTO_ONLY_ONE_ANIMATION_KEY = "only_one_animation";

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    public static class Builder {
        private Intent mIntent;
        private boolean isAnimation;
        private GridLayoutManager mLayoutManager;
        private ArrayList<String> mThumbnailList;
        private View mThumbnailView;
        private String mOriginalUrl;
        private Context mContext;

        private Builder(Context context) {
            mIntent = new Intent(context, PictureBrowseActivity.class);
            mContext = context;
        }

        public Builder setPhotoList(ArrayList<PhotoInfo> data) {
            int size = data.size();
            mThumbnailList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                PhotoInfo photoInfo = data.get(i);
                if(!TextUtils.isEmpty(photoInfo.thumbnailUrl)) {
                    mThumbnailList.add(photoInfo.thumbnailUrl);
                }
            }
            mIntent.putParcelableArrayListExtra(PHOTO_LIST_KEY, data);
            return this;
        }

        public Builder setPhotoStringList(ArrayList<String> data) {
            int size = data.size();
            ArrayList<PhotoInfo> photos = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                PhotoInfo photoInfo = new PhotoInfo();
                photoInfo.originalUrl = data.get(i);
                photos.add(photoInfo);
            }
            mIntent.putParcelableArrayListExtra(PHOTO_LIST_KEY, photos);
            return this;
        }

        public Builder setOriginalUrl(String originalUrl) {
            mOriginalUrl = originalUrl;

            ArrayList<PhotoInfo> photos = new ArrayList<>();
            PhotoInfo photoInfo = new PhotoInfo();
            photoInfo.originalUrl = originalUrl;
            photos.add(photoInfo);

            mIntent.putParcelableArrayListExtra(PHOTO_LIST_KEY, photos);
            mIntent.putExtra(PHOTO_ONLY_ONE_ANIMATION_KEY, true);
            return this;
        }

        public Builder setPhotoInfo(PhotoInfo photoInfo) {
            mOriginalUrl = photoInfo.originalUrl;

            ArrayList<PhotoInfo> photos = new ArrayList<>();
            photos.add(photoInfo);

            mIntent.putParcelableArrayListExtra(PHOTO_LIST_KEY, photos);
            mIntent.putExtra(PHOTO_ONLY_ONE_ANIMATION_KEY, true);
            return this;
        }

        /**
         * 当前被点击的View在照片墙中的索引
         */
        public Builder setCurrentPosition(int position) {
            mIntent.putExtra(PHOTO_CURRENT_POSITION_KEY, position);
            return this;
        }

        /**
         * 在打开/关闭大图浏览界面时，是否启用动画效果
         */
        public Builder enabledAnimation(boolean isAnimation) {
            this.isAnimation = isAnimation;
            mIntent.putExtra(PHOTO_IS_ANIMATION_KEY, isAnimation);

            if (isAnimation) {
                if (mLayoutManager != null && mThumbnailList != null && mThumbnailList.size() > 0) {
                    Bundle bundle = ViewOptionsCompat.makeScaleUpAnimation(mLayoutManager, mThumbnailList);
                    mIntent.putExtras(bundle);
                } else if (mThumbnailView != null && mOriginalUrl != null) {
                    MLog.i("mOriginalUrl = " + mOriginalUrl);
                    Bundle bundle = ViewOptionsCompat.makeScaleUpAnimation(mThumbnailView, mOriginalUrl);
                    mIntent.putExtras(bundle);
                }
            }
            return this;
        }

        public Builder setLayoutManager(GridLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
            return this;
        }

        public Builder setThumbnailView(View thumbnailView) {
            this.mThumbnailView = thumbnailView;
            return this;
        }

        public void start() {
            mContext.startActivity(mIntent);
            ((Activity) mContext).overridePendingTransition(0, 0);
        }
    }

}
