package com.i9930.croptrails.Test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.FarmDetailsUpdateActivity;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.FarmDetailsUpdateActivity2;
import com.i9930.croptrails.FarmDetails.Model.FullDetailsResponse;
import com.i9930.croptrails.FarmDetails.Model.HarvestAndFloweringSendData;
import com.i9930.croptrails.FarmNavigation.FarmNavigationActivity;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.Maps.Offline.OfflineMapActivity;
import com.i9930.croptrails.Maps.ShowArea.Model.GetGpsCoordinates;
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;
import com.i9930.croptrails.Maps.ShowArea.Model.geojson.GeoJsonResponse;
import com.i9930.croptrails.Maps.ShowArea.ShowAreaWithUpdateActivity;
import com.i9930.croptrails.Maps.VerifyArea.MapsActivity;
import com.i9930.croptrails.Maps.VerifyArea.MapsActivityEasyMode;
import com.i9930.croptrails.Maps.VerifyArea.MapsActivitySamunnati;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitVisitForm.Delinquent.AddDelinquentActivity;
import com.i9930.croptrails.Test.ChakModel.FetchChakDatum;
import com.i9930.croptrails.Test.ChakModel.FetchChakResponse;
import com.i9930.croptrails.Test.model.full.FarmAddressDetails;
import com.i9930.croptrails.Test.model.full.FarmCropDetails;
import com.i9930.croptrails.Test.model.full.FarmFullDetails;
import com.i9930.croptrails.Test.model.full.FarmsDetails;
import com.i9930.croptrails.Test.model.full.PersonDetails;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.AppFeatures;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;
import static com.i9930.croptrails.Utility.AppConstants.for_date;
import static com.i9930.croptrails.Utility.AppConstants.isValidString;

public class FarmDetailsVettingActivity extends AppCompatActivity {
    @BindView(R.id.chakLeadNameTv)
    TextView chakLeadNameTv;
    @BindView(R.id.chakLeadCard)
    CardView chakLeadCard;
    String TAG = "FarmDetailsVettingActivity";
    @BindView(R.id.drawer_back_img)
    ImageView drawer_back_img;
    @BindView(R.id.title_name_d)
    TextView title_name_d;
    @BindView(R.id.title_address_d)
    TextView title_address_d;
    @BindView(R.id.farm_details_currentCrop)
    TextView currentCropTv;
    TextView seedQtyTv;
    FarmDetailsVettingActivity context;
    @BindView(R.id.farm_details_actual_flowering_date)
    TextView farm_details_actual_flowering_date;
    @BindView(R.id.farm_details_actual_harvest_date)
    TextView farm_details_actual_harvest_date;
    Resources resources;
    @BindView(R.id.update_farm_details)
    TextView update_farm_details_button;
    @BindView(R.id.view_farm_area_on_map)
    TextView view_farm_area_button;
    TextView tv_easy_mode, tv_normal_mode, tv_easy_mode_ultra;

    @BindView(R.id.rejectFarmButton)
    TextView rejectFarmButton;

    @BindView(R.id.reEntryFarmButton)
    TextView reEntryFarmButton;
    @BindView(R.id.selectFarmButton)
    TextView selectFarmButton;
    @BindView(R.id.linearLayoutVetting)
    LinearLayout linearLayoutVetting;
    @BindView(R.id.omitanceAreaButton)
    TextView omitanceAreaButton;
    @BindView(R.id.delinquentFarm)
    TextView delinquentFarm;


    @BindView(R.id.farmerNameTv)
    TextView farmerNameTv;
    @BindView(R.id.dobTv)
    TextView dobTv;
    @BindView(R.id.fatherNameTv)
    TextView fatherNameTv;
    @BindView(R.id.gender)
    TextView gender;
    @BindView(R.id.farmIdTv)
    TextView farmIdTv;
    @BindView(R.id.khasraTv)
    TextView khasraTv;

    @BindView(R.id.chakTv)
    TextView chakTv;
    @BindView(R.id.chakLeadCardMob)
    CardView chakLeadCardMob;
    @BindView(R.id.chakLeadNameTvMob)
    TextView chakLeadNameTvMob;

    private static final int UPDATE_FARM_REQUEST_CODE = 100;
    boolean isFromOther = false;
    RelativeLayout relWalAroundMode, relEasyMode, relPinDropMode, relWalAroundModeAutomatic;
    TextView walkAutoTv;
    ImageView walkAutoImg;
    float locationAccuracy = 10.0f;
    boolean isAutoClisk = false;

    public void showDialog(boolean showAutoCalculate) {
        float compAccuracy = 2.0f;
        compAccuracy = SharedPreferencesMethod.getFloatPref(context, SharedPreferencesMethod.COMP_AUTO_ACCURACY);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_select_capture_area_mode);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        relWalAroundMode = dialog.findViewById(R.id.relWalAroundMode);
        relWalAroundModeAutomatic = dialog.findViewById(R.id.relWalAroundModeAutomatic);
        relEasyMode = dialog.findViewById(R.id.relEasyMode);
        relPinDropMode = dialog.findViewById(R.id.relPinDropMode);
        walkAutoImg = dialog.findViewById(R.id.walkAutoImg);
        walkAutoTv = dialog.findViewById(R.id.walkAutoTv);
        if (!showAutoCalculate) {
            relEasyMode.setVisibility(View.GONE);
        } else {
            relEasyMode.setVisibility(View.VISIBLE);
        }

        if (locationAccuracy < compAccuracy) {
            isAutoClisk = true;
            walkAutoTv.setTextColor(getResources().getColor(R.color.black));
            walkAutoImg.setImageResource(R.drawable.ic_directions_walk_24px);
        } else {
            isAutoClisk = false;
            walkAutoTv.setTextColor(getResources().getColor(R.color.faded_text));
            walkAutoImg.setImageResource(R.drawable.ic_directions_walk_disabled_24px);
        }

        relWalAroundModeAutomatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAutoClisk) {
                    // Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(context, MapsActivity.class);
                    if (!showAutoCalculate)
                        intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE);
                    else
                        intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.ACTUAL_AREA);
                    intent.putExtra("isAutomaticMode", true);
                    ActivityOptions options = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                    } else {
                        startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                    }
                }
            }
        });

        relWalAroundMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, MapsActivity.class);
                if (!showAutoCalculate)
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE);
                else
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.ACTUAL_AREA);
                intent.putExtra("isAutomaticMode", false);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                } else {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
            }
        });
        relPinDropMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, MapsActivitySamunnati.class);
                if (!showAutoCalculate)
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE);
                else
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.ACTUAL_AREA);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                } else {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
            }
        });
        relEasyMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, MapsActivityEasyMode.class);
                if (!showAutoCalculate)
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.OMITANCE);
                else
                    intent.putExtra(AppConstants.MAP_AREA_SUBMIT_TYPE.TYPE, AppConstants.MAP_AREA_SUBMIT_TYPE.ACTUAL_AREA);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                } else {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
            }
        });
        dialog.show();


    }

    @BindView(R.id.farmerDetailsLabelTv)
    TextView farmerDetailsLabelTv;
    @BindView(R.id.areaFab)
    CircleImageView areaFab;
    //    @BindView(R.id.fabParent)
