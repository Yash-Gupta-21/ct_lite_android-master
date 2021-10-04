package com.i9930.croptrails.Landing;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.AlarmManager;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.listener.StateUpdatedListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.IStatusListener;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.i9930.croptrails.AddFarm.AddFarmActivity;
import com.i9930.croptrails.AddFarm.Model.CowAndCatles;
import com.i9930.croptrails.AddFarm.Model.FPOCrop;
import com.i9930.croptrails.AddFarm.Model.FarmCrop;
import com.i9930.croptrails.AddFarm.Model.FarmData;
import com.i9930.croptrails.AddFarm.Model.FarmerAndFarmData;
import com.i9930.croptrails.AddFarm.Model.FarmerFarmInsertResponse;
import com.i9930.croptrails.AddFarm.Model.FarmerFarmInsertResponseFarms;
import com.i9930.croptrails.AddFarm.Model.FarmerSpinnerData;
import com.i9930.croptrails.AddFarm.Model.FetchFarmerResponse;
import com.i9930.croptrails.AddFarm.Model.FormModel;
import com.i9930.croptrails.AddFarm.Model.SupervisorListResponse;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterCluster;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonAdapters.SimpleStringAdapter;
import com.i9930.croptrails.CommonAdapters.SpinnerAdapterVillage;
import com.i9930.croptrails.CommonAdapters.SpinnerAdapterchak;
import com.i9930.croptrails.CommonClasses.Cluster;
import com.i9930.croptrails.CommonClasses.ClusterResponse;
import com.i9930.croptrails.CommonClasses.DDResponseNew;
import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormDatum;
import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormResponse;
import com.i9930.croptrails.CommonClasses.ParamData;
import com.i9930.croptrails.CommonClasses.PldReason;
import com.i9930.croptrails.CommonClasses.Season.SeasonResponse;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.CommonClasses.TimelineUnits;
import com.i9930.croptrails.Contact.ContactActivity;
import com.i9930.croptrails.CropCycle.Create.CreateCropCycleActivity;
import com.i9930.croptrails.CropCycle.Show.CropCycleListActivity;
import com.i9930.croptrails.Dashboard.DashboardActivity;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.ExpenseSV.ExpenseSvActivity;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.IrrigationSource;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.IrrigationType;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.Season;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.SoilType;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.UpdatRsponse;
import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineResponse;
import com.i9930.croptrails.GerminationAndSpacing.ShowSampleGermination.Model.SampleGerminationDatum;
import com.i9930.croptrails.HarvestCollection.HarvestPlanActivity;
import com.i9930.croptrails.HarvestPlan.HarvestShowPlanActivity;
import com.i9930.croptrails.HarvestReport.Model.HarvestDetailInnerData;
import com.i9930.croptrails.HarvestReport.Model.HarvestDetailMaster;
import com.i9930.croptrails.HarvestReport.Model.ViewHarvestDetails;
import com.i9930.croptrails.HarvestSubmit.Model.SendHarvestData;
import com.i9930.croptrails.Landing.Adapter.FarmDetailAdapter;
import com.i9930.croptrails.Landing.Adapter.FarmDetailAdapterOffline;
import com.i9930.croptrails.Landing.Adapter.FarmerFarmAdapter;
import com.i9930.croptrails.Landing.Adapter.VettingFarmAdapterNew;
import com.i9930.croptrails.Landing.Fragments.Model.Datum;
import com.i9930.croptrails.Landing.Fragments.Model.ExpenseData;
import com.i9930.croptrails.Landing.Fragments.Pagination.PaginationScrollListener;
import com.i9930.croptrails.Landing.Models.FarmCountDatum;
import com.i9930.croptrails.Landing.Models.FarmCountResponse;
import com.i9930.croptrails.Landing.Models.Farmer.FetchFarmerFarmResponse;
import com.i9930.croptrails.Landing.Models.FetchFarmData;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.Landing.Models.FetchFarmSendData;
import com.i9930.croptrails.Landing.Models.Filter.ClusterVillageResponse;
import com.i9930.croptrails.Landing.Models.Filter.ClusterVillages;
import com.i9930.croptrails.Landing.Models.Filter.VillageChak;
import com.i9930.croptrails.Landing.Models.Filter.VillageChakResponse;
import com.i9930.croptrails.Landing.Models.GeoCardsResponse;
import com.i9930.croptrails.Landing.Models.NewData.FarmResponseNew;
import com.i9930.croptrails.Landing.Models.NewData.FetchFarmResultNew;
import com.i9930.croptrails.Landing.Models.Offline.FarmVisitResponse;
import com.i9930.croptrails.Landing.Models.ad.AdFarmDatum;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.Login.LoginActivity;
import com.i9930.croptrails.Login.Model.CompParamDatum;
import com.i9930.croptrails.Login.Model.CompParamResponse;
import com.i9930.croptrails.Maps.CheckArea.CheckAreaMapActivity;
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;
import com.i9930.croptrails.Notification.NotificationActivity;
import com.i9930.croptrails.OfflineMode.Model.CalendarActResponse;
import com.i9930.croptrails.OfflineMode.Model.FarmIdArray;
import com.i9930.croptrails.OfflineMode.Model.FarmsCoordinatesResponse;
import com.i9930.croptrails.OfflineMode.Model.OfflineResponse;
import com.i9930.croptrails.OfflineMode.OfflineModeActivity;
import com.i9930.croptrails.OfflineMode.send.SendOfflineInputCost;
import com.i9930.croptrails.OfflineMode.send.SendOfflineResource;
import com.i9930.croptrails.Pro.ProActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.AppDatabase;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegCacheManager;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegModel;
import com.i9930.croptrails.RoomDatabase.CompRegistry.DatabaseResInterface;
import com.i9930.croptrails.RoomDatabase.CompRegistry.Model.CompRegResult;
import com.i9930.croptrails.RoomDatabase.CompRegistry.Model.ResponseCompReg;
import com.i9930.croptrails.RoomDatabase.DoneActivities.ActivityCacheManager;
import com.i9930.croptrails.RoomDatabase.DoneActivities.AllActivityFetchListener;
import com.i9930.croptrails.RoomDatabase.DoneActivities.DoneActivity;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownCacheManager;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownCacheManager2;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownT2;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmCacheManager;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmNotifyInterface;
import com.i9930.croptrails.RoomDatabase.FarmTable.SendOfflineFarm;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerCacheManager;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerT;
import com.i9930.croptrails.RoomDatabase.Gps.GetSetInterface;
import com.i9930.croptrails.RoomDatabase.Gps.GpsCacheManager;
import com.i9930.croptrails.RoomDatabase.Gps.GpsLog;
import com.i9930.croptrails.RoomDatabase.Harvest.AllHarvestFetchListener;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestCacheManager;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestT;
import com.i9930.croptrails.RoomDatabase.InputCost.AllInputCostFetchListener;
import com.i9930.croptrails.RoomDatabase.InputCost.InputCostCacheManager;
import com.i9930.croptrails.RoomDatabase.InputCost.InputCostT;
import com.i9930.croptrails.RoomDatabase.Pld.OfflinePld;
import com.i9930.croptrails.RoomDatabase.Pld.PldCacheManager;
import com.i9930.croptrails.RoomDatabase.ResourceCost.AllResourceFetchListener;
import com.i9930.croptrails.RoomDatabase.ResourceCost.ResourceCacheManager;
import com.i9930.croptrails.RoomDatabase.ResourceCost.ResourceCostT;
import com.i9930.croptrails.RoomDatabase.Task.SvTask;
import com.i9930.croptrails.RoomDatabase.Task.TaskCacheManager;
import com.i9930.croptrails.RoomDatabase.Timeline.CalendarJSON;
import com.i9930.croptrails.RoomDatabase.Timeline.Timeline;
import com.i9930.croptrails.RoomDatabase.Timeline.TimelineCacheManager;
import com.i9930.croptrails.RoomDatabase.Visit.AllVisitFetchListener;
import com.i9930.croptrails.RoomDatabase.Visit.Visit;
import com.i9930.croptrails.RoomDatabase.Visit.VrCacheManager;
import com.i9930.croptrails.RoomDatabase.VisitStructure.VisitCacheManager;
import com.i9930.croptrails.RoomDatabase.VisitStructure.VisitNotifyInterface;
import com.i9930.croptrails.RoomDatabase.VisitStructure.VisitTable;
import com.i9930.croptrails.Scan.ScanQRActivity;
import com.i9930.croptrails.ServiceAndBroadcasts.Model.RecieveResponseGpsBack;
import com.i9930.croptrails.ServiceAndBroadcasts.Model.SendGpsArray;
import com.i9930.croptrails.Setting.SettingActivity;
import com.i9930.croptrails.ShowInputCost.Model.InputCostData;
import com.i9930.croptrails.ShowInputCost.Model.InputCostResponse;
import com.i9930.croptrails.ShowInputCost.Model.ResourceData;
import com.i9930.croptrails.SoilSense.BluetoothCommunicationActivity;
import com.i9930.croptrails.SoilSense.Dashboard.SoilSensDashboardActivity;
import com.i9930.croptrails.SubmitActivityForm.Model.CompAgriResponse;
import com.i9930.croptrails.SubmitActivityForm.Model.DoneActNew.DoneActInputs;
import com.i9930.croptrails.SubmitActivityForm.Model.DoneActNew.DoneActivityResponseNew;
import com.i9930.croptrails.SubmitActivityForm.Model.SendTimelineActivityDataNew;
import com.i9930.croptrails.SubmitInputCost.Model.CostAndResourceSubmitData;
import com.i9930.croptrails.SubmitInputCost.Model.CostSubmitResponse;
import com.i9930.croptrails.SubmitInputCost.Model.ExpenseDataDD;
import com.i9930.croptrails.SubmitInputCost.Model.InputCostSubmitDatum;
import com.i9930.croptrails.SubmitInputCost.Model.ResourceDataDD;
import com.i9930.croptrails.SubmitInputCost.Model.ResourceSubmitDatum;
import com.i9930.croptrails.SubmitPld.Model.PldReasonResponse;
import com.i9930.croptrails.SubmitPld.Model.SendPldData;
import com.i9930.croptrails.SubmitVisitForm.Model.AddVisitSendDataNew;
import com.i9930.croptrails.SubmitVisitForm.Model.VisitResponseNew;
import com.i9930.croptrails.Task.Model.Farm.SvFarm;
import com.i9930.croptrails.Task.Model.Farm.SvFarmResponse;
import com.i9930.croptrails.Task.Model.TaskDatum;
import com.i9930.croptrails.Task.Model.TaskResponse;
import com.i9930.croptrails.Task.SvTaskActivityActivity;
import com.i9930.croptrails.Test.FarmDetailsVettingActivity;
import com.i9930.croptrails.Test.PldModel.PldData;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Test.model.TimelineHarvest;
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
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.LocationInsertReciever;
import com.i9930.croptrails.Utility.LocationUploadReceiver;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;
import com.i9930.croptrails.Utility.ShowProgress;
import com.i9930.croptrails.UtilityFP.BiometricUtils;
import com.i9930.croptrails.Vetting.RejectedVettingActivity;
import com.i9930.croptrails.Vetting.VettingOthersFarmActivity;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class LandingActivity extends AppCompatActivity implements StateUpdatedListener<InstallState>, NavigationView.OnNavigationItemSelectedListener
        , FarmNotifyInterface, DatabaseResInterface, AllResourceFetchListener,
        AllInputCostFetchListener, AllVisitFetchListener, AllHarvestFetchListener, AllActivityFetchListener, VisitNotifyInterface {

    String TAG = "LandingActivity";
    public static boolean isNearFarmLoaded = false;
    String network_type = "";
    Context context;
    Boolean exit = false;
    RecyclerView recyclerView;
    PaginationScrollListener paginationScrollListener;
    Boolean home_frag = true;
    ConnectivityManager connectivityManager;
    boolean connected = false;
    ProgressDialog progressDialog;
    Location currentLocation;

    NavigationView navigationView;
    CompRegCacheManager compRegCacheManager;

    CountryCodePicker countryCodePicker;
    Resources resources;
    ViewFailDialog viewFailDialog;
    ShowProgress showProgress;
    int locationFlag = 0;

    AppUpdateManager appUpdateManager;
    private final int UPDATE_REQ_CODE = 403;
    List<OfflinePld> offlinePldList = new ArrayList<>();
    View connectivityLine;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                }
                return;
            }


        }

    }

    boolean isActivityRunning = false;

    @Override
    protected void onStop() {
        super.onStop();
        isActivityRunning = false;
    }

    Toolbar toolbar;
    boolean isVetting;

    DrawerLayout drawer;
    FarmNotifyInterface farmNotifyInterface;
    AlarmManager manager, managerToUpload;
    PendingIntent pendingIntentFetchLocation;
    PendingIntent pendingIntentUploadLocation;
    CircleImageView imageView;
    static TextView compNameTv;
    GifImageView progressBar;
    com.github.clans.fab.FloatingActionButton fabChildAddExistingFarm, fabChildAddFarm;
    public FloatingActionMenu fabParentAddFarm;
    LinearLayout delQLayoutHide;
    RelativeLayout no_data_available;
    String json = "{\"ciphertext\":\"3lmHmNTL1OaXfBet31uFFtacYW9TvbAocEgECtFI3cW0GW6QrLZldnPhyVkv96rYGLSDOtfdhEWSWFrSqemLO3om9\\/NUwGI\\/kL+vyIw11ZxccuQ5h5Q2lw2xGC80KzVmd3pxJiJTeUp8uh3RXdVGy2boy0tbW\\/bVYkCTm1VIwjwgT3irXw7ZI63R\\/WKUAfCXbRC9vahP\\/7mqnwtGAXdzUh\\/4vFmwAbKLgWrAQ0bI1gAroOYNzlPeYo+OhNTdSfuQXIEuOUftxKLzuig\\/VaWaOyf9IhXB7YRGZM5o7\\/w9kr8nPl8MNC50OZaR\\/gLFyki3C6T195ST0dXIB+WyjcHiXRj6wDQbcyeDCQNsBmIRE76Nh5VdDIJptX6PKEQ\\/HhRaNaJd3hcqUTsT4uzv5y\\/4Kuxx5WjGBbUo34PJk+ycy5ZEmqGR4u\\/ZxRpwH4JLpeC0tw0F0j2Tq9ZgbcWQN771szZW5sq5JIntYt4M\\/G30RPPJeLCNoYlSrDUxqQeGe5j0kKMnXQo7kUUUUgSkH0YcTBJKH+JzjUqqZGw+V5Lw7x4ciskznbyOpp0EV7iP9jqwVUZCAyaxTqc2x8IL8DjXJtn3KL34nVbGD1NLYHwWcYsVHFMj6xs+pobVCqLztzRS6MaFa3+V1NxyT1\\/OTog1r\\/gJfqHvCEA6JOScDw8902ReQ73Plu3naeTZt3Wjg12mn4Oz2MB\\/mPc4K6aVhrP5JQ0Yo215Ysv6iU2qa3xXQjpQ366xOgVKKbwtJ\\/+0\\/xuEVmtsg7Ll6fAZJH9\\/TBxyfnLucLe0JJM0BmudzVIhlTfFRz2\\/8j1CDf8PcYEIFJjTG17RKhf6tQqzB2Zl3BNbAQ1t04Yl5L4zUjGQF0OU7utjJktuZqIPsZrvJ8SpspzEjG9r1pD2UG1MRDXYBRUFcYOX0ObjjH8R1wwm3qK+50mZGKQtWv4b1rugWD7yXV2kAVeeqxa9ftlnXB2JX\\/c9xEFOzZN2YsZCqysYPC64Sxh5YAlV8raiaBLBE\\/+p52+UUtTn5s45VYY3MAq3rOqlt3O9XIgKqcmwE9n+u1gcRCiS5r7upt5tqs1OGSVkUG6EGGFrdjxXEf\\/YACgsrxfg6TDAphHmrlSPYdtpKZBtMjC8PwPIy7ikijLfBf0w1J7XzYwt0\\/n1cpG3bqx80\\/Jjakk+aClJxIuZmmrAY0UpmBckn0LLkYaG6O\\/IUQwqXQ\\/+ETkQlK5CRLTJsVpApmGYJVVcy20GO4Kn4\\/T8QYtPTY5UsuKIgzJQpsYN0dmFGrnqOZfHTed8f+I3mp6wFv5sW89+w2eAoTZZIlf3uaN7eOrqbPQuphA7Q8yAO\\/65k6OJtV3z4p3u8sELDk+Jd2gtjWGVaEIS1xc+TDZKA8fYkHMsCeTs8amSjRtaOhq2HJeldXLvOq3BLl\\/QpCLmVEHpvfbZEydPu6jXCaAzWOLyGi4vTbCU1R+e9OdSOONYw7qMpYsPWHAyibiFlx3qzC1jc4NX7tIsbY3P4iUIw4s0CthmpJpFjbqLH1R6POSfEUDA7qv87ziwOJGoMTYyCpZsPy\\/Li2q9GJXglkjdSxBDTlrJIRigKxYO7l7tFF80sGfu7D3JHSmZRM3Lc0rJGo++kp2UHkSDiuIwVaPsvNimnmDI9OHAlXMvDu7VKmIh6Zw4SxbiipbgVJd5gpgQt+bw6\\/Y7XWnDr5B94gPimiNWaoV+jGq2Kfcs7\\/V\\/JdpCWIXsQAXc+IFA415yA0HB8ai1XXXoageDXjwf7wNcA2cfAxP0VeNIKVO+L2hPKgwKtEV9HklOht9rf\\/2BOl3Wf9SUh2nimUkyHS9m143SM37xEsxtOrEqkBFWRTYa12S6a9uHWJcFmETcggemVrmoPex77Km4\\/yVD7YiJNtsPRFml\\/Qrf5V9Pw4BuLflSEI+yJuGXWruHI3M4Qxajgia+xmjKXLK7MmvcvQbz97S4aLdeREUgwlDpT6evjr8loyg2O+NwFqrWUT7XY+XLporVUeX6sPHCILkS0JL7d4Zks0MoU+AGkh6lBkyjORY23fxPPQ6+gBE6pUm8y2LKVlTchqt\\/jIiONxza3KqK1vVedRRRoH0bGzmQeh1IMzQq8zZR0GaFl6k3rEK6cJHv2TY\\/Jb4dTcUXmUZ4Fux7ynWLjOuWzeblBNYIQ83zAephaivsvBcUcj2NNhjHY3FNxvKdDhEgF2owfGy4bs27u4YfUAYz94P61fTqPFcRTMhLXdScbHEvG+xFTq\\/bYxUVCoIAXc1vZ\\/aXC+JYh\\/mWgZP0PHBSL6i0KVtsszZkraxLq+r\\/yHhOtpnrLCfJUJezJlxlbkSAGgmpA6bQfYKEvEAugtNGJ6Re76q\\/Dg6zU9lRAmqvL1\\/PY0p+yQOTnOMEgrLMhgAziKVT9nezrU+1Fi3ZwvWqUpEF+KXBckKTpz1HFUHMjRFyQ9frENwj0tsr0hdtfJVe8\\/XbGhLfCr7bl57m4PgPg\\/YFJA2bSVa+bMjnSa8Jao2dsMmm+cLBrLIkI5nIyh8Z4cfeSWXbSTTGwqdFmWVD6fl7HQow2ylB+8\\/A32O4RbL7Gtcp4WvHELu7nd2ChokklL3RoXFDGNW3uZClCxobialb\\/B0DEHNukhE6RssIqZ1n00d2t41iT6rvlqO53D+7juCBBlAOu9F3ZPZdHA+TarF4KBitgWLTxTYJEjx8UMI3Z9w1i5e5sgUCpwYrVTNzxBeaT+8R9xq+QTjVHxSYoax7CqnBVpOyJOLXvPe6I3UgDigrD2eh61z7BFY3T+TQLRQ6+kOaCU\\/hkZAFaWS5X5HBTdyys67uFVTRnJY\\/MU3L9cDukMkSOSsboUkpJZmp8NgzTqWRinkGIJJX3eV\\/xriXq1khkSRQlaTFZ4DjOflCbx0fk\\/gNQEIxVG9C6aBC1u2HjQYwMoQRMEMDGtENelhxKte8cukN1qbHTCKoqeMss+PT5xqd2baOqfTKFQIzTerrjbcSl1BhELYqzaJYaZcPFinryvjicq1jkP895s7UGdngFeo+ZfcjFdaTAEd0XXsqAi2Koj7nDlK1IV\\/SB19k2eoAint4GW0FBkhDY+fWlfnE8Jq8VxyFTpGtSVLGmWd3PdbrbLtQd\\/u9W1a3nWd1U4M+Ld+znFmWJRBeLKG7l0Yal+bom0LSUiLOWhKMKeITle+2Hssh9NzszEGxJZX5+mYrj06Ph6tUYGBsST8g0yspUh49xOqzpoxdjI0pRtmc\\/skIYGt4ADbYAgDALs9IUOUYCvuUrtt5sDa\\/F3lsigFfKiKkQaarW95AMH1nH4CY8vuHmWDMHqPo7yS2tqpOio2htk5vRKSvSOGEpd\\/wXSSj9ygZN6kgYX2wTKqICMFK2JIW5Z91bJVbKPVVahLPfuYUnCGP1a8413SooePrfNaq+UtNyldtEex9+o5LKxeLEdgnKea4Sv1WEsBiDAkqP9vHAOxGchYGUBTuo1mlP9FY9tZpUhQ7zSs6dWv5w41YeMwsXjAVvQV4IJNkxq9lqDTbBAy51ZCPgju9JmSl3ZeyqEIRRahElZWW\\/WMkE+L0Zh93nng1l8abJOhnz2lgMjpSGmHUlRSb+tftpK0FmWH1kiAtdZzjPIYBvV1BIBzZZ5jePjHpCQqymmc0MCp1B\\/Sh4XoxLEKA5BVblZOUbQjVPHg9KCEB8OLEp6cko6jwAZLCD6eCybqAm1sQuR71e57kMRDu4x+m5QBHkugKrp5UVZDXAQR1fem\\/8IHZ04Mg0g4nk544hWntWBP\\/Le5gF1wkEp8KGjq5sI+TGpYlO0i342o6NDEzMNr72td7i5G\\/\\/wnBu0q5DqtYYLI7OzmX8cKXfgWxYY86hTq0xakPunrS\\/5izjtyFMmVrTMUoy\\/15NfPmnA+HBTrRiC1FLILtO90nN8piip5dAVnmQEdnOjh0VDZmzsGVXQfd7dBn6IT+OhhLA+YRHYbeILZDxjk8PaR9iqtILoBnYg8koXNwQQEY40IO\\/MyBiXviqnl5rp7ZLQOOn9idFsGRbVggT9KcnURopcWG91OTbHqMwquyTJaCW6TvmSY33KfUVWGVAzxl\\/SFnGR2apt7gKv+45gepddCUXVgoaMIVZNea5K4cDURTwfPHOiinizccOhQD\\/GrvELyJiXg+3msaa8MhSN8Wwjb1m+6g5jfTt5EHgr3Qerl6lXOh1MDMu+fcCiivP\\/krd0E4lk5pRd4Wc4AexmXpAkmsSoyiTMgbMyulnjttqVBtUuL+ShaZTJAAzV6+KQPYvvICT1XSxOZAv6XbRvaN74QCQxDhk1tFE7Bv8hZ06d+3SZa1Tx6+yzFWKQPJ44PTjzl4MJh5Waf6DlfDZH+n3\\/wMnGWI9J6rd9H9F4lGEEYb0XkWyK2gK5A1AK9zkVq+9dUAz8XR7LTcGsu9WOMJBVPpr0SlYrf7mOMiJfiryc11m\\/zY5LBbtd5IzRYfOidXzEoin0P8xhtzg342Nv\\/4qWEg5zkFSl33MjEnfOLHuLzs23DDZ6tz6e7Gfyn11W1o48qCOen1VWkE8dutqfa\\/WBRoMmUDUDu+2tuD6WyFnktqGIS5gt1TwpUsS929VQKZNT\\/73dO+ue8nAV6V5ipKGCIpRJhsjWgtrJKy7qB3wsJB\\/+e1cVzNJV\\/RXHTP0gCt4zMENOmQ51X0RXy+uOuQegg+PT+\\/IrKD+NZGN0sEHXft7SbfTXOBnVcfcgWJNxigZlyYP4toCDLshIaY2fjXRmqJtWVwuMMGp2suHa2svyzfBJNEQQnpvlcyW2UEygj6rEcvdZR+lP26UbnE66GeDi\\/r4Vb1VmITtY1jHmC7PfMJtPucaf0Wpp4fJdkS4XtNJZySHRJdMLKQNalI\\/lMaA4yndLVolNnrVhwkrvguDKAGVW122RVjOhlOXS4PXFMUiV9qg1uq3o4x6KgbfHitpBTvF9LP\\/69UGms7ENDoIRUGMJ8FYth+o6mrzGYTqXCcnEgv+FY8Ip\\/bwWaX7dGLs2kzoRj56PvvOI2G8seAgER50UcQOYvJtrUJftSMQFvorX0ku9R\\/TI4IUDQTDo\\/oV5Gk6Dk5\\/HOp62sMW6EqNtBAtxIBlOja8UxZbqeuCTz50wNLfnlpLgIzS4J+6rRmRoN8OK36RwoT3N1YDJWE33F+s46e7mpCIMUfzcrbciBuUzzG0JeQXvF7BqFBF6d0FyC4oXBf3iMnTIF\\/hyziIks\\/FjxNmD7\\/cwtoy0BIZ3k6ULaS1BawtulIZ3sTKFuzvkuI6AtYc+Ngrw+akiiugEFLdNGP7jdm1AbIJjaHlGW\\/UWPFaOqprfrvemn6TmOcxFHTxV\\/f58OwNO9HdcbV8i53UfsFZwZ7etMwhJgYSH1IY8YxQxviSw4Kpa5xoWwxjwmLaETzYSxCk4WdIAZ2gcXSOT0nJ6fDBj4cvG2ILtFn4TJKtRdGs1hZ40TEWqmK\\/vdyvS+0pDBBT8gy4+Z43yn2gxW2IayRfpB08e5yO2T7MCSaQ32LqxGkV5RohJLExi0Mpfo6Jkr+xF+EpNOILdaDR61EvRBLpOwN2qZ3eRWXd8StKI+1h6Q5aVT0PI+IQFl7Bl8AViJsUwAYpaZMrCRb8KwCwBf8y2fkvEYlhgw\\/iwEoKJZFsGGfJsrFD6Tdk3W0ET1+uaodr\\/kHlp34UvIkim8aN+MGiAyKu+pvvRTAH5Z+sUcQ0hD+IHUbxHnH7tZeadunrRobwZ5LVoY1oeKQPUOI6VG4aLrturF7SjeItBlYktYkuQN0WRDbx6Y29fJyI96nmvgADJzJcx+A8VCGwCKwnJ13sXgcaULOhqniuQCu\\/JWM57T8JGXKI1p3QQTlVKLqIWZUtu1D+DEJ\\/UeLnczD6M4bFVU7uHFeRA99aa3QU6VykE+d5SqEEIyjiAsxNi0\\/0ScOiunKZugFhqRuTCBypGv82N549+FkblUjArlTSfFPTtGb6VA7AL5rPN4xElI+k9SxF+xSRxb5gPOF3HfcAeFWkx4hugvg7MbDs1NgD8EDHms7NfwguwpA1tCGed1w53HNY4aQ0d\\/MjTW+FYHA2B2covKohV9Ql2oYycQqCTbZlJ5VCw9\\/WMgqUjrMqeOYUdt+1DtqbSrTsEdId1MIUt+9nsVTTY1zbri+cSmGaUGCxmJmofv7fT8D3E6ghpdQyu357U1HH0gXFD2l3W0PzqYpcC6Qu75+OT23kLPbOUKsqql\\/3ANQP9N73msjiv0KIrsQGPcquypf3zGPDDCN3owajRskIHHRlo1NQNKG85dsvDyFcFKinIWcq7Nolsz9sDYAEcSE7w9OyD6+6QSPeqA2xBDK0Pmdz4cKt15S+DYZmXYvFyLALN\\/bphsc\\/+TIJCZOVYZLkX9YcaMD4HpydSOwvNzcywBzLRLpAbxgSjI51MZXYo2CxXnEwnoOVwSDkuvlsw1xwLNIBF77UIbvs0pEMuI2We4Jtus0wyBjwXvZQtS+wL0pxNNMgXRpvODdcQk2qQBhQncP5g8+98jqvSTSUzxjczrwhzzqf9p35sWasuDbRdPftc8HduwLOSKpZliJU4RLO8+SYnaWRX9QZ+VmzOppV1O54np6PRgWwBkugtDh7SCc9pur4kjKGWjvIAy+S1nlJz2hXsXKLfiJtE6CPAsccbjGNAKoiydblrBd1AM01igveRUdWiLg\\/bsUr7FiHCzH2PW0UQC+S61xqyj3Rf6xISkTYtQvJ6gvHceHwq3pknlGmwfaojUO+7CUSs\\/JBJnOr68ktTVp7iNRlW6mspl6NJRZdWjnKlgQb8CvmNqVx2RNI3OVgzSBK5iLPUjrwQwFctrCiIGtoquKGR7xGD\\/xae3VGrHaG1KV7lYZNGYwoTNhMYXj\\/dMFKIbBKf6DF4QuW7C+TqAUrDKQNpircw5QZjwGqXd+Zh+ZwxGjbeOFuaJblf\\/yRfZyqYYAAUDDOwjURFOqCYAZhhegLICqiBm\\/2nDFniXUWb9Oaz\\/+YAdFw2rMZcizcJjDZwWEU\\/ciznSIafVn4hYoR+vKtpdtOUORAI+4GronAj6bFMu\\/ppFPgBCS\\/xfJnDin1UFCTlR8MtepJ0Gbt7ukLlXRPTba6Gq8+mm1UMDz7gPawc\\/m41tOPajGm1QI0d6iQUtZPTzeZ1r689MF8mity2170TrgFuY0ocY7GDj3mJAmQbmbLFcVoJkqfm8H56e01k5XK47hBu\\/4tNNdd3TzHcs3k1hLPAlSwOiuJWbYqXaMFckTOPY9yKEYePNSwGzEHP8xHcBRnZuq6psNvLwpO4koh+ud28rcOuCmFG0nH4jE\\/dUQ495UrNE67dObQIGSQwnxiXDblTJzfktew1ZwrWB9FVzYUK3JuSJNFCfuHg5hsn1HdI+cGaxWCaPBDxYhieEtpq5lgktaee1qrwlNmtryVCfAIxNzBftJyVGId0NKcqejDeaNr2EvVD01ucTDC81GSa+4dgvyx3Hs0C5kN8nEYVCZrQeU8Qi0ddG4hwqzi5Pl8Ak+EYN2LLAJWuGVuhycuvwCGVm+13GEXH\\/f07\\/O8GIruqqaVn74rvI\\/AzHiiqthIKPQvKN2pWFwVcpZns9x\\/3i5UZ9UA6qDq2tYCCvI6CWJryr34EV4whgDyDeP++2\\/ujlRBx6L4\\/CsQMDTpX8ZeYbiSSqIi6H5CTtESsT9mgBUFMD7M46tyvlFQm9kEbc8ZlluJVIzXAP5JRYu2GuKu\\/ez9CB8theU4hROHYxxL5fKizJT8dRPaJ4EuNvsEFcDfVXs0I52PHWGxsUEFTkSdcuZ3TXPuQZ2gOHrG1HqGUWub8WwziOaHFg1kW0Xq7xE8MGk18sP9O389mzYdx7IhROPLyEr5aBf+qySG+3juTWULBLdKLbsuQt6PXUDY+6UQN8EoGEPKPugw7oyqEF06ZmVKbM2Q0GlD0805CyA\\/HgEhvlLuF9KntulIz5y4JNBGmeJr7SBynXsQhppXu\\/VkL2Cut8a6i3jBdqbF2pNKS6MXLrQKtozAw4T+sdpFv7yZ0ibnY2H2fBadG3abV2RV5s+VHH9r8MPKMh2QHmMzvWu\\/buV000PAVIO1loiHwELrmPcY\\/PEA9cTFMkHHFBhPWQFUQJHWBVCasJv0chJicGFa+gJ5\\/cEf3oY7IrZtOrGauPq5gBYcLbPDemOhTmLbUkZyVqvwQ6D9QYdNN\\/6kvlEtSaIejKiEsKcte+\\/DDxrWQ0UTVlZHWBweTL76npqle4BaLmBkWi81MnZaEI3Xx8bj9tHM5VqkTTYuFnq1CaN\\/LVnMfw3dQQ0NQuvMoN75joyzYLruFU9lvqTg8cXD00WmPw7PhzvuRqIg9DTQrwkl+G52Uts+arNmEDD05WkvKOGJlQ86eSA6QmKGNVVBqU2EgJmxVsRfvNHX\\/2UvRnJXJzsPGi7c34UO3NGyVVQxmK8AR9D3OZJaQs9Zg2gYCN6naif8Q4zZuXyfTnBrg0MnG\\/xNvjCmaRDYrSOSsGzEyw\\/nKawxSnokNpumN1dr2BiCLOvwAcAeYtN3HLdgK7rWihPrqdExb9IR4du0iTTVtcd6wZWpc5ouNy37TUBUC9e1R3MrQGsj1lpXaoQnrOjYlMBE2XOx7pViTMyy4hSa9x0wW2H0\\/QVaZAK9CLz4Qhe2pbAAWe5WWu2QSjg7iOGXsERdMAV9TTeptpN\\/oJq7mj09YQubyxH0iLV9IQj9dcmqQLkySjdu65o5jXGelnzbKpUbjrXt4Y4l0e1kjdWmKKP+AqhX2LQLG1ogFO079nvxzIdLm3rY80uCJDTl9CK+wpBuD7mGtRtL30jlJ0bUNxMEhk2e4xUtv2eVpS0e6Tl9XvvA4wU\\/d8rscRE4pSHaX3rkq1eLOTqLQidxPFH9m0AxljgtxGobxNArdM\\/kHLzoqcaHvYIouVd0IvcVzlrCYVhEomDnYlPcGZ5E\\/GICPz6llfEOHUVRUdC4HtXO\\/fp6X3NUkiSgd1e6Agp\\/StUrSmvzxlvqkA+qt1K83csRFzGJSWc9GMKGGHjZR8VNtcsoxYLk94khG9oPiG+qYEWwli\\/mugJZbbFyeT9iC+nIfKmarAdYiEg411SC+a37oD10ZtDFBaXVDP7wCSuxv+Nn3OW2t4X3TpPO0N\\/yoOUBNlSkTZxcAVSUr3e+\\/xBpAJQnups2d0qQk3Th20Sc5oDuQAbkSHHuKmndxkCwRUx7F42dE\\/EbltIznoG0ZXtZX6HWZsNa7wkI+GmQAFJRQ+tRnYXxlB824khisEWizUeKTaA3yxDmYtbVrs7W1DyKVCsFoSgqyFMDdth1q\\/i072aL\\/nwKbY+Fg4BWRP1PA7SfguxK73UkoH\\/PYqKxVIxJtlh0xzZrd8FRIfPsGr5OeOIUCAkSm4UdUgDivoVaIv\\/vS9ljsvjQu82WwkDyW0WnIZbGx4Hg7swDFHhrYagWNtECU578nv3Owa0uG4V1IFPWJX4K\\/QHnbGChAPTM+hvqMqrxMwiB4flfcQKYcc0yC9++FvIiFulZQqxEcp3wWNyxaC0J6Qnt7P2lcndwmK3hQBm1YAdm7XVgmgr2jz1L7oj1pivyItq3iUGQm1\\/AWUENYjitUSAoJLWNqCx6fXXDB\\/KlyL3TJYPcQNgCGx\\/miYnXRrHLpHDDYeRH0AUXdUTqVndYxDiDofH4QiV2Y6YMhOjFWoEdvkLFe7\\/NcaSqWa8q8bKVirFBmSqol1pPC3c1c0O6Y9pQzgL1cSwDuMvj1nWWprjksIGaALCLz3fiq+j34jDW6a9lNYxC17Yxq7P\\/6LZ8dxYBORM0ZpCl4Wazhi3CIjEaMHUdVcPLLIQntnztCIc4KydHvtggRRDNWzwxTlQIkd\\/WhCBNfz\\/bxWLI3fxGTS5DGYwQ4POwOijbAMxY9057BHTPlGC9mgNHZni0ARGjzxvQBWfCfgDanwZrCZf1V3VSXb719xAYDAF\\/ul++CLoqc3rHXMvwFFeKef71gTrrFvXk\\/e1cXyZWJIrnKuLkZdwU5RlJvvMuFw6hg2Xi3XIoM\\/Yya0SxpbVCE9548G1zzSuOrx1kmpM5gHSfb\\/B8oeerenq6+lh1+GTlpbUi4VP3TPe7cfUBrJi6dwwlnDGEbkGejLZTgRDHlLt6fx2ccVU9X0P7iML67KW8YY3\\/S4vJu+zbng6RbxO49mcWuq3g6nUWkUoRkXfpaHayeLqL7Nil9udyAPYi2tU2yyAdbU6ulf9gNGG\\/Ok9BK5o8VRxWUm5HgCwz3ECOl3fYldLhP9vbwAI88KAVsur8d2hbGJeAWBpaMJm\\/ILvPE+lsTiete84yGQuNI9u9slmXpxJCBIbXmvV+R\\/cnT\\/RKsJTMReC+uiqzAiO7\\/PlkZz+ZlnZe0AMeGM53h2KMxU\\/kFnmEyZBOx6iF5BqbPNJUEgM7bp4fjmo\\/HCMHO57wXohKAbfx6HeLpqc4Eei38KN3UxbQCSTz56MIF5+8NEWglBuhdW2+wa4Xl7gtGnbN3GW9z+NnHhJDtNgp3fqIyF5UxWEnuGtYUcyn5c7AbXtNLSpXsGyH4Xs+fkEBJ5Boz26lrajKW9x4m15rw1uHCL4cUlFaHkuXavAl04nAgZTrUzws6\\/IqtPonr+Wao6cQI\\/LaAGFHulbPET0hFTNgWO+sVNd0GT8hH1HluHTsJNzPXm2MsT\\/0tfY57LiE7arn6sAkbu9l9JGNJ0F8CKVEkmA8hhJML6ipKzJTjwP1P8RZZv4ddzqc9vZxhzoDnh63cbWkSmNFUT4bta9BXxcrpoEvagKKeK5GORkBj0uqyrWlIDKGBtpNwzi2UB+DtwM9TIBf0wlDIbjZgZ9F\\/6EvMbB\\/fHwee13RcqIaggsPfceQvgn1DfmgJF\\/wkEPlaBjCtBxKp2JXs9WHm3D7ye2V9I+sTIhA8gaB1X0gJ2N4TQZ0KuAipzvAYx23chhDdyTSocTUaPZ8uz0icmuEjFM0eoPwP9grtxiyf+83OsSG6rVF5SikleGfHWq9wPBE+RfPhDgx7KCVKJumEpksmCThTmcjCJxcv3Ar7wTAY5Dxl+rqMP10gPoe6wnjjhw3\\/c3+MuXwVE3JoaHYHsSdbZhhrUJQbbNRD7monYDnbxdxRxhciaAgPQzCeBYTGyU4YOyTTJmRkdAUuxf970ChSMDfDItNWngQUZKPt9GMAAcvj40vgd+hhjT4nqAYirazOloX1IAabtHOwtMP0cYBW0jCNc2K6q0lN85mdIykM\\/aHW2u2MuwKY4CFaam1xXU9MIHlFtEcMkGTvJ0SSze4jzajsBSCUnzyYzdW2mdGceSuDcOlBQNrEErLeXLYtw630puRkLIz8oUykhdxtaJGaGpXcrlOnmCcsiZmLA7eRcA5LEFAoOmr6yj9meoKuUbaGEn6mvdeLIJkEdVFlvhW1cL2OEDL9e32cfgOdmNx8Qqs1lxcBBkilZOUecW1owtzbTJ8Od3DAs1TbX7K5n\\/xeoMaVd9xgfshkhTwbUj2McmuA8d9p\\/DiwCtOPbwnFfw54CtRlLRQepj7DdZxCd6z4okau81wxXNyCHeIk6oKauJZzdc1HBDccYAqbWwqJMAIh6wDUt6Eh2sXsvo8vDSc\\/leG9+G\\/UYEP4MI\\/lf28xgyojWRiNEy4EMDo0xSPMUpM1xU1RPVDTw\\/vaeMBrhjDXSzVJpCFnCd4LSx2lF99eW\\/b6JwFU+mV6RF8xIeyqwerzmdAf\\/KMG45PlhzMrcbEU5i6UDiH0mtVmYGKKmRziqevy1AaCYyD7PNOQu\\/O0CsvHh\\/76LRKcZROBHtQ51ys5CCRu6NPTPFc+C1Y5YslkcyNMYNbHa3gHYuDFGYodsUATyXw4RpxLTUnmRjxqSJGVaQY\\/nYbIsTvxLOzdm+q7EZC+SMkhqRFB4yCwh8IxP9JSj3AM3UFG0ENfu8BuLTOzeioY5N4vJ2xzt82jGqQj4yW5+ZL\\/8DIA4Xre0oAVdD1P+TV7HJzPzjGI6TF6fYUBXipZSfBOjIDK7I9Rx7P2WfA4E+j21klAiwcyAU0S\\/JKWUPxHL+nJStmGZ1V+f0IOy\\/m+LWgqOjbHhh6+gMBt0q4RTe+dAi3ygKqsPHD6bC\\/XVD5ZG7tGWtmLsdcQb5wAKQ\\/hebsx8sGVGdOuVjuzOULSxyLNIvZzUdi9T4lR+5U5nnTlIk4zldY61NUh8apyomRo9KJ7FXGfU+bqIhsbKpLTjvZWxeMeSGJ3fDWmAVTF1pnLzixBbioV6JV0z3EJy8DM7\\/XNomkd\\/Ijq3Rb6ulxIlLRwXNsx5tPUNVsE\\/N9TIdW0uJ8xUvmX2iV6WgJXRTLO9nY392h248Cgm300wxl\\/i5lFpAXKryYS6Q8IQT68TQstJ2Y61cR56\\/7m7UVSoXPcMV\\/DQxDZyI7TyyFYMbp9QXfscInRF7lGlEWUdjr7mFMyPVQ28uv7SXyi8Rs+wOPchtLIOb4LOtejDt2DD31sBEG+SCbbBHvMl9YDZggPK28ZWCOVPPEI14RlDLUfeOrrr7FXOywwO6ohWVIRwQHqopnoQSQXhsnbEZizmYxxv5nM\\/wHCu3vYl6cmrndLbZr3o1USJrcMggj82i0GFboBDrr1Cgq7irG3HctAyVzC4ALoKhoQIdh9URFSNb+66B\\/9nE94wf7fOVF3lHHgp2xbX8GxYVn2pWqGZT2Xf0ojZ6Me2+9W6\\/9TOjW9\\/MlDPK0lsILPSq4ptFRsOo8PixtdE0rWBmmO0VT5V\\/mmNWSxGVQ33wg4WtOwx1kA3Tuw1ax5rMR67mnNjzrIiXDzOwi\\/P1FVx9Rd+vDl0iwh1VsbQMbMelNRQFyo778phpeG1Gv35QtgZSmD7PBItb2iWTyJPtbs3Tez5Kp\\/KQfn5P2Yvu\\/8RRNqczl0njdaaOb+h93SdAgqZr8m\\/USG5SAIN74oN++qw3QF8p8zrxv8yGWUPTjN67DeJ2cpdnsY3UrMZp3GZpwVGwnB73bZ3Q\\/\\/NWoMfkto0Bqt\\/JJpKLOezBnp2S2zLwT3hMUhgQOWNzbE1u9OkknS0wW1Yz0MNh2fspTDc9ObKit7NPrPcpds6w6dhTi4K0qbs4qOlfMedI3\\/p4mwwD3ivwtme+bW6lR+7TRqpfHu0cA+OpKkdi3tW7Oj5HCTQZVQtgVtuKvesntf7HgQqlbjk99645eFjjbav\\/DyI61+PYJ3TgJ8YlHHud2ye\\/XCHfER\\/4eYQh1IDPRJQpmSqalrBRRUmHOIyJZfcpgnHNGJfCkdtvU6Z4F\\/1YbKVF7yVensPbSg7LLBMhLxmgrqKKEl6ZVs77Be8y1YhCHDeMugMYPkFYWFZWJw7Bei3aLheTjMPtJvpySEcBnVVmHXKD2gG2MTIiKrdLv7d+wC6mnlYvHlDD0SCp23bhF7RvtGy1MnOWBwMcSbZhCKhzN06TzzLWYz+xokbbedQxh2w++4xJ1HrEbbRokYJVvJSiWO2\\/kU91uHCELttq59Nf7ewR1++QNeWLGcNh1xYB8128BfpYaMhUnsCZO2x97XhiOMfuNz5yMq4oMzzmxXUSBpPO+6vwXBTUXheusiENKumPuOv0mhAr4\\/gLId10R1Ua6LMZwyWlyAFsGg+zaxKME182NcwOSzAnyRMDEWBZCzPd9SYd7ixYQ6oRMBoYybbzvyukwK0rWi04\\/IE3XLrQXEV35m9l70SIEhinSGEAB94M58ZULMtkmI9FUpGz9EDpbzAQcNciLnt7iaEV2MnfZdnSMcd9Rv97jkcCuvO8U8ha7\\/XcJpCI3o2yf6lq7+Pw7N5pa+kEJSnlLkII3pZ67UWjYzlzT9zh7mHN5xhAt3MBx7G\\/ttf+uzO4Egho2IMmgu+FXGF5G1lzvuNkR9RhK0IGWV4WAVPfIEqbcPlqpjTvfNeZms5HQVyKm+VPXUnkfCMME1zOuhoOVp6B87hp+jGZFUnrtB\\/Qq7pI5D\\/uFTQRg1kAuKVJaooHYSqZixnL5IxZfOVpbyAaG0BUzwDOwN7XVklCAjpNVN0SAfmzj6Mz00M27dTf46gMLdg62iNRodU1M+I5E6OLwP0LTtAGQh7dqsj5jBi2zgvzvKrwlovzOlrtU6FiXBvq443AJ0RC2WKujSdjBGMfZve\\/kDOt+0\\/It7\\/7RXFu++Ny7H9ddv+zwbNAVeC+mH5pgw90AhRHq6alP\\/2kn\\/s54ipyfg9jUh2LePnKg83MP7LiqSxUpKcU57QaLRNZS2CE23afNruO9Rk3rQm0ZV1qm1ixDogrCKddiiezjwHBV3QHXGSq0u5Pz0jexfeeW\\/kDO6pJt3efKEvJx7nS5eCa8QiIt4uBOvRuLEldoHTcu+1mME402HjvlTsRw2elF\\/RdpdvHJdH14reAmjJSBLztnD71w3KL52iBXoghGBrTqjJ+RC3FzTgKl\\/krrKXBXMWlAK50OyZuIvtp0nNyML59GSbaXF5z0+utSgZROFEnXncjouKF7NzRRSbi3YdpCY2Er0y44FAo6SzjLbFVTkbEfNkKpqZAihq2oPrQvgKP6\\/oteXbv6z9cXeBqcgYCb+wBb4rKvPk3VbvvNMEri1UTM1jNYVEaZj+8rxWeILQqeMnSapIsAVqypWfKKYaKLHSmPGBtlwKX19wJVjjJM7nDPIIAdNOqW1a9mYM\\/Th70BUxXU935KoC7wV1eRfNHUgTYd021TRlv2uC3qa1x5ccSPMt3p\\/CerXgFxjMwzb8C24AGFxrfqjjUJLBMDAyMNIVLMCdfyV8JdAx1SIC0Y9\\/A8mny22mWSRap52MxFmiQ\\/WgA7psPHUVSUz3xPxpADwHWs\\/h9mJyG4KhM+x4EH3Pzf5kp5OALekO3JrIxhGiRdHosk8iJJJKzdNU\\/HG5dyzqGVrD+xTYzcuKX2LJea3GXY0DB8vT17k\\/esaQNU0LvL5J0MhRqrzjTGHITOvrOgdYAfYJMTP7ABY8hH+Jqw66CxvmuuYYcHrUAgMXmC+0CxUk38vdyEMlSNnzEtNhXFeEZFTc+8OvdxTKqMtKr5wrE7omzBVB64Ur62DfXH5zDZFJ6xBwqVl1+PW9LFYvdWBD9Mx5bq0wkFRsT2qzKPmuzPuQ+FXTR6Z6WdG0SbDLmLfkHVYO9x2uzL46M3S6Z64ibF2djidmAFC7TuhA6uGa1J1YXfKm6p+rjeAfLpptFSY5\\/jJlZxiNQORTLcCQXGGDKPs+ZbqN3+rUU1zjviKzCL1wrKNltkHOegExyxRi0FqYQYZP2E\\/GMQu6ueK3qlYMyHABiJNYM1Az7Ddrlb\\/9b\\/8zvr4zKCSTcBwOqlInOb6NV8FZs1o6jbdcRDcLEf5rb+OgyTOOupKGc\\/8ykWZMM6p80fsex89Zw1IPs5R0+0Jm0B7Scm7YZY9wDiyx+wkCNpbystgsKzxcHNOkTWZsAKodYYZ9atL6MuBzhu9X0TIadvN3OC\\/AnorYa313utKFb6Zgh8ak3t9K2inqk1CApkpSwYsFkGU7yiLZLikd8HN7cexD+J3jGxUMcNblZIDFkpTmzUyzLUwlE63soUvVrt+HDxbj1t0E3muo+W2mRF9sARGiUMub7qUWQbIiodQ32VuucZG\\/aLEFFiEJOLtqeJar9eDR1pUIdEu8n4aUuVyx8V8Hfizo7QmWwnfAGMY3qA7Ls2Gnm7tmpOlnLwCIQJDf\\/Ndw49ZTdtsjLk4CRxUfJe0IC+bX9u2kJBWbVO2BoNhXzFSjC3ys5aXigEwTxgfK2ek+SnAjrRdsSziyx2151ec\\/kPuzkm0a3IFwM915UN34Qh5uuxZodyWmEMWLg+wqSQV0Cd8G1bJ4cuAULiSU8ZgCgQeJueXSZtAHk4g2GpSfd+Ymbsd9akRlnGM6emDIu9MpQLWFVeMEIBMiBhxzZjiMP\\/0g6aLhgSCuNm4uBh0ViTe+9iTqayzM4ARCSiXhdmceiI1VmAe4CURFJL3e2ibOXNAEy7jiIP50IG4RspuUmwS0D\\/tWgTe8QzOn8s+kZg69nQzaO1AQ8ifibZsKYxeQA2lGdqtGXPKSEIQxaxGBW3ExubO43X6AYfNUsmM6jEZdI49dy1cJzKGmuACfsbR1+c7+mL5HPiEsko+ExmBLYqU0Voy+QLX9Bn2PkO+4\\/fhc9Ra89BrZptDGLpy1BHfS+g47OVfNW+tK\\/qSC7me\\/UJUMCukNpV2LsyCA9Y42F6hML4ERXsG5xUVpeiimq9lytsxnjRrph0FvATASFE9RioqNj+G6+x2PoAdEkW0uYO6j3m6BH\\/72zbslgeFcY7gGwd4OfgTMUoBbqYt44y1o5pLoYPFzWE0GEGs2jtoLsYeuSrVd\\/cvsLETdgeBjp9wpNmQiBH4c\\/ETpXl3z90\\/eBunT48ThASge2XdryFZH1vLdjGIlxhpDZAxIxUEH2f0E+j8zm300rfeRfuBGT\\/H0UCqSGFI2c\\/sWdosazgd2jLZ4Def3ByMJHreAWXH61ljcPzmaOxuabptnUV2nAtzNBIfNg4Wat\\/D\\/+NlSKNCJsTtbS1ltn+gnSmCiEHEyAARTiu\\/gmJ8nuGl2yTCI0VTv7nFnVZ7LXXtHvkEALR8RF8Q1GeUiKnEKXN4IdDb3AJosDO4+9j4xqRsp7eTQduIuwNIG6i7zpgsa7kB34DJW+KAGE0hvpXCnElRz2qdr\\/MJ9dzkOojnCi7sAldbhPLEiAC6t1byjIteLpPI4d35J\\/tKIJTXqZ2wJeQx0SMRx7MyoTVGb4eAADtFgMbU97zgzyDMfPkjtBaFpmaLxZsYk4SwlCnSHxU66lNh7m\\/UK4tTwLi35X0se1heo8QzsErQhAr4IuvuBT74i7hQ7xzibmF14VkT\\/GFwug7IXS0mDaXEfsUUcNzTF+Rnvwdr5UNdVHRlUyHy2W0vFaNBnnlk4owmZK2FvpeE77SN4mCRMJZCBrSZf9CGYjyCukveqc85yBNmiyguF3eLafHx0dnteVd\\/waJGB7HS2+nj\\/pJFplQdMQ4SyhttcsVsOxx+dlgAtjti44V7zc5kV3uIhob6PIyR0hjOikp16a55Lp+DZIROt\\/U3hzjnKZnIfqGsV+DdVJVayCJjbT9sDF23HwvE69a7X8qG7GuZ06jv9NzjDBXTWLxqjau\\/o96EtBy64l98SKmbELEaNwXZegY0BCBScX9RUOrgXRP5mHeWEs6Ii5mu6tO2jdK7\\/wj2cA8jY\\/KM7zGWk9C+P\\/l0\\/xgwgN+aNrM2XsRhnbbNmpfpMBSdiNoatGfQwx3Onj0f9IFD\\/tB2oYdmR\\/Xka4BFobRqJePuBtL8qfZiH6ZLOVM7In6GtMtG0cwaPLfkE1qcA9ehR2pjTLpYRJmn\\/H\\/gTgqrJO3okoUApfErk1jV6SgukRXYhX8U0nruXsKP+UW9EMvKcmyLWiQE3ob6HdG+N137kst2D5naw9fM2Ml3TXg3CrVrOsdUbDicgbDRVIpHRsmDt1BUlVzJ9fP\\/iJgFzlJP9wFTueibpNx8pmoVdhbQRvwnWg7Pu8LFMYkOeCU7TRKKEmMNIIUd7W1xby90j50gTgBz7ZFzqGlwITfeq2yxEgU\\/70WUJ82xJb6hzAmxV4PbZSn+xU1aN8q3W1TcnqTguYXHPFgEEZE1q77ZRZUuqsZz\\/dDl4agSQ\\/j+LgNtgiGrRGiatHCfmfVYu\\/kzYuT3XG4a9vvT49e0VcVHmUACwgQg+ReTlUnZv3zZ8KCqK0s2+HZDvpp0\\/O\\/YSa5qrpdMJd9CrY7UDaiiomrKDFuK8vz17TOyk47gnk\\/psQVkQA+zEyHFywJKPswRJRGQjRjOINwuqoJ7UAB+61Q\\/KZ+OllzhrecP6AIfg7oFBXI+5fDOA1JySDKzL60rvqjrYx+kq55JplMGylE1XVHfVmzvhrILCNlf00z0LV2jCptcuoQtu5\\/\\/hGo46hqz4dZoEX2gym0\\/IzDGgtbzPO98rh3HWmugKN0NJ98WfCmL0Zxcenm+6\\/OfXrA+zMZk0ycrd2+vylbys7C64J\\/3HwSbPOpWzvIjSNJ\\/W5iZQRCxDlWAfOmP1h7liQkJFTcy6jLWGbtF598ci8bsMsmUBG9SYq0vF719IZHBY2zbHfmQ4AN0M1gJpGIOuBhLTXO\\/GI+9V6djYOKn\\/l+CAsKCv6f6dwxll\\/sM8piJXI9O2ob+wHO472chPtLs9eVTQKDSgvi6hYZLdaFwKaEGv9nFevpi+S\\/SwtJGBRZWZDGpjg0Dkzo\\/mi44VYwjrhkJOvQhuLeS6EMwXGn5\\/2KRk43pYuGQl3F3Kp3V0mLsfOcnldqz+D4jb4LeVoSJTf9uTRho\\/\\/c0f8wCHv14Pu9GJv4jFojhLYRGnEhHeHfFBXMXL3pXUfN2G3OmPGWfqNMpb9lblyChRaFTulfAiS3JDmuSrJSpYmoDfoiwEbxqVgTNDXq470+2Fcbwm5sih5uO8AOOx9O3ypPwnuP9hsFvj+S7uiWJxy2CtFB3D2uk95cPNfPUbguLsGV2gqUZZkcqh6ImnjHOmMhh\\/wKNH6HHzryF0n1gvLyxMTJcb98x7Zj17M5pauyWHtT4\\/E9cT\\/74DYOyTI5CJHZYvXF1yqa9NM6KfWjSbkszifUYujpf3vA7c5u5+eMsjuTzfu8Ek=\",\"iv\":\"97414f08f8be3b4b0671d589d94437fc\",\"salt\":\"56314d47351036ec9e351440f23dec7bf2a6885472e0c1885e5922d5c21569767bfa5ca02a541bb1c461fe988c2ef9b868f21ca554e79463f57470e568b0eba6a6d013c6065b3c8de514e1db99ac7967a6ae1666e729cc62857445bd3efe0e47175b48689d8cee695ccae701bbfa3322db59ccda3ab882593950a3248dcc82c3465ae063a5be1ddc238875360134332654f6ce8a302112160a2f13bebbe9f93205f744e8197ea4c1b113902718b9512bf33994b57cd845032b5880922af8a96af0a472881b9c473aa5ac536f64a0e9c0ff6d2f79f48906617b7af7b4c044390e85e3612dcba0eda645ef69e080ee2ae46ee3c88459f5536f56e6ecc22d11370d\"}";
    String cypher = "HJzQJlwQnhtNOkmA29ITvUShrGESKyc9k1Xq7KnRhm1YG9aXbdq1rqzAZxriAPoP1eUKHdipJKSIrrvfIkqPOZGTCCeokDn14t1vE52QXvFOVzVDrMabwJcPmhRGRPjaLHKmqJF0e+idXMnLx7XhI3AxOfSdD5Ks+sKV8F6BWq3XGkFQXIsVpTfv7xRwYMfbAMMkTYTDr/rroWInj8MQHSrtq1qDI6k6ZVDoXEkXjYwEF96o6k13MGW9byUjqRdZoapCpBO7R44nJMAMXSMzj0GqUlAk5Wk9GbLagjoEYZ0q34hwcXWa0D7zJgtRZXQ9Qedc0O8r0Po/dy4e+r43mxWqxQp5OMFaEDTHJlXeOQCiXi70TzfodqOnjOc0ah2tcqE4tMXP9q5ind7Wvpu/D4Ywqeu98ZLgVgGhewXV9R7uE5af1uQ4/PKU4deKiaRl0zcylsfhlMsFL89ZMQR3xBwJNseRTCZ30HkGpiBm0GwFVRnzV5YJWFJ8FOFJl6O5obdT6vHN2/mIN9GIsnTQ/BPE13Sg1Hi4GsasFGl5tr9Tmv3S1tw6T5hq/ObFaP2HwyyMTytVVfHGvt+6TQVNAeLQF2wh4I7Ue+gf+MfWI45N51am7tHTKyKZzAIOm4ys0fThLP/eAN+jbqiHYZs4oyD4GIt5fHCRI9UhJBzakNO1iyAD4zbyWftIU0aoGCicG9ei1ahIekJTJVXNSMlAkVR9D3IxwtkreJNNFCETO9f2MpbSYswKvV+deRYxiz8yAuiYkT+tsk1AYIRIWOeKqFplGqmGM9nOAPaulp5iQ5DVa0QncNoFUMknhV8kSD/SzT42DHCDS7+4OMtxsxO+UtBmeVSZumAGJatnvPZwCeEeAWHv7LAg1q7mCBT4UfVsIKds0Jz0AShp8ckfTFLeZHMzmj0XXJl33PbWHJYzRYBDGHijAmTtcFoM9oHtxiD/bYBZ6jimHm5YToJXdK88w9lJ/fdrsewrrANNNesLGU+UgpqtKrsqofZvS1V31t4ZhIZq/AzTIbCD8DRW8zkeOCZt2BX7xkZ7XpJbYgRMc+YiOFRuixoSrZW3vFZHpBzfZWWGzPPOpO+0Mn8eBXtmz3R4HkJuMafNvYbTnxi/sD7fQ0Xtbk0PkCX7RQQ4aVt61awR+npDP1ru9Af1WXoIGUgw/iQJwuEb1qhiihehvkNHyAwukMvAkgsQxhfbUOCt0gtmhuKjYM9CMYSF32eF1MYxEx2YNt2edVclMGOqFyCD1yzWtZrDUbujnjqLV4FPavMLmqZU1qbhjGP8BcSjCfYEdIquusvCK8DzThQg4Yst06oWO+hFaLtQCtKZWjLncw+TsS1RsPJHAU/oO0DnGllrmjOWtx/ZaV6r43IVFy8NKDTnt88eiZdfugpJoqqdogjBvytr26wCLkjlxeWRwS9x2N+PJ7S/2s3sKjGStfaZ8gTZtfmdLEkDXa8k21z8bfRrA4d0awbl9dv8g5yJJ8yW0OAGche0G57garuOQBE4j3dTsBCQfCzhSuOM3vqG1MglHYGT1n9OzxMT+iM13PODOfq+X5bwjkQQLOs/LvTlDJF0pWNOUKg4b2eMKuljveGn52gA6p3S0/rEhDQwa5uOyx8rQs1Mhj+FzWb0ZcJjKaqTVnOGpdwYkorTbLkIfSjJQMimS2P1XN6vEgEczDGR0rehbUrvR0Zsolnhdb2HArxjldFsZQkVR4M97POaPCC1bknG0QYsklX6yKGOyH50SilS94mBjGjZq4++zt3PBhZOoI+6lsegOvZuRcPQMm43iSYwIsESmsIfev8+qN0k2SxkZWlbYf43GwIV/B5qw2QhJVN+pGALKnF8XVPX0U9F1GqTx3+1bwZy0x6EEpKQNC+LDMcqcyW437oljYbY5cOq503m9GV/thYUDerly++EbyGzeF0FG4FkaNMWki7jhnQGYjhr/ifxBzk0miZKWyxHocWX+R7NzYtOHlIs2h6YGRtEsvc8t+12SF8wbbdWLmYFK+5GQd4s6MRa55P4PVg12nXxVioo4NdRe6ZKxWomtpnfvCBOYvKfEaVjDare1AD8QLvAVjz6E2vRkfEq3d2q8qQEAoML7mtJR8R0iBJXNapUGuNfgKDrH6qYxJh6HyGLsXZdz7D3lYylJgIJtEx1ZA2AhzGyJNuzo4hYMx2HPtkaF4BxALNbjUzx4XknquHVRdou4EyXZFULOSPQ1Ajgk5g0AFLMOj5WOc1RwvIhv4Ze8+Dik4CP5xW00ekX59TGlbu4UtNt8IR/VT5nCvJMHrx9/XcXkR+Ed7qnHLoCKN1yFIU3QRtSJBZ2Rr6Tw0OTgljJCNeo366vWUeDdBxZa/Ndr/hji69vha/Qcw+ZhqE6ujIXANcAypblvW1LDFCofaO+6GOLwN26mpi4LtTjHoO4xpo2KAUVy1hKSg+W67foZR4/xNRl8hATgSj/SC7LG26JA6yA1n3NBTUvMS4/rLkWm+hJKnHOMR+UBhTiNEjzevyLnTHYX/51rEBic+v0R8VWknnoYq55tHN1aSNFqGhzFzBMBFootJjIUVPcWOi8ThLDsWYZcChqvC3PHrFmuozzwYZ6RMY7rXqqrIOffEg+5jBMORv+5yUXSUukzQzy0U2kTeSmVijeLqoE6nc2qoxAvbelU0xQY63Vc0RnAUAJQyrzpaCaz+yKzDy2AUKDPxS9eE01M/fjBMHdh9v8XIKw28Kr0/bDPy2aSBg1kYwQCfx8YoPUI3dm4XThvggi6cgLvgecR/jnN49zrubKLdr/qcKsYnqY+22wtOtHY7HzYpTjM6BjZDjOHJXPIDBMHpP89jWUECHSuhUAwVUbzx6XxKgvM3D+IKWfYHJ3uAjmyDkLk9YXtHpHKGKZ0JjYFB+qvKM83dwcR1pXrsN8RLU73QER7DdncC/RvmDRa0a39l8njylW8C6x+EOpd3QMaqwmrih4UNvvSU+Msj0xW1Nkc299DJOuNi+bk11fKTG8jQOIGQjrQ+i478EqRMJBCEdrjpF8uwEETkarrNAEsx3bdUGA0320gpzAZgAFOWze4ECYETbob23ksD/v4RtcUPjY09uQlL0lvGUYZrD9DZTgl6bJDSwp/ek/nf9rso7JwfMojBGkgu6SclUvuRx2zFlU36ju/KjJSyOR/KrgRbSPukSzjS1Pm4eKl2J5rLvXxYV+4xInoMHN3l12e7RsnHbaOJoHpplrkVbtfwnuCYWhrZDKaLbnXjeUlY9rafuhPZyKjhcJSG4ijC5KCoAQ3Z0qzSyp/MJ1CUvHRyWir9vaDrB3FoRzn2gjjLq+akuh1r7IUwd/yUS5cF9HDcR2ngq77tzMBWD2y3vy49it1HBdqywqQ5PQfnCUJ6u8y+A2bX77Q2atGUWVRp63OB7+9fbtljJfzXrk1g8fsEIFFte+SzhPcW8eMs2nSx3oHkUEwmuUp+D98rqTtiCGDzw2vpThrE0Mw4BZapiW/ZPyvghC4m8LLOuIcClgdX54r80IbL8g/nG/REIaXXlrfOqFvOGHL5CtVFgHHfMQRBmt3j8XQzmf3YKvEAtCwuYTwODMWqcWoIYAdMV7ZYbJpcicCyxn1TjV/LlrnyTTF3lti3ak5W/Q23NSxy9vxDs0pPt5CdcIm6DkM8BFHXVYBjftIIWYzHGGo0IrBsVooCV96ARqoCr9cIo8OPN2wWSk9NfEnlL6Cr2oJL2Z/j5lx1WUM8jui89LcSbfa9j6cJmHV1F9YKEN0YKTYOkAA9DZ8YkFwezlJ79QIqeAzddYKlIVeB0d5TSt8oNuNACAcOnM3xg88laB098edMHmXVWrM0ZFKuui77Z74/ezDGY0YpErrpEoPaQcbC5ZOwGt5dvbmySrPOpbHBOoEc3IFc3W2lr1dpEpOJqSPJ7GqOQNeMyfgDzI203EcLpPMd2QprCAoM3yF/2gmDAJ+qSWZCAJlhqYcrkbiapZykV9fi2XCzM5PtpKhnjPGy6ujkULkJX28eNU/cELZW+3ocjRMZB7l3eLm/NhPxGYs4kiEf5bZCIxDNz2/01NVBUH2VTBymbgOFJeiEtlcGXYYLaGMNnIDLHlcjPBFlLv6qzxzidJfi22Xx1YantRGo5vv2ojDeCfgSeMPwx1KXiJvWVTZJC7Fel2w5K7oL1eljmt1xrx7mqsacL0NSppOvBvY1dyIpytEo6Qe+m91cERSzgN8ukkuUpE7+eGgUxVHm++B05cjMDz8Pz/LfaKfpKnrkBlhEf5HwnTPF3yGKSu5SpEH0KLsvUTpiWAwLdde+y7PrVD9EPD+0cWagRxLavHAqThr0vCalHxnuSgNVKm9zFQb52bkUftfkdfJBdTgo/87Sv1N0hBDDS7actKv9j4ZX1zewnHpeUW05Lt7s/FaNMCHdN922WpAND4zYYaG1XKTdft1U0cwzRlnPcyGr6R5F4qLW6josuyjYeipIhL8DLH/V08Vqw/I/yR7Ck6Zq8ftks/XcxaNDPIz2RSGswh9yiXipOiK7zqNiwh/AXR+sY3f+h6hPQvsbsn9DK9aRWGXS6amZOudrt01INeJREafiXKmE7w3EEvZRhWLig5UqngrbtexjzQJ6OYdfza2LV+owdQcUelTTsod4FX8vjD3/isv2lcR3sN8YmpD+Q06LNnHDGhKTAeBioA4lxXCRT2zwUpWD40stJL4Ye7xQmjoSGxbRA8mtuwQh6GAOdsc2a+FK+xvNtl99kyBITVcHu2odB0i2SE/0mXsP3xDS1NF8YY21KyA2aDpw5a36dqzpgV89M1NQr11pV5lwRrB4RADW3dXRH/Nv8Cabc+Grvz1+tElgtimj+6po8NiYlRd2rQyYdIQUQ2vcEMecip1xnnjchzZPi9UKOW9TI/AC8oljgdjWqZY5kvj8l23VlykmbkdtQEFtKA8wHAWcFLD4MUZX9TY5ZFm9gjiuZLzYkkmnKu4KfXMy4/HWfraiWCWO6TILGkRZLg6sIw598BtYFM9saNGxTlEgPPmrbabErlGqurMO83jfAiPJp05VSQLJBTsnKQFZVXZ+o+lZ96Wa73uvi5aocnANChalaCFFNZeILLNDmPcWTXJu8GVvTEjUWwJIwvarPlZRk0dVIMmMe1wVvGG5CXKrEEOIfgpwQi9iWkNAh/k9DOh0RjYa9Kko74Aht1GFgDNTCIKFDwZYYr2WeDZM9R+aQVDYJPH2pRZiRk/vA6kU5O62d+X9UKpHqApkDvjflDRf+g9yrZRnjt8YOq8VpwFS+CXNIIWBdUCUAoj+oVARfrHypKHiCz/6d+pDA3IUphq2Ul/spReiNr+YAZHCGcyXBiat+zfQdBB9ZE8n2tydNmvLp2s64eymQc4zZY+c0cB/xc2kU9yvvDfU20HUAvy0FUkv155vW90fWmStKyptCLVDcC1z492fF/uANgTqAMl4EWHFq+jK8nar5MYanKstXNzSL9GN7BafhhHJgxBgScq2xvNIa3dXddPPihZkSQbUlS/gZX4pF6eSoLM5koZ0CxNXzbBmzfryl1PyC6EYcev7fa5TOHYTd0TxGMN7Uw4K80igFNdPrHehscOLy0avFUwDVrDJEVwlSdysBXvCfDcxiV1U24kAfFyNOZQGlavE87s4GZQ2AEBadH5t1D6gyZ/x8WazAUI0p0PJZzZryKQVJUF4Pg9UYoCj4E12drDg63JpMkSpbUfcPzC0Ty75wGB03PWXU5YsUfJ5uScqzmhHS2H1/CLSiNQ8EGDZ9nHDATCsMfUrwFbUFod42jDkCeG9pMhncLaqw65dvvTt36rGkLMRv22DxhdWOIgzwr6LDBqOT6TS5jmihJYh0BQ5HZ05nUMIW3YSMlV0zdu8Dv8/LadmvRFaNO0bIz69HWAgdELOJBzyrMwlMbqSVVav1bKZHppeNQJ7HavfD+drS61d38wjAelr4upvkRbVs+9rJqAD5aRdYKis+s66yGPlT0DtqYPxgQhq+8dU3S5YIEUgnKXvoPWGIupmB4PD6Hx1Eu8eLgordl9ieH8YCP8I1Hs2GJ75A94jJOMLzWGScd3ALBT5gnT+1bWTvy7bmpaO+8FGI2xqO9YM3ANYQGSPg/Q6ajAn0zS8VeLutHW0dpPVE59sIddByI8zmW7yC0kzLGu/jdhiFSq6fZeWVukb5SXmWREo5qZ8RpQp0Anwb8uJ1fGyHGGAC9vPIWk3zDTWs5W0Bd8bloNU2OBV+tiOpw/MtdL1P8TUqLlziXapjNBQMcky0CIoQWcXWYof+1TsYb6NHEwU8xU2njBQEdclvxnv3eW9oEZlwaOCUY8xnxuK7xyVetxpdW1Bsibu3rTGCNCPSj2JXIox7hXXOACf/Lw9M7H2WwA/LUWW/fqxIsMCceI0LIgv070VizM7jLSPHYQq3SCGhkDtTTQ7GNYFw/o8PLVvkd0dJTOdTJv9JN+weXbfph302nfy0bSuQvbHtDJaCFwhddFRimfxFoxPpDGbhBvMp6BD4qj38bjkLT5nKgRe4lUfIM4Go8hjPKvEnWfUyDfdS3yl/RMJdQr7LPKQnlwOy1ysKpZECYP19G1/Q2jwvIMQgwvaxINy1w4im86C3eBtiqFX8vsuQSQRI/uXPF48DDd/4BoUECYSlAY/ghO2i9XO9IvGvLgGl9huozFrpKhWmbIbWVYBmHPGSi/W8+CownPsLR95tvXlUtW9jp+lMTr4x+I6Cy15Q/ApjECU8TffX2D9xW9qZMG1k/HV+Q5guftkz42gKMhvws5ghrONvOmmpZce8E82LnuBe0uy1Whc2PtITu9K2SmegHHwjK8QCsxqh9ODaLeAc65k3cl5ruCDRh9FOv0m5DrkbgOY7iNLiOFEU2qANqaXSFb4CgJB9tJMqsdK4piIrKlxqDpCjWWcnDajaXS8QGKVjN04ZUOPDNRb+EhhGqMZ2itpCIjBwYcah02E7nf9mK2fY0Af3E1sioA6a8M3pQzU07hSlxORIuTVtj16t8v8bc/7ZfpTZZ5GDBOEljBZ42IssxQ3z/wc28SLwljRpppW4EhsWjpOv0idcKUHZXO+AEqNqbcyOpvyr1PV4SX+CgmYiGWzGPkat+4UVjt+Q4s6uZY3qJ8yi8AbKirCfZxlo9EyNjXKuRmUUKmJAm1JASQhKJOwUs4QqEGzoVsIlD/Epr1imyn6Plq9zLZ63SFyFHLlPYuM53v3pTqFlgFHSSnY7h8JlgRqfFcn6KQRAxbNT+i357a5MyMEl8LWvytB96uALC2R/xzHzCod8zTOW9/MghKfXZs99JyJNaDvLfFyOtAQfJpBThG4ATpXqVj87xc0w7EgwAUqmVIzCHrn/eggmG4spsgE4CfarNCmmgRg8n7Fw1cbynPwtMn/TYslD8RqbC/2+A0OFU8G6iB5Budc6c+tWp8C4+Dv+Z58gsiWbJbAdUGSKjX4TzOpihtsNFJWuwIJI3kHVh0STipcodxYxoQ/aEhAdOCLXlLD4f94A5aRPTuf4zqewfekSXyEG5yE0Fk0tV5qVbNEpwlFQx3V2tXqoNP+4ew+3VBj75OJs/dsOmC0pP8KsOV7xqIlJZiGvR2+bdlv8qtkE5JkQhsHhzHolMG3Pfo8iteS4o+qIIXCdf54hC4x5jWoZCkQOU/hnGGpIyH8h+PoV8nBud5xhVb0rUCZ17DaWGUfjrodYlDn2W20z5Mcm9uD41Ipq3+FUVEF5s8SMg0ki1NH4of0cjUQS/LQxhh/hnyr5Q9+UqrCM0ZN6/nY4fd8+/OQfT4Uv0TVghvZLMWqrfoXOch6lEdzKm8cQA0aDCbdU64K9/bBnDBekwJSzrwXflUrWP6aHtl8yhJnCeVCNJ7lOV2/jtiTM/gs1qDMNeHzfQHC/OU6khD/g1F+458p0B79ngNNoa90ubCpjw8aELGmAT74PsVIHWTwJqTo1tHSNmjoGMuc7ONDd2pBOrgskStzsYUUcs8rfHihS5AElW4meg5zSL2XXW8OgvxfncntY1p+JMR/4RwHMfHNAnSAHlvdj62ioxltwbbJposPlsUVOL3vXCnIfabPM9MpmG/CwmjOgZSJXmOQgitHcAfrsskW6ie/UIWpp3IZ3ZS+isjf7sfHQePhcXyEyXpDti4vtLCFOUds+5s51vAAuzW3zDGeljaNg9zdTmrAIcyoq2exD/TfKLz6LY6Oeu+qaLy0hcLX9B3K9Q6GnRgrk5xDD9R3vrK5xmGa7fSRao3ukQCIkb9hbJIALfNafbZW9wB3rE26vSIeK1t2REIJ4MjuvIMn37hZ+ozwsoHuO225VJyrkAiNwrrPW0N4pyZ/vOJISr3fkVKBhcMJN9RU08vd3IV9P2vMTp9wgY+rBNtW674RgC+ZhEoMQcPU31xHtFU+Lr+JkAvbcBHBKoPqYiT/Rk6Nks6zPaTZmZLDk2gIxGwMrJENjHiE5DwwRqT64ve/V2RrFT0fAwXIPPERPZPcTddvJhcrocMXoUouu+iBP1BToo/J9MARCIW7z159lAfgpE5hOCnPIljVo4/N3bCvafSh7W/2RigRQmFmXfYn0Y67zTV/QOZ+ChqioUHYaEiuZVSlTziWPvYzjORarGAPdxI0UJifmjvsAZu5FVpTngVW8uY5ZAQscqLudS1Yr7R4JWIb+KCt271Lr0NMBhhdOfHriAxhTdfhLqYtVWucMjK4CxWfHew7l3AQoKqm3gfRDy1hTy7c+swCoLRp129bSYyI9BPnm6jRCqppG/r3A4wc+L0fipPcYv+E0zqjGj3uy3jAD602hZT5bryvBqhae32HWMZDHEke+maaeXDytoX80CTsG67K688qWV7vzyNi3ESaMr2jbIg6BWHCPz4JC2HiKVvO9x1BeMgLArwluVJRlBJ1mTY0uh3KnBN+JAzXFfeycxchWoj8oANo4RcjTrkayfZsbd7IzhK/q0tMpXuew0pCrBTjJXpUxMEL0mbdtzac6rMan3TmuhChswboxl7hD9ObapRDeMbFl404WtFGvH5cP46ht0pQq5v1NMSrUwYmqhHEcWDiXdGsE+GGUPmdrAOZybLt3z0JHRaoF74T6siEy/7dS8YEqiXzGBMelLsfKfqtozWwSKjBEICdrHEEIQk+P8+77t5OGwrl4aNVCDwG1IwQke9X4i8xcf7FIiXcrw1/09ziaSDeoCy2cpspWVh0WaTd2JjvCyhKK3wcmr+93XhQ/fmEnzvzb79QPqrp7ZInyuKZXUspBnZG/+0hy6cPDd2cyBjvXMVnFwqqnRAMOVu//g3zsamsGVoIvcYzowWj7sMDopr3ZcRc/8U0W6oQcxyl1Xq+d+TmjT3O9/nuXY9PFcEEG9MYXSA11htkDao4GVMWDgBIt2C/skaWl9voejeRR8DbEBhHJt8dgxnWB/Fu/3O5xtNEH8zffg2Y1m6xHIwBJbK1oAj/Gg1CUy5OHG3jUejTNOCPdF8PFqFjEWWDW9GK5cWDpkDi9cjKkYSoM/XhdMZLkDRuSLSrlQRYlYeTrYzdc+GGx0pOT7Xlv+d1hgAXQTmDhqNMzKM3+lkG6JIwswHWFpNhuKr/zYoq3aJtFKTH641oVJVp02wanZrVM7BHmxAoCiY1mWkDTVlHWw41Zv9zJ9UdFRKquFNjSerh/sjrmcQcGdA1hHObtWJPeisjjF5HSSqx07lJ0cIevIqG3y7OFx3Y3NGqtyadR1b2OH0/tHIxuazG8bCUAgSXmytidPJJ2cv+9LV8LqSCFEixO4qYEf1Xzl0KjzRBzOT3HD42Eft+9RfzU3/ZogvclovET+kXKvN7xz9kJrV1k6tM7gCUR+KR5wq5qEbrclkP9qL1tEg21l0Tf9cVNfZBifTC5lViCWY7mLmoTZbfS/SEIlzWNa6XmjDM/Fs8qdFBDcGKQlKQAxCYrA8UasNUsU6A7rwsL1axVivttGjuteOz9qB4lHaGUziKkKOl+85mfSRRiBU/M4sNuHLGYMhNK/20wg1uMSZcROoqKKWhJAQA3/DOGRwN0ibVLX9Oyr+Z4sO2YlpGpAjRcGDsqiol6WBQaop7hZsImKK5iaZ/RAI7PnMmyj+N+OQdhsd6OVxOClN/J8ozpawCrHJTu5l4vc1oFJ0NKoWRpBLr4TC/+BuqdAXXpEUUB9iDmeYVLA7u/db5/amPtRuxnKLF2SWpqf0DdJdEgMPO9DG1GRf2dxGIllf23dXk9fAOEuICa3OMYA1+2k1klBfpauZWAhwzoOr1+488x9INm9ocLUxC++pRIkUgT+8e9+F3iq/y8NMSLm/2kvjZelrVoHxXRvXreut5399hTb80ZND5y8xMO3WRWN8pvMMT6epYQh+m2dZV3NgNjA5X673wcrXuWrBC7CDrtFEgCyp5uZ02EqZL1C/K6El6vkWjm1bCVh4uuvSf6t/u5CFmb3YUTrxND0roYVyaUQ3NTxM/T/03EPp40zvUJj2Ctqrkp7yzp21akvm6u5n5a05g4nKHljYXBrIuk7391a8wfZgerRMISml3SCH0+jNlcNVY2S0O+aiF9WKSVZTGd2TKxREQOXCkQwBO9MdrJ5V4U+wTqYHPcTNfktvv4pi/hF31XW4tcuo5fj6vnbUvpyf340JtuQCFFf7E0MgBp6KgXFVdB/tjg1oRM4rxaMsRTxFINFatnZa/wlPWuzDH/3jLMjs0lHFpM4rjJpdsvVnmUsJbfbcAU7oc8Hc1NLwG1v+q3GsYtiffiXNt4DuMz/vaEsrWHYw1AKP75gOdzymV/4IEUm+vht/lQXjZxndc/77W6C6s3am2DKjQrTJZacn4vwh3m7Qncs2lSOEMrNj+VLfYqhgqBrCglXL5T1K4ufYoWxgDyM+blofp/NfNWzCitZalA1SgGVHv2zg2sR11ukoopi9U9GEe5HFPGXi06z11Gq7kI7CaA7bYaEbwHiy693o6gWAN/1wZB9j57YblsxtVCa84/qC3rdjfv0/FhaEXFNdBbrUwVmgTM3Mv8IwfiqU48ySHDLVLjJbGOzPKaFKtY+6ZWsnpjJqDuyvwy3M9CjB1UpVSeb1PuTd6ufPdEyZCuQKGcZskLrDjUfEjR2vDKmdJtjhR9vsYUlpOB58E0fhV+H9Du3OLmf5S7Y3Tej1CNtMJSrUOtFwX70ybxyrbJRVaAu0BVuL8El367slflCWVKgnRZjji4xzCGx6yfYsEccX6UN5Bxz5fTw1lM5fBiIpZDCCf6Iw7VhJpej2BbVf3tUKYG4zD0PHnxn62dJu9ucDn69q3/1Ad3TlR4sirYXkiiCx3AeypZTLZmaPtN1iKh8euJ+of1g2RTo3JVLPsMFjATrQv1K4U37Q0eLRtALI56F3CzsYcFiWrRuYS8EN5jJVv6mH+Wv4MPiZuw7EesgNbew3R6ozhw23H1lRWql41prI1dJynzi0JehCEKgJwsYbpAUIJGnaZFAMsmaAc1MXHxKQVLAryehW2cZ2yLODoguogQQllhYoTDewsuomdGThb40EBc2GfqvB9w9CsP/CpUyo4qKlMH1NxETwuINY/z4Jip6VCkQdVlcPSKlgaiV3AA24a1Ed57SWciJeJV9cqT6IgTcK444meP0ojz5NAWiNToOExl7dvxQtiIwYqfZkvBBItzb26WIIX7EQbxzdaVPB8Qnn2a1jWzorG3ngXIFE4pntoWaEjzKOIeiB6XRX/O3AYg1WBTgpV2IFYYjRuIAd/aKRN3++vfUhp9LaSRsRiqSvDeyKDiFmwMO8rF3iNb+dO0hIJHuNkKtJMwBMYU5eYp8eildPQkL2KVis90/r7uOQGZ3pLCY12vyu2jdCXO19bEN6stY2Hnd5kgukZgdORnOY2hrn1n1XAuDm21YtcuVrTC3jw3Skt1BHY8YOJ0tBVSOqH9W/tXj0HwjNBcNpfh5H0L7ZIrXpfvqE1yjsbykgUz+AtIWBesuDWwSOiYnpOY+c+gcc5P8NPcIpqbjwFRvlX7yJRT4YHq79DfA4sUpVP+IMvXrmbvrFoCufbVvNjBhJ66rLZhecj3P36EYhnk1WlFxT5J8XYupLg30MmoY7KR5E3IF+TKXP4rKoLhWjYZSjqpi0ClX0dnefeAMDrRqdnrPLrRlsgUmYBS9tEDr0QixMYiSedN23t7X2Bl2G3o96WN/sbOKbd4juU/byqAHQKISPiOn9k3/4LjnrqgQKAuf5S+HL9qByrmSzRPWGYwr8qNiArrxAhGOxn3CPlvaIedh6cPG9RPACPNDk06vngRjGutXBcPt3R5XPRchNlQskVvzzumvL1rDdyltHmedZiOSJ0bfMzKG1R6luELqWQqiJ8aDr9QIjqbj+aerT+sKsJWcO77b1C2U0yqHN4TEKAFqaGf3FPSFeQiYNlEjxeSr4j4LZHjqBxoiuP+QSxIh89bsJtorPfbqOS14ZNcKVAMoQ739kd6XLFr4LfL/gPw9nV0En8cAkel50NX6yDQii6pbeV9IqILV4A1leknRA8PTUSL3GyeKzlMhmgUWFZdwA2yoQkuHN7H39gpyWj2a4sATX6FIGITI/P4/tSwsEOxUH9Xf7SWnc4FIKv2bnVVLOYS4LI/cxq0HHMPjtwVrH4h4akOE7aNkd7V8jB8YGVYxMMAMuw3FsPWw4Naq8Fj4LoTpAlvoNP/6xK+TMkwxGjpbb0tRaUr+8XqnTPGFoNmqByNjvoUID5F4NaojisVSNCdpj/P9mt5m6XX2X6yE5+3JCUbx/vJLRIsA7f15dh2FCZcK4NnB9PCrtXOuxmsOjT10L04CDjpopRDOxEQtgH1bBVbaVe6Ee935ufQgwdrY6f/lIyIE0iCTUus77b+XK6lDv3Wc03Oiu3oVjJu/CMg3KMcB3WvaOzZCr5/9lJmXYWSrOxhgnbPVRcUcqqG1W/kHxgj7mti6RyF3+3ntlf/Mal40WbbL9wBglRjV4iWMNXxpwIj+uya6et1xiZmVmNow0uavysVoxI0K7M81wol7EnZ3LqJOp1EIyMrDW0ZWbrrCFbDK0sCaR4gvbl/AmuZiUvheLcZgK3LlzYznplRATrE6MMFwlh3zBGE/8V5DQzmSK990OU3aS0d1vsozOsMn9ATBdvi6gF5GTnp4alJSde5VPdV4mdT8UQXetx1qGgyWVdKDac+WZH3DvO6POSS+DzT80lJOeiCrrK+lnpq7dEBZ82O4uWsgnvoHJduecnIL41829HfvDgj6y53WQfh4rYjx7wOGhNbPCOFqOcad2prhUakOmK2/p8/q+Nnw7hru7sgJIPf8R4iTS7EZ02H0Aw6m9l7S+sz55fhC/3T8dQePHFykkJPrRWjTwLTUD7FK/VhnsLQ2TsmzBBbhmVwWB1p84FZXoWWPNVmZ5PC8pFY8gVHAaAKwila7/fowdJ7sjsEcsuS8JxPG1udy4Z0BSgt5y5u3/Nq0VkNgXIkO1g364RP16mvvml8Q/NIQ+cB73eTGjRv81NZ+yiPMkPhEKtkB1C9rZsDwnU2X+ByiXMaP6QehvJYVbIW06UJe/thgt4RpcL0eZoMj7KX3QVvBdgL9O7yWpSyqojU56y6zMJIjreResSua2ahkVZaac8gkPfvZgraElTwHP1QPTs1EeeR+XAAHHkGY0rWAU6iXP1OeekAVNlln0czLJac+k/8JVDzqeZM29tjngtZ970+ppnkU2iBx7JEeovf/VFv/ftZyOpbl1wfJ/Gi3Ekgt7az2vX9MOzGFcYPI2BiJwRnNENQH7jdjXdUSwNWSut7EBXxLSj444FimqTmAA5fqhX+Anx+ogiedS4q7CQFID+NB1u9p+zKITn+EnB9HZAquX+sKnCDGRVGynN8wxIa8zhasMgFRq5KjSy0/f7iWLonxtHJreeHROAPFmnb4l4O1uICPIqqAvgS+iGdNB8klSG0W+XYw4x+At/xtPYYxMlocS8OKxWvP7hgNtxcfqu1ZxqLfxXKvGieVwSrlN93BEV5rySkmmcLgRcg09vOle6rDgMDHhXTy4Of2m0UP3lXvZFF//7llJvaV3OPbiiDzyFvmDJmQkxrddL2z6u6/PaZRSjYBTD6bmTlqpf+Gp1jO6oK63j6nCI37gqX0eVCyVnTxXWhq6teYxsEd09s4orrNxnO3cIyvpGpfsv4QGZJ1EcyhB93SZWpOocC4yvnxkXubdLiAjy4o6d2HIlFIZ1v3h9V72zaF8lApZHlfxxhjzChLegOCwYwavnOxm88T78UY95YEi7434xO1i8lshY4TZd6xX18EvlVTEx+fRhZ0WhJBPs/AaEVfr7GRA6I6I/Cx6RzdoyHbVA8cxvcdeq8e2tIZl4DtCykg4XjGPeSpPtJ/tN+0lG4T2HLVhvBNAqxtHay4iUNcb79qpNPwxOh6Wfmt65gBbY++1ZCQfYi1nLbtbMF0+hKVF9YpXtwh+lflNnD7tUOxXJ+9NmRnCwt+Jiuv7U8E9X9+U2lPSk+1gXXcCeruc0vpnd/xit3mgF4erWQ+yzexgr4OA2ws+ajDT6NeQ3DeuwJ9PxyZ5UIW+VqzOVN5AHUknC4IRv2TAl8x6pt+MiG9m1002Zr5vVTjTy/UcUFZbGrx5j+/TB5JMBt8i0xIJJiuQUnI4gi1JXekdmuDHixMk6XJ+tNeLKSURVXLqm4Rn9Pg8iR20mtrepTZI/QNIzoPsSdCriC9uY1jXQJG1rWc+OtnsphnfZ/H9yi0eNUsvzWhWbPRS6+RCkPIBhYPa0O6PXKIXq7ePFFpYvFZUJW3K81t3CTM2RMm3xmUMFlL0kq769A0e5frXFATUYQCbvAnisD4eF7VZp0cfflWDKL8RAXY1tDjFCi/t/wbm8jZI2fOBKmxGvzV5PB5zvcNhMcrUpCIXUSsBwGGG848O3NbmkHQVzSOj3aZESeikHPFNfIcmH6pDI24Jz7fBAh9wV4tCQP6jyseyeGiNgqJ2d3OkJd0dE7Qksfifubrds2f0HjSegurl8UHHZ2Uc0BYC10eZrggyPwmCfzeHaPCl65FB3e0BdI0ce2fiFUJWhTQeK67q/iv3xN4H2Z9hAdZUSmFOvRBkUqUfWj4CgpQcNQ043TiNDELYfpOOQ5QMAM1AHzs/yeVJD3GRqXHNikHdVX1IcuykfhdWQC63P9u7+jdcfSGGs0tFU+h0XsX3tuog/Vdn/tZevXkMX2tEBGQDDcbJbHL8FAPesRCwJlgziBNvFeoCx220lVBXqY++dBngM4JNpCHRz2GVDCeQiChbWaf69v3UsCNhb55t03sJWsuaotJqrdG7LzxFE4RSe8PqKc4pVMoq+6tJRoo5NAXflWoMSQnRfYEx5KimAXPn91UON8v1wgG9l02FOwZhnrU8hRU7sKr7k6V+RwAYNRY+xcg+epPDur7U3gi5WJ3x4RR6wmpiE2IyqH07wCwNryHCnkeuYCn2jaEvsKHIL/RDlW5m8H1BnluOy3v46Lf+muUy8SXU+A2bA4A4d5EFFnd3XClby/Q7ZtRtm9V1EJpLvHK9AMgpnSh3NXOexGekzbznGzMmwrTXdt1SFKhCbvcIfZ4uBKYwxRjOTd+vi7AAq9ey07RiSkcONcT1cedAobSNf0XzpwguX1ndpvNV1mTIHt1U8OMWQyRPK4Lj13aQl4T83jJuQY5pAMznT9vtA1tJea8s41b2UTehB/pCEZ25h1LEYKKdmw8bGi7ZQwm4jZMnRC3lKuw3/05gVcUooILpSAQl1+TiRwgEvLyV5JH0WGGmUZzQiGzXYh7jfloLtMuxWGiqo40ELcFYD/apmF4Bl2EMCN+VfvRNj3v86UGkPQMIibboJ/9w1xi4XvfEdLcB+CaPWE7HM1FqesTpr9sErrMeh0UqvuHtM25f8+xOjjGBY+39ggLy7n2fpIdHlDfaWJsOpAWC3D5F4+wu9c2aFwjsR9HelC1Wugb8985G1595QIfskNU09npDB8Viqn9h1idBYXWkjxxOzkBsQRhjMIoXIAgKsCOX2cxWCSNawdGyIEOkM/QkhSSrp1CMsDWx17RLOxcX5V1tqeX9oQZvoEOueJZMaX5d3ri0CCPFQN1BHW9dvrF7PROt3af+5HwiKTXlqkVOJs+StexY6/m9bd+YfDse/s2W2Jfi4HMWJ+hzZRAmRGNjgF6zvRMYmjr/EHOezzgkmtPu39PxSxZdbKBBmjIiGhGVHZmbNoQ4RUbPQxCPZJPbyFey08iUYR+vlAOE+DeXtapDj5xaY87W6hJIJTM2RMtJO7VomdNY7wQpIqvw6OANB7DAXSC0xAnMt6yWgZlu+qnS738D/X1w+A8F+K9Lbm5KtT52zGQ5Kcw1kq8obZYwdP4D0zN31LP3jN+Uv9U8Px76Csc+ThY9D/2FDiJuBKzKHU5aY8Bkg47QLCO6gbEAYE3Ko3FM7AzytEIe5Yiq+O3IwMGKgkMwNLgGPeMcVRxFSypeyaJxMtqT0MpeNL4BQNyC3vqikVOgjKyb84GT/yoIAVgZOz/KN+KftoQ16d8M21LC8OlqrkQd/BbvcxyHxZzSR/hTwNwwuC+2K53RRfxAjhbsDRl/HkT/aTtj6TMQgGu1XlmGq0E08kE6AJwo+fTuHZfYzbUaPOFoOr09F23UWAgqe83k7lIajTNpQy1gzk2x8bpNQa/6czhW8KuNNFY7CIygOq05OnadC8UevEUoDwiZTgv92TrwfnUPc6oi8+9vRMAI+8S6Zm1gN+24Kk1s7Uclv15yp5DgbQmThc/W1ey/Z9P9m1tK7uR+CYw2rGu6FnrMMAZ+2Qa2r6StGAwR3rJ+Iq/EyogqYRNgs4lpDo455RVTsbinYZHMHKFKGcYQC8JshuYXjJppZS01En6pOr/aHqBBSoRo97llhEQlJAIBiba6Xgt3ee6ak/ostiFrLlZJTGN3KMBoIPDEGNKH1QQM2127iyAdv2Ucb3cnrW3ZclDacIxXpRCiKV+9oGkcGm8uIHDmTW5pxm7AMQ1y7kPy6E0nCccpteDCtRq6vDIdb++kDKJ06m8k1msKajp60G98oQwq8i51fruux00j+E6x0uyDr7Ht+tGtAJkr3xHvwW7lgmUZ5zglAxyz2XL3/Oz9rJVJzx15lCtHX/Q15+iqV4w5/eUEbdoCGosLPCFtSVfsq08pTZeVl/3mUNL4h5JirFMk59lnGWz8qc2O5+ARG2jtmG3MjU3j6LA4edrY/fV1vS82XlhTq7gGTXZ9wC6x7K6QgGq90Y1Yujyb3OX6nzs/ahjnWJQwJmuZpkqJmknLkZg3vT9+DDe3r6NjUUynGL91hzMCH9teWjFOskpg/2CWfDU5PPP1NVf89RKIJRC+w6EQWNfI18M+gaV7/WhB6c4mbnTTE3XRoh37Heh9dN9B1Zl+iWdguBT+1DI+oR9YfeHdp4auZeSfa3K3kQLcAmDhMFtuvZSO8VU0ET7T1OKUPvekrvnmrhuP6y30/W5JBnZ2Fi7fg1IIsXcGVSZvWUrEWKrsj4Jftv/C23qHxxkGZItSBrDuXcJMdQWXUgutSZyFi/v0h7SNVqazJkFoiH/TP/Dwu1hePM6ocsfIQ9ktkCtqtuAmup96JRMni5cF4qQBjj2d8oD7UoIZ0EbM8M6mtiNPhSxyjF9OsOPMbc0A/QAT8MOB7wUvd+CMGu1Mz52lj8R2liloHvvAZpQYDqIWEMM4Q+NISoglLcWyikijqrTA0c1Lbl9qgCoyYCY2deihGIPWe0b2qUzLS2E11ABVPxZHcjdz/yOkJMBRmBkXT28Yq8Asdw2/Du7ympFkBaw/o/N9n/jXpHlNaroH0D5NSS1K/y+VHdha0ZmJ2NnRTuEnbsLUu7qJ4y2ixgZGFs+0vmfgWQuQtberv3gwmuw9GmCeNv8ENqWsc0JpUU0hi8etjJeM3Wd1MD4ZTIfF6t9Gc88krnBiH5x95kuuO7WIsdAYp7LuNWDMrb0waDe43k7+q/2X6M9ptf8kwloA0eLEMcAkGtqHiHeOOUFUucS64Wc+rB9o08Zy6MgEE++r9ycegVuhuTJZpHlasRns3NKNtiCopK/M5bV3vrFgCXgGM5R4sDjydS8YKbrU6LmM/ZSF2qyv1ASQBrY/0lyllqqQg7Mm3UOH8+Oz3XHDaZ/ElKpIVBHkKGFSckjq1xHsBfdWb5LCsQpztOVnJ0jf6ptUWnBOI0HEEuJb/79IKbVIXcgq/Nc/H+Y/WRSHzqK1AUQM0QIJeVqKa3r1kaHgu6Ypky2smbfLzQUG3x1n0nAy2kz0sS0wqejIsrPANOpqnUp0M6uhFaNiVOzrQ0T+UDYxdaFZRlenidMUuLfHCLigpA90JtjaEVWq551jfdXbHesUcGNhNJ/J3J+lXyzZbDzDB13uJh4xUIk3Nv0fZfEjTgoLofkZ43qyGIvnt1SzZ+VlWcUNbpNVbuo5kt+/vZ42dB7pyS0fycNEZvKqdfNMwmWJ0+yunWjw9yLlBMpa9IFgm9ZtPkUc/2PZ1Z53ueaGJhDK7Wh2J+LXgCtqJCuZGW8M/g8gmJsZmecwjuZ1WKnwt8=";
    String iv = "97414f08f8be3b4b0671d589d94437fc";
    String salt = "56314d47351036ec9e351440f23dec7bf2a6885472e0c1885e5922d5c21569767bfa5ca02a541bb1c461fe988c2ef9b868f21ca554e79463f57470e568b0eba6a6d013c6065b3c8de514e1db99ac7967a6ae1666e729cc62857445bd3efe0e47175b48689d8cee695ccae701bbfa3322db59ccda3ab882593950a3248dcc82c3465ae063a5be1ddc238875360134332654f6ce8a302112160a2f13bebbe9f93205f744e8197ea4c1b113902718b9512bf33994b57cd845032b5880922af8a96af0a472881b9c473aa5ac536f64a0e9c0ff6d2f79f48906617b7af7b4c044390e85e3612dcba0eda645ef69e080ee2ae46ee3c88459f5536f56e6ecc22d11370d";

    String personId, userId, token, compId;

    private String getCountryId(Context c) {
        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        String networkCountry = tm.getNetworkCountryIso();
        return networkCountry;
    }

    boolean isFarmer = true;
    VettingFarmAdapterNew farmDetailsAdapterVetting;
    FarmDetailAdapter farmDetailAdapter;
    BottomSheetDialog bottomSheetDialog;

    TextView sortByNew;
    TextView sortByDataEntry;
    TextView etSrtNon, tv_filter_delinquent;
    TextView sortBySelected;

    boolean isSheetSowable = true;
    LinearLayout delinqLayout;
    Spinner searchFilterDD;
    boolean isCrossVettingEnabled = false;
    boolean isDelinquencyEnabled = false;

    private void initializeBottomsheet() {
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.dialog_sort_farms);
        Window window = bottomSheetDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        searchFilterDD = bottomSheetDialog.findViewById(R.id.searchFilterDD);
        tv_filter_delinquent = bottomSheetDialog.findViewById(R.id.tv_filter_delinquent);
        sortByNew = bottomSheetDialog.findViewById(R.id.et_sort_standing_area);
        delinqLayout = bottomSheetDialog.findViewById(R.id.delinqLayout);
        sortByDataEntry = bottomSheetDialog.findViewById(R.id.et_sort_standing_area_l_to_h);
        etSrtNon = bottomSheetDialog.findViewById(R.id.etSrtNon);
        sortBySelected = bottomSheetDialog.findViewById(R.id.et_sort_harvest_date);
        searchFilterDD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFilterDD = resources.getStringArray(R.array.filter_array)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sortByNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                sortByNew.setTextColor(getResources().getColor(R.color.yellow));
                sortByDataEntry.setTextColor(getResources().getColor(R.color.black));
                tv_filter_delinquent.setTextColor(getResources().getColor(R.color.black));
                etSrtNon.setTextColor(getResources().getColor(R.color.black));
                sortBySelected.setTextColor(getResources().getColor(R.color.black));
                isSheetSowable = false;
                FilterAsynch asynch = new FilterAsynch(AppConstants.VETTING.FRESH);
                asynch.execute();

            }
        });
        sortByDataEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                tv_filter_delinquent.setTextColor(getResources().getColor(R.color.black));
                sortByNew.setTextColor(getResources().getColor(R.color.black));
                sortByDataEntry.setTextColor(getResources().getColor(R.color.orange));
                etSrtNon.setTextColor(getResources().getColor(R.color.black));
                sortBySelected.setTextColor(getResources().getColor(R.color.black));

                isSheetSowable = false;
                FilterAsynch asynch = new FilterAsynch(AppConstants.VETTING.DATA_ENTRY);
                asynch.execute();
            }
        });
        etSrtNon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                sortByNew.setTextColor(getResources().getColor(R.color.black));
                tv_filter_delinquent.setTextColor(getResources().getColor(R.color.black));
                sortByDataEntry.setTextColor(getResources().getColor(R.color.black));
                etSrtNon.setTextColor(getResources().getColor(R.color.black));
                sortBySelected.setTextColor(getResources().getColor(R.color.black));

                try {
                    if (isVetting) {
                        if (fetchFarmResultVettingFilter != null)
                            fetchFarmResultVettingFilter.clear();
                        fetchFarmResultVettingFilter.addAll(fetchFarmResultVetting);
                        farmDetailsAdapterVetting.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        sortBySelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                sortByNew.setTextColor(getResources().getColor(R.color.black));
                tv_filter_delinquent.setTextColor(getResources().getColor(R.color.black));
                sortByDataEntry.setTextColor(getResources().getColor(R.color.black));
                etSrtNon.setTextColor(getResources().getColor(R.color.black));
                sortBySelected.setTextColor(getResources().getColor(R.color.darkgreen));

                isSheetSowable = false;
                FilterAsynch asynch = new FilterAsynch(AppConstants.VETTING.SELECTED);
                asynch.execute();
            }
        });

        if (isDelinquencyEnabled) {
            delinqLayout.setVisibility(View.VISIBLE);
            delinqLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    tv_filter_delinquent.setTextColor(getResources().getColor(R.color.darkpurple));
                    sortByNew.setTextColor(getResources().getColor(R.color.black));
                    sortByDataEntry.setTextColor(getResources().getColor(R.color.black));
                    etSrtNon.setTextColor(getResources().getColor(R.color.black));
                    sortBySelected.setTextColor(getResources().getColor(R.color.black));
                    isSheetSowable = false;
                    FilterAsynch asynch = new FilterAsynch(AppConstants.VETTING.DELINQUENT);
                    asynch.execute();
                }
            });
        } else {
            delQLayoutHide.setVisibility(View.GONE);
            delinqLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isFarmer && !SharedPreferencesMethod.getBoolean(LandingActivity.this, SharedPreferencesMethod.OFFLINE_MODE)) {
            if (isVetting)
                getMenuInflater().inflate(R.menu.search_singl_vetting, menu);
            else
                getMenuInflater().inflate(R.menu.search_singl, menu);

            MenuItem filterItem = menu.findItem(R.id.action_filter);

            MenuItem searchMenuItem = menu.findItem(R.id.action_search);

//            searchMenuItem.setVisible(false);

            if (SharedPreferencesMethod.getBoolean(LandingActivity.this, SharedPreferencesMethod.IS_VETTING_COMP)) {
                searchMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        startActivityForResult(new Intent(context, FilterActivity.class), REQ_CODE_FILTER);
                        return false;
                    }
                });
            } else {
                SearchView searchView = (SearchView) searchMenuItem.getActionView();
                searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {

                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        try {
                            if (isVetting) {
                                if (fetchFarmResultVettingFilter != null)
                                    fetchFarmResultVettingFilter.clear();
                                fetchFarmResultVettingFilter.addAll(fetchFarmResultVetting);
                                farmDetailsAdapterVetting.notifyDataSetChanged();
                            } else {
                                if (fetchFarmResultFilter != null)
                                    fetchFarmResultFilter.clear();
                                fetchFarmResultFilter.addAll(fetchFarmResult);
                                farmDetailAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                        }

                        return true;
                    }
                });
                SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                    public boolean onQueryTextChange(String query) {
                        if (query != null && query.length() > 1) {
                            if (searchAsynch == null) {
                                searchAsynch = new SearchAsynch(query.trim());
                                searchAsynch.execute();
                            } else {
                                searchAsynch.cancel(true);
                                searchAsynch = new SearchAsynch(query.trim());
                                searchAsynch.execute();
                            }
                        } else {
                            try {
                                if (isVetting) {
                                    fetchFarmResultVettingFilter.clear();
                                    fetchFarmResultVettingFilter.addAll(fetchFarmResultVetting);
                                    farmDetailsAdapterVetting.notifyDataSetChanged();
                                } else {
                                    fetchFarmResultFilter.clear();
                                    fetchFarmResultFilter.addAll(fetchFarmResult);
                                    farmDetailAdapter.notifyDataSetChanged();

                                }
                            } catch (Exception e) {
                            }
                        }
                        return true;
                    }

                    public boolean onQueryTextSubmit(String query) {
                        if (query != null && query.length() > 1) {
                            if (searchAsynch == null) {
                                searchAsynch = new SearchAsynch(query.trim());
                                searchAsynch.execute();
                            } else {
                                searchAsynch.cancel(true);
                                searchAsynch = new SearchAsynch(query.trim());
                                searchAsynch.execute();
                            }

                        } else {
                            try {
                                if (isVetting) {
                                    fetchFarmResultVettingFilter.clear();
                                    fetchFarmResultVettingFilter.addAll(fetchFarmResultVetting);
                                    farmDetailsAdapterVetting.notifyDataSetChanged();
                                } else {
                                    fetchFarmResultFilter.clear();
                                    fetchFarmResultFilter.addAll(fetchFarmResult);
                                    farmDetailAdapter.notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                            }

                        }
                        return true;
                    }
                };
                searchView.setOnQueryTextListener(queryTextListener);

            }


            if (filterItem != null) {
                if (isVetting) {
//                    filterItem.setVisible(true);
                    filterItem.setVisible(false);
                    filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (isSheetSowable) {
//                                bottomSheetDialog.show();
                                startActivityForResult(new Intent(context, FilterActivity.class), REQ_CODE_FILTER);
                            }
                            return false;
                        }
                    });
                } else {
                    filterItem.setVisible(false);
                }
            }

            MenuItem action_refresh = menu.findItem(R.id.action_refresh);
//            action_refresh.setVisible(false);
            action_refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    getData(resources.getString(R.string.app_name));
                    return false;
                }
            });
        }
        return true;
    }

    SearchAsynch searchAsynch;

    private class FilterAsynch extends AsyncTask<String, Integer, String> {
        String type;

        public FilterAsynch(String query) {
            this.type = query.trim();
        }

        @Override
        protected String doInBackground(String... strings) {

            if (isVetting) {
                if (farmDetailsAdapterVetting != null) {
                    try {
                        fetchFarmResultVettingFilter.clear();
//                        farmDetailsAdapterVetting.notifyDataSetChanged();

                        for (int i = 0; i < fetchFarmResultVetting.size(); i++) {
                            try {
                                AdFarmDatum data = fetchFarmResultVetting.get(i);
                                FetchFarmResultNew dataa = data.getFetchFarmResultNew();
                                if (dataa.getVetting() != null && dataa.getVetting().trim().equals(type.trim())) {
                                    fetchFarmResultVettingFilter.add(data);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        LandingActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    isSheetSowable = true;
                                    farmDetailsAdapterVetting.notifyDataSetChanged();
                                } catch (Exception e) {
                                }
                            }
                        });
                    } catch (Exception e) {

                    }
                }
            } else {

            }
            return null;
        }
    }

    String selectedFilterDD = "";

    private class SearchAsynch extends AsyncTask<String, Integer, String> {
        String query;

        public SearchAsynch(String query) {
            this.query = query.trim();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (isVetting) {
                if (farmDetailsAdapterVetting != null) {
                    try {
                        fetchFarmResultVettingFilter.clear();
                        for (int i = 0; i < fetchFarmResultVetting.size(); i++) {
                            try {
                                if (AppConstants.isValidString(selectedFilterDD)) {
                                    AdFarmDatum data = fetchFarmResultVetting.get(i);
                                    FetchFarmResultNew dataa = data.getFetchFarmResultNew();

                                    if (selectedFilterDD.equalsIgnoreCase("All")) {
                                        if (dataa.getFarmName() != null && dataa.getFarmName().toLowerCase().contains(query.toLowerCase())) {
                                            fetchFarmResultVettingFilter.add(data);
                                        } else if (dataa.getLotNo() != null && dataa.getLotNo().toLowerCase().contains(query.toLowerCase())) {
                                            fetchFarmResultVettingFilter.add(data);
                                        } else if (dataa.getFarmerName() != null && dataa.getFarmerName().toLowerCase().contains(query.toLowerCase())) {
                                            fetchFarmResultVettingFilter.add(data);
                                        } else if (dataa.getMandalOrTehsil() != null && dataa.getMandalOrTehsil().toLowerCase().contains(query.toLowerCase())) {
                                            fetchFarmResultVettingFilter.add(data);
                                        } else if (dataa.getVillageOrCity() != null && dataa.getVillageOrCity().toLowerCase().contains(query.toLowerCase())) {
                                            fetchFarmResultVettingFilter.add(data);
                                        } else if (dataa.getAddL1() != null && dataa.getAddL1().toLowerCase().contains(query.toLowerCase())) {
                                            fetchFarmResultVettingFilter.add(data);
                                        } else if (dataa.getAddL2() != null && dataa.getAddL2().toLowerCase().contains(query.toLowerCase())) {
                                            fetchFarmResultVettingFilter.add(data);
                                        } /*else if (data.getDistrict() != null && data.getDistrict().toLowerCase().contains(query.toLowerCase())) {
                                            fetchFarmResultVettingFilter.add(data);
                                        }*/ else if (dataa.getLotNo() != null && dataa.getLotNo().toLowerCase().contains(query.toLowerCase())) {
                                            fetchFarmResultVettingFilter.add(data);
                                        }
                                    } else if (selectedFilterDD.equalsIgnoreCase("Project")) {
                                        if (dataa.getClusterName() != null && dataa.getClusterName().toLowerCase().contains(query.toLowerCase())) {
                                            fetchFarmResultVettingFilter.add(data);
                                        }
                                    } else if (selectedFilterDD.equalsIgnoreCase("Village")) {
                                        if (dataa.getVillageOrCity() != null && dataa.getVillageOrCity().toLowerCase().contains(query.toLowerCase())) {
                                            fetchFarmResultVettingFilter.add(data);
                                        }
                                    } else if (selectedFilterDD.equalsIgnoreCase("Chak")) {
                                        if (dataa.getAddL2() != null && dataa.getAddL2().toLowerCase().contains(query.toLowerCase())) {
                                            fetchFarmResultVettingFilter.add(data);
                                        }
                                    } else if (selectedFilterDD.equalsIgnoreCase("Khasra")) {
                                        if (dataa.getAddL1() != null && dataa.getAddL1().toLowerCase().contains(query.toLowerCase())) {
                                            fetchFarmResultVettingFilter.add(data);
                                        }
                                    } else if (selectedFilterDD.equalsIgnoreCase("Farmer")) {
                                        if (dataa.getFarmerName() != null && dataa.getFarmerName().toLowerCase().contains(query.toLowerCase())) {
                                            fetchFarmResultVettingFilter.add(data);
                                        }
                                    } else if (selectedFilterDD.equalsIgnoreCase("Farm ID")) {
                                        if (dataa.getLotNo() != null && dataa.getLotNo().toLowerCase().contains(query.toLowerCase())) {
                                            fetchFarmResultVettingFilter.add(data);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        LandingActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    farmDetailsAdapterVetting.notifyDataSetChanged();
                                } catch (Exception e) {
                                }
                            }
                        });
                    } catch (Exception e) {

                    }
                }
            } else {
                if (farmDetailAdapter != null) {
                    try {
                        fetchFarmResultFilter.clear();
                        for (int i = 0; i < fetchFarmResult.size(); i++) {
                            try {
                                AdFarmDatum data = fetchFarmResult.get(i);
                                FetchFarmResult dataa = data.getFetchFarmResult();
                                if (dataa.getFarmName() != null && dataa.getFarmName().toLowerCase().contains(query.toLowerCase())) {
                                    fetchFarmResultFilter.add(data);
                                } else if (dataa.getLotNo() != null && dataa.getLotNo().toLowerCase().contains(query.toLowerCase())) {
                                    fetchFarmResultFilter.add(data);
                                } else if (dataa.getName() != null && dataa.getName().toLowerCase().contains(query.toLowerCase())) {
                                    fetchFarmResultFilter.add(data);
                                } else if (dataa.getMandalOrTehsil() != null && dataa.getMandalOrTehsil().toLowerCase().contains(query.toLowerCase())) {
                                    fetchFarmResultFilter.add(data);
                                } else if (dataa.getVillageOrCity() != null && dataa.getVillageOrCity().toLowerCase().contains(query.toLowerCase())) {
                                    fetchFarmResultFilter.add(data);
                                } else if (dataa.getAddL1() != null && dataa.getAddL1().toLowerCase().contains(query.toLowerCase())) {
                                    fetchFarmResultFilter.add(data);
                                } else if (dataa.getAddL2() != null && dataa.getAddL2().toLowerCase().contains(query.toLowerCase())) {
                                    fetchFarmResultFilter.add(data);
                                } else if (dataa.getDistrict() != null && dataa.getDistrict().toLowerCase().contains(query.toLowerCase())) {
                                    fetchFarmResultFilter.add(data);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        LandingActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    farmDetailAdapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                }
                            }
                        });
                    } catch (Exception e) {
                    }
                }

            }
            return null;
        }
    }


    List<AdFarmDatum> fetchFarmResultVetting = new ArrayList<>();
    List<AdFarmDatum> fetchFarmResult = new ArrayList<>();
    List<AdFarmDatum> fetchFarmResultVettingFilter = new ArrayList<>();
    List<AdFarmDatum> fetchFarmResultFilter = new ArrayList<>();
//    SwipeRefreshLayout swipeRefreshLayout;

    LinearLayout legendColorLayout;
    boolean isOnlySoilSens = false;
    boolean isSoilSensEnabled = false;

    boolean mode = false;
    GifImageView loaderCacheLoad;
    String role = "";
    @BindView(R.id.offlineModeRelLayout)
    RelativeLayout offlineModeRelLayout;
    @BindView(R.id.backImage)
    ImageView backImage;
    @BindView(R.id.countTv)
    TextView countTv;
    @BindView(R.id.selectAll)
    CheckBox selectAll;
    @BindView(R.id.offlineModeImg)
    ImageView offlineModeImg;
    @BindView(R.id.taskCountTv)
    TextView taskCountTv;
    @BindView(R.id.fabSvTask)
    CardView fabSvTask;
    String auth = "";
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
    protected void onCreate(Bundle savedInstanceState) {
        String languageToLoad = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        final String userLang = SharedPreferencesMethod.getString(this, SharedPreferencesMethod.USER_LANG);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        context = this;




        loadAds();
        auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        role = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE);
        ButterKnife.bind(this);

        mode = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE);
        Log.e(TAG, "auth " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION));
        loaderCacheLoad = findViewById(R.id.loader_refr);
        delQLayoutHide = findViewById(R.id.delQLayoutHide);
//        Toast.makeText(context, ""+SharedPreferencesMethod.getString(context,SharedPreferencesMethod.LOGIN_TYPE), Toast.LENGTH_SHORT).show();
        isOnlySoilSens = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_ONLY_SOIL_SENS);
        isSoilSensEnabled = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_SOIL_SENS_ENABLED);
        Log.e("languageToLoad", "languageToLoad  " + languageToLoad);
//        isOnlySoilSens=true;
        Log.e(TAG, "isSoilSensEnabled " + isSoilSensEnabled + ", " + isOnlySoilSens);
        Log.e(TAG, "auth " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION));

        if ((AppConstants.isValidString(userLang)) && (languageCode == null || TextUtils.isEmpty(languageCode))) {
            SharedPreferencesMethod2.setString(this, SharedPreferencesMethod2.LANGUAGECODE, userLang);

        }
//        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeToRefreshmainpage);
//        swipeRefreshLayout.setColorSchemeColors(android.R.color.holo_green_dark,
//                android.R.color.holo_red_dark,
//                android.R.color.holo_blue_dark,
//                android.R.color.holo_orange_dark);
        legendColorLayout = findViewById(R.id.navigation);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        try {
            String preference = getCountryId(context);
            if (preference != null) {

                countryCodePicker.setCountryPreference(preference.toUpperCase());
                Log.e(TAG, "Preferences " + preference + " And code " + countryCodePicker.getSelectedCountryCode());
            }
        } catch (Exception e) {

        }
//        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.IS_CROSS_VETTING_ENABLED,true);
        isVetting = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_VETTING_COMP);
        isCrossVettingEnabled = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_CROSS_VETTING_ENABLED);
        isDelinquencyEnabled = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_DELINQUENT_COMP);
        initializeBottomsheet();
        connectivityLine = findViewById(R.id.connectivityLine);
        userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        personId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);

        fabParentAddFarm = findViewById(R.id.fabParentAddFarm);
        fabChildAddExistingFarm = findViewById(R.id.fabChildAddExistingFarm);
        fabChildAddFarm = findViewById(R.id.fabChildAddFarm);
        progressBar = findViewById(R.id.loader);
        no_data_available = findViewById(R.id.no_data_available);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(resources.getString(R.string.please_wait_msg));
        farmNotifyInterface = this;
        viewFailDialog = new ViewFailDialog();
        showProgress = new ShowProgress(LandingActivity.this);
        firebaseSubscription();
        if (BiometricUtils.isBiometricPromptEnabled() && BiometricUtils.isPermissionGranted(context) &&
                BiometricUtils.isFingerprintAvailable(context) && BiometricUtils.isHardwareSupported(context) &&
                !SharedPreferencesMethod2.getBoolean(context, SharedPreferencesMethod2.IS_FINGER_SELECTED) &&
                !SharedPreferencesMethod2.getBoolean(context, SharedPreferencesMethod2.IS_APP_LOCK) &&
                !SharedPreferencesMethod2.getBoolean(context, SharedPreferencesMethod2.IS_ALLOW_FINGER) &&
                !SharedPreferencesMethod2.getBoolean(context, SharedPreferencesMethod2.IS_FINGER_DONT_ASK)) {
            showFingerPrintDialog();
        }
        Double mulFact = 1.0;
        mulFact = SharedPreferencesMethod.getDoubleSharedPreferences(context, SharedPreferencesMethod.AREA_MULTIPLICATON_FACTOR);
        String area = "26";
        String con1 = AppConstants.getShowableArea(area, mulFact);
        Log.e(TAG, "token " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        Log.e(TAG, "mul factor " + mulFact);
        Log.e(TAG, "area acre " + area);
        Log.e(TAG, "area con1 " + con1);
        String con2 = AppConstants.getUploadableArea(con1, mulFact);
        Log.e(TAG, "area con2 " + con2);
        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Saisanket");
        compRegCacheManager = CompRegCacheManager.getInstance(context);
        loadCompRegData();
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.LOGIN)) {
            finishAffinity();
            startActivity(new Intent(context, LoginActivity.class));
            finish();
        }
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
        Log.e(TAG, "Person Id " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.FIRST_LOAD)) {
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.FIRST_LOAD, true);
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
            Log.e(TAG, "Coordinates farm  OnFirstLoad");

        }
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //for checking default menu item.

        if (!mode) {
            try {
                DropDownCacheManager.getInstance(context).getDataByType(new DropDownCacheManager.OnFarmerClusterFetchListener() {
                    @Override
                    public void onDataLoaded(DropDownT dropDown) {
                        if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("[]")) {
                            getPldReasons();
                        }
                    }
                }, AppConstants.CHEMICAL_UNIT.PLD_REASON);
                if (role.equalsIgnoreCase(AppConstants.ROLES.ADMIN) || role.equalsIgnoreCase(AppConstants.ROLES.ADMIN))
                    DropDownCacheManager.getInstance(context).getDataByType(new DropDownCacheManager.OnFarmerClusterFetchListener() {
                        @Override
                        public void onDataLoaded(DropDownT dropDown) {
                            if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("[]")) {

                                getClusterOfCompany();
                            }
                        }
                    }, AppConstants.CHEMICAL_UNIT.CLUSTER_LIST);
                if (!role.equalsIgnoreCase(AppConstants.ROLES.FARMER))
                    DropDownCacheManager.getInstance(context).getDataByType(new DropDownCacheManager.OnFarmerClusterFetchListener() {
                        @Override
                        public void onDataLoaded(DropDownT dropDown) {
                            if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("[]")) {
                                getFarmerList();
                            }
                        }
                    }, AppConstants.CHEMICAL_UNIT.FARMER_LIST);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String offlineDateStr = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.CACHE_DATE);
            String todayDateStr;
            Date c;
            SimpleDateFormat df;
            c = Calendar.getInstance().getTime();
            df = new SimpleDateFormat("yyyy-MM-dd");
            todayDateStr = df.format(c);
            if (AppConstants.isValidString(offlineDateStr)) {
                Date todayDate;
                Date offlineDate;
                try {
                    todayDate = df.parse(todayDateStr);
                    offlineDate = df.parse(offlineDateStr);
                    if (todayDate == null || offlineDate == null || !todayDate.equals(offlineDate)) {
                        updateCache(todayDateStr);
                    } else {
                        Log.e(TAG, "Cache Data 0");
//                   updateCache(todayDateStr);
                        elseCondition();
                    }

                } catch (ParseException e) {
                    Log.e(TAG, "Cache Data ", e);
                    e.printStackTrace();
                    updateCache(todayDateStr);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Cache Data2 ", e);
                    updateCache(todayDateStr);
                }
            } else {
                updateCache(todayDateStr);
            }
        }
//        getSuperVisors2();
//        getCalendarActData();
        if (role.equals(AppConstants.ROLES.FARMER)) {
            isFarmer = true;
        } else {
            isFarmer = false;
        }

        if (isFarmer) {
            fabSvTask.setVisibility(View.GONE);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_farmers).setVisible(false);
//            nav_Menu.findItem(R.id.nav_add_farm).setVisible(false);
            if (isSoilSensEnabled || isOnlySoilSens)
                nav_Menu.findItem(R.id.soil_sense).setVisible(true);
            else
                nav_Menu.findItem(R.id.soil_sense).setVisible(false);

//            nav_Menu.findItem(R.id.nav_vetting).setVisible(false);
            nav_Menu.findItem(R.id.nav_online).setVisible(false);
            nav_Menu.findItem(R.id.nav_task).setVisible(false);
            nav_Menu.findItem(R.id.nav_offline).setVisible(false);
//            nav_Menu.findItem(R.id.nav_harvest_paln).setVisible(false);
//            nav_Menu.findItem(R.id.nav_show_planned_harvest).setVisible(false);
            nav_Menu.findItem(R.id.nav_slideshow).setVisible(false);
            nav_Menu.findItem(R.id.nav_crop_cycle).setVisible(false);
            nav_Menu.findItem(R.id.nav_sv_expense).setVisible(false);
//            nav_Menu.findItem(R.id.crop_trac).setVisible(false);
//            nav_Menu.findItem(R.id.contactUs).setVisible(false);

        } else {
            manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            managerToUpload = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(this, LocationInsertReciever.class);
            pendingIntentFetchLocation = PendingIntent.getBroadcast(this, AppConstants.LOCATION_ALARM.LOCATION_REQ_CODE_FETCH, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            manager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 3600000, pendingIntentFetchLocation);
            managerToUpload = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intentUpload = new Intent(this, LocationUploadReceiver.class);
            pendingIntentUploadLocation = PendingIntent.getBroadcast(this, AppConstants.LOCATION_ALARM.LOCATION_REQ_CODE_UPLOAD, intentUpload, PendingIntent.FLAG_UPDATE_CURRENT);
            managerToUpload.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + 900000, 3600000, pendingIntentUploadLocation);
            Intent alarmintent = new Intent();
            alarmintent.setAction("com.example.alarm.sendGps");
            sendBroadcast(alarmintent);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        DataHandler.newInstance().setLandingActivity(this);


        if (isVetting) {
            legendColorLayout.setVisibility(View.VISIBLE);
//            legendColorLayout.setVisibility(View.GONE);
        } else {
            legendColorLayout.setVisibility(View.GONE);

        }
        String title = resources.getString(R.string.app_name);

        getData(title);

//        setActionBarTitle(title);


        View header = navigationView.getHeaderView(0);
        compNameTv = header.findViewById(R.id.compNameTv);
        String compName = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMPANY_NAME);
        if (compName != null && !TextUtils.isEmpty(compName)) {
            compNameTv.setVisibility(View.VISIBLE);
            compNameTv.setText(compName);
        } else {
            compNameTv.setVisibility(View.GONE);
        }

        imageView = header.findViewById(R.id.imageView);
        TextView userNameTv = header.findViewById(R.id.userNameTv);
        TextView userContact = header.findViewById(R.id.userContact);
        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NAME) != null && !TextUtils.isEmpty(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NAME)))
            userNameTv.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NAME));
        else
            userNameTv.setText("-");

        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NUMBER) != null && !TextUtils.isEmpty(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NUMBER)))
            userContact.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NUMBER));
        else if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_EMAIL) != null && !TextUtils.isEmpty(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_EMAIL)))
            userContact.setText(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_EMAIL));
        else
            userContact.setText("-");


        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                    Intent intent = new Intent(context, SettingActivity.class);
                    intent.putExtra("user_name", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NAME));
                    intent.putExtra("img_link", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_IMAGE));
                    ActivityOptions options = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivity(intent, options.toBundle());
                    } else {
                        startActivity(intent);
                    }
                }
            }
        });
        setActionBarTitle(String.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.CLUSTER_NAME)));
        Locale defaultLocale = new Locale(Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());
        //Log.e(TAG,"Language: " + localee.getLanguage()+", Conuntry: "+localee.getCountry());
        Locale swedishLocale = new Locale("sv", "SE");
        displayCurrencyInfoForLocale(swedishLocale);
        checkUpdate();


        /*swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isFarmer){
                    getSupervisorFarm();
                }else {
                    getFarmerFarm();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });*/

    }

    private void getData(String title) {
        if (!isFarmer) {
            if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                navigationView = (NavigationView) findViewById(R.id.nav_view);
                Menu nav_Menu = navigationView.getMenu();
                if (!SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR) &&
                        !SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.CLUSTER_ADMIN)) {
//                    nav_Menu.findItem(R.id.nav_show_planned_harvest).setVisible(false);
                    nav_Menu.findItem(R.id.nav_sv_expense).setVisible(false);
                    nav_Menu.findItem(R.id.nav_task).setVisible(false);
                    fabSvTask.setVisibility(View.GONE);
//                    nav_Menu.findItem(R.id.nav_harvest_paln).setVisible(false);
                } else {
                    nav_Menu.findItem(R.id.nav_sv_expense).setVisible(true);
                    nav_Menu.findItem(R.id.nav_task).setVisible(true);
                    fabSvTask.setVisibility(View.VISIBLE);
                    fabSvTask.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, SvTaskActivityActivity.class);
                            ActivityOptions options = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                startActivity(intent, options.toBundle());
                            } else {
                                startActivity(intent);
                            }
                        }
                    });
                    getSvTasks();
//                    nav_Menu.findItem(R.id.nav_harvest_paln).setVisible(true);
//                    nav_Menu.findItem(R.id.nav_show_planned_harvest).setVisible(true);
                }

                nav_Menu.findItem(R.id.nav_settings).setVisible(true);
                nav_Menu.findItem(R.id.nav_slideshow).setVisible(true);
                nav_Menu.findItem(R.id.nav_crop_cycle).setVisible(true);
                nav_Menu.findItem(R.id.nav_noti).setVisible(true);
                nav_Menu.findItem(R.id.nav_farmers).setVisible(false);
//                nav_Menu.findItem(R.id.nav_add_farm).setVisible(false);
                if (isSoilSensEnabled || isOnlySoilSens)
                    nav_Menu.findItem(R.id.soil_sense).setVisible(true);
                else
                    nav_Menu.findItem(R.id.soil_sense).setVisible(false);
//                nav_Menu.findItem(R.id.nav_offline).setVisible(true);
                nav_Menu.findItem(R.id.nav_offline).setVisible(false);
                nav_Menu.findItem(R.id.nav_online).setVisible(false);
//                nav_Menu.findItem(R.id.crop_trac).setVisible(true);
//                nav_Menu.findItem(R.id.contactUs).setVisible(true);
//                nav_Menu.findItem(R.id.queryAdd).setVisible(true);
                nav_Menu.findItem(R.id.signOut).setVisible(true);

                if (isVetting) {
                    nav_Menu.findItem(R.id.nav_farmers).setVisible(true);

//                    if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR)){
//                        nav_Menu.findItem(R.id.nav_vetting).setVisible(true);
//                    }else {
//                        nav_Menu.findItem(R.id.nav_vetting).setVisible(false);
//                    }

                    if (isCrossVettingEnabled && SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR)) {
//                        nav_Menu.findItem(R.id.nav_vetting).setVisible(true);
                    } else {
//                        nav_Menu.findItem(R.id.nav_vetting).setVisible(false);
                    }

                } else {
                    nav_Menu.findItem(R.id.nav_farmers).setVisible(false);
//                    nav_Menu.findItem(R.id.nav_vetting).setVisible(false);

                }

                CompRegCacheManager.getInstance(context).getCompRegData(new CompRegCacheManager.CompRegListener() {
                    @Override
                    public void onRegLoaded(CompRegModel compRegModel) {
                        if (compRegModel != null) {
                            try {
                                CompRegResult compRegResult = new Gson().fromJson(compRegModel.getData().trim(), CompRegResult.class);
                                if (compRegResult != null) {
                                    if (compRegResult.getStatus() != null && !TextUtils.isEmpty(compRegResult.getStatus()) && compRegResult.getStatus().equals("1")) {
                                        nav_Menu.findItem(R.id.signOut).setVisible(true);
                                    } else {
                                        nav_Menu.findItem(R.id.signOut).setVisible(false);
                                    }
                                } else {
                                    nav_Menu.findItem(R.id.signOut).setVisible(false);
                                }
                            } catch (Exception e) {
                                nav_Menu.findItem(R.id.signOut).setVisible(false);
                            }
                        } else if (AppConstants.COMP_REG.DEFAULT) {
                            nav_Menu.findItem(R.id.signOut).setVisible(true);
                        } else {
                            nav_Menu.findItem(R.id.signOut).setVisible(false);
                        }
                    }
                }, AppConstants.COMP_REG.CROP_TRACING);

                if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.SUPERVISOR_ALLOWED_ADD_FARM)) {
//                    nav_Menu.findItem(R.id.nav_add_farm).setVisible(false);
                } else {
//                    nav_Menu.findItem(R.id.nav_add_farm).setVisible(false);
                }
                setTheme(R.style.Theme1);
                /*if (!isVetting) {
                    nav_Menu.findItem(R.id.nav_vetting_farms).setVisible(false);
                } else {
                    nav_Menu.findItem(R.id.nav_vetting_farms).setVisible(true);

                }*/

            } else {
                fabSvTask.setVisibility(View.GONE);
                navigationView = (NavigationView) findViewById(R.id.nav_view);
                Menu nav_Menu = navigationView.getMenu();
                nav_Menu.findItem(R.id.nav_task).setVisible(false);
                nav_Menu.findItem(R.id.nav_sv_expense).setVisible(false);
                nav_Menu.findItem(R.id.nav_slideshow).setVisible(false);
                nav_Menu.findItem(R.id.nav_crop_cycle).setVisible(false);
                nav_Menu.findItem(R.id.nav_settings).setVisible(false);
                nav_Menu.findItem(R.id.nav_harvest_paln).setVisible(false);
                nav_Menu.findItem(R.id.nav_show_planned_harvest).setVisible(false);
                nav_Menu.findItem(R.id.nav_noti).setVisible(false);
                nav_Menu.findItem(R.id.nav_offline).setVisible(false);
                nav_Menu.findItem(R.id.nav_online).setVisible(true);
                nav_Menu.findItem(R.id.nav_farmers).setVisible(false);
                nav_Menu.findItem(R.id.nav_add_farm).setVisible(false);
                nav_Menu.findItem(R.id.soil_sense).setVisible(false);
                nav_Menu.findItem(R.id.crop_trac).setVisible(false);
                nav_Menu.findItem(R.id.nav_vetting).setVisible(false);
                nav_Menu.findItem(R.id.contactUs).setVisible(false);
//                nav_Menu.findItem(R.id.queryAdd).setVisible(false);
                nav_Menu.findItem(R.id.signOut).setVisible(false);
                setTheme(R.style.Theme2);
                ActivityCacheManager.getInstance(context).getAllActivities(this::onAllActivityLoaded);
                FarmCacheManager.getInstance(context).getAllFarms(this);
                HarvestCacheManager.getInstance(context).getAllHarvest(this::onAllHarvestLoaded);
                InputCostCacheManager.getInstance(context).getAllInputCost(this::onAllInputCostLoaded);
                ResourceCacheManager.getInstance(context).getAllResources(this::onAllResourceLoaded);
                VisitCacheManager.getInstance(context).getAllVisits(this);
                String offlineDateStr = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.OFFLINE_MODE_DATE);
                Date todayDate;
                Date offlineDate;
                String todayDateStr;
                Date c;
                SimpleDateFormat df;
                c = Calendar.getInstance().getTime();
                df = new SimpleDateFormat("yyyy-MM-dd");
                todayDateStr = df.format(c);
                try {
                    todayDate = df.parse(todayDateStr);
                    offlineDate = df.parse(offlineDateStr);
                    long difference = todayDate.getTime() - offlineDate.getTime();
                    float daysBetween = (difference / (1000 * 60 * 60 * 24));
                    Log.e(TAG, "No of days " + (int) daysBetween);
                    SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.NO_OF_DAYS_OFFLINE, (int) daysBetween);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mailContentLayout.setVisibility(View.GONE);

            }
            //load supervisor and admin farm

            getSupervisorFarm();
            List<String> vettingList = Arrays.asList(resources.getStringArray(R.array.vetting_array));
//            if (!isDelinquencyEnabled)
//                vettingList.remove(resources.getString(R.string.delinquent));
            List<String> filters = Arrays.asList(resources.getStringArray(R.array.filter_array));


            try {
                if (!isDelinquencyEnabled)
                    vettingList.remove(resources.getString(R.string.delinquent));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayList<String> vettingList2 = new ArrayList<>();
            vettingList2.addAll(vettingList);

            ArrayList<String> filters2 = new ArrayList<>();
            filters2.addAll(filters);
            vettingSpinnerAdapter = new SimpleStringAdapter(context, vettingList2, "Select farm status", true);
            filterSpinnerAdapter = new SimpleStringAdapter(context, filters2, "Select filter");

            vettingSpinner.setAdapter(vettingSpinnerAdapter);
            filterSpinner.setAdapter(filterSpinnerAdapter);
            vettingSpinner.setSelectedItem(1);
            filterSpinner.setSelectedItem(1);

            vettingSpinner.setStatusListener(new IStatusListener() {
                @Override
                public void spinnerIsOpening() {
                    filterSpinner.hideEdit();
                    villageSpinner.hideEdit();
                    chakSpinner.hideEdit();
                    cluster_spinner.hideEdit();
                }

                @Override
                public void spinnerIsClosing() {

                }
            });

            cluster_spinner.setStatusListener(new IStatusListener() {
                @Override
                public void spinnerIsOpening() {
                    filterSpinner.hideEdit();
                    villageSpinner.hideEdit();
                    chakSpinner.hideEdit();
                    vettingSpinner.hideEdit();
                }

                @Override
                public void spinnerIsClosing() {

                }
            });

            villageSpinner.setStatusListener(new IStatusListener() {
                @Override
                public void spinnerIsOpening() {
                    filterSpinner.hideEdit();
                    vettingSpinner.hideEdit();
                    chakSpinner.hideEdit();
                    cluster_spinner.hideEdit();
                }

                @Override
                public void spinnerIsClosing() {

                }
            });

            chakSpinner.setStatusListener(new IStatusListener() {
                @Override
                public void spinnerIsOpening() {
                    filterSpinner.hideEdit();
                    villageSpinner.hideEdit();
                    vettingSpinner.hideEdit();
                    cluster_spinner.hideEdit();
                }

                @Override
                public void spinnerIsClosing() {

                }
            });

            filterSpinner.setStatusListener(new IStatusListener() {
                @Override
                public void spinnerIsOpening() {
                    vettingSpinner.hideEdit();
                    villageSpinner.hideEdit();
                    chakSpinner.hideEdit();
                    cluster_spinner.hideEdit();
                }

                @Override
                public void spinnerIsClosing() {

                }
            });

            /*vettingSpinner.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    filterSpinner.hideEdit();
                    vettingSpinner.hideEdit();
                    cluster_spinner.hideEdit();
                    return false;
                }
            });*/


            if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR) ||
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.CLUSTER_ADMIN)) {
                title = String.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.CLUSTER_NAME));
                cluster_spinner.setVisibility(View.GONE);
                projectTv.setVisibility(View.GONE);

//                getVillageOfCluster(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
//                getVillageOfCluster(null);
//                getChaksOfVillage(null, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
            } else {
                title = String.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMPANY_NAME));
                setClusterDDData();
//                getVillageOfCluster(null);
//                getChaksOfVillage(null, null);
            }

            goButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = "";
                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FILTER_NAMES, "");
                    JsonObject filterObject = new JsonObject();
                    filterObject.addProperty("comp_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                    if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR) ||
                            SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.CLUSTER_ADMIN)) {
                        filterObject.addProperty("cluster_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
                    } else {
                        try {
                            if (clusterAdapter != null && clusterAdapter.getItem(cluster_spinner.getSelectedPosition()) != null &&
                                    clusterAdapter.getItem(cluster_spinner.getSelectedPosition()).getClusterId() != null) {
                                if (clusterAdapter.getItem(cluster_spinner.getSelectedPosition()).getClusterName() != null &&
                                        !clusterAdapter.getItem(cluster_spinner.getSelectedPosition()).getClusterName().
                                                equalsIgnoreCase("all"))
                                    name = "Project: " + clusterAdapter.getItem(cluster_spinner.getSelectedPosition()).getClusterName();
                                filterObject.addProperty("cluster_id", clusterAdapter.getItem(cluster_spinner.getSelectedPosition()).getClusterId());
//                                else
//                                    filterObject.addProperty("cluster_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
                            } else {

//                                filterObject.addProperty("cluster_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
//                            filterObject.addProperty("cluster_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
                        }
                    }
                    try {
                        if (villageSpinnerAdapter != null && villageSpinnerAdapter.getItem(villageSpinner.getSelectedPosition()) != null &&
                                villageSpinnerAdapter.getItem(villageSpinner.getSelectedPosition()).getName() != null &&
                                !villageSpinnerAdapter.getItem(villageSpinner.getSelectedPosition()).getName().equalsIgnoreCase("all")) {
                            if (AppConstants.isValidString(name)) {
                                name = name + ", Village: " + villageSpinnerAdapter.getItem(villageSpinner.getSelectedPosition()).getName();
                            } else
                                name = "Village: " + villageSpinnerAdapter.getItem(villageSpinner.getSelectedPosition()).getName();
                            filterObject.addProperty("village", villageSpinnerAdapter.getItem(villageSpinner.getSelectedPosition()).getName());
                        } else {
//                            filterObject.addProperty("village", "");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
//                        filterObject.addProperty("village", "");
                    }
                    try {
                        if (chackAdapter != null && chackAdapter.getItem(chakSpinner.getSelectedPosition()) != null &&
                                chackAdapter.getItem(chakSpinner.getSelectedPosition()).getAddl2() != null &&
                                !chackAdapter.getItem(chakSpinner.getSelectedPosition()).getAddl2().equalsIgnoreCase("all")) {

                            if (AppConstants.isValidString(name)) {
                                name = name + ", Chak:" + chackAdapter.getItem(chakSpinner.getSelectedPosition()).getAddl2();
                            } else
                                name = "Chak: " + chackAdapter.getItem(chakSpinner.getSelectedPosition()).getAddl2();

                            filterObject.addProperty("addL2", chackAdapter.getItem(chakSpinner.getSelectedPosition()).getAddl2());
                        } else {
//                            filterObject.addProperty("addL2  ", "");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
//                        filterObject.addProperty("addL2  ", "");
                    }
                    try {
                        if (vettingSpinnerAdapter != null && vettingSpinnerAdapter.getItem(vettingSpinner.getSelectedPosition()) != null &&
                                vettingSpinnerAdapter.getItem(vettingSpinner.getSelectedPosition()) != null) {
                            String vt = vettingSpinnerAdapter.getItem(vettingSpinner.getSelectedPosition());
                            int index = vettingList.indexOf(vt);
                            String isSelected = null;

                            if (vt.equals(resources.getString(R.string.all))) {
                                isSelected = "-1";

                            } else if (vt.equals(resources.getString(R.string.delinquent))) {
                                isSelected = "" + AppConstants.VETTING.DELINQUENT;
                                if (AppConstants.isValidString(name)) {
                                    name = name + ", Status: " + resources.getString(R.string.delinquent);
                                } else {
                                    name = "Status: " + resources.getString(R.string.delinquent);
                                }
                            } else if (vt.equals(resources.getString(R.string.selected_farms))) {
                                isSelected = "" + AppConstants.VETTING.SELECTED;
                                if (AppConstants.isValidString(name)) {
                                    name = name + ", Status: " + resources.getString(R.string.selected_farms);
                                } else {
                                    name = "Status: " + resources.getString(R.string.selected_farms);
                                }
                            } else if (vt.equals(resources.getString(R.string.fresh_farms))) {
                                isSelected = "" + AppConstants.VETTING.FRESH;
                                if (AppConstants.isValidString(name)) {
                                    name = name + ", Status: " + resources.getString(R.string.fresh_farms);
                                } else {
                                    name = "Status: " + resources.getString(R.string.fresh_farms);
                                }
                            } else if (vt.equals(resources.getString(R.string.data_entry_farms))) {
                                isSelected = "" + AppConstants.VETTING.DATA_ENTRY;
                                if (AppConstants.isValidString(name)) {
                                    name = name + ", Status: " + resources.getString(R.string.data_entry_farms);
                                } else {
                                    name = "Status: " + resources.getString(R.string.data_entry_farms);
                                }
                            }
                            /*if (index == 0) {
                                isSelected = "-1";
                            }
                            else if (index == 1) {
                                isSelected = "" + AppConstants.VETTING.DELINQUENT;
                            } else if (index == 2) {
                                isSelected = "" + AppConstants.VETTING.SELECTED;
                            } else if (index == 3) {
                                isSelected = "" + AppConstants.VETTING.FRESH;
                            } else if (index == 4) {
                                isSelected = "" + AppConstants.VETTING.DATA_ENTRY;
                            }*//*else if (index==0){

                            }*/
                            if (isSelected != null)
                                filterObject.addProperty("isSelected", isSelected);
                        } else {
//                            filterObject.addProperty("isSelected",null);
                        }
                        String isSelected = null;
                        if (filterSpinnerAdapter != null && filterSpinnerAdapter.getItem(filterSpinner.getSelectedPosition()) != null &&
                                filterSpinnerAdapter.getItem(filterSpinner.getSelectedPosition()) != null) {
                            String vt = filterSpinnerAdapter.getItem(filterSpinner.getSelectedPosition());
                            if (vt.equals(resources.getString(R.string.all))) {
                                isSelected = null;

                            } else if (vt.equals(resources.getString(R.string.farmer))) {
                                isSelected = "farmer";
                                if (AppConstants.isValidString(name)) {
                                    name = name + ", Filter: " + resources.getString(R.string.farmer);
                                } else {
                                    name = "Filter: " + resources.getString(R.string.farmer);
                                }
                            } else if (vt.equals(resources.getString(R.string.mobile))) {
                                isSelected = "mob";
                                if (AppConstants.isValidString(name)) {
                                    name = name + ", Filter:  " + resources.getString(R.string.mobile);
                                } else {
                                    name = "Filter: " + resources.getString(R.string.mobile);
                                }
                            } else if (vt.equals(resources.getString(R.string.khasra))) {
                                isSelected = "address_addL1";
                                if (AppConstants.isValidString(name)) {
                                    name = name + ", Filter:  " + resources.getString(R.string.khasra);
                                } else {
                                    name = "Filter: " + resources.getString(R.string.khasra);
                                }
                            } else if (vt.equals(resources.getString(R.string.lot_no))) {
                                isSelected = "comp_farm_id";
                                if (AppConstants.isValidString(name)) {
                                    name = name + ", Filter:  " + resources.getString(R.string.lot_no);
                                } else {
                                    name = "Filter: " + resources.getString(R.string.lot_no);
                                }
                            }
                            /*if (index == 0) {
                                isSelected = null;
                            } else
                            if (index == 1) {
                                isSelected = "farmer";
                            } else if (index == 2) {
                                isSelected = "mob";
                            } else if (index == 3) {
                                isSelected = "address_addL1";
                            } else if (index == 4) {
                                isSelected = "comp_farm_id";
                            }*/
                            if (isSelected != null)
                                filterObject.addProperty("filter", isSelected);
                        } else {
//                            filterObject.addProperty("isSelected",null);
                        }
                        filterObject.addProperty("query", etSearch.getText().toString().trim());
                        //order-> vetting->
                        Log.e(TAG, "DATA SEND " + new Gson().toJson(filterObject));
                        filterObject.addProperty("user_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                        filterObject.addProperty("token", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));

                        if (AppConstants.isValidString(etSearch.getText().toString())) {
                            if (AppConstants.isValidString(name)) {
                                name = name + ", " + etSearch.getText().toString();
                            } else {
                                name = etSearch.getText().toString();
                            }
                        }

                        if (AppConstants.isValidString(isSelected) && TextUtils.isEmpty(etSearch.getText().toString())) {
                            AppConstants.failToast(context, "Please enter a query to search");
                        } else {
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FILTER_JSON, new Gson().toJson(filterObject));
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FILTER_NAMES, name);
                            DataHandler.newInstance().setFilterData(filterObject);
                            Intent intent = new Intent(context, LandingActivity3.class);
                            intent.putExtra("name", "Search Results");
                            startActivity(intent);
//                            geFilterData(filterObject);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
//                        filterObject.addProperty("isSelected","");
                    }


                }
            });
//            etSearch;

        } else {
            //Farmer login
            mailContentLayout.setVisibility(View.GONE);
            getFarmerFarm();
            title = String.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.CLUSTER_NAME));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!filterSpinner.isInsideSearchEditText(event)) {
            filterSpinner.hideEdit();
        }
        return super.onTouchEvent(event);
    }

    private void getSupervisorFarm() {
        fabChildAddExistingFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabParentAddFarm.isOpened()) {
                    fabParentAddFarm.close(true);
                }
                if (AppConstants.COMP_ID.equals(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.COMP_ID))&&(
                        SharedPreferencesMethod.getDoubleSharedPreferences(context,SharedPreferencesMethod.AREA_COUNT)>=100||
                                SharedPreferencesMethod.getDoubleSharedPreferences(context,SharedPreferencesMethod.AREA_COUNT)>=10)) {
                    AppConstants.failToast(context,"Please upgrade your plan to get unlimited access of the application");


                }else{
                    Intent intent = new Intent(context, AddFarmActivity.class);
                    DataHandler.newInstance().setLatLngsCheckArea(null);
                    intent.putExtra(AppConstants.ADD_FARM.KEY, AppConstants.ADD_FARM.ADD_EXISTING_FARM);
                    ActivityOptions options = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivity(intent, options.toBundle());
                    } else {
                        startActivity(intent);
                    }
                }
            }
        });
        fabChildAddFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabParentAddFarm.isOpened()) {
                    fabParentAddFarm.close(true);
                }
                if (AppConstants.COMP_ID.equals(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.COMP_ID))&&(
                        SharedPreferencesMethod.getDoubleSharedPreferences(context,SharedPreferencesMethod.AREA_COUNT)>=100||
                                SharedPreferencesMethod.getDoubleSharedPreferences(context,SharedPreferencesMethod.AREA_COUNT)>=10)) {
                    AppConstants.failToast(context,"Please upgrade your plan to get unlimited access of the application");

                }else{

                    Intent intent = new Intent(context, AddFarmActivity.class);
                    DataHandler.newInstance().setLatLngsCheckArea(null);
                    intent.putExtra(AppConstants.ADD_FARM.KEY, AppConstants.ADD_FARM.NEW_FARM_AND_FARMER);
                    ActivityOptions options = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivity(intent, options.toBundle());
                    } else {
                        startActivity(intent);
                    }
                }
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        no_data_available.setVisibility(View.GONE);
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            int currentPage = 1;
            FetchFarmSendData fetchFarmSendData = new FetchFarmSendData();
            fetchFarmSendData.setOffset(String.valueOf(currentPage));
//            fetchFarmSendData.setSize(String.valueOf(currentPage));
            Log.e(TAG, "current_page " + String.valueOf(currentPage));
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            fetchFarmSendData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
            fetchFarmSendData.setCluster_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
            fetchFarmSendData.setOrder(null);
            fetchFarmSendData.setSort_by(null);
            fetchFarmSendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            fetchFarmSendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            Log.e(TAG, "Get Farms " + new Gson().toJson(fetchFarmSendData) + " isVetting " + isVetting);
//            isVetting=false;
            if (!isVetting) {
                fetchFarmSendData.setPage_items("500000");
                Log.e(TAG, "Get Farms " + new Gson().toJson(fetchFarmSendData) + " isVetting " + isVetting);
                apiService.fetchFarmDatafncn(fetchFarmSendData).enqueue(new Callback<FetchFarmData>() {
                    @Override
                    public void onResponse(Call<FetchFarmData> call, Response<FetchFarmData> response) {
                        try {
                            Log.e(TAG, "Get Farm code1 " + response.code());
                            if (response.isSuccessful()) {
                                Log.e(TAG, "Get Farm Res " + new Gson().toJson(response.body()));
                                if (response != null) {
                                    FetchFarmData fetchFarmData = response.body();
                                    if (response.body().getStatus() == 10) {
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.multiple_login_msg));
                                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                        //un authorized access
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                                    } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                        // auth token expired
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                                    } else if (fetchFarmData.getResult() != null) {
                                        no_data_available.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        if (fetchFarmData.getStatus() != 0) {
                                            List<TimelineUnits> timelineUnits = new ArrayList<>();
                                            timelineUnits = fetchFarmData.getUnit();
                                            DataHandler.newInstance().setTimelineUnits(timelineUnits);
                                            fetchFarmResult.clear();
                                            fetchFarmResultFilter.clear();
                                            float area=0.0f;
                                            for (int i=0;i<fetchFarmData.getResult().size();i++) {
                                                FetchFarmResult farmResult=fetchFarmData.getResult().get(i);
                                                if (i%3==0){
                                                    fetchFarmResult.add(new AdFarmDatum(0));
                                                }
                                                fetchFarmResult.add(new AdFarmDatum(1,farmResult));
                                                if (AppConstants.isValidActualArea(farmResult.getStandingAcres())){
                                                    area=area+Float.valueOf(farmResult.getStandingAcres());
                                                }
//                                                fetchFarmResult = fetchFarmData.getResult();
                                            }
                                            if (fetchFarmResult != null)
                                                fetchFarmResultFilter.addAll(fetchFarmResult);

                                            farmDetailAdapter = new FarmDetailAdapter(context, fetchFarmResultFilter);
                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                            recyclerView.setLayoutManager(linearLayoutManager);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            recyclerView.setHasFixedSize(true);

//                                            farmDetailAdapter.removeLoadingFooter();
                                            recyclerView.setNestedScrollingEnabled(false);
                                            recyclerView.setAdapter(farmDetailAdapter);
                                            SharedPreferencesMethod.setDoubleSharedPreference(context,
                                                    SharedPreferencesMethod.FARM_COUNT,fetchFarmData.getResult().size());

                                            SharedPreferencesMethod.setDoubleSharedPreference(context,
                                                    SharedPreferencesMethod.AREA_COUNT,area);
                                        } else {
                                            SharedPreferencesMethod.setDoubleSharedPreference(context,
                                                    SharedPreferencesMethod.FARM_COUNT,0);
                                            no_data_available.setVisibility(View.VISIBLE);
                                            recyclerView.setVisibility(View.GONE);
                                            SharedPreferencesMethod.setDoubleSharedPreference(context,
                                                    SharedPreferencesMethod.AREA_COUNT,0);
                                        }
                                    } else {
                                        SharedPreferencesMethod.setDoubleSharedPreference(context,
                                                SharedPreferencesMethod.FARM_COUNT,0);
                                        SharedPreferencesMethod.setDoubleSharedPreference(context,
                                                SharedPreferencesMethod.AREA_COUNT,0);
                                        no_data_available.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                }
                            } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                            } else {
                                try {
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showDialog(LandingActivity.this, resources.getString(R.string.failed_load_farm));

                                    //Toast.makeText(context, "Oops something went wrong. Please Reload", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Get Farm Error1 " + response.errorBody().string().toString());

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {


                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<FetchFarmData> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (t instanceof SocketTimeoutException) {
                            getSupervisorFarm();
                        }

                        Log.e(TAG, "Get Farm Exception1 " + t.toString());
                    }
                });
            } else {

                getFarmCounts();
            }
        } else {
            legendColorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
//            no_internet_layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            FarmCacheManager.getInstance(context).getAllFarms(new FarmNotifyInterface() {
                @Override
                public void onFarmLoaded(List<Farm> farmList) {
                    if (farmList != null && farmList.size() > 0) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        FarmDetailAdapterOffline adapterOffline = new FarmDetailAdapterOffline(farmList, context);
                        recyclerView.setAdapter(adapterOffline);
                        progressBar.setVisibility(View.INVISIBLE);
                        recyclerView.setNestedScrollingEnabled(true);

                    } else {
                        Log.e(TAG, "Offline farm No ");
                        progressBar.setVisibility(View.GONE);
                        no_data_available.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
//                        no_internet_layout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFarmAdded() {

                }

                @Override
                public void onFarmInsertError(Throwable e) {

                }

                @Override
                public void onNoFarmsAvailable() {

                }

                @Override
                public void farmDeleted() {

                }

                @Override
                public void farmDeletionFaild() {

                }
            });
        }
    }

    private void getFarmCounts() {
//        getGeoCount();
        progressBar.setVisibility(View.VISIBLE);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String clusterId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        JsonObject object = new JsonObject();
        object.addProperty("cluster_id", "" + clusterId);
        object.addProperty("comp_id", "" + compId);
        object.addProperty("user_id", "" + userId);
        object.addProperty("token", "" + token);
        Log.e(TAG, "FARM COUNT REQ " + new Gson().toJson(object));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getVettingFarmCounts(clusterId, compId, userId, token).enqueue(new Callback<FarmCountResponse>() {
            @Override
            public void onResponse(Call<FarmCountResponse> call, Response<FarmCountResponse> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    FarmCountResponse countResponse = response1.body();
                    Log.e(TAG, "FARM COUNT Response " + new Gson().toJson(countResponse));
                    if (countResponse.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, countResponse.getMessage());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        if (countResponse != null && countResponse.getFarms() != null && countResponse.getFarms().size() > 0) {
                            int totalCount = 0;
                            for (int i = 0; i < countResponse.getFarms().size(); i++) {
                                FarmCountDatum datum = countResponse.getFarms().get(i);
                                if (AppConstants.isValidString(datum.getCount())) {
                                    try {
                                        totalCount = totalCount + Integer.valueOf(datum.getCount().trim());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            if (totalCount > 0) {
                                TOTAL_PAGES = ((int) Math.floor(totalCount / OFFSET_SIZE)) + 1;
                                Log.e(TAG, "FARM COUNT TOTAL COUNT " + totalCount + "\n" + "TOTAL PAGE " + TOTAL_PAGES + "\n");
                                getVettingFarms();

                                totalFarmTv.setText(totalCount + "");
                                SharedPreferencesMethod.setDoubleSharedPreference(context,
                                        SharedPreferencesMethod.FARM_COUNT,totalCount);

                            }else{
                                SharedPreferencesMethod.setDoubleSharedPreference(context,
                                        SharedPreferencesMethod.FARM_COUNT,0);

                            }

                        } else {

                            totalFarmTv.setText("0");
                            TOTAL_PAGES = 1000 + 1;
//                            Log.e(TAG, "FARM COUNT TOTAL COUNT " + totalCount + "\n" + "TOTAL PAGE " + TOTAL_PAGES + "\n");
                            getVettingFarms();
                        }
                    }
                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        TOTAL_PAGES = 1000 + 1;
//                            Log.e(TAG, "FARM COUNT TOTAL COUNT " + totalCount + "\n" + "TOTAL PAGE " + TOTAL_PAGES + "\n");
                        getVettingFarms();
                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "FARM COUNT Error " + error + " Code " + response1.code() + " ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<FarmCountResponse> call, Throwable t) {
                try {
                    TOTAL_PAGES = 1000 + 1;
                    getVettingFarms();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "FARM COUNT Failure  " + t.toString());
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

    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.goButton)
    Button goButton;

    @BindView(R.id.totalFarmTv)
    TextView totalFarmTv;
    @BindView(R.id.totalGeoFenceAreaTv)
    TextView totalGeoFenceAreaTv;
    @BindView(R.id.getfencedFarmCountTv)
    TextView getfencedFarmCountTv;
    @BindView(R.id.nonGetfencedFarmCountTv)
    TextView nonGetfencedFarmCountTv;
    @BindView(R.id.mailContentLayout)
    LinearLayout mailContentLayout;
    @BindView(R.id.cluster_spinner)
    SearchableSpinner cluster_spinner;
    @BindView(R.id.projectTv)
    TextView projectTv;

    @BindView(R.id.villageSpinner)
    SearchableSpinner villageSpinner;
    @BindView(R.id.chakSpinner)
    SearchableSpinner chakSpinner;
    @BindView(R.id.vettingSpinner)
    SearchableSpinner vettingSpinner;
    @BindView(R.id.filterSpinner)
    SearchableSpinner filterSpinner;
    SpinnerAdapterCluster clusterAdapter;
    SpinnerAdapterVillage villageSpinnerAdapter;
    SpinnerAdapterchak chackAdapter;
    SimpleStringAdapter vettingSpinnerAdapter;
    SimpleStringAdapter filterSpinnerAdapter;

    private void setClusterDDData() {
        projectTv.setVisibility(View.VISIBLE);
        cluster_spinner.setVisibility(View.VISIBLE);
        DropDownCacheManager.getInstance(context).getDataByType(new DropDownCacheManager.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT dropDown) {
                Log.e(TAG, "Offline clusters " + new Gson().toJson(dropDown));

                if (dropDown != null && AppConstants.isValidString(dropDown.getData()) && !dropDown.getData().trim().equals("[]")) {
                    try {
                        JSONArray array = new JSONArray(dropDown.getData());

                        List<Cluster> clusterList = new ArrayList<>();
                        Cluster clusterr = new Cluster();
                        clusterr.setClusterName("All");
                        clusterr.setClusterId("0");
                        clusterList.add(clusterr);
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
                } else {
                    getClusterOfCompany();
                }
            }
        }, AppConstants.CHEMICAL_UNIT.CLUSTER_LIST);
    }

    private void setClusterData(List<Cluster> list) {
        ArrayList<Cluster> clusterList = new ArrayList<>();
        clusterList.addAll(list);
        clusterAdapter = new SpinnerAdapterCluster(context, clusterList);
        cluster_spinner.setAdapter(clusterAdapter);
        cluster_spinner.setSelectedItem(1);
        cluster_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                if (i > 0) {
                    String clusterId = clusterAdapter.getItem(i).getClusterId();
                    getVillageOfCluster(clusterId);
                }
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }

    private void setVillageData(List<ClusterVillages> list, String clusterId) {
        ArrayList<ClusterVillages> villages = new ArrayList<>();
        ClusterVillages clusterVillages = new ClusterVillages();
        clusterVillages.setName("All");
        villages.add(clusterVillages);

        villages.addAll(list);
        villageSpinnerAdapter = new SpinnerAdapterVillage(context, villages);
        villageSpinner.setAdapter(villageSpinnerAdapter);
        villageSpinner.setSelectedItem(1);
        villageSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                if (i > 0) {
                    String village = villageSpinnerAdapter.getItem(i).getName();
                    getChaksOfVillage(village, clusterId);
                }
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }

    private void setChakData(List<VillageChak> list) {
        ArrayList<VillageChak> villages = new ArrayList<>();
        VillageChak villageChak = new VillageChak();
        villageChak.setAddl2("All");
        villages.add(villageChak);
        villages.addAll(list);
        chackAdapter = new SpinnerAdapterchak(context, villages);

        chakSpinner.setAdapter(chackAdapter);
        chakSpinner.setSelectedItem(1);
        chakSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                if (i > 0) {

                }
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }

    private void getVillageOfCluster(String clusterId) {
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        JsonObject jsonObject = new JsonObject();
        if (clusterId != null && !clusterId.equals("0"))
            jsonObject.addProperty("cluster_id", "" + clusterId);
        else getChaksOfVillage(null, null);
        jsonObject.addProperty("comp_id", "" + compId);
        jsonObject.addProperty("token", "" + token);
        jsonObject.addProperty("user_id", "" + userId);
        Log.e(TAG, "getVillageOfCluster PARAM " + new Gson().toJson(jsonObject));
        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getClusterVillages(jsonObject).enqueue(new Callback<ClusterVillageResponse>() {
            @Override
            public void onResponse(Call<ClusterVillageResponse> call, Response<ClusterVillageResponse> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    ClusterVillageResponse villageResponse = response1.body();
                    /*try {
                        Log.e(TAG, "getVillageOfCluster Response " + response1.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
//                    Log.e(TAG, "getGeoCount Response " + new Gson().toJson(countResponse));
                    if (villageResponse.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this);
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        if (villageResponse.getData() != null)
                            setVillageData(villageResponse.getData(), clusterId);

                    }
                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {

                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "getVillageOfCluster Error " + error + " Code " + response1.code() + " ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ClusterVillageResponse> call, Throwable t) {
                Log.e(TAG, "getVillageOfCluster Failure  " + t.toString());
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

    private void getChaksOfVillage(String village, String clusterId) {
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        JsonObject jsonObject = new JsonObject();
        if (village != null && !village.equalsIgnoreCase("all"))
            jsonObject.addProperty("village", "" + village);
        if (clusterId != null && !clusterId.equals("0"))
            jsonObject.addProperty("cluster_id", "" + clusterId);
        jsonObject.addProperty("comp_id", "" + compId);
        jsonObject.addProperty("token", "" + token);
        jsonObject.addProperty("user_id", "" + userId);
        Log.e(TAG, "getChaksOfVillage PARAM " + new Gson().toJson(jsonObject));
        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getChaksOfVillage(jsonObject).enqueue(new Callback<VillageChakResponse>() {
            @Override
            public void onResponse(Call<VillageChakResponse> call, Response<VillageChakResponse> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    VillageChakResponse villageResponse = response1.body();
                    Log.e(TAG, "getChaksOfVillage " + new Gson().toJson(villageResponse));
                   /* try {
                        Log.e(TAG, "getChaksOfVillage Response " + response1.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
//                    Log.e(TAG, "getGeoCount Response " + new Gson().toJson(countResponse));
                    if (villageResponse.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, villageResponse.getMessage());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        if (villageResponse.getData() != null) {
                            setChakData(villageResponse.getData());
                        }

                    }
                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {

                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "getChaksOfVillage Error " + error + " Code " + response1.code() + " ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<VillageChakResponse> call, Throwable t) {
                Log.e(TAG, "getChaksOfVillage Failure  " + t.toString());
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

    private void getGeoCount() {
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String clusterId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("cluster_id", "" + clusterId);
        jsonObject.addProperty("comp_id", "" + compId);
        jsonObject.addProperty("token", "" + token);
        jsonObject.addProperty("user_id", "" + userId);
        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getGeoFenceCount(jsonObject).enqueue(new Callback<GeoCardsResponse>() {
            @Override
            public void onResponse(Call<GeoCardsResponse> call, Response<GeoCardsResponse> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    GeoCardsResponse countResponse = response1.body();
                    Log.e(TAG, "getGeoCount Response " + new Gson().toJson(countResponse));
                    if (countResponse.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, countResponse.getMessage());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        if (AppConstants.isValidString(countResponse.getGeoTaggedArea())) {
                            totalGeoFenceAreaTv.setText(countResponse.getGeoTaggedArea());
                        } else {
                            totalGeoFenceAreaTv.setText("0");
                        }
                        if (countResponse.getGeoTaggedCount() != null) {
                            getfencedFarmCountTv.setText(countResponse.getGeoTaggedCount() + "");
                        } else {
                            getfencedFarmCountTv.setText("0");
                        }
                        if (countResponse.getNonGeoTagged() != null) {
                            nonGetfencedFarmCountTv.setText(countResponse.getNonGeoTagged() + "");
                        } else {
                            nonGetfencedFarmCountTv.setText("0");
                        }
                    }
                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {

                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "getGeoCount Error " + error + " Code " + response1.code() + " ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GeoCardsResponse> call, Throwable t) {
                Log.e(TAG, "getGeoCount Failure  " + t.toString());
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


    private int OFFSET = 0;
    private int OFFSET_SIZE = 11;
    private int TOTAL_PAGES = 1;
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    //Add Ad every 4th index of farm list


    private void getVettingFarms() {
        OFFSET = 0;
        progressBar.setVisibility(View.VISIBLE);
        OFFSET_SIZE = 11;
        currentPage = 0;
        isLoading = false;
        isLastPage = false;
        FetchFarmSendData fetchFarmSendData = new FetchFarmSendData();
        fetchFarmSendData.setOffset(String.valueOf(OFFSET));
        fetchFarmSendData.setSize(String.valueOf(OFFSET_SIZE));
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        fetchFarmSendData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        fetchFarmSendData.setCluster_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
        fetchFarmSendData.setOrder(null);
        fetchFarmSendData.setSort_by(null);
        fetchFarmSendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        fetchFarmSendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        long currMillis = AppConstants.getCurrentMills();
        Log.e(TAG, "Vetting Rreq " + new Gson().toJson(fetchFarmSendData));
        apiService.getAllVetting(fetchFarmSendData).enqueue(new Callback<FarmResponseNew>() {
            @Override
            public void onResponse(Call<FarmResponseNew> call, Response<FarmResponseNew> response) {
                String error = null;
                Log.e(TAG, "Get Farm code" + response.code());
                try {
                    if (response.isSuccessful()) {
                        Log.e(TAG, "Get Farm Res  P,  " + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                        Log.e(TAG, "Get Farm Res  ,  " + new Gson().toJson(response.body()));
                        if (response != null) {
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this);
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                            } else if (response.body().getStatus() == 0) {
//                                if (fetchFarmResultVetting == null && fetchFarmResultVetting.size() == 0) {

                                no_data_available.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                Log.e(TAG, "Get Farm No Data");
//                                }
                            } else {
                                FarmResponseNew fetchFarmData = response.body();
                                if (fetchFarmData.getData() != null && fetchFarmData.getData().size() > 0) {
                                    no_data_available.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    fetchFarmResultVetting.clear();
                                    fetchFarmResultVettingFilter.clear();
                                    double area=0.0f;
                                    for (int i=0;i<fetchFarmData.getData().size();i++){
                                        FetchFarmResultNew resultNew=fetchFarmData.getData().get(i);
                                        if (i%3==0){
                                            fetchFarmResultVetting.add(new AdFarmDatum(0));
                                        }
                                        if(resultNew.getStandingArea()!=null)
                                        area=area+resultNew.getStandingArea();
                                        fetchFarmResultVetting.add(new AdFarmDatum(1,resultNew));
                                    }
                                    fetchFarmResultVetting.add(new AdFarmDatum(0));


                                    SharedPreferencesMethod.setDoubleSharedPreference(context,
                                            SharedPreferencesMethod.AREA_COUNT,area);

                                    fetchFarmResultVettingFilter.addAll(fetchFarmResultVetting);
//                                    if (farmDetailsAdapterVetting!=null)

                                    farmDetailsAdapterVetting = new VettingFarmAdapterNew(context, fetchFarmResultVettingFilter, new VettingFarmAdapterNew.ItemClickListener() {
                                        @Override
                                        public void onItemClicked(int index, FetchFarmResultNew farmResultNew, String vetting) {
                                            if (AppConstants.isValidString(vetting)) {
                                                int code = 99;
                                                selectedPreviousFarm = farmResultNew;
                                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARM_VETTING, vetting);
                                                selectedPreviousIndex = index;
                                                selectedPreviousVetting = vetting;
                                                Intent intent = null;
                                                if (vetting != null && (vetting.trim().equals(AppConstants.VETTING.SELECTED) ||
                                                        vetting.trim().equals(AppConstants.VETTING.DELINQUENT))) {
                                                    if (isOnlySoilSens) {
                                                        intent = new Intent(context, SoilSensDashboardActivity.class);
                                                    } else
                                                        intent = new Intent(context, TestActivity.class);
                                                    code = 99;
                                                } else if (isOnlySoilSens) {
                                                    intent = new Intent(context, SoilSensDashboardActivity.class);
                                                    code = 99;
                                                } else {
                                                    intent = new Intent(context, FarmDetailsVettingActivity.class);
                                                    code = 9930;
                                                }

                                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTUAL_AREA, farmResultNew.getActualArea() + "");
                                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, farmResultNew.getStandingArea() + "");
                                                SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.EXPECTED_AREA,
                                                        Double.valueOf(farmResultNew.getExpArea()));
                                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, farmResultNew.getFarmMasterNum());
//                                                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, /*farmResultNew.getFarmMasterNum()*/"2349");
//                                                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.COMP_ID, /*farmResultNew.getFarmMasterNum()*/"100131");
                                                startActivityForResult(intent, code);
                                            }
                                        }
                                    });
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    recyclerView.setAdapter(farmDetailsAdapterVetting);

                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setNestedScrollingEnabled(true);


                                    paginationScrollListener = new PaginationScrollListener(linearLayoutManager) {
                                        @Override
                                        protected void loadMoreItems() {
                                            isLoading = true;
                                            currentPage += 1;
                                            OFFSET = OFFSET + OFFSET_SIZE;
                                            loadSecondPageVetting();
                                        }

                                        @Override
                                        public int getTotalPageCount() {
                                            return TOTAL_PAGES;
                                        }

                                        @Override
                                        public boolean isLastPage() {
                                            return isLastPage;
                                        }

                                        @Override
                                        public boolean isLoading() {
                                            return isLoading;
                                        }
                                    };
                                    recyclerView.addOnScrollListener(paginationScrollListener);


                                    if (currentPage <= TOTAL_PAGES)
                                        farmDetailsAdapterVetting.addLoadingFooter();
                                    else isLastPage = true;

                                    farmDetailsAdapterVetting.setLongPressListener(new VettingFarmAdapterNew.OnLongPressListener() {
                                        @Override
                                        public void onLongPressed(int index) {
//                                            fetchFarmResultVettingFilter.get(index).setSelectedd(true);
                                            farmDetailsAdapterVetting.setLongPressed(true);
                                            countTv.setText(index + "/" + fetchFarmResultVettingFilter.size() + " selected");
                                            offlineModeRelLayout.setVisibility(View.VISIBLE);
                                        }
                                    });

                                    selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            int count = 0;
                                            if (isChecked) {
                                                for (int i = 0; i < fetchFarmResultVettingFilter.size(); i++) {
                                                    fetchFarmResultVettingFilter.get(i).getFetchFarmResultNew().setSelectedd(true);
                                                }
                                                countTv.setText(fetchFarmResultVettingFilter.size() + "/" + fetchFarmResultVettingFilter.size() + " selected");
                                                count = fetchFarmResultVettingFilter.size();
                                            } else {
                                                for (int i = 0; i < fetchFarmResultVettingFilter.size(); i++) {
                                                    fetchFarmResultVettingFilter.get(i).getFetchFarmResultNew().setSelectedd(false);
                                                }
                                                countTv.setText("0/" + fetchFarmResultVettingFilter.size() + " selected");
                                                count = 0;
                                            }
                                            try {
                                                farmDetailsAdapterVetting.setCount(count);
                                                farmDetailsAdapterVetting.notifyDataSetChanged();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    offlineModeImg.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                                            Toast.makeText(context, "Please wait...", Toast.LENGTH_SHORT).show();
                                            if (checkPermission()) {
                                                backImage.setClickable(false);
                                                backImage.setEnabled(false);
                                                selectAll.setClickable(false);
                                                selectAll.setEnabled(false);
                                                getFarmIdList();
                                            } else {
                                                requestPermission();
                                            }
                                        }
                                    });
                                    backImage.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            offlineModeRelLayout.setVisibility(View.GONE);
                                            for (int i = 0; i < fetchFarmResultVettingFilter.size(); i++) {
                                                fetchFarmResultVettingFilter.get(i).getFetchFarmResultNew().setSelectedd(false);
                                            }
                                            selectAll.setChecked(false);
                                            farmDetailsAdapterVetting.setLongPressed(false);

                                        }
                                    });


                                } else {

                                    no_data_available.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                            }
                        } else {

                            no_data_available.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
//                                no_internet_layout.setVisibility(View.GONE);
                            Log.e(TAG, "Get Farm No Data 2");
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialog(LandingActivity.this, resources.getString(R.string.failed_load_farm));

                            //Toast.makeText(context, "Oops something went wrong. Please Reload", Toast.LENGTH_SHORT).show();
                            error = response.errorBody().string().toString();
                            Log.e(TAG, "Get Farm Error " + error);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    try {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(context, resources.getString(R.string.failed_load_farm));

                        error = response.errorBody().string().toString();
                        Log.e(TAG, "Get Farm Error2" + error);
                    } catch (Exception f) {
                        f.printStackTrace();

                    }

                    e.printStackTrace();
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
                    notifyApiDelay(LandingActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FarmResponseNew> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(LandingActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                progressBar.setVisibility(View.GONE);
                try {
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showDialog(LandingActivity.this, resources.getString(R.string.failed_load_farm));

                    //Toast.makeText(context, "Oops something went wrong. Please Reload", Toast.LENGTH_SHORT).show();
                } catch (Exception f) {
                    f.printStackTrace();
                }                //Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Get Farm Farms Exception" + t.toString());
            }
        });
    }

    private void loadSecondPageVetting() {
        FetchFarmSendData fetchFarmSendData = new FetchFarmSendData();
        fetchFarmSendData.setOffset(String.valueOf(OFFSET));
        fetchFarmSendData.setSize(String.valueOf(OFFSET_SIZE));
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        fetchFarmSendData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        fetchFarmSendData.setCluster_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID));
        fetchFarmSendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        fetchFarmSendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        Log.e(TAG, "loadSecondPageVetting REQ " + new Gson().toJson(fetchFarmSendData));

        apiService.getAllVetting(fetchFarmSendData).enqueue(new Callback<FarmResponseNew>() {
            @Override
            public void onResponse(Call<FarmResponseNew> call, Response<FarmResponseNew> response) {
                String error = null;
                Log.e(TAG, "loadSecondPageVetting code" + response.code());
                try {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        Log.e(TAG, "loadSecondPageVetting Res " + new Gson().toJson(response.body()));
                        FarmResponseNew fetchFarmData = response.body();

                        if (fetchFarmData.getData() != null && fetchFarmData.getData().size() > 0) {
                            farmDetailsAdapterVetting.removeLoadingFooter();

                            for (int i=0;i<fetchFarmData.getResult().size();i++) {
                                FetchFarmResultNew farmResult=fetchFarmData.getData().get(i);
                                if (i%3==0){
                                    fetchFarmResult.add(new AdFarmDatum(0));
                                }
                                fetchFarmResult.add(new AdFarmDatum(1,farmResult));
                                fetchFarmResultVettingFilter.add(new AdFarmDatum(1,farmResult));
//                                                fetchFarmResult = fetchFarmData.getResult();
                            }

//                            fetchFarmResultVetting.addAll(fetchFarmData.getData());
//                            fetchFarmResultVettingFilter.addAll(fetchFarmData.getData());
                            isLoading = false;
//                            farmDetailsAdapterVetting.addAll(fetchFarmData.getData());
                            farmDetailsAdapterVetting.notifyDataSetChanged();
                            if (currentPage != TOTAL_PAGES)
                                farmDetailsAdapterVetting.addLoadingFooter();
                            else isLastPage = true;

                        } else {
                            farmDetailsAdapterVetting.removeLoadingFooter();
                            isLastPage = true;

                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            progressBar.setVisibility(View.INVISIBLE);
                            error = response.errorBody().string().toString();
                            Log.e(TAG, "loadSecondPageVetting Error " + error);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    try {
                        progressBar.setVisibility(View.INVISIBLE);
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "loadSecondPageVetting Error2" + error);
                    } catch (Exception f) {
                        f.printStackTrace();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<FarmResponseNew> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, "loadSecondPageVetting Failure" + t.toString());
            }
        });
    }

    private void geFilterData(JsonObject jsonObject) {

    }

    private void getFarmerFarm() {
        fabChildAddFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabParentAddFarm.isOpened()) {
                    fabParentAddFarm.close(true);
                }
                if (AppConstants.COMP_ID.equals(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.COMP_ID))&&(
                        SharedPreferencesMethod.getDoubleSharedPreferences(context,SharedPreferencesMethod.AREA_COUNT)<100||
                                SharedPreferencesMethod.getDoubleSharedPreferences(context,SharedPreferencesMethod.AREA_COUNT)<10)) {
                    Intent intent = new Intent(context, AddFarmActivity.class);
                    DataHandler.newInstance().setLatLngsCheckArea(null);
                    intent.putExtra(AppConstants.ADD_FARM.KEY, AppConstants.ADD_FARM.NEW_FARM_AND_FARMER);
                    ActivityOptions options = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivity(intent, options.toBundle());
                    } else {
                        startActivity(intent);
                    }
                }
            }
        });
        fabChildAddExistingFarm.hide(true);
        fabChildAddExistingFarm.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String personId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("owner_id", "806");
        jsonObject.addProperty("user_id", userId);
        jsonObject.addProperty("token", token);
        Log.e(TAG, "Farmer data Id " + new Gson().toJson(jsonObject));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getFarmerFarms(jsonObject).enqueue(new Callback<FetchFarmerFarmResponse>() {
            @Override
            public void onResponse(Call<FetchFarmerFarmResponse> call, Response<FetchFarmerFarmResponse> response) {
                String error = null;
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.e(TAG, "Farmer Farms Resp " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == 1) {
                        if (response.body().getFarm() != null) {
                            FarmerFarmAdapter adapter = new FarmerFarmAdapter(context, response.body().getFarm());
                            recyclerView.setAdapter(adapter);
                            recyclerView.setHasFixedSize(true);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());

                            recyclerView.setNestedScrollingEnabled(true);
                        } else {
                            //No Active Farms Available
                            recyclerView.setVisibility(View.GONE);
                            no_data_available.setVisibility(View.VISIBLE);
                        }

                    } else {
                        recyclerView.setVisibility(View.GONE);
                        no_data_available.setVisibility(View.VISIBLE);
                        Toasty.error(context, response.body().getMsg(), Toast.LENGTH_LONG, false).show();
                        /*ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(LandingActivity.this, resources.getString(R.string.failed_load_farm));*/
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString() + " code:" + response.code();
                        Log.e(TAG, "Farmer Farm Err " + error);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(LandingActivity.this, resources.getString(R.string.failed_load_farm));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
                    notifyApiDelay(LandingActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<FetchFarmerFarmResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(LandingActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                progressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, "Farmer Farm fail " + t.toString());
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialog(LandingActivity.this, resources.getString(R.string.failed_load_farm));
            }
        });
        internetFlow(isLoaded[0]);

    }

    private void getAppTour() {
        Log.e(TAG, "apptour start");
        ShowcaseConfig config = new ShowcaseConfig(); //create the showcase config
        config.setDelay(0); //set the delay of each sequence using millis variable
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);
        sequence.setConfig(config);
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setSkipText(getResources().getString(R.string.tour_skip))
                        .setTarget(findViewById(R.id.navigation_stats))
                        .setTitleText(getResources().getString(R.string.tour_dashboard_title))
                        .setDismissText(getResources().getString(R.string.got_it))
                        .setContentText(getResources().getString(R.string.tour_dashboard_content))
                        .build());
        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.FARMER)) {
            sequence.addSequenceItem(
                    new MaterialShowcaseView.Builder(this)
                            .setSkipText(getResources().getString(R.string.tour_skip))
                            .setTarget(findViewById(R.id.navigation_farm))
                            .setTitleText(getResources().getString(R.string.tour_home_title))
                            .setDismissText(getResources().getString(R.string.got_it))
                            .setContentText(getResources().getString(R.string.tour_home_farmer_content))
                            .build());
        } else if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR) ||
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.CLUSTER_ADMIN)) {
            sequence.addSequenceItem(
                    new MaterialShowcaseView.Builder(this)
                            .setSkipText(getResources().getString(R.string.tour_skip))
                            .setTarget(findViewById(R.id.navigation_farm))
                            .setTitleText(getResources().getString(R.string.tour_home_title))
                            .setDismissText(getResources().getString(R.string.got_it))
                            .setContentText(getResources().getString(R.string.tour_home_supervisor_content))
                            .build());
        } else if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.ADMIN) ||
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPER_ADMIN) ||
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUB_ADMIN)) {
            sequence.addSequenceItem(
                    new MaterialShowcaseView.Builder(this)
                            .setSkipText(getResources().getString(R.string.tour_skip))
                            .setTarget(findViewById(R.id.navigation_farm))
                            .setTitleText(getResources().getString(R.string.tour_home_title))
                            .setDismissText(getResources().getString(R.string.got_it))
                            .setContentText(getResources().getString(R.string.tour_home_admin_content))
                            .build());
        }
        try {


            if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR) ||
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.ADMIN) ||
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUB_ADMIN) ||
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPER_ADMIN)) {
                sequence.addSequenceItem(
                        new MaterialShowcaseView.Builder(this)
                                .setSkipText(getResources().getString(R.string.tour_skip))
                                .setTarget(findViewById(R.id.near_me))
                                .setTitleText(getResources().getString(R.string.tour_nearest_farm_title))
                                .setDismissText(getResources().getString(R.string.got_it))
                                .setContentText(getResources().getString(R.string.tour_nearest_farm_content))
                                .build());
            }
            if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR) ||
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.CLUSTER_ADMIN)) {
                sequence.addSequenceItem(
                        new MaterialShowcaseView.Builder(this)
                                .setSkipText(getResources().getString(R.string.tour_skip))
                                .setTarget(findViewById(R.id.navigation_expense))
                                .setTitleText(getResources().getString(R.string.tour_expenses_title))
                                .setDismissText(getResources().getString(R.string.got_it))
                                .setContentText(getResources().getString(R.string.tour_expenses_content))
                                .build());

            }
        } catch (Exception e) {
        }

        sequence.start();
        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.APPTOUR, false);
    }

    private void showFingerPrintDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_enable_fingerprint_login);
        Window window1 = dialog.getWindow();
        window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView titleTv = dialog.findViewById(R.id.titleTv);
        CheckBox checkBoxDontAsk = dialog.findViewById(R.id.checkBoxDontAsk);
        TextView messageTv = dialog.findViewById(R.id.messageTv);
        TextView okTv = dialog.findViewById(R.id.okTv);
        Switch loginSwitch = dialog.findViewById(R.id.loginSwitch);
        Switch lockSwitch = dialog.findViewById(R.id.lockSwitch);
        TextView cancelTv = dialog.findViewById(R.id.cancelTv);
        if (SharedPreferencesMethod2.getBoolean(context, SharedPreferencesMethod2.IS_ALLOW_FINGER)) {
            loginSwitch.setChecked(true);
        } else {
            loginSwitch.setChecked(false);
        }
        if (SharedPreferencesMethod2.getBoolean(context, SharedPreferencesMethod2.IS_APP_LOCK)) {
            lockSwitch.setChecked(true);
        } else {
            lockSwitch.setChecked(false);
        }
        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginSwitch.isChecked()) {
                    SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_FINGER_SELECTED, true);
                    SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_ALLOW_FINGER, true);
                } else {
                    SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_ALLOW_FINGER, false);
                    SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_FINGER_SELECTED, false);
                }
                if (lockSwitch.isChecked()) {
                    SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_APP_LOCK, true);
                } else {
                    SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_APP_LOCK, false);
                }
                /*if (checkBoxDontAsk.isChecked())
                    SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_FINGER_DONT_ASK, true);
                else
                    SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_FINGER_DONT_ASK, true);*/
                SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_FINGER_DONT_ASK, true);

                dialog.dismiss();
                dialog.dismiss();
            }
        });
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxDontAsk.isChecked())
                    SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_FINGER_DONT_ASK, true);
                else
                    SharedPreferencesMethod2.setBoolean(context, SharedPreferencesMethod2.IS_FINGER_DONT_ASK, true);
                dialog.dismiss();
            }
        });
        dialog.show();
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

//    are a farmer on top of add farm page then if yes then owner id willl be sv persoin id
    // connect agronomist


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

            } else {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    String selectedPreviousVetting = "";
    FetchFarmResultNew selectedPreviousFarm;
    int selectedPreviousIndex = 0;
    private final static int REQ_CODE_FILTER = 9940;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_FILTER) {
            try {
                if (resultCode != RESULT_OK) {
                    Log.e(TAG, "Update flow failed! Result code: " + resultCode);
                } else {
                    Log.e(TAG, "Update flow Result code: " + resultCode + " "
                            + data.getStringExtra("filter")
                            + " " + data.getStringExtra("query")
                            + " " + data.getStringExtra("vetting"));
                    geFilterData(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == UPDATE_REQ_CODE) {
            try {
                if (resultCode != RESULT_OK) {
                    Log.e(TAG, "Update flow failed! Result code: " + resultCode);
                } else {
                    InstallStateUpdatedListener listener = state -> {
                    };
                    appUpdateManager.registerListener(listener);
                    appUpdateManager.unregisterListener(listener);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 9930) {
            if (resultCode == RESULT_OK) {
                boolean val = data.getBooleanExtra("value", false);
                String val2 = data.getStringExtra("value1");
                Log.e(TAG, "9930TAG " + val2 + " " + val);

                if (val2 != null && !selectedPreviousVetting.equals(val2 + "")) {
                    try {

                        if (selectedPreviousFarm != null && selectedPreviousFarm.getVetting() != null &&
                                !selectedPreviousFarm.getVetting().equals(AppConstants.VETTING.SELECTED)) {
                            if (val) {
                                int i = fetchFarmResultVetting.indexOf(selectedPreviousFarm);
                                fetchFarmResultVettingFilter.get(selectedPreviousIndex).getFetchFarmResultNew().setVetting(val2 + "");
                                fetchFarmResultVetting.set(i, fetchFarmResultVettingFilter.get(selectedPreviousIndex));
                                farmDetailsAdapterVetting.notifyDataSetChanged();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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
                        findViewById(R.id.rel_landing_id),
                        resources.getString(R.string.update_has_been_just_dowloaded_msg),
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(resources.getString(R.string.restart_label), view -> appUpdateManager.completeUpdate());
        snackbar.setActionTextColor(
                getResources().getColor(R.color.white));
        snackbar.show();
    }

    public void displayCurrencyInfoForLocale(Locale locale) {
        Log.e(TAG, "Locale: " + locale.getDisplayName());
        Currency currency = Currency.getInstance(locale);
        Log.e(TAG, "Currency Code: " + currency.getCurrencyCode());
        Log.e(TAG, "Symbol: " + currency.getSymbol());
        Log.e(TAG, "Default Fraction Digits: " + currency.getDefaultFractionDigits());

    }

    public static void setCompName(String name) {
        compNameTv.setVisibility(View.VISIBLE);
        compNameTv.setText(name);
    }

    Map<String, List<LatLng>> coordinateHashMap = new HashMap<>();

    List<String> imagePathListToDelete = new ArrayList<>();

//    public static void printObjectSize(Object object) {
//        byte[] data = SerializationUtils.serialize((Serializable) object);
//        System.out.println("Object type: " + object.getClass() +
//                ", size: " + data.length + " bytes");
//    }

    List<FarmerAndFarmData> farmerListForUpdate = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        farmerListForUpdate.clear();
        DataHandler.newInstance().setLatLngsCheckArea(null);
        SharedPreferencesMethod.setString(context,SharedPreferencesMethod.FARMER_NAME, "");
        SharedPreferencesMethod.setString(context,SharedPreferencesMethod.KHASRA, "");
        Log.e(TAG, "appTourValue " + SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.APPTOUR));

//        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.APPTOUR)) {
        //getAppTour();
//        }
        if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            imagePathListToDelete.clear();
            FarmerCacheManager.getInstance(context).getAllFarmers(new FarmerCacheManager.GetAllFarmerListener() {
                @Override
                public void onFarmerLoaded(List<FarmerT> farmerT) {
                    farmerAndFarmDataListG.clear();
                    Log.e(TAG, "Farmer Fetch " + new Gson().toJson(farmerT));
                    for (int i = 0; i < farmerT.size(); i++) {
                        int finalI = i;
                        FarmerAndFarmData farmerAndFarmData = new Gson().fromJson(farmerT.get(finalI).getData(), FarmerAndFarmData.class);
                        farmerAndFarmData.setPersonId(farmerT.get(i).getFarmerId());
                        farmerAndFarmDataListG.add(farmerAndFarmData);
                    }
                }
            }, "Y");

            FarmerCacheManager.getInstance(context).getAllFarmers(new FarmerCacheManager.GetAllFarmerListener() {
                @Override
                public void onFarmerLoaded(List<FarmerT> farmerT) {
                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .create();
                    Log.e(TAG, "Farmer Fetch " + new Gson().toJson(farmerT));
                    for (int i = 0; i < farmerT.size(); i++) {
                        int finalI = i;
                        FarmerAndFarmData farmerAndFarmData = new Gson().fromJson(farmerT.get(finalI).getData(), FarmerAndFarmData.class);
                        farmerAndFarmData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                        FarmCacheManager.getInstance(context).getAllFarmsOfFarmer(new FarmCacheManager.FetchFarmerFarmListener() {
                            @Override
                            public void onFarmsLoaded(List<Farm> list) {
                                if (list != null && list.size() > 0)
                                    for (int i = 0; i < list.size(); i++) {
                                        Farm farm = list.get(i);
                                        List<FarmData> farmDataList = new ArrayList<>();
                                        FarmData farmData = null;
                                        try {
                                            farmData = gson.fromJson(farm.getFarmFullData(), FarmData.class);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        farmDataList.add(farmData);
                                        farmerAndFarmData.setFarmDataList(farmDataList);
                                        farmerListForUpdate.add(farmerAndFarmData);
                                    }

                            }
                        }, farmerAndFarmData.getPersonId(), "N");


                    }
                }
            }, "N", "Y");

            FarmCacheManager
                    .getInstance(context)
                    .getExistingFarmerFarm(
                            new FarmCacheManager.FetchFarmerFarmListener() {
                                @Override
                                public void onFarmsLoaded(List<Farm> farmListOffline) {
                                    farmerAndFarmDataListOnlyFarm.clear();
                                    for (int j = 0; j < farmListOffline.size(); j++) {
                                        FarmData farmData = null;
                                        if (AppConstants.isValidString(farmListOffline.get(j).getFarmFullData()) && !farmListOffline.get(j).getFarmFullData().trim().equals("{}"))
                                            try {
                                                farmData = new Gson().fromJson(farmListOffline.get(j).getFarmFullData(), FarmData.class);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        Log.e(TAG, "getExistingFarmerFarm " + new Gson().toJson(farmData));
                                        if (farmData == null) {
                                            farmData = new FarmData();
                                            try {
                                                if (farmListOffline.get(j).getAssets() != null && !TextUtils.isEmpty(farmListOffline.get(j).getAssets())) {
                                                    List<List<CropFormDatum>> assetsList = new ArrayList<>();
                                                    JSONArray array = new JSONArray(farmListOffline.get(j).getAssets());
                                                    for (int k = 0; k < array.length(); k++) {
                                                        JSONArray jsonArray = array.getJSONArray(k);
                                                        List<CropFormDatum> assets = new ArrayList<>();
                                                        if (jsonArray != null && jsonArray.length() > 0)
                                                            for (int l = 0; l < jsonArray.length(); l++) {
                                                                CropFormDatum cropFormDatum = new Gson().fromJson(new Gson().toJson(jsonArray.getJSONObject(l)), CropFormDatum.class);
                                                                assets.add(cropFormDatum);
                                                            }
                                                        if (assets != null && assets.size() > 0)
                                                            assetsList.add(assets);
                                                    }
                                                    farmData.setAssetsList(assetsList);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            farmData.setTransplant_date(farmListOffline.get(j).getTransplant_date());
                                            farmData.setIs_irrigated(farmListOffline.get(j).getIs_irrigated());
                                            farmData.setPlanting_method(farmListOffline.get(j).getPlanting_method());
                                            farmData.setCropping_pattern(farmListOffline.get(j).getCropping_pattern());
                                            farmData.setId(farmListOffline.get(j).getLotNo());
                                            farmData.setFarmIdOffline(farmListOffline.get(j).getFarmId());
                                            farmData.setOwnerId(farmListOffline.get(j).getFarmerId());
                                            farmData.setCluster(farmListOffline.get(j).getClusterId());
                                            farmData.setCompId(farmListOffline.get(j).getCompId());
                                            farmData.setSoiltype(farmListOffline.get(j).getSoilTypeId());
                                            farmData.setIrrigationsource(farmListOffline.get(j).getIrriSourceId());
                                            farmData.setIrrigationtype(farmListOffline.get(j).getIrriTypeId());
                                            farmData.setAddedBy(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                                            farmData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                                            farmData.setAddL1(farmListOffline.get(j).getAddL1());
                                            farmData.setAddL2(farmListOffline.get(j).getAddL2());
                                            farmData.setArea(farmListOffline.get(j).getExpArea());
                                            farmData.setCropId(farmListOffline.get(j).getCropId());
                                            farmData.setSeason(farmListOffline.get(j).getSeasonId());
                                            farmData.setName(farmListOffline.get(j).getFarmName());
                                            farmData.setCurrentCropName(farmListOffline.get(j).getCropName());
                                            farmData.setPreviouscrop(farmListOffline.get(j).getPreviousCrop());
                                            farmData.setVillageOrCity(farmListOffline.get(j).getVillageOrCity());
                                            farmData.setMandalOrTehsil(farmListOffline.get(j).getMandalOrTehsil());
                                            farmData.setDistrict(farmListOffline.get(j).getDistrict());
                                            farmData.setState(farmListOffline.get(j).getState());
                                            farmData.setCountry(farmListOffline.get(j).getCountry());
                                            farmData.setDistrictId(farmListOffline.get(j).getDistrict());
                                            farmData.setStateId(farmListOffline.get(j).getState());
                                            farmData.setCountryId(farmListOffline.get(j).getCountry());
                                            farmData.setIsOwned(farmListOffline.get(j).getIsOwned());
                                            farmData.setLat_cord(farmListOffline.get(j).getLatCord());
                                            farmData.setLong_cord(farmListOffline.get(j).getLongCord());
                                            farmData.setOwnerShipPath(farmListOffline.get(j).getOwnerShipProof());
                                            farmData.setFarmImagePath(farmListOffline.get(j).getFarmImage());
                                            farmData.setSvId(farmListOffline.get(j).getSv_id());
                                            farmData.setFarmId(farmListOffline.get(j).getFarmId());
//                                                           farmData.setIsUploaded(null);


                                            try {
                                                JSONArray array = new JSONArray(farmListOffline.get(j).getFpoData());
                                                List<FPOCrop> clusterList = new ArrayList<>();
                                                for (int k = 0; k < array.length(); k++) {
                                                    JSONObject object = array.getJSONObject(k);
                                                    FPOCrop cluster = new Gson().fromJson(object.toString(), FPOCrop.class);
                                                    clusterList.add(cluster);
                                                }
                                                farmData.setFpoCropList(clusterList);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else
//                                                           farmData.setIsUploaded(null);
                                            Log.e(TAG, "getExistingFarmerFarm at " + j + " " + new Gson().toJson(farmData));
                                        List<FarmData> farmData1 = new ArrayList<>();
                                        farmData1.add(farmData);

                                        FarmerAndFarmData farmerAndFarmData = new FarmerAndFarmData();
                                        farmerAndFarmData.setPersonId(farmListOffline.get(j).getFarmerId());
                                        farmerAndFarmData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                                        farmerAndFarmData.setFarmDataList(farmData1);
                                        farmerAndFarmData.setClusterId(farmListOffline.get(j).getClusterId());
                                        farmerAndFarmData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                                        farmerAndFarmData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                                        farmerAndFarmData.setSvId(farmListOffline.get(j).getSv_id());
                                        farmerAndFarmData.setSvName(farmListOffline.get(j).getSv_name());

                                        farmerAndFarmDataListOnlyFarm.add(farmerAndFarmData);
                                    }

                                }
                            },
                            "Y", "N");

//            printObjectSize(farmerAndFarmDataListG);
//            FarmCacheManager.getInstance(context).getAllFarms(this);
            ResourceCacheManager.getInstance(context).getAllResources(this);
            InputCostCacheManager.getInstance(context).getAllInputCost(this);
            VrCacheManager.getInstance(context).getAllVisits(this);
            HarvestCacheManager.getInstance(context).getAllHarvest(this);

            PldCacheManager.getInstance(context).getOfflineAddedPld(new PldCacheManager.PldFetchListener() {
                @Override
                public void onPldLoaded(List<OfflinePld> pldList) {
                    offlinePldList.clear();
                    offlinePldList = pldList;
                    Log.e(TAG, "Offline Pld " + new Gson().toJson(pldList));
                }
            });

            FusedLocationProviderClient fusedLocationProviderClient;
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    currentLocation = location;
                                }
                            }
                        });
            }
        }
        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_IMAGE) != null && !TextUtils.isEmpty(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_IMAGE))) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(this).load(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_IMAGE))
                    .apply(options).into(imageView);
        }

        if (role != null && (role.equals(AppConstants.ROLES.SUPERVISOR) || role.equals(AppConstants.ROLES.CLUSTER_ADMIN)))
            taskCountTv.setText("" + SharedPreferencesMethod.getInt(context, SharedPreferencesMethod.TASK_COUNT));
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    private void firebaseSubscription() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@io.reactivex.annotations.NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d(TAG, token);
                    }
                });
        /*FirebaseMessaging.getInstance().subscribeToTopic("sandeep_test")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@io.reactivex.annotations.NonNull Task<Void> task) {
                        String msg = "subscribed";
                        if (!task.isSuccessful()) {
                            msg = "msg_subscribe_failed";
                        }
                        Log.d(TAG, msg);
                    }
                });*/

        FirebaseMessaging.getInstance().subscribeToTopic("croptrails")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@io.reactivex.annotations.NonNull Task<Void> task) {
                        String msg = "subscribed";
                        if (!task.isSuccessful()) {
                            msg = "msg_subscribe_failed";
                        }
                        Log.d(TAG, msg);
                    }
                });
        FirebaseMessaging.getInstance().subscribeToTopic("user_" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@io.reactivex.annotations.NonNull Task<Void> task) {
                        String msg = "subscribed";
                        if (!task.isSuccessful()) {
                            msg = "msg_subscribe_failed";
                        }
                        Log.d(TAG, msg);
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("comp_" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@io.reactivex.annotations.NonNull Task<Void> task) {
                        String msg = "subscribed";
                        if (!task.isSuccessful()) {
                            msg = "msg_subscribe_failed";
                        }
                        Log.d(TAG, msg);
                    }
                });
    }

    public void setActionBarTitle(String title) {
        if (title != null && !TextUtils.isEmpty(title))
            getSupportActionBar().setTitle(title);
        else
            getSupportActionBar().setTitle(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMPANY_NAME));
    }

    @Override
    public void onGetAllCompRegData(List<CompRegModel> compRegModelList) {
        checkCompRegForSv(compRegModelList);
    }

    private void checkCompRegForSv(List<CompRegModel> compRegModelList) {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat output = new SimpleDateFormat(AppConstants.DATE_FORMAT_SERVER);
        final String curdate = output.format(date);
        if (compRegModelList != null && compRegModelList.size() > 0) {
            Log.e(TAG, "comp reg list available");


            if (!compRegModelList.get(0).getDate().equals(curdate)) {
                getCopRegData(curdate);
            }
        } else {
            Log.e(TAG, "no comp reg list available");
//            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.AREA_UNIT_LABEL, "acres");
//            SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.AREA_MULTIPLICATON_FACTOR, 1.0);
            getCopRegData(curdate);
        }
    }

    private void getCopRegData(String date) {

        /*if (date==null){
            Date dateObj = Calendar.getInstance().getTime();
            SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
            date = output.format(dateObj);
        }*/
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
                    if (response.body().getStatus() == 10) {
                        if (isActivityRunning) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(LandingActivity.this, response.body().getMsg());
                        }
                    } else {
                        ResponseCompReg responseCompReg = response.body();
                        List<CompRegResult> compRegResultList = responseCompReg.getResult();
                        // compRegCacheManager.deleteAllData(SplashActivity.this);
                        if (compRegResultList != null && compRegResultList.size() > 0) {
                            compRegCacheManager.deleteAllData();
                            String curdate = AppConstants.getCurrentDateTime();
                            for (int i = 0; i < compRegResultList.size(); i++) {
                                String parameter = compRegResultList.get(i).getFeatureName();
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
                    }
                } else {
                    Log.e(TAG, "COMPREG rError" + response.errorBody().toString());

                }

            }

            @Override
            public void onFailure(Call<ResponseCompReg> call, Throwable t) {
                Log.e("RFerror", t.toString());
                AppConstants.setDefaultUnit(context);
                Intent intent = new Intent(context, LandingActivity.class);
                ActivityOptions options =
                        null;
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
        });

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

    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(context, CheckAreaMapActivity.class);
//            Intent intent = new Intent(context, CreateCropCycleActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }

        } else if (id == R.id.nav_crop_cycle) {
//            Intent intent = new Intent(context, CheckAreaMapActivity.class);
            Intent intent = new Intent(context, CropCycleListActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_pro) {
//            Intent intent = new Intent(context, CheckAreaMapActivity.class);
            Intent intent = new Intent(context, ProActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_task) {
            Intent intent = new Intent(context, SvTaskActivityActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }

        } else if (id == R.id.nav_sv_expense) {
            Intent intent = new Intent(context, ExpenseSvActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        } else if (id == R.id.nav_online) {
            new AlertDialog.Builder(this)
                    .setMessage(resources.getString(R.string.go_online_upload_offline_data))
                    .setCancelable(false)
                    .setPositiveButton(resources.getString(R.string.profile_alert_dialog_yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                                connected = true;
                            } else {
                                connected = false;
                            }

                            if (connected) {
                                checkNetworkType();
                                if (network_type.equals("2G") || network_type.equals("")) {
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showDialog(LandingActivity.this, resources.getString(R.string.opps_message), resources.getString(R.string.connect_to_3g_or_4g));
                                } else if (network_type.equals("3G") || network_type.equals("4G") || network_type.equals("WIFI")) {

//                                    uploadFarm2();
//                                    uploadFarms();
                                    new FarmerAndFarmDataAsync().execute();
                                    //uploadHarvest();
                                    //uploadResources();
                                }
                            } else {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showDialog(LandingActivity.this, resources.getString(R.string.dialog_heading_unable_to_connect), getString(R.string.internet_not_connected));
                                //Toast.makeText(context, getString(R.string.internet_not_connected), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton(resources.getString(R.string.profile_alert_dialog_no), null)
                    .show();
        } else if (id == R.id.nav_offline) {
            Intent intent = new Intent(context, OfflineModeActivity.class);
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
        } else if (id == R.id.nav_add_farm) {
            Intent intent = new Intent(context, DashboardActivity.class);
            intent.putExtra(AppConstants.ADD_FARM.KEY, AppConstants.ADD_FARM.NEW_FARM_AND_FARMER);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        } else if (id == R.id.soil_sense) {
            Intent intent = new Intent(context, BluetoothCommunicationActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        } /*else if (id == R.id.nav_vetting) {
            Intent intent = new Intent(context, VettingOthersFarmActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        }*/ else if (id == R.id.nav_harvest_paln) {
            Intent intent = new Intent(context, HarvestPlanActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }

        } else if (id == R.id.nav_farmers) {
            Intent intent = new Intent(context, RejectedVettingActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(context, SettingActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        } else if (id == R.id.nav_show_planned_harvest) {
            Intent intent = new Intent(context, HarvestShowPlanActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        } else if (id == R.id.nav_noti) {
            Intent intent = new Intent(context, NotificationActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        } else if (id == R.id.crop_trac) {
            Intent intent = new Intent(context, ScanQRActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        } else if (id == R.id.contactUs) {
            Intent intent = new Intent(context, ContactActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        } /*else if (id == R.id.queryAdd) {
            Intent intent = new Intent(context, QueryActivity.class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        }*/ else if (id == R.id.signOut) {
            new AlertDialog.Builder(this)
                    .setMessage(resources.getString(R.string.signout_alert_dialog_message))
                    .setCancelable(false)
                    .setPositiveButton((resources.getString(R.string.signout_alert_dialog_yes)), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            GpsCacheManager.getInstance(context).getContacts(new GetSetInterface() {
                                @Override
                                public void onContactsLoaded(List<GpsLog> gpsLogList) {
                                    userLogout(gpsLogList);
                                }

                                @Override
                                public void onContactsAdded() {

                                }

                                @Override
                                public void onDataNotAvailable() {
                                    userLogout(null);
                                }
                            });

                        }
                    })
                    .setNegativeButton(getString(R.string.signout_alert_dialog_no), null)
                    .show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    String[] lati_cord, longi_cord, enter_date, sv_id, time;

    private void userLogout(List<GpsLog> gpsLogList) {
        progressBar.setVisibility(View.VISIBLE);
//        GpsLog log=new GpsLog("67.22387","22.2144",
//                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID),"2020-11-21","21:21:21");
//        log.setEnter_date("2020-11-21");
//        log.setTime("21:21:21");
//        gpsLogList.add(log);
        if (gpsLogList != null && gpsLogList.size() > 0) {
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
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            SendGpsArray sendGpsArray = new SendGpsArray();
            //sendGpsArray.setJsonObject(contactsObj);
            sendGpsArray.setLat_cord(lati_cord);
            sendGpsArray.setLong_cord(longi_cord);
            sendGpsArray.setEnter_date(enter_date);
            sendGpsArray.setSv_id(sv_id);
            sendGpsArray.setTime(time);
            sendGpsArray.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            sendGpsArray.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            Log.e(TAG, "GPS DATA " + new Gson().toJson(sendGpsArray));
            Call<RecieveResponseGpsBack> statusMsgModelCall = apiService.getStatusMsgforUploadGpsInBack(sendGpsArray);
            statusMsgModelCall.enqueue(new Callback<RecieveResponseGpsBack>() {
                @Override
                public void onResponse(Call<RecieveResponseGpsBack> call, Response<RecieveResponseGpsBack> response) {
                    if (response.isSuccessful()) {

                        if (response != null) {
                            RecieveResponseGpsBack statusMsgModel = response.body();
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.multiple_login_msg));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                            } else if (statusMsgModel.getStatus() != 0) {
                                Log.e(TAG, "Successfull" + statusMsgModel.getMsg());
                                Log.e(TAG, "Successfull" + statusMsgModel.getResult() + "  entries");
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("croptrails");
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("comp_" + SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
             /*                   AddFarmCCacheManager.getInstance(context).deleteAllData();
                                GpsCacheManager.getInstance(context).deleteAllGps(null);
                                SharedPreferencesMethod.clear(LandingActivity.this);
                                CompRegCacheManager.getInstance(LandingActivity.this).deleteAllData();
                                FarmCacheManager.getInstance(context).deleteAllFarms();
                                TimelineCacheManager.getInstance(context).deleteAllTimeline();
                                HarvestCacheManager.getInstance(context).deleteAllHarvests();
                                VrCacheManager.getInstance(context).deleteAllVisits();
                                VisitCacheManager.getInstance(context).deleteAllVisitStructure();
//                                DropDownCacheManager.getInstance(context).deleteAllChemicalUnit();

            */
                                try {
                                    AppDatabase.eraseDB(context, null);
                                } catch (Exception e) {
                                }
                                progressBar.setVisibility(View.GONE);
                                SharedPreferencesMethod.setBoolean(LandingActivity.this, SharedPreferencesMethod.LOGIN, false);
                                Intent intent = new Intent(LandingActivity.this, LoginActivity
                                        .class);
                                ActivityOptions options = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    options = ActivityOptions.makeCustomAnimation(LandingActivity.this, R.anim.fade_in, R.anim.fade_out);
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
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toasty.error(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toasty.error(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Toasty.error(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Not Successfull" + response.errorBody().string().toString() + "   Status" + response.code() + "  Message" + response.message());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RecieveResponseGpsBack> call, Throwable t) {
                    Log.e(TAG, "On Failure" + t.toString());
                    progressBar.setVisibility(View.GONE);
                    Toasty.error(context, resources.getString(R.string.something_went_wrong_msg) + ", " + resources.getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();

                }
            });

        } else {
//            progressDialog.cancel();
            progressBar.setVisibility(View.GONE);
//            GpsCacheManager.getInstance(context).deleteAllGps(null);
//            SharedPreferencesMethod.clear(LandingActivity.this);
//            CompRegCacheManager.getInstance(LandingActivity.this).deleteAllData();
//            FarmCacheManager.getInstance(context).deleteAllFarms();
//            TimelineCacheManager.getInstance(context).deleteAllTimeline();
//            HarvestCacheManager.getInstance(context).deleteAllHarvests();
//            VrCacheManager.getInstance(context).deleteAllVisits();
//            VisitCacheManager.getInstance(context).deleteAllVisitStructure();
//            progressDialog.cancel();
            try {
                AppDatabase.eraseDB(context, null);
            } catch (Exception e) {
            }
            SharedPreferencesMethod.setBoolean(LandingActivity.this, SharedPreferencesMethod.LOGIN, false);
            Intent intent = new Intent(LandingActivity.this, LoginActivity
                    .class);
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                options = ActivityOptions.makeCustomAnimation(LandingActivity.this, R.anim.fade_in, R.anim.fade_out);
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
            Log.e(TAG, "No Gps to upload");
        }
    }

    public void checkNetworkType() {
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = conManager.getAllNetworkInfo();
        for (NetworkInfo netInfo : info) {
            //Toast.makeText(context, ""+netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
            if (netInfo.getTypeName().equalsIgnoreCase("WIFI")) {
                if (netInfo.isConnected()) {
                    Log.e("Connectivity type wifi ", netInfo.getSubtype() + "");
                    network_type = "WIFI";
                }
            }
            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (netInfo.isConnected()) {
                    if (netInfo.getSubtype() == 7 || netInfo.getSubtype() == 4 || netInfo.getSubtype() == 2 || netInfo.getSubtype() == 1 || netInfo.getSubtype() == 11) {
                        Log.e("Connectivity", "2G Network");
                        network_type = "2G";

                    } else if (netInfo.getSubtype() == 3 || netInfo.getSubtype() == 5 || netInfo.getSubtype() == 6 || netInfo.getSubtype() == 8 || netInfo.getSubtype() == 9 || netInfo.getSubtype() == 10 || netInfo.getSubtype() == 12 || netInfo.getSubtype() == 14 || netInfo.getSubtype() == 15) {
                        Log.e("Connectivity", "3G Network");
                        network_type = "3G";
                    } else if (netInfo.getSubtype() == 13) {
                        Log.e("Connectivity", "4G Network");
                        network_type = "4G";
                    }
                    Log.e("Connection network_type", netInfo.getSubtype() + "");
                } else {
                    network_type = "";
                }
            }


        }

//   public static final int NETWORK_TYPE_1xRTT
//   Since: API Level 4
//   Current network is 1xRTT
//   Constant Value: 7 (0x00000007)
//
//   public static final int NETWORK_TYPE_CDMA
//   Since: API Level 4
//   Current network is CDMA: Either IS95A or IS95B
//   Constant Value: 4 (0x00000004)
//
//   public static final int NETWORK_TYPE_EDGE
//   Since: API Level 1
//   Current network is EDGE
//   Constant Value: 2 (0x00000002)
//
//   public static final int NETWORK_TYPE_EHRPD
//   Since: API Level 11
//   Current network is eHRPD
//   Constant Value: 14 (0x0000000e)
//
//   public static final int NETWORK_TYPE_EVDO_0
//   Since: API Level 4
//   Current network is EVDO revision 0
//   Constant Value: 5 (0x00000005)
//
//   public static final int NETWORK_TYPE_EVDO_A
//   Since: API Level 4
//   Current network is EVDO revision A
//   Constant Value: 6 (0x00000006)
//
//   public static final int NETWORK_TYPE_EVDO_B
//   Since: API Level 9
//   Current network is EVDO revision B
//   Constant Value: 12 (0x0000000c)
//
//   public static final int NETWORK_TYPE_GPRS
//   Since: API Level 1
//   Current network is GPRS
//   Constant Value: 1 (0x00000001)
//
//   public static final int NETWORK_TYPE_HSDPA
//   Since: API Level 5
//   Current network is HSDPA
//   Constant Value: 8 (0x00000008)
//
//   public static final int NETWORK_TYPE_HSPA
//   Since: API Level 5
//   Current network is HSPA
//   Constant Value: 10 (0x0000000a)
//
//   public static final int NETWORK_TYPE_HSPAP
//   Since: API Level 13
//   Current network is HSPA+
//   Constant Value: 15 (0x0000000f)
//
//   public static final int NETWORK_TYPE_HSUPA
//   Since: API Level 5
//   Current network is HSUPA
//   Constant Value: 9 (0x00000009)
//
//   public static final int NETWORK_TYPE_IDEN
//   Since: API Level 8
//   Current network is iDen
//   Constant Value: 11 (0x0000000b)
//
//   public static final int NETWORK_TYPE_LTE
//   Since: API Level 11
//   Current network is LTE
//   Constant Value: 13 (0x0000000d)
//
//   public static final int NETWORK_TYPE_UMTS
//   Since: API Level 1
//   Current network is UMTS
//   Constant Value: 3 (0x00000003)
//
//   public static final int NETWORK_TYPE_UNKNOWN
//   Since: API Level 1
//   Network type is unknown
//   Constant Value: 0 (0x00000000)

        //return wifiDataAvailable || mobileDataAvailable;

        // Other list of various subtypes you can check for and their bandwidth limits
        // TelephonyManager.NETWORK_TYPE_1xRTT       ~ 50-100 kbps
        // TelephonyManager.NETWORK_TYPE_CDMA        ~ 14-64 kbps
        // TelephonyManager.NETWORK_TYPE_HSDPA       ~ 2-14 Mbps
        // TelephonyManager.NETWORK_TYPE_HSPA        ~ 700-1700 kbps
        // TelephonyManager.NETWORK_TYPE_HSUPA       ~ 1-23 Mbps
        // TelephonyManager.NETWORK_TYPE_UMTS        ~ 400-7000 kbps
        // TelephonyManager.NETWORK_TYPE_UNKNOWN     ~ Unknown


    }


    private void loadCompRegData() {
        compRegCacheManager.getCompRegData(this);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    @Override
    public void onFarmAdded() {

    }

    @Override
    public void onFarmInsertError(Throwable e) {

    }

    @Override
    public void onNoFarmsAvailable() {

    }

    @Override
    public void farmDeleted() {

    }

    @Override
    public void farmDeletionFaild() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exit) {
                finish(); // finish activity
            } else {
                Toast.makeText(context, "Press back again to exit",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }
        }
    }


    public void deleteImage(String path) {
        try {
            String file_dj_path = path;// Environment.getExternalStorageDirectory() + "/ECP_Screenshots/abc.jpg";
            File fdelete = new File(file_dj_path);
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    Log.e(TAG, "file Deleted :" + file_dj_path);
                    callBroadCast();
                } else {
                    Log.e(TAG, "file not Deleted :" + file_dj_path);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e(TAG, " >= 14");
            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.e(TAG, "ExternalStorage Scanned " + path + ":");
                    Log.e(TAG, "ExternalStorage -> uri=" + uri);
                }
            });
        } else {
            Log.e(TAG, " < 14");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }


    //All Offline Data fetch overridden methods


    @Override
    public void onVisitAdded() {

    }

    @Override
    public void onVisitInsertError(Throwable e) {

    }

    @Override
    public void onNoVisitAvailable() {

    }

    @Override
    public void visitDeleted() {

    }

    @Override
    public void visitDeletionFailed() {

    }

    @Override
    public void onVisitLoaded(List<VisitTable> visitTableList) {
        /*for (int i = 0; i < harvestTList.size(); i++) {
            if (harvestTList.get(i).getOfflineHarvest()!=null&&!harvestTList.get(i).getOfflineHarvest().equals("{}")) {

            }

        }*/
    }

    //-----------------------------------------------------------------------------


    List<Farm> farmListT = new ArrayList<>();
    List<Farm> farmListToUpload = new ArrayList<>();

/*
    List<Farm> farmListTOffline = new ArrayList<>();
    List<Farm> farmListToUploadOffline = new ArrayList<>();*/


    private void changeFarmIds(String where, String farmId, String ownerId, String whereOwner, boolean flag) {

        VrCacheManager.getInstance(context).update(where, farmId);
//        FarmCacheManager.getInstance(context).updateFarm(where, farmId, "N", ownerId);
        ActivityCacheManager.getInstance(context).update(where, farmId);
        HarvestCacheManager.getInstance(context).updateHarvestData(where, farmId);
        InputCostCacheManager.getInstance(context).update(where, farmId);
        ResourceCacheManager.getInstance(context).update(where, farmId);
        TimelineCacheManager.getInstance(context).update(where, farmId);


        for (int i = 0; i < resourceCostListT.size(); i++) {
            if (resourceCostListT.get(i).getFarmId().trim().equals(where.trim()))
                resourceCostListT.get(i).setFarmId(farmId);
        }


        /*for (int i = 0; i < farmListOffline.size(); i++) {
            if (farmListOffline.get(i).getFarmId().trim().equals(where.trim()))
                farmListOffline.get(i).setIsOfflineAdded("N");
        }*/
        for (int i = 0; i < inputCostListT.size(); i++) {
            if (inputCostListT.get(i).getFarmId().trim().equals(where.trim()))
                inputCostListT.get(i).setFarmId(farmId);
        }

        for (int i = 0; i < harvestListT.size(); i++) {
            if (harvestListT.get(i).getFarmId().trim().equals(where.trim()))
                harvestListT.get(i).setFarmId(farmId);
        }

        for (int i = 0; i < visitListT.size(); i++) {
            if (visitListT.get(i).getFarmId().trim().equals(where.trim()))
                visitListT.get(i).setFarmId(farmId);
        }

        for (int i = 0; i < activityListT.size(); i++) {
            if (activityListT.get(i).getFarmId().trim().equals(where.trim()))
                activityListT.get(i).setFarmId(farmId);
        }

        for (int i = 0; i < resourceDataList.size(); i++) {
            for (int j = 0; j < resourceDataList.get(i).size(); j++) {
                if (resourceDataList.get(i).get(j).getFarmId().trim().equals(where.trim())) {
                    resourceDataList.get(i).get(j).setFarmId(farmId);
                    resourceDataList.get(i).get(j).setFarmMasterNum(farmId);

                }
            }
        }
        for (int i = 0; i < inputCostDataList.size(); i++) {
            for (int j = 0; j < inputCostDataList.get(i).size(); j++) {
                if (inputCostDataList.get(i).get(j).getFarmId().trim().equals(where.trim())) {
                    inputCostDataList.get(i).get(j).setFarmId(farmId);
                    inputCostDataList.get(i).get(j).setFarmMasterNum(farmId);
                }
            }
        }
        for (int i = 0; i < harvestDetailsList.size(); i++) {
            if (harvestDetailsList.get(i).getFarmId().trim().equals(where.trim()))
                harvestDetailsList.get(i).setFarmId(farmId);
        }
        for (int i = 0; i < doneActivityResponseList.size(); i++) {
            if (doneActivityResponseList.get(i).getFarmId().trim().equals(where.trim()))
                doneActivityResponseList.get(i).setFarmId(farmId);
        }

        for (int i = 0; i < visitResponseList.size(); i++) {
            if (visitResponseList.get(i).getFarmId().trim().equals(where.trim()))
                visitResponseList.get(i).setFarmId(farmId);
        }
        //Need to put this line inside async
        if (flag) {
            for (int i = 0; i < farmerAndFarmDataListG.size(); i++) {
                if (farmerAndFarmDataListG.get(i).getPersonId().trim().equals(whereOwner.trim())) {
                    farmerAndFarmDataListG.get(i).setIsUploaded("Y");

                    for (int j = 0; j < farmerAndFarmDataListG.get(i).getFarmDataList().size(); j++) {
                        if (farmerAndFarmDataListG.get(i).getFarmDataList().get(j).getFarmIdOffline().equals(where)) {
                            farmerAndFarmDataListG.get(i).getFarmDataList().get(j).setIsUploaded("Y");
                            FarmCacheManager.getInstance(context)
                                    .updateFarm(farmerAndFarmDataListG.get(i).getFarmDataList().get(j).getFarmIdOffline(),
                                            farmId, "Y", ownerId);
                        }
                    }

                }
            }
            FarmerCacheManager.getInstance(context).updateFarm(whereOwner, ownerId, "Y");
        } else {

            for (int i = 0; i < farmerAndFarmDataListOnlyFarm.size(); i++) {
                if (farmerAndFarmDataListOnlyFarm.get(i).getPersonId().trim().equals(whereOwner.trim())) {
                    farmerAndFarmDataListOnlyFarm.get(i).setIsUploaded("Y");

                    for (int j = 0; j < farmerAndFarmDataListOnlyFarm.get(i).getFarmDataList().size(); j++) {
                        if (farmerAndFarmDataListOnlyFarm.get(i).getFarmDataList().get(j).getFarmIdOffline().equals(where)) {
                            farmerAndFarmDataListOnlyFarm.get(i).getFarmDataList().get(j).setIsUploaded("Y");
                            farmerAndFarmDataOnlyFarm.get(i).getFarmDataList().get(j).setIsUploaded("Y");
                            FarmCacheManager.getInstance(context).updateFarmS(where, farmId, "Y");
                        }
                    }

                }
            }
        }

    }

    List<ResourceCostT> resourceCostListT = new ArrayList<>();
    List<InputCostT> inputCostListT = new ArrayList<>();
    List<HarvestT> harvestListT = new ArrayList<>();
    List<Visit> visitListT = new ArrayList<>();
    List<DoneActivity> activityListT = new ArrayList<>();
    List<List<ResourceData>> resourceDataList = new ArrayList<>();
    List<List<InputCostData>> inputCostDataList = new ArrayList<>();
    List<ViewHarvestDetails> harvestDetailsList = new ArrayList<>();
    List<DoneActivityResponseNew> doneActivityResponseList = new ArrayList<>();
    List<VisitResponseNew> visitResponseList = new ArrayList<>();

    @Override
    public void onFarmLoaded(List<Farm> farmList) {
        farmListToUpload.clear();
        farmListT.clear();
        coordinateHashMap.clear();

        for (int i = 0; i < farmList.size(); i++) {

            String cords = farmList.get(i).getCoords();
            JSONArray arrayOffline = null;
            try {
                List<LatLng> latLngs = new ArrayList<>();

                arrayOffline = new JSONArray(cords);
                Log.e(TAG, "Res 1 " + arrayOffline.toString());
                for (int j = 0; j < arrayOffline.length(); j++) {

                    JSONObject object = arrayOffline.getJSONObject(j);
                    LatLngFarm data = new Gson().fromJson(object.toString(), LatLngFarm.class);
                    LatLng latLng = new LatLng(Double.valueOf(data.getLng()), Double.valueOf(data.getLng()));
                    latLngs.add(latLng);
                }
                coordinateHashMap.put(farmList.get(i).getFarmId(), latLngs);


            } catch (JSONException e) {

            } catch (Exception e) {

            }

            if (farmList.get(i).getIsUpdated().equals("Y") && farmList.get(i).getIsUploaded().trim().toUpperCase().equals("N") /*&&
                    farmList.get(i).getIsOfflineAdded().trim().toUpperCase().equals("N")*/) {
                farmListT.add(farmList.get(i));
//                farmListToUpload.add(farmList.get(i));
                Log.e(TAG, "Farm " + i);
                Log.e(TAG, "Farm Offline Pld" + farmList.get(i).getOfflinePldJson());
            }
        }
    }

    @Override
    public void onAllResourceLoaded(List<ResourceCostT> resourceCostTList) {
        resourceCostListT.clear();
        resourceDataList.clear();
        for (int i = 0; i < resourceCostTList.size(); i++) {
            if (resourceCostTList.get(i).getIsUploaded().trim().toUpperCase().equals("N") && resourceCostTList.get(i).getResourceJsonOffline() != null && !resourceCostTList.get(i).getResourceJsonOffline().equals("[]")) {
                resourceCostListT.add(resourceCostTList.get(i));
                isResUploaded.add(false);
                List<ResourceData> resourceDataL = new ArrayList<>();
                JSONArray arrayOffline = null;
                try {
                    arrayOffline = new JSONArray(resourceCostTList.get(i).getResourceJsonOffline());
                    Log.e(TAG, "Res 1 " + arrayOffline.toString());
                    for (int j = 0; j < arrayOffline.length(); j++) {
                        Log.e(TAG, "Res " + j);
                        JSONObject object = arrayOffline.getJSONObject(j);
                        ResourceData data = new Gson().fromJson(object.toString(), ResourceData.class);
                        data.setFarmId(resourceCostTList.get(i).getFarmId());
                        resourceDataL.add(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(context, "Resource", Toast.LENGTH_SHORT).show();
                }
                resourceDataList.add(resourceDataL);
            }
        }
    }

    @Override
    public void onAllInputCostLoaded(List<InputCostT> inputCostTList) {
        inputCostListT.clear();
        inputCostDataList.clear();
        for (int i = 0; i < inputCostTList.size(); i++) {
            if (inputCostTList.get(i).getIsUploaded().trim().toUpperCase().equals("N") && inputCostTList.get(i).getInputCostJsonOfflineAdded() != null && !inputCostTList.get(i).getInputCostJsonOfflineAdded().equals("[]")) {
                inputCostListT.add(inputCostTList.get(i));
                isInputUploaded.add(false);
                JSONArray arrayOffline = null;
                List<InputCostData> inputCostDataL = new ArrayList<>();
                try {
                    arrayOffline = new JSONArray(inputCostTList.get(i).getInputCostJsonOfflineAdded());
                    for (int j = 0; j < arrayOffline.length(); j++) {
                        JSONObject object = arrayOffline.getJSONObject(j);
                        InputCostData data = new Gson().fromJson(object.toString(), InputCostData.class);
                        data.setFarmId(inputCostTList.get(i).getFarmId());
                        inputCostDataL.add(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                inputCostDataList.add(inputCostDataL);
            }
        }
    }

    @Override
    public void onAllHarvestLoaded(List<HarvestT> harvestTList) {
        Gson gson = new Gson();
        harvestListT.clear();
        harvestDetailsList.clear();
        for (int i = 0; i < harvestTList.size(); i++) {
            if (harvestTList.get(i).getIsUploaded().trim().toUpperCase().equals("N") && harvestTList.get(i).getOfflineHarvest() != null && !harvestTList.get(i).getOfflineHarvest().equals("{}")) {
                harvestListT.add(harvestTList.get(i));
                ViewHarvestDetails offlineDetails = gson.fromJson(harvestTList.get(i).getOfflineHarvest(), ViewHarvestDetails.class);
                offlineDetails.setFarmId(harvestTList.get(i).getFarmId());
                if (offlineDetails != null && offlineDetails.getData() != null && offlineDetails.getData1() != null)
                    harvestDetailsList.add(offlineDetails);
            }

        }
    }


    @Override
    public void onAllVisitLoaded(List<Visit> visitTableList) {
        visitListT.clear();
        visitResponseList.clear();
        for (int i = 0; i < visitTableList.size(); i++) {
            VisitResponseNew visitResponse = new Gson().fromJson(visitTableList.get(i).getVisitJson(), VisitResponseNew.class);
            if (visitTableList.get(i).isUploaded().trim().toUpperCase().equals("N") && visitTableList.get(i).getIsOfflineAdded().trim().toUpperCase().equals("Y")) {
                visitListT.add(visitTableList.get(i));

                visitResponse.setFarmId(visitTableList.get(i).getFarmId());
                visitResponse.setDoa(visitTableList.get(i).getDoa());
                visitResponse.setAddedBy(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                visitResponse.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                visitResponse.setApprovedMethod("images");
                visitResponseList.add(visitResponse);
            } else {
                if (visitResponse.getVisitImages() != null && visitResponse.getVisitImages().size() > 0) {
                    for (int j = 0; j < visitResponse.getVisitImages().size(); j++) {
                        if (visitResponse.getVisitImages().get(j).getImgLink() != null && !visitResponse.getVisitImages().get(j).getImgLink().isEmpty() && !visitResponse.getVisitImages().get(j).getImgLink().equals("0")) {
                            imagePathListToDelete.add(visitResponse.getVisitImages().get(j).getImgLink());
                        }
                    }
                }

            }
        }
    }

    @Override
    public void onAllActivityLoaded(List<DoneActivity> doneActivityList) {
        doneActivityResponseList.clear();
        activityListT.clear();
        for (int i = 0; i < doneActivityList.size(); i++) {
            DoneActivityResponseNew doneActivityResponse = new Gson().fromJson(doneActivityList.get(i).getActivityJson(), DoneActivityResponseNew.class);

            if (doneActivityList.get(i).getIsUploaded().trim().toUpperCase().equals("N") && doneActivityList.get(i).getIsOfflineAdded().trim().toUpperCase().equals("Y") && doneActivityList.get(i).getActivityJson() != null && !doneActivityList.get(i).getActivityJson().equals("{}")) {
                doneActivityResponse.setFarmCalActivityId(doneActivityList.get(i).getActivityId());
                doneActivityResponse.setFarmId(doneActivityList.get(i).getFarmId());
                doneActivityResponseList.add(doneActivityResponse);
                activityListT.add(doneActivityList.get(i));
            } else {
                if (doneActivityResponse.getDoneActivityImageList() != null && doneActivityResponse.getDoneActivityImageList().size() > 0) {
                    for (int j = 0; j < doneActivityResponse.getDoneActivityImageList().size(); j++) {
                        if (doneActivityResponse.getDoneActivityImageList().get(j).getImgLink() != null && !doneActivityResponse.getDoneActivityImageList().get(j).getImgLink().isEmpty() && !doneActivityResponse.getDoneActivityImageList().get(j).getImgLink().equals("0")) {
                            imagePathListToDelete.add(doneActivityResponse.getDoneActivityImageList().get(j).getImgLink());
                        }
                    }
                }
            }

        }
    }


    private void updateUploadStatus(Farm farm) {
        farm.setIsUploaded("Y");
        FarmCacheManager.getInstance(context).updateFarm(farm);
    }

    private void updateUploadStatus(ResourceCostT resourceCostT) {
        resourceCostT.setIsUploaded("Y");
        ResourceCacheManager.getInstance(context).update(resourceCostT);
    }

    private void updateUploadStatus(InputCostT inputCostT) {
        inputCostT.setIsUploaded("Y");
        InputCostCacheManager.getInstance(context).update(inputCostT);
    }

    private void updateUploadStatus(HarvestT harvestT) {
        harvestT.setIsUploaded("Y");
        HarvestCacheManager.getInstance(context).updateHarvestData(harvestT);
    }

    private void updateUploadStatus(Visit visit) {
        visit.setUploaded("Y");
        VrCacheManager.getInstance(context).update(visit);
    }

    private void updateUploadStatus(DoneActivity doneActivity) {
        doneActivity.setIsUploaded("Y");
        ActivityCacheManager.getInstance(context).update(doneActivity);
    }


    List<Boolean> isResUploaded = new ArrayList<>();
    List<Boolean> isInputUploaded = new ArrayList<>();

    private void uploadFarm2() {
        /*Log.e(TAG, "Uploadable Farm Data 1 " + new Gson().toJson(farmListToUpload));

        Log.e(TAG, "Uploadable Resource Data 1 " + new Gson().toJson(resourceDataList));
        Log.e(TAG, "Uploadable Resource Data 2 " + new Gson().toJson(resourceCostListT));

        Log.e(TAG, "Uploadable Input Data 1 " + new Gson().toJson(inputCostDataList));
        Log.e(TAG, "Uploadable Input Data 2 " + new Gson().toJson(inputCostListT));

        Log.e(TAG, "Uploadable Harvest Data 1 " + new Gson().toJson(harvestDetailsList));
        Log.e(TAG, "Uploadable Harvest Data 2 " + new Gson().toJson(harvestListT));

        Log.e(TAG, "Uploadable Activity Data 1 " + new Gson().toJson(doneActivityResponseList));
        Log.e(TAG, "Uploadable Activity Data 2 " + new Gson().toJson(activityListT));

        Log.e(TAG, "Uploadable Visit Data 1 " + new Gson().toJson(visitListT));
        Log.e(TAG, "Uploadable Visit Data 2 " + new Gson().toJson(visitResponseList));
*/
       /* Log.e(TAG, "Uploadable Farm Data 1 " + new Gson().toJson(farmListToUpload));
        Log.e(TAG, "Uploadable Farm Data 2 " + new Gson().toJson(farmListToUpload));*/
    }

    List<FarmerAndFarmData> farmerAndFarmDataListG = new ArrayList<>();
    List<FarmerAndFarmData> farmerAndFarmDataListOnlyFarm = new ArrayList<>();
    List<FarmerAndFarmData> farmerAndFarmData = new ArrayList<>();
    List<FarmerAndFarmData> farmerAndFarmDataOnlyFarm = new ArrayList<>();

    private class FarmerAndFarmDataAsync extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {


            farmerAndFarmData.clear();
            farmerAndFarmDataOnlyFarm.clear();

            for (int i = 0; i < farmerAndFarmDataListG.size(); i++) {
                List<FarmData> farmDataList = new ArrayList<>();
                if (farmerAndFarmDataListG.get(i).getIsUploaded() == null && farmerAndFarmDataListG.get(i).getFarmDataList() != null && farmerAndFarmDataListG.get(i).getFarmDataList().size() > 0) {

                    for (int j = 0; j < farmerAndFarmDataListG.get(i).getFarmDataList().size(); j++) {
                        if (farmerAndFarmDataListG.get(i).getFarmDataList().get(j).getIsUploaded() == null) {
                            farmDataList.add(farmerAndFarmDataListG.get(i).getFarmDataList().get(j));
                        }
                    }

                    if (farmDataList.size() > 0) {
                        FarmerAndFarmData d = farmerAndFarmDataListG.get(i);
                        d.setFarmDataList(farmDataList);
                        farmerAndFarmData.add(d);
                    }

                }
            }
            for (int i = 0; i < farmerAndFarmDataListOnlyFarm.size(); i++) {
                if (farmerAndFarmDataListOnlyFarm.get(i).getFarmDataList() != null &&
                        farmerAndFarmDataListOnlyFarm.get(i).getFarmDataList().size() > 0 &&
                        farmerAndFarmDataListOnlyFarm.get(i).getFarmDataList().get(0).getIsUploaded() == null) {

                    List<FarmData> farmData1 = new ArrayList<>();
                    farmData1.add(farmerAndFarmDataListOnlyFarm.get(i).getFarmDataList().get(0));
                    FarmerAndFarmData farmData = new FarmerAndFarmData();
                    farmData.setPersonId(farmerAndFarmDataListOnlyFarm.get(i).getPersonId());
                    farmData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                    farmData.setFarmDataList(farmData1);
                    farmData.setClusterId(farmerAndFarmDataListOnlyFarm.get(i).getFarmDataList().get(0).getCluster());
                    farmData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                    farmData.setAdded_by(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                    farmData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                    farmData.setSvName(farmerAndFarmDataListOnlyFarm.get(i).getSvName());
                    farmData.setSvId(farmerAndFarmDataListOnlyFarm.get(i).getSvId());
                    farmerAndFarmDataOnlyFarm.add(farmData);
                }
            }
            startUploading();
            return null;
        }
    }

    private class FarmerAsync extends AsyncTask<String, Integer, String> {
        FarmerAndFarmData farmerAndFarm;
        int index;

        public FarmerAsync(FarmerAndFarmData farmerAndFarm, int index) {
            this.farmerAndFarm = farmerAndFarm;
            this.index = index;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject farmData = new JSONObject();

                if (AppConstants.isValidString(farmerAndFarm.getSpouseDob())) {
                    farmData.put("spouse_dob", farmerAndFarm.getSpouseDob());
                }
                if (AppConstants.isValidString(farmerAndFarm.getDob())) {
                    farmData.put("dob", farmerAndFarm.getDob());
                } else if (AppConstants.isValidString(farmerAndFarm.getYob())) {
                    try {
                        farmData.put("yob", farmerAndFarm.getYob());
                    } catch (Exception e) {

                    }
                }

                String imgBase64 = null;
                if (AppConstants.isValidString(farmerAndFarm.getImage())) {
                    Bitmap bm = BitmapFactory.decodeFile(farmerAndFarm.getImage());
                    imgBase64 = compressBitmappp(bm, 2, 50);
                    farmData.put("img_link", imgBase64);
                }


                if (AppConstants.isValidString(farmerAndFarm.getLiteracy()))
                    farmData.put("literacy", farmerAndFarm.getLiteracy());
                if (AppConstants.isValidString(farmerAndFarm.getFinancial_status()))
                    farmData.put("financial_status", farmerAndFarm.getFinancial_status());


              /*  if (AppConstants.isValidString(imgBase64IDProof)) {
                    farmData.put("img_link", imgBase64AddressProf);
                }*/

                farmData.put("added_by", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                if (farmerAndFarm.getCasteCategoryId() != null && !farmerAndFarm.getCasteCategoryId().equals("0"))
                    farmData.put("caste_category", farmerAndFarm.getCasteCategoryId());
                if (farmerAndFarm.getCasteId() != null && !farmerAndFarm.getCasteId().equals("0"))
                    farmData.put("caste", farmerAndFarm.getCasteId());
                if (farmerAndFarm.getMobileNetworkId() != null && !farmerAndFarm.getMobileNetworkId().equals("0"))
                    farmData.put("mobile_operator", farmerAndFarm.getMobileNetworkId());
                if (farmerAndFarm.getReligionId() != null && !farmerAndFarm.getReligionId().equals("0"))
                    farmData.put("religion", farmerAndFarm.getReligionId());
                if (farmerAndFarm.getMiaId() != null && !farmerAndFarm.getMiaId().equals("0"))
                    farmData.put("mia", farmerAndFarm.getMiaId());

                if (farmerAndFarm.getDistrict() != null && !farmerAndFarm.getDistrict().equals("0"))
                    farmData.put("pdistrict", farmerAndFarm.getDistrict());
                if (farmerAndFarm.getState() != null && !farmerAndFarm.getState().equals("0"))
                    farmData.put("pstate", farmerAndFarm.getState());
                if (farmerAndFarm.getCountry() != null && !farmerAndFarm.getCountry().equals("0"))
                    farmData.put("pcountry", farmerAndFarm.getCountry());

                if (farmerAndFarm.getFamily_mem_count() != null && !farmerAndFarm.getFamily_mem_count().equals("0"))
                    farmData.put("family_mem_count", farmerAndFarm.getFamily_mem_count());
                if (farmerAndFarm.getAdults_count() != null && !farmerAndFarm.getAdults_count().equals("0"))
                    farmData.put("adults_count", farmerAndFarm.getAdults_count());
                if (farmerAndFarm.getKids_count() != null && !farmerAndFarm.getKids_count().equals("0"))
                    farmData.put("kids_count", farmerAndFarm.getKids_count());
                if (farmerAndFarm.getDependent_count() != null && !farmerAndFarm.getDependent_count().equals("0"))
                    farmData.put("dependent_count", farmerAndFarm.getDependent_count());
                if (farmerAndFarm.getIsFarmAndFarmer() != null && !farmerAndFarm.getIsFarmAndFarmer().equals("0"))
                    farmData.put("isFarmAndFarmer", farmerAndFarm.getIsFarmAndFarmer());
                if (farmerAndFarm.getUpi_id() != null && !farmerAndFarm.getUpi_id().equals("0"))
                    farmData.put("upi_id", farmerAndFarm.getUpi_id());
                if (farmerAndFarm.getCivil_status() != null && !farmerAndFarm.getCivil_status().equals("0"))
                    farmData.put("civil_status", farmerAndFarm.getCivil_status());
                if (farmerAndFarm.getSpouse_name() != null && !farmerAndFarm.getSpouse_name().equals("0"))
                    farmData.put("spouse_name", farmerAndFarm.getSpouse_name());
                if (farmerAndFarm.getBeneficiary_name() != null && !farmerAndFarm.getBeneficiary_name().equals("0"))
                    farmData.put("beneficiary_name", farmerAndFarm.getBeneficiary_name());
                if (farmerAndFarm.getImage() != null && !farmerAndFarm.getImage().equals("0"))
                    farmData.put("image", farmerAndFarm.getImage());
                if (farmerAndFarm.getIsUsingPhone() != null && !farmerAndFarm.getIsUsingPhone().equals("0"))
                    farmData.put("isUsingPhone", farmerAndFarm.getIsUsingPhone());
                if (farmerAndFarm.getIsSmartPhone() != null && !farmerAndFarm.getIsSmartPhone().equals("0"))
                    farmData.put("isSmartPhone", farmerAndFarm.getIsSmartPhone());
                if (farmerAndFarm.getIsShareHolder() != null && !farmerAndFarm.getIsShareHolder().equals("0"))
                    farmData.put("isShareHolder", farmerAndFarm.getIsShareHolder());
                if (farmerAndFarm.getSvId() != null && !farmerAndFarm.getSvId().equals("0"))
                    farmData.put("sv_id", farmerAndFarm.getSvId());
                if (farmerAndFarm.getSvName() != null && !farmerAndFarm.getSvName().equals("0"))
                    farmData.put("sv_name", farmerAndFarm.getSvName());
                JSONObject shareJson = null;
                try {
                    shareJson = new JSONObject(new Gson().toJson(farmerAndFarm.getShareJson()));
                } catch (Exception e) {

                }
                if (shareJson != null)
                    farmData.put("share_json", shareJson);
                if (farmerAndFarm.getName() != null && !farmerAndFarm.getName().equals("0"))
                    farmData.put("name", farmerAndFarm.getName());
                if (farmerAndFarm.getFather_name() != null && !farmerAndFarm.getFather_name().equals("0"))
                    farmData.put("father_name", farmerAndFarm.getFather_name());
                if (farmerAndFarm.getMob() != null && !farmerAndFarm.getMob().equals("0"))
                    farmData.put("mob", farmerAndFarm.getMob());
                if (farmerAndFarm.getMob2() != null && !farmerAndFarm.getMob2().equals("0"))
                    farmData.put("mob2", farmerAndFarm.getMob2());
                if (farmerAndFarm.getEmail() != null && !farmerAndFarm.getEmail().equals("0"))
                    farmData.put("email", farmerAndFarm.getEmail());
                if (farmerAndFarm.getAadhaar() != null && !farmerAndFarm.getAadhaar().equals("0"))
                    farmData.put("aadhar", farmerAndFarm.getAadhaar());
                if (farmerAndFarm.getPan() != null && !farmerAndFarm.getPan().equals("0"))
                    farmData.put("pan", farmerAndFarm.getPan());
                if (farmerAndFarm.getAddL1() != null && !farmerAndFarm.getAddL1().equals("0"))
                    farmData.put("pAddL1", farmerAndFarm.getAddL1());
                if (farmerAndFarm.getAddL2() != null && !farmerAndFarm.getAddL2().equals("0"))
                    farmData.put("pAddL2", farmerAndFarm.getAddL2());
                if (farmerAndFarm.getVillage_or_city() != null && !farmerAndFarm.getVillage_or_city().equals("0"))
                    farmData.put("pvillage_or_city", farmerAndFarm.getVillage_or_city());
                if (farmerAndFarm.getMandal_or_tehsil() != null && !farmerAndFarm.getMandal_or_tehsil().equals("0"))
                    farmData.put("pmandal_or_tehsil", farmerAndFarm.getMandal_or_tehsil());
                if (farmerAndFarm.getBankName() != null && !farmerAndFarm.getBankName().equals("0"))
                    farmData.put("bank_name", farmerAndFarm.getBankName());
                if (farmerAndFarm.getAccountNumber() != null && !farmerAndFarm.getAccountNumber().equals("0"))
                    farmData.put("account_no", farmerAndFarm.getAccountNumber());
                if (farmerAndFarm.getHolderName() != null && !farmerAndFarm.getHolderName().equals("0"))
                    farmData.put("account_name", farmerAndFarm.getHolderName());
                if (farmerAndFarm.getBranch() != null && !farmerAndFarm.getBranch().equals("0"))
                    farmData.put("branch", farmerAndFarm.getBranch());
                if (farmerAndFarm.getIfscCode() != null && !farmerAndFarm.getIfscCode().equals("0"))
                    farmData.put("ifsc", farmerAndFarm.getIfscCode());
                if (farmerAndFarm.getClusterId() != null && !farmerAndFarm.getClusterId().equals("0"))
                    farmData.put("cluster_id", farmerAndFarm.getClusterId());
                if (farmerAndFarm.getCompId() != null && !farmerAndFarm.getCompId().equals("0"))
                    farmData.put("comp_id", farmerAndFarm.getCompId());
                if (AppConstants.isValidString(farmerAndFarm.getPersonId()) && !farmerAndFarm.getPersonId().equals("0"))
                    farmData.put("owner_id", farmerAndFarm.getPersonId());

                farmData.put("user_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));

                farmData.put("token", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                if (farmerAndFarm.getCountryCode() != null && !farmerAndFarm.getCountryCode().equals("0"))
                    farmData.put("countryCode", farmerAndFarm.getCountryCode());
//                if (farmerAndFarm.getDob() != null && !farmerAndFarm.getDob().equals("0"))
//                    farmData.put("dob", farmerAndFarm.getDob());

                if (farmerAndFarm.getAnualIncome() != null && !farmerAndFarm.getAnualIncome().equals("0"))
                    farmData.put("est_income", farmerAndFarm.getAnualIncome());
                if (farmerAndFarm.getGeder() != null && !farmerAndFarm.getGeder().equals("0"))
                    farmData.put("gender", farmerAndFarm.getGeder());
                if (farmerAndFarm.getSwiftCode() != null && !farmerAndFarm.getSwiftCode().equals("0"))
                    farmData.put("swift_code", farmerAndFarm.getSwiftCode());

                JSONArray items = new JSONArray();

                for (int i = 0; i < farmerAndFarm.getFarmDataList().size(); i++) {
                    FarmData farmData1 = farmerAndFarm.getFarmDataList().get(i);
                    JSONObject farm = new JSONObject();
                    farm.put("farm_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                    if (AppConstants.isValidString(farmData1.getOwnerShipDoc()))
                        farm.put("ownership_doc", farmData1.getOwnerShipDoc());
                    if (AppConstants.isValidString(farmData1.getFarmImage()))
                        farm.put("farm_photo", farmData1.getFarmImage());
                    farm.put("added_by", farmData1.getAddedBy());
                    farm.put("comp_id", SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                    farm.put("cluster_id", farmData1.getCluster());
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
                    if (AppConstants.isValidString(farmData1.getOwnerId()) && !farmData1.getOwnerId().equals("0"))
                        farm.put("owner_id", farmData1.getOwnerId());
                    if (farmData1.getId() != null && !farmData1.getId().equals("0"))
                        farm.put("lot_no", farmData1.getId());
                    if (farmData1.getName() != null && !farmData1.getName().equals("0"))
                        farm.put("farm_name", farmData1.getName());
                    if (farmData1.getCropping_pattern() != null && !farmData1.getCropping_pattern().equals("0"))
                        farm.put("cropping_pattern", farmData1.getCropping_pattern());
                    if (farmData1.getIs_irrigated() != null && !farmData1.getIs_irrigated().equals("0"))
                        farm.put("is_irrigated", farmData1.getIs_irrigated());
                    if (farmData1.getArea() != null && !farmData1.getArea().equals("0"))
                        farm.put("exp_area", farmData1.getArea());

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

                    if (farmData1.getStandingArea() != null && !farmData1.getStandingArea().equals("0"))
                        farm.put("standing_area", farmData1.getStandingArea());

                    if (farmData1.getActualArea() != null && !farmData1.getActualArea().equals("0"))
                        farm.put("actual_area", farmData1.getActualArea());
                    if (farmData1.getGermination() != null && !farmData1.getGermination().equals("0"))
                        farm.put("germination", farmData1.getGermination());
                    if (farmData1.getPopulation() != null && !farmData1.getPopulation().equals("0"))
                        farm.put("population", farmData1.getPopulation());
                    if (farmData1.getSpacing_rtr() != null && !farmData1.getSpacing_rtr().equals("0"))
                        farm.put("spacing_rtr", farmData1.getSpacing_rtr());
                    if (farmData1.getSpacing_ptp() != null && !farmData1.getSpacing_ptp().equals("0"))
                        farm.put("spacing_ptp", farmData1.getSpacing_ptp());
                    if (farmData1.getPld_acres() != null && !farmData1.getPld_acres().equals("0"))
                        farm.put("pld_acres", farmData1.getPld_acres());

                    if (farmData1.getAreaHarvested() != 0)
                        farm.put("area_harvested", farmData1.getAreaHarvested());

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
//                jsonObject.addProperty("comp_id",SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));

                Log.e(TAG + "L", "Uploading1 Farmer " + new Gson().toJson(jsonObject));
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
                            Log.e(TAG, "Uploading1 Farmer response " + new Gson().toJson(response.body()));
                            if (response.body().getStatus() == 10) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.multiple_login_msg));
                                    }
                                });
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                                    }
                                });
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                                    }
                                });
                            } else if (response.body().getStatus() == 1) {
                                farmerListForUpdate.get(index).setIsUploaded("Y");
                                FarmerCacheManager.getInstance(context).updateFarm(farmerListForUpdate.get(index).getPersonId(),
                                        farmerListForUpdate.get(index).getPersonId(), "Y");

                                if (AppConstants.isValidString(farmerAndFarm.getAddressProof()) && farmerAndFarm.getAddressProof().length() > 200)
                                    uploadAddressIdImage(response.body().getPersonId() + "", farmerAndFarm.getAddressProof());
                                if (AppConstants.isValidString(farmerAndFarm.getIdProof()) && farmerAndFarm.getIdProof().length() > 200)
                                    uploadIDImage(response.body().getPersonId() + "", farmerAndFarm.getIdProof());

                                if (index == farmerListForUpdate.size() - 1) {
                                    if (farmerAndFarmData.size() > 0) {
                                        for (int i = 0; i < farmerAndFarmData.size(); i++) {
                                            uploadFarmAndFarmerData(farmerAndFarmData.get(i), i);
                                        }
                                    } else if (farmerAndFarmDataOnlyFarm.size() > 0) {

//                                        for (int i = 0; i < farmerAndFarmDataOnlyFarm.size(); i++)
                                            addExistingFarmerFarm();

                                    } else {
                                        uploadFarm2();
                                        uploadFarms();
                                    }
                                }
                            } else {
                                try {
                                    Toasty.error(context, resources.getString(R.string.failed_add_farm_and_farmer_msg), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                }
                            }

                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                                }
                            });
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                                }
                            });
                        } else {
                            try {

                                error = response.errorBody().string().toString();
                                Log.e(TAG, "Uploading1 Farmer error " + error);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                                notifyApiDelay(LandingActivity.this, response.raw().request().url().toString(),
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

                                    isLoaded[0] = true;
                                    long newMillis = AppConstants.getCurrentMills();
                                    long diff = newMillis - currMillis;
                                    notifyApiDelay(LandingActivity.this, call.request().url().toString(),
                                            "" + diff, internetSPeed, t.toString(), 0);
                                    Log.e(TAG, "Uploading1 Failure " + t.toString());
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


//                DropDownCacheManager.getInstance(context).deleteViaType("NEW_FARM_DATA_ADDITION");
//                DropDownT material = new DropDownT("NEW_FARM_DATA_ADDITION", new Gson().toJson(jsonObject));
//                DropDownCacheManager.getInstance(context).addChemicalUnit(material);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Exception Submit " + e.toString());
            }
            return null;
        }
    }

    private void uploadAddressIdImage(String personId, String image) {
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", "" + userId);
        jsonObject.addProperty("token", "" + token);
        jsonObject.addProperty("person_id", "" + personId);
        jsonObject.addProperty("img_link", "" + image);
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
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {

                Log.e("OfflineModeAct", "Failure getOfflineData " + t.toString());
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;

            }
        });


    }

    private void uploadIDImage(String personId, String image) {
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", "" + userId);
        jsonObject.addProperty("token", "" + token);
        jsonObject.addProperty("person_id", "" + personId);
        jsonObject.addProperty("img_link", "" + image);
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
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e(TAG, "Failure getOfflineData " + t.toString());
            }
        });
    }

    private void updateFarmer(FarmerAndFarmData farmData, int index) {

        FarmerAsync farmAndFarmerAsync = new FarmerAsync(farmData, index);
        farmAndFarmerAsync.execute();
    }

    private void startUploading() {
        if (farmerListForUpdate != null && farmerListForUpdate.size() > 0) {

            for (int i = 0; i < farmerListForUpdate.size(); i++) {
                updateFarmer(farmerListForUpdate.get(i), i);
            }
        } else if (farmerAndFarmData.size() > 0) {
            for (int i = 0; i < farmerAndFarmData.size(); i++) {
                uploadFarmAndFarmerData(farmerAndFarmData.get(i), i);
            }
        } else if (farmerAndFarmDataOnlyFarm.size() > 0) {
//            for (int i = 0; i < farmerAndFarmDataOnlyFarm.size(); i++)
                addExistingFarmerFarm();
        } else {
            uploadFarm2();
            uploadFarms();
        }
    }

    private class FarmAndFarmerAsync extends AsyncTask<String, Integer, String> {
        FarmerAndFarmData farmerAndFarmDataa;
        int index;

        public FarmAndFarmerAsync(FarmerAndFarmData farmerAndFarm, int index) {
            this.farmerAndFarmDataa = farmerAndFarm;
            this.index = index;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject farmData = new JSONObject();
                if (AppConstants.isValidString(farmerAndFarmDataa.getSpouseDob())) {
                    farmData.put("spouse_dob", farmerAndFarmDataa.getSpouseDob());
                }
                if (AppConstants.isValidString(farmerAndFarmDataa.getDob())) {
                    farmData.put("dob", farmerAndFarmDataa.getDob());
                } else if (AppConstants.isValidString(farmerAndFarmDataa.getYob())) {
                    try {
                        farmData.put("yob", farmerAndFarmDataa.getYob());
                    } catch (Exception e) {

                    }
                }

                String imgBase64 = null;
                if (AppConstants.isValidString(farmerAndFarmDataa.getImage())) {
                    Bitmap bm = BitmapFactory.decodeFile(farmerAndFarmDataa.getImage());
                    imgBase64 = compressBitmappp(bm, 2, 50);
                    farmData.put("img_link", imgBase64);
                }
                farmData.put("literacy", farmerAndFarmDataa.getLiteracy());
                farmData.put("financial_status", farmerAndFarmDataa.getFinancial_status());
                farmData.put("caste_category", farmerAndFarmDataa.getCasteCategoryId());
                farmData.put("caste", farmerAndFarmDataa.getCasteId());
                farmData.put("mobile_operator", farmerAndFarmDataa.getMobileNetworkId());
                farmData.put("religion", farmerAndFarmDataa.getReligionId());
                farmData.put("mia", farmerAndFarmDataa.getMiaId());

                farmData.put("pdistrict", farmerAndFarmDataa.getDistrict());
                farmData.put("pstate", farmerAndFarmDataa.getState());
                farmData.put("pcountry", farmerAndFarmDataa.getCountry());
                farmData.put("family_mem_count", farmerAndFarmDataa.getFamily_mem_count());
                farmData.put("adults_count", farmerAndFarmDataa.getAdults_count());
                farmData.put("kids_count", farmerAndFarmDataa.getKids_count());
                farmData.put("dependent_count", farmerAndFarmDataa.getDependent_count());
                farmData.put("isFarmAndFarmer", farmerAndFarmDataa.getIsFarmAndFarmer());
                farmData.put("upi_id", farmerAndFarmDataa.getUpi_id());
                farmData.put("civil_status", farmerAndFarmDataa.getCivil_status());
                farmData.put("spouse_name", farmerAndFarmDataa.getSpouse_name());
                farmData.put("beneficiary_name", farmerAndFarmDataa.getBeneficiary_name());
                farmData.put("image", farmerAndFarmDataa.getImage());
                farmData.put("isUsingPhone", farmerAndFarmDataa.getIsUsingPhone());
                farmData.put("isSmartPhone", farmerAndFarmDataa.getIsSmartPhone());
                farmData.put("isShareHolder", farmerAndFarmDataa.getIsShareHolder());
                farmData.put("sv_id", farmerAndFarmDataa.getSvId());
                farmData.put("sv_name", farmerAndFarmDataa.getSvName());

                JSONObject shareJson = null;
                try {
                    shareJson = new JSONObject(new Gson().toJson(farmerAndFarmDataa.getShareJson()));
                } catch (Exception e) {

                }
                if (shareJson != null)
                    farmData.put("share_json", shareJson);
                farmData.put("name", farmerAndFarmDataa.getName());
                farmData.put("father_name", farmerAndFarmDataa.getFather_name());
                farmData.put("mob", farmerAndFarmDataa.getMob());
                farmData.put("mob2", farmerAndFarmDataa.getMob2());
                farmData.put("email", farmerAndFarmDataa.getEmail());
                farmData.put("aadhar", farmerAndFarmDataa.getAadhaar());
                farmData.put("pan", farmerAndFarmDataa.getPan());
                farmData.put("pAddL1", farmerAndFarmDataa.getAddL1());
                farmData.put("pAddL2", farmerAndFarmDataa.getAddL2());
                farmData.put("pvillage_or_city", farmerAndFarmDataa.getVillage_or_city());
                farmData.put("pmandal_or_tehsil", farmerAndFarmDataa.getMandal_or_tehsil());
//                farmData.put("pdistrict", farmerAndFarmDataa.getDistrict());
//                farmData.put("pstate", farmerAndFarmDataa.getState());
//                farmData.put("pcountry", farmerAndFarmDataa.getCountry());
                farmData.put("bank_name", farmerAndFarmDataa.getBankName());
                farmData.put("account_no", farmerAndFarmDataa.getAccountNumber());
                farmData.put("account_name", farmerAndFarmDataa.getHolderName());
                farmData.put("branch", farmerAndFarmDataa.getBranch());
                farmData.put("ifsc", farmerAndFarmDataa.getIfscCode());
                farmData.put("cluster_id", farmerAndFarmDataa.getClusterId());
                farmData.put("comp_id", farmerAndFarmDataa.getCompId());
                farmData.put("added_by", farmerAndFarmDataa.getAdded_by());
                farmData.put("owner_id", farmerAndFarmDataa.getPersonId());
                farmData.put("user_id", farmerAndFarmDataa.getUserId());
                farmData.put("token", farmerAndFarmDataa.getToken());
                farmData.put("countryCode", farmerAndFarmDataa.getCountryCode());

                farmData.put("caste", farmerAndFarmDataa.getCast());
                farmData.put("est_income", farmerAndFarmDataa.getAnualIncome());
                farmData.put("gender", farmerAndFarmDataa.getGeder());
                farmData.put("swift_code", farmerAndFarmDataa.getSwiftCode());
                farmData.put("religion", farmerAndFarmDataa.getReligion());

                JSONArray items = new JSONArray();

                for (int i = 0; i < farmerAndFarmDataa.getFarmDataList().size(); i++) {
                    FarmData farmData1 = farmerAndFarmDataa.getFarmDataList().get(i);
//                    Log.e(TAG, "uploading1 FARM " + new Gson().toJson(farmData1));
                    JSONObject farm = new JSONObject();
                    if (AppConstants.isValidString(farmData1.getOwnerShipDoc()))
                        farm.put("ownership_doc", farmData1.getOwnerShipDoc());
                    if (AppConstants.isValidString(farmData1.getFarmImage()))
                        farm.put("farm_photo", farmData1.getFarmImage());
                    farm.put("farm_id_offline", farmData1.getFarmId());
                    farm.put("planting_method", farmData1.getPlanting_method());
                    farm.put("motor_hp", farmData1.getMotorHp());
                    farm.put("transplant_date", farmData1.getTransplant_date());
                    farm.put("fcountry", farmData1.getCountry());
                    farm.put("fstate", farmData1.getState());
                    farm.put("fdistrict", farmData1.getDistrict());
                    farm.put("sv_id", farmData1.getSvId());
                    farm.put("is_owned", farmData1.getIsOwned());
                    farm.put("owner_id", farmData1.getOwnerId());
                    farm.put("lot_no", farmData1.getId());
                    farm.put("farm_name", farmData1.getName());
                    farm.put("exp_area", farmData1.getArea());
                    if (farmData1.getCropping_pattern() != null && !farmData1.getCropping_pattern().equals("0"))
                        farm.put("cropping_pattern", farmData1.getCropping_pattern());
                    if (farmData1.getIs_irrigated() != null && !farmData1.getIs_irrigated().equals("0"))
                        farm.put("is_irrigated", farmData1.getIs_irrigated());
                    farm.put("season_num", farmData1.getSeason());
                    farm.put("cluster_id", farmerAndFarmDataa.getClusterId());
                    farm.put("previous_crop", farmData1.getPreviouscrop());
                    farm.put("irrigation_source_id", farmData1.getIrrigationsource());
                    farm.put("irri_source_name", farmData1.getIrriSourceName());
                    farm.put("irrigation_type_id", farmData1.getIrrigationtype());
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
                    farm.put("comp_id", farmerAndFarmDataa.getCompId());
                    farm.put("added_by", farmerAndFarmDataa.getAdded_by());
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
                Log.e(TAG + "L", "Uploading1 " + new Gson().toJson(jsonObject));
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
                            Log.e(TAG, "Uploading1 response " + new Gson().toJson(response.body()));
                            if (response.body().getStatus() == 10) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.multiple_login_msg));
                                    }
                                });
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                                    }
                                });
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                                    }
                                });
                            } else if (response.body().getStatus() == 1) {
                                final String farmerId = response.body().getPersonId().get(0) + "";
                                for (int i = 0; i < response.body().getFarms().size(); i++) {
                                    FarmerFarmInsertResponseFarms datum = response.body().getFarms().get(i);
                                    changeFarmIds(datum.getFarm_id_offline() + "", "" + datum.getFarm_master_num(), "" +
                                            farmerId, farmerAndFarmDataa.getPersonId(), true);
                                }
                                if (index == farmerAndFarmData.size() - 1) {
                                    if (farmerAndFarmDataOnlyFarm.size() > 0) {
//                                        for (int i = 0; i < farmerAndFarmDataOnlyFarm.size(); i++)
                                        addExistingFarmerFarm();
                                    } else {
                                        uploadFarm2();
                                        uploadFarms();
                                    }
                                }
                            } else {
                                try {
                                    Toasty.error(context, resources.getString(R.string.failed_add_farm_and_farmer_msg), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                }
                            }

                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                                }
                            });
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                                }
                            });
                        } else {
                            try {

                                error = response.errorBody().string().toString();
                                Log.e(TAG, "Uploading1 error " + error);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                                notifyApiDelay(LandingActivity.this, response.raw().request().url().toString(),
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
                                    isLoaded[0] = true;
                                    long newMillis = AppConstants.getCurrentMills();
                                    long diff = newMillis - currMillis;
                                    notifyApiDelay(LandingActivity.this, call.request().url().toString(),
                                            "" + diff, internetSPeed, t.toString(), 0);
                                    Log.e(TAG, "Uploading1 Failure " + t.toString());
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


//                DropDownCacheManager.getInstance(context).deleteViaType("NEW_FARM_DATA_ADDITION");
//                DropDownT material = new DropDownT("NEW_FARM_DATA_ADDITION", new Gson().toJson(jsonObject));
//                DropDownCacheManager.getInstance(context).addChemicalUnit(material);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Exception Submit " + e.toString());
            }
            return null;
        }
    }

    private class FarmAsync extends AsyncTask<String, Integer, String> {
        FarmerAndFarmData farmerAndFarm;
        String personId, clusterId;
        int index;

        public FarmAsync(FarmerAndFarmData farmerAndFarm, String personId, String clusterId, int index) {
            this.farmerAndFarm = farmerAndFarm;
            this.personId = personId;
            this.index = index;
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
                farmData.put("country_code", farmerAndFarm.getCountryCode());
                farmData.put("dob", farmerAndFarm.getDob());
                farmData.put("caste", farmerAndFarm.getCast());
                farmData.put("est_income", farmerAndFarm.getAnualIncome());
                farmData.put("gender", farmerAndFarm.getGeder());
                farmData.put("swift_code", farmerAndFarm.getSwiftCode());
                farmData.put("religion", farmerAndFarm.getReligion());
                farmData.put("sv_id", farmerAndFarm.getSvId());
                farmData.put("sv_name", farmerAndFarm.getSvName());
                JSONArray items = new JSONArray();
                for (int i = 0; i < farmerAndFarm.getFarmDataList().size(); i++) {
                    FarmData farmData1 = farmerAndFarm.getFarmDataList().get(i);
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
                    farm.put("sv_id", farmData1.getSvId());
                    farm.put("is_owned", farmData1.getIsOwned());
                    farm.put("owner_id", farmData1.getOwnerId());

                    farm.put("lot_no", farmData1.getId());
                    farm.put("farm_name", farmData1.getName());
                    farm.put("exp_area", farmData1.getArea());
                    if (farmData1.getCropping_pattern() != null && !farmData1.getCropping_pattern().equals("0"))
                        farm.put("cropping_pattern", farmData1.getCropping_pattern());
                    if (farmData1.getIs_irrigated() != null && !farmData1.getIs_irrigated().equals("0"))
                        farm.put("is_irrigated", farmData1.getIs_irrigated());
                    farm.put("season_num", farmData1.getSeason());
                    farm.put("cluster_id", farmerAndFarm.getClusterId());
                    farm.put("previous_crop", farmData1.getPreviouscrop());
                    farm.put("irrigation_source_id", farmData1.getIrrigationsource());
                    farm.put("irri_source_name", farmData1.getIrriSourceName());
                    farm.put("irrigation_type_id", farmData1.getIrrigationtype());
                    farm.put("irri_type_name", farmData1.getIrriTypename());
                    farm.put("soil_type_id", farmData1.getSoiltype());
                    farm.put("soil_type_name", farmData1.getSoiltypeName());
                    farm.put("fAddL1", farmData1.getAddL1());
                    farm.put("fAddL2", farmData1.getAddL2());
                    farm.put("fvillage_or_city", farmData1.getVillageOrCity());
                    farm.put("fmandal_or_tehsil", farmData1.getMandalOrTehsil());
//                    farm.put("fdistrict",farmData1.getDistrict());
//                    farm.put("fstate",farmData1.getState());
//                    farm.put("fcountry",farmData1.getCountry());
                    farm.put("comp_id", farmerAndFarm.getCompId());
                    farm.put("added_by", farmerAndFarm.getAdded_by());
                    farm.put("lat_cord", farmData1.getLat_cord());
                    farm.put("long_cord", farmData1.getLong_cord());
                    farm.put("crop_name", farmData1.getCurrentCropName());
                    farm.put("farm_id_offline", farmData1.getFarmId());
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
                Log.e(TAG + "L2", "Uploading2 " + new Gson().toJson(jsonObject));

                String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                apiInterface.insertExistingFarmerFarmOfflineNew(jsonObject).enqueue(new Callback<FarmerFarmInsertResponse>() {
                    @Override
                    public void onResponse(Call<FarmerFarmInsertResponse> call, Response<FarmerFarmInsertResponse> response) {
                        String error = null;
                        isLoaded[0] = true;
                        Log.e(TAG + "L", "Uploading2 code " + response.code());
                        if (response.isSuccessful()) {
                            Log.e(TAG, "Uploading2 Farm Data Response " + new Gson().toJson(response.body()));
                            if (response.body().getStatus() == 1) {
                                //Update Local Cache
                                if (response.body().getUpdated() != null)
                                    for (int i = 0; i < response.body().getUpdated().size(); i++) {
                                        FarmerFarmInsertResponseFarms datum = response.body().getUpdated().get(i);
                                        changeFarmIds("" + datum.getFarm_id_offline(), "" + datum.getFarm_master_num(), "", "", false);
                                    }
                                if (index == farmerAndFarmDataOnlyFarm.size() - 1) {
                                    uploadFarm2();
                                    uploadFarms();
                                }
                            } else {
                                Toasty.error(context, resources.getString(R.string.failed_add_farm_msg), Toast.LENGTH_LONG).show();
                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            try {
                                showProgress.hideProgressDialog();
                                error = response.errorBody().string().toString();
                                Log.e(TAG, "Uploading2 Err " + response.errorBody().string().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                                notifyApiDelay(LandingActivity.this, response.raw().request().url().toString(),
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

                                    isLoaded[0] = true;
                                    long newMillis = AppConstants.getCurrentMills();
                                    long diff = newMillis - currMillis;
                                    notifyApiDelay(LandingActivity.this, call.request().url().toString(),
                                            "" + diff, internetSPeed, t.toString(), 0);
                                    Log.e(TAG, "Uploading2 Farm Failure " + t.toString());
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

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Uploading2 Exception Submit " + e.toString());
            }
            return null;
        }
    }

    private void uploadFarmAndFarmerData(FarmerAndFarmData farmData, int index) {
        FarmAndFarmerAsync farmAndFarmerAsync = new FarmAndFarmerAsync(farmData, index);
        farmAndFarmerAsync.execute();
    }

    private void addExistingFarmerFarm() {

        for (int i = 0; i < farmerAndFarmDataOnlyFarm.size(); i++) {
            FarmerAndFarmData farmData = farmerAndFarmDataOnlyFarm.get(i);
            Log.e(TAG, "Uploading Farm Data " + new Gson().toJson(farmData));
            FarmAsync farmAsync = new FarmAsync(farmData, farmData.getPersonId(), farmData.getClusterId(), i);
            farmAsync.execute();
        }
    }


    //Farm Upload
    private void uploadFarms() {
        if (farmListToUpload.size() > 0) {
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showProgress.showProgressDialog();
                }
            });
            for (int i = 0; i < farmListToUpload.size(); i++) {
                if (!farmListT.get(i).getIsUploaded().equals("Y")) {
                    showProgress.showProgressDialog();
                    Farm farm = farmListToUpload.get(i);
                    int finalI = i;
                    List<SampleGerminationDatum> germinationDatumList = new ArrayList<>();
                    List<PldData> pldDataList = new ArrayList<>();
                    if (farm.getGerminationDataOffline() != null && !TextUtils.isEmpty(farm.getGerminationDataOffline())) {
                        try {
                            JSONArray array = new JSONArray(farm.getGerminationDataOffline());
                            for (int k = 0; k < array.length(); k++) {
                                JSONObject object = array.getJSONObject(k);
                                SampleGerminationDatum datum = new Gson().fromJson(object.toString(), SampleGerminationDatum.class);
                                datum.setAddedBy(userId);
                                germinationDatumList.add(datum);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (farm.getOfflinePldJson() != null && !TextUtils.isEmpty(farm.getOfflinePldJson())) {
                        try {
                            JSONArray array = new JSONArray(farm.getOfflinePldJson());
                            for (int k = 0; k < array.length(); k++) {
                                JSONObject object = array.getJSONObject(k);
                                PldData datum = new Gson().fromJson(object.toString(), PldData.class);
                                pldDataList.add(datum);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    SendOfflineFarm sendOfflineFarm = new SendOfflineFarm(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                            farmListToUpload.get(i).getFarmId(),
                            farmListToUpload.get(i).getLotNo(), farmListToUpload.get(i).getFarmerId(), farmListToUpload.get(i).getFarmerName(),
                            farmListToUpload.get(i).getFarmerMob(), farmListToUpload.get(i).getClusterId(), farmListToUpload.get(i).getCompId(),
                            farmListToUpload.get(i).getAddL1(), farmListToUpload.get(i).getAddL2(), farmListToUpload.get(i).getVillageOrCity(),
                            farmListToUpload.get(i).getDistrict(), farmListToUpload.get(i).getMandalOrTehsil(), farmListToUpload.get(i).getState(),
                            farmListToUpload.get(i).getCountry(), farmListToUpload.get(i).getExpArea(), farmListToUpload.get(i).getActualArea(),
                            farmListToUpload.get(i).getIrriSourceId(), farmListToUpload.get(i).getPreviousCrop(), farmListToUpload.get(i).getIrriTypeId(),
                            farmListToUpload.get(i).getSoilTypeId(), farmListToUpload.get(i).getSowingDate(), farmListToUpload.get(i).getExpFloweringDate(),
                            farmListToUpload.get(i).getActualFloweringDate(), farmListToUpload.get(i).getExpHarvestDate(), farmListToUpload.get(i).getActualHarvestDate(),
                            farmListToUpload.get(i).getAreaHarvested(), farmListToUpload.get(i).getStandingAcres(), farmListToUpload.get(i).getGermination(),
                            farmListToUpload.get(i).getPopulation(), farmListToUpload.get(i).getSpacingPtp(), farmListToUpload.get(i).getPldAcres(), farmListToUpload.get(i).getPldReason(),
                            farmListToUpload.get(i).getLatCord(), farmListToUpload.get(i).getLongCord(), farmListToUpload.get(i).getSpacingRtr(),
                            farmListToUpload.get(i).getGrade(), farmListToUpload.get(i).getFatherName(), null, null, germinationDatumList, /*pldDataList*/new ArrayList<>());
                    Log.e(TAG, "Offline Farm Upload " + new Gson().toJson(sendOfflineFarm));
                    final boolean[] isLoaded = {false};
                    long currMillis = AppConstants.getCurrentMills();
                    apiInterface.uploadOfflineFarm(sendOfflineFarm).enqueue(new Callback<StatusMsgModel>() {
                        @Override
                        public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                            String error = null;
                            isLoaded[0] = true;
                            if (response.isSuccessful()) {
                                Log.e(TAG, "Farm Offline Response " + new Gson().toJson(response.body()));
                                if (response.body().getStatus() == 1 || response.body().getStatus() == 0) {
                                    farm.setIsUploaded("Y");
                                    //farmListToUpload.get(finalI).setIsUploaded("Y");
                                    FarmCacheManager.getInstance(context).updateFarm(farm);
                                    farmListT.set(finalI, farm);
                                    if (finalI == farmListToUpload.size() - 1) {
                                        showProgress.hideProgressDialog();
                                        if (harvestDetailsList.size() > 0) {
                                            uploadHarvest();
                                        } else if (resourceDataList.size() > 0) {
                                            uploadResources();
                                        } else if (inputCostDataList.size() > 0) {
                                            uploadInputCost();
                                        } else if (offlinePldList.size() > 0) {
                                            uploadPldData();
                                        } else if (doneActivityResponseList.size() > 0) {
                                            uploadDoneActivities();
                                        } else if (visitResponseList.size() > 0) {
                                            uploadVisits();
                                        } else {
                                            showProgress.hideProgressDialog();
                                            PldCacheManager.getInstance(context).deleteAllFarms();
                                            GpsCacheManager.getInstance(context).deleteAllGps(null);
//                                            CompRegCacheManager.getInstance(context).deleteAllData();
                                            FarmCacheManager.getInstance(context).deleteAllFarms();
                                            TimelineCacheManager.getInstance(context).deleteAllTimeline();
                                            HarvestCacheManager.getInstance(context).deleteAllHarvests();
                                            VrCacheManager.getInstance(context).deleteAllVisits();
                                            VisitCacheManager.getInstance(context).deleteAllVisitStructure();
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
                                            FarmerCacheManager.getInstance(context).deleteAllFarmers();
                                            finishAndBackOnline();
                                        }
                                    }
                                } else {
                                    showProgress.hideProgressDialog();
                                    viewFailDialog.showDialog(context, "Opps!", response.body().getMsg());
                                }
                            } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                            } else {
                                try {
                                    error = response.errorBody().string();
                                    showProgress.hideProgressDialog();
                                    viewFailDialog.showDialog(context, "Opps!", "Something went wrong, Please try again later");
                                    Log.e(TAG, "Offline Farm Err " + error);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                                notifyApiDelay(LandingActivity.this, response.raw().request().url().toString(),
                                        "" + diff, internetSPeed, error, response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                            long newMillis = AppConstants.getCurrentMills();
                            isLoaded[0] = true;
                            long diff = newMillis - currMillis;
                            notifyApiDelay(LandingActivity.this, call.request().url().toString(),
                                    "" + diff, internetSPeed, t.toString(), 0);
                            showProgress.hideProgressDialog();
                            Log.e(TAG, "Offline Farm Failure " + finalI + " " + t.toString());
                            viewFailDialog.showDialog(context, "Opps!", "Something went wrong, Please try again later");

                        }
                    });
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            internetFlow(isLoaded[0]);
                        }
                    }, AppConstants.DELAY);
                } else if (i == farmListToUpload.size() - 1) {
                    if (harvestDetailsList.size() > 0) {
                        uploadHarvest();
                    } else if (resourceDataList.size() > 0) {
                        uploadResources();
                    } else if (inputCostDataList.size() > 0) {
                        uploadInputCost();
                    } else if (offlinePldList.size() > 0) {
                        uploadPldData();
                    } else if (doneActivityResponseList.size() > 0) {
                        uploadDoneActivities();
                    } else if (visitResponseList.size() > 0) {
                        uploadVisits();
                    } else {
                        showProgress.hideProgressDialog();
                        PldCacheManager.getInstance(context).deleteAllFarms();
                        GpsCacheManager.getInstance(context).deleteAllGps(null);
//                        CompRegCacheManager.getInstance(this).deleteAllData();
                        FarmCacheManager.getInstance(context).deleteAllFarms();
                        TimelineCacheManager.getInstance(context).deleteAllTimeline();
                        HarvestCacheManager.getInstance(context).deleteAllHarvests();
                        VrCacheManager.getInstance(context).deleteAllVisits();
                        VisitCacheManager.getInstance(context).deleteAllVisitStructure();
                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
                        finishAndBackOnline();
                    }
                }
            }


        } else if (harvestDetailsList.size() > 0) {
            uploadHarvest();
        } else if (resourceDataList.size() > 0) {
            uploadResources();
        } else if (inputCostDataList.size() > 0) {
            uploadInputCost();
        } else if (offlinePldList.size() > 0) {
            uploadPldData();
        } else if (doneActivityResponseList.size() > 0) {
            uploadDoneActivities();
        } else if (visitResponseList.size() > 0) {
            uploadVisits();
        } else {
            showProgress.hideProgressDialog();
            PldCacheManager.getInstance(context).deleteAllFarms();
            GpsCacheManager.getInstance(context).deleteAllGps(null);
//            CompRegCacheManager.getInstance(this).deleteAllData();
            FarmCacheManager.getInstance(context).deleteAllFarms();
            TimelineCacheManager.getInstance(context).deleteAllTimeline();
            HarvestCacheManager.getInstance(context).deleteAllHarvests();
            VrCacheManager.getInstance(context).deleteAllVisits();
            VisitCacheManager.getInstance(context).deleteAllVisitStructure();
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
            finishAndBackOnline();
        }
    }

    String internetSPeed = "";

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
                            ConnectivityUtils.checkSpeedWithColor(LandingActivity.this, new ConnectivityUtils.ColorListener() {
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

    //Harvest Upload
    private void uploadHarvest() {
        if (harvestDetailsList.size() > 0) {
            showProgress.showProgressDialog();
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);

            for (int i = 0; i < harvestDetailsList.size(); i++) {
                final int finalI = i;
                if (!harvestListT.get(i).getIsUploaded().equals("Y")) {
                    ViewHarvestDetails viewHarvestDetails = harvestDetailsList.get(i);
                    harvestDetailsList.get(i).setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));

                    final boolean[] isLoaded = {false};
                    long currMillis = AppConstants.getCurrentMills();
                    if (viewHarvestDetails.getData1() != null && viewHarvestDetails.getData() != null) {
                        for (int k = 0; k < viewHarvestDetails.getData().size(); k++) {
                            final int finalK=k;
                            HarvestDetailMaster masterDetails=viewHarvestDetails.getData1().get(k);
                            List<HarvestDetailInnerData>innerData=viewHarvestDetails.getData().get(k);
                            List<String> bagArr = new ArrayList<>();
                            List<String> weightArr = new ArrayList<>();
                            //int bagArr[] = new int[bagList.size()];
                            float total_net_weight = 0;
                            float total_gross_weight = 0;

                            for (int l = 0; l < innerData.size(); l++) {
                                weightArr.add(String.valueOf(innerData.get(l).getNetWt()));
                                bagArr.add(String.valueOf(innerData.get(l).getBagNo()));
                            }
                            if (AppConstants.isValidString(masterDetails.getTotalNetWeight()))
                                total_net_weight=Float.valueOf(masterDetails.getTotalNetWeight());
                            SendHarvestData sendHarvestDataObject = new SendHarvestData();
                            sendHarvestDataObject.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                            sendHarvestDataObject.setFarm_id(viewHarvestDetails.getFarmId());
                            sendHarvestDataObject.setSupervisor_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID));
                            sendHarvestDataObject.setArea_harvested(Float.parseFloat(masterDetails.getHarvestedArea()));
                            sendHarvestDataObject.setStanding_acres(Float.parseFloat(masterDetails.getStandingArea()));
                            sendHarvestDataObject.setBag_no(bagArr);
                            sendHarvestDataObject.setNet_wt(weightArr);

                            sendHarvestDataObject.setGross_wt(weightArr);

                            sendHarvestDataObject.setTotal_net_weight(total_net_weight);
                            sendHarvestDataObject.setTotal_gross_weight(total_net_weight);
                            //sendHarvestDataObject.setNet_wt();
                            sendHarvestDataObject.setHarvest_date(masterDetails.getHarvestDate());
                            sendHarvestDataObject.setWeighment_date(masterDetails.getWeighmentDate());
                            sendHarvestDataObject.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                            sendHarvestDataObject.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                            Log.e(TAG, "Offline Harvest Upload " + new Gson().toJson(sendHarvestDataObject));
                            apiInterface.getStatusMsgModelForHarvestBags(sendHarvestDataObject).enqueue(new Callback<StatusMsgModel>() {
                                @Override
                                public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                                    String error = null;
                                    isLoaded[0] = true;
                                    if (response.isSuccessful()) {
                                        Log.e(TAG, "Offline Harvest res " + new Gson().toJson(response.body()));
                                        if (response.body().getStatus() == 1) {
                                            HarvestT harvestT = harvestListT.get(finalI);
                                            harvestT.setIsUploaded("Y");
                                            harvestDetailsList.get(finalI).setIsUploaded("Y");
                                            HarvestCacheManager.getInstance(context).updateHarvestData(harvestT);
                                            harvestListT.set(finalI, harvestT);
                                            Log.e(TAG, "FinalI = " + finalI + ", harvest zize = " + harvestDetailsList.size());
                                            if (finalK==viewHarvestDetails.getData().size()-1&&finalI == harvestDetailsList.size() - 1) {
                                                if (resourceDataList.size() > 0) {
                                                    uploadResources();
                                                } else if (inputCostDataList.size() > 0) {
                                                    uploadInputCost();
                                                } else if (offlinePldList.size() > 0) {
                                                    uploadPldData();
                                                } else if (doneActivityResponseList.size() > 0) {
                                                    uploadDoneActivities();
                                                } else if (visitResponseList.size() > 0) {
                                                    uploadVisits();
                                                } else {
                                                    showProgress.hideProgressDialog();
                                                    GpsCacheManager.getInstance(context).deleteAllGps(null);
                                                    PldCacheManager.getInstance(context).deleteAllFarms();
//                                            CompRegCacheManager.getInstance(context).deleteAllData();
                                                    FarmCacheManager.getInstance(context).deleteAllFarms();
                                                    TimelineCacheManager.getInstance(context).deleteAllTimeline();
                                                    HarvestCacheManager.getInstance(context).deleteAllHarvests();
                                                    VrCacheManager.getInstance(context).deleteAllVisits();
                                                    VisitCacheManager.getInstance(context).deleteAllVisitStructure();
                                                    FarmerCacheManager.getInstance(context).deleteAllFarmers();
                                                    SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
                                                    finishAndBackOnline();

                                                }
                                            }
                                        } else {
                                            showProgress.hideProgressDialog();
                                            viewFailDialog.showDialog(context, "Opps!", response.body().getMsg());
                                        }
                                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                        //un authorized access
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                        // auth token expired
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                                    } else {
                                        try {
                                            showProgress.hideProgressDialog();
                                            viewFailDialog.showDialog(context, "Opps!", "Something went wrong, Please try again later");
                                            error = response.errorBody().string();
                                            Log.e(TAG, "Offline Harvest Err " + error);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    long newMillis = AppConstants.getCurrentMills();
                                    long diff = newMillis - currMillis;
                                    if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                                        notifyApiDelay(LandingActivity.this, response.raw().request().url().toString(),
                                                "" + diff, internetSPeed, error, response.code());
                                    }
                                }

                                @Override
                                public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                                    showProgress.hideProgressDialog();
                                    isLoaded[0] = true;
                                    long newMillis = AppConstants.getCurrentMills();
                                    long diff = newMillis - currMillis;
                                    notifyApiDelay(LandingActivity.this, call.request().url().toString(),
                                            "" + diff, internetSPeed, t.toString(), 0);
                                    Log.e(TAG, "Offline Harvest Failure " + finalI + " " + t.toString());
                                    viewFailDialog.showDialog(context, "Opps!", "Something went wrong, Please try again later");
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
                    }
                } else if (i == harvestDetailsList.size() - 1) {
                    if (resourceDataList.size() > 0) {
                        uploadResources();
                    } else if (inputCostDataList.size() > 0) {
                        uploadInputCost();
                    } else if (offlinePldList.size() > 0) {
                        uploadPldData();
                    } else if (doneActivityResponseList.size() > 0) {
                        uploadDoneActivities();
                    } else if (visitResponseList.size() > 0) {
                        uploadVisits();
                    } else {
                        PldCacheManager.getInstance(context).deleteAllFarms();
                        GpsCacheManager.getInstance(context).deleteAllGps(null);
//                        CompRegCacheManager.getInstance(this).deleteAllData();
                        FarmCacheManager.getInstance(context).deleteAllFarms();
                        TimelineCacheManager.getInstance(context).deleteAllTimeline();
                        HarvestCacheManager.getInstance(context).deleteAllHarvests();
                        VrCacheManager.getInstance(context).deleteAllVisits();
                        VisitCacheManager.getInstance(context).deleteAllVisitStructure();
                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
                        Intent intent = getIntent();
                        finishAndBackOnline();
                    }
                }
            }
        } else if (resourceDataList.size() > 0) {
            uploadResources();
        } else if (inputCostDataList.size() > 0) {
            uploadInputCost();
        } else if (offlinePldList.size() > 0) {
            uploadPldData();
        } else if (doneActivityResponseList.size() > 0) {
            uploadDoneActivities();
        } else if (visitResponseList.size() > 0) {
            uploadVisits();
        } else {
            PldCacheManager.getInstance(context).deleteAllFarms();
            GpsCacheManager.getInstance(context).deleteAllGps(null);
//            CompRegCacheManager.getInstance(this).deleteAllData();
            FarmCacheManager.getInstance(context).deleteAllFarms();
            TimelineCacheManager.getInstance(context).deleteAllTimeline();
            HarvestCacheManager.getInstance(context).deleteAllHarvests();
            VrCacheManager.getInstance(context).deleteAllVisits();
            VisitCacheManager.getInstance(context).deleteAllVisitStructure();
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
            Intent intent = getIntent();
            finishAndBackOnline();
        }
    }

    //Resource Upload
    private void uploadResources() {
        if (resourceDataList.size() > 0) {
            showProgress.showProgressDialog();
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            for (int i = 0; i < resourceDataList.size(); i++) {
                if (!isResUploaded.get(i)) {
                    final int finalI = i;
                    List<ResourceData> data = resourceDataList.get(i);
                    List<ResourceSubmitDatum> resourceDataList = new ArrayList<>();
                    for (int k = 0; k < data.size(); k++) {
                        ResourceData resourceData = data.get(k);
                        ResourceSubmitDatum datum = new ResourceSubmitDatum(resourceData.getCompId(), resourceData.getFarmId(),
                                resourceData.getResourceTypeId(), resourceData.getActivityTypeId(),
                                resourceData.getValue(),
                                resourceData.getResourceTypeId(), resourceData.getUsedOn(),
                                resourceData.getAddedBy(),
                                resourceData.getOtherUnit(), resourceData.getOtherMultiplier(), resourceData.getDoa(),
                                resourceData.getUnit(),
                                resourceData.getName(), resourceData.getValue());
                        resourceDataList.add(datum);
                    }
                    CostAndResourceSubmitData sendOfflineResource = new CostAndResourceSubmitData();
                    sendOfflineResource.setInputCostSubmitList(new ArrayList<>());
                    sendOfflineResource.setResourceSubmitList(resourceDataList);
                    sendOfflineResource.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                    sendOfflineResource.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                    Log.e(TAG, "Offline resource Upload " + new Gson().toJson(sendOfflineResource));
                    final boolean[] isLoaded = {false};
                    long currMillis = AppConstants.getCurrentMills();
                    apiInterface.insertCostAndResourceData(sendOfflineResource).enqueue(new Callback<CostSubmitResponse>() {
                        @Override
                        public void onResponse(Call<CostSubmitResponse> call, Response<CostSubmitResponse> response) {
                            String error = null;
                            isLoaded[0] = true;
                            if (response.isSuccessful()) {
                                Log.e(TAG, "Offline Resource Res " + new Gson().toJson(response.body()));
                                if (response.body().getStatus() == 1) {
                                    ResourceCostT resourceCostT = resourceCostListT.get(finalI);
                                    resourceCostT.setIsUploaded("Y");
                                    ResourceCacheManager.getInstance(context).update(resourceCostT);
                                    isResUploaded.set(finalI, true);
                                    //resourceDataList.remove(finalI);

                                    if (finalI == resourceDataList.size() - 1) {
                                        showProgress.hideProgressDialog();
                                        if (inputCostDataList.size() > 0) {
                                            uploadInputCost();
                                        } else if (offlinePldList.size() > 0) {
                                            uploadPldData();
                                        } else if (doneActivityResponseList.size() > 0) {
                                            uploadDoneActivities();
                                        } else if (visitResponseList.size() > 0) {
                                            uploadVisits();
                                        } else {
                                            showProgress.hideProgressDialog();
                                            PldCacheManager.getInstance(context).deleteAllFarms();
                                            GpsCacheManager.getInstance(context).deleteAllGps(null);
//                                            CompRegCacheManager.getInstance(context).deleteAllData();
                                            FarmCacheManager.getInstance(context).deleteAllFarms();
                                            TimelineCacheManager.getInstance(context).deleteAllTimeline();
                                            HarvestCacheManager.getInstance(context).deleteAllHarvests();
                                            VrCacheManager.getInstance(context).deleteAllVisits();
                                            VisitCacheManager.getInstance(context).deleteAllVisitStructure();
                                            FarmerCacheManager.getInstance(context).deleteAllFarmers();
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
                                            finishAndBackOnline();
                                        }
                                    }

                                } else {
                                    showProgress.hideProgressDialog();
                                    viewFailDialog.showDialog(context, "Opps!", response.body().getMsg());
                                }
                            } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                            } else {
                                try {
                                    showProgress.hideProgressDialog();
                                    viewFailDialog.showDialog(context, "Opps!", "Something went wrong, Please try again later");
                                    error = response.errorBody().string();
                                    Log.e(TAG, "Offline Resorce Err " + error);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                                notifyApiDelay(LandingActivity.this, response.raw().request().url().toString(),
                                        "" + diff, internetSPeed, error, response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<CostSubmitResponse> call, Throwable t) {
                            showProgress.hideProgressDialog();
                            isLoaded[0] = true;
                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            notifyApiDelay(LandingActivity.this, call.request().url().toString(),
                                    "" + diff, internetSPeed, t.toString(), 0);
                            Log.e(TAG, "Offline Resorce Failure " + finalI + " " + t.toString());
                            viewFailDialog.showDialog(context, "Opps!", "Something went wrong, Please try again later");

                        }
                    });

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            internetFlow(isLoaded[0]);
                        }
                    }, AppConstants.DELAY);
                } else if (i == resourceDataList.size() - 1) {
                    if (inputCostDataList.size() > 0) {
                        uploadInputCost();
                    } else if (offlinePldList.size() > 0) {
                        uploadPldData();
                    } else if (doneActivityResponseList.size() > 0) {
                        uploadDoneActivities();
                    } else if (visitResponseList.size() > 0) {
                        uploadVisits();
                    } else {
                        showProgress.hideProgressDialog();
                        PldCacheManager.getInstance(context).deleteAllFarms();
                        GpsCacheManager.getInstance(context).deleteAllGps(null);
//                        CompRegCacheManager.getInstance(this).deleteAllData();
                        FarmCacheManager.getInstance(context).deleteAllFarms();
                        TimelineCacheManager.getInstance(context).deleteAllTimeline();
                        HarvestCacheManager.getInstance(context).deleteAllHarvests();
                        VrCacheManager.getInstance(context).deleteAllVisits();
                        VisitCacheManager.getInstance(context).deleteAllVisitStructure();
                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
                        finishAndBackOnline();
                    }
                }
            }


        } else if (inputCostDataList.size() > 0) {
            uploadInputCost();
        } else if (offlinePldList.size() > 0) {
            uploadPldData();
        } else if (doneActivityResponseList.size() > 0) {
            uploadDoneActivities();
        } else if (visitResponseList.size() > 0) {
            uploadVisits();
        } else {
            showProgress.hideProgressDialog();
            PldCacheManager.getInstance(context).deleteAllFarms();
            GpsCacheManager.getInstance(context).deleteAllGps(null);
//            CompRegCacheManager.getInstance(this).deleteAllData();
            FarmCacheManager.getInstance(context).deleteAllFarms();
            TimelineCacheManager.getInstance(context).deleteAllTimeline();
            HarvestCacheManager.getInstance(context).deleteAllHarvests();
            VrCacheManager.getInstance(context).deleteAllVisits();
            VisitCacheManager.getInstance(context).deleteAllVisitStructure();
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
            finishAndBackOnline();
        }
    }

    //Input Cost Upload
    private void uploadInputCost() {
        if (inputCostDataList.size() > 0) {
            showProgress.showProgressDialog();
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            final boolean[] isLoaded = {false};
            long currMillis = AppConstants.getCurrentMills();
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            for (int i = 0; i < inputCostDataList.size(); i++) {
                if (!isInputUploaded.get(i)) {
                    final int finalI = i;
//                    SendOfflineInputCost sendOfflineInputCost = new SendOfflineInputCost(inputCostDataList.get(i), SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));


                    List<InputCostData> data = inputCostDataList.get(i);
                    List<InputCostSubmitDatum> resourceDataList = new ArrayList<>();
                    for (int k = 0; k < data.size(); k++) {
                        InputCostData resourceData = data.get(k);
                        InputCostSubmitDatum datum = new InputCostSubmitDatum(
                                resourceData.getCompId(), resourceData.getFarmId(),
                                resourceData.getCostTypeId(), resourceData.getOtherTypeName(),
                                resourceData.getExpense(), resourceData.getExpenseDate(),
                                resourceData.getAddedBy(), resourceData.getDoa(),
                                resourceData.getName());
                        resourceDataList.add(datum);
                    }
                    CostAndResourceSubmitData sendOfflineResource = new CostAndResourceSubmitData();
                    sendOfflineResource.setInputCostSubmitList(resourceDataList);
                    sendOfflineResource.setResourceSubmitList(new ArrayList<>());
                    sendOfflineResource.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                    sendOfflineResource.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));

                    Log.e(TAG, "Offline Inputcost Upload " + new Gson().toJson(sendOfflineResource));
                    apiInterface.insertCostAndResourceData(sendOfflineResource).enqueue(new Callback<CostSubmitResponse>() {
                        @Override
                        public void onResponse(Call<CostSubmitResponse> call, Response<CostSubmitResponse> response) {
                            String error = null;
                            isLoaded[0] = true;
                            if (response.isSuccessful()) {
                                Log.e(TAG, "Offline Input Cost Res " + new Gson().toJson(response.body()));
                                if (response.body().getStatus() == 1) {
                                    InputCostT inputCostT = inputCostListT.get(finalI);
                                    inputCostT.setIsUploaded("Y");
                                    InputCostCacheManager.getInstance(context).update(inputCostT);
                                    //inputCostDataList.remove(finalI);
                                    isInputUploaded.set(finalI, true);
                                    if (finalI == inputCostDataList.size() - 1) {
                                        showProgress.hideProgressDialog();
                                        if (offlinePldList.size() > 0) {
                                            uploadPldData();
                                        } else if (doneActivityResponseList.size() > 0) {
                                            uploadDoneActivities();
                                        } else if (visitResponseList.size() > 0) {
                                            uploadVisits();
                                        } else {
                                            showProgress.hideProgressDialog();
                                            PldCacheManager.getInstance(context).deleteAllFarms();
                                            GpsCacheManager.getInstance(context).deleteAllGps(null);
//                                            CompRegCacheManager.getInstance(context).deleteAllData();
                                            FarmCacheManager.getInstance(context).deleteAllFarms();
                                            TimelineCacheManager.getInstance(context).deleteAllTimeline();
                                            HarvestCacheManager.getInstance(context).deleteAllHarvests();
                                            VrCacheManager.getInstance(context).deleteAllVisits();
                                            VisitCacheManager.getInstance(context).deleteAllVisitStructure();
                                            FarmerCacheManager.getInstance(context).deleteAllFarmers();
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
                                            finishAndBackOnline();
                                        }
                                    }
                                } else {
                                    showProgress.hideProgressDialog();
                                    viewFailDialog.showDialog(context, "Opps!", response.body().getMsg());
                                }
                            } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                            } else {
                                try {
                                    showProgress.hideProgressDialog();
                                    viewFailDialog.showDialog(context, "Opps!", "Something went wrong, Please try again later");
                                    error = response.errorBody().string();
                                    Log.e(TAG, "Offline InputCost Err " + error);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                                notifyApiDelay(LandingActivity.this, response.raw().request().url().toString(),
                                        "" + diff, internetSPeed, error, response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<CostSubmitResponse> call, Throwable t) {
                            showProgress.hideProgressDialog();
                            isLoaded[0] = true;
                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            notifyApiDelay(LandingActivity.this, call.request().url().toString(),
                                    "" + diff, internetSPeed, t.toString(), 0);
                            Log.e(TAG, "Offline InputCost Failure " + finalI + " " + t.toString());
                            viewFailDialog.showDialog(context, "Opps!", "Something went wrong, Please try again later");
                        }
                    });
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            internetFlow(isLoaded[0]);
                        }
                    }, AppConstants.DELAY);
                } else if (i == inputCostDataList.size() - 1) {
                    if (offlinePldList.size() > 0) {
                        uploadPldData();
                    } else if (doneActivityResponseList.size() > 0) {
                        uploadDoneActivities();
                    } else if (visitResponseList.size() > 0) {
                        uploadVisits();
                    } else {
                        showProgress.hideProgressDialog();
                        PldCacheManager.getInstance(context).deleteAllFarms();
                        GpsCacheManager.getInstance(context).deleteAllGps(null);
//                        CompRegCacheManager.getInstance(this).deleteAllData();
                        FarmCacheManager.getInstance(context).deleteAllFarms();
                        TimelineCacheManager.getInstance(context).deleteAllTimeline();
                        HarvestCacheManager.getInstance(context).deleteAllHarvests();
                        VrCacheManager.getInstance(context).deleteAllVisits();
                        VisitCacheManager.getInstance(context).deleteAllVisitStructure();
                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
                        finishAndBackOnline();
                    }
                }
            }

        } else if (offlinePldList.size() > 0) {
            uploadPldData();
        } else if (doneActivityResponseList.size() > 0) {
            uploadDoneActivities();
        } else if (visitResponseList.size() > 0) {
            uploadVisits();
        } else {
            showProgress.hideProgressDialog();
            PldCacheManager.getInstance(context).deleteAllFarms();
            GpsCacheManager.getInstance(context).deleteAllGps(null);
//            CompRegCacheManager.getInstance(this).deleteAllData();
            FarmCacheManager.getInstance(context).deleteAllFarms();
            TimelineCacheManager.getInstance(context).deleteAllTimeline();
            HarvestCacheManager.getInstance(context).deleteAllHarvests();
            VrCacheManager.getInstance(context).deleteAllVisits();
            VisitCacheManager.getInstance(context).deleteAllVisitStructure();
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
            finishAndBackOnline();
        }
    }

    //Upload Crop loss damage data
    private void uploadPldData() {
        if (offlinePldList.size() > 0) {
            showProgress.showProgressDialog();
            new UploadOfflinePld(offlinePldList).execute();
        } else {
            uploadDoneActivities();
        }
    }

    //Calendar Activity Upload
    private void uploadDoneActivities() {
        if (doneActivityResponseList.size() > 0) {
            showProgress.showProgressDialog();
            UploadOfflineActivity uploadOfflineActivity = new UploadOfflineActivity(doneActivityResponseList);
            uploadOfflineActivity.execute();

        } else if (visitResponseList.size() > 0) {
            uploadVisits();
        } else {
            showProgress.hideProgressDialog();
            PldCacheManager.getInstance(context).deleteAllFarms();
            GpsCacheManager.getInstance(context).deleteAllGps(null);
//            CompRegCacheManager.getInstance(this).deleteAllData();
            FarmCacheManager.getInstance(context).deleteAllFarms();
            TimelineCacheManager.getInstance(context).deleteAllTimeline();
            HarvestCacheManager.getInstance(context).deleteAllHarvests();
            VrCacheManager.getInstance(context).deleteAllVisits();
            VisitCacheManager.getInstance(context).deleteAllVisitStructure();
            FarmerCacheManager.getInstance(context).deleteAllFarmers();
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
            finishAndBackOnline();
        }
    }

    //Visit Upload
    private void uploadVisits() {
        if (visitResponseList.size() > 0) {
            showProgress.showProgressDialog();
            UploadOfflineVisitAsync uploadOfflineVisitAsync = new UploadOfflineVisitAsync(visitResponseList);
            uploadOfflineVisitAsync.execute();

        } else {
            PldCacheManager.getInstance(context).deleteAllFarms();
            GpsCacheManager.getInstance(context).deleteAllGps(null);
//            CompRegCacheManager.getInstance(this).deleteAllData();
            FarmCacheManager.getInstance(context).deleteAllFarms();
            TimelineCacheManager.getInstance(context).deleteAllTimeline();
            FarmerCacheManager.getInstance(context).deleteAllFarmers();
            HarvestCacheManager.getInstance(context).deleteAllHarvests();
            VrCacheManager.getInstance(context).deleteAllVisits();
            VisitCacheManager.getInstance(context).deleteAllVisitStructure();
            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
            finishAndBackOnline();
        }
    }

    String message1 = "";
    String message2 = "";
    String message3 = "";

    public Date parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy:MM:dd HH:mm:ss";
        String outputPattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public Date parseForServer(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private void prepareMessages(String imgLat, String imageLong, List<LatLng> latLngList) {
        message1 = "";
        message2 = "";
        message3 = "";
        try {
            if (imgLat != null && imageLong != null && !TextUtils.isEmpty(imgLat) && !TextUtils.isEmpty(imageLong)) {

                if (latLngList != null && latLngList.size() > 0) {
                    LatLng latLng = new LatLng(Double.valueOf(imgLat), Double.valueOf(imageLong));
                    if (latLngList != null && latLngList.size() > 2 && AppConstants.isPointInPolygon(latLng, latLngList)) {
                        message1 = "";
                        message2 = "";
                    } else {
                        message1 = "Image might not be from farm";
                        message2 = "Gps coordinates mismatch";
                    }

                } else {
                    message1 = "Image might not be from farm";
                    message2 = "Farm coordinates missing";
                }

            } else {
                message1 = "Image might not be from farm";
                message2 = "Image coordinates missing";
            }


        } catch (Exception e) {
            message1 = "Image might not be from farm";
            message2 = "coordinates missing";
            e.printStackTrace();
        }

    }

    private void prepareMessage2(String file, String date) {

        try {

            ExifInterface exifInterface = new ExifInterface(file);

            if (exifInterface.getAttribute(ExifInterface.TAG_DATETIME) != null) {

                Date final_date = parseDateToddMMyyyy(exifInterface.getAttribute(ExifInterface.TAG_DATETIME));
                Date c = parseForServer(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(c);
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                Date newDate = calendar.getTime();
                if (newDate.after(final_date)) {
                    message3 = "Image is old";
                    Log.e(TAG, "BOTH DATES " + "     " + final_date.toString() + "    " + newDate.toString());
                } else {
                    message3 = "";
                    Log.e(TAG, "BOTH DATES " + "     " + final_date.toString() + "    " + newDate.toString());
                }

            } else {
                message3 = "Image date not available";
            }
        } catch (IOException e) {
            e.printStackTrace();
            message3 = "Image date not available";
        }
    }


    private class UploadOfflinePld extends AsyncTask<String, Integer, String> {
        List<OfflinePld> doneActivityResponseList;

        public UploadOfflinePld(List<OfflinePld> doneActivityResponseList) {
            this.doneActivityResponseList = doneActivityResponseList;
        }

        @Override
        protected String doInBackground(String... strings) {
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);

            for (int i = 0; i < doneActivityResponseList.size(); i++) {
                if (!doneActivityResponseList.get(i).getIsUploaded().equals("Y")) {

                    SendPldData sendPldData = new Gson().fromJson(doneActivityResponseList.get(i).getPldData(), SendPldData.class);

                    String farmId = sendPldData.getFarm_id();

                    img = new String[sendPldData.getImageListOffline().size()];
                    for (int j = 0; j < sendPldData.getImageListOffline().size(); j++) {

                        if (coordinateHashMap.containsKey(farmId)) {
                            prepareMessages(sendPldData.getImageListOffline().get(j).getLat(), sendPldData.getImageListOffline().get(j).getLon(),
                                    coordinateHashMap.get(farmId));
                        } else {
                            message1 = "";
                            message2 = "";
                        }
                        prepareMessage2(sendPldData.getImageListOffline().get(j).getImgLink(), sendPldData.getDoa());

                        Bitmap bm = BitmapFactory.decodeFile(sendPldData.getImageListOffline().get(j).getImgLink());
                        if (bm != null) {
                            bm = addWaterMark(bm, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NAME),
                                    sendPldData.getImageListOffline().get(j).getLat(), sendPldData.getImageListOffline().get(j).getLon());
                            compressBitmapp(bm, 2, 20, j);
                        }
                    }
                    final int finalI = i;
                    sendPldData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));

                    Log.e(TAG, "Offline Pld Upload " + new Gson().toJson(sendPldData));
                    sendPldData.setImageList(img);
                    final boolean[] isLoaded = {false};
                    long currMillis = AppConstants.getCurrentMills();
                    apiInterface.getStatusMsgForPldArea(sendPldData).enqueue(new Callback<StatusMsgModel>() {
                        @Override
                        public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                            String error = null;
                            isLoaded[0] = true;
                            if (response.isSuccessful()) {
                                Log.e(TAG, "Offline Pld Res " + new Gson().toJson(response.body()));
                                if (response.body().getStatus() == 1) {
                                    OfflinePld doneActivity = doneActivityResponseList.get(finalI);
                                    doneActivity.setIsUploaded("Y");
                                    doneActivityResponseList.get(finalI).setIsUploaded("Y");
                                    PldCacheManager.getInstance(context).updatePld(doneActivity);
                                    offlinePldList.set(finalI, doneActivity);


                                    if (sendPldData.getImageListOffline() != null && sendPldData.getImageListOffline().size() > 0) {
                                        try {
                                            for (int k = 0; k < sendPldData.getImageListOffline().size(); k++) {
                                                deleteImage(sendPldData.getImageListOffline().get(k).getImgLink());
                                            }
                                        } catch (Exception e) {

                                        }
                                    }

                                    if (finalI == doneActivityResponseList.size() - 1) {
                                        showProgress.hideProgressDialog();
                                        if (doneActivityResponseList.size() > 0) {
                                            uploadDoneActivities();
                                        } else if (visitResponseList.size() > 0) {
                                            uploadVisits();
                                        } else {
                                            showProgress.hideProgressDialog();
                                            GpsCacheManager.getInstance(context).deleteAllGps(null);
                                            PldCacheManager.getInstance(context).deleteAllFarms();
//                                            CompRegCacheManager.getInstance(context).deleteAllData();
                                            FarmCacheManager.getInstance(context).deleteAllFarms();
                                            TimelineCacheManager.getInstance(context).deleteAllTimeline();
                                            HarvestCacheManager.getInstance(context).deleteAllHarvests();
                                            VrCacheManager.getInstance(context).deleteAllVisits();
                                            VisitCacheManager.getInstance(context).deleteAllVisitStructure();
                                            FarmerCacheManager.getInstance(context).deleteAllFarmers();
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
                                            finishAndBackOnline();
                                        }
                                    }

                                } else {
                                    showProgress.hideProgressDialog();
                                    viewFailDialog.showDialog(context, "Opps!", response.body().getMsg());
                                }
                            } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                            } else {
                                try {
                                    error = response.errorBody().string();
                                    showProgress.hideProgressDialog();
                                    viewFailDialog.showDialog(context, resources.getString(R.string.oops_messsage_label) + "!", resources.getString(R.string.something_went_wrong_msg));
                                    Log.e(TAG, "Offline Pld Err " + error);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                                notifyApiDelay(LandingActivity.this, response.raw().request().url().toString(),
                                        "" + diff, internetSPeed, error, response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            isLoaded[0] = true;
                            notifyApiDelay(LandingActivity.this, call.request().url().toString(),
                                    "" + diff, internetSPeed, t.toString(), 0);
                            showProgress.hideProgressDialog();
                            Log.e(TAG, "Offline Cal Activity Failure " + finalI + " " + t.toString());
                            viewFailDialog.showDialog(context, resources.getString(R.string.oops_messsage_label) + "!", resources.getString(R.string.something_went_wrong_msg));

                        }
                    });
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            internetFlow(isLoaded[0]);
                        }
                    }, AppConstants.DELAY);
                } else if (i == doneActivityResponseList.size() - 1) {
                    if (doneActivityResponseList.size() > 0) {
                        uploadDoneActivities();
                    } else if (visitResponseList.size() > 0) {
                        uploadVisits();
                    } else {
                        showProgress.hideProgressDialog();
                        GpsCacheManager.getInstance(context).deleteAllGps(null);
                        PldCacheManager.getInstance(context).deleteAllFarms();
//                        CompRegCacheManager.getInstance(context).deleteAllData();
                        FarmCacheManager.getInstance(context).deleteAllFarms();
                        TimelineCacheManager.getInstance(context).deleteAllTimeline();
                        HarvestCacheManager.getInstance(context).deleteAllHarvests();
                        VrCacheManager.getInstance(context).deleteAllVisits();
                        VisitCacheManager.getInstance(context).deleteAllVisitStructure();
                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
                        finishAndBackOnline();
                    }
                }
            }

            return null;
        }
    }


    private class UploadOfflineActivity extends AsyncTask<String, Integer, String> {
        List<DoneActivityResponseNew> doneActivityResponseList;

        public UploadOfflineActivity(List<DoneActivityResponseNew> doneActivityResponseList) {
            this.doneActivityResponseList = doneActivityResponseList;
        }

        @Override
        protected String doInBackground(String... strings) {
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            for (int i = 0; i < doneActivityResponseList.size(); i++) {
                if (!activityListT.get(i).getIsUploaded().equals("Y")) {
                    img = new String[doneActivityResponseList.get(i).getDoneActivityImageList().size()];
                    String farmId = doneActivityResponseList.get(i).getFarmId();
                    String doa = doneActivityResponseList.get(i).getDoa();
                    for (int j = 0; j < doneActivityResponseList.get(i).getDoneActivityImageList().size(); j++) {

                        if (coordinateHashMap.containsKey(farmId)) {
                            prepareMessages(doneActivityResponseList.get(i).getDoneActivityImageList().get(j).getLat(),
                                    doneActivityResponseList.get(i).getDoneActivityImageList().get(j).getLon(),
                                    coordinateHashMap.get(farmId));
                        } else {
                            message1 = "";
                            message2 = "";
                        }
                        prepareMessage2(doneActivityResponseList.get(i).getDoneActivityImageList().get(j).getImgLink(), doa);


                        Bitmap bm = BitmapFactory.decodeFile(doneActivityResponseList.get(i).getDoneActivityImageList().get(j).getImgLink());
                        if (bm != null) {
                            bm = addWaterMark(bm, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NAME),
                                    doneActivityResponseList.get(i).getDoneActivityImageList().get(j).getLat(),
                                    doneActivityResponseList.get(i).getDoneActivityImageList().get(j).getLon());
                            compressBitmapp(bm, 2, 20, j);
                        }
                    }
                    final int finalI = i;
                    doneActivityResponseList.get(i).setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                    Log.e(TAG, "Offline Activity Upload " + new Gson().toJson(doneActivityResponseList.get(i)));
                    doneActivityResponseList.get(i).setImg_list(img);

                    SendTimelineActivityDataNew activityData = new SendTimelineActivityDataNew();
                    activityData.setAgri_input_data(doneActivityResponseList.get(i).getAgriInputs());
                    activityData.setFarm_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
                    activityData.setFarmer_reply(doneActivityResponseList.get(i).getTextReply());
                    activityData.setIs_done("Y");
                    activityData.setFarm_cal_activity_id(doneActivityResponseList.get(i).getFarmCalActivityId());
                    activityData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                    activityData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                    activityData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                    Log.e(TAG, "Sending Activity Data " + new Gson().toJson(activityData));
                    activityData.setImageList(img);//image_array

                    final boolean[] isLoaded = {false};
                    long currMillis = AppConstants.getCurrentMills();
                    apiInterface.setTimelineActivityNew(activityData).enqueue(new Callback<StatusMsgModel>() {
                        @Override
                        public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                            String error = null;
                            isLoaded[0] = true;
                            if (response.isSuccessful()) {
                                /*try {
                                    Log.e(TAG, "Offline Activity Res " + response.body().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }*/
                                Log.e(TAG, "Offline Activity Res " + new Gson().toJson(response.body()));
                                if (response.body().getStatus() == 1) {
                                    DoneActivity doneActivity = activityListT.get(finalI);
                                    doneActivity.setIsUploaded("Y");
                                    activityListT.get(finalI).setIsUploaded("Y");
                                    ActivityCacheManager.getInstance(context).update(doneActivity);
                                    activityListT.set(finalI, doneActivity);

                                    if (doneActivityResponseList.get(finalI).getDoneActivityImageList() != null && doneActivityResponseList.get(finalI).getDoneActivityImageList().size() > 0) {
                                        try {
                                            for (int k = 0; k < doneActivityResponseList.get(finalI).getDoneActivityImageList().size(); k++) {
                                                deleteImage(doneActivityResponseList.get(finalI).getDoneActivityImageList().get(k).getImgLink());
                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                    if (finalI == doneActivityResponseList.size() - 1) {
                                        showProgress.hideProgressDialog();
                                        if (visitResponseList.size() > 0) {
                                            uploadVisits();
                                        } else {
                                            showProgress.hideProgressDialog();
                                            GpsCacheManager.getInstance(context).deleteAllGps(null);
                                            PldCacheManager.getInstance(context).deleteAllFarms();
//                                            CompRegCacheManager.getInstance(context).deleteAllData();
                                            FarmCacheManager.getInstance(context).deleteAllFarms();
                                            TimelineCacheManager.getInstance(context).deleteAllTimeline();
                                            HarvestCacheManager.getInstance(context).deleteAllHarvests();
                                            VrCacheManager.getInstance(context).deleteAllVisits();
                                            VisitCacheManager.getInstance(context).deleteAllVisitStructure();
                                            FarmerCacheManager.getInstance(context).deleteAllFarmers();
                                            SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
                                            finishAndBackOnline();
                                        }
                                    }

                                } else {
                                    showProgress.hideProgressDialog();
                                    viewFailDialog.showDialog(context, "Opps!", response.body().getMsg());
                                }
                            } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                            } else {
                                try {
                                    error = response.errorBody().string();
                                    showProgress.hideProgressDialog();
                                    viewFailDialog.showDialog(context, "Opps!", "Something went wrong, Please try again later");
                                    Log.e(TAG, "Offline Cal Activity Err " + error);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                                notifyApiDelay(LandingActivity.this, response.raw().request().url().toString(),
                                        "" + diff, internetSPeed, error, response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                            showProgress.hideProgressDialog();
                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            isLoaded[0] = true;
                            notifyApiDelay(LandingActivity.this, call.request().url().toString(),
                                    "" + diff, internetSPeed, t.toString(), 0);
                            Log.e(TAG, "Offline Cal Activity Failure " + finalI + " " + t.toString());
                            viewFailDialog.showDialog(context, "Opps!", "Something went wrong, Please try again later");

                        }
                    });
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            internetFlow(isLoaded[0]);
                        }
                    }, AppConstants.DELAY);
                } else if (i == doneActivityResponseList.size() - 1) {
                    if (visitResponseList.size() > 0) {
                        uploadVisits();
                    } else {
                        showProgress.hideProgressDialog();
                        GpsCacheManager.getInstance(context).deleteAllGps(null);
                        PldCacheManager.getInstance(context).deleteAllFarms();
//                        CompRegCacheManager.getInstance(context).deleteAllData();
                        FarmCacheManager.getInstance(context).deleteAllFarms();
                        TimelineCacheManager.getInstance(context).deleteAllTimeline();
                        HarvestCacheManager.getInstance(context).deleteAllHarvests();
                        VrCacheManager.getInstance(context).deleteAllVisits();
                        VisitCacheManager.getInstance(context).deleteAllVisitStructure();
                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
                        finishAndBackOnline();
                    }
                }
            }

            return null;
        }
    }

    private class UploadOfflineVisitAsync extends AsyncTask<String, Integer, String> {
        List<VisitResponseNew> visitResponseList;

        public UploadOfflineVisitAsync(List<VisitResponseNew> visitResponseList) {
            this.visitResponseList = visitResponseList;
        }

        @Override
        protected String doInBackground(String... strings) {
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            for (int i = 0; i < visitResponseList.size(); i++) {
                if (!visitListT.get(i).isUploaded().equals("Y")) {
                    img = null;
                    String farmId = visitResponseList.get(i).getFarmId();
                    img = new String[visitResponseList.get(i).getVisitImages().size()];
                    for (int j = 0; j < visitResponseList.get(i).getVisitImages().size(); j++) {

                        if (coordinateHashMap.containsKey(farmId)) {
                            prepareMessages(visitResponseList.get(i).getVisitImages().get(j).getLat(),
                                    visitResponseList.get(i).getVisitImages().get(j).getLon(),
                                    coordinateHashMap.get(farmId));
                        } else {
                            message1 = "";
                            message2 = "";
                        }
                        prepareMessage2(visitResponseList.get(i).getVisitImages().get(j).getImgLink()
                                , visitResponseList.get(i).getDoa());

                        Bitmap bm = BitmapFactory.decodeFile(visitResponseList.get(i).getVisitImages().get(j).getImgLink());
                        if (bm != null) {
                            bm = addWaterMark(bm, SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_NAME),
                                    visitResponseList.get(i).getVisitImages().get(j).getLat(),
                                    visitResponseList.get(i).getVisitImages().get(j).getLon());
                            compressBitmapp(bm, 2, 20, j);
                        }
                        //img_list[j] = compressBitmap(bm, 2, 20, j);
                    }
                    visitResponseList.get(i).setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                    Log.e(TAG, "Offline Visit Upload " + new Gson().toJson(visitResponseList.get(i)));
                    visitResponseList.get(i).setImg_list(img);
                    final int finalI = i;

                    AddVisitSendDataNew addVisitSendData = new AddVisitSendDataNew();
                    addVisitSendData.setAgri_input_data(visitResponseList.get(i).getAgriInputList());

                    addVisitSendData.setFarm_id(Long.valueOf(visitResponseList.get(i).getFarmId()));
                    addVisitSendData.setComp_id(Long.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID)));
                    addVisitSendData.setApproved_method("images");
                    addVisitSendData.setComment(visitResponseList.get(i).getComment());
                    addVisitSendData.setMoisture(visitResponseList.get(i).getMoisture());
                    addVisitSendData.setVisit_date(visitResponseList.get(i).getDoa());
//                    addVisitSendData.setVisit_number(visitResponseList.get(i).nu);
//                    addVisitSendData.setEffective_area(visitResponseList.get(i).gest);
//                    addVisitSendData.setEffective_area2(area);
                    addVisitSendData.setAdded_by(Integer.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID)));
//        addVisitSendData.setFarm_grade(cpc.getName());
                    addVisitSendData.setFarm_grade(visitResponseList.get(i).getFarmGrade() + "");
                    addVisitSendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                    addVisitSendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));

                    Log.e(TAG, "Send Visit Data " + new Gson().toJson(addVisitSendData));
                    addVisitSendData.setImageList(img/*new String[]{}*/);//img_list
                    Log.e(TAG, "Image list size " + img.length);
                    final boolean[] isLoaded = {false};
                    long currMillis = AppConstants.getCurrentMills();
                    apiInterface.getVisitMsgStatusNew(addVisitSendData).enqueue(new Callback<StatusMsgModel>() {
                        @Override
                        public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                            String error = null;
                            isLoaded[0] = true;
                            if (response.isSuccessful()) {
                                Log.e(TAG, "Offline Visit res " + new Gson().toJson(response.body()));
                                if (response.body().getStatus() == 1) {
                                    Visit visit = visitListT.get(finalI);
                                    visit.setUploaded("Y");
                                    visitListT.get(finalI).setUploaded("Y");
                                    VrCacheManager.getInstance(context).update(visit);
                                    visitListT.set(finalI, visit);

                                    if (visitResponseList.get(finalI).getVisitImages() != null && visitResponseList.get(finalI).getVisitImages().size() > 0) {
                                        try {
                                            for (int k = 0; k < visitResponseList.get(finalI).getVisitImages().size(); k++) {
                                                deleteImage(visitResponseList.get(finalI).getVisitImages().get(k).getImgLink());
                                            }
                                        } catch (Exception e) {

                                        }
                                    }


                                    if (finalI == visitResponseList.size() - 1) {
                                        showProgress.hideProgressDialog();
                                        GpsCacheManager.getInstance(context).deleteAllGps(null);
                                        FarmerCacheManager.getInstance(context).deleteAllFarmers();
//                                        CompRegCacheManager.getInstance(context).deleteAllData();
                                        FarmCacheManager.getInstance(context).deleteAllFarms();
                                        TimelineCacheManager.getInstance(context).deleteAllTimeline();
                                        HarvestCacheManager.getInstance(context).deleteAllHarvests();
                                        VrCacheManager.getInstance(context).deleteAllVisits();
                                        PldCacheManager.getInstance(context).deleteAllFarms();
                                        VisitCacheManager.getInstance(context).deleteAllVisitStructure();
                                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
                                        finishAndBackOnline();
                                    }
                                } else {
                                    showProgress.hideProgressDialog();
                                    viewFailDialog.showDialog(context, "Opps!", response.body().getMsg());
                                }
                            } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                            } else {
                                try {
                                    error = response.errorBody().string();
                                    showProgress.hideProgressDialog();
                                    viewFailDialog.showDialog(context, "Opps!", "Something went wrong, Please try again later");
                                    Log.e(TAG, "Offline Visit Err " + error);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                                notifyApiDelay(LandingActivity.this, response.raw().request().url().toString(),
                                        "" + diff, internetSPeed, error, response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                            isLoaded[0] = true;
                            showProgress.hideProgressDialog();
                            long newMillis = AppConstants.getCurrentMills();
                            long diff = newMillis - currMillis;
                            notifyApiDelay(LandingActivity.this, call.request().url().toString(),
                                    "" + diff, internetSPeed, t.toString(), 0);
                            Log.e(TAG, "Offline Visit Failure " + finalI + " " + t.toString());
                            viewFailDialog.showDialog(context, "Opps!", "Something went wrong, Please try again later");
                        }
                    });
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            internetFlow(isLoaded[0]);
                        }
                    }, AppConstants.DELAY);
                } else if (i == visitResponseList.size() - 1) {
                    GpsCacheManager.getInstance(context).deleteAllGps(null);
                    PldCacheManager.getInstance(context).deleteAllFarms();
//                    CompRegCacheManager.getInstance(context).deleteAllData();
                    FarmCacheManager.getInstance(context).deleteAllFarms();
                    TimelineCacheManager.getInstance(context).deleteAllTimeline();
                    HarvestCacheManager.getInstance(context).deleteAllHarvests();
                    VrCacheManager.getInstance(context).deleteAllVisits();
                    VisitCacheManager.getInstance(context).deleteAllVisitStructure();
                    SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, false);
                    finishAndBackOnline();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }

    String[] img;

    public String[] compressBitmapp(Bitmap selectedBitmap, int sampleSize, int quality, int index) {
        try {
            ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStreamObject);
            //byteArrayOutputStreamObject.close();
            long lengthInKb = byteArrayOutputStreamObject.toByteArray().length / 1024; //in kb

            Log.e(TAG, "Image size at " + index + " " + lengthInKb + "Kb");
            if (lengthInKb > 400) {
                compressBitmapp(selectedBitmap, (sampleSize * 2), (quality / 2), index);
            } else {
                img[index] = Base64.encodeToString(byteArrayOutputStreamObject.toByteArray(), Base64.DEFAULT);
                Log.e(TAG, "Img Setting " + lengthInKb + "  " + img[index]);
                return img;
            }

            //selectedBitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }

    public String compressBitmappp(Bitmap selectedBitmap, int sampleSize, int quality) {
        try {
            ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStreamObject);
            //byteArrayOutputStreamObject.close();
            long lengthInKb = byteArrayOutputStreamObject.toByteArray().length / 1024; //in kb

            Log.e(TAG, "Image size at " + " " + lengthInKb + "Kb");
            if (lengthInKb > 400) {
                compressBitmappp(selectedBitmap, (sampleSize * 2), (quality / 2));
            } else {
                return Base64.encodeToString(byteArrayOutputStreamObject.toByteArray(), Base64.DEFAULT);
            }

            //selectedBitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*  private Bitmap addWaterMark(Bitmap src, String watermarkText,String latt,String lon) {
          int w = src.getWidth();
          int h = src.getHeight();
          String longitude = "0";
          String latitude = "0";
          if (latt != null&&lon!=null&&!TextUtils.isEmpty(latt)&&!TextUtils.isEmpty(lon)) {
              latitude = String.valueOf(latt);
              longitude = String.valueOf(lon);
              ;
          }
          Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
          Canvas canvas = new Canvas(result);
          canvas.drawBitmap(src, 0, 0, null);
          Paint addedBy = new Paint();
          //apply color
          addedBy.setColor(resources.getColor(R.color.white));
          addedBy.setTextSize(50);
          addedBy.setAntiAlias(true);
          addedBy.setUnderlineText(true);
          int y = canvas.getHeight() - 200;
          canvas.drawText("Clicked By: " + watermarkText, 20, y, addedBy);
          Paint lat = new Paint();
          //apply color
          lat.setColor(resources.getColor(R.color.white));
          lat.setTextSize(50);
          lat.setAntiAlias(true);
          lat.setUnderlineText(true);
          int y2 = canvas.getHeight() - 125;
          lat.setColorFilter(new ColorFilter());
          canvas.drawText("Latitude: " + latitude, 20, y2, lat);

          Paint lng = new Paint();
          //apply color
          lng.setColor(resources.getColor(R.color.white));
          lng.setTextSize(50);
          lng.setAntiAlias(true);
          lng.setUnderlineText(true);
          int y3 = canvas.getHeight() - 50;
          canvas.drawText("Longitude: " + longitude, 20, y3, lng);
          if (message1 != null && !TextUtils.isEmpty(message1) && message2 != null && !TextUtils.isEmpty(message2)) {
              String mText = message1;
              Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
              paint.setColor(getResources().getColor(R.color.red));
              paint.setTextSize((int) (50));
              paint.setTextAlign(Paint.Align.CENTER);
              Rect bounds = new Rect();
              paint.getTextBounds(mText, 0, mText.length(), bounds);
              canvas.drawText(mText, src.getWidth() / 2, y, paint);

              String mText2 = message2;
              Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
              paint2.setColor(getResources().getColor(R.color.red));
              paint2.setTextSize((int) (50));
              paint2.setTextAlign(Paint.Align.CENTER);
              Rect bounds2 = new Rect();
              paint2.getTextBounds(mText2, 0, mText2.length(), bounds2);
              canvas.drawText(mText2, src.getWidth() / 2, y2, paint2);

          }
          if (message3!=null&&!TextUtils.isEmpty(message3)){
              String mText2 = message3;
              Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
              paint2.setColor(getResources().getColor(R.color.red));
              paint2.setTextSize((int) (50));
              paint2.setTextAlign(Paint.Align.CENTER);
              Rect bounds2 = new Rect();
              paint2.getTextBounds(mText2, 0, mText2.length(), bounds2);
              canvas.drawText(mText2, src.getWidth() / 2, y3, paint2);
          }

          Bitmap waterMark = BitmapFactory.decodeResource(context.getResources(), R.drawable.ct_water_mark);

          Log.e(TAG, "Canvas" + canvas.getWidth() + " " + canvas.getHeight());
          if (canvas.getWidth() > canvas.getHeight()) {
              canvas.drawBitmap(waterMark, (9 * (canvas.getWidth() / 10) - 50), (9 * (canvas.getHeight() / 10) - 200), null);
              Log.e(TAG, "CanvasW" + (8 * (canvas.getWidth() / 10) - 100) + " " + (9 * (canvas.getHeight() / 10) - 200));

          } else {
              canvas.drawBitmap(waterMark, (8 * (canvas.getWidth() / 10) - 50), (9 * (canvas.getHeight() / 10) - 90), null);
              Log.e(TAG, "CanvasH" + (8 * (canvas.getWidth() / 10) - 50) + " " + (9 * (canvas.getHeight() / 10) - 90));
          }

          return result;
      }*/

    private int checkSize(Paint addedBy, Rect textRec, int imageWidth, int textSize, String watermarkText) {

        while (textRec.width() >= (imageWidth - 4) / 3) {
            addedBy.getTextBounds(watermarkText, 0, watermarkText.length(), textRec);
            textSize--;
            addedBy.setTextSize(textSize);

            Log.e("checkSize", "TextRect: " + textRec.width() + ", ImageWidth: " + (imageWidth - 4) / 3);
            Log.e("checkSize", "Textsize " + textSize);
        }

        return textSize;

    }

    public Bitmap addWatermark(Bitmap source) {
        int w, h;
        Canvas c;
        Paint paint;
        Bitmap bmp, watermark;
        Matrix matrix;
        float scale;
        RectF r;
        w = source.getWidth();
        h = source.getHeight();
        // Create the new bitmap
        bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
        // Copy the original bitmap into the new one
        c = new Canvas(bmp);
        c.drawBitmap(source, 0, 0, paint);
        // Load the watermark
        watermark = BitmapFactory.decodeResource(resources, R.drawable.ct_water_mark);
        // Scale the watermark to be approximately 40% of the source image height
        scale = (float) (((float) h * 0.15) / (float) watermark.getHeight());
        // Create the matrix
        matrix = new Matrix();
        matrix.postScale(scale, scale);
        // Determine the post-scaled size of the watermark
        r = new RectF(0, 0, watermark.getWidth(), watermark.getHeight());
        matrix.mapRect(r);
        // Move the watermark to the bottom right corner
        matrix.postTranslate(w - r.width(), h - r.height());
        // Draw the watermark
        c.drawBitmap(watermark, matrix, paint);
        // Free up the bitmap memory
        watermark.recycle();
        return bmp;
    }

    private Bitmap addWaterMark(Bitmap src, String watermarkText, String latt, String lon) {
        String userName = "Clicked by: " + watermarkText;
        Bitmap pikedPhoto = src.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(pikedPhoto);
        Paint paintCenter = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
        paintCenter.setStyle(Paint.Style.FILL);
        paintCenter.setColor(Color.RED);
        //float size = convertDpToPx(context, 25);
        paintCenter.setTextAlign(Paint.Align.CENTER);
        float textY = pikedPhoto.getHeight();
        float textX = pikedPhoto.getWidth() / 2;
        String text1 = "Image might not be from farm";
        Rect textRect = new Rect();
       /* if (message1 != null && !TextUtils.isEmpty(message1)) {
            text1 = message1;
        } else if (message2 != null && !TextUtils.isEmpty(message2)) {
            text1 = message2;
        } else if (message3 != null && !TextUtils.isEmpty(message3)) {
            text1 = message3;
        }*/
        paintCenter.getTextBounds(text1, 0, text1.length(), textRect);
        int s = checkSize(paintCenter, textRect, (canvas.getWidth() - 4), 50, text1);
        paintCenter.setTextSize(s);
        canvas.drawText(message1, textX + 5, textY - 2 * s - 5, paintCenter);
        canvas.drawText(message2, textX + 5, textY - s - 5, paintCenter);
        canvas.drawText(message3, textX + 5, textY - 5, paintCenter);

        Paint paintLeft = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
        paintLeft.setStyle(Paint.Style.FILL);
        paintLeft.setColor(Color.WHITE);
        //float sizeLeft = convertDpToPx(context, 25);
        paintLeft.setTextSize(s); //Text Size
        paintLeft.setTextAlign(Paint.Align.LEFT);
        float textYL = pikedPhoto.getHeight();

        if (text1 == null || TextUtils.isEmpty(text1)) {
            Rect textRectL = new Rect();
            paintLeft.getTextBounds(userName, 0, userName.length(), textRectL);
            int sL = checkSize(paintLeft, textRectL, (canvas.getWidth() - 4), 50, userName);
            s = sL;
        }
        paintLeft.setTextSize(s);
        canvas.drawText(userName, 10, textYL - 2 * s - 5, paintLeft);
        canvas.drawText("Lat: " + latt, 10, textYL - s - 5, paintLeft);
        canvas.drawText("Long: " + lon, 10, textYL - 5, paintLeft);
        // in this case, center.x and center.y represent the coordinates of the center of the rectangle in which the text is being placed
        pikedPhoto = addWatermark(pikedPhoto);
        return pikedPhoto;
    }

    private void finishAndBackOnline() {


        new DeleteImageAsync().execute();


    }


    private class DeleteImageAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.e(TAG, "imagePathListToDelete size " + imagePathListToDelete.size());
            for (int i = 0; i < imagePathListToDelete.size(); i++) {
                deleteImage(imagePathListToDelete.get(i));
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
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
            });


            return null;
        }
    }


//    private void storeCompRegData(List<CompRegModel> compRegModels) {
//        progressDialog.show();
//        Date date = Calendar.getInstance().getTime();
//        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
//        final String curdate = output.format(date);
//        if (connected) {
//            if (compRegModels != null && compRegModels.size() > 0) {
//                if (!compRegModels.get(compRegModels.size() - 1).getDate().equals(curdate)) {
//
//                    ApiInterface expApiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
//                    Call<ResponseCompReg> compRegCall = expApiInterface.getCompRegData(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID),
//                            SharedPreferencesMethod.getString(context,SharedPreferencesMethod.PERSON_ID));
//
//                    compRegCall.enqueue(new Callback<ResponseCompReg>() {
//                        @Override
//                        public void onResponse(Call<ResponseCompReg> call, Response<ResponseCompReg> response) {
//                            if (response.isSuccessful()) {
//                                ResponseCompReg responseCompReg = response.body();
//                                List<CompRegResult> compRegResultList = responseCompReg.getResult();
//                                List<CompRegModel> regModelList = new ArrayList<>();
//                                // compRegCacheManager.deleteAllData(SplashActivity.this);
//                                if (compRegResultList != null && compRegResultList.size() > 0) {
//                                    progressDialog.cancel();
//                                    compRegCacheManager.deleteAllData();
//                                    Log.e(TAG, "Comp Reg Res  " + new Gson().toJson(responseCompReg));
//                                    for (int i = 0; i < compRegResultList.size(); i++) {
//                                        String id = compRegResultList.get(i).getCompId();
//                                        String parameter = compRegResultList.get(i).getParameter();
//                                        String value = compRegResultList.get(i).getValue();
//                                        String date = curdate;
//                                        String clss = compRegResultList.get(i).getClss();
//                                        Log.e("CompReg", clss + " " + id + " " + value);
//                                        CompRegModel compRegModel = new CompRegModel(id, parameter, value, date, clss);
//                                        regModelList.add(compRegModel);
//                                        compRegCacheManager.addCompRegData(LandingActivity.this, id, parameter, value, date, clss);
//                                    }
//                                    checkCompRegDataAndVisibleFeatures(regModelList);
//                                } else {
//                                    progressDialog.cancel();
//                                }
//                                //stop();
//                            } else {
//                                Log.e(TAG, "CompReg ERr" + response.errorBody().toString());
//                                progressDialog.cancel();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseCompReg> call, Throwable t) {
//                            Log.e(TAG, "Comp Reg Failure " + t.toString());
//                            progressDialog.cancel();
//                        }
//                    });
//                }
//            } else {
//                ApiInterface expApiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
//                Call<ResponseCompReg> compRegCall = expApiInterface.getCompRegData(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID),
//                        SharedPreferencesMethod.getString(context,SharedPreferencesMethod.PERSON_ID));
//                compRegCall.enqueue(new Callback<ResponseCompReg>() {
//                    @Override
//                    public void onResponse(Call<ResponseCompReg> call, Response<ResponseCompReg> response) {
//                        if (response.isSuccessful()) {
//                            ResponseCompReg responseCompReg = response.body();
//                            List<CompRegResult> compRegResultList = responseCompReg.getResult();
//                            List<CompRegModel> regModelList = new ArrayList<>();
//                            // compRegCacheManager.deleteAllData(SplashActivity.this);
//                            if (compRegResultList != null && compRegResultList.size() > 0) {
//                                progressDialog.cancel();
//                                for (int i = 0; i < compRegResultList.size(); i++) {
//                                    String id = compRegResultList.get(i).getCompId();
//                                    String clss = compRegResultList.get(i).getClss();
//                                    String parameter = compRegResultList.get(i).getParameter();
//                                    String value = compRegResultList.get(i).getValue();
//                                    String date = curdate;
//                                    Log.e("CompReg", clss + " " + id + " " + value);
//                                    CompRegModel compRegModel = new CompRegModel(id, parameter, value, date, clss);
//                                    regModelList.add(compRegModel);
//                                    compRegCacheManager.addCompRegData(LandingActivity.this, id, parameter, value, date, clss);
//                                }
//                                checkCompRegDataAndVisibleFeatures(regModelList);
//                            } else {
//                                progressDialog.cancel();
//                            }
//                            //stop();
//                        } else {
//                            Log.e(TAG, "CompReg Failure " + response.errorBody().toString());
//                            progressDialog.cancel();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseCompReg> call, Throwable t) {
//                        Log.e("RFerror", t.toString());
//                        progressDialog.cancel();
//                    }
//                });
//
//
//            }
//
//
//        }
//        if (progressDialog.isShowing())
//            progressDialog.cancel();
//    }

    int totalParamCount = 0;

    private void updateCache(String todayDate) {
        loaderCacheLoad.setVisibility(View.VISIBLE);
        Log.e(TAG, "Updating Cache");
        if (role == AppConstants.ROLES.SUPERVISOR) {
            getFarmerList();
        } else {
            getClusterOfCompany();
            getFarmerList();
        }
        getCompParams();
        getFormData();
        getAllCompData();
        getAllData(todayDate);
        getSuperVisors();
        List<String> paramTypes = new ArrayList<>();
        paramTypes.add(AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE_NEW);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE_NEW);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE_NEW);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.CIVIL_STATUS);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.CROPPING_PATTERN);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.PLANTING_METHOD);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.LAND_CATEGORY);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.CASTE_CATEGORY);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.CASTE);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.RELIGION);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.MOBILE_NETWORK_OPERATOR);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.MOTOR_HP);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.MICRO_IRRI_AWARENESS);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.DATE_FORMAT);
        paramTypes.add(AppConstants.CHEMICAL_UNIT.DELINQUENT);
        totalParamCount = paramTypes.size();
        for (int i = 0; i < paramTypes.size(); i++) {
            getCompanyParams(i, paramTypes.get(i));

        }


    }

    private void elseCondition() {
//        getSuperVisors();

        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getSuperVisors();
                }
            }
        }, AppConstants.CHEMICAL_UNIT.SUPERVISOR_LIST);

        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompAgriInputs();
                }
            }
        }, AppConstants.CHEMICAL_UNIT.COMP_AGRI_INPUTS);

        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompanyParams(0, AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE_NEW);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE_NEW);

        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompanyParams(0, AppConstants.CHEMICAL_UNIT.DATE_FORMAT);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.DATE_FORMAT);

        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompanyParams(0, AppConstants.CHEMICAL_UNIT.CASTE_CATEGORY);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.CASTE_CATEGORY);

        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompanyParams(0, AppConstants.CHEMICAL_UNIT.CASTE);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.CASTE);

        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompanyParams(0, AppConstants.CHEMICAL_UNIT.RELIGION);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.RELIGION);

        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompanyParams(0, AppConstants.CHEMICAL_UNIT.MOBILE_NETWORK_OPERATOR);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.MOBILE_NETWORK_OPERATOR);

        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompanyParams(0, AppConstants.CHEMICAL_UNIT.MOTOR_HP);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.MOTOR_HP);

        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompanyParams(0, AppConstants.CHEMICAL_UNIT.MICRO_IRRI_AWARENESS);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.MICRO_IRRI_AWARENESS);

        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompanyParams(0, AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE_NEW);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE_NEW);
        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompanyParams(0, AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE_NEW);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE_NEW);
        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompanyParams(0, AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.CROP_LIST_NEW);
        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompanyParams(0, AppConstants.CHEMICAL_UNIT.CROPPING_PATTERN);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.CROPPING_PATTERN);
        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompanyParams(0, AppConstants.CHEMICAL_UNIT.PLANTING_METHOD);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.PLANTING_METHOD);
        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompanyParams(0, AppConstants.CHEMICAL_UNIT.LAND_CATEGORY);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.LAND_CATEGORY);
        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompanyParams(0, AppConstants.CHEMICAL_UNIT.CIVIL_STATUS);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.CIVIL_STATUS);

        DropDownCacheManager2.getInstance(context).getDataByType(new DropDownCacheManager2.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT2 dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getCompanyParams(0, AppConstants.CHEMICAL_UNIT.DELINQUENT);
                }
            }
        }, AppConstants.CHEMICAL_UNIT.DELINQUENT);

        DropDownCacheManager.getInstance(context).getDataByType(new DropDownCacheManager.OnFarmerClusterFetchListener() {
            @Override
            public void onDataLoaded(DropDownT dropDown) {
                if (dropDown == null || !AppConstants.isValidString(dropDown.getData()) || dropDown.getData().trim().equals("{}")) {
                    getFormData();
                }
//                    getFormData();
            }
        }, AppConstants.CHEMICAL_UNIT.FARM_ADD_DYNAMIC);

    }

    List<PldReason> pldReasons = new ArrayList<>();
    List<Cluster> clusterList = new ArrayList<>();
    List<FarmerSpinnerData> farmerDataList = new ArrayList<>();


    protected void getPldReasons() {
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getPldReasonList(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID)).enqueue(new Callback<PldReasonResponse>() {
            @Override
            public void onResponse(Call<PldReasonResponse> call, Response<PldReasonResponse> response) {
                String error = null;
                isLoaded[0] = true;
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this);
                    } else if (response.body().getStatus() == 1 && response.body().getPldReasonList() != null && response.body().getPldReasonList().size() > 0) {

                        PldReason pldReason = new PldReason();
                        pldReason.setName("Select Loss/Damage Reason");
                        pldReasons.add(pldReason);
                        pldReasons.addAll(response.body().getPldReasonList());
                        DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.PLD_REASON);
                        DropDownT dropDownPld = new DropDownT(AppConstants.CHEMICAL_UNIT.PLD_REASON, new Gson().toJson(pldReasons));
                        DropDownCacheManager.getInstance(context).addChemicalUnit(dropDownPld);

                    } else {
                        DropDownT dropDownPld = new DropDownT(AppConstants.CHEMICAL_UNIT.PLD_REASON, new Gson().toJson(pldReasons));
                        DropDownCacheManager.getInstance(context).addChemicalUnit(dropDownPld);
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string();
                        Log.e(TAG, "Reason Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<PldReasonResponse> call, Throwable t) {

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

    protected void getCompanyParams(int index, String type) {
        ParamData paramData = new ParamData();
        paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        paramData.setType(type);
        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        Log.e(TAG, "Sending PARA , " + new Gson().toJson(paramData));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getNewParameters(paramData).enqueue(new Callback<DDResponseNew>() {
            @Override
            public void onResponse(Call<DDResponseNew> call, Response<DDResponseNew> response1) {
                Log.e(TAG + "L", "Sending PARA , " + type + "  " + response1.code());
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    DDResponseNew response = response1.body();

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
//                            DropDownCacheManager2.getInstance(context).deleteViaType(type);
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
                        Log.e(TAG + "L", "Cache Error " + error + " " + type);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (index == totalParamCount - 1) {
                    loaderCacheLoad.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<DDResponseNew> call, Throwable t) {
                Log.e(TAG + "L", "Failure getOfflineData " + t.toString());
//                    getAllDataNew2(AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE_NEW);

                if (index == totalParamCount - 1) {
                    loaderCacheLoad.setVisibility(View.GONE);
                }

            }
        });


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
                        Log.e(TAG, "1 Cache Error " + error + " Code " + response1.code() + " ");
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

    private void getAllDataDate(final String type) {
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
                        Log.e(TAG, "2 Cache Error " + error + " Code " + response1.code());
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

    private void getCasteCategoyParam(final String type) {
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
                        Log.e(TAG, "2 Cache Error " + error + " Code " + response1.code());
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

    private void getCasteParam(final String type) {
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
                        Log.e(TAG, "2 Cache Error " + error + " Code " + response1.code());
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

    private void getRelegionParam(final String type) {
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
                        Log.e(TAG, "2 Cache Error " + error + " Code " + response1.code());
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

    private void getMobileNetworkParam(final String type) {
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
                        Log.e(TAG, "2 Cache Error " + error + " Code " + response1.code());
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

    private void getMotorHpParam(final String type) {
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
                        Log.e(TAG, "2 Cache Error " + error + " Code " + response1.code());
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

    private void getMicroIrriAwarenessParam(final String type) {
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
                        Log.e(TAG, "2 Cache Error " + error + " Code " + response1.code());
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
                        Log.e(TAG, "3 Cache Error " + error + " Code " + response1.code());
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
                        Log.e(TAG, "4 Cache Error " + error + " Code " + response1.code());
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
                        Log.e(TAG, "5 Cache Error " + error + " Code " + response1.code());
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
                        Log.e(TAG, "6 Cache Error " + error + " Code " + response1.code());
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
                        Log.e(TAG, "7 Cache Error " + error + " Code " + response1.code());
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
                        Log.e(TAG, "8 Cache Error " + error + " Code " + response1.code());
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
                        Log.e(TAG, "9 Cache Error " + error + " Code " + response1.code());
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

    private void getClusterOfCompany() {
        clusterList.clear();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getClusters(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN),
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID)).enqueue(new Callback<ClusterResponse>() {
            @Override
            public void onResponse(Call<ClusterResponse> call, Response<ClusterResponse> response) {
                String error = null;
                isLoaded[0] = true;
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        Cluster clusterr = new Cluster();
                        clusterr.setClusterName("All");
                        clusterr.setClusterId("0");
                        clusterList.add(clusterr);
                        if (response.body().getClusterList() != null) {
                            clusterList.addAll(response.body().getClusterList());
                        }
                        DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST);
                        DropDownT clusters = new DropDownT(AppConstants.CHEMICAL_UNIT.CLUSTER_LIST, new Gson().toJson(clusterList));
                        DropDownCacheManager.getInstance(context).addChemicalUnit(clusters);
                        setClusterData(clusterList);
                    } else if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this);
                        //progressDialog.cancel();
                    } else {
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        //progressDialog.cancel();
                        error = response.errorBody().string();
                        Log.e(TAG, "Cluster Err " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<ClusterResponse> call, Throwable t) {
                Log.e(TAG, "Cluster fail " + t.toString());
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


    private void getCompAgriInputs() {
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getCompAgriInputs(compId, userId, token).enqueue(new Callback<CompAgriResponse>() {
            @Override
            public void onResponse(Call<CompAgriResponse> call, Response<CompAgriResponse> response) {
                String error = null;
                if (response.isSuccessful()) {
                    try {
                        CompAgriResponse data = response.body();
                        if (data != null && data.getData() != null) {

                            DropDownCacheManager2.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.COMP_AGRI_INPUTS);
                            DropDownT2 clusters = new DropDownT2(AppConstants.CHEMICAL_UNIT.COMP_AGRI_INPUTS, new Gson().toJson(data));
                            DropDownCacheManager2.getInstance(context).addChemicalUnit(clusters);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "CompAgri Err " + error + " code" + response.code());
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialog(LandingActivity.this, resources.getString(R.string.failed_load_farm));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
                    notifyApiDelay(LandingActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<CompAgriResponse> call, Throwable t) {
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(LandingActivity.this, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "CompAgri fail " + t.toString());
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialog(LandingActivity.this, resources.getString(R.string.failed_load_farm));
            }
        });
        internetFlow(isLoaded[0]);

    }

    private void getSuperVisors() {
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String clusterId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID);
//        final boolean[] isLoaded = {false};

        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("",""+);
        jsonObject.addProperty("comp_id", "" + compId);
        jsonObject.addProperty("user_id", "" + userId);
        jsonObject.addProperty("token", "" + token);

        apiInterface.getCompSupervisors(null, compId, userId, token).enqueue(new Callback<SupervisorListResponse>() {
            @Override
            public void onResponse(Call<SupervisorListResponse> call, Response<SupervisorListResponse> response) {
//                String error = null;
                Log.e(TAG, "getCompSupervisors code " + response.code());
                if (response.isSuccessful()) {
                    try {
                        Log.e(TAG, "getCompSupervisors res " + new Gson().toJson(response.body()));
                        SupervisorListResponse data = response.body();
                        if (data != null && data.getData() != null) {

                            DropDownCacheManager2.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.SUPERVISOR_LIST);
                            DropDownT2 clusters = new DropDownT2(AppConstants.CHEMICAL_UNIT.SUPERVISOR_LIST, new Gson().toJson(data));
                            DropDownCacheManager2.getInstance(context).addChemicalUnit(clusters);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
//                    ViewFailDialog viewFailDialog = new ViewFailDialog();
//                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {

                        Log.e(TAG, "getCompSupervisors Err " + response.errorBody().string().toString() + " code " + response.code());
//                        ViewFailDialog viewFailDialog = new ViewFailDialog();
//                        viewFailDialog.showDialog(LandingActivity.this, resources.getString(R.string.failed_load_farm));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

//                long newMillis = AppConstants.getCurrentMills();
//                long diff = newMillis - currMillis;
//                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() != 200) {
//                    notifyApiDelay(LandingActivity.this, response.raw().request().url().toString(),
//                            "" + diff, internetSPeed, error, response.code());
//                }
            }

            @Override
            public void onFailure(Call<SupervisorListResponse> call, Throwable t) {
//                long newMillis = AppConstants.getCurrentMills();
//                long diff = newMillis - currMillis;
//                notifyApiDelay(LandingActivity.this, call.request().url().toString(),
//                        "" + diff, internetSPeed, t.toString(), 0);
                Log.e(TAG, "getCompSupervisors fail " + t.toString());
//                ViewFailDialog viewFailDialog = new ViewFailDialog();
//                viewFailDialog.showDialog(LandingActivity.this, resources.getString(R.string.failed_load_farm));
            }
        });
//        internetFlow(isLoaded[0]);

    }

    private void getFarmerList() {
        farmerDataList.clear();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String clusterId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVCLUSTERID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getFarmerList(compId, clusterId, userId, token).enqueue(new Callback<FetchFarmerResponse>() {
            @Override
            public void onResponse(Call<FetchFarmerResponse> call, Response<FetchFarmerResponse> response) {
                String error = null;
                isLoaded[0] = true;
                if (response.isSuccessful()) {
                    Log.e(TAG, "Farmer List Res " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.multiple_login_msg));
                    } else if (response.body().getStatus() == 1) {
                        if (response.body().getFarmerDataList() != null) {
                            farmerDataList.addAll(response.body().getFarmerDataList());
                        }
                        DropDownCacheManager.getInstance(context).deleteViaType(AppConstants.CHEMICAL_UNIT.FARMER_LIST);
                        DropDownT farmers = new DropDownT(AppConstants.CHEMICAL_UNIT.FARMER_LIST, new Gson().toJson(farmerDataList));
                        DropDownCacheManager.getInstance(context).addChemicalUnit(farmers);
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "Farmer List Err " + error + " code" + response.code());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<FetchFarmerResponse> call, Throwable t) {

                Log.e(TAG, "Farmer List failure " + t.toString());
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

    private void getCompParams() {
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
                        Log.e(TAG, "CompParam rError " + response.errorBody().string());
                        SharedPreferencesMethod.setFloatPref(context, SharedPreferencesMethod.COMP_AUTO_ACCURACY, 2.0f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                getCompAgriInputs();
            }

            @Override
            public void onFailure(Call<CompParamResponse> call, Throwable t) {
                Log.e(TAG, "CompParam Fail " + t.toString());
                SharedPreferencesMethod.setFloatPref(context, SharedPreferencesMethod.COMP_AUTO_ACCURACY, 2.0f);
                getCompAgriInputs();
            }
        });
    }

    private void getAllData(String todayDate) {
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        FarmIdArray idArray = new FarmIdArray();
        idArray.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        idArray.setFarmIdList(new ArrayList<>());
        idArray.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        idArray.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        Log.e("OfflineModeAct", "Sending Data " + new Gson().toJson(idArray));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getOfflineData(idArray).enqueue(new Callback<OfflineResponse>() {
            @Override
            public void onResponse(Call<OfflineResponse> call, Response<OfflineResponse> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    OfflineResponse response = response1.body();
                    Log.e(TAG, "Response Make " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, response.getMsg());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {

                        //Insert Chemicals and units
                        DropDownT units = new DropDownT(AppConstants.CHEMICAL_UNIT.UNIT, new Gson().toJson(response.getUnitsList()));
                        DropDownCacheManager.getInstance(context).addChemicalUnit(units);

                        DropDownT compUnits = new DropDownT(AppConstants.CHEMICAL_UNIT.COMPANY_UNITS, new Gson().toJson(response.getTimelineUnitsList()));
                        DropDownCacheManager.getInstance(context).addChemicalUnit(compUnits);

                        DropDownT compChemicals = new DropDownT(AppConstants.CHEMICAL_UNIT.COMPANY_CHEMICALS, new Gson().toJson(response.getTimelineChemicalsList()));
                        DropDownCacheManager.getInstance(context).addChemicalUnit(compChemicals);


                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.CACHE_DATE, todayDate);

                    }
                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "10 Cache Error " + error + " Code " + response1.code());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<OfflineResponse> call, Throwable t) {
                Log.e(TAG, "10 Failure getOfflineData " + t.toString());
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

    private void getFormData() {
        ParamData paramData = new ParamData();
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        paramData.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        paramData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        paramData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        Log.e(TAG, "Sending New Param i  AND type  " + new Gson().toJson(paramData));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getFarmCropForm(paramData).enqueue(new Callback<CropFormResponse>() {
            @Override
            public void onResponse(Call<CropFormResponse> call, Response<CropFormResponse> response1) {
                String error = null;
                isLoaded[0] = true;
                if (response1.isSuccessful()) {
                    CropFormResponse response = response1.body();
                    Log.e(TAG, "Response Make  " + AppConstants.CHEMICAL_UNIT.FARM_ADD_DYNAMIC + " " + new Gson().toJson(response));
                    if (response.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, response.getMessage());
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
                        Log.e(TAG, "12 Cache Error " + error + " Code " + response1.code());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CropFormResponse> call, Throwable t) {
                Log.e(TAG, "12 Failure getOfflineData " + t.toString());
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

    List<ExpenseDataDD> expenseDataDDList = new ArrayList<>();
    List<ResourceDataDD> resourceDataDDList = new ArrayList<>();
    List<IrrigationSource> irrigationSourceList = new ArrayList<>();
    List<IrrigationType> irrigationTypeList = new ArrayList<>();
    List<SoilType> soilTypeList = new ArrayList<>();
    List<FarmCrop> cropList = new ArrayList<>();

    List<Season> seasonList = new ArrayList<>();

    private void insertRoomFarmsNew() {
        DropDownT expenseDD = new DropDownT(AppConstants.CHEMICAL_UNIT.INPUT_COST, new Gson().toJson(expenseDataDDList));
        DropDownCacheManager.getInstance(context).addChemicalUnit(expenseDD);
        DropDownT resourceDD = new DropDownT(AppConstants.CHEMICAL_UNIT.RESOURCE_USED, new Gson().toJson(resourceDataDDList));
        DropDownCacheManager.getInstance(context).addChemicalUnit(resourceDD);
//        DropDownT irriSourceDD = new DropDownT(AppConstants.CHEMICAL_UNIT.FARM_IRRI_SOURCE, new Gson().toJson(irrigationSourceList));
//        DropDownCacheManager.getInstance(context).addChemicalUnit(irriSourceDD);
//        DropDownT irriTypeDD = new DropDownT(AppConstants.CHEMICAL_UNIT.FARM_IRRI_TYPE, new Gson().toJson(irrigationTypeList));
//        DropDownCacheManager.getInstance(context).addChemicalUnit(irriTypeDD);
//        DropDownT soilTypeDD = new DropDownT(AppConstants.CHEMICAL_UNIT.FARM_SOIL_TYPE, new Gson().toJson(soilTypeList));
//        DropDownCacheManager.getInstance(context).addChemicalUnit(soilTypeDD);
        DropDownT seasonDD = new DropDownT(AppConstants.CHEMICAL_UNIT.FARM_SEASON, new Gson().toJson(seasonList));
        DropDownCacheManager.getInstance(context).addChemicalUnit(seasonDD);
//        DropDownT cropDD = new DropDownT(AppConstants.CHEMICAL_UNIT.CROP_LIST, new Gson().toJson(cropList));
//        DropDownCacheManager.getInstance(context).addChemicalUnit(cropDD);

    }

    private void getAllCompData() {
        try {
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);
            FetchFarmSendData fetchFarmSendData = new FetchFarmSendData();
            fetchFarmSendData.setComp_id(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
            fetchFarmSendData.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            fetchFarmSendData.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            Log.e("OfflineActivity", "Credentials " + new Gson().toJson(fetchFarmSendData));
            Call<SeasonResponse> fetchFarmDataCall = apiService.getSeasons(fetchFarmSendData);
            fetchFarmDataCall.enqueue(new Callback<SeasonResponse>() {
                @Override
                public void onResponse(Call<SeasonResponse> call, Response<SeasonResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            Log.e(TAG + "L", "COMP data " + new Gson().toJson(response.body()));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                        progressDoalog.cancel();
//                                    loader.setVisibility(View.GONE);
                                }
                            });
                            if (response != null) {
                                SeasonResponse fetchFarmData = response.body();
                                if (response.body().getStatus() == 10) {
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, response.body().getMsg());
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                    //un authorized access
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                    // auth token expired
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                                } else {
//                                    if (fetchFarmData.getExpenseDataDDList() != null)
//                                        expenseDataDDList.addAll(fetchFarmData.getExpenseDataDDList());
//                                    if (fetchFarmData.getResourceDataDDList() != null)
//                                        resourceDataDDList.addAll(fetchFarmData.getResourceDataDDList());
//                                    if (fetchFarmData.getIrrigationSource() != null)
//                                        irrigationSourceList.addAll(fetchFarmData.getIrrigationSource());
//                                    if (fetchFarmData.getIrrigationType() != null)
//                                        irrigationTypeList.addAll(fetchFarmData.getIrrigationType());
//                                    if (fetchFarmData.getSoilType() != null)
//                                        soilTypeList.addAll(fetchFarmData.getSoilType());
                                    if (fetchFarmData.getData() != null)
                                        seasonList.addAll(fetchFarmData.getData());
//                                    if (fetchFarmData.getCropList() != null)
//                                        cropList.addAll(fetchFarmData.getCropList());

                                    insertRoomFarmsNew();
                                }
                            } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                            } else {
                                Log.e(TAG + "L", "COMP data err " + response.errorBody().string());
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

//                    getAllData();
                }

                @Override
                public void onFailure(Call<SeasonResponse> call, Throwable t) {
                    Log.e(TAG + "L", "COMP data fail " + t.toString());
//                    getAllData();
                }
            });
        } catch (Exception r) {
            r.printStackTrace();
        }
    }


    //add farm, add visit, add activity, add pld, soil health card, input and resource


    //===========Offline cache all data of selected farms==============

    Map<String, List<LatLngFarm>> coordinateHashMapToStore = new HashMap<>();
    int totalSelectedFarms = 0;
    List<FetchFarmResultNew> selectedFarmList = new ArrayList<>();

    // Get selected farm id list from all the farms
    private void getFarmIdList() {
        offlineModeImg.setClickable(false);
        offlineModeImg.setEnabled(false);
//        loader.setVisibility(View.VISIBLE);
        List<String> farmIdList = new ArrayList<>();

        selectedFarmList = new ArrayList<>();
        for (int i = 0; i < fetchFarmResultVettingFilter.size(); i++) {
            if (fetchFarmResultVettingFilter.get(i).getFetchFarmResultNew().isSelectedd()) {
                farmIdList.add(fetchFarmResultVettingFilter.get(i).getFetchFarmResultNew().getFarmMasterNum());
                selectedFarmList.add(fetchFarmResultVettingFilter.get(i).getFetchFarmResultNew());
            } else {
            }
        }
        totalSelectedFarms = farmIdList.size();
        getFarmCoordinates(farmIdList);
    }

    private void getFarmCoordinates(final List<String> farmIdList) {
        coordinateHashMapToStore.clear();
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        FarmIdArray idArray = new FarmIdArray();
        idArray.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
        idArray.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        idArray.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        idArray.setFarmIdList(farmIdList);
        Log.e(TAG, "SENDING FARM IDS " + new Gson().toJson(idArray));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getAllCoordinatesOfFarms(idArray).enqueue(new Callback<FarmsCoordinatesResponse>() {
            @Override
            public void onResponse(Call<FarmsCoordinatesResponse> call, Response<FarmsCoordinatesResponse> response) {
                String error = null;
                isLoaded[0] = true;
                Log.e(TAG, "getAllCoordinatesOfFarms " + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG, "getAllCoordinatesOfFarms res " + new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 1) {
                        if (response.body().getCoordinatesList() != null && response.body().getCoordinatesList().size() > 0) {
                            for (int i = 0; i < response.body().getCoordinatesList().size(); i++) {
                                Log.e(TAG, "FarmLandingActivity3 " + response.body().getCoordinatesList().get(i).getFarmId() + " Data " + new Gson().toJson(response.body().getCoordinatesList().get(i).getData()));
                                if (response.body().getCoordinatesList().get(i).getData() != null && response.body().getCoordinatesList().get(i).getData().size() > 0) {

                                    coordinateHashMapToStore.put(response.body().getCoordinatesList().get(i).getFarmId(),
                                            response.body().getCoordinatesList().get(i).getData());
                                }

                            }
                            getAllCalActData(farmIdList);
                        } else {
                            getAllCalActData(farmIdList);
                        }

                    } else {
                        getAllCalActData(farmIdList);
                    }

                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
//                    loader.setVisibility(View.INVISIBLE);
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "Error getAllCoordinatesOfFarms" + error);
//                        make_all_farm_offline_button.setEnabled(true);
//                        make_all_farm_offline_button.setClickable(true);
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showDialogForFinish(LandingActivity.this, "Failed to load data for offline mode, Please try again later");
                        getAllCalActData(farmIdList);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<FarmsCoordinatesResponse> call, Throwable t) {

                Log.e(TAG, "getAllCoordinatesOfFarms fail " + t.toString());
                ViewFailDialog viewFailDialog = new ViewFailDialog();
                viewFailDialog.showDialogForFinish(LandingActivity.this, "Failed to load data for offline mode, Please try again later");
            }
        });

    }

    // Get all done activities of selected farms
    int totalActivityZize = 0;

    private void getAllCalActData(List<String> farmIdList) {
        if (farmIdList.size() == 0) {
//            getCalendarData(farmIdList);
            setDeviceId();
        } else {
            FarmIdArray idArray = new FarmIdArray();
            idArray.setCompId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
            idArray.setToken(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
            idArray.setUserId(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
            idArray.setFarmIdList(farmIdList);
            Log.e(TAG, "Farm Id List " + new Gson().toJson(idArray));
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            final boolean[] isLoaded = {false};
            long currMillis = AppConstants.getCurrentMills();
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            apiInterface.getDoneCalActivities(idArray).enqueue(new Callback<CalendarActResponse>() {
                @Override
                public void onResponse(Call<CalendarActResponse> call, Response<CalendarActResponse> response) {
                    String error = null;
                    isLoaded[0] = true;
                    Log.e(TAG, "Calendar Activities code " + new Gson().toJson(response.code()));

                    if (response.isSuccessful()) {
                        List<DoneActivityResponseNew> doneActivityList = response.body().getDoneActivityList();
                        if (doneActivityList != null && doneActivityList.size() > 0) {
                            totalActivityZize = doneActivityList.size() - 1;
                            for (int i = 0; i < doneActivityList.size(); i++) {
                                DoneActivityResponseNew doneAct = doneActivityList.get(i);
                                DownloadImagesActivity downloadImagesActivity = new DownloadImagesActivity(doneAct, i, farmIdList);
                                downloadImagesActivity.execute();
                            }
                        }
                        Log.e(TAG, "Calendar Activities Res " + new Gson().toJson(response.body()));
                        getCalendarData(farmIdList);
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
//                        loader.setVisibility(View.INVISIBLE);
                        try {
                            getCalendarData(farmIdList);
                            error = response.errorBody().string().toString();
                            Log.e(TAG, "Calendar Activities Error  " + error);
//                            make_all_farm_offline_button.setEnabled(true);
//                            make_all_farm_offline_button.setClickable(true);
//                            offlineModeImg.setClickable(true);
//                            offlineModeImg.setEnabled(true);
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showDialogForFinish(LandingActivity.this, "Failed to load data for offline mode, Please try again later");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<CalendarActResponse> call, Throwable t) {
                    getCalendarData(farmIdList);
                }
            });
        }
    }

    private void getCalendarData(final List<String> farmIdList) {
        for (int i = 0; i < farmIdList.size(); i++) {
            final String farmId = farmIdList.get(i);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
            final String farm_id = farmId;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("comp_id", "" + comp_id);
            jsonObject.addProperty("farm_id", "" + farm_id);
            jsonObject.addProperty("user_id", "" + userId);
            jsonObject.addProperty("token", "" + token);
            Log.e(TAG, "getCalendarData " + new Gson().toJson(jsonObject));
            final boolean[] isLoaded = {false};
            long currMillis = AppConstants.getCurrentMills();
            Call<TimelineResponse> getCalenderData = apiInterface.getCalendarData(comp_id, farm_id, userId, token);
            final int finalI = i;
            getCalenderData.enqueue(new Callback<TimelineResponse>() {
                @Override
                public void onResponse(Call<TimelineResponse> call, retrofit2.Response<TimelineResponse> response) {
                    Log.e(TAG, "Calendar Status " + response.code());
                    isLoaded[0] = true;
                    String error = "";
                    if (response.isSuccessful()) {

                        TimelineResponse timelineResponse = response.body();
                        if (response.body().getStatus() == 10) {
                            viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                        } else if (timelineResponse.getStatus() == 1) {

                            CalendarJSON calendarJSON = new CalendarJSON();
                            calendarJSON.setCalendarData(timelineResponse.getFarmCalData());
                            Timeline timeline = new Timeline(farmId, new Gson().toJson(calendarJSON), null);
                            TimelineCacheManager.getInstance(context).addTimeline(null, timeline);
                            if (finalI == farmIdList.size() - 1)
                                fetcHarvestData(farmIdList);

                        } else {
                            if (finalI == farmIdList.size() - 1)
                                fetcHarvestData(farmIdList);
                        }
                        Log.e(TAG, "Calendar " + new Gson().toJson(response.body()));
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            if (finalI == farmIdList.size() - 1)
                                fetcHarvestData(farmIdList);
                            error = response.errorBody().string().toString();
                            Log.e(TAG, "Calendar Unsuccess " + error);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<TimelineResponse> call, Throwable t) {

                    Log.e(TAG, "Calendar Failure " + t.toString());
//                viewFailDialog.showDialog(context, resources.getString(R.string.failed_to_load_timeline));
                    if (finalI == farmIdList.size() - 1)
                        fetcHarvestData(farmIdList);

                }
            });

        }
    }

    private void fetcHarvestData(final List<String> farmIdList) {
        try {
            for (int i = 0; i < farmIdList.size(); i++) {
                final String farmId = farmIdList.get(i);
                final int finalI = i;
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                Call<ViewHarvestDetails> callHarvestData = apiService.getHarvestDetailStatus(
                        farmId,
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID),
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));

                callHarvestData.enqueue(new Callback<ViewHarvestDetails>() {
                    @Override
                    public void onResponse(Call<ViewHarvestDetails> call, Response<ViewHarvestDetails> response) {
                        String error = null;
                        isLoaded[0] = true;
                        try {
                            Log.e(TAG, "Harvest Data code " + response.code());

                            if (response.isSuccessful()) {
                                Log.e(TAG, "Harvest Data " + new Gson().toJson(response.body()));
                                if (response.body().getStatus() == 10) {
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.multiple_login_msg));
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                    //un authorized access
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                                } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                    // auth token expired
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                                } else {
                                    if (response.body() != null) {
                                        ViewHarvestDetails harvestDetails = response.body();
                                        if (harvestDetails.getData1() != null && harvestDetails.getData1().size() > 0) {
                                            int lastBagNo = 0;
                                            for (int i = 0; i < harvestDetails.getData().size(); i++) {
                                                TimelineHarvest timelineHarvest = new TimelineHarvest();
                                                timelineHarvest.setType(AppConstants.TIMELINE.HR);
                                                for (int j = 0; j < harvestDetails.getData().get(i).size(); j++) {
                                                    lastBagNo = Integer.parseInt(harvestDetails.getData().get(i).get(j).getBagNo());
                                                }
                                                timelineHarvest.setHarvestDetailInnerDataList(harvestDetails.getData().get(i));
                                                timelineHarvest.setHarvestDetailMaster(harvestDetails.getData1().get(i));
                                                timelineHarvest.setDoa(timelineHarvest.getHarvestDetailMaster().getDoa());

                                                ViewHarvestDetails harvest = new ViewHarvestDetails();
                                                harvest.setData(harvestDetails.getData());
                                                harvest.setData1(harvestDetails.getData1());
                                                HarvestT harvestT = new HarvestT(farmId,
                                                        new Gson().toJson(harvest), "{}", "Y");
                                                HarvestCacheManager.getInstance(context).addVisits(harvestT);

                                            }
                                            SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.LAST_BAG_NO, lastBagNo + 1);
                                            if (finalI == farmIdList.size() - 1)
                                                getAllVisitsOfFarm(farmIdList);
                                        } else {
                                            SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.LAST_BAG_NO, 1);
                                            if (finalI == farmIdList.size() - 1)
                                                getAllVisitsOfFarm(farmIdList);
                                        }
                                    } else {
                                        if (finalI == farmIdList.size() - 1)
                                            getAllVisitsOfFarm(farmIdList);
                                    }
                                }
                            } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                            } else {
                                try {
                                    if (finalI == farmIdList.size() - 1)
                                        getAllVisitsOfFarm(farmIdList);
                                    error = response.errorBody().string().toString();
                                    Log.e(TAG, "Harvest Err" + error);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Harvest Exc" + e.toString());

                        }

                    }

                    @Override
                    public void onFailure(Call<ViewHarvestDetails> call, Throwable t) {

                        Log.e(TAG, "Failure Harvest Data " + t.getMessage().toString());
                        if (finalI == farmIdList.size() - 1)
                            getAllVisitsOfFarm(farmIdList);

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void getInputCosts(final List<String> farmIdList) {
        for (int i = 0; i < farmIdList.size(); i++) {
            final String farmId = farmIdList.get(i);
            final int finalI = i;
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            // farmId="1";

            apiInterface.getInputCostList(farmId, userId, token).enqueue(new Callback<InputCostResponse>() {

                @Override
                public void onResponse(Call<InputCostResponse> call, Response<InputCostResponse> response) {
                    Log.e(TAG, "Status IC " + response.code());
                    String error = null;
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.multiple_login_msg));
                        } else {
                            String inputData = "[]";
                            String resourceData = "[]";
                            String inputDataOffline = "[]";
                            String resourceDataOffline = "[]";

                            if (response.body().getInputCostList() != null && response.body().getInputCostList().size() > 0) {
                                inputData = new Gson().toJson(response.body().getInputCostList());
                            }
                            InputCostT costT = new InputCostT(farmId, inputData, inputDataOffline, "Y");
                            InputCostCacheManager.getInstance(context).addInputCost(costT);

                            if (response.body().getResourceList() != null && response.body().getResourceList().size() > 0) {
                                resourceData = new Gson().toJson(response.body().getResourceList());
                            }
                            ResourceCostT resourceCostT = new ResourceCostT(farmId, resourceData, resourceDataOffline, "Y");
                            ResourceCacheManager.getInstance(context).addResource(resourceCostT);
                            if (finalI == farmIdList.size() - 1)
                                insertRoomFarmsNew(selectedFarmList);
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        try {
                            if (finalI == farmIdList.size() - 1)
                                insertRoomFarmsNew(selectedFarmList);
                            error = response.errorBody().string();
                            Log.e(TAG, "Input Cost Err " + error);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<InputCostResponse> call, Throwable t) {
                    if (finalI == farmIdList.size() - 1)
                        insertRoomFarmsNew(selectedFarmList);
                    Log.e(TAG, "Input Cost Failure " + t.toString());
                }
            });
        }

    }

    private void insertRoomFarmsNew(List<FetchFarmResultNew> farmResultList) {
        Log.e(TAG, "Inserting data in cache");
        if (farmResultList != null && farmResultList.size() > 0) {
            int length = farmResultList.size();
            for (int i = 0; i < length; i++) {
                //downloadFileNew(i);
                if (farmResultList.get(i).isSelectedd()) {
                    FetchFarmResultNew result = farmResultList.get(i);
                    String germiData = "[]";

                    List<LatLngFarm> lst = new ArrayList<>();
                    if (coordinateHashMap.containsKey(result.getFarmMasterNum()) && coordinateHashMap.get(result.getFarmMasterNum()) != null) {
                        lst.addAll(coordinateHashMapToStore.get(result.getFarmMasterNum()));
                    }
                    String coords = new Gson().toJson(lst);
                    String pldOnlineJson = "[]";

                    FulDetailsAsync async = new FulDetailsAsync(result.getFarmMasterNum(), coords, germiData, pldOnlineJson,
                            i, length);
                    async.execute();

                } else {

                }
            }
        } else {

            offlineModeImg.setClickable(true);
            offlineModeImg.setEnabled(true);
            backImage.setClickable(true);
            backImage.setEnabled(true);
            selectAll.setClickable(true);
            selectAll.setEnabled(true);
            setDeviceId();
        }
        //  SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ONLINE, "offline");

    }

    private class FulDetailsAsync extends AsyncTask<String, Integer, String> {
        String farmId;
        String coords;
        String germiData;
        String pldOnlineJson;
        int index = 0, length = 0;

        public FulDetailsAsync(String farmId, String coords, String germiData, String pldOnlineJson, int index, int length) {
            this.index = index;
            this.length = length;
            this.farmId = farmId;
            this.coords = coords;
            this.germiData = germiData;
            this.pldOnlineJson = pldOnlineJson;
        }

        @Override
        protected String doInBackground(String... strings) {

            getFullDetails(farmId, coords, germiData, pldOnlineJson, index, length);

            return null;
        }
    }

    private void getFullDetails(String farmId, String coords, String germiData, String pldOnlineJson, int index, int length) {
        JsonObject object = new JsonObject();
        object.addProperty("farm_id", "" + farmId);
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

                    FarmFullDetails details = response1.body();
                    try {
                        try {
                            String farmerId = details.getPersonDetails().getPersonId() + "";

                            //Farmer details
                            FarmerAndFarmData farmerAndFarmData = new FarmerAndFarmData();
                            FarmerCacheManager.getInstance(context).getFarmer(new FarmerCacheManager.GetFarmerListener() {
                                @Override
                                public void onFarmerLoaded(FarmerT farmerT) {
                                    if (farmerT == null || !AppConstants.isValidString(farmerT.getFarmerId())) {

                                        if (details.getPersonDetails() != null) {
                                            PersonDetails personDetails = details.getPersonDetails();
                                            farmerAndFarmData.setLiteracy(personDetails.getLiteracyStatus());
                                            farmerAndFarmData.setFinancial_status(personDetails.getFinancialStatus());
                                            farmerAndFarmData.setIdProof(personDetails.getIdProof());
                                            farmerAndFarmData.setAddressProof(personDetails.getAddressProof());
                                            farmerAndFarmData.setPersonId(personDetails.getPersonId() + "");
                                            farmerAndFarmData.setCompId(personDetails.getCompId() + "");
                                            farmerAndFarmData.setClusterId(personDetails.getClusterId() + "");
//                            farmerAndFarmData.setLiteracy(personDetails.getRoleId()+"");
                                            farmerAndFarmData.setName(personDetails.getName());
                                            farmerAndFarmData.setFather_name(personDetails.getFatherName());
//                            farmerAndFarmData.setLiteracy(personDetails.getDesignation());
                                            farmerAndFarmData.setCountryCode(personDetails.getCountryCode());
                                            farmerAndFarmData.setMob(personDetails.getMob());
                                            farmerAndFarmData.setEmail(personDetails.getEmail());
//                            farmerAndFarmData.setLiteracy(personDetails.getSignature());
                                            farmerAndFarmData.setDob(personDetails.getDob());
                                            farmerAndFarmData.setGeder(personDetails.getGender());
                                            farmerAndFarmData.setIsSmartPhone(personDetails.getIsUsingSphone());
                                            farmerAndFarmData.setIsShareHolder(personDetails.getIsShareholder());
                                            try {

                                                if (AppConstants.isValidString(personDetails.getCattleJson())) {
                                                    CowAndCatles cowAndCatles = new Gson().fromJson(personDetails.getCattleJson().trim(), CowAndCatles.class);
                                                    farmerAndFarmData.setCowAndCatles(cowAndCatles);
                                                }
                                            } catch (Exception e) {

                                            }

                                            farmerAndFarmData.setAnualIncome(personDetails.getEstIncome());
                                            farmerAndFarmData.setImage(personDetails.getImgLink());
                                            farmerAndFarmData.setPan(personDetails.getPan());
                                            farmerAndFarmData.setAadhaar(personDetails.getAadhaar());
                                            farmerAndFarmData.setSpouse_name(personDetails.getSpouseName());
                                            farmerAndFarmData.setSpouseDob(personDetails.getSpouseDob());
                                            farmerAndFarmData.setBeneficiary_name(personDetails.getBeneficiaryName());
                                            farmerAndFarmData.setCivil_status(personDetails.getCivilStatus());
                                            farmerAndFarmData.setCasteCategoryId(personDetails.getCasteCategory());
                                            farmerAndFarmData.setCasteId(personDetails.getCaste());
                                            farmerAndFarmData.setReligionId(personDetails.getReligion());
                                            farmerAndFarmData.setFamily_mem_count(personDetails.getFamilyMemCount() + "");
                                            farmerAndFarmData.setAdults_count(personDetails.getAdultsCount() + "");
                                            farmerAndFarmData.setKids_count(personDetails.getKidsCount() + "");
                                            farmerAndFarmData.setDependent_count(personDetails.getDependentCount() + "");
                                            farmerAndFarmData.setMobileNetworkId(personDetails.getMobileOperator());
                                            farmerAndFarmData.setMiaId(personDetails.getMia());
                                            farmerAndFarmData.setLiteracy(personDetails.getLiteracyStatus());
                                        }

                                        if (details.getPersonAddress() != null) {
                                            PersonAddress personAddress = details.getPersonAddress();
                                            farmerAndFarmData.setAddL1(personAddress.getAddL1());
                                            farmerAndFarmData.setAddL2(personAddress.getAddL2());
                                            farmerAndFarmData.setVillage_or_city(personAddress.getVillageOrCity());
                                            farmerAndFarmData.setMandal_or_tehsil(personAddress.getMandalOrTehsil());
                                            farmerAndFarmData.setDistrict(personAddress.getDistrict());
                                            farmerAndFarmData.setState(personAddress.getState());
                                            farmerAndFarmData.setCountry(personAddress.getCountry());
//                            farmerAndFarmData.setLiteracy(personAddress.getLiteracyStatus());
//                            farmerAndFarmData.setLiteracy(personAddress.getLiteracyStatus());
                                        }
                                        if (details.getBankDetails() != null) {
                                            BankDetails bankDetails = details.getBankDetails();

                                            farmerAndFarmData.setBankName(bankDetails.getBankName());
                                            farmerAndFarmData.setHolderName(bankDetails.getAccountName());
                                            farmerAndFarmData.setAccountNumber(bankDetails.getAccountNo());
                                            farmerAndFarmData.setBranch(bankDetails.getBranch());
                                            farmerAndFarmData.setIfscCode(bankDetails.getIfsc());
                                            farmerAndFarmData.setSwiftCode(bankDetails.getSwiftCode());
                                            farmerAndFarmData.setUpi_id(bankDetails.getUpiId());

                                        }
                                        String id = "" + Calendar.getInstance().getTime().getTime();
                                        FarmerT farmerTNew = new FarmerT(farmerId, new Gson().toJson(farmerAndFarmData), "N", "N", "N");
                                        FarmerCacheManager.getInstance(context).addFarmer(new FarmerCacheManager.FarmerInsertListener() {
                                            @Override
                                            public void onFarmerInserted(long farmerId) {

                                            }
                                        }, farmerTNew);
                                    }
                                }
                            }, farmId);
                        } catch (Exception e) {

                        }

                        //======================XXX==============================
                        //FarmDetails
//                        Farm farm=new Farm();
                        FarmData farmData = new FarmData();
                        if (details.getFarmsDetails() != null) {
                            FarmsDetails farmDetails = details.getFarmsDetails();
                            farmData.setFarmImagePath(farmDetails.getFarmPhoto());
                            farmData.setOwnerShipDoc(farmDetails.getOwnershipDoc());
                            farmData.setIsOwned(farmDetails.getIsOwned());
//                                farmData.setid(farmDetails.getId()+"");
                            farmData.setFarmId(farmDetails.getFarmMasterNum() + "");
                            farmData.setName(farmDetails.getFarmName());
                            farmData.setId(farmDetails.getCompFarmId());
                            farmData.setOwnerId(farmDetails.getOwnerId() + "");
//                                farmData.seto(farmDetails.getOperatorId()+"");
                            farmData.setCluster(farmDetails.getClusterId() + "");
                            farmData.setCompId(farmDetails.getCompId() + "");
                            farmData.setSeason(farmDetails.getSeasonNum() + "");
                            farmData.setIsOwned(farmDetails.getIsOwned());
                            farmData.setZoom(farmDetails.getZoom() + "");
                            farmData.setIsSelected(farmDetails.getIsSelected());
                            farmData.setDelqReason(farmDetails.getDelqReason());
                            farmData.setMotorHp(farmDetails.getMotorHp());
//                                farmData.setIsOwned(farmDetails.getIsOwned());
//                                farmData.setIsOwned(farmDetails.getIsOwned());
                        }

                        if (details.getFarmCropDetails() != null) {
                            FarmCropDetails cropDetails = details.getFarmCropDetails();
                            farmData.setCurrentCropName(cropDetails.getCropName());
                            farmData.setPreviouscrop(cropDetails.getPreviousCrop());
                            farmData.setSeason(cropDetails.getSeasonNum() + "");
                            farmData.setCropId(cropDetails.getCropId() + "");
                            farmData.setArea(cropDetails.getExpArea() + "");
                            farmData.setAreaS(cropDetails.getExpArea() + "");
                            farmData.setStandingArea(cropDetails.getStandingArea() + "");
                            farmData.setActualArea(cropDetails.getActualArea() + "");
                            farmData.setIs_irrigated(cropDetails.getIsIrrigated() + "");
                            farmData.setIrrigationsource(cropDetails.getIrrigationSourceId() + "");
                            farmData.setIrrigationtype(cropDetails.getIrrigationTypeId() + "");
                            farmData.setSoiltype(cropDetails.getSoilTypeId() + "");
                            farmData.setPlanting_method(cropDetails.getPlantingMethod());
                            farmData.setExpFloweringDate(cropDetails.getExpFloweringDate());
                            farmData.setExpTransplantDate(cropDetails.getExpTransplantDate());
                            farmData.setExpSowingDate(cropDetails.getExpSowingDate());
                            farmData.setExpHarvestDate(cropDetails.getExpHarvestDate());

                            farmData.setTransplant_date(cropDetails.getTransplantDate());
                            farmData.setSowingDate(cropDetails.getSowingDate());
                            farmData.setActualHarvestDate(cropDetails.getActualHarvestDate());
                            farmData.setActualFloweringDate(cropDetails.getActualFloweringDate());

                            farmData.setAreaHarvested(cropDetails.getAreaHarvested());
//                            farmData.setsee(cropDetails.getSeedLotNo());
//                            farmData.setIsOwned(cropDetails.getSeedProvidedOn());
//                            farmData.setIsOwned(cropDetails.getSeedUnitId());
//                            farmData.setIsOwned(cropDetails.getQtySeedProvided());
//                            farmData.setIsOwned(cropDetails.getGermination());
//                            farmData.setIsOwned(cropDetails.getPopulation());
//                            farmData.setIsOwned(cropDetails.getSeedUnitId());
//                            farmData.setIsOwned(cropDetails.getExpHarvestDate());
                        }

                        if (details.getFarmAddressDetails() != null) {
                            FarmAddressDetails addressDetails = details.getFarmAddressDetails();
                            farmData.setCountry(addressDetails.getCountry());
                            farmData.setState(addressDetails.getState());
                            farmData.setDistrict(addressDetails.getDistrict());
                            farmData.setVillageOrCity(addressDetails.getVillageOrCity());
                            farmData.setMandalOrTehsil(addressDetails.getMandalOrTehsil());
                            farmData.setAddL1(addressDetails.getAddL1());
                            farmData.setAddL2(addressDetails.getAddL2());
                        }
                        if (details.getAssetsDataList() != null && details.getAssetsDataList().size() > 0) {
                            farmData.setAssetsList(formatAsstesList(details.getAssetsDataList()));
                        }
                        if (details.getPrevCropDatumList() != null && details.getPrevCropDatumList().size() > 0) {
                            farmData.setFpoCropList(getCrops(details.getPrevCropDatumList()));
                        }
                        Farm farm = new Farm(details.getFarmsDetails().getFarmMasterNum() + "", details.getFarmsDetails().getCompFarmId(),
                                details.getPersonDetails().getPersonId() + "", details.getPersonDetails().getName(), details.getPersonDetails().getMob(),
                                details.getPersonDetails().getClusterId() + "", details.getPersonDetails().getCompId() + "",
                                details.getFarmAddressDetails().getAddL1(), details.getFarmAddressDetails().getAddL2(),
                                details.getFarmAddressDetails().getVillageOrCity(),
                                details.getFarmAddressDetails().getDistrict(), details.getFarmAddressDetails().getMandalOrTehsil(),
                                details.getFarmAddressDetails().getState(), details.getFarmAddressDetails().getCountry(),
                                details.getFarmCropDetails().getExpArea() + "",
                                details.getFarmCropDetails().getActualArea() + "",
                                details.getFarmCropDetails().getIrrigationSourceId() + "", details.getFarmCropDetails().getPreviousCrop(),
                                details.getFarmCropDetails().getIrrigationTypeId() + "",
                                details.getFarmCropDetails().getSoilTypeId() + "", details.getFarmCropDetails().getSowingDate(),
                                details.getFarmCropDetails().getExpFloweringDate(), details.getFarmCropDetails().getActualFloweringDate(),
                                details.getFarmCropDetails().getExpHarvestDate(), details.getFarmCropDetails().getActualHarvestDate(),
                                details.getFarmCropDetails().getAreaHarvested() + "",
                                details.getFarmCropDetails().getStandingArea() + "",
                                "[]", "[]", null, null,
                                null, details.getFarmsDetails().getLatCord(), details.getFarmsDetails().getLongCord(),
                                null, "", null, germiData, null, null, "N", "Y", pldOnlineJson, "[]",
                                null, "N", "[]", "", details.getFarmsDetails().getFarmName(),
                                "Y", details.getFarmCropDetails().getSeasonNum() + "", "N", coords, "", "",
                                "", "", "", "");

                        String fullDetails = "{}";
                        fullDetails = new Gson().toJson(farmData);
                        farm.setFarmFullData(fullDetails);

//                        farmIdList.add(result.getFarmId());
//                        farmList.add(farm);
                        List<Farm> farmList = new ArrayList<>();
                        farmList.add(farm);
                        FarmCacheManager.getInstance(context).addFarms(new FarmNotifyInterface() {
                            @Override
                            public void onFarmLoaded(List<Farm> farmList) {

                            }

                            @Override
                            public void onFarmAdded() {

                            }

                            @Override
                            public void onFarmInsertError(Throwable e) {

                            }

                            @Override
                            public void onNoFarmsAvailable() {

                            }

                            @Override
                            public void farmDeleted() {

                            }

                            @Override
                            public void farmDeletionFaild() {

                            }
                        }, farmList);

                       /* FarmCacheManager.getInstance(context).addFarm(new FarmCacheManager.OnFarmInserted() {
                            @Override
                            public void onInserted(long id) {

                            }
                        }, farm);
*/
                    } catch (Exception e) {
                        e.printStackTrace();
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
                if (index == length - 1)
                    setDeviceId();
            }

            @Override
            public void onFailure(Call<FarmFullDetails> call, Throwable t) {
                Log.e(TAG, "Failure FULL " + t.toString());
                if (index == length - 1)
                    setDeviceId();
            }
        });
    }

    private List<CropFormDatum> cropFormDatumList = new ArrayList<>();
    private Map<String, CropFormDatum> cropFormDatumListMap = new HashMap<>();
    private List<CropFormDatum> cropFormDatumListSuper = new ArrayList<>();
    private Map<String, CropFormDatum> cropFormDatumListSuperMap = new HashMap<>();

    private List<List<CropFormDatum>> formatAsstesList(List<AssetsData> assetsDataList) {

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
                return assetListForm;

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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    assetListForm.add(cropFormDatumList);
                }
                return assetListForm;

            }
        } else {
            return getListDataSuper();
        }
    }

    private List<FPOCrop> getCrops(List<PrevCropDatum> cropDetailsList) {
        List<FPOCrop> fpoCropList = new ArrayList<>();
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
                    return fpoCropList;
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
//                        fpoCrop.setSelectedCrop(getOperatorIndex(id.getCropId()+"",farmCropList));
                        fpoCrop.setCropId(id.getCropId() + "");
                        fpoCropList.add(fpoCrop);

                    }
                } catch (Exception e) {

                }
            }
            return fpoCropList;
        } else {
            FPOCrop fpoCrop = new FPOCrop(new FormModel());
            fpoCrop.setCropFormDatumArrayLists(getListData());
            fpoCropList.add(fpoCrop);
            return fpoCropList;
        }
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

    private void setDeviceId() {
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String deviceId = AppConstants.getMacAddr();
        final boolean[] isLoaded = {false};
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        long currMillis = AppConstants.getCurrentMills();
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.setDeviceId(deviceId, userId, token).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response1) {
                String error = null;
                isLoaded[0] = true;
                Log.e(TAG, "setDeviceId code " + response1.code());
                if (response1.isSuccessful()) {
                    StatusMsgModel villageResponse = response1.body();
                    Log.e(TAG, "setDeviceId " + new Gson().toJson(villageResponse));
                   /* try {
                        Log.e(TAG, "getChaksOfVillage Response " + response1.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
//                    Log.e(TAG, "getGeoCount Response " + new Gson().toJson(countResponse));
                    if (villageResponse.getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, villageResponse.getMessage());
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response1.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        String todayDateStr;
                        Date todayDate = Calendar.getInstance().getTime();
                        DateFormat df = new SimpleDateFormat(AppConstants.DATE_FORMAT_SERVER);
                        todayDateStr = df.format(todayDate);
                        Log.e(TAG, "Today date " + todayDateStr);

                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.OFFLINE_MODE_DATE, todayDateStr);
                        SharedPreferencesMethod.setBoolean(context, SharedPreferencesMethod.OFFLINE_MODE, true);
                        finish();
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                } else if (response1.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response1.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(LandingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response1.errorBody().string().toString();
                        Log.e(TAG, "setDeviceId Error " + error + " Code " + response1.code() + " ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e(TAG, "setDeviceId Failure  " + t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
            }
        });
    }

    private void getAllVisitsOfFarm(final List<String> farmIdList) {
        for (int i = 0; i < farmIdList.size(); i++) {
            final String farmId = farmIdList.get(i);
            final int finalI = i;
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("farm_id", farmId);
            jsonObject.addProperty("user_id", userId);
            jsonObject.addProperty("token", token);
            apiInterface.getAllVisitsOfFarmModel(jsonObject).enqueue(new Callback<FarmVisitResponse>() {
                @Override
                public void onResponse(Call<FarmVisitResponse> call, Response<FarmVisitResponse> response) {
                    Log.e(TAG, "getAllVisitsOfFarm code " + response.code());
                    if (response.isSuccessful()) {
                        try {
                            Log.e(TAG, "getAllVisitsOfFarm res " + new Gson().toJson(response.body()));
                            List<FarmVisitResponse.VisitReport> visitResponseDatumList = response.body().getVisitReport();
                            for (int j = 0; j < visitResponseDatumList.size(); j++) {
                                FarmVisitResponse.VisitReport visitResponseDatum = visitResponseDatumList.get(j);
                                VisitResponseNew visitResponseNew = new VisitResponseNew();
                                DoneActivityResponseNew doneActivityResponseNew = new DoneActivityResponseNew();
                                doneActivityResponseNew.setImages(visitResponseDatum.getImages());
                                doneActivityResponseNew.setComment(visitResponseDatum.getComment());
                                doneActivityResponseNew.setMoisture("" + visitResponseDatum.getMoisture());
                                doneActivityResponseNew.setGrade(visitResponseDatum.getGrade());
                                doneActivityResponseNew.setVisitDate(visitResponseDatum.getVisitDate());
                                doneActivityResponseNew.setVisitNumber("" + visitResponseDatum.getVisitNumber());

                                List<DoneActInputs> data = new ArrayList<>();
                                List<FarmVisitResponse.AgriInput> agriInputs = visitResponseDatum.getAgriInputs();

                                if (agriInputs != null && agriInputs.size() > 0) {
                                    for (int k = 0; k < agriInputs.size(); k++) {
                                        DoneActInputs doneAct = new DoneActInputs();
                                        FarmVisitResponse.AgriInput input = agriInputs.get(k);
                                        doneAct.setFarmCalendarActivityId(null);
                                        doneAct.setFarmId(farmId);
//                                        doneAct.setFarmCalendarId(input.getca);
//                                        doneAct.setActivity();
                                        doneAct.setNarration(input.getNarration());
                                        doneAct.setVrId("" + input.getVrId());
                                        doneAct.setType(input.getType());
                                        doneAct.setOtherAgriInput(input.getOtherAgriInput());
                                        doneAct.setName(input.getName());

                                        data.add(doneAct);
                                    }
                                }

                                doneActivityResponseNew.setData(data);
                                doneActivityResponseNew.setFarmId(farmId);
                                visitResponseNew.setShowResponse(doneActivityResponseNew);
                                visitResponseNew.setMoisture("" + visitResponseDatum.getMoisture());
                                visitResponseNew.setFarmGrade(visitResponseDatum.getGrade());
                                visitResponseNew.setComment(visitResponseDatum.getComment());
                                visitResponseNew.setFarmId(farmId);
                                visitResponseNew.setVisitReportId("" + visitResponseDatum.getVisitReportId());
                                visitResponseNew.setEffectiveArea("" + visitResponseDatum.getEffectiveArea());
                                visitResponseNew.setVisitNumber("" + visitResponseDatum.getVisitNumber());
//                                visitResponseNew.setVisitImages(visitResponseDatum.getImages());

                                DownloadImagesVisit downloadImagesNew = new DownloadImagesVisit(visitResponseNew);
                                downloadImagesNew.execute();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (finalI == totalSelectedFarms - 1) {
                            getInputCosts(farmIdList);
                        }
                    } else {
                        try {
                            Log.e(TAG, "getAllVisitsOfFarm err " + response.errorBody().string());
                            if (finalI == totalSelectedFarms - 1) {
                                getInputCosts(farmIdList);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<FarmVisitResponse> call, Throwable t) {
                    Log.e(TAG, "getAllVisitsOfFarm fail " + t.toString());
                    if (finalI == totalSelectedFarms - 1) {
                        getInputCosts(farmIdList);
                    }
                }
            });
        }
    }

    public class DownloadImagesVisit extends AsyncTask<String, Integer, String> {
        VisitResponseNew response;

        public DownloadImagesVisit(VisitResponseNew response) {
            this.response = response;
            Log.e(TAG, "DownloadImagesVisit " + new Gson().toJson(response));
        }

        @Override
        protected String doInBackground(String... stringss) {

            if (response.getShowResponse() != null) {

                if (response.getShowResponse().getImages() != null) {
                    for (int i = 0; i < response.getShowResponse().getImages().size(); i++) {
                        String filename;
                        filename = "" + response.getVisitReportId();
                        filename = filename + "_" + i + ".png";
                        String fullPath = Environment.getExternalStorageDirectory() + AppConstants.getImagePath(AppConstants.FILE_PATH_TYPE.VIST, LandingActivity.this) + "/" + filename;
                        Log.e(TAG, "DownloadImagesVisit " + fullPath);

                        String uRl = response.getShowResponse().getImages().get(i).getImgLink();
                        if (AppConstants.isValidString(uRl)) {


                            File direct = new File(Environment.getExternalStorageDirectory() + AppConstants.getImagePath(null, LandingActivity.this));
                            if (!direct.exists()) {
                                direct.mkdirs();
                            }
                            File path = new File(Environment.getExternalStorageDirectory() + AppConstants.getImagePath(AppConstants.FILE_PATH_TYPE.VIST, LandingActivity.this));
                            File oldFile = new File(fullPath);

                            if (!path.exists()) {
                                path.mkdirs();
                            }
                            if (!oldFile.exists()) {
//                                oldFile.mkdir();

                                URL url = null;
                                try {
//                                    url = new URL(uRl);

                                    String finalFilename = filename;
                                    Log.e(TAG, "DownloadImagesVisit " + uRl);

                                    /*Glide.with(context)
                                            .load(uRl)
                                            .into(new CustomTarget<Drawable>() {
                                                @Override
                                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                                                    Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
//                                                    Toast.makeText(MainActivity.this, "Saving Image...", Toast.LENGTH_SHORT).show();
                                                    saveImage(bitmap, path, finalFilename);
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                                }

                                                @Override
                                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                                    super.onLoadFailed(errorDrawable);

//                                                    Toast.makeText(MainActivity.this, "Failed to Download Image! Please try again later.", Toast.LENGTH_SHORT).show();
                                                }
                                            });*/
                                    url = new URL(uRl);
                                    Bitmap bm = null;
                                    try {
                                        bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    //Create Path to save Image

                                    if (!path.exists()) {
                                        path.mkdirs();
                                    }

                                    File imageFile = new File(path, filename); // Imagename.png
                                    FileOutputStream out = null;
                                    try {
                                        out = new FileOutputStream(imageFile);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        bm.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
                                        out.flush();
                                        out.close();
                                        // Tell the media scanner about the new file so that it is
                                        // immediately available to the user.
                                        MediaScannerConnection.scanFile(LandingActivity.this, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                                            public void onScanCompleted(String path, Uri uri) {

                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e(TAG, "DownloadImagesVisit " + fullPath + " already exist");
                            }
                        }
                        response.getShowResponse().getImages().get(i).setImgLink(fullPath);
                    }
                }
            }
            Visit vr = new Visit(response.getVisitReportId(),
                    new Gson().toJson(response), "Y", null, "N", null, response.getFarmId());
            VrCacheManager.getInstance(context).addVisits(vr);


            return null;
        }
    }


    private void saveImage(Bitmap image, File storageDir, String imageFileName) {

        boolean successDirCreated = false;
        if (!storageDir.exists()) {
            successDirCreated = storageDir.mkdir();
        } else {
            successDirCreated = true;
        }
        if (successDirCreated) {
            File imageFile = new File(storageDir, imageFileName);
            String savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.close();
//                Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
//                Toast.makeText(MainActivity.this, "Error while saving image!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "Failed to make folder!", Toast.LENGTH_SHORT).show();
        }
    }


    public class DownloadImagesActivity extends AsyncTask<String, Integer, String> {
        String uRl;
        String filename;
        DoneActivityResponseNew response;
        int position;
        List<String> farmIdList;

        public DownloadImagesActivity(DoneActivityResponseNew response, int position, List<String> farmIdList) {
            this.response = response;
            this.position = position;
            this.farmIdList = farmIdList;
        }

        @Override
        protected String doInBackground(String... strings) {

            if (response.getImages() != null) {
                if (response.getImages() != null) {
                    for (int i = 0; i < response.getImages().size(); i++) {
                        if (response.getImages().get(i) != null && response.getImages().get(i).getImgLink() != null) {
                            filename = "";
                            filename = "" + response.getFarmCalActivityId() + "_" + i + ".png";
                            String fullPath = AppConstants.getImagePath(AppConstants.FILE_PATH_TYPE.ACTIVITY, LandingActivity.this) + "/" + filename + ".png";
                            uRl = response.getImages().get(i).getImgLink();
                            File direct = new File(Environment.getExternalStorageDirectory() + "/" + resources.getString(R.string.app_name));
                            response.getImages().get(i).setImgLink(fullPath);
                            if (!direct.exists()) {
                                direct.mkdirs();
                            }
                            File directnew = new File(Environment.getExternalStorageDirectory() + "/" + resources.getString(R.string.app_name) + "/activities");
                            if (!directnew.exists()) {
                                directnew.mkdirs();
                            }
                            File oldFile = new File(fullPath);
                            if (!oldFile.exists()) {
                                URL url = null;
                                try {
                                    url = new URL(uRl);
                                    InputStream in = new BufferedInputStream(url.openStream());
                                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                                    byte[] buf = new byte[700 * 700];
                                    int n = 0;
                                    while (-1 != (n = in.read(buf))) {
                                        out.write(buf, 0, n);
                                    }
                                    out.close();
                                    in.close();
                                    byte[] response = out.toByteArray();
                                    FileOutputStream fos = new FileOutputStream(directnew + "/" + filename + ".png");
                                    fos.write(response);
                                    fos.close();

                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else {
                    response.setDoneActivityImageList(new ArrayList<>());
                }
            }
            DoneActivity doneActivity = new DoneActivity(response.getFarmCalActivityId(), response.getFarmId(),
                    new Gson().toJson(response), "N", null, "Y");

            ActivityCacheManager.getInstance(context).addActivity(doneActivity);

            return null;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(LandingActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //Toast.makeText(OfflineModeActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();


            new AlertDialog.Builder(this)
                    .setTitle(resources.getString(R.string.please_allow_storage_permission))
                    .setMessage(resources.getString(R.string.click_on_storage))
                    .setCancelable(false)
                    .setPositiveButton(resources.getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            final Intent i = new Intent();
                            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            i.setData(Uri.parse("package:" + getPackageName()));
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(i);
                        }
                    })

                    .show();

        } else {
            ActivityCompat.requestPermissions(LandingActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(LandingActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    String fromDate = "", toDate = "";

    private void getSvTasks() {
        fromDate = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FILTER_SV_TIMELINE_FROM_DATE);
        toDate = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FILTER_SV_TIMELINE_TO_DATE);
        if (!AppConstants.isValidString(fromDate) || !AppConstants.isValidString(toDate)) {
            fromDate = AppConstants.getBeforeDate();
            toDate = AppConstants.getAfterDate();
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FILTER_SV_TIMELINE_FROM_DATE, fromDate);
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FILTER_SV_TIMELINE_TO_DATE, toDate);
        }
        taskCountTv.setText("" + SharedPreferencesMethod.getInt(context, SharedPreferencesMethod.TASK_COUNT));

        TaskCacheManager.getInstance(context).getTaskMaxId(new TaskCacheManager.TaskListener() {
            @Override
            public void onTaskFetched(SvTask svTask) {

                if (svTask != null && AppConstants.isValidString(svTask.getSyncDate()) && svTask.getSyncDate().equals(AppConstants.getCurrentDate())) {

                } else {
                    callSvTask();
                }
            }

            @Override
            public void onAllTaskDeleted(boolean isAllDeleted) {

            }
        });


    }

    private void callSvFarms() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userId);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("comp_id", compId);
        jsonObject.addProperty("added_by", personId);
        jsonObject.addProperty("start_date", fromDate);
        jsonObject.addProperty("end_date", toDate);

        Log.e(TAG, "callSvFarms Req " + new Gson().toJson(jsonObject));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getSvFarms(jsonObject).enqueue(new Callback<SvFarmResponse>() {
            @Override
            public void onResponse(Call<SvFarmResponse> call, Response<SvFarmResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        SvFarmResponse taskResponse = response.body();
                        Log.e(TAG, "callSvFarms res " + new Gson().toJson(taskResponse));
                        if (taskResponse.getData() != null && taskResponse.getData().size() > 0) {
                            new SvFarmAsync(taskResponse.getData()).execute();
                        } else {
                            callSvExpenses();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        callSvExpenses();
                        Log.e(TAG, "callSvFarms err " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SvFarmResponse> call, Throwable t) {
                Log.e(TAG, "callSvFarms fail " + t.toString());
                callSvExpenses();
            }
        });

    }

    private void callSvExpenses() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userId);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("comp_id", compId);
        jsonObject.addProperty("sv_id", personId);
        jsonObject.addProperty("start_date", fromDate);
        jsonObject.addProperty("end_date", toDate);
        Log.e(TAG, "callSvExpenses Req " + new Gson().toJson(jsonObject));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getExpenseData(jsonObject).enqueue(new Callback<ExpenseData>() {
            @Override
            public void onResponse(Call<ExpenseData> call, Response<ExpenseData> response) {
                if (response.isSuccessful()) {
                    try {
                        ExpenseData taskResponse = response.body();
                        Log.e(TAG, "callSvExpenses res " + new Gson().toJson(taskResponse));
                        if (taskResponse.getData() != null && taskResponse.getData().size() > 0) {
                            new SvExpenseAsync(taskResponse.getData()).execute();
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e(TAG, "callSvExpenses err " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ExpenseData> call, Throwable t) {
                Log.e(TAG, "callSvExpenses fail " + t.toString());
            }
        });

    }

    private void callSvTask() {
        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SV_TIMELINE_SYNC_1HOUR,AppConstants.getCurrentDateTime());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userId);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("comp_id", compId);
        jsonObject.addProperty("supervisor_id", personId);

        jsonObject.addProperty("start_date", fromDate);
        jsonObject.addProperty("end_date", toDate);

        Log.e(TAG, "getSvTasks Req " + new Gson().toJson(jsonObject));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getSvTasks(jsonObject).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        TaskResponse taskResponse = response.body();
                        Log.e(TAG, "getSvTasks res " + new Gson().toJson(taskResponse));
                        if (taskResponse.getData() != null && taskResponse.getData().size() > 0) {
                            new FindTodaysTaskCount(taskResponse.getData()).execute();
                        } else {
                            callSvFarms();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e(TAG, "getSvTasks err " + response.errorBody().string());
                        callSvFarms();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                Log.e(TAG, "getSvTasks fail " + t.toString());
                callSvFarms();
            }
        });
    }
    class FindTodaysTaskCount extends AsyncTask<String, Integer, String> {
        List<TaskDatum> taskDatumList;
        int count = 0;
        TaskCacheManager taskCacheManager;

        public FindTodaysTaskCount(List<TaskDatum> taskDatumList) {
            this.taskDatumList = taskDatumList;
            taskCacheManager = TaskCacheManager.getInstance(context);
            taskCacheManager.deleteAllTasks(new TaskCacheManager.TaskListener() {
                @Override
                public void onTaskFetched(SvTask svTask) {

                }

                @Override
                public void onAllTaskDeleted(boolean isAllDeleted) {

                }
            });
        }

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < taskDatumList.size(); i++) {
                TaskDatum taskDatum = taskDatumList.get(i);
                SvTask svTask = new SvTask(AppConstants.SV_TIMELINE_CARD_TYPES.TASK, taskDatum.getAssignDate(), new Gson().toJson(taskDatum), AppConstants.getCurrentDate(),
                        taskDatum.getIsComplete(), taskDatum.getCompleteDate(), taskDatum.getComments());
                taskCacheManager.addTask(svTask);

                if (AppConstants.isValidString(taskDatum.getAssignDate()) && !taskDatum.getAssignDate().equals("0000:00:00")
                        && !taskDatum.getAssignDate().equalsIgnoreCase("Invalid date")) {
                    Date todayDate = null, taskDate = null;
                    todayDate = AppConstants.getTodaysDateObj();
                    taskDate = AppConstants.getDateObj(taskDatum.getAssignDate());
                    if (todayDate != null && taskDate != null && todayDate.equals(taskDate)) {
                        count++;
                    } else {

                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            taskCountTv.setText("" + count);
            SharedPreferencesMethod.setInt(context, SharedPreferencesMethod.TASK_COUNT, count);
            callSvFarms();
        }
    }

    class SvFarmAsync extends AsyncTask<String, Integer, String> {
        List<SvFarm> taskDatumList;
        TaskCacheManager taskCacheManager;

        public SvFarmAsync(List<SvFarm> taskDatumList) {
            this.taskDatumList = taskDatumList;
            taskCacheManager = TaskCacheManager.getInstance(context);

        }

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < taskDatumList.size(); i++) {
                SvFarm taskDatum = taskDatumList.get(i);
                SvTask svTask = new SvTask(AppConstants.SV_TIMELINE_CARD_TYPES.FARM, taskDatum.getDoa(), new Gson().toJson(taskDatum), AppConstants.getCurrentDate(),
                        null, null, null);
                taskCacheManager.addTask(svTask);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            callSvExpenses();
        }
    }

    class SvExpenseAsync extends AsyncTask<String, Integer, String> {
        List<Datum> taskDatumList;
        TaskCacheManager taskCacheManager;

        public SvExpenseAsync(List<Datum> taskDatumList) {
            this.taskDatumList = taskDatumList;
            taskCacheManager = TaskCacheManager.getInstance(context);

        }

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < taskDatumList.size(); i++) {
                Datum taskDatum = taskDatumList.get(i);
                SvTask svTask = new SvTask(AppConstants.SV_TIMELINE_CARD_TYPES.EXPENSE, taskDatum.getExpDate(), new Gson().toJson(taskDatum), AppConstants.getCurrentDate(),
                        null, null, null);
                taskCacheManager.addTask(svTask);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
    
}
