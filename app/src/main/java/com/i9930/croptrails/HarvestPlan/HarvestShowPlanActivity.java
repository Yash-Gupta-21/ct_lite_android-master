package com.i9930.croptrails.HarvestPlan;

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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.HarvestPlan.Adapter.ShowHarvestPlanAdapter;
import com.i9930.croptrails.HarvestPlan.Model.ShowHarvestPlanData;
import com.i9930.croptrails.HarvestPlan.Model.ShowHarvestPlanMain;
import com.i9930.croptrails.HarvestPlan.Model.ShowHarvestVehicleData;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class HarvestShowPlanActivity extends AppCompatActivity {

    Context context;
    public final String TAG = "HarvestShowPlanActivity";
    @BindView(R.id.recycler_show_palnned_harvest)
    RecyclerView recycler_show_palnned_harvest;
    ShowHarvestPlanAdapter showHarvestPlanAdapter;
    LinearLayoutManager linearLayoutManager;
    List<ShowHarvestPlanData> showHarvestPlanDataList = new ArrayList<>();
    List<ShowHarvestVehicleData> showHarvestVehicleData = new ArrayList<>();
    Toolbar mActionBarToolbar;
    Resources resources;
    @BindView(R.id.no_data_available_harvest_plan_show)
    RelativeLayout no_data_available;

    @BindView(R.id.timeline_progress)
    GifImageView loader;
    @BindView(R.id.connectivityLine)
    View connectivityLine;
    private interface SORT_OPTIONS {
        public final int DATE_ASCENDING = 1;
        public final int DATE_DESCENDING = 2;
        public final int NO_OF_BAG_ASCENDING = 3;
        public final int NO_OF_BAG_DESCENDING = 4;
        public final int NET_WEIGHT_ASCENDING = 5;
        public final int NET_WEIGHT_DESCENDING = 6;
        public final int DOA = 7;
    }

    private interface FILTER_OPTIONS {
        public final int UPCOMING_PIKCUPS = 7;
        public final int OUTDATED_PICKUPS = 8;
        public final int PICKED_UP = 9;
        public final int NON_PICKED_UP = 10;
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
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        setContentView(R.layout.activity_harvest_show_plan);
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        context = this;
        loadAds();
        ButterKnife.bind(this);
        TextView title = (TextView) findViewById(R.id.tittle);
        //   Button submit_butt=(Button)findViewById(R.id.submit_test);
        title.setText(resources.getString(R.string.harvest_plan));
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

                        HarvestShowPlanActivity.super.onBackPressed();
                    }
                }
        );
        showLoader();
        AsyncShowHarvestPlannedData asyncShowHarvestPlannedData = new AsyncShowHarvestPlannedData();
        asyncShowHarvestPlannedData.execute();

    }

    SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search, menu);


        return super.onCreateOptionsMenu(menu);
    }

    SearchAsynch searchAsynch;

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            MenuItem searchItem = item;
            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    return true;
                }
            });
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView = null;
            if (searchItem != null) {
                searchView = (SearchView) searchItem.getActionView();
            }
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    if (newText != null && !TextUtils.isEmpty(newText)) {
                        if (searchAsynch == null) {
                            searchAsynch = new SearchAsynch(newText.trim());
                            searchAsynch.execute();
                        } else {
                            searchAsynch.cancel(true);
                            searchAsynch = new SearchAsynch(newText.trim());
                            searchAsynch.execute();
                        }
                    } else {
                        setRecyclerData(showHarvestPlanDataList, showHarvestVehicleData);
                    }
                    return true;
                }

                public boolean onQueryTextSubmit(String newText) {
                    if (newText != null && !TextUtils.isEmpty(newText)) {
                        if (searchAsynch == null) {
                            searchAsynch = new SearchAsynch(newText.trim());
                            searchAsynch.execute();
                        } else {
                            searchAsynch.cancel(true);
                            searchAsynch = new SearchAsynch(newText.trim());
                            searchAsynch.execute();
                        }
                    } else {
                        setRecyclerData(showHarvestPlanDataList, showHarvestVehicleData);
                    }
                    return true;
                }
            };
            if (searchView != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setIconified(false);
                searchView.setOnQueryTextListener(queryTextListener);
                searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        searchView.setIconified(false);
                        searchView.setIconifiedByDefault(false);
                        searchItem.collapseActionView();

                        return false;
                    }
                });
            }


        } else if (item.getItemId() == R.id.action_filter) {
            showFilterDialog();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }


    public class AsyncShowHarvestPlannedData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            Log.e(TAG, "PersonId " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            Call<ShowHarvestPlanMain> expenseDataCall = apiService.getShowHarvestPlannedData(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            final boolean[] isLoaded = {false};
            long currMillis=AppConstants.getCurrentMills();
            expenseDataCall.enqueue(new Callback<ShowHarvestPlanMain>() {

                @Override
                public void onResponse(Call<ShowHarvestPlanMain> call, Response<ShowHarvestPlanMain> response) {
                    String error=null;
                    isLoaded[0]=true;
                    if (response.isSuccessful()) {
                        Log.e(TAG,"Resp "+new Gson().toJson(response.body()));
                        if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(HarvestShowPlanActivity.this, response.body().getMsg());
                        } else if (response.body().getStatus()== AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(HarvestShowPlanActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(HarvestShowPlanActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            Log.e("PERSON id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                            ShowHarvestPlanMain showHarvestPlanMain = response.body();
                            if (showHarvestPlanMain.getData() != null && showHarvestPlanMain.getData().size() > 0) {
                                showHarvestPlanDataList = showHarvestPlanMain.getData();
                                showHarvestVehicleData = showHarvestPlanMain.getVehicleDetails();
                                for (int i = 0; i < showHarvestPlanDataList.size(); i++) {
                                    try {
                                        showHarvestVehicleData.get(i).setNetWeight(showHarvestPlanDataList.get(i).getNetWtOfStock());
                                        showHarvestVehicleData.get(i).setNoOfBags(showHarvestPlanDataList.get(i).getNoOfBagsBlk());
                                        showHarvestVehicleData.get(i).setPickupDate(showHarvestPlanDataList.get(i).getPlanPickupDate());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                Log.e(TAG, "Plans Data " + new Gson().toJson(showHarvestPlanDataList));
                                Log.e(TAG, "Plans Vehicles" + new Gson().toJson(showHarvestVehicleData));
                                /*LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation);
                                showHarvestPlanAdapter = new ShowHarvestPlanAdapter(showHarvestPlanDataList, showHarvestVehicleData, context);
                                recycler_show_palnned_harvest.setHasFixedSize(true);
                                recycler_show_palnned_harvest.setAdapter(showHarvestPlanAdapter);
                                showHarvestPlanAdapter.notifyDataSetChanged();
                                linearLayoutManager = new LinearLayoutManager(context);
                                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                recycler_show_palnned_harvest.setLayoutManager(linearLayoutManager);
                                recycler_show_palnned_harvest.setLayoutAnimation(animation);*/

                                /// setRecyclerData(showHarvestPlanDataList, showHarvestVehicleData);
                                FilterAsync async = new FilterAsync(SORT_OPTIONS.DOA);
                                async.execute();

                            } else {
                            /*ViewFailDialog viewFailDialog=new ViewFailDialog();
                            viewFailDialog.showDialog(HarvestShowPlanActivity.this,resources.getString(R.string.failed_to_load_harvest_data));*/
                                no_data_available.setVisibility(View.VISIBLE);
                                recycler_show_palnned_harvest.setVisibility(View.GONE);
                            }
                        }
                        hideLoader();
                    }else if (response.code()== AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(HarvestShowPlanActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(HarvestShowPlanActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            error=response.errorBody().string();
                            hideLoader();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(HarvestShowPlanActivity.this, resources.getString(R.string.failed_to_load_harvest_data));
                            Log.e(TAG, "Show Planned err "+error);
                            //Toast.makeText(context, "Oops something went wrong. Please Reload", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                        notifyApiDelay(HarvestShowPlanActivity.this, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }
                }

                @Override
                public void onFailure(Call<ShowHarvestPlanMain> call, Throwable t) {
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    isLoaded[0]=true;
                    Log.e(TAG, "Show Planned fail "+t.toString());

                    notifyApiDelay(HarvestShowPlanActivity.this, call.request().url().toString(),
                            "" + diff, internetSPeed, t.toString(), 0);
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    hideLoader();
                    viewFailDialog.showDialog(HarvestShowPlanActivity.this, resources.getString(R.string.failed_to_load_harvest_data));
                    //Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
            try {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        internetFlow(isLoaded[0]);
                    }
                }, AppConstants.DELAY);
            }catch (Exception e){

            }
            return null;
        }
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
                            ConnectivityUtils.checkSpeedWithColor(HarvestShowPlanActivity.this, new ConnectivityUtils.ColorListener() {
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

    // Setting data to recyclerview
    private void setRecyclerData(List<ShowHarvestPlanData> planDataList, List<ShowHarvestVehicleData> vehicleDataList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation);
                showHarvestPlanAdapter = new ShowHarvestPlanAdapter(planDataList, vehicleDataList, context);
                recycler_show_palnned_harvest.setHasFixedSize(true);
                recycler_show_palnned_harvest.setAdapter(showHarvestPlanAdapter);
                showHarvestPlanAdapter.notifyDataSetChanged();
                linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recycler_show_palnned_harvest.setLayoutManager(linearLayoutManager);
                recycler_show_palnned_harvest.setLayoutAnimation(animation);
            }
        });

    }

    //Applying searching on list
    private class SearchAsynch extends AsyncTask<String, Integer, String> {
        String query;
        List<ShowHarvestPlanData> planDataList = new ArrayList<>();
        List<ShowHarvestVehicleData> vehicleDataList = new ArrayList<>();

        public SearchAsynch(String query) {
            this.query = query.trim();
        }

        @Override
        protected String doInBackground(String... strings) {
            planDataList.clear();
            vehicleDataList.clear();
            if (showHarvestPlanDataList != null) {
                for (int i = 0; i < showHarvestPlanDataList.size(); i++) {
                    try {
                        ShowHarvestPlanData data = showHarvestPlanDataList.get(i);
                        ShowHarvestVehicleData vehicleData = showHarvestVehicleData.get(i);
                        if (data.getAgentCode() != null && data.getAgentCode().toLowerCase().contains(query.toLowerCase())) {
                            planDataList.add(data);
                            vehicleDataList.add(vehicleData);
                        } else if (data.getNoOfBagsBlk() != null && data.getNoOfBagsBlk().toLowerCase().contains(query.toLowerCase())) {
                            planDataList.add(data);
                            vehicleDataList.add(vehicleData);
                        } else if (data.getTotalBags() != null && data.getTotalBags().toLowerCase().contains(query.toLowerCase())) {
                            planDataList.add(data);
                            vehicleDataList.add(vehicleData);
                        } else if (data.getAvgMoisture() != null && data.getAvgMoisture().toLowerCase().contains(query.toLowerCase())) {
                            planDataList.add(data);
                            vehicleDataList.add(vehicleData);
                        } else if (data.getOfficeRemarks() != null && data.getOfficeRemarks().toLowerCase().contains(query.toLowerCase())) {
                            planDataList.add(data);
                            vehicleDataList.add(vehicleData);
                        } else if (data.getRemark() != null && data.getRemark().toLowerCase().contains(query.toLowerCase())) {
                            planDataList.add(data);
                            vehicleDataList.add(vehicleData);
                        } else if (data.getNetWtOfStock() != null && data.getNetWtOfStock().toLowerCase().contains(query.toLowerCase())) {
                            planDataList.add(data);
                            vehicleDataList.add(vehicleData);
                        } else if (data.getPlanPickupDate() != null && data.getPlanPickupDate().toLowerCase().contains(query.toLowerCase())) {
                            planDataList.add(data);
                            vehicleDataList.add(vehicleData);
                        } else if (vehicleData.getDriverName() != null && vehicleData.getDriverName().toLowerCase().contains(query.toLowerCase())) {
                            planDataList.add(data);
                            vehicleDataList.add(vehicleData);
                        } else if (vehicleData.getVehicleNetWt() != null && vehicleData.getVehicleNetWt().toLowerCase().contains(query.toLowerCase())) {
                            planDataList.add(data);
                            vehicleDataList.add(vehicleData);
                        } else if (vehicleData.getVehicleNo() != null && vehicleData.getVehicleNo().toLowerCase().contains(query.toLowerCase())) {
                            planDataList.add(data);
                            vehicleDataList.add(vehicleData);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (planDataList.size() > 0) {
                        setRecyclerData(planDataList, vehicleDataList);
                    } else {
                        no_data_available.setVisibility(View.VISIBLE);
                        recycler_show_palnned_harvest.setVisibility(View.GONE);
                    }
                }
            });
            return null;
        }
    }

    // Filter bottom sheet dialog
    private void showFilterDialog() {

        View view = getLayoutInflater().inflate(R.layout.filter_options_for_planned_harvest, null);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.setCancelable(true);

        final TextView filterDateAscending = dialog.findViewById(R.id.filterDateAscending);
        final TextView filterDateDescending = dialog.findViewById(R.id.filterDateDescending);
        final TextView filterNoOfBagAsc = dialog.findViewById(R.id.filterNoOfBagAsc);
        final TextView filterNoOfBagD = dialog.findViewById(R.id.filterNoOfBagD);
        final TextView filterNetWeightAsc = dialog.findViewById(R.id.filterNetWeightAsc);
        final TextView filterNetWeightD = dialog.findViewById(R.id.filterNetWeightD);
        String pickDateStr = resources.getString(R.string.pick_up_date);
        String noOfBagsStr = resources.getString(R.string.no_of_bags);
        String netWeightStr = resources.getString(R.string.net_weight_label);

        String higtToLowStr = resources.getString(R.string.high_to_low);
        String lowToHighStr = resources.getString(R.string.low_to_high);

        filterDateDescending.setText(pickDateStr + " -- " + higtToLowStr);
        filterDateAscending.setText(pickDateStr + " -- " + lowToHighStr);
        filterNoOfBagD.setText(noOfBagsStr + " -- " + higtToLowStr);
        filterNoOfBagAsc.setText(noOfBagsStr + " -- " + lowToHighStr);
        filterNetWeightD.setText(netWeightStr + " -- " + higtToLowStr);
        filterNetWeightAsc.setText(netWeightStr + " -- " + lowToHighStr);

        filterDateAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FilterAsync async = new FilterAsync(SORT_OPTIONS.DATE_ASCENDING);
                async.execute();
            }
        });
        filterDateDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FilterAsync async = new FilterAsync(SORT_OPTIONS.DATE_DESCENDING);
                async.execute();
            }
        });
        filterNoOfBagAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FilterAsync async = new FilterAsync(SORT_OPTIONS.NO_OF_BAG_ASCENDING);
                async.execute();
            }
        });
        filterNoOfBagD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FilterAsync async = new FilterAsync(SORT_OPTIONS.NO_OF_BAG_DESCENDING);
                async.execute();
            }
        });
        filterNetWeightAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FilterAsync async = new FilterAsync(SORT_OPTIONS.NET_WEIGHT_ASCENDING);
                async.execute();
            }
        });
        filterNetWeightD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FilterAsync async = new FilterAsync(SORT_OPTIONS.NET_WEIGHT_DESCENDING);
                async.execute();

            }
        });
        dialog.show();

    }

    private class FilterAsync extends AsyncTask<String, Integer, String> {
        int filter;
        List<ShowHarvestPlanData> planDataList = new ArrayList<>();
        List<ShowHarvestVehicleData> vehicleDataList = new ArrayList<>();

        public FilterAsync(int filter) {
            this.filter = filter;
        }
        @Override
        protected String doInBackground(String... strings) {
            planDataList.clear();
            vehicleDataList.clear();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            //Sort options
            if (filter == SORT_OPTIONS.DOA) {
                planDataList.addAll(showHarvestPlanDataList);
                vehicleDataList.addAll(showHarvestVehicleData);
                Collections.sort(planDataList, new Comparator<ShowHarvestPlanData>() {
                    @Override
                    public int compare(ShowHarvestPlanData lhs, ShowHarvestPlanData rhs) {
                        try {
                            return format.parse(rhs.getDoa()).compareTo(format.parse(lhs.getDoa()));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                Collections.sort(vehicleDataList, new Comparator<ShowHarvestVehicleData>() {
                    @Override
                    public int compare(ShowHarvestVehicleData lhs, ShowHarvestVehicleData rhs) {
                        try {
                            if (rhs.getDoa()!=null&&lhs.getDoa()!=null)
                            return format.parse(rhs.getDoa()).compareTo(format.parse(lhs.getDoa()));
                            else return vehicleDataList.size()-1;
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                setRecyclerData(planDataList, vehicleDataList);
            } else if (filter == SORT_OPTIONS.DATE_ASCENDING) {
                planDataList.addAll(showHarvestPlanDataList);
                vehicleDataList.addAll(showHarvestVehicleData);
                Collections.sort(planDataList, new Comparator<ShowHarvestPlanData>() {
                    @Override
                    public int compare(ShowHarvestPlanData lhs, ShowHarvestPlanData rhs) {
                        try {
                            return format.parse(lhs.getPlanPickupDate()).compareTo(format.parse(rhs.getPlanPickupDate()));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                Collections.sort(vehicleDataList, new Comparator<ShowHarvestVehicleData>() {
                    @Override
                    public int compare(ShowHarvestVehicleData lhs, ShowHarvestVehicleData rhs) {
                        try {
                            return format.parse(lhs.getPickupDate()).compareTo(format.parse(rhs.getPickupDate()));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                setRecyclerData(planDataList, vehicleDataList);
            } else if (filter == SORT_OPTIONS.DATE_DESCENDING) {
                planDataList.addAll(showHarvestPlanDataList);
                vehicleDataList.addAll(showHarvestVehicleData);
                Collections.sort(planDataList, new Comparator<ShowHarvestPlanData>() {
                    @Override
                    public int compare(ShowHarvestPlanData lhs, ShowHarvestPlanData rhs) {
                        try {
                            return format.parse(rhs.getPlanPickupDate()).compareTo(format.parse(lhs.getPlanPickupDate()));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                Collections.sort(vehicleDataList, new Comparator<ShowHarvestVehicleData>() {
                    @Override
                    public int compare(ShowHarvestVehicleData lhs, ShowHarvestVehicleData rhs) {
                        try {
                            return format.parse(rhs.getPickupDate()).compareTo(format.parse(lhs.getPickupDate()));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                setRecyclerData(planDataList, vehicleDataList);

            } else if (filter == SORT_OPTIONS.NO_OF_BAG_ASCENDING) {
                planDataList.addAll(showHarvestPlanDataList);
                vehicleDataList.addAll(showHarvestVehicleData);
                Collections.sort(planDataList, new Comparator<ShowHarvestPlanData>() {
                    @Override
                    public int compare(ShowHarvestPlanData lhs, ShowHarvestPlanData rhs) {
                        try {
                            return Float.valueOf(lhs.getNoOfBagsBlk().trim()).compareTo(Float.valueOf(rhs.getNoOfBagsBlk().trim()));
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                Collections.sort(vehicleDataList, new Comparator<ShowHarvestVehicleData>() {
                    @Override
                    public int compare(ShowHarvestVehicleData lhs, ShowHarvestVehicleData rhs) {
                        try {
                            return Float.valueOf(lhs.getNoOfBags().trim()).compareTo(Float.valueOf(rhs.getNoOfBags().trim()));
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                setRecyclerData(planDataList, vehicleDataList);
            } else if (filter == SORT_OPTIONS.NO_OF_BAG_DESCENDING) {
                planDataList.addAll(showHarvestPlanDataList);
                vehicleDataList.addAll(showHarvestVehicleData);
                Collections.sort(planDataList, new Comparator<ShowHarvestPlanData>() {
                    @Override
                    public int compare(ShowHarvestPlanData lhs, ShowHarvestPlanData rhs) {
                        try {
                            return Float.valueOf(rhs.getTotalBags().trim()).compareTo(Float.valueOf(lhs.getTotalBags().trim()));
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                Collections.sort(vehicleDataList, new Comparator<ShowHarvestVehicleData>() {
                    @Override
                    public int compare(ShowHarvestVehicleData lhs, ShowHarvestVehicleData rhs) {
                        try {
                            return Float.valueOf(rhs.getNoOfBags().trim()).compareTo(Float.valueOf(lhs.getNoOfBags().trim()));
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                setRecyclerData(planDataList, vehicleDataList);
            } else if (filter == SORT_OPTIONS.NET_WEIGHT_ASCENDING) {
                planDataList.addAll(showHarvestPlanDataList);
                vehicleDataList.addAll(showHarvestVehicleData);
                Collections.sort(planDataList, new Comparator<ShowHarvestPlanData>() {
                    @Override
                    public int compare(ShowHarvestPlanData lhs, ShowHarvestPlanData rhs) {
                        try {
                            return Float.valueOf(lhs.getNetWtOfStock().trim()).compareTo(Float.valueOf(rhs.getNetWtOfStock().trim()));
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                Collections.sort(vehicleDataList, new Comparator<ShowHarvestVehicleData>() {
                    @Override
                    public int compare(ShowHarvestVehicleData lhs, ShowHarvestVehicleData rhs) {
                        try {
                            return Float.valueOf(lhs.getNetWeight().trim().trim()).compareTo(Float.valueOf(rhs.getNetWeight().trim()));
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                setRecyclerData(planDataList, vehicleDataList);
            } else if (filter == SORT_OPTIONS.NET_WEIGHT_DESCENDING) {
                planDataList.addAll(showHarvestPlanDataList);
                vehicleDataList.addAll(showHarvestVehicleData);
                Collections.sort(planDataList, new Comparator<ShowHarvestPlanData>() {
                    @Override
                    public int compare(ShowHarvestPlanData lhs, ShowHarvestPlanData rhs) {
                        try {
                            return Float.valueOf(rhs.getNetWtOfStock().trim()).compareTo(Float.valueOf(lhs.getNetWtOfStock().trim()));
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                Collections.sort(vehicleDataList, new Comparator<ShowHarvestVehicleData>() {
                    @Override
                    public int compare(ShowHarvestVehicleData lhs, ShowHarvestVehicleData rhs) {
                        try {
                            return Float.valueOf(rhs.getNetWeight().trim()).compareTo(Float.valueOf(lhs.getNetWeight().trim()));
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                setRecyclerData(planDataList, vehicleDataList);
            }
            //Filter options
            else if (filter == FILTER_OPTIONS.UPCOMING_PIKCUPS) {

            } else if (filter == FILTER_OPTIONS.OUTDATED_PICKUPS) {

            } else if (filter == FILTER_OPTIONS.PICKED_UP) {

            } else if (filter == FILTER_OPTIONS.NON_PICKED_UP) {

            }

            return null;
        }
    }

    private void showLoader(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loader.setVisibility(View.VISIBLE);
            }
        });
    }
    private void hideLoader(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loader.setVisibility(View.GONE);
            }
        });
    }
}