//    FloatingActionMenu fabParent;
//    @BindView(R.id.fabChildAddOmittanceArea)
//    FloatingActionButton fabChildAddOmittanceArea;
    @BindView(R.id.moreTv)
    TextView moreTv;


    @BindView(R.id.farmerNameCard)
    CardView farmerNameCard;
    @BindView(R.id.mobileCard)
    CardView mobileCard;
    @BindView(R.id.dobCard)
    CardView dobCard;
    @BindView(R.id.fatherCard)
    CardView fatherCard;
    @BindView(R.id.genderCard)
    CardView genderCard;
    @BindView(R.id.addlCard)
    CardView addlCard;
    @BindView(R.id.addL2Card)
    CardView addL2Card;
    @BindView(R.id.expAreaCard)
    CardView expAreaCard;
    @BindView(R.id.actAreaCard)
    CardView actAreaCard;
    @BindView(R.id.standigAreaCard)
    CardView standigAreaCard;
    @BindView(R.id.seedQtyCard)
    CardView seedQtyCard;
    @BindView(R.id.seedDateCard)
    CardView seedDateCard;
    @BindView(R.id.irriTypeCard)
    CardView irriTypeCard;
    @BindView(R.id.irriSourceCard)
    CardView irriSourceCard;
    @BindView(R.id.prevCropCard)
    CardView prevCropCard;
    @BindView(R.id.currentCropCard)
    CardView currentCropCard;
    @BindView(R.id.soilTypeCard)
    CardView soilTypeCard;
    @BindView(R.id.sowingDateCard)
    CardView sowingDateCard;
    @BindView(R.id.expFlowerCard)
    CardView expFlowerCard;
    @BindView(R.id.expHarvestCard)
    CardView expHarvestCard;
    @BindView(R.id.actFlowerCard)
    CardView actFlowerCard;
    @BindView(R.id.actHarvestCard)
    CardView actHarvestCard;
//    @BindView(R.id.mobileCard)
//    CardView mobileCard;

    @BindView(R.id.directionFarmTv)
    TextView directionFarmTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_details_vetting);

        context = this;
        resources = getResources();
        ButterKnife.bind(this);

        boolean isOmitanceArea = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_OMITANCE_AREA_ENABLED);
        boolean mode = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE);
//        isOmitanceArea=true;
        isFromOther = getIntent().getBooleanExtra("fromOther", false);

        if (isFromOther) {
//            fabParent.setVisibility(View.GONE);
            areaFab.setVisibility(View.GONE);
            farm_details_actual_harvest_date.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            farm_details_actual_flowering_date.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {

        }
        getFullDetails();

        if (!mode) {
            moreTv.setVisibility(View.VISIBLE);
            moreTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FarmDetailsUpdateActivity2.class);
                    intent.putExtra("isEditDisabled", true);
                    intent.putExtra("title", "" + resources.getString(R.string.farm_detail_label));
                    startActivity(intent);
                }
            });
        } else
            moreTv.setVisibility(View.GONE);


        areaFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                    GetGeoJsonData fetchGpsCordinates = new GetGeoJsonData(true,AppConstants.MAP_NAV_MODE.SHOW_AREA);
                    fetchGpsCordinates.execute();
                } else {
                    if (farmLatitude == null || farmLongitude == null) {
                        Intent intent = new Intent(context, OfflineMapActivity.class);
                        ActivityOptions options = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            startActivity(intent, options.toBundle());
                        } else {
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(context, OfflineMapActivity.class);
                        ActivityOptions options = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            startActivity(intent, options.toBundle());
                        } else {
                            startActivity(intent);
                        }
//                        viewFailDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.cant_view_farm_in_offline_msg));
//                        Toasty.info(context, "Farm coordinates taken, You can see area in online mode", Toast.LENGTH_LONG).show();
                    }
                    //Toast.makeText(context, "Yo can't view farm in offline mode. ", Toast.LENGTH_LONG).show();
                }
            }
        });
        if (!isFromOther && isOmitanceArea && !mode) {
            omitanceAreaButton.setVisibility(View.VISIBLE);
            omitanceAreaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    showDialog(false);
                    GetGeoJsonData fetchGpsCordinates = new GetGeoJsonData(false,AppConstants.MAP_NAV_MODE.SHOW_AREA);
                    fetchGpsCordinates.execute();
                }
            });

           /* fabChildAddOmittanceArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/
        } else {

//            areaFab.setVisibility(View.GONE);
            omitanceAreaButton.setVisibility(View.GONE);
//            fabChildAddOmittanceArea.setVisibility(View.GONE);
//            fabChildAddOmittanceArea.hideButtonInMenu(false);
            float weight = 1.5f;
            if (isFromOther) {
                update_farm_details_button.setVisibility(View.GONE);
                weight = 3.0f;
            } else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.5f);
                params.setMargins(5, 0, 5, 0);
                update_farm_details_button.setLayoutParams(params);
            }
            LinearLayout.LayoutParams params2;
            params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, weight);
            params2.setMargins(5, 0, 5, 0);
            view_farm_area_button.setLayoutParams(params2);


        }
        farmerDetailsLabelTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int[] textLocation = new int[2];
                    farmerDetailsLabelTv.getLocationOnScreen(textLocation);

                    if (event.getRawX() <= textLocation[0] + farmerDetailsLabelTv.getTotalPaddingLeft()) {

                        return true;
                    }


                    if (event.getRawX() >= textLocation[0] + farmerDetailsLabelTv.getWidth() - farmerDetailsLabelTv.getTotalPaddingRight()) {

                        // Right drawable was tapped
                        if (!isFromOther) {
                            farmerDetailsLabelTv.setClickable(false);
                            farmerDetailsLabelTv.setEnabled(false);


                            Intent intent = new Intent(context, FarmDetailsUpdateActivity2.class);
                            startActivityForResult(intent, UPDATE_FARM_REQUEST_CODE);

                        }

                        return true;
                    }
                }
                return true;
            }
        });

        drawer_back_img.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                FarmDetailsVettingActivity.this.onBackPressed();
            }
        });
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.dialog_verify_area_bottom_sheet);
        Window window2 = bottomSheetDialog.getWindow();
        window2.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view_farm_area_button.setText("");
        tv_easy_mode = bottomSheetDialog.findViewById(R.id.tv_easy_mode);
        tv_normal_mode = bottomSheetDialog.findViewById(R.id.tv_normal_mode);
        tv_easy_mode_ultra = bottomSheetDialog.findViewById(R.id.tv_easy_mode_ultra);

        /*if (compId != null && compId.equals("100075")) {
            tv_easy_mode_ultra.setVisibility(View.VISIBLE);
        } else {
            tv_easy_mode_ultra.setVisibility(View.GONE);
        }*/

        tv_easy_mode_ultra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                Intent intent = new Intent(context, MapsActivitySamunnati.class);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                } else {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
                bottomSheetDialog.dismiss();
            }
        });

        tv_easy_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                Intent intent = new Intent(context, MapsActivityEasyMode.class);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                } else {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
                bottomSheetDialog.dismiss();
            }
        });

        tv_normal_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toasty.info(context, resources.getString(R.string.need_to_mark_gps_coordinates), Toast.LENGTH_LONG, true).show();
                Intent intent = new Intent(context, MapsActivity.class);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                } else {
                    startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                }
                bottomSheetDialog.dismiss();
            }
        });


        fetchAllDataOnline();

        update_farm_details_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
