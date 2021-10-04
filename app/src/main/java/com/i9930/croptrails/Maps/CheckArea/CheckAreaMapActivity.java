package com.i9930.croptrails.Maps.CheckArea;

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
import android.os.Looper;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineString;
import com.google.maps.android.data.geojson.GeoJsonMultiLineString;
import com.google.maps.android.data.geojson.GeoJsonMultiPoint;
import com.google.maps.android.data.geojson.GeoJsonMultiPolygon;
import com.google.maps.android.data.geojson.GeoJsonPoint;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;
import com.google.maps.android.data.geojson.GeoJsonPolygon;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;
import com.google.maps.android.geometry.Point;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import com.i9930.croptrails.AddFarm.AddFarmActivity;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Maps.selectFarmerFarm.SelectFarmActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static java.lang.Double.parseDouble;
import static com.i9930.croptrails.Utility.AppConstants.getCoordinatesFromGeometry;

public class CheckAreaMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    String TAG = "CheckAreaMapActivity";
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    LocationManager locationManager;
    Toolbar mActionBarToolbar;
    double area_mult_factor = 1.0;

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
    // Button next_butt;
    GifImageView progressBar;
    Boolean can_add = true;
    @BindView(R.id.linear_lay_for_map)
    LinearLayout linear_lay_for_map;
    TextView title;
    Resources resources;
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();
    TextView latTv, lngTv, zoomTv;
    BottomSheetDialog bottomSheetDialog;
    TextView tv_easy_mode, tv_normal_mode;
    @BindView(R.id.submit_area_butt)
    TextView submit_area_butt;
    @BindView(R.id.normal_mode_butt)
    TextView normal_mode_butt;
    @BindView(R.id.areaUnitTv)
    TextView areaUnitTv;
    @BindView(R.id.kasraAndFarmerLayout)
    LinearLayout kasraAndFarmerLayout;

    @BindView(R.id.mapTypeImage)
    ImageView mapTypeImage;
    int mapChoosen = 101;

    private interface MAP_TYPE {
        public static final int DEFAULT_MAP = 101;
        public static final int SATELLITE_MAP = 102;
    }

    private final Map<String, MarkerOptions> mMarkers = new ConcurrentHashMap<String, MarkerOptions>();

    private void initViews() {
        title = (TextView) findViewById(R.id.tittle);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        progressBar = findViewById(R.id.progressBar_cyclic);
        tvarea = (TextView) findViewById(R.id.area_tv);
        submit = findViewById(R.id.submit_butt);
        butt_clear = findViewById(R.id.clear_butt);
        latTv = findViewById(R.id.latTv);
        lngTv = findViewById(R.id.lngTv);
        zoomTv = findViewById(R.id.zoomTv);
        kasraAndFarmerLayout.setVisibility(View.GONE);

    }

    private boolean isEasyMode;

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("BOundary.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

//            Log.e(TAG, "myjsondata" + json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void getGeoJson() {
        LatLng latLng = new LatLng(-14.944784875088372, -47.900390625);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                // .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_my_location))
                .title("marker" + (i - 1))
                .draggable(true);
//        mMap.addMarker(markerOptions);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

        try {
            String json = loadJSONFromAsset();

//            Log.e(TAG, "loadJSONFromAsset " + json);
            JSONObject jsonObject = new JSONObject(json);
            GeoJsonLayer layer = new GeoJsonLayer(mMap, jsonObject);
            for (GeoJsonFeature feature : layer.getFeatures()) {

                if (feature != null) {

                    if (feature.getGeometry() != null && AppConstants.isValidString(feature.getGeometry().getGeometryType()) &&
                            feature.getGeometry().getGeometryType().trim().equalsIgnoreCase("point")) {
                        String name = feature.getProperty("n");
                        if (feature.getPointStyle() != null)
                            feature.getPointStyle().setTitle(name);
                        GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();
//                        pointStyle.setIcon(pointIcon);
                        pointStyle.setTitle(name);
                        pointStyle.setSnippet("Earthquake occured " + feature.getProperty("place"));
                        // Assign the point style to the feature
                        feature.setPointStyle(pointStyle);
                    } else {
                        GeoJsonPolygonStyle style = new GeoJsonPolygonStyle();
                        try {
                            Log.e(TAG, "loadJSONFromAsset type Feature " + feature.getGeometry().toString());
                            style.setFillColor(AppConstants.POLYGON.FILL);
                            style.setStrokeColor(AppConstants.POLYGON.STROKE);
                            feature.setPolygonStyle(style);
                            if (layer.getBoundingBox()!=null)
                                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(layer.getBoundingBox(), 50));
                            else{
                                if(feature.getGeometry() != null && AppConstants.isValidString(feature.getGeometry().getGeometryType()) &&
                                        feature.getGeometry().getGeometryType().trim().equalsIgnoreCase("multiPolygon")){

                                    Log.e(TAG, "loadJSONFromAsset MUL " + new Gson().toJson(AppConstants.getCoordinatesFromGeometryMultiPoly(feature.getGeometry())));

                                }

                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                if (feature.hasGeometry()) {
                                    Geometry geometry = feature.getGeometry();
                                    for (LatLng latLngg : getCoordinatesFromGeometry(geometry)) {
                                        builder.include(latLngg);
                                    }
                                }

                                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));

                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }
//                feature.getPolygonOptions().fillColor(R.color.map_area_yellow);


                        if (feature.getProperties() != null)
                            Log.e(TAG, "loadJSONFromAsset Feature " + feature.toString());

                        if (layer.getBoundingBox() != null)
                            Log.e(TAG, "loadJSONFromAsset Feature bb " + layer.getBoundingBox());
                    }
                }

            }

