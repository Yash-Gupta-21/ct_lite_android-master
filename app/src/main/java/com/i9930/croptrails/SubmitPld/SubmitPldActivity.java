package com.i9930.croptrails.SubmitPld;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import com.i9930.croptrails.AddFarm.Model.FarmData;
import com.i9930.croptrails.AddFarm.Model.FarmerAndFarmData;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonAdapters.PldSpinner.PldSpinnerAdapter;
import com.i9930.croptrails.CommonClasses.PldReason;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.FarmDetails.Model.Visit.VisitImageTimeline;
import com.i9930.croptrails.HarvestSubmit.EasyMode.HarvestSubmitEasyModeActivity;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Maps.ShowArea.Model.GetGpsCoordinates;
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;
import com.i9930.croptrails.Maps.ShowArea.Model.geojson.GeoJsonResponse;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownCacheManager;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownFetchListener;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmCacheManager;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmLoadListener;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerCacheManager;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerT;
import com.i9930.croptrails.RoomDatabase.Pld.OfflinePld;
import com.i9930.croptrails.RoomDatabase.Pld.PldCacheManager;
import com.i9930.croptrails.SubmitPld.Model.PldReasonResponse;
import com.i9930.croptrails.SubmitPld.Model.SendPldData;
import com.i9930.croptrails.Test.PldModel.PldData;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;
import static com.i9930.croptrails.Utility.AppConstants.getCoordinatesFromGeometry;

public class SubmitPldActivity extends AppCompatActivity implements FarmLoadListener {

    String pld;
    @BindView(R.id.reasonTi)
    TextInputLayout reasonTiLayout;
    @BindView(R.id.pldReasonSpinner)
    Spinner pldReasonSpinner;
    @BindView(R.id.pld_date)
    EditText pld_date;
    @BindView(R.id.pld_area_in_acre)
    EditText pld_area_in_acre;
    @BindView(R.id.pld_reason_for_damage)
    EditText pld_reason_for_damage;
    @BindView(R.id.submit_pld_area)
    TextView submit_pld_area;
    @BindView(R.id.progressBar_cyclic)
    ProgressBar progressBar_cyclic;
    @BindView(R.id.eff_farm_area_tv)
    TextView eff_farm_area_tv;
    SubmitPldActivity context;
    Toolbar mActionBarToolbar;
    int i = 0;
    double oldPldAcers = 0;
    TextView title;
    @BindView(R.id.pld_already_done_msg_tv)
    TextView pld_already_done_msg_tv;
    Resources resources;
    double multiplicationFactor = 1.0;
    Double actualArea = 0.0;
    Double standingarea = 0.0;
    String TAG = "SubmitPldActivity";
    Farm farm;
    @BindView(R.id.pldFormLayout)
    RelativeLayout pldFormLayout;
    List<PldData>plDataList=new ArrayList<>();
    private void intitViews() {
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        title = (TextView) findViewById(R.id.tittle);
    }
    List<Bitmap> imageList;
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
    ViewFailDialog viewFailDialog=new ViewFailDialog();
    String image_capture_date1 = "", image_capture_date2 = "", image_capture_date3 = "", image_capture_date4 = "", image_capture_date5 = "", image_capture_date6 = "";
    private int SELECT_FILE1 = 1, SELECT_FILE2 = 2, SELECT_FILE3 = 3, SELECT_FILE4 = 4, SELECT_FILE5 = 5, SELECT_FILE6 = 6;
    private int REQUEST_CAMERA1 = 11, REQUEST_CAMERA2 = 12, REQUEST_CAMERA3 = 13, REQUEST_CAMERA4 = 14, REQUEST_CAMERA5 = 15, REQUEST_CAMERA6 = 16;
    List<String> picturePathList = new ArrayList<>();
    String userChoosenTask = "";
    String pictureImagePath = "";
    Bitmap myBitmap;
    String message = "";
    String message2 = "";
    String message3="";

    String farmLatitude = "";
    String farmLongitude = "";
    Location currentLocation;
    String[] img_list = new String[6];
    List<LatLng>latLngList=new ArrayList<>();
    @BindView(R.id.images_to_upload_lieanr_lay)
    LinearLayout images_to_upload_lieanr_lay;
    @BindView(R.id.parentPldRelLayout)
    RelativeLayout parentPldRelLayout;
    List<String> imageLatitude=new ArrayList<>();
    List<String> imageLongitude=new ArrayList<>();

