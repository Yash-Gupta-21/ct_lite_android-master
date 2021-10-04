package com.i9930.croptrails.Splash;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.AddFarm.Model.FetchFarmerResponse;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.ClusterSelection.ClusterSelectActivityActivity;
import com.i9930.croptrails.CommonClasses.ClusterResponse;
import com.i9930.croptrails.CommonClasses.PldReason;
import com.i9930.croptrails.CompSelect.CompSelectActivity;
import com.i9930.croptrails.FingerAuth.FingerAuthActivity;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Login.LoginActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownCacheManager;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
import com.i9930.croptrails.SubmitPld.Model.PldReasonResponse;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegCacheManager;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegModel;
import com.i9930.croptrails.RoomDatabase.CompRegistry.DatabaseResInterface;
import com.i9930.croptrails.RoomDatabase.CompRegistry.Model.CompRegResult;
import com.i9930.croptrails.RoomDatabase.CompRegistry.Model.ResponseCompReg;
import com.i9930.croptrails.ServiceAndBroadcasts.SampleBootReceiver;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;
import com.i9930.croptrails.UtilityFP.BiometricUtils;


public class SplashActivity extends AppCompatActivity implements DatabaseResInterface {

    String TAG = "SplashActivity";
    private Thread mThread;
    private boolean isFinish = false;
    private boolean connected;
    int t = 0;
    Context context;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    String languageset;
    ImageView imageView;
    ImageView compLogoImage;
    ConnectivityManager connectivityManager;
    CompRegCacheManager compRegCacheManager;
    TextView proudly_made_tv;
    private boolean isActivityRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        context = this;
        imageView = (ImageView) findViewById(R.id.splash_logo);
        compLogoImage = findViewById(R.id.comp_logo);
        proudly_made_tv = findViewById(R.id.proudly_made_tv);

