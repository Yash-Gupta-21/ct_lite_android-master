package com.i9930.croptrails.AddFarm.SpinnerAdapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gr.escsoft.michaelprimez.searchablespinner.interfaces.ISpinnerSelectedView;
import com.i9930.croptrails.CommonClasses.DDNew;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.AppConstants;

public class SpinnerAdapterNewDDValue extends ArrayAdapter<DDNew> implements Filterable, ISpinnerSelectedView {
    private Context mContext;
    private List<DDNew> mBackupStrings;
    private List<DDNew> mStrings;
    private SpinnerAdapterNewDDValue.StringFilter mStringFilter = new SpinnerAdapterNewDDValue.StringFilter();

    String prompt = "";

    public SpinnerAdapterNewDDValue(Context context, List<DDNew> strings, String prompt) {
        super(context, R.layout.view_list_item);
        mContext = context;
        mStrings = strings;
        this.prompt = prompt;
        mBackupStrings = strings;
    }

    @Override
    public int getCount() {
        return mStrings == null ? 0 : mStrings.size() + 1;
    }

    @Override
    public DDNew getItem(int position) {
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
        } else
        {
            view = View.inflate(mContext, R.layout.view_list_item, null);
            TextView dispalyName = (TextView) view.findViewById(R.id.TxtVw_DisplayName);
            try {
                dispalyName.setText(mStrings.get(position - 1).getValue());

            } catch (Exception e) {

            }
        }
        return view;
    }

    @Override
    public View getSelectedView(int position) {
        View view = null;
        if (position == 0) {
            view = getNoSelectionView();
        } else
        {
            view = View.inflate(mContext, R.layout.view_list_item, null);
            TextView dispalyName = (TextView) view.findViewById(R.id.TxtVw_DisplayName);

            try {
                dispalyName.setText(mStrings.get(position - 1).getValue());

            } catch (Exception e) {

            }
        }
        return view;
    }

    @Override
    public View getNoSelectionView() {
        View view = View.inflate(mContext, R.layout.view_list_no_selection_item, null);

        try {
            TextView dispalyName = (TextView) view.findViewById(R.id.TxtVw_DisplayName);
            dispalyName.setText(prompt);
        } catch (Exception e) {
        }
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
            final List<DDNew> filterStrings = new ArrayList<>();
            for (DDNew text : mBackupStrings) {

                if (AppConstants.isValidString(text.getValue()) && text.getValue().toLowerCase().trim().contains(constraint.toString().toLowerCase().trim())) {
                    filterStrings.add(text);
                }
            }
            filterResults.count = filterStrings.size();
            filterResults.values = filterStrings;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mStrings = (List) results.values;
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

