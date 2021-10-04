package com.i9930.croptrails.Maps.WalkAround;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.os.Looper;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentActivity;

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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;

import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.VerifySendData;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

public class MapsActivityNew extends FragmentActivity implements SensorEventListener, OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


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
    //Variables used in calculations
    private long stepCount = 0;
    private long lastSteps = 0;
    private String compassOrientation;
    private double lastDistance = 0;
    private int prevStepCount = 0;
    private long stepTimestamp = 0;
    private long stepTimestampToStore = 0;
    private String stepTimestampToStoreR = "";
    private long startTime = 0;
    long timeInMilliseconds = 0;
    long elapsedTime = 0;
    long updatedTime = 0;
    private int speed = 0;
    private float distance = 0;
    private float[] accelValues;
    private float[] magnetValues;
    private String timeString;
    private String elapsedString;

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

    //Show cardinal point (compass orientation) according to degree
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

    long stepCountToStore = 0;

    public float getDistanceRun(long steps) {
        float distance = (float) (steps * 78) / (float) 100000;
        this.distance = distance*1000;
        return distance;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        switch (event.sensor.getType()) {
            /*case (Sensor.TYPE_ACCELEROMETER):
                accelValues = event.values;
                break;*/
            case (Sensor.TYPE_GYROSCOPE):
                gyroscop[0] = event.values[0];
                gyroscop[1] = event.values[1];
                gyroscop[2] = event.values[2];
                break;
            case (Sensor.TYPE_STEP_COUNTER):
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
                stepTimestampToStore=System.currentTimeMillis();

                try {
                    Date c;
                    SimpleDateFormat df;
                    c = Calendar.getInstance().getTime();
                    df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    stepTimestampToStoreR = df.format(c);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case (Sensor.TYPE_STEP_DETECTOR):
                if (stepCounter == null) {
                    stepCountToStore = stepCountToStore + 1;
                    countSteps((int) event.values[0]);
                    calculateSpeed(event.timestamp, 1);
                    getDistanceRun(stepCountToStore);
                    stepTimestampToStore=System.currentTimeMillis();
                    try {
                        Date c;
                        SimpleDateFormat df;
                        c = Calendar.getInstance().getTime();
                        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        stepTimestampToStoreR = df.format(c);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;

        }

        if (accelValues != null && magnetValues != null) {
            float rotation[] = new float[9];
            float orientation[] = new float[3];
            if (SensorManager.getRotationMatrix(rotation, null, accelValues, magnetValues)) {
                SensorManager.getOrientation(rotation, orientation);
                float azimuthDegree = (float) (Math.toDegrees(orientation[0]) + 360) % 360;
                float orientationDegree = Math.round(azimuthDegree);
                getOrientation(orientationDegree);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    SupportMapFragment mapFrag;
    GoogleMap mMap;
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
    TextView startCapturingButton;
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
    LinearLayout linear_lay_for_map;
    String TAG = "MapsActivityOffline";
    GifImageView update_standing_progress;
    TextView area_unit_label;
    double area_mult_factor = 1.0;
    double area_mult_factor_def = 0.000247105;
    double multiplicationFactor = 1.0;

    private final Map<String, MarkerOptions> mMarkers = new ConcurrentHashMap<String, MarkerOptions>();
   /* BottomSheetDialog bottomSheetDialog;
    TextView tv_easy_mode,tv_normal_mode;
    private boolean isEasyMode;*/

    ImageView mapTypeImage;
    int mapChoosen = 101;
    boolean isStoring = false;

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private interface MAP_TYPE {
        public static final int DEFAULT_MAP = 101;
        public static final int SATELLITE_MAP = 102;
    }

    Bitmap bmp;
    TextView submitAreaButton;
    String area = "";
    TextView countTv;

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    ImageView listImageButton;
    float gravity[];
    float gyroscop[]= new float[3];

    private void setupCompass() {
        compass = new Compass(this);
        Compass.CompassListener cl = new Compass.CompassListener() {
            @Override
            public void onNewAzimuth(float azimuth, float[] mGravity) {
                adjustArrow(azimuth);
                gravity = mGravity;
            }
        };
        compass.setListener(cl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        compass.start();
        try {
            wl.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    PowerManager.WakeLock wl;

    @Override
    protected void onStop() {
        super.onStop();
        compass.stop();
        try {
            wl.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float currentAzimuth;
    private String currentDirection;

    private void adjustArrow(float azimuth) {
        /*Log.d(TAG, "will set rotation from " + currentAzimuth + " to "
                + azimuth);*/
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

//        currentDirection
    }

    private Compass compass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        resources = getResources();
        context = this;
        linear_lay_for_map = findViewById(R.id.linear_lay_for_map);
        mapTypeImage = findViewById(R.id.mapTypeImage);
        initViews();
//        Toast.makeText(context, "data", Toast.LENGTH_SHORT).show();
//        title.setText(resources.getString(R.string.capture_farm_area_maps_activity_title));
        title.setText("Walk Around Mode");
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);

        //Toolbar toolbar = view.findViewById(R.id.toolbar);
       /* mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
        registerSensors();
        setupCompass();

        listImageButton = findViewById(R.id.listImageButton);
        countTv = findViewById(R.id.countTv);
        progressBar.setVisibility(View.VISIBLE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        listImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(context, FarmListActivity.class));
            }
        });
        mprovider = locationManager.getBestProvider(criteria, false);
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locationManager.getLastKnownLocation(mprovider);
//            locationManager.requestLocationUpdates(mprovider, 1000, 1, MapsActivityNew.this);

        }
//        next_butt.setEnabled(false);
        area_unit_label.setText(SharedPreferencesMethod.getString(MapsActivityNew.this,SharedPreferencesMethod.AREA_UNIT_LABEL));
        latPoints = new Double[100];
        longPoints = new Double[100];
        Log.e(TAG, "Multiplication Factor " + area_mult_factor);


        bmp = getBitmapFromVectorDrawable(context, R.drawable.ic_current_location);

        submitAreaButton = findViewById(R.id.submitAreaButton);
        startCapturingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStoring) {
                    if (location != null && latLngList.size() == 0) {
                        storeData(new LatLng(location.getLatitude(), location.getLongitude()));
                    }
                    isStoring = true;
                    startCapturingButton.setText("Stop Capturing");
                    next_butt.setVisibility(View.VISIBLE);
                    if (latLngList.size() > 3 && !TextUtils.isEmpty(tvarea.getText().toString()) && Float.valueOf(tvarea.getText().toString().trim()) > 0) {
                        submitAreaButton.setVisibility(View.VISIBLE);
                    } else {
                        submitAreaButton.setVisibility(View.GONE);
                    }
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                } else {
                    isStoring = false;
                    if (latLngList.size() > 0)
                        startCapturingButton.setText("Continue");
                    else
                        startCapturingButton.setText("Start");
                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                    if (latLngList.size() > 3 && !TextUtils.isEmpty(tvarea.getText().toString()) && Float.valueOf(tvarea.getText().toString().trim()) > 0) {
                        submitAreaButton.setVisibility(View.VISIBLE);
                    } else {
                        submitAreaButton.setVisibility(View.GONE);
                    }
                }
            }
        });

        next_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next_butt.setVisibility(View.GONE);
                submitAreaButton.setVisibility(View.GONE);
                latLngList.clear();
                startCapturingButton.setText("Start Capturing");
                area = "";
                tvarea.setText("0.00");
                countTv.setText("0");
                count = 1;
                mMap.clear();
                isStoring = false;
            }
        });
        submitAreaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(context)
                        .setMessage(resources.getString(R.string.sure_to_submit_area))
                        .setCancelable(false)
                        .setPositiveButton(resources.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (Double.parseDouble(tvarea.getText().toString()) > 0) {

                                    FarmLocationData farmLocationData=new FarmLocationData(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.SVFARMID),locationDataList,
                                            SharedPreferencesMethod.getString(context,SharedPreferencesMethod.USER_ID),
                                            SharedPreferencesMethod.getString(context,SharedPreferencesMethod.TOKEN),
                                            SharedPreferencesMethod.getString(context,SharedPreferencesMethod.COMP_ID));
//                                FarmLocationDataOuter dataOuter=new FarmLocationDataOuter(farmLocationData);
                                    addFarmLocation(farmLocationData);
//                                    progressBar.setVisibility(View.VISIBLE);
//                                    SubmitAreaAsync asyncTaskRunner = new SubmitAreaAsync(tvarea.getText().toString());
//                                    asyncTaskRunner.execute();
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

        butt_clear.setVisibility(View.GONE);
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

    }

    private void addFarmLocation(FarmLocationData farmLocationData) {
        progressBar.setVisibility(View.VISIBLE);
        String data=new Gson().toJson(farmLocationData);
        Log.e(TAG, "Sending New Param " + data);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        apiInterface.uploadGps(farmLocationData).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response1) {
                String error = null;
                if (response1.isSuccessful()) {
                    StatusMsgModel response = response1.body();
                    Log.e(TAG, "Sending New Param Response "  + new Gson().toJson(response));
                    if(response.getStatus()==1){
                        String farmLoc=new Gson().toJson(farmLocationData);
                        Log.e(TAG,"FarmLocation "+farmLoc);
                        String data = new Gson().toJson(latLngList);
                        isStoring = false;
//                        Toast.makeText(context, "Farm location added successfully" , Toast.LENGTH_LONG).show();
                        area = "";
                        count = 1;
                        tvarea.setText("0.00");
                        next_butt.setVisibility(View.GONE);
                        submitAreaButton.setVisibility(View.GONE);
                        latLngList.clear();
                        startCapturingButton.setText("Start Capturing");
                        countTv.setText("0");
                        mMap.clear();
                    }else {
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
                Toast.makeText(context,"Failed to add farm location",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                Toasty.success(context, resources.getString(R.string.area_successfully_added), Toast.LENGTH_SHORT, true).show();
                Intent intent = getIntent();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void initViews() {
        title = (TextView) findViewById(R.id.tittle);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        tvarea = (TextView) findViewById(R.id.area_tv);
        startCapturingButton = findViewById(R.id.submit_butt);
        butt_clear = findViewById(R.id.clear_butt);
        next_butt = findViewById(R.id.next_butt);
        progressBar = findViewById(R.id.progressBar_cyclic);
        area_unit_label = (TextView) findViewById(R.id.area_unit_label);


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
        mLocationRequest.setInterval(3000); // two minute interval
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//                locationManager.requestLocationUpdates(mprovider, 1000, 1, MapsActivity.this);
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//            locationManager.requestLocationUpdates(mprovider, 1000, 1, MapsActivity.this);
            mMap.setMyLocationEnabled(true);
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

    private void submit_area() {
        Polygon UCCpolygon;

        if (latLngList.size() < 3) {
            //Toast.makeText(context, "Please view_area_button atleast 3 points", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "" + resources.getString(R.string.please_submit_at_least_3_points), Toast.LENGTH_SHORT).show();
        } else {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            LatLng[] latLng = new LatLng[latLngList.size()];
            List<LatLng> latLngs = new ArrayList<>();
            for (int j = 0; j < latLngList.size(); j++) {
                builder.include(latLngList.get(j));
                latLng[j] = new LatLng(latPoints[j], longPoints[j]);
                latLngs.add(new LatLng(latPoints[j], longPoints[j]));
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
            UCCpolygon = mMap.addPolygon(new PolygonOptions().add(latLng).strokeColor(Color.RED).fillColor(Color.parseColor("#310000FF")));
            Log.i("Location", "computeArea " + SphericalUtil.computeArea(latLngList));
            String a = String.valueOf(String.format("%.4f", SphericalUtil.computeArea(latLngList) * area_mult_factor_def));
            tvarea.setText(a);
            area = a;
        }

    }

    List<LatLng> latLngList = new ArrayList<>();
    int count = 1;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivityOffline", "Location: " + location.getLatitude() + "," + location.getLongitude() + " Accuracy " + location.getAccuracy());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30));
                progressBar.setVisibility(View.INVISIBLE);
                if (isStoring) {
                    storeData(latLng);
                }
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
                                ActivityCompat.requestPermissions(MapsActivityNew.this,
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
//                        if (mprovider != null && !TextUtils.isEmpty(mprovider))
//                            locationManager.requestLocationUpdates(mprovider, 1000, 1, MapsActivity.this);

                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //Toast.makeText(this, resources.getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                    Toast.makeText(context, "" + resources.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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
            if (i < 100) {
                final MarkerOptions marker = new MarkerOptions().position(ll).title(name);
                mMarkers.put(name, marker);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMap.addMarker(marker);
                        latPoints[i] = ll.latitude;
                        longPoints[i] = ll.longitude;
                        i++;
                    }
                });
            } else {
                //Toast.makeText(context, resources.getString(R.string.cant_add_more_points), Toast.LENGTH_LONG).show();
                Toast.makeText(context, resources.getString(R.string.cant_add_more_points), Toast.LENGTH_LONG).show();

            }
        } else {
            // Toast.makeText(context, resources.getString(R.string.please_clear_then_add), Toast.LENGTH_LONG).show();
            Toast.makeText(context, resources.getString(R.string.please_clear_then_add), Toast.LENGTH_LONG).show();

        }
    }


    private void remove(String name) {
        mMarkers.remove(name);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.clear();

                for (MarkerOptions item : mMarkers.values()) {
                    mMap.addMarker(item);
                }
            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.i("MapsActivityOffline", "Location: " + location.getLatitude() + "," + location.getLongitude() + " Accuracy " + location.getAccuracy());
            /*mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            //Place current location marker
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30));
            progressBar.setVisibility(View.INVISIBLE);
            if (isStoring) {
                storeData(latLng);
            }*/
        }

    }

    private void storeData(LatLng latLng) {
        if (latLngList.size() == 0) {
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("" + latLngList.size())
                    .icon(BitmapDescriptorFactory.fromBitmap(bmp)));
            latLngList.add(latLng);
            countTv.setText("" + count);
            count++;
            LocationData data=new LocationData(latLng.latitude,latLng.longitude,gravity,stepCountToStore,distance,currentAzimuth,""+speed,gyroscop,stepTimestampToStore,stepTimestampToStoreR,0);
            locationDataList.add(data);

        } else {
            LatLng prev = latLngList.get(latLngList.size() - 1);
            double d = meterDistanceBetweenPoints(latLng.latitude, latLng.longitude, prev.latitude, prev.longitude);
//                    d = d * 3.28084;
            if (d > 1) {
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("" + latLngList.size())
                        .icon(BitmapDescriptorFactory.fromBitmap(bmp)));

                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(prev, latLng)
                        .width(2)
                        .color(Color.RED));
                latLngList.add(latLng);
                countTv.setText("" + count);
                count++;
                submit_area();
                LocationData data=new LocationData(latLng.latitude,latLng.longitude,gravity,stepCountToStore,distance,currentAzimuth,""+speed,gyroscop,stepTimestampToStore,stepTimestampToStoreR,0);
                locationDataList.add(data);


            }
        }

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
            LatLng firstAndLastPoint=latLngList.get(0);
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
//                                progressBar.setVisibility(View.INVISIBLE);

                                FarmLocationData farmLocationData=new FarmLocationData(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.SVFARMID),locationDataList,
                                        SharedPreferencesMethod.getString(context,SharedPreferencesMethod.USER_ID),
                                        SharedPreferencesMethod.getString(context,SharedPreferencesMethod.TOKEN),
                                        SharedPreferencesMethod.getString(context,SharedPreferencesMethod.COMP_ID));
//                                FarmLocationDataOuter dataOuter=new FarmLocationDataOuter(farmLocationData);
                                addFarmLocation(farmLocationData);

                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivityNew.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivityNew.this, resources.getString(R.string.authorization_expired));
                            } else if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivityNew.this, response.body().getMsg());
                            } else if (statusMsgModel.getStatus() == 4) {
                                String msg;
                                if (response.body().getSatMsg() != null && !TextUtils.isEmpty(response.body().getSatMsg()))
                                    msg = response.body().getSatMsg();
                                else
                                    msg = "Invalid area selected, Please select area again.";
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(context, msg);
                            } else {

                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_verify_farm_area));
                                //Toast.makeText(context, "Response Error Try again latter", Toast.LENGTH_SHORT).show();
                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivityNew.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivityNew.this, resources.getString(R.string.authorization_expired));
                        } else {
                            try {
                                error = response.errorBody().string().toString();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_verify_farm_area));
                                Log.e(TAG, "Error " + error);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                        Log.e(TAG, "Failure " + t.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_verify_farm_area));
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });

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


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        mprovider = provider;
    }

    @Override
    public void onProviderDisabled(String provider) {
        enableGPS();
    }

    private void enableGPS() {
        /*if (mGoogleApiClient == null) {*/

        //Toast.makeText(context, "Coming in enable gps", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(MapsActivityNew.this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        if (mLocationRequest==null) {
         mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(3000);
            mLocationRequest.setFastestInterval(3000);
        }
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
                        // Toast.makeText(this, "Gps Enabled", Toast.LENGTH_SHORT).show();

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
                            //Toast.makeText(this, "Requesting for gps", Toast.LENGTH_SHORT).show();

                            status.startResolutionForResult(MapsActivityNew.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Toast.makeText(this, "Requesting for gps in catch", Toast.LENGTH_SHORT).show();

                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have
                        // no way to fix the
                        // settings so we won't show the dialog.
                        //Toast.makeText(this, "Settings change unavailable", Toast.LENGTH_SHORT).show();
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


}
