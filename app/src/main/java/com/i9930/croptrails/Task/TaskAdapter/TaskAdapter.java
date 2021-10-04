package com.i9930.croptrails.Task.TaskAdapter;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineInnerData;
import com.i9930.croptrails.Landing.Fragments.Model.Datum;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.Task.SvTask;
import com.i9930.croptrails.SoilSense.Dashboard.SoilSensDashboardActivity;
import com.i9930.croptrails.Task.Model.Farm.SvFarm;
import com.i9930.croptrails.Task.Model.TaskDatum;
import com.i9930.croptrails.Test.FarmDetailsVettingActivity;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Test.adapter.TimelineAdapterTest;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<SvTask> objectList;
    Date todayDate;
    int lastPostion = 0;
    Gson gson;
    boolean isOnlySoilSens = false;

    public interface ItemClickListener {
        public void onItemClicked(int index, SvTask taskDatum);
    }

    ItemClickListener itemClickListener;

    public TaskAdapter(Context context, List<SvTask> objectList, ItemClickListener itemClickListener) {
        gson = new Gson();
        isOnlySoilSens = SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.IS_ONLY_SOIL_SENS);
        this.itemClickListener = itemClickListener;
        this.context = context;
        this.objectList = objectList;
        todayDate = Calendar.getInstance().getTime();
        if (objectList != null)
            lastPostion = objectList.size() - 1;
    }

    @Override
    public int getItemViewType(int position) {
        return objectList.get(position).getType();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);


        switch (viewType) {
            case AppConstants.SV_TIMELINE_CARD_TYPES.TASK:
                View v1 = inflater.inflate(R.layout.timeline_rv_layout_act, parent, false);
                viewHolder = new ActivityHolder(v1);
                break;
            case AppConstants.SV_TIMELINE_CARD_TYPES.EXPENSE:
                View v2 = inflater.inflate(R.layout.rv_layout_sv_timeline_expense, parent, false);
                viewHolder = new ExpenseHolder(v2);
                break;

            case AppConstants.SV_TIMELINE_CARD_TYPES.FARM:
                View v10 = inflater.inflate(R.layout.timeline_rv_layout_farm_details, parent, false);
                viewHolder = new FarmHolder(v10);
                break;
            default:
                View v8 = inflater.inflate(R.layout.timeline_rv_layout_act, parent, false);
                viewHolder = new ActivityHolder(v8);
                break;
        }
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.e("onBindViewHolder", "type " + holder.getItemViewType());
        if (holder.getItemViewType() == AppConstants.SV_TIMELINE_CARD_TYPES.TASK) {
            ActivityHolder vh1 = (ActivityHolder) holder;
            configureAct(vh1, position);
        } else if (holder.getItemViewType() == AppConstants.SV_TIMELINE_CARD_TYPES.FARM) {
            FarmHolder vh1 = (FarmHolder) holder;
            configureFarm(vh1, position);
        } else if (holder.getItemViewType() == AppConstants.SV_TIMELINE_CARD_TYPES.EXPENSE) {
            ExpenseHolder vh1 = (ExpenseHolder) holder;
            configureExpense(vh1, position);
        }
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }


    public class ActivityHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView, timelineDotImage;
        TextView moreButtonTv, timelineActDate, timelineActHead, timelineActivityType, timelineActDetails;
        View view_not_allowed_to_input, timelineLine;
        CardView activity_card;

        public ActivityHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.timeline_center_image);
            moreButtonTv = itemView.findViewById(R.id.more_button_tv);
            view_not_allowed_to_input = itemView.findViewById(R.id.view_not_allowed_to_input);
            timelineActDate = itemView.findViewById(R.id.timelineActDate);
            timelineActHead = itemView.findViewById(R.id.timelineActHead);
            timelineActivityType = itemView.findViewById(R.id.timeline_activity_type);
            timelineActDetails = itemView.findViewById(R.id.full_details_tv2);
            timelineLine = itemView.findViewById(R.id.timelineLine);
            timelineDotImage = itemView.findViewById(R.id.timelineDotImage);
            activity_card = itemView.findViewById(R.id.activity_card);

        }
    }

    public class ExpenseHolder extends RecyclerView.ViewHolder {
        TextView visitDateTv, visitHead, reasonTv, amount, comment;
        ProgressBar imageProgress;
        ImageView visitImage;
        CircleImageView timelineDotImage;
        View timelineLine;
        CardView visit_card;

        public ExpenseHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);
            amount = itemView.findViewById(R.id.amount);
            visit_card = itemView.findViewById(R.id.visit_card);
            visitDateTv = itemView.findViewById(R.id.timeline_date_visit);
            reasonTv = itemView.findViewById(R.id.reasonTv);
            imageProgress = itemView.findViewById(R.id.image_loading_progress);
            visitImage = itemView.findViewById(R.id.timeline_image_visit);
            visitHead = itemView.findViewById(R.id.timeline_title_visit);
            timelineDotImage = itemView.findViewById(R.id.timelineDotImage);
            timelineLine = itemView.findViewById(R.id.timelineLine);
        }
    }

    private class FarmHolder extends RecyclerView.ViewHolder {
        CircleImageView timelineDotImage;
        ImageView timelineCenterImageFarm;
        View timelineLine;
        TextView timelineDoaFarm, timelineFarmHead, diapatchAreaLabel,
                timelineDisaptchArea, sowingAreaLabel, timelineSowingArea, standingAreaLabel,
                timelineStandingArea, moreFarmTv, areaUnitTv, areaUnitLabel;
        CardView timelineFarmDetailsCard;

        public FarmHolder(@NonNull View itemView) {
            super(itemView);

            timelineDotImage = itemView.findViewById(R.id.timelineDotImage);
            timelineLine = itemView.findViewById(R.id.timelineLine);
            timelineFarmDetailsCard = itemView.findViewById(R.id.timelineFarmDetailsCard);
            timelineDoaFarm = itemView.findViewById(R.id.timelineDoaFarm);
            timelineFarmHead = itemView.findViewById(R.id.timelineFarmHead);

            diapatchAreaLabel = itemView.findViewById(R.id.diapatchAreaLabel);
            timelineDisaptchArea = itemView.findViewById(R.id.timelineDisaptchArea);

            sowingAreaLabel = itemView.findViewById(R.id.sowingAreaLabel);
            timelineSowingArea = itemView.findViewById(R.id.timelineSowingArea);

            standingAreaLabel = itemView.findViewById(R.id.standingAreaLabel);
            timelineStandingArea = itemView.findViewById(R.id.timelineStandingArea);

            areaUnitTv = itemView.findViewById(R.id.areaUnitTv);
            areaUnitLabel = itemView.findViewById(R.id.areaUnitLabel);
            timelineCenterImageFarm = itemView.findViewById(R.id.timelineCenterImageFarm);
            moreFarmTv = itemView.findViewById(R.id.moreFarmTv);

        }
    }

    private void configureAct(ActivityHolder holder, int position) {

        SvTask cacheData = objectList.get(position);
        Log.e("onBindViewHolder", "data " + gson.toJson(cacheData));
        if (cacheData.getType() == AppConstants.SV_TIMELINE_CARD_TYPES.TASK) {
            TaskDatum innerData = null;
            try {
                innerData = gson.fromJson(cacheData.getData(), TaskDatum.class);
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (innerData != null) {
                Date farmDate = null;
                if (innerData.getAssignDate() != null) {
                    holder.timelineActDate.setText(getDate(innerData.getAssignDate()));
                    SimpleDateFormat formatDate = new SimpleDateFormat(AppConstants.DATE_FORMAT_SERVER);
                    try {

                        farmDate = formatDate.parse(innerData.getAssignDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    holder.timelineActDate.setVisibility(View.INVISIBLE);
                }
                holder.timelineActHead.setText(innerData.getTask());
                holder.timelineActivityType.setText(innerData.getInstructions());

                holder.circleImageView.setVisibility(View.GONE);
                String text;
                if (innerData.getComments() != null) {
                    text = context.getResources().getString(R.string.comment_label) + ": " + innerData.getComments();
                    SpannableStringBuilder ssBuilder = new SpannableStringBuilder(text);
                    ssBuilder.setSpan(
                            new StyleSpan(Typeface.BOLD), // span to add
                            text.indexOf(context.getResources().getString(R.string.comment_label) + ":"), // start of the span (inclusive)
                            text.indexOf(context.getResources().getString(R.string.comment_label) + ":") + String.valueOf(context.getResources().getString(R.string.comment_label) + ":").length(), // end of the span (exclusive)
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // do not extend the span when text add later
                    );
                    holder.timelineActDetails.setText(ssBuilder);
                } else {
                    holder.timelineActDetails.setVisibility(View.GONE);
                }

                if (todayDate == null || farmDate == null) {
                    holder.activity_card.setAlpha(1);
                    holder.timelineDotImage.setImageResource(R.color.orange);
                    holder.timelineActDate.setBackgroundColor(context.getResources().getColor(R.color.orange));
                    holder.moreButtonTv.setBackgroundColor(context.getResources().getColor(R.color.orange));
                    holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.orange));
                    holder.timelineLine.setBackgroundColor(context.getResources().getColor(R.color.orange));
                    holder.view_not_allowed_to_input.setVisibility(View.GONE);
                    holder.moreButtonTv.setText(R.string.upcomming_activity_label);
                    holder.moreButtonTv.setVisibility(View.INVISIBLE);

                } else if (todayDate != null && farmDate != null && todayDate.equals(farmDate)) {

                    if (innerData.getIsComplete().equalsIgnoreCase("Y")) {
                        holder.timelineDotImage.setImageResource(R.color.timeline_act_done);

                        holder.timelineActDate.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                        holder.moreButtonTv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                        holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.timeline_act_done));
                        holder.timelineLine.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                        holder.moreButtonTv.setText(context.getResources().getString(R.string.view_more_label));
                        holder.activity_card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });

                    } else {
                        holder.timelineDotImage.setImageResource(R.color.new_theme_light);
                        holder.timelineActDate.setBackgroundColor(context.getResources().getColor(R.color.new_theme_light));
                        holder.moreButtonTv.setBackgroundColor(context.getResources().getColor(R.color.new_theme_light));
                        holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.new_theme_light));
                        holder.timelineLine.setBackgroundColor(context.getResources().getColor(R.color.new_theme_light));
                        holder.moreButtonTv.setText("Today's task is pending");

                    }
                } else if (todayDate != null && farmDate != null && todayDate.after(farmDate)) {
                    if (innerData.getIsComplete() != null && innerData.getIsComplete().equals("Y")) {
                        holder.activity_card.setAlpha(1);
                        holder.timelineDotImage.setImageResource(R.color.timeline_act_done);
                        holder.timelineActDate.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                        holder.moreButtonTv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                        holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.timeline_act_done));
                        holder.timelineLine.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_done));
                        holder.moreButtonTv.setText("View Task");
                        holder.view_not_allowed_to_input.setVisibility(View.GONE);


                    } else {

                        holder.timelineDotImage.setImageResource(R.color.timeline_act_pending);

                        holder.timelineActDate.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_pending));
                        holder.moreButtonTv.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_pending));
                        holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.timeline_act_pending));
                        holder.timelineLine.setBackgroundColor(context.getResources().getColor(R.color.timeline_act_pending));
                        holder.moreButtonTv.setText(context.getResources().getString(R.string.pending_label));


                    }
                } else {
                    holder.activity_card.setAlpha(1);
                    holder.timelineDotImage.setImageResource(R.color.yellow);
                    holder.timelineActDate.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                    holder.moreButtonTv.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                    holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.yellow));
                    holder.timelineLine.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                    holder.view_not_allowed_to_input.setVisibility(View.GONE);
                    holder.moreButtonTv.setText("Upcoming task");
                    holder.moreButtonTv.setVisibility(View.INVISIBLE);
                }

                if (position == lastPostion) {
                    holder.timelineLine.setVisibility(View.GONE);
                } else {
                    holder.timelineLine.setVisibility(View.VISIBLE);
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClicked(position, cacheData);
                    }
                });
            }
        }
    }

    private void configureFarm(FarmHolder holder, int position) {

        SvTask cacheData = objectList.get(position);
        Log.e("onBindViewHolder", "data " + gson.toJson(cacheData));
        if (cacheData.getType() == AppConstants.SV_TIMELINE_CARD_TYPES.FARM) {
            SvFarm innerData = null;
            try {
                innerData = gson.fromJson(cacheData.getData(), SvFarm.class);
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (innerData != null) {

                if (innerData.getDoa() != null) {
                    holder.timelineDoaFarm.setText(getDate(innerData.getDoa()));
                    holder.timelineDoaFarm.setVisibility(View.VISIBLE);

                } else {
                    holder.timelineDoaFarm.setVisibility(View.INVISIBLE);
                }
                holder.timelineFarmHead.setText(context.getResources().getString(R.string.farm));

                holder.diapatchAreaLabel.setText(context.getResources().getString(R.string.lot_no));
                holder.timelineDisaptchArea.setText(innerData.getCompFarmId());

                holder.sowingAreaLabel.setText(context.getResources().getString(R.string.farmer_name_hint));
                holder.timelineSowingArea.setText(innerData.getFarmerName());

                holder.standingAreaLabel.setText(context.getResources().getString(R.string.mobile));
                holder.timelineStandingArea.setText(innerData.getMob());


            }
            final String vetting = innerData.getIsSelected();
            if (AppConstants.isValidString(vetting)) {
                SvFarm finalInnerData = innerData;
                holder.moreFarmTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.FARM_VETTING, vetting);
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, ""+ finalInnerData.getFarmId());
                        Intent intent = null;
                        if (vetting != null && (vetting.trim().equals(AppConstants.VETTING.SELECTED) ||
                                vetting.trim().equals(AppConstants.VETTING.DELINQUENT))) {
                            if (isOnlySoilSens) {
                                intent = new Intent(context, SoilSensDashboardActivity.class);
                            } else
                                intent = new Intent(context, TestActivity.class);

                        } else if (isOnlySoilSens) {
                            intent = new Intent(context, SoilSensDashboardActivity.class);

                        } else {
                            intent = new Intent(context, FarmDetailsVettingActivity.class);

                        }

                        context.startActivity(intent);
                    }
                });
            }
        }


        if(position ==lastPostion)

    {
        holder.timelineLine.setVisibility(View.GONE);
    } else

    {
        holder.timelineLine.setVisibility(View.VISIBLE);
    }

}

    private void configureExpense(ExpenseHolder holder, int position) {

        SvTask cacheData = objectList.get(position);
        Log.e("onBindViewHolder", "data " + gson.toJson(cacheData));
        if (cacheData.getType() == AppConstants.SV_TIMELINE_CARD_TYPES.EXPENSE) {
            Datum innerData = null;
            try {
                innerData = gson.fromJson(cacheData.getData(), Datum.class);
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (innerData != null) {

                if (innerData.getExpDate() != null) {
                    holder.visitDateTv.setText(getDate(innerData.getExpDate()));

                } else {

                }
                holder.amount.setText(innerData.getAmount());
                holder.comment.setText(innerData.getComment());

                Glide.with(context)
                        .load(innerData.getImgUrl())
                        .placeholder(R.drawable.ploughed_farm)
                        .error(R.drawable.ploughed_farm)
                        .into(holder.visitImage);

            }
        }

        if (position == lastPostion) {
            holder.timelineLine.setVisibility(View.GONE);
        } else {
            holder.timelineLine.setVisibility(View.VISIBLE);
        }
    }

    private String getDate(String dateStr) {
        try {
            DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateView = srcDf.parse(dateStr);
            DateFormat srcDfView = new SimpleDateFormat("dd MMM");
            dateStr = srcDfView.format(dateView);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public void markComplete(int index, String date, String comment) {
        if (index >= 0 && index <= lastPostion)
            try {
                SvTask svTask = objectList.get(index);
                TaskDatum taskDatum = new Gson().fromJson(svTask.getData(), TaskDatum.class);
                taskDatum.setIsComplete("Y");
                taskDatum.setCompleteDate(AppConstants.getCurrentDate());
                taskDatum.setComments(comment);
                svTask.setData(new Gson().toJson(taskDatum));
                svTask.setIsComplete("Y");
                svTask.setCompleteDate(date);
                svTask.setComments(comment);

                objectList.set(index, svTask);
                Log.d("markComplete", new Gson().toJson(objectList.get(index)));
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }


}
