package com.i9930.croptrails.Maps.VerifyArea;

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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.Layer;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPoint;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;
import com.google.maps.android.geometry.Point;
import com.tooltip.Tooltip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.LineSegmentLineSegmentIntersection;
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.NearFarmDatum;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.NearFarmResponse;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.NewFarmLatLang;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.OmitanceDetails;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.SendOmtanceArea;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.SendWayPointData;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.VerifySendData;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.WayPoint;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static java.lang.Double.parseDouble;
import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Maps.WalkAround.MapsActivityNew.getBitmapFromVectorDrawable;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;
import static com.i9930.croptrails.Utility.AppConstants.getCoordinatesFromGeometry;

public class MapsActivitySamunnati extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    double area_mult_factor = 1.0;
    double area_mult_factor_def = 0.000247105;
    double multiplicationFactor = 1.0;
    private GoogleMap mMap;
    String TAG = "MapsActivityEasyMode";
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
    String farmLatitude, farmLongitude, submitType;
    ImageView helpImage;

    private interface MAP_TYPE {
        public static final int DEFAULT_MAP = 101;
        public static final int SATELLITE_MAP = 102;
    }

    String msg = "1) Please use the pin drops to mark the farm corners.\n" +
            "2) Follow a circular (clockwise or anticlockwise) pattern to mark them, don't mark random corners of the farm.\n" +
            "3) If you start with a corner, please note that you should close the formation and end at the same corner, marking with another dropped pin.";
    Tooltip tooltip;
    //    private Map<String, MarkerOptions> mMarkers = new ConcurrentHashMap<String, MarkerOptions>();
    int pageVisitCount = 0;
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


        pageVisitCount = SharedPreferencesMethod2.getInt(context, SharedPreferencesMethod2.MAP_VISIT_COUNT);
        tooltip = new Tooltip.Builder(helpImage)
                .setText(msg)
                .setBackgroundColor(resources.getColor(R.color.white))
                .setTextColor(resources.getColor(R.color.black))
                .setCancelable(true).build();
        if (pageVisitCount <= 3) {


            tooltip.show();
            pageVisitCount++;
            SharedPreferencesMethod2.setInt(context, SharedPreferencesMethod2.MAP_VISIT_COUNT, pageVisitCount);
        }

        helpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tooltip.isShowing()) {
                    tooltip.dismiss();
                } else
                    tooltip.show();

            }
        });

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
                    ConnectivityUtils.checkSpeedWithColor(MapsActivitySamunnati.this, new ConnectivityUtils.ColorListener() {
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
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                }
            }
        });


    }

    Bitmap bmp;
    CircleImageView back_to_farm;


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

    List<List<LatLng>> latLngListGeoJSON = new ArrayList<>();
    List<WayPoint> wayPointListOld = new ArrayList<>();

    private void getGeoJson() {

        try {
            String json = loadJSONFromAsset();
            JSONObject jsonObject = new JSONObject(json);
            GeoJsonLayer layer = new GeoJsonLayer(null, jsonObject);
            layer.getDefaultPolygonStyle().setGeodesic(false);

//            layer.getDefaultPolygonStyle().setGeodesic(false);
//            layer.getDefaultPolygonStyle().set(false);
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
                        try {
                            WayPoint wayPoint = new WayPoint(name, ((GeoJsonPoint) feature.getGeometry()).getCoordinates());
                            wayPointListOld.add(wayPoint);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Assign the point style to the feature
                        feature.setPointStyle(pointStyle);
                    } else {
                        GeoJsonPolygonStyle style = new GeoJsonPolygonStyle();
                        try {
                            if (feature.getGeometry() != null && AppConstants.isValidString(feature.getGeometry().getGeometryType()) &&
                                    feature.getGeometry().getGeometryType().trim().equalsIgnoreCase("multiPolygon")) {
                                latLngListGeoJSON = AppConstants.getCoordinatesFromGeometryMultiPoly(feature.getGeometry());

                                if (latLngListGeoJSON != null && latLngListGeoJSON.size() > 0)
//                                    for (int i=0;i<latLngListGeoJSON.size();i++){
//                                        if (i==0)
                                    latLngsPrevious.addAll(latLngListGeoJSON.get(0));

//                                    }

//                                else {}
                            }


                            Log.e(TAG, "loadJSONFromAsset type Feature " + feature.getGeometry().toString());
                            style.setFillColor(AppConstants.POLYGON.FILL);
                            style.setStrokeColor(AppConstants.POLYGON.STROKE);
                            feature.setPolygonStyle(style);
                            if (layer.getBoundingBox() != null)
                                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(layer.getBoundingBox(), 50));
                            else {
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

//                        layer.addLayerToMap();



            /*layer.setOnFeatureClickListener(new Layer.OnFeatureClickListener() {
                @Override
                public void onFeatureClick(Feature feature) {
                    if (feature != null && AppConstants.isValidString(feature.getGeometry().getGeometryType()) && feature.getGeometry().getGeometryType().equalsIgnoreCase("multipolygon")) {
                        Toast.makeText(context, "cl", Toast.LENGTH_SHORT).show();
//                                feature.
                    } else {
                        Toast.makeText(context, "not", Toast.LENGTH_SHORT).show();
                    }
                }
            });*/
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
        setContentView(R.layout.activity_maps_samunnati);

        back_to_farm = findViewById(R.id.back_to_farm);

        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        context = this;
        ButterKnife.bind(this);
        initViews();
        bmp = getBitmapFromVectorDrawable(context, R.drawable.marker_green_24px);
//        title.setText(resources.getString(R.string.check_area_title));
        title.setText("Pin Drop Mode");
        areaUnitTv.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL));
        area_mult_factor = SharedPreferencesMethod.getDoubleSharedPreferences(context, SharedPreferencesMethod.AREA_MULTIPLICATON_FACTOR);
      /*  if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL).toLowerCase().equals("hectare")) {
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


       /* bottomSheetDialog = new BottomSheetDialog(this);
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

        Intent intent = getIntent();
        if (intent != null) {
            farmLatitude = intent.getStringExtra("farmLat");
            farmLongitude = intent.getStringExtra("farmLon");
            submitType = intent.getStringExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE);
//            submitType = AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE;
        }
        boolean isWayPointEnabled = SharedPreferencesMethod.getBoolean(this, SharedPreferencesMethod.IS_WAY_POINT_ENABLED);
        if (submitType != null && submitType.equals(AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE)) {
            isWayPointEnabled = false;
        }
        normal_mode_butt.setText(resources.getString(R.string.way_point));
        if (isWayPointEnabled) {
            normal_mode_butt.setVisibility(View.VISIBLE);
            normal_mode_butt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Intent intent = new Intent(context, MapsActivitySamunnati.class);
//                intent.putExtra("farmLat", farmLatitude);
//                intent.putExtra("farmLon", farmLongitude);
//                startActivityForResult(intent, 101);
//                finish();
//
                    if (!isWayPoint)
                        isWayPoint = true;
                    else
                        isWayPoint = false;

                }
            });
        } else {
            normal_mode_butt.setVisibility(View.GONE);
        }

//        submitType=AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE;

        /*if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableGPS();

        } else {
        }*/
    }

    boolean isWayPoint = false;

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
                    submit_area_butt.setVisibility(View.VISIBLE);
//                    if (UCCpolygon != null)
//                        UCCpolygon.remove();
//            onAreaClicked = false;
//                    mMap.clear();
                    removeAreaPoly();
                    removeAllmarkerAndCurrentAreaPoly();

                    AsyncTaskRunner taskRunner = new AsyncTaskRunner();
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

        /*normal_mode_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, MapsActivityNew.class));
                finish();
            }
        });*/
        submit_area_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setMessage(resources.getString(R.string.sure_to_submit_area))
                        .setCancelable(false)
                        .setPositiveButton(resources.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (submitType != null && submitType.equals(AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE)) {
                                    showDialogNameOmitanceArea();
                                } else if (Double.parseDouble(tvarea.getText().toString()) > 0) {

                                    if (submitType != null && submitType.equals(AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE)) {
                                        showDialogNameOmitanceArea();
                                    } else {
                                        progressBar.setVisibility(View.VISIBLE);
                                        SubmitAreaAsync asyncTaskRunner = new SubmitAreaAsync(tvarea.getText().toString(),
                                                null, AppConstants.AREA_TYPE.Farm);
                                        asyncTaskRunner.execute();
                                    }
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

                removeAreaPoly();
                removeAllmarkerAndCurrentAreaPoly();

                can_add = true;
                markerList.remove(markerList.size() - 1);
                remove();
            } else {
                tvarea.setText("0");
                submit_area_butt.setVisibility(View.GONE);
//                mMarkers.remove("marker" + (conflictIndex + 1));
                latLngList.remove(conflictIndex + 1);
//                pointList.remove(conflictIndex + 1);
                can_add = true;
                removeAreaPoly();
                removeAllmarkerAndCurrentAreaPoly();

                markerList.remove(markerList.size() - 1);
                remove();
                this.conflictIndex--;
            }
            i--;

//            Log.e(TAG, "mMarkers =" + mMarkers.size());
//            Log.e(TAG, "pointList =" + pointList.size());
            Log.e(TAG, "latLngList =" + latLngList.size());
            Log.e(TAG, "markerList =" + markerList.size());
            Log.e(TAG, "i =" + i);
//            prepareData();
        }
//        onAreaClicked = false;
    }

    private class SubmitAreaAsync extends AsyncTask<String, Void, String> {
        String area;
        float zoom = 15.0f;
        String name;
        String type;

        public SubmitAreaAsync(String area, String name, String type) {
            super();
            this.area = area;
            this.type = type;
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
                                viewFailDialog.showSessionExpireDialog(MapsActivitySamunnati.this, resources.getString(R.string.multiple_login_msg));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                if (isActivityRunning)
                                    viewFailDialog.showSessionExpireDialog(MapsActivitySamunnati.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                if (isActivityRunning)
                                    viewFailDialog.showSessionExpireDialog(MapsActivitySamunnati.this, resources.getString(R.string.authorization_expired));
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

        public SubmitWayAsync(String name, JsonObject geometry, int index) {
            super();
            this.geometry = geometry;
            this.index = index;
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
                                viewFailDialog.showSessionExpireDialog(MapsActivitySamunnati.this, resources.getString(R.string.multiple_login_msg));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                if (isActivityRunning)
                                    viewFailDialog.showSessionExpireDialog(MapsActivitySamunnati.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                if (isActivityRunning)
                                    viewFailDialog.showSessionExpireDialog(MapsActivitySamunnati.this, resources.getString(R.string.authorization_expired));
                            } else if (response.body().getStatus() == 1) {
                                if (index == wayPointList.size() - 1) {
                                    progressBar.setVisibility(View.GONE);
                                    Toasty.success(context, resources.getString(R.string.area_successfully_added), Toast.LENGTH_SHORT, true).show();
                                    Intent intent = getIntent();
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            if (index == wayPointList.size() - 1) {
                                progressBar.setVisibility(View.GONE);
                                Toasty.success(context, resources.getString(R.string.area_successfully_added), Toast.LENGTH_SHORT, true).show();
                                Intent intent = getIntent();
                                setResult(RESULT_OK, intent);
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
                    if (index == wayPointList.size() - 1) {
                        progressBar.setVisibility(View.GONE);
                        Toasty.success(context, resources.getString(R.string.area_successfully_added), Toast.LENGTH_SHORT, true).show();
                        Intent intent = getIntent();
                        setResult(RESULT_OK, intent);
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
            SubmitWayAsync async = new SubmitWayAsync(wayPointList.get(i).getName(), geoMetry, i);
            async.execute();
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

    private void checkImprove(List<LinePoint> linePointList) {
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
//        if (UCCpolygon != null)
//            UCCpolygon.remove();
//        mMap.clear();
        getArea();
        submit_area_butt.setVisibility(View.VISIBLE);

//        return flag;
        return true;
    }


    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active

    }

//    boolean onAreaClicked = false;

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapChoosen = MAP_TYPE.SATELLITE_MAP;
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

        if (farmLatitude != null && farmLongitude != null && !TextUtils.isEmpty(farmLatitude)
                && !TextUtils.isEmpty(farmLongitude) && !farmLatitude.trim().equals("0") && !farmLongitude.trim().equals("0")) {

            final LatLng latLng = new LatLng(Double.valueOf(farmLatitude), Double.valueOf(farmLongitude));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            mMap.setMyLocationEnabled(false);

            LatLng point = new LatLng(Double.parseDouble(farmLatitude), Double.parseDouble(farmLongitude));
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            // latLngList.clear();
            latLngList.add(point);
            i = latLngList.size();
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(point)
                    // .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_my_location))
                    .title("marker" + (i))
                    .draggable(true);

//            mMarkers.put("marker" + (i), markerOptions);
            //markFarmBoundary(point.latitude, point.longitude);
            Marker marker = mMap.addMarker(markerOptions);
            markerList.add(marker);

        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                mMap.getUiSettings().setScrollGesturesEnabled(true);
                {
                    if (isWayPoint) {
                        showDialog(point);
                    } else {

                        if (latLngList.size() == 0) {
                            if (submitType != null && submitType.equals(AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE) && !PolyUtil.containsLocation(point, latLngsPrevious, false)) {
                                Toasty.error(context, "Not within farm boundaries", Toast.LENGTH_SHORT, false).show();
                            } else {
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
                            }
                        } else {
                            if (submitType != null && submitType.equals(AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE) && !
                                    PolyUtil.containsLocation(point, latLngsPrevious, false)) {
                                Toasty.error(context, "Not Inside", Toast.LENGTH_SHORT, false).show();
                            } else {
                                float[] results1 = new float[1];
                                Location.distanceBetween(latLngList.get(latLngList.size() - 1).latitude, latLngList.get(latLngList.size() - 1).longitude, point.latitude, point.longitude, results1);
                                double distanceinKm = results1[0] / 1000;
                                if (distanceinKm < 10) {
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
                                } else {
                                    Toasty.error(context, "Point cannot be 10km away from last point", Toast.LENGTH_SHORT, false).show();
                                }
                            }
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
//                        mMap.clear();
                        removeAreaPoly();
                        removeAllmarkerAndCurrentAreaPoly();

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

        if (submitType != null && submitType.equals(AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE)) {
            AsyncTaskRunnerShow asyncTaskRunnerShow = new AsyncTaskRunnerShow();
            asyncTaskRunnerShow.execute();
        }

    }

    Map<Integer, List<LatLng>> pointMap = new HashMap<>();
    Map<Integer, List<LatLng>> locationMap = new HashMap<>();

    private String convertAreaTo(String area) {
        String convertArea = area;
        convertArea = String.format("%.2f", parseDouble(area));
        return convertArea;
    }


    private void getArea() {
        /*markerList = new ArrayList<>();
        for (int i = 0; i < latLngList.size(); i++) {
            LatLng latlngforbound = latLngList.get(i);
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
        }*/
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                submit_area1();
            }
        });

    }

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
                                .draggable(true);
                        Marker marker = mMap.addMarker(options);
                        markerList.add(marker);

                    }
                });
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 45));
                    submit_area1();
                }
            });
            MapsActivitySamunnati.this.runOnUiThread(new Runnable() {
                public void run() {
//                    showArea();
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

    private void showArea() {
        if (i < 3) {
            //Toast.makeText(context, "Please view_area_button atleast 3 points", Toast.LENGTH_SHORT).show();
            ViewFailDialog viewFailDialog = new ViewFailDialog();
            viewFailDialog.showDialog(MapsActivitySamunnati.this, resources.getString(R.string.opps_message), resources.getString(R.string.should_have_at_least_two_points_to_display));
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
            String a = String.valueOf(String.format("%.4f", SphericalUtil.computeArea(latLngList) * 0.000247105));
            Log.i(TAG, "computeArea " + SphericalUtil.computeArea(latLngList) + " Acre " + a);

            tvarea.setText(AppConstants.getShowableAreaWithConversion(a, area_mult_factor));
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
                    /*float[] results1 = new float[1];
                    Location.distanceBetween(latPoints[j], longPoints[j], latPoints[i - j - 1], longPoints[i - j - 1], results1);
                    double distance = results1[0];
                    Bitmap obm = writeTextOnDrawable(R.drawable.white_back, String.format("%.2f", distance) + " m");
                    LatLng point = new LatLng((latPoints[j] + latPoints[i - j - 1]) / 2, (longPoints[j] + longPoints[i - j - 1]) / 2);
                    MarkerOptions markerOptions = new MarkerOptions().icon(
                            BitmapDescriptorFactory.fromBitmap(obm))
                            .position(point);
                    mMap.addMarker(markerOptions);*/
                    latLngs.add(new LatLng(latPoints[j], longPoints[j]));
                } else {
                    // double distance=distanceinmeters(latPoints[j],longPoints[j],latPoints[j+1],longPoints[j+1]);
                    /*float[] results1 = new float[1];
                    Location.distanceBetween(latPoints[j], longPoints[j], latPoints[j + 1], longPoints[j + 1], results1);
                    double distance = results1[0];

                    Bitmap obm = writeTextOnDrawable(R.drawable.white_back, String.format("%.2f", distance) + " m");
                    LatLng point = new LatLng((latPoints[j] + latPoints[j + 1]) / 2, (longPoints[j] + longPoints[j + 1]) / 2);

                    MarkerOptions markerOptions = new MarkerOptions().icon(
                            BitmapDescriptorFactory.fromBitmap(obm))
                            .position(point);
                    mMap.addMarker(markerOptions);*/
                    latLngs.add(new LatLng(latPoints[j], longPoints[j]));
                }
            }

            latLngs.add(firstAndLastPoint);
            Log.e(TAG, "1 latLngs " + latLngs.size() + " AND latPoints " + latPoints.length + " AND i " + i);

           /* if (PolyUtil.isClosedPolygon(latLngs)){
                Log.e(TAG,"1 CLOSSED true");
            }else{
                Log.e(TAG,"1 CLOSSED false");
            }

            PolyUtil.simplify(latLngs,10);

            if (PolyUtil.isClosedPolygon(latLngs)){
                Log.e(TAG,"2 CLOSSED true");
            }else{
                Log.e(TAG,"2 CLOSSED false");
            }
*/

//            if (UCCpolygon != null)
//                UCCpolygon.remove();
            removeAreaPoly();
            UCCpolygon = mMap.addPolygon(new PolygonOptions()
                    .addAll(latLngs)
                    .strokeColor(Color.RED)
                    .fillColor(Color.parseColor("#330000FF")));
            String a = String.valueOf(String.format("%.2f", SphericalUtil.computeArea(latLngs) * area_mult_factor_def));
            Log.i("AREA", "computeArea " + SphericalUtil.computeArea(latLngs) + " Acre " + a);

            tvarea.setText(AppConstants.getShowableAreaWithConversion(a, area_mult_factor));


//            onAreaClicked = true;


        }

    }

    //    int locationCount=0
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
                    getNearFarms(mLastLocation.getLatitude() + "", mLastLocation.getLongitude() + "");
                    isNearFarmCalled = true;
                }
                if (locMoveCount == 0) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                    progressBar.setVisibility(View.INVISIBLE);
                    locMoveCount++;
                }
