
package com.i9930.croptrails.Maps.ShowArea;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.maps.android.SphericalUtil;

import com.i9930.croptrails.Maps.ShowArea.Model.geojson.GeoJsonResponse;
import com.i9930.croptrails.Maps.VerifyArea.MapsActivity;
import com.i9930.croptrails.Maps.VerifyArea.MapsActivityEasyMode;
import com.i9930.croptrails.Maps.VerifyArea.MapsActivitySamunnati;

import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPoint;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;
import com.tooltip.Tooltip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.LineSegmentLineSegmentIntersection;
import com.i9930.croptrails.Maps.MapsActivityNew;
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;
import com.i9930.croptrails.Maps.ShowArea.Model.OmiFetchDatum;
import com.i9930.croptrails.Maps.ShowArea.Model.OmitanceShowResponse;
import com.i9930.croptrails.Maps.ShowArea.Model.WayPointFetchDatum;
import com.i9930.croptrails.Maps.ShowArea.Model.WayPointFetchResponse;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.SendOmtanceArea;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.VerifySendData;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.WayPoint;
import com.i9930.croptrails.Maps.WayPoint.AddWayPointActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Maps.WalkAround.MapsActivityNew.getBitmapFromVectorDrawable;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;
import static com.i9930.croptrails.Utility.AppConstants.getCoordinatesFromGeometry;

public class ShowAreaWithUpdateActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    double area_mult_factor = 1.0;
    double area_mult_factor_def = 0.000247105;
    private GoogleMap mMap;
    String TAG = "ShowAreaWithUpdateActivity";
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    LocationManager locationManager;
    Toolbar mActionBarToolbar;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Context context;
    FusedLocationProviderClient mFusedLocationClient;
    Double[] latPoints;
    Double[] longPoints;
    String[] lat, lng;
    int i = 0;
    Button view_area_button;
    TextView tvarea;
    Boolean onclick = false;
    TextView butt_clear;
    String mprovider;
    Location location;
    // Button next_butt;
    GifImageView progressBar;
    Boolean can_add = true;
    @BindView(R.id.linear_lay_for_map)
    LinearLayout linear_lay_for_map;
    @BindView(R.id.back_to_farm)
    CircleImageView back_to_farm;
    TextView title;
    Resources resources;
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();
    TextView latTv, lngTv, zoomTv;
    @BindView(R.id.submit_area_butt)
    Button submit_area_butt;
    @BindView(R.id.normal_mode_butt)
    Button normal_mode_butt;
    @BindView(R.id.areaUnitTv)
    TextView areaUnitTv;
    @BindView(R.id.mapTypeImage)
    CircleImageView mapTypeImage;
    int mapChoosen = 101;
    String farmLatitude, farmLongitude;
    ImageView helpImage;

    private interface MAP_TYPE {
        public static final int DEFAULT_MAP = 101;
        public static final int SATELLITE_MAP = 102;
    }

    String msg = "1) Please use the pin drops to mark the farm corners.\n" +
            "2) follow a circular (clockwise or anticlockwise) pattern to mark them, don't mark random corners of the farm.\n" +
            "3) If you start with a corner, please note that you should close the formation and end at the same corner, marking with another dropped pin.";
    Tooltip tooltip;
    //    private Map<String, MarkerOptions> mMarkers = new ConcurrentHashMap<String, MarkerOptions>();
    int pageVisitCount = 0;
    LatLng firstAndLastPoint;
    boolean isFromOther = false;

    private void initViews() {
        title = (TextView) findViewById(R.id.tittle);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        progressBar = findViewById(R.id.progressBar_cyclic);
        tvarea = (TextView) findViewById(R.id.area_tv);
        view_area_button = findViewById(R.id.submit_butt);
        butt_clear = findViewById(R.id.clear_butt);
        latTv = findViewById(R.id.latTv);
        lngTv = findViewById(R.id.lngTv);
        zoomTv = findViewById(R.id.zoomTv);
        helpImage = findViewById(R.id.helpImage);
        pageVisitCount = SharedPreferencesMethod2.getInt(context, SharedPreferencesMethod2.MAP_VISIT_COUNT);
        tooltip = new Tooltip.Builder(helpImage)
                .setText(msg)
                .setBackgroundColor(resources.getColor(R.color.white))
                .setTextColor(resources.getColor(R.color.black))
                .setCancelable(true).build();
        if (pageVisitCount <= 3) {


//            tooltip.show();
            pageVisitCount++;
            SharedPreferencesMethod2.setInt(context, SharedPreferencesMethod2.MAP_VISIT_COUNT, pageVisitCount);
        }

        helpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(true);

            }
        });
        if (isFromOther)
            helpImage.setVisibility(View.GONE);
        hideButtons();
    }

    private void hideButtons() {
        butt_clear.setVisibility(View.GONE);
        view_area_button.setVisibility(View.GONE);
        submit_area_butt.setVisibility(View.GONE);
        isEditing = false;
    }


    String internetSPeed = "";
    @BindView(R.id.connectivityLine)
    View connectivityLine;

    private void internetFlow(boolean isLoaded) {
        try {

            if (!isLoaded) {
                if (!ConnectivityUtils.isConnected(context)) {
                    AppConstants.failToast(context, resources.getString(R.string.check_internet_connection_msg));
                } else {
                    ConnectivityUtils.checkSpeedWithColor(ShowAreaWithUpdateActivity.this, new ConnectivityUtils.ColorListener() {
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

    /* @BindView(R.id.myLocationCard)
     CardView myLocationCard;*/
    @BindView(R.id.myLocationImg)
    CircleImageView myLocationImg;
    @BindView(R.id.kasraAndFarmerLayout)
    LinearLayout kasraAndFarmerLayout;
    @BindView(R.id.farmerNameLayout)
    LinearLayout farmerNameLayout;
    @BindView(R.id.khasraLayout)
    LinearLayout khasraLayout;
    @BindView(R.id.farmerNameLabel)
    TextView farmerNameLabel;
    @BindView(R.id.farmerNamrTv)
    TextView farmerNamrTv;
    @BindView(R.id.kasraLabelTv)
    TextView khasraLabelTv;
    @BindView(R.id.khasraTv)
    TextView khasraTv;

    private void setFarmerNameAndKhasra() {
        String farmerName = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARMER_NAME);
        String khasra = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.KHASRA);

        if (!AppConstants.isValidString(farmerName) && !AppConstants.isValidString(khasra)) {
            kasraAndFarmerLayout.setVisibility(View.GONE);
        } else {
            if (!AppConstants.isValidString(khasra)) {
                khasraLayout.setVisibility(View.GONE);
            } else {
                khasraLabelTv.setText(resources.getString(R.string.khasra) + ":");
                khasraTv.setText(khasra);
            }

            if (!AppConstants.isValidString(farmerName)) {
                farmerNameLayout.setVisibility(View.GONE);
            } else {
                farmerNameLabel.setText(resources.getString(R.string.farmer_name_hint) + ":");
                farmerNamrTv.setText(farmerName);
            }

        }

        myLocationImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLastLocation != null) {
                    LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                    mMap.animateCamera(cameraUpdate);
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                }
            }
        });

       /* myLocationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLastLocation != null) {
                    LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                    mMap.animateCamera(cameraUpdate);
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                }
            }
        });*/
    }

    @BindView(R.id.fabParent)
    FloatingActionMenu fabParent;
    @BindView(R.id.fabChildAddOmittanceArea)
    FloatingActionButton fabChildAddOmittanceArea;
    @BindView(R.id.fabChildWayPoint)
    FloatingActionButton fabChildWayPoint;
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
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_area_with_update);
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        context = this;
        loadAds();
        isFromOther = getIntent().getBooleanExtra("fromOther", false);
        bmp = getBitmapFromVectorDrawable(context, R.drawable.marker_green_24px);
        ButterKnife.bind(this);
        initViews();
        back_to_farm.setImageResource(R.drawable.farm_icon_map);
        boolean isWayPointEnabled = SharedPreferencesMethod.getBoolean(this, SharedPreferencesMethod.IS_WAY_POINT_ENABLED);
        boolean isOmitanceArea = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_OMITANCE_AREA_ENABLED);
