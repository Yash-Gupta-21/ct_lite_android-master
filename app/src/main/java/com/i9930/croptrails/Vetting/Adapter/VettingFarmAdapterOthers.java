package com.i9930.croptrails.Vetting.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import com.i9930.croptrails.Landing.Models.NewData.FetchFarmResultNew;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static java.lang.Double.parseDouble;

public class VettingFarmAdapterOthers extends RecyclerView.Adapter<VettingFarmAdapterOthers.ViewHolder> {

    public interface ItemClickListener {
        public void onItemClicked(int index, FetchFarmResultNew farmResultNew, String isVetting);
    }

    private String[] mDataSet;
    int[] colours = {R.color.dashboardcolo1, R.color.dashboardcolor2, R.color.dashboardcolor3, R.color.dashboardcolor4};
    int currentcolor = 0;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    Context context;
    List<FetchFarmResultNew> fetchFarmResults;
    List<FetchFarmResultNew> fetchFarmResultsFiltered;
    Resources resources;
    String area_unit_label = "";
    final String TAG = "FarmDetailAdapter";
    boolean clickEvent = false;
    VettingFarmAdapterOthers.ItemClickListener listener;

    public VettingFarmAdapterOthers(Context context, List<FetchFarmResultNew> vettingFarms, VettingFarmAdapterOthers.ItemClickListener listener) {
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

    public List<FetchFarmResultNew> getFetchFarmResults() {
        return fetchFarmResults;
    }

    public void setFetchFarmResults(List<FetchFarmResultNew> fetchFarmResults) {
        this.fetchFarmResults = fetchFarmResults;
    }


    public VettingFarmAdapterOthers.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        VettingFarmAdapterOthers.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new VettingFarmAdapterOthers.LoadingVH(v2);
                break;
        }

