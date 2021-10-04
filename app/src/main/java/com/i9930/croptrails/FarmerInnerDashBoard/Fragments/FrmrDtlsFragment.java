package com.i9930.croptrails.FarmerInnerDashBoard.Fragments;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.FarmDetailsUpdateActivity;
import com.i9930.croptrails.FarmDetails.Model.FullDetailsResponse;
import com.i9930.croptrails.FarmNavigation.FarmNavigationActivity;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Maps.ShowArea.Model.GetGpsCoordinates;
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;
import com.i9930.croptrails.Maps.ShowArea.Model.geojson.GeoJsonResponse;
import com.i9930.croptrails.Maps.ShowArea.ShowAreaOnMapActivity;
import com.i9930.croptrails.Maps.ShowArea.ShowAreaWithUpdateActivity;
import com.i9930.croptrails.Maps.VerifyArea.MapsActivity;
import com.i9930.croptrails.R;

import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class FrmrDtlsFragment extends Fragment {
    String TAG = "FrmrDtlsFragment";
    Call<FullDetailsResponse> farmAndHarvestCall;
    Context context;
    Resources resources;
    String[] latAraay;
    String lngArray[];
    private boolean isActivityRunning = false;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView farm_details_mob_number, farm_details_expected_area_tv, farm_details_actual_area_tv, farm_details_standing_area_tv,
            farm_details_irrigationSource, farm_details_irrigationType, farm_details_previousCrop, farm_details_soilType, farm_details_sowingDate,
            farm_details_expFloweringDate, add_details_expHarvestDate, farm_details_actual_flowering_date, farm_details_actual_harvest_date,
            view_farm_area_on_map, update_farm_details, farm_details_farm_name, farm_details_farm_id;

    private void initViews(View view) {


        farm_details_mob_number = view.findViewById(R.id.farm_details_mob_number);
        farm_details_expected_area_tv = view.findViewById(R.id.farm_details_expected_area_tv);
        farm_details_actual_area_tv = view.findViewById(R.id.farm_details_actual_area_tv);
        farm_details_standing_area_tv = view.findViewById(R.id.farm_details_standing_area_tv);
        farm_details_irrigationSource = view.findViewById(R.id.farm_details_irrigationSource);
        farm_details_irrigationType = view.findViewById(R.id.farm_details_irrigationType);
        farm_details_previousCrop = view.findViewById(R.id.farm_details_previousCrop);
        farm_details_soilType = view.findViewById(R.id.farm_details_soilType);
        farm_details_sowingDate = view.findViewById(R.id.farm_details_sowingDate);
        farm_details_expFloweringDate = view.findViewById(R.id.farm_details_expFloweringDate);
        add_details_expHarvestDate = view.findViewById(R.id.add_details_expHarvestDate);
        farm_details_actual_flowering_date = view.findViewById(R.id.farm_details_actual_flowering_date);
        farm_details_actual_harvest_date = view.findViewById(R.id.farm_details_actual_harvest_date);
        view_farm_area_on_map = view.findViewById(R.id.view_farm_area_on_map);
        update_farm_details = view.findViewById(R.id.update_farm_details);

        farm_details_farm_name = view.findViewById(R.id.farm_details_farm_name);
        farm_details_farm_id = view.findViewById(R.id.farm_details_farm_id);
    }


    public FrmrDtlsFragment() {
    }


    public static FrmrDtlsFragment newInstance(String param1, String param2) {
        FrmrDtlsFragment fragment = new FrmrDtlsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frmr_dtls, container, false);
        initViews(view);
        isActivityRunning = true;
        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();

        onClicks();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onStart() {
        super.onStart();
        fetchAllDataOnline();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void fetchAllDataOnline() {
        String farm_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        Log.e(TAG, "farmId " + farm_id + " And CompId " + comp_id);
        Log.e(TAG, "CompId " + comp_id);
        //SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID,"16");
        //SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVCOMPID,"1");
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        farmAndHarvestCall = apiInterface.getFarmAndHarvestData(farm_id, comp_id, userId, token);
        farmAndHarvestCall.enqueue(new Callback<FullDetailsResponse>() {
            @Override
            public void onResponse(Call<FullDetailsResponse> call, Response<FullDetailsResponse> response) {
                isLoaded[0] =true;
                String error="";
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "Response " + new Gson().toJson(response.body()));
                        FullDetailsResponse detailsResponse = response.body();
                        if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                        } else if (detailsResponse.getFarmData() != null) {
                            setFarmDataOnline(detailsResponse.getFarmData());
                            germinationDataSet(detailsResponse.getFarmData());
                            //Timeline Adapter work

                        } else {

                        }
                        if (detailsResponse.getGrade() != null /*&& detailsResponse.getGrade().size() > 0*/) {
                            //setLineChartInfo(detailsResponse.getGrade());
                            Log.e(TAG, "linechart if");
                        } else {
                            //lineChart.setVisibility(View.GONE);
                            /*farm_detail_layout.setVisibility(View.VISIBLE);
                            farm_details_tv.setClickable(false);*/
                            Log.e(TAG, "linechart else");
                        }


                    } else {
                        Log.e(TAG, "Null Response");
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
                        Log.e(TAG, "Unsuccess " + response.errorBody().string().toString());
                        error=response.errorBody().string().toString();
                        if (isActivityRunning) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(context, resources.getString(R.string.fail_to_load_farm_data));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis=AppConstants.getCurrentMills();
                long diff=newMillis-currMillis;
                if(diff>=DELAY_API||(diff<DELAY_API&&error!=null&&!TextUtils.isEmpty(error))||response.code()==500){
                    notifyApiDelay(getActivity(),response.raw().request().url().toString(),
                            ""+diff,internetSPeed,error,response.code());
                }
            }

            @Override
            public void onFailure(Call<FullDetailsResponse> call, Throwable t) {
                isLoaded[0] =true;
                long newMillis=AppConstants.getCurrentMills();
                long diff=newMillis-currMillis;
                notifyApiDelay(getActivity(),call.request().url().toString(),
                        ""+diff,internetSPeed,t.toString(),0);
                if (t instanceof SocketTimeoutException) {
                    Log.e(TAG, t.toString());
                    //Try again
                    fetchAllDataOnline();
                } else {
                    //something went wrong
                    Log.e(TAG, t.toString());
                    if (isActivityRunning) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.fail_to_load_farm_data));
                    }
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

    private void internetFlow(boolean isLoaded){
        try {

            if (!isLoaded){
                if (!ConnectivityUtils.isConnected(context)){
                    AppConstants.failToast(context,getResources().getString(R.string.check_internet_connection_msg));
                }else {
                    ConnectivityUtils.checkSpeedWithColor(getActivity(), new ConnectivityUtils.ColorListener() {
                        @Override
                        public void onColorChanged(int color,String speed) {
                            internetSPeed=speed;
                        }
                    },20);
                }
            }



        }catch (Exception e){

        }

    }

    String internetSPeed="";

    private void setFarmDataOnline(final FetchFarmResult farmData) {
        DataHandler.newInstance().setFetchFarmResult(farmData);
        Log.e("FarmDetailsAct", "Setting data");

        String soil_type = "-";
        //String avg_germination = "0";
        String irrigationSource = "-";
        String previousCrop = "-";
        String irrigationtype = "-";
        String sowingDate = "-";
        String expFloweringDate = "-";
        String expHarvestDate = "-";
        String actual_area = "0";
        String standing_area = "0";
        String expected_area = "0";

        /*if (farmData.getPldAcres() != null && Double.parseDouble(farmData.getPldAcres()) > 0) {
            fabChildAddPld.setLabelText("View Pld");
            fabChildAddPld.setImageResource(android.R.drawable.ic_menu_view);
        } else {
            fabChildAddPld.setLabelText("Add Pld");
            fabChildAddPld.setImageResource(android.R.drawable.ic_menu_add);
        }*/

        if (farmData.getLotNo() != null) {

            farm_details_farm_id.setText(farmData.getLotNo());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOT_NO, farmData.getLotNo());
        }
        if (farmData.getFarmName() != null) {
            farm_details_farm_name.setText(farmData.getFarmName());

        }


        if (farmData.getActualFloweringDate() != null) {
            if (!farmData.getActualFloweringDate().equals(AppConstants.for_date)) {
                farm_details_actual_flowering_date.setText(AppConstants.getShowableDate(farmData.getActualFloweringDate(),context));
            }
        }
        if (farmData.getActualHarvestDate() != null) {
            if (!farmData.getActualHarvestDate().equals(AppConstants.for_date)) {
                farm_details_actual_harvest_date.setText(AppConstants.getShowableDate(farmData.getActualHarvestDate(),context));
            }
        }
        if (farmData.getName() != null) {
            String firstLetter = farmData.getName().substring(0, 1).toUpperCase();
            String cap = farmData.getName().substring(1);
            //title_address.setText(firstLetter + cap);
        }


        if (farmData != null) {
            if (farmData.getSoilType() != null) {
                soil_type = farmData.getSoilType().toString();
            }
            if (farmData.getActualArea() != null && farmData.getActualArea() != "0") {
                actual_area = farmData.getActualArea().toString();
            }
            /*if (farmData.getGermination() != null && farmData.getGermination() != "0") {
                avg_germination = farmData.getGermination().toString();
            }*/
            if (farmData.getIrrigationSource() != null) {
                irrigationSource = farmData.getIrrigationSource().toString();
            }

            if (farmData.getPreviousCrop() != null && !TextUtils.isEmpty(farmData.getPreviousCrop()) && !farmData.getPreviousCrop().equals("0")) {
                previousCrop = farmData.getPreviousCrop().toString();
            }
            if (farmData.getIrrigationType() != null) {
                irrigationtype = farmData.getIrrigationType().toString();
            }
            if (farmData.getSowingDate() != null && !farmData.getSowingDate().equals(AppConstants.for_date)) {
                sowingDate = AppConstants.getShowableDate(farmData.getSowingDate(),context);
            }
            if (farmData.getExpFloweringDate() != null && !farmData.getExpFloweringDate().equals(AppConstants.for_date)) {
                expFloweringDate = AppConstants.getShowableDate(farmData.getExpFloweringDate(),context);
            }
            if (farmData.getExpHarvestDate() != null && !farmData.getExpHarvestDate().equals(AppConstants.for_date)) {
                expHarvestDate = AppConstants.getShowableDate(farmData.getExpHarvestDate(),context);
            }
            if (farmData.getStandingAcres() != null) {
                standing_area = farmData.getStandingAcres();
            }
            if (farmData.getExpArea() != null) {
                expected_area = farmData.getExpArea();
            }
        }

        farm_details_actual_area_tv.setText(convertAreaTo(actual_area));
        farm_details_standing_area_tv.setText(convertAreaTo(standing_area));
        farm_details_expected_area_tv.setText(convertAreaTo(expected_area));
        if (actual_area.trim().equals("0")) {
            view_farm_area_on_map.setText(resources.getString(R.string.capture_farm_area_label));


        } else {
            view_farm_area_on_map.setText(resources.getString(R.string.farm_details_view_area));
            farm_details_irrigationSource.setText(irrigationSource);
            farm_details_previousCrop.setText(previousCrop);
            farm_details_irrigationType.setText(irrigationtype);
            farm_details_soilType.setText(soil_type);
            farm_details_sowingDate.setText(sowingDate);
            farm_details_expFloweringDate.setText(expFloweringDate);
            add_details_expHarvestDate.setText(expHarvestDate);


        }


    }

    private void onClicks() {
        view_farm_area_on_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                    GetGeoJsonData fetchGpsCordinates = new GetGeoJsonData(true,AppConstants.MAP_NAV_MODE.SHOW_AREA);
                    fetchGpsCordinates.execute();
                } else {
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.cant_view_farm_in_offline_msg));
                    //Toast.makeText(context, "Yo can't view farm in offline mode. ", Toast.LENGTH_LONG).show();
                }
            }
        });
        update_farm_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FarmDetailsUpdateActivity.class);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    context.startActivity(intent, options.toBundle());
                    //finish();
                } else {
                    startActivity(intent);
                    //finish();
                }
            }
        });
    }

    private class GetGeoJsonData extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;
        boolean isShowEasyMode = false;
        int mode=1;

        public GetGeoJsonData(boolean isShowEasyMode, int mode) {
            super();
            dialog = new ProgressDialog(context);
            DataHandler.newInstance().setLatLngList(null);
            this.isShowEasyMode = isShowEasyMode;
            this.mode=mode;
            dialog.setMessage(getResources().getString(R.string.please_wait_msg));
            dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {

            //demosupervisor1
            try {
                String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("user_id",""+SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                jsonObject.addProperty("token",""+SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                jsonObject.addProperty("comp_id",""+SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                jsonObject.addProperty("farm_id",""+SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
//                jsonObject.addProperty("farm_id","20202");
                Call<GeoJsonResponse> statusMsgModelCall = apiService.getFarmGeoJson(jsonObject);
                Log.e(TAG, "Sending Data " + new Gson().toJson(jsonObject));
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<GeoJsonResponse>() {
                    @Override
                    public void onResponse(Call<GeoJsonResponse> call, retrofit2.Response<GeoJsonResponse> response) {
                        String error = null;
                        isLoaded[0] = true;
                        if (response.isSuccessful()) {
                            Log.e("Area Response", new Gson().toJson(response.body()));
                            GeoJsonResponse statusMsgModel = response.body();
                            if (statusMsgModel.getStatus() == 1) {
                                if (statusMsgModel.getData()!=null&&statusMsgModel.getData().size()>0){
                                    dialog.cancel();
                                    Intent intent = new Intent(context, ShowAreaWithUpdateActivity.class);
                                    if (mode==AppConstants.MAP_NAV_MODE.SHOW_NAVIGATION){
                                        intent = new Intent(context, FarmNavigationActivity.class);
                                    }
                                    startActivity(intent);
                                }else {
                                    if (mode==AppConstants.MAP_NAV_MODE.SHOW_NAVIGATION){
                                        Toasty.error(context, "Farm location not available", Toast.LENGTH_LONG, false).show();
                                    }/*else
                                        showDialog(true);*/
                                }
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                dialog.cancel();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                dialog.cancel();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                            } else {
                                dialog.cancel();
                                if (mode==AppConstants.MAP_NAV_MODE.SHOW_NAVIGATION){
                                    Toasty.error(context, "Farm location not available", Toast.LENGTH_LONG, false).show();
                                }/*else
                                    showDialog(true);*/
                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            dialog.cancel();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            dialog.cancel();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                        } else {
                            try {
                                error = response.errorBody().string().toString();
                                Toasty.error(context, "Farm location not available", Toast.LENGTH_LONG, false).show();
                                Log.e(TAG, "Error " + error);
                                dialog.cancel();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                            notifyApiDelay(getActivity(), response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<GeoJsonResponse> call, Throwable t) {
                        Log.e(TAG, "Failure " + t.toString());
                        isLoaded[0] = true;
                        dialog.cancel();
                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        notifyApiDelay(getActivity(), call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);
                        Toasty.error(context, "Farm location not available", Toast.LENGTH_LONG, false).show();

                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }

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
    
    /*private class FetchGpsCordinates extends AsyncTask<String, Void, String> {
        public FetchGpsCordinates() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
            Call<GetGpsCoordinates> getGpsCoordinatesCall = apiService.getGpsCoordinates(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID), userId, token);
            getGpsCoordinatesCall.enqueue(new Callback<GetGpsCoordinates>() {

                @Override
                public void onResponse(Call<GetGpsCoordinates> call, Response<GetGpsCoordinates> response) {
                    if (response.isSuccessful()) {
                        GetGpsCoordinates getGpsCoordinates = response.body();
                        Log.e(TAG, "Coordinates Res " + new Gson().toJson(getGpsCoordinates));
                        if (getGpsCoordinates.getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(getActivity(), response.body().getMsg());
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(getActivity(), resources.getString(R.string.authorization_expired));
                        } else if (getGpsCoordinates.getStatus() != 0) {
                            List<LatLngFarm> latLngList = getGpsCoordinates.getData();


                            latAraay = new String[latLngList.size()];
                            lngArray = new String[latLngList.size()];
                            int sizeofgps = latLngList.size();
                            for (int i = 0; i < latLngList.size(); i++) {
                                latAraay[i] = latLngList.get(i).getLat();
                                lngArray[i] = latLngList.get(i).getLng();
                            }
                            com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(Double.valueOf(latAraay[0]), Double.valueOf(lngArray[0]));
                            DataHandler.newInstance().setLatLngList(latLngList);
                            Intent intent = new Intent(context, ShowAreaOnMapActivity.class);
                            ActivityOptions options = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                startActivity(intent, options.toBundle());
                            } else {
                                startActivity(intent);
                            }
                        } else {
                            if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR)||
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.ADMIN)||
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUB_ADMIN)||
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPER_ADMIN)) {
                                //  Toast.makeText(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG).show();
                                Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                                Intent intent = new Intent(context, MapsActivity.class);
                                ActivityOptions options = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                                }
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    startActivity(intent, options.toBundle());
                                } else {
                                    startActivity(intent);
                                }
                            } else {
                                Toasty.error(context, resources.getString(R.string.farmer_not_allowed_to_add_area_msg), Toast.LENGTH_LONG).show();
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
                    }
                }
                @Override
                public void onFailure(Call<GetGpsCoordinates> call, Throwable t) {
                    //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, t.toString());
                    if (isActivityRunning) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_get_farm_location_msg));
                    }
                }

            });


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

    }*/

    private void germinationDataSet(FetchFarmResult fetchFarmResult) {

        Log.e(TAG, "Setting Germination Data from farm details");

        if (fetchFarmResult.getGermination() != null && !fetchFarmResult.getGermination().equals("0")) {
            Log.e(TAG, "In if Setting Germination Data from farm details");

            /*fabChildAddGermination.setLabelText("View Germination");
            fabChildAddGermination.setImageResource(android.R.drawable.ic_menu_view);
            // germination_button.setText(resources.getString(R.string.view_germination_label));
            //germination_button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_eye_white_24dp, 0, 0, 0);
            fabChildAddGermination.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fabParent.isOpened()) {
                        fabParent.close(true);
                    }
                    Intent intent = new Intent(context, ShowSampleGermiActivity.class);
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
            });*/
        } else {
            Log.e(TAG, "In Else Setting Germination Data from farm details");
            //germination_button.setVisibility(View.VISIBLE);
            String addGermiLabel = resources.getString(R.string.germination_label);
            /*fabChildAddGermination.setLabelText("Add Germination");
            fabChildAddGermination.setImageResource(android.R.drawable.ic_menu_add);
            fabChildAddGermination.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fabParent.isOpened()) {
                        fabParent.close(true);
                    }
                    Intent intent = new Intent(context, GerminationSpacingActivity.class);
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
            });*/
        }
    }

    private String convertAreaTo(String area) {
        return AppConstants.getShowableArea(area);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isActivityRunning = false;

    }
}
