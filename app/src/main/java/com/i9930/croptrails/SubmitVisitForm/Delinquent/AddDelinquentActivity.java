package com.i9930.croptrails.SubmitVisitForm.Delinquent;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterNewDD;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.DDNew;
import com.i9930.croptrails.CommonClasses.DDResponseNew;
import com.i9930.croptrails.CommonClasses.ParamData;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.FarmDetails.Model.Activity.DoneActivityImage;
import com.i9930.croptrails.FarmDetails.Model.Visit.VisitImageTimeline;
import com.i9930.croptrails.FarmDetails.Model.Visit.VisitReportTimeline;
import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineInnerData;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Maps.ShowArea.Model.GetGpsCoordinates;
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;
import com.i9930.croptrails.Maps.ShowArea.Model.geojson.GeoJsonResponse;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownFetchListener;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownCacheManager2;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownT2;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmCacheManager;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmLoadListener;
import com.i9930.croptrails.RoomDatabase.Harvest.FetchLastVisitIdListener;
import com.i9930.croptrails.RoomDatabase.Timeline.CalendarJSON;
import com.i9930.croptrails.RoomDatabase.Timeline.Timeline;
import com.i9930.croptrails.RoomDatabase.Timeline.TimelineCacheManager;
import com.i9930.croptrails.RoomDatabase.Timeline.TimelineNotifyInterface;
import com.i9930.croptrails.RoomDatabase.Visit.Visit;
import com.i9930.croptrails.RoomDatabase.Visit.VrCacheManager;
import com.i9930.croptrails.SubmitActivityForm.Model.AgriInput.AgriInput;
import com.i9930.croptrails.SubmitActivityForm.Model.CompAgriDatum;
import com.i9930.croptrails.SubmitActivityForm.Model.CompAgriResponse;
import com.i9930.croptrails.SubmitActivityForm.Model.DoneActNew.DoneActivityResponseNew;
import com.i9930.croptrails.SubmitVisitForm.Model.AddVisitSendDataNew;
import com.i9930.croptrails.SubmitVisitForm.Model.VisitResponseNew;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;
import com.i9930.croptrails.showcaseview.MaterialShowcaseSequence;
import com.i9930.croptrails.showcaseview.MaterialShowcaseView;
import com.i9930.croptrails.showcaseview.ShowcaseConfig;

import static java.lang.Double.parseDouble;
import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;
import static com.i9930.croptrails.Utility.AppConstants.getCoordinatesFromGeometry;


public class AddDelinquentActivity extends AppCompatActivity implements FarmLoadListener, DropDownFetchListener, FetchLastVisitIdListener, TimelineNotifyInterface {

    String TAG = "TimelineAddVisitActivity2";
    Toolbar mActionBarToolbar;
    AddDelinquentActivity context;
    private int SELECT_FILE1 = 1, SELECT_FILE2 = 2, SELECT_FILE3 = 3, SELECT_FILE4 = 4, SELECT_FILE5 = 5, SELECT_FILE6 = 6;
    private int REQUEST_CAMERA1 = 11, REQUEST_CAMERA2 = 12, REQUEST_CAMERA3 = 13, REQUEST_CAMERA4 = 14, REQUEST_CAMERA5 = 15, REQUEST_CAMERA6 = 16;
    Location currentLocation;
    String image_capture_date1 = "", image_capture_date2 = "", image_capture_date3 = "", image_capture_date4 = "", image_capture_date5 = "", image_capture_date6 = "";
    String userChoosenTask = "";
    String pictureImagePath = "";
    Bitmap myBitmap;
    List<Bitmap> imageList;
    ProgressDialog progressDialog;
    ViewFailDialog viewFailDialog;
    //Images
    @BindView(R.id.farm_image1_timeline_visit)
    ImageView farm_image1;
    @BindView(R.id.farm_image2_timeline_visit)
    ImageView farm_image2;
    @BindView(R.id.farm_image3_timeline_visit)
    ImageView farm_image3;
    @BindView(R.id.farm_image4_timeline_visit)
    ImageView farm_image4;
    @BindView(R.id.farm_image5_timeline_visit)
    ImageView farm_image5;
    @BindView(R.id.farm_image6_timeline_visit)
    ImageView farm_image6;
    Resources resources;
    List<String> picturePathList = new ArrayList<>();
    String[] img_list = new String[6];
    Farm farm;
    Timeline timeline;
    int lastVisitReportId = 0;
    List<TimelineInnerData> calendarData = new ArrayList<>();
    ConnectivityManager connectivityManager;
    boolean connected = false;
    //FloatingAction
    String message = "";
    String message2 = "";
    String message3 = "";
    String farmLatitude = "";
    String farmLongitude = "";
    List<LatLng> latLngList = new ArrayList<>();
    List<String> imageLatitude = new ArrayList<>();
    List<String> imageLongitude = new ArrayList<>();
    @BindView(R.id.connectivityLine)
    View connectivityLine;
    @BindView(R.id.reasonSpinner)
    SearchableSpinner reasonSpinner;
    @BindView(R.id.commentEt)
    EditText commentEt;

    @BindView(R.id.act_pres_submit_timeline_visit)
    TextView submitButton;
    List<DDNew> ddNewArrayList = new ArrayList<>();
    DDNew selectedReason = null;
    SpinnerAdapterNewDD spinnerAdapter;
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
//                Log.e(TAG,"Add Errro "+adError.toString());
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
        super.onCreate(savedInstanceState);
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
        setContentView(R.layout.activity_add_delinquent);

        context = this;
        loadAds();
//        Toast.makeText(context, ""+TAG, Toast.LENGTH_SHORT).show();
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        viewFailDialog = new ViewFailDialog();
        ButterKnife.bind(this);
        // this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        imageList = new ArrayList<>();

        onClicks();
        farmLatitude = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARM_LAT);
        farmLongitude = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARM_LONG);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        spinnerAdapter = new SpinnerAdapterNewDD(context, ddNewArrayList, resources.getString(R.string.select_reason));
        reasonSpinner.setAdapter(spinnerAdapter);
        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {

                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getAllDataNew(AppConstants.CHEMICAL_UNIT.DELINQUENT);
                } else {
                    DDResponseNew responseNew = new Gson().fromJson(dropDown.getData(), DDResponseNew.class);
                    Log.e(TAG,"DELI List "+new Gson().toJson(responseNew));
                    if (responseNew != null && responseNew.getData() != null && responseNew.getData().size() > 0) {
                        ddNewArrayList.addAll(responseNew.getData());
                        spinnerAdapter.notifyDataSetChanged();
                    } else {
                        getAllDataNew(AppConstants.CHEMICAL_UNIT.DELINQUENT);
                    }
                }
            }
        }, AppConstants.CHEMICAL_UNIT.DELINQUENT);

        reasonSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
