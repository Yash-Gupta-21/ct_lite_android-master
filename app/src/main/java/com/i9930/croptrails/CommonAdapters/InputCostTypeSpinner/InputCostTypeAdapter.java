package com.i9930.croptrails.CommonAdapters.InputCostTypeSpinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.i9930.croptrails.AddFarm.Model.FarmerSpinnerData;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitInputCost.Model.ExpenseDataDD;

public class InputCostTypeAdapter extends BaseAdapter {

    Context context;
    List<ExpenseDataDD> expenseDataDDList;

    public InputCostTypeAdapter(Context context, List<ExpenseDataDD> expenseDataDDList) {
        this.context = context;
        this.expenseDataDDList = expenseDataDDList;
    }

    @Override
    public int getCount() {
        return expenseDataDDList.size();
    }

    @Override
    public Object getItem(int i) {
        return expenseDataDDList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vie2= LayoutInflater.from(context).inflate(R.layout.spinner_layout,null);
        TextView spinner_items_tv=vie2.findViewById(R.id.spinner_items_tv);

        spinner_items_tv.setText(expenseDataDDList.get(i).getType());

        return vie2;
    }

}
