package com.i9930.croptrails.Test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
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
import retrofit2.Retrofit;

import com.i9930.croptrails.AddFarm.Model.FarmData;
import com.i9930.croptrails.AddFarm.Model.FarmerAndFarmData;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.AssignCalendar.Model.SingleCalendarResponse;
import com.i9930.croptrails.AssignCalendar.SelectCalendarActivity;
import com.i9930.croptrails.Calendar.Adapter.CalendarViewAdapter;
import com.i9930.croptrails.CommonClasses.Address.CityDatum;
import com.i9930.croptrails.CommonClasses.Address.CountryDatum;
import com.i9930.croptrails.CommonClasses.Address.StateDatum;
import com.i9930.croptrails.CommonClasses.ParamData;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.CommonClasses.TimelineUnits;
import com.i9930.croptrails.CropCycle.Show.CropCycleListActivity;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.FarmDetailsUpdateActivity;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.FarmDetailsUpdateActivity2;
import com.i9930.croptrails.FarmDetails.Model.FullDetailsResponse;
import com.i9930.croptrails.FarmDetails.Model.HarvestAndFloweringSendData;
import com.i9930.croptrails.FarmDetails.Model.LineChartInfo;
import com.i9930.croptrails.FarmDetails.Model.WeatherForecast;
import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineInnerData;
import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineResponse;
import com.i9930.croptrails.FarmNavigation.FarmNavigationActivity;
import com.i9930.croptrails.GerminationAndSpacing.AddGermiFullScreenDialog;
import com.i9930.croptrails.GerminationAndSpacing.Model.SendGerminationSpacingData;
import com.i9930.croptrails.GerminationAndSpacing.ShowSampleGermination.Model.SampleGerminationDatum;
import com.i9930.croptrails.GerminationAndSpacing.ShowSampleGermination.Model.SampleGerminationStatusNdData;
import com.i9930.croptrails.HarvestReport.Model.HarvestDetailInnerData;
import com.i9930.croptrails.HarvestReport.Model.HarvestDetailMaster;
import com.i9930.croptrails.HarvestReport.Model.ViewHarvestDetails;
import com.i9930.croptrails.HarvestSubmit.EasyMode.HarvestSubmitEasyModeActivity;
import com.i9930.croptrails.ImagePager.Document.DocumentActivity;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Maps.Offline.OfflineMapActivity;
import com.i9930.croptrails.Maps.ShowArea.Model.GetGpsCoordinates;
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;
import com.i9930.croptrails.Maps.ShowArea.Model.geojson.GeoJsonResponse;
import com.i9930.croptrails.Maps.ShowArea.ShowAreaOnMapActivity;
import com.i9930.croptrails.Maps.ShowArea.ShowAreaWithUpdateActivity;
import com.i9930.croptrails.Maps.VerifyArea.MapsActivity;
import com.i9930.croptrails.Maps.VerifyArea.MapsActivityEasyMode;
import com.i9930.croptrails.Maps.VerifyArea.MapsActivitySamunnati;
import com.i9930.croptrails.ProduceSell.AddProduceSellActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegCacheManager;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegModel;
import com.i9930.croptrails.RoomDatabase.CompRegistry.DatabaseResInterface;
import com.i9930.croptrails.RoomDatabase.CompRegistry.Model.CompRegResult;
import com.i9930.croptrails.RoomDatabase.DoneActivities.ActivityCacheManager;
import com.i9930.croptrails.RoomDatabase.DoneActivities.ActivityCountListener;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmCacheManager;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmLoadListener;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerCacheManager;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerT;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestCacheManager;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestFetchListener;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestT;
import com.i9930.croptrails.RoomDatabase.InputCost.InputCostCacheManager;
import com.i9930.croptrails.RoomDatabase.InputCost.InputCostFetchListener;
import com.i9930.croptrails.RoomDatabase.InputCost.InputCostT;
import com.i9930.croptrails.RoomDatabase.ResourceCost.ResourceCacheManager;
import com.i9930.croptrails.RoomDatabase.ResourceCost.ResourceCostT;
import com.i9930.croptrails.RoomDatabase.ResourceCost.ResourceFetchListener;
import com.i9930.croptrails.RoomDatabase.Timeline.CalendarJSON;
import com.i9930.croptrails.RoomDatabase.Timeline.Timeline;
import com.i9930.croptrails.RoomDatabase.Timeline.TimelineCacheManager;
import com.i9930.croptrails.RoomDatabase.Timeline.TimelineNotifyInterface;
import com.i9930.croptrails.RoomDatabase.Visit.VisitCountListener;
import com.i9930.croptrails.RoomDatabase.Visit.VrCacheManager;
import com.i9930.croptrails.ShowInputCost.Model.InputCostData;
import com.i9930.croptrails.ShowInputCost.Model.InputCostResponse;
import com.i9930.croptrails.ShowInputCost.Model.ResourceData;
import com.i9930.croptrails.SoilSense.BluetoothCommunicationActivity;
import com.i9930.croptrails.SoilSense.Dashboard.Model.GetSoilDatum;
import com.i9930.croptrails.SubmitHealthCard.Adapters.CustomAdapter;
import com.i9930.croptrails.SubmitHealthCard.AddSoilHealthCardActivity;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCard;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCardDDResponse;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCardData;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCardParams;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCardResponse;
import com.i9930.croptrails.SubmitInputCost.InputCostActivity;
import com.i9930.croptrails.SubmitPld.SubmitPldActivity;
import com.i9930.croptrails.SubmitVisitForm.Delinquent.AddDelinquentActivity;
import com.i9930.croptrails.SubmitVisitForm.TimelineAddVisitActivity2;
import com.i9930.croptrails.Test.ChakModel.FetchChakDatum;
import com.i9930.croptrails.Test.ChakModel.FetchChakResponse;
import com.i9930.croptrails.Test.Dialog.SeedFullScreenDialog;
import com.i9930.croptrails.Test.PldModel.PldData;
import com.i9930.croptrails.Test.PldModel.PldResponse;
import com.i9930.croptrails.Test.SatusreModel.BinSet;
import com.i9930.croptrails.Test.SatusreModel.SatsureData;
import com.i9930.croptrails.Test.SatusreModel.SatsureResponse;
import com.i9930.croptrails.Test.SellModel.SellData;
import com.i9930.croptrails.Test.SellModel.SellResponse;
import com.i9930.croptrails.Test.SoilSensResModel.SoilSenseResponse;
import com.i9930.croptrails.Test.adapter.TimelineAdapterTest;
import com.i9930.croptrails.Test.model.TimelineHarvest;
import com.i9930.croptrails.Test.model.full.FarmAddressDetails;
import com.i9930.croptrails.Test.model.full.FarmCropDetails;
import com.i9930.croptrails.Test.model.full.FarmFullDetails;
import com.i9930.croptrails.Test.model.full.FarmsDetails;
import com.i9930.croptrails.Test.model.full.PersonDetails;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.AppFeatures;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;
import com.i9930.croptrails.Weather.Model.WeatherMainRes;
import com.i9930.croptrails.Weather.WeatherActivity;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;
import static com.i9930.croptrails.Utility.AppConstants.for_date;
import static com.i9930.croptrails.Utility.AppConstants.isValidActualArea;
import static com.i9930.croptrails.Utility.AppConstants.isValidString;

public class TestActivity extends AppCompatActivity implements DatabaseResInterface,
        TimelineNotifyInterface, FarmLoadListener,
        VisitCountListener, ActivityCountListener,
        HarvestFetchListener, InputCostFetchListener, ResourceFetchListener {
    String TAG = "TestActivity";
    TestActivity context;
    WeatherForecast weatherForecast = new WeatherForecast();
    ViewFailDialog viewFailDialog;
    Resources resources;
    String cityName = "", stateName = "", countryName = "";
    boolean isActivityRunning;
    Location currentLocation;
    RecyclerView recyclerView;
    private FetchFarmResult farmResult;
    public static String AVG_PP_SPACING = "0";
    public static String AVG_RR_SPACING = "0";
    public static String AVG_POPU = "0";
    public static String AVG_GERMI = "0";
    String farmLatitude, farmLongitude;
    int noOfDays = 0;
    List<TimelineInnerData> timelineInnerData = new ArrayList<>();
    TextView title_name, title_address/*, toolbar_farm_details_tv*/;
    String mobNo = "";
    String navigateTo = "";
    @BindView(R.id.farm_details_mob_number)
    TextView farm_details_mob_number;
    static int y;
    Farm farm;
    int visitCount = 0, activityCount = 0;
    @BindView(R.id.drawerRefreshImg)
    ImageView drawerRefreshImg;

    // WeatherForecast weatherForecast;
    //List<TimelineInnerData> timelineInnerData = new ArrayList<>();
    int todayPosition = 0;
    @BindView(R.id.title_name_d)
    TextView title_name_d;
    @BindView(R.id.title_address_d)
    TextView title_address_d;
    @BindView(R.id.farm_details_currentCrop)
    TextView currentCropTv;
    BottomSheetDialog bottomSheetDialog;
    TextView tv_easy_mode, tv_normal_mode, tv_easy_mode_ultra;
    private boolean isEasyMode;
    private boolean isTodayVisitAdded = false;

    List<Object> objectList = new ArrayList<>();
    List<ObjectType> objectTypeList = new ArrayList<>();
    String compId = "";

    RelativeLayout relWalAroundMode, relEasyMode, relPinDropMode, relWalAroundModeAutomatic;
    TextView walkAutoTv;
    ImageView walkAutoImg;
    float locationAccuracy = 10.0f;
    boolean isAutoClisk = false;

    public void showDialog(boolean showAutoCalculate) {
        float compAccuracy = 2.0f;
        compAccuracy = SharedPreferencesMethod.getFloatPref(context, SharedPreferencesMethod.COMP_AUTO_ACCURACY);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_select_capture_area_mode);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        relWalAroundMode = dialog.findViewById(R.id.relWalAroundMode);
        relWalAroundModeAutomatic = dialog.findViewById(R.id.relWalAroundModeAutomatic);
        relEasyMode = dialog.findViewById(R.id.relEasyMode);
        relPinDropMode = dialog.findViewById(R.id.relPinDropMode);
        walkAutoImg = dialog.findViewById(R.id.walkAutoImg);
        walkAutoTv = dialog.findViewById(R.id.walkAutoTv);
        /*if (!showAutoCalculate) {
            relEasyMode.setVisibility(View.GONE);
        } else {
            relEasyMode.setVisibility(View.VISIBLE);
        }*/

        if (locationAccuracy < compAccuracy) {
            isAutoClisk = true;
            walkAutoTv.setTextColor(getResources().getColor(R.color.black));
            walkAutoImg.setImageResource(R.drawable.ic_directions_walk_24px);
        } else {
            isAutoClisk = false;
            walkAutoTv.setTextColor(getResources().getColor(R.color.faded_text));
            walkAutoImg.setImageResource(R.drawable.ic_directions_walk_disabled_24px);
        }

        relWalAroundModeAutomatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAutoClisk) {
                    isEasyMode = false;
                    // Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(context, MapsActivity.class);
                    if (!showAutoCalculate)
                        intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE);
                    else
                        intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.ACTUAL_AREA);
                    intent.putExtra("isAutomaticMode", true);
                    ActivityOptions options = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                    } else {
                        startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                    }
                }
            }
        });

        relWalAroundMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEasyMode = false;
                // Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, MapsActivity.class);
                if (!showAutoCalculate)
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE);
                else
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.ACTUAL_AREA);
                intent.putExtra("isAutomaticMode", false);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                } else {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
            }
        });
        relPinDropMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEasyMode = true;
                //Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, MapsActivitySamunnati.class);
                if (!showAutoCalculate)
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE);
                else
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.ACTUAL_AREA);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                } else {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
            }
        });
        relEasyMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEasyMode = true;
                //Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, MapsActivityEasyMode.class);
                if (!showAutoCalculate)
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE);
                else
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.ACTUAL_AREA);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                } else {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
            }
        });
        dialog.show();


    }


    @SuppressLint("ResourceAsColor")
    private void initViews() {
        title_name = (TextView) findViewById(R.id.title_name);
        title_address = (TextView) findViewById(R.id.title_address);
        //toolbar_farm_details_tv = findViewById(R.id.toolbar_farm_details_tv);
        // toolbar_back_img=findViewById(R.id.toolbar_back_img);

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.dialog_verify_area_bottom_sheet);
        Window window2 = bottomSheetDialog.getWindow();
        window2.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tv_easy_mode = bottomSheetDialog.findViewById(R.id.tv_easy_mode);
        tv_normal_mode = bottomSheetDialog.findViewById(R.id.tv_normal_mode);
        tv_easy_mode_ultra = bottomSheetDialog.findViewById(R.id.tv_easy_mode_ultra);
        compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);

        /*if (compId != null && compId.equals("100075")) {
            tv_easy_mode_ultra.setVisibility(View.VISIBLE);
        } else {
            tv_easy_mode_ultra.setVisibility(View.GONE);
        }*/

        tv_easy_mode_ultra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEasyMode = true;
                //Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                Intent intent = new Intent(context, MapsActivitySamunnati.class);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                } else {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
                bottomSheetDialog.dismiss();
            }
        });

        tv_easy_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEasyMode = true;
                //Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                Intent intent = new Intent(context, MapsActivityEasyMode.class);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                } else {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
                bottomSheetDialog.dismiss();
            }
        });

        tv_normal_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEasyMode = false;
                // Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                Intent intent = new Intent(context, MapsActivity.class);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                } else {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
                bottomSheetDialog.dismiss();
            }
        });


    }

    @BindView(R.id.farm_details_irrigationSource)
    TextView farm_details_irrigationSource;
    @BindView(R.id.farm_details_irrigationType)
    TextView farm_details_irrigationType;
    @BindView(R.id.farm_details_previousCrop)
    TextView farm_details_previousCrop;
    @BindView(R.id.farm_details_soilType)
    TextView farm_details_soilType;
    @BindView(R.id.farm_details_sowingDate)
    TextView farm_details_sowingDate;
    @BindView(R.id.farm_details_expFloweringDate)
    TextView farm_details_expFloweringDate;
    @BindView(R.id.add_details_expHarvestDate)
    TextView add_details_expHarvestDate;
    @BindView(R.id.farm_details_actual_flowering_date)
    TextView farm_details_actual_flowering_date;
    @BindView(R.id.farm_details_actual_harvest_date)
    TextView farm_details_actual_harvest_date;
    @BindView(R.id.timeline_progress)
    GifImageView progressBar;
    LinearLayout.LayoutParams params1;
    CompRegCacheManager compRegCacheManager;
    Calendar myCalendarAddFarmflowering = Calendar.getInstance();
    Calendar myCalendarAddFarmHarvest = Calendar.getInstance();
    DrawerLayout drawerLayout;
    //RecyclerView timeline_recycler_view;
    LinearLayoutManager layoutManager;
    Activity activity;
    Toolbar mActionBarToolbar;
    ArrayList<String> xAxisLabel;
    ArrayList<String> yAxisLabel;
    @BindView(R.id.lineChart)
    LineChart lineChart;
    @BindView(R.id.view_farm_area_on_map)
    TextView view_farm_area_button;
    @BindView(R.id.update_farm_details)
    TextView update_farm_details_button;
    String[] latAraay;
    String lngArray[];
    @BindView(R.id.farm_details_expected_area_tv)
    TextView farm_details_expected_area_tv;
    @BindView(R.id.farm_details_actual_area_tv)
    TextView farm_details_actual_area_tv;
    @BindView(R.id.farm_details_standing_area_tv)
    TextView farm_details_standing_area_tv;
    @BindView(R.id.total_expense_tv)
    TextView total_expense_tv;
    @BindView(R.id.farmer_expense_layout)
    LinearLayout farmer_expense_layout;
    @BindView(R.id.drawer_back_img)
    ImageView drawer_back_img;
    FloatingActionMenu fabParent;
    FloatingActionButton fabChildSoilSense, fabChildAssignCalendar, fabChildSellRecord, fabChildSeedIssue, fabChildAddVisit, fabChildSoilHealthCard, fabChildAddPld, fabChildViewHarvest, fabChildAddGermination, fabChildViewInputCost;
    boolean isActivityDestroyed = true;
    Call<FullDetailsResponse> farmAndHarvestCall;
    Call<TimelineResponse> getCalenderData;
    @BindView(R.id.no_data_available_calendar)
    RelativeLayout no_data_available_calendar;
    @BindView(R.id.no_data_available_tv)
    TextView no_data_available_tv;
    //    @BindView(R.id.seedLayout)
//    LinearLayout seedLayout;
    @BindView(R.id.seedQtyTv)
    TextView seedQtyTv;
    @BindView(R.id.seed_provided_on_tv)
    TextView seedProvidedOnTv;

    LinearLayout content;
    @BindView(R.id.navigation)
    LinearLayout chartLayout;
    @BindView(R.id.calendarLayout)
    LinearLayout calendarLayout;

    public static String todayDate;

    @BindView(R.id.connectivityLine)
    View connectivityLine;
    @BindView(R.id.omitanceAreaButton)
    TextView omitanceAreaButton;

    //Weather
    TextView weatherCityNameTv, txt_curr_temp, txt_max_temp, txt_min_temp, rainfall, weather_desclaimer_msg_tv;
    ImageView skycon_view;
    ProgressBar weatherProgress, progressbar;
    LinearLayout weatherLay;
    @BindView(R.id.delinquentFarmTv)
    TextView delinquentFarmTv;

    @BindView(R.id.directionFarmTv)
    TextView directionFarmTv;


    private void initWeatherView() {
        weatherProgress = findViewById(R.id.weatherProgress);
        weatherCityNameTv = findViewById(R.id.weatherCityNameTv);
        txt_curr_temp = findViewById(R.id.txt_curr_temp);
        txt_max_temp = findViewById(R.id.txt_max_temp);
        txt_min_temp = findViewById(R.id.txt_min_temp);
        rainfall = findViewById(R.id.rainfall);
        skycon_view = findViewById(R.id.skycon_view);
        progressbar = findViewById(R.id.progressbar);
        weatherLay = findViewById(R.id.weatherLay);
        weather_desclaimer_msg_tv = findViewById(R.id.weather_desclaimer_msg_tv);
    }

    private boolean flag = false;

    String auth;
    private static final int UPDATE_FARM_REQUEST_CODE = 100;
    private static final int WHOLE_DATA_CHANGE_REQUEST_CODE = 101;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i(TAG, "Location: " + location.getLatitude() + " " + location.getLongitude() + " Acc" + location.getAccuracy());
                currentLocation = location;
                if (currentLocation != null)
                    locationAccuracy = currentLocation.getAccuracy();

            }
        }
    };
    @BindView(R.id.docImage)
    ImageView docImage;
    @BindView(R.id.chakLeadNameTv)
    TextView chakLeadNameTv;
    @BindView(R.id.chakLeadCard)
    CardView chakLeadCard;


    @BindView(R.id.farmerNameTv)
    TextView farmerNameTv;
    @BindView(R.id.dobTv)
    TextView dobTv;
    @BindView(R.id.fatherNameTv)
    TextView fatherNameTv;
    @BindView(R.id.gender)
    TextView gender;
    @BindView(R.id.farmIdTv)
    TextView farmIdTv;
    @BindView(R.id.khasraTv)
    TextView khasraTv;

    @BindView(R.id.chakTv)
    TextView chakTv;
    @BindView(R.id.chakLeadCardMob)
    CardView chakLeadCardMob;
    @BindView(R.id.chakLeadNameTvMob)
    TextView chakLeadNameTvMob;
    @BindView(R.id.farmerDetailsLabelTv)
    TextView farmerDetailsLabelTv;
    @BindView(R.id.moreTv)
    TextView moreTv;
    @Nullable
    @BindView(R.id.farmerNameCard)
    CardView farmerNameCard;
    @Nullable
    @BindView(R.id.mobileCard)
    CardView mobileCard;
    @Nullable
    @BindView(R.id.dobCard)
    CardView dobCard;
    @Nullable
    @BindView(R.id.fatherCard)
    CardView fatherCard;
    @Nullable
    @BindView(R.id.genderCard)
    CardView genderCard;
    @Nullable
    @BindView(R.id.addlCard)
    CardView addlCard;
    @Nullable
    @BindView(R.id.addL2Card)
    CardView addL2Card;
    @Nullable
    @BindView(R.id.expAreaCard)
    CardView expAreaCard;
    @Nullable
    @BindView(R.id.actAreaCard)
    CardView actAreaCard;
    @Nullable
    @BindView(R.id.standigAreaCard)
    CardView standigAreaCard;
    @Nullable
    @BindView(R.id.seedQtyCard)
    CardView seedQtyCard;
    @Nullable
    @BindView(R.id.seedDateCard)
    CardView seedDateCard;
    @Nullable
    @BindView(R.id.irriTypeCard)
    CardView irriTypeCard;
    @Nullable
    @BindView(R.id.irriSourceCard)
    CardView irriSourceCard;
    @Nullable
    @BindView(R.id.prevCropCard)
    CardView prevCropCard;
    @Nullable
    @BindView(R.id.currentCropCard)
    CardView currentCropCard;
    @Nullable
    @BindView(R.id.soilTypeCard)
    CardView soilTypeCard;
    @Nullable
    @BindView(R.id.sowingDateCard)
    CardView sowingDateCard;
    @Nullable
    @BindView(R.id.expFlowerCard)
    CardView expFlowerCard;
    @Nullable
    @BindView(R.id.expHarvestCard)
    CardView expHarvestCard;
    @Nullable
    @BindView(R.id.actFlowerCard)
    CardView actFlowerCard;
    @Nullable
    @BindView(R.id.actHarvestCard)
    CardView actHarvestCard;

    private void hideCard(CardView cardView) {
        if (cardView != null)
            cardView.setVisibility(View.GONE);
    }
    private void loadAds(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();

        List<String> testDeviceIds = Arrays.asList("1126E5CF85D37A9661950E74DF5A73FE");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

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
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        setContentView(R.layout.activity_test);

        ButterKnife.bind(this);
        context = this;
        loadAds();
        getAllVisitsOfFarm();
        farmerDetailsLabelTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int[] textLocation = new int[2];
                    farmerDetailsLabelTv.getLocationOnScreen(textLocation);
                    if (event.getRawX() <= textLocation[0] + farmerDetailsLabelTv.getTotalPaddingLeft()) {

                        return true;
                    }
                    if (event.getRawX() >= textLocation[0] + farmerDetailsLabelTv.getWidth() - farmerDetailsLabelTv.getTotalPaddingRight()) {
                        // Right drawable was tapped
//                        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                        farmerDetailsLabelTv.setClickable(false);
                        farmerDetailsLabelTv.setEnabled(false);
                        Intent intent = new Intent(context, FarmDetailsUpdateActivity2.class);
                        startActivityForResult(intent, UPDATE_FARM_REQUEST_CODE);
//                        }
                        return true;
                    }
                }
                return true;
            }
        });
        auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        DataHandler.newInstance().setFarmFullDetails(null);
//        getFullDetailsByMobile();
        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARM_LAT, "");
        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARM_LONG, "");
        initWeatherView();
        //SharedPreferencesMethod.setString(context,SharedPreferencesMethod.SVFARMID,"360");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        resources = getResources();
        viewFailDialog = new ViewFailDialog();

        boolean isOmitanceArea = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_OMITANCE_AREA_ENABLED);
        boolean mode = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE);
        boolean isDelq = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_DELINQUENT_COMP);
        boolean isVetting = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_VETTING_COMP);
        String vetting = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARM_VETTING);
        checkHealthCardParams();
        if (!mode) {
            moreTv.setVisibility(View.VISIBLE);
            moreTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FarmDetailsUpdateActivity2.class);
                    intent.putExtra("isEditDisabled", true);
                    intent.putExtra("title", "" + resources.getString(R.string.farm_detail_label));
                    startActivity(intent);
                }
            });
        } else
            moreTv.setVisibility(View.GONE);
        if (!mode && isDelq && isVetting && AppConstants.isValidString(vetting) && !vetting.equals(AppConstants.VETTING.DELINQUENT)) {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 2f);
            params.setMargins(5, 0, 5, 0);
            delinquentFarmTv.setLayoutParams(params);
            delinquentFarmTv.setVisibility(View.VISIBLE);
            delinquentFarmTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, AddDelinquentActivity.class));
                }
            });

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            params2.setMargins(5, 0, 5, 0);
            directionFarmTv.setLayoutParams(params2);
            directionFarmTv.setVisibility(View.VISIBLE);
            directionFarmTv.setVisibility(View.GONE);
            directionFarmTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetGeoJsonData fetchGpsCordinates2 = new GetGeoJsonData(false, AppConstants.MAP_NAV_MODE.SHOW_NAVIGATION);
                    fetchGpsCordinates2.execute();
                }
            });
        } else {

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 2f);
            params2.setMargins(5, 0, 5, 0);
            directionFarmTv.setLayoutParams(params2);
