package com.i9930.croptrails.CommonAdapters.CalendarAct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.i9930.croptrails.Calendar.Adapter.CalendarViewAdapter;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.AppConstants;

public class ActivityNameAdapter extends ArrayAdapter<String> {


    private List<String> dataSet;
    Context mContext;
    List<Object> objectList;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        LinearLayout layout;
    }

    CalendarViewAdapter.OnItemClickListener listener;

    public ActivityNameAdapter(List<String> data, Context context, List<Object> objectList, CalendarViewAdapter.OnItemClickListener listener) {
        super(context, R.layout.listview_act_name_layout, data);
        this.dataSet = data;
        this.listener = listener;
        this.mContext = context;
        this.objectList = objectList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag
        final View result;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.listview_act_name_layout, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.layout = convertView.findViewById(R.id.layout);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.txtName.setText(name);
        if (name.equals(AppConstants.TIMELINE.VR))
            viewHolder.layout.setBackgroundResource(R.drawable.timeline_name_bg_visit);
        else if (name.equals(AppConstants.TIMELINE.HR))
            viewHolder.layout.setBackgroundResource(R.drawable.timeline_name_bg_harvest);
        else if (name.equals(AppConstants.TIMELINE.SELL))
            viewHolder.layout.setBackgroundResource(R.drawable.timeline_name_bg_sell);
        else if (name.equals(AppConstants.TIMELINE.GE))
            viewHolder.layout.setBackgroundResource(R.drawable.timeline_name_bg_plant_popu);
        else if (name.equals(AppConstants.TIMELINE.HC))
            viewHolder.layout.setBackgroundResource(R.drawable.timeline_name_bg_health_card);
        else if (name.equals(AppConstants.TIMELINE.PLD))
            viewHolder.layout.setBackgroundResource(R.drawable.timeline_name_bg_loss_damage);
        else if (name.equals(AppConstants.TIMELINE.IC))
            viewHolder.layout.setBackgroundResource(R.drawable.timeline_name_bg_input_cost);
        else if (name.equals(AppConstants.TIMELINE.RU))
            viewHolder.layout.setBackgroundResource(R.drawable.timeline_name_bg_resource_use);
        else if (name.equals(AppConstants.TIMELINE.SAT))
            viewHolder.layout.setBackgroundResource(R.drawable.timeline_name_bg_satellite);
        else if (name.equals(AppConstants.TIMELINE.FARM))
            viewHolder.layout.setBackgroundResource(R.drawable.timeline_name_bg_farm);
        else if (name.equals(AppConstants.TIMELINE.ACT))
            viewHolder.layout.setBackgroundResource(R.drawable.timeline_name_bg_cal_act);


        // Return the completed view to render on screen
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(mContext, DayDetailActivity.class);
                DataHandler.newInstance().setObjectList(objectList);
                mContext.startActivity(intent);*/
                if (objectList != null && objectList.size() > 0)
                    listener.onItemClicked(objectList.get(0));

            }
        });

        return convertView;
    }
}