//            GeoJsonPolygonStyle style = layer.getDefaultPolygonStyle();
//            style.setStrokeColor(R.color.orange);
//            style.setStrokeWidth(2);
//            style.setFillColor(R.color.yel_gre_mix_dark);

            layer.addLayerToMap();

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        setContentView(R.layout.activity_check_area_map);
        final String languageCode = SharedPreferencesMethod2.getString(CheckAreaMapActivity.this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        context = this;
        ButterKnife.bind(this);
        initViews();
        title.setText(resources.getString(R.string.check_area_title));

        mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mprovider = locationManager.getBestProvider(criteria, false);
        //gpslocation=(TextView)findViewById(R.id.gps_location);
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locationManager.getLastKnownLocation(mprovider);
            //locationManager.requestLocationUpdates(mprovider, 100, 1, this);

        }
        latPoints = new Double[100];
        longPoints = new Double[100];
        onClicks();


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /*bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.dialog_verify_area_bottom_sheet);
        Window window2 = bottomSheetDialog.getWindow();
        window2.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tv_easy_mode=bottomSheetDialog.findViewById(R.id.tv_easy_mode);
        tv_normal_mode=bottomSheetDialog.findViewById(R.id.tv_normal_mode);

        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();

        tv_easy_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEasyMode=true;
                bottomSheetDialog.dismiss();
            }
        });

        tv_normal_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEasyMode=false;
                bottomSheetDialog.dismiss();
            }
        });*/
        submit_area_butt.setVisibility(View.GONE);
        normal_mode_butt.setVisibility(View.GONE);

        normal_mode_butt.setText("Assign To Farm");

        normal_mode_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        area_mult_factor = SharedPreferencesMethod.getDoubleSharedPreferences(context, SharedPreferencesMethod.AREA_MULTIPLICATON_FACTOR);
        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL) != null) {
            areaUnitTv.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL));
        } else {
            areaUnitTv.setText("Acre");
        }

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableGPS();

        } else {
        }
    }

    private void onClicks() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = latLngList.size();

                if (i > 2) {
                    //onclick=true;
                    submit_area1();
                    // next_butt.setEnabled(true);
                    can_add = false;
                    // submit.setEnabled(false);
                } else {
                    // Toast.makeText(context, resources.getString(R.string.please_submit_at_least_3_points), Toast.LENGTH_SHORT).show();
                    Toasty.info(context, resources.getString(R.string.please_submit_at_least_3_points), Toast.LENGTH_SHORT, true).show();

                }
            }
        });

        butt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.getUiSettings().setScrollGesturesEnabled(true);
                if (i > 0) {
                    tvarea.setText("0");

                    mMarkers.remove("marker" + (i - 1));
//                    mMarkers.remove(i-1);
                    latLngList.remove(i - 1);
                    markerList.remove(i - 1);
                    can_add = true;
                    remove("marker" + (i - 1));

                    i--;
                }
                onAreaClicked = false;
                normal_mode_butt.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapChoosen = MAP_TYPE.DEFAULT_MAP;
        mapTypeImage.setImageResource(R.drawable.map_type_normal);
        mapTypeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapChoosen == MAP_TYPE.DEFAULT_MAP) {
                    mapChoosen = MAP_TYPE.SATELLITE_MAP;
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    mapTypeImage.setImageResource(R.drawable.map_type_satellite);
                } else {
                    mapChoosen = MAP_TYPE.DEFAULT_MAP;
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mapTypeImage.setImageResource(R.drawable.map_type_normal);
                }
            }
        });

        mLocationRequest = new LocationRequest();
        //mLocationRequest.setInterval(1000000); //  minute interval
        // mLocationRequest.setFastestInterval(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
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


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub

                if (!onAreaClicked) {
                    mMap.getUiSettings().setScrollGesturesEnabled(true);
                    // latLngList.clear();
                    latLngList.add(point);
                    i = latLngList.size();
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(point)
                            // .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_my_location))
                            .title("marker" + (i - 1))
                            .draggable(true);

                    mMarkers.put("marker" + (i - 1), markerOptions);
                    //markFarmBoundary(point.latitude, point.   longitude);
                    Marker marker = mMap.addMarker(markerOptions);
                    markerList.add(marker);


                } else {
                    Toasty.info(context, resources.getString(R.string.clear_area_to_add_point), Toast.LENGTH_LONG).show();
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
                        mMap.clear();
                        AsyncTaskRunner taskRunner = new AsyncTaskRunner();
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
//        getGeoJson();
    }

    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        public AsyncTaskRunner() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            final int sizeofgps = latLngList.size();
            markerList = new ArrayList<>();
            for (int i = 0; i < latLngList.size(); i++) {
                LatLng latlngforbound = latLngList.get(i);
                builder.include(latlngforbound);
                int finalI = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(latLngList.get(finalI))
                                // .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_my_location))
                                .title("" + finalI)
                                .draggable(true));
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
            CheckAreaMapActivity.this.runOnUiThread(new Runnable() {

                public void run() {

                    /*the code you want to run after the background operation otherwise they will executed earlier and give you an error*/
                    // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                    showArea(sizeofgps);

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

    String areaInAcre;

    private void showArea(int i) {

        if (i < 3) {
            //Toast.makeText(context, "Please submit atleast 3 points", Toast.LENGTH_SHORT).show();
            ViewFailDialog viewFailDialog = new ViewFailDialog();
            viewFailDialog.showDialog(CheckAreaMapActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.should_have_at_least_two_points_to_display));
        } else {
            LatLng[] latLng = new LatLng[latLngList.size()];

            Log.e(TAG, "Arraylisy Size " + latLngList.size() + " And Array Size " + latLng.length + " And i is " + i);

            for (int j = 0; j < latLngList.size(); j++) {
                latLng[j] = latLngList.get(j);
            }
            Polygon UCCpolygon;

            UCCpolygon = mMap.addPolygon(new PolygonOptions()
                    .add(latLng)
                    .strokeColor(Color.RED)
                    .fillColor(Color.parseColor("#330000FF")));
            String a = String.valueOf(String.format("%.2f", SphericalUtil.computeArea(latLngList) * 0.000247105));
        }
    }

    private void submit_area1() {
        Polygon UCCpolygon;
        for (int i = 0; i < latLngList.size(); i++) {
            longPoints[i] = latLngList.get(i).longitude;
            latPoints[i] = latLngList.get(i).latitude;
        }
        if (i < 3) {
            // Toast.makeText(context, resources.getString(R.string.please_submit_at_least_3_points), Toast.LENGTH_SHORT).show();
            Toasty.info(context, resources.getString(R.string.please_submit_at_least_3_points), Toast.LENGTH_SHORT, true).show();

        } else {
            LatLng[] latLng = new LatLng[i];
            List<LatLng> latLngs = new ArrayList<>();

            for (int j = 0; j < i; j++) {
                if (j == i - 1) {
                    //double distance=distanceinmeters(latPoints[j],longPoints[j],latPoints[i-j-1],longPoints[i-j-1]);
                    float[] results1 = new float[1];
                    Location.distanceBetween(latPoints[j], longPoints[j], latPoints[i - j - 1], longPoints[i - j - 1], results1);
                    double distance = results1[0];
                    Bitmap obm = writeTextOnDrawable(R.drawable.white_back, String.format("%.2f", distance) + " m");
                    latLng[j] = new LatLng(latPoints[j], longPoints[j]);
                    LatLng point = new LatLng((latPoints[j] + latPoints[i - j - 1]) / 2, (longPoints[j] + longPoints[i - j - 1]) / 2);
                    MarkerOptions markerOptions = new MarkerOptions().icon(
                            BitmapDescriptorFactory.fromBitmap(obm))
                            .position(point);
                    mMap.addMarker(markerOptions);
                    latLngs.add(new LatLng(latPoints[j], longPoints[j]));
                } else {
                    // double distance=distanceinmeters(latPoints[j],longPoints[j],latPoints[j+1],longPoints[j+1]);
                    float[] results1 = new float[1];
                    Location.distanceBetween(latPoints[j], longPoints[j], latPoints[j + 1], longPoints[j + 1], results1);
                    double distance = results1[0];

                    Bitmap obm = writeTextOnDrawable(R.drawable.white_back, String.format("%.2f", distance) + " m");
                    latLng[j] = new LatLng(latPoints[j], longPoints[j]);
                    LatLng point = new LatLng((latPoints[j] + latPoints[j + 1]) / 2, (longPoints[j] + longPoints[j + 1]) / 2);

                    MarkerOptions markerOptions = new MarkerOptions().icon(
                            BitmapDescriptorFactory.fromBitmap(obm))
                            .position(point);
                    mMap.addMarker(markerOptions);
                    latLngs.add(new LatLng(latPoints[j], longPoints[j]));
                }
            }
            UCCpolygon = mMap.addPolygon(new PolygonOptions()
                    .add(latLng)
                    .strokeColor(Color.RED)
                    .fillColor(Color.parseColor("#330000FF")));


            Log.i("AREA", "computeArea " + SphericalUtil.computeArea(latLngs));

            areaInAcre = String.valueOf(String.format("%.2f", SphericalUtil.computeArea(latLngs) * 0.000247105));
            Log.e("AREA", "Acre " + areaInAcre);
            Log.e("AREA", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL) + " " + convertAreaTo(AppConstants.getShowableAreaWithConversion(areaInAcre, area_mult_factor)));
            tvarea.setText(convertAreaTo(AppConstants.getShowableAreaWithConversion(areaInAcre, area_mult_factor)));

            normal_mode_butt.setVisibility(View.VISIBLE);
            //* 0.000247105
            onAreaClicked = true;

        }

    }

    boolean onAreaClicked = false;

    private String convertAreaTo(String area) {
        String convertArea = area;
        convertArea = String.format("%.2f", parseDouble(area));
        return convertArea;
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

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30));
                progressBar.setVisibility(View.INVISIBLE);

