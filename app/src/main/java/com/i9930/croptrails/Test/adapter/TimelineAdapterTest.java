package com.i9930.croptrails.Test.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.CommonClasses.TimelineUnits;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.FarmDetails.Adapter.TimelineAdapter;
import com.i9930.croptrails.FarmDetails.FullScreenDialog.FullScreenDialog;
import com.i9930.croptrails.FarmDetails.FullScreenDialog.FullScreenDialogHealthCard;
import com.i9930.croptrails.FarmDetails.FullScreenDialog.FullScreenDialogShowPld;
import com.i9930.croptrails.FarmDetails.FullScreenDialog.FullScreenDialogVisit;
import com.i9930.croptrails.FarmDetails.Model.WeatherForecast;
import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineInnerData;
import com.i9930.croptrails.GerminationAndSpacing.ShowSampleGermination.Model.SampleGerminationDatum;
import com.i9930.croptrails.HarvestReport.Adapter.HarvestBagAdapter;
import com.i9930.croptrails.HarvestReport.Model.HarvestDetailInnerData;
import com.i9930.croptrails.HarvestReport.Model.HarvestDetailMaster;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.QRGenerate.Model.QRResponse;
import com.i9930.croptrails.QRGenerate.QRGenerateActivity;
import com.i9930.croptrails.R;
import com.i9930.croptrails.ShowInputCost.Model.InputCostData;
import com.i9930.croptrails.ShowInputCost.Model.ResourceData;
import com.i9930.croptrails.SoilSense.Dashboard.Model.GetSoilDatum;
import com.i9930.croptrails.SoilSense.Dashboard.SoilSensDashboardActivity;
import com.i9930.croptrails.SoilSense.SoilDetails;
import com.i9930.croptrails.SubmitActivityForm.TimelineFormActivity2;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCard;
import com.i9930.croptrails.Test.ObjectType;
import com.i9930.croptrails.Test.PldModel.PldData;
import com.i9930.croptrails.Test.SatSureImage.SatsureImageActivity;
import com.i9930.croptrails.Test.SatusreModel.SatsureData;
import com.i9930.croptrails.Test.SellModel.SellData;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Test.model.TimelineHarvest;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Weather.WeatherActivity;

import static android.content.ContentValues.TAG;
import static java.lang.Double.parseDouble;

