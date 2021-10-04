package com.i9930.croptrails.Dashboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.FarmerInnerDashBoard.FrmrInnrDtlsPagerActivity;
import com.i9930.croptrails.Landing.Fragments.DashboardMapModel.LockableScrollView;
import com.i9930.croptrails.Landing.Fragments.DashboardMapModel.MapData;
import com.i9930.croptrails.Landing.Fragments.DashboardMapModel.MapResponse;
import com.i9930.croptrails.Landing.Fragments.DashboardMapModel.MultiTouchMapFragment;
import com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel.CompName;
import com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel.DashboardData;
import com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel.DashboardResponse;
import com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel.LastVisitedFarm;
import com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel.MaximumPopulation;
import com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel.MaximumStandingArea;
import com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel.NoOfFarmsInACluster;
import com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel.SetCompIdClusterId;
import com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel.TotalExpectedArea;
import com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel.TotalStandingArea;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback {

    DashboardActivity context ;
    String TAG="DashboardActivity";
    Toolbar mActionBarToolbar;
    Resources resources;
    private String mParam1;
    private String mParam2;
    BarChart chart;
    private GoogleMap mMap;
    private TextView totalNoOfFarms;
    private TextView totalStandingArea;
    private TextView farmerName3;
    private TextView lotNo3;
    private TextView fatherName3;
    private TextView village3;
    private TextView farmerName4;
    private TextView lotNo4;
    private TextView fatherName4;
    private TextView village4;
    // private ScrollView dashboardScrollview;
    private LockableScrollView dashboardScrollview;
    private TextView area4;
    private TextView date3;
    boolean connected = false;
    RelativeLayout relativeLayout;
    TextView unitTv1;
    TextView unitTv2;
    ConstraintLayout last_visited_farm_layout;
    ConstraintLayout total_farms_layout;

    private TextView expectedStdArea;

    private TextView area6;
    private TextView lotNo6;
    private TextView farmerName6;
    private TextView fatherName6;
    private TextView village6;
    private GifImageView progressBar;
    private LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation;
    MultiTouchMapFragment mapFragment;
    RelativeLayout map_rel_lay;
    Call<DashboardResponse> callBarChart;
    Call<MapResponse> callFarmCoordinates;
    ConnectivityManager connectivityManager;
    View view;

    View connectivityLine;
    private void initView(){
        connectivityLine=findViewById(R.id.connectivityLine);
        unitTv1 = findViewById(R.id.textView5);
        unitTv2 = findViewById(R.id.textViewA);
        chart = findViewById(R.id.chart);
        totalNoOfFarms = findViewById(R.id.no_of_farms1);
        totalStandingArea = findViewById(R.id.total_standing_area2);
        farmerName3 = findViewById(R.id.farmer_name3);
        lotNo3 = findViewById(R.id.lot_no3);
        fatherName3 = findViewById(R.id.father_name3);
        village3 = findViewById(R.id.village3);
        progressBar = findViewById(R.id.progressBarDash);
        relativeLayout = findViewById(R.id.no_internet_layout);
        farmerName4 = findViewById(R.id.farmer_name4);
        lotNo4 = findViewById(R.id.lot_no4);
        fatherName4 = findViewById(R.id.father_name4);
        village4 = findViewById(R.id.village4);
        dashboardScrollview = findViewById(R.id.dashboard_scrollview);
        area4 = findViewById(R.id.area4);
        date3 = findViewById(R.id.date3);
        expectedStdArea = findViewById(R.id.expected_stdarea1);
        lotNo6 = findViewById(R.id.lot_no6);
        farmerName6 = findViewById(R.id.farmer_name6);
        fatherName6 = findViewById(R.id.father_name6);
        village6 = findViewById(R.id.village6);
        area6 = findViewById(R.id.area6);
        map_rel_lay = findViewById(R.id.map_rel_lay);
        last_visited_farm_layout = findViewById(R.id.last_visited_farm_layout);
        total_farms_layout = findViewById(R.id.total_farms_layout);

        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mapFragment = (MultiTouchMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_dashboard);

        int height = getResources().getDisplayMetrics().heightPixels;
        map_rel_lay.getLayoutParams().height = (height / 2);
        mapFragment.getMapAsync(this);
        unitTv1.setText("(" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL) + ")");
        unitTv2.setText("(" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL) + ")");


        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
        if (connected) {

            relativeLayout.setVisibility(View.GONE);
            if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.FARMER)) {
                loadBarChartAndCardFarmer();
            } else {
                loadBarChartAndCard();
            }
        } else {
            progressBar.setVisibility(View.GONE);

            relativeLayout.setVisibility(View.VISIBLE);

        }

        total_farms_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    /*activity.loadFragment2(new FarmFragment(SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE),
                            SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE)));*/
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }
    AdView mAdView;
    private void loadAds(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
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
        setContentView(R.layout.activity_dashboard);

        context = this;
        initView();
        loadAds();
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
                DashboardActivity.this.onBackPressed();
            }
        });
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.title_dashboard));
    }


    private void loadBarChartAndCard() {
        //progressBar.setVisibility(View.VISIBLE);
        try {
            String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            SetCompIdClusterId setCompIdClusterId = new SetCompIdClusterId();
            setCompIdClusterId.setCluster_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
            setCompIdClusterId.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
            setCompIdClusterId.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            setCompIdClusterId.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            //API HIT

            callBarChart = apiService.getClusterInsights(setCompIdClusterId);
            final boolean[] isLoaded = {false};
            long currMillis=AppConstants.getCurrentMills();
            Log.e(TAG,"Sending Bar Data "+new Gson().toJson(setCompIdClusterId));
            callBarChart.enqueue(new Callback<DashboardResponse>() {
                @Override
                public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                    String error=null;
                    isLoaded[0]=true;
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                        } else {
                            setChartAndCardData(response);
                        }
                    }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                    } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            progressBar.setVisibility(View.GONE);
                            dashboardScrollview.setVisibility(View.VISIBLE);
                            error=response.errorBody().string();
                            Log.e(TAG, "Dashboard error getClusterInsights " + error);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                        notifyApiDelay(context, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }
                }

                @Override
                public void onFailure(Call<DashboardResponse> call, Throwable t) {
                    // Log error here since request failed
                    long newMillis = AppConstants.getCurrentMills();
                    isLoaded[0]=true;
                    long diff = newMillis - currMillis;
                    notifyApiDelay(context, call.request().url().toString(),
                            "" + diff, internetSPeed, t.toString(), 0);
                    Log.e(TAG, "Dashboard failure bar  " + t.toString());
                    progressBar.setVisibility(View.GONE);
                    dashboardScrollview.setVisibility(View.VISIBLE);
                }
            });
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    internetFlow(isLoaded[0]);
                }
            }, AppConstants.DELAY);
        } catch (Exception e) {
            e.printStackTrace();
            Toasty.error(context, resources.getString(R.string.server_error), Toast.LENGTH_LONG).show();
        }

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
                                        connectivityLine.setBackgroundColor(resources.getColor(color));
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

    private void loadMapData() {
        try {
            String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            String clusterId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID);
            final boolean[] isLoaded = {false};
            long currMillis=AppConstants.getCurrentMills();
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("cluster_id",clusterId);
            jsonObject.addProperty("user_id",userId);
            jsonObject.addProperty("token",token);

            Log.e(TAG,"Send Map Data "+new Gson().toJson(jsonObject));
            callFarmCoordinates = apiService.getMapData(clusterId,
                    userId, token);
            callFarmCoordinates.enqueue(new Callback<MapResponse>() {
                @Override
                public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                    String error=null;
                    isLoaded[0]=true;
                    if (response.isSuccessful()) {
                        Log.e(TAG, "Map Response " + new Gson().toJson(response.body()));
                        if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                        }  else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                        }else if (response.body().getStatus() == 1) {
                            List<MapData> data = response.body().getData();
                            setMapData(data);
                        } else {

                        }
                    }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                    } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            error=response.errorBody().string().toString();
                            Log.e(TAG, "Map Error " + error);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                        notifyApiDelay(context, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }

                }

                @Override
                public void onFailure(Call<MapResponse> call, Throwable t) {
                    Log.e(TAG, "Map Failure" + t.getMessage());
                    isLoaded[0]=true;
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    notifyApiDelay(context, call.request().url().toString(),
                            "" + diff, internetSPeed, t.toString(), 0);
                }
            });

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    internetFlow(isLoaded[0]);
                }
            }, AppConstants.DELAY);        } catch (Exception e) {
            e.printStackTrace();
            Toasty.error(context, resources.getString(R.string.server_error), Toast.LENGTH_LONG).show();


        }
    }

    private void setMapData(List<MapData> data) {
        final ArrayList<Marker> markers = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getLatCord() != null && data.get(i).getLongCord() != null && !data.get(i).getLongCord().equals("0") && !data.get(i).getLatCord().equals("0") && data.get(i).getLotNo() != null) {
                LatLng loc = new LatLng(Double.parseDouble(data.get(i).getLatCord().toString()), Double.parseDouble(data.get(i).getLongCord().toString()));
                //mMap.addMarker(new MarkerOptions().position(loc).title(data.get(i).getLotNo()));
                markers.add(mMap.addMarker(new MarkerOptions().position(loc).title(data.get(i).getLotNo())));
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

    private void loadBarChartAndCardFarmer() {
        //progressBar.setVisibility(View.VISIBLE);
        try {
            String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            String personId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
            String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
            String seasonId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SEASON_NUM);
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            JsonObject object=new JsonObject();
            object.addProperty("comp_id",""+compId);
            object.addProperty("person_id",""+personId);
            object.addProperty("sesaon_num",""+seasonId);
            object.addProperty("user_id",""+userId);
            object.addProperty("token",""+token);

            Log.e(TAG,"Farmer Dash send "+new Gson().toJson(object));
            //API HIT
            final boolean[] isLoaded = {false};
            long currMillis=AppConstants.getCurrentMills();
            callBarChart = apiService.getFarmerDashboardData(compId, personId, seasonId, userId, token);

            callBarChart.enqueue(new Callback<DashboardResponse>() {
                @Override

                public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                    String error=null;
                    isLoaded[0]=true;
                    Log.e(TAG,"getFarmerDashboardData "+response.code());
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                        } else {
                            setChartAndCardData(response);
                        }
                    }  else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                    } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                    }else {
                        try {
                            progressBar.setVisibility(View.GONE);
                            dashboardScrollview.setVisibility(View.VISIBLE);
                            error=response.errorBody().string();
                            Log.e(TAG, "getFarmerDashboardDataDashboard error " + error);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                        notifyApiDelay(context, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }
                }

                @Override
                public void onFailure(Call<DashboardResponse> call, Throwable t) {
                    // Log error here since request failed
                    long newMillis = AppConstants.getCurrentMills();
                    isLoaded[0]=true;
                    long diff = newMillis - currMillis;
                    notifyApiDelay(context, call.request().url().toString(),
                            "" + diff, internetSPeed, t.toString(), 0);
                    Log.e(TAG, "getFarmerDashboardData Dashboard failure bar famer " + t.toString());
                    progressBar.setVisibility(View.GONE);
                    dashboardScrollview.setVisibility(View.VISIBLE);

                }
            });

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    internetFlow(isLoaded[0]);
                }
            }, AppConstants.DELAY);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Catch Bar And Chart " + e.toString());
            Toasty.error(context, resources.getString(R.string.server_error), Toast.LENGTH_LONG).show();
        }


    }

    private void loadMapDataFarmer() {
        try {
            String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            final boolean[] isLoaded = {false};
            long currMillis=AppConstants.getCurrentMills();
            callFarmCoordinates = apiService.getFarmerMapData(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID),
                    userId, token);
            callFarmCoordinates.enqueue(new Callback<MapResponse>() {
                @Override
                public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                    String error=null;
                    isLoaded[0]=true;
                    if (response.isSuccessful()) {
                        Log.e(TAG, "Map Response Farmer " + new Gson().toJson(response.body()));
                        if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                        } else if (response.body().getStatus() == 1) {
                            List<MapData> data = response.body().getData();
                            setMapData(data);
                        } else {

                        }
                    }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                    } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            error=response.errorBody().string().toString();
                            Log.e(TAG, "Map Error getFarmerMapData " + error);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() ==500) {
                        notifyApiDelay(context, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }
                }

                @Override
                public void onFailure(Call<MapResponse> call, Throwable t) {
                    long newMillis = AppConstants.getCurrentMills();
                    isLoaded[0]=true;
                    long diff = newMillis - currMillis;
                    notifyApiDelay(context, call.request().url().toString(),
                            "" + diff, internetSPeed, t.toString(), 0);
                    Log.e(TAG, "Map Failure" + t.getMessage());

                }
            });

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    internetFlow(isLoaded[0]);
                }
            }, AppConstants.DELAY);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Catch " + e.toString());
            Toasty.error(context, resources.getString(R.string.server_error), Toast.LENGTH_LONG).show();


        }
    }

    private void setChartAndCardData(Response<DashboardResponse> response) {
        final List<BarEntry> entries = new ArrayList<>();
        Log.e(TAG, "Dashboard Response " + new Gson().toJson(response.body()));
        DecimalFormat decimalFormat = new DecimalFormat("##.#");
        NoOfFarmsInACluster noOfFarmsInACluster = response.body().getNoOfFarmsInACluster();
        TotalStandingArea totalStandingAreaa = response.body().getTotalStandingArea();
        LastVisitedFarm lastVisitedFarm = response.body().getLastVisitedFarm();
        MaximumStandingArea maximumStandingArea = response.body().getMaximumStandingArea();
        TotalExpectedArea expectedArea = response.body().getTotalExpectedArea();
        MaximumPopulation maximumPopulation = response.body().getMaximumPopulation();
        CompName compName = response.body().getCompName();

        if (compName.getCompanyName() != null) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMPANY_NAME, compName.getCompanyName());
            LandingActivity.setCompName(compName.getCompanyName());
        }
        if (lastVisitedFarm != null) {
            if (lastVisitedFarm.getName() != null) {
                farmerName3.setText(lastVisitedFarm.getName());
            }

            if (lastVisitedFarm.getLotNo() != null) {
                lotNo3.setText(resources.getString(R.string.lot_no) + " " + lastVisitedFarm.getLotNo());
            }
            if (lastVisitedFarm.getFatherName() != null) {
                fatherName3.setText((lastVisitedFarm.getFatherName()));
            }
            if (lastVisitedFarm.getVillageOrCity() != null && lastVisitedFarm.getState() != null) {
                village3.setText(lastVisitedFarm.getVillageOrCity() + " " + lastVisitedFarm.getState());
            }
            if (lastVisitedFarm.getDoa() != null) {


                last_visited_farm_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, lastVisitedFarm.getFarmId());
                        Intent intent = null;

                        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.FARMER)) {
                            intent = new Intent(context, FrmrInnrDtlsPagerActivity.class);
                        } else {
                            intent = new Intent(context, TestActivity.class);
                        }

                        //Intent intent = new Intent(context, FarmDashboard.class);
                        ActivityOptions options = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            context.startActivity(intent, options.toBundle());
                        } else {
                            context.startActivity(intent);
                        }
                    }
                });

                String date = lastVisitedFarm.getDoa().substring(0, 10);
                try {
                    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                    Date d = input.parse(date);
                    SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
                    date3.setText(output.format(d));
                } catch (Exception e) {
                    Log.e("dateError", e.getMessage());
                }
            }
        } else {
            // No Last Visited farm data vaialable
        }

        if (maximumStandingArea != null) {
            if (maximumStandingArea.getName() != null) {
                farmerName4.setText(maximumStandingArea.getName());
            }
            if (maximumStandingArea.getLotNo() != null) {
                lotNo4.setText(resources.getString(R.string.framer_lot_no) + maximumStandingArea.getLotNo());
            }
            if (maximumStandingArea.getFatherName() != null) {
                fatherName4.setText(maximumStandingArea.getFatherName());
            }
            if (maximumStandingArea.getVillageOrCity() != null && maximumStandingArea.getState() != null) {
                village4.setText(maximumStandingArea.getVillageOrCity() + " " + maximumStandingArea.getState());
            }
            if (maximumStandingArea.getSum() != null) {
                Double d = Double.parseDouble(maximumStandingArea.getSum());
                double roundOff = (double) Math.round(d * 100) / 100;

                area4.setText(roundOff + "ac.");
            }
        }
        if (expectedArea != null && expectedArea.getSum() != null) {
           /* Double t = Double.parseDouble(expectedArea.getSum());
            double roundOff = (double) Math.round(t * 100) / 100;*/
            Log.e(TAG, "Total Expected Area In Acres " + expectedArea.getSum());
            if (expectedArea.getSum() != null)
                expectedStdArea.setText(AppConstants.getShowableArea(expectedArea.getSum()));
            else
                expectedStdArea.setText("0");
        }else {
            expectedStdArea.setText("0");
        }
        if (maximumPopulation != null) {
            if (maximumPopulation.getLotNo() != null) {
                lotNo6.setText(resources.getString(R.string.framer_lot_no) + maximumPopulation.getLotNo());
            }
            if (maximumPopulation.getName() != null) {
                farmerName6.setText(maximumPopulation.getName());
            }
            if (maximumPopulation.getFatherName() != null) {
                fatherName6.setText(maximumPopulation.getFatherName());
            }
            if (maximumPopulation.getVillageOrCity() != null && maximumPopulation.getState() != null) {
                village6.setText(maximumPopulation.getVillageOrCity() + " " + maximumPopulation.getState());
            }
            if (maximumPopulation.getSum() != null) {
                DecimalFormat decimalFormat1 = new DecimalFormat("##");
                Double p = Double.parseDouble(maximumPopulation.getSum());
                double roundOffArea = (double) Math.round(p * 100) / 100;
                area6.setText(decimalFormat1.format(roundOffArea));
            }
        }
        if (totalStandingAreaa != null) {
            Double sum = 0.0;
            Double total_farms = 0.0;
            if (totalStandingAreaa.getSum() != null && !TextUtils.isEmpty(totalStandingAreaa.getSum()))
                sum = Double.parseDouble(totalStandingAreaa.getSum());
            if (noOfFarmsInACluster != null && noOfFarmsInACluster.getCount() != null && !TextUtils.isEmpty(noOfFarmsInACluster.getCount()))

                total_farms = Double.parseDouble(noOfFarmsInACluster.getCount());
            //DecimalFormat decimalFormat=new DecimalFormat("##.##");
            double roundOff = (double) Math.round(sum * 100) / 100;
            double roundOfftotalFarms = (double) Math.round(total_farms * 100) / 100;
            Log.e(TAG, "Total Standng Area In Ares " + totalStandingAreaa.getSum());
            totalNoOfFarms.setText(decimalFormat.format(roundOfftotalFarms));
            if (totalStandingAreaa.getSum() != null)
                totalStandingArea.setText(AppConstants.getShowableArea(totalStandingAreaa.getSum()));
            else
                totalStandingArea.setText("0");

        }

        List<DashboardData> dataa = response.body().getGrade();
        entries.clear();

        Log.e(TAG, "BarChartData " + new Gson().toJson(dataa));
        if (dataa != null) {
            entries.add(new BarEntry(0f, 0f));
            entries.add(new BarEntry(1f, 0f));
            entries.add(new BarEntry(2f, 0f));
            entries.add(new BarEntry(3f, 0f));


            for (int i = 0; i < dataa.size(); i++) {
                if (dataa.get(i).getGrade().equals("A")) {
                    entries.set(0, new BarEntry(0f, Float.parseFloat(dataa.get(i).getCount())));
                } else if (dataa.get(i).getGrade().equals("B")) {
                    entries.set(1, new BarEntry(1f, Float.parseFloat(dataa.get(i).getCount())));
                } else if (dataa.get(i).getGrade().equals("C")) {
                    entries.set(2, new BarEntry(2f, Float.parseFloat(dataa.get(i).getCount())));
                } else if (dataa.get(i).getGrade().equals("D")) {
                    entries.set(3, new BarEntry(3f, Float.parseFloat(dataa.get(i).getCount())));
                }
            }

            BarDataSet set = new BarDataSet(entries, resources.getString(R.string.farmer_no_of_farms));
            set.setValueTextSize(8);

            set.setColors(ColorTemplate.MATERIAL_COLORS);
            BarData data = new BarData(set);
            data.setBarWidth(0.7f); // set custom bar width
            chart.setData(data);
            Description description = new Description();
            description.setText("");
            chart.setFitBars(true);
            chart.setDescription(description);
            chart.setBackgroundColor(Color.WHITE);
            chart.setTouchEnabled(false);
            chart.setHorizontalScrollBarEnabled(true);// make the x-axis fit exactly all bars
            chart.invalidate(); // refresh
            progressBar.setVisibility(View.GONE);
            dashboardScrollview.setVisibility(View.VISIBLE);
            Log.e("Grades info ", String.valueOf(dataa.size()));
            final ArrayList<String> xAxisLabel = new ArrayList<>();
            xAxisLabel.add("A");
            xAxisLabel.add("B");
            xAxisLabel.add("C");
            xAxisLabel.add("D");
            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularityEnabled(true);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return xAxisLabel.get((int) value);
                }
            });
        }
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
        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.FARMER)) {
            loadMapDataFarmer();
        } else {
            loadMapData();
        }


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
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

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




}