package com.i9930.croptrails.Task.TaskDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.FarmNavigation.FarmNavigationActivity;
import com.i9930.croptrails.Maps.ShowArea.Model.geojson.GeoJsonResponse;
import com.i9930.croptrails.Maps.ShowArea.ShowAreaWithUpdateActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.Task.SvTask;
import com.i9930.croptrails.RoomDatabase.Task.TaskCacheManager;
import com.i9930.croptrails.Task.Model.TaskDatum;
import com.i9930.croptrails.Task.SvTaskActivityActivity;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class TaskDetailsActivity extends AppCompatActivity {

    TaskDetailsActivity context;
    String TAG = "TaskDetailsActivity";
    Resources resources;
    Toolbar mActionBarToolbar;
    ViewFailDialog viewFailDialog;
    String personId, userId, token, compId, auth;

    @BindView(R.id.taskTitleTv)
    TextView taskTitleTv;

    @BindView(R.id.taskInstructionTv)
    TextView taskInstructionTv;

    @BindView(R.id.compFarmIdTv)
    TextView compFarmIdTv;

    @BindView(R.id.assignedDateTv)
    TextView assignedDateTv;

    @BindView(R.id.assignedByTv)
    TextView assignedByTv;

    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.taskLayout)
    LinearLayout taskLayout;

    @BindView(R.id.instructionLayout)
    LinearLayout instructionLayout;

    @BindView(R.id.farmIdLayout)
    LinearLayout farmIdLayout;

    @BindView(R.id.assignedDateLayout)
    LinearLayout assignedDateLayout;

    @BindView(R.id.assignedByLayout)
    LinearLayout assignedByLayout;


    @BindView(R.id.completeButton)
    Button completeButton;
    String internetSPeed = "";
    Intent intent;
    int resultCode = Activity.RESULT_CANCELED;
    TaskDatum taskDatum = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        context = this;
        personId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        resources = getResources();
        ButterKnife.bind(this);
        TextView title = findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.task_details));
        mActionBarToolbar = findViewById(R.id.confirm_order_toolbar_layout);
