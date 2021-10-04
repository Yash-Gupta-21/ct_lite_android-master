package com.i9930.croptrails.Landing.Adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import com.i9930.croptrails.Landing.Models.NewData.FetchFarmResultNew;
import com.i9930.croptrails.Landing.Models.ad.AdFarmDatum;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Test.FarmDetailsVettingActivity;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Test.adapter.TimelineAdapterTest;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static android.content.ContentValues.TAG;
import static java.lang.Double.parseDouble;

public class VettingFarmAdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface ItemClickListener {
        public void onItemClicked(int index, FetchFarmResultNew farmResultNew, String isVetting);
    }

    private String[] mDataSet;
    int[] colours = {R.color.dashboardcolo1, R.color.dashboardcolor2, R.color.dashboardcolor3, R.color.dashboardcolor4};
    int currentcolor = 0;
    private static final int ITEM = 1;
    private static final int LOADING = 2;
    private static final int AD = 0;
    private boolean isLoadingAdded = false;
    Context context;
    List<AdFarmDatum> fetchFarmResults;
    List<AdFarmDatum> fetchFarmResultsFiltered;
    Resources resources;
    String area_unit_label = "";
    final String TAG = "FarmDetailAdapter";
    boolean clickEvent = false;
    boolean isLongPressed = false;
    int count = 0;
    ItemClickListener listener;

    public interface OnLongPressListener {
        public void onLongPressed(int index);
    }

    OnLongPressListener longPressListener;

    public VettingFarmAdapterNew(Context context, List<AdFarmDatum> vettingFarms, ItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        if (vettingFarms == null)
            fetchFarmResults = new ArrayList<>();
        else
            fetchFarmResults = vettingFarms;
        this.fetchFarmResultsFiltered = fetchFarmResults;
        clickEvent = true;
        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();
        area_unit_label = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL);

    }

    public List<AdFarmDatum> getFetchFarmResults() {
        return fetchFarmResults;
    }




    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case AD:
                viewHolder = getAdViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }

        return viewHolder;

    }

    @NonNull
    private ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.fetch_farm_content, parent, false);
        viewHolder = new ViewHolder(v1);

        return viewHolder;
    }

    @NonNull
    private BannerHolder getAdViewHolder(ViewGroup parent, LayoutInflater inflater) {
        BannerHolder viewHolder;
        View v1 = inflater.inflate(R.layout.rv_layout_banner_ad, parent, false);
        viewHolder = new BannerHolder(v1);

        return viewHolder;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holderr, final int position) {
        final FetchFarmResultNew fetchFarmResult = getFetchFarmResults().get(position).getFetchFarmResultNew();

        switch (getItemViewType(position)) {
            case ITEM:
                ViewHolder holder=(ViewHolder)holderr;
                final String vetting = fetchFarmResult.getVetting();
                GradientDrawable bgShape = (GradientDrawable) holder.farmer_name_single.getBackground();
                bgShape.setColor(resources.getColor(R.color.darkgreen));


                /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        longPressed(position);
                        return false;
                    }
                });
                holder.call_farmer_butt.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        longPressed(position);
                        return false;
                    }
                });
                holder.contentLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        longPressed(position);
                        return false;
                    }
                });
                holder.farm_address.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        longPressed(position);
                        return false;
                    }
                });
                holder.whole_cv_farm_frag.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        longPressed(position);
                        return false;
                    }
                });
                holder.farm_area_label.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        longPressed(position);
                        return false;
                    }
                });


                holder.farm_lot_no.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        longPressed(position);
                        return false;
                    }
                });
                holder.farmer_name.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        longPressed(position);
                        return false;
                    }
                });
                holder.farm_harvest_date.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        longPressed(position);
                        return false;
                    }
                });
                holder.fatherSpouseLableTv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        longPressed(position);
                        return false;
                    }
                });
                holder.sowingDateLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        longPressed(position);
                        return false;
                    }
                });
                holder.farmer_name_single.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        longPressed(position);
                        return false;
                    }
                });
                holder.tv_addres_linear_lay.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        longPressed(position);
                        return false;
                    }
                });*/

                if (AppConstants.isValidString(vetting) && !vetting.trim().equals(AppConstants.VETTING.REJECTED)) {

                    if (vetting.trim().equals(AppConstants.VETTING.SELECTED)) {
                        holder.contentLayout.setBackgroundResource(R.drawable.vetting_selected);
                        holder.moreOptionImage.setVisibility(View.GONE);
                        currentcolor = R.color.darkgreen;


                    } else if (vetting.trim().equals(AppConstants.VETTING.FRESH)) {
                        holder.contentLayout.setBackgroundResource(R.drawable.vetting_pending);
                        holder.moreOptionImage.setVisibility(View.GONE);
                        holder.moreOptionImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PopupMenu popup = new PopupMenu(context, holder.moreOptionImage);
                                popup.inflate(R.menu.menu_for_fresh);
                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.menuSelectFarm:
                                                //handle menu1 click
                                                break;
                                            case R.id.menuReEntryFarm:
                                                //handle menu2 click
                                                break;
                                            case R.id.menuRejectFarm:
                                                //handle menu3 click
                                                break;
                                        }
                                        return false;
                                    }
                                });
                                //displaying the popup
                                popup.show();
                            }
                        });

                        currentcolor = R.color.yellow;
                        bgShape.setColor(resources.getColor(R.color.yellow));
                    } else if (vetting.trim().equals(AppConstants.VETTING.DATA_ENTRY)) {
                        holder.contentLayout.setBackgroundResource(R.drawable.vetting_data_entry);
                        holder.moreOptionImage.setVisibility(View.GONE);
                        currentcolor = R.color.orange;
                        bgShape.setColor(resources.getColor(R.color.orange));
                    } else if (vetting.trim().equals(AppConstants.VETTING.DELINQUENT)) {
                        holder.contentLayout.setBackgroundResource(R.drawable.vetting_delinquent);
                        holder.moreOptionImage.setVisibility(View.GONE);
                        currentcolor = R.color.darkpurple;
                        bgShape.setColor(resources.getColor(R.color.darkpurple));
                    } else {
                        holder.contentLayout.setBackgroundResource(R.drawable.vetting_rejected);
                        currentcolor = R.color.red;
                        bgShape.setColor(resources.getColor(R.color.red));
                    }
                } else {
                    holder.contentLayout.setBackgroundResource(R.drawable.vetting_rejected);
                    bgShape.setColor(resources.getColor(R.color.red));
                }

                /*int s=position%4;
                if (s==1){
                    holder.contentLayout.setBackgroundResource(R.drawable.vetting_selected);
                }else if (s==2){
                    holder.contentLayout.setBackgroundResource(R.drawable.vetting_pending);
                }else if (s==3){
                    holder.contentLayout.setBackgroundResource(R.drawable.vetting_data_entry);
                }else {
                    holder.contentLayout.setBackgroundResource(R.drawable.vetting_rejected);
                }*/

                String firstLetter = "";
                String cap = "";
                String farmerName = "";
                String addl1 = "";
                String addl2 = "";
                if (fetchFarmResult.getFarmerName() != null && fetchFarmResult.getFarmerName().length() > 1) {
                    farmerName = fetchFarmResult.getFarmerName();
                    firstLetter = fetchFarmResult.getFarmerName().substring(0, 1).toUpperCase();
                    cap = fetchFarmResult.getFarmerName().substring(1);
                }
                String village = "";
                String mandal_or_tehsil = "";
                String district = "";
                String state = "";
                String country = "";
                String final_address = "-";
                if (fetchFarmResult.getVillageOrCity() != null && !fetchFarmResult.getVillageOrCity().equals("0")) {
                    village = fetchFarmResult.getVillageOrCity();
                } else {
                    village = "";
                }

                if (fetchFarmResult.getAddL1() != null && !fetchFarmResult.getAddL1().equals("0")) {
                    addl1 = fetchFarmResult.getAddL1();
                } else {
                    addl1 = "";
                }
                if (fetchFarmResult.getAddL2() != null && !fetchFarmResult.getAddL2().equals("0")) {
                    addl2 = fetchFarmResult.getAddL2();
                } else {
                    addl2 = "";
                }

                if (fetchFarmResult.getMandalOrTehsil() != null && !fetchFarmResult.getMandalOrTehsil().equals("0")) {
                    mandal_or_tehsil = fetchFarmResult.getMandalOrTehsil();
                } else {
                    mandal_or_tehsil = "";
                }

                if (fetchFarmResult.getDistrict() != null && !fetchFarmResult.getDistrict().equals("0")) {
                    district = fetchFarmResult.getDistrict();
                } else {
                    district = "";
                }

                if (fetchFarmResult.getState() != null && !fetchFarmResult.getState().equals("0")) {
                    state = fetchFarmResult.getState();
                } else {
                    state = "";
                }
                if (fetchFarmResult.getCountry() != null && !fetchFarmResult.getCountry().equals("0")) {
                    country = fetchFarmResult.getCountry();
                } else {
                    country = "";
                }
                String line = "";
                if (AppConstants.isValidString(addl1) && !addl1.equals("0"))
                    line = addl1;
                if (AppConstants.isValidString(addl2) && !addl2.equals("0"))
                    if (AppConstants.isValidString(line))
                        line = line + ", " + addl2 + ",";
                    else
                        line = addl2 + ",";
                final_address = line + " " + village + " " + mandal_or_tehsil + " " + district + " " + state + " " + country;

                if (!final_address.trim().equals("")) {
                    holder.farm_address.setText(final_address.trim());
                } else {
                    //  holder.tv_addres_linear_lay.setVisibility(View.GONE);
                    holder.farm_address.setText("-");
                }
                String standingArea = "" + fetchFarmResult.getStandingArea();
                String actualArea = "" + fetchFarmResult.getActualArea();
                String expectedArea = "" + fetchFarmResult.getExpArea();
                String sowingDate = fetchFarmResult.getSowingDate();
                Log.e(TAG, position + " 1 standingArea" + standingArea + ", actualArea" + actualArea + ", expectedArea" + expectedArea);
                if (fetchFarmResult.getFarmMasterNum() != null &&
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID) != null &&
                        fetchFarmResult.getFarmMasterNum().trim().equals(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID).trim())) {

                    standingArea = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES);
                    actualArea = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA);
                    expectedArea = "" + SharedPreferencesMethod.getDoubleSharedPreferences(context, SharedPreferencesMethod.EXPECTED_AREA);
                    sowingDate = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SOWING_DATE);
                }
                Log.e(TAG, position + " 2 standingArea" + standingArea + ", actualArea" + actualArea + ", expectedArea" + expectedArea);

                double stdArea = 0.0, expAre = 0.0, actAre = 0.0;
                try {
                    if (standingArea != null && !TextUtils.isEmpty(standingArea))
                        stdArea = parseDouble(standingArea.trim());
                } catch (Exception e) {

                }
                try {
                    if (expectedArea != null && !TextUtils.isEmpty(expectedArea))
                        expAre = parseDouble(expectedArea.trim());
                } catch (Exception e) {

                }
                try {
                    if (actualArea != null && !TextUtils.isEmpty(actualArea))
                        actAre = parseDouble(actualArea.trim());
                } catch (Exception e) {

                }

                if ((stdArea > 0)) {
                    holder.exp_area_in_acer.setText(String.format("%.4f", stdArea
                    ) + " " + area_unit_label);
                    holder.farm_area_label.setText(resources.getString(R.string.standing_area_label));
                } else if (actAre > 0) {
                    holder.exp_area_in_acer.setText(String.format("%.4f", actAre
                    ) + " " + area_unit_label);
                    holder.farm_area_label.setText(resources.getString(R.string.growing_area_msg));
                } else {
                    if ((expAre > 0)) {
                        holder.exp_area_in_acer.setText(String.format("%.4f", expAre
                        ) + " " + area_unit_label);
                        holder.farm_area_label.setText(resources.getString(R.string.expected_area_label));
                    } else {
                        holder.farm_area_label.setText(resources.getString(R.string.expected_area_label));
                        holder.exp_area_in_acer.setText("0 " + area_unit_label);
                    }
                }
                holder.dateLabel.setText(resources.getString(R.string.sowing_date_label));
                holder.fatherSpouseLableTv.setText(resources.getString(R.string.spouse));

                if (sowingDate != null && AppConstants.isValidString(sowingDate)) {
                    if (!sowingDate.equals(AppConstants.for_date)) {
                        holder.farm_harvest_date.setText(AppConstants.getShowableDate(sowingDate, context));
                        holder.sowingDateLayout.setVisibility(View.VISIBLE);
                        holder.bottomBarLayout.setWeightSum(3);
                    } else {
                        holder.farm_harvest_date.setText("-");
                        Log.e("harvD", "-");
                        holder.sowingDateLayout.setVisibility(View.GONE);
                        holder.bottomBarLayout.setWeightSum(2);
                    }
                } else {
                    holder.farm_harvest_date.setText("-");
                    holder.sowingDateLayout.setVisibility(View.GONE);
                    holder.bottomBarLayout.setWeightSum(2);
                    Log.e("harvD", "-");
                }
                String lot = "";
                if (AppConstants.isValidString(fetchFarmResult.getLotNo()))
                    lot = fetchFarmResult.getLotNo();
