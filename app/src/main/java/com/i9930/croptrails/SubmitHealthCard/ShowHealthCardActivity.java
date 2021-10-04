package com.i9930.croptrails.SubmitHealthCard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.FarmDetails.FullScreenDialog.FullScreenDialogHealthCard;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitHealthCard.Adapters.ShowHealthCardRvAdapter;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCardResponse;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

public class ShowHealthCardActivity extends AppCompatActivity {

    String TAG = "ShowHealthCardActivity";
    Context context;
    Toolbar mActionBarToolbar;
    ProgressDialog progressDialog;
    ViewFailDialog viewFailDialog;
    Resources resources;
    @BindView(R.id.healthCardProgress)
    ProgressBar healthCardProgress;
    @BindView(R.id.healthCardRecycler)
    RecyclerView healthCardRecycler;
    @BindView(R.id.no_data_available_health_card)
    RelativeLayout noDataFoundLayout;
    @BindView(R.id.addHeathCard)
    TextView addHeathCard;

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
        setContentView(R.layout.activity_show_health_card);
        resources = getResources();
        context = this;
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(resources.getString(R.string.please_wait_msg));
        viewFailDialog = new ViewFailDialog();
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        TextView tittle = findViewById(R.id.tittle);
        tittle.setText(resources.getString(R.string.soil_health_card_label));
        healthCardRecycler.setHasFixedSize(true);
        healthCardRecycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        onClicks();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getHealthCardData();
    }

    private void onClicks() {
        addHeathCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, AddSoilHealthCardActivity.class));
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void getHealthCardData() {
        healthCardProgress.setVisibility(View.VISIBLE);
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getPreviousHealthCard(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID)).enqueue(new Callback<HealthCardResponse>() {
            @Override
            public void onResponse(Call<HealthCardResponse> call, Response<HealthCardResponse> response) {
                if (response.isSuccessful()) {
                    healthCardProgress.setVisibility(View.GONE);
                    if (response.body().getStatus() == 10) {
                        viewFailDialog.showSessionExpireDialog(ShowHealthCardActivity.this, resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == 1) {
                        noDataFoundLayout.setVisibility(View.GONE);
                        healthCardRecycler.setVisibility(View.VISIBLE);
                        ShowHealthCardRvAdapter adapter = new ShowHealthCardRvAdapter(context, response.body(), new ShowHealthCardRvAdapter.CardClickListener() {
                            @Override
                            public void onClick(int position) {
                                FullScreenDialogHealthCard dialog = new FullScreenDialogHealthCard(response.body().getHealthCardList().get(position));
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                dialog.show(ft, FullScreenDialogHealthCard.TAG);
                            }
                        });

                        healthCardRecycler.setHasFixedSize(true);
                        healthCardRecycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                        healthCardRecycler.setAdapter(adapter);

                    } else {
                        noDataFoundLayout.setVisibility(View.VISIBLE);
                        healthCardRecycler.setVisibility(View.GONE);

                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(ShowHealthCardActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(ShowHealthCardActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        healthCardProgress.setVisibility(View.GONE);
                        viewFailDialog.showDialogForFinish(ShowHealthCardActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.something_went_wrong_msg) + ", " +
                                resources.getString(R.string.please_try_again_later_msg));
                        Log.e(TAG, "Err " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<HealthCardResponse> call, Throwable t) {
                healthCardProgress.setVisibility(View.GONE);
                viewFailDialog.showDialogForFinish(ShowHealthCardActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.something_went_wrong_msg) + ", " +
                        resources.getString(R.string.please_try_again_later_msg));

            }
        });
    }
}
