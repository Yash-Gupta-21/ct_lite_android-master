package com.i9930.croptrails.CommonAdapters.FarmerSpinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.i9930.croptrails.AddFarm.Model.FarmerSpinnerData;
import com.i9930.croptrails.R;

public class FarmerSpinnerAdapter extends BaseAdapter {

    Context context;
    List<FarmerSpinnerData> farmerDataList;

    public FarmerSpinnerAdapter(Context context, List<FarmerSpinnerData> farmerDataList) {
        this.context = context;
        this.farmerDataList = farmerDataList;
    }

    @Override
    public int getCount() {
        return farmerDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return farmerDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vie2= LayoutInflater.from(context).inflate(R.layout.spinner_layout,null);
        TextView spinner_items_tv=vie2.findViewById(R.id.spinner_items_tv);

        spinner_items_tv.setText(farmerDataList.get(i).getName());

        return vie2;
    }

}