//        isOmitanceArea=false;
        if (isOmitanceArea || isWayPointEnabled) {
            if (isWayPointEnabled) {
                fabChildWayPoint.setVisibility(View.VISIBLE);
                fabChildWayPoint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (fabParent.isOpened())
                            fabParent.close(true);
                        startActivityForResult(new Intent(context, AddWayPointActivity.class), 1010);
                    }
                });
            } else
                fabChildWayPoint.setVisibility(View.GONE);
            if (isOmitanceArea) {
                fabChildAddOmittanceArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if
                        (fabParent.isOpened())
                            fabParent.close(true);
                        showDialog(false);
                    }
                });
            } else {
                fabChildAddOmittanceArea.setVisibility(View.GONE);
            }

        } else {
            fabParent.setVisibility(View.GONE);
        }

        //        title.setText(resources.getString(R.string.check_area_title));
        title.setText(resources.getString(R.string.farm_area_title));
        areaUnitTv.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL));
        area_mult_factor = SharedPreferencesMethod.getDoubleSharedPreferences(context, SharedPreferencesMethod.AREA_MULTIPLICATON_FACTOR);
       /* if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL).toLowerCase().equals("hectare")) {
            multiplicationFactor = 2.47105;
        } else if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL).toLowerCase().equals("malwa_bigha")) {
            multiplicationFactor = 3.63;
        } else {

        }*/
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setFarmerNameAndKhasra();
        Intent intent = getIntent();
        boolean canUpdate = intent.getBooleanExtra("update", false);
        if (canUpdate) {
            helpImage.setVisibility(View.VISIBLE);
        } else {
            helpImage.setVisibility(View.GONE);
        }
