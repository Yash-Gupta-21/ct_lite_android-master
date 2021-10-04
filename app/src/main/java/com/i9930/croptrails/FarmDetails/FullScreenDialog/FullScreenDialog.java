package com.i9930.croptrails.FarmDetails.FullScreenDialog;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineInnerData;
import com.i9930.croptrails.ImagePager.ImageActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.DoneActivities.ActivityCacheManager;
import com.i9930.croptrails.RoomDatabase.DoneActivities.ActivityFetchListener;
import com.i9930.croptrails.RoomDatabase.DoneActivities.DoneActivity;
import com.i9930.croptrails.SubmitActivityForm.Adapter.AgriInputAdapterActual;
import com.i9930.croptrails.SubmitActivityForm.Model.DoneActNew.DoneActivityResponseNew;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

public class FullScreenDialog extends DialogFragment implements ActivityFetchListener {
    Uri[] img_uri_array;

    TextView tv_lot_no_cal_act_deatil, tv_farmer_name_cal_act_detail, tv_activity_name_cal_act_deatail, tv_date_cal_act_detail;
    TextView tv_desc_cal_act_details, no_of_labour_tv_deatil, labour_total_cost_tv_detail, no_of_machine_tv_detail, total_machine_cost_detail;
    TextView total_chemical_qty_detail, total_chemical_cost_tv_detail;
    TimelineInnerData timelineInnerData;
    GifImageView view_activity_progress_bar;
    RecyclerView recyclerChemicals;
    public static String TAG = "FullScreenDialog";
    ImageView timeline_form_img1, timeline_form_img2, timeline_form_img3, timeline_form_img4;
    CircleImageView rounded_act_image, rounded_farm_image;
    RelativeLayout timeline_view_activity_parent_rel, whole_input_rel_lay;
    NestedScrollView whole_nested_scrollview;

    CardView timeline_view_activity_full_card;
    Snackbar snackbar;
    LinearLayout images_linear_layout_parent;
    TextView farmer_reply_tv;

    LinearLayout farmer_reply_linear_lay_only;
    TextView farmer_reply_tv_only;
    Context context;
    Resources resources;

    LinearLayout no_of_labour_layout, labour_cost_layout, number_of_machine_layout, machine_cost_layout, no_of_chemical_layout, chemical_cost_layout, multiple_chem_dynamic_lay_parent;