//            directionFarmTv.setVisibility(View.VISIBLE);
            directionFarmTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetGeoJsonData fetchGpsCordinates2 = new GetGeoJsonData(false, AppConstants.MAP_NAV_MODE.SHOW_NAVIGATION);
                    fetchGpsCordinates2.execute();
                }
            });
            delinquentFarmTv.setVisibility(View.GONE);
        }
//        isOmitanceArea=true;
        if (isOmitanceArea && !mode) {
            omitanceAreaButton.setVisibility(View.VISIBLE);
            omitanceAreaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GetGeoJsonData fetchGpsCordinates = new GetGeoJsonData(false, AppConstants.MAP_NAV_MODE.SHOW_AREA);
                    fetchGpsCordinates.execute();
//                    showDialog(false);
                }
            });
        } else {
            omitanceAreaButton.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.5f);
            params.setMargins(5, 0, 5, 0);
            update_farm_details_button.setLayoutParams(params);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.5f);
            params2.setMargins(5, 0, 5, 0);
            view_farm_area_button.setLayoutParams(params2);
        }

        // SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, "360");
        Glide.with(this)
                .asBitmap()
                .load("https://dashboard-mapimages-prodv010.s3.ap-south-1.amazonaws.com/ndwi/6eCKBKl8VAhctIqtNYstv2ZmVkftEtML7qIydcTbXV8lHf66pPB3P5M=.png")
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        DataHandler.newInstance().setFarmBitmap(resource);
                    }
                });


        compRegCacheManager = CompRegCacheManager.getInstance(this);

        drawerLayout = findViewById(R.id.drawerLayout);

        content = findViewById(R.id.content);
        viewFailDialog = new ViewFailDialog();
        Log.e(TAG, "Person Id " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {
            private float scaleFactor = 6f;

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);
                content.setScaleX(1 - (slideOffset / scaleFactor));
                content.setScaleY(1 - (slideOffset / scaleFactor));
            }
        };
        noOfDays = SharedPreferencesMethod.getInt(context, SharedPreferencesMethod.NO_OF_DAYS_OFFLINE);
        drawerLayout.setScrimColor(Color.WHITE);
        drawerLayout.setDrawerElevation(0f);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        drawer_back_img.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.END))
                    drawerLayout.openDrawer(Gravity.RIGHT);
                else drawerLayout.closeDrawer(Gravity.END);
            }
        });

        //SharedPreferencesMethod.setString(context,SharedPreferencesMethod.SVFARMID,"36");
        activity = this;
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();

        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout_for_farm_details);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        initViews();


        // timeline_recycler_view = findViewById(R.id.timeline_recycler_view);
        // timeline_progress = (ProgressBar) findViewById(R.id.timeline_progress);
        //germination_button = findViewById(R.id.germination_button);
        fabChildAddGermination = findViewById(R.id.fabChildAddGermination);
        //Floating button
        fabParent = (FloatingActionMenu) findViewById(R.id.fabParent);
        //fabParent.setVisibility(View.GONE);
        fabChildSoilSense = findViewById(R.id.fabChildSoilSense);
        fabChildAssignCalendar = findViewById(R.id.fabChildAssignCalendar);
        fabChildSellRecord = findViewById(R.id.fabChildSellRecord);
        fabChildSeedIssue = findViewById(R.id.fabChildSeedIssue);
        fabChildAddVisit = findViewById(R.id.fabChildAddVisit);
        fabChildSoilHealthCard = findViewById(R.id.fabChildSoilHealthCard);
        fabChildAddPld = (FloatingActionButton) findViewById(R.id.fabChildAddPld);
        //fabChildAddHarvest= (FloatingActionButton) findViewById(R.id.fabChildAddHarvest);
        fabChildViewHarvest = (FloatingActionButton) findViewById(R.id.fabChildViewHarvest);
        fabChildViewInputCost = (FloatingActionButton) findViewById(R.id.fabChildViewInputCost);
        fabParent.setClosedOnTouchOutside(true);

        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.HARVEST_ALLOWED)
                && !SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)
                && !SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.PLD_ALLOWED)
                && !SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.GERMINATION_ALLOWED) &&
                !SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.ASSIGN_CALENDAR)
                && !SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.ADD_HC)
                && !SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.SEED_ALLOWED)
                && !SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.VISIT_ALLOWED)) {

            fabParent.setVisibility(View.GONE);
            flag = true;
            Log.e(TAG, "SharedPreferencesMethod.VISIT_ALLOWED IF " + true);
        } else {
            flag = false;
            CompRegCacheManager.getInstance(context).getCompRegData(new CompRegCacheManager.CompRegListener() {
                @Override
                public void onRegLoaded(CompRegModel compRegModel) {
                    if (compRegModel != null) {
                        try {
                            CompRegResult compRegResult = new Gson().fromJson(compRegModel.getData().trim(), CompRegResult.class);
                            if (compRegResult != null) {
                                if (compRegResult.getStatus() != null && !TextUtils.isEmpty(compRegResult.getStatus()) && compRegResult.getStatus().equals("1")) {
                                    fabChildAddVisit.setVisibility(View.VISIBLE);
                                } else {
                                    fabChildAddVisit.setVisibility(View.GONE);
                                }
                            } else {
                                fabChildAddVisit.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            fabChildAddVisit.setVisibility(View.GONE);
                        }
                    } else if (AppConstants.COMP_REG.DEFAULT) {
                        fabChildAddVisit.setVisibility(View.VISIBLE);
                    } else {
                        fabChildAddVisit.setVisibility(View.GONE);
                    }
                }
            }, AppConstants.COMP_REG.VISIT_REPORT);
            CompRegCacheManager.getInstance(context).getCompRegData(new CompRegCacheManager.CompRegListener() {
                @Override
                public void onRegLoaded(CompRegModel compRegModel) {
                    if (compRegModel != null) {
                        try {
                            CompRegResult compRegResult = new Gson().fromJson(compRegModel.getData().trim(), CompRegResult.class);
                            if (compRegResult != null || !mode) {
                                if (!mode && compRegResult.getStatus() != null && !TextUtils.isEmpty(compRegResult.getStatus()) && compRegResult.getStatus().equals("1")) {
                                    fabChildSellRecord.setVisibility(View.VISIBLE);
                                } else {
                                    fabChildSellRecord.setVisibility(View.GONE);
                                }
                            } else {
                                fabChildSellRecord.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            fabChildSellRecord.setVisibility(View.GONE);
                        }
                    } else if (AppConstants.COMP_REG.DEFAULT) {
                        if (!mode)
                            fabChildSellRecord.setVisibility(View.VISIBLE);
                        else
                            fabChildSellRecord.setVisibility(View.GONE);
                    } else {
                        fabChildSellRecord.setVisibility(View.GONE);
                    }
                }
            }, AppConstants.COMP_REG.HARVEST_SELL);
            /*if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.VISIT_ALLOWED)) {
                fabChildAddVisit.setVisibility(View.GONE);
            } else {
                Log.e(TAG, "SharedPreferencesMethod.VISIT_ALLOWED " + true);
            }*/
            if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.SEED_ALLOWED)) {
                fabChildSeedIssue.setVisibility(View.GONE);
            } else {
                Log.e(TAG, "SharedPreferencesMethod.SEED_ALLOWED " + true);
            }
            if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.GERMINATION_ALLOWED)) {
                fabChildAddGermination.setVisibility(View.GONE);
            } else {
                Log.e(TAG, "SharedPreferencesMethod.GERMINATION_ALLOWED " + true);
            }

            CompRegCacheManager.getInstance(context).getCompRegData(new CompRegCacheManager.CompRegListener() {
                @Override
                public void onRegLoaded(CompRegModel compRegModel) {
                    if (compRegModel != null) {
                        try {
                            CompRegResult compRegResult = new Gson().fromJson(compRegModel.getData().trim(), CompRegResult.class);
                            if (compRegResult != null && !mode) {
                                if (!mode && compRegResult.getStatus() != null && !TextUtils.isEmpty(compRegResult.getStatus()) && compRegResult.getStatus().equals("1")) {
                                    fabChildSoilHealthCard.setVisibility(View.VISIBLE);
                                } else {
                                    fabChildSoilHealthCard.setVisibility(View.GONE);
                                }
                            } else {
                                fabChildSoilHealthCard.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            fabChildSoilHealthCard.setVisibility(View.GONE);
                        }
                    } else if (AppConstants.COMP_REG.DEFAULT) {
                        if (!mode)
                            fabChildSoilHealthCard.setVisibility(View.VISIBLE);
                        else
                            fabChildSoilHealthCard.setVisibility(View.GONE);
                    } else {
                        fabChildSoilHealthCard.setVisibility(View.GONE);
                    }
                }
            }, AppConstants.COMP_REG.SOIL_HEALTH_CARD);
            /*if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.ADD_HC)) {
                fabChildSoilHealthCard.setVisibility(View.GONE);
                Log.e(TAG, "SharedPreferencesMethod.ADD_HC " + false);
            } else {
                Log.e(TAG, "SharedPreferencesMethod.ADD_HC " + true);
            }*/

            CompRegCacheManager.getInstance(context).getCompRegData(new CompRegCacheManager.CompRegListener() {
                @Override
                public void onRegLoaded(CompRegModel compRegModel) {
                    if (compRegModel != null) {
                        try {
                            CompRegResult compRegResult = new Gson().fromJson(compRegModel.getData().trim(), CompRegResult.class);
                            if (compRegResult != null && !mode) {
                                if (!mode && compRegResult.getStatus() != null && !TextUtils.isEmpty(compRegResult.getStatus()) && compRegResult.getStatus().equals("1")) {
                                    fabChildAssignCalendar.setVisibility(View.VISIBLE);
                                } else {
                                    fabChildAssignCalendar.setVisibility(View.GONE);
                                }
                            } else {
                                fabChildAssignCalendar.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            fabChildAssignCalendar.setVisibility(View.GONE);
                        }
                    } else if (AppConstants.COMP_REG.DEFAULT) {
                        if (!mode)
                            fabChildAssignCalendar.setVisibility(View.VISIBLE);
                        else
                            fabChildAssignCalendar.setVisibility(View.GONE);
                    } else {
                        fabChildAssignCalendar.setVisibility(View.GONE);
                    }
                }
            }, AppConstants.COMP_REG.CROP_CYCLE);
            /*if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.ASSIGN_CALENDAR)) {
                fabChildAssignCalendar.setVisibility(View.GONE);
            } else {
                Log.e(TAG, "SharedPreferencesMethod.ASSIGN_CALENDAR " + true);
            }*/

            CompRegCacheManager.getInstance(context).getCompRegData(new CompRegCacheManager.CompRegListener() {
                @Override
                public void onRegLoaded(CompRegModel compRegModel) {
                    if (compRegModel != null) {
                        try {
                            CompRegResult compRegResult = new Gson().fromJson(compRegModel.getData().trim(), CompRegResult.class);
                            if (compRegResult != null) {
                                if (compRegResult.getStatus() != null && !TextUtils.isEmpty(compRegResult.getStatus()) && compRegResult.getStatus().equals("1")) {
                                    fabChildViewInputCost.setVisibility(View.VISIBLE);
                                    farmer_expense_layout.setVisibility(View.VISIBLE);
                                } else {
                                    fabChildViewInputCost.setVisibility(View.GONE);
                                    farmer_expense_layout.setVisibility(View.GONE);
                                }
                            } else {
                                fabChildViewInputCost.setVisibility(View.GONE);
                                farmer_expense_layout.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            fabChildViewInputCost.setVisibility(View.GONE);
                            farmer_expense_layout.setVisibility(View.GONE);
                        }
                    } else if (AppConstants.COMP_REG.DEFAULT) {
                        fabChildViewInputCost.setVisibility(View.VISIBLE);
                        farmer_expense_layout.setVisibility(View.VISIBLE);
                    } else {
                        fabChildViewInputCost.setVisibility(View.GONE);
                        farmer_expense_layout.setVisibility(View.GONE);
                    }
                }
            }, AppConstants.COMP_REG.INPUT_RESOURCE_MANAGEMENT);
            if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.HARVEST_ALLOWED)) {
                fabChildViewHarvest.setVisibility(View.GONE);
            } else {
                Log.e(TAG, "SharedPreferencesMethod.HARVEST_ALLOWED " + true);
            }
           /* if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)) {
                fabChildViewInputCost.setVisibility(View.GONE);
                farmer_expense_layout.setVisibility(View.GONE);
            }*/
        }
        fabChildAddVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabParent.isOpened()) {
                    fabParent.close(true);
                }

//                if (!isTodayVisitAdded) {
                startActivityForResult(new Intent(context, TimelineAddVisitActivity2.class), WHOLE_DATA_CHANGE_REQUEST_CODE);
