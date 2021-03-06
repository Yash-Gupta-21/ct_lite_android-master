package com.i9930.croptrails.CustomTextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import com.i9930.croptrails.R;

public class CustomTextView extends AppCompatTextView {
    private Typeface tf = null;
    private String customFont;
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFontTextView(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFontTextView(context, attrs);
    }

    public CustomTextView(Context context) {
        super(context);

    }
    public boolean setCustomFontTextView(Context ctx, String asset) {
        try {
            tf = Typeface.createFromAsset (ctx.getAssets (), asset);
        } catch (Exception e) {
            return false;
        }
        setTypeface(tf);
        return true;
    }

    private void setCustomFontTextView(Context ctx, AttributeSet attrs) {
        final TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        customFont = a.getString(R.styleable.CustomEditText_edittextfont);
        setCustomFontTextView(ctx, customFont);
        a.recycle();
    }
}