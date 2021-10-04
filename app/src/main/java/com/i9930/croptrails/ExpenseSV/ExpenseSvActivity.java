package com.i9930.croptrails.ExpenseSV;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.Landing.Adapter.ExpenseAdapter;
import com.i9930.croptrails.Landing.Fragments.InterFaces.ExpenseRemoveNotifyInterface;
import com.i9930.croptrails.Landing.Fragments.Model.AddExpenseResponse;
import com.i9930.croptrails.Landing.Fragments.Model.Datum;
import com.i9930.croptrails.Landing.Fragments.Model.ExpenseData;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;
import com.i9930.croptrails.Utility.Utility;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class ExpenseSvActivity extends AppCompatActivity implements ExpenseRemoveNotifyInterface{

    ExpenseSvActivity context ;
    String TAG="ExpenseSvActivity";
    Toolbar mActionBarToolbar;
    Resources resources;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ExpenseRemoveNotifyInterface notifyInterface;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    ArrayList<String> encodedImageList;
    JSONObject jsonObject;
    String pictureImagePath = "";
    Bitmap myBitmap;
    Bitmap default_bitmap;
    ImageView img_expense_recycler;
    ExpenseAdapter expenseAdapter;
    RecyclerView expenseDataRecyclerView;
    LinearLayoutManager linearLayoutManager;
    List<Datum> result;
    Datum datum;
    Button democheck;
    EditText et_exp_narration, et_exp_amount;
    String str_et_amount;
    String str_et_date;
    String str_et_narration;
    TextView et_exp_date;
    Calendar expense_date = Calendar.getInstance();
    GifImageView progressBar;
    ConnectivityManager connectivityManager;
    Boolean connected = true;
    RelativeLayout rel_exp_recycler_view;
    RelativeLayout relativeLayout;
    RelativeLayout rel_exp_in_offline_mode;
    RelativeLayout no_data_available;
    Call<ExpenseData> expenseDataCall;
    TextView total_expense_tv;
    int positionnew = 0;

    private int page = 0;
    List<Bitmap> imageBitmapList;
    Uri[] imageUriList;
    private Handler handler_slider;
    private final int delay = 3000;
    LinearLayout ll_dots;
    private TextView[] dots;
    Calendar c = Calendar.getInstance();
    int currentMonth = c.get(Calendar.MONTH) + 1;
    int currentYear = c.get(Calendar.YEAR);

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FloatingActionButton add_fab;
    Dialog add_expense;

    ImageView img_add_expense;
    LinearLayout lay_date;
    TextView select_date;
    EditText edtxt_amount;
    EditText edtxt_narration;
    ImageView close;
    RelativeLayout total_cost_rel_lay;
    TextView this_month_total_expense_tv;

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
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = getResources();
        String languageToLoad = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_sv);

        context = this;
        initView();
        loadAds();
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpenseSvActivity.this.onBackPressed();
            }
        });
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.bottom_nav_expenditure));

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            if (connected) {
                progressBar.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);
                expenseDataRecyclerView.setVisibility(View.VISIBLE);
                rel_exp_in_offline_mode.setVisibility(View.GONE);
                fetchData();
            } else {
                relativeLayout.setVisibility(View.VISIBLE);
                expenseDataRecyclerView.setVisibility(View.GONE);
                rel_exp_in_offline_mode.setVisibility(View.GONE);
            }
            clickListeners();
        } else {
            rel_exp_in_offline_mode.setVisibility(View.VISIBLE);
            expenseDataRecyclerView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.GONE);
        }
    }
    public void clickListeners() {
        img_expense_recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_expense = new Dialog(context);
                add_expense.setContentView(R.layout.dialog_add_expense);
                Window window = add_expense.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                add_expense.show();
                img_add_expense = add_expense.findViewById(R.id.img_add_expense);
                close = add_expense.findViewById(R.id.close);
                lay_date = add_expense.findViewById(R.id.lay_date);
                select_date = add_expense.findViewById(R.id.select_date);
                edtxt_amount = add_expense.findViewById(R.id.edtxt_amount);
                edtxt_narration = add_expense.findViewById(R.id.edtxt_narration);
                Button btn_add = add_expense.findViewById(R.id.btn_add);
                img_add_expense.setEnabled(true);
                lay_date.setEnabled(true);
                select_date.setEnabled(true);
                edtxt_amount.setEnabled(true);
                edtxt_narration.setEnabled(true);
                btn_add.setEnabled(true);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add_expense.dismiss();
                        add_expense.cancel();
                    }
                });

                final DatePickerDialog.OnDateSetListener datesowing = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        expense_date.set(Calendar.YEAR, year);
                        expense_date.set(Calendar.MONTH, monthOfYear);
                        expense_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updatesowingDateLabel();
                    }
                };

                img_add_expense.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage();
                    }
                });
                lay_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog datePickerDialog = new DatePickerDialog(context, datesowing, expense_date
                                .get(Calendar.YEAR), expense_date.get(Calendar.MONTH),
                                expense_date.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                        datePickerDialog.show();
                    }
                });
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        str_et_amount = edtxt_amount.getText().toString().trim();
                        str_et_date = select_date.getText().toString().trim();
                        str_et_narration = edtxt_narration.getText().toString().trim();
                        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                            connected = true;
                        } else {
                            connected = false;
                        }
                        if (connected) {
                            if (str_et_amount.matches("")) {
                                edtxt_amount.setError(resources.getString(R.string.validation_amount_null));
                            } else if (str_et_narration.matches("")) {
                                edtxt_narration.setError(resources.getString(R.string.validation_comment_null));
                            } else if (str_et_date.matches("DD/MM/YYYY")) {
                                select_date.setError(resources.getString(R.string.validation_date_null));
                            } else if (pictureImagePath.matches("")) {
                                // Toast.makeText(context, "Please Upload an image of expenditure", Toast.LENGTH_SHORT).show();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.validation_image_null));
                            } else {
                                progressBar.setVisibility(View.VISIBLE);
                        /*ExpenseActivity.AsyncTaskRunner runner = new ExpenseActivity.AsyncTaskRunner();
                        runner.execute();*/
                                AsyncTaskRunner runner = new AsyncTaskRunner();
                                runner.execute();
                            }
                        } else {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(context, resources.getString(R.string.dialog_heading_unable_to_connect), getString(R.string.internet_not_connected));
                            //Toast.makeText(context, getString(R.string.internet_not_connected), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        democheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_et_amount = et_exp_amount.getText().toString().trim();
                str_et_date = et_exp_date.getText().toString().trim();
                str_et_narration = et_exp_narration.getText().toString().trim();
                //connectivityManager = (ConnectivityManager)v.getSystemService(Context.CONNECTIVITY_SERVICE);

                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    connected = true;
                } else {
                    connected = false;
                }
                if (connected) {
                    if (str_et_amount.matches("")) {
                        et_exp_amount.setError(resources.getString(R.string.validation_amount_null));
                    } else if (str_et_narration.matches("")) {
                        et_exp_narration.setError(resources.getString(R.string.validation_comment_null));
                    } else if (str_et_date.matches("DD/MM/YYYY")) {
                        et_exp_date.setError(resources.getString(R.string.validation_date_null));
                    } else if (pictureImagePath.matches("")) {
                        // Toast.makeText(context, "Please Upload an image of expenditure", Toast.LENGTH_SHORT).show();
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.validation_image_null));
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        /*ExpenseActivity.AsyncTaskRunner runner = new ExpenseActivity.AsyncTaskRunner();
                        runner.execute();*/
                        AsyncTaskRunner runner = new AsyncTaskRunner();
                        runner.execute();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.internet_not_connected), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    View connectivityLine;

    private void initView(){
        notifyInterface = this;
        connectivityLine=findViewById(R.id.connectivityLine);
        imageBitmapList = new ArrayList<>();
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        rel_exp_recycler_view = findViewById(R.id.rel_exp_recycler_view);
        img_expense_recycler = (ImageView) findViewById(R.id.img_expense_recycler);
        expenseDataRecyclerView = (RecyclerView) findViewById(R.id.expence_recyclerview);
        democheck = (Button) findViewById(R.id.demoCheck);
        et_exp_amount = (EditText) findViewById(R.id.amount_et);
        et_exp_date = (TextView) findViewById(R.id.date_et_exp);
        et_exp_narration = (EditText) findViewById(R.id.comment_et);
        progressBar = findViewById(R.id.progressBar_cyclic);
        relativeLayout = (RelativeLayout) findViewById(R.id.no_internet_layout);
        rel_exp_in_offline_mode = (RelativeLayout) findViewById(R.id.on_offline_mode_exp);
        no_data_available = (RelativeLayout) findViewById(R.id.no_data_available);
        add_fab = findViewById(R.id.add_fab);
        result = new ArrayList<>();
        ll_dots = findViewById(R.id.ll_dots);
        total_expense_tv = findViewById(R.id.total_expense_tv);

        this_month_total_expense_tv = findViewById(R.id.this_month_total_expense_tv);

    }


    private void fetchData() {
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();
        expenseDataCall = apiService.getExpenseData(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID), userId, token);
        expenseDataCall.enqueue(new Callback<ExpenseData>() {

            @Override
            public void onResponse(Call<ExpenseData> call, Response<ExpenseData> response) {

                try {
                    String error=null;
                    if (response.isSuccessful()) {
                        Log.e(TAG, "Expense Response " + new Gson().toJson(response.body()));
                        progressBar.setVisibility(View.INVISIBLE);
                        if (response != null) {
                            //result=new ArrayList<>();
                            Log.e("TAG_DATA", response.body().toString() + "        " + response.message() + "      " + response.code());
                            ExpenseData expenseData = response.body();
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                            } else if (expenseData.getStatus() != 0) {
                                rel_exp_recycler_view.setVisibility(View.VISIBLE);
                                Log.e("Expense Previous", new Gson().toJson(expenseData));
                                //WeatherDatum datum=expenseData.getData();
                                result = expenseData.getData();
                                no_data_available.setVisibility(View.GONE);
                                img_expense_recycler.setVisibility(View.VISIBLE);
                                List<Float> list = new ArrayList<>();
                                expenseDataRecyclerView.setBackgroundColor(Color.parseColor("#696969"));
                                expenseAdapter = new ExpenseAdapter(result, context, notifyInterface);
                                expenseDataRecyclerView.setHasFixedSize(true);
                                expenseDataRecyclerView.setAdapter(expenseAdapter);
                                expenseAdapter.notifyDataSetChanged();
                                linearLayoutManager = new LinearLayoutManager(context);
                                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                expenseDataRecyclerView.setLayoutManager(linearLayoutManager);
                                PagerSnapHelper snapHelper = new PagerSnapHelper();
                                snapHelper.attachToRecyclerView(expenseDataRecyclerView);
                                setMonthAndTotalExpense();

                                // expenseDataRecyclerView.addItemDecoration(new LinePagerIndicatorDecoration());
                                expenseDataRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);
                                        //    addBottomDots(newState);
                                        //Toast.makeText(context, "nee State"+newState, Toast.LENGTH_SHORT).show();
                                    }


                                    @Override
                                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                        super.onScrolled(recyclerView, dx, dy);
                                        positionnew = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                                        // Log.e("positionnew",positionnew+"");
                                        if (positionnew != -1) {
                                            addBottomDots(positionnew);
                                        }
                                        // Toast.makeText(context, "dx value :"+dx+" dy value:"+dy, Toast.LENGTH_SHORT).show();
                                    }
                                });

                                scrollMyListViewToBottom();
                            } else {
                                no_data_available.setVisibility(View.VISIBLE);
                                rel_exp_recycler_view.setVisibility(View.GONE);
                                //img_expense_recycler.setVisibility(View.GONE);
                                // Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            error=response.errorBody().string().toString();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(context, resources.getString(R.string.expense_data_upload_failed));
                            Log.e(TAG, "Expense Error  " +error+"   "+response.code());
                            //Toast.makeText(context, "Oops something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
                        notifyApiDelay(context, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }
                } catch (Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showDialog(context, resources.getString(R.string.expense_data_upload_failed));
                    //Toast.makeText(context, "Server error. Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.e("ExpenseFragment", "exception " + e.toString());
                    expenseAdapter = new ExpenseAdapter(result, context, notifyInterface);
                    expenseDataRecyclerView.setHasFixedSize(true);
                    expenseDataRecyclerView.setAdapter(expenseAdapter);
                }

            }


            @Override
            public void onFailure(Call<ExpenseData> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(context, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "Expense Failure " + t.toString());
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialog(context, resources.getString(R.string.expense_data_upload_failed));
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        internetFlow(isLoaded[0]);

    }
    private void setMonthAndTotalExpense() {
        int totalCost = 0;
        for (int i = 0; i < result.size(); i++) {
            totalCost = totalCost + Integer.parseInt(result.get(i).getAmount().trim());
        }
        int thisMonthExpense = 0;
        for (int position = 0; position < result.size(); position++) {
            Log.e("ExpenseFragment", "result date " + result.get(position).getExpDate());
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
            Date d = null;
            try {
                d = input.parse(result.get(position).getExpDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();

            cal.setTime(d);
            if (currentMonth == (cal.get(Calendar.MONTH) + 1) && currentYear == cal.get(Calendar.YEAR)) {
                Log.e("ExpenseFragment", position + " month " + (cal.get(Calendar.MONTH) + 1) + " Year " + cal.get(Calendar.YEAR));
                thisMonthExpense = thisMonthExpense + Integer.parseInt(result.get(position).getAmount().trim());
            } else {
                Log.e("ExpenseFragment", position + "  else month " + (cal.get(Calendar.MONTH) + 1) + "  year " + cal.get(Calendar.YEAR));
                Log.e("ExpenseFragment", position + "  else month curr " + currentMonth + "  year curr " + currentYear);

            }
        }
        this_month_total_expense_tv.setText("Rs " + thisMonthExpense);
        total_expense_tv.setText("Rs " + totalCost);
    }
    private void addBottomDots(int currentPage) {

        ll_dots.removeAllViews();
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            dots = new TextView[result.size()];

            // ll_dots.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(context);
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(18);
                dots[i].setTextColor(getResources().getColor(R.color.white));
                ll_dots.addView(dots[i]);
            }

            if (dots.length > 0)
                dots[currentPage].setTextColor(getResources().getColor(R.color.darkgreen));
        } else {
            dots = new TextView[imageBitmapList.size()];

            //ll_dots.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(context);
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(25);
                dots[i].setTextColor(getResources().getColor(R.color.li8_grey_back));
                ll_dots.addView(dots[i]);
            }

            if (dots.length > 0)
                dots[currentPage].setTextColor(getResources().getColor(R.color.new_theme_dark));
        }
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
                            ConnectivityUtils.checkSpeedWithColor(context, new ConnectivityUtils.ColorListener() {
                                @Override
                                public void onColorChanged(int color, String speed) {
                                    if (color == android.R.color.holo_green_dark) {
                                        connectivityLine.setVisibility(View.GONE);
//                                    connectivityLine.setBackgroundColor(getColor(R.color.blue));
                                    } else {
                                        connectivityLine.setVisibility(View.VISIBLE);
                                        connectivityLine.setBackgroundColor(resources.getColor(color));
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

    public void register(String comp_id, String sv_id, String amount, String exp_date, String img_path, String comment, String category_id) {

        try {
            // create upload service client
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface service = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);

            // add another part within the multipart request
            RequestBody requestCompId = RequestBody.create(MediaType.parse("multipart/form-data"), comp_id);
            // add another part within the multipart request
            RequestBody requestsv_id = RequestBody.create(MediaType.parse("multipart/form`-data"), sv_id);

            RequestBody requestAmount = RequestBody.create(MediaType.parse("multipart/form-data"), amount);
            // add another part within the multipart request
            RequestBody requestexp_id = RequestBody.create(MediaType.parse("multipart/form-data"), exp_date);

            RequestBody userIdReq = RequestBody.create(MediaType.parse("multipart/form-data"), userId);
            RequestBody tokenReq = RequestBody.create(MediaType.parse("multipart/form-data"), token);
           /* RequestBody requestimg_url =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), img_url);*/
            final RequestBody request_comment = RequestBody.create(MediaType.parse("multipart/form-data"), comment);
            RequestBody requestcategory_id = RequestBody.create(MediaType.parse("multipart/form-data"), category_id);

            ByteArrayOutputStream byteArrayOutputStreamObject1;
            byteArrayOutputStreamObject1 = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStreamObject1);
            final String ConvertImage1 = Base64.encodeToString(byteArrayOutputStreamObject1.toByteArray(), Base64.DEFAULT);

            RequestBody request_exp_image = RequestBody.create(MediaType.parse("multipart/form-data"), ConvertImage1);

            JsonObject jsonObject=new JsonObject();

            jsonObject.addProperty("comp_id",""+comp_id);
            jsonObject.addProperty("amount",""+amount);
            jsonObject.addProperty("sv_id",""+sv_id);
            jsonObject.addProperty("exp_date",""+exp_date);
            jsonObject.addProperty("comment",""+comment);
            jsonObject.addProperty("category_id",""+category_id);
            jsonObject.addProperty("user_id",""+userId);
            jsonObject.addProperty("token",""+token);

            Log.e(TAG,"SEND DATA "+new Gson().toJson(jsonObject));
            jsonObject.addProperty("exp_img",""+ConvertImage1);
            // finally, execute the request
            Call<AddExpenseResponse> call = service.registerUser2(jsonObject);
            final boolean[] isLoaded = {false};
            long currMillis=AppConstants.getCurrentMills();
            call.enqueue(new Callback<AddExpenseResponse>() {
                @Override
                public void onResponse(Call<AddExpenseResponse> call, Response<AddExpenseResponse> response) {
                    String error=null;
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                        } else {
                            String data = "";
                            AddExpenseResponse expenseResponse = response.body();
                            data = expenseResponse.getImg_link();
                            Log.e("Reponse new ", "Link " + data);
                            String newData = data.replace("\\", "");
                            Log.e("Reponse new ", "Link D " + newData);
                            SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
                            Date d = null;

                            try {
                                d = input.parse(str_et_date);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            datum = new Datum();
                            datum.setAmount(str_et_amount);
                            datum.setCategoryId("0");
                            datum.setComment(str_et_narration);
                            datum.setCompId("0");
                            datum.setDoa("12/1234/12");
                            datum.setExpDate(output.format(d));
                            datum.setImgUrl(newData);
                            datum.setIsActive("Y");
                            datum.setPersonId("1");
                            datum.setSvDailyExpId("1");
                            result.add(datum);


                            //expenseAdapter.notifyItemInserted(result.size() - 1);
                            expenseAdapter = new ExpenseAdapter(result, context, notifyInterface);
                            expenseDataRecyclerView.setHasFixedSize(true);
                            expenseDataRecyclerView.setAdapter(expenseAdapter);
                            expenseAdapter.notifyDataSetChanged();
                            linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            expenseDataRecyclerView.setLayoutManager(linearLayoutManager);
                            scrollMyListViewToBottom();
                            setMonthAndTotalExpense();
                            progressBar.setVisibility(View.INVISIBLE);

                            default_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.photo_big);
                            et_exp_amount.setText("");
                            et_exp_date.setText("DD/MM/YYYY");
                            et_exp_narration.setText("");
                            img_expense_recycler.setImageBitmap(default_bitmap);
                            pictureImagePath = "";
                            Toasty.success(context, resources.getString(R.string.expense_added), Toast.LENGTH_LONG, true).show();
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            error=response.errorBody().string().toString();
                            //Toast.makeText(context, "Error Occurred. Please try again later", Toast.LENGTH_LONG).show();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(context, resources.getString(R.string.expense_error_occured));
                            Log.e("Expense err", error);
                            progressBar.setVisibility(View.INVISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
                        notifyApiDelay(context, response.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response.code());
                    }
                }

                @Override
                public void onFailure(Call<AddExpenseResponse> call, Throwable t) {
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    notifyApiDelay(context, call.request().url().toString(),
                            "" + diff, internetSPeed, t.toString(), 0);
                    progressBar.setVisibility(View.INVISIBLE);
                    //Toast.makeText(context, "Error Occurred.", Toast.LENGTH_LONG).show();
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showDialog(context, resources.getString(R.string.expense_error_occured));
                    Log.e("Expense Ex", t.toString());
                }
            });
            internetFlow(isLoaded[0]);


        } catch (Exception e) {
            e.printStackTrace();

            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(context, resources.getString(R.string.txt_server_error), Toast.LENGTH_SHORT).show();


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
            }
        }
    }

    private int SELECT_FILE1 = 1, SELECT_FILE2 = 2;

    public boolean checkStoragePermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(getString(R.string.permission_necessary));
                    alertBuilder.setMessage(getString(R.string.external_storage_permission_is_necessary));
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            requestGalleryPermission();
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                    return false;
                } else {
                    ActivityCompat.requestPermissions(context,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            SELECT_FILE1);

                    return false;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean checkCameraPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                        Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(getString(R.string.permission_necessary));
                    alertBuilder.setMessage("Camera permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            requestCameraAndStoragePermission();
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                    return false;
                } else {
                    ActivityCompat.requestPermissions(context,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, SELECT_FILE2
                    );

                    return false;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void requestCameraAndStoragePermission() {
        ActivityCompat.requestPermissions(context,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, SELECT_FILE2
        );

    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(context,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                SELECT_FILE1);
    }



    private void selectImage() {
        final CharSequence[] items = {resources.getString(R.string.image_take_photo),
                resources.getString(R.string.image_choose_from_library),
                resources.getString(R.string.image_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(resources.getString(R.string.add_photo_msg));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
//                boolean result = Utility.checkPermission(context);
//                boolean resultCam = checkCameraPermission(context);
//                boolean resultGal = checkStoragePermission(context);
                if (items[item].equals(resources.getString(R.string.image_take_photo))) {
                    userChoosenTask = "Take Photo";
                    if (checkCameraPermission())
                        cameraIntent();

                } else if (items[item].equals(resources.getString(R.string.image_choose_from_library))) {
                    userChoosenTask = "Choose from Library";
                    if (checkStoragePermission())
                        galleryIntent();

                } else if (items[item].equals(resources.getString(R.string.image_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //  intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.image_select_image_title)), SELECT_FILE);
    }


    private void cameraIntent() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    private void onCaptureImageResult(Intent data) {
        File imgFile = new File(pictureImagePath);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            myBitmap = getResizedBitmap(myBitmap, 720, 1080);
            Point ptext = new Point();
            //  ptext.set(180, 900);
            Point pimage = new Point();
            pimage.set(100, 950);
            myBitmap = addWaterMark(myBitmap, "", ptext, pimage);
            if (myBitmap != null) {
                //ImageUploadToServerFunction();
                //register("0","3","2018/07/03",pictureImagePath,"Hello","0");
                img_add_expense.setImageBitmap(myBitmap);
            } else {
                Toast.makeText(context, resources.getString(R.string.expense_error_occured), Toast.LENGTH_SHORT).show();
            }

            // Exif.setText(ReadExif(imgFile.getAbsolutePath()));

        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        pictureImagePath = cursor.getString(columnIndex);
        cursor.close();
        myBitmap = BitmapFactory.decodeFile(pictureImagePath);
        myBitmap = getResizedBitmap(myBitmap, 720, 1080);
        Point ptext = new Point();
        //  ptext.set(180, 900);
        Point pimage = new Point();
        pimage.set(100, 950);
        myBitmap = addWaterMark(myBitmap, "", ptext, pimage);
        if (myBitmap != null) {
            //ImageUploadToServerFunction();
            //register("0","3","2018/07/03",pictureImagePath,"Hello","0");
            img_add_expense.setImageBitmap(myBitmap);
        } else {
            Toast.makeText(context, resources.getString(R.string.expense_error_occured), Toast.LENGTH_SHORT).show();
        }
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        // CREATE A MATRIX FOR THE MANIPULATION
        float scaleWidth = (float) (2.0 / 3.0);
        float scaleHeight = (float) (2.0 / 3.0);

        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @Override
    public void expenseDeletedSuccess(int i) {
        result.remove(i);
        expenseAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.INVISIBLE);
        setMonthAndTotalExpense();
        scrollMyListViewToBottom();
    }

    @Override
    public void expenseDeletingFailed() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void deletingExpense() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {
        public AsyncTaskRunner() {
            super();
        }

        String str = select_date.getText().toString().trim();

        @Override
        protected String doInBackground(String... params) {

            String submit_format = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat submit_sdf = new SimpleDateFormat(submit_format, Locale.US);
            Date exp_date_in_date = null;

            try {
                //exp_date_in_date = new SimpleDateFormat("dd/MM/yyyy").parse(et_exp_date.getText().toString().trim());

                exp_date_in_date = new SimpleDateFormat("dd/MM/yyyy").parse(str);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            String str_exp_date_final = submit_sdf.format(exp_date_in_date);


            register(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID), SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID), str_et_amount, str_exp_date_final, pictureImagePath, str_et_narration, "0");

            //public void register(String comp_id, String sv_id, String amount,String exp_date, String img_path,String comment, String category_id){
            add_expense.dismiss();
            return null;
        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String s) {


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

   
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void updatesowingDateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        //et_exp_date.setText(sdf.format(expense_date.getTime()));
        select_date.setText(sdf.format(expense_date.getTime()));
        select_date.setError(null);
        //et_exp_date.setError(null);

    }

    private void scrollMyListViewToBottom() {
        if (result != null) {
            if (result.size() > 0) {
                expenseDataRecyclerView.scrollToPosition(result.size() - 1);
                final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                //linearLayoutManager.setReverseLayout(true);
                linearLayoutManager.setStackFromEnd(true);
            }
        }
        //expenseDataRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private Bitmap addWaterMark(Bitmap src, String watermarkText, Point location, Point imageLocation) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();
        //apply color
        //paint.setColor(color);
        //set transparency
        //paint.setAlpha(128);
        //set text size
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        //set should be underlined or not
        paint.setUnderlineText(true);
        //draw text on given location
        canvas.drawText(watermarkText, location.x, location.y, paint);

        Bitmap waterMark = BitmapFactory.decodeResource(context.getResources(), R.drawable.ct_water_mark);
        Log.e(TAG, "Canvas" + canvas.getWidth() + " " + canvas.getHeight());
        if (canvas.getWidth() > canvas.getHeight()) {
            canvas.drawBitmap(waterMark, (9 * (canvas.getWidth() / 10) - 50), (9 * (canvas.getHeight() / 10) - 200), null);
            Log.e(TAG, "CanvasW" + (8 * (canvas.getWidth() / 10) - 100) + " " + (9 * (canvas.getHeight() / 10) - 200));

        } else {
            canvas.drawBitmap(waterMark, (8 * (canvas.getWidth() / 10) - 50), (9 * (canvas.getHeight() / 10) - 70), null);
            Log.e(TAG, "CanvasH" + (8 * (canvas.getWidth() / 10) - 50) + " " + (9 * (canvas.getHeight() / 10) - 70));
        }
        //  Log.e(TAG,imageLocation.x+" "+imageLocation.y);
        //Log.e(TAG,"Canvas"+(8*(canvas.getWidth()/10)-50)+" "+(9*(canvas.getHeight()/10)-70));


        return result;
    }
}