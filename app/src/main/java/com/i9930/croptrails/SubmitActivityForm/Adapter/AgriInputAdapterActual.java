package com.i9930.croptrails.SubmitActivityForm.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitActivityForm.Model.AgriInput.AgriInput;
import com.i9930.croptrails.SubmitActivityForm.Model.CompAgriDatum;
import com.i9930.croptrails.SubmitActivityForm.Model.DoneActNew.DoneActInputs;
import com.i9930.croptrails.Utility.AppConstants;

public class AgriInputAdapterActual extends RecyclerView.Adapter<AgriInputAdapterActual.MyViewHolder> {

    Context context;
    List<DoneActInputs> list;
    //    List<CompAgriDatum>compAgriDatumList=new ArrayList<>();
    List<AgriInputAdapterActual.MyViewHolder> myViewHolderList = new ArrayList<>();
    List<Integer> positionList = new ArrayList<>();

    public AgriInputAdapterActual(Context context, List<DoneActInputs> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AgriInputAdapterActual.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_agri_input_actual_data, parent, false);
        return new AgriInputAdapterActual.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AgriInputAdapterActual.MyViewHolder holder, final int position) {
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

