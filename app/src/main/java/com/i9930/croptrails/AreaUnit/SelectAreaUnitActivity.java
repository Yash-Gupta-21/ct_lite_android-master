package com.i9930.croptrails.AreaUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.AreaUnit.Adapter.AreaUnitRvAdapter;
import com.i9930.croptrails.CommonClasses.DDNew;
import com.i9930.croptrails.CommonClasses.DDResponseNew;
import com.i9930.croptrails.CommonClasses.ParamData;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

public class SelectAreaUnitActivity extends AppCompatActivity {

    String TAG = "SelectAreaUnitActivity";
    SelectAreaUnitActivity context;
    RecyclerView recyclerAreaUnit;
    Button selectArea;
    Toolbar mActionBarToolbar;
    Resources resources;
    GifImageView progressBar_image;
    ViewFailDialog viewFailDialog = new ViewFailDialog();
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
        setContentView(R.layout.activity_select_area_unit);
        context = this;
        loadAds();
        progressBar_image=findViewById(R.id.progressBar_image);
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
                SelectAreaUnitActivity.this.onBackPressed();
            }
        });
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.area_unit_label));
        recyclerAreaUnit = findViewById(R.id.recyclerAreaUnit);
        selectArea = findViewById(R.id.selectArea);
        recyclerAreaUnit.setHasFixedSize(true);
        recyclerAreaUnit.setLayoutManager(new LinearLayoutManager(context));
        areaUnitRvAdapter = new AreaUnitRvAdapter(context, ddNewLisFiltered, new AreaUnitRvAdapter.AreaUnitSelectListener() {
            @Override
            public void onUnitSelected(int index, DDNew ddNew) {
                selectedUnit=ddNew;
                selectArea.setVisibility(View.VISIBLE);
            }
        });
        recyclerAreaUnit.setAdapter(areaUnitRvAdapter);
        getAllDataNew(AppConstants.CHEMICAL_UNIT.AREA_UNIT);

        selectArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUnit();
            }
        });
    }

    List<DDNew> ddNewLis = new ArrayList<>();
    List<DDNew> ddNewLisFiltered = new ArrayList<>();
    AreaUnitRvAdapter areaUnitRvAdapter;
    DDNew selectedUnit=null;

    private void getAllDataNew(final String type) {
        progressBar_image.setVisibility(View.VISIBLE);

        ddNewLis.clear();
        ddNewLisFiltered.clear();
        ParamData paramData = new ParamData();
        paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        paramData.setType(type);

        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        Log.e(TAG, "Sending New Param  AND type " + type + " " + new Gson().toJson(paramData));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getNewParameters(paramData).enqueue(new Callback<DDResponseNew>() {
            @Override
            public void onResponse(Call<DDResponseNew> call, Response<DDResponseNew> response1) {
                String error = null;
                if (response1.isSuccessful()) {
                    DDResponseNew response = response1.body();
                    Log.e(TAG, "Sending New Param Response " + type + "  " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
                        viewFailDialog.showSessionExpireDialog(SelectAreaUnitActivity.this, response.getMsg());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        viewFailDialog.showSessionExpireDialog(SelectAreaUnitActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        viewFailDialog.showSessionExpireDialog(SelectAreaUnitActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        if (response != null && response.getData() != null && response.getData().size() > 0) {
                            ddNewLis.addAll(response.getData());
                            ddNewLisFiltered.addAll(ddNewLis);
                            if (areaUnitRvAdapter != null)
                                areaUnitRvAdapter.notifyDataSetChanged();

                        }
                    }
                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    viewFailDialog.showSessionExpireDialog(SelectAreaUnitActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(SelectAreaUnitActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "Cache Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                progressBar_image.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DDResponseNew> call, Throwable t) {
                Log.e(TAG, "Failure getOfflineData " + t.toString());
                progressBar_image.setVisibility(View.GONE);
            }
        });


    }
    private void changeUnit() {
        progressBar_image.setVisibility(View.VISIBLE);
        if (selectedUnit!=null) {
            ParamData paramData = new ParamData();
            paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
            paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            paramData.setAreaUnitId(selectedUnit.getId());

            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            Log.e(TAG, "Sending New Param " + new Gson().toJson(paramData));
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            apiInterface.changeAreaUnit(paramData).enqueue(new Callback<StatusMsgModel>() {
                @Override
                public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response1) {
                    String error = null;
                    if (response1.isSuccessful()) {
                        StatusMsgModel response = response1.body();
                        Log.e(TAG, "Sending New Param Response "  + new Gson().toJson(response));
                        if (response.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, response.getMsg());
                        } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                        } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                        } else if(response.getStatus()==1){

                            if (selectedUnit.getValue()!=null&&!TextUtils.isEmpty(selectedUnit.getValue())){
                                try {
                                    SharedPreferencesMethod.setDoubleSharedPreference(context,SharedPreferencesMethod.AREA_MULTIPLICATON_FACTOR,Double.valueOf(selectedUnit.getValue()));
                                    SharedPreferencesMethod.setString(context,SharedPreferencesMethod.AREA_UNIT_LABEL,selectedUnit.getParameters());
                                }catch (NumberFormatException e){

                                }catch (Exception e){}
                            }

                            Toasty.success(context, resources.getString(R.string.unit_update_success_msg), Toast.LENGTH_LONG, true).show();
                            Intent intent = new Intent(context, LandingActivity.class);
                            ActivityOptions options = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                finishAffinity();
                                startActivity(intent, options.toBundle());
                                fileList();
                            } else {
                                finishAffinity();
                                startActivity(intent);
                                finish();
                            }
                        }else {
                            Toasty.error(context,resources.getString(R.string.unit_update_fail),Toast.LENGTH_LONG).show();
                        }
                    } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                    } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            Toasty.error(context,resources.getString(R.string.unit_update_fail),Toast.LENGTH_LONG).show();
                            error = response1.errorBody().string().toString();
                            Log.e(TAG, "Cache Error " + error);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    progressBar_image.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                    Log.e(TAG, "Failure getOfflineData " + t.toString());
                    Toasty.error(context,resources.getString(R.string.unit_update_fail),Toast.LENGTH_LONG).show();
                    progressBar_image.setVisibility(View.GONE);
                }
            });

        }else {
            Toasty.error(context,"Please select area unit",Toast.LENGTH_SHORT).show();
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
//                searchView.clearFocus();
//                Toast.makeText(context, "onMenutItemActionCollapse called", Toast.LENGTH_SHORT).show();
                ddNewLisFiltered.clear();
                ddNewLisFiltered.addAll(ddNewLis);
                if (areaUnitRvAdapter != null)
                    areaUnitRvAdapter.notifyDataSetChanged();
                return true;
            }
        });


        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @SuppressLint("RestrictedApi")
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

                }else {
                    ddNewLisFiltered.addAll(ddNewLis);
                    if (areaUnitRvAdapter!=null)
                        areaUnitRvAdapter.notifyDataSetChanged();
                }
                return true;
            }

            @SuppressLint("RestrictedApi")
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

                }else {
                    ddNewLisFiltered.addAll(ddNewLis);
                    if (areaUnitRvAdapter!=null)
                        areaUnitRvAdapter.notifyDataSetChanged();
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

    SearchAsynch searchAsynch;

    private class SearchAsynch extends AsyncTask<String, Integer, String> {
        String query;

        public SearchAsynch(String query) {
            this.query = query.trim();
        }

        @Override
        protected String doInBackground(String... strings) {
            ddNewLisFiltered.clear();
            if (ddNewLis != null) {
                for (int i = 0; i < ddNewLis.size(); i++) {
                    try {
                        DDNew data = ddNewLis.get(i);
                        if (data.getParameters() != null && data.getParameters().toLowerCase().contains(query.toLowerCase())) {
                            ddNewLisFiltered.add(data);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            SelectAreaUnitActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   /* if (ddNewLisFiltered.size() > 0) {
                        setRecyclerData(filteredList);
                    } else {
                        noDataTv.setVisibility(View.VISIBLE);
                    }*/

                   areaUnitRvAdapter.notifyDataSetChanged();
                }
            });
            return null;
        }
    }
}