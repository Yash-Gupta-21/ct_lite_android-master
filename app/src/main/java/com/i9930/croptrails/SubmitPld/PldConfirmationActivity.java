package com.i9930.croptrails.SubmitPld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmCacheManager;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmLoadListener;
import com.i9930.croptrails.SubmitPld.Model.SendPldData;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Locale;

public class PldConfirmationActivity extends AppCompatActivity implements FarmLoadListener {

    String pld;
    @BindView(R.id.edit_pencil)
    ImageView edit_pencil;
    @BindView(R.id.pld_area_in_acre)
    EditText pld_area_in_acre;
    @BindView(R.id.pld_reason_for_damage)
    EditText pld_reason_for_damage;
    @BindView(R.id.submit_pld_area)
    TextView submit_pld_area;
    @BindView(R.id.progressBar_cyclic)
    ProgressBar progressBar_cyclic;
    @BindView(R.id.eff_farm_area_tv)
    TextView eff_farm_area_tv;
    Context context;
    Toolbar mActionBarToolbar;
    int i = 0;
    double oldPldAcers = 0;
    TextView title;
    @BindView(R.id.pld_already_done_msg_tv)
    TextView pld_already_done_msg_tv;
    Resources resources;
    double multiplicationFactor = 1.0;
    Double actualArea = 0.0;
    Double standingarea = 0.0;
    String TAG = "PldConfirmationActivity";
    Farm farm;

    private void intitViews() {
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        title = (TextView) findViewById(R.id.tittle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageToLoad  = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
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
        setContentView(R.layout.activity_pld_confirmation);
        context = this;
        ButterKnife.bind(this);

        final String languageCode = SharedPreferencesMethod2.getString(PldConfirmationActivity.this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        intitViews();
        setTitleBar();

        /*if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL).toLowerCase().equals("hectare")) {
            multiplicationFactor = 2.47105;
        } else if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL).toLowerCase().equals("malwa_bigha")) {
            multiplicationFactor = 3.63;
        } else {

        }*/
        progressBar_cyclic.setVisibility(View.VISIBLE);
        asyncGetPldAcre();
        onClicks();

        AutoCompleteTextView autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        autoCompleteTextView1.setAdapter(getEmailAddressAdapter(this));

        if (!TextUtils.isEmpty(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA))) {
            actualArea = Double.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA));
        }

