package com.i9930.croptrails.Maps.WayPoint;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.geometry.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;
import com.i9930.croptrails.Maps.VerifyArea.MapsActivitySamunnati;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.SendWayPointData;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.WayPoint;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static com.i9930.croptrails.Maps.WalkAround.MapsActivityNew.getBitmapFromVectorDrawable;

public class AddWayPointActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    double area_mult_factor = 1.0;
    double area_mult_factor_def = 0.000247105;
    private GoogleMap mMap;
    String TAG = "MapsActivityEasyMode";
    LocationRequest mLocationRequest;
    LocationManager locationManager;
    Toolbar mActionBarToolbar;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Context context;
    FusedLocationProviderClient mFusedLocationClient;
    Button view_area_button;
    TextView tvarea;
    TextView butt_clear;
    String mprovider;
    Location location;
    GifImageView progressBar;
    Boolean can_add = true;
    @BindView(R.id.linear_lay_for_map)
    LinearLayout linear_lay_for_map;
    TextView title;
    Resources resources;
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
    String farmLatitude, farmLongitude, submitType;
    ImageView helpImage;

    private interface MAP_TYPE {
        public static final int DEFAULT_MAP = 101;
        public static final int SATELLITE_MAP = 102;
    }


    LatLng firstAndLastPoint;

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
        helpImage.setVisibility(View.GONE);

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
                    ConnectivityUtils.checkSpeedWithColor(AddWayPointActivity.this, new ConnectivityUtils.ColorListener() {
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
                }
            }
        });


    }

    Bitmap bmp;
    CircleImageView back_to_farm;

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
        setContentView(R.layout.activity_add_way_point);


        back_to_farm = findViewById(R.id.back_to_farm);

        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        context = this;
        ButterKnife.bind(this);
        initViews();
        bmp = getBitmapFromVectorDrawable(context, R.drawable.marker_green_24px);
        title.setText("Pin Drop Mode");
        areaUnitTv.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL));
        area_mult_factor = SharedPreferencesMethod.getDoubleSharedPreferences(context, SharedPreferencesMethod.AREA_MULTIPLICATON_FACTOR);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setFarmerNameAndKhasra();
        progressBar.setVisibility(View.VISIBLE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mprovider = locationManager.getBestProvider(criteria, false);
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locationManager.getLastKnownLocation(mprovider);
        }
        onClicks();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        if (intent != null) {
            farmLatitude = intent.getStringExtra("farmLat");
            farmLongitude = intent.getStringExtra("farmLon");
            submitType = intent.getStringExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE);
        }
        boolean isWayPointEnabled = SharedPreferencesMethod.getBoolean(this, SharedPreferencesMethod.IS_WAY_POINT_ENABLED);
        if (submitType != null && submitType.equals(AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE)) {
            isWayPointEnabled = false;
        }
        normal_mode_butt.setText(resources.getString(R.string.submit));
        if (isWayPointEnabled) {
            normal_mode_butt.setVisibility(View.VISIBLE);
            normal_mode_butt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (wayPointList == null || wayPointList.size() == 0) {
                        Toasty.error(context, "Please add at lease 1 way point", Toast.LENGTH_SHORT, false).show();
                    }else {
                        new AlertDialog.Builder(context)
                                .setMessage(resources.getString(R.string.sure_to_submit_way_point))
                                .setCancelable(false)
                                .setPositiveButton(resources.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        if (wayPointList == null || wayPointList.size() == 0) {
                                            Toasty.error(context, "Please add at lease 1 way point", Toast.LENGTH_SHORT, false).show();
                                        } else {
                                            SendWayPointData sendWayPointData = new SendWayPointData(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID),
                                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID),
                                                    AppConstants.GPS3_TYPE.WAY_POINT, wayPointList);
                                            addWayPoints();
                                        }
                                    }
                                })
                                .setNegativeButton(resources.getString(R.string.no), null)
                                .show();
                    }
                }
            });
        } else {
            normal_mode_butt.setVisibility(View.GONE);
        }

    }

    private void onClicks() {
        submit_area_butt.setVisibility(View.GONE);
        view_area_button.setVisibility(View.GONE);
        butt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLast();
            }
        });

        submit_area_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setMessage(resources.getString(R.string.sure_to_submit_way_point))
                        .setCancelable(false)
                        .setPositiveButton(resources.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (wayPointList == null || wayPointList.size() == 0) {
                                    Toasty.success(context, "Please add at lease 1 way point", Toast.LENGTH_SHORT, true).show();
                                } else {
                                    SendWayPointData sendWayPointData = new SendWayPointData(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                                            SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                                            SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID),
                                            SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID),
                                            AppConstants.GPS3_TYPE.WAY_POINT, wayPointList);
