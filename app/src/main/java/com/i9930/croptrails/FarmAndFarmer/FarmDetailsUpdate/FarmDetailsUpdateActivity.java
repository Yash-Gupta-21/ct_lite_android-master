package com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import es.dmoral.toasty.Toasty;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.AddFarm.Adapter.AddFarmAdapter2;
import com.i9930.croptrails.AddFarm.AddFarmActivity;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterNewDD;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterNewDDValue;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonAdapters.IrrigationSourceSpinner.IrrigationSourceAdapter;
import com.i9930.croptrails.CommonAdapters.IrrigationTypeSpinner.IrrigationTypeAdapter;
import com.i9930.croptrails.CommonAdapters.SeasonSpinner.SeasonSpinnerAdapter;
import com.i9930.croptrails.CommonAdapters.SoilTypeSpinner.SoilTypeAdapter;
import com.i9930.croptrails.CommonClasses.DDNew;
import com.i9930.croptrails.CommonClasses.DDResponseNew;
import com.i9930.croptrails.CommonClasses.ParamData;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.FarmDetailsUpdateSendData;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.IrrigationSource;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.IrrigationType;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.Season;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.SoilType;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.SpinnerResponse;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownCacheManager;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownFetchListener;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownCacheManager2;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownFetchListener2;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownT2;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmCacheManager;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmLoadListener;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class FarmDetailsUpdateActivity extends AppCompatActivity implements DropDownFetchListener, FarmLoadListener {

    TextView sowingDateet, expFloweringDateet, expHarvestingDateet, update_farm_details;
    SearchableSpinner spinner_irri_source, spinner_irri_type, spinner_soil_type;
    AutoCompleteTextView previousCropEt;
    Calendar myCalendarsowing = Calendar.getInstance();
    Calendar myCalendarflowering = Calendar.getInstance();
    Calendar myCalendarharvesting = Calendar.getInstance();
    Context context;
    GifImageView progressBar;
    String TAG = "FarmDetailsUpdateActivity";
    RelativeLayout rel_lay_farm_det_update;
    ConnectivityManager connectivityManager;
    boolean connected = false;

    Toolbar mActionBarToolbar;
    ViewFailDialog viewFailDialog;
    Resources resources;
    //Material Lists


    List<Season> seasonList = new ArrayList<>();

    Farm farm;


    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
    View connectivityLine;


    SearchableSpinner cropSpinnerPrevious;
    boolean isOnlineMode=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String languageToLoad = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_details_update);

        context = this;
        final String languageCode = SharedPreferencesMethod2.getString(FarmDetailsUpdateActivity.this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.update_farm_details_title));
        connectivityLine=findViewById(R.id.connectivityLine);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        init();

        /*irrigationSourceList.add(new IrrigationSource("0","Select Irrigation Source"));
        irrigationTypeList.add(new IrrigationType("0","Select Irrigation Source"));
        soilTypeList.add(new SoilType("0","Select Irrigation Source"));
        seasonList.add(new Season("0","Select Irrigation Source"));
*/
        viewFailDialog = new ViewFailDialog();
        clickListeners();
        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