//                Toast.makeText(contextlang, "" + i, Toast.LENGTH_SHORT).show();
                try {
                    selectedReason = spinnerAdapter.getItem(i);
                } catch (Exception e) {
                    e.printStackTrace();
                    selectedReason = null;
                }
            }

            @Override
            public void onNothingSelected() {
                selectedReason = null;
            }
        });


        imageLatitude.add("");
        imageLatitude.add("");
        imageLatitude.add("");
        imageLatitude.add("");
        imageLatitude.add("");
        imageLatitude.add("");
        imageLongitude.add("");
        imageLongitude.add("");
        imageLongitude.add("");
        imageLongitude.add("");
        imageLongitude.add("");
        imageLongitude.add("");
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.delinquent));
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(resources.getString(R.string.please_wait_msg));
        onImageClick();
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                currentLocation = location;
                                String url = location.getLatitude() + "," + location.getLongitude();
                                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                try {
                                    List<Address> list = geocoder.getFromLocation(
                                            location.getLatitude(), location.getLongitude(), 1);
                                    if (list != null && list.size() > 0) {
                                        Address address = list.get(0);
                                    }
                                } catch (IOException e) {

                                }
                            }
                        }
                    });
        }

        new GetGeoJsonData().execute();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
    }

    @Override
    protected void onStart() {
        super.onStart();
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void onImageClick() {
        farm_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage("capture1");
//                Intent photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                Uri uri  = Uri.parse("file:///sdcard/photo.jpg");
//                photo.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
//                startActivityForResult(photo,REQUEST_CAMERA1);
            }
        });

        farm_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageList.size() >= 1) {
                    selectImage("capture2");
                    //showImage();
                } else {
                    viewFailDialog.showDialog(context, "Opps!", "Please capture 1st image first");
                }
            }
        });

        farm_image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageList.size() >= 2)
                    selectImage("capture3");
                else {
                    viewFailDialog.showDialog(context, "Opps!", "Please capture 2nd image first");
                }
            }
        });

        farm_image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageList.size() >= 3)
                    selectImage("capture4");
                else {
                    viewFailDialog.showDialog(context, "Opps!", "Please capture 3rd image first");
                }
            }
        });

        farm_image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageList.size() >= 4)
                    selectImage("capture5");
                else {
                    viewFailDialog.showDialog(context, "Opps!", "Please capture 4rh image first");
                }
            }
        });

        farm_image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageList.size() >= 5)
                    selectImage("capture6");
                else {
                    viewFailDialog.showDialog(context, "Opps!", "Please capture 5th image first");
                }
            }
        });
    }

    private void onClicks() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedReason == null) {
                    Toasty.error(context, "Please select reason", Toast.LENGTH_SHORT).show();
                } else {
//                    Toasty.error(context," selected reason",Toast.LENGTH_SHORT).show();
                    submitButton.setClickable(false);
                    submitButton.setEnabled(false);
                    progressDialog.show();
                    updateVettingStatus(AppConstants.VETTING.DELINQUENT, true);
//                    uploadVisitData();
                }
            }
        });
    }


    private void updateVettingStatus(String isSelected, boolean uploadVisit) {
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            try {
                String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
                String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
                String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
                String farmID = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
                Call<StatusMsgModel> statusMsgModelCall = apiService.updateVettingStatus(userId, token, compId, farmID, isSelected);
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<StatusMsgModel>() {
                    @Override
                    public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                        String error = null;
                        isLoaded[0] = true;
                        Log.e(TAG, "Status act flow " + response.code());
                        if (response.isSuccessful()) {
                            Log.e(TAG, "Update Flower res " + response.body());
                            StatusMsgModel statusMsgModel = response.body();
                            if (response.body().getStatus() == 10) {
                                submitButton.setClickable(true);
                                submitButton.setEnabled(true);
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, response.body().getMsg());
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                submitButton.setClickable(true);
                                submitButton.setEnabled(true);
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                submitButton.setClickable(true);
                                submitButton.setEnabled(true);
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                            } else if (statusMsgModel.getStatus() == 1) {

                                if (uploadVisit)
                                    uploadVisitData();

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        submitButton.setClickable(true);
                                        submitButton.setEnabled(true);
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.something_went_wrong_msg));

                                    }
                                });

                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            submitButton.setClickable(true);
                            submitButton.setEnabled(true);
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            submitButton.setClickable(true);
                            submitButton.setEnabled(true);
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                        } else {
                            try {

                                error = response.errorBody().string().toString();
                                submitButton.setClickable(true);
                                submitButton.setEnabled(true);
                                Log.e("Error", response.errorBody().string().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                            notifyApiDelay(context, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {

                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        submitButton.setClickable(true);
                        submitButton.setEnabled(true);
                        notifyApiDelay(context, call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);
                        Log.e(TAG, "failure update status" + t.toString());
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
        } else {
        }
    }

    public boolean checkStoragePermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(getString(R.string.permission_necessary));
                    alertBuilder.setMessage(getString(R.string.external_storage_permission_is_necessary));
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            requestGalleryPermission();
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                    return false;
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            SELECT_FILE1);

                    return false;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean checkCameraPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(getString(R.string.permission_necessary));
                    alertBuilder.setMessage("Camera permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            requestCameraAndStoragePermission();
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                    return false;
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, SELECT_FILE2
                    );

                    return false;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void requestCameraAndStoragePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, SELECT_FILE2
        );

    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                SELECT_FILE1);
    }


    private void selectImage(final String onclick) {
        final CharSequence[] items = {resources.getString(R.string.image_take_photo),
                resources.getString(R.string.image_choose_from_library),
                resources.getString(R.string.image_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(resources.getString(R.string.add_photo_msg));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
//                boolean result = Utility.checkPermission(context);
//                boolean resultCam = checkCameraPermission(context);
//                boolean resultGal = checkStoragePermission(context);
                if (items[item].equals(resources.getString(R.string.image_take_photo))) {
                    userChoosenTask = "Take Photo";
                    if (checkCameraPermission(context))
                        cameraIntent(onclick);

                } else if (items[item].equals(resources.getString(R.string.image_choose_from_library))) {
                    userChoosenTask = "Choose from Library";
                    if (checkStoragePermission(context))
                        galleryIntent(onclick);

                } else if (items[item].equals(resources.getString(R.string.image_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tour_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getAppTour();
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void getAppTour() {

        Log.e(TAG, "app tour start");
        ShowcaseConfig config = new ShowcaseConfig(); //create the showcase config
        config.setDelay(0); //set the delay of each sequence using millis variable

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);
        sequence.setConfig(config);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setSkipText(getResources().getString(R.string.tour_skip))
                        .setTarget(findViewById(R.id.images_to_upload_lieanr_lay))
                        .setTitleText(getResources().getString(R.string.timeline_ac3_tour_upload_img_title)).setTitleTextGravity(Gravity.TOP)
                        .setDismissText(getResources().getString(R.string.got_it))
                        .setContentText(getResources().getString(R.string.timeline_ac3_tour_upload_img_content))
                        .withRectangleShape()
                        .build());

        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setSkipText(getResources().getString(R.string.tour_skip)).setGravity(20)
                .setTarget(findViewById(R.id.cpc_linear_lay_timeline_visit)).setTitleTextGravity(Gravity.TOP)
                .setTitleText(getResources().getString(R.string.timeline_ac3_tour_crop_cond_title))
                .setDismissText(getResources().getString(R.string.got_it))
                .setContentText(getResources().getString(R.string.timeline_ac3_tour_crop_cond_content))
                .withRectangleShape()
                .build());

        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setSkipText(getResources().getString(R.string.tour_skip))
                .setTarget(findViewById(R.id.app_tour_floating_btn))
                .setTitleText(getResources().getString(R.string.timeline_ac3_tour_add_more_details_title))
                .setDismissText(getResources().getString(R.string.got_it))
                .build());/*.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
            @Override
            public void onDismiss(MaterialShowcaseView itemView, int position) {
                fabParentVisitAct.open(true);
                if (fabParentVisitAct.isOpened())
                {
                    FabMenuTour();
                }
            }
        });*/
        sequence.start();


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBackPressed() {
        finish();
    }

    private void finishAllAndStartLanding() {
        Intent intent = new Intent(context, LandingActivity.class);
        ActivityOptions options = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
            startActivity(intent, options.toBundle());
            finish();
        } else {
            finishAffinity();
            startActivity(intent);
            finish();
        }
    }


    private void cameraIntent(String onclick) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.putExtra(android.provider.MediaStore.EXTRA_SIZE_LIMIT, "7200000");
        if (onclick.equals("capture1")) {
            startActivityForResult(intent, REQUEST_CAMERA1);
        } else if (onclick.equals("capture2")) {
            startActivityForResult(intent, REQUEST_CAMERA2);
        } else if (onclick.equals("capture3")) {
            startActivityForResult(intent, REQUEST_CAMERA3);
        } else if (onclick.equals("capture4")) {
            startActivityForResult(intent, REQUEST_CAMERA4);
        } else if (onclick.equals("capture5")) {
            startActivityForResult(intent, REQUEST_CAMERA5);
        } else if (onclick.equals("capture6")) {
            startActivityForResult(intent, REQUEST_CAMERA6);
        }
    }


    private void galleryIntent(String onclick) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(android.provider.MediaStore.EXTRA_SIZE_LIMIT, "720000");
        intent.setType("image/*");
        //intent.putExtra("result",onclick);
        if (onclick.equals("capture1")) {
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file_msg)), SELECT_FILE1);
        } else if (onclick.equals("capture2")) {
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file_msg)), SELECT_FILE2);
        } else if (onclick.equals("capture3")) {
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file_msg)), SELECT_FILE3);
        } else if (onclick.equals("capture4")) {
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file_msg)), SELECT_FILE4);
        } else if (onclick.equals("capture5")) {
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file_msg)), SELECT_FILE5);
        } else if (onclick.equals("capture6")) {
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file_msg)), SELECT_FILE6);
        }

    }

    private void onSelectFromGalleryResult(Intent data, String onclick) {
        Uri selectedImage = data.getData();
        //String picturePath=getRealPathFromURI(selectedImage);
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        Log.e(TAG, "Selected Picture Path " + picturePath);
        cursor.close();
        if (onclick.equals("capture1")) {
            myBitmap = BitmapFactory.decodeFile(picturePath);
            myBitmap = getResizedBitmap(myBitmap, 720, 1080);
//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
            if (ReadExif(picturePath, "capture1", 0, false)) {
                myBitmap = addWaterMark(myBitmap);
                if (imageList.size() > 0) {
                    imageList.remove(0);
//                    imageList.add(0, compressBitmap(myBitmap, 2, 50, 0));
                    imageList.add(0, compressImage(myBitmap, 0));
                    picturePathList.remove(0);
                    picturePathList.add(0, picturePath);
                    Log.e("TimelineAddVisit", "Image 1 Replaced");
                } else {
//                    imageList.add(0, compressBitmap(myBitmap, 2, 50, 0));
                    imageList.add(0, compressImage(myBitmap, 0));
                    picturePathList.add(0, picturePath);
                    Log.e("TimelineAddVisit", "Image 1 Added");
                }
                farm_image1.setImageBitmap(myBitmap);
//                imagefull.setVisibility(View.VISIBLE);
//                imagefull.setImageBitmap(myBitmap);
            } else {
                myBitmap = null;
                //image1.setImageBitmap(null);
            }
        } else if (onclick.equals("capture2")) {
            myBitmap = BitmapFactory.decodeFile(picturePath);
            myBitmap = getResizedBitmap(myBitmap, 720, 1080);
//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
            if (ReadExif(picturePath, "capture2", 1, false)) {
                myBitmap = addWaterMark(myBitmap);
                if (imageList.size() > 1) {
                    picturePathList.remove(1);
                    picturePathList.add(1, picturePath);
                    imageList.remove(1);
//                    imageList.add(0, compressBitmap(myBitmap, 2, 50, 1));
                    imageList.add(1, compressImage(myBitmap, 1));
                    Log.e("TimelineAddVisit", "Image 2 Replaced");
                } else {
//                    imageList.add(0, compressBitmap(myBitmap, 2, 50, 1));
                    imageList.add(1, compressImage(myBitmap, 1));
                    Log.e("TimelineAddVisit", "Image 2 added");
                    picturePathList.add(1, picturePath);
                }
                farm_image2.setImageBitmap(myBitmap);
            } else {
                myBitmap = null;
                //image1.setImageBitmap(null);
            }
        } else if (onclick.equals("capture3")) {
            myBitmap = BitmapFactory.decodeFile(picturePath);
            myBitmap = getResizedBitmap(myBitmap, 720, 1080);

//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
            if (ReadExif(picturePath, "capture3", 2, false)) {
                myBitmap = addWaterMark(myBitmap);
                if (imageList.size() > 2) {
                    picturePathList.remove(2);
                    picturePathList.add(2, picturePath);
                    imageList.remove(2);
//                    imageList.add(0, compressBitmap(myBitmap, 2, 50, 2));
                    imageList.add(2, compressImage(myBitmap, 2));
                    Log.e("TimelineAddVisit", "Image 3 Replaced");
                } else {
//                    imageList.add(0, compressBitmap(myBitmap, 2, 50, 2));
                    imageList.add(2, compressImage(myBitmap, 2));
                    picturePathList.add(2, picturePath);
                    Log.e("TimelineAddVisit", "Image 3 added");
                }
                farm_image3.setImageBitmap(myBitmap);
            } else {
                myBitmap = null;
                //image1.setImageBitmap(null);
            }

        } else if (onclick.equals("capture4")) {
            myBitmap = BitmapFactory.decodeFile(picturePath);
            myBitmap = getResizedBitmap(myBitmap, 720, 1080);

//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
            if (ReadExif(picturePath, "capture4", 3, false)) {
                myBitmap = addWaterMark(myBitmap);
                if (imageList.size() > 3) {
                    picturePathList.remove(3);
                    picturePathList.add(3, picturePath);
                    imageList.remove(3);
//                    imageList.add(0, compressBitmap(myBitmap, 2, 50, 3));
                    imageList.add(3, compressImage(myBitmap, 3));
                    Log.e("TimelineAddVisit", "Image 4 Replaced");
                } else {
                    picturePathList.add(3, picturePath);
//                    imageList.add(0, compressBitmap(myBitmap, 2, 50, 3));
                    imageList.add(3, compressImage(myBitmap, 3));
                    Log.e("TimelineAddVisit", "Image 4 added");
                }

                farm_image4.setImageBitmap(myBitmap);
            } else {
                myBitmap = null;
                //image1.setImageBitmap(null);
            }
        } else if (onclick.equals("capture5")) {
            myBitmap = BitmapFactory.decodeFile(picturePath);
            myBitmap = getResizedBitmap(myBitmap, 720, 1080);

//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
            if (ReadExif(picturePath, "capture5", 4, false)) {
                myBitmap = addWaterMark(myBitmap);
                if (imageList.size() > 4) {
                    imageList.remove(4);
                    picturePathList.remove(4);
                    picturePathList.add(4, picturePath);
//                    imageList.add(4, compressBitmap(myBitmap, 2, 50, 4));
                    imageList.add(4, compressImage(myBitmap, 4));
                    Log.e("TimelineAddVisit", "Image 5 Replaced");
                } else {
//                    imageList.add(4, compressBitmap(myBitmap, 2, 50, 4));
                    imageList.add(4, compressImage(myBitmap, 4));
                    Log.e("TimelineAddVisit", "Image 5 added");
                    picturePathList.add(4, picturePath);
                }
                farm_image5.setImageBitmap(myBitmap);
            } else {
                myBitmap = null;
                //image1.setImageBitmap(null);
            }
        } else if (onclick.equals("capture6")) {
            myBitmap = BitmapFactory.decodeFile(picturePath);
            myBitmap = getResizedBitmap(myBitmap, 720, 1080);

//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
            if (ReadExif(picturePath, "capture6", 5, false)) {
                myBitmap = addWaterMark(myBitmap);
                if (imageList.size() > 5) {
                    picturePathList.remove(5);
                    picturePathList.add(5, picturePath);
                    imageList.remove(5);
//                    imageList.add(5, compressBitmap(myBitmap, 2, 50, 5));
                    imageList.add(5, compressImage(myBitmap, 5));
                    Log.e("TimelineAddVisit", "Image 6 Replaced");
                } else {
//                    imageList.add(5, compressBitmap(myBitmap, 2, 50, 5));
                    imageList.add(5, compressImage(myBitmap, 5));
                    Log.e("TimelineAddVisit", "Image 6 replaced");
                    picturePathList.add(5, picturePath);
                }
                farm_image6.setImageBitmap(myBitmap);
            } else {
                myBitmap = null;
                //image1.setImageBitmap(null);
            }
        }
    }


    private void onCaptureImageResult(Intent data, String onclick) {
//        Bundle extras = data.getExtras();
        //String onclick=extras.getString("result");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        File imgFile = new File(pictureImagePath);

        if (imgFile.exists()) {
            if (onclick.equals("capture1")) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = getResizedBitmap(myBitmap, 720, 1080);

//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
                if (ReadExif(imgFile.getAbsolutePath(), "capture1", 0, true)) {
                    myBitmap = addWaterMark(myBitmap);
                    if (imageList.size() > 0) {
                        imageList.remove(0);
//                        imageList.add(0, compressBitmap(myBitmap, 2, 50, 0));
                        imageList.add(0, compressImage(myBitmap, 0));
                        picturePathList.remove(0);
                        picturePathList.add(0, pictureImagePath);
                        Log.e("TimelineAddVisit", "Image 1 Replaced");
                    } else {
//                        imageList.add(0, compressBitmap(myBitmap, 2, 50, 0));
                        imageList.add(0, compressImage(myBitmap, 0));
                        picturePathList.add(0, pictureImagePath);
                    }
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    farm_image1.setImageBitmap(myBitmap);
//                    imagefull.setVisibility(View.VISIBLE);
//                    imagefull.setImageBitmap(myBitmap);

                } else {
                    //image1.setImageBitmap(null);
                }
                //Log.e("Exif",ReadExif(imgFile.getAbsolutePath()));
            } else if (onclick.equals("capture2")) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = getResizedBitmap(myBitmap, 720, 1080);

//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
                if (ReadExif(imgFile.getAbsolutePath(), "capture2", 1, true)) {
                    myBitmap = addWaterMark(myBitmap);
                    if (imageList.size() > 1) {
                        picturePathList.remove(1);
                        picturePathList.add(1, pictureImagePath);
                        imageList.remove(1);
//                        imageList.add(0, compressBitmap(myBitmap, 2, 50, 1));
                        imageList.add(1, compressImage(myBitmap, 1));
                    } else {
//                        imageList.add(1, compressBitmap(myBitmap, 2, 50, 1));
                        imageList.add(1, compressImage(myBitmap, 1));
                        picturePathList.add(1, pictureImagePath);
                    }
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    farm_image2.setImageBitmap(myBitmap);

                }
            } else if (onclick.equals("capture3")) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = getResizedBitmap(myBitmap, 720, 1080);

