package com.i9930.croptrails.CropCycle.Create.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.i9930.croptrails.CropCycle.Create.Model.AddCycleAgriDatum;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitActivityForm.Adapter.AgriSelectSpinnerAdapter;
import com.i9930.croptrails.Utility.AppConstants;

import java.util.List;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class AgriInputAdapter extends RecyclerView.Adapter<AgriInputAdapter.Holder> {
    Context context;
    List<AddCycleAgriDatum> cycleDatumList;
    LayoutInflater inflater;

    public AgriInputAdapter(Context context, List<AddCycleAgriDatum> cycleDatumList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.cycleDatumList = cycleDatumList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_add_crop_cycle_agri_input, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        AddCycleAgriDatum datum=cycleDatumList.get(position);
        if (cycleDatumList.get(position).getAgriDatumList() != null && cycleDatumList.get(position).getAgriDatumList().size() > 0) {
            AgriSelectSpinnerAdapter adapter = new AgriSelectSpinnerAdapter(context, cycleDatumList.get(position).getAgriDatumList());
            holder.agriInputSpinner.setAdapter(adapter);
            if (cycleDatumList.get(position).getSelectedAgriDatum() != null)
                holder.agriInputSpinner.setSelectedItem(cycleDatumList.get(position).getSelectedAgriDatum());
            holder.agriInputSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(View view, int i, long id) {
                    try {
                        cycleDatumList.get(position).setSelectedAgriDatum(adapter.getItem(holder.agriInputSpinner.getSelectedPosition()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected() {

                }
            });

        }

        holder.etCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s!=null)
                    cycleDatumList.get(position).setCost(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.etQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s!=null)
                    cycleDatumList.get(position).setQty(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.agriInputLabel.setText("Input "+(position+1));

        //set data
        if (datum.getSelectedAgriDatum()!=null)
            holder.agriInputSpinner.setSelectedItem(datum.getSelectedAgriDatum());
        if (AppConstants.isValidString(datum.getCost()))
            holder.etCost.setText(datum.getCost());
        if (AppConstants.isValidString(datum.getQty()))
            holder.etQty.setText(datum.getQty());
    }

    @Override
    public int getItemCount() {
        if (cycleDatumList==null)
        return  0 ;
        else return  cycleDatumList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        SearchableSpinner agriInputSpinner;
        EditText etQty, etCost;
        TextView agriInputLabel;

        public Holder(@NonNull View itemView) {
            super(itemView);
            agriInputSpinner = itemView.findViewById(R.id.agriInputSpinner);
            agriInputLabel = itemView.findViewById(R.id.agriInputLabel);
            etQty = itemView.findViewById(R.id.etQty);
            etCost = itemView.findViewById(R.id.etCost);
        }
    }
}

