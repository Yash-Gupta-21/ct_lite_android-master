package com.i9930.croptrails.Landing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.IStatusListener;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterCluster;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonAdapters.SimpleStringAdapter;
import com.i9930.croptrails.CommonAdapters.SpinnerAdapterVillage;
import com.i9930.croptrails.CommonAdapters.SpinnerAdapterchak;
import com.i9930.croptrails.CommonClasses.Cluster;
import com.i9930.croptrails.CommonClasses.ClusterResponse;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.Landing.Models.Filter.ClusterVillageResponse;
import com.i9930.croptrails.Landing.Models.Filter.ClusterVillages;
import com.i9930.croptrails.Landing.Models.Filter.VillageChak;
import com.i9930.croptrails.Landing.Models.Filter.VillageChakResponse;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownCacheManager;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

public class FilterActivity extends AppCompatActivity {
    Toolbar mActionBarToolbar;
    Resources resources;
    FilterActivity context;
    String TAG = "FilterActivity";
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.goButton)
    Button goButton;
    List<Cluster> clusterList = new ArrayList<>();
    @BindView(R.id.totalFarmTv)
    TextView totalFarmTv;
    @BindView(R.id.totalGeoFenceAreaTv)
    TextView totalGeoFenceAreaTv;
    @BindView(R.id.getfencedFarmCountTv)
    TextView getfencedFarmCountTv;
    @BindView(R.id.nonGetfencedFarmCountTv)
    TextView nonGetfencedFarmCountTv;
    @BindView(R.id.mailContentLayout)
    LinearLayout mailContentLayout;
    @BindView(R.id.cluster_spinner)
    SearchableSpinner cluster_spinner;
    @BindView(R.id.projectTv)
    TextView projectTv;

    @BindView(R.id.villageSpinner)
    SearchableSpinner villageSpinner;
    @BindView(R.id.chakSpinner)
    SearchableSpinner chakSpinner;
    @BindView(R.id.vettingSpinner)
    SearchableSpinner vettingSpinner;
    @BindView(R.id.filterSpinner)
    SearchableSpinner filterSpinner;
    SpinnerAdapterCluster clusterAdapter;
    SpinnerAdapterVillage villageSpinnerAdapter;
    SpinnerAdapterchak chackAdapter;
    SimpleStringAdapter vettingSpinnerAdapter;
    SimpleStringAdapter filterSpinnerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        resources = getResources();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        context = this;
        ButterKnife.bind(this);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.search_and_filters));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterActivity.this.onBackPressed();
            }
        });

        List<String> vettingList = Arrays.asList(resources.getStringArray(R.array.vetting_array));
        List<String> filters = Arrays.asList(resources.getStringArray(R.array.filter_array));
        boolean isDelinquencyEnabled = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_DELINQUENT_COMP);
        try {
            if (!isDelinquencyEnabled)
                vettingList.remove(resources.getString(R.string.delinquent));
        } catch (Exception e) {

        }

        ArrayList<String> vettingList2 = new ArrayList<>();
        vettingList2.addAll(vettingList);

        ArrayList<String> filters2 = new ArrayList<>();
        filters2.addAll(filters);
        vettingSpinnerAdapter = new SimpleStringAdapter(context, vettingList2, "Select farm status", true);
        filterSpinnerAdapter = new SimpleStringAdapter(context, filters2, "Select filter");

        vettingSpinner.setAdapter(vettingSpinnerAdapter);
        filterSpinner.setAdapter(filterSpinnerAdapter);
        vettingSpinner.setSelectedItem(1);
        filterSpinner.setSelectedItem(1);

        vettingSpinner.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {
                filterSpinner.hideEdit();
                villageSpinner.hideEdit();
                chakSpinner.hideEdit();
                cluster_spinner.hideEdit();
            }

            @Override
            public void spinnerIsClosing() {

            }
        });

        cluster_spinner.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {
                filterSpinner.hideEdit();
                villageSpinner.hideEdit();
                chakSpinner.hideEdit();
                vettingSpinner.hideEdit();
            }

            @Override
            public void spinnerIsClosing() {

            }
        });

        villageSpinner.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {
                filterSpinner.hideEdit();
                vettingSpinner.hideEdit();
                chakSpinner.hideEdit();
                cluster_spinner.hideEdit();
            }

            @Override
            public void spinnerIsClosing() {

            }
        });

        chakSpinner.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {
                filterSpinner.hideEdit();
                villageSpinner.hideEdit();
                vettingSpinner.hideEdit();
                cluster_spinner.hideEdit();
            }

            @Override
            public void spinnerIsClosing() {

            }
        });

        filterSpinner.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {
                vettingSpinner.hideEdit();
                villageSpinner.hideEdit();
                chakSpinner.hideEdit();
                cluster_spinner.hideEdit();
            }

            @Override
            public void spinnerIsClosing() {

            }
        });
        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR) ||
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.CLUSTER_ADMIN)) {
//            title = String.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.CLUSTER_NAME));
            cluster_spinner.setVisibility(View.GONE);
            projectTv.setVisibility(View.GONE);

            getVillageOfCluster(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
//                getVillageOfCluster(null);
            getChaksOfVillage(null, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
        } else {
//            title = String.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMPANY_NAME));
            setClusterDDData();
            getVillageOfCluster(null);
            getChaksOfVillage(null, null);
        }

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
                JsonObject filterObject = new JsonObject();
                filterObject.addProperty("comp_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR) ||
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.CLUSTER_ADMIN)) {
                    filterObject.addProperty("cluster_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
                } else {
                    try {
                        if (clusterAdapter != null && clusterAdapter.getItem(cluster_spinner.getSelectedPosition()) != null &&
                                clusterAdapter.getItem(cluster_spinner.getSelectedPosition()).getClusterId() != null) {
                            if (clusterAdapter.getItem(cluster_spinner.getSelectedPosition()).getClusterName() != null &&
                                    !clusterAdapter.getItem(cluster_spinner.getSelectedPosition()).getClusterName().equalsIgnoreCase("all"))
                                filterObject.addProperty("cluster_id", clusterAdapter.getItem(cluster_spinner.getSelectedPosition()).getClusterId());
//                                else
//                                    filterObject.addProperty("cluster_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
                        } else {
                            filterObject.addProperty("cluster_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        filterObject.addProperty("cluster_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
                    }
                }
                try {
                    if (villageSpinnerAdapter != null && villageSpinnerAdapter.getItem(villageSpinner.getSelectedPosition()) != null &&
                            villageSpinnerAdapter.getItem(villageSpinner.getSelectedPosition()).getName() != null &&
                            !villageSpinnerAdapter.getItem(villageSpinner.getSelectedPosition()).getName().equalsIgnoreCase("all")) {
                        filterObject.addProperty("village", villageSpinnerAdapter.getItem(villageSpinner.getSelectedPosition()).getName());
                    } else {
//                            filterObject.addProperty("village", "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                        filterObject.addProperty("village", "");
                }
                try {
                    if (chackAdapter != null && chackAdapter.getItem(chakSpinner.getSelectedPosition()) != null &&
                            chackAdapter.getItem(chakSpinner.getSelectedPosition()).getAddl2() != null &&
                            !chackAdapter.getItem(chakSpinner.getSelectedPosition()).getAddl2().equalsIgnoreCase("all")) {
                        filterObject.addProperty("addL2", chackAdapter.getItem(chakSpinner.getSelectedPosition()).getAddl2());
                    } else {
//                            filterObject.addProperty("addL2  ", "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                        filterObject.addProperty("addL2  ", "");
                }
                try {
                    if (vettingSpinnerAdapter != null && vettingSpinnerAdapter.getItem(vettingSpinner.getSelectedPosition()) != null &&
                            vettingSpinnerAdapter.getItem(vettingSpinner.getSelectedPosition()) != null) {
                        String vt = vettingSpinnerAdapter.getItem(vettingSpinner.getSelectedPosition());
                        int index = vettingList.indexOf(vt);
                        String isSelected = null;

                        if (vt != null) {

                            if (vt.equals(resources.getString(R.string.all))) {
                                isSelected = "-1";
                            } else if (vt.equals(resources.getString(R.string.delinquent))) {
                                isSelected = "" + AppConstants.VETTING.DELINQUENT;
                            } else if (vt.equals(resources.getString(R.string.selected_farms))) {
                                isSelected = "" + AppConstants.VETTING.SELECTED;
                            } else if (vt.equals(resources.getString(R.string.fresh_farms))) {
                                isSelected = "" + AppConstants.VETTING.FRESH;
                            } else if (vt.equals(resources.getString(R.string.data_entry_farms))) {
                                isSelected = "" + AppConstants.VETTING.DATA_ENTRY;
                            }/*else if (index==0){

                            }*/
                            if (isSelected != null)
                                filterObject.addProperty("isSelected", isSelected);
                        }
                    } else {
//                            filterObject.addProperty("isSelected",null);
                    }
                    String isSelected = null;
                    if (filterSpinnerAdapter != null && filterSpinnerAdapter.getItem(filterSpinner.getSelectedPosition()) != null &&
                            filterSpinnerAdapter.getItem(filterSpinner.getSelectedPosition()) != null) {
                        String vt = filterSpinnerAdapter.getItem(filterSpinner.getSelectedPosition());
                        int index = filters.indexOf(vt);
                        if (vt != null) {
                            if (vt.equals(resources.getString(R.string.all))) {
                                isSelected = null;
                            } else if (vt.equals(resources.getString(R.string.farmer))) {
                                isSelected = "farmer";
                            } else if (vt.equals(resources.getString(R.string.mobile))) {
                                isSelected = "mob";
                            } /*else if (index == 3) {
                                isSelected = "address_addL1";
                            }*/ else if (vt.equals(resources.getString(R.string.lot_no))) {
                                isSelected = "comp_farm_id";
                            }
                            if (isSelected != null)
                                filterObject.addProperty("filter", isSelected);
                        }
                    } else {
//                            filterObject.addProperty("isSelected",null);
                    }
                    filterObject.addProperty("query", etSearch.getText().toString().trim());
                    //order-> vetting->
                    Log.e(TAG, "DATA SEND " + new Gson().toJson(filterObject));
                    filterObject.addProperty("user_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                    filterObject.addProperty("token", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));

                    SharedPreferencesMethod.setString(context,SharedPreferencesMethod.FILTER_JSON,new Gson().toJson(filterObject));
                    if (AppConstants.isValidString(isSelected) && TextUtils.isEmpty(etSearch.getText().toString())) {
                        AppConstants.failToast(context, "Please enter a query to search");
                    } else {
                        DataHandler.newInstance().setFilterData(filterObject);
                        Intent intent = new Intent(context, LandingActivity3.class);
                        intent.putExtra("name", "Search Results");
                        startActivity(intent);
//                            geFilterData(filterObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                        filterObject.addProperty("isSelected","");
                }


            }
        });


    }


    private void setClusterDDData() {
        projectTv.setVisibility(View.VISIBLE);
        cluster_spinner.setVisibility(View.VISIBLE);
        DropDownCacheManager.getInstance(context).getDataByType(new DropDownCacheManager.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT dropDown) {
                Log.e(TAG, "Offline clusters " + new Gson().toJson(dropDown));

                if (dropDown != null && AppConstants.isValidString(dropDown.getData()) && !dropDown.getData().trim().equals("[]")) {
                    try {
                        JSONArray array = new JSONArray(dropDown.getData());

                        List<Cluster> clusterList = new ArrayList<>();
                        Cluster clusterr = new Cluster();
                        clusterr.setClusterName("All");
                        clusterr.setClusterId("0");
                        clusterList.add(clusterr);
                        for (int j = 0; j < array.length(); j++) {

                            JSONObject object = array.getJSONObject(j);
                            Cluster cluster = new Gson().fromJson(object.toString(), Cluster.class);
                            clusterList.add(cluster);
                        }
                        setClusterData(clusterList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    getClusterOfCompany();
                }
            }
        }, AppConstants.CHEMICAL_UNIT.CLUSTER_LIST);
    }

    private void setClusterData(List<Cluster> list) {
        ArrayList<Cluster> clusterList = new ArrayList<>();
        clusterList.addAll(list);
        clusterAdapter = new SpinnerAdapterCluster(context, clusterList);
        cluster_spinner.setAdapter(clusterAdapter);
        cluster_spinner.setSelectedItem(1);
        cluster_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                if (i > 0) {
                    String clusterId = clusterAdapter.getItem(i).getClusterId();
                    getVillageOfCluster(clusterId);
                }
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }

    private void setVillageData(List<ClusterVillages> list, String clusterId) {
        ArrayList<ClusterVillages> villages = new ArrayList<>();
        ClusterVillages clusterVillages = new ClusterVillages();
        clusterVillages.setName("All");
        villages.add(clusterVillages);

        villages.addAll(list);
        villageSpinnerAdapter = new SpinnerAdapterVillage(context, villages);
        villageSpinner.setAdapter(villageSpinnerAdapter);
        villageSpinner.setSelectedItem(1);
        villageSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                if (i > 0) {
                    String village = villageSpinnerAdapter.getItem(i).getName();
                    getChaksOfVillage(village, clusterId);
                }
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }

    private void setChakData(List<VillageChak> list) {
        ArrayList<VillageChak> villages = new ArrayList<>();
        VillageChak villageChak = new VillageChak();
        villageChak.setAddl2("All");
        villages.add(villageChak);
        villages.addAll(list);
        chackAdapter = new SpinnerAdapterchak(context, villages);

        chakSpinner.setAdapter(chackAdapter);
        chakSpinner.setSelectedItem(1);
        chakSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                if (i > 0) {

                }
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }

    private void getVillageOfCluster(String clusterId) {
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        JsonObject jsonObject = new JsonObject();
        if (clusterId != null && !clusterId.equals("0"))
            jsonObject.addProperty("cluster_id", "" + clusterId);
        else getChaksOfVillage(null, null);
        jsonObject.addProperty("comp_id", "" + compId);
        jsonObject.addProperty("token", "" + token);
        jsonObject.addProperty("user_id", "" + userId);
        Log.e(TAG, "getVillageOfCluster PARAM " + new Gson().toJson(jsonObject));
        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getClusterVillages(jsonObject).enqueue(new Callback<ClusterVillageResponse>() {
            @Override
            public void onResponse(Call<ClusterVillageResponse> call, Response<ClusterVillageResponse> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    ClusterVillageResponse villageResponse = response1.body();
                    /*try {
                        Log.e(TAG, "getVillageOfCluster Response " + response1.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
//                    Log.e(TAG, "getGeoCount Response " + new Gson().toJson(countResponse));
                    if (villageResponse.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(FilterActivity.this);
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(FilterActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(FilterActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        if (villageResponse.getData() != null)
                            setVillageData(villageResponse.getData(), clusterId);

                    }
                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(FilterActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(FilterActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {

                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "getVillageOfCluster Error " + error + " Code " + response1.code() + " ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ClusterVillageResponse> call, Throwable t) {
                Log.e(TAG, "getVillageOfCluster Failure  " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
            }
        });

    }

    private void getChaksOfVillage(String village, String clusterId) {
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        JsonObject jsonObject = new JsonObject();
        if (village != null && !village.equalsIgnoreCase("all"))
            jsonObject.addProperty("village", "" + village);
        if (clusterId != null && !clusterId.equals("0"))
            jsonObject.addProperty("cluster_id", "" + clusterId);
        jsonObject.addProperty("comp_id", "" + compId);
        jsonObject.addProperty("token", "" + token);
        jsonObject.addProperty("user_id", "" + userId);
        Log.e(TAG, "getChaksOfVillage PARAM " + new Gson().toJson(jsonObject));
        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getChaksOfVillage(jsonObject).enqueue(new Callback<VillageChakResponse>() {
            @Override
            public void onResponse(Call<VillageChakResponse> call, Response<VillageChakResponse> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    VillageChakResponse villageResponse = response1.body();
                    Log.e(TAG, "getChaksOfVillage " + new Gson().toJson(villageResponse));
                   /* try {
                        Log.e(TAG, "getChaksOfVillage Response " + response1.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
//                    Log.e(TAG, "getGeoCount Response " + new Gson().toJson(countResponse));
                    if (villageResponse.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(FilterActivity.this, villageResponse.getMessage());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(FilterActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(FilterActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        if (villageResponse.getData() != null) {
                            setChakData(villageResponse.getData());
                        }

                    }
                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(FilterActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(FilterActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {

                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "getChaksOfVillage Error " + error + " Code " + response1.code() + " ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<VillageChakResponse> call, Throwable t) {
                Log.e(TAG, "getChaksOfVillage Failure  " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
            }
        });

    }

    private void getClusterOfCompany() {
        clusterList.clear();
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
                        Cluster clusterr = new Cluster();
                        clusterr.setClusterName("All");
                        clusterr.setClusterId("0");
                        clusterList.add(clusterr);
                        if (response.body().getClusterList() != null) {
                            clusterList.addAll(response.body().getClusterList());
                        }
                        DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST);
                        DropDownT clusters = new DropDownT(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST, new Gson().toJson(clusterList));
                        DropDownCacheManager.getInstance(context).addChemicalUnit(clusters);
                        setClusterData(clusterList);
                    } else if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(FilterActivity.this);
                        //progressDialog.cancel();
                    } else {
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(FilterActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(FilterActivity.this, resources.getString(R.string.authorization_expired));
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
                Log.e(TAG, "Cluster fail " + t.toString());
            }
        });


    }
}