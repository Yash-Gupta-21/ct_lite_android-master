package com.i9930.croptrails.Maps.VerifyArea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

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
import com.i9930.croptrails.Landing.Models.UpdateStandingArea.UpdateStandingResponse;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;
import com.i9930.croptrails.Maps.ShowArea.ShowAreaWithUpdateActivity;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.NearFarmDatum;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.NearFarmResponse;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.NewFarmLatLang;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.OmitanceDetails;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.SendOmtanceArea;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.SendWayPointData;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.VerifySendData;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.WayPoint;
import com.i9930.croptrails.Maps.WalkAround.Compass;
import com.i9930.croptrails.Maps.WalkAround.FarmLocationData;
import com.i9930.croptrails.Maps.WalkAround.LocationData;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.geometry.Point;
import com.tooltip.Tooltip;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static java.lang.Double.parseDouble;
import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Maps.WalkAround.MapsActivityNew.getBitmapFromVectorDrawable;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class MapsActivity extends FragmentActivity implements SensorEventListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleMap mMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Toolbar mActionBarToolbar;
    LocationManager locationManager;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Context context;
    FusedLocationProviderClient mFusedLocationClient;
    Double[] latPoints;
    Double[] longPoints;
    String[] lat, lng;
    int i = 0;
    TextView submit;
    TextView tvarea;
    Boolean onclick = false;
    TextView butt_clear;
    String mprovider;
    Location location;
    TextView next_butt;
    GifImageView progressBar;
    Boolean can_add = true;
    TextView title;
    Resources resources;
    @BindView(R.id.linear_lay_for_map)
    LinearLayout linear_lay_for_map;
    String TAG = "MapsActivity";
    TextView manually_add_standing_area_btn;
    GifImageView update_standing_progress;
    TextView area_unit_label;
    double area_mult_factor = 1.0;
    double area_mult_factor_def = 0.000247105;
    double multiplicationFactor = 1.0;
    @BindView(R.id.normal_mode_butt)
    TextView normal_mode_butt;
    private final Map<String, Marker> mMarkers = new ConcurrentHashMap<String, Marker>();
   /* BottomSheetDialog bottomSheetDialog;
    TextView tv_easy_mode,tv_normal_mode;
    private boolean isEasyMode;*/

    @BindView(R.id.wayPoint)
    TextView wayPoint;
    boolean isWayPoint = false;
   /* @BindView(R.id.accuracyCard)
    CardView accuracyCard;*/
    @BindView(R.id.accuracyImage)
    CircleImageView accuracyImage;

    @BindView(R.id.mapTypeImage)
    ImageView mapTypeImage;
    int mapChoosen = 101;
    float gravity[];
    float gyroscop[] = new float[3];
    private int prevStepCount = 0;
    private long stepTimestamp = 0;
    private long stepTimestampToStore = 0;
    private String stepTimestampToStoreR = "";
    private int speed = 0;
    private float distance = 0;
    long stepCountToStore = 0;
    private long stepCount = 0;

    private void countSteps(int step) {
        //Step count
        stepCount += step;

        //Distance calculation
//        distance = stepCount * 0.8; //Average step length in an average adult
        Log.e(TAG, "SENSOR STEP COUNT " + stepCount + " , distance " + distance);
    }

    //Calculated the amount of steps taken per minute at the current rate
    private void calculateSpeed(long eventTimeStamp, int steps) {
        long timestampDifference = eventTimeStamp - stepTimestamp;
        stepTimestamp = eventTimeStamp;
        double stepTime = timestampDifference / 1000000000.0;
        speed = (int) (60 / stepTime);

        Log.e(TAG, "STEP COUNT SPEED " + speed);
    }


    public float getDistanceRun(long steps) {
        float distance = (float) (steps * 78) / (float) 100000;
        this.distance = distance * 1000;
        return distance;
    }

    int accuracyFalseCount = 0;

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            /*case (Sensor.TYPE_ACCELEROMETER):
                accelValues = event.values;
                break;*/
            case (Sensor.TYPE_GYROSCOPE):
                try {
                    gyroscop[0] = event.values[0];
                    gyroscop[1] = event.values[1];
                    gyroscop[2] = event.values[2];
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case (Sensor.TYPE_STEP_COUNTER):
                try {
                    if (prevStepCount < 1) {
                        prevStepCount = (int) event.values[0];
                    }
                    stepCountToStore = stepCountToStore + 1;
                    if (stepCountToStore == 1) {
                        stepTimestamp = event.timestamp;
                    }

                    calculateSpeed(event.timestamp, (int) (event.values[0] - prevStepCount - stepCount));
                    countSteps((int) (event.values[0] - prevStepCount - stepCount));
                    getDistanceRun(stepCountToStore);
                    Log.e(TAG, "Sensor Working " + event.sensor.getType() + " Name " + event.sensor.getName() + " stepCountToStore " + stepCountToStore);
                    stepTimestampToStore = System.currentTimeMillis();

                    try {
                        Date c;
                        SimpleDateFormat df;
                        c = Calendar.getInstance().getTime();
                        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        stepTimestampToStoreR = df.format(c);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (isStoring && mLastLocation != null && stepCountToStore % 2 == 0) {
                        LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        storeData(latLng, mLastLocation.getAccuracy());
                    }
                } catch (Exception e) {
                }
                break;
            case (Sensor.TYPE_STEP_DETECTOR):
                try {
                    if (stepCounter == null) {
                        stepCountToStore = stepCountToStore + 1;
                        countSteps((int) event.values[0]);
                        calculateSpeed(event.timestamp, 1);
                        getDistanceRun(stepCountToStore);
                        stepTimestampToStore = System.currentTimeMillis();
                        try {
                            Date c;
                            SimpleDateFormat df;
                            c = Calendar.getInstance().getTime();
                            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            stepTimestampToStoreR = df.format(c);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {

                }
                break;

        }


    }


    @Override
    protected void onStop() {
        super.onStop();
        if (compass != null)
            compass.stop();
        isActivityRunning = false;

    }

    boolean isActivityRunning = false;
    ViewFailDialog viewFailDialog = new ViewFailDialog();

    @Override
    protected void onStart() {
        super.onStart();
        isActivityRunning = true;
        if (compass != null)
            compass.start();
    }

    Bitmap bmp;

    private void getOrientation(float orientationDegree) {

        if (orientationDegree >= 0 && orientationDegree < 90) {
            compassOrientation = "North";
        } else if (orientationDegree >= 90 && orientationDegree < 180) {
            compassOrientation = "East";
        } else if (orientationDegree >= 180 && orientationDegree < 270) {
            compassOrientation = "South";
        } else {
            compassOrientation = "West";
        }
    }

    private String compassOrientation;

    private float[] accelValues;
    private float[] magnetValues;
    String submitType;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private interface MAP_TYPE {
        public static final int DEFAULT_MAP = 101;
        public static final int SATELLITE_MAP = 102;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help_menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.help_menu:
                // do your code
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    float compAccuracy=2.0f;


    /*@BindView(R.id.myLocationCard)
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
    private void setFarmerNameAndKhasra(){
        String farmerName=SharedPreferencesMethod.getString(context,SharedPreferencesMethod.FARMER_NAME);
        String khasra=SharedPreferencesMethod.getString(context,SharedPreferencesMethod.KHASRA);

        if (!AppConstants.isValidString(farmerName)&&!AppConstants.isValidString(khasra)){
            kasraAndFarmerLayout.setVisibility(View.GONE);
        }else {
            if (!AppConstants.isValidString(khasra)){
                khasraLayout.setVisibility(View.GONE);
            }else {
                khasraLabelTv.setText(resources.getString(R.string.khasra)+":");
                khasraTv.setText(khasra);
            }

            if (!AppConstants.isValidString(farmerName)){
                farmerNameLayout.setVisibility(View.GONE);
            }else {
                farmerNameLabel.setText(resources.getString(R.string.farmer_name_hint)+":");
                farmerNamrTv.setText(farmerName);
            }

        }

        myLocationImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLastLocation!=null){
                    LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                    mMap.animateCamera(cameraUpdate);
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                }
            }
        });

       /* myLocationCard .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLastLocation!=null){
                    LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                    mMap.animateCamera(cameraUpdate);
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                }
            }
        });*/
    }

    ImageView back_to_farm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        final String languageCode = SharedPreferencesMethod2.getString(MapsActivity.this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        context = this;
        compAccuracy=SharedPreferencesMethod.getFloatPref(context,SharedPreferencesMethod.COMP_AUTO_ACCURACY);
        ButterKnife.bind(this);
        area_mult_factor = SharedPreferencesMethod.getDoubleSharedPreferences(context, SharedPreferencesMethod.AREA_MULTIPLICATON_FACTOR);
//        bmp = getBitmapFromVectorDrawable(context, R.drawable.ic_current_location);
        initViews();
        bmp = getBitmapFromVectorDrawable(context, R.drawable.marker_green_24px);
//        title.setText(resources.getString(R.string.capture_farm_area_maps_activity_title));
        title.setText("Walk Around Mode");
        //Toolbar toolbar = view.findViewById(R.id.toolbar);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        back_to_farm=findViewById(R.id.back_to_farm);
        setFarmerNameAndKhasra();
        progressBar.setVisibility(View.VISIBLE);

            Intent intent=getIntent();
            isAutoPoint=intent.getBooleanExtra("isAutomaticMode",false);

      /*  if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL).toLowerCase().equals("hectare")) {
            multiplicationFactor = 2.47105;
        } else if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL).toLowerCase().equals("malwa_bigha")) {
            multiplicationFactor = 3.63;
        } else {

        }*/


        next_butt.setEnabled(false);
        area_unit_label.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL));
        latPoints = new Double[5000];
        longPoints = new Double[5000];
        Log.e(TAG, "Multiplication Factor " + area_mult_factor);

        normal_mode_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, MapsActivityEasyMode.class));
                finish();
            }
        });
        submit.setText(resources.getString(R.string.start_label));

        if (isAutoPoint) {
            submit.setVisibility(View.VISIBLE);
        }else
            submit.setVisibility(View.GONE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (i > 2) {
                    submit_area();
                    next_butt.setEnabled(true);
                    can_add = false;
                } else {
                    ViewFailDialog failDialog = new ViewFailDialog();
                    failDialog.showDialog(MapsActivity.this, resources.getString(R.string.please_submit_at_least_3_points));
                }*/
                if (isAutoPoint) {
                    if (isMarkingStarted) {

                        isMarkingStarted=false;
                        submit.setText(resources.getString(R.string.start_label));
                    } else {

                        isMarkingStarted=true;
                        submit.setText(resources.getString(R.string.stop));
                    }
                }

            }
        });

        next_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i < 3) {
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showDialog(MapsActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.please_submit_at_least_3_points));
                } else {
                    try {
                        LatLng[] latLng = new LatLng[i];
                        List<LatLng> latLngs = new ArrayList<>();
                        for (int j = 0; j < i; j++) {
                            latLng[j] = new LatLng(latPoints[j], longPoints[j]);
                            latLngs.add(new LatLng(latPoints[j], longPoints[j]));
                        }
                        /*Polygon UCCpolygon =*/
                        if (UCCpolygon != null)
                            UCCpolygon.remove();
                        UCCpolygon =mMap.addPolygon(new PolygonOptions().add(latLng).strokeColor(Color.RED).fillColor(Color.parseColor("#330000FF")));
                        Log.i("AREA", "computeArea " + SphericalUtil.computeArea(latLngs));
                        String a = String.valueOf(String.format("%.2f", SphericalUtil.computeArea(latLngs) * area_mult_factor_def));
                        tvarea.setText(AppConstants.getShowableAreaWithConversion(a, area_mult_factor));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    isStoring = false;
                    if (!TextUtils.isEmpty(tvarea.getText().toString().trim()) && Double.valueOf(tvarea.getText().toString().trim()) > 0) {
                        new AlertDialog.Builder(context)
                                .setMessage(resources.getString(R.string.sure_to_submit_area))
                                .setCancelable(false)
                                .setPositiveButton(resources.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        if (submitType != null && submitType.equals(AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE)) {

                                            showDialogNameOmitanceArea();
                                        }else
                                        if (Double.parseDouble(tvarea.getText().toString().trim()) > 0) {

                                            if (submitType != null && submitType.equals(AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE)) {

                                                showDialogNameOmitanceArea();
                                            } else {
                                                progressBar.setVisibility(View.VISIBLE);
                                                SubmitAreaAsync asyncTaskRunner = new SubmitAreaAsync(tvarea.getText().toString(),
                                                        null,AppConstants.AREA_TYPE.Farm);
                                                asyncTaskRunner.execute();
                                            }
                                           /* SendWayPointData sendWayPointData=new SendWayPointData(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID),
                                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID),
                                                    "W",wayPointList);
                                            addWayPoints(sendWayPointData);*/

//                                            FarmLocationData farmLocationData=new FarmLocationData(
//                                                    SharedPreferencesMethod.getString(context,SharedPreferencesMethod.SVFARMID),
//                                                    locationDataList,
//                                                    SharedPreferencesMethod.getString(context,SharedPreferencesMethod.USER_ID),
//                                                    SharedPreferencesMethod.getString(context,SharedPreferencesMethod.TOKEN),
//                                                    SharedPreferencesMethod.getString(context,SharedPreferencesMethod.COMP_ID));
//                                            addFarmLocation(farmLocationData);

                                        } else {
                                            //Toast.makeText(context, "Area is too short to capture.. Please capture again", Toast.LENGTH_LONG).show();
                                            ViewFailDialog failDialog = new ViewFailDialog();
                                            failDialog.showDialog(MapsActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.area_is_too_short_for_capture));
                                        }
                                    }
                                })
                                .setNegativeButton(resources.getString(R.string.no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        isStoring = true;
                                    }
                                })
                                .show();
                    } else {
                        ViewFailDialog failDialog = new ViewFailDialog();
                        failDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.area_is_too_short_for_capture));
                    }
                } catch (Exception e) {
                    ViewFailDialog failDialog = new ViewFailDialog();
                    failDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.area_is_too_short_for_capture));

                }


            }
        });

        boolean isWayPointEnabled = SharedPreferencesMethod.getBoolean(this, SharedPreferencesMethod.IS_WAY_POINT_ENABLED);
