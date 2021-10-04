package com.i9930.croptrails.Landing;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.AddFarm.Model.CowAndCatles;
import com.i9930.croptrails.AddFarm.Model.FPOCrop;
import com.i9930.croptrails.AddFarm.Model.FarmData;
import com.i9930.croptrails.AddFarm.Model.FarmerAndFarmData;
import com.i9930.croptrails.AddFarm.Model.FormModel;
import com.i9930.croptrails.AddFarm.Share;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.DDNew;
import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormDatum;
import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormResponse;
import com.i9930.croptrails.CommonClasses.ParamData;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineResponse;
import com.i9930.croptrails.HarvestReport.Model.ViewHarvestDetails;
import com.i9930.croptrails.Landing.Adapter.VettingFarmAdapterNew;
import com.i9930.croptrails.Landing.Models.NewData.FarmResponseNew;
import com.i9930.croptrails.Landing.Models.NewData.FetchFarmResultNew;
import com.i9930.croptrails.Landing.Models.Offline.FarmVisitResponse;
import com.i9930.croptrails.Landing.Models.ad.AdFarmDatum;
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;
import com.i9930.croptrails.OfflineMode.Model.CalendarActResponse;
import com.i9930.croptrails.OfflineMode.Model.FarmIdArray;
import com.i9930.croptrails.OfflineMode.Model.FarmsCoordinatesResponse;
import com.i9930.croptrails.OfflineMode.Model.InputCostAndResourceData;
import com.i9930.croptrails.OfflineMode.Model.OfflineResponse;
import com.i9930.croptrails.OfflineMode.OfflineModeActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.DoneActivities.ActivityCacheManager;
import com.i9930.croptrails.RoomDatabase.DoneActivities.DoneActivity;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownCacheManager;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
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
import com.i9930.croptrails.RoomDatabase.Visit.Visit;
import com.i9930.croptrails.RoomDatabase.Visit.VrCacheManager;
import com.i9930.croptrails.ShowInputCost.Model.InputCostResponse;
import com.i9930.croptrails.SubmitActivityForm.Model.DoneActNew.DoneActInputs;
import com.i9930.croptrails.SubmitActivityForm.Model.DoneActNew.DoneActivityResponseNew;
import com.i9930.croptrails.SubmitVisitForm.Model.VisitResponseNew;
import com.i9930.croptrails.Test.FarmDetailsVettingActivity;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Test.model.TimelineHarvest;
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
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

public class LandingActivity3 extends AppCompatActivity {
    String TAG = "LandingActivity3";
    LandingActivity3 context;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Toolbar toolbar;
    Resources resources;
    @BindView(R.id.loader)
    GifImageView loader;
    @BindView(R.id.no_data_available)
    RelativeLayout no_data_available;
    JsonObject jsonObject;
    @BindView(R.id.offlineModeRelLayout)
    RelativeLayout offlineModeRelLayout;
    @BindView(R.id.backImage)
    ImageView backImage;
    @BindView(R.id.countTv)
    TextView countTv;
    @BindView(R.id.selectAll)
    CheckBox selectAll;
    @BindView(R.id.offlineModeImg)
    ImageView offlineModeImg;

    @BindView(R.id.totalFarmTv)
    TextView totalFarmTv;
    @BindView(R.id.freshCountTv)
    TextView freshCountTv;

    @BindView(R.id.editReqFarmsTv)
    TextView editReqFarmsTv;
    @BindView(R.id.approvedFarmsTv)
    TextView approvedFarmsTv;
    String auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing3);
        context = this;
        ButterKnife.bind(this);
        resources = getResources();
        isVetting = SharedPreferencesMethod.getBoolean(this, SharedPreferencesMethod.IS_VETTING_COMP);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LandingActivity3.this.onBackPressed();
            }
        });