//                String sourceString = "<b>" + firstLetter + cap +  "</b>";
//                String sourceString1 = sourceString+"<br> " + "(" + lot + ")" + "";

                String sourceString = firstLetter + cap;
                String sourceString1 = sourceString + "<br> " + "(" + lot + ")" + "";

                holder.farm_lot_no.setText(Html.fromHtml(sourceString1));
                holder.farmer_name_single.setText(firstLetter);

                if (AppConstants.isValidString(fetchFarmResult.getImgLink())){
                    holder.farmerImage.setVisibility(View.VISIBLE);
                    holder.farmer_name_single.setVisibility(View.GONE);
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.ic_launcher_round);
                    Glide.with(context).load(fetchFarmResult.getImgLink())
                            .apply(options).into(holder.farmerImage);
                }else{
                    holder.farmerImage.setVisibility(View.GONE);
                    holder.farmer_name_single.setVisibility(View.VISIBLE);
                }

                /*int[] androidColors = resources.getIntArray(R.array.androidcolors);
                if (currentcolor < 3)
                    currentcolor = currentcolor + 1;
                else
                    currentcolor = 0;*/

                if (AppConstants.isValidString(fetchFarmResult.getSpouseName())) {
                    holder.farm_farmer_fathers_name.setText(fetchFarmResult.getSpouseName());
                    holder.spouseLayout.setVisibility(View.VISIBLE);

                } else {
                    holder.farm_farmer_fathers_name.setText("-");
                    holder.spouseLayout.setVisibility(View.GONE);
                }
                String finalFarmerName = farmerName;
                String finalAddl = addl1;
               /* holder.farmer_name_single.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (clickEvent) {
                            onOnlineFarmClick(v, position, vetting, finalFarmerName, finalAddl);

                        }
                    }
                });
                holder.farmer_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position, vetting, finalFarmerName, finalAddl);
                    }
                });
                holder.farm_lot_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position, vetting, finalFarmerName, finalAddl);
                    }
                });

                holder.exp_area_in_acer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position, vetting, finalFarmerName, finalAddl);
                    }
                });

                holder.whole_cv_farm_frag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position, vetting, finalFarmerName, finalAddl);
                    }
                });

                holder.farm_harvest_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position, vetting, finalFarmerName, finalAddl);
                    }
                });

                holder.farm_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position, vetting, finalFarmerName, finalAddl);
                    }
                });

                holder.farm_farmer_fathers_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position, vetting, finalFarmerName, finalAddl);
                    }
                });*/

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position, vetting, finalFarmerName, finalAddl);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        if (longPressListener != null && !fetchFarmResult.isSelectedd()) {

                            fetchFarmResults.get(position).getFetchFarmResultNew().setSelectedd(true);
                            count++;
                            longPressListener.onLongPressed(count);
                            isLongPressed = true;
//                            longPressed(position);

                            try {
                                notifyDataSetChanged();
                            } catch (Exception e) {

                            }
                        }

                        return true;
                    }
                });
                holder.call_farmer_butt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (fetchFarmResult.getMob() != null) {
                            if (!fetchFarmResult.getMob().trim().equals("0") && !fetchFarmResult.getMob().equals("9999999999")) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + fetchFarmResult.getMob()));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            } else {
                                Toasty.info(context, resources.getString(R.string.mobile_number_not_availbale_msg),
                                        Toast.LENGTH_LONG, true).show();
                            }
                        } else {
                            Toasty.info(context, resources.getString(R.string.mobile_number_not_availbale_msg),
                                    Toast.LENGTH_LONG, true).show();
                        }
                    }
                });

                if (isLongPressed && longPressListener != null) {

                    holder.selectCheckbox.setChecked(fetchFarmResults.get(position).getFetchFarmResultNew().isSelectedd());

                    final boolean[] isTouch = {false};
                    holder.selectCheckbox.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            isTouch[0] = true;
                            return false;
                        }
                    });
                    holder.selectCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            fetchFarmResults.get(position).getFetchFarmResultNew().setSelectedd(isChecked);
                            if (isTouch[0]) {
                                if (isChecked) {
                                    count++;

                                } else
                                    count--;

                                longPressListener.onLongPressed(count);
                                isTouch[0]=false;
                            }
                        }
                    });
                    holder.selectCheckbox.setVisibility(View.VISIBLE);
                } else {
                    holder.selectCheckbox.setVisibility(View.GONE);
                }

                try {
                    holder.setIsRecyclable(false);
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;

            case AD:
                configureAD((BannerHolder)holderr,position);
                break;
            case LOADING:
                break;
        }
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }


    @Override
    public int getItemViewType(int position) {
        return fetchFarmResults.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return fetchFarmResults == null ? 0 : fetchFarmResults.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView farmer_name_single, farm_lot_no, exp_area_in_acer, farmer_name, farm_harvest_date, farm_address, expectedOrActualTV, farm_farmer_fathers_name;
        ImageView call_farmer_butt;
        TextView farm_area_label, dateLabel, fatherSpouseLableTv;
        TextView exp_area_tv, stand_area_tv;
        LinearLayout tv_addres_linear_lay, contentLayout, spouseLayout, bottomBarLayout, sowingDateLayout;
        CardView whole_cv_farm_frag;
        ImageView moreOptionImage;
        CheckBox selectCheckbox;
        CircleImageView farmerImage;

        public ViewHolder(View v) {
            super(v);
            farmerImage = v.findViewById(R.id.farmerImage);
            selectCheckbox = v.findViewById(R.id.selectCheckbox);
            sowingDateLayout = v.findViewById(R.id.sowingDateLayout);
            bottomBarLayout = v.findViewById(R.id.bottomBarLayout);
            moreOptionImage = v.findViewById(R.id.moreOptionImage);
            spouseLayout = v.findViewById(R.id.spouseLayout);
            contentLayout = v.findViewById(R.id.contentLayout);
            farmer_name_single = (TextView) v.findViewById(R.id.farmer_single_tv);
            farmer_name = (TextView) v.findViewById(R.id.farmer_name);
            farm_lot_no = (TextView) v.findViewById(R.id.farm_lot_no);
            fatherSpouseLableTv = v.findViewById(R.id.fatherSpouseLableTv);
            dateLabel = v.findViewById(R.id.dateLabel);
            exp_area_in_acer = (TextView) v.findViewById(R.id.exp_area_in_acer);
            farm_harvest_date = (TextView) v.findViewById(R.id.farm_harvest_date);
            farm_address = (TextView) v.findViewById(R.id.farm_address);
            /*expectedOrActualTV = v.findViewById(R.id.e5);*/
            farm_farmer_fathers_name = v.findViewById(R.id.farm_farmer_fathers_name);
            call_farmer_butt = v.findViewById(R.id.call_farmer_butt);
            tv_addres_linear_lay = v.findViewById(R.id.tv_addres_linear_lay);
            whole_cv_farm_frag = v.findViewById(R.id.whole_cv_farm_frag);
            farm_area_label = v.findViewById(R.id.farm_area_label);
        }

        @Override
        public void onClick(View v) {

//            onOnlineFarmClick(v, getAdapterPosition(), vetting, finalFarmerName, finalAddl);
        }

        @Override
        public boolean onLongClick(View v) {

            return true;
        }
    }

    public interface FarmDetailAdapterListener {
        void OnFarmSelected(FetchFarmResultNew fetchFarmResult);
    }

    void onOnlineFarmClick(View v, int position, String vetting, String farmerName, String addl1) {
        final FetchFarmResultNew fetchFarmResult = fetchFarmResultsFiltered.get(position).getFetchFarmResultNew();
        Log.e("FarmDetailAdapter", "Farm Data " + new Gson().toJson(fetchFarmResult));
        if (!isLongPressed) {
            if (clickEvent) {
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARMER_NAME, farmerName);
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.KHASRA, addl1);

                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, fetchFarmResult.getFarmMasterNum());
                Log.e("TAG", "farm_id " + fetchFarmResult.getFarmMasterNum());
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SOWING_DATE, fetchFarmResult.getSowingDate());
                listener.onItemClicked(position, fetchFarmResult, vetting);

            }
        }else {
            fetchFarmResultsFiltered.get(position).getFetchFarmResultNew().setSelectedd(true);
            try {
                notifyDataSetChanged();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    void onExpAreaClick(View v, int position) {

    }

    public boolean isClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(boolean clickEvent) {
        try {
            this.clickEvent = clickEvent;
            notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    public void add(AdFarmDatum fetchFarmResult) {
        fetchFarmResults.add(fetchFarmResult);
        notifyItemInserted(fetchFarmResults.size() - 1);

    }

   /* public void addAll(List<FetchFarmResultNew> fetchFarmResults) {
        if (fetchFarmResults != null && fetchFarmResults.size() > 0) {
            for (FetchFarmResultNew fetchFarmResult : fetchFarmResults) {
                add(fetchFarmResult);
            }
        }
    }*/

    public void remove(FetchFarmResultNew fetchFarmResult) {
        int position = fetchFarmResults.indexOf(fetchFarmResult);
        if (position > -1) {
            fetchFarmResults.remove(position);
            notifyItemRemoved(position);
        }
    }


    /*public void clearAll() {
     *//* if(fetchFarmResults.size()>0) {
            for (FetchFarmResultNew fetchFarmResult : fetchFarmResults) {
                remove(fetchFarmResult);
            }
        }*//*
        try {
            fetchFarmResults.clear();
            notifyDataSetChanged();
        }catch (Exception e){}
        // notifyItemRangeChanged(0,fetchFarmResults.size());
    }*/

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new AdFarmDatum(2));
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = fetchFarmResults.size() - 1;
        AdFarmDatum fetchFarmResult = getItem(position);

        if (fetchFarmResult != null) {
            fetchFarmResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public AdFarmDatum getItem(int position) {
        return fetchFarmResults.get(position);
    }

    public class LoadingVH extends ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public OnLongPressListener getLongPressListener() {
        return longPressListener;
    }

    public void setLongPressListener(OnLongPressListener longPressListener) {
        this.longPressListener = longPressListener;
    }

    public boolean isLongPressed() {
        return isLongPressed;
    }

    public void setLongPressed(boolean longPressed) {
        try {
            isLongPressed = longPressed;
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        try {
            this.count = count;
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
