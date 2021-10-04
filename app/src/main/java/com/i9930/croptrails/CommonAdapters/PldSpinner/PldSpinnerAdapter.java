package com.i9930.croptrails.CommonAdapters.PldSpinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.i9930.croptrails.CommonClasses.Cluster;
import com.i9930.croptrails.CommonClasses.PldReason;
import com.i9930.croptrails.R;

public class PldSpinnerAdapter  extends BaseAdapter {


    List<PldReason> pldReasonList;
    Context context;

    public PldSpinnerAdapter(Context context,List<PldReason> pldReasonList) {
        this.pldReasonList= pldReasonList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pldReasonList.size();
    }

    @Override
    public Object getItem(int i) {
        return pldReasonList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vie2= LayoutInflater.from(context).inflate(R.layout.spinner_layout,null);
        TextView spinner_items_tv=vie2.findViewById(R.id.spinner_items_tv);

        spinner_items_tv.setText(pldReasonList.get(i).getName());

        return vie2;
    }
}
