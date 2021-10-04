package com.i9930.croptrails.Notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
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
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Notification.Adapter.NotificationShowAdapter;
import com.i9930.croptrails.Notification.Model.ShowNotificationResponse;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class NotificationActivity extends AppCompatActivity {

    Context context;
    private final String TAG = "NotificationActivity";
    @BindView(R.id.notification_recycler)
    RecyclerView notification_recycler;
    ViewFailDialog viewFailDialog;
    @BindView(R.id.notification_progress)
    GifImageView progressBar;
    @BindView(R.id.no_data_available_notification)
    RelativeLayout no_data_available;
    Resources resources;
    Toolbar mActionBarToolbar;
    String noti_sch_id;
    String internetSPeed = "";
    @BindView(R.id.connectivityLine)
    View connectivityLine;
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
                            ConnectivityUtils.checkSpeedWithColor(NotificationActivity.this, new ConnectivityUtils.ColorListener() {
                                @Override
                                public void onColorChanged(int color, String speed) {
                                    if (color == android.R.color.holo_green_dark) {
                                        connectivityLine.setVisibility(View.GONE);
//                                    connectivityLine.setBackgroundColor(getColor(R.color.blue));
                                    } else {
                                        connectivityLine.setVisibility(View.VISIBLE);
                                        connectivityLine.setBackgroundColor(getColor(color));
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
        String languageToLoad  = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
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
        setContentView(R.layout.activity_notification);
        context = this;
        loadAds();
        ButterKnife.bind(this);
        viewFailDialog = new ViewFailDialog();
        progressBar.setVisibility(View.VISIBLE);
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        TextView title = (TextView) findViewById(R.id.tittle);
        //   Button submit_butt=(Button)findViewById(R.id.submit_test);
        title.setText(resources.getString(R.string.notification_label));
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            noti_sch_id = extras.getString("id");
            //Toast.makeText(contextlang, "Id "+noti_sch_id, Toast.LENGTH_LONG).show();
        }



        /*FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        // Log and toast
                       // String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, token);
                        Toast.makeText(NotificationActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("croptrails")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "subscribed";
                        if (!task.isSuccessful()) {
                            msg ="msg_subscribe_failed";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(NotificationActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            new FusedLocationService(this);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllNotifications();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    private void getAllNotifications() {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        String personId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token= SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        Log.e(TAG, "PersonId " + personId);
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();
        apiInterface.getListOfNotifications(personId,userId,token).enqueue(new Callback<ShowNotificationResponse>() {
            @Override
            public void onResponse(Call<ShowNotificationResponse> call, Response<ShowNotificationResponse> response) {
                String error=null;
                isLoaded[0]=true;
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Notification Res " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(NotificationActivity.this, resources.getString(R.string.multiple_login_msg));
                    } else
                    if (response.body().getStatus() == 1) {
                        NotificationShowAdapter adapter = new NotificationShowAdapter(NotificationActivity.this, response.body().getNotificationDataList());
                        notification_recycler.setHasFixedSize(true);
                        notification_recycler.setLayoutManager(new LinearLayoutManager(context));
                        notification_recycler.setAdapter(adapter);
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < response.body().getNotificationDataList().size(); i++) {
                            if (response.body().getNotificationDataList().get(i).getKeyType() != null &&
                                    response.body().getNotificationDataList().get(i).getKeyType().equals(AppConstants.OPEN_IN.SIMPLE_MSG)
                                    && response.body().getNotificationDataList().get(i).getIsRead().equals("N")) {
                                list.add(response.body().getNotificationDataList().get(i).getNotiSchId());
                            }
                        }
                        if (list.size() > 0) {
                            /*SetSimpleNotificationAsRead task=new SetSimpleNotificationAsRead();
                            task.execute(new Gson().toJson(list));*/
                        }

                        for (int i = 0; i < response.body().getNotificationDataList().size(); i++) {
                            if (noti_sch_id != null &&response.body().getNotificationDataList().get(i).getNotiSchId()!=null) {
                                adapter.onItemClick(i);
                                break;
                            }
                        }
                    } else {
                        notification_recycler.setVisibility(View.GONE);
                        no_data_available.setVisibility(View.VISIBLE);
                    }
                } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access

                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(NotificationActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(NotificationActivity.this, resources.getString(R.string.authorization_expired));
                }else {
                    try {
                        progressBar.setVisibility(View.GONE);
                        error=response.errorBody().string();
                        Log.e(TAG, "Notification Err " + error);
                        //viewFailDialog.showDialog(context, "Failed to load notification");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(NotificationActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<ShowNotificationResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                isLoaded[0]=true;
                notifyApiDelay(NotificationActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "Notification Failure " + t.toString());
                if (t instanceof SocketTimeoutException) {
                    getAllNotifications();
                } else {
                    //viewFailDialog.showDialog(context, "Failed to load notification");
                    progressBar.setVisibility(View.GONE);
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

    @Override
    protected void onStop() {
        super.onStop();
        noti_sch_id=null;
    }

    class SetSimpleNotificationAsRead extends AsyncTask<String, Void ,String> {


        @Override
        protected String doInBackground(String... strings) {
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
            String ids = strings[0];
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token= SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            apiInterface.setNotificationAsRead(ids,userId,token).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            Log.e(TAG, "Set As Read Res " + response.body().string().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(NotificationActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(NotificationActivity.this, resources.getString(R.string.authorization_expired));
                    }else {
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

}
