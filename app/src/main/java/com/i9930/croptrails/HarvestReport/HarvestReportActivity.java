package com.i9930.croptrails.HarvestReport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.HarvestReport.Adapter.HarvestAdapter;
import com.i9930.croptrails.HarvestReport.Model.HarvestDetailInnerData;
import com.i9930.croptrails.HarvestReport.Model.HarvestDetailMaster;
import com.i9930.croptrails.HarvestReport.Model.ViewHarvestDetails;
import com.i9930.croptrails.HarvestSubmit.EasyMode.HarvestSubmitEasyModeActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestCacheManager;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestFetchListener;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestT;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Locale;

public class HarvestReportActivity extends AppCompatActivity implements HarvestFetchListener {

    String TAG = "HarvestReportActivity";
    @BindView(R.id.harvest_recycler)
    RecyclerView harvest_recycler;
    @BindView(R.id.add_harvest_button)
    TextView add_harvest_button;
    @BindView(R.id.harvest_progress)
    GifImageView progressBar_cyclic;
    @BindView(R.id.no_data_available_harvest)
    RelativeLayout no_data_available_harvest;
    @BindView(R.id.harvest_report_parent_rel_lay)
    RelativeLayout harvest_report_parent_rel_lay;
    Context context;
    Resources resources;
    Toolbar mActionBarToolbar;
    TextView title;
    HarvestAdapter harvestAdapter;
    List<HarvestDetailMaster> harvestList;
    List<List<HarvestDetailInnerData>> harvestBagList;
    int noOfDays = 0;

    Call<ViewHarvestDetails> callHarvestData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String languageToLoad  = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
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
        setContentView(R.layout.activity_harvest_report);

