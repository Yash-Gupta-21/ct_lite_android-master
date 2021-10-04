package com.i9930.croptrails.OfflineMode.Adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.Chart;

import es.dmoral.toasty.Toasty;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static java.lang.Double.parseDouble;


/**
 * Created by hp on 01-10-2018.
 */

public class OfflineFarmsAdapter extends RecyclerView.Adapter<OfflineFarmsAdapter.ViewHolder> {
    Context context;
    List<FetchFarmResult> fetchFarmResults;
    boolean[] checkBoxState;
    Resources resources;

    String area_unit_label = "";
    final String TAG = "OfflineFarmsAdapter";
    int currentcolor = 0;

    public OfflineFarmsAdapter(Context context, List<FetchFarmResult> fetchFarmResults) {
        this.context = context;
        this.fetchFarmResults = fetchFarmResults;
        checkBoxState = new boolean[fetchFarmResults.size()];
        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();
        area_unit_label = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_layout_offline_farm, parent, false);
        OfflineFarmsAdapter.ViewHolder vh = new OfflineFarmsAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(OfflineFarmsAdapter.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final FetchFarmResult fetchFarmResult = fetchFarmResults.get(position);


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
            holder.farm_address.setText("-");
        }
        if ((fetchFarmResult.getStandingAcres() != null && parseDouble(fetchFarmResult.getStandingAcres().trim()) > 0)) {
            holder.exp_area_in_acer.setText(String.format("%.2f", parseDouble(fetchFarmResult.getStandingAcres())
            ) + " " + area_unit_label);
            holder.farm_area_label.setText(resources.getString(R.string.standing_area_label));
        } else if (fetchFarmResult.getActualArea() != null && parseDouble(fetchFarmResult.getActualArea().trim()) > 0) {
            holder.exp_area_in_acer.setText(String.format("%.2f", parseDouble(fetchFarmResult.getActualArea())
            ) + " " + area_unit_label);
            holder.farm_area_label.setText(resources.getString(R.string.growing_area_msg));
        } else {
            if ((fetchFarmResult.getExpArea() != null && parseDouble(fetchFarmResult.getExpArea().trim()) > 0)) {
                holder.exp_area_in_acer.setText(String.format("%.2f", parseDouble(fetchFarmResult.getExpArea())
                ) + " " + area_unit_label);
                holder.farm_area_label.setText(resources.getString(R.string.expected_area_label));
            } else {
                holder.farm_area_label.setText(resources.getString(R.string.expected_area_label));
                holder.exp_area_in_acer.setText("0 " + area_unit_label);
            }
        }

        if (fetchFarmResult.getSowingDate() != null) {
            if (!fetchFarmResult.getSowingDate().equals(AppConstants.for_date)) {
                holder.farm_harvest_date.setText(fetchFarmResult.getSowingDate().toString());
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


        holder.checkBox_for_offline.setChecked(fetchFarmResult.isSelected());

        holder.checkBox_for_offline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (fetchFarmResult.isSelected()) {
                    fetchFarmResult.setSelected(isChecked);
                } else {
                    fetchFarmResult.setSelected(isChecked);
                }
            }
        });

       /* if (fetchFarmResult.getLotNo() != null) {
            holder.farm_lot_no.setText(resources.getString(R.string.lot_no) + fetchFarmResult.getLotNo());
        }
        if (fetchFarmResult.getName() != null) {
            holder.farmer_name.setText(resources.getString(R.string.fathers_name) + fetchFarmResult.getName());
        }

        holder.checkBox_for_offline.setChecked(fetchFarmResult.isSelected());

        holder.checkBox_for_offline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (fetchFarmResult.isSelected()) {
                    fetchFarmResult.setSelected(isChecked);
                } else {
                    fetchFarmResult.setSelected(isChecked);
                }
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return fetchFarmResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        /*TextView farm_lot_no, farmer_name;
        CheckBox checkBox_for_offline;
*/
        TextView farmer_name_single, farm_lot_no, exp_area_in_acer, farmer_name, farm_harvest_date, farm_address, expectedOrActualTV, farm_farmer_fathers_name;
        CheckBox checkBox_for_offline;
        TextView farm_area_label;
        TextView exp_area_tv, stand_area_tv;
        LinearLayout tv_addres_linear_lay, tv_exp_area_stand_area_lay;
        CardView whole_cv_farm_frag;
        Chart pieChart;

        public ViewHolder(View v) {
            super(v);
           /* farmer_name = (TextView) v.findViewById(R.id.farmer_name_offline);
            farm_lot_no = (TextView) v.findViewById(R.id.farm_lot_no_offline);
            checkBox_for_offline = (CheckBox) v.findViewById(R.id.checkbox_offline);*/

            farmer_name_single = (TextView) v.findViewById(R.id.farmer_single_tv);
            farmer_name = (TextView) v.findViewById(R.id.farmer_name);
            farm_lot_no = (TextView) v.findViewById(R.id.farm_lot_no);
            exp_area_in_acer = (TextView) v.findViewById(R.id.exp_area_in_acer);
            farm_harvest_date = (TextView) v.findViewById(R.id.farm_harvest_date);
            farm_address = (TextView) v.findViewById(R.id.farm_address);
            /*expectedOrActualTV = v.findViewById(R.id.e5);*/
            farm_farmer_fathers_name = v.findViewById(R.id.farm_farmer_fathers_name);
            checkBox_for_offline = v.findViewById(R.id.checkbox_offline);
            tv_addres_linear_lay = v.findViewById(R.id.tv_addres_linear_lay);
            whole_cv_farm_frag = v.findViewById(R.id.whole_cv_farm_frag);
            farm_area_label = v.findViewById(R.id.farm_area_label);

        }
    }


    //This two methods are imp for checkbox bug -->> on scrolling checkbox disappears
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
