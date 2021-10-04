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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;
import com.i9930.croptrails.AddFarm.Adapter.AddFarmAdapter2;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.Season;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitActivityForm.Model.AgriInput.AgriInput;
import com.i9930.croptrails.SubmitActivityForm.Model.ChemicalDataList;
import com.i9930.croptrails.SubmitActivityForm.Model.CompAgriDatum;
import com.i9930.croptrails.Utility.AppConstants;

public class AgriInputAdapter extends RecyclerView.Adapter<AgriInputAdapter.MyViewHolder> {

    Context context;
    List<AgriInput> list;
    //    List<CompAgriDatum>compAgriDatumList=new ArrayList<>();
    List<AgriInputAdapter.MyViewHolder> myViewHolderList = new ArrayList<>();
    List<Integer> positionList = new ArrayList<>();

    public AgriInputAdapter(Context context, List<AgriInput> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AgriInputAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_agri_input, parent, false);
        return new AgriInputAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
//        position=position;
         holder.actAmountEt.setTag(holder);
        holder.actQtyEt.setTag(holder);
        holder.expAmountEt.setTag(holder);
        holder.expQtyEt.setTag(holder);
        holder.otherAgriNameEt.setTag(holder);
        holder.instructionTv.setTag(holder);
        holder.expectedLayout.setTag(holder);
        holder.inputSpinner.setTag(holder);
        holder.otherAgriNameTi.setTag(holder);
        if (list.get(position).isManualAdded()) {
            holder.instructionTv.setVisibility(View.GONE);
            holder.expectedLayout.setVisibility(View.GONE);
            holder.inputSpinner.setVisibility(View.VISIBLE);
            holder.removeItemImg.setVisibility(View.VISIBLE);

            holder.otherAgriNameEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    try {
                        list.get(position).setName(charSequence.toString().trim());
//                    notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            holder.removeItemImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        list.remove(position);
                        notifyDataSetChanged();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        } else {
            holder.expectedLayout.setVisibility(View.VISIBLE);
            holder.instructionTv.setVisibility(View.VISIBLE);
            holder.inputSpinner.setVisibility(View.GONE);
            holder.otherAgriNameTi.setVisibility(View.GONE);
            holder.removeItemImg.setVisibility(View.GONE);
        }
        if (AppConstants.isValidString(list.get(position).getAmount()))
            holder.expAmountEt.setText(list.get(position).getAmount());
        else
            holder.expAmountEt.setText("-");
        if (AppConstants.isValidString(list.get(position).getQuantity()))
            holder.expQtyEt.setText(list.get(position).getQuantity());
        else
            holder.expQtyEt.setText("-");
        if (AppConstants.isValidString(list.get(position).getName()))
            holder.instructionTv.setText(list.get(position).getName());
        else
            holder.instructionTv.setText("Unknown");

        if (AppConstants.isValidString(list.get(position).getCost())) {
            holder.actAmountEt.setText(list.get(position).getCost());
        } else {
            holder.actAmountEt.setText("");
        }
        if (AppConstants.isValidString(list.get(position).getUsedQuantity())) {
            holder.actQtyEt.setText(list.get(position).getUsedQuantity());
        } else {
            holder.actQtyEt.setText("");
        }
        holder.actAmountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    list.get(position).setCost(charSequence.toString().trim());
//                    list.get(position).setCost(charSequence.toString().trim());
//                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.actQtyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    list.get(position).setUsedQuantity(charSequence.toString().trim());
//                    list.get(position).setUsedQuantity(charSequence.toString().trim());
//                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (positionList.contains(position)) {
            Log.e("AgriInputAdapter", "Item at " + position + " Already added");
        } else {
            Log.e("AgriInputAdapter", "Item at " + position + " Adding");
            positionList.add(position);
            myViewHolderList.add(holder);
        }


