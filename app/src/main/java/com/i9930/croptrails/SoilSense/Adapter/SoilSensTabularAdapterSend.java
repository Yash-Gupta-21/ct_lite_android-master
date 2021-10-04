package com.i9930.croptrails.SoilSense.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.i9930.croptrails.FarmerInnerDashBoard.Models.Image;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SoilSense.SendSoilData;
import com.i9930.croptrails.Utility.AppConstants;

public class SoilSensTabularAdapterSend extends RecyclerView.Adapter<SoilSensTabularAdapterSend.Holder> {

    public interface OnSoilDateDeleteListener {
        public void onItemRemoved(int index);
    }

    Context context;
    List<SendSoilData.Data> stringListHashMap;
    OnSoilDateDeleteListener listener;
    public SoilSensTabularAdapterSend(Context context, List<SendSoilData.Data> stringListHashMap,OnSoilDateDeleteListener listener) {
        this.context = context;
        this.listener=listener;
        this.stringListHashMap = stringListHashMap;

    }

    @NonNull
    @Override
    public SoilSensTabularAdapterSend.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_soilsens_tabular_send, parent, false);
        return new SoilSensTabularAdapterSend.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoilSensTabularAdapterSend.Holder holder, int position) {

        try {
            /* if (position == 0) {
                holder.dateTv.setText("Time");
                holder.locationTv.setText("Location");
                holder.moistureTv.setText("Moisture");
                holder.deleteTv.setText(context.getResources().getString(R.string.delete));
                holder.deleteTv.setVisibility(View.VISIBLE);
                holder.deleteImage.setVisibility(View.GONE);
                holder.layout1.setBackgroundResource(R.drawable.large_edittext_background_black);
                holder.layout2.setBackgroundResource(R.drawable.large_edittext_background_black);
                holder.layout3.setBackgroundResource(R.drawable.large_edittext_background_black);
                holder.layout4.setBackgroundResource(R.drawable.large_edittext_background_black);
            } else {*/
                holder.deleteTv.setVisibility(View.GONE);
                holder.deleteImage.setVisibility(View.VISIBLE);
                holder.deleteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if (listener!=null)
                                listener.onItemRemoved(position);
//                            stringListHashMap.remove()
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                holder.dateTv.setText(AppConstants.getShowableDate(stringListHashMap.get(position).getDoa(),context));
                holder.locationTv.setText((stringListHashMap.get(position).getLatitude() + "," + stringListHashMap.get(position).getLongitude()));
                if (stringListHashMap.get(position).getDetail() != null && AppConstants.isValidString(stringListHashMap.get(position).getDetail().getReading()))
                    holder.moistureTv.setText((stringListHashMap.get(position).getDetail().getReading()+"%"));
//                else if (stringListHashMap.get(position).getDetails() != null && AppConstants.isValidString(stringListHashMap.get(position).getDetails().getReading()))
//                    holder.moistureTv.setText(AppConstants.getShowableDate(stringListHashMap.get(position).getDetails().getReading()));

                holder.layout1.setBackgroundResource(R.drawable.large_edittext_background_black_lr);
                holder.layout2.setBackgroundResource(R.drawable.large_edittext_background_black_lr);
                holder.layout3.setBackgroundResource(R.drawable.large_edittext_background_black_lr);
                holder.layout4.setBackgroundResource(R.drawable.large_edittext_background_black_lr);
//                holder.v1.setVisibility(View.GONE);
//                holder.v2.setVisibility(View.GONE);
//            }
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
        TextView dateTv, locationTv, moistureTv,deleteTv;
        View v1, v2;
        LinearLayout layout3,layout1,layout2,layout4;
        ImageView deleteImage;

        public Holder(@NonNull View itemView) {
            super(itemView);
            layout4=itemView.findViewById(R.id.layout4);
            //cost_img = itemView.findViewById(R.id.cost_img);
//            v1 = itemView.findViewById(R.id.v1);
//            v2 = itemView.findViewById(R.id.v2);
            deleteTv=itemView.findViewById(R.id.deleteTv);
            dateTv = itemView.findViewById(R.id.dateTimeTv);
            locationTv = itemView.findViewById(R.id.locationTv);
            deleteImage=itemView.findViewById(R.id.deleteImage);
            moistureTv = itemView.findViewById(R.id.moistureTv);

            layout3 = itemView.findViewById(R.id.layout3);
            layout1 = itemView.findViewById(R.id.layout1);
            layout2 = itemView.findViewById(R.id.layout2);


        }
    }

}

