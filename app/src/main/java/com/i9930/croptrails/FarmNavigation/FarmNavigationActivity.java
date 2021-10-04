package com.i9930.croptrails.FarmNavigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;
import com.i9930.croptrails.Maps.ShowArea.Model.OmiFetchDatum;
import com.i9930.croptrails.Maps.ShowArea.Model.OmitanceShowResponse;
import com.i9930.croptrails.Maps.ShowArea.Model.geojson.GeoJsonResponse;
import com.i9930.croptrails.Maps.ShowArea.ShowAreaWithUpdateActivity;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.SendOmtanceArea;
import com.i9930.croptrails.Maps.WalkAround.Compass;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Maps.WalkAround.MapsActivityNew.getBitmapFromVectorDrawable;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;
import static com.i9930.croptrails.Utility.AppConstants.getCoordinatesFromGeometry;


public class FarmNavigationActivity extends FragmentActivity implements SensorEventListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private Compass compass;
    private float currentAzimuth;

    private void setupCompass() {
        try {
            compass = new Compass(this);
            Compass.CompassListener cl = new Compass.CompassListener() {
                @Override
                public void onNewAzimuth(float azimuth, float[] mGravity) {
//                    currentAzimuth = azimuth;
//                    currentDegree = Math.abs(azimuth);
                    adjustArrow(azimuth);
//                    gravity = mGravity;
//                    Log.e(TAG, "Compass " + azimuth + " gravity " + mGravity.toString());
                }
            };
            compass.setListener(cl);
        } catch (Exception e) {

        }
    }

    int lastAnimation = 0;

    private void adjustArrow(float azimuth) {
        /*Log.d(TAG, "will set rotation from " + currentAzimuth + " to "
                + azimuth);*/
        String cardDirect = "";
        try {
            currentAzimuth = azimuth;
            String display = (int) currentAzimuth + "";
            if (currentAzimuth == 0 || currentAzimuth == 360) {
                cardDirect = "N";
//                if (lastAnimation != 1)
//                    hideImages(1);
//                lastAnimation = 1;
            } else if (currentAzimuth > 0 && currentAzimuth < 90) {
                cardDirect = "NE";
//                if (lastAnimation != 2)
//                    hideImages(2);
//                lastAnimation = 2;
            } else if (currentAzimuth == 90) {
                cardDirect = "E";
//                if (lastAnimation != 3)
//                    hideImages(3);
//                lastAnimation = 3;
            } else if (currentAzimuth > 90 && currentAzimuth < 180) {
                cardDirect = "SE";
//                if (lastAnimation != 4)
//                    hideImages(4);
//                lastAnimation = 4;
            } else if (currentAzimuth == 180) {
//                if (lastAnimation != 5)
//                    hideImages(5);
//                lastAnimation = 5;
                cardDirect = "S";

            } else if (currentAzimuth > 180 && currentAzimuth < 270) {
                cardDirect = "SW";
//                if (lastAnimation != 6)
//                    hideImages(6);
//                lastAnimation = 6;
            } else if (currentAzimuth == 270) {
                cardDirect = "W";
//                if (lastAnimation != 7)
//                    hideImages(7);
//                lastAnimation = 7;
            } else if (currentAzimuth > 270 && currentAzimuth < 360) {
                cardDirect = "NW";
//                if (lastAnimation != 8)
//                    hideImages(8);
//                lastAnimation = 8;
            } else
                cardDirect = "Unknown";

        } catch (Exception e) {
        }
        /*imageGif1.setVisibility(View.VISIBLE);
        RotateAnimation ra = new RotateAnimation(currentDegree, -Math.abs(azimuth), Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(150);
        ra.setFillAfter(true);
        imageGif1.startAnimation(ra);*/
        currentDegree = Math.abs(azimuth);
        int rotation = (int) currentDegree;
        tvHeading.setText("Heading: " + Float.toString(rotation) + " " + cardDirect);

    }

    private void adjustArrow2(float azimuth) {

        String cardDirect = "";
        try {
            float currentAzimuth = azimuth;
            if (currentAzimuth == 0 || currentAzimuth == 360||currentAzimuth>345||currentAzimuth<26) {
                cardDirect = "N";
//                if (lastAnimation != 1)
                hideImages(1);
                lastAnimation = 1;
            } else if (currentAzimuth > 0 && currentAzimuth < 90) {
                cardDirect = "NE";
//                if (lastAnimation != 2)
                hideImages(2);
                lastAnimation = 2;
            } else if (currentAzimuth == 90) {
                cardDirect = "E";
//                if (lastAnimation != 3)
                hideImages(3);
                lastAnimation = 3;
            } else if (currentAzimuth > 90 && currentAzimuth < 180) {
                cardDirect = "SE";
//                if (lastAnimation != 4)
                hideImages(4);
                lastAnimation = 4;
            } else if (currentAzimuth == 180) {
//                if (lastAnimation != 5)
                hideImages(5);
                lastAnimation = 5;
                cardDirect = "S";

            } else if (currentAzimuth > 180 && currentAzimuth < 270) {
                cardDirect = "SW";
//                if (lastAnimation != 6)
                hideImages(6);
                lastAnimation = 6;
            } else if (currentAzimuth == 270) {
                cardDirect = "W";
//                if (lastAnimation != 7)
                hideImages(7);
                lastAnimation = 7;
            } else if (currentAzimuth > 270 && currentAzimuth < 360) {
                cardDirect = "NW";
//                if (lastAnimation != 8)
                hideImages(8);
                lastAnimation = 8;
            } else
                cardDirect = "Unknown";

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void hideImages(int img) {
        if (img == 1) {
            //N
            imageNorth.setVisibility(View.VISIBLE);
            imageEast.setVisibility(View.INVISIBLE);
            imageNorthEast.setVisibility(View.INVISIBLE);
            imageSouthEast.setVisibility(View.INVISIBLE);
            imageSouth.setVisibility(View.INVISIBLE);
            imageSouthWest.setVisibility(View.INVISIBLE);
            imageWest.setVisibility(View.INVISIBLE);
            imageNorthWest.setVisibility(View.INVISIBLE);
            fadeInAnim(imageNorth);
        } else if (img == 2) {
            //NE
            imageNorthEast.setVisibility(View.VISIBLE);
            imageEast.setVisibility(View.INVISIBLE);
            imageNorth.setVisibility(View.INVISIBLE);
            imageSouthEast.setVisibility(View.INVISIBLE);
            imageSouth.setVisibility(View.INVISIBLE);
            imageSouthWest.setVisibility(View.INVISIBLE);
            imageWest.setVisibility(View.INVISIBLE);
            imageNorthWest.setVisibility(View.INVISIBLE);
            fadeInAnim(imageNorthEast);
        } else if (img == 3) {
            //E
            imageEast.setVisibility(View.VISIBLE);
            imageNorth.setVisibility(View.INVISIBLE);
            imageNorthEast.setVisibility(View.INVISIBLE);
            imageSouthEast.setVisibility(View.INVISIBLE);
            imageSouth.setVisibility(View.INVISIBLE);
            imageSouthWest.setVisibility(View.INVISIBLE);
            imageWest.setVisibility(View.INVISIBLE);
            imageNorthWest.setVisibility(View.INVISIBLE);
            fadeInAnim(imageEast);
        } else if (img == 4) {
            //SE
            imageSouthEast.setVisibility(View.VISIBLE);
            imageEast.setVisibility(View.INVISIBLE);
            imageNorthEast.setVisibility(View.INVISIBLE);
            imageNorth.setVisibility(View.INVISIBLE);
            imageSouth.setVisibility(View.INVISIBLE);
            imageSouthWest.setVisibility(View.INVISIBLE);
            imageWest.setVisibility(View.INVISIBLE);
            imageNorthWest.setVisibility(View.INVISIBLE);
            fadeInAnim(imageSouthEast);
        } else if (img == 5) {
            //S
            imageSouth.setVisibility(View.VISIBLE);
            imageEast.setVisibility(View.INVISIBLE);
            imageNorthEast.setVisibility(View.INVISIBLE);
            imageSouthEast.setVisibility(View.INVISIBLE);
            imageNorth.setVisibility(View.INVISIBLE);
            imageSouthWest.setVisibility(View.INVISIBLE);
            imageWest.setVisibility(View.INVISIBLE);
            imageNorthWest.setVisibility(View.INVISIBLE);
            fadeInAnim(imageSouth);
        } else if (img == 6) {
            //SW
            imageSouthWest.setVisibility(View.VISIBLE);
            imageEast.setVisibility(View.INVISIBLE);
            imageNorthEast.setVisibility(View.INVISIBLE);
            imageSouthEast.setVisibility(View.INVISIBLE);
            imageSouth.setVisibility(View.INVISIBLE);
            imageNorth.setVisibility(View.INVISIBLE);
            imageWest.setVisibility(View.INVISIBLE);
            imageNorthWest.setVisibility(View.INVISIBLE);
            fadeInAnim(imageSouthWest);
        } else if (img == 7) {
            //W
            imageWest.setVisibility(View.VISIBLE);
            imageEast.setVisibility(View.INVISIBLE);
            imageNorthEast.setVisibility(View.INVISIBLE);
            imageSouthEast.setVisibility(View.INVISIBLE);
            imageSouth.setVisibility(View.INVISIBLE);
            imageSouthWest.setVisibility(View.INVISIBLE);
            imageNorth.setVisibility(View.INVISIBLE);
            imageNorthWest.setVisibility(View.INVISIBLE);
            fadeInAnim(imageWest);
        } else if (img == 8) {
            //NW
            imageNorthWest.setVisibility(View.VISIBLE);
            imageEast.setVisibility(View.INVISIBLE);
            imageNorthEast.setVisibility(View.INVISIBLE);
            imageSouthEast.setVisibility(View.INVISIBLE);
            imageSouth.setVisibility(View.INVISIBLE);
            imageSouthWest.setVisibility(View.INVISIBLE);
            imageWest.setVisibility(View.INVISIBLE);
            imageNorth.setVisibility(View.INVISIBLE);
            fadeInAnim(imageNorthWest);


        }
    }

    private void fadeInAnim(ImageView image) {

        /*AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(2500);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        image.startAnimation(alphaAnimation);
        */
//        fadeOutAnim(image);
    }

    private void fadeOutAnim(ImageView image) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(image, "alpha", 1f, 0);
        fadeOut.setDuration(3000);
        fadeOut.start();
//        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
//        image.startAnimation(aniFade);
//        image.setVisibility(View.GONE);
    }

    float lastDegree = 0.0f;
    Timer _timer;
    float lastBearing = 0f;

    private void adjustArrow2(float azimuth, String s, LatLng latLng, float currentDegree, float bearing) {
        try {
            float currentAzimuth = azimuth;
            tvHeadingMove.setText(tvHeadingMove.getText().toString() + ",,," + s);
            if (currentLocationMarker != null)
                currentLocationMarker.remove();
            MarkerOptions marker = new MarkerOptions().position(latLng);
            BitmapDescriptor descriptor = null;
            String title = "";

            if (Math.abs(currentAzimuth) <= 25) {
                descriptor = BitmapDescriptorFactory.fromBitmap(bmp2);
                title = "Go straight";
                updateCameraBearingFarm(bearing, latLng);
            } else if (Math.abs(currentDegree) <= 180) {
                isFarmFacing = false;
                descriptor = BitmapDescriptorFactory.fromBitmap(bmp3);
                title = "Turn right";
                updateCameraBearing(currentDegree, latLng);
            } else {
                isFarmFacing = false;
                descriptor = BitmapDescriptorFactory.fromBitmap(bmp4);
                title = "Turn left";
                updateCameraBearing(currentDegree, latLng);
            }
            marker.icon(descriptor);
            marker.title(title);
//            marker.flat(true);
            currentLocationMarker = mMap.addMarker(marker);

//            imageGif1.setVisibility(View.VISIBLE);
//            RotateAnimation ra = new RotateAnimation(lastBearing, -Math.abs(azimuth), Animation.RELATIVE_TO_SELF, 0.5f,
//                    Animation.RELATIVE_TO_SELF, 0.5f);
//            lastBearing = currentAzimuth;
//            ra.setDuration(150);
//            ra.setFillAfter(true);
//            imageGif1.startAnimation(ra);

            float heading = bearing - (bearing + currentAzimuth);
            Log.e(TAG, "bearing head " + heading
                    + " and h " + (-Math.abs(heading) + 360)+
                    ", and b " +(-Math.abs(bearing) + 360)
                    +", and round"+Math.round(-heading / 360 + 180));
//            adjustArrow2((-Math.abs(bearing) + 360));
            adjustArrow2(heading);
        } catch (Exception e) {
            e.printStackTrace();
        }

        lastDegree = currentDegree;
    }


    boolean isAlreadyMovedCamera = false;

    private void updateCameraBearing(float bearing, LatLng latLng) {
        if (mMap == null) return;
        float bear=0.0f;
        if (bearing>180)
            bear=-(360-Math.abs(bearing));
        else
            bear=bearing;
        CameraPosition camPos = CameraPosition.builder(mMap.getCameraPosition())
                .target(latLng)
                .bearing(bear)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
    }

    private void updateCameraBearingFarm(float bearing, LatLng latLng) {
        if (mMap == null) return;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(farmLat, farmLong));
        builder.include(latLng);

        if (latAraay != null && lngArray != null) {
            for (int i = 0; i < latAraay.length; i++) {
                try {
                    builder.include(new LatLng(Double.valueOf(latAraay[i]), Double.valueOf(lngArray[i])));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        LatLngBounds bounds = builder.build();
        zoomToBounds(bounds, bearing);

    }

    boolean isFarmFacing = false;

    public void zoomToBounds(LatLngBounds bounds, float bear) {
        if (!isFarmFacing) {
            LatLngBounds latLngBounds = bounds;
            final int PADDING = 20;
            final CameraPosition now = mMap.getCameraPosition();
            final float tilt = mMap.getCameraPosition().tilt;
            final float bearing = bear;
            final LatLng[] target = new LatLng[1];
            final float[] zoom = new float[1];

            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, PADDING), 1, null);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    target[0] = mMap.getCameraPosition().target;
                    zoom[0] = mMap.getCameraPosition().zoom - 0.5f;
                }
            }, 30);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(now), 1, null);
                }
            }, 50);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(target[0], zoom[0], tilt, bearing)));
                }
            }, 70);
        }
        isFarmFacing = true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // get the angle around the z-axis rotated
       /* float degree = Math.round(event.values[0]);

        tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");

        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

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
    Location mLastLocationFarm;
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
    final String TAG = "FarmNavigationActivity";
    @BindView(R.id.back_to_farm)
    ImageView back_to_farm;
    TextView title;
    Resources resources;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    TextView area_unit_label;
    double area_mult_factor = 1.0;
    Bitmap imageBitmap;

    int selectedMode = -1;
    double farmLat = 0.0;
    double farmLong = 0.0;

    private void initViews() {
        title = (TextView) findViewById(R.id.tittle);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        progressBar = findViewById(R.id.progressBar_cyclic);
        tvarea = (TextView) findViewById(R.id.area_tv);
        area_unit_label = (TextView) findViewById(R.id.acer_label);
        back_to_farm.setImageResource(R.drawable.farm_icon_map);
        image = (ImageView) findViewById(R.id.imageViewCompass);
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        try {
            String lat = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARM_LAT);
            String lon = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARM_LONG);
            farmLat = Double.valueOf(lat);
            farmLong = Double.valueOf(lon);
        } catch (Exception e) {
            e.printStackTrace();
            farmLat = 0.0;
            farmLong = 0.0;
        }

    }

    private final Map<String, MarkerOptions> mMarkers = new ConcurrentHashMap<String, MarkerOptions>();

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    boolean canBeUpdated = false;

    @BindView(R.id.image1)
    GifImageView imageNorth;
    @BindView(R.id.image2)
    GifImageView imageNorthEast;
    @BindView(R.id.image3)
    GifImageView imageEast;
    @BindView(R.id.image4)
    GifImageView imageSouthEast;
    @BindView(R.id.image5)
    GifImageView imageSouth;
    @BindView(R.id.image6)
    GifImageView imageSouthWest;
    @BindView(R.id.image7)
    GifImageView imageWest;
    @BindView(R.id.image8)
    GifImageView imageNorthWest;

    @BindView(R.id.imageGif1)
    ImageView imageGif1;
    @BindView(R.id.googleMapTv)
    TextView googleMapTv;

    int locationCount = 0;
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
                Log.e(TAG,"Add Errro "+adError.toString());
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
        setContentView(R.layout.activity_farm_navigation);
        imageBitmap = DataHandler.newInstance().getFarmBitmap();
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        ButterKnife.bind(this);
        loadAds();
        context = this;
        bmp = getBitmapFromVectorDrawable(context, R.drawable.marker_green_24px);
        bmp2 = getBitmapFromVectorDrawable(context, R.drawable.ic_iconfinder_arrow);
        bmp3 = getBitmapFromVectorDrawable(context, R.drawable.ic_iconfinder_turn_right);
        bmp4 = getBitmapFromVectorDrawable(context, R.drawable.ic_iconfinder_turn_left);
        initViews();
        area_mult_factor = SharedPreferencesMethod.getDoubleSharedPreferences(context, SharedPreferencesMethod.AREA_MULTIPLICATON_FACTOR);
        area_unit_label.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SHARED_PREFERENCE_NAME));
        Intent intent = getIntent();
        if (intent != null) {
            canBeUpdated = intent.getBooleanExtra("canBeUpdated", false);
        }
        title.setText(resources.getString(R.string.direction));
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

        setupCompass();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
        if (compass != null)
            compass.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (compass != null)
            compass.start();
    }

    Marker currentLocationMarker;

    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000); // two minute interval
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        MarkerOptions marker = new MarkerOptions().position(new LatLng(farmLat, farmLong)).title(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOT_NO)).flat(true);
        mMap.addMarker(marker);
        new GetGeoJsonData().execute();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setMyLocationEnabled(true);
        }
//        AsyncTaskRunnerOmitance asyncTaskRunnerOmitance = new AsyncTaskRunnerOmitance();
//        asyncTaskRunnerOmitance.execute();
//        AsyncTaskRunnerWayPoint asyncTaskRunnerWayPoint = new AsyncTaskRunnerWayPoint();
//        asyncTaskRunnerWayPoint.execute();


    }

    private void prepareData( List<LatLng> latLngs) {

        latLngList.clear();
        latLngList.addAll(latLngs);
        DataHandler.newInstance().setLatLngsFarm(latLngList);
        i = latLngList.size();

        AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
        asyncTaskRunner.execute();

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
//                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    List<LatLng> latLngList = new ArrayList<>();
                                    if (feature.hasGeometry()) {
                                        Geometry geometry = feature.getGeometry();

                                        latLngList.addAll(getCoordinatesFromGeometry(geometry));
                                        for (LatLng latLngg : latLngList) {
//                                            builder.include(latLngg);
                                        }
                                    }
                                    prepareData(latLngList);
//                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
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
                            viewFailDialog.showSessionExpireDialog(FarmNavigationActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(FarmNavigationActivity.this, resources.getString(R.string.authorization_expired));
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
                        viewFailDialog.showSessionExpireDialog(FarmNavigationActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(FarmNavigationActivity.this, resources.getString(R.string.authorization_expired));
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


                }

                @Override
                public void onFailure(Call<GeoJsonResponse> call, Throwable t) {
                    Log.e(TAG, "GetGeoJsonData Failure " + t.toString());
                    isLoaded[0] = true;

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

    private ImageView image;

    private float currentDegree = 0f;

    private SensorManager mSensorManager;

    TextView tvHeading;
    @BindView(R.id.tvHeadingMove)
    TextView tvHeadingMove;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                locationCount++;
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i(TAG, "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                final LatLng farmLatLng = new LatLng(farmLat, farmLong);
                if (UCCpolygon != null) {
                    if (PolyUtil.containsLocation(latLng, UCCpolygon.getPoints(), false)) {
                        Toast.makeText(context, "inside", Toast.LENGTH_SHORT).show();
                    } else {

                    }
                } else {

                }
                mLastLocationFarm = new Location(location.getProvider());
                mLastLocationFarm.setLatitude(farmLat);
                mLastLocationFarm.setLongitude(farmLong);
                double distance = AppConstants.meterDistanceBetweenPoints(farmLat, farmLong, location.getLatitude(), location.getLongitude());

                if (locationCount > 4 && distance > 1000) {
                    Toasty.info(context, "Farm location is much far from your current location", Toast.LENGTH_SHORT, false).show();
                    googleMapTv.setVisibility(View.VISIBLE);
                    googleMapTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            oprnGoogleMap(latLng, farmLatLng);
                        }
                    });
                } else {
                    googleMapTv.setVisibility(View.GONE);
                }

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(latLng);
                builder.include(farmLatLng);
                LatLngBounds bounds = builder.build();
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * .15);
                float f1 = mLastLocation.bearingTo(mLastLocationFarm);
                float f = currentDegree - f1;
                float f2 = getRotationAngle(latLng, new LatLng(farmLat, farmLong));
                String s = "" + "Turn: " + f + " degree" + ",\n current: " + currentDegree + ", \nbearingTo:" + f1 + ", \n rotation:" + f2 + ", distance:" + distance;
                tvHeadingMove.setText("");
                if (!isAlreadyMovedCamera) {
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height / 2, padding);
                    mMap.moveCamera(cu);
                    isAlreadyMovedCamera = true;
                }
                adjustArrow2(f, s, latLng, currentDegree, f1);
                Log.e(TAG, s);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    };

    private void oprnGoogleMap(LatLng current, LatLng farm) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + current.latitude + "," + current.longitude + "&daddr=" + farm.latitude + "," + farm.longitude));
        startActivity(intent);
    }

    private static final double LN2 = 0.6931471805599453;
    private static final int WORLD_PX_HEIGHT = 256;
    private static final int WORLD_PX_WIDTH = 256;
    private static final int ZOOM_MAX = 21;

    public int getBoundsZoomLevel(LatLngBounds bounds, int mapWidthPx, int mapHeightPx) {

        LatLng ne = bounds.northeast;
        LatLng sw = bounds.southwest;

        double latFraction = (latRad(ne.latitude) - latRad(sw.latitude)) / Math.PI;

        double lngDiff = ne.longitude - sw.longitude;
        double lngFraction = ((lngDiff < 0) ? (lngDiff + 360) : lngDiff) / 360;

        double latZoom = zoom(mapHeightPx, WORLD_PX_HEIGHT, latFraction);
        double lngZoom = zoom(mapWidthPx, WORLD_PX_WIDTH, lngFraction);

        int result = Math.min((int) latZoom, (int) lngZoom);
        return Math.min(result, ZOOM_MAX);
    }

    private double latRad(double lat) {
        double sin = Math.sin(lat * Math.PI / 180);
        double radX2 = Math.log((1 + sin) / (1 - sin)) / 2;
        return Math.max(Math.min(radX2, Math.PI), -Math.PI) / 2;
    }

    private double zoom(int mapPx, int worldPx, double fraction) {
        return Math.floor(Math.log(mapPx / worldPx / fraction) / LN2);
    }

    public static float getRotationAngle(LatLng secondLastLatLng, LatLng lastLatLng) {
        double x1 = secondLastLatLng.latitude;
        double y1 = secondLastLatLng.longitude;
        double x2 = lastLatLng.latitude;
        double y2 = lastLatLng.longitude;

        float xDiff = (float) (x2 - x1);
        float yDiff = (float) (y2 - y1);

        return (float) (Math.atan2(yDiff, xDiff) * 180.0 / Math.PI);

    }

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
                                ActivityCompat.requestPermissions(FarmNavigationActivity.this,
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
                .addOnConnectionFailedListener(FarmNavigationActivity.this).build();
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

                            status.startResolutionForResult(FarmNavigationActivity.this, 1000);
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
    List<LatLng> latLngList=new ArrayList<>();
    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {
        public AsyncTaskRunner() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e(TAG, "Poly " + new Gson().toJson(latLngList));
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            latAraay = new String[latLngList.size()];
            lngArray = new String[latLngList.size()];
            final int sizeofgps = latLngList.size();
            for (int i = 0; i < latLngList.size(); i++) {
                latAraay[i] = ""+latLngList.get(i).latitude;
                lngArray[i] = ""+latLngList.get(i).longitude;
                LatLng latlngforbound = new LatLng(Double.parseDouble(latAraay[i]), Double.parseDouble(lngArray[i]));
                builder.include(latlngforbound);

            }

            final LatLng latLng = new LatLng(Double.valueOf(latAraay[0]), Double.valueOf(lngArray[0]));

            FarmNavigationActivity.this.runOnUiThread(new Runnable() {

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

    private LatLngBounds getPolygonBounds(List<LatLng> polygon) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < polygon.size(); i++) {
            builder.include(polygon.get(i));
        }
        LatLngBounds bounds = builder.build();
        return bounds;
    }

    Polygon UCCpolygon;
    PolygonOptions options;

    private void showArea(int i) {

        if (i < 3) {
            //Toast.makeText(context, "Please submit atleast 3 points", Toast.LENGTH_SHORT).show();
            ViewFailDialog viewFailDialog = new ViewFailDialog();
            viewFailDialog.showDialog(FarmNavigationActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.should_have_at_least_two_points_to_display));
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


        }
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
                                viewFailDialog.showSessionExpireDialog(FarmNavigationActivity.this, response.body().getMsg());
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(FarmNavigationActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(FarmNavigationActivity.this, resources.getString(R.string.authorization_expired));
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
                            viewFailDialog.showSessionExpireDialog(FarmNavigationActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(FarmNavigationActivity.this, resources.getString(R.string.authorization_expired));
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
                            notifyApiDelay(FarmNavigationActivity.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }*/
                    }

                    @Override
                    public void onFailure(Call<OmitanceShowResponse> call, Throwable t) {

                       /* long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        notifyApiDelay(FarmNavigationActivity.this, call.request().url().toString(),
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
                                String json = response.body().string();
                                Log.e(TAG, "OMITANCE res way " + json);
//                                json="{\"status\":1,\"msg\":\"success\",\"data\":[{\"id\":\"11\",\"farm_id\":\"2903\",\"comp_id\":\"100130\",\"region_id\":null,\"details\":{\"latitude\":23.968942366591712,\"longitude\":76.82759441435337},\"type\":\"W\",\"name\":\"OMS Location No 10\",\"doa\":\"2020-10-02 06:34:27\",\"is_active\":\"Y\"}]}";
                                JSONObject jsonObject = new JSONObject(json);
                                try {

                                    if (jsonObject != null && jsonObject.has("data")) {
                                        JSONArray array = jsonObject.getJSONArray("data");
                                        if (array != null) {
                                            for (int i = 0; i < array.length(); i++) {
                                                JSONObject object = array.getJSONObject(i);
                                                String name = null;
                                                name = object.getString("name");
                                                Log.e(TAG, "Name " + name);
                                                if (name != null && !TextUtils.isEmpty(name) && !name.toLowerCase().equals("null")) {
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
                                    System.out.println(TAG + " " + jsonObject);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                            /*WayPointFetchResponse statusMsgModel = response.body();
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(FarmNavigationActivity.this, response.body().getMsg());
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(FarmNavigationActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(FarmNavigationActivity.this, resources.getString(R.string.authorization_expired));
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
                            viewFailDialog.showSessionExpireDialog(FarmNavigationActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(FarmNavigationActivity.this, resources.getString(R.string.authorization_expired));
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
                            notifyApiDelay(FarmNavigationActivity.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }*/
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                       /* long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        notifyApiDelay(FarmNavigationActivity.this, call.request().url().toString(),
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

    Bitmap bmp, bmp2, bmp3, bmp4;

    private void showWayPoints(String name, LatLng latLngList) {
        Log.e(TAG, "WayPoint name " + name + " point: " + new Gson().toJson(latLngList));
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


}
