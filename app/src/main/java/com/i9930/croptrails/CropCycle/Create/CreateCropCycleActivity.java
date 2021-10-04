package com.i9930.croptrails.CropCycle.Create;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterNewDD;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterSeason;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.DDNew;
import com.i9930.croptrails.CommonClasses.DDResponseNew;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.CropCycle.Create.Adapter.CreateCropCycleAdapter;
import com.i9930.croptrails.CropCycle.Create.Model.AddCycleAgriDatum;
import com.i9930.croptrails.CropCycle.Create.Model.AddCycleDatum;
import com.i9930.croptrails.CropCycle.Create.Model.FetchType.ActivityType;
import com.i9930.croptrails.CropCycle.Create.Model.FetchType.ActivityTypeResponse;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.Season;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownCacheManager;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownFetchListener;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownCacheManager2;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownFetchListener2;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownT2;
import com.i9930.croptrails.SubmitActivityForm.Model.CompAgriDatum;
import com.i9930.croptrails.SubmitActivityForm.Model.CompAgriResponse;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.databinding.ActivityCreateCropCycleBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCropCycleActivity extends AppCompatActivity {
    String TAG = "CreateCropCycleActivity";
    CreateCropCycleActivity context;
    Resources resources;
    List<CompAgriDatum> compAgriDatumList = new ArrayList<>();
    AdView mAdView;
    @BindView(R.id.etCropCycleName)
    EditText etCropCycleName;
    @BindView(R.id.cropSpinner)
    SearchableSpinner cropSpinner;
    @BindView(R.id.seasonSpinner)
    SearchableSpinner seasonSpinner;
    @BindView(R.id.soilTypeSpinner)
    SearchableSpinner soilTypeSpinner;
    @BindView(R.id.etRegion)
    EditText etRegion;
    @BindView(R.id.cycleDetailsRel)
    RelativeLayout cycleDetailsRel;
    @BindView(R.id.etExpHarvestDay)
    EditText etExpHarvestDay;
    @BindView(R.id.etExpFlowerDay)
    EditText etExpFlowerDay;


    @BindView(R.id.cycleDetailsInner)
    LinearLayout cycleDetailsInner;
    @BindView(R.id.arrowCycle)
    ImageView arrowCycle;
    @BindView(R.id.activityRecycler)
    RecyclerView activityRecycler;
    CreateCropCycleAdapter adapter;
    ViewFailDialog viewFailDialog = new ViewFailDialog();
    List<AddCycleDatum> addCycleDatumList = new ArrayList<>();
    @BindView(R.id.addMore)
    FloatingActionButton addMore;
    String userId, token, compId;
    List<ActivityType> activityTypeList = new ArrayList<>();
    List<Season> seasonDDList = new ArrayList<>();

    @BindView(R.id.submitBtn)
    Button submitBtn;
    String auth;

    ActivityCreateCropCycleBinding binding;
    Toolbar mActionBarToolbar;
    Intent intent=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_crop_cycle);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_crop_cycle);
        resources = getResources();
        ButterKnife.bind(this);
        userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        loadAds();
        onClicks();
