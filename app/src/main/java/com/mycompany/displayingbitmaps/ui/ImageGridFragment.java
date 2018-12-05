package com.mycompany.displayingbitmaps.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.mycompany.displayingbitmaps.R;
import com.mycompany.displayingbitmaps.provider.Image;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageGridFragment extends Fragment {


    public ImageGridFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_image_grid, container, false);
        final GridView mGridView = (GridView) v.findViewById(R.id.gridView);
        return v;
    }

    private class ImageAdapter extends BaseAdapter {

        private final Context mContext;
        private int mItemHeight = 0;
        private int mNumCloumns = 0;
        private int mActionBarHeight = 0;
        private GridView.LayoutParams mImageViewLayoutParams;

        public ImageAdapter(Context context) {
            super();
            mContext = context;
            mImageViewLayoutParams = new GridView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            //Calculate ActionBar height
            TypedValue tv = new TypedValue();
            if(context.getTheme().resolveAttribute(
                    android.R.attr.actionBarSize, tv, true)) {
                mActionBarHeight = TypedValue.complexToDimensionPixelSize(
                        tv.data, context.getResources().getDisplayMetrics());
            }

        }

        @Override
        public int getCount() {
            //If colums have yet to be determined, return no items
            if(getNumCloumns() == 0) {
                return 0;
            }

            // Size + number of columns for top empty row
            return Image.imageThumbUrls.length + mNumCloumns;
        }

        @Override
        public Object getItem(int i) {
            return i < mNumCloumns ?
                    null : Image.imageThumbUrls[i - mNumCloumns];
        }

        @Override
        public long getItemId(int i) {
            return i < mNumCloumns ? 0 : i - mNumCloumns;
        }

        @Override
        public int getViewTypeCount() {
            // Two types of views, the normal ImageView and the top row of empty views
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return position < mNumCloumns ? 1 : 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            //First check if this is the top row
            if(i < mNumCloumns) {
                if(view == null) {
                    view = new View(mContext);
                }
                // set empty view with height of ActionBar
                view.setLayoutParams(new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,mActionBarHeight));
                return  view;
            }

            //No handle the main ImageView thumbnails
            ImageView imageView;
            if (view == null) {
                imageView = new RecyclingImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(mImageViewLayoutParams);
            } else {
                imageView = (ImageView) view;
            }

            // Check the height matches our calculated column width
            if (imageView.getLayoutParams().height != mItemHeight) {
                imageView.setLayoutParams(mImageViewLayoutParams);
            }

            // Finally load the image asynchronously into the ImageView, this also takes care of
            // setting a placeholder image while the background thread runs

            return imageView;
        }

        public void setItemHeight(int height) {
            if (height == mItemHeight) {
                return;
            }
            mItemHeight = height;
            mImageViewLayoutParams =
                    new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight);

            notifyDataSetChanged();
        }

        public void setNumCloumns(int numCloumns) {
            mNumCloumns = numCloumns;
        }

        public int getNumCloumns() {
            return mNumCloumns;
        }
    }

}