//            DropDownCacheManager.getInstance(context).getAllChemicalUnits(this::onChemicalUnitLoaded);
            isOnlineMode=false;
            FarmCacheManager.getInstance(context).getFarm(this::onFarmLoader, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));

        } else {
            isOnlineMode=true;
//            getSpinnerData();

        }
        getSpinnerDataFromCache();


        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {

        } else {
        }
    }
    List<DDNew> cropList = new ArrayList<>();

    private void getSpinnerDataFromCache(){

        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown!=null&&dropDown.getData()!=null&&!TextUtils.isEmpty(dropDown.getData())) {

                    try {
                        DDResponseNew data = new Gson().fromJson(dropDown.getData(), DDResponseNew.class);
                        if (data.getData() != null)
                            cropList.addAll(data.getData());

                        setPreviousCropSpinner(cropList);
                    }catch (Exception e){
                        getAllDataNew4(AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW);
                    }
                }else {
                    getAllDataNew4(AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW);
                }
            }
        },AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW);

        final String[] where = {AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE_NEW, AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE_NEW,
                AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE_NEW};

        DropDownCacheManager2.getInstance(context).getAllChemicalUnits(new DropDownFetchListener2() {
            @Override
            public void onChemicalUnitLoaded(List<DropDownT2> chemicalUnitList) {
                if (chemicalUnitList != null && chemicalUnitList.size() > 0) {

                    setOfflineSpinnerData(chemicalUnitList);

                } else {
                    Log.e(TAG,"No cache data available");

//                        getAllDataNew(AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE_NEW);
//                        getAllDataNew2(AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE_NEW);
//                        getAllDataNew3(AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE_NEW);
//                        getAllDataNew4(AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW);
                    }


                }

        }, where);
    }
    List<DDNew> irrigationSourceList= new ArrayList<>();
    List<DDNew> irrigationTypeList = new ArrayList<>();
    List<DDNew> soilTypeList = new ArrayList<>();
    private void setOfflineSpinnerData(List<DropDownT2> chemicalUnitList) {
        irrigationSourceList= new ArrayList<>();
         irrigationTypeList = new ArrayList<>();
         soilTypeList = new ArrayList<>();
        Log.e(TAG, "getDataType() list size " + chemicalUnitList.size());
        for (int i = 0; i < chemicalUnitList.size(); i++) {
            Log.e(TAG, "getDataType() " + chemicalUnitList.get(i).getDataType() + "  ==>  " + chemicalUnitList.get(i).getData());
            Log.e(TAG, "getDataType Data " + new Gson().toJson(chemicalUnitList.get(i)));
            if (AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE_NEW.equals(chemicalUnitList.get(i).getDataType())) {
                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                        if (data.getData() != null)
                            irrigationSourceList.addAll(data.getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE_NEW.equals(chemicalUnitList.get(i).getDataType())) {
                //IRRIGATION TYPE DD
                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                        if (data.getData() != null)
                            irrigationTypeList.addAll(data.getData());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if (AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE_NEW.equals(chemicalUnitList.get(i).getDataType())) {
                try {
                    DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                    if (data.getData() != null)
                        soilTypeList.addAll(data.getData());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        SpinnerAdapterNewDD irriSourceAdapter;
        SpinnerAdapterNewDD irriTypeAdapter;
        SpinnerAdapterNewDD soilTypeAdapter;
        irriTypeAdapter= new SpinnerAdapterNewDD(context, irrigationTypeList, resources.getString(R.string.select_irri_type_prompt));
        irriSourceAdapter= new SpinnerAdapterNewDD(context, irrigationSourceList, resources.getString(R.string.select_irri_source_prompt));
        soilTypeAdapter= new SpinnerAdapterNewDD(context, soilTypeList, resources.getString(R.string.select_soil_type_prompt));
        spinner_irri_source.setAdapter(irriSourceAdapter);
        spinner_irri_type.setAdapter(irriTypeAdapter);
        spinner_soil_type.setAdapter(soilTypeAdapter);

        if (isOnlineMode){
            onlineModeSetData();
        }
    }
    public void clickListeners() {

        final DatePickerDialog.OnDateSetListener datesowing = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarsowing.set(Calendar.YEAR, year);
                myCalendarsowing.set(Calendar.MONTH, monthOfYear);
                myCalendarsowing.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updatesowingDateLabel();
            }
        };

        sowingDateet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(FarmDetailsUpdateActivity.this, datesowing, myCalendarsowing
                        .get(Calendar.YEAR), myCalendarsowing.get(Calendar.MONTH),
                        myCalendarsowing.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }
        });


        update_farm_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DDNew irrigationSource = (DDNew) spinner_irri_source.getSelectedItem();
                DDNew irrigationType = (DDNew) spinner_irri_type.getSelectedItem();
                DDNew soilType = (DDNew) spinner_soil_type.getSelectedItem();

                if (irrigationSource==null||irrigationSource.getParameters().equals(resources.getString(R.string.select_message_tag))) {
                    Snackbar snackbar = Snackbar.make(rel_lay_farm_det_update, resources.getString(R.string.select_irrigation_source_msg), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (irrigationType==null||irrigationType.getParameters().equals(resources.getString(R.string.select_message_tag))) {
                    Snackbar snackbar = Snackbar.make(rel_lay_farm_det_update, resources.getString(R.string.select_irrigation_type_msg), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (soilType==null||soilType.getParameters().equals(resources.getString(R.string.select_message_tag))) {
                    Snackbar snackbar = Snackbar
                            .make(rel_lay_farm_det_update, resources.getString(R.string.select_soil_type_msg), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }/* else if (TextUtils.isEmpty(previousCropEt.getText().toString())) {
                    previousCropEt.getParent().requestChildFocus(previousCropEt, previousCropEt);
                    Snackbar snackbar = Snackbar.make(rel_lay_farm_det_update, resources.getString(R.string.select_previous_crop_msg), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } */ else if (sowingDateet.getText().toString().trim().matches("")) {
                    Snackbar snackbar = Snackbar
                            .make(rel_lay_farm_det_update, resources.getString(R.string.select_sowing_date_msg), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    submitData();
                }
            }
        });
    }
    public void init() {
        sowingDateet = (TextView) findViewById(R.id.sowing_date_et);
        expFloweringDateet = (TextView) findViewById(R.id.exp_flowering_date_et);
        expHarvestingDateet = (TextView) findViewById(R.id.exp_harvesting_date_et);
        progressBar = findViewById(R.id.progressBar_cyclic);
        cropSpinnerPrevious=findViewById(R.id.cropSpinnerPrevious);
        spinner_irri_source = (SearchableSpinner) findViewById(R.id.spinner_irrigation_source);
        spinner_irri_type = (SearchableSpinner) findViewById(R.id.spinner_irrigation_type);
        //spinner_perv_crop = (Spinner) findViewById(R.id.spinner_previous_crop);
        previousCropEt = (AutoCompleteTextView) findViewById(R.id.previousCropEt);
        spinner_soil_type = (SearchableSpinner) findViewById(R.id.spinner_soil_type);

        update_farm_details = (TextView) findViewById(R.id.update_farm_details);
        rel_lay_farm_det_update = (RelativeLayout) findViewById(R.id.rel_lay_farm_det_update);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cropList);
//        previousCropEt.setAdapter(adapter);
//        previousCropEt.setThreshold(1);


        IrrigationSource source = new IrrigationSource();
        source.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        source.setIrrigationSourceId("0");
        source.setName(resources.getString(R.string.select_irri_source_prompt));
        IrrigationType type = new IrrigationType();
        type.setName(resources.getString(R.string.select_irri_type_prompt));
        type.setIrrigationTypeId("0");
        SoilType soilType = new SoilType();
        soilType.setName(resources.getString(R.string.select_soil_type_prompt));
        soilType.setSoilTypeId("0");
        Season season = new Season();
        season.setName(resources.getString(R.string.select_season_prompt));
        season.setSeasonId("0");
        seasonList.add(season);


    }
    private void getAllDataNew4(final String type) {
        ParamData paramData = new ParamData();
        paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        paramData.setType(type);

        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        Log.e(TAG, "Sending New Param  AND type " + type + " " + new Gson().toJson(paramData));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getNewParameters(paramData).enqueue(new Callback<DDResponseNew>() {
            @Override
            public void onResponse(Call<DDResponseNew> call, Response<DDResponseNew> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    DDResponseNew response = response1.body();
                    Log.e(TAG, "Sending New Param Response " + type + "  " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, response.getMsg());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        if (response != null && response.getData() != null && response.getData().size() > 0) {
                            DropDownCacheManager2.getInstance(context).deleteViaType(type);
                            DropDownT2 material = new DropDownT2(type, new Gson().toJson(response));
                            DropDownCacheManager2.getInstance(context).addChemicalUnit(material);
                            setPreviousCropSpinner(response.getData());
                        }
                    }
                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
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
            public void onFailure(Call<DDResponseNew> call, Throwable t) {
                Log.e("OfflineModeAct", "Failure getOfflineData " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                internetFlow(isLoaded[0]);
            }
        }, AppConstants.DELAY);

    }
    String previousCrop=null;
    private void setPreviousCropSpinner(List<DDNew> cropList){
        SpinnerAdapterNewDD cropSpinnerAdapterPrevious = new SpinnerAdapterNewDD(context, cropList, resources.getString(R.string.select_crop_previous_rompt));
        cropSpinnerPrevious.setAdapter(cropSpinnerAdapterPrevious);
        cropSpinnerPrevious.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
//                Toast.makeText(context, ""+i+" "+new Gson().toJson(cropSpinnerAdapter.getItem(i)), Toast.LENGTH_SHORT).show();
                if (i < 0) {
                    try {
//                        farmList.get(holder.ref).setPreviouscrop(null);
                        previousCrop=null;
                    } catch (Exception e) {
                    }
                } else {
                    try {
//                        farmList.get(holder.ref).setPreviouscrop(cropSpinnerAdapterPrevious.getItem(i).getParameters());
                        previousCrop =cropSpinnerAdapterPrevious.getItem(i).getParameters();
                    } catch (Exception e) {
                    }
                }
            }
            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void onlineModeSetData() {
        FetchFarmResult fetchFarmResult;
        fetchFarmResult = DataHandler.newInstance().getFetchFarmResult();
        Log.e(TAG, "FetchFarmResult " + new Gson().toJson(fetchFarmResult));
        if (fetchFarmResult!=null) {
            if (fetchFarmResult.getIrrigationSource() != null) {
                for (int i = 0; i < irrigationSourceList.size(); i++) {
                    if (irrigationSourceList.get(i).getParameters().trim().equals(fetchFarmResult.getIrrigationSource().trim())) {
                        spinner_irri_source.setSelectedItem(irrigationSourceList.get(i));
                        Log.e(TAG, "sourcePosition " + i);
                        break;
                    }
                }
            }

            if (fetchFarmResult.getSoilType() != null) {
                for (int i = 0; i < soilTypeList.size(); i++) {
                    if (soilTypeList.get(i).getParameters().trim().equals(fetchFarmResult.getSoilType().trim())) {
                        spinner_soil_type.setSelectedItem(soilTypeList.get(i));
                        Log.e(TAG, "soilTypePosition " + i);
                        break;
                    }
                }
            }

            if (fetchFarmResult.getIrrigationType() != null) {
                for (int i = 0; i < irrigationTypeList.size(); i++) {
                    if (irrigationTypeList.get(i).getParameters().trim().equals(fetchFarmResult.getIrrigationType().trim())) {
                        spinner_irri_type.setSelectedItem(irrigationTypeList.get(i));
                        Log.e(TAG, "typePosition " + i);
                        break;
                    }
                }
            }
            if (fetchFarmResult.getPreviousCrop() != null) {
                for (int i = 0; i < cropList.size(); i++) {
                    if (cropList.get(i).getParameters().trim().equals(fetchFarmResult.getPreviousCrop().trim())) {
                        cropSpinnerPrevious.setSelectedItem(cropList.get(i));
                        Log.e(TAG, "typePosition " + i);
                        break;
                    }
                }
            }


            if (fetchFarmResult.getPreviousCrop() != null && !fetchFarmResult.getPreviousCrop().trim().equals("0"))
                previousCropEt.setText(fetchFarmResult.getPreviousCrop());

            if (fetchFarmResult != null && fetchFarmResult.getSowingDate() != null && !fetchFarmResult.getSowingDate().contains("1900")) {
                try {
                    sowingDateet.setText(AppConstants.getShowableDate(fetchFarmResult.getSowingDate(),context));
                } catch (Exception e) {
                    Log.e("FarmDetailUpdate", e.toString());
                }
            }

            if (fetchFarmResult != null && fetchFarmResult.getExpFloweringDate() != null && !fetchFarmResult.getExpFloweringDate().contains("1900")) {
                try {
                    DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = srcDf.parse(fetchFarmResult.getExpFloweringDate());
                    Log.e("Adapter", date.toString());
                    DateFormat destDf = new SimpleDateFormat("dd/MM/yyyy");

                    expFloweringDateet.setText(destDf.format(date));
                } catch (ParseException e) {
                    Log.e("FarmDetailUpdate", e.toString());
                }

            }

            if (fetchFarmResult != null && fetchFarmResult.getExpHarvestDate() != null && !fetchFarmResult.getExpHarvestDate().contains("1900")) {
                try {
                    DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = srcDf.parse(fetchFarmResult.getExpHarvestDate());
                    Log.e("Adapter", date.toString());
                    DateFormat destDf = new SimpleDateFormat("dd/MM/yyyy");
                    expHarvestingDateet.setText(destDf.format(date));
                } catch (ParseException e) {
                    Log.e("FarmDetailUpdate", e.toString());
                }

            }
        }
    }

    private void offlineModeSetData(Farm farm) {

        Farm fetchFarmResult;

        fetchFarmResult = farm;

        if (fetchFarmResult.getIrrigationSource() != null) {
            for (int i = 0; i < irrigationSourceList.size(); i++) {
                if (irrigationSourceList.get(i).getParameters().equals(fetchFarmResult.getIrrigationSource().trim())) {
                    spinner_irri_source.setSelectedItem(i);
                    Log.e(TAG, "sourcePosition " + i);
                    break;
                }
            }
        }

        if (fetchFarmResult.getSoilType() != null) {
            for (int i = 0; i < soilTypeList.size(); i++) {
                if (soilTypeList.get(i).getParameters().equals(fetchFarmResult.getSoilType().trim())) {
                    spinner_soil_type.setSelectedItem(i);
                    Log.e(TAG, "soilTypePosition " + i);
                    break;
                }
            }
        }

        if (fetchFarmResult.getIrrigationType() != null) {
            for (int i = 0; i < irrigationTypeList.size(); i++) {
                if (irrigationTypeList.get(i).getParameters().equals(fetchFarmResult.getIrrigationType().trim())) {
                    spinner_irri_type.setSelectedItem(i);
                    Log.e(TAG, "typePosition " + i);
                    break;
                }
            }
        }

        if (fetchFarmResult.getPreviousCrop() != null && !fetchFarmResult.getPreviousCrop().trim().equals("0"))
            previousCropEt.setText(fetchFarmResult.getPreviousCrop());

        if (fetchFarmResult != null && fetchFarmResult.getSowingDate() != null && !fetchFarmResult.getSowingDate().contains("1900")) {
            try {
                DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = srcDf.parse(fetchFarmResult.getSowingDate());
                Log.e("Adapter", date.toString());
                DateFormat destDf = new SimpleDateFormat("dd/MM/yyyy");

                sowingDateet.setText(destDf.format(date));
            } catch (ParseException e) {
                Log.e("FarmDetailUpdate", e.toString());
            }
        }

        if (fetchFarmResult != null && fetchFarmResult.getExpFloweringDate() != null && !fetchFarmResult.getExpFloweringDate().contains("1900")) {
            try {
                DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = srcDf.parse(fetchFarmResult.getExpFloweringDate());
                Log.e("Adapter", date.toString());
                DateFormat destDf = new SimpleDateFormat("dd/MM/yyyy");

                expFloweringDateet.setText(destDf.format(date));
            } catch (ParseException e) {
                Log.e("FarmDetailUpdate", e.toString());
            }

        }

        if (fetchFarmResult != null && fetchFarmResult.getExpHarvestDate() != null && !fetchFarmResult.getExpHarvestDate().contains("1900")) {
            try {
                DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = srcDf.parse(fetchFarmResult.getExpHarvestDate());
                Log.e("Adapter", date.toString());
                DateFormat destDf = new SimpleDateFormat("dd/MM/yyyy");

                expHarvestingDateet.setText(destDf.format(date));
            } catch (ParseException e) {
                Log.e("FarmDetailUpdate", e.toString());
            }

        }
    }

    private void updatesowingDateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sowingDateet.setText(sdf.format(myCalendarsowing.getTime()));


        myCalendarflowering = myCalendarsowing;
        myCalendarflowering.add(Calendar.DATE, 45);
        expFloweringDateet.setText(sdf.format(myCalendarflowering.getTime()));

        myCalendarharvesting = myCalendarsowing;
        myCalendarharvesting.add(Calendar.DATE, 45);
        expHarvestingDateet.setText(sdf.format(myCalendarharvesting.getTime()));

        myCalendarsowing.add(Calendar.DATE, -90);
    }


    protected void submitData() {

        String submit_format = "yyyy-MM-dd"; //In which you need put here
        String str_flowering_date = null;
        String str_sowing_date = null;
        String str_harvesting_date = null;
        SimpleDateFormat submit_sdf = new SimpleDateFormat(submit_format, Locale.US);


        Date sowing_date_final = null, flowering_date_final = null, harvesting_date_final = null;
        try {
            sowing_date_final = new SimpleDateFormat("dd/MM/yyyy").parse(sowingDateet.getText().toString().trim());
            str_sowing_date = submit_sdf.format(sowing_date_final);

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            flowering_date_final = new SimpleDateFormat("dd/MM/yyyy").parse(expFloweringDateet.getText().toString().trim());
            str_flowering_date = submit_sdf.format(flowering_date_final);

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            harvesting_date_final = new SimpleDateFormat("dd/MM/yyyy").parse(expHarvestingDateet.getText().toString().trim());
            str_harvesting_date = submit_sdf.format(harvesting_date_final);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        DDNew irrigationSource = (DDNew) spinner_irri_source.getSelectedItem();
        DDNew irrigationType = (DDNew) spinner_irri_type.getSelectedItem();
        DDNew soilType = (DDNew) spinner_soil_type.getSelectedItem();

        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            try {
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
                FarmDetailsUpdateSendData farmDetailsUpdateSendData = new FarmDetailsUpdateSendData();
                farmDetailsUpdateSendData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                farmDetailsUpdateSendData.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
//                farmDetailsUpdateSendData.setExp_flowering_date(str_flowering_date);
                farmDetailsUpdateSendData.setIrrigation_source(irrigationSource.getId());
                farmDetailsUpdateSendData.setIrrigation_type(irrigationType.getId());
                farmDetailsUpdateSendData.setSoil_type(soilType.getId());
                farmDetailsUpdateSendData.setPrevious_crop(previousCrop);
                farmDetailsUpdateSendData.setSowing_date(str_sowing_date);
//                farmDetailsUpdateSendData.setExp_harvest_date(str_harvesting_date);
                farmDetailsUpdateSendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                farmDetailsUpdateSendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                Log.e(TAG, "Sending Data " + new Gson().toJson(farmDetailsUpdateSendData));
                Call<StatusMsgModel> statusMsgModelCall = apiService.getMsgStatusForFarmDetailsUpdate(farmDetailsUpdateSendData);
                final boolean[] isLoaded = {false};
                long currMillis=AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<StatusMsgModel>() {

                    @Override
                    public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                        isLoaded[0] =true;
                        String error="";
                        Log.e(TAG, "Update Res code " + response.code());
                        if (response.isSuccessful()) {
                            Log.e(TAG, "Update Res " + new Gson().toJson(response.body()));
                            StatusMsgModel statusMsgModel = response.body();

                            if (statusMsgModel.getStatus() == 1) {

                                //GetFarmCoordinates getFarmCoordinates=new GetFarmCoordinates();
                                //getFarmCoordinates.execute();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Toast.makeText(context, resources.getString(R.string.farm_details_updated_success), Toast.LENGTH_LONG).show();
                                        Toasty.success(context, resources.getString(R.string.farm_details_updated_success), Toast.LENGTH_LONG, true).show();

                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });


                                Intent intent = getIntent();
                                setResult(RESULT_OK, intent);
                                finish();
                                //Toast.makeText(context, statusMsgModel.getMsg(), Toast.LENGTH_SHORT).show();
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity.this, resources.getString(R.string.authorization_expired));
                            } else if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity.this, resources.getString(R.string.multiple_login_msg));
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        //Toast.makeText(context, "Response Error Try again latter", Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(context, "Oops something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                                        viewFailDialog.showDialog(FarmDetailsUpdateActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.farm_detail_update_fail_msg));
                                    }
                                });

                            }

                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    viewFailDialog.showDialog(FarmDetailsUpdateActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.farm_detail_update_fail_msg));
                                }
                            });
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    viewFailDialog.showDialog(FarmDetailsUpdateActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.farm_detail_update_fail_msg));
                                }
                            });
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        viewFailDialog.showDialog(FarmDetailsUpdateActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.farm_detail_update_fail_msg));
                                    }
                                });
                                error=response.errorBody().string().toString();

                                Log.e(TAG,"Update Error "+ response.errorBody().string().toString());
                                //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        long newMillis=AppConstants.getCurrentMills();
                        long diff=newMillis-currMillis;
                        if(diff>=DELAY_API||(diff<DELAY_API&&error!=null&&!TextUtils.isEmpty(error))||response.code()==500){
                            notifyApiDelay(FarmDetailsUpdateActivity.this,response.raw().request().url().toString(),
                                    ""+diff,internetSPeed,error,response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                        isLoaded[0] =true;
                        Log.e(TAG,"Update Fail "+t.toString());
                        long newMillis=AppConstants.getCurrentMills();
                        long diff=newMillis-currMillis;
                        notifyApiDelay(FarmDetailsUpdateActivity.this,call.request().url().toString(),
                                ""+diff,internetSPeed,t.toString(),0);
                        progressBar.setVisibility(View.INVISIBLE);
                        viewFailDialog.showDialog(FarmDetailsUpdateActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.farm_detail_update_fail_msg));
                    }

                });

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        internetFlow(isLoaded[0]);
                    }
                }, AppConstants.DELAY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            farm.setSowingDate(str_sowing_date);
