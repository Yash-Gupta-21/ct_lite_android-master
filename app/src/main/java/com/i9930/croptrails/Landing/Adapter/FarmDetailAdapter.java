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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.charts.Chart;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.Landing.Models.ad.AdFarmDatum;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SoilSense.Dashboard.SoilSensDashboardActivity;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static java.lang.Double.parseDouble;

/**
 * Created by hp on 03-07-2018.
 */
public class FarmDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    boolean isOnlySoilSens=false;

    public FarmDetailAdapter(Context context, List<AdFarmDatum> list) {
        this.context = context;
        fetchFarmResults = list;
//        Toast.makeText(context, "size "+list.size(), Toast.LENGTH_SHORT).show();
        this.fetchFarmResultsFiltered = fetchFarmResults;

        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();
        area_unit_label = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL);
        isOnlySoilSens=SharedPreferencesMethod.getBoolean(context,SharedPreferencesMethod.IS_ONLY_SOIL_SENS);
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
        FetchFarmResult fetchFarmResult = null;
        fetchFarmResult = getFetchFarmResults().get(position).getFetchFarmResult();

        switch (getItemViewType(position)) {
            case ITEM:
                ViewHolder holder=(ViewHolder)holderr;
                if (position == 0) {
                }
                String firstLetter = "";
                String cap = "";
                if (fetchFarmResult.getName() != null&&fetchFarmResult.getName().length()>0) {

                    firstLetter = fetchFarmResult.getName().substring(0, 1).toUpperCase();
                    cap = fetchFarmResult.getName().substring(1);
                }
                String village = "";
                String mandal_or_tehsil = "";
                String district = "";
                String state = "";
                String country = "";
                String final_address = "-";
                if (fetchFarmResult.getFvillage_or_city() != null) {
                    village = fetchFarmResult.getFvillage_or_city();
                } else {
                    village = "";
                }
                if (fetchFarmResult.getFmandal_or_tehsil() != null) {
                    mandal_or_tehsil = fetchFarmResult.getFmandal_or_tehsil();
                } else {
                    mandal_or_tehsil = "";
                }

                if (fetchFarmResult.getFdistrict() != null) {
                    district = fetchFarmResult.getFdistrict();
                } else {
                    district = "";
                }
                if (fetchFarmResult.getFstate() != null) {
                    state = fetchFarmResult.getFstate();
                } else {
                    state = "";
                }
                if (fetchFarmResult.getFcountry() != null) {
                    country = fetchFarmResult.getFcountry();
                } else {
                    country = "";
                }


                final_address = village + " " + mandal_or_tehsil + " " + district + " " + state + " " + country;

                if (!final_address.trim().equals("")) {
                    holder.farm_address.setText(final_address.trim());
                } else {
                    //  holder.tv_addres_linear_lay.setVisibility(View.GONE);
                    holder.farm_address.setText("-");
                }
                String standingArea = fetchFarmResult.getStandingAcres();
                String actualArea = fetchFarmResult.getActualArea();
                String expectedArea = fetchFarmResult.getExpArea();

                if (fetchFarmResult.getFarmId() != null &&
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID) != null &&
                        fetchFarmResult.getFarmId().trim().equals(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID).trim())) {
                    standingArea = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES);
                    actualArea = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA);
                    expectedArea = "" + SharedPreferencesMethod.getDoubleSharedPreferences(context, SharedPreferencesMethod.EXPECTED_AREA);
                }

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
                    holder.exp_area_in_acer.setText(String.format("%.2f", stdArea) + " " + area_unit_label);
                    holder.farm_area_label.setText(resources.getString(R.string.standing_area_label));
                } else if (actAre > 0) {
                    holder.exp_area_in_acer.setText(String.format("%.2f", actAre) + " " + area_unit_label);
                    holder.farm_area_label.setText(resources.getString(R.string.growing_area_msg));
                } else {
                    if ((expAre > 0)) {
                        holder.exp_area_in_acer.setText(String.format("%.2f", expAre) + " " + area_unit_label);
                        holder.farm_area_label.setText(resources.getString(R.string.expected_area_label));
                    } else {
                        holder.farm_area_label.setText(resources.getString(R.string.expected_area_label));
                        holder.exp_area_in_acer.setText("0 " + area_unit_label);
                    }
                }

                if (holder.dateLabel != null)
                    holder.dateLabel.setText(resources.getString(R.string.sowing_date_label));
                if (holder.fatherSpouseLableTv != null)
                    holder.fatherSpouseLableTv.setText(resources.getString(R.string.spouse));
                if (fetchFarmResult.getSowingDate() != null) {
                    if (!fetchFarmResult.getSowingDate().equals(AppConstants.for_date)) {
                        holder.farm_harvest_date.setText(AppConstants.getShowableDate(fetchFarmResult.getSowingDate(),context));
                        // Log.e("harvD", fetchFarmResult.getExpHarvestDate());
                        //Log.e("harvD", fetchFarmResult.getActualHarvestDate());


                    } else {
                        holder.farm_harvest_date.setText("-");
                        Log.e("harvD", "-");
                    }
                } else {
                    holder.farm_harvest_date.setText("-");
                    Log.e("harvD", "-");

                }

                String sourceString = "<b>" + firstLetter + cap + " (" + fetchFarmResult.getLotNo() + ")" + "</b>";
                holder.farm_lot_no.setText(Html.fromHtml(sourceString));
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

                int[] androidColors = resources.getIntArray(R.array.androidcolors);
                if (currentcolor < 3)
                    currentcolor = currentcolor + 1;
                else
                    currentcolor = 0;
                GradientDrawable bgShape = (GradientDrawable) holder.farmer_name_single.getBackground();
                bgShape.setColor(androidColors[currentcolor]);
                if (fetchFarmResult.getSpouseName() != null)
                    holder.farm_farmer_fathers_name.setText(fetchFarmResult.getSpouseName());
                else
                    holder.farm_farmer_fathers_name.setText("-");

                holder.farmer_name_single.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onOnlineFarmClick(v, position);
                        //int position = getAdapterPosition();
                        final FetchFarmResult fetchFarmResult = fetchFarmResultsFiltered.get(position).getFetchFarmResult();
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTUAL_AREA, fetchFarmResult.getActualArea().trim());
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, fetchFarmResult.getStandingAcres().trim());
                        SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.EXPECTED_AREA,
                                Float.valueOf(fetchFarmResult.getExpArea()));
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, fetchFarmResult.getFarmId());
                        //  Log.e("TAG", fetchFarmResult.getName());
                        Intent intent = new Intent(context, TestActivity.class);

                        if (isOnlySoilSens){
                            intent=new Intent(context, SoilSensDashboardActivity.class);
                        }

                        ActivityOptions options = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            v.getContext().startActivity(intent, options.toBundle());
                        } else {
                            v.getContext().startActivity(intent);
                        }
                    }
                });

                holder.farmer_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position);
                    }
                });

                holder.farm_lot_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position);
                    }
                });

                holder.exp_area_in_acer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position);
                    }
                });

                holder.whole_cv_farm_frag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position);
                    }
                });


                holder.farm_harvest_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position);
                    }
                });

                holder.farm_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position);
                    }
                });


                holder.farm_farmer_fathers_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position);
                    }
                });


                holder.call_farmer_butt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final FetchFarmResult fetchFarmResult = fetchFarmResultsFiltered.get(position).getFetchFarmResult();
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
                        // Log.e("TAG", fetchFarmResult.getName());
                    }
                });
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

    public int getItemCount() {
        return fetchFarmResults == null ? 0 : fetchFarmResults.size();

    }

    @Override
    public int getItemViewType(int position) {
        return fetchFarmResults.get(position).getType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView farmer_name_single, farm_lot_no, exp_area_in_acer, farmer_name, farm_harvest_date, farm_address, expectedOrActualTV, farm_farmer_fathers_name;
        ImageView call_farmer_butt;
        TextView farm_area_label, dateLabel, fatherSpouseLableTv;
        TextView exp_area_tv, stand_area_tv;
        LinearLayout tv_addres_linear_lay, tv_exp_area_stand_area_lay;
        CardView whole_cv_farm_frag;

        CircleImageView farmerImage;

        public ViewHolder(View v) {
            super(v);
            farmerImage = v.findViewById(R.id.farmerImage);
            fatherSpouseLableTv = v.findViewById(R.id.fatherSpouseLableTv);
            dateLabel = v.findViewById(R.id.dateLabel);
            farmer_name_single = (TextView) v.findViewById(R.id.farmer_single_tv);
            farmer_name = (TextView) v.findViewById(R.id.farmer_name);
            farm_lot_no = (TextView) v.findViewById(R.id.farm_lot_no);
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
    }


    public interface FarmDetailAdapterListener {
        void OnFarmSelected(FetchFarmResult fetchFarmResult);
    }


    void onOnlineFarmClick(View v, int position) {
        final FetchFarmResult fetchFarmResult = fetchFarmResultsFiltered.get(position).getFetchFarmResult();
        Log.e("FarmDetailAdapter", "Farm Data " + new Gson().toJson(fetchFarmResult));
        /*if (fetchFarmResult.getActualArea() != null) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTUAL_AREA, fetchFarmResult.getActualArea().trim());
        }
        if (fetchFarmResult.getStandingAcres() != null) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, fetchFarmResult.getStandingAcres().trim());
        }
        if (fetchFarmResult.getExpArea() != null) {
            SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.EXPECTED_AREA,
                    Float.valueOf(fetchFarmResult.getExpArea()));
        }*/

        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, fetchFarmResult.getFarmId());
//        Log.e("TAG", fetchFarmResult.getName());
        Log.e("TAG", "farm_id " + fetchFarmResult.getFarmerId());
        Intent intent = new Intent(context, TestActivity.class);
        if (isOnlySoilSens){
            intent=new Intent(context, SoilSensDashboardActivity.class);
        }
        //Intent intent = new Intent(context, FarmDashboard.class);
        ActivityOptions options = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.getContext().startActivity(intent, options.toBundle());
        } else {
            v.getContext().startActivity(intent);
        }
    }

    void onExpAreaClick(View v, int position) {

    }


    public void add(AdFarmDatum fetchFarmResult) {
        fetchFarmResults.add(fetchFarmResult);
        notifyItemInserted(fetchFarmResults.size() - 1);

    }



    public void remove(AdFarmDatum fetchFarmResult) {
        int position = fetchFarmResults.indexOf(fetchFarmResult);
        if (position > -1) {
            fetchFarmResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public void clearAll() {
       /* if(fetchFarmResults.size()>0) {
            for (FetchFarmResult fetchFarmResult : fetchFarmResults) {
                remove(fetchFarmResult);
            }
        }*/
        fetchFarmResults.clear();
        notifyDataSetChanged();
        // notifyItemRangeChanged(0,fetchFarmResults.size());
    }

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

    public class LoadingVH extends FarmDetailAdapter.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

}