        List<CompAgriDatum> compAgriDatumList = list.get(position).getCompAgriDatumList();
        if (compAgriDatumList != null && compAgriDatumList.size() > 0) {
            AgriSelectSpinnerAdapter adapter = new AgriSelectSpinnerAdapter(context, compAgriDatumList);
            holder.inputSpinner.setAdapter(adapter);
            try {
                if (list.get(position).getSelectedIndex() > 0)
                    holder.inputSpinner.setSelectedItem(list.get(position).getSelectedIndex());
            }catch (Exception e){
                e.printStackTrace();
            }
            holder.inputSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(View view, int i, long id) {
                    try {
                        list.get(position).setSelectedIndex(i);
                        CompAgriDatum season = adapter.getItem(i);
                        if (season != null) {
                            list.get(position).setAgriId(season.getId());
                            list.get(position).setInputId(season.getId());
                            if (AppConstants.isValidString(season.getName()) && season.getName().toLowerCase().equals("other") || season.getName().toLowerCase().equals("others")) {
                                holder.otherAgriNameTi.setVisibility(View.VISIBLE);
                            } else {
                                holder.otherAgriNameTi.setVisibility(View.GONE);
                            }
                            list.get(position).setName(season.getName());
                        } else {
                            list.get(position).setAgriId(null);
                            holder.otherAgriNameTi.setVisibility(View.VISIBLE);
                            list.get(position).setName(null);
                        }
//                            holder.otherAgriNameEt.setText("");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected() {

                }
            });
        } else {
            holder.inputSpinner.setVisibility(View.GONE);
            if (list.get(position).isManualAdded())
            holder.otherAgriNameTi.setVisibility(View.VISIBLE);
            else
                holder.otherAgriNameTi.setVisibility(View.GONE);
        }

        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        int ref;
        EditText actAmountEt, actQtyEt,otherAgriNameEt;
        TextView expAmountEt, expQtyEt;
        TextView instructionTv;
        LinearLayout expectedLayout;
        SearchableSpinner inputSpinner;
        TextInputLayout otherAgriNameTi;
        ImageView removeItemImg;

        public MyViewHolder(View itemView) {
            super(itemView);
            otherAgriNameEt = itemView.findViewById(R.id.otherAgriNameEt);
            removeItemImg = itemView.findViewById(R.id.removeItemImg);
            otherAgriNameTi = itemView.findViewById(R.id.otherAgriNameTi);
            inputSpinner = itemView.findViewById(R.id.inputSpinner);
            expectedLayout = itemView.findViewById(R.id.expectedLayout);
            actAmountEt = itemView.findViewById(R.id.actAmountEt);
            actQtyEt = itemView.findViewById(R.id.actQtyEt);
            instructionTv = itemView.findViewById(R.id.instructionTv);
            expAmountEt = itemView.findViewById(R.id.expAmountEt);
            expQtyEt = itemView.findViewById(R.id.expQtyEt);
        }
    }

   /* public void setErrorAndFocus(int position, String from, String msg) {
        AgriInputAdapter.MyViewHolder holder = myViewHolderList.get(position);
        if (from.equals("id")) {
            holder.ti_et_farmid.getParent().requestChildFocus(holder.ti_et_farmid, holder.ti_et_farmid);
            holder.ti_et_farmid.setError(msg);
        } else if (from.equals("area")) {
            holder.ti_et_farmarea.getParent().requestChildFocus(holder.ti_et_farmarea, holder.ti_et_farmarea);
            holder.ti_et_farmarea.setError(msg);
        } else if (from.equals("season")) {
            holder.seasonSpinner.getParent().requestChildFocus(holder.seasonSpinner, holder.seasonSpinner);
        } else if (from.equals("crop")) {
            holder.cropSpinner.getParent().requestChildFocus(holder.cropSpinner, holder.cropSpinner);
        }
        else if (from.equals("cropP")) {
            try {
                holder.cropSpinnerPrevious.getParent().requestChildFocus(holder.cropSpinnerPrevious, holder.cropSpinnerPrevious);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }*/
}

