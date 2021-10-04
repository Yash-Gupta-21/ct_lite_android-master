package com.i9930.croptrails.Task;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.gson.JsonObject;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.Landing.Fragments.Model.Datum;
import com.i9930.croptrails.Landing.Fragments.Model.ExpenseData;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.Task.SvTask;
import com.i9930.croptrails.RoomDatabase.Task.TaskCacheManager;
import com.i9930.croptrails.Task.Model.Farm.SvFarm;
import com.i9930.croptrails.Task.Model.Farm.SvFarmResponse;
import com.i9930.croptrails.Task.Model.TaskDatum;
import com.i9930.croptrails.Task.Model.TaskResponse;
import com.i9930.croptrails.Task.TaskAdapter.TaskAdapter;
import com.i9930.croptrails.Task.TaskDetails.TaskDetailsActivity;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SvTaskActivityActivity extends AppCompatActivity {

    SvTaskActivityActivity context;
    String TAG = "SvTaskActivityActivity";
    Resources resources;
    Toolbar mActionBarToolbar;
    ViewFailDialog viewFailDialog;
    String personId, userId, token, compId, auth;
    @BindView(R.id.svTaskRecyclerView)
    RecyclerView svTaskRecyclerView;
    @BindView(R.id.noTaskAvailable)
    RelativeLayout noTaskAvailable;
    @BindView(R.id.progressBar)
    GifImageView progressBar;
    /*@BindView(R.id.etFromDate)
    EditText etFromDate;
    @BindView(R.id.etToDate)
    EditText etToDate;*/

    @BindView(R.id.totalActivityCount)
    TextView totalActivityCountTv;
    @BindView(R.id.farmCountTv)
    TextView farmCountTv;
    private static final int REQUEST_CODE = 200;
    TaskAdapter adapter;
    int clickIndex = -1;
    TaskCacheManager taskCacheManager;
    Dialog filterDialog;
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
        setContentView(R.layout.activity_sv_task_activity);
        context = this;
        personId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        resources = getResources();
        ButterKnife.bind(this);
        loadAds();
        TextView title = findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.task));
        mActionBarToolbar = findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SvTaskActivityActivity.this.onBackPressed();
            }
        });
        initializeFilterDialog();

        taskCacheManager = TaskCacheManager.getInstance(context);
        getAllDatFromCache();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter, menu);
        MenuItem action_filter = menu.findItem(R.id.action_filter);

        action_filter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                AppConstants.failToast(context,"filter");;
                filterDialog.show();
                return false;
            }
        });
        MenuItem action_refresh = menu.findItem(R.id.action_refresh);

        action_refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                callSvTask("reftrsh");
                return false;
            }
        });

        return true;
    }

    String fromDate = "", toDate = "";

    private void initializeFilterDialog() {
        fromDate = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FILTER_SV_TIMELINE_FROM_DATE);
        toDate = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FILTER_SV_TIMELINE_TO_DATE);
        if (!AppConstants.isValidString(fromDate) || !AppConstants.isValidString(toDate)) {
            fromDate = AppConstants.getBeforeDate();
            toDate = AppConstants.getAfterDate();
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FILTER_SV_TIMELINE_FROM_DATE, fromDate);
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FILTER_SV_TIMELINE_TO_DATE, toDate);
        }
        filterDialog = new Dialog(context);
        filterDialog.setContentView(R.layout.dialog_sv_timeline_filter);
        Window window = filterDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        EditText etFromDate = filterDialog.findViewById(R.id.etFromDate);
        EditText etToDate = filterDialog.findViewById(R.id.etToDate);
        Button applyButton = filterDialog.findViewById(R.id.applyButton);
        ImageView close = filterDialog.findViewById(R.id.close);
        etFromDate.setText(fromDate);
        etToDate.setText(toDate);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
            }
        });
        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(etFromDate);
            }
        });

        etToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(etToDate);
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etFromDate.getText().toString().trim().equals(fromDate) || !etToDate.getText().toString().trim().equals(toDate)) {
                    fromDate = etFromDate.getText().toString().trim();
                    toDate = etToDate.getText().toString().trim();
                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FILTER_SV_TIMELINE_FROM_DATE, fromDate);
                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FILTER_SV_TIMELINE_TO_DATE, toDate);
                    callSvTask("Filter");// call data refresh;
                }
                filterDialog.dismiss();
            }
        });

    }




