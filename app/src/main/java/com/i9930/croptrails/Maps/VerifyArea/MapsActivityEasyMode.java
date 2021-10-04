package com.i9930.croptrails.Maps.VerifyArea;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Magnifier;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.geometry.Point;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.LoupeView;
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.NearFarmDatum;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.NearFarmResponse;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.NewFarmLatLang;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.OmitanceDetails;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.SendOmtanceArea;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.VerifySendData;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static java.lang.Double.parseDouble;
import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class MapsActivityEasyMode extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    double area_mult_factor = 1.0;
    double area_mult_factor_def = 0.000247105;
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
    TextView view_area_button;
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
    TextView submit_area_butt;
    @BindView(R.id.normal_mode_butt)
    TextView normal_mode_butt;
    @BindView(R.id.areaUnitTv)
    TextView areaUnitTv;

    @BindView(R.id.mapTypeImage)
    CircleImageView mapTypeImage;
    int mapChoosen = 101;
    @BindView(R.id.loupView)
    LoupeView mLoupeView;
    Magnifier magnifier = null;
    LoupeView view;
    ImageView helpImage;

    private interface MAP_TYPE {
        public static final int DEFAULT_MAP = 101;
        public static final int SATELLITE_MAP = 102;
    }

    String farmLatitude, farmLongitude;

    private final Map<String, MarkerOptions> mMarkers = new ConcurrentHashMap<String, MarkerOptions>();

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
        helpImage=findViewById(R.id.helpImage);
        helpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=null;
                try {
                    intent =new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.google.android.youtube");
                    intent.setData(Uri.parse("https://www.youtube.com/watch?v=1GUl6weFrLI&list=PLu5534zIb53NnoHxanrC9jt_7nw8lBQUU&index=6&t=0s"));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/watch?v=1GUl6weFrLI&list=PLu5534zIb53NnoHxanrC9jt_7nw8lBQUU&index=6&t=0s"));
                    startActivity(intent);
                }

            }
        });





/*
        helpImage = findViewById(R.id.helpImage);
        int pageVisitCount= SharedPreferencesMethod2.getInt(context, SharedPreferencesMethod2.MAP_VISIT_COUNT);
        String msg = "1) Please use the pin drops to mark the farm corners.\n" +
                "2) follow a circular (clockwise or anticlockwise) pattern to mark them, don't mark random corners of the farm.\n" +
                "3) If you start with a corner, please note that you should close the formation and end at the same corner, marking with another dropped pin.";
        Tooltip tooltip = new Tooltip.Builder(helpImage)
                .setText(msg)
                .setBackgroundColor(resources.getColor(R.color.white))
                .setTextColor(resources.getColor(R.color.black))
                .setCancelable(true).build();
        if (pageVisitCount <= 9) {
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
*/

    }

    public static Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }
    String internetSPeed = "";
    @BindView(R.id.connectivityLine)
    View connectivityLine;
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
                            ConnectivityUtils.checkSpeedWithColor(MapsActivityEasyMode.this, new ConnectivityUtils.ColorListener() {
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
    String submitType;


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


    }
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
        setContentView(R.layout.activity_check_area_map);
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        context = this;
        dialog= new ProgressDialog(context);
        ButterKnife.bind(this);
        initViews();
//        mLoupeView.setImageBitmap(getBitmapFromView(findViewById(R.id.map_touch_layer)));
//        mLoupeView.setMFactor(2);
//        mLoupeView.setRadius(100);

//        title.setText(resources.getString(R.string.check_area_title));
        title.setText("Easy Mode");
        areaUnitTv.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL));
        area_mult_factor = SharedPreferencesMethod.getDoubleSharedPreferences(context, SharedPreferencesMethod.AREA_MULTIPLICATON_FACTOR);
        setFarmerNameAndKhasra();
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        back_to_farm=findViewById(R.id.back_to_farm);
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
//        if (isWayPointEnabled) {
//            normal_mode_butt.setText(resources.getString(R.string.way_point));
            normal_mode_butt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MapsActivitySamunnati.class);
                    intent.putExtra("farmLat", farmLatitude);
                    intent.putExtra("farmLon", farmLongitude);
                    startActivityForResult(intent, 101);
                    finish();

//                    if (!isWayPoint)
//                        isWayPoint = true;
//                    else
//                        isWayPoint = false;

                }
            });
//        }else {
//            normal_mode_butt.setVisibility(View.GONE);
//        }


    }
