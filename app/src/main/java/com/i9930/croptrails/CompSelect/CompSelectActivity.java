package com.i9930.croptrails.CompSelect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
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
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.AESUtils2;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.ClusterSelection.ClusterSelectActivityActivity;
import com.i9930.croptrails.CompSelect.Adapter.CompanyRvAdapter;
import com.i9930.croptrails.CompSelect.Model.CompanyDatum;
import com.i9930.croptrails.CompSelect.Model.CompanyResponse;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Login.Model.CompParamDatum;
import com.i9930.croptrails.Login.Model.CompParamResponse;
import com.i9930.croptrails.Login.Model.LoginData;
import com.i9930.croptrails.Login.Model.Post;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegCacheManager;
import com.i9930.croptrails.RoomDatabase.CompRegistry.Model.CompRegResult;
import com.i9930.croptrails.RoomDatabase.CompRegistry.Model.ResponseCompReg;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownCacheManager;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class CompSelectActivity extends AppCompatActivity {

    CompSelectActivity context;
    String TAG = "CompSelectActivity";
    Resources resources;
    Toolbar mActionBarToolbar;
    @BindView(R.id.compRecycler)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    GifImageView progressBar;
    @BindView(R.id.submitBtn)
    Button submitBtn;
    List<CompanyDatum> companyDatumList = new ArrayList<>();
    CompRegCacheManager compRegCacheManager;
    int selectedIndex = -1;
    ViewFailDialog viewFailDialog = new ViewFailDialog();
    @BindView(R.id.connectivityLine)
    View connectivityLine;
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
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        setContentView(R.layout.activity_comp_select);
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        context = this;
        loadAds();
        ButterKnife.bind(this);
        compRegCacheManager = CompRegCacheManager.getInstance(context);
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(getString(R.string.select_company));
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
                        CompSelectActivity.super.onBackPressed();
                    }
                }
        );
        checkData();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedIndex == -1 || selectedIndex < 0) {
                    Toasty.error(context, getString(R.string.please_select_a_company), Toast.LENGTH_LONG).show();
                } else {
                    updateUserCreds();
                }
            }
        });
    }

    private void checkData() {
        if (DataHandler.newInstance().getCompanyDatumList() != null) {
            companyDatumList = DataHandler.newInstance().getCompanyDatumList();
            setRecyclerData(companyDatumList);
        } else {
            getCompanyList();
        }
    }

    private void updateUserCreds() {
        showProgressDialog();
        String mob = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NUMBER);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String compId = companyDatumList.get(selectedIndex).getCompId();
        final String dateFormat = companyDatumList.get(selectedIndex).getDateFormat();
        String roleId = companyDatumList.get(selectedIndex).getRoleId();
        Log.e(TAG, "mob: " + mob + ", useriId: " + userId + ", token: " + token + ", compId: " + compId);
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.changeUserCreds(mob, userId, token, compId).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                isLoaded[0] = true;
                String error = "";
                if (response.isSuccessful()) {
                    Log.e(TAG, "Farmer Login Res " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 1) {
                        boolean dontAllowLogin = false;
                        String name = "";
                        boolean isNavigationToClusterSelection = false;
                        if (response.body().getData().getRoleId() != null && !TextUtils.isEmpty(response.body().getData().getRoleId())) {
                            String role = response.body().getData().getRoleId();
                            if (role.trim().equals("3")) {
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOGIN_TYPE, AppConstants.ROLES.FARMER);
                                SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE, AppConstants.ROLES.FARMER);
                            } else if (role.trim().equals("1")) {
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOGIN_TYPE, AppConstants.ROLES.ADMIN);
                                SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE, AppConstants.ROLES.ADMIN);
                                if (response.body().getClusterIdList() != null&&response.body().getClusterIdList().size()>0) {
                                    StringBuilder builder = new StringBuilder();
                                    for (int i = 0; i < response.body().getClusterIdList().size(); i++) {
                                        if (i == response.body().getClusterIdList().size() - 1)
                                            builder.append(response.body().getClusterIdList().get(i).getClusterId());
                                        else
                                            builder.append(response.body().getClusterIdList().get(i).getClusterId()).append(",");
                                    }
                                    Log.e(TAG, "Cluster Ids " + builder.toString());
                                    DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST);
                                    DropDownT clusters = new DropDownT(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST, new Gson().toJson(response.body().getClusterIdList()));
                                    DropDownCacheManager.getInstance(context).addChemicalUnit(clusters);
                                    isNavigationToClusterSelection = true;
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVCLUSTERID, builder.toString());
                                }
                            } else if (role.trim().equals("2")) {
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOGIN_TYPE, AppConstants.ROLES.SUPERVISOR);
                                SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE, AppConstants.ROLES.SUPERVISOR);
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVCLUSTERID, response.body().getData().getClusterId());
                            } else if (role.trim().equals("0")) {
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOGIN_TYPE, AppConstants.ROLES.SUPER_ADMIN);
                                SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE, AppConstants.ROLES.SUPER_ADMIN);
                                if (response.body().getClusterIdList() != null&&response.body().getClusterIdList().size()>0) {
                                    StringBuilder builder = new StringBuilder();
                                    for (int i = 0; i < response.body().getClusterIdList().size(); i++) {
                                        if (i == response.body().getClusterIdList().size() - 1)
                                            builder.append(response.body().getClusterIdList().get(i).getClusterId());
                                        else
                                            builder.append(response.body().getClusterIdList().get(i).getClusterId()).append(",");
                                    }
                                    Log.e(TAG, "Cluster Ids " + builder.toString());
                                    isNavigationToClusterSelection = true;
                                    DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST);
                                    DropDownT clusters = new DropDownT(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST, new Gson().toJson(response.body().getClusterIdList()));
                                    DropDownCacheManager.getInstance(context).addChemicalUnit(clusters);
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVCLUSTERID, builder.toString());
                                }
                            } else if (role.trim().equals("4")) {
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOGIN_TYPE, AppConstants.ROLES.SUB_ADMIN);
                                SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE, AppConstants.ROLES.SUB_ADMIN);
                                if (response.body().getClusterIdList() != null&&response.body().getClusterIdList().size()>0) {
                                    StringBuilder builder = new StringBuilder();
                                    for (int i = 0; i < response.body().getClusterIdList().size(); i++) {
                                        if (i == response.body().getClusterIdList().size() - 1)
                                            builder.append(response.body().getClusterIdList().get(i).getClusterId());
                                        else
                                            builder.append(response.body().getClusterIdList().get(i).getClusterId()).append(",");
                                    }
                                    Log.e(TAG, "Cluster Ids " + builder.toString());
                                    isNavigationToClusterSelection = true;
                                    DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST);
                                    DropDownT clusters = new DropDownT(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST, new Gson().toJson(response.body().getClusterIdList()));
                                    DropDownCacheManager.getInstance(context).addChemicalUnit(clusters);
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVCLUSTERID, builder.toString());
                                }
                            } else if (role.trim().equals("7")) {
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOGIN_TYPE, AppConstants.ROLES.CLUSTER_ADMIN);
                                SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE, AppConstants.ROLES.CLUSTER_ADMIN);
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVCLUSTERID, response.body().getData().getClusterId());
                            } else {
//                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOGIN_TYPE, AppConstants.ROLES.FARMER);
//                                SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE,AppConstants.ROLES.FARMER);
                                if (role.trim().equals("0")) {
                                    name = "Manager";
                                } else if (role.trim().equals("0")) {
                                    name = "Vet/Lab";
                                }
                                dontAllowLogin = true;
                            }
                            if (!dontAllowLogin) {
                                try {
//                            SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.UID, AESUtils2.encrypt(uid));
                                    SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.USER_NUMBER, AESUtils2.encrypt(mob));
//                                SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE, AppConstants.FARMER);
                                    SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.UNAME, response.body().getData().getName());
                                    SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.IMAGE, response.body().getData().getImgLink());
                                    String oldId = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.USER_ID);
                                    String newId = AESUtils2.encrypt(response.body().getData().getUserId());
                                    if (oldId != null && !newId.equals(oldId)) {
                                        if (!SharedPreferencesMethod2.getBoolean(context, SharedPreferencesMethod2.IS_FINGER_SELECTED) &&
                                                !SharedPreferencesMethod2.getBoolean(context, SharedPreferencesMethod2.IS_FINGER_DONT_ASK)) {
                                            SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_FINGER_SELECTED, false);
                                            SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_APP_LOCK, false);
                                            SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_ALLOW_FINGER, false);
                                            SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_FINGER_DONT_ASK, false);
                                        }
                                    }
                                    SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.USER_ID, newId);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //sandeep 2/8/19 comp reg
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.AUTHORIZATION, response.body().getJwToken());
                                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.HARVEST_ALLOWED, true);
                                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED, true);
                                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.VISIT_ALLOWED, true);
                                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.EXPENSE__ALLOWED, true);
                                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.SUPERVISOR_ALLOWED_ADD_FARM, true);
                                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.FARMER_ALLOWED_ADD_FARM, true);
                                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.PLD_ALLOWED, true);
                                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.GERMINATION_ALLOWED, true);
                                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.FARMER_ALLOWED_ADD_ACTIVITY, true);
                                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_INPUT_COST, true);
                                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.FARMER_ALLOWED_UPDATE_FARM_DETAILS, true);
                                LoginData loginData = response.body().getData();
