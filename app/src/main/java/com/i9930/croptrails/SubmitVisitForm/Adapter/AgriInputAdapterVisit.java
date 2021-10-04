package com.i9930.croptrails.SubmitVisitForm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitActivityForm.Model.DoneActNew.DoneActInputs;
import com.i9930.croptrails.SubmitVisitForm.Model.VisitDatum;

public class AgriInputAdapterVisit extends RecyclerView.Adapter<AgriInputAdapterVisit.MyViewHolder> {

    Context context;
    List<VisitDatum> list;
    //    List<CompAgriDatum>compAgriDatumList=new ArrayList<>();
    List<AgriInputAdapterVisit.MyViewHolder> myViewHolderList = new ArrayList<>();
    List<Integer> positionList = new ArrayList<>();

    public AgriInputAdapterVisit(Context context, List<VisitDatum> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AgriInputAdapterVisit.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_agri_input_actual_data, parent, false);
        return new AgriInputAdapterVisit.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AgriInputAdapterVisit.MyViewHolder holder, final int position) {
//        position=position;
        holder.actAmountEt.setTag(holder);
        holder.actQtyEt.setTag(holder);
        holder.actAmountEt.setText(list.get(position).getAmount());
        holder.actQtyEt.setText(list.get(position).getQuantity());
        holder.instructionTv.setText(list.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        int ref;
        TextView actAmountEt, actQtyEt,instructionTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            actAmountEt = itemView.findViewById(R.id.actAmountEt);
            actQtyEt = itemView.findViewById(R.id.actQtyEt);
            instructionTv = itemView.findViewById(R.id.instructionTv);
        }
    }

}

