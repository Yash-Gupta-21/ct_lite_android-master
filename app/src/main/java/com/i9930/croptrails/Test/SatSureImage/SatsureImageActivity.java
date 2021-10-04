package com.i9930.croptrails.Test.SatSureImage;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.tooltip.Tooltip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.ImagePager.CustomViewPager;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Test.SatSureImage.Utils.CustomMarkerView;
import com.i9930.croptrails.Test.SatusreModel.SatsureData;
import com.i9930.croptrails.Test.SatusreModel.SatsureResponse;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

public class SatsureImageActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    String TAG = "SatsureImageActivity";
    ViewPagerAdapter adapter;
    private int page = 0;
    List<Bitmap> imageBitmapList;
    List<SatsureData> satsureDataListNdvi = new ArrayList<>();
    List<SatsureData> satsureDataListNdwi = new ArrayList<>();

    private static interface MapConstants {
        int SIMPLE_MODE = -1;
        int NDVI_MODE = 0;
        int NDWI_MODE = 1;
    }

    private int selectedMode = -1;
    private Handler handler_slider;
    private final int delay = 3000;
    CustomViewPager viewPager;
    SatsureImageActivity context;
    LinearLayout ll_dots;
    Resources resources;
    Toolbar mActionBarToolbar;
    Runnable runnable = new Runnable() {
        public void run() {
            if (adapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            viewPager.setCurrentItem(page, true);
            handler_slider.postDelayed(this, delay);
        }
    };
    private TextView[] dots;

    private void initViews() {
        tittle = (TextView) findViewById(R.id.tittle);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        viewPager = findViewById(R.id.vp_slider);
        ll_dots = findViewById(R.id.ll_dots);
    }

    TextView tittle;
    private int sliderIndex = 0;
    @BindView(R.id.imageRelLayout)
    RelativeLayout imageRelLayout;
    @BindView(R.id.selectTypeImage)
    ImageView selectTypeImage;

    @BindView(R.id.satsureChart)
    LineChart lineChart;

    ArrayList<String> xAxisLabel = new ArrayList<>();
    ArrayList<String> yAxisLabel = new ArrayList<>();

    @BindView(R.id.noDataAvailableLayout)
    RelativeLayout noDataAvailableLayout;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.progressBarSat)
    ProgressBar progressBarSat;
    // TooltipWindow tipWindow;
    String productId;


    //legend color views
    @BindView(R.id.tv0To100)
    TextView tv0To100;
    @BindView(R.id.tv100To110)
    TextView tv100To110;
    @BindView(R.id.tv110To120)
    TextView tv110To120;
    @BindView(R.id.tv120To130)
    TextView tv120To130;
    @BindView(R.id.tv130To140)
    TextView tv130To140;
    @BindView(R.id.tv140To150)
    TextView tv140To150;
    @BindView(R.id.tv150To160)
    TextView tv150To160;
    @BindView(R.id.tv160To170)
    TextView tv160To170;
    @BindView(R.id.tv170To180)
    TextView tv170To180;
    @BindView(R.id.tv180To190)
    TextView tv180To190;
    @BindView(R.id.tv190To200)
    TextView tv190To200;

    @BindView(R.id.messageTv)
    TextView messageTv;

    //Color code for marker
    public static String to100Color = "#000000";
    public static String to110Color = "#6e0000";
    public static String to120Color = "#8c4646";
    public static String to130Color = "#ff4600";
    public static String to140Color = "#ff8c00";
    public static String to150Color = "#ffff14";
    public static String to160Color = "#00FFFF";
    public static String to170Color = "#0099FF";
    public static String to180Color = "#0044FF";
    public static String to190Color = "#0000FF";
    public static String to200Color = "#0000FF";

    Map<Integer, String> productMap = new HashMap<>();

    private interface SATSURE_NDVI {
        public static String to100Color = "Unable to fetch data because of bad weather conditions.";
        public static String to110Color = "Unable to fetch data because of bad weather conditions.";
        public static String to120Color = "Poor Vegetation, dead crops present!";
        public static String to130Color = "Poor Vegetation, dead crops present!";
        public static String to140Color = "Poor Vegetation, Crops in early vegetation stage!";
        public static String to150Color = "Poor Vegetation, Crops in early vegetation stage!";
        public static String to160Color = "Average vegetation, Vegetation cycle started!";
        public static String to170Color = "Average vegetation, Vegetation cycle started!";
        public static String to180Color = "Good vegetation, crops in great condition!";
        public static String to190Color = " Good vegetation, crops in great condition!";
        public static String to200Color = "Excellent vegetation, Crops in great condition!";
    }

    private interface SATSURE_NDWI {
        public static String to100Color = "Unable to fetch data because of bad weather conditions.";
        public static String to110Color = "Unable to fetch data because of bad weather conditions.";
        public static String to120Color = " Poor Watering, Dry crops present!";
        public static String to130Color = "Poor Watering, Dry crops present!";
        public static String to140Color = "Poor Watering, Crops need more moisture!";
        public static String to150Color = "Poor Watering, Crops need more moisture!";
        public static String to160Color = " Average Watering, Crops need more moisture!";
        public static String to170Color = "Average Watering, Crops need more moisture!";
        public static String to180Color = "Good Watering, Crops in great condition!";
        public static String to190Color = "Good Watering, Crops in great condition!";
        public static String to200Color = " Excellent Watering, Crops in great condition!";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satsure_image);
        context = this;
        ButterKnife.bind(this);
        //tipWindow = new TooltipWindow(context);
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        initViews();
        tittle.setText(resources.getString(R.string.image_activity_title));
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SatsureImageActivity.super.onBackPressed();
            }
        });
        initVariables();
        String name = "SatSure Analysis";

        if (getIntent().getStringExtra("productName") != null && !TextUtils.isEmpty(getIntent().getStringExtra("productName")))
            name = getChars(getIntent().getStringExtra("productName"));
        sliderIndex = getIntent().getIntExtra("index", 0);
        productId = getIntent().getStringExtra("typeId");
        getData(productId, name);
      /*  if (DataHandler.newInstance().getSatsureDataList() != null) {
            for (int i = 0; i < DataHandler.newInstance().getSatsureDataList().size(); i++) {
                if (DataHandler.newInstance().getSatsureDataList().get(i).getProductId() != null &&
                        DataHandler.newInstance().getSatsureDataList().get(i).getProductId().trim().equals("3")) {
                    satsureDataListNdvi.add(DataHandler.newInstance().getSatsureDataList().get(i));
                } else if (DataHandler.newInstance().getSatsureDataList().get(i).getProductId() != null &&
                        DataHandler.newInstance().getSatsureDataList().get(i).getProductId().trim().equals("4")) {
                    satsureDataListNdwi.add(DataHandler.newInstance().getSatsureDataList().get(i));
                }
            }

            if (getIntent().getStringExtra("typeId") != null && getIntent().getStringExtra("typeId").trim().equals("3")) {
                onlineModeEvent(satsureDataListNdvi);
                selectTypeImage.setImageResource(R.drawable.ndvi_img);
                tittle.setText("NDVI");
                selectedMode = MapConstants.NDVI_MODE;
                initializeChart2(satsureDataListNdvi);
            } else {
                onlineModeEvent(satsureDataListNdwi);
                selectTypeImage.setImageResource(R.drawable.ndwi_img);
                tittle.setText("NDWI");
                selectedMode = MapConstants.NDWI_MODE;
                setNdwiChart(satsureDataListNdwi);
            }
        } else {
            finish();
            Toasty.error(context, "No data available", Toast.LENGTH_LONG, false).show();
        }*/

        imageRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (tipWindow != null && tipWindow.isTooltipShown())
                    tipWindow.dismissTooltip();
                else
                    tipWindow.showToolTip(imageRelLayout);*/
                showDialog();
            }
        });


        //Chart
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getXAxis().setDrawGridLines(true);
        lineChart.setNoDataText(resources.getString(R.string.no_chart_data_msg));
        lineChart.setOnChartValueSelectedListener(this);
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(false);

        CustomMarkerView customMarkerView = new CustomMarkerView(this, R.layout.chart_marker_view);
        customMarkerView.setChartView(lineChart);
        lineChart.setDrawMarkers(true);
        lineChart.setMarkerView(customMarkerView);
        lineChart.setDrawMarkerViews(true);
    }

    private void initVariables() {
        imageBitmapList = new ArrayList<>();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        // viewPager.getLayoutParams().height = (height * 4) / 5;
    }

    private void onlineModeEvent(List<SatsureData> satsureDataList) {

        adapter = new ViewPagerAdapter(satsureDataList, "Heading");
        viewPager.setAdapter(adapter);

        if (satsureDataList != null && satsureDataList.size() > 0) {
            addBottomDots(0, satsureDataList);
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                page = position;

               /* if (selectedMode == MapConstants.NDVI_MODE) {
                    lineChart.highlightValue(ndviEntries.get(page).getX(), 0);
                } else if (selectedMode == MapConstants.NDWI_MODE) {
                    lineChart.highlightValue(ndwiEntries.get(page).getX(), 0);
                }*/
                try {
                    chengeMessageText(ndwiEntries.get(page));
                    lineChart.highlightValue(ndwiEntries.get(page).getX(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position, satsureDataList);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        try {
            viewPager.setCurrentItem(sliderIndex, true);
            chengeMessageText(ndwiEntries.get(sliderIndex));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addBottomDots(int currentPage, List<SatsureData> satsureDataList) {
        ll_dots.removeAllViews();
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            dots = new TextView[satsureDataList.size()];

            // ll_dots.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(this);
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(25);
                dots[i].setTextColor(getResources().getColor(R.color.yellow4));
                ll_dots.addView(dots[i]);
            }

            if (dots.length > 0)
                dots[currentPage].setTextColor(getResources().getColor(R.color.new_theme));
        } else {
            dots = new TextView[imageBitmapList.size()];

            //ll_dots.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(this);
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(25);
                dots[i].setTextColor(getResources().getColor(R.color.yellow4));
                ll_dots.addView(dots[i]);
            }

            if (dots.length > 0)
                dots[currentPage].setTextColor(getResources().getColor(R.color.new_theme));
        }
    }


    private void finishAllAndStartLanding() {
        Intent intent = new Intent(context, LandingActivity.class);
        ActivityOptions options = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
            startActivity(intent, options.toBundle());
            finish();
        } else {
            finishAffinity();
            startActivity(intent);
            finish();
        }
    }


    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        List<SatsureData> satsureDataList;


        public ViewPagerAdapter(List<SatsureData> satsureDataList, String headinf) {
            this.satsureDataList = satsureDataList;
        }


        @Override
        public View instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.layout_slider_satsure, container, false);
            ImageView im_slider = (ImageView) view.findViewById(R.id.im_slider);
            PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
            TextView dateTv = view.findViewById(R.id.dateTv);
            ProgressBar progressBar = view.findViewById(R.id.progressBar);

            ViewGroup.MarginLayoutParams imageViewParams = new ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            if (satsureDataList.get(position).getDate() != null &&
                    !TextUtils.isEmpty(satsureDataList.get(position).getDate())) {
                dateTv.setVisibility(View.VISIBLE);
                dateTv.setText(AppConstants.getShowableDate(satsureDataList.get(position).getDate(),context));

            } else
                dateTv.setVisibility(View.GONE);
