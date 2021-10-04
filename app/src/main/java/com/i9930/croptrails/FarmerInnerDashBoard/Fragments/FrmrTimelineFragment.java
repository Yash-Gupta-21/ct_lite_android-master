package com.i9930.croptrails.FarmerInnerDashBoard.Fragments;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import com.i9930.croptrails.Test.ObjectType;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.TimelineUnits;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.FarmDetails.Adapter.TimelineAdapter;
import com.i9930.croptrails.FarmDetails.Model.WeatherForecast;
import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineInnerData;
import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineResponse;
import com.i9930.croptrails.HarvestReport.HarvestReportActivity;
import com.i9930.croptrails.ProduceSell.AddProduceSellActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.ShowInputCost.ShowInputCostActivity;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Weather.Model.WeatherMainRes;


public class FrmrTimelineFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Context context;
    Call<TimelineResponse> getCalenderData;
    private int todayPosition = 0;
    List<TimelineInnerData> timelineInnerData = new ArrayList<>();
    String TAG = "FrmrTimelineFragment";
    TimelineAdapter timelineAdapter;
    RecyclerView timeline_recycler_view;
    LinearLayoutManager layoutManager;
    Resources resources;
    GifImageView progressBar;
    String countryName;
    String stateName;
    String cityName;
    WeatherForecast weatherForecast=new WeatherForecast();
    Location currentLocation;
    RelativeLayout no_data_available_calendar;
    List<Object> objectList = new ArrayList<>();
    List<ObjectType> objectTypeList = new ArrayList<>();
    FloatingActionMenu fabParent;
    FloatingActionButton /*fabChildAddPld,*/ fabChildSellRecord,fabChildViewInputCost, fabChildViewHarvest;

    public FrmrTimelineFragment() {
    }


    public static FrmrTimelineFragment newInstance(String param1, String param2) {
        FrmrTimelineFragment fragment = new FrmrTimelineFragment();
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
                Intent intent = new Intent(context, ShowInputCostActivity.class);
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
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDateStr = df.format(c);
        weatherForecast.setDoa(todayDateStr);
        weatherForecast.setIsLoaded('N');
        objectList.add(weatherForecast);
        objectTypeList.add(new ObjectType(todayDateStr, AppConstants.TIMELINE.WR));
        onClicks();
        getCalendarData();
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
                                String url = location.getLatitude() + "," + location.getLongitude();
                                Log.e("Test_Location", "  loc " + location.getLatitude() + " " + location.getLongitude());
                                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                try {
                                    List<Address> list = geocoder.getFromLocation(
                                            location.getLatitude(), location.getLongitude(), 1);
                                    if (list != null && list.size() > 0) {
                                        Address address = list.get(0);
                                        Log.e("trace_location", address.toString());
                                    }
                                } catch (IOException e) {

                                }
                            }
                        }
                    });
        }
        return view;
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
    private void getCalendarData() {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        final String farm_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        final String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        final String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        Log.e("TimelineActivity", "FarmId " + farm_id);
        Log.e("TimelineActivity", "CompId " + comp_id);
        getCalenderData = apiInterface.getCalendarData(comp_id, farm_id, userId, token);
        getCalenderData.enqueue(new Callback<TimelineResponse>() {
            @Override
            public void onResponse(Call<TimelineResponse> call, retrofit2.Response<TimelineResponse> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String todayDateStr = df.format(c);
                    // if (DataHandler.newInstance().getAllMaterials() != null && DataHandler.newInstance().getTimelineUnits() != null) {
                    TimelineResponse timelineResponse = response.body();
                    Log.e("Res", timelineResponse.getMsg());

                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(getActivity(), response.body().getMsg());
                    } else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                    } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                    } else if (timelineResponse.getStatus() == 1) {
                        todayPosition = 0;
                        // timeline_progress.setVisibility(View.INVISIBLE);
                        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
                        Date todayDate = null;
                        Date farmDate = null;
                        Date nextFarmDate;
                        Date lastDate = null;
                        try {
                            todayDate = df.parse(todayDateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        try {
                            lastDate = formatDate.parse(timelineResponse.getFarmCalData().get(timelineResponse.getFarmCalData().size() - 1).getDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (todayDate.after(lastDate)) {
                            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)) {
                                timelineResponse.getFarmCalData().add(new TimelineInnerData("0", "100",
                                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                        "0", "0", "I", "reply", "inp"));
                                todayPosition = timelineResponse.getFarmCalData().size() - 1;
                            }

                            TimelineInnerData data = new TimelineInnerData("0", "100",
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                    "0", "0", "W", "reply", "wr");
                            timelineResponse.getFarmCalData().add(data);
                            todayPosition = timelineResponse.getFarmCalData().size() - 2;
                            Log.e(TAG, "Adapter add in Last Positon at " + todayPosition);
                        } else {
                            for (int i = 0; i < timelineResponse.getFarmCalData().size() - 1; i++) {

                                if (timelineResponse.getFarmCalData().get(i).getImg_link() != null &&
                                        !timelineResponse.getFarmCalData().get(i).getImg_link().equals("0")) {
                                    Log.e("MyProblem", "img " + i + " " + timelineResponse.getFarmCalData().get(i).getImg_link());
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_LATEST_IMG, timelineResponse.getFarmCalData().get(i).getImg_link());
                                }
                                try {
                                    farmDate = formatDate.parse(timelineResponse.getFarmCalData().get(i).getDate());
                                    nextFarmDate = formatDate.parse(timelineResponse.getFarmCalData().get(i + 1).getDate());

                                    if (todayDate.before(farmDate) && i == 0) {
                                        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)) {
                                            timelineResponse.getFarmCalData().add(0, new TimelineInnerData("0", "100",
                                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                                    "0", "0", "I", "reply", "inp"));
                                            todayPosition = i;
                                        }
                                        TimelineInnerData data = new TimelineInnerData("0", "100",
                                                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                                "0", "0", "W", "reply", "wr");
                                        timelineResponse.getFarmCalData().add(1, data);
                                        Log.e(TAG, "Adapter add in frst at " + i);
                                        todayPosition = i + 1;
                                        break;
                                    } else if (todayDate.equals(farmDate) && timelineResponse.getFarmCalData().get(i).getActivity() != null) {
                                        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)) {
                                            timelineResponse.getFarmCalData().add(i + 1, new TimelineInnerData("0", "100",
                                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                                    "0", "0", "I", "reply", "inp"));
                                            todayPosition = i;
                                        }

                                        TimelineInnerData data = new TimelineInnerData("0", "100",
                                                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                                "0", "0", "W", "reply", "wr");
                                        timelineResponse.getFarmCalData().add(i + 2, data);
                                        Log.e(TAG, "Adapter add in second at " + i);
                                        todayPosition = i + 1;
                                        break;
                                    } else if (todayDate.equals(farmDate) && timelineResponse.getFarmCalData().get(i).getVisitReportId() != null) {
                                        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)) {
                                            timelineResponse.getFarmCalData().add(i + 1, new TimelineInnerData("0", "100",
                                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                                    "0", "0", "I", "reply", "inp"));
                                            todayPosition = i;
                                        }


                                        TimelineInnerData data = new TimelineInnerData("0", "100",
                                                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                                "0", "0", "W", "reply", "wr");
                                        timelineResponse.getFarmCalData().add(i + 2, data);
                                        Log.e(TAG, "Adapter add in new at " + i + " Whene today visit is submitted");
                                        todayPosition = i + 1;
                                        break;
                                    } else if (farmDate.before(todayDate) && todayDate.before(nextFarmDate)) {
                                        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)) {
                                            timelineResponse.getFarmCalData().add(i + 1, new TimelineInnerData("0", "100",
                                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                                    "0", "0", "I", "reply", "inp"));
                                            todayPosition = i;
                                        }
                                        TimelineInnerData data = new TimelineInnerData("0", "100",
                                                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                                "0", "0", "W", "reply", "wr");
                                        //Toast.makeText(context, "New object added", Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "Adapter add in third at " + i);
                                        timelineResponse.getFarmCalData().add(i + 2, data);
                                        todayPosition = i + 1;
                                        break;

                                    } else if (i == timelineResponse.getFarmCalData().size() - 1 && todayDate.after(farmDate)) {
                                        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)) {
                                            timelineResponse.getFarmCalData().add(new TimelineInnerData("0", "100",
                                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                                    "0", "0", "I", "reply", "inp"));
                                            todayPosition = timelineResponse.getFarmCalData().size() - 2;
                                        }
                                        TimelineInnerData data = new TimelineInnerData("0", "100",
                                                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                                "0", "0", "W", "reply", "wr");
                                        timelineResponse.getFarmCalData().add(data);
                                        todayPosition = timelineResponse.getFarmCalData().size() - 1;
                                        //Toast.makeText(context, "New object added", Toast.LENGTH_SHORT).show();
                                        break;
                                    } else {
                                        Log.e(TAG, "Adapter add Data added for today");
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Log.e("Exception", e.toString());
                                }
                            }
                        }
                        timelineInnerData.addAll(timelineResponse.getFarmCalData());

                        List<TimelineUnits> timelineUnits = DataHandler.newInstance().getTimelineUnits();
                        timelineAdapter = new TimelineAdapter(context, getActivity(), timelineInnerData, timelineUnits, weatherForecast);
                        timeline_recycler_view.setHasFixedSize(true);
                        timeline_recycler_view.setAdapter(timelineAdapter);
                        timelineAdapter.notifyDataSetChanged();

                        layoutManager = new LinearLayoutManager(context);
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        timeline_recycler_view.setLayoutManager(layoutManager);
                        Log.e("", "Today pos " + todayPosition);
                        timeline_recycler_view.getLayoutManager().scrollToPosition(todayPosition);


                    } else {
                        Log.e("Adapter add", "Response not equal 1");
                        timelineInnerData = new ArrayList<>();
                        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED)) {
                            timelineInnerData.add(new TimelineInnerData("0", "100",
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                    "0", "0", "I", "reply", "inp"));
                        }
                        TimelineInnerData data = new TimelineInnerData("0", "100",
                                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                "0", "0", "W", "reply", "wr");
                        timelineInnerData.add(data);
                        todayPosition = 0;
                        List<TimelineUnits> timelineUnits = DataHandler.newInstance().getTimelineUnits();
                        timelineAdapter = new TimelineAdapter(context, getActivity(), timelineInnerData, timelineUnits, weatherForecast);
                        timeline_recycler_view.setHasFixedSize(true);
                        timeline_recycler_view.setAdapter(timelineAdapter);
                        timelineAdapter.notifyDataSetChanged();

                        layoutManager = new LinearLayoutManager(context);
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        timeline_recycler_view.setLayoutManager(layoutManager);
                        Log.e("", "Today pos " + todayPosition);
                        timeline_recycler_view.getLayoutManager().scrollToPosition(todayPosition);
                        Log.e("TimelineActivity", new Gson().toJson(response.body()));

                        if (timelineInnerData != null && timelineInnerData.size() == 0) {
                            no_data_available_calendar.setVisibility(View.VISIBLE);
                            timeline_recycler_view.setVisibility(View.GONE);
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
                } else {
                    try {
                        progressBar.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e("TimelineActivity", response.errorBody().string().toString());
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_load_timeline));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TimelineResponse> call, Throwable t) {
                Log.e("TimelineActivity", "Excecption " + t.toString());
                progressBar.setVisibility(View.INVISIBLE);
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_load_timeline));

            }
        });
    }

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
        Log.e(TAG, "Calling Weather Api  " + from + "  Latitude " + latitude + ", Longitude " + longitude);
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION));
        ApiInterface weatherService = retrofit.create(ApiInterface.class);
        String userId=SharedPreferencesMethod.getString(context,SharedPreferencesMethod.USER_ID);
        String token=SharedPreferencesMethod.getString(context,SharedPreferencesMethod.TOKEN);
        String farmID=SharedPreferencesMethod.getString(context,SharedPreferencesMethod.SVFARMID);
        Call<WeatherMainRes> forecastCall = weatherService.getWeatherData(userId,token,String.valueOf(longitude).trim() , String.valueOf(latitude).trim() ,farmID);
        forecastCall.enqueue(new Callback<WeatherMainRes>() {
            @Override
            public void onResponse(Call<WeatherMainRes> call, Response<WeatherMainRes> response) {
                if (response.isSuccessful()) {
                    if (response.body()!=null&&response.body().getWeatherForecast()!=null) {
                        weatherForecast = response.body().getWeatherForecast();
                        if (weatherForecast != null) {
                            weatherForecast.setIsLoaded('Y');
                            weatherForecast.setCityName(cityName);
                            weatherForecast.setStateName(stateName);
                            weatherForecast.setCountryName(countryName);
                            weatherForecast.setType(AppConstants.TIMELINE.WR);
                            String icon = (String.valueOf(weatherForecast.getCurrently().getIcon()));
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            String todayDateStr = df.format(c);
                            TimelineInnerData data = new TimelineInnerData("0", "100",
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID), "New Visit", "0", todayDateStr, null, "0",
                                    "0", "0", "Y", "reply", "wr");
                            if (timelineAdapter != null)
                                timelineAdapter.addTimelineWeather(0, data, weatherForecast,true);
                            DecimalFormat format = new DecimalFormat("##.##");
                            double precipitation_val = Math.round(weatherForecast.getDaily().getData().get(0).getPrecipIntensity());

                            Log.e(TAG, "WeatherResp " + new Gson().toJson(weatherForecast.getCurrently()));
                        }
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
