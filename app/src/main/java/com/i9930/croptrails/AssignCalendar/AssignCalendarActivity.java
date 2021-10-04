package com.i9930.croptrails.AssignCalendar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.AddFarm.Model.FarmCrop;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.AssignCalendar.Model.CalendarDatum;
import com.i9930.croptrails.CommonAdapters.CropSpinner.CropSpinnerAdapter;
import com.i9930.croptrails.CommonAdapters.SeasonSpinner.SeasonSpinnerAdapter;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.Season;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.SpinnerResponse;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class AssignCalendarActivity extends AppCompatActivity {

    String TAG = "AssignCalendarActivity";
    AssignCalendarActivity context;
    @BindView(R.id.etCycleName)
    EditText etCycleName;
    @BindView(R.id.seasonSpinner)
    Spinner seasonSpinner;
    @BindView(R.id.cropSpinner)
    Spinner cropSpinner;
    @BindView(R.id.etSowingDate)
    EditText etSowingDate;
    @BindView(R.id.progressBar)
    GifImageView progressBar;
    Toolbar mActionBarToolbar;
    private List<Season> seasonDDList = new ArrayList<>();
    private List<FarmCrop> farmCropList = new ArrayList<>();
    @BindView(R.id.viewLayout)
    LinearLayout viewLayout;
    Calendar calendar = Calendar.getInstance();
    Calendar myCalendarflowering = Calendar.getInstance();
    Calendar myCalendarharvesting = Calendar.getInstance();
    ViewFailDialog viewFailDialog = new ViewFailDialog();
    @BindView(R.id.submitBtn)
    Button submitBtn;
    CalendarDatum calendarDatum;
    String expFlowDate;
    String expHarvDate;
    @BindView(R.id.connectivityLine)
    View connectivityLine;
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
        setContentView(R.layout.activity_assign_calendar);

        context = this;
        ButterKnife.bind(this);
        loadAds();
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(getResources().getString(R.string.assign_crop_cycle));
        calendarDatum = getIntent().getParcelableExtra("calendar");
        if (calendarDatum != null) {
            etCycleName.setText(calendarDatum.getCycleName());
        }
        Log.e(TAG, "Data " + new Gson().toJson(calendarDatum));
        onClicks();
        getSpinnerData();
    }
    private void onClicks() {
        etSowingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        seasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (TextUtils.isEmpty(etCycleName.getText().toString())) {

                } else*/ if (seasonSpinner.getSelectedItemPosition() == 0) {
                    Toasty.error(context, getResources().getString(R.string.select_season_msg), Toast.LENGTH_LONG).show();
                } else if (cropSpinner.getSelectedItemPosition() == 0) {
                    Toasty.error(context, getResources().getString(R.string.select_crop_for_farm_msg), Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(etSowingDate.getText().toString())) {
                    Toasty.error(context, getResources().getString(R.string.select_sowing_date_msg), Toast.LENGTH_LONG).show();
                } else {
                    showAlert();
                }
            }
        });

    }

    private void showDatePicker() {
        final DatePickerDialog.OnDateSetListener add_details_farm_flowering = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String submit_format = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat submit_sdf = new SimpleDateFormat(submit_format, Locale.US);
                String finalDate = submit_sdf.format(calendar.getTime());
                etSowingDate.setText(AppConstants.getShowableDate(finalDate,context));

                myCalendarflowering = calendar;
                myCalendarflowering.add(Calendar.DATE, 45);
                expFlowDate = submit_sdf.format(myCalendarflowering.getTime());

                myCalendarharvesting = calendar;
                myCalendarharvesting.add(Calendar.DATE, 45);
                expHarvDate = submit_sdf.format(myCalendarharvesting.getTime());
                calendar.add(Calendar.DATE, -90);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, add_details_farm_flowering, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        // datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.setTitle("Select date of sowing");
        datePickerDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void showAlert() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.confirmation))
                .setMessage(getResources().getString(R.string.sure_want_to_add_crop_cycle))
                .setPositiveButton(getResources().getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setFarmCropCycle();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();

    }

    private void getSpinnerData() {
        progressBar.setVisibility(View.VISIBLE);
        viewLayout.setVisibility(View.GONE);
        submitBtn.setVisibility(View.GONE);
        submitBtn.setVisibility(View.VISIBLE);
        seasonDDList.clear();
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        JsonObject jsonObject =new JsonObject();
        jsonObject.addProperty("comp_id",""+compId);
        jsonObject.addProperty("user_id",""+userId);
        jsonObject.addProperty("token",""+token);
        Call<SpinnerResponse> spinnerResponseCall = apiService.getspinnerData(jsonObject);
        Log.e("SpinnerResponse", "Data Sending  " + new Gson().toJson(jsonObject));
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();
        spinnerResponseCall.enqueue(new Callback<SpinnerResponse>() {
            @Override
            public void onResponse(Call<SpinnerResponse> call, Response<SpinnerResponse> response) {
                isLoaded[0] =true;
                String error="";                if (response.isSuccessful()) {
                    SpinnerResponse spinnerResponse = response.body();
                    Log.e("SpinnerResponse", "Data  " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context);
                    }  else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(AssignCalendarActivity.this, context.getResources().getString(R.string.unauthorized_access));
                    } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(AssignCalendarActivity.this, context.getResources().getString(R.string.authorization_expired));
                    }else {
                        //  Log.e("SR",spinnerResponse.getMsg());
                        progressBar.setVisibility(View.GONE);
                        viewLayout.setVisibility(View.VISIBLE);
                        submitBtn.setVisibility(View.VISIBLE);
                        if (spinnerResponse.getSeasonList() != null)
                            seasonDDList.addAll(spinnerResponse.getSeasonList());

                        if (spinnerResponse.getCropList() != null)
                            farmCropList.addAll(spinnerResponse.getCropList());

                        seasonDDList.add(0, new Season("0", getResources().getString(R.string.select_season_prompt)));
                        farmCropList.add(0, new FarmCrop("0", getResources().getString(R.string.select_crop_rompt)));
                        spinnerResponse.setSeasonList(seasonDDList);
                        CropSpinnerAdapter cropSpinnerAdapter = new CropSpinnerAdapter(context, R.layout.spinner_layout, farmCropList);
                        SeasonSpinnerAdapter adapter = new SeasonSpinnerAdapter(context, R.layout.spinner_layout, seasonDDList);
                        seasonSpinner.setAdapter(adapter);
                        cropSpinner.setAdapter(cropSpinnerAdapter);
                        progressBar.setVisibility(View.GONE);
                    }
                } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(AssignCalendarActivity.this, context.getResources().getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(AssignCalendarActivity.this, context.getResources().getString(R.string.authorization_expired));
                }else {
                    progressBar.setVisibility(View.GONE);
                    viewLayout.setVisibility(View.VISIBLE);
                    submitBtn.setVisibility(View.VISIBLE);
                    //progressDialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    try {
                        error=response.errorBody().string().toString();
                        Log.e("SpinnerResponse ", "err  " + response.errorBody().string().toString());
                        viewFailDialog.showDialogForFinish(context, getResources().getString(R.string.failed_to_load_data_msg));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis=AppConstants.getCurrentMills();
                long diff=newMillis-currMillis;
                if(diff>=DELAY_API||(diff<DELAY_API&&error!=null&&!TextUtils.isEmpty(error))||response.code()==500){
                    notifyApiDelay(AssignCalendarActivity.this,response.raw().request().url().toString(),
                            ""+diff,internetSPeed,error,response.code());
                }
            }


            @Override
            public void onFailure(Call<SpinnerResponse> call, Throwable t) {
                isLoaded[0] =true;
                long newMillis=AppConstants.getCurrentMills();
                long diff=newMillis-currMillis;
                notifyApiDelay(AssignCalendarActivity.this,call.request().url().toString(),
                        ""+diff,internetSPeed,t.toString(),0);
                Log.e("SpinnerResponse", t.toString());
                if (t instanceof SocketTimeoutException) {
                    getSpinnerData();
                } else {
                    // progressDialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    viewLayout.setVisibility(View.VISIBLE);
                    submitBtn.setVisibility(View.VISIBLE);
                    viewFailDialog.showDialogForFinish(context, getResources().getString(R.string.failed_to_load_data_msg));
                }

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
    private void internetFlow(boolean isLoaded){
        try {

            if (!isLoaded){
                if (!ConnectivityUtils.isConnected(context)){
                    AppConstants.failToast(context,getResources().getString(R.string.check_internet_connection_msg));
                }else {
                    ConnectivityUtils.checkSpeedWithColor(AssignCalendarActivity.this, new ConnectivityUtils.ColorListener() {
                        @Override
                        public void onColorChanged(int color,String speed) {
                            if (color==android.R.color.holo_green_dark){
                                connectivityLine .setVisibility(View.GONE);
//                                    connectivityLine.setBackgroundColor(getColor(R.color.blue));
                            }else {
                                connectivityLine .setVisibility(View.VISIBLE);

                                connectivityLine.setBackgroundColor(getColor(color));
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
    private void setFarmCropCycle() {
        submitBtn.setClickable(false);
        submitBtn.setEnabled(false);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String calendarId = calendarDatum.getCalendarId();
        String sowingDate = AppConstants.getUploadableDate(etSowingDate.getText().toString().trim(),context);
        FarmCrop farmCrop = farmCropList.get(cropSpinner.getSelectedItemPosition());
        String cropName = farmCrop.getCropId();
        Season season = seasonDDList.get(seasonSpinner.getSelectedItemPosition());
        String seasonId = season.getSeasonNum();
        String data="user_id:"+userId+" token:"+token+" farm_id:"+farmId+" calendar_id:"+calendarId+ " sowing_date:"+sowingDate+" exp_flowering_date:"+expFlowDate+" exp_harvest_date:"+expHarvDate+" season_id:"+seasonId+" crop_id:"+cropName;
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("user_id",""+userId);
        jsonObject.addProperty("token",""+token);
        jsonObject.addProperty("farm_id",""+farmId);
        jsonObject.addProperty("calendar_id",""+calendarId);
        jsonObject.addProperty("sowing_date",""+sowingDate);
//        jsonObject.addProperty("exp_flowering_date",""+expFlowDate);
//        jsonObject.addProperty("exp_harvest_date",""+expHarvDate);
//        jsonObject.addProperty("season_id",""+seasonId);
//        jsonObject.addProperty("crop_id",""+cropName);

        Log.e(TAG,"Data Sending "+new Gson().toJson(jsonObject));
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        //Shivangi told status dala to front me bhi change karna padega
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();
        apiInterface.assignCalendar(jsonObject).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                isLoaded[0] =true;
                String error="";
                if (response.isSuccessful()) {
                    /*if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context);
                        submitBtn.setClickable(true);
                        submitBtn.setEnabled(true);
                    } else if (response.body().getStatus() == 1) {
                        submitBtn.setClickable(true);
                        submitBtn.setEnabled(true);
                        Toasty.error(context, getResources().getString(R.string.data_submit_success_msg), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(context, LandingActivity.class));
                        finish();
                        finishAffinity();
                    } else {
                        submitBtn.setClickable(true);
                        submitBtn.setEnabled(true);
                        Toasty.error(context, getResources().getString(R.string.something_went_wrong_msg) + ", " + getResources().getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                    }*/

                    submitBtn.setClickable(true);
                    submitBtn.setEnabled(true);
                    Toasty.success(context, getResources().getString(R.string.data_submit_success_msg), Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();

                }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(AssignCalendarActivity.this, context.getResources().getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(AssignCalendarActivity.this, context.getResources().getString(R.string.authorization_expired));
                } else {
                    try {
                        error=response.errorBody().string().toString();
                        submitBtn.setClickable(true);
                        submitBtn.setEnabled(true);
                        Log.e(TAG, "AssignErr " + response.errorBody().string());
                        Toasty.error(context, getResources().getString(R.string.something_went_wrong_msg) + ", " + getResources().getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis=AppConstants.getCurrentMills();
                long diff=newMillis-currMillis;
                if(diff>=DELAY_API||(diff<DELAY_API&&error!=null&&!TextUtils.isEmpty(error))||response.code()==500){
                    notifyApiDelay(AssignCalendarActivity.this,call.request().url().toString(),
                            ""+diff,internetSPeed,error,response.code());
                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e(TAG, "AssignFail " + t.toString());
                isLoaded[0] =true;
                long newMillis=AppConstants.getCurrentMills();
                long diff=newMillis-currMillis;
                notifyApiDelay(AssignCalendarActivity.this,call.request().url().toString(),
                        ""+diff,internetSPeed,t.toString(),0);
                submitBtn.setClickable(true);
                submitBtn.setEnabled(true);
                Toasty.error(context, getResources().getString(R.string.something_went_wrong_msg) + ", " + getResources().getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
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
}
