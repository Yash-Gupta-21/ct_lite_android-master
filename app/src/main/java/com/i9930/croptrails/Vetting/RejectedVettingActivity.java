package com.i9930.croptrails.Vetting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.Landing.Adapter.FarmDetailAdapter;
import com.i9930.croptrails.Landing.Adapter.FarmDetailAdapterOffline;
import com.i9930.croptrails.Landing.Adapter.FarmerFarmAdapter;
import com.i9930.croptrails.Landing.Adapter.NewFarmSearchAdapter;
import com.i9930.croptrails.Landing.Fragments.Pagination.PaginationScrollListener;
import com.i9930.croptrails.Landing.Models.Farmer.FetchFarmerFarmResponse;
import com.i9930.croptrails.Landing.Models.FetchFarmData;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.Landing.Models.FetchFarmSendData;
import com.i9930.croptrails.Landing.Models.Vetting.VettingFarm;
import com.i9930.croptrails.Landing.Models.Vetting.VettingFarmResponse;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmCacheManager;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmNotifyInterface;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;
import com.i9930.croptrails.Vetting.Adapter.VettingFarmAdapter;
import com.i9930.croptrails.Vetting.Model.VettingSearchBody;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class RejectedVettingActivity extends AppCompatActivity implements FarmDetailAdapter.FarmDetailAdapterListener,
        SwipeRefreshLayout.OnRefreshListener, FarmNotifyInterface, LocationListener {

    RejectedVettingActivity context ;
    String TAG="RejectedVettingActivity";
    Toolbar mActionBarToolbar;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = getResources();
        String languageToLoad = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_vetting);

        context = this;
