package com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import com.i9930.croptrails.HarvestCollection.Model.HarvestCollectionData;
import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.HarvestShowSinglePlanMapActivity;
import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model.GpsDataForSingleHarvest;
import com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model.HarvestCollectionDetails;
import com.i9930.croptrails.R;

public class SingleHCOuterAdapter extends RecyclerView.Adapter<SingleHCOuterAdapter.Holder> {
    HarvestShowSinglePlanMapActivity context;
    List<GpsDataForSingleHarvest> detailsList;
    List<Integer>alreadyAllPickStatus;
    public SingleHCOuterAdapter(HarvestShowSinglePlanMapActivity context, List<GpsDataForSingleHarvest> detailsList,List<Integer>alreadyAllPickStatus) {
        this.context = context;
        this.detailsList = detailsList;
        this.alreadyAllPickStatus=alreadyAllPickStatus;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_single_harvest_collection_outer, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        GpsDataForSingleHarvest data = detailsList.get(position);

        SingleHCInnerAdapter adapter = new SingleHCInnerAdapter(context, data.getHarvestCollectionDetailsList(), this, data);
        holder.innerRecyclerView.setHasFixedSize(true);
        holder.innerRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.innerRecyclerView.setAdapter(adapter);
        holder.lotNoTv.setText(data.getLotNo());
        if (data.isAllSelected()) {
            holder.selectAllCheck.setChecked(true);
        } else {
            holder.selectAllCheck.setChecked(false);
        }
        if (alreadyAllPickStatus.get(position)==1) {
            holder.selectAllCheck.setChecked(true);
            holder.selectAllCheck.setClickable(false);
            holder.selectAllCheck.setEnabled(false);
            holder.selectAllCheck.setText(context.getResources().getString(R.string.picked_up_label));
        } else {
            holder.selectAllCheck.setClickable(true);
            holder.selectAllCheck.setEnabled(true);
            holder.selectAllCheck.setText(context.getResources().getString(R.string.pickup_label));
        }
        holder.selectAllCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (int i = 0; i < data.getHarvestCollectionDetailsList().size(); i++) {
                        data.getHarvestCollectionDetailsList().get(i).setPickedUp(true);
                        data.getHarvestCollectionDetailsList().get(i).setPickedup("Y");
                    }
                    detailsList.get(position).setAllSelected(true);
                    adapter.notifyDataSetChanged();
                } else {
                    for (int i=0;i< data.getHarvestCollectionDetailsList().size();i++){
                        data.getHarvestCollectionDetailsList().get(i).setPickedUp(false);
                        if (!data.getHarvestCollectionDetailsList().get(i).isAlreadyPickedUp()&&!data.getHarvestCollectionDetailsList().get(i).isSelfClicked()) {
                            data.getHarvestCollectionDetailsList().get(i).setPickedup("N");

                        }
                    }
                    detailsList.get(position).setAllSelected(false);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(holder.innerRecyclerView, holder.arrowImageview);
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
        ImageView arrowImageview;
        TextView lotNoTv;
        CheckBox selectAllCheck;
        RecyclerView innerRecyclerView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            selectAllCheck = itemView.findViewById(R.id.selectAllCheck);
            arrowImageview = itemView.findViewById(R.id.arroImageview);
            lotNoTv = itemView.findViewById(R.id.lotNoTv);
            innerRecyclerView = itemView.findViewById(R.id.innerRecycler);
        }
    }


    public void animate(final View cardView, ImageView down_arrow_img) {
        if (cardView.getVisibility() == View.GONE) {
            RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setInterpolator(new DecelerateInterpolator());
            rotateAnimation.setRepeatCount(0);
            rotateAnimation.setDuration(500);
            rotateAnimation.setFillAfter(true);
            down_arrow_img.startAnimation(rotateAnimation);
            cardView.setFocusable(true);

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_down);
            animation.setDuration(500);
            cardView.setAnimation(animation);
            cardView.animate();
            animation.start();
            cardView.setVisibility(View.VISIBLE);
        } else {
            RotateAnimation rotateAnimation = new RotateAnimation(180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setInterpolator(new DecelerateInterpolator());
            rotateAnimation.setRepeatCount(0);
            rotateAnimation.setDuration(500);
            rotateAnimation.setFillAfter(true);
            down_arrow_img.startAnimation(rotateAnimation);

            cardView.setVisibility(View.INVISIBLE);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            animation.setDuration(500);
            cardView.setAnimation(animation);
            cardView.animate();
            animation.start();
            cardView.setFocusable(false);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    cardView.setVisibility(View.GONE);
                }
            }, 500);
        }

    }

}
