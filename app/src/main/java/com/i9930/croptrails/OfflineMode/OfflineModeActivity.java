package com.i9930.croptrails.OfflineMode;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.AddFarm.Model.CowAndCatles;
import com.i9930.croptrails.AddFarm.Model.FPOCrop;
import com.i9930.croptrails.AddFarm.Model.FarmCrop;
import com.i9930.croptrails.AddFarm.Model.FarmData;
import com.i9930.croptrails.AddFarm.Model.FarmerAndFarmData;
import com.i9930.croptrails.AddFarm.Model.FarmerSpinnerData;
import com.i9930.croptrails.AddFarm.Model.FetchFarmerResponse;
import com.i9930.croptrails.AddFarm.Model.FormModel;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.Cluster;
import com.i9930.croptrails.CommonClasses.ClusterResponse;
import com.i9930.croptrails.CommonClasses.DDNew;
import com.i9930.croptrails.CommonClasses.DDResponseNew;
import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormDatum;
import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormResponse;
import com.i9930.croptrails.CommonClasses.ParamData;
import com.i9930.croptrails.CommonClasses.PldReason;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.FarmDetailsUpdateActivity2;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.IrrigationSource;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.IrrigationType;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.Season;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.SoilType;
import com.i9930.croptrails.FarmDetails.Model.Visit.VisitResponse;
import com.i9930.croptrails.FarmDetails.Model.Visit.VisitResponseDatum;
import com.i9930.croptrails.FarmerInnerDashBoard.Models.FarmDetails;
import com.i9930.croptrails.HarvestReport.Model.ViewHarvestDetails;
import com.i9930.croptrails.Landing.FilterActivity;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Landing.Models.FetchFarmData;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.Landing.Models.FetchFarmSendData;
import com.i9930.croptrails.Landing.Models.Filter.VillageChakResponse;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;
import com.i9930.croptrails.OfflineMode.Adapter.OfflineFarmsAdapter;
import com.i9930.croptrails.OfflineMode.Model.CalendarActResponse;
import com.i9930.croptrails.OfflineMode.Model.FarmIdArray;
import com.i9930.croptrails.OfflineMode.Model.FarmsCoordinatesResponse;
import com.i9930.croptrails.OfflineMode.Model.InputCostAndResourceData;
import com.i9930.croptrails.OfflineMode.Model.OfflineResponse;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.DoneActivities.ActivityCacheManager;
import com.i9930.croptrails.RoomDatabase.DoneActivities.DoneActivity;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownCacheManager;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownCacheManager2;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownT2;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmCacheManager;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmNotifyInterface;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerCacheManager;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerT;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestCacheManager;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestT;
import com.i9930.croptrails.RoomDatabase.InputCost.InputCostCacheManager;
import com.i9930.croptrails.RoomDatabase.InputCost.InputCostT;
import com.i9930.croptrails.RoomDatabase.ResourceCost.ResourceCacheManager;
import com.i9930.croptrails.RoomDatabase.ResourceCost.ResourceCostT;
import com.i9930.croptrails.RoomDatabase.Timeline.CalendarJSON;
import com.i9930.croptrails.RoomDatabase.Timeline.Timeline;
import com.i9930.croptrails.RoomDatabase.Timeline.TimelineCacheManager;
import com.i9930.croptrails.RoomDatabase.Timeline.TimelineNotifyInterface;
import com.i9930.croptrails.RoomDatabase.Visit.Visit;
import com.i9930.croptrails.RoomDatabase.Visit.VrCacheManager;
import com.i9930.croptrails.SubmitActivityForm.Model.CompAgriResponse;
import com.i9930.croptrails.SubmitActivityForm.Model.DoneActNew.DoneActivityResponseNew;
import com.i9930.croptrails.SubmitInputCost.Model.ExpenseDataDD;
import com.i9930.croptrails.SubmitInputCost.Model.ResourceDataDD;
import com.i9930.croptrails.SubmitPld.Model.PldReasonResponse;
import com.i9930.croptrails.SubmitVisitForm.Model.VisitResponseNew;
import com.i9930.croptrails.Test.model.full.AssetsData;
import com.i9930.croptrails.Test.model.full.BankDetails;
import com.i9930.croptrails.Test.model.full.DataCropDetails;
import com.i9930.croptrails.Test.model.full.FarmAddressDetails;
import com.i9930.croptrails.Test.model.full.FarmCropDetails;
import com.i9930.croptrails.Test.model.full.FarmFullDetails;
import com.i9930.croptrails.Test.model.full.FarmsDetails;
import com.i9930.croptrails.Test.model.full.PersonAddress;
import com.i9930.croptrails.Test.model.full.PersonDetails;
import com.i9930.croptrails.Test.model.full.PrevCropDatum;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.AppFeatures;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class OfflineModeActivity extends AppCompatActivity implements FarmNotifyInterface, TimelineNotifyInterface {
    private static final int PERMISSION_REQUEST_CODE = 1;
    String TAG = "OfflineModeActivity";
    TimelineNotifyInterface timelineNotifyInterface;
    //    ProgressDialog progressDoalog;
    Resources resources;
    int totalActivityZize;
    LinearLayoutManager linearLayoutManager;
    OfflineFarmsAdapter offlineFarmsAdapter;
    @BindView(R.id.recycler_make_offline_list)
    RecyclerView recycler_make_offline_list;
    @BindView(R.id.make_all_farm_offline_button)
    TextView make_all_farm_offline_button;
    Context context;
    List<FetchFarmResult> fetchFarmResultList;
    FetchFarmData fetchFarmData;
    int imgCount = 0;
    List<Farm> farmList = new ArrayList<>();
    List<String> farmIdList = new ArrayList<>();
    @BindView(R.id.prog_bar_offline)
    ProgressBar prog_bar_offline;
    @BindView(R.id.no_data_available)
    RelativeLayout no_data_available;
    @BindView(R.id.no_internet_layout)
    RelativeLayout no_internet_layout;
    @BindView(R.id.offline_mode_parent_rel_lay)
    RelativeLayout offline_mode_parent_rel_lay;
    Toolbar mActionBarToolbar;
    int totalVisitSize = 0;
    List<ExpenseDataDD> expenseDataDDList = new ArrayList<>();
    List<ResourceDataDD> resourceDataDDList = new ArrayList<>();
    List<IrrigationSource> irrigationSourceList = new ArrayList<>();
    List<IrrigationType> irrigationTypeList = new ArrayList<>();
    List<SoilType> soilTypeList = new ArrayList<>();
    List<FarmCrop> cropList = new ArrayList<>();

    List<Season> seasonList = new ArrayList<>();
    private boolean isPldReasonLoaded = false;
    @BindView(R.id.loader)
    GifImageView loader;
    @BindView(R.id.connectivityLine)
    View connectivityLine;
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
                            ConnectivityUtils.checkSpeedWithColor(OfflineModeActivity.this, new ConnectivityUtils.ColorListener() {
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
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_offline_mode);

        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        ButterKnife.bind(this);
        context = this;
        timelineNotifyInterface = this;
        Log.e("Data", "Data fetched");

        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.offline_mode_title));
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mActionBarToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                }
        );

        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompAgriInputs();
                }
            }
        }, AppConstants.CHEMICAL_UNIT.COMP_AGRI_INPUTS);

        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getAllDataNew(AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE_NEW);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE_NEW);
        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getAllDataNew(AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE_NEW);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE_NEW);
        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getAllDataNew2(AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE_NEW);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE_NEW);
        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getAllDataNew3(AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE_NEW);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE_NEW);
        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getAllDataNew4(AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW);
        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getAllDataNew5(AppConstants.CHEMICAL_UNIT.CROPPING_PATTERN);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.CROPPING_PATTERN);
        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getAllDataNew6(AppConstants.CHEMICAL_UNIT.PLANTING_METHOD);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.PLANTING_METHOD);
        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getAllDataNew7(AppConstants.CHEMICAL_UNIT.LAND_CATEGORY);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.LAND_CATEGORY);
        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getAllDataNew8(AppConstants.CHEMICAL_UNIT.CIVIL_STATUS);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.CIVIL_STATUS);

        DropDownCacheManager.getInstance(context).getDataByType(new DropDownCacheManager.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getFormData();
                }else {
                    try {
                        CropFormResponse data = new Gson().fromJson(dropDown.getData(), CropFormResponse.class);
                        if (data.getData() != null) {
                            OfflineModeActivity.this.cropFormDatumList.addAll(data.getData());
                            for (CropFormDatum cropFormDatum : data.getData()) {
                                OfflineModeActivity.this.cropFormDatumListMap.put(cropFormDatum.getInfoTypeId(), cropFormDatum);
                            }
                        }

                        if (data.getSuperData() != null) {
                            OfflineModeActivity.this.cropFormDatumListSuper.addAll(data.getSuperData());
                            for (CropFormDatum cropFormDatum : data.getSuperData()) {
                                if (cropFormDatum.getType().trim().equals("Equipment / Machinery"))
                                    OfflineModeActivity.this.cropFormDatumListSuperMap.put("E", cropFormDatum);
                                else
                                    OfflineModeActivity.this.cropFormDatumListSuperMap.put("A", cropFormDatum);
                            }
                        }

                        if (cropFormDatumList != null && cropFormDatumListSuper != null) {
                            if (cropFormDatumListSuper.size() == 0 && cropFormDatumList.size() == 0) {
                                getFormData();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        getFormData();
                    }
                }
            }
        }, AppConstants.CHEMICAL_UNIT.FARM_ADD_DYNAMIC);



        DropDownCacheManager.getInstance(context).getDataByType(new DropDownCacheManager.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("[]")) {
                    getPldReasons();
                }
            }
        }, AppConstants.CHEMICAL_UNIT.PLD_REASON);


        /*final String []where={AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE_NEW,AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE_NEW,
                AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE_NEW,AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW,
                AppConstants.CHEMICAL_UNIT.CROPPING_PATTERN,AppConstants.CHEMICAL_UNIT.PLANTING_METHOD,
                AppConstants.CHEMICAL_UNIT.LAND_CATEGORY,AppConstants.CHEMICAL_UNIT.CIVIL_STATUS};
        for (int i=0;i<where.length;i++){
            getAllDataNew(where[i]);
        }*/
        initializeBottomsheet();
        if (isConnected()) {
//            progressDoalog.show();
            loader.setVisibility(View.VISIBLE);
            if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.ADMIN) ||
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPER_ADMIN) ||
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUB_ADMIN)) {
                getClusterOfCompany();
            }
            getFarmerList();

            make_all_farm_offline_button.setVisibility(View.GONE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute();
                    make_all_farm_offline_button.setVisibility(View.VISIBLE);

                }
            }, 6000);


        } else {
            final Snackbar snackbar = Snackbar.make(offline_mode_parent_rel_lay, resources.getString(R.string.internet_not_connected), Snackbar.LENGTH_INDEFINITE);
            View view = snackbar.getView();
            view.setBackgroundColor(getResources().getColor(R.color.main_theme));
            TextView tv = (TextView) view.findViewById(R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
            snackbar.setAction(resources.getString(R.string.snackbar_ok), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
        make_all_farm_offline_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fetchFarmResultList != null) {
//                    progressDoalog.setMessage(resources.getString(R.string.preparing_your_data_for_offline_mode));
//                    progressDoalog.setTitle(resources.getString(R.string.stay_connected));
                    //insertRoomFarmsNew(fetchFarmResultList);
                    loader.setVisibility(View.VISIBLE);
                    make_all_farm_offline_button.setEnabled(false);
                    make_all_farm_offline_button.setClickable(false);

                    getFarmIdList();

                }

            }
        });
    }

    List<PldReason> pldReasons = new ArrayList<>();

    protected void getPldReasons() {
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getPldReasonList(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID)).enqueue(new Callback<PldReasonResponse>() {
            @Override
            public void onResponse(Call<PldReasonResponse> call, Response<PldReasonResponse> response) {
                String error = null;
                isLoaded[0] = true;
                Log.e(TAG, "Pld Reason Code " + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG, "Pld Reason res " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 10) {
                        isPldReasonLoaded = false;
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this);
                    } else if (response.body().getStatus() == 1 && response.body().getPldReasonList() != null && response.body().getPldReasonList().size() > 0) {

                        PldReason pldReason = new PldReason();
                        pldReason.setName("Select Loss/Damage Reason");
                        pldReasons.add(pldReason);
                        pldReasons.addAll(response.body().getPldReasonList());

                        DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.PLD_REASON);
                        DropDownT dropDownPld = new DropDownT(AppConstants.CHEMICAL_UNIT.PLD_REASON, new Gson().toJson(pldReasons));
                        DropDownCacheManager.getInstance(context).addChemicalUnit(dropDownPld);

                        isPldReasonLoaded = true;
                    } else {
                        isPldReasonLoaded = false;
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        isPldReasonLoaded = false;
                        error = response.errorBody().string();
                        Log.e(TAG, "Reason Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(OfflineModeActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<PldReasonResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                isLoaded[0] = true;
                long diff = newMillis - currMillis;
                notifyApiDelay(OfflineModeActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "Reason fail " + t.toString());
                isPldReasonLoaded = false;
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                internetFlow(isLoaded[0]);
            }
        }, AppConstants.DELAY);
        /*progressBar_cyclic.setVisibility(View.INVISIBLE);
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            onlineModeEvent();
        } else {
            offlineModeEvent();
        }

        pld_area_in_acre.setEnabled(false);
        pld_reason_for_damage.setEnabled(false);
        submit_pld_area.setEnabled(false);*/

    }


    private boolean isConnected() {
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
        return connected;
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        Intent intent = new Intent(context, LandingActivity.class);
        ActivityOptions options = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options.toBundle());
            finish();
        } else {
            startActivity(intent);
            finish();
        }
    }

    private void getFarmIdList() {
        List<String> farmIdList = new ArrayList<>();
        for (int i = 0; i < fetchFarmResultList.size(); i++) {
            if (fetchFarmResultList.get(i).isSelected()) {
                farmIdList.add(fetchFarmResultList.get(i).getFarmId());
                Log.e("OfflineModeAct", "Farm Selected List " + new Gson().toJson(fetchFarmResultList.get(i).getName()));
            } else {
                Log.e("OfflineModeAct", "Farm Not Selected List " + new Gson().toJson(fetchFarmResultList.get(i).getName()));
            }
        }


        getFarmCoordinates(farmIdList);
//        getAllCalActData(farmIdList);

    }


    private void getFarmCoordinates(final List<String> farmIdList) {
        coordinateHashMap.clear();
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        FarmIdArray idArray = new FarmIdArray();
        idArray.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        idArray.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        idArray.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        idArray.setFarmIdList(farmIdList);
        Log.e(TAG, "SENDING FARM IDS " + new Gson().toJson(idArray));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getAllCoordinatesOfFarms(idArray).enqueue(new Callback<FarmsCoordinatesResponse>() {
            @Override
            public void onResponse(Call<FarmsCoordinatesResponse> call, Response<FarmsCoordinatesResponse> response) {
                String error = null;
                isLoaded[0] = true;
                Log.e(TAG, "getAllCoordinatesOfFarms " + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG, "getAllCoordinatesOfFarms res " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 1) {
                        if (response.body().getCoordinatesList() != null && response.body().getCoordinatesList().size() > 0) {
                            for (int i = 0; i < response.body().getCoordinatesList().size(); i++) {
                                Log.e(TAG, "FarmOfflineModeActivity " + response.body().getCoordinatesList().get(i).getFarmId() + " Data " + new Gson().toJson(response.body().getCoordinatesList().get(i).getData()));
                                if (response.body().getCoordinatesList().get(i).getData() != null && response.body().getCoordinatesList().get(i).getData().size() > 0) {

                                    coordinateHashMap.put(response.body().getCoordinatesList().get(i).getFarmId(), response.body().getCoordinatesList().get(i).getData());
                                }

                            }
                            getAllCalActData(farmIdList);
                        } else {
                            getAllCalActData(farmIdList);
                        }

                    } else {
                        getAllCalActData(farmIdList);
                    }

                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    prog_bar_offline.setVisibility(View.INVISIBLE);
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "Error getAllCoordinatesOfFarms" + error);
                        make_all_farm_offline_button.setEnabled(true);
                        make_all_farm_offline_button.setClickable(true);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialogForFinish(OfflineModeActivity.this, "Failed to load data for offline mode, Please try again later");
                        getAllCalActData(farmIdList);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(OfflineModeActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<FarmsCoordinatesResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                isLoaded[0] = true;
                notifyApiDelay(OfflineModeActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                getAllCalActData(farmIdList);
                prog_bar_offline.setVisibility(View.INVISIBLE);
                make_all_farm_offline_button.setEnabled(true);
                make_all_farm_offline_button.setClickable(true);
                Log.e(TAG, "getAllCoordinatesOfFarms fail " + t.toString());
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialogForFinish(OfflineModeActivity.this, "Failed to load data for offline mode, Please try again later");
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

    private void getAllCalActData(List<String> farmIdList) {
        VrCacheManager.getInstance(context).deleteAllVisits();
        TimelineCacheManager.getInstance(context).deleteAllTimeline();
        ResourceCacheManager.getInstance(context).deleteAllResources();
        InputCostCacheManager.getInstance(context).deleteAllInputCost();
        HarvestCacheManager.getInstance(context).deleteAllHarvests();
        FarmCacheManager.getInstance(context).deleteAllFarms();
//        DropDownCacheManager.getInstance(context).deleteAllChemicalUnit();


//        getAllDataNew(AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE_NEW);
//        getAllDataNew(AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE_NEW);
//        getAllDataNew(AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE_NEW);
//        getAllDataNew(AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW);
//        getAllDataNew(AppConstants.CHEMICAL_UNIT.CIVIL_STATUS);
//        getAllDataNew(AppConstants.CHEMICAL_UNIT.CROPPING_PATTERN);
//        getAllDataNew(AppConstants.CHEMICAL_UNIT.PLANTING_METHOD);
//        getAllDataNew(AppConstants.CHEMICAL_UNIT.LAND_CATEGORY);

        ActivityCacheManager.getInstance(context).deleteAllHarvests();
        boolean check = true;
        if (farmIdList.size() == 0) {
//            loader.setVisibility(View.GONE);
//            prog_bar_offline.setVisibility(View.GONE);
//            make_all_farm_offline_button.setEnabled(true);
//            make_all_farm_offline_button.setClickable(true);
//            Toasty.error(context, resources.getString(R.string.please_select_at_least_1_farm_to_go_offline), Toast.LENGTH_LONG, false).show();
            getAllData(farmIdList);
        } else if (farmIdList.size() > 10) {
            make_all_farm_offline_button.setEnabled(true);
            make_all_farm_offline_button.setClickable(true);
            loader.setVisibility(View.GONE);
            prog_bar_offline.setVisibility(View.GONE);
            Toasty.error(context, resources.getString(R.string.you_can_only_take_10_farms_in_offline_mode_at_a_time), Toast.LENGTH_LONG, false).show();
        } else {
//            prog_bar_offline.setVisibility(View.VISIBLE);

            FarmIdArray idArray = new FarmIdArray();
            idArray.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
            idArray.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            idArray.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            idArray.setFarmIdList(farmIdList);
            Log.e(TAG, "Farm Id List " + new Gson().toJson(idArray));
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            final boolean[] isLoaded = {false};
            long currMillis = AppConstants.getCurrentMills();
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            apiInterface.getDoneCalActivities(idArray).enqueue(new Callback<CalendarActResponse>() {
                @Override
                public void onResponse(Call<CalendarActResponse> call, Response<CalendarActResponse> response) {
                    String error = null;
                    isLoaded[0] = true;
                    Log.e(TAG, "Calendar Activities code " + new Gson().toJson(response.code()));

                    if (response.isSuccessful()) {
                       /* if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, response.body().getMsg());
                        } else if (response.body().getStatus() == 1) {*/
                        Log.e(TAG, "Calendar Activities Res " + new Gson().toJson(response.body()));
                        List<DoneActivityResponseNew> doneActivityList = response.body().getDoneActivityList();
                        if (doneActivityList != null && doneActivityList.size() > 0) {
                            totalActivityZize = doneActivityList.size() - 1;
                            for (int i = 0; i < doneActivityList.size(); i++) {
                                DoneActivityResponseNew doneAct = doneActivityList.get(i);
                                DownloadImagesActivity downloadImagesActivity = new DownloadImagesActivity(doneAct, i, farmIdList);
                                downloadImagesActivity.execute();
                            }
                        } else {
                            getAllData(farmIdList);
                        }

                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        prog_bar_offline.setVisibility(View.INVISIBLE);
                        try {
                            getAllData(farmIdList);
                            error = response.errorBody().string().toString();
                            Log.e(TAG, "Calendar Activities Error  " + error);
                            make_all_farm_offline_button.setEnabled(true);
                            make_all_farm_offline_button.setClickable(true);
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialogForFinish(OfflineModeActivity.this, "Failed to load data for offline mode, Please try again later");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                        notifyApiDelay(OfflineModeActivity.this, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }
                }

                @Override
                public void onFailure(Call<CalendarActResponse> call, Throwable t) {
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    isLoaded[0] = true;
                    notifyApiDelay(OfflineModeActivity.this, call.request().url().toString(),
                            "" + diff, internetSPeed, t.toString(), 0);
                    Log.e(TAG, "Calendar Activities Failure " + t.toString());
                    prog_bar_offline.setVisibility(View.INVISIBLE);
                    make_all_farm_offline_button.setEnabled(true);
                    make_all_farm_offline_button.setClickable(true);
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showDialogForFinish(OfflineModeActivity.this, "Failed to load data for offline mode, Please try again later");
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

    private void getAllData(List<String> farmIdList) {
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        FarmIdArray idArray = new FarmIdArray();
        idArray.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        idArray.setFarmIdList(farmIdList);
        idArray.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        idArray.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        Log.e(TAG, "getOfflineData Sending Data " + new Gson().toJson(idArray));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getOfflineData(idArray).enqueue(new Callback<OfflineResponse>() {
            @Override
            public void onResponse(Call<OfflineResponse> call, Response<OfflineResponse> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    OfflineResponse response = response1.body();
                    Log.e(TAG, "getOfflineData Response Make " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, response.getMsg());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        //Insert Chemicals and units

                        if (response.getUnitsList() != null && response.getUnitsList().size() > 0) {
                            DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.UNIT);
                            DropDownT units = new DropDownT(AppConstants.CHEMICAL_UNIT.UNIT, new Gson().toJson(response.getUnitsList()));
                            DropDownCacheManager.getInstance(context).addChemicalUnit(units);
                        }

                        if (response.getTimelineUnitsList() != null && response.getTimelineUnitsList().size() > 0) {
                            DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.COMPANY_UNITS);
                            DropDownT compUnits = new DropDownT(AppConstants.CHEMICAL_UNIT.COMPANY_UNITS, new Gson().toJson(response.getTimelineUnitsList()));
                            DropDownCacheManager.getInstance(context).addChemicalUnit(compUnits);
                        }

                        if (response.getTimelineChemicalsList() != null && response.getTimelineChemicalsList().size() > 0) {
                            DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.COMPANY_CHEMICALS);
                            DropDownT compChemicals = new DropDownT(AppConstants.CHEMICAL_UNIT.COMPANY_CHEMICALS, new Gson().toJson(response.getTimelineChemicalsList()));
                            DropDownCacheManager.getInstance(context).addChemicalUnit(compChemicals);
                        }
                        if (response.getInputCostAndResourceDataList() != null && response.getInputCostAndResourceDataList().size() > 0) {
                            for (int i = 0; i < response.getInputCostAndResourceDataList().size(); i++) {
                                String inputData = "[]";
                                String resourceData = "[]";
                                String inputDataOffline = "[]";
                                String resourceDataOffline = "[]";
                                String farmId;

                                InputCostAndResourceData data = response.getInputCostAndResourceDataList().get(i);
                                if (data != null) {
                                    farmId = data.getFarmId();
                                    inputData = new Gson().toJson(data.getInputCostDataList());
                                    resourceData = new Gson().toJson(data.getResourceDataList());
                                    InputCostT costT = new InputCostT(farmId, inputData, inputDataOffline, "Y");
                                    InputCostCacheManager.getInstance(context).addInputCost(costT);
                                    ResourceCostT resourceCostT = new ResourceCostT(farmId, resourceData, resourceDataOffline, "Y");
                                    ResourceCacheManager.getInstance(context).addResource(resourceCostT);
                                }


                            }
                        } else {
                            String inputData = "[]";
                            String resourceData = "[]";
                            String inputDataOffline = "[]";
                            String resourceDataOffline = "[]";
                            String farmId;
                            for (int i = 0; i < farmIdList.size(); i++) {
                                farmId = farmIdList.get(i);
                                InputCostT costT = new InputCostT(farmId, inputData, inputDataOffline, "Y");
                                InputCostCacheManager.getInstance(context).addInputCost(costT);
                                ResourceCostT resourceCostT = new ResourceCostT(farmId, resourceData, resourceDataOffline, "Y");
                                ResourceCacheManager.getInstance(context).addResource(resourceCostT);
                            }
                        }

                        //Inserting in farm table
                        insertRoomFarmsNew(fetchFarmResultList);

                        //Inserting in harvest table
                        if (response.getHarvestOfflineList() != null) {
                            for (int i = 0; i < response.getHarvestOfflineList().size(); i++) {
                                ViewHarvestDetails harvest = new ViewHarvestDetails();
                                harvest.setData(response.getHarvestOfflineList().get(i).getHarvestDetails());
                                harvest.setData1(response.getHarvestOfflineList().get(i).getHarvestMaster());
                                HarvestT harvestT = new HarvestT(response.getHarvestOfflineList().get(i).getFarmId(),
                                        new Gson().toJson(harvest), "{}", "Y");
                                HarvestCacheManager.getInstance(context).addVisits(harvestT);
                            }
                        }

                        //inserting in visit table
                        if (response.getVisitMasterList() != null) {
                            totalVisitSize = response.getVisitMasterList().size() - 1;
                            if (response.getVisitMasterList().size() > 0) {
                                Log.e(TAG, "Offline Visit List Size " + response.getVisitMasterList().size());
                                for (int i = 0; i < response.getVisitMasterList().size(); i++) {
                                    Log.e(TAG, "Visit Report " + i + " " + new Gson().toJson(response.getVisitMasterList().get(i)));
                                    VisitResponse visit = response.getVisitMasterList().get(i);
                                    if (visit.getVisitResponseDatumList() != null && visit.getVisitResponseDatumList().size() > 0) {
                                        List<VisitResponseDatum> visitResponseDatumList = visit.getVisitResponseDatumList();
                                        for (int j = 0; j < visitResponseDatumList.size(); j++) {
                                            VisitResponseDatum visitResponseDatum = visitResponseDatumList.get(j);
                                            VisitResponseNew visitResponseNew = new VisitResponseNew();
                                            DoneActivityResponseNew doneActivityResponseNew = new DoneActivityResponseNew();
                                            doneActivityResponseNew.setImages(visitResponseDatum.getImages());
                                            doneActivityResponseNew.setComment(visitResponseDatum.getComment());
                                            doneActivityResponseNew.setMoisture(visitResponseDatum.getMoisture());
                                            doneActivityResponseNew.setGrade(visitResponseDatum.getGrade());
                                            doneActivityResponseNew.setData(visitResponseDatum.getAgriInputs());
                                            doneActivityResponseNew.setFarmId(visit.getFarmId());
                                            visitResponseNew.setShowResponse(doneActivityResponseNew);
                                            visitResponseNew.setMoisture(visitResponseDatum.getMoisture());
                                            visitResponseNew.setFarmGrade(visitResponseDatum.getGrade());
                                            visitResponseNew.setComment(visitResponseDatum.getComment());
                                            visitResponseNew.setFarmId(visit.getFarmId());
                                            visitResponseNew.setVisitReportId(""+visitResponseDatum.getVisitReportId());
                                            visitResponseNew.setEffectiveArea(""+visitResponseDatum.getEffectiveArea());
                                            visitResponseNew.setVisitNumber(""+visitResponseDatum.getVisitNumber());
                                            DownloadImagesVisit downloadImagesNew = new DownloadImagesVisit(visitResponseNew, i);
                                            downloadImagesNew.execute();
                                        }
                                    }
                                }
                            } else {
                                prog_bar_offline.setVisibility(View.INVISIBLE);
                               setDeviceId();
                            }

                        } else {
                            prog_bar_offline.setVisibility(View.INVISIBLE);
                            setDeviceId();
                        }
                    }
                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    prog_bar_offline.setVisibility(View.INVISIBLE);
                    try {
                        error = response1.errorBody().string().toString();
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(OfflineModeActivity.this, "Failed to load data for offline mode, Please try again later");
                        Log.e(TAG, "getOfflineData Error " + error);
                        make_all_farm_offline_button.setEnabled(true);
                        make_all_farm_offline_button.setClickable(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response1.code() == 500) {
                    notifyApiDelay(OfflineModeActivity.this, response1.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response1.code());
                }
            }

            @Override
            public void onFailure(Call<OfflineResponse> call, Throwable t) {
                Log.e(TAG, "getOfflineData Failure " + t.toString());
                prog_bar_offline.setVisibility(View.INVISIBLE);
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(OfflineModeActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                make_all_farm_offline_button.setEnabled(true);
                make_all_farm_offline_button.setClickable(true);
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialog(OfflineModeActivity.this, "Failed to load data for offline mode, Please try again later");
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

    private class FulDetailsAsync extends AsyncTask<String, Integer, String> {
        String farmId;
        String coords;
        String germiData;
        String pldOnlineJson;
        FetchFarmResult result;

        public FulDetailsAsync(String farmId, String coords, String germiData, String pldOnlineJson, FetchFarmResult result) {
            this.farmId = farmId;
            this.result = result;
            this.coords = coords;
            this.germiData = germiData;
            this.pldOnlineJson = pldOnlineJson;
        }

        @Override
        protected String doInBackground(String... strings) {

            getFullDetails(farmId, coords, germiData, pldOnlineJson, result);

            return null;
        }
    }

    private void getFullDetails(String farmId, String coords, String germiData, String pldOnlineJson, FetchFarmResult result) {
        JsonObject object = new JsonObject();
        object.addProperty("farm_id", "" + farmId);
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
                        try {
                            String farmerId = details.getPersonDetails().getPersonId() + "";

                            //Farmer details
                            FarmerAndFarmData farmerAndFarmData = new FarmerAndFarmData();
                            FarmerCacheManager.getInstance(context).getFarmer(new FarmerCacheManager.GetFarmerListener() {
                                @Override
                                public void onFarmerLoaded(FarmerT farmerT) {
                                    if (farmerT == null || !AppConstants.isValidString(farmerT.getFarmerId())) {

                                        if (details.getPersonDetails() != null) {
                                            PersonDetails personDetails = details.getPersonDetails();
                                            farmerAndFarmData.setLiteracy(personDetails.getLiteracyStatus());
                                            farmerAndFarmData.setFinancial_status(personDetails.getFinancialStatus());
                                            farmerAndFarmData.setIdProof(personDetails.getIdProof());
                                            farmerAndFarmData.setAddressProof(personDetails.getAddressProof());
                                            farmerAndFarmData.setPersonId(personDetails.getPersonId() + "");
                                            farmerAndFarmData.setCompId(personDetails.getCompId() + "");
                                            farmerAndFarmData.setClusterId(personDetails.getClusterId() + "");
//                            farmerAndFarmData.setLiteracy(personDetails.getRoleId()+"");
                                            farmerAndFarmData.setName(personDetails.getName());
                                            farmerAndFarmData.setFather_name(personDetails.getFatherName());
//                            farmerAndFarmData.setLiteracy(personDetails.getDesignation());
                                            farmerAndFarmData.setCountryCode(personDetails.getCountryCode());
                                            farmerAndFarmData.setMob(personDetails.getMob());
                                            farmerAndFarmData.setEmail(personDetails.getEmail());
//                            farmerAndFarmData.setLiteracy(personDetails.getSignature());
                                            farmerAndFarmData.setDob(personDetails.getDob());
                                            farmerAndFarmData.setGeder(personDetails.getGender());
                                            farmerAndFarmData.setIsSmartPhone(personDetails.getIsUsingSphone());
                                            farmerAndFarmData.setIsShareHolder(personDetails.getIsShareholder());
                                            try {

                                                if (AppConstants.isValidString(personDetails.getCattleJson())) {
                                                    CowAndCatles cowAndCatles = new Gson().fromJson(personDetails.getCattleJson().trim(), CowAndCatles.class);
                                                    farmerAndFarmData.setCowAndCatles(cowAndCatles);
                                                }
                                            } catch (Exception e) {

                                            }

                                            farmerAndFarmData.setAnualIncome(personDetails.getEstIncome());
                                            farmerAndFarmData.setImage(personDetails.getImgLink());
                                            farmerAndFarmData.setPan(personDetails.getPan());
                                            farmerAndFarmData.setAadhaar(personDetails.getAadhaar());
                                            farmerAndFarmData.setSpouse_name(personDetails.getSpouseName());
                                            farmerAndFarmData.setSpouseDob(personDetails.getSpouseDob());
                                            farmerAndFarmData.setBeneficiary_name(personDetails.getBeneficiaryName());
                                            farmerAndFarmData.setCivil_status(personDetails.getCivilStatus());
                                            farmerAndFarmData.setCasteCategoryId(personDetails.getCasteCategory());
                                            farmerAndFarmData.setCasteId(personDetails.getCaste());
                                            farmerAndFarmData.setReligionId(personDetails.getReligion());
                                            farmerAndFarmData.setFamily_mem_count(personDetails.getFamilyMemCount() + "");
                                            farmerAndFarmData.setAdults_count(personDetails.getAdultsCount() + "");
                                            farmerAndFarmData.setKids_count(personDetails.getKidsCount() + "");
                                            farmerAndFarmData.setDependent_count(personDetails.getDependentCount() + "");
                                            farmerAndFarmData.setMobileNetworkId(personDetails.getMobileOperator());
                                            farmerAndFarmData.setMiaId(personDetails.getMia());
                                            farmerAndFarmData.setLiteracy(personDetails.getLiteracyStatus());
                                        }

                                        if (details.getPersonAddress() != null) {
                                            PersonAddress personAddress = details.getPersonAddress();
                                            farmerAndFarmData.setAddL1(personAddress.getAddL1());
                                            farmerAndFarmData.setAddL2(personAddress.getAddL2());
                                            farmerAndFarmData.setVillage_or_city(personAddress.getVillageOrCity());
                                            farmerAndFarmData.setMandal_or_tehsil(personAddress.getMandalOrTehsil());
                                            farmerAndFarmData.setDistrict(personAddress.getDistrict());
                                            farmerAndFarmData.setState(personAddress.getState());
                                            farmerAndFarmData.setCountry(personAddress.getCountry());
//                            farmerAndFarmData.setLiteracy(personAddress.getLiteracyStatus());
//                            farmerAndFarmData.setLiteracy(personAddress.getLiteracyStatus());
                                        }
                                        if (details.getBankDetails() != null) {
                                            BankDetails bankDetails = details.getBankDetails();

                                            farmerAndFarmData.setBankName(bankDetails.getBankName());
                                            farmerAndFarmData.setHolderName(bankDetails.getAccountName());
                                            farmerAndFarmData.setAccountNumber(bankDetails.getAccountNo());
                                            farmerAndFarmData.setBranch(bankDetails.getBranch());
                                            farmerAndFarmData.setIfscCode(bankDetails.getIfsc());
                                            farmerAndFarmData.setSwiftCode(bankDetails.getSwiftCode());
                                            farmerAndFarmData.setUpi_id(bankDetails.getUpiId());

                                        }
                                        String id = "" + Calendar.getInstance().getTime().getTime();
                                        FarmerT farmerTNew = new FarmerT(farmerId, new Gson().toJson(farmerAndFarmData), "N", "N", "N");
                                        FarmerCacheManager.getInstance(context).addFarmer(new FarmerCacheManager.FarmerInsertListener() {
                                            @Override
                                            public void onFarmerInserted(long farmerId) {

                                            }
                                        }, farmerTNew);
                                    }
                                }
                            }, farmerId);
                        } catch (Exception e) {

                        }

                        //======================XXX==============================
                        //FarmDetails
//                        Farm farm=new Farm();
                        String svId=null;
                        FarmData farmData = new FarmData();
                        if (details.getFarmsDetails() != null) {
                            FarmsDetails farmDetails = details.getFarmsDetails();
                            farmData.setFarmImagePath(farmDetails.getFarmPhoto());
                            farmData.setOwnerShipDoc(farmDetails.getOwnershipDoc());
                            farmData.setIsOwned(farmDetails.getIsOwned());
//                                farmData.setid(farmDetails.getId()+"");
                            farmData.setSvId(svId);
                            farmData.setFarmId(farmDetails.getFarmMasterNum() + "");
                            farmData.setName(farmDetails.getFarmName());
                            farmData.setId(farmDetails.getCompFarmId());
                            farmData.setOwnerId(farmDetails.getOwnerId() + "");
//                                farmData.seto(farmDetails.getOperatorId()+"");
                            farmData.setCluster(farmDetails.getClusterId() + "");
                            farmData.setCompId(farmDetails.getCompId() + "");
                            farmData.setSeason(farmDetails.getSeasonNum() + "");
                            farmData.setIsOwned(farmDetails.getIsOwned());
                            farmData.setZoom(farmDetails.getZoom() + "");
                            farmData.setIsSelected(farmDetails.getIsSelected());
                            farmData.setDelqReason(farmDetails.getDelqReason());
                            farmData.setMotorHp(farmDetails.getMotorHp());
                            farmData.setIsFree(""+farmDetails.getIsFree());
                            farmData.setAddedBy(""+farmDetails.getAddedBy());
                            farmData.setIsSelected(farmDetails.getIsSelected());
                            farmData.setDoa(farmDetails.getDoa());
                            farmData.setDod(farmDetails.getDod());
//                                farmData.setIsOwned(farmDetails.getIsOwned());
//                                farmData.setIsOwned(farmDetails.getIsOwned());
                        }

                        if (details.getFarmCropDetails() != null) {
                            FarmCropDetails cropDetails = details.getFarmCropDetails();
                            farmData.setCurrentCropName(cropDetails.getCropName());
                            farmData.setPreviouscrop(cropDetails.getPreviousCrop());
                            farmData.setSeason(cropDetails.getSeasonNum() + "");
                            farmData.setCropId(cropDetails.getCropId() + "");
                            farmData.setArea(cropDetails.getExpArea() + "");
                            farmData.setAreaS(cropDetails.getExpArea() + "");
                            farmData.setStandingArea(cropDetails.getStandingArea() + "");
                            farmData.setActualArea(cropDetails.getActualArea() + "");
                            farmData.setIs_irrigated(cropDetails.getIsIrrigated() + "");
                            farmData.setIrrigationsource(cropDetails.getIrrigationSourceId() + "");
                            farmData.setIrrigationtype(cropDetails.getIrrigationTypeId() + "");
                            farmData.setSoiltype(cropDetails.getSoilTypeId() + "");
                            farmData.setPlanting_method(cropDetails.getPlantingMethod());
                            farmData.setExpFloweringDate(cropDetails.getExpFloweringDate());
                            farmData.setExpTransplantDate(cropDetails.getExpTransplantDate());
                            farmData.setExpSowingDate(cropDetails.getExpSowingDate());
                            farmData.setExpHarvestDate(cropDetails.getExpHarvestDate());
                            farmData.setTransplant_date(cropDetails.getTransplantDate());
                            farmData.setSowingDate(cropDetails.getSowingDate());
                            farmData.setActualHarvestDate(cropDetails.getActualHarvestDate());
                            farmData.setActualFloweringDate(cropDetails.getActualFloweringDate());
                            farmData.setAreaHarvested(cropDetails.getAreaHarvested());




                            farmData.setSeed_lot_no(cropDetails.getSeedLotNo());
                            farmData.setSeedProvidedOn(cropDetails.getSeedProvidedOn());
                            farmData.setSeed_unit_id(""+cropDetails.getSeedUnitId());
                            farmData.setQtySeedProvided(cropDetails.getQtySeedProvided());
                            farmData.setGermination(cropDetails.getGermination());
                            farmData.setPopulation(cropDetails.getPopulation());
                            farmData.setSpacing_ptp(cropDetails.getSpacingPtp());
                            farmData.setSpacing_rtr(cropDetails.getSpacingRtr());

                            farmData.setPld_acres(cropDetails.getPldAcres());
                            farmData.setPld_reason(cropDetails.getPldReason());
                            farmData.setPld_receipt_no(cropDetails.getPldReceiptNo());
                            farmData.setReceived_qty(cropDetails.getReceivedQty());
                            farmData.setOutward_no(cropDetails.getOutwardNo());
                            farmData.setLoad_no(cropDetails.getLoadNo());
                            farmData.setGrade(cropDetails.getGrade());
                            farmData.setAgreed_rate(cropDetails.getAgreedRate());
                            farmData.setPayment_mode(cropDetails.getPaymentMode());
                        }

                        if (details.getFarmAddressDetails() != null) {
                            FarmAddressDetails addressDetails = details.getFarmAddressDetails();
                            farmData.setCountry(addressDetails.getCountry());
                            farmData.setState(addressDetails.getState());
                            farmData.setDistrict(addressDetails.getDistrict());
                            farmData.setVillageOrCity(addressDetails.getVillageOrCity());
                            farmData.setMandalOrTehsil(addressDetails.getMandalOrTehsil());
                            farmData.setAddL1(addressDetails.getAddL1());
                            farmData.setAddL2(addressDetails.getAddL2());
                        }
                        if (details.getAssetsDataList() != null && details.getAssetsDataList().size() > 0) {
                            farmData.setAssetsList(formatAsstesList(details.getAssetsDataList()));
                        }
                        if (details.getPrevCropDatumList() != null && details.getPrevCropDatumList().size() > 0) {
                            farmData.setFpoCropList(getCrops(details.getPrevCropDatumList()));
                        }

                        Farm farm = new Farm(result.getFarmId(), result.getLotNo(), result.getFarmerId(), result.getName(), result.getMob(),
                                result.getClusterId(), result.getCompId(), result.getAddL1(), result.getAddL2(), result.getVillageOrCity(),
                                result.getDistrict(), result.getMandalOrTehsil(), result.getState(), result.getCountry(), result.getExpArea(),
                                result.getActualArea(), result.getIrrigationSource(), result.getPreviousCrop(), result.getIrrigationType(),
                                result.getSoilType(), result.getSowingDate(), result.getExpFloweringDate(), result.getActualFloweringDate(),
                                result.getExpHarvestDate(), result.getActualHarvestDate(), "", result.getStandingAcres(),
                                result.getGermination(), result.getPopulation(), result.getSpacingPtp(), result.getPldAcres(),
                                result.getPldReason(), result.getLatCord(), result.getLongCord(),
                                result.getSpacingRtr(), "", result.getFatherName(), germiData, null, null, "N", "Y", pldOnlineJson, "[]",
                                result.getCropName(), "N", "[]", "", result.getFarmName(),
                                "Y", result.getSeasonId(), "N", coords, "", "",
                                "", "", svId, "");

                        String fullDetails = "{}";


//                        farmData.setAssetsList();
//                        farmData.setFpoCropList();

                        fullDetails = new Gson().toJson(farmData);
                        farm.setFarmFullData(fullDetails);
                        List<Farm> farmList = new ArrayList<>();
                        farmList.add(farm);
                        FarmCacheManager.getInstance(context).addFarms(OfflineModeActivity.this, farmList);
                    } catch (Exception e) {
                        e.printStackTrace();
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

    private List<List<CropFormDatum>> formatAsstesList(List<AssetsData> assetsDataList){

        if (assetsDataList != null && assetsDataList.size() > 0) {
            if (cropFormDatumListSuper == null || cropFormDatumListSuper.size() == 0) {
                List<List<CropFormDatum>> assetListForm = new ArrayList<>();
                Map<String, List<CropFormDatum>> stringListMap = new HashMap<>();
                Map<String, List<AssetsData>> assetsMap = new HashMap<>();
                for (int i = 0; i < assetsDataList.size(); i++) {
                    AssetsData data = assetsDataList.get(i);
                    if (assetsMap.containsKey(data.getAssetType())) {
                        assetsMap.get(data.getAssetType()).add(data);
                    } else {
                        List<AssetsData> list = new ArrayList<>();
                        list.add(data);
                        assetsMap.put(data.getAssetType(), list);
                    }
                }
                Log.e(TAG, "ASSETS " + new Gson().toJson(assetsMap));

                for (String keyss : assetsMap.keySet()) {

                    List<AssetsData> assetsDataListt = assetsMap.get(keyss);
                    List<CropFormDatum> cropFormDatumList = new ArrayList<>();
                    for (int i = 0; i < assetsDataListt.size(); i++) {
                        try {
                            AssetsData data = assetsDataListt.get(i);
                            Log.e(TAG, "PRINTING0 " + i + " " + new Gson().toJson(data));
                            CropFormDatum datum = null;
                            {
                                datum = new CropFormDatum();
                            }
                            Gson gson = new GsonBuilder()
                                    .setLenient()
                                    .create();
                            datum.setSuperType(data.getAssetType());
                            Log.e(TAG, "PRINTING1 " + i + " " + data.getJson());
                            JsonObject jsonObject = gson.fromJson(data.getJson(), JsonObject.class);
                            Log.e(TAG, "PRINTING " + i + " " + new Gson().toJson(jsonObject));
                            int s = 1;
                            for (String key : jsonObject.keySet()) {
                                if (s == 1) {
                                    datum.setCol6Value(jsonObject.get(key).getAsString());
                                    datum.setCol6(key);
                                } else if (s == 1) {
                                    datum.setCol1Value(jsonObject.get(key).getAsString());
                                    datum.setCol1(key);
                                } else if (s == 2) {
                                    datum.setCol2Value(jsonObject.get(key).getAsString());
                                    datum.setCol2(key);
                                } else if (s == 3) {
                                    datum.setCol3Value(jsonObject.get(key).getAsString());
                                    datum.setCol3(key);
                                } else if (s == 4) {
                                    datum.setCol4Value(jsonObject.get(key).getAsString());
                                    datum.setCol4(key);
                                } else if (s == 5) {
                                    datum.setCol5Value(jsonObject.get(key).getAsString());
                                    datum.setCol5(key);
                                }
                                s++;
                            }
                            cropFormDatumList.add(datum);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    assetListForm.add(cropFormDatumList);
                }
                return assetListForm;

            } else {
                List<List<CropFormDatum>> assetListForm = new ArrayList<>();
                Map<String, List<AssetsData>> assetsMap = new HashMap<>();
                for (int i = 0; i < assetsDataList.size(); i++) {
                    AssetsData data = assetsDataList.get(i);
                    if (assetsMap.containsKey(data.getAssetType())) {
                        assetsMap.get(data.getAssetType()).add(data);
                    } else {
                        List<AssetsData> list = new ArrayList<>();
                        list.add(data);
                        assetsMap.put(data.getAssetType(), list);
                    }
                }
                Log.e(TAG, "ASSETS " + new Gson().toJson(assetsMap));

                for (String keyss : assetsMap.keySet()) {

                    List<AssetsData> assetsDataListt = assetsMap.get(keyss);
                    List<CropFormDatum> cropFormDatumList = new ArrayList<>();
                    for (int i = 0; i < assetsDataListt.size(); i++) {
                        try {
                            AssetsData data = assetsDataListt.get(i);
                            Log.e(TAG, "PRINTING0 " + i + " " + new Gson().toJson(data));
                            CropFormDatum datum = null;
                            CropFormDatum oldDatum=null;
                            if (cropFormDatumListSuperMap.containsKey(data.getAssetType() + "".trim())) {
                                oldDatum = cropFormDatumListSuperMap.get(data.getAssetType());
                                if (oldDatum!=null){
                                    datum=new CropFormDatum();
                                    datum.setCol1(oldDatum.getCol1());
                                    datum.setCol2(oldDatum.getCol2());
                                    datum.setCol3(oldDatum.getCol3());
                                    datum.setCol4(oldDatum.getCol4());
                                    datum.setCol5(oldDatum.getCol5());
                                    datum.setCol6(oldDatum.getCol6());
                                    datum.setInfoTypeId(oldDatum.getInfoTypeId());
                                    datum.setSuperType(oldDatum.getSuperType());
                                    datum.setType(oldDatum.getType());
                                }
                            } else {
                                datum = new CropFormDatum();
                            }
                            Gson gson = new GsonBuilder()
                                    .setLenient()
                                    .create();
                            datum.setSuperType(data.getAssetType());
                            Log.e(TAG, "PRINTING1 " + i + " " + data.getJson());
                            JsonObject jsonObject = gson.fromJson(data.getJson(), JsonObject.class);
                            Log.e(TAG, "PRINTING " + i + " " + new Gson().toJson(jsonObject));
                            for (String key : jsonObject.keySet()) {
                                if (AppConstants.isValidString(datum.getCol6()) && datum.getCol6().equals(key)) {
                                    datum.setCol6Value(jsonObject.get(key).getAsString());
                                } else if (AppConstants.isValidString(datum.getCol1()) && datum.getCol1().equals(key)) {
                                    datum.setCol1Value(jsonObject.get(key).getAsString());

                                } else if (AppConstants.isValidString(datum.getCol2()) && datum.getCol2().equals(key)) {
                                    datum.setCol2Value(jsonObject.get(key).getAsString());

                                } else if (AppConstants.isValidString(datum.getCol3()) && datum.getCol3().equals(key)) {
                                    datum.setCol3Value(jsonObject.get(key).getAsString());

                                } else if (AppConstants.isValidString(datum.getCol4()) && datum.getCol4().equals(key)) {
                                    datum.setCol4Value(jsonObject.get(key).getAsString());

                                } else if (AppConstants.isValidString(datum.getCol5()) && datum.getCol5().equals(key)) {
                                    datum.setCol5Value(jsonObject.get(key).getAsString());

                                }

                            }
                            cropFormDatumList.add(datum);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    assetListForm.add(cropFormDatumList);
                }
                return assetListForm;

            }
        } else {
            return getListDataSuper();
        }
    }

    private List<FPOCrop>getCrops(List<PrevCropDatum> cropDetailsList){
        List<FPOCrop> fpoCropList = new ArrayList<>();
        if (cropDetailsList != null && cropDetailsList.size() > 0) {
            if (cropFormDatumList == null || cropFormDatumList.size() == 0) {
                try {
                    Map<PrevCropDatum, Map<Integer, List<DataCropDetails>>> map = new HashMap<>();
                    for (int i = 0; i < cropDetailsList.size(); i++) {
                        List<List<CropFormDatum>> cropDatumLists = new ArrayList<>();
                        PrevCropDatum details = cropDetailsList.get(i);
                        try {
                            Map<Integer, List<DataCropDetails>> mapInner = new HashMap<>();
                            for (int s = 0; s < details.getData().size(); s++) {
                                DataCropDetails dataCropDetails = details.getData().get(s);

                                if (mapInner.containsKey(dataCropDetails.getFarmAddInfoTypeId())) {
                                    mapInner.get(dataCropDetails.getFarmAddInfoTypeId()).add(dataCropDetails);
                                } else {
                                    List<DataCropDetails> dataCropDetailsList = new ArrayList<>();
                                    dataCropDetailsList.add(dataCropDetails);
                                    mapInner.put(dataCropDetails.getFarmAddInfoTypeId(), dataCropDetailsList);
                                }
                            }
                            map.put(details, mapInner);
                        } catch (Exception e) {
                        }
                    }
                    for (PrevCropDatum id : map.keySet()) {
                        List<List<CropFormDatum>> cropDatumLists = new ArrayList<>();
                        Map<Integer, List<DataCropDetails>> detailsList = map.get(id);
                        for (Integer innerId : detailsList.keySet()) {
                            List<CropFormDatum> list = new ArrayList<>();
                            List<DataCropDetails> dataCropDetails = detailsList.get(innerId);
                            for (int i = 0; i < dataCropDetails.size(); i++) {

                                Gson gson = new GsonBuilder()
                                        .setLenient()
                                        .create();

                                JsonObject jsonObject = gson.fromJson(dataCropDetails.get(i).getFarmAddInfoJson(), JsonObject.class);
                                CropFormDatum datum = new CropFormDatum();
                                datum.setType("");
                                int k = 1;
                                for (String innerKey : jsonObject.keySet()) {
                                    if (AppConstants.isValidString(innerKey)) {
                                        if (k == 1) {
                                            datum.setCol1(innerKey);
                                            datum.setCol1Value(jsonObject.get(innerKey).getAsString());
                                        } else if (k == 2) {
                                            datum.setCol2(innerKey);
                                            datum.setCol2Value(jsonObject.get(innerKey).getAsString());
                                        } else if (k == 3) {
                                            datum.setCol3(innerKey);
                                            datum.setCol3Value(jsonObject.get(innerKey).getAsString());
                                        } else if (k == 4) {
                                            datum.setCol4(innerKey);
                                            datum.setCol4Value(jsonObject.get(innerKey).getAsString());
                                        } else if (k == 5) {
                                            datum.setCol5(innerKey);
                                            datum.setCol5Value(jsonObject.get(innerKey).getAsString());
                                        } else if (k == 6) {
                                            datum.setCol6(innerKey);
                                            datum.setCol6Value(jsonObject.get(innerKey).getAsString());
                                        }
                                    }
                                    k++;
                                }
                                list.add(datum);
                            }


                            cropDatumLists.add(list);
                        }

                        FPOCrop fpoCrop = new FPOCrop(new FormModel());
                        fpoCrop.setCropFormDatumArrayLists(cropDatumLists);
                        fpoCrop.setFarmArea(id.getFarmArea() + "");
                        fpoCrop.setSeasonId(id.getSeasonNum() + "");
                        fpoCrop.setCropId(id.getCropId() + "");
                        fpoCropList.add(fpoCrop);

                    }
                    return fpoCropList;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Map<PrevCropDatum, Map<Integer, List<DataCropDetails>>> map = new HashMap<>();
                    for (int i = 0; i < cropDetailsList.size(); i++) {
                        List<List<CropFormDatum>> cropDatumLists = new ArrayList<>();
                        PrevCropDatum details = cropDetailsList.get(i);
                        try {
                            Map<Integer, List<DataCropDetails>> mapInner = new HashMap<>();
                            for (int s = 0; s < details.getData().size(); s++) {
                                DataCropDetails dataCropDetails = details.getData().get(s);

                                if (mapInner.containsKey(dataCropDetails.getFarmAddInfoTypeId())) {
                                    mapInner.get(dataCropDetails.getFarmAddInfoTypeId()).add(dataCropDetails);
                                } else {
                                    List<DataCropDetails> dataCropDetailsList = new ArrayList<>();
                                    dataCropDetailsList.add(dataCropDetails);
                                    mapInner.put(dataCropDetails.getFarmAddInfoTypeId(), dataCropDetailsList);
                                }
                            }
                            map.put(details, mapInner);
                        } catch (Exception e) {

                        }
                    }

                    for (PrevCropDatum id : map.keySet()) {
                        List<List<CropFormDatum>> cropDatumLists = new ArrayList<>();
                        Map<Integer, List<DataCropDetails>> detailsList = map.get(id);
                        for (Integer innerId : detailsList.keySet()) {
                            List<CropFormDatum> list = new ArrayList<>();
                            List<DataCropDetails> dataCropDetails = detailsList.get(innerId);
                            for (int i = 0; i < dataCropDetails.size(); i++) {
                                Gson gson = new GsonBuilder()
                                        .setLenient()
                                        .create();

                                JsonObject jsonObject = gson.fromJson(dataCropDetails.get(i).getFarmAddInfoJson(), JsonObject.class);
                                CropFormDatum datum=null;
                                CropFormDatum datumOld=null;
                                datumOld= cropFormDatumListMap.get("" + dataCropDetails.get(i).getFarmAddInfoTypeId());
                                if (datum == null)
                                    datum = new CropFormDatum();

                                if (datumOld!=null){
                                    datum.setCol1(datumOld.getCol1());
                                    datum.setCol2(datumOld.getCol2());
                                    datum.setCol3(datumOld.getCol3());
                                    datum.setCol4(datumOld.getCol4());
                                    datum.setCol5(datumOld.getCol5());
                                    datum.setCol6(datumOld.getCol6());
                                    datum.setInfoTypeId(datumOld.getInfoTypeId());
                                    datum.setSuperType(datumOld.getSuperType());
                                    datum.setType(datumOld.getType());
//                                    datum.setCol1(datumOld.getCol1());
                                }
                                Log.e(TAG, "PRINTINGGGG" + new Gson().toJson(datum));
//                            datum.setType("");
                                for (String innerKey : jsonObject.keySet()) {
                                    if (AppConstants.isValidString(innerKey)) {
                                        if (AppConstants.isValidString(datum.getCol1()) && innerKey.equals(datum.getCol1())) {
                                            datum.setCol1Value(jsonObject.get(innerKey).getAsString());
                                        } else if (AppConstants.isValidString(datum.getCol2()) && innerKey.equals(datum.getCol2())) {
                                            datum.setCol2Value(jsonObject.get(innerKey).getAsString());
                                        } else if (AppConstants.isValidString(datum.getCol3()) && innerKey.equals(datum.getCol3())) {
                                            datum.setCol3Value(jsonObject.get(innerKey).getAsString());
                                        } else if (AppConstants.isValidString(datum.getCol4()) && innerKey.equals(datum.getCol4())) {
                                            datum.setCol4Value(jsonObject.get(innerKey).getAsString());
                                        } else if (AppConstants.isValidString(datum.getCol5()) && innerKey.equals(datum.getCol5())) {
                                            datum.setCol5Value(jsonObject.get(innerKey).getAsString());
                                        } else if (AppConstants.isValidString(datum.getCol6()) && innerKey.equals(datum.getCol6())) {
                                            datum.setCol6Value(jsonObject.get(innerKey).getAsString());
                                        }
                                    }
                                }
                                list.add(datum);
                            }
                            cropDatumLists.add(list);
                        }
                        FPOCrop fpoCrop = new FPOCrop(new FormModel());
                        fpoCrop.setCropFormDatumArrayLists(cropDatumLists);
                        fpoCrop.setFarmArea(id.getFarmArea() + "");
                        fpoCrop.setSeasonId(id.getSeasonNum() + "");
//                        fpoCrop.setSelectedCrop(getOperatorIndex(id.getCropId()+"",farmCropList));
                        fpoCrop.setCropId(id.getCropId() + "");
                        fpoCropList.add(fpoCrop);

                    }
                } catch (Exception e) {

                }
            }
            return fpoCropList;
        } else {
            FPOCrop fpoCrop = new FPOCrop(new FormModel());
            fpoCrop.setCropFormDatumArrayLists(getListData());
            fpoCropList.add(fpoCrop);
            return fpoCropList;
        }
    }
    private List<DDNew> farmCropList = new ArrayList<>();
    private List<List<CropFormDatum>> getListData() {
        List<List<CropFormDatum>> lists = new ArrayList<>();

        for (int i = 0; i < cropFormDatumList.size(); i++) {

            List<CropFormDatum> list = new ArrayList<>();
            list.add(CropFormDatum.copy(cropFormDatumList.get(i)));
            lists.add(list);
        }
        return lists;
    }

    private List<List<CropFormDatum>> getListDataSuper() {
        List<List<CropFormDatum>> lists = new ArrayList<>();

        for (int i = 0; i < cropFormDatumListSuper.size(); i++) {
            List<CropFormDatum> list = new ArrayList<>();
            list.add(CropFormDatum.copy(cropFormDatumListSuper.get(i)));
            lists.add(list);
        }
        return lists;
    }

    private List<CropFormDatum> cropFormDatumList = new ArrayList<>();
    private Map<String, CropFormDatum> cropFormDatumListMap = new HashMap<>();
    private List<CropFormDatum> cropFormDatumListSuper = new ArrayList<>();
    private Map<String, CropFormDatum> cropFormDatumListSuperMap = new HashMap<>();

    Map<String, List<LatLngFarm>> coordinateHashMap = new HashMap<>();

    private void insertRoomFarmsNew(List<FetchFarmResult> farmResultList) {
        DropDownT expenseDD = new DropDownT(AppConstants.CHEMICAL_UNIT.INPUT_COST, new Gson().toJson(expenseDataDDList));
        DropDownCacheManager.getInstance(context).addChemicalUnit(expenseDD);
        DropDownT resourceDD = new DropDownT(AppConstants.CHEMICAL_UNIT.RESOURCE_USED, new Gson().toJson(resourceDataDDList));
        DropDownCacheManager.getInstance(context).addChemicalUnit(resourceDD);
        DropDownT irriSourceDD = new DropDownT(AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE, new Gson().toJson(irrigationSourceList));
        DropDownCacheManager.getInstance(context).addChemicalUnit(irriSourceDD);
        DropDownT irriTypeDD = new DropDownT(AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE, new Gson().toJson(irrigationTypeList));
        DropDownCacheManager.getInstance(context).addChemicalUnit(irriTypeDD);
        DropDownT soilTypeDD = new DropDownT(AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE, new Gson().toJson(soilTypeList));
        DropDownCacheManager.getInstance(context).addChemicalUnit(soilTypeDD);
        DropDownT seasonDD = new DropDownT(AppConstants.CHEMICAL_UNIT.FARM_SEASON, new Gson().toJson(seasonList));
        DropDownCacheManager.getInstance(context).addChemicalUnit(seasonDD);
        DropDownT cropDD = new DropDownT(AppConstants.CHEMICAL_UNIT.CROP_LIST, new Gson().toJson(cropList));
        DropDownCacheManager.getInstance(context).addChemicalUnit(cropDD);

        for (int i = 0; i < farmResultList.size(); i++) {
            //downloadFileNew(i);
            if (farmResultList.get(i).isSelected()) {
                FetchFarmResult result = farmResultList.get(i);
                DownloadImagesNew downloadImagesNew = new DownloadImagesNew(i);
                downloadImagesNew.execute();
                String germiData = "[]";
                if (farmResultList.get(i).getGerminationList() != null) {
                    germiData = new Gson().toJson(farmResultList.get(i).getGerminationList());
                }
                List<LatLngFarm> lst = new ArrayList<>();
                if (coordinateHashMap.containsKey(result.getFarmId()) && coordinateHashMap.get(result.getFarmId()) != null) {
                    lst.addAll(coordinateHashMap.get(result.getFarmId()));
                }
                String coords = new Gson().toJson(lst);
                String pldOnlineJson = "[]";
                if (result.getPlDataList() != null && result.getPlDataList().size() > 0) {
                    pldOnlineJson = new Gson().toJson(result.getPlDataList());
                }

                FulDetailsAsync async = new FulDetailsAsync(result.getFarmId(), coords, germiData, pldOnlineJson, result);
                async.execute();

                Log.e("OfflineModeAct", "Farm Selected " + new Gson().toJson(farmResultList.get(i).getName()));
            } else {
                Log.e("OfflineModeAct", "Farm Not Selected " + new Gson().toJson(farmResultList.get(i).getName()));
            }
        }

//        FarmCacheManager.getInstance(context).addFarms(this, farmList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {

            } else {
                requestPermission(); // Code for permission
            }
        } else {

            // Code for Below 23 API Oriented Device
            // Do next code
        }
    }

    class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
                FetchFarmSendData fetchFarmSendData = new FetchFarmSendData();
                fetchFarmSendData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                fetchFarmSendData.setCluster_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
                fetchFarmSendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                fetchFarmSendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                Log.e(TAG, "Credentials " + new Gson().toJson(fetchFarmSendData));
                Call<FetchFarmData> fetchFarmDataCall = apiService.fetchFarmDatafncnold(fetchFarmSendData);
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                fetchFarmDataCall.enqueue(new Callback<FetchFarmData>() {
                    @Override
                    public void onResponse(Call<FetchFarmData> call, Response<FetchFarmData> response) {
                        fetchFarmResultList = new ArrayList<>();
                        String error = null;
                        isLoaded[0] = true;
                        Log.e(TAG, "Farm List code " + new Gson().toJson(response.code()));
                        try {
                            if (response.isSuccessful()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        progressDoalog.cancel();
                                        loader.setVisibility(View.GONE);
                                    }
                                });
                                Log.e(TAG, "Farm List " + new Gson().toJson(response.body()));
                                if (response != null) {
                                    fetchFarmData = response.body();
                                    if (response.body().getStatus() == 10) {
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, response.body().getMsg());
                                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                        //un authorized access
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.unauthorized_access));
                                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                        // auth token expired
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.authorization_expired));
                                    } else if (fetchFarmData.getResult() != null) {
                                        if (fetchFarmData.getStatus() != 0) {
                                            if (fetchFarmData.getResult().size() > 0) {
                                                fetchFarmResultList = fetchFarmData.getResult();
                                                //insertRoomFarms(fetchFarmResultList);
                                                if (fetchFarmData.getExpenseDataDDList() != null)
                                                    expenseDataDDList.addAll(fetchFarmData.getExpenseDataDDList());
                                                if (fetchFarmData.getResourceDataDDList() != null)
                                                    resourceDataDDList.addAll(fetchFarmData.getResourceDataDDList());
                                                if (fetchFarmData.getIrrigationSource() != null)
                                                    irrigationSourceList.addAll(fetchFarmData.getIrrigationSource());
                                                if (fetchFarmData.getIrrigationType() != null)
                                                    irrigationTypeList.addAll(fetchFarmData.getIrrigationType());
                                                if (fetchFarmData.getSoilType() != null)
                                                    soilTypeList.addAll(fetchFarmData.getSoilType());
                                                if (fetchFarmData.getSeasonList() != null)
                                                    seasonList.addAll(fetchFarmData.getSeasonList());

                                                if (fetchFarmData.getCropList() != null)
                                                    cropList.addAll(fetchFarmData.getCropList());

                                                if (fetchFarmResultList != null) {
                                                    for (int i = 0; i < fetchFarmResultList.size(); i++) {
                                                        Log.e("TagOffline", fetchFarmResultList.get(i).toString());
//                                                        fetchFarmResultList.get(i).setAdded_by(SharedPreferencesMethod.getString(context,
//                                                                SharedPreferencesMethod.PERSON_ID));
                                                        fetchFarmResultList.get(i).setIs_from_server("0");
                                                    }

                                                    setRecyclerData(fetchFarmResultList);
                                                }

                                            } else {
                                                no_data_available.setVisibility(View.VISIBLE);
                                                make_all_farm_offline_button.setVisibility(View.GONE);
                                                //make_all_farm_offline_button.setClickable(false);
                                                //make_all_farm_offline_button.setEnabled(false);
                                            }

                                        } else {
                                            //progressBar.setVisibility(View.INVISIBLE);
                                            offlineFarmsAdapter = new OfflineFarmsAdapter(context, fetchFarmResultList);
                                            //  recycler_make_offline_list.setHasFixedSize(true);
                                            recycler_make_offline_list.setAdapter(offlineFarmsAdapter);
                                            no_data_available.setVisibility(View.VISIBLE);

                                            make_all_farm_offline_button.setVisibility(View.GONE);
                                            ///make_all_farm_offline_button.setClickable(false);
                                            //make_all_farm_offline_button.setEnabled(false);
                                        }
                                    } else {
                                        make_all_farm_offline_button.setVisibility(View.GONE);
                                        //make_all_farm_offline_button.setClickable(false);
                                        //make_all_farm_offline_button.setEnabled(false);
                                        no_data_available.setVisibility(View.VISIBLE);
                                        Log.e(TAG, "response farm Data  null " + new Gson().toJson(response.body()));
                                        //ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        //viewFailDialog.showDialog(OfflineModeActivity.this, resources.getString(R.string.fail_to_load_offline_mode));
                                    }
                                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                    //un authorized access
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.unauthorized_access));
                                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                    // auth token expired
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.authorization_expired));
                                } else {
                                    try {
                                        error = response.errorBody().string().toString();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
//                                                progressDoalog.cancel();
                                                loader.setVisibility(View.GONE);
                                            }
                                        });
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showDialog(OfflineModeActivity.this, resources.getString(R.string.fail_to_load_offline_mode));
                                        Log.e(TAG, "fetchFarmDatafncnold error  " + error);
                                        //Toast.makeText(context, "Oops something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                long newMillis = AppConstants.getCurrentMills();
                                long diff = newMillis - currMillis;
                                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                                    notifyApiDelay(OfflineModeActivity.this, response.raw().request().url().toString(),
                                            "" + diff, internetSPeed, error, response.code());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    progressDoalog.cancel();
                                    loader.setVisibility(View.GONE);
                                }
                            });
                        }


                    }

                    @Override
                    public void onFailure(Call<FetchFarmData> call, Throwable t) {
                        isLoaded[0] = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                progressDoalog.cancel();
                                loader.setVisibility(View.GONE);
                            }
                        });
                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        notifyApiDelay(OfflineModeActivity.this, call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);
                        Log.e(TAG, "onFailure fetchFarmDatafncnold " + t.toString());
                        //Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(OfflineModeActivity.this, resources.getString(R.string.fail_to_load_offline_mode));

                    }
                });
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        internetFlow(isLoaded[0]);
                    }
                }, AppConstants.DELAY);

            } catch (Exception r) {
                r.printStackTrace();
            }
            return null;
        }
    }


    public class DownloadImagesNew extends AsyncTask<String, Integer, String> {
        String uRl;
        String uRlActivity;
        String filename;
        String filenameActivity;
        int position;

        public DownloadImagesNew(int position) {
            this.position = position;
        }

        @Override
        protected String doInBackground(String... strings) {
            if (fetchFarmData.getResult().get(position).getCalendar() != null && fetchFarmData.getResult().get(position).getCalendar().size() > 0) {
                for (int i = 0; i < fetchFarmData.getResult().get(position).getCalendar().size(); i++) {
                    String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                    filename = "visit_img_" + position + "_" + timeStamp + "_" + i;
                    filenameActivity = "activity_img_" + position + "_" + timeStamp + "_" + i;
                    uRl = fetchFarmData.getResult().get(position).getCalendar().get(i).getImg_link();
                    uRlActivity = fetchFarmData.getResult().get(position).getCalendar().get(i).getActivityImg();
                    File direct = new File(Environment.getExternalStorageDirectory() + "/" + resources.getString(R.string.app_name));
                    fetchFarmData.getResult().get(position).getCalendar().get(i).setImg_link(Environment.getExternalStorageDirectory() + "/" + resources.getString(R.string.app_name) + "/visits/" + filename + ".png");
                    fetchFarmData.getResult().get(position).getCalendar().get(i).setActivityImg(Environment.getExternalStorageDirectory() + "/" + resources.getString(R.string.app_name) + "/visits/" + filenameActivity + ".png");
                    if (!direct.exists()) {
                        direct.mkdirs();
                    }
                    File directnew = new File(Environment.getExternalStorageDirectory() + "/" + resources.getString(R.string.app_name) + "/visits");
                    if (!directnew.exists()) {
                        directnew.mkdirs();
                    }

                    URL url = null;
                    URL urlActivity = null;
                    try {
                        //  OutputStream output = new FileOutputStream(directnew + filename + ".png");
                        if (uRl != null && !uRl.trim().equals("0")) {
                            url = new URL(uRl);
                            InputStream in = new BufferedInputStream(url.openStream());
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            byte[] buf = new byte[700 * 700];
                            int n = 0;
                            while (-1 != (n = in.read(buf))) {
                                out.write(buf, 0, n);
                            }
                            out.close();
                            in.close();
                            byte[] response = out.toByteArray();

                            FileOutputStream fos = new FileOutputStream(directnew + "/" + filename + ".png");
                            fos.write(response);
                            fos.close();
                        } else {
                            Log.e("OfflineMode", "Url Visit is null or 0");
                        }
                        if (uRlActivity != null && !uRlActivity.trim().equals("0")) {
                            urlActivity = new URL(uRlActivity);
                            InputStream inA = new BufferedInputStream(urlActivity.openStream());
                            ByteArrayOutputStream outA = new ByteArrayOutputStream();
                            byte[] bufA = new byte[700 * 700];
                            int nA = 0;
                            while (-1 != (nA = inA.read(bufA))) {
                                outA.write(bufA, 0, nA);
                            }
                            outA.close();
                            inA.close();
                            byte[] responseA = outA.toByteArray();

                            FileOutputStream fosA = new FileOutputStream(directnew + "/" + filenameActivity + ".png");
                            fosA.write(responseA);
                            fosA.close();
                        } else {
                            Log.e("OfflineMode", "Url Activity is null or 0");
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        Log.e("OfflineModeAct", uRl + " , First Exception:  " + e.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("OfflineModeAct", uRl + " , Second Exception:  " + e.toString());
                    }

                }
            }

            CalendarJSON calendarJSON = new CalendarJSON();
            calendarJSON.setCalendarData(fetchFarmData.getResult().get(position).getCalendar());
            Timeline timeline = new Timeline(fetchFarmData.getResult().get(position).getFarmId(), new Gson().toJson(calendarJSON), null);
            TimelineCacheManager.getInstance(context).addTimeline(timelineNotifyInterface, timeline);
            return null;
        }
    }

    public class DownloadImagesVisit extends AsyncTask<String, Integer, String> {
        String uRl;
        String filename;
        VisitResponseNew response;
        int position;

        public DownloadImagesVisit(VisitResponseNew response, int position) {
            this.response = response;
            this.position = position;
        }

        @Override
        protected String doInBackground(String... strings) {

            if (response.getVisitImages() != null) {
                for (int i = 0; i < response.getVisitImages().size(); i++) {
                    if (response.getVisitImages().get(i) != null && response.getVisitImages().get(i).getImgLink() != null) {
                        imgCount++;
                        Log.e("OfflineModeAct", "Img Count " + imgCount);
                        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                        filename = "visit_img_" + i + "_new_" + timeStamp + "_" + i;
                        uRl = response.getVisitImages().get(i).getImgLink();
                        File direct = new File(Environment.getExternalStorageDirectory() + "/" + resources.getString(R.string.app_name));
                        response.getVisitImages().get(i).setImgLink(Environment.getExternalStorageDirectory() + "/" + resources.getString(R.string.app_name) + "/visits/" + filename + ".png");
                        if (!direct.exists()) {
                            direct.mkdirs();
                        }
                        File directnew = new File(Environment.getExternalStorageDirectory() + "/" + resources.getString(R.string.app_name) + "/visits");
                        if (!directnew.exists()) {
                            directnew.mkdirs();
                        }

                        URL url = null;
                        try {
                            //  OutputStream output = new FileOutputStream(directnew + filename + ".png");

                            url = new URL(uRl);
                            InputStream in = new BufferedInputStream(url.openStream());
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            byte[] buf = new byte[700 * 700];
                            int n = 0;
                            while (-1 != (n = in.read(buf))) {
                                out.write(buf, 0, n);
                            }
                            out.close();
                            in.close();
                            byte[] response = out.toByteArray();

                            FileOutputStream fos = new FileOutputStream(directnew + "/" + filename + ".png");
                            fos.write(response);
                            fos.close();


                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


            Visit vr = new Visit(response.getVisitReportId(),
                    new Gson().toJson(response), "Y", null, "N", null, response.getFarmId());
            VrCacheManager.getInstance(context).addVisits(vr);

            if (totalVisitSize == position) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        prog_bar_offline.setVisibility(View.INVISIBLE);
                    }
                });

                setDeviceId();
            }

            return null;
        }
    }

    public class DownloadImagesActivity extends AsyncTask<String, Integer, String> {
        String uRl;
        String filename;
        DoneActivityResponseNew response;
        int position;
        List<String> farmIdList;

        public DownloadImagesActivity(DoneActivityResponseNew response, int position, List<String> farmIdList) {
            this.response = response;
            this.position = position;
            this.farmIdList = farmIdList;
        }

        @Override
        protected String doInBackground(String... strings) {

            if (response.getImages() != null) {
                if (response.getImages() != null) {
                    for (int i = 0; i < response.getImages().size(); i++) {
                        if (response.getImages().get(i) != null && response.getImages().get(i).getImgLink() != null) {
                            imgCount++;
                            Log.e("OfflineModeAct", "Img Count activity " + imgCount);
                            String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                            filename = "activity_img_" + i + "_new_" + timeStamp + "_" + i;
                            uRl = response.getImages().get(i).getImgLink();
                            File direct = new File(Environment.getExternalStorageDirectory() + "/" + resources.getString(R.string.app_name));
                            response.getImages().get(i).setImgLink(Environment.getExternalStorageDirectory() + "/" + resources.getString(R.string.app_name) + "/activities/" + filename + ".png");
                            if (!direct.exists()) {
                                direct.mkdirs();
                            }
                            File directnew = new File(Environment.getExternalStorageDirectory() + "/" + resources.getString(R.string.app_name) + "/activities");
                            if (!directnew.exists()) {
                                directnew.mkdirs();
                            }
                            URL url = null;
                            try {
                                url = new URL(uRl);
                                InputStream in = new BufferedInputStream(url.openStream());
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                byte[] buf = new byte[700 * 700];
                                int n = 0;
                                while (-1 != (n = in.read(buf))) {
                                    out.write(buf, 0, n);
                                }
                                out.close();
                                in.close();
                                byte[] response = out.toByteArray();
                                FileOutputStream fos = new FileOutputStream(directnew + "/" + filename + ".png");
                                fos.write(response);
                                fos.close();

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    response.setDoneActivityImageList(new ArrayList<>());
                }
            }
            DoneActivity doneActivity = new DoneActivity(response.getFarmCalActivityId(), response.getFarmId(),
                    new Gson().toJson(response), "N", null, "Y");

            ActivityCacheManager.getInstance(context).addActivity(doneActivity);

            if (totalActivityZize == position) {
                getAllData(farmIdList);
            }

            return null;
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(OfflineModeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(OfflineModeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //Toast.makeText(OfflineModeActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();


            new AlertDialog.Builder(this)
                    .setTitle(resources.getString(R.string.please_allow_storage_permission))
                    .setMessage(resources.getString(R.string.click_on_storage))
                    .setCancelable(false)
                    .setPositiveButton(resources.getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            final Intent i = new Intent();
                            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            i.setData(Uri.parse("package:" + getPackageName()));
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(i);
                        }
                    })

                    .show();

        } else {
            ActivityCompat.requestPermissions(OfflineModeActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                    requestPermission();
                }
                break;
        }


    }

    @Override
    public void onFarmLoaded(List<Farm> farmList) {

    }

    @Override
    public void onFarmAdded() {
        Log.e("OfflineMode", "OnfarmAdded");
    }

    @Override
    public void onFarmInsertError(Throwable e) {
        Log.e("OfflineMode", "FarmInsererror " + e);
    }

    @Override
    public void onTimelineLoaded(Timeline timeline) {

    }

    @Override
    public void onTimelineAdded() {

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
    public void farmDeleted() {

    }

    @Override
    public void farmDeletionFaild() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    SearchView searchView;
    SearchAsynch searchAsynch;
    BottomSheetDialog bottomSheetDialog;
    TextView et_sort_standing_area;
    TextView et_sort_standing_area_l_to_h;
    TextView et_sort_expected_area;
    TextView et_sort_actual_area;
    TextView et_sort_available_area_l_to_h;
    TextView et_sort_available_area_h_to_l;
    TextView et_sort_harvest_date;

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_search) {
            MenuItem searchItem = item;
            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    return true;
                }
            });
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            if (searchItem != null) {
                searchView = (SearchView) searchItem.getActionView();
            }
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    if (newText != null && !TextUtils.isEmpty(newText)) {
                        if (searchAsynch == null) {
                            searchAsynch = new SearchAsynch(newText.trim());
                            searchAsynch.execute();
                        } else {
                            searchAsynch.cancel(true);
                            searchAsynch = new SearchAsynch(newText.trim());
                            searchAsynch.execute();
                        }
                    } else {
                        setRecyclerData(fetchFarmResultList);
                    }
                    return true;
                }

                public boolean onQueryTextSubmit(String newText) {
                    if (newText != null && !TextUtils.isEmpty(newText)) {
                        if (searchAsynch == null) {
                            searchAsynch = new SearchAsynch(newText.trim());
                            searchAsynch.execute();
                        } else {
                            searchAsynch.cancel(true);
                            searchAsynch = new SearchAsynch(newText.trim());
                            searchAsynch.execute();
                        }
                    } else {
                        setRecyclerData(fetchFarmResultList);
                    }
                    return true;
                }
            };
            if (searchView != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setIconified(false);
                searchView.setOnQueryTextListener(queryTextListener);
                searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        searchView.setIconified(false);
                        searchView.setIconifiedByDefault(false);
                        searchItem.collapseActionView();

                        return false;
                    }
                });
            }


        } else if (item.getItemId() == R.id.action_filter) {
            //showFilterDialog();
            bottomSheetDialog.show();
            return false;
        }


        return super.onOptionsItemSelected(item);
    }

    /*private void showFilterDialog() {

        View view = getLayoutInflater().inflate(R.layout.filter_options_for_planned_harvest, null);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.setCancelable(true);

        final TextView filterDateAscending = dialog.findViewById(R.id.filterDateAscending);
        final TextView filterDateDescending = dialog.findViewById(R.id.filterDateDescending);
        final TextView filterNoOfBagAsc = dialog.findViewById(R.id.filterNoOfBagAsc);
        final TextView filterNoOfBagD = dialog.findViewById(R.id.filterNoOfBagD);
        final TextView filterNetWeightAsc = dialog.findViewById(R.id.filterNetWeightAsc);
        final TextView filterNetWeightD = dialog.findViewById(R.id.filterNetWeightD);
        String pickDateStr = resources.getString(R.string.pick_up_date);
        String noOfBagsStr = resources.getString(R.string.no_of_bags);
        String netWeightStr = resources.getString(R.string.net_weight_label);

        String higtToLowStr = resources.getString(R.string.high_to_low);
        String lowToHighStr = resources.getString(R.string.low_to_high);

        filterDateDescending.setText(pickDateStr + " -- " + higtToLowStr);
        filterDateAscending.setText(pickDateStr + " -- " + lowToHighStr);
        filterNoOfBagD.setText(noOfBagsStr + " -- " + higtToLowStr);
        filterNoOfBagAsc.setText(noOfBagsStr + " -- " + lowToHighStr);
        filterNetWeightD.setText(netWeightStr + " -- " + higtToLowStr);
        filterNetWeightAsc.setText(netWeightStr + " -- " + lowToHighStr);

        filterDateAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                HarvestShowPlanActivity.FilterAsync async = new HarvestShowPlanActivity.FilterAsync(HarvestShowPlanActivity.SORT_OPTIONS.DATE_ASCENDING);
                async.execute();
            }
        });
        filterDateDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                HarvestShowPlanActivity.FilterAsync async = new HarvestShowPlanActivity.FilterAsync(HarvestShowPlanActivity.SORT_OPTIONS.DATE_DESCENDING);
                async.execute();
            }
        });
        filterNoOfBagAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                HarvestShowPlanActivity.FilterAsync async = new HarvestShowPlanActivity.FilterAsync(HarvestShowPlanActivity.SORT_OPTIONS.NO_OF_BAG_ASCENDING);
                async.execute();
            }
        });
        filterNoOfBagD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                HarvestShowPlanActivity.FilterAsync async = new HarvestShowPlanActivity.FilterAsync(HarvestShowPlanActivity.SORT_OPTIONS.NO_OF_BAG_DESCENDING);
                async.execute();
            }
        });
        filterNetWeightAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                HarvestShowPlanActivity.FilterAsync async = new HarvestShowPlanActivity.FilterAsync(HarvestShowPlanActivity.SORT_OPTIONS.NET_WEIGHT_ASCENDING);
                async.execute();
            }
        });
        filterNetWeightD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                HarvestShowPlanActivity.FilterAsync async = new HarvestShowPlanActivity.FilterAsync(HarvestShowPlanActivity.SORT_OPTIONS.NET_WEIGHT_DESCENDING);
                async.execute();

            }
        });
        dialog.show();

    }*/


    private class SearchAsynch extends AsyncTask<String, Integer, String> {
        String query;
        List<FetchFarmResult> filteredList = new ArrayList<>();

        public SearchAsynch(String query) {
            this.query = query.trim();
        }

        @Override
        protected String doInBackground(String... strings) {
            filteredList.clear();
            if (fetchFarmResultList != null) {
                for (int i = 0; i < fetchFarmResultList.size(); i++) {
                    try {
                        FetchFarmResult data = fetchFarmResultList.get(i);
                        if (data.getName() != null && data.getName().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(data);
                        } else if (data.getCropName() != null && data.getCropName().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(data);
                        } else if (data.getActualHarvestDate() != null && data.getActualHarvestDate().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(data);
                        } else if (data.getExpHarvestDate() != null && data.getExpHarvestDate().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(data);
                        } else if (data.getActualArea() != null && data.getActualArea().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(data);
                        } else if (data.getStandingAcres() != null && data.getStandingAcres().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(data);
                        } else if (data.getExpArea() != null && data.getExpArea().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(data);
                        } else if (data.getFatherName() != null && data.getFatherName().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(data);
                        } else if (data.getFarmName() != null && data.getFarmName().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(data);
                        } else if (data.getLotNo() != null && data.getLotNo().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(data);
                        } else if (data.getAddL1() != null && data.getAddL1().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(data);
                        } else if (data.getAddL2() != null && data.getAddL2().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(data);
                        } else if (data.getVillageOrCity() != null && data.getVillageOrCity().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(data);
                        } else if (data.getDistrict() != null && data.getDistrict().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(data);
                        } else if (data.getMob() != null && data.getMob().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (filteredList.size() > 0) {
                        setRecyclerData(filteredList);
                    } else {
                        no_data_available.setVisibility(View.VISIBLE);
                        recycler_make_offline_list.setVisibility(View.GONE);
                    }
                }
            });
            return null;
        }
    }

    private void setRecyclerData(List<FetchFarmResult> fetchFarmResultList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                offlineFarmsAdapter = new OfflineFarmsAdapter(context, fetchFarmResultList);
                recycler_make_offline_list.setAdapter(offlineFarmsAdapter);
                offlineFarmsAdapter.notifyDataSetChanged();
                linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recycler_make_offline_list.setLayoutManager(linearLayoutManager);
            }
        });
    }


    private void initializeBottomsheet() {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.dialog_sort_farms);
        Window window = bottomSheetDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        et_sort_standing_area = bottomSheetDialog.findViewById(R.id.et_sort_standing_area);
        et_sort_standing_area_l_to_h = bottomSheetDialog.findViewById(R.id.et_sort_standing_area_l_to_h);
        et_sort_expected_area = bottomSheetDialog.findViewById(R.id.et_sort_expected_area);
        et_sort_actual_area = bottomSheetDialog.findViewById(R.id.et_sort_actual_area);
        et_sort_available_area_l_to_h = bottomSheetDialog.findViewById(R.id.et_sort_available_area_lto_h);
        et_sort_available_area_h_to_l = bottomSheetDialog.findViewById(R.id.et_sort_available_area_h_to_l);
        et_sort_harvest_date = bottomSheetDialog.findViewById(R.id.et_sort_harvest_date);


        et_sort_standing_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                FilterAsync async = new FilterAsync(SORT_OPTIONS.STANDING_AREA_HTL);
                async.execute();

            }
        });
        et_sort_standing_area_l_to_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                FilterAsync async = new FilterAsync(SORT_OPTIONS.STANDING_AREA_LTH);
                async.execute();

            }
        });
        et_sort_expected_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Log.e("expected area", "y");


            }
        });
        et_sort_actual_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Log.e("actual area", "y");


            }
        });
        et_sort_available_area_h_to_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        et_sort_available_area_l_to_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();

                Log.e("available arae l to h", "y");


            }
        });
        et_sort_harvest_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                FilterAsync async = new FilterAsync(SORT_OPTIONS.HARVEST_DATE);
                async.execute();
            }
        });
    }

    private interface SORT_OPTIONS {
        public final int HARVEST_DATE = 1;
        public final int STANDING_AREA_LTH = 2;
        public final int STANDING_AREA_HTL = 3;
        public final int EXPECTED_AREA_LTH = 4;
        public final int EXPECTED_AREA_HTL = 5;
        public final int ACTUAL_AREA_LTH = 6;
        public final int ACTUAL_AREA_HTL = 7;
    }

    private class FilterAsync extends AsyncTask<String, Integer, String> {
        int filter;
        List<FetchFarmResult> farmResults = new ArrayList<>();
        List<FetchFarmResult> tempList = new ArrayList<>();
        List<FetchFarmResult> tempList2 = new ArrayList<>();

        public FilterAsync(int filter) {
            this.filter = filter;
        }

        @Override
        protected String doInBackground(String... strings) {
            if (fetchFarmResultList != null) {
                farmResults.clear();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                //Sort options
                if (filter == SORT_OPTIONS.HARVEST_DATE) {
                    for (int i = 0; i < fetchFarmResultList.size(); i++) {
                        if (fetchFarmResultList.get(i).getExpHarvestDate() != null && !TextUtils.isEmpty(fetchFarmResultList.get(i).getExpHarvestDate()))
                            tempList.add(fetchFarmResultList.get(i));
                        else
                            tempList2.add(fetchFarmResultList.get(i));
                    }
                    Log.e(TAG, "Temp list 1 " + new Gson().toJson(tempList));
                    Log.e(TAG, "Temp list 2 " + new Gson().toJson(tempList2));
                    farmResults.addAll(tempList);
                    Collections.sort(farmResults, new Comparator<FetchFarmResult>() {
                        @Override
                        public int compare(FetchFarmResult lhs, FetchFarmResult rhs) {
                            try {
                                return format.parse(rhs.getExpHarvestDate()).compareTo(format.parse(lhs.getExpHarvestDate()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return farmResults.size() - 1;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return farmResults.size() - 1;
                            }
                        }
                    });
                    farmResults.addAll(tempList2);
                    setRecyclerData(farmResults);
                } else if (filter == SORT_OPTIONS.STANDING_AREA_HTL) {
                    for (int i = 0; i < fetchFarmResultList.size(); i++) {
                        if (fetchFarmResultList.get(i).getStandingAcres() != null && !TextUtils.isEmpty(fetchFarmResultList.get(i).getStandingAcres()))
                            tempList.add(fetchFarmResultList.get(i));
                        else
                            tempList2.add(fetchFarmResultList.get(i));
                    }
                    Log.e(TAG, "Temp list 1 " + new Gson().toJson(tempList));
                    Log.e(TAG, "Temp list 2 " + new Gson().toJson(tempList2));
                    farmResults.addAll(tempList);
                    Collections.sort(farmResults, new Comparator<FetchFarmResult>() {
                        @Override
                        public int compare(FetchFarmResult lhs, FetchFarmResult rhs) {
                            try {
                                return Float.valueOf(rhs.getStandingAcres().trim()).compareTo(Float.valueOf(lhs.getStandingAcres().trim()));
                            } catch (Exception e) {
                                e.printStackTrace();
                                return farmResults.size() - 1;
                            }
                        }
                    });
                    farmResults.addAll(tempList2);
                    setRecyclerData(farmResults);
                } else if (filter == SORT_OPTIONS.STANDING_AREA_LTH) {
                    for (int i = 0; i < fetchFarmResultList.size(); i++) {
                        if (fetchFarmResultList.get(i).getStandingAcres() != null && !TextUtils.isEmpty(fetchFarmResultList.get(i).getStandingAcres()))
                            tempList.add(fetchFarmResultList.get(i));
                        else
                            tempList2.add(fetchFarmResultList.get(i));
                    }
                    Log.e(TAG, "Temp list 1 " + new Gson().toJson(tempList));
                    Log.e(TAG, "Temp list 2 " + new Gson().toJson(tempList2));
                    farmResults.addAll(tempList);
                    Collections.sort(farmResults, new Comparator<FetchFarmResult>() {
                        @Override
                        public int compare(FetchFarmResult lhs, FetchFarmResult rhs) {
                            try {
                                return Float.valueOf(lhs.getStandingAcres().trim()).compareTo(Float.valueOf(rhs.getStandingAcres().trim()));
                            } catch (Exception e) {
                                e.printStackTrace();
                                return farmResults.size() - 1;
                            }
                        }
                    });
                    farmResults.addAll(tempList2);
                    setRecyclerData(farmResults);

                }/* else if (filter == HarvestShowPlanActivity.SORT_OPTIONS.NO_OF_BAG_ASCENDING) {
                planDataList.addAll(showHarvestPlanDataList);
                vehicleDataList.addAll(showHarvestVehicleData);
                Collections.sort(planDataList, new Comparator<ShowHarvestPlanData>() {
                    @Override
                    public int compare(ShowHarvestPlanData lhs, ShowHarvestPlanData rhs) {
                        try {
                            return Float.valueOf(lhs.getNoOfBagsBlk().trim()).compareTo(Float.valueOf(rhs.getNoOfBagsBlk().trim()));
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                Collections.sort(vehicleDataList, new Comparator<ShowHarvestVehicleData>() {
                    @Override
                    public int compare(ShowHarvestVehicleData lhs, ShowHarvestVehicleData rhs) {
                        try {
                            return Float.valueOf(lhs.getNoOfBags().trim()).compareTo(Float.valueOf(rhs.getNoOfBags().trim()));
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                setRecyclerData(planDataList, vehicleDataList);
            } else if (filter == HarvestShowPlanActivity.SORT_OPTIONS.NO_OF_BAG_DESCENDING) {
                planDataList.addAll(showHarvestPlanDataList);
                vehicleDataList.addAll(showHarvestVehicleData);
                Collections.sort(planDataList, new Comparator<ShowHarvestPlanData>() {
                    @Override
                    public int compare(ShowHarvestPlanData lhs, ShowHarvestPlanData rhs) {
                        try {
                            return Float.valueOf(rhs.getTotalBags().trim()).compareTo(Float.valueOf(lhs.getTotalBags().trim()));
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                Collections.sort(vehicleDataList, new Comparator<ShowHarvestVehicleData>() {
                    @Override
                    public int compare(ShowHarvestVehicleData lhs, ShowHarvestVehicleData rhs) {
                        try {
                            return Float.valueOf(rhs.getNoOfBags().trim()).compareTo(Float.valueOf(lhs.getNoOfBags().trim()));
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                setRecyclerData(planDataList, vehicleDataList);
            } else if (filter == HarvestShowPlanActivity.SORT_OPTIONS.NET_WEIGHT_ASCENDING) {
                planDataList.addAll(showHarvestPlanDataList);
                vehicleDataList.addAll(showHarvestVehicleData);
                Collections.sort(planDataList, new Comparator<ShowHarvestPlanData>() {
                    @Override
                    public int compare(ShowHarvestPlanData lhs, ShowHarvestPlanData rhs) {
                        try {
                            return Float.valueOf(lhs.getNetWtOfStock().trim()).compareTo(Float.valueOf(rhs.getNetWtOfStock().trim()));
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                Collections.sort(vehicleDataList, new Comparator<ShowHarvestVehicleData>() {
                    @Override
                    public int compare(ShowHarvestVehicleData lhs, ShowHarvestVehicleData rhs) {
                        try {
                            return Float.valueOf(lhs.getNetWeight().trim().trim()).compareTo(Float.valueOf(rhs.getNetWeight().trim()));
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                setRecyclerData(planDataList, vehicleDataList);
            } else if (filter == HarvestShowPlanActivity.SORT_OPTIONS.NET_WEIGHT_DESCENDING) {
                planDataList.addAll(showHarvestPlanDataList);
                vehicleDataList.addAll(showHarvestVehicleData);
                Collections.sort(planDataList, new Comparator<ShowHarvestPlanData>() {
                    @Override
                    public int compare(ShowHarvestPlanData lhs, ShowHarvestPlanData rhs) {
                        try {
                            return Float.valueOf(rhs.getNetWtOfStock().trim()).compareTo(Float.valueOf(lhs.getNetWtOfStock().trim()));
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                Collections.sort(vehicleDataList, new Comparator<ShowHarvestVehicleData>() {
                    @Override
                    public int compare(ShowHarvestVehicleData lhs, ShowHarvestVehicleData rhs) {
                        try {
                            return Float.valueOf(rhs.getNetWeight().trim()).compareTo(Float.valueOf(lhs.getNetWeight().trim()));
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                setRecyclerData(planDataList, vehicleDataList);
            }*/

            }
            return null;
        }
    }

    List<Cluster> clusterList = new ArrayList<>();

    private void getClusterOfCompany() {
        clusterList.clear();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getClusters(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID)).enqueue(new Callback<ClusterResponse>() {
            @Override
            public void onResponse(Call<ClusterResponse> call, Response<ClusterResponse> response) {
                String error = null;
                isLoaded[0] = true;

                if (response.isSuccessful()) {
                    Log.e(TAG, "Sending New Param CLUSTER " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 1) {
                        if (response.body().getClusterList() != null) {
                            clusterList.addAll(response.body().getClusterList());
                            DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST);
                            DropDownT clusters = new DropDownT(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST, new Gson().toJson(clusterList));
                            DropDownCacheManager.getInstance(context).addChemicalUnit(clusters);
                        }
                    } else if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this);
                        //progressDialog.cancel();
                    } else {

                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        //progressDialog.cancel();
                        error = response.errorBody().string();
                        Log.e(TAG, "Cluster Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(OfflineModeActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<ClusterResponse> call, Throwable t) {
                isLoaded[0] = true;
                Log.e(TAG, "Cluster Failure " + t.toString());
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(OfflineModeActivity.this, call.request().url().toString(),
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

    List<FarmerSpinnerData> farmerDataList = new ArrayList<>();

    private void getFarmerList() {
        farmerDataList.clear();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String clusterId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getFarmerList(compId, clusterId, userId, token).enqueue(new Callback<FetchFarmerResponse>() {
            @Override
            public void onResponse(Call<FetchFarmerResponse> call, Response<FetchFarmerResponse> response) {
                String error = null;
                isLoaded[0] = true;
                if (response.isSuccessful()) {
                    Log.e(TAG, "Sending New Param Farmer List " + new Gson().toJson(response.body()));
//                    Log.e(TAG, "Farmer List Res " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == 1) {
                        if (response.body().getFarmerDataList() != null && response.body().getFarmerDataList().size() > 0) {
                            farmerDataList.addAll(response.body().getFarmerDataList());
                            DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.FARMER_LIST);
                            DropDownT farmers = new DropDownT(AppConstants.CHEMICAL_UNIT.FARMER_LIST, new Gson().toJson(farmerDataList));
                            DropDownCacheManager.getInstance(context).addChemicalUnit(farmers);
                        }
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "Farmer List Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(OfflineModeActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<FetchFarmerResponse> call, Throwable t) {
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(OfflineModeActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "Farmer List failure " + t.toString());
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
                        CompAgriResponse data = response.body();
                        if (data != null && data.getData() != null) {

                            DropDownCacheManager2.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.COMP_AGRI_INPUTS);
                            DropDownT2 clusters = new DropDownT2(AppConstants.CHEMICAL_UNIT.COMP_AGRI_INPUTS, new Gson().toJson(data));
                            DropDownCacheManager2.getInstance(context).addChemicalUnit(clusters);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "Comp Agri Err " + error);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(OfflineModeActivity.this, resources.getString(R.string.failed_load_farm));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
                    notifyApiDelay(OfflineModeActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<CompAgriResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(OfflineModeActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "Comp Agri Err " + t.toString());
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialog(OfflineModeActivity.this, resources.getString(R.string.failed_load_farm));
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
                        Log.e(TAG, "getAllDataNew Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DDResponseNew> call, Throwable t) {
                Log.e(TAG, "Failure getAllDataNew " + t.toString());
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

    private void getAllDataNew2(final String type) {
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
                        Log.e(TAG, "Cache Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DDResponseNew> call, Throwable t) {
                Log.e(TAG, "Failure getOfflineData " + t.toString());
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

    private void getAllDataNew3(final String type) {
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
                        Log.e(TAG, "Cache Error " + error);
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

    private void getAllDataNew4(final String type) {
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
                        Log.e(TAG, "Cache Error " + error);
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

    private void getAllDataNew5(final String type) {
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
                        Log.e(TAG, "Cache Error " + error);
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

    private void getAllDataNew6(final String type) {
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
                        Log.e(TAG, "Cache Error " + error);
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

    private void getAllDataNew7(final String type) {
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
                        Log.e(TAG, "Cache Error " + error);
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

    private void getAllDataNew8(final String type) {
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
                        Log.e(TAG, "Cache Error " + error);
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

    private void getFormData() {
        ParamData paramData = new ParamData();
        paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        Log.e(TAG, "Sending New Param i  AND type  " + new Gson().toJson(paramData));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getFarmCropForm(paramData).enqueue(new Callback<CropFormResponse>() {
            @Override
            public void onResponse(Call<CropFormResponse> call, Response<CropFormResponse> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    CropFormResponse response = response1.body();
                    Log.e(TAG, "Response getFormData  " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, response.getMessage());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        if (response != null  &&(response.getData() != null || response.getSuperData()!=null)) {

                            DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.FARM_ADD_DYNAMIC);
                            DropDownT material = new DropDownT(AppConstants.CHEMICAL_UNIT.FARM_ADD_DYNAMIC, new Gson().toJson(response));
                            DropDownCacheManager.getInstance(context).addChemicalUnit(material);
                        }

                        if (response.getData() != null) {
                            OfflineModeActivity.this.cropFormDatumList.addAll(response.getData());
                            for (CropFormDatum cropFormDatum : response.getData()) {
                                OfflineModeActivity.this.cropFormDatumListMap.put(cropFormDatum.getInfoTypeId(), cropFormDatum);
                            }
                        }

                        if (response.getSuperData() != null) {
                            OfflineModeActivity.this.cropFormDatumListSuper.addAll(response.getSuperData());
                            for (CropFormDatum cropFormDatum : response.getSuperData()) {
                                if (cropFormDatum.getType().trim().equals("Equipment / Machinery"))
                                    OfflineModeActivity.this.cropFormDatumListSuperMap.put("E", cropFormDatum);
                                else
                                    OfflineModeActivity.this.cropFormDatumListSuperMap.put("A", cropFormDatum);
                            }
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
                        Log.e(TAG, "getFormData Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CropFormResponse> call, Throwable t) {
                Log.e(TAG, "Failure getFormData " + t.toString());
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


    private void setDeviceId(){
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String deviceId = AppConstants.getMacAddr();
        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.setDeviceId(deviceId,userId,token).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    StatusMsgModel villageResponse = response1.body();
                    Log.e(TAG, "getChaksOfVillage " + new Gson().toJson(villageResponse));
                   /* try {
                        Log.e(TAG, "getChaksOfVillage Response " + response1.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
//                    Log.e(TAG, "getGeoCount Response " + new Gson().toJson(countResponse));
                    if (villageResponse.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, villageResponse.getMessage());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        String todayDateStr;
                        Date todayDate = Calendar.getInstance().getTime();
                        DateFormat df = new SimpleDateFormat(AppConstants.DATE_FORMAT_SERVER);
                        todayDateStr = df.format(todayDate);
                        Log.e(TAG, "Today date " + todayDateStr);

                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.OFFLINE_MODE_DATE, todayDateStr);
                        Intent intent = new Intent(context, LandingActivity.class);
                        ActivityOptions options = null;
                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, true);
                        //  SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ONLINE, "offline");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            startActivity(intent, options.toBundle());
                            finish();
                        } else {
                            startActivity(intent);
                            finish();
                        }
                    }
                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {

                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "getChaksOfVillage Error " + error + " Code " + response1.code() + " ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e(TAG, "getChaksOfVillage Failure  " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
            }
        });




    }

    private void wipeDeviceId(){
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String deviceId = AppConstants.getMacAddr();
        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.wipeDeviceId(userId,token).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    StatusMsgModel villageResponse = response1.body();
                    Log.e(TAG, "getChaksOfVillage " + new Gson().toJson(villageResponse));
                   /* try {
                        Log.e(TAG, "getChaksOfVillage Response " + response1.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
//                    Log.e(TAG, "getGeoCount Response " + new Gson().toJson(countResponse));
                    if (villageResponse.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, villageResponse.getMessage());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        String todayDateStr;
                        Date todayDate = Calendar.getInstance().getTime();
                        DateFormat df = new SimpleDateFormat(AppConstants.DATE_FORMAT_SERVER);
                        todayDateStr = df.format(todayDate);
                        Log.e(TAG, "Today date " + todayDateStr);

                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.OFFLINE_MODE_DATE, todayDateStr);
                        Intent intent = new Intent(context, LandingActivity.class);
                        ActivityOptions options = null;
                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, true);
                        //  SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ONLINE, "offline");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            startActivity(intent, options.toBundle());
                            finish();
                        } else {
                            startActivity(intent);
                            finish();
                        }
                    }
                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(OfflineModeActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {

                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "getChaksOfVillage Error " + error + " Code " + response1.code() + " ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e(TAG, "getChaksOfVillage Failure  " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
            }
        });




    }

}
