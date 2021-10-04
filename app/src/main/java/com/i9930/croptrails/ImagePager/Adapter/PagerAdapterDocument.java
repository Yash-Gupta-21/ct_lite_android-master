package com.i9930.croptrails.ImagePager.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

public class PagerAdapterDocument extends PagerAdapter {
    private LayoutInflater layoutInflater;
    List<JsonObject> imageUrlList;
    Context context;
    String TAG="PagerAdapterDocument";
    public PagerAdapterDocument(List<JsonObject> imageUrlList, Context context) {
        this.imageUrlList = imageUrlList;
        this.context = context;
    }


    @Override
    public View instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_slider, container, false);
//        TextView msgTv=view.findViewById(R.id.msgTv);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);

        String link;
        int type = -1;
        String msg = "";

        try {
            type = imageUrlList.get(position).get("type").getAsInt();
            link = imageUrlList.get(position).get("link").getAsString();

            if (type == AppConstants.DOCS.PROFILE) {
                msg = "" + context.getResources().getString(R.string.profile_image);
            } else if (type == AppConstants.DOCS.ID_PROOF) {
                msg = "" + context.getResources().getString(R.string.id_proof);
            } else if (type == AppConstants.DOCS.ADDRESS_PROOF) {
                msg = "" + context.getResources().getString(R.string.address_proof);
            } else if (type == AppConstants.DOCS.FARM) {
                msg = "" + context.getResources().getString(R.string.farm_image);
            } else if (type == AppConstants.DOCS.OWNERSHIP) {
                msg = "" + context.getResources().getString(R.string.ownership_image);
            }
//            msgTv.setText(msg);
//            msgTv.setVisibility(View.VISIBLE);
            photoView.setVisibility(View.GONE);

            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.crp_trails_icon_splash)
                    .error(R.drawable.crp_trails_icon_splash);
//            Uri uriprofile = Uri.parse(link);
            Log.e(TAG,"Msg "+msg+", Link "+link);

           /* Glide
                    .with(context)
                    .load(link)
                    .centerCrop()

                    .placeholder(R.drawable.crp_trls_rounded_icon)
                    .into(im_slider);*/

            Glide.with(context).
                    asBitmap().
                    load(link).
                    into(photoView);

//            photoView.setMaximumScale(5.0F);
//            photoView.setMediumScale(3.0F);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public int getCount() {
        return imageUrlList.size();
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