//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
                if (ReadExif(imgFile.getAbsolutePath(), "capture3", 2, true)) {
                    myBitmap = addWaterMark(myBitmap);
                    if (imageList.size() > 2) {
                        picturePathList.remove(2);
                        picturePathList.add(2, pictureImagePath);
                        imageList.remove(2);
//                        imageList.add(0, compressBitmap(myBitmap, 2, 50, 2));
                        imageList.add(2, compressImage(myBitmap, 2));
                    } else {
//                        imageList.add(0, compressBitmap(myBitmap, 2, 50, 2));
                        imageList.add(2, compressImage(myBitmap, 2));
                        picturePathList.add(2, pictureImagePath);
                    }
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    farm_image3.setImageBitmap(myBitmap);

                }
            } else if (onclick.equals("capture4")) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = getResizedBitmap(myBitmap, 720, 1080);

//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
                if (ReadExif(imgFile.getAbsolutePath(), "capture4", 3, true)) {
                    myBitmap = addWaterMark(myBitmap);
                    if (imageList.size() > 3) {
                        picturePathList.remove(3);
                        picturePathList.add(3, pictureImagePath);
                        imageList.remove(3);
//                        imageList.add(3, compressBitmap(myBitmap, 2, 50, 3));
                        imageList.add(3, compressImage(myBitmap, 3));

                    } else {
                        picturePathList.add(3, pictureImagePath);
//                        imageList.add(3, compressBitmap(myBitmap, 2, 50, 3));
                        imageList.add(3, compressImage(myBitmap, 3));

                    }
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    farm_image4.setImageBitmap(myBitmap);

                }
            } else if (onclick.equals("capture5")) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = getResizedBitmap(myBitmap, 720, 1080);

