package com.i9930.croptrails.SubmitHealthCard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.NonScrollListView;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitHealthCard.Adapters.CustomAdapter;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCardDDResponse;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCardData;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCardParams;
import com.i9930.croptrails.SubmitHealthCard.Model.SendHealthCardData;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DATE_FORMAT_SERVER;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class AddSoilHealthCardActivity extends AppCompatActivity {
    private static final String TAG = "AddSoilHealthCardAct";
    Context context;
    Toolbar mActionBarToolbar;
    @BindView(R.id.moreCardItems)
    ImageView addMoreParameters;
    @BindView(R.id.etHealthCardNo)
    EditText etHealthCardNo;
    @BindView(R.id.etFromDate)
    EditText etFromDate;
    @BindView(R.id.etToDate)
    EditText etToDate;
    @BindView(R.id.labNameEt)
    EditText labNameEt;
    List<HealthCardData> paramsList = new ArrayList<>();
    CustomAdapter customAdapter;
    @BindView(R.id.recyclerAddHealthCard)
    RecyclerView recyclerAddHealthCard;
    private boolean isActivityRunning = false;
    @BindView(R.id.submitCardDataTv)
    TextView submitCardDataTv;
    Resources resources;
    private boolean isValidData = true;
    ProgressDialog progressDialog;
    ViewFailDialog viewFailDialog;
    @BindView(R.id.sampleNumberEt)
    EditText sampleNumberEt;
    @BindView(R.id.sampleCollectedOnEt)
    EditText sampleCollectedOnEt;

    @BindView(R.id.listView)
    NonScrollListView listView;

    @BindView(R.id.connectivityLine)
    View connectivityLine;
    AdView mAdView;
    private void loadAds(){
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
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
//                Log.e(TAG,"Add Errro "+adError.toString());
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
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_soil_health_card);
        context = this;
        loadAds();
        resources = getResources();
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(resources.getString(R.string.please_wait_msg));
        viewFailDialog = new ViewFailDialog();
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        TextView tittle = findViewById(R.id.tittle);
        tittle.setText(resources.getString(R.string.add_health_card_title));
        clickEvents();
        getDropDownData();
        ViewCompat.setNestedScrollingEnabled(listView, false);

        /*FullScreenDialogHealthCard dialog = new FullScreenDialogHealthCard();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        dialog.show(ft, FullScreenDialogHealthCard.TAG);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActivityRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityRunning = false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    Calendar myCalendar = Calendar.getInstance();

    private void clickEvents() {

        submitCardDataTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etHealthCardNo.getText().toString())) {
                    etHealthCardNo.getParent().requestChildFocus(etHealthCardNo, etHealthCardNo);
                    Toasty.error(context, resources.getString(R.string.enter_health_card_number_msg), Toast.LENGTH_LONG).show();
                    etHealthCardNo.setError(resources.getString(R.string.required_label));
                    isValidData = false;
                } else if (TextUtils.isEmpty(sampleNumberEt.getText().toString())) {
                    sampleNumberEt.getParent().requestChildFocus(sampleNumberEt, sampleNumberEt);
                    Toasty.error(context, resources.getString(R.string.enter_health_card_sample_number_msg), Toast.LENGTH_LONG).show();
                    sampleNumberEt.setError(resources.getString(R.string.required_label));
                    isValidData = false;
                } else if (TextUtils.isEmpty(sampleCollectedOnEt.getText().toString())) {
                    sampleCollectedOnEt.getParent().requestChildFocus(sampleCollectedOnEt, sampleCollectedOnEt);
                    Toasty.error(context, resources.getString(R.string.enter_health_card_sample_collect_date_msg), Toast.LENGTH_LONG).show();
                    sampleCollectedOnEt.setError(resources.getString(R.string.required_label));
                    isValidData = false;
                } else if (TextUtils.isEmpty(labNameEt.getText().toString())) {
                    labNameEt.getParent().requestChildFocus(labNameEt, labNameEt);
                    Toasty.error(context, resources.getString(R.string.enter_laboratory_name_msg), Toast.LENGTH_LONG).show();
                    sampleCollectedOnEt.setError(resources.getString(R.string.required_label));
                    isValidData = false;
                } else if (TextUtils.isEmpty(etFromDate.getText().toString())) {
                    etFromDate.getParent().requestChildFocus(etFromDate, etFromDate);
                    Toasty.error(context, resources.getString(R.string.enter_health_card_validity_date_start_msg), Toast.LENGTH_LONG).show();
                    etFromDate.setError(resources.getString(R.string.required_label));
                    isValidData = false;
                } else if (TextUtils.isEmpty(etToDate.getText().toString())) {
                    etHealthCardNo.getParent().requestChildFocus(etHealthCardNo, etHealthCardNo);
                    Toasty.error(context, resources.getString(R.string.enter_health_card_validity_date_end_msg), Toast.LENGTH_LONG).show();
                    etToDate.setError(resources.getString(R.string.required_label));
                    isValidData = false;
                } else if (paramsList.size() < 1) {
                    Toasty.error(context, resources.getString(R.string.add_atleast_one_param_health_card), Toast.LENGTH_LONG).show();
                    isValidData = false;
                } else {
                    // Toasty.success(context,"Uploading", Toast.LENGTH_LONG).show();
                    isValidData = true;
                    submitCardDataTv.setClickable(false);
                    submitCardDataTv.setEnabled(false);


                    UploadCardData uploadCardData = new UploadCardData();
                    uploadCardData.execute();

                }
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = DATE_FORMAT_SERVER;
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                etFromDate.setText(AppConstants.getShowableDate(sdf.format(myCalendar.getTime()),context));
            }
        };

        final DatePickerDialog.OnDateSetListener dateCollectedOn = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar = Calendar.getInstance();
                myCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = DATE_FORMAT_SERVER;
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                sampleCollectedOnEt.setText(AppConstants.getShowableDate(sdf.format(myCalendar.getTime()),context));
            }
        };

        sampleCollectedOnEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,/*R.style.DatePickerDialogTheme,*/
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = DATE_FORMAT_SERVER;
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                sampleCollectedOnEt.setText(AppConstants.getShowableDate(sdf.format(myCalendar.getTime()),context));

                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();

            }
        });

        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddSoilHealthCardActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        final DatePickerDialog.OnDateSetListener dateTO = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = DATE_FORMAT_SERVER;
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                etToDate.setText(AppConstants.getShowableDate(sdf.format(myCalendar.getTime()),context));
            }
        };
        etToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar = Calendar.getInstance();
                // TODO Auto-generated method stub
                new DatePickerDialog(AddSoilHealthCardActivity.this, dateTO, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        addMoreParameters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Log.e(TAG, "Last Obj " + new Gson().toJson(paramsList.get(paramsList.size() - 1)));
                if (paramsList.get(paramsList.size() - 1).getSpinnerPosition() == 0) {
                    customAdapter.setErrorOnValidation(paramsList.size() - 1, "param");
                } else if (paramsList.get(paramsList.size() - 1).getValue() == null || TextUtils.isEmpty(paramsList.get(paramsList.size() - 1).getValue())) {
                    customAdapter.setErrorOnValidation(paramsList.size() - 1, "value");
                } else if (paramsList.get(paramsList.size() - 1).getUnit() == null || TextUtils.isEmpty(paramsList.get(paramsList.size() - 1).getUnit())) {
                    customAdapter.setErrorOnValidation(paramsList.size() - 1, "unit");
                } else {
                    paramsList.add(new HealthCardData());
                    customAdapter.notifyDataSetChanged();
                }

            }
        });
    }
    String internetSPeed="";
    private void internetFlow(boolean isLoaded){
        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLoaded){
                        if (!ConnectivityUtils.isConnected(context)){
                            AppConstants.failToast(context,resources.getString(R.string.check_internet_connection_msg));
                        }else {
                            ConnectivityUtils.checkSpeedWithColor(AddSoilHealthCardActivity.this, new ConnectivityUtils.ColorListener() {
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
                }
            }, AppConstants.DELAY);
        }catch (Exception e){

        }

    }

    private void getDropDownData() {
        progressDialog.show();
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();
        apiInterface.getSoilHealthCardDropDown(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID), SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID)).enqueue(new Callback<HealthCardDDResponse>() {
            @Override
            public void onResponse(Call<HealthCardDDResponse> call, Response<HealthCardDDResponse> response) {
                String error=null;
                isLoaded[0]=true;
                if (response.isSuccessful()) {
                    Log.e(TAG, "DD Res " + new Gson().toJson(response.body()));
                    progressDialog.cancel();
                    if (response.body().getStatus() == 10) {
                        viewFailDialog.showSessionExpireDialog(AddSoilHealthCardActivity.this, resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == 1) {
                        progressDialog.cancel();
                        List<HealthCardParams> paramsListt = new ArrayList<>();
                        HealthCardParams params = new HealthCardParams();
                        params.setParameter(resources.getString(R.string.select_parameter_prompt));
                        params.setParameterId("0");
                        paramsListt.add(params);
                        if (response.body().getHealthCardParamsList() != null) {
                            paramsListt.addAll(response.body().getHealthCardParamsList());
                        }
                        paramsList.add(new HealthCardData());
                        customAdapter=new CustomAdapter(context, paramsList, new CustomAdapter.OnDataSetChangeListener() {
                            @Override
                            public void onRemoveClick(int position) {
                                paramsList.remove(position);
                                customAdapter.notifyDataSetChanged();
                            }
                        },paramsListt);

                        listView.setAdapter(customAdapter);
                        /*adapter = new HealthCardParamAdapter(context, paramsList, new HealthCardParamAdapter.OnDataSetChangeListener() {
                            @Override
                            public void onRemoveClick(int position) {
                                paramsList.remove(position);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onParamIdChanged(int position, String id, int spinnerPos) {
                                paramsList.get(position).setSpinnerPosition(spinnerPos);
                                paramsList.get(position).setParameterId(id);
                            }

                            @Override
                            public void onValueChanged(int position, String value) {
                                paramsList.get(position).setValue(value);
                                Log.e(TAG,"Value at "+position+" is "+value);
                            }

                            @Override
                            public void onUnitChanged(int position, String unit) {
                                paramsList.get(position).setUnit(unit);
                                Log.e(TAG,"Unit at "+position+" is "+unit);
                            }
                        }, paramsListt);
                        recyclerAddHealthCard.setHasFixedSize(true);
                        recyclerAddHealthCard.setNestedScrollingEnabled(false);
                        recyclerAddHealthCard.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
                        recyclerAddHealthCard.setAdapter(adapter);*/
                    } else {
                        progressDialog.cancel();
//                        viewFailDialog.showDialogForFinish(AddSoilHealthCardActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.no_parameter_values_found_for_adding_soil_health_card));

                    }
                } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(AddSoilHealthCardActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(AddSoilHealthCardActivity.this, resources.getString(R.string.authorization_expired));
                }else {
                    try {
                        error=response.errorBody().string();
                        progressDialog.cancel();
                        viewFailDialog.showDialogForFinish(AddSoilHealthCardActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.something_went_wrong_msg) + ", " +
                                resources.getString(R.string.please_try_again_later_msg));
                        Log.e(TAG, "DD Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                long newMillis=AppConstants.getCurrentMills();
                long diff=newMillis-currMillis;
                if(diff>=DELAY_API||(diff<DELAY_API&&error!=null&&!TextUtils.isEmpty(error))||response.code()==500) {
                    notifyApiDelay(AddSoilHealthCardActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<HealthCardDDResponse> call, Throwable t) {
                Log.e(TAG, "DD failure " + t.toString());
                long newMillis=AppConstants.getCurrentMills();
                isLoaded[0]=true;
                long diff=newMillis-currMillis;
                notifyApiDelay(AddSoilHealthCardActivity.this,call.request().url().toString(),
                        ""+diff,internetSPeed,t.toString(),0);
                progressDialog.cancel();
                viewFailDialog.showDialogForFinish(AddSoilHealthCardActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.something_went_wrong_msg) + ", " +
                        resources.getString(R.string.please_try_again_later_msg));
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

    private class UploadCardData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < paramsList.size(); i++) {
                if (paramsList.get(i).getSpinnerPosition() == 0) {
                    int finalI = i;
                    isValidData = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customAdapter.setErrorOnValidation(finalI, "param");
                            if (progressDialog.isShowing())
                                progressDialog.cancel();
                        }
                    });
                    break;
                } else if (paramsList.get(i).getValue() == null || TextUtils.isEmpty(paramsList.get(i).getValue())) {
                    int finalI1 = i;
                    isValidData = false;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customAdapter.setErrorOnValidation(finalI1, "value");
                            if (progressDialog.isShowing())
                                progressDialog.cancel();
                        }
                    });
                    break;
                } else if (paramsList.get(i).getUnit() == null || TextUtils.isEmpty(paramsList.get(i).getUnit())) {
                    int finalI2 = i;
                    isValidData = false;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customAdapter.setErrorOnValidation(finalI2, "unit");
                            if (progressDialog.isShowing())
                                progressDialog.cancel();
                        }
                    });
                    break;
                }
            }
            String[] value = new String[paramsList.size()];
            String[] parameter_id = new String[paramsList.size()];
            String[] unit = new String[paramsList.size()];
            for (int i = 0; i < paramsList.size(); i++) {
                value[i] = paramsList.get(i).getValue();
                parameter_id[i] = paramsList.get(i).getParameterId();
                unit[i] = paramsList.get(i).getUnit();
            }
            SendHealthCardData data = new SendHealthCardData();
            data.setHealthCardNo(etHealthCardNo.getText().toString());
            data.setValidDateFrom(AppConstants.getUploadableDate(etFromDate.getText().toString(),context));
            data.setValidDateTo(AppConstants.getUploadableDate(etToDate.getText().toString(),context));
            data.setHealthCardParamsList(paramsList);
            data.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
            data.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            data.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            data.setParameter_id(parameter_id);
            data.setValue(value);
            data.setUnit(unit);
            data.setAddedBy(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            data.setCollectedOnDate(AppConstants.getUploadableDate(sampleCollectedOnEt.getText().toString(),context));
            data.setSampleNumber(sampleNumberEt.getText().toString());
            data.setLabName(labNameEt.getText().toString());
            Log.e(TAG, "Data " + new Gson().toJson(data));
            Log.e(TAG, "Data " + new Gson().toJson(paramsList));
            String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            final boolean[] isLoaded = {false};
            long currMillis=AppConstants.getCurrentMills();
            apiInterface.uploadHealthCardData(data).enqueue(new Callback<StatusMsgModel>() {
                @Override
                public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                    String error=null;
                    isLoaded[0]=true;
                    if (response.isSuccessful()){
                        Log.e(TAG,"Resp "+new Gson().toJson(response.body()));
                        if (response.body().getStatus()==10){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    submitCardDataTv.setClickable(true);
                                    submitCardDataTv.setEnabled(true);
                                    viewFailDialog.showSessionExpireDialog(AddSoilHealthCardActivity.this,resources.getString(R.string.multiple_login_msg));

                                }
                            });
                        }else if (response.body().getStatus()==1){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    submitCardDataTv.setClickable(true);
                                    submitCardDataTv.setEnabled(true);
                                    Toasty.success(context,resources.getString(R.string.health_card_added_success_msg),Toast.LENGTH_LONG).show();
                                }
                            });
                            Intent intent=getIntent();
                            setResult(RESULT_OK,intent);
                            finish();

                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    submitCardDataTv.setClickable(true);
                                    submitCardDataTv.setEnabled(true);
                                    Toasty.error(context,resources.getString(R.string.something_went_wrong_msg),Toast.LENGTH_LONG).show();

                                }
                            });

                        }
                    }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.cancel();
                            }
                        });
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(AddSoilHealthCardActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.cancel();
                            }
                        });
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(AddSoilHealthCardActivity.this, resources.getString(R.string.authorization_expired));
                    }else {
                        try {
                            error=response.errorBody().string();
                            Log.e(TAG,"Err "+error+", code:"+response.code());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.cancel();
                                    submitCardDataTv.setClickable(true);
                                    submitCardDataTv.setEnabled(true);
                                    Toasty.error(context,resources.getString(R.string.something_went_wrong_msg),Toast.LENGTH_LONG).show();
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    long newMillis=AppConstants.getCurrentMills();
                    long diff=newMillis-currMillis;
                    if(diff>=DELAY_API||(diff<DELAY_API&&error!=null&&!TextUtils.isEmpty(error))||response.code()==500) {
                        notifyApiDelay(AddSoilHealthCardActivity.this, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }
                }

                @Override
                public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                    Log.e(TAG,"Failure "+t.toString());
                    isLoaded[0]=true;
                    long newMillis=AppConstants.getCurrentMills();
                    long diff=newMillis-currMillis;
                    notifyApiDelay(AddSoilHealthCardActivity.this,call.request().url().toString(),
                            ""+diff,internetSPeed,t.toString(),0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            submitCardDataTv.setClickable(true);
                            submitCardDataTv.setEnabled(true);
                            Toasty.error(context,resources.getString(R.string.something_went_wrong_msg),Toast.LENGTH_LONG).show();
                            // viewFailDialog.showDialog(context,resources.getString(R.string.opps_message),resources.getString(R.string.something_went_wrong_msg));
                        }
                    });
                }
            });

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    internetFlow(isLoaded[0]);
                }
            }, AppConstants.DELAY);


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.cancel();
            //Toast.makeText(context, "Finished", Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            Log.e(TAG, "Progress Update " + String.valueOf(values));
        }
    }

}