//                getGeoJson();
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
                                ActivityCompat.requestPermissions(MapsActivitySamunnati.this,
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


    private void remove() {
        // mMarkers.remove(name);
       /* mMarkers.clear();

        latLngList.clear();
        markerList.clear();*/
//        mMap.clear();

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
                    Marker m = mMap.addMarker(markerOptions);
                    markerList.set(i - 1, m);
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
        Toast.makeText(context, "Gps Disabled", Toast.LENGTH_SHORT).show();

        enableGPS();
    }

    private void enableGPS() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(MapsActivitySamunnati.this).build();
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

                                status.startResolutionForResult(MapsActivitySamunnati.this, 1000);
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
                    SubmitAreaAsync asyncTaskRunner = new SubmitAreaAsync(tvarea.getText().toString(),
                            etPointName.getText().toString().trim(), AppConstants.AREA_TYPE.Omittance);
                    asyncTaskRunner.execute();
                }

            }
        });

        dialog.show();


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
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", "FeatureCollection");
            JsonArray features = new JsonArray();
            //===============Adding Farm boundary GeoJson
            JsonObject feature = new JsonObject();
            feature.addProperty("type", "Feature");
            JsonObject geometry = new JsonObject();
            geometry.addProperty("type", "MultiPolygon");
            JsonArray coordinates = new JsonArray();
            JsonArray c1 = new JsonArray();

            for (int j = 0; j < latLngListGeoJSON.size(); j++) {
                JsonArray c2 = new JsonArray();
                List<LatLng> latLngList = latLngListGeoJSON.get(j);
                for (int k = 0; k < latLngList.size(); k++) {

                    JsonArray c3 = new JsonArray();
                    c3.add(latLngList.get(k).longitude);
                    c3.add(latLngList.get(k).latitude);
                    c2.add(c3);
                }
                c1.add(c2);
            }
            JsonArray c2 = new JsonArray();
            for (int k = 0; k < MapsActivitySamunnati.this.latLngList.size(); k++) {
                JsonArray c3 = new JsonArray();
                c3.add(MapsActivitySamunnati.this.latLngList.get(k).longitude);
                c3.add(MapsActivitySamunnati.this.latLngList.get(k).latitude);
                c2.add(c3);
            }

            c1.add(c2);
            coordinates.add(c1);
            geometry.add("coordinates", coordinates);
            feature.add("geometry", geometry);
            features.add(feature);

            //=============Adding waypoint as GeoJson
            if (wayPointList != null && wayPointList.size() > 0) {
                for (int i = 0; i < wayPointList.size(); i++) {
                    JsonObject pointFeature = new JsonObject();
                    pointFeature.addProperty("type", "Feature");
                    JsonObject geoMetry = new JsonObject();
                    geoMetry.addProperty("type", "Point");
                    JsonArray pCoords = new JsonArray();
                    pCoords.add(wayPointList.get(i).getPoint().longitude);
                    pCoords.add(wayPointList.get(i).getPoint().latitude);
                    geoMetry.add("coordinates", pCoords);

                    pointFeature.add("geometry", geoMetry);

                    JsonObject properties = new JsonObject();
                    properties.addProperty("n", "" + wayPointList.get(i).getName());
                    pointFeature.add("properties", properties);

                    features.add(pointFeature);
                }
            }

            jsonObject.add("features", features);


            Log.e(TAG, "SUBMITTING GEOJSON " + new Gson().toJson(jsonObject));


            /*for (int j = 0; j < MapsActivitySamunnati.this.latLngList.size(); j++) {
                latLngList.add(MapsActivitySamunnati.this.latLngList.get(j));
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
                            Log.e(TAG, "OMITANCE res " + new Gson().toJson(response.body()));

                            StatusMsgModel statusMsgModel = response.body();
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivitySamunnati.this, response.body().getMsg());
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivitySamunnati.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivitySamunnati.this, resources.getString(R.string.authorization_expired));
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
                            viewFailDialog.showSessionExpireDialog(MapsActivitySamunnati.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivitySamunnati.this, resources.getString(R.string.authorization_expired));
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
                            notifyApiDelay(MapsActivitySamunnati.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {

                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        notifyApiDelay(MapsActivitySamunnati.this, call.request().url().toString(),
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
            }*/

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

    private void getNearFarms(String lat, String lon) {
        if (!isNearFarmCalled) {
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
            final String farm_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
            final String clusterId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID);
            Log.e(TAG, "SoilSenseResponse " + "lat:" + lat);
            Log.e(TAG, "SoilSenseResponse " + "long:" + lon);
            Log.e(TAG, "SoilSenseResponse " + "user_id:" + userId);
            Log.e(TAG, "SoilSenseResponse " + "token:" + token);
            Log.e(TAG, "SoilSenseResponse " + "comp_id:" + comp_id);
            JsonObject object = new JsonObject();
            object.addProperty("farm_id", "" + farm_id);
            object.addProperty("comp_id", "" + comp_id);
            object.addProperty("user_id", "" + userId);
            object.addProperty("token", "" + token);
            object.addProperty("lat", "" + lat);
            object.addProperty("long", "" + lon);
            object.addProperty("cluster_id", "" + clusterId);
            Log.e(TAG, "getNearFarms req " + new Gson().toJson(object));
            final boolean[] isLoaded = {false};
            long currMillis = AppConstants.getCurrentMills();
            Call<NearFarmResponse> getCalenderData = apiInterface.getNearFarms(comp_id, farm_id, userId, token, lat, lon, clusterId);
            getCalenderData.enqueue(new Callback<NearFarmResponse>() {
                @Override
                public void onResponse(Call<NearFarmResponse> call, retrofit2.Response<NearFarmResponse> response) {
                    Log.e(TAG, "getNearFarms Status " + response.code());
                    try {
                        Log.e(TAG, "getNearFarms res " + new Gson().toJson(response.body()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isLoaded[0] = true;
                    String error = "";
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 10) {
                            viewFailDialog.showSessionExpireDialog(MapsActivitySamunnati.this, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            if (isActivityRunning)
                                viewFailDialog.showSessionExpireDialog(MapsActivitySamunnati.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            if (isActivityRunning)
                                viewFailDialog.showSessionExpireDialog(MapsActivitySamunnati.this, resources.getString(R.string.authorization_expired));
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
                            viewFailDialog.showSessionExpireDialog(MapsActivitySamunnati.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        if (isActivityRunning)
                            viewFailDialog.showSessionExpireDialog(MapsActivitySamunnati.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {

                            error = response.errorBody().string().toString();
                            Log.e(TAG, "getNearFarms Unsuccess " + error);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                        notifyApiDelay(MapsActivitySamunnati.this, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }

                }

                @Override
                public void onFailure(Call<NearFarmResponse> call, Throwable t) {
                    long newMillis = AppConstants.getCurrentMills();
                    isLoaded[0] = true;
                    long diff = newMillis - currMillis;
                    notifyApiDelay(MapsActivitySamunnati.this, call.request().url().toString(),
                            "" + diff, internetSPeed, t.toString(), 0);
                    Log.e(TAG, "getNearFarms Failure " + t.toString());
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
                mMap.addPolygon(new PolygonOptions().add(latLngList).strokeColor(
                        Color.parseColor("#330000FF"))
                        .fillColor(Color.parseColor("#330000FF")));
            }
        });
    }

    private void removeAllmarkerAndCurrentAreaPoly() {
        for (int i = 0; i < markerList.size(); i++) {
            markerList.get(i).remove();

        }
    }

    private void removeAreaPoly() {
        if (UCCpolygon != null) {
            UCCpolygon.remove();
        }
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
                    LatLng latlngforbound = new LatLng(latLngList.get(i).latitude, latLngList.get(i).longitude);
                    builder.include(latlngforbound);
                    latLngs[i] = latlngforbound;
                    latLngsPrevious.add(latlngforbound);
                }
                MapsActivitySamunnati.this.runOnUiThread(new Runnable() {

                    public void run() {

                        /*the code you want to run after the background operation otherwise they will executed earlier and give you an error*/
                        // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
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
        List<LatLng> latLngList = DataHandler.newInstance().getLatLngsFarm();
        if (latLngList != null && latLngList.size() > 0) {
            List<LatLng> latLngListt = new ArrayList<>();
            back_to_farm.setVisibility(View.VISIBLE);
            back_to_farm.setImageResource(R.drawable.farm_icon_map);
            Log.e(TAG, "Poly " + new Gson().toJson(latLngList));
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            final int sizeofgps = latLngList.size();
            for (int i = 0; i < latLngList.size(); i++) {
                LatLng latlngforbound = new LatLng(Double.valueOf(latLngList.get(i).latitude), Double.valueOf(latLngList.get(i).longitude));
                builder.include(latlngforbound);
                latLngListt.add(latlngforbound);
            }
            int i = latLngListt.size();

            if (i > 0) {
                final LatLng latLng = latLngListt.get(0);

                back_to_farm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //showDialog();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                        //  mMap.setLatLngBoundsForCameraTarget();
                    }
                });
                showArea(i, latLngListt);

            }

        } else {
            back_to_farm.setVisibility(View.GONE);
        }
    }

    private void showArea(int i, List<LatLng> latLngList) {

        if (i < 3) {
            //Toast.makeText(context, "Please submit atleast 3 points", Toast.LENGTH_SHORT).show();
            ViewFailDialog viewFailDialog = new ViewFailDialog();
            viewFailDialog.showDialog(MapsActivitySamunnati.this, resources.getString(R.string.opps_message), resources.getString(R.string.should_have_at_least_two_points_to_display));
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
            PolygonOptions options = new PolygonOptions()
                    .addAll(latLngs)
                    .strokeColor(Color.RED)
                    .strokeWidth(2f)
                    .fillColor(Color.parseColor("#33effb5e"));

            mMap.addPolygon(options);


        }
    }

}
