package com.i9930.croptrails.HarvestSubmit.EasyMode;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
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
import com.google.gson.Gson;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.GsonBuilder;
import com.i9930.croptrails.AddFarm.Model.FarmData;
import com.i9930.croptrails.AddFarm.Model.FarmerAndFarmData;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.HarvestReport.Model.HarvestDetailInnerData;
import com.i9930.croptrails.HarvestReport.Model.HarvestDetailMaster;
import com.i9930.croptrails.HarvestReport.Model.ViewHarvestDetails;
import com.i9930.croptrails.HarvestSubmit.Adapter.DynamicBagAdapterEasy;
import com.i9930.croptrails.HarvestSubmit.Interfaces.OnFarmDatasetChanged;
import com.i9930.croptrails.HarvestSubmit.Model.DynamicBagAdapterModel;
import com.i9930.croptrails.HarvestSubmit.Model.SendHarvestData;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmCacheManager;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmLoadListener;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerCacheManager;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerT;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestCacheManager;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestFetchListener;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestT;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static java.lang.Double.parseDouble;
import static com.i9930.croptrails.Utility.SharedPreferencesMethod.LAST_BAG_NO;

public class HarvestSubmitEasyModeActivity extends AppCompatActivity implements HarvestFetchListener, FarmLoadListener, OnFarmDatasetChanged {