    @BindView(R.id.connectivityLine)
    View connectivityLine;
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
        setContentView(R.layout.activity_submit_pld);
        context = this;
        loadAds();
        ButterKnife.bind(this);
        farmLatitude=SharedPreferencesMethod.getString(context,SharedPreferencesMethod.FARM_LAT);
        farmLongitude=SharedPreferencesMethod.getString(context,SharedPreferencesMethod.FARM_LONG);
        imageList = new ArrayList<>();
        final String languageCode = SharedPreferencesMethod2.getString(SubmitPldActivity.this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        intitViews();
        setTitleBar();

        imageLatitude.add("");
        imageLatitude.add("");
        imageLatitude.add("");
        imageLatitude.add("");

        imageLongitude.add("");
        imageLongitude.add("");
        imageLongitude.add("");
        imageLongitude.add("");

        //multiplicationFactor=SharedPreferencesMethod.getDoubleSharedPreferences(context, SharedPreferencesMethod.AREA_MULTIPLICATON_FACTOR);
       /* if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL).toLowerCase().equals("hectare")) {
            multiplicationFactor = 2.47105;
        } else if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL).toLowerCase().equals("malwa_bigha")) {
            multiplicationFactor = 3.63;
        } else {

        }*/
        // progressBar_cyclic.setVisibility(View.VISIBLE);

        onImageClick();
        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            FarmCacheManager.getInstance(context).getFarm(this::onFarmLoader,SharedPreferencesMethod.getString(context,SharedPreferencesMethod.SVFARMID));
            DropDownCacheManager.getInstance(context).getAllChemicalUnits(new DropDownFetchListener() {
                @Override
                public void onChemicalUnitLoaded(List<DropDownT> dropDownList) {
                    if (dropDownList != null && dropDownList.size() > 0) {
                        for (int i = 0; i < dropDownList.size(); i++) {
                            if (AppConstants.CHEMICAL_UNIT.PLD_REASON.trim().equals(dropDownList.get(i).getDataType().trim())) {
                                //Units

                                List<PldReason> pldReasons = new ArrayList<>();
                                PldReason pldReason = new PldReason();
                                pldReason.setName(resources.getString(R.string.select_loss_damage_reason_prompt));
                                pldReasons.add(pldReason);
                                if (dropDownList.get(i).getData() != null) {
                                    try {
                                        JSONArray array = new JSONArray(dropDownList.get(i).getData());
                                        for (int j = 0; j < array.length(); j++) {
                                            JSONObject object = array.getJSONObject(j);
                                            PldReason dataDD = new Gson().fromJson(object.toString(), PldReason.class);
                                            pldReasons.add(dataDD);
                                        }
                                        Log.e(TAG,"Pld resons "+new Gson().toJson(pldReasons));
                                        PldReason otherReason = new PldReason();
                                        otherReason.setName(resources.getString(R.string.other_labe));
                                        pldReasons.add(otherReason);
                                        lastPosition = pldReasons.size() - 1;
                                        PldSpinnerAdapter adapter = new PldSpinnerAdapter(context, pldReasons);
                                        pldReasonSpinner.setAdapter(adapter);
                                        isOtherReason = false;
                                        progressBar_cyclic.setVisibility(View.GONE);
                                        pldFormLayout.setVisibility(View.VISIBLE);
                                        pldReasonSpinner.setVisibility(View.VISIBLE);
                                        reasonTiLayout.setVisibility(View.GONE);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }
                }
            });
        } else {
            new GetGeoJsonData().execute();
            getPldReasons();
        }
        onClicks();


        if (!TextUtils.isEmpty(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA))) {
            actualArea = Double.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA));
        }

        if (!TextUtils.isEmpty(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES))) {
            standingarea = Double.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES));
        }
        if (actualArea > 0) {
        } else {
            noAcrualAreaEvent("actual");
        }

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


    }

    Calendar calendar = Calendar.getInstance();

    private void updatesowingDateLabel() {

        String myFormat = AppConstants.DATE_FORMAT_SERVER; //In which you need put here

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        //et_exp_date.setText(sdf.format(expense_date.getTime()));
        pld_date.setText(AppConstants.getShowableDate(sdf.format(calendar.getTime()),context));
        pld_date.setError(null);
        //et_exp_date.setError(null);

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
    private void onImageClick() {
        farm_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage("capture1");
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
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(AppConstants.DATE_FORMAT_SERVER);
        String todayDateStr = df.format(c);
        pld_date.setText(AppConstants.getShowableDate(todayDateStr,context));

        final DatePickerDialog.OnDateSetListener datesowing = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updatesowingDateLabel();
            }
        };

        submit_pld_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPldClickEvent();
            }
        });
        pld_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, datesowing, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }
        });

        pldReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*try {
                    ((TextView) parent.getChildAt(0)).setVisibility(View.GONE);
                }catch (Exception e){
                    e.printStackTrace();
                }*/
                if (position == lastPosition) {
                    isOtherReason = true;
                    reasonTiLayout.setVisibility(View.VISIBLE);
                } else {
                    isOtherReason = false;
                    reasonTiLayout.setVisibility(View.GONE);
                    pld_reason_for_damage.setText(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    int lastPosition = 0;

    private void setTitleBar() {
        title.setText(resources.getString(R.string.pld_form_title));
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void noAcrualAreaEvent(String from) {

        String msg = "";
        if (from.trim().toLowerCase().equals("actual")) {
            //Actual
            msg = resources.getString(R.string.no_actual_area_available_go_to_capture);
        } else {
            //Standing
            msg = resources.getString(R.string.there_is_no_standing_area_avilable_to_enter_for_pld_label);
        }

        //Toast.makeText(context, "Go to capture area", Toast.LENGTH_LONG).show();
        submit_pld_area.setEnabled(false);
        pld_area_in_acre.setEnabled(false);
        pld_reason_for_damage.setEnabled(false);

        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_for_goto_capure);
        Window window1 = dialog.getWindow();
        window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        TextView msgTv = dialog.findViewById(R.id.msg_tv);
        msgTv.setText(msg);
        Button okButton = dialog.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dialog.show();
    }

    private void submitPldClickEvent() {
//        if (oldPldAcers > 0) {
//            Toasty.warning(context, resources.getString(R.string.already_pld_added), Toast.LENGTH_LONG, true).show();
//        } else {
        if (pld_area_in_acre.getText().toString().equals("") || Double.parseDouble(AppConstants.getUploadableArea(pld_area_in_acre.getText().toString().trim())) >
                Double.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).trim())) {
            pld_area_in_acre.setError(resources.getString(R.string.invalid_entry_error));
            Toasty.error(context, resources.getString(R.string.invalid_entry_error), Toast.LENGTH_LONG).show();
        } else if (isOtherReason && TextUtils.isEmpty(pld_reason_for_damage.getText().toString())) {
            pld_reason_for_damage.setError(resources.getString(R.string.invalid_entry_in_pld));
            Toasty.error(context, resources.getString(R.string.invalid_entry_in_pld), Toast.LENGTH_LONG).show();
        } else if (!isOtherReason && pldReasonSpinner.getSelectedItemPosition() == 0) {
            //pld_reason_for_damage.setError(resources.getString(R.string.invalid_entry_in_pld));
            Toasty.error(context, resources.getString(R.string.invalid_entry_in_pld), Toast.LENGTH_LONG).show();
        }else if (/*!SharedPreferencesMethod.getBoolean(context,SharedPreferencesMethod.OFFLINE_MODE)&&*/imageList.size() < 4){
            images_to_upload_lieanr_lay.getParent().requestChildFocus(images_to_upload_lieanr_lay, images_to_upload_lieanr_lay);
            Snackbar snackbar = Snackbar.make(parentPldRelLayout, "Please capture all images", Snackbar.LENGTH_LONG);
            snackbar.show();
        }else {
            progressBar_cyclic.setVisibility(View.VISIBLE);
            SendDataAsyncTask sendDataAsyncTask = new SendDataAsyncTask();
            sendDataAsyncTask.execute();
        }
//        }

    }

    private String convertAreaToAcres(String area) {
        String convertArea = area;
        double a = Double.parseDouble(area) * multiplicationFactor;
        convertArea = String.valueOf(a);
        return convertArea;
    }

    private String convertAreaTo(String area) {
        return AppConstants.getShowableArea(area);
    }

    class SendDataAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pld = pld_area_in_acre.getText().toString().trim();
                }
            });


            submitPldData();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(context, "new pld "+pld, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE))
                Toasty.success(context, resources.getString(R.string.your_pld_data_update), Toast.LENGTH_SHORT, true).show();

        }
    }

    private void offlineModeSubmitPldData(SendPldData sendPldData) {
        List<VisitImageTimeline> imageTimelineList = new ArrayList<>();


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String todayDateStr = df.format(c);

        for (int i = 0; i < picturePathList.size(); i++) {
            VisitImageTimeline imageTimeline = new VisitImageTimeline();
            imageTimeline.setImgLink(picturePathList.get(i));
            imageTimeline.setVisitReportId("0");

            imageTimeline.setPldId(todayDateStr);


            try {
                imageTimeline.setLat(imageLatitude.get(i));
                imageTimeline.setLon(imageLongitude.get(i));
            }catch (Exception e){

            }
            imageTimelineList.add(imageTimeline);

        }
        sendPldData.setImageListOffline(imageTimelineList);
        sendPldData.setDoa(todayDateStr);
      /*  if (imageLongitude!=null&&imageLongitude!=null&&!TextUtils.isEmpty(imageLatitude)&&!TextUtils.isEmpty(imageLongitude)){
            sendPldData.setLat(""+currentLocation.getLatitude());
            sendPldData.setLon(""+currentLocation.getLongitude());
        }
        else if (currentLocation!=null){
            sendPldData.setLat(""+currentLocation.getLatitude());
            sendPldData.setLon(""+currentLocation.getLongitude());
        }*/
        Log.e(TAG,"OfflinepldLat "+new Gson().toJson(imageLatitude));
        Log.e(TAG,"OfflinepldLong "+new Gson().toJson(imageLongitude));
      Log.e(TAG,"Offlinepld "+new Gson().toJson(sendPldData));

        OfflinePld offlinePld=new OfflinePld(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.SVFARMID),
                new Gson().toJson(sendPldData),"Y","N",todayDateStr);

        PldCacheManager.getInstance(context).addPld(offlinePld);
        PldData pldData=new PldData(AppConstants.TIMELINE.PLD,"0",sendPldData.getFarm_id(),sendPldData.getPldArea(),
                sendPldData.getPldReasonId(),sendPldData.getPld_reason(),sendPldData.getPldDate(),sendPldData.getOtherReason(),
                sendPldData.getAddedBy(),todayDateStr,"Y",null,null,imageTimelineList,"Y",SharedPreferencesMethod.getString(context,SharedPreferencesMethod.USER_NAME));

        plDataList.add(pldData);
        farm.setOfflinePldJson(new Gson().toJson(plDataList));
        double oldPld=0;
        if (farm.getPldAcres()!=null&&!TextUtils.isEmpty(farm.getPldAcres())&&Double.valueOf(farm.getPldAcres())>0){
            oldPld=Double.valueOf(farm.getPldAcres().trim());
        }
        pld = AppConstants.getUploadableArea(pld_area_in_acre.getText().toString().trim());
        Double finalPld=Double.parseDouble(pld)+oldPld;
        double standingAcres = Double.parseDouble(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).trim());
        double pldAdded = Double.parseDouble(AppConstants.getUploadableArea(pld_area_in_acre.getText().toString().trim()));
        double newStandingAcers = standingAcres - pldAdded;
        SharedPreferencesMethod.setString(context,SharedPreferencesMethod.STANDING_ACRES,""+newStandingAcers);


        FarmData farmData = null;

        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            farmData = gson.fromJson(farm.getFarmFullData(), FarmData.class);
            farmData.setStandingArea(""+newStandingAcers);

                if (farmerT != null && AppConstants.isValidString(farmerT.getData())) {
                    FarmerAndFarmData farmerAndFarmData = null;
                    farmerAndFarmData = gson.fromJson(farmerT.getData(), FarmerAndFarmData.class);

                    if (farmerAndFarmData != null && farmerAndFarmData.getFarmDataList() != null && farmerAndFarmData.getFarmDataList().size() > 0) {
                        for (int i = 0; i < farmerAndFarmData.getFarmDataList().size(); i++) {
                            if (AppConstants.isValidString(farmerAndFarmData.getFarmDataList().get(i).getFarmId()) &&
                                    AppConstants.isValidString(farmData.getFarmId()) &&
                                    farmData.getFarmId().equalsIgnoreCase(farmerAndFarmData.getFarmDataList().get(i).getFarmId())) {
                                farmerAndFarmData.getFarmDataList().get(i).setStandingArea(""+newStandingAcers);
                                break;
                            }

                        }
                        farmerT.setData(new Gson().toJson(farmerAndFarmData));
                        farmerT.setIsUpdated("Y");
                        farmerT.setIsUploaded("N");
                        FarmerCacheManager.getInstance(context).updateFarm(new FarmerCacheManager.OnUpdateSuccessListener() {
                            @Override
                            public void onFarmerUpdated(boolean isUpdated) {

                            }
                        },farmerT);
                    }

                }


        } catch (Exception e) {

        }
        if (farmData!=null)
            farm.setFarmFullData(new Gson().toJson(farmData));


        farm.setStandingAcres(String.valueOf(newStandingAcers));
        farm.setPldAcres(finalPld+"");
        farm.setPldReason(pld_reason_for_damage.getText().toString());
        farm.setIsUpdated("Y");
        farm.setIsUploaded("N");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toasty.success(context, "Pld Data stored successfully", Toast.LENGTH_LONG, false).show();
            }
        });
        submit_pld_area.setClickable(true);
        submit_pld_area.setEnabled(true);
        FarmCacheManager.getInstance(context).updateFarm(farm);
        finish();
    }

    private void submitPldData() {
        double standingAcres = Double.parseDouble(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).trim());
        double pldAdded = Double.parseDouble(AppConstants.getUploadableArea(pld_area_in_acre.getText().toString().trim()));

        double newStandingAcers = standingAcres - pldAdded;
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        SendPldData sendPldData = new SendPldData();

        sendPldData.setAddedBy(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        if (!isOtherReason) {
            PldReason reason = (PldReason) pldReasonSpinner.getSelectedItem();
            sendPldData.setPldReasonId(reason.getPldId());
            sendPldData.setPld_reason(reason.getName());
        } else {
            sendPldData.setOtherReason(pld_reason_for_damage.getText().toString().trim());
            sendPldData.setPld_reason(pld_reason_for_damage.getText().toString());
        }
        sendPldData.setPldDate(AppConstants.getUploadableDate(pld_date.getText().toString().trim(),context));
        sendPldData.setPldArea(String.valueOf(pldAdded));
        sendPldData.setStanding_acres(String.valueOf(newStandingAcers));
        sendPldData.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
        sendPldData.setCluster_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
        sendPldData.setPerson_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
        sendPldData.setPld_acres(AppConstants.getUploadableArea(pld_area_in_acre.getText().toString()));
        sendPldData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        sendPldData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        Log.e(TAG, "Sending Pld Area " + new Gson().toJson(sendPldData));


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                submit_pld_area.setClickable(false);
                submit_pld_area.setEnabled(false);
            }
        });
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            String[] imageLis = new String[imageList.size()];

            for (int i = 0; i < img_list.length; i++) {
                if (img_list[i] != null) {
                    imageLis[i] = img_list[i];
                }
            }
            sendPldData.setImageList(imageLis);
            Log.e(TAG,"Image list "+sendPldData.getImageList().length);
            Log.e(TAG, "Sending Pld Area " + new Gson().toJson(sendPldData));
            Call<StatusMsgModel> msgModelCall = apiService.getStatusMsgForPldArea(sendPldData);
            final boolean[] isLoaded = {false};
            long currMillis=AppConstants.getCurrentMills();
            msgModelCall.enqueue(new Callback<StatusMsgModel>() {
                @Override
                public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                    String error=null;
                    isLoaded[0]=true;
                    if (response.isSuccessful()) {
                        StatusMsgModel statusMsgModel = response.body();
                        Log.e(TAG, "Success " + new Gson().toJson(response.body()));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar_cyclic.setVisibility(View.INVISIBLE);
                                submit_pld_area.setClickable(true);
                                submit_pld_area.setEnabled(true);
                            }
                        });
                        if (response.body().getStatus() == 10) {

                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(SubmitPldActivity.this, response.body().getMsg());
                        }  else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(SubmitPldActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(SubmitPldActivity.this, resources.getString(R.string.authorization_expired));
                        }else if (statusMsgModel.getStatus() == 1) {
                            String farmid=SharedPreferencesMethod.getString(context,SharedPreferencesMethod.SVFARMID);
                            ActivityOptions options = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                SharedPreferencesMethod.setBoolean(context,SharedPreferencesMethod.IS_DATA_CHANGED_FARM+"_"+farmid,true);
                                Intent intent=getIntent();
                                setResult(RESULT_OK);

                                finish();
                            } else {
                                Intent intent=getIntent();
                                setResult(RESULT_OK);
                                SharedPreferencesMethod.setBoolean(context,SharedPreferencesMethod.IS_DATA_CHANGED_FARM+"_"+farmid,true);
                                finish();
                            }

                        } else {
                            //  Log.e("Error", statusMsgModel.getMsg() + " " + statusMsgModel.getStatus());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar_cyclic.setVisibility(View.INVISIBLE);
                                    submit_pld_area.setClickable(true);
                                    submit_pld_area.setEnabled(true);
                                }
                            });
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(SubmitPldActivity.this, resources.getString(R.string.failure_upload_msg));
                            //Toast.makeText(context, "Server Error Please try again latter", Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(SubmitPldActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(SubmitPldActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            error=response.errorBody().string().toString();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar_cyclic.setVisibility(View.INVISIBLE);
                                    submit_pld_area.setClickable(true);
                                    submit_pld_area.setEnabled(true);
                                }
                            });
                            Log.e("Error", error);
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(SubmitPldActivity.this, resources.getString(R.string.failure_upload_msg));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    long newMillis=AppConstants.getCurrentMills();
                    long diff=newMillis-currMillis;
                    if(diff>=DELAY_API||(diff<DELAY_API&&error!=null&&!TextUtils.isEmpty(error))||response.code()==500) {
                        notifyApiDelay(SubmitPldActivity.this, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }
                }

                @Override
                public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                    long newMillis=AppConstants.getCurrentMills();
                    isLoaded[0]=true;
                    long diff=newMillis-currMillis;
                    notifyApiDelay(SubmitPldActivity.this,call.request().url().toString(),
                            ""+diff,internetSPeed,t.toString(),0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar_cyclic.setVisibility(View.INVISIBLE);
                            submit_pld_area.setClickable(true);
                            submit_pld_area.setEnabled(true);
                        }
                    });
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showDialog(SubmitPldActivity.this, resources.getString(R.string.failure_upload_msg));
                }
            });
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    internetFlow(isLoaded[0]);
                }
            }, AppConstants.DELAY);

        }else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    submit_pld_area.setClickable(false);
                    submit_pld_area.setEnabled(false);
                    offlineModeSubmitPldData(sendPldData);
                }
            });
        }
    }
    String internetSPeed="";
    private void internetFlow(boolean isLoaded){
        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLoaded){
                        if (!ConnectivityUtils.isConnected(context)){
                            AppConstants.failToast(context,resources.getString(R.string.check_internet_connection_msg));
                        }else {
                            ConnectivityUtils.checkSpeedWithColor(SubmitPldActivity.this, new ConnectivityUtils.ColorListener() {
                                @Override
                                public void onColorChanged(int color,String speed) {
                                    if (color==android.R.color.holo_green_dark){
                                        connectivityLine .setVisibility(View.GONE);
//                                    connectivityLine.setBackgroundColor(getColor(R.color.blue));
                                    }else {
                                        connectivityLine .setVisibility(View.VISIBLE);
                                        connectivityLine.setBackgroundColor(resources.getColor(color));
                                    }
                                    internetSPeed=speed;
                                }
                            },20);
                        }
                    }
                }
            }, AppConstants.DELAY);
        }catch (Exception e){

        }

    }

    private boolean isOtherReason = false;

    protected void getPldReasons() {
        progressBar_cyclic.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();
        apiInterface.getPldReasonList(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID)).enqueue(new Callback<PldReasonResponse>() {
            @Override
            public void onResponse(Call<PldReasonResponse> call, Response<PldReasonResponse> response) {
                String error=null;
                isLoaded[0]=true;
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 10) {
                        progressBar_cyclic.setVisibility(View.GONE);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context);
                    } else if (response.body().getStatus() == 1 && response.body().getPldReasonList() != null && response.body().getPldReasonList().size() > 0) {
                        List<PldReason> pldReasons = new ArrayList<>();
                        PldReason pldReason = new PldReason();
                        pldReason.setName("Select Loss/Damage Reason");
                        pldReasons.add(pldReason);
                        pldReasons.addAll(response.body().getPldReasonList());
                        PldReason otherReason = new PldReason();
                        otherReason.setName("Other");
                        pldReasons.add(otherReason);
                        lastPosition = pldReasons.size() - 1;
                        PldSpinnerAdapter adapter = new PldSpinnerAdapter(context, pldReasons);
                        pldReasonSpinner.setAdapter(adapter);
                        isOtherReason = false;
                        progressBar_cyclic.setVisibility(View.GONE);
                        pldFormLayout.setVisibility(View.VISIBLE);
                        pldReasonSpinner.setVisibility(View.VISIBLE);
                        reasonTiLayout.setVisibility(View.GONE);
                    } else {
                        progressBar_cyclic.setVisibility(View.GONE);
                        isOtherReason = true;
                        pldFormLayout.setVisibility(View.VISIBLE);
                        pldReasonSpinner.setVisibility(View.GONE);
                        reasonTiLayout.setVisibility(View.VISIBLE);
                    }
                } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(SubmitPldActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(SubmitPldActivity.this, resources.getString(R.string.authorization_expired));
                }else {
                    try {
                        error=response.errorBody().string();
                        progressBar_cyclic.setVisibility(View.GONE);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialogForFinish(context, resources.getString(R.string.something_went_wrong_msg));
                        Log.e(TAG, "Reason Err " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis=AppConstants.getCurrentMills();
                long diff=newMillis-currMillis;
                if(diff>=DELAY_API||(diff<DELAY_API&&error!=null&&!TextUtils.isEmpty(error))||response.code()==500) {
                    notifyApiDelay(SubmitPldActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<PldReasonResponse> call, Throwable t) {
                isLoaded[0]=true;
                long newMillis=AppConstants.getCurrentMills();
                long diff=newMillis-currMillis;
                notifyApiDelay(SubmitPldActivity.this,call.request().url().toString(),
                        ""+diff,internetSPeed,t.toString(),0);
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialogForFinish(context, resources.getString(R.string.something_went_wrong_msg));
                progressBar_cyclic.setVisibility(View.GONE);
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


    private void offlineModeEvent() {

        FarmCacheManager.getInstance(context).getFarm(this::onFarmLoader, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));

    }

    private void onlineModeEvent() {
        if (DataHandler.newInstance().getFetchFarmResult() != null) {

            if (DataHandler.newInstance().getFetchFarmResult().getPldAcres() != null && !DataHandler.newInstance().getFetchFarmResult().getPldAcres().trim().equals("")) {
                oldPldAcers = Double.parseDouble(DataHandler.newInstance().getFetchFarmResult().getPldAcres().trim());
                if (oldPldAcers > 0) {
                    submit_pld_area.setBackgroundColor(Color.parseColor("#33ffc107"));
                    //submit_pld_area.setVisibility(View.GONE);
                    pld_already_done_msg_tv.setVisibility(View.VISIBLE);
                }
                pld_area_in_acre.setText(convertAreaTo(DataHandler.newInstance().getFetchFarmResult().getPldAcres().trim()));

            } else {
                pld_area_in_acre.setHint(resources.getString(R.string.enter_pld_here_hint));
            }

            try {
                if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA) != null ||
                        !SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA).equals("0")) {
                    eff_farm_area_tv.setText(convertAreaTo(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).trim()) + " / "
                            + convertAreaTo(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA).trim()));
                } else {
                    eff_farm_area_tv.setText(convertAreaTo(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).trim()) + " / "
                            +
                            convertAreaTo(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.EXPECTED_AREA).trim()));
                }
            } catch (NumberFormatException numExep) {
                numExep.printStackTrace();
            }
            if (DataHandler.newInstance().getFetchFarmResult().getPldReason() != null &&
                    !DataHandler.newInstance().getFetchFarmResult().getPldReason().equals("")) {
                pld_reason_for_damage.setText(DataHandler.newInstance().getFetchFarmResult().getPldReason());
                Log.e("PldReason", "Pld Reason " + DataHandler.newInstance().getFetchFarmResult().getPldReason());
            } else {
                pld_reason_for_damage.setHint(resources.getString(R.string.enter_reason_for_pld_hint));
                Log.e("PldReason", "Pld Reason " + DataHandler.newInstance().getFetchFarmResult().getPldReason());
            }
        } else {
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*Intent intent=new Intent(context,LandingActivity.class);
        startActivity(intent);
        finish();*/
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    FarmerT farmerT;
    @Override
    public void onFarmLoader(Farm farm) {
        this.farm = farm;

        if (farm != null && AppConstants.isValidString(farm.getFarmerId()))
            FarmerCacheManager.getInstance(context).getFarmer(new FarmerCacheManager.GetFarmerListener() {
                @Override
                public void onFarmerLoaded(FarmerT farmerT) {
                    SubmitPldActivity.this.farmerT = farmerT;
                }
            }, farm.getFarmerId());

        if (farm.getOfflinePldJson()!=null){
            try {
                JSONArray array = new JSONArray(farm.getOfflinePldJson());
                for (int j = 0; j < array.length(); j++) {
                    JSONObject object = array.getJSONObject(j);
                    PldData dataDD = new Gson().fromJson(object.toString(), PldData.class);
                    plDataList.add(dataDD);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

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

        /*if (farm.getPldAcres() != null && !farm.getPldAcres().trim().equals("")) {
            oldPldAcers = Double.valueOf(farm.getPldAcres().trim());
            if (oldPldAcers > 0) {
                //submit_pld_area.setVisibility(View.GONE);
                submit_pld_area.setBackgroundColor(Color.parseColor("#33ffc107"));
                pld_already_done_msg_tv.setVisibility(View.VISIBLE);
            }
            pld_area_in_acre.setText(convertAreaTo(farm.getPldAcres()));
        } else {
            pld_area_in_acre.setHint(resources.getString(R.string.enter_pld_hint));
        }
        try {
            if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA) != null ||
                    !SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA).equals("0")) {
                eff_farm_area_tv.setText(convertAreaTo(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).trim()) + " / "
                        + convertAreaTo(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA).trim()));
            } else {
                eff_farm_area_tv.setText(convertAreaTo(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).trim()) + " / "
                        +
                        convertAreaTo(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.EXPECTED_AREA).trim()));
            }
        } catch (NumberFormatException numExep) {
            numExep.printStackTrace();
        }
        if (farm.getPldReason() != null && !farm.getPldReason().equals("")) {
            pld_reason_for_damage.setText(farm.getPldReason());
        } else {
            pld_reason_for_damage.setHint(resources.getString(R.string.enter_reason_for_pld_hint));
        }*/
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE1) {
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

    private void cameraIntent(String onclick) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.putExtra(android.provider.MediaStore.EXTRA_SIZE_LIMIT, "720000");
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

    /*private void showAlertForExit() {
        if (imageList.size() > 0 || cpcSpinner.getSelectedItemPosition() > 0 || visitMaterialDynamicList.size() > 0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            AlertDialog alertDialog = builder.setMessage(resources.getString(R.string.are_you_sure_exit_without_saving))
                    .setTitle(resources.getString(R.string.alert_title_label))
                    .setCancelable(false)
                    .setPositiveButton(resources.getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(resources.getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        } else {
            finish();
        }

    }*/

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
                if (ReadExif(imgFile.getAbsolutePath(), "capture1",0,true)) {
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
                    //imagefull.setImageBitmap(myBitmap);

                } else {
                    //image1.setImageBitmap(null);
                }
                //Log.e("Exif",ReadExif(imgFile.getAbsolutePath()));
            } else if (onclick.equals("capture2")) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = getResizedBitmap(myBitmap, 720, 1080);
//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
                if (ReadExif(imgFile.getAbsolutePath(), "capture2",1,true)) {
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
                if (ReadExif(imgFile.getAbsolutePath(), "capture3",2,true)) {
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
                if (ReadExif(imgFile.getAbsolutePath(), "capture4",3,true)) {
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
                if (ReadExif(imgFile.getAbsolutePath(), "capture5",4,true)) {
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
                if (ReadExif(imgFile.getAbsolutePath(), "capture6",5,true)) {
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
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
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
            if (ReadExif(picturePath, "capture1",0,false)) {
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
            } else {
                myBitmap = null;
                //image1.setImageBitmap(null);
            }
        } else if (onclick.equals("capture2")) {
            myBitmap = BitmapFactory.decodeFile(picturePath);
            myBitmap = getResizedBitmap(myBitmap, 720, 1080);
//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
            if (ReadExif(picturePath, "capture2",1,false)) {

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
            if (ReadExif(picturePath, "capture3",2,false)) {
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
            if (ReadExif(picturePath, "capture4",3,false)) {
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
            if (ReadExif(picturePath, "capture5",4,false)) {
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
            if (ReadExif(picturePath, "capture6",5,false)) {
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

    private int checkSize(Paint addedBy, Rect textRec, int imageWidth, int textSize, String watermarkText) {

/*
        int width = 0;
        if (textRec.width() >= (imageWidth - 4) / 3)     //the padding on either sides is considered as 4, so as to appropriately fit in the text
        {

            checkSize(addedBy, textRec, imageWidth, textSize, watermarkText);
        }

*/

        while (textRec.width() >= (imageWidth - 4) / 3)
        {
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
        String userName="Clicked by: "+SharedPreferencesMethod.getString(context,SharedPreferencesMethod.USER_NAME);
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
        Log.e("checkSize","textsize "+s);
        canvas.drawText(message, textX+5, textY- 2*s-5, paintCenter);
        canvas.drawText(message2, textX+5, textY - s-5, paintCenter);
        canvas.drawText(message3, textX+5, textY-5 , paintCenter);

        String text2 = "Left Text";
        Paint paintLeft = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
        paintLeft.setStyle(Paint.Style.FILL);
        paintLeft.setColor(Color.WHITE);
        //float sizeLeft = convertDpToPx(context, 25);
        paintLeft.setTextAlign(Paint.Align.LEFT);
        float textYL = pikedPhoto.getHeight();

        if (text1==null||TextUtils.isEmpty(text1)){
            Rect textRectL = new Rect();
            paintLeft.getTextBounds(userName, 0, userName.length(), textRectL);
            int sL = checkSize(paintLeft, textRectL, (canvas.getWidth() - 4), 50, userName);
            s=sL;
        }
        paintLeft.setTextSize(s);
        canvas.drawText(userName, 10, textYL- 2*s -5, paintLeft);
        canvas.drawText("Lat: "+imgLat, 10, textYL - s-5, paintLeft);
        canvas.drawText("Long: "+imgLong, 10, textYL-5 , paintLeft);
        // in this case, center.x and center.y represent the coordinates of the center of the rectangle in which the text is being placed
        pikedPhoto = addWatermark(pikedPhoto);

        int f=(s*2)+10;

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
        int w = src.getWidth();
        int h = src.getHeight();
        String longitude = imgLat;
        String latitude = imgLong;

        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        Paint addedBy = new Paint();
        //apply color
        addedBy.setColor(resources.getColor(R.color.white));
        addedBy.setTextSize(50);
        addedBy.setAntiAlias(true);
        addedBy.setUnderlineText(true);
        int y = canvas.getHeight() - 200;
        canvas.drawText("Clicked By: " + watermarkText, 20, y, addedBy);


        Paint lat = new Paint();
        //apply color
        lat.setColor(resources.getColor(R.color.white));
        lat.setTextSize(50);
        lat.setAntiAlias(true);
        lat.setUnderlineText(true);
        int y2 = canvas.getHeight() - 125;
        lat.setColorFilter(new ColorFilter());
        canvas.drawText("Latitude: " + latitude, 20, y2, lat);

        Paint lng = new Paint();
        //apply color
        lng.setColor(resources.getColor(R.color.white));
        lng.setTextSize(50);
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

        }
        if (message3!=null&&!TextUtils.isEmpty(message3)){
            String mText2 = message3;
            Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint2.setColor(getResources().getColor(R.color.red));
            paint2.setTextSize((int) (50));
            paint2.setTextAlign(Paint.Align.CENTER);
            Rect bounds2 = new Rect();
            paint2.getTextBounds(mText2, 0, mText2.length(), bounds2);
            canvas.drawText(mText2, src.getWidth() / 2, y3, paint2);
        }else {
            //Toast.makeText(context, "message3 empty", Toast.LENGTH_SHORT).show();
        }

        Bitmap waterMark = BitmapFactory.decodeResource(context.getResources(), R.drawable.ct_water_mark);

        Log.e(TAG, "Canvas" + canvas.getWidth() + " " + canvas.getHeight());
        if (canvas.getWidth() > canvas.getHeight()) {
            canvas.drawBitmap(waterMark, (9 * (canvas.getWidth() / 10) - 50), (9 * (canvas.getHeight() / 10) - 200), null);
            Log.e(TAG, "CanvasW" + (8 * (canvas.getWidth() / 10) - 100) + " " + (9 * (canvas.getHeight() / 10) - 200));

        } else {
            canvas.drawBitmap(waterMark, (8 * (canvas.getWidth() / 10) - 50), (9 * (canvas.getHeight() / 10) - 90), null);
            Log.e(TAG, "CanvasH" + (8 * (canvas.getWidth() / 10) - 50) + " " + (9 * (canvas.getHeight() / 10) - 90));
        }

        return result;
    }

    String imgLat="0",imgLong="0";


    Boolean ReadExif(String file, String captureNo,int index,boolean flag) {

        message = "";
        message2 = "";
        message3="";
        imgLat="0";
        imgLong="0";
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
                if (final_date.before(newDate)) {
                    time_stamp = true;
                    message3="Image is old";
                    Log.e(TAG, "BOTH DATES IF " + "     " + final_date.toString() + "    " + newDate.toString());
                    time_stamp = true;
                } else {
                    message3="";
                    Log.e(TAG, "BOTH DATES ELSE " + "     " + final_date.toString() + "    " + newDate.toString());
                }

            } else {
                message3="Image date not available";
                time_stamp = true;
                Log.e(TAG,"outer else exif");
            }


            float[] latLong = new float[2];
            boolean hasLatLong = exifInterface.getLatLong(latLong);
            String imgLat="";
            String imgLong="";
            if (hasLatLong) {
                System.out.println("SubmitPldActivity Latitude: " + latLong[0]);
                System.out.println("SubmitPldActivity Longitude: " + latLong[1]);
                if (latLong.length>1) {
                    imgLat = "" + latLong[0];
                    imgLong = "" + latLong[0];
                }
            }

            if (imgLat != null && imgLong != null&&!TextUtils.isEmpty(imgLat)&&!TextUtils.isEmpty(imgLong)) {
                Log.e(TAG,"image lat "+imgLat);
                Log.e(TAG,"image long "+imgLong);
                this.imgLat=imgLat;
                this.imgLong=imgLong;
                imageLatitude.set(index,imgLat);
                imageLongitude.set(index,imgLong);

                if (farmLatitude != null && farmLongitude != null && !TextUtils.isEmpty(farmLongitude) && !TextUtils.isEmpty(farmLatitude)) {
                    LatLng latLng=new LatLng(Double.valueOf(imgLat),Double.valueOf(imgLong));
                    if (latLngList!=null&&latLngList.size()>2&&AppConstants.isPointInPolygon(latLng,latLngList)){
                        message="";
                        message2="";
                    }else {
                        message = "Image might not be from farm";
                        message2 = "Gps coordinates mismatch";
                    }

                } else {
                    message = "Image might not be from farm";
                    message2 = "Farm coordinates missing";
                }

            } else if (flag&&currentLocation != null) {
                this.imgLat=""+currentLocation.getLatitude();
                this.imgLong=""+currentLocation.getLongitude();
                imageLatitude.set(index,""+currentLocation.getLatitude());
                imageLongitude.set(index,""+currentLocation.getLongitude());
                System.out.println("SubmitPldActivity Latitude: " + currentLocation.getLatitude());
                System.out.println("SubmitPldActivity Longitude: " + currentLocation.getLongitude());
//                if (farmLatitude != null && farmLongitude != null && !TextUtils.isEmpty(farmLongitude) && !TextUtils.isEmpty(farmLatitude)) {
                    LatLng latLng=new LatLng(Double.valueOf(currentLocation.getLatitude()),Double.valueOf(currentLocation.getLongitude()));
                    if (latLngList!=null&&latLngList.size()>2&&AppConstants.isPointInPolygon(latLng,latLngList)){
                        message="";
                        message2="";
                    }else {
                        message = "Image might not be from farm";
                        message2 = "Gps coordinates mismatch";
                    }
//                } else {
//                    message = "Image might not be from farm";
//                    message2 = "Farm coordinates missing";
//                }

            } else {
                this.imgLat="0";
                this.imgLat="0";
                imageLatitude.add(index,"0");
                imageLongitude.add(index,"0");
                message = "Image might not be from farm";
                message2 = "Image coordinates missing";
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(context,
                    e.toString(),
                    Toast.LENGTH_LONG).show();
        }





        return time_stamp;
    }
    /*private class FetchGpsCordinates extends AsyncTask<String, Void, String> {
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
                        Log.e(TAG,"Farm corrds list "+new Gson().toJson(getGpsCoordinates));
                        if (getGpsCoordinates.getStatus() == 10) {

                            viewFailDialog.showSessionExpireDialog(SubmitPldActivity.this, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                            //un authorized access
//                            ViewFailDialog viewFailDialog = new ViewFailDialog();
//                            viewFailDialog.showSessionExpireDialog(TimelineAddVisitActivity3.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                            // auth token expired
//                            ViewFailDialog viewFailDialog = new ViewFailDialog();
//                            viewFailDialog.showSessionExpireDialog(TimelineAddVisitActivity3.this, resources.getString(R.string.authorization_expired));
                        } else if (getGpsCoordinates.getStatus() != 0) {
                            List<LatLngFarm> latLngs = getGpsCoordinates.getData();

                            if (latLngs!=null){
                                for (int i=0;i<latLngs.size();i++){
                                    latLngList.add(new LatLng(Double.valueOf(latLngs.get(i).getLat()),Double.valueOf(latLngs.get(i).getLng())));
                                }
                            }
                        }
                    }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(TimelineAddVisitActivity3.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(TimelineAddVisitActivity3.this, resources.getString(R.string.authorization_expired));
                    }  else {

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

    }*/

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


}
