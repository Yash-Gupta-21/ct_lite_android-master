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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormDatum;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.AppConstants;

public class CropFormRvAdapterOuter extends RecyclerView.Adapter<CropFormRvAdapterOuter.Holder> {
    Context context;
    List<List<CropFormDatum>> cropFormDatumList;

    public CropFormRvAdapterOuter(Context context,List<List<CropFormDatum>> cropFormDatumList ) {
        this.context = context;
        this.cropFormDatumList = cropFormDatumList;

    }

    @NonNull
    @Override
    public CropFormRvAdapterOuter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_add_farm_crop_2_child_outer, parent, false);
        return new CropFormRvAdapterOuter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CropFormRvAdapterOuter.Holder holder, int position) {


        CropFormRvAdapter cropFormRvAdapter=new CropFormRvAdapter(context,cropFormDatumList.get(position));

        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setNestedScrollingEnabled(false);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(cropFormRvAdapter);
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
        RecyclerView recyclerView;

        public Holder(@NonNull View itemView) {
            super(itemView);

            recyclerView=itemView.findViewById(R.id.recyclerView);

        }
    }
}

