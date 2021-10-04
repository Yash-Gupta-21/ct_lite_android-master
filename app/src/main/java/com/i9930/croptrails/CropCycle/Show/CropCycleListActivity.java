package com.i9930.croptrails.CropCycle.Show;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.AssignCalendar.Adapter.CalendarAdapter;
import com.i9930.croptrails.AssignCalendar.Model.CalendarDatum;
import com.i9930.croptrails.AssignCalendar.Model.CalendarFetchResponse;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.CropCycle.Create.CreateCropCycleActivity;
import com.i9930.croptrails.Maps.CheckArea.CheckAreaMapActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

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

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class CropCycleListActivity extends AppCompatActivity {
    String TAG = "CropCycleListActivity";
    CropCycleListActivity context;
    @BindView(R.id.calendarRecycler)
    RecyclerView calendarRecycler;
    List<CalendarDatum> calendarDatumList = new ArrayList<>();
    Toolbar mActionBarToolbar;
    Calendar calendar = Calendar.getInstance();
    ViewFailDialog viewFailDialog = new ViewFailDialog();
    private static final int ASSIGN_CALENDAR_REQUEST_CODE = 101;
    @BindView(R.id.connectivityLine)
    View connectivityLine;
    AdView mAdView;

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

    @BindView(R.id.addMore)
    FloatingActionButton addMore;
    @BindView(R.id.noDataMsgTv)
    TextView noDataMsgTv;
    @BindView(R.id.noDataAvailableLayout)
    RelativeLayout noDataAvailableLayout;
    private final int REQ_CODE = 100;
    boolean withListener = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_cycle_list);

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
        title.setText(getResources().getString(R.string.crop_cycle));
        Intent intent = getIntent();
        withListener = intent.getBooleanExtra("withListener", false);
        prepareData();
        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CreateCropCycleActivity.class);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, REQ_CODE, options.toBundle());
                } else {
                    startActivityForResult(intent, REQ_CODE);
                }
            }
        });

    }

    private void prepareData() {
//        calendarDatumList.add(new CalendarDatum("Cycle1","Maize","May","Up","Black"));
//        calendarDatumList.add(new CalendarDatum("Wheat Master","Wheat","Rabi","India","Black"));
//        calendarDatumList.add(new CalendarDatum("Wheat Calendar","Wheat","June-Oct","MP","Black"));
//        calendarDatumList.add(new CalendarDatum("Cycle1","Maize","May","Up","Black"));
//        calendarDatumList.add(new CalendarDatum("Cycle1","Maize","May","Up","Black"));

        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getCropCycles(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID)).enqueue(new Callback<CalendarFetchResponse>() {
            @Override
            public void onResponse(Call<CalendarFetchResponse> call, Response<CalendarFetchResponse> response) {
                String error = null;
                isLoaded[0] = true;
                if (response.isSuccessful()) {
                    Log.e(TAG, "Resp1 " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context);
                    } else if (response.body().getStatus() == 1 && response.body().getCalendarDatumList() != null) {
                        calendarDatumList = new ArrayList<>();
                        calendarDatumList.addAll(response.body().getCalendarDatumList());
                        calendarRecycler.setHasFixedSize(true);
                        calendarRecycler.setLayoutManager(new LinearLayoutManager(context));
                        CalendarAdapter adapter;
                        if (!withListener)
                            adapter = new CalendarAdapter(context, calendarDatumList, null);
                        else
                            adapter = new CalendarAdapter(context, calendarDatumList, new CalendarAdapter.OnAssignClicked() {
                                @Override
                                public void onClick(CalendarDatum calendarDatum) {
                                    Log.e(TAG, "Selected " + new Gson().toJson(calendarDatum));
                                    showDialog(calendarDatum);
                                }
                            });
                        calendarRecycler.setAdapter(adapter);

                    } else {
                        noDataAvailableLayout.setVisibility(View.VISIBLE);
                        noDataMsgTv.setText("Crop cycle not added yet!");
//                        viewFailDialog.showDialogForFinish(context, getResources().getString(R.string.failed_to_load_data_msg));
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(CropCycleListActivity.this, context.getResources().getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(CropCycleListActivity.this, context.getResources().getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string() + " code: " + response.code();
                        viewFailDialog.showDialogForFinish(context, getResources().getString(R.string.failed_to_load_data_msg));
                        Log.e(TAG, "Resp1 err" + error);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error))) {
                    notifyApiDelay(CropCycleListActivity.this, call.request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<CalendarFetchResponse> call, Throwable t) {
                isLoaded[0] = true;
                Log.e(TAG, "Resp1 fail " + t.toString());
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(CropCycleListActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                viewFailDialog.showDialogForFinish(context, getResources().getString(R.string.failed_to_load_data_msg));
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

    private void internetFlow(boolean isLoaded) {
        try {

            if (!isLoaded) {
                if (!ConnectivityUtils.isConnected(context)) {
                    AppConstants.failToast(context, getResources().getString(R.string.check_internet_connection_msg));
                } else {
                    ConnectivityUtils.checkSpeedWithColor(CropCycleListActivity.this, new ConnectivityUtils.ColorListener() {
                        @Override
                        public void onColorChanged(int color, String speed) {
                            if (color == android.R.color.holo_green_dark) {
                                connectivityLine.setVisibility(View.GONE);
//                                    connectivityLine.setBackgroundColor(getColor(R.color.blue));
                            } else {
                                connectivityLine.setVisibility(View.VISIBLE);

                                connectivityLine.setBackgroundColor(getResources().getColor(color));
                            }
                            internetSPeed = speed;
                        }
                    }, 20);
                }
            }


        } catch (Exception e) {

        }

    }

    String internetSPeed = "";

    private void showDatePicker(CalendarDatum calendarDatum) {
        final DatePickerDialog.OnDateSetListener add_details_farm_flowering = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                showAlert(calendarDatum);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, add_details_farm_flowering, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        // datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.setTitle("Select date of sowing");
        datePickerDialog.show();
    }

    private void showAlert(final CalendarDatum datum) {

        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Are you sure do you want to assign this crop cycle to the farm?")
                .setPositiveButton(getResources().getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ASSIGN_CALENDAR_REQUEST_CODE && resultCode == RESULT_OK && !SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
        } else if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            prepareData();
        }
    }

    private void showDialog(CalendarDatum datum) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_way_point);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        AutoCompleteTextView etPointName = dialog.findViewById(R.id.etPointName);
        TextView cancelTv = dialog.findViewById(R.id.cancelTv);
        TextView addTv = dialog.findViewById(R.id.addTv);
        TextView title = dialog.findViewById(R.id.title);
        TextInputLayout tiName = dialog.findViewById(R.id.tiName);
        tiName.setHint(getResources().getString(R.string.sowing_date_label));
        title.setText("Select sowing date");
        addTv.setText(getResources().getString(R.string.assign_crop_cycle));

        etPointName.setFocusable(false);
        etPointName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar myCalendarAddFarmflowering = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener add_details_farm_flowering = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myCalendarAddFarmflowering.set(Calendar.YEAR, year);
                        myCalendarAddFarmflowering.set(Calendar.MONTH, monthOfYear);
                        myCalendarAddFarmflowering.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String submit_format = "yyyy-MM-dd"; //In which you need put here
                        SimpleDateFormat submit_sdf = new SimpleDateFormat(submit_format, Locale.US);
                        String final_submit_actual_flowering_date = submit_sdf.format(myCalendarAddFarmflowering.getTime());
                        Log.e(TAG, "Sowing Date " + final_submit_actual_flowering_date);

                        etPointName.setText(AppConstants.getShowableDate(final_submit_actual_flowering_date, CropCycleListActivity.this));
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(CropCycleListActivity.this, add_details_farm_flowering, myCalendarAddFarmflowering
                        .get(Calendar.YEAR), myCalendarAddFarmflowering.get(Calendar.MONTH),
                        myCalendarAddFarmflowering.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }
        });
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etPointName.getText().toString())) {
                    Toasty.error(context, getResources().getString(R.string.required_label), Toast.LENGTH_SHORT).show();
                    etPointName.setError(getResources().getString(R.string.required_label));
                } else {
                    dialog.dismiss();
                    setFarmCropCycle(datum, etPointName.getText().toString().trim());
                }

            }
        });

        dialog.show();


    }

    private void setFarmCropCycle(CalendarDatum calendarDatum, String date) {
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String calendarId = calendarDatum.getCalendarId();
        String sowingDate = AppConstants.getUploadableDate(date.trim(), context);
        String data = "user_id:" + userId + " token:" + token + " farm_id:" + farmId + " calendar_id:" + calendarId + " sowing_date:" + sowingDate + " exp_flowering_date:" + null + " exp_harvest_date:" + null + " season_id:" + null + " crop_id:" + null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", "" + userId);
        jsonObject.addProperty("token", "" + token);
        jsonObject.addProperty("farm_id", "" + farmId);
        jsonObject.addProperty("calendar_id", "" + calendarId);
        jsonObject.addProperty("sowing_date", "" + sowingDate);
//        jsonObject.addProperty("exp_flowering_date",""+expFlowDate);
//        jsonObject.addProperty("exp_harvest_date",""+expHarvDate);
//        jsonObject.addProperty("season_id",""+seasonId);
//        jsonObject.addProperty("crop_id",""+cropName);

        Log.e(TAG, "Data Sending " + new Gson().toJson(jsonObject));
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        //Shivangi told status dala to front me bhi change karna padega
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.assignCalendar(jsonObject).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                isLoaded[0] = true;
                String error = "";
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

                    Toasty.success(context, getResources().getString(R.string.data_submit_success_msg), Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();

                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(CropCycleListActivity.this, context.getResources().getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(CropCycleListActivity.this, context.getResources().getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString();
//                        submitBtn.setClickable(true);
//                        submitBtn.setEnabled(true);
                        Log.e(TAG, "AssignErr " + response.errorBody().string());
                        Toasty.error(context, getResources().getString(R.string.something_went_wrong_msg) + ", " + getResources().getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(CropCycleListActivity.this, call.request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e(TAG, "AssignFail " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(CropCycleListActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
//                submitBtn.setClickable(true);
//                submitBtn.setEnabled(true);
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