//        addCycleDatumList.add(getNewDatum());
        adapter = new CreateCropCycleAdapter(context, addCycleDatumList, compAgriDatumList);
        activityRecycler.setHasFixedSize(true);
        activityRecycler.setNestedScrollingEnabled(false);
        activityRecycler.setLayoutManager(new LinearLayoutManager(context));
        activityRecycler.setAdapter(adapter);
        intent=getIntent();
        getActivityTypes();
        setupToolbar();
    }
    private void setupToolbar(){
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
                CreateCropCycleActivity.this.onBackPressed();
            }
        });
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.create_crop_cycle));
    }

    private void onClicks() {

//        cropSpinner.();

        cycleDetailsRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(cycleDetailsInner, arrowCycle);
            }
        });

        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCycleDatumList.add(getNewDatum());
                adapter.notifyDataSetChanged();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DDNew crop = (DDNew) cropSpinner.getSelectedItem();
                DDNew soilType = (DDNew) cropSpinner.getSelectedItem();
                Season season = (Season) seasonSpinner.getSelectedItem();
                if (TextUtils.isEmpty(etCropCycleName.getText().toString())) {
                    etCropCycleName.setError(resources.getString(R.string.required_label));
                    etCropCycleName.requestFocus();
                    AppConstants.failToast(context, "Please enter crop cycle name");
                } else if (crop == null) {
//                    etCropCycleName.setError(resources.getString(R.string.required_label));
                    cropSpinner.requestFocus();
                    AppConstants.failToast(context, "Please select crop");
                } else if (season == null) {
//                    etCropCycleName.setError(resources.getString(R.string.required_label));
                    seasonSpinner.requestFocus();
                    AppConstants.failToast(context, "Please select season");
                } else if (TextUtils.isEmpty(etRegion.getText().toString())) {
                    etRegion.setError(resources.getString(R.string.required_label));
                    etRegion.requestFocus();
                    AppConstants.failToast(context, "Please enter region name");
                } else if (crop == null) {
//                    etCropCycleName.setError(resources.getString(R.string.required_label));
                    soilTypeSpinner.requestFocus();
                    AppConstants.failToast(context, "Please select soil type");
                } else if (TextUtils.isEmpty(etExpHarvestDay.getText().toString())) {
                    etExpHarvestDay.setError(resources.getString(R.string.required_label));
                    etExpHarvestDay.requestFocus();
                    AppConstants.failToast(context, "Please enter expected harvest day");
                } else if (TextUtils.isEmpty(etExpFlowerDay.getText().toString())) {
                    etExpFlowerDay.setError(resources.getString(R.string.required_label));
                    etExpFlowerDay.requestFocus();
                    AppConstants.failToast(context, "Please enter expected flowering day");
                } else if (addCycleDatumList == null || addCycleDatumList.size() == 0) {
                    AppConstants.failToast(context, "Please add at least one calendar activity");
                } else {
                    submitBtn.setClickable(false);
                    submitBtn.setEnabled(false);


                    new ValidateAsync(etCropCycleName.getText().toString().trim(),
                            crop.getParameters(), season.getSeasonNum(), soilType.getId(), etRegion.getText().toString().trim(),
                            etExpHarvestDay.getText().toString().trim(), etExpFlowerDay.getText().toString().trim(), crop.getId()).execute();
                }
            }
        });
    }

    public void animate(final View cardView, ImageView down_arrow_img) {
        if (cardView.getVisibility() == View.GONE) {
            RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setInterpolator(new DecelerateInterpolator());
            rotateAnimation.setRepeatCount(0);
            rotateAnimation.setDuration(500);
            rotateAnimation.setFillAfter(true);
            down_arrow_img.startAnimation(rotateAnimation);
            cardView.setFocusable(true);

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_down);
            animation.setDuration(500);
            cardView.setAnimation(animation);
            cardView.animate();
            animation.start();
            cardView.setVisibility(View.VISIBLE);
        } else {
            RotateAnimation rotateAnimation = new RotateAnimation(180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setInterpolator(new DecelerateInterpolator());
            rotateAnimation.setRepeatCount(0);
            rotateAnimation.setDuration(500);
            rotateAnimation.setFillAfter(true);
            down_arrow_img.startAnimation(rotateAnimation);

            cardView.setVisibility(View.INVISIBLE);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            animation.setDuration(500);
            cardView.setAnimation(animation);
            cardView.animate();
            animation.start();
            cardView.setFocusable(false);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    cardView.setVisibility(View.GONE);
                }
            }, 500);
        }

    }

    private AddCycleDatum getNewDatum() {
        List<AddCycleAgriDatum> list = new ArrayList<>();
        if (compAgriDatumList != null && compAgriDatumList.size() > 0) {
            /*for (int i = 0; i < compAgriDatumList.size(); i++) {
                list.add(compAgriDatumList.get(i));
            }*/
            AddCycleAgriDatum addCycleAgriDatum = new AddCycleAgriDatum(compAgriDatumList);
            list.add(addCycleAgriDatum);
        }
        AddCycleDatum datum = new AddCycleDatum(list);

        List<ActivityType> activityTypes = new ArrayList<>();
        if (activityTypeList != null)
            for (int i = 0; i < activityTypeList.size(); i++) {
                activityTypes.add(ActivityType.copy(activityTypeList.get(i)));
            }
        datum.setActivityTypeList(activityTypes);
        return datum;

    }

    private void loadAds() {
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
                Log.e(TAG, "Add Errro " + adError.toString());
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

    private void getDropDownData() {
        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
//                    getCompAgriInputs();
                } else {
                    try {
                        CompAgriResponse compAgriResponse = new Gson().fromJson(dropDown.getData(), CompAgriResponse.class);
                        if (compAgriResponse != null && compAgriResponse.getData() != null && compAgriResponse.getData().size() > 0) {
                            compAgriDatumList.addAll(compAgriResponse.getData());
                            adapter.setAgriDatumList(compAgriDatumList);
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }
        }, AppConstants.CHEMICAL_UNIT.COMP_AGRI_INPUTS);
        final String[] where1 = {AppConstants.CHEMICAL_UNIT.FARM_SEASON};
        DropDownCacheManager.getInstance(context).getAllChemicalUnits(new DropDownFetchListener() {
            @Override
            public void onChemicalUnitLoaded(List<DropDownT> chemicalUnitList) {
                for (int i = 0; i < chemicalUnitList.size(); i++) {
                    Log.e(TAG, "getDataType() " + chemicalUnitList.get(i).getDataType() + "  ==>  " + chemicalUnitList.get(i).getData());
                    if (AppConstants.CHEMICAL_UNIT.FARM_SEASON.equals(chemicalUnitList.get(i).getDataType())) {
                        //SEASON
                        if (chemicalUnitList.get(i).getData() != null) {
                            try {
                                JSONArray array = new JSONArray(chemicalUnitList.get(i).getData());
                                for (int j = 0; j < array.length(); j++) {
                                    JSONObject object = array.getJSONObject(j);
                                    Season data = new Gson().fromJson(object.toString(), Season.class);
                                    seasonDDList.add(data);
                                }
                                SpinnerAdapterSeason adapter = new SpinnerAdapterSeason(context, seasonDDList);
                                seasonSpinner.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }, where1);

        final String[] where = {
                AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE_NEW, AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW,};
        DropDownCacheManager2.getInstance(context).getAllChemicalUnits(new DropDownFetchListener2() {
            @Override
            public void onChemicalUnitLoaded(List<DropDownT2> chemicalUnitList) {
                if (chemicalUnitList != null && chemicalUnitList.size() > 0) {

                    setOfflineSpinnerData(chemicalUnitList);


                } else {

                }
            }
        }, where);
    }

    private void getActivityTypes() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comp_id", compId);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("user_id", userId);


        final boolean[] isLoaded = {false};

        long currMillis = AppConstants.getCurrentMills();
        Log.e(TAG, "Sending " + new Gson().toJson(jsonObject));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getActivityTypes(jsonObject).enqueue(new Callback<ActivityTypeResponse>() {
            @Override
            public void onResponse(Call<ActivityTypeResponse> call, Response<ActivityTypeResponse> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    ActivityTypeResponse response = response1.body();
                    if (response.getStatus() == 10) {
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, response.getMessage());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                    } else {

                        if (response != null && response.getData() != null && response.getData().size() > 0) {
                            activityTypeList = response.getData();
                            getDropDownData();
                        }
                    }
                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access

                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "Cache Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ActivityTypeResponse> call, Throwable t) {
                Log.e("OfflineModeAct", "Failure getOfflineData " + t.toString());
            }
        });

    }

    private void setOfflineSpinnerData(List<DropDownT2> chemicalUnitList) {

        List<DDNew> soilTypeList1 = new ArrayList<>();
        List<DDNew> cropList = new ArrayList<>();
        for (int i = 0; i < chemicalUnitList.size(); i++) {
            Log.e(TAG, "getDataType() " + chemicalUnitList.get(i).getDataType() + "  ==>  " + chemicalUnitList.get(i).getData());
            if (AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE_NEW.equals(chemicalUnitList.get(i).getDataType())) {
                try {
                    DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                    if (data.getData() != null)
                        soilTypeList1.addAll(data.getData());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW.equals(chemicalUnitList.get(i).getDataType())) {
                //SOIL TYPE
                try {
                    DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                    if (data.getData() != null)
                        cropList.addAll(data.getData());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        SpinnerAdapterNewDD cropSpinnerAdapter = new SpinnerAdapterNewDD(context, cropList, resources.getString(R.string.select_crop_rompt));
        cropSpinner.setAdapter(cropSpinnerAdapter);
        SpinnerAdapterNewDD stSpinnerAdapter = new SpinnerAdapterNewDD(context, soilTypeList1, resources.getString(R.string.select_soil_type_prompt));
        soilTypeSpinner.setAdapter(stSpinnerAdapter);
        addCycleDatumList.add(getNewDatum());
        adapter.notifyDataSetChanged();

    }


    public class ValidateAsync extends AsyncTask<String, Integer, String> {

        String calendarName, cropName, growingSeason, soilType, growingRegion, expHarvesDay, expFlowerDay, cropId;
        int index = -1;
        CreateCropCycleAdapter.ErrorType type = null;

        public ValidateAsync(String calendarName, String cropName, String growingSeason, String soilType,
                             String growingRegion, String expHarvesDay, String expFlowerDay, String cropId) {
            this.calendarName = calendarName;
            this.cropName = cropName;
            this.growingSeason = growingSeason;
            this.soilType = soilType;
            this.growingRegion = growingRegion;
            this.expHarvesDay = expHarvesDay;
            this.expFlowerDay = expFlowerDay;
            this.cropId = cropId;
        }

        @Override
        protected String doInBackground(String... strings) {

            for (int i = 0; i < addCycleDatumList.size(); i++) {
                AddCycleDatum datum = addCycleDatumList.get(i);
//                Log.e(TAG,"Starting data "+new Gson().toJson(datum));
                if (!AppConstants.isValidString(datum.getDayNo())) {
                    index = i;
                    type = CreateCropCycleAdapter.ErrorType.DAY_NO;
                    break;
                } else if (!AppConstants.isValidString(datum.getActivity())) {
                    index = i;
                    type = CreateCropCycleAdapter.ErrorType.ACTIVITY;
                    break;
                } else if (datum.getSelectedActivity() == null) {
                    index = i;
                    type = CreateCropCycleAdapter.ErrorType.ACTIVITY_TYPE;
                    break;
                } else if (!AppConstants.isValidActualArea(datum.getSelectedPriority())) {
                    index = i;
                    type = CreateCropCycleAdapter.ErrorType.PRIORITY;
                    break;
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (index == -1 && type == null) {
//                Log.e(TAG,"Starting...");
                new UploadAsync(calendarName, cropName, growingSeason, soilType, growingRegion, expHarvesDay, expFlowerDay, cropId).execute();
            } else {
//                Log.e(TAG,"Not Starting... "+index+", type "+type);
                adapter.showError(index, type);
                submitBtn.setClickable(true);
                submitBtn.setEnabled(true);
            }
        }
    }

    public class UploadAsync extends AsyncTask<String, Integer, String> {

        String calendarName, cropName, growingSeason, soilType, growingRegion, expHarvesDay, expFlowerDay, cropId;

        public UploadAsync(String calendarName, String cropName, String growingSeason, String soilType,
                           String growingRegion, String expHarvesDay, String expFlowerDay, String cropId) {
            this.calendarName = calendarName;
            this.cropName = cropName;
            this.growingSeason = growingSeason;
            this.soilType = soilType;
            this.growingRegion = growingRegion;
            this.expHarvesDay = expHarvesDay;
            this.expFlowerDay = expFlowerDay;
            this.cropId = cropId;
        }

        @Override
        protected String doInBackground(String... strings) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("calendar_name", calendarName);
            jsonObject.addProperty("crop_name", cropName);
            jsonObject.addProperty("growing_season", growingSeason);
            jsonObject.addProperty("soil_type", soilType);
            jsonObject.addProperty("growing_region", growingRegion);
            jsonObject.addProperty("exp_harvest_day", expHarvesDay);
            jsonObject.addProperty("exp_flowering_day", expFlowerDay);
            jsonObject.addProperty("comp_id", compId);
            jsonObject.addProperty("token", token);
            jsonObject.addProperty("user_id", userId);
            jsonObject.addProperty("crop_id", cropId);
            JsonArray items = new JsonArray();
            for (int i = 0; i < addCycleDatumList.size(); i++) {
                AddCycleDatum datum = addCycleDatumList.get(i);
                List<AddCycleAgriDatum> agriDatumList = datum.getAgriInputs();
                JsonObject object = new JsonObject();
                object.addProperty("day_no", datum.getDayNo());
                object.addProperty("activity", datum.getActivity());
                if (datum.getSelectedActivity() != null)
                    object.addProperty("activity_type", datum.getSelectedActivity().getActivityTypeId());
                else
                    object.addProperty("activity_type", "");
                object.addProperty("priority", datum.getSelectedPriority());
                object.addProperty("description", datum.getDescription());
                if (datum.getInst1() != null)
                    object.addProperty("instruction1", datum.getInst1());
                else
                    object.addProperty("instruction1", "");
                if (datum.getInst2() != null)
                    object.addProperty("instruction2", datum.getInst2());
                else
                    object.addProperty("instruction2", "");
                if (datum.getInst3() != null)
                    object.addProperty("instruction3", datum.getInst3());
                else
                    object.addProperty("instruction3", "");
                if (datum.getInst4() != null)
                    object.addProperty("instruction4", datum.getInst4());
                else
                    object.addProperty("instruction4", "");
                if (datum.getInst5() != null)
                    object.addProperty("instruction5", datum.getInst5());
                else
                    object.addProperty("instruction5", "");
                JsonArray cost = new JsonArray();
                if (agriDatumList != null && agriDatumList.size() > 0) {
                    for (int j = 0; j < agriDatumList.size(); j++) {
                        AddCycleAgriDatum agriDatum = agriDatumList.get(j);
                        if (agriDatum.getSelectedAgriDatum() != null) {
                            JsonObject agriData = new JsonObject();
                            if (agriDatum.getSelectedAgriDatum() != null /*&& AppConstants.isValidString(agriDatum.getQty()) && AppConstants.isValidString(agriDatum.getCost())*/) {
                                agriData.addProperty("agriId", agriDatum.getSelectedAgriDatum().getId());
                            } else {
                                agriData.addProperty("agriId", "");
                            }
                            if (agriDatum.getQty() != null)
                                agriData.addProperty("quantity", agriDatum.getQty());
                            else
                                agriData.addProperty("quantity", "");
                            if (agriDatum.getCost() != null)
                                agriData.addProperty("cost", agriDatum.getCost());
                            else
                                agriData.addProperty("cost", "");
                            cost.add(agriData);

                        }
                    }
                }
                object.add("cost", cost);
                items.add(object);

            }
            jsonObject.add("items", items);
            Log.e(TAG, "Submit data " + new Gson().toJson(jsonObject));
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            apiInterface.createCalendar(jsonObject).enqueue(new Callback<StatusMsgModel>() {
                @Override
                public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response1) {
                    String error = null;

                    if (response1.isSuccessful()) {
                        StatusMsgModel response = response1.body();
                        if (response.getStatus() == 10) {
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, response.getMessage());
                        } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                        } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                        } else if (response.getStatus() == 1) {
                            AppConstants.successToast(context, "Crop cycle added successfully");
                            setResult(RESULT_OK,intent);
                            finish();

                        } else {
                            AppConstants.failToast(context, "Failed to add crop cycle, Please try again.");
                            enableButton();
                        }
                    } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access

                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                    } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            error = response1.errorBody().string().toString();
                            Log.e(TAG, "Cache Error " + error);
                            AppConstants.failToast(context, "Failed to add crop cycle, Please try again.");
                            enableButton();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                    Log.e("OfflineModeAct", "Failure getOfflineData " + t.toString());
                    AppConstants.failToast(context, "Failed to add crop cycle, Please try again.");
                    enableButton();
                }
            });
            return null;
        }
    }

    private void enableButton() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                submitBtn.setClickable(true);
                submitBtn.setEnabled(true);
            }
        });
    }
}