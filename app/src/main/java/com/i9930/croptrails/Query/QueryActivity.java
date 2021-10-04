package com.i9930.croptrails.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

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
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Query.Adapter.QuerySpinnerAdapter;
import com.i9930.croptrails.Query.Model.QueryParamResponse;
import com.i9930.croptrails.Query.Model.QueryParams;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class QueryActivity extends AppCompatActivity {

    String TAG = "QueryActivity";
    Context context;
    Resources resources;
    Toolbar mActionBarToolbar;
    @BindView(R.id.etQuery)
    EditText etQuery;
    @BindView(R.id.progressBar)
    GifImageView progressBar;
    @BindView(R.id.submitBtn)
    Button submitBtn;
    @BindView(R.id.tiComment)
    TextInputLayout tiComment;
    @BindView(R.id.typeSpinner)
    Spinner typeSpinner;
    List<QueryParams> paramsList = new ArrayList<>();
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
        setContentView(R.layout.activity_query);
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        context = this;
        loadAds();
        ButterKnife.bind(this);

        TextView title = (TextView) findViewById(R.id.tittle);
        //   Button submit_butt=(Button)findViewById(R.id.submit_test);
        title.setText(resources.getString(R.string.add_query));
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
                        QueryActivity.super.onBackPressed();
                    }
                }
        );
        QueryParams params = new QueryParams();
        params.setName(resources.getString(R.string.select_query_type));
        params.setParamId("0");
        paramsList.add(params);
        tiComment.setHint("*" + resources.getString(R.string.enter_query));

        QuerySpinnerAdapter adapter = new QuerySpinnerAdapter(context, R.layout.spinner_layout, paramsList);
        typeSpinner.setAdapter(adapter);
        getQueryParameters();
        onCLicks();
    }

    private void onCLicks() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (typeSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(context, resources.getString(R.string.please_select_subject), Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(etQuery.getText().toString())) {
                    Toast.makeText(context, resources.getString(R.string.please_enter_query), Toast.LENGTH_LONG).show();
                    etQuery.setError(resources.getString(R.string.please_enter_query));
                }/*else  if (etQuery.getText().toString().length()<50){
                    Toast.makeText(context, "Please enter query", Toast.LENGTH_SHORT).show();
                }*/ else {
                    addQuery();
                }
            }
        });
    }

    private void addQuery() {
        try {
            submitBtn.setClickable(false);
            submitBtn.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            String query = etQuery.getText().toString().trim();
            String id = paramsList.get(typeSpinner.getSelectedItemPosition()).getParamId();
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            final boolean[] isLoaded = {false};
            long currMillis = AppConstants.getCurrentMills();
            apiInterface.addUserQuery(query, userId, token, id).enqueue(new Callback<StatusMsgModel>() {
                @Override
                public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                    String error = null;
                    isLoaded[0] = true;
                    Log.e(TAG, "Query code " + response.code());
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        submitBtn.setClickable(true);
                        submitBtn.setEnabled(true);
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(context, resources.getString(R.string.query_registered_successfully), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, LandingActivity.class);
                            ActivityOptions options = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                startActivity(intent, options.toBundle());
                                finishAffinity();
                            } else {
                                startActivity(intent);
                                finishAffinity();
                            }
                        } else if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(QueryActivity.this);
                        } else {
                            Toast.makeText(context, resources.getString(R.string.failed_to_submit_query) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                        }

                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(QueryActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(QueryActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            Toast.makeText(context, resources.getString(R.string.failed_to_submit_query) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                            error = response.errorBody().string().toString();
                            Log.e(TAG, "Query Add err " + response.errorBody().string() + " code " + response.code());
                            progressBar.setVisibility(View.GONE);
                            submitBtn.setClickable(true);
                            submitBtn.setEnabled(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                        notifyApiDelay(QueryActivity.this, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }
                }

                @Override
                public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                    Log.e(TAG, "Query Add fail " + t.toString());
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    isLoaded[0] = true;
                    notifyApiDelay(QueryActivity.this, call.request().url().toString(),
                            "" + diff, internetSPeed, t.toString(), 0);
                    progressBar.setVisibility(View.GONE);
                    submitBtn.setClickable(true);
                    submitBtn.setEnabled(true);
                    Toast.makeText(context, resources.getString(R.string.failed_to_submit_query) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();

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
            ViewFailDialog viewFailDialog = new ViewFailDialog();
            viewFailDialog.showDialogForFinish(QueryActivity.this, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg));
        }

    }

    private void getQueryParameters() {
        submitBtn.setClickable(false);
        submitBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getQueryParams(userId, token).enqueue(new Callback<QueryParamResponse>() {
            @Override
            public void onResponse(Call<QueryParamResponse> call, Response<QueryParamResponse> response) {
                String error = null;
                isLoaded[0] = true;
                Log.e(TAG, "GET Query CODE " + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG, "GET Query " + new Gson().toJson(response.body()));
                    progressBar.setVisibility(View.GONE);
                    submitBtn.setClickable(true);
                    submitBtn.setEnabled(true);

                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(QueryActivity.this);
                    } else {
                        if (response.body().getParamsList() != null && response.body().getParamsList().size() > 0) {
                            paramsList.addAll(response.body().getParamsList());
                        } else {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialogForFinish(QueryActivity.this, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg));
                        }
                    }

                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(QueryActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(QueryActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString() + ", code:" + response.code();

                        progressBar.setVisibility(View.GONE);
                        submitBtn.setClickable(true);
                        submitBtn.setEnabled(true);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialogForFinish(QueryActivity.this, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg));
                        Log.e(TAG, "Get err " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(QueryActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<QueryParamResponse> call, Throwable t) {
                Log.e(TAG, "Get fail " + t.toString());
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                isLoaded[0] = true;
                notifyApiDelay(QueryActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                progressBar.setVisibility(View.GONE);
                submitBtn.setClickable(true);
                submitBtn.setEnabled(true);
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialogForFinish(QueryActivity.this, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg));
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
                            ConnectivityUtils.checkSpeedWithColor(QueryActivity.this, new ConnectivityUtils.ColorListener() {
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
