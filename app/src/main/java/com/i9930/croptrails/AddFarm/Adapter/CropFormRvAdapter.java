package com.i9930.croptrails.AddFarm.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormDatum;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.Season;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.AppConstants;

public class CropFormRvAdapter extends RecyclerView.Adapter<CropFormRvAdapter.Holder> {
    Context context;
    List<CropFormDatum> cropFormDatumList;

    CropFormDatum cropFormDatumForMore;
    public CropFormRvAdapter(Context context,List<CropFormDatum> cropFormDatumList ) {
        this.context = context;
        this.cropFormDatumList = cropFormDatumList;
        cropFormDatumForMore=this.cropFormDatumList.get(0);
        if (cropFormDatumList!=null)
        Log.e("CropFormRvAdapter","cropFormDatumList lenght "+cropFormDatumList.size());
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_add_farm_crop_2_child, parent, false);
        return new CropFormRvAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
//        CropFormDatum cropFormDatum=cropFormDatumList.get(position);

        if (position==0){
            holder.delete.setVisibility(View.GONE);
        }else {
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        cropFormDatumList.remove(position);
                        notifyDataSetChanged();
                    }catch (Exception e){

                    }
                }
            });
        }


        holder.addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cropFormDatumList.add(CropFormDatum.copy(cropFormDatumForMore));
                    notifyDataSetChanged();
                }catch (Exception e){

                }
            }
        });

        if (AppConstants.isValidString(cropFormDatumList.get(position).getType())) {
            holder.formHead.setText(cropFormDatumList.get(position).getType());

        }

        if (AppConstants.isValidString(cropFormDatumList.get(position).getCol1())){
            holder.tiColumn1.setHint(cropFormDatumList.get(position).getCol1());
            holder.etColumn1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence!=null&&!TextUtils.isEmpty(charSequence)){
                        cropFormDatumList.get(position).setCol1Value(charSequence.toString().trim());
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            if (AppConstants.isValidString(cropFormDatumList.get(position).getCol1Value())){
                holder.etColumn1.setText(cropFormDatumList.get(position).getCol1Value());
            }
        }else {
            holder.tiColumn1.setVisibility(View.GONE);
        }
        if (AppConstants.isValidString(cropFormDatumList.get(position).getCol2())){
            holder.tiColumn2.setHint(cropFormDatumList.get(position).getCol2());
            holder.etColumn2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence!=null&&!TextUtils.isEmpty(charSequence)){
                        cropFormDatumList.get(position).setCol2Value(charSequence.toString().trim());
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            if (AppConstants.isValidString(cropFormDatumList.get(position).getCol2Value())){
                holder.etColumn2.setText(cropFormDatumList.get(position).getCol2Value());
            }

        }else {
            holder.tiColumn2.setVisibility(View.GONE);
        }
        if (AppConstants.isValidString(cropFormDatumList.get(position).getCol3())){
            holder.tiColumn3.setHint(cropFormDatumList.get(position).getCol3());
            holder.etColumn3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence!=null&&!TextUtils.isEmpty(charSequence)){
                        cropFormDatumList.get(position).setCol3Value(charSequence.toString().trim());
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            if (AppConstants.isValidString(cropFormDatumList.get(position).getCol3Value())){
                holder.etColumn3.setText(cropFormDatumList.get(position).getCol3Value());
            }

        }else {
            holder.tiColumn3.setVisibility(View.GONE);
        }
        if (AppConstants.isValidString(cropFormDatumList.get(position).getCol4())){
            holder.tiColumn4.setHint(cropFormDatumList.get(position).getCol4());
            holder.etColumn4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence!=null&&!TextUtils.isEmpty(charSequence)){
                        cropFormDatumList.get(position).setCol4Value(charSequence.toString().trim());
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            if (AppConstants.isValidString(cropFormDatumList.get(position).getCol4Value())){
                holder.etColumn4.setText(cropFormDatumList.get(position).getCol4Value());
            }

        }else {
            holder.tiColumn4.setVisibility(View.GONE);
        }
        if (AppConstants.isValidString(cropFormDatumList.get(position).getCol5())){
            holder.tiColumn5.setHint(cropFormDatumList.get(position).getCol5());
            holder.etColumn5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence!=null&&!TextUtils.isEmpty(charSequence)){
                        cropFormDatumList.get(position).setCol5Value(charSequence.toString().trim());
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            if (AppConstants.isValidString(cropFormDatumList.get(position).getCol5Value())){
                holder.etColumn5.setText(cropFormDatumList.get(position).getCol5Value());
            }

        }else {
            holder.tiColumn5.setVisibility(View.GONE);
        }
        if (AppConstants.isValidString(cropFormDatumList.get(position).getCol6())){
            holder.tiColumn6.setHint(cropFormDatumList.get(position).getCol6());
            holder.etColumn6.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence!=null&&!TextUtils.isEmpty(charSequence)){
                        cropFormDatumList.get(position).setCol6Value(charSequence.toString().trim());
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            if (AppConstants.isValidString(cropFormDatumList.get(position).getCol6Value())){
                holder.etColumn6.setText(cropFormDatumList.get(position).getCol6Value());
            }
        }else {
            holder.tiColumn6.setVisibility(View.GONE);
        }

        try {
            holder.setIsRecyclable(false);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if (cropFormDatumList != null)
            return cropFormDatumList.size();
        else return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView formHead;
        EditText etColumn1,etColumn2,etColumn3,etColumn4,etColumn5,etColumn6;
        TextInputLayout tiColumn1,tiColumn2,tiColumn3,tiColumn4,tiColumn5,tiColumn6;
        ImageView delete;
        ImageView addMore;


        public Holder(@NonNull View itemView) {
            super(itemView);

            delete=itemView.findViewById(R.id.delete);
            formHead=itemView.findViewById(R.id.formHead);
            addMore=itemView.findViewById(R.id.addMore);

            etColumn1=itemView.findViewById(R.id.etColumn1);
            etColumn2=itemView.findViewById(R.id.etColumn2);
            etColumn3=itemView.findViewById(R.id.etColumn3);
            etColumn4=itemView.findViewById(R.id.etColumn4);
            etColumn5=itemView.findViewById(R.id.etColumn5);
            etColumn6=itemView.findViewById(R.id.etColumn6);
            tiColumn1=itemView.findViewById(R.id.tiColumn1);
            tiColumn2=itemView.findViewById(R.id.tiColumn2);
            tiColumn3=itemView.findViewById(R.id.tiColumn3);
            tiColumn4=itemView.findViewById(R.id.tiColumn4);
            tiColumn5=itemView.findViewById(R.id.tiColumn5);
            tiColumn6=itemView.findViewById(R.id.tiColumn6);

        }
    }
}

