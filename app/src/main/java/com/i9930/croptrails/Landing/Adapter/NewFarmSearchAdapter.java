package com.i9930.croptrails.Landing.Adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import es.dmoral.toasty.Toasty;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

public class NewFarmSearchAdapter extends RecyclerView.Adapter<NewFarmSearchAdapter.ViewHolder>  {
    private String[] mDataSet;
    int[] colours = {R.color.dashboardcolo1, R.color.dashboardcolor2, R.color.dashboardcolor3, R.color.dashboardcolor4};
    int currentcolor=0;
    Context context;
    List<FetchFarmResult> fetchFarmResults;
    List<FetchFarmResult> fetchFarmResultsFiltered;
    private int lastPosition = -1;
    ProgressBar update_standing_progress;


    FetchFarmResult fetchResult = new FetchFarmResult();

    private FarmDetailAdapterListener farmDetailAdapterListener;
    String from = "";
    Resources resources;

    public NewFarmSearchAdapter(Context context, List<FetchFarmResult> fetchFarmResults/*FarmDetailAdapterListener farmDetailAdapterListener*/) {
        this.context = context;
        this.fetchFarmResults = fetchFarmResults;
        this.fetchFarmResultsFiltered = fetchFarmResults;
        this.farmDetailAdapterListener = farmDetailAdapterListener;

        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();

    }


   