//        isWayPointEnabled=true;
//        Intent intent = getIntent();
        submitType = intent.getStringExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE);

        if (submitType != null && submitType.equals(AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE)) {
            isWayPointEnabled = false;
        }

        if (isWayPointEnabled)
            wayPoint.setVisibility(View.VISIBLE);
        else
            wayPoint.setVisibility(View.GONE);
        wayPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isWayPoint)
                    isWayPoint = true;
                else
                    isWayPoint = false;

            }
        });

        butt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i > 0) {
                    tvarea.setText("0");
                    remove("marker" + (i - 1));
                    next_butt.setEnabled(false);
                    can_add = true;
                    i--;
                }
            }
        });
       /* if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }*/
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Double actualArea = 0.0;
        if (!TextUtils.isEmpty(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA))) {
            actualArea = Double.parseDouble(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA));
        } else {
            Log.e(TAG, "ActualArea No setted");
        }
        if (actualArea > 0) {
            Log.e(TAG, "ActualArea if " + actualArea);
            manually_add_standing_area_btn.setVisibility(View.GONE);
        } else {
            Log.e(TAG, "ActualArea else " + actualArea);
//            manually_add_standing_area_btn.setVisibility(View.VISIBLE);
            manually_add_standing_area_btn.setVisibility(View.GONE);
        }

        manually_add_standing_area_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAreaManually();
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mprovider = locationManager.getBestProvider(criteria, false);
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLastLocation = locationManager.getLastKnownLocation(mprovider);
            //locationManager.requestLocationUpdates(mprovider, 100, 1, this);
            locationCount++;
            if (mLastLocation != null && !isNearFarmCalled && locationCount > 1) {
                getNearFarms(mLastLocation.getLatitude() + "", mLastLocation.getLongitude() + "");

            }

        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100); //  minute interval
        mLocationRequest.setFastestInterval(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        getLocation();
        enableGPS();
        registerSensors();
        setupCompass();


    }

    int locationCount = 0;

    List<WayPoint> wayPointList = new ArrayList<>();

    public void showDialog(LatLng latLng) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_way_point);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        AutoCompleteTextView etPointName = dialog.findViewById(R.id.etPointName);
        TextView cancelTv = dialog.findViewById(R.id.cancelTv);
        TextView addTv = dialog.findViewById(R.id.addTv);
        TextView title = dialog.findViewById(R.id.title);

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWayPoint = false;
                dialog.dismiss();
            }
        });

        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etPointName.getText().toString())) {
                    Toasty.error(context, resources.getString(R.string.required_label), Toast.LENGTH_SHORT).show();
                    etPointName.setError(resources.getString(R.string.required_label));
                } else {
                    wayPointList.add(new WayPoint(etPointName.getText().toString().trim(), latLng));
                    dialog.dismiss();
                    isWayPoint = false;
                    final MarkerOptions marker = new MarkerOptions().position(latLng)
                            .title(etPointName.getText().toString().trim())
                            .icon(BitmapDescriptorFactory.fromBitmap(bmp));
                    mMap.addMarker(marker);

                }

            }
        });

        dialog.show();


    }

    public void showDialogNameOmitanceArea() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_way_point);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        AutoCompleteTextView etPointName = dialog.findViewById(R.id.etPointName);
        TextView cancelTv = dialog.findViewById(R.id.cancelTv);
        TextView addTv = dialog.findViewById(R.id.addTv);
        TextView title = dialog.findViewById(R.id.title);
        TextInputLayout tiName = dialog.findViewById(R.id.tiName);
        tiName.setHint(resources.getString(R.string.name_label));
        title.setText("Omitance area name");
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

                if (TextUtils.isEmpty(etPointName.getText().toString())) {
                    Toasty.error(context, resources.getString(R.string.required_label), Toast.LENGTH_SHORT).show();
                    etPointName.setError(resources.getString(R.string.required_label));
                } else {
                    dialog.dismiss();
                    progressBar.setVisibility(View.VISIBLE);
//                    AsyncTaskRunnerOmitance asyncTaskRunner = new AsyncTaskRunnerOmitance(tvarea.getText().toString(), etPointName.getText().toString());
//                    asyncTaskRunner.execute();
                    SubmitAreaAsync asyncTaskRunner = new SubmitAreaAsync(tvarea.getText().toString(),
                            etPointName.getText().toString().trim(),AppConstants.AREA_TYPE.Omittance);
                    asyncTaskRunner.execute();
                }

            }
        });

        dialog.show();


    }

    private void addAreaManually() {
        final String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.update_standing_area_dialog_layout);
        Window window1 = dialog.getWindow();
        window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        update_standing_progress = dialog.findViewById(R.id.update_standing_progress);
        ImageView update_acres_cancel_image = dialog.findViewById(R.id.update_acres_cancel_image);
        TextView update_acres_dialog_title = dialog.findViewById(R.id.update_acres_dialog_title);
        final TextInputEditText update_acres_dialog_et = dialog.findViewById(R.id.update_acres_dialog_et);
        TextView update_standing_acres_bt = dialog.findViewById(R.id.update_standing_acres_btn);

        //update_acres_dialog_et.setHint(resources.getString(R.string.enter_area_in_hint) + " " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL));

        update_acres_cancel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        update_standing_acres_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(update_acres_dialog_et.getText().toString())) {
                    update_acres_dialog_et.setError(resources.getString(R.string.please_enter_valid_area));
                    //  Toast.makeText(context, resources.getString(R.string.please_enter_valid_area), Toast.LENGTH_LONG).show();
                    Toasty.info(context, resources.getString(R.string.please_enter_valid_area), Toast.LENGTH_LONG, true).show();

                    //ViewFailDialog viewFailDialog=new ViewFailDialog();
                    //viewFailDialog.showDialog(MapsActivity.this,"Opps!","Please Enter valid Area");
                } else {
                    update_standing_progress.setVisibility(View.VISIBLE);
                    UpdateStandingAcres updateStandingAcres = new UpdateStandingAcres(update_acres_dialog_et.getText().toString(), dialog, farmId);
                    updateStandingAcres.execute();
                }
            }
        });


        dialog.show();


    }


    private class UpdateStandingAcres extends AsyncTask<String, Void, String> {
        String standingAcres;
        Dialog dialog;
        String farmId;

        public UpdateStandingAcres(String standingAcres, Dialog dialog, String farmId) {
            this.standingAcres = standingAcres;
            this.dialog = dialog;
            this.farmId = farmId;
        }

        @Override
        protected String doInBackground(String... strings) {
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            final String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            final String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            Call<UpdateStandingResponse> updateStandingAcres = apiInterface.updateStandingAcres(farmId, AppConstants.getUploadableArea(standingAcres), userId, token);
            Log.e(TAG, "Sending Data Munual " + AppConstants.getUploadableArea(standingAcres));
            updateStandingAcres.enqueue(new Callback<UpdateStandingResponse>() {
                @Override
                public void onResponse(Call<UpdateStandingResponse> call, Response<UpdateStandingResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            //  Toast.makeText(context, resources.getString(R.string.actual_area_update_successfully), Toast.LENGTH_LONG).show();
                            Toasty.success(context, resources.getString(R.string.actual_area_update_successfully), Toast.LENGTH_LONG, true).show();
                            setResult(RESULT_OK);
                            finish();
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.authorization_expired));
                        } else if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivity.this, response.body().getMsg());
                        } else {
                            Log.e("FarmDetailAdapter e1", new Gson().toJson(response.body()));
                            //Toast.makeText(context, "Failed to update", Toast.LENGTH_LONG).show();

                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(MapsActivity.this, resources.getString(R.string.opps_message), response.body().getMsg());

                            if (update_standing_progress != null) {
                                update_standing_progress.setVisibility(View.INVISIBLE);
                            }
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(MapsActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.failed_to_update_actual_area));
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            if (update_standing_progress != null) {
                                update_standing_progress.setVisibility(View.INVISIBLE);
                            }
                            //Toast.makeText(context, "Failed to update", Toast.LENGTH_LONG).show();
                            Log.e(TAG,"FarmDetailAdapter e2 "+ response.errorBody().string().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UpdateStandingResponse> call, Throwable t) {
                    //Toast.makeText(context, "Failed to update", Toast.LENGTH_LONG).show();
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showDialog(MapsActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.failed_to_update_actual_area));
                    Log.e("FarmDetailAdapter", t.toString());
                    if (update_standing_progress != null) {
                        update_standing_progress.setVisibility(View.INVISIBLE);
                    }
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
            return null;
        }
    }

    String msg = "1) Please turn on you location service from mobile settings.\n" +
            "2) Mark the corners of the farm by tapping on the circle shown in the center.\n" +
            "3) Follow a circular (clockwise or anticlockwise) pattern to mark them, don't mark random corners of the farm.\n" +
            "4) If you start with a corner, please note that you should close the formation and end at the same corner, marking with another dropped pin.";
    ImageView helpImage;

    private void initViews() {
        manually_add_standing_area_btn = findViewById(R.id.manually_add_standing_area_btn);
        title = (TextView) findViewById(R.id.tittle);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        tvarea = (TextView) findViewById(R.id.area_tv);
        submit = findViewById(R.id.submit_butt);
        butt_clear = findViewById(R.id.clear_butt);
        next_butt = findViewById(R.id.next_butt);
        progressBar = findViewById(R.id.progressBar_cyclic);
        area_unit_label = (TextView) findViewById(R.id.area_unit_label);

        /*if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.FARMER)) {
            manually_add_standing_area_btn.setVisibility(View.GONE);
        } else {
            manually_add_standing_area_btn.setVisibility(View.VISIBLE);
        }*/
        manually_add_standing_area_btn.setVisibility(View.VISIBLE);
        helpImage = findViewById(R.id.helpImage);
        int pageVisitCount = SharedPreferencesMethod2.getInt(context, SharedPreferencesMethod2.MAP_VISIT_COUNT);

        //msg = "";
        Tooltip tooltip = new Tooltip.Builder(helpImage)
                .setText(msg)
                .setBackgroundColor(resources.getColor(R.color.white))
                .setTextColor(resources.getColor(R.color.black))
                .setCancelable(true).build();
        if (pageVisitCount <= 9 && msg.length() > 0) {
            tooltip.show();
            pageVisitCount++;
            SharedPreferencesMethod2.setInt(context, SharedPreferencesMethod2.MAP_VISIT_COUNT, pageVisitCount);
        }

        helpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tooltip.isShowing()) {
                    tooltip.dismiss();
                } else if (msg.length() > 0)
                    tooltip.show();

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapChoosen = MAP_TYPE.SATELLITE_MAP;
        mapTypeImage.setImageResource(R.drawable.map_type_normal);
        mapTypeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapChoosen == MAP_TYPE.DEFAULT_MAP) {
                    mapChoosen = MAP_TYPE.SATELLITE_MAP;
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    mapTypeImage.setImageResource(R.drawable.map_type_normal);
                } else {
                    mapChoosen = MAP_TYPE.DEFAULT_MAP;
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mapTypeImage.setImageResource(R.drawable.map_type_satellite);
                }
            }
        });
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100); // two minute interval
        mLocationRequest.setFastestInterval(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        prepareData();
        linear_lay_for_map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Point touchedPoint = new Point(Math.round(event.getX()), Math.round(event.getY()));
                // Toast.makeText(context, ""+touchedPoint.toString(), Toast.LENGTH_SHORT).show();

                if (isInsideCircle(touchedPoint)) {
                    onclick = false;
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng point) {
                            // TODO Auto-generated method stub
                            if (!onclick) {
                                isStoring = true;
                                if (submitType != null && submitType.equals(AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE) && !PolyUtil.containsLocation(point, latLngsPrevious, false)) {
                                    Toasty.error(context, "Not within farm boundaries", Toast.LENGTH_SHORT, false).show();
                                } else {
                                    if (isAutoPoint/*&&isAutoPointEnabled*/) {
                                        if (isWayPoint) {
                                            showDialog(point);
                                        }
                                    } else {
                                        if (isWayPoint) {
                                            showDialog(point);
                                        } else {
                                            LatLng latLng = new LatLng(point.latitude, point.longitude);
                                            add("marker" + i, latLng);
                                            //mMap.addMarker(marker);
                                            Log.e("TOUCH Points", point.latitude + "    " + point.longitude);
                                        }
                                    }
                                }
                            }
                        }
                    });
                } else {
                    onclick = true;
                    //    Toast.makeText(context, "Outside Circle"+" radius"+linear_lay_for_map.getWidth()/10, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(context, resources.getString(R.string.please_click_inside_circle_to_mark), Toast.LENGTH_LONG).show();
                    if (!isAutoPoint)
                    Toasty.info(context, resources.getString(R.string.please_click_inside_circle_to_mark), Toast.LENGTH_SHORT, true).show();
                    //ViewFailDialog viewFailDialog=new ViewFailDialog();
                    //viewFailDialog.showDialog(MapsActivity.this,resources.getString(R.string.opps_message),resources.getString(R.string.please_click_inside_circle_to_mark));
                }
                return false;
            }
        });

        if (submitType != null && submitType.equals(AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE)) {
            AsyncTaskRunnerShow asyncTaskRunnerShow = new AsyncTaskRunnerShow();
            asyncTaskRunnerShow.execute();
        }

    }

    Polygon UCCpolygon;

    private void submit_area() {


        if (i < 3) {
            //Toast.makeText(context, "Please view_area_button atleast 3 points", Toast.LENGTH_SHORT).show();
            ViewFailDialog viewFailDialog = new ViewFailDialog();
            viewFailDialog.showDialog(MapsActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.please_submit_at_least_3_points));
        } else {
            LatLng[] latLng = new LatLng[i];
            List<LatLng> latLngs = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                latLng[j] = new LatLng(latPoints[j], longPoints[j]);
                latLngs.add(new LatLng(latPoints[j], longPoints[j]));
            }
            if (UCCpolygon != null)
                UCCpolygon.remove();
            UCCpolygon = mMap.addPolygon(new PolygonOptions().add(latLng).strokeColor(Color.RED).fillColor(Color.parseColor("#330000FF")));
            Log.i("AREA", "computeArea " + SphericalUtil.computeArea(latLngs));
            String a = String.valueOf(String.format("%.2f", SphericalUtil.computeArea(latLngs) * area_mult_factor_def));
            tvarea.setText(AppConstants.getShowableAreaWithConversion(a, area_mult_factor));
        }

    }

    private Compass compass;
    private float currentAzimuth;

    private void setupCompass() {
        try {
            compass = new Compass(this);
            Compass.CompassListener cl = new Compass.CompassListener() {
                @Override
                public void onNewAzimuth(float azimuth, float[] mGravity) {
                    adjustArrow(azimuth);
                    gravity = mGravity;
//                    Log.e(TAG, "Compass " + azimuth + " gravity " + mGravity.toString());
                }
            };
            compass.setListener(cl);
        } catch (Exception e) {

        }
    }

    private void adjustArrow(float azimuth) {
        /*Log.d(TAG, "will set rotation from " + currentAzimuth + " to "
                + azimuth);*/
        try {
            currentAzimuth = azimuth;
            String display = (int) currentAzimuth + "";
            String cardDirect;

            if (currentAzimuth == 0 || currentAzimuth == 360)
                cardDirect = "N";
            else if (currentAzimuth > 0 && currentAzimuth < 90)
                cardDirect = "NE";
            else if (currentAzimuth == 90)
                cardDirect = "E";
            else if (currentAzimuth > 90 && currentAzimuth < 180)
                cardDirect = "SE";
            else if (currentAzimuth == 180)
                cardDirect = "S";
            else if (currentAzimuth > 180 && currentAzimuth < 270)
                cardDirect = "SW";
            else if (currentAzimuth == 270)
                cardDirect = "W";
            else if (currentAzimuth > 270 && currentAzimuth < 360)
                cardDirect = "NW";
            else
                cardDirect = "Unknown";
        } catch (Exception e) {
        }

//        currentDirection
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude() + " Acc" + location.getAccuracy());
                mLastLocation = location;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        locationCount++;
                        if (mLastLocation != null && !isNearFarmCalled && locationCount > 1) {
                            getNearFarms(mLastLocation.getLatitude() + "", mLastLocation.getLongitude() + "");
                            isNearFarmCalled = true;
                        }
                        if (mCurrLocationMarker != null) {
                            mCurrLocationMarker.remove();
                        }
                        //Place current location marker
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30));
                        progressBar.setVisibility(View.INVISIBLE);
                        if (mLastLocation != null) {
                            Log.e(TAG, "ACCURACY " + mLastLocation.getAccuracy());
                            if (mLastLocation.getAccuracy() < 7) {
                                //green
                                accuracyImage.setImageResource(R.drawable.ic_signal_4);
                                /*if (latLngList.size() < 1 && mLastLocation.getAccuracy() < 3) {
//                                    isAutoPoint = true;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (isAutoPoint) {
                                                submit.setVisibility(View.VISIBLE);
                                            }else
                                                submit.setVisibility(View.GONE);
                                            if (isMarkingStarted){
                                                submit.setText(resources.getString(R.string.stop));
                                            }else
                                                submit.setText(resources.getString(R.string.start_label));

                                        }
                                    });
                                }*/

                            } else if (mLastLocation.getAccuracy() < 13) {
                                //green2
                                accuracyImage.setImageResource(R.drawable.ic_signal_3);
                            } else if (mLastLocation.getAccuracy() < 17) {
                                //orange
                                accuracyImage.setImageResource(R.drawable.ic_signal_2);
                            } else if (mLastLocation.getAccuracy() < 25) {
                                //red
                                accuracyImage.setImageResource(R.drawable.ic_signal_1);
                            } else {
                                accuracyImage.setImageResource(R.drawable.ic_signal_0);
                            }

                            if (isAutoPoint&&isMarkingStarted) {
                                myLocationList.add(mLastLocation);
                                Collections.sort(myLocationList, new Comparator<Location>() {
                                    @Override
                                    public int compare(Location lhs, Location rhs) {
                                        return Float.valueOf(lhs.getAccuracy()).compareTo(rhs.getAccuracy());
                                    }
                                });
                                if (latLngListData.size() == 0) {
                                    myLocationList.clear();
                                    add("marker" + i, latLng);
                                    MyData data = new MyData(AppConstants.getCurrentMills(), currentAzimuth);
                                    latLngListData.add(data);
                                } else {
                                    long diffInMillisec = AppConstants.getCurrentMills() - latLngListData.get(latLngListData.size() - 1).getTimeStamp();
                                    float directionChanged = currentAzimuth - latLngListData.get(latLngListData.size() - 1).getDirection();
                                    directionChanged = Math.abs(directionChanged);
                                    if (TimeUnit.MILLISECONDS.toSeconds(diffInMillisec) > 30 || directionChanged > 20) {
                                        /*for (int i=0;i<myLocationList.size();i++){
                                            Log.e(TAG,"Locations "+myLocationList.get(i).getAccuracy());
                                        }*/
                                        if (myLocationList.size()>0) {
                                            Location firstLoc = myLocationList.get(0);
                                            latLng = new LatLng(firstLoc.getLatitude(),firstLoc.getLongitude());
                                        }
                                        myLocationList.clear();
                                        add("marker" + i, latLng);
                                        MyData data = new MyData(AppConstants.getCurrentMills(), currentAzimuth);
                                        latLngListData.add(data);
                                    }
                                }
                            }
                        }
                    }
                });

            }
        }
    };

    List<Location>myLocationList=new ArrayList<>();
    private class MyData {
        long timeStamp;
        float direction;

        public MyData(long timeStamp, float direction) {
            this.timeStamp = timeStamp;
            this.direction = direction;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public float getDirection() {
            return direction;
        }
    }

    List<MyData> latLngListData = new ArrayList<>();

    boolean isAutoPoint = false;
    boolean isMarkingStarted=false;
    boolean isAutoPointEnabled = false;
    boolean isStoring = false;
    List<LatLng> latLngList = new ArrayList<>();
    private SensorManager sensorManager;
    Sensor stepCounter;

    private void registerSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor stepDetectorSensor;
        Sensor gyroscop;
        Sensor accelerometer;
        Sensor magnetometer;


        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscop = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepDetectorSensor != null)
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);

       /* if (accelerometer != null)
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        if (magnetometer != null)
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
*/
        if (stepCounter != null)
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);