//        String title=SharedPreferencesMethod.getString(context,)
        getSupportActionBar().setTitle("Saisanket");
        auth = SharedPreferencesMethod.getString(this, SharedPreferencesMethod.AUTHORIZATION);
        jsonObject = DataHandler.newInstance().getFilterData();
        toolbar.setTitle("Farms");
        DropDownCacheManager.getInstance(context).getDataByType(new DropDownCacheManager.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getFormData();
                } else {
                    try {
                        CropFormResponse data = new Gson().fromJson(dropDown.getData(), CropFormResponse.class);
                        if (data.getData() != null) {
                            LandingActivity3.this.cropFormDatumList.addAll(data.getData());
                            for (CropFormDatum cropFormDatum : data.getData()) {
                                LandingActivity3.this.cropFormDatumListMap.put(cropFormDatum.getInfoTypeId(), cropFormDatum);
                            }
                        }
                        if (data.getSuperData() != null) {
                            LandingActivity3.this.cropFormDatumListSuper.addAll(data.getSuperData());
                            for (CropFormDatum cropFormDatum : data.getSuperData()) {
                                if (cropFormDatum.getType().trim().equals("Equipment / Machinery"))
                                    LandingActivity3.this.cropFormDatumListSuperMap.put("E", cropFormDatum);
                                else
                                    LandingActivity3.this.cropFormDatumListSuperMap.put("A", cropFormDatum);
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
//        geFilterData(jsonObject,true);

        if (jsonObject != null)
            geFilterData(jsonObject, isFirstLoad);
        else {
            jsonObject = new Gson().fromJson(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FILTER_JSON), JsonObject.class);
            geFilterData(jsonObject, isFirstLoad);
        }

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
                        viewFailDialog.showSessionExpireDialog(LandingActivity3.this, response.getMessage());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.authorization_expired));
                    } else {

                        if (response != null && response.getData() != null && response.getData().size() > 0) {

                            DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.FARM_ADD_DYNAMIC);
                            DropDownT material = new DropDownT(AppConstants.CHEMICAL_UNIT.FARM_ADD_DYNAMIC, new Gson().toJson(response));
                            DropDownCacheManager.getInstance(context).addChemicalUnit(material);
                        }
                    }
                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.authorization_expired));
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
            }
        });


    }

    boolean isFirstLoad = true;

    @Override
    protected void onStart() {
        super.onStart();

        isFirstLoad = false;
    }

    String selectedPreviousVetting = "";
    FetchFarmResultNew selectedPreviousFarm;
    int selectedPreviousIndex = 0;
    private final static int REQ_CODE_FILTER = 9940;
    boolean isVetting;
    List<AdFarmDatum> fetchFarmResultVetting = new ArrayList<>();
    List<AdFarmDatum> fetchFarmResultVettingFilter = new ArrayList<>();
    VettingFarmAdapterNew farmDetailsAdapterVetting;

    int total = 0, fresh = 0, dataEntry = 0, approved = 0;

    private void geFilterData(JsonObject jsonObject, boolean showLoader) {
        total = 0;
        fresh = 0;
        dataEntry = 0;
        approved = 0;
        if (showLoader)
            loader.setVisibility(View.VISIBLE);
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        Log.e(TAG, "geFilterData REQ " + new Gson().toJson(jsonObject));
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        long currMillis = AppConstants.getCurrentMills();
        apiService.getFarmFilters(jsonObject).enqueue(new Callback<FarmResponseNew>() {
            @Override
            public void onResponse(Call<FarmResponseNew> call, Response<FarmResponseNew> response) {
                String error = null;
                Log.e(TAG, "geFilterData code" + response.code() + " \n " + call.request().url().toString());
                try {
                    if (response.isSuccessful()) {
                        Log.e(TAG, "geFilterData Res " + new Gson().toJson(response.body()));
                        if (response != null) {
                            loader.setVisibility(View.GONE);
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity3.this);
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.authorization_expired));
                            } else if (response.body().getStatus() == 0) {
                                no_data_available.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                FarmResponseNew fetchFarmData = response.body();
                                if (fetchFarmData.getResult() != null && fetchFarmData.getResult().size() > 0) {
//                                    no_data_available.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    fetchFarmResultVetting.clear();
                                    fetchFarmResultVettingFilter.clear();


                                    for (int i = 0; i < fetchFarmData.getResult().size(); i++) {
                                        FetchFarmResultNew resultNew = fetchFarmData.getResult().get(i);
                                        if (i%3==0){
                                            fetchFarmResultVetting.add(new AdFarmDatum(0));
                                        }
                                        fetchFarmResultVetting.add(new AdFarmDatum(1,resultNew));
                                        String status = resultNew.getVetting();
                                        if (status != null) {
                                            if (status.equals(AppConstants.VETTING.FRESH)) {
                                                fresh++;
                                            } else if (status.equals(AppConstants.VETTING.DATA_ENTRY)) {
                                                dataEntry++;
                                            } else if (status.equals(AppConstants.VETTING.SELECTED)) {
                                                approved++;
                                            }
                                        }


                                    }
                                    fetchFarmResultVettingFilter.addAll(fetchFarmResultVetting);
                                    totalFarmTv.setText("" + fetchFarmResultVettingFilter.size());
                                    freshCountTv.setText("" + fresh);
                                    editReqFarmsTv.setText("" + dataEntry);
                                    approvedFarmsTv.setText("" + approved);
                                    farmDetailsAdapterVetting = new VettingFarmAdapterNew(context, fetchFarmResultVettingFilter, new VettingFarmAdapterNew.ItemClickListener() {
                                        @Override
                                        public void onItemClicked(int index, FetchFarmResultNew farmResultNew, String vetting) {
                                            if (AppConstants.isValidString(vetting)) {
                                                int code = 99;
                                                selectedPreviousFarm = farmResultNew;
                                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARM_VETTING, vetting);
                                                selectedPreviousIndex = index;
                                                selectedPreviousVetting = vetting;
                                                Intent intent = null;
                                                if (vetting != null && (vetting.trim().equals(AppConstants.VETTING.SELECTED) ||
                                                        vetting.trim().equals(AppConstants.VETTING.DELINQUENT))) {
                                                    intent = new Intent(context, TestActivity.class);
                                                    code = 99;
                                                } else {
                                                    intent = new Intent(context, FarmDetailsVettingActivity.class);
                                                    code = 9930;
                                                }

                                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTUAL_AREA, farmResultNew.getActualArea() + "");
                                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, farmResultNew.getStandingArea() + "");
                                                if (farmResultNew.getExpArea() != null)
                                                    SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.EXPECTED_AREA, Double.valueOf(farmResultNew.getExpArea()));
                                                else
                                                    SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.EXPECTED_AREA, 0.0);
                                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, farmResultNew.getFarmMasterNum());
                                                startActivityForResult(intent, code);
                                            }
                                        }
                                    });
                                    try {

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    recyclerView.setAdapter(farmDetailsAdapterVetting);
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setNestedScrollingEnabled(true);
//                                    progressBar.setVisibility(View.GONE);

                                    farmDetailsAdapterVetting.setLongPressListener(new VettingFarmAdapterNew.OnLongPressListener() {
                                        @Override
                                        public void onLongPressed(int index) {
//                                            fetchFarmResultVettingFilter.get(index).setSelectedd(true);
                                            farmDetailsAdapterVetting.setLongPressed(true);
                                            countTv.setText(index + "/" + fetchFarmResultVettingFilter.size() + " selected");
                                            offlineModeRelLayout.setVisibility(View.VISIBLE);
                                        }
                                    });

                                    selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            int count = 0;
                                            if (isChecked) {
                                                for (int i = 0; i < fetchFarmResultVettingFilter.size(); i++) {
                                                    fetchFarmResultVettingFilter.get(i).getFetchFarmResultNew().setSelectedd(true);
                                                }
                                                countTv.setText(fetchFarmResultVettingFilter.size() + "/" + fetchFarmResultVettingFilter.size() + " selected");
                                                count = fetchFarmResultVettingFilter.size();
                                            } else {
                                                for (int i = 0; i < fetchFarmResultVettingFilter.size(); i++) {
                                                    fetchFarmResultVettingFilter.get(i).getFetchFarmResultNew().setSelectedd(false);
                                                }
                                                countTv.setText("0/" + fetchFarmResultVettingFilter.size() + " selected");
                                                count = 0;
                                            }
                                            try {
                                                farmDetailsAdapterVetting.setCount(count);
                                                farmDetailsAdapterVetting.notifyDataSetChanged();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    offlineModeImg.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                                            Toast.makeText(context, "Please wait...", Toast.LENGTH_SHORT).show();
                                            if (checkPermission()) {
                                                backImage.setClickable(false);
                                                backImage.setEnabled(false);
                                                selectAll.setClickable(false);
                                                selectAll.setEnabled(false);


                                                getFarmIdList();
                                            } else {
                                                requestPermission();
                                            }
                                        }
                                    });
                                    backImage.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            offlineModeRelLayout.setVisibility(View.GONE);
                                            for (int i = 0; i < fetchFarmResultVettingFilter.size(); i++) {
                                                fetchFarmResultVettingFilter.get(i).getFetchFarmResultNew().setSelectedd(false);
                                            }
                                            selectAll.setChecked(false);
                                            farmDetailsAdapterVetting.setLongPressed(false);

                                        }
                                    });

                                } else {
//                                    progressBar.setVisibility(View.GONE);
//                                    no_data_available.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                    no_data_available.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
//                            progressBar.setVisibility(View.GONE);
//                            no_data_available.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
//                                no_internet_layout.setVisibility(View.GONE);
                            Log.e(TAG, "geFilterData No Data 2");
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(LandingActivity3.this, resources.getString(R.string.failed_load_farm));
//                            progressBar.setVisibility(View.INVISIBLE);
                            //Toast.makeText(context, "Oops something went wrong. Please Reload", Toast.LENGTH_SHORT).show();
                            error = response.errorBody().string().toString();
                            Log.e(TAG, "geFilterData Error " + error);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    try {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_load_farm));
