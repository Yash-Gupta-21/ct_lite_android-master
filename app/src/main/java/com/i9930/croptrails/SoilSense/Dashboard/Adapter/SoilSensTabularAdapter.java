package com.i9930.croptrails.SoilSense.Dashboard.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import com.i9930.croptrails.R;
import com.i9930.croptrails.SoilSense.Dashboard.Model.GetSoilDatum;
import com.i9930.croptrails.SoilSense.SoilDetails;
import com.i9930.croptrails.Utility.AppConstants;

public class SoilSensTabularAdapter extends RecyclerView.Adapter<SoilSensTabularAdapter.Holder> {

    public interface OnSoilDateClickListener {
        public void onDateClicked(String key, List<GetSoilDatum> GetSoilDatumList);
    }

    Context context;
    List<GetSoilDatum> stringListHashMap;

    public SoilSensTabularAdapter(Context context, List<GetSoilDatum> stringListHashMap) {
        this.context = context;
        this.stringListHashMap = stringListHashMap;

    }

    @NonNull
    @Override
    public SoilSensTabularAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_soilsens_tabular, parent, false);
        return new SoilSensTabularAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoilSensTabularAdapter.Holder holder, int position) {
        SoilDetails  details=null;
        try {
            details=new Gson().fromJson(stringListHashMap.get(position).getDetailStr(),SoilDetails.class);
        }catch (Exception e){}
        try {
            if (position == 0) {
                holder.dateTv.setText("Time");
//                holder.dateTv.setBackgroundColor(context.getColor(R.color.lightgreen));
//                holder.locationTv.setBackgroundColor(context.getColor(R.color.lightgreen));
//                holder.moistureTv.setBackgroundColor(context.getColor(R.color.lightgreen));

                holder.locationTv.setText("Location");
                holder.moistureTv.setText("Moisture");

//                holder.v1.setVisibility(View.VISIBLE);
//                holder.v2.setVisibility(View.VISIBLE);

                holder.layout1.setBackgroundResource(R.drawable.large_edittext_background_black);
                holder.layout2.setBackgroundResource(R.drawable.large_edittext_background_black);
                holder.layout3.setBackgroundResource(R.drawable.large_edittext_background_black);
            } else {
                holder.dateTv.setText(AppConstants.getShowableDate(stringListHashMap.get(position).getDoa(),context));
                holder.locationTv.setText((stringListHashMap.get(position).getLatitude() + "," + stringListHashMap.get(position).getLongitude()));
                if (details != null && AppConstants.isValidString(details.getReading()))
                    holder.moistureTv.setText((details.getReading()+"%"));

                holder.layout1.setBackgroundResource(R.drawable.large_edittext_background_black_lr);
                holder.layout2.setBackgroundResource(R.drawable.large_edittext_background_black_lr);
                holder.layout3.setBackgroundResource(R.drawable.large_edittext_background_black_lr);
//                holder.v1.setVisibility(View.GONE);
//                holder.v2.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("InputCostAdapter", e.toString());
        }


    }

    @Override
    public int getItemCount() {
        if (stringListHashMap != null)
            return stringListHashMap.size();
        else return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        //ImageView cost_img;
        TextView dateTv, locationTv, moistureTv;
        View v1, v2;
        LinearLayout layout3,layout1,layout2;

        public Holder(@NonNull View itemView) {
            super(itemView);
            //cost_img = itemView.findViewById(R.id.cost_img);
//            v1 = itemView.findViewById(R.id.v1);
//            v2 = itemView.findViewById(R.id.v2);
            dateTv = itemView.findViewById(R.id.dateTimeTv);
            locationTv = itemView.findViewById(R.id.locationTv);
            moistureTv = itemView.findViewById(R.id.moistureTv);

            layout3 = itemView.findViewById(R.id.layout3);
            layout1 = itemView.findViewById(R.id.layout1);
            layout2 = itemView.findViewById(R.id.layout2);


        }
    }

}