        return viewHolder;

    }

    @NonNull
    private VettingFarmAdapterOthers.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        VettingFarmAdapterOthers.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.fetch_farm_content, parent, false);
        viewHolder = new VettingFarmAdapterOthers.ViewHolder(v1);

        return viewHolder;
    }

    public void onBindViewHolder(final VettingFarmAdapterOthers.ViewHolder holder, final int position) {
        final FetchFarmResultNew fetchFarmResult = getFetchFarmResults().get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                if (position == 0) {
                }
                final String vetting = fetchFarmResult.getVetting();
                GradientDrawable bgShape = (GradientDrawable) holder.farmer_name_single.getBackground();
                if (AppConstants.isValidString(vetting) && !vetting.trim().equals(AppConstants.VETTING.REJECTED)) {

                    if (vetting.trim().equals(AppConstants.VETTING.SELECTED)) {
                        holder.contentLayout.setBackgroundResource(R.drawable.vetting_selected);
                        holder.moreOptionImage.setVisibility(View.GONE);
                        currentcolor=R.color.darkgreen;

                        bgShape.setColor(resources.getColor(R.color.darkgreen));
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

                        currentcolor=R.color.yellow;
                        bgShape.setColor(resources.getColor(R.color.yellow));
                    } else if (vetting.trim().equals(AppConstants.VETTING.DATA_ENTRY)) {
                        holder.contentLayout.setBackgroundResource(R.drawable.vetting_data_entry);
                        holder.moreOptionImage.setVisibility(View.GONE);
                        currentcolor=R.color.orange;
                        bgShape.setColor(resources.getColor(R.color.orange));
                    } else {
                        holder.contentLayout.setBackgroundResource(R.drawable.vetting_rejected);
                        currentcolor=R.color.red;
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
                if (fetchFarmResult.getFarmName() != null) {
                    firstLetter = fetchFarmResult.getFarmerName().substring(0, 1).toUpperCase();
                    cap = fetchFarmResult.getFarmerName().substring(1);
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
                String standingArea = "" + fetchFarmResult.getStandingArea();
                String actualArea = "" + fetchFarmResult.getActualArea();
                String expectedArea = "" + fetchFarmResult.getExpArea();
                String sowingDate=fetchFarmResult.getSowingDate();
                Log.e(TAG, position + " 1 standingArea" + standingArea + ", actualArea" + actualArea + ", expectedArea" + expectedArea);
                if (fetchFarmResult.getFarmMasterNum() != null &&
                        SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID) != null &&
                        fetchFarmResult.getFarmMasterNum().trim().equals(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID).trim())) {

                    standingArea = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES);
                    actualArea = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.ACTUAL_AREA);
                    expectedArea = "" + SharedPreferencesMethod.getDoubleSharedPreferences(context, SharedPreferencesMethod.EXPECTED_AREA);
                    sowingDate=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SOWING_DATE);
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
                holder.dateLabel.setText(resources.getString(R.string.sowing_date_label));
                holder.fatherSpouseLableTv.setText(resources.getString(R.string.spouse));

                if ( sowingDate!= null&&AppConstants.isValidString(sowingDate)) {
                    if (!sowingDate.equals(AppConstants.for_date)) {
                        holder.farm_harvest_date.setText(AppConstants.getShowableDate(sowingDate,context));

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
                /*int[] androidColors = resources.getIntArray(R.array.androidcolors);
                if (currentcolor < 3)
                    currentcolor = currentcolor + 1;
                else
                    currentcolor = 0;*/

                if (fetchFarmResult.getSpouseName() != null)
                    holder.farm_farmer_fathers_name.setText(fetchFarmResult.getSpouseName());
                else
                    holder.farm_farmer_fathers_name.setText("-");

                holder.farmer_name_single.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (clickEvent) {
                            onOnlineFarmClick(v, position, vetting);

                        }
                    }
                });
                holder.farmer_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position, vetting);
                    }
                });
                holder.farm_lot_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position, vetting);
                    }
                });

                holder.exp_area_in_acer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position, vetting);
                    }
                });

                holder.whole_cv_farm_frag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position, vetting);
                    }
                });

                holder.farm_harvest_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position, vetting);
                    }
                });

                holder.farm_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position, vetting);
                    }
                });

                holder.farm_farmer_fathers_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, position, vetting);
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
                break;

            case LOADING:
                break;
        }
    }

    @Override
    public void onBindViewHolder(VettingFarmAdapterOthers.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }


    @Override
    public int getItemViewType(int position) {
        return (position == fetchFarmResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public int getItemCount() {
        return fetchFarmResults == null ? 0 : fetchFarmResults.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView farmer_name_single, farm_lot_no, exp_area_in_acer, farmer_name, farm_harvest_date, farm_address, expectedOrActualTV, farm_farmer_fathers_name;
        ImageView call_farmer_butt;
        TextView farm_area_label, dateLabel, fatherSpouseLableTv;
        TextView exp_area_tv, stand_area_tv;
        LinearLayout tv_addres_linear_lay, contentLayout;
        CardView whole_cv_farm_frag;
        ImageView moreOptionImage;

        public ViewHolder(View v) {
            super(v);
            moreOptionImage = v.findViewById(R.id.moreOptionImage);
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
    }

    public interface FarmDetailAdapterListener {
        void OnFarmSelected(FetchFarmResultNew fetchFarmResult);
    }

    void onOnlineFarmClick(View v, int position, String vetting) {
        final FetchFarmResultNew fetchFarmResult = fetchFarmResultsFiltered.get(position);
        Log.e("FarmDetailAdapter", "Farm Data " + new Gson().toJson(fetchFarmResult));
        if (clickEvent) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, fetchFarmResult.getFarmMasterNum());
            Log.e("TAG", "farm_id " + fetchFarmResult.getFarmMasterNum());
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SOWING_DATE,fetchFarmResult.getSowingDate());
            listener.onItemClicked(position, fetchFarmResult, vetting);

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

    public void add(FetchFarmResultNew fetchFarmResult) {
        fetchFarmResults.add(fetchFarmResult);
        notifyItemInserted(fetchFarmResults.size() - 1);

    }

    public void addAll(List<FetchFarmResultNew> fetchFarmResults) {
        if (fetchFarmResults != null && fetchFarmResults.size() > 0) {
            for (FetchFarmResultNew fetchFarmResult : fetchFarmResults) {
                add(fetchFarmResult);
            }
        }
    }

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
        add(new FetchFarmResultNew());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = fetchFarmResults.size() - 1;
        FetchFarmResultNew fetchFarmResult = getItem(position);

        if (fetchFarmResult != null) {
            fetchFarmResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public FetchFarmResultNew getItem(int position) {
        return fetchFarmResults.get(position);
    }

    public class LoadingVH extends VettingFarmAdapterOthers.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }



}
