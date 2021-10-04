package com.i9930.croptrails.AddFarm;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hbb20.CountryCodePicker;
import com.i9930.croptrails.AddFarm.Adapter.AddFarmAdapter2;
import com.i9930.croptrails.AddFarm.Adapter.ImageType;
import com.i9930.croptrails.AddFarm.Model.CowAndCatles;
import com.i9930.croptrails.AddFarm.Model.FarmData;
import com.i9930.croptrails.AddFarm.Model.FarmerAndFarmData;
import com.i9930.croptrails.AddFarm.Model.FarmerFarmInsertResponse;
import com.i9930.croptrails.AddFarm.Model.FarmerSpinnerData;
import com.i9930.croptrails.AddFarm.Model.FetchFarmerResponse;
import com.i9930.croptrails.AddFarm.Model.IfscResponse;
import com.i9930.croptrails.AddFarm.Model.SupervisorDatum;
import com.i9930.croptrails.AddFarm.Model.SupervisorListResponse;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterCity;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterCluster;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterCountry;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterFarmer;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterNewDD;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterNewDDValue;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SvSpinnerAdapter;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.Address.CityDatum;
import com.i9930.croptrails.CommonClasses.Address.CityResponse;
import com.i9930.croptrails.CommonClasses.Address.CountryDatum;
import com.i9930.croptrails.CommonClasses.Address.CountryResponse;
import com.i9930.croptrails.CommonClasses.Address.StateDatum;
import com.i9930.croptrails.CommonClasses.Cluster;
import com.i9930.croptrails.CommonClasses.ClusterResponse;
import com.i9930.croptrails.CommonClasses.DDNew;
import com.i9930.croptrails.CommonClasses.DDResponseNew;
import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormDatum;
import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormResponse;
import com.i9930.croptrails.CommonClasses.ParamData;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.DatePick.DateTimePickerEditText;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.Season;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.SpinnerResponse;
import com.i9930.croptrails.HarvestSubmit.Interfaces.OnFarmDatasetChanged;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.VerifySendData;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.AddFarmCache.AddFarmCCacheManager;
import com.i9930.croptrails.RoomDatabase.AddFarmCache.AddFarmCache;
import com.i9930.croptrails.RoomDatabase.AddFarmCache.FetchFarmCacheListener;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegCacheManager;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegModel;
import com.i9930.croptrails.RoomDatabase.CompRegistry.Model.CompRegResult;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownCacheManager;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownFetchListener;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownCacheManager2;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownFetchListener2;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownT2;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmCacheManager;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerCacheManager;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerT;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.CaptureConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.LocationInsertReciever;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;
import com.i9930.croptrails.showcaseview.MaterialShowcaseSequence;
import com.i9930.croptrails.showcaseview.MaterialShowcaseView;
import com.i9930.croptrails.showcaseview.ShowcaseConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class AddFarmActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnFarmDatasetChanged {
    String TAG = "AddFarmActivity";
    Context context;
    @BindView(R.id.farmDetailsLAyout)
    LinearLayout farmDetailsLAyout;
    private List<FarmData> farmList = new ArrayList<>();
    @BindView(R.id.addFarmTv)
    TextView addFarmTv;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    AddFarmAdapter2 adapter;
    ProgressDialog progressDialog;
    boolean isShownGpsDialog = false;
    boolean isValidIfsc = false;
    @BindView(R.id.tv_add_new_farm_submit_bt)
    TextView tv_add_new_farm_submit_bt;

    @BindView(R.id.ti_et_name_of_farmer)
    AutoCompleteTextView farmerNameEt;
    @BindView(R.id.ti_et_father_name)
    EditText fatherNameEt;
    @BindView(R.id.ti_et_phone)
    EditText phoneNumberEt;
    @BindView(R.id.ti_et_aadhaarcard)
    EditText adhaarEt;

    @BindView(R.id.ti_dob)
    TextInputLayout ti_dob;
    @BindView(R.id.ti_et_date_of_birth)
    DateTimePickerEditText dobEt;
    @BindView(R.id.ti_et_date_of_birth_age)
    EditText dobEtAge;

    @BindView(R.id.ti_et_village_or_city)
    EditText villageOrCityEt;
    @BindView(R.id.ti_et_mandal_or_tehsil)
    EditText mandalOrTehsilEt;
    @BindView(R.id.ti_et_district)
    EditText districtEt;
    @BindView(R.id.ti_et_state)
    EditText stateEt;


    @BindView(R.id.ti_et_country)
    EditText countryEt;
    @BindView(R.id.ti_et_bankname)
    EditText bankNameEt;
    @BindView(R.id.ti_et_accountno)
    EditText accountEt;

    @BindView(R.id.etAddressLine1)
    EditText etAddressLine1;
    @BindView(R.id.etAddressLine2)
    EditText etAddressLine2;
    @BindView(R.id.ti_et_holdername)
    EditText accountHolderName;
    @BindView(R.id.ti_et_branch)
    EditText branchEt;
    @BindView(R.id.ti_et_ifsccode)
    EditText ifscCodeEt;


    boolean isSpinnerDataLoaded = false;
    Toolbar mActionBarToolbar;
    Resources resources;
    /*@BindView(R.id.checkbox_add_farm)
    CheckBox checkbox_add_farm;*/
    @BindView(R.id.existing_farmer_layout)
    LinearLayout existing_farmer_layout;
    @BindView(R.id.new_farmer_layout)
    LinearLayout new_farmer_layout;
    @BindView(R.id.farmer_spinner)
    gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner farmer_spinner;
    private boolean addingToExistingFarmer = false;
    private ArrayList<FarmerSpinnerData> farmerDataList = new ArrayList<>();
    private boolean isFarmer = false;
    ViewFailDialog viewFailDialog;
    LocationManager locationManager;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    String mprovider;
    FusedLocationProviderClient mFusedLocationClient;
    Location location;
    Location mLastLocation;
    @BindView(R.id.moreDetailsFarmerBtn)
    TextView moreDetailsFarmerBtn;
    @BindView(R.id.extraFarmerDetails)
    LinearLayout extraFarmerDetailsLayout;
    private List<DDNew> irrigationTypeDDList = new ArrayList<>();
    private List<DDNew> irrigationSourceDDList = new ArrayList<>();
    private List<DDNew> soilTypeDDList = new ArrayList<>();
    private List<Season> seasonDDList = new ArrayList<>();
    private List<DDNew> farmCropList = new ArrayList<>();
    @BindView(R.id.countryCodePicker)
    CountryCodePicker countryCodePicker;
    @BindView(R.id.ti_phone)
    TextInputLayout ti_phone;
    @BindView(R.id.phone_layout_inner)
    RelativeLayout phone_layout_inner;
    @BindView(R.id.ti_et_phone2)
    EditText ti_et_phone2;
    AlarmManager manager;
    PendingIntent pendingIntentFetchLocation;
    boolean isAdmin = false;

    @BindView(R.id.cluster_spinner)
    gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner cluster_spinner;
    @BindView(R.id.cluster_spinner_layout)
    LinearLayout cluster_spinner_layout;
    @BindView(R.id.progressBar_image)
    GifImageView progressBar_image;

    @BindView(R.id.ti_et_cast)
    EditText ti_et_cast;

    @BindView(R.id.ti_et_income)
    EditText ti_et_income;

    @BindView(R.id.maleRadio)
    RadioButton maleRadio;
    @BindView(R.id.femaleRadio)
    RadioButton femaleRadio;
    @BindView(R.id.otherRadio)
    RadioButton otherRadio;
    @BindView(R.id.capture_image_profile)
    CircleImageView capture_image_profile;

    //Share
    @BindView(R.id.radioIsShareYes)
    RadioButton radioIsShareYes;
    @BindView(R.id.radioIsShareNo)
    RadioButton radioIsShareNo;
    @BindView(R.id.shareCertLayout)
    LinearLayout shareCertLayout;
    @BindView(R.id.radioShareCertYes)
    RadioButton radioShareCertYes;
    @BindView(R.id.radioShareCertNo)
    RadioButton radioShareCertNo;
    @BindView(R.id.ti_et_cert_count)
    EditText ti_et_cert_count;
    @BindView(R.id.ti_et_cert_value)
    EditText ti_et_cert_value;
    @BindView(R.id.shareCertLay2)
    LinearLayout shareCertLay2;

    //Animal
    @BindView(R.id.ti_et_animal_count)
    EditText ti_et_animal_count;
    @BindView(R.id.ti_et_milk_sale_price)
    EditText ti_et_milk_sale_price;
    @BindView(R.id.ti_et_milk_sale)
    EditText ti_et_milk_sale;
    /*@BindView(R.id.radioAnimalsYes)
    RadioButton radioAnimalsYes;
    @BindView(R.id.radioAnimalsNo)
    RadioButton radioAnimalsNo;*/
    @BindView(R.id.ti_et_other_animal)
    EditText ti_et_other_animal;
    @BindView(R.id.radioSPhoneYes)
    RadioButton radioSPhoneYes;
    @BindView(R.id.radioSPhoneNo)
    RadioButton radioSPhoneNo;
    @BindView(R.id.imgRelLay)
    LinearLayout imgRelLay;
    @BindView(R.id.ti_et_pan)
    EditText ti_et_pan;

    @BindView(R.id.supervisor_spinner_layout)
    LinearLayout supervisor_spinner_layout;
    @BindView(R.id.supervisor_spinner)
    SearchableSpinner supervisor_spinner;
    SupervisorDatum supervisorDatum = null;
    ArrayList<SupervisorDatum> supervisorDatumList = new ArrayList<>();
    ArrayList<SupervisorDatum> supervisorDatumListBackup = new ArrayList<>();
    SvSpinnerAdapter svAdapter;
    FarmerAndFarmData farmerAndFarmDataCache = new FarmerAndFarmData();
    AddFarmCache addFarmCache;
    List<LatLng> latLngListCheckArea = new ArrayList<>();

    public static boolean isIfscCodeValid(String ifsc) {
        String regExp = "^[A-Z]{4}[0][A-Z0-9]{6}$";
        boolean isvalid = false;

        if (ifsc.length() > 0) {
            isvalid = ifsc.matches(regExp);
        }
        return isvalid;
    }

    String areaInAcre;
    String mapZoom;
    @BindView(R.id.connectivityLine)
    View connectivityLine;

    @BindView(R.id.ti_et_beneficiary)
    EditText beneficiaryEt;
    @BindView(R.id.civil_status_spinner)
    gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner civil_status_spinner;
    @BindView(R.id.etSpouse)
    EditText etSpouse;
    @BindView(R.id.tiSpouse)
    TextInputLayout tiSpouse;
    @BindView(R.id.etUpi)
    EditText etUpi;
    @BindView(R.id.tiSpouseAge)
    TextInputLayout tiSpouseAge;

    @BindView(R.id.personalOuter)
    LinearLayout personalOuter;
    @BindView(R.id.personalInner)
    LinearLayout personalInner;
    @BindView(R.id.personalAnim)
    RelativeLayout personalAnim;
    @BindView(R.id.personalArroImageview)
    ImageView personalArroImageview;


    @BindView(R.id.identityOuter)
    LinearLayout identityOuter;
    @BindView(R.id.identityInner)
    LinearLayout identityInner;
    @BindView(R.id.identityAnim)
    RelativeLayout identityAnim;
    @BindView(R.id.identityArroImageview)
    ImageView identityArroImageview;


    @BindView(R.id.addressOuter)
    LinearLayout addressOuter;
    @BindView(R.id.addressInner)
    LinearLayout addressInner;
    @BindView(R.id.addressAnim)
    RelativeLayout addressAnim;
    @BindView(R.id.addressArroImageview)
    ImageView addressArroImageview;

    @BindView(R.id.bankOuter)
    LinearLayout bankOuter;
    @BindView(R.id.bankInner)
    LinearLayout bankInner;
    @BindView(R.id.bankAnim)
    RelativeLayout bankAnim;
    @BindView(R.id.bankArroImageview)
    ImageView bankArroImageview;

    @BindView(R.id.etSwiftCode)
    EditText etSwiftCode;
    @BindView(R.id.ti_et_phone_address)
    EditText ti_et_phone_address;
    @BindView(R.id.emailEt)
    EditText emailEt;


    @BindView(R.id.familyOuter)
    LinearLayout familyOuter;
    @BindView(R.id.familyInner)
    LinearLayout familyInner;
    @BindView(R.id.familyAnim)
    RelativeLayout familyAnim;
    @BindView(R.id.familyArroImageview)
    ImageView familyArroImageview;

    @BindView(R.id.familyCount)
    EditText familyCount;
    @BindView(R.id.adultCount)
    EditText adultCount;
    @BindView(R.id.kidsCount)
    EditText kidsCount;
    @BindView(R.id.dependentCount)
    EditText dependentCount;

    @BindView(R.id.etReligion)
    EditText etReligion;
    @BindView(R.id.etCast)
    EditText etCast;


    @BindView(R.id.socialOuter)
    LinearLayout socialOuter;
    @BindView(R.id.socialInner)
    LinearLayout socialInner;
    @BindView(R.id.socialAnim)
    RelativeLayout socialAnim;
    @BindView(R.id.socialArroImageview)
    ImageView socialArroImageview;
    @BindView(R.id.ti_addL2)
    TextInputLayout ti_addL2;
    @BindView(R.id.addressMobRel)
    RelativeLayout addressMobRel;

    boolean isPreviousCropMandatory = false;

    private void hideShowAndClick() {

        socialAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animate(socialInner, socialArroImageview);
            }
        });

        bankAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animate(bankInner, bankArroImageview);
            }
        });


        familyAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animate(familyInner, familyArroImageview);
            }
        });
        personalAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animate(personalInner, personalArroImageview);
            }
        });

        identityAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animate(identityInner, identityArroImageview);
            }
        });

        addressAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animate(addressInner, addressArroImageview);
            }
        });

    }

    @BindView(R.id.countrySpinner)
    gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner countrySpinner;
    @BindView(R.id.stateSpinner)
    gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner stateSpinner;
    @BindView(R.id.districtSpinner)
    gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner districtSpinner;
    ArrayList<CountryDatum> countryDatumList = new ArrayList<>();
    ArrayList<StateDatum> stateDatumList = new ArrayList<>();
    ArrayList<CityDatum> cityDatumList = new ArrayList<>();

    SpinnerAdapterCountry countrySpinnerAdapter;
    SpinnerAdapterState stateSpinnerAdapter;
    SpinnerAdapterCity citySpinnerAdapter;


    private void checkCompReg() {
        identityOuter.setVisibility(View.GONE);
        bankOuter.setVisibility(View.GONE);
        addressOuter.setVisibility(View.GONE);
        familyOuter.setVisibility(View.GONE);
//        socialOuter.setVisibility(View.GONE);
        personalOuter.setVisibility(View.GONE);
        CompRegCacheManager.getInstance(context).getCompRegData(new CompRegCacheManager.CompRegListener() {
            @Override
            public void onRegLoaded(CompRegModel compRegModel) {
                if (compRegModel != null) {
                    try {
                        CompRegResult compRegResult = new Gson().fromJson(compRegModel.getData().trim(), CompRegResult.class);
                        if (compRegResult != null) {
                            if (compRegResult.getStatus() != null && !TextUtils.isEmpty(compRegResult.getStatus()) && compRegResult.getStatus().equals("1")) {
                                identityOuter.setVisibility(View.VISIBLE);
                            }
                        } else {
                        }
                    } catch (Exception e) {
                    }
                } else if (AppConstants.COMP_REG.DEFAULT) {
                    identityOuter.setVisibility(View.VISIBLE);
                }
            }
        }, AppConstants.COMP_REG.FARMER_PERSONAL_ID);

        CompRegCacheManager.getInstance(context).getCompRegData(new CompRegCacheManager.CompRegListener() {
            @Override
            public void onRegLoaded(CompRegModel compRegModel) {
                if (compRegModel != null) {
                    try {
                        CompRegResult compRegResult = new Gson().fromJson(compRegModel.getData().trim(), CompRegResult.class);
                        if (compRegResult != null) {
                            if (compRegResult.getStatus() != null && !TextUtils.isEmpty(compRegResult.getStatus()) && compRegResult.getStatus().equals("1")) {
                                bankOuter.setVisibility(View.VISIBLE);
                            }
                        } else {

                        }
                    } catch (Exception e) {

                    }
                } else if (AppConstants.COMP_REG.DEFAULT) {
                    bankOuter.setVisibility(View.VISIBLE);
                }
            }
        }, AppConstants.COMP_REG.FARMER_FINANCIAL_INFO);
        CompRegCacheManager.getInstance(context).getCompRegData(new CompRegCacheManager.CompRegListener() {
            @Override
            public void onRegLoaded(CompRegModel compRegModel) {
                if (compRegModel != null) {
                    try {
                        CompRegResult compRegResult = new Gson().fromJson(compRegModel.getData().trim(), CompRegResult.class);
                        if (compRegResult != null) {
                            if (compRegResult.getStatus() != null && !TextUtils.isEmpty(compRegResult.getStatus()) && compRegResult.getStatus().equals("1")) {
                                addressOuter.setVisibility(View.VISIBLE);
                            }
                        } else {

                        }
                    } catch (Exception e) {

                    }
                } else if (AppConstants.COMP_REG.DEFAULT) {
                    addressOuter.setVisibility(View.VISIBLE);
                }
            }
        }, AppConstants.COMP_REG.FARMER_CONTACT_INFO);
        CompRegCacheManager.getInstance(context).getCompRegData(new CompRegCacheManager.CompRegListener() {
            @Override
            public void onRegLoaded(CompRegModel compRegModel) {
                if (compRegModel != null) {
                    try {
                        CompRegResult compRegResult = new Gson().fromJson(compRegModel.getData().trim(), CompRegResult.class);
                        if (compRegResult != null) {
                            if (compRegResult.getStatus() != null && !TextUtils.isEmpty(compRegResult.getStatus()) && compRegResult.getStatus().equals("1")) {
                                familyOuter.setVisibility(View.VISIBLE);
                            }
                        } else {

                        }
                    } catch (Exception e) {

                    }
                } else if (AppConstants.COMP_REG.DEFAULT) {
                    familyOuter.setVisibility(View.VISIBLE);
                }
            }
        }, AppConstants.COMP_REG.FARMER_FAMILY_INFO);
//        CompRegCacheManager.getInstance(context).getCompRegData(new CompRegCacheManager.CompRegListener() {
//            @Override
//            public void onRegLoaded(CompRegModel compRegModel) {
//                if (compRegModel!=null){
//                    try {
//                        CompRegResult compRegResult=new Gson().fromJson(compRegModel.getData().trim(),CompRegResult.class);
//                        if (compRegResult!=null){
//                            if (compRegResult.getStatus()!=null&&!TextUtils.isEmpty(compRegResult.getStatus())&&compRegResult.getStatus().equals("1")){
//                                socialOuter.setVisibility(View.VISIBLE);
//                            }
//                        }else {
//
//                        }
//                    }catch (Exception e){
//
//                    }
//                }else if (AppConstants.COMP_REG.DEFAULT){
//                    socialOuter.setVisibility(View.VISIBLE);
//                }
//            }
//        },AppConstants.COMP_REG.SOCIAL);
        CompRegCacheManager.getInstance(context).getCompRegData(new CompRegCacheManager.CompRegListener() {
            @Override
            public void onRegLoaded(CompRegModel compRegModel) {
                if (compRegModel != null) {
                    try {
                        CompRegResult compRegResult = new Gson().fromJson(compRegModel.getData().trim(), CompRegResult.class);
                        if (compRegResult != null) {
                            if (compRegResult.getStatus() != null && !TextUtils.isEmpty(compRegResult.getStatus()) && compRegResult.getStatus().equals("1")) {
                                personalOuter.setVisibility(View.VISIBLE);
                            }
                        } else {

                        }
                    } catch (Exception e) {

                    }
                } else if (AppConstants.COMP_REG.DEFAULT) {
                    personalOuter.setVisibility(View.VISIBLE);
                }
            }
        }, AppConstants.COMP_REG.FARMER_PERSONAL_INFO);

        /*countrySpinner.setPrompt("Gender");
        stateSpinner.setPrompt("Gender");*/

    }

    public static int countryIndex = 0;

    boolean isOfflineMode = true;
    //    private SearchableSpinner mSearchableSpinner;
//    private SearchableSpinner mSearchableSpinner1;
//    SimpleListAdapter mSimpleListAdapter;
    SpinnerAdapterState mSimpleArrayListAdapter;

    @BindView(R.id.casteCategoryspinner)
    SearchableSpinner casteCategoryspinner;
    @BindView(R.id.casteSpinner)
    SearchableSpinner casteSpinner;
    @BindView(R.id.religionSpinner)
    SearchableSpinner religionSpinner;
    @BindView(R.id.networkSpinner)
    SearchableSpinner networkSpinner;
    //    @BindView(R.id.motorHPSpinner)
