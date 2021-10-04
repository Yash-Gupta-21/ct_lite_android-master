package com.i9930.croptrails.ProduceSell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class AddProduceSellActivity extends AppCompatActivity {

    private static final String TAG = "AddProduceSellActivity";
    AddProduceSellActivity context;
    Resources resources;
    Toolbar mActionBarToolbar;
    TextView tittle;
    @BindView(R.id.tiSellQty)
    TextInputLayout tiSellQty;
    @BindView(R.id.etSellQty)
    EditText etSellQty;
    @BindView(R.id.tiSellTotalAmmount)
    TextInputLayout tiSellTotalAmmount;
    @BindView(R.id.etSellTotalAmmount)
    EditText etSellTotalAmmount;
    @BindView(R.id.tiCropRate)
    TextInputLayout tiCropRate;
    @BindView(R.id.etCropRate)
    EditText etCropRate;
    @BindView(R.id.tiComment)
    TextInputLayout tiComment;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.tiSoldOn)
    TextInputLayout tiSoldOn;
    @BindView(R.id.etSoldOn)
    EditText etSoldOn;
    @BindView(R.id.submitBtn)
    Button submitBtn;
    boolean isOneChanging = false;
    boolean isTwoChanging = false;
    @BindView(R.id.progressBar)
    GifImageView progressBar;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produce_sell);
        context = this;
        resources = getResources();
        ButterKnife.bind(this);
        tittle = (TextView) findViewById(R.id.tittle);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadAds();
        tittle.setText(resources.getString(R.string.sell_record_label));
        tiCropRate.setHint("*" + AppConstants.getCapsWords(resources.getString(R.string.rate_label) + " (" + resources.getString(R.string.per_kg_label) + ")"));
        tiSellQty.setHint("*" + AppConstants.getCapsWords(resources.getString(R.string.sell_quantity_label)));
        tiSellTotalAmmount.setHint("*" + AppConstants.getCapsWords(resources.getString(R.string.total_amount_label)));
        tiSoldOn.setHint("*" + AppConstants.getCapsWords(resources.getString(R.string.sold_on_label)));
        tiSoldOn.setHint("*" + AppConstants.getCapsWords(resources.getString(R.string.sold_on_label)));
        tiComment.setHint(AppConstants.getCapsWords(resources.getString(R.string.comment_label)) + "(" + AppConstants.getCapsWords(resources.getString(R.string.optional_label)) + ")");
        listeners();
    }

    private void listeners() {

        etSellTotalAmmount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isOneChanging = true;
                isTwoChanging = false;
                return false;
            }
        });

        etCropRate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isOneChanging = false;
                isTwoChanging = true;
                return false;
            }
        });
        etSellTotalAmmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(etSellQty.getText().toString())) {
                    float finalQty = Float.valueOf(etSellQty.getText().toString().trim());
                    if (!TextUtils.isEmpty(s.toString()) && !isTwoChanging) {
                        etCropRate.setText("" + Float.valueOf(s.toString()) / finalQty);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etCropRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(etSellQty.getText().toString())) {
                    float finalQty = Float.valueOf(etSellQty.getText().toString().trim());
                    if (!TextUtils.isEmpty(s.toString()) && !isOneChanging) {
                        etSellTotalAmmount.setText("" + Float.valueOf(s.toString()) * finalQty);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datesowing = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = SharedPreferencesMethod.getString(context,SharedPreferencesMethod.COMP_DATE_FORMAT); //I
                if (!AppConstants.isValidString(myFormat))// n which you need put here
                    myFormat=AppConstants.DATE_FORMAT_SHOW;
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                etSoldOn.setText(sdf.format(calendar.getTime()));
                etSoldOn.setError(null);
            }
        };
        etSoldOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, "data", Toast.LENGTH_SHORT).show();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, datesowing, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etSellQty.getText().toString())) {
                    etSellQty.getParent().requestChildFocus(etSellQty, etSellQty);
                    etSellQty.setError(resources.getString(R.string.required_label));
                    Toasty.error(context, resources.getString(R.string.enter_sell_qty_of_harvest_msg), Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(etSellTotalAmmount.getText().toString())) {
                    etSellTotalAmmount.getParent().requestChildFocus(etSellTotalAmmount, etSellTotalAmmount);
                    etSellTotalAmmount.setError(resources.getString(R.string.required_label));
                    Toasty.error(context, resources.getString(R.string.enter_total_selling_amount_msg), Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(etCropRate.getText().toString())) {
                    etCropRate.getParent().requestChildFocus(etCropRate, etCropRate);
                    etCropRate.setError(resources.getString(R.string.required_label));
                    Toasty.error(context, resources.getString(R.string.enter_current_rate_of_crop_msg), Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(etSoldOn.getText().toString())) {
                    etSoldOn.getParent().requestChildFocus(etSoldOn, etSoldOn);
                    etSoldOn.setError(resources.getString(R.string.required_label));
                    Toasty.error(context, resources.getString(R.string.enter_selling_date), Toast.LENGTH_LONG).show();
                } else {
                    addProducedSell();
                }
            }
        });

    }

    private void addProducedSell() {
        submitBtn.setClickable(false);
        submitBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
//        String sesionId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SEASON_NUM);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);

        String qty = etSellQty.getText().toString().trim();
        String amount = etSellTotalAmmount.getText().toString().trim();
        String rate = etCropRate.getText().toString().trim();
        String date = AppConstants.getUploadableDate(etSoldOn.getText().toString().trim(),context);
        String comment = etComment.getText().toString().trim();
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();

        JsonObject obj=new JsonObject();
        obj.addProperty("user_id",""+userId);
        obj.addProperty("token",""+token);
        obj.addProperty("farm_id",""+farmId);
        obj.addProperty("sale_qty",""+qty);
        obj.addProperty("amount",""+amount);
        obj.addProperty("rate",""+rate);
        obj.addProperty("sold_on",""+date);
        obj.addProperty("added_by",""+userId);
        obj.addProperty("comment",""+comment);

        Log.e(TAG,"Sending Data "+new Gson().toJson(obj));

        apiInterface.addSellRecord(obj).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                String error=null;
                isLoaded[0]=true;
                if (response.isSuccessful()) {
                    submitBtn.setClickable(true);
                    submitBtn.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Sell Ress " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 10) {
                        new ViewFailDialog().showSessionExpireDialog(AddProduceSellActivity.this);
                    } else if (response.body().getStatus() == 1) {
                        Toasty.success(context, resources.getString(R.string.sell_record_added_msg), Toast.LENGTH_LONG).show();
                       setResult(RESULT_OK);
                        finish();
                    } else {
                        Toasty.error(context, resources.getString(R.string.failed_to_add_sell_record_msg) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                    }
                }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(AddProduceSellActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(AddProduceSellActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error=response.errorBody().string().toString();

                        submitBtn.setClickable(true);
                        submitBtn.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Log.e(TAG, "Sell Err " + response.errorBody().string()+", code "+response.code());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toasty.error(context, resources.getString(R.string.failed_to_add_sell_record_msg) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(AddProduceSellActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                isLoaded[0]=true;
                notifyApiDelay(AddProduceSellActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "Sell Failure " + t.toString());
                submitBtn.setClickable(true);
                submitBtn.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Toasty.error(context, resources.getString(R.string.failed_to_add_sell_record_msg) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
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
                            ConnectivityUtils.checkSpeedWithColor(AddProduceSellActivity.this, new ConnectivityUtils.ColorListener() {
                                @Override
                                public void onColorChanged(int color, String speed) {

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