//                } else {
//                    Toasty.info(context, resources.getString(R.string.already_added_visit_for_today), Toast.LENGTH_LONG, false).show();
//                }
            }
        });
        fabChildSoilHealthCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                    Toasty.info(context, resources.getString(R.string.soil_health_card_seen_and_addded_in_online_mode_msg));
                } else {
                    startActivityForResult(new Intent(context, AddSoilHealthCardActivity.class), WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
            }
        });

        fabChildAddPld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabParent.isOpened()) {
                    fabParent.close(true);
                }
                if (fabChildAddPld.getLabelText().equals(resources.getString(R.string.crop_loss_damage_labe)) && noOfDays > 3) {
                    Snackbar snackbar = Snackbar.make(content, context.getResources().getString(R.string.no_of_days_offline_exceeded), Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction(resources.getString(R.string.ok_msg), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                } else {
                    Intent intent = new Intent(context, SubmitPldActivity.class);
                    ActivityOptions options = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                        //finish();

                    } else {
                        startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                        //finish();
                    }
                }
            }
        });

        fabChildViewHarvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabParent.isOpened()) {
                    fabParent.close(true);
                }
                Intent intent = new Intent(context, HarvestSubmitEasyModeActivity.class);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                    //finish();
                } else {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                    //finish();
                }
            }
        });

        fabChildViewInputCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabParent.isOpened()) {
                    fabParent.close(true);
                }
                Intent intent = new Intent(context, InputCostActivity.class);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                } else {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
            }
        });
        xAxisLabel = new ArrayList<>();
        yAxisLabel = new ArrayList<>();
        params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getXAxis().setDrawGridLines(true);
        lineChart.setNoDataText(resources.getString(R.string.no_chart_data_msg));
        onclick();
        loadCompReegData();
        datePickingClicks();
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            progressBar.setVisibility(View.VISIBLE);
            drawerRefreshImg.setVisibility(View.VISIBLE);
            drawerRefreshImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fetchAllDataOnline(false);
                }
            });
            if (isInternetOn()) {
                fetchAllDataOnline(true);
                getFullDetails();
                getCurrentDate();
                if (!SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.FARMER))
                    getParticularCalendar();
                    //fabChildAssignCalendar.setVisibility(View.GONE);
                else
                    fabChildAssignCalendar.setVisibility(View.GONE);
            } else {
                Snackbar.make(drawerLayout, resources.getString(R.string.no_internet_connection), Snackbar.LENGTH_INDEFINITE)
                        .setAction(resources.getString(R.string.ok_label), null).show();
            }
            fabChildAssignCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, CropCycleListActivity.class);
                    intent.putExtra("withListener",true);
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
            });
            BluetoothAdapter mBluetoothAdapter = null;
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            boolean isSoilSens = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_SOIL_SENS_ENABLED);
            /*if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled() || !isSoilSens) {
                fabChildSoilSense.hide(true);
                fabChildSoilSense.setVisibility(View.GONE);
            } else {
            }*/
            fabChildSoilSense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, BluetoothCommunicationActivity.class);
                    intent.putExtra(SharedPreferencesMethod.SVFARMID, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
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
            });
        } else {

            drawerRefreshImg.setVisibility(View.GONE);
            try {
                fabChildSoilSense.setVisibility(View.GONE);
                fabChildSoilSense.setClickable(false);
                fabChildSoilSense.setEnabled(false);
//                fabChildSoilSense.hide(true);
            } catch (Exception e) {

            }
            fabChildAssignCalendar.setVisibility(View.GONE);
            fabChildAssignCalendar.setClickable(false);
            fabChildAssignCalendar.setEnabled(false);

            fabChildSeedIssue.setVisibility(View.GONE);
            fabChildSeedIssue.setClickable(false);
            fabChildSeedIssue.setEnabled(false);

            fabChildSellRecord.setVisibility(View.GONE);
            fabChildSellRecord.setClickable(false);
            fabChildSellRecord.setEnabled(false);

            fabChildSoilHealthCard.setVisibility(View.GONE);
            fabChildSoilHealthCard.setClickable(false);
            fabChildSoilHealthCard.setEnabled(false);
            Log.e(TAG, "FarmId " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
            ActivityCacheManager.getInstance(context).getOfflineVisitCount(this::onActivityCountLoaded);
            VrCacheManager.getInstance(context).getOfflineVisitCount(this::onActivityCountLoaded);
            //TimelineCacheManager.getInstance(context).getAllTimeline(this, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));

        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String noti_sch_id = extras.getString("id");
            if (noti_sch_id != null) {
                SetNotificationAsRead task = new SetNotificationAsRead();
                task.execute(noti_sch_id);
            }

        }


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); //  minute interval
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        findViewById(R.id.timelineFilter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog();
            }
        });

        isListView = true;
        calImage = findViewById(R.id.calImage);
        findViewById(R.id.timelineViewChange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isListView) {
                    recyclerView.setVisibility(View.GONE);
                    chartLayout.setVisibility(View.GONE);
                    isListView = false;
                    calendarLayout.setVisibility(View.VISIBLE);
                    calImage.setImageResource(R.drawable.ic_format_list_numbered);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    chartLayout.setVisibility(View.VISIBLE);
                    isListView = true;
                    calendarLayout.setVisibility(View.GONE);
                    calImage.setImageResource(R.drawable.ic_date_range_white);
                }

            }
        });

    }

    boolean isListView;
    //    int dataCount = 0;
    ImageView calImage;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_FARM_REQUEST_CODE && resultCode == RESULT_OK && !SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            fetchAllDataOnline(false);
        } else if (requestCode == WHOLE_DATA_CHANGE_REQUEST_CODE && resultCode == RESULT_OK && !SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            Log.e(TAG, "onActivityResultSecond");
            progressBar.setVisibility(View.VISIBLE);
            fetchAllDataOnline(true);
            if (!SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.FARMER))
                getParticularCalendar();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityRunning = true;

    }

    @BindView(R.id.areaFab)
    CircleImageView areaFab;

    @Override
    protected void onStart() {
        super.onStart();
        farmerDetailsLabelTv.setClickable(true);
        farmerDetailsLabelTv.setEnabled(true);
        DataHandler.newInstance().setLatLngList(null);
        try {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    //Location Permission already granted
                    fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                } else {
                    //Request Location Permission
//                checkLocationPermission();
                }
            } else {
                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }

            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                if (isListView) {
                    FarmCacheManager.getInstance(context).getFarm(this, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                    ActivityCacheManager.getInstance(context).getOfflineVisitCount(this::onActivityCountLoaded);
                    VrCacheManager.getInstance(context).getOfflineVisitCount(this::onActivityCountLoaded);
                }
                if (!flag)
                    fabParent.setVisibility(View.VISIBLE);
                else
                    fabParent.setVisibility(View.GONE);
            } else {
                //progressBar.setVisibility(View.VISIBLE);
                //fetchAllDataOnline();
                if (!flag)
                    fabParent.setVisibility(View.VISIBLE);
                else
                    fabParent.setVisibility(View.GONE);
            }
            mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isListView) {
                        recyclerView.setVisibility(View.VISIBLE);
                        chartLayout.setVisibility(View.VISIBLE);
                        isListView = true;
                        calendarLayout.setVisibility(View.GONE);
                        calImage.setImageResource(R.drawable.ic_date_range_white);
                    } else
                        TestActivity.super.onBackPressed();
                }
            });

            //getAppTour();

            String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
            if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE) &&
                    SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_DATA_CHANGED_FARM + "_" + farmId)) {
                progressBar.setVisibility(View.VISIBLE);
                if (isInternetOn()) {
                    objectList.clear();
                    objectTypeList.clear();

                    fetchAllDataOnline(true);
                    getCurrentDate();
                    if (!SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.FARMER))
                        getParticularCalendar();
                        //fabChildAssignCalendar.setVisibility(View.GONE);
                    else
                        fabChildAssignCalendar.setVisibility(View.GONE);
                } else {
                    Snackbar.make(drawerLayout, resources.getString(R.string.no_internet_connection), Snackbar.LENGTH_INDEFINITE)
                            .setAction(resources.getString(R.string.ok_label), null).show();
                }
                fabChildAssignCalendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(context, SelectCalendarActivity.class));
                    }
                });
                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_DATA_CHANGED_FARM + "_" + farmId, false);
            }
        } catch (Exception e) {

        }
    }

    public final boolean isInternetOn() {

        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            return false;
        }
        return false;
    }

    List<LineChartInfo> lineChartInfoList = new ArrayList<>();
    FetchFarmResult fetchFarmResult;
    boolean isaFarmLoaded = false;


    private void getCurrentDate() {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getCurrentTime(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID)).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                Log.e(TAG, "Status date " + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG, "Res Date " + new Gson().toJson(response.body()));
                    if (response.body() != null) {
                        todayDate = response.body().getDate();
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        Log.e(TAG, "Err Date " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e(TAG, "Fail Date " + t.toString());
            }
        });
    }

    private void fetchAllDataOnline(boolean flag) {
        if (flag) {
            objectList.clear();
            objectTypeList.clear();

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String todayDateStr = df.format(c);
            weatherForecast.setDoa(todayDateStr);
            weatherForecast.setIsLoaded('N');
            objectList.add(weatherForecast);
            objectTypeList.add(new ObjectType(todayDateStr, AppConstants.TIMELINE.WR));
            if (adapterTest != null)
                adapterTest.notifyDataSetChanged();
        } else {
            progressBar.setVisibility(View.VISIBLE);
//            Toast.makeText(context, "Refreshing farm data", Toast.LENGTH_SHORT).show();
        }
        String farm_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        Log.e(TAG, "farmId " + farm_id + " And CompId " + comp_id);
        Log.e(TAG, "UserId " + userId);
        Log.e(TAG, "Token " + token);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        Call<FullDetailsResponse> farmAndHarvestCall = apiInterface.getFarmAndHarvestData(farm_id, comp_id, userId, token);
        farmAndHarvestCall.enqueue(new Callback<FullDetailsResponse>() {
            @Override
            public void onResponse(Call<FullDetailsResponse> call, Response<FullDetailsResponse> response) {
                Log.e(TAG, "Status farm " + response.code());
                isLoaded[0] = true;
                String error = "";
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 10) {
                            isaFarmLoaded = false;
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            if (response.body().getExpense() != null) {
                                total_expense_tv.setText(response.body().getExpense());
                            }
                            FullDetailsResponse detailsResponse = response.body();
                            if (detailsResponse.getFarmData() != null) {
                                isaFarmLoaded = true;
                                fetchFarmResult = detailsResponse.getFarmData();
                                fetchFarmResult.setType(AppConstants.TIMELINE.FARM);
                                String date = "2010-01-21";
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARM_CLUSTERID, detailsResponse.getFarmData().getClusterId());
                                if (fetchFarmResult.getDoa() != null && !TextUtils.isEmpty(fetchFarmResult.getDoa()))
                                    date = fetchFarmResult.getDoa();
                                else fetchFarmResult.setDoa(date);

                                setFarmDataOnline(fetchFarmResult);
                                germinationDataSet(fetchFarmResult);
                                if (detailsResponse.getFarmData().getActualArea() != null) {
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTUAL_AREA, detailsResponse.getFarmData().getActualArea().trim());
                                }
                                if (detailsResponse.getFarmData().getStandingAcres() != null) {
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, detailsResponse.getFarmData().getStandingAcres().trim());
                                }
                                if (detailsResponse.getFarmData().getExpArea() != null) {
                                    SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.EXPECTED_AREA,
                                            Float.valueOf(detailsResponse.getFarmData().getExpArea()));
                                }
                                if (detailsResponse.getFarmData().getGermination() != null &&
                                        !TextUtils.isEmpty(detailsResponse.getFarmData().getGermination())
                                        && !detailsResponse.getFarmData().getGermination().equals("[]")) {
                                    SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.GERMINATION,
                                            Float.valueOf(detailsResponse.getFarmData().getGermination().trim()));
                                } else {
                                    SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.GERMINATION,
                                            0.0);
                                }
                                String sowingDate = response.body().getFarmData().getSowingDate();
                                if (sowingDate != null && !TextUtils.isEmpty(sowingDate))
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SOWING_DATE, sowingDate);
                                if (detailsResponse.getGrade() != null /*&& detailsResponse.getGrade().size() > 0*/) {
                                    lineChartInfoList.addAll(detailsResponse.getGrade());
                                    Log.e(TAG, "linechart if");
                                } else {
                                    Log.e(TAG, "linechart else");
                                }

                                AVG_GERMI = detailsResponse.getFarmData().getGermination();
                                AVG_POPU = detailsResponse.getFarmData().getPopulation();
                                AVG_PP_SPACING = detailsResponse.getFarmData().getSpacingPtp();
                                AVG_RR_SPACING = detailsResponse.getFarmData().getSpacingRtr();

                                farmResult = detailsResponse.getFarmData();
                                if (flag) {
                                    List<Object> objectList = new ArrayList<>();
                                    List<ObjectType> objectTypeList = new ArrayList<>();
                                    objectList.add( fetchFarmResult);
                                    MyCustomLayoutManager manager = new MyCustomLayoutManager(context);
                                    if (fetchFarmResult.getDoa() != null && !TextUtils.isEmpty(fetchFarmResult.getDoa()))
                                        objectTypeList.add( new ObjectType(fetchFarmResult.getDoa(), AppConstants.TIMELINE.FARM));
                                    else
                                        objectTypeList.add(0, new ObjectType("2010-01-21", AppConstants.TIMELINE.FARM));
                                    adapterTest = new TimelineAdapterTest(context, context, objectList, objectTypeList, null, isCurrentLocation, new TimelineAdapterTest.FarmDetailsClickListener() {
                                        @SuppressLint("WrongConstant")
                                        @Override
                                        public void onFarmDetailsClicked() {
                                            if (!drawerLayout.isDrawerOpen(GravityCompat.END))
                                                drawerLayout.openDrawer(Gravity.RIGHT);
                                            else drawerLayout.closeDrawer(Gravity.END);
                                        }

                                        @Override
                                        public void onMapIconClicked() {
                                            GetGeoJsonData fetchGpsCordinates = new GetGeoJsonData(true, AppConstants.MAP_NAV_MODE.SHOW_AREA);
                                            fetchGpsCordinates.execute();
                                        }
                                    });
                                    adapterTest.setOnSoilCardClick(true);
                                    recyclerView.setLayoutManager(manager);
                                    recyclerView.setAdapter(adapterTest);
                                    getCalendarData();
                                } else
                                    progressBar.setVisibility(View.GONE);


                            } else {
                                if (flag) {
                                    getCalendarData();
                                } else
                                    progressBar.setVisibility(View.GONE);
                                isaFarmLoaded = false;
                            }

                        }
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        isaFarmLoaded = false;
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "Unsuccess " + error);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isActivityRunning) {
                                    if (flag)
                                        getCalendarData();
                                    else
                                        progressBar.setVisibility(View.GONE);
//                                    viewFailDialog.showDialogForFinish(context, resources.getString(R.string.opps_message), resources.getString(R.string.fail_to_load_farm_data));
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        isaFarmLoaded = false;
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(TestActivity.this, "android_vr/get_harvest_farm",
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<FullDetailsResponse> call, Throwable t) {
                isaFarmLoaded = false;
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(TestActivity.this, "android_vr/get_harvest_farm",
                        "" + diff, internetSPeed, t.toString(), 0);
                if (flag)
                    getCalendarData();
                else
                    progressBar.setVisibility(View.GONE);
                //something went wrong
                Log.e(TAG, t.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isActivityRunning) {

                            viewFailDialog.showDialogForFinish(context, resources.getString(R.string.opps_message), resources.getString(R.string.fail_to_load_farm_data));
                        }
                    }
                });
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
                            ConnectivityUtils.checkSpeedWithColor(TestActivity.this, new ConnectivityUtils.ColorListener() {
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

    private boolean isCurrentLocation;

    /*private void callWeatherApi(double latitude, double longitude, String from, boolean isCurrentLocation) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> address = null;
        try {
            address = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (address != null && address.size() > 0) {
                cityName = address.get(0).getSubAdminArea();
                stateName = address.get(0).getAdminArea();
                countryName = address.get(0).getCountryName();
                Log.e(TAG, "Address " + new Gson().toJson(address));
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        this.isCurrentLocation = isCurrentLocation;
        Log.e(TAG, "Calling Weather Api  " + from + "  Latitude " + latitude + ", Longitude " + longitude);

        Retrofit retrofit = RetrofitClient.getClient("https://api.darksky.net/forecast/f956826eace7ccc50533c39ef3a3a9fb/");
        ApiInterface weatherService = retrofit.create(ApiInterface.class);
        Call<WeatherForecast> forecastCall = weatherService.getWeather(String.valueOf(latitude).trim() + "," + String.valueOf(longitude).trim() + "?units=si");
        forecastCall.enqueue(new Callback<WeatherForecast>() {
            @Override
            public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                if (response.isSuccessful()) {
                    weatherForecast = response.body();
                    if (weatherForecast != null) {
                        weatherForecast.setIsLoaded('Y');
                        weatherForecast.setCityName(cityName);
                        weatherForecast.setStateName(stateName);
                        weatherForecast.setCountryName(countryName);
                        weatherForecast.setType(AppConstants.TIMELINE.WR);
                        adapterTest.notifyWeather(weatherForecast, isCurrentLocation);
                    }

                } else {
                    try {
                        Log.e(TAG, "error weather " + response.errorBody().string().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherForecast> call, Throwable t) {
                Log.e(TAG, "WeatherFailure " + t.toString());
            }
        });
    }*/


    private void callWeatherApi(double latitude, double longitude, String from, boolean isCurrentLocation) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> address = null;
        try {
            address = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (address != null && address.size() > 0) {
                cityName = address.get(0).getSubAdminArea();
                stateName = address.get(0).getAdminArea();
                countryName = address.get(0).getCountryName();
                Log.e(TAG, "Address " + new Gson().toJson(address));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.isCurrentLocation = isCurrentLocation;
        Log.e(TAG, "Calling Weather Api  " + from + "  Latitude " + latitude + ", Longitude " + longitude);
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(auth);
        ApiInterface weatherService = retrofit.create(ApiInterface.class);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String farmID = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        Call<WeatherMainRes> forecastCall = weatherService.getWeatherData(userId, token, String.valueOf(longitude).trim(), String.valueOf(latitude).trim(), farmID);
        forecastCall.enqueue(new Callback<WeatherMainRes>() {
            @Override
            public void onResponse(Call<WeatherMainRes> call, Response<WeatherMainRes> response) {
                Log.e(TAG, "Status weather " + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG, "Status weather " + new Gson().toJson(response.body()));
                    if (response.body() != null && response.body().getWeatherForecast() != null) {
                        weatherForecast = response.body().getWeatherForecast();
                        if (weatherForecast != null) {
                            weatherForecast.setIsLoaded('Y');
                            weatherForecast.setCityName(cityName);
                            weatherForecast.setStateName(stateName);
                            weatherForecast.setCountryName(countryName);
                            weatherForecast.setType(AppConstants.TIMELINE.WR);
                            adapterTest.notifyWeather(weatherForecast, isCurrentLocation);
                            if (lineChartInfoList == null || lineChartInfoList.size() == 0) {
                                //configureWeather();
                            }
                        }
                    }

                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        Log.e(TAG, "error weather " + response.errorBody().string().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherMainRes> call, Throwable t) {
                Log.e(TAG, "WeatherFailure " + t.toString());
            }
        });
    }

    private void configureWeather() {
        if (weatherForecast != null && weatherForecast.getIsLoaded() == 'Y') {
            weatherLay.setVisibility(View.VISIBLE);
            if (isCurrentLocation)
                weather_desclaimer_msg_tv.setText(context.getResources().getString(R.string.farm_coordinates_not_availabe_showing_weather_data_of_your_current_location));
            else
                weather_desclaimer_msg_tv.setText(context.getResources().getString(R.string.showing_weather_data_of_your_farm_location));

            String cityName = "";
            String stateName = "";
            String countryName = "";
            if (weatherForecast.getCityName() != null)
                cityName = weatherForecast.getCityName();
            if (weatherForecast.getCityName() != null)
                stateName = weatherForecast.getStateName();
            if (weatherForecast.getCityName() != null)
                countryName = weatherForecast.getCountryName();
            weatherCityNameTv.setText(cityName + ", " + stateName + ", " + countryName);
            String icon = (String.valueOf(weatherForecast.getCurrently().getIcon()));
            DecimalFormat format = new DecimalFormat("##.##");
            double precipitation_val = Math.round(weatherForecast.getDaily().getData().get(0).getPrecipIntensity());
            txt_curr_temp.setText(String.valueOf(Math.round(weatherForecast.getCurrently().getTemperature()) + "\u00B0" + " "));
            txt_max_temp.setText(String.valueOf(Math.round(weatherForecast.getDaily().getData().get(0).getApparentTemperatureHigh()) + "\u00b0"));
            txt_min_temp.setText(String.valueOf(Math.round(weatherForecast.getDaily().getData().get(0).getApparentTemperatureLow()) + "\u00b0c"));
            rainfall.setText(context.getResources().getString(R.string.rainfall_label) + " " + String.valueOf(format.format(precipitation_val)) + " " + context.getResources().getString(R.string.mm_unit));

            select_skycon(icon, skycon_view);
            Log.e("WeatherResp", new Gson().toJson(weatherForecast.getCurrently()));
            weatherLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("inside_onClick", "done");
                    Intent intent = new Intent(context, WeatherActivity.class);
                    intent.putExtra("weatherForecast", (Serializable) weatherForecast);
                    context.startActivity(intent);
                }
            });
            Log.e("TimelineAdapterTest", "Weather is not null");
        } else {
            weatherLay.setVisibility(View.GONE);
            Log.e("TimelineAdapterTest", "Weather is null");
        }
    }

    public void select_skycon(String icon, ImageView skycon_view) {
        switch (icon) {
            case "clear-day": {

                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_day));
                break;
            }
            case "clear-night": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_night));
                break;
            }
            case "rain": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rainy));
                break;
            }
            case "snow": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_snow));
                break;
            }
            case "sleet": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_sleet));
                break;
            }
            case "wind": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_windy));
                break;
            }
            case "fog": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_fog));
                break;
            }
            case "cloudy": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cloudy));
                break;
            }
            case "partly-cloudy-day": {

                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cloudy_day_2));
                break;
            }
            case "partly-cloudy-night": {

                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cloudy_night_2));
                break;
            }
            case "hail": {

                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_hail));
                break;
            }
            case "thunderstorm": {

                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thunder));
                break;
            }
        }
    }

    private void getCalendarData() {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        final String farm_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comp_id", "" + comp_id);
        jsonObject.addProperty("farm_id", "" + farm_id);
        jsonObject.addProperty("user_id", "" + userId);
        jsonObject.addProperty("token", "" + token);
        Log.e(TAG, "getCalendarData " + new Gson().toJson(jsonObject));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        Call<TimelineResponse> getCalenderData = apiInterface.getCalendarData(comp_id, farm_id, userId, token);
        getCalenderData.enqueue(new Callback<TimelineResponse>() {
            @Override
            public void onResponse(Call<TimelineResponse> call, retrofit2.Response<TimelineResponse> response) {
                Log.e(TAG, "Calendar Status " + response.code());
                isLoaded[0] = true;
                String error = "";
                if (response.isSuccessful()) {
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String todayDateStr = df.format(c);
                    TimelineResponse timelineResponse = response.body();
                    if (response.body().getStatus() == 10) {
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                    } else if (timelineResponse.getStatus() == 1) {
                        boolean added = false;
                        List<TimelineInnerData> innerDataPending = new ArrayList<>();
                        for (int position = 0; position < timelineResponse.getFarmCalData().size(); position++) {
                            Log.e(TAG, "Calendar " + position + " " + timelineResponse.getFarmCalData().get(position).getImg_link());
                            Log.e(TAG, "TimeLine " + position + " " + new Gson().toJson(timelineResponse.getFarmCalData().get(position)));
                            if (timelineResponse.getFarmCalData().get(position).getFarmCalActivityId()
                                    != null && !timelineResponse.getFarmCalData().get(position).getFarmCalActivityId().equals("0")) {
                                TimelineInnerData innerData = timelineResponse.getFarmCalData().get(position);
                                innerData.setType(AppConstants.TIMELINE.ACT);
                                innerData.setDoa(innerData.getDate());
//
                                objectList.add(innerData);
                                objectTypeList.add(new ObjectType(timelineResponse.getFarmCalData().get(position).getDate(), AppConstants.TIMELINE.ACT));
                                if (innerData.getIsDone() != null && innerData.getIsDone().equals("N")) {
                                    innerDataPending.add(innerData);
                                }
                            } else if (timelineResponse.getFarmCalData().get(position).getVisitReportId() != null &&
                                    !timelineResponse.getFarmCalData().get(position).getVisitReportId().equals("0")) {
                                TimelineInnerData innerData = timelineResponse.getFarmCalData().get(position);
                                innerData.setDoa(innerData.getDate());
                                Log.e(TAG, "Calendar " + position + " " + new Gson().toJson(innerData));

                                if (innerData.getResultType() != null && innerData.getResultType().trim().equals("dr")) {
                                    objectList.add(innerData);
                                    innerData.setType(AppConstants.TIMELINE.DELINQUENT);
                                    objectTypeList.add(new ObjectType(timelineResponse.getFarmCalData().get(position).getDate(), AppConstants.TIMELINE.DELINQUENT));
                                } else {
                                    objectList.add(innerData);
                                    innerData.setType(AppConstants.TIMELINE.VR);
                                    objectTypeList.add(new ObjectType(timelineResponse.getFarmCalData().get(position).getDate(), AppConstants.TIMELINE.VR));
                                }

                                if (!added) {
                                    Date todayDate = null, farmDate = null;
                                    try {
                                        farmDate = df.parse(innerData.getDate());
                                        todayDate = df.parse(todayDateStr);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        Log.e(TAG, "Outer 2 Date Exception " + e.toString());
                                    }
                                    if (todayDate != null && farmDate != null && todayDate.equals(farmDate)) {
                                        isTodayVisitAdded = true;
                                        added = true;
                                    } else {
                                        isTodayVisitAdded = false;
                                    }
                                }
                            }
                            DataHandler.newInstance().setInnerDataPending(innerDataPending);
                        }

                        getSoilSensData();


                    } else {
                        getSoilSensData();
                    }
                    Log.e(TAG, "Calendar " + new Gson().toJson(response.body()));
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        getSoilSensData();
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "Calendar Unsuccess " + error);
//                        if (isActivityRunning)
//                            viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_load_timeline));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(TestActivity.this, call.request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<TimelineResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                isLoaded[0] = true;
                long diff = newMillis - currMillis;
                notifyApiDelay(TestActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "Calendar Failure " + t.toString());
//                viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_load_timeline));
                getSoilSensData();

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

    private void getSoilSensData() {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        final String farm_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        Call<SoilSenseResponse> getCalenderData = apiInterface.getSoilSensData(comp_id, farm_id, userId, token);
        getCalenderData.enqueue(new Callback<SoilSenseResponse>() {
            @Override
            public void onResponse(Call<SoilSenseResponse> call, retrofit2.Response<SoilSenseResponse> response) {
                Log.e(TAG, "SoilSenseResponse Status " + response.code());
                isLoaded[0] = true;
                String error = "";
                if (response.isSuccessful()) {
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String todayDateStr = df.format(c);
                    SoilSenseResponse timelineResponse = response.body();
                    if (response.body().getStatus() == 10) {
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                    } else if (timelineResponse.getStatus() == 1) {
                        for (int position = 0; position < timelineResponse.getData().size(); position++) {
                            GetSoilDatum data = timelineResponse.getData().get(position);
                            /*if (data.getDetails()!=null&&data.getDoa()!=null){
                                objectList.add(data);
                                objectTypeList.add(new ObjectType(data.getDoa(), AppConstants.TIMELINE.SOIL_SENSE));
                            }else if (data.getDetail()!=null){
                                objectList.add(data);
                                objectTypeList.add(new ObjectType(data.getDoa(), AppConstants.TIMELINE.SOIL_SENSE));
                            }*/

                            objectList.add(data);
                            objectTypeList.add(new ObjectType(data.getDoa(), AppConstants.TIMELINE.SOIL_SENSE));
                        }
                        fetcHarvestData();
                    } else {
                        fetcHarvestData();
                    }
                    Log.e(TAG, "SoilSenseResponse " + new Gson().toJson(response.body()));
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        fetcHarvestData();
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "SoilSenseResponse Unsuccess " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(TestActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }

//                SendSoilData data=new SendSoilData("0","0","","","lat","lon",new SoilDetails("19.9"));
//                data.setDoa("2020-10-14");
//                objectList.add(data);
//                objectTypeList.add(new ObjectType(data.getDoa(), AppConstants.TIMELINE.SOIL_SENSE));
            }

            @Override
            public void onFailure(Call<SoilSenseResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                isLoaded[0] = true;
                long diff = newMillis - currMillis;
                notifyApiDelay(TestActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "SoilSenseResponse Failure " + t.toString());
                fetcHarvestData();

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


    private void fetcHarvestData() {
        try {
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            final boolean[] isLoaded = {false};
            long currMillis = AppConstants.getCurrentMills();
            Call<ViewHarvestDetails> callHarvestData = apiService.getHarvestDetailStatus(
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));

            callHarvestData.enqueue(new Callback<ViewHarvestDetails>() {
                @Override
                public void onResponse(Call<ViewHarvestDetails> call, Response<ViewHarvestDetails> response) {
                    String error = null;
                    isLoaded[0] = true;
                    try {
                        Log.e(TAG, "Harvest Data code " + response.code());

                        if (response.isSuccessful()) {
                            Log.e(TAG, "Harvest Data " + new Gson().toJson(response.body()));
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.multiple_login_msg));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                            } else {
                                if (response.body() != null) {
                                    ViewHarvestDetails harvestDetails = response.body();
                                    if (harvestDetails.getData1() != null && harvestDetails.getData1().size() > 0) {
                                        int lastBagNo = 0;
                                        for (int i = 0; i < harvestDetails.getData().size(); i++) {
                                            TimelineHarvest timelineHarvest = new TimelineHarvest();
                                            timelineHarvest.setType(AppConstants.TIMELINE.HR);
                                            for (int j = 0; j < harvestDetails.getData().get(i).size(); j++) {
                                                lastBagNo = Integer.parseInt(harvestDetails.getData().get(i).get(j).getBagNo());
                                            }
                                            timelineHarvest.setHarvestDetailInnerDataList(harvestDetails.getData().get(i));
                                            timelineHarvest.setHarvestDetailMaster(harvestDetails.getData1().get(i));
                                            timelineHarvest.setDoa(timelineHarvest.getHarvestDetailMaster().getDoa());
//                                            dataCount++;
//                                            timelineHarvest.setIndex(dataCount);
                                            objectList.add(timelineHarvest);
                                            objectTypeList.add(new ObjectType(timelineHarvest.getHarvestDetailMaster().getDoa(), AppConstants.TIMELINE.HR));
                                        }
                                        SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.LAST_BAG_NO, lastBagNo + 1);
                                        getGerminationData();
                                    } else {
                                        SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.LAST_BAG_NO, 1);
                                        getGerminationData();
                                    }
                                } else {
                                    getGerminationData();
                                }
                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            try {
                                getGerminationData();
                                error = response.errorBody().string().toString();
                                Log.e(TAG, "Harvest Err" + error);
                                //Toast.makeText(context, "Oops something went wrong. Please Reload", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Harvest Exc" + e.toString());

                    }


                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                        notifyApiDelay(TestActivity.this, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }
                }

                @Override
                public void onFailure(Call<ViewHarvestDetails> call, Throwable t) {
                    long newMillis = AppConstants.getCurrentMills();
                    isLoaded[0] = true;
                    long diff = newMillis - currMillis;
                    Log.e(TAG, "Failure Harvest Data " + t.getMessage().toString());
                    notifyApiDelay(TestActivity.this, call.request().url().toString(),
                            "" + diff, internetSPeed, t.toString(), 0);
                    getGerminationData();

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

    }

    TimelineAdapterTest adapterTest;
    public static Date lastSelectdeDate;

    private void setRecyclerData() {
//        setResult();
        RecyclerAsync async = new RecyclerAsync();
        async.execute();

    }

    private void getGerminationData() {
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            SendGerminationSpacingData sendGerminationSpacingData = new SendGerminationSpacingData();
            sendGerminationSpacingData.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
            sendGerminationSpacingData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            sendGerminationSpacingData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            final boolean[] isLoaded = {false};
            long currMillis = AppConstants.getCurrentMills();
            final Call<SampleGerminationStatusNdData> sampleGerminationDatumCall = apiService.getSampleGermiData(sendGerminationSpacingData);
            sampleGerminationDatumCall.enqueue(new Callback<SampleGerminationStatusNdData>() {
                @Override
                public void onResponse(Call<SampleGerminationStatusNdData> call, Response<SampleGerminationStatusNdData> response) {
                    Log.e(TAG, "Status Ger " + response.code());
                    String error = null;
                    isLoaded[0] = true;
                    if (response.isSuccessful()) {
                        SampleGerminationStatusNdData sampleGerminationStatusNdData = response.body();
                        Log.e(TAG, "Ger response " + new Gson().toJson(sampleGerminationStatusNdData));
                        try {
                            if (response.isSuccessful()) {
                                if (response != null) {
                                    if (sampleGerminationStatusNdData.getStatus() == 1) {
                                        germinationDataList.addAll(sampleGerminationStatusNdData.getData());
                                        for (int i = 0; i < sampleGerminationStatusNdData.getData().size(); i++) {
                                            SampleGerminationDatum data = sampleGerminationStatusNdData.getData().get(i);
                                            if (data.getDoa() != null && !TextUtils.isEmpty(data.getDoa())) {
                                                data.setType(AppConstants.TIMELINE.GE);
//                                                dataCount++;
//                                                data.setIndex(dataCount);
                                                objectList.add(data);
                                                objectTypeList.add(new ObjectType(data.getDoa(), AppConstants.TIMELINE.GE));
                                            } else {
                                                Log.e(TAG, "Germi no doa at " + i);
                                            }
                                        }
                                        getProduceSell();

                                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                        //un authorized access
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                        // auth token expired
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                                    } else if (sampleGerminationStatusNdData.getStatus() == 10) {
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.multiple_login_msg));
                                    } else {
                                        getProduceSell();
                                        Log.e(TAG, "status 0 " + new Gson().toJson(response.body()));
                                    }
                                } else {
                                    getProduceSell();
                                }
                            } else {
                                try {
                                    getProduceSell();
                                    error = response.errorBody().string().toString();
                                    Log.e("DataError", error);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            getProduceSell();
                        }


                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            getProduceSell();
                            error = response.errorBody().string().toString();
                            Log.e("DataErrorFailure", error);
                            //Toast.makeText(context, "Oops something went wrong. Please try again", Toast.LENGTH_SHORT).show();
//                            Toasty.error(context, resources.getString(R.string.opps_message) + " " + resources.getString(R.string.something_went_wrong_msg), Toast.LENGTH_LONG, false).show();
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    }
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                        notifyApiDelay(TestActivity.this, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }

                }

                @Override
                public void onFailure(Call<SampleGerminationStatusNdData> call, Throwable t) {
                    getProduceSell();
                    isLoaded[0] = true;
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    notifyApiDelay(TestActivity.this, call.request().url().toString(),
                            "" + diff, internetSPeed, t.toString(), 0);

//                    Toasty.error(context, resources.getString(R.string.opps_message) + " " + resources.getString(R.string.something_went_wrong_msg), Toast.LENGTH_LONG, false).show();

                    Log.e("Failure", t.toString());

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
    }

    private void getProduceSell() {
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            final boolean[] isLoaded = {false};
            long currMillis = AppConstants.getCurrentMills();
            apiService.getSellData(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID))
                    .enqueue(new Callback<SellResponse>() {
                        @Override
                        public void onResponse(Call<SellResponse> call, Response<SellResponse> response) {
                            Log.e(TAG, "Status sell " + response.code());
                            String error = null;
                            isLoaded[0] = true;
                            if (response.isSuccessful()) {
                                SellResponse data = response.body();
                                Log.e(TAG, "Status sell " + new Gson().toJson(data));
                                try {
                                    if (response.isSuccessful()) {
                                        if (response != null) {
                                            if (data.getStatus() == 1) {

                                                for (int i = 0; i < data.getData().size(); i++) {
                                                    SellData sellData = data.getData().get(i);
                                                    if (sellData.getDoa() != null && !TextUtils.isEmpty(sellData.getDoa())) {
                                                        sellData.setType(AppConstants.TIMELINE.SELL);
//                                                        dataCount++;
//                                                        sellData.setIndex(dataCount);
                                                        objectList.add(sellData);
                                                        objectTypeList.add(new ObjectType(sellData.getDoa(), AppConstants.TIMELINE.SELL));
                                                    } else {
                                                        Log.e(TAG, "Sell no doa at " + i);
                                                    }
                                                }
                                                getHealthCardData();

                                            } else if (data.getStatus() == 10) {
                                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                                viewFailDialog.showSessionExpireDialog(TestActivity.this);
                                            } else {
                                                getHealthCardData();
                                                Log.e(TAG, "Sell status 0 " + new Gson().toJson(response.body()));
                                            }
                                        }
                                    } else {
                                        try {
                                            getHealthCardData();

                                            Log.e(TAG, "Sell DataError " + response.errorBody().string().toString());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (Exception e) {
                                }


                            } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                            } else {
                                try {
                                    getHealthCardData();
                                    error = response.errorBody().string().toString();
                                    Log.e(TAG, "Sell DataErrorFailure " + error);
                                    //Toast.makeText(context, "Oops something went wrong. Please try again", Toast.LENGTH_SHORT).show();
//                                    Toasty.error(context, resources.getString(R.string.opps_message) + " " + resources.getString(R.string.something_went_wrong_msg), Toast.LENGTH_LONG, false).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }


                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                                notifyApiDelay(TestActivity.this, response.raw().request().url().toString(),
                                        "" + diff, internetSPeed, error, response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<SellResponse> call, Throwable t) {
                            getHealthCardData();
                            isLoaded[0] = true;
                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            notifyApiDelay(TestActivity.this, call.request().url().toString(),
                                    "" + diff, internetSPeed, t.toString(), 0);
                            Log.e("Failure", t.toString());

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
    }

    private void getHealthCardData() {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getPreviousHealthCard(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID)).enqueue(new Callback<HealthCardResponse>() {
            @Override
            public void onResponse(Call<HealthCardResponse> call, Response<HealthCardResponse> response) {
                Log.e(TAG, "Status HC " + response.code());
                String error = null;
                isLoaded[0] = true;
                if (response.isSuccessful()) {
                    Log.e(TAG, "Status HC res " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 10) {
                        viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == 1) {
                        for (int i = 0; i < response.body().getHealthCardList().size(); i++) {
                            HealthCard card = response.body().getHealthCardList().get(i);
                            card.setType(AppConstants.TIMELINE.HC);
                            if (card != null && card.getDoa() != null && !TextUtils.isEmpty(card.getDoa())) {
//                                dataCount++;
//                                card.setIndex(dataCount);
                                objectList.add(card);
                                objectTypeList.add(new ObjectType(card.getDoa(), AppConstants.TIMELINE.HC));
                            } else
                                Log.e(TAG, "HealthCard no doa at " + i);
                        }
                        getFarmPldList();
                    } else {
                        getFarmPldList();
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired

                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        getFarmPldList();
                        Log.e(TAG, "Health Card Err " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(TestActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<HealthCardResponse> call, Throwable t) {
                Log.e(TAG, "Health Card Fail " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(TestActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                getFarmPldList();
            }
        });
    }

    private void getFarmPldList() {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getPldReasonListOfFarm(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID)).enqueue(new Callback<PldResponse>() {
            @Override
            public void onResponse(Call<PldResponse> call, Response<PldResponse> response) {
                Log.e(TAG, "Status pld " + response.code());
                String error = null;
                isLoaded[0] = true;
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 10) {
                        viewFailDialog.showSessionExpireDialog(TestActivity.this, response.body().getMsg());
                    } else if (response.body().getStatus() == 1) {
                        Log.e(TAG, "Pld Data res " + new Gson().toJson(response.body()));
                        if (response.body().getData() != null && response.body().getData().size() > 0) {
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                PldData data = response.body().getData().get(i);
                                data.setType(AppConstants.TIMELINE.PLD);
                                if (data.getDoa() != null && !TextUtils.isEmpty(data.getDoa())) {
//                                    dataCount++;
//                                    data.setIndex(dataCount);
                                    objectList.add(data);
                                    objectTypeList.add(new ObjectType(data.getDoa(), AppConstants.TIMELINE.PLD));
                                } else
                                    Log.e(TAG, "pld no doa at " + i);

                            }
                            getInputCosts();
                        } else {
                            getInputCosts();
                        }
                    } else {
                        getInputCosts();
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        getInputCosts();
                        error = response.errorBody().string();
                        Log.e(TAG, "Pld Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(TestActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<PldResponse> call, Throwable t) {
                Log.e(TAG, "Pld Fail " + t.toString());
                long newMillis = AppConstants.getCurrentMills();
                isLoaded[0] = true;
                long diff = newMillis - currMillis;
                notifyApiDelay(TestActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                getInputCosts();
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

    private void getInputCosts() {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        // farmId="1";
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getInputCostList(farmId, userId, token).enqueue(new Callback<InputCostResponse>() {

            @Override
            public void onResponse(Call<InputCostResponse> call, Response<InputCostResponse> response) {
                Log.e(TAG, "Status IC " + response.code());
                String error = null;
                isLoaded[0] = true;
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.multiple_login_msg));
                    } else {

                        Log.e(TAG, "Show Data INPUT COST " + new Gson().toJson(response.body().getInputCostList()));
                        Log.e(TAG, "Show Data RESOURCE " + new Gson().toJson(response.body().getResourceList()));
                        int inputCostSize = 0;
                        int resourceSize = 0;

                        if (response.body().getInputCostList() != null && response.body().getInputCostList().size() > 0) {
                            inputCostSize = response.body().getInputCostList().size();

                            for (int i = 0; i < inputCostSize; i++) {
                                InputCostData input = response.body().getInputCostList().get(i);
                                input.setTypeC(AppConstants.TIMELINE.IC);
                                input.setDoa(input.getAddedOn());
                                if (input.getAddedOn() != null && !TextUtils.isEmpty(input.getAddedOn())) {
//                                    dataCount++;
//                                    input.setIndex(dataCount);
                                    objectList.add(input);
                                    objectTypeList.add(new ObjectType(input.getAddedOn(), AppConstants.TIMELINE.IC));
                                } else
                                    Log.e(TAG, "input no doa at " + i);

                            }

                        } else {
                            inputCostSize = 0;
                        }
                        if (response.body().getResourceList() != null && response.body().getResourceList().size() > 0) {
                            resourceSize = response.body().getResourceList().size();

                            for (int i = 0; i < resourceSize; i++) {
                                ResourceData res = response.body().getResourceList().get(i);
                                res.setTypeC(AppConstants.TIMELINE.RU);
                                res.setDoa(res.getAddedOn());
                                if (res.getAddedOn() != null && !TextUtils.isEmpty(res.getAddedOn())) {
                                    objectList.add(res);
                                    objectTypeList.add(new ObjectType(res.getAddedOn(), AppConstants.TIMELINE.RU));
                                } else
                                    Log.e(TAG, "resource no doa at " + i);

                            }

                        } else {
                            resourceSize = 0;
                        }
                        if (resourceSize == 0 && inputCostSize == 0) {

                        }

                        //setRecyclerData();
                        getSatSureData();

                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        //setRecyclerData();
                        getSatSureData();
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "Input Cost Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(TestActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<InputCostResponse> call, Throwable t) {
                //setRecyclerData();
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(TestActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                getSatSureData();
                Log.e(TAG, "Input Cost Failure " + t.toString());

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


    private void showFilterDialog() {

        View view = getLayoutInflater().inflate(R.layout.timeline_filter_layout, null);

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
        final TextView filterActivityTv = view.findViewById(R.id.filterActivityTv);
        final TextView filterVisitTv = view.findViewById(R.id.filterVisitTv);
        final TextView filterGerminationTv = view.findViewById(R.id.filterGerminationTv);
        final TextView filterHarvestTv = view.findViewById(R.id.filterHarvestTv);
        final TextView filterHealthCardTv = view.findViewById(R.id.filterHealthCardTv);
        final TextView filterInputTv = view.findViewById(R.id.filterInputTv);
        final TextView filterResourceTv = view.findViewById(R.id.filterResourceTv);
        final TextView farmDetailsTv = view.findViewById(R.id.filterFarmDetails);
        final TextView filterSatelite = view.findViewById(R.id.filterSatelite);
        final TextView filterCropLoss = view.findViewById(R.id.filterCropLoss);
        final View viewLineSatelite = view.findViewById(R.id.viewLineSatelite);
        final View viewLineHealthCard = view.findViewById(R.id.viewLineHealthCard);


        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            viewLineHealthCard.setVisibility(View.GONE);
            filterHealthCardTv.setVisibility(View.GONE);
            filterSatelite.setVisibility(View.GONE);
            viewLineSatelite.setVisibility(View.GONE);
        }

        filterActivityTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FilterAsync async = new FilterAsync(AppConstants.TIMELINE.ACT);
                async.execute();
            }
        });

        filterCropLoss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FilterAsync async = new FilterAsync(AppConstants.TIMELINE.PLD);
                async.execute();
            }
        });


        filterSatelite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FilterAsync async = new FilterAsync(AppConstants.TIMELINE.SAT);
                async.execute();
            }
        });

        filterVisitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FilterAsync async = new FilterAsync(AppConstants.TIMELINE.VR);
                async.execute();
            }
        });
        filterGerminationTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FilterAsync async = new FilterAsync(AppConstants.TIMELINE.GE);
                async.execute();
            }
        });
        filterHarvestTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FilterAsync async = new FilterAsync(AppConstants.TIMELINE.HR);
                async.execute();
            }
        });
        filterInputTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FilterAsync async = new FilterAsync(AppConstants.TIMELINE.IC);
                async.execute();
            }
        });
        filterHealthCardTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FilterAsync async = new FilterAsync(AppConstants.TIMELINE.HC);
                async.execute();
            }
        });
        filterResourceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FilterAsync async = new FilterAsync(AppConstants.TIMELINE.RU);
                async.execute();
            }
        });

        farmDetailsTv.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (!drawerLayout.isDrawerOpen(GravityCompat.END))
                    drawerLayout.openDrawer(Gravity.RIGHT);
                else drawerLayout.closeDrawer(Gravity.END);
            }
        });

    }

    @Override
    public void onHarvestLoaded(HarvestT harvest) {
        if (harvest != null) {
            Gson gson = new Gson();
            List<HarvestDetailMaster> harvestList;
            List<List<HarvestDetailInnerData>> harvestBagList;
            ViewHarvestDetails harvestDetails = gson.fromJson(harvest.getHarvestJson(), ViewHarvestDetails.class);
            ViewHarvestDetails offlineDetails = gson.fromJson(harvest.getOfflineHarvest(), ViewHarvestDetails.class);

            if (harvestDetails != null) {
                if (harvestDetails.getData1() != null && harvestDetails.getData1().size() > 0) {
                    harvestList = harvestDetails.getData1();
                    harvestBagList = harvestDetails.getData();
                    Log.e(TAG, "Harvest Data--> If " + harvest.getHarvestJson());

                    for (int i = 0; i < harvestDetails.getData1().size(); i++) {
                        TimelineHarvest timelineHarvest = new TimelineHarvest();
                        timelineHarvest.setHarvestDetailInnerDataList(harvestDetails.getData().get(i));
                        timelineHarvest.setHarvestDetailMaster(harvestDetails.getData1().get(i));
                        timelineHarvest.setDoa(timelineHarvest.getHarvestDetailMaster().getDoa());
                        timelineHarvest.setType(AppConstants.TIMELINE.HR);
                        objectList.add(timelineHarvest);
                        objectTypeList.add(new ObjectType(timelineHarvest.getHarvestDetailMaster().getDoa(), AppConstants.TIMELINE.HR));
                    }
                    if (offlineDetails != null && offlineDetails.getData1() != null && offlineDetails.getData1().size() > 0) {
                        harvestList.addAll(offlineDetails.getData1());
                        harvestBagList.addAll(offlineDetails.getData());

                        for (int i = 0; i < offlineDetails.getData1().size(); i++) {
                            TimelineHarvest timelineHarvest = new TimelineHarvest();
                            timelineHarvest.setHarvestDetailInnerDataList(offlineDetails.getData().get(i));
                            timelineHarvest.setHarvestDetailMaster(offlineDetails.getData1().get(i));
                            timelineHarvest.setDoa(timelineHarvest.getHarvestDetailMaster().getDoa());
                            timelineHarvest.setType(AppConstants.TIMELINE.HR);
                            objectList.add(timelineHarvest);
                            objectTypeList.add(new ObjectType(timelineHarvest.getHarvestDetailMaster().getDoa(), AppConstants.TIMELINE.HR));
                        }

                    }

                    int lastBagNo = 0;
                    for (int i = 0; i < harvestBagList.size(); i++) {
                        for (int j = 0; j < harvestBagList.get(i).size(); j++) {
                            lastBagNo = Integer.parseInt(harvestBagList.get(i).get(j).getBagNo());
                        }
                    }
                    SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.LAST_BAG_NO, lastBagNo + 1);

                } else if (offlineDetails.getData() != null && offlineDetails.getData().size() > 0) {
                    harvestList = offlineDetails.getData1();
                    harvestBagList = offlineDetails.getData();
                    for (int i = 0; i < offlineDetails.getData1().size(); i++) {
                        TimelineHarvest timelineHarvest = new TimelineHarvest();
                        timelineHarvest.setHarvestDetailInnerDataList(offlineDetails.getData().get(i));
                        timelineHarvest.setHarvestDetailMaster(offlineDetails.getData1().get(i));
                        timelineHarvest.setDoa(timelineHarvest.getHarvestDetailMaster().getDoa());
                        timelineHarvest.setType(AppConstants.TIMELINE.HR);
                        objectList.add(timelineHarvest);
                        objectTypeList.add(new ObjectType(timelineHarvest.getHarvestDetailMaster().getDoa(), AppConstants.TIMELINE.HR));
                    }
                    Log.e(TAG, "Harvest Data--> Else If " + harvest.getOfflineHarvest());
                    Log.e(TAG, "Harvest DataMaster--> Else If " + new Gson().toJson(offlineDetails.getData1()));
                    Log.e(TAG, "Harvest DataChild--> Else If " + new Gson().toJson(offlineDetails.getData()));
                    int lastBagNo = 0;
                    for (int i = 0; i < harvestBagList.size(); i++) {
                        for (int j = 0; j < harvestBagList.get(i).size(); j++) {
                            lastBagNo = Integer.parseInt(harvestBagList.get(i).get(j).getBagNo());
                        }
                    }
                    SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.LAST_BAG_NO, lastBagNo + 1);

                } else {
                    Log.e(TAG, "Harvest Data--> Else " + harvest.getHarvestJson());
                    SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.LAST_BAG_NO, 1);


                }
            } else {
                Toast.makeText(context, "Harvest Data--> null", Toast.LENGTH_SHORT).show();
            }
            setRecyclerData();
        } else {
            ViewHarvestDetails harvestDetails = new ViewHarvestDetails();
            HarvestT harvestT = new HarvestT(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID),
                    new Gson().toJson(harvestDetails), "{}", "Y");
            HarvestCacheManager.getInstance(context).addVisits(new HarvestCacheManager.HarvestInsertListener() {
                @Override
                public void onHarvestInserted() {
                    HarvestCacheManager.getInstance(context).getHarvest(TestActivity.this::onHarvestLoaded, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));

                }
            }, harvestT);

        }
    }

    @Override
    public void onInputCostLoaded(InputCostT inputCostT) {

        Log.e(TAG, "In onInputCostLoaded " + new Gson().toJson(inputCostT));
        if (inputCostT != null) {
            try {
                JSONArray array = new JSONArray(inputCostT.getInputCostData());
                JSONArray arrayOffline = new JSONArray(inputCostT.getInputCostJsonOfflineAdded());
                List<InputCostData> inputCostDataList = new ArrayList<>();

                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    InputCostData data = new Gson().fromJson(object.toString(), InputCostData.class);
                    data.setTypeC(AppConstants.TIMELINE.IC);
                    data.setDoa(data.getAddedOn());
                    if (data.getAddedOn() != null && !TextUtils.isEmpty(data.getAddedOn())) {
                        objectList.add(data);
                        objectTypeList.add(new ObjectType(data.getAddedOn(), AppConstants.TIMELINE.IC));
                    } else
                        Log.e(TAG, "input no doa at " + i);
                    inputCostDataList.add(data);
                }
                for (int i = 0; i < arrayOffline.length(); i++) {
                    JSONObject object = arrayOffline.getJSONObject(i);
                    InputCostData data = new Gson().fromJson(object.toString(), InputCostData.class);
                    data.setTypeC(AppConstants.TIMELINE.IC);
                    data.setDoa(data.getAddedOn());
                    if (data.getAddedOn() != null && !TextUtils.isEmpty(data.getAddedOn())) {
                        objectList.add(data);
                        objectTypeList.add(new ObjectType(data.getAddedOn(), AppConstants.TIMELINE.IC));
                    } else
                        Log.e(TAG, "input no doa at " + i);
                    inputCostDataList.add(data);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            HarvestCacheManager.getInstance(context).getHarvest(this, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));

        } else {

            String inputData = "[]";
            String inputDataOffline = "[]";
            String farmId;

            farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);

            InputCostT costT = new InputCostT(farmId, inputData, inputDataOffline, "Y");
            InputCostCacheManager.getInstance(context).addInputCost(new InputCostCacheManager.InputCostAddListener() {
                @Override
                public void onCostAdded() {
                    InputCostCacheManager.getInstance(context).getInputCost(TestActivity.this, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));

                }
            }, costT);


        }

    }

    @Override
    public void onInputCostUpdated() {

    }

    @Override
    public void onResourceLoaded(ResourceCostT resourceCost) {
        Log.e(TAG, "In onResourceLoaded " + new Gson().toJson(resourceCost));
        if (resourceCost != null) {
            try {
                JSONArray array = new JSONArray(resourceCost.getResourceJson());
                JSONArray arrayOffline = new JSONArray(resourceCost.getResourceJsonOffline());
                List<ResourceData> resourceDataList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    ResourceData data = new Gson().fromJson(object.toString(), ResourceData.class);
                    data.setTypeC(AppConstants.TIMELINE.RU);
                    data.setDoa(data.getAddedOn());
                    if (data.getAddedOn() != null && !TextUtils.isEmpty(data.getAddedOn())) {
                        objectList.add(data);
                        objectTypeList.add(new ObjectType(data.getAddedOn(), AppConstants.TIMELINE.RU));

                    } else
                        Log.e(TAG, "resource no doa at " + i);
                    resourceDataList.add(data);
                }
                for (int i = 0; i < arrayOffline.length(); i++) {
                    JSONObject object = arrayOffline.getJSONObject(i);
                    ResourceData data = new Gson().fromJson(object.toString(), ResourceData.class);
                    data.setTypeC(AppConstants.TIMELINE.RU);
                    data.setDoa(data.getAddedOn());
                    if (data.getAddedOn() != null && !TextUtils.isEmpty(data.getAddedOn())) {
                        objectList.add(data);
                        objectTypeList.add(new ObjectType(data.getAddedOn(), AppConstants.TIMELINE.RU));
                    } else
                        Log.e(TAG, "resource no doa at " + i);
                    resourceDataList.add(data);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            InputCostCacheManager.getInstance(context).getInputCost(this, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));

        } else {

            String resourceData = "[]";
            String resourceDataOffline = "[]";
            String farmId;
            farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
            ResourceCostT resourceCostT = new ResourceCostT(farmId, resourceData, resourceDataOffline, "Y");

            ResourceCacheManager.getInstance(context).addResource(new ResourceCacheManager.ResourceAddListener() {
                @Override
                public void onResourceAdded() {
                    ResourceCacheManager.getInstance(context).getResource(TestActivity.this, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                }
            }, resourceCostT);
        }

    }

    @Override
    public void onResourceUpdated() {

    }


    private class FilterAsync extends AsyncTask<String, Integer, List<String>> {

        String sortBy;
        ProgressDialog dialog;

        public FilterAsync(String sortBy) {
            this.sortBy = sortBy;
            dialog = new ProgressDialog(context);
            dialog.setMessage(getResources().getString(R.string.please_wait_msg));
        }

        @Override
        protected List<String> doInBackground(String... strings) {

            List<ObjectType> typeList = new ArrayList<>();
            ArrayList<Object> finalList = new ArrayList<>();

            if (sortBy == null || sortBy.trim().toLowerCase().equals("none")) {
                finalList.clear();
                typeList.clear();
                finalList.addAll(objectList);
                typeList.addAll(objectTypeList);
            } else {
                finalList.clear();
                typeList.clear();
                for (int i = 0; i < objectTypeList.size(); i++) {

                    if (objectTypeList.get(i).getType().equals(sortBy)) {
                        typeList.add(objectTypeList.get(i));
                        finalList.add(objectList.get(i));
                    }

                }

            }
            if (finalList.size() > 0) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setVisibility(View.VISIBLE);
                        no_data_available_calendar.setVisibility(View.GONE);
                    }
                });
                List<TimelineUnits> timelineUnits = DataHandler.newInstance().getTimelineUnits();
                adapterTest = new TimelineAdapterTest(context, context, finalList, typeList, timelineUnits, isCurrentLocation, new TimelineAdapterTest.FarmDetailsClickListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onFarmDetailsClicked() {
                        if (!drawerLayout.isDrawerOpen(GravityCompat.END))
                            drawerLayout.openDrawer(Gravity.RIGHT);
                        else drawerLayout.closeDrawer(Gravity.END);
                    }

                    @Override
                    public void onMapIconClicked() {
//                        GetGeoJsonData fetchGpsCordinates = new GetGeoJsonData(true, AppConstants.MAP_NAV_MODE.SHOW_AREA);
//                        fetchGpsCordinates.execute();
                    }
                });
