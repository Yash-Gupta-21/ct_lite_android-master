package com.i9930.croptrails.CommonAdapters.InputResourceCostTypeSpinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.i9930.croptrails.AddFarm.Model.FarmerSpinnerData;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitInputCost.Model.ResourceDataDD;

public class InputResourceCostTypeAdapter extends BaseAdapter {

    Context context;
    List<ResourceDataDD>resourceDataDDList;

    public InputResourceCostTypeAdapter(Context context, List<ResourceDataDD>resourceDataDDList) {
        this.context = context;
        this.resourceDataDDList = resourceDataDDList;
    }

    @Override
    public int getCount() {
        return resourceDataDDList.size();
    }

    @Override
    public Object getItem(int i) {
        return resourceDataDDList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vie2= LayoutInflater.from(context).inflate(R.layout.spinner_layout,null);
        TextView spinner_items_tv=vie2.findViewById(R.id.spinner_items_tv);

        spinner_items_tv.setText(resourceDataDDList.get(i).getType());

        return vie2;
    }
}