public class TimelineAdapterTest extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    Map<String, Object> map;
    boolean onSoilCardClick=false;
    List<Object> objectList;
    List<ObjectType> list;
    List<Integer> positionList = new ArrayList<>();
    List<RecyclerView.ViewHolder> myViewHolderList = new ArrayList<>();
    private final int AC = 0, VR = 1, WR = 2, GE = 3, HR = 4, IC = 5, RU = 6, HC = 7, FARM = 8, SAT = 9, PLD = 10, SELL = 11, SOIL_SENS = 12,DELINQUENT = 13,
            ADVERTISE = 14;
    Activity activity2;
    Date c;
    SimpleDateFormat df;
    String todayDateStr = "";
    Date todayDate;
    Date farmDate;
    List<TimelineUnits> timelineUnit;
    private boolean isCurrentLocation;
    int lastPostion;

    LinearLayoutManager linearLayoutManager;
    String area_unit_label = "";
    HarvestBagAdapter adapter;
    FarmDetailsClickListener listener;
    boolean isSoilSens;
    boolean isOfflineMode=false;
    String imagePath="";

    public TimelineAdapterTest(Activity activity, Context context, List<Object> objectList,
                               List<ObjectType> list, List<TimelineUnits> timelineUnit,
                                boolean isCurrentLocation, FarmDetailsClickListener listener) {
        imagePath= Environment.getExternalStorageDirectory() + AppConstants.getImagePath(AppConstants.FILE_PATH_TYPE.VIST, activity);
        isOfflineMode=SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE);
        this.context = context;
        this.isCurrentLocation = isCurrentLocation;
        this.listener = listener;
        this.timelineUnit = timelineUnit;
        this.activity2 = activity;
        this.objectList = objectList;
        this.list = list;
        lastPostion = objectList.size() - 1;
        isSoilSens=SharedPreferencesMethod.getBoolean(context,SharedPreferencesMethod.IS_SOIL_SENS_ENABLED);
        area_unit_label = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL);
        c = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("yyyy-MM-dd");
        todayDateStr = df.format(c);

        try {
            if (TestActivity.todayDate != null && !TextUtils.isEmpty(TestActivity.todayDate)) {
                todayDate = df.parse(TestActivity.todayDate);
                Log.e("Adapter", "Server date is " + TestActivity.todayDate);
            } else {
                Log.e("Adapter", "Server date is null");
                todayDate = df.parse(todayDateStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            try {
                todayDate = df.parse(todayDateStr);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case AC:
                View v1 = inflater.inflate(R.layout.timeline_rv_layout_act, parent, false);
                viewHolder = new ActivityHolder(v1);
                break;
            case VR:
                View v2 = inflater.inflate(R.layout.timeline_rv_layout_visit, parent, false);
                viewHolder = new VisitHolder(v2);
                break;
            case DELINQUENT:
                View v13 = inflater.inflate(R.layout.timeline_rv_layout_visit, parent, false);
                viewHolder = new VisitHolder(v13);
                break;
            case HR:
                View v3 = inflater.inflate(R.layout.timeline_rv_layout_harvest, parent, false);
                viewHolder = new HarvestHolder(v3);
                break;
            case GE:
                View v4 = inflater.inflate(R.layout.timeline_rv_layout_germination, parent, false);
                viewHolder = new GerminationHolder(v4);
                break;
            case HC:
                View v5 = inflater.inflate(R.layout.timeline_rv_layout_health_card, parent, false);
                viewHolder = new HealthCardHolder(v5);
                break;
            case IC:
                View v6 = inflater.inflate(R.layout.timeline_rv_layout_input_cost, parent, false);
                viewHolder = new InputHolder(v6);
                break;
            case RU:
                View v7 = inflater.inflate(R.layout.timeline_rv_layout_resource_used, parent, false);
                viewHolder = new ResourceHolder(v7);
                break;

            case WR:
                View v9 = inflater.inflate(R.layout.timeline_rv_layout_weather, parent, false);
                viewHolder = new WeatherHolder(v9);
                break;

            case FARM:
                View v10 = inflater.inflate(R.layout.timeline_rv_layout_farm_details, parent, false);
                viewHolder = new FarmHolder(v10);
                break;
            case SAT:
                View v11 = inflater.inflate(R.layout.timeline_rv_layout_sature_data, parent, false);
                viewHolder = new SatViewHolder(v11);
                break;
            case PLD:
                View vPld = inflater.inflate(R.layout.timeline_rv_layout_pld, parent, false);
                viewHolder = new PldViewHolder(vPld);
                break;
            case SELL:
                View vSell = inflater.inflate(R.layout.timeline_rv_layout_produce_sell, parent, false);
                viewHolder = new SellViewHolder(vSell);
                break;
            case SOIL_SENS:
                View vSoil = inflater.inflate(R.layout.timeline_rv_layout_soil_sens, parent, false);
                viewHolder = new SoilSense(vSoil);

                break;
            case ADVERTISE:
                View addv = inflater.inflate(R.layout.rv_layout_banner_ad, parent, false);
                viewHolder = new BannerHolder(addv);

                break;
            default:
                View v8 = inflater.inflate(R.layout.timeline_rv_layout_act, parent, false);
                viewHolder = new ActivityHolder(v8);
                break;
        }
        return viewHolder;
    }


    private class SatViewHolder extends RecyclerView.ViewHolder {
        CircleImageView timelineDotImage;
        TextView timelineDoaSat, valueLabelTv, timelineSatHead, dNameTv, pNameTv, valueTv;
        View timelineLine;
        ImageView imageView;


        public SatViewHolder(@NonNull View itemView) {
            super(itemView);
            valueTv = itemView.findViewById(R.id.valueTv);
            imageView = itemView.findViewById(R.id.imageView);
            dNameTv = itemView.findViewById(R.id.dNameTv);
            valueLabelTv = itemView.findViewById(R.id.valueLabelTv);

            pNameTv = itemView.findViewById(R.id.pNameTv);
            timelineDoaSat = itemView.findViewById(R.id.timelineDoaSat);
            timelineSatHead = itemView.findViewById(R.id.timelineSatHead);
            timelineLine = itemView.findViewById(R.id.timelineLine);
            timelineDotImage = itemView.findViewById(R.id.timelineDotImage);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        switch (holder.getItemViewType()) {
            case AC:
                ActivityHolder vh1 = (ActivityHolder) holder;
                configureAct(vh1, position);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(vh1);
                }
                break;
            case VR:
                VisitHolder vh2 = (VisitHolder) holder;
                configureVisit(vh2, position,false);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(vh2);
                }
                break;
            case ADVERTISE:
                BannerHolder vhAd = (BannerHolder) holder;
                configureAD(vhAd, position);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(vhAd);
                }
                break;
            case DELINQUENT:
                VisitHolder vh13 = (VisitHolder) holder;
                configureVisit(vh13, position,true);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(vh13);
                }
                break;
            case HR:
                HarvestHolder vh3 = (HarvestHolder) holder;
                configureHarvest(vh3, position);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(vh3);
                }
                break;
            case GE:
                GerminationHolder vh4 = (GerminationHolder) holder;
                configureGermination(vh4, position);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(vh4);
                }
                break;
            case HC:
                HealthCardHolder vh5 = (HealthCardHolder) holder;
                configureHealthCard(vh5, position);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(vh5);
                }
                break;
            case IC:
                InputHolder vh6 = (InputHolder) holder;
                configureInput(vh6, position);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(vh6);
                }
                break;
            case RU:
                ResourceHolder vh7 = (ResourceHolder) holder;
                configureResource(vh7, position);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(vh7);
                }
                break;
            case WR:
                //if (weatherForecast != null) {
                WeatherHolder vh8 = (WeatherHolder) holder;
                configureWeather(vh8, position);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(vh8);
                }
                //}
                break;

            case FARM:
                FarmHolder vh10 = (FarmHolder) holder;
                configureFarm(vh10, position);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(vh10);
                }
                break;
            case SAT:
                SatViewHolder vh11 = (SatViewHolder) holder;
                configureSatsure(vh11, position);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(vh11);
                }
                break;
            case PLD:
                PldViewHolder vPld = (PldViewHolder) holder;
                configurePld(vPld, position);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(vPld);
                }
                break;

            case SELL:
                SellViewHolder sellHolder = (SellViewHolder) holder;
                configureSell(sellHolder, position);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(sellHolder);
                }
                break;

            case SOIL_SENS:
                SoilSense vh12 = (SoilSense) holder;
                configureSoilSens(vh12, position);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(vh12);
                }
                break;

            default:
                /*RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) holder;
                configureDefaultViewHolder(vh, position);*/
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getType().equals(AppConstants.TIMELINE.ACT)) {
            return AC;
        } else if (list.get(position).getType().equals(AppConstants.TIMELINE.ADVERTISE)) {
            return ADVERTISE;
        } else if (list.get(position).getType().equals(AppConstants.TIMELINE.VR)) {
            return VR;
        }else if (list.get(position).getType().equals(AppConstants.TIMELINE.DELINQUENT)) {
            return DELINQUENT;
        } else if (list.get(position).getType().equals(AppConstants.TIMELINE.WR)) {
            return WR;
        } else if (list.get(position).getType().equals(AppConstants.TIMELINE.GE)) {
            return GE;
        } else if (list.get(position).getType().equals(AppConstants.TIMELINE.HR)) {
            return HR;
        } else if (list.get(position).getType().equals(AppConstants.TIMELINE.IC)) {
            return IC;
        } else if (list.get(position).getType().equals(AppConstants.TIMELINE.RU)) {
            return RU;
        } else if (list.get(position).getType().equals(AppConstants.TIMELINE.HC)) {
            return HC;
        } else if (list.get(position).getType().equals(AppConstants.TIMELINE.FARM)) {
            return FARM;
        } else if (list.get(position).getType().equals(AppConstants.TIMELINE.SAT)) {
            return SAT;
        } else if (list.get(position).getType().equals(AppConstants.TIMELINE.PLD)) {
            return PLD;
        } else if (list.get(position).getType().equals(AppConstants.TIMELINE.SELL)) {
            return SELL;
        } else if (list.get(position).getType().equals(AppConstants.TIMELINE.SOIL_SENSE)) {
            return SOIL_SENS;
        } else {
            return 0;
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    private class ActivityHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView, timelineDotImage;
        TextView moreButtonTv, timelineActDate, timelineActHead, timelineActivityType, timelineActDetails;
        View view_not_allowed_to_input, timelineLine;
        CardView activity_card;

        public ActivityHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.timeline_center_image);
            moreButtonTv = itemView.findViewById(R.id.more_button_tv);
            view_not_allowed_to_input = itemView.findViewById(R.id.view_not_allowed_to_input);
            timelineActDate = itemView.findViewById(R.id.timelineActDate);
            timelineActHead = itemView.findViewById(R.id.timelineActHead);
            timelineActivityType = itemView.findViewById(R.id.timeline_activity_type);
            timelineActDetails = itemView.findViewById(R.id.full_details_tv2);
            timelineLine = itemView.findViewById(R.id.timelineLine);
            timelineDotImage = itemView.findViewById(R.id.timelineDotImage);
            activity_card = itemView.findViewById(R.id.activity_card);

        }
    }

    private class GerminationHolder extends RecyclerView.ViewHolder {
        CircleImageView timelineDotImage;
        View timelineLine;
        CardView timelineGermiCard;
        TextView moreGermiTv, timelineDoaGermination, timelineActualPopu, timelineGermitHead, timelineConfiguredArea, timelineIdealPopu;

        public GerminationHolder(@NonNull View itemView) {
            super(itemView);
            timelineGermiCard = itemView.findViewById(R.id.timelineGermiCard);
            timelineDotImage = itemView.findViewById(R.id.timelineDotImage);
            timelineLine = itemView.findViewById(R.id.timelineLine);
            timelineDoaGermination = itemView.findViewById(R.id.timelineDoaGermination);
            timelineActualPopu = itemView.findViewById(R.id.timelineActualPopu);
            timelineGermitHead = itemView.findViewById(R.id.timelineGermitHead);
            timelineConfiguredArea = itemView.findViewById(R.id.timelineConfiguredArea);
            timelineIdealPopu = itemView.findViewById(R.id.timelineIdealPopu);
            moreGermiTv = itemView.findViewById(R.id.moreGermiTv);
        }
    }

    private class SellViewHolder extends RecyclerView.ViewHolder {
        CircleImageView timelineDotImage;
        View timelineLine;

        CardView timelineSellCard;
        TextView updateSellTv, timelineDoaSell, timelineQtyTv, timelineSellHead, timelineTotalAmmount, timelineRate;

        public SellViewHolder(@NonNull View itemView) {
            super(itemView);
            timelineSellCard = itemView.findViewById(R.id.timelineSellCard);
            timelineDotImage = itemView.findViewById(R.id.timelineDotImage);
            timelineLine = itemView.findViewById(R.id.timelineLine);

            timelineDoaSell = itemView.findViewById(R.id.timelineDoaSell);
            timelineQtyTv = itemView.findViewById(R.id.timelineQtyTv);
            timelineSellHead = itemView.findViewById(R.id.timelineSellHead);

            timelineTotalAmmount = itemView.findViewById(R.id.timelineTotalAmmount);
            timelineRate = itemView.findViewById(R.id.timelineRate);
            updateSellTv = itemView.findViewById(R.id.updateSellTv);
        }
    }

    private class SoilSense extends RecyclerView.ViewHolder {

        TextView moreHarvestTv, timelineDoaHarvest, moistureLevel;
        CircleImageView timelineDotImage;
        View timelineLine;
        CardView harvest_card;
        ImageView qrImage;

        public SoilSense(@NonNull View itemView) {
            super(itemView);
            qrImage = itemView.findViewById(R.id.qrImage);
            harvest_card = itemView.findViewById(R.id.harvest_card);
            timelineLine = itemView.findViewById(R.id.timelineLine);
            timelineDotImage = itemView.findViewById(R.id.timelineDotImage);
            moreHarvestTv = itemView.findViewById(R.id.moreHarvestTv);
            timelineDoaHarvest = itemView.findViewById(R.id.timelineDoaHarvest);
            moistureLevel = itemView.findViewById(R.id.timelineHarvestArea);

        }
    }

    private class BannerHolder extends RecyclerView.ViewHolder {


        CardView ad_card_view;
        AdView adView;


        public BannerHolder(@NonNull View itemView) {
            super(itemView);
            adView = itemView.findViewById(R.id.adView);
            ad_card_view = itemView.findViewById(R.id.ad_card_view);

        }
    }

    private class VisitHolder extends RecyclerView.ViewHolder {
        TextView visitDateTv, visitHead,reasonTv;
        ProgressBar imageProgress;
        ImageView visitImage;
        CircleImageView timelineDotImage;
        View timelineLine;
        CardView visit_card;

        public VisitHolder(@NonNull View itemView) {
            super(itemView);
            visit_card = itemView.findViewById(R.id.visit_card);
            visitDateTv = itemView.findViewById(R.id.timeline_date_visit);
            reasonTv = itemView.findViewById(R.id.reasonTv);
            imageProgress = itemView.findViewById(R.id.image_loading_progress);
            visitImage = itemView.findViewById(R.id.timeline_image_visit);
            visitHead = itemView.findViewById(R.id.timeline_title_visit);
            timelineDotImage = itemView.findViewById(R.id.timelineDotImage);
            timelineLine = itemView.findViewById(R.id.timelineLine);
        }
    }


    private class WeatherHolder extends RecyclerView.ViewHolder {
        TextView weatherNotAvailable, weatherCityNameTv, txt_curr_temp, txt_max_temp, txt_min_temp, rainfall, weather_desclaimer_msg_tv;
        ImageView skycon_view;
        ProgressBar weatherProgress, progressbar;
        LinearLayout weatherLay;
        CircleImageView timelineDotImage;
        RelativeLayout linear_weather_lay;
        View timelineLine;

        public WeatherHolder(@NonNull View itemView) {
            super(itemView);
            weatherProgress = itemView.findViewById(R.id.weatherProgress);
            weatherNotAvailable = itemView.findViewById(R.id.weatherNotAvailable);
            weatherCityNameTv = itemView.findViewById(R.id.weatherCityNameTv);
            txt_curr_temp = itemView.findViewById(R.id.txt_curr_temp);
            txt_max_temp = itemView.findViewById(R.id.txt_max_temp);
            txt_min_temp = itemView.findViewById(R.id.txt_min_temp);
            rainfall = itemView.findViewById(R.id.rainfall);
            skycon_view = itemView.findViewById(R.id.skycon_view);
            progressbar = itemView.findViewById(R.id.progressbar);
            weatherLay = itemView.findViewById(R.id.weatherLay);
            weather_desclaimer_msg_tv = itemView.findViewById(R.id.weather_desclaimer_msg_tv);
            linear_weather_lay = itemView.findViewById(R.id.linear_weather_lay);
            timelineDotImage = itemView.findViewById(R.id.timelineDotImage);
            timelineLine = itemView.findViewById(R.id.timelineLine);

        }
    }


    private class FarmHolder extends RecyclerView.ViewHolder {
        CircleImageView timelineDotImage;
        ImageView timelineCenterImageFarm;
        View timelineLine;
        TextView timelineDoaFarm, timelineFarmHead, diapatchAreaLabel,
                timelineDisaptchArea, sowingAreaLabel, timelineSowingArea, standingAreaLabel,
                timelineStandingArea, moreFarmTv, areaUnitTv, areaUnitLabel;
        CardView timelineFarmDetailsCard;

        public FarmHolder(@NonNull View itemView) {
            super(itemView);

            timelineDotImage = itemView.findViewById(R.id.timelineDotImage);
            timelineLine = itemView.findViewById(R.id.timelineLine);
            timelineFarmDetailsCard = itemView.findViewById(R.id.timelineFarmDetailsCard);
            timelineDoaFarm = itemView.findViewById(R.id.timelineDoaFarm);
            timelineFarmHead = itemView.findViewById(R.id.timelineFarmHead);

            diapatchAreaLabel = itemView.findViewById(R.id.diapatchAreaLabel);
            timelineDisaptchArea = itemView.findViewById(R.id.timelineDisaptchArea);

            sowingAreaLabel = itemView.findViewById(R.id.sowingAreaLabel);
            timelineSowingArea = itemView.findViewById(R.id.timelineSowingArea);

            standingAreaLabel = itemView.findViewById(R.id.standingAreaLabel);
            timelineStandingArea = itemView.findViewById(R.id.timelineStandingArea);

            areaUnitTv = itemView.findViewById(R.id.areaUnitTv);
            areaUnitLabel = itemView.findViewById(R.id.areaUnitLabel);
            timelineCenterImageFarm = itemView.findViewById(R.id.timelineCenterImageFarm);
            moreFarmTv = itemView.findViewById(R.id.moreFarmTv);

        }
    }


    private class HarvestHolder extends RecyclerView.ViewHolder {

        TextView moreHarvestTv, timelineDoaHarvest, timelineHarvestHead, timelineWeighmentDate, timelineHarvestDate, timelineHarvestArea;
        CircleImageView timelineDotImage;
        View timelineLine;
        CardView harvest_card;
        ImageView qrImage;

        public HarvestHolder(@NonNull View itemView) {
            super(itemView);
            qrImage = itemView.findViewById(R.id.qrImage);
            harvest_card = itemView.findViewById(R.id.harvest_card);
            timelineLine = itemView.findViewById(R.id.timelineLine);
            timelineDotImage = itemView.findViewById(R.id.timelineDotImage);
            moreHarvestTv = itemView.findViewById(R.id.moreHarvestTv);
            timelineDoaHarvest = itemView.findViewById(R.id.timelineDoaHarvest);
            timelineHarvestHead = itemView.findViewById(R.id.timelineHarvestHead);
            timelineWeighmentDate = itemView.findViewById(R.id.timelineWeighmentDate);
            timelineHarvestDate = itemView.findViewById(R.id.timelineHarvestDate);
            timelineHarvestArea = itemView.findViewById(R.id.timelineHarvestArea);

        }
    }

    private class HealthCardHolder extends RecyclerView.ViewHolder {
        CircleImageView timelineDotImage;
        View timelineLine;
        TextView timelineDoaHealth, timelineHealthHead, timelineLabName, timelineSampleNumber, timelineCollectedOn, moreHealthTv;
        CardView healthcard_card;

        public HealthCardHolder(@NonNull View itemView) {
            super(itemView);
            healthcard_card = itemView.findViewById(R.id.healthcard_card);
            timelineLine = itemView.findViewById(R.id.timelineLine);
            timelineDotImage = itemView.findViewById(R.id.timelineDotImage);

            timelineDoaHealth = itemView.findViewById(R.id.timelineDoaHealth);
            timelineHealthHead = itemView.findViewById(R.id.timelineHealthHead);
            timelineLabName = itemView.findViewById(R.id.timelineLabName);
            timelineSampleNumber = itemView.findViewById(R.id.timelineSampleNumber);
            timelineCollectedOn = itemView.findViewById(R.id.timelineCollectedOn);
            moreHealthTv = itemView.findViewById(R.id.moreHealthTv);
        }
    }

    private class InputHolder extends RecyclerView.ViewHolder {
        CircleImageView timelineDotImage;
        View timelineLine;
        CardView inputCostCard;
        TextView added_by_tv, cost_tv, cost_type_tv, cost_date_tv, costDate;

        public InputHolder(@NonNull View itemView) {
            super(itemView);
            inputCostCard = itemView.findViewById(R.id.inputCostCard);
            //cost_img = itemView.findViewById(R.id.cost_img);
            added_by_tv = itemView.findViewById(R.id.added_by_tv);
            cost_type_tv = itemView.findViewById(R.id.cost_type_tv);
            cost_tv = itemView.findViewById(R.id.cost_tv);
            cost_date_tv = itemView.findViewById(R.id.cost_date_tv_);
            costDate = itemView.findViewById(R.id.costDate);
            timelineLine = itemView.findViewById(R.id.timelineLine);
            timelineDotImage = itemView.findViewById(R.id.timelineDotImage);

        }
    }

    private class PldViewHolder extends RecyclerView.ViewHolder {
        CircleImageView timelineDotImage;
        View timelineLine;
        CardView timelinePldCard;
        TextView timelineDoaPld, timelinePldHead, timelineLossArea, lossDateTv, lossReasonTv, more_button_tv;

        public PldViewHolder(@NonNull View itemView) {
            super(itemView);
            timelinePldCard = itemView.findViewById(R.id.timelinePldCard);
            //cost_img = itemView.findViewById(R.id.cost_img);
            timelineDoaPld = itemView.findViewById(R.id.timelineDoaPld);
            timelinePldHead = itemView.findViewById(R.id.timelinePldHead);
            timelineLossArea = itemView.findViewById(R.id.timelineLossArea);

            lossDateTv = itemView.findViewById(R.id.lossDateTv);
            lossReasonTv = itemView.findViewById(R.id.lossReasonTv);
            timelineLine = itemView.findViewById(R.id.timelineLine);
            timelineDotImage = itemView.findViewById(R.id.timelineDotImage);
            more_button_tv = itemView.findViewById(R.id.more_button_tv);

        }
    }

    private class ResourceHolder extends RecyclerView.ViewHolder {
        CircleImageView timelineDotImage;
        View timelineLine;
        TextView resourceUsedDate;
        ImageView cost_img;
        TextView added_by_res_tv, qty_tv, date_res_tv, type_res_tv;


        public ResourceHolder(@NonNull View itemView) {
            super(itemView);
            timelineLine = itemView.findViewById(R.id.timelineLine);
            timelineDotImage = itemView.findViewById(R.id.timelineDotImage);
            resourceUsedDate = itemView.findViewById(R.id.resourceUsedDate);
            cost_img = itemView.findViewById(R.id.cost_img);

            added_by_res_tv = itemView.findViewById(R.id.added_by_res_tv);
            qty_tv = itemView.findViewById(R.id.qty_tv);
            date_res_tv = itemView.findViewById(R.id.resource_date_tv_);
            type_res_tv = itemView.findViewById(R.id.type_res_tv);

        }
    }

    private void configureAct(ActivityHolder holder, int position) {
        String json = new Gson().toJson(objectList.get(position));
        TimelineInnerData innerData = new Gson().fromJson(json, TimelineInnerData.class);

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        try {
            farmDate = formatDate.parse(innerData.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.timelineActHead.setText(innerData.getActivity());
        holder.timelineActivityType.setText(innerData.getActivityType());

        if (innerData.getActivityImg() != null && !innerData.getActivityImg().equals("0")) {
           /* if (innerData.getActivityImg().contains("croptrailsimages.s3")) {
                ShowAwsImage.getInstance(context).downloadFile(Uri.parse(innerData.getActivityImg()), holder.circleImageView, innerData.getActivityImg());
            } else {*/
            Glide.with(context).
                    asBitmap().
                    load(innerData.getActivityImg()).
                    into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            holder.circleImageView.setImageBitmap(resource);

                        }

                    });
            //  }
        } else {

        }
        String text;
        if (innerData.getDescription() != null) {
            text = context.getResources().getString(R.string.narration) + ": " + innerData.getDescription();
        } else {
            text = context.getResources().getString(R.string.narration) + ": " + "-";
        }

        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(text);
        ssBuilder.setSpan(
                new StyleSpan(Typeface.BOLD), // span to add
                text.indexOf(context.getResources().getString(R.string.narration) + ":"), // start of the span (inclusive)
                text.indexOf(context.getResources().getString(R.string.narration) + ":") + String.valueOf(context.getResources().getString(R.string.narration) + ":").length(), // end of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // do not extend the span when text add later
        );
        holder.timelineActDetails.setText(ssBuilder);

        if (todayDate != null && farmDate != null && todayDate.equals(farmDate)) {

            if (innerData.getIsDone().equals("Y")) {
                holder.timelineDotImage.setImageResource(R.color.timeline_act_done);
                holder.timelineActDate.setText(getDate(innerData.getDate()));
                holder.timelineActDate.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                holder.moreButtonTv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.timeline_act_done));
                holder.timelineLine.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                holder.moreButtonTv.setText(context.getResources().getString(R.string.view_more_label));
                holder.activity_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*if (SharedPreferencesMethod.getString(context,SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.SUPERVISOR)||
                                SharedPreferencesMethod.getString(context,SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ADMIN)) {*/
                        if (innerData.getActivityImg() != null)
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, innerData.getActivityImg());
                        else
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, "0");

                        DataHandler.newInstance().setTimelineInnerData(innerData);
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTITY_ID, innerData.getFarmCalActivityId());
                        FullScreenDialog dialog = new FullScreenDialog();
                        FragmentTransaction ft = activity2.getFragmentManager().beginTransaction();
                        dialog.show(ft, FullScreenDialog.TAG);
