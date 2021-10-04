package com.i9930.croptrails.Calendar.Adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.i9930.croptrails.CommonAdapters.CalendarAct.ActivityNameAdapter;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Test.ObjectType;


public class CalendarViewAdapter extends BaseAdapter {
    public static List<String> day_string;
    static int count = 0;
    public GregorianCalendar pmonth;
    /**
     * calendar instance for previous month for getting complete view
     */
    public GregorianCalendar pmonthmaxset;
    public List<Object> emp_data_arr;
    String tag = "CalendarActivity";
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int mnthlength;
    String itemvalue, curentDateString;
    DateFormat df;
    private Activity context;
    private java.util.Calendar month;
    private GregorianCalendar selectedDate;
    private ArrayList<String> items;
    private String gridvalue;
    private ListView listDetails;
    private ArrayList<Object> customList = new ArrayList<Object>();
    List<ObjectType> objectTypeList;


    DisplayMetrics displayMetrics = new DisplayMetrics();
    int height = 0;
    int width = 0;
    HashMap<String, List<Object>> hashMap;

    public interface OnItemClickListener{
        public void onItemClicked(Object object);
    }
    OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CalendarViewAdapter(Activity context, GregorianCalendar monthCalendar,
                               ArrayList<Object> emp_data_arr,
                               List<ObjectType> objectTypes, HashMap<String, List<Object>> hashMap) {
        this.emp_data_arr = emp_data_arr;
        this.objectTypeList = objectTypes;
        this.hashMap = hashMap;
        CalendarViewAdapter.day_string = new ArrayList<String>();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        Locale.setDefault(Locale.ENGLISH);
        month = monthCalendar;
        selectedDate = (GregorianCalendar) monthCalendar.clone();
        this.context = context;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);
        this.items = new ArrayList<String>();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        curentDateString = df.format(selectedDate.getTime());
        refreshDays();

    }

    public int getCount() {
        return day_string.size();
    }

    public Object getItem(int position) {
        return day_string.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new view for each item referenced by the Adapter
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;


        TextView dayView;
        ListView activityName;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.grid_item, null);

        }
        int w = width / 7;
        int h = height / 6;
        Log.e("CalendarViewActivity", "W1: " + width + ", H1: " + height + ", W2: " + w + ", H2: " + h);
        v.setLayoutParams(new GridView.LayoutParams(w, h));

        dayView = v.findViewById(R.id.date);
        activityName = v.findViewById(R.id.activityName);
        String[] separatedTime = day_string.get(position).split("-");
        gridvalue = separatedTime[2].replaceFirst("^0*", "");
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            dayView.setTextColor(Color.parseColor("#A9A9A9"));
            //  context.getResources().getColor(R.color.colorAccent);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            dayView.setTextColor(Color.parseColor("#A9A9A9"));
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else {
            // setting curent month's days in blue color.
            dayView.setTextColor(Color.parseColor("#696969"));
        }
        if (day_string.get(position).equals(curentDateString)) {

            v.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            v.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        dayView.setText(gridvalue);
        if (df.format(new Date()).equals(day_string.get(position))) {
            dayView.setTypeface(null, Typeface.BOLD);
            dayView.setTextColor(Color.parseColor("#000000"));
        }
        // create date string for comparison
        String date = day_string.get(position);
        if (date.length() == 1) {
            date = "0" + date;
        }
        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }


        /*try {

            Log.e("CalendarViewAdapter",day_string.get(position)+" => "+new Gson().toJson(hashMap.get(format2.format(format.parse(day_string.get(position))))));

        }catch (Exception e){
            e.printStackTrace();
        }*/


        setEventView(v, position, dayView, activityName);
        return v;
    }

    public void refreshDays() {
        // clear items
        items.clear();
        day_string.clear();
        Locale.setDefault(Locale.ENGLISH);
        pmonth = (GregorianCalendar) month.clone();
        // month start day. ie; sun, mon, etc
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        // finding number of weeks in current month.
        maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP(); // previous month maximum day 31,30....
        calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
        pmonthmaxset = (GregorianCalendar) pmonth.clone();

        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

        for (int n = 0; n < mnthlength; n++) {

            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            day_string.add(itemvalue);
        }
    }

    private int getMaxP() {
        int maxP;
        if (month.get(GregorianCalendar.MONTH) == month.getActualMinimum(GregorianCalendar.MONTH)) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1), month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pmonth.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }

    public void setEventView(View v, int position, TextView txt, ListView activityName) {
        DateFormat format2 = new SimpleDateFormat("yyyy-MM");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        activityName.setVisibility(View.GONE);
        try {
            if (hashMap.containsKey(format2.format(format.parse(day_string.get(position)))))
                emp_data_arr = hashMap.get(format2.format(format.parse(day_string.get(position))));
            else
                emp_data_arr = null;
        } catch (Exception e) {
            e.printStackTrace();
            emp_data_arr = null;
        }
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {

        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {

        } else {
            if (position % 7 == 0) {
                txt.setTextColor(context.getResources().getColor(R.color.red));
            }
            if (emp_data_arr != null) {
                int len = emp_data_arr.size();
                String s = "";
                List<String> names = new ArrayList<>();
                List<Object> objectList = new ArrayList<>();
                for (int i = 0; i < len; i++) {
                    int monthSize = day_string.size();
                    Object empData = emp_data_arr.get(i);
                    Log.e("CalendarViewAdapter", "Inside for");
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(new Gson().toJson(empData));
                        String date = obj.getString("doa");
                        if (monthSize > position) {
                            if (format.parse(obj.getString("doa")).equals(format.parse(day_string.get(position)))) {
                                Log.e("CalendarViewAdapter", "ObjData " + new Gson().toJson(empData));
                                String type = obj.getString("compare_type");
                                objectList.add(emp_data_arr.get(i));
                                if (type != null) {
                                    if (s.isEmpty()) {
                                        s = type;
                                        names.add(type);
                                    } else if (!s.contains(type)) {
                                        s = s + "," + type;
                                        names.add(type);
                                    }
                                }
                                txt.setTextColor(context.getResources().getColor(R.color.blue));
                            }
                        }
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                }
                Log.e("CalendarViewAdapter", "Value of s " + s);
                if (s.isEmpty()) {
                    activityName.setVisibility(View.GONE);
                } else {
                    List<String> namess = Arrays.asList(s.split(","));
                    activityName.setVisibility(View.VISIBLE);
                    ActivityNameAdapter adapter = new ActivityNameAdapter(namess, context, objectList,onItemClickListener);
                    activityName.setAdapter(adapter);
                }
                if (objectList.size() > 0) {
                    v.setClickable(true);
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*Intent intent = new Intent(context, DayDetailActivity.class);
                            DataHandler.newInstance().setObjectList(objectList);
                            context.startActivity(intent);*/
                            if (onItemClickListener!=null)
                            onItemClickListener.onItemClicked(objectList.get(0));

                        }
                    });

                } else {
                    v.setClickable(false);
                }
            }
        }
    }

   /* public void getPositionList(String date, final Activity act) {
        int len = emp_data_arr.size();
        JSONArray jbarrays = new JSONArray();
        for (int j = 0; j < len; j++) {
            if (emp_data_arr.get(j).date.equals(date)) {
                HashMap<String, String> maplist = new HashMap<String, String>();
                maplist.put("EntryTime", emp_data_arr.get(j).entry_time);
                maplist.put("ExitTime", emp_data_arr.get(j).exit_time);
                String entryTime = emp_data_arr.get(j).entry_time;
                String exitTime = emp_data_arr.get(j).exit_time;
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                Timestamp timestamp1;
                Timestamp timestamp2;
                if (entryTime != null) {
                    timestamp1 = Timestamp.valueOf(entryTime);
                    Date entryTimeDt = new Date(timestamp1.getTime());
                    entryTime = dateFormat.format(entryTimeDt);
                    maplist.put("EntryTime12Hours", entryTime);
                } else {
                    String noEntryTime = "NA";
                    maplist.put("EntryTime12Hours", noEntryTime);
                }

                if (exitTime != null) {
                    timestamp2 = Timestamp.valueOf(exitTime);
                    Date exitTimeDt = new Date(timestamp2.getTime());
                    exitTime = dateFormat.format(exitTimeDt);
                    maplist.put("ExitTime12Hours", exitTime);
                } else {
                    String noExitTime = "NA";
                    maplist.put("ExitTime12Hours", noExitTime);
                }
                maplist.put("Status", String.valueOf(emp_data_arr.get(j).isOnLeave));
                JSONObject json1 = new JSONObject(maplist);
                jbarrays.put(json1);
            }
        }
        if (jbarrays.length() != 0) {
            final Dialog dialogs = new Dialog(context);
            dialogs.setContentView(R.layout.dialog_information);
            listDetails = dialogs.findViewById(R.id.list_details);
            ImageView imgCross = dialogs.findViewById(R.id.img_cross);
            listDetails.setAdapter(new DialogAdapter(context, getMatchList(jbarrays + "")));
            imgCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogs.dismiss();
                }
            });
            dialogs.show();

        }

    }

    private ArrayList<Object> getMatchList(String detail) {
        try {
            JSONArray jsonArray = new JSONArray(detail);
            customList = new ArrayList<Object>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                Object emp_data = new Object();
                emp_data.setEntry_time(jsonObject.optString("EntryTime"));
                emp_data.setExit_time(jsonObject.optString("ExitTime"));
                emp_data.setIsOnLeave(Boolean.parseBoolean(jsonObject.optString("Status")));
                emp_data.setEntryTime12Hour(jsonObject.optString("EntryTime12Hours"));
                emp_data.setExitTime12Hour(jsonObject.optString("ExitTime12Hours"));
                customList.add(emp_data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customList;
    }
*/
}


