package com.i9930.croptrails.Maps.selectFarmerFarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.AddFarm.Model.FarmerSpinnerData;
import com.i9930.croptrails.AddFarm.Model.FetchFarmerResponse;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonAdapters.ClusterSpinnerAdapter;
import com.i9930.croptrails.CommonAdapters.FarmerSpinner.FarmerSpinnerAdapter;
import com.i9930.croptrails.CommonClasses.Cluster;
import com.i9930.croptrails.CommonClasses.ClusterResponse;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.VerifySendData;
import com.i9930.croptrails.Maps.selectFarmerFarm.model.FarmerData;
import com.i9930.croptrails.Maps.selectFarmerFarm.model.SelectionDatum;
import com.i9930.croptrails.Maps.selectFarmerFarm.model.SelectionResponse;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class SelectFarmActivity extends AppCompatActivity {

    SelectFarmActivity context;
    String TAG="SelectFarmActivity";
    Toolbar mActionBarToolbar;
    Resources resources;
    FarmSelectionAdapter adapter;
    @BindView(R.id.farmer_spinner)
    Spinner farmer_spinner;

    @BindView(R.id.cluster_spinner)
    Spinner cluster_spinner;
    @BindView(R.id.progressBar_image)
    GifImageView progressBar_image;

    @BindView(R.id.recyclerSelectFarm)
    RecyclerView recyclerSelectFarm;

    ViewFailDialog viewFailDialog;
    @BindView(R.id.noDataTv)
    TextView noDataTv;
    String areaInAcre;
    List<LatLng>latLngListCheckArea=new ArrayList<>();
    String mapZoom;
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
                            ConnectivityUtils.checkSpeedWithColor(SelectFarmActivity.this, new ConnectivityUtils.ColorListener() {
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
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_farm);
        ButterKnife.bind(this);
        context=this;
        viewFailDialog=new ViewFailDialog();
        TextView title = (TextView) findViewById(R.id.tittle);
//        title.setText(resources.getString(R.string.add_farm_title));
        title.setText("Select Farm To Assign Area");
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        areaInAcre=getIntent().getStringExtra("areaInAcre");
        mapZoom=getIntent().getStringExtra("mapZoom");
        latLngListCheckArea= DataHandler.newInstance().getLatLngsCheckArea();
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUB_ADMIN)||
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPER_ADMIN)||
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.ADMIN)){
            getClusterOfCompany();
            cluster_spinner.setVisibility(View.VISIBLE);
        }else {
            cluster_spinner.setVisibility(View.GONE);
            getFarmerList(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.SVCLUSTERID));
        }
    }


    private void getClusterOfCompany() {
        progressBar_image.setVisibility(View.VISIBLE);
        String auth= SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();
        apiInterface.getClusters(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID)).enqueue(new Callback<ClusterResponse>() {
            @Override
            public void onResponse(Call<ClusterResponse> call, Response<ClusterResponse> response) {
                String error=null;
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {

                        setClusterData(response.body().getClusterList());
                        //progressDialog.cancel();
                        progressBar_image.setVisibility(View.GONE);
                    } else if (response.body().getStatus() == 10) {
                        viewFailDialog.showSessionExpireDialog(context);
                        //progressDialog.cancel();
                        progressBar_image.setVisibility(View.GONE);
                    } else {
                        //   progressDialog.cancel();
                        progressBar_image.setVisibility(View.GONE);
                        viewFailDialog.showDialogForFinish(context, resources.getString(R.string.opps_message), resources.getString(R.string.no_cluster_found_to_add_cluster));
                    }
                } else if (response.code()== AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                }else {
                    try {
                        //progressDialog.cancel();
                        progressBar_image.setVisibility(View.GONE);
                        viewFailDialog.showDialogForFinish(context, resources.getString(R.string.opps_message), resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg));
                        error=response.errorBody().string();
                        Log.e(TAG, "Cluster Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
                    notifyApiDelay(SelectFarmActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }

            }

            @Override
            public void onFailure(Call<ClusterResponse> call, Throwable t) {
                //progressDialog.cancel();
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(SelectFarmActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                progressBar_image.setVisibility(View.GONE);
                viewFailDialog.showDialogForFinish(context, resources.getString(R.string.opps_message), resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg));
                Log.e(TAG, "Cluster Failure " + t.toString());
            }
        });
        internetFlow(isLoaded[0]);

    }
    List<Cluster> clusterList = new ArrayList<>();
    private void setClusterData(List<Cluster> list){
        clusterList.clear();
        Cluster cluster = new Cluster();
        cluster.setClusterId("0");
        cluster.setClusterName(resources.getString(R.string.select_cluster_label));


        clusterList.add(cluster);
        clusterList.addAll(list);
        ClusterSpinnerAdapter adapter = new ClusterSpinnerAdapter(context, clusterList);
        cluster_spinner.setAdapter(adapter);

        cluster_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    recyclerSelectFarm.setVisibility(View.GONE);
                    farmer_spinner.setVisibility(View.GONE);
                }else {
                    recyclerSelectFarm.setVisibility(View.VISIBLE);
                    farmer_spinner.setVisibility(View.VISIBLE);
                    getFarmerList(clusterList.get(position).getClusterId());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getFarmerList(String clusterId) {
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();
        apiInterface.getFarmerList(compId, clusterId, userId, token).enqueue(new Callback<FetchFarmerResponse>() {
            @Override
            public void onResponse(Call<FetchFarmerResponse> call, Response<FetchFarmerResponse> response) {
                String error=null;
                if (response.isSuccessful()) {
                    Log.e(TAG, "Farmer List Res " + new Gson().toJson(response.body()));

                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == 1) {
                        setFarmerListData(response.body().getFarmerDataList());

                    } else {

                        farmer_spinner.setVisibility(View.VISIBLE);
                        farmer_spinner.setVisibility(View.GONE);
                        recyclerSelectFarm.setVisibility(View.GONE);
                    }
                }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error=response.errorBody().string().toString();
                        Log.e(TAG, "Farmer List Err " + error);
                        viewFailDialog.showDialogForFinish(context, resources.getString(R.string.failed_to_load_data_msg));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
                    notifyApiDelay(SelectFarmActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<FetchFarmerResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(SelectFarmActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "Farmer List failure " + t.toString());
                viewFailDialog.showDialogForFinish(context, resources.getString(R.string.failed_to_load_data_msg));
            }
        });

        internetFlow(isLoaded[0]);
    }
    private List<FarmerSpinnerData> farmerDataList = new ArrayList<>();
    private void setFarmerListData(List<FarmerSpinnerData>list){
        farmerDataList.clear();
        FarmerSpinnerData farmerData = new FarmerSpinnerData();
        farmerData.setPersonId(0);
        farmerData.setName(getString(R.string.select_farmer));
        farmerDataList.add(farmerData);
        farmerDataList.addAll(list);
        FarmerSpinnerAdapter farmerSpinnerAdapter = new FarmerSpinnerAdapter(context, farmerDataList);
        farmer_spinner.setAdapter(farmerSpinnerAdapter);
        farmer_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(context, "Position " + i, Toast.LENGTH_SHORT).show();
                if (farmerDataList.get(i).getName().toLowerCase().equals("select farmer") || i == 0) {
                    recyclerSelectFarm.setVisibility(View.GONE);
                }  else {
                    recyclerSelectFarm.setVisibility(View.VISIBLE);
                    getFarmList(farmerDataList.get(i).getPersonId()+"");


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void getFarmList(String farmerId) {
        noDataTv.setVisibility(View.GONE);
        recyclerSelectFarm.setVisibility(View.GONE);
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);

        FarmerData farmerData=new FarmerData(farmerId,userId,token,compId);
        Log.e(TAG,"Farmer Data "+new Gson().toJson(farmerData));
        apiInterface.getFarmerFarmNonGps(farmerData).enqueue(new Callback<SelectionResponse>() {
            @Override
            public void onResponse(Call<SelectionResponse> call, Response<SelectionResponse> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Farm List Res " + new Gson().toJson(response.body()));

                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == 1) {
                        if (response.body().getData()!=null&&response.body().getData().size()>0){
                            noDataTv.setVisibility(View.GONE);
                            recyclerSelectFarm.setVisibility(View.VISIBLE);
                            setRecyclerData(response.body().getData());
                        }else {
                            noDataTv.setVisibility(View.VISIBLE);
                            recyclerSelectFarm.setVisibility(View.GONE);
                        }

                    } else {

                        noDataTv.setVisibility(View.VISIBLE);
                        recyclerSelectFarm.setVisibility(View.GONE);
                    }
                }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        Log.e(TAG, "Farmer List Err " + response.errorBody().string().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SelectionResponse> call, Throwable t) {
                Log.e(TAG, "Farmer List failure " + t.toString());
            }
        });


    }


    private void setRecyclerData(List<SelectionDatum>selectionData){
        recyclerSelectFarm.setHasFixedSize(true);
        recyclerSelectFarm.setLayoutManager(new LinearLayoutManager(context));

        adapter=new FarmSelectionAdapter(context, selectionData, new FarmSelectionAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(SelectionDatum selectionDatum) {
                showDialog(selectionDatum);
            }
        });
        recyclerSelectFarm.setAdapter(adapter);

    }



    public void showDialog( SelectionDatum selectionDatum) {

        try {
            String msg="Are you sure you want to assign selected area to ";
            String sourceString =msg+" "+ "<b>" + selectionDatum.getLotNo() + "</b> " ;
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            try {
                AlertDialog  alertDialog = builder.setMessage(Html.fromHtml(sourceString))
                        .setTitle(resources.getString(R.string.alert_title_label))
                        .setCancelable(false)
                        .setPositiveButton("Assign", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressBar_image.setVisibility(View.VISIBLE);
                                if (adapter!=null)
                                    adapter.setClickable(false);

                                SharedPreferencesMethod.setString(context,SharedPreferencesMethod.SVFARMID,selectionDatum.getFarmMasterNum());
                                SubmitAreaAsync areaAsync=new SubmitAreaAsync();
                                areaAsync.execute();
                            }
                        })
                        .setNegativeButton(resources.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e){

        }
    }



    private class SubmitAreaAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

           String[] lat = new String[latLngListCheckArea.size()];
            String[] lng = new String[latLngListCheckArea.size()];

            for (int j = 0; j < latLngListCheckArea.size(); j++) {
                lat[j] = String.valueOf(latLngListCheckArea.get(j).latitude);
                lng[j] = String.valueOf(latLngListCheckArea.get(j).longitude);
            }

            //demosupervisor1
            try {
                String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                VerifySendData verifySendData = new VerifySendData();
                verifySendData.setZoom(String.valueOf(mapZoom));
                verifySendData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                verifySendData.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                verifySendData.setArea(areaInAcre);
                verifySendData.setLat(lat);
                verifySendData.setDeleted_by(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                verifySendData.setLng(lng);
                verifySendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                verifySendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                Call<StatusMsgModel> statusMsgModelCall = apiService.getMsgStatusForVerifyFarm(verifySendData);
                Log.e(TAG, "Sending Data " + new Gson().toJson(verifySendData));
                final boolean[] isLoaded = {false};
                long currMillis=AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<StatusMsgModel>() {
                    @Override
                    public void onResponse(Call<StatusMsgModel> call, retrofit2.Response<StatusMsgModel> response) {
                        String error=null;
                        if (response.isSuccessful()) {
                            Log.e("Area Response", new Gson().toJson(response.body()));
                            StatusMsgModel statusMsgModel = response.body();
                            if (statusMsgModel.getStatus() == 1) {
                                hideProgress();
                                Toasty.success(context, resources.getString(R.string.area_successfully_added), Toast.LENGTH_SHORT, true).show();
                                Intent intent = getIntent();
                                startActivity(new Intent(context, LandingActivity.class));
                                finishAffinity();
                            }  else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                                //un authorized access
                                hideProgress();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                                // auth token expired
                                hideProgress();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                            }else if (response.body().getStatus() == 10) {
                                hideProgress();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, response.body().getMsg());
                            } else {
                                hideProgress();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_verify_farm_area));
                            }
                        } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                            hideProgress();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                        } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                            hideProgress();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                        } else {
                            try {
                                error=response.errorBody().string().toString();
                                hideProgress();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_verify_farm_area));
                                Log.e(TAG, "Error " + error);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
                            notifyApiDelay(SelectFarmActivity.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        notifyApiDelay(SelectFarmActivity.this, call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);
                        Log.e(TAG, "Failure " + t.toString());
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_verify_farm_area));
                        hideProgress();
                    }

                });
                internetFlow(isLoaded[0]);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String s) {


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }


    private void hideProgress(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (adapter!=null)
                    adapter.setClickable(true);
                progressBar_image.setVisibility(View.GONE);
            }
        });
    }

}
