package com.mycompany.displayingbitmaps.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.mycompany.displayingbitmaps.util.RecyclingBitmapDrawable;

public class RecyclingImageView extends AppCompatImageView {

    public RecyclingImageView(Context context) {
        super(context);
    }

    public RecyclingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDetachedFromWindow() {
        // This has been detached from Window, so clear the drawable
        setImageDrawable(null);

        super.onDetachedFromWindow();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        //Keep hold of previous Drawable
        final Drawable previousDrawable = getDrawable();

        //Call super to set new Drawable
        super.setImageDrawable(drawable);

        //Notify new Drawable that it is being displayed

    }

    //notify the drawable that it's displayed state has changed
    public static void notifyDrawable(Drawable drawable, final boolean isDisplyed) {
        if (drawable instanceof RecyclingBitmapDrawable) {
            // The drawable is a CountingBitmapDrawable, so notify it
            ((RecyclingBitmapDrawable) drawable).setIsDisplayed(isDisplyed);
        } else if (drawable instanceof LayerDrawable) {
            // The drawable is a LayerDrawable, so recurse on each layer
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            for(int i = 0, z = layerDrawable.getNumberOfLayers(); i < z; i++) {
                notifyDrawable(layerDrawable.getDrawable(i), isDisplyed);
            }
        }
    }
}