//    SearchableSpinner motorHPSpinner;
    @BindView(R.id.microIrriAwarenessSpinner)
    SearchableSpinner microIrriAwarenessSpinner;

    @BindView(R.id.literateRadio)
    RadioButton literateRadio;

    @BindView(R.id.illiterateRadio)
    RadioButton illiterateRadio;
    @BindView(R.id.aplRadio)
    RadioButton aplRadio;
    @BindView(R.id.bplRadio)
    RadioButton bplRadio;

    @BindView(R.id.etSpouseAge)
    EditText etSpouseAge;
    @BindView(R.id.etSpouseDob)
    DateTimePickerEditText etSpouseDob;

    @BindView(R.id.mobLayout)
    LinearLayout mobLayout;
    @BindView(R.id.selfSvFarmerCheckBox)
    CheckBox selfSvFarmerCheckBox;

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
        setContentView(R.layout.activity_add_farm);
        context = this;
        ButterKnife.bind(this);
        isPreviousCropMandatory = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_PREVIOUS_CROP_MANDATORY);
        mSimpleArrayListAdapter = new SpinnerAdapterState(this, stateDatumList);
        hideShowAndClick();
        initializeCountry();
        TextView title = (TextView) findViewById(R.id.tittle);
        title.setText(resources.getString(R.string.add_farm_title));
        viewFailDialog = new ViewFailDialog();
        checkCompReg();
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        final String userName=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NAME);
        final String userNumber=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NUMBER);
        final String countryCode=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COUNTRY_CODE);
        isOfflineMode = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE);
        if (isOfflineMode){
            selfSvFarmerCheckBox.setVisibility(View.GONE);
        }else{
            selfSvFarmerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        mobLayout.setWeightSum(0.60f);

//                        phone_layout_inner.setVisibility(View.GONE);
                        imgRelLay.setVisibility(View.GONE);
                        networkSpinner.setVisibility(View.GONE);
                        moreDetailsFarmerBtn.setVisibility(View.GONE);
//                        new_farmer_layout.setVisibility(View.GONE);
                        farmerNameEt.setText(userName!=null?userName:"");
                        phoneNumberEt.setText(userNumber!=null?userNumber:"");
                        if (AppConstants.isValidString(countryCode))
                        countryCodePicker.setFullNumber(countryCode+userNumber);
                    }else{
                        mobLayout.setWeightSum(1f);
                        phone_layout_inner.setVisibility(View.VISIBLE);
                        imgRelLay.setVisibility(View.VISIBLE);
                        networkSpinner.setVisibility(View.VISIBLE);
                        moreDetailsFarmerBtn.setVisibility(View.VISIBLE);
                        new_farmer_layout.setVisibility(View.VISIBLE);
                        farmerNameEt.setText("");
                        phoneNumberEt.setText("");
                    }
                }
            });
        }
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, LocationInsertReciever.class);
        pendingIntentFetchLocation = PendingIntent.getBroadcast(this, 0, intent, 0);
        manager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 3600000, pendingIntentFetchLocation);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(resources.getString(R.string.please_wait_msg));
        progressDialog.setCancelable(false);
        countrySpinnerAdapter = new SpinnerAdapterCountry(context, countryDatumList);
        stateSpinnerAdapter = new SpinnerAdapterState(context, stateDatumList);
        citySpinnerAdapter = new SpinnerAdapterCity(context, cityDatumList);
        countrySpinner.setAdapter(countrySpinnerAdapter);
        stateSpinner.setAdapter(stateSpinnerAdapter);
        districtSpinner.setAdapter(citySpinnerAdapter);
        mSimpleArrayListAdapter.notifyDataSetChanged();
        countrySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                try {
                    if (i > 0) {
                        initializeState();
                        initializeCity();
                        if (countrySpinnerAdapter.getItem(i).getStateDatumList() != null) {
                            stateDatumList.addAll(countrySpinnerAdapter.getItem(i).getStateDatumList());
                            Log.e(TAG, "STATES at" + i + " " + new Gson().toJson(stateDatumList));
                        }

                        stateSpinnerAdapter.notifyDataSetChanged();
                        citySpinnerAdapter.notifyDataSetChanged();
                        try {
                            stateSpinner.setSelectedItem(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        initializeState();
                        initializeCity();
                        stateSpinnerAdapter.notifyDataSetChanged();
                        citySpinnerAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        String country = countryCodePicker.getSelectedCountryName();
        String compCountry = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_COUNTRY_ID);
        for (int i = 0; i < countryDatumList.size(); i++) {
            if (AppConstants.isValidString(compCountry) && compCountry.equals(countryDatumList.get(i).getId())) {
                try {
                    countryIndex = i + 1;
                    countrySpinner.setSelectedItem(countryIndex);
                } catch (Exception e) {
                }
                break;
            } else if (AppConstants.isValidString(country) && countryDatumList.get(i).getName() != null && country.trim().equals(countryDatumList.get(i).getName().trim())) {
                try {
                    countryIndex = i + 1;
                    countrySpinner.setSelectedItem(countryIndex);
                } catch (Exception e) {
                }
                break;
            }

        }
        stateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                if (i > 0) {
                    initializeCity();
                    if (stateSpinnerAdapter.getItem(i).getCities() != null && stateSpinnerAdapter.getItem(i).getCities().size() > 0)
                        cityDatumList.addAll(stateSpinnerAdapter.getItem(i).getCities());
                    citySpinnerAdapter.notifyDataSetChanged();
                    try {
                        districtSpinner.setSelectedItem(0);
                    } catch (Exception e) {

                    }
                } else {
                    try {
                        initializeCity();
                        citySpinnerAdapter.notifyDataSetChanged();
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onNothingSelected() {
            }
        });
        onClick();
        final String[] where1 = {AppConstants.CHEMICAL_UNIT.FARM_SEASON, AppConstants.CHEMICAL_UNIT.FARM_ADD_DYNAMIC};
        DropDownCacheManager.getInstance(context).getAllChemicalUnits(new DropDownFetchListener() {
            @Override
            public void onChemicalUnitLoaded(List<DropDownT> chemicalUnitList) {
                for (int i = 0; i < chemicalUnitList.size(); i++) {
                    Log.e(TAG, "getDataType() " + chemicalUnitList.get(i).getDataType() + "  ==>  " + chemicalUnitList.get(i).getData());
                    if (AppConstants.CHEMICAL_UNIT.FARM_ADD_DYNAMIC.equals(chemicalUnitList.get(i).getDataType())) {
                        if (chemicalUnitList.get(i).getData() != null) {
                            try {
                                CropFormResponse data = new Gson().fromJson(chemicalUnitList.get(i).getData(), CropFormResponse.class);
                                if (data.getData() != null)
                                    AddFarmActivity.this.cropFormDatumList.addAll(data.getData());

                                if (data.getSuperData() != null)
                                    AddFarmActivity.this.cropFormDatumListSuper.addAll(data.getSuperData());

                                if (cropFormDatumList != null && cropFormDatumListSuper != null) {
                                    if (cropFormDatumListSuper.size() == 0 && cropFormDatumList.size() == 0) {
                                        getFormData();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                getFormData();
                            }
                        } else {
                            getFormData();
                        }
                    } else if (AppConstants.CHEMICAL_UNIT.FARM_SEASON.equals(chemicalUnitList.get(i).getDataType())) {
                        //SEASON
                        if (chemicalUnitList.get(i).getData() != null) {
                            try {
                                JSONArray array = new JSONArray(chemicalUnitList.get(i).getData());
                                for (int j = 0; j < array.length(); j++) {
                                    JSONObject object = array.getJSONObject(j);
                                    Season data = new Gson().fromJson(object.toString(), Season.class);
                                    seasonDDList.add(data);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }, where1);
        final String[] where = {AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE_NEW, AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE_NEW,
                AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE_NEW, AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW,
                AppConstants.CHEMICAL_UNIT.CROPPING_PATTERN, AppConstants.CHEMICAL_UNIT.PLANTING_METHOD,
                AppConstants.CHEMICAL_UNIT.LAND_CATEGORY, AppConstants.CHEMICAL_UNIT.CIVIL_STATUS,

                AppConstants.CHEMICAL_UNIT.CASTE_CATEGORY, AppConstants.CHEMICAL_UNIT.CASTE,
                AppConstants.CHEMICAL_UNIT.RELIGION, AppConstants.CHEMICAL_UNIT.MOBILE_NETWORK_OPERATOR,
                AppConstants.CHEMICAL_UNIT.MOTOR_HP, AppConstants.CHEMICAL_UNIT.MICRO_IRRI_AWARENESS};
        DropDownCacheManager2.getInstance(context).getAllChemicalUnits(new DropDownFetchListener2() {
            @Override
            public void onChemicalUnitLoaded(List<DropDownT2> chemicalUnitList) {
                if (chemicalUnitList != null && chemicalUnitList.size() > 0) {
                    Log.e(TAG, "Spinner Data Offline " + new Gson().toJson(chemicalUnitList));
                    setOfflineSpinnerData(chemicalUnitList);
                    Log.e(TAG, "Spinner Data Online cache if where: " + where.length + " AND list size: " + chemicalUnitList.size());
                } else {

                    casteCategoryspinner.setVisibility(View.GONE);
                    casteSpinner.setVisibility(View.GONE);
                    religionSpinner.setVisibility(View.GONE);
                    networkSpinner.setVisibility(View.GONE);
                    mobLayout.setWeightSum(0.60f);
//                    motorHPSpinner.setVisibility(View.GONE);
                    microIrriAwarenessSpinner.setVisibility(View.GONE);

                    if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                        adapter = new AddFarmAdapter2(context, farmList, irrigationTypeDDList, irrigationSourceDDList, soilTypeDDList,
                                seasonDDList, farmCropList, plantingMethodDDList, isIrrigatedDDList, croppingPatternDDList,
                                cropFormDatumList, cropFormDatumList, new ArrayList<>(), AddFarmActivity.this);

                        adapter.setImagePickClickListener(new AddFarmAdapter2.OnImagePickClickListener() {
                            @Override
                            public void onImageClicked(int index, ImageType imageType) {
                                Log.e(TAG, "Index " + index + " type " + imageType.toString());
                                imageIndex = index;
                                if (imageType == ImageType.OWNERSHIP) {
                                    selectImage(CaptureConstants.CAPTURE5);
                                } else if (imageType == ImageType.FARM) {
                                    selectImage(CaptureConstants.CAPTURE4);
                                }

                            }
                        });

                        String country = countryCodePicker.getSelectedCountryName();
                        String compCountry = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_COUNTRY_ID);

                        for (int i = 0; i < countryDatumList.size(); i++) {

                            if (AppConstants.isValidString(compCountry) && compCountry.equals(countryDatumList.get(i).getId())) {
                                try {
                                    countryIndex = i + 1;
                                    adapter.setCountryIndex(countryIndex);
                                } catch (Exception e) {
                                }
                                break;
                            } else if (AppConstants.isValidString(country) && countryDatumList.get(i).getName() != null && country.trim().equals(countryDatumList.get(i).getName().trim())) {
                                try {
                                    countryIndex = i + 1;
                                    adapter.setCountryIndex(countryIndex);
                                } catch (Exception e) {
                                }
                                break;
                            }

                        }
                        if (compCountry != null && compCountry.trim().equals("63")) {
                            if (adapter != null)
                                adapter.setHideAddl2(true);
                        }
                        getFormData();
                        getAllDataNew(AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE_NEW);
                        getAllDataNew2(AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE_NEW);
                        getAllDataNew3(AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE_NEW);
                        getAllDataNew4(AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW);
                        getAllDataNew5(AppConstants.CHEMICAL_UNIT.CROPPING_PATTERN);
                        getAllDataNew6(AppConstants.CHEMICAL_UNIT.PLANTING_METHOD);
                        getAllDataNew7(AppConstants.CHEMICAL_UNIT.LAND_CATEGORY);
                        getAllDataNew8(AppConstants.CHEMICAL_UNIT.CIVIL_STATUS);
                    }

                    getSpinnerData();
                    Log.e(TAG, "Spinner Data Online cache else");
                }
            }
        }, where);


        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.FARMER)) {
            isFarmer = true;
            FarmData data = new FarmData();
            data.setIsOwned("Y");
            data.setMotorHpList(motorHpList);
            farmList.add(data);
            addFarmTv.setText(resources.getString(R.string.add_more_farm_label));
            farmDetailsLAyout.setVisibility(View.VISIBLE);
            existing_farmer_layout.setVisibility(View.GONE);
            new_farmer_layout.setVisibility(View.GONE);
            cluster_spinner_layout.setVisibility(View.GONE);
            addingToExistingFarmer = false;
            imgRelLay.setVisibility(View.GONE);

            supervisor_spinner_layout.setVisibility(View.VISIBLE);
            svAdapter = new SvSpinnerAdapter(context, supervisorDatumList);
            supervisor_spinner.setAdapter(svAdapter);
            supervisor_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(View view, int position, long id) {
                    try {
                        if (position < 0) {
                            supervisorDatum = null;
//                            Toast.makeText(context, "sup null" , Toast.LENGTH_SHORT).show();
                        } else {
                            supervisorDatum = svAdapter.getItem(supervisor_spinner.getSelectedPosition());
//                            Toast.makeText(context, "sup " + new Gson().toJson(supervisorDatum), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        supervisorDatum = null;
//                        Toast.makeText(context, "sup null" , Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onNothingSelected() {
//                    supervisorDatum = null;
                }
            });

        } else {
            if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR)) {
                supervisor_spinner_layout.setVisibility(View.GONE);
            } else {
                supervisor_spinner_layout.setVisibility(View.VISIBLE);

                svAdapter = new SvSpinnerAdapter(context, supervisorDatumList);
                supervisor_spinner.setAdapter(svAdapter);
                supervisor_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(View view, int position, long id) {
                        try {
                            if (position < 0) {
                                supervisorDatum = null;
//                            Toast.makeText(context, "sup null" , Toast.LENGTH_SHORT).show();
                            } else {
                                supervisorDatum = svAdapter.getItem(supervisor_spinner.getSelectedPosition());
//                            Toast.makeText(context, "sup " + new Gson().toJson(supervisorDatum), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            supervisorDatum = null;
//                        Toast.makeText(context, "sup null" , Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onNothingSelected() {
//                    supervisorDatum = null;
                    }
                });
            }


            areaInAcre = getIntent().getStringExtra("areaInAcre");
            mapZoom = getIntent().getStringExtra("mapZoom");
            latLngListCheckArea = DataHandler.newInstance().getLatLngsCheckArea();
            isFarmer = false;
            if (getIntent().getStringExtra(AppConstants.ADD_FARM.KEY) != null &&
                    getIntent().getStringExtra(AppConstants.ADD_FARM.KEY).equals(AppConstants.ADD_FARM.ADD_EXISTING_FARM)) {
                farmer_spinner.setVisibility(View.VISIBLE);
                selfSvFarmerCheckBox.setVisibility(View.GONE);

                ti_phone.setVisibility(View.VISIBLE);
                addingToExistingFarmer = true;
                phone_layout_inner.setVisibility(View.GONE);
                imgRelLay.setVisibility(View.GONE);
                networkSpinner.setVisibility(View.GONE);
                DropDownCacheManager.getInstance(context).getDataByType(new DropDownCacheManager.OnFarmerClusterFetchListener() {
                    @Override
                    public void onDataLoaded(DropDownT dropDown) {
                        Log.e(TAG, "FARMERCACHE " + new Gson().toJson(dropDown));
                        if (dropDown != null && AppConstants.isValidString(dropDown.getData()) && !dropDown.getData().trim().equals("[]")) {
                            try {
                                JSONArray array = new JSONArray(dropDown.getData());
                                List<FarmerSpinnerData> clusterList = new ArrayList<>();
                                for (int j = 0; j < array.length(); j++) {
                                    JSONObject object = array.getJSONObject(j);
                                    FarmerSpinnerData cluster = new Gson().fromJson(object.toString(), FarmerSpinnerData.class);
                                    clusterList.add(cluster);
                                }
                                setFarmerListData(clusterList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            getFarmerList();
                        }
                    }
                }, AppConstants.CHEMICAL_UNIT.FARMER_LIST);
            } else {
                farmer_spinner.setVisibility(View.GONE);

                if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.ADMIN) ||
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUB_ADMIN) ||
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPER_ADMIN)) {
                    isAdmin = true;

                    DropDownCacheManager.getInstance(context).getDataByType(new DropDownCacheManager.OnFarmerClusterFetchListener() {
                        @Override
                        public void onDataLoaded(DropDownT dropDown) {
                            Log.e(TAG, "Offline clusters " + new Gson().toJson(dropDown));

                            if (dropDown != null && AppConstants.isValidString(dropDown.getData()) && !dropDown.getData().trim().equals("[]")) {
                                try {
                                    JSONArray array = new JSONArray(dropDown.getData());
                                    List<Cluster> clusterList = new ArrayList<>();
                                    for (int j = 0; j < array.length(); j++) {
                                        JSONObject object = array.getJSONObject(j);
                                        Cluster cluster = new Gson().fromJson(object.toString(), Cluster.class);
                                        clusterList.add(cluster);
                                    }
                                    setClusterData(clusterList);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (!isOfflineMode) {
                                getClusterOfCompany();
                            }
                        }
                    }, AppConstants.CHEMICAL_UNIT.CLUSTER_LIST);
                    cluster_spinner_layout.setVisibility(View.VISIBLE);
                } else {
                    cluster_spinner_layout.setVisibility(View.GONE);
                    isAdmin = false;
                }
            }

        }
        ifscCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && isIfscCodeValid(s.toString())) {
                    Log.e(TAG, "IFSC valid");
                    getBankDetails(s.toString().toUpperCase());
                    farmerAndFarmDataCache.setIfscCode(s.toString());
                    updateCache();

                } else {
                    Log.e(TAG, "IFSC Not valid");
                    isValidIfsc = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        String preference = getCountryId(context);
        if (preference != null) {
            String l;
            Locale current = new Locale("", preference.toUpperCase());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                l = current.getCountry();
            } else {
                l = current.getCountry();
            }




            countryCodePicker.setCountryPreference(preference.toUpperCase());
//            Log.e(TAG, "Preferences " + preference + " l " + l + " co " + "loc" + ", code " + getCountryCode(context) + " DATA " + new Gson().toJson(current)+", selection:"+countryCodePicker.getSelectedCountryCodeWithPlus());
            try {
                countryCodePicker.setDefaultCountryUsingNameCode(preference);
            } catch (Exception e) {

            }


        }

        if (countryCodePicker.getSelectedCountryCode().trim().equals("91")) {
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(10);
            ti_et_phone2.setFilters(FilterArray);
            phoneNumberEt.setFilters(FilterArray);
            Log.e(TAG, "IF Country code 91, Code = " + countryCodePicker.getSelectedCountryCode());
        } else {
            Log.e(TAG, "IF Country code not 91, Code = " + countryCodePicker.getSelectedCountryCode());
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(15);
            ti_et_phone2.setFilters(FilterArray);
            phoneNumberEt.setFilters(FilterArray);
        }
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                if (countryCodePicker.getSelectedCountryCode().trim().equals("91")) {
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(10);
                    ti_et_phone2.setFilters(FilterArray);
                    phoneNumberEt.setFilters(FilterArray);
                    Log.e(TAG, "IF Country code 91, Code = " + countryCodePicker.getSelectedCountryCode());
                } else {
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(15);
                    ti_et_phone2.setFilters(FilterArray);
                    phoneNumberEt.setFilters(FilterArray);
                    Log.e(TAG, "IF Country code not 91, Code = " + countryCodePicker.getSelectedCountryCode());
                }
            }
        });
        AddFarmCCacheManager.getInstance(context).getCompRegData(new FetchFarmCacheListener() {
            @Override
            public void onGetAllCompRegData(List<AddFarmCache> addFarmCacheList) {
                if (addFarmCacheList == null || addFarmCacheList.size() == 0) {
                    addFarmCache = new AddFarmCache(new Gson().toJson(farmerAndFarmDataCache));
                    AddFarmCCacheManager.getInstance(context).addFarmCache(addFarmCache);
                } else {
                    try {
                        addFarmCache = addFarmCacheList.get(0);
                        farmerAndFarmDataCache = new Gson().fromJson(addFarmCache.getUserData(), FarmerAndFarmData.class);
                    } catch (Exception e) {
                    }
                    Log.e(TAG, "Farm Catche " + new Gson().toJson(farmerAndFarmDataCache));
                    if (farmerAndFarmDataCache != null) {
                        if (farmerAndFarmDataCache.getFarmDataList() != null && farmerAndFarmDataCache.getFarmDataList().size() > 0) {
                            farmList.addAll(farmerAndFarmDataCache.getFarmDataList());
                            if (farmDetailsLAyout.getVisibility() == View.GONE)
                                farmDetailsLAyout.setVisibility(View.VISIBLE);
                            addFarmTv.setText(resources.getString(R.string.add_more_farm_label));
                        }
                        setUserDataFromCache();

                    } else {
                        farmerAndFarmDataCache = new FarmerAndFarmData();
                    }
                }
            }
        });

        if (countryCodePicker.getSelectedCountryCode() != null && countryCodePicker.getSelectedCountryCode().trim().equals("63")) {
            ti_addL2.setVisibility(View.GONE);
            addressMobRel.setVisibility(View.GONE);
            countryCodePicker.setVisibility(View.GONE);
            if (adapter != null)
                adapter.setHideAddl2(true);
        }
        textChaneListenters();

        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    Log.e(TAG, "Supervisor list not found");
                } else {
                    SupervisorListResponse response = new Gson().fromJson(dropDown.getData(), SupervisorListResponse.class);

                    if (response != null && response.getData() != null && response.getData().size() > 0) {
                        supervisorDatumList.addAll(response.getData());
                        supervisorDatumListBackup.addAll(response.getData());

                        if (svAdapter != null)
                            svAdapter.notifyDataSetChanged();
                    }

                }
            }
        }, AppConstants.CHEMICAL_UNIT.SUPERVISOR_LIST);

    }

    public static String getCountryCode(@Nullable Context context) {
        if (context != null) {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                String countryCode = telephonyManager.getNetworkCountryIso();
                if (!TextUtils.isEmpty(countryCode)) {
                    return countryCode;
                }
            }
        }
        return Locale.getDefault().getCountry();
    }

    ArrayList<DDNew> casteCategoryList = new ArrayList<>();
    ArrayList<DDNew> casteList = new ArrayList<>();
    ArrayList<DDNew> religionList = new ArrayList<>();
    ArrayList<DDNew> mobileOperatorList = new ArrayList<>();
    ArrayList<DDNew> motorHpList = new ArrayList<>();
    ArrayList<DDNew> microIrriAwarenessList = new ArrayList<>();

    ArrayList<DDNew> civilStatusList = new ArrayList<>();

    private List<DDNew> plantingMethodDDList = new ArrayList<>();
    private List<DDNew> isIrrigatedDDList = new ArrayList<>();
    private List<DDNew> croppingPatternDDList = new ArrayList<>();

    private List<CropFormDatum> cropFormDatumList = new ArrayList<>();
    private List<CropFormDatum> cropFormDatumListSuper = new ArrayList<>();

    private void setOfflineSpinnerData(List<DropDownT2> chemicalUnitList) {
        casteCategoryList.clear();
        casteList.clear();
        religionList.clear();
        mobileOperatorList.clear();
        motorHpList.clear();
        microIrriAwarenessList.clear();
        civilStatusList.clear();
        ArrayList<DDNew> casteCategoryList = new ArrayList<>();
        ArrayList<DDNew> casteList = new ArrayList<>();
        ArrayList<DDNew> religionList = new ArrayList<>();
        ArrayList<DDNew> mobileOperatorList = new ArrayList<>();
        ArrayList<DDNew> motorHpList = new ArrayList<>();
        ArrayList<DDNew> microIrriAwarenessList = new ArrayList<>();

        List<DDNew> irrigationSourceList1 = new ArrayList<>();
        List<DDNew> irrigationTypeList1 = new ArrayList<>();
        List<DDNew> soilTypeList1 = new ArrayList<>();
        List<DDNew> cropList = new ArrayList<>();
        List<DDNew> civilStatusLis = new ArrayList<>();
        List<DDNew> plantingMethodDDList = new ArrayList<>();
        List<DDNew> isIrrigatedDDList = new ArrayList<>();
        List<DDNew> croppingPatternDDList = new ArrayList<>();
        for (int i = 0; i < chemicalUnitList.size(); i++) {
            Log.e(TAG, "getDataType() " + chemicalUnitList.get(i).getDataType() + "  ==>  " + chemicalUnitList.get(i).getData());
            if (AppConstants.CHEMICAL_UNIT.CASTE_CATEGORY.equals(chemicalUnitList.get(i).getDataType())) {
                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                        if (data.getData() != null)
                            casteCategoryList.addAll(data.getData());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (AppConstants.CHEMICAL_UNIT.CASTE.equals(chemicalUnitList.get(i).getDataType())) {
                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                        if (data.getData() != null)
                            casteList.addAll(data.getData());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (AppConstants.CHEMICAL_UNIT.RELIGION.equals(chemicalUnitList.get(i).getDataType())) {
                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                        if (data.getData() != null)
                            religionList.addAll(data.getData());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (AppConstants.CHEMICAL_UNIT.MOBILE_NETWORK_OPERATOR.equals(chemicalUnitList.get(i).getDataType())) {
                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                        if (data.getData() != null)
                            mobileOperatorList.addAll(data.getData());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (AppConstants.CHEMICAL_UNIT.MOTOR_HP.equals(chemicalUnitList.get(i).getDataType())) {
                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                        if (data.getData() != null) {
                            motorHpList.addAll(data.getData());
                            AddFarmActivity.this.motorHpList.addAll(data.getData());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (AppConstants.CHEMICAL_UNIT.MICRO_IRRI_AWARENESS.equals(chemicalUnitList.get(i).getDataType())) {
                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                        if (data.getData() != null)
                            microIrriAwarenessList.addAll(data.getData());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE_NEW.equals(chemicalUnitList.get(i).getDataType())) {
                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                        if (data.getData() != null)
                            irrigationSourceList1.addAll(data.getData());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE_NEW.equals(chemicalUnitList.get(i).getDataType())) {
                //IRRIGATION TYPE DD
                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                        if (data.getData() != null)
                            irrigationTypeList1.addAll(data.getData());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if (AppConstants.CHEMICAL_UNIT.PLANTING_METHOD.equals(chemicalUnitList.get(i).getDataType())) {
                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                        if (data.getData() != null)
                            plantingMethodDDList.addAll(data.getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if (AppConstants.CHEMICAL_UNIT.CROPPING_PATTERN.equals(chemicalUnitList.get(i).getDataType())) {
                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                        if (data.getData() != null)
                            croppingPatternDDList.addAll(data.getData());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if (AppConstants.CHEMICAL_UNIT.LAND_CATEGORY.equals(chemicalUnitList.get(i).getDataType())) {
                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                        if (data.getData() != null)
                            isIrrigatedDDList.addAll(data.getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if (AppConstants.CHEMICAL_UNIT.CIVIL_STATUS.equals(chemicalUnitList.get(i).getDataType())) {
                if (chemicalUnitList.get(i).getData() != null) {
                    try {
                        DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                        Log.e(TAG, "civilStatusList " + new Gson().toJson(data));

                        if (data.getData() != null)
                            civilStatusLis.addAll(data.getData());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if (AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE_NEW.equals(chemicalUnitList.get(i).getDataType())) {
                try {
                    DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                    if (data.getData() != null)
                        soilTypeList1.addAll(data.getData());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW.equals(chemicalUnitList.get(i).getDataType())) {
                //SOIL TYPE
                try {
                    DDResponseNew data = new Gson().fromJson(chemicalUnitList.get(i).getData(), DDResponseNew.class);
                    if (data.getData() != null)
                        cropList.addAll(data.getData());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
//        civilStatusList.add(new DDNew("0", "", resources.getString(R.string.select_civil_status)));
        civilStatusList.addAll(civilStatusLis);
        civilStatusSpinnerAdapter = new SpinnerAdapterNewDDValue(this, civilStatusList, resources.getString(R.string.select_civil_status));
        civil_status_spinner.setAdapter(civilStatusSpinnerAdapter);
        civil_status_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                try {
                    if (civilStatusSpinnerAdapter.getItem(civil_status_spinner.getSelectedPosition()).getParameters() != null &&
                            civilStatusSpinnerAdapter.getItem(civil_status_spinner.getSelectedPosition()).getParameters().toUpperCase().equalsIgnoreCase("M")) {
                        tiSpouse.setVisibility(View.VISIBLE);

                        tiSpouseAge.setVisibility(View.VISIBLE);


                        etSpouse.setText("");
                    } else {
                        tiSpouse.setVisibility(View.GONE);
                        tiSpouseAge.setVisibility(View.GONE);
                        etSpouseAge.setText("");
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        if (civilStatusLis == null || civilStatusLis.size() == 0) {
            getAllDataNew8(AppConstants.CHEMICAL_UNIT.CIVIL_STATUS);
        }

        if (casteCategoryList != null && casteCategoryList.size() > 0) {
            casteCategoryspinner.setVisibility(View.VISIBLE);
            this.casteCategoryList.addAll(casteCategoryList);
            casteCategoryAdapter = new SpinnerAdapterNewDD(this, this.casteCategoryList, resources.getString(R.string.select_caste_category));
            casteCategoryspinner.setAdapter(casteCategoryAdapter);
        } else {
            casteCategoryspinner.setVisibility(View.GONE);
        }

        if (microIrriAwarenessList != null && microIrriAwarenessList.size() > 0) {
            microIrriAwarenessSpinner.setVisibility(View.VISIBLE);
            this.microIrriAwarenessList.addAll(microIrriAwarenessList);
            microAgriSpinnerAdapter = new SpinnerAdapterNewDD(this, this.microIrriAwarenessList, resources.getString(R.string.select_mia));
            microIrriAwarenessSpinner.setAdapter(microAgriSpinnerAdapter);
        } else {
            microIrriAwarenessSpinner.setVisibility(View.GONE);
        }

        /*if (motorHpList!=null&&motorHpList.size()>0){
            motorHPSpinner.setVisibility(View.VISIBLE);
            this.motorHpList.addAll(motorHpList);
            motorHpSpinnerAdapter= new SpinnerAdapterNewDD(this, this.motorHpList, resources.getString(R.string.select_motor_hp));
            motorHPSpinner.setAdapter(motorHpSpinnerAdapter);
        }else {
            motorHPSpinner.setVisibility(View.GONE);
        }*/

        if (!addingToExistingFarmer && mobileOperatorList != null && mobileOperatorList.size() > 0) {
            networkSpinner.setVisibility(View.VISIBLE);
            this.mobileOperatorList.addAll(mobileOperatorList);
            networkAdapter = new SpinnerAdapterNewDD(this, this.mobileOperatorList, resources.getString(R.string.select_network));
            networkSpinner.setAdapter(networkAdapter);
        } else {
            networkSpinner.setVisibility(View.GONE);
            mobLayout.setWeightSum(0.60f);
        }

        if (religionList != null && religionList.size() > 0) {
            religionSpinner.setVisibility(View.VISIBLE);
            this.religionList.addAll(religionList);

            religionSpinnerAdapter = new SpinnerAdapterNewDD(this, this.religionList, resources.getString(R.string.select_religion));
            religionSpinner.setAdapter(religionSpinnerAdapter);
        } else {
            religionSpinner.setVisibility(View.GONE);
        }

        if (casteList != null && casteList.size() > 0) {
            casteSpinner.setVisibility(View.VISIBLE);
            this.casteList.addAll(casteList);

            casteSpinnerAdapter = new SpinnerAdapterNewDD(this, this.casteList, resources.getString(R.string.select_caste));
            casteSpinner.setAdapter(casteSpinnerAdapter);
        } else {
            casteSpinner.setVisibility(View.GONE);
        }

        setSpinnerData(irrigationSourceList1, irrigationTypeList1, soilTypeList1, null, cropList,
                isIrrigatedDDList, plantingMethodDDList, croppingPatternDDList, this.cropFormDatumList, this.cropFormDatumListSuper);


    }

    SpinnerAdapterNewDDValue civilStatusSpinnerAdapter;
    SpinnerAdapterNewDD casteCategoryAdapter, casteSpinnerAdapter, religionSpinnerAdapter, networkAdapter,
            motorHpSpinnerAdapter, microAgriSpinnerAdapter;
    String blockCharacterSet = "~#^|$%*!@/()-'\":;,?{}=!$^';,?×÷<>{}€£¥₩%~`¤♡♥_|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪:-);-):-D:-(:'(:O 01234567890";

    private void setUserDataFromCache() {
        if (farmerAndFarmDataCache.getName() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getName())) {
            farmerNameEt.setText(farmerAndFarmDataCache.getName());
        }
        if (farmerAndFarmDataCache.getPan() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getPan())) {
            ti_et_pan.setText(farmerAndFarmDataCache.getPan());
        }

        if (farmerAndFarmDataCache.getAadhaar() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getAadhaar())) {
            adhaarEt.setText(farmerAndFarmDataCache.getAadhaar());
        }

        if (farmerAndFarmDataCache.getMob() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getMob())) {
            phoneNumberEt.setText(farmerAndFarmDataCache.getMob());
        }
        /*if (farmerAndFarmDataCache.getDob() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getDob())) {
            dobEt.setText(farmerAndFarmDataCache.getDob());
        }*/
        if (farmerAndFarmDataCache.getCast() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getCast())) {
            ti_et_cast.setText(farmerAndFarmDataCache.getCast());
        }
        if (farmerAndFarmDataCache.getAnualIncome() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getAnualIncome())) {
            ti_et_income.setText(farmerAndFarmDataCache.getAnualIncome());
        }
        if (farmerAndFarmDataCache.getFather_name() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getFather_name())) {
            fatherNameEt.setText(farmerAndFarmDataCache.getFather_name());
        }
        if (farmerAndFarmDataCache.getAddL1() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getAddL1())) {
            etAddressLine1.setText(farmerAndFarmDataCache.getAddL1());
        }
        if (farmerAndFarmDataCache.getAddL2() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getAddL2())) {
            etAddressLine2.setText(farmerAndFarmDataCache.getAddL2());
        }
        if (farmerAndFarmDataCache.getVillage_or_city() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getVillage_or_city())) {
            villageOrCityEt.setText(farmerAndFarmDataCache.getVillage_or_city());
        }
        if (farmerAndFarmDataCache.getMandal_or_tehsil() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getMandal_or_tehsil())) {
            mandalOrTehsilEt.setText(farmerAndFarmDataCache.getMandal_or_tehsil());
        }
        if (farmerAndFarmDataCache.getDistrict() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getDistrict())) {
            districtEt.setText(farmerAndFarmDataCache.getDistrict());
        }
///////////////
        if (farmerAndFarmDataCache.getState() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getState())) {
            stateEt.setText(farmerAndFarmDataCache.getState());
        }
        if (farmerAndFarmDataCache.getCountry() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getCountry())) {
            countryEt.setText(farmerAndFarmDataCache.getCountry());
        }
        if (farmerAndFarmDataCache.getIfscCode() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getIfscCode())) {
            ifscCodeEt.setText(farmerAndFarmDataCache.getIfscCode());
        }
        if (farmerAndFarmDataCache.getBankName() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getBankName())) {
            bankNameEt.setText(farmerAndFarmDataCache.getBankName());
        }
        if (farmerAndFarmDataCache.getBranch() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getBranch())) {
            branchEt.setText(farmerAndFarmDataCache.getBranch());
        }
        if (farmerAndFarmDataCache.getAccountNumber() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getAccountNumber())) {
            accountEt.setText(farmerAndFarmDataCache.getAccountNumber());
        }
        if (farmerAndFarmDataCache.getHolderName() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getHolderName())) {
            accountHolderName.setText(farmerAndFarmDataCache.getHolderName());
        }

        if (farmerAndFarmDataCache.getAnimalCount() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getAnimalCount())) {
            ti_et_animal_count.setText(farmerAndFarmDataCache.getAnimalCount());
        }
        if (farmerAndFarmDataCache.getMilkInLitters() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getMilkInLitters())) {
            ti_et_milk_sale.setText(farmerAndFarmDataCache.getMilkInLitters());
        }
        if (farmerAndFarmDataCache.getMilkSalePrice() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getMilkSalePrice())) {
            ti_et_milk_sale_price.setText(farmerAndFarmDataCache.getMilkSalePrice());
        }
        if (farmerAndFarmDataCache.getIsOtherAnimal() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getIsOtherAnimal())) {
            ti_et_other_animal.setText(farmerAndFarmDataCache.getIsOtherAnimal());
        }

        if (farmerAndFarmDataCache.getGeder() != null) {
            if (farmerAndFarmDataCache.getGeder().trim().toUpperCase().equals("M")) {
                maleRadio.setChecked(true);
            } else if (farmerAndFarmDataCache.getGeder().trim().toUpperCase().equals("M")) {
                femaleRadio.setChecked(true);
            } else {
                otherRadio.setChecked(true);
            }
        }

        if (farmerAndFarmDataCache.getIsUsingPhone() != null) {
            if (farmerAndFarmDataCache.getIsUsingPhone().trim().toUpperCase().equals("Y")) {
                radioIsShareYes.setChecked(true);
                radioSPhoneNo.setChecked(false);
            } else {
                radioIsShareYes.setChecked(false);
                radioSPhoneNo.setChecked(true);
            }
        }
        if (farmerAndFarmDataCache.getIsShareHolder() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getIsShareHolder()) &&
                farmerAndFarmDataCache.getIsShareHolder().toUpperCase().equals("Y")) {
            radioIsShareYes.setChecked(true);
            if (farmerAndFarmDataCache.getShareJson() != null && farmerAndFarmDataCache.getShareJson().getCertificate() != null &&
                    !TextUtils.isEmpty(farmerAndFarmDataCache.getShareJson().getCertificate()) &&
                    farmerAndFarmDataCache.getShareJson().getCertificate().toUpperCase().equals("Y")) {
                radioShareCertYes.setChecked(true);

                ti_et_cert_value.setText(farmerAndFarmDataCache.getShareJson().getHowMuchValue());
                ti_et_cert_count.setText(farmerAndFarmDataCache.getShareJson().getHowManyCert());
            } else {
                radioShareCertNo.setChecked(true);
            }
        } else {
            radioIsShareNo.setChecked(true);
        }

    }

    private void textChaneListenters() {
        farmerNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setName(s.toString());
                if (!isAdmin) {
                    farmerAndFarmDataCache.setClusterId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
                }
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ti_et_pan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setPan(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phoneNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setMob(s.toString());
                if (!isAdmin) {
                    farmerAndFarmDataCache.setClusterId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
                }
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dobEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setDob(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ti_et_cast.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setCast(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ti_et_income.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setAnualIncome(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fatherNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setFather_name(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        adhaarEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setAadhaar(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etAddressLine1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setAddL1(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etAddressLine2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setAddL2(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        villageOrCityEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setVillage_or_city(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mandalOrTehsilEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setMandal_or_tehsil(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        districtEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setDistrict(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        stateEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setState(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        countryEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setCountry(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bankNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setBankName(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        branchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setBranch(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        accountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setAccountNumber(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        accountHolderName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setHolderName(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ti_et_cert_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.getShareJson().setHowManyCert(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ti_et_cert_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.getShareJson().setHowMuchValue(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        maleRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    farmerAndFarmDataCache.setGeder("M");
                    updateCache();
                }
            }
        });
        femaleRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    farmerAndFarmDataCache.setGeder("F");
                    updateCache();
                }
            }
        });
        otherRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    farmerAndFarmDataCache.setGeder("O");
                    updateCache();
                }
            }
        });


        ///
        ti_et_animal_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setAnimalCount(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ti_et_milk_sale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setMilkInLitters(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ti_et_milk_sale_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setMilkSalePrice(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ti_et_other_animal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                farmerAndFarmDataCache.setIsOtherAnimal(s.toString());
                updateCache();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       /* radioAnimalsYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    farmerAndFarmDataCache.setIsOtherAnimal("Y");
                    updateCache();
                }
            }
        });
        radioAnimalsNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    farmerAndFarmDataCache.setIsOtherAnimal("N");
                    updateCache();
                }
            }
        });*/
        radioSPhoneYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    farmerAndFarmDataCache.setIsSmartPhone("Y");
                    updateCache();
                }
            }
        });
        radioSPhoneYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    farmerAndFarmDataCache.setIsSmartPhone("N");
                    updateCache();
                }
            }
        });


    }

    private void updateCache() {

        if (addFarmCache == null)
            addFarmCache = new AddFarmCache(new Gson().toJson(farmerAndFarmDataCache));
        else
            addFarmCache.setUserData(new Gson().toJson(farmerAndFarmDataCache));
        AddFarmCCacheManager.getInstance(context).updateHarvestData(addFarmCache);
    }

    SpinnerAdapterCluster clusterAdapter;


    private void setClusterData(List<Cluster> list) {

        /*Cluster cluster = new Cluster();
        cluster.setClusterId("0");
        cluster.setClusterName(resources.getString(R.string.select_cluster_label));*/
        ArrayList<Cluster> clusterList = new ArrayList<>();

//        clusterList.add(cluster);
        clusterList.addAll(list);
        clusterAdapter = new SpinnerAdapterCluster(context, clusterList);
        cluster_spinner.setAdapter(clusterAdapter);
        if (farmerAndFarmDataCache != null && farmerAndFarmDataCache.getClusterId() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getClusterId())) {
            int selection = 0;
            Cluster cluster = null;
            for (int i = 0; i < clusterList.size(); i++) {
                if (clusterList.get(i).getClusterId().equals(farmerAndFarmDataCache.getClusterId())) {
                    selection = i;
                    cluster = clusterList.get(i);
                    break;
                }
            }
            if (cluster != null)
                cluster_spinner.setSelectedItem(cluster);
//            Toast.makeText(context, selection+" "+new Gson().toJson(cluster), Toast.LENGTH_SHORT).show();

        }

        cluster_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                if (i > 0) {
                    String clusterId = clusterAdapter.getItem(i).getClusterId();

                    farmerAndFarmDataCache.setClusterId(clusterId);
                    supervisorDatumList.clear();
                    if (svAdapter != null && supervisorDatumListBackup != null && supervisorDatumList != null) {
                        for (int j = 0; j < supervisorDatumListBackup.size(); j++) {
                            String clusterIdOfSv = supervisorDatumListBackup.get(j).getClusterId();
                            if (clusterId != null && clusterIdOfSv != null && clusterIdOfSv.trim().equals(clusterId.trim())) {
                                supervisorDatumList.add(supervisorDatumListBackup.get(j));
                            }

                        }
                        try {
                            svAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    updateCache();
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    private String getCountryId(Context c) {
        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        String networkCountry = tm.getNetworkCountryIso();
        return networkCountry;
    }

    public void animate(final View cardView) {
        if (cardView.getVisibility() == View.GONE) {
            Log.e(TAG, "Animate If");
            if (ADDRESS_LINE1 != null && !TextUtils.isEmpty(ADDRESS_LINE1))
                etAddressLine1.setText(ADDRESS_LINE1);
            if (ADDRESS_LINE2 != null && !TextUtils.isEmpty(ADDRESS_LINE2))
                etAddressLine2.setText(ADDRESS_LINE2);
            if (CITY != null && !TextUtils.isEmpty(CITY))
                villageOrCityEt.setText(CITY);
            if (STATE != null && !TextUtils.isEmpty(STATE))
                stateEt.setText(STATE);
            if (COUNTRY != null && !TextUtils.isEmpty(COUNTRY))
                countryEt.setText(COUNTRY);

            moreDetailsFarmerBtn.setText(resources.getString(R.string.hide_details_label));
            cardView.setFocusable(true);
            cardView.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
            animation.setDuration(500);
            cardView.setAnimation(animation);
            cardView.animate();
            animation.start();
            /*try {
                isMoreUserDetails = true;
                farmerAndFarmDataCache.setMoreUserDetails(isMoreUserDetails);
                updateCache();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        } else {
            Log.e(TAG, "Animate Else");
//            etAddressLine1.setText(null);
//            etAddressLine2.setText(null);
//            villageOrCityEt.setText(null);
//            stateEt.setText(null);
//            countryEt.setText(null);
            moreDetailsFarmerBtn.setText(resources.getString(R.string.add_more_details_label));
            cardView.setVisibility(View.INVISIBLE);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            animation.setDuration(500);
            cardView.setAnimation(animation);
            cardView.animate();
            animation.start();
            cardView.setFocusable(false);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    cardView.setVisibility(View.GONE);
                }
            }, 500);
            /*try {
                isMoreUserDetails = false;
                farmerAndFarmDataCache.setMoreUserDetails(isMoreUserDetails);
                updateCache();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tour_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_app_tour) {
            getAppTour();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void getAppTour() {
        Log.e(TAG, "app tour start" + "   farmerType " + addingToExistingFarmer);

        ShowcaseConfig config = new ShowcaseConfig(); //create the showcase config
        config.setDelay(0); //set the delay of each sequence using millis variable

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);
        sequence.setConfig(config);

        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.ADMIN) ||
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR) ||
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.CLUSTER_ADMIN) ||
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPER_ADMIN) ||
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUB_ADMIN)) {

            if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.ADMIN) ||
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPER_ADMIN) ||
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUB_ADMIN)) {
                if (addingToExistingFarmer) {
                    Log.e(TAG, "admin exist farm");
                    sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(this)
                                    .setSkipText(getResources().getString(R.string.tour_skip))
                                    .setTarget(findViewById(R.id.existing_farmer_layout))
                                    .setTitleText(getResources().getString(R.string.addfarm_tour_farmer_sel_title))
                                    .setDismissText(getResources().getString(R.string.got_it))
                                    .setContentText(getResources().getString(R.string.addfarm_tour_farmer_sel_content))
                                    .withRectangleShape()
                                    .build());

                    sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(this)
                                    .setSkipText(getResources().getString(R.string.tour_skip))
                                    .setTarget(findViewById(R.id.ti_name_of_farmer))
                                    .setTitleText(getResources().getString(R.string.addfarm_tour_farmer_detail_title))
                                    .setDismissText(getResources().getString(R.string.got_it))
                                    .setContentText(getResources().getString(R.string.addfarm_tour_farmer_detail_content))
                                    .withRectangleShape()
                                    .build());

                } else {
                    Log.e(TAG, "admin new farm");
                    sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(this)
                                    .setSkipText(getResources().getString(R.string.tour_skip))
                                    .setTarget(findViewById(R.id.cluster_spinner))
                                    .setTitleText(getResources().getString(R.string.addfarm_tour_clust_sel_title))
                                    .setDismissText(getResources().getString(R.string.got_it))
                                    .setContentText(getResources().getString(R.string.addfarm_tour_clust_sel_content))
                                    .withRectangleShape()
                                    .build());
                    sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(this)
                                    .setSkipText(getResources().getString(R.string.tour_skip))
                                    .setTarget(findViewById(R.id.ti_name_of_farmer))
                                    .setTitleText(getResources().getString(R.string.addfarm_tour_farmer_detail_title))
                                    .setDismissText(getResources().getString(R.string.got_it))
                                    .setContentText(getResources().getString(R.string.addfarm_tour_farmer_detail_content))
                                    .withRectangleShape()
                                    .build());

                    sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(this)
                                    .setSkipText(getResources().getString(R.string.tour_skip))
                                    .setTarget(findViewById(R.id.phone_layout_inner))
                                    .setTitleText(getResources().getString(R.string.addfarm_tour_contact_detail_title))
                                    .setDismissText(getResources().getString(R.string.got_it))
                                    .setContentText(getResources().getString(R.string.addfarm_tour_contact_details_content))
                                    .withRectangleShape()
                                    .build());
                }


            } else if ((SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR) ||
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.CLUSTER_ADMIN))) {
                if (addingToExistingFarmer) {
                    sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(this)
                                    .setSkipText(getResources().getString(R.string.tour_skip))
                                    .setTarget(findViewById(R.id.existing_farmer_layout))
                                    .setTitleText(getResources().getString(R.string.addfarm_tour_clust_sel_title))
                                    .setDismissText(getResources().getString(R.string.got_it))
                                    .setContentText(getResources().getString(R.string.addfarm_tour_clust_sel_content))
                                    .withRectangleShape()
                                    .build());

                    sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(this)
                                    .setSkipText(getResources().getString(R.string.tour_skip))
                                    .setTarget(findViewById(R.id.ti_name_of_farmer))
                                    .setTitleText(getResources().getString(R.string.addfarm_tour_farmer_detail_title))
                                    .setDismissText(getResources().getString(R.string.got_it))
                                    .setContentText(getResources().getString(R.string.addfarm_tour_farmer_detail_content))
                                    .withRectangleShape()
                                    .build());
                } else {
                    sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(this)
                                    .setSkipText(getResources().getString(R.string.tour_skip))
                                    .setTarget(findViewById(R.id.ti_name_of_farmer))
                                    .setTitleText(getResources().getString(R.string.addfarm_tour_farmer_detail_title))
                                    .setDismissText(getResources().getString(R.string.got_it))
                                    .setContentText(getResources().getString(R.string.addfarm_tour_farmer_detail_content))
                                    .withRectangleShape()
                                    .build());
                    sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(this)
                                    .setSkipText(getResources().getString(R.string.tour_skip))
                                    .setTarget(findViewById(R.id.phone_layout_inner))
                                    .setTitleText(getResources().getString(R.string.addfarm_tour_contact_detail_title))
                                    .setDismissText(getResources().getString(R.string.got_it))
                                    .setContentText(getResources().getString(R.string.addfarm_tour_contact_details_content))
                                    .withRectangleShape()
                                    .build());
                }
            }

            sequence.addSequenceItem(
                    new MaterialShowcaseView.Builder(this)
                            .setSkipText(getResources().getString(R.string.tour_skip))
                            .setTarget(findViewById(R.id.addFarmTv))
                            .setTitleText(getResources().getString(R.string.addfarm_tour_farm_detail_title))
                            .setDismissText(getResources().getString(R.string.got_it))
                            .setContentText(getResources().getString(R.string.addfarm_tour_farm_detail_content))
                            .withRectangleShape()
                            .build());
            sequence.addSequenceItem(
                    new MaterialShowcaseView.Builder(this)
                            .setSkipText(getResources().getString(R.string.tour_skip))
                            .setTarget(findViewById(R.id.tv_add_new_farm_submit_bt))
                            .setTitleText(getResources().getString(R.string.addfarm_tour_submit_title))
                            .setDismissText(getResources().getString(R.string.got_it))
                            .setContentText(getResources().getString(R.string.addfarm_tour_submit_content))
                            .withRectangleShape()
                            .build());
            sequence.start();
        } else if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.FARMER)) {
            sequence.addSequenceItem(
                    new MaterialShowcaseView.Builder(this)
                            .setSkipText(getResources().getString(R.string.tour_skip))
                            .setTarget(findViewById(R.id.ti_et_farmid))
                            .setDismissText(getResources().getString(R.string.got_it))
                            .setTitleText(getResources().getString(R.string.addfarm_tour_farmer_farmid_content))
                            .withRectangleShape()
                            .build());
            sequence.addSequenceItem(
                    new MaterialShowcaseView.Builder(this)
                            .setSkipText(getResources().getString(R.string.tour_skip))
                            .setTarget(findViewById(R.id.ti_et_farmarea))
                            .setDismissText(getResources().getString(R.string.got_it))
                            .setTitleText(getResources().getString(R.string.addfarm_tour_farmer_farmarea_content))
                            .withRectangleShape()
                            .build());
            sequence.addSequenceItem(
                    new MaterialShowcaseView.Builder(this)
                            .setSkipText(getResources().getString(R.string.tour_skip))
                            .setTarget(findViewById(R.id.cropSpinner))
                            .setDismissText(getResources().getString(R.string.got_it))
                            .setTitleText(getResources().getString(R.string.addfarm_tour_farmer_selcrop_content))
                            .withRectangleShape()
                            .build());
            sequence.addSequenceItem(
                    new MaterialShowcaseView.Builder(this)
                            .setSkipText(getResources().getString(R.string.tour_skip))
                            .setTarget(findViewById(R.id.seasonSpinner))
                            .setDismissText(getResources().getString(R.string.got_it))
                            .setTitleText(getResources().getString(R.string.addfarm_tour_farmer_selseason_content))
                            .withRectangleShape()
                            .build());
            sequence.addSequenceItem(
                    new MaterialShowcaseView.Builder(this)
                            .setSkipText(getResources().getString(R.string.tour_skip))
                            .setTarget(findViewById(R.id.addFarmTv))
                            .setDismissText(getResources().getString(R.string.got_it))
                            .setTitleText(getResources().getString(R.string.addfarm_tour_farmer_addmore_farm_content))
                            .withRectangleShape()
                            .build());
            sequence.addSequenceItem(
                    new MaterialShowcaseView.Builder(this)
                            .setSkipText(getResources().getString(R.string.tour_skip))
                            .setTarget(findViewById(R.id.tv_add_new_farm_submit_bt))
                            .setTitleText(getResources().getString(R.string.addfarm_tour_submit_title))
                            .setDismissText(getResources().getString(R.string.got_it))
                            .setContentText(getResources().getString(R.string.addfarm_tour_submit_content))
                            .withRectangleShape()
                            .build());
            sequence.start();

        }
    }

    @BindView(R.id.sv_profile_photo)
    CircleImageView sv_profile_photo;

    @BindView(R.id.idCircularImg)
    CircleImageView idCircularImg;

    @BindView(R.id.addressIdCircularImage)
    CircleImageView addressIdCircularImage;
    String userChoosenTask, pictureImagePath = "", pictureImagePathId = "", pictureImagePathAddress = "";
    Bitmap myBitmap, myBitmapId, myBitmapAddress;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    int imageIndex = -1;

    private void selectImage(final String onclick) {
        final CharSequence[] items = {resources.getString(R.string.image_take_photo),
                resources.getString(R.string.image_choose_from_library),
                resources.getString(R.string.image_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(resources.getString(R.string.add_photo_msg));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
//                boolean result = Utility.checkPermission(context);
//                boolean resultCam = checkCameraPermission(context);
//                boolean resultGal = checkStoragePermission(context);
                if (items[item].equals(resources.getString(R.string.image_take_photo))) {
                    userChoosenTask = "Take Photo";
                    if (checkCameraPermission(context))
                        cameraIntent(onclick);

                } else if (items[item].equals(resources.getString(R.string.image_choose_from_library))) {
                    userChoosenTask = "Choose from Library";
                    if (checkStoragePermission(context))
                        galleryIntent(onclick);

                } else if (items[item].equals(resources.getString(R.string.image_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //capure1=profile,capture2=id,=capture3=address
    private int SELECT_FILE1 = 1, SELECT_FILE2 = 2, SELECT_FILE3 = 3, SELECT_FILE4 = 4, SELECT_FILE5 = 5;
    private int REQUEST_CAMERA1 = 11, REQUEST_CAMERA2 = 12, REQUEST_CAMERA3 = 13, REQUEST_CAMERA4 = 14, REQUEST_CAMERA5 = 15;

    private void cameraIntent(String onclick) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = timeStamp + ".jpg";
//        Toast.makeText(context, "" + imageFileName, Toast.LENGTH_SHORT).show();
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        if (onclick.equals(CaptureConstants.CAPTURE1)) {
            startActivityForResult(intent, REQUEST_CAMERA1);
        } else if (onclick.equals(CaptureConstants.CAPTURE2)) {
            startActivityForResult(intent, REQUEST_CAMERA2);
        } else if (onclick.equals(CaptureConstants.CAPTURE3)) {
            startActivityForResult(intent, REQUEST_CAMERA3);
        } else if (onclick.equals(CaptureConstants.CAPTURE4)) {
            startActivityForResult(intent, REQUEST_CAMERA4);
        } else if (onclick.equals(CaptureConstants.CAPTURE5)) {
            startActivityForResult(intent, REQUEST_CAMERA5);
        }
    }

    private void galleryIntent(String onclick) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(android.provider.MediaStore.EXTRA_SIZE_LIMIT, "720000");
        intent.setType("image/*");
        //intent.putExtra("result",onclick);
        if (onclick.equals(CaptureConstants.CAPTURE1)) {
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file_msg)), SELECT_FILE1);
        } else if (onclick.equals(CaptureConstants.CAPTURE2)) {
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file_msg)), SELECT_FILE2);
        } else if (onclick.equals(CaptureConstants.CAPTURE3)) {
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file_msg)), SELECT_FILE3);
        } else if (onclick.equals(CaptureConstants.CAPTURE4)) {
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file_msg)), SELECT_FILE4);
        } else if (onclick.equals(CaptureConstants.CAPTURE5)) {
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file_msg)), SELECT_FILE5);
        }

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
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean checkCameraPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
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
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA
                    );

                    return false;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void requestCameraAndStoragePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA
        );

    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                SELECT_FILE);
    }


    private void galleryIntent() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.gallery_intent_title)), SELECT_FILE);

        //Create an Intent with action as ACTION_PICK
        Log.e(TAG, "Gallery Intent");
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, SELECT_FILE);
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

    String imgBase64 = "";
    String imgBase64IDProof = "";
    String imgBase64AddressProf = "";

    private void onCaptureImageResult(Intent data, String onclick) {
//        Bundle extras = data.getExtras();
        //String onclick=extras.getString("result");
        File imgFile = new File(pictureImagePath);
        if (imgFile.exists()) {
            if (onclick.equals(CaptureConstants.CAPTURE1)) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = getResizedBitmap(myBitmap, 720, 1080);
                sv_profile_photo.setImageBitmap(myBitmap);

                imgBase64 = compressBitmap(myBitmap, 2, 50);
            } else if (onclick.equals(CaptureConstants.CAPTURE2)) {
                myBitmapId = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmapId = getResizedBitmap(myBitmapId, 720, 1080);
                idCircularImg.setImageBitmap(myBitmapId);
                imgBase64IDProof = compressBitmap(myBitmapId, 2, 50);
            } else if (onclick.equals(CaptureConstants.CAPTURE3)) {
                myBitmapAddress = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmapAddress = getResizedBitmap(myBitmapAddress, 720, 1080);
                addressIdCircularImage.setImageBitmap(myBitmapAddress);
                imgBase64AddressProf = compressBitmap(myBitmapAddress, 2, 50);
            } else if (onclick.equals(CaptureConstants.CAPTURE4)) {
                Bitmap myBitmapAddress = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmapAddress = getResizedBitmap(myBitmapAddress, 720, 1080);
                farmList.get(imageIndex).setFarmImage(compressBitmap(myBitmapAddress, 2, 50));
                farmList.get(imageIndex).setFarmImagePath(pictureImagePath);
                if (farmImageEt != null)
                    farmImageEt.setText(pictureImagePath);
//                adapter.notifyDataSetChanged();
            } else if (onclick.equals(CaptureConstants.CAPTURE5)) {
                Bitmap myBitmapAddress = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmapAddress = getResizedBitmap(myBitmapAddress, 720, 1080);
                farmList.get(imageIndex).setOwnerShipDoc(compressBitmap(myBitmapAddress, 2, 50));
                farmList.get(imageIndex).setOwnerShipPath(pictureImagePath);
                if (ownershipImageEt != null)
                    ownershipImageEt.setText(pictureImagePath);
//                adapter.notifyDataSetChanged();
            }
        }
    }

    EditText ownershipImageEt, farmImageEt;

    private void onSelectFromGalleryResult(Intent data, String onclick) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        Log.e(TAG, "Selected Pic Path " + picturePath);
        cursor.close();
        if (onclick.equals(CaptureConstants.CAPTURE1)) {
            myBitmap = BitmapFactory.decodeFile(picturePath);
            myBitmap = getResizedBitmap(myBitmap, 720, 1080);
//            Bitmap scaledBitmap = scaleDown(realImage, MAX_IMAGE_SIZE, true);
            sv_profile_photo.setImageBitmap(myBitmap);
            imgBase64 = compressBitmap(myBitmap, 2, 50);
        } else if (onclick.equals(CaptureConstants.CAPTURE2)) {
            myBitmapId = BitmapFactory.decodeFile(picturePath);
            myBitmapId = getResizedBitmap(myBitmapId, 720, 1080);
            idCircularImg.setImageBitmap(myBitmapId);
            imgBase64IDProof = compressBitmap(myBitmapId, 2, 50);

        } else if (onclick.equals(CaptureConstants.CAPTURE3)) {
            myBitmapAddress = BitmapFactory.decodeFile(picturePath);
            myBitmapAddress = getResizedBitmap(myBitmapAddress, 720, 1080);
            addressIdCircularImage.setImageBitmap(myBitmapAddress);
            imgBase64AddressProf = compressBitmap(myBitmapAddress, 2, 50);

        } else if (onclick.equals(CaptureConstants.CAPTURE4)) {
            Bitmap myBitmapAddress = BitmapFactory.decodeFile(picturePath);
            myBitmapAddress = getResizedBitmap(myBitmapAddress, 720, 1080);
            farmList.get(imageIndex).setFarmImage(compressBitmap(myBitmapAddress, 2, 50));
            farmList.get(imageIndex).setFarmImagePath(picturePath);
            if (farmImageEt != null)
                farmImageEt.setText(picturePath);
//            adapter.notifyDataSetChanged();
        } else if (onclick.equals(CaptureConstants.CAPTURE5)) {
            Bitmap myBitmapAddress = BitmapFactory.decodeFile(picturePath);
            myBitmapAddress = getResizedBitmap(myBitmapAddress, 720, 1080);
            farmList.get(imageIndex).setOwnerShipDoc(compressBitmap(myBitmapAddress, 2, 50));
            farmList.get(imageIndex).setOwnerShipPath(picturePath);
            if (ownershipImageEt != null)
                ownershipImageEt.setText(picturePath);
//            adapter.notifyDataSetChanged();
        }
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

    private void onClick() {

        capture_image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(CaptureConstants.CAPTURE1);
            }
        });

        sv_profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(CaptureConstants.CAPTURE1);
            }
        });

        idCircularImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(CaptureConstants.CAPTURE2);
            }
        });

        addressIdCircularImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(CaptureConstants.CAPTURE3);
            }
        });

        moreDetailsFarmerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animate(extraFarmerDetailsLayout);
            }
        });
        initPersonDobDialog();
        initSpouseDobDialog();
        dobEtAge.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isAgeTouched = true;
                showDateDialog();
                return false;
            }
        });
        /*dobEtAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null && s.length() > 0 && dobEtAge.hasFocus() && isAgeTouched) {

                    dobEt.setText(AppConstants.getShowableDate(getDobFromAge(Integer.valueOf(s.toString())), context));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
        dobEt.setFormat(AppConstants.DATE_FORMAT_SERVER);
        dobEt.setOnDatePickedListener(new DateTimePickerEditText.OnDatePicked() {
            @Override
            public void onDatePicked(String date) {


                dobEt.setText(AppConstants.getShowableDate(date, context));
                dobEtAge.setText("" + getAge(date));
            }
        });

       /* dobEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAgeTouched = false;
                Calendar calendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener add_details_farm_flowering = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = AppConstants.DATE_FORMAT_SERVER; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        dobEt.setText(AppConstants.getShowableDate(sdf.format(calendar.getTime()), context));
                        dobEtAge.setText(getAge(year, monthOfYear, dayOfMonth));
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddFarmActivity.this, add_details_farm_flowering, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }
        });*/


        etSpouseAge.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isAgeTouched2 = true;
                showDateDialogSpouse();
                return false;
            }
        });
        /*etSpouseAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null && s.length() > 0 && etSpouseAge.hasFocus() && isAgeTouched2) {

                    etSpouseDob.setText(AppConstants.getShowableDate(getDobFromAge(Integer.valueOf(s.toString())), context));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        etSpouseDob.setFormat(AppConstants.DATE_FORMAT_SERVER);
        etSpouseDob.setOnDatePickedListener(new DateTimePickerEditText.OnDatePicked() {
            @Override
            public void onDatePicked(String date) {
                etSpouseDob.setText(AppConstants.getShowableDate(date, context));
                etSpouseAge.setText("" + getAge(date));

//                etSpouseDob.setText(AppConstants.getShowableDate(sdf.format(calendar.getTime()), context));
//                etSpouseAge.setText(getAge(year, monthOfYear, dayOfMonth));
            }
        });

        etSpouseDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAgeTouched2 = false;
                try {
                    if (etSpouseAge.hasFocus())
                        etSpouseAge.clearFocus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener add_details_farm_flowering = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = AppConstants.DATE_FORMAT_SERVER; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        etSpouseDob.setText(AppConstants.getShowableDate(sdf.format(calendar.getTime()), context));
                        etSpouseAge.setText(getAge(year, monthOfYear, dayOfMonth));
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddFarmActivity.this, add_details_farm_flowering, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }
        });


        tv_add_new_farm_submit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FarmerSpinnerData farmerdata = new FarmerSpinnerData();

                if (isFarmer) {
                    //Toasty.error(context, "Farmer Submitting", Toast.LENGTH_LONG).show();
                    if (supervisorDatum == null) {
                        Toasty.error(context, resources.getString(R.string.please_select_supervisor), Toast.LENGTH_LONG).show();
                    } else if (farmList != null && farmList.size() == 0) {
                        Toasty.error(context, resources.getString(R.string.plsease_select_atleast_one_farm), Toast.LENGTH_LONG).show();
                    } else {
                        addExistingFarmerFarm();
                    }

                } else if (isAdmin && cluster_spinner.getSelectedPosition() < 1) {
                    Toasty.error(context, resources.getString(R.string.please_select_a_cluser_msg), Toast.LENGTH_LONG).show();
                    cluster_spinner.getParent().requestChildFocus(cluster_spinner, cluster_spinner);
                } else if (farmerdata != null) {

                    /*if (farmerdata.getName().toLowerCase().trim().equals("select farmer")) {
                        Toasty.error(context, resources.getString(R.string.please_select_a_farmer_msg), Toast.LENGTH_LONG).show();
                        farmer_spinner.getParent().requestChildFocus(farmer_spinner, farmer_spinner);
                    } else */
                    {
                        if (addingToExistingFarmer || selfSvFarmerCheckBox.isChecked()) {
                            FarmerSpinnerData data = (FarmerSpinnerData) farmer_spinner.getSelectedItem();
                            if (!selfSvFarmerCheckBox.isChecked() && (data == null || data.getName().trim().toLowerCase().equals("select farmer") || farmer_spinner.getSelectedPosition() < 1)) {
                                farmer_spinner.getParent().requestChildFocus(farmer_spinner, farmer_spinner);
                                Toasty.error(context, resources.getString(R.string.please_select_a_farmer_msg), Toast.LENGTH_LONG).show();
                            } else if (!selfSvFarmerCheckBox.isChecked() && (supervisorDatum == null && !SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR))) {
                                Toasty.error(context, resources.getString(R.string.please_select_supervisor), Toast.LENGTH_LONG).show();
                            } else if (farmList != null && farmList.size() == 0) {
                                Toasty.error(context, resources.getString(R.string.plsease_select_atleast_one_farm), Toast.LENGTH_LONG).show();
                            } else {
                                addExistingFarmerFarm();
                            }
                        } else {
                            if (supervisorDatum == null && !SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR)) {
                                Toasty.error(context, resources.getString(R.string.please_select_supervisor), Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(farmerNameEt.getText().toString())) {
                                farmerNameEt.getParent().requestChildFocus(farmerNameEt, farmerNameEt);
                                Toasty.error(context, resources.getString(R.string.enter_farmer_name_msg), Toast.LENGTH_LONG).show();
                            } else if (farmerNameEt.getText().toString().length() < 3) {
                                farmerNameEt.getParent().requestChildFocus(farmerNameEt, farmerNameEt);
                                Toasty.error(context, resources.getString(R.string.invalid_farmer_name_msg), Toast.LENGTH_LONG).show();
                            } else if (TextUtils.isEmpty(phoneNumberEt.getText().toString())) {
                                phoneNumberEt.getParent().requestChildFocus(phoneNumberEt, phoneNumberEt);
                                Toasty.error(context, resources.getString(R.string.enter_farmer_contact_number_msg), Toast.LENGTH_LONG).show();
                            } else if (phoneNumberEt.getText().toString().length() < 4 || phoneNumberEt.getText().toString().equals("123456") || phoneNumberEt.getText().toString().equals("0123456")
                                    || phoneNumberEt.getText().toString().equals("012345") || phoneNumberEt.getText().toString().equals("1234567890") || phoneNumberEt.getText().toString().equals("0123456789")) {
                                phoneNumberEt.getParent().requestChildFocus(phoneNumberEt, phoneNumberEt);
                                Toasty.error(context, resources.getString(R.string.invalid_mobile_number_msg), Toast.LENGTH_LONG).show();
                            } else if (!TextUtils.isEmpty(fatherNameEt.getText().toString()) &&
                                    fatherNameEt.getText().toString().length() < 3) {
                                fatherNameEt.getParent().requestChildFocus(fatherNameEt, fatherNameEt);
                                Toasty.error(context, resources.getString(R.string.invalid_father_name_msg), Toast.LENGTH_LONG).show();
                            } /*else if (!TextUtils.isEmpty(adhaarEt.getText().toString()) && adhaarEt.getText().toString().trim().length() < 12) {
                                Toasty.error(context, resources.getString(R.string.invalid_aadhar_number_msg), Toast.LENGTH_LONG).show();
                            }*/ else if (farmList.size() == 0) {
                                Toasty.error(context, resources.getString(R.string.click_to_add_farm_msg), Toast.LENGTH_LONG).show();
                            } else if (!TextUtils.isEmpty(ifscCodeEt.getText().toString().trim()) && !isValidIfsc) {
                                Toasty.error(context, resources.getString(R.string.invalid_ifsc_code_msg), Toast.LENGTH_LONG).show();
                            } else if (isValidIfsc && TextUtils.isEmpty(accountEt.getText().toString())) {
                                Toasty.error(context, resources.getString(R.string.please_enter_account_number), Toast.LENGTH_LONG).show();
                            } else if (isValidIfsc && TextUtils.isEmpty(accountHolderName.getText().toString())) {
                                Toasty.error(context, resources.getString(R.string.please_enter_account_holder_name), Toast.LENGTH_LONG).show();
                            } else if (farmList != null && farmList.size() == 0) {
                                Toasty.error(context, resources.getString(R.string.click_to_add_farm_msg), Toast.LENGTH_LONG).show();
                            } else {
                                uploadFarmAndFarmerData();
                            }
                        }
                    }
                } else {

                    if (selfSvFarmerCheckBox.isChecked()) {
                        if (farmList != null && farmList.size() == 0) {
                            Toasty.error(context, resources.getString(R.string.plsease_select_atleast_one_farm), Toast.LENGTH_LONG).show();
                        } else {
                            addExistingFarmerFarm();
                        }
                    } else if (supervisorDatum == null && !SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR)) {
                        Toasty.error(context, resources.getString(R.string.please_select_supervisor), Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(farmerNameEt.getText().toString())) {
                        farmerNameEt.getParent().requestChildFocus(farmerNameEt, farmerNameEt);
                        Toasty.error(context, resources.getString(R.string.enter_farmer_name_msg), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Farmer name cannot be empty");
                    } else if (farmerNameEt.getText().toString().length() < 3) {
                        farmerNameEt.getParent().requestChildFocus(farmerNameEt, farmerNameEt);
                        Toasty.error(context, resources.getString(R.string.invalid_farmer_name_msg), Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(phoneNumberEt.getText().toString())) {
                        phoneNumberEt.getParent().requestChildFocus(phoneNumberEt, phoneNumberEt);
                        Toasty.error(context, resources.getString(R.string.enter_farmer_contact_number_msg), Toast.LENGTH_LONG).show();
                    } else if (phoneNumberEt.getText().toString().length() < 4 || phoneNumberEt.getText().toString().equals("123456") || phoneNumberEt.getText().toString().equals("0123456")
                            || phoneNumberEt.getText().toString().equals("012345") || phoneNumberEt.getText().toString().equals("1234567890") || phoneNumberEt.getText().toString().equals("0123456789")) {
                        phoneNumberEt.getParent().requestChildFocus(phoneNumberEt, phoneNumberEt);
                        Toasty.error(context, resources.getString(R.string.invalid_mobile_number_msg), Toast.LENGTH_LONG).show();
                    } else if (!TextUtils.isEmpty(fatherNameEt.getText().toString()) &&
                            fatherNameEt.getText().toString().length() < 3) {
                        fatherNameEt.getParent().requestChildFocus(fatherNameEt, fatherNameEt);
                        Toasty.error(context, resources.getString(R.string.invalid_father_name_msg), Toast.LENGTH_LONG).show();
                    } /*else if (!TextUtils.isEmpty(adhaarEt.getText().toString()) && adhaarEt.getText().toString().trim().length() < 12) {
                        Toasty.error(context, resources.getString(R.string.invalid_aadhar_number_msg), Toast.LENGTH_LONG).show();
                    }*/ else if (farmList.size() == 0) {
                        Toasty.error(context, resources.getString(R.string.click_to_add_farm_msg), Toast.LENGTH_LONG).show();
                    } else if (!TextUtils.isEmpty(ifscCodeEt.getText().toString().trim()) && !isValidIfsc) {
                        Toasty.error(context, resources.getString(R.string.invalid_ifsc_code_msg), Toast.LENGTH_LONG).show();
                    } else if (isValidIfsc && TextUtils.isEmpty(accountEt.getText().toString())) {
                        Toasty.error(context, resources.getString(R.string.please_enter_account_number), Toast.LENGTH_LONG).show();
                    } else if (isValidIfsc && TextUtils.isEmpty(accountHolderName.getText().toString())) {
                        Toasty.error(context, resources.getString(R.string.please_enter_account_holder_name), Toast.LENGTH_LONG).show();
                    } else if (farmList != null && farmList.size() == 0) {
                        Toasty.error(context, resources.getString(R.string.click_to_add_farm_msg), Toast.LENGTH_LONG).show();
                    } else {
                        //Toasty.error(context, "Data Upload 2", Toast.LENGTH_LONG).show();
                        uploadFarmAndFarmerData();

                    }
                }
            }
        });

        addFarmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSpinnerDataLoaded) {
                    if (farmList.size() > 0) {
                        Log.e(TAG, "if List Size " + farmList.size() + " Data " + new Gson().toJson(farmList));
                        FarmData farmData = farmList.get(farmList.size() - 1);
                        if (farmData.getId() == null || TextUtils.isEmpty(farmData.getId())) {
                            Toasty.error(context, resources.getString(R.string.enter_crop_id_msg), Toast.LENGTH_LONG).show();
                            adapter.setErrorAndFocus(farmList.size() - 1, "id", resources.getString(R.string.error_field_required));
                        } else if (farmData.getArea() == null || TextUtils.isEmpty(farmData.getArea())) {
                            Toasty.error(context, resources.getString(R.string.enter_farm_area_msg), Toast.LENGTH_LONG).show();
                            adapter.setErrorAndFocus(farmList.size() - 1, "area", resources.getString(R.string.error_field_required));
                        } else if (farmData.getSeason() == null || farmData.getSeason().trim().equals("0")) {
                            Toasty.error(context, resources.getString(R.string.select_season_msg), Toast.LENGTH_LONG).show();
                            adapter.setErrorAndFocus(farmList.size() - 1, "season", resources.getString(R.string.error_field_required));
                        } else if (farmData.getCropId() == null || TextUtils.isEmpty(farmData.getCropId()) || farmData.getCropId().trim().equals("0")) {
                            Toasty.error(context, resources.getString(R.string.select_crop_for_farm_msg), Toast.LENGTH_LONG).show();
                            adapter.setErrorAndFocus(farmList.size() - 1, "crop", resources.getString(R.string.error_field_required));
                        } else {
                            Log.e("Adapter", " else List Size " + farmList.size() + " Data " + new Gson().toJson(farmList));
                            FarmData data = new FarmData();
                            data.setIsOwned("Y");
                            data.setMotorHpList(motorHpList);
                            farmList.add(data);
                            adapter.notifyData(farmList);
//                            adapter.notifyDataSetChanged();
//                            adapter.notifyItemInserted(farmList.size()-1);
//                            adapter.add(new FarmData());
                            //adapter.addItem(new FarmData());
                            Log.e("Adapter", " else List Size new " + farmList.size() + " Data " + new Gson().toJson(farmList));
                        }

                    } else {
                        Log.e("Adapter", " else List Size " + farmList.size() + " Data " + new Gson().toJson(farmList));
                        FarmData data = new FarmData();
                        data.setIsOwned("Y");
                        data.setMotorHpList(motorHpList);
                        farmList.add(data);
                        adapter.notifyData(farmList);

                        if (latLngListCheckArea != null && latLngListCheckArea.size() > 2) {
                            addFarmTv.setVisibility(View.GONE);
                        }
//                        adapter.notifyItemInserted(farmList.size()-1);
//                        adapter.notifyDataSetChanged();
                        //adapter.addItem(new FarmData());
//                        adapter.add(new FarmData());
                        Log.e("Adapter", " else List Size new " + farmList.size() + " Data " + new Gson().toJson(farmList));
                    }


                    if (farmDetailsLAyout.getVisibility() == View.GONE)
                        farmDetailsLAyout.setVisibility(View.VISIBLE);
                    addFarmTv.setText(resources.getString(R.string.add_more_farm_label));
                } else {
                    Toasty.error(context, "Something went wrong, Please try again later", Toast.LENGTH_LONG).show();
                }
            }
        });


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(60000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Criteria criteria = new Criteria();
        Log.e(TAG, "Best Criteria Provider " + locationManager.getBestProvider(criteria, false));
        mprovider = locationManager.getBestProvider(criteria, false);

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


      /*  if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            if (isGPSEnabled) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.e(TAG, "Gps provider");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 1, this);
            } else if (isNetworkEnabled) {
                Log.e(TAG, "Network provider");
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 1, this);
            } else {
                Log.e(TAG, "No provider");
            }
        }*/
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }


        //Share
        radioIsShareYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    shareCertLayout.setVisibility(View.VISIBLE);
                    farmerAndFarmDataCache.setIsShareHolder("Y");
                    updateCache();
                }
            }
        });
        radioIsShareNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    shareCertLayout.setVisibility(View.GONE);
                    farmerAndFarmDataCache.setIsShareHolder("N");
                    updateCache();
                }
            }
        });
        radioShareCertYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    shareCertLay2.setVisibility(View.VISIBLE);
                    farmerAndFarmDataCache.getShareJson().setCertificate("Y");
                    updateCache();
                }
            }
        });
        radioShareCertNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    shareCertLay2.setVisibility(View.GONE);
                    farmerAndFarmDataCache.getShareJson().setCertificate("N");
                    updateCache();
                }
            }
        });
    }

    boolean isAgeTouched = false;
    boolean isAgeTouched2 = false;

    private String getDobFromAge(int age) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(Calendar.YEAR, (today.get(Calendar.YEAR) - age));

        int yr = today.get(Calendar.YEAR) - age;


        String ageS = yr + "-01-01";

        return ageS;
    }

    private int getAge(String dobString) {

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.DATE_FORMAT_SERVER);
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month + 1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        return age;
    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    private void uploadFarmAndFarmerData() {
        //progressDialog.show();
        tv_add_new_farm_submit_bt.setClickable(false);
        tv_add_new_farm_submit_bt.setEnabled(false);
        progressBar_image.setVisibility(View.VISIBLE);
        FarmerAndFarmData farmData = new FarmerAndFarmData();
        String farmerName = "";
        String villageOrCity = "";
        String mandalOrTehsil = "";

        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR)) {
            farmData.setSvId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
//            farmData.setSvName(supervisorDatum.getName());
        } else {
            if (supervisorDatum != null) {
                farmData.setSvId(supervisorDatum.getPersonId());
                farmData.setSvName(supervisorDatum.getName());
            }
        }

        String districtId = "0";
        try {
            districtId = citySpinnerAdapter.getItem(districtSpinner.getSelectedPosition()).getName();
            Log.e(TAG, "Uploading Farm And Farmer Data " + districtSpinner.getSelectedPosition() + "  " + new Gson().toJson(citySpinnerAdapter.getItem(districtSpinner.getSelectedPosition())));
        } catch (Exception e) {

        }
        String stateId = "0";
        try {
            stateId = stateSpinnerAdapter.getItem(stateSpinner.getSelectedPosition()).getName();
            Log.e(TAG, "Uploading Farm And Farmer Data " + stateSpinner.getSelectedPosition() + "  " + new Gson().toJson(stateSpinnerAdapter.getItem(stateSpinner.getSelectedPosition()).getName()));
        } catch (Exception e) {

        }
        String countryId = "0";
        try {
            countryId = countrySpinnerAdapter.getItem(countrySpinner.getSelectedPosition()).getName();
            Log.e(TAG, "Uploading Farm And Farmer Data " + countrySpinner.getSelectedPosition() + "  " + new Gson().toJson(countrySpinnerAdapter.getItem(countrySpinner.getSelectedPosition()).getName()));
        } catch (Exception e) {

        }
        String bankName = "";
        String accountNumber = "";
        String holderName = "";
        String branch = "";
        String ifscCode = "";
        String addL1 = "";
        String addL2 = "";
        String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();

        String financialStatus = null;
        String literacy = null;

        if (illiterateRadio.isChecked()) {
            literacy = "I";
        } else if (literateRadio.isChecked()) {
            literacy = "L";
        }
        if (aplRadio.isChecked()) {
            financialStatus = "APL";
        } else if (bplRadio.isChecked()) {
            financialStatus = "BPL";
        }
        farmData.setFinancial_status(financialStatus);
        farmData.setLiteracy(literacy);

        String isShareHolder = "";//y or n
        String isCert = "";//y or n
        String howManyCert = "";
        String howMuchValue = "";
        String img_link = "";
        String is_owned = "";
        String upi = etUpi.getText().toString().trim();
        String swiftCode = etSwiftCode.getText().toString().trim();
        String animalCount = "0";
        animalCount = ti_et_animal_count.getText().toString();
        String milkSalePrice = "0";
        milkSalePrice = ti_et_milk_sale_price.getText().toString();
        String milkInLitters = "0";
        milkInLitters = ti_et_milk_sale.getText().toString();
        String isOtherAnimal = "N";
        Share share = null;

        if (radioSPhoneYes.isChecked()) {
            farmData.setIsSmartPhone("Y");
        } else {
            farmData.setIsSmartPhone("N");
        }
        farmData.setCountryId(countryId);
        farmData.setState(stateId);
        farmData.setCityId(districtId);

        isOtherAnimal = ti_et_other_animal.getText().toString();
        farmData.setSwiftCode(swiftCode);
        if (!TextUtils.isEmpty(ti_et_phone_address.getText().toString()))
            farmData.setMob2(ti_et_phone_address.getText().toString().trim());
        if (!TextUtils.isEmpty(emailEt.getText().toString()))
            farmData.setEmail(emailEt.getText().toString().trim());

