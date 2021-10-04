package com.i9930.croptrails.FarmDetails.FullScreenDialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.i9930.croptrails.FarmDetails.Adapter.HealthCardAdapter;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCard;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCardData;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCardResponse;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

public class FullScreenDialogHealthCard extends DialogFragment {
    Context context;
    public static String TAG="DialogHealthCard";
    @BindView(R.id.etHealthCardNo)
    EditText etHealthCardNo;
    @BindView(R.id.sampleNumberEt)
    EditText sampleNumberEt;
    @BindView(R.id.sampleCollectedOnEt)
    EditText sampleCollectedOnEt;
    @BindView(R.id.etFromDate)
    EditText etFromDate;
    @BindView(R.id.etToDate)
    EditText etToDate;
    @BindView(R.id.recyclerAddHealthCardShow)
    RecyclerView recyclerAddHealthCardShow;
    List<HealthCardData>healthCardParamsList=new ArrayList<>();
    @BindView(R.id.tiCardNo)
    TextInputLayout tiCardNo;

    @BindView(R.id.tiSampleNumber)
    TextInputLayout tiSampleNumber;
    @BindView(R.id.tiCollectedOn)
    TextInputLayout tiCollectedOn;
    @BindView(R.id.tiFromDate)
    TextInputLayout tiFromDate;
    @BindView(R.id.tiToDate)
    TextInputLayout tiToDate;
    @BindView(R.id.labNameTv)
    EditText labNameTv;
    @BindView(R.id.labNameTi)
            TextInputLayout labNameTi;

    HealthCard cardResponse;

    public FullScreenDialogHealthCard() {
    }

    public FullScreenDialogHealthCard(HealthCard cardResponse) {
        this.cardResponse = cardResponse;
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_full_screen_dialog_health_card, container, false);
        ButterKnife.bind(this,view);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_18dp);
        //this.dismiss();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
        toolbar.setTitle(context.getResources().getString(R.string.soil_health_card_label));
        toolbar.setTitleTextColor(context.getResources().getColor(R.color.white));

        loadAds(view);
        healthCardParamsList.add(new HealthCardData("pH","7.1","Test Unit"));
        healthCardParamsList.add(new HealthCardData("Organic Carbon (OC)","6.2","Test Unit"));
        healthCardParamsList.add(new HealthCardData(" Nitrogen (N)","5.4","Test Unit"));
        healthCardParamsList.add(new HealthCardData(" Phosphorus (P)","3.6","Test Unit"));
        healthCardParamsList.add(new HealthCardData(" Potassium (K)","7.6","Test Unit"));
        healthCardParamsList.add(new HealthCardData(" Sulphur (S)","5.1","Test Unit"));
        healthCardParamsList.add(new HealthCardData(" Zinc (Zn)","2.8","Test Unit"));
        healthCardParamsList.add(new HealthCardData(" Boron (B)","3.3","Test Unit"));
        String hint=context.getResources().getString(R.string.health_card_no_hint).substring(1);
        String hintNumber=context.getResources().getString(R.string.soil_sample_number_hint).substring(1);
        String hintCollectedOn=context.getResources().getString(R.string.sample_collected_on_hint).substring(1);
        String hintFromDate=context.getResources().getString(R.string.from_date_hint).substring(1);
        String hintToDate=context.getResources().getString(R.string.to_date_hint).substring(1);
        Log.e(TAG,"hint1 "+hint);
        etHealthCardNo.setHint(hint);
        tiCardNo.setHint(hint);
        labNameTi.setHint(context.getResources().getString(R.string.laboratory_name));
        labNameTv.setText(cardResponse.getLaboratoryName());
        sampleNumberEt.setHint(hintNumber);
        sampleCollectedOnEt.setHint(hintCollectedOn);
        etFromDate.setHint(hintFromDate);
        etToDate.setHint(hintToDate);

        tiSampleNumber.setHint(hintNumber);
        tiCollectedOn.setHint(hintCollectedOn);
        tiFromDate.setHint(hintFromDate);
        tiToDate.setHint(hintToDate);

        etHealthCardNo.setText(cardResponse.getHealthCardNum());
        sampleNumberEt.setText(cardResponse.getSoilSampleNum());
        sampleCollectedOnEt.setText(AppConstants.getShowableDate(cardResponse.getSampleCollectionDate(),context));
        etFromDate.setText(AppConstants.getShowableDate(cardResponse.getFromDate(),context));
        etToDate.setText(AppConstants.getShowableDate(cardResponse.getToDate(),context));
        recyclerAddHealthCardShow.setHasFixedSize(true);
        recyclerAddHealthCardShow.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        recyclerAddHealthCardShow.setAdapter(new HealthCardAdapter(context,cardResponse.getParameter()));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle( androidx.fragment.app.DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void dismissDialog() {
      //  view_activity_progress_bar.setVisibility(View.GONE);
        this.dismiss();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "ondetatch");
     //   view_activity_progress_bar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