//                        }
                    }
                });
            } else {
                holder.timelineDotImage.setImageResource(R.color.timeline_act_today_pending);
                holder.timelineActDate.setText(getDate(innerData.getDate()));
                holder.timelineActDate.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_today_pending));
                holder.moreButtonTv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_today_pending));
                holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.timeline_act_today_pending));
                holder.timelineLine.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_today_pending));
                holder.moreButtonTv.setText(R.string.add_todays_activity_label);
                holder.activity_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR)||
                                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.ADMIN)||
                                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUB_ADMIN)||
                                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPER_ADMIN)) {
                            if (innerData.getActivityImg() != null)
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, innerData.getActivityImg());
                            else
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, "0");

                            DataHandler.newInstance().setTimelineUnits(timelineUnit);
                            DataHandler.newInstance().setTimelineInnerData(innerData);
                            Intent intent=new Intent(context, TimelineFormActivity2.class);
                            intent.putExtra("position",innerData.getIndex());
                            context.startActivity(intent);

                        }
                    }
                });
            }


        } else if (todayDate != null && farmDate != null && todayDate.after(farmDate)) {
            if (innerData.getIsDone() != null && innerData.getIsDone().equals("Y")) {
                holder.activity_card.setAlpha(1);
                holder.timelineDotImage.setImageResource(R.color.timeline_act_done);
                holder.timelineActDate.setText(getDate(innerData.getDate()));
                holder.timelineActDate.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                holder.moreButtonTv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.timeline_act_done));
                holder.timelineLine.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                holder.moreButtonTv.setText(R.string.view_activity_label);
                holder.view_not_allowed_to_input.setVisibility(View.GONE);
                holder.activity_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (innerData.getActivityImg() != null)
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, innerData.getActivityImg());
                        else
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, "0");

                        DataHandler.newInstance().setTimelineInnerData(innerData);
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTITY_ID, innerData.getFarmCalActivityId());
                        FullScreenDialog dialog = new FullScreenDialog();
                        FragmentTransaction ft = activity2.getFragmentManager().beginTransaction();
                        dialog.show(ft, FullScreenDialog.TAG);

                    }
                });
            } else {

                holder.timelineDotImage.setImageResource(R.color.timeline_act_pending);
                holder.timelineActDate.setText(getDate(innerData.getDate()));
                holder.timelineActDate.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_pending));
                holder.moreButtonTv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_pending));
                holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.timeline_act_pending));
                holder.timelineLine.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_pending));
                holder.moreButtonTv.setText(context.getResources().getString(R.string.pending_label));

                if (getCountOfDays(innerData.getDate(), todayDateStr) > 15) {
                    holder.activity_card.setAlpha(.5f);
                    holder.view_not_allowed_to_input.setVisibility(View.VISIBLE);
                    holder.activity_card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /*if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR)||
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.ADMIN)||
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUB_ADMIN)||
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPER_ADMIN)) {
                                Toasty.error(context, context.getResources().getString(R.string.now_not_allowed_to_add_activity_msg), Toast.LENGTH_LONG, false).show();
                            }
*/
                            if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR)||
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.ADMIN)||
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUB_ADMIN)||
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPER_ADMIN)) {

                                if (innerData.getActivityImg() != null)
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, innerData.getActivityImg());
                                else
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, "0");
                                DataHandler.newInstance().setTimelineUnits(timelineUnit);
                                DataHandler.newInstance().setTimelineInnerData(innerData);
                                Intent intent=new Intent(context, TimelineFormActivity2.class);
                                intent.putExtra("position",innerData.getIndex());
                                context.startActivity(intent);
                            }
                        }
                    });
                } else {
                    holder.activity_card.setAlpha(1);
                    holder.activity_card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR)||
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.ADMIN)||
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUB_ADMIN)||
                                    SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPER_ADMIN)) {

                                if (innerData.getActivityImg() != null)
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, innerData.getActivityImg());
                                else
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, "0");
                                DataHandler.newInstance().setTimelineUnits(timelineUnit);
                                DataHandler.newInstance().setTimelineInnerData(innerData);
                                Intent intent=new Intent(context, TimelineFormActivity2.class);
                                intent.putExtra("position",innerData.getIndex());
                                context.startActivity(intent);
                            }
                        }
                    });

                }
            }
        } else {
            holder.activity_card.setAlpha(1);
            holder.timelineDotImage.setImageResource(R.color.timeline_act_upcomming);
            holder.timelineActDate.setText(getDate(innerData.getDate()));
            holder.timelineActDate.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_upcomming));
            holder.moreButtonTv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_upcomming));
            holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.timeline_act_upcomming));
            holder.timelineLine.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_upcomming));
            holder.view_not_allowed_to_input.setVisibility(View.GONE);
            holder.moreButtonTv.setText(R.string.upcomming_activity_label);
            holder.moreButtonTv.setVisibility(View.INVISIBLE);
        }


        if (position == lastPostion) {
            holder.timelineLine.setVisibility(View.GONE);
        } else {
            holder.timelineLine.setVisibility(View.VISIBLE);
        }
    }

    private String getDate(String dateStr) {

        try {
            DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateView = srcDf.parse(dateStr);
            DateFormat srcDfView = new SimpleDateFormat("dd MMM");
            dateStr = srcDfView.format(dateView);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    private void configureAD(BannerHolder bannerHolder, int position) {
        try {

            AdRequest adRequest = new AdRequest.Builder().build();

            /*List<String> testDeviceIds = Arrays.asList("1126E5CF85D37A9661950E74DF5A73FE");
            RequestConfiguration configuration =
                    new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
            MobileAds.setRequestConfiguration(configuration);*/
//        mAdView.setAdUnitId("ca-app-pub-5740952327077672/5919581872");
            bannerHolder.adView.loadAd(adRequest);

            bannerHolder.adView.setAdListener(new AdListener() {
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void configureVisit(VisitHolder holder, int position,boolean isDelinquent) {
        try {
            String json = new Gson().toJson(objectList.get(position));
            TimelineInnerData innerData = new Gson().fromJson(json, TimelineInnerData.class);
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            try {
                farmDate = formatDate.parse(innerData.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (isDelinquent) {
                holder.visitHead.setText(R.string.delinquent_report);
                holder.reasonTv.setVisibility(View.VISIBLE);

                holder.reasonTv.setText(context.getResources().getString(R.string.reason) + " : " + innerData.getReason());
            } else {
                if (todayDate != null && farmDate != null && todayDate.equals(farmDate)) {
                    holder.visitHead.setText(R.string.visit_added_for_today_label);

                } else {
                    holder.visitHead.setText(R.string.visit_label);

                }
                holder.reasonTv.setVisibility(View.GONE);
            }
            holder.timelineDotImage.setImageResource(R.color.timeline_vis_done);
            holder.visitDateTv.setText(getDate(innerData.getDate()));
            holder.visitDateTv.setBackgroundColor(context.getResources().getColor(R.color.timeline_vis_done));
            holder.timelineLine.setBackgroundColor(context.getResources().getColor(R.color.timeline_vis_done));
            if (!isOfflineMode) {
                if (innerData.getImg_link() != null && !innerData.getImg_link().equals("0")) {
                    Glide.with(context).
                            asBitmap().
                            load(innerData.getImg_link()).
                            into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    holder.imageProgress.setVisibility(View.GONE);
                                    holder.visitImage.setImageBitmap(resource);

                                }

                            });
                } else {
                    holder.imageProgress.setVisibility(View.GONE);
                    holder.visitImage.setImageResource(R.drawable.ploughed_farm);
                }
            } else {
                String url = imagePath + "/" + innerData.getVisitReportId() + "_0.png";
                Log.e("configureVisit", "TAG " + "" + url);
                Glide.with(context).
                        asBitmap().
                        load(url).
                        into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                holder.imageProgress.setVisibility(View.GONE);
                                holder.visitImage.setImageBitmap(resource);

                            }

                        });
            }

            holder.visit_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.VR_ID, innerData.getVisitReportId());
                    //DataHandler.newInstance().setVisit_report_id(Integer.parseInt(timelineDataList.get(position).getVisitReportId()));
                    FullScreenDialogVisit dialog = new FullScreenDialogVisit();
                    FragmentTransaction ft = activity2.getFragmentManager().beginTransaction();
                    dialog.show(ft, FullScreenDialogVisit.TAG);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        if (position == lastPostion) {
            holder.timelineLine.setVisibility(View.GONE);
        } else {
            holder.timelineLine.setVisibility(View.VISIBLE);
        }

    }

    private void configureGermination(GerminationHolder holder, int position) {
        String json = new Gson().toJson(objectList.get(position));
        SampleGerminationDatum datum = new Gson().fromJson(json, SampleGerminationDatum.class);
        holder.timelineDotImage.setImageResource(R.color.timeline_act_done);
        holder.timelineLine.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
        holder.moreGermiTv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
        holder.timelineDoaGermination.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
        holder.timelineDoaGermination.setText(getDate(datum.getDoa()));
        holder.timelineConfiguredArea.setText(datum.getConfiguredArea());
        holder.timelineIdealPopu.setText(datum.getIdealPop());
        holder.timelineActualPopu.setText(datum.getActualPop());

        holder.timelineGermiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                germinationDialog(context, datum);
            }
        });
        if (position == lastPostion) {
            holder.timelineLine.setVisibility(View.GONE);
        } else {
            holder.timelineLine.setVisibility(View.VISIBLE);
        }
    }


    private void configureSell(SellViewHolder holder, int position) {
        String json = new Gson().toJson(objectList.get(position));
        SellData datum = new Gson().fromJson(json, SellData.class);
        holder.timelineDotImage.setImageResource(R.color.timeline_act_done);
        holder.timelineLine.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
        holder.updateSellTv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
        holder.timelineDoaSell.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));

        holder.timelineDoaSell.setText(getDate(datum.getDoa()));
        holder.timelineQtyTv.setText(datum.getSaleQty());
        holder.timelineRate.setText(datum.getRate());
        holder.timelineTotalAmmount.setText(datum.getAmount());

        holder.updateSellTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellDialog(context, datum);
            }
        });
        if (position == lastPostion) {
            holder.timelineLine.setVisibility(View.GONE);
        } else {
            holder.timelineLine.setVisibility(View.VISIBLE);
        }
    }

    private void configureHarvest(HarvestHolder holder, int position) {
        String json = new Gson().toJson(objectList.get(position));
        TimelineHarvest data = new Gson().fromJson(json, TimelineHarvest.class);
        holder.timelineDotImage.setImageResource(R.color.timeline_act_done);
        holder.timelineDoaHarvest.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
        holder.timelineLine.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
        holder.moreHarvestTv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
        holder.timelineDoaHarvest.setText(getDate(data.getHarvestDetailMaster().getDoa()));
        holder.timelineWeighmentDate.setText(AppConstants.getShowableDate(data.getHarvestDetailMaster().getWeighmentDate(),context));
        holder.timelineHarvestDate.setText(AppConstants.getShowableDate(data.getHarvestDetailMaster().getHarvestDate(),context));
        holder.timelineHarvestArea.setText(AppConstants.getShowableArea(data.getHarvestDetailMaster().getHarvestedArea()) + " " + area_unit_label);
        holder.harvest_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                harvestDialog(context, data.getHarvestDetailInnerDataList(), data.getHarvestDetailMaster());
            }
        });
        holder.qrImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, QRGenerateActivity.class);
                intent.putExtra("hmid",data.getHarvestDetailMaster().getHarvestMasterId());
                context.startActivity(intent);
            }
        });
        if (position == lastPostion) {
            holder.timelineLine.setVisibility(View.GONE);
        } else {
            holder.timelineLine.setVisibility(View.VISIBLE);
        }


    }

    public boolean isOnSoilCardClick() {
        return onSoilCardClick;
    }

    public void setOnSoilCardClick(boolean onSoilCardClick) {
        this.onSoilCardClick = onSoilCardClick;
    }

    private void configureSoilSens(SoilSense holder, int position) {
        String json = new Gson().toJson(objectList.get(position));
        GetSoilDatum data = new Gson().fromJson(json, GetSoilDatum.class);
        holder.timelineDotImage.setImageResource(R.color.timeline_act_done);
        holder.timelineDoaHarvest.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
        holder.timelineLine.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
        holder.moreHarvestTv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
        holder.timelineDoaHarvest.setText(getDate(data.getDoa()));

        SoilDetails det=null;
        try {
            det=new Gson().fromJson(data.getDetailStr(),SoilDetails.class);
        }catch (Exception e){}

        String details = "-";
        if (det!=null){
            details=det.getReading();
        }
        holder.moistureLevel.setText(details+"%");
        holder.moreHarvestTv.setVisibility(View.INVISIBLE);

        if (onSoilCardClick&&isSoilSens){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onSoilCardClick){
                        context.startActivity(new Intent(context, SoilSensDashboardActivity.class));
                    }
                }
            });
        }
        /*holder.harvest_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                harvestDialog(context, data.getHarvestDetailInnerDataList(), data.getHarvestDetailMaster());
            }
        });
        holder.qrImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, QRGenerateActivity.class);
                intent.putExtra("hmid",data.getHarvestDetailMaster().getHarvestMasterId());
                context.startActivity(intent);
            }
        });*/
        if (position == lastPostion) {
            holder.timelineLine.setVisibility(View.GONE);
        } else {
            holder.timelineLine.setVisibility(View.VISIBLE);
        }


    }


    private void configureHealthCard(HealthCardHolder holder, int position) {
        String json = new Gson().toJson(objectList.get(position));
        HealthCard data = new Gson().fromJson(json, HealthCard.class);
        holder.moreHealthTv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
        holder.timelineDoaHealth.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
        holder.timelineDoaHealth.setText(getDate(data.getDoa()));
        holder.timelineLabName.setText(data.getLaboratoryName());
        holder.timelineCollectedOn.setText(AppConstants.getShowableDate(data.getSampleCollectionDate(),context));
        holder.timelineSampleNumber.setText(data.getSoilSampleNum());

        holder.healthcard_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullScreenDialogHealthCard dialog = new FullScreenDialogHealthCard(data);
                FragmentTransaction ft = activity2.getFragmentManager().beginTransaction();
                dialog.show(ft, FullScreenDialogHealthCard.TAG);
            }
        });

        if (position == lastPostion) {
            holder.timelineLine.setVisibility(View.GONE);
        } else {
            holder.timelineLine.setVisibility(View.VISIBLE);
        }
    }

    private void configureInput(InputHolder holder, int position) {
        String json = new Gson().toJson(objectList.get(position));
        InputCostData data = new Gson().fromJson(json, InputCostData.class);

        holder.added_by_tv.setText(context.getResources().getString(R.string.added_by_label) + " " + data.getName());
        holder.cost_date_tv.setText(getDate(data.getAddedOn()));
        holder.costDate.setText(context.getResources().getString(R.string.expense_date_label) + " " + AppConstants.getShowableDate(data.getExpenseDate(),context));

        holder.cost_tv.setText("Rs. " + data.getExpense() + "/-");
        if (data.getType() != null && !TextUtils.isEmpty(data.getType()) && !data.getType().equals("0"))
            holder.cost_type_tv.setText(context.getResources().getString(R.string.input_cost_type_label) + " " + data.getType());
        else
            holder.cost_type_tv.setText(context.getResources().getString(R.string.input_cost_type_label) + " " + data.getOtherTypeName());

       /* Locale locale = new Locale("hi");
        Currency currency = Currency.getInstance( context.getResources().getConfiguration().locale);
        String symbol = CURRENCIES.get(currency.getCurrencyCode());
        Log.e("Config", "Symb " + symbol);
        Log.e("Config", "Symb " + currency.getSymbol(Locale.getDefault()));
        */
        if (position == lastPostion) {
            holder.timelineLine.setVisibility(View.GONE);
        } else {
            holder.timelineLine.setVisibility(View.VISIBLE);
        }
    }

    public final Map<String, String> CURRENCIES = new HashMap<String, String>() {
        {
            put("EUR", "€");
            put("USD", "$");
            put("INR", "Rs");
        }
    };


    private void configureResource(ResourceHolder holder, int position) {
        String json = new Gson().toJson(objectList.get(position));
        ResourceData data = new Gson().fromJson(json, ResourceData.class);

        holder.added_by_res_tv.setText(context.getResources().getString(R.string.added_by_label) + " " + data.getName());
        holder.resourceUsedDate.setText(AppConstants.getShowableDate(data.getUsedOn(),context));
        holder.date_res_tv.setText(getDate(data.getAddedOn()));
        String unit = "";
        String type = "";
        String otherUnit = "";
        String otherType = "";
        if (data.getType() != null) {
            type = data.getType();
        }
        if (data.getUnit() != null) {
            unit = " " + data.getUnit();
        }
        if (data.getOtherResourceType() != null) {
            otherType = data.getOtherResourceType();
        }
        if (data.getOtherUnit() != null) {
            otherUnit = " " + data.getOtherUnit();
        }
        if (data.getType() != null) {
            holder.qty_tv.setText(type);
            holder.type_res_tv.setText(context.getResources().getString(R.string.resource_type_label) + " " + data.getValue() + unit);
        } else {
            //holder.type_res_tv.setText("Resource type " + otherType + otherUnit);
            holder.type_res_tv.setText(context.getResources().getString(R.string.resource_type_label) + " " + data.getValue() + " " + otherUnit);
            holder.qty_tv.setText(otherType);
        }
        if (position == lastPostion) {
            holder.timelineLine.setVisibility(View.GONE);
        } else {
            holder.timelineLine.setVisibility(View.VISIBLE);
        }
    }

    private void configureSatsure(SatViewHolder holder, int position) {
        String json = new Gson().toJson(objectList.get(position));
        SatsureData data = new Gson().fromJson(json, SatsureData.class);
        Log.e("TimelineAdapterTest", "Img " + data.getImageLink());
        holder.pNameTv.setText(data.getProductName());
        /*holder.dNameTv.setText(data.getDisplayName());
        holder.valueLabelTv.setText(data.getValueName());*/
        //if (data.getProductName()!=null&&data.getDisplayName()!=null&&data.getProductName().trim().equals(data.getDisplayName().trim()))
        holder.dNameTv.setVisibility(View.GONE);
        if (data.getImageLink() != null) {
            /*if (data.getImageLink().contains("croptrailsimages.s3")) {
                ShowAwsImage.getInstance(context).downloadFile(Uri.parse(data.getImageLink()), holder.imageView, data.getImageLink());
            } else {*/
            Glide.with(context)
                    .asBitmap()
                    .load(data.getImageLink())
                    .into(holder.imageView);
            // }
        }
        holder.timelineSatHead.setText(context.getResources().getString(R.string.satelite_view_label));
        holder.timelineDoaSat.setText(getDate(data.getDate()));

        if (position == lastPostion) {
            holder.timelineLine.setVisibility(View.GONE);
        } else {
            holder.timelineLine.setVisibility(View.VISIBLE);
        }
        try {
            holder.valueTv.setText(String.format("%.2f", parseDouble("" + data.getValue())));
        } catch (Exception e) {
            e.printStackTrace();
            holder.valueTv.setText("-");

        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SatsureImageActivity.class);
                intent.putExtra("typeId", data.getProductId());
                intent.putExtra("index", data.getSliderIndex());
                intent.putExtra("productName", data.getProductName());
                context.startActivity(intent);
            }
        });
    }

    private void configurePld(PldViewHolder holder, int position) {
        String json = new Gson().toJson(objectList.get(position));
        PldData data = new Gson().fromJson(json, PldData.class);
        holder.timelineDoaPld.setText(getDate(data.getDoa()));
        holder.lossDateTv.setText(AppConstants.getShowableDate(data.getPldDate(),context));
        if (data.getPldReason() != null && !data.getPldReason().trim().equals("0"))
            holder.lossReasonTv.setText(data.getPldReason());
        else if (data.getOtherReason() != null && !data.getOtherReason().trim().equals("0"))
            holder.lossReasonTv.setText(data.getOtherReason());
        if (AppConstants.isValidString(data.getPldArea()))
        holder.timelineLossArea.setText(AppConstants.getShowableArea(data.getPldArea()));
        else
            holder.timelineLossArea.setText("-");
        if (position == lastPostion) {
            holder.timelineLine.setVisibility(View.GONE);
        } else {
            holder.timelineLine.setVisibility(View.VISIBLE);
        }

        holder.more_button_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenDialogShowPld fragment = FullScreenDialogShowPld.newInstance(data);
                FragmentTransaction ft = activity2.getFragmentManager().beginTransaction();
                fragment.show(ft, FullScreenDialogShowPld.TAG);
            }
        });

    }

    WeatherForecast weatherForecast;

    private void configureWeather(WeatherHolder holder, int position) {

        String json = new Gson().toJson(objectList.get(position));
        //WeatherForecast weatherForecast = new Gson().fromJson(json, WeatherForecast.class);
        Log.e("TimelineAdapterTest", "In Weaather Card " + json);
        if (weatherForecast != null && weatherForecast.getIsLoaded() == 'Y') {
            holder.weatherLay.setVisibility(View.VISIBLE);
            holder.weatherNotAvailable.setVisibility(View.GONE);
            if (isCurrentLocation)
                holder.weather_desclaimer_msg_tv.setText(context.getResources().getString(R.string.farm_coordinates_not_availabe_showing_weather_data_of_your_current_location));
            else
                holder.weather_desclaimer_msg_tv.setText(context.getResources().getString(R.string.showing_weather_data_of_your_farm_location));

            holder.timelineDotImage.setImageResource(R.color.timeline_vis_done);
            holder.timelineLine.setBackgroundColor(context.getResources().getColor(R.color.timeline_vis_done));
            String cityName = "";
            String stateName = "";
            String countryName = "";
            if (weatherForecast.getCityName() != null)
                cityName = weatherForecast.getCityName();
            if (weatherForecast.getCityName() != null)
                stateName = weatherForecast.getStateName();
            if (weatherForecast.getCityName() != null)
                countryName = weatherForecast.getCountryName();
            holder.weatherCityNameTv.setText(cityName + ", " + stateName + ", " + countryName);
            holder.linear_weather_lay.setVisibility(View.VISIBLE);
            String icon = (String.valueOf(weatherForecast.getCurrently().getIcon()));
            DecimalFormat format = new DecimalFormat("##.##");
            double precipitation_val = Math.round(weatherForecast.getDaily().getData().get(0).getPrecipIntensity());
            holder.txt_curr_temp.setText(String.valueOf(Math.round(weatherForecast.getCurrently().getTemperature()) + "\u00B0" + " "));
            holder.txt_max_temp.setText(String.valueOf(Math.round(weatherForecast.getDaily().getData().get(0).getApparentTemperatureHigh()) + "\u00b0"));
            holder.txt_min_temp.setText(String.valueOf(Math.round(weatherForecast.getDaily().getData().get(0).getApparentTemperatureLow()) + "\u00b0c"));
            holder.rainfall.setText(context.getResources().getString(R.string.rainfall_label) + " " + String.valueOf(format.format(precipitation_val)) + " " + context.getResources().getString(R.string.mm_unit));

            select_skycon(icon, holder.skycon_view);
            Log.e("WeatherResp", new Gson().toJson(weatherForecast.getCurrently()));
            holder.weatherLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("inside_onClick", "done");
                    Intent intent = new Intent(context, WeatherActivity.class);
                    intent.putExtra("weatherForecast", (Serializable) weatherForecast);
                    context.startActivity(intent);


                }
            });
            Log.e("TimelineAdapterTest", "Weather is not null");
        } else {
            holder.weatherLay.setVisibility(View.GONE);
            holder.weatherNotAvailable.setVisibility(View.VISIBLE);
            Log.e("TimelineAdapterTest", "Weather is null");
        }

        if (position == lastPostion) {
            holder.timelineLine.setVisibility(View.GONE);
        } else {
            holder.timelineLine.setVisibility(View.VISIBLE);
        }
    }

    private void configureFarm(FarmHolder holder, int position) {

        String json = new Gson().toJson(objectList.get(position));
        FetchFarmResult farm = new Gson().fromJson(json, FetchFarmResult.class);
        holder.areaUnitTv.setText(area_unit_label);
        holder.timelineCenterImageFarm.setImageResource(R.drawable.ic_google_maps);
        holder.moreFarmTv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
        if (farm != null) {

            if (farm.getDoa() != null && !TextUtils.isEmpty(farm.getDoa())) {
                /*holder.timelineDoaFarm.setVisibility(View.VISIBLE);
                holder.timelineDoaFarm.setText(getDate(farm.getDoa()));*/
                holder.timelineDoaFarm.setVisibility(View.INVISIBLE);
            } else {
                holder.timelineDoaFarm.setVisibility(View.INVISIBLE);
            }

            Log.e("TestActivity", "Farm is not null");

            if (farm.getExpArea() != null && !TextUtils.isEmpty(farm.getExpArea())) {
                holder.timelineDisaptchArea.setText(convertAreaTo(farm.getExpArea()) + " " + area_unit_label);
            }
            if (farm.getActualArea() != null && !TextUtils.isEmpty(farm.getActualArea())) {
                holder.timelineSowingArea.setText(convertAreaTo(farm.getActualArea()) + " " + area_unit_label);
            }
            if (farm.getStandingAcres() != null && !TextUtils.isEmpty(farm.getStandingAcres())) {
                holder.timelineStandingArea.setText(convertAreaTo(farm.getStandingAcres()) + " " + area_unit_label);
            }

        } else {
            Log.e("TestActivity", "Farm is null");
        }
        holder.moreFarmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFarmDetailsClicked();
            }
        });

        holder.timelineCenterImageFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE))
                    listener.onMapIconClicked();
            }
        });
        if (position == lastPostion) {
            holder.timelineLine.setVisibility(View.GONE);
        } else {
            holder.timelineLine.setVisibility(View.VISIBLE);
        }

    }

    public int getCountOfDays(String farmDateStr, String todayDateStr) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date createdConvertedDate = null;
        Date expireCovertedDate = null;
        try {
            createdConvertedDate = dateFormat.parse(farmDateStr);
            expireCovertedDate = dateFormat.parse(todayDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar start = new GregorianCalendar();
        start.setTime(createdConvertedDate);

        Calendar end = new GregorianCalendar();
        end.setTime(expireCovertedDate);

        long diff = end.getTimeInMillis() - start.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        Log.e("TimelineAdapterTest", "Days: " + dayCount);
        return (int) (dayCount);
    }

    public void select_skycon(String icon, ImageView skycon_view) {
        switch (icon) {
            case "clear-day": {

                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_day));
                break;
            }
            case "clear-night": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_night));
                break;
            }
            case "rain": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rainy));
                break;
            }
            case "snow": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_snow));
                break;
            }
            case "sleet": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_sleet));
                break;
            }
            case "wind": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_windy));
                break;
            }
            case "fog": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_fog));
                break;
            }
            case "cloudy": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cloudy));
                break;
            }
            case "partly-cloudy-day": {

                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cloudy_day_2));
                break;
            }
            case "partly-cloudy-night": {

                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cloudy_night_2));
                break;
            }
            case "hail": {

                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_hail));
                break;
            }
            case "thunderstorm": {

                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thunder));
                break;
            }
        }
    }

    private void harvestDialog(Context context, List<HarvestDetailInnerData> innerData, HarvestDetailMaster harvestData) {

        TextView tv_harvest_done_date_dialog, tv_weightment_date_dialog, tv_harvested_area_dialog, tv_standing_areaa_dialog;
        LinearLayout bag_heading_layout_dialog;
        RecyclerView harvestBagRecycler_dialog;
        ImageView cancel_harvest_dialog,qrImageView,shareImage;
        CardView qrCard;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_harvest_details);
        Window window1 = dialog.getWindow();
        window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        qrCard = dialog.findViewById(R.id.qrCard);
        qrImageView = dialog.findViewById(R.id.qrImageView);
        shareImage = dialog.findViewById(R.id.shareImage);

        getImage(harvestData.getHarvestMasterId(),qrCard,qrImageView,shareImage);
        tv_harvest_done_date_dialog = dialog.findViewById(R.id.tv_harvest_done_date_dialog);
        tv_weightment_date_dialog = dialog.findViewById(R.id.tv_weightment_date_dialog);
        tv_harvested_area_dialog = dialog.findViewById(R.id.tv_harvested_area_dialog);
        tv_standing_areaa_dialog = dialog.findViewById(R.id.tv_standing_areaa_dialog);
        cancel_harvest_dialog = dialog.findViewById(R.id.cancel_harvest_dialog);
        bag_heading_layout_dialog = dialog.findViewById(R.id.bag_heading_layout_dialog);
        harvestBagRecycler_dialog = dialog.findViewById(R.id.harvestBagRecycler_dialog);

        tv_harvest_done_date_dialog.setText(harvestData.getHarvestDate());
        tv_weightment_date_dialog.setText(harvestData.getWeighmentDate());
        tv_harvested_area_dialog.setText(AppConstants.getShowableArea(harvestData.getHarvestedArea()) + " " + area_unit_label);
        tv_standing_areaa_dialog.setText(harvestData.getStandingArea() + " " + area_unit_label);
        if (innerData != null && innerData.size() > 0) {
            adapter = new HarvestBagAdapter(innerData, context);
            harvestBagRecycler_dialog.setAdapter(adapter);
            harvestBagRecycler_dialog.setHasFixedSize(false);
            linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            harvestBagRecycler_dialog.setLayoutManager(linearLayoutManager);
            adapter.notifyDataSetChanged();
            bag_heading_layout_dialog.setVisibility(View.VISIBLE);

        } else {
            bag_heading_layout_dialog.setVisibility(View.GONE);
        }
        harvestBagRecycler_dialog.setNestedScrollingEnabled(false);
        cancel_harvest_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_harvest_done_date_dialog.getParent().requestChildFocus(tv_harvest_done_date_dialog, tv_harvest_done_date_dialog);
        dialog.show();
    }

    private void getImage(String hmid,CardView cardView,ImageView imageView,ImageView shareImage) {
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getQrImage(hmid,"harvest").enqueue(new Callback<QRResponse>() {
            @Override
            public void onResponse(Call<QRResponse> call, Response<QRResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getQrCode() != null) {
                        cardView.setVisibility(View.VISIBLE);
                        setImageToImagevIew(response.body().getQrCode(),imageView,shareImage,cardView);
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<QRResponse> call, Throwable t) {

            }
        });


    }

    private void getQRImage(CardView cardView,ImageView imageView,ImageView shareImage){

        Bitmap bitmapOrg = BitmapFactory.decodeResource(context.getResources(), R.drawable.qr_image);
        setImageToImagevIew(getBase64String(bitmapOrg),imageView,shareImage,cardView);
    }
    private String getBase64String(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
        try {
            byteArrayOutputStreamObject.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(byteArrayOutputStreamObject.toByteArray(), Base64.DEFAULT);
    }
    private void setImageToImagevIew(String image,ImageView imageView,ImageView shareImage,CardView cardView) {
        try {
            String sharingPath="";
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            imageView.setImageBitmap(decodedByte);
            if (decodedByte != null) {
                cardView.setVisibility(View.VISIBLE);
                sharingPath = saveToInternalStorage(decodedByte);
                String finalSharingPath = sharingPath;
                shareImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        send(finalSharingPath);
                    }
                });
            }