//        farmData.setCast(etCast.getText().toString().trim());
//        farmData.setReligion(etReligion.getText().toString().trim());

        if (!TextUtils.isEmpty(familyCount.getText().toString()))
            farmData.setFamily_mem_count(familyCount.getText().toString().trim());
        if (!TextUtils.isEmpty(adultCount.getText().toString()))
            farmData.setAdults_count(adultCount.getText().toString().trim());
        if (!TextUtils.isEmpty(kidsCount.getText().toString()))
            farmData.setKids_count(kidsCount.getText().toString().trim());
        if (!TextUtils.isEmpty(dependentCount.getText().toString()))
            farmData.setDependent_count(dependentCount.getText().toString().trim());

        farmData.setAnimalCount(animalCount);
        farmData.setMilkSalePrice(milkSalePrice);
        farmData.setMilkInLitters(milkInLitters);
        farmData.setIsOtherAnimal(isOtherAnimal);
        farmData.setSpouse_name(etSpouse.getText().toString().trim());
        farmData.setUpi_id(upi);
        farmData.setBeneficiary_name(beneficiaryEt.getText().toString().trim());
        try {
            DDNew ddNew = civilStatusSpinnerAdapter.getItem(civil_status_spinner.getSelectedPosition());
            farmData.setCivil_status(ddNew.getParameters());

        } catch (Exception e) {

        }
        CowAndCatles cowAndCatles = new CowAndCatles(animalCount, milkInLitters, milkSalePrice, isOtherAnimal);
        farmData.setCowAndCatles(cowAndCatles);
        if (radioIsShareYes.isChecked()) {
            farmData.setIsShareHolder("Y");
        } else {
            farmData.setIsShareHolder("N");
        }
        if (radioIsShareYes.isChecked() && radioShareCertYes.isChecked()) {
            howManyCert = ti_et_cert_count.getText().toString().trim();
            howMuchValue = ti_et_cert_value.getText().toString().trim();
            share = new Share("Y", howManyCert, howMuchValue);
        }
        String gender = null;
        if (maleRadio.isChecked()) {
            gender = "M";

        } else if (femaleRadio.isChecked()) {
            gender = "F";
        }
        farmData.setShareJson(share);
        String dob = dobEt.getText().toString();
        String anualIncome = ti_et_income.getText().toString();
        farmerName = farmerNameEt.getText().toString();
        String fatherName = fatherNameEt.getText().toString();
        String phoneNumber = phoneNumberEt.getText().toString();
        String addharNumber = "";
        String pan = "";
        if (!TextUtils.isEmpty(ti_et_pan.getText().toString()))
            pan = ti_et_pan.getText().toString();

        if (!TextUtils.isEmpty(etAddressLine1.getText().toString()))
            addL1 = etAddressLine1.getText().toString();
        if (!TextUtils.isEmpty(etAddressLine2.getText().toString()))
            addL2 = etAddressLine2.getText().toString();
        if (!TextUtils.isEmpty(adhaarEt.getText().toString()))
            addharNumber = adhaarEt.getText().toString();
        if (!TextUtils.isEmpty(villageOrCityEt.getText().toString()))
            villageOrCity = villageOrCityEt.getText().toString();
        if (!TextUtils.isEmpty(mandalOrTehsilEt.getText().toString()))
            mandalOrTehsil = mandalOrTehsilEt.getText().toString();
        /*if (!TextUtils.isEmpty(districtEt.getText().toString()))
            district = districtEt.getText().toString();
        if (!TextUtils.isEmpty(stateEt.getText().toString()))
            state = stateEt.getText().toString();
        if (!TextUtils.isEmpty(countryEt.getText().toString()))
            country = countryEt.getText().toString();*/
        if (!TextUtils.isEmpty(bankNameEt.getText().toString()))
            bankName = bankNameEt.getText().toString();
        if (!TextUtils.isEmpty(accountEt.getText().toString()))
            accountNumber = accountEt.getText().toString();
        if (!TextUtils.isEmpty(accountHolderName.getText().toString()))
            holderName = accountHolderName.getText().toString();
        if (!TextUtils.isEmpty(branchEt.getText().toString()))
            branch = branchEt.getText().toString();
        if (!TextUtils.isEmpty(ifscCodeEt.getText().toString()))
            ifscCode = ifscCodeEt.getText().toString();
        for (int i = 0; i < farmList.size(); i++) {
            FarmData farmDataa = farmList.get(i);
            Log.e(TAG, "Farm at " + i + " data " + new Gson().toJson(farmDataa));
            if (farmDataa.getId() == null || TextUtils.isEmpty(farmDataa.getId())) {
                Toasty.error(context, resources.getString(R.string.enter_farm_id_msg), Toast.LENGTH_LONG).show();
                adapter.setErrorAndFocus(i, "id", resources.getString(R.string.error_field_required));
                // progressDialog.dismiss();
                progressBar_image.setVisibility(View.GONE);
                tv_add_new_farm_submit_bt.setClickable(true);
                tv_add_new_farm_submit_bt.setEnabled(true);
                return;

            } else if (farmDataa.getArea() == null || TextUtils.isEmpty(farmDataa.getArea())) {
                Toasty.error(context, resources.getString(R.string.enter_farm_area_msg), Toast.LENGTH_LONG).show();
                adapter.setErrorAndFocus(i, "area", resources.getString(R.string.error_field_required));
                progressBar_image.setVisibility(View.GONE);
                tv_add_new_farm_submit_bt.setClickable(true);
                tv_add_new_farm_submit_bt.setEnabled(true);
                return;

            } else if (farmDataa.getSeason() == null || farmDataa.getSeason().trim().equals("0")) {
                Toasty.error(context, resources.getString(R.string.select_season_msg), Toast.LENGTH_LONG).show();
                adapter.setErrorAndFocus(i, "season", resources.getString(R.string.error_field_required));
                progressBar_image.setVisibility(View.GONE);
                tv_add_new_farm_submit_bt.setClickable(true);
                tv_add_new_farm_submit_bt.setEnabled(true);
                return;
            } else if (farmDataa.getCropId() == null || TextUtils.isEmpty(farmDataa.getCropId()) || farmDataa.getCropId().trim().equals("0")) {
                Toasty.error(context, resources.getString(R.string.select_crop_for_farm_msg), Toast.LENGTH_LONG).show();
                adapter.setErrorAndFocus(i, "crop", resources.getString(R.string.error_field_required));
                progressBar_image.setVisibility(View.GONE);
                tv_add_new_farm_submit_bt.setClickable(true);
                tv_add_new_farm_submit_bt.setEnabled(true);
                return;

            } else if (isPreviousCropMandatory && (farmDataa.getPreviouscrop() == null || TextUtils.isEmpty(farmDataa.getPreviouscrop()) || farmDataa.getPreviouscrop().trim().equals("0"))) {
                Toasty.error(context, resources.getString(R.string.select_previous_crop_msg), Toast.LENGTH_LONG).show();
                adapter.setErrorAndFocus(i, "cropP", resources.getString(R.string.error_field_required));
                progressBar_image.setVisibility(View.GONE);
                tv_add_new_farm_submit_bt.setClickable(true);
                tv_add_new_farm_submit_bt.setEnabled(true);
                return;
            }
        }
        String clusterId;
        if (isAdmin) {
            if (clusterAdapter != null) {
                Cluster cluster = clusterAdapter.getItem(cluster_spinner.getSelectedPosition());
                if (cluster != null)
                    clusterId = cluster.getClusterId();
                else
                    clusterId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID).split(",")[0];
            } else {
                clusterId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID).split(",")[0];
            }
        } else {
            clusterId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID);
        }
        farmData.setPan(pan);
        if (AppConstants.isValidString(dob))
            farmData.setDob(AppConstants.getUploadableDate(dob, context));
        farmData.setAnualIncome(anualIncome);
        farmData.setGeder(gender);
        farmData.setImage(imgBase64);
        farmData.setAddL1(addL1);
        farmData.setAddL2(addL2);
        farmData.setName(farmerName);
        farmData.setFather_name(fatherName);
        farmData.setMob(phoneNumber);
        farmData.setAadhaar(addharNumber);
        farmData.setVillage_or_city(villageOrCity);
        farmData.setMandal_or_tehsil(mandalOrTehsil);
        farmData.setDistrict(districtId);
        farmData.setCountryCode(countryCode);
        farmData.setState(stateId);
        farmData.setCountry(countryId);
        farmData.setBankName(bankName);
        farmData.setAccountNumber(accountNumber);
        farmData.setHolderName(holderName);
        farmData.setBranch(branch);
        farmData.setIfscCode(ifscCode);
        farmData.setFarmDataList(farmList);
        farmData.setAdded_by(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
        farmData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        farmData.setClusterId(clusterId);
        farmData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        farmData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        String casteCategory = null, caste = null, religion = null, operator = null, motorHp = null, microIrri = null;
        if (casteCategoryAdapter != null) {
            DDNew castCat = casteCategoryAdapter.getItem(casteCategoryspinner.getSelectedPosition());
            if (castCat != null) {
                casteCategory = castCat.getId();
            }
        }
        if (casteSpinnerAdapter != null) {
            DDNew castCat = casteSpinnerAdapter.getItem(casteSpinner.getSelectedPosition());
            if (castCat != null) {
                caste = castCat.getId();
            }
        }
        if (religionSpinnerAdapter != null) {
            DDNew castCat = religionSpinnerAdapter.getItem(religionSpinner.getSelectedPosition());

            if (castCat != null) {
                religion = castCat.getId();
            }
        }
        if (networkAdapter != null) {
            DDNew castCat = networkAdapter.getItem(networkSpinner.getSelectedPosition());

            if (castCat != null) {
                operator = castCat.getId();
            }
        }
        /*if (motorHpSpinnerAdapter!=null){
            DDNew castCat=motorHpSpinnerAdapter.getItem(motorHPSpinner.getSelectedPosition());
            if (castCat!=null){
                motorHp=castCat.getId();
            }
        }*/
        if (microAgriSpinnerAdapter != null) {
            DDNew castCat = microAgriSpinnerAdapter.getItem(microIrriAwarenessSpinner.getSelectedPosition());
            if (castCat != null) {
                microIrri = castCat.getId();
            }
        }
        farmData.setCasteCategoryId(casteCategory);
        farmData.setCasteCategoryId(caste);
        farmData.setReligionId(religion);
        farmData.setMobileNetworkId(operator);
        farmData.setMotorhpId(motorHp);
        farmData.setMiaId(microIrri);

        if (selectedPersonDateSpouse == SELECTED_TYPE.DOB && AppConstants.isValidString(personDobStrSpouse)) {
            farmData.setSpouseDob(AppConstants.getUploadableDate(personDobStrSpouse, context));
        } else if (selectedPersonDateSpouse == SELECTED_TYPE.AGE && AppConstants.isValidString(personDobStrSpouse)) {
            farmData.setSpouseDob(personDobStrSpouse.trim() + "-01-01");
        } else if (selectedPersonDateSpouse == SELECTED_TYPE.DOB && AppConstants.isValidString(personDobStrSpouse)) {
            farmData.setSpouseDob(personDobStrSpouse.trim() + "-01-01");
        }
//                farmData.put("dob", farmerAndFarmData.getDob());

        if (selectedPersonDate == SELECTED_TYPE.DOB && AppConstants.isValidString(personDobStr)) {
            farmData.setDob(AppConstants.getUploadableDate(personDobStr, context));
        } else if (selectedPersonDate == SELECTED_TYPE.AGE && AppConstants.isValidString(personDobStr)) {
            farmData.setDob(getYOBByAge(Integer.valueOf(personDobStr.trim())));
        } else if (selectedPersonDate == SELECTED_TYPE.DOB && AppConstants.isValidString(personDobStr)) {
            farmData.setYob(personDobStr.trim());
        }

        if (AppConstants.isValidString(etSpouseDob.getText().toString()))
            farmData.setSpouseDob(etSpouseDob.getText().toString());
        Log.e(TAG, "Uploading Farm And Farmer Data: " + new Gson().toJson(farmData));
        // progressDialog.hide();
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            FarmAndFarmerAsync farmAndFarmerAsync = new FarmAndFarmerAsync(farmData);
            farmAndFarmerAsync.execute();
        } else {
            final String farmerIdStr = "" + Calendar.getInstance().getTime().getTime();
            farmData.setImage(pictureImagePath);
            farmData.setAddressProof(pictureImagePathAddress);
            farmData.setIdProof(pictureImagePathId);

            FarmerT farmerT = new FarmerT(farmerIdStr, new Gson().toJson(farmData), "N", "Y", "N");
            FarmerCacheManager.getInstance(context).addFarmer(new FarmerCacheManager.FarmerInsertListener() {
                @Override
                public void onFarmerInserted(long farmerId) {
                    Log.e(TAG, "Offline saving farm and farmer");
                    String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);

                    String mob = "", name = "", fatherName = "";
                    if (farmData != null)
                        mob = farmData.getMob();
                    if (farmData != null)
                        name = farmData.getName();
                    if (farmData != null)
                        fatherName = farmData.getFather_name();
                    String svId = null, svName = null;
                    if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR)) {
                        farmData.setSvId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                    } else {
                        if (supervisorDatum != null) {
                            farmData.setSvId(supervisorDatum.getPersonId());
                            farmData.setSvName(supervisorDatum.getName());
                        }
                    }
                    for (int i = 0; i < farmList.size(); i++) {
                        final String timeStamp = "" + Calendar.getInstance().getTime().getTime() + "" + i;
                        FarmData data = farmList.get(i);

                        String irriSourceId = "";
                        if (data.getIrrigationsource() != null)
                            irriSourceId = data.getIrrigationsource();
                        String irriTypeId = "";
                        if (data.getIrrigationtype() != null)
                            irriTypeId = data.getIrrigationtype();
                        String soilTypeId = "";
                        if (data.getSoiltype() != null)
                            soilTypeId = data.getSoiltype();

                        String fpoData = new Gson().toJson(data.getFpoCropList());
                        Farm farm = new Farm(timeStamp, data.getId(), "" + farmerIdStr, name, phoneNumber, clusterId, compId,
                                data.getAddL1(), data.getAddL2(), data.getVillageOrCity(), data.getDistrictId(),
                                data.getMandalOrTehsil(), data.getStateId(), data.getCountryId(), data.getArea(), "0",
                                data.getIrriSourceName(), data.getPreviouscrop(), data.getIrriTypename(), data.getSoiltypeName(),
                                null, null, null,
                                null, null, null, "0", null,
                                name, null, "0", null,
                                null, null, null, null, fatherName,
                                "[]", null, null, "Y", "N",
                                "[]", "[]", data.getCurrentCropName(), "Y", fpoData, data.getCropId(),
                                data.getName(), data.getIsOwned(), data.getSeason(), "N", "[]",
                                data.getTransplant_date(), data.getIs_irrigated(), data.getCropping_pattern(),
                                data.getPlanting_method(), svId, svName);

                        data.setStateDatumList(new ArrayList<>());
                        data.setStateDatumList(new ArrayList<>());

                        farm.setFarmFullData(new Gson().toJson(data));
                        farm.setIrriTypeId(irriTypeId);
                        farm.setIrriSourceId(irriSourceId);
                        farm.setSoilTypeId(soilTypeId);
                        farm.setOwnerShipProof(data.getOwnerShipPath());
                        farm.setFarmImage(data.getFarmImagePath());
                        farm.setMotorHp(data.getMotorHp());
                        farm.setLandCategoty(data.getIs_irrigated());
                        if (data.getAssetsList() != null && data.getAssetsList().size() > 0)
                            farm.setAssets(new Gson().toJson(data.getAssetsList()));
                        FarmCacheManager.getInstance(context).addFarm(null, farm);
                        farmList.get(i).setStateDatumList(new ArrayList<>());
                        farmList.get(i).setCityDatumList(new ArrayList<>());
                        farmList.get(i).setSvId(svId);
                        farmList.get(i).setFarmId(timeStamp);
                        farmList.get(i).setFarmIdOffline(timeStamp);
                        data.setFarmId(timeStamp);
                        data.setFarmIdOffline(timeStamp);
                    }

                    farmData.setFarmDataList(farmList);

                    FarmerCacheManager.getInstance(context).updateFarmData(farmerIdStr, new Gson().toJson(farmData));
                    AddFarmCCacheManager.getInstance(context).deleteAllData();
                    Toasty.success(context, resources.getString(R.string.farm_added_success_msg), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, LandingActivity.class);
                    ActivityOptions options = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                        startActivity(intent, options.toBundle());
                        finish();
                    } else {
                        finishAffinity();
                        startActivity(intent);
                        finish();
                    }
                }
            }, farmerT);


        }
    }

    private void setSpinnerData(List<DDNew> sourceList, List<DDNew> typeList, List<DDNew> soilTypeList, List<Season> seasonList, List<DDNew> cropList,
                                List<DDNew> isIrrigatedDDList, List<DDNew> plantingMethodDDList, List<DDNew> croppingPatternDDList
            , List<CropFormDatum> cropFormDatumList, List<CropFormDatum> cropFormDatumListSuper) {
        if (sourceList != null)
            irrigationSourceDDList.addAll(sourceList);
        if (typeList != null)
            irrigationTypeDDList.addAll(typeList);
        if (soilTypeList != null)
            soilTypeDDList.addAll(soilTypeList);
        if (seasonList != null)
            seasonDDList.addAll(seasonList);

        if (cropList != null)
            farmCropList.addAll(cropList);
//        irrigationSourceDDList.add(0, new DDNew("0", resources.getString(R.string.select_irri_source_prompt)));
//        irrigationTypeDDList.add(0, new DDNew("0", resources.getString(R.string.select_irri_type_prompt)));
//        soilTypeDDList.add(0, new DDNew("0", resources.getString(R.string.select_soil_type_prompt)));
        // seasonDDList.add(0, new Season("0", resources.getString(R.string.select_season_prompt)));
//        farmCropList.add(0, new DDNew("0", resources.getString(R.string.select_crop_rompt)));
//        this.isIrrigatedDDList.add(0, new DDNew("0", resources.getString(R.string.select_land_category), resources.getString(R.string.select_land_category)));
//        this.plantingMethodDDList.add(0, new DDNew("0", resources.getString(R.string.select_planting_method), resources.getString(R.string.select_planting_method)));
//        this.croppingPatternDDList.add(0, new DDNew("0", resources.getString(R.string.select_cropping_pattern), resources.getString(R.string.select_cropping_pattern)));
        this.isIrrigatedDDList.addAll(isIrrigatedDDList);
        this.plantingMethodDDList.addAll(plantingMethodDDList);
        this.croppingPatternDDList.addAll(croppingPatternDDList);
        adapter = new AddFarmAdapter2(context, farmList, irrigationTypeDDList, irrigationSourceDDList, soilTypeDDList, seasonDDList, farmCropList,
                this.plantingMethodDDList, this.isIrrigatedDDList, this.croppingPatternDDList, cropFormDatumList, cropFormDatumListSuper, countryDatumList, AddFarmActivity.this);


        adapter.setHpMotorList(motorHpList);
        adapter.setImagePickClickListener(new AddFarmAdapter2.OnImagePickClickListener() {
            @Override
            public void onImageClicked(int index, ImageType imageType) {

                imageIndex = index;
                if (imageType == ImageType.OWNERSHIP) {
                    selectImage(CaptureConstants.CAPTURE4);
                } else if (imageType == ImageType.FARM) {
                    selectImage(CaptureConstants.CAPTURE5);
                }

            }
        });
        String country = countryCodePicker.getSelectedCountryName();
        if (country != null) {
            for (int i = 0; i < countryDatumList.size(); i++) {
                if (countryDatumList.get(i).getName() != null && country.trim().equals(countryDatumList.get(i).getName().trim())) {
                    try {
                        adapter.setCountryIndex(i);
                    } catch (Exception e) {
                    }
                    break;
                }
            }
        }
        if (countryCodePicker.getSelectedCountryCode() != null && countryCodePicker.getSelectedCountryCode().trim().equals("63")) {

            if (adapter != null)
                adapter.setHideAddl2(true);
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        //progressDialog.dismiss();
        progressBar_image.setVisibility(View.GONE);
        tv_add_new_farm_submit_bt.setVisibility(View.VISIBLE);
        isSpinnerDataLoaded = true;

        if (sourceList == null || sourceList.size() == 0) {
            getAllDataNew(AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE_NEW);
        }

        if (typeList == null || typeList.size() == 0) {
            getAllDataNew2(AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE_NEW);
        }
        if (soilTypeList == null || soilTypeList.size() == 0) {
            getAllDataNew3(AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE_NEW);
        }
        if (cropList == null || cropList.size() == 0) {
            getAllDataNew4(AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW);
        }
        if (isIrrigatedDDList == null || isIrrigatedDDList.size() == 0) {
            getAllDataNew7(AppConstants.CHEMICAL_UNIT.LAND_CATEGORY);
        }
        if (plantingMethodDDList == null || plantingMethodDDList.size() == 0) {
            getAllDataNew6(AppConstants.CHEMICAL_UNIT.PLANTING_METHOD);
        }
        if (croppingPatternDDList == null || croppingPatternDDList.size() == 0) {
            getAllDataNew5(AppConstants.CHEMICAL_UNIT.CROPPING_PATTERN);
        }

    }

    private void getFormData() {
        ParamData paramData = new ParamData();
        paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        Log.e(TAG, "Sending New Param i  AND type  " + new Gson().toJson(paramData));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getFarmCropForm(paramData).enqueue(new Callback<CropFormResponse>() {
            @Override
            public void onResponse(Call<CropFormResponse> call, Response<CropFormResponse> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    CropFormResponse response = response1.body();
                    Log.e("OfflineModeAct", "Response Make  " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, response.getMessage());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        if (response != null && response.getData() != null && response.getData().size() > 0) {

                            DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.FARM_ADD_DYNAMIC);
                            DropDownT material = new DropDownT(AppConstants.CHEMICAL_UNIT.FARM_ADD_DYNAMIC, new Gson().toJson(response));
                            DropDownCacheManager.getInstance(context).addChemicalUnit(material);
                        }
                        if (adapter != null) {
                            if (response.getData() != null)
                                adapter.setCropFormDatumList(response.getData());
                            if (response.getSuperData() != null) {
                                adapter.setCropFormDatumListSuper(response.getSuperData());

                            }
                        }
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
                        Log.e(TAG, "Cache Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CropFormResponse> call, Throwable t) {
                Log.e("OfflineModeAct", "Failure getOfflineData " + t.toString());
            }
        });


    }

    private void initializeCountry() {
        countryDatumList.clear();
        CountryResponse countryResponse = new Gson().fromJson(loadJSONFromAsset(), CountryResponse.class);
        if (countryResponse.getData() != null && countryResponse.getData().size() > 0)
            countryDatumList.addAll(countryResponse.getData());
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("response.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

//            Log.e(TAG, "myjsondata" + json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void initializeState() {
        stateDatumList.clear();
//        stateDatumList.add(new StateDatum("", resources.getString(R.string.select_state)));
    }

    private void initializeCity() {
        cityDatumList.clear();
//        cityDatumList.add(new CityDatum("", resources.getString(R.string.select_district)));
    }

    private void getCity(String countryId, String stateId) {
        try {
            initializeCity();
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);

            Call<CityResponse> fetchFarmDataCall = apiService.getAllCity(
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN), countryId, stateId);
            fetchFarmDataCall.enqueue(new Callback<CityResponse>() {
                @Override
                public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response != null) {
                                CityResponse countryResponse = response.body();
                                if (response.body().getStatus() == 10) {
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, response.body().getMsg());
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                    //un authorized access
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.unauthorized_access));
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                    // auth token expired
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.authorization_expired));
                                } else {
                                    try {
                                        if (countryResponse.getData() != null)
                                            cityDatumList.addAll(countryResponse.getData());
                                        citySpinnerAdapter.notifyDataSetChanged();
                                    } catch (Exception e) {
                                    }
                                }
                            } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.authorization_expired));
                            } else {

                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }


                }

                @Override
                public void onFailure(Call<CityResponse> call, Throwable t) {

                }
            });
        } catch (Exception r) {
            r.printStackTrace();
        }
    }


    private void getSpinnerData() {
        //try {
        //progressDialog.show();
        tv_add_new_farm_submit_bt.setVisibility(View.GONE);
        //progressBar_image.setVisibility(View.VISIBLE);
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comp_id", "" + compId);
        jsonObject.addProperty("user_id", "" + userId);
        jsonObject.addProperty("token", "" + token);
        Call<SpinnerResponse> spinnerResponseCall = apiService.getspinnerData(jsonObject);
        Log.e("SpinnerResponse", "Data Sending  " + new Gson().toJson(jsonObject));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        spinnerResponseCall.enqueue(new Callback<SpinnerResponse>() {
            @Override
            public void onResponse(Call<SpinnerResponse> call, Response<SpinnerResponse> response) {
                isLoaded[0] = true;
                String error = "";
                if (response.isSuccessful()) {
                    SpinnerResponse spinnerResponse = response.body();
                    Log.e("SpinnerResponse", "Data  " + new Gson().toJson(response.body()));
                    if (!isOfflineMode && response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.multiple_login_msg));
                    } else {
                       /* //  Log.e("SR",spinnerResponse.getMsg());
                        setSpinnerData(response.body().getIrrigationSource(),response.body().getIrrigationType(),response.body().getSoilType(),
                                response.body().getSeasonList(),response.body().getCropList());*/

                        seasonDDList = new ArrayList<>();
                        if (response.body().getSeasonList() != null)
                            seasonDDList.addAll(response.body().getSeasonList());
                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                    }
                } else if (!isOfflineMode && response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (!isOfflineMode && response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    //progressDialog.dismiss();
                    progressBar_image.setVisibility(View.GONE);
                    isSpinnerDataLoaded = false;
                    tv_add_new_farm_submit_bt.setVisibility(View.VISIBLE);
                    try {
                        error = response.errorBody().string().toString();
                        Log.e("SpinnerResponse ", "err  " + error);
                        if (!isOfflineMode)
                            viewFailDialog.showDialogForFinish(AddFarmActivity.this, resources.getString(R.string.failed_to_load_data_msg));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(AddFarmActivity.this, call.request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }


            }


            @Override
            public void onFailure(Call<SpinnerResponse> call, Throwable t) {
                Log.e("SpinnerResponse", t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(AddFarmActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                if (t instanceof SocketTimeoutException) {
                    getSpinnerData();
                } else {
                    // progressDialog.dismiss();
                    tv_add_new_farm_submit_bt.setVisibility(View.VISIBLE);
                    progressBar_image.setVisibility(View.GONE);
                    isSpinnerDataLoaded = false;
                    if (!isOfflineMode)
                        viewFailDialog.showDialogForFinish(AddFarmActivity.this, resources.getString(R.string.failed_to_load_data_msg));
                }

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

    private void getClusterOfCompany() {
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getClusters(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID)).enqueue(new Callback<ClusterResponse>() {
            @Override
            public void onResponse(Call<ClusterResponse> call, Response<ClusterResponse> response) {
                isLoaded[0] = true;
                String error = "";
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {

                        setClusterData(response.body().getClusterList());
                        //progressDialog.cancel();
                        progressBar_image.setVisibility(View.GONE);
                    } else if (response.body().getStatus() == 10) {
                        viewFailDialog.showSessionExpireDialog(AddFarmActivity.this);
                        //progressDialog.cancel();
                        progressBar_image.setVisibility(View.GONE);
                    } else {
                        //   progressDialog.cancel();
                        progressBar_image.setVisibility(View.GONE);
                        viewFailDialog.showDialogForFinish(AddFarmActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.no_cluster_found_to_add_cluster));
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        //progressDialog.cancel();
                        progressBar_image.setVisibility(View.GONE);
                        viewFailDialog.showDialogForFinish(AddFarmActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg));
                        error = response.errorBody().string();
                        Log.e(TAG, "Cluster Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(AddFarmActivity.this, call.request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<ClusterResponse> call, Throwable t) {
                //progressDialog.cancel();
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(AddFarmActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                progressBar_image.setVisibility(View.GONE);
                isLoaded[0] = true;
                viewFailDialog.showDialogForFinish(AddFarmActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg));
                Log.e(TAG, "Cluster Failure " + t.toString());
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


    private void getFarmerList() {
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String clusterId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getFarmerList(compId, clusterId, userId, token).enqueue(new Callback<FetchFarmerResponse>() {
            @Override
            public void onResponse(Call<FetchFarmerResponse> call, Response<FetchFarmerResponse> response) {
                isLoaded[0] = true;
                String error = "";
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    Log.e(TAG, "Farmer List Res " + new Gson().toJson(response.body()));

                    if (response.body().getStatus() == 10 && !isOfflineMode) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == 1) {
                        setFarmerListData(response.body().getFarmerDataList());

                    } else {
                        //checkbox_add_farm.setVisibility(View.GONE);
                        new_farmer_layout.setVisibility(View.VISIBLE);
                        existing_farmer_layout.setVisibility(View.GONE);
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS && !isOfflineMode) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED && !isOfflineMode) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "Farmer List Err " + error);
                        if (!isOfflineMode)
                            viewFailDialog.showDialogForFinish(AddFarmActivity.this, resources.getString(R.string.failed_to_load_data_msg));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(AddFarmActivity.this, call.request().url().toString(),
                            "" + diff, internetSPeed, error, statusCode);
                }
            }

            @Override
            public void onFailure(Call<FetchFarmerResponse> call, Throwable t) {
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(AddFarmActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "Farmer List failure " + t.toString());
                if (!isOfflineMode)
                    viewFailDialog.showDialogForFinish(AddFarmActivity.this, resources.getString(R.string.failed_to_load_data_msg));
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

    private void internetFlow(boolean isLoaded) {
        try {

            if (!isLoaded) {
                if (!ConnectivityUtils.isConnected(context)) {
                    AppConstants.failToast(context, resources.getString(R.string.check_internet_connection_msg));
                } else {
                    ConnectivityUtils.checkSpeedWithColor(AddFarmActivity.this, new ConnectivityUtils.ColorListener() {
                        @Override
                        public void onColorChanged(int color, String speed) {
                            if (color == android.R.color.holo_green_dark) {
                                connectivityLine.setVisibility(View.GONE);
//                                    connectivityLine.setBackgroundColor(getColor(R.color.blue));
                            } else {
                                connectivityLine.setVisibility(View.VISIBLE);

                                connectivityLine.setBackgroundColor(resources.getColor(color));
                            }
                            internetSPeed = speed;
                        }
                    }, 20);
                }
            }


        } catch (Exception e) {

        }

    }

    String internetSPeed = "";
    SpinnerAdapterFarmer farmerSpinnerAdapter;

    private void setFarmerListData(List<FarmerSpinnerData> list) {
       /* FarmerSpinnerData farmerData = new FarmerSpinnerData();
        farmerData.setPersonId("0");
        farmerData.setName(getString(R.string.select_farmer));
        farmerDataList.add(farmerData);*/
        farmerDataList.addAll(list);

        farmerSpinnerAdapter = new SpinnerAdapterFarmer(context, farmerDataList);
        farmer_spinner.setAdapter(farmerSpinnerAdapter);

        if (farmerAndFarmDataCache != null && farmerAndFarmDataCache.getPersonId() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getPersonId())) {
            int selection = 0;
            for (int i = 0; i < farmerDataList.size(); i++) {
                if (String.valueOf(farmerDataList.get(i).getPersonId()).equals(farmerAndFarmDataCache.getPersonId())) {
                    selection = i;
                    break;
                }
            }
            farmer_spinner.setSelectedItem(selection);
        }
//        Toasty.error(context,"size "+farmerDataList.size(),Toast.LENGTH_LONG).show();
        AutoCompleteAdapter adapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line, farmerDataList);
        farmerNameEt.setAdapter(adapter);

        farmerNameEt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    try {
                        Log.e(TAG, "DATTTTTTA " + new Gson().toJson(adapter.getItem(i)));
                        farmerNameEt.setText(adapter.getItem(i).getName());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        farmerNameEt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        farmer_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                {
                    if (i > 0) {
                        addingToExistingFarmer = true;
                        farmerAndFarmDataCache.setPersonId("" + farmerSpinnerAdapter.getItem(i).getPersonId());
                        updateCache();
                        enableDisable("old", farmerSpinnerAdapter.getItem(i), i);
                    } else {

                    }
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    private void getBankDetails(String ifsc) {

        final String url = "https://api.techm.co.in/api/v1/ifsc/" + ifsc;
        Log.e(TAG, "URL " + url);

// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "IFSC RES " + response.toString());
                        try {
                            IfscResponse ifscResponse = new Gson().fromJson(response.toString(), IfscResponse.class);
                            if (ifscResponse.getStatus().trim().toLowerCase().equals("success")) {
                                isValidIfsc = true;
                                branchEt.setText(ifscResponse.getData().getBRANCH());
                                bankNameEt.setText(ifscResponse.getData().getBANK());
                                branchEt.setClickable(false);
                                branchEt.setEnabled(false);
                                bankNameEt.setClickable(false);
                                bankNameEt.setEnabled(false);
                            } else {
                                isValidIfsc = false;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            isValidIfsc = false;
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "IFSC ERR " + error.toString());
                    }
                }
        );

// add it to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(getRequest);

       /* Retrofit retrofit = RetrofitClient.getClient("https://api.techm.co.in/api/v1/ifsc");
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        apiInterface.getBankDetails(*//*ifscCodeEt.getText().toString()*//*"ANDB0001154").enqueue(new Callback<IfscResponse>() {
            @Override
            public void onResponse(Call<IfscResponse> call, Response<IfscResponse> response) {
                if (response.isSuccessful()){
                    Log.e(TAG,"IFSC Res "+new Gson().toJson(response.body()));
                }else {
                    try {
                        Log.e(TAG,"IFSC ERR "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<IfscResponse> call, Throwable t) {
                Log.e(TAG,"IFSC FAILURE "+t.toString());
            }
        });*/

    }

    private void addExistingFarmerFarm() {
        //progressDialog.show();
        tv_add_new_farm_submit_bt.setClickable(false);
        tv_add_new_farm_submit_bt.setEnabled(false);
        progressBar_image.setVisibility(View.VISIBLE);
        String personId;
        String clusterId;
        FarmerSpinnerData farmerSpinnerData = null;
        String isExistingFarm = "";

        if (isFarmer ||selfSvFarmerCheckBox.isChecked()) {
            personId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
            clusterId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID);
        }else{

            farmerSpinnerData = farmerSpinnerAdapter.getItem(farmer_spinner.getSelectedPosition());
            personId = farmerSpinnerData.getPersonId() + "";
            clusterId = farmerSpinnerData.getClusterId() + "";
            if (farmerSpinnerData.getIsExistingFarm() == null || TextUtils.isEmpty(farmerSpinnerData.getIsExistingFarm())) {
                isExistingFarm = "Y";
            } else {
                isExistingFarm = "N";
            }
        }
        for (int i = 0; i < farmList.size(); i++) {
            FarmData farmData = farmList.get(i);
            if (farmData.getId() == null || TextUtils.isEmpty(farmData.getId())) {
                Toasty.error(context, resources.getString(R.string.enter_farm_id_msg), Toast.LENGTH_LONG).show();
                adapter.setErrorAndFocus(i, "id", resources.getString(R.string.error_field_required));
                // progressDialog.dismiss();
                progressBar_image.setVisibility(View.GONE);
                tv_add_new_farm_submit_bt.setClickable(true);
                tv_add_new_farm_submit_bt.setEnabled(true);
                return;
            } else if (farmData.getArea() == null || TextUtils.isEmpty(farmData.getArea())) {
                Toasty.error(context, resources.getString(R.string.enter_farm_area_msg), Toast.LENGTH_LONG).show();
                adapter.setErrorAndFocus(i, "area", resources.getString(R.string.error_field_required));
                //progressDialog.dismiss();
                progressBar_image.setVisibility(View.GONE);
                tv_add_new_farm_submit_bt.setClickable(true);
                tv_add_new_farm_submit_bt.setEnabled(true);
                return;
            } else if (farmData.getSeason() == null || farmData.getSeason().trim().equals("0")) {
                Toasty.error(context, resources.getString(R.string.select_season_msg), Toast.LENGTH_LONG).show();
                adapter.setErrorAndFocus(i, "season", resources.getString(R.string.error_field_required));

                progressBar_image.setVisibility(View.GONE);
                tv_add_new_farm_submit_bt.setClickable(true);
                tv_add_new_farm_submit_bt.setEnabled(true);
                return;
            } else if (farmData.getCropId() == null || TextUtils.isEmpty(farmData.getCropId()) || farmData.getCropId().trim().equals("0")) {
                Toasty.error(context, resources.getString(R.string.select_crop_for_farm_msg), Toast.LENGTH_LONG).show();
                adapter.setErrorAndFocus(i, "crop", resources.getString(R.string.error_field_required));
                progressBar_image.setVisibility(View.GONE);
                tv_add_new_farm_submit_bt.setClickable(true);
                tv_add_new_farm_submit_bt.setEnabled(true);
                return;
            } else if (isPreviousCropMandatory && (farmData.getPreviouscrop() == null || TextUtils.isEmpty(farmData.getPreviouscrop()) || farmData.getPreviouscrop().trim().equals("0"))) {
                Toasty.error(context, resources.getString(R.string.select_previous_crop_msg), Toast.LENGTH_LONG).show();
                adapter.setErrorAndFocus(i, "cropP", resources.getString(R.string.error_field_required));
                progressBar_image.setVisibility(View.GONE);
                tv_add_new_farm_submit_bt.setClickable(true);
                tv_add_new_farm_submit_bt.setEnabled(true);
                return;
            }
        }
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        FarmerAndFarmData farmData = new FarmerAndFarmData();

        String role = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE);
        if (role != null && role.equals(AppConstants.ROLES.SUPERVISOR)) {
            farmData.setSvId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
        } else if (supervisorDatum != null) {
            farmData.setSvId(supervisorDatum.getPersonId());
            farmData.setSvName(supervisorDatum.getName());
        }
        farmData.setPersonId(personId);
        farmData.setCompId(compId);
        farmData.setFarmDataList(farmList);
        farmData.setClusterId(clusterId);
        farmData.setAdded_by(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
        farmData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        farmData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        Log.e(TAG, "Uploading Farm Data " + new Gson().toJson(farmData));
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            FarmAsync farmAsync = new FarmAsync(farmData, personId, clusterId);
            farmAsync.execute();
        } else {


            String mob = "", name = "", fatherName = "";
            String ownerId = "";
            if (farmerSpinnerData != null) {
                if (farmerSpinnerData != null)
                    mob = farmerSpinnerData.getMob();
                if (farmerSpinnerData != null)
                    name = farmerSpinnerData.getName();
                if (farmerSpinnerData != null)
                    fatherName = farmerSpinnerData.getFatherName();
                if (farmerSpinnerData != null)
                    ownerId = farmerSpinnerData.getPersonId() + "";

                farmData.setFather_name(farmerSpinnerData.getFatherName());
                farmData.setMob(mob);
                farmData.setName(name);
                farmData.setMobileNetworkId(farmerSpinnerData.getMobileOperator());
                farmData.setDob(farmerSpinnerData.getDob());
                farmData.setGeder(farmerSpinnerData.getGender());
                farmData.setLiteracy(farmerSpinnerData.getLiteracyStatus());
                farmData.setFinancial_status(farmerSpinnerData.getFinancialStatus());
                farmData.setCivil_status(farmerSpinnerData.getCivilStatus());
                farmData.setMiaId(farmerSpinnerData.getMia());
                farmData.setIsSmartPhone(farmerSpinnerData.getIsUsingSphone());
                farmData.setCasteCategoryId(farmerSpinnerData.getCasteCategory());
                farmData.setCasteId(farmerSpinnerData.getCaste());
                farmData.setReligionId(farmerSpinnerData.getReligion());
                farmData.setAadhaar(farmerSpinnerData.getAadhaar());
                farmData.setPan(farmerSpinnerData.getPan());
                farmData.setCountry(farmerSpinnerData.getCountry());
                farmData.setStateId(farmerSpinnerData.getState());
                farmData.setCityId(farmerSpinnerData.getDistrict());
                farmData.setVillage_or_city(farmerSpinnerData.getVillageOrCity());
                farmData.setMandal_or_tehsil(farmerSpinnerData.getMandalOrTehsil());
                farmData.setAddL1(farmerSpinnerData.getAddL1());
                farmData.setAddL2(farmerSpinnerData.getAddL2());
                farmData.setMob2(farmerSpinnerData.getMob2());
                farmData.setEmail(farmerSpinnerData.getEmail());
                farmData.setSpouseDob(farmerSpinnerData.getSpouseDob());
                farmData.setSpouse_name(farmerSpinnerData.getSpouseName());
                farmData.setFamily_mem_count(farmerSpinnerData.getFamilyMemCount() + "");
                farmData.setAdults_count(farmerSpinnerData.getAdultsCount() + "");
                farmData.setKids_count(farmerSpinnerData.getKidsCount() + "");
                farmData.setDependent_count(farmerSpinnerData.getDependentCount() + "");

                farmData.setIfscCode(farmerSpinnerData.getIfsc());
                farmData.setBankName(farmerSpinnerData.getBankName());
                farmData.setAccountNumber(farmerSpinnerData.getAccountNo());
                farmData.setHolderName(farmerSpinnerData.getAccountName());
                farmData.setBranch(farmerSpinnerData.getBranch());
                farmData.setUpi_id(farmerSpinnerData.getUpiId());
                farmData.setSwiftCode(farmerSpinnerData.getSwiftCode());
                farmData.setBeneficiary_name(farmerSpinnerData.getBeneficiaryName());
                farmData.setAnualIncome(farmerSpinnerData.getEstIncome());
                farmData.setIsShareHolder(farmerSpinnerData.getIsShareholder());

//                farmData.setMobileNetworkId(farmerSpinnerData.getisS());
//                farmData.setMobileNetworkId(farmerSpinnerData.getIsShareholder());
//                farmData.setMobileNetworkId(farmerSpinnerData.getIsShareholder());
//                farmData.setMobileNetworkId(farmerSpinnerData.getIsShareholder());
//                farmData.setMobileNetworkId(farmerSpinnerData.getIsShareholder());

            }
            String svId = null, svName = null;
            if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR)) {

                svId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
//            farmData.setSvName(supervisorDatum.getName());
            } else {
                if (supervisorDatum != null) {
                    svId = supervisorDatum.getPersonId();
                    svName = supervisorDatum.getName();
                }
            }

            String finalOwnerId = ownerId;


            for (int i = 0; i < farmList.size(); i++) {
                final String timeStamp = "" + Calendar.getInstance().getTime().getTime() + "" + i;
                FarmData data = farmList.get(i);

                String irriSourceId = "0";
                if (data.getSeason() != null)
                    irriSourceId = data.getIrrigationsource();
                String irriTypeId = "0";
                if (data.getSeason() != null)
                    irriTypeId = data.getIrrigationtype();
                String soilTypeId = "0";
                if (data.getSeason() != null)
                    soilTypeId = data.getSeason();


                String fpoData = new Gson().toJson(data.getFpoCropList());
                Farm farm = new Farm(timeStamp, data.getId(), ownerId, name, mob, clusterId, compId, data.getAddL1(), data.getAddL2(),
                        data.getVillageOrCity(), data.getDistrictId(), data.getMandalOrTehsil(), data.getStateId(), data.getCountryId(),
                        data.getArea(), "0", data.getIrriSourceName(), data.getPreviouscrop(), data.getIrriTypename(),
                        data.getSoiltypeName(), null, null, null, null, null,
                        null, "0", null, name, null, "0", null, null,
                        null, null, null, fatherName, "[]", null, null, "Y",
                        "N", "[]", "[]", data.getCurrentCropName(), "Y", fpoData, data.getCropId(),
                        data.getName(), data.getIsOwned(), data.getSeason(), isExistingFarm, "[]", data.getTransplant_date(),
                        data.getIs_irrigated(), data.getCropping_pattern(), data.getPlanting_method(), svId, svName);

                farm.setIsOfflineAdded("Y");

                farm.setIrriTypeId(irriTypeId);
                farm.setIrriSourceId(irriSourceId);
                farm.setSoilTypeId(soilTypeId);
                farm.setOwnerShipProof(data.getOwnerShipPath());
                farm.setFarmImage(data.getFarmImagePath());
                farm.setMotorHp(data.getMotorHp());
                farm.setLandCategoty(data.getIs_irrigated());
                if (data.getAssetsList() != null && data.getAssetsList().size() > 0)
                    farm.setAssets(new Gson().toJson(data.getAssetsList()));

                farmList.get(i).setStateDatumList(new ArrayList<>());
                farmList.get(i).setCityDatumList(new ArrayList<>());

                farmList.get(i).setSvId(svId);
                farmList.get(i).setFarmId(timeStamp);
                farmList.get(i).setFarmIdOffline(timeStamp);
                data.setFarmIdOffline(timeStamp);
                data.setFarmId(timeStamp);
                farm.setFarmFullData(new Gson().toJson(data));
                FarmCacheManager.getInstance(context).addFarm(null, farm);
            }

            farmData.setFarmDataList(farmList);
            FarmerCacheManager.getInstance(context).getFarmer(new FarmerCacheManager.GetFarmerListener() {
                @Override
                public void onFarmerLoaded(FarmerT farmerT) {
                    if (farmerT == null || !AppConstants.isValidString(farmerT.getFarmerId())) {
                        farmData.setFarmDataList(new ArrayList<>());
                        FarmerT farmerTNew = new FarmerT(finalOwnerId, new Gson().toJson(farmData), "N", "N",
                                "N");
                        FarmerCacheManager.getInstance(context).addFarmer(new FarmerCacheManager.FarmerInsertListener() {
                            @Override
                            public void onFarmerInserted(long farmerId) {

                            }
                        }, farmerTNew);
                    }
                }
            }, ownerId);

            AddFarmCCacheManager.getInstance(context).deleteAllData();
            Toasty.success(context, resources.getString(R.string.farm_added_success_msg), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, LandingActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
                startActivity(intent, options.toBundle());
                finish();
            } else {
                finishAffinity();
                startActivity(intent);
                finish();
            }

        }
    }

    private void enableDisable(String from, FarmerSpinnerData data, int position) {
        if (from.equals("new")) {
            //Toast.makeText(context, from+" "+position, Toast.LENGTH_SHORT).show();
            farmerNameEt.setEnabled(true);
            farmerNameEt.setClickable(true);
            fatherNameEt.setEnabled(true);
            fatherNameEt.setClickable(true);
            phoneNumberEt.setEnabled(true);
            phoneNumberEt.setClickable(true);
            adhaarEt.setEnabled(true);
            adhaarEt.setClickable(true);
            villageOrCityEt.setEnabled(true);
            villageOrCityEt.setClickable(true);
            mandalOrTehsilEt.setEnabled(true);
            mandalOrTehsilEt.setClickable(true);
            districtEt.setEnabled(true);
            districtEt.setClickable(true);
            stateEt.setEnabled(true);
            stateEt.setClickable(true);
            countryEt.setEnabled(true);
            countryEt.setClickable(true);
            bankNameEt.setEnabled(true);
            bankNameEt.setClickable(true);
            accountEt.setEnabled(true);
            accountEt.setClickable(true);
            etAddressLine1.setEnabled(true);
            etAddressLine1.setClickable(true);
            ifscCodeEt.setEnabled(true);
            ifscCodeEt.setClickable(true);
            branchEt.setEnabled(true);
            branchEt.setClickable(true);
            accountHolderName.setEnabled(true);
            accountHolderName.setClickable(true);
            etAddressLine2.setEnabled(true);
            etAddressLine2.setClickable(true);
            ti_et_phone2.setEnabled(true);
            ti_et_phone2.setClickable(true);

           /* farmerNameEt.setText(null);
            fatherNameEt.setText(null);
            phoneNumberEt.setText(null);
            adhaarEt.setText(null);
            villageOrCityEt.setText(null);
            mandalOrTehsilEt.setText(null);
            districtEt.setText(null);
            stateEt.setText(null);
            countryEt.setText(null);
            bankNameEt.setText(null);
            accountEt.setText(null);
            etAddressLine1.setText(null);
            ifscCodeEt.setText(null);
            branchEt.setText(null);
            accountHolderName.setText(null);
            etAddressLine2.setText(null);*/
        } else {
            // Toast.makeText(context, from + " " + data.getName() + " " + position, Toast.LENGTH_SHORT).show();

            farmerNameEt.setEnabled(false);
            farmerNameEt.setClickable(false);
            fatherNameEt.setEnabled(false);
            fatherNameEt.setClickable(false);
            phoneNumberEt.setEnabled(false);
            phoneNumberEt.setClickable(false);
            adhaarEt.setEnabled(false);
            adhaarEt.setClickable(false);
            villageOrCityEt.setEnabled(false);
            villageOrCityEt.setClickable(false);
            mandalOrTehsilEt.setEnabled(false);
            mandalOrTehsilEt.setClickable(false);
            districtEt.setEnabled(false);
            districtEt.setClickable(false);
            stateEt.setEnabled(false);
            stateEt.setClickable(false);
            countryEt.setEnabled(false);
            countryEt.setClickable(false);
            bankNameEt.setEnabled(false);
            bankNameEt.setClickable(false);
            accountEt.setEnabled(false);
            accountEt.setClickable(false);
            etAddressLine1.setEnabled(false);
            etAddressLine1.setClickable(false);
            ifscCodeEt.setEnabled(false);
            ifscCodeEt.setClickable(false);
            branchEt.setEnabled(false);
            branchEt.setClickable(false);
            accountHolderName.setEnabled(false);
            accountHolderName.setClickable(false);
            etAddressLine2.setEnabled(false);
            etAddressLine2.setClickable(true);
            ti_et_phone2.setEnabled(false);
            ti_et_phone2.setClickable(false);

            if (data.getName() != null)
                farmerNameEt.setText(data.getName());
            if (data.getFatherName() != null)
                fatherNameEt.setText(data.getFatherName());
            String code = "";
            if (data.getCountryCode() != null)
                code = data.getCountryCode();
            if (data.getMob() != null) {
                phoneNumberEt.setText(/*code +*/ data.getMob());
                ti_et_phone2.setText(/*code + */data.getMob());
            }
            countryCodePicker.setFullNumber(code+data.getMob());


            if (data.getAadhaar() != null && !data.getAadhaar().equals("0"))
                adhaarEt.setText(data.getAadhaar());
            if (data.getVillageOrCity() != null && !data.getVillageOrCity().equals("0"))
                villageOrCityEt.setText(data.getVillageOrCity());
            if (data.getMandalOrTehsil() != null)
                mandalOrTehsilEt.setText(data.getMandalOrTehsil());
            if (data.getDistrict() != null && !data.getDistrict().equals("0"))
                districtEt.setText(data.getDistrict());
            if (data.getState() != null && !data.getState().equals("0"))
                stateEt.setText(data.getState());
            if (data.getCountry() != null)
                countryEt.setText(data.getCountry());
            if (data.getBankName() != null && !data.getBankName().equals("0"))
                bankNameEt.setText(data.getBankName());
            if (data.getAccountNo() != null && !data.getAccountNo().equals("0"))
                accountEt.setText(data.getAccountNo());
            if (data.getAddL1() != null && !data.getAddL1().equals("0"))
                etAddressLine1.setText(data.getAddL1());
            if (data.getAddL2() != null && !data.getAddL2().equals("0"))
                etAddressLine2.setText(data.getAddL2());
            if (data.getIfsc() != null && !data.getIfsc().equals("0"))
                ifscCodeEt.setText(data.getIfsc());
            if (data.getBranch() != null && !data.getBranch().equals("0"))
                branchEt.setText(data.getBranch());
            if (data.getAccountName() != null && !data.getAccountName().equals("0"))
                accountHolderName.setText(data.getAccountName());

            moreDetailsFarmerBtn.setVisibility(View.GONE);

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

        Log.e(TAG, "Provider " + s);

    }

    @Override
    public void onProviderDisabled(String s) {
        if (!isShownGpsDialog)
            enableGPS();
    }

    private void enableGPS() {
        isShownGpsDialog = true;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(AddFarmActivity.this).build();
        mGoogleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(2 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

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
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            status.startResolutionForResult(AddFarmActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Toast.makeText(MapsActivity.this, "Requesting for gps in catch", Toast.LENGTH_SHORT).show();

                            // Ignore the error.
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
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(resources.getString(R.string.location_permission_needed))
                        .setMessage(resources.getString(R.string.this_apps_needs_location_permission))
                        .setPositiveButton(resources.getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(AddFarmActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {

            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE1) {
                    String result = CaptureConstants.CAPTURE1;
                    //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    onSelectFromGalleryResult(data, result);
                } else if (requestCode == SELECT_FILE2) {
                    String result = CaptureConstants.CAPTURE2;
                    // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                    onSelectFromGalleryResult(data, result);
                } else if (requestCode == SELECT_FILE3) {
                    String result = CaptureConstants.CAPTURE3;
                    // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                    onSelectFromGalleryResult(data, result);
                } else if (requestCode == SELECT_FILE4) {
                    String result = CaptureConstants.CAPTURE4;
                    // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                    onSelectFromGalleryResult(data, result);
                } else if (requestCode == SELECT_FILE5) {
                    String result = CaptureConstants.CAPTURE5;
                    // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                    onSelectFromGalleryResult(data, result);
                } else if (requestCode == REQUEST_CAMERA1) {
                    String result = CaptureConstants.CAPTURE1;
                    // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                    onCaptureImageResult(data, result);
                } else if (requestCode == REQUEST_CAMERA2) {
                    String result = CaptureConstants.CAPTURE2;
                    // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                    onCaptureImageResult(data, result);
                } else if (requestCode == REQUEST_CAMERA3) {
                    String result = CaptureConstants.CAPTURE3;
                    // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                    onCaptureImageResult(data, result);
                } else if (requestCode == REQUEST_CAMERA4) {
                    String result = CaptureConstants.CAPTURE4;
                    // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                    onCaptureImageResult(data, result);


                } else if (requestCode == REQUEST_CAMERA5) {
                    String result = CaptureConstants.CAPTURE5;
                    // Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                    onCaptureImageResult(data, result);


                }


                    /*Place place = PlaceAutocomplete.getPlace(this, data);
                    Toast.makeText(this, "place "+place.toString(),
                            Toast.LENGTH_LONG).show();*/

            }
        } else {
            Log.e("MYLOCATIONTAG", "Main Result not ok");
            //Toast.makeText(context, "Result Not ok", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //Toast.makeText(this, resources.getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                    Toasty.error(this, resources.getString(R.string.permission_denied), Toast.LENGTH_LONG, true).show();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static String ADDRESS_LINE1 = "";
    public static String ADDRESS_LINE2 = "";
    public static String CITY = "";
    public static String DISTRICT = "";
    public static String STATE = "";
    public static String COUNTRY = "";
    private int flag = 0;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                try {
                    Geocoder gcd = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses = gcd.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);

                    if (addresses.size() > 0) {
                        String countryCode = addresses.get(0).getCountryCode();
                        String countryName = addresses.get(0).getCountryName();
                        String adminArea = addresses.get(0).getAdminArea();
                        String locality = addresses.get(0).getLocality();
                        String subAminArea = addresses.get(0).getSubAdminArea();
                        String subLocality = addresses.get(0).getSubLocality();

                        String featureName = addresses.get(0).getFeatureName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String phone = addresses.get(0).getPhone();
                        String subThoroughfare = addresses.get(0).getSubThoroughfare();
                        String thoroughfare = addresses.get(0).getThoroughfare();
                        String premises = addresses.get(0).getPremises();
                        StringBuilder sb = new StringBuilder();
                        String[] values = addresses.get(0).getAddressLine(0).split(",");


                        if (flag == 0) {

                            if (values.length > 1) {
                                ADDRESS_LINE1 = values[0];
                            }

                            if (values.length > 2) {
                                ADDRESS_LINE2 = values[1];
                            }

                            Log.e(TAG, "Data Address " + addresses.get(0).getAddressLine(0));
                            COUNTRY = countryName;
                            if (adminArea != null) {
                                STATE = adminArea;
                            }
                            if (locality != null) {
                                CITY = locality;
                            }
                            flag = 1;
                        }
                        /*Log.e(TAG, " MaxAddressLineIndex " + sb.toString());
                        Log.e(TAG, "Country " + countryName);
                        Log.e(TAG, "Country Code" + countryCode);
                        Log.e(TAG, "AdminArea " + adminArea);
                        Log.e(TAG, "locality " + locality);
                        Log.e(TAG, "subAdmin " + subAminArea);
                        Log.e(TAG, "subLocality " + subLocality);

                        Log.e(TAG, "featureName " + featureName);
                        Log.e(TAG, "postalCode " + postalCode);
                        Log.e(TAG, "phone " + phone);
                        Log.e(TAG, "subThoroughfare " + subThoroughfare);
                        Log.e(TAG, "thoroughfare " + thoroughfare);
                        Log.e(TAG, "premises " + premises);*/
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void isLastItemRemoved(boolean isLatsItem, int position) {
        if (isLatsItem) {
            if (farmDetailsLAyout.getVisibility() == View.VISIBLE)
                farmDetailsLAyout.setVisibility(View.GONE);
            addFarmTv.setText(resources.getString(R.string.add_farm_label));
        } else {
            addFarmTv.setText(resources.getString(R.string.add_more_farm_label));
        }
    }

    @Override
    public void onDataSetChanged(List<FarmData> farmData) {
        Log.e(TAG, "Farm " + new Gson().toJson(farmData));
        farmerAndFarmDataCache.setFarmDataList(farmList);
        updateCache();
    }

    @Override
    public void onOwnerShipImageClick(int index, EditText editText) {
        ownershipImageEt = editText;
        imageIndex = index;
        selectImage(CaptureConstants.CAPTURE5);
    }

    @Override
    public void onFarmImageClick(int index, EditText editText) {
        farmImageEt = editText;
        imageIndex = index;
        selectImage(CaptureConstants.CAPTURE4);
    }




    /* @Override
    public void onContactsLoaded(List<GpsLog> gpsLogList) {

        String[] lati_cord, longi_cord, enter_date, sv_id, time;
        if (gpsLogList != null && gpsLogList.size() > 0) {
            Log.e(TAG, "Uploading Sup Loc");
            lati_cord = new String[gpsLogList.size()];
            longi_cord = new String[gpsLogList.size()];
            enter_date = new String[gpsLogList.size()];
            sv_id = new String[gpsLogList.size()];
            time = new String[gpsLogList.size()];
            for (int i = 0; i < gpsLogList.size(); i++) {
                //JSONObject contact = new JSONObject();
                lati_cord[i] = gpsLogList.get(i).getLat_cord().trim();
                longi_cord[i] = gpsLogList.get(i).getLong_cord().trim();
                sv_id[i] = gpsLogList.get(i).getSv_id().trim();
                enter_date[i] = gpsLogList.get(i).getEnter_date().trim();
                time[i] = gpsLogList.get(i).getTime().trim();
            }

            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
            SendGpsArray sendGpsArray = new SendGpsArray();
            //sendGpsArray.setJsonObject(contactsObj);
            sendGpsArray.setLat_cord(lati_cord);
            sendGpsArray.setLong_cord(longi_cord);
            sendGpsArray.setEnter_date(enter_date);
            sendGpsArray.setSv_id(sv_id);
            sendGpsArray.setTime(time);
            sendGpsArray.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            sendGpsArray.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            Call<RecieveResponseGpsBack> statusMsgModelCall = apiService.getStatusMsgforUploadGpsInBack(sendGpsArray);

            statusMsgModelCall.enqueue(new Callback<RecieveResponseGpsBack>() {
                @Override
                public void onResponse(Call<RecieveResponseGpsBack> call, Response<RecieveResponseGpsBack> response) {
                    if (response.isSuccessful()) {
                        if (response != null) {

                            RecieveResponseGpsBack statusMsgModel = response.body();
                            if (statusMsgModel.getStatus() != 0 && statusMsgModel.getStatus() != 10) {
                                GpsCacheManager.getInstance(context).deleteAllGps(null);
                            }
                        }
                    } else {
                        try {
                            Log.e(TAG, "Not Successfull" + response.errorBody().string().toString() + "   Status" + response.code() + "  Message" + response.message());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RecieveResponseGpsBack> call, Throwable t) {
                    Log.e(TAG, "On Failure" + t.toString());
                }
            });

        } else {
            Log.e(TAG, "No Gps to upload");
        }
    }

    @Override
    public void onContactsAdded() {

    }

    @Override
    public void onDataNotAvailable() {

    }*/

    @Override
    protected void onStart() {
        super.onStart();
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertForExit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        showAlertForExit();
    }

    private void showAlertForExit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.setMessage(resources.getString(R.string.are_you_sure_exit_without_saving))
                .setTitle(resources.getString(R.string.alert_title_label))
                .setCancelable(false)
                .setPositiveButton(resources.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(resources.getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }


    private class SubmitAreaAsync extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            String[] lat = new String[latLngListCheckArea.size()];
            String[] lng = new String[latLngListCheckArea.size()];

            for (int j = 0; j < latLngListCheckArea.size(); j++) {
                lat[j] = String.valueOf(latLngListCheckArea.get(j).latitude);
                lng[j] = String.valueOf(latLngListCheckArea.get(j).longitude);
            }

            //demosupervisor1
            try {
                String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                VerifySendData verifySendData = new VerifySendData();
                verifySendData.setZoom(String.valueOf(mapZoom));
                verifySendData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                verifySendData.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                verifySendData.setArea(areaInAcre);
                verifySendData.setLat(lat);
                verifySendData.setDeleted_by(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                verifySendData.setLng(lng);
                verifySendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                verifySendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                Call<StatusMsgModel> statusMsgModelCall = apiService.getMsgStatusForVerifyFarm(verifySendData);
                Log.e(TAG, "Sending Data " + new Gson().toJson(verifySendData));
                statusMsgModelCall.enqueue(new Callback<StatusMsgModel>() {
                    @Override
                    public void onResponse(Call<StatusMsgModel> call, retrofit2.Response<StatusMsgModel> response) {

                        if (response.isSuccessful()) {
                            Log.e("Area Response", new Gson().toJson(response.body()));
                            StatusMsgModel statusMsgModel = response.body();
                            if (statusMsgModel.getStatus() == 1) {
//                                progressBar.setVisibility(View.INVISIBLE);
                                hideProgress();
                                Toasty.success(context, resources.getString(R.string.area_successfully_added), Toast.LENGTH_SHORT, true).show();
                                startActivity(new Intent(context, LandingActivity.class));
                                finishAffinity();
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                hideProgress();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                hideProgress();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.authorization_expired));
                            } else if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                hideProgress();
                                viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, response.body().getMsg());
                            } else {
//                                progressBar.setVisibility(View.INVISIBLE);
                                hideProgress();
//                                ViewFailDialog viewFailDialog = new ViewFailDialog();
//                                viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_verify_farm_area));
                                Toasty.error(context, resources.getString(R.string.failed_to_verify_farm_area), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            hideProgress();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            hideProgress();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            try {
                                hideProgress();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_verify_farm_area));
                                Log.e(TAG, "Error " + response.errorBody().string().toString());
//                                progressBar.setVisibility(View.INVISIBLE);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                        Log.e(TAG, "Failure " + t.toString());
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_verify_farm_area));
//                        progressBar.setVisibility(View.INVISIBLE);
                        hideProgress();
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String s) {


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }


    private void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                progressBar_image.setVisibility(View.GONE);
            }
        });
    }


    public void animate(final View cardView, ImageView down_arrow_img) {
        if (cardView.getVisibility() == View.GONE) {
            RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setInterpolator(new DecelerateInterpolator());
            rotateAnimation.setRepeatCount(0);
            rotateAnimation.setDuration(500);
            rotateAnimation.setFillAfter(true);
            down_arrow_img.startAnimation(rotateAnimation);
            cardView.setFocusable(true);

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_down);
            animation.setDuration(500);
            cardView.setAnimation(animation);
            cardView.animate();
            animation.start();
            cardView.setVisibility(View.VISIBLE);
        } else {
            RotateAnimation rotateAnimation = new RotateAnimation(180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setInterpolator(new DecelerateInterpolator());
            rotateAnimation.setRepeatCount(0);
            rotateAnimation.setDuration(500);
            rotateAnimation.setFillAfter(true);
            down_arrow_img.startAnimation(rotateAnimation);

            cardView.setVisibility(View.INVISIBLE);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            animation.setDuration(500);
            cardView.setAnimation(animation);
            cardView.animate();
            animation.start();
            cardView.setFocusable(false);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    cardView.setVisibility(View.GONE);
                }
            }, 500);
        }

    }

    private class FarmAndFarmerAsync extends AsyncTask<String, Integer, String> {
        FarmerAndFarmData farmerAndFarmData;

        public FarmAndFarmerAsync(FarmerAndFarmData farmerAndFarmData) {
            this.farmerAndFarmData = farmerAndFarmData;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject farmData = new JSONObject();
                /*if (AppConstants.isValidString(farmerAndFarmData.getSpouseDob()))
                    farmData.put("spouse_dob", AppConstants.getUploadableDate(farmerAndFarmData.getSpouseDob(), context));
                else
                    farmData.put("spouse_dob", null);*/

                if (selectedPersonDateSpouse == SELECTED_TYPE.DOB && AppConstants.isValidString(personDobStrSpouse)) {
                    farmData.put("spouse_dob", AppConstants.getUploadableDate(personDobStrSpouse, context));
                } else if (selectedPersonDateSpouse == SELECTED_TYPE.AGE && AppConstants.isValidString(personDobStrSpouse)) {
                    try {
                        farmData.put("spouse_dob", getDobFromAge(Integer.valueOf(personDobStrSpouse.trim())) + "-01-01");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (selectedPersonDateSpouse == SELECTED_TYPE.YOB && AppConstants.isValidString(personDobStrSpouse)) {
                    farmData.put("spouse_dob", personDobStrSpouse.trim() + "-01-01");
                }

                if (selectedPersonDate == SELECTED_TYPE.DOB && AppConstants.isValidString(personDobStr)) {
                    farmData.put("dob", AppConstants.getUploadableDate(personDobStr, context));
                } else if (selectedPersonDate == SELECTED_TYPE.AGE && AppConstants.isValidString(personDobStr)) {
                    try {
                        farmData.put("yob", getYOBByAge(Integer.valueOf(personDobStr.trim())));
                    } catch (Exception e) {

                    }
                } else if (selectedPersonDate == SELECTED_TYPE.YOB && AppConstants.isValidString(personDobStr)) {
                    farmData.put("yob", personDobStr.trim());
                }


                farmData.put("img_link", imgBase64);

                farmData.put("literacy", farmerAndFarmData.getLiteracy());
                farmData.put("financial_status", farmerAndFarmData.getFinancial_status());

                farmData.put("caste_category", farmerAndFarmData.getCasteCategoryId());
                farmData.put("caste", farmerAndFarmData.getCasteId());
                farmData.put("mobile_operator", farmerAndFarmData.getMobileNetworkId());
                farmData.put("religion", farmerAndFarmData.getReligionId());
                farmData.put("mia", farmerAndFarmData.getMiaId());

                farmData.put("pdistrict", farmerAndFarmData.getDistrict());
                farmData.put("pstate", farmerAndFarmData.getState());
                farmData.put("pcountry", farmerAndFarmData.getCountry());
                farmData.put("family_mem_count", farmerAndFarmData.getFamily_mem_count());
                farmData.put("adults_count", farmerAndFarmData.getAdults_count());
                farmData.put("kids_count", farmerAndFarmData.getKids_count());
                farmData.put("dependent_count", farmerAndFarmData.getDependent_count());
                farmData.put("isFarmAndFarmer", farmerAndFarmData.getIsFarmAndFarmer());
                farmData.put("upi_id", farmerAndFarmData.getUpi_id());
                farmData.put("civil_status", farmerAndFarmData.getCivil_status());
                farmData.put("spouse_name", farmerAndFarmData.getSpouse_name());
                farmData.put("beneficiary_name", farmerAndFarmData.getBeneficiary_name());
                farmData.put("image", farmerAndFarmData.getImage());
                farmData.put("isUsingPhone", farmerAndFarmData.getIsUsingPhone());
                farmData.put("isSmartPhone", farmerAndFarmData.getIsSmartPhone());
                farmData.put("isShareHolder", farmerAndFarmData.getIsShareHolder());
                farmData.put("sv_id", farmerAndFarmData.getSvId());
                farmData.put("sv_name", farmerAndFarmData.getSvName());

                JSONObject shareJson = null;
                try {
                    shareJson = new JSONObject(new Gson().toJson(farmerAndFarmData.getShareJson()));
                } catch (Exception e) {

                }
                if (shareJson != null)
                    farmData.put("share_json", shareJson);
                farmData.put("name", farmerAndFarmData.getName());
                farmData.put("father_name", farmerAndFarmData.getFather_name());
                farmData.put("mob", farmerAndFarmData.getMob());
                farmData.put("mob2", farmerAndFarmData.getMob2());
                farmData.put("email", farmerAndFarmData.getEmail());
                farmData.put("aadhar", farmerAndFarmData.getAadhaar());
                farmData.put("pan", farmerAndFarmData.getPan());
                farmData.put("pAddL1", farmerAndFarmData.getAddL1());
                farmData.put("pAddL2", farmerAndFarmData.getAddL2());
                farmData.put("pvillage_or_city", farmerAndFarmData.getVillage_or_city());
                farmData.put("pmandal_or_tehsil", farmerAndFarmData.getMandal_or_tehsil());
//                farmData.put("pdistrict", farmerAndFarmData.getDistrict());
//                farmData.put("pstate", farmerAndFarmData.getState());
//                farmData.put("pcountry", farmerAndFarmData.getCountry());
                farmData.put("bank_name", farmerAndFarmData.getBankName());
                farmData.put("account_no", farmerAndFarmData.getAccountNumber());
                farmData.put("account_name", farmerAndFarmData.getHolderName());
                farmData.put("branch", farmerAndFarmData.getBranch());
                farmData.put("ifsc", farmerAndFarmData.getIfscCode());
                farmData.put("cluster_id", farmerAndFarmData.getClusterId());
                farmData.put("comp_id", farmerAndFarmData.getCompId());
                farmData.put("added_by", farmerAndFarmData.getAdded_by());
                farmData.put("owner_id", farmerAndFarmData.getPersonId());
                farmData.put("user_id", farmerAndFarmData.getUserId());
                farmData.put("token", farmerAndFarmData.getToken());
                farmData.put("countryCode", farmerAndFarmData.getCountryCode());

                farmData.put("caste", farmerAndFarmData.getCast());
                farmData.put("est_income", farmerAndFarmData.getAnualIncome());
                farmData.put("gender", farmerAndFarmData.getGeder());
                farmData.put("swift_code", farmerAndFarmData.getSwiftCode());
                farmData.put("religion", farmerAndFarmData.getReligion());

                JSONArray items = new JSONArray();

                for (int i = 0; i < farmerAndFarmData.getFarmDataList().size(); i++) {
                    FarmData farmData1 = farmerAndFarmData.getFarmDataList().get(i);
                    JSONObject farm = new JSONObject();
                    if (AppConstants.isValidString(farmData1.getOwnerShipDoc()))
                        farm.put("ownership_doc", farmData1.getOwnerShipDoc());
                    if (AppConstants.isValidString(farmData1.getFarmImage()))
                        farm.put("farm_photo", farmData1.getFarmImage());

                    farm.put("planting_method", farmData1.getPlanting_method());
                    farm.put("motor_hp", farmData1.getMotorHp());
                    farm.put("transplant_date", farmData1.getTransplant_date());
                    farm.put("fcountry", farmData1.getCountry());
                    farm.put("fstate", farmData1.getState());
                    farm.put("fdistrict", farmData1.getDistrict());
                    farm.put("is_owned", farmData1.getIsOwned());
                    farm.put("owner_id", farmData1.getOwnerId());
                    farm.put("farm_id_offline", farmData1.getFarmIdOffline());
                    farm.put("lot_no", farmData1.getId());
                    farm.put("farm_name", farmData1.getName());
                    farm.put("exp_area", farmData1.getArea());
                    farm.put("season_num", farmData1.getSeason());
                    farm.put("cluster_id", farmerAndFarmData.getClusterId());
                    farm.put("previous_crop", farmData1.getPreviouscrop());
                    farm.put("irrigation_source_id", farmData1.getIrrigationsource());
                    farm.put("irri_source_name", farmData1.getIrriSourceName());
                    farm.put("irrigation_type_id", farmData1.getIrrigationtype());
                    farm.put("cropping_pattern", farmData1.getCropping_pattern());
                    farm.put("is_irrigated", farmData1.getIs_irrigated());
                    farm.put("irri_type_name", farmData1.getIrriTypename());
                    farm.put("soil_type_id", farmData1.getSoiltype());
                    farm.put("soil_type_name", farmData1.getSoiltypeName());
                    farm.put("fAddL1", farmData1.getAddL1());
                    farm.put("fAddL2", farmData1.getAddL2());
                    farm.put("fvillage_or_city", farmData1.getVillageOrCity());
                    farm.put("fmandal_or_tehsil", farmData1.getMandalOrTehsil());
//                    farm.put("fdistrict", farmData1.getDistrict());
//                    farm.put("fstate", farmData1.getState());
//                    farm.put("fcountry", farmData1.getCountry());
                    farm.put("comp_id", farmerAndFarmData.getCompId());
                    farm.put("added_by", farmerAndFarmData.getAdded_by());
                    farm.put("lat_cord", farmData1.getLat_cord());
                    farm.put("long_cord", farmData1.getLong_cord());
                    farm.put("crop_name", farmData1.getCurrentCropName());
//                    if (farmData1.getCropId()!=null&&farmData1.getCropId().equals(resources.getString(R.string.select_soil_type_prompt)))
                    farm.put("crop_id", farmData1.getCropId());
                    if (farmData1.getAssetsList() != null && farmData1.getAssetsList().size() > 0) {
                        JSONObject asset = new JSONObject();
                        for (int j = 0; j < farmData1.getAssetsList().size(); j++) {
                            if (farmData1.getAssetsList().get(j) != null && farmData1.getAssetsList().get(j).size() > 0) {
                                String name = "";
                                JSONArray assetsArray = new JSONArray();
                                for (int k = 0; k < farmData1.getAssetsList().get(j).size(); k++) {
                                    JSONObject assetObj = new JSONObject();
                                    if (k == 0) {
                                        name = farmData1.getAssetsList().get(j).get(k).getType();
                                    }
                                    if (AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol1())
                                            && AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol1Value())) {
                                        assetObj.put(farmData1.getAssetsList().get(j).get(k).getCol1(),
                                                farmData1.getAssetsList().get(j).get(k).getCol1Value());
                                    }
                                    if (AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol2())
                                            && AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol2Value())) {
                                        assetObj.put(farmData1.getAssetsList().get(j).get(k).getCol2(),
                                                farmData1.getAssetsList().get(j).get(k).getCol2Value());
                                    }
                                    if (AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol3())
                                            && AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol3Value())) {
                                        assetObj.put(farmData1.getAssetsList().get(j).get(k).getCol3(),
                                                farmData1.getAssetsList().get(j).get(k).getCol3Value());
                                    }
                                    if (AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol4())
                                            && AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol4Value())) {
                                        assetObj.put(farmData1.getAssetsList().get(j).get(k).getCol4(),
                                                farmData1.getAssetsList().get(j).get(k).getCol4Value());
                                    }
                                    if (AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol5())
                                            && AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol5Value())) {
                                        assetObj.put(farmData1.getAssetsList().get(j).get(k).getCol5(),
                                                farmData1.getAssetsList().get(j).get(k).getCol5Value());
                                    }
                                    if (AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol6())
                                            && AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol6Value())) {
                                        assetObj.put(farmData1.getAssetsList().get(j).get(k).getCol6(),
                                                farmData1.getAssetsList().get(j).get(k).getCol6Value());
                                    }
                                    if (assetObj != null && assetObj.length() > 0)
                                        assetsArray.put(assetObj);
                                }

                                if (assetsArray != null && assetsArray.length() > 0)
                                    asset.put(name, assetsArray);
                            }

                        }
                        if (asset != null && asset.length() > 0)
                            farm.put("asset", asset);
                    }

                    if (farmData1.getFpoCropList() != null && farmData1.getFpoCropList().size() > 0) {
                        JSONArray cropArray = new JSONArray();
                        for (int l = 0; l < farmData1.getFpoCropList().size(); l++) {
                            JSONObject crop = new JSONObject();
                            crop.put("season_id", farmData1.getFpoCropList().get(l).getSeasonId());
                            crop.put("crop_id", farmData1.getFpoCropList().get(l).getCropId());
                            crop.put("farm_area", farmData1.getFpoCropList().get(l).getFarmArea());
//                            crop.put("datatype",farmData1.getFpoCropList().get(l).getda());
//                            crop.put("profit_loss",farmData1.getFpoCropList().get(l).getProfitLoss());
//                            crop.put("satisfactory",farmData1.getFpoCropList().get(l).getSatisfactory());

                            List<List<CropFormDatum>> cropFormDatumArrayLists = farmData1.getFpoCropList().get(l).getCropFormDatumArrayLists();

                            if (cropFormDatumArrayLists != null && cropFormDatumArrayLists.size() > 0) {
                                JSONObject asset = new JSONObject();
                                for (int j = 0; j < cropFormDatumArrayLists.size(); j++) {
                                    List<CropFormDatum> cropFormDatumList = cropFormDatumArrayLists.get(j);
                                    if (cropFormDatumList != null && cropFormDatumList.size() > 0) {
                                        String name = "";
                                        JSONArray assetsArray = new JSONArray();
                                        for (int k = 0; k < cropFormDatumList.size(); k++) {
                                            JSONObject assetObj = new JSONObject();
                                            CropFormDatum cropFormDatum = cropFormDatumList.get(k);
                                            if (k == 0) {
                                                name = cropFormDatum.getType();
                                            }
                                            if (AppConstants.isValidString(cropFormDatum.getCol1())
                                                    && AppConstants.isValidString(cropFormDatum.getCol1Value())) {
                                                assetObj.put(cropFormDatum.getCol1(),
                                                        cropFormDatum.getCol1Value());
                                            }
                                            if (AppConstants.isValidString(cropFormDatum.getCol2())
                                                    && AppConstants.isValidString(cropFormDatum.getCol2Value())) {
                                                assetObj.put(cropFormDatum.getCol2(),
                                                        cropFormDatum.getCol2Value());
                                            }
                                            if (AppConstants.isValidString(cropFormDatum.getCol3())
                                                    && AppConstants.isValidString(cropFormDatum.getCol3Value())) {
                                                assetObj.put(cropFormDatum.getCol3(),
                                                        cropFormDatum.getCol3Value());
                                            }
                                            if (AppConstants.isValidString(cropFormDatum.getCol4())
                                                    && AppConstants.isValidString(cropFormDatum.getCol4Value())) {
                                                assetObj.put(cropFormDatum.getCol4(),
                                                        cropFormDatum.getCol4Value());
                                            }
                                            if (AppConstants.isValidString(cropFormDatum.getCol5())
                                                    && AppConstants.isValidString(cropFormDatum.getCol5Value())) {
                                                assetObj.put(cropFormDatum.getCol5(), cropFormDatum.getCol5Value());
                                            }
                                            if (AppConstants.isValidString(cropFormDatum.getCol6())
                                                    && AppConstants.isValidString(cropFormDatum.getCol6Value())) {
                                                assetObj.put(cropFormDatum.getCol6(), cropFormDatum.getCol6Value());
                                            }
                                            if (assetObj != null && assetObj.length() > 0)
                                                assetsArray.put(assetObj);
                                        }

                                        if (assetsArray != null && assetsArray.length() > 0)
                                            asset.put(name, assetsArray);
                                    }
                                }
                                JSONArray asstArray = new JSONArray();
                                asstArray.put(asset);
                                crop.put("farm_add_info_json", asstArray);
                            }
                            cropArray.put(crop);
                        }
                        farm.put("crop", cropArray);
                    }
                    items.put(farm);
                }
                farmData.putOpt("items", items);
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParser.parse(farmData.toString());
                Log.e(TAG, "Uploading1 " + new Gson().toJson(jsonObject));
                String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                apiInterface.insertFarmerAndFarm(jsonObject).enqueue(new Callback<FarmerFarmInsertResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(Call<FarmerFarmInsertResponse> call, Response<FarmerFarmInsertResponse> response) {
                        String error = null;
                        isLoaded[0] = true;
                        if (response.isSuccessful()) {
                            Log.e(TAG, "Farm response " + new Gson().toJson(response.body()));
                            if (response.body().getStatus() == 10) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar_image.setVisibility(View.GONE);
                                        tv_add_new_farm_submit_bt.setClickable(true);
                                        tv_add_new_farm_submit_bt.setEnabled(true);
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.multiple_login_msg));
                                    }
                                });
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar_image.setVisibility(View.GONE);
                                        tv_add_new_farm_submit_bt.setClickable(true);
                                        tv_add_new_farm_submit_bt.setEnabled(true);
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.unauthorized_access));
                                    }
                                });
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar_image.setVisibility(View.GONE);
                                        tv_add_new_farm_submit_bt.setClickable(true);
                                        tv_add_new_farm_submit_bt.setEnabled(true);
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.authorization_expired));
                                    }
                                });
                            } else if (response.body().getStatus() == 1) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            AddFarmCCacheManager.getInstance(context).deleteAllData();
                                            Toasty.success(context, resources.getString(R.string.farmer_and_farm_added_succes_msg), Toast.LENGTH_LONG).show();
                                            long personId = 0;
                                            if (response.body().getPersonId() != null && response.body().getPersonId().size() > 0)
                                                personId = response.body().getPersonId().get(0);
                                            if (personId != 0 && myBitmapId != null) {
                                                uploadIDImage(personId + "");
                                            } else if (personId != 0 && myBitmapAddress != null) {
                                                uploadAddressIdImage(personId + "");
                                            } else if (latLngListCheckArea != null && latLngListCheckArea.size() > 2) {
                                                progressBar_image.setVisibility(View.VISIBLE);
                                                AddFarmActivity.SubmitAreaAsync areaAsync = new AddFarmActivity.SubmitAreaAsync();
                                                areaAsync.execute();
                                            } else {
                                                DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.FARMER_LIST);
                                                Intent intent = new Intent(context, LandingActivity.class);
                                                ActivityOptions options = null;
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                                                }
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                    finishAffinity();
                                                    startActivity(intent, options.toBundle());
                                                    finish();
                                                } else {
                                                    finishAffinity();
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                });
                            } else if (response.body().getStatus() == 2) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar_image.setVisibility(View.GONE);
                                        tv_add_new_farm_submit_bt.setClickable(true);
                                        tv_add_new_farm_submit_bt.setEnabled(true);
                                        Toasty.error(context, resources.getString(R.string.already_registered_mobile_msg), Toast.LENGTH_LONG).show();
                                        phoneNumberEt.setError(resources.getString(R.string.invalid_number_message_label));
                                        phoneNumberEt.getParent().requestChildFocus(phoneNumberEt, phoneNumberEt);
                                    }
                                });

                            } else {
                                try {
                                    progressBar_image.setVisibility(View.GONE);
                                    tv_add_new_farm_submit_bt.setClickable(true);
                                    tv_add_new_farm_submit_bt.setEnabled(true);
                                    Toasty.error(context, resources.getString(R.string.failed_add_farm_and_farmer_msg), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                }
                            }

                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar_image.setVisibility(View.GONE);
                                    tv_add_new_farm_submit_bt.setClickable(true);
                                    tv_add_new_farm_submit_bt.setEnabled(true);
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.unauthorized_access));
                                }
                            });
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar_image.setVisibility(View.GONE);
                                    tv_add_new_farm_submit_bt.setClickable(true);
                                    tv_add_new_farm_submit_bt.setEnabled(true);
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.authorization_expired));
                                }
                            });
                        } else {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_add_new_farm_submit_bt.setClickable(true);
                                        tv_add_new_farm_submit_bt.setEnabled(true);
                                        progressBar_image.setVisibility(View.GONE);
                                        Toasty.error(context, resources.getString(R.string.failed_add_farm_and_farmer_msg), Toast.LENGTH_LONG).show();
                                    }
                                });
                                error = response.errorBody().string().toString();
                                Log.e(TAG, "Farm error " + error + " code " + response.code());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                                notifyApiDelay(AddFarmActivity.this, response.raw().request().url().toString(),
                                        "" + diff, internetSPeed, error, response.code());
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onFailure(Call<FarmerFarmInsertResponse> call, Throwable t) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    progressBar_image.setVisibility(View.GONE);
                                    tv_add_new_farm_submit_bt.setClickable(true);
                                    tv_add_new_farm_submit_bt.setEnabled(true);
                                    isLoaded[0] = true;
                                    long newMillis = AppConstants.getCurrentMills();
                                    long diff = newMillis - currMillis;
                                    notifyApiDelay(AddFarmActivity.this, call.request().url().toString(),
                                            "" + diff, internetSPeed, t.toString(), 0);
                                    Log.e(TAG, "Farm Failure " + t.toString());
                                    Toasty.error(context, resources.getString(R.string.failed_add_farm_and_farmer_msg), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {

                                }
                            }
                        });
                    }
                });
                try {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                internetFlow(isLoaded[0]);
                            } catch (Exception e) {
                            }
                        }
                    }, AppConstants.DELAY);
                } catch (Exception e) {

                }
                notifyApiDelay(AddFarmActivity.this, "additional_farm/insert_farmer_new",
                        "" + "10000000", internetSPeed, "", 0);
                DropDownCacheManager.getInstance(context).deleteViaType("NEW_FARM_DATA_ADDITION");
                DropDownT material = new DropDownT("NEW_FARM_DATA_ADDITION", new Gson().toJson(jsonObject));
                DropDownCacheManager.getInstance(context).addChemicalUnit(material);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Exception Submit " + e.toString());
            }
            return null;
        }
    }

    private class FarmAsync extends AsyncTask<String, Integer, String> {
        FarmerAndFarmData farmerAndFarmData;
        String personId, clusterId;

        public FarmAsync(FarmerAndFarmData farmerAndFarmData, String personId, String clusterId) {
            this.farmerAndFarmData = farmerAndFarmData;
            this.personId = personId;
            this.clusterId = clusterId;
        }

        @Override
        protected String doInBackground(String... strings) {
            String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            try {
                JSONObject farmData = new JSONObject();
                farmData.put("cluster_id", clusterId);
                farmData.put("comp_id", compId);
                farmData.put("owner_id", personId);
                farmData.put("user_id", userId);
                farmData.put("token", token);
//                farmData.put("country_code", farmerAndFarmData.getCountryCode());
                farmData.put("dob", farmerAndFarmData.getDob());
                farmData.put("caste", farmerAndFarmData.getCast());
                farmData.put("est_income", farmerAndFarmData.getAnualIncome());
                farmData.put("gender", farmerAndFarmData.getGeder());
                farmData.put("swift_code", farmerAndFarmData.getSwiftCode());
                farmData.put("religion", farmerAndFarmData.getReligion());
                farmData.put("sv_id", farmerAndFarmData.getSvId());
                farmData.put("sv_name", farmerAndFarmData.getSvName());
                JSONArray items = new JSONArray();
                for (int i = 0; i < farmerAndFarmData.getFarmDataList().size(); i++) {
                    FarmData farmData1 = farmerAndFarmData.getFarmDataList().get(i);
                    JSONObject farm = new JSONObject();
                    if (AppConstants.isValidString(farmData1.getOwnerShipDoc()))
                        farm.put("ownership_doc", farmData1.getOwnerShipDoc());
                    if (AppConstants.isValidString(farmData1.getFarmImage()))
                        farm.put("farm_photo", farmData1.getFarmImage());
                    farm.put("motor_hp", farmData1.getMotorHp());
                    farm.put("transplant_date", farmData1.getTransplant_date());
                    farm.put("fcountry", farmData1.getCountry());
                    farm.put("fstate", farmData1.getState());
                    farm.put("fdistrict", farmData1.getDistrict());
                    farm.put("is_owned", farmData1.getIsOwned());
                    farm.put("owner_id", farmData1.getOwnerId());
                    farm.put("farm_id_offline", farmData1.getFarmIdOffline());
                    farm.put("lot_no", farmData1.getId());
                    farm.put("farm_name", farmData1.getName());
                    farm.put("exp_area", farmData1.getArea());
                    farm.put("season_num", farmData1.getSeason());
                    farm.put("cluster_id", farmerAndFarmData.getClusterId());
                    farm.put("previous_crop", farmData1.getPreviouscrop());
                    farm.put("irrigation_source_id", farmData1.getIrrigationsource());
                    farm.put("irri_source_name", farmData1.getIrriSourceName());
                    farm.put("irrigation_type_id", farmData1.getIrrigationtype());
                    farm.put("cropping_pattern", farmData1.getCropping_pattern());
                    farm.put("is_irrigated", farmData1.getIs_irrigated());
                    farm.put("irri_type_name", farmData1.getIrriTypename());
                    farm.put("soil_type_id", farmData1.getSoiltype());
                    farm.put("soil_type_name", farmData1.getSoiltypeName());
                    farm.put("fAddL1", farmData1.getAddL1());
                    farm.put("fAddL2", farmData1.getAddL2());
                    farm.put("fvillage_or_city", farmData1.getVillageOrCity());
                    farm.put("fmandal_or_tehsil", farmData1.getMandalOrTehsil());
//                    farm.put("fdistrict", farmData1.getDistrict());
//                    farm.put("fstate", farmData1.getState());
//                    farm.put("fcountry", farmData1.getCountry());
                    farm.put("comp_id", farmerAndFarmData.getCompId());
                    farm.put("added_by", farmerAndFarmData.getAdded_by());
                    farm.put("lat_cord", farmData1.getLat_cord());
                    farm.put("long_cord", farmData1.getLong_cord());
                    farm.put("crop_name", farmData1.getCurrentCropName());
                    farm.put("crop_id", farmData1.getCropId());
                    if (farmData1.getAssetsList() != null && farmData1.getAssetsList().size() > 0) {
                        JSONObject asset = new JSONObject();
                        for (int j = 0; j < farmData1.getAssetsList().size(); j++) {
                            if (farmData1.getAssetsList().get(j) != null && farmData1.getAssetsList().get(j).size() > 0) {
                                String name = "";
                                JSONArray assetsArray = new JSONArray();
                                for (int k = 0; k < farmData1.getAssetsList().get(j).size(); k++) {
                                    JSONObject assetObj = new JSONObject();
                                    if (k == 0) {
                                        name = farmData1.getAssetsList().get(j).get(k).getType();
                                    }
                                    if (AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol1()) &&
                                            AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol1Value())) {
                                        assetObj.put(farmData1.getAssetsList().get(j).get(k).getCol1(),
                                                farmData1.getAssetsList().get(j).get(k).getCol1Value());
                                    }
                                    if (AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol2())
                                            && AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol1())) {
                                        assetObj.put(farmData1.getAssetsList().get(j).get(k).getCol2Value(),
                                                farmData1.getAssetsList().get(j).get(k).getCol2Value());
                                    }
                                    if (AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol3())
                                            && AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol3Value())) {
                                        assetObj.put(farmData1.getAssetsList().get(j).get(k).getCol3(),
                                                farmData1.getAssetsList().get(j).get(k).getCol3Value());
                                    }
                                    if (AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol4())
                                            && AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol4Value())) {
                                        assetObj.put(farmData1.getAssetsList().get(j).get(k).getCol4(),
                                                farmData1.getAssetsList().get(j).get(k).getCol4Value());
                                    }
                                    if (AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol5())
                                            && AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol5Value())) {
                                        assetObj.put(farmData1.getAssetsList().get(j).get(k).getCol5(),
                                                farmData1.getAssetsList().get(j).get(k).getCol5Value());
                                    }
                                    if (AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol6())
                                            && AppConstants.isValidString(farmData1.getAssetsList().get(j).get(k).getCol6Value())) {
                                        assetObj.put(farmData1.getAssetsList().get(j).get(k).getCol6(),
                                                farmData1.getAssetsList().get(j).get(k).getCol6Value());
                                    }
                                    if (assetObj != null && assetObj.length() > 0)
                                        assetsArray.put(assetObj);
                                }

                                if (assetsArray != null && assetsArray.length() > 0)
                                    asset.put(name, assetsArray);
                            }

                        }
                        if (asset != null && asset.length() > 0)
                            farm.put("asset", asset);
                    }

                    if (farmData1.getFpoCropList() != null && farmData1.getFpoCropList().size() > 0) {
                        JSONArray cropArray = new JSONArray();
                        for (int l = 0; l < farmData1.getFpoCropList().size(); l++) {
                            JSONObject crop = new JSONObject();
                            crop.put("season_id", farmData1.getFpoCropList().get(l).getSeasonId());
                            crop.put("crop_id", farmData1.getFpoCropList().get(l).getCropId());
                            crop.put("farm_area", farmData1.getFpoCropList().get(l).getFarmArea());
//                            crop.put("datatype",farmData1.getFpoCropList().get(l).getda());
//                            crop.put("profit_loss",farmData1.getFpoCropList().get(l).getProfitLoss());
//                            crop.put("satisfactory",farmData1.getFpoCropList().get(l).getSatisfactory());

                            List<List<CropFormDatum>> cropFormDatumArrayLists = farmData1.getFpoCropList().get(l).getCropFormDatumArrayLists();

                            if (cropFormDatumArrayLists != null && cropFormDatumArrayLists.size() > 0) {
                                JSONObject asset = new JSONObject();
                                for (int j = 0; j < cropFormDatumArrayLists.size(); j++) {

                                    List<CropFormDatum> cropFormDatumList = cropFormDatumArrayLists.get(j);

                                    if (cropFormDatumList != null && cropFormDatumList.size() > 0) {
                                        String name = "";
                                        JSONArray assetsArray = new JSONArray();
                                        for (int k = 0; k < cropFormDatumList.size(); k++) {
                                            JSONObject assetObj = new JSONObject();
                                            CropFormDatum cropFormDatum = cropFormDatumList.get(k);
                                            if (k == 0) {
                                                name = cropFormDatum.getType();
                                            }
                                            if (AppConstants.isValidString(cropFormDatum.getCol1())
                                                    && AppConstants.isValidString(cropFormDatum.getCol1Value())) {
                                                assetObj.put(cropFormDatum.getCol1(),
                                                        cropFormDatum.getCol1Value());
                                            }
                                            if (AppConstants.isValidString(cropFormDatum.getCol2())
                                                    && AppConstants.isValidString(cropFormDatum.getCol2Value())) {
                                                assetObj.put(cropFormDatum.getCol2(),
                                                        cropFormDatum.getCol2Value());
                                            }
                                            if (AppConstants.isValidString(cropFormDatum.getCol3())
                                                    && AppConstants.isValidString(cropFormDatum.getCol3Value())) {
                                                assetObj.put(cropFormDatum.getCol3(),
                                                        cropFormDatum.getCol3Value());
                                            }
                                            if (AppConstants.isValidString(cropFormDatum.getCol4())
                                                    && AppConstants.isValidString(cropFormDatum.getCol4Value())) {
                                                assetObj.put(cropFormDatum.getCol4(),
                                                        cropFormDatum.getCol4Value());
                                            }
                                            if (AppConstants.isValidString(cropFormDatum.getCol5())
                                                    && AppConstants.isValidString(cropFormDatum.getCol5Value())) {
                                                assetObj.put(cropFormDatum.getCol5(), cropFormDatum.getCol5Value());
                                            }
                                            if (AppConstants.isValidString(cropFormDatum.getCol6())
                                                    && AppConstants.isValidString(cropFormDatum.getCol6Value())) {
                                                assetObj.put(cropFormDatum.getCol6(), cropFormDatum.getCol6Value());
                                            }
                                            if (assetObj != null && assetObj.length() > 0)
                                                assetsArray.put(assetObj);
                                        }
                                        if (assetsArray != null && assetsArray.length() > 0)
                                            asset.put(name, assetsArray);
                                    }
                                }
                                JSONArray asstArray = new JSONArray();
                                asstArray.put(asset);
                                crop.put("farm_add_info_json", asstArray);
//                                crop.put("farm_add_info_json", asset);
                            }
                            cropArray.put(crop);
                        }
                        farm.put("crop", cropArray);
                    }
                    items.put(farm);
                }
                farmData.putOpt("items", items);
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParser.parse(farmData.toString());

                /*
                DropDownCacheManager.getInstance(context).deleteViaType("NEW_FARM_DATA_ADDITION");
                DropDownT material = new DropDownT("NEW_FARM_DATA_ADDITION", new Gson().toJson(jsonObject));
                DropDownCacheManager.getInstance(context).addChemicalUnit(material);
*/

                Log.e(TAG, "Uploading Farm Dataj " + new Gson().toJson(jsonObject));
                String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                apiInterface.insertExistingFarmerFarm(jsonObject).enqueue(new Callback<StatusMsgModel>() {
                    @Override
                    public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                        String error = null;
                        isLoaded[0] = true;
                        if (response.isSuccessful()) {

                            if (response.body().getStatus() == 10) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_add_new_farm_submit_bt.setClickable(true);
                                        tv_add_new_farm_submit_bt.setEnabled(true);
                                        progressBar_image.setVisibility(View.GONE);
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.multiple_login_msg));
                                    }
                                });
                            } else if (response.body().getStatus() == 1) {


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            AddFarmCCacheManager.getInstance(context).deleteAllData();
                                            Toasty.success(context, resources.getString(R.string.farm_added_success_msg), Toast.LENGTH_LONG).show();
                                            if (latLngListCheckArea != null && latLngListCheckArea.size() > 2) {

                                                SubmitAreaAsync areaAsync = new SubmitAreaAsync();
                                                areaAsync.execute();
                                            } else {
                                                Intent intent = new Intent(context, LandingActivity.class);
                                                ActivityOptions options = null;
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                                                }
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                    finishAffinity();
                                                    startActivity(intent, options.toBundle());
                                                    finish();
                                                } else {
                                                    finishAffinity();
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toasty.error(context, resources.getString(R.string.failed_add_farm_msg), Toast.LENGTH_LONG).show();
                                        tv_add_new_farm_submit_bt.setClickable(true);
                                        tv_add_new_farm_submit_bt.setEnabled(true);
                                        progressBar_image.setVisibility(View.GONE);
                                    }

                                });
                            }

                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar_image.setVisibility(View.GONE);
                                    tv_add_new_farm_submit_bt.setClickable(true);
                                    tv_add_new_farm_submit_bt.setEnabled(true);
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.unauthorized_access));
                                }
                            });
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar_image.setVisibility(View.GONE);
                                    tv_add_new_farm_submit_bt.setClickable(true);
                                    tv_add_new_farm_submit_bt.setEnabled(true);
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(AddFarmActivity.this, resources.getString(R.string.authorization_expired));
                                }
                            });
                        } else {
                            try {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_add_new_farm_submit_bt.setClickable(true);
                                        tv_add_new_farm_submit_bt.setEnabled(true);
                                        progressBar_image.setVisibility(View.GONE);
                                    }
                                });
                                error = response.errorBody().string().toString();
                                Log.e(TAG, "addExistingFarmerFarm Err " + error);
                                Toasty.error(context, resources.getString(R.string.failed_add_farm_msg), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                                notifyApiDelay(AddFarmActivity.this, response.raw().request().url().toString(),
                                        "" + diff, internetSPeed, error, response.code());
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                        try {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_add_new_farm_submit_bt.setClickable(true);
                                    tv_add_new_farm_submit_bt.setEnabled(true);
                                    progressBar_image.setVisibility(View.GONE);
                                }
                            });
                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            isLoaded[0] = true;
                            notifyApiDelay(AddFarmActivity.this, call.request().url().toString(),
                                    "" + diff, internetSPeed, t.toString(), 0);
                            tv_add_new_farm_submit_bt.setEnabled(true);
                            Log.e(TAG, "addExistingFarmerFarm failure " + t.toString());
                            progressBar_image.setVisibility(View.GONE);
                            Toasty.error(context, resources.getString(R.string.failed_add_farm_msg), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                        }
                    }
                });
                try {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                internetFlow(isLoaded[0]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, AppConstants.DELAY);
                } catch (Exception e) {

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Exception Submit " + e.toString());
            }
            return null;
        }
    }

    private void getAllDataNew(final String type) {
        ParamData paramData = new ParamData();
        paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        paramData.setType(type);

        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        Log.e(TAG, "Sending New Param  AND type " + type + " " + new Gson().toJson(paramData));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getNewParameters(paramData).enqueue(new Callback<DDResponseNew>() {
            @Override
            public void onResponse(Call<DDResponseNew> call, Response<DDResponseNew> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    DDResponseNew response = response1.body();
                    Log.e(TAG, "Sending New Param Response " + type + "  " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, response.getMsg());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        if (response != null && response.getData() != null && response.getData().size() > 0) {
                            DropDownCacheManager2.getInstance(context).deleteViaType(type);
                            DropDownT2 material = new DropDownT2(type, new Gson().toJson(response));
                            DropDownCacheManager2.getInstance(context).addChemicalUnit(material);

                            try {
                                adapter.setIrrigationSourceDDList(response.getData());
                            } catch (Exception e) {
                            }
                        }
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
                        Log.e(TAG, "Cache Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DDResponseNew> call, Throwable t) {
                Log.e("OfflineModeAct", "Failure getOfflineData " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
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

    private void getAllDataNew2(final String type) {
        ParamData paramData = new ParamData();
        paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        paramData.setType(type);

        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        Log.e(TAG, "Sending New Param  AND type " + type + " " + new Gson().toJson(paramData));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getNewParameters(paramData).enqueue(new Callback<DDResponseNew>() {
            @Override
            public void onResponse(Call<DDResponseNew> call, Response<DDResponseNew> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    DDResponseNew response = response1.body();
                    Log.e(TAG, "Sending New Param Response " + type + "  " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, response.getMsg());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        if (response != null && response.getData() != null && response.getData().size() > 0) {
                            DropDownCacheManager2.getInstance(context).deleteViaType(type);
                            DropDownT2 material = new DropDownT2(type, new Gson().toJson(response));
                            DropDownCacheManager2.getInstance(context).addChemicalUnit(material);

                            try {
                                adapter.setIrrigationTypeDDList(response.getData());
                            } catch (Exception e) {
                            }
                        }
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
                        Log.e(TAG, "Cache Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DDResponseNew> call, Throwable t) {
                Log.e("OfflineModeAct", "Failure getOfflineData " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
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

    private void getAllDataNew3(final String type) {
        ParamData paramData = new ParamData();
        paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        paramData.setType(type);

        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        Log.e(TAG, "Sending New Param  AND type " + type + " " + new Gson().toJson(paramData));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getNewParameters(paramData).enqueue(new Callback<DDResponseNew>() {
            @Override
            public void onResponse(Call<DDResponseNew> call, Response<DDResponseNew> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    DDResponseNew response = response1.body();
                    Log.e(TAG, "Sending New Param Response " + type + "  " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, response.getMsg());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        if (response != null && response.getData() != null && response.getData().size() > 0) {
                            DropDownCacheManager2.getInstance(context).deleteViaType(type);
                            DropDownT2 material = new DropDownT2(type, new Gson().toJson(response));
                            DropDownCacheManager2.getInstance(context).addChemicalUnit(material);

                            try {
                                adapter.setSoilTypeDDList(response.getData());
                            } catch (Exception e) {
                            }
                        }
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
                        Log.e(TAG, "Cache Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DDResponseNew> call, Throwable t) {
                Log.e("OfflineModeAct", "Failure getOfflineData " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
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

    private void getAllDataNew4(final String type) {
        ParamData paramData = new ParamData();
        paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        paramData.setType(type);

        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        Log.e(TAG, "Sending New Param  AND type " + type + " " + new Gson().toJson(paramData));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getNewParameters(paramData).enqueue(new Callback<DDResponseNew>() {
            @Override
            public void onResponse(Call<DDResponseNew> call, Response<DDResponseNew> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    DDResponseNew response = response1.body();
                    Log.e(TAG, "Sending New Param Response " + type + "  " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, response.getMsg());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        if (response != null && response.getData() != null && response.getData().size() > 0) {
                            DropDownCacheManager2.getInstance(context).deleteViaType(type);
                            DropDownT2 material = new DropDownT2(type, new Gson().toJson(response));
                            DropDownCacheManager2.getInstance(context).addChemicalUnit(material);
                            try {
                                adapter.setFarmCropList(response.getData());
                            } catch (Exception e) {
                            }
                        }
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
                        Log.e(TAG, "Cache Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DDResponseNew> call, Throwable t) {
                Log.e("OfflineModeAct", "Failure getOfflineData " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
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

    private void getAllDataNew5(final String type) {
        ParamData paramData = new ParamData();
        paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        paramData.setType(type);

        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        Log.e(TAG, "Sending New Param  AND type " + type + " " + new Gson().toJson(paramData));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getNewParameters(paramData).enqueue(new Callback<DDResponseNew>() {
            @Override
            public void onResponse(Call<DDResponseNew> call, Response<DDResponseNew> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    DDResponseNew response = response1.body();
                    Log.e(TAG, "Sending New Param Response " + type + "  " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, response.getMsg());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        if (response != null && response.getData() != null && response.getData().size() > 0) {
                            DropDownCacheManager2.getInstance(context).deleteViaType(type);
                            DropDownT2 material = new DropDownT2(type, new Gson().toJson(response));
                            DropDownCacheManager2.getInstance(context).addChemicalUnit(material);

                            try {
                                adapter.setCroppingPatternDDList(response.getData());
                            } catch (Exception e) {
                            }
                        }

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
                        Log.e(TAG, "Cache Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DDResponseNew> call, Throwable t) {
                Log.e("OfflineModeAct", "Failure getOfflineData " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
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

    private void getAllDataNew6(final String type) {
        ParamData paramData = new ParamData();
        paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        paramData.setType(type);

        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        Log.e(TAG, "Sending New Param  AND type " + type + " " + new Gson().toJson(paramData));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getNewParameters(paramData).enqueue(new Callback<DDResponseNew>() {
            @Override
            public void onResponse(Call<DDResponseNew> call, Response<DDResponseNew> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    DDResponseNew response = response1.body();
                    Log.e(TAG, "Sending New Param Response " + type + "  " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, response.getMsg());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        if (response != null && response.getData() != null && response.getData().size() > 0) {
                            DropDownCacheManager2.getInstance(context).deleteViaType(type);
                            DropDownT2 material = new DropDownT2(type, new Gson().toJson(response));
                            DropDownCacheManager2.getInstance(context).addChemicalUnit(material);

                            try {
                                adapter.setPlantingMethodDDList(response.getData());
                            } catch (Exception e) {
                            }
                        }
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
                        Log.e(TAG, "Cache Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DDResponseNew> call, Throwable t) {
                Log.e("OfflineModeAct", "Failure getOfflineData " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
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

    private void getAllDataNew7(final String type) {
        ParamData paramData = new ParamData();
        paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        paramData.setType(type);

        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        Log.e(TAG, "Sending New Param  AND type " + type + " " + new Gson().toJson(paramData));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getNewParameters(paramData).enqueue(new Callback<DDResponseNew>() {
            @Override
            public void onResponse(Call<DDResponseNew> call, Response<DDResponseNew> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    DDResponseNew response = response1.body();
                    Log.e(TAG, "Sending New Param Response " + type + "  " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, response.getMsg());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        if (response != null && response.getData() != null && response.getData().size() > 0) {
                            DropDownCacheManager2.getInstance(context).deleteViaType(type);
                            DropDownT2 material = new DropDownT2(type, new Gson().toJson(response));
                            DropDownCacheManager2.getInstance(context).addChemicalUnit(material);

                            try {
                                adapter.setIsIrrigatedDDList(response.getData());
                            } catch (Exception e) {
                            }
                        }
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
                        Log.e(TAG, "Cache Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DDResponseNew> call, Throwable t) {
                Log.e("OfflineModeAct", "Failure getOfflineData " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
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

    private void getAllDataNew8(final String type) {
        ParamData paramData = new ParamData();
        paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        paramData.setType(type);

        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        Log.e(TAG, "Sending New Param  AND type " + type + " " + new Gson().toJson(paramData));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getNewParameters(paramData).enqueue(new Callback<DDResponseNew>() {
            @Override
            public void onResponse(Call<DDResponseNew> call, Response<DDResponseNew> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    DDResponseNew response = response1.body();
                    Log.e(TAG, "Sending New Param Response " + type + "  " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, response.getMsg());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        if (response != null && response.getData() != null && response.getData().size() > 0) {
                            DropDownCacheManager2.getInstance(context).deleteViaType(type);
                            DropDownT2 material = new DropDownT2(type, new Gson().toJson(response));
                            DropDownCacheManager2.getInstance(context).addChemicalUnit(material);

                        }
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
                        Log.e(TAG, "Cache Error " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DDResponseNew> call, Throwable t) {
                Log.e("OfflineModeAct", "Failure getOfflineData " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
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

    public String compressBitmap(Bitmap selectedBitmap, int sampleSize, int quality) {
        try {
            ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStreamObject);
            byteArrayOutputStreamObject.close();
            long lengthInKb = byteArrayOutputStreamObject.toByteArray().length / 1024; //in kb

            Log.e(TAG, "Image size at " + " " + lengthInKb + "Kb");
            if (lengthInKb > 350) {
                compressBitmap(selectedBitmap, (sampleSize * 2), (quality / 2));
            } else {
                return Base64.encodeToString(byteArrayOutputStreamObject.toByteArray(), Base64.DEFAULT);

            }

            //selectedBitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void uploadAddressIdImage(String personId) {
        if (myBitmapAddress != null) {
            String base64Image = compressBitmap(myBitmapAddress, 2, 50);
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("user_id", "" + userId);
            jsonObject.addProperty("token", "" + token);
            jsonObject.addProperty("person_id", "" + personId);
            jsonObject.addProperty("img_link", "" + base64Image);
            final boolean[] isLoaded = {false};
            long currMillis = AppConstants.getCurrentMills();
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            Log.e(TAG, "Sending New Param i  AND type  " + new Gson().toJson(jsonObject));
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            apiInterface.updateAddressProf(jsonObject).enqueue(new Callback<StatusMsgModel>() {
                @Override
                public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response1) {
                    String error = null;
                    isLoaded[0] = true;
                    if (response1.isSuccessful()) {

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
                            Log.e(TAG, "Cache Error " + error);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response1.code() == 500) {
                        notifyApiDelay(AddFarmActivity.this, response1.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response1.code());
                    }

                    if (latLngListCheckArea != null && latLngListCheckArea.size() > 2) {
                        progressBar_image.setVisibility(View.VISIBLE);
                        AddFarmActivity.SubmitAreaAsync areaAsync = new AddFarmActivity.SubmitAreaAsync();
                        areaAsync.execute();
                    } else {
                        DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.FARMER_LIST);
                        Intent intent = new Intent(context, LandingActivity.class);
                        ActivityOptions options = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                            startActivity(intent, options.toBundle());
                            finish();
                        } else {
                            finishAffinity();
                            startActivity(intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                    if (latLngListCheckArea != null && latLngListCheckArea.size() > 2) {
                        progressBar_image.setVisibility(View.VISIBLE);
                        AddFarmActivity.SubmitAreaAsync areaAsync = new AddFarmActivity.SubmitAreaAsync();
                        areaAsync.execute();
                    } else {
                        DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.FARMER_LIST);
                        Intent intent = new Intent(context, LandingActivity.class);
                        ActivityOptions options = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                            startActivity(intent, options.toBundle());
                            finish();
                        } else {
                            finishAffinity();
                            startActivity(intent);
                            finish();
                        }
                    }
                    Log.e("OfflineModeAct", "Failure getOfflineData " + t.toString());
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    notifyApiDelay(AddFarmActivity.this, call.request().url().toString(),
                            "" + diff, internetSPeed, t.toString(), 0);
                }
            });

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    internetFlow(isLoaded[0]);
                }
            }, AppConstants.DELAY);

        } else {

        }

    }

    private void uploadIDImage(String personId) {
        if (myBitmapId != null) {
            String base64Image = compressBitmap(myBitmapId, 2, 50);
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("user_id", "" + userId);
            jsonObject.addProperty("token", "" + token);
            jsonObject.addProperty("person_id", "" + personId);
            jsonObject.addProperty("img_link", "" + base64Image);
            final boolean[] isLoaded = {false};
            long currMillis = AppConstants.getCurrentMills();
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            Log.e(TAG, "Sending New Param i  AND type  " + new Gson().toJson(jsonObject));
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            apiInterface.updateIdProff(jsonObject).enqueue(new Callback<StatusMsgModel>() {
                @Override
                public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response1) {
                    String error = null;
                    isLoaded[0] = true;
                    if (response1.isSuccessful()) {

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
                            Log.e(TAG, "Cache Error " + error);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response1.code() == 500) {
                        notifyApiDelay(AddFarmActivity.this, response1.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response1.code());
                    }

                    if (myBitmapAddress != null)
                        uploadAddressIdImage(personId);
                    else {
                        if (latLngListCheckArea != null && latLngListCheckArea.size() > 2) {
                            progressBar_image.setVisibility(View.VISIBLE);
                            AddFarmActivity.SubmitAreaAsync areaAsync = new AddFarmActivity.SubmitAreaAsync();
                            areaAsync.execute();
                        } else {
                            DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.FARMER_LIST);
                            Intent intent = new Intent(context, LandingActivity.class);
                            ActivityOptions options = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                finishAffinity();
                                startActivity(intent, options.toBundle());
                                finish();
                            } else {
                                finishAffinity();
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                    if (myBitmapAddress != null)
                        uploadAddressIdImage(personId);
                    else {
                        if (latLngListCheckArea != null && latLngListCheckArea.size() > 2) {
                            progressBar_image.setVisibility(View.VISIBLE);
                            AddFarmActivity.SubmitAreaAsync areaAsync = new AddFarmActivity.SubmitAreaAsync();
                            areaAsync.execute();
                        } else {
                            DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.FARMER_LIST);
                            Intent intent = new Intent(context, LandingActivity.class);
                            ActivityOptions options = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                finishAffinity();
                                startActivity(intent, options.toBundle());
                                finish();
                            } else {
                                finishAffinity();
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                    Log.e("OfflineModeAct", "Failure getOfflineData " + t.toString());
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    notifyApiDelay(AddFarmActivity.this, call.request().url().toString(),
                            "" + diff, internetSPeed, t.toString(), 0);
                }
            });

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    internetFlow(isLoaded[0]);
                }
            }, AppConstants.DELAY);
        } else {
            if (myBitmapAddress != null)
                uploadAddressIdImage(personId);
            else {
                if (latLngListCheckArea != null && latLngListCheckArea.size() > 2) {
                    progressBar_image.setVisibility(View.VISIBLE);
                    AddFarmActivity.SubmitAreaAsync areaAsync = new AddFarmActivity.SubmitAreaAsync();
                    areaAsync.execute();
                } else {
                    DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.FARMER_LIST);
                    Intent intent = new Intent(context, LandingActivity.class);
                    ActivityOptions options = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                        startActivity(intent, options.toBundle());
                        finish();
                    } else {
                        finishAffinity();
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }
    }

    private interface SELECTED_TYPE {
        public final static int NA = 1;
        public final static int DOB = 2;
        public final static int AGE = 3;
        public final static int YOB = 4;
    }

    RadioButton dobRadio, radioAge, radioYob;
    DateTimePickerEditText etDobDialog;
    EditText ageEtDialog, etYobDilog;
    Dialog dialogPersonDob;
    TextView doneTv;
    int selectedPersonDate = SELECTED_TYPE.NA;
    Date dobPersonDate;
    String personDobStr = null;
    TextInputLayout tiDobDialog, tiAgeDialog, tiYobDialog;

    private void initPersonDobDialog() {
        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();
        dialogPersonDob = new Dialog(context);
        dialogPersonDob.setContentView(R.layout.dialog_pick_dob);
        Window window1 = dialogPersonDob.getWindow();
        window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dobRadio = dialogPersonDob.findViewById(R.id.dobRadio);
        radioAge = dialogPersonDob.findViewById(R.id.radioAge);
        radioYob = dialogPersonDob.findViewById(R.id.radioYob);
        etDobDialog = dialogPersonDob.findViewById(R.id.etDobDialog);
        ageEtDialog = dialogPersonDob.findViewById(R.id.ageEtDialog);
        etYobDilog = dialogPersonDob.findViewById(R.id.etYobDilog);
        doneTv = dialogPersonDob.findViewById(R.id.doneTv);

        tiDobDialog = dialogPersonDob.findViewById(R.id.tiDobDialog);
        tiAgeDialog = dialogPersonDob.findViewById(R.id.tiAgeDialog);
        tiYobDialog = dialogPersonDob.findViewById(R.id.tiYobDialog);

        dobRadio.setChecked(true);
        tiYobDialog.setFocusable(false);
        tiYobDialog.setEnabled(false);
        tiAgeDialog.setFocusable(false);
        tiAgeDialog.setEnabled(false);

        etDobDialog.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (dobPersonDate != null && selectedPersonDate == SELECTED_TYPE.DOB) {
//                    etDobDialog.setDate(dobPersonDate);
                } else {
                    Calendar current = Calendar.getInstance();
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, (current.get(Calendar.YEAR) - 30));
                    dobPersonDate = c.getTime();
                    etDobDialog.setDate(dobPersonDate);
                }

                return false;
            }
        });

        dobRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioYob.setChecked(false);
                    radioAge.setChecked(false);
                    ageEtDialog.setText("");
                    etYobDilog.setText("");

                    tiDobDialog.setFocusable(true);
                    tiDobDialog.setEnabled(true);
                    tiYobDialog.setFocusable(false);
                    tiYobDialog.setEnabled(false);
                    tiAgeDialog.setFocusable(false);
                    tiAgeDialog.setEnabled(false);
                }
            }
        });

        radioYob.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dobRadio.setChecked(false);
                    radioAge.setChecked(false);
                    ageEtDialog.setText("");
                    etDobDialog.setText("");

                    tiDobDialog.setFocusable(false);
                    tiDobDialog.setEnabled(false);
                    tiYobDialog.setFocusable(true);
                    tiYobDialog.setEnabled(true);
                    tiAgeDialog.setFocusable(false);
                    tiAgeDialog.setEnabled(false);
                }
            }
        });

        radioAge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dobRadio.setChecked(false);
                    radioYob.setChecked(false);
                    etDobDialog.setText("");
                    etYobDilog.setText("");

                    tiDobDialog.setFocusable(false);
                    tiDobDialog.setEnabled(false);
                    tiYobDialog.setFocusable(false);
                    tiYobDialog.setEnabled(false);
                    tiAgeDialog.setFocusable(true);
                    tiAgeDialog.setEnabled(true);
                }
            }
        });

        doneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dobRadio.isChecked()) {
                    if (TextUtils.isEmpty(etDobDialog.getText().toString())) {
                        AppConstants.failToast(context, "Please enter date of birth");
                    } else {
                        personDobStr = AppConstants.getShowableDate(etDobDialog.getText().toString(), context);
                        selectedPersonDate = SELECTED_TYPE.DOB;
                        dobPersonDate = etDobDialog.getDate();
                        dobEtAge.setText(personDobStr);
                        dialogPersonDob.dismiss();
                    }
                } else if (radioAge.isChecked()) {
                    if (TextUtils.isEmpty(ageEtDialog.getText().toString())) {
                        AppConstants.failToast(context, "Please enter age");
                    } else {
                        personDobStr = ageEtDialog.getText().toString();
                        selectedPersonDate = SELECTED_TYPE.AGE;
                        dobEtAge.setText(personDobStr);
                        dialogPersonDob.dismiss();

                    }
                } else if (radioYob.isChecked()) {
                    if (TextUtils.isEmpty(etYobDilog.getText().toString())) {
                        AppConstants.failToast(context, "Please enter year of birth");
                    } else {
                        personDobStr = etYobDilog.getText().toString();
                        selectedPersonDate = SELECTED_TYPE.YOB;
                        dobEtAge.setText(personDobStr);
                        dialogPersonDob.dismiss();
                    }

                } else {
                    personDobStr = "";
                    selectedPersonDate = SELECTED_TYPE.NA;
                }


            }
        });


    }

    private void showDateDialog() {
        if (dobPersonDate != null && selectedPersonDate == SELECTED_TYPE.DOB)
            etDobDialog.setDate(dobPersonDate);

        dialogPersonDob.show();
    }

    RadioButton dobRadioSpouse, radioAgeSpouse, radioYobSpouse;
    DateTimePickerEditText etDobDialogSpouse;
    EditText ageEtDialogSpouse, etYobDilogSpouse;
    Dialog dialogPersonDobSpouse;
    TextView doneTvSpouse;
    int selectedPersonDateSpouse = SELECTED_TYPE.NA;
    Date dobPersonDateSpouse;
    String personDobStrSpouse = null;
    TextInputLayout tiDobDialogSpouse, tiAgeDialogSpouse, tiYobDialogSpouse;

    private void initSpouseDobDialog() {
        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();
        dialogPersonDobSpouse = new Dialog(context);
        dialogPersonDobSpouse.setContentView(R.layout.dialog_pick_dob);
        Window window1 = dialogPersonDobSpouse.getWindow();
        window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dobRadioSpouse = dialogPersonDobSpouse.findViewById(R.id.dobRadio);
        radioAgeSpouse = dialogPersonDobSpouse.findViewById(R.id.radioAge);
        radioYobSpouse = dialogPersonDobSpouse.findViewById(R.id.radioYob);
        etDobDialogSpouse = dialogPersonDobSpouse.findViewById(R.id.etDobDialog);
        ageEtDialogSpouse = dialogPersonDobSpouse.findViewById(R.id.ageEtDialog);
        etYobDilogSpouse = dialogPersonDobSpouse.findViewById(R.id.etYobDilog);
        doneTvSpouse = dialogPersonDobSpouse.findViewById(R.id.doneTv);

        tiDobDialogSpouse = dialogPersonDobSpouse.findViewById(R.id.tiDobDialog);
        tiAgeDialogSpouse = dialogPersonDobSpouse.findViewById(R.id.tiAgeDialog);
        tiYobDialogSpouse = dialogPersonDobSpouse.findViewById(R.id.tiYobDialog);

        dobRadioSpouse.setChecked(true);
        tiYobDialogSpouse.setFocusable(false);
        tiYobDialogSpouse.setEnabled(false);
        tiAgeDialogSpouse.setFocusable(false);
        tiAgeDialogSpouse.setEnabled(false);
        dobRadioSpouse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioYobSpouse.setChecked(false);
                    radioAgeSpouse.setChecked(false);
                    ageEtDialogSpouse.setText("");
                    etYobDilogSpouse.setText("");

                    tiDobDialogSpouse.setFocusable(true);
                    tiDobDialogSpouse.setEnabled(true);
                    tiYobDialogSpouse.setFocusable(false);
                    tiYobDialogSpouse.setEnabled(false);
                    tiAgeDialogSpouse.setFocusable(false);
                    tiAgeDialogSpouse.setEnabled(false);
                }
            }
        });

        radioYobSpouse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dobRadioSpouse.setChecked(false);
                    radioAgeSpouse.setChecked(false);
                    ageEtDialogSpouse.setText("");
                    etDobDialogSpouse.setText("");

                    tiDobDialogSpouse.setFocusable(false);
                    tiDobDialogSpouse.setEnabled(false);
                    tiYobDialogSpouse.setFocusable(true);
                    tiYobDialogSpouse.setEnabled(true);
                    tiAgeDialogSpouse.setFocusable(false);
                    tiAgeDialogSpouse.setEnabled(false);
                }
            }
        });

        radioAgeSpouse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dobRadioSpouse.setChecked(false);
                    radioYobSpouse.setChecked(false);
                    etDobDialogSpouse.setText("");
                    etYobDilogSpouse.setText("");

                    tiDobDialogSpouse.setFocusable(false);
                    tiDobDialogSpouse.setEnabled(false);
                    tiYobDialogSpouse.setFocusable(false);
                    tiYobDialogSpouse.setEnabled(false);
                    tiAgeDialogSpouse.setFocusable(true);
                    tiAgeDialogSpouse.setEnabled(true);
                }
            }
        });

        doneTvSpouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dobRadioSpouse.isChecked()) {
                    if (TextUtils.isEmpty(etDobDialogSpouse.getText().toString())) {
                        AppConstants.failToast(context, "Please enter date of birth");
                    } else {
                        personDobStrSpouse = AppConstants.getShowableDate(etDobDialogSpouse.getText().toString(), context);
                        selectedPersonDateSpouse = AddFarmActivity.SELECTED_TYPE.DOB;
                        dobPersonDateSpouse = etDobDialogSpouse.getDate();
                        etSpouseAge.setText(personDobStrSpouse);
                        dialogPersonDobSpouse.dismiss();
                    }
                } else if (radioAgeSpouse.isChecked()) {
                    if (TextUtils.isEmpty(ageEtDialogSpouse.getText().toString())) {
                        AppConstants.failToast(context, "Please enter age");
                    } else {
                        personDobStrSpouse = ageEtDialogSpouse.getText().toString();
                        selectedPersonDateSpouse = AddFarmActivity.SELECTED_TYPE.AGE;
                        etSpouseAge.setText(personDobStrSpouse);
                        dialogPersonDobSpouse.dismiss();
                    }
                } else if (radioYobSpouse.isChecked()) {
                    if (TextUtils.isEmpty(etYobDilogSpouse.getText().toString())) {
                        AppConstants.failToast(context, "Please enter year of birth");
                    } else {
                        personDobStrSpouse = etYobDilogSpouse.getText().toString();
                        selectedPersonDateSpouse = AddFarmActivity.SELECTED_TYPE.YOB;
                        etSpouseAge.setText(personDobStrSpouse);
                        dialogPersonDobSpouse.dismiss();
                    }
                } else {
                    personDobStrSpouse = "";
                    selectedPersonDateSpouse = AddFarmActivity.SELECTED_TYPE.NA;
                }
                if (selectedPersonDateSpouse == AddFarmActivity.SELECTED_TYPE.DOB && AppConstants.isValidString(personDobStrSpouse)) {
                    Log.e(TAG, "DOBYOB 1, " + AppConstants.getUploadableDate(personDobStrSpouse, context));

                } else if (selectedPersonDateSpouse == AddFarmActivity.SELECTED_TYPE.AGE && AppConstants.isValidString(personDobStrSpouse)) {
                    try {
                        Log.e(TAG, "DOBYOB 2, " + getYOBByAge(Integer.valueOf(personDobStrSpouse.trim())));

                    } catch (Exception e) {

                    }
                } else if (selectedPersonDateSpouse == AddFarmActivity.SELECTED_TYPE.YOB && AppConstants.isValidString(personDobStrSpouse)) {
                    Log.e(TAG, "DOBYOB 3, " + personDobStrSpouse.trim());
                }

            }
        });


    }

    private void showDateDialogSpouse() {

        dialogPersonDobSpouse.show();
    }

    private String getYOBByAge(int age) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(Calendar.YEAR, (today.get(Calendar.YEAR) - age));

        int yr = today.get(Calendar.YEAR) - age;


        String ageS = yr + "";

        return ageS;
    }
}