    public NewFarmSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fetch_farm_content, parent, false);
        NewFarmSearchAdapter.ViewHolder vh = new NewFarmSearchAdapter.ViewHolder(v);
        return vh;
    }

    public void onBindViewHolder(final NewFarmSearchAdapter.ViewHolder holder, final int position) {
        FetchFarmResult fetchFarmResult = null;
            fetchFarmResult = fetchFarmResultsFiltered.get(position);
            if (position == 0) {
            }

            if (fetchFarmResult.getMob() != null) {
                if (!fetchFarmResult.getMob().trim().equals("0")) {
                    holder.call_farmer_butt.setVisibility(View.VISIBLE);

                } else {
                    holder.call_farmer_butt.setVisibility(View.GONE);

                }
            } else {
                holder.call_farmer_butt.setVisibility(View.GONE);
            }

            String firstLetter = "C";
            if (fetchFarmResult.getName()!=null&&fetchFarmResult.getName().length()>0)
            firstLetter = fetchFarmResult.getName().substring(0, 1).toUpperCase();

            String cap = fetchFarmResult.getName().substring(1);
            String village = "";
            String mandal_or_tehsil = "";
            String district = "";
            String state = "";
            String country = "";
            String final_address = "-";
            if (fetchFarmResult.getFvillage_or_city() != null) {
                village = fetchFarmResult.getFvillage_or_city();
            } else {
                village = "";
            }
            if (fetchFarmResult.getFmandal_or_tehsil() != null) {
                mandal_or_tehsil = fetchFarmResult.getFmandal_or_tehsil();
            } else {
                mandal_or_tehsil = "";
            }

            if (fetchFarmResult.getFdistrict() != null) {
                district = fetchFarmResult.getFdistrict();
            } else {
                district = "";
            }

            if (fetchFarmResult.getFstate() != null) {
                state = fetchFarmResult.getFstate();
            } else {
                state = "";
            }
            if (fetchFarmResult.getFcountry() != null) {
                country = fetchFarmResult.getFcountry();
            } else {
                country = "";
            }


            final_address = village + " " + mandal_or_tehsil + " " + district + " " + state + " " + country;

            if (!final_address.trim().equals("")) {
                holder.farm_address.setText(final_address);
            } else {
                holder.tv_addres_linear_lay.setVisibility(View.GONE);
            }


            if((fetchFarmResult.getStandingAcres()!=null && Double.parseDouble(fetchFarmResult.getStandingAcres().trim())>0)) {
                holder.exp_area_in_acer.setText(fetchFarmResult.getStandingAcres()+resources.getString(R.string.acre_msg));
                holder.farm_area_label.setText(resources.getString(R.string.standing_area_label)+" : ");
            } else  if (fetchFarmResult.getActualArea() != null && Double.parseDouble(fetchFarmResult.getActualArea().trim()) > 0) {
                holder.exp_area_in_acer.setText(fetchFarmResult.getActualArea()+resources.getString(R.string.acre_msg));
                holder.farm_area_label.setText(resources.getString(R.string.growing_area_msg)+" : ");
            } else {
                if((fetchFarmResult.getExpArea()!=null && Double.parseDouble(fetchFarmResult.getExpArea().trim())>0)) {
                    holder.exp_area_in_acer.setText(fetchFarmResult.getExpArea()+resources.getString(R.string.acre_msg));
                    holder.farm_area_label.setText(resources.getString(R.string.expected_area_label)+" : ");
                }else {
                    holder.farm_area_label.setText(resources.getString(R.string.expected_area_label)+" : ");
                    holder.exp_area_in_acer.setText("0 "+resources.getString(R.string.acre_msg));
                }
            }

            if (fetchFarmResult.getExpHarvestDate() != null) {
                if (!fetchFarmResult.getExpHarvestDate().equals(AppConstants.for_date)) {
                    holder.farm_harvest_date.setText(AppConstants.getShowableDate(fetchFarmResult.getExpHarvestDate(),context));
                } else {
                    holder.farm_harvest_date.setText("-");
                }
            }else{
                holder.farm_harvest_date.setText("-");
            }

            String sourceString = "<b>" + firstLetter + cap + " (" + fetchFarmResult.getLotNo() + ")" + "</b>";
            holder.farm_lot_no.setText(Html.fromHtml(sourceString));
            holder.farmer_name_single.setText(firstLetter);
            int[] androidColors = resources.getIntArray(R.array.androidcolors);
            if(currentcolor<3)
                currentcolor=currentcolor+1;
            else
                currentcolor=0;
            GradientDrawable bgShape = (GradientDrawable)holder.farmer_name_single.getBackground();
            bgShape.setColor(androidColors[currentcolor]);
            holder.farm_farmer_fathers_name.setText(fetchFarmResult.getFatherName());

    }

    @Override
    public void onBindViewHolder(NewFarmSearchAdapter.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public int getItemCount() {
            return fetchFarmResultsFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView farmer_name_single, farm_lot_no, exp_area_in_acer, farmer_name, farm_harvest_date, farm_address, expectedOrActualTV, farm_farmer_fathers_name;
        ImageView call_farmer_butt;
        TextView farm_area_label;
        TextView exp_area_tv, stand_area_tv;
        LinearLayout tv_addres_linear_lay;
        CardView whole_cv_farm_frag;

        public ViewHolder(View v) {
            super(v);
            farmer_name_single = (TextView) v.findViewById(R.id.farmer_single_tv);
            farmer_name = (TextView) v.findViewById(R.id.farmer_name);
            farm_lot_no = (TextView) v.findViewById(R.id.farm_lot_no);
            exp_area_in_acer = (TextView) v.findViewById(R.id.exp_area_in_acer);
            farm_harvest_date = (TextView) v.findViewById(R.id.farm_harvest_date);
            farm_address = (TextView) v.findViewById(R.id.farm_address);
            /*  expectedOrActualTV = v.findViewById(R.id.e5);*/
            farm_farmer_fathers_name = v.findViewById(R.id.farm_farmer_fathers_name);
            call_farmer_butt = v.findViewById(R.id.call_farmer_butt);
            tv_addres_linear_lay = v.findViewById(R.id.tv_addres_linear_lay);
            whole_cv_farm_frag = v.findViewById(R.id.whole_cv_farm_frag);
         
            farm_area_label=v.findViewById(R.id.farm_area_label);

                farmer_name_single.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onOnlineFarmClick(v, getAdapterPosition());

                    }
                });

                farmer_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, getAdapterPosition());
                    }
                });

                farm_lot_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, getAdapterPosition());
                    }
                });

                exp_area_in_acer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, getAdapterPosition());
                    }
                });

                whole_cv_farm_frag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, getAdapterPosition());
                    }
                });


                farm_harvest_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, getAdapterPosition());
                    }
                });

                farm_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, getAdapterPosition());
                    }
                });

                farm_farmer_fathers_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOnlineFarmClick(v, getAdapterPosition());
                    }
                });

                call_farmer_butt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final FetchFarmResult fetchFarmResult = fetchFarmResultsFiltered.get(getAdapterPosition());
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTUAL_AREA, fetchFarmResult.getActualArea().trim());
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, fetchFarmResult.getStandingAcres().trim());
                        SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.EXPECTED_AREA,
                                Float.valueOf(fetchFarmResult.getExpArea()));
                        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, fetchFarmResult.getFarmId());
                        Log.e("TAG", fetchFarmResult.getName());
                        if (fetchFarmResult.getMob() != null&&!fetchResult.getMob().equals("9999999999")) {
                            if (!fetchFarmResult.getMob().equals("")) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + fetchFarmResult.getMob()));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }else {
                                Toasty.info(context, context.getResources().getString(R.string.mobile_number_not_availbale_msg),
                                        Toast.LENGTH_LONG,true).show();
                            }
                        }else {
                            Toasty.info(context, context.getResources().getString(R.string.mobile_number_not_availbale_msg),
                                    Toast.LENGTH_LONG,true).show();
                        }
                    }
                });
            
        }
    }
    public void clear(){
        fetchFarmResults.clear();
        notifyDataSetChanged();
    }



    public interface FarmDetailAdapterListener {
        void OnFarmSelected(FetchFarmResult fetchFarmResult);
    }


    void onOnlineFarmClick(View v, int position) {
        final FetchFarmResult fetchFarmResult = fetchFarmResultsFiltered.get(position);
        if (fetchFarmResult.getActualArea() != null) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.ACTUAL_AREA, fetchFarmResult.getActualArea().trim());
        }
        if (fetchFarmResult.getStandingAcres() != null) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.STANDING_ACRES, fetchFarmResult.getStandingAcres().trim());
        }
        if (fetchFarmResult.getExpArea() != null) {
            SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.EXPECTED_AREA,
                    Float.valueOf(fetchFarmResult.getExpArea()));
        }

        SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, fetchFarmResult.getFarmId());
        Log.e("TAG", fetchFarmResult.getName());
        Log.e("TAG", "farm_id "+fetchFarmResult.getFarmerId());
        Intent intent = new Intent(context, TestActivity.class);
        //Intent intent = new Intent(context, FarmDashboard.class);
        ActivityOptions options = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.getContext().startActivity(intent, options.toBundle());
        } else {
            v.getContext().startActivity(intent);
        }
    }

    void onExpAreaClick(View v, int position) {

    }

}