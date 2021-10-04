package com.i9930.croptrails.Profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.Landing.Fragments.Model.ProfileReciveMainData;
import com.i9930.croptrails.Landing.Fragments.Model.ResultProfile;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Profile.Model.AddProfileSendData;
import com.i9930.croptrails.Profile.Model.ImageBody;
import com.i9930.croptrails.Profile.Model.ImageUpdateResponse;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;
import com.i9930.croptrails.Utility.Utility;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class ProfileActivity extends AppCompatActivity {

    String TAG = "ProfileActivity";
    @BindView(R.id.sv_user_name)
    TextView sv_user_name;
    @BindView(R.id.sv_address)
    TextView sv_address;
    @BindView(R.id.sv_name)
    TextView sv_name;
    @BindView(R.id.sv_fathers_name)
    TextView sv_fathers_name;
    @BindView(R.id.sv_dob)
    TextView sv_dob;
    @BindView(R.id.sv_mobile)
    TextView sv_mobile;
    @BindView(R.id.sv_email)
    TextView sv_email;
    @BindView(R.id.sv_profile_photo)
    CircleImageView sv_profile_photo;
    @BindView(R.id.capture_image_profile)
    CircleImageView capture_image_profile;
    @BindView(R.id.editText_name)
    EditText editText_name;
    @BindView(R.id.editText_father_name)
    EditText editText_father_name;
    @BindView(R.id.txt_contact)
    TextView txt_contact;
    @BindView(R.id.editText_email)
    EditText editText_email;
    @BindView(R.id.img_edit)
    ImageView img_edit;
    @BindView(R.id.button_done)
    Button button_done;
    @BindView(R.id.main_layout)
    FrameLayout main_layout;
    private int REQUEST_CAMERA = 0,
            SELECT_FILE = 1;
    private String userChoosenTask;
    String pictureImagePath = "";
    Bitmap myBitmap;
    ConnectivityManager connectivityManager;
    boolean connected = false;
    Context context;
    String bundleuser;
    boolean is_Editing = FALSE;
    Bitmap bmp;
    Resources resources;
    ImagePopup imagePopup;
    String image_link;
    Calendar date_of_birth = Calendar.getInstance();
    Toolbar mActionBarToolbar;

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
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
                            ConnectivityUtils.checkSpeedWithColor(ProfileActivity.this, new ConnectivityUtils.ColorListener() {
                                @Override
                                public void onColorChanged(int color, String speed) {
                                    if (color == android.R.color.holo_green_dark) {
                                        connectivityLine.setVisibility(View.GONE);
//                                    connectivityLine.setBackgroundColor(getColor(R.color.blue));
                                    } else {
                                        connectivityLine.setVisibility(View.VISIBLE);
                                        connectivityLine.setBackgroundColor(getColor(color));
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
    AdView mAdView;
    private void loadAds(){
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
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
//                Log.e(TAG,"Add Errro "+adError.toString());
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
    protected void onCreate(Bundle savedInstanceState) {
        String languageToLoad = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        setContentView(R.layout.activity_profile);
        context = ProfileActivity.this;
        loadAds();
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.profile_activity_title));
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        imagePopup = new ImagePopup(context);
        imagePopup.setWindowHeight(ViewGroup.LayoutParams.MATCH_PARENT); // Optional
        imagePopup.setWindowWidth(ViewGroup.LayoutParams.MATCH_PARENT); // Optional
        //imagePopup.setBackgroundColor(Color.BLACK);  // Optional
        imagePopup.setFullScreen(true); // Optional
        imagePopup.setHideCloseIcon(true);  // Optional
        imagePopup.setImageOnClickClose(true);  // Optional
        imagePopup.setBackgroundColor(Color.TRANSPARENT);

        ButterKnife.bind(this);
        LoadProfileData loadProfileData = new LoadProfileData();
        loadProfileData.execute();

        Bundle bundle = getIntent().getExtras();
        bundleuser = bundle.getString("user_name");
        sv_user_name.setText(bundleuser);
        image_link = bundle.getString("img_link");


        if (image_link != null) {
            if (!image_link.equals("")) {
              /*  if (image_link.contains("croptrailsimages.s3")) {
                    ShowAwsImage.getInstance(context).downloadFile(Uri.parse(image_link), sv_profile_photo, image_link);
                }else{*/
                Uri uriprofile = Uri.parse(image_link);
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.ic_person_white_24dp)
                        .error(R.drawable.ic_person_white_24dp);
                Glide.with(context).load(uriprofile).apply(options).into(sv_profile_photo);
                //}
            }
            sv_profile_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bmp = DataHandler.newInstance().getMyBitmapForSvProfile();
                    Drawable d = new BitmapDrawable(getResources(), bmp);
                    imagePopup.initiatePopup(d);
                    imagePopup.viewPopup();
                }
            });
        } else {
            sv_profile_photo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person_white_24dp));
        }
        onClicks();
    }

    private void onClicks() {
        capture_image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    selectImage();
                } else {
                    //Toast.makeText(context, resources.getString(R.string.internet_not_connected), Toast.LENGTH_SHORT).show();
                    Toasty.error(context, resources.getString(R.string.internet_not_connected), Toast.LENGTH_SHORT, true).show();
                }
            }
        });
        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sv_dob.setPaintFlags(sv_dob.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                img_edit.setVisibility(View.INVISIBLE);
                sv_name.setVisibility(View.GONE);
                sv_fathers_name.setVisibility(View.GONE);
                sv_email.setVisibility(View.GONE);
                //sv_dob.setVisibility(View.GONE);
                sv_dob.setClickable(TRUE);
                editText_name.setVisibility(View.VISIBLE);
                editText_father_name.setVisibility(View.VISIBLE);
                editText_email.setVisibility(View.VISIBLE);
                button_done.setVisibility(View.VISIBLE);
                is_Editing = TRUE;
            }
        });
        sv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_Editing) {
                    final DatePickerDialog.OnDateSetListener dateofbirth = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            date_of_birth.set(Calendar.YEAR, year);
                            date_of_birth.set(Calendar.MONTH, monthOfYear);
                            date_of_birth.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            updateDateOfBirth();
                        }
                    };
                    new DatePickerDialog(ProfileActivity.this, dateofbirth, date_of_birth
                            .get(Calendar.YEAR), date_of_birth.get(Calendar.MONTH),
                            date_of_birth.get(Calendar.DAY_OF_MONTH)).show();
                }
            }

        });
        button_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                sendProfileData();
            }
        });
    }

    public void updateDateOfBirth() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sv_dob.setText(sdf.format(date_of_birth.getTime()));
        sv_dob.setError(null);

    }


    private class LoadProfileData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                Call<ProfileReciveMainData> profileReciveMainDataCall = apiService.getProfileDataforSupervisor(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID),
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID),
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                final boolean[] isLoaded = {false};
                long currMillis=AppConstants.getCurrentMills();
                profileReciveMainDataCall.enqueue(new Callback<ProfileReciveMainData>() {
                    @Override
                    public void onResponse(Call<ProfileReciveMainData> call, Response<ProfileReciveMainData> response) {
                        String error=null;
                        isLoaded[0]=true;
                        if (response.isSuccessful()) {
                            Log.e(TAG, new Gson().toJson(response.body()));
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(ProfileActivity.this, response.body().getMsg());
                            }  else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(ProfileActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(ProfileActivity.this, resources.getString(R.string.authorization_expired));
                            }else {
                                ResultProfile resultProfile = response.body().getResult();
                                if (resultProfile != null) {
                                    String stradd = "";
                                    if (resultProfile.getAddl1() != null) {
                                        if (resultProfile.getAddl1().equals("")) {

                                        } else {
                                            stradd = resultProfile.getAddl1() + ", ";
                                        }
                                    }
                                    if (resultProfile.getAddl2() != null) {
                                        if (resultProfile.getAddl2().equals("")) {

                                        } else {
                                            stradd = stradd + resultProfile.getAddl2() + ", ";
                                        }
                                    }
                                    if (resultProfile.getVillageOrCity() != null) {
                                        if (resultProfile.getVillageOrCity().equals("")) {

                                        } else {
                                            stradd = stradd + resultProfile.getVillageOrCity() + ", ";
                                        }
                                    }
                                    if (resultProfile.getState() != null) {
                                        if (resultProfile.getState().equals("")) {

                                        } else {
                                            stradd = stradd + resultProfile.getState();
                                        }
                                    }

                                    sv_address.setText(stradd);
                                    if (resultProfile.getName() != null) {
                                        sv_name.setText(resultProfile.getName());
                                        editText_name.setText(resultProfile.getName());
                                    } else {
                                        sv_name.setText("-");
                                    }

                                    if (resultProfile.getDob() != null) {
                                        if (!resultProfile.getDob().equals(AppConstants.for_date)) {
                                            String submit_format = "dd/MM/yyyy";
                                            SimpleDateFormat submit_sdf = new SimpleDateFormat(submit_format, Locale.US);
                                            Date DOB = null;
                                            try {
                                                DOB = new SimpleDateFormat("yyyy-MM-dd").parse(resultProfile.getDob().trim());
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            String setDOB = submit_sdf.format(DOB);
                                            sv_dob.setText(setDOB);
                                        } else
                                            sv_dob.setText("-");
                                    } else {
                                        sv_dob.setText("-");
                                    }
                                    if (resultProfile.getEmail() != null) {
                                        if (!resultProfile.getEmail().equals("")) {
                                            sv_email.setText(resultProfile.getEmail());
                                            editText_email.setText(resultProfile.getEmail());
                                        } else
                                            sv_email.setText("-");
                                    } else {
                                        sv_email.setText("-");
                                    }

                                    if (resultProfile.getFatherName() != null) {
                                        sv_fathers_name.setText(resultProfile.getFatherName());
                                        editText_father_name.setText(resultProfile.getFatherName());
                                    } else {
                                        sv_fathers_name.setText("-");
                                    }
                                    if (resultProfile.getMob() != null) {
                                        if (!resultProfile.getMob().equals(""))
                                            txt_contact.setText(resultProfile.getMob());
                                        else
                                            txt_contact.setText("-");
                                    } else {
                                        sv_mobile.setText("-");
                                    }
                                }
                            }
                        }  else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(ProfileActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(ProfileActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            ResponseBody responseBody = response.errorBody();
                            try {
                                error=responseBody.string().toString();

                                //Toast.makeText(context, resources.getString(R.string.no_response_plz_reload), Toast.LENGTH_SHORT).show();
                                Toasty.error(context, resources.getString(R.string.no_response_plz_reload), Toast.LENGTH_SHORT, true).show();

                                Log.e(TAG,  error+ "   Status" + response.code() + "  Message" + response.message());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                            notifyApiDelay(ProfileActivity.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileReciveMainData> call, Throwable t) {
                        long newMillis = AppConstants.getCurrentMills();
                        isLoaded[0]=true;
                        long diff = newMillis - currMillis;
                        notifyApiDelay(ProfileActivity.this, call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);
                        // Toast.makeText(context, resources.getString(R.string.no_response_plz_reload), Toast.LENGTH_SHORT).show();
                        Toasty.error(context, resources.getString(R.string.no_response_plz_reload), Toast.LENGTH_SHORT, true).show();

                    }
                });
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        internetFlow(isLoaded[0]);
                    }
                }, AppConstants.DELAY);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    private boolean isConnected() {
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;

        } else {
            return false;
        }
    }

    protected void sendProfileData() {

        Date DOB;
        String putDOB = null;

        {
            try {
                String submit_format = "yyyy-MM-dd";
                SimpleDateFormat submit_sdf = new SimpleDateFormat(submit_format, Locale.US);
                DOB = new SimpleDateFormat("dd/MM/yyyy").parse(sv_dob.getText().toString().trim());
                putDOB = submit_sdf.format(DOB);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        //SendAddNewFarmData sendAddNewFarmData=new SendAddNewFarmData();
        AddProfileSendData addProfileSendData = new AddProfileSendData();
        addProfileSendData.setSv_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
        addProfileSendData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        addProfileSendData.setFather_name(editText_father_name.getText().toString().trim());
        addProfileSendData.setName(editText_name.getText().toString().trim());
        addProfileSendData.setEmail(editText_email.getText().toString().trim());
        addProfileSendData.setDob(putDOB);
        addProfileSendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        addProfileSendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        Call<StatusMsgModel> statusMsgModelCall = apiService.getAddNewProfile(addProfileSendData);
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();
        statusMsgModelCall.enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                String error=null;
                isLoaded[0]=true;
                StatusMsgModel statusMsgModel = response.body();
                if (response.isSuccessful()) {
                    if (statusMsgModel.getStatus() == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sv_name.setText(editText_name.getText().toString());
                                sv_fathers_name.setText(editText_father_name.getText().toString());
                                sv_dob.setText(sv_dob.getText().toString());
                                sv_email.setText(editText_email.getText().toString());

                                is_Editing = FALSE;
                                img_edit.setVisibility(View.VISIBLE);
                                sv_name.setVisibility(View.VISIBLE);
                                sv_fathers_name.setVisibility(View.VISIBLE);
                                sv_email.setVisibility(View.VISIBLE);
                                sv_dob.setVisibility(View.VISIBLE);
                                editText_name.setVisibility(View.GONE);
                                editText_father_name.setVisibility(View.GONE);
                                editText_email.setVisibility(View.GONE);
                                //sv_dob.setVisibility(View.GONE);
                                button_done.setVisibility(View.GONE);
                            }
                        });

                    }  else if (response.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(ProfileActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(ProfileActivity.this, resources.getString(R.string.authorization_expired));
                    }else if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(ProfileActivity.this, response.body().getMsg());
                    } else {
                        //  Toast.makeText(context, resources.getString(R.string.no_response), Toast.LENGTH_LONG).show();
                        Toasty.error(context, resources.getString(R.string.no_response), Toast.LENGTH_LONG, true).show();

                        Log.e("status", response.toString());
                    }
                } else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(ProfileActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(ProfileActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    // Toast.makeText(context, resources.getString(R.string.no_response), Toast.LENGTH_LONG).show();

                    try {
                        error= response.errorBody().string().toString();
                        Toasty.error(context, resources.getString(R.string.no_response), Toast.LENGTH_LONG, true).show();
                        Log.e("Response",error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(ProfileActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                // Toast.makeText(context, resources.getString(R.string.no_response), Toast.LENGTH_LONG).show();
                Toasty.error(context, resources.getString(R.string.no_response), Toast.LENGTH_LONG, true).show();
                long newMillis = AppConstants.getCurrentMills();
                isLoaded[0]=true;
                long diff = newMillis - currMillis;
                notifyApiDelay(ProfileActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, t.toString());
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

    public boolean checkStoragePermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(getString(R.string.permission_necessary));
                    alertBuilder.setMessage(getString(R.string.external_storage_permission_is_necessary));
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            requestGalleryPermission();
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                    return false;
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            SELECT_FILE);

                    return false;
                }
            } else {
                return  true;
            }
        } else {
            return true;
        }
    }

    public boolean checkCameraPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(getString(R.string.permission_necessary));
                    alertBuilder.setMessage("Camera permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            requestCameraAndStoragePermission();
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                    return false;
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CAMERA
                    );

                    return false;
                }
            } else {
                return  true;
            }
        } else {
            return true;
        }
    }

    private void requestCameraAndStoragePermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CAMERA
        );

    }

    private void requestGalleryPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                SELECT_FILE);
    }



    private void selectImage() {
        final CharSequence[] items = {resources.getString(R.string.image_take_photo), resources.getString(R.string.image_choose_from_library),
                resources.getString(R.string.image_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(resources.getString(R.string.image_select_image_title));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(resources.getString(R.string.image_take_photo))) {
                    userChoosenTask = "Take Photo";
                    if (checkCameraPermission(context))
                        cameraIntent();
                } else if (items[item].equals(resources.getString(R.string.image_choose_from_library))) {
                    userChoosenTask = "Choose from Library";
                    if (checkStoragePermission(context))
                        galleryIntent();
                } else if (items[item].equals(resources.getString(R.string.image_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //  intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.gallery_intent_title)), SELECT_FILE);
    }


    private void cameraIntent() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    private void onCaptureImageResult(Intent data) {
        File imgFile = new File(pictureImagePath);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            myBitmap = getResizedBitmap(myBitmap, 720, 1080);
            if (myBitmap != null) {
                Toast.makeText(context, "one", Toast.LENGTH_SHORT).show();
                UploadProfileAsync uploadProfileAsync = new UploadProfileAsync();
                uploadProfileAsync.execute(pictureImagePath,
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID), SharedPreferencesMethod
                                .getString(context, SharedPreferencesMethod.COMP_ID));
            } else {
                //  Toast.makeText(context, resources.getString(R.string.no_response), Toast.LENGTH_SHORT).show();
                Toasty.error(context, resources.getString(R.string.no_response), Toast.LENGTH_SHORT, true).show();

            }
            sv_profile_photo.setImageBitmap(myBitmap);

            // Exif.setText(ReadExif(imgFile.getAbsolutePath()));

        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        pictureImagePath = cursor.getString(columnIndex);
        cursor.close();
        myBitmap = BitmapFactory.decodeFile(pictureImagePath);
        myBitmap = getResizedBitmap(myBitmap, 720, 1080);
        if (myBitmap != null) {
            Toast.makeText(context, "two", Toast.LENGTH_SHORT).show();
            UploadProfileAsync uploadProfileAsync = new UploadProfileAsync();
            uploadProfileAsync.execute(pictureImagePath,
                    SharedPreferencesMethod.getString(this, SharedPreferencesMethod.PERSON_ID), SharedPreferencesMethod
                            .getString(context, SharedPreferencesMethod.COMP_ID));
            /*      ImageUploadToServerFunction(pictureImagePath,SharedPreferencesMethod.getString(getContext(),SharedPreferencesMethod.SVUSERID));
             */
        } else {
            // Toast.makeText(this, resources.getString(R.string.select_from_gallery_error), Toast.LENGTH_SHORT).show();
            Toasty.error(this, resources.getString(R.string.select_from_gallery_error), Toast.LENGTH_SHORT, true).show();

        }
        sv_profile_photo.setImageBitmap(myBitmap);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
            }
            /*case 1:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }*/
        }
    }


    @Override
    public void onBackPressed() {

        if (is_Editing) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle(resources.getString(R.string.alert_title_label));
            alertDialog.setMessage(resources.getString(R.string.profile_alert_dialog_message));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.profile_alert_dialog_yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /*is_Editing = FALSE;
                            img_edit.setVisibility(View.VISIBLE);
                            sv_name.setVisibility(View.VISIBLE);
                            sv_fathers_name.setVisibility(View.VISIBLE);
                            sv_email.setVisibility(View.VISIBLE);
                            sv_dob.setVisibility(View.VISIBLE);
                            editText_name.setVisibility(View.GONE);
                            editText_father_name.setVisibility(View.GONE);
                            editText_email.setVisibility(View.GONE);
                            editText_dob.setVisibility(View.GONE);
                            button_done.setVisibility(View.GONE);
                            Intent intent = new Intent(context,SettingActivity.class);
                            startActivity(intent);*/
                            finish();
                            is_Editing = FALSE;
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.profile_alert_dialog_no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } else {
            super.onBackPressed();
        }
    }




    class UploadProfileAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String img_path = strings[0];
            String person_id = strings[1];
            String comp_id = strings[2];
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);


            try {
                String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface service = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                RequestBody request_person_id = RequestBody.create(MediaType.parse("multipart/form-data"), person_id);

                RequestBody request_comp_id = RequestBody.create(MediaType.parse("multipart/form-data"), comp_id);
                RequestBody request_user_id = RequestBody.create(MediaType.parse("multipart/form-data"), userId);
                RequestBody request_token_id = RequestBody.create(MediaType.parse("multipart/form-data"), token);
                ByteArrayOutputStream byteArrayOutputStreamObject1;
                byteArrayOutputStreamObject1 = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStreamObject1);
                final String ConvertImage1 = Base64.encodeToString(byteArrayOutputStreamObject1.toByteArray(), Base64.DEFAULT);

                RequestBody request_exp_image = RequestBody.create(MediaType.parse("multipart/form-data"), ConvertImage1);

                ImageBody imageBody=new ImageBody(ConvertImage1,person_id,comp_id,userId,token);
                Log.e(TAG,"Data_Sent "+new Gson().toJson(imageBody));
                Call<ImageUpdateResponse> call = service.updateUserProfile(imageBody);
                final boolean[] isLoaded = {false};
                long currMillis=AppConstants.getCurrentMills();
                final Response<ImageUpdateResponse> bodyResponse = call.execute();
                String error=null;
                isLoaded[0]=true;
                if (bodyResponse.isSuccessful()) {
                    Log.e("ProfileActivity", "Image Res " + new Gson().toJson(bodyResponse.body()));

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (bodyResponse.body().getStatus() == 10) {
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(ProfileActivity.this, resources.getString(R.string.multiple_login_msg));
                                } else if (bodyResponse.body().getStatus()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                                    //un authorized access
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(ProfileActivity.this, resources.getString(R.string.unauthorized_access));
                                } else if (bodyResponse.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                                    // auth token expired
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(ProfileActivity.this, resources.getString(R.string.authorization_expired));
                                } else {


                                    String img_url = bodyResponse.body().getImageLink();
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.USER_IMAGE, img_url);

                                    Uri uriprofile = Uri.parse(img_url);
                                        RequestOptions options = new RequestOptions()
                                                .placeholder(R.drawable.ic_person_white_24dp)
                                                .error(R.drawable.ic_person_white_24dp);
                                        Glide.with(context).load(uriprofile).apply(options).into(sv_profile_photo);
                                        URL url = null;
                                        try {
                                            url = new URL(img_url);
                                            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else if (bodyResponse.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(ProfileActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (bodyResponse.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(ProfileActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    Log.e("ProfileCheck", "here in not successfull");
                    try {
                        error=bodyResponse.errorBody().string().toString();
                        Log.e(TAG, "WITHERROR" + error);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(ProfileActivity.this, getString(R.string.profile_image_update_failed));

                        }
                    });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || bodyResponse.code() != 200) {
                    notifyApiDelay(ProfileActivity.this, bodyResponse.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, bodyResponse.code());
                }


            } catch (
                    Exception e) {
                e.printStackTrace();

                Log.e(TAG, "here in not looperexception ",e);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //  Toast.makeText(getContext(),"In Catch", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(ProfileActivity.this, "Failed to update profile image. Please try again");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }


            return null;
        }
    }
}
