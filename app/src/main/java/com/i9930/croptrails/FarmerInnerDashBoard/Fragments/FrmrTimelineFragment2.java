package com.i9930.croptrails.FarmerInnerDashBoard.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;

import com.i9930.croptrails.SubmitInputCost.InputCostActivity;
import com.i9930.croptrails.Test.ObjectType;
import android.content.Intent;

import com.i9930.croptrails.CommonClasses.TimelineUnits;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.FarmDetails.Model.WeatherForecast;
import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineInnerData;
import com.i9930.croptrails.Test.MyCustomLayoutManager;
import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineResponse;
import com.i9930.croptrails.GerminationAndSpacing.Model.SendGerminationSpacingData;
import com.i9930.croptrails.GerminationAndSpacing.ShowSampleGermination.Model.SampleGerminationDatum;
import com.i9930.croptrails.GerminationAndSpacing.ShowSampleGermination.Model.SampleGerminationStatusNdData;
import com.i9930.croptrails.HarvestReport.HarvestReportActivity;
import com.i9930.croptrails.HarvestReport.Model.ViewHarvestDetails;
import com.i9930.croptrails.ProduceSell.AddProduceSellActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.ShowInputCost.Model.InputCostData;
import com.i9930.croptrails.ShowInputCost.Model.InputCostResponse;
import com.i9930.croptrails.ShowInputCost.Model.ResourceData;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCard;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCardResponse;
import com.i9930.croptrails.Test.PldModel.PldData;
import com.i9930.croptrails.Test.PldModel.PldResponse;
import com.i9930.croptrails.Test.SatusreModel.SatsureData;
import com.i9930.croptrails.Test.SatusreModel.SatsureResponse;
import com.i9930.croptrails.Test.SellModel.SellData;
import com.i9930.croptrails.Test.SellModel.SellResponse;
import com.i9930.croptrails.Test.adapter.TimelineAdapterTest;
import com.i9930.croptrails.Test.model.TimelineHarvest;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Weather.Model.WeatherMainRes;
public class FrmrTimelineFragment2 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private boolean isCurrentLocation;
    ViewFailDialog viewFailDialog = new ViewFailDialog();
    Context context;
    String TAG = "FrmrTimelineFragment";
    RecyclerView timeline_recycler_view;
    LinearLayoutManager layoutManager;
    Resources resources;
    GifImageView progressBar;
    String countryName;
    String stateName;
    String cityName;
    Map<String, String> calendarMap = new HashMap<>();
    Map<String, String> calendarMMap = new HashMap<>();
    Map<String, String> calendarYMap = new HashMap<>();
    WeatherForecast weatherForecast=new WeatherForecast();
    Location currentLocation;
    RelativeLayout no_data_available_calendar;
    List<SampleGerminationDatum> germinationDataList = new ArrayList<>();
    List<Object> objectList = new ArrayList<>();
    List<ObjectType> objectTypeList = new ArrayList<>();
    FloatingActionMenu fabParent;
    FloatingActionButton /*fabChildAddPld,*/ fabChildSellRecord,fabChildViewInputCost, fabChildViewHarvest;
    String todayDateStr;
    public FrmrTimelineFragment2() {
    }


    public static FrmrTimelineFragment2 newInstance(String param1, String param2) {
        FrmrTimelineFragment2 fragment = new FrmrTimelineFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void onClicks() {
        fabChildViewHarvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabParent.isOpened()) {
                    fabParent.close(true);
                }
                Intent intent = new Intent(context, HarvestReportActivity.class);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, options.toBundle());
                    //finish();
                } else {
                    startActivity(intent);
                    //finish();
                }
            }
        });
        fabChildSellRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabParent.isOpened())
                    fabParent.close(true);
                startActivity(new Intent(context, AddProduceSellActivity.class));
            }
        });
        fabChildViewInputCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabParent.isOpened()) {
                    fabParent.close(true);
                }
                Intent intent = new Intent(context, InputCostActivity.class);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frmr_timeline, container, false);
        timeline_recycler_view = view.findViewById(R.id.timeline_recycler_view);
        progressBar = view.findViewById(R.id.timeline_progress);
        no_data_available_calendar = view.findViewById(R.id.no_data_available_calendar_farmer);
        fabChildViewInputCost = view.findViewById(R.id.fabChildViewInputCost);
        fabParent = (FloatingActionMenu) view.findViewById(R.id.fabParent);
        fabChildSellRecord = view.findViewById(R.id.fabChildSellRecord);
        //fabChildAddPld = (FloatingActionButton) view.findViewById(R.id.fabChildAddPld);
        fabChildViewHarvest = (FloatingActionButton) view.findViewById(R.id.fabChildViewHarvest);
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)) {
            fabChildViewInputCost.setVisibility(View.GONE);
        }

        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.HARVEST_ALLOWED)) {
            fabChildViewHarvest.setVisibility(View.GONE);
        }else {
            fabChildViewHarvest.setVisibility(View.GONE);
        }

