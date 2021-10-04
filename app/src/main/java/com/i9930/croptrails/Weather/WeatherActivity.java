package com.i9930.croptrails.Weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.i9930.croptrails.FarmDetails.Model.WeatherForecast;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {
    Resources resources;
    String result_city;
    String result_state;

    WeatherForecast weatherForecast;

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String languageToLoad  = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();

        result_city = resources.getString(R.string.location_not_available);
        result_state = resources.getString(R.string.location_not_available);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //setupTabIcons();
        View headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.weather_tab_content, null, false);

        LinearLayout day1 = (LinearLayout) headerView.findViewById(R.id.custom_tab_day1);
        LinearLayout day2 = (LinearLayout) headerView.findViewById(R.id.custom_tab_day2);
        LinearLayout day3 = (LinearLayout) headerView.findViewById(R.id.custom_tab_day3);
        LinearLayout day4 = (LinearLayout) headerView.findViewById(R.id.custom_tab_day4);
        LinearLayout day5 = (LinearLayout) headerView.findViewById(R.id.custom_tab_day5);
        LinearLayout day6 = (LinearLayout) headerView.findViewById(R.id.custom_tab_day6);
        LinearLayout day7 = (LinearLayout) headerView.findViewById(R.id.custom_tab_day7);
        LinearLayout day8 = (LinearLayout) headerView.findViewById(R.id.custom_tab_day8);



        //Bundle extras = getIntent().getExtras();
        weatherForecast = (WeatherForecast) getIntent().getSerializableExtra("weatherForecast"); //Obtaining data

        //set day of the week

        TextView tvday1=day1.findViewById(R.id.weather_tab_day1);
        TextView tvday2=day2.findViewById(R.id.weather_tab_day2);
        TextView tvday3=day3.findViewById(R.id.weather_tab_day3);
        TextView tvday4=day4.findViewById(R.id.weather_tab_day4);
        TextView tvday5=day5.findViewById(R.id.weather_tab_day5);
        TextView tvday6=day6.findViewById(R.id.weather_tab_day6);
        TextView tvday7=day7.findViewById(R.id.weather_tab_day7);
        TextView tvday8=day8.findViewById(R.id.weather_tab_day8);

        tvday1.setText(String.valueOf((weatherForecast.getDaily().getData().get(0).getTime())));
        tvday2.setText(String.valueOf((weatherForecast.getDaily().getData().get(1).getTime())));
        tvday3.setText(String.valueOf((weatherForecast.getDaily().getData().get(2).getTime())));
        tvday4.setText(String.valueOf((weatherForecast.getDaily().getData().get(3).getTime())));
        tvday5.setText(String.valueOf((weatherForecast.getDaily().getData().get(4).getTime())));
        tvday6.setText(String.valueOf((weatherForecast.getDaily().getData().get(5).getTime())));
        tvday7.setText(String.valueOf((weatherForecast.getDaily().getData().get(6).getTime())));
        tvday8.setText(String.valueOf((weatherForecast.getDaily().getData().get(7).getTime())));

        //set date of the week

        TextView tvdate1=day1.findViewById(R.id.weather_tab_date1);
        TextView tvdate2=day2.findViewById(R.id.weather_tab_date2);
        TextView tvdate3=day3.findViewById(R.id.weather_tab_date3);
        TextView tvdate4=day4.findViewById(R.id.weather_tab_date4);
        TextView tvdate5=day5.findViewById(R.id.weather_tab_date5);
        TextView tvdate6=day6.findViewById(R.id.weather_tab_date6);
        TextView tvdate7=day7.findViewById(R.id.weather_tab_date7);
        TextView tvdate8=day8.findViewById(R.id.weather_tab_date8);

        tvdate1.setText(String.valueOf((weatherForecast.getDaily().getData().get(0).getDate())));
        tvdate2.setText(String.valueOf((weatherForecast.getDaily().getData().get(1).getDate())));
        tvdate3.setText(String.valueOf((weatherForecast.getDaily().getData().get(2).getDate())));
        tvdate4.setText(String.valueOf((weatherForecast.getDaily().getData().get(3).getDate())));
        tvdate5.setText(String.valueOf((weatherForecast.getDaily().getData().get(4).getDate())));
        tvdate6.setText(String.valueOf((weatherForecast.getDaily().getData().get(5).getDate())));
        tvdate7.setText(String.valueOf((weatherForecast.getDaily().getData().get(6).getDate())));
        tvdate8.setText(String.valueOf((weatherForecast.getDaily().getData().get(7).getDate())));

        //set weather svg

        ImageView icon1 = day1.findViewById(R.id.weather_tab_icon1);
        ImageView icon2 = day2.findViewById(R.id.weather_tab_icon2);
        ImageView icon3 = day3.findViewById(R.id.weather_tab_icon3);
        ImageView icon4 = day4.findViewById(R.id.weather_tab_icon4);
        ImageView icon5 = day5.findViewById(R.id.weather_tab_icon5);
        ImageView icon6 = day6.findViewById(R.id.weather_tab_icon6);
        ImageView icon7 = day7.findViewById(R.id.weather_tab_icon7);
        ImageView icon8 = day8.findViewById(R.id.weather_tab_icon8);

        select_tab_skycon(weatherForecast.getDaily().getData().get(0).getIcon(),icon1);
        select_tab_skycon(weatherForecast.getDaily().getData().get(1).getIcon(),icon2);
        select_tab_skycon(weatherForecast.getDaily().getData().get(2).getIcon(),icon3);
        select_tab_skycon(weatherForecast.getDaily().getData().get(3).getIcon(),icon4);
        select_tab_skycon(weatherForecast.getDaily().getData().get(4).getIcon(),icon5);
        select_tab_skycon(weatherForecast.getDaily().getData().get(5).getIcon(),icon6);
        select_tab_skycon(weatherForecast.getDaily().getData().get(6).getIcon(),icon7);
        select_tab_skycon(weatherForecast.getDaily().getData().get(7).getIcon(),icon8);


        tabLayout.getTabAt(0).setCustomView(day1);
        tabLayout.getTabAt(1).setCustomView(day2);
        tabLayout.getTabAt(2).setCustomView(day3);
        tabLayout.getTabAt(3).setCustomView(day4);
        tabLayout.getTabAt(4).setCustomView(day5);
        tabLayout.getTabAt(5).setCustomView(day6);
        tabLayout.getTabAt(6).setCustomView(day7);
        tabLayout.getTabAt(7).setCustomView(day8);
        Geocoder geocoder = new Geocoder(WeatherActivity.this, Locale.getDefault());
        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(
                    weatherForecast.getLatitude(),weatherForecast.getLongitude(), 1);
            if (list != null && list.size() > 0) {
                Address address = list.get(0);
                // sending back first address line and locality
                result_city = address.getLocality();
                result_state = address.getSubLocality();
                Log.e("trace_location",address.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WeatherFragmentToday());
        adapter.addFragment(new WeatherFragmentToday());
        adapter.addFragment(new WeatherFragmentToday());
        adapter.addFragment(new WeatherFragmentToday());
        adapter.addFragment(new WeatherFragmentToday());
        adapter.addFragment(new WeatherFragmentToday());
        adapter.addFragment(new WeatherFragmentToday());
        adapter.addFragment(new WeatherFragmentToday());
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            DecimalFormat format = new DecimalFormat("##.##");
            switch (position)
            {

                case 0:return WeatherFragmentToday.newInstance(result_city,result_state,
                        weatherForecast.getCurrently().getSummary(),
                        String.valueOf(Math.round(weatherForecast.getCurrently().getTemperature()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(0).getApparentTemperatureHigh()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(0).getApparentTemperatureLow()))+"\u00b0 c",
                        weatherForecast.getDaily().getData().get(0).getSummary(),
                        String.valueOf((weatherForecast.getDaily().getData().get(0).getHumidity()))+" mg/l",
                        String.valueOf(format.format(Math.round(weatherForecast.getDaily().getData().get(0).getPrecipIntensityMax())))+" mm/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(0).getPrecipProbability()))+"% " + resources.getString(R.string.probability),
                        String.valueOf((weatherForecast.getDaily().getData().get(0).getCloudCover()))+" (0-8)",
                        String.valueOf((weatherForecast.getDaily().getData().get(0).getWindSpeed()))+" km/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(0).getIcon())),"0");

                case 1:return WeatherFragmentToday.newInstance(result_city,result_state,
                        weatherForecast.getCurrently().getSummary(),
                        String.valueOf(Math.round(weatherForecast.getCurrently().getTemperature()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(1).getApparentTemperatureHigh()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(1).getApparentTemperatureLow()))+"\u00b0 c",
                        weatherForecast.getDaily().getData().get(1).getSummary(),
                        String.valueOf((weatherForecast.getDaily().getData().get(1).getHumidity()))+" mg/l",
                        String.valueOf(format.format(Math.round(weatherForecast.getDaily().getData().get(1).getPrecipIntensityMax())))+" mm/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(1).getPrecipProbability()))+"% " + resources.getString(R.string.probability),
                        String.valueOf((weatherForecast.getDaily().getData().get(1).getCloudCover()))+" (0-8)",
                        String.valueOf((weatherForecast.getDaily().getData().get(1).getWindSpeed()))+" km/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(1).getIcon())),"1");

                case 2:return WeatherFragmentToday.newInstance(result_city,result_state,
                        weatherForecast.getCurrently().getSummary(),
                        String.valueOf(Math.round(weatherForecast.getCurrently().getTemperature()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(2).getApparentTemperatureHigh()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(2).getApparentTemperatureLow()))+"\u00b0 c",
                        weatherForecast.getDaily().getData().get(2).getSummary(),
                        String.valueOf((weatherForecast.getDaily().getData().get(2).getHumidity()))+" mg/l",
                        String.valueOf(format.format(Math.round(weatherForecast.getDaily().getData().get(2).getPrecipIntensityMax())))+" mm/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(2).getPrecipProbability()))+"% " + resources.getString(R.string.probability),
                        String.valueOf((weatherForecast.getDaily().getData().get(2).getCloudCover()))+" (0-8)",
                        String.valueOf((weatherForecast.getDaily().getData().get(2).getWindSpeed()))+" km/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(2).getIcon())),"2");

                case 3:return WeatherFragmentToday.newInstance(result_city,result_state,
                        weatherForecast.getCurrently().getSummary(),
                        String.valueOf(Math.round(weatherForecast.getCurrently().getTemperature()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(3).getApparentTemperatureHigh()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(3).getApparentTemperatureLow()))+"\u00b0 c",
                        weatherForecast.getDaily().getData().get(3).getSummary(),
                        String.valueOf((weatherForecast.getDaily().getData().get(3).getHumidity()))+" mg/l",
                        String.valueOf(format.format(Math.round(weatherForecast.getDaily().getData().get(3).getPrecipIntensityMax())))+" mm/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(3).getPrecipProbability()))+"% " + resources.getString(R.string.probability),
                        String.valueOf((weatherForecast.getDaily().getData().get(3).getCloudCover()))+" (0-8)",
                        String.valueOf((weatherForecast.getDaily().getData().get(3).getWindSpeed()))+" km/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(3).getIcon())),"3");

                case 4:return WeatherFragmentToday.newInstance(result_city,result_state,
                        weatherForecast.getCurrently().getSummary(),
                        String.valueOf(Math.round(weatherForecast.getCurrently().getTemperature()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(4).getApparentTemperatureHigh()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(4).getApparentTemperatureLow()))+"\u00b0 c",
                        weatherForecast.getDaily().getData().get(4).getSummary(),
                        String.valueOf((weatherForecast.getDaily().getData().get(4).getHumidity()))+" mg/l",
                        String.valueOf(format.format(Math.round(weatherForecast.getDaily().getData().get(4).getPrecipIntensityMax())))+" mm/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(4).getPrecipProbability()))+"% " + resources.getString(R.string.probability),
                        String.valueOf((weatherForecast.getDaily().getData().get(4).getCloudCover()))+" (0-8)",
                        String.valueOf((weatherForecast.getDaily().getData().get(4).getWindSpeed()))+" km/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(4).getIcon())),"4");

                case 5:return WeatherFragmentToday.newInstance(result_city,result_state,
                        weatherForecast.getCurrently().getSummary(),
                        String.valueOf(Math.round(weatherForecast.getCurrently().getTemperature()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(5).getApparentTemperatureHigh()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(5).getApparentTemperatureLow()))+"\u00b0 c",
                        weatherForecast.getDaily().getData().get(5).getSummary(),
                        String.valueOf((weatherForecast.getDaily().getData().get(5).getHumidity()))+" mg/l",
                        String.valueOf(format.format(Math.round(weatherForecast.getDaily().getData().get(5).getPrecipIntensityMax())))+" mm/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(5).getPrecipProbability()))+"% " + resources.getString(R.string.probability),
                        String.valueOf((weatherForecast.getDaily().getData().get(5).getCloudCover()))+" (0-8)",
                        String.valueOf((weatherForecast.getDaily().getData().get(5).getWindSpeed()))+" km/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(5).getIcon())),"5");

                case 6:return WeatherFragmentToday.newInstance(result_city,result_state,
                        weatherForecast.getCurrently().getSummary(),
                        String.valueOf(Math.round(weatherForecast.getCurrently().getTemperature()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(6).getApparentTemperatureHigh()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(6).getApparentTemperatureLow()))+"\u00b0 c",
                        weatherForecast.getDaily().getData().get(6).getSummary(),
                        String.valueOf((weatherForecast.getDaily().getData().get(6).getHumidity()))+" mg/l",
                        String.valueOf(format.format(Math.round(weatherForecast.getDaily().getData().get(6).getPrecipIntensityMax())))+" mm/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(6).getPrecipProbability()))+"% " + resources.getString(R.string.probability),
                        String.valueOf((weatherForecast.getDaily().getData().get(6).getCloudCover()))+" (0-8)",
                        String.valueOf((weatherForecast.getDaily().getData().get(6).getWindSpeed()))+" km/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(6).getIcon())),"6");

                case 7:return WeatherFragmentToday.newInstance(result_city,result_state,
                        weatherForecast.getCurrently().getSummary(),
                        String.valueOf(Math.round(weatherForecast.getCurrently().getTemperature()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(7).getApparentTemperatureHigh()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(7).getApparentTemperatureLow()))+"\u00b0 c",
                        weatherForecast.getDaily().getData().get(7).getSummary(),
                        String.valueOf((weatherForecast.getDaily().getData().get(7).getHumidity()))+" mg/l",
                        String.valueOf(format.format(Math.round(weatherForecast.getDaily().getData().get(7).getPrecipIntensityMax())))+" mm/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(7).getPrecipProbability()))+"% " + resources.getString(R.string.probability),
                        String.valueOf((weatherForecast.getDaily().getData().get(7).getCloudCover()))+" (0-8)",
                        String.valueOf((weatherForecast.getDaily().getData().get(7).getWindSpeed()))+" km/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(7).getIcon())),"7");

                default :return WeatherFragmentToday.newInstance(result_city,result_state,
                        weatherForecast.getCurrently().getSummary(),
                        String.valueOf(Math.round(weatherForecast.getCurrently().getTemperature()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(7).getApparentTemperatureHigh()))+"\u00b0",
                        String.valueOf(Math.round(weatherForecast.getDaily().getData().get(7).getApparentTemperatureLow()))+"\u00b0 c",
                        weatherForecast.getDaily().getData().get(7).getSummary(),
                        String.valueOf((weatherForecast.getDaily().getData().get(7).getHumidity()))+" mg/l",
                        String.valueOf(format.format(Math.round(weatherForecast.getDaily().getData().get(0).getPrecipIntensityMax())))+" mm/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(0).getPrecipProbability()))+"% " + resources.getString(R.string.probability),
                        String.valueOf((weatherForecast.getDaily().getData().get(0).getCloudCover()))+" (0-8)",
                        String.valueOf((weatherForecast.getDaily().getData().get(0).getWindSpeed()))+" km/hr",
                        String.valueOf((weatherForecast.getDaily().getData().get(8).getIcon())),"8");
            }
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
/*
            mFragmentTitleList.add(title);
*/
        }

    /*    @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
*/
    }

    public void select_tab_skycon(String icon,ImageView imageview)
    {
        switch (icon)
        {
            case "clear-day":
            {
                imageview.setImageResource(R.drawable.ic_day);
                break;
            }
            case "clear-night":
            {
                imageview.setImageResource(R.drawable.ic_night);
                break;
            }
            case "rain":
            {
                imageview.setImageResource(R.drawable.ic_rainy);
                break;
            }
            case "snow":
            {
                imageview.setImageResource(R.drawable.ic_rainy);
                break;
            }
            case "sleet":
            {
                imageview.setImageResource(R.drawable.ic_sleet);
                break;
            }
            case "wind":
            {
                imageview.setImageResource(R.drawable.ic_windy);
                break;
            }
            case "fog":
            {
                imageview.setImageResource(R.drawable.ic_fog_day);
                break;
            }
            case "cloudy":
            {
                imageview.setImageResource(R.drawable.ic_cloudy);
                break;
            }
            case "partly-cloudy-day":
            {
                imageview.setImageResource(R.drawable.ic_cloudy_day_2);
                break;
            }
            case "partly-cloudy-night":
            {
                imageview.setImageResource(R.drawable.ic_cloudy_night_2);
                break;
            }
            case "hail":
            {
                imageview.setImageResource(R.drawable.ic_hail);
                break;
            }
            case "thunderstorm":
            {
                imageview.setImageResource(R.drawable.ic_thunder);
                break;
            }
        }
    }
}