//            farm.setExpFloweringDate(str_flowering_date);
//            farm.setExpHarvestDate(str_harvesting_date);
            farm.setPreviousCrop(previousCrop);
            farm.setIrrigationSource(irrigationSource.getParameters());
            farm.setIrriSourceId(irrigationSource.getId());
            farm.setIrrigationType(irrigationType.getParameters());
            farm.setIrriTypeId(irrigationType.getId());
            farm.setSoilType(soilType.getParameters());
            farm.setSoilTypeId(soilType.getId());
            farm.setIsUpdated("Y");
            farm.setIsUploaded("N");
            FarmCacheManager.getInstance(context).updateFarm(farm);
            finish();
        }


    }
    private void internetFlow(boolean isLoaded){
        try {

            if (!isLoaded){
                if (!ConnectivityUtils.isConnected(context)){
                    AppConstants.failToast(context,getResources().getString(R.string.check_internet_connection_msg));
                }else {
                    ConnectivityUtils.checkSpeedWithColor(FarmDetailsUpdateActivity.this, new ConnectivityUtils.ColorListener() {
                        @Override
                        public void onColorChanged(int color,String speed) {
                            if (color==android.R.color.holo_green_dark){
                                connectivityLine .setVisibility(View.GONE);
//                                    connectivityLine.setBackgroundColor(getColor(R.color.blue));
                            }else {
                                connectivityLine .setVisibility(View.VISIBLE);

                                connectivityLine.setBackgroundColor(resources.getColor(color));
                            }
                            internetSPeed=speed;
                        }
                    },20);
                }
            }



        }catch (Exception e){

        }

    }

    String internetSPeed="";

    @Override
    protected void onStop() {
        super.onStop();
        viewFailDialog.cancelDialog();
    }

    @Override
    public void onChemicalUnitLoaded(List<DropDownT> chemicalUnitList) {


    }

    @Override
    public void onFarmLoader(Farm farm) {
        this.farm = farm;
        offlineModeSetData(farm);
    }
}