        context = this;
        noOfDays = SharedPreferencesMethod.getInt(context, SharedPreferencesMethod.NO_OF_DAYS_OFFLINE);
        ButterKnife.bind(this);
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.dynamic_bag_act_title));
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        onClicks();


        add_harvest_button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_white_24dp, 0, 0, 0);
       /* if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.FARMER)) {
            add_harvest_button.setVisibility(View.GONE);
        } else {
            add_harvest_button.setVisibility(View.VISIBLE);
        }*/

    }

    private void onClicks() {
        add_harvest_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noOfDays > 3) {
                    Snackbar snackbar = Snackbar.make(harvest_report_parent_rel_lay, context.getResources().getString(R.string.no_of_days_offline_exceeded), Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                } else {
                    if (Double.parseDouble(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES)) > 0) {
                        startActivity(new Intent(context, HarvestSubmitEasyModeActivity.class));

                    } else {
                        Toast.makeText(getApplicationContext(), resources.getString(R.string.standing_area_not_available_harvest_msg),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar_cyclic.setVisibility(View.VISIBLE);
        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
            HarvestCacheManager.getInstance(context).getHarvest(this, farmId);
        } else {
            fetcHarvestData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    private void fetcHarvestData() {
        try {
            String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            Log.e(TAG, "Farm Id " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
            callHarvestData = apiService.getHarvestDetailStatus(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));

            callHarvestData.enqueue(new Callback<ViewHarvestDetails>() {
                @Override
                public void onResponse(Call<ViewHarvestDetails> call, Response<ViewHarvestDetails> response) {
                    try {

                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(HarvestReportActivity.this, resources.getString(R.string.multiple_login_msg));
                            } else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(HarvestReportActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(HarvestReportActivity.this, resources.getString(R.string.authorization_expired));
                            } else {
                                if (response.body() != null) {
                                    ViewHarvestDetails harvestDetails = response.body();
                                    if (harvestDetails.getData1() != null && harvestDetails.getData1().size() > 0) {
                                        harvestList = harvestDetails.getData1();
                                        harvestBagList = harvestDetails.getData();
                                        Log.e(TAG, "res 0 " + new Gson().toJson(response.body()));
                                        harvest_recycler.setHasFixedSize(true);
                                        harvestAdapter = new HarvestAdapter(context, harvestDetails.getData1(), harvestDetails.getData());
                                        harvestAdapter.notifyDataSetChanged();
                                        harvest_recycler.setAdapter(harvestAdapter);
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                        harvest_recycler.setLayoutManager(linearLayoutManager);
                                        int lastBagNo = 0;
                                        for (int i = 0; i < harvestDetails.getData().size(); i++) {
                                            for (int j = 0; j < harvestDetails.getData().get(i).size(); j++) {
                                                lastBagNo = Integer.parseInt(harvestDetails.getData().get(i).get(j).getBagNo());
                                            }
                                        }
                                    /*lastBagNo=Integer.parseInt(harvestDetails.getData().get(harvestDetails.getData().size()-1)
                                            .get(harvestDetails.getData().get(harvestDetails.getData().get(harvestDetails.getData().size()-1).size()).size()-1).getBagNo());*/
                                        SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.LAST_BAG_NO, lastBagNo + 1);

                                        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.FARMER)) {
                                            add_harvest_button.setVisibility(View.GONE);
                                        } else {
                                            add_harvest_button.setVisibility(View.VISIBLE);
                                        }

                                    } else {
                                        Log.e(TAG, "res 1 " + new Gson().toJson(response.body()));
                                        SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.LAST_BAG_NO, 1);
                                        harvest_recycler.setVisibility(View.GONE);
                                        no_data_available_harvest.setVisibility(View.VISIBLE);
                                        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.FARMER)) {
                                            add_harvest_button.setVisibility(View.GONE);
                                        } else {
                                            add_harvest_button.setVisibility(View.VISIBLE);
                                        }
                                    }
                                } else {
                                    Log.e(TAG, "res 3 " + new Gson().toJson(response.body()));
                                    SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.LAST_BAG_NO, 1);
                                    harvest_recycler.setVisibility(View.GONE);
                                    no_data_available_harvest.setVisibility(View.VISIBLE);
                                    if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.FARMER)) {
                                        add_harvest_button.setVisibility(View.GONE);
                                    } else {
                                        add_harvest_button.setVisibility(View.VISIBLE);
                                    }
                                }
                                progressBar_cyclic.setVisibility(View.INVISIBLE);
                            }
                        } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                            //un authorized access
                            progressBar_cyclic.setVisibility(View.INVISIBLE);
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(HarvestReportActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                            // auth token expired
                            progressBar_cyclic.setVisibility(View.INVISIBLE);
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(HarvestReportActivity.this, resources.getString(R.string.authorization_expired));
                        }else {
                            try {
                                add_harvest_button.setVisibility(View.GONE);
                                progressBar_cyclic.setVisibility(View.INVISIBLE);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showDialog(HarvestReportActivity.this, resources.getString(R.string.failed_to_load_previous_harvest_msg));
                                    }
                                });
                                Log.e(TAG, "res 4" + response.errorBody().string().toString());
                                //Toast.makeText(context, "Oops something went wrong. Please Reload", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "res 5" + e.toString());
                        add_harvest_button.setVisibility(View.INVISIBLE);
                        progressBar_cyclic.setVisibility(View.INVISIBLE);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(HarvestReportActivity.this, resources.getString(R.string.failed_to_load_previous_harvest_msg));
                    }
                }

                @Override
                public void onFailure(Call<ViewHarvestDetails> call, Throwable t) {
                    Log.e(TAG, "Failure HarvestT data " + t.getMessage().toString());
                    add_harvest_button.setVisibility(View.GONE);

                    if (t instanceof SocketTimeoutException) {
                        fetcHarvestData();
                        Log.e(TAG, "Failure HarvestT data recalling ");
                    } else {
                        //Toast.makeText(context, "Server Error. Please Reload", Toast.LENGTH_SHORT).show();
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialogForFinish(HarvestReportActivity.this, resources.getString(R.string.failed_to_load_previous_harvest_msg));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (harvestList != null) {
            harvestList.clear();
            if (harvestAdapter != null) {
                harvestAdapter.notifyDataSetChanged();
            }

        }
        if (harvestBagList != null) {
            harvestBagList.clear();
            if (harvestAdapter != null) {
                harvestAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (callHarvestData != null) {
            if (!callHarvestData.isExecuted()) {
                callHarvestData.cancel();
            }
        }
    }

    @Override
    public void onHarvestLoaded(HarvestT harvest) {
        if (harvest!=null){
        Gson gson = new Gson();
        ViewHarvestDetails harvestDetails = gson.fromJson(harvest.getHarvestJson(), ViewHarvestDetails.class);
        ViewHarvestDetails offlineDetails = gson.fromJson(harvest.getOfflineHarvest(), ViewHarvestDetails.class);
        if (harvestDetails != null) {
            if (harvestDetails.getData1() != null && harvestDetails.getData1().size() > 0) {
                harvestList = harvestDetails.getData1();
                harvestBagList = harvestDetails.getData();
                Log.e(TAG, "Harvest Data--> If " + harvest.getHarvestJson());
                // harvest_recycler.setHasFixedSize(true);

                if (offlineDetails != null && offlineDetails.getData1() != null && offlineDetails.getData1().size() > 0) {
                    harvestList.addAll(offlineDetails.getData1());
                    harvestBagList.addAll(offlineDetails.getData());
                }


                harvestAdapter = new HarvestAdapter(context, harvestList, harvestBagList);
                harvestAdapter.notifyDataSetChanged();
                harvest_recycler.setAdapter(harvestAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                harvest_recycler.setLayoutManager(linearLayoutManager);
                int lastBagNo = 0;
                for (int i = 0; i < harvestBagList.size(); i++) {
                    for (int j = 0; j < harvestBagList.get(i).size(); j++) {
                        lastBagNo = Integer.parseInt(harvestBagList.get(i).get(j).getBagNo());
                    }
                }
               /* lastBagNo = Integer.parseInt(harvestBagList.get(harvestBagList.size() - 1)
                        .get(harvestBagList.get(harvestBagList.get(harvestBagList.size() - 1).size()).size() - 1).getBagNo());*/
                SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.LAST_BAG_NO, lastBagNo + 1);

            } else if (offlineDetails.getData() != null && offlineDetails.getData().size() > 0) {
                harvestList = offlineDetails.getData1();
                harvestBagList = offlineDetails.getData();
                Log.e(TAG, "Harvest Data--> Else If " + harvest.getOfflineHarvest());
                Log.e(TAG, "Harvest DataMaster--> Else If " + new Gson().toJson(offlineDetails.getData1()));
                Log.e(TAG, "Harvest DataChild--> Else If " + new Gson().toJson(offlineDetails.getData()));
                //harvest_recycler.setHasFixedSize(true);

                harvestAdapter = new HarvestAdapter(context, harvestList, harvestBagList);
                harvestAdapter.notifyDataSetChanged();
                harvest_recycler.setAdapter(harvestAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                harvest_recycler.setLayoutManager(linearLayoutManager);
                int lastBagNo = 0;
                for (int i = 0; i < harvestBagList.size(); i++) {
                    for (int j = 0; j < harvestBagList.get(i).size(); j++) {
                        lastBagNo = Integer.parseInt(harvestBagList.get(i).get(j).getBagNo());
                    }
                }

                SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.LAST_BAG_NO, lastBagNo + 1);

            } else {
                Log.e(TAG, "Harvest Data--> Else " + harvest.getHarvestJson());
                SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.LAST_BAG_NO, 1);
                harvest_recycler.setVisibility(View.GONE);
                no_data_available_harvest.setVisibility(View.VISIBLE);


            }
        } else {
            Toast.makeText(context, "Harvest Data--> null", Toast.LENGTH_SHORT).show();
        }
        progressBar_cyclic.setVisibility(View.INVISIBLE);
        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.FARMER)) {
            add_harvest_button.setVisibility(View.GONE);
        } else {
            add_harvest_button.setVisibility(View.VISIBLE);
        }
    }}
}