//                        progressBar.setVisibility(View.INVISIBLE);
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "geFilterData Error2" + error);
                    } catch (Exception f) {
                        f.printStackTrace();
//                        progressBar.setVisibility(View.INVISIBLE);
                    }
//                    progressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }

//                long newMillis = AppConstants.getCurrentMills();
//                long diff = newMillis - currMillis;
//                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
//                    notifyApiDelay(LandingActivity3.this, response.raw().request().url().toString(),
//                            "" + diff, internetSPeed, error, response.code());
//                }

            }

            @Override
            public void onFailure(Call<FarmResponseNew> call, Throwable t) {
               /* long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(LandingActivity3.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showDialog(LandingActivity3.this, resources.getString(R.string.failed_load_farm));
                    progressBar.setVisibility(View.INVISIBLE);
                } catch (Exception f) {
                    f.printStackTrace();
                }   */
                Log.e(TAG, "geFilterData Exception" + t.toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!SharedPreferencesMethod.getBoolean(LandingActivity3.this, SharedPreferencesMethod.OFFLINE_MODE)) {
            getMenuInflater().inflate(R.menu.search_singl_vetting, menu);
            MenuItem searchMenuItem = menu.findItem(R.id.action_search);
            MenuItem action_filter = menu.findItem(R.id.action_filter);
            MenuItem action_refresh = menu.findItem(R.id.action_refresh);
//            searchMenuItem.setVisible(false);

            if (SharedPreferencesMethod.getBoolean(LandingActivity3.this, SharedPreferencesMethod.IS_VETTING_COMP)) {
                searchMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        finish();
                        return false;
                    }
                });
            }

            if (SharedPreferencesMethod.getBoolean(LandingActivity3.this, SharedPreferencesMethod.IS_VETTING_COMP)) {
                action_refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        geFilterData(jsonObject, true);
                        return false;
                    }
                });
            }

            action_filter.setVisible(false);