//        initView();
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RejectedVettingActivity.this.onBackPressed();
            }
        });
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.rejected_farms));

        comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        user_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        cluster_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID);

        connectivityLine=findViewById(R.id.connectivityLine);
        initViews();
        //getAppTour();
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(resources.getColor(android.R.color.holo_green_dark),
                resources.getColor(android.R.color.holo_red_dark),
                resources.getColor(android.R.color.holo_blue_dark),
                resources.getColor(android.R.color.holo_orange_dark));
        offline_mode = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE);
        isLocationFound();
        if (!SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.FARMER)) {

            if (!offline_mode) {
                farmDetailAdapter = new VettingFarmAdapter(context,fetchFarmResultList);
                isScrolling = true;
                sort_by = null;
                order = null;
//                if (!SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.FARMER))
//                    fabParentAddFarm.setVisibility(View.VISIBLE);
//                else
//                    fabParentAddFarm.setVisibility(View.GONE);
                linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                farm_recycler_view.setLayoutManager(linearLayoutManager);

                farm_recycler_view.setItemAnimator(new DefaultItemAnimator());
                farm_recycler_view.setAdapter(farmDetailAdapter);
                if (fetchFarmDataCall != null) {
                    fetchFarmDataCall.cancel();
                }
                farm_recycler_view.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
                    @Override
                    protected void loadMoreItems() {

                        if (isScrolling) {

                            isLoading = true;
                            currentPage += 1;
                            loadNextPage();
                        }
                    }

                    @Override
                    public int getTotalPageCount() {
                        Log.e("ScrollListner", "Total pages " + TOTAL_PAGES);
                        return TOTAL_PAGES;
                    }

                    @Override
                    public boolean isLastPage() {
                        Log.e("ScrollListner", "is last pages " + isLastPage);
                        return isLastPage;
                    }

                    @Override
                    public boolean isLoading() {
                        Log.e("ScrollListner", "is loading " + isLoading);
                        return isLoading;
                    }
                });

                farm_recycler_view.setNestedScrollingEnabled(true);

                /*if (location != null) {
                    getNearFarms(location);
                } else {
                    ReloadData();
                }*/
                ReloadData();

            } else {

                progressBar.setVisibility(View.VISIBLE);
                no_internet_layout.setVisibility(View.GONE);
                farm_recycler_view.setVisibility(View.VISIBLE);
                FarmCacheManager.getInstance(context).getAllFarms(this);
                //fab_sort.setVisibility(View.GONE);
                //fabParentAddFarm.setVisibility(View.GONE);
            }
            //Toast.makeText(context, "Supervisor", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(context, "farmer", Toast.LENGTH_SHORT).show();
            loadFarmerFarms();
            //fab_sort.setVisibility(View.GONE);

        }


       /* search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (farmDetailAdapter != null) {
                    farmDetailAdapter.notifyDataSetChanged();
                    //farmDetailAdapter.getFilter().filter(et_search.getText().toString());
                }
            }
        });*/

       /* //fab_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
                Log.e("fab", "y");
            }
        });
        //fab_sort.setVisibility(View.GONE);*/
        et_sort_standing_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                sort_by = "standing_acres";
                order = "desc";

                progressBar.setVisibility(View.VISIBLE);
                if (farmDetailAdapter != null&&farmDetailAdapter.getFetchFarmResults()!=null) {
                    farmDetailAdapter.getFetchFarmResults().clear();
                    farmDetailAdapter.notifyDataSetChanged();
                }

                currentPage = 1;
                isLastPage = false;
                loadFirstPage();
                Toasty.info(context, "farms sorted according to standing area", Toast.LENGTH_LONG).show();
                Log.e("standing area", "y");

            }
        });
        et_sort_standing_area_l_to_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                sort_by = "standing_acres";
                order = "asc";

                progressBar.setVisibility(View.VISIBLE);
                if (farmDetailAdapter != null&&farmDetailAdapter.getFetchFarmResults()!=null) {
                    farmDetailAdapter.getFetchFarmResults().clear();
                    farmDetailAdapter.notifyDataSetChanged();
                }

                currentPage = 1;
                isLastPage = false;
                loadFirstPage();
                Toasty.info(context, "farms sorted according to standing area", Toast.LENGTH_LONG).show();


            }
        });
        et_sort_expected_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Log.e("expected area", "y");


            }
        });
        et_sort_actual_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Log.e("actual area", "y");


            }
        });
        et_sort_available_area_h_to_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                sort_by = "farm_area";
                order = "desc";


                Log.e("available area h to l", "y");

            }
        });
        et_sort_available_area_l_to_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();

                Log.e("available arae l to h", "y");


            }
        });
        et_sort_harvest_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                sort_by = "exp_harvest_date";
                order = "desc";
                progressBar.setVisibility(View.VISIBLE);
                if (farmDetailAdapter != null&&farmDetailAdapter.getFetchFarmResults()!=null) {
                    farmDetailAdapter.getFetchFarmResults().clear();
                    farmDetailAdapter.notifyDataSetChanged();
                }
                currentPage = 1;
                isLastPage = false;

                loadFirstPage();
                Toasty.info(context, "farms sorted according to harvest date", Toast.LENGTH_LONG).show();
                Log.e("harvest date", "y");


            }
        });
        /*if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.FARMER)) {

            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.FARMER_ALLOWED_ADD_FARM) && !offline_mode) {
                fabParentAddFarm.setVisibility(View.VISIBLE);
            } else {
                fabParentAddFarm.setVisibility(View.GONE);
            }

        } else {
            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.SUPERVISOR_ALLOWED_ADD_FARM)*//* && !offline_mode*//*) {
                fabParentAddFarm.setVisibility(View.VISIBLE);
            } else {
                fabParentAddFarm.setVisibility(View.GONE);//INVISIBLE
            }
        }*/



    }

    Location location;

    private void isLocationFound() {
        boolean isGPSEnable = false;
        boolean isNetworkEnable = false;
        LocationManager locationManager;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnable && !isNetworkEnable) {
                Log.e(TAG, "Nothing Enabled");
            } else {
                if (isGPSEnable) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1 * 60 * 1000, 300, RejectedVettingActivity.this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            Log.e(TAG, "gps lat:" + location.getLatitude());
                        }
                    }
                } else if (isNetworkEnable) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1 * 60 * 1000, 300, RejectedVettingActivity.this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            Log.e(TAG, "net lat:" + location.getLatitude());
                        }
                    }
                }
            }

        } else {
            Log.e(TAG, "Permission not grant");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_singl, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
//                searchView.requestFocus();
//                Toast.makeText(context, "onMenuItemActionExpand called", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                loadFirstPage();
                return true;
            }
        });


        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @SuppressLint("RestrictedApi")
            public boolean onQueryTextChange(String query) {

                if (query != null && query.length() > 1) {

                    getSearchResult(query);
                }else {

                }
                return true;
            }

            @SuppressLint("RestrictedApi")
            public boolean onQueryTextSubmit(String query) {
                if (query != null && query.length() > 1) {
                    getSearchResult(query);

                }else {

                }
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        MenuItem filterItem = menu.findItem(R.id.action_filter);
        if(filterItem!=null){
            filterItem.setVisible(false);
        }
        return true;
    }


    private void getSearchResult(String keyword) {
        if (farmDetailAdapter!=null){
            farmDetailAdapter.clearAll();
        }
        if (keyword!=null&&keyword.length() > 0) {
            final boolean[] isLoaded = {false};
            long currMillis = AppConstants.getCurrentMills();
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            String clusterId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID);
            String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            {
                VettingSearchBody body=new VettingSearchBody(userId,token,compId,VettingSearchBody.FILTER.REJECTED,keyword.toString().trim(),clusterId);
                Call<VettingFarmResponse> fetchFarmDataCallVetting=apiService.getVettingSearch(body);
                fetchFarmDataCallVetting.enqueue(new Callback<VettingFarmResponse>() {
                    @Override
                    public void onResponse(Call<VettingFarmResponse> call, Response<VettingFarmResponse> response) {
                        String error=null;
                        isLoaded[0]=true;
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                            } else {
                                VettingFarmResponse fetchFarmData = response.body();
                                Log.e(TAG, "First Page Res " + new Gson().toJson(fetchFarmData));
                                TOTAL_PAGES = fetchFarmData.getPages();
                                if (fetchFarmData.getData() != null && fetchFarmData.getData().size() > 0) {
                                    no_data_available.setVisibility(View.GONE);
                                    farm_recycler_view.setVisibility(View.VISIBLE);
                                    farm_recycler_view.setNestedScrollingEnabled(true);
                                    fetchFarmResultList = fetchFarmData.getData();
                                    farmDetailAdapter=new VettingFarmAdapter(context,fetchFarmData.getData());
                                    farm_recycler_view.setAdapter(farmDetailAdapter);
                                    linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                    farm_recycler_view.setLayoutManager(linearLayoutManager);
                                    farm_recycler_view.setItemAnimator(new DefaultItemAnimator());
                                    progressBar.setVisibility(View.INVISIBLE);
//                                    farmDetailAdapterVetting.addAll(fetchFarmResultListVetting);
                                    farm_recycler_view.setHasFixedSize(true);
                                    farm_recycler_view.setNestedScrollingEnabled(true);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    farm_recycler_view.setHasFixedSize(true);
                                    farm_recycler_view.setAdapter(farmDetailAdapter);
                                    farm_recycler_view.setVisibility(View.GONE);
                                    no_internet_layout.setVisibility(View.GONE);
                                    no_data_available.setVisibility(View.VISIBLE);
                                }
                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                        } else {
                            error = response.errorBody().toString();
                            Log.e(TAG, "error search " + error);
                            progressBar.setVisibility(View.INVISIBLE);
                            farmDetailAdapter.clearAll();
                        }

                        long newMillis=AppConstants.getCurrentMills();
                        long diff=newMillis-currMillis;
                        if(diff>=DELAY_API||(diff<DELAY_API&&error!=null&&!TextUtils.isEmpty(error))||response.code()==500) {
                            notifyApiDelay(context, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<VettingFarmResponse> call, Throwable t) {
                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        notifyApiDelay(context, call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);
                        Log.e(TAG, "failure search" + t.toString());
                        progressBar.setVisibility(View.INVISIBLE);
                        farmDetailAdapter.clearAll();
                    }
                });
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    internetFlow(isLoaded[0]);
                }
            }, AppConstants.DELAY);

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
                            ConnectivityUtils.checkSpeedWithColor(context, new ConnectivityUtils.ColorListener() {
                                @Override
                                public void onColorChanged(int color, String speed) {
                                    if (color == android.R.color.holo_green_dark) {
                                        connectivityLine.setVisibility(View.GONE);
//                                    connectivityLine.setBackgroundColor(getColor(R.color.blue));
                                    } else {
                                        connectivityLine.setVisibility(View.VISIBLE);
                                        connectivityLine.setBackgroundColor(resources.getColor(color));
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
    private void ReloadData() {
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
        if (connected) {
            //fab_sort.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            no_internet_layout.setVisibility(View.GONE);
            farm_recycler_view.setVisibility(View.VISIBLE);
            loadFirstPage();

        } else {
            no_internet_layout.setVisibility(View.VISIBLE);
            farm_recycler_view.setVisibility(View.GONE);
            //fab_sort.setVisibility(View.GONE);
        }

    }

    // try {
    public void loadFirstPage() {
        if (farmDetailAdapter!=null){
            farmDetailAdapter.clearAll();
        }
        progressImage.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        FetchFarmSendData fetchFarmSendData = new FetchFarmSendData();
        fetchFarmSendData.setOffset(String.valueOf(currentPage));
        fetchFarmSendData.setSize(pageItemCount);
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        Log.e(TAG,"jwt "+auth);
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        fetchFarmSendData.setComp_id(comp_id);
        fetchFarmSendData.setCluster_id(cluster_id);
        fetchFarmSendData.setOrder(order);
        fetchFarmSendData.setSort_by(sort_by);
        fetchFarmSendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        fetchFarmSendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        Log.e(TAG, "Data Sending First" + new Gson().toJson(fetchFarmSendData));
        fetchFarmDataCall = apiService.getVettingRejected(fetchFarmSendData);
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();
        fetchFarmDataCall.enqueue(new Callback<VettingFarmResponse>() {
            @Override
            public void onResponse(Call<VettingFarmResponse> call, Response<VettingFarmResponse> response) {
                String error=null;
                fetchFarmResultList = new ArrayList<>();
                progressImage.setVisibility(View.GONE);
                Log.e(TAG,"first response code "+response.code());
                Log.e(TAG,"first response  "+new Gson().toJson(response.body()));
                try {

                    if (response.isSuccessful()) {
                        if (response != null) {
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context);
                            }
                            else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                            }else if (response.body().getStatus() == 0) {

                                progressBar.setVisibility(View.GONE);
                                no_data_available.setVisibility(View.VISIBLE);
                                farm_recycler_view.setVisibility(View.GONE);
                                no_internet_layout.setVisibility(View.GONE);
                                Log.e(TAG, "No Data");
                                try {
                                    context.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
//                                            fabChildAddExistingFarm.setVisibility(View.GONE);
                                        }
                                    });
                                }catch (Exception e){
                                    e.printStackTrace();
                                }



                            } else {
                                VettingFarmResponse fetchFarmData = response.body();
                                Log.e(TAG, "First Page Res " + new Gson().toJson(fetchFarmData));
//                                TOTAL_PAGES = fetchFarmData.getPages();
//                                Log.e(TAG, "total_pages  " + fetchFarmData.getPages().toString());
                                if (fetchFarmData.getData() != null && fetchFarmData.getData().size() > 0) {
                                    no_data_available.setVisibility(View.GONE);
                                    farm_recycler_view.setVisibility(View.VISIBLE);
                                    farm_recycler_view.setNestedScrollingEnabled(true);
                                    fetchFarmResultList = fetchFarmData.getData();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    farmDetailAdapter.addAll(fetchFarmResultList);
                                    if (currentPage <= TOTAL_PAGES) {
                                        isLastPage=true;
                                    } else {
                                        farmDetailAdapter.addLoadingFooter();
                                    }
                                    farm_recycler_view.setHasFixedSize(true);
                                    farm_recycler_view.setNestedScrollingEnabled(true);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    farm_recycler_view.setHasFixedSize(true);
                                    farm_recycler_view.setAdapter(farmDetailAdapter);
                                    farm_recycler_view.setVisibility(View.GONE);
                                    no_internet_layout.setVisibility(View.GONE);
                                    no_data_available.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            no_data_available.setVisibility(View.VISIBLE);
                            farm_recycler_view.setVisibility(View.GONE);
                            no_internet_layout.setVisibility(View.GONE);
                            Log.e(TAG, "No Data 2");
                        }
                    }  else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                    } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                    }else {
                        try {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(context, resources.getString(R.string.failed_load_farm));
                            progressBar.setVisibility(View.INVISIBLE);
                            //Toast.makeText(context, "Oops something went wrong. Please Reload", Toast.LENGTH_SHORT).show();
                            error=response.errorBody().string().toString();
                            Log.e(TAG, "Error " + error);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    swipeRefreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    try {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_load_farm));
                        progressBar.setVisibility(View.INVISIBLE);
                        //Toast.makeText(context, "Oops something went wrong. Please Reload", Toast.LENGTH_SHORT).show();
                        error=response.errorBody().string().toString();
                        Log.e(TAG, "Error2" + error);
                    } catch (Exception f) {
                        f.printStackTrace();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
                    notifyApiDelay(context, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<VettingFarmResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(context, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showDialog(context, resources.getString(R.string.failed_load_farm));
                    progressBar.setVisibility(View.INVISIBLE);
                    //Toast.makeText(context, "Oops something went wrong. Please Reload", Toast.LENGTH_SHORT).show();
                } catch (Exception f) {
                    f.printStackTrace();
                }                //Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Farms Exception" + t.toString());
            }
        });

        internetFlow(isLoaded[0]);

        /*   }catch (Exception r){
               r.printStackTrace();
           }
            return null;
        }
    }*/
    }

    public void loadNextPage() {
        FetchFarmSendData fetchFarmSendData = new FetchFarmSendData();
        fetchFarmSendData.setOffset(String.valueOf(currentPage));
        Log.e(TAG, "current_page " + String.valueOf(currentPage));
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        fetchFarmSendData.setPage_items(pageItemCount);
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        fetchFarmSendData.setComp_id(comp_id);
        fetchFarmSendData.setCluster_id(cluster_id);
        fetchFarmSendData.setOrder(order);
        fetchFarmSendData.setSort_by(sort_by);
        fetchFarmSendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        fetchFarmSendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        Log.e(TAG, "object next page " + new Gson().toJson(fetchFarmSendData));
        fetchFarmDataCall = apiService.getVettingRejected(fetchFarmSendData);
        fetchFarmDataCall.enqueue(new Callback<VettingFarmResponse>() {
            @Override
            public void onResponse(Call<VettingFarmResponse> call, Response<VettingFarmResponse> response) {
                List<FetchFarmResult> fetchFarmResultList;
                try {
                    if (response.isSuccessful()) {
                        if (response != null) {
                            VettingFarmResponse fetchFarmData = response.body();
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                            }  else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                            }else if (fetchFarmData.getData() != null) {
                                no_data_available.setVisibility(View.INVISIBLE);
                                farm_recycler_view.setVisibility(View.VISIBLE);
                                if (fetchFarmData.getStatus() != 0) {

//                                     context.setActionBarTitle(String.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.CLUSTER_NAME)));
                                    progressBar.setVisibility(View.INVISIBLE);
                                    farmDetailAdapter.removeLoadingFooter();
                                    isLoading = false;

                                    farmDetailAdapter.addAll(fetchFarmData.getData());

                                    if (currentPage <= TOTAL_PAGES) {
                                        farmDetailAdapter.addLoadingFooter();
                                    } else {
                                        isLastPage = true;
                                    }

                                    farm_recycler_view.setHasFixedSize(true);
                                    farm_recycler_view.setNestedScrollingEnabled(true);
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    //farmDetailAdapter = new FarmDetailAdapter(context, fetchFarmResultList);
                                    farm_recycler_view.setHasFixedSize(true);
                                    farm_recycler_view.setAdapter(farmDetailAdapter);
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                                no_data_available.setVisibility(View.VISIBLE);
                                farm_recycler_view.setVisibility(View.GONE);
                            }
                        }
                    } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                    } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                    }else {
                        try {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(context, resources.getString(R.string.failed_load_farm));
                            progressBar.setVisibility(View.INVISIBLE);
                            //Toast.makeText(context, "Oops something went wrong. Please Reload", Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.errorBody().string().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<VettingFarmResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                if (t instanceof SocketTimeoutException) {
                    loadNextPage();
                }
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                //Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                Log.e("Farms Exception", t.toString());
            }
        });
    }




    @Override
    public void OnFarmSelected(FetchFarmResult fetchFarmResult) {
        Toast.makeText(context, getString(R.string.farm_fragment_toast_selected) + fetchFarmResult.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        //ReloadData();
       loadFirstPage();
    }

    @Override
    public void onFarmLoaded(List<Farm> farmList) {
        if (farmList!=null&&farmList.size()>0) {
            linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            farm_recycler_view.setLayoutManager(linearLayoutManager);
            farm_recycler_view.setItemAnimator(new DefaultItemAnimator());
            FarmDetailAdapterOffline adapterOffline = new FarmDetailAdapterOffline(farmList, context);
            farm_recycler_view.setAdapter(adapterOffline);
            progressBar.setVisibility(View.INVISIBLE);
            farm_recycler_view.setNestedScrollingEnabled(true);

        }else {
            Log.e(TAG, "Offline farm No ");
            progressBar.setVisibility(View.GONE);
            no_data_available.setVisibility(View.VISIBLE);
            farm_recycler_view.setVisibility(View.GONE);
            no_internet_layout.setVisibility(View.GONE);
        }

    }
    @Override
    public void onFarmAdded() {

    }

    @Override
    public void onFarmInsertError(Throwable e) {
        Log.e(TAG,"Offline farm err "+e.toString());
    }

    @Override
    public void onNoFarmsAvailable() {

    }

    @Override
    public void farmDeleted() {

    }

    @Override
    public void farmDeletionFaild() {
        Log.e(TAG,"Offline farm err ");
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    @Override
    public void onResume() {
        super.onResume();
        offline_mode = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE);
        // Toast.makeText(context, "On Resume", Toast.LENGTH_SHORT).show();
    }


    private void loadFarmerFarms() {
        progressBar.setVisibility(View.VISIBLE);
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String personId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("owner_id",personId);
        jsonObject.addProperty("owner_id",userId);
        jsonObject.addProperty("token",token);



        Log.e(TAG, "Farmer data Id " + new Gson().toJson(jsonObject));
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();
        apiInterface.getFarmerFarms(jsonObject).enqueue(new Callback<FetchFarmerFarmResponse>() {
            @Override
            public void onResponse(Call<FetchFarmerFarmResponse> call, Response<FetchFarmerFarmResponse> response) {
                String error=null;
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.e(TAG, "Farmer Farms Resp " + new Gson().toJson(response.body()));
                    Log.e(TAG, "Owner Id " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == 1) {
                        if (response.body().getFarm() != null) {
                            FarmerFarmAdapter adapter = new FarmerFarmAdapter(context, response.body().getFarm());
                            farm_recycler_view.setAdapter(adapter);
                            farm_recycler_view.setHasFixedSize(true);
                            linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                            farm_recycler_view.setLayoutManager(linearLayoutManager);
                            farm_recycler_view.setItemAnimator(new DefaultItemAnimator());


                        } else {
                            //No Active Farms Available
                            farm_recycler_view.setVisibility(View.GONE);
                            no_data_available.setVisibility(View.VISIBLE);
                        }

                    } else {
                        farm_recycler_view.setVisibility(View.GONE);
                        no_data_available.setVisibility(View.VISIBLE);
                        Toasty.error(context, response.body().getMsg(), Toast.LENGTH_LONG, false).show();
                        /*ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_load_farm));*/
                    }
                } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                }else {
                    try {
                        error=response.errorBody().string().toString();
                        Log.e(TAG, "Farmer Farm Err " + error);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_load_farm));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
                    notifyApiDelay(context, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<FetchFarmerFarmResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(context, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                progressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, "Farmer Farm Err " + t.toString());
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialog(context, resources.getString(R.string.failed_load_farm));
            }
        });
        internetFlow(isLoaded[0]);

    }
    private void initViews() {
        farm_recycler_view = (RecyclerView) findViewById(R.id.farm_data_recycler_view);
//        fabChildAddExistingFarm = findViewById(R.id.fabChildAddExistingFarm);
//        fabChildAddFarm = findViewById(R.id.fabChildAddFarm);
        /*et_search = (EditText) findViewById(R.id.search_query_et);
        search_img = (ImageView) findViewById(R.id.search_img);*/
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        progressImage = findViewById(R.id.progressBar_image);
//        progressImage.setImageResource(R.drawable.croptrails_loader2);
        no_internet_layout = (RelativeLayout) findViewById(R.id.no_internet_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefreshmainpage);
        //fab_sort = findViewById(R.id.//fab_sort);
//        fabParentAddFarm = findViewById(R.id.fabParentAddFarm);
//        fabParentAddFarm.setVisibility(View.GONE);
        bottomSheetDialog = new BottomSheetDialog(context);
//        app_tour_floating_btn=findViewById(R.id.app_tour_floating_btn);
        bottomSheetDialog.setContentView(R.layout.dialog_sort_farms);
        Window window = bottomSheetDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        et_sort_standing_area = bottomSheetDialog.findViewById(R.id.et_sort_standing_area);
        et_sort_standing_area_l_to_h = bottomSheetDialog.findViewById(R.id.et_sort_standing_area_l_to_h);
        et_sort_expected_area = bottomSheetDialog.findViewById(R.id.et_sort_expected_area);
        et_sort_actual_area = bottomSheetDialog.findViewById(R.id.et_sort_actual_area);
        et_sort_available_area_l_to_h = bottomSheetDialog.findViewById(R.id.et_sort_available_area_lto_h);
        et_sort_available_area_h_to_l = bottomSheetDialog.findViewById(R.id.et_sort_available_area_h_to_l);
        et_sort_harvest_date = bottomSheetDialog.findViewById(R.id.et_sort_harvest_date);
        no_data_available = findViewById(R.id.no_data_available);

        /*fabChildAddExistingFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabParentAddFarm.isOpened()) {
                    fabParentAddFarm.close(true);
                }
                Intent intent = new Intent(context, AddFarm2Activity.class);
                DataHandler.newInstance().setLatLngsCheckArea(null);
                intent.putExtra(AppConstants.ADD_FARM.KEY, AppConstants.ADD_FARM.ADD_EXISTING_FARM);
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

        fabChildAddFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabParentAddFarm.isOpened()) {
                    fabParentAddFarm.close(true);
                }
                Intent intent = new Intent(context, AddFarm2Activity.class);
                DataHandler.newInstance().setLatLngsCheckArea(null);
                intent.putExtra(AppConstants.ADD_FARM.KEY, AppConstants.ADD_FARM.NEW_FARM_AND_FARMER);
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
        fabParentAddFarm.hideMenu(true);
        fabParentAddFarm.hideMenuButton(true);

        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).trim().equals(AppConstants.FARMER)) {
            fabChildAddExistingFarm.hide(true);
            fabChildAddExistingFarm.setVisibility(View.GONE);
        }*/

    }

    View connectivityLine;
    VettingFarmAdapter farmDetailAdapter;
    LinearLayoutManager linearLayoutManager;
    private static final int PAGE_START = 0;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    Call<FetchFarmData> call;

    private static int TOTAL_PAGES = 2;
    private int currentPage = PAGE_START;
    private static String order = null;
    private static String sort_by = null;
    private boolean isScrolling = true;


    RecyclerView farm_recycler_view;
    ProgressBar progressBar;
    GifImageView progressImage;
    String comp_id, user_id, cluster_id;
    RelativeLayout no_internet_layout;

    ConnectivityManager connectivityManager;
    boolean connected = false;
    SwipeRefreshLayout swipeRefreshLayout;
    Boolean offline_mode;
    Call<VettingFarmResponse> fetchFarmDataCall;
    List<VettingFarm> fetchFarmResultList;
    public SearchView searchView;
    BottomSheetDialog bottomSheetDialog;
    // ImageView fab_sort;
//    public FloatingActionMenu fabParentAddFarm;
//    public FloatingActionMenu app_tour_floating_btn;
    TextView et_sort_standing_area;
    TextView et_sort_standing_area_l_to_h;
    TextView et_sort_expected_area;
    TextView et_sort_actual_area;
    TextView et_sort_available_area_l_to_h;
    TextView et_sort_available_area_h_to_l;
    TextView et_sort_harvest_date;
    NewFarmSearchAdapter newFarmSearchAdapter;
    RelativeLayout no_data_available;

    String pageItemCount = "500000";
    String loginType;

}