package com.i9930.croptrails.Scan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.i9930.croptrails.FarmerInnerDashBoard.FrmrInnrDtlsPagerActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SoilSense.Dashboard.SoilSensDashboardActivity;
import com.i9930.croptrails.Test.FarmDetailsVettingActivity;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.QRUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.WebView.WebViewActivity;

import java.io.IOException;

public class ScanQRActivity extends AppCompatActivity {
    String TAG = "ScanQRActivity";
    ScanQRActivity context;
    //Barcode
    private IntentIntegrator qrScan;
    Toolbar mActionBarToolbar;
    TextView title;
    SurfaceView surfaceView;
    String vetting = null;
    boolean isOnlySoilSens = false;
    String userId,personId,token,auth,compId,role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        surfaceView = findViewById(R.id.surfaceView);
        context = this;
        compId= SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        userId=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        personId=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        token=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        role = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE);
        isOnlySoilSens = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_ONLY_SOIL_SENS);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        title = findViewById(R.id.tittle);
        title.setText(getString(R.string.scan_label));
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //Barcode
        qrScan = new IntentIntegrator(this);
        qrScan.setPrompt("");
        qrScan.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String url = result.getContents();
            Log.e(TAG, "Url " + url);
            if (url == null) {
                //Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
                finish();
            } else {
                try {

                    if (url.contains("croptrails.farm") || url.contains("croptrails.com") || url.contains("croptrace.com") || url.contains("croptrace.farm")) {
                        if (url.contains(QRUtils.HARVEST)) {
                            openWebView(result.getContents());
                        } else if (url.contains(QRUtils.FARM)) {
                            String[] bits = url.split("/");
                            String lastOne = bits[bits.length - 1];
                            getFarmDetails(result.getContents(),lastOne);

                        } else {

                            Toasty.error(context, getString(R.string.invalid_qr_code_msg), Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } else {
                        Toasty.error(context, getString(R.string.invalid_qr_code_msg), Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(context, getString(R.string.invalid_qr_code_msg), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void openWebView(String url){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
        finish();
    }

    private void getFarmDetails(String fullUrl,String farmId) {
        JsonObject object = new JsonObject();
        object.addProperty("farm_id", farmId);
        object.addProperty("user_id", userId);
        object.addProperty("token", token);
        Log.e(TAG, "SEND FULL DETAIL PARAM  " + new Gson().toJson(object));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getFarmIdFromEncFarmId(object).enqueue(new Callback<EncFarmResponse>() {
            @Override
            public void onResponse(Call<EncFarmResponse> call, Response<EncFarmResponse> response1) {
                String error = null;
                Log.e(TAG, "Response FULL CODE" + response1.code());
                if (response1.isSuccessful()) {
                    Log.e(TAG, "Response FULL  " + new Gson().toJson(response1.body()));

                    EncFarmResponse data = response1.body();
                    try {
                        if (data.getData()!=null&&data.getData().size()>0) {
                            EncFarmData details =data.getData().get(0);
                            vetting=details.getIsSelected();
                            if (details.getCompId()!=null
                                    && details.getCompId().toString().trim().equals(compId)
                                    && AppConstants.isValidString(details.getIsSelected())) {
                                if (role.equals(role.equalsIgnoreCase(AppConstants.ROLES.ADMIN) ||
                                        role.equalsIgnoreCase(AppConstants.ROLES.SUPER_ADMIN))) {
                                    navigateToFarm(false, details.getFarmId(),fullUrl);
                                } else if (details.getSvId()!=null && details.getSvId().toString().equals(personId)) {
                                    navigateToFarm(false, details.getFarmId(),fullUrl);
                                } else if (details.getOwnerId()!=null && details.getOwnerId().toString().equals(personId)) {
                                    navigateToFarm(true, details.getFarmId(),fullUrl);
                                } else {
                                    openWebView(fullUrl);
                                }
                            } else {
                                openWebView(fullUrl);
                            }
                        }else{
                            openWebView(fullUrl);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        openWebView(fullUrl);
                    }

                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response1.errorBody().string().toString();
                        Log.e(TAG, " Error FULL " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<EncFarmResponse> call, Throwable t) {
                Log.e(TAG, "Failure FULL " + t.toString());
                openWebView(fullUrl);
            }
        });

    }

    private void navigateToFarm(boolean isFarmer,String farmId,String fullUrl) {
        if (AppConstants.isValidString(vetting)&&farmId!=null) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARM_VETTING, vetting);
            Intent intent = null;
            if (!isFarmer){
                if (vetting != null && (vetting.trim().equals(AppConstants.VETTING.SELECTED) ||
                        vetting.trim().equals(AppConstants.VETTING.DELINQUENT))) {
                    if (isOnlySoilSens) {
                        intent = new Intent(context, SoilSensDashboardActivity.class);
                    } else
                        intent = new Intent(context, TestActivity.class);

                } else if (isOnlySoilSens) {
                    intent = new Intent(context, SoilSensDashboardActivity.class);

                } else {
                    intent = new Intent(context, FarmDetailsVettingActivity.class);

                }
            }else{
                intent = new Intent(context, FrmrInnrDtlsPagerActivity.class);
            }

//            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTUAL_AREA, farmResultNew.getActualArea() + "");
//            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, farmResultNew.getStandingArea() + "");
//            SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.EXPECTED_AREA,
//                    Double.valueOf(farmResultNew.getExpArea()));
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, farmId.toString());
//                                                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, /*farmResultNew.getFarmMasterNum()*/"2349");
//                                                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_ID, /*farmResultNew.getFarmMasterNum()*/"100131");
            startActivity(intent);
        }else {
            openWebView(fullUrl);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
