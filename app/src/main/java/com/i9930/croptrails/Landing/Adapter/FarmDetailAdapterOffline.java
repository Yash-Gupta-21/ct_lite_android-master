package com.i9930.croptrails.Landing.Adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import es.dmoral.toasty.Toasty;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.SoilSense.Dashboard.SoilSensDashboardActivity;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static java.lang.Double.parseDouble;

public class FarmDetailAdapterOffline extends RecyclerView.Adapter<FarmDetailAdapterOffline.MyViewholder> {
    List<Farm> farmList;
    Context context;
    Resources resources;
    int[] colours = {R.color.dashboardcolo1, R.color.dashboardcolor2, R.color.dashboardcolor3, R.color.dashboardcolor4};
    int currentcolor = 0;

    String area_unit_label="";
    boolean isOnlySoilSens=false;
    public FarmDetailAdapterOffline(List<Farm> farmList, Context context) {
        this.farmList = farmList;
        this.context = context;
        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();
        isOnlySoilSens=SharedPreferencesMethod.getBoolean(context,SharedPreferencesMethod.IS_ONLY_SOIL_SENS);
        area_unit_label=SharedPreferencesMethod.getString(context,SharedPreferencesMethod.AREA_UNIT_LABEL);
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fetch_farm_content, parent, false);