//        mActionBarToolbar.setNavigationIcon(R.drawable.ic_clear_black_24dp);

        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setResult(resultCode, intent);
                finish();
            }
        });

        intent = getIntent();
        setTaskData();
    }

    @Override
    public void onBackPressed() {
        setResult(resultCode, intent);
        finish();
    }
    SvTask cacheData;
    private void setTaskData() {

        cacheData= DataHandler.newInstance().getTaskDatum();


        try {
            taskDatum = new Gson().fromJson(cacheData.getData(), TaskDatum.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (taskDatum != null) {
            Log.d(TAG, "data " + new Gson().toJson(taskDatum));
            String description;
            if (taskDatum.getInstructions() != null) {
//                description = context.getResources().getString(R.string.instructions) + ": " + taskDatum.getInstructions();
//                taskInstructionTv.setText(getSpannedText(context.getResources().getString(R.string.instructions), description));
                taskInstructionTv.setText(taskDatum.getInstructions());
            } else {
                instructionLayout.setVisibility(View.GONE);
            }
            if (AppConstants.isValidString(taskDatum.getTask()))
                taskTitleTv.setText(taskDatum.getTask());
            if (AppConstants.isValidString(taskDatum.getComments()))
                etComment.setText(taskDatum.getComments());


            if (taskDatum.getCompFarmId() != null) {
//                String compFarmId = context.getResources().getString(R.string.lot_no) + ": " + taskDatum.getCompFarmId();
//                compFarmIdTv.setText(getSpannedText(context.getResources().getString(R.string.lot_no), compFarmId));
                compFarmIdTv.setText(taskDatum.getCompFarmId());
            } else {
                farmIdLayout.setVisibility(View.GONE);
            }
            if (taskDatum.getAssignDate() != null) {

                assignedDateTv.setText(AppConstants.getShowableDate(taskDatum.getAssignDate(), context));
            } else {
                assignedDateLayout.setVisibility(View.GONE);
            }


            if (taskDatum.getName() != null) {
//                String name = context.getResources().getString(R.string.assigned_by) + ": " + taskDatum.getName();
//                assignedByTv.setText(getSpannedText(context.getResources().getString(R.string.assigned_by), name));
                assignedByTv.setText(taskDatum.getName());
            } else {
                assignedByLayout.setVisibility(View.GONE);
            }

            if (AppConstants.isValidString(taskDatum.getIsComplete()) && taskDatum.getIsComplete().trim().equalsIgnoreCase("N")) {
                if (AppConstants.isValidString(taskDatum.getAssignDate()) && !taskDatum.getAssignDate().equals("0000:00:00")) {
                    Date todayDate = null, taskDate = null;
                    todayDate = AppConstants.getTodaysDateObj();
                    taskDate = AppConstants.getDateObj(taskDatum.getAssignDate());
                    if (todayDate != null && taskDate != null && (todayDate.after(taskDate) || todayDate.equals(taskDate))) {
                        completeButton.setVisibility(View.VISIBLE);
                        completeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                        Toast.makeText(context, "completing", Toast.LENGTH_SHORT).show();
                                completeTask(taskDatum);
                            }
                        });
                    } else {
                        completeButton.setVisibility(View.GONE);
                    }


                } else {
                    completeButton.setVisibility(View.GONE);
                }

            } else {
                completeButton.setVisibility(View.GONE);
                etComment.setFocusable(false);
                etComment.setEnabled(false);
            }

        } else {
            finish();
        }
    }

    private SpannableStringBuilder getSpannedText(String label, String data) {
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(data);
        ssBuilder.setSpan(
                new StyleSpan(Typeface.BOLD), // span to add
                data.indexOf(label + ":"), // start of the span (inclusive)
                data.indexOf(label + ":") + String.valueOf(label + ":").length(), // end of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // do not extend the span when text add later
        );
        return ssBuilder;
    }


    private void completeTask(TaskDatum taskDatum) {
        completeButton.setEnabled(false);
        completeButton.setClickable(false);
        final String date = AppConstants.getCurrentDateTime();
        final String comment = etComment.getText().toString().trim();
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        jsonObject.addProperty("token", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        jsonObject.addProperty("comp_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));

        jsonObject.addProperty("farm_id", taskDatum.getFarmId());
        jsonObject.addProperty("supervisor_id", personId);
        jsonObject.addProperty("is_complete", "Y");
        jsonObject.addProperty("complete_date", date);
        jsonObject.addProperty("comments", comment);
        if (AppConstants.isValidString(taskDatum.getAssignDate()))
            jsonObject.addProperty("assign_date", taskDatum.getAssignDate());
        if (AppConstants.isValidString(taskDatum.getInstructions()))
            jsonObject.addProperty("instructions", taskDatum.getInstructions());
        if (AppConstants.isValidString(taskDatum.getTask()))
            jsonObject.addProperty("task", taskDatum.getTask());
        jsonObject.addProperty("id", taskDatum.getId());
        Call<StatusMsgModel> statusMsgModelCall = apiService.completeTask(jsonObject);
        Log.d(TAG, "Sending completeTask data " + new Gson().toJson(jsonObject));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        statusMsgModelCall.enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, retrofit2.Response<StatusMsgModel> response) {
                String error = null;
                isLoaded[0] = true;
                if (response.isSuccessful()) {
                    StatusMsgModel statusMsgModel = response.body();
                    if (statusMsgModel.getStatus() == 1) {
                        completeButton.setVisibility(View.GONE);
                        resultCode = Activity.RESULT_OK;
                        intent.putExtra("date", date);
                        intent.putExtra("comment", comment);
                        taskDatum.setIsComplete("Y");
                        taskDatum.setCompleteDate(AppConstants.getCurrentDate());
                        taskDatum.setComments(comment);
                        cacheData.setData(new Gson().toJson(taskDatum));
                        cacheData.setIsComplete("Y");
                        cacheData.setCompleteDate(AppConstants.getCurrentDate());
                        cacheData.setComments(comment);
                        TaskCacheManager.getInstance(context).update(cacheData);
                        AppConstants.successToast(context, resources.getString(R.string.task_completed_successfully));

                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access

                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired

                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                    } else {
                        AppConstants.failToast(context, resources.getString(R.string.failed_to_complete_task));
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access

                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired

                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string();
                        AppConstants.failToast(context, resources.getString(R.string.failed_to_complete_task));
                        Log.e(TAG, "Error " + error);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(context, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
                completeButton.setEnabled(true);
                completeButton.setClickable(true);
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e(TAG, "Failure " + t.toString());
                isLoaded[0] = true;
                completeButton.setEnabled(true);
                completeButton.setClickable(true);
                AppConstants.failToast(context, resources.getString(R.string.failed_to_complete_task));
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(context, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);

            }

        });

    }
}