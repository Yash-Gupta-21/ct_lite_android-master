package com.i9930.croptrails.Login;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.listener.StateUpdatedListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.AESUtils2;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.BuildConfig;
import com.i9930.croptrails.ClusterSelection.ClusterSelectActivityActivity;
import com.i9930.croptrails.CommonClasses.DDResponseNew;
import com.i9930.croptrails.CommonClasses.ParamData;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.CompSelect.Adapter.CompanyRvAdapter;
import com.i9930.croptrails.CompSelect.CompSelectActivity;
import com.i9930.croptrails.CompSelect.Model.CompanyDatum;
import com.i9930.croptrails.CompSelect.Model.CompanyResponse;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.ForgotPassword.ForgotPasswordActivity;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Login.Model.CheckUserResponse;
import com.i9930.croptrails.Login.Model.CompParamDatum;
import com.i9930.croptrails.Login.Model.CompParamResponse;
import com.i9930.croptrails.Login.Model.LoginData;
import com.i9930.croptrails.Login.Model.Post;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Register.RegisterActivity;
import com.i9930.croptrails.RoomDatabase.AppDatabase;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegCacheManager;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegModel;
import com.i9930.croptrails.RoomDatabase.CompRegistry.DatabaseResInterface;
import com.i9930.croptrails.RoomDatabase.CompRegistry.Model.CompRegResult;
import com.i9930.croptrails.RoomDatabase.CompRegistry.Model.ResponseCompReg;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownCacheManager;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmCacheManager;
import com.i9930.croptrails.RoomDatabase.Gps.GpsCacheManager;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestCacheManager;
import com.i9930.croptrails.RoomDatabase.Timeline.TimelineCacheManager;
import com.i9930.croptrails.RoomDatabase.Visit.VrCacheManager;
import com.i9930.croptrails.RoomDatabase.VisitStructure.VisitCacheManager;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.LocationInsertReciever;
import com.i9930.croptrails.Utility.LocationUploadReceiver;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;
import com.i9930.croptrails.UtilityFP.BiometricCallback;
import com.i9930.croptrails.UtilityFP.BiometricManager;
import com.i9930.croptrails.UtilityFP.BiometricUtils;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;
import static com.i9930.croptrails.Utility.AppConstants.getCountryId;

public class LoginActivity extends AppCompatActivity implements BiometricCallback, StateUpdatedListener<InstallState>, DatabaseResInterface {
    String TAG = "LoginActivity";
    Resources resources;
    Context context;
    @BindView(R.id.forward_img)
    ImageView forward_img;
    @BindView(R.id.et_email)
    TextInputEditText et_email;
    @BindView(R.id.ti_email)
    TextInputLayout ti_email;
    AppUpdateManager appUpdateManager;
    @BindView(R.id.et_password)
    TextInputEditText et_password;
    @BindView(R.id.ti_password)
    TextInputLayout ti_password;

    @BindView(R.id.et_otp)
    TextInputEditText et_otp;
    @BindView(R.id.ti_otp)
    TextInputLayout ti_otp;
    private Boolean exit = false;
    ViewFailDialog viewFailDialog;
    ConnectivityManager connectivityManager;
    @BindView(R.id.progressBar)
    GifImageView progressBar;
    @BindView(R.id.logo9930i_icon)
    ImageView logo9930iicon;
    Boolean locationpermission = false;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private FirebaseAuth mAuth;
    private String phoneVerificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    @BindView(R.id.loginTvButton)
    TextView loginTvButton;
    public ProgressDialog mProgressDialog;
    String role = "";
    FusedLocationProviderClient mFusedLocationClient;
    String gps_cordinates = "";
    CheckUserResponse userResponse;
    @BindView(R.id.resendTvButton)
    TextView resendTvButton;
    CompRegCacheManager compRegCacheManager;
    @BindView(R.id.getOtpButton)
    TextView getOtpButton;
    @BindView(R.id.countryCodePickerLogin)
    CountryCodePicker countryCodePicker;
    @BindView(R.id.loginParentRelLay)
    RelativeLayout loginParentRelLay;
    private final int UPDATE_REQ_CODE = 404;
    @BindView(R.id.img_fingerprint)
    ImageView img_fingerprint;

    @BindView(R.id.fingerMessageLay)
    LinearLayout fingerMessageLay;
    @BindView(R.id.termAndConditionTv)
    TextView termAndConditionTv;

