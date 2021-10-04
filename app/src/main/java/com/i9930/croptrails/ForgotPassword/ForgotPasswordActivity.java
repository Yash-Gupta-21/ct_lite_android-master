package com.i9930.croptrails.ForgotPassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;

public class ForgotPasswordActivity extends AppCompatActivity {
    ForgotPasswordActivity context;
    String TAG = "ForgotPasswordActivity";
    Resources resources;
    Toolbar mActionBarToolbar;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etOtp)
    EditText etOtp;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @BindView(R.id.getOtpButton)
    Button getOtpButton;

    @BindView(R.id.tiOtp)
    TextInputLayout tiOtp;
    @BindView(R.id.tiPasswordConfirm)
    TextInputLayout tiPasswordConfirm;
    @BindView(R.id.tiPassword)
    TextInputLayout tiPassword;

    private interface KEYS {
        public static String FORGOT = "forgot";
        public static String VERIFY = "verify";
        public static String RESET = "reset";
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        resources = getResources();
        context = this;
        ButterKnife.bind(this);
        loadAds();
        TextView title = findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.forgot_password));
        mActionBarToolbar = findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPasswordActivity.this.onBackPressed();
            }
        });
        onClicks();

    }

    // key => forgot, verify, reset
    String key = KEYS.FORGOT;

    private void onClicks() {
        getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etUserName.getText().toString())) {
                    AppConstants.failToast(context, resources.getString(R.string.enter_user_name));
                } else if (key.equalsIgnoreCase("forgot")) {
                    callApi();
                } else if (key.equalsIgnoreCase(KEYS.VERIFY)) {
                    if (TextUtils.isEmpty(etOtp.getText().toString()))
                        AppConstants.failToast(context, resources.getString(R.string.otp_message));
                    else
                        callApi();
                } else if (key.equalsIgnoreCase(KEYS.RESET)) {
                    if (TextUtils.isEmpty(etPassword.getText().toString()))
                        AppConstants.failToast(context, resources.getString(R.string.please_enter_password_msg));
                    else if (etPassword.getText().toString().trim().length() < 6)
                        AppConstants.failToast(context, resources.getString(R.string.error_invalid_password));
                    else if (!etPassword.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim())) {
                        etConfirmPassword.setError("Password do not match");
                        Toasty.error(context, "Password do not match", Toast.LENGTH_LONG).show();
                    } else
                        callApi();
                }
            }
        });
    }
//    verify {"status":1,"message":"verified"}

    private void callApi() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", key);
        jsonObject.addProperty("username", etUserName.getText().toString().trim());
        if (key.equalsIgnoreCase(KEYS.VERIFY) || key.equalsIgnoreCase(KEYS.RESET)) {
            jsonObject.addProperty("otp", etOtp.getText().toString().trim());
        }
        if (key.equalsIgnoreCase(KEYS.RESET)) {
            jsonObject.addProperty("password", etPassword.getText().toString().trim());
        }

        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.forgotPassword(jsonObject).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                Log.e(TAG, "callApi code " + response.code());
                if (response.isSuccessful()) {
                    try {
                        StatusMsgModel msgModel = response.body();
                        Log.e(TAG, "callApi res " + new Gson().toJson(msgModel));
                        if (msgModel.getStatus() == 1) {
                            if (key == KEYS.FORGOT) {
                                key = KEYS.VERIFY;
                                etUserName.setFocusable(false);
                                etUserName.clearFocus();
                                etUserName.setEnabled(false);
                                AppConstants.successToast(context, msgModel.getMessage());
                                tiOtp.setVisibility(View.VISIBLE);
                            } else if (key == KEYS.VERIFY) {
                                key = KEYS.RESET;
                                etOtp.setFocusable(false);
                                etOtp.clearFocus();
                                etOtp.setEnabled(false);
                                tiPassword.setVisibility(View.VISIBLE);
                                tiPasswordConfirm.setVisibility(View.VISIBLE);
                            } else {
                                AppConstants.successToast(context, response.body().getMessage());
                                finish();
                            }

                        } else {
                            AppConstants.failToast(context, response.body().getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        AppConstants.failToast(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg));
                    }
                } else {
                    try {

                        String error = response.errorBody().string().toString();
                        Log.e("Error", error);
                        Log.e(TAG, "callApi err " + error);
                        StatusMsgModel data = new Gson().fromJson(error, StatusMsgModel.class);
                        AppConstants.failToast(context, data.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        AppConstants.failToast(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg));
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e(TAG, "callApi fail " + t.toString());
                AppConstants.failToast(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg));
            }
        });
    }
}