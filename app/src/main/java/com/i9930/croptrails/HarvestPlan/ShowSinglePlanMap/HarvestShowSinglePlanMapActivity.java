package com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.transition.Transition;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.HarvestPlan.Model.ShowHarvestPlanData;
import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Adapter.SingleHCOuterAdapter;
import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.DistanceSorting.SortPlaces;
import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model.GpsDataForSingleHarvest;
import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model.GpsMainDataForSingleHarvest;
import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model.HarvestCollectionDetails;
import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model.Place;
import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model.SendDataForMapSingleHarvestPlan;
import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model.StatusChangeCollectionData;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Maps.MapLineModels.MapMainResponse;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegCacheManager;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegModel;
import com.i9930.croptrails.RoomDatabase.CompRegistry.Model.CompRegResult;
import com.i9930.croptrails.Utility.AnimationConstants;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class HarvestShowSinglePlanMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private String TAG = "HarvestShowSinglePlanMapActivity";
    private GoogleMap mMap;
    LocationRequest mLocationRequest;
    LocationManager locationManager;
    FusedLocationProviderClient mFusedLocationClient;
    String mprovider;
    Location location;
    HarvestShowSinglePlanMapActivity context;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Marker[] markers;
    TextView tittle;
    Toolbar mActionBarToolbar;
    ShowHarvestPlanData showHarvestPlanData;
    Resources resources;
    int harvestDataPosition;
    int flagLoc = 0;
    ArrayList<Place> places;
    ArrayList<Place> Nplaces;
    List<MapMainResponse> mapMainResponseList = new ArrayList<>();
    RecyclerView recyclerView;
    TextView submitTv;
    private boolean isEverythingPicked = true;
    TextView selectBagHeadTv;
    TextView markBagTipTv;

    GifImageView pickupStatusProgress;
    private Transition.TransitionListener mEnterTransitionListener;
    GpsMainDataForSingleHarvest data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvest_show_single_plan_map);
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        context = this;
        tittle = (TextView) findViewById(R.id.tittle);
        pickupStatusProgress=findViewById(R.id.pickupStatusProgress);
        showHarvestPlanData = DataHandler.newInstance().getShowHarvestPlanData();
        harvestDataPosition = DataHandler.newInstance().getHarvestDataPosition();
        tittle.setText(resources.getString(R.string.plan) + harvestDataPosition + " (" + showHarvestPlanData.getPlanPickupDate() + ")");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mprovider = locationManager.getBestProvider(criteria, false);
        //gpslocation=(TextView)findViewById(R.id.gps_location);
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locationManager.getLastKnownLocation(mprovider);
            mLastLocation = location;
            locationManager.requestLocationUpdates(mprovider, 2000, 1, this);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            initializeAnimator();
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }







    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    AnimationConstants constants = null;
    private void bottomSheet() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            constants = new AnimationConstants(this);
        }
        submitTv = findViewById(R.id.submitTv);
        // get the bottom sheet view
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        recyclerView = findViewById(R.id.bagRecycler);
        markBagTipTv = findViewById(R.id.markBagTipTv);
        selectBagHeadTv = findViewById(R.id.selectBagHeadTv);

        try {
            CompRegCacheManager.getInstance(context).getCompRegData(new CompRegCacheManager.CompRegListener() {
                @Override
                public void onRegLoaded(CompRegModel compRegModel) {
                    if (compRegModel != null) {
                        try {
                            CompRegResult compRegResult = new Gson().fromJson(compRegModel.getData().trim(), CompRegResult.class);
                            if (compRegResult != null) {
                                if (compRegResult.getStatus() != null && !TextUtils.isEmpty(compRegResult.getStatus()) && compRegResult.getStatus().equals("1")) {
                                    llBottomSheet.setVisibility(View.VISIBLE);
                                }else {
                                    llBottomSheet.setVisibility(View.GONE);
                                }
                            } else {
                                llBottomSheet.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            llBottomSheet.setVisibility(View.GONE);
                        }
                    } else if (AppConstants.COMP_REG.DEFAULT) {
                        llBottomSheet.setVisibility(View.VISIBLE);
                    } else {
                        llBottomSheet.setVisibility(View.GONE);
                    }
                }
            }, AppConstants.COMP_REG.HARVEST_SELL);
        }catch (Exception e){}



        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setPeekHeight(dpToPx(125));
        bottomSheetBehavior.setHideable(false);
// set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // Toast.makeText(context, "STATE_COLLAPSED", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&constants!=null) {
                        constants.exitReveal(submitTv);
                    } else {
                        submitTv.setVisibility(View.GONE);
                    }
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // Toast.makeText(context, "STATE_EXPANDED", Toast.LENGTH_SHORT).show();
                    if (submitTv.getVisibility() == View.INVISIBLE || submitTv.getVisibility() == View.GONE) {
                        if (!isEverythingPicked) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&constants!=null) {
                                constants.enterReveal(submitTv);
                            } else {
                                submitTv.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    //Toast.makeText(context, "STATE_DRAGGING", Toast.LENGTH_SHORT).show();
                } else if (newState == BottomSheetBehavior.STATE_HALF_EXPANDED) {
                    //Toast.makeText(context, "STATE_HALF_EXPANDED", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                bottomSheetBehavior.setPeekHeight(dpToPx(65));
            }
        });

        /*GpsMainDataForSingleHarvest data = new Gson().fromJson("{" +
                "    \"status\": 1," +
                "    \"msg\": \"success\"," +
                "    \"data\": [" +
                "        {" +
                "            \"no_of_bags\": \"8\"," +
                "            \"farm_id\": \"15\"," +
                "            \"lat_cord\": null," +
                "            \"long_cord\": null," +
                "            \"farm_name\": null," +
                "            \"lot_no\": \"1359\"," +
                "            \"pickedup\": \"N\"," +
                "            \"hcmid\": \"22\"," +
                "            \"farm\": [" +
                "                {" +
                "                    \"id\": \"100\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"15\"," +
                "                    \"harvest_detail_id\": \"64\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"7\"," +
                "                    \"net_wt\": \"7\"," +
                "                    \"gross_wt\": \"7\"" +
                "                }," +
                " {" +
                "                    \"id\": \"100\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"15\"," +
                "                    \"harvest_detail_id\": \"64\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"7\"," +
                "                    \"net_wt\": \"7\"," +
                "                    \"gross_wt\": \"7\"" +
                "                }," +
                " {" +
                "                    \"id\": \"100\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"15\"," +
                "                    \"harvest_detail_id\": \"64\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"7\"," +
                "                    \"net_wt\": \"7\"," +
                "                    \"gross_wt\": \"7\"" +
                "                }," +
                " {" +
                "                    \"id\": \"100\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"15\"," +
                "                    \"harvest_detail_id\": \"64\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"7\"," +
                "                    \"net_wt\": \"7\"," +
                "                    \"gross_wt\": \"7\"" +
                "                }," +
                " {" +
                "                    \"id\": \"100\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"15\"," +
                "                    \"harvest_detail_id\": \"64\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"7\"," +
                "                    \"net_wt\": \"7\"," +
                "                    \"gross_wt\": \"7\"" +
                "                }," +
                " {" +
                "                    \"id\": \"100\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"15\"," +
                "                    \"harvest_detail_id\": \"64\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"7\"," +
                "                    \"net_wt\": \"7\"," +
                "                    \"gross_wt\": \"7\"" +
                "                }," +
                " {" +
                "                    \"id\": \"100\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"15\"," +
                "                    \"harvest_detail_id\": \"64\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"7\"," +
                "                    \"net_wt\": \"7\"," +
                "                    \"gross_wt\": \"7\"" +
                "                }," +
                " {" +
                "                    \"id\": \"100\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"15\"," +
                "                    \"harvest_detail_id\": \"64\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"7\"," +
                "                    \"net_wt\": \"7\"," +
                "                    \"gross_wt\": \"7\"" +
                "                }," +
                " {" +
                "                    \"id\": \"100\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"15\"," +
                "                    \"harvest_detail_id\": \"64\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"7\"," +
                "                    \"net_wt\": \"7\"," +
                "                    \"gross_wt\": \"7\"" +
                "                }," +
                " {" +
                "                    \"id\": \"100\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"15\"," +
                "                    \"harvest_detail_id\": \"64\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"7\"," +
                "                    \"net_wt\": \"7\"," +
                "                    \"gross_wt\": \"7\"" +
                "                }," +
                "                {" +
                "                    \"id\": \"101\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"15\"," +
                "                    \"harvest_detail_id\": \"127\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"2\"," +
                "                    \"net_wt\": \"100\"," +
                "                    \"gross_wt\": \"100\"" +
                "                }," +
                "                {" +
                "                    \"id\": \"102\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"15\"," +
                "                    \"harvest_detail_id\": \"131\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"6\"," +
                "                    \"net_wt\": \"160\"," +
                "                    \"gross_wt\": \"160\"" +
                "                }," +
                "                {" +
                "                    \"id\": \"103\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"15\"," +
                "                    \"harvest_detail_id\": \"135\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"0\"," +
                "                    \"net_wt\": \"100\"," +
                "                    \"gross_wt\": \"100\"" +
                "                }," +
                "                {" +
                "                    \"id\": \"104\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"15\"," +
                "                    \"harvest_detail_id\": \"136\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"1\"," +
                "                    \"net_wt\": \"90\"," +
                "                    \"gross_wt\": \"90\"" +
                "                }," +
                "                {" +
                "                    \"id\": \"105\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"15\"," +
                "                    \"harvest_detail_id\": \"137\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"2\"," +
                "                    \"net_wt\": \"80\"," +
                "                    \"gross_wt\": \"80\"" +
                "                }," +
                "                {" +
                "                    \"id\": \"106\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"15\"," +
                "                    \"harvest_detail_id\": \"138\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"3\"," +
                "                    \"net_wt\": \"70\"," +
                "                    \"gross_wt\": \"70\"" +
                "                }," +
                "                {" +
                "                    \"id\": \"107\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"15\"," +
                "                    \"harvest_detail_id\": \"139\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"4\"," +
                "                    \"net_wt\": \"85\"," +
                "                    \"gross_wt\": \"85\"" +
                "                }" +
                "            ]" +
                "        }," +
                "        {" +
                "            \"no_of_bags\": \"2\"," +
                "            \"farm_id\": \"16\"," +
                "            \"lat_cord\": null," +
                "            \"long_cord\": null," +
                "            \"farm_name\": null," +
                "            \"lot_no\": \"1358\"," +
                "            \"pickedup\": \"N\"," +
                "            \"hcmid\": \"22\"," +
                "            \"farm\": [" +
                "                {" +
                "                    \"id\": \"108\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"16\"," +
                "                    \"harvest_detail_id\": \"116\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"4\"," +
                "                    \"net_wt\": \"4\"," +
                "                    \"gross_wt\": \"4\"" +
                "                }," +
                "                {" +
                "                    \"id\": \"109\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"16\"," +
                "                    \"harvest_detail_id\": \"117\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"5\"," +
                "                    \"net_wt\": \"5\"," +
                "                    \"gross_wt\": \"5\"" +
                "                }" +
                "            ]" +
                "        }," +
                "        {" +
                "            \"no_of_bags\": \"3\"," +
                "            \"farm_id\": \"64\"," +
                "            \"lat_cord\": null," +
                "            \"long_cord\": null," +
                "            \"farm_name\": null," +
                "            \"lot_no\": \"1001\"," +
                "            \"pickedup\": \"N\"," +
                "            \"hcmid\": \"22\"," +
                "            \"farm\": [" +
                "                {" +
                "                    \"id\": \"110\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"64\"," +
                "                    \"harvest_detail_id\": \"121\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"6\"," +
                "                    \"net_wt\": \"6\"," +
                "                    \"gross_wt\": \"6\"" +
                "                }," +
                "                {" +
                "                    \"id\": \"111\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"64\"," +
                "                    \"harvest_detail_id\": \"122\"," +
                "                    \"pickedup\": \"Y\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"7\"," +
                "                    \"net_wt\": \"7\"," +
                "                    \"gross_wt\": \"7\"" +
                "                }," +
                "                {" +
                "                    \"id\": \"112\"," +
                "                    \"hcmid\": \"22\"," +
                "                    \"farm_id\": \"64\"," +
                "                    \"harvest_detail_id\": \"124\"," +
                "                    \"pickedup\": \"N\"," +
                "                    \"arrived\": \"N\"," +
                "                    \"doa\": \"2018-11-25 02:30:53\"," +
                "                    \"is_active\": \"Y\"," +
                "                    \"bag_no\": \"9\"," +
                "                    \"net_wt\": \"9\"," +
                "                    \"gross_wt\": \"9\"" +
                "                }" +
                "            ]" +
                "        }" +
                "    ]" +
                "}", GpsMainDataForSingleHarvest.class);*/
        recyclerData(data);

        submitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> ids = new ArrayList<>();
                List<HarvestCollectionDetails> dataToUpload = new ArrayList<>();
                try {
                    for (int i = 0; i < data.getData().size(); i++) {
                        float qty = 0;
                        for (int j = 0; j < data.getData().get(i).getHarvestCollectionDetailsList().size(); j++) {
                            if (data.getData().get(i).getHarvestCollectionDetailsList().get(j).getPickedup().trim().toUpperCase().equals("Y")
                                    && !data.getData().get(i).getHarvestCollectionDetailsList().get(j).isAlreadyPickedUp()) {
                                Log.e(TAG, i + " Data " + j + " " + new Gson().toJson(data.getData().get(i).getHarvestCollectionDetailsList().get(j)));
                                ids.add(data.getData().get(i).getHarvestCollectionDetailsList().get(j).getHarvestDetailId());
                                qty = qty + Float.valueOf(data.getData().get(i).getHarvestCollectionDetailsList().get(j).getNetWeight().trim());

                            }
                        }
                        HarvestCollectionDetails details = new HarvestCollectionDetails();
                        details.setFarmId(data.getData().get(i).getFarmId());
                        details.setSaleQty("" + qty);
                        dataToUpload.add(details);
                    }
                    if (ids.size() > 0)
                        changeStatusOfCollection(ids, dataToUpload);
                    else
                        Toasty.error(context, resources.getString(R.string.select_atleast_one_bag_to_pick_up), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void changeStatusOfCollection(List<String> ids, List<HarvestCollectionDetails> dataToUpload) {
        submitTv.setClickable(false);
        submitTv.setEnabled(false);
        pickupStatusProgress.setVisibility(View.VISIBLE);
        StatusChangeCollectionData data = new StatusChangeCollectionData(ids, dataToUpload,
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        Log.e(TAG, "Update Data " + new Gson().toJson(data));
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();
        apiInterface.updateStatusOfHarvestCollection(data).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                String error=null;
                isLoaded[0]=true;
                Log.e(TAG,"updateStatusOfHarvestCollection code "+response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG,"updateStatusOfHarvestCollection res "+new Gson().toJson(response.body()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        exitReveal(pickupStatusProgress);
                    } else {
                        pickupStatusProgress.setVisibility(View.GONE);
                    }
                    if (response.body().getStatus() == 10) {
                        submitTv.setClickable(true);
                        submitTv.setEnabled(true);
                        pickupStatusProgress.setVisibility(View.GONE);
                        new ViewFailDialog().showSessionExpireDialog(HarvestShowSinglePlanMapActivity.this);
                    } else if (response.body().getStatus() == 1) {
                        pickupStatusProgress.setVisibility(View.GONE);
                        finish();

                    } else {
                        pickupStatusProgress.setVisibility(View.GONE);
                        submitTv.setClickable(true);
                        submitTv.setEnabled(true);
                        Toasty.error(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();

                    }
                }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(HarvestShowSinglePlanMapActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(HarvestShowSinglePlanMapActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    submitTv.setClickable(true);
                    submitTv.setEnabled(true);
                    pickupStatusProgress.setVisibility(View.GONE);
                    Toasty.error(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();

                    try {
                        error=response.errorBody().string();
                        Log.e(TAG,"updateStatusOfHarvestCollection err "+error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(HarvestShowSinglePlanMapActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                isLoaded[0]=true;
                notifyApiDelay(HarvestShowSinglePlanMapActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Toasty.error(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                pickupStatusProgress.setVisibility(View.GONE);
                submitTv.setClickable(true);
                submitTv.setEnabled(true);
                Log.e(TAG,"updateStatusOfHarvestCollection fail "+t.toString());
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
                            ConnectivityUtils.checkSpeedWithColor(HarvestShowSinglePlanMapActivity.this, new ConnectivityUtils.ColorListener() {
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
    String internetSPeed = "";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initializeAnimator() {
        mEnterTransitionListener = new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {

            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        };
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
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

        FetchFarmsGpsAsyncTask fetchFarmsGpsAsyncTask = new FetchFarmsGpsAsyncTask();
        fetchFarmsGpsAsyncTask.execute();
        // Add a marker in Sydney and move the camera
        /*LatLngFarm sydney = new LatLngFarm(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));*/
    }

    @Override
    public void onLocationChanged(Location location) {

        // Toast.makeText(this, location.getLatitude()+"", Toast.LENGTH_SHORT).show();
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        enableGPS();

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

    public class FetchFarmsGpsAsyncTask extends AsyncTask<String, Void, String> {
        LatLng currentlatlng;

        @Override
        protected String doInBackground(String... strings) {
            String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            SendDataForMapSingleHarvestPlan sendDataForMapSingleHarvestPlan = new SendDataForMapSingleHarvestPlan();
            sendDataForMapSingleHarvestPlan.setHcmid(DataHandler.newInstance().getHcmid());
            sendDataForMapSingleHarvestPlan.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            sendDataForMapSingleHarvestPlan.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            Log.e(TAG, "Data Sent " + new Gson().toJson(sendDataForMapSingleHarvestPlan));
            Call<GpsMainDataForSingleHarvest> expenseDataCall = apiService.getGpsDataForSingleHarvest(sendDataForMapSingleHarvestPlan);
            expenseDataCall.enqueue(new Callback<GpsMainDataForSingleHarvest>() {
                @Override
                public void onResponse(Call<GpsMainDataForSingleHarvest> call, Response<GpsMainDataForSingleHarvest> response) {
                    Log.e(TAG,"getGpsDataForSingleHarvest code "+response.code());
                    if (response.isSuccessful()) {
                        Log.e(TAG,"getGpsDataForSingleHarvest res "+new Gson().toJson(response.body()));
                        if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(HarvestShowSinglePlanMapActivity.this, response.body().getMsg());
                        } else if (response.body().getStatus()== AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(HarvestShowSinglePlanMapActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(HarvestShowSinglePlanMapActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            GpsMainDataForSingleHarvest gpsMainDataForSingleHarvest = response.body();
                            data = response.body();
                            bottomSheet();
                            List<GpsDataForSingleHarvest> gpsDataForSingleHarvestList = gpsMainDataForSingleHarvest.getData();
                            boolean flag = false;
                            markers = new Marker[gpsDataForSingleHarvestList.size()];
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            if (mLastLocation != null) {
                                currentlatlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                                builder.include(currentlatlng);
                            } else {
                                Toast.makeText(context, resources.getString(R.string.location_not_available), Toast.LENGTH_SHORT).show();

                            }
                            for (int i = 0; i < gpsDataForSingleHarvestList.size(); i++) {
                                if (gpsDataForSingleHarvestList.get(i).getLongCord() != null && !gpsDataForSingleHarvestList.get(i).getLongCord().equals("0")) {
                                    Log.e("GpsData", gpsDataForSingleHarvestList.get(i).getLatCord() + " " + gpsDataForSingleHarvestList.get(i).getLongCord());
                                    markers[i] = createMarker(gpsDataForSingleHarvestList.get(i).getLatCord(), gpsDataForSingleHarvestList.get(i).getLongCord(),
                                            gpsDataForSingleHarvestList.get(i).getNoOfBags(), gpsDataForSingleHarvestList.get(i).getFarmId()/* markersArray.get(i).getIconResID()*/);
                                    // markers[i].showInfoWindow();
                                    builder.include(markers[i].getPosition());
                                    flag = true;
                                }
                            }
                            LatLngBounds bounds = null;
                            if (flag) {
                                bounds = builder.build();
                            }

                            int width = getResources().getDisplayMetrics().widthPixels;
                            int height = getResources().getDisplayMetrics().heightPixels;
                            int padding = (int) (width * .15);

                            if (bounds != null) {
                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                                LatLng latLng = new LatLng(Double.parseDouble(gpsDataForSingleHarvestList.get(0).getLatCord()), Double.parseDouble(gpsDataForSingleHarvestList.get(0).getLongCord()));
                                mMap.animateCamera(cu);
                                String[] results = new String[markers.length];
                                String path_to_first_loc = null;
                                places = new ArrayList<Place>();
                                Nplaces = new ArrayList<Place>();
                                if (mLastLocation != null) {
                                    currentlatlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                                    places.add(new Place(currentlatlng));
                                    for (int m = 0; m < markers.length; m++) {
                                        if (markers[m] != null)
                                            places.add(new Place(new LatLng(markers[m].getPosition().latitude, markers[m].getPosition().longitude)));
                                    }
                                    Nplaces = sortNextNearestFirst(places, places.get(0));
                                    // Nplaces.add(places.get(0));
                                    // Collections.sort(places, new SortPlaces(currentlatlng));
                               /* path_to_first_loc=makeURL(mLastLocation.getLatitude(),mLastLocation.getLongitude(),Nplaces.get(0).latlng.latitude,Nplaces.get(0).latlng.longitude);
                                drawPath(path_to_first_loc);*/
                                }
                                for (int m = 0; m < Nplaces.size() - 1; m++) {
                                    results[m] = makeURL(Nplaces.get(m).latlng.latitude, Nplaces.get(m).latlng.longitude, Nplaces.get(m + 1).latlng.latitude, Nplaces.get(m + 1).latlng.longitude);
                                    drawPath(results[m]);
                                    Log.e("result", results[m]);
                                    // Toast.makeText(context, "path provided by api", Toast.LENGTH_SHORT).show();
                                }
                       /* StringBuffer alpha = new StringBuffer(),
                                num = new StringBuffer();
                        for (int count=0;count<mapMainResponseList.size();count++){
                            for (int i=0; i<mapMainResponseList.get(count).getRoutes().get(0).getLegs().get(0).getDistance().getText().length(); i++)
                            {
                                if (Character.isDigit(mapMainResponseList.get(count).getRoutes().get(0).getLegs().get(0).getDistance().getText().charAt(i)))
                                    num.append(mapMainResponseList.get(count).getRoutes().get(0).getLegs().get(0).getDistance().getText().charAt(i));

                            }
                            mapMainResponseList.get(count).getRoutes().get(0).getLegs().get(0).getDistance().setText(num.toString());
                        }
                        Collections.sort(mapMainResponseList, new Comparator<MapMainResponse>() {
                            @Override
                            public int compare(MapMainResponse mapMainResponse, MapMainResponse t1) {
                                return Float.valueOf(mapMainResponse.getRoutes().get(0).getLegs().get(0).getDistance().getText())
                                        .compareTo(Float.valueOf(t1.getRoutes().get(0).getLegs().get(0).getDistance().getText()));
                            }
                        });
                        for (int i = 0; i < mapMainResponseList.size(); i++) {
                            Log.e("MainRespo", "position " + i);
                            drawPath(mapMainResponseList.get(i));
                        }*/
                       /* new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (flagLoc <= Nplaces.size() - 1) {
                                    Log.e("MainRespo", "position " + mapMainResponseList.size());
                                    for (int i = 0; i < mapMainResponseList.size(); i++) {
                                        Log.e("MainRespo", "position " + i);
                                        drawPath(mapMainResponseList.get(i));
                                    }
                                }
                            }
                        }).start();*/

                            } else {
                            }


                        }
                    }else if (response.code()== AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(HarvestShowSinglePlanMapActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(HarvestShowSinglePlanMapActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            Log.e(TAG, "getGpsDataForSingleHarvest Err " + response.errorBody().string());

                            Toasty.error(context, resources.getString(R.string.something_went_wrong_msg), Toast.LENGTH_LONG, false).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GpsMainDataForSingleHarvest> call, Throwable t) {
                    Toasty.error(context, resources.getString(R.string.something_went_wrong_msg), Toast.LENGTH_LONG, false).show();
                    Log.e(TAG,"getGpsDataForSingleHarvest "+t.toString());
                }
            });
            return null;
        }
    }

    private ArrayList<Place> sortNextNearestFirst(ArrayList<Place> places, Place place) {
        Collections.sort(places, new SortPlaces(place.latlng));
        Nplaces.add(places.get(0));
        places.remove(places.get(0));
        if (places.size() > 0) {
            sortNextNearestFirst(places, places.get(0));
        }
        return Nplaces;
    }

    @SuppressLint("Range")
    protected Marker createMarker(String latitude, String longitude, String title, String snippet) {
        LinearLayout tv = (LinearLayout) this.getLayoutInflater().inflate(R.layout.lay_for_planned_bags_map, null, false);
        tv.measure(View.MeasureSpec.makeMeasureSpec(300, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(RelativeLayout.LayoutParams.WRAP_CONTENT, View.MeasureSpec.UNSPECIFIED));
        TextView tv1 = (TextView) tv.findViewById(R.id.title_for_planned_bags_map);
        ImageView im1 = (ImageView) tv.findViewById(R.id.icon_for_planned_bags_map);
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

        // im1.setLayoutParams(new LinearLayout.LayoutParams(100,100));
        //im1.sca
        tv1.setText(resources.getString(R.string.bags) + title);
        // tv1.setLayoutParams(new LinearLayout.LayoutParams(100, 50));
        tv1.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setDrawingCacheEnabled(true);
        tv.buildDrawingCache();
        Bitmap bm = tv.getDrawingCache();


        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromBitmap(bm))/*fromResource(R.drawable.crp_trails_icon_splash_splash))*/);
/*
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)))*/
        /*BitmapDescriptorFactory.fromResource(iconResID))*//*
;
*/
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
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
                //Toast.makeText(context, location.getLatitude()+"", Toast.LENGTH_SHORT).show();
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

            }
        }
    };

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
                        .setMessage(resources.getString(R.string.this_app_needs_location_permission))
                        .setPositiveButton(resources.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(HarvestShowSinglePlanMapActivity.this,
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

    private void enableGPS() {
        /*if (mGoogleApiClient == null) {*/

        //Toast.makeText(context, "Coming in enable gps", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(HarvestShowSinglePlanMapActivity.this).build();
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

                            status.startResolutionForResult(HarvestShowSinglePlanMapActivity.this, 1000);
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


    public String makeURL(double sourcelat, double sourcelog, double destlat, double destlog) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from

        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=" + getResources().getString(R.string.google_maps_key));
        return urlString.toString();
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }


    public void drawPath(String result) {
        flagLoc++;

        Random rnd = new Random();
        final int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, result,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject json = null;
                            try {
                                json = new JSONObject(response.toString());
                                MapMainResponse mainResponse = new Gson().fromJson(json.toString(), MapMainResponse.class);
                                mapMainResponseList.add(new Gson().fromJson(json.toString(), MapMainResponse.class));
                                Log.e("MainRespo", "resp " + new Gson().toJson(mainResponse));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JSONArray routeArray = null;
                            try {
                                routeArray = json.getJSONArray("routes");
                                JSONObject routes = routeArray.getJSONObject(0);

                                JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
                                String encodedString = overviewPolylines.getString("points");
                                Log.e("Points", encodedString);
                                List<LatLng> list = decodePoly(encodedString);
                                Log.e("PolyList", "Decoded list " + new Gson().toJson(routeArray));
                                int[] mix_color = context.getResources().getIntArray(R.array.mix_blue);
                                int zvalue = 0;
                                for (int z = 0; z < list.size() - 1; z++) {
                                    LatLng src = list.get(z);
                                    LatLng dest = list.get(z + 1);
                                    //Random rnd = new Random();
                                    zvalue = z;
                                    if (z < 5) {
                                        Log.e("color_array", String.valueOf(mix_color[z]));
                                    }
                                    Polyline line = mMap.addPolyline(new PolylineOptions()
                                            .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude))
                                            .width(7)
                                            .color(color).geodesic(true));


                                }
                                Log.e("value of z", String.valueOf(zvalue));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error", error.toString());
                            // Toast.makeText(context, getString(R.string.error_text), Toast.LENGTH_SHORT).show();
                            //  progressDialog.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                  /*  if(user_num!=null){
                        params.put(KEY_USER_NUM,user_num);
                    }
                    if(ct1!=null){
                        params.put(KEY_TOKEN,ct1);
                    }*/

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }


    }

    public static List<Location> sortLocations(List<Location> locations,
                                               final double myLatitude, final double myLongitude) {
        Comparator comp = new Comparator<Location>() {
            @Override
            public int compare(Location o, Location o2) {
                float[] result1 = new float[3];
                android.location.Location.distanceBetween(myLatitude, myLongitude, o.getLatitude(), o.getLongitude(), result1);
                Float distance1 = result1[0];

                float[] result2 = new float[3];
                android.location.Location.distanceBetween(myLatitude, myLongitude, o2.getLatitude(), o2.getLongitude(), result2);
                Float distance2 = result2[0];

                return distance1.compareTo(distance2);
            }
        };


        Collections.sort(locations, comp);
        return locations;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void exitReveal(View get_otp_tv) {
        int cx = get_otp_tv.getMeasuredWidth() / 2;
        int cy = get_otp_tv.getMeasuredHeight() / 2;
        // get the initial radius for the clipping circle
        int initialRadius = get_otp_tv.getWidth() / 2;

        // create the animation (the final radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(get_otp_tv, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                get_otp_tv.setVisibility(View.INVISIBLE);
            }
        });

        // start the animation
        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enterReveal(View get_otp_tv) {
        int cx = get_otp_tv.getMeasuredWidth() / 2;
        int cy = get_otp_tv.getMeasuredHeight() / 2;
        int finalRadius = Math.max(get_otp_tv.getWidth(), get_otp_tv.getHeight()) / 2;
        Animator anim = ViewAnimationUtils.createCircularReveal(get_otp_tv, cx, cy, 0, finalRadius);
        get_otp_tv.setVisibility(View.VISIBLE);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                getWindow().getEnterTransition().removeListener(mEnterTransitionListener);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }


    private void recyclerData(GpsMainDataForSingleHarvest data) {
        List<Integer> alreadyAllPickStatus = new ArrayList<>();
        for (int i = 0; i < data.getData().size(); i++) {
            int count = 0;
            for (int j = 0; j < data.getData().get(i).getHarvestCollectionDetailsList().size(); j++) {
                if (data.getData().get(i).getHarvestCollectionDetailsList().get(j).getPickedup().trim().toUpperCase().equals("Y")) {
                    data.getData().get(i).getHarvestCollectionDetailsList().get(j).setAlreadyPickedUp(true);
                    count++;
                }
            }
            if (data.getData().get(i).getHarvestCollectionDetailsList().size() == count) {
                data.getData().get(i).setAllSelected(true);
                alreadyAllPickStatus.add(1);
            } else {
                data.getData().get(i).setAllSelected(false);
                alreadyAllPickStatus.add(0);
                isEverythingPicked = false;
            }
            Log.e(TAG, "List Size -> " + data.getData().get(i).getHarvestCollectionDetailsList().size() + " And Count -> " + count);
        }
        if (isEverythingPicked) {
            markBagTipTv.setText(resources.getString(R.string.mark_bags_tip_no_remaining_bags_msg));
            selectBagHeadTv.setText(resources.getString(R.string.alredy_all_bags_selected_label));
        } else {
            markBagTipTv.setText(resources.getString(R.string.mark_bags_tip_msg));
            selectBagHeadTv.setText(resources.getString(R.string.select_bags_label));
        }

        SingleHCOuterAdapter adapter = new SingleHCOuterAdapter(context, data.getData(), alreadyAllPickStatus);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

}