/*
    private void getSvTasks() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userId);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("comp_id", compId);
        jsonObject.addProperty("supervisor_id", personId);
        Log.e(TAG, "getSvTasks Req " + new Gson().toJson(jsonObject));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getSvTasks(jsonObject).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        TaskResponse taskResponse = response.body();
                        Log.e(TAG, "getSvTasks res " + new Gson().toJson(taskResponse));
                        if (taskResponse.getData() != null && taskResponse.getData().size() > 0) {
                            taskCacheManager.deleteAllTasks(null);
                            new SortTask(taskResponse.getData(), true).execute();
                        } else {
                            svTaskRecyclerView.setVisibility(View.GONE);
                            noTaskAvailable.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e(TAG, "getSvTasks err " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                Log.e(TAG, "getSvTasks fail " + t.toString());
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    class SortTask extends AsyncTask<String, Integer, String> {
        List<TaskDatum> taskDatumList;
        int count = 0;
        boolean insertInCache = false;

        public SortTask(List<TaskDatum> taskDatumList, boolean insertInCache) {
            this.taskDatumList = taskDatumList;
            this.insertInCache = insertInCache;
            Log.e(TAG, "Async " + new Gson().toJson(taskDatumList));
        }

        @Override
        protected String doInBackground(String... strings) {

            if (insertInCache) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Collections.sort(taskDatumList, new Comparator<TaskDatum>() {
                    @Override
                    public int compare(TaskDatum lhs, TaskDatum rhs) {
                        try {
//                        if (AppConstants.isValidString(lhs.getAssignDate())&&AppConstants.isValidString(rhs.getAssignDate())&&
//                                !lhs.getAssignDate().equalsIgnoreCase("Invalid date")&&
//                                !rhs.getAssignDate().equalsIgnoreCase("Invalid date"))
                            return format.parse(lhs.getAssignDate()).compareTo(format.parse(rhs.getAssignDate()));
//                        else return 0;
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });


                for (int i = 0; i < taskDatumList.size(); i++) {
                    TaskDatum taskDatum = taskDatumList.get(i);

                    SvTask svTask=new SvTask(AppConstants.SV_TIMELINE_CARD_TYPES.TASK,taskDatum.getAssignDate(),new Gson().toJson(taskDatum),AppConstants.getCurrentDate(),
                            taskDatum.getIsComplete(),taskDatum.getCompleteDate(),taskDatum.getComments());
                    taskCacheManager.addTask(svTask);

                    if (AppConstants.isValidString(taskDatum.getAssignDate()) && !taskDatum.getAssignDate().equals("0000:00:00") && !taskDatum.getAssignDate().equalsIgnoreCase("Invalid date")) {
                        Date todayDate = null, taskDate = null;
                        todayDate = AppConstants.getTodaysDateObj();
                        taskDate = AppConstants.getDateObj(taskDatum.getAssignDate());
                        if (todayDate != null && taskDate != null && todayDate.equals(taskDate)) {
                            count++;
                        } else {

                        }
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (insertInCache) {
                SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.TASK_COUNT, count);
                taskCacheManager.getAllTasks(new TaskCacheManager.AllTaskFetchListener() {
                    @Override
                    public void onTaskLoaded(List<SvTask> taskList) {
                        if (taskList != null) {
                            svTaskRecyclerView.setHasFixedSize(true);
                            svTaskRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                            adapter = new TaskAdapter(context, taskList, new TaskAdapter.ItemClickListener() {
                                @Override
                                public void onItemClicked(int index, SvTask taskDatum) {
                                    clickIndex = index;
                                    DataHandler.newInstance().setTaskDatum(taskDatum);
                                    startActivityForResult(new Intent(context, TaskDetailsActivity.class), REQUEST_CODE);
                                }
                            });
                            svTaskRecyclerView.setAdapter(adapter);
                        }
                    }
                });
            }


        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String date = null, comment = null;
            if (data != null) {
                date = data.getStringExtra("date");
                comment = data.getStringExtra("comment");

            }
            adapter.markComplete(clickIndex, date, comment);

        }
    }

    private void showDatePicker(EditText editText) {
        String previousDate = editText.getText().toString().trim();
        Date date = AppConstants.getDateObjFromServerFormat(previousDate);
        Calendar calendar = Calendar.getInstance();
        if (date == null) {

        } else {
            calendar.setTime(date);
        }
        final DatePickerDialog.OnDateSetListener add_details_farm_flowering = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = AppConstants.DATE_FORMAT_SERVER; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//                editText.setText(AppConstants.getShowableDate(sdf.format(calendar.getTime()), context));
                editText.setText(sdf.format(calendar.getTime()));

            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, add_details_farm_flowering, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void callSvFarms() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userId);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("comp_id", compId);
        jsonObject.addProperty("added_by", personId);
        jsonObject.addProperty("start_date", fromDate);
        jsonObject.addProperty("end_date", toDate);
        Log.e(TAG, "callSvFarms Req " + new Gson().toJson(jsonObject));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getSvFarms(jsonObject).enqueue(new Callback<SvFarmResponse>() {
            @Override
            public void onResponse(Call<SvFarmResponse> call, Response<SvFarmResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        SvFarmResponse taskResponse = response.body();
                        Log.e(TAG, "callSvFarms res " + new Gson().toJson(taskResponse));
                        if (taskResponse.getData() != null && taskResponse.getData().size() > 0) {
                            new SvFarmAsync(taskResponse.getData()).execute();
                            farmCountTv.setText("" + taskResponse.getData().size());
                        } else {
                            callSvExpenses();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        callSvExpenses();
                        Log.e(TAG, "callSvFarms err " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SvFarmResponse> call, Throwable t) {
                Log.e(TAG, "callSvFarms fail " + t.toString());
                callSvExpenses();
            }
        });
    }

    private void callSvExpenses() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userId);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("comp_id", compId);
        jsonObject.addProperty("sv_id", personId);
        jsonObject.addProperty("start_date", fromDate);
        jsonObject.addProperty("end_date", toDate);
        Log.e(TAG, "callSvExpenses Req " + new Gson().toJson(jsonObject));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getExpenseData(jsonObject).enqueue(new Callback<ExpenseData>() {
            @Override
            public void onResponse(Call<ExpenseData> call, Response<ExpenseData> response) {
                if (response.isSuccessful()) {
                    try {
                        ExpenseData taskResponse = response.body();
                        Log.e(TAG, "callSvExpenses res " + new Gson().toJson(taskResponse));
                        if (taskResponse.getData() != null && taskResponse.getData().size() > 0) {
                            new SvExpenseAsync(taskResponse.getData()).execute();
                        } else {
                            getAllDatFromCache();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getAllDatFromCache();
                    }
                } else {
                    try {
                        getAllDatFromCache();
                        Log.e(TAG, "callSvExpenses err " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ExpenseData> call, Throwable t) {
                Log.e(TAG, "callSvExpenses fail " + t.toString());
                progressBar.setVisibility(View.GONE);
                getAllDatFromCache();
            }

        });

    }

    private void callSvTask(String from) {

        TaskCacheManager.getInstance(context).deleteAllTasks(null);
        Log.e(TAG,"One Hour date "+AppConstants.getCurrentDateTime());
        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SV_TIMELINE_SYNC_1HOUR, AppConstants.getCurrentDateTime());
        progressBar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userId);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("comp_id", compId);
        jsonObject.addProperty("supervisor_id", personId);
        jsonObject.addProperty("start_date", fromDate);
        jsonObject.addProperty("end_date", toDate);
        Log.e(TAG, "getSvTasks Req " + new Gson().toJson(jsonObject)+"\n"+from);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getSvTasks(jsonObject).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        TaskResponse taskResponse = response.body();
                        Log.e(TAG, "getSvTasks res " + new Gson().toJson(taskResponse));
                        if (taskResponse.getData() != null && taskResponse.getData().size() > 0) {
                            new FindTodaysTaskCount(taskResponse.getData()).execute();
                        } else {
                            callSvFarms();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e(TAG, "getSvTasks err " + response.errorBody().string());
                        callSvFarms();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                Log.e(TAG, "getSvTasks fail " + t.toString());
                callSvFarms();
            }
        });
    }

    class FindTodaysTaskCount extends AsyncTask<String, Integer, String> {
        List<TaskDatum> taskDatumList;
        int count = 0;
        TaskCacheManager taskCacheManager;

        public FindTodaysTaskCount(List<TaskDatum> taskDatumList) {
            this.taskDatumList = taskDatumList;
            taskCacheManager = TaskCacheManager.getInstance(context);
            taskCacheManager.deleteAllTasks(new TaskCacheManager.TaskListener() {
                @Override
                public void onTaskFetched(SvTask svTask) {

                }

                @Override
                public void onAllTaskDeleted(boolean isAllDeleted) {

                }
            });
        }

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < taskDatumList.size(); i++) {
                TaskDatum taskDatum = taskDatumList.get(i);
                SvTask svTask = new SvTask(AppConstants.SV_TIMELINE_CARD_TYPES.TASK, taskDatum.getAssignDate(), new Gson().toJson(taskDatum), AppConstants.getCurrentDate(),
                        taskDatum.getIsComplete(), taskDatum.getCompleteDate(), taskDatum.getComments());
                taskCacheManager.addTask(svTask);

                if (AppConstants.isValidString(taskDatum.getAssignDate()) && !taskDatum.getAssignDate().equals("0000:00:00")
                        && !taskDatum.getAssignDate().equalsIgnoreCase("Invalid date")) {
                    Date todayDate = null, taskDate = null;
                    todayDate = AppConstants.getTodaysDateObj();
                    taskDate = AppConstants.getDateObj(taskDatum.getAssignDate());
                    if (todayDate != null && taskDate != null && todayDate.equals(taskDate)) {
                        count++;
                    } else {

                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.TASK_COUNT, count);
            callSvFarms();
        }
    }

    class SvFarmAsync extends AsyncTask<String, Integer, String> {
        List<SvFarm> taskDatumList;
        TaskCacheManager taskCacheManager;

        public SvFarmAsync(List<SvFarm> taskDatumList) {
            this.taskDatumList = taskDatumList;
            taskCacheManager = TaskCacheManager.getInstance(context);

        }

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < taskDatumList.size(); i++) {
                SvFarm taskDatum = taskDatumList.get(i);
                SvTask svTask = new SvTask(AppConstants.SV_TIMELINE_CARD_TYPES.FARM, taskDatum.getDoa(), new Gson().toJson(taskDatum), AppConstants.getCurrentDate(),
                        null, null, null);
                taskCacheManager.addTask(svTask);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            callSvExpenses();
        }
    }

    class SvExpenseAsync extends AsyncTask<String, Integer, String> {
        List<Datum> taskDatumList;
        TaskCacheManager taskCacheManager;

        public SvExpenseAsync(List<Datum> taskDatumList) {
            this.taskDatumList = taskDatumList;
            taskCacheManager = TaskCacheManager.getInstance(context);

        }

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < taskDatumList.size(); i++) {
                Datum taskDatum = taskDatumList.get(i);
                SvTask svTask = new SvTask(AppConstants.SV_TIMELINE_CARD_TYPES.EXPENSE, taskDatum.getExpDate(), new Gson().toJson(taskDatum), AppConstants.getCurrentDate(),
                        null, null, null);
                taskCacheManager.addTask(svTask);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            getAllDatFromCache();
        }

    }

    boolean isCalled = false;

    private void getAllDatFromCache() {
        String oneHourTime = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SV_TIMELINE_SYNC_1HOUR);
        progressBar.setVisibility(View.VISIBLE);
        taskCacheManager.getAllTasks(new TaskCacheManager.AllTaskFetchListener() {
            @Override
            public void onTaskLoaded(List<SvTask> taskList) {
                if (taskList != null) {
                    svTaskRecyclerView.setHasFixedSize(true);
                    svTaskRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new TaskAdapter(context, taskList, new TaskAdapter.ItemClickListener() {
                        @Override
                        public void onItemClicked(int index, SvTask taskDatum) {
                            if (taskDatum.getType() == AppConstants.SV_TIMELINE_CARD_TYPES.TASK) {
                                clickIndex = index;
                                DataHandler.newInstance().setTaskDatum(taskDatum);
                                startActivityForResult(new Intent(context, TaskDetailsActivity.class), REQUEST_CODE);
                            }
                        }
                    });
                    svTaskRecyclerView.setAdapter(adapter);
                    totalActivityCountTv.setText("" + taskList.size());
                } else {
                    if (!isCalled)
                        callSvTask("On List null");
                    isCalled = true;
                }
                progressBar.setVisibility(View.GONE);

            }
        });
        new FarmCountAsync().execute();

        if (!AppConstants.isValidString(oneHourTime)) {
            oneHourTime = AppConstants.getCurrentDateTime();
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SV_TIMELINE_SYNC_1HOUR, oneHourTime);
        }
        if (AppConstants.getDifference(oneHourTime) > 1) {
            if (!isCalled) {
                callSvTask("One hour "+AppConstants.getDifference(oneHourTime));
                isCalled = true;
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SV_TIMELINE_SYNC_1HOUR, AppConstants.getCurrentDateTime());
            }
        }

    }

    private class FarmCountAsync extends AsyncTask<Integer, Integer, Integer> {
        int count = 0;

        @Override
        protected Integer doInBackground(Integer... integers) {
            count = taskCacheManager.getCountByType(AppConstants.SV_TIMELINE_CARD_TYPES.FARM);
            return count;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            farmCountTv.setText("" + count);
        }
    }
}