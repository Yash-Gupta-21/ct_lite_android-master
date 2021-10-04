package com.i9930.croptrails.ResetPassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
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
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Login.LoginActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.ResetPassword.Model.ResetPassSendData;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class ResetPasswordActivity extends AppCompatActivity {

    String TAG="ResetPasswordActivity";
    @BindView(R.id.ti_et_old_password)
    TextInputEditText ti_et_old_password;
    @BindView(R.id.ti_et_new_password)
    TextInputEditText ti_et_new_password;
    @BindView(R.id.ti_et_cnfrm_new_password)
    TextInputEditText ti_et_cnfrm_new_password;
    @BindView(R.id.tv_submit_update_password)
    TextView tv_submit_update_password;
    @BindView(R.id.resetProgress)
    ProgressBar progressBar;
    Context context;
    Resources resources;
    Toolbar mActionBarToolbar;
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
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
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        context=this;
        ButterKnife.bind(this);

        loadAds();
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.forget_password_activity_title));
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        tv_submit_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ti_et_old_password.getText().toString())){
                    ti_et_old_password.setError("Password cannot be empty");
                    Toasty.error(context,"Please enter your old password",Toast.LENGTH_LONG).show();
                }else if (TextUtils.isEmpty(ti_et_new_password.getText().toString())){
                    ti_et_new_password.setError("Password cannot be empty");
                    Toasty.error(context,"Please enter new password",Toast.LENGTH_LONG).show();

                }else if (!ti_et_new_password.getText().toString().trim().equals(ti_et_cnfrm_new_password.getText().toString().trim())){
                    ti_et_new_password.setError("Password do not match");
                    Toasty.error(context,"Please enter correct password",Toast.LENGTH_LONG).show();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    tv_submit_update_password.setClickable(false);
                    tv_submit_update_password.setEnabled(false);
                    submitData();
                }

            }
        });
        
    }

    private void submitData(){
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        ResetPassSendData resetPassSendData = new ResetPassSendData();
        resetPassSendData.setUserId(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.USER_ID));
        resetPassSendData.setToken(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.TOKEN));
        resetPassSendData.setOld_password(ti_et_old_password.getText().toString().trim());
        resetPassSendData.setPassword(ti_et_new_password.getText().toString().trim());
        resetPassSendData.setPerson_id(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.PERSON_ID));
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();
        Log.e(TAG,"RESET SEND DATA "+new Gson().toJson(resetPassSendData));
        Call<StatusMsgModel> statusMsgModelCall= apiService.getStatusMsgForResetPassword(resetPassSendData);
        statusMsgModelCall.enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                String error=null;
                isLoaded[0]=true;
                if (response.isSuccessful()){
                    Log.e(TAG,"RESET RESPONSE "+new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(ResetPasswordActivity.this, response.body().getMsg());
                    }  else if (response.body().getStatus()== AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(ResetPasswordActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(ResetPasswordActivity.this, resources.getString(R.string.authorization_expired));
                    }else {
                        if (response.body().getStatus()==1) {
                            progressBar.setVisibility(View.INVISIBLE);
                            tv_submit_update_password.setClickable(true);
                            tv_submit_update_password.setEnabled(true);
                            // Toasty.success(context, resources.getString(R.string.password_updated), Toast.LENGTH_LONG, true).show();
                            if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {

                                SharedPreferencesMethod.clear(context);
                                SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.LOGIN, false);
                                Intent intent = new Intent(context, LoginActivity
                                        .class);
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
                        }else {
                            progressBar.setVisibility(View.INVISIBLE);
                            tv_submit_update_password.setClickable(true);
                            tv_submit_update_password.setEnabled(true);
                            Toasty.error(context,""+response.body().getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if (response.code()== AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(ResetPasswordActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(ResetPasswordActivity.this, resources.getString(R.string.authorization_expired));
                }else {

                    try {
                        Log.e(TAG,"Status And message "+response.errorBody().string().toString());

                        error=response.errorBody().string().toString();

                    progressBar.setVisibility(View.INVISIBLE);
                    tv_submit_update_password.setClickable(true);
                    tv_submit_update_password.setEnabled(true);
                    Toasty.error(context,"Failed to update password", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(ResetPasswordActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e(TAG,"Failure  "+t.toString());
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                isLoaded[0]=true;
                notifyApiDelay(ResetPasswordActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Toasty.success(context,"Failed to update password",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
                tv_submit_update_password.setClickable(true);
                tv_submit_update_password.setEnabled(true);
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
                            ConnectivityUtils.checkSpeedWithColor(ResetPasswordActivity.this, new ConnectivityUtils.ColorListener() {
                                @Override
                                public void onColorChanged(int color, String speed) {

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

}
