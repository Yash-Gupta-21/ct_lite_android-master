package com.i9930.croptrails.Maps.Offline;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import com.i9930.croptrails.Maps.Offline.Adapter.OfflineMapSuggestionAdapter;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmCacheManager;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmLoadListener;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

public class OfflineMapActivity extends AppCompatActivity implements FarmLoadListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static String TAG = "OfflineMapActivity";
    GoogleApiClient mGoogleApiClient;
    LocationManager locationManager;
    boolean isGPSEnabled, isNetworkEnabled;
    GPSTracker gps;
    Toolbar mActionBarToolbar;
    RecyclerView suggestionRecycler;
    GifImageView progressBar;
    List<String> suggestions = new ArrayList<>();
    Farm farm;
    TextView currentLocationButton;
    Location currentLocation;

    private void getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            Log.e(TAG, "Address:- " + address + ", " + city + ", " + state + ", " + country + ", " + postalCode + ", " + knownName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_map);
        TextView tittle = (TextView) findViewById(R.id.tittle);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        currentLocationButton = findViewById(R.id.currentLocationButton);
        progressBar = findViewById(R.id.progressBar_image);
        suggestionRecycler = findViewById(R.id.suggestionRecycler);
        suggestions.addAll(getSuggestions());
        OfflineMapSuggestionAdapter adapter = new OfflineMapSuggestionAdapter(this, suggestions);
        suggestionRecycler.setHasFixedSize(true);
        suggestionRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        suggestionRecycler.setAdapter(adapter);


        tittle.setText("Offline location");
        gps = new GPSTracker(OfflineMapActivity.this);
        getCurrentLocation();
        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.GONE);
                currentLocationButton.setClickable(false);
                currentLocationButton.setEnabled(false);

                getLoc();
//                new GetLocation().execute();
            }
        });
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String mprovider = locationManager.getBestProvider(criteria, false);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
/*
        if (!isGPSEnabled && !isNetworkEnabled) {
            enableGPS();
        }*/

        if (mprovider != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider, 100, 1, this);
        }

        FarmCacheManager.getInstance(this).getFarm(this, SharedPreferencesMethod.getString(this, SharedPreferencesMethod.SVFARMID));
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList != null && locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                currentLocation = location;

            }
        }
    };

    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest mLocationRequest;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

           /* if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                } else {
//                        checkLocationPermission();
                }
            } else {
                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }
            */
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {
                                currentLocation = location;
                                Log.e(TAG, " 10 Location: " + location.getLatitude() + ", " + location.getLongitude() + " Acc: " + location.getAccuracy());
                            }
                        }
                    });
        }
    }

    private void enableGPS() {
        /*if (mGoogleApiClient == null) {*/

        //Toast.makeText(context, "Coming in enable gps", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "In EnableGps");
        mGoogleApiClient = new GoogleApiClient.Builder(OfflineMapActivity.this)
                .addApi(LocationServices.API).addConnectionCallbacks(OfflineMapActivity.this)
                .addOnConnectionFailedListener(OfflineMapActivity.this).build();
        mGoogleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000000);
        locationRequest.setFastestInterval(2 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        // **************************
        builder.setAlwaysShow(true); // this is the key ingredient
        // **************************

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result
                        .getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(OfflineMapActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        break;
                }
            }
        });
        //}
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
        Log.e(TAG, "Disabled " + s);
        enableGPS();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                getCurrentLocation();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                //Log.e(TAG,"Cancelled");
//                enableGPS();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.help_menu_satsure, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.empty) {
//            Toast.makeText(getApplicationContext(), "help", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private List<String> getSuggestions() {
        List<String> suggestions = new ArrayList<>();
        suggestions.add("Please press the button at the bottom to mark your current location as Farm's location.");
//        suggestions.add("To mark your current location as farm location, click '"+currentLocationButton.getText().toString().trim()+"'.");
        suggestions.add("Once you are back online, you can mark the farm end points as usual using easy or pin drop mode.");
//        suggestions.add("After verifying and re-arrange of area coordinates you can submit this area as actual farm area.");
//        suggestions.add("After submission this area, you cannot change the farm area.");
        return suggestions;

    }


    @Override
    public void onFarmLoader(Farm farmData) {
        Log.e(TAG, "Offline fetch farm " + new Gson().toJson(farmData));
        this.farm = farmData;
    }

    private void getLoc() {
        Location location = null;
        if (gps == null)
            gps = new GPSTracker(OfflineMapActivity.this);
        if (currentLocation != null)
            location = currentLocation;
        else
            location = gps.location;

        if (location == null) {
//            Toasty.info(this,"Getting your location, Please wait...",Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Inserting 1");
            int i = 1;
            while (location == null) {
                Log.e(TAG, "while count " + i + " ");
                i++;
                location = gps.getLocation();
                if (location!=null)
                    break;
            }
            farm.setIsUpdated("Y");
            farm.setIsUploaded("N");
            farm.setLatCord("" + location.getLatitude());
            farm.setLongCord("" + location.getLongitude());
            FarmCacheManager.getInstance(getApplicationContext()).updateFarm(farm);
            showToast("Farm location captured");
            finish();
        } else {
            Log.e(TAG, "Inserting 2");
            farm.setIsUploaded("N");
            farm.setIsUpdated("Y");
            farm.setLatCord("" + location.getLatitude());
            farm.setLongCord("" + location.getLongitude());
            FarmCacheManager.getInstance(getApplicationContext()).updateFarm(farm);
            showToast("Farm location captured");
            finish();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                currentLocationButton.setClickable(true);
                currentLocationButton.setEnabled(true);
            }
        });
    }

    private class GetLocation extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            Location location;
            gps = new GPSTracker(OfflineMapActivity.this);
            location = gps.getLocation();
            if (location == null) {
                while (location != null) {
//                    gps = new GPSTracker(OfflineMapActivity.this);
                    location = gps.getLocation();
                }
                farm.setIsUpdated("Y");
                farm.setIsUploaded("N");
                farm.setLatCord("" + location.getLatitude());
                farm.setLongCord("" + location.getLongitude());
                FarmCacheManager.getInstance(getApplicationContext()).updateFarm(farm);
                showToast("Farm location captured");
                finish();
            } else {
                farm.setIsUploaded("N");
                farm.setIsUpdated("Y");
                farm.setLatCord("" + location.getLatitude());
                farm.setLongCord("" + location.getLongitude());
                FarmCacheManager.getInstance(getApplicationContext()).updateFarm(farm);
                showToast("Farm location captured");
                finish();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    currentLocationButton.setClickable(true);
                    currentLocationButton.setEnabled(true);
                }
            });

            return null;
        }
    }

    private void showToast(String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toasty.success(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

            }
        });

    }

}
