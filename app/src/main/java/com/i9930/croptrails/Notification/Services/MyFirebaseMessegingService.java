package com.i9930.croptrails.Notification.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;

import com.i9930.croptrails.FarmDetails.Model.WeatherForecast;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Notification.NotificationActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Setting.SettingActivity;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.FusedLocationService;
import com.i9930.croptrails.Weather.WeatherActivity;

//ODE973088622sGp
public class MyFirebaseMessegingService extends FirebaseMessagingService {
    String TAG = "FireService";
    private static int count = 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        new FusedLocationService(this);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(remoteMessage.getData().get("body"));
            Log.e(TAG, "JOBJ: " + count + " " + jsonObject);
            count++;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "JOBJ EXC: " + e.toString());
        }
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            if (jsonObject != null) {
                sendNotification(data, jsonObject);
            }
            if (true) {
                scheduleJob();
            } else {
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification());
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }

    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(UploadWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendNotification(Map<String, String> data, JSONObject dataObj) {
        Intent intent = null;
        String title = null, message = null;
        Bitmap bitmap = null;
        Bitmap largeBitmap = null;
        String compReg=null;

        try {
            Log.e(TAG, "DATA " + dataObj);
            if (dataObj.has(AppConstants.NOTIFICATION_KEY_IMAGE_LINK) && dataObj.getString(AppConstants.NOTIFICATION_KEY_IMAGE_LINK) != null &&
                    !dataObj.getString(AppConstants.NOTIFICATION_KEY_IMAGE_LINK).equals("")) {
                bitmap = getBitmapfromUrl(dataObj.getString(AppConstants.NOTIFICATION_KEY_IMAGE_LINK));
            } else {
                bitmap = null;
            }
            if (dataObj.has(AppConstants.NOTIFICATION_KEY_TITLE) && dataObj.getString(AppConstants.NOTIFICATION_KEY_TITLE) != null) {
                title = dataObj.getString(AppConstants.NOTIFICATION_KEY_TITLE);
            } else {
                title = "";
            }
            if (dataObj.has(AppConstants.NOTIFICATION_KEY_TITLE) && dataObj.getString(AppConstants.NOTIFICATION_KEY_TITLE) != null) {
                message = dataObj.getString(AppConstants.NOTIFICATION_KEY_MESSAGE);
            } else {
                message = dataObj.getString(AppConstants.NOTIFICATION_KEY_DESCRIPTION);
            }
            if (dataObj.has(AppConstants.NOTIFICATION_KEY_ACTIVITY) && dataObj.getString(AppConstants.NOTIFICATION_KEY_ACTIVITY) != null) {
                if (dataObj.get(AppConstants.NOTIFICATION_KEY_ACTIVITY).equals(AppConstants.NAVIGATE.COMP_REG)) {
                    compReg=AppConstants.NAVIGATE.COMP_REG;
                }
                else if (dataObj.get(AppConstants.NOTIFICATION_KEY_ACTIVITY).equals(AppConstants.NAVIGATE.WEATHER)) {
                    Log.e(TAG, " NAVIGATE TO " + "WEATHER");
                    intent = new Intent(this, LandingActivity.class);
                    if (dataObj.has("weather_data")) {
                        intent = new Intent(this, WeatherActivity.class);
                        WeatherForecast weatherForecast = new Gson().fromJson(data.get("weather_data"), WeatherForecast.class);
                        intent.putExtra("weatherForecast", weatherForecast);
                        String icon = (String.valueOf(weatherForecast.getCurrently().getIcon()));
                        largeBitmap=select_skycon(icon);
                        title="";
                        if (dataObj.has("farm_name"))
                            title=dataObj.getString("farm_name");
                        if (dataObj.has("lot_no"))
                        {
                            if (title.isEmpty())
                                title=dataObj.getString("lot_no");
                            else
                                title=title+"("+dataObj.getString("lot_no")+")";
                        }

                        message=String.valueOf(Math.round(weatherForecast.getCurrently().getTemperature()) + "\u00B0" + " ");
                    }

                } else if (dataObj.get(AppConstants.NOTIFICATION_KEY_ACTIVITY).equals(AppConstants.NAVIGATE.TIMELINE)) {
                    Log.e(TAG, " NAVIGATE TO  " + "TIMELINE");
                    intent = new Intent(this, TestActivity.class);
                } else if (dataObj.get(AppConstants.NOTIFICATION_KEY_ACTIVITY).equals(AppConstants.NAVIGATE.UPDATE_PROFILE)) {
                    Log.e(TAG, " NAVIGATE TO  " + "UPDATE_PROFILE");
                    intent = new Intent(this, SettingActivity.class);
                }
            } else {
                Log.e(TAG, " NAVIGATE TO  " + "LANDING");
                intent = new Intent(this, LandingActivity.class);
            }
            if (compReg!=null&&compReg.equals(AppConstants.NAVIGATE.COMP_REG))
            {
                storeCompReg();
                return;
            }

//            intent = new Intent(this, NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            if (dataObj.has(AppConstants.NOTIFICATION_KEY_SH_ID))
                intent.putExtra("id", dataObj.getString(AppConstants.NOTIFICATION_KEY_SH_ID));
            else
                intent.putExtra("id", "0");

            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            PendingIntent pendingIntent = PendingIntent.getActivity(this, m /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (bitmap != null) {
                createImageNotification(title, message, pendingIntent, bitmap, largeBitmap);
            } else {
                createNotification(title, message, pendingIntent);
            }

        } catch (Exception e) {
            Log.e(TAG, "DATA Exception " + e.toString());
            intent = new Intent(this, LandingActivity.class);
        }

    }
    private void storeCompReg(){

    }

    private void createImageNotification(String title, String messageBody, PendingIntent pendingIntent, Bitmap bitmap, Bitmap largeBitmap) {
        Log.e(TAG, "In Create Notification");
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap);
        String channelId = getString(R.string.default_notification_channel_id);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ct_water_mark)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        if (largeBitmap != null)
            notificationBuilder.setLargeIcon(largeBitmap);
        notificationBuilder.setStyle(style);
        // .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //NotificationChannel channel = createNotificationChannel();
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(new Random().nextInt(10000), notificationBuilder.build());
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(m, notificationBuilder.build());
    }

    private void createNotification(String title, String messageBody, PendingIntent pendingIntent) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle()
                .bigText(messageBody);

        String channelId = getString(R.string.default_notification_channel_id);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ct_water_mark)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        Log.e(TAG, messageBody.length() + "");
        if (messageBody.length() > 60) {
            notificationBuilder.setStyle(style);
        }
        // .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(m, notificationBuilder.build());
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

