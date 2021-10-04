package com.i9930.croptrails.ApiFailDialog;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.i9930.croptrails.AddFarm.Share;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Login.LoginActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.AddFarmCache.AddFarmCCacheManager;
import com.i9930.croptrails.RoomDatabase.AppDatabase;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegCacheManager;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownCacheManager;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmCacheManager;
import com.i9930.croptrails.RoomDatabase.Gps.GetSetInterface;
import com.i9930.croptrails.RoomDatabase.Gps.GpsCacheManager;
import com.i9930.croptrails.RoomDatabase.Gps.GpsLog;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestCacheManager;
import com.i9930.croptrails.RoomDatabase.Timeline.TimelineCacheManager;
import com.i9930.croptrails.RoomDatabase.Visit.VrCacheManager;
import com.i9930.croptrails.RoomDatabase.VisitStructure.VisitCacheManager;
import com.i9930.croptrails.ServiceAndBroadcasts.Model.RecieveResponseGpsBack;
import com.i9930.croptrails.ServiceAndBroadcasts.Model.SendGpsArray;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.LocationInsertReciever;
import com.i9930.croptrails.Utility.LocationUploadReceiver;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static android.content.Context.ALARM_SERVICE;

public class ViewFailDialog implements GetSetInterface {
    Dialog dialog;
    Resources resources;
    AlertDialog alertDialog;
    String TAG = "ViewFailDialog";
    Context context;

    public void showDialog(final Context context, String msg) {
        this.context = context;
        try {
            final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
            Context contextlang = LocaleHelper.setLocale(context, languageCode);
            resources = contextlang.getResources();
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_api_response_fail);
            Window window1 = dialog.getWindow();
            window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            TextView messageHeadTv = dialog.findViewById(R.id.api_response_fail_msg_head_tv);
            TextView messageTv = dialog.findViewById(R.id.api_response_fail_msg_tv);
            Button okButton = dialog.findViewById(R.id.okButton);
            messageHeadTv.setText(resources.getString(R.string.opps_message));
            messageTv.setText(msg);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {

        }
    }


    public void showDialog(final Context context, String heading, String msg) {
        this.context = context;
        try {
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_api_response_fail);
            Window window1 = dialog.getWindow();
            window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            TextView messageHeadTv = dialog.findViewById(R.id.api_response_fail_msg_head_tv);
            TextView messageTv = dialog.findViewById(R.id.api_response_fail_msg_tv);
            Button okButton = dialog.findViewById(R.id.okButton);
            messageHeadTv.setText(heading);
            messageTv.setText(msg);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {

        }
    }