        String proudly = "<font color='#00ad00'>Proudly</font>";
        String india = "<font color='#e5d306'>Indian</font>";
        proudly_made_tv.setText((Html.fromHtml(proudly + " " + india)));
        StartAnimations();
        loadCompImage();
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.LOGIN) &&
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID) != null &&
                !TextUtils.isEmpty(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID))) {
            compRegCacheManager = CompRegCacheManager.getInstance(context);
            compRegCacheManager.getCompRegData(this);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigateToLogin();
                }
            }, 1500);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActivityRunning = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityRunning = false;
    }

    @Override
    protected void onDestroy() {
        try {
            mThread.interrupt();
            mThread = null;

        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        stop();
        super.onStop();
        isActivityRunning = false;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    private void stop() {
        isFinish = true;
        if (t == 0) {
            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.LOGIN) &&
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID) != null &&
                    !TextUtils.isEmpty(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID)) &&
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID) != null &&
                    !TextUtils.isEmpty(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID)) &&
                    !SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID).equals("0")) {
                String loginType = "";
                loginType = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE);
                if (loginType == null)
                    loginType = "";
                Intent intent = new Intent(context, LandingActivity.class);
                if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.COMP_NOT_SELECTED)) {
                    intent = new Intent(context, CompSelectActivity.class);
                } else if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_CLUSTER_SELECTED) && (loginType.equals(AppConstants.ROLES.ADMIN) || loginType.equals(AppConstants.ROLES.SUPER_ADMIN))) {
                    intent = new Intent(context, ClusterSelectActivityActivity.class);
                } else if (BiometricUtils.isBiometricPromptEnabled() && BiometricUtils.isPermissionGranted(context) &&
                        BiometricUtils.isFingerprintAvailable(context) && BiometricUtils.isHardwareSupported(context) &&
                        SharedPreferencesMethod2.getBoolean(context, SharedPreferencesMethod2.IS_APP_LOCK)) {
                    intent = new Intent(context, FingerAuthActivity.class);
                }
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
            } else {
                navigateToLogin();
            }
        }
    }

    @Override
    public void finish() {
        t = 1;
        super.finish();
    }

    private void loadCompImage() {
        RequestOptions requestOption = new RequestOptions()
                .placeholder(R.drawable.crp_trails_icon_splash);

        Glide.with(this).load(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_BANNER))
                .transition(DrawableTransitionOptions.withCrossFade())
                .thumbnail(Glide.with(this)
                        .load(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_BANNER))
                )
                .into(compLogoImage);
        //}
    }

    private void navigateToLogin() {
        Intent intent = new Intent(context, LoginActivity.class);
        ActivityOptions options =
                null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
                finish();
            } else {
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e.toString());
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // stop();
        }
        return super.onTouchEvent(event);
    }

    private void StartAnimations() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.splash_animation);
        imageView.startAnimation(animation);
    }

    private void scheduleAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, SampleBootReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        //alarmManager.cancel(pendingIntent);
        Calendar alarmStartTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        Calendar current_time = Calendar.getInstance();
        alarmStartTime.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
        alarmStartTime.set(Calendar.MINUTE, now.get(Calendar.MINUTE));
        alarmStartTime.set(Calendar.SECOND, now.get(Calendar.SECOND) + 10);
        if (current_time.after(alarmStartTime)) {
            Log.d("Hey", "Added a day");
            alarmStartTime.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), 15 * 60 * 1000, pendingIntent);//AlarmManager.INTERVAL_DAY
        Log.d("Alarm", "Alarms set for everyday 8 am.");
    }

    @Override
    public void onGetAllCompRegData(List<CompRegModel> compRegModelList) {
        storeCompRegData(compRegModelList);
    }

    private void storeCompRegData(List<CompRegModel> compRegModels) {
        /*if (true){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.AREA_UNIT_LABEL, "acres");
                    SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.AREA_MULTIPLICATON_FACTOR, 1.0);
                    Intent intent = new Intent(context, LandingActivity.class);
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
            });
        }
        else*/
        {
            Date date = Calendar.getInstance().getTime();
        /*SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        Date d = input.parse(date);*/

            SimpleDateFormat output = new SimpleDateFormat(AppConstants.DATE_FORMAT_SERVER);
            final String curdate = output.format(date);
            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.LOGIN)) {
                if (connected) {
                    if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                        if (compRegModels != null && compRegModels.size() > 0) {
                            updateCacheData();
                            if (!compRegModels.get(compRegModels.size() - 1).getDate().equals(curdate)) {
                                getCopRegData(curdate);
                            } else {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        showCompImage();

                                    }
                                }, 1500);

                            }
                        } else {
                            getCopRegData(curdate);

                        }

                    } else {
                        stop();
                    }

                } else {
                    if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE))
                        Toasty.warning(context, "Please connect to internet", Toast.LENGTH_LONG, true).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showCompImage();
                        }
                    }, 1500);


                }


            } else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navigateToLogin();

                    }
                }, 1500);

            }
        }
    }

    private void updateCacheData() {
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            String offlineDateStr = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.CACHE_DATE);
            String todayDateStr;
            Date c;
            SimpleDateFormat df;
            c = Calendar.getInstance().getTime();
            df = new SimpleDateFormat("yyyy-MM-dd");
            todayDateStr = df.format(c);
            if (AppConstants.isValidString(offlineDateStr)) {
                Date todayDate;
                Date offlineDate;

                try {
                    todayDate = df.parse(todayDateStr);
                    offlineDate = df.parse(offlineDateStr);
                    if (todayDate != null && offlineDate != null && !todayDate.equals(offlineDate)) {
                        updateCache(todayDateStr);
                    } else {
                        Log.e(TAG, "Cache Data 0");
//                   updateCache(todayDateStr);
                    }

                } catch (ParseException e) {
                    Log.e(TAG, "Cache Data ", e);
                    e.printStackTrace();
                    updateCache(todayDateStr);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Cache Data2 ", e);
                    updateCache(todayDateStr);
                }
            } else {
                updateCache(todayDateStr);
            }
        }

    }

    private void updateCache(String todayDate) {
        Log.e(TAG, "Updating Cache");
        getPldReasons();
        getClusterOfCompany();
        getFarmerList();


    }

    private void getFarmerList() {
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
                    Log.e(TAG, "Farmer List Res " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 10) {
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(SplashActivity.this, getResources().getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == 1) {
                        if (response.body().getFarmerDataList() != null) {
                            DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.FARMER_LIST);
                            DropDownT farmers = new DropDownT(AppConstants.CHEMICAL_UNIT.FARMER_LIST, new Gson().toJson(response.body().getFarmerDataList()));
                            DropDownCacheManager.getInstance(context).addChemicalUnit(farmers);
                        }
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(SplashActivity.this, getResources().getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(SplashActivity.this, getResources().getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "Farmer List Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<FetchFarmerResponse> call, Throwable t) {

                Log.e(TAG, "Farmer List failure " + t.toString());
            }
        });


    }

    private void getClusterOfCompany() {
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
                    if (response.body().getStatus() == 1) {

                        if (response.body().getClusterList() != null) {
                            DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST);
                            DropDownT clusters = new DropDownT(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST, new Gson().toJson(response.body().getClusterList()));
                            DropDownCacheManager.getInstance(context).addChemicalUnit(clusters);
                        }


                    } else if (response.body().getStatus() == 10) {
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this);
                        //progressDialog.cancel();
                    } else {
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
                    try {
                        //progressDialog.cancel();
                        error = response.errorBody().string();
                        Log.e(TAG, "Cluster Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<ClusterResponse> call, Throwable t) {

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
                    if (response.body().getStatus() == 10) {
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this);
                    } else if (response.body().getStatus() == 1 && response.body().getPldReasonList() != null && response.body().getPldReasonList().size() > 0) {
                        List<PldReason> pldReasons = new ArrayList<>();
                        PldReason pldReason = new PldReason();
                        pldReason.setName("Select Loss/Damage Reason");
                        pldReasons.add(pldReason);
                        pldReasons.addAll(response.body().getPldReasonList());
                        DropDownT dropDownPld = new DropDownT(AppConstants.CHEMICAL_UNIT.PLD_REASON, new Gson().toJson(pldReasons));
                        DropDownCacheManager.getInstance(context).addChemicalUnit(dropDownPld);

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
                    try {
                        error = response.errorBody().string();
                        Log.e(TAG, "Reason Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<PldReasonResponse> call, Throwable t) {

            }
        });
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

    private void getCopRegData(String date) {
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
                    if (response.body().getStatus() == 10) {
                        if (isActivityRunning) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(SplashActivity.this, response.body().getMsg());
                        }
                    } else {
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
                                    } else if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.CROSS_VETTING)) {
                                        if (status != null && status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_CROSS_VETTING_ENABLED, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_CROSS_VETTING_ENABLED, false);
                                    } else if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.AGRI_FINANCER)) {
                                        if (status != null && status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_DELINQUENT_COMP, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_DELINQUENT_COMP, false);
                                    }
                                } catch (Exception e) {

                                }
                                compRegCacheManager.addCompRegData(SplashActivity.this, new Gson().toJson(compRegResultList.get(i)), date, parameter);
                            }
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showCompImage();

                            }
                        }, 1500);
                    }
                } else {
                    try {
                        Log.e(TAG, "rError " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showCompImage();

                        }
                    }, 1500);
                }

            }

            @Override
            public void onFailure(Call<ResponseCompReg> call, Throwable t) {
                Log.e("RFerror", t.toString());
                AppConstants.setDefaultUnit(context);
                Intent intent = new Intent(context, LandingActivity.class);
                ActivityOptions options =
                        null;
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

    }

    private void showCompImage() {
        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.LOGIN) && SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_BANNER) != null &&
                !TextUtils.isEmpty(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_BANNER))) {
            Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
            imageView.startAnimation(fadeOut);
            compLogoImage.setVisibility(View.VISIBLE);
            Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            compLogoImage.startAnimation(fadeInAnimation);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stop();
                }
            }, 1500);
        } else {
            stop();
        }
    }

    @Override
    public void onCompRegDataAdded() {
        Log.e("msg", "onCompRegDataAdded");


    }

    @Override
    public void onAllDataDeleted() {
        Log.e("msg", "onAllDataDeleted");


    }

    @Override
    public void onDataNotAdded() {
        Log.e("msg", "onDataNotAdded");

    }

    @Override
    public void onAllDataNotDeleted() {
        Log.e("msg", "onAllDataNotDeleted");

    }

    @Override
    public void onUpdateCompRegData() {

    }

    @Override
    public void onUpdateNotSuccess() {

    }
}
