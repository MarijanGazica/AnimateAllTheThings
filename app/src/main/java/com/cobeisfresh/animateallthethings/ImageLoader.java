package com.cobeisfresh.animateallthethings;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Marijan on 06/02/2017.
 */
public class ImageLoader {

    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }
}
