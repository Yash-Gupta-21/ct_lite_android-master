package com.i9930.croptrails.Setting;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
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
import com.i9930.croptrails.AddFarm.AddFarmActivity;
import com.i9930.croptrails.AddFarm.Model.FarmCrop;
import com.i9930.croptrails.AddFarm.Model.FarmerSpinnerData;
import com.i9930.croptrails.AddFarm.Model.FetchFarmerResponse;
import com.i9930.croptrails.AddFarm.Model.SupervisorListResponse;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.AreaUnit.SelectAreaUnitActivity;
import com.i9930.croptrails.BuildConfig;
import com.i9930.croptrails.ClusterSelection.ClusterSelectActivityActivity;
import com.i9930.croptrails.CommonClasses.Cluster;
import com.i9930.croptrails.CommonClasses.ClusterResponse;
import com.i9930.croptrails.CommonClasses.DDResponseNew;
import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormResponse;
import com.i9930.croptrails.CommonClasses.ParamData;
import com.i9930.croptrails.CommonClasses.PldReason;
import com.i9930.croptrails.CommonClasses.Season.SeasonResponse;
import com.i9930.croptrails.CompSelect.CompSelectActivity;
import com.i9930.croptrails.CompSelect.Model.CompanyResponse;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.IrrigationSource;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.IrrigationType;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.Season;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.SoilType;
import com.i9930.croptrails.InternetSpeed.InternetSpeedActivity;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Landing.Models.FetchFarmData;
import com.i9930.croptrails.Landing.Models.FetchFarmSendData;
import com.i9930.croptrails.Login.Model.CompParamDatum;
import com.i9930.croptrails.Login.Model.CompParamResponse;
import com.i9930.croptrails.OfflineMode.Model.FarmIdArray;
import com.i9930.croptrails.OfflineMode.Model.OfflineResponse;
import com.i9930.croptrails.ResetPassword.ResetPasswordActivity;
import com.i9930.croptrails.Landing.Fragments.Model.ProfileReciveMainData;
import com.i9930.croptrails.Landing.Fragments.Model.ResultProfile;
import com.i9930.croptrails.Language.LanguageSelectActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Login.LoginActivity;
import com.i9930.croptrails.Profile.ProfileActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.AddFarmCache.AddFarmCCacheManager;
import com.i9930.croptrails.RoomDatabase.AppDatabase;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegCacheManager;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegModel;
import com.i9930.croptrails.RoomDatabase.CompRegistry.DatabaseResInterface;
import com.i9930.croptrails.RoomDatabase.CompRegistry.Model.CompRegResult;
import com.i9930.croptrails.RoomDatabase.CompRegistry.Model.ResponseCompReg;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownCacheManager;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownCacheManager2;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownT2;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmCacheManager;
import com.i9930.croptrails.RoomDatabase.Gps.GetSetInterface;
import com.i9930.croptrails.RoomDatabase.Gps.GpsCacheManager;
import com.i9930.croptrails.RoomDatabase.Gps.GpsLog;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestCacheManager;
import com.i9930.croptrails.RoomDatabase.Timeline.TimelineCacheManager;
import com.i9930.croptrails.RoomDatabase.Visit.VrCacheManager;
import com.i9930.croptrails.RoomDatabase.VisitStructure.VisitCacheManager;
import com.i9930.croptrails.ServiceAndBroadcasts.Model.RecieveResponseGpsBack;
import com.i9930.croptrails.ServiceAndBroadcasts.Model.SendGpsArray;
import com.i9930.croptrails.SoilSense.BluetoothCommunicationActivity;
import com.i9930.croptrails.SoilSense.DeviceListActivity;
import com.i9930.croptrails.SubmitActivityForm.Model.CompAgriResponse;
import com.i9930.croptrails.SubmitInputCost.Model.ExpenseDataDD;
import com.i9930.croptrails.SubmitInputCost.Model.ResourceDataDD;
import com.i9930.croptrails.SubmitPld.Model.PldReasonResponse;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, DatabaseResInterface, GetSetInterface {

    String TAG = "SettingActivity";
    CardView card_user_profile;
    LinearLayout add_farm;
    LinearLayout reset_password;
    RelativeLayout setting_layout;
    CircleImageView profile_pic;
    TextView user_name;
    TextView fathers_name;
    TextView signout;
    TextView feedback;
    TextView share;
    TextView rate_us;
    TextView privacy;
    TextView t_and_c;
    TextView change_language_tv;
    Resources resources;
    Toolbar mActionBarToolbar;
    GetSetInterface getSetInterface;
    List<String> imagePathList;
    String img_linlk = null;
    @BindView(R.id.layoutAddExistingFarm)
    LinearLayout layoutAddExistingFarm;
    //    ProgressDialog progressDialog;
    Context context;
    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.fingerPrintLockSwitch)
    Switch fingerPrintLockSwitch;
    @BindView(R.id.fingerPrintLoginSwitch)
    Switch fingerPrintLoginSwitch;
    @BindView(R.id.loader)
    GifImageView loader;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.changeCompanyLayout)
    LinearLayout changeCompanyLayout;

    String[] lati_cord, longi_cord, enter_date, sv_id, time;
    LinearLayout appTour;

    @BindView(R.id.layout_internet_speed)
        LinearLayout layout_internet_speed;

    @BindView(R.id.layout_refresh)
    LinearLayout layout_refresh;
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    int flag1 = 0;
    int flag2 = 0;
    @BindView(R.id.area_unit_layout)
    LinearLayout area_unit_layout;

    @BindView(R.id.layout_bt)
    LinearLayout layout_bt;
    @BindView(R.id.changeClusterLayout)
    LinearLayout changeClusterLayout;
    @BindView(R.id.view99)
    View view99;

    private void hideSwitchCluster(){
        changeClusterLayout.setVisibility(View.GONE);
        view99.setVisibility(View.GONE);
    }

    AdView mAdView;
    private void loadAds(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
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
        setContentView(R.layout.activity_setting);
        context = this;
        loadAds();
        getSetInterface = this;
        ButterKnife.bind(this);
        TextView speed=findViewById(R.id.speed);
        ConnectivityUtils.checkSpeed(this,speed,true,50);
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();

        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(getString(R.string.setting_activity_title));
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        setting_layout = findViewById(R.id.setting_layout);
        card_user_profile = findViewById(R.id.card_user_profile);
        add_farm = findViewById(R.id.layout_addfarm);
        reset_password = findViewById(R.id.layout_reset_password);
        signout = findViewById(R.id.signout);
        profile_pic = findViewById(R.id.profile_pic);
        user_name = findViewById(R.id.username_tv);
        fathers_name = findViewById(R.id.fathersname_tv);

        feedback = findViewById(R.id.feedback);
        share = findViewById(R.id.share);
        rate_us = findViewById(R.id.rate_us);
        privacy = findViewById(R.id.privacy_policy);
        t_and_c = findViewById(R.id.t_and_c);
        change_language_tv = findViewById(R.id.change_language_tv);
        appTour = findViewById(R.id.layout_app_tour);
        version.setText(resources.getString(R.string.version_label) + " " + BuildConfig.VERSION_NAME);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String noti_sch_id = extras.getString("id");
            if (noti_sch_id != null) {
                SetNotificationAsRead task = new SetNotificationAsRead();
                task.execute(noti_sch_id);
            }

        }

        if (SharedPreferencesMethod2.getBoolean(context, SharedPreferencesMethod2.IS_ALLOW_FINGER)) {
            fingerPrintLoginSwitch.setChecked(true);
            flag1 = 1;
        } else {
            fingerPrintLoginSwitch.setChecked(false);
            flag1 = 0;
        }

        if (SharedPreferencesMethod2.getBoolean(context, SharedPreferencesMethod2.IS_APP_LOCK)) {
            fingerPrintLockSwitch.setChecked(true);
            flag2 = 1;
        } else {
            fingerPrintLockSwitch.setChecked(false);
            flag2 = 0;
        }

        area_unit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SelectAreaUnitActivity.class);
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

        /*if (SharedPreferencesMethod.getBoolean(context,SharedPreferencesMethod.HAS_MULTI_COMP)){
            changeCompanyLayout.setVisibility(View.VISIBLE);
            changeCompanyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, CompSelectActivity.class));
                }
            });
        }else {
            changeCompanyLayout.setVisibility(View.GONE);
        }*/
        getCompanyList();


        fingerPrintLockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_APP_LOCK, true);
                } else {
                    SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_APP_LOCK, false);
                }
                SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_FINGER_DONT_ASK, true);
            }
        });

        fingerPrintLoginSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_FINGER_SELECTED, true);
                } else {
                    SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_FINGER_SELECTED, false);
                }
                SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_FINGER_DONT_ASK, true);
            }
        });

        layout_internet_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, InternetSpeedActivity.class));
            }
        });

        String role=SharedPreferencesMethod.getString(context,SharedPreferencesMethod.LOGIN_TYPE);

        if (role==null||role.equals(AppConstants.ROLES.ADMIN)||role.equals(AppConstants.ROLES.SUPER_ADMIN)){
            changeClusterLayout.setVisibility(View.VISIBLE);
            view99.setVisibility(View.VISIBLE);
            changeClusterLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ClusterSelectActivityActivity.class);

                    ActivityOptions options = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                    }
                    //intent.putExtra("activity","login");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivity(intent, options.toBundle());
                        finish();
                        finishAffinity();
                    } else {
                        startActivity(intent);
                        finish();
                        finishAffinity();
                    }
                }
            });
        }else {
            hideSwitchCluster();
        }
        layout_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_refresh.setClickable(false);
                layout_refresh.setEnabled(false);
                loader.setVisibility(View.VISIBLE);

                if (role==AppConstants.ROLES.FARMER){
                    getFormData();
                }else if (role==AppConstants.ROLES.SUPERVISOR){
                    getFarmerList();
                }else {
                    getClusterOfCompany();
                }
                getCompParams();


            }
        });

        layout_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serverIntent = new Intent(SettingActivity.this, DeviceListActivity.class);
                startActivityForResult(serverIntent, BluetoothCommunicationActivity.REQUEST_CONNECT_DEVICE);
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BluetoothCommunicationActivity.REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    Intent intent = new Intent();
                    intent.putExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS, address);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                break;

        }
    }

    private void getCompanyList() {
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String mob = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NUMBER);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        apiInterface.getCompanyList(mob, userId, token).enqueue(new Callback<CompanyResponse>() {
            @Override
            public void onResponse(Call<CompanyResponse> call, Response<CompanyResponse> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "CompResponse " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 1) {

                        if (response.body().getCompanyDatumList() != null && response.body().getCompanyDatumList().size() > 1) {
                            changeCompanyLayout.setVisibility(View.VISIBLE);
                            changeCompanyLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(context, CompSelectActivity.class));
                                }
                            });

                        } else {
                            changeCompanyLayout.setVisibility(View.GONE);
                        }

                    } else {
                        changeCompanyLayout.setVisibility(View.GONE);
                    }

                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    changeCompanyLayout.setVisibility(View.GONE);

                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    changeCompanyLayout.setVisibility(View.GONE);

                } else {
                    changeCompanyLayout.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<CompanyResponse> call, Throwable t) {
                changeCompanyLayout.setVisibility(View.GONE);

            }
        });
    }
    @Override
    public void onContactsLoaded(List<GpsLog> gpsLogList) {
        loader.setVisibility(View.VISIBLE);
        if (gpsLogList != null && gpsLogList.size() > 0) {
            lati_cord = new String[gpsLogList.size()];
            longi_cord = new String[gpsLogList.size()];
            enter_date = new String[gpsLogList.size()];
            sv_id = new String[gpsLogList.size()];
            time = new String[gpsLogList.size()];
            for (int i = 0; i < gpsLogList.size(); i++) {
                //JSONObject contact = new JSONObject();
                lati_cord[i] = gpsLogList.get(i).getLat_cord().trim();
                longi_cord[i] = gpsLogList.get(i).getLong_cord().trim();
                sv_id[i] = gpsLogList.get(i).getSv_id().trim();
                enter_date[i] = gpsLogList.get(i).getEnter_date().trim();
                time[i] = gpsLogList.get(i).getTime().trim();
            }
            String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            SendGpsArray sendGpsArray = new SendGpsArray();
            sendGpsArray.setLat_cord(lati_cord);
            sendGpsArray.setLong_cord(longi_cord);
            sendGpsArray.setEnter_date(enter_date);
            sendGpsArray.setSv_id(sv_id);
            sendGpsArray.setTime(time);
            sendGpsArray.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            sendGpsArray.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            Log.e(TAG, "GPS DATA " + new Gson().toJson(sendGpsArray));
            Call<RecieveResponseGpsBack> statusMsgModelCall = apiService.getStatusMsgforUploadGpsInBack(sendGpsArray);
            statusMsgModelCall.enqueue(new Callback<RecieveResponseGpsBack>() {
                @Override
                public void onResponse(Call<RecieveResponseGpsBack> call, Response<RecieveResponseGpsBack> response) {
                    if (response.isSuccessful()) {

                      /*  try {
                            Log.e(TAG,"Data "+response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/

                        if (response != null) {
                            RecieveResponseGpsBack statusMsgModel = response.body();
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.multiple_login_msg));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.authorization_expired));
                            } else if (statusMsgModel.getStatus() != 0) {
                                Log.e(TAG, "Successfull" + statusMsgModel.getMsg());
                                Log.e(TAG, "Successfull" + statusMsgModel.getResult() + "  entries");
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("croptrails");
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("comp_" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                                AddFarmCCacheManager.getInstance(context).deleteAllData();
                                GpsCacheManager.getInstance(context).deleteAllGps(getSetInterface);
                                SharedPreferencesMethod.clear(SettingActivity.this);
                                CompRegCacheManager.getInstance(SettingActivity.this).deleteAllData();
                                FarmCacheManager.getInstance(context).deleteAllFarms();
                                TimelineCacheManager.getInstance(context).deleteAllTimeline();
                                HarvestCacheManager.getInstance(context).deleteAllHarvests();
                                VrCacheManager.getInstance(context).deleteAllVisits();
                                VisitCacheManager.getInstance(context).deleteAllVisitStructure();
                                DropDownCacheManager.getInstance(context).deleteAllChemicalUnit();
                                loader.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);
                                SharedPreferencesMethod.setBoolean(SettingActivity.this, SharedPreferencesMethod.LOGIN, false);
                                Intent intent = new Intent(SettingActivity.this, LoginActivity
                                        .class);
                                ActivityOptions options = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    options = ActivityOptions.makeCustomAnimation(SettingActivity.this, R.anim.fade_in, R.anim.fade_out);
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
                            } else {
                                loader.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);
                                Toasty.error(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            loader.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                            Toasty.error(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            loader.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                            Toasty.error(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Not Successfull" + response.errorBody().string().toString() + "   Status" + response.code() + "  Message" + response.message());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RecieveResponseGpsBack> call, Throwable t) {
                    Log.e(TAG, "On Failure" + t.toString());
                    loader.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    Toasty.error(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();

                }
            });

        } else {
//            progressDialog.cancel();
            loader.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
//            GpsCacheManager.getInstance(context).deleteAllGps(getSetInterface);
//            SharedPreferencesMethod.clear(SettingActivity.this);
//            CompRegCacheManager.getInstance(SettingActivity.this).deleteAllData();
//            FarmCacheManager.getInstance(context).deleteAllFarms();
//            TimelineCacheManager.getInstance(context).deleteAllTimeline();
//            HarvestCacheManager.getInstance(context).deleteAllHarvests();
//            VrCacheManager.getInstance(context).deleteAllVisits();
//            VisitCacheManager.getInstance(context).deleteAllVisitStructure();
//            progressDialog.cancel();
            try {
                AppDatabase.eraseDB(SettingActivity.this,null);
            }catch (Exception e){}
            SharedPreferencesMethod.setBoolean(SettingActivity.this, SharedPreferencesMethod.LOGIN, false);
            Intent intent = new Intent(SettingActivity.this, LoginActivity
                    .class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(SettingActivity.this, R.anim.fade_in, R.anim.fade_out);
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
            Log.e(TAG, "No Gps to upload");
        }
    }


    @Override
    public void onContactsAdded() {

    }

    @Override
    public void onDataNotAvailable() {

    }


    class SetNotificationAsRead extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            String ids = strings[0];
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            apiInterface.setNotificationAsRead(ids, userId, token).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            Log.e(TAG, "Set As Read Res " + response.body().string().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.authorization_expired));
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
    protected void onStart() {
        context = this;
        //change_language_tv.setVisibility(View.GONE);
        super.onStart();
        card_user_profile.setOnClickListener(this);
        add_farm.setOnClickListener(this);

        reset_password.setOnClickListener(this);
        signout.setOnClickListener(this);
        layoutAddExistingFarm.setOnClickListener(this::onClick);
        feedback.setOnClickListener(this::onClick);
        share.setOnClickListener(this);
        rate_us.setOnClickListener(this);
        privacy.setOnClickListener(this);
        t_and_c.setOnClickListener(this);
        change_language_tv.setOnClickListener(this);
        appTour.setOnClickListener(this);
        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.FARMER)) {
            reset_password.setVisibility(View.GONE);

            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.FARMER_ALLOWED_ADD_FARM)) {
                add_farm.setVisibility(View.GONE);
            } else {
                add_farm.setVisibility(View.GONE);
            }

        } else {
            reset_password.setVisibility(View.VISIBLE);
            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.SUPERVISOR_ALLOWED_ADD_FARM)) {
                add_farm.setVisibility(View.GONE);
            } else {
                add_farm.setVisibility(View.GONE);//INVISIBLE
            }
        }
        loadProfileData();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_user_profile: {
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("user_name", user_name.getText().toString());
                intent.putExtra("img_link", img_linlk);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(SettingActivity.this, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }

                break;
            }
            case R.id.layout_addfarm: {
                Intent intent = new Intent(getApplicationContext(), AddFarmActivity.class);
                intent.putExtra(AppConstants.ADD_FARM.KEY, AppConstants.ADD_FARM.NEW_FARM_AND_FARMER);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(SettingActivity.this, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
                break;
            }
            case R.id.layoutAddExistingFarm: {
                Intent intent = new Intent(getApplicationContext(), AddFarmActivity.class);
                intent.putExtra(AppConstants.ADD_FARM.KEY, AppConstants.ADD_FARM.ADD_EXISTING_FARM);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(SettingActivity.this, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
                break;
            }
            case R.id.layout_reset_password: {
                Intent intent = new Intent(this, ResetPasswordActivity.class);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(SettingActivity.this, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
                break;
            }
            case R.id.signout: {
                if (!SharedPreferencesMethod.getBoolean(this, SharedPreferencesMethod.OFFLINE_MODE)) {
                    new AlertDialog.Builder(this)
                            .setMessage(resources.getString(R.string.signout_alert_dialog_message))
                            .setCancelable(false)
                            .setPositiveButton((resources.getString(R.string.signout_alert_dialog_yes)), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    GpsCacheManager.getInstance(context).getContacts(getSetInterface);

                                }
                            })
                            .setNegativeButton(getString(R.string.signout_alert_dialog_no), null)
                            .show();
                } else {
                    final Snackbar snackbar = Snackbar.make(setting_layout, resources.getString(R.string.offline_mode_logout_error), Snackbar.LENGTH_INDEFINITE);
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
                break;
            }
            case R.id.share: {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String share_body = "https://play.google.com/store/apps/details?id=com.i9930.croptrails";
                //String share_body = "";
                intent.putExtra(Intent.EXTRA_TEXT, share_body);
                startActivity(Intent.createChooser(intent, resources.getString(R.string.share_title)));
                break;
            }
            case R.id.rate_us: {
                Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }
                break;
            }
            case R.id.privacy_policy: {
                break;
            }
            case R.id.t_and_c: {
                break;
            }
            case R.id.change_language_tv: {
                Intent intent = new Intent(getApplicationContext(), LanguageSelectActivity.class);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(SettingActivity.this, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
                break;
            }

            case R.id.layout_app_tour: {
                Intent appTourIntent = new Intent(getApplicationContext(), LandingActivity.class);
                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.APPTOUR, true);
                startActivity(appTourIntent);
                finishAffinity();
                break;

            }

        }

    }

    @Override
    public void onGetAllCompRegData(List<CompRegModel> compRegModelList) {

    }

    @Override
    public void onCompRegDataAdded() {

    }

    @Override
    public void onAllDataDeleted() {
        Log.e(TAG, "Data Deleted");

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

    protected void loadProfileData() {
//        progressDialog.show();
        loader.setVisibility(View.VISIBLE);
        try {
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            Log.e(TAG, "PersonId " + SharedPreferencesMethod.getString(SettingActivity.this, SharedPreferencesMethod.PERSON_ID));
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            apiService.getProfileDataforSupervisor(SharedPreferencesMethod.getString(SettingActivity.this, SharedPreferencesMethod.PERSON_ID),
                    SharedPreferencesMethod.getString(SettingActivity.this, SharedPreferencesMethod.COMP_ID), userId, token)
                    .enqueue(new Callback<ProfileReciveMainData>() {
                        @Override
                        public void onResponse(Call<ProfileReciveMainData> call, Response<ProfileReciveMainData> response) {
                            Log.e(TAG,"profile code "+response.code());
                            if (response.isSuccessful()) {
                                Log.e(TAG,"profile code "+new Gson().toJson(response.body()));
                                if (response.body().getStatus() == 10) {
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.multiple_login_msg));
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                    //un authorized access
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.unauthorized_access));
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                    // auth token expired
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.authorization_expired));
                                } else {
//                                    progressDialog.cancel();
                                    loader.setVisibility(View.GONE);
                                    scrollView.setVisibility(View.VISIBLE);
                                    Log.e(TAG, new Gson().toJson(response.body()));
                                    ResultProfile resultProfile = response.body().getResult();
                                    if (resultProfile != null) {
                                        SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.UNAME, resultProfile.getName());
                                        SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.IMAGE, resultProfile.getImgLink());
                                        if (resultProfile.getImgLink() != null) {
                                            if (!resultProfile.getImgLink().equals("")) {
                                                img_linlk = resultProfile.getImgLink();
                                                URL url = null;
//                                                try {
//                                                    url = new URL(img_linlk);
//                                                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                                                    DataHandler.newInstance().setMyBitmapForSvProfile(bmp);
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                }
                                                String img_url = resultProfile.getImgLink();
                                              /*  if (img_url != null && !TextUtils.isEmpty(img_url) && img_url.contains("croptrailsimages.s3")) {
                                                    ShowAwsImage.getInstance(context).downloadFile(Uri.parse(img_url), profile_pic, img_url);
                                                } else {*/
                                                Uri uriprofile = Uri.parse(resultProfile.getImgLink());
                                                //Picasso.with(context).load(uriprofile).into(holder.expense_image);
                                                RequestOptions options = new RequestOptions()
                                                        .placeholder(R.drawable.ic_person_white_24dp)
                                                        .error(R.drawable.ic_person_white_24dp);
                                                Glide.with(getApplicationContext()).load(uriprofile).apply(options).into(profile_pic);
                                                // }
                                            }
                                        } else {
                                            profile_pic.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_person_white_24dp));
                                        }

                                        if (resultProfile.getName() != null) {
                                            user_name.setText(response.body().getResult().getName());
                                        } else {
                                            user_name.setText(getString(R.string.setting_no_fathername));
                                        }

                                        if (resultProfile.getFatherName() != null) {
                                            fathers_name.setText(resultProfile.getFatherName());
                                        } else {
                                            fathers_name.setText(resources.getString(R.string.setting_no_fathername));
                                        }
                                    }
                                }
                            } else {
                                ResponseBody responseBody = response.errorBody();
                                try {
                                    //  Toast.makeText(SettingActivity.this,resources.getString(R.string.no_response_plz_reload), Toast.LENGTH_SHORT).show();
                                    Toasty.error(SettingActivity.this, resources.getString(R.string.no_response_plz_reload), Toast.LENGTH_LONG, true).show();
//                                    progressDialog.cancel();
                                    loader.setVisibility(View.GONE);
                                    scrollView.setVisibility(View.VISIBLE);
                                    Log.e(TAG, "profile   "+responseBody.string().toString() + "   Status" + response.code() + "  Message" + response.message());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ProfileReciveMainData> call, Throwable t) {
                            Log.e(TAG, "profile "+t.toString());
                            //Toast.makeText(SettingActivity.this,resources.getString(R.string.no_response_plz_reload), Toast.LENGTH_SHORT).show();
                            Toasty.error(SettingActivity.this, resources.getString(R.string.no_response_plz_reload), Toast.LENGTH_LONG, true).show();
//                            progressDialog.cancel();
                            loader.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
//        progressDialog.cancel();
        loader.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
    }

    //Refresh
    List<PldReason> pldReasons = new ArrayList<>();
    List<Cluster> clusterList = new ArrayList<>();
    List<FarmerSpinnerData> farmerDataList = new ArrayList<>();

    private void getClusterOfCompany() {

        clusterList.clear();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        apiInterface.getClusters(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID)).enqueue(new Callback<ClusterResponse>() {
            @Override
            public void onResponse(Call<ClusterResponse> call, Response<ClusterResponse> response) {
                String error = null;
                if (response.isSuccessful()) {
                    Log.e(TAG+"L","Cluster "+new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 1) {

                        if (response.body().getClusterList() != null) {
                            clusterList.addAll(response.body().getClusterList());
                        }
                        DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST);
                        DropDownT clusters = new DropDownT(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST, new Gson().toJson(clusterList));
                        DropDownCacheManager.getInstance(context).addChemicalUnit(clusters);

                    } else if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(SettingActivity.this);
                        //progressDialog.cancel();
                    } else {
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        //progressDialog.cancel();
                        error = response.errorBody().string();
                        Log.e(TAG+"L", "Cluster Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                getFarmerList();
            }

            @Override
            public void onFailure(Call<ClusterResponse> call, Throwable t) {
                Log.e(TAG+"L", "Cluster Fail " + t.toString());
                getFarmerList();
            }
        });


    }
    private void getFarmerList() {
        farmerDataList.clear();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String clusterId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        apiInterface.getFarmerList(compId, clusterId, userId, token).enqueue(new Callback<FetchFarmerResponse>() {
            @Override
            public void onResponse(Call<FetchFarmerResponse> call, Response<FetchFarmerResponse> response) {
                String error = null;
                if (response.isSuccessful()) {
                    Log.e(TAG+"L", "Farmer List Res " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == 1) {
                        if (response.body().getFarmerDataList() != null) {
                            farmerDataList.addAll(response.body().getFarmerDataList());
                        }
                        DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.FARMER_LIST);
                        DropDownT farmers = new DropDownT(AppConstants.CHEMICAL_UNIT.FARMER_LIST, new Gson().toJson(farmerDataList));
                        DropDownCacheManager.getInstance(context).addChemicalUnit(farmers);
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG+"L", "Farmer List Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                getFormData();
            }

            @Override
            public void onFailure(Call<FetchFarmerResponse> call, Throwable t) {

                Log.e(TAG+"L", "Farmer List failure " + t.toString());
                getFormData();
            }
        });

    }

    private void getCompParams() {
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String type = "gps_accuracy";
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        Log.e(TAG, "Type " + type + "   CompId " + compId + "  UserId " + userId + "   Token " + token);
        ApiInterface expApiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        Call<CompParamResponse> compRegCall = expApiInterface.getCompParam(compId, type, userId, token);
        compRegCall.enqueue(new Callback<CompParamResponse>() {
            @Override
            public void onResponse(Call<CompParamResponse> call, Response<CompParamResponse> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "CompParam Res " + new Gson().toJson(response.body()));
                    List<CompParamDatum> data =response.body().getData();
                    Float f=2.0f;
                    if (data!=null&&data.size()>0){
                        CompParamDatum datum=data.get(0);
                        String accuracy=datum.getValue();

                        if (AppConstants.isValidString(accuracy)){
                            try {
                                f=Float.valueOf(accuracy);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        SharedPreferencesMethod.setFloatPref(context, SharedPreferencesMethod.COMP_AUTO_ACCURACY, f);
                    }else {
                        SharedPreferencesMethod.setFloatPref(context, SharedPreferencesMethod.COMP_AUTO_ACCURACY, f);
                    }

                } else {
                    try {
                        Log.e(TAG, "CompParam rError " + response.errorBody().string());
                        SharedPreferencesMethod.setFloatPref(context, SharedPreferencesMethod.COMP_AUTO_ACCURACY, 2.0f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                getCompAgriInputs();
            }
            @Override
            public void onFailure(Call<CompParamResponse> call, Throwable t) {
                Log.e(TAG,"CompParam Fail "+ t.toString());
                SharedPreferencesMethod.setFloatPref(context, SharedPreferencesMethod.COMP_AUTO_ACCURACY, 2.0f);
                getCompAgriInputs();
            }
        });
    }


    private void getFormData() {
        ParamData paramData = new ParamData();
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        Log.e(TAG, "Sending New Param i  AND type  " + new Gson().toJson(paramData));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getFarmCropForm(paramData).enqueue(new Callback<CropFormResponse>() {
            @Override
            public void onResponse(Call<CropFormResponse> call, Response<CropFormResponse> response1) {
                String error = null;
                if (response1.isSuccessful()) {
                    CropFormResponse response = response1.body();
                    Log.e(TAG+"L", "Form  " + AppConstants.CHEMICAL_UNIT.FARM_ADD_DYNAMIC + " " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(SettingActivity.this, response.getMessage());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.authorization_expired));
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
//                    viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response1.errorBody().string().toString();
                        Log.e(TAG+"L", "Form Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                getPldReasons();
            }

            @Override
            public void onFailure(Call<CropFormResponse> call, Throwable t) {
                Log.e(TAG+"L", "Form Failure " + t.toString());
                getPldReasons();
            }
        });


    }

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
                if (response.isSuccessful()) {
                    Log.e(TAG+"L","Reason "+new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(SettingActivity.this);
                    } else if (response.body().getStatus() == 1 && response.body().getPldReasonList() != null && response.body().getPldReasonList().size() > 0) {

                        PldReason pldReason = new PldReason();
                        pldReason.setName("Select Loss/Damage Reason");
                        pldReasons.add(pldReason);
                        pldReasons.addAll(response.body().getPldReasonList());
                        DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.PLD_REASON);
                        DropDownT dropDownPld = new DropDownT(AppConstants.CHEMICAL_UNIT.PLD_REASON, new Gson().toJson(pldReasons));
                        DropDownCacheManager.getInstance(context).addChemicalUnit(dropDownPld);

                    } else {
                        DropDownT dropDownPld = new DropDownT(AppConstants.CHEMICAL_UNIT.PLD_REASON, new Gson().toJson(pldReasons));
                        DropDownCacheManager.getInstance(context).addChemicalUnit(dropDownPld);
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string();
                        Log.e(TAG+"L", "Reason Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                getCopRegData();
            }

            @Override
            public void onFailure(Call<PldReasonResponse> call, Throwable t) {
                Log.e(TAG+"L", "Reason Faril " + t.toString());
                getCopRegData();
            }
        });

    }

    private void getCopRegData(){
        String date=AppConstants.getTodaysDate();
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String personId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        Log.e(TAG, "PersonId " + personId + "   CompId " + compId + "  UserId " + userId + "   Token " + token);

        ApiInterface expApiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        Call<ResponseCompReg> compRegCall = expApiInterface.getCompRegData(compId, personId, userId, token);
        compRegCall.enqueue(new Callback<ResponseCompReg>() {
            @Override
            public void onResponse(Call<ResponseCompReg> call, Response<ResponseCompReg> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG+"L","CompReg "+new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 10) {

                    } else {
                        ResponseCompReg responseCompReg = response.body();
                        List<CompRegResult> compRegResultList = responseCompReg.getResult();
                        // compRegCacheManager.deleteAllData(SplashActivity.this);
                        if (compRegResultList != null && compRegResultList.size() > 0) {
                            CompRegCacheManager.getInstance(context).deleteAllData();
                            for (int i = 0; i < compRegResultList.size(); i++) {
                                String parameter = compRegResultList.get(i).getFeatureName();
                                try {
                                    String status=compRegResultList.get(i).getStatus();
                                    if (parameter!=null&&parameter.trim().equals(AppConstants.COMP_REG.IS_VETTING_FARM)){
                                        if (status!=null&&status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_VETTING_COMP, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_VETTING_COMP, false);
                                    }else if (parameter!=null&&parameter.trim().equals(AppConstants.COMP_REG.SOIL_SENS_ONLY)){
                                        if (status!=null&&status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_ONLY_SOIL_SENS, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_ONLY_SOIL_SENS, false);
                                    }else if (parameter!=null&&parameter.trim().equals(AppConstants.COMP_REG.SOIL_SENS_ENABLED)){
                                        if (status!=null&&status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_SOIL_SENS_ENABLED, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_SOIL_SENS_ENABLED, false);
                                    }else if (parameter!=null&&parameter.trim().equals(AppConstants.COMP_REG.WAY_POINT_ENABLED)){
                                        if (status!=null&&status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_WAY_POINT_ENABLED, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_WAY_POINT_ENABLED, false);
                                    }
                                    else if (parameter!=null&&parameter.trim().equals(AppConstants.COMP_REG.OMITANCE_AREA_ENABLED)){
                                        if (status!=null&&status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_OMITANCE_AREA_ENABLED, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_OMITANCE_AREA_ENABLED, false);
                                    }
                                    else if (parameter!=null&&parameter.trim().equals(AppConstants.COMP_REG.IS_PREVIOUS_CROP_MANDATORY)){
                                        if (status!=null&&status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_PREVIOUS_CROP_MANDATORY, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_PREVIOUS_CROP_MANDATORY, false);
                                    }else if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.CROSS_VETTING)) {
                                        if (status != null && status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_CROSS_VETTING_ENABLED, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_CROSS_VETTING_ENABLED, false);
                                    }else if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.AGRI_FINANCER)) {
                                        if (status != null && status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_DELINQUENT_COMP, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_DELINQUENT_COMP, false);
                                    }
                                }catch (Exception e){

                                }
                                CompRegCacheManager.getInstance(context).addCompRegData(SettingActivity.this, new Gson().toJson(compRegResultList.get(i)),  date, parameter);
                            }
                        }
                    }
                } else {
                    try {
                        Log.e(TAG+"L","CompReg Error "+ response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                getAllCompData();
            }

            @Override
            public void onFailure(Call<ResponseCompReg> call, Throwable t) {
                Log.e(TAG,"CompReg Fail "+ t.toString());
                getAllCompData();
            }
        });

    }

    private void getAllCompData() {
        try {
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
            FetchFarmSendData fetchFarmSendData = new FetchFarmSendData();
            fetchFarmSendData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
            fetchFarmSendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            fetchFarmSendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            Log.e("OfflineActivity", "Credentials " + new Gson().toJson(fetchFarmSendData));
            Call<SeasonResponse> fetchFarmDataCall = apiService.getSeasons(fetchFarmSendData);
            fetchFarmDataCall.enqueue(new Callback<SeasonResponse>() {
                @Override
                public void onResponse(Call<SeasonResponse> call, Response<SeasonResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            Log.e(TAG+"L", "COMP data " + new Gson().toJson(response.body()));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                        progressDoalog.cancel();
                                    loader.setVisibility(View.GONE);
                                }
                            });
                            if (response != null) {
                                SeasonResponse fetchFarmData = response.body();
                                if (response.body().getStatus() == 10) {
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(SettingActivity.this, response.body().getMsg());
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                    //un authorized access
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.unauthorized_access));
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                    // auth token expired
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.authorization_expired));
                                } else {
//                                    if (fetchFarmData.getExpenseDataDDList() != null)
//                                        expenseDataDDList.addAll(fetchFarmData.getExpenseDataDDList());
//                                    if (fetchFarmData.getResourceDataDDList() != null)
//                                        resourceDataDDList.addAll(fetchFarmData.getResourceDataDDList());
//                                    if (fetchFarmData.getIrrigationSource() != null)
//                                        irrigationSourceList.addAll(fetchFarmData.getIrrigationSource());
//                                    if (fetchFarmData.getIrrigationType() != null)
//                                        irrigationTypeList.addAll(fetchFarmData.getIrrigationType());
//                                    if (fetchFarmData.getSoilType() != null)
//                                        soilTypeList.addAll(fetchFarmData.getSoilType());
                                    if (fetchFarmData.getData() != null)
                                        seasonList.addAll(fetchFarmData.getData());
//                                    if (fetchFarmData.getCropList() != null)
//                                        cropList.addAll(fetchFarmData.getCropList());

                                    insertRoomFarmsNew();
                                }
                            } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.authorization_expired));
                            } else {
                                Log.e(TAG+"L","COMP data err "+response.errorBody().string());
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

                    callCompParams();
                }

                @Override
                public void onFailure(Call<SeasonResponse> call, Throwable t) {
                    Log.e(TAG+"L","COMP data fail "+t.toString());
                    callCompParams();
                }
            });
        } catch (Exception r) {
            r.printStackTrace();
        }
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
                        CompAgriResponse data=response .body();
                        Log.e(TAG,"CompAgri res "+new Gson().toJson(data));
                        if (data!=null&&data.getData() != null) {

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
                    viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(SettingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "CompAgri Err " + error);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(SettingActivity.this, resources.getString(R.string.failed_load_farm));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                getSuperVisors();
            }

            @Override
            public void onFailure(Call<CompAgriResponse> call, Throwable t) {

                Log.e(TAG, "CompAgri fail " + t.toString());
                getSuperVisors();
            }
        });

    }
    private void getSuperVisors() {
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String clusterId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID);
//        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getCompSupervisors(null,compId, userId, token).enqueue(new Callback<SupervisorListResponse>() {
            @Override
            public void onResponse(Call<SupervisorListResponse> call, Response<SupervisorListResponse> response) {
//                String error = null;
                if (response.isSuccessful()) {
                    try {
                        SupervisorListResponse data=response .body();
                        Log.e(TAG,"Svs "+new Gson().toJson(data));
                        if (data!=null&&data.getData() != null) {
                            DropDownCacheManager2.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.SUPERVISOR_LIST);
                            DropDownT2 clusters = new DropDownT2(AppConstants.CHEMICAL_UNIT.SUPERVISOR_LIST, new Gson().toJson(data));
                            DropDownCacheManager2.getInstance(context).addChemicalUnit(clusters);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
//                    try {
//                        error = response.errorBody().string().toString();
//                        Log.e(TAG, "Farmer Farm Err " + error);
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showDialog(LandingActivity.this, resources.getString(R.string.failed_load_farm));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }

//                long newMillis = AppConstants.getCurrentMills();
//                long diff = newMillis - currMillis;
//                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
//                    notifyApiDelay(LandingActivity.this, response.raw().request().url().toString(),
//                            "" + diff, internetSPeed, error, response.code());
//                }
            }

            @Override
            public void onFailure(Call<SupervisorListResponse> call, Throwable t) {
//                long newMillis = AppConstants.getCurrentMills();
//                long diff = newMillis - currMillis;
//                notifyApiDelay(LandingActivity.this, call.request().url().toString(),
//                        "" + diff, internetSPeed, t.toString(), 0);
//                Log.e(TAG, "Farmer Farm Err " + t.toString());
//                ViewFailDialog viewFailDialog = new ViewFailDialog();
//                viewFailDialog.showDialog(LandingActivity.this, resources.getString(R.string.failed_load_farm));
            }
        });