/*
            im_slider.setLayoutParams(imageViewParams);
            photoView.setLayoutParams(imageViewParams);*/

            if (satsureDataList.get(position).getImageLink() != null) {
            /*    if (satsureDataList.get(position).getImageLink() != null && satsureDataList.get(position).getImageLink().contains("croptrailsimages.s3")) {
                    ShowAwsImage.getInstance(context).downloadFile(Uri.parse(satsureDataList.get(position).getImageLink()), photoView, satsureDataList.get(position).getImageLink());
                } else {*/
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.crp_trails_icon_splash)
                            .error(R.drawable.crp_trails_icon_splash);
                    Glide.with(context)
                            .load(satsureDataList.get(position).getImageLink()).apply(options)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(photoView);
               // }

                photoView.setMaximumScale(5.0F);
                photoView.setMediumScale(3.0F);
            } else {
                im_slider.setImageDrawable(context.getResources().getDrawable(R.drawable.crp_trails_icon_splash));
            }
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return satsureDataList.size();
        }


        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_map_option);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        LinearLayout areaLayout, ndviLayout, ndwiLayout;
        areaLayout = dialog.findViewById(R.id.areaLayout);
        ndviLayout = dialog.findViewById(R.id.ndviLayout);
        ndwiLayout = dialog.findViewById(R.id.ndwiLayout);

        TextView ndviTv = dialog.findViewById(R.id.ndviTv);
        TextView ndwiTv = dialog.findViewById(R.id.ndwiTv);


        try {
            if (selectedMode == MapConstants.NDVI_MODE) {
                ndviTv.setTextColor(getResources().getColor(R.color.darkblue));
                ndwiTv.setTextColor(getResources().getColor(R.color.grey));
            } else if (selectedMode == MapConstants.NDWI_MODE) {
                ndviTv.setTextColor(getResources().getColor(R.color.grey));
                ndwiTv.setTextColor(getResources().getColor(R.color.darkblue));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ndwiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (satsureDataListNdwi != null && satsureDataListNdwi.size() > 0) {
                    //selectedMode= MapConstants.NDWI_MODE;
                    updateSliderData(MapConstants.NDWI_MODE);
                    setNdwiChart(satsureDataListNdvi, "NDWI");
                } else
                    Toasty.error(context, resources.getString(R.string.no_data_available_msg), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });


        ndviLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (satsureDataListNdvi != null && satsureDataListNdvi.size() > 0) {
                    //selectedMode= MapConstants.NDVI_MODE;
                    updateSliderData(MapConstants.NDVI_MODE);
                    initializeChart2(satsureDataListNdvi);
                } else
                    Toasty.error(context, resources.getString(R.string.no_data_available_msg), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void updateSliderData(int selected) {
        if (selected == MapConstants.NDVI_MODE) {
            sliderIndex = 0;
            onlineModeEvent(satsureDataListNdvi);
            selectTypeImage.setImageResource(R.drawable.ndvi_img);
            tittle.setText("NDVI");
            selectedMode = MapConstants.NDVI_MODE;
        } else if (selected == MapConstants.NDWI_MODE) {
            sliderIndex = 0;
            onlineModeEvent(satsureDataListNdwi);
            selectTypeImage.setImageResource(R.drawable.ndwi_img);
            tittle.setText("NDWI");
            selectedMode = MapConstants.NDWI_MODE;
        }

    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i(TAG, "Entry selected " + e.toString());
        // lineChart.highlightValue(e.getX(), 2);

        /*lineChart.centerViewToAnimated(e.getX(), e.getY(), lineChart.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency(), 500);
        viewPager.setCurrentItem((int)e.getX());
*/
    }

    @Override
    public void onNothingSelected() {

    }

    ArrayList<Entry> ndviEntries = new ArrayList<>();
    ArrayList<Entry> ndwiEntries = new ArrayList<>();

    private void initializeChart2(List<SatsureData> satsureDataListNdvi) {
        xAxisLabel.clear();
        lineChart.invalidate();
        ndviEntries.clear();
        ndwiEntries.clear();

        for (int i = 0; i < satsureDataListNdvi.size(); i++) {
            float y2 = 0;
            String date = satsureDataListNdvi.get(i).getDate();
            try {
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                Date d = input.parse(date);
                SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
                xAxisLabel.add(output.format(d).substring(0, 5));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String s2 = satsureDataListNdvi.get(i).getValue();
            y2 = Float.valueOf(s2);
            ndviEntries.add(new Entry(i + 1, y2));
        }


        LineDataSet ndviSet = new LineDataSet(ndviEntries, "NDVI");
        ndviSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        ndviSet.setLineWidth(1f);

        ndviSet.setCircleColor(resources.getColor(R.color.green));
        ndviSet.setDrawCircleHole(false);
        ndviSet.setColor(resources.getColor(R.color.green));
        ndviSet.setValueTextColor(resources.getColor(R.color.green));

        yAxisLabel.clear();
        for (int i = 0; i < 12; i++) {
            int value = 10 * i;
            yAxisLabel.add(" " + value);
        }

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);
        // set an alternative background color
        lineChart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData(ndviSet);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        // set data
        lineChart.setData(data);

        lineChart.animateX(1500);
        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        //X Axis
        XAxis xAxis = lineChart.getXAxis();
        //xAxis.setTextSize(11f);
        xAxis.setGranularity(1f);
        xAxis.setXOffset(1f);
        xAxis.setSpaceMin(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisLineWidth(1f);
        xAxis.setAxisMaximum(xAxisLabel.size());
        xAxis.setAxisMinimum(0f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setSpaceMax(1f);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        //Y Axis
        lineChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setTextColor(Color.BLACK);
       /* YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaximum(900);
        rightAxis.setAxisMinimum(-200);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);*/
        lineChart.highlightValue(ndviEntries.get(2).getX(), 0); //


    }


    private void setNdwiChart(List<SatsureData> satsureDataListNdwi, String title) {
        lineChart.invalidate();
        xAxisLabel.clear();
        ndviEntries.clear();
        ndwiEntries.clear();

        for (int i = 0; i < satsureDataListNdwi.size(); i++) {
            float y2 = 0;
            try {
                String date = satsureDataListNdwi.get(i).getDate();
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                Date d = input.parse(date);
                SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
                xAxisLabel.add(output.format(d).substring(0, 5));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String s2 = satsureDataListNdwi.get(i).getValue();
            y2 = Float.valueOf(s2);
            ndwiEntries.add(new Entry(i + 1, y2));
        }

        LineDataSet ndwiSet = new LineDataSet(ndwiEntries, title);
        ndwiSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        ndwiSet.setLineWidth(1f);
        ndwiSet.setCircleColor(resources.getColor(R.color.turqouise));
        ndwiSet.setDrawCircleHole(false);
        ndwiSet.setColor(resources.getColor(R.color.turqouise));
        ndwiSet.setValueTextColor(resources.getColor(R.color.turqouise));

        yAxisLabel.clear();
        for (int i = 0; i < 12; i++) {
            int value = 10 * i;
            yAxisLabel.add(" " + value);
        }

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);
        // set an alternative background color
        lineChart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData(ndwiSet);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        // set data
        lineChart.setData(data);

        lineChart.animateX(1500);
        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        //X Axis
        XAxis xAxis = lineChart.getXAxis();
        //xAxis.setTextSize(11f);
        xAxis.setGranularity(1f);
        xAxis.setXOffset(1f);
        xAxis.setSpaceMin(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisLineWidth(1f);
        xAxis.setAxisMaximum(xAxisLabel.size());
        xAxis.setAxisMinimum(0f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setSpaceMax(1f);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        //Y Axis
        lineChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = lineChart.getAxisLeft();

        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setTextColor(Color.BLACK);
       /* YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaximum(900);
        rightAxis.setAxisMinimum(-200);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);*/
        lineChart.highlightValue(ndwiEntries.get(sliderIndex).getX(), 0); //
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu.add(Menu.NONE, 1, Menu.NONE, "Item name");

        getSatusreProducts(menu);
        return true;
    }

    private Tooltip mTooltip;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e(TAG, "Menu Click, ID: " + item.getItemId() + ", NAME: " + item.getTitle().toString());
        if (item.getItemId() == R.id.empty) {
            //Toasty.info(context,"Help option menu clicked",Toast.LENGTH_LONG).show();
            // View view = item.getActionView();
            item.setActionView(findViewById(R.id.empty));
            String description = productMap.get(Integer.valueOf(productId.trim()));
            String text = "<html><body style=\"text-align:justify\"> " + description + "</body></Html>\n";
           /* text = "<html><body><p align=\"justify\">";
            text+= description;
            text+= "</p></body></html>";
            String finalDesc=text;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                finalDesc=Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT).toString();
            } else {
                finalDesc=Html.fromHtml(text).toString();
            }*/
            Log.e(TAG, "Descr " + description);
            if (mTooltip != null && mTooltip.isShowing()) {
                mTooltip.dismiss();
            } else {
                mTooltip = new Tooltip.Builder(item, R.style.Tooltip)
                        .setDismissOnClick(true)
                        .setGravity(Gravity.BOTTOM)
                        .setText(Html.fromHtml(text))
                        .show();
            }
        } else {
            if (productId != null && !TextUtils.isEmpty(productId)) {
                if (Integer.valueOf(productId) != item.getItemId()) {
                    productId = item.getItemId() + "";
                    getData(item.getItemId() + "", item.getTitle().toString());
                }
            } else {
                productId = item.getItemId() + "";
                getData(item.getItemId() + "", item.getTitle().toString());
            }
        }
        return true;
    }

    JSONObject object;
    private boolean isMenuItemLoaded = false;

    private void getSatusreProducts(Menu menu) {
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        apiInterface.getProducts(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.USER_ID),
                SharedPreferencesMethod.getString(context,SharedPreferencesMethod.TOKEN)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String data = response.body().string();
                        Log.e(TAG, "Products " + data);
                        object = new JSONObject(data);
                        JSONArray array = object.getJSONArray("data");
                        isMenuItemLoaded = true;
                        setColorLegend(object, productId);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            JSONArray jsonArray = jsonObject.getJSONArray("products");
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject product = jsonArray.getJSONObject(j);
                                if (product.has("productId")) {
                                    if (product.has("description")) {
                                        productMap.put(product.getInt("productId"), product.getString("description"));
                                    } else {
                                        productMap.put(product.getInt("productId"), product.getString("No description available right now!"));
                                    }
                                }
                                int id = product.getInt("productId");
                                String name = product.getString("productName");
                                menu.add(Menu.NONE, id, Menu.NONE, getChars(name));
                            }
                        }
                        getMenuInflater().inflate(R.menu.help_menu_satsure, menu);

                    } catch (IOException e) {
                        e.printStackTrace();
                        isMenuItemLoaded = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(SatsureImageActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(SatsureImageActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        isMenuItemLoaded = false;
                        Log.e(TAG, "Products Err " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isMenuItemLoaded = false;
            }
        });
    }

    private String getChars(String fullname) {
        StringBuilder initials = new StringBuilder();
        for (String s : fullname.trim().split(" ")) {
            initials.append(s.toUpperCase().charAt(0));
        }
        System.out.println(initials.toString());
        return initials.toString();
    }
    private void getData(String id, String name) {
        if (mTooltip != null && mTooltip.isShowing()) {
            mTooltip.dismiss();
        }
        productId = id;
        if (name != null && !TextUtils.isEmpty(name))
            tittle.setText(name);
        else
            tittle.setText("SatSure Analysis");

        Log.e(TAG,"ProductId "+id);

        progressBarSat.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        noDataAvailableLayout.setVisibility(View.GONE);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String farmId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.SVFARMID);
        apiInterface.getSatsureData(farmId, id,SharedPreferencesMethod.getString(context,SharedPreferencesMethod.USER_ID)).enqueue(new Callback<SatsureResponse>() {
            @Override
            public void onResponse(Call<SatsureResponse> call, Response<SatsureResponse> response) {
                if (response.isSuccessful()) {
                    progressBarSat.setVisibility(View.GONE);
                    SatsureResponse satsureResponse = response.body();
                    if (satsureResponse != null && satsureResponse.getData() != null && satsureResponse.getData().size() > 0) {
                        Log.e(TAG, "SatSure res " + new Gson().toJson(satsureResponse));
                        scrollView.setVisibility(View.VISIBLE);
                        noDataAvailableLayout.setVisibility(View.GONE);
                        setNdwiChart(satsureResponse.getData(), name);
                        onlineModeEvent(satsureResponse.getData());

                    } else {
                        scrollView.setVisibility(View.GONE);
                        noDataAvailableLayout.setVisibility(View.VISIBLE);
                        Log.e(TAG, "SatSure else 1");
                    }
                }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(SatsureImageActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(SatsureImageActivity.this, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        progressBarSat.setVisibility(View.GONE);
                        Log.e(TAG, "SatSure else 2 " + response.errorBody().string());
                        scrollView.setVisibility(View.GONE);
                        noDataAvailableLayout.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SatsureResponse> call, Throwable t) {
                Log.e(TAG, "SatSure Failure " + t.toString());
                progressBarSat.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                noDataAvailableLayout.setVisibility(View.VISIBLE);
            }
        });

        setColorLegend(object, id);
    }


    private void setColorLegend(JSONObject object, String productId) {
        if (isMenuItemLoaded && productId != null) {
            try {
                JSONArray array = object.getJSONArray("data");
                isMenuItemLoaded = true;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    JSONArray jsonArray = jsonObject.getJSONArray("products");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject product = jsonArray.getJSONObject(j);
                        int id = product.getInt("productId");
                        if (id == Integer.valueOf(productId)) {
                            JSONArray report = product.getJSONArray("reportDetails");
                            to100Color = report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("0 to 100");
                            to110Color = report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("100 to 110");
                            to120Color = report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("110 to 120");
                            to130Color = report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("120 to 130");
                            to140Color = report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("130 to 140");
                            to150Color = report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("140 to 150");
                            to160Color = report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("150 to 160");
                            to170Color = report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("160 to 170");
                            to180Color = report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("170 to 180");
                            to190Color = report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("180 to 190");
                            to200Color = report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("190 to 200");

                            tv0To100.setBackgroundColor(Color.parseColor(report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("0 to 100")));
                            tv100To110.setBackgroundColor(Color.parseColor(report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("100 to 110")));
                            tv110To120.setBackgroundColor(Color.parseColor(report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("110 to 120")));
                            tv120To130.setBackgroundColor(Color.parseColor(report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("120 to 130")));
                            tv130To140.setBackgroundColor(Color.parseColor(report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("130 to 140")));
                            tv140To150.setBackgroundColor(Color.parseColor(report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("140 to 150")));
                            tv150To160.setBackgroundColor(Color.parseColor(report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("150 to 160")));
                            tv160To170.setBackgroundColor(Color.parseColor(report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("160 to 170")));
                            tv170To180.setBackgroundColor(Color.parseColor(report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("170 to 180")));
                            tv180To190.setBackgroundColor(Color.parseColor(report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("180 to 190")));
                            tv190To200.setBackgroundColor(Color.parseColor(report.getJSONObject(0).getJSONObject("valueLegendDetailed").getString("190 to 200")));
                            break;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                isMenuItemLoaded = false;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    private void chengeMessageText(Entry e) {
        if (productId != null) {
            if (productId.trim().equals("3")) {
                messageTv.setVisibility(View.VISIBLE);
                String color = SATSURE_NDVI.to100Color;
                if (e.getY() >= 0 && e.getY() <= 100) {
                    color = SATSURE_NDVI.to100Color;
                } else if (e.getY() > 100 && e.getY() <= 110) {
                    color = SATSURE_NDVI.to110Color;
                } else if (e.getY() > 110 && e.getY() <= 120) {
                    color = SATSURE_NDVI.to120Color;
                } else if (e.getY() > 120 && e.getY() <= 130) {
                    color = SATSURE_NDVI.to130Color;
                } else if (e.getY() > 130 && e.getY() <= 140) {
                    color = SATSURE_NDVI.to140Color;
                } else if (e.getY() > 140 && e.getY() <= 150) {
                    color = SATSURE_NDVI.to150Color;
                } else if (e.getY() > 150 && e.getY() <= 160) {
                    color = SATSURE_NDVI.to160Color;
                } else if (e.getY() > 160 && e.getY() <= 170) {
                    color = SATSURE_NDVI.to170Color;
                } else if (e.getY() > 170 && e.getY() <= 180) {
                    color = SATSURE_NDVI.to180Color;
                } else if (e.getY() > 180 && e.getY() <= 190) {
                    color = SATSURE_NDVI.to190Color;
                } else if (e.getY() > 190 && e.getY() <= 200) {
                    color = SATSURE_NDVI.to200Color;
                }
                messageTv.setText(color);
            } else if (productId.trim().equals("4")) {
                messageTv.setVisibility(View.VISIBLE);
                String color = SATSURE_NDWI.to100Color;
                if (e.getY() >= 0 && e.getY() <= 100) {
                    color = SATSURE_NDWI.to100Color;
                } else if (e.getY() > 100 && e.getY() <= 110) {
                    color = SATSURE_NDWI.to110Color;
                } else if (e.getY() > 110 && e.getY() <= 120) {
                    color = SATSURE_NDWI.to120Color;
                } else if (e.getY() > 120 && e.getY() <= 130) {
                    color = SATSURE_NDWI.to130Color;
                } else if (e.getY() > 130 && e.getY() <= 140) {
                    color = SATSURE_NDWI.to140Color;
                } else if (e.getY() > 140 && e.getY() <= 150) {
                    color = SATSURE_NDWI.to150Color;
                } else if (e.getY() > 150 && e.getY() <= 160) {
                    color = SATSURE_NDWI.to160Color;
                } else if (e.getY() > 160 && e.getY() <= 170) {
                    color = SATSURE_NDWI.to170Color;
                } else if (e.getY() > 170 && e.getY() <= 180) {
                    color = SATSURE_NDWI.to180Color;
                } else if (e.getY() > 180 && e.getY() <= 190) {
                    color = SATSURE_NDWI.to190Color;
                } else if (e.getY() > 190 && e.getY() <= 200) {
                    color = SATSURE_NDWI.to200Color;
                }
                messageTv.setText(color);
            } else {
                //hide text
            }
        } else {
            //hide text
        }
    }

}