//                    Toasty.error(context, "Currently farm details can not be updated in offline mode", Toast.LENGTH_LONG, false).show();
//                } else {
                if (!isFromOther) {
                    Intent intent = new Intent(context, FarmDetailsUpdateActivity2.class);
                    startActivityForResult(intent, UPDATE_FARM_REQUEST_CODE);
                }
            }
            //}
        });

        view_farm_area_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
                    GetGeoJsonData fetchGpsCordinates = new GetGeoJsonData(true,AppConstants.MAP_NAV_MODE.SHOW_AREA);
                    fetchGpsCordinates.execute();
                }
            }
        });


        rejectFarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectFarmButton.setClickable(false);
                reEntryFarmButton.setClickable(false);
                selectFarmButton.setClickable(false);
                updateVettingStatus(AppConstants.VETTING.REJECTED);
            }
        });

        delinquentFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                rejectFarmButton.setClickable(false);
//                reEntryFarmButton.setClickable(false);
//                selectFarmButton.setClickable(false);
//                updateVettingStatus(AppConstants.VETTING.REJECTED);

                startActivity(new Intent(context, AddDelinquentActivity.class));
            }
        });

        String vetting = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.FARM_VETTING);
        boolean isDelinq = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_DELINQUENT_COMP);
        if (isValidString(vetting) && vetting.equals(AppConstants.VETTING.DATA_ENTRY)) {
            reEntryFarmButton.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.5f);
            params.setMargins(5, 0, 5, 0);
            rejectFarmButton.setLayoutParams(params);

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.5f);
            params2.setMargins(5, 0, 5, 0);
            selectFarmButton.setLayoutParams(params2);
        }

        if (isValidString(vetting) && !vetting.equals(AppConstants.VETTING.DELINQUENT) && isDelinq) {
            delinquentFarm.setVisibility(View.VISIBLE);
        } else {
            delinquentFarm.setVisibility(View.GONE);
        }

        reEntryFarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectFarmButton.setClickable(false);
                reEntryFarmButton.setClickable(false);
                selectFarmButton.setClickable(false);
                updateVettingStatus(AppConstants.VETTING.DATA_ENTRY);
            }
        });
        selectFarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectFarmButton.setClickable(false);
                reEntryFarmButton.setClickable(false);
                selectFarmButton.setClickable(false);
                updateVettingStatus(AppConstants.VETTING.SELECTED);
            }
        });


        String loginTpye = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE);

        if ((isValidString(loginTpye) && !loginTpye.equals(AppConstants.ROLES.SUPERVISOR) && !loginTpye.equals(AppConstants.ROLES.FARMER)) || isFromOther) {
            selectFarmButton.setVisibility(View.VISIBLE);
            reEntryFarmButton.setVisibility(View.VISIBLE);
            rejectFarmButton.setVisibility(View.VISIBLE);
        } else {
            selectFarmButton.setVisibility(View.GONE);
            reEntryFarmButton.setVisibility(View.GONE);
            rejectFarmButton.setVisibility(View.GONE);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); //  minute interval
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i(TAG, "Location: " + location.getLatitude() + " " + location.getLongitude() + " Acc" + location.getAccuracy());
                currentLocation = location;
                if (currentLocation != null)
                    locationAccuracy = currentLocation.getAccuracy();

            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        farmerDetailsLabelTv.setClickable(true);
        farmerDetailsLabelTv.setEnabled(true);
        DataHandler.newInstance().setLatLngList(null);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                //Request Location Permission
