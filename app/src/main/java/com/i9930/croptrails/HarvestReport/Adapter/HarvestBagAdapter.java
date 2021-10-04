package com.i9930.croptrails.HarvestReport.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.i9930.croptrails.HarvestReport.Model.HarvestDetailInnerData;
import com.i9930.croptrails.R;


public class HarvestBagAdapter extends RecyclerView.Adapter<HarvestBagAdapter.MyViewHolder> {

    List<HarvestDetailInnerData> bagList;
    Context context;

    public HarvestBagAdapter(List<HarvestDetailInnerData> bagList, Context context) {
        this.bagList = bagList;
        this.context = context;
        Log.e("BagAdapter","Size "+bagList.size());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.field_bag, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bagNo.setText(bagList.get(position).getBagNo());
        holder.bagWeight.setText(bagList.get(position).getNetWt());
    }

    @Override
    public int getItemCount() {
        return bagList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bagNo, bagWeight;

        public MyViewHolder(View itemView) {
            super(itemView);
            bagNo = itemView.findViewById(R.id.dynamic_bagno_harvest_details);
            bagWeight = itemView.findViewById(R.id.dynamic_bagweight_harvest_details);

        }
    }
}