        if (!TextUtils.isEmpty(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES))) {
            standingarea = Double.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES));
        }
        if (actualArea > 0) {
        } else {
            noAcrualAreaEvent("actual");
        }
    }

    private void onClicks() {
        submit_pld_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPldClickEvent();
            }
        });


        edit_pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (standingarea > 0) {
                    pld_reason_for_damage.setEnabled(true);
                    pld_area_in_acre.setEnabled(true);
                    submit_pld_area.setEnabled(true);
                } else {
                    noAcrualAreaEvent("standing");
                }
                //Toast.makeText(context, "Edit Called", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setTitleBar() {
        title.setText(resources.getString(R.string.pld_form_title));
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void noAcrualAreaEvent(String from) {

        String msg = "";
        if (from.trim().toLowerCase().equals("actual")) {
            //Actual
            msg = resources.getString(R.string.no_actual_area_available_go_to_capture);
        } else {
            //Standing
            msg = resources.getString(R.string.there_is_no_standing_area_avilable_to_enter_for_pld_label);
        }

        //Toast.makeText(context, "Go to capture area", Toast.LENGTH_LONG).show();
        submit_pld_area.setEnabled(false);
        pld_area_in_acre.setEnabled(false);
        pld_reason_for_damage.setEnabled(false);

        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_for_goto_capure);
        Window window1 = dialog.getWindow();
        window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        TextView msgTv = dialog.findViewById(R.id.msg_tv);
        msgTv.setText(msg);
        Button okButton = dialog.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (DataHandler.newInstance().getFetchFarmResult().getPldAcres()!=null
                        &&Double.parseDouble(DataHandler.newInstance().getFetchFarmResult().getPldAcres())>0)*/
                finish();
            }
        });

        dialog.show();
    }

    private void submitPldClickEvent() {
        if (oldPldAcers > 0) {
            // Toast.makeText(context, resources.getString(R.string.already_pld_added), Toast.LENGTH_LONG).show();
            Toasty.warning(context, resources.getString(R.string.already_pld_added), Toast.LENGTH_LONG, true).show();

            edit_pencil.setVisibility(View.GONE);

        } else {
            if (pld_area_in_acre.getText().toString().equals("") || Double.valueOf(pld_area_in_acre.getText().toString()) >
                    Double.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).trim())) {
                pld_area_in_acre.setError(resources.getString(R.string.invalid_entry_error));
                Toasty.error(context, resources.getString(R.string.invalid_entry_error), Toast.LENGTH_LONG).show();
            } else if (pld_reason_for_damage.getText().toString().equals("")) {
                pld_reason_for_damage.setError(resources.getString(R.string.invalid_entry_in_pld));
                Toasty.error(context, resources.getString(R.string.invalid_entry_in_pld), Toast.LENGTH_LONG).show();
            } else {
                progressBar_cyclic.setVisibility(View.VISIBLE);
                SendDataAsyncTask sendDataAsyncTask = new SendDataAsyncTask();
                sendDataAsyncTask.execute();
            }
        }

    }



    private String convertAreaTo(String area) {

        return AppConstants.getShowableArea(area);
    }


    class SendDataAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pld = pld_area_in_acre.getText().toString().trim();

                }
            });

            if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                onlineModeSubmitPldData();
            } else {
                offlineModeSubmitPldData();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(context, "new pld "+pld, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE))
                //   Toast.makeText(context, resources.getString(R.string.your_pld_data_update), Toast.LENGTH_SHORT).show();
                Toasty.success(context, resources.getString(R.string.your_pld_data_update), Toast.LENGTH_SHORT, true).show();

        }
    }

    private void offlineModeSubmitPldData() {
        pld = pld_area_in_acre.getText().toString().trim();
        double standingAcres = Double.parseDouble(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).trim());
        double pldAdded = Double.parseDouble(pld);
        double newStandingAcers = standingAcres - pldAdded;
        farm.setStandingAcres(String.valueOf(newStandingAcers));
        farm.setPldAcres(pld);
        farm.setPldReason(pld_reason_for_damage.getText().toString());
        farm.setIsUpdated("Y");
        farm.setIsUploaded("N");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toasty.success(context, "Pld Data stored successfully", Toast.LENGTH_LONG, false).show();
            }
        });
        FarmCacheManager.getInstance(context).updateFarm(farm);
        finish();
    }

    private void onlineModeSubmitPldData() {
        double standingAcres = Double.parseDouble(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).trim());
        double pldAdded=0.0;
        if (!TextUtils.isEmpty(pld_area_in_acre.getText().toString().trim()))
        pldAdded= Double.parseDouble(pld_area_in_acre.getText().toString().trim());
        double newStandingAcers = standingAcres - pldAdded;
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        SendPldData sendPldData = new SendPldData();

        sendPldData.setStanding_acres(String.valueOf(newStandingAcers));
        sendPldData.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
        sendPldData.setCluster_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
        sendPldData.setPerson_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
        sendPldData.setPld_acres(AppConstants.getUploadableArea(pld_area_in_acre.getText().toString()));
        sendPldData.setPld_reason(pld_reason_for_damage.getText().toString());
        sendPldData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        sendPldData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));

        Log.e(TAG, "Sending Pld Area " + new Gson().toJson(sendPldData));
        Call<StatusMsgModel> msgModelCall = apiService.getStatusMsgForPldAcres(sendPldData);
        msgModelCall.enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {

                if (response.isSuccessful()) {
                    StatusMsgModel statusMsgModel = response.body();
                    Log.e(TAG,"Success "+ new Gson().toJson(response.body()));
                    progressBar_cyclic.setVisibility(View.INVISIBLE);
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(PldConfirmationActivity.this, response.body().getMsg());
                    }  else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(PldConfirmationActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(PldConfirmationActivity.this, resources.getString(R.string.authorization_expired));
                    }else if (statusMsgModel.getStatus() == 1) {
                        ActivityOptions options = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finish();
                        } else {
                            finish();
                        }
                    } else {
                        //  Log.e("Error", statusMsgModel.getMsg() + " " + statusMsgModel.getStatus());
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(PldConfirmationActivity.this, resources.getString(R.string.failure_upload_msg));
                        //Toast.makeText(context, "Server Error Please try again latter", Toast.LENGTH_SHORT).show();
                    }
                }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(PldConfirmationActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(PldConfirmationActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        Log.e("Error", response.errorBody().string().toString());
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(PldConfirmationActivity.this, resources.getString(R.string.failure_upload_msg));
                        //Toast.makeText(context, "Oops something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                progressBar_cyclic.setVisibility(View.INVISIBLE);
                //Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "Failed to upload pld acres. Please try again", Toast.LENGTH_SHORT).show();
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialog(PldConfirmationActivity.this, resources.getString(R.string.failure_upload_msg));
            }
        });
    }


    private ArrayAdapter<String> getEmailAddressAdapter(Context context) {
        Account[] accounts = AccountManager.get(context).getAccounts();
        String[] addresses = new String[10];
        for (int i = 0; i < 10; i++) {
            addresses[i] = "utkarsh" + i;
        }
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, addresses);
    }


    protected void asyncGetPldAcre() {

        progressBar_cyclic.setVisibility(View.INVISIBLE);
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            onlineModeEvent();
        } else {
            offlineModeEvent();
        }

        pld_area_in_acre.setEnabled(false);
        pld_reason_for_damage.setEnabled(false);
        submit_pld_area.setEnabled(false);

    }


    private void offlineModeEvent() {

        FarmCacheManager.getInstance(context).getFarm(this::onFarmLoader, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));

    }

    private void onlineModeEvent() {

        if (DataHandler.newInstance().getFetchFarmResult() != null) {

            if (DataHandler.newInstance().getFetchFarmResult().getPldAcres() != null && !DataHandler.newInstance().getFetchFarmResult().getPldAcres().trim().equals("")) {
                oldPldAcers = Double.parseDouble(DataHandler.newInstance().getFetchFarmResult().getPldAcres().trim());
                if (oldPldAcers > 0) {
                    edit_pencil.setVisibility(View.GONE);
                    submit_pld_area.setBackgroundColor(Color.parseColor("#33ffc107"));
                    //submit_pld_area.setVisibility(View.GONE);
                    pld_already_done_msg_tv.setVisibility(View.VISIBLE);
                }
                pld_area_in_acre.setText(convertAreaTo(DataHandler.newInstance().getFetchFarmResult().getPldAcres().trim()));

            } else {
                pld_area_in_acre.setHint(resources.getString(R.string.enter_pld_here_hint));
            }

            try {
                if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA) != null ||
                        !SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA).equals("0")) {
                    eff_farm_area_tv.setText(convertAreaTo(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).trim()) + " / "
                            + convertAreaTo(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA).trim()));
                } else {
                    eff_farm_area_tv.setText(convertAreaTo(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).trim()) + " / "
                            +
                            convertAreaTo(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.EXPECTED_AREA).trim()));
                }

            } catch (NumberFormatException numExep) {
                numExep.printStackTrace();
            }

            if (DataHandler.newInstance().getFetchFarmResult().getPldReason() != null &&
                    !DataHandler.newInstance().getFetchFarmResult().getPldReason().equals("")) {
                pld_reason_for_damage.setText(DataHandler.newInstance().getFetchFarmResult().getPldReason());
                Log.e("PldReason", "Pld Reason " + DataHandler.newInstance().getFetchFarmResult().getPldReason());
            } else {
                pld_reason_for_damage.setHint(resources.getString(R.string.enter_reason_for_pld_hint));
                Log.e("PldReason", "Pld Reason " + DataHandler.newInstance().getFetchFarmResult().getPldReason());

            }
        } else {
            Intent intent = new Intent(context, LandingActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
                startActivity(intent, options.toBundle());
                finish();
            } else {
                finishAffinity();
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*Intent intent=new Intent(context,LandingActivity.class);
        startActivity(intent);
        finish();*/
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFarmLoader(Farm farm) {

        this.farm = farm;
        if (farm.getPldAcres() != null && !farm.getPldAcres().trim().equals("")) {
            oldPldAcers = Double.valueOf(farm.getPldAcres().trim());
            if (oldPldAcers > 0) {
                edit_pencil.setVisibility(View.GONE);
                //submit_pld_area.setVisibility(View.GONE);
                submit_pld_area.setBackgroundColor(Color.parseColor("#33ffc107"));
                pld_already_done_msg_tv.setVisibility(View.VISIBLE);
            }
            pld_area_in_acre.setText(convertAreaTo(farm.getPldAcres()));

        } else {
            pld_area_in_acre.setHint(resources.getString(R.string.enter_pld_hint));
        }
        try {

            if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA) != null ||
                    !SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA).equals("0")) {
                eff_farm_area_tv.setText(convertAreaTo(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).trim()) + " / "
                        + convertAreaTo(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA).trim()));
            } else {
                eff_farm_area_tv.setText(convertAreaTo(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).trim()) + " / "
                        +
                        convertAreaTo(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.EXPECTED_AREA).trim()));
            }
        } catch (NumberFormatException numExep) {
            numExep.printStackTrace();
        }

        if (farm.getPldReason() != null && !farm.getPldReason().equals("")) {
            pld_reason_for_damage.setText(farm.getPldReason());
        } else {
            pld_reason_for_damage.setHint(resources.getString(R.string.enter_reason_for_pld_hint));
        }

    }




}
