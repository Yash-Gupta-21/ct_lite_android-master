package com.i9930.croptrails.CommonAdapters.IrrigationSourceSpinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.IrrigationSource;
import com.i9930.croptrails.R;

public class IrrigationSourceAdapter extends ArrayAdapter {

    List<IrrigationSource> irrigationSourceList;
    int mResource;
    Context context;

    /*public IrrigationSourceAdapter(Context context, List<IrrigationSource> irrigationSourceList) {
        this.context = context;
        this.irrigationSourceList = irrigationSourceList;
    }*/

    public IrrigationSourceAdapter(@NonNull Context context, int resource, List<IrrigationSource> irrigationSourceList) {
        super(context, resource);
        this.irrigationSourceList = irrigationSourceList;
        this.mResource=resource;
        this.context = context;
    }

    @Override
    public int getCount() {
        return irrigationSourceList.size();
    }

    @Override
    public Object getItem(int i) {
        return irrigationSourceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

      /*  View vie2= LayoutInflater.from(context).inflate(mResource,viewGroup,false);
        TextView spinner_items_tv=vie2.findViewById(R.id.spinner_items_tv);

        spinner_items_tv.setText(irrigationSourceList.get(i).getName());*/

        return createItemView(i,view,viewGroup);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }
    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = LayoutInflater.from(context).inflate(mResource, parent, false);

        TextView spinner_items_tv = (TextView) view.findViewById(R.id.spinner_items_tv);

        spinner_items_tv.setText(irrigationSourceList.get(position    ).getName());

        return view;
    }
}
