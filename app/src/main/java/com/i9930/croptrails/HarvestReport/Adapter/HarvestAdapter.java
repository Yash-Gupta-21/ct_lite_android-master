package com.i9930.croptrails.HarvestReport.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.i9930.croptrails.CustomTextview.CustomTextView;
import com.i9930.croptrails.HarvestReport.Model.HarvestDetailInnerData;
import com.i9930.croptrails.HarvestReport.Model.HarvestDetailMaster;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

import static java.lang.Double.parseDouble;


public class HarvestAdapter extends RecyclerView.Adapter<HarvestAdapter.MyViewHolder> {

    Context context;
    List<HarvestDetailMaster> harvestList;
    List<List<HarvestDetailInnerData>> harvestBagList;
    List<Integer> positionList = new ArrayList<>();
    HarvestBagAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    String area_unit_label = "";

    public HarvestAdapter(Context context, List<HarvestDetailMaster> harvestList, List<List<HarvestDetailInnerData>> harvestBagList) {
        this.context = context;
        this.harvestList = harvestList;
        this.harvestBagList = harvestBagList;
        area_unit_label = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL);
        for (int i = 0; i < this.harvestList.size(); i++) {
            try {
                DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat destDf = new SimpleDateFormat("dd/MM/yyyy");
                //harvest date
                Date date = srcDf.parse(this.harvestList.get(i).getHarvestDate());
                this.harvestList.get(i).setHarvestDate(destDf.format(date));
                //weighment date
                Date weighMentDate = srcDf.parse(this.harvestList.get(i).getWeighmentDate());
                this.harvestList.get(i).setWeighmentDate(destDf.format(weighMentDate));


            } catch (ParseException e) {
                Log.e("Adapter", e.toString());
            }
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.harvest_rv_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.harvest_details_custom_textview.setText(harvestList.get(position).getHarvestDate());
        holder.harvest_details_custom_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (harvestBagList.size() == harvestList.size())
                    harvestDialog(context, harvestBagList.get(position), harvestList.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return harvestList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cv_rel_harvest_details;
        RelativeLayout rel_harvest_details, full_rel_lay_harvest;
        CustomTextView harvest_details_custom_textview;
        ImageView down_arrow_img_harvest_details;


        private MyViewHolder(View itemView) {
            super(itemView);
            cv_rel_harvest_details = itemView.findViewById(R.id.cv_rel_harvest_details);
            rel_harvest_details = itemView.findViewById(R.id.rel_harvest_details);
            harvest_details_custom_textview = itemView.findViewById(R.id.harvest_details_custom_textview);
            down_arrow_img_harvest_details = itemView.findViewById(R.id.down_arrow_img_harvest_details);
            full_rel_lay_harvest = itemView.findViewById(R.id.full_rel_lay_harvest);


        }
    }

    private void harvestDialog(Context context, List<HarvestDetailInnerData> innerData, HarvestDetailMaster harvestData) {

        TextView tv_harvest_done_date_dialog, tv_weightment_date_dialog, tv_harvested_area_dialog, tv_standing_areaa_dialog;
        LinearLayout bag_heading_layout_dialog;
        RecyclerView harvestBagRecycler_dialog;
        ImageView cancel_harvest_dialog;

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_harvest_details);
        Window window1 = dialog.getWindow();
        window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

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
        tv_standing_areaa_dialog.setText(AppConstants.getShowableArea(harvestData.getStandingArea())+ " " + area_unit_label);
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


}
