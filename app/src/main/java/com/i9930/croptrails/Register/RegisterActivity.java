package com.i9930.croptrails.Register;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.AESUtils2;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.BuildConfig;
import com.i9930.croptrails.ClusterSelection.ClusterSelectActivityActivity;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.CompSelect.CompSelectActivity;
import com.i9930.croptrails.CompSelect.Model.CompanyDatum;
import com.i9930.croptrails.CompSelect.Model.CompanyResponse;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Login.Model.CheckUserResponse;
import com.i9930.croptrails.Login.Model.CompParamDatum;
import com.i9930.croptrails.Login.Model.CompParamResponse;
import com.i9930.croptrails.Login.Model.LoginData;
import com.i9930.croptrails.Login.Model.Post;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegCacheManager;
import com.i9930.croptrails.RoomDatabase.CompRegistry.Model.CompRegResult;
import com.i9930.croptrails.RoomDatabase.CompRegistry.Model.ResponseCompReg;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;
import static com.i9930.croptrails.Utility.AppConstants.OLD_VERSION_STATUS;
import static com.i9930.croptrails.Utility.AppConstants.getCountryId;


public class RegisterActivity extends AppCompatActivity {
    RegisterActivity context;
    String TAG = "RegisterActivity";
    Resources resources;
    Toolbar mActionBarToolbar;
    @BindView(R.id.getOtpButton)
    Button getOtpButton;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etFatherName)
    EditText etFatherName;
    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.countryCodePicker)
    CountryCodePicker countryCodePicker;
    String osVersion;
    String internetSPeed = "";
    ViewFailDialog viewFailDialog;
    @BindView(R.id.connectivityLine)
    View connectivityLine;
    @BindView(R.id.tiOtp)
    TextInputLayout tiOtp;
    @BindView(R.id.etOtp)
    EditText etOtp;
    @BindView(R.id.verifyOtpButton)
    Button verifyOtpButton;

    @BindView(R.id.tiUserName)
    TextInputLayout tiUserName;

    @BindView(R.id.etUserName)
    EditText etUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = getResources();
        String languageToLoad = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;
        viewFailDialog = new ViewFailDialog();
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        compRegCacheManager = CompRegCacheManager.getInstance(context);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.this.onBackPressed();
            }
        });
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.register));
        clickEvents();
        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);
        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            if (fieldValue == Build.VERSION.SDK_INT) {
                osVersion = fieldName;
                builder.append(" : ").append(fieldName).append(" : ");
                builder.append("sdk=").append(fieldValue);
            }
            Log.e(TAG, "OS NAME " + fieldName);
        }

        Log.d(TAG, "OS: " + builder.toString());

        String preference = getCountryId(this);
        if (preference != null) {
            countryCodePicker.setCountryPreference(preference.toUpperCase());
        }
    }

    private void clickEvents() {
        getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etName.getText().toString()) || etName.getText().toString().length() < 3) {
                    etName.setError(resources.getString(R.string.invalid_farmer_name_msg));
                    etName.getParent().requestChildFocus(etName, etName);
                    AppConstants.failToast(context, "Please enter valid farmer name");
                } else if (TextUtils.isEmpty(etMobile.getText().toString()) || etMobile.getText().toString().length() < 3) {
                    etMobile.setError(resources.getString(R.string.invalid_mobile_number_msg));
                    etMobile.getParent().requestChildFocus(etMobile, etMobile);
                    AppConstants.failToast(context, "Please enter valid phone number");
                } else if (TextUtils.isEmpty(etUserName.getText().toString()) || etUserName.getText().toString().length() < 6) {
                    etUserName.setError("Invalid username");
                    etUserName.getParent().requestChildFocus(etUserName, etUserName);
                    AppConstants.failToast(context, "Please enter valid user name");
                } else {
                    checkUser();
                }
            }
        });
    }

    private void sendOtp() {
        String number = countryCodePicker.getSelectedCountryCodeWithPlus() + etMobile.getText().toString().trim();
        setUpVerificatonCallbacks();
        startPhoneNumberVerification(number);
//        AppConstants.successToast(context,"otp");
    }

    private void callPDBMAuth() {
        isFirebaseAuth = false;
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comp_id", "" + AppConstants.COMP_ID);
        jsonObject.addProperty("mob", etMobile.getText().toString().trim());
        apiInterface.callPDBMAuth(jsonObject).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "callPDBMAuth res " + new Gson().toJson(response.body()));

                    if (response.body().getStatus() == 1) {
                        tiOtp.setVisibility(View.VISIBLE);
                        etOtp.setVisibility(View.VISIBLE);
                        getOtpButton.setVisibility(View.GONE);
                        etName.setFocusable(false);
                        etMobile.setFocusable(false);
                        etUserName.setFocusable(false);
                        etUserName.setFocusable(false);
                        etFatherName.setFocusable(false);
                        countryCodePicker.setFocusable(false);
                        countryCodePicker.setEnabled(false);
                        countryCodePicker.setClickable(false);
                        getOtpButton.setVisibility(View.GONE);
                        verifyOtpButton.setVisibility(View.VISIBLE);
                        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isRegistered)
                                    mobileLogin(true);
                                else
                                    verifyPDBMAuth();
                            }
                        });
                    }
                } else {
                    try {
                        Log.e(TAG, "callPDBMAuth err " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e(TAG, "callPDBMAuth onFailure " + t.toString());
            }
        });
    }

    private void verifyPDBMAuth() {
        String mac = AppConstants.getMacAddr();
        String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();

        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comp_id", "" + AppConstants.COMP_ID);
        jsonObject.addProperty("mob", etMobile.getText().toString().trim());
        jsonObject.addProperty("name", etName.getText().toString().trim());
        jsonObject.addProperty("authFireBase", isFirebaseAuth);
        jsonObject.addProperty("isRegistered", isRegistered);
        jsonObject.addProperty("mac", mac);
        jsonObject.addProperty("countryCode", countryCode);
        jsonObject.addProperty("otp", etOtp.getText().toString().trim());
        jsonObject.addProperty("username", etUserName.getText().toString().trim());
        Log.e(TAG, "verifyPDBMAuth req " + new Gson().toJson(jsonObject));
        apiInterface.registerSv(jsonObject).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.e(TAG, "verifyPDBMAuth res " + new Gson().toJson(response.body()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    StatusMsgModel model=response.body();
//                    2400 - Invalid Request
//                    2401 - Invalid OTP
//                    2403 - OTP Expired
//                    2100 - Successful
//                    24001 - username exist
//                    24002 - mobile number already exist
                    if (model.getStatus()==1){
                        mobileLogin(true);
                    }else if (model.getStatus()==24001){
                        etUserName.setError(model.getMessage());
                        etUserName.setEnabled(true);
                        etUserName.setFocusable(true);
//                        AppConstants.failToast(context,error);
                    }else if (model.getStatus()==24002){
                        AppConstants.successToast(context,"Mobile number already registered.");
                    }else if (model.getStatus()==2400 ){
                        AppConstants.failToast(context,"Please enter valid OTP");
                    }else if (model.getStatus()==2401 ){
                        AppConstants.failToast(context,"Please enter valid OTP");
                    }else if (model.getStatus()==2403 ){
                        AppConstants.failToast(context,"OTP expired");
                    }else {
                        if (AppConstants.isValidString(response.body().getMsg()))
                            AppConstants.failToast(context, response.body().getMsg());
                        else
                            AppConstants.failToast(context, response.body().getMessage()    );

                    }

                } else {
                    try {
                        String error=response.errorBody().string();
                        if (response.code()==400){
                            StatusMsgModel msgModel= new Gson().fromJson(error,StatusMsgModel.class);
                            if (msgModel!=null){
                                if (msgModel.getStatus()==24001){
                                    etUserName.setError(msgModel.getMessage());
                                    etUserName.setEnabled(true);
                                    etUserName.setFocusable(true);
                                    AppConstants.failToast(context,error);
                                }else if (msgModel.getStatus()==24002){
                                    AppConstants.successToast(context,"Mobile number already registered.");
                                }

                            }
                        }
                        Log.e(TAG, "verifyPDBMAuth err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e(TAG, "verifyPDBMAuth onFailure " + t.toString());
            }
        });
    }

    private void mobileLogin(boolean flag) {
        showProgressDialog();
        String user_name = "";
        String password = "";
        user_name = etMobile.getText().toString();
        password = AppConstants.getMacAddr();

        if (TextUtils.isEmpty(password)) {
            Toasty.error(context, resources.getString(R.string.please_enter_password_msg), Toast.LENGTH_LONG, false).show();
            hideProgressDialog();
            return;
        }
        String appVersion = BuildConfig.VERSION_NAME;
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        String finalUser_name = user_name;
        String finalPassword = password;
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("mob",""+user_name);
        if (userResponse!=null)
            jsonObject.addProperty("comp_id",userResponse.getCompId());
        jsonObject.addProperty("password",""+password);
        jsonObject.addProperty("app_version",""+appVersion);
        jsonObject.addProperty("android_version",""+osVersion);
        jsonObject.addProperty("forced_online_mode",""+flag);
        Log.e(TAG,"mobileLogin REQ "+new Gson().toJson(jsonObject));
        apiService.mobileLogin(user_name, password, "", "", appVersion, osVersion, flag,AppConstants.COMP_ID).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                String error = null;

                isLoaded[0] = true;
                if (response.isSuccessful()) {
                    Post post = response.body();
                    Log.e(TAG, "mobileLogin Login Res " + new Gson().toJson(post));
                    /*if (response.body().getStatus() == 2300) {
                        String msg=response.body().getMessage();
                        if (!AppConstants.isValidString(msg))
                            msg=response.body().getMsg();
                        showDataWipeAlert(msg);
                    } else*/
                    if (response.body().getStatus() == OLD_VERSION_STATUS) {
                        Toasty.error(RegisterActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                    } else if (post.getStatus() == 1 || post.getStatus() == 3) {
                        //sandeep 2/8/19 comp reg
                        data(post, finalUser_name, finalPassword);
                    } else if (post.getStatus() == 2) {
                        hideProgressDialog();
                        Toasty.error(context, response.body().getMsg(), Toast.LENGTH_LONG).show();
                        sendOtp();
                    } else if (response.body().getStatus() == 5) {
                        hideProgressDialog();
                        sendOtp();
//                        viewFailDialog.showDialog(RegisterActivity.this, resources.getString(R.string.alert_title_label), resources.getString(R.string.account_deactivated_contact_admin_msg));

                    } else {
                        hideProgressDialog();
                        sendOtp();
//                        viewFailDialog.showDialog(RegisterActivity.this, resources.getString(R.string.invalid_username_password));
                    }
                    // Toast.makeText(LoginActivity.this, z, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        hideProgressDialog();
                        sendOtp();
//                        viewFailDialog.showDialog(RegisterActivity.this, resources.getString(R.string.failed_to_login));
                        Log.e(TAG, "mobileLogin Error "+ error);
                        //Toast.makeText(context, "Oops something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(RegisterActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                isLoaded[0] = true;
                long diff = newMillis - currMillis;
                notifyApiDelay(RegisterActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                hideProgressDialog();
//                progressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG ,"mobileLogin Fail" + t.toString());
                viewFailDialog.showDialog(RegisterActivity.this, resources.getString(R.string.failed_to_login));
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                internetFlow(isLoaded[0]);
            }
        }, AppConstants.DELAY);

    }

    public ProgressDialog mProgressDialog;

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing() && isActivityRunning) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                }
            });
        }
    }

    private void internetFlow(boolean isLoaded) {
        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLoaded) {
                        if (!ConnectivityUtils.isConnected(context)) {
                            AppConstants.failToast(context, resources.getString(R.string.check_internet_connection_msg));
                        } else {
                            ConnectivityUtils.checkSpeedWithColor(RegisterActivity.this, new ConnectivityUtils.ColorListener() {
                                @Override
                                public void onColorChanged(int color, String speed) {
                                    if (color == android.R.color.holo_green_dark) {
                                        connectivityLine.setVisibility(View.GONE);
//                                    connectivityLine.setBackgroundColor(getColor(R.color.blue));
                                    } else {
                                        connectivityLine.setVisibility(View.VISIBLE);
                                        connectivityLine.setBackgroundColor(getResources().getColor(color));
                                    }
                                    internetSPeed = speed;
                                }
                            }, 20);
                        }
                    }
                }
            }, AppConstants.DELAY);
        } catch (Exception e) {

        }

    }

    boolean isActivityRunning = false;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(resources.getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isActivityRunning)
                    mProgressDialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActivityRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityRunning = false;
    }

    CompRegCacheManager compRegCacheManager;

    private void data(Post post, String finalUser_name, String finalPassword) {
        boolean dontAllowLogin = false;
        boolean isNavigationToClusterSelection = false;
        String name = "";
        String role = post.getData().getRoleId();
        if (role.trim().equals("3")) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOGIN_TYPE, AppConstants.ROLES.FARMER);
            SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE, AppConstants.ROLES.FARMER);
        } else if (role.trim().equals("1")) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOGIN_TYPE, AppConstants.ROLES.ADMIN);
            SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE, AppConstants.ROLES.ADMIN);
            String ids = "";
            StringBuilder builder = new StringBuilder();
            if (post.getClusterIdList() != null) {
                isNavigationToClusterSelection = true;
                for (int i = 0; i < post.getClusterIdList().size(); i++) {
                    if (i == post.getClusterIdList().size() - 1)
                        builder.append(post.getClusterIdList().get(i).getClusterId());
                    else
                        builder.append(post.getClusterIdList().get(i).getClusterId()).append(",");
                }
                Log.e(TAG, "Cluster Ids " + builder.toString());

            } else {
                isNavigationToClusterSelection = false;
            }
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVCLUSTERID, builder.toString());
        } else if (role.trim().equals("2")) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOGIN_TYPE, AppConstants.ROLES.SUPERVISOR);
            SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE, AppConstants.ROLES.SUPERVISOR);
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVCLUSTERID, post.getData().getClusterId());
        } else if (role.trim().equals("0")) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOGIN_TYPE, AppConstants.ROLES.SUPER_ADMIN);
            SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE, AppConstants.ROLES.SUPER_ADMIN);
            StringBuilder builder = new StringBuilder();
            if (post.getClusterIdList() != null) {
                isNavigationToClusterSelection = true;
                for (int i = 0; i < post.getClusterIdList().size(); i++) {
                    if (i == post.getClusterIdList().size() - 1)
                        builder.append(post.getClusterIdList().get(i).getClusterId());
                    else
                        builder.append(post.getClusterIdList().get(i).getClusterId()).append(",");
                }
            } else
                isNavigationToClusterSelection = false;
            Log.e(TAG, "Cluster Ids " + builder.toString());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVCLUSTERID, builder.toString());
        } else if (role.trim().equals("4")) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOGIN_TYPE, AppConstants.ROLES.SUB_ADMIN);
            SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE, AppConstants.ROLES.SUB_ADMIN);
            StringBuilder builder = new StringBuilder();
            if (post.getClusterIdList() != null) {
                isNavigationToClusterSelection = true;
                for (int i = 0; i < post.getClusterIdList().size(); i++) {
                    if (i == post.getClusterIdList().size() - 1)
                        builder.append(post.getClusterIdList().get(i).getClusterId());
                    else
                        builder.append(post.getClusterIdList().get(i).getClusterId()).append(",");
                }
            } else
                isNavigationToClusterSelection = false;
            Log.e(TAG, "Cluster Ids " + builder.toString());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVCLUSTERID, builder.toString());
        } else if (role.trim().equals("7")) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOGIN_TYPE, AppConstants.ROLES.CLUSTER_ADMIN);
            SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE, AppConstants.ROLES.CLUSTER_ADMIN);
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVCLUSTERID, post.getData().getClusterId());
        } else {
            if (role.trim().equals("8")) {
                name = "Manager";
            } else if (role.trim().equals("9")) {
                name = "Vet/Lab";
            }

            dontAllowLogin = true;

        }

        if (!dontAllowLogin) {
            try {
                SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.USERNAME, AESUtils2.encrypt(finalUser_name));
                SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.PASSWORD, AESUtils2.encrypt(finalPassword));