    public void showDialogForFinish(final Activity context, String msg) {
        try {
            this.context = context;
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_api_response_fail);
            dialog.setCancelable(false);
            Window window1 = dialog.getWindow();
            window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            TextView messageHeadTv = dialog.findViewById(R.id.api_response_fail_msg_head_tv);
            TextView messageTv = dialog.findViewById(R.id.api_response_fail_msg_tv);
            Button okButton = dialog.findViewById(R.id.okButton);
            messageTv.setText(msg);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    context.finish();
                }
            });

            dialog.show();
        } catch (Exception e) {

        }
    }

    public void showDialogForFinish(final Activity context, String title, String msg) {
        try {
            this.context = context;
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_api_response_fail);
            dialog.setCancelable(false);
            Window window1 = dialog.getWindow();
            window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            TextView messageHeadTv = dialog.findViewById(R.id.api_response_fail_msg_head_tv);
            TextView messageTv = dialog.findViewById(R.id.api_response_fail_msg_tv);
            Button okButton = dialog.findViewById(R.id.okButton);
            messageHeadTv.setText(title);
            messageTv.setText(msg);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    context.finish();
                }
            });

            dialog.show();
        } catch (Exception e) {

        }
    }

    public void showDialogAndGoHomePage(final Activity context, String title, String msg) {
        try {
            this.context = context;
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_api_response_fail);
            dialog.setCancelable(false);
            Window window1 = dialog.getWindow();
            window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            TextView messageHeadTv = dialog.findViewById(R.id.api_response_fail_msg_head_tv);
            TextView messageTv = dialog.findViewById(R.id.api_response_fail_msg_tv);
            Button okButton = dialog.findViewById(R.id.okButton);
            messageHeadTv.setText(title);
            messageTv.setText(msg);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    context.startActivity(new Intent(context, LandingActivity.class));
                    context.finishAffinity();
                    context.finish();
                }
            });

            dialog.show();
        } catch (Exception e) {

        }
    }

    public void showSessionExpireDialog(final Activity context, String msg) {
        try {
            boolean mode = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE);
            if (!mode) {
                GpsCacheManager.getInstance(context).getContacts(this);
                this.context = context;
                final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
                Context contextlang = LocaleHelper.setLocale(context, languageCode);
                resources = contextlang.getResources();
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                try {

                    FirebaseMessaging.getInstance().unsubscribeFromTopic("croptrails");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("comp_" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
//            FirebaseMessaging.getInstance().unsubscribeFromTopic("cluster_" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));

                    alertDialog = builder.setMessage(msg)
                            .setTitle(resources.getString(R.string.alert_title_label))
                            .setCancelable(false)
                            .setPositiveButton(resources.getString(R.string.login_again_label), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    try {
                                        AppDatabase.eraseDB(context, null);
                                    } catch (Exception e) {
                                    }

                                    Intent intent = new Intent(context, LoginActivity.class);
                                    ActivityOptions options = null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        context.finishAffinity();
                                        context.startActivity(intent, options.toBundle());
                                        context.finish();
                                    } else {
                                        context.finishAffinity();
                                        context.startActivity(intent);
                                        context.finish();
                                    }
                                    Intent intent2 = new Intent(context, LocationInsertReciever.class);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AppConstants.LOCATION_ALARM.LOCATION_REQ_CODE_FETCH, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
                                    AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                                    am.cancel(pendingIntent);

                                    Intent intent3 = new Intent(context, LocationUploadReceiver.class);
                                    PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context, AppConstants.LOCATION_ALARM.LOCATION_REQ_CODE_UPLOAD, intent3, PendingIntent.FLAG_CANCEL_CURRENT);
                                    AlarmManager am3 = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                                    am3.cancel(pendingIntent3);
//                            AddFarmCCacheManager.getInstance(context).deleteAllData();
//                            GpsCacheManager.getInstance(context).deleteAllGps(null);
//                            SharedPreferencesMethod.clear(context);
//                            CompRegCacheManager.getInstance(context).deleteAllData();
//                            FarmCacheManager.getInstance(context).deleteAllFarms();
//                            TimelineCacheManager.getInstance(context).deleteAllTimeline();
//                            HarvestCacheManager.getInstance(context).deleteAllHarvests();
//                            VrCacheManager.getInstance(context).deleteAllVisits();
//                            VisitCacheManager.getInstance(context).deleteAllVisitStructure();
//                            DropDownCacheManager.getInstance(context).deleteAllChemicalUnit();

                                }
                            })
                            .setNegativeButton(resources.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {

        }
    }

    public void showSessionExpireDialog(final Activity context) {
        try {
            boolean mode = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE);
            if (!mode) {
                GpsCacheManager.getInstance(context).getContacts(this);
                this.context = context;
                final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
                Context contextlang = LocaleHelper.setLocale(context, languageCode);
                resources = contextlang.getResources();
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                try {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("croptrails");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("comp_" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
//            FirebaseMessaging.getInstance().unsubscribeFromTopic("cluster_" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));

                    alertDialog = builder.setMessage(resources.getString(R.string.multiple_login_msg))
                            .setTitle(resources.getString(R.string.alert_title_label))
                            .setCancelable(false)
                            .setPositiveButton(resources.getString(R.string.login_again_label), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    try {
                                        AppDatabase.eraseDB(context, null);
                                    } catch (Exception e) {
                                    }

                                    Intent intent = new Intent(context, LoginActivity.class);
                                    ActivityOptions options =
                                            null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        context.finishAffinity();
                                        context.startActivity(intent, options.toBundle());
                                        context.finish();
                                    } else {
                                        context.finishAffinity();
                                        context.startActivity(intent);
                                        context.finish();
                                    }
                                    Intent intent2 = new Intent(context, LocationInsertReciever.class);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AppConstants.LOCATION_ALARM.LOCATION_REQ_CODE_FETCH, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
                                    AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                                    am.cancel(pendingIntent);

                                    Intent intent3 = new Intent(context, LocationUploadReceiver.class);
                                    PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context, AppConstants.LOCATION_ALARM.LOCATION_REQ_CODE_UPLOAD, intent3, PendingIntent.FLAG_CANCEL_CURRENT);
                                    AlarmManager am3 = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                                    am3.cancel(pendingIntent3);
//                            AddFarmCCacheManager.getInstance(context).deleteAllData();
//                            GpsCacheManager.getInstance(context).deleteAllGps(null);
//                            SharedPreferencesMethod.clear(context);
//                            CompRegCacheManager.getInstance(context).deleteAllData();
//                            FarmCacheManager.getInstance(context).deleteAllFarms();
//                            TimelineCacheManager.getInstance(context).deleteAllTimeline();
//                            HarvestCacheManager.getInstance(context).deleteAllHarvests();
//                            VrCacheManager.getInstance(context).deleteAllVisits();
//                            VisitCacheManager.getInstance(context).deleteAllVisitStructure();
//                            DropDownCacheManager.getInstance(context).deleteAllChemicalUnit();


                                }
                            })
                            .setNegativeButton(resources.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {

        }
    }

    public void cancelDialog() {
        if (dialog != null) {
            dialog.cancel();
        }
        if (alertDialog != null)
            alertDialog.cancel();
    }

    @Override
    public void onContactsLoaded(List<GpsLog> gpsLogList) {

        String[] lati_cord, longi_cord, enter_date, sv_id, time;
        gpsLogList = new ArrayList<>();

        if (gpsLogList != null && gpsLogList.size() > 0) {
            Log.e(TAG, "Uploading Sup Loc");
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
            sendGpsArray.setLat_cord(lati_cord);
            sendGpsArray.setLong_cord(longi_cord);
            sendGpsArray.setEnter_date(enter_date);
            sendGpsArray.setSv_id(sv_id);
            sendGpsArray.setTime(time);
            sendGpsArray.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            sendGpsArray.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            Call<RecieveResponseGpsBack> statusMsgModelCall = apiService.getStatusMsgforUploadGpsInBack(sendGpsArray);

            statusMsgModelCall.enqueue(new Callback<RecieveResponseGpsBack>() {
                @Override
                public void onResponse(Call<RecieveResponseGpsBack> call, Response<RecieveResponseGpsBack> response) {
                    if (response.isSuccessful()) {
                        if (response != null) {
                            Log.e(TAG, "Locatio Res " + new Gson().toJson(response.body()));
                            RecieveResponseGpsBack statusMsgModel = response.body();
                            if (statusMsgModel.getStatus() != 0 && statusMsgModel.getStatus() != 10) {
                                GpsCacheManager.getInstance(context).deleteAllGps(null);
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

    @Override
    public void onContactsAdded() {

    }

    @Override
    public void onDataNotAvailable() {

    }
}
