package com.i9930.croptrails.Landing.Adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.i9930.croptrails.FarmerInnerDashBoard.FrmrInnrDtlsPagerActivity;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SoilSense.Dashboard.SoilSensDashboardActivity;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static java.lang.Double.parseDouble;

public class FarmerFarmAdapter extends RecyclerView.Adapter<FarmerFarmAdapter.Holder> {
    Context context;
    List<FetchFarmResult> farmResultList;
    Resources resources;
    String area_unit_label="";
    boolean isOnlySoilSens=false;
    public FarmerFarmAdapter(Context context, List<FetchFarmResult> farmResultList) {
        this.context = context;
        this.farmResultList = farmResultList;
        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();
        area_unit_label=SharedPreferencesMethod.getString(context,SharedPreferencesMethod.AREA_UNIT_LABEL);
        isOnlySoilSens=SharedPreferencesMethod.getBoolean(context,SharedPreferencesMethod.IS_ONLY_SOIL_SENS);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_layout_farmer_farm, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {


        String village = "";
        String mandal_or_tehsil = "";
        String district = "";
        String state = "";
        String country = "";
        String final_address = "-";

        String firstLetter = "C";
        String cap = "";
        String lotNo = "-";
        String season = "-";
        String harvestDate = "-";
        String expHarvestDate = "-";

        if (farmResultList.get(position).getFvillage_or_city() != null&&!farmResultList.get(position).getFvillage_or_city().equals("0")) {
            village = farmResultList.get(position).getFvillage_or_city();
        } else {
            village = "";
        }
        if (farmResultList.get(position).getFmandal_or_tehsil() != null&&!farmResultList.get(position).getFmandal_or_tehsil().equals("0")) {
            mandal_or_tehsil = farmResultList.get(position).getFmandal_or_tehsil();
        } else {
            mandal_or_tehsil = "";
        }

        if (farmResultList.get(position).getFdistrict() != null&&!farmResultList.get(position).getFdistrict().equals("0")) {
            district = farmResultList.get(position).getFdistrict();
        } else {
            district = "";
        }

        if (farmResultList.get(position).getFstate() != null&&!farmResultList.get(position).getFstate().equals("0")) {
            state = farmResultList.get(position).getFstate();
        } else {
            state = "";
        }
        if (farmResultList.get(position).getFcountry() != null&&!farmResultList.get(position).getFcountry().equals("0")) {
            country = farmResultList.get(position).getFcountry();
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

        if (farmResultList.get(position).getActualHarvestDate() != null && !farmResultList.get(position).getActualHarvestDate().matches(AppConstants.for_date)) {
            harvestDate = AppConstants.getShowableDate(farmResultList.get(position).getActualHarvestDate(),context);
        }

        if (farmResultList.get(position).getExpHarvestDate() != null && !farmResultList.get(position).getExpHarvestDate().matches(AppConstants.for_date)) {
            expHarvestDate = AppConstants.getShowableDate(farmResultList.get(position).getExpHarvestDate(),context);
        }

        if (farmResultList.get(position).getFarmName() != null) {
            if (farmResultList.get(position).getFarmName().length() > 1) {
                firstLetter = farmResultList.get(position).getFarmName().substring(0, 1).toUpperCase();
                cap = farmResultList.get(position).getFarmName().substring(1);
            } else {
                firstLetter = "C";
                cap = "";
                Log.e("TAG", "Name " + farmResultList.get(position).getFarmName());
            }
        }
        if (farmResultList.get(position).getSeasonName() != null) {
            season = farmResultList.get(position).getSeasonName();
        }

        if ((farmResultList.get(position).getStandingAcres() != null && Double.parseDouble(farmResultList.get(position).getStandingAcres().trim()) > 0)) {
            holder.area_tv.setText(String.format("%.2f", parseDouble(farmResultList.get(position).getStandingAcres())
            )+" "+ area_unit_label);
            holder.farm_area_label.setText(resources.getString(R.string.standing_area_label));
        } else if (farmResultList.get(position).getActualArea() != null && Double.parseDouble(farmResultList.get(position).getActualArea().trim()) > 0) {
            holder.area_tv.setText(String.format("%.2f", parseDouble(farmResultList.get(position).getActualArea())
            )+" "+ area_unit_label);
            holder.farm_area_label.setText(resources.getString(R.string.growing_area_msg));
        } else {
            if ((farmResultList.get(position).getExpArea() != null && Double.parseDouble(farmResultList.get(position).getExpArea().trim()) > 0)) {
                holder.area_tv.setText(String.format("%.2f", parseDouble(farmResultList.get(position).getExpArea())
                )+" "+ area_unit_label);
                holder.farm_area_label.setText(resources.getString(R.string.expected_area_label));
            } else {
                holder.farm_area_label.setText(resources.getString(R.string.expected_area_label));
                holder.area_tv.setText("0 " + resources.getString(R.string.acre_msg));
            }
        }
        holder.farm_address.setText(final_address);
        holder.season_tv.setText(season);
        String sourceString = "<b>" + firstLetter + cap + " (" + farmResultList.get(position).getLotNo() + ")" + "</b>";
        holder.farm_name_and_lot_no.setText(Html.fromHtml(sourceString));
        holder.farm_single_tv.setText(firstLetter);
        holder.farm_harvest_date.setText(harvestDate);
        holder.farm_harvest_date_exp.setText(expHarvestDate);
        String farmId = farmResultList.get(position).getFarmId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, farmId);
                if (farmResultList.get(position).getActualArea() != null) {
                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTUAL_AREA, farmResultList.get(position).getActualArea().trim());
                }
                if (farmResultList.get(position).getStandingAcres() != null) {
                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, farmResultList.get(position).getStandingAcres().trim());
                }
                if (farmResultList.get(position).getExpArea() != null) {
                    SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.EXPECTED_AREA,
                            Float.valueOf(farmResultList.get(position).getExpArea()));
                }
                Intent intent = new Intent(context, FrmrInnrDtlsPagerActivity.class);
                if (isOnlySoilSens){
                    intent=new Intent(context, SoilSensDashboardActivity.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

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
    public int getItemCount() {
        return farmResultList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView farm_single_tv, farm_name_and_lot_no, farm_address, farm_harvest_date, area_tv, season_tv, farm_area_label, farm_harvest_date_exp;

        public Holder(@NonNull View itemView) {
            super(itemView);

            farm_single_tv = itemView.findViewById(R.id.farm_single_tv);
            farm_name_and_lot_no = itemView.findViewById(R.id.farm_name_and_lot_no);
            farm_address = itemView.findViewById(R.id.farm_address);
            farm_harvest_date = itemView.findViewById(R.id.farm_harvest_date);
            area_tv = itemView.findViewById(R.id.area_in_acer);
            season_tv = itemView.findViewById(R.id.season_tv);
            farm_area_label = itemView.findViewById(R.id.farm_area_label);
            farm_harvest_date_exp = itemView.findViewById(R.id.farm_harvest_date_exp);
        }

    }
}
