package com.i9930.croptrails.ClusterSelection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.gson.Gson;

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
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.ClusterSelection.Adapter.ClusterRvAdapter;
import com.i9930.croptrails.CommonClasses.Cluster;
import com.i9930.croptrails.CommonClasses.ClusterResponse;
import com.i9930.croptrails.CompSelect.Adapter.CompanyRvAdapter;
import com.i9930.croptrails.CompSelect.CompSelectActivity;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownCacheManager;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

public class ClusterSelectActivityActivity extends AppCompatActivity {
    ClusterSelectActivityActivity context;
    String TAG="ClusterSelectActivityActivity";
    List<Cluster>clusterList=new ArrayList<>();
    Resources resources;
    Toolbar mActionBarToolbar;
    @BindView(R.id.compRecycler)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    GifImageView progressBar;
    @BindView(R.id.submitBtn)
    Button submitBtn;
    int selectedIndex = -1;
    ViewFailDialog viewFailDialog = new ViewFailDialog();
    @BindView(R.id.connectivityLine)
    View connectivityLine;
    ClusterRvAdapter adapter;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluster_select_activity);
        context=this;
        loadAds();
        resources=getResources();
        ButterKnife.bind(this);
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(getString(R.string.select_cluster_label));
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
                        ClusterSelectActivityActivity.super.onBackPressed();
                    }
                }
        );


        adapter= new ClusterRvAdapter(context, clusterList, new ClusterRvAdapter.ClusterSelectListener() {
            @Override
            public void onCompanySelected(int index) {
                selectedIndex = index;
                if (index == -1) {
                    submitBtn.setVisibility(View.GONE);
                } else if (index > -1) {
                    submitBtn.setVisibility(View.VISIBLE);
                    //  Toasty.info(context, companyDatumList.get(index).getCompName() + " selected", Toast.LENGTH_LONG, false).show();
                }
            }
        });
        progressBar.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedIndex == -1 || selectedIndex < 0) {
                    Toasty.error(context, getString(R.string.please_select_a_cluster), Toast.LENGTH_LONG).show();
                } else {
                    updateUserCreds(clusterList.get(selectedIndex));
                }
            }
        });
        DropDownCacheManager.getInstance(context).getDataByType(new DropDownCacheManager.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT dropDown) {
                Log.e(TAG, "Offline clusters " + new Gson().toJson(dropDown));

                if (dropDown != null && AppConstants.isValidString(dropDown.getData()) && !dropDown.getData().trim().equals("[]")) {
                    try {
                        clusterList.clear();
                        JSONArray array = new JSONArray(dropDown.getData());
                        for (int j = 0; j < array.length(); j++) {
                            JSONObject object = array.getJSONObject(j);
                            Cluster cluster = new Gson().fromJson(object.toString(), Cluster.class);
                            clusterList.add(cluster);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else  {
                    getClusterOfCompany();
                }
            }
        }, AppConstants.CHEMICAL_UNIT.CLUSTER_LIST);

    }

    private void updateUserCreds(Cluster cluster) {
        SharedPreferencesMethod.setString(context,SharedPreferencesMethod.SVCLUSTERID,cluster.getClusterId());
        SharedPreferencesMethod.setString(context,SharedPreferencesMethod.CLUSTER_NAME,cluster.getClusterName());
        SharedPreferencesMethod.setBoolean(context,SharedPreferencesMethod.IS_CLUSTER_SELECTED,true);
        Intent intent = new Intent(context, LandingActivity.class);
        ActivityOptions options = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
        }
        //intent.putExtra("activity","login");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options.toBundle());
            finish();
            finishAffinity();
        } else {
            startActivity(intent);
            finish();
            finishAffinity();
        }
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

                        if (response.body().getClusterList() != null) {
                            clusterList.addAll(response.body().getClusterList());
                        }
                        adapter.notifyDataSetChanged();
                        DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST);
                        DropDownT clusters = new DropDownT(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST, new Gson().toJson(clusterList));
                        DropDownCacheManager.getInstance(context).addChemicalUnit(clusters);

                    } else if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context);
                        //progressDialog.cancel();
                    } else {
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
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                internetFlow(isLoaded[0]);
            }
        }, AppConstants.DELAY);*/

    }
}