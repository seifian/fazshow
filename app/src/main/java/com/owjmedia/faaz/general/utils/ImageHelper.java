package com.owjmedia.faaz.general.utils;

import android.content.Context;
import android.widget.ImageView;

import com.owjmedia.faaz.R;
import com.squareup.picasso.Picasso;


/**
 * Created by salman on 11/11/17.
 */

public class ImageHelper {

    public ImageHelper(Context context) {
        this.context = context;
    }

    public static ImageHelper getInstance(Context context) {
        if (instance == null)
            instance = new ImageHelper(context);
        return instance;
    }

    public void imageLoader(String imageUrl, ImageView imageView, ImageType imageType) {
        switch (imageType) {
            case AVATAR:
                Picasso.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.avatar)
                        .into(imageView);
                break;
            case NEWS:
                Picasso.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_news)
                        .into(imageView);
                break;
            default:
                break;
        }

    }

    public enum ImageType {
        NEWS, AVATAR, POLL
    }

    private static ImageHelper instance;
    private Context context;
}