//        internetFlow(isLoaded[0]);

    }

    int totalParamCount=0;

    private void   callCompParams(){
        List<String>paramTypes=new ArrayList<>();
        paramTypes.add(AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE_NEW);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE_NEW);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE_NEW);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.CIVIL_STATUS);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.CROPPING_PATTERN);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.PLANTING_METHOD);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.LAND_CATEGORY);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.CASTE_CATEGORY);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.CASTE);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.RELIGION);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.MOBILE_NETWORK_OPERATOR);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.MOTOR_HP);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.MICRO_IRRI_AWARENESS);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.DATE_FORMAT);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.DELINQUENT);
        totalParamCount=paramTypes.size();
        for (int i=0;i<paramTypes.size();i++){
            getCompanyParams(i,paramTypes.get(i));
        }

    }

    protected void getCompanyParams(int index,String type) {
        ParamData paramData=new ParamData();
        paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        paramData.setType(type);
        final boolean[] isLoaded = {false};
        String auth=SharedPreferencesMethod.getString(context,SharedPreferencesMethod.AUTHORIZATION);
        long currMillis=AppConstants.getCurrentMills();
        Log.e(TAG, "Sending PARA at "+index+", "+ new Gson().toJson(paramData));
        ApiInterface apiInterface=RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getNewParameters(paramData).enqueue(new Callback<DDResponseNew>() {
            @Override
            public void onResponse(Call<DDResponseNew> call, Response<DDResponseNew> response1) {
                String error=null;
                isLoaded[0]=true;
                if (response1.isSuccessful()) {
                    DDResponseNew response = response1.body();
                    Log.e(TAG+"L", "Sending New Param Response "+type+"  " + new Gson().toJson(response));
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

                        if (response!=null&&response.getData()!=null&&response.getData().size()>0) {
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
                        error=response1.errorBody().string().toString();
                        Log.e(TAG+"L", "Cache Error " + error+" "+type);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (index==totalParamCount-1){
                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.CACHE_DATE, AppConstants.getTodaysDate());
                    Toasty.success(context,resources.getString(R.string.data_sync_success),Toast.LENGTH_SHORT).show();
//                        loader.setVisibility(View.GONE);
                    finishAffinity();
                    startActivity(new Intent(context, LandingActivity.class));
                    finish();
                }

            }

            @Override
            public void onFailure(Call<DDResponseNew> call, Throwable t) {
                Log.e(TAG+"L", "Failure getOfflineData " + t.toString());
//                    getAllDataNew2(AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE_NEW);

                if (index==totalParamCount-1){
                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.CACHE_DATE, AppConstants.getTodaysDate());
                    Toasty.success(context,resources.getString(R.string.data_sync_success),Toast.LENGTH_SHORT).show();
//                        loader.setVisibility(View.GONE);
                    finishAffinity();
                    startActivity(new Intent(context, LandingActivity.class));
                    finish();
                }

            }
        });


    }
    List<ExpenseDataDD> expenseDataDDList = new ArrayList<>();
    List<ResourceDataDD> resourceDataDDList = new ArrayList<>();

    List<Season> seasonList = new ArrayList<>();

    private void insertRoomFarmsNew() {
        DropDownT expenseDD = new DropDownT(AppConstants.CHEMICAL_UNIT.INPUT_COST, new Gson().toJson(expenseDataDDList));
        DropDownCacheManager.getInstance(context).addChemicalUnit(expenseDD);
        DropDownT resourceDD = new DropDownT(AppConstants.CHEMICAL_UNIT.RESOURCE_USED, new Gson().toJson(resourceDataDDList));
        DropDownCacheManager.getInstance(context).addChemicalUnit(resourceDD);
//        DropDownT irriSourceDD = new DropDownT(AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE, new Gson().toJson(irrigationSourceList));
//        DropDownCacheManager.getInstance(context).addChemicalUnit(irriSourceDD);
//        DropDownT irriTypeDD = new DropDownT(AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE, new Gson().toJson(irrigationTypeList));
//        DropDownCacheManager.getInstance(context).addChemicalUnit(irriTypeDD);
//        DropDownT soilTypeDD = new DropDownT(AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE, new Gson().toJson(soilTypeList));
//        DropDownCacheManager.getInstance(context).addChemicalUnit(soilTypeDD);
        DropDownT seasonDD = new DropDownT(AppConstants.CHEMICAL_UNIT.FARM_SEASON, new Gson().toJson(seasonList));
        DropDownCacheManager.getInstance(context).addChemicalUnit(seasonDD);
//        DropDownT cropDD = new DropDownT(AppConstants.CHEMICAL_UNIT.CROP_LIST, new Gson().toJson(cropList));
//        DropDownCacheManager.getInstance(context).addChemicalUnit(cropDD);

    }


}
