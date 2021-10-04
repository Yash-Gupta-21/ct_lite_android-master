package com.i9930.croptrails.HarvestCollection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.HarvestCollection.Model.HarvestCollectionData;
import com.i9930.croptrails.HarvestCollection.Model.HarvestCollectionMainData;
import com.i9930.croptrails.HarvestCollection.Model.HarvestCollectionTotalGrossData;
import com.i9930.croptrails.HarvestCollection.Model.HarvestSendPlannedData;
import com.i9930.croptrails.HarvestCollection.Model.HarvestSubmitData;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class HarvestPlanActivity extends AppCompatActivity {

    String TAG = "HarvestPlanActivity";
    CheckBox checkBox;
    Calendar harvest_plan_date = Calendar.getInstance();

    Context context;
    @BindView(R.id.expanding_list_main)
    ExpandingList expanding_list_main;
    Resources resources;
    @BindView(R.id.harvest_plan_pick_up_date)
    TextView harvest_plan_pick_up_date;
    List<List<HarvestCollectionTotalGrossData>> data1;
    List<List<HarvestCollectionData>> data;
    Toolbar mActionBarToolbar;
    List<String> parentList = new ArrayList<>();
    int[] colors = {R.color.pink, R.color.blue, R.color.purple, R.color.orange, R.color.green, R.color.yellow,
            R.color.new_color, R.color.colorAccent, R.color.new_theme, R.color.new_theme_status_bar, R.color.grey, R.color.black};

    @BindView(R.id.harvest_plan_vehicle_no)
    EditText harvest_plan_vehicle_no;
    @BindView(R.id.harvest_plan_vehicle_weight)
    EditText harvest_plan_vehicle_weight;
    @BindView(R.id.harvest_plan_remark)
    EditText harvest_plan_remark;
    @BindView(R.id.main_harvest_plan_rela_lay)
    RelativeLayout main_harvest_plan_rela_lay;
    float[] bag_weight;
    String[] harvest_detail_id_array;
    String[] farm_id_array;
    double total_weight_full;
    @BindView(R.id.total_planned_weight)
    TextView total_planned_weight;
    @BindView(R.id.img_check_total_weight)
    ImageView img_check_total_weight;
    @BindView(R.id.no_data_available_harvest_plan_collection)
    RelativeLayout no_data_available;
    @BindView(R.id.data_available_layout)
    RelativeLayout data_available_layout;
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
        String languageToLoad = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
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

        setContentView(R.layout.activity_harvest_plan);

        context = this;
        loadAds();
        ButterKnife.bind(this);
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        TextView title = (TextView) findViewById(R.id.tittle);
        //   Button submit_butt=(Button)findViewById(R.id.submit_test);
        title.setText(resources.getString(R.string.harvest_plan_activity_title));
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        getCollectionData();
        datePickingEvent();

        img_check_total_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size_of_final_array = 0;
                for (int i = 0; i < data.size(); i++) {
                    for (int j = 0; j < data.get(i).size(); j++) {
                        if (data.get(i).get(j).isSelected()) {
                            size_of_final_array++;
                        }
                        Log.e("TAGgg", j + "  " + String.valueOf(data.get(i).get(j).isSelected()) + "");
                    }
                }

                int index = 0;
                bag_weight = new float[size_of_final_array];
                for (int i = 0; i < data.size(); i++) {
                    for (int j = 0; j < data.get(i).size(); j++) {
                        if (data.get(i).get(j).isSelected()) {
                            bag_weight[index] = Float.parseFloat(data.get(i).get(j).getNetWt());
                            index++;
                        }

                        Log.e("TAGgg", j + "  " + String.valueOf(data.get(i).get(j).isSelected()) + "");
                    }
                }

                //  Toast.makeText(context, index + "", Toast.LENGTH_SHORT).show();
                total_weight_full = 0.0;
                for (int k = 0; k < index; k++) {
                    total_weight_full = bag_weight[k] + total_weight_full;
                    Log.e("TagArray", String.valueOf(bag_weight[k]) + "");
                }

                total_planned_weight.setText(String.valueOf(total_weight_full));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_harvest_plan, menu);
        return true;
    }

    private void getCollectionData() {
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);

        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        Call<HarvestCollectionMainData> expenseDataCall = apiService.getHarvestDataForCollection(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        expenseDataCall.enqueue(new Callback<HarvestCollectionMainData>() {

            @Override
            public void onResponse(Call<HarvestCollectionMainData> call, Response<HarvestCollectionMainData> response) {
                String error = null;
                isLoaded[0] =true;
                if (response.isSuccessful()) {

                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(HarvestPlanActivity.this, response.body().getMsg());
                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(HarvestPlanActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(HarvestPlanActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        HarvestCollectionMainData harvestCollectionMainData = response.body();
                        Log.e(TAG, "Rsponse " + new Gson().toJson(harvestCollectionMainData));
                        if (harvestCollectionMainData.getData() != null && harvestCollectionMainData.getData().size() > 0) {
                            for (int i = 0; i < harvestCollectionMainData.getData().size(); i++) {
                                for (int j = 0; j < harvestCollectionMainData.getData().get(i).size(); j++) {
                                    Log.e("ServerHarvestData", harvestCollectionMainData.getData().get(i).get(j).getNetWt() + "");
                                }
                            }

                            data1 = harvestCollectionMainData.getData1();
                            data = harvestCollectionMainData.getData();
                            for (int i = 0; i < data1.size(); i++) {
                                parentList.add(data1.get(i).get(0).getLotNo());
                            }
                            setDataInExpandable(parentList);
                        } else {
                        /*runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(context, resources.getString(R.string.no_data_available_to_plan_harvest));
                            }
                        });*/

                            data_available_layout.setVisibility(View.GONE);
                            no_data_available.setVisibility(View.VISIBLE);

                        }
                        Log.e("Message", harvestCollectionMainData.getMsg());


                    }


                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(HarvestPlanActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(HarvestPlanActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG,"ServerError "+ error);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_load_plan_harvest));
                        //Toast.makeText(context, "Oops something went wrong. Please Reload", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(HarvestPlanActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<HarvestCollectionMainData> call, Throwable t) {
                isLoaded[0] =true;
                //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(HarvestPlanActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_load_plan_harvest));
                Log.e(TAG, "Failure " + t.toString());
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


    private void setDataInExpandable(List<String> parentList) {
        for (int i = 0; i < parentList.size(); i++) {
            ExpandingItem item = expanding_list_main.createNewItem(R.layout.test_expanding_layout);
            TextView textView = (TextView) item.findViewById(R.id.title);
            textView.setText(resources.getString(R.string.farm_id_label) + ": " + parentList.get(i));
            //CheckBox selectAllCheck = item.findViewById(R.id.select_all_bags_check);
            item.createSubItems(data.get(i).size());
            Random r = new Random();
            int i1 = r.nextInt(colors.length - 0) + 0;
            int color = colors[i1];
            item.setIndicatorColorRes(R.color.new_theme);
            item.setIndicatorIconRes(R.drawable.ic_dot_for_timeline);
            for (int j = 0; j < data.get(i).size(); j++) {
                final int tempI = i;
                final int tempJ = j;
                int seq = j + 1;
                View subItemZero = item.getSubItemView(j);
                ((TextView) subItemZero.findViewById(R.id.sub_title)).setText(resources.getString(R.string.weight_label) + ": " + data.get(i).get(j).getNetWt() + " " + resources.getString(R.string.kg_label));
                ((TextView) subItemZero.findViewById(R.id.sub_item_sequence)).setText("" + seq);
                checkBox = subItemZero.findViewById(R.id.checkbox);
                if (data.get(i).get(j).isSelected()) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            data.get(tempI).get(tempJ).setSelected(true);
                        } else {
                            data.get(tempI).get(tempJ).setSelected(false);
                        }
                    }
                });
            }
        }
    }

    private void datePickingEvent() {
        final DatePickerDialog.OnDateSetListener datesowing = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                harvest_plan_date.set(Calendar.YEAR, year);
                harvest_plan_date.set(Calendar.MONTH, monthOfYear);
                harvest_plan_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateHarvestPlanDate();
            }
        };

        harvest_plan_pick_up_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, datesowing, harvest_plan_date
                        .get(Calendar.YEAR), harvest_plan_date.get(Calendar.MONTH),
                        harvest_plan_date.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }

    private void updateHarvestPlanDate() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        harvest_plan_pick_up_date.setText(sdf.format(harvest_plan_date.getTime()));
        harvest_plan_pick_up_date.setError(null);

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_submit) {

            if (harvest_plan_vehicle_no.getText().toString().equals("")) {
                harvest_plan_vehicle_no.setError(resources.getString(R.string.please_enter_valid_vehicle_no));
            } else if (harvest_plan_vehicle_weight.getText().toString().equals("")) {
                harvest_plan_vehicle_weight.setError(resources.getString(R.string.please_enter_valid_vehicle_weight));
            } else if (harvest_plan_remark.getText().toString().equals("")) {
                harvest_plan_remark.setError(resources.getString(R.string.please_enter_valid_remark));
            } else if (harvest_plan_pick_up_date.getText().equals("DD/MM/YYYY")) {
                harvest_plan_pick_up_date.setError(resources.getString(R.string.please_choose_valid_date));
            } else {
                int size_of_final_array = 0;

                for (int i = 0; i < data.size(); i++) {
                    for (int j = 0; j < data.get(i).size(); j++) {
                        if (data.get(i).get(j).isSelected()) {
                            size_of_final_array++;
                        }
                        Log.e("TAGgg", j + "  " + String.valueOf(data.get(i).get(j).isSelected()) + "");
                    }
                }

                int index = 0;
                bag_weight = new float[size_of_final_array];
                harvest_detail_id_array = new String[size_of_final_array];
                farm_id_array = new String[size_of_final_array];


                for (int i = 0; i < data.size(); i++) {
                    for (int j = 0; j < data.get(i).size(); j++) {
                        if (data.get(i).get(j).isSelected()) {
                            bag_weight[index] = Float.parseFloat(data.get(i).get(j).getNetWt());
                            harvest_detail_id_array[index] = String.valueOf(data.get(i).get(j).getHarvestDetailId());
                            farm_id_array[index] = String.valueOf(data.get(i).get(j).getFarmId());
                            index++;
                        }


                        Log.e("TAGgg", j + "  " + String.valueOf(data.get(i).get(j).isSelected()) + "");
                    }
                }

                //Toast.makeText(context, index + "", Toast.LENGTH_SHORT).show();
                total_weight_full = 0.0;
                for (int k = 0; k < index; k++) {
                    total_weight_full = bag_weight[k] + total_weight_full;
                    Log.e("TagArray", String.valueOf(bag_weight[k]) + "");
                }


                if (index != 0) {

                    String submit_format = "yyyy-MM-dd"; //In which you need put here
                    SimpleDateFormat submit_sdf = new SimpleDateFormat(submit_format, Locale.US);
                    Date harvest_pick_up_date = null;


                    try {
                        harvest_pick_up_date = new SimpleDateFormat("dd/MM/yyyy").parse(harvest_plan_pick_up_date.getText().toString().trim());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String str_harvest_pick_up_date = submit_sdf.format(harvest_pick_up_date);
                    String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                    ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                    HarvestSendPlannedData harvestSendPlannedData = new HarvestSendPlannedData();
                    harvestSendPlannedData.setFarm_id(farm_id_array);
                    harvestSendPlannedData.setNet_wt_of_stock(String.valueOf(total_weight_full));
                    harvestSendPlannedData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                    harvestSendPlannedData.setNo_of_bags_blk(String.valueOf(index));
                    harvestSendPlannedData.setTotal_bags(String.valueOf(index));
                    harvestSendPlannedData.setAvg_moisture("5");
                    harvestSendPlannedData.setSvid(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                    harvestSendPlannedData.setTrans_admin_id("1");
                    harvestSendPlannedData.setVehicle_no(harvest_plan_vehicle_no.getText().toString().trim());
                    harvestSendPlannedData.setVehicle_net_wt(harvest_plan_vehicle_weight.getText().toString().trim());
                    harvestSendPlannedData.setDriver_name("");
                    harvestSendPlannedData.setPlan_pickup_date(str_harvest_pick_up_date);
                    harvestSendPlannedData.setHarvest_detail_id(harvest_detail_id_array);
                    harvestSendPlannedData.setRemark(harvest_plan_remark.getText().toString().trim());
                    harvestSendPlannedData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                    harvestSendPlannedData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                    Log.e(TAG, "Full Data " + new Gson().toJson(harvestSendPlannedData));
                    Call<HarvestSubmitData> expenseDataCall = apiService.getHarvestSubmitPlannedData(harvestSendPlannedData);
                    final boolean[] isLoaded = {false};
                    long currMillis = AppConstants.getCurrentMills();
                    expenseDataCall.enqueue(new Callback<HarvestSubmitData>() {
                                                @Override
                                                public void onResponse(Call<HarvestSubmitData> call, Response<HarvestSubmitData> response) {
                                                    String error=null;
                                                    isLoaded[0] =true;
                                                    if (response.isSuccessful()) {
                                                        Log.e(TAG, "Uploading Response " + new Gson().toJson(response.body()));
                                                        HarvestSubmitData harvestSubmitData = response.body();
                                                        if (response.body().getStatus() == 10) {
                                                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                                                            viewFailDialog.showSessionExpireDialog(HarvestPlanActivity.this, resources.getString(R.string.multiple_login_msg));
                                                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                                            //un authorized access
                                                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                                                            viewFailDialog.showSessionExpireDialog(HarvestPlanActivity.this, resources.getString(R.string.unauthorized_access));
                                                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                                            // auth token expired
                                                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                                                            viewFailDialog.showSessionExpireDialog(HarvestPlanActivity.this, resources.getString(R.string.authorization_expired));
                                                        } else if (harvestSubmitData.getStatus() != 0) {
                                                            Toasty.success(context, response.body().getMsg(), Toast.LENGTH_LONG, true).show();

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

                                                        } else {
                                                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                                                            viewFailDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.failed_to_plan_harvest_collection));
                                                        }
                                                        // Toast.makeText(context,resources.getString(R.string.harvest_collection_planned_successfully), Toast.LENGTH_LONG).show();

                                                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                                        //un authorized access
                                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                                        viewFailDialog.showSessionExpireDialog(HarvestPlanActivity.this, resources.getString(R.string.unauthorized_access));
                                                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                                        // auth token expired
                                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                                        viewFailDialog.showSessionExpireDialog(HarvestPlanActivity.this, resources.getString(R.string.authorization_expired));
                                                    } else {
                                                        try {
                                                            error=response.errorBody().string().toString();
                                                            Log.e(TAG, "Submit Errr "+error);
                                                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                                                            viewFailDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.failed_to_plan_harvest_collection));
                                                            //Toast.makeText(context, "Oops something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    long newMillis = AppConstants.getCurrentMills();
                                                    long diff = newMillis - currMillis;
                                                    if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
                                                        notifyApiDelay(HarvestPlanActivity.this, response.raw().request().url().toString(),
                                                                "" + diff, internetSPeed, error, response.code());
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<HarvestSubmitData> call, Throwable t) {
                                                    isLoaded[0]=true;
                                                    long newMillis = AppConstants.getCurrentMills();
                                                    long diff = newMillis - currMillis;
                                                    notifyApiDelay(HarvestPlanActivity.this, call.request().url().toString(),
                                                            "" + diff, internetSPeed, t.toString(), 0);
                                                    //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                                    viewFailDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.failed_to_plan_harvest_collection));
                                                }
                                            }
                    );

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            internetFlow(isLoaded[0]);
                        }
                    }, AppConstants.DELAY);
                } else {
                    Snackbar snackbar = Snackbar.make(main_harvest_plan_rela_lay, resources.getString(R.string.please_select), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

                //Toast.makeText(context, "Toast clicked from toolbar", Toast.LENGTH_SHORT).show();

            }

        } else {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    String internetSPeed = "";

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
                            ConnectivityUtils.checkSpeedWithColor(HarvestPlanActivity.this, new ConnectivityUtils.ColorListener() {
                                @Override
                                public void onColorChanged(int color, String speed) {
                                    if (color == android.R.color.holo_green_dark) {
                                        connectivityLine.setVisibility(View.GONE);
//                                    connectivityLine.setBackgroundColor(getColor(R.color.blue));
                                    } else {
                                        connectivityLine.setVisibility(View.VISIBLE);
                                        connectivityLine.setBackgroundColor(getColor(color));
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

}
