package com.i9930.croptrails.CommonAdapters;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gr.escsoft.michaelprimez.searchablespinner.interfaces.ISpinnerSelectedView;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.AppConstants;

public class SimpleStringAdapter extends ArrayAdapter<String> implements Filterable, ISpinnerSelectedView {

    private Context mContext;
    private ArrayList<String> mBackupStrings;
    private ArrayList<String> mStrings;
    private SimpleStringAdapter.StringFilter mStringFilter = new SimpleStringAdapter.StringFilter();
    Resources resources;
    public SimpleStringAdapter(Context context, ArrayList<String> strings) {
        super(context, R.layout.view_list_item);
        mContext = context;
        resources=context.getResources();
        mStrings = strings;
        mBackupStrings = strings;
        label="";
    }
    String label;
    public SimpleStringAdapter(Context context, ArrayList<String> strings,String label) {
        super(context, R.layout.view_list_item);
        mContext = context;
        this.label=label;
        resources=context.getResources();
        mStrings = strings;
        mBackupStrings = strings;
    }
    boolean withStatus=false;

    public SimpleStringAdapter(Context context, ArrayList<String> strings,String label,boolean withFarmStatus) {
        super(context, R.layout.view_list_item);
        mContext = context;
        this.label=label;
        this.withStatus=withFarmStatus;
        resources=context.getResources();
        mStrings = strings;
        mBackupStrings = strings;
    }

    @Override
    public int getCount() {
        return mStrings == null ? 0 : mStrings.size() + 1;
    }

    @Override
    public String getItem(int position) {
        if (mStrings != null && position > 0)
            return mStrings.get(position - 1);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        if (mStrings == null && position > 0)
            return mStrings.get(position).hashCode();
        else
            return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (position == 0) {
            view = getNoSelectionView();
        } else {
            view = View.inflate(mContext, R.layout.view_list_item, null);
            try {
                TextView dispalyName = (TextView) view.findViewById(R.id.TxtVw_DisplayName);
                String name=mStrings.get(position-1);
                TextView statusTv = (TextView) view.findViewById(R.id.statusTv);
                dispalyName.setText(name);

                if (withStatus&& AppConstants.isValidString(name)){
                    statusTv.setVisibility(View.VISIBLE);
                    if (name.equalsIgnoreCase("all")){
                        statusTv.setBackgroundColor(resources.getColor(R.color.grey));
                    }else
                    if (name.equals(resources.getString(R.string.delinquent))){
                        statusTv.setBackgroundColor(resources.getColor(R.color.darkpurple));
                    }else if (name.equals(resources.getString(R.string.selected_farms))){
                        statusTv.setBackgroundColor(resources.getColor(R.color.darkgreen));
                    }else if (name.equals(resources.getString(R.string.fresh_farms))){
                        statusTv.setBackgroundColor(resources.getColor(R.color.yellow));
                    }else if (name.equals(resources.getString(R.string.data_entry_farms))){
                        statusTv.setBackgroundColor(resources.getColor(R.color.orange));
                    }else {
                        statusTv.setVisibility(View.GONE);
                    }
                }else {
                    statusTv.setVisibility(View.GONE);
                }


            }catch (Exception e){}
        }
        return view;
    }

    @Override
    public View getSelectedView(int position) {
        View view = null;
        if (position == 0) {
            view = getNoSelectionView();
        } else {
            view = View.inflate(mContext, R.layout.view_list_item, null);
            try {
                TextView dispalyName = (TextView) view.findViewById(R.id.TxtVw_DisplayName);
                String name=mStrings.get(position-1);
                TextView statusTv = (TextView) view.findViewById(R.id.statusTv);
                dispalyName.setText(name);
                if (withStatus&& AppConstants.isValidString(name)){
                    statusTv.setVisibility(View.VISIBLE);
                    if (name.equals(resources.getString(R.string.delinquent))){
                        statusTv.setBackgroundColor(resources.getColor(R.color.darkpurple));
                    }else if (name.equals(resources.getString(R.string.selected_farms))){
                        statusTv.setBackgroundColor(resources.getColor(R.color.darkgreen));
                    }else if (name.equals(resources.getString(R.string.fresh_farms))){
                        statusTv.setBackgroundColor(resources.getColor(R.color.yellow));
                    }else if (name.equals(resources.getString(R.string.data_entry_farms))){
                        statusTv.setBackgroundColor(resources.getColor(R.color.orange));
                    }else {
                        statusTv.setVisibility(View.GONE);
                    }
                }else {
                    statusTv.setVisibility(View.GONE);
                }
            }catch (Exception e){}
        }
        return view;
    }

    @Override
    public View getNoSelectionView() {
        View view = View.inflate(mContext, R.layout.view_list_no_selection_item, null);
        try {
            TextView dispalyName = (TextView) view.findViewById(R.id.TxtVw_DisplayName);
            dispalyName.setText(""+label);
        }catch (Exception e){}
        return view;
    }


    @Override
    public Filter getFilter() {
        return mStringFilter;
    }

    public class StringFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final FilterResults filterResults = new FilterResults();
            if (TextUtils.isEmpty(constraint)) {
                filterResults.count = mBackupStrings.size();
                filterResults.values = mBackupStrings;
                return filterResults;
            }
            final ArrayList<String> filterStrings = new ArrayList<>();
            for (String text : mBackupStrings) {
                if (text.toLowerCase().contains(constraint.toString().toLowerCase())) {
                    filterStrings.add(text);
                }
            }
            filterResults.count = filterStrings.size();
            filterResults.values = filterStrings;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mStrings = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    }

    private class ItemView {
        public ImageView mImageView;
        public TextView mTextView;
    }

    public enum ItemViewType {
        ITEM, NO_SELECTION_ITEM;
    }
}

