package com.i9930.croptrails.Utility;

import android.content.Context;
import android.location.Location;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.i9930.croptrails.RoomDatabase.Gps.GetSetInterface;
import com.i9930.croptrails.RoomDatabase.Gps.GpsCacheManager;
import com.i9930.croptrails.RoomDatabase.Gps.GpsLog;


public class FusedLocationService implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GetSetInterface {
    private static final long INTERVAL = 3600000;//1000
    private static final long FASTEST_INTERVAL = 3600000;//500
    //	Activity locationActivity;
    String TAG="Location";
    Context locationActivity;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private Location location;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    Context mcontext;
    List<GpsLog> gpsLogList;

    public FusedLocationService(Context context) {
        try {


        mcontext=context;
        Log.e(TAG,"IN fused Location Service");
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.locationActivity = context;

        GpsCacheManager.getInstance(mcontext).getContacts(this);

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        if (googleApiClient != null) {
            Log.d("GoogApiClient", googleApiClient.toString());
            googleApiClient.connect();
        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            Location currentLocation = fusedLocationProviderApi
                    .getLastLocation(googleApiClient);
//            Log.e(TAG,"curr"+currentLocation.getLatitude()+" "+currentLocation.getLongitude());
            GpsCacheManager.getInstance(mcontext).getContacts(this);
            fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //System.out.println("Current Long is: " + location.getLongitude());
        try {


            Log.e(TAG, location.getLatitude() + " " + location.getLongitude());
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (SharedPreferencesMethod.getBoolean(mcontext, SharedPreferencesMethod.LOGIN)) {
                        insertNewGps(location);
                    } else {
                        Log.e(TAG, "User logged out");
                    }
                    //Do something after 100ms
                }
            }, 500);
        }catch (Exception e){
            e.printStackTrace();
        }

       /* PreferenceClass.setStringPreference(locationActivity, Constant.StringClass.CURRENT_LATITUDE, "" + location.getLatitude());
        PreferenceClass.setStringPreference(locationActivity, Constant.StringClass.CURRENT_LONGITUDE, "" + location.getLongitude());
*/    }

    private void insertNewGps(Location location) {
        try {


            if ((gpsLogList.size() > 0)) {

                String str_lat = gpsLogList.get(gpsLogList.size() - 1).getLat_cord();
                String str_long = gpsLogList.get(gpsLogList.size() - 1).getLong_cord();
                float[] results1 = new float[1];
                Location.distanceBetween(Double.parseDouble(str_lat), Double.parseDouble(str_long), location.getLatitude(), location.getLongitude(), results1);
                float distanceInMetersgpsc1 = results1[0];
                double distanceinKm = distance(Double.parseDouble(str_lat), Double.parseDouble(str_long), location.getLatitude(), location.getLongitude());
                Log.e(TAG, "In Km: " + distanceinKm + " In Meter: " + distanceInMetersgpsc1);
                boolean isWithinhalfkmgpsc1 = distanceinKm > 0.400;

                if (isWithinhalfkmgpsc1) {
                    insertGps(location);
                    Log.e(TAG, "Out of 400m Range Coordinate inserted : " + location.getLatitude() + " " + location.getLongitude());

                } else {
                    Log.e(TAG, "In 400m Range");
                }
            } else {
                insertGps(location);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void insertGps(Location location) {
        try {


        Calendar cfirst = Calendar.getInstance();
        SimpleDateFormat dffirst = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDatefirst = dffirst.format(cfirst.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        GpsCacheManager.getInstance(mcontext).addContact(this, String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude()), SharedPreferencesMethod.getString(mcontext
                        ,SharedPreferencesMethod.PERSON_ID), formattedDatefirst, currentDateandTime);
        GpsCacheManager.getInstance(mcontext).getContacts(this);
        Log.e(TAG,"Gps coordinates Inserted");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Location getLocation() {
        return this.location;
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        googleApiClient.connect();
    }

    @Override
    public void onContactsLoaded(List<GpsLog> gpsLogList) {
        this.gpsLogList=new ArrayList<>();
            this.gpsLogList=gpsLogList;
            for(int i=0;i<gpsLogList.size();i++){
                Log.e(TAG,gpsLogList.get(i).getLat_cord()+" "+gpsLogList.get(i).getLong_cord()+" "+gpsLogList.get(i).getEnter_date()+" "+gpsLogList.get(i).getTime()+" "+gpsLogList.get(i).getSv_id());
            }

    }

    @Override
    public void onContactsAdded() {

    }

    @Override
    public void onDataNotAvailable() {

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}