//                getGeoJson();
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
                                ActivityCompat.requestPermissions(CheckAreaMapActivity.this,
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
                // Toast.makeText(context, resources.getString(R.string.cant_add_more_points), Toast.LENGTH_SHORT).show();
                Toasty.error(context, resources.getString(R.string.cant_add_more_points), Toast.LENGTH_SHORT, true).show();

            }
        } else {
            // Toast.makeText(context,resources.getString( R.string.please_clear_then_add), Toast.LENGTH_SHORT).show();
            Toasty.info(context, resources.getString(R.string.please_clear_then_add), Toast.LENGTH_SHORT, true).show();

        }
    }


    private void remove(String name) {
        // mMarkers.remove(name);
       /* mMarkers.clear();

        latLngList.clear();
        markerList.clear();*/
        mMap.clear();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                for (MarkerOptions item : mMarkers.values()) {
                    mMap.addMarker(item);
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
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(CheckAreaMapActivity.this).build();
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

                                status.startResolutionForResult(CheckAreaMapActivity.this, 1000);
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

    private double distanceinmeters(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist * 1000);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    RelativeLayout relExistFarm, relNewFarm, relNewFarmAnd;
    TextView tvNewFarmAnd, tvNewFarm, tvExistFarm;
    ImageView imgExistFarm, imgNewFarm, imgNewFarmAnd;
    Button cancel, nextButton;

    public void showDialog() {
        selectedOption = 0;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.check_area_farm_gps_select_option_dialog);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        relExistFarm = dialog.findViewById(R.id.relExistFarm);
        relNewFarm = dialog.findViewById(R.id.relNewFarm);
        relNewFarmAnd = dialog.findViewById(R.id.relNewFarmAnd);

        tvNewFarmAnd = dialog.findViewById(R.id.tvNewFarmAnd);
        tvNewFarm = dialog.findViewById(R.id.tvNewFarm);
        tvExistFarm = dialog.findViewById(R.id.tvExistFarm);

        imgExistFarm = dialog.findViewById(R.id.imgExistFarm);
        imgNewFarm = dialog.findViewById(R.id.imgNewFarm);
        imgNewFarmAnd = dialog.findViewById(R.id.imgNewFarmAnd);

        cancel = dialog.findViewById(R.id.cancel);
        nextButton = dialog.findViewById(R.id.nextButton);

        relExistFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTSelection(selectedOption, SELECTED_OPTIONS.TO_EXISTING_FARM);
            }
        });

        relNewFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTSelection(selectedOption, SELECTED_OPTIONS.TO_NEW_FARM);
            }
        });

        relNewFarmAnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTSelection(selectedOption, SELECTED_OPTIONS.TO_NEW_FARM_AND_FARMER);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedOption == 0) {
                    Toasty.error(context, "Please select any option", Toast.LENGTH_LONG).show();
                } else if (selectedOption == SELECTED_OPTIONS.TO_NEW_FARM) {
                    Intent intent = new Intent(context, AddFarmActivity.class);
                    intent.putExtra("areaInAcre", areaInAcre);
                    intent.putExtra("mapZoom", "" + mMap.getCameraPosition().zoom);
                    DataHandler.newInstance().setLatLngsCheckArea(latLngList);
                    intent.putExtra(AppConstants.ADD_FARM.KEY, AppConstants.ADD_FARM.ADD_EXISTING_FARM);
                    ActivityOptions options = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivity(intent, options.toBundle());
                    } else {
                        startActivity(intent);
                    }
                } else if (selectedOption == SELECTED_OPTIONS.TO_NEW_FARM_AND_FARMER) {
                    Intent intent = new Intent(context, AddFarmActivity.class);
                    DataHandler.newInstance().setLatLngsCheckArea(latLngList);
                    intent.putExtra("areaInAcre", areaInAcre);
                    intent.putExtra("mapZoom", "" + mMap.getCameraPosition().zoom);
                    intent.putExtra(AppConstants.ADD_FARM.KEY, AppConstants.ADD_FARM.NEW_FARM_AND_FARMER);
                    ActivityOptions options = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivity(intent, options.toBundle());
                    } else {
                        startActivity(intent);
                    }

                } else if (selectedOption == SELECTED_OPTIONS.TO_EXISTING_FARM) {

                    Intent intent = new Intent(context, SelectFarmActivity.class);
                    intent.putExtra("areaInAcre", areaInAcre);
                    intent.putExtra("mapZoom", "" + mMap.getCameraPosition().zoom);
                    DataHandler.newInstance().setLatLngsCheckArea(latLngList);
                    intent.putExtra(AppConstants.ADD_FARM.KEY, AppConstants.ADD_FARM.NEW_FARM_AND_FARMER);
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

        dialog.show();


    }

    int selectedOption = 0;


    interface SELECTED_OPTIONS {
        static final int TO_EXISTING_FARM = 44;
        static final int TO_NEW_FARM = 45;
        static final int TO_NEW_FARM_AND_FARMER = 46;
    }

    private void setTSelection(int oldSelection, int newSelection) {
        if (oldSelection != 0) {
            if (oldSelection == SELECTED_OPTIONS.TO_EXISTING_FARM) {
                relExistFarm.setBackgroundResource(R.drawable.selected_option_check_area_not);
                tvExistFarm.setTextColor(resources.getColor(R.color.black));
                imgExistFarm.setImageResource(R.drawable.ic_add_to_existing);
            } else if (oldSelection == SELECTED_OPTIONS.TO_NEW_FARM) {
                relNewFarm.setBackgroundResource(R.drawable.selected_option_check_area_not);
                tvNewFarm.setTextColor(resources.getColor(R.color.black));
                imgNewFarm.setImageResource(R.drawable.ic_area_farm);
            } else if (oldSelection == SELECTED_OPTIONS.TO_NEW_FARM_AND_FARMER) {
                relNewFarmAnd.setBackgroundResource(R.drawable.selected_option_check_area_not);
                tvNewFarmAnd.setTextColor(resources.getColor(R.color.black));
                imgNewFarmAnd.setImageResource(R.drawable.ic_new_user);
            }
        }

        if (newSelection == SELECTED_OPTIONS.TO_EXISTING_FARM) {
            relExistFarm.setBackgroundResource(R.drawable.selected_option_check_area);
            tvExistFarm.setTextColor(resources.getColor(R.color.white));
            imgExistFarm.setImageResource(R.drawable.ic_add_to_existing_white);
        } else if (newSelection == SELECTED_OPTIONS.TO_NEW_FARM) {
            relNewFarm.setBackgroundResource(R.drawable.selected_option_check_area);
            tvNewFarm.setTextColor(resources.getColor(R.color.white));
            imgNewFarm.setImageResource(R.drawable.ic_area_farm_white);
        } else if (newSelection == SELECTED_OPTIONS.TO_NEW_FARM_AND_FARMER) {
            relNewFarmAnd.setBackgroundResource(R.drawable.selected_option_check_area);
            tvNewFarmAnd.setTextColor(resources.getColor(R.color.white));
            imgNewFarmAnd.setImageResource(R.drawable.ic_new_user_white);
        }
        selectedOption = newSelection;
    }

}