//        progressBar.setVisibility(View.VISIBLE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mprovider = locationManager.getBestProvider(criteria, false);
        //gpslocation=(TextView)findViewById(R.id.gps_location);
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locationManager.getLastKnownLocation(mprovider);
        }
        latPoints = new Double[100];
        longPoints = new Double[100];
        onClicks();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (intent != null) {
            farmLatitude = intent.getStringExtra("farmLat");
            farmLongitude = intent.getStringExtra("farmLon");
        }

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableGPS();

        } else {
        }
    }

    public float calculationByDistance(LatLng StartP, LatLng EndP) {
        float[] results1 = new float[1];
        Location.distanceBetween(StartP.latitude, StartP.longitude, EndP.latitude, EndP.longitude, results1);
        float distanceInMetersgpsc1 = results1[0];
        return distanceInMetersgpsc1;
    }

    private void onClicks() {
        submit_area_butt.setVisibility(View.GONE);
        view_area_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i > 2) {
                    /*float distance=calculationByDistance(latLngList.get(0),latLngList.get(latLngList.size()-1));//In meters
                    if (distance<7) {*/
                    submit_area_butt.setVisibility(View.GONE);
                    if (UCCpolygon != null)
                        UCCpolygon.remove();
//            onAreaClicked = false;
                    mMap.clear();
                    ShowAreaWithUpdateActivity.AsyncTaskRunner taskRunner = new ShowAreaWithUpdateActivity.AsyncTaskRunner();
                    taskRunner.execute();
                    /*}else{
                        Toasty.error(context,"Please add last near 5 meter to the firs point").show();
                    }*/
                } else {
                    // Toast.makeText(context, resources.getString(R.string.please_submit_at_least_3_points), Toast.LENGTH_SHORT).show();
                    Toasty.info(context, resources.getString(R.string.please_submit_at_least_3_points), Toast.LENGTH_SHORT, true).show();

                }
            }
        });

        butt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLast(conflictIndex);
            }
        });

        normal_mode_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, MapsActivityNew.class));
                finish();
            }
        });
        submit_area_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setMessage(resources.getString(R.string.sure_to_submit_area))
                        .setCancelable(false)
                        .setPositiveButton(resources.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (Double.parseDouble(tvarea.getText().toString()) > 0) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    ShowAreaWithUpdateActivity.SubmitAreaAsync asyncTaskRunner = new ShowAreaWithUpdateActivity.SubmitAreaAsync(tvarea.getText().toString());
                                    asyncTaskRunner.execute();
                                } else {
                                    //Toast.makeText(context, "Area is too short to capture.. Please capture again", Toast.LENGTH_LONG).show();
                                    ViewFailDialog failDialog = new ViewFailDialog();
                                    failDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.area_is_too_short_for_capture));
                                }
                            }
                        })
                        .setNegativeButton(resources.getString(R.string.no), null)
                        .show();
            }
        });
    }

    void removeLast(int conflictIndex) {
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        if (i > 0) {
            if (/*conflictIndex == -1*/ true) {
                tvarea.setText("0");
                submit_area_butt.setVisibility(View.GONE);
//                mMarkers.remove("marker" + (i - 1));
                latLngList.remove(latLngList.size() - 1);
//                pointList.remove(latLngList.size() - 1);
                markerList.remove(markerList.size() - 1);
                can_add = true;
                remove();
            } else {
                tvarea.setText("0");
                submit_area_butt.setVisibility(View.GONE);
//                mMarkers.remove("marker" + (conflictIndex + 1));
                latLngList.remove(conflictIndex + 1);
//                pointList.remove(conflictIndex + 1);
                markerList.remove(conflictIndex + 1);
                can_add = true;
                remove();
                this.conflictIndex--;
            }
            i--;

//            Log.e(TAG, "mMarkers =" + mMarkers.size());
//            Log.e(TAG, "pointList =" + pointList.size());
            Log.e(TAG, "latLngList =" + latLngList.size());
            Log.e(TAG, "markerList =" + markerList.size());
            Log.e(TAG, "i =" + i);
        }
//        onAreaClicked = false;
    }


    private class SubmitAreaAsync extends AsyncTask<String, Void, String> {
        String area;
        float zoom = 15.0f;

        public SubmitAreaAsync(String area) {
            super();
            this.area = area;
            zoom = mMap.getCameraPosition().zoom;
        }

        @Override
        protected String doInBackground(String... params) {

            lat = new String[latLngList.size() + 1];
            lng = new String[latLngList.size() + 1];

            for (int j = 0; j < latLngList.size(); j++) {
                lat[j] = String.valueOf(latLngList.get(j).latitude);
                lng[j] = String.valueOf(latLngList.get(j).longitude);
            }
            lat[latLngList.size()] = String.valueOf(firstAndLastPoint.latitude);
            lng[latLngList.size()] = String.valueOf(firstAndLastPoint.longitude);
            //demosupervisor1
            try {
                String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                VerifySendData verifySendData = new VerifySendData();
                verifySendData.setZoom(String.valueOf(zoom));
                verifySendData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                verifySendData.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                verifySendData.setArea(AppConstants.getUploadableArea(area));
                verifySendData.setLat(lat);
                verifySendData.setDeleted_by(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                verifySendData.setLng(lng);
                verifySendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                verifySendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                Call<StatusMsgModel> statusMsgModelCall = apiService.getMsgStatusForVerifyFarm(verifySendData);
                Log.e(TAG, "Sending Data " + new Gson().toJson(verifySendData));
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<StatusMsgModel>() {
                    @Override
                    public void onResponse(Call<StatusMsgModel> call, retrofit2.Response<StatusMsgModel> response) {
                        String error = null;
                        isLoaded[0] = true;
                        if (response.isSuccessful()) {
                            Log.e("Area Response", new Gson().toJson(response.body()));
                            StatusMsgModel statusMsgModel = response.body();
                            if (statusMsgModel.getStatus() == 1) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toasty.success(context, resources.getString(R.string.area_successfully_added), Toast.LENGTH_SHORT, true).show();
                                Intent intent = getIntent();
                                setResult(RESULT_OK);
                                finish();
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.authorization_expired));
                            } else if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, response.body().getMsg());
                            } else if (statusMsgModel.getStatus() == 4) {
                                String msg;
                                if (response.body().getSatMsg() != null && !TextUtils.isEmpty(response.body().getSatMsg()))
                                    msg = response.body().getSatMsg();
                                else
                                    msg = "Invalid area selected, Please select area again.";
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(context, msg);
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_verify_farm_area));
                                //Toast.makeText(context, "Response Error Try again latter", Toast.LENGTH_SHORT).show();
                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            try {
                                error = response.errorBody().string().toString();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_verify_farm_area));
                                Log.e(TAG, "Error " + error);
                                progressBar.setVisibility(View.INVISIBLE);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                            notifyApiDelay(ShowAreaWithUpdateActivity.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                        Log.e(TAG, "Failure " + t.toString());
                        isLoaded[0] = true;
                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        notifyApiDelay(ShowAreaWithUpdateActivity.this, call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_verify_farm_area));
                        progressBar.setVisibility(View.INVISIBLE);
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
                        pointStyle.setIcon(BitmapDescriptorFactory.fromBitmap(bmp));
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

    private class GetGeoJsonData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //demosupervisor1

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
                            }
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.authorization_expired));
                        } else if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
