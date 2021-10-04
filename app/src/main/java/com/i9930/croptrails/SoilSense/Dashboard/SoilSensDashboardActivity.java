package com.i9930.croptrails.SoilSense.Dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionMenu;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.TimelineUnits;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.FarmDetailsUpdateActivity2;
import com.i9930.croptrails.FarmDetails.Model.FullDetailsResponse;
import com.i9930.croptrails.Landing.Fragments.DashboardMapModel.LockableScrollView;
import com.i9930.croptrails.Landing.Fragments.DashboardMapModel.MapResponse;
import com.i9930.croptrails.Landing.Fragments.DashboardMapModel.MultiTouchMapFragment;
import com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel.DashboardResponse;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Maps.ShowArea.Model.GetGpsCoordinates;
import com.i9930.croptrails.Maps.ShowArea.Model.geojson.GeoJsonResponse;
import com.i9930.croptrails.Maps.ShowArea.ShowAreaOnMapActivity;
import com.i9930.croptrails.Maps.VerifyArea.MapsActivity;
import com.i9930.croptrails.Maps.VerifyArea.MapsActivityEasyMode;
import com.i9930.croptrails.Maps.VerifyArea.MapsActivitySamunnati;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SoilSense.BluetoothCommunicationActivity;
import com.i9930.croptrails.SoilSense.Dashboard.Adapter.SoilSensDateBarAdapter;
import com.i9930.croptrails.SoilSense.Dashboard.Adapter.SoilSensTabularAdapter;
import com.i9930.croptrails.SoilSense.Dashboard.Model.GetSoilDatum;
import com.i9930.croptrails.SoilSense.SoilDetails;
import com.i9930.croptrails.Test.MyCustomLayoutManager;
import com.i9930.croptrails.Test.ObjectType;
import com.i9930.croptrails.Test.SoilSensResModel.SoilSenseResponse;
import com.i9930.croptrails.Test.adapter.TimelineAdapterTest;
import com.i9930.croptrails.Test.model.full.FarmAddressDetails;
import com.i9930.croptrails.Test.model.full.FarmCropDetails;
import com.i9930.croptrails.Test.model.full.FarmFullDetails;
import com.i9930.croptrails.Test.model.full.FarmsDetails;
import com.i9930.croptrails.Test.model.full.PersonDetails;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.AppFeatures;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;
import static com.i9930.croptrails.Utility.AppConstants.for_date;
import static com.i9930.croptrails.Utility.AppConstants.getCoordinatesFromGeometry;
import static com.i9930.croptrails.Utility.AppConstants.isValidActualArea;
import static com.i9930.croptrails.Utility.AppConstants.isValidString;

public class SoilSensDashboardActivity extends AppCompatActivity implements OnMapReadyCallback {


    SoilSensDashboardActivity context;
    String TAG = "SoilSensDashboardActivity";
    Toolbar mActionBarToolbar;
    Resources resources;
    private String mParam1;
    private String mParam2;
    LineChart chart;
    private GoogleMap mMap;
    // private ScrollView dashboardScrollview;
    private LockableScrollView dashboardScrollview;
    private TextView area4;
    private TextView date3;
    boolean connected = false;
    RelativeLayout relativeLayout;

    private TextView expectedStdArea;

    private GifImageView progressBar;
    private LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation;
    MultiTouchMapFragment mapFragment;
    RelativeLayout map_rel_lay;
    RelativeLayout no_data_layout;
    Call<DashboardResponse> callBarChart;
    Call<MapResponse> callFarmCoordinates;
    ConnectivityManager connectivityManager;
    View view;
    @BindView(R.id.soilSensDataLayout)
    LinearLayout soilSensDataLayout;
    View connectivityLine;
    RecyclerView recyclerView, recyclerViewBar, recyclerViewTabular;

    HashMap<String, List<GetSoilDatum>> stringListHashMap = new HashMap<>();
    FloatingActionButton addNewSoilSens;
    FloatingActionMenu fabParent;
    com.github.clans.fab.FloatingActionButton fabChildAddRecord, fabChildAddArea;

    private void initView() {


        moreFarmTv = findViewById(R.id.moreFarmTv);
        timelineStandingArea = findViewById(R.id.timelineStandingArea);
        timelineSowingArea = findViewById(R.id.timelineSowingArea);
        timelineDisaptchArea = findViewById(R.id.timelineDisaptchArea);


        try {
            fabChildAddRecord = findViewById(R.id.fabChildAddRecord);
            fabChildAddArea = findViewById(R.id.fabChildAddArea);
            fabParent = (FloatingActionMenu) findViewById(R.id.fabParent);
            addNewSoilSens = findViewById(R.id.addNewSoilSens);
        } catch (Exception e) {
            e.printStackTrace();
        }


        no_data_layout = findViewById(R.id.no_data_layout);
        recyclerViewTabular = findViewById(R.id.recyclerViewTabular);
        recyclerViewBar = findViewById(R.id.recyclerViewBar);
        connectivityLine = findViewById(R.id.connectivityLine);
        chart = findViewById(R.id.chart);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBarDash);
        relativeLayout = findViewById(R.id.no_internet_layout);
        dashboardScrollview = findViewById(R.id.dashboard_scrollview);
        area4 = findViewById(R.id.area4);
        date3 = findViewById(R.id.date3);
        expectedStdArea = findViewById(R.id.expected_stdarea1);
        map_rel_lay = findViewById(R.id.map_rel_lay);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mapFragment = (MultiTouchMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_dashboard);

