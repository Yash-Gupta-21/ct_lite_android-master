package com.i9930.croptrails.ServiceAndBroadcasts;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.RoomDatabase.Gps.GetSetInterface;
import com.i9930.croptrails.RoomDatabase.Gps.GpsCacheManager;
import com.i9930.croptrails.RoomDatabase.Gps.GpsLog;
import com.i9930.croptrails.ServiceAndBroadcasts.Model.RecieveResponseGpsBack;
import com.i9930.croptrails.ServiceAndBroadcasts.Model.SaveGpsGetterSetter;
import com.i9930.croptrails.ServiceAndBroadcasts.Model.SendGpsArray;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;


/**
 * Created by hp on 13-04-2018.
 */

public class UploadGpsLogToServer extends BroadcastReceiver implements GetSetInterface {

    String TAG = "UploadGpsLogToServer";
    GetSetInterface getSetInterface;

    String[] lati_cord, longi_cord, enter_date, sv_id, time;
    Context context;

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        getSetInterface = this;
        Log.e("MYTAG", "uploading cords");
        GpsCacheManager.getInstance(context).getContacts(this); }


    @Override
    public void onContactsLoaded(List<GpsLog> gpsLogList) {
        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.LOGIN)) {
            if (gpsLogList != null && gpsLogList.size() > 0) {
                lati_cord = new String[gpsLogList.size()];
                longi_cord = new String[gpsLogList.size()];
                enter_date = new String[gpsLogList.size()];
                sv_id = new String[gpsLogList.size()];
                time = new String[gpsLogList.size()];
                for (int i = 0; i < gpsLogList.size(); i++) {
                    //JSONObject contact = new JSONObject();
                    lati_cord[i] = gpsLogList.get(i).getLat_cord().trim();
                    longi_cord[i] = gpsLogList.get(i).getLong_cord().trim();
                    sv_id[i] = gpsLogList.get(i).getSv_id().trim();
                    enter_date[i] = gpsLogList.get(i).getEnter_date().trim();
                    time[i] = gpsLogList.get(i).getTime().trim();
                }

                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
                SendGpsArray sendGpsArray = new SendGpsArray();
                //sendGpsArray.setJsonObject(contactsObj);
                sendGpsArray.setLat_cord(lati_cord);
                sendGpsArray.setLong_cord(longi_cord);
                sendGpsArray.setEnter_date(enter_date);
                sendGpsArray.setSv_id(sv_id);
                sendGpsArray.setTime(time);
                sendGpsArray.setToken(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.TOKEN));
                sendGpsArray.setUserId(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.USER_ID));
                Call<RecieveResponseGpsBack> statusMsgModelCall = apiService.getStatusMsgforUploadGpsInBack(sendGpsArray);

                statusMsgModelCall.enqueue(new Callback<RecieveResponseGpsBack>() {
                    @Override
                    public void onResponse(Call<RecieveResponseGpsBack> call, Response<RecieveResponseGpsBack> response) {
                        if (response.isSuccessful()) {
                            if (response != null) {

                                RecieveResponseGpsBack statusMsgModel = response.body();
                                if (statusMsgModel.getStatus() != 0&&statusMsgModel.getStatus()!=10) {
                                    Log.e(TAG, "Successfull" + statusMsgModel.getMsg());
                                    Log.e(TAG, "Successfull" + statusMsgModel.getResult() + "  entries");
                                    GpsCacheManager.getInstance(context).deleteAllGps(getSetInterface);
                                }
                            }
                        } else {
                            try {
                                Log.e(TAG, "Not Successfull" + response.errorBody().string().toString() + "   Status" + response.code() + "  Message" + response.message());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RecieveResponseGpsBack> call, Throwable t) {
                        Log.e(TAG, "On Failure" + t.toString());
                    }
                });

            } else {
                Log.e(TAG, "No Gps to upload");
            }
        }
    }

    @Override
    public void onContactsAdded() {

    }

    @Override
    public void onDataNotAvailable() {

    }

}