//                                viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, response.body().getMsg());
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
                        viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.authorization_expired));
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
                        notifyApiDelay(ShowAreaWithUpdateActivity.this, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }
                }

                @Override
                public void onFailure(Call<GeoJsonResponse> call, Throwable t) {
                    Log.e(TAG, "GetGeoJsonData Failure " + t.toString());
                    isLoaded[0] = true;
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    notifyApiDelay(ShowAreaWithUpdateActivity.this, call.request().url().toString(),
                            "" + diff, internetSPeed, t.toString(), 0);
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_verify_farm_area));
                    progressBar.setVisibility(View.INVISIBLE);
                }

            });
            /*Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    internetFlow(isLoaded[0]);
                }
            }, AppConstants.DELAY);*/

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


    int conflictIndex = -1;
    int conflictIndex2 = -1;

    private class LinePoint {
        LatLng a;
        LatLng b;

        public LinePoint(LatLng a, LatLng b) {
            this.a = a;
            this.b = b;
        }

        public LatLng getA() {
            return a;
        }

        public void setA(LatLng a) {
            this.a = a;
        }

        public LatLng getB() {
            return b;
        }

        public void setB(LatLng b) {
            this.b = b;
        }
    }

    private void checkImprove(List<ShowAreaWithUpdateActivity.LinePoint> linePointList) {
        boolean notIntersect = true;
        while (notIntersect) {
            for (int i = linePointList.size() - 1; i > 0; i--) {
                boolean flag = LineSegmentLineSegmentIntersection.segmentsIntersect(linePointList.get(i).getA(), linePointList.get(i).getB(),
                        linePointList.get(i - 1).getA(), linePointList.get(i - 1).getB());
                if (flag) {
                    notIntersect = false;
                }
                Log.e(TAG, "INTERSECT one flag " + flag + " on " + i + " index");
            }
        }
    }


    public LatLng findCentroid(List<LatLng> points) {
        int x = 0;
        int y = 0;
        for (LatLng p : points) {
            x += p.latitude;
            y += p.longitude;
        }

        double latitude = x / points.size();
        double longitude = y / points.size();
        LatLng center = new LatLng(latitude, longitude);
        return center;
    }

    public List<LatLng> sortVerticies(List<LatLng> points) {
        // get centroid
        LatLng center = findCentroid(points);
        Collections.sort(points, (a, b) -> {
            double a1 = (Math.toDegrees(Math.atan2(a.latitude - center.latitude, a.longitude - center.longitude)) + 360) % 360;
            double a2 = (Math.toDegrees(Math.atan2(b.latitude - center.latitude, b.longitude - center.longitude)) + 360) % 360;
            return (int) (a1 - a2);
        });
        return points;
    }


    void checkIntersection4() {
        /*if (latLngList.size() > 3) {
            latLngList=sortVerticies(latLngList);
        }*/

        checkIntersection3();
    }

    boolean checkIntersection3() {
       /* boolean flag = false;
        boolean flag2 = false;
        if (latLngList.size() > 3) {
            {
                LatLng pt11 = latLngList.get(0);
                LatLng pt22 = latLngList.get(latLngList.size() - 1);
                int lengthh = latLngList.size() - 2;
                conflictIndex2 = -1;
                for (int i = 1; i < lengthh; i++) {
                    if (i < lengthh) {
                        LatLng ptA = latLngList.get(i);
                        LatLng ptB = latLngList.get(i + 1);
                        Log.e(TAG, "INTERSECT SECOND point A  " +new Gson().toJson(ptA));
                        Log.e(TAG, "INTERSECT SECOND point B " + new Gson().toJson(ptB));
                        flag2 = LineSegmentLineSegmentIntersection.segmentsIntersect(pt11, pt22, ptA, ptB);
                        Log.e(TAG, "INTERSECT SECOND  " + flag2 + " at index " + i);
                        if (flag2) {
                            conflictIndex2 = i;
                            conflictIndex2++;
                            LatLng lastLatLng = latLngList.get(latLngList.size() - 1);
                            latLngList.add(conflictIndex2, lastLatLng);
                            latLngList.remove(latLngList.size() - 1);
                            break;
                        }
                    }
                }
            }
            if (!flag2) {
                flag=false;
                LatLng pt1 = latLngList.get(latLngList.size() - 2);
                LatLng pt2 = latLngList.get(latLngList.size() - 1);
                int length = latLngList.size() - 2;
                conflictIndex = -1;
                for (int l = 0; l < length; l++) {
                    if (l < length-1) {
                        LatLng ptA = latLngList.get(l);
                        LatLng ptB = latLngList.get(l + 1);
                        flag = LineSegmentLineSegmentIntersection.segmentsIntersect(pt1, pt2, ptA, ptB);
                        Log.e(TAG, "INTERSECT 1 at i:" + l + " and flag:" + flag + " AND length:" + length);
                        if (flag) {
                            conflictIndex = l ;
                            conflictIndex++;
                            LatLng lastLatLng = latLngList.get(latLngList.size() - 1);
                            latLngList.add(conflictIndex, lastLatLng);
                            latLngList.remove(latLngList.size() - 1);
                            Log.e(TAG, "INTERSECT FIRSt SEt  " + l + " TO " + (conflictIndex ) + " flag= " + flag + " And i" + l);
                            break;
                        }
                    }
                }
            }
        }*/
        locationMap.put(latLngList.size(), latLngList);
        Log.e(TAG, "INTERSECT MAP LOCAT2 " + new Gson().toJson(latLngList));
        if (UCCpolygon != null)
            UCCpolygon.remove();
        mMap.clear();
        getArea();
//        return flag;
        return true;
    }


    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    //    boolean onAreaClicked = false;
    boolean isEditing = false;

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapChoosen = ShowAreaWithUpdateActivity.MAP_TYPE.DEFAULT_MAP;
        mapTypeImage.setImageResource(R.drawable.map_type_satellite);
        mapTypeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapChoosen == ShowAreaWithUpdateActivity.MAP_TYPE.DEFAULT_MAP) {
                    mapChoosen = ShowAreaWithUpdateActivity.MAP_TYPE.SATELLITE_MAP;
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//                    mapTypeImage.setImageResource(R.drawable.map_type_satellite);
                    mapTypeImage.setImageResource(R.drawable.map_type_normal);
                } else {
                    mapChoosen = ShowAreaWithUpdateActivity.MAP_TYPE.DEFAULT_MAP;
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mapTypeImage.setImageResource(R.drawable.map_type_satellite);
                }
            }
        });

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {

            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                if (isEditing) {
                    mMap.getUiSettings().setScrollGesturesEnabled(true);
                    {
                        latLngList.add(point);
                        i = latLngList.size();
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(point)
                                // .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_my_location))
                                .title("marker" + (i))
                                .draggable(true);
//                    mMarkers.put("marker" + (i), markerOptions);
                        Marker marker = mMap.addMarker(markerOptions);
                        markerList.add(marker);

                        if (latLngList.size() > 2) {
                            checkIntersection4();

                        } else {
                            pointMap.put(latLngList.size(), latLngList);
                            locationMap.put(latLngList.size(), latLngList);
                        }

                    /*if (latLngList.size()>3) {
                        List<LinePoint> linePointList=new ArrayList<>();
                        for(int i=0;i<latLngList.size()-1;i++){
                            LinePoint linePoint=new LinePoint(latLngList.get(i),latLngList.get(i+1));
                            linePointList.add(linePoint);
                        }
                        checkImprove(linePointList);
                    }*/
//                    Log.e(TAG, "mMarkers =" + mMarkers.size());
//                    Log.e(TAG, "pointList =" + pointList.size());
                        Log.e(TAG, "latLngList =" + latLngList.size());
                        Log.e(TAG, "markerList =" + markerList.size());
                        Log.e(TAG, "i =" + i);
                    }

                /*} else {
                    Toasty.info(context, resources.getString(R.string.clear_area_to_add_point), Toast.LENGTH_LONG).show();
                }*/
                }
            }
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub


                Log.d("CheckAreaMapActivity", "onMarkerDragStart..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub

                for (int i = 0; i < markerList.size(); i++) {
                    if (markerList.get(i).equals(arg0)) {
                        Log.e(TAG, "Got marker in list at " + i);
                        LatLng latLng = new LatLng(arg0.getPosition().latitude, arg0.getPosition().longitude);
                        latLngList.set(i, latLng);
//                        LatLng pt = new LatLng(latLng.latitude, latLng.longitude);
//                        pointList.set(i, pt);
                        mMap.clear();
                        ShowAreaWithUpdateActivity.AsyncTaskRunner taskRunner = new ShowAreaWithUpdateActivity.AsyncTaskRunner();
                        taskRunner.execute();
                        break;
                    }
                }

                Log.e(TAG, "Update Lat Long " + new Gson().toJson(latLngList));
                Log.d("CheckAreaMapActivity", "onMarkerDragEnd..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);

                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.i("System out", "onMarkerDrag...");
            }
        });