//            action_refresh.setVisible(false);
        }


        return true;
    }

    Map<String, List<LatLngFarm>> coordinateHashMapToStore = new HashMap<>();
    int totalSelectedFarms = 0;
    List<FetchFarmResultNew> selectedFarmList = new ArrayList<>();

    private void getFarmIdList() {
        offlineModeImg.setClickable(false);
        offlineModeImg.setEnabled(false);
        loader.setVisibility(View.VISIBLE);
        List<String> farmIdList = new ArrayList<>();
        for (int i = 0; i < fetchFarmResultVettingFilter.size(); i++) {
            if (fetchFarmResultVettingFilter.get(i).getFetchFarmResultNew().isSelectedd()) {
                farmIdList.add(fetchFarmResultVettingFilter.get(i).getFetchFarmResultNew().getFarmMasterNum());
//                Log.e("OfflineModeAct", "Farm Selected List " + new Gson().toJson(fetchFarmResultList.get(i).getName()));
            } else {
//                Log.e("OfflineModeAct", "Farm Not Selected List " + new Gson().toJson(fetchFarmResultList.get(i).getName()));
            }
        }


        getFarmCoordinates(farmIdList);
//        getAllCalActData(farmIdList);

    }


    private void getFarmCoordinates(final List<String> farmIdList) {
        coordinateHashMapToStore.clear();
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
                                Log.e(TAG, "FarmLandingActivity3 " + response.body().getCoordinatesList().get(i).getFarmId() + " Data " + new Gson().toJson(response.body().getCoordinatesList().get(i).getData()));
                                if (response.body().getCoordinatesList().get(i).getData() != null && response.body().getCoordinatesList().get(i).getData().size() > 0) {

                                    coordinateHashMapToStore.put(response.body().getCoordinatesList().get(i).getFarmId(),
                                            response.body().getCoordinatesList().get(i).getData());
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
                    viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.authorization_expired));
                } else {
//                    loader.setVisibility(View.INVISIBLE);
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "Error getAllCoordinatesOfFarms" + error);
//                        make_all_farm_offline_button.setEnabled(true);
//                        make_all_farm_offline_button.setClickable(true);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialogForFinish(LandingActivity3.this, "Failed to load data for offline mode, Please try again later");
                        getAllCalActData(farmIdList);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<FarmsCoordinatesResponse> call, Throwable t) {

                Log.e(TAG, "getAllCoordinatesOfFarms fail " + t.toString());
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialogForFinish(LandingActivity3.this, "Failed to load data for offline mode, Please try again later");
            }
        });

    }

    // Get all done activities of selected farms
    int totalActivityZize = 0;

    private void getAllCalActData(List<String> farmIdList) {
        if (farmIdList.size() == 0) {
//            getCalendarData(farmIdList);
            setDeviceId();
        } else {
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
                        List<DoneActivityResponseNew> doneActivityList = response.body().getDoneActivityList();
                        if (doneActivityList != null && doneActivityList.size() > 0) {
                            totalActivityZize = doneActivityList.size() - 1;
                            for (int i = 0; i < doneActivityList.size(); i++) {
                                DoneActivityResponseNew doneAct = doneActivityList.get(i);
                                LandingActivity3.DownloadImagesActivity downloadImagesActivity = new LandingActivity3.DownloadImagesActivity(doneAct, i, farmIdList);
                                downloadImagesActivity.execute();
                            }
                        }
                        Log.e(TAG, "Calendar Activities Res " + new Gson().toJson(response.body()));
                        getCalendarData(farmIdList);
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.authorization_expired));
                    } else {
//                        loader.setVisibility(View.INVISIBLE);
                        try {
                            getCalendarData(farmIdList);
                            error = response.errorBody().string().toString();
                            Log.e(TAG, "Calendar Activities Error  " + error);
//                            make_all_farm_offline_button.setEnabled(true);
//                            make_all_farm_offline_button.setClickable(true);
//                            offlineModeImg.setClickable(true);
//                            offlineModeImg.setEnabled(true);
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialogForFinish(LandingActivity3.this, "Failed to load data for offline mode, Please try again later");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<CalendarActResponse> call, Throwable t) {
                    getCalendarData(farmIdList);
                }
            });
        }
    }

    private void getCalendarData(final List<String> farmIdList) {
        for (int i = 0; i < farmIdList.size(); i++) {
            final String farmId = farmIdList.get(i);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
            final String farm_id = farmId;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("comp_id", "" + comp_id);
            jsonObject.addProperty("farm_id", "" + farm_id);
            jsonObject.addProperty("user_id", "" + userId);
            jsonObject.addProperty("token", "" + token);
            Log.e(TAG, "getCalendarData " + new Gson().toJson(jsonObject));
            final boolean[] isLoaded = {false};
            long currMillis = AppConstants.getCurrentMills();
            Call<TimelineResponse> getCalenderData = apiInterface.getCalendarData(comp_id, farm_id, userId, token);
            final int finalI = i;
            getCalenderData.enqueue(new Callback<TimelineResponse>() {
                @Override
                public void onResponse(Call<TimelineResponse> call, retrofit2.Response<TimelineResponse> response) {
                    Log.e(TAG, "Calendar Status " + response.code());
                    isLoaded[0] = true;
                    String error = "";
                    if (response.isSuccessful()) {

                        TimelineResponse timelineResponse = response.body();
                        if (response.body().getStatus() == 10) {
//                            viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.authorization_expired));
                        } else if (timelineResponse.getStatus() == 1) {

                            CalendarJSON calendarJSON = new CalendarJSON();
                            calendarJSON.setCalendarData(timelineResponse.getFarmCalData());
                            Timeline timeline = new Timeline(farmId, new Gson().toJson(calendarJSON), null);
                            TimelineCacheManager.getInstance(context).addTimeline(null, timeline);
                            if (finalI == farmIdList.size() - 1)
                                fetcHarvestData(farmIdList);

                        } else {
                            if (finalI == farmIdList.size() - 1)
                                fetcHarvestData(farmIdList);
                        }
                        Log.e(TAG, "Calendar " + new Gson().toJson(response.body()));
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            if (finalI == farmIdList.size() - 1)
                                fetcHarvestData(farmIdList);
                            error = response.errorBody().string().toString();
                            Log.e(TAG, "Calendar Unsuccess " + error);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<TimelineResponse> call, Throwable t) {

                    Log.e(TAG, "Calendar Failure " + t.toString());
//                viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_load_timeline));
                    if (finalI == farmIdList.size() - 1)
                        fetcHarvestData(farmIdList);

                }
            });

        }
    }

    private void fetcHarvestData(final List<String> farmIdList) {
        try {
            for (int i = 0; i < farmIdList.size(); i++) {
                final String farmId = farmIdList.get(i);
                final int finalI = i;
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                Call<ViewHarvestDetails> callHarvestData = apiService.getHarvestDetailStatus(
                        farmId,
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
                                    viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.multiple_login_msg));
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                    //un authorized access
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.unauthorized_access));
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                    // auth token expired
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.authorization_expired));
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

                                                ViewHarvestDetails harvest = new ViewHarvestDetails();
                                                harvest.setData(harvestDetails.getData());
                                                harvest.setData1(harvestDetails.getData1());
                                                HarvestT harvestT = new HarvestT(farmId,
                                                        new Gson().toJson(harvest), "{}", "Y");
                                                HarvestCacheManager.getInstance(context).addVisits(harvestT);

                                            }
                                            SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.LAST_BAG_NO, lastBagNo + 1);
                                            if (finalI == farmIdList.size() - 1)
                                                getAllVisitsOfFarm(farmIdList);
                                        } else {
                                            SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.LAST_BAG_NO, 1);
                                            if (finalI == farmIdList.size() - 1)
                                                getAllVisitsOfFarm(farmIdList);
                                        }
                                    } else {
                                        if (finalI == farmIdList.size() - 1)
                                            getAllVisitsOfFarm(farmIdList);
                                    }
                                }
                            } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.authorization_expired));
                            } else {
                                try {
                                    if (finalI == farmIdList.size() - 1)
                                        getAllVisitsOfFarm(farmIdList);
                                    error = response.errorBody().string().toString();
                                    Log.e(TAG, "Harvest Err" + error);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Harvest Exc" + e.toString());

                        }

                    }

                    @Override
                    public void onFailure(Call<ViewHarvestDetails> call, Throwable t) {

                        Log.e(TAG, "Failure Harvest Data " + t.getMessage().toString());
                        if (finalI == farmIdList.size() - 1)
                            getAllVisitsOfFarm(farmIdList);

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void getInputCosts(final List<String> farmIdList) {
        for (int i = 0; i < farmIdList.size(); i++) {
            final String farmId = farmIdList.get(i);
            final int finalI = i;
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            // farmId="1";

            apiInterface.getInputCostList(farmId, userId, token).enqueue(new Callback<InputCostResponse>() {

                @Override
                public void onResponse(Call<InputCostResponse> call, Response<InputCostResponse> response) {
                    Log.e(TAG, "Status IC " + response.code());
                    String error = null;
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.multiple_login_msg));
                        } else {
                            String inputData = "[]";
                            String resourceData = "[]";
                            String inputDataOffline = "[]";
                            String resourceDataOffline = "[]";

                            if (response.body().getInputCostList() != null && response.body().getInputCostList().size() > 0) {
                                inputData = new Gson().toJson(response.body().getInputCostList());
                            }
                            InputCostT costT = new InputCostT(farmId, inputData, inputDataOffline, "Y");
                            InputCostCacheManager.getInstance(context).addInputCost(costT);

                            if (response.body().getResourceList() != null && response.body().getResourceList().size() > 0) {
                                resourceData = new Gson().toJson(response.body().getResourceList());
                            }
                            ResourceCostT resourceCostT = new ResourceCostT(farmId, resourceData, resourceDataOffline, "Y");
                            ResourceCacheManager.getInstance(context).addResource(resourceCostT);
                            if (finalI == farmIdList.size() - 1)
                                insertRoomFarmsNew(selectedFarmList);
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            if (finalI == farmIdList.size() - 1)
                                insertRoomFarmsNew(selectedFarmList);
                            error = response.errorBody().string();
                            Log.e(TAG, "Input Cost Err " + error);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<InputCostResponse> call, Throwable t) {
                    if (finalI == farmIdList.size() - 1)
                        insertRoomFarmsNew(selectedFarmList);
                    Log.e(TAG, "Input Cost Failure " + t.toString());
                }
            });
        }

    }

    private void insertRoomFarmsNew(List<FetchFarmResultNew> farmResultList) {
        Log.e(TAG, "Inserting data in cache");
        if (farmResultList != null && farmResultList.size() > 0) {
            int length = farmResultList.size();
            for (int i = 0; i < length; i++) {
                //downloadFileNew(i);
                if (farmResultList.get(i).isSelectedd()) {
                    FetchFarmResultNew result = farmResultList.get(i);
                    String germiData = "[]";

                    List<LatLngFarm> lst = new ArrayList<>();
                    if (coordinateHashMapToStore.containsKey(result.getFarmMasterNum()) && coordinateHashMapToStore.get(result.getFarmMasterNum()) != null) {
                        lst.addAll(coordinateHashMapToStore.get(result.getFarmMasterNum()));
                    }
                    String coords = new Gson().toJson(lst);
                    String pldOnlineJson = "[]";

                    LandingActivity3.FulDetailsAsync async = new LandingActivity3.FulDetailsAsync(result.getFarmMasterNum(), coords, germiData, pldOnlineJson,
                            i, length);
                    async.execute();

                } else {

                }
            }
        } else {

            offlineModeImg.setClickable(true);
            offlineModeImg.setEnabled(true);
            backImage.setClickable(true);
            backImage.setEnabled(true);
            selectAll.setClickable(true);
            selectAll.setEnabled(true);
            setDeviceId();
        }
        //  SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ONLINE, "offline");

    }

    private class FulDetailsAsync extends AsyncTask<String, Integer, String> {
        String farmId;
        String coords;
        String germiData;
        String pldOnlineJson;
        int index = 0, length = 0;

        public FulDetailsAsync(String farmId, String coords, String germiData, String pldOnlineJson, int index, int length) {
            this.index = index;
            this.length = length;
            this.farmId = farmId;
            this.coords = coords;
            this.germiData = germiData;
            this.pldOnlineJson = pldOnlineJson;
        }

        @Override
        protected String doInBackground(String... strings) {

            getFullDetails(farmId, coords, germiData, pldOnlineJson, index, length);

            return null;
        }
    }

    private void getFullDetails(String farmId, String coords, String germiData, String pldOnlineJson, int index, int length) {
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
                            }, farmId);
                        } catch (Exception e) {

                        }

                        //======================XXX==============================
                        //FarmDetails