    String TAG = "HarvestSubmitEasyModeActivity";
    @BindView(R.id.harvestDoneDateEasy)
    TextView harvestDoneDate;
    @BindView(R.id.weighmentDateEasy)
    TextView weighmentDate;
    @BindView(R.id.harvestAreaEasy)
    EditText harvestArea;
    @BindView(R.id.tv_standing_areaEasy)
    TextView tv_standing_area;
    @BindView(R.id.dynamicBagRecyclerViewEasy)
    RecyclerView dynamicBagRecyclerView;
    @BindView(R.id.add_more_imgEasy)
    ImageView add_more_img;
    @BindView(R.id.submit_bag_buttonEasy)
    Button submit_bag_button;

    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();
    Context context;
    Toolbar mActionBarToolbar;
    Resources resources;
    TextView title;
    Double standingdArea = 0.0;
    List<DynamicBagAdapterModel> bagList;
    int bagCount = 0;
    DynamicBagAdapterEasy adapter;
    @BindView(R.id.dynamicBagsTextEasy)
    TextView dynamicBagsText;
    @BindView(R.id.dynamicBagsWeightEasy)
    EditText dynamicBagsWeight;
    @BindView(R.id.addBagButtonEasy)
    TextView addBagButton;
    @BindView(R.id.weightAndBagCardEasy)
    CardView weightAndBagCard;
    @BindView(R.id.harvest_progress)
    GifImageView harvest_progress;
    int removeFlag = 0;
    int lastBagNo = 1;
    double multiplicationFactor = 1.0;
    ViewHarvestDetails details;
    HarvestT harvest;
    Farm farm;
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
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvest_submit_easy_mode);

        context = this;
        loadAds();
        ButterKnife.bind(this);
      /*  if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL).toLowerCase().equals("hectare")) {
            multiplicationFactor = 2.47105;
        } else if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL).toLowerCase().equals("malwa_bigha")) {
            multiplicationFactor = 3.63;
        } else {

        }*/

        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.dynamic_bag_act_title));

        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        lastBagNo = SharedPreferencesMethod.getInt(context, LAST_BAG_NO);
        bagList = new ArrayList<>();
        dynamicBagRecyclerView.setHasFixedSize(true);
        adapter = new DynamicBagAdapterEasy(context, bagList, lastBagNo, this);
        dynamicBagRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        dynamicBagRecyclerView.setLayoutManager(linearLayoutManager);
        dynamicBagRecyclerView.setNestedScrollingEnabled(false);
        listeners();
        datePickingEvents();
        String area = String.format("%.2f", parseDouble(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES)));
        tv_standing_area.setText(area);

        HarvestCacheManager.getInstance(context).getHarvest(this, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
        FarmCacheManager.getInstance(context).getFarm(this, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
       /* dynamicBagsWeight.setHint(resources.getString(R.string.weight_in_kgs));
        dynamicBagsText.setHint(resources.getString(R.string.bags_qty_label));*/
        addBagButton.setHint(resources.getString(R.string.add_label));

        String unit = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL);
        if (unit != null && !TextUtils.isEmpty(unit))
            harvestArea.setHint(unit);
        else {
            harvestArea.setHint(null);
        }

    }


    private void listeners() {
        harvestArea.setHint("in " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL));
        standingdArea = Double.parseDouble(AppConstants.getShowableArea(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES)));
        Log.e(TAG, "standing Area " + Double.parseDouble(AppConstants.getShowableArea(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES))));
        harvestArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!charSequence.toString().trim().equals(".") && !charSequence.toString().trim().equals("")) {
                    if (Double.parseDouble(charSequence.toString()) <= standingdArea) {
                       /* if (standingdArea < Double.parseDouble(charSequence.toString())) {
                            harvestArea.setError("Please enter correct value of harvest area");
                        } else {*/
                        Log.e(TAG, "Entered Area " + Double.parseDouble(charSequence.toString()));
                        tv_standing_area.setText(String.format("%.5f", standingdArea - parseDouble(charSequence.toString())));
                         /*   harvestArea.setError(null);
                        }*/
                    } else {
                        //tv_standing_area.setText("0");
                        tv_standing_area.setError("Standing area can not be less than harvest area");
                    }
                } else {
                    tv_standing_area.setText(String.format("%.5f", standingdArea));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        add_more_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bagList.get(bagList.size() - 1).getBagValue().trim().equals("")) {
                    // bagCount++;
                    bagList.add(new DynamicBagAdapterModel(""));
                    adapter.notifyDataSetChanged();
                } else {
                    Toasty.info(context, "please fill bag").show();
                }

                /*bagList.add(new DynamicBagAdapterModel(bagCount, ""));
                adapter.notifyDataSetChanged();*/
            }
        });


        submit_bag_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                harvestValidations();
                /*for (int i = 0; i < bagList.size(); i++) {
                    Log.e("DynamicBag", "final List bag values " + new Gson().toJson(bagList.get(i)));
                }*/
            }
        });

        addBagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dynamicBagsWeight.getText().toString().trim().equals("")) {
                    Toasty.info(context, resources.getString(R.string.enter_total_weghts_of_packing_count_msg)).show();
                    dynamicBagsWeight.setError(resources.getString(R.string.invalid_input_msg));
                } else if (dynamicBagsText.getText().toString().trim().equals("")) {
                    Toasty.info(context, resources.getString(R.string.enter_packing_count_msg)).show();
                    dynamicBagsText.setError(resources.getString(R.string.invalid_input_msg));
                } else {

                    DynamicBagAdapterModel model = new DynamicBagAdapterModel(Integer.valueOf(dynamicBagsText.getText().toString()), dynamicBagsWeight.getText().toString());
                    bagList.add(model);
                    dynamicBagsText.setText("");
                    dynamicBagsWeight.setText("");
                    adapter.notifyDataSetChanged();
                    weightAndBagCard.setVisibility(View.VISIBLE);
                    addBagButton.setText(resources.getString(R.string.add_more_label));
                }

            }
        });
    }

    private void datePickingEvents() {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                harvestDoneDate.setText(sdf.format(myCalendar.getTime()));
            }
        };
        harvestDoneDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(HarvestSubmitEasyModeActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //date for weighmentDoneDate
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel1();
            }

            private void updateLabel1() {

                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                weighmentDate.setText(sdf.format(myCalendar1.getTime()));
            }

        };
        weighmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void harvestValidations() {
        if (TextUtils.isEmpty(harvestDoneDate.getText())) {
            harvestDoneDate.setError(resources.getString(R.string.please_fill_in_field));
            harvestDoneDate.getParent().requestChildFocus(harvestDoneDate, harvestDoneDate);
            Toasty.error(context, resources.getString(R.string.select_harvest_done_date_msg), Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(weighmentDate.getText())) {
            weighmentDate.setError(resources.getString(R.string.please_fill_in_field));
            weighmentDate.getParent().requestChildFocus(weighmentDate, weighmentDate);
            Toasty.error(context, resources.getString(R.string.enter_weighment_date_msg), Toast.LENGTH_LONG).show();
        } else if (harvestArea.getText().toString().trim().equals("")) {
            harvestArea.setError(resources.getString(R.string.please_fill_in_field));
            harvestArea.getParent().requestChildFocus(harvestArea, harvestArea);
            Toasty.error(context, resources.getString(R.string.enter_harvested_area_msg), Toast.LENGTH_LONG).show();
        } else if (Float.parseFloat(harvestArea.getText().toString().trim()) == 0.0) {
            harvestArea.setError(resources.getString(R.string.correct_harvest_area));
            harvestArea.getParent().requestChildFocus(harvestArea, harvestArea);
        } else if (standingdArea < Double.parseDouble(harvestArea.getText().toString().trim())) {
            harvestArea.setError(resources.getString(R.string.correct_harvest_area));
            harvestArea.getParent().requestChildFocus(harvestArea, harvestArea);
        } else if (bagList.size() == 0 && TextUtils.isEmpty(dynamicBagsWeight.getText().toString())) {
            dynamicBagsWeight.setError(resources.getString(R.string.please_fill_in_field));
            dynamicBagsWeight.getParent().requestChildFocus(dynamicBagsWeight, dynamicBagsWeight);
        } else if (bagList.size() == 0) {
            //data in list is zero and weight et is empty
            if (dynamicBagsWeight.getText().toString().trim().equals("")) {
                Toasty.info(context, resources.getString(R.string.enter_total_weghts_of_packing_count_msg)).show();
                dynamicBagsWeight.setError(resources.getString(R.string.invalid_input_msg));
            } else if (dynamicBagsText.getText().toString().trim().equals("")) {
                Toasty.info(context, resources.getString(R.string.enter_packing_count_msg)).show();
                dynamicBagsText.setError(resources.getString(R.string.invalid_input_msg));
            } else {
                Log.e("DynamicBag", "data in list is zero and weight et is empty " + 1);
                List<String> weightList = new ArrayList<>();
                weightList.add(dynamicBagsWeight.getText().toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setTitle(resources.getString(R.string.harvest_data));
                builder.setMessage(resources.getString(R.string.do_you_want_to_submit_this_data) + weightList.toString());
                builder.setPositiveButton(resources.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        removeFlag = 1;
                        int bagNo = 1;
                        DynamicBagAdapterModel model = new DynamicBagAdapterModel(Integer.valueOf(dynamicBagsText.getText().toString().trim()), dynamicBagsWeight.getText().toString());
                        bagList.add(model);
                        //Toast.makeText(DynamicBagActivity.this, "bagWeightList=" + bagWeightList.toString(), Toast.LENGTH_SHORT).show();
                        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                            saveHarvestOffline();
                        } else
                            callRetrofit();
                    }
                });
                builder.setNeutralButton(resources.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }

        } else if (bagList.size() > 0) {

            if (TextUtils.isEmpty(dynamicBagsText.getText().toString()) && TextUtils.isEmpty(dynamicBagsWeight.getText().toString())) {
                //data in list present and weight et is empty
                Log.e("DynamicBag", "data in list is present and weight et is empty " + 2);
                List<String> weightList = new ArrayList<>();
                for (int i = 0; i < bagList.size(); i++) {
                    weightList.add(bagList.get(i).bagValue);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setTitle(resources.getString(R.string.harvest_data));
                builder.setMessage(resources.getString(R.string.do_you_want_to_submit_this_data) + weightList.toString());
                builder.setPositiveButton(resources.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeFlag = 0;
                        //Toast.makeText(DynamicBagActivity.this, "bagWeightList=" + bagWeightList.toString(), Toast.LENGTH_SHORT).show();
                        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                            saveHarvestOffline();
                        } else
                            callRetrofit();
                    }
                });
                builder.setNeutralButton(resources.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            } else {
                if (dynamicBagsWeight.getText().toString().trim().equals("")) {
                    Toasty.info(context, resources.getString(R.string.enter_total_weghts_of_packing_count_msg)).show();
                    dynamicBagsWeight.setError(resources.getString(R.string.invalid_input_msg));
                } else if (dynamicBagsText.getText().toString().trim().equals("")) {
                    Toasty.info(context, resources.getString(R.string.enter_packing_count_msg)).show();
                    dynamicBagsText.setError(resources.getString(R.string.invalid_input_msg));
                } else {
                    Log.e("DynamicBag", "data in list is zero and weight et is empty " + 1);
                    List<String> weightList = new ArrayList<>();
                    for (int i = 0; i < bagList.size(); i++) {
                        weightList.add(bagList.get(i).bagValue);
                    }
                    weightList.add(dynamicBagsWeight.getText().toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(false);
                    builder.setTitle(resources.getString(R.string.harvest_data));
                    builder.setMessage(resources.getString(R.string.do_you_want_to_submit_this_data) + weightList.toString());
                    builder.setPositiveButton(resources.getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            removeFlag = 1;
                            int bagNo = 1;
                            DynamicBagAdapterModel model = new DynamicBagAdapterModel(Integer.valueOf(dynamicBagsText.getText().toString().trim()), dynamicBagsWeight.getText().toString());
                            bagList.add(model);
                            //Toast.makeText(DynamicBagActivity.this, "bagWeightList=" + bagWeightList.toString(), Toast.LENGTH_SHORT).show();
                            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                                saveHarvestOffline();
                            } else
                                callRetrofit();
                        }
                    });
                    builder.setNeutralButton(resources.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            }


        }

    }


    public void callRetrofit() {
      /*  final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();*/

        harvest_progress.setVisibility(View.VISIBLE);
        // last_bag_no = SharedPreferencesMethod.getInt(context, SharedPreferencesMethod.LAST_BAG_NO);

        //float weightArr[] = new float[bagList.size()];
        List<String> bagArr = new ArrayList<>();
        List<String> weightArr = new ArrayList<>();
        //int bagArr[] = new int[bagList.size()];
        float total_net_weight = 0;
        float total_gross_weight = 0;

        for (int i = 0; i < bagList.size(); i++) {
            int bags = bagList.get(i).getBagNo();
            float totalValue = Float.valueOf(bagList.get(i).getBagValue());
            float singleBagValue = totalValue / bags;
            for (int j = 1; j <= bags; j++) {
                weightArr.add(String.valueOf(singleBagValue));
                bagArr.add(String.valueOf(lastBagNo));
                lastBagNo++;
            }

            total_net_weight = total_net_weight + Integer.valueOf(bagList.get(i).getBagValue());

        }

        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            String standingArea = AppConstants.getUploadableArea(tv_standing_area.getText().toString().trim());
            String harvestedArea = AppConstants.getUploadableArea(harvestArea.getText().toString().trim());
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            SendHarvestData sendHarvestDataObject = new SendHarvestData();
            sendHarvestDataObject.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
            sendHarvestDataObject.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
            sendHarvestDataObject.setSupervisor_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
            sendHarvestDataObject.setArea_harvested(Float.parseFloat(harvestedArea));
            sendHarvestDataObject.setStanding_acres(Float.parseFloat(standingArea));
            sendHarvestDataObject.setBag_no(bagArr);
            sendHarvestDataObject.setNet_wt(weightArr);
            sendHarvestDataObject.setDeleted_by(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
            sendHarvestDataObject.setGross_wt(weightArr);
            sendHarvestDataObject.setWeighment_date(weighmentDate.getText().toString());
            sendHarvestDataObject.setTotal_net_weight(total_net_weight);
            sendHarvestDataObject.setTotal_gross_weight(total_net_weight);
            //sendHarvestDataObject.setNet_wt();
            sendHarvestDataObject.setHarvest_date(harvestDoneDate.getText().toString());
            sendHarvestDataObject.setWeighment_date(weighmentDate.getText().toString());
            sendHarvestDataObject.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            sendHarvestDataObject.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            Log.e(TAG, "Send Data " + new Gson().toJson(sendHarvestDataObject));
            Call<StatusMsgModel> msgModelCall = apiService.getStatusMsgModelForHarvestBags(sendHarvestDataObject);
            msgModelCall.enqueue(new Callback<StatusMsgModel>() {
                @Override
                public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {

                    //StatusMsgModel statusMsgModel = response.body();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(HarvestSubmitEasyModeActivity.this, response.body().getMsg());
                        } else {
                            Log.e("DynamicBag", "response " + new Gson().toJson(response.body()));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toasty.success(context, resources.getString(R.string.bag_added_harvest), Toast.LENGTH_LONG).show();
//                                    progressDialog.dismiss();
                                    harvest_progress.setVisibility(View.GONE);
                                }
                            });

                            double standingArea = Double.parseDouble(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES));
                            double harvestAre = Double.parseDouble(harvestArea.getText().toString().trim());
                            standingArea = standingArea - harvestAre;
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, String.valueOf(standingArea));
                            Intent intent = getIntent();
                            setResult(RESULT_OK);
                            finish();
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        if (removeFlag == 1) {
                            bagList.remove(bagList.size() - 1);
                        }
                        harvest_progress.setVisibility(View.GONE);
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(HarvestSubmitEasyModeActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        harvest_progress.setVisibility(View.GONE);
                        if (removeFlag == 1) {
                            bagList.remove(bagList.size() - 1);
                        }
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(HarvestSubmitEasyModeActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        if (removeFlag == 1) {
                            bagList.remove(bagList.size() - 1);
                        }
                        try {
//                            progressDialog.dismiss();
                            harvest_progress.setVisibility(View.GONE);
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_add_harvest_data));
                            Log.e("DynamicBag", "error " + response.errorBody().string().toString());
                            //Toast.makeText(context, "Oops something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        if (removeFlag == 1) {
                            bagList.remove(bagList.size() - 1);
                        }
                        callRetrofit();
                    } else {
//                        progressDialog.dismiss();
                        harvest_progress.setVisibility(View.GONE);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_add_harvest_data));
                    }
                    Log.e("DynamicBag exc", "HArvest Data Send Failure" + t.toString());
                }
            });

        } else {

        }

    }

    private void saveHarvestOffline() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDateStr = df.format(c);

        float total_net_weight = 0;
        List<HarvestDetailInnerData> weightArr = new ArrayList<>();
        for (int i = 0; i < bagList.size(); i++) {

            int bags = bagList.get(i).getBagNo();
            float totalValue = Float.valueOf(bagList.get(i).getBagValue());
            float singleBagValue = totalValue / bags;
            for (int j = 1; j <= bags; j++) {
                HarvestDetailInnerData data = new HarvestDetailInnerData();
                data.setBagNo(lastBagNo + "");
                data.setGrossWt(String.valueOf(singleBagValue));
                data.setNetWt(String.valueOf(singleBagValue));
                data.setDoa(todayDateStr);
                total_net_weight = total_net_weight + singleBagValue;
                weightArr.add(data);
                lastBagNo++;
            }

        }

        String standingArea = AppConstants.getUploadableArea(tv_standing_area.getText().toString().trim());
        String harvestedArea = AppConstants.getUploadableArea(harvestArea.getText().toString().trim());

        HarvestDetailMaster master = new HarvestDetailMaster();
        master.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        master.setFarmId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
        master.setHarvestDate(harvestDoneDate.getText().toString());
        master.setHarvestedArea(harvestedArea);
        master.setDoa(todayDateStr);
        master.setStandingArea(standingArea);
        master.setTotalGrossWeight(String.valueOf(total_net_weight));
        master.setTotalNetWeight(String.valueOf(total_net_weight));
        master.setWeighmentDate(weighmentDate.getText().toString());
        master.setSupervisorId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, standingArea);

        if (details == null) {
            details = new ViewHarvestDetails();
            List<List<HarvestDetailInnerData>> bagLists = new ArrayList<>();
            bagLists.add(weightArr);
            List<HarvestDetailMaster> masterList = new ArrayList<>();
            masterList.add(master);
            details.setData(bagLists);
            details.setData1(masterList);
        } else {
            if (details.getData1() != null) {
                details.getData1().add(master);
                details.getData().add(weightArr);
            } else {
                List<List<HarvestDetailInnerData>> bagLists = new ArrayList<>();
                bagLists.add(weightArr);
                List<HarvestDetailMaster> masterList = new ArrayList<>();
                masterList.add(master);
                details.setData1(masterList);
                details.setData(bagLists);
            }
        }
        FarmData farmData = null;

        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            farmData = gson.fromJson(farm.getFarmFullData(), FarmData.class);
            farmData.setStandingArea(standingArea);
            if (AppConstants.isValidActualArea(harvestedArea)) {
                float areaHarvested;
                areaHarvested= farmData.getAreaHarvested() + Float.valueOf(harvestedArea);
                farmData.setAreaHarvested(areaHarvested);
                farm.setAreaHarvested("" + areaHarvested);


                if (farmerT != null && AppConstants.isValidString(farmerT.getData())) {
                    FarmerAndFarmData farmerAndFarmData = null;
                    farmerAndFarmData = gson.fromJson(farmerT.getData(), FarmerAndFarmData.class);

                    if (farmerAndFarmData != null && farmerAndFarmData.getFarmDataList() != null && farmerAndFarmData.getFarmDataList().size() > 0) {
                        for (int i = 0; i < farmerAndFarmData.getFarmDataList().size(); i++) {
                            if (AppConstants.isValidString(farmerAndFarmData.getFarmDataList().get(i).getFarmId()) &&
                                    AppConstants.isValidString(farmData.getFarmId()) &&
                                    farmData.getFarmId().equalsIgnoreCase(farmerAndFarmData.getFarmDataList().get(i).getFarmId())) {
                                farmerAndFarmData.getFarmDataList().get(i).setAreaHarvested(areaHarvested);
                                farmerAndFarmData.getFarmDataList().get(i).setStandingArea(standingArea);
                                break;
                            }

                        }
                        farmerT.setData(new Gson().toJson(farmerAndFarmData));
                        farmerT.setIsUpdated("Y");
                        farmerT.setIsUploaded("N");
                        FarmerCacheManager.getInstance(context).updateFarm(new FarmerCacheManager.OnUpdateSuccessListener() {
                            @Override
                            public void onFarmerUpdated(boolean isUpdated) {

                            }
                        },farmerT);
                    }

                }
            }

        } catch (Exception e) {

        }
        if (farmData!=null)
            farm.setFarmFullData(new Gson().toJson(farmData));
        harvest.setOfflineHarvest(new Gson().toJson(details));
        harvest.setIsUploaded("N");
        HarvestCacheManager.getInstance(context).updateHarvestData(harvest);
        farm.setStandingAcres(standingArea);
        farm.setIsUploaded("N");
        farm.setIsUpdated("Y");
        FarmCacheManager.getInstance(context).updateFarm(farm);
        Toasty.success(context, "Offline harvest stored successfully", Toast.LENGTH_LONG, false).show();
        finish();

    }

    @Override
    public void onHarvestLoaded(HarvestT harvest) {
        this.harvest = harvest;
        if (harvest != null) {
            Gson gson = new Gson();
            details = gson.fromJson(harvest.getOfflineHarvest(), ViewHarvestDetails.class);
        } else {

        }
    }

    FarmerT farmerT;

    @Override
    public void onFarmLoader(Farm farm) {
        this.farm = farm;
        if (farm != null && AppConstants.isValidString(farm.getFarmerId()))
            FarmerCacheManager.getInstance(context).getFarmer(new FarmerCacheManager.GetFarmerListener() {
                @Override
                public void onFarmerLoaded(FarmerT farmerT) {
                    HarvestSubmitEasyModeActivity.this.farmerT = farmerT;
                }
            }, farm.getFarmerId());
    }

    @Override
    public void isLastItemRemoved(boolean isLatsItem, int position) {
        if (isLatsItem) {
            addBagButton.setText(resources.getString(R.string.add_label));
            weightAndBagCard.setVisibility(View.GONE);

        } else {
            addBagButton.setText(resources.getString(R.string.add_more_label));
        }
    }

    @Override
    public void onDataSetChanged(List<FarmData> farmData) {

    }

    @Override
    public void onOwnerShipImageClick(int index, EditText editText) {

    }

    @Override
    public void onFarmImageClick(int index, EditText editText) {

    }
}
