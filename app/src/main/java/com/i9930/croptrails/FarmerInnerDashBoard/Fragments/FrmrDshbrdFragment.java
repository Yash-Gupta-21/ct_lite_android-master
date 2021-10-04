package com.i9930.croptrails.FarmerInnerDashBoard.Fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import com.i9930.croptrails.FarmDetails.Model.LineChartInfo;
import com.i9930.croptrails.FarmerInnerDashBoard.Models.ActivityStats;
import com.i9930.croptrails.FarmerInnerDashBoard.Models.FarmDetails;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static java.lang.Double.parseDouble;

/*
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FrmrDshbrdFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrmrDshbrdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrmrDshbrdFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    Context context;
    private static final String ARG_DAYS = "ARG_DAYS";
    private static final String ARG_FRMR_DTLS = "ARG_FRMR_DTLS";
    private static final String ARG_FRMR_GRADE = "ARG_FRMR_GRADE";
    private static final String ARG_ACTIVITY_STATS = "ARG_ACTIVITY_STATS";
    private static final String ARG_LATEST_IMAGE = "ARG_LATEST_IMAGE";
    private String TAG = "FrmrDshbrdFragment";
    // TODO: Rename and change types of parameters
    private String getArgDays;
    private FarmDetails getArgFrmrDtls;
    private ActivityStats getActivityStats;
    private List<LineChartInfo> getDateWiseGradeList;
    private String getArgLatestImage;
    LinearLayout.LayoutParams params1;
    ImageView latest_image_iv;
    String latest_image;
    TextView tv_days;
    ArrayList<String> xAxisLabel;
    ArrayList<String> yAxisLabel;
    @BindView(R.id.lineChart)
    LineChart lineChart;
    PieChartView pieChartView, pieChartViewdate;
    TextView completed, pending, datecompleted, datepending;
    TextView seed_issued_on, qty_seed_provided, exp_flwrng_date, act_flwrng_date, act_area, stand_area, farm_name_frmr_dash, address_frmr_dash;
    String area_unit_label = "";
    Resources resources;

    //  private OnFragmentInteractionListener mListener;

    public FrmrDshbrdFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FrmrDshbrdFragment newInstance(String days, FarmDetails farmDetails, List<LineChartInfo> dateWiseGradeList, String latest_image, ActivityStats activityStats) {
        FrmrDshbrdFragment fragment = new FrmrDshbrdFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DAYS, days);
        args.putParcelable(ARG_FRMR_DTLS, farmDetails);
        args.putParcelableArrayList(ARG_FRMR_GRADE, (ArrayList<? extends Parcelable>) dateWiseGradeList);
        args.putString(ARG_LATEST_IMAGE, latest_image);
        args.putParcelable(ARG_ACTIVITY_STATS, activityStats);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getArgDays = getArguments().getString(ARG_DAYS);
            getArgFrmrDtls = (FarmDetails) getArguments().getParcelable(ARG_FRMR_DTLS);
            getDateWiseGradeList = getArguments().getParcelableArrayList(ARG_FRMR_GRADE);
            getArgLatestImage = getArguments().getString(ARG_LATEST_IMAGE);
            getActivityStats = getArguments().getParcelable(ARG_ACTIVITY_STATS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frmr_dshbrd, container, false);
        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();

        tv_days = view.findViewById(R.id.crop_age_in_days);
        lineChart = view.findViewById(R.id.lineChart);
        latest_image_iv = view.findViewById(R.id.latest_image_imgview);
        completed = (TextView) view.findViewById(R.id.completed);
        pending = (TextView) view.findViewById(R.id.pending);
        datecompleted = (TextView) view.findViewById(R.id.datecompleted);
        datepending = (TextView) view.findViewById(R.id.datepending);
        pieChartView = view.findViewById(R.id.chart);
        pieChartViewdate = view.findViewById(R.id.datechart);
        seed_issued_on = view.findViewById(R.id.seed_provided_on_frag_dash);
        qty_seed_provided = view.findViewById(R.id.qty_seed_provided_frag_dash);
        exp_flwrng_date = view.findViewById(R.id.exp_flwring_date_frag_dash);
        act_flwrng_date = view.findViewById(R.id.act_flwerng_date_frag_dash);
        act_area = view.findViewById(R.id.actual_area_frag_dash);
        stand_area = view.findViewById(R.id.standing_area_frag_dash);
        farm_name_frmr_dash = view.findViewById(R.id.farm_name_frmr_dash);
        address_frmr_dash = view.findViewById(R.id.address_frmr_dash);
        area_unit_label = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AREA_UNIT_LABEL);

        if (getArgFrmrDtls.getSeedProvidedOn() != null && TextUtils.isEmpty(getArgFrmrDtls.getSeedProvidedOn())) {
            seed_issued_on.setText(getArgFrmrDtls.getSeedProvidedOn());
        } else {
            seed_issued_on.setText("-");
        }
        if (getArgFrmrDtls.getQtySeedProvided() != null) {
            qty_seed_provided.setText(getArgFrmrDtls.getQtySeedProvided() + " Kg");
        } else {
            qty_seed_provided.setText("0 Kg");
        }
        if (getArgFrmrDtls.getExpFloweringDate() != null && !TextUtils.isEmpty(getArgFrmrDtls.getExpFloweringDate())) {
            exp_flwrng_date.setText(AppConstants.getShowableDate(getArgFrmrDtls.getExpFloweringDate(),context));
        } else {
            exp_flwrng_date.setText("-");
        }
        if (getArgFrmrDtls.getActualFloweringDate() != null && !TextUtils.isEmpty(getArgFrmrDtls.getActualFloweringDate())) {
            act_flwrng_date.setText(AppConstants.getShowableDate(getArgFrmrDtls.getActualFloweringDate(),context));
        } else {
            act_flwrng_date.setText("-");
        }
        if (getArgFrmrDtls.getActualArea() != null && !TextUtils.isEmpty(getArgFrmrDtls.getActualArea())) {
            act_area.setText(convertAreaTo(getArgFrmrDtls.getActualArea()) + " " + area_unit_label);
        } else {
            act_area.setText("0 " + area_unit_label);
        }
        if (getArgFrmrDtls.getStandingArea() != null && !TextUtils.isEmpty(getArgFrmrDtls.getStandingArea())) {
            stand_area.setText(convertAreaTo(getArgFrmrDtls.getStandingArea()) + " " + area_unit_label);
        } else {
            stand_area.setText("0 " + area_unit_label);
        }
        if (getArgFrmrDtls.getFarmName() != null && !TextUtils.isEmpty(getArgFrmrDtls.getFarmName())) {
            farm_name_frmr_dash.setText(getArgFrmrDtls.getFarmName());
        } else {
            farm_name_frmr_dash.setText("-");
        }
        if (getArgFrmrDtls.getDistrict() != null && !TextUtils.isEmpty(getArgFrmrDtls.getDistrict())) {
            address_frmr_dash.setText(getArgFrmrDtls.getDistrict());
        } else {
            address_frmr_dash.setText("-");
        }

        if (getArgDays != null)
            tv_days.setText(getArgDays);
        setLabels();
        setLineChartInfo(getDateWiseGradeList);
        setPieChartData(getActivityStats.getPendingActivity(), getActivityStats.getCompletedActivity(), String.valueOf(getActivityStats.getTotalActivity()), true);
        setPieChartData(getActivityStats.getPendingDateStatus(), getActivityStats.getCompleteDateStatus(), String.valueOf(getActivityStats.getTotalDateStatus()), false);

        if (getArgLatestImage != null) {
            Uri uriLatestImage = Uri.parse(getArgLatestImage);
            //Picasso.with(context).load(uriprofile).into(holder.expense_image);
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.crp_trails_icon_splash)
                    .error(R.drawable.crp_trails_icon_splash);
            Glide.with(getContext()).load(uriLatestImage).apply(options).into(latest_image_iv);
            Log.e(TAG, getArgLatestImage);
        } else {
            Log.e(TAG, "Image url null");
            latest_image_iv.setImageDrawable(getContext().getResources().getDrawable(R.drawable.crp_trails_icon_splash));
        }
        return view;
    }

    private void setLabels() {
        xAxisLabel = new ArrayList<>();
        yAxisLabel = new ArrayList<>();
        params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getXAxis().setDrawGridLines(true);
        lineChart.setNoDataText(getResources().getString(R.string.no_chart_data_msg));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
      /*  if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // mListener = null;
    }

    private void setLineChartInfo(List<LineChartInfo> lineChartInfos) {
        List<Entry> entries = new ArrayList<>();
        entries.clear();
        xAxisLabel.clear();
        xAxisLabel.add(" ");
        float y = 0;
        for (int i = 0; i < lineChartInfos.size(); i++) {
            String date = lineChartInfos.get(i).getVisitDate();

            try {

                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                Date d = input.parse(date);
                SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
                xAxisLabel.add(output.format(d).substring(0, 5));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (lineChartInfos.get(i).getGrade().equals("A")) {
                y = 4;
            } else if (lineChartInfos.get(i).getGrade().equals("B")) {
                y = 3;
            } else if (lineChartInfos.get(i).getGrade().equals("C")) {
                y = 2;
            } else if (lineChartInfos.get(i).getGrade().equals("D")) {
                y = 1;
            }
            entries.add(new Entry(i + 1, y));

        }
        xAxisLabel.add(" ");
        Log.e("xAxisLabel", String.valueOf(xAxisLabel.size()));
        LineDataSet set = new LineDataSet(entries, "Visits");
        set.setValueTextSize(0);
        //set.setDrawFilled(true);
        //set.setCircleColor(Color.parseColor("#ffffff"));
        set.setCircleColor(getResources().getColor(R.color.new_theme_light_leaf));
        //set.setDrawCircles(false);
        // set.setCircleColor(Color.parseColor("#FFA000"));

        set.setDrawCircleHole(false);

        //set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setColor(getResources().getColor(R.color.new_theme_light_leaf));
        LineData data = new LineData(set);
        //data.setBarWidth(0.7f); // set custom bar width
        lineChart.setData(data);
        Description description = new Description();
        description.setText("");
        //lineChart.setFitsSystemWindows(true);
        lineChart.setDescription(description);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setGranularityEnabled(true);
        lineChart.getXAxis().setDrawGridLines(false);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.setTouchEnabled(false);
        lineChart.setHorizontalScrollBarEnabled(true);// make the x-axis fit exactly all bars
        lineChart.invalidate(); // refresh
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(xAxisLabel.size() - 1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextColor(Color.parseColor("#FFA000"));
        if (xAxisLabel != null && xAxisLabel.size() > 0) {
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    Log.e("float Value", String.valueOf(value));

                    return xAxisLabel.get((int) value);


                }
            });
        }
        YAxis leftAxis = lineChart.getAxisLeft();
        // leftAxis.setAxisMaxValue(4f);
        leftAxis.setAxisMinimum(0f);
        //leftAxis.setYOffset(1f);
        leftAxis.setAxisMaximum(4f);
        leftAxis.setTextColor(Color.parseColor("#FFA000"));

        yAxisLabel.clear();
        yAxisLabel.add(" ");

        yAxisLabel.add(resources.getString(R.string.grade_d));
        yAxisLabel.add(resources.getString(R.string.grade_c));
        yAxisLabel.add(resources.getString(R.string.grade_b));
        yAxisLabel.add(resources.getString(R.string.grade_a));
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return yAxisLabel.get((int) value);
            }
        });
        Legend l = lineChart.getLegend();
        l.setTextColor(Color.parseColor("#8b9800"));


    }

    void setPieChartData(String pendingCount, String completedCount, String totalCount, boolean isAct) {


        float fcomplete = Float.parseFloat(completedCount);
        float fpending = Float.parseFloat(pendingCount);
        float ftotal = Float.parseFloat(totalCount);
        DecimalFormat df2 = new DecimalFormat(".##");

        float pcomplete = ((fcomplete / ftotal) * 100);
        float pPending = ((fpending / ftotal) * 100);


        Log.e("server_response", String.valueOf(pcomplete));
        Log.e("server_response", String.valueOf(pPending));
        Log.e("server_response", totalCount);


        List<SliceValue> pieData = new ArrayList<>();

        pieData.add(new SliceValue(fcomplete, Color.parseColor("#66ff33")).setLabel("" + String.valueOf(df2.format(pcomplete)) + "%"));
        pieData.add(new SliceValue(fpending, Color.RED).setLabel("" + String.valueOf(df2.format(pPending)) + "%"));

        PieChartData pieChartData = new PieChartData(pieData);

        pieChartData.setHasLabels(true).setValueLabelTextSize(12);
        pieChartData.setHasCenterCircle(true);
        pieChartData.setValueLabelsTextColor(Color.BLACK);
        if (isAct) {
            completed.setText(completedCount);
            pending.setText(pendingCount);
            pieChartView.setChartRotationEnabled(false);
            pieChartView.setPieChartData(pieChartData);
        } else
            datecompleted.setText(completedCount);
        datepending.setText(pendingCount);
        pieChartViewdate.setChartRotationEnabled(false);
        pieChartViewdate.setPieChartData(pieChartData);
    }

    private String convertAreaTo(String area) {

        return AppConstants.getShowableArea(area);
    }

}