boolean isWayPoint=false;
    private void onClicks() {
        view_area_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = latLngList.size();

                if (i > 2) {
                    //onclick=true;
                    submit_area1();
                    // next_butt.setEnabled(true);
                    can_add = false;
                    // view_area_button.setEnabled(false);
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
                latLngList.clear();
                if (i > 0) {
                    tvarea.setText("0");
                    remove("marker" + (i - 1));
                    // next_butt.setEnabled(false);
                    can_add = true;
                    i--;
                }
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
                                if (submitType != null && submitType.equals(AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE)) {

                                    showDialogNameOmitanceArea();
                                } else if (Double.parseDouble(tvarea.getText().toString()) > 0) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    SubmitAreaAsync asyncTaskRunner = new SubmitAreaAsync(tvarea.getText().toString(),null,
                                            AppConstants.AREA_TYPE.Farm);
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
                public void onResponse(Call<StatusMsgModel> call, retrofit2.Response<StatusMsgModel> response) {
                    Log.e(TAG, "SUBMITTING " + response.code());
                    if (response.isSuccessful()) {
                        try {
                            Log.e(TAG, "SUBMITTING res " + new Gson().toJson(response.body()));
                            if (response.body().getStatus() == 10) {
                                viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, resources.getString(R.string.multiple_login_msg));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                if (isActivityRunning)
                                    viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                if (isActivityRunning)
                                    viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, resources.getString(R.string.authorization_expired));
                            } else if (response.body().getStatus() == 1) {
                                /*if (wayPointList != null && wayPointList.size() > 0) {
                                    addWayPoints();
                                } else*/ {
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

    /*private class SubmitAreaAsync extends AsyncTask<String, Void, String> {
        String area;
        float zoom = 15.0f;

        public SubmitAreaAsync(String area) {
            super();
            this.area = area;
            *//*try {

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(latLngList.get(0));
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
            }catch (Exception e){
                e.printStackTrace();;
            }*//*

            zoom = mMap.getCameraPosition().zoom;
        }

        @Override
        protected String doInBackground(String... params) {

            lat = new String[latLngList.size()];
            lng = new String[latLngList.size()];

            for (int j = 0; j < latLngList.size(); j++) {
                lat[j] = String.valueOf(latLngList.get(j).latitude);
                lng[j] = String.valueOf(latLngList.get(j).longitude);
            }

            //demosupervisor1
            try {
                String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                VerifySendData verifySendData = new VerifySendData();
                verifySendData.setZoom(String.valueOf(zoom));
                verifySendData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                verifySendData.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                verifySendData.setArea(AppConstants.getUploadableArea(area.toString().trim()));
                verifySendData.setLat(lat);
                verifySendData.setDeleted_by(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                verifySendData.setLng(lng);
                verifySendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                verifySendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                Call<StatusMsgModel> statusMsgModelCall = apiService.getMsgStatusForVerifyFarm(verifySendData);
                Log.e(TAG, "Sending Data " + new Gson().toJson(verifySendData));
                final boolean[] isLoaded = {false};
                long currMillis=AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<StatusMsgModel>() {

                    @Override
                    public void onResponse(Call<StatusMsgModel> call, retrofit2.Response<StatusMsgModel> response) {
                        String error=null;
                        isLoaded[0]=true;
                        if (response.isSuccessful()) {
                            Log.e("Area Response", new Gson().toJson(response.body()));
                            StatusMsgModel statusMsgModel = response.body();
                            if (statusMsgModel.getStatus() == 1) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toasty.success(context, resources.getString(R.string.area_successfully_added), Toast.LENGTH_SHORT, true).show();
                                Intent intent = getIntent();
                                setResult(RESULT_OK,intent);
                                finish();

                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, resources.getString(R.string.authorization_expired));
                            } else if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, response.body().getMsg());
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_verify_farm_area));
                                //Toast.makeText(context, "Response Error Try again latter", Toast.LENGTH_SHORT).show();
                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, resources.getString(R.string.authorization_expired));
                        } else {
                            try {
                                error=response.errorBody().string().toString();
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
                            notifyApiDelay(MapsActivityEasyMode.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                        Log.e(TAG, "Failure " + t.toString());
                        long newMillis = AppConstants.getCurrentMills();
                        isLoaded[0]=true;
                        long diff = newMillis - currMillis;
                        notifyApiDelay(MapsActivityEasyMode.this, call.request().url().toString(),
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

    }*/

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;
//        mMap.setTrafficEnabled(true);
//        mMap.setBuildingsEnabled(true);
//        mMap.setIndoorEnabled(true);

        mMap.getUiSettings().setZoomGesturesEnabled(true);
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

        /*if (farmLongitude != null && farmLatitude != null && !TextUtils.isEmpty(farmLongitude) && !TextUtils.isEmpty(farmLatitude)) {
            mMap.zo
        }*/
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000000); //  minute interval
        mLocationRequest.setFastestInterval(100000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            enableGPS();

        }else{
        }

        prepareData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            } else {
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
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            mMap.setMyLocationEnabled(false);
            final Handler handler = new Handler();

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title("Farm Location")
                    .draggable(true);

            mMarkers.put("Farm Location", markerOptions);
            Marker marker = mMap.addMarker(markerOptions);
            markerList.add(marker);


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    markFarmBoundary(Double.valueOf(farmLatitude), Double.valueOf(farmLongitude));

                }
            }, 1500);

        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                if (!onclick) {
                    mMap.getUiSettings().setScrollGesturesEnabled(true);
                    latLngList.clear();
                    if (i > 0) {
                        tvarea.setText("0");
                        remove("marker" + (i - 1));
                        // next_butt.setEnabled(false);
                        can_add = true;
                        i--;
                    }
                    markFarmBoundary(point.latitude, point.longitude);
                }
            }
        });
        view = findViewById(R.id.loupView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (magnifier == null)
                magnifier = new Magnifier(view);
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            // Fall through.
                        case MotionEvent.ACTION_MOVE: {
                            final int[] viewPosition = new int[2];
                            v.getLocationOnScreen(viewPosition);
                            magnifier.show(event.getRawX() - viewPosition[0],
                                    event.getRawY() - viewPosition[1]);
                            break;
                        }
                        case MotionEvent.ACTION_CANCEL:
                            // Fall through.
                        case MotionEvent.ACTION_UP: {
                            magnifier.dismiss();
                            findViewById(R.id.linear_lay_for_map).setElevation(-1);
                        }
                    }
                    return true;
                }
            });
        }
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.setElevation(10);
                }
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
                Log.d("CheckAreaMapActivity", "onMarkerDragEnd..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
//                onMarkerTouchStartEnd();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.setElevation(-1);
                }
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                Log.i("System out", "onMarkerDrag...");
            }
        });
    }


    private void onMarkerTouchStartDrag() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setElevation(10);
        }

    }

    private void onMarkerTouchStartEnd() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            findViewById(R.id.linear_lay_for_map).setElevation(-1);
            if (magnifier != null)
                magnifier.dismiss();
        }
    }

    private String convertAreaTo(String area) {
        String convertArea = area;
        convertArea = String.format("%.2f", parseDouble(area));
        return convertArea;
    }



    JsonObjectRequest request;
    AsyncTaskRunner taskRunner;
    ProgressDialog dialog;


    private void markFarmBoundary(double lat, double lng) {
        dialog.setMessage("Wait");
        dialog.show();
        float zoom = mMap.getCameraPosition().zoom;
        if (zoom>18)
            zoom=17;
        latTv.setText("Lat: " + String.valueOf(lat));
        lngTv.setText("Lng: " + String.valueOf(lng));
        zoomTv.setText("zoom: " + String.valueOf(zoom));
        latTv.setVisibility(View.GONE);
        zoomTv.setVisibility(View.GONE);
        lngTv.setVisibility(View.GONE);
        JSONObject object = new JSONObject();

        try {
            object.put("lat", String.valueOf(lat));
            object.put("lon", String.valueOf(lng));

            object.put("zoom", zoom);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("CheckAreaMapAct", "Sending Data " + object.toString());
        if (request != null)
            request.cancel();
        request = new JsonObjectRequest(Request.Method.POST, "https://id1i5dzi5d.execute-api.ap-south-1.amazonaws.com/final", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject mainObj = new JSONObject(response.toString());
                    Log.e("CheckAreaMapAct", "Volley Res " + mainObj);

                    if (mainObj.getInt("status") == 1) {
                        latLngList.clear();
                        try {
                            mMap.clear();
                            markerList.clear();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        JSONArray lats = response.getJSONArray("latitudes");
                        JSONArray lngs = response.getJSONArray("longitudes");
                        for (int i = 0; i < lats.length(); i++) {
                            LatLng latLng = new LatLng(lats.getDouble(i), lngs.getDouble(i));
                            latLngList.add(latLng);
                        }
                        dialog.dismiss();
                        if (taskRunner != null)
                            taskRunner.cancel(true);
                        taskRunner = new AsyncTaskRunner();
                        taskRunner.execute();
                    } else if (mainObj.getInt("status") == 2) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Toasty.info(context, resources.getString(R.string.please_zoom_map_msg_easy_mode), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Toasty.error(context, resources.getString(R.string.failed_to_measure_area_msg) + ", " + resources.getString(R.string.try_again_msg), Toast.LENGTH_LONG).show();

                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            dialog.dismiss();
                            Toasty.error(context, resources.getString(R.string.failed_to_measure_area_msg) + ", " + resources.getString(R.string.try_again_msg), Toast.LENGTH_LONG).show();

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            dialog.dismiss();
                            Toasty.error(context, resources.getString(R.string.failed_to_measure_area_msg) + ", " + resources.getString(R.string.try_again_msg), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        dialog.dismiss();
                        Toasty.error(context, resources.getString(R.string.failed_to_measure_area_msg) + ", " + resources.getString(R.string.try_again_msg), Toast.LENGTH_LONG).show();
                    }
                });
                Log.e("CheckAreaMapAct", "Volley Err " + error.toString());
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                600000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue rq = Volley.newRequestQueue(context);
        rq.add(request);

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
            i = latLngList.size();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    submit_area1();
                }
            });
            MapsActivityEasyMode.this.runOnUiThread(new Runnable() {

                public void run() {

                    /*the code you want to run after the background operation otherwise they will executed earlier and give you an error*/
                    // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                    showArea(sizeofgps);
                    // mMap.getUiSettings().setScrollGesturesEnabled(false);


                   /* back_to_farm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                            //mMap.setLatLngBoundsForCameraTarget();
                        }
                    });*/
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

    private void showArea(int i) {

        if (i < 3) {
            //Toast.makeText(context, "Please view_area_button atleast 3 points", Toast.LENGTH_SHORT).show();
            ViewFailDialog viewFailDialog = new ViewFailDialog();
            viewFailDialog.showDialog(MapsActivityEasyMode.this, resources.getString(R.string.opps_message), resources.getString(R.string.should_have_at_least_two_points_to_display));
        } else {
            LatLng[] latLng = new LatLng[latLngList.size()];

            Log.e(TAG, "Arraylisy Size " + latLngList.size() + " And Array Size " + latLng.length + " And i is " + i);

            for (int j = 0; j < latLngList.size(); j++) {
                latLng[j] = latLngList.get(j);
            }

            if (UCCpolygon!=null)
                UCCpolygon.remove();

            UCCpolygon = mMap.addPolygon(new PolygonOptions()
                    .add(latLng)
                    .strokeColor(Color.RED)
                    .fillColor(Color.parseColor("#330000FF")));
//            String a = String.valueOf(String.format("%.2f", SphericalUtil.computeArea(latLngList) * 0.000247105));

            Log.i("AREA", "computeArea " + SphericalUtil.computeArea(latLngList));
            String a = String.valueOf(String.format("%.2f", SphericalUtil.computeArea(latLngList) * area_mult_factor_def));
            tvarea.setText(convertAreaTo(AppConstants.getShowableAreaWithConversion(a, area_mult_factor)));
        }


    }
    Polygon UCCpolygon;


    private void submit_area1() {
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
            if (UCCpolygon!=null)
                UCCpolygon.remove();
            UCCpolygon = mMap.addPolygon(new PolygonOptions()
                    .add(latLng)
                    .strokeColor(Color.RED)
                    .fillColor(Color.parseColor("#330000FF")));


            Log.i("AREA", "computeArea " + SphericalUtil.computeArea(latLngs));
            String a = String.valueOf(String.format("%.2f", SphericalUtil.computeArea(latLngs) * area_mult_factor_def));
            tvarea.setText(convertAreaTo(AppConstants.getShowableAreaWithConversion(a, area_mult_factor)));


            //* 0.000247105


        }

    }

    int locMoveCount=0;

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
                if (mLastLocation!=null&&!isNearFarmCalled){
                    getNearFarms(mLastLocation.getLatitude()+"",mLastLocation.getLongitude()+"");
                    isNearFarmCalled=true;
                }
                if (locMoveCount==0) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                    progressBar.setVisibility(View.INVISIBLE);
                    locMoveCount++;
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

                new AlertDialog.Builder(this)
                        .setTitle(resources.getString(R.string.location_permission_needed))
                        .setMessage(resources.getString(R.string.this_app_needs_location_permisssion))
                        .setPositiveButton(resources.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivityEasyMode.this,
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
        mMarkers.remove(name);
//        mMarkers.get(name).remove();
//        mMarkers.remove(name);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.clear();

                for (MarkerOptions item : mMarkers.values()) {
                    mMap.addMarker(item);
                    prepareData();
                }
            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {

       /* Log.i(TAG, "Location: " + location.getLatitude() + " " + location.getLongitude());
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        LatLngFarm latLng = new LatLngFarm(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 35));
        progressBar.setVisibility(View.INVISIBLE);*/
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
                    .addOnConnectionFailedListener(MapsActivityEasyMode.this).build();
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

                                status.startResolutionForResult(MapsActivityEasyMode.this, 1000);
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
        }catch (Exception e){

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


    @Override
    protected void onStart() {
        super.onStart();
        isActivityRunning=true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityRunning=false;
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    ViewFailDialog viewFailDialog=new ViewFailDialog();
    boolean isNearFarmCalled=false;
    boolean isActivityRunning;
    private void getNearFarms(String lat, String lon) {
        if (!isNearFarmCalled) {
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
            final String farm_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
            Log.e(TAG, "SoilSenseResponse " + "lat:"+lat);
            Log.e(TAG, "SoilSenseResponse " + "long:"+lon);
            Log.e(TAG, "SoilSenseResponse " + "user_id:"+userId);
            Log.e(TAG, "SoilSenseResponse " + "token:"+token);
            Log.e(TAG, "SoilSenseResponse " + "comp_id:"+comp_id);
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
                            viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            if (isActivityRunning)
                                viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            if (isActivityRunning)
                                viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, resources.getString(R.string.authorization_expired));
                        } else if (response.body().getStatus() == 1 && response.body().getData() != null && response.body().getData().size() > 0) {

                            try {
                                PolyAsync async=new PolyAsync(response.body().getData());
                                async.execute();
                            }catch (Exception e){

                            }

                        } else {


                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        if (isActivityRunning)
                            viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        if (isActivityRunning)
                            viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, resources.getString(R.string.authorization_expired));
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
                        notifyApiDelay(MapsActivityEasyMode.this, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }

                }

                @Override
                public void onFailure(Call<NearFarmResponse> call, Throwable t) {
                    long newMillis = AppConstants.getCurrentMills();
                    isLoaded[0] = true;
                    long diff = newMillis - currMillis;
                    notifyApiDelay(MapsActivityEasyMode.this, call.request().url().toString(),
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
        isNearFarmCalled=true;
    }
    private void makePolygons(LatLng []latLngList){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.addPolygon(new PolygonOptions().add(latLngList).strokeColor(Color.parseColor("#330000FF")).fillColor(Color.parseColor("#330000FF")));
            }
        });
    }
    class PolyAsync extends AsyncTask<String ,Integer,String>{
        List<NearFarmDatum> data;

        public PolyAsync(List<NearFarmDatum> data) {
            this.data = data;
        }

        @Override
        protected String doInBackground(String... strings) {

            if (data!=null&&data.size()>0){
                for (int i=0;i<data.size();i++){
                    List<NewFarmLatLang>datumList=data.get(i).getGeometry();
                    try {
                        if (datumList!=null&&datumList.size()>2) {
                            LatLng[] latLngs = new LatLng[datumList.size()];
                            for (int j = 0;j<datumList.size();j++ ){
                                try {
                                    latLngs[j]=(new LatLng(Double.valueOf(datumList.get(j).getLatitude()),Double.valueOf(datumList.get(j).getLongitude())));
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            makePolygons(latLngs);

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }
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
                    AsyncTaskRunnerOmitance asyncTaskRunner = new AsyncTaskRunnerOmitance(tvarea.getText().toString(), etPointName.getText().toString());
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
//        List<LatLng> latLngList = new ArrayList<>();

        public AsyncTaskRunnerOmitance(String area, String name) {
            super();
            this.area = area;
            this.name = name;
            zoom = mMap.getCameraPosition().zoom;
        }

        @Override
        protected String doInBackground(String... params) {

            /*for (int j = 0; j < i; j++) {
                latLngList.add(new LatLng(latPoints[j], longPoints[j]));
            }*/

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
                    public void onResponse(Call<StatusMsgModel> call, retrofit2.Response<StatusMsgModel> response) {
                        String error = null;
                        isLoaded[0] = true;
                        Log.e(TAG, "Status OMITANCE " + response.code());
                        if (response.isSuccessful()) {
                            Log.e(TAG, "OMITANCE res " + response.body());

                            StatusMsgModel statusMsgModel = response.body();
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, response.body().getMsg());
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, resources.getString(R.string.authorization_expired));
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
                            viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivityEasyMode.this, resources.getString(R.string.authorization_expired));
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
                            notifyApiDelay(MapsActivityEasyMode.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {

                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        notifyApiDelay(MapsActivityEasyMode.this, call.request().url().toString(),
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
            viewFailDialog.showDialog(MapsActivityEasyMode.this, resources.getString(R.string.opps_message), resources.getString(R.string.should_have_at_least_two_points_to_display));
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