//                checkLocationPermission();
            }
        } else {
            fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fusedLocationProviderClient != null && mLocationCallback != null)
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @BindView(R.id.seed_provided_on_tv)
    TextView seedProvidedOnTv;
    @BindView(R.id.farm_details_expected_area_tv)
    TextView farm_details_expected_area_tv;
    @BindView(R.id.farm_details_actual_area_tv)
    TextView farm_details_actual_area_tv;
    @BindView(R.id.farm_details_standing_area_tv)
    TextView farm_details_standing_area_tv;

    @BindView(R.id.farm_details_irrigationSource)
    TextView farm_details_irrigationSource;
    @BindView(R.id.farm_details_irrigationType)
    TextView farm_details_irrigationType;
    @BindView(R.id.farm_details_previousCrop)
    TextView farm_details_previousCrop;
    @BindView(R.id.farm_details_soilType)
    TextView farm_details_soilType;
    @BindView(R.id.farm_details_sowingDate)
    TextView farm_details_sowingDate;
    @BindView(R.id.farm_details_expFloweringDate)
    TextView farm_details_expFloweringDate;
    @BindView(R.id.add_details_expHarvestDate)
    TextView add_details_expHarvestDate;
    String mobNo = "-";
    String navigateTo = "";

    @BindView(R.id.connectivityLine)
    View connectivityLine;

    private void setFarmDataOnline(final FetchFarmResult farmData) {
        DataHandler.newInstance().setFetchFarmResult(farmData);
        if (farmData.getQtySeedProvided() != null) {
            seedQtyTv.setText(farmData.getQtySeedProvided());
        } else {
            hideCard(seedQtyCard);
        }
        if (farmData.getSeedProvidedOn() != null && !farmData.getSeedProvidedOn().equals(for_date)) {
            seedProvidedOnTv.setText(AppConstants.getShowableDate(farmData.getSeedProvidedOn(), context));
        } else {
            hideCard(seedDateCard);
        }
        if (farmData.getLatCord() != null && farmData.getLongCord() != null && !TextUtils.isEmpty(farmData.getLatCord()) && !TextUtils.isEmpty(farmData.getLongCord())) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARM_LAT, farmData.getLatCord());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARM_LONG, farmData.getLongCord());
        }

        String soil_type = "";
        //String avg_germination = "0";
        String irrigationSource = "";
        String previousCrop = "";
        String irrigationtype = "";
        String sowingDate = "";
        String expFloweringDate = "";
        String expHarvestDate = "";
        String actual_area = "0";
        String standing_area = "0";
        String expected_area = "0";

        if (farmData.getLotNo() != null) {
            ///farm_lot_no.setText(farmData.getLotNo());
            title_name_d.setText(farmData.getLotNo());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.LOT_NO, farmData.getLotNo());
        }
        if (farmData.getName() != null) {
            title_address_d.setText(farmData.getName());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARMER_NAME, farmData.getName());
            farmerNameTv.setText(farmData.getName());
        } else {
            farmerNameTv.setText("-");
            hideCard(farmerNameCard);
        }
        if (farmData.getMob() != null && !farmData.getMob().equals("9999999999")) {
            mobNo = farmData.getMob();
        } else {
            mobNo = "-";
            hideCard(mobileCard);
        }
        if (farmData != null && farmData.getCropName() != null) {
            currentCropTv.setText(farmData.getCropName());
        } else {
            hideCard(currentCropCard);
        }

        if (isValidString(farmData.getActualFloweringDate())) {
            if (!farmData.getActualFloweringDate().equals(for_date)) {
                farm_details_actual_flowering_date.setText(AppConstants.getShowableDate(farmData.getActualFloweringDate(), context));
            } else {
                hideCard(actFlowerCard);
            }
        } else {
            hideCard(actFlowerCard);
        }
        if (farmData.getActualHarvestDate() != null) {
            if (!farmData.getActualHarvestDate().equals(for_date)) {
                farm_details_actual_harvest_date.setText(AppConstants.getShowableDate(farmData.getActualHarvestDate(), context));
            } else
                hideCard(actHarvestCard);
        } else {
            hideCard(actHarvestCard);
        }
        if (farmData != null) {
            if (farmData.getSoilType() != null) {
                soil_type = farmData.getSoilType().toString();
            }
            if (farmData.getActualArea() != null && farmData.getActualArea() != "0") {
                actual_area = farmData.getActualArea().toString();
            }

            if (farmData.getIrrigationSource() != null) {
                irrigationSource = farmData.getIrrigationSource().toString();
            }
            if (farmData.getMob() != null && !farmData.getMob().equals("9999999999")) {
                mobNo = farmData.getMob().toString();
            }
            if (farmData.getPreviousCrop() != null) {
                previousCrop = farmData.getPreviousCrop().toString();
            }
            if (farmData.getIrrigationType() != null) {
                irrigationtype = farmData.getIrrigationType().toString();
            }
            if (farmData.getSowingDate() != null && !farmData.getSowingDate().equals(for_date)) {
                sowingDate = AppConstants.getShowableDate(farmData.getSowingDate(), context);
            } else {
//                showDialogForSowingDate(resources.getString(R.string.add_sowing_date_msg));
            }
            if (farmData.getExpFloweringDate() != null && !farmData.getExpFloweringDate().equals(for_date)) {
                expFloweringDate = AppConstants.getShowableDate(farmData.getExpFloweringDate(), context);
            }
            if (farmData.getExpHarvestDate() != null && !farmData.getExpHarvestDate().equals(for_date)) {
                expHarvestDate = AppConstants.getShowableDate(farmData.getExpHarvestDate(), context);
            }
            if (farmData.getStandingAcres() != null) {
                standing_area = farmData.getStandingAcres();
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, standing_area);
            }
            if (farmData.getExpArea() != null) {
                expected_area = farmData.getExpArea();
            }
        }
        if (AppConstants.isValidActualArea(actual_area))
            farm_details_actual_area_tv.setText(convertAreaTo(actual_area));
        else
            hideCard(actAreaCard);
        if (AppConstants.isValidActualArea(standing_area))
            farm_details_standing_area_tv.setText(standing_area);
        else
            hideCard(standigAreaCard);
        if (AppConstants.isValidActualArea(expected_area))
            farm_details_expected_area_tv.setText(convertAreaTo(expected_area));
        else
            hideCard(expAreaCard);
        if (isValidString(irrigationSource))
            farm_details_irrigationSource.setText(irrigationSource);
        else
            hideCard(irriSourceCard);
        if (isValidString(irrigationtype))
            farm_details_irrigationType.setText(irrigationtype);
        else
            hideCard(irriTypeCard);
        if (isValidString(previousCrop))
            farm_details_previousCrop.setText(previousCrop);
        else
            hideCard(prevCropCard);
        if (isValidString(soil_type))
            farm_details_soilType.setText(soil_type);
        else
            hideCard(soilTypeCard);
        if (mobNo != null && !mobNo.equals("0") && !mobNo.equals("9999999999"))
            farm_details_mob_number.setText(mobNo);
        else
            farm_details_mob_number.setText("-");
        if (isValidString(sowingDate))
            farm_details_sowingDate.setText(sowingDate);
        else
            hideCard(sowingDateCard);
        if (isValidString(expFloweringDate))
            farm_details_expFloweringDate.setText(expFloweringDate);
        else
            hideCard(expFlowerCard);
        if (isValidString(expHarvestDate))
            add_details_expHarvestDate.setText(expHarvestDate);
        else
            hideCard(expHarvestCard);
        if (!AppConstants.isValidActualArea(actual_area)) {
            view_farm_area_button.setText(resources.getString(R.string.capture_farm_area_label));
            navigateTo = "map";
            if (isFromOther) {
                view_farm_area_button.setVisibility(View.GONE);
                update_farm_details_button.setVisibility(View.GONE);
            }
            directionFarmTv.setVisibility(View.GONE);
//            Toast.makeText(context, "eeeeer", Toast.LENGTH_SHORT).show();
        } else {
            navigateTo = "view_details";
            view_farm_area_button.setText(resources.getString(R.string.farm_details_view_area));
//            if (!isFromOther) {
            directionFarmTv.setVisibility(View.VISIBLE);
            directionFarmTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetGeoJsonData fetchGpsCordinates2 = new GetGeoJsonData(true,AppConstants.MAP_NAV_MODE.SHOW_NAVIGATION);
                    fetchGpsCordinates2.execute();
                }
            });

