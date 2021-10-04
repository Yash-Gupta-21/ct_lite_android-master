package com.i9930.croptrails.SubmitActivityForm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.CommonClasses.TimelineUnits;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.FarmDetails.Model.Activity.DoneActivityImage;
import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineInnerData;
import com.i9930.croptrails.FarmNavigation.FarmNavigationActivity;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Maps.ShowArea.Model.GetGpsCoordinates;
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;
import com.i9930.croptrails.Maps.ShowArea.Model.geojson.GeoJsonResponse;
import com.i9930.croptrails.Maps.ShowArea.ShowAreaWithUpdateActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.DoneActivities.ActivityCacheManager;
import com.i9930.croptrails.RoomDatabase.DoneActivities.DoneActivity;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownCacheManager;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownFetchListener;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownCacheManager2;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownT2;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmCacheManager;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmLoadListener;
import com.i9930.croptrails.RoomDatabase.Timeline.CalendarJSON;
import com.i9930.croptrails.RoomDatabase.Timeline.Timeline;
import com.i9930.croptrails.RoomDatabase.Timeline.TimelineCacheManager;
import com.i9930.croptrails.RoomDatabase.Timeline.TimelineNotifyInterface;
import com.i9930.croptrails.SubmitActivityForm.Adapter.AgriInputAdapter;
import com.i9930.croptrails.SubmitActivityForm.Adapter.AgriInstructionAdapter;
import com.i9930.croptrails.SubmitActivityForm.Model.ActivityChemicalResponse;
import com.i9930.croptrails.SubmitActivityForm.Model.ActivityUnitResponse;
import com.i9930.croptrails.SubmitActivityForm.Model.AgriInput.AgriInput;
import com.i9930.croptrails.SubmitActivityForm.Model.AgriInput.AgriInputResponse;
import com.i9930.croptrails.SubmitActivityForm.Model.ChemicalDataList;
import com.i9930.croptrails.SubmitActivityForm.Model.CompAgriDatum;
import com.i9930.croptrails.SubmitActivityForm.Model.CompAgriResponse;
import com.i9930.croptrails.SubmitActivityForm.Model.DoneActNew.DoneActInputs;
import com.i9930.croptrails.SubmitActivityForm.Model.DoneActNew.DoneActivityResponseNew;
import com.i9930.croptrails.SubmitActivityForm.Model.SendTimelineActivityDataNew;
import com.i9930.croptrails.SubmitActivityForm.Model.TimelineChemicals;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;
import com.i9930.croptrails.Utility.Utility;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;
import static com.i9930.croptrails.Utility.AppConstants.getCoordinatesFromGeometry;
import static com.i9930.croptrails.Utility.AppConstants.isValidString;

public class TimelineFormActivity2 extends AppCompatActivity implements TimelineNotifyInterface, DropDownFetchListener, FarmLoadListener {
    Timeline timeline;
    List<TimelineInnerData> calendarData = new ArrayList<>();
    String TAG = "TimelineFormActivity2";
    int timelineIndex = 0;
    List<AgriInput> agriInputs = new ArrayList<>();
    AgriInputAdapter agriInputAdapter;
    List<CompAgriDatum> compAgriDatumList = new ArrayList<>();
    Location currentLocation;
    Toolbar mActionBarToolbar;
    Context context;
    TextView title;
    /*@BindView(R.id.done_button)
    Button done_button;*/
    @BindView(R.id.timeline_form_img1)
    ImageView timeline_form_img1;
    @BindView(R.id.timeline_form_img2)
    ImageView timeline_form_img2;
    @BindView(R.id.timeline_form_img3)
    ImageView timeline_form_img3;
    @BindView(R.id.timeline_form_img4)
    ImageView timeline_form_img4;


    @BindView(R.id.tv_lot_no_cal_act)
    TextView tv_lot_no_cal_act;
    @BindView(R.id.tv_farmer_name_cal_act)
    TextView tv_farmer_name_cal_act;
    @BindView(R.id.tv_activity_name_cal_act)
    TextView tv_activity_name_cal_act;
    @BindView(R.id.tv_date_cal_act)
    TextView tv_date_cal_act;
    @BindView(R.id.tv_desc_cal_act)
    TextView tv_desc_cal_act;
    @BindView(R.id.tv_chem_cal_act)
    TextView tv_chem_cal_act;
    @BindView(R.id.tv_chem_qty_cal_act)
    TextView tv_chem_qty_cal_act;

    @BindView(R.id.rounded_act_image)
    CircleImageView activity_type_image_view;
    @BindView(R.id.rounded_farm_image)
    CircleImageView farm_latest_image_view;

    @BindView(R.id.timeline_farmer_reply_et)
    EditText timeline_farmer_reply_et;
    List<Bitmap> imageList;
    String userChoosenTask = "";
    String pictureImagePath = "";
    Bitmap myBitmap;
    private int SELECT_FILE1 = 1, SELECT_FILE2 = 2, SELECT_FILE3 = 3, SELECT_FILE4 = 4;
    private int REQUEST_CAMERA1 = 11, REQUEST_CAMERA2 = 12, REQUEST_CAMERA3 = 13, REQUEST_CAMERA4 = 14;
    String image_capture_date1 = "", image_capture_date2 = "", image_capture_date3 = "", image_capture_date4 = "";

    boolean deleteClicked = false;

    List<TimelineChemicals> chemicalsList;
    List<TimelineUnits> timelineUnits;
    TimelineChemicals timelineChemicals;
    TimelineUnits timelineUnitsID;
    List<String> chemicalSpinnerList;
    List<View> viewList;
    List<ChemicalDataList> chemicalDataList;
    ChemicalDataList dataListItemFirst, dataListItem;
    @BindView(R.id.timeline_form_submit)
    Button timeline_form_submit;
    int flag = 0;
    // TimelineInnerData timelineInnerData;

    @BindView(R.id.whole_input_rel_lay)
    RelativeLayout whole_input_rel_lay;
    @BindView(R.id.timeline_form_most_parent_rel_lay)
    RelativeLayout timeline_form_most_parent_rel_lay;
    String[] image_array = new String[4];
    Snackbar snackbar;
    String farm_cal_activity_id;
    ProgressDialog progressDialog;
    TimelineInnerData timelineInnerData;
    ProgressDialog fetchProgress;
    ViewFailDialog viewFailDialog;
    Resources resources;
    List<String> picturePathList = new ArrayList<>();
    int offlinePosition = 0;
    String message = "";
    String message2 = "";
    String message3 = "";
    String farmLatitude = "";
    String farmLongitude = "";
    List<LatLng> latLngList = new ArrayList<>();

    List<String> imageLatitude = new ArrayList<>();
    List<String> imageLongitude = new ArrayList<>();

    @BindView(R.id.connectivityLine)
    View connectivityLine;
    @BindView(R.id.agriInputInstructioRecyclerData)
    RecyclerView agriInputInstructioRecyclerData;

    @BindView(R.id.agriInputInstructioRecycler)
    RecyclerView agriInputInstructioRecycler;

    @BindView(R.id.addAgriInput)
    ImageView addAgriInput;
    @BindView(R.id.addAgriInput2)
    ImageView addAgriInput2;
    @BindView(R.id.agriInptRel)
    LinearLayout agriInptRel;
    @BindView(R.id.agriInputRel)
    LinearLayout agriInputRel;

