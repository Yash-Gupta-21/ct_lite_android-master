package com.i9930.croptrails.AgriInput.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.i9930.croptrails.AgriInput.Model.FarmAgriInput;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.AppConstants;

public class FarmAgriAdapter extends RecyclerView.Adapter<FarmAgriAdapter.Holder> {

    Activity context;
    List<FarmAgriInput> inputList;
    public FarmAgriAdapter(Activity context, List<FarmAgriInput> inputList) {
        this.context = context;
        this.inputList = inputList;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_adapter_farm_agri,parent,false);
        return new Holder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        FarmAgriInput input=inputList.get(position);
//        int sn=(position+1);
        String name="",actQty=""+input.getQuantity(),qty="",unit="";

        if (AppConstants.isValidString(input.getParameters())){
            unit="(in "+input.getParameters()+")";
        }
        holder.unitTvExp.setText(unit);
        holder.unitTvAct.setText(unit);
        if (AppConstants.isValidString(input.getName())&&(!input.getName().equalsIgnoreCase("other")||!input.getName().equalsIgnoreCase("others"))){
            name=input.getName();
        }else if (AppConstants.isValidString(input.getOther_agri_input())){
            name =input.getOther_agri_input();
        }
        if (input.getExp_quanity()!=null)
            qty=""+input.getExp_quanity();
//        holder.amountTv.setText(amount);
        holder.qtyTv.setText(qty);
        holder.actQtyTv.setText(actQty);
        holder.nameTv.setText(name);
    }

    @Override
    public int getItemCount() {
        if (inputList==null)
            return 0;
        else return inputList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView actQtyTv,nameTv,qtyTv,unitTvExp,unitTvAct;


        public Holder(@NonNull View itemView) {
            super(itemView);
            unitTvAct=itemView.findViewById(R.id.unitTvAct);
            actQtyTv=itemView.findViewById(R.id.actQtyTv);
            unitTvExp=itemView.findViewById(R.id.unitTvExp);
            nameTv=itemView.findViewById(R.id.nameTv);
            qtyTv=itemView.findViewById(R.id.qtyTv);
//            amountTv=itemView.findViewById(R.id.amountTv);
        }
    }
}

