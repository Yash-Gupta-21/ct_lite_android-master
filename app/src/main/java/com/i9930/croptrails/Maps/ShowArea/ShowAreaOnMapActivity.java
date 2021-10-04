package com.i9930.croptrails.Maps.ShowArea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;
import com.i9930.croptrails.Maps.ShowArea.Model.OmiFetchDatum;
import com.i9930.croptrails.Maps.ShowArea.Model.OmitanceShowResponse;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.SendOmtanceArea;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Test.SatusreModel.BinSet;
import com.i9930.croptrails.Test.SliderAdapter;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import android.Manifest;
import android.app.Activity;
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
import android.graphics.Path;
import android.graphics.Point;
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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Double.parseDouble;
import static com.i9930.croptrails.Maps.WalkAround.MapsActivityNew.getBitmapFromVectorDrawable;

public class ShowAreaOnMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static interface MapConstants {
        int SIMPLE_MODE = -1;
        int NDVI_MODE = 0;
        int NDWI_MODE = 1;
    }

    private GoogleMap mMap;
    Toolbar mActionBarToolbar;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
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
    //Button submit;
    TextView tvarea;
    //Boolean onclick=false;
    // Button butt_clear;
    String mprovider;
    Location location;
    //Button next_butt;
    GifImageView progressBar;
    Boolean can_add = true;
    String[] latAraay;
    String lngArray[];
    final String TAG = "ShowAreaOnMapActivity";
    @BindView(R.id.back_to_farm)
    ImageView back_to_farm;
    TextView title;
    Resources resources;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    TextView area_unit_label;
    double area_mult_factor = 1.0;
    String imageUrl = "https://dashboard-mapimages-prodv010.s3.ap-south-1.amazonaws.com/ndwi/6eCKBKl8VAhctIqtNYstv2ZmVkftEtML7qIyd8B5_mNHEUG3Nm-QWdM=.png";
    Bitmap imageBitmap;

    ViewPager viewPager;
    CircleIndicator circleIndicator;
    int selectedMode = -1;
    SliderAdapter adapter;
    final int delay = 5000;
    final int period = 5000;

    public void slideshowadapter(List<BinSet> binSetList, int selectedMode) {

        if (selectedMode != MapConstants.SIMPLE_MODE) {
            Log.e(TAG, "Binset list length " + binSetList.size());
            circleIndicator.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            adapter = new SliderAdapter(this, binSetList, new SliderAdapter.ItemPositionListener() {
                @Override
                public void onPositionChanged(int position) {
                    Log.e(TAG, "Activity Index " + position);
                    // Toast.makeText(context, "index "+position, Toast.LENGTH_SHORT).show();
                    groundOverlay = fillPoly(mMap.getProjection(), UCCpolygon, binSetList.get(position).getImageBitmap(), 0.5f);
                }
            });

            viewPager.setAdapter(adapter);
            circleIndicator.setViewPager(viewPager);
            final Runnable r = new Runnable() {
                public void run() {
                    new Handler().postDelayed(this, 4000);

                }
            };
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    int i = viewPager.getCurrentItem();
                    if (i == adapter.getCount() - 1)
                        i = 0;
                    else
                        i++;
                    viewPager.setCurrentItem(i, true);
                }
            };
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    handler.post(runnable);
                }
            }, delay, period);


        } else {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            circleIndicator.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            if (groundOverlay != null)
                groundOverlay.remove();

        }

    }

    private void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public synchronized URL getTileUrl(int x, int y, int zoom) {
                // The moon tile coordinate system is reversed.  This is not normal.
                int reversedY = (1 << zoom) - y - 1;
                String s = String.format(Locale.US, imageUrl, zoom, x, reversedY);
                URL url = null;
                try {
                    url = new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
                return url;
            }
        };

        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
    }

    List<BinSet> binSetListNdvi;
    List<BinSet> binSetListNdwi;

    private void initViews() {
        title = (TextView) findViewById(R.id.tittle);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        progressBar = findViewById(R.id.progressBar_cyclic);
        tvarea = (TextView) findViewById(R.id.area_tv);
        area_unit_label = (TextView) findViewById(R.id.acer_label);
        circleIndicator = findViewById(R.id.circleIndicator);
        viewPager = findViewById(R.id.viewPager);

        if (DataHandler.newInstance().getBinSetListNdvi() != null && DataHandler.newInstance().getBinSetListNdvi().size() > 0) {
            binSetListNdvi = DataHandler.newInstance().getBinSetListNdvi();
        }
        if (DataHandler.newInstance().getBinSetListNdwi() != null && DataHandler.newInstance().getBinSetListNdwi().size() > 0) {
            binSetListNdwi = DataHandler.newInstance().getBinSetListNdwi();
        }
        back_to_farm.setImageResource(R.drawable.farm_icon_map);

        //new AsyncGettingBitmapFromUrl().execute();

        loadBitmaps();
    }

    public static Bitmap getBitmapFromURL(String imgUrl) {
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    private class AsyncGettingBitmapFromUrl extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... params) {

            if (binSetListNdvi != null) {
                for (int i = 0; i < binSetListNdvi.size(); i++) {
                    binSetListNdvi.get(i).setImageBitmap(getBitmapFromURL(binSetListNdvi.get(i).getImage()));
                }
            }

            if (binSetListNdwi != null) {
                for (int i = 0; i < binSetListNdwi.size(); i++) {
                    binSetListNdwi.get(i).setImageBitmap(getBitmapFromURL(binSetListNdwi.get(i).getImage()));
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {


            slideshowadapter(null, MapConstants.SIMPLE_MODE);

        }
    }

    private void loadBitmaps() {
        if (binSetListNdvi != null) {
            for (int i = 0; i < binSetListNdvi.size(); i++) {
                int finalI = i;
                Log.e(TAG, "Image NDVI index " + i);
                Glide.with(ShowAreaOnMapActivity.this)
                        .asBitmap()
                        .load(binSetListNdvi.get(i).getImage())
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                binSetListNdvi.get(finalI).setImageBitmap(resource);
                            }
                        });
            }
        }

        if (binSetListNdwi != null) {
            for (int i = 0; i < binSetListNdwi.size(); i++) {
                int finalI = i;
                Log.e(TAG, "Image NDWI index " + i);
                Glide.with(ShowAreaOnMapActivity.this)
                        .asBitmap()
                        .load(binSetListNdwi.get(i).getImage())
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                binSetListNdwi.get(finalI).setImageBitmap(resource);
                            }
                        });
            }
        }
        slideshowadapter(null, MapConstants.SIMPLE_MODE);

    }

    private final Map<String, MarkerOptions> mMarkers = new ConcurrentHashMap<String, MarkerOptions>();

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    ImageView imageView;
    boolean canBeUpdated=false;
    @BindView(R.id.myLocationCard)
    CardView myLocationCard;
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

        myLocationCard.setOnClickListener(new View.OnClickListener() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String languageToLoad = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
        Locale locale1 = new Locale(languageToLoad);
        Locale.setDefault(locale1);
        Configuration config1 = new Configuration();
        config1.locale = locale1;
        getBaseContext().getResources().updateConfiguration(config1,
                getBaseContext().getResources().getDisplayMetrics());
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_area_on_map);
        imageBitmap = DataHandler.newInstance().getFarmBitmap();
        imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(imageBitmap);
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        ButterKnife.bind(this);
        context = this;
        bmp = getBitmapFromVectorDrawable(context, R.drawable.marker_green_24px);
        initViews();
        area_mult_factor = SharedPreferencesMethod.getDoubleSharedPreferences(context, SharedPreferencesMethod.AREA_MULTIPLICATON_FACTOR);
        area_unit_label.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SHARED_PREFERENCE_NAME));
        Intent intent=getIntent();
        if (intent!=null){
            canBeUpdated=intent.getBooleanExtra("canBeUpdated",false);
        }
        title.setText(resources.getString(R.string.farm_area_title));

        mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setFarmerNameAndKhasra();
        String language = "hi"; //Hindi language!.
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            config.setLocale(locale);
            context.createConfigurationContext(config);
        } else { //deprecated
            config.locale = locale;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }
        progressBar.setVisibility(View.VISIBLE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mprovider = locationManager.getBestProvider(criteria, false);
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider, 2000, 1, this);


        }
        latPoints = new Double[100];
        longPoints = new Double[100];

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    public void onMapReady(final GoogleMap googleMap) {
        Polygon UCCpolygon;
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
        asyncTaskRunner.execute();




       /* UCCpolygon = mMap.addPolygon(new PolygonOptions()
                .add(new LatLngFarm(22.686798712822696,75.85943765938282),
                        new LatLngFarm(22.68557683491474,75.85923314094543),
                        new LatLngFarm(22.685512183349857,75.86002070456743))
                .strokeColor(Color.RED)
                .fillColor(Color.parseColor("#330000FF")));

        List<LatLngFarm> latLngs = new ArrayList<>();
        latLngs.add(new LatLngFarm(22.686798712822696,75.85943765938282));
        latLngs.add(new LatLngFarm(22.68557683491474,75.85923314094543));
        latLngs.add(new LatLngFarm(22.685512183349857,75.86002070456743));

        Log.i("AREA", "computeArea " + SphericalUtil.computeArea(latLngs));
        tvarea.setText(String.valueOf(String.format("%.2f", SphericalUtil.computeArea(latLngs)*0.000247105)));
*/


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




                       /* new LatLngFarm(-34.364, 147.891),
                        new LatLngFarm(-33.501, 150.217),
                        new LatLngFarm(-32.306, 149.248),
                        new LatLngFarm(-32.491, 147.309)))*/

       /* mMap.setOnPolylineClickListener(this);
        mMap.setOnPolygonClickListener(this);*/



      /*  mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLngFarm point) {
                // TODO Auto-generated method stub



                      *//* MarkerOptions marker = new MarkerOptions().position(
                               new LatLngFarm(point.latitude, point.longitude)).title("New Marker");

                       LatLngFarm latLng=new LatLngFarm(point.latitude,point.longitude);
                       add("marker"+i,latLng);
                       mMap.addMarker(marker);
                       Log.e("TOUCH Points", point.latitude + "    " + point.longitude);

                       latPoints[i] = point.latitude;
                       longPoints[i] = point.longitude;
                       i++;*//*


                LatLngFarm latLng=new LatLngFarm(point.latitude,point.longitude);
                add("marker"+i,latLng);
                //mMap.addMarker(marker);
                Log.e("TOUCH Points", point.latitude + "    " + point.longitude);

                //  submit_area();



                //new LatLngFarm(28.7041, 77.1025)));
            }
        });*/


        // Add a marker in Sydney and move the camera
        /*LatLngFarm sydney = new LatLngFarm(51.893728, -8.491865);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        AsyncTaskRunnerOmitance asyncTaskRunnerOmitance=new AsyncTaskRunnerOmitance();
        asyncTaskRunnerOmitance.execute();
        AsyncTaskRunnerWayPoint asyncTaskRunnerWayPoint=new AsyncTaskRunnerWayPoint();
        asyncTaskRunnerWayPoint.execute();
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
//                Location newLoc=new Location(location.getProvider());
//                newLoc.setLatitude(21.823293);
//                newLoc.setLongitude(76.352884);
//                Toast.makeText(context, ""+mLastLocation.bearingTo(newLoc), Toast.LENGTH_SHORT).show();
                //Place current location marker
                // LatLngFarm latLng = new LatLngFarm(22.685512183349857, 75.86002070456743 );
                   /* MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Current Position");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    mCurrLocationMarker = mMap.addMarker(markerOptions);*/

                //move map camera
                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                progressBar.setVisibility(View.INVISIBLE);
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
                                ActivityCompat.requestPermissions(ShowAreaOnMapActivity.this,
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
            if (i < 14) {
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
                //  Toast.makeText(context, resources.getString(R.string.cant_add_more_points), Toast.LENGTH_SHORT).show();
                Toasty.error(context, resources.getString(R.string.cant_add_more_points), Toast.LENGTH_SHORT, true).show();

            }
        } else {
            // Toast.makeText(context, resources.getString(R.string.please_clear_then_add_more), Toast.LENGTH_SHORT).show();
            Toasty.error(context, resources.getString(R.string.please_clear_then_add_more), Toast.LENGTH_SHORT, true).show();

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
                .addOnConnectionFailedListener(ShowAreaOnMapActivity.this).build();
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

                            status.startResolutionForResult(ShowAreaOnMapActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
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

    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {
        public AsyncTaskRunner() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {
            List<LatLngFarm> latLngList = DataHandler.newInstance().getLatLngList();
            Log.e(TAG, "Poly " + new Gson().toJson(latLngList));
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            latAraay = new String[latLngList.size()];
            lngArray = new String[latLngList.size()];
            final int sizeofgps = latLngList.size();
            for (int i = 0; i < latLngList.size(); i++) {
                latAraay[i] = latLngList.get(i).getLat();
                lngArray[i] = latLngList.get(i).getLng();
                LatLng latlngforbound = new LatLng(Double.parseDouble(latAraay[i]), Double.parseDouble(lngArray[i]));
                builder.include(latlngforbound);

            }

            final LatLng latLng = new LatLng(Double.valueOf(latAraay[0]), Double.valueOf(lngArray[0]));

            ShowAreaOnMapActivity.this.runOnUiThread(new Runnable() {

                public void run() {

                    /*the code you want to run after the background operation otherwise they will executed earlier and give you an error*/
                    // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                    showArea(sizeofgps);

                    back_to_farm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //showDialog();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                            //  mMap.setLatLngBoundsForCameraTarget();
                        }
                    });
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


    private void setUpData(List<LatLng> polygonPoints, Polygon polygon) {
        LatLngBounds bounds = getPolygonBounds(polygon.getPoints());

        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(DataHandler.newInstance().getFarmBitmap());

        double boundHeight = bounds.northeast.latitude - bounds.southwest.latitude;
        double boundWidth = bounds.northeast.longitude - bounds.southwest.longitude;

        double gridCntLat = 5;  // 5 icons vertically
        double gridCntLon = 5;  // 5 icons horizontally

        double dLat = (bounds.northeast.latitude - bounds.southwest.latitude) / gridCntLat;
        double dLng = (bounds.northeast.longitude - bounds.southwest.longitude) / gridCntLon;

        double lat = dLat + bounds.southwest.latitude;

        while (lat < bounds.northeast.latitude) {
            double lon = dLng + bounds.southwest.longitude;
            while (lon < bounds.northeast.longitude) {

                LatLng iconPos = new LatLng(lat, lon);

                if (PolyUtil.containsLocation(iconPos, polygonPoints, true)) {
                    MarkerOptions markerOptions = new MarkerOptions().position(iconPos)
                            .draggable(false)
                            .flat(true)
                            .icon(icon);
                    mMap.addMarker(markerOptions);
                }
                lon += dLng;
            }
            lat += dLat;
        }
    }

    private GroundOverlay fillPoly(Projection projection, Polygon polygon, Bitmap tileBitmap, float transparency) {
        if (selectedMode != MapConstants.SIMPLE_MODE) {
            if (groundOverlay != null)
                groundOverlay.remove();
            LatLngBounds bounds = getPolygonBounds(polygon.getPoints());
            double boundHeight = bounds.northeast.latitude - bounds.southwest.latitude;
            double boundWidth = bounds.northeast.longitude - bounds.southwest.longitude;
            if (tileBitmap == null && options != null) {

                options.fillColor(Color.parseColor("#1A00aaff"));
            }
            Point northEast = projection.toScreenLocation(bounds.northeast);
            Point southWest = projection.toScreenLocation(bounds.southwest);

            int screenBoundHeight = northEast.y - southWest.y;
            int screenBoundWidth = northEast.x - southWest.x;

            // determine overlay bitmap size
            double k = Math.abs((double) screenBoundHeight / (double) screenBoundWidth);
            Log.e(TAG, "screenBoundHeight " + screenBoundHeight);
            Log.e(TAG, "screenBoundWidth " + screenBoundWidth);
            Log.e(TAG, "K " + k);
            int overlayWidth = 0;
            int overlayHeight = 0;
            if (Math.abs(boundWidth) > Math.abs(boundHeight)) {
                overlayWidth = 200;
                overlayHeight = (int) (overlayWidth * k);
            } else {
                overlayHeight = 200;
                overlayWidth = (int) (overlayHeight * k);
            }
            Log.e(TAG, "overlayWidth " + overlayWidth);
            Log.e(TAG, "overlayHeight " + overlayHeight);
            if (overlayHeight < 1)
                overlayHeight = 200;
            if (overlayWidth < 1)
                overlayWidth = 200;
            // create overlay bitmap

            Bitmap overlayBitmap = createOverlayBitmap(overlayWidth, overlayHeight, bounds, polygon.getPoints(), tileBitmap);

            // create ground overlay
            GroundOverlayOptions overlayOptions = new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromBitmap(overlayBitmap))
                    .transparency(transparency)
                    .anchor(.5f, .5f)
                    .positionFromBounds(bounds);

            return mMap.addGroundOverlay(overlayOptions);
        } else return null;
    }

    private Bitmap createOverlayBitmap(int width, int height, LatLngBounds bounds, List<LatLng> polygon, Bitmap tileBitmap) {
        Bitmap bitmap = tileBitmap;
        Canvas canvas = new Canvas(bitmap);

        Path path = new Path();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        List<Point> screenPoints = new ArrayList<>(polygon.size());

        double boundHeight = bounds.northeast.latitude - bounds.southwest.latitude;
        double boundWidth = bounds.northeast.longitude - bounds.southwest.longitude;

        for (int i = 0; i < polygon.size(); i++) {
            LatLng latLng = polygon.get(i);
            Point screenPoint = new Point();
            screenPoint.x = (int) (width * (latLng.longitude - bounds.southwest.longitude) / boundWidth);
            screenPoint.y = height - (int) (height * (latLng.latitude - bounds.southwest.latitude) / boundHeight);
            screenPoints.add(screenPoint);
        }

        path.moveTo(screenPoints.get(0).x, screenPoints.get(0).y);
        for (int i = 1; i < polygon.size(); i++) {
            path.lineTo(screenPoints.get(i).x, screenPoints.get(i).y);
        }
        path.close();

        canvas.drawPath(path, paint);

        return bitmap;
    }

    private LatLngBounds getPolygonBounds(List<LatLng> polygon) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < polygon.size(); i++) {
            builder.include(polygon.get(i));
        }
        LatLngBounds bounds = builder.build();
        return bounds;
    }

    GroundOverlay groundOverlay;
    Polygon UCCpolygon;
    PolygonOptions options;

    private void showArea(int i) {

        if (i < 3) {
            //Toast.makeText(context, "Please submit atleast 3 points", Toast.LENGTH_SHORT).show();
            ViewFailDialog viewFailDialog = new ViewFailDialog();
            viewFailDialog.showDialog(ShowAreaOnMapActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.should_have_at_least_two_points_to_display));
        } else {
            LatLng[] latLng = new LatLng[i];
            List<LatLng> latLngs = new ArrayList<>();
            latLngs.clear();
            for (int j = 0; j < i; j++) {
                latLng[j] = new LatLng(Double.valueOf(latAraay[j]), Double.valueOf(lngArray[j]));
                latLngs.add(new LatLng(Double.valueOf(latAraay[j]), Double.valueOf(lngArray[j])));
            }
            Log.e(TAG, "computeArea " + SphericalUtil.computeArea(latLngs));
            Log.i(TAG, "AREA " + SphericalUtil.computeArea(latLngs));
            String a = String.valueOf(String.format("%.4f", SphericalUtil.computeArea(latLngs) * 0.000247105));
            Log.e(TAG, "Area in Acres " + a);
            Log.e(TAG, "Multiplication Factor " + area_mult_factor);
            String ar = AppConstants.getShowableAreaWithConversion(a, area_mult_factor);
            tvarea.setText(ar + " " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL));
            options = new PolygonOptions()
                    .addAll(latLngs)
                    .strokeColor(Color.BLUE)
                    .strokeWidth(2f);
            UCCpolygon = mMap.addPolygon(options);
            Bitmap tileBitmap = DataHandler.newInstance().getFarmBitmap();   // create bitmap of fill icon
            groundOverlay = fillPoly(mMap.getProjection(), UCCpolygon, null, 0.5f);


        }
    }

    private String convertAreaTo(String area) {
        String convertArea = area;
        convertArea = String.format("%.2f", parseDouble(area) * area_mult_factor);
        return convertArea;
    }


    private class AsyncTaskRunnerOmitance extends AsyncTask<String, Void, String> {

        public AsyncTaskRunnerOmitance() {
            super();
        }
        @Override
        protected String doInBackground(String... params) {

            SendOmtanceArea sendWayPointData=new SendOmtanceArea(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID),AppConstants.GPS3_TYPE.OMITANCE_AREA,null);
            try {
                Log.e(TAG,"SEND OMITANCE "+new Gson().toJson(sendWayPointData));
                ApiInterface apiService= RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
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
                                viewFailDialog.showSessionExpireDialog(ShowAreaOnMapActivity.this, response.body().getMsg());
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(ShowAreaOnMapActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(ShowAreaOnMapActivity.this, resources.getString(R.string.authorization_expired));
                            } else if (statusMsgModel.getStatus() == 1) {

                                if (statusMsgModel.getData()!=null&&statusMsgModel.getData().size()>0) {
                                    for (int i=0;i<statusMsgModel.getData().size();i++){
                                        OmiFetchDatum area=statusMsgModel.getData().get(i);
                                        if (area.getDetails()!=null&&area.getDetails().getLatLngList()!=null&&area.getDetails().getLatLngList().size()>2){
                                            List<LatLng> latLngs=new ArrayList<>();
                                            for (int j=0;j<area.getDetails().getLatLngList().size();j++){
                                                latLngs.add(new LatLng(Double.valueOf(area.getDetails().getLatLngList().get(j).getLatitude()),
                                                        Double.valueOf(area.getDetails().getLatLngList().get(j).getLongitude())));
                                            }

                                            Log.e(TAG, "OMITANCE latLngs " + new Gson().toJson(latLngs));
                                            showPolygons(area.getName(),latLngs);
                                        }
                                    }
                                }

                            } else {

                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(ShowAreaOnMapActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(ShowAreaOnMapActivity.this, resources.getString(R.string.authorization_expired));
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
                            notifyApiDelay(ShowAreaOnMapActivity.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }*/
                    }

                    @Override
                    public void onFailure(Call<OmitanceShowResponse> call, Throwable t) {

                       /* long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        notifyApiDelay(ShowAreaOnMapActivity.this, call.request().url().toString(),
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

            SendOmtanceArea sendWayPointData=new SendOmtanceArea(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID),AppConstants.GPS3_TYPE.WAY_POINT,null);
            try {
                Log.e(TAG,"SEND OMITANCE Way"+new Gson().toJson(sendWayPointData));
                ApiInterface apiService= RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
                Call<ResponseBody> statusMsgModelCall = apiService.getWayPoints2(sendWayPointData);
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String error = null;
                        isLoaded[0] = true;
                        Log.e(TAG, "Status OMITANCE way " + response.code());
                        if (response.isSuccessful()) {
                            try {
                                String json=response.body().string();
                                Log.e(TAG, "OMITANCE res way " + json);
//                                json="{\"status\":1,\"msg\":\"success\",\"data\":[{\"id\":\"11\",\"farm_id\":\"2903\",\"comp_id\":\"100130\",\"region_id\":null,\"details\":{\"latitude\":23.968942366591712,\"longitude\":76.82759441435337},\"type\":\"W\",\"name\":\"OMS Location No 10\",\"doa\":\"2020-10-02 06:34:27\",\"is_active\":\"Y\"}]}";
                                JSONObject jsonObject =new JSONObject(json);
                                try {

                                    if (jsonObject!=null&&jsonObject.has("data")) {
                                        JSONArray array = jsonObject.getJSONArray("data");
                                        if (array != null) {
                                            for (int i = 0; i < array.length(); i++) {
                                                JSONObject object = array.getJSONObject(i);
                                                String name = null;
                                                name = object.getString("name");
                                                Log.e(TAG, "Name " + name);
                                                if (name != null && !TextUtils.isEmpty(name)&&!name.toLowerCase().equals("null")) {
                                                    try {
                                                        JSONObject object1 = object.getJSONObject("details");
                                                        Double lat = object1.getDouble("latitude");
                                                        Double lon = object1.getDouble("longitude");
                                                        showWayPoints(name, new LatLng(lat, lon));
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        try {
                                                            JSONArray jsonArray = object.getJSONArray("details");
                                                            if (jsonArray != null && jsonArray.length() > 0) {
                                                                for (int j = 0; j < jsonArray.length(); j++) {
                                                                    JSONObject latLang = jsonArray.getJSONObject(j);
                                                                    String name1 = latLang.getString("name");
                                                                    JSONObject latlang2 = latLang.getJSONObject("point");
                                                                    Double lat = latlang2.getDouble("latitude");
                                                                    Double lon = latlang2.getDouble("longitude");
                                                                    showWayPoints(name1, new LatLng(lat, lon));
                                                                }
                                                            }
                                                        } catch (Exception e1) {
                                                            e1.printStackTrace();
                                                        }
                                                    }
                                                } else {
                                                    try {
                                                        JSONArray jsonArray = object.getJSONArray("details");
                                                        if (jsonArray != null && jsonArray.length() > 0) {
                                                            for (int j = 0; j < jsonArray.length(); j++) {
                                                                JSONObject latLang = jsonArray.getJSONObject(j);
                                                                String name1 = latLang.getString("name");
                                                                JSONObject latlang2 = latLang.getJSONObject("point");
                                                                Double lat = latlang2.getDouble("latitude");
                                                                Double lon = latlang2.getDouble("longitude");
                                                                showWayPoints(name1, new LatLng(lat, lon));
                                                            }
                                                        }
                                                    } catch (Exception e1) {
                                                        e1.printStackTrace();
                                                    }
                                                }
                                            }
                                        }


                                    }
                                    System.out.println(TAG+" "+jsonObject);

                                } catch (Exception e ) {
                                    e.printStackTrace();
                                }

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                            /*WayPointFetchResponse statusMsgModel = response.body();
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(ShowAreaOnMapActivity.this, response.body().getMsg());
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(ShowAreaOnMapActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(ShowAreaOnMapActivity.this, resources.getString(R.string.authorization_expired));
                            } else if (statusMsgModel.getStatus() == 1) {
                                if (statusMsgModel.getData()!=null&&statusMsgModel.getData().size()>0){
                                    for (int i=0;i<statusMsgModel.getData().size();i++){
                                        WayPointFetchDatum datum=statusMsgModel.getData().get(i);
                                        if (datum.getDetails()!=null&&datum.getDetails().getLatitude()!=null&&datum.getDetails().getLongitude()!=null){
                                            showWayPoints(datum.getName(),new LatLng(datum.getDetails().getLatitude(),datum.getDetails().getLongitude()));
                                        }
                                    }
                                }

                            } else {


                            }*/
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(ShowAreaOnMapActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(ShowAreaOnMapActivity.this, resources.getString(R.string.authorization_expired));
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
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

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


    private void showPolygons(String name,List<LatLng>latLngList){
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
    private void showWayPoints(String name,LatLng latLngList){
        Log.e(TAG,"WayPoint name "+name+" point: "+new Gson().toJson(latLngList));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final MarkerOptions marker = new MarkerOptions().position(latLngList)
                            .title(name)
                            .icon(BitmapDescriptorFactory.fromBitmap(bmp));
                    mMap.addMarker(marker);
                }catch (Exception e){

                }
            }
        });
    }
}
