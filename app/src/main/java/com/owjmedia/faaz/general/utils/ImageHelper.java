package com.owjmedia.faaz.general.utils;

import android.content.Context;
import android.widget.ImageView;

import com.owjmedia.faaz.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


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

    public void imageLoader(String imageUrl, ImageView imageView) {
        Picasso.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(imageView);

    }

    public void imageLoader(String imageUrl, ImageView imageView, ImageType imageType) {
        switch (imageType) {
            case AVATAR:
                Picasso.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder)
                        .into(imageView);
                break;
            case NEWS:
                Picasso.with(context)
                        .load(imageUrl)
                        .fit()
                        .placeholder(R.drawable.placeholder)
                        .into(imageView);
                break;
            case NEWS_DETAIL:
                Picasso.with(context)
                        .load(imageUrl)
                        .fit()
                        .placeholder(R.drawable.placeholder)
                        .into(imageView);
                break;
            case SIMPLE:
                Picasso.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder)
                        .into(imageView);
                break;
            default:
                break;
        }

    }


    public enum ImageType {
        NEWS, NEWS_DETAIL, AVATAR, SIMPLE
    }

    private static ImageHelper instance;
    private Context context;
}