//                                    addWayPoints(sendWayPointData);
                                    addWayPoints();
                                }
                            }
                        })
                        .setNegativeButton(resources.getString(R.string.no), null)
                        .show();
            }
        });
    }

    void removeLast() {
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        if (wayPointList != null && wayPointList.size() > 0) {
            int lastIndex = wayPointList.size() - 1;
            wayPointList.remove(lastIndex);
            Marker marker = wayPointMaekers.get(lastIndex);
            marker.remove();
            wayPointMaekers.remove(lastIndex);

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
            /*LatLng center = AppConstants.getCenterOfPolygon(latLngList);
            if (center != null) {
                JsonObject centroid = new JsonObject();
                centroid.addProperty("lat", center.latitude);
                centroid.addProperty("long", center.longitude);
                jsonObject.add("centroid", centroid);
            }*/
            jsonObject.addProperty("farm_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
//            jsonObject.addProperty("farm_id", "20202");
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
                                viewFailDialog.showSessionExpireDialog(AddWayPointActivity.this, resources.getString(R.string.multiple_login_msg));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                if (isActivityRunning)
                                    viewFailDialog.showSessionExpireDialog(AddWayPointActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                if (isActivityRunning)
                                    viewFailDialog.showSessionExpireDialog(AddWayPointActivity.this, resources.getString(R.string.authorization_expired));
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
                            if (index==wayPointList.size()-1)  {
                                progressBar.setVisibility(View.GONE);
                                Toasty.success(context, resources.getString(R.string.area_successfully_added), Toast.LENGTH_SHORT, true).show();
                                Intent intent = getIntent();
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                            Log.e(TAG, "SUBMITTING err " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
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
                        Toasty.success(context, resources.getString(R.string.way_point_successfully_added), Toast.LENGTH_SHORT, true).show();
                        Intent intent = getIntent();
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
//                        Toast.makeText(context,"Failed to add farm location",Toast.LENGTH_LONG).show();
                        Toasty.success(context, resources.getString(R.string.way_point_failed), Toast.LENGTH_SHORT, true).show();
                    }
                } else {
                    try {
                        Toasty.success(context, resources.getString(R.string.way_point_failed), Toast.LENGTH_SHORT, true).show();
                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "Cache Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e(TAG, "Failure getOfflineData " + t.toString());
                Toasty.success(context, resources.getString(R.string.way_point_failed), Toast.LENGTH_SHORT, true).show();

            }
        });
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

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapChoosen = AddWayPointActivity.MAP_TYPE.SATELLITE_MAP;
        mapTypeImage.setImageResource(R.drawable.map_type_normal);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000000); //  minute interval
        mLocationRequest.setFastestInterval(100000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableGPS();
        } else {
        }
        mapTypeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapChoosen == AddWayPointActivity.MAP_TYPE.DEFAULT_MAP) {
                    mapChoosen = AddWayPointActivity.MAP_TYPE.SATELLITE_MAP;
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    mapTypeImage.setImageResource(R.drawable.map_type_normal);
                } else {
                    mapChoosen = AddWayPointActivity.MAP_TYPE.DEFAULT_MAP;
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mapTypeImage.setImageResource(R.drawable.map_type_satellite);

                }
            }
        });
        prepareData();
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


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {

                Log.d("CheckAreaMapActivity", "onMarkerDragStart..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                for (int i = 0; i < wayPointList.size(); i++) {
                    WayPoint wayPoint = wayPointList.get(i);
                    if (wayPointMaekers.get(i).equals(arg0)) {
                        Log.e(TAG, "Got marker in list at " + i);
                        LatLng latLng = new LatLng(arg0.getPosition().latitude, arg0.getPosition().longitude);
                        wayPoint.setPoint(latLng);
                        wayPointList.set(i, wayPoint);
                        break;
                    }
                }
                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.i("System out", "onMarkerDrag...");
            }
        });

    }

    List<WayPoint> wayPointList = new ArrayList<>();
    List<Marker> wayPointMaekers = new ArrayList<>();

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
                    final MarkerOptions marker = new MarkerOptions().position(latLng)
                            .title(etPointName.getText().toString().trim())
                            .icon(BitmapDescriptorFactory.fromBitmap(bmp));
                    Marker marker1=mMap.addMarker(marker);
                    wayPointMaekers.add(marker1);
                }

            }
        });

        dialog.show();


    }

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


                if (mLastLocation != null && !isNearFarmCalled) {
//                    getNearFarms(mLastLocation.getLatitude() + "", mLastLocation.getLongitude() + "");
                    isNearFarmCalled = true;
                }
                if (locMoveCount == 0) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));

                    locMoveCount++;
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    };
    int locMoveCount = 0;

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
                                ActivityCompat.requestPermissions(AddWayPointActivity.this,
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
        Toast.makeText(context, "Gps Disabled", Toast.LENGTH_SHORT).show();

        enableGPS();
    }

    private void enableGPS() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(AddWayPointActivity.this).build();
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

                                status.startResolutionForResult(AddWayPointActivity.this, 1000);
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


    private boolean isInsideCircle(Point touchedPoint) {
        int distance = (int) Math.round(Math.pow(touchedPoint.x - linear_lay_for_map.getPivotX(), 2) + Math.pow(touchedPoint.y - linear_lay_for_map.getPivotY(), 2));

        if (distance < Math.pow(linear_lay_for_map.getWidth() / 10, 2)) {
            return true;
        } else {
            return false;
        }
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


    @Override
    protected void onStart() {
        super.onStart();
        isActivityRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityRunning = false;
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    ViewFailDialog viewFailDialog = new ViewFailDialog();
    boolean isNearFarmCalled = false;
    boolean isActivityRunning;

    //PREVIOU
    private void prepareData() {
        List<LatLng> latLngList = DataHandler.newInstance().getLatLngsFarm();
        if (latLngList != null && latLngList.size() > 0) {
            List<LatLng> latLngListt = new ArrayList<>();
            back_to_farm.setVisibility(View.VISIBLE);
            back_to_farm.setImageResource(R.drawable.farm_icon_map);
            Log.e(TAG, "Poly " + new Gson().toJson(latLngList));
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            final int sizeofgps = latLngList.size();
            for (int i = 0; i < latLngList.size(); i++) {
                LatLng latlngforbound = new LatLng(latLngList.get(i).latitude, latLngList.get(i).longitude);
                builder.include(latlngforbound);
                latLngListt.add(latlngforbound);
            }
            int i = latLngListt.size();

            if (i > 0) {
                final LatLng latLng = latLngListt.get(0);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                back_to_farm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                    }
                });
                showArea(i, latLngListt);

            }

        } else {
            back_to_farm.setVisibility(View.GONE);
        }
    }

    private void showArea(int i, final List<LatLng> latLngList) {

        if (i < 3) {
            ViewFailDialog viewFailDialog = new ViewFailDialog();
            viewFailDialog.showDialog(AddWayPointActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.should_have_at_least_two_points_to_display));
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
            String a = String.valueOf(String.format("%.4f", SphericalUtil.computeArea(latLngs) * 0.000247105));
            String ar = AppConstants.getShowableAreaWithConversion(a, area_mult_factor);
            tvarea.setText(ar + " " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL));
            PolygonOptions options = new PolygonOptions()
                    .addAll(latLngs)
                    .strokeColor(Color.RED)
                    .strokeWidth(2f)
                    .fillColor(Color.parseColor("#33effb5e"));

            mMap.addPolygon(options);

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {
                    {
                        if (PolyUtil.containsLocation(point, latLngList, false))
                            showDialog(point);
                        else
                            Toasty.error(context,resources.getString(R.string.point_can_not_be_outside_farm),Toast.LENGTH_SHORT,false).show();

                    }
                    try {
                        mMap.getUiSettings().setScrollGesturesEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        }
    }

}
