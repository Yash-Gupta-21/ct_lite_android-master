package com.i9930.croptrails.FarmDetails.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitHealthCard.Model.CardParameters;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCard;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCardData;

public class HealthCardAdapter extends RecyclerView.Adapter<HealthCardAdapter.Holder> {

    Context context;
    List<CardParameters>paramsList;
    int []colors={R.color.red,R.color.colorPrimary,R.color.colorPrimaryDark,R.color.blue,R.color.quantum_bluegrey50,R.color.notification_read,R.color.turqouise,
            R.color.red,R.color.colorPrimary,R.color.colorPrimaryDark,R.color.blue,R.color.quantum_bluegrey50,R.color.notification_read,R.color.turqouise,
            R.color.red,R.color.colorPrimary,R.color.colorPrimaryDark,R.color.blue,R.color.quantum_bluegrey50,R.color.notification_read,R.color.turqouise};

    public HealthCardAdapter(Context context, List<CardParameters> paramsList) {
        this.context = context;
        this.paramsList = paramsList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_layout_show_health_card,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int t=new Random().nextInt((colors.length - 1) + 1) + 0;
        Log.e("HealthAdapter","Color "+t);
        holder.arrowTv.setTextColor(colors[t]);
        holder.arrowTv.setText("\u27A3"+" ");
        holder.paramTv.setText(paramsList.get(position).getParameter());
        holder.paramValueTv.setText(paramsList.get(position).getValue());
        holder.paramUnitTv.setText(paramsList.get(position).getUnit());

    }

    @Override
    public int getItemCount() {
        if (paramsList!=null)
        return paramsList.size();
        else return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView arrowTv,paramTv,paramValueTv,paramUnitTv;
        public Holder(@NonNull View itemView) {
            super(itemView);
            arrowTv=itemView.findViewById(R.id.arrowTv);
            paramTv=itemView.findViewById(R.id.paramTv);
            paramValueTv=itemView.findViewById(R.id.paramValueTv);
            paramUnitTv=itemView.findViewById(R.id.paramUnitTv);
        }
    }
}
