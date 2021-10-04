package com.i9930.croptrails.FarmDetails.FullScreenDialog;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.FarmDetails.Model.Visit.VisitImageTimeline;
import com.i9930.croptrails.ImagePager.ImageActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Test.PldModel.PldData;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

public class FullScreenDialogShowPld extends DialogFragment {
    TextView farmIdTv, farmerNameTv, timelineLossArea, lossDateTv, lossReasonTv, addedByTv;
    LinearLayout images_to_upload_lieanr_lay, images_linear_layout_parent;
    ImageView farm_image1_timeline_visit, farm_image2_timeline_visit, farm_image3_timeline_visit, farm_image4_timeline_visit;


    public static String TAG = "FullScreenDialogShowPld";
    Context context;
    String area_unit_label = "";
    static PldData pldDataa;
    Uri[] img_uri_array;

    public static FullScreenDialogShowPld newInstance(PldData pldData) {
        pldDataa = pldData;

        return new FullScreenDialogShowPld();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_full_screen_dialog_pld, container, false);
        area_unit_label = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL);
        initView(view);
        setData();


        return view;
    }
    AdView mAdView;
    private void loadAds(View view){
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        List<String> testDeviceIds = Arrays.asList("1126E5CF85D37A9661950E74DF5A73FE");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
//        mAdView.setAdUnitId("ca-app-pub-5740952327077672/5919581872");
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.e(TAG,"Add Errro "+adError.toString());
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }
    private void initView(View view) {
        loadAds(view);
        Toolbar toolbar = view.findViewById(R.id.toolbar_full);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_18dp);
        //this.dismiss();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
        toolbar.setTitle(context.getResources().getString(R.string.crop_loss_damage_labe));
        toolbar.setTitleTextColor(context.getResources().getColor(R.color.white));
        farmIdTv = view.findViewById(R.id.lot_no_timeline);
        farmerNameTv = view.findViewById(R.id.farmer_name_timeline);
        timelineLossArea = view.findViewById(R.id.timelineLossArea);
        lossDateTv = view.findViewById(R.id.lossDateTv);
        lossReasonTv = view.findViewById(R.id.lossReasonTv);
        addedByTv = view.findViewById(R.id.addedByTv);
        images_to_upload_lieanr_lay = view.findViewById(R.id.images_to_upload_lieanr_lay);
        farm_image1_timeline_visit = view.findViewById(R.id.farm_image1_timeline_visit);
        farm_image2_timeline_visit = view.findViewById(R.id.farm_image2_timeline_visit);
        farm_image3_timeline_visit = view.findViewById(R.id.farm_image3_timeline_visit);
        farm_image4_timeline_visit = view.findViewById(R.id.farm_image4_timeline_visit);
        images_linear_layout_parent = view.findViewById(R.id.images_linear_layout_parent);

    }

    private void setData() {
        PldData data = pldDataa;
        if (data.getName() != null) {
            addedByTv.setText(data.getName());
        } else {
            addedByTv.setText("-");
        }

        lossDateTv.setText(AppConstants.getShowableDate(data.getPldDate(),context));
        if (data.getPldReason() != null && !data.getPldReason().trim().equals("0"))
            lossReasonTv.setText(data.getPldReason());
        else if (data.getOtherReason() != null && !data.getOtherReason().trim().equals("0"))
            lossReasonTv.setText(data.getOtherReason());
        if (AppConstants.isValidString(data.getPldArea()))
        timelineLossArea.setText(AppConstants.getShowableArea(data.getPldArea()) + " " + area_unit_label);
        else
        timelineLossArea.setText("-");

        farmIdTv.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOT_NO));
        farmerNameTv.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARMER_NAME));

        if (data.getPldImages() != null && data.getPldImages().size() > 0) {
            setImages(data.getPldImages());

            images_linear_layout_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onImageClick();
                }
            });
        } else {
            images_linear_layout_parent.setVisibility(View.GONE);
        }
    }

    private void onImageClick() {
        if (img_uri_array.length > 0) {
            DataHandler.newInstance().setImageUriList(img_uri_array);
            Log.e(TAG, "Image Uri List " + new Gson().toJson(img_uri_array));
            Intent intent = new Intent(getActivity(), ImageActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        }
    }

    private void setImages(List<VisitImageTimeline> timelineList) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.crp_trls_rounded_icon)
                .error(R.drawable.crp_trls_rounded_icon);
        img_uri_array = new Uri[timelineList.size()];
        for (int i = 0; i < timelineList.size(); i++) {

            img_uri_array[i] = Uri.parse(timelineList.get(i).getImgLink());

            if (timelineList.get(i) != null && i == 0) {
                Log.e(TAG, "Senting image " + i + 1 + " " + timelineList.get(i).getImgLink());
                Glide.with(getActivity()).load(timelineList.get(i).getImgLink()).apply(options).into(farm_image1_timeline_visit);
            } else {
                farm_image1_timeline_visit.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.crp_trls_rounded_icon));
            }
            if (timelineList.get(i) != null && i == 1) {
                Log.e(TAG, "Senting image " + i + 1 + " " + timelineList.get(i).getImgLink());

                Glide.with(getActivity()).load(timelineList.get(i).getImgLink()).apply(options).into(farm_image2_timeline_visit);
            } else {
                farm_image2_timeline_visit.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.crp_trls_rounded_icon));
            }
            if (timelineList.get(i) != null && i == 2) {
                Log.e(TAG, "Senting image " + i + 1 + " " + timelineList.get(i).getImgLink());
                Glide.with(getActivity()).load(timelineList.get(i).getImgLink()).apply(options).into(farm_image3_timeline_visit);
            } else {
                farm_image3_timeline_visit.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.crp_trls_rounded_icon));
            }
            if (timelineList.get(i) != null && i == 3) {
                Log.e(TAG, "Senting image " + i + 1 + " " + timelineList.get(i).getImgLink());
                Glide.with(getActivity()).load(timelineList.get(i).getImgLink()).apply(options).into(farm_image4_timeline_visit);
            } else {
                farm_image4_timeline_visit.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.crp_trls_rounded_icon));
            }
        }
    }

    private void dismissDialog() {
        this.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {

            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