//                adapterTest.setOnSoilCardClick(true);
                adapterTest.setOnSoilCardClick(true);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapterTest);

                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setVisibility(View.GONE);
                        no_data_available_calendar.setVisibility(View.VISIBLE);
                    }
                });

            }

            return null;
        }


        @Override
        protected void onPreExecute() {
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<String> s) {

            Log.e(TAG, "onPostExc " + new Gson().toJson(s));


            dialog.cancel();
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    public void onActivityCountLoaded(int activityCo) {
        activityCount = activityCo;
    }

    @Override
    public void onCountLoaded(int visitCo) {
        visitCount = visitCo;
    }

    class SetNotificationAsRead extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            String ids = strings[0];
            apiInterface.setNotificationAsRead(ids, userId, token).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e(TAG, "Status notif " + response.code());
                    if (response.isSuccessful()) {
                        try {
                            Log.e(TAG, "Set As Read Res " + response.body().string().toString());
                            JSONObject object = new JSONObject(response.body().string());

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            Log.e(TAG, "Set As Read Err " + response.errorBody().string().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "Set As Read Failure " + t.toString());
                }
            });

            return null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityRunning = false;
        if (viewFailDialog != null)
            viewFailDialog.cancelDialog();
        hideDialogForSowingDate();
        if (fusedLocationProviderClient != null && mLocationCallback != null)
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tour_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getAppTour();
        return super.onOptionsItemSelected(item);
    }

    private void getAppTour() {

        Log.e(TAG, "app tour start");

        ShowcaseConfig config = new ShowcaseConfig(); //create the showcase config
        config.setDelay(0); //set the delay of each sequence using millis variable

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);

        sequence.setConfig(config);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setSkipText(getResources().getString(R.string.tour_skip)).setGravity(20)
                        .setTarget(findViewById(R.id.timelineViewChange))
                        .setTitleText("Calendar view")
                        .setContentText("By tapping here user can see all the activities done on the day of month in calendar view")
                        .setDismissText(getResources().getString(R.string.got_it))
                        .build());


        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setSkipText(getResources().getString(R.string.tour_skip)).setGravity(20)
                        .setTarget(findViewById(R.id.timelineFilter))
                        .setTitleText(getResources().getString(R.string.testing_tour_filter_title))
                        .setContentText(getResources().getString(R.string.testing_tour_filter_content))
                        .setDismissText(getResources().getString(R.string.got_it))
                        .build());


        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setSkipText(getResources().getString(R.string.tour_skip)).setGravity(20)
                        .setTarget(findViewById(R.id.app_tour_floating_btn))
                        .setTitleText(getResources().getString(R.string.testing_tour_more_option_title))
                        .setContentText(getResources().getString(R.string.testing_tour_more_option_content))
                        .setDismissText(getResources().getString(R.string.got_it))
                        .build()).setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
            @Override
            public void onDismiss(MaterialShowcaseView itemView, int position) {
                fabParent.open(true);
                if (fabParent.isOpened()) {
                    FabMenuTour();
                }
            }

        });

        sequence.start();

    }

    private void FabMenuTour() {

        ShowcaseConfig config = new ShowcaseConfig(); //create the showcase config
        config.setDelay(0); //set the delay of each sequence using millis variable

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);

        sequence.setConfig(config);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setSkipText(getResources().getString(R.string.tour_skip)).setGravity(30)
                        .setTarget(findViewById(R.id.fabChildViewInputCost))
                        .setTitleText(getResources().getString(R.string.testing_tour_cost_title))
                        .setContentText(getResources().getString(R.string.testing_tour_cost_content))
                        .setDismissText(getResources().getString(R.string.got_it))
                        .build());

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setSkipText(getResources().getString(R.string.tour_skip)).setGravity(30)
                        .setTarget(findViewById(R.id.fabChildAddPld))
                        .setTitleText(getResources().getString(R.string.testing_tour_damage_title))
                        .setContentText(getResources().getString(R.string.testing_tour_damage_content))
                        .setDismissText(getResources().getString(R.string.got_it))
                        .build());

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setSkipText(getResources().getString(R.string.tour_skip)).setGravity(30)
                        .setTarget(findViewById(R.id.fabChildSoilHealthCard))
                        .setTitleText(getResources().getString(R.string.testing_tour_soil_health_title))
                        .setContentText(getResources().getString(R.string.testing_tour_soil_health_content))
                        .setDismissText(getResources().getString(R.string.got_it))
                        .build());

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setSkipText(getResources().getString(R.string.tour_skip)).setGravity(30)
                        .setTarget(findViewById(R.id.fabChildAddGermination))
                        .setTitleText(getResources().getString(R.string.testing_tour_plant_popl_title))
                        .setContentText(getResources().getString(R.string.testing_tour_plant_popl_content))
                        .setDismissText(getResources().getString(R.string.got_it))
                        .build());

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setSkipText(getResources().getString(R.string.tour_skip)).setGravity(30)
                        .setTarget(findViewById(R.id.fabChildSeedIssue))
                        .setTitleText(getResources().getString(R.string.testing_tour_seed_issue_title))
                        .setContentText(getResources().getString(R.string.testing_tour_seed_issue_content))
                        .setDismissText(getResources().getString(R.string.got_it))
                        .build());

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setSkipText(getResources().getString(R.string.tour_skip)).setGravity(30)
                        .setTarget(findViewById(R.id.fabChildViewHarvest))
                        .setTitleText(getResources().getString(R.string.testing_tour_harvesting_title))
                        .setContentText(getResources().getString(R.string.testing_tour_harvesting_content))
                        .setDismissText(getResources().getString(R.string.got_it))
                        .build());

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setSkipText(getResources().getString(R.string.tour_skip)).setGravity(30)
                        .setTarget(findViewById(R.id.fabChildAddVisit))
                        .setTitleText(getResources().getString(R.string.testing_tour_farm_visit_title))
                        .setContentText(getResources().getString(R.string.testing_tour_farm_visit_content))
                        .setDismissText(getResources().getString(R.string.got_it))
                        .build());

        sequence.start();

    }

    private void loadCompReegData() {
        compRegCacheManager.getCompRegData(this);
    }


    private void onclick() {
        areaFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                    GetGeoJsonData fetchGpsCordinates = new GetGeoJsonData(true, AppConstants.MAP_NAV_MODE.SHOW_AREA);
                    fetchGpsCordinates.execute();
                } else {
                    if (farmLatitude == null || farmLongitude == null) {
                        Intent intent = new Intent(context, OfflineMapActivity.class);
                        ActivityOptions options = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            startActivity(intent, options.toBundle());
                        } else {
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(context, OfflineMapActivity.class);
                        ActivityOptions options = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            startActivity(intent, options.toBundle());
                        } else {
                            startActivity(intent);
                        }
//                        viewFailDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.cant_view_farm_in_offline_msg));
                        Toasty.info(context, "Farm coordinates taken, You can see area in online mode", Toast.LENGTH_LONG).show();
                    }
                    //Toast.makeText(context, "Yo can't view farm in offline mode. ", Toast.LENGTH_LONG).show();
                }
            }
        });


        update_farm_details_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
