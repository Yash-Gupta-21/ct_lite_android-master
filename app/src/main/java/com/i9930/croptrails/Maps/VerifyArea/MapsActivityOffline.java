package com.i9930.croptrails.Maps.VerifyArea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.geometry.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.Landing.Models.UpdateStandingArea.UpdateStandingResponse;
import com.i9930.croptrails.Language.LocaleHelper;
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

public class MapsActivityOffline extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

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

    @BindView(R.id.mapTypeImage)
    ImageView mapTypeImage;
    int mapChoosen = 101;

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
        Toast.makeText(this, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.help_menu:
                // do your code
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_offline);

        final String languageCode = SharedPreferencesMethod2.getString(MapsActivityOffline.this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        context = this;
        ButterKnife.bind(this);
        area_mult_factor = SharedPreferencesMethod.getDoubleSharedPreferences(context, SharedPreferencesMethod.AREA_MULTIPLICATON_FACTOR);

        initViews();
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
        progressBar.setVisibility(View.VISIBLE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        mprovider = locationManager.getBestProvider(criteria, false);
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider, 100, 1, this);

        }

        /*if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL).toLowerCase().equals("hectare")) {
            multiplicationFactor = 2.47105;
        } else if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL).toLowerCase().equals("malwa_bigha")) {
            multiplicationFactor = 3.63;
        } else {

        }
*/
        next_butt.setEnabled(false);
        area_unit_label.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL));
        latPoints = new Double[100];
        longPoints = new Double[100];
        Log.e(TAG, "Multiplication Factor " + area_mult_factor);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i > 2) {
                    //onclick=true;
                    submit_area();
                    next_butt.setEnabled(true);
                    can_add = false;
                    // view_area_button.setEnabled(false);
                } else {
                    //Toast.makeText(context, "Please view_area_button atleast 3 points", Toast.LENGTH_SHORT).show();
                    ViewFailDialog failDialog = new ViewFailDialog();
                    failDialog.showDialog(MapsActivityOffline.this, resources.getString(R.string.please_submit_at_least_3_points));
                }
            }
        });

        next_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setMessage(resources.getString(R.string.sure_to_submit_area))
                        .setCancelable(false)
                        .setPositiveButton(resources.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (Double.parseDouble(tvarea.getText().toString().trim()) > 0) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    MapsActivityOffline.AsyncTaskRunner asyncTaskRunner = new MapsActivityOffline.AsyncTaskRunner(tvarea.getText().toString());
                                    asyncTaskRunner.execute();
                                } else {
                                    //Toast.makeText(context, "Area is too short to capture.. Please capture again", Toast.LENGTH_LONG).show();
                                    ViewFailDialog failDialog = new ViewFailDialog();
                                    failDialog.showDialog(MapsActivityOffline.this, resources.getString(R.string.opps_message), resources.getString(R.string.area_is_too_short_for_capture));
                                }
                            }
                        })
                        .setNegativeButton(resources.getString(R.string.no), null)
                        .show();
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
                    //viewFailDialog.showDialog(MapsActivityOffline.this,"Opps!","Please Enter valid Area");
                } else {
                    update_standing_progress.setVisibility(View.VISIBLE);
                    MapsActivityOffline.UpdateStandingAcres updateStandingAcres = new MapsActivityOffline.UpdateStandingAcres(update_acres_dialog_et.getText().toString(), dialog, farmId);
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
            String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            final String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            final String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            Call<UpdateStandingResponse> updateStandingAcres = apiInterface.updateStandingAcres(farmId, AppConstants.getUploadableArea(standingAcres), userId,token);
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
                            Toasty.success(context, resources.getString(R.string.actual_area_update_successfully), Toast.LENGTH_LONG,true).show();
                            setResult(RESULT_OK);
                            finish();
                        } else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivityOffline.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivityOffline.this, resources.getString(R.string.authorization_expired));
                        }else if (response.body().getStatus()==10){
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivityOffline.this, response.body().getMsg());
                        } else {
                            Log.e("FarmDetailAdapter e1", new Gson().toJson(response.body()));
                            //Toast.makeText(context, "Failed to update", Toast.LENGTH_LONG).show();

                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(MapsActivityOffline.this, resources.getString(R.string.opps_message), response.body().getMsg());

                            if (update_standing_progress != null) {
                                update_standing_progress.setVisibility(View.INVISIBLE);
                            }
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        }
                    } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(MapsActivityOffline.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(MapsActivityOffline.this, resources.getString(R.string.authorization_expired));
                    }else {
                        try {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(MapsActivityOffline.this, resources.getString(R.string.opps_message), resources.getString(R.string.failed_to_update_actual_area));
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            if (update_standing_progress != null) {
                                update_standing_progress.setVisibility(View.INVISIBLE);
                            }
                            //Toast.makeText(context, "Failed to update", Toast.LENGTH_LONG).show();
                            Log.e("FarmDetailAdapter e2", response.errorBody().string().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<UpdateStandingResponse> call, Throwable t) {
                    //Toast.makeText(context, "Failed to update", Toast.LENGTH_LONG).show();
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showDialog(MapsActivityOffline.this, resources.getString(R.string.opps_message), resources.getString(R.string.failed_to_update_actual_area));
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

    private void initViews() {
        title = (TextView) findViewById(R.id.tittle);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        tvarea = (TextView) findViewById(R.id.area_tv);
        submit = findViewById(R.id.submit_butt);
        butt_clear = findViewById(R.id.clear_butt);
        next_butt = findViewById(R.id.next_butt);
        progressBar =  findViewById(R.id.progressBar_cyclic);
        area_unit_label = (TextView) findViewById(R.id.area_unit_label);


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
        mapChoosen = MapsActivityOffline.MAP_TYPE.DEFAULT_MAP;
        mapTypeImage.setImageResource(R.drawable.map_type_normal);
        mapTypeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapChoosen == MapsActivityOffline.MAP_TYPE.DEFAULT_MAP) {
                    mapChoosen= MapsActivityOffline.MAP_TYPE.SATELLITE_MAP;
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    mapTypeImage.setImageResource(R.drawable.map_type_satellite);
                } else {
                    mapChoosen= MapsActivityOffline.MAP_TYPE.DEFAULT_MAP;
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mapTypeImage.setImageResource(R.drawable.map_type_normal);
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
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }
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
                     /* MarkerOptions marker = new MarkerOptions().position(
                                new LatLngFarm(point.latitude, point.longitude)).title("New Marker");
*/
                                LatLng latLng = new LatLng(point.latitude, point.longitude);
                                add("marker" + i, latLng);
                                //mMap.addMarker(marker);
                                Log.e("TOUCH Points", point.latitude + "    " + point.longitude);

                            }
                        }
                    });
                } else {
                    onclick = true;
                    //    Toast.makeText(context, "Outside Circle"+" radius"+linear_lay_for_map.getWidth()/10, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(context, resources.getString(R.string.please_click_inside_circle_to_mark), Toast.LENGTH_LONG).show();
                    Toasty.info(context, resources.getString(R.string.please_click_inside_circle_to_mark), Toast.LENGTH_LONG, true).show();
                    //ViewFailDialog viewFailDialog=new ViewFailDialog();
                    //viewFailDialog.showDialog(MapsActivityOffline.this,resources.getString(R.string.opps_message),resources.getString(R.string.please_click_inside_circle_to_mark));
                }
                return false;
            }
        });


    }

    private void submit_area() {
        Polygon UCCpolygon;

        if (i < 3) {
            //Toast.makeText(context, "Please view_area_button atleast 3 points", Toast.LENGTH_SHORT).show();
            ViewFailDialog viewFailDialog = new ViewFailDialog();
            viewFailDialog.showDialog(MapsActivityOffline.this, resources.getString(R.string.opps_message), resources.getString(R.string.please_submit_at_least_3_points));
        } else {
            LatLng[] latLng = new LatLng[i];
            List<LatLng> latLngs = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                latLng[j] = new LatLng(latPoints[j], longPoints[j]);
                latLngs.add(new LatLng(latPoints[j], longPoints[j]));
            }
            UCCpolygon = mMap.addPolygon(new PolygonOptions().add(latLng).strokeColor(Color.RED).fillColor(Color.parseColor("#330000FF")));
            Log.i("AREA", "computeArea " + SphericalUtil.computeArea(latLngs));
            String a = String.valueOf(String.format("%.2f", SphericalUtil.computeArea(latLngs) * area_mult_factor_def));
            tvarea.setText(AppConstants.getShowableAreaWithConversion(a,area_mult_factor));
        }

    }


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivityOffline", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30));
                progressBar.setVisibility(View.INVISIBLE);
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
                                ActivityCompat.requestPermissions(MapsActivityOffline.this,
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
                Toasty.error(context, resources.getString(R.string.cant_add_more_points), Toast.LENGTH_LONG, true).show();

            }
        } else {
            // Toast.makeText(context, resources.getString(R.string.please_clear_then_add), Toast.LENGTH_LONG).show();
            Toasty.error(context, resources.getString(R.string.please_clear_then_add), Toast.LENGTH_LONG, true).show();

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
                .addOnConnectionFailedListener(MapsActivityOffline.this).build();
        mGoogleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
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
                        // Toast.makeText(MapsActivityOffline.this, "Gps Enabled", Toast.LENGTH_SHORT).show();

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
                            //Toast.makeText(MapsActivityOffline.this, "Requesting for gps", Toast.LENGTH_SHORT).show();

                            status.startResolutionForResult(MapsActivityOffline.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Toast.makeText(MapsActivityOffline.this, "Requesting for gps in catch", Toast.LENGTH_SHORT).show();

                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have
                        // no way to fix the
                        // settings so we won't show the dialog.
                        //Toast.makeText(MapsActivityOffline.this, "Settings change unavailable", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        //}
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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


    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {
        String area;
        float zoom=15.0f;

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
                String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                VerifySendData verifySendData = new VerifySendData();
                verifySendData.setZoom(String.valueOf(zoom));
                verifySendData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                verifySendData.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                verifySendData.setArea(AppConstants.getUploadableArea(area.trim()));
                verifySendData.setLat(lat);
                verifySendData.setDeleted_by(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                verifySendData.setLng(lng);
                verifySendData.setUserId(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.USER_ID));
                verifySendData.setToken(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.TOKEN));
                Call<StatusMsgModel> statusMsgModelCall = apiService.getMsgStatusForVerifyFarm(verifySendData);
                Log.e(TAG, "Sending Data " + new Gson().toJson(verifySendData));
                final boolean[] isLoaded = {false};
                long currMillis=AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<StatusMsgModel>() {

                    @Override
                    public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                        String error=null;
                        isLoaded[0]=true;
                        if (response.isSuccessful()) {
                            Log.e("Area Response", new Gson().toJson(response.body()));
                            StatusMsgModel statusMsgModel = response.body();
                            if (statusMsgModel.getStatus() == 1) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toasty.success(context, resources.getString(R.string.area_successfully_added), Toast.LENGTH_SHORT,true).show();

                                Intent intent = getIntent();
                                setResult(RESULT_OK);
                                finish();
                                //Toast.makeText(context, statusMsgModel.getMsg(), Toast.LENGTH_SHORT).show();
                            } else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivityOffline.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivityOffline.this, resources.getString(R.string.authorization_expired));
                            }else if (response.body().getStatus()==10){
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(MapsActivityOffline.this, response.body().getMsg());
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(MapsActivityOffline.this, resources.getString(R.string.failed_to_verify_farm_area));
                                //Toast.makeText(context, "Response Error Try again latter", Toast.LENGTH_SHORT).show();
                            }
                        }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivityOffline.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(MapsActivityOffline.this, resources.getString(R.string.authorization_expired));
                        } else {
                            try {
                                error=response.errorBody().string().toString();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(MapsActivityOffline.this, resources.getString(R.string.failed_to_verify_farm_area));
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
                            notifyApiDelay(MapsActivityOffline.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                        long newMillis = AppConstants.getCurrentMills();
                        isLoaded[0]=true;
                        long diff = newMillis - currMillis;
                        notifyApiDelay(MapsActivityOffline.this, call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);
                        Log.e(TAG, "Failure " + t.toString());
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(MapsActivityOffline.this, resources.getString(R.string.failed_to_verify_farm_area));
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
                            ConnectivityUtils.checkSpeedWithColor(MapsActivityOffline.this, new ConnectivityUtils.ColorListener() {
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

    private boolean isInsideCircle(Point touchedPoint) {
        int distance = (int) Math.round(Math.pow(touchedPoint.x - linear_lay_for_map.getPivotX(), 2) + Math.pow(touchedPoint.y - linear_lay_for_map.getPivotY(), 2));

        if (distance < Math.pow(linear_lay_for_map.getWidth() / 10, 2)) {
            return true;
        } else {
            return false;
        }
    }



    private String convertAreaTo(String area) {
        String convertArea = area;
        convertArea = String.format("%.2f", parseDouble(area));
        return convertArea;
    }

}