//            SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE, AppConstants.SUPERVISOR);
                SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.UNAME, post.getData().getName());
                SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.IMAGE, post.getData().getImgLink());
                String oldId = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.USER_ID);
                String newId = AESUtils2.encrypt(post.getData().getUserId());


                boolean isPreviousAllowed = SharedPreferencesMethod2.getBoolean(context, SharedPreferencesMethod2.IS_FINGER_SELECTED);
                if (oldId != null && newId != null && !newId.equals(oldId) && !isPreviousAllowed) {
                    if (!SharedPreferencesMethod2.getBoolean(context, SharedPreferencesMethod2.IS_FINGER_SELECTED) &&
                            !SharedPreferencesMethod2.getBoolean(context, SharedPreferencesMethod2.IS_FINGER_DONT_ASK)) {
                        SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_FINGER_SELECTED, false);
                        SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_APP_LOCK, false);
                        SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_ALLOW_FINGER, false);
                        SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_FINGER_DONT_ASK, false);
                    }
                }
                SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.USER_ID, newId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.AUTHORIZATION, post.getJwToken());
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.ASSIGN_CALENDAR, true);
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.ADD_HC, true);
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.SEED_ALLOWED, true);
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.HARVEST_ALLOWED, true);
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED, true);
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.VISIT_ALLOWED, true);
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.EXPENSE__ALLOWED, true);
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.SUPERVISOR_ALLOWED_ADD_FARM, true);
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.FARMER_ALLOWED_ADD_FARM, true);
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.PLD_ALLOWED, true);
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.GERMINATION_ALLOWED, true);
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.FARMER_ALLOWED_ADD_ACTIVITY, true);
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_INPUT_COST, true);
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.FARMER_ALLOWED_UPDATE_FARM_DETAILS, true);

            //sandeep 2/8/19 comp reg
            LoginData loginData = post.getData();
            if (loginData.getCompName() != null && !TextUtils.isEmpty(loginData.getCompName())) {
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMPANY_NAME, loginData.getCompName());
            }
            if (loginData.getCompBanner() != null && !TextUtils.isEmpty(loginData.getCompBanner())) {
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_BANNER, loginData.getCompBanner());
            }
            if (loginData.getCompLogo() != null && !TextUtils.isEmpty(loginData.getCompLogo())) {
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_IMAGE, loginData.getCompLogo());
            }

            if (loginData.getName() != null)
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USERNAME, loginData.getName());
            else
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USERNAME, "");

            if (loginData.getImgLink() != null)
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_IMAGE, loginData.getImgLink());
            else
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_IMAGE, "");
            if (loginData.getMob() != null)
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_NUMBER, loginData.getMob());
            else
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_NUMBER, "");
            if (loginData.getEmail() != null)
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_EMAIL, loginData.getEmail());
            else
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_EMAIL, "");

            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.PASSWORD, finalPassword);
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.LOGIN, true);
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_ID, AppConstants.COMP_ID);
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.PERSON_ID, loginData.getPersonId());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_ID, post.getData().getUserId());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.CLUSTER_NAME, loginData.getClusterName());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_NAME, loginData.getName());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.TOKEN, post.getToken());

            storeCompRegData(post, isNavigationToClusterSelection);
        } else
            Toasty.error(context, "Currently " + name + " not allowed for app login", Toast.LENGTH_SHORT).show();
    }

    private void storeCompRegData(Post post, boolean isNavigationToClusterSelection) {
        getCompParams(post);
        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_COUNTRY_ID, post.getData().getCompCountry());
        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_LANG, post.getData().getUserLanguage());
        if (post.getData().getMulFactor() != null && !TextUtils.isEmpty(post.getData().getMulFactor())) {

            try {
                double d = Double.valueOf(post.getData().getMulFactor().trim());
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.AREA_UNIT_LABEL, post.getData().getUnitName());
                SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.AREA_MULTIPLICATON_FACTOR, d);
            } catch (Exception e) {
                AppConstants.setDefaultUnit(context);
            }
        } else {
            AppConstants.setDefaultUnit(context);
        }
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat output = new SimpleDateFormat(AppConstants.DATE_FORMAT_SERVER);
        final String curdate = output.format(date);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String personId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        Log.e(TAG, "PersonId " + personId + "   CompId " + compId + "  UserId " + userId + "   Token " + token);
        ApiInterface expApiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        Call<ResponseCompReg> compRegCall = expApiInterface.getCompRegData(compId, personId, userId, token);
        compRegCall.enqueue(new Callback<ResponseCompReg>() {
            @Override
            public void onResponse(Call<ResponseCompReg> call, Response<ResponseCompReg> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "compRegResultList " + new Gson().toJson(response.body()));

                    if (response.body().getStatus() == OLD_VERSION_STATUS) {
                        Toasty.error(RegisterActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                    } else if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(RegisterActivity.this, response.body().getMsg());
                    } else {
                        /*List<CompanyDatum> companyDatumList = post.getCompanyDatumList();
                        if (companyDatumList != null && companyDatumList.size() > 0) {
                            if (companyDatumList.get(0).getDateFormat() != null && !TextUtils.isEmpty(companyDatumList.get(0).getDateFormat()))
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, companyDatumList.get(0).getDateFormat());
                            else
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, AppConstants.DATE_FORMAT_SHOW);
                        } else {
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, AppConstants.DATE_FORMAT_SHOW);
                        }*/
                        if (post.getDateFormat() != null && !TextUtils.isEmpty(post.getDateFormat()))
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, post.getDateFormat());
                        else
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, AppConstants.DATE_FORMAT_SHOW);
                        ResponseCompReg responseCompReg = response.body();
                        List<CompRegResult> compRegResultList = responseCompReg.getResult();
                        // compRegCacheManager.deleteAllData(SplashActivity.this);
                        if (compRegResultList != null && compRegResultList.size() > 0) {
                            compRegCacheManager.deleteAllData();
                            for (int i = 0; i < compRegResultList.size(); i++) {
                                String parameter = compRegResultList.get(i).getFeatureName();
                                Log.e(TAG, "compRegResultList " + i + " " + new Gson().toJson(compRegResultList.get(i)));
                                try {
                                    String status = compRegResultList.get(i).getStatus();
                                    if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.IS_VETTING_FARM)) {
                                        if (status != null && status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_VETTING_COMP, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_VETTING_COMP, false);
                                    } else if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.SOIL_SENS_ONLY)) {
                                        if (status != null && status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_ONLY_SOIL_SENS, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_ONLY_SOIL_SENS, false);
                                    } else if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.SOIL_SENS_ENABLED)) {
                                        if (status != null && status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_SOIL_SENS_ENABLED, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_SOIL_SENS_ENABLED, false);
                                    } else if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.WAY_POINT_ENABLED)) {
                                        if (status != null && status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_WAY_POINT_ENABLED, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_WAY_POINT_ENABLED, false);
                                    } else if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.OMITANCE_AREA_ENABLED)) {
                                        if (status != null && status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_OMITANCE_AREA_ENABLED, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_OMITANCE_AREA_ENABLED, false);
                                    } else if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.IS_PREVIOUS_CROP_MANDATORY)) {
                                        if (status != null && status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_PREVIOUS_CROP_MANDATORY, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_PREVIOUS_CROP_MANDATORY, false);
                                    } else if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.CROSS_VETTING)) {
                                        if (status != null && status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_CROSS_VETTING_ENABLED, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_CROSS_VETTING_ENABLED, false);
                                    } else if (parameter != null && parameter.trim().equals(AppConstants.COMP_REG.AGRI_FINANCER)) {
                                        if (status != null && status.trim().equals("1"))
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_DELINQUENT_COMP, true);
                                        else
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_DELINQUENT_COMP, false);
                                    }
                                } catch (Exception e) {

                                }
                                compRegCacheManager.addCompRegData(null, new Gson().toJson(compRegResultList.get(i)), curdate, parameter);
                            }
                        }
