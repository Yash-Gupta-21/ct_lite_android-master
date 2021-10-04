package com.i9930.croptrails.GerminationAndSpacing;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.GsonBuilder;
import com.i9930.croptrails.AddFarm.Model.FarmData;
import com.i9930.croptrails.AddFarm.Model.FarmerAndFarmData;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.GerminationAndSpacing.Model.SendGerminationSpacingData;
import com.i9930.croptrails.GerminationAndSpacing.ShowSampleGermination.Model.SampleGerminationDatum;
import com.i9930.croptrails.HarvestSubmit.EasyMode.HarvestSubmitEasyModeActivity;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmCacheManager;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmLoadListener;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerCacheManager;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerT;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class AddGermiFullScreenDialog extends DialogFragment implements FarmLoadListener {
    public static String TAG = "AddGermiFullScreenDialog";
    Context context;
    Farm farm;
    @BindView(R.id.configuredArea_text_input_et1)
    EditText configured_area_et1;
    @BindView(R.id.plant_to_plant_et1)
    EditText plant_to_plant_spacing_et1;
    @BindView(R.id.row_to_row_et1)
    EditText row_to_row_spacing_et1;
    @BindView(R.id.actual_population_et1)
    EditText actual_population_et1;
    @BindView(R.id.idealPopulationtv1)
    TextView ideal_population_tv1;
    @BindView(R.id.germination_text_input_tv1)
    TextView germiantion_tv1;
    @BindView(R.id.full_population_tv1)
    TextView full_popu_tv1;

    //sample2
    @BindView(R.id.configuredArea_text_input_et2)
    EditText configured_area_et2;
    @BindView(R.id.plant_to_plant_et2)
    EditText plant_to_plant_spacing_et2;
    @BindView(R.id.row_to_row_et2)
    EditText row_to_row_spacing_et2;
    @BindView(R.id.actual_population_et2)
    EditText actual_population_et2;
    @BindView(R.id.idealPopulationtv2)
    TextView ideal_population_tv2;
    @BindView(R.id.germination_text_input_tv2)
    TextView germiantion_tv2;
    @BindView(R.id.full_population_tv2)
    TextView full_popu_tv2;


    //sample3
    @BindView(R.id.configuredArea_text_input_et3)
    EditText configured_area_et3;
    @BindView(R.id.plant_to_plant_et3)
    EditText plant_to_plant_spacing_et3;
    @BindView(R.id.row_to_row_et3)
    EditText row_to_row_spacing_et3;
    @BindView(R.id.actual_population_et3)
    EditText actual_population_et3;
    @BindView(R.id.idealPopulationtv3)
    TextView ideal_population_tv3;
    @BindView(R.id.germination_text_input_tv3)
    TextView germiantion_tv3;
    @BindView(R.id.full_population_tv3)
    TextView full_popu_tv3;


    //sample4
    @BindView(R.id.configuredArea_text_input_et4)
    EditText configured_area_et4;
    @BindView(R.id.plant_to_plant_et4)
    EditText plant_to_plant_spacing_et4;
    @BindView(R.id.row_to_row_et4)
    EditText row_to_row_spacing_et4;
    @BindView(R.id.actual_population_et4)
    EditText actual_population_et4;
    @BindView(R.id.idealPopulationtv4)
    TextView ideal_population_tv4;
    @BindView(R.id.germination_text_input_tv4)
    TextView germiantion_tv4;
    @BindView(R.id.full_population_tv4)
    TextView full_popu_tv4;


    //sample5
    @BindView(R.id.configuredArea_text_input_et5)
    EditText configured_area_et5;
    @BindView(R.id.plant_to_plant_et5)
    EditText plant_to_plant_spacing_et5;
    @BindView(R.id.row_to_row_et5)
    EditText row_to_row_spacing_et5;
    @BindView(R.id.actual_population_et5)
    EditText actual_population_et5;
    @BindView(R.id.idealPopulationtv5)
    TextView ideal_population_tv5;
    @BindView(R.id.germination_text_input_tv5)
    TextView germiantion_tv5;
    @BindView(R.id.full_population_tv5)
    TextView full_popu_tv5;

    @BindView(R.id.submit_germi_data)
    TextView submit_germi_data;
    Resources resources;
    @BindView(R.id.remove_img_butt)
    ImageView remove_img_butt;
    @BindView(R.id.add_more_img_butt)
    ImageView add_more_img;
    @BindView(R.id.addGerminationFullLayout)
    RelativeLayout germi_linear_layout;
    @BindView(R.id.sample1_rel_lay1)
    LinearLayout sampleLayout1;
    @BindView(R.id.sample1_rel_lay2)
    LinearLayout sampleLayout2;
    @BindView(R.id.sample1_rel_la3)
    LinearLayout sampleLayout3;
    @BindView(R.id.sample1_rel_la4)
    LinearLayout sampleLayout4;
    @BindView(R.id.sample1_rel_lay5)
    LinearLayout sampleLayout5;
    /*@BindView(R.id.top_linear_lay)
    LinearLayout top_linear_lay;*/

    @BindView(R.id.avg_farm_popu_tv)
    TextView avg_farm_popu_tv;
    @BindView(R.id.avg_pp_spacing_tv)
    TextView avg_pp_spacing_tv;
    @BindView(R.id.avg_rr_spacing_tv)
    TextView avg_rr_spacing_tv;
    @BindView(R.id.avg_germination_tv)
    TextView avg_germi_tv;
    ProgressDialog progressDialog;
    @BindView(R.id.connectivityLine)
    View connectivityLine;
    double flt_row_to_row_spacing1, flt_plant_to_plant_spacing1, flt_configured_area1;
    double flt_row_to_row_spacing2, flt_plant_to_plant_spacing2, flt_configured_area2;
    double flt_row_to_row_spacing3, flt_plant_to_plant_spacing3, flt_configured_area3;
    double flt_row_to_row_spacing4, flt_plant_to_plant_spacing4, flt_configured_area4;
    double flt_row_to_row_spacing5, flt_plant_to_plant_spacing5, flt_configured_area5;
    int int_actual_population1;
    int int_actual_population2;
    int int_actual_population3;
    int int_actual_population4;
    int int_actual_population5;
    double actual_area = 0;
    List<SampleGerminationDatum> germinationDataList;

    Double oldPopulation = 0.0, oldGermination = 0.0, oldSpacingPtp = 0.0, oldSpacingRtr = 0.0;
    int listSIze = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    public AddGermiFullScreenDialog(List<SampleGerminationDatum> germinationDataList, Context context) {
        this.germinationDataList = germinationDataList;
        if (germinationDataList != null) {
            for (int i = 0; i < germinationDataList.size(); i++) {
                oldPopulation = oldPopulation + Double.valueOf(germinationDataList.get(i).getActualTotalPopulation());
                oldSpacingPtp = oldSpacingPtp + Double.valueOf(germinationDataList.get(i).getSpacingPtp());
                oldSpacingRtr = oldSpacingRtr + Double.valueOf(germinationDataList.get(i).getSpacingRtr());
                if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                    oldGermination = oldGermination + Double.valueOf(germinationDataList.get(i).getGermination());
                }
            }
            listSIze = germinationDataList.size();
            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                oldGermination = SharedPreferencesMethod.getDoubleSharedPreferences(context, SharedPreferencesMethod.GERMINATION) * germinationDataList.size();
                //Toasty.info(context, "Avg Germi " + oldGermination, Toast.LENGTH_LONG).show();
            }

            Log.e(TAG, "oldPopulation: " + oldPopulation + ", oldGermination:" + oldGermination + ", oldSpacingPtp:" + oldSpacingPtp + ", oldSpacingRtr" + oldSpacingRtr + ", listSIze:" + listSIze);
        }
    }
    AdView mAdView;
    private void loadAds(View view){
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = view.findViewById(R.id.adView);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_full_screen_dialog_add_germination, container, false);
        ButterKnife.bind(this, view);
        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(resources.getString(R.string.please_wait_msg));
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(view1 -> dismiss());
        toolbar.setTitleTextColor(resources.getColor(R.color.white));
        toolbar.setTitle(resources.getString(R.string.germination_label));
       /* TextView textView=view.findViewById(R.id.tittle_add_germi);
        textView.setText("Add Germination");*/
        loadAds(view);
        onClicks();
        CalculationforSample1();
        CalculationforSample2();
        CalculationforSample3();
        CalculationforSample4();
        CalculationforSample5();

        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            //Offline Mode
            FarmCacheManager.getInstance(context).getFarm(this::onFarmLoader, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));

        } else {

            if (DataHandler.newInstance().getFetchFarmResult() != null) {
                if (DataHandler.newInstance().getFetchFarmResult().getActualArea() != null) {
                    actual_area = Double.parseDouble(DataHandler.newInstance().getFetchFarmResult().getActualArea().toString());
                }
            } else {
                finishAllAndStartLanding();
            }
        }

        // actual_area=Double.parseDouble(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.ACTUAL_AREA));

        //Log.e(TAG,"Actual Area "+actual_area+"  Area 2 "+SharedPreferencesMethod.getString(context,SharedPreferencesMethod.ACTUAL_AREA));
        return view;
    }

    private void onClicks() {
        submit_germi_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckValidations2();
            }
        });

        add_more_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (germiCardCount == 1 && validateSample1()) {
                    animate(sampleLayout2, "add_more");
                } else if (germiCardCount == 2 && validateSample2()) {
                    animate(sampleLayout3, "add_more");
                } else if (germiCardCount == 3 && validateSample3()) {
                    animate(sampleLayout4, "add_more");
                } else if (germiCardCount == 4 && validateSample4()) {
                    animate(sampleLayout5, "add_more");
                }/* else if (germiCardCount == 5) {
                    animate(hidden_lin_germi, "add_more");
                }*/

            }
        });

        remove_img_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (germiCardCount == 5) {
                    animate(sampleLayout5, "add_more");
                    removeSample5();
                } else if (germiCardCount == 4) {
                    animate(sampleLayout4, "add_more");
                    removeSample4();
                } else if (germiCardCount == 3) {
                    animate(sampleLayout3, "add_more");
                    removeSample3();
                } else if (germiCardCount == 2) {
                    animate(sampleLayout2, "add_more");
                    removeSample2();
                } /*else if (germiCardCount == 5) {
                    animate(hidden_lin_germi, "add_more");
                }*/

            }
        });
    }

    private void CheckValidations2() {
        int count = 1;
        if (configured_area_et1.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_configured_of_sample1_msg), "1"), Snackbar.LENGTH_LONG);
            snackbar.show();
            configured_area_et1.setError(resources.getString(R.string.invalid_area_msg));
            configured_area_et1.getParent().requestChildFocus(configured_area_et1, configured_area_et1);
        } else if (plant_to_plant_spacing_et1.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_pp_spacing_of_sample1_msg), "1"), Snackbar.LENGTH_LONG);
            snackbar.show();
            plant_to_plant_spacing_et1.setError(resources.getString(R.string.invalid_pp_spacing_msg));
            plant_to_plant_spacing_et1.getParent().requestChildFocus(plant_to_plant_spacing_et1, plant_to_plant_spacing_et1);
        } else if (row_to_row_spacing_et1.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_rr_spacing_of_sample1_msg), "1"), Snackbar.LENGTH_LONG);
            snackbar.show();
            row_to_row_spacing_et1.setError(resources.getString(R.string.invalid_rr_spacing_msg));
            row_to_row_spacing_et1.getParent().requestChildFocus(row_to_row_spacing_et1, row_to_row_spacing_et1);
        } else if (actual_population_et1.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_actual_pop_of_sample1_msg), "1"), Snackbar.LENGTH_LONG);
            snackbar.show();
            actual_population_et1.setError(resources.getString(R.string.invalid_population_msg));
            actual_population_et1.getParent().requestChildFocus(actual_population_et1, actual_population_et1);
        } else if (germiCardCount > 1 && !validateSample2()/*!configured_area_et2.getText().toString().equals("") || !plant_to_plant_spacing_et2.getText().toString().equals("")
                || !row_to_row_spacing_et2.getText().toString().equals("") || !actual_population_et2.getText().toString().equals("")*/) {
            count = 2;


        } else if (germiCardCount > 2 && !validateSample3()/*!configured_area_et3.getText().toString().equals("") || !plant_to_plant_spacing_et3.getText().toString().equals("")
                || !row_to_row_spacing_et3.getText().toString().equals("") || !actual_population_et3.getText().toString().equals("")*/) {
            count = 3;


        } else if (germiCardCount > 3 && !validateSample4()/*!configured_area_et4.getText().toString().equals("") || !plant_to_plant_spacing_et4.getText().toString().equals("")
                || !row_to_row_spacing_et4.getText().toString().equals("") || !actual_population_et4.getText().toString().equals("")*/) {
            count = 4;


        } else if (germiCardCount > 4 && validateSample5()/*!configured_area_et5.getText().toString().equals("") || !plant_to_plant_spacing_et5.getText().toString().equals("")
                || !row_to_row_spacing_et5.getText().toString().equals("") || !actual_population_et5.getText().toString().equals("")*/) {
            count = 5;


        } else {
            //Toast.makeText(context, "Germi Count "+germiCardCount, Toast.LENGTH_SHORT).show();
            submitData("");
        }

    }

    private void CalculationforSample1() {
        configured_area_et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!plant_to_plant_spacing_et1.getText().toString().equals("") && !row_to_row_spacing_et1.getText().toString().equals("") && s.length() > 0 && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing1 = Double.parseDouble(plant_to_plant_spacing_et1.getText().toString());
                    flt_row_to_row_spacing1 = Double.parseDouble(row_to_row_spacing_et1.getText().toString());
                    flt_configured_area1 = Double.parseDouble(s.toString());
                    double ideal_area = (flt_configured_area1 * 144) / (flt_plant_to_plant_spacing1 * flt_row_to_row_spacing1);
                    ideal_population_tv1.setText(String.valueOf(String.format("%.2f", ideal_area)));

                    if (!actual_population_et1.getText().toString().equals("")) {
                        int_actual_population1 = Integer.parseInt(actual_population_et1.getText().toString().trim());
                        double ideal_population_from_tv = Double.parseDouble(ideal_population_tv1.getText().toString().trim());
                        double germination = int_actual_population1 / ideal_population_from_tv * 100.0;
                        double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing1 * flt_plant_to_plant_spacing1);

                        germiantion_tv1.setText(String.valueOf(String.format("%.2f", germination)));
                        full_popu_tv1.setText(String.valueOf(String.format("%.2f", full_population)));

                        if (germinationDataList != null) {
                            rtoravgCheckWhenPreviousGermiAdded();
                            ptopavgCheckWhenPreviousGermiAdded();
                            avgGermiCheckWhenPreviousGermiAdded();
                            avgFarmPopuCheckWhenPreviousGermiAdded();
                        } else {
                            rtoravgCheck();
                            ptopavgCheck();
                            avgGermiCheck();
                            avgFarmPopuCheck();
                        }
                    } else {
                        germiantion_tv1.setText(String.valueOf("0"));
                        full_popu_tv1.setText(String.valueOf("0"));

                    }

                } else {
                    ideal_population_tv1.setText("0");
                    germiantion_tv1.setText(String.valueOf("0"));
                    full_popu_tv1.setText(String.valueOf("0"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        row_to_row_spacing_et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!configured_area_et1.getText().toString().equals("") && !plant_to_plant_spacing_et1.getText().toString().equals("") && s.length() > 0 && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing1 = Double.parseDouble(plant_to_plant_spacing_et1.getText().toString());
                    flt_row_to_row_spacing1 = Double.parseDouble(s.toString());
                    flt_configured_area1 = Double.parseDouble(configured_area_et1.getText().toString());

                    double ideal_area = (flt_configured_area1 * 144) / (flt_plant_to_plant_spacing1 * flt_row_to_row_spacing1);
                    ideal_population_tv1.setText(String.valueOf(String.format("%.2f", ideal_area)));

                    if (!actual_population_et1.getText().toString().equals("")) {
                        int_actual_population1 = Integer.parseInt(actual_population_et1.getText().toString().trim());
                        double ideal_population_from_tv = Double.parseDouble(ideal_population_tv1.getText().toString().trim());
                        double germination = int_actual_population1 / ideal_population_from_tv * 100.0;
                        double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing1 * flt_plant_to_plant_spacing1);
                        germiantion_tv1.setText(String.valueOf(String.format("%.2f", germination)));
                        full_popu_tv1.setText(String.valueOf((int) Math.round(full_population)/*String.format("%.2f", full_population))*/));

                    } else {
                        germiantion_tv1.setText(String.valueOf("0"));
                        full_popu_tv1.setText(String.valueOf("0"));
                    }


                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }
                } else {
                    ideal_population_tv1.setText("0");
                    germiantion_tv1.setText(String.valueOf("0"));
                    full_popu_tv1.setText(String.valueOf("0"));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        plant_to_plant_spacing_et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!configured_area_et1.getText().toString().equals("") && !row_to_row_spacing_et1.getText().toString().equals("") && s.length() > 0 && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing1 = Double.parseDouble(s.toString().trim());
                    flt_row_to_row_spacing1 = Double.parseDouble(row_to_row_spacing_et1.getText().toString());
                    flt_configured_area1 = Double.parseDouble(configured_area_et1.getText().toString());
                    double ideal_area = (flt_configured_area1 * 144) / (flt_plant_to_plant_spacing1 * flt_row_to_row_spacing1);
                    ideal_population_tv1.setText(String.valueOf(String.format("%.2f", ideal_area)));


                    if (!actual_population_et1.getText().toString().equals("")) {
                        int_actual_population1 = Integer.parseInt(actual_population_et1.getText().toString().trim());
                        double ideal_population_from_tv = Double.parseDouble(ideal_population_tv1.getText().toString().trim());
                        double germination = int_actual_population1 / ideal_population_from_tv * 100.0;
                        double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing1 * flt_plant_to_plant_spacing1);
                        germiantion_tv1.setText(String.valueOf(String.format("%.2f", germination)));
                        full_popu_tv1.setText(String.valueOf(String.format("%.2f", full_population)));

                    } else {
                        germiantion_tv1.setText(String.valueOf("0"));
                        full_popu_tv1.setText(String.valueOf("0"));
                    }
                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }
                } else {
                    ideal_population_tv1.setText("0");
                    germiantion_tv1.setText(String.valueOf("0"));
                    full_popu_tv1.setText(String.valueOf("0"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        actual_population_et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!configured_area_et1.getText().toString().equals("") && !row_to_row_spacing_et1.getText().toString().equals("") && s.length() > 0 && !plant_to_plant_spacing_et1.getText().toString().equals("") && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing1 = Double.parseDouble(plant_to_plant_spacing_et1.getText().toString());
                    flt_row_to_row_spacing1 = Double.parseDouble(row_to_row_spacing_et1.getText().toString());
                    flt_configured_area1 = Double.parseDouble(configured_area_et1.getText().toString());

                    int_actual_population1 = Integer.parseInt(s.toString().trim());
                    double ideal_population_from_tv = Double.parseDouble(ideal_population_tv1.getText().toString().trim());
                    double germination = int_actual_population1 / ideal_population_from_tv * 100.0;
                    double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing1 * flt_plant_to_plant_spacing1);
                    Log.e(TAG, "Full Population 1 " + full_population);
                    germiantion_tv1.setText(String.valueOf(String.format("%.2f", germination)));
                    full_popu_tv1.setText(String.valueOf(String.format("%.2f", full_population)));
                    //Toasty.error(context,"Data "+String.valueOf(String.format("%.2f", full_population)),Toast.LENGTH_LONG,false);

                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }

                } else {
                    germiantion_tv1.setText(String.valueOf("0"));
                    full_popu_tv1.setText(String.valueOf("0"));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void CalculationforSample2() {
        configured_area_et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (!plant_to_plant_spacing_et2.getText().toString().equals("") && !row_to_row_spacing_et2.getText().toString().equals("") && s.length() > 0 && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing2 = Double.parseDouble(plant_to_plant_spacing_et2.getText().toString());
                    flt_row_to_row_spacing2 = Double.parseDouble(row_to_row_spacing_et2.getText().toString());
                    flt_configured_area2 = Double.parseDouble(s.toString());
                    double ideal_area = (flt_configured_area2 * 144) / (flt_plant_to_plant_spacing2 * flt_row_to_row_spacing2);
                    ideal_population_tv2.setText(String.valueOf(String.format("%.2f", ideal_area)));

                    if (!actual_population_et2.getText().toString().equals("")) {
                        int_actual_population2 = Integer.parseInt(actual_population_et2.getText().toString().trim());
                        double ideal_population_from_tv = Double.parseDouble(ideal_population_tv2.getText().toString().trim());
                        double germination = int_actual_population2 / ideal_population_from_tv * 100.0;
                        double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing2 * flt_plant_to_plant_spacing2);
                        germiantion_tv2.setText(String.valueOf(String.format("%.2f", germination)));
                        full_popu_tv2.setText(String.valueOf(String.format("%.2f", full_population)));
                    } else {
                        germiantion_tv2.setText(String.valueOf("0"));
                        full_popu_tv2.setText(String.valueOf("0"));
                    }

                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }

                } else {
                    ideal_population_tv2.setText("0");
                    germiantion_tv2.setText(String.valueOf("0"));
                    full_popu_tv2.setText(String.valueOf("0"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        row_to_row_spacing_et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!configured_area_et2.getText().toString().equals("") && !plant_to_plant_spacing_et2.getText().toString().equals("") && s.length() > 0 && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing2 = Double.parseDouble(plant_to_plant_spacing_et2.getText().toString());
                    flt_row_to_row_spacing2 = Double.parseDouble(s.toString());
                    flt_configured_area2 = Double.parseDouble(configured_area_et2.getText().toString());

                    double ideal_area = (flt_configured_area2 * 144) / (flt_plant_to_plant_spacing2 * flt_row_to_row_spacing2);
                    ideal_population_tv2.setText(String.valueOf(String.format("%.2f", ideal_area)));

                    if (!actual_population_et2.getText().toString().equals("")) {
                        int_actual_population2 = Integer.parseInt(actual_population_et2.getText().toString().trim());
                        double ideal_population_from_tv = Double.parseDouble(ideal_population_tv2.getText().toString().trim());
                        double germination = int_actual_population2 / ideal_population_from_tv * 100.0;
                        double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing2 * flt_plant_to_plant_spacing2);
                        germiantion_tv2.setText(String.valueOf(String.format("%.2f", germination)));
                        full_popu_tv2.setText(String.valueOf(String.format("%.2f", full_population)));

                    } else {
                        germiantion_tv2.setText(String.valueOf("0"));
                        full_popu_tv2.setText(String.valueOf("0"));
                    }
                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }
                } else {
                    ideal_population_tv2.setText("0");
                    germiantion_tv2.setText(String.valueOf("0"));
                    full_popu_tv2.setText(String.valueOf("0"));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        plant_to_plant_spacing_et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!configured_area_et2.getText().toString().equals("") && !row_to_row_spacing_et2.getText().toString().equals("") && s.length() > 0 && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing2 = Double.parseDouble(s.toString().trim());
                    flt_row_to_row_spacing2 = Double.parseDouble(row_to_row_spacing_et2.getText().toString());
                    flt_configured_area2 = Double.parseDouble(configured_area_et2.getText().toString());
                    double ideal_area = (flt_configured_area2 * 144) / (flt_plant_to_plant_spacing2 * flt_row_to_row_spacing2);
                    ideal_population_tv2.setText(String.valueOf(String.format("%.2f", ideal_area)));


                    if (!actual_population_et2.getText().toString().equals("")) {
                        int_actual_population2 = Integer.parseInt(actual_population_et2.getText().toString().trim());
                        double ideal_population_from_tv = Double.parseDouble(ideal_population_tv2.getText().toString().trim());
                        double germination = int_actual_population2 / ideal_population_from_tv * 100.0;
                        double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing2 * flt_plant_to_plant_spacing2);
                        germiantion_tv2.setText(String.valueOf(String.format("%.2f", germination)));
                        full_popu_tv2.setText(String.valueOf(String.format("%.2f", full_population)));

                    } else {
                        germiantion_tv2.setText(String.valueOf("0"));
                        full_popu_tv2.setText(String.valueOf("0"));
                    }
                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }
                } else {
                    ideal_population_tv2.setText("0");
                    germiantion_tv2.setText(String.valueOf("0"));
                    full_popu_tv2.setText(String.valueOf("0"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        actual_population_et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!configured_area_et2.getText().toString().equals("") && !row_to_row_spacing_et2.getText().toString().equals("") && s.length() > 0 && !plant_to_plant_spacing_et2.getText().toString().equals("") && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing2 = Double.parseDouble(plant_to_plant_spacing_et2.getText().toString());
                    flt_row_to_row_spacing2 = Double.parseDouble(row_to_row_spacing_et2.getText().toString());
                    flt_configured_area2 = Double.parseDouble(configured_area_et2.getText().toString());


                    int_actual_population2 = Integer.parseInt(s.toString().trim());
                    double ideal_population_from_tv = Double.parseDouble(ideal_population_tv2.getText().toString().trim());
                    double germination = int_actual_population2 / ideal_population_from_tv * 100.0;
                    double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing2 * flt_plant_to_plant_spacing2);

                    germiantion_tv2.setText(String.valueOf(String.format("%.2f", germination)));
                    full_popu_tv2.setText(String.valueOf(String.format("%.2f", full_population)));
                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }

                } else {
                    germiantion_tv2.setText(String.valueOf("0"));
                    full_popu_tv2.setText(String.valueOf("0"));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void CalculationforSample3() {
        configured_area_et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (!plant_to_plant_spacing_et3.getText().toString().equals("") && !row_to_row_spacing_et3.getText().toString().equals("") && s.length() > 0 && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing3 = Double.parseDouble(plant_to_plant_spacing_et3.getText().toString());
                    flt_row_to_row_spacing3 = Double.parseDouble(row_to_row_spacing_et3.getText().toString());
                    flt_configured_area3 = Double.parseDouble(s.toString());
                    double ideal_area = (flt_configured_area3 * 144) / (flt_plant_to_plant_spacing3 * flt_row_to_row_spacing3);
                    ideal_population_tv3.setText(String.valueOf(String.format("%.2f", ideal_area)));

                    if (!actual_population_et3.getText().toString().equals("")) {
                        int_actual_population3 = Integer.parseInt(actual_population_et3.getText().toString().trim());
                        double ideal_population_from_tv = Double.parseDouble(ideal_population_tv3.getText().toString().trim());
                        double germination = int_actual_population3 / ideal_population_from_tv * 100.0;
                        double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing3 * flt_plant_to_plant_spacing3);
                        germiantion_tv3.setText(String.valueOf(String.format("%.2f", germination)));
                        full_popu_tv3.setText(String.valueOf(String.format("%.2f", full_population)));
                    } else {
                        germiantion_tv3.setText(String.valueOf("0"));
                        full_popu_tv3.setText(String.valueOf("0"));
                    }

                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }
                } else {
                    ideal_population_tv3.setText("0");
                    germiantion_tv3.setText(String.valueOf("0"));
                    full_popu_tv3.setText(String.valueOf("0"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        row_to_row_spacing_et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!configured_area_et3.getText().toString().equals("") && !plant_to_plant_spacing_et3.getText().toString().equals("") && s.length() > 0 && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing3 = Double.parseDouble(plant_to_plant_spacing_et3.getText().toString());
                    flt_row_to_row_spacing3 = Double.parseDouble(s.toString());
                    flt_configured_area3 = Double.parseDouble(configured_area_et3.getText().toString());

                    double ideal_area = (flt_configured_area3 * 144) / (flt_plant_to_plant_spacing3 * flt_row_to_row_spacing3);
                    ideal_population_tv3.setText(String.valueOf(String.format("%.2f", ideal_area)));

                    if (!actual_population_et3.getText().toString().equals("")) {
                        int_actual_population3 = Integer.parseInt(actual_population_et3.getText().toString().trim());
                        double ideal_population_from_tv = Double.parseDouble(ideal_population_tv3.getText().toString().trim());
                        double germination = int_actual_population3 / ideal_population_from_tv * 100.0;
                        double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing3 * flt_plant_to_plant_spacing3);
                        germiantion_tv3.setText(String.valueOf(String.format("%.2f", germination)));
                        full_popu_tv3.setText(String.valueOf(String.format("%.2f", full_population)));

                    } else {
                        germiantion_tv3.setText(String.valueOf("0"));
                        full_popu_tv3.setText(String.valueOf("0"));
                    }
                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }
                } else {
                    ideal_population_tv3.setText("0");
                    germiantion_tv3.setText(String.valueOf("0"));
                    full_popu_tv3.setText(String.valueOf("0"));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        plant_to_plant_spacing_et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!configured_area_et3.getText().toString().equals("") && !row_to_row_spacing_et3.getText().toString().equals("") && s.length() > 0 && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing3 = Double.parseDouble(s.toString().trim());
                    flt_row_to_row_spacing3 = Double.parseDouble(row_to_row_spacing_et3.getText().toString());
                    flt_configured_area3 = Double.parseDouble(configured_area_et3.getText().toString());
                    double ideal_area = (flt_configured_area3 * 144) / (flt_plant_to_plant_spacing3 * flt_row_to_row_spacing3);
                    ideal_population_tv3.setText(String.valueOf(String.format("%.2f", ideal_area)));


                    if (!actual_population_et3.getText().toString().equals("")) {
                        int_actual_population3 = Integer.parseInt(actual_population_et3.getText().toString().trim());
                        double ideal_population_from_tv = Double.parseDouble(ideal_population_tv3.getText().toString().trim());
                        double germination = int_actual_population3 / ideal_population_from_tv * 100.0;
                        double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing3 * flt_plant_to_plant_spacing3);
                        germiantion_tv3.setText(String.valueOf(String.format("%.2f", germination)));
                        full_popu_tv3.setText(String.valueOf(String.format("%.2f", full_population)));

                    } else {
                        germiantion_tv3.setText(String.valueOf("0"));
                        full_popu_tv3.setText(String.valueOf("0"));
                    }

                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }
                } else {
                    ideal_population_tv3.setText("0");
                    germiantion_tv3.setText(String.valueOf("0"));
                    full_popu_tv3.setText(String.valueOf("0"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        actual_population_et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!configured_area_et3.getText().toString().equals("") && !row_to_row_spacing_et3.getText().toString().equals("") && s.length() > 0 && !plant_to_plant_spacing_et3.getText().toString().equals("") && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing3 = Double.parseDouble(plant_to_plant_spacing_et3.getText().toString());
                    flt_row_to_row_spacing3 = Double.parseDouble(row_to_row_spacing_et3.getText().toString());
                    flt_configured_area3 = Double.parseDouble(configured_area_et3.getText().toString());


                    int_actual_population3 = Integer.parseInt(s.toString().trim());
                    double ideal_population_from_tv = Double.parseDouble(ideal_population_tv3.getText().toString().trim());
                    double germination = int_actual_population3 / ideal_population_from_tv * 100.0;
                    double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing3 * flt_plant_to_plant_spacing3);

                    germiantion_tv3.setText(String.valueOf(String.format("%.2f", germination)));
                    full_popu_tv3.setText(String.valueOf(String.format("%.2f", full_population)));

                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }
                } else {
                    germiantion_tv3.setText(String.valueOf("0"));
                    full_popu_tv3.setText(String.valueOf("0"));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void CalculationforSample4() {
        configured_area_et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (!plant_to_plant_spacing_et4.getText().toString().equals("") && !row_to_row_spacing_et4.getText().toString().equals("") && s.length() > 0 && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing4 = Double.parseDouble(plant_to_plant_spacing_et4.getText().toString());
                    flt_row_to_row_spacing4 = Double.parseDouble(row_to_row_spacing_et4.getText().toString());
                    flt_configured_area4 = Double.parseDouble(s.toString());
                    double ideal_area = (flt_configured_area4 * 144) / (flt_plant_to_plant_spacing4 * flt_row_to_row_spacing4);
                    ideal_population_tv4.setText(String.valueOf(String.format("%.2f", ideal_area)));

                    if (!actual_population_et4.getText().toString().equals("")) {
                        int_actual_population4 = Integer.parseInt(actual_population_et4.getText().toString().trim());
                        double ideal_population_from_tv = Double.parseDouble(ideal_population_tv4.getText().toString().trim());
                        double germination = int_actual_population4 / ideal_population_from_tv * 100.0;
                        double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing4 * flt_plant_to_plant_spacing4);
                        germiantion_tv4.setText(String.valueOf(String.format("%.2f", germination)));
                        full_popu_tv4.setText(String.valueOf(String.format("%.2f", full_population)));
                    } else {
                        germiantion_tv4.setText(String.valueOf("0"));
                        full_popu_tv4.setText(String.valueOf("0"));
                    }
                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }
                } else {
                    ideal_population_tv4.setText("0");
                    germiantion_tv4.setText(String.valueOf("0"));
                    full_popu_tv4.setText(String.valueOf("0"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        row_to_row_spacing_et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!configured_area_et4.getText().toString().equals("") && !plant_to_plant_spacing_et4.getText().toString().equals("") && s.length() > 0 && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing4 = Double.parseDouble(plant_to_plant_spacing_et4.getText().toString());
                    flt_row_to_row_spacing4 = Double.parseDouble(s.toString());
                    flt_configured_area4 = Double.parseDouble(configured_area_et4.getText().toString());

                    double ideal_area = (flt_configured_area4 * 144) / (flt_plant_to_plant_spacing4 * flt_row_to_row_spacing4);
                    ideal_population_tv4.setText(String.valueOf(String.format("%.2f", ideal_area)));

                    if (!actual_population_et4.getText().toString().equals("")) {
                        int_actual_population4 = Integer.parseInt(actual_population_et4.getText().toString().trim());
                        double ideal_population_from_tv = Double.parseDouble(ideal_population_tv4.getText().toString().trim());
                        double germination = int_actual_population4 / ideal_population_from_tv * 100.0;
                        double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing4 * flt_plant_to_plant_spacing4);
                        germiantion_tv4.setText(String.valueOf(String.format("%.2f", germination)));
                        full_popu_tv4.setText(String.valueOf(String.format("%.2f", full_population)));

                    } else {
                        germiantion_tv4.setText(String.valueOf("0"));
                        full_popu_tv4.setText(String.valueOf("0"));
                    }

                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }
                } else {
                    ideal_population_tv4.setText("0");
                    germiantion_tv4.setText(String.valueOf("0"));
                    full_popu_tv4.setText(String.valueOf("0"));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        plant_to_plant_spacing_et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!configured_area_et4.getText().toString().equals("") && !row_to_row_spacing_et4.getText().toString().equals("") && s.length() > 0 && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing4 = Double.parseDouble(s.toString().trim());
                    flt_row_to_row_spacing4 = Double.parseDouble(row_to_row_spacing_et4.getText().toString());
                    flt_configured_area4 = Double.parseDouble(configured_area_et4.getText().toString());
                    double ideal_area = (flt_configured_area4 * 144) / (flt_plant_to_plant_spacing4 * flt_row_to_row_spacing4);
                    ideal_population_tv4.setText(String.valueOf(String.format("%.2f", ideal_area)));


                    if (!actual_population_et4.getText().toString().equals("")) {
                        int_actual_population4 = Integer.parseInt(actual_population_et4.getText().toString().trim());
                        double ideal_population_from_tv = Double.parseDouble(ideal_population_tv4.getText().toString().trim());
                        double germination = int_actual_population4 / ideal_population_from_tv * 100.0;
                        double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing4 * flt_plant_to_plant_spacing4);
                        germiantion_tv4.setText(String.valueOf(String.format("%.2f", germination)));
                        full_popu_tv4.setText(String.valueOf(String.format("%.2f", full_population)));

                    } else {
                        germiantion_tv4.setText(String.valueOf("0"));
                        full_popu_tv4.setText(String.valueOf("0"));
                    }

                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }
                } else {
                    ideal_population_tv4.setText("0");
                    germiantion_tv4.setText(String.valueOf("0"));
                    full_popu_tv4.setText(String.valueOf("0"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        actual_population_et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!configured_area_et4.getText().toString().equals("") && !row_to_row_spacing_et4.getText().toString().equals("") && s.length() > 0 && !plant_to_plant_spacing_et4.getText().toString().equals("") && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing4 = Double.parseDouble(plant_to_plant_spacing_et4.getText().toString());
                    flt_row_to_row_spacing4 = Double.parseDouble(row_to_row_spacing_et4.getText().toString());
                    flt_configured_area4 = Double.parseDouble(configured_area_et4.getText().toString());


                    int_actual_population4 = Integer.parseInt(s.toString().trim());
                    double ideal_population_from_tv = Double.parseDouble(ideal_population_tv4.getText().toString().trim());
                    double germination = int_actual_population4 / ideal_population_from_tv * 100.0;
                    double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing4 * flt_plant_to_plant_spacing4);

                    germiantion_tv4.setText(String.valueOf(String.format("%.2f", germination)));
                    full_popu_tv4.setText(String.valueOf(String.format("%.2f", full_population)));

                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }
                } else {
                    germiantion_tv4.setText(String.valueOf("0"));
                    full_popu_tv4.setText(String.valueOf("0"));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void CalculationforSample5() {
        configured_area_et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (!plant_to_plant_spacing_et5.getText().toString().equals("") && !row_to_row_spacing_et5.getText().toString().equals("") && s.length() > 0 && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing5 = Double.parseDouble(plant_to_plant_spacing_et5.getText().toString());
                    flt_row_to_row_spacing5 = Double.parseDouble(row_to_row_spacing_et5.getText().toString());
                    flt_configured_area5 = Double.parseDouble(s.toString());
                    double ideal_area = (flt_configured_area5 * 144) / (flt_plant_to_plant_spacing5 * flt_row_to_row_spacing5);
                    ideal_population_tv5.setText(String.valueOf(String.format("%.2f", ideal_area)));

                    if (!actual_population_et5.getText().toString().equals("")) {
                        int_actual_population5 = Integer.parseInt(actual_population_et5.getText().toString().trim());
                        double ideal_population_from_tv = Double.parseDouble(ideal_population_tv5.getText().toString().trim());
                        double germination = int_actual_population5 / ideal_population_from_tv * 100.0;
                        double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing5 * flt_plant_to_plant_spacing5);
                        germiantion_tv5.setText(String.valueOf(String.format("%.2f", germination)));
                        full_popu_tv5.setText(String.valueOf(String.format("%.2f", full_population)));
                    } else {
                        germiantion_tv5.setText(String.valueOf("0"));
                        full_popu_tv5.setText(String.valueOf("0"));
                    }

                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }

                } else {
                    ideal_population_tv5.setText("0");
                    germiantion_tv5.setText(String.valueOf("0"));
                    full_popu_tv5.setText(String.valueOf("0"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        row_to_row_spacing_et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!configured_area_et5.getText().toString().equals("") && !plant_to_plant_spacing_et5.getText().toString().equals("") && s.length() > 0 && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing5 = Double.parseDouble(plant_to_plant_spacing_et5.getText().toString());
                    flt_row_to_row_spacing5 = Double.parseDouble(s.toString());
                    flt_configured_area5 = Double.parseDouble(configured_area_et5.getText().toString());

                    double ideal_area = (flt_configured_area5 * 144) / (flt_plant_to_plant_spacing5 * flt_row_to_row_spacing5);
                    ideal_population_tv5.setText(String.valueOf(String.format("%.2f", ideal_area)));

                    if (!actual_population_et5.getText().toString().equals("")) {
                        int_actual_population5 = Integer.parseInt(actual_population_et5.getText().toString().trim());
                        double ideal_population_from_tv = Double.parseDouble(ideal_population_tv5.getText().toString().trim());
                        double germination = int_actual_population5 / ideal_population_from_tv * 100.0;
                        double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing5 * flt_plant_to_plant_spacing5);
                        germiantion_tv5.setText(String.valueOf(String.format("%.2f", germination)));
                        full_popu_tv5.setText(String.valueOf(String.format("%.2f", full_population)));

                    } else {
                        germiantion_tv5.setText(String.valueOf("0"));
                        full_popu_tv5.setText(String.valueOf("0"));
                    }

                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }
                } else {
                    ideal_population_tv5.setText("0");
                    germiantion_tv5.setText(String.valueOf("0"));
                    full_popu_tv5.setText(String.valueOf("0"));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        plant_to_plant_spacing_et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!configured_area_et5.getText().toString().equals("") && !row_to_row_spacing_et5.getText().toString().equals("") && s.length() > 0 && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing5 = Double.parseDouble(s.toString().trim());
                    flt_row_to_row_spacing5 = Double.parseDouble(row_to_row_spacing_et5.getText().toString());
                    flt_configured_area5 = Double.parseDouble(configured_area_et5.getText().toString());
                    double ideal_area = (flt_configured_area5 * 144) / (flt_plant_to_plant_spacing5 * flt_row_to_row_spacing5);
                    ideal_population_tv5.setText(String.valueOf(String.format("%.2f", ideal_area)));


                    if (!actual_population_et5.getText().toString().equals("")) {
                        int_actual_population5 = Integer.parseInt(actual_population_et5.getText().toString().trim());
                        double ideal_population_from_tv = Double.parseDouble(ideal_population_tv5.getText().toString().trim());
                        double germination = int_actual_population5 / ideal_population_from_tv * 100.0;
                        double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing5 * flt_plant_to_plant_spacing5);
                        germiantion_tv5.setText(String.valueOf(String.format("%.2f", germination)));
                        full_popu_tv5.setText(String.valueOf(String.format("%.2f", full_population)));

                    } else {
                        germiantion_tv5.setText(String.valueOf("0"));
                        full_popu_tv5.setText(String.valueOf("0"));
                    }
                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }

                } else {
                    ideal_population_tv5.setText("0");
                    germiantion_tv5.setText(String.valueOf("0"));
                    full_popu_tv5.setText(String.valueOf("0"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        actual_population_et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!configured_area_et5.getText().toString().equals("") && !row_to_row_spacing_et5.getText().toString().equals("") && s.length() > 0 && !plant_to_plant_spacing_et5.getText().toString().equals("") && !s.toString().trim().equals(".")) {
                    flt_plant_to_plant_spacing5 = Double.parseDouble(plant_to_plant_spacing_et5.getText().toString());
                    flt_row_to_row_spacing5 = Double.parseDouble(row_to_row_spacing_et5.getText().toString());
                    flt_configured_area5 = Double.parseDouble(configured_area_et5.getText().toString());


                    int_actual_population5 = Integer.parseInt(s.toString().trim());
                    double ideal_population_from_tv = Double.parseDouble(ideal_population_tv5.getText().toString().trim());
                    double germination = int_actual_population5 / ideal_population_from_tv * 100.0;
                    double full_population = (43560.0 * 144 * germination * actual_area / 100) / (flt_row_to_row_spacing5 * flt_plant_to_plant_spacing5);

                    germiantion_tv5.setText(String.valueOf(String.format("%.2f", germination)));
                    full_popu_tv5.setText(String.valueOf(String.format("%.2f", full_population)));

                    if (germinationDataList != null) {
                        rtoravgCheckWhenPreviousGermiAdded();
                        ptopavgCheckWhenPreviousGermiAdded();
                        avgGermiCheckWhenPreviousGermiAdded();
                        avgFarmPopuCheckWhenPreviousGermiAdded();
                    } else {
                        rtoravgCheck();
                        ptopavgCheck();
                        avgGermiCheck();
                        avgFarmPopuCheck();
                    }

                } else {
                    germiantion_tv5.setText(String.valueOf("0"));
                    full_popu_tv5.setText(String.valueOf("0"));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private boolean validateSample1() {
        boolean flag = false;
        if (configured_area_et1.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_configured_of_sample1_msg), "1"), Snackbar.LENGTH_LONG);
            snackbar.show();
            configured_area_et1.setError(resources.getString(R.string.invalid_area_msg));
            configured_area_et1.getParent().requestChildFocus(configured_area_et1, configured_area_et1);
            flag = false;
        } else if (plant_to_plant_spacing_et1.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_pp_spacing_of_sample1_msg), "1"), Snackbar.LENGTH_LONG);
            snackbar.show();
            plant_to_plant_spacing_et1.setError(resources.getString(R.string.invalid_pp_spacing_msg));
            plant_to_plant_spacing_et1.getParent().requestChildFocus(plant_to_plant_spacing_et1, plant_to_plant_spacing_et1);
            flag = false;
        } else if (row_to_row_spacing_et1.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_rr_spacing_of_sample1_msg), "1"), Snackbar.LENGTH_LONG);
            snackbar.show();
            row_to_row_spacing_et1.setError(resources.getString(R.string.invalid_rr_spacing_msg));
            row_to_row_spacing_et1.getParent().requestChildFocus(row_to_row_spacing_et1, row_to_row_spacing_et1);
            flag = false;
        } else if (actual_population_et1.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_actual_pop_of_sample1_msg), "1"), Snackbar.LENGTH_LONG);
            snackbar.show();
            actual_population_et1.setError(resources.getString(R.string.invalid_population_msg));
            actual_population_et1.getParent().requestChildFocus(actual_population_et1, actual_population_et1);
            flag = false;
        } else {
            flag = true;
        }
        return flag;
    }

    private boolean validateSample2() {
        boolean flag = false;
        if (configured_area_et2.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_configured_of_sample1_msg), "2"), Snackbar.LENGTH_LONG);
            snackbar.show();
            configured_area_et2.setError(resources.getString(R.string.invalid_area_msg));
            configured_area_et2.getParent().requestChildFocus(configured_area_et2, configured_area_et2);
            flag = false;
        } else if (plant_to_plant_spacing_et2.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_pp_spacing_of_sample1_msg), "2"), Snackbar.LENGTH_LONG);
            snackbar.show();
            plant_to_plant_spacing_et2.setError(resources.getString(R.string.invalid_pp_spacing_msg));
            plant_to_plant_spacing_et2.getParent().requestChildFocus(plant_to_plant_spacing_et2, plant_to_plant_spacing_et2);
            flag = false;
        } else if (row_to_row_spacing_et2.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_rr_spacing_of_sample1_msg), "2"), Snackbar.LENGTH_LONG);
            snackbar.show();
            row_to_row_spacing_et2.setError(resources.getString(R.string.invalid_rr_spacing_msg));
            row_to_row_spacing_et2.getParent().requestChildFocus(row_to_row_spacing_et2, row_to_row_spacing_et2);
            flag = false;
        } else if (actual_population_et2.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_actual_pop_of_sample1_msg), "2"), Snackbar.LENGTH_LONG);
            snackbar.show();
            actual_population_et2.setError(resources.getString(R.string.invalid_population_msg));
            actual_population_et2.getParent().requestChildFocus(actual_population_et2, actual_population_et2);
            flag = false;
        } else {
            flag = true;
        }
        return flag;
    }

    private boolean validateSample3() {
        boolean flag = false;
        if (configured_area_et3.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_configured_of_sample1_msg), "3"), Snackbar.LENGTH_LONG);
            snackbar.show();
            configured_area_et3.setError(resources.getString(R.string.invalid_area_msg));
            configured_area_et3.getParent().requestChildFocus(configured_area_et3, configured_area_et3);
            flag = false;
        } else if (plant_to_plant_spacing_et3.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_pp_spacing_of_sample1_msg), "3"), Snackbar.LENGTH_LONG);
            snackbar.show();
            plant_to_plant_spacing_et3.setError(resources.getString(R.string.invalid_pp_spacing_msg));
            plant_to_plant_spacing_et3.getParent().requestChildFocus(plant_to_plant_spacing_et3, plant_to_plant_spacing_et3);
            flag = false;
        } else if (row_to_row_spacing_et3.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_rr_spacing_of_sample1_msg), "3"), Snackbar.LENGTH_LONG);
            snackbar.show();
            row_to_row_spacing_et3.setError(resources.getString(R.string.invalid_rr_spacing_msg));
            row_to_row_spacing_et3.getParent().requestChildFocus(row_to_row_spacing_et3, row_to_row_spacing_et3);
            flag = false;
        } else if (actual_population_et3.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_actual_pop_of_sample1_msg), "3"), Snackbar.LENGTH_LONG);
            snackbar.show();
            actual_population_et3.setError(resources.getString(R.string.invalid_population_msg));
            actual_population_et3.getParent().requestChildFocus(actual_population_et3, actual_population_et3);
            flag = false;
        } else {
            flag = true;
        }
        return flag;
    }

    private boolean validateSample4() {
        boolean flag = false;
        if (configured_area_et4.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_configured_of_sample1_msg), "4"), Snackbar.LENGTH_LONG);
            snackbar.show();
            configured_area_et4.setError(resources.getString(R.string.invalid_area_msg));
            configured_area_et4.getParent().requestChildFocus(configured_area_et4, configured_area_et4);
            flag = false;
        } else if (plant_to_plant_spacing_et4.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_pp_spacing_of_sample1_msg), "4"), Snackbar.LENGTH_LONG);
            snackbar.show();
            plant_to_plant_spacing_et4.setError(resources.getString(R.string.invalid_pp_spacing_msg));
            plant_to_plant_spacing_et4.getParent().requestChildFocus(plant_to_plant_spacing_et4, plant_to_plant_spacing_et4);
            flag = false;
        } else if (row_to_row_spacing_et4.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_rr_spacing_of_sample1_msg), "4"), Snackbar.LENGTH_LONG);
            snackbar.show();
            row_to_row_spacing_et4.setError(resources.getString(R.string.invalid_rr_spacing_msg));
            row_to_row_spacing_et4.getParent().requestChildFocus(row_to_row_spacing_et4, row_to_row_spacing_et4);
            flag = false;
        } else if (actual_population_et4.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_actual_pop_of_sample1_msg), "4"), Snackbar.LENGTH_LONG);
            snackbar.show();
            actual_population_et4.setError(resources.getString(R.string.invalid_population_msg));
            actual_population_et4.getParent().requestChildFocus(actual_population_et4, actual_population_et4);
            flag = false;
        } else {
            flag = true;
        }
        return flag;
    }

    private boolean validateSample5() {
        boolean flag = false;
        if (configured_area_et5.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_configured_of_sample1_msg), "5"), Snackbar.LENGTH_LONG);
            snackbar.show();
            configured_area_et5.setError(resources.getString(R.string.invalid_area_msg));
            configured_area_et5.getParent().requestChildFocus(configured_area_et5, configured_area_et5);
            flag = false;
        } else if (plant_to_plant_spacing_et5.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_pp_spacing_of_sample1_msg), "5"), Snackbar.LENGTH_LONG);
            snackbar.show();
            plant_to_plant_spacing_et5.setError(resources.getString(R.string.invalid_pp_spacing_msg));
            plant_to_plant_spacing_et5.getParent().requestChildFocus(plant_to_plant_spacing_et5, plant_to_plant_spacing_et5);
            flag = false;
        } else if (row_to_row_spacing_et5.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_rr_spacing_of_sample1_msg), "5"), Snackbar.LENGTH_LONG);
            snackbar.show();
            row_to_row_spacing_et5.setError(resources.getString(R.string.invalid_rr_spacing_msg));
            row_to_row_spacing_et5.getParent().requestChildFocus(row_to_row_spacing_et5, row_to_row_spacing_et5);
            flag = false;
        } else if (actual_population_et5.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(germi_linear_layout, String.format(resources.getString(R.string.enter_actual_pop_of_sample1_msg), "5"), Snackbar.LENGTH_LONG);
            snackbar.show();
            actual_population_et5.setError(resources.getString(R.string.invalid_population_msg));
            actual_population_et5.getParent().requestChildFocus(actual_population_et5, actual_population_et5);
            flag = false;
        } else {
            flag = true;
        }
        return flag;
    }

    private void removeSample5() {
        configured_area_et5.setText("");
        plant_to_plant_spacing_et5.setText("");
        row_to_row_spacing_et5.setText("");
        actual_population_et5.setText("");
        if (germinationDataList != null) {
            rtoravgCheckWhenPreviousGermiAdded();
            ptopavgCheckWhenPreviousGermiAdded();
            avgGermiCheckWhenPreviousGermiAdded();
            avgFarmPopuCheckWhenPreviousGermiAdded();
        } else {
            rtoravgCheck();
            ptopavgCheck();
            avgGermiCheck();
            avgFarmPopuCheck();
        }

    }

    private void removeSample4() {
        configured_area_et4.setText("");
        plant_to_plant_spacing_et4.setText("");
        row_to_row_spacing_et4.setText("");
        actual_population_et4.setText("");
        if (germinationDataList != null) {
            rtoravgCheckWhenPreviousGermiAdded();
            ptopavgCheckWhenPreviousGermiAdded();
            avgGermiCheckWhenPreviousGermiAdded();
            avgFarmPopuCheckWhenPreviousGermiAdded();
        } else {
            rtoravgCheck();
            ptopavgCheck();
            avgGermiCheck();
            avgFarmPopuCheck();
        }

    }

    private void removeSample3() {
        configured_area_et3.setText("");
        plant_to_plant_spacing_et3.setText("");
        row_to_row_spacing_et3.setText("");
        actual_population_et3.setText("");
        if (germinationDataList != null) {
            rtoravgCheckWhenPreviousGermiAdded();
            ptopavgCheckWhenPreviousGermiAdded();
            avgGermiCheckWhenPreviousGermiAdded();
            avgFarmPopuCheckWhenPreviousGermiAdded();
        } else {
            rtoravgCheck();
            ptopavgCheck();
            avgGermiCheck();
            avgFarmPopuCheck();
        }

    }

    private void removeSample2() {
        configured_area_et2.setText("");
        plant_to_plant_spacing_et2.setText("");
        row_to_row_spacing_et2.setText("");
        actual_population_et2.setText("");
        if (germinationDataList != null) {
            rtoravgCheckWhenPreviousGermiAdded();
            ptopavgCheckWhenPreviousGermiAdded();
            avgGermiCheckWhenPreviousGermiAdded();
            avgFarmPopuCheckWhenPreviousGermiAdded();
        } else {
            rtoravgCheck();
            ptopavgCheck();
            avgGermiCheck();
            avgFarmPopuCheck();
        }

    }


    int germiCardCount = 1;

    public void animate(final LinearLayout linearLayout, String from_where) {

        //LinearLayout dialog   = (LinearLayout)findViewById(R.id.my_layout);
        if (from_where.equals("add_more")) {
            if (linearLayout.getVisibility() == View.GONE) {
                //add_more_img.setBackgroundResource(R.drawable.ic_remove_circle_outline_dark_grey_24dp);
                linearLayout.setVisibility(LinearLayout.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_down);
                animation.setDuration(1000);
                linearLayout.setAnimation(animation);
                linearLayout.animate();
                animation.start();
                germiCardCount = germiCardCount + 1;
                if (germiCardCount > 1) {
                    remove_img_butt.setVisibility(View.VISIBLE);
                }
                if (germiCardCount > 4) {
                    add_more_img.setVisibility(View.GONE);
                }
            } else {
                // add_more_img.setBackgroundResource(R.drawable.ic_add_circle_outline_dark_grey_black_24dp);
                linearLayout.setVisibility(LinearLayout.INVISIBLE);
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                animation.setDuration(1000);
                linearLayout.setAnimation(animation);
                linearLayout.animate();
                animation.start();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        linearLayout.setVisibility(LinearLayout.GONE);
                        germiCardCount = germiCardCount - 1;
                        if (germiCardCount == 1) {
                            remove_img_butt.setVisibility(View.GONE);
                        }
                        if (germiCardCount < 5) {
                            add_more_img.setVisibility(View.VISIBLE);
                        }
                    }
                }, 1000);
            }
        }

    }

    public void ptopavgCheck() {
        if (!plant_to_plant_spacing_et1.getText().toString().equals("") && !plant_to_plant_spacing_et2.getText().toString().equals("") && !plant_to_plant_spacing_et3.getText().toString().equals("") &&
                !plant_to_plant_spacing_et4.getText().toString().equals("") && !plant_to_plant_spacing_et5.getText().toString().equals("")) {
            avg_pp_spacing_tv.setText(String.valueOf(String.format("%.2f", (Double.parseDouble(plant_to_plant_spacing_et1.getText().toString()) + Double.parseDouble(plant_to_plant_spacing_et2.getText().toString()) +
                    Double.parseDouble(plant_to_plant_spacing_et3.getText().toString()) + Double.parseDouble(plant_to_plant_spacing_et4.getText().toString()) +
                    Double.parseDouble(plant_to_plant_spacing_et5.getText().toString())) / 5)));
        } else if (!plant_to_plant_spacing_et1.getText().toString().equals("") && !plant_to_plant_spacing_et2.getText().toString().equals("") && !plant_to_plant_spacing_et3.getText().toString().equals("") &&
                !plant_to_plant_spacing_et4.getText().toString().equals("")) {
            avg_pp_spacing_tv.setText(String.valueOf(String.format("%.2f", (Double.parseDouble(plant_to_plant_spacing_et1.getText().toString()) + Double.parseDouble(plant_to_plant_spacing_et2.getText().toString()) +
                    Double.parseDouble(plant_to_plant_spacing_et3.getText().toString()) + Double.parseDouble(plant_to_plant_spacing_et4.getText().toString())) / 4)));
        } else if (!plant_to_plant_spacing_et1.getText().toString().equals("") && !plant_to_plant_spacing_et2.getText().toString().equals("") && !plant_to_plant_spacing_et3.getText().toString().equals("")
        ) {
            avg_pp_spacing_tv.setText(String.valueOf(String.format("%.2f", (Double.parseDouble(plant_to_plant_spacing_et1.getText().toString()) + Double.parseDouble(plant_to_plant_spacing_et2.getText().toString()) +
                    Double.parseDouble(plant_to_plant_spacing_et3.getText().toString())) / 3)));
        } else if (!plant_to_plant_spacing_et1.getText().toString().equals("") && !plant_to_plant_spacing_et2.getText().toString().equals("")
        ) {
            avg_pp_spacing_tv.setText(String.valueOf(String.format("%.2f", (Double.parseDouble(plant_to_plant_spacing_et1.getText().toString()) +
                    Double.parseDouble(plant_to_plant_spacing_et2.getText().toString())) / 2)));
        } else {
            avg_pp_spacing_tv.setText(String.valueOf(String.format("%.2f", Double.parseDouble(plant_to_plant_spacing_et1.getText().toString()))));
        }
    }

    public void rtoravgCheck() {

        if (!row_to_row_spacing_et1.getText().toString().equals("") && !row_to_row_spacing_et2.getText().toString().equals("") && !row_to_row_spacing_et3.getText().toString().equals("") &&
                !row_to_row_spacing_et4.getText().toString().equals("") && !row_to_row_spacing_et5.getText().toString().equals("")) {
            avg_rr_spacing_tv.setText(String.valueOf(String.format("%.2f", (Double.parseDouble(row_to_row_spacing_et1.getText().toString()) + Double.parseDouble(row_to_row_spacing_et2.getText().toString()) +
                    Double.parseDouble(row_to_row_spacing_et3.getText().toString()) + Double.parseDouble(row_to_row_spacing_et4.getText().toString()) +
                    Double.parseDouble(row_to_row_spacing_et5.getText().toString())) / 5)));
        } else if (!row_to_row_spacing_et1.getText().toString().equals("") && !row_to_row_spacing_et2.getText().toString().equals("") && !row_to_row_spacing_et3.getText().toString().equals("") &&
                !row_to_row_spacing_et4.getText().toString().equals("")) {
            avg_rr_spacing_tv.setText(String.valueOf(String.format("%.2f", (Double.parseDouble(row_to_row_spacing_et1.getText().toString()) + Double.parseDouble(row_to_row_spacing_et2.getText().toString()) +
                    Double.parseDouble(row_to_row_spacing_et3.getText().toString()) + Double.parseDouble(row_to_row_spacing_et4.getText().toString())) / 4)));
        } else if (!row_to_row_spacing_et1.getText().toString().equals("") && !row_to_row_spacing_et2.getText().toString().equals("") && !row_to_row_spacing_et3.getText().toString().equals("")
        ) {
            avg_rr_spacing_tv.setText(String.valueOf(String.format("%.2f", (Double.parseDouble(row_to_row_spacing_et1.getText().toString()) + Double.parseDouble(row_to_row_spacing_et2.getText().toString()) +
                    Double.parseDouble(row_to_row_spacing_et3.getText().toString())) / 3)));
        } else if (!row_to_row_spacing_et1.getText().toString().equals("") && !row_to_row_spacing_et2.getText().toString().equals("")
        ) {
            avg_rr_spacing_tv.setText(String.valueOf(String.format("%.2f", (Double.parseDouble(row_to_row_spacing_et1.getText().toString())
                    + Double.parseDouble(row_to_row_spacing_et2.getText().toString())) / 2)));
        } else {
            avg_rr_spacing_tv.setText(String.valueOf(String.format("%.2f", Double.parseDouble(row_to_row_spacing_et1.getText().toString()))));
        }
    }

    public void avgGermiCheck() {
        if (!germiantion_tv1.getText().toString().equals("0") && !germiantion_tv2.getText().toString().equals("0") && !germiantion_tv3.getText().toString().equals("0") &&
                !germiantion_tv4.getText().toString().equals("0") && !germiantion_tv5.getText().toString().equals("0")) {
            avg_germi_tv.setText(String.valueOf(String.format("%.2f", (Double.parseDouble(germiantion_tv1.getText().toString()) + Double.parseDouble(germiantion_tv2.getText().toString()) +
                    Double.parseDouble(germiantion_tv3.getText().toString()) + Double.parseDouble(germiantion_tv4.getText().toString()) +
                    Double.parseDouble(germiantion_tv5.getText().toString())) / 5)));
        } else if (!germiantion_tv1.getText().toString().equals("0") && !germiantion_tv2.getText().toString().equals("0") && !germiantion_tv3.getText().toString().equals("0") &&
                !germiantion_tv4.getText().toString().equals("0")) {
            avg_germi_tv.setText(String.valueOf(String.format("%.2f", (Double.parseDouble(germiantion_tv1.getText().toString()) + Double.parseDouble(germiantion_tv2.getText().toString()) +
                    Double.parseDouble(germiantion_tv3.getText().toString()) + Double.parseDouble(germiantion_tv4.getText().toString())) / 4)));
        } else if (!germiantion_tv1.getText().toString().equals("0") && !germiantion_tv2.getText().toString().equals("0") && !germiantion_tv3.getText().toString().equals("0")
        ) {
            avg_germi_tv.setText(String.valueOf(String.format("%.2f", (Double.parseDouble(germiantion_tv1.getText().toString()) + Double.parseDouble(germiantion_tv2.getText().toString()) +
                    Double.parseDouble(germiantion_tv3.getText().toString())) / 3)));
        } else if (!germiantion_tv1.getText().toString().equals("0") && !germiantion_tv2.getText().toString().equals("0")
        ) {
            avg_germi_tv.setText(String.valueOf(String.format("%.2f", (Double.parseDouble(germiantion_tv1.getText().toString()) + Double.parseDouble(germiantion_tv2.getText().toString())) / 2)));
        } else {
            avg_germi_tv.setText(String.valueOf(String.format("%.2f", Double.parseDouble(germiantion_tv1.getText().toString()))));
        }
    }

    public void avgFarmPopuCheck() {
        if (!full_popu_tv1.getText().toString().equals("0") && !full_popu_tv2.getText().toString().equals("0") && !full_popu_tv3.getText().toString().equals("0") &&
                !full_popu_tv4.getText().toString().equals("0") && !full_popu_tv5.getText().toString().equals("0")) {
            avg_farm_popu_tv.setText(String.valueOf(String.format("%.2f", (Double.parseDouble(full_popu_tv1.getText().toString()) + Double.parseDouble(full_popu_tv2.getText().toString()) +
                    Double.parseDouble(full_popu_tv3.getText().toString()) + Double.parseDouble(full_popu_tv4.getText().toString()) +
                    Double.parseDouble(full_popu_tv5.getText().toString())) / 5)));
        } else if (!full_popu_tv1.getText().toString().equals("0") && !full_popu_tv2.getText().toString().equals("0") && !full_popu_tv3.getText().toString().equals("0") &&
                !full_popu_tv4.getText().toString().equals("0")) {
            avg_farm_popu_tv.setText(String.valueOf(String.format("%.2f", (Double.parseDouble(full_popu_tv1.getText().toString()) + Double.parseDouble(full_popu_tv2.getText().toString()) +
                    Double.parseDouble(full_popu_tv3.getText().toString()) + Double.parseDouble(full_popu_tv4.getText().toString())) / 4)));
        } else if (!full_popu_tv1.getText().toString().equals("0") && !full_popu_tv2.getText().toString().equals("0") && !full_popu_tv3.getText().toString().equals("0")
        ) {
            avg_farm_popu_tv.setText(String.valueOf(String.format("%.2f", (Double.parseDouble(full_popu_tv1.getText().toString()) + Double.parseDouble(full_popu_tv2.getText().toString()) +
                    Double.parseDouble(full_popu_tv3.getText().toString())) / 3)));
        } else if (!full_popu_tv1.getText().toString().equals("0") && !full_popu_tv2.getText().toString().equals("0")
        ) {
            avg_farm_popu_tv.setText(String.valueOf(String.format("%.2f", (Double.parseDouble(full_popu_tv1.getText().toString()) + Double.parseDouble(full_popu_tv2.getText().toString())) / 2)));
        } else {
            avg_farm_popu_tv.setText(String.valueOf(String.format("%.2f", Double.parseDouble(full_popu_tv1.getText().toString()))));
        }
    }

    public void ptopavgCheckWhenPreviousGermiAdded() {


        if (!plant_to_plant_spacing_et1.getText().toString().equals("") && !plant_to_plant_spacing_et2.getText().toString().equals("") && !plant_to_plant_spacing_et3.getText().toString().equals("") &&
                !plant_to_plant_spacing_et4.getText().toString().equals("") && !plant_to_plant_spacing_et5.getText().toString().equals("")) {
            int value = listSIze + 5;
            avg_pp_spacing_tv.setText(String.valueOf(String.format("%.2f", (oldSpacingPtp + Double.parseDouble(plant_to_plant_spacing_et1.getText().toString()) + Double.parseDouble(plant_to_plant_spacing_et2.getText().toString()) +
                    Double.parseDouble(plant_to_plant_spacing_et3.getText().toString()) + Double.parseDouble(plant_to_plant_spacing_et4.getText().toString()) +
                    Double.parseDouble(plant_to_plant_spacing_et5.getText().toString())) / value)));
        } else if (!plant_to_plant_spacing_et1.getText().toString().equals("") && !plant_to_plant_spacing_et2.getText().toString().equals("") && !plant_to_plant_spacing_et3.getText().toString().equals("") &&
                !plant_to_plant_spacing_et4.getText().toString().equals("")) {
            int value = listSIze + 4;
            avg_pp_spacing_tv.setText(String.valueOf(String.format("%.2f", (oldSpacingPtp + Double.parseDouble(plant_to_plant_spacing_et1.getText().toString()) + Double.parseDouble(plant_to_plant_spacing_et2.getText().toString()) +
                    Double.parseDouble(plant_to_plant_spacing_et3.getText().toString()) + Double.parseDouble(plant_to_plant_spacing_et4.getText().toString())) / value)));
        } else if (!plant_to_plant_spacing_et1.getText().toString().equals("") && !plant_to_plant_spacing_et2.getText().toString().equals("") && !plant_to_plant_spacing_et3.getText().toString().equals("")
        ) {
            int value = listSIze + 3;
            avg_pp_spacing_tv.setText(String.valueOf(String.format("%.2f", (oldSpacingPtp + Double.parseDouble(plant_to_plant_spacing_et1.getText().toString()) + Double.parseDouble(plant_to_plant_spacing_et2.getText().toString()) +
                    Double.parseDouble(plant_to_plant_spacing_et3.getText().toString())) / value)));
        } else if (!plant_to_plant_spacing_et1.getText().toString().equals("") && !plant_to_plant_spacing_et2.getText().toString().equals("")
        ) {
            int value = listSIze + 2;
            avg_pp_spacing_tv.setText(String.valueOf(String.format("%.2f", (oldSpacingPtp + Double.parseDouble(plant_to_plant_spacing_et1.getText().toString()) +
                    Double.parseDouble(plant_to_plant_spacing_et2.getText().toString())) / value)));
        } else {
            int value = listSIze + 1;
            avg_pp_spacing_tv.setText(String.valueOf(String.format("%.2f", (oldSpacingPtp + Double.parseDouble(plant_to_plant_spacing_et1.getText().toString())) / value)));
        }
    }

    public void rtoravgCheckWhenPreviousGermiAdded() {

        if (!row_to_row_spacing_et1.getText().toString().equals("") && !row_to_row_spacing_et2.getText().toString().equals("") && !row_to_row_spacing_et3.getText().toString().equals("") &&
                !row_to_row_spacing_et4.getText().toString().equals("") && !row_to_row_spacing_et5.getText().toString().equals("")) {
            int value = listSIze + 5;
            avg_rr_spacing_tv.setText(String.valueOf(String.format("%.2f", (oldSpacingRtr + Double.parseDouble(row_to_row_spacing_et1.getText().toString()) + Double.parseDouble(row_to_row_spacing_et2.getText().toString()) +
                    Double.parseDouble(row_to_row_spacing_et3.getText().toString()) + Double.parseDouble(row_to_row_spacing_et4.getText().toString()) +
                    Double.parseDouble(row_to_row_spacing_et5.getText().toString())) / value)));
        } else if (!row_to_row_spacing_et1.getText().toString().equals("") && !row_to_row_spacing_et2.getText().toString().equals("") && !row_to_row_spacing_et3.getText().toString().equals("") &&
                !row_to_row_spacing_et4.getText().toString().equals("")) {
            int value = listSIze + 4;
            avg_rr_spacing_tv.setText(String.valueOf(String.format("%.2f", (oldSpacingRtr + Double.parseDouble(row_to_row_spacing_et1.getText().toString()) + Double.parseDouble(row_to_row_spacing_et2.getText().toString()) +
                    Double.parseDouble(row_to_row_spacing_et3.getText().toString()) + Double.parseDouble(row_to_row_spacing_et4.getText().toString())) / value)));
        } else if (!row_to_row_spacing_et1.getText().toString().equals("") && !row_to_row_spacing_et2.getText().toString().equals("") && !row_to_row_spacing_et3.getText().toString().equals("")
        ) {
            int value = listSIze + 3;
            avg_rr_spacing_tv.setText(String.valueOf(String.format("%.2f", (oldSpacingRtr + Double.parseDouble(row_to_row_spacing_et1.getText().toString()) + Double.parseDouble(row_to_row_spacing_et2.getText().toString()) +
                    Double.parseDouble(row_to_row_spacing_et3.getText().toString())) / value)));
        } else if (!row_to_row_spacing_et1.getText().toString().equals("") && !row_to_row_spacing_et2.getText().toString().equals("")
        ) {
            int value = listSIze + 2;
            avg_rr_spacing_tv.setText(String.valueOf(String.format("%.2f", (oldSpacingRtr + Double.parseDouble(row_to_row_spacing_et1.getText().toString())
                    + Double.parseDouble(row_to_row_spacing_et2.getText().toString())) / value)));
        } else {
            int value = listSIze + 1;
            avg_rr_spacing_tv.setText(String.valueOf(String.format("%.2f", (oldSpacingRtr + Double.parseDouble(row_to_row_spacing_et1.getText().toString())) / value)));
        }
    }


    public void avgGermiCheckWhenPreviousGermiAdded() {
        if (!germiantion_tv1.getText().toString().equals("0") && !germiantion_tv2.getText().toString().equals("0") && !germiantion_tv3.getText().toString().equals("0") &&
                !germiantion_tv4.getText().toString().equals("0") && !germiantion_tv5.getText().toString().equals("0")) {
            int value = listSIze + 5;
            avg_germi_tv.setText(String.valueOf(String.format("%.2f", (oldGermination + Double.parseDouble(germiantion_tv1.getText().toString()) + Double.parseDouble(germiantion_tv2.getText().toString()) +
                    Double.parseDouble(germiantion_tv3.getText().toString()) + Double.parseDouble(germiantion_tv4.getText().toString()) +
                    Double.parseDouble(germiantion_tv5.getText().toString())) / value)));
        } else if (!germiantion_tv1.getText().toString().equals("0") && !germiantion_tv2.getText().toString().equals("0") && !germiantion_tv3.getText().toString().equals("0") &&
                !germiantion_tv4.getText().toString().equals("0")) {
            int value = listSIze + 4;
            avg_germi_tv.setText(String.valueOf(String.format("%.2f", (oldGermination + Double.parseDouble(germiantion_tv1.getText().toString()) + Double.parseDouble(germiantion_tv2.getText().toString()) +
                    Double.parseDouble(germiantion_tv3.getText().toString()) + Double.parseDouble(germiantion_tv4.getText().toString())) / value)));
        } else if (!germiantion_tv1.getText().toString().equals("0") && !germiantion_tv2.getText().toString().equals("0") && !germiantion_tv3.getText().toString().equals("0")
        ) {
            int value = listSIze + 3;
            avg_germi_tv.setText(String.valueOf(String.format("%.2f", (oldGermination + Double.parseDouble(germiantion_tv1.getText().toString()) + Double.parseDouble(germiantion_tv2.getText().toString()) +
                    Double.parseDouble(germiantion_tv3.getText().toString())) / value)));
        } else if (!germiantion_tv1.getText().toString().equals("0") && !germiantion_tv2.getText().toString().equals("0")
        ) {
            int value = listSIze + 2;
            avg_germi_tv.setText(String.valueOf(String.format("%.2f", (oldGermination + Double.parseDouble(germiantion_tv1.getText().toString()) + Double.parseDouble(germiantion_tv2.getText().toString())) / value)));
        } else {
            int value = listSIze + 1;
            avg_germi_tv.setText(String.valueOf(String.format("%.2f", (oldGermination + Double.parseDouble(germiantion_tv1.getText().toString())) / value)));
        }
    }

    public void avgFarmPopuCheckWhenPreviousGermiAdded() {
        if (!full_popu_tv1.getText().toString().equals("0") && !full_popu_tv2.getText().toString().equals("0") && !full_popu_tv3.getText().toString().equals("0") &&
                !full_popu_tv4.getText().toString().equals("0") && !full_popu_tv5.getText().toString().equals("0")) {
            int value = listSIze + 5;
            avg_farm_popu_tv.setText(String.valueOf(String.format("%.2f", (oldPopulation + Double.parseDouble(full_popu_tv1.getText().toString()) + Double.parseDouble(full_popu_tv2.getText().toString()) +
                    Double.parseDouble(full_popu_tv3.getText().toString()) + Double.parseDouble(full_popu_tv4.getText().toString()) +
                    Double.parseDouble(full_popu_tv5.getText().toString())) / value)));
        } else if (!full_popu_tv1.getText().toString().equals("0") && !full_popu_tv2.getText().toString().equals("0") && !full_popu_tv3.getText().toString().equals("0") &&
                !full_popu_tv4.getText().toString().equals("0")) {
            int value = listSIze + 4;
            avg_farm_popu_tv.setText(String.valueOf(String.format("%.2f", (oldPopulation + Double.parseDouble(full_popu_tv1.getText().toString()) + Double.parseDouble(full_popu_tv2.getText().toString()) +
                    Double.parseDouble(full_popu_tv3.getText().toString()) + Double.parseDouble(full_popu_tv4.getText().toString())) / value)));
        } else if (!full_popu_tv1.getText().toString().equals("0") && !full_popu_tv2.getText().toString().equals("0") && !full_popu_tv3.getText().toString().equals("0")
        ) {
            int value = listSIze + 3;
            avg_farm_popu_tv.setText(String.valueOf(String.format("%.2f", (oldPopulation + Double.parseDouble(full_popu_tv1.getText().toString()) + Double.parseDouble(full_popu_tv2.getText().toString()) +
                    Double.parseDouble(full_popu_tv3.getText().toString())) / value)));
        } else if (!full_popu_tv1.getText().toString().equals("0") && !full_popu_tv2.getText().toString().equals("0")
        ) {
            int value = listSIze + 2;
            avg_farm_popu_tv.setText(String.valueOf(String.format("%.2f", (oldPopulation + Double.parseDouble(full_popu_tv1.getText().toString()) + Double.parseDouble(full_popu_tv2.getText().toString())) / value)));
        } else {
            int value = listSIze + 1;

            avg_farm_popu_tv.setText(String.valueOf(String.format("%.2f", (oldPopulation + Double.parseDouble(full_popu_tv1.getText().toString())) / value)));
        }
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
                            ConnectivityUtils.checkSpeedWithColor(getActivity(), new ConnectivityUtils.ColorListener() {
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

    protected void submitData(String from) {
        progressDialog.show();
        String fromwhere = from;
        String[] sample_configured_area_arr = new String[0];
        String[] sample_spacing_ptp_arr = new String[0];
        String[] sample_spacing_rtr_arr = new String[0];
        String[] sample_ideal_pop_arr = new String[0];
        String[] sample_actual_pop_arr = new String[0];
        String[] sample_actual_total_pop_arr = new String[0];
        String[] germination = new String[0];

        if (germiCardCount == 1) {
            sample_configured_area_arr = new String[]{configured_area_et1.getText().toString()};
            sample_spacing_ptp_arr = new String[]{plant_to_plant_spacing_et1.getText().toString()};
            sample_spacing_rtr_arr = new String[]{row_to_row_spacing_et1.getText().toString()};
            sample_ideal_pop_arr = new String[]{ideal_population_tv1.getText().toString()};
            sample_actual_pop_arr = new String[]{actual_population_et1.getText().toString()};
            sample_actual_total_pop_arr = new String[]{full_popu_tv1.getText().toString()};

            germination = new String[]{germiantion_tv1.getText().toString()};
        } else if (germiCardCount == 2) {
            sample_configured_area_arr = new String[]{configured_area_et1.getText().toString(), configured_area_et2.getText().toString()};
            sample_spacing_ptp_arr = new String[]{plant_to_plant_spacing_et1.getText().toString(), plant_to_plant_spacing_et2.getText().toString()};
            sample_spacing_rtr_arr = new String[]{row_to_row_spacing_et1.getText().toString(), row_to_row_spacing_et2.getText().toString()};
            sample_ideal_pop_arr = new String[]{ideal_population_tv1.getText().toString(), ideal_population_tv2.getText().toString()};
            sample_actual_pop_arr = new String[]{actual_population_et1.getText().toString(), actual_population_et2.getText().toString()};
            sample_actual_total_pop_arr = new String[]{full_popu_tv1.getText().toString(), full_popu_tv2.getText().toString()};
            germination = new String[]{germiantion_tv1.getText().toString(), germiantion_tv2.getText().toString()};

        } else if (germiCardCount == 3) {
            sample_configured_area_arr = new String[]{configured_area_et1.getText().toString(), configured_area_et2.getText().toString(), configured_area_et3.getText().toString()};
            sample_spacing_ptp_arr = new String[]{plant_to_plant_spacing_et1.getText().toString(), plant_to_plant_spacing_et2.getText().toString(), plant_to_plant_spacing_et3.getText().toString()};
            sample_spacing_rtr_arr = new String[]{row_to_row_spacing_et1.getText().toString(), row_to_row_spacing_et2.getText().toString(), row_to_row_spacing_et3.getText().toString()};
            sample_ideal_pop_arr = new String[]{ideal_population_tv1.getText().toString(), ideal_population_tv2.getText().toString(), ideal_population_tv3.getText().toString()};
            sample_actual_pop_arr = new String[]{actual_population_et1.getText().toString(), actual_population_et2.getText().toString(), actual_population_et3.getText().toString()};
            sample_actual_total_pop_arr = new String[]{full_popu_tv1.getText().toString(), full_popu_tv2.getText().toString(), full_popu_tv3.getText().toString()};
            germination = new String[]{germiantion_tv1.getText().toString(), germiantion_tv2.getText().toString(), germiantion_tv3.getText().toString()};
        } else if (germiCardCount == 4) {
            sample_configured_area_arr = new String[]{configured_area_et1.getText().toString(), configured_area_et2.getText().toString(),
                    configured_area_et3.getText().toString(), configured_area_et4.getText().toString()};
            sample_spacing_ptp_arr = new String[]{plant_to_plant_spacing_et1.getText().toString(), plant_to_plant_spacing_et2.getText().toString(),
                    plant_to_plant_spacing_et3.getText().toString(), plant_to_plant_spacing_et4.getText().toString()};
            sample_spacing_rtr_arr = new String[]{row_to_row_spacing_et1.getText().toString(), row_to_row_spacing_et2.getText().toString(),
                    row_to_row_spacing_et3.getText().toString(), row_to_row_spacing_et4.getText().toString()};
            sample_ideal_pop_arr = new String[]{ideal_population_tv1.getText().toString(), ideal_population_tv2.getText().toString(),
                    ideal_population_tv3.getText().toString(), ideal_population_tv4.getText().toString()};
            sample_actual_pop_arr = new String[]{actual_population_et1.getText().toString(), actual_population_et2.getText().toString(),
                    actual_population_et3.getText().toString(), actual_population_et4.getText().toString()};
            sample_actual_total_pop_arr = new String[]{full_popu_tv1.getText().toString(), full_popu_tv2.getText().toString(),
                    full_popu_tv3.getText().toString(), full_popu_tv4.getText().toString()};

            sample_actual_total_pop_arr = new String[]{germiantion_tv1.getText().toString(), germiantion_tv2.getText().toString(),
                    germiantion_tv3.getText().toString(), germiantion_tv4.getText().toString()};
        } else if (germiCardCount == 5) {
            sample_configured_area_arr = new String[]{configured_area_et1.getText().toString(), configured_area_et2.getText().toString(),
                    configured_area_et3.getText().toString(), configured_area_et4.getText().toString(), configured_area_et5.getText().toString()};
            sample_spacing_ptp_arr = new String[]{plant_to_plant_spacing_et1.getText().toString(), plant_to_plant_spacing_et2.getText().toString(),
                    plant_to_plant_spacing_et3.getText().toString(), plant_to_plant_spacing_et4.getText().toString(), plant_to_plant_spacing_et5.getText().toString()};
            sample_spacing_rtr_arr = new String[]{row_to_row_spacing_et1.getText().toString(), row_to_row_spacing_et2.getText().toString(),
                    row_to_row_spacing_et3.getText().toString(), row_to_row_spacing_et4.getText().toString(), row_to_row_spacing_et5.getText().toString()};
            sample_ideal_pop_arr = new String[]{ideal_population_tv1.getText().toString(), ideal_population_tv2.getText().toString(),
                    ideal_population_tv3.getText().toString(), ideal_population_tv4.getText().toString(), ideal_population_tv5.getText().toString()};
            sample_actual_pop_arr = new String[]{actual_population_et1.getText().toString(), actual_population_et2.getText().toString(),
                    actual_population_et3.getText().toString(), actual_population_et4.getText().toString(), actual_population_et5.getText().toString()};
            sample_actual_total_pop_arr = new String[]{full_popu_tv1.getText().toString(), full_popu_tv2.getText().toString(),
                    full_popu_tv3.getText().toString(), full_popu_tv4.getText().toString(), full_popu_tv5.getText().toString()};

            germination = new String[]{germiantion_tv1.getText().toString(), germiantion_tv2.getText().toString(),
                    germiantion_tv3.getText().toString(), germiantion_tv4.getText().toString(), germiantion_tv5.getText().toString()};
        }


        for (int i = 0; i < sample_actual_pop_arr.length; i++) {
            Log.e(TAG, "conf_area " + i + " " + sample_configured_area_arr[i]);
            Log.e(TAG, "spacing_ptp" + i + " " + sample_spacing_ptp_arr[i]);
            Log.e(TAG, "spacing_rtr" + i + " " + sample_spacing_rtr_arr[i]);
            Log.e(TAG, "sample_ideal_pop_arr" + i + " " + sample_ideal_pop_arr[i]);
            Log.e(TAG, "sample_actual_pop_arr" + i + " " + sample_actual_pop_arr[i]);
            Log.e(TAG, "sample_total_pop" + i + " " + sample_actual_total_pop_arr[i]);
        }

        Log.e(TAG, "avg_germi " + avg_germi_tv.getText().toString());
        Log.e(TAG, "avg_farm_popu " + avg_farm_popu_tv.getText().toString());
        Log.e(TAG, "avg_pp_spacing " + avg_pp_spacing_tv.getText().toString());
        Log.e(TAG, "avg_rr_spacing " + avg_rr_spacing_tv.getText().toString());
        Log.e(TAG, "Farm_id " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
        Log.e(TAG, "Used_id " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));

        Log.e(TAG, "Length sample_configured_area_arr " + sample_configured_area_arr.length);
        Log.e(TAG, "Length sample_spacing_ptp_arr " + sample_spacing_ptp_arr.length);
        Log.e(TAG, "Length sample_spacing_rtr_arr " + sample_spacing_rtr_arr.length);
        Log.e(TAG, "Length sample_ideal_pop_arr " + sample_ideal_pop_arr.length);
        Log.e(TAG, "Length sample_actual_pop_arr " + sample_actual_pop_arr.length);
        Log.e(TAG, "Length sample_actual_total_pop_arr " + sample_actual_total_pop_arr.length);

        // progressDialog.cancel();
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            try {
                String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                SendGerminationSpacingData sendGerminationSpacingData = new SendGerminationSpacingData();
                //Log.e("Comp and cluster id",comp_id+"  "+cluster_id);
                sendGerminationSpacingData.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                sendGerminationSpacingData.setActual_pop(sample_actual_pop_arr);
                sendGerminationSpacingData.setActual_total_population(sample_actual_total_pop_arr);
                sendGerminationSpacingData.setAdded_by(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                sendGerminationSpacingData.setSpacing_ptp(sample_spacing_ptp_arr);
                sendGerminationSpacingData.setSpacing_rtr(sample_spacing_rtr_arr);
                sendGerminationSpacingData.setConfigured_area(sample_configured_area_arr);
                sendGerminationSpacingData.setIdeal_pop(sample_ideal_pop_arr);
                sendGerminationSpacingData.setAvg_germination(String.valueOf(avg_germi_tv.getText().toString()));
                sendGerminationSpacingData.setAvg_population(avg_farm_popu_tv.getText().toString());
                sendGerminationSpacingData.setAvg_spacing_ptp(avg_pp_spacing_tv.getText().toString());
                sendGerminationSpacingData.setAvg_spacing_rtr(avg_rr_spacing_tv.getText().toString());
                sendGerminationSpacingData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                sendGerminationSpacingData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));

                Log.e(TAG, "Sample Data " + new Gson().toJson(sendGerminationSpacingData));
                final boolean[] isLoaded = {false};
                long currMillis=AppConstants.getCurrentMills();
                Call<StatusMsgModel> statusMsgModelCall = apiService.getStatusMsgForGerminationAndSpacing(sendGerminationSpacingData);
                statusMsgModelCall.enqueue(new Callback<StatusMsgModel>() {
                    @Override
                    public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                        String error=null;
                        isLoaded[0] =true;

                        if (response.isSuccessful()) {
                            progressDialog.cancel();
                            StatusMsgModel statusMsgModel = response.body();
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(getActivity(), response.body().getMsg());
                            }
                            if (statusMsgModel.getStatus() == 1) {
                                //Toast.makeText(context, resources.getString(R.string.germination_submitted_success_msg), Toast.LENGTH_LONG).show();
                                Toasty.success(context, resources.getString(R.string.germination_submitted_success_msg), Toast.LENGTH_LONG, true).show();

                                ActivityOptions options = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                                }
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    getActivity().finish();
                                } else {
                                    getActivity().finish();
                                }
                            }  else if (response.body().getStatus()== AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                            }else {
                                submit_germi_data.setClickable(true);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showDialog(context, response.body().getMsg());
                                    }
                                });
                            }
                        } else if (response.code()== AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                        } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                        } else {
                            try {
                                error=response.errorBody().string().toString();
                                progressDialog.cancel();
                                submit_germi_data.setClickable(true);
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(context, resources.getString(R.string.germination_update_fail_msg));
                                Log.e("Error", error);
                                //Toast.makeText(context, "Oops something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        long newMillis=AppConstants.getCurrentMills();
                        long diff=newMillis-currMillis;
                        if(diff>=DELAY_API||(diff<DELAY_API&&error!=null&&!TextUtils.isEmpty(error))||response.code()==500) {
                            notifyApiDelay(getActivity(), response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                        long newMillis=AppConstants.getCurrentMills();
                        long diff=newMillis-currMillis;
                        isLoaded[0] =true;
                        notifyApiDelay(getActivity(),call.request().url().toString(),
                                ""+diff,internetSPeed,t.toString(),0);

                        progressDialog.cancel();
                        submit_germi_data.setClickable(true);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.germination_update_fail_msg));
                        // Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                    }
                });

                internetFlow(isLoaded[0]);

            } catch (Exception e) {
                e.printStackTrace();
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialog(context, resources.getString(R.string.germination_update_fail_msg));
            }
        } else {

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String todayDateStr = df.format(c);

            for (int i = 0; i < sample_configured_area_arr.length; i++) {
                SampleGerminationDatum datum = new SampleGerminationDatum();
                datum.setGerminationAvg(Double.parseDouble(avg_germi_tv.getText().toString()));
                datum.setActualPopAvg(Double.parseDouble(avg_farm_popu_tv.getText().toString()));
                datum.setSpacingPtpAvg(Double.parseDouble(avg_pp_spacing_tv.getText().toString()));
                datum.setSpacingRtrAvg(Double.parseDouble(avg_rr_spacing_tv.getText().toString()));
                datum.setDoa(todayDateStr);
                datum.setSpacingRtr(sample_spacing_rtr_arr[i]);
                datum.setConfiguredArea(sample_configured_area_arr[i]);
                datum.setSpacingPtp(sample_spacing_ptp_arr[i]);
                datum.setIdealPop(sample_ideal_pop_arr[i]);
                datum.setActualPop(sample_actual_pop_arr[i]);
                datum.setActualTotalPopulation(sample_actual_total_pop_arr[i]);
                try {
                    datum.setGermination(Double.valueOf(germination[i]));
                }catch (Exception e){

                }
                offlineGerminationList.add(datum);

            }


            FarmData farmData = null;

            try {
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                farmData = gson.fromJson(farm.getFarmFullData(), FarmData.class);
                farmData.setGermination(avg_germi_tv.getText().toString());
                farmData.setPopulation(avg_farm_popu_tv.getText().toString());
                farmData.setSpacing_rtr(avg_rr_spacing_tv.getText().toString());
                farmData.setSpacing_ptp(avg_pp_spacing_tv.getText().toString());
                farm.setGermination(avg_germi_tv.getText().toString());
                farm.setPopulation("" + avg_farm_popu_tv.getText().toString());
                farm.setSpacingRtr("" + avg_rr_spacing_tv.getText().toString());
                farm.setSpacingPtp("" + avg_pp_spacing_tv.getText().toString());


                if (farmerT != null && AppConstants.isValidString(farmerT.getData())) {
                    FarmerAndFarmData farmerAndFarmData = null;
                    farmerAndFarmData = gson.fromJson(farmerT.getData(), FarmerAndFarmData.class);

                    if (farmerAndFarmData != null && farmerAndFarmData.getFarmDataList() != null && farmerAndFarmData.getFarmDataList().size() > 0) {
                        for (int i = 0; i < farmerAndFarmData.getFarmDataList().size(); i++) {
                            if (AppConstants.isValidString(farmerAndFarmData.getFarmDataList().get(i).getFarmId()) &&
                                    AppConstants.isValidString(farmData.getFarmId()) &&
                                    farmData.getFarmId().equalsIgnoreCase(farmerAndFarmData.getFarmDataList().get(i).getFarmId())) {

                                farmerAndFarmData.getFarmDataList().get(i).setGermination(avg_germi_tv.getText().toString());
                                farmerAndFarmData.getFarmDataList().get(i).setPopulation(avg_farm_popu_tv.getText().toString());
                                farmerAndFarmData.getFarmDataList().get(i).setSpacing_ptp(avg_pp_spacing_tv.getText().toString());
                                farmerAndFarmData.getFarmDataList().get(i).setSpacing_rtr(avg_rr_spacing_tv.getText().toString());
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

            } catch (Exception e) {

            }
            if (farmData!=null)
                farm.setFarmFullData(new Gson().toJson(farmData));

            farm.setGerminationDataOffline(new Gson().toJson(offlineGerminationList));
            farm.setSpacingPtp(avg_pp_spacing_tv.getText().toString());
            farm.setSpacingRtr(avg_rr_spacing_tv.getText().toString());
            farm.setPopulation(avg_farm_popu_tv.getText().toString());
            farm.setGermination(avg_germi_tv.getText().toString());
            farm.setIsUpdated("Y");
            farm.setIsUploaded("N");
            progressDialog.cancel();
            FarmCacheManager.getInstance(context).updateFarm(farm);
            Toasty.success(context, resources.getString(R.string.germination_submitted_success_msg), Toast.LENGTH_LONG, true).show();
            finishAllAndStartLanding();


        }


    }

    List<SampleGerminationDatum> offlineGerminationList = new ArrayList<>();

    private void finishAllAndStartLanding() {
        Intent intent = new Intent(context, LandingActivity.class);
        ActivityOptions options = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getActivity().finishAffinity();
            startActivity(intent, options.toBundle());
            getActivity().finish();
        } else {
            getActivity().finishAffinity();
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
    FarmerT farmerT;
    @Override
    public void onFarmLoader(Farm farm) {
        this.farm = farm;
        if (farm.getActualArea() != null)
            actual_area = Double.valueOf(farm.getActualArea().trim());

        if (farm.getGerminationDataOffline() != null && !farm.getGerminationDataOffline().equals("[]") && !TextUtils.isEmpty(farm.getGerminationDataOffline())) {
            try {
                JSONArray array = new JSONArray(farm.getGerminationDataOffline());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    SampleGerminationDatum data = new Gson().fromJson(object.toString(), SampleGerminationDatum.class);
                    offlineGerminationList.add(data);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (farm != null && AppConstants.isValidString(farm.getFarmerId()))
            FarmerCacheManager.getInstance(context).getFarmer(new FarmerCacheManager.GetFarmerListener() {
                @Override
                public void onFarmerLoaded(FarmerT farmerT) {
                    AddGermiFullScreenDialog.this.farmerT = farmerT;
                }
            }, farm.getFarmerId());

    }


}