    TextView tv_lot_no_cal_act_label, tv_farmer_name_cal_act_label, tv_activity_name_cal_act_label,
            tv_date_cal_act_label, tv_desc_cal_act_label, tv_chem_cal_act_label, tv_chem_qty_cal_act_label, no_of_labour_et_label,
            labour_total_cost_tv_label, no_of_machine_tv_label, total_machine_cost_label, total_chemical_qty_label,
            total_chemical_cost_tv_label;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

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
        View view = inflater.inflate(R.layout.layout_full_screen_dialog, container, false);
        connectivityLine = view.findViewById(R.id.connectivityLine);

        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();
        Toolbar toolbar = view.findViewById(R.id.toolbar_full);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_18dp);
        //this.dismiss();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
        loadAds(view);
        toolbar.setTitle(resources.getString(R.string.timeline_activity_label));
        toolbar.setTitleTextColor(resources.getColor(R.color.white));
        tv_lot_no_cal_act_deatil = view.findViewById(R.id.tv_lot_no_cal_act_deatil);
        tv_farmer_name_cal_act_detail = view.findViewById(R.id.tv_farmer_name_cal_act_detail);
        tv_activity_name_cal_act_deatail = view.findViewById(R.id.tv_activity_name_cal_act_deatail);
        tv_date_cal_act_detail = view.findViewById(R.id.tv_date_cal_act_detail);
        tv_desc_cal_act_details = view.findViewById(R.id.tv_desc_cal_act_details);
        no_of_labour_tv_deatil = view.findViewById(R.id.no_of_labour_tv_deatil);
        labour_total_cost_tv_detail = view.findViewById(R.id.labour_total_cost_tv_detail);
        no_of_machine_tv_detail = view.findViewById(R.id.no_of_machine_tv_detail);
        total_machine_cost_detail = view.findViewById(R.id.total_machine_cost_detail);
        total_chemical_qty_detail = view.findViewById(R.id.total_chemical_qty_detail);
        total_chemical_cost_tv_detail = view.findViewById(R.id.total_chemical_cost_tv_detail);
        timeline_form_img1 = view.findViewById(R.id.timeline_form_img1);
        timeline_form_img2 = view.findViewById(R.id.timeline_form_img2);
        timeline_form_img3 = view.findViewById(R.id.timeline_form_img3);
        timeline_form_img4 = view.findViewById(R.id.timeline_form_img4);
        recyclerChemicals = view.findViewById(R.id.chemical_list_recycler_details);
        timeline_view_activity_parent_rel = view.findViewById(R.id.timeline_view_activity_parent_rel);
        whole_input_rel_lay = view.findViewById(R.id.whole_input_rel_lay);
        whole_nested_scrollview = view.findViewById(R.id.whole_nested_scrollview);
        timeline_view_activity_full_card = view.findViewById(R.id.timeline_view_activity_full_card);
        farmer_reply_tv = view.findViewById(R.id.farmer_reply_tv);
        recyclerChemicals.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerChemicals.setHasFixedSize(true);
        timelineInnerData = DataHandler.newInstance().getTimelineInnerData();
        view_activity_progress_bar = view.findViewById(R.id.view_activity_progress_bar);
        images_linear_layout_parent = view.findViewById(R.id.images_linear_layout_parent);
        view_activity_progress_bar.setVisibility(View.VISIBLE);
        //FetchFarmResult fetchFarmResult = DataHandler.newInstance().getFetchFarmResult();
        //tv_lot_no_cal_act_deatil.setText(fetchFarmResult.getLotNo());
        //tv_farmer_name_cal_act_detail.setText(fetchFarmResult.getName());
        tv_activity_name_cal_act_deatail.setText(timelineInnerData.getActivity());
        tv_date_cal_act_detail.setText(timelineInnerData.getDate());
        tv_desc_cal_act_details.setText(timelineInnerData.getDescription());

        no_of_labour_layout = view.findViewById(R.id.no_of_labour_layout);
        labour_cost_layout = view.findViewById(R.id.labour_cost_layout);
        number_of_machine_layout = view.findViewById(R.id.number_of_machine_layout);
        machine_cost_layout = view.findViewById(R.id.machine_cost_layout);
        no_of_chemical_layout = view.findViewById(R.id.no_of_chemical_layout);
        chemical_cost_layout = view.findViewById(R.id.chemical_cost_layout);
        multiple_chem_dynamic_lay_parent = view.findViewById(R.id.multiple_chem_dynamic_lay_parent);

        rounded_act_image = view.findViewById(R.id.rounded_act_image);
        rounded_farm_image = view.findViewById(R.id.rounded_farm_image);

        tv_lot_no_cal_act_deatil.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOT_NO));
        tv_farmer_name_cal_act_detail.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARMER_NAME));

        tv_lot_no_cal_act_label = view.findViewById(R.id.tv_lot_no_cal_act_label);
        tv_farmer_name_cal_act_label = view.findViewById(R.id.tv_farmer_name_cal_act_label);
        tv_activity_name_cal_act_label = view.findViewById(R.id.tv_activity_name_cal_act_label);
        tv_date_cal_act_label = view.findViewById(R.id.tv_date_cal_act_label);
        tv_desc_cal_act_label = view.findViewById(R.id.tv_desc_cal_act_label);
        tv_chem_cal_act_label = view.findViewById(R.id.tv_chem_cal_act_label);
        tv_chem_qty_cal_act_label = view.findViewById(R.id.tv_chem_qty_cal_act_label);
        no_of_labour_et_label = view.findViewById(R.id.no_of_labour_et_label);
        labour_total_cost_tv_label = view.findViewById(R.id.labour_total_cost_tv_label);
        no_of_machine_tv_label = view.findViewById(R.id.no_of_machine_tv_label);
        total_machine_cost_label = view.findViewById(R.id.total_machine_cost_label);
        total_chemical_qty_label = view.findViewById(R.id.total_chemical_qty_label);
        total_chemical_cost_tv_label = view.findViewById(R.id.total_chemical_cost_tv_label);

        tv_lot_no_cal_act_label.setText(resources.getString(R.string.farm_id_label) + ":");
        tv_farmer_name_cal_act_label.setText(resources.getString(R.string.farmer_name_hint) + ":");
        tv_activity_name_cal_act_label.setText(resources.getString(R.string.farm_id_label));
        tv_date_cal_act_label.setText(resources.getString(R.string.date_label));
        tv_desc_cal_act_label.setText(resources.getString(R.string.description_label));
        tv_chem_cal_act_label.setText(resources.getString(R.string.chemical_label) + ":");
        tv_chem_qty_cal_act_label.setText(resources.getString(R.string.quantity_hint) + ":");
        no_of_labour_et_label.setText(resources.getString(R.string.number_of_labour_hint) + ":");
        labour_total_cost_tv_label.setText(resources.getString(R.string.labour_total_cost_hint) + ":");
        no_of_machine_tv_label.setText(resources.getString(R.string.number_of_machines_hint) + ":");
        total_machine_cost_label.setText(resources.getString(R.string.machine_total_cost_hint) + ":");
        total_chemical_qty_label.setText(resources.getString(R.string.chemical_quantity_hint) + ":");
        total_chemical_cost_tv_label.setText(resources.getString(R.string.total_cost_of_chemical_hint) + ":");

        //Setting Latest Image
        if (SharedPreferencesMethod.getString(getActivity(), SharedPreferencesMethod.ACT_LATEST_IMG) != null &&
                !SharedPreferencesMethod.getString(getActivity(), SharedPreferencesMethod.ACT_LATEST_IMG).equals("0")) {
           /* if (SharedPreferencesMethod.getString(getActivity(),SharedPreferencesMethod.ACT_LATEST_IMG).contains("croptrailsimages.s3")) {
                ShowAwsImage.getInstance(context).downloadFile(Uri.parse(SharedPreferencesMethod.getString(getActivity(),SharedPreferencesMethod.ACT_LATEST_IMG)), rounded_farm_image, SharedPreferencesMethod.getString(getActivity(),SharedPreferencesMethod.ACT_LATEST_IMG));
            }else {*/
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.crp_trails_icon_splash)
                    .error(R.drawable.crp_trails_icon_splash);
            Glide.with(getActivity()).load(SharedPreferencesMethod.getString(getActivity(), SharedPreferencesMethod.ACT_LATEST_IMG)).apply(options).into(rounded_farm_image);
            //}
        } else {
            Log.e("MyProblem", "this " + SharedPreferencesMethod.getString(getActivity(), SharedPreferencesMethod.ACT_LATEST_IMG));
            rounded_farm_image.setImageResource(R.drawable.ploughed_farm);
        }

        //Setting  FArm Image
        if (SharedPreferencesMethod.getString(getActivity(), SharedPreferencesMethod.ACT_TYPE_IMG) != null &&
                !SharedPreferencesMethod.getString(getActivity(), SharedPreferencesMethod.ACT_TYPE_IMG).equals("0")) {
           /* if (SharedPreferencesMethod.getString(getActivity(),SharedPreferencesMethod.ACT_TYPE_IMG).contains("croptrailsimages.s3")) {
                ShowAwsImage.getInstance(context).downloadFile(Uri.parse(SharedPreferencesMethod.getString(getActivity(),SharedPreferencesMethod.ACT_TYPE_IMG)), rounded_act_image, SharedPreferencesMethod.getString(getActivity(),SharedPreferencesMethod.ACT_TYPE_IMG));
            }else {*/
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.crp_trails_icon_splash)
                    .error(R.drawable.crp_trails_icon_splash);
            Glide.with(getActivity()).load(SharedPreferencesMethod.getString(getActivity(), SharedPreferencesMethod.ACT_TYPE_IMG)).apply(options).into(rounded_act_image);
            //}
        } else {
            rounded_act_image.setImageResource(R.drawable.ploughed_farm);
        }


        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            String activity_id = SharedPreferencesMethod.getString(getActivity(), SharedPreferencesMethod.ACTITY_ID);
            ActivityCacheManager.getInstance(context).getActivity(this, activity_id);
        } else {
//            FetchDoneActivity fetchDoneActivity = new FetchDoneActivity();
//            fetchDoneActivity.execute();
            getCalendarActData();
        }

        return view;
    }

    private void dismissDialog() {
        view_activity_progress_bar.setVisibility(View.GONE);
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
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }


    private void getCalendarActData() {
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String activity_id = SharedPreferencesMethod.getString(getActivity(), SharedPreferencesMethod.ACTITY_ID);
//        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();

        JsonObject object=new JsonObject();
        object.addProperty("user_id",""+userId);
        object.addProperty("token",""+token);
        object.addProperty("farm_calendar_activity_id",""+activity_id);

        Log.e(TAG,"getCalendarActData SEND "+new Gson().toJson(object));

        apiInterface.getCalendarActData(activity_id, userId, token).enqueue(new Callback<DoneActivityResponseNew>() {
            @Override
            public void onResponse(Call<DoneActivityResponseNew> call, Response<DoneActivityResponseNew> response) {
//                String error = null;
                if (response.isSuccessful()) {
                    try {
                        Log.e(TAG, "getCalendarActData Resp " + new Gson().toJson(response.body()));
                        DoneActivityResponseNew data = response.body();

                        setActivityData(data);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        String error = response.errorBody().string().toString();
                        Log.e(TAG, "getCalendarActData Err " + error);
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showDialog(LandingActivity.this, resources.getString(R.string.failed_load_farm));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

//                long newMillis = AppConstants.getCurrentMills();
//                long diff = newMillis - currMillis;
//                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
//                    notifyApiDelay(LandingActivity.this, response.raw().request().url().toString(),
//                            "" + diff, internetSPeed, error, response.code());
//                }
            }

            @Override
            public void onFailure(Call<DoneActivityResponseNew> call, Throwable t) {
//                long newMillis = AppConstants.getCurrentMills();
//                long diff = newMillis - currMillis;
//                notifyApiDelay(LandingActivity.this, call.request().url().toString(),
//                        "" + diff, internetSPeed, t.toString(), 0);
//                Log.e(TAG, "Farmer Farm Err " + t.toString());
//                ViewFailDialog viewFailDialog = new ViewFailDialog();
//                viewFailDialog.showDialog(LandingActivity.this, resources.getString(R.string.failed_load_farm));
            }
        });
//        internetFlow(isLoaded[0]);

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

    private void onImageClick() {
        if (img_uri_array != null && img_uri_array.length > 0) {

            DataHandler.newInstance().setImageUriList(img_uri_array);
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
            Toast.makeText(getActivity(), resources.getString(R.string.no_image_found_for_activity_msg), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "ondetatch");
        view_activity_progress_bar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityLoaded(DoneActivity doneActivity) {
        if (doneActivity != null&&doneActivity.getActivityJson()!=null&&!TextUtils.isEmpty(doneActivity.getActivityJson())) {

            DoneActivityResponseNew doneActivityResponse = new Gson().fromJson(doneActivity.getActivityJson(), DoneActivityResponseNew.class);
            setActivityData(doneActivityResponse);

        } else {
            whole_nested_scrollview.setVisibility(View.INVISIBLE);
            Snackbar.make(timeline_view_activity_parent_rel, resources.getString(R.string.no_data_found_for_activity_msg), Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            }).show();
        }
    }

    @Override
    public void onActivityLoadingFail(Throwable e) {
        whole_nested_scrollview.setVisibility(View.INVISIBLE);
        Snackbar.make(timeline_view_activity_parent_rel, resources.getString(R.string.no_data_found_for_activity_msg), Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        }).show();
    }

    List<Bitmap> imageBitmapList = new ArrayList<>();

    private void setActivityData(DoneActivityResponseNew data) {

        if (data != null) {
            Log.e(TAG,"Agri Input data "+new Gson().toJson(data.getData()));
            if (data.getData() != null && data.getData().size() > 0) {

                multiple_chem_dynamic_lay_parent.setVisibility(View.VISIBLE);

                AgriInputAdapterActual activityAdapter = new AgriInputAdapterActual(getActivity(), data.getData());
                recyclerChemicals.setAdapter(activityAdapter);
                recyclerChemicals.setNestedScrollingEnabled(false);
                view_activity_progress_bar.setVisibility(View.GONE);

            } else {
                multiple_chem_dynamic_lay_parent.setVisibility(View.GONE);
            }

            if (data.getImages() != null && data.getImages().size() > 0) {
                images_linear_layout_parent.setVisibility(View.VISIBLE);

                /*for (int i=0;i<data.getImages().size();i++){
                    try {
                        Bitmap bitmap = BitmapFactory.decodeFile(data.getImages().get(i).getImgLink());
                        imageBitmapList.add(bitmap);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
*/
                if (data.getImages() != null && data.getImages().size() > 0) {
                    if (data.getImages() != null && data.getImages().size() == 4) {
                        img_uri_array = new Uri[data.getImages().size()];
                        for (int imgCounr = 0; imgCounr < data.getImages().size(); imgCounr++) {
                            Log.e(TAG, "Image Url " + data.getImages().get(imgCounr).getImgLink());
                            img_uri_array[imgCounr] = Uri.parse(data.getImages().get(imgCounr).getImgLink());
                            Bitmap bitmap = BitmapFactory.decodeFile(data.getImages().get(imgCounr).getImgLink());
                            imageBitmapList.add(bitmap);
                            RequestOptions options = new RequestOptions()
                                    .placeholder(R.drawable.crp_trails_icon_splash)
                                    .error(R.drawable.crp_trails_icon_splash);
                            if (imgCounr == 0) {
                                Glide.with(getActivity()).load(data.getImages().get(imgCounr).getImgLink()).apply(options).into(timeline_form_img1);
                            } else if (imgCounr == 1) {
                                Glide.with(getActivity()).load(data.getImages().get(imgCounr).getImgLink()).apply(options).into(timeline_form_img2);
                            } else if (imgCounr == 2) {
                                Glide.with(getActivity()).load(data.getImages().get(imgCounr).getImgLink()).apply(options).into(timeline_form_img3);
                            } else if (imgCounr == 3) {
                                Glide.with(getActivity()).load(data.getImages().get(imgCounr).getImgLink()).apply(options).into(timeline_form_img4);
                            }

                        }
                        DataHandler.newInstance().setImageUriList(img_uri_array);
                        DataHandler.newInstance().setImageBitmapList(imageBitmapList);
                    }
                } else {
                    //No Image
                    images_linear_layout_parent.setVisibility(View.GONE);
                }
                images_linear_layout_parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onImageClick();
                    }
                });
            } else {
                images_linear_layout_parent.setVisibility(View.GONE);
            }

            if (AppConstants.isValidString(data.getTextReply())) {
                farmer_reply_tv.setText(data.getTextReply());
            } else {
                farmer_reply_tv.setText("N/A");
            }
            images_linear_layout_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onImageClick();
                }
            });


        }

        view_activity_progress_bar.setVisibility(View.GONE);

    }

}
