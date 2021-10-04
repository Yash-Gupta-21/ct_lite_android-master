package com.i9930.croptrails.Vetting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.Landing.Adapter.VettingFarmAdapterNew;
import com.i9930.croptrails.Landing.Models.FetchFarmSendData;
import com.i9930.croptrails.Landing.Models.NewData.FetchFarmResultNew;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Test.FarmDetailsVettingActivity;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;
import com.i9930.croptrails.Vetting.Adapter.PersonRecyclerAdapter;
import com.i9930.croptrails.Vetting.Model.OtherPersonDatum;
import com.i9930.croptrails.Vetting.Model.OtherVettingFarmResponse;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class VettingOthersFarmActivity extends AppCompatActivity {

    VettingOthersFarmActivity context;
    String TAG = "VettingOthersFarmActivity";
    Toolbar mActionBarToolbar;
    Resources resources;
    String comp_id, user_id, cluster_id, person_id, token, auth;
    String selectedPreviousVetting = "";
    FetchFarmResultNew selectedPreviousFarm;
    int selectedPreviousIndex = 0;
    VettingFarmAdapterNew farmDetailsAdapterVetting;
    List<FetchFarmResultNew> fetchFarmResultVetting = new ArrayList<>();
    List<OtherPersonDatum> fetchFarmResultVettingPerson = new ArrayList<>();
    List<FetchFarmResultNew> fetchFarmResultVettingFilter = new ArrayList<>();
    View connectivityLine;
    GifImageView progressBar;
    RelativeLayout no_data_available;
    RecyclerView recyclerView;
    String internetSPeed = "";
    PersonRecyclerAdapter personRecyclerAdapter;
    BottomSheetDialog bottomSheetDialog;

    private void initializeView() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.loader);
        no_data_available = findViewById(R.id.no_data_available);
        connectivityLine = findViewById(R.id.connectivityLine);
    }

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
        setContentView(R.layout.activity_vetting_others_farm);
        context = this;
        initializeView();
//        initView();
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VettingOthersFarmActivity.this.onBackPressed();
            }
        });
        mActionBarToolbar.setTitle(resources.getString(R.string.vetting_label));
        comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        user_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        cluster_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID);
        person_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        initializeBottomsheet();

