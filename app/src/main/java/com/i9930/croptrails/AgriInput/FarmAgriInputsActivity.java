package com.i9930.croptrails.AgriInput;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.AgriInput.Adapter.FarmAgriAdapter;
import com.i9930.croptrails.AgriInput.Model.FarmAgriInput;
import com.i9930.croptrails.AgriInput.Model.FarmAgriInputResponse;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

import static com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.HarvestShowSinglePlanMapActivity.dpToPx;

public class FarmAgriInputsActivity extends AppCompatActivity {
    FarmAgriInputsActivity context;
    String TAG = "FarmAgriInputsActivity";
    Resources resources;
    Toolbar mActionBarToolbar;
    @BindView(R.id.agriRecyclerView)
    RecyclerView agriRecyclerView;
    FarmAgriAdapter adapter;
    List<FarmAgriInput> inputList = new ArrayList<>();
    String farmId, userId, token, auth;
    ViewFailDialog viewFailDialog = new ViewFailDialog();
    @BindView(R.id.progressBar)
    GifImageView progressBar;
    @BindView(R.id.noDataTv)
    TextView noDataTv;
    @BindView(R.id.dataLayout)
    LinearLayout dataLayout;

    @BindView(R.id.totalInputTv)
    TextView totalInputTv;
    @BindView(R.id.totalExpQty)
    TextView totalExpQtyTv;
    @BindView(R.id.totalActQty)
    TextView totalActQtyTv;

    @BindView(R.id.chart)
    PieChart chart;

    @BindView(R.id.chartExpected)
    PieChart chartExpected;

    @BindView(R.id.agriBottomLayout)
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_agri_inputs);
        context = this;
        resources = getResources();
        ButterKnife.bind(this);
        farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(getString(R.string.agri_inputs));
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mActionBarToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                }
        );

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setPeekHeight(dpToPx(50));
        bottomSheetBehavior.setHideable(false);


        adapter = new FarmAgriAdapter(context, inputList);

        agriRecyclerView.setHasFixedSize(true);
        agriRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        agriRecyclerView.setAdapter(adapter);

        getInputs();
//        getInputs2();
    }


    private void getInputs() {
        progressBar.setVisibility(View.VISIBLE);

        JsonObject object = new JsonObject();
        object.addProperty("farm_id", farmId);
        object.addProperty("user_id", userId);
        object.addProperty("token", token);
        Log.e(TAG,"getFarmAgriInputs REQ "+new Gson().toJson(object));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getFarmAgriInputs(object).enqueue(new Callback<FarmAgriInputResponse>() {
            @Override
            public void onResponse(Call<FarmAgriInputResponse> call, Response<FarmAgriInputResponse> response) {
                String error = null;
                if (response.isSuccessful()) {
                    FarmAgriInputResponse inputResponse=response.body();
                    Log.e(TAG,"RES "+new Gson().toJson(inputResponse));
                    if (inputResponse.getData()!=null&&inputResponse.getData().size()>0){

                        for (int i=0;i<inputResponse.getData().size();i++) {
                            List<FarmAgriInput>data=inputResponse.getData().get(i);
                            if (data!=null&&data.size()>0)
                            inputList.addAll(data);
                        }

                        inputList.clear();
                        FarmAgriInput i=new FarmAgriInput();
                        i.setName("Seed");
                        i.setExp_quanity(2000L);
                        i.setQuantity(1500L);
                        i.setExp_amount(2000L);
                        i.setAmount(1500L);
                        i.setParameters("Kg");


                        FarmAgriInput i2=new FarmAgriInput();
                        i2.setName("Farm Power");
                        i2.setExp_quanity(12000L);
                        i2.setQuantity(1400L);
                        i2.setExp_amount(12000L);
                        i2.setAmount(1400L);
                        i2.setParameters("Liter");
                        FarmAgriInput i3=new FarmAgriInput();
                        i3.setName("Pesticides");
                        i3.setExp_quanity(3000L);
                        i3.setQuantity(2500L);
                        i3.setExp_amount(3000L);
                        i3.setAmount(2500L);
                        inputList.add(i);
                        inputList.add(i2);
                        inputList.add(i3);
//                        inputList.addAll(inputResponse.getData());
//                        inputList.addAll(inputResponse.getData());
//                        inputList.addAll(inputResponse.getData());
//                        inputList.addAll(inputResponse.getData());
//                        inputList.addAll(inputResponse.getData());
//                        inputList.addAll(inputResponse.getData());
                        adapter.notifyDataSetChanged();
                        new GetCounts().execute();
                    }else{
                        noDataTv.setVisibility(View.VISIBLE);
                        dataLayout.setVisibility(View.GONE);
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access

                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, " Error getFarmAgriInputs " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FarmAgriInputResponse> call, Throwable t) {
                Log.e(TAG, "Failure getFarmAgriInputs " + t.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getInputs2() {
        progressBar.setVisibility(View.VISIBLE);
        JsonObject object = new JsonObject();
        object.addProperty("farm_id", farmId);
        object.addProperty("user_id", userId);
        object.addProperty("token", token);
        Log.e(TAG,"getFarmAgriInputs REQ "+new Gson().toJson(object));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);

        apiInterface.getFarmAgriInputs2(object).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String error = null;
                if (response.isSuccessful()) {
                    try {
                        Log.e(TAG,"getFarmAgriInputs res "+response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, " Error getFarmAgriInputs " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Failure getFarmAgriInputs " + t.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    Map<String, Long>chartMap=new HashMap<>();
    Map<String, Long>chartMapExpected=new HashMap<>();

    private class GetCounts extends AsyncTask<String,Integer,String>{
        long totalExpAmount =0;
        long totalActAmount =0;
        int totalCount=0;
        @Override
        protected String doInBackground(String... strings) {
            totalCount=inputList.size();
            chartMap.clear();
            for (FarmAgriInput input: inputList) {
                totalActAmount = totalActAmount +input.getAmount();
                if (input.getExp_quanity()!=null)
                totalExpAmount = totalActAmount +input.getExp_amount();
                if (AppConstants.isValidString(input.getName())) {
                    if (chartMap.containsKey(input.getName())){
                        long old=chartMap.get(input.getName());
                        long newVal=old+input.getQuantity();
                        chartMap.put(input.getName(),newVal);
                    }else{
//                        if (input.getQuantity()<1)
//                            input.setQuantity(20);
                        chartMap.put(input.getName(),input.getQuantity());
                    }

                    if (input.getExp_quanity()!=null) {
                        if (chartMapExpected.containsKey(input.getName())) {
                            long old = chartMapExpected.get(input.getName());
                            long newVal = old + input.getExp_quanity();
                            chartMapExpected.put(input.getName(), newVal);
                        } else {
//                        if (input.getQuantity()<1)
//                            input.setQuantity(20);

                            chartMapExpected.put(input.getName(), input.getExp_quanity());
                        }

                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            totalInputTv.setText("Total "+totalCount+" added");
            totalExpQtyTv.setText(""+ totalExpAmount);
            totalActQtyTv.setText(""+ totalActAmount);

            setData(chart,chartMap,"Total Actual Expense");
            setData(chartExpected,chartMapExpected,"Total Expected Expense");
        }
    }

    private void setData(PieChart chart,Map<String, Long> chartMap,String label) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        for (Map.Entry<String, Long> entry : chartMap.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            entries.add(new PieEntry(value,key));
        }


        PieDataSet dataSet = new PieDataSet(entries, label);

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(tfLight);
        chart.setData(data);

        // undo all highlights
//        chart.highlightValues(null);

        chart.invalidate();
    }
}