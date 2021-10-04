package com.i9930.croptrails.AssignCalendar.Adapter;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.i9930.croptrails.AssignCalendar.Model.CalendarDatum;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.AppConstants;

import static com.i9930.croptrails.Utility.AppConstants.getCapSentence;
import static com.i9930.croptrails.Utility.AppConstants.getImagePath;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.Holder> {

    Context context;
    List<CalendarDatum>calendarDatumList;
    OnAssignClicked onAssignClicked;

    public CalendarAdapter(Context context, List<CalendarDatum> calendarDatumList,OnAssignClicked onAssignClicked) {
        this.context = context;
        this.onAssignClicked=onAssignClicked;
        this.calendarDatumList = calendarDatumList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_layout_crop_cycle_list,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.cycleNameTv.setText(getCapSentence(calendarDatumList.get(position).getCycleName()));
//        holder.cycleNameTv.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        holder.seasonNameTv.setText(getCapSentence(calendarDatumList.get(position).getGrowingSeason()));
        holder.regionNameTv.setText(getCapSentence(calendarDatumList.get(position).getGrowingRegion()));
        holder.cropNameTv.setText(getCapSentence(calendarDatumList.get(position).getCropName()));
        holder.soilNameTv.setText(getCapSentence(calendarDatumList.get(position).getSoilType()));
        if (onAssignClicked!=null) {
            holder.onAssign.setVisibility(View.VISIBLE);
            holder.onAssign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAssignClicked.onClick(calendarDatumList.get(position));
                }
            });
        }else{
            holder.onAssign.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (calendarDatumList!=null)
        return calendarDatumList.size();
        else return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView cycleNameTv,cropNameTv,seasonNameTv,regionNameTv,soilNameTv,onAssign;
        public Holder(@NonNull View itemView) {
            super(itemView);
            cycleNameTv=itemView.findViewById(R.id.cycleNameTv);

            cropNameTv=itemView.findViewById(R.id.cropNameTv);
            seasonNameTv=itemView.findViewById(R.id.seasonNameTv);
            regionNameTv=itemView.findViewById(R.id.regionNameTv);
            soilNameTv=itemView.findViewById(R.id.soilNameTv);
            onAssign=itemView.findViewById(R.id.onAssign);
        }
    }


    public interface OnAssignClicked{
        public void onClick(CalendarDatum calendarDatum);
    }
}