//        AsyncTaskRunnerOmitance asyncTaskRunnerOmitance = new AsyncTaskRunnerOmitance();
//        asyncTaskRunnerOmitance.execute();
//        AsyncTaskRunnerWayPoint asyncTaskRunnerWayPoint = new AsyncTaskRunnerWayPoint();
//        asyncTaskRunnerWayPoint.execute();

        GetGeoJsonData getGeoJsonData = new GetGeoJsonData();
        getGeoJsonData.execute();
    }

    private void prepareData(LatLngBounds.Builder builder, List<LatLng> latLngs) {

        latLngList.clear();
        latLngList.addAll(latLngs);
        DataHandler.newInstance().setLatLngsFarm(latLngList);
        i = latLngList.size();

        if (i > 0) {
            final LatLng latLng = this.latLngList.get(0);
            back_to_farm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showDialog();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                    //  mMap.setLatLngBoundsForCameraTarget();
                }
            });

            Log.e(TAG, "computeArea " + SphericalUtil.computeArea(latLngs));
            Log.i(TAG, "AREA " + SphericalUtil.computeArea(latLngs));
            String a = String.valueOf(String.format("%.4f", SphericalUtil.computeArea(latLngs) * 0.000247105));
            Log.e(TAG, "Area in Acres " + a);
            Log.e(TAG, "Multiplication Factor " + area_mult_factor);
            String ar = AppConstants.getShowableAreaWithConversion(a, area_mult_factor);
            tvarea.setText(ar);
