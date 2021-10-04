package com.i9930.croptrails.ShowInputCost.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import com.i9930.croptrails.R;
import com.i9930.croptrails.ShowInputCost.Model.ResourceData;

public class ResourceUsedAdapter extends RecyclerView.Adapter<ResourceUsedAdapter.Holder> {
    Context context;
    List<ResourceData> list;

    public ResourceUsedAdapter(Context context, List<ResourceData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_show_resource_used, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String date = list.get(position).getUsedOn();
        try {
            DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateView = srcDf.parse(list.get(position).getUsedOn());
            DateFormat srcDfView = new SimpleDateFormat("dd MMM");
            date = srcDfView.format(dateView);
        } catch (Exception e) {
            Log.e("InputCostAdapter", e.toString());
        }

        holder.added_by_res_tv.setText("Added by " + list.get(position).getName());
        holder.date_res_tv.setText(date);
       // holder.qty_tv.setText(list.get(position).getValue()/*+" "+list.get(position).getUnit()*/);

        String unit = "";
        String type = "";
        String otherUnit = "";
        String otherType = "";
        if (list.get(position).getType() != null) {
            type = list.get(position).getType();
        }
        if (list.get(position).getUnit() != null) {
            unit = " "+list.get(position).getUnit();
        }
        if (list.get(position).getOtherResourceType() != null) {
            otherType = list.get(position).getOtherResourceType();
        }
        if (list.get(position).getOtherUnit() != null) {
            otherUnit = " "+list.get(position).getOtherUnit();
        }
        if (list.get(position).getType() != null) {
            holder.qty_tv.setText(type);
            holder.type_res_tv.setText(context.getResources().getString(R.string.resource_type_label)+" " + list.get(position).getValue()+ unit);
        }
        else {
            //holder.type_res_tv.setText("Resource type " + otherType + otherUnit);
            holder.type_res_tv.setText(context.getResources().getString(R.string.resource_type_label)+" " +list.get(position).getValue()+" "+otherUnit);
            holder.qty_tv.setText(otherType);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView cost_img;
        TextView added_by_res_tv, qty_tv, date_res_tv, type_res_tv;

        public Holder(@NonNull View itemView) {
            super(itemView);
            cost_img = itemView.findViewById(R.id.cost_img);

            added_by_res_tv = itemView.findViewById(R.id.added_by_res_tv);
            qty_tv = itemView.findViewById(R.id.qty_tv);
            date_res_tv = itemView.findViewById(R.id.resource_date_tv_);
            type_res_tv = itemView.findViewById(R.id.type_res_tv);
        }
    }
}
