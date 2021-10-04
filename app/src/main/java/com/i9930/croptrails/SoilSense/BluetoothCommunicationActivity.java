package com.i9930.croptrails.SoilSense;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import androidRecyclerView.MessageAdapter;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SoilSense.Adapter.SoilSensTabularAdapterSend;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;


public class BluetoothCommunicationActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    String TAG = "BluetoothChat";
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;


    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "bt_device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private EditText mOutEditText;
    private Button mSendButton;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;

    // Member object for the chat services
    private BluetoothChatService mChatService = null;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MessageAdapter mAdapter;

    public int counter = 0;

    private List<androidRecyclerView.Message> messageList = new ArrayList<androidRecyclerView.Message>();
    TextView messageTv;
    Toolbar mActionBarToolbar;
    GoogleApiClient mGoogleApiClient;
    String farmId = null;
    Button getDataButton;
    LinearLayout headingLayout, dropLayout;
    ImageView dropImage1, dropImage2, dropImage3;

    AdView mAdView;
    private void loadAds(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        List<String> testDeviceIds = Arrays.asList("1126E5CF85D37A9661950E74DF5A73FE");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
//        mAdView.setAdUnitId("ca-app-pub-5740952327077672/5919581872");
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.e(TAG,"Add Errro "+adError.toString());
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt_main_activity);
        Intent intent = getIntent();
        if (intent != null) {
            farmId = intent.getStringExtra(SharedPreferencesMethod.SVFARMID);
        }
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        dropImage1 = findViewById(R.id.dropImage1);
        dropImage2 = findViewById(R.id.dropImage2);
        dropImage3 = findViewById(R.id.dropImage3);
        dropLayout = findViewById(R.id.dropLayout);
        headingLayout = findViewById(R.id.headingLayout);
        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        mOutEditText.setOnEditorActionListener(mWriteListener);
        mSendButton = (Button) findViewById(R.id.button_send);
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(getResources().getString(R.string.soil_sense_go));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        loadAds();
        getDataButton = findViewById(R.id.getDataButton);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MessageAdapter(getBaseContext(), messageList);
//        mRecyclerView.setAdapter(mAdapter);
        messageTv = findViewById(R.id.messageTv);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
//        mRecyclerView.setVisibility(View.GONE);
        headingLayout.setVisibility(View.GONE);