//            showArea(i);

        }


    }

    Map<Integer, List<LatLng>> pointMap = new HashMap<>();
    Map<Integer, List<LatLng>> locationMap = new HashMap<>();

    private void getArea() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        markerList = new ArrayList<>();
        for (int i = 0; i < latLngList.size(); i++) {
            LatLng latlngforbound = latLngList.get(i);
            builder.include(latlngforbound);
            int finalI = i;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MarkerOptions options = new MarkerOptions()
                            .position(latLngList.get(finalI))
                            .title("marker" + (finalI + 1))
                            .draggable(true);
                    Marker marker = mMap.addMarker(options);
                    markerList.add(marker);

                }
            });
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                submit_area1();
            }
        });

    }

    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        public AsyncTaskRunner() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            markerList = new ArrayList<>();
            for (int i = 0; i < latLngList.size(); i++) {
                LatLng latlngforbound = latLngList.get(i);
                builder.include(latlngforbound);
                int finalI = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MarkerOptions options = new MarkerOptions()
                                .position(latLngList.get(finalI))
                                // .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_my_location))
                                .title("marker" + (finalI + 1))
                                .draggable(false);
                        Marker marker = mMap.addMarker(options);
                        markerList.add(marker);

                    }
                });
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    submit_area1();
                }
            });
            ShowAreaWithUpdateActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 45));
                    showArea();
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

    Polygon UCCpolygon;
    PolygonOptions options;

    private void showArea(int i) {

        if (i < 3) {
            //Toast.makeText(context, "Please submit atleast 3 points", Toast.LENGTH_SHORT).show();
            ViewFailDialog viewFailDialog = new ViewFailDialog();
            viewFailDialog.showDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.should_have_at_least_two_points_to_display));
        } else {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            LatLng[] latLng = new LatLng[i];
            List<LatLng> latLngs = new ArrayList<>();
            latLngs.clear();
            for (int j = 0; j < latLngList.size(); j++) {
                latLng[j] = latLngList.get(j);
                latLngs.add(latLngList.get(j));
                builder.include(latLngList.get(j));
            }
            try {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngList.get(0), 18));
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
            } catch (Exception e) {

            }
            Log.e(TAG, "computeArea " + SphericalUtil.computeArea(latLngs));
            Log.i(TAG, "AREA " + SphericalUtil.computeArea(latLngs));
            String a = String.valueOf(String.format("%.4f", SphericalUtil.computeArea(latLngs) * 0.000247105));
            Log.e(TAG, "Area in Acres " + a);
            Log.e(TAG, "Multiplication Factor " + area_mult_factor);
            String ar = AppConstants.getShowableAreaWithConversion(a, area_mult_factor);
            tvarea.setText(ar);
            options = new PolygonOptions()
                    .addAll(latLngs)
                    .strokeColor(Color.RED)
                    .strokeWidth(2f)
                    .fillColor(Color.parseColor("#330000FF"));
            UCCpolygon = mMap.addPolygon(options);
        }
    }


    private void showArea() {
        if (i < 3) {
            //Toast.makeText(context, "Please view_area_button atleast 3 points", Toast.LENGTH_SHORT).show();
            ViewFailDialog viewFailDialog = new ViewFailDialog();
            viewFailDialog.showDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.should_have_at_least_two_points_to_display));
        } else {
            LatLng[] latLng = new LatLng[latLngList.size()];
            Log.e(TAG, "Arraylisy Size " + latLngList.size() + " And Array Size " + latLng.length + " And i is " + i);
            for (int j = 0; j < latLngList.size(); j++) {
                latLng[j] = latLngList.get(j);
            }
            UCCpolygon = mMap.addPolygon(new PolygonOptions()
                    .add(latLng)
                    .strokeColor(Color.RED)
                    .fillColor(Color.parseColor("#330000FF")));
            String a = String.valueOf(String.format("%.2f", SphericalUtil.computeArea(latLngList) * 0.000247105));
        }
    }


    private void submit_area1() {
        firstAndLastPoint = latLngList.get(0);
//        latLngList.add(latLngList.get(0));
        latPoints = new Double[latLngList.size() + 1];
        longPoints = new Double[latLngList.size() + 1];
        for (int i = 0; i < latLngList.size(); i++) {
            longPoints[i] = latLngList.get(i).longitude;
            latPoints[i] = latLngList.get(i).latitude;
        }

        longPoints[latLngList.size()] = firstAndLastPoint.latitude;
        latPoints[latLngList.size()] = firstAndLastPoint.latitude;
        if (i < 3) {
            // Toast.makeText(context, resources.getString(R.string.please_submit_at_least_3_points), Toast.LENGTH_SHORT).show();
            Toasty.info(context, resources.getString(R.string.please_submit_at_least_3_points), Toast.LENGTH_SHORT, true).show();

        } else {
            List<LatLng> latLngs = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                if (j == i - 1) {
                    //double distance=distanceinmeters(latPoints[j],longPoints[j],latPoints[i-j-1],longPoints[i-j-1]);
                    if (isMeterEnabled) {
                        float[] results1 = new float[1];
                        Location.distanceBetween(latPoints[j], longPoints[j], latPoints[i - j - 1], longPoints[i - j - 1], results1);
                        double distance = results1[0];
                        Bitmap obm = writeTextOnDrawable(R.drawable.white_back, String.format("%.2f", distance) + " m");
                        LatLng point = new LatLng((latPoints[j] + latPoints[i - j - 1]) / 2, (longPoints[j] + longPoints[i - j - 1]) / 2);
                        MarkerOptions markerOptions = new MarkerOptions().icon(
                                BitmapDescriptorFactory.fromBitmap(obm))
                                .position(point);
                        mMap.addMarker(markerOptions);
                    }
                    latLngs.add(new LatLng(latPoints[j], longPoints[j]));
                } else {
                    // double distance=distanceinmeters(latPoints[j],longPoints[j],latPoints[j+1],longPoints[j+1]);
                    if (isMeterEnabled) {
                        float[] results1 = new float[1];
                        Location.distanceBetween(latPoints[j], longPoints[j], latPoints[j + 1], longPoints[j + 1], results1);
                        double distance = results1[0];

                        Bitmap obm = writeTextOnDrawable(R.drawable.white_back, String.format("%.2f", distance) + " m");
                        LatLng point = new LatLng((latPoints[j] + latPoints[j + 1]) / 2, (longPoints[j] + longPoints[j + 1]) / 2);

                        MarkerOptions markerOptions = new MarkerOptions().icon(
                                BitmapDescriptorFactory.fromBitmap(obm))
                                .position(point);
                        mMap.addMarker(markerOptions);

                    }
                    latLngs.add(new LatLng(latPoints[j], longPoints[j]));
                }
            }

            latLngs.add(firstAndLastPoint);
            Log.e(TAG, "1 latLngs " + latLngs.size() + " AND latPoints " + latPoints.length + " AND i " + i);
            UCCpolygon = mMap.addPolygon(new PolygonOptions()
                    .addAll(latLngs)
                    .strokeColor(Color.RED)
                    .fillColor(Color.parseColor("#330000FF")));
            String a = String.valueOf(String.format("%.2f", SphericalUtil.computeArea(latLngs) * area_mult_factor_def));
            Log.i("AREA", "computeArea " + SphericalUtil.computeArea(latLngs) + " Acre " + a);
            tvarea.setText(AppConstants.getShowableAreaWithConversion(a, area_mult_factor));
        }

    }

    boolean isMeterEnabled = false;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