//        farmDetailsAdapterVetting = new VettingFarmAdapterNew(context, fetchFarmResultVettingFilter, new VettingFarmAdapterNew.ItemClickListener() {
//            @Override
//            public void onItemClicked(int index, FetchFarmResultNew farmResultNew, String vetting) {
//                if (AppConstants.isValidString(vetting)) {
//                    int code = 99;
//                    selectedPreviousFarm = farmResultNew;
//                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARM_VETTING, vetting);
//                    selectedPreviousIndex = index;
//                    selectedPreviousVetting = vetting;
//                    Intent intent = null;
//                    if (vetting != null && vetting.trim().equals(AppConstants.VETTING.SELECTED)) {
//                        /*if (isOnlySoilSens) {
//                            intent = new Intent(context, SoilSensDashboardActivity.class);
//                        } else*/
//                        intent = new Intent(context, FarmDetailsVettingActivity.class);
//                        code = 99;
//                    } /*else if (isOnlySoilSens) {
//                        intent = new Intent(context, SoilSensDashboardActivity.class);
//                        code = 99;
//                    } */ else {
//                        intent = new Intent(context, FarmDetailsVettingActivity.class);
//                        code = 9930;
//                    }
//                    intent.putExtra("fromOther",true);
//                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTUAL_AREA, farmResultNew.getActualArea() + "");
//                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, farmResultNew.getStandingArea() + "");
//                    SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.EXPECTED_AREA,
//                            Double.valueOf(farmResultNew.getExpArea()));
//                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, farmResultNew.getFarmMasterNum());
////                                                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, /*farmResultNew.getFarmMasterNum()*/"2349");
////                                                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_ID, /*farmResultNew.getFarmMasterNum()*/"100131");
//                    startActivityForResult(intent, code);
//                }
//            }
//        });
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(farmDetailsAdapterVetting);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setNestedScrollingEnabled(true);
//        getFarms();

    }

    private void getFarms() {
        fetchFarmResultVetting.clear();
        fetchFarmResultVettingFilter.clear();
        FetchFarmSendData fetchFarmSendData = new FetchFarmSendData();
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        fetchFarmSendData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        fetchFarmSendData.setCluster_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
        fetchFarmSendData.setOrder(null);
        fetchFarmSendData.setSort_by(null);
        fetchFarmSendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        fetchFarmSendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        fetchFarmSendData.setPage_items("1000000");
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        Log.e(TAG, "Get Farms " + new Gson().toJson(fetchFarmSendData));
        apiService.getOtherVetting(fetchFarmSendData).enqueue(new Callback<OtherVettingFarmResponse>() {
            @Override
            public void onResponse(Call<OtherVettingFarmResponse> call, Response<OtherVettingFarmResponse> response) {
                String error = null;
                Log.e(TAG, "Get Farm code" + response.code());
                progressBar.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful()) {
                        Log.e(TAG, "Get Farm Res " + new Gson().toJson(response.body()));
                        if (response != null) {
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(VettingOthersFarmActivity.this);
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(VettingOthersFarmActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(VettingOthersFarmActivity.this, resources.getString(R.string.authorization_expired));
                            } else if (response.body().getStatus() == 0) {

                                progressBar.setVisibility(View.GONE);
                                no_data_available.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                Log.e(TAG, "Get Farm No Data");
                            } else {
                                OtherVettingFarmResponse fetchFarmData = response.body();
                                if (fetchFarmData.getData() != null && fetchFarmData.getData().size() > 0) {
                                    no_data_available.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    fetchFarmResultVetting.clear();
                                    fetchFarmResultVettingFilter.clear();
                                    fetchFarmResultVettingPerson.clear();
                                    fetchFarmResultVettingPerson = fetchFarmData.getData();
                                    if (personRecyclerAdapter != null)
                                        personRecyclerAdapter.notifyDataSetChanged();
                                    for (int i = 0; i < fetchFarmResultVettingPerson.size(); i++) {
                                        fetchFarmResultVettingPerson.get(i).setChecked(true);
                                        OtherPersonDatum datum = fetchFarmResultVettingPerson.get(i);
                                        if (datum != null && datum.getFarms() != null && datum.getFarms().size() > 0) {
                                            fetchFarmResultVetting.addAll(datum.getFarms());
                                        }
                                    }
                                    fetchFarmResultVettingFilter.addAll(fetchFarmResultVetting);
                                    if (farmDetailsAdapterVetting != null)
                                        farmDetailsAdapterVetting.notifyDataSetChanged();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    if (fetchFarmResultVettingFilter == null || fetchFarmResultVettingFilter.size() == 0) {
                                        no_data_available.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    no_data_available.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            no_data_available.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
//                                no_internet_layout.setVisibility(View.GONE);
                            Log.e(TAG, "Get Farm No Data 2");
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(VettingOthersFarmActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(VettingOthersFarmActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(VettingOthersFarmActivity.this, resources.getString(R.string.failed_load_farm));
                            progressBar.setVisibility(View.INVISIBLE);
                            //Toast.makeText(context, "Oops something went wrong. Please Reload", Toast.LENGTH_SHORT).show();
                            error = response.errorBody().string().toString();
                            Log.e(TAG, "Get Farm Error " + error);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    try {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_load_farm));
                        progressBar.setVisibility(View.INVISIBLE);
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "Get Farm Error2" + error);
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
                    notifyApiDelay(VettingOthersFarmActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }

            }

            @Override
            public void onFailure(Call<OtherVettingFarmResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(VettingOthersFarmActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showDialog(VettingOthersFarmActivity.this, resources.getString(R.string.failed_load_farm));
                    progressBar.setVisibility(View.INVISIBLE);
                    //Toast.makeText(context, "Oops something went wrong. Please Reload", Toast.LENGTH_SHORT).show();
                } catch (Exception f) {
                    f.printStackTrace();
                }                //Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Get Farm Farms Exception" + t.toString());
            }
        });
        internetFlow(isLoaded[0]);
    }

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
                            ConnectivityUtils.checkSpeedWithColor(VettingOthersFarmActivity.this, new ConnectivityUtils.ColorListener() {
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
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!SharedPreferencesMethod.getBoolean(this, SharedPreferencesMethod.OFFLINE_MODE)) {
            getMenuInflater().inflate(R.menu.search_singl, menu);
            MenuItem searchMenuItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) searchMenuItem.getActionView();
            searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    try {
                        if (fetchFarmResultVettingFilter != null)
                            fetchFarmResultVettingFilter.clear();
                        fetchFarmResultVettingFilter.addAll(fetchFarmResultVetting);
                        farmDetailsAdapterVetting.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String query) {
                    if (query != null && query.length() > 1) {
                        if (searchAsynch == null) {
                            searchAsynch = new SearchAsynch(query.trim());
                            searchAsynch.execute();
                        } else {
                            searchAsynch.cancel(true);
                            searchAsynch = new SearchAsynch(query.trim());
                            searchAsynch.execute();
                        }
                    } else {
                        try {

                            fetchFarmResultVettingFilter.clear();
                            fetchFarmResultVettingFilter.addAll(fetchFarmResultVetting);
                            farmDetailsAdapterVetting.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                }

                public boolean onQueryTextSubmit(String query) {
                    if (query != null && query.length() > 1) {
                        if (searchAsynch == null) {
                            searchAsynch = new SearchAsynch(query.trim());
                            searchAsynch.execute();
                        } else {
                            searchAsynch.cancel(true);
                            searchAsynch = new SearchAsynch(query.trim());
                            searchAsynch.execute();
                        }

                    } else {
                        try {

                            fetchFarmResultVettingFilter.clear();
                            fetchFarmResultVettingFilter.addAll(fetchFarmResultVetting);
                            farmDetailsAdapterVetting.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);

            MenuItem filterItem = menu.findItem(R.id.action_filter);
            if (filterItem != null) {
                filterItem.setVisible(true);
                filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        showBottomsheetDialog();
                        return false;
                    }
                });
            }
            MenuItem action_refresh = menu.findItem(R.id.action_refresh);

            action_refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    getFarms();
                    return false;
                }
            });
        }
        return true;
    }

    SearchAsynch searchAsynch;

    private class SearchAsynch extends AsyncTask<String, Integer, String> {
        String query;

        public SearchAsynch(String query) {
            this.query = query.trim();
        }

        @Override
        protected String doInBackground(String... strings) {


            if (farmDetailsAdapterVetting != null) {
                try {
                    fetchFarmResultVettingFilter.clear();
                    for (int i = 0; i < fetchFarmResultVetting.size(); i++) {
                        try {
                            FetchFarmResultNew data = fetchFarmResultVetting.get(i);
                            if (data.getFarmName() != null && data.getFarmName().toLowerCase().contains(query.toLowerCase())) {
                                fetchFarmResultVettingFilter.add(data);
                            } else if (data.getLotNo() != null && data.getLotNo().toLowerCase().contains(query.toLowerCase())) {
                                fetchFarmResultVettingFilter.add(data);
                            } else if (data.getFarmerName() != null && data.getFarmerName().toLowerCase().contains(query.toLowerCase())) {
                                fetchFarmResultVettingFilter.add(data);
                            } else if (data.getMandalOrTehsil() != null && data.getMandalOrTehsil().toLowerCase().contains(query.toLowerCase())) {
                                fetchFarmResultVettingFilter.add(data);
                            } else if (data.getVillageOrCity() != null && data.getVillageOrCity().toLowerCase().contains(query.toLowerCase())) {
                                fetchFarmResultVettingFilter.add(data);
                            } else if (data.getAddL1() != null && data.getAddL1().toLowerCase().contains(query.toLowerCase())) {
                                fetchFarmResultVettingFilter.add(data);
                            } else if (data.getAddL2() != null && data.getAddL2().toLowerCase().contains(query.toLowerCase())) {
                                fetchFarmResultVettingFilter.add(data);
                            } else if (data.getDistrict() != null && data.getDistrict().toLowerCase().contains(query.toLowerCase())) {
                                fetchFarmResultVettingFilter.add(data);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    VettingOthersFarmActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                farmDetailsAdapterVetting.notifyDataSetChanged();
                            } catch (Exception e) {
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    RecyclerView personRecycler;
    LinearLayout approvedLayout, freshLayout, editLayout, rejectedLayout;
    Button filterApplyButton, clearFilterButton;

    private void initializeBottomsheet() {
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.dialog_bottomsheet_filter_vetting_farms);
        Window window = bottomSheetDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        personRecycler = bottomSheetDialog.findViewById(R.id.personRecycler);
        filterApplyButton = bottomSheetDialog.findViewById(R.id.filterApplyButton);
        clearFilterButton = bottomSheetDialog.findViewById(R.id.clearFilterButton);
        approvedLayout = bottomSheetDialog.findViewById(R.id.approvedLayout);
        freshLayout = bottomSheetDialog.findViewById(R.id.freshLayout);
        editLayout = bottomSheetDialog.findViewById(R.id.editLayout);
        rejectedLayout = bottomSheetDialog.findViewById(R.id.rejectedLayout);

        approvedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isApproved)
                    isApproved = false;
                else
                    isApproved = true;
                setTSelection();
            }
        });

        freshLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFresh)
                    isFresh = false;
                else
                    isFresh = true;
                setTSelection();
            }
        });

        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit)
                    isEdit = false;
                else
                    isEdit = true;
                setTSelection();
            }
        });

        rejectedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isReject)
                    isReject = false;
                else
                    isReject = true;
                setTSelection();
            }
        });

        filterApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterData(1);

            }
        });

        clearFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterData(0);
            }
        });

    }

    private void filterData(int filterType) {
        if (filterAsynch != null) {
            try {
                filterAsynch.cancel(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        fetchFarmResultVettingFilter.clear();
        filterAsynch = new FilterAsynch(filterType);
        filterAsynch.execute();
        bottomSheetDialog.dismiss();
    }

    FilterAsynch filterAsynch;

    private void showBottomsheetDialog() {
        bottomSheetDialog.show();

        personRecyclerAdapter = new PersonRecyclerAdapter(context, fetchFarmResultVettingPerson);
        personRecycler.setHasFixedSize(true);
        personRecycler.setNestedScrollingEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        personRecycler.setLayoutManager(linearLayoutManager);
        personRecycler.setAdapter(personRecyclerAdapter);
        setTSelection();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTSelection() {

        if (approvedLayout != null)
            if (isApproved) {
                approvedLayout.setBackgroundResource(R.drawable.large_edittext_background3);
            } else {
                approvedLayout.setBackgroundResource(R.drawable.selected_option_check_area_not);
            }
        if (rejectedLayout != null)
            if (isReject) {
                rejectedLayout.setBackgroundResource(R.drawable.large_edittext_background3);
            } else {
                rejectedLayout.setBackgroundResource(R.drawable.selected_option_check_area_not);
            }
        if (freshLayout != null)
            if (isFresh) {
                freshLayout.setBackgroundResource(R.drawable.large_edittext_background3);
            } else {
                freshLayout.setBackgroundResource(R.drawable.selected_option_check_area_not);
            }
        if ((editLayout != null)) {
            if (isEdit) {
                editLayout.setBackgroundResource(R.drawable.large_edittext_background3);
            } else {
                editLayout.setBackgroundResource(R.drawable.selected_option_check_area_not);
            }
        }
    }

    boolean isApproved = true, isFresh = true, isEdit = true, isReject = true;

    private class FilterAsynch extends AsyncTask<String, Integer, String> {
        int type;//0 for clear filter and 1 for apply filter

        public FilterAsynch(int filterType) {
            type = filterType;
        }

        @Override
        protected String doInBackground(String... strings) {
            if (type == 1) {


                for (int i = 0; i < fetchFarmResultVettingPerson.size(); i++) {
                    Log.e(TAG, "INDEX " + i);
                    OtherPersonDatum datum = fetchFarmResultVettingPerson.get(i);
                    if (datum != null && datum.getFarms() != null && datum.getFarms().size() > 0) {
                        List<FetchFarmResultNew> farms = datum.getFarms();
                        if (datum.isChecked()) {
                            fetchFarmResultVettingFilter.addAll(farms);
                        } else if (isApproved || isFresh || isFresh || isReject) {
                            for (int j = 0; j < farms.size(); j++) {
                                String vett = farms.get(j).getVetting();
                                if (vett != null && ((vett.equals(AppConstants.VETTING.SELECTED) && isApproved) || (vett.equals(AppConstants.VETTING.REJECTED) && isReject) ||
                                        (vett.equals(AppConstants.VETTING.DATA_ENTRY) && isEdit) || (vett.equals(AppConstants.VETTING.FRESH) && isFresh))) {

                                    fetchFarmResultVettingFilter.add(farms.get(j));
                                }
                            }
                        }
                    }
                }
            } else {
                isApproved = true; isFresh = true; isEdit = true; isReject = true;
                for (int i = 0; i < fetchFarmResultVettingPerson.size(); i++) {
                    fetchFarmResultVettingPerson.get(i).setChecked(true);
                    OtherPersonDatum datum = fetchFarmResultVettingPerson.get(i);
                    if (datum != null && datum.getFarms() != null && datum.getFarms().size() > 0) {
                        fetchFarmResultVettingFilter.addAll(datum.getFarms());
                    }
                }
            }
            VettingOthersFarmActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {

                        farmDetailsAdapterVetting.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }
    }
}
