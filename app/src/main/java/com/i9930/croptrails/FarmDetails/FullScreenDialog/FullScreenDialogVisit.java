package com.i9930.croptrails.FarmDetails.FullScreenDialog;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.FarmDetails.Model.Activity.DoneActivityImage;
import com.i9930.croptrails.FarmDetails.Model.Visit.RecyclerModelClass;
import com.i9930.croptrails.ImagePager.ImageActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.Visit.Visit;
import com.i9930.croptrails.RoomDatabase.Visit.VrCacheManager;
import com.i9930.croptrails.RoomDatabase.Visit.VrFetchListener;
import com.i9930.croptrails.SubmitActivityForm.Adapter.AgriInputAdapterActual;
import com.i9930.croptrails.SubmitActivityForm.Model.DoneActNew.DoneActivityResponseNew;
import com.i9930.croptrails.SubmitVisitForm.Adapter.AgriInputAdapterVisit;
import com.i9930.croptrails.SubmitVisitForm.Model.FetchVisitResponse;
import com.i9930.croptrails.SubmitVisitForm.Model.VisitResponseNew;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class FullScreenDialogVisit extends DialogFragment implements VrFetchListener {
    Context context;
    @BindView(R.id.progressBarVisitDialog)
    GifImageView progressBarVisitDialog;

    @BindView(R.id.detailLayout)
    LinearLayout detailLayout;
    @BindView(R.id.commentLabel)
    TextView commentLabel;
    @BindView(R.id.reasonLabel)
    TextView reasonLabel;
    @BindView(R.id.addedByLabel)
    TextView addedByLabel;
    @BindView(R.id.addedByTv)
    TextView addedByTv;
    @BindView(R.id.farm_detail_label_visit)
    TextView farm_detail_label_visit;

    @BindView(R.id.delqLayout)
    LinearLayout delqLayout;
    @BindView(R.id.reasonTv)
    TextView reasonTv;
    @BindView(R.id.commentTv)
    TextView commentTv;

    @BindView(R.id.lot_no_timeline)
    TextView lot_no;
    @BindView(R.id.farmer_name_timeline)
    TextView farmer_name;
    @BindView(R.id.visited_on_timeline)
    TextView visited_on;
    @BindView(R.id.visit_number_timeline)
    TextView visit_number;
    @BindView(R.id.click_message_tv_timeline)
    TextView click_message_tv_timeline;

    @BindView(R.id.visit_iv0_timeline)
    ImageView visit_iv0;
    @BindView(R.id.visit_iv1_timeline)
    ImageView visit_iv1;
    @BindView(R.id.visit_iv2_timeline)
    ImageView visit_iv2;
    @BindView(R.id.visit_iv3_timeline)
    ImageView visit_iv3;
    @BindView(R.id.visit_iv4_timeline)
    ImageView visit_iv4;
    @BindView(R.id.visit_iv5_timeline)
    ImageView visit_iv5;

    @BindView(R.id.thirdLinearForImageView_timeline)
    LinearLayout thirdLinearForImageView;
    @BindView(R.id.secondLinearForImageView_timeline)
    LinearLayout secondLinearForImageView;
    @BindView(R.id.firstLinearForImageView_timeline)
    LinearLayout firstLinearForImageView;

    RelativeLayout parentRelativeLayout;
    @BindView(R.id.image_circle_layout_timeline)
    RelativeLayout image_circle_layout_timeline;

    @BindView(R.id.inmRecyclerview_timeline)
    RecyclerView inmRecyclerview;

    LinearLayoutManager linearLayoutManager;
    ImagePopup imagePopup;

    @BindView(R.id.timeline_view_visit_parent_rel)
    RelativeLayout timeline_view_visit_parent_rel;
    Snackbar snackbar;

    Uri[] img_uri_array;
    VrFetchListener fetchListener;

    List<Bitmap> imageBitmapList = new ArrayList<>();

    List<String> masterList;

    List<List<RecyclerModelClass>> masterListSetData;


    Resources resources;
    public static String TAG = "FullScreenDialogVisit";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @BindView(R.id.connectivityLine)
    View connectivityLine;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_full_screen_dialog_visit, container, false);
        ButterKnife.bind(this, view);
        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();

        toolbar = view.findViewById(R.id.toolbar_full_visit);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_18dp);

        //this.dismiss();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
        loadAds(view);
        toolbar.setTitle(resources.getString(R.string.visit_button_label));
        toolbar.setTitleTextColor(resources.getColor(R.color.white));
        masterList = new ArrayList<>();
        fetchListener = this;
        masterListSetData = new ArrayList<>();
        progressBarVisitDialog.setVisibility(View.VISIBLE);
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            getCalendarActData();
            //Toast.makeText(context, "Online", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(context, "Offline", Toast.LENGTH_SHORT).show();
            //Toast.makeText(context, "Vr Id "+SharedPreferencesMethod.getString(context, SharedPreferencesMethod.VR_ID), Toast.LENGTH_SHORT).show();
            VrCacheManager.getInstance(context).getVisit(fetchListener, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.VR_ID));
        }
        lot_no.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOT_NO));
        farmer_name.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARMER_NAME));
        farmer_name.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARMER_NAME));

        commentLabel.setText(resources.getString(R.string.comment_label) + " :");
        reasonLabel.setText(resources.getString(R.string.reason) + " :");
        addedByLabel.setText(resources.getString(R.string.added_by_label) + " :");

        return view;
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

    private void internetFlow(boolean isLoaded) {
        try {

            if (!isLoaded) {
                if (!ConnectivityUtils.isConnected(context)) {
                    AppConstants.failToast(context, getResources().getString(R.string.check_internet_connection_msg));
                } else {
                    ConnectivityUtils.checkSpeedWithColor(getActivity(), new ConnectivityUtils.ColorListener() {
                        @Override
                        public void onColorChanged(int color, String speed) {
                            if (color == android.R.color.holo_green_dark) {
                                connectivityLine.setVisibility(View.GONE);
//                                    connectivityLine.setBackgroundColor(getColor(R.color.blue));
                            } else {
                                connectivityLine.setVisibility(View.VISIBLE);

                                connectivityLine.setBackgroundColor(resources.getColor(color));
                            }
                            internetSPeed = speed;
                        }
                    }, 20);
                }
            }


        } catch (Exception e) {

        }

    }

    String internetSPeed = "";

    private void setImages(List<DoneActivityImage> timelineList) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 200, 5);
        layoutParams.setMargins(10, 5, 10, 5);

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.crp_trls_rounded_icon)
                .error(R.drawable.crp_trls_rounded_icon);


        img_uri_array = new Uri[timelineList.size()];

        for (int i = 0; i < timelineList.size(); i++) {
            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                Bitmap bitmap = BitmapFactory.decodeFile(timelineList.get(i).getImgLink());
                imageBitmapList.add(bitmap);
            } else {
                img_uri_array[i] = Uri.parse(timelineList.get(i).getImgLink());
            }

            if (timelineList.get(i) != null && i == 0) {
                Log.e(TAG, "Senting image " + i + 1 + " " + timelineList.get(i).getImgLink());
                //Glide.with(getActivity()).load(timelineList.get(i).getImgLink()).apply(options).into(visit_iv0);
              /*  if (timelineList.get(i).getImgLink() != null && timelineList.get(i).getImgLink().contains("croptrailsimages.s3")) {
                    ShowAwsImage.getInstance(context).downloadFile(Uri.parse(timelineList.get(i).getImgLink()), visit_iv0, timelineList.get(i).getImgLink());
                } else {*/
                Glide.with(getActivity()).load(timelineList.get(i).getImgLink()).apply(options).into(visit_iv0);
                //}
            } else {
                visit_iv0.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.crp_trls_rounded_icon));
            }
            visit_iv0.setLayoutParams(layoutParams);
            visit_iv0.setScaleType(ImageView.ScaleType.CENTER_CROP);
            visit_iv0.setPadding(10, 10, 10, 10);
            if (timelineList.get(i) != null && i == 1) {
                Log.e(TAG, "Senting image " + i + 1 + " " + timelineList.get(i).getImgLink());
               /* if (timelineList.get(i).getImgLink() != null && timelineList.get(i).getImgLink().contains("croptrailsimages.s3")) {
                    ShowAwsImage.getInstance(context).downloadFile(Uri.parse(timelineList.get(i).getImgLink()), visit_iv1, timelineList.get(i).getImgLink());
                } else {*/
                Glide.with(getActivity()).load(timelineList.get(i).getImgLink()).apply(options).into(visit_iv1);
                // }
            } else {
                visit_iv1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.crp_trls_rounded_icon));
            }
            visit_iv1.setLayoutParams(layoutParams);
            visit_iv1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            visit_iv1.setPadding(10, 10, 10, 10);

            if (timelineList.get(i) != null && i == 2) {
                Log.e(TAG, "Senting image " + i + 1 + " " + timelineList.get(i).getImgLink());
                /*if (timelineList.get(i).getImgLink() != null && timelineList.get(i).getImgLink().contains("croptrailsimages.s3")) {
                    ShowAwsImage.getInstance(context).downloadFile(Uri.parse(timelineList.get(i).getImgLink()), visit_iv2, timelineList.get(i).getImgLink());
                } else {*/
                Glide.with(getActivity()).load(timelineList.get(i).getImgLink()).apply(options).into(visit_iv2);
                // }
            } else {
                visit_iv2.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.crp_trls_rounded_icon));
            }
            visit_iv2.setLayoutParams(layoutParams);
            visit_iv2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            visit_iv2.setPadding(10, 10, 10, 10);

            if (timelineList.get(i) != null && i == 3) {
                /*if (timelineList.get(i).getImgLink() != null && timelineList.get(i).getImgLink().contains("croptrailsimages.s3")) {
                    ShowAwsImage.getInstance(context).downloadFile(Uri.parse(timelineList.get(i).getImgLink()), visit_iv3, timelineList.get(i).getImgLink());
                } else {*/
                Log.e(TAG, "Senting image " + i + 1 + " " + timelineList.get(i).getImgLink());
                Glide.with(getActivity()).load(timelineList.get(i).getImgLink()).apply(options).into(visit_iv3);
                // }
            } else {
                visit_iv3.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.crp_trls_rounded_icon));
            }
            visit_iv3.setLayoutParams(layoutParams);
            visit_iv3.setScaleType(ImageView.ScaleType.CENTER_CROP);
            visit_iv3.setPadding(10, 10, 10, 10);
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "ondetatch");
        progressBarVisitDialog.setVisibility(View.INVISIBLE);
    }

    private void onImageClick() {
        if (img_uri_array.length > 0) {
            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                DataHandler.newInstance().setImageBitmapList(imageBitmapList);
            } else {
                DataHandler.newInstance().setImageUriList(img_uri_array);
            }
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
        } else {
            //Toast.makeText(getActivity(), "No images for this Visit", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onVisitLoaded(Visit visit) {
        Gson gson = new Gson();
        VisitResponseNew visitResponse = gson.fromJson(visit.getVisitJson(), VisitResponseNew.class);
        Log.e(TAG,"onVisitLoaded "+new Gson().toJson(visitResponse));
        if (visitResponse != null && visitResponse.getShowResponse() != null) {
            if (visitResponse.getShowResponse().getVisitNumber() != null)
                visit_number.setText(visitResponse.getShowResponse().getVisitNumber());
            if (visitResponse.getShowResponse().getVisitDate() != null)
                visited_on.setText(AppConstants.getShowableDate(visitResponse.getShowResponse().getVisitDate(),context));

            setActivityData(visitResponse.getShowResponse());
        }
        progressBarVisitDialog.setVisibility(View.INVISIBLE);
    }

    private void setActivityData(DoneActivityResponseNew data) {

        if (data != null) {

            if (data.getImages() != null && data.getImages().size() > 0) {
                setImages(data.getImages());
                click_message_tv_timeline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onImageClick();
                    }
                });
                image_circle_layout_timeline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onImageClick();
                    }
                });
            }

            if (data.getAgriInputs() != null && data.getAgriInputs().size() > 0) {
//                multiple_chem_dynamic_lay_parent.setVisibility(View.VISIBLE);

                AgriInputAdapterActual activityAdapter = new AgriInputAdapterActual(getActivity(), data.getData());
                inmRecyclerview.setAdapter(activityAdapter);
                inmRecyclerview.setNestedScrollingEnabled(false);
                inmRecyclerview.setHasFixedSize(true);
                inmRecyclerview.setLayoutManager(new LinearLayoutManager(context));
//                view_activity_progress_bar.setVisibility(View.GONE);

            } else {
//                multiple_chem_dynamic_lay_parent.setVisibility(View.GONE);
            }


        }
        progressBarVisitDialog.setVisibility(View.GONE);
    }

    private void setActivityDataOnline(FetchVisitResponse data) {

        if (data != null) {
            if (data.getVisitNumber()!=null)
                visit_number.setText(data.getVisitNumber());

            if (data.getVisitDate()!=null)
                visited_on.setText(AppConstants.getShowableDate(data.getVisitDate(),context));
            if (data.getImages() != null && data.getImages().size() > 0) {
                setImages(data.getImages());
                click_message_tv_timeline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onImageClick();
                    }
                });
                image_circle_layout_timeline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onImageClick();
                    }
                });
            }


            if (!AppConstants.isValidString(data.getDelqReason())) {
                detailLayout.setVisibility(View.VISIBLE);
                delqLayout.setVisibility(View.GONE);
                if (data.getAgriInputs() != null && data.getAgriInputs().size() > 0) {
//                multiple_chem_dynamic_lay_parent.setVisibility(View.VISIBLE);

                    AgriInputAdapterVisit activityAdapter = new AgriInputAdapterVisit(getActivity(), data.getAgriInputs());
                    inmRecyclerview.setAdapter(activityAdapter);
                    inmRecyclerview.setNestedScrollingEnabled(false);
                    inmRecyclerview.setHasFixedSize(true);
                    inmRecyclerview.setLayoutManager(new LinearLayoutManager(context));
//                view_activity_progress_bar.setVisibility(View.GONE);
                    toolbar.setTitle(resources.getString(R.string.visit_button_label));
                    farm_detail_label_visit.setText(resources.getString(R.string.farm_visit_details_label));

                }
            } else {
                detailLayout.setVisibility(View.GONE);
                delqLayout.setVisibility(View.VISIBLE);
                reasonTv.setText(data.getDelqReason());
                toolbar.setTitle(resources.getString(R.string.visit_button_label));
                toolbar.setTitle(resources.getString(R.string.delinquent_report));
                farm_detail_label_visit.setText(resources.getString(R.string.details_label));
            }
            if (AppConstants.isValidString(data.getComment()))
                commentTv.setText(data.getComment());
            else
                commentTv.setText("N/A");
            addedByTv.setText(data.getAdded());


        }
        progressBarVisitDialog.setVisibility(View.GONE);
    }

    private void getCalendarActData() {
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String activity_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.VR_ID);
        Log.e(TAG, "VR_ID " + activity_id);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getVisitDetails(activity_id, userId, token).enqueue(new Callback<FetchVisitResponse>() {
            @Override
            public void onResponse(Call<FetchVisitResponse> call, Response<FetchVisitResponse> response) {
                String error = null;
                if (response.isSuccessful()) {
                    try {
                        Log.e(TAG, "getCalendarActData Resp " + new Gson().toJson(response.body()));
                        FetchVisitResponse data = response.body();
                        setActivityDataOnline(data);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "getCalendarActData Err " + error);
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showDialog(LandingActivity.this, resources.getString(R.string.failed_load_farm));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
                    notifyApiDelay(getActivity(), response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<FetchVisitResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(getActivity(), call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "Farmer Farm Err " + t.toString());
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialog(getActivity(), resources.getString(R.string.failed_load_farm));
            }
        });
        internetFlow(isLoaded[0]);

    }
}
