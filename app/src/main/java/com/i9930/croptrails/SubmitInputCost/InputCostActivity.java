package com.i9930.croptrails.SubmitInputCost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.i9930.croptrails.CommonAdapters.InputCostTypeSpinner.InputCostTypeAdapter;
import com.i9930.croptrails.CommonAdapters.InputResourceCostTypeSpinner.InputResourceCostTypeAdapter;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownCacheManager;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownFetchListener;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
import com.i9930.croptrails.RoomDatabase.InputCost.InputCostCacheManager;
import com.i9930.croptrails.RoomDatabase.InputCost.InputCostFetchListener;
import com.i9930.croptrails.RoomDatabase.InputCost.InputCostT;
import com.i9930.croptrails.RoomDatabase.ResourceCost.ResourceCacheManager;
import com.i9930.croptrails.RoomDatabase.ResourceCost.ResourceCostT;
import com.i9930.croptrails.RoomDatabase.ResourceCost.ResourceFetchListener;
import com.i9930.croptrails.ShowInputCost.Model.InputCostData;
import com.i9930.croptrails.ShowInputCost.Model.ResourceData;
import com.i9930.croptrails.SubmitInputCost.Adapters.InputCostListAdapterSubmit;
import com.i9930.croptrails.SubmitInputCost.Adapters.ResourceListAdapterSubmit;
import com.i9930.croptrails.SubmitInputCost.Model.CostAndResourceSubmitData;
import com.i9930.croptrails.SubmitInputCost.Model.CostDropdownResponse;
import com.i9930.croptrails.SubmitInputCost.Model.CostSubmitResponse;
import com.i9930.croptrails.SubmitInputCost.Model.ExpenseDataDD;
import com.i9930.croptrails.SubmitInputCost.Model.InputCostSubmitDatum;
import com.i9930.croptrails.SubmitInputCost.Model.ResourceDataDD;
import com.i9930.croptrails.SubmitInputCost.Model.ResourceSubmitDatum;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class InputCostActivity extends AppCompatActivity implements DropDownFetchListener, InputCostFetchListener, ResourceFetchListener {

    String TAG = "InputCostActivity";
    ProgressDialog progressDialog;
    Context context;
    Toolbar mActionBarToolbar;
    @BindView(R.id.dateLayout)
    RelativeLayout dateLayout;
    @BindView(R.id.dateTv)
    TextView dateTv;
    @BindView(R.id.commentEt)
    EditText commentEt;
    @BindView(R.id.submitCostInput)
    TextView submitCostInput;
    @BindView(R.id.amountEt)
    EditText amountEt;
    Calendar currentCalendar = Calendar.getInstance();
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar2 = Calendar.getInstance();

    //New Input Cost Views
    @BindView(R.id.spinner_input_cost)
    Spinner spinner_input_cost;
    @BindView(R.id.et_input_cost_amount)
    EditText et_input_cost_amount;
    @BindView(R.id.recycler_amount_list)
    RecyclerView recycler_amount_list;
    @BindView(R.id.addMoreAmountImg)
    ImageView addMoreAmountImg;
    @BindView(R.id.et_other_cost_type)
    EditText et_other_cost_type;
    @BindView(R.id.dateTvNew)
    TextView dateTvNew;
    private List<ExpenseDataDD> expenseDataDDList = new ArrayList<>();
    private List<ResourceDataDD> resourceDataDDList = new ArrayList<>();
    private boolean isOtherInputType = false;
    private boolean isOtherResourceType = false;

    //Resources Views
    @BindView(R.id.spinner_resources_cost)
    Spinner spinner_resources_cost;
    @BindView(R.id.et_resource_qty)
    EditText et_resource_qty;
    @BindView(R.id.dateTvResource)
    TextView dateTvResource;
    @BindView(R.id.recycler_resource_amount_list)
    RecyclerView recycler_resource_list;
    @BindView(R.id.addMoreResourceAmountImg)
    ImageView addMoreResourceAmountImg;
    @BindView(R.id.unit_tv)
    TextView unit_tv;
    //Other Resource
    @BindView(R.id.et_other_resource_name)
    EditText et_other_resource_name;
    @BindView(R.id.et_other_resource_unit)
    EditText et_other_resource_unit;

    List<ResourceSubmitDatum> resourceSubmitList = new ArrayList<>();
    List<InputCostSubmitDatum> inputCostSubmitList = new ArrayList<>();

    ResourceListAdapterSubmit resourceAdapter;
    InputCostListAdapterSubmit costAdapter;
    @BindView(R.id.submitCostInputN)
    TextView submitCostInputN;
    ExpenseDataDD expenseDataDD;
    ResourceDataDD resourceDataDD;
    boolean isApiFailedToUpload = false;
    ViewFailDialog viewFailDialog;

    private boolean isError = true;
    private boolean isError2 = true;

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    List<ResourceData> resourceDataList = new ArrayList<>();
    List<InputCostData> inputCostDataList = new ArrayList<>();
    InputCostT inputCostT;
    ResourceCostT resourceCostT;
    Resources resources;
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
        String languageToLoad  = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
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
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        setContentView(R.layout.activity_input_cost);

        context = this;
        loadAds();
        ButterKnife.bind(this);
        viewFailDialog = new ViewFailDialog();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(resources.getString(R.string.please_wait_msg));
        progressDialog.setCancelable(false);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        resourceAdapter = new ResourceListAdapterSubmit(context, resourceSubmitList);
        costAdapter = new InputCostListAdapterSubmit(context, inputCostSubmitList);
        LinearLayoutManager manager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        LinearLayoutManager manager2 = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        recycler_amount_list.setHasFixedSize(true);
        recycler_amount_list.setLayoutManager(manager);
        recycler_amount_list.setAdapter(costAdapter);

        recycler_resource_list.setHasFixedSize(true);
        recycler_resource_list.setLayoutManager(manager2);
        recycler_resource_list.setAdapter(resourceAdapter);

        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.input_cost_and_resource_title));
        datePickingEvents();
        onClick();
        onClickNew();
        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            DropDownCacheManager.getInstance(context).getAllChemicalUnits(this::onChemicalUnitLoaded);
            InputCostCacheManager.getInstance(context).getInputCost(this, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
            ResourceCacheManager.getInstance(context).getResource(this, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
        } else {
            getDropdownData();
        }
    }


    private void onClick() {
        submitCostInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(amountEt.getText().toString())) {
                    Toasty.error(context, resources.getString(R.string.enter_expense_ammount_msg), Toast.LENGTH_LONG).show();
                    amountEt.getParent().requestChildFocus(amountEt, amountEt);
                    amountEt.setError(resources.getString(R.string.enter_expense_ammount_msg));
                } else if (TextUtils.isEmpty(dateTv.getText().toString())) {
                    Toasty.error(context, resources.getString(R.string.enter_date_of_expense_msg), Toast.LENGTH_LONG).show();
                    dateTv.getParent().requestChildFocus(dateTv, dateTv);
                    dateTv.setError(resources.getString(R.string.enter_date_of_expense_msg));
                } else if (TextUtils.isEmpty(commentEt.getText().toString())) {
                    Toasty.error(context, resources.getString(R.string.enter_expense_reson_msg), Toast.LENGTH_LONG).show();
                    commentEt.getParent().requestChildFocus(commentEt, commentEt);
                    commentEt.setError(resources.getString(R.string.enter_expense_reson_msg));
                } else {
                    progressDialog.show();
                    submitCost();
                }
            }
        });

        commentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    commentEt.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void datePickingEvents() {
        //Input Cost Date Picking Event
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateTvNew.setText(sdf.format(myCalendar.getTime()));
                dateTvNew.setError(null);
            }
        };
        dateTvNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                DatePickerDialog dpDialog = new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dpDialog.getDatePicker().setMaxDate(currentCalendar.getTimeInMillis());
                dpDialog.show();
            }
        });

        //Resource Cost Date Picking Event
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateTvResource.setText(sdf.format(myCalendar2.getTime()));
                dateTvResource.setError(null);
            }
        };
        dateTvResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DatePickerDialog dpDialog = new DatePickerDialog(context, date1, myCalendar2
                        .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                        myCalendar2.get(Calendar.DAY_OF_MONTH));
                dpDialog.getDatePicker().setMaxDate(currentCalendar.getTimeInMillis());
                dpDialog.show();
            }
        });


    }

    private void submitCost() {
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String comment = commentEt.getText().toString().trim();
        String activityTypeId = null;
        String expenseDate = getDate(dateTv.getText().toString());
        String expense = amountEt.getText().toString().trim();
        String addedBy = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        Log.e(TAG, "Sending Data CompId " + compId + "  FarmId " + farmId + "  Comment " + comment + "  ActivityId  " + activityTypeId + "  Expense Date " + expenseDate + "  AddedBy " + addedBy);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        progressDialog.cancel();
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();
        apiInterface.insertCost(expense, compId, farmId, comment, activityTypeId, expenseDate, addedBy, userId, token).enqueue(new Callback<CostSubmitResponse>() {
            @Override
            public void onResponse(Call<CostSubmitResponse> call, Response<CostSubmitResponse> response) {
                String error=null;
                isLoaded[0]=true;
                if (response.isSuccessful()) {
                    Log.e(TAG, "Cost Response " + new Gson().toJson(response.body()));
                    progressDialog.dismiss();
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(InputCostActivity.this, resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == 1) {
                        Toasty.success(context, resources.getString(R.string.cost_added_sussessful_msg), Toast.LENGTH_LONG).show();
                        Intent intent=getIntent();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toasty.error(context, resources.getString(R.string.failed_to_add_farmer_expense), Toast.LENGTH_LONG).show();
                    }

                } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(InputCostActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(InputCostActivity.this, resources.getString(R.string.authorization_expired));
                }else {
                    progressDialog.dismiss();
                    Toasty.error(context, resources.getString(R.string.failed_to_add_farmer_expense), Toast.LENGTH_LONG).show();
                    try {
                        error=response.errorBody().string();
                        Log.e(TAG, "Cost Error " + response.errorBody().string().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                long newMillis=AppConstants.getCurrentMills();
                long diff=newMillis-currMillis;
                if(diff>=DELAY_API||(diff<DELAY_API&&error!=null&&!TextUtils.isEmpty(error))||response.code()==500) {
                    notifyApiDelay(InputCostActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<CostSubmitResponse> call, Throwable t) {
                isLoaded[0]=true;
                long newMillis=AppConstants.getCurrentMills();
                long diff=newMillis-currMillis;
                notifyApiDelay(InputCostActivity.this,call.request().url().toString(),
                        ""+diff,internetSPeed,t.toString(),0);
                Log.e(TAG, "Cost Failure " + t.toString());
                if (t instanceof SocketTimeoutException) {
                    submitCost();
                } else {
                    progressDialog.dismiss();
                    Toasty.error(context, resources.getString(R.string.failed_to_add_farmer_expense), Toast.LENGTH_LONG).show();
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

    private String getDate(String date) {

        SimpleDateFormat submit_sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date exp_date_in_date = null;

        try {
            exp_date_in_date = new SimpleDateFormat("dd/MM/yyyy").parse(date.trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return submit_sdf.format(exp_date_in_date);


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    private void getDropdownData() {


        progressDialog.show();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();
        apiInterface.getInputDropDownData(compId,userId,token).enqueue(new Callback<CostDropdownResponse>() {
            @Override
            public void onResponse(Call<CostDropdownResponse> call, Response<CostDropdownResponse> response) {
                String error=null;
                isLoaded[0]=true;
                if (response.isSuccessful()) {
                    Log.e(TAG, "Dropdown Res " + new Gson().toJson(response.body()));
                    if (response.body().getStatus()!=null&&response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(InputCostActivity.this, resources.getString(R.string.multiple_login_msg));
                    } else
//                    if (response.body().getExpenseDataDDList() != null) {
                        if (response.body().getExpenseDataDDList() != null&&response.body().getExpenseDataDDList().size() > 0) {
                            ExpenseDataDD dataDD = new ExpenseDataDD();
                            dataDD.setType(resources.getString(R.string.select_cost_type_promt));
                            ExpenseDataDD dataDD2 = new ExpenseDataDD();
                            dataDD2.setType(resources.getString(R.string.other_labe));
                            dataDD2.setActivityTypeId("0");
                            expenseDataDDList.add(dataDD);
                            expenseDataDDList.addAll(response.body().getExpenseDataDDList());
                            expenseDataDDList.add(dataDD2);

                            InputCostTypeAdapter adapter = new InputCostTypeAdapter(context, expenseDataDDList);
                            spinner_input_cost.setAdapter(adapter);
                            DropDownT expenseDD = new DropDownT(AppConstants.CHEMICAL_UNIT.INPUT_COST, new Gson().toJson(expenseDataDDList));
                            DropDownCacheManager.getInstance(context).addChemicalUnit(expenseDD);
                        } else {
                            ExpenseDataDD dataDD = new ExpenseDataDD();
                            dataDD.setType(resources.getString(R.string.select_cost_type_promt));
                            ExpenseDataDD dataDD2 = new ExpenseDataDD();
                            dataDD2.setType(resources.getString(R.string.other_labe));
                            dataDD2.setActivityTypeId("0");
                            expenseDataDDList.add(dataDD);
                            expenseDataDDList.add(dataDD2);

                            InputCostTypeAdapter adapter = new InputCostTypeAdapter(context, expenseDataDDList);
                            spinner_input_cost.setAdapter(adapter);
                        }


//                    } else {
//                        spinner_input_cost.setVisibility(View.GONE);
//                        et_other_cost_type.setVisibility(View.VISIBLE);
//                        isOtherInputType = true;
//                    }


//                    if (response.body().getResourceDataDDList() != null) {

                        if (response.body().getResourceDataDDList() != null&&response.body().getResourceDataDDList().size() > 0) {
                            ResourceDataDD dataDD = new ResourceDataDD();
                            dataDD.setType(resources.getString(R.string.select_resource_type_prompt));
                            ResourceDataDD dataDD2 = new ResourceDataDD();
                            dataDD2.setType(resources.getString(R.string.other_labe));
                            resourceDataDDList.add(dataDD);
                            resourceDataDDList.addAll(response.body().getResourceDataDDList());
                            resourceDataDDList.add(dataDD2);
                            InputResourceCostTypeAdapter adapter = new InputResourceCostTypeAdapter(context, resourceDataDDList);
                            spinner_resources_cost.setAdapter(adapter);

                            DropDownT resourceDD = new DropDownT(AppConstants.CHEMICAL_UNIT.RESOURCE_USED, new Gson().toJson(resourceDataDDList));
                            DropDownCacheManager.getInstance(context).addChemicalUnit(resourceDD);
                        } else {
                            ResourceDataDD dataDD = new ResourceDataDD();
                            dataDD.setType(resources.getString(R.string.select_resource_type_prompt));
                            ResourceDataDD dataDD2 = new ResourceDataDD();
                            dataDD2.setType(resources.getString(R.string.other_labe));
                            resourceDataDDList.add(dataDD);
                            resourceDataDDList.add(dataDD2);
                            InputResourceCostTypeAdapter adapter = new InputResourceCostTypeAdapter(context, resourceDataDDList);
                            spinner_resources_cost.setAdapter(adapter);

                        }
//                    } else {
//                        isOtherResourceType = true;
//                    }
                    // }
                    progressDialog.cancel();

                }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(InputCostActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(InputCostActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error=response.errorBody().string().toString();
                        Log.e(TAG, "Dropdown Err " + error);
                        viewFailDialog.showDialogForFinish(InputCostActivity.this, resources.getString(R.string.opps_message),resources.getString(R.string.something_went_wrong_msg));
                        progressDialog.cancel();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                long newMillis=AppConstants.getCurrentMills();
                long diff=newMillis-currMillis;
                if(diff>=DELAY_API||(diff<DELAY_API&&error!=null&&!TextUtils.isEmpty(error))||response.code()==500) {
                    notifyApiDelay(InputCostActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<CostDropdownResponse> call, Throwable t) {
                long newMillis=AppConstants.getCurrentMills();
                long diff=newMillis-currMillis;
                isLoaded[0]=true;
                notifyApiDelay(InputCostActivity.this,call.request().url().toString(),
                        ""+diff,internetSPeed,t.toString(),0);
                Log.e(TAG, "Dropdown Failure " + t.toString());
                viewFailDialog.showDialogForFinish(InputCostActivity.this, resources.getString(R.string.opps_message),resources.getString(R.string.something_went_wrong_msg));
                progressDialog.cancel();

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
                            ConnectivityUtils.checkSpeedWithColor(InputCostActivity.this, new ConnectivityUtils.ColorListener() {
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
                }
            }, AppConstants.DELAY);
        }catch (Exception e){

        }

    }

    private void onClickNew() {
        spinner_input_cost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                expenseDataDD = expenseDataDDList.get(i);
                //Toast.makeText(context, ""+new Gson().toJson(expenseDataDDList.get(i)), Toast.LENGTH_SHORT).show();
                if (expenseDataDDList.get(i).getType().equals(resources.getString(R.string.other_labe))) {
                    isOtherInputType = true;
                    et_other_cost_type.setVisibility(View.VISIBLE);
                } else {
                    isOtherInputType = false;
                    et_other_cost_type.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_resources_cost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(context, ""+new Gson().toJson(expenseDataDDList.get(i)), Toast.LENGTH_SHORT).show();

                resourceDataDD = resourceDataDDList.get(i);
                if (resourceDataDDList.get(i).getType().equals(resources.getString(R.string.select_resource_type_prompt))) {
                    unit_tv.setText(null);
                    unit_tv.setVisibility(View.GONE);
                    et_other_resource_name.setVisibility(View.GONE);
                    et_other_resource_unit.setVisibility(View.GONE);
                    isOtherResourceType = false;
                } else if (resourceDataDDList.get(i).getType().equals(resources.getString(R.string.other_labe))) {
                    unit_tv.setText(null);
                    unit_tv.setVisibility(View.GONE);
                    et_other_resource_name.setVisibility(View.VISIBLE);
                    et_other_resource_unit.setVisibility(View.VISIBLE);
                    isOtherResourceType = true;
                } else {
                    isOtherResourceType = false;
                    unit_tv.setText("(" + resourceDataDDList.get(i).getUnit() + ")");
                    unit_tv.setVisibility(View.VISIBLE);
                    et_other_resource_name.setVisibility(View.GONE);
                    et_other_resource_unit.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addMoreAmountImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpenseDataDD dataDD = (ExpenseDataDD) spinner_input_cost.getSelectedItem();
                if (expenseDataDD != null && expenseDataDD.getType().equals(resources.getString(R.string.select_cost_type_promt))) {
                    spinner_input_cost.getParent().requestChildFocus(spinner_input_cost, spinner_input_cost);
                    Toasty.error(context, resources.getString(R.string.select_cost_type_msg), Toast.LENGTH_LONG).show();
                    isError = true;
                } else if (TextUtils.isEmpty(et_input_cost_amount.getText().toString())) {
                    et_input_cost_amount.getParent().requestChildFocus(et_input_cost_amount, et_input_cost_amount);
                    Toasty.error(context, resources.getString(R.string.enter_cost_ammount_msg), Toast.LENGTH_LONG).show();
                    et_input_cost_amount.setError(resources.getString(R.string.enter_cost_ammount_msg));
                    isError = true;
                } else if (TextUtils.isEmpty(dateTvNew.getText().toString())) {
                    dateTvNew.getParent().requestChildFocus(dateTvNew, dateTvNew);
                    Toasty.error(context, resources.getString(R.string.enter_cost_date_msg), Toast.LENGTH_LONG).show();
                    isError = true;
                    dateTvNew.setError(resources.getString(R.string.enter_cost_date_msg));
                }  else {
                    if (isOtherInputType) {
                        if (TextUtils.isEmpty(et_other_cost_type.getText().toString())) {
                            et_other_cost_type.getParent().requestChildFocus(et_other_cost_type, et_other_cost_type);
                            Toasty.error(context, resources.getString(R.string.enter_expense_type_msg), Toast.LENGTH_LONG).show();
                            et_other_cost_type.setError(resources.getString(R.string.enter_expense_type_msg));
                            isError = true;
                        } else {
                            // Toast.makeText(context, "Cost item added to list", Toast.LENGTH_SHORT).show();
                            InputCostSubmitDatum datum = new InputCostSubmitDatum();
                            datum.setAddedBy(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                            datum.setExpenseAmount(et_input_cost_amount.getText().toString());
                            datum.setExpenseDate(getDate(dateTvNew.getText().toString()));
                            datum.setOtherTypeName(et_other_cost_type.getText().toString());
                            datum.setName(et_other_cost_type.getText().toString());
                            datum.setCostTypeId("0");
                            datum.setFarmId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                            datum.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                            inputCostSubmitList.add(datum);
                            costAdapter.notifyDataSetChanged();
                            spinner_input_cost.setSelection(0);
                            et_other_cost_type.setText(null);
                            et_input_cost_amount.setText(null);
                            dateTvNew.setText(null);
                        }
                    } else {
                        //Toast.makeText(context, "Cost item added to list", Toast.LENGTH_SHORT).show();
                        InputCostSubmitDatum datum = new InputCostSubmitDatum();
                        datum.setName(dataDD.getType());
                        datum.setAddedBy(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                        datum.setExpenseAmount(et_input_cost_amount.getText().toString());
                        datum.setExpenseDate(getDate(dateTvNew.getText().toString()));
                        datum.setOtherTypeName(et_other_cost_type.getText().toString());
                        datum.setCostTypeId(dataDD.getIctId());
                        datum.setFarmId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                        datum.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                        inputCostSubmitList.add(datum);
                        costAdapter.notifyDataSetChanged();

                        spinner_input_cost.setSelection(0);
                        et_other_cost_type.setText(null);
                        et_input_cost_amount.setText(null);
                        dateTvNew.setText(null);
                    }
                }
            }
        });

        addMoreResourceAmountImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResourceDataDD dataDD = (ResourceDataDD) spinner_resources_cost.getSelectedItem();
                if (resourceDataDD.getType().equals(resources.getString(R.string.select_resource_type_prompt))) {
                    spinner_resources_cost.getParent().requestChildFocus(spinner_resources_cost, spinner_resources_cost);
                    Toasty.error(context, resources.getString(R.string.select_resource_type_msg), Toast.LENGTH_LONG).show();
                    isError2 = true;
                } else if (!isOtherResourceType && TextUtils.isEmpty(et_resource_qty.getText().toString())) {
                    et_resource_qty.getParent().requestChildFocus(et_resource_qty, et_resource_qty);
                    Toasty.error(context, resources.getString(R.string.please_enter_resource_qty_msg), Toast.LENGTH_LONG).show();
                    et_resource_qty.setError(resources.getString(R.string.please_enter_resource_qty_msg));
                    isError2 = true;
                } else if (isOtherResourceType && TextUtils.isEmpty(et_other_resource_name.getText().toString())) {
                    et_other_resource_name.getParent().requestChildFocus(et_other_resource_name, et_other_resource_name);
                    Toasty.error(context, resources.getString(R.string.enter_resource_name_msg), Toast.LENGTH_LONG).show();
                    et_other_resource_name.setError(resources.getString(R.string.enter_resource_name_msg));
                    isError2 = true;
                } else if (isOtherResourceType && TextUtils.isEmpty(et_other_resource_unit.getText().toString())) {
                    et_other_resource_unit.getParent().requestChildFocus(et_other_resource_unit, et_other_resource_unit);
                    Toasty.error(context, resources.getString(R.string.enter_resource_unit_msg), Toast.LENGTH_LONG).show();
                    et_other_resource_unit.setError(resources.getString(R.string.enter_resource_unit_msg));
                    isError2 = true;
                } else if (TextUtils.isEmpty(dateTvResource.getText().toString())) {
                    dateTvResource.setError(resources.getString(R.string.select_date_msg));
                    dateTvResource.getParent().requestChildFocus(dateTvResource, dateTvResource);
                    Toasty.error(context, resources.getString(R.string.select_resource_use_date_msg), Toast.LENGTH_LONG).show();
                    isError2 = true;
                } else {
                    if (isOtherResourceType) {
                        //Toast.makeText(context, "Resource item added to list", Toast.LENGTH_SHORT).show();
                        ResourceSubmitDatum datum = new ResourceSubmitDatum();
                        datum.setAddedBy(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                        datum.setQty(et_resource_qty.getText().toString());
                        datum.setQ(et_resource_qty.getText().toString());
                        datum.setName(et_other_resource_name.getText().toString());
                        datum.setOtherResourceType(et_other_resource_name.getText().toString());
                        datum.setResourceUsedDate(getDate(dateTvResource.getText().toString()));
                        datum.setOtherUnit(et_other_resource_unit.getText().toString());
                        datum.setActivityTypeId("0");
                        datum.setResourceTypeId("0");

                        datum.setFarmId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                        datum.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                        resourceSubmitList.add(datum);
                        resourceAdapter.notifyDataSetChanged();

                        spinner_resources_cost.setSelection(0);
                        et_resource_qty.setText(null);
                        dateTvResource.setText(null);
                        et_other_resource_unit.setText(null);
                    } else {
                        // Toast.makeText(context, "Resource item added to list", Toast.LENGTH_SHORT).show();
                        ResourceSubmitDatum datum = new ResourceSubmitDatum();
                        datum.setAddedBy(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                        datum.setQty(et_resource_qty.getText().toString());
                        datum.setQ(et_resource_qty.getText().toString());
                        datum.setOtherResourceType("");
                        datum.setOtherUnit("");
                        datum.setName(dataDD.getType());
                        datum.setResourceTypeId(dataDD.getRtId());
                        datum.setResourceUsedDate(getDate(dateTvResource.getText().toString()));
                        datum.setActivityTypeId("0");
                        datum.setUnit(resourceDataDD.getUnit());
                        datum.setFarmId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                        datum.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                        resourceSubmitList.add(datum);
                        resourceAdapter.notifyDataSetChanged();

                        spinner_resources_cost.setSelection(0);
                        dateTvResource.setText(null);
                        et_resource_qty.setText(null);
                        et_other_resource_unit.setText(null);
                    }
                }
            }
        });

        submitCostInputN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<InputCostSubmitDatum> inputDataList = validateSubmitExpense();
                List<ResourceSubmitDatum> resourceSubmitList = validateSubmitResource();
                Log.e(TAG, "InputCostData " + new Gson().toJson(inputDataList));
                Log.e(TAG, "ResourceData " + new Gson().toJson(resourceSubmitList));

                if (!isError && !isError2) {
                    progressDialog.show();
                    if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                        submitOfflineData(inputDataList, resourceSubmitList);
                    } else {
                        submitData(inputDataList, resourceSubmitList);
                    }

                } else {
                    Log.e(TAG, "Not Hitting Api Due to error in filling data ");
                }

            }
        });

    }


    private List<InputCostSubmitDatum> validateSubmitExpense() {
        List<InputCostSubmitDatum> list = new ArrayList<>();

        try {
            list.addAll(inputCostSubmitList);
            if (list.size() == 0) {
                if (!expenseDataDD.getType().equals(resources.getString(R.string.select_cost_type_promt)) || !TextUtils.isEmpty(et_input_cost_amount.getText().toString()) ||
                        !TextUtils.isEmpty(dateTvNew.getText().toString())) {
                    if (expenseDataDD != null && expenseDataDD.getType().equals(resources.getString(R.string.select_cost_type_promt))) {
                        spinner_input_cost.getParent().requestChildFocus(spinner_input_cost, spinner_input_cost);
                        Toasty.error(context, resources.getString(R.string.select_cost_type_msg), Toast.LENGTH_LONG).show();
                        isError = true;
                    } else if (TextUtils.isEmpty(et_input_cost_amount.getText().toString())) {
                        et_input_cost_amount.getParent().requestChildFocus(et_input_cost_amount, et_input_cost_amount);
                        Toasty.error(context, resources.getString(R.string.enter_cost_ammount_msg), Toast.LENGTH_LONG).show();
                        et_input_cost_amount.setError(resources.getString(R.string.enter_cost_ammount_msg));
                        isError = true;
                    } else if (TextUtils.isEmpty(dateTvNew.getText().toString())) {
                        dateTvNew.getParent().requestChildFocus(dateTvNew, dateTvNew);
                        Toasty.error(context, resources.getString(R.string.enter_cost_date_msg), Toast.LENGTH_LONG).show();
                        isError = true;
                        dateTvNew.setError(resources.getString(R.string.enter_cost_date_msg));
                    } else {
                        if (isOtherInputType) {
                            if (TextUtils.isEmpty(et_other_cost_type.getText().toString())) {
                                et_other_cost_type.getParent().requestChildFocus(et_other_cost_type, et_other_cost_type);
                                Toasty.error(context, resources.getString(R.string.enter_expense_type_msg), Toast.LENGTH_LONG).show();
                                et_other_cost_type.setError(resources.getString(R.string.enter_expense_type_msg));
                                isError = true;
                            } else {
                                //Toast.makeText(context, "Cost item added to list", Toast.LENGTH_SHORT).show();
                                isError = false;
                                InputCostSubmitDatum datum = new InputCostSubmitDatum();
                                datum.setAddedBy(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                                datum.setExpenseAmount(et_input_cost_amount.getText().toString());
                                datum.setExpenseDate(getDate(dateTvNew.getText().toString()));
                                datum.setOtherTypeName(et_other_cost_type.getText().toString());
                                datum.setName(et_other_cost_type.getText().toString());
                                datum.setCostTypeId("0");
                                datum.setFarmId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                                datum.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                                list.clear();
                                list.add(datum);
                            }
                        } else {
                            //Toast.makeText(context, "Cost item added to list", Toast.LENGTH_SHORT).show();
                            isError = false;
                            InputCostSubmitDatum datum = new InputCostSubmitDatum();
                            datum.setName(expenseDataDD.getType());
                            datum.setAddedBy(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                            datum.setExpenseAmount(et_input_cost_amount.getText().toString());
                            datum.setExpenseDate(getDate(dateTvNew.getText().toString()));
                            datum.setOtherTypeName(et_other_cost_type.getText().toString());
                            datum.setCostTypeId(expenseDataDD.getIctId());
                            datum.setFarmId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                            datum.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                            list.clear();
                            list.add(datum);
                        }
                    }
                } else {
                    isError = false;
                }
            } else {
                if (!expenseDataDD.getType().equals(resources.getString(R.string.select_cost_type_promt)) || !TextUtils.isEmpty(et_input_cost_amount.getText().toString()) ||
                        !TextUtils.isEmpty(dateTvNew.getText().toString())) {
                    if (expenseDataDD != null && expenseDataDD.getType().equals(resources.getString(R.string.select_cost_type_promt))) {
                        spinner_input_cost.getParent().requestChildFocus(spinner_input_cost, spinner_input_cost);
                        Toasty.error(context, resources.getString(R.string.select_cost_type_msg), Toast.LENGTH_LONG).show();
                        isError = true;
                    } else if (TextUtils.isEmpty(et_input_cost_amount.getText().toString())) {
                        et_input_cost_amount.getParent().requestChildFocus(et_input_cost_amount, et_input_cost_amount);
                        Toasty.error(context, resources.getString(R.string.enter_cost_ammount_msg), Toast.LENGTH_LONG).show();
                        et_input_cost_amount.setError(resources.getString(R.string.enter_cost_ammount_msg));
                        isError = true;
                    } else if (TextUtils.isEmpty(dateTvNew.getText().toString())) {
                        dateTvNew.getParent().requestChildFocus(dateTvNew, dateTvNew);
                        Toasty.error(context, resources.getString(R.string.enter_cost_date_msg), Toast.LENGTH_LONG).show();
                        isError = true;
                        dateTvNew.setError(resources.getString(R.string.enter_cost_date_msg));
                    } else {
                        if (isOtherInputType) {
                            if (TextUtils.isEmpty(et_other_cost_type.getText().toString())) {
                                et_other_cost_type.getParent().requestChildFocus(et_other_cost_type, et_other_cost_type);
                                Toasty.error(context, resources.getString(R.string.enter_expense_type_msg), Toast.LENGTH_LONG).show();
                                et_other_cost_type.setError(resources.getString(R.string.enter_expense_type_msg));
                                isError = true;
                            } else {
                                //Toast.makeText(context, "Cost item added to list", Toast.LENGTH_SHORT).show();
                                isError = false;
                                InputCostSubmitDatum datum = new InputCostSubmitDatum();
                                datum.setAddedBy(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                                datum.setExpenseAmount(et_input_cost_amount.getText().toString());
                                datum.setExpenseDate(getDate(dateTvNew.getText().toString()));
                                datum.setOtherTypeName(et_other_cost_type.getText().toString());
                                datum.setName(et_other_cost_type.getText().toString());
                                datum.setCostTypeId("0");
                                datum.setFarmId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                                datum.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                                if (isApiFailedToUpload)
                                    list.remove(list.size() - 1);
                                list.add(datum);
                            }
                        } else {
                            isError = false;
                            // Toast.makeText(context, "Cost item added to list", Toast.LENGTH_SHORT).show();
                            InputCostSubmitDatum datum = new InputCostSubmitDatum();
                            datum.setName(expenseDataDD.getType());
                            datum.setAddedBy(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                            datum.setExpenseAmount(et_input_cost_amount.getText().toString());
                            datum.setExpenseDate(getDate(dateTvNew.getText().toString()));
                            datum.setOtherTypeName(et_other_cost_type.getText().toString());
                            datum.setCostTypeId(expenseDataDD.getIctId());
                            datum.setFarmId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                            datum.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                            if (isApiFailedToUpload)
                                list.remove(list.size() - 1);
                            list.add(datum);
                        }
                    }
                } else {
                    isError = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toasty.error(context, resources.getString(R.string.something_went_wrong_msg), Toast.LENGTH_LONG).show();
        }
        Log.e(TAG, "Expense Submit Data " + new Gson().toJson(list));
        return list;
    }

    private List<ResourceSubmitDatum> validateSubmitResource() {
        List<ResourceSubmitDatum> list = new ArrayList<>();
        try {
            list.addAll(resourceSubmitList);
            resourceDataDD = (ResourceDataDD) spinner_resources_cost.getSelectedItem();
            if (list.size() == 0) {
                if (!resourceDataDD.getType().equals(resources.getString(R.string.select_resource_type_prompt)) || !TextUtils.isEmpty(et_resource_qty.getText().toString()) ||
                        !TextUtils.isEmpty(dateTvResource.getText().toString())) {
                    if (resourceDataDD.getType().equals(resources.getString(R.string.select_resource_type_prompt))) {
                        spinner_resources_cost.getParent().requestChildFocus(spinner_resources_cost, spinner_resources_cost);
                        Toasty.error(context, resources.getString(R.string.select_resource_type_msg), Toast.LENGTH_LONG).show();
                        isError2 = true;
                    } else if (!isOtherResourceType && TextUtils.isEmpty(et_resource_qty.getText().toString())) {
                        et_resource_qty.getParent().requestChildFocus(et_resource_qty, et_resource_qty);
                        Toasty.error(context, resources.getString(R.string.please_enter_resource_qty_msg), Toast.LENGTH_LONG).show();
                        et_resource_qty.setError(resources.getString(R.string.please_enter_resource_qty_msg));
                        isError2 = true;
                    } else if (isOtherResourceType && TextUtils.isEmpty(et_other_resource_name.getText().toString())) {
                        et_other_resource_name.getParent().requestChildFocus(et_other_resource_name, et_other_resource_name);
                        Toasty.error(context, resources.getString(R.string.enter_resource_name_msg), Toast.LENGTH_LONG).show();
                        et_other_resource_name.setError(resources.getString(R.string.enter_resource_name_msg));
                        isError2 = true;
                    } else if (isOtherResourceType && TextUtils.isEmpty(et_other_resource_unit.getText().toString())) {
                        et_other_resource_unit.getParent().requestChildFocus(et_other_resource_unit, et_other_resource_unit);
                        Toasty.error(context, resources.getString(R.string.enter_resource_unit_msg), Toast.LENGTH_LONG).show();
                        et_other_resource_unit.setError(resources.getString(R.string.enter_resource_unit_msg));
                        isError2 = true;
                    } else if (TextUtils.isEmpty(dateTvResource.getText().toString())) {
                        dateTvResource.setError(resources.getString(R.string.select_date_msg));
                        dateTvResource.getParent().requestChildFocus(dateTvResource, dateTvResource);
                        Toasty.error(context, resources.getString(R.string.select_resource_use_date_msg), Toast.LENGTH_LONG).show();
                        isError2 = true;
                    } else {
                        if (isOtherResourceType) {
                            // Toast.makeText(context, "Resource item added to list", Toast.LENGTH_SHORT).show();
                            isError2 = false;
                            ResourceSubmitDatum datum = new ResourceSubmitDatum();
                            datum.setAddedBy(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                            datum.setQty(et_resource_qty.getText().toString());
                            datum.setQ(et_resource_qty.getText().toString());
                            datum.setName(et_other_resource_name.getText().toString());
                            datum.setOtherResourceType(et_other_resource_name.getText().toString());
                            datum.setResourceUsedDate(getDate(dateTvResource.getText().toString()));
                            datum.setActivityTypeId("0");
                            datum.setOtherUnit(et_other_resource_unit.getText().toString());
                            datum.setOtherMultiplier("0");
                            datum.setResourceTypeId("0");
                            datum.setFarmId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                            datum.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                            list.clear();
                            list.add(datum);
                        } else {
                            // Toast.makeText(context, "Resource item added to list", Toast.LENGTH_SHORT).show();
                            isError2 = false;
                            ResourceSubmitDatum datum = new ResourceSubmitDatum();
                            datum.setOtherUnit("");
                            datum.setOtherMultiplier("0");
                            datum.setAddedBy(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                            datum.setQty(et_resource_qty.getText().toString());
                            datum.setQ(et_resource_qty.getText().toString());
                            datum.setOtherResourceType("");
                            datum.setName(resourceDataDD.getType());
                            datum.setUnit(resourceDataDD.getUnit());
                            datum.setResourceTypeId(resourceDataDD.getRtId());
                            datum.setResourceUsedDate(getDate(dateTvResource.getText().toString()));
                            datum.setActivityTypeId("0");
                            datum.setFarmId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                            datum.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                            list.clear();
                            list.add(datum);
                        }
                    }
                } else {
                    isError2 = false;
                }
            } else {
                if (!resourceDataDD.getType().equals(resources.getString(R.string.select_resource_type_prompt)) || !TextUtils.isEmpty(et_resource_qty.getText().toString()) ||
                        !TextUtils.isEmpty(dateTvResource.getText().toString())) {
                    if (resourceDataDD.getType().equals(resources.getString(R.string.select_resource_type_prompt))) {
                        spinner_resources_cost.getParent().requestChildFocus(spinner_resources_cost, spinner_resources_cost);
                        Toasty.error(context, resources.getString(R.string.select_resource_type_msg), Toast.LENGTH_LONG).show();
                        isError2 = true;
                    } else if (!isOtherResourceType && TextUtils.isEmpty(et_resource_qty.getText().toString())) {
                        et_resource_qty.getParent().requestChildFocus(et_resource_qty, et_resource_qty);
                        Toasty.error(context, resources.getString(R.string.please_enter_resource_qty_msg), Toast.LENGTH_LONG).show();
                        et_resource_qty.setError(resources.getString(R.string.please_enter_resource_qty_msg));
                        isError2 = true;
                    } else if (isOtherResourceType && TextUtils.isEmpty(et_other_resource_name.getText().toString())) {
                        et_other_resource_name.getParent().requestChildFocus(et_other_resource_name, et_other_resource_name);
                        Toasty.error(context, resources.getString(R.string.enter_resource_name_msg), Toast.LENGTH_LONG).show();
                        et_other_resource_name.setError(resources.getString(R.string.enter_resource_name_msg));
                        isError2 = true;
                    } else if (isOtherResourceType && TextUtils.isEmpty(et_other_resource_unit.getText().toString())) {
                        et_other_resource_unit.getParent().requestChildFocus(et_other_resource_unit, et_other_resource_unit);
                        Toasty.error(context, resources.getString(R.string.enter_resource_unit_msg), Toast.LENGTH_LONG).show();
                        et_other_resource_unit.setError(resources.getString(R.string.enter_resource_unit_msg));
                        isError2 = true;
                    } else if (TextUtils.isEmpty(dateTvResource.getText().toString())) {
                        dateTvResource.setError(resources.getString(R.string.select_date_msg));
                        dateTvResource.getParent().requestChildFocus(dateTvResource, dateTvResource);
                        Toasty.error(context, resources.getString(R.string.select_resource_use_date_msg), Toast.LENGTH_LONG).show();
                        isError2 = true;
                    } else {
                        if (isOtherResourceType) {
                            //  Toast.makeText(context, "Resource item added to list", Toast.LENGTH_SHORT).show();
                            isError2 = false;
                            ResourceSubmitDatum datum = new ResourceSubmitDatum();
                            datum.setAddedBy(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                            datum.setQty(et_resource_qty.getText().toString());
                            datum.setQ(et_resource_qty.getText().toString());
                            datum.setName(et_other_resource_name.getText().toString());
                            datum.setOtherResourceType(et_other_resource_name.getText().toString());
                            datum.setResourceUsedDate(getDate(dateTvResource.getText().toString()));
                            datum.setActivityTypeId("0");
                            datum.setResourceTypeId("0");
                            datum.setOtherUnit(et_other_resource_unit.getText().toString());
                            datum.setOtherMultiplier("0");
                            datum.setFarmId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                            datum.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                            if (isApiFailedToUpload)
                                list.remove(list.size() - 1);
                            list.add(datum);
                        } else {
                            // Toast.makeText(context, "Resource item added to list", Toast.LENGTH_SHORT).show();
                            isError2 = false;
                            ResourceSubmitDatum datum = new ResourceSubmitDatum();
                            datum.setOtherUnit("");
                            datum.setOtherMultiplier("0");
                            datum.setAddedBy(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                            datum.setQty(et_resource_qty.getText().toString());
                            datum.setQ(et_resource_qty.getText().toString());
                            datum.setOtherResourceType("");
                            datum.setName(resourceDataDD.getType());
                            datum.setUnit(resourceDataDD.getUnit());
                            datum.setResourceTypeId(resourceDataDD.getRtId());
                            datum.setResourceUsedDate(getDate(dateTvResource.getText().toString()));
                            datum.setActivityTypeId("0");
                            datum.setFarmId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                            datum.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                            if (isApiFailedToUpload)
                                list.remove(list.size() - 1);
                            list.add(datum);

                        }
                    }
                } else {
                    isError2 = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toasty.error(context, resources.getString(R.string.something_went_wrong_msg), Toast.LENGTH_LONG).show();
        }

        Log.e(TAG, "Resource Submit Data " + new Gson().toJson(list));
        return list;
    }

    private void submitData(List<InputCostSubmitDatum> inputDataList, List<ResourceSubmitDatum> resourceSubmitList) {

        if (inputDataList.size() != 0 || resourceSubmitList.size() != 0) {

            //progressDialog.show();
            CostAndResourceSubmitData data = new CostAndResourceSubmitData();
            data.setInputCostSubmitList(inputDataList);
            data.setResourceSubmitList(resourceSubmitList);
            data.setToken(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.TOKEN));
            data.setUserId(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.USER_ID));

            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
            Log.e(TAG, "SENDIND DATA " + new Gson().toJson(data));

            final boolean[] isLoaded = {false};
            long currMillis=AppConstants.getCurrentMills();
            apiInterface.insertCostAndResourceData(data).enqueue(new Callback<CostSubmitResponse>() {
                @Override
                public void onResponse(Call<CostSubmitResponse> call, Response<CostSubmitResponse> response) {
                    String error=null;
                    isLoaded[0]=true;
                    if (response.isSuccessful()) {
                        progressDialog.cancel();
                        Log.e(TAG, "Cost And Resource res " + new Gson().toJson(response.body()));
                        if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(InputCostActivity.this, resources.getString(R.string.multiple_login_msg));
                        } else
                        if (response.body().getStatus() == 1) {
                            ActivityOptions options = null;
                            isApiFailedToUpload = false;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                Intent intent=getIntent();
                                setResult(RESULT_OK);
                                finish();
                                finish();
                            } else {
                                Intent intent=getIntent();
                                setResult(RESULT_OK);
                                finish();
                                finish();
                            }

                            Toasty.success(context, resources.getString(R.string.data_submit_success_msg), Toast.LENGTH_LONG).show();
                        } else {
                            isApiFailedToUpload = true;

                            Toasty.error(context, resources.getString(R.string.failed_to_submit_resource_and_cost_msg), Toast.LENGTH_LONG).show();
                        }
                    } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(InputCostActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(InputCostActivity.this, resources.getString(R.string.authorization_expired));
                    }else {
                        try {
                            isApiFailedToUpload = true;
                            error=response.errorBody().string();
                            progressDialog.cancel();
                            Log.e(TAG, "Cost And Resource " + error);
                            Toasty.error(context, resources.getString(R.string.failed_to_submit_resource_and_cost_msg), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    long newMillis=AppConstants.getCurrentMills();
                    long diff=newMillis-currMillis;
                    if(diff>=DELAY_API||(diff<DELAY_API&&error!=null&&!TextUtils.isEmpty(error))||response.code()==500) {
                        notifyApiDelay(InputCostActivity.this, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }
                }

                @Override
                public void onFailure(Call<CostSubmitResponse> call, Throwable t) {
                    isLoaded[0]=true;
                    Log.e(TAG, "Cost And Resource " + t.toString());
                    long newMillis=AppConstants.getCurrentMills();
                    long diff=newMillis-currMillis;
                    notifyApiDelay(InputCostActivity.this,call.request().url().toString(),
                            ""+diff,internetSPeed,t.toString(),0);
                    progressDialog.cancel();
                    isApiFailedToUpload = true;

                    Toasty.error(context, resources.getString(R.string.failed_to_submit_resource_and_cost_msg), Toast.LENGTH_LONG).show();
                }
            });
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    internetFlow(isLoaded[0]);
                }
            }, AppConstants.DELAY);
        } else {
            progressDialog.cancel();
            Toasty.error(context, resources.getString(R.string.need_to_fill_input_or_resorce_msg), Toast.LENGTH_LONG).show();
        }
    }

    private void submitOfflineData(List<InputCostSubmitDatum> inputDataList, List<ResourceSubmitDatum> resourceSubmitList) {

        if (inputDataList.size() > 0 || resourceSubmitList.size() > 0) {

            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String dateStr = dateFormatter.format(date);

            // Toast.makeText(context, "Date " + dateStr, Toast.LENGTH_SHORT).show();
            for (int i = 0; i < inputDataList.size(); i++) {
                InputCostSubmitDatum inputDatum = inputDataList.get(i);
                InputCostData data = new InputCostData();
                data.setAddedBy(inputDatum.getAddedBy());
                data.setAddedOn(dateStr);
                data.setFarmMasterNum(inputDatum.getFarmId());
                data.setFarmId(inputDatum.getFarmId());
                data.setCompId(inputDatum.getCompId());
                data.setExpense(inputDatum.getExpenseAmount());
                data.setExpenseDate(inputDatum.getExpenseDate());
                data.setType(inputDatum.getCostTypeId());
                data.setName(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NAME));
                data.setOtherTypeName(inputDatum.getOtherTypeName());
                data.setCostTypeId(inputDatum.getCostTypeId());
                inputCostDataList.add(data);
            }
            for (int i = 0; i < resourceSubmitList.size(); i++) {
                ResourceData data = new ResourceData();
                ResourceSubmitDatum datum = resourceSubmitList.get(i);
                data.setActivityTypeId(datum.getActivityTypeId());
                data.setAddedBy(datum.getAddedBy());
                data.setAddedOn(dateStr);
                data.setCompId(datum.getCompId());
                data.setFarmMasterNum(datum.getFarmId());
                data.setFarmId(datum.getFarmId());
                data.setName(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NAME));
                data.setOtherResourceType(datum.getOtherResourceType());
                data.setOtherUnit(datum.getOtherUnit());
                data.setUnit(datum.getUnit());
                data.setResourceId(datum.getResourceTypeId());
                data.setResourceTypeId(datum.getResourceTypeId());
                data.setUsedOn(datum.getResourceUsedDate());
                data.setValue(datum.getQty());
                data.setType(datum.getName());
                resourceDataList.add(data);
            }

            inputCostT.setInputCostJsonOfflineAdded(new Gson().toJson(inputCostDataList));
            inputCostT.setIsUploaded("N");
            InputCostCacheManager.getInstance(context).update( inputCostT);
            resourceCostT.setResourceJsonOffline(new Gson().toJson(resourceDataList));
            resourceCostT.setIsUploaded("N");
            ResourceCacheManager.getInstance(context).update( resourceCostT);

            Toasty.success(context, resources.getString(R.string.data_submit_success_msg), Toast.LENGTH_LONG).show();
            finish();
        } else {
            progressDialog.cancel();
            Toasty.error(context, resources.getString(R.string.need_to_fill_input_or_resorce_msg), Toast.LENGTH_LONG).show();
        }

    }

    private void getDropDownOffline(List<ExpenseDataDD> listExpense, List<ResourceDataDD> listResource) {
        //Expense DD
        ExpenseDataDD dataDDE = new ExpenseDataDD();
        dataDDE.setType(resources.getString(R.string.select_cost_type_promt));
        ExpenseDataDD dataDD2E = new ExpenseDataDD();
        dataDD2E.setType(resources.getString(R.string.other_labe));
        dataDD2E.setActivityTypeId("0");

        //Resource DD
        ResourceDataDD dataDD = new ResourceDataDD();
        dataDD.setType(resources.getString(R.string.select_resource_type_prompt));
        ResourceDataDD dataDD2 = new ResourceDataDD();
        dataDD2.setType(resources.getString(R.string.other_labe));

        //Resource Set Data to adapter and spinner
        resourceDataDDList.add(dataDD);
        resourceDataDDList.addAll(listResource);
        resourceDataDDList.add(dataDD2);
        InputResourceCostTypeAdapter adapter = new InputResourceCostTypeAdapter(context, resourceDataDDList);
        spinner_resources_cost.setAdapter(adapter);

        //Expense Set Data to adapter and spinner
        expenseDataDDList.add(dataDDE);
        expenseDataDDList.addAll(listExpense);
        expenseDataDDList.add(dataDD2E);
        InputCostTypeAdapter adapterE = new InputCostTypeAdapter(context, expenseDataDDList);
        spinner_input_cost.setAdapter(adapterE);
    }

    @Override
    public void onChemicalUnitLoaded(List<DropDownT> chemicalUnitList) {
        List<ExpenseDataDD> listExpenseDD = new ArrayList<>();
        List<ResourceDataDD> listResourceDD = new ArrayList<>();

        for (int i = 0; i < chemicalUnitList.size(); i++) {

            if (AppConstants.CHEMICAL_UNIT.INPUT_COST.trim().equals(chemicalUnitList.get(i).getDataType().trim())) {
                //Timeline Chemicals
                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        JSONArray array = new JSONArray(chemicalUnitList.get(i).getData());
                        for (int j = 0; j < array.length(); j++) {
                            JSONObject object = array.getJSONObject(j);
                            ExpenseDataDD dataDD = new Gson().fromJson(object.toString(), ExpenseDataDD.class);

                            listExpenseDD.add(dataDD);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } else if (AppConstants.CHEMICAL_UNIT.RESOURCE_USED.trim().equals(chemicalUnitList.get(i).getDataType().trim())) {
                //Units
                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        JSONArray array = new JSONArray(chemicalUnitList.get(i).getData());
                        for (int j = 0; j < array.length(); j++) {
                            JSONObject object = array.getJSONObject(j);
                            ResourceDataDD dataDD = new Gson().fromJson(object.toString(), ResourceDataDD.class);

                            listResourceDD.add(dataDD);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        getDropDownOffline(listExpenseDD, listResourceDD);


    }

    @Override
    public void onInputCostLoaded(InputCostT inputCostT) {


        this.inputCostT = inputCostT;
        if (inputCostT!=null) {

            try {
                JSONArray array = new JSONArray(inputCostT.getInputCostJsonOfflineAdded());

                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    InputCostData data = new Gson().fromJson(object.toString(), InputCostData.class);
                    inputCostDataList.add(data);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onInputCostUpdated() {

    }


    @Override
    public void onResourceLoaded(ResourceCostT resourceCost) {

        this.resourceCostT = resourceCost;
        if (resourceCost != null){

            try {
                JSONArray array = new JSONArray(resourceCost.getResourceJsonOffline());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    ResourceData data = new Gson().fromJson(object.toString(), ResourceData.class);
                    resourceDataList.add(data);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
    }else {

        }

    }

    @Override
    public void onResourceUpdated() {

    }
}