//                        Farm farm=new Farm();
                        FarmData farmData = new FarmData();
                        if (details.getFarmsDetails() != null) {
                            FarmsDetails farmDetails = details.getFarmsDetails();
                            farmData.setFarmImagePath(farmDetails.getFarmPhoto());
                            farmData.setOwnerShipDoc(farmDetails.getOwnershipDoc());
                            farmData.setIsOwned(farmDetails.getIsOwned());
//                                farmData.setid(farmDetails.getId()+"");
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
//                            farmData.setsee(cropDetails.getSeedLotNo());
//                            farmData.setIsOwned(cropDetails.getSeedProvidedOn());
//                            farmData.setIsOwned(cropDetails.getSeedUnitId());
//                            farmData.setIsOwned(cropDetails.getQtySeedProvided());
//                            farmData.setIsOwned(cropDetails.getGermination());
//                            farmData.setIsOwned(cropDetails.getPopulation());
//                            farmData.setIsOwned(cropDetails.getSeedUnitId());
//                            farmData.setIsOwned(cropDetails.getExpHarvestDate());
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
                        Farm farm = new Farm(details.getFarmsDetails().getFarmMasterNum() + "", details.getFarmsDetails().getCompFarmId(),
                                details.getPersonDetails().getPersonId() + "", details.getPersonDetails().getName(), details.getPersonDetails().getMob(),
                                details.getPersonDetails().getClusterId() + "", details.getPersonDetails().getCompId() + "",
                                details.getFarmAddressDetails().getAddL1(), details.getFarmAddressDetails().getAddL2(),
                                details.getFarmAddressDetails().getVillageOrCity(),
                                details.getFarmAddressDetails().getDistrict(), details.getFarmAddressDetails().getMandalOrTehsil(),
                                details.getFarmAddressDetails().getState(), details.getFarmAddressDetails().getCountry(),
                                details.getFarmCropDetails().getExpArea() + "",
                                details.getFarmCropDetails().getActualArea() + "",
                                details.getFarmCropDetails().getIrrigationSourceId() + "", details.getFarmCropDetails().getPreviousCrop(),
                                details.getFarmCropDetails().getIrrigationTypeId() + "",
                                details.getFarmCropDetails().getSoilTypeId() + "", details.getFarmCropDetails().getSowingDate(),
                                details.getFarmCropDetails().getExpFloweringDate(), details.getFarmCropDetails().getActualFloweringDate(),
                                details.getFarmCropDetails().getExpHarvestDate(), details.getFarmCropDetails().getActualHarvestDate(),
                                details.getFarmCropDetails().getAreaHarvested() + "",
                                details.getFarmCropDetails().getStandingArea() + "",
                                "[]", "[]", null, null,
                                null, details.getFarmsDetails().getLatCord(), details.getFarmsDetails().getLongCord(),
                                null, "", null, germiData, null, null, "N", "Y", pldOnlineJson, "[]",
                                null, "N", "[]", "", details.getFarmsDetails().getFarmName(),
                                "Y", details.getFarmCropDetails().getSeasonNum() + "", "N", coords, "", "",
                                "", "", "", "");

                        String fullDetails = "{}";
                        fullDetails = new Gson().toJson(farmData);
                        farm.setFarmFullData(fullDetails);

//                        farmIdList.add(result.getFarmId());
//                        farmList.add(farm);
                        List<Farm> farmList = new ArrayList<>();
                        farmList.add(farm);
                        FarmCacheManager.getInstance(context).addFarms(new FarmNotifyInterface() {
                            @Override
                            public void onFarmLoaded(List<Farm> farmList) {

                            }

                            @Override
                            public void onFarmAdded() {

                            }

                            @Override
                            public void onFarmInsertError(Throwable e) {

                            }

                            @Override
                            public void onNoFarmsAvailable() {

                            }

                            @Override
                            public void farmDeleted() {

                            }

                            @Override
                            public void farmDeletionFaild() {

                            }
                        }, farmList);

                       /* FarmCacheManager.getInstance(context).addFarm(new FarmCacheManager.OnFarmInserted() {
                            @Override
                            public void onInserted(long id) {

                            }
                        }, farm);
*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {

                    //un authorized access
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {

                    // auth token expired
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {

                        error = response1.errorBody().string().toString();
                        Log.e(TAG, " Error FULL " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (index == length - 1)
                    setDeviceId();
            }

            @Override
            public void onFailure(Call<FarmFullDetails> call, Throwable t) {
                Log.e(TAG, "Failure FULL " + t.toString());
                if (index == length - 1)
                    setDeviceId();
            }
        });
    }

    private List<CropFormDatum> cropFormDatumList = new ArrayList<>();
    private Map<String, CropFormDatum> cropFormDatumListMap = new HashMap<>();
    private List<CropFormDatum> cropFormDatumListSuper = new ArrayList<>();
    private Map<String, CropFormDatum> cropFormDatumListSuperMap = new HashMap<>();

    private List<List<CropFormDatum>> formatAsstesList(List<AssetsData> assetsDataList) {

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
                            CropFormDatum oldDatum = null;
                            if (cropFormDatumListSuperMap.containsKey(data.getAssetType() + "".trim())) {
                                oldDatum = cropFormDatumListSuperMap.get(data.getAssetType());
                                if (oldDatum != null) {
                                    datum = new CropFormDatum();
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

    private List<FPOCrop> getCrops(List<PrevCropDatum> cropDetailsList) {
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
                                CropFormDatum datum = null;
                                CropFormDatum datumOld = null;
                                datumOld = cropFormDatumListMap.get("" + dataCropDetails.get(i).getFarmAddInfoTypeId());
                                if (datum == null)
                                    datum = new CropFormDatum();

                                if (datumOld != null) {
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

    private void setDeviceId() {
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String deviceId = AppConstants.getMacAddr();
        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.setDeviceId(deviceId, userId, token).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response1) {
                String error = null;
                isLoaded[0] = true;
                Log.e(TAG, "setDeviceId code " + response1.code());
                if (response1.isSuccessful()) {
                    StatusMsgModel villageResponse = response1.body();
                    Log.e(TAG, "setDeviceId " + new Gson().toJson(villageResponse));
                   /* try {
                        Log.e(TAG, "getChaksOfVillage Response " + response1.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
//                    Log.e(TAG, "getGeoCount Response " + new Gson().toJson(countResponse));
                    if (villageResponse.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity3.this, villageResponse.getMessage());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.authorization_expired));
                    } else {
                        String todayDateStr;
                        Date todayDate = Calendar.getInstance().getTime();
                        DateFormat df = new SimpleDateFormat(AppConstants.DATE_FORMAT_SERVER);
                        todayDateStr = df.format(todayDate);
                        Log.e(TAG, "Today date " + todayDateStr);

                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.OFFLINE_MODE_DATE, todayDateStr);
                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, true);
                        finish();
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity3.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "setDeviceId Error " + error + " Code " + response1.code() + " ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e(TAG, "setDeviceId Failure  " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
            }
        });
    }

    private void getAllVisitsOfFarm(final List<String> farmIdList) {
        for (int i = 0; i < farmIdList.size(); i++) {
            final String farmId = farmIdList.get(i);
            final int finalI = i;
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("farm_id", farmId);
            jsonObject.addProperty("user_id", userId);
            jsonObject.addProperty("token", token);
            apiInterface.getAllVisitsOfFarmModel(jsonObject).enqueue(new Callback<FarmVisitResponse>() {
                @Override
                public void onResponse(Call<FarmVisitResponse> call, Response<FarmVisitResponse> response) {
                    Log.e(TAG, "getAllVisitsOfFarm code " + response.code());
                    if (response.isSuccessful()) {
                        try {
                            Log.e(TAG, "getAllVisitsOfFarm res " + new Gson().toJson(response.body()));
                            List<FarmVisitResponse.VisitReport> visitResponseDatumList = response.body().getVisitReport();
                            for (int j = 0; j < visitResponseDatumList.size(); j++) {
                                FarmVisitResponse.VisitReport visitResponseDatum = visitResponseDatumList.get(j);
                                VisitResponseNew visitResponseNew = new VisitResponseNew();
                                DoneActivityResponseNew doneActivityResponseNew = new DoneActivityResponseNew();
                                doneActivityResponseNew.setImages(visitResponseDatum.getImages());
                                doneActivityResponseNew.setComment(visitResponseDatum.getComment());
                                doneActivityResponseNew.setMoisture("" + visitResponseDatum.getMoisture());
                                doneActivityResponseNew.setGrade(visitResponseDatum.getGrade());
                                doneActivityResponseNew.setVisitDate(visitResponseDatum.getVisitDate());
                                doneActivityResponseNew.setVisitNumber("" + visitResponseDatum.getVisitNumber());

                                List<DoneActInputs> data = new ArrayList<>();
                                List<FarmVisitResponse.AgriInput> agriInputs = visitResponseDatum.getAgriInputs();

                                if (agriInputs != null && agriInputs.size() > 0) {
                                    for (int k = 0; k < agriInputs.size(); k++) {
                                        DoneActInputs doneAct = new DoneActInputs();
                                        FarmVisitResponse.AgriInput input = agriInputs.get(k);
                                        doneAct.setFarmCalendarActivityId(null);
                                        doneAct.setFarmId(farmId);
//                                        doneAct.setFarmCalendarId(input.getca);
//                                        doneAct.setActivity();
                                        doneAct.setNarration(input.getNarration());
                                        doneAct.setVrId("" + input.getVrId());
                                        doneAct.setType(input.getType());
                                        doneAct.setOtherAgriInput(input.getOtherAgriInput());
                                        doneAct.setName(input.getName());

                                        data.add(doneAct);
                                    }
                                }

                                doneActivityResponseNew.setData(data);
                                doneActivityResponseNew.setFarmId(farmId);
                                visitResponseNew.setShowResponse(doneActivityResponseNew);
                                visitResponseNew.setMoisture("" + visitResponseDatum.getMoisture());
                                visitResponseNew.setFarmGrade(visitResponseDatum.getGrade());
                                visitResponseNew.setComment(visitResponseDatum.getComment());
                                visitResponseNew.setFarmId(farmId);
                                visitResponseNew.setVisitReportId("" + visitResponseDatum.getVisitReportId());
                                visitResponseNew.setEffectiveArea("" + visitResponseDatum.getEffectiveArea());
                                visitResponseNew.setVisitNumber("" + visitResponseDatum.getVisitNumber());
//                                visitResponseNew.setVisitImages(visitResponseDatum.getImages());

                                LandingActivity3.DownloadImagesVisit downloadImagesNew = new LandingActivity3.DownloadImagesVisit(visitResponseNew);
                                downloadImagesNew.execute();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (finalI == totalSelectedFarms - 1) {
                            getInputCosts(farmIdList);
                        }
                    } else {
                        try {
                            Log.e(TAG, "getAllVisitsOfFarm err " + response.errorBody().string());
                            if (finalI == totalSelectedFarms - 1) {
                                getInputCosts(farmIdList);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<FarmVisitResponse> call, Throwable t) {
                    Log.e(TAG, "getAllVisitsOfFarm fail " + t.toString());
                    if (finalI == totalSelectedFarms - 1) {
                        getInputCosts(farmIdList);
                    }
                }
            });
        }
    }

    public class DownloadImagesVisit extends AsyncTask<String, Integer, String> {
        VisitResponseNew response;

        public DownloadImagesVisit(VisitResponseNew response) {
            this.response = response;
            Log.e(TAG, "DownloadImagesVisit " + new Gson().toJson(response));
        }

        @Override
        protected String doInBackground(String... stringss) {

            if (response.getShowResponse() != null) {

                if (response.getShowResponse().getImages() != null) {
                    for (int i = 0; i < response.getShowResponse().getImages().size(); i++) {
                        String filename;
                        filename = "" + response.getVisitReportId();
                        filename = filename + "_" + i + ".png";
                        String fullPath = Environment.getExternalStorageDirectory() + AppConstants.getImagePath(AppConstants.FILE_PATH_TYPE.VIST, LandingActivity3.this) + "/" + filename;
                        Log.e(TAG, "DownloadImagesVisit " + fullPath);

                        String uRl = response.getShowResponse().getImages().get(i).getImgLink();
                        if (AppConstants.isValidString(uRl)) {


                            File direct = new File(Environment.getExternalStorageDirectory() + AppConstants.getImagePath(null, LandingActivity3.this));
                            if (!direct.exists()) {
                                direct.mkdirs();
                            }
                            File path = new File(Environment.getExternalStorageDirectory() + AppConstants.getImagePath(AppConstants.FILE_PATH_TYPE.VIST, LandingActivity3.this));
                            File oldFile = new File(fullPath);

                            if (!path.exists()) {
                                path.mkdirs();
                            }
                            if (!oldFile.exists()) {
//                                oldFile.mkdir();
                                URL url = null;
                                try {
//                                    url = new URL(uRl);

                                    String finalFilename = filename;
                                    Log.e(TAG, "DownloadImagesVisit " + uRl);

                                    /*Glide.with(context)
                                            .load(uRl)
                                            .into(new CustomTarget<Drawable>() {
                                                @Override
                                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                                                    Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
//                                                    Toast.makeText(MainActivity.this, "Saving Image...", Toast.LENGTH_SHORT).show();
                                                    saveImage(bitmap, path, finalFilename);
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                                }

                                                @Override
                                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                                    super.onLoadFailed(errorDrawable);

//                                                    Toast.makeText(MainActivity.this, "Failed to Download Image! Please try again later.", Toast.LENGTH_SHORT).show();
                                                }
                                            });*/
                                    url = new URL(uRl);
                                    Bitmap bm = null;
                                    try {
                                        bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    //Create Path to save Image

                                    if (!path.exists()) {
                                        path.mkdirs();
                                    }

                                    File imageFile = new File(path, filename); // Imagename.png
                                    FileOutputStream out = null;
                                    try {
                                        out = new FileOutputStream(imageFile);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        bm.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
                                        out.flush();
                                        out.close();
                                        // Tell the media scanner about the new file so that it is
                                        // immediately available to the user.
                                        MediaScannerConnection.scanFile(LandingActivity3.this, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                                            public void onScanCompleted(String path, Uri uri) {

                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e(TAG, "DownloadImagesVisit " + fullPath + " already exist");
                            }
                        }
                        response.getShowResponse().getImages().get(i).setImgLink(fullPath);
                    }
                }
            }
            Visit vr = new Visit(response.getVisitReportId(),
                    new Gson().toJson(response), "Y", null, "N", null, response.getFarmId());
            VrCacheManager.getInstance(context).addVisits(vr);


            return null;
        }
    }


    private void saveImage(Bitmap image, File storageDir, String imageFileName) {

        boolean successDirCreated = false;
        if (!storageDir.exists()) {
            successDirCreated = storageDir.mkdir();
        } else {
            successDirCreated = true;
        }
        if (successDirCreated) {
            File imageFile = new File(storageDir, imageFileName);
            String savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.close();
//                Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
//                Toast.makeText(MainActivity.this, "Error while saving image!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "Failed to make folder!", Toast.LENGTH_SHORT).show();
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
                            filename = "";
                            filename = "" + response.getFarmCalActivityId() + "_" + i + ".png";
                            String fullPath = AppConstants.getImagePath(AppConstants.FILE_PATH_TYPE.ACTIVITY, LandingActivity3.this) + "/" + filename + ".png";
                            uRl = response.getImages().get(i).getImgLink();
                            File direct = new File(Environment.getExternalStorageDirectory() + "/" + resources.getString(R.string.app_name));
                            response.getImages().get(i).setImgLink(fullPath);
                            if (!direct.exists()) {
                                direct.mkdirs();
                            }
                            File directnew = new File(Environment.getExternalStorageDirectory() + "/" + resources.getString(R.string.app_name) + "/activities");
                            if (!directnew.exists()) {
                                directnew.mkdirs();
                            }
                            File oldFile = new File(fullPath);
                            if (!oldFile.exists()) {
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
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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

            return null;
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(LandingActivity3.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(LandingActivity3.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
            ActivityCompat.requestPermissions(LandingActivity3.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }


}