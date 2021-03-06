package com.owjmedia.faaz.general.utils.CustomWidgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.owjmedia.faaz.general.Constants;


public class TypefaceTextView extends TextView {
    public TypefaceTextView(Context context) {
        super(context);
        setTypeface(context);
    }

    public TypefaceTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }

    public TypefaceTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }

    public TypefaceTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setTypeface(context);
    }

    private void setTypeface(Context context) {
        Typeface regularTypeface = Typeface.createFromAsset(context.getAssets(), Constants.FONTS.FONT_PATH + Constants.FONTS.FONT_NAME_REGULAR);
        Typeface boldTypeface = Typeface.createFromAsset(context.getAssets(), Constants.FONTS.FONT_PATH + Constants.FONTS.FONT_NAME_BOLD);
        this.setTypeface(regularTypeface, Typeface.NORMAL);
        this.setTypeface(boldTypeface, Typeface.BOLD);
    }
}