    AdView mAdView;
    private void loadAds(){
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
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
//                Log.e(TAG,"Add Errro "+adError.toString());
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
    protected void onCreate(Bundle savedInstanceState) {
        String languageToLoad = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        setContentView(R.layout.activity_timeline_form2);

        context = this;
        loadAds();
        ButterKnife.bind(this);
//        timelineIndex=getIntent().getIntExtra("position", 0);
//        Toast.makeText(context, "index "+timelineIndex, Toast.LENGTH_SHORT).show();
        agriInputAdapter = new AgriInputAdapter(context, agriInputs);
        agriInputInstructioRecyclerData.setHasFixedSize(true);
        agriInputInstructioRecyclerData.setLayoutManager(new LinearLayoutManager(context));
        agriInputInstructioRecyclerData.setNestedScrollingEnabled(false);
        agriInputInstructioRecyclerData.setAdapter(agriInputAdapter);
        imageLatitude.add("");
        imageLatitude.add("");
        imageLatitude.add("");
        imageLatitude.add("");

        imageLongitude.add("");
        imageLongitude.add("");
        imageLongitude.add("");
        imageLongitude.add("");
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        viewFailDialog = new ViewFailDialog();
        progressDialog = new ProgressDialog(context);
        fetchProgress = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(resources.getString(R.string.please_wait_msg));
        fetchProgress.setTitle(resources.getString(R.string.please_wait_msg));
        title = (TextView) findViewById(R.id.tittle);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        viewList = new ArrayList<>();
//        viewList.add(linearLayout.getRootView());
        chemicalDataList = new ArrayList<>();
        chemicalsList = new ArrayList<>();
        TimelineChemicals chemical = new TimelineChemicals();
        chemical.setChemical(resources.getString(R.string.select_dropdown));
        chemical.setChemicalId("0");
        chemicalsList.add(chemical);

        //Unit Data
        timelineUnits = new ArrayList<>();
        TimelineUnits units = new TimelineUnits();
        units.setUnit(resources.getString(R.string.unit_dropdown));
        units.setUnit_id("0");
        timelineUnits.add(units);
        farmLatitude = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARM_LAT);
        farmLongitude = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARM_LONG);
        timelineInnerData = DataHandler.newInstance().getTimelineInnerData();
        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompAgriInputs();
                } else {
                    try {
                        CompAgriResponse compAgriResponse = new Gson().fromJson(dropDown.getData(), CompAgriResponse.class);
                        if (compAgriResponse != null && compAgriResponse.getData() != null && compAgriResponse.getData().size() > 0) {
//                            agriInputAdapter.setCompAgriDatumList(response.body().getData());
                            compAgriDatumList.addAll(compAgriResponse.getData());
                            addAgriInput2.setVisibility(View.VISIBLE);
                            agriInputRel.setVisibility(View.VISIBLE);
                        } else {
                            addAgriInput2.setVisibility(View.GONE);
                            addAgriInput.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }, AppConstants.CHEMICAL_UNIT.COMP_AGRI_INPUTS);
        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            //Offline Mode
            DropDownCacheManager.getInstance(context).getAllChemicalUnits(this::onChemicalUnitLoaded);
            FarmCacheManager.getInstance(context).getFarm(this::onFarmLoader, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
            TimelineCacheManager.getInstance(context).getAllTimeline(this, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
            offlinePosition = getIntent().getIntExtra("position", 0);
            if ((timelineInnerData != null && timelineInnerData.getAgriInput() != null && timelineInnerData.getAgriInput().size() > 0) || (compAgriDatumList != null && compAgriDatumList.size() > 0)) {
                agriInputRel.setVisibility(View.VISIBLE);
            } else {
                agriInputRel.setVisibility(View.GONE);
            }
            if ((timelineInnerData != null && timelineInnerData.getInstructionList() != null && timelineInnerData.getInstructionList().size() > 0)) {
                agriInptRel.setVisibility(View.VISIBLE);
                AgriInstructionAdapter agriInstructionAdapter;
                agriInstructionAdapter = new AgriInstructionAdapter(context, timelineInnerData.getInstructionList());

                agriInputInstructioRecycler.setHasFixedSize(true);
                agriInputInstructioRecycler.setLayoutManager(new LinearLayoutManager(context));
                agriInputInstructioRecycler.setNestedScrollingEnabled(false);
                agriInputInstructioRecycler.setAdapter(agriInstructionAdapter);
            } else {
                agriInptRel.setVisibility(View.GONE);
            }

        } else {
            //Online Mode
            farm_cal_activity_id = timelineInnerData.getFarmCalActivityId();
            new TimelineFormActivity2.GetGeoJsonData().execute();
            FetchFarmResult fetchFarmResult = DataHandler.newInstance().getFetchFarmResult();
            if (fetchFarmResult != null) {
                tv_lot_no_cal_act.setText(fetchFarmResult.getLotNo());
                tv_farmer_name_cal_act.setText(fetchFarmResult.getName());
            } else {
                tv_lot_no_cal_act.setText("N/A");
                tv_farmer_name_cal_act.setText("N/A");
            }
            getData(farm_cal_activity_id);
        }


        tv_activity_name_cal_act.setText(timelineInnerData.getActivity());
        tv_date_cal_act.setText(timelineInnerData.getDate());
        tv_desc_cal_act.setText(timelineInnerData.getDescription());
        if (timelineInnerData.getChemical() != null && !TextUtils.isEmpty(timelineInnerData.getChemical())) {
            tv_chem_cal_act.setText(timelineInnerData.getChemical());
            tv_chem_qty_cal_act.setText(timelineInnerData.getChemicalQty());
        } else {
            tv_chem_cal_act.setText("N/A");
            tv_chem_qty_cal_act.setText("0");
        }
        onClicks();
        imageList = new ArrayList<>();
        chemicalSpinnerList = new ArrayList<>();
        chemicalSpinnerList.add(resources.getString(R.string.select_chemical_label));

        title.setText(resources.getString(R.string.fill_date_msg));
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACT_LATEST_IMG) != null &&
                !SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACT_LATEST_IMG).equals("0")) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.crp_trails_icon_splash)
                    .error(R.drawable.crp_trails_icon_splash);
            Glide.with(context).load(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACT_LATEST_IMG)).apply(options).into(farm_latest_image_view);
        } else {
            farm_latest_image_view.setImageResource(R.drawable.ploughed_farm);
        }

        //Setting  FArm Image
        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACT_TYPE_IMG) != null &&
                !SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACT_TYPE_IMG).equals("0")) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.crp_trails_icon_splash)
                    .error(R.drawable.crp_trails_icon_splash);
            Glide.with(context).load(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACT_TYPE_IMG)).apply(options).into(activity_type_image_view);
        } else {
            activity_type_image_view.setImageResource(R.drawable.ploughed_farm);
        }
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                currentLocation = location;
                                String url = location.getLatitude() + "," + location.getLongitude();
                                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                try {
                                    List<Address> list = geocoder.getFromLocation(
                                            location.getLatitude(), location.getLongitude(), 1);
                                    if (list != null && list.size() > 0) {
                                        Address address = list.get(0);
                                    }
                                } catch (IOException e) {

                                }
                            }
                        }
                    });
        }
    }

    private void onClicks() {
        addAgriInput.setVisibility(View.GONE);
        addAgriInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (compAgriDatumList != null && compAgriDatumList.size() > 0)
                    showManualInputDialog();
            }
        });

        addAgriInput2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (compAgriDatumList != null && compAgriDatumList.size() > 0) {
                    AgriInput agriInput = new AgriInput("0", "N/A", null, "N/A", "", "", "", "");
                    agriInput.setManualAdded(true);
                    agriInput.setCompAgriDatumList(compAgriDatumList);
                    agriInputs.add(agriInput);
                    agriInputAdapter.notifyDataSetChanged();
                }
            }
        });

        timeline_form_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage("capture1");
            }
        });

        timeline_form_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageList.size() >= 1) {
                    selectImage("capture2");
                } else {
                    viewFailDialog.showDialog(TimelineFormActivity2.this, resources.getString(R.string.opps_message), resources.getString(R.string.capure_1_image_msg));

                }
            }
        });

        timeline_form_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageList.size() >= 2)
                    selectImage("capture3");
                else {
                    viewFailDialog.showDialog(TimelineFormActivity2.this, resources.getString(R.string.opps_message), resources.getString(R.string.capure_2_image_msg));

                }
            }
        });

        timeline_form_img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageList.size() >= 3)
                    selectImage("capture4");
                else {
                    viewFailDialog.showDialog(TimelineFormActivity2.this, resources.getString(R.string.opps_message), resources.getString(R.string.capure_3_image_msg));

                }
            }
        });
        /*timeline_form_chemicals_spinner.setSelection(0);
        timeline_qty_unit_spinner.setSelection(0);*/

        dataListItemFirst = new ChemicalDataList();

        //Submit all data
        timeline_form_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeline_farmer_reply_et.getText().toString().trim().equals("")) {
                    timeline_farmer_reply_et.setError(resources.getString(R.string.farmer_reply_must_entered_msg));
                    Toasty.error(context, "Please enter farmer reply", Toast.LENGTH_LONG, false).show();
                } else {

                    if (imageList.size() < 4) {
                        snackbar = Snackbar
                                .make(timeline_form_most_parent_rel_lay, resources.getString(R.string.capture_all_image_msg), Snackbar.LENGTH_INDEFINITE)
                                .setAction(resources.getString(R.string.ok_msg), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        snackbar.dismiss();
                                    }
                                });
                        snackbar.show();
                    } else {
                        progressDialog.show();
                        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                            saveOfflineActivity();
                        } else {
                            timeline_form_submit.setClickable(false);
                            timeline_form_submit.setEnabled(false);
                            uploadActivityData();
                        }
                    }

                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public boolean checkStoragePermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(getString(R.string.permission_necessary));
                    alertBuilder.setMessage(getString(R.string.external_storage_permission_is_necessary));
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                    return false;
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            SELECT_FILE1);

                    return false;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean checkCameraPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(getString(R.string.permission_necessary));
                    alertBuilder.setMessage("Camera permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                    return false;
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, SELECT_FILE2
                    );

                    return false;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void selectImage(final String onclick) {
        final CharSequence[] items = {resources.getString(R.string.image_take_photo),
                resources.getString(R.string.image_choose_from_library),
                resources.getString(R.string.image_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(TimelineFormActivity2.this);
        builder.setTitle(resources.getString(R.string.image_select_image_title));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(TimelineFormActivity2.this);
                boolean resultCam = checkCameraPermission(context);
                boolean resultGal = checkStoragePermission(context);
                if (items[item].equals(resources.getString(R.string.image_take_photo))) {
                    userChoosenTask = "Take Photo";
                    if (resultCam)
                        cameraIntent(onclick);

                } else if (items[item].equals(resources.getString(R.string.image_choose_from_library))) {
                    userChoosenTask = "Choose from Library";
                    if (resultGal)
                        galleryIntent(onclick);

                } else if (items[item].equals(resources.getString(R.string.image_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent(String onclick) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        if (onclick.equals("capture1")) {
            startActivityForResult(intent, REQUEST_CAMERA1);
        } else if (onclick.equals("capture2")) {
            startActivityForResult(intent, REQUEST_CAMERA2);
        } else if (onclick.equals("capture3")) {
            startActivityForResult(intent, REQUEST_CAMERA3);
        } else if (onclick.equals("capture4")) {
            startActivityForResult(intent, REQUEST_CAMERA4);
        }
    }

    private void galleryIntent(String onclick) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(android.provider.MediaStore.EXTRA_SIZE_LIMIT, "720000");
        intent.setType("image/*");
        //intent.putExtra("result",onclick);
        if (onclick.equals("capture1")) {
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file_msg)), SELECT_FILE1);
        } else if (onclick.equals("capture2")) {
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file_msg)), SELECT_FILE2);
        } else if (onclick.equals("capture3")) {
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file_msg)), SELECT_FILE3);
        } else if (onclick.equals("capture4")) {
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file_msg)), SELECT_FILE4);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {

            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE1) {
                    String result = "capture1";
                    //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    onSelectFromGalleryResult(data, result);
                } else if (requestCode == SELECT_FILE2) {
                    String result = "capture2";
                    // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                    onSelectFromGalleryResult(data, result);
                } else if (requestCode == SELECT_FILE3) {
                    String result = "capture3";
                    // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                    onSelectFromGalleryResult(data, result);
                } else if (requestCode == SELECT_FILE4) {
                    String result = "capture4";
                    // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                    onSelectFromGalleryResult(data, result);
                } else if (requestCode == REQUEST_CAMERA1) {
                    String result = "capture1";
                    // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                    onCaptureImageResult(data, result);
                } else if (requestCode == REQUEST_CAMERA2) {
                    String result = "capture2";
                    // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                    onCaptureImageResult(data, result);
                } else if (requestCode == REQUEST_CAMERA3) {
                    String result = "capture3";
                    // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                    onCaptureImageResult(data, result);
                } else if (requestCode == REQUEST_CAMERA4) {
                    String result = "capture4";
                    // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                    onCaptureImageResult(data, result);


                }

                    /*Place place = PlaceAutocomplete.getPlace(this, data);
                    Toast.makeText(this, "place "+place.toString(),
                            Toast.LENGTH_LONG).show();*/

            }
        } else {
            Log.e("MYLOCATIONTAG", "Main Result not ok");
            //Toast.makeText(context, "Result Not ok", Toast.LENGTH_SHORT).show();
        }


    }

    private void onCaptureImageResult(Intent data, String onclick) {
//        Bundle extras = data.getExtras();
        //String onclick=extras.getString("result");
        File imgFile = new File(pictureImagePath);
        if (imgFile.exists()) {
            if (onclick.equals("capture1")) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = getResizedBitmap(myBitmap, 720, 1080);
//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
                if (ReadExif(imgFile.getAbsolutePath(), "capture1", 0, true)) {
                    myBitmap = addWaterMark(myBitmap);

                    timeline_form_img1.setImageBitmap(myBitmap);
                    if (imageList.size() > 0) {
                        imageList.remove(0);
                        imageList.add(0, compressBitmap(myBitmap, 2, 50, 0));
                        picturePathList.remove(0);
                        picturePathList.add(0, pictureImagePath);
                    } else {
                        imageList.add(0, compressBitmap(myBitmap, 2, 50, 0));
                        picturePathList.add(0, pictureImagePath);
                    }
                } else {
                    //image1.setImageBitmap(null);
                }
                //Log.e("Exif",ReadExif(imgFile.getAbsolutePath()));
            } else if (onclick.equals("capture2")) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = getResizedBitmap(myBitmap, 720, 1080);
//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
                if (ReadExif(imgFile.getAbsolutePath(), "capture2", 1, true)) {
                    myBitmap = addWaterMark(myBitmap);

                    timeline_form_img2.setImageBitmap(myBitmap);
                    if (imageList.size() > 1) {
                        imageList.remove(1);
                        imageList.add(1, compressBitmap(myBitmap, 2, 50, 1));
                        picturePathList.remove(1);
                        picturePathList.add(1, pictureImagePath);
                    } else {
                        imageList.add(1, compressBitmap(myBitmap, 2, 50, 1));
                        picturePathList.add(1, pictureImagePath);
                    }
                }
            } else if (onclick.equals("capture3")) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = getResizedBitmap(myBitmap, 720, 1080);