    List<CompanyDatum> companyDatumList = new ArrayList<>();
    String osVersion;
    String internetSPeed = "";
    @BindView(R.id.connectivityLine)
    View connectivityLine;

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
                            ConnectivityUtils.checkSpeedWithColor(LoginActivity.this, new ConnectivityUtils.ColorListener() {
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

    @BindView(R.id.forgotPasswordTv)
    TextView forgotPasswordTv;

    @BindView(R.id.register)
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String languageToLoad = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        context = this;
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        //Clear all Cache data
//        SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_FINGER_SELECTED, false);
//        SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_FINGER_DONT_ASK, false);
        GpsCacheManager.getInstance(context).deleteAllGps(null);
        SharedPreferencesMethod.clear(this);
        CompRegCacheManager.getInstance(this).deleteAllData();
        FarmCacheManager.getInstance(context).deleteAllFarms();
        TimelineCacheManager.getInstance(context).deleteAllTimeline();
        HarvestCacheManager.getInstance(context).deleteAllHarvests();
        VrCacheManager.getInstance(context).deleteAllVisits();
        VisitCacheManager.getInstance(context).deleteAllVisitStructure();
        viewFailDialog = new ViewFailDialog();
        compRegCacheManager = CompRegCacheManager.getInstance(context);
        String preference = getCountryId(context);
        if (preference != null) {
            countryCodePicker.setCountryPreference(preference.toUpperCase());
        }

        AppConstants.setDefaultUnit(context);
//        termAndConditionTv.setVisibility(View.GONE);
        String first = "<font color='#ffffff'>I have read and agree to the</font>";
        String next = "<font color='#2278d4'><b><u>term and conditions<u></b></font>";
        termAndConditionTv.setText(Html.fromHtml(first + " " + next));

        termAndConditionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://croptrails.farm/web/html/terms-condition.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, RegisterActivity.class));
            }
        });
        checkpermission();
        loadAnimation();
        onClick();
        GetGpsLocationinBackAsync async = new GetGpsLocationinBackAsync();
        async.execute();


        if (BiometricUtils.isBiometricPromptEnabled() && BiometricUtils.isPermissionGranted(context) &&
                BiometricUtils.isFingerprintAvailable(context) && BiometricUtils.isHardwareSupported(context) &&
                SharedPreferencesMethod2.getBoolean(context, SharedPreferencesMethod2.IS_ALLOW_FINGER)
        ) {
            showFingerPrintDialog();
            /*img_fingerprint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFingerPrintDialog();
                }
            });*/
//            fingerMessageLay.setVisibility(View.VISIBLE);
            initializeBottomSheet();
//            fingerprint_msg.setVisibility(View.VISIBLE);
//            fingerprint_msg.setVisibility(View.GONE);
//            fingerprint_msg.setText(getString(R.string.or_fingerprint_login));
        } else {
            //img_fingerprint.setVisibility(View.GONE);
//            fingerprint_msg.setVisibility(View.GONE);
        }

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

    }

    BiometricManager mBiometricManager;

    private void showFingerPrintDialog() {
        mBiometricManager = new BiometricManager.BiometricBuilder(LoginActivity.this)
                .setTitle(getString(R.string.fingerprint_login_label))
                .setSubtitle(getString(R.string.biometric_subtitle))
                .setDescription(getString(R.string.biometric_description))
                .setNegativeButtonText(getString(R.string.biometric_negative_button_text))
                .build();
        mBiometricManager.authenticate(LoginActivity.this);
    }

    private void loadAnimation() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.myanimation);
        logo9930iicon.startAnimation(animation);
    }



    private void onClick() {
        forgotPasswordTv.setText(resources.getString(R.string.forgot_password)+"?");
        forgotPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ForgotPasswordActivity.class));
            }
        });

        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 0) {
                    userResponse = null;
                    ti_otp.setVisibility(View.GONE);
                    ti_password.setVisibility(View.GONE);
                    getOtpButton.setVisibility(View.GONE);
                    loginTvButton.setVisibility(View.GONE);
                    findViewById(R.id.countryCodePickerLogin).setVisibility(View.GONE);
                } else if (!isDigitOnly(charSequence.toString())) {
                    userResponse = null;
                    ti_password.setVisibility(View.VISIBLE);
                    loginTvButton.setVisibility(View.VISIBLE);
                    getOtpButton.setVisibility(View.GONE);
                    findViewById(R.id.countryCodePickerLogin).setVisibility(View.GONE);
                } else if (/*charSequence.toString().length() == 10 &&*/ isDigitOnly(charSequence.toString())) {
                    getOtpButton.setVisibility(View.VISIBLE);
                    ti_password.setVisibility(View.GONE);
                    loginTvButton.setVisibility(View.GONE);
                    findViewById(R.id.countryCodePickerLogin).setVisibility(View.VISIBLE);
                    getOtpButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (charSequence.toString().length() > 4) {
                                checkUser();
//                                userResponse = new CheckUserResponse();
//                                userResponse.setStatus(1);
//                                getOtpButton.setVisibility(View.GONE);
//                                String number = countryCodePicker.getSelectedCountryCodeWithPlus() + et_email.getText().toString().trim();
//                                setUpVerificatonCallbacks();
//                                startPhoneNumberVerification(number);
                            } else
                                Toasty.error(context, resources.getString(R.string.invalid_number_please_check));
                        }
                    });
                } else {
                    getOtpButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        forward_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUser();
            }
        });

        forward_img.setVisibility(View.GONE);

        loginTvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userResponse != null) {
                    if (userResponse.getStatus() == 1) {
                        farmerLogin();
                    } else if (userResponse.getStatus() == 2 || userResponse.getStatus() == 4) {
                        mobileLogin(false,false,userResponse.getCompId());
                    } else if (userResponse.getStatus() == 3) {
                        Toasty.error(context, "Currently app login is allowed only for admin, supervisor and farmer", Toast.LENGTH_LONG).show();
                    } else {

                    }
                } else if (isDigitOnly(et_email.getText().toString().trim())) {
                    mobileLogin(false,false,null);
                } else {
                    superVisorLogin(false);
                }
            }
        });
        et_password.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                        || keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        // do nothing yet
                        // Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                    } else if (event.getAction() == KeyEvent.ACTION_UP) {
                        //Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();

                        if (isDigitOnly(et_email.getText().toString().trim())) {

                            mobileLogin(false,false,null);
                        } else {
                            superVisorLogin(false);
                        }
                    }
                    return true;
                } else {
                    // it is not an Enter key - let others handle the event
                    return false;
                }
            }
        });
        et_otp.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                        || keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    } else if (event.getAction() == KeyEvent.ACTION_UP) {
                        if (userResponse != null) {
                            if (userResponse.getStatus() == 1) {
                                farmerLogin();
                            } else if (userResponse.getStatus() == 2 || userResponse.getStatus() == 4) {
                                mobileLogin(false,false,userResponse.getCompId());
                            } else {
                            }
                        }  else {
                            superVisorLogin(false);
                        }
                    }
                    return true;
                } else {
                    // it is not an Enter key - let others handle the event
                    return false;
                }
            }
        });

    }

    @Override
    public void onGetAllCompRegData(List<CompRegModel> compRegModelList) {

    }

    @Override
    public void onCompRegDataAdded() {

    }

    @Override
    public void onAllDataDeleted() {

    }

    @Override
    public void onDataNotAdded() {

    }

    @Override
    public void onAllDataNotDeleted() {

    }

    @Override
    public void onUpdateCompRegData() {

    }

    @Override
    public void onUpdateNotSuccess() {

    }

    public class GetGpsLocationinBackAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.e("Test_Location", location.getLatitude() + " " + location.getLongitude());
                                gps_cordinates = location.getLatitude() + "_" + location.getLongitude();
                                //Toast.makeText(TestLocationcheckActivity.this, location.getLatitude()+"   "+location.getLongitude(), Toast.LENGTH_SHORT).show();
                                // Logic to handle location object
                            }
                        }
                    });
            return null;
        }
    }

    public boolean isDigitOnly(String text) {

        boolean isDigit = false;
        if (text.matches("[0-9]+")) {
            isDigit = true;
        } else {
            isDigit = false;
        }
        return isDigit;
    }

    private boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            /*if(phone.length() < 6 || phone.length() > 13) {
                check = false;
            } else {
                check = true;
            }*/
            check = true;
        } else {
            check = false;
        }
        return check;
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
           /* Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_LONG).show();*/
            Toasty.info(this, "Press Back again to Exit.",
                    Toast.LENGTH_LONG, true).show();

            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                   /* GetGpsLocationinBackAsync getGpsLocationinBackAsync=new GetGpsLocationinBackAsync();
                    getGpsLocationinBackAsync.execute();*/

                    //Toast.makeText(context, "granteed", Toast.LENGTH_SHORT).show();

                } else {
                    //Toast.makeText(context, "not granted", Toast.LENGTH_SHORT).show();
                    //checkNewFunction();

                }
                return;
            }

            /*case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    GetGpsLocationinBackAsync getGpsLocationinBackAsync=new GetGpsLocationinBackAsync();
                    getGpsLocationinBackAsync.execute();

                    //Toast.makeText(context, "granteed", Toast.LENGTH_SHORT).show();

                } else {
                    //Toast.makeText(context, "not granted", Toast.LENGTH_SHORT).show();

                }
                return;
            }*/
        }

    }

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    private void checkpermission() {
        if (locationpermission == checkLocationPermission()) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }


    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Toast.makeText(context, "Authenticated", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            uid = user.getUid();
                            et_otp.setText(credential.getSmsCode());
                            Log.e("CREDENTIAL", credential.getSmsCode() + "\n" + credential.getProvider() + "\n" + credential.getSignInMethod());
                            Log.e("USER", user.getDisplayName() + "\n" + user.getPhoneNumber() + "\n" + user.getUid());
                            farmerLogin();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                et_otp.setError(resources.getString(R.string.invalid_code_message_label));
                                hideProgressDialog();
                            }
                        }
                    }
                });
    }

    String uid = "";

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
                    et_email.setError(resources.getString(R.string.invalid_number_message_label));
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // SMS quota exceeded
                    Log.d(TAG, "SMS Quota exceeded.");
                    et_email.setError(resources.getString(R.string.message_quota_finished_message_label));
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   final PhoneAuthProvider.ForceResendingToken token) {
                phoneVerificationId = verificationId;
                resendToken = token;
                Log.e("phoneVerif and token", phoneVerificationId + " " + token);
                et_otp.setVisibility(View.VISIBLE);
                ti_otp.setVisibility(View.VISIBLE);
                getOtpButton.setVisibility(View.GONE);
                resendTvButton.setVisibility(View.VISIBLE);
                et_email.setClickable(false);
                et_email.setEnabled(false);
                loginTvButton.setVisibility(View.VISIBLE);
                loginTvButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        verifyCode();
                    }
                });
                resendTvButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String number = countryCodePicker.getSelectedCountryCodeWithPlus() + et_email.getText().toString().trim();
                        resendVerificationCode(number, resendToken);
                    }
                });

            }
        };
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


    @Override
    protected void onPause() {
        super.onPause();
        hideProgressDialog();

    }


    public void verifyCode() {

        showProgressDialog();
        String codee = et_otp.getText().toString();

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(phoneVerificationId, codee);

        signInWithPhoneAuthCredential(credential);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityRunning = false;
        if (bottomSheetDialog!=null)
            bottomSheetDialog.dismiss();
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

    private boolean isSingleCompany = true;
    final int OLD_VERSION_STATUS = 9;

    private void checkUser() {
        showProgressDialog();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Log.e(TAG, "Mobile Number " + et_email.getText().toString().trim());
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getCompanyList(et_email.getText().toString().trim(), "", "").enqueue(new Callback<CompanyResponse>() {
            @Override
            public void onResponse(Call<CompanyResponse> call, Response<CompanyResponse> response) {
                String error = null;
                isLoaded[0] = true;
                if (response.isSuccessful()) {
                    Log.e(TAG, "CompResponse " + new Gson().toJson(response.body()));

                    if (response.body().getStatus() == OLD_VERSION_STATUS) {
                        Toasty.error(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                    } else if (response.body().getStatus() == 1) {
                        if (response.body().getCompanyDatumList() == null) {
                            userResponse = null;
                            isSingleCompany = false;
                            Toasty.error(context, "This number is not registered with us or account is deactivated", Toast.LENGTH_LONG).show();
//                            ti_password.setVisibility(View.VISIBLE);
//                            loginTvButton.setVisibility(View.VISIBLE);
//                            getOtpButton.setVisibility(View.GONE);
//                            findViewById(R.id.countryCodePickerLogin).setVisibility(View.GONE);
                        } else if (response.body().getCompanyDatumList() != null && response.body().getCompanyDatumList().size() == 1) {
                            companyDatumList.clear();
                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.HAS_MULTI_COMP, false);
                            CompanyDatum companyDatum = response.body().getCompanyDatumList().get(0);
                            companyDatumList.add(companyDatum);
                            isSingleCompany = true;
                            if (companyDatum.getRoleId() != null && companyDatum.getRoleId().trim().equals("3")) {
                                //its farmer
                                userResponse = new CheckUserResponse();
                                userResponse.setStatus(1);
                                userResponse.setCompId(companyDatum.getCompId());
                                getOtpButton.setVisibility(View.GONE);
                                mobileLogin(false,true,companyDatum.getCompId());
//                                String number = countryCodePicker.getSelectedCountryCodeWithPlus() + et_email.getText().toString().trim();
//                                setUpVerificatonCallbacks();
//                                startPhoneNumberVerification(number);

                            } else if (companyDatum.getRoleId() != null && (companyDatum.getRoleId().trim().equals("1") || companyDatum.getRoleId().trim().equals("2"))) {
                                //Login with mob and pass
//                                userResponse = null;
                                userResponse = new CheckUserResponse();
                                userResponse.setStatus(2);
                                userResponse.setCompId(companyDatum.getCompId());
                                mobileLogin(false,true,companyDatum.getCompId());

                            } else {
                                userResponse = null;
                                Toasty.error(context, "Sorry, currently app login is allowed only for admin,supervisor and farmer", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            isSingleCompany = false;
                            int allFarmer = 0;
                            int allSupevisor = 0;//or admin
                            int allOther = 0;
                            companyDatumList.clear();
                            companyDatumList.addAll(response.body().getCompanyDatumList());
                            for (int i = 0; i < response.body().getCompanyDatumList().size(); i++) {
                                CompanyDatum companyDatum = response.body().getCompanyDatumList().get(i);
                                try {
                                    if (companyDatum.getRoleId().trim().equals("1") || companyDatum.getRoleId().trim().equals("2")) {
                                        allSupevisor++;
                                    } else if (companyDatum.getRoleId().trim().equals("3")) {
                                        allFarmer++;
                                    } else {
                                        allOther++;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (response.body().getCompanyDatumList().size() == allSupevisor) {
                                //Login with mob and pass
                                userResponse = new CheckUserResponse();
                                userResponse.setStatus(2);
//                                mobileLogin(false,true);
                                initializeCompListDialog(companyDatumList);
//                                ti_password.setVisibility(View.VISIBLE);
//                                loginTvButton.setVisibility(View.VISIBLE);
//                                getOtpButton.setVisibility(View.GONE);
//                                findViewById(R.id.countryCodePickerLogin).setVisibility(View.GONE);
                            } else if (response.body().getCompanyDatumList().size() == allFarmer) {
                                userResponse = new CheckUserResponse();
                                userResponse.setStatus(1);
//                                getOtpButton.setVisibility(View.GONE);
//                                String number = countryCodePicker.getSelectedCountryCodeWithPlus() + et_email.getText().toString().trim();
//                                setUpVerificatonCallbacks();
//                                startPhoneNumberVerification(number);
//                                mobileLogin(false,true);
                                initializeCompListDialog(companyDatumList);
                            } else if (response.body().getCompanyDatumList().size() == allOther) {
                                userResponse = new CheckUserResponse();
                                userResponse.setStatus(1);
                                Toasty.error(context, "Sorry, currently app login is allowed only for admin,supervisor and farmer", Toast.LENGTH_LONG).show();
                            } else if (response.body().getCompanyDatumList().size() == (allSupevisor + allOther)) {
                                //Login with mob and pass
                                userResponse = null;
                                ti_password.setVisibility(View.VISIBLE);
                                loginTvButton.setVisibility(View.VISIBLE);
                                getOtpButton.setVisibility(View.GONE);
                                findViewById(R.id.countryCodePickerLogin).setVisibility(View.GONE);
                            } else {

                                userResponse = new CheckUserResponse();
                                userResponse.setStatus(2);
                                getOtpButton.setVisibility(View.GONE);
//                                String number = countryCodePicker.getSelectedCountryCodeWithPlus() + et_email.getText().toString().trim();
//                                setUpVerificatonCallbacks();
//                                startPhoneNumberVerification(number);
//                                mobileLogin(false,true);
                                initializeCompListDialog(companyDatumList);
                            }
                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.HAS_MULTI_COMP, true);
                        }
                    } else {
                        userResponse = null;
//                        ti_password.setVisibility(View.VISIBLE);
//                        loginTvButton.setVisibility(View.VISIBLE);
//                        getOtpButton.setVisibility(View.GONE);
//                        findViewById(R.id.countryCodePickerLogin).setVisibility(View.GONE);
                        Toasty.error(context, "This number is not registered with us or account is deactivated", Toast.LENGTH_LONG).show();
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
                    notifyApiDelay(LoginActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<CompanyResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                isLoaded[0] = true;
                long diff = newMillis - currMillis;
                notifyApiDelay(LoginActivity.this, call.request().url().toString(),
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

    boolean isFingerPrintLogin = false;
    String number;
    String uname, pass;

    private void farmerLogin() {
        showProgressDialog();
        //Toast.makeText(context, "Farmer Login success", Toast.LENGTH_LONG).show();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        String appVersion = BuildConfig.VERSION_NAME;
        String mob = "";
        if (isFingerPrintLogin && number != null) {
            mob = number.trim();
        } else {
            mob = et_email.getText().toString().trim();
        }
        String finalMob = mob;
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        String mac=AppConstants.getMacAddr();
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("mob",mob);
        jsonObject.addProperty("mac",mac);
        jsonObject.addProperty("app_version",appVersion);
        jsonObject.addProperty("android_version",""+osVersion);
        jsonObject.addProperty("authFireBase",true);
        if (userResponse!=null&&AppConstants.isValidString(userResponse.getCompId()))
        jsonObject.addProperty("comp_id",userResponse.getCompId());
        jsonObject.addProperty("otp",et_otp.getText().toString().trim());
        Log.e(TAG,"farmerLogin REQ "+new Gson().toJson(jsonObject)+"\n"+new Gson().toJson(userResponse));

        apiInterface.farmerLogin(jsonObject).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                String error = null;
                isLoaded[0] = true;
                if (response.isSuccessful()) {
                    Log.e(TAG, "Farmer Login Res " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == OLD_VERSION_STATUS) {
                        Toasty.error(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                    } else if (response.body().getStatus() == 1) {
                        try {
                            SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.UID, AESUtils2.encrypt(uid));
                            SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.USER_NUMBER, AESUtils2.encrypt(finalMob));
                            SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE, AppConstants.ROLES.FARMER);
                            SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.UNAME, response.body().getData().getName());
                            SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.IMAGE, response.body().getData().getImgLink());
                            String oldId = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.USER_ID);
                            String newId = AESUtils2.encrypt(response.body().getData().getUserId());
                            boolean isPreviousAllowed = SharedPreferencesMethod2.getBoolean(context, SharedPreferencesMethod2.IS_FINGER_SELECTED);
                            if (oldId != null && !newId.equals(oldId) && !isPreviousAllowed && !isFingerPrintLogin) {
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
                        //sandeep 2/8/19 comp reg
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.AUTHORIZATION, response.body().getJwToken());
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
//                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.ASSIGN_CALENDAR, false);
//                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.ADD_HC, false);
//                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.SEED_ALLOWED, false);
//                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.HARVEST_ALLOWED, false);
//                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED, false);
//                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.VISIT_ALLOWED, false);
//                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.EXPENSE__ALLOWED, false);
//                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.SUPERVISOR_ALLOWED_ADD_FARM, false);
//                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.FARMER_ALLOWED_ADD_FARM, false);
//                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.PLD_ALLOWED, false);
//                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.GERMINATION_ALLOWED, false);
//                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.FARMER_ALLOWED_ADD_ACTIVITY, false);
//                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.FARMER_ALLOWED_ADD_INPUT_COST, false);
//                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.FARMER_ALLOWED_UPDATE_FARM_DETAILS, false);
                        LoginData loginData = response.body().getData();
                        if (loginData.getCompName() != null && !TextUtils.isEmpty(loginData.getCompName())) {
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMPANY_NAME, loginData.getCompName());
                        }
                        if (loginData.getCompLogo() != null && !TextUtils.isEmpty(loginData.getCompLogo())) {
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_IMAGE, loginData.getCompLogo());
                        }
                        if (loginData.getCompBanner() != null && !TextUtils.isEmpty(loginData.getCompBanner())) {
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_BANNER, loginData.getCompBanner());
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
                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.LOGIN, true);
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOGIN_TYPE, AppConstants.ROLES.FARMER);
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_ID, response.body().getData().getCompId());
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.PERSON_ID, response.body().getData().getPersonId().toString());
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_ID, response.body().getData().getUserId().toString());
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVCLUSTERID, response.body().getData().getClusterId().toString());
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_NAME, response.body().getData().getName());
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.TOKEN, response.body().getToken());
                        storeCompRegData(response.body(), false);

                    } else if (response.body().getStatus() == 5) {
                        hideProgressDialog();
                        viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.alert_title_label), resources.getString(R.string.account_deactivated_contact_admin_msg));
                    } else if (response.body().getStatus() == 2) {
                        hideProgressDialog();
                        viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.alert_title_label), response.body().getMsg());
                    } else {
                        hideProgressDialog();

                        viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.failed_to_login));
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_NOT_FOUND) {
                    viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.alert_title_label), resources.getString(R.string.account_deactivated_contact_admin_msg));
                } else if (response.code() == AppConstants.API_STATUS.API_NOT_IMPLEMENTED) {
                    viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.alert_title_label), resources.getString(R.string.account_deactivated_contact_admin_msg));
                } else {
                    try {
                        hideProgressDialog();
                        error = response.errorBody().string().toString();
                        viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.failed_to_login));
                        Log.e(TAG, "Farmer Login Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(LoginActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                isLoaded[0] = true;
                long diff = newMillis - currMillis;
                notifyApiDelay(LoginActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                hideProgressDialog();
                viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.failed_to_login));
                Log.e(TAG, "Farmer Login Failure " + t.toString());
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

    private void superVisorLogin(boolean flag) {
        showProgressDialog();
        String user_name = "";
        String password = "";
        if (isFingerPrintLogin && uname != null && pass != null) {
            user_name = uname.trim();
            password = pass.trim();
        } else {
            if (userResponse != null && userResponse.getUsername() != null) {
                user_name = userResponse.getUsername();
            } else {
                user_name = et_email.getText().toString();
            }
            password = et_password.getText().toString().trim();
        }
        if (TextUtils.isEmpty(password)) {
            Toasty.error(context, resources.getString(R.string.please_enter_password_msg), Toast.LENGTH_LONG, false).show();
            hideProgressDialog();
            return;
        }
        String appVersion = BuildConfig.VERSION_NAME;
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        String finalUser_name = user_name;
        String finalPassword = password;
        Log.e(TAG, user_name + "   " + password);
//        Log.e(TAG,"username "+user_name+" pass "+password);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiService.superVisorLogin(user_name, password, gps_cordinates, "", appVersion, osVersion, flag).enqueue(new Callback<Post>() {

            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Log.e(TAG, "Supervisor Login Code " + response.code());
                String error = null;
                isLoaded[0] = true;
                if (response.isSuccessful()) {
                    String z = response.body().toString();
                    Post post = response.body();
                    Log.e(TAG, "Supervisor Login Res " + new Gson().toJson(post));
                    if (response.body().getStatus() == 2300) {
                        String msg = response.body().getMessage();
                        if (!AppConstants.isValidString(msg))
                            msg = response.body().getMsg();
                        showDataWipeAlert(msg);
                    } else if (response.body().getStatus() == OLD_VERSION_STATUS) {
                        Toasty.error(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                    } else if (post.getStatus() == 1 || post.getStatus() == 3) {
                        //sandeep 2/8/19 comp reg
                        showProDialog(post, finalUser_name, finalPassword);

                    } else if (post.getStatus() == 2) {
                        hideProgressDialog();
                        //Toasty.error(context, "Currently app login is allowed only for supervisor and farmer", Toast.LENGTH_LONG).show();
                        Toasty.error(context, response.body().getMsg(), Toast.LENGTH_LONG).show();
                    } else if (response.body().getStatus() == 5) {
                        hideProgressDialog();
                        progressBar.setVisibility(View.INVISIBLE);
                        /*viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.alert_title_label),
                                "Your account has been deactivated, Please contact your admin to activate account.");*/
//                        viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.alert_title_label), resources.getString(R.string.account_deactivated_contact_admin_msg));

                        viewFailDialog.showDialog(LoginActivity.this, response.body().getMsg());
                    } else {
                        hideProgressDialog();
                        progressBar.setVisibility(View.INVISIBLE);
                        //Toast.makeText(context, post.getMsg(), Toast.LENGTH_SHORT).show();
//                        viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.invalid_username_password));
                        viewFailDialog.showDialog(LoginActivity.this, response.body().getMsg());
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_NOT_FOUND) {
                    viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.alert_title_label), resources.getString(R.string.account_deactivated_contact_admin_msg));
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        Log.e("Error", error);
                        StatusMsgModel data = new Gson().fromJson(error, StatusMsgModel.class);
                        hideProgressDialog();
                        Toasty.error(context, data.getMessage(), Toast.LENGTH_SHORT, false).show();

                        //Toast.makeText(context, "Oops something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.failed_to_login));
                    }
                }
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(LoginActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                isLoaded[0] = true;
                notifyApiDelay(LoginActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                hideProgressDialog();
                progressBar.setVisibility(View.INVISIBLE);
                Log.e("Login", t.toString());
                viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.failed_to_login));
                //Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
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

    private void mobileLogin(boolean flag, boolean withMac,String compId) {
        showProgressDialog();
        String user_name = "";
        String password = "";
        user_name = et_email.getText().toString();
        password = et_password.getText().toString().trim();

        if (withMac)
            password = AppConstants.getMacAddr();

        if (TextUtils.isEmpty(password))
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
//        Log.e(TAG,"username "+user_name+" pass "+password);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();

        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("mob",user_name);
        jsonObject.addProperty("password",password);
        jsonObject.addProperty("gps",gps_cordinates);
        jsonObject.addProperty("ip","");
        jsonObject.addProperty("app_version",appVersion);
        jsonObject.addProperty("android_version",osVersion);
        jsonObject.addProperty("forced_online_mode",flag);
        if (AppConstants.isValidString(compId))
        jsonObject.addProperty("comp_id",compId);


        apiService.mobileLogin(jsonObject).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                String error = null;
                isLoaded[0] = true;
                if (response.isSuccessful()) {
                    Post post = response.body();
                    Log.e(TAG, "Supervisor Login Res mob " + new Gson().toJson(post));
                    if (response.body().getStatus() == 2300) {
                        String msg = response.body().getMessage();
                        if (!AppConstants.isValidString(msg))
                            msg = response.body().getMsg();
                        showDataWipeAlert(msg);
                    } else if (response.body().getStatus() == OLD_VERSION_STATUS) {
                        Toasty.error(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                    } else if (post.getStatus() == 1 || post.getStatus() == 3) {
                        //sandeep 2/8/19 comp reg
                        showProDialog(post, finalUser_name, finalPassword);
                    } else if (post.getStatus() == 2) {
                        hideProgressDialog();
                        //Toasty.error(context, "Currently app login is allowed only for supervisor and farmer", Toast.LENGTH_LONG).show();
                        Toasty.error(context, response.body().getMsg(), Toast.LENGTH_LONG).show();
                    } else if (response.body().getStatus() == 5) {
                        hideProgressDialog();
                        progressBar.setVisibility(View.INVISIBLE);
                        viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.alert_title_label), resources.getString(R.string.account_deactivated_contact_admin_msg));

                    } else {
                        if (withMac) {
                            if (userResponse.getStatus()==1) {
                                String number = countryCodePicker.getSelectedCountryCodeWithPlus() + et_email.getText().toString().trim();
                                setUpVerificatonCallbacks();
                                startPhoneNumberVerification(number);
                            }else if (userResponse.getStatus()==2||userResponse.getStatus()==4){
                                ti_password.setVisibility(View.VISIBLE);
                                loginTvButton.setVisibility(View.VISIBLE);
                                getOtpButton.setVisibility(View.GONE);
                                findViewById(R.id.countryCodePickerLogin).setVisibility(View.GONE);
                            }
                        }else {
                            hideProgressDialog();
                            progressBar.setVisibility(View.INVISIBLE);
                            //Toast.makeText(context, post.getMsg(), Toast.LENGTH_SHORT).show();
                            viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.invalid_username_password));
                        }
                    }
                    // Toast.makeText(LoginActivity.this, z, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        hideProgressDialog();
                        try {
                            StatusMsgModel data = new Gson().fromJson(error, StatusMsgModel.class);
                            if (data!=null&&data.getStatus()==4&&withMac){
                                if (userResponse.getStatus()==1) {
                                    String number = countryCodePicker.getSelectedCountryCodeWithPlus() + et_email.getText().toString().trim();
                                    setUpVerificatonCallbacks();
                                    startPhoneNumberVerification(number);
                                }else if (userResponse.getStatus()==2||userResponse.getStatus()==4){
                                    ti_password.setVisibility(View.VISIBLE);
                                    loginTvButton.setVisibility(View.VISIBLE);
                                    getOtpButton.setVisibility(View.GONE);
                                    findViewById(R.id.countryCodePickerLogin).setVisibility(View.GONE);
                                }
                            }else {
                                viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.failed_to_login));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.failed_to_login));
                        }

                        Log.e(TAG,"mobileLogin Error "+ error);
                        //Toast.makeText(context, "Oops something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(LoginActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                isLoaded[0] = true;
                long diff = newMillis - currMillis;
                notifyApiDelay(LoginActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                hideProgressDialog();
                progressBar.setVisibility(View.INVISIBLE);
                Log.e("Login", t.toString());
                viewFailDialog.showDialog(LoginActivity.this, resources.getString(R.string.failed_to_login));
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

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    private void showDialog(Post post, String finalUser_name, String finalPasswordPost){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_pro_account_detect);
        Window window1 = dialog.getWindow();
        window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        Button useProButton=dialog.findViewById(R.id.useProButton);
        Button continueAppButton=dialog.findViewById(R.id.continueAppButton);

        useProButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uri="com.i9930.croptrails";
                boolean isAppInstalled = appInstalledOrNot(uri);
                if(isAppInstalled) {
                    Intent LaunchIntent = getPackageManager()
                            .getLaunchIntentForPackage(uri);
                    startActivity(LaunchIntent);
                    dialog.dismiss();
                    Log.i(TAG,"Application is already installed.");
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+uri)));
                    Log.i(TAG,"Application is not currently installed.");
                }

            }
        });


        continueAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                data(post,finalUser_name,finalPasswordPost);
            }
        });

        dialog.show();
    }

    private void showProDialog(Post post, String finalUser_name, String finalPasswordPost){
        if (post.getData()!=null&&AppConstants.isValidString(post.getData().getCompId())&&!post.getData().getCompId().equals(AppConstants.COMP_ID)){
            //Show pro account dialog
            showDialog(post,finalUser_name,finalPasswordPost);
        }else{
            data(post,finalUser_name,finalPasswordPost);
        }
    }

    private void data(Post post, String finalUser_name, String finalPassword) {
        boolean dontAllowLogin = false;
        boolean isNavigationToClusterSelection = false;
        String name = "";
        String role = post.getData().getRoleId();
        SharedPreferencesMethod.setString(context,SharedPreferencesMethod.COUNTRY_CODE,post.getData().getCountryCode());
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

//                DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST);
//                DropDownT clusters = new DropDownT(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST, new Gson().toJson(post.getClusterIdList()));
//                DropDownCacheManager.getInstance(context).addChemicalUnit(clusters);
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
//                DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST);
//                DropDownT clusters = new DropDownT(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST, new Gson().toJson(post.getClusterIdList()));
//                DropDownCacheManager.getInstance(context).addChemicalUnit(clusters);
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
//                DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST);
//                DropDownT clusters = new DropDownT(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST, new Gson().toJson(post.getClusterIdList()));
//                DropDownCacheManager.getInstance(context).addChemicalUnit(clusters);
            } else
                isNavigationToClusterSelection = false;
            Log.e(TAG, "Cluster Ids " + builder.toString());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVCLUSTERID, builder.toString());
        } else if (role.trim().equals("7")) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOGIN_TYPE, AppConstants.ROLES.CLUSTER_ADMIN);
            SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE, AppConstants.ROLES.CLUSTER_ADMIN);
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVCLUSTERID, post.getData().getClusterId());
        } else {
//            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOGIN_TYPE, AppConstants.ROLES.FARMER);
//            SharedPreferencesMethod2.setString(context, SharedPreferencesMethod2.ROLE,AppConstants.ROLES.FARMER);
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

                if (isFingerPrintLogin) {

                }
                boolean isPreviousAllowed = SharedPreferencesMethod2.getBoolean(context, SharedPreferencesMethod2.IS_FINGER_SELECTED);
                if (oldId != null && newId != null && !newId.equals(oldId) && !isFingerPrintLogin && !isPreviousAllowed) {
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

//        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.ASSIGN_CALENDAR, false);
//        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.ADD_HC, false);
//        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.SEED_ALLOWED, false);
//        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.HARVEST_ALLOWED, false);
//        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.INPUT_COST_ALLOWED, false);
//        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.VISIT_ALLOWED, false);
//        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.EXPENSE__ALLOWED, false);
//        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.SUPERVISOR_ALLOWED_ADD_FARM, false);
//        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.FARMER_ALLOWED_ADD_FARM, false);
//        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.PLD_ALLOWED, false);
//        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.GERMINATION_ALLOWED, false);
//        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.FARMER_ALLOWED_ADD_ACTIVITY, false);
//        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.FARMER_ALLOWED_ADD_INPUT_COST, false);
//        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.FARMER_ALLOWED_UPDATE_FARM_DETAILS, false);
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
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_ID, loginData.getCompId());
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
//        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOGIN_TYPE, AppConstants.ROLES.FARMER);
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
                        Toasty.error(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                    } else if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LoginActivity.this, response.body().getMsg());
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

    private void showCompImage(boolean isNavigationToClusterSelection) {
       /* hideProgressDialog();
        Intent intent = new Intent(context, LandingActivity.class);
        if (!isSingleCompany && companyDatumList != null && companyDatumList.size() > 1) {
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.COMP_NOT_SELECTED, true);
            intent = new Intent(context, CompSelectActivity.class);
        } else {
            if (isNavigationToClusterSelection) {
                intent = new Intent(context, ClusterSelectActivityActivity.class);
            }
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.COMP_NOT_SELECTED, false);
        }
        startActivity(intent);
        finish();
        finishAffinity();*/

        hideProgressDialog();
        Intent intent = new Intent(context, LandingActivity.class);
        if (isNavigationToClusterSelection) {
            intent = new Intent(context, ClusterSelectActivityActivity.class);
        }
        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.COMP_NOT_SELECTED, false);
        startActivity(intent);
        finish();
        finishAffinity();

    }


    InstallStateUpdatedListener listener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState installState) {
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                Log.d("InstallDownloded", "InstallStatus sucsses");
                notifyUser();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        checkUpdate();
    }

    private void checkUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(context);
        appUpdateManager.registerListener(listener);
        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            this,
                            UPDATE_REQ_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                    Log.e(TAG, "exe " + e.toString());
                    Log.e(TAG, "exe1 " + e.getLocalizedMessage());
                    Log.e(TAG, "exe2 " + e.getMessage());
                }

                // Toast.makeText(context, "Update available", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(context, "Update not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityRunning = true;
        try {
            appUpdateManager
                    .getAppUpdateInfo()
                    .addOnSuccessListener(
                            appUpdateInfo -> {
                                if (appUpdateInfo.updateAvailability()
                                        == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                    // If an in-app update is already running, resume the update.
                                    try {
                                        appUpdateManager.startUpdateFlowForResult(
                                                appUpdateInfo,
                                                AppUpdateType.IMMEDIATE,
                                                this,
                                                UPDATE_REQ_CODE);
                                    } catch (IntentSender.SendIntentException e) {
                                        e.printStackTrace();
                                    }
                                } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                                    notifyUser();
                                }
                            });
        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            appUpdateManager.unregisterListener(listener);
            if (mBiometricManager != null)
                mBiometricManager.cancelAuthentication();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQ_CODE) {
            try {
                if (resultCode != RESULT_OK) {
                    Log.e(TAG, "Update flow failed! Result code: " + resultCode);
                } else {
                    InstallStateUpdatedListener listener = state -> {
                        // Show module progress, log state, or install the update.

                    };

// Before starting an update, register a listener for updates.
                    appUpdateManager.registerListener(listener);

// Start an update.

// When status updates are no longer needed, unregister the listener.
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStateUpdate(InstallState state) {
        try {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                notifyUser();
            } else {
                // Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notifyUser() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.loginParentRelLay),
                        resources.getString(R.string.update_has_been_just_dowloaded_msg),
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(resources.getString(R.string.restart_label), view -> appUpdateManager.completeUpdate());
        snackbar.setActionTextColor(
                getResources().getColor(R.color.white));
        snackbar.show();
    }


    @Override
    public void onSdkVersionNotSupported() {
        //Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_sdk_not_supported), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationNotSupported() {
        // Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_hardware_not_supported), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {
        //Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_fingerprint_not_available), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {
        //Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_permission_not_granted), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {
        //Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFailed() {
//        Toast.makeText(getApplicationContext(), getString(R.string.biometric_failure), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationCancelled() {
        //Toast.makeText(getApplicationContext(), getString(R.string.biometric_cancelled), Toast.LENGTH_LONG).show();
        //mBiometricManager.cancelAuthentication();
    }

    @Override
    public void onAuthenticationSuccessful() {

        if (SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.ROLE) != null &&
                !TextUtils.isEmpty(SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.ROLE))) {
            if (SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.ROLE).trim().equals(AppConstants.ROLES.FARMER)) {
                try {
                    isFingerPrintLogin = true;
                    number = AESUtils2.decrypt(SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.USER_NUMBER));
                    if (number != null && !TextUtils.isEmpty(number))
                        farmerLogin();
                    else
                        Toasty.error(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_login_manually), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    isFingerPrintLogin = false;
                    Toasty.error(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_login_manually), Toast.LENGTH_LONG).show();
                }

            } else {
                try {
                    isFingerPrintLogin = true;
                    Log.e(TAG, "Uname " + SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.USERNAME));
                    Log.e(TAG, "Pass " + SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.PASSWORD));
                    uname = AESUtils2.decrypt(SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.USERNAME));
                    pass = AESUtils2.decrypt(SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.PASSWORD));
                    superVisorLogin(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    isFingerPrintLogin = false;
                    Toasty.error(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_login_manually), Toast.LENGTH_LONG).show();

                }
            }
            // Toast.makeText(getApplicationContext(), getString(R.string.biometric_success), Toast.LENGTH_LONG).show();
        } else {
            isFingerPrintLogin = false;
            Toasty.error(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_login_manually), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//        Toast.makeText(getApplicationContext(), helpString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
//        Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
    }

    private void showDataWipeAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        try {
            builder.setMessage(msg)
                    .setTitle(resources.getString(R.string.alert_title_label))
                    .setCancelable(false)
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if (userResponse != null) {
                                if (userResponse.getStatus()==1||userResponse.getStatus() == 2 || userResponse.getStatus() == 4) {
                                    mobileLogin(true,false,userResponse.getCompId());////////clear previous id and login forcefully
                                } else if (userResponse.getStatus() == 3) {
                                    Toasty.error(context, "Currently app login is allowed only for admin, supervisor and farmer", Toast.LENGTH_LONG).show();
                                } else {

                                }
                            } else if (isDigitOnly(et_email.getText().toString().trim())) {
                                mobileLogin(true,false,null);/////clear previous id and login forcefully
                            } else {
                                superVisorLogin(true);////////clear previous id and login forcefully
                            }

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


    //Fingerprint bottomsheet dialog
    BottomSheetDialog bottomSheetDialog;

    private void initializeBottomSheet(){
        String previousDetails=getPreviousLoginUserName();
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.fingerprint_bottomsheet);
        Window window = bottomSheetDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView fingerprintHeadTv=bottomSheetDialog.findViewById(R.id.fingerprintHeadTv);
        if (AppConstants.isValidString(previousDetails)) {
            String sourceString1 = resources.getString(R.string.fingerprint_login_to) + " " + "<b>" + previousDetails + "</b>?";
            fingerprintHeadTv.setText(Html.fromHtml(sourceString1));
            et_email.setText(previousDetails);
        }
        TextView cancelTv=bottomSheetDialog.findViewById(R.id.cancelTv);

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBiometricManager != null)
                    mBiometricManager.cancelAuthentication();
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.show();

    }

    private String getPreviousLoginUserName(){
        try {

            if (SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.ROLE).trim().equals(AppConstants.ROLES.FARMER)) {

                return AESUtils2.decrypt(SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.USER_NUMBER));

            } else {
                return AESUtils2.decrypt(SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.USERNAME));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    CompanyDatum selectedComp=null;
    private void initializeCompListDialog(List<CompanyDatum>companyDatumList) {

        Dialog compSelectDialog = new Dialog(context);
        compSelectDialog.setContentView(R.layout.dialog_comp_select);
        Window window = compSelectDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        RecyclerView recyclerViewCompList = compSelectDialog.findViewById(R.id.recyclerViewCompList);
        Button selectButton = compSelectDialog.findViewById(R.id.selectButton);
        ImageView close = compSelectDialog.findViewById(R.id.close);
        CompanyRvAdapter adapter = new CompanyRvAdapter(this, companyDatumList, new CompanyRvAdapter.CompanySelectListener() {
            @Override
            public void onCompanySelected(int index) {
                if (index == -1) {
                    selectedComp=null;
                    selectButton.setVisibility(View.GONE);
                } else if (index > -1) {
                    selectButton.setVisibility(View.VISIBLE);
                    selectedComp=companyDatumList.get(index);
                }
            }
        });


        recyclerViewCompList.setHasFixedSize(true);
        recyclerViewCompList.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewCompList.setAdapter(adapter);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compSelectDialog.dismiss();
            }
        });
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedComp!=null) {

                    compSelectDialog.dismiss();
                    userResponse = new CheckUserResponse();
                    userResponse.setCompId(selectedComp.getCompId());
                    if (AppConstants.isValidString(selectedComp.getRoleId())&&selectedComp.getRoleId().trim().equals("3"))
                    userResponse.setStatus(1);
                    else
                        userResponse.setStatus(2);
                    mobileLogin(false,true,selectedComp.getCompId());
                }else
                {
                    AppConstants.failToast(context, getString(R.string.please_select_a_company));
                }
            }
        });
        compSelectDialog.show();
    }

}
