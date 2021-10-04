package com.i9930.croptrails.Utility;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

import com.i9930.croptrails.RoomDatabase.Gps.GetSetInterface;
import com.i9930.croptrails.RoomDatabase.Gps.GpsCacheManager;
import com.i9930.croptrails.RoomDatabase.Gps.GpsLog;

import static android.content.Context.LOCATION_SERVICE;

public class LocationInsertReciever extends BroadcastReceiver implements LocationListener, GetSetInterface {
    String TAG = "LocationInsertReciever";
    List<GpsLog> contacts;
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        GpsCacheManager.getInstance(context).getContacts(this);


    }


    private void fn_getlocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnable && !isNetworkEnable) {

            } else {

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
                            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.LOGIN))
                                fn_update(location, context);
                            else
                                Log.e(TAG, "User is not logged in");
                        }
                    }
                }
                else if (isNetworkEnable) {
                    location = null;
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1 * 60 * 1000, 300, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {

                            Log.e(TAG, "locationFromnetwork " + location.getLatitude() + "");
                            Log.e(TAG, location.getLongitude() + "");

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.LOGIN))
                                fn_update(location, context);
                            else
                                Log.e(TAG, "User is not logged in");
                        }
                    }

                }



            }
        } else {
            Log.e("GoogleService", "Permission not granted for Location");
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onContactsLoaded(List<GpsLog> gpsLogList) {
        contacts = gpsLogList;
        fn_getlocation();
    }

    @Override
    public void onContactsAdded() {

    }

    @Override
    public void onDataNotAvailable() {

    }

    private void fn_update(Location location, Context context) {
        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.LOGIN)) {

            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
            String currentDateandTime1 = sdf1.format(new Date());

            if (contacts.size() > 0) {
                String str_lat = contacts.get(contacts.size() - 1).getLat_cord();
                String str_long = contacts.get(contacts.size() - 1).getLong_cord();
                float[] results1 = new float[1];
                Location.distanceBetween(Double.parseDouble(str_lat), Double.parseDouble(str_long), location.getLatitude(), location.getLongitude(), results1);
                float distanceInMetersgpsc1 = results1[0];
                boolean isWithinhalfkmgpsc1 = distanceInMetersgpsc1 > 400;
                if ((!str_lat.equals(String.valueOf(location.getLatitude())) || !str_long.equals(String.valueOf(location.getLongitude())))) {
                    if (isWithinhalfkmgpsc1) {
                        Log.e(TAG, "Not same GPS");
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate = df.format(c.getTime());
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());

                        Log.e(TAG, "Current time  " + currentDateandTime1);
                        Log.e(TAG, "Lat  " + location.getLatitude());
                        Log.e(TAG, "Long  " + location.getLongitude());
                        Log.e(TAG, "Coming in fnupdate");

                        GpsCacheManager.getInstance(context).addContact(this, String.valueOf(location.getLatitude()),
                                String.valueOf(location.getLongitude()), SharedPreferencesMethod.getString(context
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
                /*DatabaseHandler dbsavefirst = new DatabaseHandler(this);
                dbsavefirst.addGpsCordinates(new SaveGpsGetterSetter(String.valueOf(location.getLatitude()),
                        String.valueOf(location.getLongitude()), SharedPreferencesMethod.getString(getApplicationContext()
                        , SharedPreferencesMethod.SVUSERID), currentDateandTime, formattedDatefirst));*/
                GpsCacheManager.getInstance(context).addContact(this, String.valueOf(location.getLatitude()),
                        String.valueOf(location.getLongitude()), SharedPreferencesMethod.getString(context
                                , SharedPreferencesMethod.PERSON_ID), formattedDatefirst, currentDateandTime);
            }

        /* ExpApiInterface apiService = RetrofitClientInstance.getRetrofitInstance().create(ExpApiInterface.class);
        apiService.savePost(SharedPreferencesMethod.getString(this,SharedPreferencesMethod.USERNAME),
                SharedPreferencesMethod.getString(this,SharedPreferencesMethod.PASSWORD),
                location.getLatitude()+"_"+location.getLongitude(),"").enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
            }
        });*/
        }
    }
}
