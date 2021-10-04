package com.i9930.croptrails.SubmitVisitForm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineInnerData;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitActivityForm.Adapter.AgriInputAdapter;
import com.i9930.croptrails.SubmitActivityForm.Model.AgriInput.AgriInput;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Utility.AppConstants;

public class PendingActAdapter extends RecyclerView.Adapter<PendingActAdapter.MyViewHolder> {

    public interface OnItemClickListener{
        public void onItemClicked(int index,TimelineInnerData timelineInnerData);
    }

    Context context;
    List<TimelineInnerData> list;
    OnItemClickListener listener;
    Date todayDate;
    Date c;
    SimpleDateFormat df;
    String todayDateStr = "";
    public PendingActAdapter(Context context, List<TimelineInnerData> list,OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener=listener;
        c = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("yyyy-MM-dd");
        todayDateStr = df.format(c);
        try {
            todayDate = df.parse(TestActivity.todayDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @NonNull
    @Override
    public PendingActAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_layout_pending_activity,parent,false);
        return new PendingActAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingActAdapter.MyViewHolder holder, final int position) {
        TimelineInnerData data=list.get(position);
        Date farmDate=null;
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        try {
            farmDate = formatDate.parse(data.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int count=position+1;
        String name=data.getActivity();
        String date="";
        if (AppConstants.isValidString(data.getDate())){
            date=" ("+AppConstants.getShowableDate(data.getDate(),context)+") ";
        }
        String finalStr=count+". "+name+date;

        holder.tv.setText(finalStr);





        if (todayDate != null && farmDate != null && (todayDate.before(farmDate)||todayDate.equals(farmDate))) {
            holder.tv.setTextColor(context.getResources().getColor(R.color.timeline_act_upcomming));
        }else {
            holder.tv.setTextColor(context.getResources().getColor(R.color.red));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(position,list.get(position));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.tv);
        }
    }
}