//        SharedPreferencesMethod.setString(context,SharedPreferencesMethod.SVFARMID,"389");
        onClicks();

        objectList.clear();
        objectTypeList.clear();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        todayDateStr= df.format(c);
        weatherForecast.setDoa(todayDateStr);
        weatherForecast.setIsLoaded('N');
        objectList.add(weatherForecast);
        objectTypeList.add(new ObjectType(todayDateStr, AppConstants.TIMELINE.WR));
        getCalendarData();

        return view;
    }


    private  void startWeatherFetch(){
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                currentLocation = location;
                                callWeatherApi(location.getLatitude(), location.getLongitude(),"curr loc",true);
                            }
                        }
                    });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //   mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private boolean isTodayVisitAdded = false;
    private void getCalendarData() {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        final String farm_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        Log.e(TAG, "FarmId " + farm_id);
        Log.e(TAG, "CompId " + comp_id);
        Log.e(TAG, "UserId " + userId);
        Log.e(TAG, "Token " + token);

        Call<TimelineResponse> getCalenderData = apiInterface.getCalendarData(comp_id, farm_id, userId, token);

        getCalenderData.enqueue(new Callback<TimelineResponse>() {
            @Override
            public void onResponse(Call<TimelineResponse> call, retrofit2.Response<TimelineResponse> response) {
                if (response.isSuccessful()) {
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String todayDateStr = df.format(c);
                    TimelineResponse timelineResponse = response.body();
                    if (response.body().getStatus() == 10) {
                        viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.multiple_login_msg));
                    }  else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                    } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                    }else if (timelineResponse.getStatus() == 1) {

                        boolean added = false;
                        for (int position = 0; position < timelineResponse.getFarmCalData().size(); position++) {
                            if (timelineResponse.getFarmCalData().get(position).getFarmCalActivityId() != null && !timelineResponse.getFarmCalData().get(position).getFarmCalActivityId().equals("0")) {
                                TimelineInnerData innerData = timelineResponse.getFarmCalData().get(position);
                                innerData.setType(AppConstants.TIMELINE.ACT);
                                innerData.setDoa(innerData.getDate());
                                objectList.add(innerData);
                                objectTypeList.add(new ObjectType(timelineResponse.getFarmCalData().get(position).getDate(), AppConstants.TIMELINE.ACT));

                            } else if (timelineResponse.getFarmCalData().get(position).getVisitReportId() != null && !timelineResponse.getFarmCalData().get(position).getVisitReportId().equals("0")) {
                                TimelineInnerData innerData = timelineResponse.getFarmCalData().get(position);
                                innerData.setType(AppConstants.TIMELINE.VR);
                                innerData.setDoa(innerData.getDate());
                                objectList.add(innerData);
                                objectTypeList.add(new ObjectType(timelineResponse.getFarmCalData().get(position).getDate(), AppConstants.TIMELINE.VR));
                                if (!added) {
                                    Date todayDate = null, farmDate = null;
                                    try {
                                        farmDate = df.parse(innerData.getDate());
                                        todayDate = df.parse(todayDateStr);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        Log.e(TAG, "Outer 2 Date Exception " + e.toString());
                                    }
                                    if (todayDate != null && farmDate != null && todayDate.equals(farmDate)) {
                                        isTodayVisitAdded = true;
                                        //fabChildAddVisit.setVisibility(View.GONE);
                                        added = true;
                                    } else {
                                        isTodayVisitAdded = false;
                                        //fabChildAddVisit.setVisibility(View.VISIBLE);
                                    }
                                }

                                //fabChildAddVisit.setVisibility(View.GONE);
                            }

                        }

                        fetcHarvestData();


                    } else {
                        fetcHarvestData();
                    }
                }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        fetcHarvestData();

                        Log.e(TAG, "Calendar Unsuccess " + response.errorBody().string().toString());
                        if (isActivityRunning)
                            viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_load_timeline));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TimelineResponse> call, Throwable t) {
                Log.e(TAG, "Calendar Failure " + t.toString());
                viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_load_timeline));
                fetcHarvestData();

            }
        });
    }


    private void fetcHarvestData() {
        try {
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
            Log.e(TAG, "Farm Id " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
            Log.e(TAG, "USER Id " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            Log.e(TAG, "TOKEN Id " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            Call<ViewHarvestDetails> callHarvestData = apiService.getHarvestDetailStatus(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));

            callHarvestData.enqueue(new Callback<ViewHarvestDetails>() {
                @Override
                public void onResponse(Call<ViewHarvestDetails> call, Response<ViewHarvestDetails> response) {
                    try {

                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.multiple_login_msg));
                            }  else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                            }else {
                                if (response.body() != null) {
                                    ViewHarvestDetails harvestDetails = response.body();
                                    if (harvestDetails.getData1() != null && harvestDetails.getData1().size() > 0) {
                                        int lastBagNo = 0;

                                        for (int i = 0; i < harvestDetails.getData().size(); i++) {
                                            TimelineHarvest timelineHarvest = new TimelineHarvest();
                                            timelineHarvest.setType(AppConstants.TIMELINE.HR);
                                            for (int j = 0; j < harvestDetails.getData().get(i).size(); j++) {
                                                lastBagNo = Integer.parseInt(harvestDetails.getData().get(i).get(j).getBagNo());
                                            }
                                            timelineHarvest.setHarvestDetailInnerDataList(harvestDetails.getData().get(i));
                                            timelineHarvest.setHarvestDetailMaster(harvestDetails.getData1().get(i));
                                            timelineHarvest.setDoa(timelineHarvest.getHarvestDetailMaster().getDoa());
                                            objectList.add(timelineHarvest);
                                            objectTypeList.add(new ObjectType(timelineHarvest.getHarvestDetailMaster().getDoa(), AppConstants.TIMELINE.HR));
                                        }
                                        SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.LAST_BAG_NO, lastBagNo + 1);
                                        getGerminationData();
                                    } else {
                                        SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.LAST_BAG_NO, 1);
                                        getGerminationData();
                                    }
                                } else {
                                    getGerminationData();
                                }
                            }
                        } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                        } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                        }else {
                            try {
                                getGerminationData();

                                Log.e(TAG, "Harvest Err" + response.errorBody().string().toString());
                                //Toast.makeText(context, "Oops something went wrong. Please Reload", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Harvest Exc" + e.toString());

                    }
                }

                @Override
                public void onFailure(Call<ViewHarvestDetails> call, Throwable t) {
                    Log.e(TAG, "Failure HarvestT data " + t.getMessage().toString());
                    getGerminationData();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void getGerminationData() {
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
            SendGerminationSpacingData sendGerminationSpacingData = new SendGerminationSpacingData();
            sendGerminationSpacingData.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
            sendGerminationSpacingData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            sendGerminationSpacingData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));

            Log.e(TAG, "Data " + new Gson().toJson(sendGerminationSpacingData));
            final Call<SampleGerminationStatusNdData> sampleGerminationDatumCall = apiService.getSampleGermiData(sendGerminationSpacingData);
            sampleGerminationDatumCall.enqueue(new Callback<SampleGerminationStatusNdData>() {
                @Override
                public void onResponse(Call<SampleGerminationStatusNdData> call, Response<SampleGerminationStatusNdData> response) {
                    if (response.isSuccessful()) {
                        SampleGerminationStatusNdData sampleGerminationStatusNdData = response.body();
                        try {
                            if (response.isSuccessful()) {
                                if (response != null) {
                                    if (sampleGerminationStatusNdData.getStatus() == 1) {
                                        germinationDataList.addAll(sampleGerminationStatusNdData.getData());
                                        for (int i = 0; i < sampleGerminationStatusNdData.getData().size(); i++) {
                                            SampleGerminationDatum data = sampleGerminationStatusNdData.getData().get(i);
                                            if (data.getDoa() != null && !TextUtils.isEmpty(data.getDoa())) {
                                                data.setType(AppConstants.TIMELINE.GE);
                                                objectList.add(data);
                                                objectTypeList.add(new ObjectType(data.getDoa(), AppConstants.TIMELINE.GE));
                                            } else {
                                                Log.e(TAG, "Germi no doa at " + i);
                                            }
                                        }
                                        getProduceSell();

                                    }  else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                                        //un authorized access
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                                    } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                                        // auth token expired
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                                    }else if (sampleGerminationStatusNdData.getStatus() == 10) {
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.multiple_login_msg));
                                    } else {
                                        getProduceSell();
                                        Log.e(TAG, "status 0 " + new Gson().toJson(response.body()));
                                    }
                                } else {
                                    getProduceSell();
                                }
                            } else {
                                try {
                                    getProduceSell();

                                    Log.e("DataError", response.errorBody().string().toString());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            getProduceSell();
                        }


                    } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                    } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            getProduceSell();
                            Log.e("DataErrorFailure", response.errorBody().string().toString());
                            //Toast.makeText(context, "Oops something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                            Toasty.error(context, resources.getString(R.string.opps_message) + " " + resources.getString(R.string.something_went_wrong_msg), Toast.LENGTH_LONG, false).show();
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    }


                }

                @Override
                public void onFailure(Call<SampleGerminationStatusNdData> call, Throwable t) {
                    getProduceSell();
                    Toasty.error(context, resources.getString(R.string.opps_message) + " " + resources.getString(R.string.something_went_wrong_msg), Toast.LENGTH_LONG, false).show();

                    Log.e("Failure", t.toString());

                }
            });

        }
    }

    private void getProduceSell() {
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
            apiService.getSellData(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID))
                    .enqueue(new Callback<SellResponse>() {
                        @Override
                        public void onResponse(Call<SellResponse> call, Response<SellResponse> response) {
                            if (response.isSuccessful()) {
                                SellResponse data = response.body();
                                try {
                                    if (response.isSuccessful()) {
                                        if (response != null) {
                                            if (data.getStatus() == 1) {

                                                for (int i = 0; i < data.getData().size(); i++) {
                                                    SellData sellData = data.getData().get(i);
                                                    if (sellData.getDoa() != null && !TextUtils.isEmpty(sellData.getDoa())) {
                                                        sellData.setType(AppConstants.TIMELINE.SELL);
                                                        objectList.add(sellData);
                                                        objectTypeList.add(new ObjectType(sellData.getDoa(), AppConstants.TIMELINE.SELL));
                                                    } else {
                                                        Log.e(TAG, "Sell no doa at " + i);
                                                    }
                                                }
                                                getHealthCardData();

                                            } else if (data.getStatus() == 10) {
                                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                                viewFailDialog.showSessionExpireDialog(getActivity());
                                            } else {
                                                getHealthCardData();
                                                Log.e(TAG, "Sell status 0 " + new Gson().toJson(response.body()));
                                            }
                                        }
                                    } else {
                                        try {
                                            getHealthCardData();

                                            Log.e(TAG, "Sell DataError " + response.errorBody().string().toString());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (Exception e) {
                                }


                            }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                            } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                            } else {
                                try {
                                    getHealthCardData();
                                    Log.e(TAG, "Sell DataErrorFailure " + response.errorBody().string().toString());
                                    //Toast.makeText(context, "Oops something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                                    Toasty.error(context, resources.getString(R.string.opps_message) + " " + resources.getString(R.string.something_went_wrong_msg), Toast.LENGTH_LONG, false).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }


                        }

                        @Override
                        public void onFailure(Call<SellResponse> call, Throwable t) {
                            getHealthCardData();
                            Toasty.error(context, resources.getString(R.string.opps_message) + " " + resources.getString(R.string.something_went_wrong_msg), Toast.LENGTH_LONG, false).show();

                            Log.e("Failure", t.toString());

                        }
                    });

           /* SellResponse data = new Gson().fromJson("{" +
                    "   \"status\": 1," +
                    "   \"msg\": \"successfully insert harvest sale data\"," +
                    "   \"data\": [" +
                    "       {" +
                    "           \"harvest_sale_id\": \"1\"," +
                    "           \"farm_id\": \"244\"," +
                    "           \"season_id\": \"3\"," +
                    "           \"sale_qty\": \"100\"," +
                    "           \"rate\": \"10\"," +
                    "           \"amount\": \"20\"," +
                    "           \"sold_on\": \"2019-11-05\"," +
                    "           \"added_by\": \"41\"," +
                    "           \"doa\": \"2019-11-01 12:50:17\"," +
                    "           \"is_active\": \"Y\"," +
                    "           \"name\": \"Mr. Priyan Shah\"" +
                    "       }," +
                    "       {" +
                    "           \"harvest_sale_id\": \"1\"," +
                    "           \"farm_id\": \"244\"," +
                    "           \"season_id\": \"3\"," +
                    "           \"sale_qty\": \"100\"," +
                    "           \"rate\": \"\"," +
                    "           \"amount\": \"\"," +
                    "           \"sold_on\": \"\"," +
                    "           \"added_by\": \"41\"," +
                    "           \"doa\": \"2019-11-05 12:50:17\"," +
                    "           \"is_active\": \"Y\"," +
                    "           \"name\": \"Mr. Priyan Shah\"" +
                    "       }" +
                    "   ]" +
                    "}", SellResponse.class);

            for (int i = 0; i < data.getData().size(); i++) {
                SellData sellData = data.getData().get(i);
                if (sellData.getDoa() != null && !TextUtils.isEmpty(sellData.getDoa())) {
                    sellData.setType(AppConstants.TIMELINE.SELL);
                    objectList.add(sellData);
                    objectTypeList.add(new ObjectType(sellData.getDoa(), AppConstants.TIMELINE.SELL));
                } else {
                    Log.e(TAG, "Germi no doa at " + i);
                }
            }
            getHealthCardData();*/

        }
    }
    private void getHealthCardData() {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        apiInterface.getPreviousHealthCard(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID)).enqueue(new Callback<HealthCardResponse>() {
            @Override
            public void onResponse(Call<HealthCardResponse> call, Response<HealthCardResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 10) {
                        viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == 1) {
                        for (int i = 0; i < response.body().getHealthCardList().size(); i++) {
                            HealthCard card = response.body().getHealthCardList().get(i);
                            card.setType(AppConstants.TIMELINE.HC);
                            if (card != null && card.getDoa() != null && !TextUtils.isEmpty(card.getDoa())) {
                                objectList.add(card);
                                objectTypeList.add(new ObjectType(card.getDoa(), AppConstants.TIMELINE.HC));
                            } else
                                Log.e(TAG, "HealthCard no doa at " + i);
                        }
                        getFarmPldList();
                    } else {
                        getFarmPldList();
                    }
                } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                }else {
                    try {
                        getFarmPldList();
                        Log.e(TAG, "Health Card Err " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<HealthCardResponse> call, Throwable t) {
                Log.e(TAG, "Health Card Fail " + t.toString());
                getFarmPldList();
            }
        });
    }
    private void getFarmPldList() {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        apiInterface.getPldReasonListOfFarm(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID)).enqueue(new Callback<PldResponse>() {
            @Override
            public void onResponse(Call<PldResponse> call, Response<PldResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 10) {
                        viewFailDialog.showSessionExpireDialog(getActivity(), response.body().getMsg());
                    } else if (response.body().getStatus() == 1) {
                        Log.e(TAG, "Pld Data res " + new Gson().toJson(response.body()));
                        if (response.body().getData() != null && response.body().getData().size() > 0) {
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                PldData data = response.body().getData().get(i);
                                data.setType(AppConstants.TIMELINE.PLD);
                                if (data.getDoa() != null && !TextUtils.isEmpty(data.getDoa())) {
                                    objectList.add(data);
                                    objectTypeList.add(new ObjectType(data.getDoa(), AppConstants.TIMELINE.PLD));
                                } else
                                    Log.e(TAG, "pld no doa at " + i);

                            }
                            getInputCosts();
                        } else {
                            getInputCosts();
                        }
                    } else {
                        getInputCosts();
                    }
                }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        getInputCosts();
                        Log.e(TAG, "Pld Err " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PldResponse> call, Throwable t) {
                Log.e(TAG, "Pld Fail " + t.toString());
                getInputCosts();
            }
        });
    }

    private void getInputCosts() {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        // farmId="1";
        apiInterface.getInputCostList(farmId, userId, token).enqueue(new Callback<InputCostResponse>() {
            @Override
            public void onResponse(Call<InputCostResponse> call, Response<InputCostResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.multiple_login_msg));
                    } else {

                        Log.e(TAG, "Show Data INPUT COST " + new Gson().toJson(response.body().getInputCostList()));
                        Log.e(TAG, "Show Data RESOURCE " + new Gson().toJson(response.body().getResourceList()));
                        int inputCostSize = 0;
                        int resourceSize = 0;

                        if (response.body().getInputCostList() != null && response.body().getInputCostList().size() > 0) {
                            inputCostSize = response.body().getInputCostList().size();

                            for (int i = 0; i < inputCostSize; i++) {
                                InputCostData input = response.body().getInputCostList().get(i);
                                input.setTypeC(AppConstants.TIMELINE.IC);
                                input.setDoa(input.getAddedOn());
                                if (input.getAddedOn() != null && !TextUtils.isEmpty(input.getAddedOn())) {
                                    objectList.add(input);
                                    objectTypeList.add(new ObjectType(input.getAddedOn(), AppConstants.TIMELINE.IC));
                                } else
                                    Log.e(TAG, "input no doa at " + i);

                            }

                        } else {
                            inputCostSize = 0;
                        }
                        if (response.body().getResourceList() != null && response.body().getResourceList().size() > 0) {
                            resourceSize = response.body().getResourceList().size();

                            for (int i = 0; i < resourceSize; i++) {
                                ResourceData res = response.body().getResourceList().get(i);
                                res.setTypeC(AppConstants.TIMELINE.RU);
                                res.setDoa(res.getAddedOn());
                                if (res.getAddedOn() != null && !TextUtils.isEmpty(res.getAddedOn())) {
                                    objectList.add(res);
                                    objectTypeList.add(new ObjectType(res.getAddedOn(), AppConstants.TIMELINE.RU));
                                } else
                                    Log.e(TAG, "resource no doa at " + i);

                            }

                        } else {
                            resourceSize = 0;
                        }
                        if (resourceSize == 0 && inputCostSize == 0) {

                        }

                        //setRecyclerData();
                        getSatSureData();

                    }
                }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        //setRecyclerData();
                        getSatSureData();
                        Log.e(TAG, "Input Cost Err " + response.errorBody().string().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<InputCostResponse> call, Throwable t) {
                //setRecyclerData();
                getSatSureData();
                Log.e(TAG, "Input Cost Failure " + t.toString());

            }
        });
    }

    private void getSatSureData() {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        apiInterface.getSatsureData(farmId,userId).enqueue(new Callback<SatsureResponse>() {
            @Override
            public void onResponse(Call<SatsureResponse> call, Response<SatsureResponse> response) {
                if (response.isSuccessful()) {
                    SatsureResponse satsureResponse = response.body();
                    if (satsureResponse != null && satsureResponse.getSatsureData() != null && satsureResponse.getSatsureData().size() > 0) {
                        Log.e(TAG, "SatSure res " + new Gson().toJson(satsureResponse));
                        int ndviPos = 0, ndwiPos = 0;
                        /*for (int i = 0; i < satsureResponse.getData().size(); i++) {
                            SatsureData data = satsureResponse.getData().get(i);
                            if (satsureResponse.getData().get(i).getDate() != null && !TextUtils.isEmpty(satsureResponse.getData().get(i).getDate())) {
                                data.setType(AppConstants.TIMELINE.SAT);
                                data.setDoa(data.getDate());
                                objectList.add(data);
                                objectTypeList.add(new ObjectType(data.getDate(), AppConstants.TIMELINE.SAT));
                            }
                            if (data.getProductId() != null && data.getProductId().trim().equals("3")) {
                                satsureResponse.getData().get(i).setSliderIndex(ndviPos);
                                ndviPos++;

                            } else if (data.getProductId() != null && data.getProductId().trim().equals("4")) {

                                satsureResponse.getData().get(i).setSliderIndex(ndwiPos);
                                ndwiPos++;
                            }
                        }*/
                        for (int i = 0; i < satsureResponse.getSatsureData().size(); i++) {

                            for (int j = 0; j < satsureResponse.getSatsureData().get(i).size(); j++) {
                                SatsureData data = satsureResponse.getSatsureData().get(i).get(j);
                                if (data.getDate() != null && !TextUtils.isEmpty(data.getDate())) {
                                    data.setType(AppConstants.TIMELINE.SAT);
                                    data.setDoa(data.getDate());
                                    data.setSliderIndex(j);
                                    objectList.add(data);
                                    objectTypeList.add(new ObjectType(data.getDate(), AppConstants.TIMELINE.SAT));
                                }
                               /* if (data.getProductId() != null && data.getProductId().trim().equals("3")) {
                                    satsureResponse.getData().get(i).setSliderIndex(ndviPos);
                                    ndviPos++;

                                } else if (data.getProductId() != null && data.getProductId().trim().equals("4")) {

                                    satsureResponse.getData().get(i).setSliderIndex(ndwiPos);
                                    ndwiPos++;
                                }   */
                            }
                        }
                        DataHandler.newInstance().setSatsureDataList(satsureResponse.getData());
                        setRecyclerData();
                    } else {
                        Log.e(TAG, "SatSure else 1");
                        setRecyclerData();
                    }
                } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                }else {
                    try {
                        setRecyclerData();
                        Log.e(TAG, "SatSure else 2 " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SatsureResponse> call, Throwable t) {
                Log.e(TAG, "SatSure Failure " + t.toString());
                setRecyclerData();
            }
        });


    }
    private void setRecyclerData() {
        RecyclerAsync async = new RecyclerAsync();
        async.execute();
    }
    private class RecyclerAsync extends AsyncTask<String, Integer, List<String>> {
        @Override
        protected List<String> doInBackground(String... strings) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Collections.sort(objectList, new Comparator<Object>() {
                @Override
                public int compare(Object lhs, Object rhs) {
                    try {
                        JSONObject obj = new JSONObject(new Gson().toJson(lhs));
                        JSONObject obj2 = new JSONObject(new Gson().toJson(rhs));
                        return format.parse(obj.getString("doa")).compareTo(format.parse(obj2.getString("doa")));
                    } catch (ParseException | JSONException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });
            Collections.sort(objectTypeList, new Comparator<ObjectType>() {
                @Override
                public int compare(ObjectType lhs, ObjectType rhs) {
                    try {
                        return format.parse(lhs.getDate()).compareTo(format.parse(rhs.getDate()));
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });

            for (int i = 0; i < objectList.size(); i++) {
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(objectList.get(i)));
                    Date date = format.parse(obj.getString("doa"));
                    calendarMap.put(format.format(date), obj.getString("doa"));

                    calendarYMap.put(obj.getString("doa").substring(0, 4), obj.getString("doa"));
                    calendarMMap.put(obj.getString("doa").substring(5, 7), obj.getString("doa"));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Log.e(TAG, "Map " + new Gson().toJson(calendarMap));
            Log.e(TAG, "Map M " + new Gson().toJson(calendarMMap));
            Log.e(TAG, "Map Y " + new Gson().toJson(calendarYMap));

            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRecyclerData2();
                    }
                });
            }catch (Exception e){

            }


            return null;
        }
    }

    private void setRecyclerData2() {

        MyCustomLayoutManager manager = new MyCustomLayoutManager(context);
        progressBar.setVisibility(View.GONE);
        List<TimelineUnits> timelineUnits = DataHandler.newInstance().getTimelineUnits();
        adapterTest = new TimelineAdapterTest(getActivity(), context, objectList, objectTypeList, timelineUnits, isCurrentLocation, new TimelineAdapterTest.FarmDetailsClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onFarmDetailsClicked() {

            }

            @Override
            public void onMapIconClicked() {

            }
        });
        timeline_recycler_view.setLayoutManager(manager);
        timeline_recycler_view.setAdapter(adapterTest);

        startWeatherFetch();

    }

    TimelineAdapterTest adapterTest;
    boolean isActivityRunning=true;
    private void callWeatherApi(double latitude, double longitude, String from, boolean isCurrentLocation) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> address = null;
        try {
            address = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (address != null && address.size() > 0) {
                cityName = address.get(0).getSubAdminArea();
                stateName = address.get(0).getAdminArea();
                countryName = address.get(0).getCountryName();
                Log.e(TAG, "Address " + new Gson().toJson(address));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.isCurrentLocation = isCurrentLocation;
        Log.e(TAG, "Calling Weather Api  " + from + "  Latitude " + latitude + ", Longitude " + longitude);
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION));
        ApiInterface weatherService = retrofit.create(ApiInterface.class);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String farmID = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        Call<WeatherMainRes> forecastCall = weatherService.getWeatherData(userId, token, String.valueOf(longitude).trim(), String.valueOf(latitude).trim(), farmID);
        forecastCall.enqueue(new Callback<WeatherMainRes>() {
            @Override
            public void onResponse(Call<WeatherMainRes> call, Response<WeatherMainRes> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG,"Weather Data "+new Gson().toJson(response.body()));
                    if (response.body() != null && response.body().getWeatherForecast() != null) {
                        weatherForecast = response.body().getWeatherForecast();
                        if (weatherForecast != null&&adapterTest!=null) {
                            weatherForecast.setDoa(todayDateStr);
                            weatherForecast.setIsLoaded('Y');
                            weatherForecast.setCityName(cityName);
                            weatherForecast.setStateName(stateName);
                            weatherForecast.setCountryName(countryName);
                            weatherForecast.setType(AppConstants.TIMELINE.WR);
                            adapterTest.notifyWeather(weatherForecast, isCurrentLocation);
                        }
                    }
                } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                }else {
                    try {
                        Log.e(TAG, "error weather " + response.errorBody().string().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherMainRes> call, Throwable t) {
                Log.e(TAG, "WeatherFailure " + t.toString());
            }
        });
    }

}
