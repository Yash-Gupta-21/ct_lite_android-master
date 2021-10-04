package com.i9930.croptrails.ImagePager.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import pl.droidsonroids.gif.GifImageView;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

public class ViewPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Uri[] imageUrlList;
    Context context;
    List<Bitmap> imageBitmapList;

    public ViewPagerAdapter(Uri[] imageUrlList, Context context) {
        this.imageUrlList = imageUrlList;
        this.context = context;
    }

    public ViewPagerAdapter(Context context, List<Bitmap> imageBitmapList) {
        this.context = context;
        this.imageBitmapList = imageBitmapList;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_slider, container, false);
        ImageView im_slider = (ImageView) view.findViewById(R.id.im_slider);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
        GifImageView progressBar = view.findViewById(R.id.progressBar);
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {

            if (imageUrlList[position] != null) {

              /*  if (imageUrlList[position].toString().contains("croptrailsimages.s3")) {
                    ShowAwsImage.getInstance(context).downloadFile(imageUrlList[position], photoView, imageUrlList[position].toString());
                } else {*/
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.crp_trails_icon_splash)
                            .error(R.drawable.crp_trails_icon_splash);

                    Glide.with(context)
                            .load(imageUrlList[position]).apply(options)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(photoView);


                    photoView.setMaximumScale(5.0F);
                    photoView.setMediumScale(3.0F);
               // }
            } else {
                im_slider.setImageDrawable(context.getResources().getDrawable(R.drawable.crp_trails_icon_splash));
            }
            container.addView(view);
        } else {
            progressBar.setVisibility(View.GONE);
            photoView.setImageBitmap(imageBitmapList.get(position));
            container.addView(view);
        }


        return view;
    }

    @Override
    public int getCount() {
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE))
            return imageUrlList.length;
        else
            return imageBitmapList.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