/*
    private NotificationChannel createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
             channel = new NotificationChannel(getString(R.string.notification_channel_id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
          */
/*  NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);*//*

        }
        return channel;
    }
*/


    public Bitmap select_skycon(String icon) {
        switch (icon) {
            case "clear-day": {

                return BitmapFactory.decodeResource(getResources(), R.drawable.ic_day);
            }
            case "clear-night": {
                return BitmapFactory.decodeResource(getResources(), R.drawable.ic_night);
            }
            case "rain": {
                return BitmapFactory.decodeResource(getResources(), R.drawable.ic_rainy);
            }
            case "snow": {
                return BitmapFactory.decodeResource(getResources(), R.drawable.ic_snow);
            }
            case "sleet": {
                return BitmapFactory.decodeResource(getResources(), R.drawable.ic_sleet);
            }
            case "wind": {
                return BitmapFactory.decodeResource(getResources(), R.drawable.ic_windy);
            }
            case "fog": {
                return BitmapFactory.decodeResource(getResources(), R.drawable.ic_fog);
            }
            case "cloudy": {
                return BitmapFactory.decodeResource(getResources(), R.drawable.ic_cloudy);
            }
            case "partly-cloudy-day": {

                return BitmapFactory.decodeResource(getResources(), R.drawable.ic_cloudy_day_2);
            }
            case "partly-cloudy-night": {

                return BitmapFactory.decodeResource(getResources(), R.drawable.ic_cloudy_night_2);
            }
            case "hail": {

                return BitmapFactory.decodeResource(getResources(), R.drawable.ic_hail);
            }
            case "thunderstorm": {

                return BitmapFactory.decodeResource(getResources(), R.drawable.ic_thunder);
            }
            default: {
                return null;
            }
        }

    }
}