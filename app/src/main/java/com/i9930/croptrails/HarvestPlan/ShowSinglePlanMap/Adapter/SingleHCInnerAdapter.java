package com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.HarvestShowSinglePlanMapActivity;
import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model.GpsDataForSingleHarvest;
import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model.HarvestCollectionDetails;
import com.i9930.croptrails.R;

public class SingleHCInnerAdapter extends RecyclerView.Adapter<SingleHCInnerAdapter.Holder> {
    HarvestShowSinglePlanMapActivity context;
    List<HarvestCollectionDetails> detailsList;
    SingleHCOuterAdapter adapter;
    GpsDataForSingleHarvest dataOuter;

    public SingleHCInnerAdapter(HarvestShowSinglePlanMapActivity context, List<HarvestCollectionDetails> detailsList, SingleHCOuterAdapter adapter, GpsDataForSingleHarvest data) {
        this.context = context;
        this.dataOuter = data;
        this.adapter = adapter;
        this.detailsList = detailsList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_single_harvest_collection_inner, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        int seq = position + 1;
        holder.seqTv.setText("" + seq + ".");
        holder.weightTv.setText(detailsList.get(position).getNetWeight() + context.getResources().getString(R.string.kg_label));
       /* if (data.getPickedup().trim().toUpperCase().equals("Y")){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }*/
        if (detailsList.get(position).isPickedUp()||detailsList.get(position).isSelfClicked()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        if (detailsList.get(position).isAlreadyPickedUp()){
            holder.checkBox.setEnabled(false);
            holder.checkBox.setClickable(false);
            holder.checkBox.setText(context.getResources().getString(R.string.picked_up_label));
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setEnabled(true);
            holder.checkBox.setClickable(true);
            holder.checkBox.setText(context.getResources().getString(R.string.pickup_label));

        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    detailsList.get(position).setPickedUp(true);
                    detailsList.get(position).setPickedup("Y");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean allSelected = true;
                            for (int i = 0; i < detailsList.size(); i++) {
                                if (detailsList.get(i).getPickedup().trim().toUpperCase().equals("N")) {
                                    allSelected = false;
                                    break;
                                }
                            }
                            if (allSelected) {
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dataOuter.setAllSelected(true);
                                        try {
                                            adapter.notifyDataSetChanged();
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    }).start();
                } else {

                    detailsList.get(position).setPickedUp(false);
                    detailsList.get(position).setPickedup("N");
                    dataOuter.setAllSelected(false);
                    try {
                        adapter.notifyDataSetChanged();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()){
                    detailsList.get(position).setSelfClicked(true);
                }else {
                    detailsList.get(position).setSelfClicked(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (detailsList != null)
            return detailsList.size();
        else return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView seqTv, weightTv;
        CheckBox checkBox;

        public Holder(@NonNull View itemView) {
            super(itemView);
            seqTv = itemView.findViewById(R.id.seqTv);
            weightTv = itemView.findViewById(R.id.qtyTv);
            checkBox = itemView.findViewById(R.id.pickedUp);
        }
    }
}
