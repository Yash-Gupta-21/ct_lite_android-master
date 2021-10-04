package com.i9930.croptrails.AddFarm.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.Season;
import com.i9930.croptrails.R;


public class SeasonAdapterRad extends RecyclerView.Adapter<SeasonAdapterRad.Holder> {
    Context context;
    List<Season> seasonList;
    public int lastSelectedPosition = -1;
    OnItemClick onItemClick;

    public interface OnItemClick {
        public void onItemClick(int position, String id);
    }

    public SeasonAdapterRad(Context context,String seasonId, List<Season> seasonList, OnItemClick onItemClick) {
        this.context = context;
        this.onItemClick = onItemClick;
        this.seasonList = seasonList;
        if (seasonList!=null&&seasonId!=null&&!TextUtils.isEmpty(seasonId)){
            for (int i=0;i<seasonList.size();i++){
                if (seasonList.get(i).getSeasonId()!=null&&seasonList.get(i).getSeasonId().trim().equals(seasonId)){
                    lastSelectedPosition=i;
                    break;
                }
            }
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_radio_button, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.ref = position;
        holder.radioButton.setText(seasonList.get(holder.ref).getName());
        holder.radioButton.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        if (seasonList != null)
            return seasonList.size();
        else return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        int ref;

        public Holder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.rabiRadio);


            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastSelectedPosition != -1) {
                        if (lastSelectedPosition == getAdapterPosition()) {
                           /* lastSelectedPosition = -1;
                            notifyDataSetChanged();*/
                        } else {
                            seasonList.get(lastSelectedPosition).setChecked(false);
                            lastSelectedPosition = getAdapterPosition();
                            seasonList.get(lastSelectedPosition).setChecked(true);
                            notifyDataSetChanged();
                            onItemClick.onItemClick(lastSelectedPosition, seasonList.get(lastSelectedPosition).getSeasonId());
                        }
                    } else {
                        lastSelectedPosition = getAdapterPosition();
                        seasonList.get(lastSelectedPosition).setChecked(true);
                        onItemClick.onItemClick(lastSelectedPosition, seasonList.get(lastSelectedPosition).getSeasonId());
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
