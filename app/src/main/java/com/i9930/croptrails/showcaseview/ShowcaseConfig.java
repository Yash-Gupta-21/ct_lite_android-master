package com.i9930.croptrails.showcaseview;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;

import com.i9930.croptrails.showcaseview.shape.Shape;


public class    ShowcaseConfig {

    public static final String DEFAULT_MASK_COLOUR = "#dd335075";

    private long mDelay = -1;
    private int mMaskColour;
    private Typeface mDismissTextStyle;

    private int mContentTextColor;
    private int mDismissTextColor;
    private long mFadeDuration = -1;
    private Shape mShape = null;
    private int mShapePadding = -1;
    private Boolean renderOverNav;
    public static final int DEFAULT_TEXT_GRAVITY = Gravity.LEFT;

    private int dismissTextGravity = DEFAULT_TEXT_GRAVITY ;
    private int titleTextGravity = DEFAULT_TEXT_GRAVITY ;
    private int contentTextGravity = DEFAULT_TEXT_GRAVITY ;

    public ShowcaseConfig() {
        mMaskColour = Color.parseColor(ShowcaseConfig.DEFAULT_MASK_COLOUR);
        mContentTextColor = Color.parseColor("#ffffff");
        mDismissTextColor = Color.parseColor("#ffffff");
    }

    /*>>>>>>>>*/

    public void setDismissTextGravity(int dismissTextGravity) {
        this.dismissTextGravity = dismissTextGravity;
    }

    public void setTitleTextGravity(int titleTextGravity) {
        this.titleTextGravity = titleTextGravity;
    }

    public void setContentTextGravity(int contentTextGravity) {
        this.contentTextGravity = contentTextGravity;
    }

    public int getDismissTextGravity() {
        return dismissTextGravity;
    }

    public int getTitleTextGravity() {
        return titleTextGravity;
    }

    public int getContentTextGravity() {
        return contentTextGravity;
    }


    /*>>>>>>>>>>>>>*/


    public long getDelay() {
        return mDelay;
    }

    public void setDelay(long delay) {
        this.mDelay = delay;
    }

    public int getMaskColor() {
        return mMaskColour;
    }

    public void setMaskColor(int maskColor) {
        mMaskColour = maskColor;
    }

    public int getContentTextColor() {
        return mContentTextColor;
    }

    public void setContentTextColor(int mContentTextColor) {
        this.mContentTextColor = mContentTextColor;
    }

    public int getDismissTextColor() {
        return mDismissTextColor;
    }

    public void setDismissTextColor(int dismissTextColor) {
        this.mDismissTextColor = dismissTextColor;
    }

    public Typeface getDismissTextStyle() {
        return mDismissTextStyle;
    }

    public void setDismissTextStyle(Typeface dismissTextStyle) {
        this.mDismissTextStyle = dismissTextStyle;
    }

    public long getFadeDuration() {
        return mFadeDuration;
    }

    public void setFadeDuration(long fadeDuration) {
        this.mFadeDuration = fadeDuration;
    }

    public Shape getShape() {
        return mShape;
    }

    public void setShape(Shape shape) {
        this.mShape = shape;
    }

    public void setShapePadding(int padding) {
        this.mShapePadding = padding;
    }

    public int getShapePadding() {
        return mShapePadding;
    }

    public Boolean getRenderOverNavigationBar() {
        return renderOverNav;
    }

    public void setRenderOverNavigationBar(boolean renderOverNav) {
        this.renderOverNav = renderOverNav;
    }
}