        int height = getResources().getDisplayMetrics().heightPixels;
        map_rel_lay.getLayoutParams().height = (height / 2);
        mapFragment.getMapAsync(this);

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
        if (connected) {

            relativeLayout.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }
        if (fabChildAddRecord != null) {
            fabChildAddRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fabParent.isOpened())
                        fabParent.close(true);
                    Intent intent = new Intent(context, BluetoothCommunicationActivity.class);
                    intent.putExtra(SharedPreferencesMethod.SVFARMID, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                    startActivityForResult(intent, MY_PERMISSIONS_REQUEST_GET_BT_DATA);

                }
            });
        }

        if (fabChildAddArea != null) {
            fabChildAddArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fabParent.isOpened())
                        fabParent.close(true);
                    if (!isAreaCaptured) {
                        showDialog(true);
                    } else {
                        Intent intent = new Intent(context, ShowAreaOnMapActivity.class);
                        intent.putExtra(SharedPreferencesMethod.SVFARMID, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                        ActivityOptions options = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            startActivity(intent, options.toBundle());
                        } else {
                            startActivity(intent);
                        }
                    }
                }
            });
        }

    }

    boolean isAreaCaptured = false;
    TextView timelineDisaptchArea, timelineSowingArea, timelineStandingArea, moreFarmTv;
    @BindView(R.id.chakLeadNameTv)
    TextView chakLeadNameTv;
    @BindView(R.id.chakLeadCard)
    CardView chakLeadCard;


    @BindView(R.id.farmerNameTv)
    TextView farmerNameTv;
    @BindView(R.id.dobTv)
    TextView dobTv;
    @BindView(R.id.fatherNameTv)
    TextView fatherNameTv;
    @BindView(R.id.gender)
    TextView gender;
    @BindView(R.id.farmIdTv)
    TextView farmIdTv;
    @BindView(R.id.khasraTv)
    TextView khasraTv;

    @BindView(R.id.chakTv)
    TextView chakTv;
    @BindView(R.id.chakLeadCardMob)
    CardView chakLeadCardMob;
    @BindView(R.id.chakLeadNameTvMob)
    TextView chakLeadNameTvMob;
    @BindView(R.id.farmerDetailsLabelTv)
    TextView farmerDetailsLabelTv;
    @BindView(R.id.moreTv)
    TextView moreTv;
    @Nullable
    @BindView(R.id.farmerNameCard)
    CardView farmerNameCard;
    @Nullable
    @BindView(R.id.mobileCard)
    CardView mobileCard;
    @Nullable
    @BindView(R.id.dobCard)
    CardView dobCard;
    @Nullable
    @BindView(R.id.fatherCard)
    CardView fatherCard;
    @Nullable
    @BindView(R.id.genderCard)
    CardView genderCard;
    @Nullable
    @BindView(R.id.addlCard)
    CardView addlCard;
    @Nullable
    @BindView(R.id.addL2Card)
    CardView addL2Card;
    @Nullable
    @BindView(R.id.expAreaCard)
    CardView expAreaCard;
    @Nullable
    @BindView(R.id.actAreaCard)
    CardView actAreaCard;
    @Nullable
    @BindView(R.id.standigAreaCard)
    CardView standigAreaCard;
    @Nullable
    @BindView(R.id.seedQtyCard)
    CardView seedQtyCard;
    @Nullable
    @BindView(R.id.seedDateCard)
    CardView seedDateCard;
    @Nullable
    @BindView(R.id.irriTypeCard)
    CardView irriTypeCard;
    @Nullable
    @BindView(R.id.irriSourceCard)
    CardView irriSourceCard;
    @Nullable
    @BindView(R.id.prevCropCard)
    CardView prevCropCard;
    @Nullable
    @BindView(R.id.currentCropCard)
    CardView currentCropCard;
    @Nullable
    @BindView(R.id.soilTypeCard)
    CardView soilTypeCard;
    @Nullable
    @BindView(R.id.sowingDateCard)
    CardView sowingDateCard;
    @Nullable
    @BindView(R.id.expFlowerCard)
    CardView expFlowerCard;
    @Nullable
    @BindView(R.id.expHarvestCard)
    CardView expHarvestCard;
    @Nullable
    @BindView(R.id.actFlowerCard)
    CardView actFlowerCard;
    @Nullable
    @BindView(R.id.actHarvestCard)
    CardView actHarvestCard;
    @BindView(R.id.farm_details_irrigationSource)
    TextView farm_details_irrigationSource;
    @BindView(R.id.farm_details_irrigationType)
    TextView farm_details_irrigationType;
    @BindView(R.id.farm_details_previousCrop)
    TextView farm_details_previousCrop;
    @BindView(R.id.farm_details_soilType)
    TextView farm_details_soilType;
    @BindView(R.id.farm_details_sowingDate)
    TextView farm_details_sowingDate;
    @BindView(R.id.farm_details_expFloweringDate)
    TextView farm_details_expFloweringDate;
    @BindView(R.id.add_details_expHarvestDate)
    TextView add_details_expHarvestDate;
    @BindView(R.id.farm_details_actual_flowering_date)
    TextView farm_details_actual_flowering_date;
    @BindView(R.id.farm_details_actual_harvest_date)
    TextView farm_details_actual_harvest_date;
    @BindView(R.id.farm_details_mob_number)
    TextView farm_details_mob_number;
    String mobNo = "";
    String navigateTo = "";
    //    @BindView(R.id.farmerDetailsLabelTv)
