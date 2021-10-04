package com.i9930.croptrails.HarvestSubmit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import com.i9930.croptrails.HarvestSubmit.Interfaces.OnFarmDatasetChanged;
import com.i9930.croptrails.HarvestSubmit.Model.DynamicBagAdapterModel;
import com.i9930.croptrails.R;

public class DynamicBagAdapterEasy extends RecyclerView.Adapter<DynamicBagAdapterEasy.MyViewHolder>{

    Context context;
    List<DynamicBagAdapterModel> bagList;
    int lastBagNo;
    OnFarmDatasetChanged listener;

    public DynamicBagAdapterEasy(Context context, List<DynamicBagAdapterModel> bagList, int lastBagNo, OnFarmDatasetChanged listener) {
        this.context = context;
        this.bagList = bagList;
        this.lastBagNo=lastBagNo;
        this.listener=listener;
    }

    @NonNull
    @Override
    public DynamicBagAdapterEasy.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.dynamic,parent,false);
        return new DynamicBagAdapterEasy.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DynamicBagAdapterEasy.MyViewHolder holder, final int position) {
       /* if (position==0){
            holder.removeBagImg.setVisibility(View.GONE);
        }else {
            holder.removeBagImg.setVisibility(View.VISIBLE);
        }*/

        holder.dynamicBagsWeight.setClickable(false);
        holder.dynamicBagsWeight.setFocusable(false);
        holder.dynamicBagsWeight.setText(bagList.get(position).getBagValue());

        holder.dynamicBagsText.setText(String.valueOf(bagList.get(position).getBagNo()));

        holder.removeBagImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bagList.remove(position);
                notifyDataSetChanged();
                if (bagList.size()==0){
                    listener.isLastItemRemoved(true,position);
                }else {
                    listener.isLastItemRemoved(false,position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return bagList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dynamicBagsText;
        EditText dynamicBagsWeight;
        ImageView removeBagImg;
        public MyViewHolder(View itemView) {
            super(itemView);
            dynamicBagsText=itemView.findViewById(R.id.dynamicBagsText);
            dynamicBagsWeight=itemView.findViewById(R.id.dynamicBagsWeight);
            removeBagImg=itemView.findViewById(R.id.removeBagImg);




        }
    }
}