//                        if (loginData.getCompName() != null && !TextUtils.isEmpty(loginData.getCompName())) {
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMPANY_NAME, loginData.getCompName());
//                        }

//                        if (loginData.getCompLogo() != null && !TextUtils.isEmpty(loginData.getCompLogo())) {
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_IMAGE, loginData.getCompLogo());
//                        }
//                        if (loginData.getCompBanner() != null && !TextUtils.isEmpty(loginData.getCompBanner())) {
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_BANNER, loginData.getCompBanner());
//                        }
                                if (loginData.getName() != null)
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USERNAME, loginData.getName());
                                else
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USERNAME, "");

                                if (loginData.getImgLink() != null)
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_IMAGE, loginData.getImgLink());
                                else
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_IMAGE, "");
                                if (loginData.getMob() != null)
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_NUMBER, loginData.getMob());
                                else
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_NUMBER, "");
                                if (loginData.getEmail() != null)
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_EMAIL, loginData.getEmail());
                                else
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_EMAIL, "");
                                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.LOGIN, true);

                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_ID, response.body().getData().getCompId());
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.CLUSTER_NAME, response.body().getData().getClusterName());
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.PERSON_ID, response.body().getData().getPersonId());
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_ID, response.body().getData().getUserId());
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVCLUSTERID, response.body().getData().getClusterId());
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_NAME, response.body().getData().getName());
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.TOKEN, response.body().getToken());
                                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.COMP_NOT_SELECTED, false);


                                getCompParams(response.body());
