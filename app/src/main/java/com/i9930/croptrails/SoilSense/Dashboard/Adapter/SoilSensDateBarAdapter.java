package com.i9930.croptrails.SoilSense.Dashboard.Adapter;

import android.content.Context;
import android.hardware.Sensor;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.i9930.croptrails.R;
import com.i9930.croptrails.ShowInputCost.Adapters.InputCostAdapter;
import com.i9930.croptrails.ShowInputCost.Model.InputCostData;
import com.i9930.croptrails.SoilSense.Dashboard.Model.GetSoilDatum;
import com.i9930.croptrails.SoilSense.SendSoilData;
import com.i9930.croptrails.Utility.AppConstants;

public class SoilSensDateBarAdapter extends RecyclerView.Adapter<SoilSensDateBarAdapter.Holder> {

    public interface OnSoilDateClickListener{
        public void onDateClicked(String key, List<GetSoilDatum>sendSoilDataList);
    }

    Context context;
    HashMap<String, List<GetSoilDatum>> stringListHashMap;
    List<String> indexes;
    OnSoilDateClickListener listener;
    public  int  lastSelectedPosition=-1;
    public SoilSensDateBarAdapter(Context context, HashMap<String, List<GetSoilDatum>> stringListHashMap,List<String> indexes,OnSoilDateClickListener listener) {
        this.context = context;
        this.stringListHashMap = stringListHashMap;
        this.listener=listener;

        this.indexes= indexes; // <== Parse
        lastSelectedPosition=indexes.size()-1;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_soilsens_date_bar, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        try {

            holder.dateTv.setText(AppConstants.getShowableDate(indexes.get(position),context));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastSelectedPosition!=-1){
                       /* if (lastSelectedPosition==getAdapterPosition()){
                            lastSelectedPosition = -1;
                            listener.onCompanySelected(-1);
                            notifyDataSetChanged();
                        }else */{

                            lastSelectedPosition = position;
                            notifyDataSetChanged();
                            listener.onDateClicked(indexes.get(position),stringListHashMap.get(indexes.get(position)));
                        }
                    }else {
                        lastSelectedPosition = position;
                        listener.onDateClicked(indexes.get(position),stringListHashMap.get(indexes.get(position)));
                        notifyDataSetChanged();
                    }
                }
            });

            if (lastSelectedPosition==position){
                holder.dateTv.setTextColor(context.getColor(R.color.white));
                holder.dateTv.setBackgroundResource(R.drawable.large_edittext_dark_filled_background);
            }else {
                holder.dateTv.setBackgroundResource(R.drawable.large_edittext_background2);
                holder.dateTv.setTextColor(R.color.black);
            }
        } catch (Exception e) {
            Log.e("InputCostAdapter", e.toString());
        }



    }

    @Override
    public int getItemCount() {
        if (indexes!=null)
        return indexes.size();
        else return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        //ImageView cost_img;
        TextView dateTv;

        public Holder(@NonNull View itemView) {
            super(itemView);
            //cost_img = itemView.findViewById(R.id.cost_img);
            dateTv = itemView.findViewById(R.id.dateTv);




        }
    }

}