//                    Toasty.error(context, "Currently farm details can not be updated in offline mode", Toast.LENGTH_LONG, false).show();
//                } else {
                if (noOfDays > 3) {
                    Snackbar snackbar = Snackbar.make(content, context.getResources().getString(R.string.no_of_days_offline_exceeded), Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction(resources.getString(R.string.ok_msg), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                } else {
                    Intent intent = new Intent(context, FarmDetailsUpdateActivity2.class);
                    startActivityForResult(intent, UPDATE_FARM_REQUEST_CODE);
                }
            }
            //}
        });

    }


    @Override
    public void onGetAllCompRegData(List<CompRegModel> compRegModelList) {

    }

    @Override
    public void onCompRegDataAdded() {

    }

    @Override
    public void onAllDataDeleted() {

    }

    @Override
    public void onDataNotAdded() {

    }

    @Override
    public void onAllDataNotDeleted() {

    }

    @Override
    public void onUpdateCompRegData() {

    }

    @Override
    public void onUpdateNotSuccess() {

    }


    private void setOfflineCalendarData(List<TimelineInnerData> timelineInnerDataList) {
       /* Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDateStr = df.format(c);
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDate = null;
        Date farmDate = null;
        Date nextFarmDate = null;
        Date lastDate = null;
        try {
            todayDate = df.parse(todayDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Outer 1 Date Exception " + e.toString());
        }
        try {
            lastDate = formatDate.parse(timelineInnerDataList.get(timelineInnerDataList.size() - 1).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Outer 2 Date Exception " + e.toString());
        }
        if (todayDate.after(lastDate)) {
            todayPosition = timelineInnerDataList.size();
            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.VISIT_ALLOWED)) {
                timelineInnerDataList.add(new TimelineInnerData("0", "100",
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                        "0", "0", "N", "reply", "vr"));
            }
            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)) {
                timelineInnerDataList.add(new TimelineInnerData("0", "100",
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                        "0", "0", "I", "reply", "inp"));
            }

            Log.e(TAG, "Adapter add in Last Positon at " + todayPosition);
        } else if (todayDate.equals(lastDate) && timelineInnerDataList.size() == 1) {
            todayPosition = timelineInnerDataList.size();
            if (!isTodayVisitAdded(timelineInnerDataList)) {
                if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.VISIT_ALLOWED)) {
                    timelineInnerDataList.add(new TimelineInnerData("0", "100",
                            SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                            "0", "0", "N", "reply", "vr"));
                }
            }
            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)) {
                timelineInnerDataList.add(new TimelineInnerData("0", "100",
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                        "0", "0", "I", "reply", "inp"));
            }

            Log.e(TAG, "Adapter add in Last Positon at " + todayPosition);
        } else {
            for (int i = 0; i < timelineInnerDataList.size(); i++) {

                if (timelineInnerDataList.get(i).getImg_link() != null &&
                        !timelineInnerDataList.get(i).getImg_link().equals("0")) {
                    Log.e(TAG, "MyProblem img " + i + " " + timelineInnerDataList.get(i).getImg_link());
                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_LATEST_IMG, timelineInnerDataList.get(i).getImg_link());
                }
                try {
                    farmDate = formatDate.parse(timelineInnerDataList.get(i).getDate());
                    if (timelineInnerDataList.size() - 1 > i) {
                        nextFarmDate = formatDate.parse(timelineInnerDataList.get(i + 1).getDate());
                        Log.e(TAG, "Next Date " + timelineInnerDataList.get(i + 1).getDate() + " At Position " + i + 1);
                    }


                    if (todayDate.before(farmDate) && i == 0) {
                        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.VISIT_ALLOWED)) {
                            timelineInnerDataList.add(0, new TimelineInnerData("0", "100",
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                    "0", "0", "N", "reply", "vr"));
                        }
                        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)) {
                            timelineInnerDataList.add(1, new TimelineInnerData("0", "100",
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                    "0", "0", "I", "reply", "inp"));
                        }

                        Log.e(TAG, "Adapter add in frst at " + i);
                        todayPosition = i;
                        break;
                    } else if (todayDate.equals(farmDate) && timelineInnerDataList.get(i).getVisitReportId() != null
                            && !timelineInnerDataList.get(i).getVisitReportId().equals("0") && !TextUtils.isEmpty(timelineInnerDataList.get(i).getVisitReportId())) {
                        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)) {
                            timelineInnerDataList.add(i + 1, new TimelineInnerData("0", "100",
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                    "0", "0", "I", "reply", "inp"));
                        }

                        Log.e(TAG, "Adapter add in new at " + i + " Whene today visit is submitted");
                        todayPosition = i + 1;
                        break;
                    } else if (todayDate.equals(farmDate) && timelineInnerDataList.get(i).getFarmCalActivityId() != null && !timelineInnerDataList.get(i).getFarmCalActivityId().trim().equals("0")) {
                        if (isTodayVisitAdded(timelineInnerDataList)) {
                            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)) {
                                timelineInnerDataList.add(i + 1, new TimelineInnerData("0", "100",
                                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                        "0", "0", "I", "reply", "inp"));
                            }
                            Log.e(TAG, "Adapter add in second at  " + i + " Today visit Added");
                            todayPosition = i;
                        } else {
                            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.VISIT_ALLOWED)) {
                                timelineInnerDataList.add(i + 1, new TimelineInnerData("0", "100",
                                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                        "0", "0", "N", "reply", "vr"));
                            }
                            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)) {
                                timelineInnerDataList.add(i + 2, new TimelineInnerData("0", "100",
                                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                        "0", "0", "I", "reply", "inp"));
                            }

                            Log.e(TAG, "Adapter add in second at  " + i + " Today visit Not Added");
                            todayPosition = i;
                        }
                        break;
                    } else if (farmDate.before(todayDate) && nextFarmDate != null && todayDate.before(nextFarmDate)) {
                        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.VISIT_ALLOWED)) {
                            timelineInnerDataList.add(i + 1, new TimelineInnerData("0", "100",
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                    "0", "0", "N", "reply", "vr"));
                        }
                        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)) {
                            timelineInnerDataList.add(i + 2, new TimelineInnerData("0", "100",
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                    "0", "0", "I", "reply", "inp"));
                        }

                        //Toast.makeText(context, "New object added", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Adapter add in third at " + i);
                        todayPosition = i;
                        break;
                    } else if (i == timelineInnerDataList.size() - 1 && todayDate.after(farmDate)) {
                        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.VISIT_ALLOWED)) {
                            timelineInnerDataList.add(new TimelineInnerData("0", "100",
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                    "0", "0", "N", "reply", "vr"));
                        }
                        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)) {
                            timelineInnerDataList.add(new TimelineInnerData("0", "100",
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                    "0", "0", "I", "reply", "inp"));
                        }

                        todayPosition = timelineInnerDataList.size() - 1;
                        Log.e(TAG, "Adapter add in fourth at " + i);
                        //Toast.makeText(context, "New object added", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        if (nextFarmDate != null)
                            Log.e(TAG, "Next Farm Date " + nextFarmDate.getDate() + " at position " + i);
                        Log.e(TAG, "Adapter add Data added for today " + todayDateStr + " at position " + i);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Inner Date Exception " + e.toString());
                }
            }
        }
        timelineInnerData.addAll(timelineInnerDataList);

        Material material = DataHandler.newInstance().getAllMaterials();
        List<TimelineUnits> timelineUnits = DataHandler.newInstance().getTimelineUnits();
        int count = visitCount + activityCount;
        TimelineAdapterOffline timelineAdapter = new TimelineAdapterOffline(context, activity, timelineInnerData, timelineUnits, material, weatherForecast, count, content);
        timeline_recycler_view.setHasFixedSize(true);
        timeline_recycler_view.setAdapter(timelineAdapter);
        timelineAdapter.notifyDataSetChanged();
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        timeline_recycler_view.setLayoutManager(layoutManager);
        Log.e(TAG, "Today pos " + todayPosition);
        timeline_recycler_view.getLayoutManager().scrollToPosition(todayPosition);
        if (timelineInnerData != null && timelineInnerData.size() == 0) {
            no_data_available_calendar.setVisibility(View.VISIBLE);
            timeline_recycler_view.setVisibility(View.GONE);
        }
*/
    }

    @Override
    public void onTimelineLoaded(Timeline timeline) {
        Log.e(TAG, "Offline fetch timeline " + new Gson().toJson(timeline));
        if (timeline != null) {
            Gson gson = new Gson();
            CalendarJSON calendarJSON = gson.fromJson(timeline.getJsondata(), CalendarJSON.class);
            if (calendarJSON != null && calendarJSON.getCalendarData() != null && calendarJSON.getCalendarData().size() > 0) {
                Log.e(TAG, "Offline Cal if size > 0");
                //setOfflineCalendarData(calendarJSON.getCalendarData());
                for (int position = 0; position < calendarJSON.getCalendarData().size(); position++) {
                    if (calendarJSON.getCalendarData().get(position).getFarmCalActivityId() != null && !calendarJSON.getCalendarData().get(position).getFarmCalActivityId().equals("0")) {
                        TimelineInnerData innerData = calendarJSON.getCalendarData().get(position);
                        innerData.setType(AppConstants.TIMELINE.ACT);
                        innerData.setDoa(innerData.getDate());
                        objectList.add(innerData);
                        objectTypeList.add(new ObjectType(calendarJSON.getCalendarData().get(position).getDate(), AppConstants.TIMELINE.ACT));
                    } else if (calendarJSON.getCalendarData().get(position).getVisitReportId() != null && !calendarJSON.getCalendarData().get(position).getVisitReportId().equals("0")) {
                        TimelineInnerData innerData = calendarJSON.getCalendarData().get(position);
                        innerData.setType(AppConstants.TIMELINE.VR);
                        innerData.setDoa(innerData.getDate());
                        if (innerData.getResultType() != null && innerData.getResultType().trim().equals("dr")) {
                            objectList.add(innerData);
                            innerData.setType(AppConstants.TIMELINE.DELINQUENT);
                            objectTypeList.add(new ObjectType(innerData.getDate(), AppConstants.TIMELINE.DELINQUENT));
                        } else {
                            objectList.add(innerData);
                            innerData.setType(AppConstants.TIMELINE.VR);
                            objectTypeList.add(new ObjectType(innerData.getDate(), AppConstants.TIMELINE.VR));
                        }
                    }
                }

            } else {

            }
            ResourceCacheManager.getInstance(context).getResource(this, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));

        } else {
            Timeline line = new Timeline(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), null, null);
            TimelineCacheManager.getInstance(context).addTimeline(TestActivity.this, line);
        }
    }

    @Override
    public void onTimelineAdded() {
//        Toast.makeText(context, "Added", Toast.LENGTH_LONG).show();
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
    public void onFarmLoader(Farm farmData) {

        Log.e(TAG, "Offline fetch farm " + new Gson().toJson(farmData));
        this.farm = farmData;
        objectTypeList = new ArrayList<>();
        objectList = new ArrayList<>();
        objectList.clear();
        objectTypeList.clear();
        isaFarmLoaded = true;

        fetchFarmResult = new FetchFarmResult("", farmData.getFarmId(), farmData.getLotNo(), farmData.getFarmerId(),
                farmData.getClusterId(), farmData.getCompId(), farmData.getAddL1(), farmData.getAddL2(), farmData.getVillageOrCity(),
                farmData.getDistrict(), farmData.getMandalOrTehsil(), farmData.getState(), farmData.getCountry(), farmData.getExpArea(),
                farmData.getActualArea(), farmData.getIrrigationSource(), farmData.getPreviousCrop(), farmData.getIrrigationType(),
                farmData.getSoilType(), farmData.getSowingDate(), farmData.getExpFloweringDate(), farmData.getActualFloweringDate(),
                farmData.getExpHarvestDate(), farmData.getActualHarvestDate(), "", "", "", "", farmData.getGermination(), farmData.getPopulation(),
                farmData.getSpacingRtr(), farmData.getSpacingPtp(), "2010-09-09", "Y", farmData.getFarmerName(), farmData.getFatherName(),
                farmData.getPldAcres(), farmData.getPldReason(), farmData.getStandingAcres(), false, "", farmData.getFarmerMob(),
                farmData.getAddL1(), farmData.getAddL2(), farmData.getVillageOrCity(), farmData.getDistrict(), farmData.getMandalOrTehsil(),
                farmData.getState(), farmData.getCountry(), farmData.getLatCord(), farmData.getLongCord(), "");

        fetchFarmResult.setCropName(farm.getCropName());


        if (farm.getOfflinePldJson() != null && !TextUtils.isEmpty(farm.getOfflinePldJson()) && !farm.getOfflinePldJson().equals("[]")) {
            try {
                JSONArray array = new JSONArray(farm.getOfflinePldJson());
                for (int j = 0; j < array.length(); j++) {
                    JSONObject object = array.getJSONObject(j);
                    PldData dataDD = new Gson().fromJson(object.toString(), PldData.class);
                    dataDD.setType(AppConstants.TIMELINE.PLD);
                    objectList.add(dataDD);
                    objectTypeList.add(new ObjectType(dataDD.getDoa(), AppConstants.TIMELINE.PLD));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (farm.getOnlinePldJson() != null && !TextUtils.isEmpty(farm.getOnlinePldJson()) && !farm.getOnlinePldJson().equals("[]")) {
            try {
                JSONArray array = new JSONArray(farm.getOnlinePldJson());
                for (int j = 0; j < array.length(); j++) {
                    JSONObject object = array.getJSONObject(j);
                    PldData dataDD = new Gson().fromJson(object.toString(), PldData.class);
                    dataDD.setType(AppConstants.TIMELINE.PLD);
                    objectList.add(dataDD);
                    objectTypeList.add(new ObjectType(dataDD.getDoa(), AppConstants.TIMELINE.PLD));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (farm.getGerminationData() != null && !farm.getGerminationData().equals("[]") && !TextUtils.isEmpty(farm.getGerminationData())) {
            try {
                JSONArray array = new JSONArray(farm.getGerminationData());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    SampleGerminationDatum data = new Gson().fromJson(object.toString(), SampleGerminationDatum.class);
                    if (data.getDoa() != null && !TextUtils.isEmpty(data.getDoa())) {
                        data.setType(AppConstants.TIMELINE.GE);
                        objectList.add(data);
                        objectTypeList.add(new ObjectType(data.getDoa(), AppConstants.TIMELINE.GE));
                    } else {
                        Log.e(TAG, "Germi no doa at " + i);
                    }
                    germinationDataList.add(data);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (farm.getGerminationDataOffline() != null && !TextUtils.isEmpty(farm.getGerminationDataOffline())) {
            try {
                JSONArray array = new JSONArray(farm.getGerminationDataOffline());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    SampleGerminationDatum data = new Gson().fromJson(object.toString(), SampleGerminationDatum.class);
                    if (data.getDoa() != null && !TextUtils.isEmpty(data.getDoa())) {
                        data.setType(AppConstants.TIMELINE.GE);
                        objectList.add(data);
                        objectTypeList.add(new ObjectType(data.getDoa(), AppConstants.TIMELINE.GE));
                    } else {
                        Log.e(TAG, "Germi no doa at " + i);
                    }
                    germinationDataList.add(data);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (farmData != null) {
            String soil_type = "";
            String irrigationSource = "";
            String previousCrop = "";
            String irrigationtype = "";
            String sowingDate = "";
            String expFloweringDate = "";
            String expHarvestDate = "";
            String actual_area = "";
            String standing_area = "";
            String expected_area = "";
            if (farmData.getPldAcres() != null && Double.parseDouble(farmData.getPldAcres()) > 0) {
                fabChildAddPld.setLabelText(resources.getString(R.string.crop_loss_damage_labe));
                fabChildAddPld.setImageResource(android.R.drawable.ic_menu_add);
            } else {
                fabChildAddPld.setLabelText(resources.getString(R.string.crop_loss_damage_labe));
                fabChildAddPld.setImageResource(android.R.drawable.ic_menu_add);
            }

            hideCard(seedDateCard);
            hideCard(seedQtyCard);
            if (farmData.getLotNo() != null) {
                title_name.setText(farmData.getLotNo());
                title_name_d.setText(farmData.getLotNo());
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOT_NO, farmData.getLotNo());
                farmIdTv.setText(farmData.getLotNo());
            } else {

            }
            if (farmData.getFarmerName() != null) {
                title_address.setText(farmData.getFarmerName());
                title_address_d.setText(farmData.getFarmerName());
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARMER_NAME, farmData.getFarmerName());
            }
            if (farmData.getFarmerMob() != null && !farmData.getFarmerMob().equals("9999999999")) {
                mobNo = farmData.getFarmerMob();
            } else {
                mobNo = "";
            }
            if (farmData.getPreviousCrop() != null) {

                farm_details_previousCrop.setText(farmData.getActualFloweringDate());

            } else hideCard(prevCropCard);

            if (farmData.getActualFloweringDate() != null) {
                if (!farmData.getActualFloweringDate().equals(AppConstants.for_date)) {
                    farm_details_actual_flowering_date.setText(AppConstants.getShowableDate(farmData.getActualFloweringDate(), context));
                } else hideCard(actFlowerCard);
            } else hideCard(actFlowerCard);
            if (farmData.getActualHarvestDate() != null) {
                if (!farmData.getActualHarvestDate().equals(AppConstants.for_date)) {
                    farm_details_actual_harvest_date.setText(AppConstants.getShowableDate(farmData.getActualHarvestDate(), context));
                } else
                    hideCard(actHarvestCard);
            } else
                hideCard(actHarvestCard);
            if (fetchFarmResult != null && fetchFarmResult.getCropName() != null) {
                currentCropTv.setText(fetchFarmResult.getCropName());
            } else
                hideCard(currentCropCard);
            if (farmData != null) {
                if (farmData.getSoilType() != null) {
                    soil_type = farmData.getSoilType().toString();
                }
                if (farmData.getActualArea() != null && farmData.getActualArea() != "0") {
                    actual_area = farmData.getActualArea().toString();
                }
                if (farmData.getIrrigationSource() != null) {
                    irrigationSource = farmData.getIrrigationSource().toString();
                }
                if (farmData.getFarmerMob() != null && !farmData.getFarmerMob().equals("9999999999")) {
                    mobNo = farmData.getFarmerMob().toString();
                }
                if (farmData.getPreviousCrop() != null) {
                    previousCrop = farmData.getPreviousCrop().toString();
                }
                if (farmData.getIrrigationType() != null) {
                    irrigationtype = farmData.getIrrigationType().toString();
                }
                if (farmData.getSowingDate() != null && !farmData.getSowingDate().equals(AppConstants.for_date)) {
                    sowingDate = farmData.getSowingDate().toString();
                }
                if (farmData.getExpFloweringDate() != null && !farmData.getExpFloweringDate().equals(AppConstants.for_date)) {
                    expFloweringDate = farmData.getExpFloweringDate().toString();
                }
                if (farmData.getExpHarvestDate() != null && !farmData.getExpHarvestDate().equals(AppConstants.for_date)) {
                    expHarvestDate = farmData.getExpHarvestDate().toString();
                }
                if (farmData.getStandingAcres() != null) {
                    standing_area = farmData.getStandingAcres();
                }
                if (farmData.getExpArea() != null) {
                    expected_area = farmData.getExpArea();
                }
            }
            if (isValidActualArea(actual_area))
                farm_details_actual_area_tv.setText(convertAreaTo(actual_area));
            else hideCard(actAreaCard);
            if (isValidActualArea(standing_area))
                farm_details_standing_area_tv.setText(convertAreaTo(standing_area));
            else hideCard(standigAreaCard);
            if (isValidActualArea(expected_area))
                farm_details_expected_area_tv.setText(convertAreaTo(expected_area));
            else hideCard(expAreaCard);
            if (isValidString(irrigationSource))
                farm_details_irrigationSource.setText(irrigationSource);
            else hideCard(irriSourceCard);
            if (isValidString(irrigationtype))
                farm_details_irrigationType.setText(irrigationtype);
            else hideCard(irriTypeCard);
            if (isValidString(previousCrop) && !previousCrop.equals("0"))
                farm_details_previousCrop.setText(previousCrop);
            else hideCard(prevCropCard);
            if (isValidString(soil_type))
                farm_details_soilType.setText(soil_type);
            else hideCard(soilTypeCard);

            if (isValidString(farmData.getAddL1())) {
                khasraTv.setText(farmData.getAddL1());
            } else hideCard(addlCard);

            if (isValidString(farmData.getAddL2())) {
                chakTv.setText(farmData.getAddL1());
            } else hideCard(addL2Card);

            if (!AppConstants.isValidString(actual_area) || AppConstants.isValidActualArea(actual_area)) {
                view_farm_area_button.setText(resources.getString(R.string.capture_farm_area_label));
                navigateTo = "map";
            } else {
                navigateTo = "view_details";
                view_farm_area_button.setText(resources.getString(R.string.farm_details_view_area));

            }
            if (mobNo != null && !mobNo.equals("0") && !mobNo.equals("9999999999"))
                farm_details_mob_number.setText(mobNo);
            else
                farm_details_mob_number.setText("-");
            farm_details_mob_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mobNo.equals("-") && !mobNo.equals("9999999999")) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + mobNo));
                        startActivity(intent);
                    }
                }
            });
            if (isValidString(sowingDate) && !sowingDate.equals(for_date))
                farm_details_sowingDate.setText(AppConstants.getShowableDate(sowingDate, context));
            else hideCard(sowingDateCard);
            if (isValidString(expFloweringDate) && !expFloweringDate.equals(for_date))
                farm_details_expFloweringDate.setText(AppConstants.getShowableDate(expFloweringDate, context));
            else hideCard(expFlowerCard);
            if (isValidString(expHarvestDate) && !expHarvestDate.equals(for_date))
                add_details_expHarvestDate.setText(AppConstants.getShowableDate(expHarvestDate, context));
            else hideCard(expHarvestCard);

            //Germination
            germinationDataSet(fetchFarmResult);

            if (farmData.getGermination() != null && !
                    !TextUtils.isEmpty(farmData.getGermination())
                    && !farmData.getGermination().equals("[]")) {
                SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.GERMINATION,
                        Float.valueOf(farmData.getGermination().trim()));
            } else {
                SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.GERMINATION,
                        0.0);
            }
            TimelineCacheManager.getInstance(context).getAllTimeline(TestActivity.this, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
            if (farmData.getLatCord() != null && farmData.getLongCord() != null && !TextUtils.isEmpty(farmData.getLatCord())
                    && !TextUtils.isEmpty(farmData.getLongCord()) && !farmData.getLatCord().trim().equals("0") && !farmData.getLongCord().trim().equals("0")) {
                farmLatitude = fetchFarmResult.getLatCord();
                farmLongitude = farmData.getLongCord();
                navigateTo = "view_details";
                view_farm_area_button.setText(resources.getString(R.string.farm_details_view_area));
            } else {
                farmLatitude = null;
                farmLongitude = null;
            }

        }

        FarmerCacheManager.getInstance(context).getFarmer(new FarmerCacheManager.GetFarmerListener() {
            @Override
            public void onFarmerLoaded(FarmerT farmerT) {
                if (farmerT != null && AppConstants.isValidString(farmerT.getData()) && !farmerT.getData().equals("{}")) {
                    try {
                        FarmerAndFarmData farmerAndFarmData = new Gson().fromJson(farmerT.getData().trim(), FarmerAndFarmData.class);

                        if (farmerAndFarmData != null) {

                            if (AppConstants.isValidString(farmerAndFarmData.getName())) {
                                farmerNameTv.setText(farmerAndFarmData.getName());
                            } else {
                                farmerNameTv.setText("-");

                            }
                            if (AppConstants.isValidString(farmerAndFarmData.getMob())) {
                                farm_details_mob_number.setText(farmerAndFarmData.getMob());
                            } else {
//                                farmerNameTv.setText("-");
                                hideCard(mobileCard);
                            }
                            if (AppConstants.isValidString(farmerAndFarmData.getFather_name())) {
                                fatherNameTv.setText(farmerAndFarmData.getFather_name());
                            } else {
//                                farmerNameTv.setText("-");
                                hideCard(fatherCard);
                            }

                            if (isValidString(farmerAndFarmData.getGeder())) {
                                if (farmerAndFarmData.getGeder().equalsIgnoreCase("m")) {
                                    gender.setText(resources.getString(R.string.male_label));
                                } else if (farmerAndFarmData.getGeder().equalsIgnoreCase("f")) {
                                    gender.setText(resources.getString(R.string.female_label));
                                } else if (farmerAndFarmData.getGeder().equalsIgnoreCase("o")) {
                                    gender.setText(resources.getString(R.string.other_labe));
                                } else {
                                    gender.setText("-");
                                    hideCard(genderCard);
                                }
                            } else {
                                gender.setText("-");
                                hideCard(genderCard);
                            }


                            if (isValidString(farmerAndFarmData.getDob()) && !farmerAndFarmData.getDob().equals(for_date)) {
                                dobTv.setText(AppConstants.getShowableDate(farmerAndFarmData.getDob(), context));
                            } else {
//                                dobTv.setText("-");
                                hideCard(dobCard);
                            }


                        }

                    } catch (Exception e) {

                    }

                } else {
//                    Toast.makeText(context, "else farmer ", Toast.LENGTH_SHORT).show();
                }
            }
        }, farm.getFarmerId());
    }

    /*private class FetchGpsCordinates extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;
        boolean isShowEasyMode = false;

        public FetchGpsCordinates(boolean isShowEasyMode) {
            super();
            DataHandler.newInstance().setLatLngList(null);
            this.isShowEasyMode = isShowEasyMode;
            dialog = new ProgressDialog(context);
            dialog.setMessage(getResources().getString(R.string.please_wait_msg));
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            Call<GetGpsCoordinates> getGpsCoordinatesCall = apiService.getGpsCoordinates(
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID), userId, token);
            getGpsCoordinatesCall.enqueue(new Callback<GetGpsCoordinates>() {
                @Override
                public void onResponse(Call<GetGpsCoordinates> call, Response<GetGpsCoordinates> response) {
                    Log.e(TAG, "Status coords " + response.code());
                    if (response.isSuccessful()) {
                        GetGpsCoordinates getGpsCoordinates = response.body();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog != null)
                                    dialog.cancel();
                            }
                        });
                        if (getGpsCoordinates.getStatus() == 10) {
                            viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                        } else if (getGpsCoordinates.getStatus() != 0) {
                            List<LatLngFarm> latLngList = getGpsCoordinates.getData();
                            latAraay = new String[latLngList.size()];
                            lngArray = new String[latLngList.size()];
                            int sizeofgps = latLngList.size();
                            for (int i = 0; i < latLngList.size(); i++) {
                                latAraay[i] = latLngList.get(i).getLat();
                                lngArray[i] = latLngList.get(i).getLng();
                                Log.e(TAG, "ShowFarmAreaCoordinates " + latLngList.get(i).getLat() + " " + latLngList.get(i).getLng());
                            }
                            com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(Double.valueOf(latAraay[0]), Double.valueOf(lngArray[0]));
                            DataHandler.newInstance().setLatLngList(latLngList);
                            if (isShowEasyMode) {
//                                showDialog(true);
                                Intent intent = new Intent(context, ShowAreaWithUpdateActivity.class);
                                intent.putExtra("update", false);
                                ActivityOptions options = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                                }
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    startActivity(intent, options.toBundle());
                                } else {
                                    startActivity(intent);
                                }
                            } else {
                                showDialog(false);
                            }
                        } else if (navigateTo.toLowerCase().trim().equals("map")) {
                            Log.e(TAG, "ShowFarmAreaCoordinates " + new Gson().toJson(response.body()));
                            //  Toast.makeText(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG).show();
                            if (isShowEasyMode) {
                                if (farmLatitude != null && farmLongitude != null) {
                                    isEasyMode = true;
                                    //Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                                    Intent intent = new Intent(context, MapsActivityEasyMode.class);
                                    intent.putExtra("farmLat", farmLatitude);
                                    intent.putExtra("farmLon", farmLongitude);
                                    ActivityOptions options = null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                                    } else {
                                        startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                                    }
                                } else {
//                                bottomSheetDialog.show();
                                    showDialog(true);
                                }
                            } else {
                                showDialog(true);
//                                Toasty.error(context, "Please mark farm area before you can mark omitance area", Toast.LENGTH_LONG, false).show();
                            }
                        } else {
                            Toasty.error(context, resources.getString(R.string.area_entere_manually_msg), Toast.LENGTH_LONG, false).show();
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog != null)
                                    dialog.cancel();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<GetGpsCoordinates> call, Throwable t) {
                    //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, t.toString());
                    if (isActivityRunning) {
                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_get_farm_location_msg));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog != null)
                                dialog.cancel();
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

    }*/

    private class GetGeoJsonData extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;
        boolean isShowEasyMode = false;
        int mode = 1;

        public GetGeoJsonData(boolean isShowEasyMode, int mode) {
            super();
            dialog = new ProgressDialog(context);
            DataHandler.newInstance().setLatLngList(null);
            this.isShowEasyMode = isShowEasyMode;
            this.mode = mode;
            dialog.setMessage(getResources().getString(R.string.please_wait_msg));
            dialog.show();
        }

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
                                    dialog.cancel();
                                    Intent intent = new Intent(context, ShowAreaWithUpdateActivity.class);
                                    if (mode == AppConstants.MAP_NAV_MODE.SHOW_NAVIGATION) {
                                        intent = new Intent(context, FarmNavigationActivity.class);
                                    }
                                    startActivity(intent);
                                } else {
                                    if (mode == AppConstants.MAP_NAV_MODE.SHOW_NAVIGATION) {
                                        Toasty.error(context, "Farm location not available", Toast.LENGTH_LONG, false).show();
                                    } else
                                        showDialog(true);
                                }
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                dialog.cancel();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                dialog.cancel();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                            } else {
                                dialog.cancel();
                                if (mode == AppConstants.MAP_NAV_MODE.SHOW_NAVIGATION) {
                                    Toasty.error(context, "Farm location not available", Toast.LENGTH_LONG, false).show();
                                } else
                                    showDialog(true);
                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            dialog.cancel();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            dialog.cancel();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            try {
                                error = response.errorBody().string().toString();
                                Toasty.error(context, "Farm location not available", Toast.LENGTH_LONG, false).show();
                                Log.e(TAG, "Error " + error);
                                dialog.cancel();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                            notifyApiDelay(TestActivity.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<GeoJsonResponse> call, Throwable t) {
                        Log.e(TAG, "Failure " + t.toString());
                        isLoaded[0] = true;
                        dialog.cancel();
                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        notifyApiDelay(TestActivity.this, call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);
                        Toasty.error(context, "Farm location not available", Toast.LENGTH_LONG, false).show();

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

    /*private class FetchGpsCordinates2 extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        public FetchGpsCordinates2() {
            super();
            DataHandler.newInstance().setLatLngList(null);
            dialog = new ProgressDialog(context);
            dialog.setMessage(getResources().getString(R.string.please_wait_msg));
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            Call<GetGpsCoordinates> getGpsCoordinatesCall = apiService.getGpsCoordinates(
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID), userId, token);
            getGpsCoordinatesCall.enqueue(new Callback<GetGpsCoordinates>() {
                @Override
                public void onResponse(Call<GetGpsCoordinates> call, Response<GetGpsCoordinates> response) {
                    Log.e(TAG, "Status coords " + response.code());
                    if (response.isSuccessful()) {
                        GetGpsCoordinates getGpsCoordinates = response.body();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog != null)
                                    dialog.cancel();
                            }
                        });
                        if (getGpsCoordinates.getStatus() == 10) {
                            viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                        } else if (getGpsCoordinates.getStatus() != 0) {

                            List<LatLngFarm> latLngList = getGpsCoordinates.getData();
                            if (latLngList != null && latLngList.size() > 0) {
                                latAraay = new String[latLngList.size()];
                                lngArray = new String[latLngList.size()];
                                int sizeofgps = latLngList.size();
                                for (int i = 0; i < latLngList.size(); i++) {
                                    latAraay[i] = latLngList.get(i).getLat();
                                    lngArray[i] = latLngList.get(i).getLng();
                                    Log.e(TAG, "ShowFarmAreaCoordinates " + latLngList.get(i).getLat() + " " + latLngList.get(i).getLng());
                                }
                                com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(Double.valueOf(latAraay[0]), Double.valueOf(lngArray[0]));
                                DataHandler.newInstance().setLatLngList(latLngList);
                                Intent intent = new Intent(context, FarmNavigationActivity.class);
                                ActivityOptions options = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                                }
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    startActivity(intent, options.toBundle());
                                } else {
                                    startActivity(intent);
                                }
                            } else {
                                Toasty.error(context, "Farm location not available", Toast.LENGTH_LONG, false).show();
                            }
                        } else {
                            Toasty.error(context, "Farm location not available", Toast.LENGTH_LONG, false).show();
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog != null)
                                    dialog.cancel();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<GetGpsCoordinates> call, Throwable t) {
                    //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, t.toString());
                    if (isActivityRunning) {
//                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_get_farm_location_msg));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog != null)
                                dialog.cancel();
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

    }*/

    private boolean isSeedDataProvided = false;

    private void setFarmDataOnline(final FetchFarmResult farmData) {
        DataHandler.newInstance().setFetchFarmResult(farmData);
        Log.e(TAG, "Farm Data " + new Gson().toJson(farmData));
        if (farmData.getQtySeedProvided() != null) {
            seedQtyTv.setText(farmData.getQtySeedProvided());
            isSeedDataProvided = true;
        } else {
            hideCard(seedQtyCard);
        }
        if (farmData.getSeedProvidedOn() != null && !farmData.getSeedProvidedOn().equals(for_date)) {
            seedProvidedOnTv.setText(AppConstants.getShowableDate(farmData.getSeedProvidedOn(), context));
            isSeedDataProvided = true;
        } else {
            hideCard(seedDateCard);
        }

        if (farmData.getLatCord() != null && farmData.getLongCord() != null && !TextUtils.isEmpty(farmData.getLatCord()) && !TextUtils.isEmpty(farmData.getLongCord())) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARM_LAT, farmData.getLatCord());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARM_LONG, farmData.getLongCord());
        }
        if (isSeedDataProvided) {
            fabChildSeedIssue.setImageResource(android.R.drawable.ic_menu_view);
//            fabChildSeedIssue.setVisibility(View.GONE);
        } else {
            fabChildSeedIssue.setImageResource(android.R.drawable.ic_menu_add);
        }

        fabChildSeedIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabParent.isOpened())
                    fabParent.close(true);
                SeedFullScreenDialog dialog = new SeedFullScreenDialog(farmData, null, isSeedDataProvided);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(getSupportFragmentManager(), SeedFullScreenDialog.TAG);

            }
        });

        fabChildSellRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabParent.isOpened())
                    fabParent.close(true);
                if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE))
                    startActivityForResult(new Intent(context, AddProduceSellActivity.class), WHOLE_DATA_CHANGE_REQUEST_CODE);
            }
        });


        String soil_type = "";
        //String avg_germination = "0";
        String irrigationSource = "";
        String previousCrop = "";
        String irrigationtype = "";
        String sowingDate = "";
        String expFloweringDate = "";
        String expHarvestDate = "";
        String actual_area = "0";
        String standing_area = "0";
        String expected_area = "0";

        if (farmData.getPldAcres() != null && Double.parseDouble(farmData.getPldAcres()) > 0) {
            fabChildAddPld.setLabelText(resources.getString(R.string.crop_loss_damage_labe));
            fabChildAddPld.setImageResource(android.R.drawable.ic_menu_add);
        } else {
            fabChildAddPld.setLabelText(resources.getString(R.string.crop_loss_damage_labe));
            fabChildAddPld.setImageResource(android.R.drawable.ic_menu_add);
        }
        if (isActivityDestroyed) {
            isActivityDestroyed = false;

        }
        if (farmData.getLotNo() != null) {
            ///farm_lot_no.setText(farmData.getLotNo());
            title_name.setText(farmData.getLotNo());
            title_name_d.setText(farmData.getLotNo());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOT_NO, farmData.getLotNo());
        }
        if (farmData.getName() != null) {
            title_address.setText(farmData.getName());
            title_address_d.setText(farmData.getName());
            farmerNameTv.setText(farmData.getName());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARMER_NAME, farmData.getName());
        } else
            farmerNameTv.setText("-");
        if (farmData.getMob() != null && !farmData.getMob().equals("9999999999")) {
            mobNo = farmData.getMob();
        } else {
            mobNo = "-";
            hideCard(mobileCard);
        }


        if (farmData.getActualFloweringDate() != null) {
            if (!farmData.getActualFloweringDate().equals(AppConstants.for_date)) {
                farm_details_actual_flowering_date.setText(AppConstants.getShowableDate(farmData.getActualFloweringDate(), context));
            } else
                hideCard(actFlowerCard);
        } else
            hideCard(actFlowerCard);
        if (farmData.getActualHarvestDate() != null) {
            if (!farmData.getActualHarvestDate().equals(AppConstants.for_date)) {
                farm_details_actual_harvest_date.setText(AppConstants.getShowableDate(farmData.getActualHarvestDate(), context));
            } else
                hideCard(actHarvestCard);
        } else
            hideCard(actHarvestCard);

        if (farmData != null) {
            if (farmData.getSoilType() != null) {
                soil_type = farmData.getSoilType().toString();
            }
            if (farmData.getActualArea() != null && farmData.getActualArea() != "0") {
                actual_area = farmData.getActualArea().toString();
            }

            if (farmData.getIrrigationSource() != null) {
                irrigationSource = farmData.getIrrigationSource().toString();
            }
            if (farmData.getMob() != null && !farmData.getMob().equals("9999999999")) {
                mobNo = farmData.getMob().toString();
            }
            if (farmData.getPreviousCrop() != null) {
                previousCrop = farmData.getPreviousCrop().toString();
            }
            if (farmData.getIrrigationType() != null) {
                irrigationtype = farmData.getIrrigationType().toString();
            }
            if (farmData.getSowingDate() != null && !farmData.getSowingDate().equals(AppConstants.for_date)) {
                sowingDate = AppConstants.getShowableDate(farmData.getSowingDate(), context);
            } else {
                showDialogForSowingDate(resources.getString(R.string.add_sowing_date_msg));
            }
            if (farmData.getExpFloweringDate() != null && !farmData.getExpFloweringDate().equals(AppConstants.for_date)) {
                expFloweringDate = AppConstants.getShowableDate(farmData.getExpFloweringDate(), context);
            }
            if (farmData.getExpHarvestDate() != null && !farmData.getExpHarvestDate().equals(AppConstants.for_date)) {
                expHarvestDate = AppConstants.getShowableDate(farmData.getExpHarvestDate(), context);
            }
            if (farmData.getStandingAcres() != null) {
                standing_area = farmData.getStandingAcres();
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, standing_area);
            }
            if (farmData.getExpArea() != null) {
                expected_area = farmData.getExpArea();
            }
        }

        if (isValidActualArea(actual_area))
            farm_details_actual_area_tv.setText(convertAreaTo(actual_area));
        else hideCard(actAreaCard);
        if (isValidActualArea(standing_area))
            farm_details_standing_area_tv.setText(convertAreaTo(standing_area));
        else hideCard(standigAreaCard);
        if (isValidActualArea(expected_area))
            farm_details_expected_area_tv.setText(convertAreaTo(expected_area));
        else hideCard(expAreaCard);
        if (isValidString(irrigationSource))
            farm_details_irrigationSource.setText(irrigationSource);
        else hideCard(irriSourceCard);
        if (isValidString(irrigationtype))
            farm_details_irrigationType.setText(irrigationtype);
        else hideCard(irriTypeCard);
        if (isValidString(previousCrop) && !previousCrop.equals("0"))
            farm_details_previousCrop.setText(previousCrop);
        else hideCard(prevCropCard);
        if (isValidString(soil_type))
            farm_details_soilType.setText(soil_type);
        else hideCard(soilTypeCard);
        if (mobNo != null && !mobNo.equals("0") && !mobNo.equals("9999999999"))
            farm_details_mob_number.setText(mobNo);
        else {
            farm_details_mob_number.setText("-");
            hideCard(mobileCard);
        }
        if (isValidString(sowingDate) && !sowingDate.equals(for_date))
            farm_details_sowingDate.setText(sowingDate);
        else hideCard(sowingDateCard);
        if (isValidString(expFloweringDate) && !expFloweringDate.equals(for_date))
            farm_details_expFloweringDate.setText(expFloweringDate);
        else hideCard(expFlowerCard);
        if (isValidString(expHarvestDate) && !expHarvestDate.equals(for_date))
            add_details_expHarvestDate.setText(expHarvestDate);
        else hideCard(expHarvestCard);

        boolean mode = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE);
        boolean isDelq = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_DELINQUENT_COMP);
        boolean isVetting = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_VETTING_COMP);
        String vetting = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARM_VETTING);
        if (actual_area.trim().equals("0") || !AppConstants.isValidActualArea(actual_area)) {
            view_farm_area_button.setText(resources.getString(R.string.capture_farm_area_label));
            navigateTo = "map";
            if (!mode && isDelq && isVetting && AppConstants.isValidString(vetting) && !vetting.equals(AppConstants.VETTING.DELINQUENT)) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 2f);
                params.setMargins(5, 0, 5, 0);
                delinquentFarmTv.setLayoutParams(params);
                delinquentFarmTv.setVisibility(View.VISIBLE);
                delinquentFarmTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(context, AddDelinquentActivity.class));
                    }
                });
            } else {
                delinquentFarmTv.setVisibility(View.GONE);
                directionFarmTv.setVisibility(View.GONE);
            }

        } else {
            navigateTo = "view_details";
            view_farm_area_button.setText(resources.getString(R.string.farm_details_view_area));
            if (!mode && isDelq && isVetting && AppConstants.isValidString(vetting) && !vetting.equals(AppConstants.VETTING.DELINQUENT)) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                params.setMargins(5, 0, 5, 0);
                delinquentFarmTv.setLayoutParams(params);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                params2.setMargins(5, 0, 5, 0);
                directionFarmTv.setLayoutParams(params2);
                directionFarmTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GetGeoJsonData fetchGpsCordinates2 = new GetGeoJsonData(false, AppConstants.MAP_NAV_MODE.SHOW_NAVIGATION);
                        fetchGpsCordinates2.execute();
                    }
                });
                directionFarmTv.setVisibility(View.VISIBLE);
