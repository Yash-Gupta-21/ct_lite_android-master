package com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate;

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
import android.widget.Button;
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
import com.bumptech.glide.Glide;
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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hbb20.CountryCodePicker;

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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

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

import com.i9930.croptrails.AddFarm.AddFarmActivity;
import com.i9930.croptrails.AddFarm.AutoCompleteAdapter;
import com.i9930.croptrails.AddFarm.Model.CowAndCatles;
import com.i9930.croptrails.AddFarm.Model.FPOCrop;
import com.i9930.croptrails.AddFarm.Model.FarmData;
import com.i9930.croptrails.AddFarm.Model.FarmerAndFarmData;
import com.i9930.croptrails.AddFarm.Model.FarmerSpinnerData;
import com.i9930.croptrails.AddFarm.Model.FormModel;
import com.i9930.croptrails.AddFarm.Model.IfscResponse;
import com.i9930.croptrails.AddFarm.Model.SupervisorDatum;
import com.i9930.croptrails.AddFarm.Model.SupervisorListResponse;
import com.i9930.croptrails.AddFarm.Share;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterCity;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterCluster;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterCountry;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterFarmer;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterNewDD;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterNewDDValue;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SvSpinnerAdapter;
import com.i9930.croptrails.AddFarm.SpinnerAdapterState;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.Address.CityDatum;
import com.i9930.croptrails.CommonClasses.Address.CityResponse;
import com.i9930.croptrails.CommonClasses.Address.CountryDatum;
import com.i9930.croptrails.CommonClasses.Address.CountryResponse;
import com.i9930.croptrails.CommonClasses.Address.StateDatum;
import com.i9930.croptrails.CommonClasses.Cluster;
import com.i9930.croptrails.CommonClasses.DDNew;
import com.i9930.croptrails.CommonClasses.DDResponseNew;
import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormDatum;
import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormResponse;
import com.i9930.croptrails.CommonClasses.ParamData;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.DatePick.DateTimePickerEditText;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.PersonFullData;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.PersonFullResponse;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.Season;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.SpinnerResponse;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.UpdatRsponse;
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
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmLoadListener;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerCacheManager;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerT;
import com.i9930.croptrails.Test.model.full.AssetsData;
import com.i9930.croptrails.Test.model.full.BankDetails;
import com.i9930.croptrails.Test.model.full.DataCropDetails;
import com.i9930.croptrails.Test.model.full.FarmAddressDetails;
import com.i9930.croptrails.Test.model.full.FarmCropDetails;
import com.i9930.croptrails.Test.model.full.FarmFullDetails;
import com.i9930.croptrails.Test.model.full.FarmsDetails;
import com.i9930.croptrails.Test.model.full.PersonAddress;
import com.i9930.croptrails.Test.model.full.PersonDetails;
import com.i9930.croptrails.Test.model.full.PrevCropDatum;
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

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;
import static com.i9930.croptrails.Utility.AppConstants.for_date;

public class FarmDetailsUpdateActivity2 extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnFarmDatasetChanged {
    FarmDetailsUpdateActivity2 context;
    String TAG = "FarmDetailsUpdateActivity2";
    @BindView(R.id.farmDetailsLAyout)
    LinearLayout farmDetailsLAyout;
    private List<FarmData> farmList = new ArrayList<>();
    @BindView(R.id.addFarmTv)
    TextView addFarmTv;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    UpdateFarmAdapter adapter;
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
    EditText dobEt;

    @BindView(R.id.etSpouseAge)
    EditText etSpouseAge;
    @BindView(R.id.etSpouseDob)
    EditText etSpouseDob;

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
    @BindView(R.id.new_farmer_layout)
    LinearLayout new_farmer_layout;
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

    @BindView(R.id.sv_profile_photo)
    CircleImageView sv_profile_photo;

    @BindView(R.id.idCircularImg)
    CircleImageView idCircularImg;

    @BindView(R.id.addressIdCircularImage)
    CircleImageView addressIdCircularImage;
    String userChoosenTask, pictureImagePath = "", pictureImagePathId = "", pictureImagePathAddress = "";
    Bitmap myBitmap, myBitmapId, myBitmapAddress;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;


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
    @BindView(R.id.tiSpouseAge)
    TextInputLayout tiSpouseAge;
    @BindView(R.id.etUpi)
    EditText etUpi;

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
    @BindView(R.id.literateRadio)
    RadioButton literateRadio;

    @BindView(R.id.illiterateRadio)
    RadioButton illiterateRadio;
    @BindView(R.id.aplRadio)
    RadioButton aplRadio;
    @BindView(R.id.bplRadio)
    RadioButton bplRadio;

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

