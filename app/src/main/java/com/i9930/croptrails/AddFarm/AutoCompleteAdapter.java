package com.i9930.croptrails.AddFarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.i9930.croptrails.AddFarm.Model.FarmerSpinnerData;
import com.i9930.croptrails.R;

public class AutoCompleteAdapter extends ArrayAdapter<FarmerSpinnerData> implements Filterable {

    private List<FarmerSpinnerData> fullList;
    private List<FarmerSpinnerData> mOriginalValues;
    private ArrayFilter mFilter;

    Context context;
    public AutoCompleteAdapter(Context context, int resource, List<FarmerSpinnerData> objects) {

        super(context, resource, objects);
        fullList = (List<FarmerSpinnerData>) objects;
        this.context=context;
        mOriginalValues = fullList;
    }

    @Override
    public int getCount() {
        return fullList.size();
    }

    @Override
    public FarmerSpinnerData getItem(int position) {
        return fullList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.spinner_layout, parent,
                false);
        TextView nameView = (TextView) rowView.findViewById(R.id.spinner_items_tv);

        nameView.setText(fullList.get(position).getName());

        return rowView;
    }


    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }



    private class ArrayFilter extends Filter {
        private Object lock=new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (lock) {
                    mOriginalValues = new ArrayList<FarmerSpinnerData>(fullList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    ArrayList<FarmerSpinnerData> list = new ArrayList<FarmerSpinnerData>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                final String prefixString = prefix.toString().toLowerCase();

                List<FarmerSpinnerData> values = mOriginalValues;
                int count = values.size();

                ArrayList<FarmerSpinnerData> newValues = new ArrayList<FarmerSpinnerData>(count);

                for (int i = 0; i < count; i++) {
                    FarmerSpinnerData item = values.get(i);
                    if (item.getName().toLowerCase().contains(prefixString.toLowerCase())) {
                        newValues.add(item);
                    }

                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        if(results.values!=null){
        fullList = (ArrayList<FarmerSpinnerData>) results.values;
        }else{
            fullList = new ArrayList<FarmerSpinnerData>();
        }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}