//            else
//                AppConstants.failToast(context, "bitmap null");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     private String saveToInternalStorage(Bitmap bitmapImage) {
        // path to /data/data/yourapp/app_data/imageDir
        File direct = new File(Environment.getExternalStorageDirectory() + "/" + context.getResources().getString(R.string.app_name));
        if (!direct.exists())
            direct.mkdir();
        // Create imageDir
        FileOutputStream fos = null;
        String path = direct + "/" + "code.png";
        File oldFile = new File(path);
        if (oldFile.exists())
            oldFile.delete();
        File imageFile = new File(direct, "code.png");
        try {
            fos = new FileOutputStream(imageFile);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return imageFile.getAbsolutePath();
    }

    public void send(String sharingPath) {
        try {
            File myFile = new File(sharingPath);
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String ext = myFile.getName().substring(myFile.getName().lastIndexOf(".") + 1);
            String type = mime.getMimeTypeFromExtension(ext);
            Intent sharingIntent = new Intent("android.intent.action.SEND");
            sharingIntent.setType("image/*");
            sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(myFile));
            context.startActivity(Intent.createChooser(sharingIntent, "Share using"));
        } catch (Exception e) {
//            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            Log.e(TAG, "Failure To send" + e.getMessage());
        }
    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        activity2.requestPermissions(permissions, requestCode);
    }

    private void germinationDialog(Context context, SampleGerminationDatum sampleGerminationDatum) {

        LinearLayout top_linear_lay;
        TextView avg_pp_spacing_tv_show_sample, avg_rr_spacing_tv_show_sample,
                avg_germination_tv_show_sample, avg_farm_popu_tv_show_sample, sample_confi_area,
                sample_p_to_p_spacing, sample_r_to_r_spacing, sample_ideal_population, sample_actual_population,
                sample_germination, sample_total_popu, sample_title;

        ImageView cancel_germi_dialog;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.timeline_dialog_germi);
        Window window1 = dialog.getWindow();
        window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        cancel_germi_dialog = dialog.findViewById(R.id.cancel_germi_dialog);
        top_linear_lay = dialog.findViewById(R.id.top_linear_lay);
        top_linear_lay.setVisibility(View.VISIBLE);
        avg_pp_spacing_tv_show_sample = dialog.findViewById(R.id.avg_pp_spacing_tv_show_sample);
        avg_rr_spacing_tv_show_sample = dialog.findViewById(R.id.avg_rr_spacing_tv_show_sample);
        avg_germination_tv_show_sample = dialog.findViewById(R.id.avg_germination_tv_show_sample);
        avg_farm_popu_tv_show_sample = dialog.findViewById(R.id.avg_farm_popu_tv_show_sample);
        sample_title = dialog.findViewById(R.id.sample_title);
        sample_confi_area = dialog.findViewById(R.id.sample_confi_area);
        sample_p_to_p_spacing = dialog.findViewById(R.id.sample_p_to_p_spacing);
        sample_r_to_r_spacing = dialog.findViewById(R.id.sample_r_to_r_spacing);
        sample_ideal_population = dialog.findViewById(R.id.sample_ideal_population);
        sample_actual_population = dialog.findViewById(R.id.sample_actual_population);
        sample_germination = dialog.findViewById(R.id.sample_germination);
        sample_total_popu = dialog.findViewById(R.id.sample_total_popu);

        if (TestActivity.AVG_PP_SPACING != null)
            avg_pp_spacing_tv_show_sample.setText(TestActivity.AVG_PP_SPACING);
        if (TestActivity.AVG_RR_SPACING != null)
            avg_rr_spacing_tv_show_sample.setText(TestActivity.AVG_RR_SPACING);
        if (TestActivity.AVG_GERMI != null)
            avg_germination_tv_show_sample.setText(TestActivity.AVG_GERMI);
        if (TestActivity.AVG_POPU != null)
            avg_farm_popu_tv_show_sample.setText(TestActivity.AVG_POPU);


        if (sampleGerminationDatum.getConfiguredArea() != null) {
            sample_confi_area.setText(sampleGerminationDatum.getConfiguredArea());
        }
        if (sampleGerminationDatum.getSpacingPtp() != null) {
            sample_p_to_p_spacing.setText(sampleGerminationDatum.getSpacingPtp());
        }
        if (sampleGerminationDatum.getSpacingRtr() != null) {
            sample_r_to_r_spacing.setText(sampleGerminationDatum.getSpacingRtr());
        }
        if (sampleGerminationDatum.getActualPop() != null) {
            sample_actual_population.setText(sampleGerminationDatum.getActualPop());
            //actualPop=Double.valueOf(sampleGerminationDatum.getActualPop());
        }
        if (sampleGerminationDatum.getIdealPop() != null) {
            sample_ideal_population.setText(sampleGerminationDatum.getIdealPop());
            //idealPop=Double.valueOf(sampleGerminationDatum.getIdealPop());
        }
        if (sampleGerminationDatum.getActualTotalPopulation() != null) {
            sample_total_popu.setText(sampleGerminationDatum.getActualTotalPopulation());
        }
        if (sampleGerminationDatum.getGermination() != null) {
            sample_germination.setText(String.valueOf(sampleGerminationDatum.getGermination()));
        }
        sample_title.setText(context.getResources().getString(R.string.sample_label));

      /*  if (SharedPreferencesMethod.getBoolean(context,SharedPreferencesMethod.OFFLINE_MODE)){
            double germination = actualPop / idealPop * 100.0;
            holder.sample_germination.setText(String.valueOf(String.format("%.2f", germination)));
        }*/

        cancel_germi_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    boolean isOneChanging = false;
    boolean isTwoChanging = false;
    int isAllFilled = 0;

    private void sellDialog(Context context, SellData sellData) {

        TextView addedByTv, rateLabelTv;
        EditText timelineQtyTv, timelineTotalAmmount,
                timelineRate, timelineSoldOn;
        Button updateTv;

        ImageView cancelSellDialog;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.timeline_dialog_sell);
        Window window1 = dialog.getWindow();
        window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        cancelSellDialog = dialog.findViewById(R.id.cancelSellDialog);
        timelineQtyTv = dialog.findViewById(R.id.timelineQtyTv);
        timelineTotalAmmount = dialog.findViewById(R.id.timelineTotalAmmount);
        timelineRate = dialog.findViewById(R.id.timelineRate);
        timelineSoldOn = dialog.findViewById(R.id.timelineSoldOn);
        addedByTv = dialog.findViewById(R.id.addedByTv);
        rateLabelTv = dialog.findViewById(R.id.rateLabelTv);
        updateTv = dialog.findViewById(R.id.updateTv);

        if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR)||
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.ADMIN)||
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUB_ADMIN)||
                SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPER_ADMIN)) {
            timelineQtyTv.setFocusable(true);
            timelineTotalAmmount.setFocusable(true);
            timelineRate.setFocusable(true);
            timelineSoldOn.setClickable(true);
        } else {
            updateTv.setVisibility(View.GONE);
            timelineQtyTv.setFocusable(false);
            timelineTotalAmmount.setFocusable(false);
            timelineRate.setFocusable(false);
            timelineSoldOn.setClickable(false);

        }

        rateLabelTv.setText(context.getResources().getString(R.string.rate_label) + "(" + context.getResources().getString(R.string.per_kg_label) + ")");
        addedByTv.setText("* " + context.getResources().getString(R.string.added_by_label) + " " + sellData.getName());
        float amount = 0;
        float rate = 0;
        float qty = 0;
        isOneChanging = false;
        isTwoChanging = false;
        isAllFilled = 0;
        if (sellData.getSoldOn() != null && !TextUtils.isEmpty(sellData.getSoldOn())) {
            timelineSoldOn.setText(AppConstants.getShowableDate(sellData.getSoldOn(),context));
            timelineSoldOn.setClickable(false);
            timelineSoldOn.setEnabled(false);
            isAllFilled++;
        }

        if (sellData.getAmount() != null && !TextUtils.isEmpty(sellData.getAmount()) && Float.valueOf(sellData.getAmount()) > 0) {
            timelineTotalAmmount.setText(sellData.getAmount());
            timelineTotalAmmount.setClickable(false);
            timelineTotalAmmount.setEnabled(false);
            isAllFilled++;
            amount = Float.valueOf(sellData.getAmount().trim());
        }
        if (sellData.getRate() != null && !TextUtils.isEmpty(sellData.getRate()) && Float.valueOf(sellData.getRate().trim()) > 0) {
            timelineRate.setText(sellData.getRate());
            rate = Float.valueOf(sellData.getRate().trim());
            timelineRate.setClickable(false);
            timelineRate.setEnabled(false);
            isAllFilled++;
        }
        if (sellData.getSaleQty() != null && !TextUtils.isEmpty(sellData.getSaleQty())) {
            timelineQtyTv.setText(sellData.getSaleQty());
            qty = Float.valueOf(sellData.getSaleQty());
            timelineQtyTv.setClickable(false);
            timelineQtyTv.setEnabled(false);
            isAllFilled++;
        }


        float finalQty = qty;
        timelineTotalAmmount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isOneChanging = true;
                isTwoChanging = false;
                return false;
            }
        });

        timelineRate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isOneChanging = false;
                isTwoChanging = true;
                return false;
            }
        });
        timelineTotalAmmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!TextUtils.isEmpty(s.toString()) && !isTwoChanging) {
                    timelineRate.setText("" + Float.valueOf(s.toString()) / finalQty);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        timelineRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(s.toString()) && !isOneChanging) {
                    timelineTotalAmmount.setText("" + Float.valueOf(s.toString()) * finalQty);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datesowing = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                timelineSoldOn.setText(sdf.format(calendar.getTime()));
                timelineSoldOn.setError(null);
            }
        };
        timelineSoldOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, "data", Toast.LENGTH_SHORT).show();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, datesowing, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }
        });

        updateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAllFilled == 4) {
                    Toasty.info(context, context.getResources().getString(R.string.already_added_all_information_about_sell_msg), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else {

                    updateSell(dialog, sellData, timelineTotalAmmount.getText().toString().trim(),
                            timelineRate.getText().toString().trim(), timelineSoldOn.getText().toString().trim(),
                            timelineQtyTv.getText().toString().trim());
                }
            }
        });
        cancelSellDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        dialog.show();
    }


    public void notifyWeather(WeatherForecast weatherForecast, boolean isCurrentLocation) {
        try {
            this.isCurrentLocation = isCurrentLocation;
            this.weatherForecast = weatherForecast;
            TimelineAdapterTest.this.notifyDataSetChanged();
            Log.e("TimelineAdapterTest", "Notifying weather ");
        } catch (Exception e) {
            Log.e("TimelineAdapterTest", "EXception ", e);
        }

    }

    private String convertAreaTo(String area) {
        return AppConstants.getShowableArea(area);
    }

    public interface FarmDetailsClickListener {

        public void onFarmDetailsClicked();

        public void onMapIconClicked();

    }

    private void updateSell(Dialog dialog, SellData data, String amount, String rate, String soldOn, String qty) {
        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        JsonObject obj=new JsonObject();
        obj.addProperty("user_id",""+SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID));
        obj.addProperty("token",""+SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN));
        obj.addProperty("harvest_sale_id",""+data.getHarvestSaleId());
        obj.addProperty("amount",""+amount);
        obj.addProperty("rate",""+rate);
        obj.addProperty("sold_on",""+AppConstants.getUploadableDate(soldOn,context));
        obj.addProperty("sale_qty",""+qty);
        Log.e(TimelineAdapterTest.class.getSimpleName(),"Sending Data "+new Gson().toJson(obj));

        apiInterface.updateProduceSell(obj).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 10) {
                        new ViewFailDialog().showSessionExpireDialog(activity2);
                    } else if (response.body().getStatus() == 1) {
                        data.setAmount(amount);
                        data.setRate(rate);
                        if (!TextUtils.isEmpty(soldOn))
                            data.setSoldOn(AppConstants.getUploadableDate(soldOn,context));
                        Toasty.success(context, activity2.getResources().getString(R.string.sell_record_updated_success_msg) + ".", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    } else {
                        Toasty.error(context, activity2.getResources().getString(R.string.something_went_wrong_msg) + ", " +
                                activity2.getResources().getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(activity2, activity2.getResources().getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(activity2, activity2.getResources().getString(R.string.authorization_expired));
                } else {
                    try {
                        Log.e("TestAdapterTest", "Sell Err " + response.errorBody().string());
                        Toasty.error(context, activity2.getResources().getString(R.string.something_went_wrong_msg) + ", " +
                                activity2.getResources().getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {
                Log.e("TestAdapterTest", "Sell Fail " + t.toString());
                Toasty.error(context, activity2.getResources().getString(R.string.something_went_wrong_msg) + ", " +
                        activity2.getResources().getString(R.string.please_try_again_later_msg), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        dialog.dismiss();
    }

}
