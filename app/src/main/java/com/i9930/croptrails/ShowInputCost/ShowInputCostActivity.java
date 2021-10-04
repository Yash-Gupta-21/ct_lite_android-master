package com.i9930.croptrails.ShowInputCost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.InputCost.InputCostCacheManager;
import com.i9930.croptrails.RoomDatabase.InputCost.InputCostFetchListener;
import com.i9930.croptrails.RoomDatabase.InputCost.InputCostT;
import com.i9930.croptrails.RoomDatabase.ResourceCost.ResourceCacheManager;
import com.i9930.croptrails.RoomDatabase.ResourceCost.ResourceCostT;
import com.i9930.croptrails.RoomDatabase.ResourceCost.ResourceFetchListener;
import com.i9930.croptrails.ShowInputCost.Adapters.InputCostAdapter;
import com.i9930.croptrails.ShowInputCost.Adapters.ResourceUsedAdapter;
import com.i9930.croptrails.ShowInputCost.Model.InputCostData;
import com.i9930.croptrails.ShowInputCost.Model.InputCostResponse;
import com.i9930.croptrails.ShowInputCost.Model.ResourceData;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShowInputCostActivity extends AppCompatActivity implements InputCostFetchListener, ResourceFetchListener {
    Toolbar mActionBarToolbar;
    String TAG = "ShowInputCostActivity";
    Context context;
    TextView title;
    Resources resources;
    ViewFailDialog viewFailDialog;
    ProgressDialog progressDialog;
    @BindView(R.id.cost_whole_srcoll_view)
    ScrollView scrollViewWhole;

    //Cost Views
    @BindView(R.id.recyclerInputCost)
    RecyclerView recyclerInputCost;
    @BindView(R.id.input_cost_layout)
    LinearLayout input_cost_layout;

    //Resource Views
    @BindView(R.id.recyclerResources)
    RecyclerView recyclerResources;
    @BindView(R.id.resource_layout)
    LinearLayout resource_layout;


    @BindView(R.id.no_data_available)
    RelativeLayout no_data_available;
    int inputSize=0;
    int resourceSize=0;


    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
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
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        setContentView(R.layout.activity_show_input_cost);
        context = this;
        ButterKnife.bind(this);

        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        viewFailDialog = new ViewFailDialog();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(resources.getString(R.string.please_wait_msg));
        title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.farmer_cost_title));
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        recyclerInputCost.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        recyclerInputCost.setLayoutManager(layoutManager);
        recyclerInputCost.setNestedScrollingEnabled(false);

        recyclerResources.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        recyclerResources.setLayoutManager(layoutManager2);
        recyclerResources.setNestedScrollingEnabled(false);
        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            //Offline Mode
            InputCostCacheManager.getInstance(context).getInputCost(this, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
            ResourceCacheManager.getInstance(context).getResource(this, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
            if (inputSize==0&&resourceSize==0){
                Log.e(TAG,"In Check Condition");
                no_data_available.setVisibility(View.VISIBLE);
                scrollViewWhole.setVisibility(View.GONE);
            }
        } else {
            //Online Mode
            getInputCosts();
        }


    }

    private void getInputCosts() {
        progressDialog.show();
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        // farmId="1";
        apiInterface.getInputCostList(farmId,userId,token).enqueue(new Callback<InputCostResponse>() {
            @Override
            public void onResponse(Call<InputCostResponse> call, Response<InputCostResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(ShowInputCostActivity.this, resources.getString(R.string.multiple_login_msg));
                    } else {

                        Log.e(TAG, "Show Data INPUT COST " + new Gson().toJson(response.body().getInputCostList()));
                        Log.e(TAG, "Show Data RESOURCE " + new Gson().toJson(response.body().getResourceList()));
                        progressDialog.cancel();
                        int inputCostSize = 0;
                        int resourceSize = 0;

                        if (response.body().getInputCostList() != null && response.body().getInputCostList().size() > 0) {
                            inputCostSize = response.body().getInputCostList().size();
                            InputCostAdapter adapter = new InputCostAdapter(context, response.body().getInputCostList());
                            recyclerInputCost.setAdapter(adapter);

                        } else {
                            inputCostSize = 0;
                            input_cost_layout.setVisibility(View.GONE);
                        }
                        if (response.body().getResourceList() != null && response.body().getResourceList().size() > 0) {
                            resourceSize = response.body().getResourceList().size();
                            ResourceUsedAdapter adapter = new ResourceUsedAdapter(context, response.body().getResourceList());
                            recyclerResources.setAdapter(adapter);
                        } else {
                            resourceSize = 0;
                            resource_layout.setVisibility(View.GONE);
                        }
                        if (resourceSize == 0 && inputCostSize == 0) {
                            no_data_available.setVisibility(View.VISIBLE);
                            scrollViewWhole.setVisibility(View.GONE);
                        }
                    }
                } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(ShowInputCostActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code()== AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(ShowInputCostActivity.this, resources.getString(R.string.authorization_expired));
                }else {
                    try {
                        Log.e(TAG, "Input Cost Err " + response.errorBody().string().toString());
                        progressDialog.cancel();
                        viewFailDialog.showDialogForFinish(ShowInputCostActivity.this, resources.getString(R.string.fail_to_load_cost_msg));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<InputCostResponse> call, Throwable t) {

                Log.e(TAG, "Input Cost Failure " + t.toString());
                progressDialog.cancel();
                viewFailDialog.showDialogForFinish(ShowInputCostActivity.this, resources.getString(R.string.fail_to_load_cost_msg));

            }
        });
    }

    @Override
    public void onInputCostLoaded(InputCostT inputCostT) {
        Log.e(TAG,"In onInputCostLoaded "+new Gson().toJson(inputCostT));
        if (inputCostT != null) {
            try {
                JSONArray array = new JSONArray(inputCostT.getInputCostData());
                JSONArray arrayOffline = new JSONArray(inputCostT.getInputCostJsonOfflineAdded());
                inputSize=array.length();
                inputSize=inputSize+arrayOffline.length();
                List<InputCostData>inputCostDataList=new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object=array.getJSONObject(i);
                    InputCostData data=new Gson().fromJson(object.toString(),InputCostData.class);
                    inputCostDataList.add(data);
                }
                for (int i = 0; i < arrayOffline.length(); i++) {
                    JSONObject object=arrayOffline.getJSONObject(i);
                    InputCostData data=new Gson().fromJson(object.toString(),InputCostData.class);
                    inputCostDataList.add(data);
                }

                if (inputSize>0){
                    input_cost_layout.setVisibility(View.VISIBLE);
                    no_data_available.setVisibility(View.GONE);
                    scrollViewWhole.setVisibility(View.VISIBLE);
                    InputCostAdapter adapter = new InputCostAdapter(context, inputCostDataList);
                    recyclerInputCost.setAdapter(adapter);
                }else {
                    input_cost_layout.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {

        }
    }

    @Override
    public void onInputCostUpdated() {

    }
    @Override
    public void onResourceLoaded(ResourceCostT resourceCost) {
        Log.e(TAG,"In onResourceLoaded "+new Gson().toJson(resourceCost));
        if (resourceCost != null) {
            try {
                JSONArray array = new JSONArray(resourceCost.getResourceJson());
                JSONArray arrayOffline = new JSONArray(resourceCost.getResourceJsonOffline());
                resourceSize=array.length();
                resourceSize=resourceSize+arrayOffline.length();
                List<ResourceData>resourceDataList=new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object=array.getJSONObject(i);
                    ResourceData data=new Gson().fromJson(object.toString(),ResourceData.class);
                    resourceDataList.add(data);
                }
                for (int i = 0; i < arrayOffline.length(); i++) {
                    JSONObject object=arrayOffline.getJSONObject(i);
                    ResourceData data=new Gson().fromJson(object.toString(),ResourceData.class);
                    resourceDataList.add(data);
                }
                if (resourceSize>0){
                    resource_layout.setVisibility(View.VISIBLE);
                    no_data_available.setVisibility(View.GONE);
                    scrollViewWhole.setVisibility(View.VISIBLE);
                    ResourceUsedAdapter adapter = new ResourceUsedAdapter(context, resourceDataList);
                    recyclerResources.setAdapter(adapter);
                }else {
                    input_cost_layout.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResourceUpdated() {

    }
}