//        sendSoilDataList.add(new SendSoilData.Data("0", "", null, null, null));
        mRecyclerView.setNestedScrollingEnabled(false);
        tabularAdapterSend = new SoilSensTabularAdapterSend(this, sendSoilDataList, new SoilSensTabularAdapterSend.OnSoilDateDeleteListener() {
            @Override
            public void onItemRemoved(int index) {

                try {
                    sendSoilDataList.remove(index);
                    tabularAdapterSend.notifyDataSetChanged();

                    if (sendSoilDataList.size() > 0) {
                        headingLayout.setVisibility(View.VISIBLE);
                    } else {
                        headingLayout.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        mRecyclerView.setAdapter(tabularAdapterSend);
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mprovider = locationManager.getBestProvider(criteria, false);
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLastLocation = locationManager.getLastKnownLocation(mprovider);
            //locationManager.requestLocationUpdates(mprovider, 100, 1, this);

        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100); //  minute interval
        mLocationRequest.setFastestInterval(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        getLocation();
        enableGPS();
//        displayLocationSettingsRequest(this);


    }

    Timer timer;
    LocationManager locationManager;
    String mprovider;
    LocationRequest mLocationRequest;

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            enableGPS();
//                            displayLocationSettingsRequest(BluetoothCommunicationActivity.this);
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(BluetoothCommunicationActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;

            }
        }
    };

    private void getLocation() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    //Location Permission already granted
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

                    try {
                        if (mprovider != null && !mprovider.equals("")) {
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            mLastLocation = locationManager.getLastKnownLocation(mprovider);
                            //locationManager.requestLocationUpdates(mprovider, 100, 1, this);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    //Request Location Permission
//                checkLocationPermission();
                }
            } else {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

                try {
                    if (mprovider != null && !mprovider.equals("")) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mLastLocation = locationManager.getLastKnownLocation(mprovider);
                        //locationManager.requestLocationUpdates(mprovider, 100, 1, this);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            mSendButton.setBackgroundColor(getResources().getColor(R.color.faded_text));
            mSendButton.setTextColor(getResources().getColor(R.color.black));
        } else {
            if (mChatService == null) setupChat();
        }

        Log.e(TAG, "onstart");

    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (mChatService != null) {
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                mChatService.start();
            }
        }
    }

    public interface StateChangeListener {
        public void onStateChanged(int state);
    }

    SoilSensTabularAdapterSend tabularAdapterSend;

    private void setupChat() {
//        SoilDetails det = new SoilDetails("" + 61 / 2);
//        SendSoilData.Data data = new SendSoilData.Data("21.813372", "76.346951", det, det, AppConstants.getCurrentDateTime());
//        sendSoilDataList.add(data);
//        SoilDetails det2 = new SoilDetails("" + 52 / 2);
//        SendSoilData.Data data2 = new SendSoilData.Data("21.813372", "76.346951", det2, det2, AppConstants.getCurrentDateTime());
//        sendSoilDataList.add(data2);
        mSendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TextView view = (TextView) findViewById(R.id.edit_text_out);
                String message = view.getText().toString();
                isForUpload = true;
//                sendMessage(message);
                if (sendSoilDataList != null && sendSoilDataList.size() > 0)
                    addDetails();
//                    Toasty.info(BluetoothChat.this,"Sending",Toast.LENGTH_SHORT).show();
                else
                    Toasty.info(BluetoothCommunicationActivity.this, "Please get data from device to post", Toast.LENGTH_SHORT).show();
            }
        });
        getDataButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                /*SoilDetails det = new SoilDetails("" + (AppConstants.getRandomInt(1,80)/2));
                String lat = "0", lon = "0";
                if (mLastLocation != null) {
                    lat = mLastLocation.getLatitude() + "";
                    lon = mLastLocation.getLongitude() + "";
                }
                SendSoilData.Data data = new SendSoilData.Data(lat, lon, det, det, AppConstants.getCurrentDateTime());
                Toast.makeText(BluetoothCommunicationActivity.this, ""+new Gson().toJson(data), Toast.LENGTH_LONG).show();
                sendSoilDataList.add(data);
                tabularAdapterSend.notifyDataSetChanged();*/

                if (isConnected) {
                    sendMessage("#");
                } else {
                    Toasty.info(BluetoothCommunicationActivity.this, getResources().getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mChatService = new BluetoothChatService(this, mHandler, new StateChangeListener() {
            @Override
            public void onStateChanged(int state) {
                try {
                    if (state == BluetoothChatService.STATE_CONNECTED) {
                        isForUpload = true;
                        isConnected = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSendButton.setBackgroundColor(getResources().getColor(R.color.darkgreen));
                                mSendButton.setTextColor(getResources().getColor(R.color.white));
                                messageTv.setText("");
                                getDataButton.setBackgroundColor(getResources().getColor(R.color.darkgreen));
                                getDataButton.setTextColor(getResources().getColor(R.color.white));
                                if (connectedMenu != null) {
                                    connectedMenu.setIcon(R.drawable.connected_bt);
                                    connectedMenu.setTitle(getResources().getString(R.string.title_connected_to));
                                }
                            }
                        });
                    } else {

                        isConnected = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSendButton.setBackgroundColor(getResources().getColor(R.color.faded_text));
                                mSendButton.setTextColor(getResources().getColor(R.color.black));
                                getDataButton.setBackgroundColor(getResources().getColor(R.color.faded_text));
                                getDataButton.setTextColor(getResources().getColor(R.color.black));
                                messageTv.setText("");
                                if (connectedMenu != null) {
                                    connectedMenu.setIcon(R.drawable.connected_non_bt);
                                    connectedMenu.setTitle(getResources().getString(R.string.not_connected));
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toasty.error(BluetoothCommunicationActivity.this, "exc: " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    boolean isConnected = false;

    @Override
    public synchronized void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();

        /*if (timer!=null){
            try {
                timer.cancel();

            }catch (Exception e){
                e.printStackTrace();
            }

        }*/
    }

    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }


    private void sendMessage(String message) {

        // Check that we're actually connected before trying anything
        Log.e(TAG, "Sending " + message);

        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            try {
                Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
//                Intent serverIntent = new Intent(this, DeviceListActivity.class);
//                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        message = "#";
        // Check that there's actually something to send

        if (message.length() > 0) {
            // Get the bt_message bytes and tell the BluetoothChatService to write
            String finalMessage = message;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] send = finalMessage.getBytes();
                        mChatService.write(send);
                        mOutStringBuffer.setLength(0);
                        mOutEditText.setText(mOutStringBuffer);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
            new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                    // If the action is a key-up event on the return key, send the bt_message
                    if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                        String message = view.getText().toString();
                        sendMessage(message);
                    }
                    return true;
                }
            };

    // The Handler that gets information back from the BluetoothChatService
    boolean isForUpload = false;

    private void addDetails() {
        List<SendSoilData.Data> list = new ArrayList<>();
        for (int i = 0; i < sendSoilDataList.size(); i++) {
            list.add(sendSoilDataList.get(i));
        }

        SendSoilData sendSoilData = new SendSoilData(SharedPreferencesMethod.getString(BluetoothCommunicationActivity.this, SharedPreferencesMethod.COMP_ID),
                SharedPreferencesMethod.getString(BluetoothCommunicationActivity.this, SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(BluetoothCommunicationActivity.this, SharedPreferencesMethod.TOKEN),
                farmId, list);
//        progressBar.setVisibility(View.VISIBLE);
        String data = new Gson().toJson(sendSoilData);
        Log.e(TAG, "Sending New Param " + data);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(this, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        apiInterface.addSoilSenseData(sendSoilData).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response1) {
                String error = null;
                if (response1.isSuccessful()) {
                    StatusMsgModel response = response1.body();
                    Log.e(TAG, "Sending New Param Response " + new Gson().toJson(response));
                    if (response.getStatus() == 1) {
                        Toasty.success(BluetoothCommunicationActivity.this, "Data added successfully", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        Toasty.error(BluetoothCommunicationActivity.this, "Failed to add data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
//                        Toast.makeText(context,"Failed to add farm location",Toast.LENGTH_LONG).show();
//                        Toasty.error(BluetoothCommunicationActivity.this, "Failed to add data", Toast.LENGTH_SHORT).show();
                        Toasty.error(BluetoothCommunicationActivity.this, "Failed to add data", Toast.LENGTH_SHORT).show();

                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "Sending New Param Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                isForUpload = false;
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e(TAG, "Sending New Param Failure " + t.toString());
//                Toasty.error(BluetoothCommunicationActivity.this, "Failed to add data", Toast.LENGTH_SHORT).show();
                Toasty.error(BluetoothCommunicationActivity.this, "Failed to add data", Toast.LENGTH_SHORT).show();

                isForUpload = false;
            }
        });
    }

    List<SendSoilData.Data> sendSoilDataList = new ArrayList<>();
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mAdapter.notifyDataSetChanged();
                    messageList.add(new androidRecyclerView.Message(counter++, writeMessage, "Me"));
                    break;
                case MESSAGE_READ:
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           byte[] readBuf = (byte[]) msg.obj;
                           // construct a string from the valid bytes in the buffer
                           String readMessage = new String(readBuf, 0, msg.arg1);
//                    Toast.makeText(BluetoothChat.this, ""+readMessage, Toast.LENGTH_LONG).show();
                           if (readMessage != null && readMessage.length() > 0/*&&!readMessage.equals("#")*/) {
                               char c = readMessage.charAt(0); // c='a'
                               int ascii = (int) c;
                               float ascii2 = ascii;
                               messageTv.setText(getResources().getString(R.string.soil_moisture_value) + " : " + ascii2 / 2 + "%");
//                        if (isForUpload) {
                               SoilDetails det = new SoilDetails("" + ascii2 / 2);
                               String lat = "0", lon = "0";
                               if (mLastLocation != null) {
                                   lat = mLastLocation.getLatitude() + "";
                                   lon = mLastLocation.getLongitude() + "";
                               }
                               SendSoilData.Data data = new SendSoilData.Data(lat, lon, det, det, AppConstants.getCurrentDateTime());
                               sendSoilDataList.add(data);
                               if (tabularAdapterSend != null)
                                   tabularAdapterSend.notifyDataSetChanged();
                               headingLayout.setVisibility(View.VISIBLE);
                               dropLayout.setVisibility(View.VISIBLE);
                               if (ascii2 < 11) {
                                   //dry
                                   dropImage1.setVisibility(View.GONE);
                                   dropImage2.setVisibility(View.VISIBLE);
                                   dropImage2.setImageResource(R.drawable.ic_iconfinder_holidays_tree_1459431);
                                   dropImage3.setVisibility(View.GONE);

                               } else if (ascii2 < 21) {
                                   //one drop
                                   dropImage1.setVisibility(View.GONE);
                                   dropImage2.setVisibility(View.VISIBLE);
                                   dropImage2.setImageResource(R.drawable.ic_drop);
                                   dropImage3.setVisibility(View.GONE);
                               } else if (ascii2 < 31) {
                                   // two drops
                                   dropImage1.setVisibility(View.VISIBLE);
                                   dropImage2.setVisibility(View.VISIBLE);
                                   dropImage2.setImageResource(R.drawable.ic_drop);
                                   dropImage1.setImageResource(R.drawable.ic_drop);
                                   dropImage3.setVisibility(View.GONE);
                               } else {
                                   // three drops
                                   dropImage1.setVisibility(View.VISIBLE);
                                   dropImage2.setVisibility(View.VISIBLE);
                                   dropImage2.setImageResource(R.drawable.ic_drop);
                                   dropImage3.setVisibility(View.VISIBLE);
                                   dropImage1.setImageResource(R.drawable.ic_drop);
                                   dropImage3.setImageResource(R.drawable.ic_drop);
                               }

//                        }
                           } else {
                               messageTv.setText("N/A");
                           }
                           mAdapter.notifyDataSetChanged();
                           messageList.add(new androidRecyclerView.Message(counter++, readMessage, mConnectedDeviceName));
                       }
                   });
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    Log.e(TAG, "DeviceActivity2 onActivityResult " + address);
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mChatService.connect(device);
                } else {
//                    Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

            case 1000:
                if (requestCode == 1000) {
                    if (resultCode == Activity.RESULT_OK) {
                        getLocation();
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        //Write your code if there's no result
                        enableGPS();
//                        displayLocationSettingsRequest(this);

                    }
                }
                break;
        }
    }

    public void connect(View v) {
        Intent serverIntent = new Intent(this, DeviceActivity2.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    public void discoverable(View v) {
        ensureDiscoverable();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        getLocation();
    }

    @Override
    public void onProviderDisabled(String s) {
        enableGPS();
//        displayLocationSettingsRequest(this);
    }

    private void enableGPS() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(BluetoothCommunicationActivity.this).build();
            mGoogleApiClient.connect();
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            builder.setAlwaysShow(true); // this is the key ingredient
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
                            try {
                                getLocation();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.

                            try {
                                getLocation();
                                status.startResolutionForResult(BluetoothCommunicationActivity.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            //Toast.makeText(MapsActivity.this, "Settings change unavailable", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    MenuItem connectedMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bt, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_bluetooth);

        connectedMenu = menu.findItem(R.id.action_bluetooth_connected);

        searchMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent serverIntent = new Intent(BluetoothCommunicationActivity.this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                return false;
            }
        });
        return true;
    }
}