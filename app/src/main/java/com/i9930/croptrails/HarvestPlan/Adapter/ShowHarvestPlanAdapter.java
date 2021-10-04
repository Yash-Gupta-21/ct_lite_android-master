package com.i9930.croptrails.HarvestPlan.Adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.HarvestPlan.Model.ShowHarvestPlanData;
import com.i9930.croptrails.HarvestPlan.Model.ShowHarvestVehicleData;
import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.HarvestShowSinglePlanMapActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

/**
 * Created by hp on 07-09-2018.
 */

public class ShowHarvestPlanAdapter extends RecyclerView.Adapter<ShowHarvestPlanAdapter.ViewHolder> {
    Context context;
    private List<ShowHarvestPlanData> showHarvestPlanDataList;
    private List<ShowHarvestVehicleData> showHarvestVehicleDataList;
    Resources resources;

    private int lastPosition = -1;

    public ShowHarvestPlanAdapter(List<ShowHarvestPlanData> showHarvestPlanDataList, List<ShowHarvestVehicleData> showHarvestVehicleDataList, Context context) {
        this.context = context;
        this.showHarvestPlanDataList = showHarvestPlanDataList;
        this.showHarvestVehicleDataList = showHarvestVehicleDataList;
        //Log.d("Data:", "TaskRecyclerAdapter :" + farmImages);
        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();
    }

    public ShowHarvestPlanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_show_planned_harvest, parent, false);
        ShowHarvestPlanAdapter.ViewHolder vh = new ShowHarvestPlanAdapter.ViewHolder(v);
        return vh;
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void onBindViewHolder(final ShowHarvestPlanAdapter.ViewHolder holder, final int position) {
        final ShowHarvestPlanData showHarvestPlanData = showHarvestPlanDataList.get(position);
        ShowHarvestVehicleData showHarvestVehicleData = showHarvestVehicleDataList.get(position);
        setAnimation(holder.itemView, position);

        holder.stock_wt_label.setText(resources.getString(R.string.stock_weight_label));
        holder.vehicle_wt_label.setText(resources.getString(R.string.vehicle_weight_label));
        holder.bags_blk_label.setText(resources.getString(R.string.bags_blk_label));
        holder.total_bags_label.setText(resources.getString(R.string.total_bags_label));
        holder.remark_label.setText(resources.getString(R.string.remark_label));

        if (showHarvestPlanData.getPlanPickupDate() != null) {


            String sourceString =resources.getString(R.string.plan) + (position + 1) ;
            holder.title_show_planning.setText(Html.fromHtml(sourceString));
        }
        if (showHarvestPlanData.getNetWtOfStock() != null) {
            String sourceString1 = resources.getString(R.string.pick_up_date) + " " + "<b>"+showHarvestPlanData.getPlanPickupDate() + "</b>" ;
            holder.date.setText(Html.fromHtml(sourceString1));
        }
        if (showHarvestPlanData.getNetWtOfStock() != null) {
            holder.stock_wt_show_hr_tv.setText(showHarvestPlanData.getNetWtOfStock());
        }
        if (showHarvestVehicleData.getVehicleNetWt() != null) {
            holder.vehicle_wt_show_hr_tv.setText(showHarvestVehicleData.getVehicleNetWt());
        }
        if (showHarvestPlanData.getNoOfBagsBlk() != null) {
            holder.blk_bag_count_show_hr_tv.setText(showHarvestPlanData.getNoOfBagsBlk());
        }
        if (showHarvestPlanData.getTotalBags() != null) {
            holder.total_bag_count_show_hr_tv.setText(showHarvestPlanData.getTotalBags());
        }
        if (showHarvestPlanData.getRemark() != null) {
            holder.remark_show_hr_tv.setText(showHarvestPlanData.getRemark());
        }
        holder.show_har_plan_rel_full_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHandler.newInstance().setHcmid(showHarvestPlanData.getHcmid());
                DataHandler.newInstance().setShowHarvestPlanData(showHarvestPlanData);
                DataHandler.newInstance().setHarvestDataPosition(position + 1);
                Intent intent = new Intent(context, HarvestShowSinglePlanMapActivity.class);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    context.startActivity(intent, options.toBundle());
                } else {
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public int getItemCount() {
        return showHarvestPlanDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView remark_show_hr_tv;
        TextView title_show_planning;
        TextView date;
        TextView stock_wt_show_hr_tv;
        TextView vehicle_wt_show_hr_tv;
        TextView blk_bag_count_show_hr_tv;
        TextView total_bag_count_show_hr_tv;
        RelativeLayout show_har_plan_rel_full_lay;

        TextView stock_wt_label, vehicle_wt_label, bags_blk_label, total_bags_label, remark_label;

        public ViewHolder(View v) {
            super(v);
            remark_show_hr_tv = (TextView) v.findViewById(R.id.remark_show_hr_tv);
            title_show_planning = (TextView) v.findViewById(R.id.title_show_planning);
            date= (TextView) v.findViewById(R.id.date);
            stock_wt_show_hr_tv = (TextView) v.findViewById(R.id.stock_wt_show_hr_tv);
            vehicle_wt_show_hr_tv = (TextView) v.findViewById(R.id.vehicle_wt_show_hr_tv);
            blk_bag_count_show_hr_tv = (TextView) v.findViewById(R.id.blk_bag_count_show_hr_tv);
            total_bag_count_show_hr_tv = (TextView) v.findViewById(R.id.total_bag_count_show_hr_tv);
            show_har_plan_rel_full_lay = (RelativeLayout) v.findViewById(R.id.show_har_plan_rel_full_lay);

            stock_wt_label=v.findViewById(R.id.stock_wt_label);
            vehicle_wt_label=v.findViewById(R.id.vehicle_wt_label);
            bags_blk_label=v.findViewById(R.id.bags_blk_label);
            total_bags_label=v.findViewById(R.id.total_bags_label);
            remark_label=v.findViewById(R.id.remark_label);
        }
    }


}