//            }
        }

        farm_details_mob_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mobNo.equals("-") && !mobNo.equals("9999999999")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mobNo));
                    startActivity(intent);
                }
            }
        });

        if (farmData.getLatCord() != null && farmData.getLongCord() != null && !TextUtils.isEmpty(farmData.getLatCord())
                && !TextUtils.isEmpty(farmData.getLongCord()) && !farmData.getLatCord().trim().equals("0") && !farmData.getLongCord().trim().equals("0")) {
            farmLatitude = farmData.getLatCord();
            farmLongitude = farmData.getLongCord();
            if (actual_area == null || TextUtils.isEmpty(actual_area) || actual_area.trim().equals("0")) {
                view_farm_area_button.setText(resources.getString(R.string.verify_farm_area_label));
            }
        } else {
            farmLatitude = null;
            farmLongitude = null;
//            Toast.makeText(context, "nullll "+farmData.getLatCord()+" "+farmData.getLongCord(), Toast.LENGTH_SHORT).show();
        }
    }

    String farmLatitude, farmLongitude;
    Calendar myCalendarAddFarmflowering = Calendar.getInstance();
    Calendar myCalendarAddFarmHarvest = Calendar.getInstance();
    @BindView(R.id.farm_details_mob_number)
    TextView farm_details_mob_number;

    private String convertAreaTo(String area) {
        return AppConstants.getShowableArea(area);
    }

    private void updateVettingStatus(String isSelected) {
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            try {
                String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
                String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
                String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
                String farmID = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
                Call<StatusMsgModel> statusMsgModelCall = apiService.updateVettingStatus(userId, token, compId, farmID, isSelected);
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<StatusMsgModel>() {
                    @Override
                    public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                        String error = null;
                        isLoaded[0] = true;
                        Log.e(TAG, "Status act flow " + response.code());
                        if (response.isSuccessful()) {
                            rejectFarmButton.setClickable(true);
                            reEntryFarmButton.setClickable(true);
                            selectFarmButton.setClickable(true);

//                            Log.e(TAG, "Update Flower res " + new Gson().toJson(response.body()));
                            Log.e(TAG, "Update Flower res " + response.body());

                            StatusMsgModel statusMsgModel = response.body();
                            if (response.body().getStatus() == 10) {
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, response.body().getMsg());
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                            } else if (statusMsgModel.getStatus() == 1) {
                                //Toast.makeText(context, resources.getString(R.string.flowering_date_updated_success_msg), Toast.LENGTH_LONG).show();
                                if (isSelected.equals(AppConstants.VETTING.SELECTED))
                                    Toasty.success(context, resources.getString(R.string.farm_selected), Toast.LENGTH_SHORT, true).show();
                                else if (isSelected.equals(AppConstants.VETTING.REJECTED))
                                    Toasty.success(context, resources.getString(R.string.farm_rejected), Toast.LENGTH_SHORT, true).show();
                                else if (isSelected.equals(AppConstants.VETTING.DATA_ENTRY))
                                    Toasty.success(context, resources.getString(R.string.farm_re_entry), Toast.LENGTH_SHORT, true).show();
//                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARM_VETTING, isSelected);

                                Intent intent = new Intent();
                                intent.putExtra("value", true);
                                intent.putExtra("value1", isSelected);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showDialog(context, resources.getString(R.string.opps_message), resources.getString(R.string.something_went_wrong_msg));

                                    }
                                });

                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                        } else {
                            try {

                                error = response.errorBody().string().toString();

                                Log.e("Error", response.errorBody().string().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                            notifyApiDelay(context, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusMsgModel> call, Throwable t) {

                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        notifyApiDelay(context, call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);
                        Log.e(TAG, "failure update status" + t.toString());
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
        } else {
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
                            ConnectivityUtils.checkSpeedWithColor(context, new ConnectivityUtils.ColorListener() {
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
                }
            }, AppConstants.DELAY);
        } catch (Exception e) {

        }

    }

    private void fetchAllDataOnline() {
        String farm_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        Log.e(TAG, "fetchAllDataOnline farmId " + farm_id + " And CompId " + comp_id);
        Log.e(TAG, "fetchAllDataOnline UserId " + userId);

        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        JsonObject object=new JsonObject();
        object.addProperty("farm_id",""+farm_id);
        object.addProperty("comp_id",""+comp_id);
        object.addProperty("user_id",""+userId);
        object.addProperty("token",""+token);
        Log.e(TAG, "fetchAllDataOnline Token " + new Gson().toJson(object));
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        Call<FullDetailsResponse> farmAndHarvestCall = apiInterface.getFarmAndHarvestData(farm_id, comp_id, userId, token);
        farmAndHarvestCall.enqueue(new Callback<FullDetailsResponse>() {
            @Override
            public void onResponse(Call<FullDetailsResponse> call, Response<FullDetailsResponse> response) {
                Log.e(TAG, "fetchAllDataOnline Status farm " + response.code());
                isLoaded[0] = true;
                String error = "";
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                        } else {

                            FullDetailsResponse detailsResponse = response.body();
                            Log.e(TAG, "Status farm " + new Gson().toJson(detailsResponse));

                            if (detailsResponse.getFarmData() != null) {
                                FetchFarmResult fetchFarmResult = detailsResponse.getFarmData();
                                fetchFarmResult.setType(AppConstants.TIMELINE.FARM);
                                String date = "2010-01-21";
SharedPreferencesMethod.setString(context,SharedPreferencesMethod.FARM_CLUSTERID,detailsResponse.getFarmData().getClusterId());
                                if (fetchFarmResult.getDoa() != null && !TextUtils.isEmpty(fetchFarmResult.getDoa()))
                                    date = fetchFarmResult.getDoa();
                                else fetchFarmResult.setDoa(date);

                                setFarmDataOnline(fetchFarmResult);
                                if (detailsResponse.getFarmData().getActualArea() != null) {
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTUAL_AREA, detailsResponse.getFarmData().getActualArea().trim());
                                }
                                if (detailsResponse.getFarmData().getStandingAcres() != null) {
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, detailsResponse.getFarmData().getStandingAcres().trim());
                                }
                                if (detailsResponse.getFarmData().getExpArea() != null) {
                                    SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.EXPECTED_AREA,
                                            Float.valueOf(detailsResponse.getFarmData().getExpArea()));
                                }
                                if (detailsResponse.getFarmData().getGermination() != null &&
                                        !TextUtils.isEmpty(detailsResponse.getFarmData().getGermination())
                                        && !detailsResponse.getFarmData().getGermination().equals("[]")) {
                                    SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.GERMINATION,
                                            Float.valueOf(detailsResponse.getFarmData().getGermination().trim()));
                                } else {
                                    SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.GERMINATION,
                                            0.0);
                                }


                            }

                        }
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "fetchAllDataOnline Unsuccess " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FullDetailsResponse> call, Throwable t) {
                Log.e(TAG,"fetchAllDataOnline "+t.toString());
            }
        });

    }

    /*private class FetchGpsCordinates extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;
        boolean isShowEasyMode = false;

        public FetchGpsCordinates(boolean isShowEasyMode) {
            super();
            dialog = new ProgressDialog(context);
            DataHandler.newInstance().setLatLngList(null);
            this.isShowEasyMode = isShowEasyMode;
            dialog.setMessage(getResources().getString(R.string.please_wait_msg));
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            Call<GetGpsCoordinates> getGpsCoordinatesCall = apiService.getGpsCoordinates(
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID), userId, token);
            getGpsCoordinatesCall.enqueue(new Callback<GetGpsCoordinates>() {

                @Override
                public void onResponse(Call<GetGpsCoordinates> call, Response<GetGpsCoordinates> response) {
                    Log.e(TAG, "Status coords " + response.code());
                    if (response.isSuccessful()) {
                        GetGpsCoordinates getGpsCoordinates = response.body();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog != null)
                                    dialog.cancel();
                            }
                        });
                        if (getGpsCoordinates.getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                        } else if (getGpsCoordinates.getStatus() != 0) {
                            List<LatLngFarm> latLngList = getGpsCoordinates.getData();
                            String[] latAraay = new String[latLngList.size()];
                            String[] lngArray = new String[latLngList.size()];
                            for (int i = 0; i < latLngList.size(); i++) {
                                latAraay[i] = latLngList.get(i).getLat();
                                lngArray[i] = latLngList.get(i).getLng();
                                Log.e(TAG, "ShowFarmAreaCoordinates " + latLngList.get(i).getLat() + " " + latLngList.get(i).getLng());
                            }
                            com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(Double.valueOf(latAraay[0]), Double.valueOf(lngArray[0]));
                            DataHandler.newInstance().setLatLngList(latLngList);
                            if (isShowEasyMode) {
                                Intent intent = new Intent(context, ShowAreaWithUpdateActivity.class);
                                intent.putExtra("update", true);
                                intent.putExtra("canBeUpdated", true);
                                intent.putExtra("fromOther", isFromOther);
                                startActivity(intent);
                            } else {
                                if (isFromOther) {
                                    Toasty.error(context, "You can only change vetting status of farm", Toast.LENGTH_SHORT, false).show();
                                } else
                                    showDialog(false);
                            }

                        } else if (navigateTo.toLowerCase().trim().equals("map")) {
                            Log.e(TAG, "ShowFarmAreaCoordinates " + new Gson().toJson(response.body()));

                            if (isFromOther) {
                                Toasty.error(context, "You can only change vetting status of farm", Toast.LENGTH_SHORT, false).show();
                            } else if (isShowEasyMode) {

                                if (farmLatitude != null && farmLongitude != null) {
                                    Intent intent = new Intent(context, MapsActivityEasyMode.class);
                                    intent.putExtra("farmLat", farmLatitude);
                                    intent.putExtra("farmLon", farmLongitude);
                                    ActivityOptions options = null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                                    } else {
                                        startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                                    }
                                } else
                                    showDialog(true);

                            }

                           *//* Toasty.error(context,"You can only change vetting status of farm",Toast.LENGTH_SHORT,false).show();
                            if (isShowEasyMode) {
                                if (farmLatitude != null && farmLongitude != null) {
                                    Intent intent = new Intent(context, MapsActivityEasyMode.class);
                                    intent.putExtra("farmLat", farmLatitude);
                                    intent.putExtra("farmLon", farmLongitude);
                                    ActivityOptions options = null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE, options.toBundle());
                                    } else {
                                        startActivityForResult(intent, WHOLE_DATA_CHANGE_REQUEST_CODE);
                                    }
                                } else {
                                    showDialog(true);
                                }
                            } else {
                                Toasty.error(context, "Please mark farm area before you can mark omitance area", Toast.LENGTH_LONG, false).show();
                            }*//*
                        } else {
                            showDialog(true);
//                            Toasty.error(context, resources.getString(R.string.area_entere_manually_msg), Toast.LENGTH_LONG, false).show();
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog != null)
                                    dialog.cancel();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<GetGpsCoordinates> call, Throwable t) {
                    //Toast.makeText(context, "Server Error. Please try again", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, t.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog != null)
                                dialog.cancel();
                        }
                    });
                }

            });


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

    }*/

    private class GetGeoJsonData extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;
        boolean isShowEasyMode = false;
        int mode=1;

        public GetGeoJsonData(boolean isShowEasyMode, int mode) {
            super();
            dialog = new ProgressDialog(context);
            DataHandler.newInstance().setLatLngList(null);
            this.isShowEasyMode = isShowEasyMode;
            this.mode=mode;
            dialog.setMessage(getResources().getString(R.string.please_wait_msg));
            dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {

            //demosupervisor1
            try {
                String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
                ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("user_id",""+SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
                jsonObject.addProperty("token",""+SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
                jsonObject.addProperty("comp_id",""+SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID));
                jsonObject.addProperty("farm_id",""+SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID));
//                jsonObject.addProperty("farm_id","20202");
                Call<GeoJsonResponse> statusMsgModelCall = apiService.getFarmGeoJson(jsonObject);
                Log.e(TAG, "Sending Data " + new Gson().toJson(jsonObject));
                final boolean[] isLoaded = {false};
                long currMillis = AppConstants.getCurrentMills();
                statusMsgModelCall.enqueue(new Callback<GeoJsonResponse>() {
                    @Override
                    public void onResponse(Call<GeoJsonResponse> call, retrofit2.Response<GeoJsonResponse> response) {
                        String error = null;
                        isLoaded[0] = true;
                        if (response.isSuccessful()) {
                            Log.e("Area Response", new Gson().toJson(response.body()));
                            GeoJsonResponse statusMsgModel = response.body();
                            if (statusMsgModel.getStatus() == 1) {
                                if (statusMsgModel.getData()!=null&&statusMsgModel.getData().size()>0){
                                    dialog.cancel();
                                    Intent intent = new Intent(context, ShowAreaWithUpdateActivity.class);
                                    if (mode==AppConstants.MAP_NAV_MODE.SHOW_NAVIGATION){
                                        intent = new Intent(context, FarmNavigationActivity.class);
                                    }
                                    startActivity(intent);
                                }else {
                                    if (mode==AppConstants.MAP_NAV_MODE.SHOW_NAVIGATION){
                                        Toasty.error(context, "Farm location not available", Toast.LENGTH_LONG, false).show();
                                    }else
                                        showDialog(true);
                                }
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                                //un authorized access
                                dialog.cancel();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(FarmDetailsVettingActivity.this, resources.getString(R.string.unauthorized_access));
                            } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                                // auth token expired
                                dialog.cancel();
                                ViewFailDialog viewFailDialog = new ViewFailDialog();
                                viewFailDialog.showSessionExpireDialog(FarmDetailsVettingActivity.this, resources.getString(R.string.authorization_expired));
                            } else {
                                dialog.cancel();
                                if (mode==AppConstants.MAP_NAV_MODE.SHOW_NAVIGATION){
                                    Toasty.error(context, "Farm location not available", Toast.LENGTH_LONG, false).show();
                                }else
                                    showDialog(true);
                            }
                        } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            dialog.cancel();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(FarmDetailsVettingActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            dialog.cancel();
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(FarmDetailsVettingActivity.this, resources.getString(R.string.authorization_expired));
                        } else {
                            try {
                                error = response.errorBody().string().toString();
                                Toasty.error(context, "Farm location not available", Toast.LENGTH_LONG, false).show();
                                Log.e(TAG, "Error " + error);
                                dialog.cancel();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                            notifyApiDelay(FarmDetailsVettingActivity.this, response.raw().request().url().toString(),
                                    "" + diff, internetSPeed, error, response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<GeoJsonResponse> call, Throwable t) {
                        Log.e(TAG, "Failure " + t.toString());
                        isLoaded[0] = true;
                        dialog.cancel();
                        long newMillis = AppConstants.getCurrentMills();
                        long diff = newMillis - currMillis;
                        notifyApiDelay(FarmDetailsVettingActivity.this, call.request().url().toString(),
                                "" + diff, internetSPeed, t.toString(), 0);
                        Toasty.error(context, "Farm location not available", Toast.LENGTH_LONG, false).show();

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

    private static final int WHOLE_DATA_CHANGE_REQUEST_CODE = 101;
    BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_FARM_REQUEST_CODE && resultCode == RESULT_OK && !SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            fetchAllDataOnline();
        } else if (requestCode == WHOLE_DATA_CHANGE_REQUEST_CODE && resultCode == RESULT_OK && !SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            Log.e(TAG, "onActivityResultSecond");
//            progressBar.setVisibility(View.VISIBLE);
            fetchAllDataOnline();
        }
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
                    DataHandler.newInstance().setFarmFullDetails(response1.body());
                    FarmFullDetails details = response1.body();
                    try {
                        if (details.getPersonDetails() != null) {
                            PersonDetails personDetails = details.getPersonDetails();

                            if (AppFeatures.isChakLeaderEnabled() && details.getFarmAddressDetails() != null) {
                                if (isValidString(details.getFarmAddressDetails().getAddL2())) {
                                    getChakLeader("" + details.getPersonDetails().getName(), details.getPersonDetails().getPersonId() + "", details.getFarmAddressDetails().getAddL2());
                                }
                            }
                            if (details.getPersonDetails() != null) {
                                if (isValidString(details.getPersonDetails().getImgLink())) {
                                    JSONObject object1 = new JSONObject();
                                    object1.put("type", AppConstants.DOCS.PROFILE);
                                    object1.put("link", personDetails.getImgLink());
                                }
                                if (isValidString(personDetails.getGender())) {
                                    if (personDetails.getGender().equalsIgnoreCase("m")) {
                                        gender.setText(resources.getString(R.string.male_label));
                                    } else if (personDetails.getGender().equalsIgnoreCase("f")) {
                                        gender.setText(resources.getString(R.string.female_label));
                                    } else if (personDetails.getGender().equalsIgnoreCase("o")) {
                                        gender.setText(resources.getString(R.string.other_labe));
                                    } else {
                                        gender.setText("-");
                                        hideCard(genderCard);
                                    }
                                } else {
                                    gender.setText("-");
                                    hideCard(genderCard);
                                }
                                if (isValidString(personDetails.getDob()) && !personDetails.getDob().equals(for_date)) {
                                    dobTv.setText(AppConstants.getShowableDate(personDetails.getDob(), context));
                                } else {
                                    dobTv.setText("-");
                                    hideCard(dobCard);
                                }
                                if (isValidString(personDetails.getFatherName())) {
                                    fatherNameTv.setText(personDetails.getFatherName());
                                } else {
                                    fatherNameTv.setText("-");
                                    hideCard(fatherCard);
                                }

//                                imageList.add(object1);
                            }

                            if (isValidString(details.getPersonDetails().getIdProof())) {
//                            imageList.put(AppConstants.DOCS.ID_PROOF,details.getPersonDetails().getIdProof());

                                JSONObject object1 = new JSONObject();
                                object1.put("type", AppConstants.DOCS.ID_PROOF);
                                object1.put("link", details.getPersonDetails().getIdProof());
//                                imageList.add(object1);
                            }
                            if (isValidString(details.getPersonDetails().getAddressProof())) {
//                            imageList.put(AppConstants.DOCS.ADDRESS_PROOF,details.getPersonDetails().getAddressProof());
                                JSONObject object1 = new JSONObject();
                                object1.put("type", AppConstants.DOCS.ADDRESS_PROOF);
                                object1.put("link", details.getPersonDetails().getAddressProof());
//                                imageList.add(object1);
                            }
                        }
                        if (details.getFarmsDetails() != null) {
                            Log.e(TAG,"FULL FARM "+new Gson().toJson(details.getFarmsDetails()));
                            if (isValidString(details.getFarmsDetails().getOwnershipDoc())) {
                                {

                                    JSONObject object1 = new JSONObject();
                                    object1.put("type", AppConstants.DOCS.OWNERSHIP);
                                    object1.put("link", details.getFarmsDetails().getOwnershipDoc());
//                                    imageList.add(object1);
                                }
                            }
                            if (isValidString(details.getFarmsDetails().getFarmPhoto())) {
                                JSONObject object1 = new JSONObject();
                                object1.put("type", AppConstants.DOCS.FARM);
                                object1.put("link", details.getFarmsDetails().getFarmPhoto());
//                                imageList.add(object1);
                            }
                        }

                        if (details.getFarmCropDetails() != null) {
                            Log.e(TAG,"FULL CROP "+new Gson().toJson(details.getFarmCropDetails()));
                            FarmsDetails farmsDetails = details.getFarmsDetails();
                            if (isValidString(farmsDetails.getCompFarmId())) {
                                farmIdTv.setText(farmsDetails.getCompFarmId());
                            } else {
                                farmIdTv.setText("-");
                            }

                        }

                        if (details.getFarmAddressDetails() != null) {
                            FarmAddressDetails addressDetails = details.getFarmAddressDetails();
                            if (isValidString(addressDetails.getAddL1()) && !addressDetails.getAddL1().equals("0")) {
                                khasraTv.setText(addressDetails.getAddL1());
                            } else {
                                khasraTv.setText("-");
                                hideCard(addlCard);
                            }

                            if (isValidString(addressDetails.getAddL2()) && !addressDetails.getAddL2().equals("0")) {
                                chakTv.setText(addressDetails.getAddL2());
                            } else {
                                chakTv.setText("-");
                                hideCard(addL2Card);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    /*if (imageList.size() > 0) {
                        DataHandler.newInstance().setImageListDoc(imageList);
                        docImage.setVisibility(View.GONE);
                        docImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(context, DocumentActivity.class));
                            }
                        });
                    } else {
                        docImage.setVisibility(View.GONE);
                    }*/


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

    private void getChakLeader(String name, String farmerId, String chak) {
        String farm_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", "" + userId);
        jsonObject.addProperty("token", "" + token);
        jsonObject.addProperty("comp_id", "" + comp_id);
        jsonObject.addProperty("farm_id", "" + farm_id);
//        jsonObject.addProperty("addl2",""+chak);
        Log.e(TAG, "getChakLeader REQ " + new Gson().toJson(jsonObject));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        Call<FetchChakResponse> farmAndHarvestCall = apiInterface.getChakLeader(jsonObject);
        farmAndHarvestCall.enqueue(new Callback<FetchChakResponse>() {
            @Override
            public void onResponse(Call<FetchChakResponse> call, Response<FetchChakResponse> response) {
                Log.e(TAG, "getChakLeader farm " + response.code());
                isLoaded[0] = true;
                String error = "";
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "getChakLeader RES " + new Gson().toJson(response.body()));
                        FetchChakResponse chakResponse = response.body();
                        if (chakResponse.getData() == null || chakResponse.getData().size() == 0) {
                            if (isValidString(chak) && !chak.trim().equals("0"))
                                showChakLeaderDialog(name, farmerId, chak);
                        } else {
                            FetchChakDatum datum = chakResponse.getData().get(0);
                            if (!isValidString(datum.getLeadChak())) {
                                if (isValidString(chak) && !chak.trim().equals("0"))
                                    showChakLeaderDialog(name, farmerId, chak);
                            } else {
                                chakLeadCard.setVisibility(View.VISIBLE);
                                chakLeadCardMob.setVisibility(View.VISIBLE);
                                if (isValidString(datum.getName()))
                                    chakLeadNameTv.setText(datum.getName());
                                else
                                    chakLeadNameTv.setText("-");

                                if (isValidString(datum.getMob()))
                                    chakLeadNameTvMob.setText(datum.getMob());
                                else {
                                    chakLeadNameTvMob.setText("-");
                                    hideCard(chakLeadCardMob);
                                }
                            }
                        }
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(FarmDetailsVettingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(FarmDetailsVettingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
//                        isaFarmLoaded = false;
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "getChakLeader Unsuccess " + error);

                    } catch (IOException e) {
                        e.printStackTrace();
//                        isaFarmLoaded = false;
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(FarmDetailsVettingActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<FetchChakResponse> call, Throwable t) {
//                isaFarmLoaded = false;
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(FarmDetailsVettingActivity.this, call.request().url().toString(),
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

    }

    private void setChakLeader(String farmerId, String chak, String name) {
        String farm_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        String comp_id = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comp_id", "" + comp_id);
        jsonObject.addProperty("user_id", "" + userId);
        jsonObject.addProperty("token", "" + token);
        jsonObject.addProperty("farm_id", "" + farm_id);
        jsonObject.addProperty("addL2", "" + chak);
        jsonObject.addProperty("farmer_id", "" + farmerId);
        Log.e(TAG, "setChakLeader REQ " + new Gson().toJson(jsonObject));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        String auth = SharedPreferencesMethod.getString(FarmDetailsVettingActivity.this, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        Call<StatusMsgModel> farmAndHarvestCall = apiInterface.setChakLeader(jsonObject);
        farmAndHarvestCall.enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                Log.e(TAG, "setChakLeader farm " + response.code());
                isLoaded[0] = true;
                String error = "";
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "setChakLeader RES " + new Gson().toJson(response.body()));
                        Toasty.success(context, name + " " + "assigned as chak leader", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(FarmDetailsVettingActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(FarmDetailsVettingActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
//                        isaFarmLoaded = false;
                        error = response.errorBody().string().toString();
                        Log.e(TAG, "setChakLeader Unsuccess " + error);

                    } catch (IOException e) {
                        e.printStackTrace();
//                        isaFarmLoaded = false;
                    }
                }

                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(FarmDetailsVettingActivity.this, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
//                isaFarmLoaded = false;
                isLoaded[0] = true;
                Log.e(TAG, "setChakLeader fail " + t.toString());
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(FarmDetailsVettingActivity.this, call.request().url().toString(),
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

    }

    private void showChakLeaderDialog(String name, String farmerId, String chak) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_chak_leader);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView title = dialog.findViewById(R.id.title);
        ImageView close = dialog.findViewById(R.id.close);
        TextView msgTv = dialog.findViewById(R.id.msgTv);
        Button markButton = dialog.findViewById(R.id.markButton);
        Button noButton = dialog.findViewById(R.id.noButton);

//        String msg = "" + resources.getString(R.string.mark) + " " + "<b>" + name + "</b> " + " " + resources.getString(R.string.as_chak_leader) + " " + "<b>" + chak + "</b> ";
        String msg = "" + resources.getString(R.string.mark) + " " + "<b>" + name + "</b> " + " " + resources.getString(R.string.as_chak_leader) + "?";

        msgTv.setText(Html.fromHtml(msg));

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setChakLeader(farmerId, chak, name);
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    private void hideCard(CardView cardView) {
        if (cardView != null)
            cardView.setVisibility(View.GONE);
    }

    /*private class FetchGpsCordinates2 extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        public FetchGpsCordinates2() {
            super();
            DataHandler.newInstance().setLatLngList(null);
            dialog = new ProgressDialog(context);
            dialog.setMessage(getResources().getString(R.string.please_wait_msg));
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
            String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
            String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
            ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
            Call<GetGpsCoordinates> getGpsCoordinatesCall = apiService.getGpsCoordinates(
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID),
                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID), userId, token);
            getGpsCoordinatesCall.enqueue(new Callback<GetGpsCoordinates>() {
                @Override
                public void onResponse(Call<GetGpsCoordinates> call, Response<GetGpsCoordinates> response) {
                    Log.e(TAG, "Status coords " + response.code());
                    if (response.isSuccessful()) {
                        GetGpsCoordinates getGpsCoordinates = response.body();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog != null)
                                    dialog.cancel();
                            }
                        });
                        if (getGpsCoordinates.getStatus() == 10) {
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(FarmDetailsVettingActivity.this, resources.getString(R.string.multiple_login_msg));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                            //un authorized access
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(FarmDetailsVettingActivity.this, resources.getString(R.string.unauthorized_access));
                        } else if (response.body().getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                            // auth token expired
                            ViewFailDialog viewFailDialog = new ViewFailDialog();
                            viewFailDialog.showSessionExpireDialog(FarmDetailsVettingActivity.this, resources.getString(R.string.authorization_expired));
                        } else if (getGpsCoordinates.getStatus() != 0) {

                            List<LatLngFarm> latLngList = getGpsCoordinates.getData();
                            if (latLngList != null && latLngList.size() > 0) {

                                DataHandler.newInstance().setLatLngList(latLngList);
                                Intent intent = new Intent(context, FarmNavigationActivity.class);
                                ActivityOptions options = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                                }
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    startActivity(intent, options.toBundle());
                                } else {
                                    startActivity(intent);
                                }
                            } else {
                                Toasty.error(context, "Farm location not available", Toast.LENGTH_LONG, false).show();
                            }
                        } else {
                            Toasty.error(context, "Farm location not available", Toast.LENGTH_LONG, false).show();
                        }
                    } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(FarmDetailsVettingActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(FarmDetailsVettingActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog != null)
                                    dialog.cancel();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<GetGpsCoordinates> call, Throwable t) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog != null)
                                dialog.cancel();
                        }
                    });
                }

            });


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

    }*/
}