//                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle(resources.getString(R.string.location_permission_needed))
                        .setMessage(resources.getString(R.string.this_app_needs_location_permisssion))
                        .setPositiveButton(resources.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(ShowAreaWithUpdateActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    // Toast.makeText(this, resources.getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                    Toasty.error(this, resources.getString(R.string.permission_denied), Toast.LENGTH_LONG, true).show();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private static final double EARTH_RADIUS = 6371000;// meters

    public static double calculateAreaOfGPSPolygonOnEarthInSquareMeters(final List<Location> locations) {
        return calculateAreaOfGPSPolygonOnSphereInSquareMeters(locations, EARTH_RADIUS);
    }

    private static double calculateAreaOfGPSPolygonOnSphereInSquareMeters(final List<Location> locations, final double radius) {
        if (locations.size() < 3) {
            return 0;
        }

        final double diameter = radius * 2;
        final double circumference = diameter * Math.PI;
        final List<Double> listY = new ArrayList<Double>();
        final List<Double> listX = new ArrayList<Double>();
        final List<Double> listArea = new ArrayList<Double>();
        // calculate segment x and y in degrees for each point
        final double latitudeRef = locations.get(0).getLatitude();
        final double longitudeRef = locations.get(0).getLongitude();
        for (int i = 1; i < locations.size(); i++) {
            final double latitude = locations.get(i).getLatitude();
            final double longitude = locations.get(i).getLongitude();
            listY.add(calculateYSegment(latitudeRef, latitude, circumference));
            Log.d("Area", String.format("Y %s: %s", listY.size() - 1, listY.get(listY.size() - 1)));
            listX.add(calculateXSegment(longitudeRef, longitude, latitude, circumference));
            Log.d("Area", String.format("X %s: %s", listX.size() - 1, listX.get(listX.size() - 1)));
        }

        // calculate areas for each triangle segment
        for (int i = 1; i < listX.size(); i++) {
            final double x1 = listX.get(i - 1);
            final double y1 = listY.get(i - 1);
            final double x2 = listX.get(i);
            final double y2 = listY.get(i);
            listArea.add(calculateAreaInSquareMeters(x1, x2, y1, y2));
            Log.d("Area", String.format("area %s: %s", listArea.size() - 1, listArea.get(listArea.size() - 1)));
        }

        // sum areas of all triangle segments
        double areasSum = 0;
        for (final Double area : listArea) {
            areasSum = areasSum + area;
        }

        // get abolute value of area, it can't be negative
        return Math.abs(areasSum);// Math.sqrt(areasSum * areasSum);
    }

    private static Double calculateAreaInSquareMeters(final double x1, final double x2, final double y1, final double y2) {
        return (y1 * x2 - x1 * y2) / 2;
    }

    private static double calculateYSegment(final double latitudeRef, final double latitude, final double circumference) {
        return (latitude - latitudeRef) * circumference / 360.0;
    }

    private static double calculateXSegment(final double longitudeRef, final double longitude, final double latitude,
                                            final double circumference) {
        return (longitude - longitudeRef) * circumference * Math.cos(Math.toRadians(latitude)) / 360.0;
    }


    private void remove() {
        // mMarkers.remove(name);
       /* mMarkers.clear();

        latLngList.clear();
        markerList.clear();*/
        mMap.clear();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int i = 1;
                for (Marker item : markerList) {
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(item.getPosition())
                            // .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_my_location))
                            .title("marker" + (i))
                            .draggable(true);
                    mMap.addMarker(markerOptions);

                    i++;
                }
            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(context, "Enabled " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(context, "Gps Disabled", Toast.LENGTH_SHORT).show();

        enableGPS();
    }

    private void enableGPS() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(ShowAreaWithUpdateActivity.this).build();
            mGoogleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000000);
            locationRequest.setFastestInterval(2 * 1000);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location
                            // Toast.makeText(MapsActivity.this, "Gps Enabled", Toast.LENGTH_SHORT).show();

                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.

                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                //Toast.makeText(MapsActivity.this, "Requesting for gps", Toast.LENGTH_SHORT).show();

                                status.startResolutionForResult(ShowAreaWithUpdateActivity.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Toast.makeText(MapsActivity.this, "Requesting for gps in catch", Toast.LENGTH_SHORT).show();

                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            //Toast.makeText(MapsActivity.this, "Settings change unavailable", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
            //}
        } catch (Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1010) {
            if (resultCode == RESULT_OK) {
//                AsyncTaskRunnerWayPoint wayPoint = new AsyncTaskRunnerWayPoint();
//                wayPoint.execute();
                mMap.clear();
                GetGeoJsonData getGeoJsonData = new GetGeoJsonData();
                getGeoJsonData.execute();
            }
        } else if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                enableGPS();
            }
        }
    }


    private Bitmap writeTextOnDrawable(int drawableId, String text) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);
        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.WHITE);

        paint.setColor(Color.BLACK);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(this, 11));
        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);
        Canvas canvas = new Canvas(bm);
        // If the text is bigger than the canvas , reduce the font size
        if (textRect.width() >= (canvas.getWidth() - 4)) // the padding on
            // either sides is
            // considered as 4,
            // so as to
            // appropriately fit
            // in the text
            paint.setTextSize(convertToPixels(this, 7)); // Scaling needs to be
        // used for
        // different dpi's
        // Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2; // -2 is for regulating the x
        // position offset
        // "- ((paint.descent() + paint.ascent()) / 2)" is the distance from the
        // baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint
                .ascent()) / 2));
        canvas.drawText(text, xPos, yPos, paint);
        return bm;
    }

    public static int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources()
                .getDisplayMetrics().density;
        return (int) ((nDP * conversionScale) + 0.5f);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    private class AsyncTaskRunnerOmitance extends AsyncTask<String, Void, String> {

        public AsyncTaskRunnerOmitance() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {

            SendOmtanceArea sendWayPointData = new SendOmtanceArea(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), AppConstants.GPS3_TYPE.OMITANCE_AREA, null);
            try {
                Log.e(TAG, "SEND OMITANCE " + new Gson().toJson(sendWayPointData));
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
                Call<OmitanceShowResponse> statusMsgModelCall = apiService.getOmitanceArea(sendWayPointData);
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<OmitanceShowResponse>() {
                    @Override
                    public void onResponse(Call<OmitanceShowResponse> call, Response<OmitanceShowResponse> response) {
                        String error = null;
                        isLoaded[0] = true;
                        Log.e(TAG, "Status OMITANCE " + response.code());
                        if (response.isSuccessful()) {
                            try {
                                Log.e(TAG, "OMITANCE res " + new Gson().toJson(response.body()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            OmitanceShowResponse statusMsgModel = response.body();
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, response.body().getMsg());
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.authorization_expired));
                            } else if (statusMsgModel.getStatus() == 1) {

                                if (statusMsgModel.getData() != null && statusMsgModel.getData().size() > 0) {
                                    for (int i = 0; i < statusMsgModel.getData().size(); i++) {
                                        OmiFetchDatum area = statusMsgModel.getData().get(i);
                                        if (area.getDetails() != null && area.getDetails().getLatLngList() != null && area.getDetails().getLatLngList().size() > 2) {
                                            List<LatLng> latLngs = new ArrayList<>();
                                            for (int j = 0; j < area.getDetails().getLatLngList().size(); j++) {
                                                latLngs.add(new LatLng(Double.valueOf(area.getDetails().getLatLngList().get(j).getLatitude()),
                                                        Double.valueOf(area.getDetails().getLatLngList().get(j).getLongitude())));
                                            }

                                            Log.e(TAG, "OMITANCE latLngs " + new Gson().toJson(latLngs));
                                            showPolygons(area.getName(), latLngs);
                                        }
                                    }
                                }

                            } else {

                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            try {

                                error = response.errorBody().string().toString();

                                Log.e("OMITANCE Error", response.errorBody().string().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                       /* long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                            notifyApiDelay(ShowAreaWithUpdateActivity.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }*/
                    }

                    @Override
                    public void onFailure(Call<OmitanceShowResponse> call, Throwable t) {

                       /* long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        notifyApiDelay(ShowAreaWithUpdateActivity.this, call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);*/
                        Log.e(TAG, "OMITANCE failure " + t.toString());
                    }
                });

               /* Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        internetFlow(isLoaded[0]);
                    }
                }, AppConstants.DELAY);*/
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

    private class AsyncTaskRunnerWayPoint extends AsyncTask<String, Void, String> {

        public AsyncTaskRunnerWayPoint() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {

            SendOmtanceArea sendWayPointData = new SendOmtanceArea(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), AppConstants.GPS3_TYPE.WAY_POINT, null);
            try {
                Log.e(TAG, "SEND OMITANCE Way" + new Gson().toJson(sendWayPointData));
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
                Call<WayPointFetchResponse> statusMsgModelCall = apiService.getWayPoints(sendWayPointData);
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<WayPointFetchResponse>() {
                    @Override
                    public void onResponse(Call<WayPointFetchResponse> call, Response<WayPointFetchResponse> response) {
                        String error = null;
                        isLoaded[0] = true;
                        Log.e(TAG, "Status OMITANCE way " + response.code());
                        if (response.isSuccessful()) {
                           /* try {
                                Log.e(TAG, "OMITANCE res way " + response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }*/

                            WayPointFetchResponse statusMsgModel = response.body();
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, response.body().getMsg());
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.authorization_expired));
                            } else if (statusMsgModel.getStatus() == 1) {
                                if (statusMsgModel.getData() != null && statusMsgModel.getData().size() > 0) {
                                    for (int i = 0; i < statusMsgModel.getData().size(); i++) {
                                        WayPointFetchDatum datum = statusMsgModel.getData().get(i);
                                        if (datum.getDetails() != null && datum.getDetails().getLatitude() != null && datum.getDetails().getLongitude() != null) {
                                            showWayPoints(datum.getName(), new LatLng(datum.getDetails().getLatitude(), datum.getDetails().getLongitude()));
                                        }
                                    }
                                }

                            } else {


                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(ShowAreaWithUpdateActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            try {

                                error = response.errorBody().string().toString();

                                Log.e("OMITANCE Error way", response.errorBody().string().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                       /* long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                            notifyApiDelay(ShowAreaOnMapActivity.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }*/
                    }

                    @Override
                    public void onFailure(Call<WayPointFetchResponse> call, Throwable t) {

                       /* long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        notifyApiDelay(ShowAreaOnMapActivity.this, call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);*/
                        Log.e(TAG, "OMITANCE failure way " + t.toString());
                    }
                });

               /* Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        internetFlow(isLoaded[0]);
                    }
                }, AppConstants.DELAY);*/
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


    private void showPolygons(String name, List<LatLng> latLngList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.addPolygon(new PolygonOptions()
                        .addAll(latLngList)
                        .strokeColor(Color.GREEN)

                        .fillColor(R.color.map_area_yellow)
                        .strokeWidth(2f));
            }
        });
    }

    Bitmap bmp;

    private void showWayPoints(String name, LatLng latLngList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final MarkerOptions marker = new MarkerOptions().position(latLngList)
                            .title(name)
                            .icon(BitmapDescriptorFactory.fromBitmap(bmp));
                    mMap.addMarker(marker);
                } catch (Exception e) {

                }
            }
        });
    }

    private static final int UPDATE_FARM_REQUEST_CODE = 100;
    TextView walkAutoTv;
    ImageView walkAutoImg;
    float locationAccuracy = 10.0f;
    boolean isAutoClisk = false;
    private static final int WHOLE_DATA_CHANGE_REQUEST_CODE = 101;
    RelativeLayout relWalAroundMode, relEasyMode, relPinDropMode, relWalAroundModeAutomatic;

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
}
