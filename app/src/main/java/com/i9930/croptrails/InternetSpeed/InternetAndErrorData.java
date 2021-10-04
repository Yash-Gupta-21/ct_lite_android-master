package com.i9930.croptrails.InternetSpeed;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.i9930.croptrails.BuildConfig;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

public class InternetAndErrorData {
    @SerializedName("app_id")
    String productId;
    @SerializedName("user_id")
    String userId;
    @SerializedName("comp_id")
    String compId;
    @SerializedName("api_name")
    String apiName;
    @SerializedName("api_time_to_resp")
    String apiTimeToResponse;
    @SerializedName("ip_address")
    String ipAddress;
    @SerializedName("user_device_os")
    String os;
    @SerializedName("user_device_os_version")
    String osVersion;
    @SerializedName("user_device_browser")
    String browser;
    @SerializedName("app_version")
    String appVersion;
    @SerializedName("network_type")
    String networkType;
    @SerializedName("network_speed")
    String networkSPeed;
    @SerializedName("err_msg")
    String error;
    @SerializedName("status_code")
    int statusCode;
    Activity activity;
    @SerializedName("user_device")
    String device;

    public InternetAndErrorData() {
    }

    public InternetAndErrorData(Activity activity, String apiName,
                                String apiTimeToResponse, String networkSPeed, String error, int statusCode) {
        this.error = error;
        this.statusCode = statusCode;
//        this.productId = "CT";
        this.productId = "5";
        this.userId = SharedPreferencesMethod.getString(activity, SharedPreferencesMethod.USER_ID);
        this.compId = SharedPreferencesMethod.getString(activity, SharedPreferencesMethod.COMP_ID);
        ;
        this.apiName = apiName;
        this.apiTimeToResponse = apiTimeToResponse;
        this.ipAddress = ConnectivityUtils.getLocalIpAddress();
        this.os = "Android " + Build.VERSION.RELEASE;
        this.device = android.os.Build.MODEL + "(" + Build.MANUFACTURER + ")";
        this.osVersion = "" + Build.VERSION.SDK_INT;
        this.browser = "Unknown";
        this.appVersion = BuildConfig.VERSION_NAME;
        this.networkType = ConnectivityUtils.getNetworkName(activity);
        this.networkSPeed = networkSPeed;
    }


    public static void notifyApiDelay(Activity activity, String apiName, String apiTimeToResponse, String networkSPeed, String error, int statusCode) {
        try {

            String api=apiName;
            String[] apiArr=apiName.split(RetrofitClientInstance.SELECTED_BASE_URL);
            if (apiArr.length>1)
                api=apiArr[1];
            InternetAndErrorData data = new InternetAndErrorData(activity, api, apiTimeToResponse, networkSPeed, error, statusCode);
            String url = "https://log.shahsoftsol.com/log/insertLog";
            JSONObject parameters = null;
            try {
                parameters = new JSONObject(new Gson().toJson(data));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(InternetAndErrorData.class.getName(), "DATA " + parameters.toString());
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e(InternetAndErrorData.class.getName(), "RESPONSE DATA " + response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(InternetAndErrorData.class.getName(), "ERROR ", error);
                }
            });

            Volley.newRequestQueue(activity).add(jsonRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