    boolean isEditDisabled = false;

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
        setContentView(R.layout.activity_farm_details_update2);
        context = this;
        farmList.add(new FarmData());
        ButterKnife.bind(this);
        isPreviousCropMandatory = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_PREVIOUS_CROP_MANDATORY);
        mSimpleArrayListAdapter = new SpinnerAdapterState(this, stateDatumList);
        hideShowAndClick();
        initializeCountry();
        TextView title = (TextView) findViewById(R.id.tittle);

        viewFailDialog = new ViewFailDialog();
        checkCompReg();
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        isOfflineMode = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE);
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
                                if (data.getData() != null) {
                                    FarmDetailsUpdateActivity2.this.cropFormDatumList.addAll(data.getData());
                                    for (CropFormDatum cropFormDatum : data.getData()) {
                                        FarmDetailsUpdateActivity2.this.cropFormDatumListMap.put(cropFormDatum.getInfoTypeId(), cropFormDatum);
                                    }
                                }

                                if (data.getSuperData() != null) {
                                    FarmDetailsUpdateActivity2.this.cropFormDatumListSuper.addAll(data.getSuperData());
                                    for (CropFormDatum cropFormDatum : data.getSuperData()) {
                                        if (cropFormDatum.getType().trim().equals("Equipment / Machinery"))
                                            FarmDetailsUpdateActivity2.this.cropFormDatumListSuperMap.put("E", cropFormDatum);
                                        else
                                            FarmDetailsUpdateActivity2.this.cropFormDatumListSuperMap.put("A", cropFormDatum);
                                    }
                                }

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
//                    motorHPSpinner.setVisibility(View.GONE);
                    microIrriAwarenessSpinner.setVisibility(View.GONE);

                    if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                        adapter = new UpdateFarmAdapter(context, farmList, irrigationTypeDDList, irrigationSourceDDList, soilTypeDDList,
                                seasonDDList, farmCropList, plantingMethodDDList, isIrrigatedDDList, croppingPatternDDList,
                                cropFormDatumList, cropFormDatumList, new ArrayList<>(), FarmDetailsUpdateActivity2.this);

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

                        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                            farmFullDetails = DataHandler.newInstance().getFarmFullDetails();
                            if (farmFullDetails != null)
                                setPreviousData(farmFullDetails);
                            else
                                getFullDetails();
                        } else {
                            Log.e(TAG, "Offline farmId " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                            FarmCacheManager.getInstance(context).getFarm(new FarmLoadListener() {
                                @Override
                                public void onFarmLoader(Farm farm) {
                                    Log.d(TAG, "Offline farm " + new Gson().toJson(farm));
//                                    Toast.makeText(FarmDetailsUpdateActivity2.this, " "+new Gson().toJson(farm), Toast.LENGTH_SHORT).show();
                                    setPreviousDataOffline(farm);
                                }
                            }, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                        }
                    }

                    getSpinnerData();
                    Log.e(TAG, "Spinner Data Online cache else");
                }
            }
        }, where);

        isEditDisabled = getIntent().getBooleanExtra("isEditDisabled", false);
        String head = getIntent().getStringExtra("title");
        if (!AppConstants.isValidString(head))
            title.setText(resources.getString(R.string.profile_btn_update));
        else
            title.setText(resources.getString(R.string.farm_detail_label));
        /*if (isEditDisabled) {
            disableAllFields(!isEditDisabled);
            tv_add_new_farm_submit_bt.setEnabled(false);
            tv_add_new_farm_submit_bt.setClickable(false);
            tv_add_new_farm_submit_bt.setVisibility(View.GONE);
        }*/

        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.FARMER)) {
            isFarmer = true;
            FarmData data = new FarmData();
            data.setIsOwned("Y");
            farmList.add(data);
            addFarmTv.setText(resources.getString(R.string.add_more_farm_label));
            farmDetailsLAyout.setVisibility(View.VISIBLE);
            addFarmTv.setVisibility(View.GONE);
            new_farmer_layout.setVisibility(View.GONE);
            imgRelLay.setVisibility(View.GONE);
            svAdapter = new SvSpinnerAdapter(context, supervisorDatumList);

        } else {

            areaInAcre = getIntent().getStringExtra("areaInAcre");
            mapZoom = getIntent().getStringExtra("mapZoom");
            latLngListCheckArea = DataHandler.newInstance().getLatLngsCheckArea();
            isFarmer = false;
            if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.ADMIN) ||
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUB_ADMIN) ||
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPER_ADMIN)) {
                isAdmin = true;

            } else {

                isAdmin = false;
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


            Log.e(TAG, "Preferences " + preference + " l " + l + " co " + "loc" + ", code " + getCountryCode(context) + " DATA " + new Gson().toJson(current));
            countryCodePicker.setCountryPreference(preference.toUpperCase());
            try {
                countryCodePicker.setDefaultCountryUsingNameCode(preference);
            } catch (Exception e) {

            }
        }

        /*if (countryCodePicker.getSelectedCountryCode().trim().equals("91")) {
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
*/
        phoneNumberEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isMotEtTouch = true;
                return false;
            }
        });

        phoneNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isMotEtTouch) {
                    if (countryCodePicker.getSelectedCountryCode().trim().equals("91")) {
                        if (s.length() == 10)
                            getFullDetailsByMobile(s.toString());
                    } else {
                        getFullDetailsByMobile(s.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phoneNumberEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    isMotEtTouch = false;
                }
            }
        });

        AddFarmCCacheManager.getInstance(context).getCompRegData(new FetchFarmCacheListener() {
            @Override
            public void onGetAllCompRegData(List<AddFarmCache> addFarmCacheList) {
                /*if (addFarmCacheList == null || addFarmCacheList.size() == 0) {
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
                }*/
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

//        FarmFullDetails fullDetails = new Gson().fromJson("{\"farmsDetails\":{\"id\":11223,\"farm_master_num\":6539,\"farm_name\":\"\",\"comp_farm_id\":\"TestFarm001\",\"owner_id\":12111,\"operator_id\":0,\"cluster_id\":122,\"comp_id\":23,\"season_num\":286,\"address_id\":28295,\"lat_cord\":\"0\",\"long_cord\":\"0\",\"is_free\":0,\"is_owned\":\"Y\",\"zoom\":0,\"is_active\":1,\"doa\":\"2021-01-04 07:27:02\",\"added_by\":3934,\"dod\":null,\"isSelected\":\"0\",\"deleted_by\":null,\"delq_reason\":null,\"sv_id\":3934,\"motor_hp\":null,\"ownership_doc\":null,\"farm_photo\":null},\"farmCropDetails\":{\"farm_cm_id\":11899,\"farm_master_num\":6539,\"comp_id\":23,\"cluster_id\":122,\"crop_name\":\"Alfalfa\",\"season_num\":286,\"crop_id\":649,\"exp_area\":55,\"actual_area\":0,\"cropping_pattern\":\"MO\",\"standing_area\":0,\"is_irrigated\":\"I\",\"irrigation_source_id\":0,\"irrigation_type_id\":0,\"previous_crop\":null,\"soil_type_id\":0,\"planting_method\":\"S\",\"exp_sowing_date\":null,\"exp_transplant_date\":null,\"exp_flowering_date\":null,\"exp_harvest_date\":null,\"sowing_date\":null,\"transplant_date\":null,\"actual_flowering_date\":null,\"actual_harvest_date\":null,\"area_harvested\":null,\"seed_provided_on\":null,\"seed_lot_no\":null,\"qty_seed_provided\":null,\"seed_unit_id\":null,\"germination\":null,\"population\":null,\"spacing_rtr\":null,\"spacing_ptp\":null,\"pld_acres\":null,\"pld_reason\":null,\"pld_receipt_no\":null,\"received_qty\":null,\"outward_no\":null,\"load_no\":null,\"grade\":\"D\",\"agreed_rate\":\"0\",\"payment_mode\":null,\"doa\":\"2021-01-04 13:56:59\",\"is_active\":1,\"dod\":null,\"deleted_by\":null},\"personDetails\":{\"person_id\":12111,\"comp_id\":23,\"cluster_id\":122,\"user_id\":11660,\"role_id\":3,\"name\":\"TestFrmer\",\"father_name\":\"\",\"designation\":null,\"country_code\":\"+91\",\"mob\":\"95088709848\",\"mob2\":null,\"email\":null,\"signature\":null,\"dob\":\"1900-01-01 00:00:00\",\"gender\":\"M\",\"is_using_sphone\":\"Y\",\"is_shareholder\":\"Y\",\"share_json\":null,\"cattle_json\":null,\"est_income\":null,\"img_link\":null,\"address_id\":28294,\"pan\":\"\",\"aadhaar\":null,\"doa\":\"2021-01-04 07:27:02\",\"is_active\":\"Y\",\"added_by\":3934,\"is_deleted\":\"N\",\"deleted_by\":null,\"deactivated_by\":null,\"dod\":null,\"spouse_name\":null,\"spouse_dob\":null,\"beneficiary_name\":null,\"civil_status\":null,\"caste_category\":null,\"caste\":null,\"religion\":null,\"family_mem_count\":0,\"adults_count\":0,\"kids_count\":0,\"dependent_count\":0,\"mobile_operator\":null,\"mia\":null,\"id_proof_link\":null,\"address_proof_link\":null,\"literacy_status\":null,\"financial_status\":null,\"lead_chak\":null},\"bankDetails\":{\"bank_detail_id\":3545,\"person_id\":12111,\"bank_name\":\"\",\"account_no\":\"\",\"account_name\":\"\",\"branch\":\"\",\"ifsc\":\"\",\"upi_id\":\"\",\"swift_code\":\"\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:56:58\"},\"farmAddressDetails\":{\"address_id\":28295,\"addL1\":\"0\",\"addL2\":\"0\",\"village_or_city\":\"0\",\"district\":\"0\",\"mandal_or_tehsil\":\"0\",\"state\":\"0\",\"country\":\"0\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:56:59\"},\"personAddress\":{\"address_id\":28294,\"addL1\":\"0\",\"addL2\":\"0\",\"village_or_city\":\"0\",\"district\":\"0\",\"mandal_or_tehsil\":\"0\",\"state\":\"0\",\"country\":\"0\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 07:27:02\"},\"dataCropDetails\":[{\"farm_add_info_id\":855,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Price\\\": \\\"Hrih\\\", \\\"Arrows\\\": \\\"Hru\\\", \\\"Company\\\": \\\"Hrub\\\", \\\"Quantity\\\": \\\"Hrjb\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:56:59\",\"source\":\"M\",\"farm_add_info_type_id\":1,\"farm_cmp_id\":14063},{\"farm_add_info_id\":856,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Type\\\": \\\"Yrib\\\", \\\"Price\\\": \\\"Hdjb\\\", \\\"Company\\\": \\\"Hruh\\\", \\\"Quantity\\\": \\\"Hrb\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:56:59\",\"source\":\"M\",\"farm_add_info_type_id\":2,\"farm_cmp_id\":14063},{\"farm_add_info_id\":857,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Type\\\": \\\"Hhhhuh\\\", \\\"Price\\\": \\\"Hhhhbcccih7\\\", \\\"Company\\\": \\\"H4yuh\\\", \\\"Quantity\\\": \\\"Hrhrub\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:56:59\",\"source\":\"M\",\"farm_add_info_type_id\":3,\"farm_cmp_id\":14063},{\"farm_add_info_id\":858,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Price\\\": \\\"Vvhhhih\\\", \\\"Company\\\": \\\"Ccccub\\\", \\\"Quantity\\\": \\\"Chjjbib\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:56:59\",\"source\":\"M\",\"farm_add_info_type_id\":4,\"farm_cmp_id\":14063},{\"farm_add_info_id\":859,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Company\\\": \\\"Bbvccuh\\\", \\\"How many hours\\\": \\\"Cvvbi\\\", \\\"Expenses incurred\\\": \\\"Vgvvih\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":5,\"farm_cmp_id\":14063},{\"farm_add_info_id\":860,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Company\\\": \\\"Vbjjuh\\\", \\\"How many hours\\\": \\\"Hhhi\\\", \\\"Expenses incurred\\\": \\\"Hjjgggg\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":6,\"farm_cmp_id\":14063},{\"farm_add_info_id\":861,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Name\\\": \\\"Ghhhug\\\", \\\"Measures\\\": \\\"Hhhbhbjbh\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":7,\"farm_cmp_id\":14063},{\"farm_add_info_id\":862,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Buyer\\\": \\\"Hhub\\\", \\\"Price\\\": \\\"Vjrurguv\\\", \\\"Dimensions \\/ unit\\\": \\\"Hhccchv\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":8,\"farm_cmp_id\":14063},{\"farm_add_info_id\":863,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Price\\\": \\\"Brbbih\\\", \\\"Quantity\\\": \\\"Vrrjkug\\\", \\\"Identifier\\\": \\\"Gruuhc\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":11,\"farm_cmp_id\":14063},{\"farm_add_info_id\":864,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Price\\\": \\\"Hrih\\\", \\\"Arrows\\\": \\\"Hru\\\", \\\"Company\\\": \\\"Hrub\\\", \\\"Quantity\\\": \\\"Hrjb\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":1,\"farm_cmp_id\":14064},{\"farm_add_info_id\":865,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Type\\\": \\\"Yrib\\\", \\\"Price\\\": \\\"Hdjb\\\", \\\"Company\\\": \\\"Hruh\\\", \\\"Quantity\\\": \\\"Hrb\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":2,\"farm_cmp_id\":14064},{\"farm_add_info_id\":866,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Type\\\": \\\"Hhhhuh\\\", \\\"Price\\\": \\\"Hhhhbcccih7\\\", \\\"Company\\\": \\\"H4yuh\\\", \\\"Quantity\\\": \\\"Hrhrub\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":3,\"farm_cmp_id\":14064},{\"farm_add_info_id\":867,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Price\\\": \\\"Vvhhhih\\\", \\\"Company\\\": \\\"Ccccub\\\", \\\"Quantity\\\": \\\"Chjjbib\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":4,\"farm_cmp_id\":14064},{\"farm_add_info_id\":868,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Company\\\": \\\"Bbvccuh\\\", \\\"How many hours\\\": \\\"Cvvbi\\\", \\\"Expenses incurred\\\": \\\"Vgvvih\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":5,\"farm_cmp_id\":14064},{\"farm_add_info_id\":869,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Company\\\": \\\"Vbjjuh\\\", \\\"How many hours\\\": \\\"Hhhi\\\", \\\"Expenses incurred\\\": \\\"Hjjgggg\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":6,\"farm_cmp_id\":14064},{\"farm_add_info_id\":870,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Name\\\": \\\"Ghhhug\\\", \\\"Measures\\\": \\\"Hhhbhbjbh\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":7,\"farm_cmp_id\":14064},{\"farm_add_info_id\":871,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Buyer\\\": \\\"Hhub\\\", \\\"Price\\\": \\\"Vjrurguv\\\", \\\"Dimensions \\/ unit\\\": \\\"Hhccchv\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":8,\"farm_cmp_id\":14064},{\"farm_add_info_id\":872,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Price\\\": \\\"Brbbih\\\", \\\"Quantity\\\": \\\"Vrrjkug\\\", \\\"Identifier\\\": \\\"Gruuhc\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:01\",\"source\":\"M\",\"farm_add_info_type_id\":11,\"farm_cmp_id\":14064}],\"assetDetails\":[{\"id\":292,\"farm_master_num\":6539,\"asset_type\":\"E\",\"json\":\"{\\\"Animal Husbandry\\\":{\\\"Name\\\":\\\"Bfh\\\",\\\"Quantity\\\":\\\"Ehe\\\",\\\"Breed\\\":\\\"Hrh\\\",\\\"Price\\\":\\\"Eh\\\",\\\"Housing\\\":\\\"Hrh\\\",\\\"Fodder\\\":\\\"Hrhh4h\\\"},\\\"Equipment \\/ Machinery\\\":{\\\"Name\\\":\\\"Hr\\\",\\\"Quantity\\\":\\\"He\\\",\\\"Type\\\":\\\"Hr\\\",\\\"Price\\\":\\\"Yd\\\",\\\"Operational Cost\\\":\\\"Y4\\\",\\\"Ownership\\\":\\\"He\\\"}}\"}],\"addInfo\":[{\"farm_cmp_id\":14063,\"comp_id\":23,\"cluster_id\":122,\"farm_master_num\":6539,\"season_num\":369,\"crop_id\":95,\"farm_area\":5,\"doa\":\"2021-01-04 13:56:59\",\"is_active\":\"Y\",\"dod\":null,\"deleted_by\":null,\"data\":[{\"farm_add_info_id\":855,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Price\\\": \\\"Hrih\\\", \\\"Arrows\\\": \\\"Hru\\\", \\\"Company\\\": \\\"Hrub\\\", \\\"Quantity\\\": \\\"Hrjb\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:56:59\",\"source\":\"M\",\"farm_add_info_type_id\":1,\"farm_cmp_id\":14063},{\"farm_add_info_id\":856,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Type\\\": \\\"Yrib\\\", \\\"Price\\\": \\\"Hdjb\\\", \\\"Company\\\": \\\"Hruh\\\", \\\"Quantity\\\": \\\"Hrb\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:56:59\",\"source\":\"M\",\"farm_add_info_type_id\":2,\"farm_cmp_id\":14063},{\"farm_add_info_id\":857,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Type\\\": \\\"Hhhhuh\\\", \\\"Price\\\": \\\"Hhhhbcccih7\\\", \\\"Company\\\": \\\"H4yuh\\\", \\\"Quantity\\\": \\\"Hrhrub\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:56:59\",\"source\":\"M\",\"farm_add_info_type_id\":3,\"farm_cmp_id\":14063},{\"farm_add_info_id\":858,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Price\\\": \\\"Vvhhhih\\\", \\\"Company\\\": \\\"Ccccub\\\", \\\"Quantity\\\": \\\"Chjjbib\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:56:59\",\"source\":\"M\",\"farm_add_info_type_id\":4,\"farm_cmp_id\":14063},{\"farm_add_info_id\":859,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Company\\\": \\\"Bbvccuh\\\", \\\"How many hours\\\": \\\"Cvvbi\\\", \\\"Expenses incurred\\\": \\\"Vgvvih\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":5,\"farm_cmp_id\":14063},{\"farm_add_info_id\":860,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Company\\\": \\\"Vbjjuh\\\", \\\"How many hours\\\": \\\"Hhhi\\\", \\\"Expenses incurred\\\": \\\"Hjjgggg\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":6,\"farm_cmp_id\":14063},{\"farm_add_info_id\":861,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Name\\\": \\\"Ghhhug\\\", \\\"Measures\\\": \\\"Hhhbhbjbh\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":7,\"farm_cmp_id\":14063},{\"farm_add_info_id\":862,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Buyer\\\": \\\"Hhub\\\", \\\"Price\\\": \\\"Vjrurguv\\\", \\\"Dimensions \\/ unit\\\": \\\"Hhccchv\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":8,\"farm_cmp_id\":14063},{\"farm_add_info_id\":863,\"farm_id\":6539,\"season_id\":369,\"crop_id\":95,\"farm_area\":\"5\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Price\\\": \\\"Brbbih\\\", \\\"Quantity\\\": \\\"Vrrjkug\\\", \\\"Identifier\\\": \\\"Gruuhc\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":11,\"farm_cmp_id\":14063}]},{\"farm_cmp_id\":14064,\"comp_id\":23,\"cluster_id\":122,\"farm_master_num\":6539,\"season_num\":523,\"crop_id\":0,\"farm_area\":4771,\"doa\":\"2021-01-04 13:57:00\",\"is_active\":\"Y\",\"dod\":null,\"deleted_by\":null,\"data\":[{\"farm_add_info_id\":864,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Price\\\": \\\"Hrih\\\", \\\"Arrows\\\": \\\"Hru\\\", \\\"Company\\\": \\\"Hrub\\\", \\\"Quantity\\\": \\\"Hrjb\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":1,\"farm_cmp_id\":14064},{\"farm_add_info_id\":865,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Type\\\": \\\"Yrib\\\", \\\"Price\\\": \\\"Hdjb\\\", \\\"Company\\\": \\\"Hruh\\\", \\\"Quantity\\\": \\\"Hrb\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":2,\"farm_cmp_id\":14064},{\"farm_add_info_id\":866,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Type\\\": \\\"Hhhhuh\\\", \\\"Price\\\": \\\"Hhhhbcccih7\\\", \\\"Company\\\": \\\"H4yuh\\\", \\\"Quantity\\\": \\\"Hrhrub\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":3,\"farm_cmp_id\":14064},{\"farm_add_info_id\":867,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Price\\\": \\\"Vvhhhih\\\", \\\"Company\\\": \\\"Ccccub\\\", \\\"Quantity\\\": \\\"Chjjbib\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":4,\"farm_cmp_id\":14064},{\"farm_add_info_id\":868,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Company\\\": \\\"Bbvccuh\\\", \\\"How many hours\\\": \\\"Cvvbi\\\", \\\"Expenses incurred\\\": \\\"Vgvvih\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":5,\"farm_cmp_id\":14064},{\"farm_add_info_id\":869,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Company\\\": \\\"Vbjjuh\\\", \\\"How many hours\\\": \\\"Hhhi\\\", \\\"Expenses incurred\\\": \\\"Hjjgggg\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":6,\"farm_cmp_id\":14064},{\"farm_add_info_id\":870,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Name\\\": \\\"Ghhhug\\\", \\\"Measures\\\": \\\"Hhhbhbjbh\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":7,\"farm_cmp_id\":14064},{\"farm_add_info_id\":871,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Buyer\\\": \\\"Hhub\\\", \\\"Price\\\": \\\"Vjrurguv\\\", \\\"Dimensions \\/ unit\\\": \\\"Hhccchv\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:00\",\"source\":\"M\",\"farm_add_info_type_id\":8,\"farm_cmp_id\":14064},{\"farm_add_info_id\":872,\"farm_id\":6539,\"season_id\":523,\"crop_id\":0,\"farm_area\":\"4771\",\"datatype\":\"A\",\"farm_add_info_json\":\"{\\\"Price\\\": \\\"Brbbih\\\", \\\"Quantity\\\": \\\"Vrrjkug\\\", \\\"Identifier\\\": \\\"Gruuhc\\\"}\",\"is_active\":\"Y\",\"doa\":\"2021-01-04 13:57:01\",\"source\":\"M\",\"farm_add_info_type_id\":11,\"farm_cmp_id\":14064}]}]}", FarmFullDetails.class);
//        setPreviousData(fullDetails);
    }

    FarmFullDetails farmFullDetails;

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
    private Map<String, CropFormDatum> cropFormDatumListMap = new HashMap<>();
    private List<CropFormDatum> cropFormDatumListSuper = new ArrayList<>();
    private Map<String, CropFormDatum> cropFormDatumListSuperMap = new HashMap<>();

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
                        if (data.getData() != null)
                            motorHpList.addAll(data.getData());

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

                    Log.e(TAG, "SELECTED CIVIL " + new Gson().toJson(civilStatusList.get(i)));

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

        /*if (motorHpList != null && motorHpList.size() > 0) {
            motorHPSpinner.setVisibility(View.VISIBLE);
            this.motorHpList.addAll(motorHpList);
            motorHpSpinnerAdapter = new SpinnerAdapterNewDD(this, this.motorHpList, resources.getString(R.string.select_motor_hp));
            motorHPSpinner.setAdapter(motorHpSpinnerAdapter);
        } else {
            motorHPSpinner.setVisibility(View.GONE);
        }
*/
        if (mobileOperatorList != null && mobileOperatorList.size() > 0) {
            networkSpinner.setVisibility(View.VISIBLE);
            this.mobileOperatorList.addAll(mobileOperatorList);
            networkAdapter = new SpinnerAdapterNewDD(this, this.mobileOperatorList, resources.getString(R.string.select_network));
            networkSpinner.setAdapter(networkAdapter);
        } else {
            networkSpinner.setVisibility(View.GONE);
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


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tour_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

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
                   /* sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(this)
                                    .setSkipText(getResources().getString(R.string.tour_skip))
                                    .setTarget(findViewById(R.id.existing_farmer_layout))
                                    .setTitleText(getResources().getString(R.string.addfarm_tour_farmer_sel_title))
                                    .setDismissText(getResources().getString(R.string.got_it))
                                    .setContentText(getResources().getString(R.string.addfarm_tour_farmer_sel_content))
                                    .withRectangleShape()
                                    .build());*/

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
                   /* sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(this)
                                    .setSkipText(getResources().getString(R.string.tour_skip))
                                    .setTarget(findViewById(R.id.cluster_spinner))
                                    .setTitleText(getResources().getString(R.string.addfarm_tour_clust_sel_title))
                                    .setDismissText(getResources().getString(R.string.got_it))
                                    .setContentText(getResources().getString(R.string.addfarm_tour_clust_sel_content))
                                    .withRectangleShape()
                                    .build());*/
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


    private void selectImage() {
        final CharSequence[] items = {resources.getString(R.string.image_take_photo), resources.getString(R.string.image_choose_from_library),
                resources.getString(R.string.image_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(resources.getString(R.string.image_select_image_title));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
//                boolean resultCamera = Utility.checkPermission(context);
//                boolean resultStorage = checkStoragePermission(context);
                if (items[item].equals(resources.getString(R.string.image_take_photo))) {
                    userChoosenTask = "Take Photo";
                    if (checkCameraPermission(context))
                        cameraIntent();
                    /*else {
                        Log.e(TAG, "Camera permission Not ");
                        checkCameraPermission(context);
                    }*/
                } else if (items[item].equals(resources.getString(R.string.image_choose_from_library))) {
                    userChoosenTask = "Choose from Library";
                    if (checkStoragePermission(context))
                        galleryIntent();
                   /* else {
                        Log.e(TAG, "Gallery permission Not ");
                        checkStoragePermission(context);
                    }*/
                } else if (items[item].equals(resources.getString(R.string.image_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

    private int mYear, mMonth, mDay, mHour, mMinute;

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
       /* dobEtAge.addTextChangedListener(new TextWatcher() {
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
        dobEt.setOnClickListener(new View.OnClickListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, add_details_farm_flowering, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }
        });


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
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, add_details_farm_flowering, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }
        });

        tv_add_new_farm_submit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(farmerNameEt.getText().toString())) {
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
                } else if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE) && !TextUtils.isEmpty(ifscCodeEt.getText().toString().trim()) && !isValidIfsc) {
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

    private void uploadFarmAndFarmerData() {
        //progressDialog.show();
//        tv_add_new_farm_submit_bt.setClickable(false);
//        tv_add_new_farm_submit_bt.setEnabled(false);
//        progressBar_image.setVisibility(View.VISIBLE);
        if (farmerAndFarmData == null)
            farmerAndFarmData = new FarmerAndFarmData();
        String farmerName = "";
        String villageOrCity = "";
        String mandalOrTehsil = "";
        String district = "";
        String state = "";
        String country = "";

        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR)) {
            farmerAndFarmData.setSvId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
//            farmerAndFarmData.setSvName(supervisorDatum.getName());
        } else {
            if (supervisorDatum != null) {
                farmerAndFarmData.setSvId(supervisorDatum.getPersonId());
                farmerAndFarmData.setSvName(supervisorDatum.getName());
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
        farmerAndFarmData.setFinancial_status(financialStatus);
        farmerAndFarmData.setLiteracy(literacy);

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
            farmerAndFarmData.setIsSmartPhone("Y");
        } else {
            farmerAndFarmData.setIsSmartPhone("N");
        }
        farmerAndFarmData.setCountryId(countryId);
        farmerAndFarmData.setState(stateId);
        farmerAndFarmData.setCityId(districtId);

        isOtherAnimal = ti_et_other_animal.getText().toString();
        farmerAndFarmData.setSwiftCode(swiftCode);
        if (!TextUtils.isEmpty(ti_et_phone_address.getText().toString()))
            farmerAndFarmData.setMob2(ti_et_phone_address.getText().toString().trim());
        if (!TextUtils.isEmpty(emailEt.getText().toString()))
            farmerAndFarmData.setEmail(emailEt.getText().toString().trim());

//        farmerAndFarmData.setCast(etCast.getText().toString().trim());
//        farmerAndFarmData.setReligion(etReligion.getText().toString().trim());

        if (!TextUtils.isEmpty(familyCount.getText().toString()))
            farmerAndFarmData.setFamily_mem_count(familyCount.getText().toString().trim());
        if (!TextUtils.isEmpty(adultCount.getText().toString()))
            farmerAndFarmData.setAdults_count(adultCount.getText().toString().trim());
        if (!TextUtils.isEmpty(kidsCount.getText().toString()))
            farmerAndFarmData.setKids_count(kidsCount.getText().toString().trim());
        if (!TextUtils.isEmpty(dependentCount.getText().toString()))
            farmerAndFarmData.setDependent_count(dependentCount.getText().toString().trim());

        farmerAndFarmData.setAnimalCount(animalCount);
        farmerAndFarmData.setMilkSalePrice(milkSalePrice);
        farmerAndFarmData.setMilkInLitters(milkInLitters);
        farmerAndFarmData.setIsOtherAnimal(isOtherAnimal);
        farmerAndFarmData.setSpouse_name(etSpouse.getText().toString().trim());
        farmerAndFarmData.setUpi_id(upi);
        farmerAndFarmData.setBeneficiary_name(beneficiaryEt.getText().toString().trim());
        try {
            DDNew ddNew = civilStatusSpinnerAdapter.getItem(civil_status_spinner.getSelectedPosition());
            farmerAndFarmData.setCivil_status(ddNew.getParameters());

        } catch (Exception e) {

        }

        CowAndCatles cowAndCatles = new CowAndCatles(animalCount, milkInLitters, milkSalePrice, isOtherAnimal);
        farmerAndFarmData.setCowAndCatles(cowAndCatles);
        if (radioIsShareYes.isChecked()) {
            farmerAndFarmData.setIsShareHolder("Y");
        } else {
            farmerAndFarmData.setIsShareHolder("N");
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
        farmerAndFarmData.setShareJson(share);
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
        /*String clusterId;
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
        }*/
        farmerAndFarmData.setPan(pan);
        if (AppConstants.isValidString(dob))
            farmerAndFarmData.setDob(AppConstants.getUploadableDate(dob, context));
        farmerAndFarmData.setAnualIncome(anualIncome);
        farmerAndFarmData.setGeder(gender);
        farmerAndFarmData.setImage(imgBase64);
        farmerAndFarmData.setAddL1(addL1);
        farmerAndFarmData.setAddL2(addL2);
        farmerAndFarmData.setName(farmerName);
        farmerAndFarmData.setFather_name(fatherName);
        farmerAndFarmData.setMob(phoneNumber);
        farmerAndFarmData.setAadhaar(addharNumber);
        farmerAndFarmData.setVillage_or_city(villageOrCity);
        farmerAndFarmData.setMandal_or_tehsil(mandalOrTehsil);
        farmerAndFarmData.setDistrict(districtId);
        farmerAndFarmData.setCountryCode(countryCode);
        farmerAndFarmData.setState(stateId);
        farmerAndFarmData.setCountry(countryId);
        farmerAndFarmData.setBankName(bankName);
        farmerAndFarmData.setAccountNumber(accountNumber);
        farmerAndFarmData.setHolderName(holderName);
        farmerAndFarmData.setBranch(branch);
        farmerAndFarmData.setIfscCode(ifscCode);
        farmerAndFarmData.setFarmDataList(farmList);
        farmerAndFarmData.setAdded_by(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
        farmerAndFarmData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        farmerAndFarmData.setClusterId(clusterId);
        farmerAndFarmData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        farmerAndFarmData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
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
                farmerAndFarmData.setCasteId(caste);
                farmerAndFarmData.setCast(caste);
            }
        }
        if (religionSpinnerAdapter != null) {
            DDNew castCat = religionSpinnerAdapter.getItem(religionSpinner.getSelectedPosition());

            if (castCat != null) {
                religion = castCat.getId();
                farmerAndFarmData.setReligionId(caste);
                farmerAndFarmData.setReligion(caste);
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
//                farmerAndFarmData.setMiaId(caste);
            }
        }
        farmerAndFarmData.setCasteCategoryId(casteCategory);
        farmerAndFarmData.setCasteCategoryId(caste);
        farmerAndFarmData.setReligionId(religion);
        farmerAndFarmData.setMobileNetworkId(operator);
        farmerAndFarmData.setMotorhpId(motorHp);
        farmerAndFarmData.setMiaId(microIrri);

        if (selectedPersonDateSpouse == SELECTED_TYPE.DOB && AppConstants.isValidString(personDobStrSpouse)) {
            farmerAndFarmData.setSpouseDob(AppConstants.getUploadableDate(personDobStrSpouse, context));
        } else if (selectedPersonDateSpouse == SELECTED_TYPE.AGE && AppConstants.isValidString(personDobStrSpouse)) {
            farmerAndFarmData.setSpouseDob(personDobStrSpouse.trim() + "-01-01");
        } else if (selectedPersonDateSpouse == SELECTED_TYPE.DOB && AppConstants.isValidString(personDobStrSpouse)) {
            farmerAndFarmData.setSpouseDob(personDobStrSpouse.trim() + "-01-01");
        }
//                farmerAndFarmData.put("dob", farmerAndFarmData.getDob());

        if (selectedPersonDate == SELECTED_TYPE.DOB && AppConstants.isValidString(personDobStr)) {
            farmerAndFarmData.setDob(AppConstants.getUploadableDate(personDobStr, context));
        } else if (selectedPersonDate == SELECTED_TYPE.AGE && AppConstants.isValidString(personDobStr)) {
            farmerAndFarmData.setYob(getYOBByAge(Integer.valueOf(personDobStr.trim())));
            farmerAndFarmData.setDob(null);
        } else if (selectedPersonDate == SELECTED_TYPE.DOB && AppConstants.isValidString(personDobStr)) {
            farmerAndFarmData.setYob(personDobStr.trim());
            farmerAndFarmData.setDob(null);
        }

        if (AppConstants.isValidString(etSpouseDob.getText().toString()))
            farmerAndFarmData.setSpouseDob(etSpouseDob.getText().toString());
        Log.e(TAG, "Uploading Farm And Farmer Data: " + new Gson().toJson(farmerAndFarmData));
        // progressDialog.hide();
        Log.e(TAG, "casteCategory:" + casteCategory + ", caste:" + caste + ", religion:" + religion + ", operator:" + operator + ", microIrri:" + microIrri);

        Log.e(TAG, "Uploading Farm And Farmer Data: " + new Gson().toJson(farmerAndFarmData));
        // progressDialog.hide();
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            FarmAndFarmerAsync farmAndFarmerAsync = new FarmAndFarmerAsync(farmerAndFarmData);
            farmAndFarmerAsync.execute();
        } else {
            offlineUpdateFarm();
        }
    }

    FarmerAndFarmData farmerAndFarmData;
    FarmerT farmerT;

    private void offlineUpdateFarm() {
        tv_add_new_farm_submit_bt.setClickable(false);
        tv_add_new_farm_submit_bt.setEnabled(false);
        progressBar_image.setVisibility(View.VISIBLE);
        String fatherName = fatherNameEt.getText().toString().trim();
        if (farmerAndFarmData == null) {
            farmerAndFarmData = new FarmerAndFarmData();
            String farmerName = "0";
            String villageOrCity = "0";
            String mandalOrTehsil = "0";
            String district = "0";
            String state = "0";
            String country = "0";

            String districtId = null;
            try {
                districtId = citySpinnerAdapter.getItem(districtSpinner.getSelectedPosition()).getId();
                district = citySpinnerAdapter.getItem(districtSpinner.getSelectedPosition()).getName();
                Log.e(TAG, "Uploading Farm And Farmer Data " + districtSpinner.getSelectedPosition() + "  " + new Gson().toJson(citySpinnerAdapter.getItem(districtSpinner.getSelectedPosition())));
            } catch (Exception e) {

            }
            String stateId = null;
            try {
                stateId = stateSpinnerAdapter.getItem(stateSpinner.getSelectedPosition()).getId();
                state = stateSpinnerAdapter.getItem(stateSpinner.getSelectedPosition()).getName();
                Log.e(TAG, "Uploading Farm And Farmer Data " + stateSpinner.getSelectedPosition() + "  " + new Gson().toJson(stateSpinnerAdapter.getItem(stateSpinner.getSelectedPosition()).getName()));
            } catch (Exception e) {

            }
            String countryId = null;
            try {
                country = countrySpinnerAdapter.getItem(countrySpinner.getSelectedPosition()).getName();
                countryId = countrySpinnerAdapter.getItem(countrySpinner.getSelectedPosition()).getId();
                Log.e(TAG, "Uploading Farm And Farmer Data " + countrySpinner.getSelectedPosition() + "  " + new Gson().toJson(countrySpinnerAdapter.getItem(countrySpinner.getSelectedPosition()).getName()));
            } catch (Exception e) {

            }
            String bankName = null;
            String accountNumber = null;
            String holderName = null;
            String branch = null;
            String ifscCode = null;
            String addL1 = null;
            String addL2 = "";
            String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();

            String isShareHolder = "";//y or n
            String isCert = "";//y or n
            String howManyCert = null;
            String howMuchValue = null;
            String img_link = "";
            String is_owned = "";
            String upi = etUpi.getText().toString().trim();
            String swiftCode = etSwiftCode.getText().toString().trim();
            String animalCount = null;
            animalCount = ti_et_animal_count.getText().toString();
            String milkSalePrice = null;
            milkSalePrice = ti_et_milk_sale_price.getText().toString();
            String milkInLitters = null;
            milkInLitters = ti_et_milk_sale.getText().toString();
            String isOtherAnimal = "N";
            Share share = null;

            if (radioSPhoneYes.isChecked()) {
                farmerAndFarmData.setIsSmartPhone("Y");
            } else {
                farmerAndFarmData.setIsSmartPhone("N");
            }
            if (AppConstants.isValidString(countryId) && !countryId.equals("0"))
                farmerAndFarmData.setCountryId(countryId);
            if (AppConstants.isValidString(stateId) && !stateId.equals("0"))
                farmerAndFarmData.setState(stateId);
            if (AppConstants.isValidString(districtId) && !districtId.equals("0"))
                farmerAndFarmData.setCityId(districtId);

            isOtherAnimal = ti_et_other_animal.getText().toString();
            farmerAndFarmData.setSwiftCode(swiftCode);
            if (!TextUtils.isEmpty(ti_et_phone_address.getText().toString()))
                farmerAndFarmData.setMob2(ti_et_phone_address.getText().toString().trim());
            if (!TextUtils.isEmpty(emailEt.getText().toString()))
                farmerAndFarmData.setEmail(emailEt.getText().toString().trim());

//        farmerAndFarmData.setCast(etCast.getText().toString().trim());
//        farmerAndFarmData.setReligion(etReligion.getText().toString().trim());

            if (!TextUtils.isEmpty(familyCount.getText().toString()))
                farmerAndFarmData.setFamily_mem_count(familyCount.getText().toString().trim());
            if (!TextUtils.isEmpty(adultCount.getText().toString()))
                farmerAndFarmData.setAdults_count(adultCount.getText().toString().trim());
            if (!TextUtils.isEmpty(kidsCount.getText().toString()))
                farmerAndFarmData.setKids_count(kidsCount.getText().toString().trim());
            if (!TextUtils.isEmpty(dependentCount.getText().toString()))
                farmerAndFarmData.setDependent_count(dependentCount.getText().toString().trim());

            if (AppConstants.isValidString(animalCount) && !animalCount.equals("0"))
                farmerAndFarmData.setAnimalCount(animalCount);
            if (AppConstants.isValidString(milkSalePrice) && !milkSalePrice.equals("0"))
                farmerAndFarmData.setMilkSalePrice(milkSalePrice);
            if (AppConstants.isValidString(milkInLitters) && !milkInLitters.equals("0"))
                farmerAndFarmData.setMilkInLitters(milkInLitters);
            if (AppConstants.isValidString(isOtherAnimal) && !isOtherAnimal.equals("0"))
                farmerAndFarmData.setIsOtherAnimal(isOtherAnimal);


            farmerAndFarmData.setSpouse_name(etSpouse.getText().toString().trim());
            if (AppConstants.isValidString(upi) && !upi.equals("0"))
                farmerAndFarmData.setUpi_id(upi);
            farmerAndFarmData.setBeneficiary_name(beneficiaryEt.getText().toString().trim());

            try {
                DDNew ddNew = civilStatusSpinnerAdapter.getItem(civil_status_spinner.getSelectedPosition());

                farmerAndFarmData.setCivil_status(ddNew.getParameters());

            } catch (Exception e) {

            }


            CowAndCatles cowAndCatles = new CowAndCatles(animalCount, milkInLitters, milkSalePrice, isOtherAnimal);
            farmerAndFarmData.setCowAndCatles(cowAndCatles);
            if (radioIsShareYes.isChecked()) {
                farmerAndFarmData.setIsShareHolder("Y");
            } else {
                farmerAndFarmData.setIsShareHolder("N");
            }
            if (radioIsShareYes.isChecked() && radioShareCertYes.isChecked()) {
                howManyCert = ti_et_cert_count.getText().toString().trim();
                howMuchValue = ti_et_cert_value.getText().toString().trim();
                share = new Share("Y", howManyCert, howMuchValue);
            }
            String gender = "O";
            if (maleRadio.isChecked()) {
                gender = "M";
            } else if (femaleRadio.isChecked()) {
                gender = "F";
            }
            if (share != null)
                farmerAndFarmData.setShareJson(share);
            String dob = dobEt.getText().toString();
            String cast = etCast.getText().toString();
            String anualIncome = ti_et_income.getText().toString();
            farmerName = farmerNameEt.getText().toString();

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
            if (AppConstants.isValidString(pan) && !pan.equals("0"))
                farmerAndFarmData.setPan(pan);
            if (AppConstants.isValidString(dob) && !dob.equals("0"))
                farmerAndFarmData.setDob(AppConstants.getUploadableDate(dob, context));
            if (AppConstants.isValidString(anualIncome) && !anualIncome.equals("0"))
                farmerAndFarmData.setAnualIncome(anualIncome);
            if (AppConstants.isValidString(cast) && !cast.equals("0"))
                farmerAndFarmData.setCast(cast);
            if (AppConstants.isValidString(gender) && !gender.equals("0"))
                farmerAndFarmData.setGeder(gender);
            if (AppConstants.isValidString(imgBase64) && !imgBase64.equals("0"))
                farmerAndFarmData.setImage(imgBase64);
            if (AppConstants.isValidString(addL1) && !addL1.equals("0"))
                farmerAndFarmData.setAddL1(addL1);
            if (AppConstants.isValidString(addL2) && !addL2.equals("0"))
                farmerAndFarmData.setAddL2(addL2);
            if (AppConstants.isValidString(farmerName) && !farmerName.equals("0"))
                farmerAndFarmData.setName(farmerName);
            if (AppConstants.isValidString(phoneNumber) && !phoneNumber.equals("0"))
                farmerAndFarmData.setMob(phoneNumber);
            if (AppConstants.isValidString(addharNumber) && !addharNumber.equals("0"))
                farmerAndFarmData.setAadhaar(addharNumber);
            if (AppConstants.isValidString(villageOrCity) && !villageOrCity.equals("0"))
                farmerAndFarmData.setVillage_or_city(villageOrCity);
            if (AppConstants.isValidString(mandalOrTehsil) && !mandalOrTehsil.equals("0"))
                farmerAndFarmData.setMandal_or_tehsil(mandalOrTehsil);
            if (AppConstants.isValidString(district) && !district.equals("0"))
                farmerAndFarmData.setDistrict(district);
            if (AppConstants.isValidString(countryCode) && !countryCode.equals("0"))
                farmerAndFarmData.setCountryCode(countryCode);
            if (AppConstants.isValidString(state) && !state.equals("0"))
                farmerAndFarmData.setState(state);
            if (AppConstants.isValidString(country) && !country.equals("0"))
                farmerAndFarmData.setCountry(country);
            if (AppConstants.isValidString(bankName) && !bankName.equals("0"))
                farmerAndFarmData.setBankName(bankName);
            if (AppConstants.isValidString(accountNumber) && !accountNumber.equals("0"))
                farmerAndFarmData.setAccountNumber(accountNumber);
            if (AppConstants.isValidString(holderName) && !holderName.equals("0"))
                farmerAndFarmData.setHolderName(holderName);
            if (AppConstants.isValidString(branch) && !branch.equals("0"))
                farmerAndFarmData.setBranch(branch);
            if (AppConstants.isValidString(ifscCode) && !ifscCode.equals("0"))
                farmerAndFarmData.setIfscCode(ifscCode);
            farmerAndFarmData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            farmerAndFarmData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));

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
            if (microAgriSpinnerAdapter != null) {
                DDNew castCat = microAgriSpinnerAdapter.getItem(microIrriAwarenessSpinner.getSelectedPosition());

                if (castCat != null) {
                    microIrri = castCat.getId();
                }
            }
            if (AppConstants.isValidString(casteCategory) && !casteCategory.equals("0")) {
                farmerAndFarmData.setCasteCategoryId(casteCategory);
            }
            if (AppConstants.isValidString(caste) && !caste.equals("0")) {
                farmerAndFarmData.setCast(caste);
                farmerAndFarmData.setCasteId(caste);
            }
            if (AppConstants.isValidString(religion) && !religion.equals("0")) {
                farmerAndFarmData.setReligionId(religion);
                farmerAndFarmData.setReligion(religion);
            }
            if (AppConstants.isValidString(operator) && !operator.equals("0"))
                farmerAndFarmData.setMobileNetworkId(operator);
            if (AppConstants.isValidString(motorHp) && !motorHp.equals("0"))
                farmerAndFarmData.setMotorhpId(motorHp);
            if (AppConstants.isValidString(microIrri) && !microIrri.equals("0"))
                farmerAndFarmData.setMiaId(microIrri);
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
            if (AppConstants.isValidString(financialStatus) && !financialStatus.equals("0"))
                farmerAndFarmData.setFinancial_status(financialStatus);
            if (AppConstants.isValidString(literacy) && !literacy.equals("0"))
                farmerAndFarmData.setLiteracy(literacy);
            if (AppConstants.isValidString(etSpouseDob.getText().toString()))
                farmerAndFarmData.setSpouseDob(etSpouseDob.getText().toString());


            if (AppConstants.isValidString(addedBy))
                farmerAndFarmData.setAdded_by(addedBy);


            if (AppConstants.isValidString(clusterId))
                farmerAndFarmData.setClusterId(clusterId);
            if (selectedPersonDateSpouse == SELECTED_TYPE.DOB && AppConstants.isValidString(personDobStrSpouse)) {
                farmerAndFarmData.setSpouseDob(AppConstants.getUploadableDate(personDobStrSpouse, context));
            } else if (selectedPersonDateSpouse == SELECTED_TYPE.AGE && AppConstants.isValidString(personDobStrSpouse)) {
                farmerAndFarmData.setSpouseDob(personDobStrSpouse.trim() + "-01-01");
            } else if (selectedPersonDateSpouse == SELECTED_TYPE.DOB && AppConstants.isValidString(personDobStrSpouse)) {
                farmerAndFarmData.setSpouseDob(personDobStrSpouse.trim() + "-01-01");
            }
//                farmData.put("dob", farmerAndFarmData.getDob());

            if (selectedPersonDate == SELECTED_TYPE.DOB && AppConstants.isValidString(personDobStr)) {
                farmerAndFarmData.setDob(AppConstants.getUploadableDate(personDobStr, context));
            } else if (selectedPersonDate == SELECTED_TYPE.AGE && AppConstants.isValidString(personDobStr)) {
                farmerAndFarmData.setYob(getYOBByAge(Integer.valueOf(personDobStr.trim())));
            } else if (selectedPersonDate == SELECTED_TYPE.DOB && AppConstants.isValidString(personDobStr)) {
                farmerAndFarmData.setYob(personDobStr.trim());
            }
        }


        {
            FarmData data = farmList.get(0);
            data.setCityDatumList(new ArrayList<>());
            data.setStateDatumList(new ArrayList<>());
            Log.e(TAG, "COUNTRYOFF: " + data.getCountry() + ", " + data.getState() + ", " + data.getDistrict());
            if (farmerAndFarmData.getFarmDataList() != null && farmerAndFarmData.getFarmDataList().size() > 0) {
                try {
                    int index = 0;
                    index = getFarmDataIndex(data.getFarmId(), farmerAndFarmData.getFarmDataList());
                    if (index != -1 && index < farmerAndFarmData.getFarmDataList().size())
                        farmerAndFarmData.getFarmDataList().set(index, data);

                } catch (Exception e) {
                    farmerAndFarmData.getFarmDataList().set(0, data);
                    e.printStackTrace();
                }
            } else {
                farmerAndFarmData.setFarmDataList(new ArrayList<>());
                farmerAndFarmData.getFarmDataList().add(data);
            }
            String id = "" + Calendar.getInstance().getTime().getTime();
            farmerAndFarmData.setImage(pictureImagePath);
            if (farmerT != null) {
                farmerT.setIsUpdated("Y");
                farmerT.setIsUploaded("N");
                farmerT.setData(new Gson().toJson(farmerAndFarmData));
            } else
                farmerT = new FarmerT(id, new Gson().toJson(farmerAndFarmData), "N", "Y", "Y");

            String name = farmerAndFarmData.getName();
            farmerAndFarmData.setFather_name(fatherName);
            FarmerCacheManager.getInstance(context).updateFarm(new FarmerCacheManager.OnUpdateSuccessListener() {
                @Override
                public void onFarmerUpdated(boolean isUpdated) {

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

                    farm.setIs_irrigated(data.getIs_irrigated());
                    farm.setCropping_pattern(data.getCropping_pattern());
                    farm.setPlanting_method(data.getPlanting_method());

                    farm.setLotNo(data.getId());
                    farm.setAddL1(data.getAddL1());
                    farm.setAddL2(data.getAddL2());
                    farm.setVillageOrCity(data.getVillageOrCity());
                    farm.setMandalOrTehsil(data.getMandalOrTehsil());
                    farm.setDistrict(data.getDistrict());
                    farm.setState(data.getState());
                    farm.setCountry(data.getCountry());
                    farm.setExpArea(data.getArea());
                    farm.setIrrigationSource(data.getIrrigationsource());
                    farm.setIrrigationType(data.getIrrigationtype());
                    farm.setPreviousCrop(data.getPreviouscrop());
                    farm.setFarmerName(name);//////
                    farm.setFatherName(farmerAndFarmData.getFather_name());
                    farm.setIsUpdated("Y");
                    farm.setIsUploaded("N");
                    farm.setFpoData(fpoData);
                    farm.setCropId(data.getCropId());
                    farm.setFarmName(data.getName());
                    farm.setIsOwned(data.getIsOwned());
                    farm.setSeasonId(data.getSeason());
                    farm.setTransplant_date(data.getTransplant_date());
                    farm.setFarmFullData(new Gson().toJson(data));

                    farm.setIrriTypeId(irriTypeId);
                    farm.setIrriSourceId(irriSourceId);
                    farm.setSoilTypeId(soilTypeId);
                    if (data.getAssetsList() != null && data.getAssetsList().size() > 0)
                        farm.setAssets(new Gson().toJson(data.getAssetsList()));
                    FarmCacheManager.getInstance(context).updateFarm(farm);

                    AddFarmCCacheManager.getInstance(context).deleteAllData();
                    Toasty.success(context, resources.getString(R.string.farmer_and_farm_update_succes_msg), Toast.LENGTH_LONG).show();
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
        adapter = new UpdateFarmAdapter(context, farmList, irrigationTypeDDList, irrigationSourceDDList, soilTypeDDList, seasonDDList, farmCropList,
                this.plantingMethodDDList, this.isIrrigatedDDList, this.croppingPatternDDList,
                cropFormDatumList, cropFormDatumListSuper, countryDatumList, FarmDetailsUpdateActivity2.this);
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
        if (!isEditDisabled)
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

        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            farmFullDetails = DataHandler.newInstance().getFarmFullDetails();
            if (farmFullDetails != null)
                setPreviousData(farmFullDetails);
            else
                getFullDetails();

        } else {
            Log.e(TAG, "Offline farmId " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
            FarmCacheManager.getInstance(context).getFarm(new FarmLoadListener() {
                @Override
                public void onFarmLoader(Farm farm) {
                    Log.d(TAG, "Offline farm " + new Gson().toJson(farm));
//                                    Toast.makeText(FarmDetailsUpdateActivity2.this, " "+new Gson().toJson(farm), Toast.LENGTH_SHORT).show();
                    setPreviousDataOffline(farm);
                }
            }, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
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
                    Log.e(TAG, "Response Make  " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, response.getMessage());
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
                            if (response.getData() != null) {
                                adapter.setCropFormDatumList(response.getData());

                            }
                            if (response.getSuperData() != null) {
                                adapter.setCropFormDatumListSuper(response.getSuperData());

                            }
                            if (response.getData() != null) {
                                FarmDetailsUpdateActivity2.this.cropFormDatumList.addAll(response.getData());
                                for (CropFormDatum cropFormDatum : response.getData()) {
                                    FarmDetailsUpdateActivity2.this.cropFormDatumListMap.put(cropFormDatum.getInfoTypeId(), cropFormDatum);
                                }
                            }

                            if (response.getSuperData() != null) {
                                FarmDetailsUpdateActivity2.this.cropFormDatumListSuper.addAll(response.getSuperData());
                                for (CropFormDatum cropFormDatum : response.getSuperData()) {
                                    if (cropFormDatum.getType().trim().equals("Equipment / Machinery"))
                                        FarmDetailsUpdateActivity2.this.cropFormDatumListSuperMap.put("E", cropFormDatum);
                                    else
                                        FarmDetailsUpdateActivity2.this.cropFormDatumListSuperMap.put("A", cropFormDatum);
                                }
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
                Log.e(TAG, "Failure getOfflineData " + t.toString());
            }
        });


    }


   /* private void getAllCountry() {
        try {
            countryDatumList.clear();
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);

            Call<CountryResponse> fetchFarmDataCall = apiService.getAllCountries(
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            fetchFarmDataCall.enqueue(new Callback<CountryResponse>() {
                @Override
                public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response != null) {
                                CountryResponse countryResponse = response.body();
                                if (response.body().getStatus() == 10) {
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, response.body().getMsg());
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.UNAUTH_ACCESS) {
                                    //un authorized access
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.unauthorized_access));
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.AUTH_EXPIRED) {
                                    // auth token expired
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.authorization_expired));
                                } else {
                                    DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.COUNTRIES);
                                    DropDownT cropDD = new DropDownT(AppConstants.CHEMICAL_UNIT.COUNTRIES, new Gson().toJson(countryResponse));
                                    DropDownCacheManager.getInstance(context).addChemicalUnit(cropDD);
                                    try {
//                                        initializeCountry();
                                        if (countryResponse.getData() != null)
                                            countryDatumList.addAll(countryResponse.getData());
                                        countrySpinnerAdapter.notifyDataSetChanged();
                                        if (adapter != null)
                                            adapter.setCountryDatumList(countryDatumList);
                                    } catch (Exception e) {
                                    }
                                }
                            } else if (response.code() == AppConstants.API_STATUS.UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.code() == AppConstants.API_STATUS.AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.authorization_expired));
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
                public void onFailure(Call<CountryResponse> call, Throwable t) {

                }
            });
        } catch (Exception r) {
            r.printStackTrace();
        }
    }

    private void getStates(String countryId) {
        try {
            initializeState();
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);

            Call<StateResponse> fetchFarmDataCall = apiService.getAllStates(
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN), countryId);
            fetchFarmDataCall.enqueue(new Callback<StateResponse>() {
                @Override
                public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response != null) {
                                StateResponse countryResponse = response.body();
                                if (response.body().getStatus() == 10) {
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, response.body().getMsg());
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.UNAUTH_ACCESS) {
                                    //un authorized access
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.unauthorized_access));
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.AUTH_EXPIRED) {
                                    // auth token expired
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.authorization_expired));
                                } else {

                                    try {
                                        if (countryResponse.getData() != null)
                                            stateDatumList.addAll(countryResponse.getData());
                                        stateSpinnerAdapter.notifyDataSetChanged();
                                    } catch (Exception e) {
                                    }
                                }
                            } else if (response.code() == AppConstants.API_STATUS.UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.code() == AppConstants.API_STATUS.AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.authorization_expired));
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
                public void onFailure(Call<StateResponse> call, Throwable t) {

                }
            });
        } catch (Exception r) {
            r.printStackTrace();
        }
    }*/

    private void initializeCountry() {
        countryDatumList.clear();
//        countryDatumList.add(new CountryDatum("", resources.getString(R.string.country_prompt)));
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
                                    viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, response.body().getMsg());
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                    //un authorized access
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.unauthorized_access));
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                    // auth token expired
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.authorization_expired));
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
                                viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.authorization_expired));
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
                        viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.multiple_login_msg));
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
                    viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.unauthorized_access));
                } else if (!isOfflineMode && response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.authorization_expired));
                } else {
                    //progressDialog.dismiss();
                    progressBar_image.setVisibility(View.GONE);
                    isSpinnerDataLoaded = false;
                    if (!isEditDisabled)
                        tv_add_new_farm_submit_bt.setVisibility(View.VISIBLE);
                    try {
                        error = response.errorBody().string().toString();
                        Log.e("SpinnerResponse ", "err  " + error);
                        if (!isOfflineMode)
                            viewFailDialog.showDialogForFinish(FarmDetailsUpdateActivity2.this, resources.getString(R.string.failed_to_load_data_msg));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(FarmDetailsUpdateActivity2.this, call.request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }


            }


            @Override
            public void onFailure(Call<SpinnerResponse> call, Throwable t) {
                Log.e("SpinnerResponse", t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(FarmDetailsUpdateActivity2.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                if (t instanceof SocketTimeoutException) {
                    getSpinnerData();
                } else {
                    // progressDialog.dismiss();
                    if (isEditDisabled)
                        tv_add_new_farm_submit_bt.setVisibility(View.VISIBLE);
                    progressBar_image.setVisibility(View.GONE);
                    isSpinnerDataLoaded = false;
                    if (!isOfflineMode)
                        viewFailDialog.showDialogForFinish(FarmDetailsUpdateActivity2.this, resources.getString(R.string.failed_to_load_data_msg));
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


    private void internetFlow(boolean isLoaded) {
        try {

            if (!isLoaded) {
                if (!ConnectivityUtils.isConnected(context)) {
                    AppConstants.failToast(context, resources.getString(R.string.check_internet_connection_msg));
                } else {
                    ConnectivityUtils.checkSpeedWithColor(FarmDetailsUpdateActivity2.this, new ConnectivityUtils.ColorListener() {
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

        if (farmerAndFarmDataCache != null && farmerAndFarmDataCache.getPersonId() != null && !TextUtils.isEmpty(farmerAndFarmDataCache.getPersonId())) {
            int selection = 0;
            for (int i = 0; i < farmerDataList.size(); i++) {
                if (String.valueOf(farmerDataList.get(i).getPersonId()).equals(farmerAndFarmDataCache.getPersonId())) {
                    selection = i;
                    break;
                }
            }
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
                .addOnConnectionFailedListener(FarmDetailsUpdateActivity2.this).build();
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
                            status.startResolutionForResult(FarmDetailsUpdateActivity2.this, 1000);
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
                                ActivityCompat.requestPermissions(FarmDetailsUpdateActivity2.this,
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

    }

    EditText ownershipImageEt, farmImageEt;

    @Override
    public void onOwnerShipImageClick(int index, EditText editText) {
//        Toast.makeText(context, "index "+index, Toast.LENGTH_SHORT).show();
        ownershipImageEt = editText;
        imageIndex = index;
        selectImage(CaptureConstants.CAPTURE5);
    }

    int imageIndex = -1;

    @Override
    public void onFarmImageClick(int index, EditText editText) {
//        Toast.makeText(context, "index "+index, Toast.LENGTH_SHORT).show();
        farmImageEt = editText;
        imageIndex = index;
        selectImage(CaptureConstants.CAPTURE4);
    }

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
        if (!isEditDisabled) {
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
        } else finish();
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
                                viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                hideProgress();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.authorization_expired));
                            } else if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                hideProgress();
                                viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, response.body().getMsg());
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
                            viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            hideProgress();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.authorization_expired));
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

    String farmAddedBy = null, farmClusterId = null, farmCompId = null;

    private class FarmAndFarmerAsync extends AsyncTask<String, Integer, String> {
        FarmerAndFarmData farmerAndFarmData;

        public FarmAndFarmerAsync(FarmerAndFarmData farmerAndFarmData) {
            this.farmerAndFarmData = farmerAndFarmData;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject farmData = new JSONObject();
//                if (AppConstants.isValidString(farmerAndFarmData.getSpouseDob()))
//                    farmData.put("spouse_dob", AppConstants.getUploadableDate(farmerAndFarmData.getSpouseDob(), context));
//                else
//                    farmData.put("spouse_dob", null);

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
                        farmData.put("dob", "");
                    } catch (Exception e) {

                    }
                } else if (selectedPersonDate == SELECTED_TYPE.YOB && AppConstants.isValidString(personDobStr)) {
                    farmData.put("yob", personDobStr.trim());
                    farmData.put("dob", "");
                }

                if (AppConstants.isValidString(farmerAndFarmData.getLiteracy()))
                    farmData.put("literacy", farmerAndFarmData.getLiteracy());
                if (AppConstants.isValidString(farmerAndFarmData.getFinancial_status()))
                    farmData.put("financial_status", farmerAndFarmData.getFinancial_status());

                if (AppConstants.isValidString(imgBase64)) {
                    farmData.put("img_link", imgBase64);
                }
               /* if (AppConstants.isValidString(imgBase64IDProof)) {
                    farmData.put("img_link", imgBase64AddressProf);
                }*/

                if (AppConstants.isValidString(ownerId))
                    farmData.put("person_id", ownerId);
                if (AppConstants.isValidString(addedBy))
                    farmData.put("added_by", addedBy);
                if (farmerAndFarmData.getCasteCategoryId() != null && !farmerAndFarmData.getCasteCategoryId().equals("0"))
                    farmData.put("caste_category", farmerAndFarmData.getCasteCategoryId());
                if (farmerAndFarmData.getCasteId() != null && !farmerAndFarmData.getCasteId().equals("0"))
                    farmData.put("caste", farmerAndFarmData.getCasteId());
                if (farmerAndFarmData.getMobileNetworkId() != null && !farmerAndFarmData.getMobileNetworkId().equals("0"))
                    farmData.put("mobile_operator", farmerAndFarmData.getMobileNetworkId());
                if (farmerAndFarmData.getReligionId() != null && !farmerAndFarmData.getReligionId().equals("0"))
                    farmData.put("religion", farmerAndFarmData.getReligionId());
                if (farmerAndFarmData.getMiaId() != null && !farmerAndFarmData.getMiaId().equals("0"))
                    farmData.put("mia", farmerAndFarmData.getMiaId());

                if (farmerAndFarmData.getDistrict() != null && !farmerAndFarmData.getDistrict().equals("0"))
                    farmData.put("pdistrict", farmerAndFarmData.getDistrict());
                if (farmerAndFarmData.getState() != null && !farmerAndFarmData.getState().equals("0"))
                    farmData.put("pstate", farmerAndFarmData.getState());
                if (farmerAndFarmData.getCountry() != null && !farmerAndFarmData.getCountry().equals("0"))
                    farmData.put("pcountry", farmerAndFarmData.getCountry());

                if (farmerAndFarmData.getFamily_mem_count() != null && !farmerAndFarmData.getFamily_mem_count().equals("0"))
                    farmData.put("family_mem_count", farmerAndFarmData.getFamily_mem_count());
                if (farmerAndFarmData.getAdults_count() != null && !farmerAndFarmData.getAdults_count().equals("0"))
                    farmData.put("adults_count", farmerAndFarmData.getAdults_count());
                if (farmerAndFarmData.getKids_count() != null && !farmerAndFarmData.getKids_count().equals("0"))
                    farmData.put("kids_count", farmerAndFarmData.getKids_count());
                if (farmerAndFarmData.getDependent_count() != null && !farmerAndFarmData.getDependent_count().equals("0"))
                    farmData.put("dependent_count", farmerAndFarmData.getDependent_count());
                if (farmerAndFarmData.getIsFarmAndFarmer() != null && !farmerAndFarmData.getIsFarmAndFarmer().equals("0"))
                    farmData.put("isFarmAndFarmer", farmerAndFarmData.getIsFarmAndFarmer());
                if (farmerAndFarmData.getUpi_id() != null && !farmerAndFarmData.getUpi_id().equals("0"))
                    farmData.put("upi_id", farmerAndFarmData.getUpi_id());
                if (farmerAndFarmData.getCivil_status() != null && !farmerAndFarmData.getCivil_status().equals("0"))
                    farmData.put("civil_status", farmerAndFarmData.getCivil_status());
                if (farmerAndFarmData.getSpouse_name() != null && !farmerAndFarmData.getSpouse_name().equals("0"))
                    farmData.put("spouse_name", farmerAndFarmData.getSpouse_name());
                if (farmerAndFarmData.getBeneficiary_name() != null && !farmerAndFarmData.getBeneficiary_name().equals("0"))
                    farmData.put("beneficiary_name", farmerAndFarmData.getBeneficiary_name());
                if (farmerAndFarmData.getImage() != null && !farmerAndFarmData.getImage().equals("0"))
                    farmData.put("image", farmerAndFarmData.getImage());
                if (farmerAndFarmData.getIsUsingPhone() != null && !farmerAndFarmData.getIsUsingPhone().equals("0"))
                    farmData.put("isUsingPhone", farmerAndFarmData.getIsUsingPhone());
                if (farmerAndFarmData.getIsSmartPhone() != null && !farmerAndFarmData.getIsSmartPhone().equals("0"))
                    farmData.put("isSmartPhone", farmerAndFarmData.getIsSmartPhone());
                if (farmerAndFarmData.getIsShareHolder() != null && !farmerAndFarmData.getIsShareHolder().equals("0"))
                    farmData.put("isShareHolder", farmerAndFarmData.getIsShareHolder());
                if (farmerAndFarmData.getSvId() != null && !farmerAndFarmData.getSvId().equals("0"))
                    farmData.put("sv_id", farmerAndFarmData.getSvId());
                if (farmerAndFarmData.getSvName() != null && !farmerAndFarmData.getSvName().equals("0"))
                    farmData.put("sv_name", farmerAndFarmData.getSvName());
                JSONObject shareJson = null;
                try {
                    shareJson = new JSONObject(new Gson().toJson(farmerAndFarmData.getShareJson()));
                } catch (Exception e) {

                }
                if (shareJson != null)
                    farmData.put("share_json", shareJson);
                if (farmerAndFarmData.getName() != null && !farmerAndFarmData.getName().equals("0"))
                    farmData.put("name", farmerAndFarmData.getName());
                if (farmerAndFarmData.getFather_name() != null && !farmerAndFarmData.getFather_name().equals("0"))
                    farmData.put("father_name", farmerAndFarmData.getFather_name());
                if (farmerAndFarmData.getMob() != null && !farmerAndFarmData.getMob().equals("0"))
                    farmData.put("mob", farmerAndFarmData.getMob());
                if (farmerAndFarmData.getMob2() != null && !farmerAndFarmData.getMob2().equals("0"))
                    farmData.put("mob2", farmerAndFarmData.getMob2());
                if (farmerAndFarmData.getEmail() != null && !farmerAndFarmData.getEmail().equals("0"))
                    farmData.put("email", farmerAndFarmData.getEmail());
                if (farmerAndFarmData.getAadhaar() != null && !farmerAndFarmData.getAadhaar().equals("0"))
                    farmData.put("aadhar", farmerAndFarmData.getAadhaar());
                if (farmerAndFarmData.getPan() != null && !farmerAndFarmData.getPan().equals("0"))
                    farmData.put("pan", farmerAndFarmData.getPan());
                if (farmerAndFarmData.getAddL1() != null && !farmerAndFarmData.getAddL1().equals("0"))
                    farmData.put("pAddL1", farmerAndFarmData.getAddL1());
                if (farmerAndFarmData.getAddL2() != null && !farmerAndFarmData.getAddL2().equals("0"))
                    farmData.put("pAddL2", farmerAndFarmData.getAddL2());
                if (farmerAndFarmData.getVillage_or_city() != null && !farmerAndFarmData.getVillage_or_city().equals("0"))
                    farmData.put("pvillage_or_city", farmerAndFarmData.getVillage_or_city());
                if (farmerAndFarmData.getMandal_or_tehsil() != null && !farmerAndFarmData.getMandal_or_tehsil().equals("0"))
                    farmData.put("pmandal_or_tehsil", farmerAndFarmData.getMandal_or_tehsil());
                if (farmerAndFarmData.getBankName() != null && !farmerAndFarmData.getBankName().equals("0"))
                    farmData.put("bank_name", farmerAndFarmData.getBankName());
                if (farmerAndFarmData.getAccountNumber() != null && !farmerAndFarmData.getAccountNumber().equals("0"))
                    farmData.put("account_no", farmerAndFarmData.getAccountNumber());
                if (farmerAndFarmData.getHolderName() != null && !farmerAndFarmData.getHolderName().equals("0"))
                    farmData.put("account_name", farmerAndFarmData.getHolderName());
                if (farmerAndFarmData.getBranch() != null && !farmerAndFarmData.getBranch().equals("0"))
                    farmData.put("branch", farmerAndFarmData.getBranch());
                if (farmerAndFarmData.getIfscCode() != null && !farmerAndFarmData.getIfscCode().equals("0"))
                    farmData.put("ifsc", farmerAndFarmData.getIfscCode());
//                if (farmerAndFarmData.getClusterId() != null && !farmerAndFarmData.getClusterId().equals("0"))
                farmData.put("cluster_id", clusterId);
                if (farmerAndFarmData.getCompId() != null && !farmerAndFarmData.getCompId().equals("0"))
                    farmData.put("comp_id", farmerAndFarmData.getCompId());
                if (AppConstants.isValidString(ownerId) && !ownerId.equals("0"))
                    farmData.put("owner_id", ownerId);
                if (farmerAndFarmData.getUserId() != null && !farmerAndFarmData.getUserId().equals("0"))
                    farmData.put("user_id", farmerAndFarmData.getUserId());
                if (farmerAndFarmData.getToken() != null && !farmerAndFarmData.getToken().equals("0"))
                    farmData.put("token", farmerAndFarmData.getToken());
                if (farmerAndFarmData.getCountryCode() != null && !farmerAndFarmData.getCountryCode().equals("0"))
                    farmData.put("countryCode", farmerAndFarmData.getCountryCode());
//                if (farmerAndFarmData.getDob() != null && !farmerAndFarmData.getDob().equals("0"))
//                    farmData.put("dob", farmerAndFarmData.getDob());

                if (farmerAndFarmData.getAnualIncome() != null && !farmerAndFarmData.getAnualIncome().equals("0"))
                    farmData.put("est_income", farmerAndFarmData.getAnualIncome());
                if (farmerAndFarmData.getGeder() != null && !farmerAndFarmData.getGeder().equals("0"))
                    farmData.put("gender", farmerAndFarmData.getGeder());
                if (farmerAndFarmData.getSwiftCode() != null && !farmerAndFarmData.getSwiftCode().equals("0"))
                    farmData.put("swift_code", farmerAndFarmData.getSwiftCode());

                JSONArray items = new JSONArray();

                for (int i = 0; i < farmerAndFarmData.getFarmDataList().size(); i++) {
                    FarmData farmData1 = farmerAndFarmData.getFarmDataList().get(i);
                    JSONObject farm = new JSONObject();
                    farm.put("farm_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                    if (AppConstants.isValidString(farmData1.getOwnerShipDoc()))
                        farm.put("ownership_doc", farmData1.getOwnerShipDoc());
                    if (AppConstants.isValidString(farmData1.getFarmImage()))
                        farm.put("farm_photo", farmData1.getFarmImage());
                    farm.put("added_by", farmAddedBy);
                    farm.put("comp_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                    farm.put("cluster_id", farmClusterId);
                    if (AppConstants.isValidString(farmData1.getTransplant_date()) && !farmData1.getTransplant_date().equals("0"))
                        farm.put("transplant_date", farmData1.getTransplant_date());
                    if (AppConstants.isValidString(farmData1.getCountry()) && !farmData1.getCountry().equals("0"))
                        farm.put("fcountry", farmData1.getCountry());
                    if (farmData1.getState() != null && !farmData1.getState().equals("0"))
                        farm.put("fstate", farmData1.getState());
                    if (farmData1.getDistrict() != null && !farmData1.getDistrict().equals("0"))
                        farm.put("fdistrict", farmData1.getDistrict());
                    if (farmData1.getIsOwned() != null && !farmData1.getIsOwned().equals("0"))
                        farm.put("is_owned", farmData1.getIsOwned());
                    if (AppConstants.isValidString(ownerId) && !ownerId.equals("0"))
                        farm.put("owner_id", ownerId);
                    if (farmData1.getId() != null && !farmData1.getId().equals("0"))
                        farm.put("lot_no", farmData1.getId());
                    if (farmData1.getName() != null && !farmData1.getName().equals("0"))
                        farm.put("farm_name", farmData1.getName());
                    if (farmData1.getArea() != null && !farmData1.getArea().equals("0"))
                        farm.put("exp_area", farmData1.getArea());
                    if (farmData1.getCropping_pattern() != null && !farmData1.getCropping_pattern().equals("0"))
                        farm.put("cropping_pattern", farmData1.getCropping_pattern());
                    if (farmData1.getIs_irrigated() != null && !farmData1.getIs_irrigated().equals("0"))
                        farm.put("is_irrigated", farmData1.getIs_irrigated());
                    if (AppConstants.isValidString(farmData1.getSeason()) && !farmData1.getSeason().equals("0"))
                        farm.put("season_num", farmData1.getSeason());
                    if (AppConstants.isValidString(farmData1.getPlanting_method()) && !farmData1.getPlanting_method().equals("0"))
                        farm.put("planting_method", farmData1.getPlanting_method());
//                    if (farmData1.getPreviouscrop() != null && !farmData1.getPreviouscrop().equals("0"))
//                        farm.put("previous_crop", farmData1.getPreviouscrop());
                    if (AppConstants.isValidString(farmData1.getIrrigationsource()) && !farmData1.getIrrigationsource().equals("0"))
                        farm.put("irrigation_source_id", farmData1.getIrrigationsource());
                    if (farmData1.getIrriSourceName() != null && !farmData1.getIrriSourceName().equals("0"))
                        farm.put("irri_source_name", farmData1.getIrriSourceName());
                    if (AppConstants.isValidString(farmData1.getIrrigationtype()) && !farmData1.getIrrigationtype().equals("0"))
                        farm.put("irrigation_type_id", farmData1.getIrrigationtype());
                    if (farmData1.getIrriTypename() != null && !farmData1.getIrriTypename().equals("0"))
                        farm.put("irri_type_name", farmData1.getIrriTypename());
                    if (AppConstants.isValidString(farmData1.getSoiltype()) && !farmData1.getSoiltype().equals("0"))
                        farm.put("soil_type_id", farmData1.getSoiltype());
                    if (farmData1.getSoiltypeName() != null && !farmData1.getSoiltypeName().equals("0"))
                        farm.put("soil_type_name", farmData1.getSoiltypeName());
                    if (farmData1.getAddL1() != null && !farmData1.getAddL1().equals("0"))
                        farm.put("fAddL1", farmData1.getAddL1());
                    if (farmData1.getAddL2() != null && !farmData1.getAddL2().equals("0"))
                        farm.put("fAddL2", farmData1.getAddL2());
                    if (farmData1.getVillageOrCity() != null && !farmData1.getVillageOrCity().equals("0"))
                        farm.put("fvillage_or_city", farmData1.getVillageOrCity());
                    if (farmData1.getMandalOrTehsil() != null && !farmData1.getMandalOrTehsil().equals("0"))
                        farm.put("fmandal_or_tehsil", farmData1.getMandalOrTehsil());

//                    farm.put("fdistrict", farmData1.getDistrict());
//                    farm.put("fstate", farmData1.getState());
//                    farm.put("fcountry", farmData1.getCountry());
                    if (farmData1.getCropId() != null && !farmData1.getCropId().equals("0"))
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
                farmData.putOpt("item", items.get(0));
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParser.parse(farmData.toString());
                Log.e(TAG, "Uploading1 " + new Gson().toJson(jsonObject));
                String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                apiInterface.updateFarmAndFarmer(jsonObject).enqueue(new Callback<UpdatRsponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(Call<UpdatRsponse> call, Response<UpdatRsponse> response) {
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
                                        viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.multiple_login_msg));
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
                                        viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.unauthorized_access));
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
                                        viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.authorization_expired));
                                    }
                                });
                            } else if (response.body().getStatus() == 1) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            AddFarmCCacheManager.getInstance(context).deleteAllData();
                                            Toasty.success(context, resources.getString(R.string.farmer_and_farm_update_succes_msg), Toast.LENGTH_LONG).show();
                                            if (AppConstants.isValidString(response.body().getPersonId()))
                                                ownerId = "" + response.body().getPersonId();
                                            if (AppConstants.isValidString(ownerId) && myBitmapId != null) {
                                                uploadIDImage(ownerId + "");
                                            } else if (AppConstants.isValidString(ownerId) && myBitmapAddress != null) {
                                                uploadAddressIdImage(ownerId + "");
                                            } else if (latLngListCheckArea != null && latLngListCheckArea.size() > 2) {
                                                progressBar_image.setVisibility(View.VISIBLE);
                                                SubmitAreaAsync areaAsync = new SubmitAreaAsync();
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
                                    viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.unauthorized_access));
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
                                    viewFailDialog.showSessionExpireDialog(FarmDetailsUpdateActivity2.this, resources.getString(R.string.authorization_expired));
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
                                notifyApiDelay(FarmDetailsUpdateActivity2.this, response.raw().request().url().toString(),
                                        "" + diff, internetSPeed, error, response.code());
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdatRsponse> call, Throwable t) {
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
                                    notifyApiDelay(FarmDetailsUpdateActivity2.this, call.request().url().toString(),
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
                Log.e(TAG, "Failure getOfflineData " + t.toString());
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
                Log.e(TAG, "Failure getOfflineData " + t.toString());
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
                Log.e(TAG, "Failure getOfflineData " + t.toString());
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
                Log.e(TAG, "Failure getOfflineData " + t.toString());
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
                Log.e(TAG, "Failure getOfflineData " + t.toString());
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
                Log.e(TAG, "Failure getOfflineData " + t.toString());
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
                Log.e(TAG, "Failure getOfflineData " + t.toString());
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
                Log.e(TAG, "Failure getOfflineData " + t.toString());
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


    private void getFullDetails() {
        JsonObject object = new JsonObject();
        object.addProperty("farm_id", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
        object.addProperty("user_id", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        object.addProperty("token", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        Log.e(TAG, "SEND FULL DETAIL PARAM  " + new Gson().toJson(object));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        apiInterface.getFullFarmDetails(object).enqueue(new Callback<FarmFullDetails>() {
            @Override
            public void onResponse(Call<FarmFullDetails> call, Response<FarmFullDetails> response1) {
                String error = null;
                Log.e(TAG, "Response FULL CODE" + response1.code());
                if (response1.isSuccessful()) {
                    Log.e(TAG, "Response FULL  " + new Gson().toJson(response1.body()));
                    farmFullDetails = response1.body();
                    DataHandler.newInstance().setFarmFullDetails(farmFullDetails);
                    setPreviousData(farmFullDetails);

                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    DataHandler.newInstance().setFarmFullDetails(null);
                    //un authorized access
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    DataHandler.newInstance().setFarmFullDetails(null);
                    // auth token expired
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        DataHandler.newInstance().setFarmFullDetails(null);
                        error = response1.errorBody().string().toString();
                        Log.e(TAG, " Error FULL " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FarmFullDetails> call, Throwable t) {
                Log.e(TAG, "Failure FULL " + t.toString());
                DataHandler.newInstance().setFarmFullDetails(null);
            }
        });


    }

    private DDNew getOperatorIndex(String id, ArrayList<DDNew> list) {
        int index = 0;
        if (AppConstants.isValidString(id) && !id.trim().equals("0") && list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Log.d(TAG, "Offline Farmmm2 id " + list.get(i).getId());
                if (list.get(i).getId() != null && list.get(i).getId().equalsIgnoreCase(id)) {
                    index = i;
                    break;
                }
            }
            Log.e(TAG, "3 INDEX " + index);
            return list.get(index);
        } else {
            Log.d(TAG, "Offline Farmmm00 Else " + id + ", " + new Gson().toJson(list));
            return null;
        }
    }

    private DDNew getOperatorIndexByName(String id, ArrayList<DDNew> list) {
        int index = 0;
        if (AppConstants.isValidString(id) && !id.trim().equals("0") && list.size() > 0) {
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Log.e(TAG, "INDEX 3 DATA " + new Gson().toJson(list.get(i)));
                    if (id != null && list.get(i).getParameters() != null && list.get(i).getParameters().equalsIgnoreCase(id)) {
                        index = i;
                        break;
                    }
                }
                Log.e(TAG, "3 INDEX " + index);
                return list.get(index);
            } else return null;
        } else return null;
    }

    private DDNew getOperatorIndex(String id, List<DDNew> list) {
        int index = 0;
        if (AppConstants.isValidString(id) && !id.trim().equals("0") && list.size() > 0) {
            if (list != null)
                for (int i = 0; i < list.size(); i++) {
                    if (id != null && list.get(i).getId() != null && list.get(i).getId().equalsIgnoreCase(id)) {
                        index = i;
                        break;
                    }
                }
            Log.e(TAG, "1 INDEX " + index);
            return list.get(index);
        } else return null;
    }

    private DDNew getOperatorIndexByName(String id, List<DDNew> list) {
        int index = 0;
        if (AppConstants.isValidString(id) && !id.trim().equals("0") && !id.trim().equalsIgnoreCase("N/A") && list.size() > 0) {
            if (list != null)
                for (int i = 0; i < list.size(); i++) {
                    if (id != null && list.get(i).getId() != null && list.get(i).getParameters().equalsIgnoreCase(id)) {
                        index = i;
                        break;
                    }
                }
            Log.e(TAG, "1 INDEX " + index);
            return list.get(index);
        } else return null;
    }

    private DDNew getOperatorIndexByValue(String id, List<DDNew> list) {
        int index = 0;
        if (AppConstants.isValidString(id) && !id.trim().equals("0") && list.size() > 0) {
            if (list != null)
                for (int i = 0; i < list.size(); i++) {
                    Log.e(TAG, "size " + list.size() + " " + i + " " + new Gson().toJson(list.get(i)));
                    if (id != null && list.get(i).getParameters() != null && list.get(i).getParameters().equalsIgnoreCase(id)) {
                        index = i;
                        break;
                    }
                }
            Log.e(TAG, "4 INDEX " + index);
            return list.get(index);
        } else return null;
    }

    private Season getOperatorIndex(String id) {
        int index = 0;
        if (AppConstants.isValidString(id) && !id.trim().equals("0") && seasonDDList.size() > 0) {
            if (seasonDDList != null)
                for (int i = 0; i < seasonDDList.size(); i++) {
                    if (id != null && seasonDDList.get(i).getSeasonNum() != null && seasonDDList.get(i).getSeasonNum().equalsIgnoreCase(id)) {
                        index = i;
                        break;
                    }
                }
            Log.e(TAG, "2 INDEX " + index);
            return seasonDDList.get(index);
        } else return null;
    }

    String ownerId = null;
    String addedBy = null;
    String clusterId = null;

    CountryDatum countryDatumPerson = null;
    StateDatum stateDatumPerson = null;
    CityDatum cityDatumPerson = null;
    String personMandal = null;
    String personVillage = null;

    private void setPreviousData(FarmFullDetails fullDetails) {
        if (farmerAndFarmData == null)
            farmerAndFarmData = new FarmerAndFarmData();
        if (fullDetails.getPersonDetails() != null) {
            PersonDetails details = fullDetails.getPersonDetails();

            String profileImage = details.getImgLink();
            String addressProof = details.getAddressProof();
            String idProof = details.getIdProof();
            Glide.with(context).load(profileImage).into
                    (sv_profile_photo);
            Glide.with(context).load(addressProof).into
                    (addressIdCircularImage);
            Glide.with(context).load(idProof).into
                    (idCircularImg);

            Log.e(TAG, "PERSONDETAILS " + new Gson().toJson(details));
            if (AppConstants.isValidString(details.getName()) && !details.getName().equals("0"))
                farmerNameEt.setText(details.getName());

            if (AppConstants.isValidString(details.getMob()) && !details.getMob().equals("0")) {
                phoneNumberEt.setText(details.getMob());
                oldMobile = details.getMob();
                farmerAndFarmData.setMob(oldMobile);
                countryCodePicker.setFullNumber(details.getCountryCode()+oldMobile);
            }

            if (AppConstants.isValidString(details.getFinancialStatus()) && !details.getFinancialStatus().equals("0")) {
                if (details.getFinancialStatus().equalsIgnoreCase("bpl")) {
                    bplRadio.setChecked(true);
                } else if (details.getFinancialStatus().equalsIgnoreCase("apl")) {
                    aplRadio.setChecked(true);
                }
                farmerAndFarmData.setFinancial_status(details.getFinancialStatus());
            }
            farmerAndFarmData.setAdded_by(details.getAddedBy() + "");
            farmerAndFarmData.setClusterId(details.getClusterId() + "");
            if (AppConstants.isValidString(details.getLiteracyStatus()) && !details.getLiteracyStatus().equals("0")) {
                if (details.getLiteracyStatus().equalsIgnoreCase("L")) {
                    literateRadio.setChecked(true);
                } else if (details.getLiteracyStatus().equalsIgnoreCase("I")) {
                    illiterateRadio.setChecked(true);
                }
            }

            phoneNumberEt.clearFocus();
            try {
                if (AppConstants.isValidString(details.getMobileOperator()))
                    networkSpinner.setSelectedItem(getOperatorIndex(details.getMobileOperator(), mobileOperatorList));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (AppConstants.isValidString(details.getCivilStatus()))
                    civil_status_spinner.setSelectedItem(getOperatorIndexByName(details.getCivilStatus(), civilStatusList));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (AppConstants.isValidString(details.getReligion()))
                    religionSpinner.setSelectedItem(getOperatorIndex(details.getReligion(), religionList));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (AppConstants.isValidString(details.getCasteCategory()))
                    casteCategoryspinner.setSelectedItem(getOperatorIndex(details.getCasteCategory(), casteCategoryList));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (AppConstants.isValidString(details.getCaste()))
                    casteSpinner.setSelectedItem(getOperatorIndex(details.getCaste(), casteList));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (AppConstants.isValidString(details.getMia()))
                    microIrriAwarenessSpinner.setSelectedItem(getOperatorIndex(details.getMia(), microIrriAwarenessList));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (AppConstants.isValidString(details.getFatherName()) && !details.getFatherName().equals("0"))
                fatherNameEt.setText(details.getFatherName());
            if (AppConstants.isValidString(details.getAadhaar()) && !details.getAadhaar().equals("0"))
                adhaarEt.setText(details.getAadhaar());
            if (AppConstants.isValidString(details.getPan()) && !details.getPan().equals("0"))
                ti_et_pan.setText(details.getPan());

            if (AppConstants.isValidString(details.getDob()) && !details.getDob().equals("0") && !details.getDob().equals(for_date)) {
                dobEt.setText(AppConstants.getShowableDate(details.getDob(), context));
                dobEtAge.setText(AppConstants.getShowableDate(details.getDob(), context));

            } else if (AppConstants.isValidString(details.getYob())) {
//                dobEt.setText(AppConstants.getShowableDate(details.getDob(), context));
                dobEtAge.setText("" + details.getYob());
            }
            String gender = details.getGender();
            String is_using_smartPhone = details.getIsUsingSphone();
            if (gender != null) {
                if (gender.equalsIgnoreCase("M")) {
                    maleRadio.setChecked(true);
                } else if (gender.equalsIgnoreCase("F")) {
                    femaleRadio.setChecked(true);
                } else {
                    otherRadio.setChecked(true);
                }
            }
            if (is_using_smartPhone != null && is_using_smartPhone.equals("Y")) {
                radioSPhoneYes.setChecked(true);
            } else {
                radioSPhoneNo.setChecked(true);
            }
            if (AppConstants.isValidString(details.getSpouseName()) && !details.getSpouseName().equals("0"))
                etSpouse.setText(details.getSpouseName());

            if (AppConstants.isValidString(details.getSpouseDob()) && !details.getSpouseDob().equals("0")
                    && !details.getSpouseDob().equals(AppConstants.for_date)) {
                etSpouseDob.setText(AppConstants.getShowableDate(details.getSpouseDob(), context));
                etSpouseAge.setText("" + getAge(details.getSpouseDob()));
            }

//            if (AppConstants.isValidString(details.getName())&&!details.getName().equals("0"))
            familyCount.setText("" + details.getFamilyMemCount());
//            if (AppConstants.isValidString(details.getName())&&!details.getName().equals("0"))
            adultCount.setText("" + details.getAdultsCount());
//            if (AppConstants.isValidString(details.getName())&&!details.getName().equals("0"))
            kidsCount.setText("" + details.getKidsCount());
//            if (AppConstants.isValidString(details.getName())&&!details.getName().equals("0"))
            dependentCount.setText("" + details.getDependentCount());
            if (AppConstants.isValidString(details.getEmail()) && !details.getEmail().equals("0"))
                emailEt.setText("" + details.getEmail());
            if (AppConstants.isValidString(details.getMob2()) && !details.getMob2().equals("0"))
                ti_et_phone_address.setText("" + details.getMob2());
            if (AppConstants.isValidString(details.getBeneficiaryName()) && !details.getBeneficiaryName().equals("0"))
                beneficiaryEt.setText("" + details.getBeneficiaryName());
            if (AppConstants.isValidString(details.getEstIncome()) && !details.getEstIncome().equals("0"))
                ti_et_income.setText("" + details.getEstIncome());

            String isShareHolder = null;
            if (isShareHolder != null && isShareHolder.equalsIgnoreCase("y")) {
                radioIsShareYes.setChecked(true);
                Share share = null;
                if (share != null) {
                    if (share.getCertificate() != null && share.getCertificate().equalsIgnoreCase("Y")) {
                        radioShareCertYes.setChecked(true);
                        ti_et_cert_count.setText(share.getHowManyCert());
                        ti_et_cert_value.setText(share.getHowMuchValue());
                    } else {
                        radioIsShareNo.setChecked(true);
                    }
                }
            } else {
                radioIsShareNo.setChecked(true);
            }
        }
        BankDetails bankDetails = fullDetails.getBankDetails();
        if (bankDetails != null) {
            if (AppConstants.isValidString(bankDetails.getIfsc()) && !bankDetails.getIfsc().equals("0"))
                ifscCodeEt.setText(bankDetails.getIfsc());
            if (AppConstants.isValidString(bankDetails.getBankName()) && !bankDetails.getBankName().equals("0"))
                bankNameEt.setText(bankDetails.getBankName());
            if (AppConstants.isValidString(bankDetails.getAccountNo()) && !bankDetails.getAccountNo().equals("0"))
                accountEt.setText(bankDetails.getAccountNo());
            if (AppConstants.isValidString(bankDetails.getAccountName()) && !bankDetails.getAccountName().equals("0"))
                accountHolderName.setText(bankDetails.getAccountName());
            if (AppConstants.isValidString(bankDetails.getBranch()) && !bankDetails.getBranch().equals("0"))
                branchEt.setText(bankDetails.getBranch());
            if (AppConstants.isValidString(bankDetails.getUpiId()) && !bankDetails.getUpiId().equals("0"))
                etUpi.setText(bankDetails.getUpiId());
            if (AppConstants.isValidString(bankDetails.getSwiftCode()) && !bankDetails.getSwiftCode().equals("0"))
                etSwiftCode.setText(bankDetails.getSwiftCode());
        }
        PersonAddress personAddress = fullDetails.getPersonAddress();
        if (personAddress != null) {
            Log.e(TAG, "PERSON_ADDRESS " + new Gson().toJson(personAddress));
            if (AppConstants.isValidString(personAddress.getMandalOrTehsil()) && !personAddress.getMandalOrTehsil().equals("0")) {
                mandalOrTehsilEt.setText(personAddress.getMandalOrTehsil());
                personMandal = personAddress.getMandalOrTehsil();
            }
            if (AppConstants.isValidString(personAddress.getVillageOrCity()) && !personAddress.getVillageOrCity().equals("0")) {
                villageOrCityEt.setText(personAddress.getVillageOrCity());
                personVillage = personAddress.getVillageOrCity();
            }
            if (AppConstants.isValidString(personAddress.getAddL2()) && !personAddress.getAddL2().equals("0"))
                etAddressLine2.setText(personAddress.getAddL2());
            if (AppConstants.isValidString(personAddress.getAddL1()) && !personAddress.getAddL1().equals("0"))
                etAddressLine1.setText(personAddress.getAddL1());

            if (!AppConstants.isValidString(personAddress.getCountry()))
                personAddress.setCountry("India");

            if (AppConstants.isValidString(personAddress.getCountry())) {

                for (int i = 0; i < countryDatumList.size(); i++) {
                    if (personAddress.getCountry().trim().equalsIgnoreCase(countryDatumList.get(i).getName().trim())) {
                        countryDatumPerson = countryDatumList.get(i);
                        break;
                    }
                }
                if (countryDatumPerson != null) {
                    try {
                        countrySpinner.setSelectedItem(countryDatumPerson);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (AppConstants.isValidString(personAddress.getState()) && countryDatumPerson.getStateDatumList() != null &&
                            countryDatumPerson.getStateDatumList().size() > 0) {

                        for (int i = 0; i < countryDatumPerson.getStateDatumList().size(); i++) {
                            if (personAddress.getState().trim().equals(countryDatumPerson.getStateDatumList().get(i).getName().trim())) {
                                stateDatumPerson = countryDatumPerson.getStateDatumList().get(i);
                                break;
                            }
                        }
                        if (stateDatumPerson != null) {
                            try {
                                stateSpinner.setSelectedItem(stateDatumPerson);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            for (int i = 0; i < stateDatumPerson.getCities().size(); i++) {
                                if (personAddress.getDistrict().trim().equals(stateDatumPerson.getCities().get(i).getName().trim())) {
                                    cityDatumPerson = stateDatumPerson.getCities().get(i);
                                    break;
                                }
                            }
                            if (cityDatumPerson != null) {
                                try {
                                    districtSpinner.setSelectedItem(cityDatumPerson);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                    personAddress.getDistrict();

                }
            }
        }


        FarmData farmData = new FarmData();
        if (fullDetails != null && fullDetails.getFarmsDetails() != null) {
            FarmsDetails farmsDetails = fullDetails.getFarmsDetails();
            farmAddedBy = farmsDetails.getAddedBy() + "";
            farmCompId = farmsDetails.getCompFarmId() + "";
            farmClusterId = farmsDetails.getClusterId() + "";
            farmData.setId(farmsDetails.getCompFarmId());
            farmData.setFarmId(farmsDetails.getFarmMasterNum() + "");
            farmData.setName(farmsDetails.getFarmName());
            farmData.setOwnerId(farmsDetails.getOwnerId() + "");
            ownerId = farmsDetails.getOwnerId() + "";
            clusterId = "" + farmsDetails.getClusterId();
            addedBy = "" + farmsDetails.getAddedBy();
            farmData.setCluster(farmsDetails.getClusterId() + "");
            farmData.setSeason(farmsDetails.getSeasonNum() + "");
            farmData.setLat_cord(farmsDetails.getLatCord() + "");
            farmData.setLong_cord(farmsDetails.getLongCord() + "");
            farmData.setCluster(farmsDetails.getClusterId() + "");
            farmData.setAddedBy(farmsDetails.getAddedBy() + "");
            farmData.setIsFree(farmsDetails.getIsFree() + "");

            farmData.setDelqReason(farmsDetails.getDelqReason() + "");
            farmData.setSvId(farmsDetails.getSvId() + "");
//            farmData.setDelqReason(farmsDetails.getIsFree() + "");

            farmData.setZoom(farmsDetails.getZoom() + "");
            farmData.setMotorHp(farmsDetails.getMotorHp());
            farmData.setSelectedSeason(getOperatorIndex(farmsDetails.getSeasonNum() + ""));
            farmData.setSelectedMotorHP(getOperatorIndex(farmsDetails.getMotorHp() + "", motorHpList));


        }
        if (fullDetails != null && fullDetails.getFarmAddressDetails() != null) {
            FarmAddressDetails farmAddressDetails = fullDetails.getFarmAddressDetails();
            Log.e(TAG, "FARM_AADDRESS " + new Gson().toJson(farmAddressDetails));
            farmData.setCountry(farmAddressDetails.getCountry() + "");
            farmData.setCountryId(farmAddressDetails.getCountry());
            farmData.setState(farmAddressDetails.getState());
            farmData.setStateId(farmAddressDetails.getState());
            farmData.setDistrict(farmAddressDetails.getDistrict());
            farmData.setDistrictId(farmAddressDetails.getDistrict());
            farmData.setMandalOrTehsil(farmAddressDetails.getMandalOrTehsil());
            if (AppConstants.isValidString(farmAddressDetails.getMandalOrTehsil()) && !AppConstants.isValidString(personMandal)) {
                mandalOrTehsilEt.setText(farmAddressDetails.getMandalOrTehsil());
            }
            farmData.setVillageOrCity(farmAddressDetails.getVillageOrCity());
            if (AppConstants.isValidString(farmAddressDetails.getVillageOrCity()) && !AppConstants.isValidString(personVillage)) {
                villageOrCityEt.setText(farmAddressDetails.getVillageOrCity());
            }
            farmData.setAddL1(farmAddressDetails.getAddL1());
            farmData.setAddL2(farmAddressDetails.getAddL2());
            if (!AppConstants.isValidString(farmAddressDetails.getCountry()))
                farmAddressDetails.setCountry("India");
            if (AppConstants.isValidString(farmAddressDetails.getCountry())) {
                CountryDatum countryDatum = null;
                for (int i = 0; i < countryDatumList.size(); i++) {
                    if (farmAddressDetails.getCountry().trim().equals(countryDatumList.get(i).getName().trim())) {
                        countryDatum = countryDatumList.get(i);
                        break;
                    }
                }

                if (countryDatum != null) {
                    try {
                        farmData.setSelectedCountry(countryDatum);
                        if (countryDatum.getStateDatumList() != null && countryDatum.getStateDatumList().size() > 0) {
                            if (farmData.getStateDatumList() != null) {
                                farmData.getStateDatumList().clear();
                            } else
                                farmData.setStateDatumList(new ArrayList<>());
                            farmData.getStateDatumList().addAll(countryDatum.getStateDatumList());
                        }
                        if (countryDatumPerson == null) {
                            countryDatumPerson = countryDatum;
                            countrySpinner.setSelectedItem(countryDatumPerson);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (AppConstants.isValidString(farmAddressDetails.getState()) && countryDatum.getStateDatumList() != null && countryDatum.getStateDatumList().size() > 0) {
                        StateDatum stateDatum = null;
                        for (int i = 0; i < countryDatum.getStateDatumList().size(); i++) {
                            if (farmAddressDetails.getState().trim().equals(countryDatum.getStateDatumList().get(i).getName().trim())) {
                                stateDatum = countryDatum.getStateDatumList().get(i);
                                break;
                            }
                        }
                        if (stateDatum != null) {
                            try {
                                farmData.setSelectedState(stateDatum);
                                if (stateDatum.getCities() != null && stateDatum.getCities().size() > 0) {
                                    if (farmData.getCityDatumList() != null) {
                                        farmData.getCityDatumList().clear();
                                    } else farmData.setCityDatumList(new ArrayList<>());
                                    farmData.getCityDatumList().addAll(stateDatum.getCities());
                                }
                                if (stateDatumPerson == null) {
                                    stateDatumPerson = stateDatum;
                                    stateSpinner.setSelectedItem(stateDatumPerson);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CityDatum cityDatum = null;
                            for (int i = 0; i < stateDatum.getCities().size(); i++) {
                                if (farmAddressDetails.getDistrict().trim().equals(stateDatum.getCities().get(i).getName().trim())) {

                                    cityDatum = stateDatum.getCities().get(i);
                                    break;
                                }
                            }
                            if (cityDatum != null) {
                                try {
                                    farmData.setSelectedDistrict(cityDatum);
                                    if (farmData.getCityDatumList() != null) {
                                        farmData.getCityDatumList().clear();
                                        farmData.getCityDatumList().addAll(stateDatum.getCities());
                                    } else {
                                        farmData.setCityDatumList(new ArrayList<>());
                                        farmData.getCityDatumList().addAll(stateDatum.getCities());
                                    }
                                    if (cityDatumPerson == null) {
                                        cityDatumPerson = cityDatum;
                                        districtSpinner.setSelectedItem(cityDatumPerson);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }


                }
            }

        }


        if (fullDetails != null && fullDetails.getFarmCropDetails() != null) {
            FarmCropDetails farmCropDetails = fullDetails.getFarmCropDetails();
            Log.e(TAG, "CROPDETAILS " + new Gson().toJson(farmCropDetails));
            farmData.setCropId(farmCropDetails.getCropId() + "");
            farmData.setArea(farmCropDetails.getExpArea() + "");
            farmData.setAreaS(farmCropDetails.getExpArea() + "");
            farmData.setCurrentCropName(farmCropDetails.getCropName());
            farmData.setCropping_pattern(farmCropDetails.getCroppingPattern());
            farmData.setIs_irrigated(farmCropDetails.getIsIrrigated());
            farmData.setIrrigationsource(farmCropDetails.getIrrigationSourceId() + "");
            farmData.setIrrigationtype(farmCropDetails.getIrrigationTypeId() + "");
            farmData.setPreviouscrop(farmCropDetails.getPreviousCrop());
            farmData.setSoiltype(farmCropDetails.getSoilTypeId() + "");
            farmData.setPlanting_method(farmCropDetails.getPlantingMethod());
            farmData.setTransplant_date(farmCropDetails.getTransplantDate());

            farmData.setSelectedCrop(getOperatorIndexByName(farmCropDetails.getCropName() + "", farmCropList));
            farmData.setSelectedPrevCrop(getOperatorIndex(farmCropDetails.getPreviousCrop() + "", farmCropList));
            farmData.setSelectedIrriSource(getOperatorIndex(farmCropDetails.getIrrigationSourceId() + "", irrigationSourceDDList));
            farmData.setSelectedIrriType(getOperatorIndex(farmCropDetails.getIrrigationTypeId() + "", irrigationTypeDDList));
            farmData.setSelectedSoilType(getOperatorIndex(farmCropDetails.getSoilTypeId() + "", soilTypeDDList));
            farmData.setSelectedCroppingPattern(getOperatorIndexByValue(farmCropDetails.getCroppingPattern() + "", croppingPatternDDList));
            farmData.setSelectedPlantingMethod(getOperatorIndexByValue(farmCropDetails.getPlantingMethod() + "", plantingMethodDDList));
            farmData.setSelectedLandCategory(getOperatorIndexByValue(farmCropDetails.getIsIrrigated() + "", isIrrigatedDDList));
        }
        farmData.setFpoCropList(new ArrayList<>());
        List<DataCropDetails> cropDetailsList1 = new ArrayList<>();
        Map<DataCropDetails, List<DataCropDetails>> info = new HashMap<>();
        DataCropDetails cropFormDatum1 = new DataCropDetails();
        cropFormDatum1.setCropId(1);
        cropFormDatum1.setFarmId(11);
        DataCropDetails cropFormDatum2 = new DataCropDetails();
        cropFormDatum2.setCropId(1);
        cropFormDatum2.setFarmId(11);
        info.put(cropFormDatum1, cropDetailsList1);
        if (info.containsKey(cropFormDatum2)) {
            Toast.makeText(context, "Contains", Toast.LENGTH_LONG).show();
        } else {
//            Toast.makeText(context, "Contains not", Toast.LENGTH_LONG).show();
        }


        List<PrevCropDatum> cropDetailsList = fullDetails.getPrevCropDatumList();
        if (cropDetailsList != null && cropDetailsList.size() > 0) {
            if (cropFormDatumList == null || cropFormDatumList.size() == 0) {
                try {
                    Map<PrevCropDatum, Map<Integer, List<DataCropDetails>>> map = new HashMap<>();
                    for (int i = 0; i < cropDetailsList.size(); i++) {
                        List<List<CropFormDatum>> cropDatumLists = new ArrayList<>();
                        PrevCropDatum details = cropDetailsList.get(i);
                        try {
                            Map<Integer, List<DataCropDetails>> mapInner = new HashMap<>();
                            for (int s = 0; s < details.getData().size(); s++) {
                                DataCropDetails dataCropDetails = details.getData().get(s);

                                if (mapInner.containsKey(dataCropDetails.getFarmAddInfoTypeId())) {
                                    mapInner.get(dataCropDetails.getFarmAddInfoTypeId()).add(dataCropDetails);
                                } else {
                                    List<DataCropDetails> dataCropDetailsList = new ArrayList<>();
                                    dataCropDetailsList.add(dataCropDetails);
                                    mapInner.put(dataCropDetails.getFarmAddInfoTypeId(), dataCropDetailsList);
                                }
                            }
                            map.put(details, mapInner);
                        } catch (Exception e) {
                        }
                    }
                    List<FPOCrop> fpoCropList = new ArrayList<>();
                    for (PrevCropDatum id : map.keySet()) {
                        List<List<CropFormDatum>> cropDatumLists = new ArrayList<>();
                        Map<Integer, List<DataCropDetails>> detailsList = map.get(id);
                        for (Integer innerId : detailsList.keySet()) {
                            List<CropFormDatum> list = new ArrayList<>();
                            List<DataCropDetails> dataCropDetails = detailsList.get(innerId);
                            for (int i = 0; i < dataCropDetails.size(); i++) {

                                Gson gson = new GsonBuilder()
                                        .setLenient()
                                        .create();

                                JsonObject jsonObject = gson.fromJson(dataCropDetails.get(i).getFarmAddInfoJson(), JsonObject.class);
                                CropFormDatum datum = new CropFormDatum();
                                datum.setType("");
                                int k = 1;
                                for (String innerKey : jsonObject.keySet()) {
                                    if (AppConstants.isValidString(innerKey)) {
                                        if (k == 1) {
                                            datum.setCol1(innerKey);
                                            datum.setCol1Value(jsonObject.get(innerKey).getAsString());
                                        } else if (k == 2) {
                                            datum.setCol2(innerKey);
                                            datum.setCol2Value(jsonObject.get(innerKey).getAsString());
                                        } else if (k == 3) {
                                            datum.setCol3(innerKey);
                                            datum.setCol3Value(jsonObject.get(innerKey).getAsString());
                                        } else if (k == 4) {
                                            datum.setCol4(innerKey);
                                            datum.setCol4Value(jsonObject.get(innerKey).getAsString());
                                        } else if (k == 5) {
                                            datum.setCol5(innerKey);
                                            datum.setCol5Value(jsonObject.get(innerKey).getAsString());
                                        } else if (k == 6) {
                                            datum.setCol6(innerKey);
                                            datum.setCol6Value(jsonObject.get(innerKey).getAsString());
                                        }
                                    }
                                    k++;
                                }
                                list.add(datum);
                            }


                            cropDatumLists.add(list);
                        }

                        FPOCrop fpoCrop = new FPOCrop(new FormModel());
                        fpoCrop.setCropFormDatumArrayLists(cropDatumLists);
                        fpoCrop.setFarmArea(id.getFarmArea() + "");
                        fpoCrop.setSeasonId(id.getSeasonNum() + "");
                        fpoCrop.setCropId(id.getCropId() + "");
                        fpoCropList.add(fpoCrop);

                    }
                    farmData.setFpoCropList(fpoCropList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Map<PrevCropDatum, Map<Integer, List<DataCropDetails>>> map = new HashMap<>();
                    for (int i = 0; i < cropDetailsList.size(); i++) {
                        List<List<CropFormDatum>> cropDatumLists = new ArrayList<>();
                        PrevCropDatum details = cropDetailsList.get(i);
                        try {
                            Map<Integer, List<DataCropDetails>> mapInner = new HashMap<>();
                            for (int s = 0; s < details.getData().size(); s++) {
                                DataCropDetails dataCropDetails = details.getData().get(s);

                                if (mapInner.containsKey(dataCropDetails.getFarmAddInfoTypeId())) {
                                    mapInner.get(dataCropDetails.getFarmAddInfoTypeId()).add(dataCropDetails);
                                } else {
                                    List<DataCropDetails> dataCropDetailsList = new ArrayList<>();
                                    dataCropDetailsList.add(dataCropDetails);
                                    mapInner.put(dataCropDetails.getFarmAddInfoTypeId(), dataCropDetailsList);
                                }
                            }
                            map.put(details, mapInner);


                        } catch (Exception e) {

                        }
                    }


                    List<FPOCrop> fpoCropList = new ArrayList<>();
                    for (PrevCropDatum id : map.keySet()) {
                        List<List<CropFormDatum>> cropDatumLists = new ArrayList<>();
                        Map<Integer, List<DataCropDetails>> detailsList = map.get(id);
                        for (Integer innerId : detailsList.keySet()) {
                            List<CropFormDatum> list = new ArrayList<>();
                            List<DataCropDetails> dataCropDetails = detailsList.get(innerId);
                            for (int i = 0; i < dataCropDetails.size(); i++) {
                                Gson gson = new GsonBuilder()
                                        .setLenient()
                                        .create();

                                JsonObject jsonObject = gson.fromJson(dataCropDetails.get(i).getFarmAddInfoJson(), JsonObject.class);
                                CropFormDatum datum = null;
                                CropFormDatum datumOld = null;
                                datumOld = cropFormDatumListMap.get("" + dataCropDetails.get(i).getFarmAddInfoTypeId());
                                if (datum == null)
                                    datum = new CropFormDatum();

                                if (datumOld != null) {
                                    datum.setCol1(datumOld.getCol1());
                                    datum.setCol2(datumOld.getCol2());
                                    datum.setCol3(datumOld.getCol3());
                                    datum.setCol4(datumOld.getCol4());
                                    datum.setCol5(datumOld.getCol5());
                                    datum.setCol6(datumOld.getCol6());
                                    datum.setInfoTypeId(datumOld.getInfoTypeId());
                                    datum.setSuperType(datumOld.getSuperType());
                                    datum.setType(datumOld.getType());
//                                    datum.setCol1(datumOld.getCol1());
                                }
                                Log.e(TAG, "PRINTINGGGG" + new Gson().toJson(datum));
//                            datum.setType("");
                                for (String innerKey : jsonObject.keySet()) {
                                    if (AppConstants.isValidString(innerKey)) {
                                        if (AppConstants.isValidString(datum.getCol1()) && innerKey.equals(datum.getCol1())) {
                                            datum.setCol1Value(jsonObject.get(innerKey).getAsString());
                                        } else if (AppConstants.isValidString(datum.getCol2()) && innerKey.equals(datum.getCol2())) {
                                            datum.setCol2Value(jsonObject.get(innerKey).getAsString());
                                        } else if (AppConstants.isValidString(datum.getCol3()) && innerKey.equals(datum.getCol3())) {
                                            datum.setCol3Value(jsonObject.get(innerKey).getAsString());
                                        } else if (AppConstants.isValidString(datum.getCol4()) && innerKey.equals(datum.getCol4())) {
                                            datum.setCol4Value(jsonObject.get(innerKey).getAsString());
                                        } else if (AppConstants.isValidString(datum.getCol5()) && innerKey.equals(datum.getCol5())) {
                                            datum.setCol5Value(jsonObject.get(innerKey).getAsString());
                                        } else if (AppConstants.isValidString(datum.getCol6()) && innerKey.equals(datum.getCol6())) {
                                            datum.setCol6Value(jsonObject.get(innerKey).getAsString());
                                        }
                                    }
                                }
                                list.add(datum);
                            }
                            cropDatumLists.add(list);
                        }
                        FPOCrop fpoCrop = new FPOCrop(new FormModel());
                        fpoCrop.setCropFormDatumArrayLists(cropDatumLists);
                        fpoCrop.setFarmArea(id.getFarmArea() + "");
                        fpoCrop.setSeasonId(id.getSeasonNum() + "");
                        fpoCrop.setSelectedCrop(getOperatorIndex(id.getCropId() + "", farmCropList));
                        fpoCrop.setCropId(id.getCropId() + "");
                        fpoCropList.add(fpoCrop);

                    }
                    farmData.setFpoCropList(fpoCropList);
                } catch (Exception e) {

                }
            }
        } else {
            List<FPOCrop> fpoCropList = new ArrayList<>();
            FPOCrop fpoCrop = new FPOCrop(new FormModel());
            fpoCrop.setCropFormDatumArrayLists(getListData());
            farmData.setFpoCropList(fpoCropList);
        }

//=======================XXXX=================================
        List<AssetsData> assetsDataList = fullDetails.getAssetsDataList();
        farmData.setAssetsList(new ArrayList<>());
        if (assetsDataList != null && assetsDataList.size() > 0) {
            if (cropFormDatumListSuper == null || cropFormDatumListSuper.size() == 0) {
                List<List<CropFormDatum>> assetListForm = new ArrayList<>();
                Map<String, List<CropFormDatum>> stringListMap = new HashMap<>();
                Map<String, List<AssetsData>> assetsMap = new HashMap<>();
                for (int i = 0; i < assetsDataList.size(); i++) {
                    AssetsData data = assetsDataList.get(i);
                    if (assetsMap.containsKey(data.getAssetType())) {
                        assetsMap.get(data.getAssetType()).add(data);
                    } else {
                        List<AssetsData> list = new ArrayList<>();
                        list.add(data);
                        assetsMap.put(data.getAssetType(), list);
                    }
                }
                Log.e(TAG, "ASSETS " + new Gson().toJson(assetsMap));

                for (String keyss : assetsMap.keySet()) {

                    List<AssetsData> assetsDataListt = assetsMap.get(keyss);
                    List<CropFormDatum> cropFormDatumList = new ArrayList<>();
                    for (int i = 0; i < assetsDataListt.size(); i++) {
                        try {
                            AssetsData data = assetsDataListt.get(i);
                            Log.e(TAG, "PRINTING0 " + i + " " + new Gson().toJson(data));
                            CropFormDatum datum = null;
                            {
                                datum = new CropFormDatum();
                            }
                            Gson gson = new GsonBuilder()
                                    .setLenient()
                                    .create();
                            datum.setSuperType(data.getAssetType());
                            Log.e(TAG, "PRINTING1 " + i + " " + data.getJson());
                            JsonObject jsonObject = gson.fromJson(data.getJson(), JsonObject.class);
                            Log.e(TAG, "PRINTING " + i + " " + new Gson().toJson(jsonObject));
                            int s = 1;
                            for (String key : jsonObject.keySet()) {
                                if (s == 1) {
                                    datum.setCol6Value(jsonObject.get(key).getAsString());
                                    datum.setCol6(key);
                                } else if (s == 1) {
                                    datum.setCol1Value(jsonObject.get(key).getAsString());
                                    datum.setCol1(key);
                                } else if (s == 2) {
                                    datum.setCol2Value(jsonObject.get(key).getAsString());
                                    datum.setCol2(key);
                                } else if (s == 3) {
                                    datum.setCol3Value(jsonObject.get(key).getAsString());
                                    datum.setCol3(key);
                                } else if (s == 4) {
                                    datum.setCol4Value(jsonObject.get(key).getAsString());
                                    datum.setCol4(key);
                                } else if (s == 5) {
                                    datum.setCol5Value(jsonObject.get(key).getAsString());
                                    datum.setCol5(key);
                                }
                                s++;
                            }
                            cropFormDatumList.add(datum);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    assetListForm.add(cropFormDatumList);
                }
                farmData.setAssetsList(assetListForm);

            } else {
                List<List<CropFormDatum>> assetListForm = new ArrayList<>();
                Map<String, List<AssetsData>> assetsMap = new HashMap<>();
                for (int i = 0; i < assetsDataList.size(); i++) {
                    AssetsData data = assetsDataList.get(i);
                    if (assetsMap.containsKey(data.getAssetType())) {
                        assetsMap.get(data.getAssetType()).add(data);
                    } else {
                        List<AssetsData> list = new ArrayList<>();
                        list.add(data);
                        assetsMap.put(data.getAssetType(), list);
                    }
                }
                Log.e(TAG, "ASSETS " + new Gson().toJson(assetsMap));

                for (String keyss : assetsMap.keySet()) {

                    List<AssetsData> assetsDataListt = assetsMap.get(keyss);
                    List<CropFormDatum> cropFormDatumList = new ArrayList<>();
                    for (int i = 0; i < assetsDataListt.size(); i++) {
                        try {
                            AssetsData data = assetsDataListt.get(i);
                            Log.e(TAG, "PRINTING0 " + i + " " + new Gson().toJson(data));
                            CropFormDatum datum = null;
                            CropFormDatum oldDatum = null;
                            if (cropFormDatumListSuperMap.containsKey(data.getAssetType() + "".trim())) {
                                oldDatum = cropFormDatumListSuperMap.get(data.getAssetType());
                                if (oldDatum != null) {
                                    datum = new CropFormDatum();
                                    datum.setCol1(oldDatum.getCol1());
                                    datum.setCol2(oldDatum.getCol2());
                                    datum.setCol3(oldDatum.getCol3());
                                    datum.setCol4(oldDatum.getCol4());
                                    datum.setCol5(oldDatum.getCol5());
                                    datum.setCol6(oldDatum.getCol6());
                                    datum.setInfoTypeId(oldDatum.getInfoTypeId());
                                    datum.setSuperType(oldDatum.getSuperType());
                                    datum.setType(oldDatum.getType());
                                }
                            } else {
                                datum = new CropFormDatum();
                            }
                            Gson gson = new GsonBuilder()
                                    .setLenient()
                                    .create();
                            datum.setSuperType(data.getAssetType());
                            Log.e(TAG, "PRINTING1 " + i + " " + data.getJson());
                            JsonObject jsonObject = gson.fromJson(data.getJson(), JsonObject.class);
                            Log.e(TAG, "PRINTING " + i + " " + new Gson().toJson(jsonObject));
                            for (String key : jsonObject.keySet()) {
                                if (AppConstants.isValidString(datum.getCol6()) && datum.getCol6().equals(key)) {
                                    datum.setCol6Value(jsonObject.get(key).getAsString());
                                } else if (AppConstants.isValidString(datum.getCol1()) && datum.getCol1().equals(key)) {
                                    datum.setCol1Value(jsonObject.get(key).getAsString());

                                } else if (AppConstants.isValidString(datum.getCol2()) && datum.getCol2().equals(key)) {
                                    datum.setCol2Value(jsonObject.get(key).getAsString());

                                } else if (AppConstants.isValidString(datum.getCol3()) && datum.getCol3().equals(key)) {
                                    datum.setCol3Value(jsonObject.get(key).getAsString());

                                } else if (AppConstants.isValidString(datum.getCol4()) && datum.getCol4().equals(key)) {
                                    datum.setCol4Value(jsonObject.get(key).getAsString());

                                } else if (AppConstants.isValidString(datum.getCol5()) && datum.getCol5().equals(key)) {
                                    datum.setCol5Value(jsonObject.get(key).getAsString());

                                }

                            }
                            cropFormDatumList.add(datum);
//                            if (stringListMap.containsKey(datum.getSuperType())) {
//                                stringListMap.get(datum.getSuperType()).add(datum);
//                            } else {
//                                List<CropFormDatum> lst = new ArrayList<>();
//                                lst.add(datum);
//                                stringListMap.put(datum.getSuperType(), lst);
//                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    assetListForm.add(cropFormDatumList);
                }


                /*for (String superType : stringListMap.keySet()) {
                    assetListForm.add(stringListMap.get(superType));
                }*/
                farmData.setAssetsList(assetListForm);

            }
        } else {
            farmData.setAssetsList(getListDataSuper());
        }

//        farmData.setAssetsList(getListDataSuper());
        //=======================XXXX=================================

        /*if (fullDetails.getAssetsDataList() != null) {
            for (int i = 0; i < fullDetails.getAssetsDataList().size(); i++) {
                AssetsData assetsData = fullDetails.getAssetsDataList().get(i);
                farmData.setAssetsList(getListDataSuper(null));
            }

        }*/

        Log.e(TAG, "FARM ADA " + new Gson().toJson(farmData));

        farmList.clear();
        farmList.add(farmData);
        adapter.setEditDisabled(isEditDisabled);
        adapter.notifyDataSetChanged();

    }

    private List<List<CropFormDatum>> getListData() {
        List<List<CropFormDatum>> lists = new ArrayList<>();

        for (int i = 0; i < cropFormDatumList.size(); i++) {

            List<CropFormDatum> list = new ArrayList<>();
            list.add(CropFormDatum.copy(cropFormDatumList.get(i)));
            lists.add(list);
        }
        return lists;
    }

    private List<List<CropFormDatum>> getListDataSuper() {
        List<List<CropFormDatum>> lists = new ArrayList<>();

        for (int i = 0; i < cropFormDatumListSuper.size(); i++) {
            List<CropFormDatum> list = new ArrayList<>();
            list.add(CropFormDatum.copy(cropFormDatumListSuper.get(i)));
            lists.add(list);
        }
        return lists;
    }

    boolean isMotEtTouch = false;

    private void getFullDetailsByMobile(String mob) {
        isMotEtTouch = false;
        JsonObject object = new JsonObject();
        object.addProperty("mob", "" + mob);
        object.addProperty("user_id", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        object.addProperty("token", "" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        Log.e(TAG, "SEND FULL DETAIL PARAM MOB  " + new Gson().toJson(object));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        apiInterface.getDeialsByMob(object).enqueue(new Callback<PersonFullResponse>() {
            @Override
            public void onResponse(Call<PersonFullResponse> call, Response<PersonFullResponse> response1) {
                String error = null;
                Log.e(TAG, "Response FULL MOB CODE" + response1.code());
                if (response1.isSuccessful()) {
                    PersonFullResponse fullResponse = response1.body();
                    Log.e(TAG, "FULL DET BY PHONE " + new Gson().toJson(fullResponse));
                    if (fullResponse.getStatus() == 1 && fullResponse.getPersonFullData() != null) {
                        showDialogPersonDetected(fullResponse.getPersonFullData());
//                        setNewPersonData(fullResponse.getPersonFullData());
                    } else {
                        ownerId = null;
//                        setNewPersonData();
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
                        Log.e(TAG, " Error FULL MOB " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PersonFullResponse> call, Throwable t) {
                Log.e(TAG, "Failure FULL MOB " + t.toString());
            }
        });


    }

    private void setNewPersonData(PersonFullData data) {
        if (data != null) {
            ownerId = data.getPersonId() + "";
            clusterId = "" + data.getClusterId();
//            addedBy=""+farmsDetails.getAddedBy();
            isMotEtTouch = false;
            if (AppConstants.isValidString(data.getName()) && !data.getName().equals("0"))
                farmerNameEt.setText(data.getName());
            else
                farmerNameEt.setText("");
            if (AppConstants.isValidString(data.getMob()) && !data.getMob().equals("0")) {
                phoneNumberEt.setText(data.getMob());
                oldMobile = data.getMob();
                countryCodePicker.setFullNumber(data.getCountryCode()+oldMobile);
            } else
                phoneNumberEt.setText("");
            phoneNumberEt.clearFocus();
            try {
                networkSpinner.setSelectedItem(getOperatorIndex(data.getMobileOperator(), mobileOperatorList));
                civil_status_spinner.setSelectedItem(getOperatorIndexByName(data.getCivilStatus(), civilStatusList));
                religionSpinner.setSelectedItem(getOperatorIndex(data.getReligion(), religionList));
                casteCategoryspinner.setSelectedItem(getOperatorIndex(data.getCasteCategory(), casteCategoryList));
                casteSpinner.setSelectedItem(getOperatorIndex(data.getCaste(), casteList));

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (AppConstants.isValidString(data.getFatherName()) && !data.getFatherName().equals("0"))
                fatherNameEt.setText(data.getFatherName());
            if (AppConstants.isValidString(data.getAadhaar()) && !data.getAadhaar().equals("0"))
                adhaarEt.setText(data.getAadhaar());
            if (AppConstants.isValidString(data.getPan()) && !data.getPan().equals("0"))
                ti_et_pan.setText(data.getPan());

            if (AppConstants.isValidString(data.getDob()) && !data.getDob().equals("0") && !data.getDob().equals(for_date)) {
                dobEt.setText(AppConstants.getShowableDate(data.getDob(), context));
                dobEtAge.setText("" + getAge(data.getDob()));
            }
            String gender = data.getGender();
            String is_using_smartPhone = data.getIsUsingSphone();
            if (gender != null) {
                if (gender.equalsIgnoreCase("M")) {
                    maleRadio.setChecked(true);
                } else if (gender.equalsIgnoreCase("F")) {
                    femaleRadio.setChecked(true);
                } else {
                    otherRadio.setChecked(true);
                }
            }
            if (is_using_smartPhone != null && is_using_smartPhone.equals("Y")) {
                radioSPhoneYes.setChecked(true);
            } else {
                radioSPhoneNo.setChecked(true);
            }
            if (AppConstants.isValidString(data.getSpouseName()) && !data.getSpouseName().equals("0"))
                etSpouse.setText(data.getSpouseName());


            if (AppConstants.isValidString(data.getSpouseDob()) && !data.getSpouseDob().equals("0")
                    && !data.getSpouseDob().equals(AppConstants.for_date)) {
                etSpouseDob.setText(AppConstants.getShowableDate(data.getSpouseDob(), context));
                etSpouseAge.setText("" + getAge(data.getSpouseDob()));
            }

//            if (AppConstants.isValidString(details.getName())&&!details.getName().equals("0"))
            familyCount.setText("" + data.getFamilyMemCount());
//            if (AppConstants.isValidString(details.getName())&&!details.getName().equals("0"))
            adultCount.setText("" + data.getAdultsCount());
//            if (AppConstants.isValidString(details.getName())&&!details.getName().equals("0"))
            kidsCount.setText("" + data.getKidsCount());
//            if (AppConstants.isValidString(details.getName())&&!details.getName().equals("0"))
            dependentCount.setText("" + data.getDependentCount());
            if (AppConstants.isValidString(data.getEmail()) && !data.getEmail().equals("0"))
                emailEt.setText("" + data.getEmail());
            else
                emailEt.setText("");
            if (AppConstants.isValidString(data.getMob2()) && !data.getMob2().equals("0"))
                ti_et_phone_address.setText("" + data.getMob2());
            else
                ti_et_phone_address.setText("");
            if (AppConstants.isValidString(data.getBeneficiaryName()) && !data.getBeneficiaryName().equals("0"))
                beneficiaryEt.setText("" + data.getBeneficiaryName());
            else
                beneficiaryEt.setText("");
            if (AppConstants.isValidString(data.getEstIncome()) && !data.getEstIncome().equals("0"))
                ti_et_income.setText("" + data.getEstIncome());
            else
                ti_et_income.setText("");

            String isShareHolder = null;
            if (isShareHolder != null && isShareHolder.equalsIgnoreCase("y")) {
                radioIsShareYes.setChecked(true);
                Share share = null;
                if (share != null) {
                    if (share.getCertificate() != null && share.getCertificate().equalsIgnoreCase("Y")) {
                        radioShareCertYes.setChecked(true);
                        ti_et_cert_count.setText(share.getHowManyCert());
                        ti_et_cert_value.setText(share.getHowMuchValue());
                    } else {
                        radioIsShareNo.setChecked(true);
                    }
                }
            } else {
                radioIsShareNo.setChecked(true);
            }
        }
        if (AppConstants.isValidString(data.getIfsc()) && !data.getIfsc().equals("0"))
            ifscCodeEt.setText(data.getIfsc());
        else
            ifscCodeEt.setText("");
        if (AppConstants.isValidString(data.getBankName()) && !data.getBankName().equals("0"))
            bankNameEt.setText(data.getBankName());
        else
            bankNameEt.setText("");
        if (AppConstants.isValidString(data.getAccountNo()) && !data.getAccountNo().equals("0"))
            accountEt.setText(data.getAccountNo());
        else
            accountEt.setText("");
        if (AppConstants.isValidString(data.getAccountName()) && !data.getAccountName().equals("0"))
            accountHolderName.setText(data.getAccountName());
        else
            accountHolderName.setText("");
        if (AppConstants.isValidString(data.getBranch()) && !data.getBranch().equals("0"))
            branchEt.setText(data.getBranch());
        else
            branchEt.setText("");
        if (AppConstants.isValidString(data.getUpiId()) && !data.getUpiId().equals("0"))
            etUpi.setText(data.getUpiId());
        else
            etUpi.setText("");
        if (AppConstants.isValidString(data.getSwiftCode()) && !data.getSwiftCode().equals("0"))
            etSwiftCode.setText(data.getSwiftCode());
        else
            etSwiftCode.setText("");
        if (AppConstants.isValidString(data.getMandalOrTehsil()) && !data.getMandalOrTehsil().equals("0"))
            mandalOrTehsilEt.setText(data.getMandalOrTehsil());
        else
            mandalOrTehsilEt.setText("");
        if (AppConstants.isValidString(data.getVillageOrCity()) && !data.getVillageOrCity().equals("0"))
            villageOrCityEt.setText(data.getVillageOrCity());
        else
            villageOrCityEt.setText("");
        if (AppConstants.isValidString(data.getAddL2()) && !data.getAddL2().equals("0"))
            etAddressLine2.setText(data.getAddL2());
        else
            etAddressLine2.setText("");
        if (AppConstants.isValidString(data.getAddL1()) && !data.getAddL1().equals("0"))
            etAddressLine1.setText(data.getAddL1());
        else
            etAddressLine1.setText("");


    }

    String oldMobile = "";

    private void showDialogPersonDetected(PersonFullData data) {
        String msg = "We found farmer name " + data.getName() + " " + "in system with this mobile, are you sure you want update it?";
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_show_new_person);
        Window window1 = dialog.getWindow();
        window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView messageTv = dialog.findViewById(R.id.api_response_fail_msg_tv);
        TextView okButton = dialog.findViewById(R.id.okButton);
        TextView cancel = dialog.findViewById(R.id.cancel);
        messageTv.setText(msg);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumberEt.setText(oldMobile);
                dialog.cancel();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewPersonData(data);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setNewPersonData() {
        etAddressLine2.setText("");
        villageOrCityEt.setText("");
        mandalOrTehsilEt.setText("");
        etSwiftCode.setText("");
        etUpi.setText("");
        branchEt.setText("");
        accountHolderName.setText("");
        accountEt.setText("");
        bankNameEt.setText("");
        ifscCodeEt.setText("");
        ti_et_income.setText("");
        beneficiaryEt.setText("");
        ti_et_phone_address.setText("");
        etSpouse.setText("");
        dobEt.setText("");
        ti_et_pan.setText("");
        adhaarEt.setText("");
        fatherNameEt.setText("");
//        phoneNumberEt.setText("");
        farmerNameEt.setText("");
        emailEt.setText("");
        etAddressLine1.setText("");
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
                        notifyApiDelay(FarmDetailsUpdateActivity2.this, response1.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response1.code());
                    }

                    if (latLngListCheckArea != null && latLngListCheckArea.size() > 2) {
                        progressBar_image.setVisibility(View.VISIBLE);
                        FarmDetailsUpdateActivity2.SubmitAreaAsync areaAsync = new FarmDetailsUpdateActivity2.SubmitAreaAsync();
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
                        FarmDetailsUpdateActivity2.SubmitAreaAsync areaAsync = new FarmDetailsUpdateActivity2.SubmitAreaAsync();
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
                    Log.e(TAG, "Failure getOfflineData " + t.toString());
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    notifyApiDelay(FarmDetailsUpdateActivity2.this, call.request().url().toString(),
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
                        notifyApiDelay(FarmDetailsUpdateActivity2.this, response1.raw().request().url().toString(),
                                "" + diff, internetSPeed, error, response1.code());
                    }

                    if (myBitmapAddress != null)
                        uploadAddressIdImage(personId);
                    else {
                        if (latLngListCheckArea != null && latLngListCheckArea.size() > 2) {
                            progressBar_image.setVisibility(View.VISIBLE);
                            FarmDetailsUpdateActivity2.SubmitAreaAsync areaAsync = new FarmDetailsUpdateActivity2.SubmitAreaAsync();
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
                            FarmDetailsUpdateActivity2.SubmitAreaAsync areaAsync = new FarmDetailsUpdateActivity2.SubmitAreaAsync();
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
                    Log.e(TAG, "Failure getOfflineData " + t.toString());
                    long newMillis = AppConstants.getCurrentMills();
                    long diff = newMillis - currMillis;
                    notifyApiDelay(FarmDetailsUpdateActivity2.this, call.request().url().toString(),
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
                    FarmDetailsUpdateActivity2.SubmitAreaAsync areaAsync = new FarmDetailsUpdateActivity2.SubmitAreaAsync();
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

    private String getDobFromAge(int age) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(Calendar.YEAR, (today.get(Calendar.YEAR) - age));

        int yr = today.get(Calendar.YEAR) - age;


        String ageS = yr + "-01-01";

        return ageS;
    }


    private String getYOBByAge(int age) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(Calendar.YEAR, (today.get(Calendar.YEAR) - age));

        int yr = today.get(Calendar.YEAR) - age;


        String ageS = yr + "";

        return ageS;
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

    private void disableAllFields(boolean status) {
        farmerNameEt.setEnabled(status);
        farmerNameEt.setFocusable(status);

        phoneNumberEt.setEnabled(status);
        phoneNumberEt.setFocusable(status);

        countryCodePicker.setEnabled(status);
        countryCodePicker.setFocusable(status);

        networkSpinner.setEnabled(status);
        networkSpinner.setFocusable(status);

        fatherNameEt.setEnabled(status);
        fatherNameEt.setFocusable(status);

        dobEtAge.setEnabled(status);
        dobEtAge.setFocusable(status);

        dobEt.setEnabled(status);
        dobEt.setFocusable(status);

        literateRadio.setEnabled(status);
        literateRadio.setFocusable(status);

        illiterateRadio.setEnabled(status);
        illiterateRadio.setFocusable(status);

        aplRadio.setEnabled(status);
        aplRadio.setFocusable(status);

        maleRadio.setEnabled(status);
        maleRadio.setFocusable(status);
        maleRadio.setClickable(status);

        femaleRadio.setEnabled(status);
        femaleRadio.setFocusable(status);

        otherRadio.setEnabled(status);
        otherRadio.setFocusable(status);

        bplRadio.setEnabled(status);
        bplRadio.setFocusable(status);
        civil_status_spinner.setEnabled(status);
        civil_status_spinner.setFocusable(status);

        microIrriAwarenessSpinner.setEnabled(status);
        microIrriAwarenessSpinner.setFocusable(status);

        radioSPhoneYes.setEnabled(status);
        radioSPhoneYes.setFocusable(status);

        radioSPhoneNo.setEnabled(status);
        radioSPhoneNo.setFocusable(status);

        religionSpinner.setEnabled(status);
        religionSpinner.setFocusable(status);

        casteSpinner.setEnabled(status);
        casteSpinner.setFocusable(status);

        casteCategoryspinner.setEnabled(status);
        casteCategoryspinner.setFocusable(status);

        adhaarEt.setEnabled(status);
        adhaarEt.setFocusable(status);

        ti_et_pan.setEnabled(status);
        ti_et_pan.setFocusable(status);

        countrySpinner.setEnabled(status);
        countrySpinner.setFocusable(status);
        countrySpinner.setClickable(status);
        countrySpinner.setActivated(status);

        stateSpinner.setEnabled(status);
        stateSpinner.setFocusable(status);
        stateSpinner.setClickable(status);

        districtSpinner.setEnabled(status);
        districtSpinner.setFocusable(status);
        districtSpinner.setClickable(status);

        mandalOrTehsilEt.setEnabled(status);
        mandalOrTehsilEt.setFocusable(status);


        villageOrCityEt.setEnabled(status);
        villageOrCityEt.setFocusable(status);

        etAddressLine2.setEnabled(status);
        etAddressLine2.setFocusable(status);

        etAddressLine1.setEnabled(status);
        etAddressLine1.setFocusable(status);

        ti_et_phone_address.setEnabled(status);
        ti_et_phone_address.setFocusable(status);


        emailEt.setEnabled(status);
        emailEt.setFocusable(status);

        etSpouse.setEnabled(status);
        etSpouse.setFocusable(status);

        etSpouseAge.setEnabled(status);
        etSpouseAge.setFocusable(status);

        etSpouseDob.setEnabled(status);
        etSpouseDob.setFocusable(status);

        familyCount.setEnabled(status);
        familyCount.setFocusable(status);

        adultCount.setEnabled(status);
        adultCount.setFocusable(status);

        kidsCount.setEnabled(status);
        kidsCount.setFocusable(status);

        dependentCount.setEnabled(status);
        dependentCount.setFocusable(status);

        sv_profile_photo.setEnabled(status);
        sv_profile_photo.setFocusable(status);

        idCircularImg.setEnabled(status);
        idCircularImg.setFocusable(status);

        addressIdCircularImage.setEnabled(status);
        addressIdCircularImage.setFocusable(status);

        ifscCodeEt.setEnabled(status);
        ifscCodeEt.setFocusable(status);


        bankNameEt.setEnabled(status);
        bankNameEt.setFocusable(status);

        accountEt.setEnabled(status);
        accountEt.setFocusable(status);

        accountHolderName.setEnabled(status);
        accountHolderName.setFocusable(status);

        branchEt.setEnabled(status);
        branchEt.setFocusable(status);

        etSwiftCode.setEnabled(status);
        etSwiftCode.setFocusable(status);

        beneficiaryEt.setEnabled(status);
        beneficiaryEt.setFocusable(status);

        radioShareCertYes.setEnabled(status);
        radioShareCertYes.setFocusable(status);

        radioIsShareNo.setEnabled(status);
        radioIsShareNo.setFocusable(status);

        beneficiaryEt.setEnabled(status);
        beneficiaryEt.setFocusable(status);

        radioShareCertYes.setEnabled(status);
        radioShareCertYes.setFocusable(status);

        radioShareCertNo.setEnabled(status);
        radioShareCertNo.setFocusable(status);

        ti_et_cert_count.setEnabled(status);
        ti_et_cert_count.setFocusable(status);

        ti_et_cert_value.setEnabled(status);
        ti_et_cert_value.setFocusable(status);

/*
        farmerNameEt.setEnabled(status);
        farmerNameEt.setFocusable(status);

        farmerNameEt.setEnabled(status);
        farmerNameEt.setFocusable(status);

        farmerNameEt.setEnabled(status);
        farmerNameEt.setFocusable(status);

        farmerNameEt.setEnabled(status);
        farmerNameEt.setFocusable(status);

        farmerNameEt.setEnabled(status);
        farmerNameEt.setFocusable(status);

        farmerNameEt.setEnabled(status);
        farmerNameEt.setFocusable(status);

        farmerNameEt.setEnabled(status);setEditDisabled
        farmerNameEt.setFocusable(status);

        farmerNameEt.setEnabled(status);
        farmerNameEt.setFocusable(status);
*/

    }

    Farm farm;

    private FarmData getFarmData(String id, List<FarmData> farmList) {

        FarmData farmData = null;

        if (farmList != null && farmList.size() > 0) {

            for (int i = 0; i < farmList.size(); i++) {
                if (id != null && farmList.get(i).getFarmId() != null && id.trim().equals(farmList.get(i).getFarmId())) {
                    farmData = farmList.get(i);
                    break;
                }
            }
        }

        return farmData;

    }

    private int getFarmDataIndex(String id, List<FarmData> farmList) {

        int farmData = -1;

        if (farmList != null && farmList.size() > 0) {

            for (int i = 0; i < farmList.size(); i++) {
                if (id != null && farmList.get(i).getFarmId() != null && id.trim().equals(farmList.get(i).getFarmId())) {
                    farmData = i;
                    break;
                }
            }
        }

        return farmData;

    }


    private void setPreviousDataOffline(final Farm fullDetails) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FarmDetailsUpdateActivity2.this.farm = fullDetails;
                FarmData farmDataOld = null;
                ownerId = fullDetails.getFarmerId() + "";
                clusterId = "" + fullDetails.getClusterId();
//                addedBy=""+fullDetails.getadd();
                try {
                    farmDataOld = new Gson().fromJson(fullDetails.getFarmFullData(), FarmData.class);
                    Log.d(TAG, "Offline farm2 " + new Gson().toJson(farmDataOld));
                    Log.d(TAG, "Offline farm2 O " + ownerId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FarmData finalFarmDataOld = farmDataOld;
                FarmerCacheManager.getInstance(context).getFarmer(new FarmerCacheManager.GetFarmerListener() {
                    @Override
                    public void onFarmerLoaded(FarmerT farmerT) {
                        if (farmerT != null && AppConstants.isValidString(farmerT.getData()) && !farmerT.getData().equals("{}")) {
                            FarmDetailsUpdateActivity2.this.farmerT = farmerT;
                            try {
                                FarmerAndFarmData farmerAndFarmData = new Gson().fromJson(farmerT.getData().trim(), FarmerAndFarmData.class);
                                Log.d(TAG, "Offline farmer data " + new Gson().toJson(farmerAndFarmData));
                                setOfflineFarmerData(farmerAndFarmData);
                                FarmData farmData;
                                farmData = finalFarmDataOld;

                                if (finalFarmDataOld != null) {
                                    farmCompId = finalFarmDataOld.getCompId() + "";
                                    farmClusterId = finalFarmDataOld.getCluster() + "";
                                    farmData.setId(finalFarmDataOld.getId());
                                    farmData.setFarmId(finalFarmDataOld.getFarmId() + "");
                                    farmData.setName(finalFarmDataOld.getName());
                                    farmData.setOwnerId(finalFarmDataOld.getOwnerId() + "");
                                    farmData.setCluster(finalFarmDataOld.getCluster() + "");
                                    farmData.setSeason(finalFarmDataOld.getSeason() + "");
                                    farmData.setLat_cord(finalFarmDataOld.getLat_cord() + "");
                                    farmData.setLong_cord(finalFarmDataOld.getLong_cord() + "");
                                    farmData.setCurrentCropName(finalFarmDataOld.getCurrentCropName());
                                    farmData.setMotorHp(finalFarmDataOld.getMotorHp());
                                    farmData.setSelectedSeason(getOperatorIndex(finalFarmDataOld.getSeason()));
                                    Log.e(TAG, "Offline Farmmm " + finalFarmDataOld.getMotorHp() + " LIST " + new Gson().toJson(motorHpList));
                                    DDNew motorHp = getOperatorIndex(finalFarmDataOld.getMotorHp(), motorHpList);
                                    Log.d(TAG, "Offline Farmmm2 " + new Gson().toJson(motorHp));
                                    if (motorHp != null)
                                        farmData.setSelectedMotorHP(motorHp);
                                }
                                if (finalFarmDataOld != null) {
                                    farmData.setCountry(finalFarmDataOld.getCountry() + "");
                                    farmData.setCountryId(finalFarmDataOld.getCountry());
                                    farmData.setState(finalFarmDataOld.getState());
                                    farmData.setStateId(finalFarmDataOld.getState());
                                    farmData.setDistrict(finalFarmDataOld.getDistrict());
                                    farmData.setDistrictId(finalFarmDataOld.getDistrict());
                                    farmData.setMandalOrTehsil(finalFarmDataOld.getMandalOrTehsil());
                                    farmData.setVillageOrCity(finalFarmDataOld.getVillageOrCity());
                                    farmData.setAddL1(finalFarmDataOld.getAddL1());
                                    farmData.setAddL2(finalFarmDataOld.getAddL2());
                                    Log.e(TAG, "COUNTRYOFF " + finalFarmDataOld.getCountry() + ", " + finalFarmDataOld.getState() + ", " + finalFarmDataOld.getDistrict());
                                    if (AppConstants.isValidString(finalFarmDataOld.getCountry())) {
                                        CountryDatum countryDatum = null;
                                        for (int i = 0; i < countryDatumList.size(); i++) {
                                            if (finalFarmDataOld.getCountry().trim().equalsIgnoreCase(countryDatumList.get(i).getName().trim())) {
                                                countryDatum = countryDatumList.get(i);
                                                break;
                                            }
                                        }
                                        if (countryDatum != null) {
                                            try {
                                                farmData.setSelectedCountry(countryDatum);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            if (AppConstants.isValidString(finalFarmDataOld.getState()) && countryDatum.getStateDatumList() != null &&
                                                    countryDatum.getStateDatumList().size() > 0) {
                                                if (AppConstants.isValidString(finalFarmDataOld.getState())) {
                                                    StateDatum stateDatum = null;
                                                    for (int i = 0; i < countryDatum.getStateDatumList().size(); i++) {
                                                        if (finalFarmDataOld.getState().trim().
                                                                equalsIgnoreCase(countryDatum.getStateDatumList().get(i).getName().trim())) {
                                                            stateDatum = countryDatum.getStateDatumList().get(i);
                                                            break;
                                                        }
                                                    }
                                                    if (stateDatum != null) {
                                                        try {
                                                            if (farmData.getStateDatumList() == null) {
                                                                farmData.setStateDatumList(new ArrayList<>());
                                                                ;
                                                            } else {
                                                                farmData.getStateDatumList().clear();
                                                            }
                                                            farmData.getStateDatumList().addAll(countryDatum.getStateDatumList());
                                                            farmData.setSelectedState(stateDatum);
                                                            farmData.setState(stateDatum.getName());
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (AppConstants.isValidString(finalFarmDataOld.getDistrict())) {
                                                            CityDatum cityDatum = null;
                                                            for (int i = 0; i < stateDatum.getCities().size(); i++) {
//                                                        Log.e(TAG,"FARMCITY "+stateDatum.getCities().get(i).getName());
                                                                if (finalFarmDataOld.getDistrict().trim().
                                                                        equalsIgnoreCase(stateDatum.getCities().get(i).getName().trim())) {
                                                                    cityDatum = stateDatum.getCities().get(i);
//                                                            Log.e(TAG,"FARMCITY INN "+new Gson().toJson(cityDatum));
                                                                    break;
                                                                }
                                                            }
                                                            if (cityDatum != null) {
                                                                try {
                                                                    farmData.setSelectedDistrict(cityDatum);
                                                                    farmData.setDistrict(cityDatum.getName());
                                                                    if (farmData.getCityDatumList() != null) {
                                                                        farmData.getCityDatumList().clear();
                                                                        farmData.getCityDatumList().addAll(stateDatum.getCities());
                                                                    } else {
                                                                        farmData.setCityDatumList(new ArrayList<>());
                                                                        farmData.getCityDatumList().addAll(stateDatum.getCities());
                                                                    }


//                                                            Log.e(TAG,"ERRRRRRRO NO ");
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
//                                                            Log.e(TAG,"ERRRRRRRO "+e.toString());
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }


                                        }
                                    }

                                }
                                if (finalFarmDataOld != null) {
                                    if (finalFarmDataOld.getFpoCropList() != null && finalFarmDataOld.getFpoCropList().size() > 0)
                                        farmData.setFpoCropList(finalFarmDataOld.getFpoCropList());
                                    if (finalFarmDataOld.getAssetsList() != null && finalFarmDataOld.getAssetsList().size() > 0)
                                        farmData.setAssetsList(finalFarmDataOld.getAssetsList());

                                    farmData.setCropId(finalFarmDataOld.getCropId() + "");
                                    farmData.setArea(finalFarmDataOld.getArea() + "");
                                    farmData.setAreaS(finalFarmDataOld.getArea() + "");
                                    farmData.setCurrentCropName(finalFarmDataOld.getCurrentCropName());
                                    farmData.setCropping_pattern(finalFarmDataOld.getCropping_pattern());
                                    farmData.setIs_irrigated(finalFarmDataOld.getPlanting_method());
                                    farmData.setIrrigationsource(finalFarmDataOld.getIrrigationsource() + "");
                                    farmData.setIrrigationtype(finalFarmDataOld.getIrrigationtype() + "");
                                    farmData.setPreviouscrop(finalFarmDataOld.getPreviouscrop());
                                    farmData.setSoiltype(finalFarmDataOld.getSoiltype() + "");
                                    farmData.setPlanting_method(finalFarmDataOld.getPlanting_method());
                                    farmData.setTransplant_date(finalFarmDataOld.getTransplant_date());
                                    farmData.setSelectedCrop(getOperatorIndexByName(finalFarmDataOld.getCurrentCropName() + "", farmCropList));
                                    farmData.setSelectedPrevCrop(getOperatorIndex(finalFarmDataOld.getPreviouscrop() + "", farmCropList));
                                    farmData.setSelectedIrriSource(getOperatorIndex(finalFarmDataOld.getIrrigationsource() + "", irrigationSourceDDList));
                                    farmData.setSelectedIrriType(getOperatorIndex(finalFarmDataOld.getIrrigationtype() + "", irrigationTypeDDList));
                                    farmData.setSelectedSoilType(getOperatorIndex(finalFarmDataOld.getSoiltype() + "", soilTypeDDList));
                                    farmData.setSelectedCroppingPattern(getOperatorIndexByValue(finalFarmDataOld.getCropping_pattern() + "", croppingPatternDDList));
                                    farmData.setSelectedPlantingMethod(getOperatorIndexByValue(finalFarmDataOld.getPlanting_method() + "", plantingMethodDDList));
                                    farmData.setSelectedLandCategory(getOperatorIndexByValue(finalFarmDataOld.getIs_irrigated() + "", isIrrigatedDDList));
                                }
                                Log.e(TAG, "ERRRRRRRO " + new Gson().toJson(farmData.getSelectedDistrict()));
                                farmList.clear();
                                farmList.add(farmData);

                                adapter.setEditDisabled(isEditDisabled);
                                adapter.notifyDataSetChanged();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
//                    Toast.makeText(context, "else farmer ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, ownerId);
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 1000);

    }

    private void setOfflineFarmerData(FarmerAndFarmData farmerAndFarmData) {
        this.farmerAndFarmData = farmerAndFarmData;


//        if (fullDetails.getPersonDetails() != null) {
//            PersonDetails details = fullDetails.getPersonDetails();
        if (AppConstants.isValidString(farmerAndFarmData.getName()) && !farmerAndFarmData.getName().equals("0"))
            farmerNameEt.setText(farmerAndFarmData.getName());
        if (AppConstants.isValidString(farmerAndFarmData.getMob()) && !farmerAndFarmData.getMob().equals("0")) {
            phoneNumberEt.setText(farmerAndFarmData.getMob());
            oldMobile = farmerAndFarmData.getMob();
            countryCodePicker.setFullNumber(farmerAndFarmData.getCountryCode()+oldMobile);
        }

        if (AppConstants.isValidString(farmerAndFarmData.getFinancial_status()) && !farmerAndFarmData.getFinancial_status().equals("0")) {
            if (farmerAndFarmData.getFinancial_status().equalsIgnoreCase("bpl")) {
                bplRadio.setChecked(true);
            } else if (farmerAndFarmData.getFinancial_status().equalsIgnoreCase("apl")) {
                aplRadio.setChecked(true);
            }
        }
        if (AppConstants.isValidString(farmerAndFarmData.getLiteracy()) && !farmerAndFarmData.getLiteracy().equals("0")) {
            if (farmerAndFarmData.getLiteracy().equalsIgnoreCase("L")) {
                literateRadio.setChecked(true);
            } else if (farmerAndFarmData.getLiteracy().equalsIgnoreCase("I")) {
                illiterateRadio.setChecked(true);
            }
        }

        phoneNumberEt.clearFocus();
        try {
            DDNew operator = getOperatorIndex(farmerAndFarmData.getMobileNetworkId(), mobileOperatorList);
            if (operator != null)
                networkSpinner.setSelectedItem(operator);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            DDNew status = getOperatorIndexByName(farmerAndFarmData.getCivil_status(), civilStatusList);
            if (status != null)
                civil_status_spinner.setSelectedItem(status);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Log.e(TAG, "RELIOFF " + farmerAndFarmData.getReligion());
            DDNew religion = getOperatorIndex(farmerAndFarmData.getReligion(), religionList);
            if (religion != null)
                religionSpinner.setSelectedItem(religion);


        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            DDNew casteCat = getOperatorIndex(farmerAndFarmData.getCasteCategoryId(), casteCategoryList);
            if (casteCat != null)
                casteCategoryspinner.setSelectedItem(casteCat);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            DDNew caste = getOperatorIndex(farmerAndFarmData.getCasteId(), casteList);
            if (caste != null)
                casteSpinner.setSelectedItem(caste);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            DDNew mia = getOperatorIndex(farmerAndFarmData.getMiaId(), microIrriAwarenessList);
            if (mia != null)
                microIrriAwarenessSpinner.setSelectedItem(mia);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (AppConstants.isValidString(farmerAndFarmData.getFather_name()) && !farmerAndFarmData.getFather_name().equals("0"))
            fatherNameEt.setText(farmerAndFarmData.getFather_name());
        if (AppConstants.isValidString(farmerAndFarmData.getAadhaar()) && !farmerAndFarmData.getAadhaar().equals("0"))
            adhaarEt.setText(farmerAndFarmData.getAadhaar());
        if (AppConstants.isValidString(farmerAndFarmData.getPan()) && !farmerAndFarmData.getPan().equals("0"))
            ti_et_pan.setText(farmerAndFarmData.getPan());

        if (AppConstants.isValidString(farmerAndFarmData.getDob()) && !farmerAndFarmData.getDob().equals("0") && !farmerAndFarmData.getDob().equals(for_date)) {
            dobEt.setText(AppConstants.getShowableDate(farmerAndFarmData.getDob(), context));
            dobEtAge.setText(AppConstants.getShowableDate(farmerAndFarmData.getDob(), context));

        } else if (AppConstants.isValidString(farmerAndFarmData.getYob()) && !farmerAndFarmData.getYob().equals("0") && !farmerAndFarmData.getYob().equals(for_date)) {
            dobEt.setText(farmerAndFarmData.getYob());
            dobEtAge.setText(farmerAndFarmData.getYob());
        }
        String gender = farmerAndFarmData.getGeder();
        String is_using_smartPhone = farmerAndFarmData.getIsSmartPhone();
        if (gender != null) {
            if (gender.equalsIgnoreCase("M")) {
                maleRadio.setChecked(true);
            } else if (gender.equalsIgnoreCase("F")) {
                femaleRadio.setChecked(true);
            } else {
                otherRadio.setChecked(true);
            }
        }
        if (is_using_smartPhone != null && is_using_smartPhone.equals("Y")) {
            radioSPhoneYes.setChecked(true);
        } else {
            radioSPhoneNo.setChecked(true);
        }
        if (AppConstants.isValidString(farmerAndFarmData.getSpouse_name()) && !farmerAndFarmData.getSpouse_name().equals("0"))
            etSpouse.setText(farmerAndFarmData.getSpouse_name());

        if (AppConstants.isValidString(farmerAndFarmData.getSpouseDob()) && !farmerAndFarmData.getSpouseDob().equals("0")
                && !farmerAndFarmData.getSpouseDob().equals(AppConstants.for_date)) {
            etSpouseDob.setText(AppConstants.getShowableDate(farmerAndFarmData.getSpouseDob(), context));
            etSpouseAge.setText(AppConstants.getShowableDate(farmerAndFarmData.getSpouseDob(), context));
        }

        familyCount.setText("" + farmerAndFarmData.getFamily_mem_count());
        adultCount.setText("" + farmerAndFarmData.getAdults_count());
        kidsCount.setText("" + farmerAndFarmData.getKids_count());
        dependentCount.setText("" + farmerAndFarmData.getDependent_count());

        if (AppConstants.isValidString(farmerAndFarmData.getEmail()) && !farmerAndFarmData.getEmail().equals("0"))
            emailEt.setText("" + farmerAndFarmData.getEmail());
        if (AppConstants.isValidString(farmerAndFarmData.getMob2()) && !farmerAndFarmData.getMob2().equals("0"))
            ti_et_phone_address.setText("" + farmerAndFarmData.getMob2());
        if (AppConstants.isValidString(farmerAndFarmData.getBeneficiary_name()) && !farmerAndFarmData.getBeneficiary_name().equals("0"))
            beneficiaryEt.setText("" + farmerAndFarmData.getBeneficiary_name());
        if (AppConstants.isValidString(farmerAndFarmData.getAnualIncome()) && !farmerAndFarmData.getAnualIncome().equals("0"))
            ti_et_income.setText("" + farmerAndFarmData.getAnualIncome());

        String isShareHolder = null;
        if (isShareHolder != null && isShareHolder.equalsIgnoreCase("y")) {
            radioIsShareYes.setChecked(true);
            Share share = farmerAndFarmData.getShareJson();
            if (share != null) {
                if (share.getCertificate() != null && share.getCertificate().equalsIgnoreCase("Y")) {
                    radioShareCertYes.setChecked(true);
                    ti_et_cert_count.setText(share.getHowManyCert());
                    ti_et_cert_value.setText(share.getHowMuchValue());
                } else {
                    radioIsShareNo.setChecked(true);
                }
            }
        } else {
            radioIsShareNo.setChecked(true);
        }
//        }

        if (farmerAndFarmData != null) {
            if (AppConstants.isValidString(farmerAndFarmData.getIfscCode()) && !farmerAndFarmData.getIfscCode().equals("0"))
                ifscCodeEt.setText(farmerAndFarmData.getIfscCode());
            if (AppConstants.isValidString(farmerAndFarmData.getBankName()) && !farmerAndFarmData.getBankName().equals("0"))
                bankNameEt.setText(farmerAndFarmData.getBankName());
            if (AppConstants.isValidString(farmerAndFarmData.getAccountNumber()) && !farmerAndFarmData.getAccountNumber().equals("0"))
                accountEt.setText(farmerAndFarmData.getAccountNumber());
            if (AppConstants.isValidString(farmerAndFarmData.getHolderName()) && !farmerAndFarmData.getHolderName().equals("0"))
                accountHolderName.setText(farmerAndFarmData.getHolderName());
            if (AppConstants.isValidString(farmerAndFarmData.getBranch()) && !farmerAndFarmData.getBranch().equals("0"))
                branchEt.setText(farmerAndFarmData.getBranch());
            if (AppConstants.isValidString(farmerAndFarmData.getUpi_id()) && !farmerAndFarmData.getUpi_id().equals("0"))
                etUpi.setText(farmerAndFarmData.getUpi_id());
            if (AppConstants.isValidString(farmerAndFarmData.getSwiftCode()) && !farmerAndFarmData.getSwiftCode().equals("0"))
                etSwiftCode.setText(farmerAndFarmData.getUpi_id());
        }
        if (farmerAndFarmData != null) {
            if (AppConstants.isValidString(farmerAndFarmData.getMandal_or_tehsil()) && !farmerAndFarmData.getMandal_or_tehsil().equals("0"))
                mandalOrTehsilEt.setText(farmerAndFarmData.getMandal_or_tehsil());
            if (AppConstants.isValidString(farmerAndFarmData.getVillage_or_city()) && !farmerAndFarmData.getVillage_or_city().equals("0"))
                villageOrCityEt.setText(farmerAndFarmData.getVillage_or_city());
            if (AppConstants.isValidString(farmerAndFarmData.getAddL2()) && !farmerAndFarmData.getAddL2().equals("0"))
                etAddressLine2.setText(farmerAndFarmData.getAddL2());
            if (AppConstants.isValidString(farmerAndFarmData.getAddL1()) && !farmerAndFarmData.getAddL1().equals("0"))
                etAddressLine1.setText(farmerAndFarmData.getAddL1());
            Log.e(TAG, "PERSON_ADD_OFF " + farmerAndFarmData.getCountry() + ", " + farmerAndFarmData.getState() + ", " + farmerAndFarmData.getDistrict());
            if (AppConstants.isValidString(farmerAndFarmData.getCountry())) {
                CountryDatum countryDatum = null;
                for (int i = 0; i < countryDatumList.size(); i++) {
                    if (farmerAndFarmData.getCountry().trim().equalsIgnoreCase(countryDatumList.get(i).getName().trim())) {
                        countryDatum = countryDatumList.get(i);
                        break;
                    }
                }
                if (countryDatum != null) {
                    try {
                        countrySpinner.setSelectedItem(countryDatum);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (AppConstants.isValidString(farmerAndFarmData.getState()) && countryDatum.getStateDatumList() != null && countryDatum.getStateDatumList().size() > 0) {
                        StateDatum stateDatum = null;
                        for (int i = 0; i < countryDatum.getStateDatumList().size(); i++) {
                            if (farmerAndFarmData.getState().trim().equalsIgnoreCase(countryDatum.getStateDatumList().get(i).getName().trim())) {
                                stateDatum = countryDatum.getStateDatumList().get(i);
                                break;
                            }
                        }
                        if (stateDatum != null) {
                            try {
                                stateSpinner.setSelectedItem(stateDatum);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CityDatum cityDatum = null;
                            if (AppConstants.isValidString(farmerAndFarmData.getDistrict())) {
                                for (int i = 0; i < stateDatum.getCities().size(); i++) {
                                    if (farmerAndFarmData.getDistrict().trim().equalsIgnoreCase(stateDatum.getCities().get(i).getName().trim())) {
                                        cityDatum = stateDatum.getCities().get(i);
                                        break;
                                    }
                                }
                                if (cityDatum != null) {
                                    try {
                                        districtSpinner.setSelectedItem(cityDatum);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }
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
//        if (dobPersonDate != null && selectedPersonDate == SELECTED_TYPE.DOB)
//            etDobDialog.setDate(dobPersonDate);

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
                        selectedPersonDateSpouse = SELECTED_TYPE.DOB;
                        dobPersonDateSpouse = etDobDialogSpouse.getDate();
                        etSpouseAge.setText(personDobStrSpouse);
                        dialogPersonDobSpouse.dismiss();
                    }
                } else if (radioAgeSpouse.isChecked()) {
                    if (TextUtils.isEmpty(ageEtDialogSpouse.getText().toString())) {
                        AppConstants.failToast(context, "Please enter age");
                    } else {
                        personDobStrSpouse = ageEtDialogSpouse.getText().toString();
                        selectedPersonDateSpouse = SELECTED_TYPE.AGE;
                        etSpouseAge.setText(personDobStrSpouse);
                        dialogPersonDobSpouse.dismiss();
                    }
                } else if (radioYobSpouse.isChecked()) {
                    if (TextUtils.isEmpty(etYobDilogSpouse.getText().toString())) {
                        AppConstants.failToast(context, "Please enter year of birth");
                    } else {
                        personDobStrSpouse = etYobDilogSpouse.getText().toString();
                        selectedPersonDateSpouse = SELECTED_TYPE.YOB;
                        etSpouseAge.setText(personDobStrSpouse);
                        dialogPersonDobSpouse.dismiss();
                    }
                } else {
                    personDobStrSpouse = "";
                    selectedPersonDateSpouse = SELECTED_TYPE.NA;
                }
                if (selectedPersonDateSpouse == SELECTED_TYPE.DOB && AppConstants.isValidString(personDobStrSpouse)) {
                    Log.e(TAG, "DOBYOB 1, " + AppConstants.getUploadableDate(personDobStrSpouse, context));

                } else if (selectedPersonDateSpouse == SELECTED_TYPE.AGE && AppConstants.isValidString(personDobStrSpouse)) {
                    try {
                        Log.e(TAG, "DOBYOB 2, " + getYOBByAge(Integer.valueOf(personDobStrSpouse.trim())));

                    } catch (Exception e) {

                    }
                } else if (selectedPersonDateSpouse == SELECTED_TYPE.YOB && AppConstants.isValidString(personDobStrSpouse)) {
                    Log.e(TAG, "DOBYOB 3, " + personDobStrSpouse.trim());
                }

            }
        });


    }

    private void showDateDialogSpouse() {
        /*if (dobPersonDateSpouse != null && selectedPersonDateSpouse == SELECTED_TYPE.DOB)
            etDobDialogSpouse.setDate(dobPersonDateSpouse);*/

        dialogPersonDobSpouse.show();
    }
}
