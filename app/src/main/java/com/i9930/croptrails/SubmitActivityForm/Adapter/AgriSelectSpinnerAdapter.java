package com.i9930.croptrails.SubmitActivityForm.Adapter;

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
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterSeason;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.Season;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitActivityForm.Model.CompAgriDatum;

public class AgriSelectSpinnerAdapter extends ArrayAdapter<CompAgriDatum> implements Filterable, ISpinnerSelectedView {

    private Context mContext;
    private List<CompAgriDatum> mBackupStrings;
    private List<CompAgriDatum> mStrings;
    private AgriSelectSpinnerAdapter.StringFilter mStringFilter = new AgriSelectSpinnerAdapter.StringFilter();

    public AgriSelectSpinnerAdapter(Context context, List<CompAgriDatum> strings) {
        super(context, R.layout.view_list_item);
        mContext = context;
        mStrings = strings;
        mBackupStrings = strings;
    }

    @Override
    public int getCount() {
        return mStrings == null ? 0 : mStrings.size() + 1;
    }

    @Override
    public CompAgriDatum getItem(int position) {
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
                dispalyName.setText(mStrings.get(position-1).getName());
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
                dispalyName.setText(mStrings.get(position-1).getName());
            }catch (Exception e){}
        }
        return view;
    }

    @Override
    public View getNoSelectionView() {
        View view = View.inflate(mContext, R.layout.view_list_no_selection_item, null);

        try {
            TextView dispalyName = (TextView) view.findViewById(R.id.TxtVw_DisplayName);
            dispalyName.setText(mContext.getResources().getString(R.string.select_season_prompt));
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
            final List<CompAgriDatum> filterStrings = new ArrayList<>();
            for (CompAgriDatum text : mBackupStrings) {
                if (text.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
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



