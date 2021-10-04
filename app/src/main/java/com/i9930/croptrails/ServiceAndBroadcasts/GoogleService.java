package com.i9930.croptrails.ServiceAndBroadcasts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.Gps.GetSetInterface;
import com.i9930.croptrails.RoomDatabase.Gps.GpsCacheManager;
import com.i9930.croptrails.RoomDatabase.Gps.GpsLog;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;


public class GoogleService extends Service implements LocationListener, GetSetInterface {

    List<GpsLog> contacts;
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 30 * 60 * 1000;
    public static String str_receiver = "fcms.crptrls.i9930.croptrailsfcms.SuperVisorModule.ServicesAndBroadCastRecivers";
    Intent intent1;
    public final String TAG = "GoogleService";


    public GoogleService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_NONE);

            notificationManager.createNotificationChannel(notificationChannel);
            Notification notification = new Notification.Builder(this, channelId)
                    .build();
            startForeground(1, notification);
        } else
            startForeground(1, new Notification());
        Log.e(TAG, "in Google Services onCreate");
        intent1 = new Intent(str_receiver);
        GpsCacheManager.getInstance(this).getContacts(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "in Google Services onStartCommand");
        //this.intent1=intent;
        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(), 1 * 60 * 1000, notify_interval);

        return START_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @SuppressLint("MissingPermission")
    private void fn_getlocation() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnable && !isNetworkEnable) {

            } else {

                if (isNetworkEnable) {
                    location = null;
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1 * 60 * 1000, 300, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {

                            Log.e(TAG, "locationFromnetwork" + location.getLatitude() + "");
                            Log.e(TAG, location.getLongitude() + "");

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            fn_update(location);
                        }
                    }

                }


                if (isGPSEnable) {
                    location = null;
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1 * 60 * 1000, 300, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            Log.e(TAG, "locationfromgps" + location.getLatitude() + "");
                            Log.e(TAG, location.getLongitude() + "");
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            fn_update(location);
                            // fn_update(location);
                        }
                    }
                }


            }
        } else {
            Log.e("GoogleService", "Permission not granted for Location");
        }

    }

    @Override
    public void onContactsLoaded(List<GpsLog> gpsLogs) {
        contacts = gpsLogs;
        if (contacts.size() == 0) {
            onDataNotAvailable();
        }


    }

    @Override
    public void onContactsAdded() {
        // Toast.makeText(this,"contact Added",Toast.LENGTH_SHORT).show();
        Log.e("msg", "contact added");

    }

    @Override
    public void onDataNotAvailable() {
        //Toast.makeText(this,"No Contacts Yet",Toast.LENGTH_SHORT).show();
        Log.e("msg", "no contacts yet");

    }

    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            });

        }
    }

    private void fn_update(Location location) {
        if (SharedPreferencesMethod.getBoolean(getApplicationContext(), SharedPreferencesMethod.LOGIN)) {

            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
            String currentDateandTime1 = sdf1.format(new Date());
            Log.e(TAG, "Current time  " + currentDateandTime1);
            Log.e(TAG, "Lat  " + location.getLatitude());
            Log.e(TAG, "Long  " + location.getLongitude());
            Log.e(TAG, "Coming in fnupdate");
            intent1.putExtra("latutide", location.getLatitude() + "");
            intent1.putExtra("longitude", location.getLongitude() + "");
            sendBroadcast(intent1);
            if (contacts.size() > 0) {
                String str_lat = contacts.get(contacts.size() - 1).getLat_cord();
                String str_long = contacts.get(contacts.size() - 1).getLong_cord();
                float[] results1 = new float[1];
                Location.distanceBetween(Double.parseDouble(str_lat), Double.parseDouble(str_long), location.getLatitude(), location.getLongitude(), results1);
                float distanceInMetersgpsc1 = results1[0];
                boolean isWithinhalfkmgpsc1 = distanceInMetersgpsc1 > 400;

                if ((!str_lat.equals(String.valueOf(location.getLatitude())) || !str_long.equals(String.valueOf(location.getLongitude())))) {

                    if (isWithinhalfkmgpsc1) {
                        Log.e(TAG, "Not same GPS Coordinates");
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate = df.format(c.getTime());
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        GpsCacheManager.getInstance(this).addContact(this, String.valueOf(location.getLatitude()),
                                String.valueOf(location.getLongitude()), SharedPreferencesMethod.getString(getApplicationContext()
                                        , SharedPreferencesMethod.PERSON_ID), formattedDate, currentDateandTime);
                    }
                } else {

                    Log.e(TAG, "same Gps Coordinates");
                }

            } else {
                Calendar cfirst = Calendar.getInstance();
                SimpleDateFormat dffirst = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDatefirst = dffirst.format(cfirst.getTime());
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());

                GpsCacheManager.getInstance(this).addContact(this, String.valueOf(location.getLatitude()),
                        String.valueOf(location.getLongitude()), SharedPreferencesMethod.getString(getApplicationContext()
                                , SharedPreferencesMethod.PERSON_ID), formattedDatefirst, currentDateandTime);
            }

        }
    }
}