        return new MyViewholder(v);
    }

    @Override
    public void onBindViewHolder(final FarmDetailAdapterOffline.MyViewholder holder, final int position) {
        final Farm farm = farmList.get(position);
        String firstLetter = "";
        String cap = "";
        // setAnimation(holder.itemView,position);
        if (farm.getFarmerName() != null&&farm.getFarmerName().length()>0) {
            firstLetter = farm.getFarmerName().substring(0, 1).toUpperCase();
            cap = farm.getFarmerName().substring(1);
        }
        String village = "";
        String mandal_or_tehsil = "";
        String district = "";
        String state = "";
        String country = "";
        String final_address = "-";
        if (farm.getVillageOrCity() != null) {
            village = farm.getVillageOrCity();
        } else {
            village = "";
        }
        if (farm.getMandalOrTehsil() != null) {
            mandal_or_tehsil = farm.getMandalOrTehsil();
        } else {
            mandal_or_tehsil = "";
        }

        if (farm.getDistrict() != null) {
            district = farm.getDistrict();
        } else {
            district = "";
        }

        if (farm.getState() != null) {
            state = farm.getState();
        } else {
            state = "";
        }
        if (farm.getCountry() != null) {
            country = farm.getCountry();
        } else {
            country = "";
        }
        final_address = village + " " + mandal_or_tehsil + " " + district + " " + state + " " + country;

        if (!final_address.trim().equals("")) {
            holder.farm_address.setText(final_address);
            holder.tv_addres_linear_lay.setVisibility(View.GONE);

        } else {
            holder.tv_addres_linear_lay.setVisibility(View.GONE);
        }
        String standingArea=farm.getStandingAcres();
        String actualArea=farm.getActualArea();
        String expectedArea=farm.getExpArea();

        double stdArea=0.0,expAre=0.0,actAre=0.0;
        try {
            if (standingArea!=null&&!TextUtils.isEmpty(standingArea))
                stdArea=parseDouble(standingArea.trim());
        }catch (Exception e){

        }
        try {
            if (expectedArea!=null&&!TextUtils.isEmpty(expectedArea))
                expAre=parseDouble(expectedArea.trim());
        }catch (Exception e){

        }
        try {
            if (actualArea!=null&&!TextUtils.isEmpty(actualArea))
                actAre=parseDouble(actualArea.trim());
        }catch (Exception e){

        }

        if ((stdArea > 0)) {
            holder.exp_area_in_acer.setText(String.format("%.2f", stdArea
            )+" "+ area_unit_label);
            holder.farm_area_label.setText(resources.getString(R.string.standing_area_label));
        } else if (actAre > 0) {
            holder.exp_area_in_acer.setText(String.format("%.2f", actAre
            )+" "+ area_unit_label);
            holder.farm_area_label.setText(resources.getString(R.string.growing_area_msg));
        } else {
            if (( expAre > 0)) {
                holder.exp_area_in_acer.setText(String.format("%.2f", expAre
                )+" "+ area_unit_label);
                holder.farm_area_label.setText(resources.getString(R.string.expected_area_label));
            } else {
                holder.farm_area_label.setText(resources.getString(R.string.expected_area_label));
                holder.exp_area_in_acer.setText("0 " + area_unit_label);
            }
        }
        holder.exp_area_in_acer.setTextColor(resources.getColor(R.color.darkgreen));

        if (farm.getExpHarvestDate() != null) {
            if (!farm.getExpHarvestDate().equals(AppConstants.for_date)) {
                holder.farm_harvest_date.setText(AppConstants.getShowableDate(farm.getExpHarvestDate(),context));
                holder.farm_harvest_date.setTextColor(resources.getColor(R.color.black));
            } else {
                holder.farm_harvest_date.setText("-");
                holder.farm_harvest_date.setTextColor(resources.getColor(R.color.black));
            }
        } else {
            holder.farm_harvest_date.setText("-");

        }


        String sourceString = "<b>" + firstLetter + cap + " (" + farm.getLotNo() + ")" + "</b>";
        holder.farm_lot_no.setText(Html.fromHtml(sourceString));
        holder.farmer_name_single.setText(firstLetter);
        holder.farm_farmer_fathers_name.setText(farm.getFatherName());

        int[] androidColors = resources.getIntArray(R.array.androidcolors);
        if (currentcolor < 3)
            currentcolor = currentcolor + 1;
        else
            currentcolor = 0;
        GradientDrawable bgShape = (GradientDrawable) holder.farmer_name_single.getBackground();
        bgShape.setColor(androidColors[currentcolor]);

        holder.call_farmer_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (farm.getFarmerMob() != null) {
                    if (!farm.getFarmerMob().trim().equals("0")&&!farm.getFarmerMob().equals("9999999999")) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + farm.getFarmerMob()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toasty.info(context, context.getResources().getString(R.string.mobile_number_not_availbale_msg),
                                Toast.LENGTH_LONG,true).show();
                    }
                } else {
                    Toasty.info(context, context.getResources().getString(R.string.mobile_number_not_availbale_msg),
                            Toast.LENGTH_LONG,true).show();
                }
                // Log.e("TAG", fetchFarmResult.getName());
            }
        });


       /* if (farm.getFarmerMob() != null) {
            if (farm.getFarmerMob().trim().equals("0")) {
                holder.call_farmer_butt.setVisibility(View.GONE);
            }
        }*/


    }

    void onOfflineFarmClick(View v, int position) {
        //Toast.makeText(context.getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, farmList.get(position).getStandingAcres().trim());
        SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.EXPECTED_AREA, Float.valueOf(farmList.get(position).getExpArea()));
        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTUAL_AREA, farmList.get(position).getActualArea().trim());
        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, String.valueOf(farmList.get(position).getFarmId()));

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

    @Override
    public int getItemCount() {
        return farmList.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        TextView farmer_name_single, farm_lot_no, exp_area_in_acer, farmer_name, farm_harvest_date, farm_address, farm_farmer_fathers_name;
        ImageView call_farmer_butt;
        TextView farm_area_label;
        TextView exp_area_tv, stand_area_tv;
        LinearLayout tv_addres_linear_lay, tv_exp_area_stand_area_lay;
        CardView whole_cv_farm_frag;


        public MyViewholder(View v) {
            super(v);
            farmer_name_single = (TextView) v.findViewById(R.id.farmer_single_tv);
            farmer_name = (TextView) v.findViewById(R.id.farmer_name);
            farm_lot_no = (TextView) v.findViewById(R.id.farm_lot_no);
            exp_area_in_acer = (TextView) v.findViewById(R.id.exp_area_in_acer);
            farm_harvest_date = (TextView) v.findViewById(R.id.farm_harvest_date);
            farm_address = (TextView) v.findViewById(R.id.farm_address);
            // expectedOrActualTV = v.findViewById(R.id.e5);
            farm_farmer_fathers_name = v.findViewById(R.id.farm_farmer_fathers_name);
            call_farmer_butt = v.findViewById(R.id.call_farmer_butt);
            tv_addres_linear_lay = v.findViewById(R.id.tv_addres_linear_lay);
            whole_cv_farm_frag = v.findViewById(R.id.whole_cv_farm_frag);
         /*   edit_standing_acres_pencil=v.findViewById(R.id.edit_standing_acres_pencil);
            rel_layout_for_pencil=v.findViewById(R.id.rel_layout_for_pencil);*/
           /* tv_exp_area_stand_area_lay = v.findViewById(R.id.tv_exp_area_stand_area_lay);
            exp_area_tv = v.findViewById(R.id.exp_area_tv);
            stand_area_tv = v.findViewById(R.id.stand_area_tv);*/
            farm_area_label = v.findViewById(R.id.farm_area_label);
            farm_lot_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOfflineFarmClick(v, getAdapterPosition());
                }
            });

            exp_area_in_acer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOfflineFarmClick(v, getAdapterPosition());
                }
            });


            farm_harvest_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOfflineFarmClick(v, getAdapterPosition());
                }
            });

            farm_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOfflineFarmClick(v, getAdapterPosition());
                }
            });
            whole_cv_farm_frag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOfflineFarmClick(v, getAdapterPosition());
                }
            });


            farm_farmer_fathers_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onOfflineFarmClick(v, getAdapterPosition());
                }
            });

            farmer_name_single.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Farm farm = farmList.get(getAdapterPosition());

                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, farm.getStandingAcres().trim());
                    SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.EXPECTED_AREA, Float.valueOf(farm.getExpArea()));
                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTUAL_AREA, farm.getActualArea().trim());
                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, String.valueOf(farm.getFarmId()));
                }
            });
        }
    }
}