//                directionFarmTv.setVisibility(View.GONE);
                delinquentFarmTv.setVisibility(View.VISIBLE);
                delinquentFarmTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(context, AddDelinquentActivity.class));
                    }
                });
            } else {

                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 2f);
                params2.setMargins(5, 0, 5, 0);
                directionFarmTv.setLayoutParams(params2);
//                directionFarmTv.setVisibility(View.GONE);
                directionFarmTv.setVisibility(View.VISIBLE);
//                delinquentFarmTv.setVisibility(View.GONE);
                directionFarmTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GetGeoJsonData fetchGpsCordinates2 = new GetGeoJsonData(false, AppConstants.MAP_NAV_MODE.SHOW_NAVIGATION);
                        fetchGpsCordinates2.execute();
                    }
                });
            }

        }
        farm_details_mob_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mobNo.equals("-") && !mobNo.equals("9999999999")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mobNo));
                    startActivity(intent);
                }
            }
        });
        if (farmData.getLatCord() != null && farmData.getLongCord() != null && !TextUtils.isEmpty(farmData.getLatCord())
                && !TextUtils.isEmpty(farmData.getLongCord()) && !farmData.getLatCord().trim().equals("0") && !farmData.getLongCord().trim().equals("0")) {
            farmLatitude = farmData.getLatCord();
            farmLongitude = farmData.getLongCord();
            if (actual_area == null || TextUtils.isEmpty(actual_area) || !AppConstants.isValidActualArea(actual_area)) {
                view_farm_area_button.setText(resources.getString(R.string.verify_farm_area_label));
            }
        } else {
            farmLatitude = null;
            farmLongitude = null;
        }
    }

    AlertDialog alertDialog;


    private void showDialogForSowingDate(String msg) {
        try {
            if (isActivityRunning) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                alertDialog = builder.setMessage(msg)
                        .setTitle(resources.getString(R.string.sowing_date_not_available_label))
                        .setCancelable(false)
                        .setPositiveButton(resources.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myCalendarAddFarmflowering = Calendar.getInstance();
                                final DatePickerDialog.OnDateSetListener add_details_farm_flowering = new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        myCalendarAddFarmflowering.set(Calendar.YEAR, year);
                                        myCalendarAddFarmflowering.set(Calendar.MONTH, monthOfYear);
                                        myCalendarAddFarmflowering.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                                        new AlertDialog.Builder(context)
                                                .setMessage(resources.getString(R.string.sure_want_to_enter_sowing_date_mg))
                                                .setCancelable(false)
                                                .setPositiveButton(resources.getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        updateFloweringDate();
                                                    }
                                                })
                                                .setNegativeButton(resources.getString(R.string.no), null)
                                                .show();

                                    }
                                };
                                DatePickerDialog datePickerDialog = new DatePickerDialog(TestActivity.this, add_details_farm_flowering, myCalendarAddFarmflowering
                                        .get(Calendar.YEAR), myCalendarAddFarmflowering.get(Calendar.MONTH),
                                        myCalendarAddFarmflowering.get(Calendar.DAY_OF_MONTH));
                                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                                datePickerDialog.show();
                            }
                        })
                        .setNegativeButton(resources.getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideDialogForSowingDate() {
        if (alertDialog != null)
            alertDialog.cancel();
    }


    private void updateFloweringDate() {
        String submit_format = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat submit_sdf = new SimpleDateFormat(submit_format, Locale.US);
        String final_submit_actual_flowering_date = submit_sdf.format(myCalendarAddFarmflowering.getTime());
        Log.e(TAG, "Sowing Date " + final_submit_actual_flowering_date);
        hideDialogForSowingDate();
        auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);

        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        HarvestAndFloweringSendData harvestAndFloweringSendData = new HarvestAndFloweringSendData();
        harvestAndFloweringSendData.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
        harvestAndFloweringSendData.setSowing_date(final_submit_actual_flowering_date);
        harvestAndFloweringSendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        harvestAndFloweringSendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        Log.e(TAG, "Update Sowing Data " + new Gson().toJson(harvestAndFloweringSendData));
        Call<StatusMsgModel> statusMsgModelCall = apiService.getSowingDateStatus(harvestAndFloweringSendData);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        statusMsgModelCall.enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                String error = null;
                isLoaded[0] = true;
                Log.e(TAG, "Status update flow " + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG, "Update Sowing res " + new Gson().toJson(response.body()));
                    StatusMsgModel statusMsgModel = response.body();

                    if (response.body().getStatus() == 10) {

                        viewFailDialog.showSessionExpireDialog(TestActivity.this, response.body().getMsg());
                    } else if (statusMsgModel.getStatus() == 1) {

                        //Toast.makeText(context, resources.getString(R.string.flowering_date_updated_success_msg), Toast.LENGTH_LONG).show();
                        Toasty.success(context, resources.getString(R.string.sowing_date_updated_success_msg), Toast.LENGTH_LONG, true).show();

                        onStart();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isActivityRunning) {

                                    viewFailDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.something_went_wrong_msg));
                                }
                            }
                        });

                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isActivityRunning) {

                                    viewFailDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.something_went_wrong_msg));
                                }
                            }
                        });

                        Log.e("Error", response.errorBody().string().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(TestActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                isLoaded[0] = true;
                long diff = newMillis - currMillis;
                notifyApiDelay(TestActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "Update Sowing Failure " + t.toString());
                if (isActivityRunning) {

                    viewFailDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.something_went_wrong_msg));
                }
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

    private void setLineChartInfo(List<LineChartInfo> lineChartInfos) {
        if (lineChartInfos != null && lineChartInfos.size() > 0) {
            lineChart.animateXY(800, 1200);
            List<Entry> entries = new ArrayList<>();
            entries.clear();
            xAxisLabel.clear();
            xAxisLabel.add(" ");
            float y = 0;
            for (int i = 0; i < lineChartInfos.size(); i++) {
                String date = lineChartInfos.get(i).getVisitDate();
                try {
                    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                    Date d = input.parse(date);
                    SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
                    xAxisLabel.add(output.format(d).substring(0, 5));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (lineChartInfos.get(i).getGrade().equals("A")) {
                    y = 4;
                } else if (lineChartInfos.get(i).getGrade().equals("B")) {
                    y = 3;
                } else if (lineChartInfos.get(i).getGrade().equals("C")) {
                    y = 2;
                } else if (lineChartInfos.get(i).getGrade().equals("D")) {
                    y = 1;
                }
                entries.add(new Entry(i + 1, y));

            }
            xAxisLabel.add(" ");
            Log.e("xAxisLabel", String.valueOf(xAxisLabel.size()));
            LineDataSet set = new LineDataSet(entries, "Visits");
            set.setValueTextSize(0);
            //set.setDrawFilled(true);
            //set.setCircleColor(Color.parseColor("#ffffff"));
            set.setCircleColor(resources.getColor(R.color.new_theme_light_leaf));
            //set.setDrawCircles(false);
            // set.setCircleColor(Color.parseColor("#FFA000"));

            set.setDrawCircleHole(false);

            //set.setColors(ColorTemplate.MATERIAL_COLORS);
            set.setColor(resources.getColor(R.color.new_theme_light_leaf));
            LineData data = new LineData(set);
            //data.setBarWidth(0.7f); // set custom bar width
            lineChart.setData(data);
            Description description = new Description();
            description.setText("");
            //lineChart.setFitsSystemWindows(true);
            lineChart.setDescription(description);
            lineChart.setBackgroundColor(Color.WHITE);
            lineChart.getAxisLeft().setDrawGridLines(false);
            lineChart.getAxisLeft().setGranularityEnabled(true);
            lineChart.getXAxis().setDrawGridLines(false);

            lineChart.getAxisRight().setEnabled(false);
            lineChart.setTouchEnabled(false);
            lineChart.setHorizontalScrollBarEnabled(true);// make the x-axis fit exactly all bars
            lineChart.invalidate(); // refresh
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setAxisMinimum(0);
            xAxis.setAxisMaximum(xAxisLabel.size() - 1);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularityEnabled(true);
            xAxis.setTextColor(Color.parseColor("#FFA000"));
            if (xAxisLabel != null && xAxisLabel.size() > 0) {
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        Log.e("float Value", String.valueOf(value));

                        return xAxisLabel.get((int) value);


                    }
                });
            }
            YAxis leftAxis = lineChart.getAxisLeft();
            // leftAxis.setAxisMaxValue(4f);
            leftAxis.setAxisMinimum(0f);
            //leftAxis.setYOffset(1f);
            leftAxis.setAxisMaximum(4f);
            leftAxis.setTextColor(Color.parseColor("#FFA000"));

            yAxisLabel.clear();
            yAxisLabel.add(" ");

            yAxisLabel.add("GRADE D");
            yAxisLabel.add("GRADE C");
            yAxisLabel.add("GRADE B");
            yAxisLabel.add("GRADE A");
            leftAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return yAxisLabel.get((int) value);
                }
            });
            Legend l = lineChart.getLegend();
            l.setTextColor(Color.parseColor("#8b9800"));
            //lineChart.animateX(600);

        } else {
            lineChart.setVisibility(View.GONE);

            chartLayout.getLayoutParams().height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (farmAndHarvestCall != null) {
            farmAndHarvestCall.cancel();
        }
    }

    private void update_actual_flowering_date() {

        //actual_flowering_date.setText(sdf.format(myCalendarAddFarmflowering.getTime()));

        String submit_format = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat submit_sdf = new SimpleDateFormat(submit_format, Locale.US);
        String final_submit_actual_flowering_date = submit_sdf.format(myCalendarAddFarmflowering.getTime());

        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {

            try {
                auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                HarvestAndFloweringSendData harvestAndFloweringSendData = new HarvestAndFloweringSendData();
                harvestAndFloweringSendData.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                harvestAndFloweringSendData.setActual_flowering_date(final_submit_actual_flowering_date);
                harvestAndFloweringSendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                harvestAndFloweringSendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                Log.e(TAG, "Update Flower Data " + new Gson().toJson(harvestAndFloweringSendData));
                Call<StatusMsgModel> statusMsgModelCall = apiService.getActualFloweringDateStatus(harvestAndFloweringSendData);
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<StatusMsgModel>() {
                    @Override
                    public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                        String error = null;
                        isLoaded[0] = true;
                        Log.e(TAG, "Status act flow " + response.code());
                        if (response.isSuccessful()) {
                            Log.e(TAG, "Update Flower res " + new Gson().toJson(response.body()));
                            StatusMsgModel statusMsgModel = response.body();
                            farm_details_actual_flowering_date.setText(AppConstants.getShowableDate(final_submit_actual_flowering_date, context));
                            if (response.body().getStatus() == 10) {

                                viewFailDialog.showSessionExpireDialog(TestActivity.this, response.body().getMsg());
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                            } else if (statusMsgModel.getStatus() == 1) {

                                //Toast.makeText(context, resources.getString(R.string.flowering_date_updated_success_msg), Toast.LENGTH_LONG).show();
                                Toasty.success(context, resources.getString(R.string.flowering_date_updated_success_msg), Toast.LENGTH_LONG, true).show();

                                onStart();
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isActivityRunning) {

                                            viewFailDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.something_went_wrong_msg));
                                        }
                                    }
                                });

                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isActivityRunning) {

                                            viewFailDialog.showDialog(context, resources.getString(R.string.flowering_date_update_fail_msg));
                                        }
                                    }
                                });
                                error = response.errorBody().string().toString();

                                Log.e("Error", response.errorBody().string().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                            notifyApiDelay(TestActivity.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                        long newMillis = AppConstants.getCurrentMills();
                        isLoaded[0] = true;
                        long diff = newMillis - currMillis;
                        notifyApiDelay(TestActivity.this, call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);
                        Log.e(TAG, "Update Flowering Failure " + t.toString());
                        if (isActivityRunning) {

                            viewFailDialog.showDialog(context, resources.getString(R.string.flowering_date_update_fail_msg));
                        }
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
            farm.setActualFloweringDate(final_submit_actual_flowering_date);
            FarmCacheManager.getInstance(context).updateFarm(farm);
            farm.setIsUpdated("Y");
            farm.setIsUploaded("N");
            farm_details_actual_flowering_date.setText(final_submit_actual_flowering_date);
        }
    }

    private void update_actual_harvesting_date() {
        String submit_format = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat submit_sdf = new SimpleDateFormat(submit_format, Locale.US);
        String final_submit_actual_harvesting_date = submit_sdf.format(myCalendarAddFarmHarvest.getTime());
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            try {
                auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                HarvestAndFloweringSendData harvestAndFloweringSendData = new HarvestAndFloweringSendData();
                harvestAndFloweringSendData.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                harvestAndFloweringSendData.setActual_harvest_date(final_submit_actual_harvesting_date);
                harvestAndFloweringSendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                harvestAndFloweringSendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                Log.e(TAG, "Update Harvest Data " + new Gson().toJson(harvestAndFloweringSendData));
                Call<StatusMsgModel> statusMsgModelCall = apiService.getActualHarvestDateStatus(harvestAndFloweringSendData);
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<StatusMsgModel>() {
                    @Override
                    public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                        String error = null;
                        isLoaded[0] = true;
                        Log.e(TAG, "Status act harv " + response.code());
                        if (response.isSuccessful()) {
                            StatusMsgModel statusMsgModel = response.body();
                            farm_details_actual_harvest_date.setText(AppConstants.getShowableDate(final_submit_actual_harvesting_date, context));
                            Log.e(TAG, "Update Flower Res " + new Gson().toJson(statusMsgModel));

                            if (response.body().getStatus() == 10) {
                                viewFailDialog.showSessionExpireDialog(TestActivity.this, response.body().getMsg());
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                            } else if (statusMsgModel.getStatus() == 1) {

                                // Toast.makeText(context, resources.getString(R.string.harvest_date_updated_success_msg), Toast.LENGTH_LONG).show();
                                Toasty.success(context, resources.getString(R.string.harvest_date_updated_success_msg), Toast.LENGTH_LONG, true).show();
                                onStart();

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isActivityRunning) {

                                            viewFailDialog.showDialog(context, response.body().getMsg());
                                        }
                                    }
                                });

                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            try {
                                error = response.errorBody().string().toString();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isActivityRunning) {

                                            viewFailDialog.showDialog(context, resources.getString(R.string.harvest_date_update_fail_msg));
                                        }
                                    }
                                });


                                Log.e("Error", response.errorBody().string().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                            notifyApiDelay(TestActivity.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                        long newMillis = AppConstants.getCurrentMills();
                        isLoaded[0] = true;
                        long diff = newMillis - currMillis;
                        notifyApiDelay(TestActivity.this, call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);
                        Log.e(TAG, "Update Harvest Date Failure " + t.toString());
                        if (isActivityRunning) {
                            viewFailDialog.showDialog(context, resources.getString(R.string.harvest_date_update_fail_msg));
                        }
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
            farm.setActualHarvestDate(final_submit_actual_harvesting_date);
            FarmCacheManager.getInstance(context).updateFarm(farm);
            farm.setIsUpdated("Y");
            farm.setIsUploaded("N");
            farm_details_actual_harvest_date.setText(final_submit_actual_harvesting_date);
        }

    }

    private void datePickingClicks() {
        final DatePickerDialog.OnDateSetListener add_details_farm_flowering = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarAddFarmflowering.set(Calendar.YEAR, year);
                myCalendarAddFarmflowering.set(Calendar.MONTH, monthOfYear);
                myCalendarAddFarmflowering.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                new AlertDialog.Builder(context)
                        .setMessage(resources.getString(R.string.sure_want_to_enter_actual_flowering_mg))
                        .setCancelable(false)
                        .setPositiveButton(resources.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                update_actual_flowering_date();
                            }
                        })
                        .setNegativeButton(resources.getString(R.string.no), null)
                        .show();

            }
        };


        final DatePickerDialog.OnDateSetListener add_details_farm_harvesting = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarAddFarmHarvest.set(Calendar.YEAR, year);
                myCalendarAddFarmHarvest.set(Calendar.MONTH, monthOfYear);
                myCalendarAddFarmHarvest.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                new AlertDialog.Builder(context)
                        .setMessage(resources.getString(R.string.sure_want_to_enter_actual_harvest_mg))
                        .setCancelable(false)
                        .setPositiveButton(resources.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                update_actual_harvesting_date();
                            }
                        })
                        .setNegativeButton(resources.getString(R.string.no), null)
                        .show();
            }
        };

        farm_details_actual_flowering_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  new DatePickerDialog(context, add_details_farm_flowering, myCalendarAddFarmflowering
                        .get(Calendar.YEAR), myCalendarAddFarmflowering.get(Calendar.MONTH),
                        myCalendarAddFarmflowering.get(Calendar.DAY_OF_MONTH)).show();*/
                if (noOfDays > 3) {
                    Snackbar snackbar = Snackbar.make(content, context.getResources().getString(R.string.no_of_days_offline_exceeded), Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                } else {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(TestActivity.this, add_details_farm_flowering, myCalendarAddFarmflowering
                            .get(Calendar.YEAR), myCalendarAddFarmflowering.get(Calendar.MONTH),
                            myCalendarAddFarmflowering.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                    datePickerDialog.show();
                }
            }
        });

        farm_details_actual_harvest_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (noOfDays > 3) {
                    Snackbar snackbar = Snackbar.make(content, context.getResources().getString(R.string.no_of_days_offline_exceeded), Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction(resources.getString(R.string.ok_msg), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                } else {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, add_details_farm_harvesting, myCalendarAddFarmHarvest
                            .get(Calendar.YEAR), myCalendarAddFarmHarvest.get(Calendar.MONTH), myCalendarAddFarmHarvest.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                    datePickerDialog.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else if (!isListView) {
            recyclerView.setVisibility(View.VISIBLE);
            chartLayout.setVisibility(View.VISIBLE);
            isListView = true;
            calendarLayout.setVisibility(View.GONE);
            calImage.setImageResource(R.drawable.ic_date_range_white);
        } else {
            finish();
        }
    }

    List<SampleGerminationDatum> germinationDataList = new ArrayList<>();

    private void germinationDataSet(FetchFarmResult fetchFarmResult) {
        if (fetchFarmResult.getGermination() != null && !fetchFarmResult.getGermination().equals("0")) {
            fabChildAddGermination.setLabelText(resources.getString(R.string.germination_label));
            fabChildAddGermination.setImageResource(android.R.drawable.ic_menu_add);
            fabChildAddGermination.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fabParent.isOpened()) {
                        fabParent.close(true);
                    }
                    if (fetchFarmResult.getActualArea() != null &&
                            !TextUtils.isEmpty(fetchFarmResult.getActualArea()) &&
                            Double.valueOf(fetchFarmResult.getActualArea().trim()) > 0) {
                        AddGermiFullScreenDialog dialog = new AddGermiFullScreenDialog(germinationDataList, context);
                        dialog.show(getSupportFragmentManager(), AddGermiFullScreenDialog.TAG);
                    } else {
                        noAcrualAreaEvent();
                    }


                }
            });
        } else {
            Log.e(TAG, "In Else Setting Germination Data from farm details");
            //germination_button.setVisibility(View.VISIBLE);
            String addGermiLabel = resources.getString(R.string.germination_label);
            fabChildAddGermination.setLabelText(addGermiLabel);
            fabChildAddGermination.setImageResource(android.R.drawable.ic_menu_add);
            fabChildAddGermination.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fabParent.isOpened()) {
                        fabParent.close(true);
                    }
                    if (fetchFarmResult.getActualArea() != null &&
                            !TextUtils.isEmpty(fetchFarmResult.getActualArea()) &&
                            Double.valueOf(fetchFarmResult.getActualArea().trim()) > 0) {
                        AddGermiFullScreenDialog dialog = new AddGermiFullScreenDialog(germinationDataList, context);
                        dialog.show(getSupportFragmentManager(), AddGermiFullScreenDialog.TAG);
                    } else {
                        noAcrualAreaEvent();
                    }

                }

            });
        }
    }

    private void noAcrualAreaEvent() {
        String msg = "";
        msg = resources.getString(R.string.no_actual_area_available_go_to_capture);
        //Toast.makeText(context, "Go to capture area", Toast.LENGTH_LONG).show();
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
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private String convertAreaTo(String area) {

        return AppConstants.getShowableArea(area);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fabParent.isOpened()) {
            fabParent.close(true);
        }
    }

    List<BinSet> binSetList = new ArrayList<>();
    List<BinSet> binSetListNdwi = new ArrayList<>();

    private void getSatSureData() {
        auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getSatsureData(farmId, userId).enqueue(new Callback<SatsureResponse>() {
            @Override
            public void onResponse(Call<SatsureResponse> call, Response<SatsureResponse> response) {
                Log.e(TAG, "Status sat " + response.code());
                String error = null;
                isLoaded[0] = true;
                if (response.isSuccessful()) {
                    SatsureResponse satsureResponse = response.body();
                    if (satsureResponse != null && satsureResponse.getSatsureData() != null && satsureResponse.getSatsureData().size() > 0) {
                        Log.e(TAG, "SatSure res " + new Gson().toJson(satsureResponse));
                        int ndviPos = 0, ndwiPos = 0;
                        /*for (int i = 0; i < satsureResponse.getData().size(); i++) {
                            SatsureData data = satsureResponse.getData().get(i);
                            if (satsureResponse.getData().get(i).getDate() != null && !TextUtils.isEmpty(satsureResponse.getData().get(i).getDate())) {
                                data.setType(AppConstants.TIMELINE.SAT);
                                data.setDoa(data.getDate());
                                objectList.add(data);
                                objectTypeList.add(new ObjectType(data.getDate(), AppConstants.TIMELINE.SAT));
                            }
                            if (data.getProductId() != null && data.getProductId().trim().equals("3")) {
                                satsureResponse.getData().get(i).setSliderIndex(ndviPos);
                                ndviPos++;

                            } else if (data.getProductId() != null && data.getProductId().trim().equals("4")) {

                                satsureResponse.getData().get(i).setSliderIndex(ndwiPos);
                                ndwiPos++;
                            }
                        }*/
                        for (int i = 0; i < satsureResponse.getSatsureData().size(); i++) {

                            for (int j = 0; j < satsureResponse.getSatsureData().get(i).size(); j++) {
                                SatsureData data = satsureResponse.getSatsureData().get(i).get(j);
                                if (data.getDate() != null && !TextUtils.isEmpty(data.getDate())) {
                                    data.setType(AppConstants.TIMELINE.SAT);
                                    data.setDoa(data.getDate());
                                    data.setSliderIndex(j);
//                                    dataCount++;
//                                    data.setIndex(dataCount);
                                    objectList.add(data);
                                    objectTypeList.add(new ObjectType(data.getDate(), AppConstants.TIMELINE.SAT));
                                }
                               /* if (data.getProductId() != null && data.getProductId().trim().equals("3")) {
                                    satsureResponse.getData().get(i).setSliderIndex(ndviPos);
                                    ndviPos++;

                                } else if (data.getProductId() != null && data.getProductId().trim().equals("4")) {

                                    satsureResponse.getData().get(i).setSliderIndex(ndwiPos);
                                    ndwiPos++;
                                }   */
                            }
                        }
                        DataHandler.newInstance().setSatsureDataList(satsureResponse.getData());
                        setRecyclerData();
                    } else {
                        Log.e(TAG, "SatSure else 1");
                        setRecyclerData();
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string();
                        Log.e(TAG, "SatSure else 2 " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setRecyclerData();
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(TestActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<SatsureResponse> call, Throwable t) {
                Log.e(TAG, "SatSure Failure " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(TestActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                setRecyclerData();
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


    /*private void getSatSureData() {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        apiInterface.getSatsureData(farmId).enqueue(new Callback<SatsureResponse>() {
            @Override
            public void onResponse(Call<SatsureResponse> call, Response<SatsureResponse> response) {
                if (response.isSuccessful()) {
                    SatsureResponse satsureResponse = response.body();
                    if (satsureResponse != null && satsureResponse.getData() != null) {
                        Log.e(TAG, "SatSure res " + new Gson().toJson(satsureResponse));
                        getSatSureDataProduct(satsureResponse);
                    } else {
                        Log.e(TAG, "SatSure else 1");
                        setRecyclerData();
                    }
                } else {
                    try {
                        Log.e(TAG, "SatSure else 2 " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setRecyclerData();
                }
            }

            @Override
            public void onFailure(Call<SatsureResponse> call, Throwable t) {
                Log.e(TAG, "SatSure Failure " + t.toString());
                setRecyclerData();
            }
        });

    }



    private void getSatSureDataProduct(final SatsureResponse satsureResponse) {

        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getSatsureProducts(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID)).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    Log.e(TAG, "SatSure res Meta" + new Gson().toJson(productResponse));
                    if (productResponse != null && productResponse.getData() != null)
                        if (satsureResponse.getData() != null && satsureResponse.getData().getProductData() != null) {
                            for (int i = 0; i < satsureResponse.getData().getProductData().size(); i++) {
                                ProductDatum datum = satsureResponse.getData().getProductData().get(i);
                                Log.e(TAG, "Satelite " + i + "-> " + new Gson().toJson(datum));

                                if (datum != null && datum.getProductDetails() != null && datum.getProductDetails().getBinSets() != null) {


                                    for (int j = 0; j < datum.getProductDetails().getBinSets().size(); j++) {
                                        try {
                                            BinSet binSet = datum.getProductDetails().getBinSets().get(j);
                                            Log.e(TAG, "Satelite binset of product " + i + " at " + j + " " + new Gson().toJson(binSet));
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-MM");
                                            Date dt = format.parse(binSet.getDate());
                                            SimpleDateFormat your_format = new SimpleDateFormat("yyyy-MM-dd");
                                            binSet.setDate(your_format.format(dt));
                                            binSet.setDoa(your_format.format(dt));
                                            String dName = "-", pName = "-", value = "Value";
                                            for (int k = 0; k < productResponse.getData().size(); k++) {
                                                com.i9930.croptrails.Test.SatusreModel.Product.ProductDatum datum1 = productResponse.getData().get(k);
                                                if (datum.getProductId() == datum1.getProductId()) {
                                                    dName = datum1.getDisplayName();
                                                    pName = datum1.getProductName();
                                                    try {
                                                        value = getResources().getString(R.string.average_value_label);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                            }
                                            binSet.setValueName(value);
                                            binSet.setDisplayName(dName);
                                            binSet.setProductName(pName);
                                            binSet.setImage(datum.getProductDetails().getImageBaseUrl() + binSet.getImageUrl());
                                            if (datum.getProductId() == 1) {
                                                binSetList.add(binSet);
                                            } else if (datum.getProductId() == 2) {
                                                binSetListNdwi.add(binSet);
                                            }
                                            if (binSet.getDoa() != null && !TextUtils.isEmpty(binSet.getDoa())) {
                                                objectList.add(binSet);
                                                objectTypeList.add(new ObjectType(binSet.getDoa(), AppConstants.TIMELINE.SAT));
                                            }else
                                                Log.e(TAG,"binset no doa at "+i);

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        } catch (Exception e) {
                                            setRecyclerData();
                                            e.printStackTrace();
                                            setRecyclerData();
                                        }
                                    }
                                } else {
                                    Log.e(TAG, "Satelite else " + i + "-> " + new Gson().toJson(datum));
                                }
                            }
                            DataHandler.newInstance().setBinSetListNdvi(binSetList);
                            DataHandler.newInstance().setBinSetListNdwi(binSetListNdwi);
                            setRecyclerData();
                        } else {
                            setRecyclerData();
                            Log.e(TAG, "SatSure Meta Else 1");
                        }
                    else {
                        Log.e(TAG, "SatSure Meta Else 2");
                        setRecyclerData();
                    }
                } else {
                    try {
                        Log.e(TAG, "SatSure Meta Else 3 " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setRecyclerData();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e(TAG, "SatSure Meta failure " + t.toString());
                setRecyclerData();
            }
        });
    }*/


    private class RecyclerAsync extends AsyncTask<String, Integer, List<String>> {
        @Override
        protected List<String> doInBackground(String... strings) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Collections.sort(objectList, new Comparator<Object>() {
                @Override
                public int compare(Object lhs, Object rhs) {
                    try {
                        JSONObject obj = new JSONObject(new Gson().toJson(lhs));
                        JSONObject obj2 = new JSONObject(new Gson().toJson(rhs));
                        return format.parse(obj.getString("doa")).compareTo(format.parse(obj2.getString("doa")));
                    } catch (ParseException | JSONException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });
            Collections.sort(objectTypeList, new Comparator<ObjectType>() {
                @Override
                public int compare(ObjectType lhs, ObjectType rhs) {
                    try {
                        return format.parse(lhs.getDate()).compareTo(format.parse(rhs.getDate()));
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });

            if (isaFarmLoaded) {
                objectList.add(0, fetchFarmResult);
                if (fetchFarmResult.getDoa() != null && !TextUtils.isEmpty(fetchFarmResult.getDoa()))
                    objectTypeList.add(0, new ObjectType(fetchFarmResult.getDoa(), AppConstants.TIMELINE.FARM));
                else
                    objectTypeList.add(0, new ObjectType("2010-01-21", AppConstants.TIMELINE.FARM));
            }
//            For adding adView
            for (int i = 0; i < objectList.size(); i+=3) {
                try {
                    if (i>objectList.size()-1){
                        objectTypeList.add(new ObjectType(AppConstants.getCurrentDate(), AppConstants.TIMELINE.ADVERTISE));
                        objectList.add(getAdView());
                    }else{
                        objectTypeList.add(i,new ObjectType(AppConstants.getCurrentDate(), AppConstants.TIMELINE.ADVERTISE));
                        objectList.add(i,getAdView());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < objectList.size(); i++) {
                if (!(objectList.get(i)  instanceof AdView)) {
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(new Gson().toJson(objectList.get(i)));
                    /*if (obj.has("index")){
                        obj.put("index",i);
                    }*/
                        Log.e(TAG, "Index loop " + i);
                        obj.put("index", i);
                        Object object = new Gson().fromJson(obj.toString(), Object.class);
                        objectList.set(i, object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }



            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setRecyclerData2();
                    cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
                    if (lastSelectdeDate != null)
                        cal_month.setTime(lastSelectdeDate);
                    cal_month_copy = (GregorianCalendar) cal_month.clone();
                    calendarAdapter = new CalendarViewAdapter(TestActivity.this, cal_month, emp_data_arr, objectTypeList, hashMap);
                    hashMap = groupDataIntoHashMap(objectList);
                    calendarActivity(objectList, objectTypeList);
                }
            });
            return null;
        }
    }

    private AdView getAdView(){
         AdView adView = new AdView(context);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getResources().getString(R.string.banner_unit_id));
        return adView;
    }

    private void setRecyclerData2() {
        MyCustomLayoutManager manager = new MyCustomLayoutManager(context);
        progressBar.setVisibility(View.GONE);
        List<TimelineUnits> timelineUnits = DataHandler.newInstance().getTimelineUnits();
        adapterTest = new TimelineAdapterTest(context, context, objectList, objectTypeList, timelineUnits, isCurrentLocation, new TimelineAdapterTest.FarmDetailsClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onFarmDetailsClicked() {
                if (!drawerLayout.isDrawerOpen(GravityCompat.END))
                    drawerLayout.openDrawer(Gravity.RIGHT);
                else drawerLayout.closeDrawer(Gravity.END);
            }

            @Override
            public void onMapIconClicked() {
                GetGeoJsonData fetchGpsCordinates = new GetGeoJsonData(true, AppConstants.MAP_NAV_MODE.SHOW_AREA);
                fetchGpsCordinates.execute();
            }
        });
        adapterTest.setOnSoilCardClick(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterTest);
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            setLineChartInfo(lineChartInfoList);
            if (farmResult != null && farmResult.getLatCord() != null && !farmResult.getLatCord().equals("0")) {
                try {
                    callWeatherApi(Double.parseDouble(farmResult.getLatCord()), Double.parseDouble(farmResult.getLongCord()), "From Setting data when correct farm lat long availble from try", false);
                } catch (Exception e) {
                    Log.e(TAG, "Weather Exc  " + e.toString());
                    if (currentLocation != null) {
                        callWeatherApi(currentLocation.getLatitude(), currentLocation.getLongitude(), "From Setting data when current lat long availble from catch", true);
                    } else {
                        Log.e(TAG, "Current location Not found");
                    }
                }
            } else {
                Log.e(TAG, "Farm location Not found");
                if (currentLocation != null) {
                    callWeatherApi(currentLocation.getLatitude(), currentLocation.getLongitude(), "From Setting data when current lat long availble and farm location unavailable", true);

                } else {
                    Log.e(TAG, "Current location Not found");
                }
            }
        } else {
            chartLayout.setVisibility(View.GONE);
        }
    }


    private void getParticularCalendar() {
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getParticularCalendar(userId, token, farmId, compId).enqueue(new Callback<SingleCalendarResponse>() {
            @Override
            public void onResponse(Call<SingleCalendarResponse> call, Response<SingleCalendarResponse> response) {
                Log.e(TAG, "Status getParticularCalendar" + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG, "getParticularCalendar " + new Gson().toJson(response.body()));
                    try {
                        if (response.body().getStatus() == 0) {
                            if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE))
                                fabChildAssignCalendar.setVisibility(View.VISIBLE);
                            else
                                fabChildAssignCalendar.setVisibility(View.GONE);
                        } else {
                            fabChildAssignCalendar.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        fabChildAssignCalendar.setVisibility(View.GONE);
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    fabChildAssignCalendar.setVisibility(View.GONE);
                    try {
                        Log.e(TAG, "getParticularCalendar err " + response.errorBody().string().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SingleCalendarResponse> call, Throwable t) {
                fabChildAssignCalendar.setVisibility(View.GONE);
                Log.e(TAG, "getParticularCalendar  fail " + t.toString());
            }
        });
    }


    GregorianCalendar cal_month, cal_month_copy;
    private CalendarViewAdapter calendarAdapter;
    ArrayList<Object> emp_data_arr = new ArrayList<>();
    @BindView(R.id.tv_month)
    TextView tv_month;


    HashMap<String, List<Object>> hashMap;

    /* private void setRecyclerData() {
         hashMap = groupDataIntoHashMap(objectList);
         calendarActivity(objectList, objectTypeList);

     }*/
    int lastIndex = 0;


    public void calendarActivity(List<Object> employeeData, List<ObjectType> objectTypes) {

        if (employeeData != null)
            emp_data_arr.addAll(employeeData);

        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        calendarAdapter = new CalendarViewAdapter(this, cal_month, emp_data_arr, objectTypes, hashMap);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));

        ImageButton previous = findViewById(R.id.ib_prev);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (cal_month.get(GregorianCalendar.MONTH) == 0 && cal_month.get(GregorianCalendar.YEAR) == 2019) {
                    Toast.makeText(CalendarViewActivity.this, "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                } else {*/
                setPreviousMonth();
                refreshCalendar();
//                }
                Log.e(TAG, "Previous month");
            }
        });

        ImageButton next = findViewById(R.id.ib_next);
//        next.setVisibility(View.GONE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (cal_month.get(GregorianCalendar.MONTH) == 11 && cal_month.get(GregorianCalendar.YEAR) == 2019) {
                    Toast.makeText(CalendarViewActivity.this, "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                } else {*/
                setNextMonth();
                refreshCalendar();
//                }
            }
        });

        GridView gridview = findViewById(R.id.gv_calendar);
        gridview.setAdapter(calendarAdapter);

        calendarAdapter.setOnItemClickListener(new CalendarViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Object object) {
                if (object != null) {
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(new Gson().toJson(object));
                        if (obj.has("index")) {
                            int index = obj.getInt("index");
                            recyclerView.setVisibility(View.VISIBLE);
                            if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE))
                                chartLayout.setVisibility(View.VISIBLE);
                            isListView = true;
                            calendarLayout.setVisibility(View.GONE);
                            calImage.setImageResource(R.drawable.ic_date_range_white);
                            if (index != 0 && index < objectList.size() && lastIndex < index) {
                                lastIndex = index;
                                index++;

                            }
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, new RecyclerView.State(), index);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        /*gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                String selectedGridDate = CalendarViewAdapter.day_string.get(position);
                Log.e(TAG,"CalendarViewAdapter date "+selectedGridDate);

            }

        });
*/

    }

    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
            if (cal_month != null)
                lastSelectdeDate = cal_month.getTime();
        } else {
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) + 1);
            if (cal_month != null)
                lastSelectdeDate = cal_month.getTime();
        }
    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
            if (cal_month != null)
                lastSelectdeDate = cal_month.getTime();
        } else {
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) - 1);
            if (cal_month != null)
                lastSelectdeDate = cal_month.getTime();
        }


    }

    public void refreshCalendar() {

        if (calendarAdapter != null) {
            calendarAdapter.refreshDays();
            calendarAdapter.notifyDataSetChanged();
            tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
        }

    }

    private HashMap<String, List<Object>> groupDataIntoHashMap(List<Object> listOfPojosOfJsonArray) {
        HashMap<String, List<Object>> groupedHashMap = new HashMap<>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat format2 = new SimpleDateFormat("yyyy-MM");
        for (int i = 0; i < listOfPojosOfJsonArray.size(); i++) {

            Object pojoOfJsonArray = listOfPojosOfJsonArray.get(i);
            if (!(pojoOfJsonArray instanceof  AdView)) {
                try {
                    JSONObject obj = null;
                    obj = new JSONObject(new Gson().toJson(pojoOfJsonArray));
                    String date = obj.getString("doa");
                    date = format2.format(format.parse(date));
                    String hashMapKey = date;
                    if (groupedHashMap.containsKey(hashMapKey)) {
                        // The key is already in the HashMap; add the pojo object
                        // against the existing key.
                        groupedHashMap.get(hashMapKey).add(pojoOfJsonArray);
                    } else {
                        // The key is not there in the HashMap; create a new key-value pair
                        List<Object> list = new ArrayList<>();
                        list.add(pojoOfJsonArray);
                        groupedHashMap.put(hashMapKey, list);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return groupedHashMap;
    }


    private void checkHealthCardParams() {

        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        final boolean[] isLoaded = {false};
        apiInterface.getSoilHealthCardDropDown(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID), SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID)).enqueue(new Callback<HealthCardDDResponse>() {
            @Override
            public void onResponse(Call<HealthCardDDResponse> call, Response<HealthCardDDResponse> response) {
                String error = null;
                isLoaded[0] = true;
                if (response.isSuccessful()) {
                    Log.e(TAG, "DD Res " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 10) {
                        fabChildSoilHealthCard.setVisibility(View.GONE);

                    } else if (response.body().getStatus() == 1) {

                        if (response.body().getHealthCardParamsList() == null || response.body().getHealthCardParamsList().size() == 0) {
                            fabChildSoilHealthCard.setVisibility(View.GONE);
//                            fabChildSoilHealthCard.hide(false);
                        } else {
                            if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE))
                                fabChildSoilHealthCard.setVisibility(View.VISIBLE);
//                            fabChildSoilHealthCard.hide(false);
                        }

                    } else {
                        fabChildSoilHealthCard.setVisibility(View.GONE);
//                        fabChildSoilHealthCard.hide(false);
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    fabChildSoilHealthCard.setVisibility(View.GONE);
//                    fabChildSoilHealthCard.hide(false);
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    fabChildSoilHealthCard.setVisibility(View.GONE);
//                    fabChildSoilHealthCard.hide(false);
                } else {
                    fabChildSoilHealthCard.setVisibility(View.GONE);
//                    fabChildSoilHealthCard.hide(false);
                }

            }

            @Override
            public void onFailure(Call<HealthCardDDResponse> call, Throwable t) {
                isLoaded[0] = true;
                fabChildSoilHealthCard.setVisibility(View.GONE);
            }
        });
    }

    List<JsonObject> imageList = new ArrayList<>();

    private void getFullDetails() {
        JsonObject object = new JsonObject();
        object.addProperty("farm_id", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
        object.addProperty("user_id", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        object.addProperty("token", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        Log.e(TAG, "SEND FULL DETAIL PARAM  " + new Gson().toJson(object));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        apiInterface.getFullFarmDetails(object).enqueue(new Callback<FarmFullDetails>() {
            @Override
            public void onResponse(Call<FarmFullDetails> call, Response<FarmFullDetails> response1) {
                String error = null;
                Log.e(TAG, "Response FULL CODE" + response1.code());
                if (response1.isSuccessful()) {
                    Log.e(TAG, "Response FULL  " + new Gson().toJson(response1.body()));
                    DataHandler.newInstance().setFarmFullDetails(response1.body());
                    FarmFullDetails details = response1.body();
                    try {
                        if (details.getPersonDetails() != null) {
                            PersonDetails personDetails = details.getPersonDetails();
                            if (AppFeatures.isChakLeaderEnabled() && details.getFarmAddressDetails() != null) {
                                if (AppConstants.isValidString(details.getFarmAddressDetails().getAddL2())) {
                                    getChakLeader("" + details.getPersonDetails().getName(), details.getPersonDetails().getPersonId() + "", details.getFarmAddressDetails().getAddL2());
                                }
                            }

                            if (AppConstants.isValidString(personDetails.getGender())) {
                                if (personDetails.getGender().equalsIgnoreCase("m")) {
                                    gender.setText(resources.getString(R.string.male_label));
                                } else if (personDetails.getGender().equalsIgnoreCase("f")) {
                                    gender.setText(resources.getString(R.string.female_label));
                                } else if (personDetails.getGender().equalsIgnoreCase("o")) {
                                    gender.setText(resources.getString(R.string.other_labe));
                                } else {
                                    gender.setText("-");
                                    hideCard(genderCard);
                                }
                            } else {
                                gender.setText("-");
                                hideCard(genderCard);
                            }


                            if (AppConstants.isValidString(personDetails.getDob()) && !personDetails.getDob().equals(for_date)) {
                                dobTv.setText(AppConstants.getShowableDate(personDetails.getName(), context));
                            } else if (AppConstants.isValidString(personDetails.getYob())) {
                                dobTv.setText(personDetails.getYob());
                            } else {
                                dobTv.setText("-");

                                hideCard(dobCard);
                            }
                            if (AppConstants.isValidString(personDetails.getFatherName())) {
                                fatherNameTv.setText(personDetails.getFatherName());
                            } else {
                                fatherNameTv.setText("-");
                                hideCard(fatherCard);
                            }
                            if (AppConstants.isValidString(details.getPersonDetails().getImgLink())) {

                                JsonObject object1 = new JsonObject();
                                object1.addProperty("type", AppConstants.DOCS.PROFILE);
                                object1.addProperty("link", personDetails.getImgLink());


                                imageList.add(object1);
                            }
                            if (AppConstants.isValidString(details.getPersonDetails().getIdProof())) {
//                            imageList.put(AppConstants.DOCS.ID_PROOF,details.getPersonDetails().getIdProof());

                                JsonObject object1 = new JsonObject();
                                object1.addProperty("type", AppConstants.DOCS.ID_PROOF);
                                object1.addProperty("link", details.getPersonDetails().getIdProof());
                                imageList.add(object1);
                            }
                            if (AppConstants.isValidString(details.getPersonDetails().getAddressProof())) {
//                            imageList.put(AppConstants.DOCS.ADDRESS_PROOF,details.getPersonDetails().getAddressProof());
                                JsonObject object1 = new JsonObject();
                                object1.addProperty("type", AppConstants.DOCS.ADDRESS_PROOF);
                                object1.addProperty("link", details.getPersonDetails().getAddressProof());
                                imageList.add(object1);
                            }
                        }
                        if (details.getFarmsDetails() != null) {
                            Log.e(TAG, "FULL FARM " + new Gson().toJson(details.getFarmsDetails()));
                            if (AppConstants.isValidString(details.getFarmsDetails().getOwnershipDoc())) {
                                {
                                    JsonObject object1 = new JsonObject();
                                    object1.addProperty("type", AppConstants.DOCS.OWNERSHIP);
                                    object1.addProperty("link", details.getFarmsDetails().getOwnershipDoc());
                                    imageList.add(object1);
                                }
                            }
                            if (AppConstants.isValidString(details.getFarmsDetails().getFarmPhoto())) {
                                JsonObject object1 = new JsonObject();
                                object1.addProperty("type", AppConstants.DOCS.FARM);
                                object1.addProperty("link", details.getFarmsDetails().getFarmPhoto());
                                imageList.add(object1);
                            }
                        }

                        if (details.getFarmsDetails() != null) {
                            FarmsDetails farmsDetails = details.getFarmsDetails();
                            if (AppConstants.isValidString(farmsDetails.getCompFarmId())) {
                                farmIdTv.setText(farmsDetails.getCompFarmId());
                            } else {
                                farmIdTv.setText("-");
                            }


                        }
                        if (details.getFarmCropDetails() != null) {
                            FarmCropDetails cropDetails = details.getFarmCropDetails();

                            if (cropDetails.getCropName() != null) {
                                currentCropTv.setText(cropDetails.getCropName());
                            } else
                                hideCard(currentCropCard);
                        }
                        if (details.getFarmAddressDetails() != null) {
                            FarmAddressDetails addressDetails = details.getFarmAddressDetails();
                            if (AppConstants.isValidString(addressDetails.getAddL1()) && !addressDetails.getAddL1().equals("0")) {
                                khasraTv.setText(addressDetails.getAddL1());
                            } else {
                                khasraTv.setText("-");
                                hideCard(addlCard);
                            }

                            if (AppConstants.isValidString(addressDetails.getAddL2()) && !addressDetails.getAddL2().equals("0")) {
                                chakTv.setText(addressDetails.getAddL2());
                            } else {
                                chakTv.setText("-");
                                hideCard(addL2Card);
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (imageList.size() > 0) {
                        Log.e(TAG, "imageList " + new Gson().toJson(imageList));
                        DataHandler.newInstance().setImageListDoc(imageList);
                        docImage.setVisibility(View.VISIBLE);
                        docImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(context, DocumentActivity.class));
                            }
                        });
                    } else {
                        docImage.setVisibility(View.GONE);
                    }


                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    DataHandler.newInstance().setFarmFullDetails(null);
                    //un authorized access
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    DataHandler.newInstance().setFarmFullDetails(null);
                    // auth token expired
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        DataHandler.newInstance().setFarmFullDetails(null);
                        error = response1.errorBody().string().toString();
                        Log.e(TAG, " Error FULL " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FarmFullDetails> call, Throwable t) {
                Log.e(TAG, "Failure FULL " + t.toString());
                DataHandler.newInstance().setFarmFullDetails(null);
            }
        });


    }

    private void getChakLeader(String name, String farmerId, String chak) {
        String farm_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comp_id", "" + comp_id);
        jsonObject.addProperty("user_id", "" + userId);
        jsonObject.addProperty("token", "" + token);
        jsonObject.addProperty("farm_id", "" + farm_id);
//        jsonObject.addProperty("addl2",""+chak);
        Log.e(TAG, "getChakLeader REQ " + new Gson().toJson(jsonObject));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        Call<FetchChakResponse> farmAndHarvestCall = apiInterface.getChakLeader(jsonObject);
        farmAndHarvestCall.enqueue(new Callback<FetchChakResponse>() {
            @Override
            public void onResponse(Call<FetchChakResponse> call, Response<FetchChakResponse> response) {
                Log.e(TAG, "getChakLeader farm " + response.code());
                isLoaded[0] = true;
                String error = "";
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "getChakLeader RES " + new Gson().toJson(response.body()));
                        FetchChakResponse chakResponse = response.body();
                        if (chakResponse.getData() == null || chakResponse.getData().size() == 0) {
                            if (AppConstants.isValidString(chak) && !chak.trim().equals("0"))
                                showChakLeaderDialog(name, farmerId, chak);
                        } else {
                            FetchChakDatum datum = chakResponse.getData().get(0);
                            if (!AppConstants.isValidString(datum.getLeadChak())) {
                                if (AppConstants.isValidString(chak) && !chak.trim().equals("0"))
                                    showChakLeaderDialog(name, farmerId, chak);
                            } else {
                                chakLeadCard.setVisibility(View.VISIBLE);
                                chakLeadCardMob.setVisibility(View.VISIBLE);
                                if (AppConstants.isValidString(datum.getName()))
                                    chakLeadNameTv.setText(datum.getName());
                                else
                                    chakLeadNameTv.setText("-");

                                if (AppConstants.isValidString(datum.getMob()))
                                    chakLeadNameTvMob.setText(datum.getMob());
                                else
                                    chakLeadNameTvMob.setText("-");
                            }
                        }
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        isaFarmLoaded = false;
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "getChakLeader Unsuccess " + error);

                    } catch (IOException e) {
                        e.printStackTrace();
                        isaFarmLoaded = false;
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(TestActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<FetchChakResponse> call, Throwable t) {
                isaFarmLoaded = false;
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(TestActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);

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

    private void setChakLeader(String farmerId, String chak, String name) {
        String farm_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comp_id", "" + comp_id);
        jsonObject.addProperty("user_id", "" + userId);
        jsonObject.addProperty("token", "" + token);
        jsonObject.addProperty("farm_id", "" + farm_id);
        jsonObject.addProperty("addL2", "" + chak);
        jsonObject.addProperty("farmer_id", "" + farmerId);
        Log.e(TAG, "setChakLeader REQ " + new Gson().toJson(jsonObject));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        Call<StatusMsgModel> farmAndHarvestCall = apiInterface.setChakLeader(jsonObject);
        farmAndHarvestCall.enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                Log.e(TAG, "setChakLeader farm " + response.code());
                isLoaded[0] = true;
                String error = "";
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "setChakLeader RES " + new Gson().toJson(response.body()));
                        Toasty.success(context, name + " " + "assigned as chak leader", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(TestActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        isaFarmLoaded = false;
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "setChakLeader Unsuccess " + error);

                    } catch (IOException e) {
                        e.printStackTrace();
                        isaFarmLoaded = false;
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(TestActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                isaFarmLoaded = false;
                isLoaded[0] = true;
                Log.e(TAG, "setChakLeader fail " + t.toString());
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(TestActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);

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

    private void showChakLeaderDialog(String name, String farmerId, String chak) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_chak_leader);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView title = dialog.findViewById(R.id.title);
        ImageView close = dialog.findViewById(R.id.close);
        TextView msgTv = dialog.findViewById(R.id.msgTv);
        Button markButton = dialog.findViewById(R.id.markButton);
        Button noButton = dialog.findViewById(R.id.noButton);

//        String msg = "" + resources.getString(R.string.mark) + " " + "<b>" + name + "</b> " + " " + resources.getString(R.string.as_chak_leader) + " " + "<b>" + chak + "</b> ";
        String msg = "" + resources.getString(R.string.mark) + " " + "<b>" + name + "</b> " + " " + resources.getString(R.string.as_chak_leader) + "?";

        msgTv.setText(Html.fromHtml(msg));

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setChakLeader(farmerId, chak, name);
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    private void getFullDetailsByMobile() {

        JsonObject object = new JsonObject();
        object.addProperty("mob", "" + "7869107214");
        object.addProperty("user_id", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        object.addProperty("token", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        Log.e(TAG, "SEND FULL DETAIL PARAM MOB  " + new Gson().toJson(object));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        apiInterface.getDeialsByMob2(object).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response1) {
                String error = null;
                Log.e(TAG, "Response FULL MOB CODE" + response1.code());
                if (response1.isSuccessful()) {
                    try {
                        Log.e(TAG, "Response MOB FULL  " + response1.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
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
                        Log.e(TAG, " Error FULL MOB " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Failure FULL MOB " + t.toString());
            }
        });


    }

    private void getAllVisitsOfFarm() {
        String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("farm_id", farmId);
        jsonObject.addProperty("user_id", userId);
        jsonObject.addProperty("token", token);
        apiInterface.getAllVisitsOfFarm(jsonObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.e(TAG, "getAllVisitsOfFarm res " + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e(TAG, "getAllVisitsOfFarm err " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "getAllVisitsOfFarm fail " + t.toString());
            }
        });
    }
}
