package com.i9930.croptrails.FarmDetails.Adapter;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import com.i9930.croptrails.CommonClasses.TimelineUnits;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.FarmDetails.FullScreenDialog.FullScreenDialog;
import com.i9930.croptrails.FarmDetails.FullScreenDialog.FullScreenDialogVisit;
import com.i9930.croptrails.FarmDetails.Model.WeatherForecast;
import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineInnerData;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitActivityForm.TimelineFormActivity2;
import com.i9930.croptrails.SubmitInputCost.InputCostActivity;
import com.i9930.croptrails.SubmitVisitForm.TimelineAddVisitActivity2;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;
import com.i9930.croptrails.Weather.WeatherActivity;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.MyViewHolder> {

    Context context;
    List<TimelineInnerData> timelineDataList;
    List<TimelineUnits> timelineUnit;
    Activity activity2;

    int lastPosition;
    WeatherForecast weatherForecast;

    Date c;
    SimpleDateFormat df;
    String todayDateStr = "";
    Date todayDate;
    Date farmDate;
    Date nextFarmDate;
    List<String> doneDateList = new ArrayList<>();

    Resources resources;
    private boolean isCurrentLocation=false;

    public TimelineAdapter(Context context, Activity activity, List<TimelineInnerData> timelineDataList, List<TimelineUnits> timelineUnit, WeatherForecast weatherForecast) {
        this.context = context;
        this.weatherForecast = weatherForecast;
        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();
        this.timelineDataList = timelineDataList;
        this.timelineUnit = timelineUnit;
        lastPosition = timelineDataList.size() - 2;
        this.activity2 = activity;
        c = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("dd/MM/yyyy");
        todayDateStr = df.format(c);
        try {
            todayDate = df.parse(todayDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        lastPosition = this.timelineDataList.size() - 2;

        Log.e("TimelineAdapter", "Initial Size " + this.timelineDataList.size());
        for (int count = 0; count < this.timelineDataList.size(); count++) {
            try {
                DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = srcDf.parse(this.timelineDataList.get(count).getDate());
                Date dateView = srcDf.parse(this.timelineDataList.get(count).getDate());
                DateFormat srcDfView = new SimpleDateFormat("dd MMM");
                doneDateList.add(srcDfView.format(dateView));

                Log.e("Adapter", date.toString());
                DateFormat destDf = new SimpleDateFormat("dd/MM/yyyy");
                this.timelineDataList.get(count).setDate(destDf.format(date));
            } catch (ParseException e) {
                Log.e("Adapter", e.toString());
            }

        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.timeline_rv_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        //try {
        Log.e("TimelineAdapter", "Size " + timelineDataList.size());
        holder.setIsRecyclable(false);
        String text;
        if (timelineDataList.get(position).getDescription() != null) {
            text = resources.getString(R.string.narration)+": " + timelineDataList.get(position).getDescription();
        } else {
            text = resources.getString(R.string.narration)+": "+"-";
        }
        if (position == 0) {
            holder.parent_rel_layout2.setPadding(0, 15, 0, 0);
        }
        if (position == lastPosition) {
            final float scale = context.getResources().getDisplayMetrics().density;
            int pixels = (int) (45 * scale + 0.5f);
            holder.timeline_difference_line.getLayoutParams().height = pixels;

            holder.parent_rel_layout2.setPadding(0, 0, 0, pixels);
            holder.timeline_difference_line.setVisibility(View.GONE);
            holder.difference_line_visit_td.setVisibility(View.GONE);
            holder.difference_line_visit.setVisibility(View.GONE);
            holder.timeline_difference_line_weather.setVisibility(View.GONE);

        } else {
            holder.timeline_difference_line.getLayoutParams().width = 3;
            holder.difference_line_visit_td.getLayoutParams().width = 3;
            holder.difference_line_visit.getLayoutParams().width = 3;


        }
        holder.timeline_arrow_img.setVisibility(View.GONE);
        holder.setIsRecyclable(false);
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(text);
        ssBuilder.setSpan(
                new StyleSpan(Typeface.BOLD), // span to add
                text.indexOf(resources.getString(R.string.narration)+":"), // start of the span (inclusive)
                text.indexOf(resources.getString(R.string.narration)+":") + String.valueOf(resources.getString(R.string.narration)+":").length(), // end of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // do not extend the span when text add later
        );
        if (timelineDataList.get(position).getDescription() != null)
            holder.full_details_tv2.setText(ssBuilder);
        else {

        }
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        try {
            farmDate = formatDate.parse(timelineDataList.get(position).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (todayDate != null && farmDate != null) {
            if (timelineDataList.get(position).getFarmCalActivityId() != null && !timelineDataList.get(position).getFarmCalActivityId().equals("0")) {
                holder.all_activities_layout.setVisibility(View.VISIBLE);
                holder.all_old_visit_layout.setVisibility(View.GONE);
                holder.today_visit_layout.setVisibility(View.GONE);
                holder.harvest_inner_main_lay.setVisibility(View.GONE);
                holder.germi_inner_main_lay.setVisibility(View.GONE);
                holder.linear_weather_lay.setVisibility(View.GONE);

                if (timelineDataList.get(position).getActivityImg() != null && !timelineDataList.get(position).getActivityImg().equals("0")) {
                    Glide.with(context).
                            asBitmap().
                            load(timelineDataList.get(position).getActivityImg()).
                            into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    holder.centerImage.setImageBitmap(resource);

                                }

                            });
                } else {

                }
                if (timelineDataList.get(position).getActivityType() != null) {
                    holder.timeline_activity_type.setText(timelineDataList.get(position).getActivityType());
                }
                if (timelineDataList.get(position).getActivity() != null) {
                    holder.timeline_activity.setText(timelineDataList.get(position).getActivity());
                }
                //when activity date is equal today date
                if (todayDate.equals(farmDate)) {
                    if (timelineDataList.get(position).getIsDone().equals("Y")) {
                        holder.timeline_center_image_dot.setImageResource(R.color.timeline_act_done);
                        holder.timeline_date.setText(doneDateList.get(position));
                        holder.timeline_date.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                        holder.more_button_tv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                        holder.centerImage.setBorderColor(context.getResources().getColor(R.color.timeline_act_done));
                        holder.timeline_difference_line.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                        holder.more_button_tv.setText(resources.getString(R.string.view_more_label));
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (timelineDataList.get(position).getActivityImg() != null)
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, timelineDataList.get(position).getActivityImg());
                                else
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, "0");

                                DataHandler.newInstance().setTimelineInnerData(timelineDataList.get(position));
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTITY_ID, timelineDataList.get(position).getFarmCalActivityId());
                                FullScreenDialog dialog = new FullScreenDialog();
                                FragmentTransaction ft = activity2.getFragmentManager().beginTransaction();
                                dialog.show(ft, FullScreenDialog.TAG);

                            }
                        });
                    } else {
                        holder.timeline_center_image_dot.setImageResource(R.color.timeline_act_today_pending);
                        holder.timeline_date.setText(doneDateList.get(position));
                        holder.timeline_date.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_today_pending));
                        holder.more_button_tv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_today_pending));
                        holder.centerImage.setBorderColor(context.getResources().getColor(R.color.timeline_act_today_pending));
                        holder.timeline_difference_line.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_today_pending));
                        holder.more_button_tv.setText(R.string.add_todays_activity_label);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR)
                                ||SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.ADMIN)
                                        ||SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUB_ADMIN)
                                        ||SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPER_ADMIN)) {
                                    if (timelineDataList.get(position).getActivityImg() != null)
                                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, timelineDataList.get(position).getActivityImg());
                                    else
                                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, "0");

                                    DataHandler.newInstance().setTimelineUnits(timelineUnit);
                                    DataHandler.newInstance().setTimelineInnerData(timelineDataList.get(position));
                                    context.startActivity(new Intent(context, TimelineFormActivity2.class));
                                }
                            }
                        });
                    }
                } else if (todayDate.after(farmDate)) {

                    if (timelineDataList.get(position).getIsDone().equals("Y")) {
                        holder.timeline_center_image_dot.setImageResource(R.color.timeline_act_done);
                        holder.timeline_date.setText(doneDateList.get(position));
                        holder.timeline_date.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                        holder.more_button_tv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                        holder.centerImage.setBorderColor(context.getResources().getColor(R.color.timeline_act_done));
                        holder.timeline_difference_line.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                        holder.more_button_tv.setText(R.string.view_activity_label);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (timelineDataList.get(position).getActivityImg() != null)
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, timelineDataList.get(position).getActivityImg());
                                else
                                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, "0");

                                DataHandler.newInstance().setTimelineInnerData(timelineDataList.get(position));
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTITY_ID, timelineDataList.get(position).getFarmCalActivityId());
                                FullScreenDialog dialog = new FullScreenDialog();
                                FragmentTransaction ft = activity2.getFragmentManager().beginTransaction();
                                dialog.show(ft, FullScreenDialog.TAG);

                            }
                        });
                    } else {

                        holder.timeline_center_image_dot.setImageResource(R.color.timeline_act_pending);
                        holder.timeline_date.setText(doneDateList.get(position));
                        holder.timeline_date.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_pending));
                        holder.more_button_tv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_pending));
                        holder.centerImage.setBorderColor(context.getResources().getColor(R.color.timeline_act_pending));
                        holder.timeline_difference_line.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_pending));
                        holder.more_button_tv.setText(resources.getString(R.string.pending_label));
                        if (getCountOfDays(timelineDataList.get(position).getDate(), todayDateStr) > 15) {
                            holder.activity_card.setAlpha(.5f);
                            holder.view_not_allowed_to_input.setVisibility(View.VISIBLE);
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toasty.error(context, resources.getString(R.string.now_not_allowed_to_add_activity_msg), Toast.LENGTH_LONG, false).show();

                                }
                            });
                        } else {
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPERVISOR)
                                    ||SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.ADMIN)
                                            ||SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUB_ADMIN)
                                            ||SharedPreferencesMethod.getString(context, SharedPreferencesMethod.LOGIN_TYPE).equals(AppConstants.ROLES.SUPER_ADMIN)) {

                                        if (timelineDataList.get(position).getActivityImg() != null)
                                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, timelineDataList.get(position).getActivityImg());
                                        else
                                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_TYPE_IMG, "0");
                                        DataHandler.newInstance().setTimelineUnits(timelineUnit);
                                        DataHandler.newInstance().setTimelineInnerData(timelineDataList.get(position));
                                        context.startActivity(new Intent(context, TimelineFormActivity2.class));
                                    }
                                }
                            });

                        }
                    }
                } else {
                    holder.timeline_center_image_dot.setImageResource(R.color.timeline_act_upcomming);
                    holder.timeline_date.setText(doneDateList.get(position));
                    holder.timeline_date.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_upcomming));
                    holder.more_button_tv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_upcomming));
                    holder.centerImage.setBorderColor(context.getResources().getColor(R.color.timeline_act_upcomming));
                    holder.timeline_difference_line.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_upcomming));


                    holder.more_button_tv.setText(R.string.upcomming_activity_label);
                    holder.more_button_tv.setVisibility(View.INVISIBLE);
                }
            }
            //} else if (timelineDataList.get(position).getResultType().toLowerCase().equals("vr")) {
            //Visible Visit and hide others
            else if (timelineDataList.get(position).getVisitReportId() != null && !timelineDataList.get(position).getVisitReportId().equals("0")) {
                //holder.timeline_difference_line.getLayoutParams().height = 460;


                //holder.timeline_image_visit.setImageResource(R.drawable.ploughed_farm);
                holder.image_loading_progress.setVisibility(View.VISIBLE);


                if (timelineDataList.get(position).getImg_link() != null && !timelineDataList.get(position).getImg_link().equals("0")) {
                    //SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACT_LATEST_IMG, timelineDataList.get(position).getImg_link());
                    Glide.with(context).
                            asBitmap().
                            load(timelineDataList.get(position).getImg_link()).
                            into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    holder.image_loading_progress.setVisibility(View.GONE);
                                    holder.timeline_image_visit.setImageBitmap(resource);

                                }

                            });
                } else {
                    holder.image_loading_progress.setVisibility(View.GONE);
                    holder.timeline_image_visit.setImageResource(R.drawable.ploughed_farm);
                }

                if (timelineDataList.get(position).getActivityType() != null) {
                    holder.timeline_activity_type.setText(timelineDataList.get(position).getActivityType());
                } else {
                    holder.timeline_activity_type.setText(R.string.visit_label);
                }
                if (timelineDataList.get(position).getActivity() == null) {
                    holder.timeline_activity.setText(R.string.visit_label);
                }
                if (todayDate.equals(farmDate)) {

                    if (timelineDataList.get(position).getIsDone() == null) {
                        Log.e("TimelineAdapter", "Added today visit");

                        holder.all_old_visit_layout.setVisibility(View.VISIBLE);
                        holder.today_visit_layout.setVisibility(View.GONE);
                        holder.all_activities_layout.setVisibility(View.GONE);
                        holder.linear_weather_lay.setVisibility(View.GONE);
                        /*final float scale = context.getResources().getDisplayMetrics().density;
                        int pixels = (int) (390 * scale + 0.5f);
                        holder.timeline_difference_line.getLayoutParams().height = pixels;*/

                        holder.dot_image_for_visit.setImageResource(R.color.timeline_vis_done);
                        holder.timeline_date_visit.setText(doneDateList.get(position));
                        holder.timeline_date_visit.setBackgroundColor(context.getResources().getColor(R.color.timeline_vis_done));
                        holder.more_button_tv_visit.setBackgroundColor(context.getResources().getColor(R.color.timeline_vis_done));
                        holder.difference_line_visit.setBackgroundColor(context.getResources().getColor(R.color.timeline_vis_done));

                        holder.timeline_title_visit.setText(R.string.visit_added_for_today_label);
                        holder.timeline_detail_visit.setText("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa.");
                        holder.more_button_tv_visit.setText(R.string.view_visit_label);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferencesMethod.setString(context, SharedPreferencesMethod.VR_ID, timelineDataList.get(position).getVisitReportId());
                                //DataHandler.newInstance().setVisit_report_id(Integer.parseInt(timelineDataList.get(position).getVisitReportId()));
                                FullScreenDialogVisit dialog = new FullScreenDialogVisit();
                                FragmentTransaction ft = activity2.getFragmentManager().beginTransaction();
                                dialog.show(ft, FullScreenDialogVisit.TAG);
                            }
                        });


                    } else if (timelineDataList.get(position).getIsDone().equals("N") && timelineDataList.get(position).getResultType().equals("vr")) {

                        Log.e("TimelineAdapter", "Add today visit");

                        /* CardView today_visit_card;
                        LinearLayout linearLayoutTodayVisit;
                        TextView timeline_date_today_visit,timeline_add_today_visit;*/
                        holder.timeline_add_visit_td_tv.setText(resources.getString(R.string.add_visit_label));

                        holder.linear_weather_lay.setVisibility(View.GONE);
                        holder.all_activities_layout.setVisibility(View.GONE);
                        holder.all_old_visit_layout.setVisibility(View.GONE);
                        holder.today_visit_layout.setVisibility(View.VISIBLE);
                        holder.harvest_inner_main_lay.setVisibility(View.GONE);
                        holder.germi_inner_main_lay.setVisibility(View.GONE);
                        holder.dot_image_for_visit_td.setImageResource(R.color.colorAccent);

                        holder.timeline_date_today_visit.setText(doneDateList.get(position));

                        holder.linearLayoutTodayVisit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               /* if (!SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).equals("")) {
                                    if (Double.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).trim()) > 0) {*/
                                        DataHandler.newInstance().setTimelineUnits(timelineUnit);
//                                        DataHandler.newInstance().setAllMaterials(allMaterials);
                                        context.startActivity(new Intent(context, TimelineAddVisitActivity2.class).putExtra("position", position));
                                    /*} else {
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showDialog(activity2, "Opps", "No Standing Area for New Visit");
                                    }
                                } else {
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showDialog(activity2, "Opps", "No Standing Area for New Visit");
                                }*/
                            }
                        });
                    } else if (timelineDataList.get(position).getIsDone().equals("I") && timelineDataList.get(position).getResultType().equals("inp")) {

                        holder.timeline_add_visit_td_tv.setText(resources.getString(R.string.add_input_cost_label));
                        Log.e("TimelineAdapter", "Add today InputCost");
                        holder.timeline_date_today_visit.setText(doneDateList.get(position));
                        holder.linear_weather_lay.setVisibility(View.GONE);
                        holder.all_activities_layout.setVisibility(View.GONE);
                        holder.all_old_visit_layout.setVisibility(View.GONE);
                        holder.today_visit_layout.setVisibility(View.VISIBLE);
                        holder.harvest_inner_main_lay.setVisibility(View.GONE);
                        holder.germi_inner_main_lay.setVisibility(View.GONE);
                        holder.dot_image_for_visit_td.setImageResource(R.color.colorAccent);

                        holder.linearLayoutTodayVisit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               /* if (!SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).equals("")) {

                                    if (Double.valueOf(SharedPreferencesMethod.getString(context, SharedPreferencesMethod.STANDING_ACRES).trim()) > 0) {*/
                                        context.startActivity(new Intent(context, InputCostActivity.class));
                                  /*  } else {
                                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                                        viewFailDialog.showDialog(activity2, "Opps", "No Standing Area for New Input Cost");
                                    }

                                } else {
                                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                                    viewFailDialog.showDialog(activity2, "Opps", "No Standing Area for New Input Cost");
                                }*/
                            }
                        });
                    } else {
                        holder.timeline_difference_line_weather.getLayoutParams().width = 3;
                        //Weather
                        Log.e("TimelineAdapter", "In Weaather Card");
                        if (weatherForecast != null) {
                            if (isCurrentLocation)
                            holder.weather_desclaimer_msg_tv.setText(resources.getString(R.string.farm_coordinates_not_availabe_showing_weather_data_of_your_current_location));
                            else
                                holder.weather_desclaimer_msg_tv.setText(resources.getString(R.string.showing_weather_data_of_your_farm_location));

                            holder.timeline_center_image_dot_weather.setImageResource(R.color.timeline_vis_done);
                            holder.timeline_difference_line_weather.setBackgroundColor(context.getResources().getColor(R.color.timeline_vis_done));
                            String cityName = "";
                            String stateName = "";
                            String countryName = "";
                            if (weatherForecast.getCityName() != null)
                                cityName = weatherForecast.getCityName();
                            if (weatherForecast.getCityName() != null)
                                stateName = weatherForecast.getStateName();
                            if (weatherForecast.getCityName() != null)
                                countryName = weatherForecast.getCountryName();
                            holder.weatherCityNameTv.setText(cityName + ", " + stateName + ", " + countryName);
                            holder.linear_weather_lay.setVisibility(View.VISIBLE);
                            holder.all_activities_layout.setVisibility(View.GONE);
                            holder.all_old_visit_layout.setVisibility(View.GONE);
                            holder.today_visit_layout.setVisibility(View.GONE);
                            holder.harvest_inner_main_lay.setVisibility(View.GONE);
                            holder.germi_inner_main_lay.setVisibility(View.GONE);
                            String icon = (String.valueOf(weatherForecast.getCurrently().getIcon()));
                            DecimalFormat format = new DecimalFormat("##.##");
                            double precipitation_val = Math.round(weatherForecast.getCurrently().getPrecipIntensity());

                            holder.txt_curr_temp.setText(String.valueOf(Math.round(weatherForecast.getCurrently().getTemperature()) + "\u00B0" + " "));
                            holder.txt_max_temp.setText(String.valueOf(Math.round(weatherForecast.getDaily().getData().get(0).getApparentTemperatureHigh()) + "\u00b0"));
                            holder.txt_min_temp.setText(String.valueOf(Math.round(weatherForecast.getDaily().getData().get(0).getApparentTemperatureLow()) + "\u00b0c"));
                            holder.rainfall.setText(resources.getString(R.string.rainfall_label)+" " + String.valueOf(format.format(precipitation_val)) +" "+ resources.getString(R.string.mm_unit));

                            select_skycon(icon, holder.skycon_view);
                            Log.e("WeatherResp", new Gson().toJson(weatherForecast.getCurrently()));
                            holder.linear_weather_lay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.e("inside_onClick", "done");
                                    Intent intent = new Intent(context, WeatherActivity.class);
                                    intent.putExtra("weatherForecast", (Serializable) weatherForecast);
                                    context.startActivity(intent);


                                }
                            });
                            Log.e("TimelineAdapter", "Weather is not null");
                        } else {
                            holder.linear_weather_lay.setVisibility(View.GONE);
                            holder.all_activities_layout.setVisibility(View.GONE);
                            holder.all_old_visit_layout.setVisibility(View.GONE);
                            holder.today_visit_layout.setVisibility(View.GONE);
                            holder.harvest_inner_main_lay.setVisibility(View.GONE);
                            holder.germi_inner_main_lay.setVisibility(View.GONE);
                            Log.e("TimelineAdapter", "Weather is null");
                        }
                    }
                } else {
                    holder.all_activities_layout.setVisibility(View.GONE);
                    holder.all_old_visit_layout.setVisibility(View.VISIBLE);
                    holder.today_visit_layout.setVisibility(View.GONE);
                    holder.harvest_inner_main_lay.setVisibility(View.GONE);
                    holder.germi_inner_main_lay.setVisibility(View.GONE);
                    /*final float scale = context.getResources().getDisplayMetrics().density;
                    int pixels = (int) (390 * scale + 0.5f);
                    holder.timeline_difference_line.getLayoutParams().height = pixels;*/
                    holder.timeline_detail_visit.setText("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa.");
                    holder.dot_image_for_visit.setImageResource(R.color.timeline_vis_done);
                    holder.timeline_date_visit.setText(doneDateList.get(position));
                    holder.timeline_date_visit.setBackgroundColor(context.getResources().getColor(R.color.timeline_vis_done));
                    holder.more_button_tv_visit.setBackgroundColor(context.getResources().getColor(R.color.timeline_vis_done));
                    holder.difference_line_visit.setBackgroundColor(context.getResources().getColor(R.color.timeline_vis_done));


                    holder.more_button_tv_visit.setText(R.string.view_visit_label);
                    holder.timeline_title_visit.setText(R.string.visited_label);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.VR_ID, timelineDataList.get(position).getVisitReportId());
                            //DataHandler.newInstance().setVisit_report_id(Integer.parseInt(timelineDataList.get(position).getVisitReportId()));
                            FullScreenDialogVisit dialog = new FullScreenDialogVisit();
                            FragmentTransaction ft = activity2.getFragmentManager().beginTransaction();
                            dialog.show(ft, FullScreenDialogVisit.TAG);
                        }
                    });

                }
            }
        } /*else if (timelineDataList.get(position).getResultType().toLowerCase().equals("ge")) {
            //Visible Germination And hide Others
            holder.all_activities_layout.setVisibility(View.GONE);
            holder.all_old_visit_layout.setVisibility(View.GONE);
            holder.today_visit_layout.setVisibility(View.GONE);
            holder.harvest_inner_main_lay.setVisibility(View.GONE);
            holder.germi_inner_main_lay.setVisibility(View.VISIBLE);

        } */ else {
            holder.itemView.setVisibility(View.GONE);
        }

       /* } catch (Exception exception) {
            Log.e("TimelineAdapter", exception.toString());
        }*/

    }

    // holder.timeline_date.setText(doneDateList.get(position));


    @Override
    public int getItemCount() {
        return timelineDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView timeline_date, timeline_activity, /*timeline_activity_desc,*/
                timeline_activity_type, full_details_tv2;
        View timeline_difference_line;
        TextView more_button_tv;
        CircleImageView centerImage, timeline_center_image_dot;
        RelativeLayout parent_rel_layout2;
        LinearLayout contentLinearLayout;
        ImageView timeline_arrow_img;
        CardView activity_card;
        LinearLayout all_activities_layout;
        View view_not_allowed_to_input;

        //Visit Views
        CardView visit_card;
        ImageView timeline_image_visit;
        TextView timeline_date_visit, timeline_title_visit, timeline_detail_visit, more_button_tv_visit;
        ProgressBar image_loading_progress;
        LinearLayout contentLinearLayoutVisit, all_old_visit_layout;
        CircleImageView dot_image_for_visit;
        View difference_line_visit;

        //TodayVisit
        CardView today_visit_card;
        TextView timeline_date_today_visit, timeline_add_visit_td_tv;
        LinearLayout today_visit_layout;
        CircleImageView dot_image_for_visit_td;
        View difference_line_visit_td;
        RelativeLayout linearLayoutTodayVisit;

        //Germination
        LinearLayout germi_inner_main_lay;
        CardView germiCard;
        RelativeLayout germiRelLayout;
        TextView germi_timeline_date;
        TextView germi_timeline_add;

        //HarvestT
        LinearLayout harvest_inner_main_lay;
        CardView harvestCard;
        RelativeLayout harvestRelLayout;
        TextView harvest_timeline_date;
        TextView harvest_timeline_add;

        //Weather
        LinearLayout linear_weather_lay;
        CircleImageView timeline_center_image_dot_weather;
        View timeline_difference_line_weather;
        TextView txt_curr_temp;
        TextView txt_max_temp;
        TextView txt_min_temp;
        TextView rainfall;
        ImageView skycon_view;
        TextView weatherCityNameTv,weather_desclaimer_msg_tv;


        public MyViewHolder(View itemView) {
            super(itemView);
            timeline_activity = itemView.findViewById(R.id.timeline_activity);
            timeline_activity_type = itemView.findViewById(R.id.timeline_activity_type);
            timeline_date = itemView.findViewById(R.id.timeline_date2);
            full_details_tv2 = itemView.findViewById(R.id.full_details_tv2);
            timeline_difference_line = itemView.findViewById(R.id.timeline_difference_line2);
            centerImage = itemView.findViewById(R.id.timeline_center_image);
            timeline_center_image_dot = itemView.findViewById(R.id.timeline_center_image_dot);
            contentLinearLayout = itemView.findViewById(R.id.contentLinearLayout);
            more_button_tv = itemView.findViewById(R.id.more_button_tv);
            timeline_arrow_img = itemView.findViewById(R.id.timeline_arrow_img);
            //timeline_activity_desc = itemView.findViewById(R.id.timeline_activity_desc);
            parent_rel_layout2 = itemView.findViewById(R.id.parent_rel_layout2);
            activity_card = itemView.findViewById(R.id.activity_card);
            all_activities_layout = itemView.findViewById(R.id.image_and_line_layout);

            //Visit views
            visit_card = itemView.findViewById(R.id.visit_card);
            timeline_image_visit = itemView.findViewById(R.id.timeline_image_visit);
            timeline_date_visit = itemView.findViewById(R.id.timeline_date_visit);
            timeline_title_visit = itemView.findViewById(R.id.timeline_title_visit);
            timeline_detail_visit = itemView.findViewById(R.id.timeline_detail_visit);
            more_button_tv_visit = itemView.findViewById(R.id.more_button_tv_visit);
            contentLinearLayoutVisit = itemView.findViewById(R.id.contentLinearLayoutVisit);
            image_loading_progress = itemView.findViewById(R.id.image_loading_progress);
            all_old_visit_layout = itemView.findViewById(R.id.image_and_line_layout_visit);

            dot_image_for_visit = itemView.findViewById(R.id.timeline_center_image_dot_visit);
            difference_line_visit = itemView.findViewById(R.id.timeline_difference_line_visit);
            linearLayoutTodayVisit = itemView.findViewById(R.id.linearLayoutTodayVisit);

            today_visit_card = itemView.findViewById(R.id.today_visit_card);
            timeline_add_visit_td_tv = itemView.findViewById(R.id.timeline_add_visit_td_tv);
            timeline_date_today_visit = itemView.findViewById(R.id.timeline_date_today_visit);
            today_visit_layout = itemView.findViewById(R.id.visit_inner_main_lay);

            dot_image_for_visit_td = itemView.findViewById(R.id.timeline_center_image_dot_visit_td);
            difference_line_visit_td = itemView.findViewById(R.id.timeline_difference_line_visit_td);


            //Germination
            germi_inner_main_lay = itemView.findViewById(R.id.germi_inner_main_lay);
            germiCard = itemView.findViewById(R.id.germiCard);
            germiRelLayout = itemView.findViewById(R.id.relLayoutGermi);
            germi_timeline_date = itemView.findViewById(R.id.timeline_date_germi);
            germi_timeline_add = itemView.findViewById(R.id.timeline_add_germi);

            //HarvestT
            harvest_inner_main_lay = itemView.findViewById(R.id.harvest_inner_main_lay);
            harvestCard = itemView.findViewById(R.id.harvestCard);
            harvestRelLayout = itemView.findViewById(R.id.relLayoutHarvest);
            harvest_timeline_date = itemView.findViewById(R.id.timeline_date_harvest);
            harvest_timeline_add = itemView.findViewById(R.id.timeline_add_harvest);

            //Weather
            linear_weather_lay = itemView.findViewById(R.id.linear_weather_lay);
            timeline_center_image_dot_weather = itemView.findViewById(R.id.timeline_center_image_dot_weather);
            timeline_difference_line_weather = itemView.findViewById(R.id.timeline_difference_line_weather);
            txt_curr_temp = itemView.findViewById(R.id.txt_curr_temp);
            txt_max_temp = itemView.findViewById(R.id.txt_max_temp);
            txt_min_temp = itemView.findViewById(R.id.txt_min_temp);
            rainfall = itemView.findViewById(R.id.rainfall);
            skycon_view = itemView.findViewById(R.id.skycon_view);
            weatherCityNameTv = itemView.findViewById(R.id.weatherCityNameTv);
            view_not_allowed_to_input = itemView.findViewById(R.id.view_not_allowed_to_input);
            weather_desclaimer_msg_tv=itemView.findViewById(R.id.weather_desclaimer_msg_tv);
        }
    }

    public void select_skycon(String icon, ImageView skycon_view) {
        switch (icon) {
            case "clear-day": {

                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_day));
                break;
            }
            case "clear-night": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_night));
                break;
            }
            case "rain": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rainy));
                break;
            }
            case "snow": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_snow));
                break;
            }
            case "sleet": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_sleet));
                break;
            }
            case "wind": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_windy));
                break;
            }
            case "fog": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_fog));
                break;
            }
            case "cloudy": {
                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cloudy));
                break;
            }
            case "partly-cloudy-day": {

                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cloudy_day_2));
                break;
            }
            case "partly-cloudy-night": {

                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cloudy_night_2));
                break;
            }
            case "hail": {

                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_hail));
                break;
            }
            case "thunderstorm": {

                skycon_view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thunder));
                break;
            }
        }
    }

    public void addTimelineWeather(int position, TimelineInnerData innerData, WeatherForecast weatherForecast,boolean isCurrentLocation) {
        lastPosition = lastPosition + 1;
        this.weatherForecast = weatherForecast;
        //timelineDataList.add(innerData);
        //timelineDataList.add(position, innerData);
        this.isCurrentLocation=isCurrentLocation;
        notifyDataSetChanged();
        Log.e("TimelineAdapter", "If Added new Item");
    }


    public int getCountOfDays(String farmDateStr, String todayDateStr) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date createdConvertedDate = null;
        Date expireCovertedDate = null;
        try {
            createdConvertedDate = dateFormat.parse(farmDateStr);
            expireCovertedDate = dateFormat.parse(todayDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar start = new GregorianCalendar();
        start.setTime(createdConvertedDate);

        Calendar end = new GregorianCalendar();
        end.setTime(expireCovertedDate);

        long diff = end.getTimeInMillis() - start.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        Log.e("TimelineAdapter", "Days: " + dayCount);
        return (int) (dayCount);
    }

}