//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
                if (ReadExif(imgFile.getAbsolutePath(), "capture3", 2, true)) {
                    myBitmap = addWaterMark(myBitmap);

                    timeline_form_img3.setImageBitmap(myBitmap);
                    if (imageList.size() > 2) {
                        imageList.remove(2);
                        imageList.add(2, compressBitmap(myBitmap, 2, 50, 2));
                        picturePathList.remove(2);
                        picturePathList.add(2, pictureImagePath);
                    } else {
                        imageList.add(2, compressBitmap(myBitmap, 2, 50, 2));
                        picturePathList.add(2, pictureImagePath);
                    }
                }
            } else if (onclick.equals("capture4")) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = getResizedBitmap(myBitmap, 720, 1080);
//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
                if (ReadExif(imgFile.getAbsolutePath(), "capture4", 3, true)) {
                    myBitmap = addWaterMark(myBitmap);

                    timeline_form_img4.setImageBitmap(myBitmap);
                    if (imageList.size() > 3) {
                        imageList.remove(3);
                        imageList.add(3, compressBitmap(myBitmap, 2, 50, 3));
                        picturePathList.remove(3);
                        picturePathList.add(3, pictureImagePath);
                    } else {
                        imageList.add(3, compressBitmap(myBitmap, 2, 50, 3));
                        picturePathList.add(3, pictureImagePath);
                    }
                }


            }
        }
    }

    private void onSelectFromGalleryResult(Intent data, String onclick) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        Log.e(TAG, "Selected Pic Path " + picturePath);
        cursor.close();
        if (onclick.equals("capture1")) {
            myBitmap = BitmapFactory.decodeFile(picturePath);
            myBitmap = getResizedBitmap(myBitmap, 720, 1080);
//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
            if (ReadExif(picturePath, "capture1", 0, false)) {
                myBitmap = addWaterMark(myBitmap);
                timeline_form_img1.setImageBitmap(myBitmap);
                if (imageList.size() > 0) {
                    imageList.remove(0);
                    imageList.add(0, compressBitmap(myBitmap, 2, 50, 0));
                    picturePathList.remove(0);
                    picturePathList.add(0, picturePath);
                } else {
                    imageList.add(0, compressBitmap(myBitmap, 2, 50, 0));
                    picturePathList.add(0, picturePath);
                }
            } else {
                myBitmap = null;
                //image1.setImageBitmap(null);
            }
        } else if (onclick.equals("capture2")) {
            myBitmap = BitmapFactory.decodeFile(picturePath);
            myBitmap = getResizedBitmap(myBitmap, 720, 1080);
//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
            if (ReadExif(picturePath, "capture2", 1, false)) {
                myBitmap = addWaterMark(myBitmap);

                timeline_form_img2.setImageBitmap(myBitmap);
                if (imageList.size() > 1) {
                    imageList.remove(1);
                    imageList.add(1, compressBitmap(myBitmap, 2, 50, 1));
                    picturePathList.remove(1);
                    picturePathList.add(1, picturePath);
                } else {
                    imageList.add(1, compressBitmap(myBitmap, 2, 50, 1));
                    picturePathList.add(1, picturePath);
                }
            } else {
                myBitmap = null;
                //image1.setImageBitmap(null);
            }
        } else if (onclick.equals("capture3")) {
            myBitmap = BitmapFactory.decodeFile(picturePath);
            myBitmap = getResizedBitmap(myBitmap, 720, 1080);
//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
            if (ReadExif(picturePath, "capture3", 2, false)) {
                myBitmap = addWaterMark(myBitmap);

                timeline_form_img3.setImageBitmap(myBitmap);
                if (imageList.size() > 2) {
                    imageList.remove(2);
                    imageList.add(2, compressBitmap(myBitmap, 2, 50, 2));
                    picturePathList.remove(2);
                    picturePathList.add(2, picturePath);
                } else {
                    imageList.add(2, compressBitmap(myBitmap, 2, 50, 2));
                    picturePathList.add(2, picturePath);
                }
            } else {
                myBitmap = null;
                //image1.setImageBitmap(null);
            }

        } else if (onclick.equals("capture4")) {
            myBitmap = BitmapFactory.decodeFile(picturePath);
            myBitmap = getResizedBitmap(myBitmap, 720, 1080);
//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
            if (ReadExif(picturePath, "capture4", 3, false)) {
                myBitmap = addWaterMark(myBitmap);
                timeline_form_img4.setImageBitmap(myBitmap);
                if (imageList.size() > 3) {
                    imageList.remove(3);
                    imageList.add(3, compressBitmap(myBitmap, 2, 50, 3));
                    picturePathList.remove(3);
                    picturePathList.add(3, picturePath);
                } else {
                    imageList.add(3, compressBitmap(myBitmap, 2, 50, 3));
                    picturePathList.add(3, picturePath);
                }
            } else {
                myBitmap = null;
                //image1.setImageBitmap(null);
            }

        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();


        float scaleWidth = ((float) (2.0 / 3.0));
        float scaleHeight = ((float) (2.0 / 3.0));
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


    Boolean ReadExif(String file, String captureNo, int index, boolean flag) {

        message = "";
        message2 = "";
        message3 = "";
        imgLat = "0";
        imgLong = "0";
        Boolean time_stamp = false;
        try {
            ExifInterface exifInterface = new ExifInterface(file);
            if (exifInterface.getAttribute(ExifInterface.TAG_DATETIME) != null) {
                float[] latLong = new float[2];
                boolean hasLatLong = exifInterface.getLatLong(latLong);
                String imgLat = "";
                String imgLong = "";
                if (hasLatLong) {
                    System.out.println("SubmitPldActivity Latitude: " + latLong[0]);
                    System.out.println("SubmitPldActivity Longitude: " + latLong[1]);
                    if (latLong.length > 1) {
                        imgLat = "" + latLong[0];
                        imgLong = "" + latLong[0];
                    }
                }
                if (imgLat != null && imgLong != null && !TextUtils.isEmpty(imgLat) && !TextUtils.isEmpty(imgLong)) {

                    imageLatitude.set(index, imgLat);
                    imageLongitude.add(index, imgLong);
                    this.imgLat = imgLat;
                    this.imgLong = imgLong;
//                    if (farmLatitude != null && farmLongitude != null && !TextUtils.isEmpty(farmLongitude) && !TextUtils.isEmpty(farmLatitude)) {
                    LatLng latLng = new LatLng(Double.valueOf(imgLat), Double.valueOf(imgLong));
                    if (latLngList != null && latLngList.size() > 2 && AppConstants.isPointInPolygon(latLng, latLngList)) {
                        message = "";
                        message2 = "";
                    } else {
                        message = "Image might not be from farm";
                        message2 = "Gps coordinates mismatch";
                    }

                    /*} else {
                        message = "Image might not be from farm";
                        message2 = "Farm coordinates missing";
                    }*/

                } else if (flag && currentLocation != null) {
                    this.imgLat = "" + currentLocation.getLatitude();
                    this.imgLong = "" + currentLocation.getLongitude();
                    imageLatitude.set(index, "" + currentLocation.getLatitude());
                    imageLongitude.set(index, "" + currentLocation.getLongitude());

                    if (farmLatitude != null && farmLongitude != null && !TextUtils.isEmpty(farmLongitude) && !TextUtils.isEmpty(farmLatitude)) {

                        LatLng latLng = new LatLng(Double.valueOf(currentLocation.getLatitude()), Double.valueOf(currentLocation.getLongitude()));
                        if (latLngList != null && latLngList.size() > 2 && AppConstants.isPointInPolygon(latLng, latLngList)) {
                            message = "";
                            message2 = "";
                        } else {
                            message = "Image might not be from farm";
                            message2 = "Gps coordinates mismatch";
                        }
                    } else {
                        message = "Image might not be from farm";
                        message2 = "Farm coordinates missing";
                    }

                } else {
                    imageLatitude.set(index, "0");
                    imageLongitude.set(index, "0");
                    message = "Image might not be from farm";
                    message2 = "Image coordinates missing";
                }
                time_stamp = true;
                Date final_date = parseDateToddMMyyyy(exifInterface.getAttribute(ExifInterface.TAG_DATETIME), captureNo);
                Date c = Calendar.getInstance().getTime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(c);
                calendar.add(Calendar.DAY_OF_YEAR, -3);
                Date newDate = calendar.getTime();
                if (newDate.after(final_date)) {
                    time_stamp = true;
                    message3 = "Image is old";
                    Log.e(TAG, "BOTH DATES " + "     " + final_date.toString() + "    " + newDate.toString());

                } else {
                    message3 = "";
                    Log.e(TAG, "BOTH DATES " + "     " + final_date.toString() + "    " + newDate.toString());
                }

            } else {
                message3 = "Image date not available";
                time_stamp = true;
                Log.e(TAG, "outer else exif");
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(context,
                    e.toString(),
                    Toast.LENGTH_LONG).show();
        }
        return time_stamp;
    }

    private void getGeoJson(JsonObject jsonObject, String type) {

        try {
            String json = new Gson().toJson(jsonObject);
            JSONObject jsonObjectGeo = new JSONObject(json);
            GeoJsonLayer layer = new GeoJsonLayer(null, jsonObjectGeo);
            for (GeoJsonFeature feature : layer.getFeatures()) {
                if (feature != null) {
                    if (type != null && type.equals(AppConstants.AREA_TYPE.Farm)) {
                        if (feature.hasGeometry()) {
                            Geometry geometry = feature.getGeometry();
                            latLngList.addAll(getCoordinatesFromGeometry(geometry));
                        }
                    }
                }
            }

            layer.addLayerToMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GetGeoJsonData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            //demosupervisor1
            try {
                String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("user_id", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                jsonObject.addProperty("token", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                jsonObject.addProperty("comp_id", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                jsonObject.addProperty("farm_id", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                jsonObject.addProperty("type", "" + AppConstants.AREA_TYPE.Farm);
//                jsonObject.addProperty("farm_id","20202");
                Call<GeoJsonResponse> statusMsgModelCall = apiService.getFarmGeoJson(jsonObject);
                Log.e(TAG, "Sending Data " + new Gson().toJson(jsonObject));
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<GeoJsonResponse>() {
                    @Override
                    public void onResponse(Call<GeoJsonResponse> call, retrofit2.Response<GeoJsonResponse> response) {
                        String error = null;
                        isLoaded[0] = true;
                        if (response.isSuccessful()) {
                            Log.e("Area Response", new Gson().toJson(response.body()));
                            GeoJsonResponse statusMsgModel = response.body();
                            if (statusMsgModel.getStatus() == 1) {
                                if (statusMsgModel.getData() != null && statusMsgModel.getData().size() > 0) {
                                    for (int i = 0; i < statusMsgModel.getData().size(); i++) {
                                        String type = statusMsgModel.getData().get(i).getType();
                                        if (type != null && type.equals(AppConstants.AREA_TYPE.Farm)) {
                                            getGeoJson(statusMsgModel.getData().get(i).getDetails(), type);
                                            break;
                                        }
                                    }
                                }
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {

                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired

                            } else {

                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access

                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired

                        } else {
                            try {
                                error = response.errorBody().string().toString();

                                Log.e(TAG, "Error " + error);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<GeoJsonResponse> call, Throwable t) {
                        Log.e(TAG, "Failure " + t.toString());

                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String s) {


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    /*private class FetchGpsCordinates extends AsyncTask<String, Void, String> {

        public FetchGpsCordinates() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {
            latLngList.clear();
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
            Call<GetGpsCoordinates> getGpsCoordinatesCall = apiService.getGpsCoordinates(
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID), userId, token);
            getGpsCoordinatesCall.enqueue(new Callback<GetGpsCoordinates>() {

                @Override
                public void onResponse(Call<GetGpsCoordinates> call, Response<GetGpsCoordinates> response) {
                    if (response.isSuccessful()) {
                        GetGpsCoordinates getGpsCoordinates = response.body();
                        Log.e(TAG, "Farm corrds list " + new Gson().toJson(getGpsCoordinates));
                        if (getGpsCoordinates.getStatus() == 10) {

                            viewFailDialog.showSessionExpireDialog(TimelineFormActivity2.this, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
//                            ViewFailDialog viewFailDialog = new ViewFailDialog();
//                            viewFailDialog.showSessionExpireDialog(TimelineAddVisitActivity3.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
//                            ViewFailDialog viewFailDialog = new ViewFailDialog();
//                            viewFailDialog.showSessionExpireDialog(TimelineAddVisitActivity3.this, resources.getString(R.string.authorization_expired));
                        } else if (getGpsCoordinates.getStatus() != 0) {
                            List<LatLngFarm> latLngs = getGpsCoordinates.getData();

                            if (latLngs != null) {
                                for (int i = 0; i < latLngs.size(); i++) {
                                    latLngList.add(new LatLng(Double.valueOf(latLngs.get(i).getLat()), Double.valueOf(latLngs.get(i).getLng())));
                                }
                            }
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(TimelineAddVisitActivity3.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(TimelineAddVisitActivity3.this, resources.getString(R.string.authorization_expired));
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<GetGpsCoordinates> call, Throwable t) {
                    //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, t.toString());


                }

            });


            return null;
        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }*/

    public Date parseDateToddMMyyyy(String time, String capture_no) {
        String inputPattern = "yyyy:MM:dd HH:mm:ss";
        String outputPattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            Log.e("Today's date in middle", date.toString());
            str = outputFormat.format(date);
            Log.e("Checkdate", str);

            if (capture_no.equals("capture1")) {
                image_capture_date1 = str;
            } else if (capture_no.equals("capture2")) {
                image_capture_date2 = str;
            } else if (capture_no.equals("capture3")) {
                image_capture_date3 = str;
            } else if (capture_no.equals("capture4")) {
                image_capture_date4 = str;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private void saveOfflineActivity() {
        String farrmer_reply;
        Date c;
        String todayDateStr;
        SimpleDateFormat df;
        if (TextUtils.isEmpty(timeline_farmer_reply_et.getText().toString()))
            farrmer_reply = "0";
        else
            farrmer_reply = timeline_farmer_reply_et.getText().toString().trim();
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateStr = dateFormatter.format(date);
        List<AgriInput> agriInputList = new ArrayList<>();
        List<DoneActInputs> doneActInputs = new ArrayList<>();
        List<DoneActivityImage> doneActivityImageList = new ArrayList<>();
        for (int i = 0; i < agriInputs.size(); i++) {
            AgriInput chemicals = agriInputs.get(i);
            if (AppConstants.isValidString(chemicals.getAgriId())) {
                agriInputList.add(chemicals);
                DoneActInputs doneAct = new DoneActInputs();
                if (timelineInnerData != null) {
                    doneAct.setActivity(timelineInnerData.getActivity());
                    doneAct.setActivityDoneDate(timelineInnerData.getDate());
                    doneAct.setDescription(timelineInnerData.getDescription());
                    doneAct.setDate(timelineInnerData.getDate());
                    doneAct.setFarmCalActivityId(timelineInnerData.getFarmCalActivityId());
                    doneAct.setFarmCalendarActivityId(timelineInnerData.getFarmCalActivityId());
                    doneAct.setType("A");
                }
                doneAct.setName(chemicals.getName());
                doneAct.setAgriId(chemicals.getAgriId());
                doneAct.setAmount(chemicals.getCost());
                doneAct.setQuantity(chemicals.getUsedQuantity());
                doneActInputs.add(doneAct);
            }
        }

        for (int i = 0; i < picturePathList.size(); i++) {
            DoneActivityImage image = new DoneActivityImage();
            image.setImgLink(picturePathList.get(i));
            try {
                image.setLat(imageLatitude.get(i));
                image.setLon(imageLongitude.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
            doneActivityImageList.add(image);
        }
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String doa = sdf.format(cal.getTime());
        DoneActivityResponseNew data = new DoneActivityResponseNew();
        data.setAgriInputs(agriInputList);
        data.setData(doneActInputs);
        data.setImages(doneActivityImageList);
        data.setDoneActivityImageList(doneActivityImageList);
        data.setTextReply(farrmer_reply);
        data.setActivityDoneDate(dateStr);
        data.setDoa(doa);
        data.setFarmId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
        Log.d(TAG, "Doa DATE : " + doa);
        String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        DoneActivity activity = new DoneActivity(farm_cal_activity_id, farmId, new Gson().toJson(data), "Y", doa, "N");

        Log.d(TAG, "Offline Store Data : " + new Gson().toJson(activity));
        ActivityCacheManager.getInstance(context).addActivity(activity);
        if (calendarData != null) {
            for (int i = 0; i < calendarData.size(); i++) {
                if (isValidString(calendarData.get(i).getFarmCalActivityId()) && isValidString(farm_cal_activity_id) && calendarData.get(i).getFarmCalActivityId().equals(farm_cal_activity_id)) {
                    calendarData.get(i).setIsDone("Y");
                    break;
                }
            }
        }

        CalendarJSON calendarJSON = new CalendarJSON();
        calendarJSON.setCalendarData(calendarData);
        timeline.setJsondata(new Gson().toJson(calendarJSON));
        TimelineCacheManager.getInstance(context).update(timeline);
        progressDialog.cancel();
        Toasty.success(context, resources.getString(R.string.activity_added_success_msg), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(context, LandingActivity.class);
        ActivityOptions options = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
            startActivity(intent, options.toBundle());
            finish();
        } else {
            finishAffinity();
            startActivity(intent);
            finish();
        }
    }

    public Bitmap compressBitmap(Bitmap selectedBitmap, int sampleSize, int quality, int index) {
        try {
            ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStreamObject);
            byteArrayOutputStreamObject.close();
            long lengthInKb = byteArrayOutputStreamObject.toByteArray().length / 1024; //in kb

            Log.e(TAG, "Image size at " + index + " " + lengthInKb + "Kb");
            if (lengthInKb > 350) {
                compressBitmap(selectedBitmap, (sampleSize * 2), (quality / 2), index);
            } else {
                image_array[index] = Base64.encodeToString(byteArrayOutputStreamObject.toByteArray(), Base64.DEFAULT);
                ;
                return selectedBitmap;
            }

            //selectedBitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectedBitmap;
    }

    String internetSPeed = "";

    private void internetFlow(boolean isLoaded) {
        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLoaded) {
                        if (!ConnectivityUtils.isConnected(context)) {
//                            AppConstants.failToast(context, resources.getString(R.string.check_internet_connection_msg));
                        } else {
                            ConnectivityUtils.checkSpeedWithColor(TimelineFormActivity2.this, new ConnectivityUtils.ColorListener() {
                                @Override
                                public void onColorChanged(int color, String speed) {
                                    if (color == android.R.color.holo_green_dark) {
                                        connectivityLine.setVisibility(View.GONE);
//                                    connectivityLine.setBackgroundColor(getColor(R.color.blue));
                                    } else {
                                        connectivityLine.setVisibility(View.VISIBLE);
                                        connectivityLine.setBackgroundColor(getResources().getColor(color));
                                    }
                                    internetSPeed = speed;
                                }
                            }, 20);
                        }
                    }
                }
            }, AppConstants.DELAY);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void uploadActivityData() {
        String farrmer_reply;
        String isDone;
        List<AgriInput> agriInputList = new ArrayList<>();
        SendTimelineActivityDataNew activityData = new SendTimelineActivityDataNew();
        activityData.setIs_done("Y");
        isDone = "Y";
        timeline_form_submit.setClickable(true);
        timeline_form_submit.setEnabled(true);

        if (TextUtils.isEmpty(timeline_farmer_reply_et.getText().toString()))
            farrmer_reply = "0";
        else
            farrmer_reply = timeline_farmer_reply_et.getText().toString().trim();

        for (int i = 0; i < agriInputs.size(); i++) {
            AgriInput data = agriInputs.get(i);
            Log.e(TAG, "Sending Activity " + i + " " + new Gson().toJson(data));
            AgriInput input = new AgriInput();
            input.setName(data.getName());
            input.setUsedQuantity(data.getUsedQuantity());
            input.setCost(data.getCost());
            input.setInputId(data.getAgriId());
            agriInputList.add(input);
        }
        activityData.setAgri_input_data(agriInputList);
        activityData.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
        activityData.setFarmer_reply(farrmer_reply);
        activityData.setIs_done(isDone);
        activityData.setFarm_cal_activity_id(farm_cal_activity_id);
        activityData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        activityData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        activityData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        Log.e(TAG, "Sending Activity Data " + new Gson().toJson(activityData));
        activityData.setImageList(image_array);//image_array
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        Call<StatusMsgModel> sendActivity = apiInterface.setTimelineActivityNew(activityData);
        sendActivity.enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                String error = null;
                isLoaded[0] = true;
                Log.e(TAG, "TimelineForm response code" + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG, "TimelineForm response" + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 10) {
                        timeline_form_submit.setClickable(true);
                        timeline_form_submit.setEnabled(true);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(TimelineFormActivity2.this, resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(TimelineFormActivity2.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(TimelineFormActivity2.this, resources.getString(R.string.authorization_expired));
                    } else if (response.body().getStatus() == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();

                            }
                        });
                        Intent intent = new Intent(context, LandingActivity.class);
                        ActivityOptions options = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                            startActivity(intent, options.toBundle());
                            finish();
                        } else {
                            finishAffinity();
                            startActivity(intent);
                            finish();
                        }

                        // Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_LONG).show();
                        Toasty.success(context, resources.getString(R.string.activity_added_success_msg), Toast.LENGTH_LONG).show();


                    } else {
                        timeline_form_submit.setClickable(true);
                        timeline_form_submit.setEnabled(true);
                        progressDialog.dismiss();
                        viewFailDialog.showDialog(TimelineFormActivity2.this, response.body().getMsg());
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    timeline_form_submit.setClickable(true);
                    timeline_form_submit.setEnabled(true);
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TimelineFormActivity2.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    timeline_form_submit.setClickable(true);
                    timeline_form_submit.setEnabled(true);
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TimelineFormActivity2.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().trim();
                        timeline_form_submit.setClickable(true);
                        timeline_form_submit.setEnabled(true);
                        progressDialog.dismiss();
                        Log.e(TAG, "TimelineForm Error " + error);
                        viewFailDialog.showDialog(TimelineFormActivity2.this, resources.getString(R.string.activity_submit_fail_msg));

                        // Toast.makeText(context, "Oops something went wrong. Please try again", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error))) {
                    notifyApiDelay(TimelineFormActivity2.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                isLoaded[0] = true;
                notifyApiDelay(TimelineFormActivity2.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "TimelineForm failure" + t.toString());
                if (t instanceof SocketTimeoutException) {
                    uploadActivityData();
                } else {
                    timeline_form_submit.setClickable(true);
                    timeline_form_submit.setEnabled(true);
                    progressDialog.dismiss();
                    viewFailDialog.showDialog(TimelineFormActivity2.this, resources.getString(R.string.activity_submit_fail_msg));

                }
                //Toast.makeText(context, "Failed to upload activity. Please try again", Toast.LENGTH_LONG).show();
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                internetFlow(isLoaded[0]);
            }
        }, AppConstants.DELAY);


//        setResult(RESULT_OK);
//        finish();

    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    String imgLat = "0", imgLong = "0";

    public Bitmap addWatermark(Bitmap source) {
        int w, h;
        Canvas c;
        Paint paint;
        Bitmap bmp, watermark;
        Matrix matrix;
        float scale;
        RectF r;
        w = source.getWidth();
        h = source.getHeight();
        // Create the new bitmap
        bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
        // Copy the original bitmap into the new one
        c = new Canvas(bmp);
        c.drawBitmap(source, 0, 0, paint);
        // Load the watermark
        watermark = BitmapFactory.decodeResource(resources, R.drawable.ct_water_mark);
        // Scale the watermark to be approximately 40% of the source image height
        scale = (float) (((float) h * 0.15) / (float) watermark.getHeight());
        // Create the matrix
        matrix = new Matrix();
        matrix.postScale(scale, scale);
        // Determine the post-scaled size of the watermark
        r = new RectF(0, 0, watermark.getWidth(), watermark.getHeight());
        matrix.mapRect(r);
        // Move the watermark to the bottom right corner
        matrix.postTranslate(w - r.width(), h - r.height());
        // Draw the watermark
        c.drawBitmap(watermark, matrix, paint);
        // Free up the bitmap memory
        watermark.recycle();
        return bmp;
    }

    private int checkSize(Paint addedBy, Rect textRec, int imageWidth, int textSize, String watermarkText) {

        while (textRec.width() >= (imageWidth - 4) / 3) {
            addedBy.getTextBounds(watermarkText, 0, watermarkText.length(), textRec);
            textSize--;
            addedBy.setTextSize(textSize);

            Log.e("checkSize", "TextRect: " + textRec.width() + ", ImageWidth: " + (imageWidth - 4) / 3);
            Log.e("checkSize", "Textsize " + textSize);
        }
        Log.e("checkSize", "TextRectO: " + textRec.width() + ", ImageWidth: " + (imageWidth - 4) / 3);
        Log.e("checkSize", "TextsizeO " + textSize);

        return textSize;

    }


    private Bitmap addWaterMark(Bitmap resource) {
        String userName = "Clicked by: " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NAME);
        Bitmap pikedPhoto = resource.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(pikedPhoto);
        Paint paintCenter = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
        paintCenter.setStyle(Paint.Style.FILL);
        paintCenter.setColor(Color.RED);
        paintCenter.setTextAlign(Paint.Align.CENTER);
        float textY = pikedPhoto.getHeight();
        float textX = pikedPhoto.getWidth() / 2;
        String text1 = "Image might not be from farm";
        Rect textRect = new Rect();
        paintCenter.getTextBounds(text1, 0, text1.length(), textRect);
        int s = checkSize(paintCenter, textRect, (canvas.getWidth() - 4), 50, text1);
        Log.e("checkSize", "textsize " + s);
        canvas.drawText(message, textX + 5, textY - 2 * s - 5, paintCenter);
        canvas.drawText(message2, textX + 5, textY - s - 5, paintCenter);
        canvas.drawText(message3, textX + 5, textY - 5, paintCenter);

        String text2 = "Left Text";
        Paint paintLeft = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
        paintLeft.setStyle(Paint.Style.FILL);
        paintLeft.setColor(Color.WHITE);
        //float sizeLeft = convertDpToPx(context, 25);
        paintLeft.setTextAlign(Paint.Align.LEFT);
        float textYL = pikedPhoto.getHeight();

        if (text1 == null || TextUtils.isEmpty(text1)) {
            Rect textRectL = new Rect();
            paintLeft.getTextBounds(userName, 0, userName.length(), textRectL);
            int sL = checkSize(paintLeft, textRectL, (canvas.getWidth() - 4), 50, userName);
            s = sL;
        }
        paintLeft.setTextSize(s);
        canvas.drawText(userName, 10, textYL - 2 * s - 5, paintLeft);
        canvas.drawText("Lat: " + imgLat, 10, textYL - s - 5, paintLeft);
        canvas.drawText("Long: " + imgLong, 10, textYL - 5, paintLeft);
        pikedPhoto = addWatermark(pikedPhoto);
        int f = (s * 2) + 10;
        return pikedPhoto;
    }

    @Override
    public void onChemicalUnitLoaded(List<DropDownT> chemicalUnitList) {
        for (int i = 0; i < chemicalUnitList.size(); i++) {
            Log.e(TAG, "Drop Down " + new Gson().toJson(chemicalUnitList.get(i)));
            if (AppConstants.CHEMICAL_UNIT.COMPANY_CHEMICALS.equals(chemicalUnitList.get(i).getDataType())) {
                //Timeline Chemicals
                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        JSONArray array = new JSONArray(chemicalUnitList.get(i).getData());
                        for (int j = 0; j < array.length(); j++) {
                            JSONObject object = array.getJSONObject(j);
                            TimelineChemicals chemicals = new Gson().fromJson(object.toString(), TimelineChemicals.class);
                            if (chemicalsList == null) {
                                chemicalsList = new ArrayList<>();
                            }
                            chemicalsList.add(chemicals);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } else if (AppConstants.CHEMICAL_UNIT.COMPANY_UNITS.equals(chemicalUnitList.get(i).getDataType())) {
                //Company Units

                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        JSONArray array = new JSONArray(chemicalUnitList.get(i).getData());
                        for (int j = 0; j < array.length(); j++) {
                            JSONObject object = array.getJSONObject(j);
                            TimelineUnits units = new Gson().fromJson(object.toString(), TimelineUnits.class);
                            if (timelineUnits == null) {
                                timelineUnits = new ArrayList<>();
                            }
                            timelineUnits.add(units);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onFarmLoader(Farm farm) {
        tv_lot_no_cal_act.setText(farm.getLotNo());
        tv_farmer_name_cal_act.setText(farm.getFarmerName());

        latLngList.clear();
        if (farm.getCoords() != null) {
            try {
                JSONArray arrayOffline = new JSONArray(farm.getCoords());
                for (int j = 0; j < arrayOffline.length(); j++) {

                    JSONObject object = arrayOffline.getJSONObject(j);
                    LatLngFarm data = new Gson().fromJson(object.toString(), LatLngFarm.class);
                    LatLng latLng = new LatLng(Double.valueOf(data.getLng()), Double.valueOf(data.getLng()));
                    latLngList.add(latLng);
                }
            } catch (JSONException e) {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onTimelineLoaded(Timeline timelineList) {
        calendarData.clear();
        if (timeline != null) {
            this.timeline = timelineList;
            Gson gson = new Gson();
            CalendarJSON calendarJSON = gson.fromJson(timelineList.getJsondata(), CalendarJSON.class);
            if (calendarJSON != null && calendarJSON.getCalendarData() != null && calendarJSON.getCalendarData().size() > 0) {
                calendarData.addAll(calendarJSON.getCalendarData());
            }
        } else {
            this.timeline = new Timeline(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), null, null);
            TimelineCacheManager.getInstance(context).addTimeline(TimelineFormActivity2.this, this.timeline);
        }
    }

    @Override
    public void onTimelineAdded() {
        TimelineCacheManager.getInstance(context).getAllTimeline(this, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
    }

    @Override
    public void onTimelineInsertError(Throwable e) {

    }

    @Override
    public void onNoFarmsAvailable() {

    }

    @Override
    public void TimelineDeleted() {

    }

    @Override
    public void TimelineDeletionFaild() {

    }

    private void getData(String farm_cal_activity_id) {
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("farm_cal_activity_id", "" + farm_cal_activity_id);
        jsonObject.addProperty("user_id", "" + userId);
        jsonObject.addProperty("token", "" + token);
        Log.e(TAG, "Cal ActSend " + new Gson().toJson(jsonObject));
        apiInterface.getAgriInputData2(farm_cal_activity_id, userId, token).enqueue(new Callback<AgriInputResponse>() {
            @Override
            public void onResponse(Call<AgriInputResponse> call, Response<AgriInputResponse> response) {
                String error = null;
                if (response.isSuccessful()) {
                    try {
                        Log.e(TAG, "Cal Data Resp " + new Gson().toJson(response.body()));
//                        Log.e(TAG, "Farmer Farms Resp " + response.body().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "Owner Id " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(TimelineFormActivity2.this, resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == 1) {


                        if (response.body().getData1().getAgriInputs() != null && response.body().getData1().getAgriInputs().size() > 0) {
                            agriInputs.addAll(response.body().getData1().getAgriInputs());
                            agriInputAdapter.notifyDataSetChanged();
                        } else {
//                            agriInputRel.setVisibility(View.GONE);
                        }


                        if (response.body().getData1() != null && response.body().getData1().getInstruction() != null && response.body().getData1().getInstruction().size() > 0) {
                            AgriInstructionAdapter agriInstructionAdapter;
                            agriInstructionAdapter = new AgriInstructionAdapter(context, response.body().getData1().getInstruction());

                            agriInputInstructioRecycler.setHasFixedSize(true);
                            agriInputInstructioRecycler.setLayoutManager(new LinearLayoutManager(context));
                            agriInputInstructioRecycler.setNestedScrollingEnabled(false);
                            agriInputInstructioRecycler.setAdapter(agriInstructionAdapter);
                        } else
                            agriInptRel.setVisibility(View.GONE);
                    } else {

                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TimelineFormActivity2.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TimelineFormActivity2.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "Cal Data Err " + error);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showDialog(TimelineFormActivity2.this, resources.getString(R.string.failed_load_farm));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
                    notifyApiDelay(TimelineFormActivity2.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<AgriInputResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(TimelineFormActivity2.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "Cal Data fail " + t.toString());
                ViewFailDialog viewFailDialog = new ViewFailDialog();
//                viewFailDialog.showDialog(TimelineFormActivity2.this, resources.getString(R.string.failed_load_farm));
            }
        });
        internetFlow(isLoaded[0]);

    }

    public void showManualInputDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_manual_input);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        AutoCompleteTextView etInputName = dialog.findViewById(R.id.etInputName);
        AutoCompleteTextView etInputQty = dialog.findViewById(R.id.etInputQty);
        AutoCompleteTextView etInputCost = dialog.findViewById(R.id.etInputCost);
        TextView cancelTv = dialog.findViewById(R.id.cancelTv);
        TextView addTv = dialog.findViewById(R.id.addTv);
        addTv.setText(resources.getString(R.string.add));
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etInputName.getText().toString())) {
                    Toasty.error(context, resources.getString(R.string.enter_input_name), Toast.LENGTH_SHORT).show();
                    etInputName.setError(resources.getString(R.string.required_label));
                } else if (TextUtils.isEmpty(etInputQty.getText().toString())) {
                    Toasty.error(context, resources.getString(R.string.enter_input_used_qty), Toast.LENGTH_SHORT).show();
                    etInputQty.setError(resources.getString(R.string.required_label));
                } else if (TextUtils.isEmpty(etInputCost.getText().toString())) {
                    Toasty.error(context, resources.getString(R.string.enter_input_cost), Toast.LENGTH_SHORT).show();
                    etInputCost.setError(resources.getString(R.string.required_label));
                } else {
                    dialog.dismiss();
                    AgriInput agriInput = new AgriInput("0", "N/A", null, "N/A", "", "", "", "Petrol");
                    agriInput.setCost(etInputCost.getText().toString().trim());
                    agriInput.setUsedQuantity(etInputQty.getText().toString().trim());
                    agriInput.setManualAdded(true);
                    agriInputs.add(agriInput);
                    agriInputAdapter.notifyDataSetChanged();

                }

            }
        });
        dialog.show();
    }

    private void getCompAgriInputs() {
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getCompAgriInputs(compId, userId, token).enqueue(new Callback<CompAgriResponse>() {
            @Override
            public void onResponse(Call<CompAgriResponse> call, Response<CompAgriResponse> response) {
                String error = null;
                if (response.isSuccessful()) {
                    try {
                        Log.e(TAG, "getCompAgriInputs res " + new Gson().toJson(response.body()));
//                        Log.e(TAG, "Farmer Farms Resp " + response.body().string());
                        if (response.body().getData() != null && response.body().getData().size() > 0) {
//                            agriInputAdapter.setCompAgriDatumList(response.body().getData());
                            compAgriDatumList.addAll(response.body().getData());
                        } else {


                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TimelineFormActivity2.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TimelineFormActivity2.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "getCompAgriInputs Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
                    notifyApiDelay(TimelineFormActivity2.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
                if ((timelineInnerData != null && timelineInnerData.getAgriInput() != null && timelineInnerData.getAgriInput().size() > 0) || (compAgriDatumList != null && compAgriDatumList.size() > 0)) {
                    agriInputRel.setVisibility(View.VISIBLE);
                } else {
                    agriInputRel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CompAgriResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(TimelineFormActivity2.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "getCompAgriInputs fail " + t.toString());
                if ((timelineInnerData != null && timelineInnerData.getAgriInput() != null && timelineInnerData.getAgriInput().size() > 0) || (compAgriDatumList != null && compAgriDatumList.size() > 0)) {
                    agriInputRel.setVisibility(View.VISIBLE);
                } else {
                    agriInputRel.setVisibility(View.GONE);
                }
            }
        });
        internetFlow(isLoaded[0]);

    }

}