//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
                if (ReadExif(imgFile.getAbsolutePath(), "capture5", 4, true)) {
                    myBitmap = addWaterMark(myBitmap);
                    if (imageList.size() > 4) {
                        picturePathList.remove(4);
                        picturePathList.add(4, pictureImagePath);
                        imageList.remove(4);
//                        imageList.add(4, compressBitmap(myBitmap, 2, 50, 4));
                        imageList.add(4, compressImage(myBitmap, 4));

                    } else {
//                        imageList.add(4, compressBitmap(myBitmap, 2, 50, 4));
                        imageList.add(4, compressImage(myBitmap, 4));

                        picturePathList.add(4, pictureImagePath);
                    }
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    farm_image5.setImageBitmap(myBitmap);

                }


            } else if (onclick.equals("capture6")) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = getResizedBitmap(myBitmap, 720, 1080);

//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
                if (ReadExif(imgFile.getAbsolutePath(), "capture6", 5, true)) {
                    myBitmap = addWaterMark(myBitmap);
                    if (imageList.size() > 5) {
                        picturePathList.remove(5);
                        picturePathList.add(5, pictureImagePath);
                        imageList.remove(5);
//                        imageList.add(0, compressBitmap(myBitmap, 2, 50, 5));

                        imageList.add(5, compressImage(myBitmap, 5));

                    } else {
                        picturePathList.add(5, pictureImagePath);
                        imageList.add(5, compressImage(myBitmap, 5));

//                        imageList.add(5, compressBitmap(myBitmap, 2, 50, 5));
                    }
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    farm_image6.setImageBitmap(myBitmap);

                }


            }
        }
    }

   /* public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
    }*/

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        /*float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;*/
        float scaleWidth = ((float) (2.0 / 3.0));
        float scaleHeight = ((float) (2.0 / 3.0));
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    String imgLat = "0", imgLong = "0";

    Boolean ReadExif(String file, String captureNo, int index, boolean flag) {

        message = "";
        message2 = "";
        message3 = "";
        imgLat = "0";
        imgLong = "0";
        Boolean time_stamp = false;
        try {
            ExifInterface exifInterface = new ExifInterface(file);
            if (exifInterface.getAttribute(ExifInterface.TAG_DATETIME) != null) {

                time_stamp = true;
                Date final_date = parseDateToddMMyyyy(exifInterface.getAttribute(ExifInterface.TAG_DATETIME), captureNo);
                Date c = Calendar.getInstance().getTime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(c);
                calendar.add(Calendar.DAY_OF_YEAR, -3);
                Date newDate = calendar.getTime();
                if (newDate.after(final_date)) {
                    time_stamp = true;
                    message3 = "Image is old";
                    Log.e(TAG, "BOTH DATES " + "     " + final_date.toString() + "    " + newDate.toString());

                } else {
                    message3 = "";
                    Log.e(TAG, "BOTH DATES " + "     " + final_date.toString() + "    " + newDate.toString());
                }

            } else {
                message3 = "Image date not available";
                time_stamp = true;
                Log.e(TAG, "outer else exif");
            }

            float[] latLong = new float[2];
            boolean hasLatLong = exifInterface.getLatLong(latLong);
            String imgLat = "";
            String imgLong = "";
            if (hasLatLong) {
                System.out.println("SubmitPldActivity Latitude: " + latLong[0]);
                System.out.println("SubmitPldActivity Longitude: " + latLong[1]);
                if (latLong.length > 1) {
                    imgLat = "" + latLong[0];
                    imgLong = "" + latLong[0];
                }
            }
            if (imgLat != null && imgLong != null && !TextUtils.isEmpty(imgLat) && !TextUtils.isEmpty(imgLong)) {
                this.imgLat = imgLat;
                this.imgLong = imgLong;
                imageLatitude.set(index, imgLat);
                imageLongitude.set(index, imgLong);

//                if (farmLatitude != null && farmLongitude != null && !TextUtils.isEmpty(farmLongitude) && !TextUtils.isEmpty(farmLatitude)) {
                LatLng latLng = new LatLng(Double.valueOf(imgLat), Double.valueOf(imgLong));
                if (latLngList != null && latLngList.size() > 2 && AppConstants.isPointInPolygon(latLng, latLngList)) {
                    message = "";
                    message2 = "";
                } else {
                    message = "Image might not be from farm";
                    message2 = "Gps coordinates mismatch";
                }

                /*} else {
                    message = "Image might not be from farm";
                    message2 = "Farm coordinates missing";
                }*/

            } else if (flag && currentLocation != null) {
                this.imgLat = "" + currentLocation.getLatitude();
                this.imgLong = "" + currentLocation.getLongitude();
                imageLatitude.set(index, "" + currentLocation.getLatitude());
                imageLongitude.set(index, "" + currentLocation.getLongitude());
                if (farmLatitude != null && farmLongitude != null && !TextUtils.isEmpty(farmLongitude) && !TextUtils.isEmpty(farmLatitude)) {
                    LatLng latLng = new LatLng(Double.valueOf(currentLocation.getLatitude()), Double.valueOf(currentLocation.getLongitude()));
                    if (latLngList != null && latLngList.size() > 2 && AppConstants.isPointInPolygon(latLng, latLngList)) {
                        message = "";
                        message2 = "";
                    } else {
                        message = "Image might not be from farm";
                        message2 = "Gps coordinates mismatch";
                    }
                } else {
                    message = "Image might not be from farm";
                    message2 = "Farm coordinates missing";
                }

            } else {
                imageLatitude.add(index, "0");
                imageLongitude.add(index, "0");
                message = "Image might not be from farm";
                message2 = "Image coordinates missing";
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        return time_stamp;
    }


    public Date parseDateToddMMyyyy(String time, String capture_no) {
        String inputPattern = "yyyy:MM:dd HH:mm:ss";
        String outputPattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            Log.e("Today's date in middle", date.toString());
            str = outputFormat.format(date);
            Log.e("Checkdate", str);
            if (capture_no.equals("capture1")) {
                image_capture_date1 = str;
            } else if (capture_no.equals("capture2")) {
                image_capture_date2 = str;
            } else if (capture_no.equals("capture3")) {
                image_capture_date3 = str;
            } else if (capture_no.equals("capture4")) {
                image_capture_date4 = str;
            } else if (capture_no.equals("capture5")) {
                image_capture_date4 = str;
            } else if (capture_no.equals("capture6")) {
                image_capture_date5 = str;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

/*

    private class FetchGpsCordinates extends AsyncTask<String, Void, String> {

        public FetchGpsCordinates() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {
            latLngList.clear();
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
            Call<GetGpsCoordinates> getGpsCoordinatesCall = apiService.getGpsCoordinates(
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID), userId, token);
            getGpsCoordinatesCall.enqueue(new Callback<GetGpsCoordinates>() {
                @Override
                public void onResponse(Call<GetGpsCoordinates> call, Response<GetGpsCoordinates> response) {
                    if (response.isSuccessful()) {
                        GetGpsCoordinates getGpsCoordinates = response.body();
                        Log.e(TAG, "Farm corrds list " + new Gson().toJson(getGpsCoordinates));
                        if (getGpsCoordinates.getStatus() == 10) {

                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
//                            ViewFailDialog viewFailDialog = new ViewFailDialog();
//                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
//                            ViewFailDialog viewFailDialog = new ViewFailDialog();
//                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                        } else if (getGpsCoordinates.getStatus() != 0) {
                            List<LatLngFarm> latLngs = getGpsCoordinates.getData();

                            if (latLngs != null) {
                                for (int i = 0; i < latLngs.size(); i++) {
                                    latLngList.add(new LatLng(Double.valueOf(latLngs.get(i).getLat()), Double.valueOf(latLngs.get(i).getLng())));
                                }
                            }
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<GetGpsCoordinates> call, Throwable t) {
                    //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, t.toString());


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
*/

    private void getGeoJson(JsonObject jsonObject, String type) {

        try {
            String json = new Gson().toJson(jsonObject);
            JSONObject jsonObjectGeo = new JSONObject(json);
            GeoJsonLayer layer = new GeoJsonLayer(null, jsonObjectGeo);
            for (GeoJsonFeature feature : layer.getFeatures()) {
                if (feature != null) {
                    if (type != null && type.equals(AppConstants.AREA_TYPE.Farm)) {
                        if (feature.hasGeometry()) {
                            Geometry geometry = feature.getGeometry();
                            latLngList.addAll(getCoordinatesFromGeometry(geometry));
                        }
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
            try {
                String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("user_id", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                jsonObject.addProperty("token", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                jsonObject.addProperty("comp_id", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                jsonObject.addProperty("farm_id", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                jsonObject.addProperty("type", "" + AppConstants.AREA_TYPE.Farm);
//                jsonObject.addProperty("farm_id","20202");
                Call<GeoJsonResponse> statusMsgModelCall = apiService.getFarmGeoJson(jsonObject);
                Log.e(TAG, "Sending Data " + new Gson().toJson(jsonObject));
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<GeoJsonResponse>() {
                    @Override
                    public void onResponse(Call<GeoJsonResponse> call, retrofit2.Response<GeoJsonResponse> response) {
                        String error = null;
                        isLoaded[0] = true;
                        if (response.isSuccessful()) {
                            Log.e("Area Response", new Gson().toJson(response.body()));
                            GeoJsonResponse statusMsgModel = response.body();
                            if (statusMsgModel.getStatus() == 1) {
                                if (statusMsgModel.getData() != null && statusMsgModel.getData().size() > 0) {
                                    for (int i = 0; i < statusMsgModel.getData().size(); i++) {
                                        String type = statusMsgModel.getData().get(i).getType();
                                        if (type != null && type.equals(AppConstants.AREA_TYPE.Farm)) {
                                            getGeoJson(statusMsgModel.getData().get(i).getDetails(), type);
                                            break;
                                        }
                                    }
                                }
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {

                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired

                            } else {

                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access

                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired

                        } else {
                            try {
                                error = response.errorBody().string().toString();

                                Log.e(TAG, "Error " + error);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<GeoJsonResponse> call, Throwable t) {
                        Log.e(TAG, "Failure " + t.toString());

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


    boolean check = true;


    public Bitmap addWatermark(Bitmap source) {
        int w, h;
        Canvas c;
        Paint paint;
        Bitmap bmp, watermark;
        Matrix matrix;
        float scale;
        RectF r;
        w = source.getWidth();
        h = source.getHeight();
        // Create the new bitmap
        bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
        // Copy the original bitmap into the new one
        c = new Canvas(bmp);
        c.drawBitmap(source, 0, 0, paint);
        // Load the watermark
        watermark = BitmapFactory.decodeResource(resources, R.drawable.ct_water_mark);
        // Scale the watermark to be approximately 40% of the source image height
        scale = (float) (((float) h * 0.15) / (float) watermark.getHeight());
        // Create the matrix
        matrix = new Matrix();
        matrix.postScale(scale, scale);
        // Determine the post-scaled size of the watermark
        r = new RectF(0, 0, watermark.getWidth(), watermark.getHeight());
        matrix.mapRect(r);
        // Move the watermark to the bottom right corner
        matrix.postTranslate(w - r.width(), h - r.height());
        // Draw the watermark
        c.drawBitmap(watermark, matrix, paint);
        // Free up the bitmap memory
        watermark.recycle();
        return bmp;
    }

    public int convertToPixels(int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f);

    }

    private int checkSize(Paint addedBy, Rect textRec, int imageWidth, int textSize, String watermarkText) {

/*
        int width = 0;
        if (textRec.width() >= (imageWidth - 4) / 3)     //the padding on either sides is considered as 4, so as to appropriately fit in the text
        {

            checkSize(addedBy, textRec, imageWidth, textSize, watermarkText);
        }

*/

        while (textRec.width() >= (imageWidth - 4) / 3) {
            addedBy.getTextBounds(watermarkText, 0, watermarkText.length(), textRec);
            textSize--;
            addedBy.setTextSize(textSize);

            Log.e("checkSize", "TextRect: " + textRec.width() + ", ImageWidth: " + (imageWidth - 4) / 3);
            Log.e("checkSize", "Textsize " + textSize);
        }
        Log.e("checkSize", "TextRectO: " + textRec.width() + ", ImageWidth: " + (imageWidth - 4) / 3);
        Log.e("checkSize", "TextsizeO " + textSize);

        return textSize;

    }


    private Bitmap addWaterMark(Bitmap resource) {
        String userName = "Clicked by: " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NAME);
        Bitmap pikedPhoto = resource.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(pikedPhoto);
        Paint paintCenter = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
        paintCenter.setStyle(Paint.Style.FILL);
        paintCenter.setColor(Color.RED);
        //float size = convertDpToPx(context, 25);
//        paintCenter.setTextSize(50); //Text Size
        paintCenter.setTextAlign(Paint.Align.CENTER);
        float textY = pikedPhoto.getHeight();
        float textX = pikedPhoto.getWidth() / 2;
        String text1 = "Image might not be from farm";
        Rect textRect = new Rect();
       /* if (message!=null&&!TextUtils.isEmpty(message)){
            text1=message;
            Log.e("checkSize","message1 "+message);
        }else if (message2!=null&&!TextUtils.isEmpty(message2)){
            text1=message2;
            Log.e("checkSize","message2 "+message2);
        }else if (message3!=null&&!TextUtils.isEmpty(message3)){
            text1=message3;
            Log.e("checkSize","message3 "+message3);
        }*/
        paintCenter.getTextBounds(text1, 0, text1.length(), textRect);
        int s = checkSize(paintCenter, textRect, (canvas.getWidth() - 4), 50, text1);
        Log.e("checkSize", "textsize " + s);
        canvas.drawText(message, textX + 5, textY - 2 * s - 5, paintCenter);
        canvas.drawText(message2, textX + 5, textY - s - 5, paintCenter);
        canvas.drawText(message3, textX + 5, textY - 5, paintCenter);

        String text2 = "Left Text";
        Paint paintLeft = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
        paintLeft.setStyle(Paint.Style.FILL);
        paintLeft.setColor(Color.WHITE);
        //float sizeLeft = convertDpToPx(context, 25);
        paintLeft.setTextAlign(Paint.Align.LEFT);
        float textYL = pikedPhoto.getHeight();

        if (text1 == null || TextUtils.isEmpty(text1)) {
            Rect textRectL = new Rect();
            paintLeft.getTextBounds(userName, 0, userName.length(), textRectL);
            int sL = checkSize(paintLeft, textRectL, (canvas.getWidth() - 4), 50, userName);
            s = sL;
        }
        paintLeft.setTextSize(s);
        canvas.drawText(userName, 10, textYL - 2 * s - 5, paintLeft);
        canvas.drawText("Lat: " + imgLat, 10, textYL - s - 5, paintLeft);
        canvas.drawText("Long: " + imgLong, 10, textYL - 5, paintLeft);
        // in this case, center.x and center.y represent the coordinates of the center of the rectangle in which the text is being placed
        pikedPhoto = addWatermark(pikedPhoto);

        int f = (s * 2) + 10;

        /* Bitmap waterMark = BitmapFactory.decodeResource(context.getResources(), R.drawable.ct_water_mark);
        Log.e(TAG, "Canvas" + canvas.getWidth() + " " + canvas.getHeight());
        if (canvas.getWidth() > canvas.getHeight()) {
            canvas.drawBitmap(waterMark, f, (9 * (canvas.getHeight() / 10) - 200), null);
            Log.e(TAG, "CanvasW" + (8 * (canvas.getWidth() / 10) - 100) + " " + (9 * (canvas.getHeight() / 10) - 200));

        } else {
            canvas.drawBitmap(waterMark, (8 * (canvas.getWidth() / 10) - 50), (9 * (canvas.getHeight() / 10) - 90), null);
            Log.e(TAG, "CanvasH" + (8 * (canvas.getWidth() / 10) - 50) + " " + (9 * (canvas.getHeight() / 10) - 90));
        }*/


        return pikedPhoto;
    }


    private Bitmap addWaterMark(Bitmap src, String watermarkText) {
        src = addWatermark(src);
        float scale = resources.getDisplayMetrics().density;
        int w = src.getWidth();
        int h = src.getHeight();
        String longitude = imgLat;
        String latitude = imgLong;
        if (currentLocation != null) {
            latitude = String.valueOf(currentLocation.getLatitude());
            longitude = String.valueOf(currentLocation.getLongitude());
        }
        Bitmap result = src.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        Paint addedBy = new Paint();
        //apply color
        addedBy.setColor(resources.getColor(R.color.white));
        addedBy.setTextSize(convertToPixels(12));
        addedBy.setAntiAlias(true);
        addedBy.setUnderlineText(true);
        int y = canvas.getHeight() - 200;
        Rect textRect = new Rect();
        addedBy.getTextBounds(watermarkText, 0, watermarkText.length(), textRect);
        checkSize(addedBy, textRect, (canvas.getWidth() - 4), 50, watermarkText);
       /* if(textRect.width() >= (canvas.getWidth() - 4)/3)     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            addedBy.setTextSize(convertToPixels( 7));*/
        canvas.drawText("Clicked By: " + watermarkText, 0, y, addedBy);
        Paint lat = new Paint();
        //apply color
        lat.setColor(resources.getColor(R.color.white));
        lat.setTextSize((int) 12 * scale);
        lat.setAntiAlias(true);
        lat.setUnderlineText(true);
        int y2 = canvas.getHeight() - 125;
        lat.setColorFilter(new ColorFilter());
        canvas.drawText("Latitude: " + latitude, 20, y, lat);
        Paint lng = new Paint();
        //apply color
        lng.setColor(resources.getColor(R.color.white));
        lng.setTextSize((int) 12 * scale);
        lng.setAntiAlias(true);
        lng.setUnderlineText(true);
        int y3 = canvas.getHeight() - 50;
        canvas.drawText("Longitude: " + longitude, 20, y3, lng);
        if (message != null && !TextUtils.isEmpty(message) && message2 != null && !TextUtils.isEmpty(message2)) {
            String mText = message;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(getResources().getColor(R.color.red));
            paint.setTextSize((int) (50));
            paint.setTextAlign(Paint.Align.CENTER);
            Rect bounds = new Rect();
            paint.getTextBounds(mText, 0, mText.length(), bounds);
            canvas.drawText(mText, src.getWidth() / 2, y, paint);

            String mText2 = message2;
            Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint2.setColor(getResources().getColor(R.color.red));
            paint2.setTextSize((int) (50));
            paint2.setTextAlign(Paint.Align.CENTER);
            Rect bounds2 = new Rect();
            paint2.getTextBounds(mText2, 0, mText2.length(), bounds2);
            canvas.drawText(mText2, src.getWidth() / 2, y2, paint2);
            check = false;
        }
        if (message3 != null && !TextUtils.isEmpty(message3)) {
            String mText2 = message3;
            Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint2.setColor(getResources().getColor(R.color.red));
            paint2.setTextSize((int) (50));
            paint2.setTextAlign(Paint.Align.CENTER);
            Rect bounds2 = new Rect();
            paint2.getTextBounds(mText2, 0, mText2.length(), bounds2);
            canvas.drawText(mText2, src.getWidth() / 2, y3, paint2);
        }

       /* Bitmap waterMark = BitmapFactory.decodeResource(context.getResources(), R.drawable.ct_water_mark);
        Log.e(TAG, "Canvas" + canvas.getWidth() + " " + canvas.getHeight());
        if (canvas.getWidth() > canvas.getHeight()) {
            canvas.drawBitmap(waterMark, (9 * (canvas.getWidth() / 10) - 50), (9 * (canvas.getHeight() / 10) - 200), null);
            Log.e(TAG, "CanvasW" + (8 * (canvas.getWidth() / 10) - 100) + " " + (9 * (canvas.getHeight() / 10) - 200));

        } else {
            canvas.drawBitmap(waterMark, (8 * (canvas.getWidth() / 10) - 50), (9 * (canvas.getHeight() / 10) - 90), null);
            Log.e(TAG, "CanvasH" + (8 * (canvas.getWidth() / 10) - 50) + " " + (9 * (canvas.getHeight() / 10) - 90));
        }*/

        return result;
    }


    private String convertAreaTo(String area) {
        String convertArea = area;
        convertArea = String.format("%.2f", parseDouble(area));
        return convertArea;
    }


    public Bitmap compressBitmap(Bitmap selectedBitmap, int sampleSize, int quality, int index) {
        try {
            ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStreamObject);
            byteArrayOutputStreamObject.close();
            long lengthInKb = byteArrayOutputStreamObject.toByteArray().length / 1024; //in kb

            Log.e(TAG, "Image size at " + index + " " + lengthInKb + "Kb");
            if (lengthInKb > 400) {
                compressBitmap(selectedBitmap, (sampleSize * 2), (quality / 2), index);
            } else {

                img_list[index] = Base64.encodeToString(byteArrayOutputStreamObject.toByteArray(), Base64.DEFAULT);
                ;
                return selectedBitmap;
            }

            //selectedBitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectedBitmap;
    }

    private Bitmap compressImage(Bitmap image, int index) {
        Bitmap bitmap = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//Compression quality, here 100 means no compression, the storage of compressed data to baos
            int options = 90;
            while (baos.toByteArray().length / 1024 > 400) {  //Loop if compressed picture is greater than 400kb, than to compression
                baos.reset();//Reset baos is empty baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//The compression options%, storing the compressed data to the baos
                options -= 10;//Every time reduced by 10
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//The storage of compressed data in the baos to ByteArrayInputStream
            bitmap = BitmapFactory.decodeStream(isBm, null, null);//The ByteArrayInputStream data generation

            ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStreamObject);
            byteArrayOutputStreamObject.close();

            img_list[index] = Base64.encodeToString(byteArrayOutputStreamObject.toByteArray(), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101) {

            } else if (requestCode == SELECT_FILE1) {
                String result = "capture1";
                //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                onSelectFromGalleryResult(data, result);
            } else if (requestCode == SELECT_FILE2) {
                String result = "capture2";
                // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                onSelectFromGalleryResult(data, result);
            } else if (requestCode == SELECT_FILE3) {
                String result = "capture3";
                // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                onSelectFromGalleryResult(data, result);
            } else if (requestCode == SELECT_FILE4) {
                String result = "capture4";
                // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                onSelectFromGalleryResult(data, result);
            } else if (requestCode == SELECT_FILE5) {
                String result = "capture5";
                // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                onSelectFromGalleryResult(data, result);
            } else if (requestCode == SELECT_FILE6) {
                String result = "capture6";
                // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                onSelectFromGalleryResult(data, result);
            } else if (requestCode == REQUEST_CAMERA1) {
                String result = "capture1";
                // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                onCaptureImageResult(data, result);
            } else if (requestCode == REQUEST_CAMERA2) {
                String result = "capture2";
                // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                onCaptureImageResult(data, result);
            } else if (requestCode == REQUEST_CAMERA3) {
                String result = "capture3";
                // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                onCaptureImageResult(data, result);
            } else if (requestCode == REQUEST_CAMERA4) {
                String result = "capture4";
                // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                onCaptureImageResult(data, result);
            } else if (requestCode == REQUEST_CAMERA5) {
                String result = "capture5";
                // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                onCaptureImageResult(data, result);
            } else if (requestCode == REQUEST_CAMERA6) {
                String result = "capture6";
                // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                onCaptureImageResult(data, result);
            }

        } else {
            Log.e("MYLOCATIONTAG", "Main Result not ok");
            //Toast.makeText(context, "Result Not ok", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onVisitIdLoaded(Visit visit) {
        if (visit != null) {
            lastVisitReportId = Integer.parseInt(visit.getVrId()) + 1;
        }
    }

    @Override
    public void onTimelineLoaded(Timeline timeline) {

        if (timeline != null) {
            this.timeline = timeline;
            Gson gson = new Gson();
            CalendarJSON calendarJSON = gson.fromJson(timeline.getJsondata(), CalendarJSON.class);
            if (calendarJSON != null && calendarJSON.getCalendarData() != null && calendarJSON.getCalendarData().size() > 0) {
                calendarData.addAll(calendarJSON.getCalendarData());
            }
        } else {
            this.timeline = new Timeline(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), null, null);
            TimelineCacheManager.getInstance(context).addTimeline(context, this.timeline);
        }
    }

    @Override
    public void onTimelineAdded() {
        TimelineCacheManager.getInstance(context).getAllTimeline(this, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));

    }

    @Override
    public void onTimelineInsertError(Throwable e) {

    }

    @Override
    public void onNoFarmsAvailable() {

    }

    @Override
    public void TimelineDeleted() {

    }

    @Override
    public void TimelineDeletionFaild() {

    }

    @Override
    public void onChemicalUnitLoaded(List<DropDownT> chemicalUnitList) {

    }

    @Override
    public void onFarmLoader(Farm farm) {
        this.farm = farm;
        latLngList.clear();
        if (farm.getCoords() != null) {
            try {
                JSONArray arrayOffline = new JSONArray(farm.getCoords());
                for (int j = 0; j < arrayOffline.length(); j++) {

                    JSONObject object = arrayOffline.getJSONObject(j);
                    LatLngFarm data = new Gson().fromJson(object.toString(), LatLngFarm.class);
                    LatLng latLng = new LatLng(Double.valueOf(data.getLng()), Double.valueOf(data.getLng()));
                    latLngList.add(latLng);
                }
            } catch (JSONException e) {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void uploadVisitData(String... strings) {
//        CPCList cpc = (CPCList) cpcSpinner.getSelectedItem();
        String comment = null;

        comment = commentEt.getText().toString().trim();

        String[] imageLis = new String[imageList.size()];
        for (int i = 0; i < img_list.length; i++) {
            if (img_list[i] != null) {
                imageLis[i] = img_list[i];
            }
        }
        Log.e("TimelineAddVisit", "Length " + img_list.length);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        final String oldVetting = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARM_VETTING);
        AddVisitSendDataNew addVisitSendData = new AddVisitSendDataNew();
        int visit_count = SharedPreferencesMethod.getInt(context, SharedPreferencesMethod.VISIT_COUNT);
        addVisitSendData.setFarm_id(Integer.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID)));
        addVisitSendData.setComp_id(Integer.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID)));
        addVisitSendData.setApproved_method("images");
        addVisitSendData.setEventType("dr");
        addVisitSendData.setDelQParam(selectedReason.getParameters());
        addVisitSendData.setDelQId(selectedReason.getId());
        addVisitSendData.setComment(comment);
        addVisitSendData.setVisit_date(formattedDate);
//        addVisitSendData.setVisit_number(visit_count + 1);
        addVisitSendData.setAdded_by(Integer.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID)));
//        addVisitSendData.setFarm_grade(cpc.getName());
        addVisitSendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        addVisitSendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));

        Log.e(TAG, "Send Visit Dataa " + new Gson().toJson(addVisitSendData));
        addVisitSendData.setImageList(imageLis/*new String[]{}*/);//img_list
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
        if (connected) {
            try {
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
                Call<StatusMsgModel> statusMsgModelCall = apiService.getVisitMsgStatusNew(addVisitSendData);
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<StatusMsgModel>() {
                    @Override
                    public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                        String error = null;
                        isLoaded[0] = true;
                        if (response.isSuccessful()) {
                            Log.e(TAG, "Response " + new Gson().toJson(response.body()));

                            if (response != null) {
                                StatusMsgModel statusMsgModel = response.body();
                                if (response.body().getStatus() == 10) {
                                    updateVettingStatus(oldVetting, false);
                                    submitButton.setClickable(true);
                                    submitButton.setEnabled(true);
                                    hideProgress();
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(context, response.body().getMsg());
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                    //un authorized access
                                    updateVettingStatus(oldVetting, false);
                                    submitButton.setClickable(true);
                                    submitButton.setEnabled(true);
                                    hideProgress();
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                    // auth token expired
                                    updateVettingStatus(oldVetting, false);
                                    submitButton.setClickable(true);
                                    submitButton.setEnabled(true);
                                    hideProgress();
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                                } else if (statusMsgModel.getStatus() != 0) {

                                    progressDialog.dismiss();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
//                                            Toast.makeText(context, resources.getString(R.string.visit_submit_success_msg), Toast.LENGTH_LONG).show();
                                            Toast.makeText(context, "Delinquent record added successfully", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                    finish();
                                    startActivity(new Intent(context, LandingActivity.class));
                                    finishAffinity();

                                } else {
                                    updateVettingStatus(oldVetting, false);
                                    submitButton.setClickable(true);
                                    submitButton.setEnabled(true);
                                    hideProgress();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.cancel();
                                            Intent intent = new Intent(context, LandingActivity.class);
                                            progressDialog.dismiss();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(context, "Failed to add delinquent record, Please try again", Toast.LENGTH_LONG).show();
                                                }
                                            });

                                            //ViewFailDialog viewFailDialog = new ViewFailDialog();
                                            //viewFailDialog.showDialog(TimelineAddVisitActivity.this, response.body().getMsg());
                                        }
                                    });
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateVettingStatus(oldVetting, false);
                                        submitButton.setClickable(true);
                                        hideProgress();
                                        submitButton.setEnabled(true);
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Failed to add delinquent record, Please try again", Toast.LENGTH_LONG).show();

                                    }
                                });
                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access

                            progressDialog.dismiss();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            progressDialog.dismiss();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                        } else {
                            try {
                                updateVettingStatus(oldVetting, false);
                                submitButton.setClickable(true);
                                submitButton.setEnabled(true);
                                hideProgress();
                                error = response.errorBody().string().toString();
                                Log.e(TAG, "Error " + error);
                                progressDialog.dismiss();
                                //Toast.makeText(context, "Oops something went wrong. Please try again", Toast.LENGTH_LONG).show();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                Toast.makeText(context, "Failed to add delinquent record, Please try again", Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                            notifyApiDelay(context, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                        long newMillis = AppConstants.getCurrentMills();
                        updateVettingStatus(oldVetting, false);
                        hideProgress();
                        submitButton.setClickable(true);
                        submitButton.setEnabled(true);
                        long diff = newMillis - currMillis;
                        isLoaded[0] = true;
                        notifyApiDelay(context, call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);
                        if (t instanceof SocketTimeoutException) {
                            uploadVisitData();
                        } else {
                            progressDialog.dismiss();
                            //Toast.makeText(TimelineAddVisitActivity.this, "Failed to submit visit. Please try again", Toast.LENGTH_LONG).show();
                            Toast.makeText(context, "Failed to add delinquent record, Please try again", Toast.LENGTH_LONG).show();
                        }
                        Log.e("onFailure", t.toString());
                        // progressBar_cyclic.setVisibility(View.INVISIBLE);
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
        } else {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    viewFailDialog.showDialog(context, resources.getString(R.string.internet_not_connected));
                }
            });

        }
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
                            ConnectivityUtils.checkSpeedWithColor(context, new ConnectivityUtils.ColorListener() {
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

    String internetSPeed = "";

    private void saveVisitOffline() {

        String lastVrId = String.valueOf(lastVisitReportId);
        if (lastVrId.equals("0")) {
            lastVrId = "1";
        }
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDateStr = df.format(c);

        List<VisitImageTimeline> imageTimelineList = new ArrayList<>();
        List<DoneActivityImage> doneActivityImages = new ArrayList<>();
        List<AgriInput> agriInputList = new ArrayList<>();
        VisitReportTimeline visitReportTimeline = new VisitReportTimeline();
        for (int i = 0; i < picturePathList.size(); i++) {
            VisitImageTimeline imageTimeline = new VisitImageTimeline();
            imageTimeline.setImgLink(picturePathList.get(i));
            DoneActivityImage activityImage = new DoneActivityImage();
            activityImage.setImgLink(picturePathList.get(i));
            doneActivityImages.add(activityImage);
            imageTimeline.setVisitReportId(lastVrId);
            imageTimeline.setPldId("0");
            try {
                imageTimeline.setLat(imageLatitude.get(i));
                imageTimeline.setLon(imageLongitude.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
            imageTimelineList.add(imageTimeline);
        }
        String comment = null;
//        CPCList cpc = (CPCList) cpcSpinner.getSelectedItem();
//        grade=cpc.getName();
        comment = commentEt.getText().toString().trim();

        DoneActivityResponseNew doneData = new DoneActivityResponseNew();
        doneData.setImages(doneActivityImages);
        doneData.setComment(comment);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String doa = sdf.format(cal.getTime());
        visitReportTimeline.setVisitDate(todayDateStr);
        visitReportTimeline.setVisitReportId(lastVrId);
        visitReportTimeline.setVisitNumber("0");

        VisitResponseNew visit = new VisitResponseNew();
        visit.setShowResponse(doneData);
        visit.setComment(comment);
        visit.setAgriInputList(agriInputList);
        visit.setVisitImages(imageTimelineList);
        visit.setVisitReport(visitReportTimeline);
        visit.setDoa(doa);
        visit.setFarmId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
        Log.d(TAG, "Doa DATE : " + doa);
        Log.e(TAG, "Offline Data " + new Gson().toJson(visit));
        Visit vr = new Visit(lastVrId,
                new Gson().toJson(visit), "N", null, "Y", doa, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
        VrCacheManager.getInstance(context).addVisits(vr);
        String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);

        TimelineInnerData innerData = new TimelineInnerData(null, lastVrId, farmId, null, null, todayDateStr, null, null, "", "", "", "", "vr");
        innerData.setImg_link(picturePathList.get(0));
//        Toast.makeText(context, "Position " + getIntent().getIntExtra("position", calendarData.size() - 1), Toast.LENGTH_SHORT).show();
        calendarData.add(/*getIntent().getIntExtra("position", calendarData.size() - 1),*/ innerData);

        CalendarJSON calendarJSON = new CalendarJSON();
        calendarJSON.setCalendarData(calendarData);
        timeline.setJsondata(new Gson().toJson(calendarJSON));
        TimelineCacheManager.getInstance(context).update(timeline);
        farm.setIsUpdated("Y");
        farm.setIsUploaded("N");
        FarmCacheManager.getInstance(context).updateFarm(farm);
        progressDialog.cancel();
        Toasty.success(context, "Visit Submitted", Toast.LENGTH_SHORT, false).show();
        progressDialog.cancel();
        finishAllAndStartLanding();

    }

    int activityIndex = -1;
    TimelineInnerData timelineInnerDataSelecetd;
    boolean isDataSubmittedOnce = false;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isDataSubmittedOnce)
            setResult(RESULT_OK);
    }

    List<CompAgriDatum> compAgriDatumList = new ArrayList<>();

    private void getCompAgriInputs() {
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getCompAgriInputs(compId, userId, token).enqueue(new Callback<CompAgriResponse>() {
            @Override
            public void onResponse(Call<CompAgriResponse> call, Response<CompAgriResponse> response) {
                String error = null;
                if (response.isSuccessful()) {
                    try {
                        Log.e(TAG, "GetCompAgri " + new Gson().toJson(response.body()));
//                        Log.e(TAG, "Farmer Farms Resp " + response.body().string());
                        if (response.body().getData() != null) {
//                            agriInputAdapter.setCompAgriDatumList(response.body().getData());
                            compAgriDatumList.addAll(response.body().getData());
                        } else {
//                            fabChildAddAgriInput.hide(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "Farmer Farm Err " + error);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_load_farm));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
                    notifyApiDelay(context, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<CompAgriResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(context, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "Farmer Farm Err " + t.toString());
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialog(context, resources.getString(R.string.failed_load_farm));
            }
        });
        internetFlow(isLoaded[0]);

    }

    private void getAllDataNew(final String type) {
        ParamData paramData = new ParamData();
        paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        paramData.setType(type);
        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        Log.e(TAG, "Sending New Param  AND type " + type + " " + new Gson().toJson(paramData));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getNewParameters(paramData).enqueue(new Callback<DDResponseNew>() {
            @Override
            public void onResponse(Call<DDResponseNew> call, Response<DDResponseNew> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    DDResponseNew response = response1.body();
                    Log.e(TAG, "Sending New Param Response " + type + "  " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, response.getMsg());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        if (response != null && response.getData() != null && response.getData().size() > 0) {
                            DropDownCacheManager2.getInstance(context).deleteViaType(type);
                            DropDownT2 material = new DropDownT2(type, new Gson().toJson(response));
                            DropDownCacheManager2.getInstance(context).addChemicalUnit(material);
                            ddNewArrayList.addAll(response.getData());
                            spinnerAdapter.notifyDataSetChanged();
                        }
                    }
                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "Cache Error " + error + " Code " + response1.code());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DDResponseNew> call, Throwable t) {
                Log.e("OfflineModeAct", "Failure getOfflineData " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
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

    private void hideProgress(){
        try {
            if (progressDialog!=null&&progressDialog.isShowing())
                progressDialog.hide();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}