//                        if (response.body().getDateFormat()!=null&&!TextUtils.isEmpty(response.body().getDateFormat()))
//                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, response.body().getDateFormat());
//                        else
//                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, AppConstants.DATE_FORMAT_SHOW);


                                getCopRegData(dateFormat,isNavigationToClusterSelection);
                            } else
                                Toasty.error(context, "Currently " + name + " not allowed for app login", Toast.LENGTH_SHORT).show();

                        } else {
                            viewFailDialog.showDialog(context, resources.getString(R.string.opps_message), "Could not determine your role, Please login again");
                        }
                    } else if (response.body().getStatus() == 10) {
                        viewFailDialog.showSessionExpireDialog(CompSelectActivity.this);
                    } else if (response.body().getStatus() == 5) {
                        hideProgressDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.alert_title_label), resources.getString(R.string.account_deactivated_contact_admin_msg));
                    } else if (response.body().getStatus() == 2) {
                        hideProgressDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.alert_title_label), response.body().getMsg());
                    } else {

                    }
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        hideProgressDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_login));
                        Log.e(TAG, "Farmer Login Err " + response.errorBody().string().toLowerCase());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(CompSelectActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                hideProgressDialog();
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(CompSelectActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_login));
                Log.e(TAG, "Farmer Login Failure " + t.toString());
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

    private void getCompParams(Post post) {
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
                        SharedPreferencesMethod.setFloatPref(context, SharedPreferencesMethod.COMP_AUTO_ACCURACY, 2.0f);

                        Log.e(TAG, "CompParam rError " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            @Override
            public void onFailure(Call<CompParamResponse> call, Throwable t) {
                Log.e(TAG,"CompParam Fail "+ t.toString());
                SharedPreferencesMethod.setFloatPref(context, SharedPreferencesMethod.COMP_AUTO_ACCURACY, 2.0f);

            }
        });
    }
    private void internetFlow(boolean isLoaded) {
        try {

            if (!isLoaded) {
                if (!ConnectivityUtils.isConnected(context)) {
                    AppConstants.failToast(context, getResources().getString(R.string.check_internet_connection_msg));
                } else {
                    ConnectivityUtils.checkSpeedWithColor(CompSelectActivity.this, new ConnectivityUtils.ColorListener() {
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


        } catch (Exception e) {

        }

    }

    String internetSPeed = "";

    private void setRecyclerData(List<CompanyDatum> list) {
        companyDatumList.clear();
        companyDatumList.addAll(list);

        CompanyRvAdapter adapter = new CompanyRvAdapter(context, list, new CompanyRvAdapter.CompanySelectListener() {
            @Override
            public void onCompanySelected(int index) {
                selectedIndex = index;
                if (index == -1) {
                    submitBtn.setVisibility(View.GONE);
                } else if (index > -1) {
                    submitBtn.setVisibility(View.VISIBLE);
                    //  Toasty.info(context, companyDatumList.get(index).getCompName() + " selected", Toast.LENGTH_LONG, false).show();
                }
            }
        });
        progressBar.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

    }

    private void getCompanyList() {
        showProgressDialog();
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String mob = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NUMBER);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getCompanyList(mob, userId, token).enqueue(new Callback<CompanyResponse>() {
            @Override
            public void onResponse(Call<CompanyResponse> call, Response<CompanyResponse> response) {
                isLoaded[0] = true;
                String error = "";
                if (response.isSuccessful()) {
                    hideProgressDialog();
                    Log.e(TAG, "CompResponse " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 1) {

                        if (response.body().getCompanyDatumList() != null && response.body().getCompanyDatumList().size() > 1) {
                            setRecyclerData(response.body().getCompanyDatumList());
                        } else {
                            viewFailDialog.showDialogForFinish(context, resources.getString(R.string.opps_message), getString(R.string.no_other_company_available));
                        }

                    } else {
                        viewFailDialog.showDialogAndGoHomePage(context, resources.getString(R.string.opps_message), resources.getString(R.string.no_other_company_available));
                    }

                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    hideProgressDialog();
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    hideProgressDialog();
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                } else {
                    hideProgressDialog();
                    viewFailDialog.showDialogAndGoHomePage(context, resources.getString(R.string.opps_message), resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg));
                }
            }

            @Override
            public void onFailure(Call<CompanyResponse> call, Throwable t) {
                hideProgressDialog();
                viewFailDialog.showDialogAndGoHomePage(context, resources.getString(R.string.opps_message), resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg));
            }
        });
    }

    private void showProgressDialog() {
        progressBar.setVisibility(View.VISIBLE);
        submitBtn.setClickable(false);
        submitBtn.setEnabled(false);
    }

    private void hideProgressDialog() {
        progressBar.setVisibility(View.GONE);

        submitBtn.setClickable(true);
        submitBtn.setEnabled(true);
    }

    private void getCopRegData(final String dateFormat,boolean isNavigationToClusterSelection) {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat output = new SimpleDateFormat(AppConstants.DATE_FORMAT_SERVER);
        final String curdate = output.format(date);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String personId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("comp_id",""+compId);
        jsonObject.addProperty("person_id",""+personId);
        jsonObject.addProperty("user_id",""+userId);
        jsonObject.addProperty("token",""+token);
        Log.e(TAG, "PersonId " + personId + "   CompId " + compId + "  UserId " + userId + "   Token " + token);

        ApiInterface expApiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        Call<ResponseCompReg> compRegCall = expApiInterface.getCompRegData(compId, personId, userId, token);
        compRegCall.enqueue(new Callback<ResponseCompReg>() {
            @Override
            public void onResponse(Call<ResponseCompReg> call, Response<ResponseCompReg> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(CompSelectActivity.this, response.body().getMsg());
                    } else {
                        // compRegCacheManager.deleteAllData(SplashActivity.this);
                        ResponseCompReg responseCompReg = response.body();
                        List<CompRegResult> compRegResultList = responseCompReg.getResult();
                        // compRegCacheManager.deleteAllData(SplashActivity.this);
                        if (compRegResultList != null && compRegResultList.size() > 0) {
                            compRegCacheManager.deleteAllData();
                            for (int i = 0; i < compRegResultList.size(); i++) {
                                String parameter = compRegResultList.get(i).getFeatureName();
                                try {
                                    String status = compRegResultList.get(i).getStatus();
                                    if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.IS_VETTING_FARM)) {
                                        if (status != null && status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_VETTING_COMP, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_VETTING_COMP, false);
                                    } else if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.SOIL_SENS_ONLY)) {
                                        if (status != null && status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_ONLY_SOIL_SENS, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_ONLY_SOIL_SENS, false);
                                    } else if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.SOIL_SENS_ENABLED)) {
                                        if (status != null && status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_SOIL_SENS_ENABLED, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_SOIL_SENS_ENABLED, false);
                                    } else if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.WAY_POINT_ENABLED)) {
                                        if (status != null && status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_WAY_POINT_ENABLED, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_WAY_POINT_ENABLED, false);
                                    } else if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.OMITANCE_AREA_ENABLED)) {
                                        if (status != null && status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_OMITANCE_AREA_ENABLED, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_OMITANCE_AREA_ENABLED, false);
                                    } else if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.IS_PREVIOUS_CROP_MANDATORY)) {
                                        if (status != null && status.trim().equals("1"))
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
                                } catch (Exception e) {

                                }
                                compRegCacheManager.addCompRegData(null, new Gson().toJson(compRegResultList.get(i)), curdate, parameter);
                            }
                        }
                        showCompImage(dateFormat,isNavigationToClusterSelection);

                    }
                } else {
                    try {
                        Log.e(TAG, "rError " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    showCompImage(dateFormat,isNavigationToClusterSelection);
                }

            }

            @Override
            public void onFailure(Call<ResponseCompReg> call, Throwable t) {
                Log.e("RFerror", t.toString());
                AppConstants.setDefaultUnit(context);
                showCompImage(dateFormat,isNavigationToClusterSelection);
            }
        });

    }

    private void showCompImage(String dateFormat, boolean isNavigationToClusterSelection ) {
        hideProgressDialog();
        if (dateFormat != null && !TextUtils.isEmpty(dateFormat))
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, dateFormat);
        else
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, AppConstants.DATE_FORMAT_SHOW);
        Intent intent = new Intent(context, LandingActivity.class);
        if (isNavigationToClusterSelection ){
            intent = new Intent(context, ClusterSelectActivityActivity.class);
        }
        ActivityOptions options =
                null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
        }
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

}