//        sensorManager.registerListener ( this,stepCounter,SensorManager.SENSOR_DELAY_NORMAL );


        if (gyroscop != null)
            sensorManager.registerListener(this, gyroscop, SensorManager.SENSOR_DELAY_GAME);
        else
            Log.e(TAG, "stepCounter is null");

    }

    List<LocationData> locationDataList = new ArrayList<>();
    AlertDialog.Builder builder;

    private void showAccuracyAlert() {

        try {
            if (builder == null) {
                builder = new AlertDialog.Builder(context)
                        .setTitle(resources.getString(R.string.alert_title_label))
                        .setMessage("Your current location is not accurate, Please walk throw farm corner and mark points")
                        .setCancelable(false)
                        .setPositiveButton(resources.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mMap.getUiSettings().setScrollGesturesEnabled(true);
                                mMap.getUiSettings().setRotateGesturesEnabled(true);
                            }
                        })
/*
                .setNegativeButton(resources.getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isStoring = true;
                    }
                })
*/
                ;
            }
            if (builder != null)
                builder.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void storeData(LatLng latLng, float accuracy) {
        if (latLng != null) {
            if (/*!latLngList.contains(latLng)&&*/accuracy > 10.5 && latLngList.size() < 5) {
                accuracyFalseCount++;
                if (accuracyFalseCount > 5) {
                    showAccuracyAlert();
                }
            }
            if (latLngList.size() == 0) {
                latLngList.add(latLng);
                LocationData data = new LocationData(latLng.latitude, latLng.longitude, gravity,
                        stepCountToStore, distance, currentAzimuth, "" + speed
                        , gyroscop, stepTimestampToStore, stepTimestampToStoreR, accuracy);
                locationDataList.add(data);
            } else {
                latLngList.add(latLng);
                LocationData data = new LocationData(latLng.latitude, latLng.longitude, gravity, stepCountToStore, distance, currentAzimuth, "" + speed, gyroscop, stepTimestampToStore, stepTimestampToStoreR, accuracy);
                locationDataList.add(data);
            }


        }
    }

    private double meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
        double pk = (double) (180.f / Math.PI);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(resources.getString(R.string.location_permission_needed))
                        .setMessage(resources.getString(R.string.this_apps_needs_location_permission))
                        .setPositiveButton(resources.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
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
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //Toast.makeText(this, resources.getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
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


    private void add(String name, final LatLng ll) {
        if (can_add) {
            if (i < 5000) {
                if (i > 0) {
                    float[] results1 = new float[1];
                    Location.distanceBetween(latPoints[i - 1], longPoints[i - 1], ll.latitude, ll.longitude, results1);
                    double distanceinKm = results1[0] / 1000;
                    if (distanceinKm < 10) {
                        final MarkerOptions marker = new MarkerOptions().position(ll).title(name);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Marker m = mMap.addMarker(marker);
                                mMarkers.put(name, m);
                                latPoints[i] = ll.latitude;
                                longPoints[i] = ll.longitude;
                                i++;
                            }
                        });
                        if (i > 2) {
                            next_butt.setEnabled(true);
                            next_butt.setVisibility(View.VISIBLE);
                            submit_area();
                        }
                    } else {
                        Toasty.error(context, "Point cannot be 10km far from last point", Toast.LENGTH_SHORT, false).show();
                    }
                } else {
                    final MarkerOptions marker = new MarkerOptions().position(ll).title(name);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Marker m = mMap.addMarker(marker);
                            mMarkers.put(name, m);
                            latPoints[i] = ll.latitude;
                            longPoints[i] = ll.longitude;
                            i++;
                        }
                    });
                }
            } else {
                //Toast.makeText(context, resources.getString(R.string.cant_add_more_points), Toast.LENGTH_LONG).show();
                Toasty.error(context, resources.getString(R.string.cant_add_more_points), Toast.LENGTH_LONG, true).show();
            }
        } else {
            // Toast.makeText(context, resources.getString(R.string.please_clear_then_add), Toast.LENGTH_LONG).show();
            Toasty.error(context, resources.getString(R.string.please_clear_then_add), Toast.LENGTH_LONG, true).show();

        }
    }


    private void remove(String name) {
        mMarkers.get(name).remove();
        mMarkers.remove(name);
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.clear();

                for (MarkerOptions item : mMarkers.values()) {
                    mMap.addMarker(item);
                }
            }
        });*/
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String s) {
        //gps_status=true;
        // mGoogleApiClient=null;
        // Toast.makeText(context, "Gps Enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(context, "Gps Disabled", Toast.LENGTH_SHORT).show();

        enableGPS();
    }

    private void enableGPS() {
        /*if (mGoogleApiClient == null) {*/

        //Toast.makeText(context, "Coming in enable gps", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(MapsActivity.this).build();
        mGoogleApiClient.connect();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

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

                            status.startResolutionForResult(MapsActivity.this, 1000);
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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                enableGPS();
            }
        }
    }

    private void addWayPoints(SendWayPointData sendWayPointData) {
        progressBar.setVisibility(View.VISIBLE);
        String data = new Gson().toJson(sendWayPointData);
        Log.e(TAG, "Sending New Param " + data);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        apiInterface.uploadWayPoints(sendWayPointData).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response1) {
                String error = null;
                if (response1.isSuccessful()) {
                    StatusMsgModel response = response1.body();
                    Log.e(TAG, "Sending New Param Response " + new Gson().toJson(response));
                    if (response.getStatus() == 1) {

//                        Toast.makeText(context, "Farm location added successfully" , Toast.LENGTH_LONG).show();

                    } else {
//                        Toast.makeText(context,"Failed to add farm location",Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
//                        Toast.makeText(context,"Failed to add farm location",Toast.LENGTH_LONG).show();
                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "Cache Error Way " + error+", code "+response1.code());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FarmLocationData farmLocationData = new FarmLocationData(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), locationDataList,
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
//                                FarmLocationDataOuter dataOuter=new FarmLocationDataOuter(farmLocationData);
                addFarmLocation(farmLocationData);
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e(TAG, "Failure getOfflineData " + t.toString());
                FarmLocationData farmLocationData = new FarmLocationData(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), locationDataList,
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
//                                FarmLocationDataOuter dataOuter=new FarmLocationDataOuter(farmLocationData);
                addFarmLocation(farmLocationData);
            }
        });
    }

    boolean isNearFarmCalled = false;

    private void getNearFarms(String lat, String lon) {
        if (!isNearFarmCalled) {
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
            final String farm_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
            Log.e(TAG, "SoilSenseResponse " + "lat:" + lat);
            Log.e(TAG, "SoilSenseResponse " + "long:" + lon);
            Log.e(TAG, "SoilSenseResponse " + "user_id:" + userId);
            Log.e(TAG, "SoilSenseResponse " + "token:" + token);
            Log.e(TAG, "SoilSenseResponse " + "comp_id:" + comp_id);
            final String clusterId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID);
            final boolean[] isLoaded = {false};
            long currMillis = AppConstants.getCurrentMills();
            Call<NearFarmResponse> getCalenderData = apiInterface.getNearFarms(comp_id, farm_id, userId, token, lat, lon,clusterId);
            getCalenderData.enqueue(new Callback<NearFarmResponse>() {
                @Override
                public void onResponse(Call<NearFarmResponse> call, retrofit2.Response<NearFarmResponse> response) {
                    Log.e(TAG, "SoilSenseResponse Status " + response.code());
                    try {
                        Log.e(TAG, "SoilSenseResponse " + new Gson().toJson(response.body()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isLoaded[0] = true;
                    String error = "";
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 10) {
                            viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            if (isActivityRunning)
                                viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            if (isActivityRunning)
                                viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.authorization_expired));
                        } else if (response.body().getStatus() == 1 && response.body().getData() != null && response.body().getData().size() > 0) {

                            try {
                                PolyAsync async = new PolyAsync(response.body().getData());
                                async.execute();
                            } catch (Exception e) {

                            }

                        } else {


                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        if (isActivityRunning)
                            viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        if (isActivityRunning)
                            viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.authorization_expired));
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
                        notifyApiDelay(MapsActivity.this, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }

                }

                @Override
                public void onFailure(Call<NearFarmResponse> call, Throwable t) {
                    long newMillis = AppConstants.getCurrentMills();
                    isLoaded[0] = true;
                    long diff = newMillis - currMillis;
                    notifyApiDelay(MapsActivity.this, call.request().url().toString(),
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
        isNearFarmCalled = true;
    }

    private void makePolygons(LatLng[] latLngList) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.addPolygon(new PolygonOptions().add(latLngList).strokeColor(Color.parseColor("#330000FF")).fillColor(Color.parseColor("#330000FF")));
            }
        });
    }

    class PolyAsync extends AsyncTask<String, Integer, String> {
        List<NearFarmDatum> data;

        public PolyAsync(List<NearFarmDatum> data) {
            this.data = data;
        }

        @Override
        protected String doInBackground(String... strings) {

            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    List<NewFarmLatLang> datumList = data.get(i).getGeometry();
                    try {
                        if (datumList != null && datumList.size() > 2) {
                            LatLng[] latLngs = new LatLng[datumList.size()];
                            for (int j = 0; j < datumList.size(); j++) {
                                try {
                                    latLngs[j] = (new LatLng(Double.valueOf(datumList.get(j).getLatitude()), Double.valueOf(datumList.get(j).getLongitude())));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            makePolygons(latLngs);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }
    }


    private void addFarmLocation(FarmLocationData farmLocationData) {
        progressBar.setVisibility(View.VISIBLE);
        String data = new Gson().toJson(farmLocationData);
        Log.e(TAG, "Sending New Param " + data);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        apiInterface.uploadGps(farmLocationData).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response1) {
                String error = null;
                if (response1.isSuccessful()) {
                    StatusMsgModel response = response1.body();
                    Log.e(TAG, "Sending New Param Response " + new Gson().toJson(response));
                    if (response.getStatus() == 1) {
                        String farmLoc = new Gson().toJson(farmLocationData);
                        Log.e(TAG, "FarmLocation " + farmLoc);
                        String data = new Gson().toJson(latLngList);
                        isStoring = false;
                    } else {
//                        Toast.makeText(context,"Failed to add farm location",Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
//                        Toast.makeText(context,"Failed to add farm location",Toast.LENGTH_LONG).show();
                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "Cache Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                progressBar.setVisibility(View.GONE);
                Toasty.success(context, resources.getString(R.string.area_successfully_added), Toast.LENGTH_SHORT, true).show();
                Intent intent = getIntent();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e(TAG, "Failure getOfflineData " + t.toString());
                Toast.makeText(context, "Failed to add farm location", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                Toasty.success(context, resources.getString(R.string.area_successfully_added), Toast.LENGTH_SHORT, true).show();
                Intent intent = getIntent();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private class SubmitAreaAsync extends AsyncTask<String, Void, String> {
        String area;
        float zoom = 15.0f;
        String name;
        String type;

        public SubmitAreaAsync(String area, String name,String type) {
            super();
            this.area = area;
            this.type=type;
            this.name = name;
            zoom = mMap.getCameraPosition().zoom;
        }

        @Override
        protected String doInBackground(String... params) {

            JsonObject geometry = new JsonObject();
            geometry.addProperty("type", "Polygon");
            JsonArray coordinates = new JsonArray();
            JsonArray c1 = new JsonArray();

            for (int j = 0; j < latLngList.size(); j++) {
                JsonArray c2 = new JsonArray();
                /*JsonArray c3 = new JsonArray();
                c3.add(latLngList.get(j).longitude);
                c3.add(latLngList.get(j).latitude);
                c2.add(c3);*/
                c2.add(latLngList.get(j).longitude);
                c2.add(latLngList.get(j).latitude);
                c1.add(c2);
            }
            JsonArray c2 = new JsonArray();

            c2.add(latLngList.get(0).longitude);
            c2.add(latLngList.get(0).latitude);
            c1.add(c2);

            coordinates.add(c1);
            geometry.add("coordinates", coordinates);
            String n = null;
            if (AppConstants.isValidString(n)) {
                JsonObject properties = new JsonObject();
                properties.addProperty("n", "" + n);
                geometry.add("properties", properties);
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("geoJson", geometry);
            LatLng center = AppConstants.getCenterOfPolygon(latLngList);
            if (center != null) {
                JsonObject centroid = new JsonObject();
                centroid.addProperty("lat", center.latitude);
                centroid.addProperty("long", center.longitude);
                jsonObject.add("centroid", centroid);
            }
            jsonObject.addProperty("area", area);
            jsonObject.addProperty("farm_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
            jsonObject.addProperty("comp_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
            jsonObject.addProperty("type", type);
            if (name != null)
                jsonObject.addProperty("name", name);
            else
                jsonObject.addProperty("name", "");
            jsonObject.addProperty("zoom", zoom);
            jsonObject.addProperty("cluster_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARM_CLUSTERID));

            Log.e(TAG, "SUBMITTING GEOJSON " + new Gson().toJson(jsonObject));
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
            apiInterface.submitGps(jsonObject).enqueue(new Callback<StatusMsgModel>() {
                @Override
                public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                    Log.e(TAG, "SUBMITTING " + response.code());
                    if (response.isSuccessful()) {
                        try {
                            Log.e(TAG, "SUBMITTING res " + new Gson().toJson(response.body()));
                            if (response.body().getStatus() == 10) {
                                viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.multiple_login_msg));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                if (isActivityRunning)
                                    viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                if (isActivityRunning)
                                    viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.authorization_expired));
                            } else if (response.body().getStatus() == 1) {
                                if (wayPointList != null && wayPointList.size() > 0) {
                                    addWayPoints();
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toasty.success(context, resources.getString(R.string.area_successfully_added), Toast.LENGTH_SHORT, true).show();
                                    Intent intent = getIntent();
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Log.e(TAG, "SUBMITTING err " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                    Log.e(TAG, "SUBMITTING fail " + t.toString());
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

    private class SubmitWayAsync extends AsyncTask<String, Void, String> {
        String name;
        JsonObject geometry;
        int index;

        public SubmitWayAsync( String name,JsonObject geometry,int index) {
            super();
            this.geometry=geometry;
            this.index=index;
            this.name = name;
        }

        @Override
        protected String doInBackground(String... params) {



            JsonObject jsonObject = new JsonObject();
            jsonObject.add("geoJson", geometry);
            LatLng center = AppConstants.getCenterOfPolygon(latLngList);
            if (center != null) {
                JsonObject centroid = new JsonObject();
                centroid.addProperty("lat", center.latitude);
                centroid.addProperty("long", center.longitude);
                jsonObject.add("centroid", centroid);
            }
            jsonObject.addProperty("farm_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
            jsonObject.addProperty("comp_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
            jsonObject.addProperty("type", AppConstants.AREA_TYPE.Waypoint);
            if (name != null)
                jsonObject.addProperty("name", name);
            else
                jsonObject.addProperty("name", "");
            jsonObject.addProperty("cluster_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARM_CLUSTERID));

            Log.e(TAG, "SUBMITTING GEOJSON " + new Gson().toJson(jsonObject));
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
            apiInterface.submitGps(jsonObject).enqueue(new Callback<StatusMsgModel>() {
                @Override
                public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                    Log.e(TAG, "SUBMITTING " + response.code());
                    if (response.isSuccessful()) {
                        try {
                            Log.e(TAG, "SUBMITTING res " + new Gson().toJson(response.body()));
                            if (response.body().getStatus() == 10) {
                                viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.multiple_login_msg));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                if (isActivityRunning)
                                    viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                if (isActivityRunning)
                                    viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.authorization_expired));
                            } else if (response.body().getStatus() == 1) {
                                if (index==wayPointList.size()-1)  {
                                    progressBar.setVisibility(View.GONE);
                                    Toasty.success(context, resources.getString(R.string.area_successfully_added), Toast.LENGTH_SHORT, true).show();
                                    Intent intent = getIntent();
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Log.e(TAG, "SUBMITTING err " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (index==wayPointList.size()-1)  {
                            progressBar.setVisibility(View.GONE);
                            Toasty.success(context, resources.getString(R.string.area_successfully_added), Toast.LENGTH_SHORT, true).show();
                            Intent intent = getIntent();
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                    Log.e(TAG, "SUBMITTING fail " + t.toString());
                    if (index==wayPointList.size()-1)  {
                        progressBar.setVisibility(View.GONE);
                        Toasty.success(context, resources.getString(R.string.area_successfully_added), Toast.LENGTH_SHORT, true).show();
                        Intent intent = getIntent();
                        setResult(RESULT_OK,intent);
                        finish();
                    }
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

    private void addWayPoints() {

        progressBar.setVisibility(View.VISIBLE);
        for (int i = 0; i < wayPointList.size(); i++) {
            JsonObject geoMetry = new JsonObject();
            geoMetry.addProperty("type", "Point");
            JsonArray pCoords = new JsonArray();
            pCoords.add(wayPointList.get(i).getPoint().longitude);
            pCoords.add(wayPointList.get(i).getPoint().latitude);
            geoMetry.add("coordinates", pCoords);
            JsonObject properties = new JsonObject();
//            properties.addProperty("n", "" + wayPointList.get(i).getName());
            SubmitWayAsync async=new SubmitWayAsync(wayPointList.get(i).getName(),geoMetry,i);
            async.execute();
        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {
        String area;
        float zoom = 15.0f;

        public AsyncTaskRunner(String area) {
            super();
            this.area = area;
           /* try {

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(new LatLngFarm(latPoints[0],longPoints[0]));
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
            }catch (Exception e){
                e.printStackTrace();;
            }*/

            zoom = mMap.getCameraPosition().zoom;
        }

        @Override
        protected String doInBackground(String... params) {
            lat = new String[i];
            lng = new String[i];

            for (int j = 0; j < i; j++) {
                lat[j] = String.valueOf(latPoints[j]);
                lng[j] = String.valueOf(longPoints[j]);
            }
            //demosupervisor1
            try {
                String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                VerifySendData verifySendData = new VerifySendData();
                verifySendData.setZoom(String.valueOf(zoom));
                verifySendData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                verifySendData.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                verifySendData.setArea(AppConstants.getUploadableArea(area.trim()));
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
                    public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                        String error = null;
                        isLoaded[0] = true;
                        if (response.isSuccessful()) {
                            Log.e("Area Response", new Gson().toJson(response.body()));
                            StatusMsgModel statusMsgModel = response.body();
                            if (statusMsgModel.getStatus() == 1) {

                                if (wayPointList == null || wayPointList.size() == 0) {
                                    FarmLocationData farmLocationData = new FarmLocationData(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), locationDataList,
                                            SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                                            SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                                            SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
//                                FarmLocationDataOuter dataOuter=new FarmLocationDataOuter(farmLocationData);
                                    addFarmLocation(farmLocationData);

                                } else {

                                    addWayPoints();
                                }


                                //Toast.makeText(context, statusMsgModel.getMsg(), Toast.LENGTH_SHORT).show();
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.authorization_expired));
                            } else if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivity.this, response.body().getMsg());
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(MapsActivity.this, resources.getString(R.string.failed_to_verify_farm_area));
                                //Toast.makeText(context, "Response Error Try again latter", Toast.LENGTH_SHORT).show();
                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            try {
                                error = response.errorBody().string().toString();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(MapsActivity.this, resources.getString(R.string.failed_to_verify_farm_area));
                                //Toast.makeText(context, "Error Occurred. Please try again", Toast.LENGTH_LONG).show();
                                //Toast.makeText(context, "Oops something went wrong. Please try again", Toast.LENGTH_SHORT).show();                                Log.e(TAG,"Area Response "+response.errorBody().string().toString());
                                Log.e(TAG, "Error " + response.errorBody().string().toString());
                                progressBar.setVisibility(View.INVISIBLE);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                            notifyApiDelay(MapsActivity.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                        long newMillis = AppConstants.getCurrentMills();
                        isLoaded[0] = true;
                        long diff = newMillis - currMillis;
                        notifyApiDelay(MapsActivity.this, call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);
                        Log.e(TAG, "Failure " + t.toString());
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(MapsActivity.this, resources.getString(R.string.failed_to_verify_farm_area));
                        // Toast.makeText(context, "Error Occurred. Please try again", Toast.LENGTH_LONG).show();
                        // Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
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
              /*  str_et_amount=et_exp_amount.getText().toString().trim();
                str_et_date=et_exp_date.getText().toString().trim();
                str_et_narration=et_exp_narration.getText().toString().trim();

                register("0","1",str_et_amount,str_et_date,pictureImagePath,str_et_narration,"0");*/
            //public void register(String comp_id, String sv_id, String amount,String exp_date, String img_path,String comment, String category_id){


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

    private class AsyncTaskRunnerOmitance extends AsyncTask<String, Void, String> {
        String area;
        float zoom = 15.0f;
        String name;
        List<LatLng> latLngList = new ArrayList<>();

        public AsyncTaskRunnerOmitance(String area, String name) {
            super();
            this.area = area;
            this.name = name;
            zoom = mMap.getCameraPosition().zoom;
        }

        @Override
        protected String doInBackground(String... params) {

            for (int j = 0; j < i; j++) {
                latLngList.add(new LatLng(latPoints[j], longPoints[j]));
            }

            OmitanceDetails details = new OmitanceDetails(latLngList, area);
            SendOmtanceArea sendWayPointData = new SendOmtanceArea(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), AppConstants.GPS3_TYPE.OMITANCE_AREA, details);
            sendWayPointData.setName(name);

            try {
                Log.e(TAG, "SEND OMITANCE " + new Gson().toJson(sendWayPointData));
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
                Call<StatusMsgModel> statusMsgModelCall = apiService.submitOmitanceArea(sendWayPointData);
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<StatusMsgModel>() {
                    @Override
                    public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                        String error = null;
                        isLoaded[0] = true;
                        Log.e(TAG, "Status OMITANCE " + response.code());
                        if (response.isSuccessful()) {
                            Log.e(TAG, "OMITANCE res " + response.body());

                            StatusMsgModel statusMsgModel = response.body();
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivity.this, response.body().getMsg());
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.authorization_expired));
                            } else if (statusMsgModel.getStatus() == 1) {
                                Toasty.success(context, resources.getString(R.string.area_successfully_added), Toast.LENGTH_SHORT, true).show();
                                setResult(RESULT_OK);
                                finish();

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.something_went_wrong_msg));

                                    }
                                });

                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            try {

                                error = response.errorBody().string().toString();

                                Log.e("OMITANCE Error", response.errorBody().string().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                            notifyApiDelay(MapsActivity.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {

                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        notifyApiDelay(MapsActivity.this, call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);
                        Log.e(TAG, "OMITANCE failure " + t.toString());
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
                            ConnectivityUtils.checkSpeedWithColor(MapsActivity.this, new ConnectivityUtils.ColorListener() {
                                @Override
                                public void onColorChanged(int color, String speed) {

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

    boolean canAddOutPoint = false;

    private boolean isInsideCircle(Point touchedPoint) {
        if (!canAddOutPoint) {
            int distance = (int) Math.round(Math.pow(touchedPoint.x - linear_lay_for_map.getPivotX(), 2) + Math.pow(touchedPoint.y - linear_lay_for_map.getPivotY(), 2));
            if (distance < Math.pow(linear_lay_for_map.getWidth() / 4, 2)) {
                return true;
            } else {
                return false;
            }
        } else return true;
    }


    private String convertAreaTo(String area) {
        String convertArea = area;
        convertArea = String.format("%.2f", parseDouble(area));
        return convertArea;
    }


    //    private void removeAllmarkerAndCurrentAreaPoly(){
//        for (int i=0;i<markerList.size();i++){
//            markerList.get(i).remove();
//
//        }
//    }
    private void removeAreaPoly() {
        if (UCCpolygon != null) {
            UCCpolygon.remove();
        }
    }

    List<LatLng> latLngsPrevious = new ArrayList<>();

    private class AsyncTaskRunnerShow extends AsyncTask<String, Void, String> {
        public AsyncTaskRunnerShow() {
            super();
        }
        @Override
        protected String doInBackground(String... params) {
            List<LatLng> latLngList = DataHandler.newInstance().getLatLngsFarm();
            if (latLngList != null && latLngList.size() > 2) {
                Log.e(TAG, "Poly " + new Gson().toJson(latLngList));
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                LatLng[] latLngs = new LatLng[latLngList.size()];
                for (int i = 0; i < latLngList.size(); i++) {
                    LatLng latlngforbound = new LatLng(latLngList.get(i).latitude,latLngList.get(i).longitude);
                    builder.include(latlngforbound);
                    latLngs[i] = latlngforbound;
                    latLngsPrevious.add(latlngforbound);
                }
                MapsActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                        showArea(latLngs);

                    }
                });
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

    private void showArea(LatLng[] latLng) {
        if (latLng.length > 2) {
            mMap.addPolygon(new PolygonOptions().add(latLng).strokeColor(Color.BLUE).fillColor(Color.parseColor("#33FBFB06")).strokeWidth(2f));
        }
    }


    //PREVIOU
    private void prepareData() {
        List<LatLngFarm> latLngList = DataHandler.newInstance().getLatLngList();
        if (latLngList!=null&&latLngList.size()>0) {
            List<LatLng> latLngListt=new ArrayList<>();
            back_to_farm.setVisibility(View.VISIBLE);
            back_to_farm.setImageResource(R.drawable.farm_icon_map);
            Log.e(TAG, "Poly " + new Gson().toJson(latLngList));
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            final int sizeofgps = latLngList.size();
            for (int i = 0; i < latLngList.size(); i++) {
                LatLng latlngforbound = new LatLng(Double.valueOf(latLngList.get(i).getLat()), Double.valueOf(latLngList.get(i).getLng()));
                builder.include(latlngforbound);
                latLngListt.add(latlngforbound);
            }
            int   i = latLngListt.size();

            if (i > 0) {
                final LatLng latLng = latLngListt.get(0);
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                back_to_farm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //showDialog();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                        //  mMap.setLatLngBoundsForCameraTarget();
                    }
                });
                showArea(i,latLngListt);

            }

        }else {
            back_to_farm.setVisibility(View.GONE);
        }
    }
    private void showArea(int i,List<LatLng> latLngList) {

        if (i < 3) {
            //Toast.makeText(context, "Please submit atleast 3 points", Toast.LENGTH_SHORT).show();
            ViewFailDialog viewFailDialog = new ViewFailDialog();
            viewFailDialog.showDialog(MapsActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.should_have_at_least_two_points_to_display));
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
            tvarea.setText(ar + " " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL));
            PolygonOptions   options = new PolygonOptions()
                    .addAll(latLngs)
                    .strokeColor(Color.RED)
                    .strokeWidth(2f)
                    .fillColor(Color.parseColor("#33effb5e"));

            mMap.addPolygon(options);


        }
    }

}