//    TextView farmerDetailsLabelTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = getResources();
        String languageToLoad = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_sens_dashboard);


        context = this;
        ButterKnife.bind(this);
        drawerLayout = findViewById(R.id.drawerLayout);
        initView();
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoilSensDashboardActivity.this.onBackPressed();
            }
        });
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.soil_sense) + " " + resources.getString(R.string.title_dashboard));

        fetchAllDataOnline(true);
        getFullDetails();

        farmerDetailsLabelTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int[] textLocation = new int[2];
                    farmerDetailsLabelTv.getLocationOnScreen(textLocation);
                    if (event.getRawX() <= textLocation[0] + farmerDetailsLabelTv.getTotalPaddingLeft()) {

                        return true;
                    }
                    if (event.getRawX() >= textLocation[0] + farmerDetailsLabelTv.getWidth() - farmerDetailsLabelTv.getTotalPaddingRight()) {
                        // Right drawable was tapped
//                        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                        farmerDetailsLabelTv.setClickable(false);
                        farmerDetailsLabelTv.setEnabled(false);
                        Intent intent = new Intent(context, FarmDetailsUpdateActivity2.class);
                        startActivity(intent);
//                        }
                        return true;
                    }
                }
                return true;
            }
        });
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
                            AppConstants.failToast(context, resources.getString(R.string.check_internet_connection_msg));
                        } else {
                            ConnectivityUtils.checkSpeedWithColor(context, new ConnectivityUtils.ColorListener() {
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

        }

    }

    List<Object> objectList = new ArrayList<>();
    List<ObjectType> objectTypeList = new ArrayList<>();



    private void loadMapData() {
        objectList.clear();
        objectTypeList.clear();
        stringListHashMap.clear();
//        prepareBarData();

        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        final String farm_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        Log.e(TAG, "farm_id " + farm_id);
//        comp_id="100131";
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        Call<SoilSenseResponse> getCalenderData = apiInterface.getSoilSensData(comp_id, farm_id, userId, token);
        getCalenderData.enqueue(new Callback<SoilSenseResponse>() {
            @Override
            public void onResponse(Call<SoilSenseResponse> call, retrofit2.Response<SoilSenseResponse> response) {
                Log.e(TAG, "SoilSenseResponse Status " + response.code());
                isLoaded[0] = true;
                String error = "";
                if (response.isSuccessful()) {
                    SoilSenseResponse timelineResponse = response.body();
                    if (response.body().getStatus() == 10) {
                        if (isActivityRunning)
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        if (isActivityRunning)
                            viewFailDialog.showSessionExpireDialog(SoilSensDashboardActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        if (isActivityRunning)
                            viewFailDialog.showSessionExpireDialog(SoilSensDashboardActivity.this, resources.getString(R.string.authorization_expired));
                    } else if (timelineResponse.getStatus() == 1 && timelineResponse.getData() != null && timelineResponse.getData().size() > 0) {
                        for (int position = 0; position < timelineResponse.getData().size(); position++) {
                            GetSoilDatum data = timelineResponse.getData().get(position);
                            objectList.add(data);
                            String doa = AppConstants.getShowableDate(data.getDoa(), context);
                            objectTypeList.add(new ObjectType(AppConstants.getUploadableDate(doa, context), AppConstants.TIMELINE.SOIL_SENSE));
                            try {
                                if (isValidString(AppConstants.getUploadableDate(doa, context))) {
                                    if (stringListHashMap.containsKey(AppConstants.getUploadableDate(doa, context))) {
                                        List<GetSoilDatum> d = stringListHashMap.get(AppConstants.getUploadableDate(doa, context));
                                        if (d == null || d.size() == 0) {
                                            d = new ArrayList<>();
                                            d.add(data);
                                            stringListHashMap.put(AppConstants.getUploadableDate(doa, context), d);
                                        } else {
                                            d.add(data);
                                            stringListHashMap.put(AppConstants.getUploadableDate(doa, context), d);
                                        }
                                    } else {
                                        List<GetSoilDatum> d = new ArrayList<>();
                                        d.add(data);
                                        stringListHashMap.put(AppConstants.getUploadableDate(doa, context), d);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        setbarData(timelineResponse.getData());
                        setChartAndCardData(timelineResponse.getData());
//                        setbarData();
//                        setRecyclerData();
//                        setChartAndCardData(response.body().getData());

                    } else {
                        //No data
                        try {
                            no_data_layout.setVisibility(View.VISIBLE);
                            relativeLayout.setVisibility(View.GONE);
                            soilSensDataLayout.setVisibility(View.GONE);
                        } catch (Exception e) {
                        }
                    }
                    Log.e(TAG, "SoilSenseResponse " + new Gson().toJson(response.body()));
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    if (isActivityRunning)
                        viewFailDialog.showSessionExpireDialog(SoilSensDashboardActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    if (isActivityRunning)
                        viewFailDialog.showSessionExpireDialog(SoilSensDashboardActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {

                        error = response.errorBody().string().toString();
                        Log.e(TAG, "SoilSenseResponse Unsuccess " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(SoilSensDashboardActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }

            }

            @Override
            public void onFailure(Call<SoilSenseResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                isLoaded[0] = true;
                long diff = newMillis - currMillis;
                notifyApiDelay(SoilSensDashboardActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "SoilSenseResponse Failure " + t.toString());
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                internetFlow(isLoaded[0]);
            }
        }, AppConstants.DELAY);
    }

    private void setbarData(List<GetSoilDatum> list) {

        if (stringListHashMap != null && stringListHashMap.size() > 0) {
            List<String> lst = new ArrayList<String>(stringListHashMap.keySet());
            recyclerViewBar.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
            recyclerViewBar.setLayoutManager(layoutManager);
            SoilSensDateBarAdapter adapter = new SoilSensDateBarAdapter(context, stringListHashMap, lst, new SoilSensDateBarAdapter.OnSoilDateClickListener() {
                @Override
                public void onDateClicked(String key, List<GetSoilDatum> sendSoilDataList) {
                    setMapData(sendSoilDataList);
                }
            });
            recyclerViewBar.setAdapter(adapter);
            recyclerViewBar.setNestedScrollingEnabled(false);
            recyclerViewBar.setVisibility(View.VISIBLE);
            try {
                String last = lst.get(lst.size() - 1);
                setMapData(stringListHashMap.get(last));
            } catch (Exception e) {
                e.printStackTrace();
            }
            recyclerViewTabular.setHasFixedSize(true);
            LinearLayoutManager layoutManager2 = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            recyclerViewTabular.setLayoutManager(layoutManager2);
            List<GetSoilDatum> getSoilDatumList = new ArrayList<>();
            getSoilDatumList.add(new GetSoilDatum(null, null, null, null, null, null, null));
            getSoilDatumList.addAll(list);
            SoilSensTabularAdapter adapter2 = new SoilSensTabularAdapter(context, getSoilDatumList);
            recyclerViewTabular.setAdapter(adapter2);
            recyclerViewTabular.setNestedScrollingEnabled(false);
            recyclerViewTabular.setVisibility(View.VISIBLE);
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            recyclerViewBar.setVisibility(View.GONE);
        }
    }

    ArrayList<Marker> markers = new ArrayList<>();

    private void setMapData(List<GetSoilDatum> data) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (markers == null)
            markers = new ArrayList<>();

        if (markers != null && markers.size() > 0) {
            for (int i = 0; i < markers.size(); i++) {
                markers.get(i).remove();
            }
            markers.clear();
        }
        for (int i = 0; i < data.size(); i++) {
            SoilDetails details = null;
            try {
                details = new Gson().fromJson(data.get(i).getDetailStr(), SoilDetails.class);
            } catch (Exception e) {
            }

            String title = "";
            String doa = data.get(i).getDoa();

            if (details != null && isValidString(details.getReading())) {
                title = details.getReading();
            }/* else if (data.get(i).getDetails() != null && AppConstants.isValidString(data.get(i).getDetails().getReading())) {
                title = data.get(i).getDetails().getReading();
            }*/

            if (data.get(i).getLatitude() != null && data.get(i).getLongitude() != null && !data.get(i).getLatitude().equals("0") && !data.get(i).getLongitude().equals("0") && data.get(i).getLongitude() != null) {
                LatLng loc = new LatLng(Double.parseDouble(data.get(i).getLatitude().toString()), Double.parseDouble(data.get(i).getLongitude().toString()));
                //mMap.addMarker(new MarkerOptions().position(loc).title(data.get(i).getLotNo()));
//                markers.add(mMap.addMarker(new MarkerOptions().position(loc).title(resources.getString(R.string.soil_moisture_value) + " : " + title + "(" + AppConstants.getShowableDate(doa) + ")")));
                markers.add(mMap.addMarker(new MarkerOptions().position(loc).title(resources.getString(R.string.soil_moisture_value) + "%" + " : " + title + "(" + AppConstants.getShowableDate(doa, context) + ")")));
            }
        }

        if (markers != null) {
            if (markers.size() > 0) {
                for (Marker marker : markers) {
                    builder.include(marker.getPosition());
                }

                LatLngBounds bounds = builder.build();
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;

                mapFragment.getView().setVisibility(View.VISIBLE);
                map_rel_lay.getLayoutParams().height = height / 2;
                //int height=map_rel_lay.getMeasuredHeight();
                int padding = (int) (width * .15);
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height / 2, padding);
                mMap.moveCamera(cu);
            } else {

                mapFragment.getView().setVisibility(View.GONE);
                map_rel_lay.getLayoutParams().height = 0;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Location Permission already granted
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    } else {
                        //Request Location Permission
                        checkLocationPermission();
                    }
                } else {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(true);
                }
            }
        }
    }

    ArrayList<Entry> ndviEntries = new ArrayList<>();
    ArrayList<String> xAxisLabel = new ArrayList<>();
    ArrayList<String> yAxisLabel = new ArrayList<>();

    private void initializeChart2(List<GetSoilDatum> satsureDataListNdvi) {
        xAxisLabel.clear();
        chart.invalidate();
        ndviEntries.clear();
        for (int i = 0; i < satsureDataListNdvi.size(); i++) {
            float y2 = 0;
            String date = satsureDataListNdvi.get(i).getDoa();
            try {
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                Date d = input.parse(date);
                SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
                xAxisLabel.add(output.format(d).substring(0, 5));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String s2 = "0.0";
            /*if (satsureDataListNdvi.get(i).getDetails() != null && AppConstants.isValidString(satsureDataListNdvi.get(i).getDetails().getReading())) {
                try {
                    s2 = satsureDataListNdvi.get(i).getDetails().getReading();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else*/
            SoilDetails details = null;
            try {
                details = new Gson().fromJson(satsureDataListNdvi.get(i).getDetailStr(), SoilDetails.class);
            } catch (Exception e) {
            }

            if (details != null && isValidString(details.getReading())) {
                try {
                    s2 = details.getReading();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            y2 = Float.valueOf(s2);
            ndviEntries.add(new Entry(i + 1, y2));
        }
        LineDataSet ndviSet = new LineDataSet(ndviEntries, resources.getString(R.string.soil_moisture_value));
        ndviSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        ndviSet.setLineWidth(1f);
        ndviSet.setCircleColor(resources.getColor(R.color.green));
        ndviSet.setDrawCircleHole(false);
        ndviSet.setColor(resources.getColor(R.color.green));
        ndviSet.setValueTextColor(resources.getColor(R.color.green));

        yAxisLabel.clear();
        for (int i = 0; i < 12; i++) {
            int value = 10 * i;
            yAxisLabel.add(" " + value);
        }
        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);
        // set an alternative background color
        chart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData(ndviSet);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        // set data
        chart.setData(data);
        chart.animateX(1500);
        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        //X Axis
        XAxis xAxis = chart.getXAxis();
        //xAxis.setTextSize(11f);
        xAxis.setGranularity(1f);
        xAxis.setXOffset(1f);
        xAxis.setSpaceMin(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisLineWidth(1f);
        xAxis.setAxisMaximum(xAxisLabel.size());
        xAxis.setAxisMinimum(0f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setSpaceMax(1f);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        //Y Axis
        chart.getAxisRight().setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setTextColor(Color.BLACK);
       /* YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaximum(900);
        rightAxis.setAxisMinimum(-200);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);*/
        chart.highlightValue(ndviEntries.get(ndviEntries.size() - 1).getX(), 0); //
    }

    private void setChartAndCardData(List<GetSoilDatum> response) {
        initializeChart2(response);
    }

    private void setRecyclerData() {
        RecyclerAsync async = new RecyclerAsync();
        async.execute();
    }

    private class RecyclerAsync extends AsyncTask<String, Integer, List<String>> {
        @Override
        protected List<String> doInBackground(String... strings) {
            if (objectList.size() > 0) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Collections.sort(objectList, new Comparator<Object>() {
                    @Override
                    public int compare(Object lhs, Object rhs) {
                        try {
                            JSONObject obj = new JSONObject(new Gson().toJson(lhs));
                            JSONObject obj2 = new JSONObject(new Gson().toJson(rhs));
                            return format.parse(obj.getString("doa")).compareTo(format.parse(obj2.getString("doa")));
                        } catch (ParseException | JSONException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                Collections.sort(objectTypeList, new Comparator<ObjectType>() {
                    @Override
                    public int compare(ObjectType lhs, ObjectType rhs) {
                        try {
                            return format.parse(lhs.getDate()).compareTo(format.parse(rhs.getDate()));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                for (int i = 0; i < objectList.size(); i++) {
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(new Gson().toJson(objectList.get(i)));
                        Log.e(TAG, "Index loop " + i);
                        obj.put("index", i);
                        Object object = new Gson().fromJson(obj.toString(), Object.class);
                        objectList.set(i, object);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRecyclerData2();

                    }
                });
            }
            return null;
        }
    }

    private void setRecyclerData2() {
        MyCustomLayoutManager manager = new MyCustomLayoutManager(context);
        progressBar.setVisibility(View.GONE);
        List<TimelineUnits> timelineUnits = DataHandler.newInstance().getTimelineUnits();
        TimelineAdapterTest adapterTest = new TimelineAdapterTest(context, context, objectList, objectTypeList, timelineUnits, false, new TimelineAdapterTest.FarmDetailsClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onFarmDetailsClicked() {

            }

            @Override
            public void onMapIconClicked() {

            }
        });

        adapterTest.setOnSoilCardClick(false);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterTest);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.e(TAG, "onMapReady");
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10); // two minute interval
        mLocationRequest.setFastestInterval(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        googleMap.getUiSettings().setAllGesturesEnabled(false);

        mapFragment.mTouchView.setGoogleMapAndScroll(googleMap, dashboardScrollview);

        GetGeoJsonData fetchGpsCordinates = new GetGeoJsonData();
        fetchGpsCordinates.execute();
        loadMapData();


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
        isActivityRunning = false;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int MY_PERMISSIONS_REQUEST_GET_BT_DATA = 1;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(context)
                        .setTitle(resources.getString(R.string.location_permission_title))
                        .setMessage(resources.getString(R.string.location_permission_message))
                        .setPositiveButton(resources.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions((Activity) context,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    //  Toast.makeText(context, resources.getString(R.string.toast_permission_denied), Toast.LENGTH_LONG).show();
                    Toasty.error(context, resources.getString(R.string.toast_permission_denied), Toast.LENGTH_LONG, true).show();

                }
                return;
            }
        }
    }


    /*private class FetchGpsCordinates extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        public FetchGpsCordinates() {
            super();
            dialog = new ProgressDialog(context);
            dialog.setMessage(getResources().getString(R.string.please_wait_msg));
            dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
            String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("comp_id",""+compId);
            jsonObject.addProperty("farm_id",""+farmId);
            jsonObject.addProperty("user_id",""+userId);
            jsonObject.addProperty("token",""+token);
            Log.e(TAG,"Status coords "+new Gson().toJson(jsonObject));
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);

            Call<GetGpsCoordinates> getGpsCoordinatesCall = apiService.getGpsCoordinates(
                    farmId,
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID), userId, token);
            getGpsCoordinatesCall.enqueue(new Callback<GetGpsCoordinates>() {

                @Override
                public void onResponse(Call<GetGpsCoordinates> call, Response<GetGpsCoordinates> response) {
                    Log.e(TAG, "Status coords " + response.code());
                    if (response.isSuccessful()) {
                        GetGpsCoordinates getGpsCoordinates = response.body();
                        Log.e(TAG, "Status coords Rs " + new Gson().toJson(getGpsCoordinates));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog != null)
                                    dialog.cancel();
                            }
                        });

                        if (getGpsCoordinates.getStatus() == 10) {
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            if (isActivityRunning)
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            if (isActivityRunning)
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                        } else if (getGpsCoordinates.getStatus() != 0 && getGpsCoordinates.getData() != null
                                && getGpsCoordinates.getData().size() != 0) {

                        } else {
                            //Toasty.error(context, resources.getString(R.string.area_entere_manually_msg), Toast.LENGTH_LONG, false).show();
                            isAreaCaptured = false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        fabChildAddArea.setLabelText(resources.getString(R.string.capture_farm_area_label));
                                        fabChildAddArea.setImageResource(android.R.drawable.ic_menu_add);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        if (isActivityRunning)
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        if (isActivityRunning)
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog != null)
                                    dialog.cancel();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<GetGpsCoordinates> call, Throwable t) {
                    //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, t.toString());
                    if (isActivityRunning) {
                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_get_farm_location_msg));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog != null)
                                dialog.cancel();
                        }
                    });
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

    private void getGeoJson(JsonObject jsonObject, String type, String name) {

        try {
            String json = new Gson().toJson(jsonObject);
            JSONObject jsonObjectGeo = new JSONObject(json);
            GeoJsonLayer layer = new GeoJsonLayer(mMap, jsonObjectGeo);
            for (GeoJsonFeature feature : layer.getFeatures()) {
                if (feature != null) {
                    if (feature.getGeometry() != null && AppConstants.isValidString(feature.getGeometry().getGeometryType()) &&
                            feature.getGeometry().getGeometryType().trim().equalsIgnoreCase("point")) {

                        if (feature.getPointStyle() != null) {
                            feature.getPointStyle().setTitle(name);
                        }
                        GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();
//                        pointStyle.setIcon(BitmapDescriptorFactory.fromBitmap(bmp));
                        pointStyle.setTitle(name);
                        feature.setPointStyle(pointStyle);
                    } else {
                        GeoJsonPolygonStyle style = new GeoJsonPolygonStyle();
                        try {
                            Log.e(TAG, "loadJSONFromAsset type Feature " + feature.getGeometry().toString());
                            if (type != null && type.equals(AppConstants.AREA_TYPE.Farm)) {
                                style.setFillColor(AppConstants.POLYGON.FILL_FARM);
                                style.setStrokeColor(AppConstants.POLYGON.STROKE);
                                if (layer.getBoundingBox() != null)
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(layer.getBoundingBox(), 50));
                                else {
                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    List<LatLng> latLngList = new ArrayList<>();
                                    if (feature.hasGeometry()) {
                                        Geometry geometry = feature.getGeometry();

                                        latLngList.addAll(getCoordinatesFromGeometry(geometry));
                                        for (LatLng latLngg : latLngList) {
                                            builder.include(latLngg);
                                        }
                                    }
                                    prepareData(builder, latLngList);
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                                }
                            } else {
                                style.setFillColor(AppConstants.POLYGON.FILL);
                                style.setStrokeColor(AppConstants.POLYGON.FILL);
                            }
                            feature.setPolygonStyle(style);


                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                        if (feature.getProperties() != null)
                            Log.e(TAG, "loadJSONFromAsset Feature " + feature.toString());

                        if (layer.getBoundingBox() != null)
                            Log.e(TAG, "loadJSONFromAsset Feature bb " + layer.getBoundingBox());
                    }
                }
            }

            layer.addLayerToMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void prepareData(LatLngBounds.Builder builder, List<LatLng> latLngs) {

        DataHandler.newInstance().setLatLngsFarm(latLngs);


    }
    private class GetGeoJsonData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("user_id", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            jsonObject.addProperty("token", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            jsonObject.addProperty("comp_id", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
            jsonObject.addProperty("farm_id", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
//            jsonObject.addProperty("farm_id", "20202");
            Call<GeoJsonResponse> statusMsgModelCall = apiService.getFarmGeoJson(jsonObject);
            Log.e(TAG, "GetGeoJsonData Data " + new Gson().toJson(jsonObject));
            final boolean[] isLoaded = {false};
            long currMillis = AppConstants.getCurrentMills();
            statusMsgModelCall.enqueue(new Callback<GeoJsonResponse>() {
                @Override
                public void onResponse(Call<GeoJsonResponse> call, retrofit2.Response<GeoJsonResponse> response) {
                    String error = null;
                    isLoaded[0] = true;
                    if (response.isSuccessful()) {
                        Log.e(TAG, "GetGeoJsonData Response" + new Gson().toJson(response.body()));
                        GeoJsonResponse statusMsgModel = response.body();
                        if (statusMsgModel.getStatus() == 1) {
                            if (statusMsgModel.getData() != null && statusMsgModel.getData().size() > 0) {


                                for (int i = 0; i < statusMsgModel.getData().size(); i++) {
                                    getGeoJson(statusMsgModel.getData().get(i).getDetails(), statusMsgModel.getData().get(i).getType(),
                                            statusMsgModel.getData().get(i).getName());
                                }

                                isAreaCaptured = true;


                                try {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            int width = getResources().getDisplayMetrics().widthPixels;
                                            int height = getResources().getDisplayMetrics().heightPixels;
                                            mapFragment.getView().setVisibility(View.VISIBLE);
                                            map_rel_lay.getLayoutParams().height = height / 2;
                                            try {
                                                fabChildAddArea.setLabelText(resources.getString(R.string.farm_details_view_area));
                                                fabChildAddArea.setImageResource(android.R.drawable.ic_menu_view);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(SoilSensDashboardActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(SoilSensDashboardActivity.this, resources.getString(R.string.authorization_expired));
                        } else if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
//                                viewFailDialog.showSessionExpireDialog(SoilSensDashboardActivity.this, response.body().getMsg());
                        } else if (statusMsgModel.getStatus() == 4) {

                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_verify_farm_area));
                            //Toast.makeText(context, "Response Error Try again latter", Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(SoilSensDashboardActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(SoilSensDashboardActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            error = response.errorBody().string().toString();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_verify_farm_area));
                            Log.e(TAG, "GetGeoJsonData Error " + error);
                            progressBar.setVisibility(View.INVISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                        notifyApiDelay(SoilSensDashboardActivity.this, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }
                }

                @Override
                public void onFailure(Call<GeoJsonResponse> call, Throwable t) {
                    Log.e(TAG, "GetGeoJsonData Failure " + t.toString());
                    isLoaded[0] = true;
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    notifyApiDelay(SoilSensDashboardActivity.this, call.request().url().toString(),
                            "" + diff, internetSPeed, t.toString(), 0);
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_verify_farm_area));
                    progressBar.setVisibility(View.INVISIBLE);
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

    }

    boolean isActivityRunning = false;
    ViewFailDialog viewFailDialog = new ViewFailDialog();
    Polygon UCCpolygon;
    PolygonOptions options;

    @Override
    protected void onStart() {
        super.onStart();
        isActivityRunning = true;
        farmerDetailsLabelTv.setClickable(true);
        farmerDetailsLabelTv.setEnabled(true);
    }

    private void showArea(List<LatLng> latLngs) {

        if (latLngs == null || latLngs.size() < 3) {

//            viewFailDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.should_have_at_least_two_points_to_display));
        } else {


            String a = String.valueOf(String.format("%.4f", SphericalUtil.computeArea(latLngs) * 0.000247105));
            Log.e(TAG, "Area in Acres " + a);
//            String ar = AppConstants.getShowableAreaWithConversion(a, area_mult_factor);
//            tvarea.setText(ar + " " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL));
            options = new PolygonOptions()
                    .addAll(latLngs)
                    .fillColor(Color.parseColor("#330000FF"))
                    .strokeColor(Color.RED)
                    .strokeWidth(2f);
            UCCpolygon = mMap.addPolygon(options);
        }


    }

    private static final int WHOLE_DATA_CHANGE_REQUEST_CODE = 101;

    RelativeLayout relWalAroundMode, relEasyMode, relPinDropMode, relWalAroundModeAutomatic;
    TextView walkAutoTv;
    ImageView walkAutoImg;
    float locationAccuracy = 10.0f;
    boolean isAutoClisk = false;

    public void showDialog(boolean showAutoCalculate) {
        float compAccuracy = 2.0f;
        compAccuracy = SharedPreferencesMethod.getFloatPref(context, SharedPreferencesMethod.COMP_AUTO_ACCURACY);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_select_capture_area_mode);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        relWalAroundMode = dialog.findViewById(R.id.relWalAroundMode);
        relWalAroundModeAutomatic = dialog.findViewById(R.id.relWalAroundModeAutomatic);
        relEasyMode = dialog.findViewById(R.id.relEasyMode);
        relPinDropMode = dialog.findViewById(R.id.relPinDropMode);
        walkAutoImg = dialog.findViewById(R.id.walkAutoImg);
        walkAutoTv = dialog.findViewById(R.id.walkAutoTv);
        if (!showAutoCalculate) {
            relEasyMode.setVisibility(View.GONE);
        } else {
            relEasyMode.setVisibility(View.VISIBLE);
        }

        if (locationAccuracy < compAccuracy) {
            isAutoClisk = true;
            walkAutoTv.setTextColor(getResources().getColor(R.color.black));
            walkAutoImg.setImageResource(R.drawable.ic_directions_walk_24px);
        } else {
            isAutoClisk = false;
            walkAutoTv.setTextColor(getResources().getColor(R.color.faded_text));
            walkAutoImg.setImageResource(R.drawable.ic_directions_walk_disabled_24px);
        }

        relWalAroundModeAutomatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAutoClisk) {
                    // Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(context, MapsActivity.class);
                    if (!showAutoCalculate)
                        intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE);
                    else
                        intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.ACTUAL_AREA);
                    intent.putExtra("isAutomaticMode", true);
                    ActivityOptions options = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                    } else {
                        startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                    }
                }
            }
        });

        relWalAroundMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, MapsActivity.class);
                if (!showAutoCalculate)
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE);
                else
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.ACTUAL_AREA);
                intent.putExtra("isAutomaticMode", false);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                } else {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
            }
        });
        relPinDropMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, MapsActivitySamunnati.class);
                if (!showAutoCalculate)
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE);
                else
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.ACTUAL_AREA);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                } else {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
            }
        });
        relEasyMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, MapsActivityEasyMode.class);
                if (!showAutoCalculate)
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE);
                else
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.ACTUAL_AREA);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                } else {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
            }
        });
        dialog.show();


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GET_BT_DATA:
                if (resultCode == Activity.RESULT_OK) {
                    loadMapData();
                } else {

                }
                break;
        }
    }
    @BindView(R.id.seedQtyTv)
    TextView seedQtyTv;
    @BindView(R.id.seed_provided_on_tv)
    TextView seedProvidedOnTv;
    private boolean isSeedDataProvided = false;
    private FetchFarmResult farmResult;
    private void hideCard(CardView cardView) {
        if (cardView != null)
            cardView.setVisibility(View.GONE);
    }
    @BindView(R.id.title_name_d)
    TextView title_name_d;
    @BindView(R.id.title_address_d)
    TextView title_address_d;

    @BindView(R.id.farm_details_expected_area_tv)
    TextView farm_details_expected_area_tv;
    @BindView(R.id.farm_details_actual_area_tv)
    TextView farm_details_actual_area_tv;
    @BindView(R.id.farm_details_standing_area_tv)
    TextView farm_details_standing_area_tv;

    private void fetchAllDataOnline(boolean flag) {


        String farm_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        Log.e(TAG, "farmId " + farm_id + " And CompId " + comp_id);
        Log.e(TAG, "UserId " + userId);
        Log.e(TAG, "Token " + token);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        Call<FullDetailsResponse> farmAndHarvestCall = apiInterface.getFarmAndHarvestData(farm_id, comp_id, userId, token);
        farmAndHarvestCall.enqueue(new Callback<FullDetailsResponse>() {
            @Override
            public void onResponse(Call<FullDetailsResponse> call, Response<FullDetailsResponse> response) {
                Log.e(TAG, "Status farm " + response.code());
                isLoaded[0] = true;
                String error = "";
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 10) {
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(SoilSensDashboardActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(SoilSensDashboardActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            if (response.body().getExpense() != null) {
//                                total_expense_tv.setText(response.body().getExpense());
                            }
                            FullDetailsResponse detailsResponse = response.body();
                            if (detailsResponse.getFarmData() != null) {
//                                isaFarmLoaded = true;
                                FetchFarmResult fetchFarmResult = detailsResponse.getFarmData();
                                fetchFarmResult.setType(AppConstants.TIMELINE.FARM);
                                String date = "2010-01-21";

                                if (fetchFarmResult.getDoa() != null && !TextUtils.isEmpty(fetchFarmResult.getDoa()))
                                    date = fetchFarmResult.getDoa();
                                else fetchFarmResult.setDoa(date);


                                setFarmDataOnline(fetchFarmResult);
//                                germinationDataSet(fetchFarmResult);

                                if (detailsResponse.getFarmData().getActualArea() != null) {
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTUAL_AREA, detailsResponse.getFarmData().getActualArea().trim());
                                }
                                if (detailsResponse.getFarmData().getStandingAcres() != null) {
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, detailsResponse.getFarmData().getStandingAcres().trim());
                                }
                                if (detailsResponse.getFarmData().getExpArea() != null) {
                                    SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.EXPECTED_AREA,
                                            Float.valueOf(detailsResponse.getFarmData().getExpArea()));
                                }
                                if (detailsResponse.getFarmData().getGermination() != null &&
                                        !TextUtils.isEmpty(detailsResponse.getFarmData().getGermination())
                                        && !detailsResponse.getFarmData().getGermination().equals("[]")) {
                                    SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.GERMINATION,
                                            Float.valueOf(detailsResponse.getFarmData().getGermination().trim()));
                                } else {
                                    SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.GERMINATION,
                                            0.0);
                                }
                                String sowingDate = response.body().getFarmData().getSowingDate();
                                if (sowingDate != null && !TextUtils.isEmpty(sowingDate))
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SOWING_DATE, sowingDate);


//                                AVG_GERMI = detailsResponse.getFarmData().getGermination();
//                                AVG_POPU = detailsResponse.getFarmData().getPopulation();
//                                AVG_PP_SPACING = detailsResponse.getFarmData().getSpacingPtp();
//                                AVG_RR_SPACING = detailsResponse.getFarmData().getSpacingRtr();

                                farmResult = detailsResponse.getFarmData();


                            } else {

                            }

                        }
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(SoilSensDashboardActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(SoilSensDashboardActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
//                        isaFarmLoaded = false;
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "Unsuccess " + error);

                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(SoilSensDashboardActivity.this, call.request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<FullDetailsResponse> call, Throwable t) {
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(SoilSensDashboardActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                internetFlow(isLoaded[0]);
            }
        }, AppConstants.DELAY);

    }
    DrawerLayout drawerLayout;
    String area_unit_label = "";
    private void setFarmDataOnline(final FetchFarmResult farmData) {

        area_unit_label = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL);

//        areaUnitTv.setText(area_unit_label);
//        timelineCenterImageFarm.setImageResource(R.drawable.ic_google_maps);
        moreFarmTv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
        if (farmData != null) {


            Log.e("TestActivity", "Farm is not null");

            if (farmData.getExpArea() != null && !TextUtils.isEmpty(farmData.getExpArea())) {
                timelineDisaptchArea.setText(convertAreaTo(farmData.getExpArea()) + " " + area_unit_label);
            }
            if (farmData.getActualArea() != null && !TextUtils.isEmpty(farmData.getActualArea())) {
                timelineSowingArea.setText(convertAreaTo(farmData.getActualArea()) + " " + area_unit_label);
            }
            if (farmData.getStandingAcres() != null && !TextUtils.isEmpty(farmData.getStandingAcres())) {
                timelineStandingArea.setText(convertAreaTo(farmData.getStandingAcres()) + " " + area_unit_label);
            }

        } else {
            Log.e("TestActivity", "Farm is null");
        }
        moreFarmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.END))
                    drawerLayout.openDrawer(Gravity.RIGHT);
                else drawerLayout.closeDrawer(GravityCompat.END);
            }
        });

        /*timelineCenterImageFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE))
                    listener.onMapIconClicked();
            }
        });*/

        DataHandler.newInstance().setFetchFarmResult(farmData);
        Log.e(TAG, "Farm Data " + new Gson().toJson(farmData));
        if (farmData.getQtySeedProvided() != null) {
            seedQtyTv.setText(farmData.getQtySeedProvided());
            isSeedDataProvided = true;
        } else {
            hideCard(seedQtyCard);
        }
        if (farmData.getSeedProvidedOn() != null && !farmData.getSeedProvidedOn().equals(for_date)) {
            seedProvidedOnTv.setText(AppConstants.getShowableDate(farmData.getSeedProvidedOn(), context));
            isSeedDataProvided = true;
        } else {
            hideCard(seedDateCard);
        }

        if (farmData.getLatCord() != null && farmData.getLongCord() != null && !TextUtils.isEmpty(farmData.getLatCord()) && !TextUtils.isEmpty(farmData.getLongCord())) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARM_LAT, farmData.getLatCord());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARM_LONG, farmData.getLongCord());
        }

        String soil_type = "";
        //String avg_germination = "0";
        String irrigationSource = "";
        String previousCrop = "";
        String irrigationtype = "";
        String sowingDate = "";
        String expFloweringDate = "";
        String expHarvestDate = "";
        String actual_area = "0";
        String standing_area = "0";
        String expected_area = "0";



        if (farmData.getLotNo() != null) {
            ///farm_lot_no.setText(farmData.getLotNo());
//            title_name.setText(farmData.getLotNo());
            title_name_d.setText(farmData.getLotNo());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOT_NO, farmData.getLotNo());
        }
        if (farmData.getName() != null) {
//            title_address.setText(farmData.getName());
            title_address_d.setText(farmData.getName());
            farmerNameTv.setText(farmData.getName());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARMER_NAME, farmData.getName());
        } else
            farmerNameTv.setText("-");
        if (farmData.getMob() != null && !farmData.getMob().equals("9999999999")) {
            mobNo = farmData.getMob();
        } else {
            mobNo = "-";
            hideCard(mobileCard);
        }


        if (farmData.getActualFloweringDate() != null) {
            if (!farmData.getActualFloweringDate().equals(for_date)) {
                farm_details_actual_flowering_date.setText(AppConstants.getShowableDate(farmData.getActualFloweringDate(), context));
            } else
                hideCard(actFlowerCard);
        } else
            hideCard(actFlowerCard);
        if (farmData.getActualHarvestDate() != null) {
            if (!farmData.getActualHarvestDate().equals(for_date)) {
                farm_details_actual_harvest_date.setText(AppConstants.getShowableDate(farmData.getActualHarvestDate(), context));
            } else
                hideCard(actHarvestCard);
        } else
            hideCard(actHarvestCard);

        if (farmData != null) {
            if (farmData.getSoilType() != null) {
                soil_type = farmData.getSoilType().toString();
            }
            if (farmData.getActualArea() != null && farmData.getActualArea() != "0") {
                actual_area = farmData.getActualArea().toString();
            }

            if (farmData.getIrrigationSource() != null) {
                irrigationSource = farmData.getIrrigationSource().toString();
            }
            if (farmData.getMob() != null && !farmData.getMob().equals("9999999999")) {
                mobNo = farmData.getMob().toString();
            }
            if (farmData.getPreviousCrop() != null) {
                previousCrop = farmData.getPreviousCrop().toString();
            }
            if (farmData.getIrrigationType() != null) {
                irrigationtype = farmData.getIrrigationType().toString();
            }
            if (farmData.getSowingDate() != null && !farmData.getSowingDate().equals(for_date)) {
                sowingDate = AppConstants.getShowableDate(farmData.getSowingDate(), context);
            } else {
//                showDialogForSowingDate(resources.getString(R.string.add_sowing_date_msg));
            }
            if (farmData.getExpFloweringDate() != null && !farmData.getExpFloweringDate().equals(for_date)) {
                expFloweringDate = AppConstants.getShowableDate(farmData.getExpFloweringDate(), context);
            }
            if (farmData.getExpHarvestDate() != null && !farmData.getExpHarvestDate().equals(for_date)) {
                expHarvestDate = AppConstants.getShowableDate(farmData.getExpHarvestDate(), context);
            }
            if (farmData.getStandingAcres() != null) {
                standing_area = farmData.getStandingAcres();
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, standing_area);
            }
            if (farmData.getExpArea() != null) {
                expected_area = farmData.getExpArea();
            }
        }

        if (isValidActualArea(actual_area))
            farm_details_actual_area_tv.setText(convertAreaTo(actual_area));
        else hideCard(actAreaCard);
        if (isValidActualArea(standing_area))
            farm_details_standing_area_tv.setText(convertAreaTo(standing_area));
        else hideCard(standigAreaCard);
        if (isValidActualArea(expected_area))
            farm_details_expected_area_tv.setText(convertAreaTo(expected_area));
        else hideCard(expAreaCard);
        if (isValidString(irrigationSource))
            farm_details_irrigationSource.setText(irrigationSource);
        else hideCard(irriSourceCard);
        if (isValidString(irrigationtype))
            farm_details_irrigationType.setText(irrigationtype);
        else hideCard(irriTypeCard);
        if (isValidString(previousCrop) && !previousCrop.equals("0"))
            farm_details_previousCrop.setText(previousCrop);
        else hideCard(prevCropCard);
        if (isValidString(soil_type))
            farm_details_soilType.setText(soil_type);
        else hideCard(soilTypeCard);
        if (mobNo != null && !mobNo.equals("0") && !mobNo.equals("9999999999"))
            farm_details_mob_number.setText(mobNo);
        else {
            farm_details_mob_number.setText("-");
            hideCard(mobileCard);
        }
        if (isValidString(sowingDate) && !sowingDate.equals(for_date))
            farm_details_sowingDate.setText(sowingDate);
        else hideCard(sowingDateCard);
        if (isValidString(expFloweringDate) && !expFloweringDate.equals(for_date))
            farm_details_expFloweringDate.setText(expFloweringDate);
        else hideCard(expFlowerCard);
        if (isValidString(expHarvestDate) && !expHarvestDate.equals(for_date))
            add_details_expHarvestDate.setText(expHarvestDate);
        else hideCard(expHarvestCard);

        boolean mode = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE);
        boolean isDelq = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_DELINQUENT_COMP);
        boolean isVetting = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_VETTING_COMP);
        String vetting = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARM_VETTING);
        if (actual_area.trim().equals("0") || !isValidActualArea(actual_area)) {
            navigateTo = "map";

        } else {
            navigateTo = "view_details";


        }
        farm_details_mob_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mobNo.equals("-") && !mobNo.equals("9999999999")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mobNo));
                    startActivity(intent);
                }
            }
        });

    }
    private String convertAreaTo(String area) {

        return AppConstants.getShowableArea(area);
    }
    @BindView(R.id.directionFarmTv)
    TextView directionFarmTv;
    private void getFullDetails() {
        JsonObject object = new JsonObject();
        object.addProperty("farm_id", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
        object.addProperty("user_id", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        object.addProperty("token", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        Log.e(TAG, "SEND FULL DETAIL PARAM  " + new Gson().toJson(object));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        apiInterface.getFullFarmDetails(object).enqueue(new Callback<FarmFullDetails>() {
            @Override
            public void onResponse(Call<FarmFullDetails> call, Response<FarmFullDetails> response1) {
                String error = null;
                Log.e(TAG, "Response FULL CODE" + response1.code());
                if (response1.isSuccessful()) {
                    Log.e(TAG, "Response FULL  " + new Gson().toJson(response1.body()));
                    DataHandler.newInstance().setFarmFullDetails(response1.body());
                    FarmFullDetails details = response1.body();
                    try {
                        if (details.getPersonDetails() != null) {
                            PersonDetails personDetails = details.getPersonDetails();
                            if (AppFeatures.isChakLeaderEnabled() && details.getFarmAddressDetails() != null) {
                                if (isValidString(details.getFarmAddressDetails().getAddL2())) {
//                                    getChakLeader("" + details.getPersonDetails().getName(), details.getPersonDetails().getPersonId() + "", details.getFarmAddressDetails().getAddL2());
                                }
                            }

                            if (isValidString(personDetails.getGender())) {
                                if (personDetails.getGender().equalsIgnoreCase("m")) {
                                    gender.setText(resources.getString(R.string.male_label));
                                } else if (personDetails.getGender().equalsIgnoreCase("f")) {
                                    gender.setText(resources.getString(R.string.female_label));
                                } else if (personDetails.getGender().equalsIgnoreCase("o")) {
                                    gender.setText(resources.getString(R.string.other_labe));
                                } else {
                                    gender.setText("-");
                                    hideCard(genderCard);
                                }
                            } else {
                                gender.setText("-");
                                hideCard(genderCard);
                            }

                            if (isValidString(personDetails.getDob()) && !personDetails.getDob().equals(for_date)) {
                                dobTv.setText(AppConstants.getShowableDate(personDetails.getName(), context));
                            }else if (isValidString(personDetails.getYob())){
                                dobTv.setText(personDetails.getYob());
                            }else {
                                dobTv.setText("-");

                                hideCard(dobCard);
                            }
                            if (isValidString(personDetails.getFatherName())) {
                                fatherNameTv.setText(personDetails.getFatherName());
                            } else {
                                fatherNameTv.setText("-");
                                hideCard(fatherCard);
                            }
                            if (isValidString(details.getPersonDetails().getImgLink())) {

                                JSONObject object1 = new JSONObject();
                                object1.put("type", AppConstants.DOCS.PROFILE);
                                object1.put("link", personDetails.getImgLink());


//                                imageList.add(object1);
                            }
                            if (isValidString(details.getPersonDetails().getIdProof())) {
//                            imageList.put(AppConstants.DOCS.ID_PROOF,details.getPersonDetails().getIdProof());

                                JSONObject object1 = new JSONObject();
                                object1.put("type", AppConstants.DOCS.ID_PROOF);
                                object1.put("link", details.getPersonDetails().getIdProof());
//                                imageList.add(object1);
                            }
                            if (isValidString(details.getPersonDetails().getAddressProof())) {
//                            imageList.put(AppConstants.DOCS.ADDRESS_PROOF,details.getPersonDetails().getAddressProof());
                                JSONObject object1 = new JSONObject();
                                object1.put("type", AppConstants.DOCS.ADDRESS_PROOF);
                                object1.put("link", details.getPersonDetails().getAddressProof());
//                                imageList.add(object1);
                            }
                        }
                        if (details.getFarmsDetails() != null) {
                            Log.e(TAG,"FULL FARM "+new Gson().toJson(details.getFarmsDetails()));

                            if (isValidString(details.getFarmsDetails().getOwnershipDoc())) {
                                {

                                    JSONObject object1 = new JSONObject();
                                    object1.put("type", AppConstants.DOCS.OWNERSHIP);
                                    object1.put("link", details.getFarmsDetails().getOwnershipDoc());
//                                    imageList.add(object1);
                                }
                            }
                            if (isValidString(details.getFarmsDetails().getFarmPhoto())) {
                                JSONObject object1 = new JSONObject();
                                object1.put("type", AppConstants.DOCS.FARM);
                                object1.put("link", details.getFarmsDetails().getFarmPhoto());
//                                imageList.add(object1);
                            }
                        }

                        if (details.getFarmsDetails() != null) {
                            FarmsDetails farmsDetails = details.getFarmsDetails();
                            if (isValidString(farmsDetails.getCompFarmId())) {
                                farmIdTv.setText(farmsDetails.getCompFarmId());
                            } else {
                                farmIdTv.setText("-");
                            }


                        }
                        if (details.getFarmCropDetails() != null) {
                            FarmCropDetails cropDetails = details.getFarmCropDetails();

                            if (cropDetails.getCropName() != null) {
                                currentCropTv.setText(cropDetails.getCropName());
                            } else
                                hideCard(currentCropCard);
                        }
                        if (details.getFarmAddressDetails() != null) {
                            FarmAddressDetails addressDetails = details.getFarmAddressDetails();
                            if (isValidString(addressDetails.getAddL1()) && !addressDetails.getAddL1().equals("0")) {
                                khasraTv.setText(addressDetails.getAddL1());
                            } else {
                                khasraTv.setText("-");
                                hideCard(addlCard);
                            }

                            if (isValidString(addressDetails.getAddL2()) && !addressDetails.getAddL2().equals("0")) {
                                chakTv.setText(addressDetails.getAddL2());
                            } else {
                                chakTv.setText("-");
                                hideCard(addL2Card);
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    /*if (imageList.size() > 0) {
                        DataHandler.newInstance().setImageListDoc(imageList);
                        docImage.setVisibility(View.GONE);
                        docImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(context, DocumentActivity.class));
                            }
                        });
                    } else {
                        docImage.setVisibility(View.GONE);
                    }*/


                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    DataHandler.newInstance().setFarmFullDetails(null);
                    //un authorized access
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    DataHandler.newInstance().setFarmFullDetails(null);
                    // auth token expired
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        DataHandler.newInstance().setFarmFullDetails(null);
                        error = response1.errorBody().string().toString();
                        Log.e(TAG, " Error FULL " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FarmFullDetails> call, Throwable t) {
                Log.e(TAG, "Failure FULL " + t.toString());
                DataHandler.newInstance().setFarmFullDetails(null);
            }
        });


    }
    @BindView(R.id.farm_details_currentCrop)
    TextView currentCropTv;



}