package com.i9930.croptrails.CommonAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.i9930.croptrails.CommonClasses.Cluster;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitActivityForm.Model.TimelineChemicals;

public class ClusterSpinnerAdapter extends BaseAdapter {


    List<Cluster> clusterList;
    Context context;

    public ClusterSpinnerAdapter(Context context,List<Cluster> clusterList) {
        this.clusterList = clusterList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return clusterList.size();
    }

    @Override
    public Object getItem(int i) {
        return clusterList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vie2= LayoutInflater.from(context).inflate(R.layout.spinner_layout,null);
        TextView spinner_items_tv=vie2.findViewById(R.id.spinner_items_tv);

        spinner_items_tv.setText(clusterList.get(i).getClusterName());

        return vie2;
    }

}