//                        showCompImage();
                    }
                } else {
                    try {
                        Log.e(TAG, "compRegResultList rError " + response.code() + "  Error=> " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    /*List<CompanyDatum> companyDatumList = post.getCompanyDatumList();
                    if (companyDatumList != null && companyDatumList.size() > 0) {
                        if (companyDatumList.get(0).getDateFormat() != null && !TextUtils.isEmpty(companyDatumList.get(0).getDateFormat()))
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, companyDatumList.get(0).getDateFormat());
                        else
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, AppConstants.DATE_FORMAT_SHOW);
                    } else {
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, AppConstants.DATE_FORMAT_SHOW);
                    }*/
                    if (post.getDateFormat() != null && !TextUtils.isEmpty(post.getDateFormat()))
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, post.getDateFormat());
                    else
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, AppConstants.DATE_FORMAT_SHOW);
                }
                showCompImage(isNavigationToClusterSelection);

            }

            @Override
            public void onFailure(Call<ResponseCompReg> call, Throwable t) {
                Log.e("RFerror", t.toString());
                AppConstants.setDefaultUnit(context);
                /*List<CompanyDatum> companyDatumList = post.getCompanyDatumList();
                if (companyDatumList != null && companyDatumList.size() > 0) {
                    if (companyDatumList.get(0).getDateFormat() != null && !TextUtils.isEmpty(companyDatumList.get(0).getDateFormat()))
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, companyDatumList.get(0).getDateFormat());
                    else
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, AppConstants.DATE_FORMAT_SHOW);
                } else {
                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, AppConstants.DATE_FORMAT_SHOW);
                }*/
                if (post.getDateFormat() != null && !TextUtils.isEmpty(post.getDateFormat()))
                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, post.getDateFormat());
                else
                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_DATE_FORMAT, AppConstants.DATE_FORMAT_SHOW);
                showCompImage(isNavigationToClusterSelection);
            }
        });
    }

    private void showCompImage(boolean isNavigationToClusterSelection) {
        hideProgressDialog();
        Intent intent = new Intent(context, LandingActivity.class);
        boolean isSingleCompany = true;
        if (!isSingleCompany) {
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.COMP_NOT_SELECTED, true);
            intent = new Intent(context, CompSelectActivity.class);
//                              DataHandler.newInstance().setCompanyDatumList(companyDatumList);
        } else {
            if (isNavigationToClusterSelection) {
                intent = new Intent(context, ClusterSelectActivityActivity.class);
            }
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.COMP_NOT_SELECTED, false);
        }
        ActivityOptions options = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
        }
        //intent.putExtra("activity","login");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options.toBundle());
            finish();
            finishAffinity();
        } else {
            startActivity(intent);
            finish();
            finishAffinity();
        }

    }

    private void getCompParams(Post post) {
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String type = "gps_accuracy";
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        Log.e(TAG, "Type " + type + "   CompId " + compId + "  UserId " + userId + "   Token " + token);
        ApiInterface expApiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        Call<CompParamResponse> compRegCall = expApiInterface.getCompParam(compId, type, userId, token);
        compRegCall.enqueue(new Callback<CompParamResponse>() {
            @Override
            public void onResponse(Call<CompParamResponse> call, Response<CompParamResponse> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "CompParam Res " + new Gson().toJson(response.body()));
                    List<CompParamDatum> data = response.body().getData();
                    Float f = 2.0f;
                    if (data != null && data.size() > 0) {
                        CompParamDatum datum = data.get(0);
                        String accuracy = datum.getValue();

                        if (AppConstants.isValidString(accuracy)) {
                            try {
                                f = Float.valueOf(accuracy);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        SharedPreferencesMethod.setFloatPref(context, SharedPreferencesMethod.COMP_AUTO_ACCURACY, f);
                    } else {
                        SharedPreferencesMethod.setFloatPref(context, SharedPreferencesMethod.COMP_AUTO_ACCURACY, f);
                    }

                } else {
                    try {
                        Log.e(TAG, "CompReg Error Code=> " + response.code() + "  Error=> " + response.errorBody().string());
                        SharedPreferencesMethod.setFloatPref(context, SharedPreferencesMethod.COMP_AUTO_ACCURACY, 2.0f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<CompParamResponse> call, Throwable t) {
                Log.e(TAG, "CompParam Fail " + t.toString());
                SharedPreferencesMethod.setFloatPref(context, SharedPreferencesMethod.COMP_AUTO_ACCURACY, 2.0f);

            }
        });
    }

    CheckUserResponse userResponse;
    boolean isRegistered = false;

    private void checkUser() {
        isRegistered = false;
        showProgressDialog();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Log.e(TAG, "Mobile Number " + etMobile.getText().toString().trim());
        final boolean[] isLoaded = {false};

        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getCompanyList(etMobile.getText().toString().trim(), "", "", AppConstants.COMP_ID).enqueue(new Callback<CompanyResponse>() {
            @Override
            public void onResponse(Call<CompanyResponse> call, Response<CompanyResponse> response) {
                String error = null;
                isLoaded[0] = true;
                if (response.isSuccessful()) {
                    Log.e(TAG, "CompResponse " + new Gson().toJson(response.body()));

                    if (response.body().getStatus() == OLD_VERSION_STATUS) {
                        Toasty.error(RegisterActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                    } else if (response.body().getStatus() == 1) {

                        if (response.body().getCompanyDatumList() == null || response.body().getCompanyDatumList().size() == 0) {
                            sendOtp();
                        } else {

                            for (int i = 0; i < response.body().getCompanyDatumList().size(); i++) {
                                CompanyDatum companyDatum = response.body().getCompanyDatumList().get(i);
                                try {
                                    if (AppConstants.isValidString(companyDatum.getCompId()) &&
                                            companyDatum.getCompId().equals(AppConstants.COMP_ID)) {
//                                        mobileLogin(true);
                                        isRegistered = true;
                                        Log.e(TAG,"MATCH AT "+i+" "+new Gson().toJson(companyDatum));
                                        break;
                                    }else
                                        Log.e(TAG,"NOT MATCH AT "+i+" "+new Gson().toJson(companyDatum));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (isRegistered) {
                                mobileLogin(true);
                            } else {
                                sendOtp();
                            }
                        }

                    } else {
                        userResponse = null;
                        sendOtp();
                    }
                    hideProgressDialog();


                } else if (response.code() == AppConstants.API_STATUS.API_NOT_FOUND) {
                    //un authorized access
                    Toasty.error(context, "This number is not registered with us or account is deactivated", Toast.LENGTH_LONG).show();

                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    hideProgressDialog();
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    hideProgressDialog();
                } else {
                    hideProgressDialog();
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "MOBILE COMP Err " + error + ", code:" + response.code());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(RegisterActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<CompanyResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                isLoaded[0] = true;
                long diff = newMillis - currMillis;
                notifyApiDelay(RegisterActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                hideProgressDialog();
                Log.e(TAG, "Login Failure " + t.toString());
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                internetFlow(isLoaded[0]);
            }
        }, AppConstants.DELAY);
    }


    //Firebase Auth

    String uid = "";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private FirebaseAuth mAuth;
    private String phoneVerificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Toast.makeText(context, "Authenticated", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            uid = user.getUid();
                            etOtp.setText(credential.getSmsCode());
                            Log.e("CREDENTIAL", credential.getSmsCode() + "\n" + credential.getProvider() + "\n" + credential.getSignInMethod());
                            Log.e("USER", user.getDisplayName() + "\n" + user.getPhoneNumber() + "\n" + user.getUid());
                            isFirebaseAuth=true;
                            if (isRegistered)
                                mobileLogin(true);
                            else
                                verifyPDBMAuth();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                etOtp.setError(resources.getString(R.string.invalid_code_message_label));
                                hideProgressDialog();
                            }
                        }
                    }
                });
    }



    boolean isFirebaseAuth = false;

    private void setUpVerificatonCallbacks() {
        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(
                    PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.d(TAG, "Invalid credential: " + e.getLocalizedMessage());
                    etMobile.setError(resources.getString(R.string.invalid_number_message_label));
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // SMS quota exceeded
                    Log.d(TAG, "SMS Quota exceeded.");
                    etMobile.setError(resources.getString(R.string.message_quota_finished_message_label));
                } else if (e instanceof FirebaseApiNotAvailableException) {
                    // SMS quota exceeded
//                    Log.d(TAG, "SMS Quota exceeded.");
//                    etMobile.setError(resources.getString(R.string.message_quota_finished_message_label));
                    callPDBMAuth();
                } else if (e instanceof FirebaseAuthException) {
                    // SMS quota exceeded
//                    Log.d(TAG, "SMS Quota exceeded.");
//                    etMobile.setError(resources.getString(R.string.message_quota_finished_message_label));
                    callPDBMAuth();
                }


            }

            @Override
            public void onCodeSent(String verificationId,
                                   final PhoneAuthProvider.ForceResendingToken token) {
                phoneVerificationId = verificationId;
                resendToken = token;
                Log.e("phoneVerif and token", phoneVerificationId + " " + token);
                tiOtp.setVisibility(View.VISIBLE);
                etOtp.setVisibility(View.VISIBLE);
                getOtpButton.setVisibility(View.GONE);
                isFirebaseAuth = true;
                etUserName.setFocusable(false);
                etUserName.setFocusable(false);
                etFatherName.setFocusable(false);
                countryCodePicker.setFocusable(false);
                countryCodePicker.setEnabled(false);
                countryCodePicker.setClickable(false);

//                resendTvButton.setVisibility(View.VISIBLE);
                getOtpButton.setVisibility(View.GONE);
                verifyOtpButton.setVisibility(View.VISIBLE);
                verifyOtpButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        verifyCode();
                    }
                });
                /*resendTvButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String number = countryCodePicker.getSelectedCountryCodeWithPlus() + etMobile.getText().toString().trim();
                        resendVerificationCode(number, resendToken);
                    }
                });*/

            }
        };
    }

    public void verifyCode() {

        showProgressDialog();
        String codee = etOtp.getText().toString();

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(phoneVerificationId, codee);

        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
        showProgressDialog();
        Toast.makeText(context, resources.getString(R.string.please_wait_msg), Toast.LENGTH_LONG).show();
    }

    private void startPhoneNumberVerification(String number) {
        Log.e(TAG, "Mobile " + number);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks);        // OnVerificationStateChangedCallbacks

    }

}