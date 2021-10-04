package com.i9930.croptrails.Vetting.Adapter;

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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.Landing.Models.Vetting.VettingFarm;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.AppDatabase;
import com.i9930.croptrails.Test.FarmDetailsVettingActivity;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static java.lang.Double.parseDouble;

public class VettingFarmAdapter extends RecyclerView.Adapter<VettingFarmAdapter.ViewHolder> {
    private String[] mDataSet;
    int[] colours = {R.color.dashboardcolo1, R.color.dashboardcolor2, R.color.dashboardcolor3, R.color.dashboardcolor4};
    int currentcolor = 0;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    Context context;
    List<VettingFarm> fetchFarmResults;
    List<VettingFarm> fetchFarmResultsFiltered;
    Resources resources;
    String area_unit_label = "";
    final String TAG = "FarmDetailAdapter";
    boolean clickEvent = false;
    boolean isNavigateToTimeline=true;

    public VettingFarmAdapter(Context context, List<VettingFarm> vettingFarms) {
        this.context = context;
        if (vettingFarms == null)
            fetchFarmResults = new ArrayList<>();
        else
            fetchFarmResults = vettingFarms;
        this.fetchFarmResultsFiltered = fetchFarmResults;

        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();
        area_unit_label = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL);

    }

    public VettingFarmAdapter(Context context, List<VettingFarm> vettingFarms,boolean isNavigateToTimeline) {
        this.context = context;
        if (vettingFarms == null)
            fetchFarmResults = new ArrayList<>();
        else
            fetchFarmResults = vettingFarms;
        this.fetchFarmResultsFiltered = fetchFarmResults;
        this.isNavigateToTimeline=isNavigateToTimeline;
        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();
        area_unit_label = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL);

    }

    public List<VettingFarm> getFetchFarmResults() {
        return fetchFarmResults;
    }

    public void setFetchFarmResults(List<VettingFarm> fetchFarmResults) {
        this.fetchFarmResults = fetchFarmResults;
    }


    public VettingFarmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        VettingFarmAdapter.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }

        return viewHolder;

    }

    @NonNull
    private VettingFarmAdapter.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        VettingFarmAdapter.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.fetch_farm_content, parent, false);
        viewHolder = new ViewHolder(v1);

        return viewHolder;
    }

    public void onBindViewHolder(final VettingFarmAdapter.ViewHolder holder, final int position) {
        VettingFarm fetchFarmResult = null;
        fetchFarmResult = getFetchFarmResults().get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                if (position == 0) {
                }
                String firstLetter = "";
                String cap = "";
                if (fetchFarmResult.getName() != null) {

                    firstLetter = fetchFarmResult.getName().substring(0, 1).toUpperCase();
                    cap = fetchFarmResult.getName().substring(1);
                }
                String village = "";
                String mandal_or_tehsil = "";
                String district = "";
                String state = "";
                String country = "";
                String final_address = "-";
                if (fetchFarmResult.getVillageOrCity() != null) {
                    village = fetchFarmResult.getVillageOrCity();
                } else {
                    village = "";
                }
                if (fetchFarmResult.getMandalOrTehsil() != null) {
                    mandal_or_tehsil = fetchFarmResult.getMandalOrTehsil();
                } else {
                    mandal_or_tehsil = "";
                }

                if (fetchFarmResult.getDistrict() != null) {
                    district = fetchFarmResult.getDistrict();
                } else {
                    district = "";
                }

                if (fetchFarmResult.getState() != null) {
                    state = fetchFarmResult.getState();
                } else {
                    state = "";
                }
                if (fetchFarmResult.getCountry() != null) {
                    country = fetchFarmResult.getCountry();
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
                String standingArea = fetchFarmResult.getStandingArea();
                String actualArea = fetchFarmResult.getActualArea();
                String expectedArea = fetchFarmResult.getExpArea();

                Log.e(TAG,position+" 1 standingArea"+standingArea+", actualArea"+actualArea+", expectedArea"+expectedArea);
                if (fetchFarmResult.getFarmMasterNum() != null &&
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID) != null &&
                        fetchFarmResult.getFarmMasterNum().trim().equals(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID).trim())) {
                    standingArea = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES);
                    actualArea = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA);
                    expectedArea = "" + SharedPreferencesMethod.getDoubleSharedPreferences(context, SharedPreferencesMethod.EXPECTED_AREA);
                }
                Log.e(TAG,position+" 2 standingArea"+standingArea+", actualArea"+actualArea+", expectedArea"+expectedArea);

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
                    holder.exp_area_in_acer.setText(String.format("%.2f", stdArea
                    ) + " " + area_unit_label);
                    holder.farm_area_label.setText(resources.getString(R.string.standing_area_label));
                } else if (actAre > 0) {
                    holder.exp_area_in_acer.setText(String.format("%.2f", actAre
                    ) + " " + area_unit_label);
                    holder.farm_area_label.setText(resources.getString(R.string.growing_area_msg));
                } else {
                    if ((expAre > 0)) {
                        holder.exp_area_in_acer.setText(String.format("%.2f", expAre
                        ) + " " + area_unit_label);
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
                String lot = "";
                if (AppConstants.isValidString(fetchFarmResult.getLotNo()))
                    lot = fetchFarmResult.getLotNo();
                String sourceString = "<b>" + firstLetter + cap + " (" + lot + ")" + "</b>";
                holder.farm_lot_no.setText(Html.fromHtml(sourceString));
                holder.farmer_name_single.setText(firstLetter);
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

                        if (clickEvent) {
                            onOnlineFarmClick(v, position);
                            //int position = getAdapterPosition();
                            final VettingFarm fetchFarmResult = fetchFarmResultsFiltered.get(position);
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTUAL_AREA, fetchFarmResult.getActualArea().trim());
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, fetchFarmResult.getStandingArea().trim());
                            SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.EXPECTED_AREA,
                                    Float.valueOf(fetchFarmResult.getExpArea()));
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, fetchFarmResult.getFarmMasterNum());
                            //  Log.e("TAG", fetchFarmResult.getName());
                            Intent intent ;

                            if (isNavigateToTimeline)
                            intent= new Intent(context, TestActivity.class);
                            else intent= new Intent(context, FarmDetailsVettingActivity.class);
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
                        final VettingFarm fetchFarmResult = fetchFarmResultsFiltered.get(position);
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
                break;

            case LOADING:
                break;
        }
    }

    @Override
    public void onBindViewHolder(VettingFarmAdapter.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public int getItemCount() {
        return fetchFarmResults == null ? 0 : fetchFarmResults.size();

    }

    @Override
    public int getItemViewType(int position) {
        return (position == fetchFarmResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView farmer_name_single, farm_lot_no, exp_area_in_acer, farmer_name, farm_harvest_date, farm_address, expectedOrActualTV, farm_farmer_fathers_name;
        ImageView call_farmer_butt;
        TextView farm_area_label,dateLabel, fatherSpouseLableTv;
        TextView exp_area_tv, stand_area_tv;
        LinearLayout tv_addres_linear_lay, tv_exp_area_stand_area_lay;
        CardView whole_cv_farm_frag;
        public ViewHolder(View v) {
            super(v);
            farmer_name_single = (TextView) v.findViewById(R.id.farmer_single_tv);
            farmer_name = (TextView) v.findViewById(R.id.farmer_name);
            fatherSpouseLableTv = v.findViewById(R.id.fatherSpouseLableTv);
            dateLabel = v.findViewById(R.id.dateLabel);
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
        final VettingFarm fetchFarmResult = fetchFarmResultsFiltered.get(position);
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
        if (clickEvent) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, fetchFarmResult.getFarmMasterNum());
            Log.e("TAG", "farm_id " + fetchFarmResult.getFarmMasterNum());
            Intent intent=null;
            if (isNavigateToTimeline)
                intent= new Intent(context, TestActivity.class);
            else intent= new Intent(context, FarmDetailsVettingActivity.class);
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

    public void add(VettingFarm fetchFarmResult) {
        fetchFarmResults.add(fetchFarmResult);
        notifyItemInserted(fetchFarmResults.size() - 1);

    }

    public void addAll(List<VettingFarm> fetchFarmResults) {
        for (VettingFarm fetchFarmResult : fetchFarmResults) {
            add(fetchFarmResult);
        }
    }

    public void remove(VettingFarm fetchFarmResult) {
        try {
            int position = fetchFarmResults.indexOf(fetchFarmResult);
            if (position > -1) {
                fetchFarmResults.remove(position);
                notifyItemRemoved(position);
            }
        }catch (Exception e){}
    }


    public void clearAll() {
        try {
            if(fetchFarmResults.size()>0) {
                for (VettingFarm fetchFarmResult : fetchFarmResults) {
                    remove(fetchFarmResult);
                }
            }
        }catch (Exception e){}

        // notifyItemRangeChanged(0,fetchFarmResults.size());
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new VettingFarm());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = fetchFarmResults.size() - 1;
        VettingFarm fetchFarmResult = getItem(position);

        if (fetchFarmResult != null) {
            fetchFarmResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public VettingFarm getItem(int position) {
        return fetchFarmResults.get(position);
    }

    public class LoadingVH extends VettingFarmAdapter.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public boolean isNavigateToTimeline() {
        return isNavigateToTimeline;
    }

    public void setNavigateToTimeline(boolean navigateToTimeline) {
        try {
            isNavigateToTimeline = navigateToTimeline;
            notifyDataSetChanged();
        }catch (Exception e){}
    }
}

