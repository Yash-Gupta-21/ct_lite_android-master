package com.i9930.croptrails.Weather;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import com.i9930.croptrails.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link WeatherFragmentToday#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragmentToday extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CITY = "ARG_CITY";
    private static final String ARG_STATE = "ARG_STATE";
    private static final String ARG_WEATHER_INFO = "ARG_WEATHER_INFO";
    private static final String ARG_CURR_TEMP = "ARG_CURR_TEMP";
    private static final String ARG_MAX_TEMP = "ARG_MAX_TEMP";
    private static final String ARG_MIN_TEMP = "ARG_MIN_TEMP";
    private static final String ARG_TODAYS_REPORT = "ARG_TODAYS_REPORT";
    private static final String ARG_HUMIDITY = "ARG_HUMIDITY";
    private static final String ARG_PRECIPITATION = "ARG_PRECIPITATION";
    private static final String ARG_PROBABILITY = "ARG_PROBABILITY";
    private static final String ARG_CLOUD_COVER = "ARG_CLOUD_COVER";
    private static final String ARG_WIND_SPEED = "ARG_WIND_SPEED";
    private static final String ARG_ICON = "ARG_ICON";
    private static final String ARG_POS = "ARG_POS";

     TextView txt_city;
     TextView txt_state;
     TextView txt_weather_info;
     TextView txt_curr_temp;
     TextView txt_max_temp;
     TextView txt_min_temp;
     TextView txt_todays_report;
     TextView humidity;
     TextView precipitation;
     TextView probability;
     TextView cloud_cover;
     TextView wind_speed;
     LinearLayout weather_skycon_layout;
     LinearLayout weather_skycon_layout_next;
     ImageView skycon_view;
     ImageView skycon_view_next;
     //LinearLayout skycon_layout;
     Context context;

    // TODO: Rename and change types of parameters
    private  String mParamcity;
    private String mParamstate;
    private String mParamweatherinfo;
    private String mParamcurrtemp;
    private String mParammaxtemp;
    private String mParammintemp;
    private String mParamtodaysreport;
    private String mParamhumidity;
    private String mParamprecipitation;
    private String mParamprobability;
    private String mParamcloudcover;
    private String mParamwindspeed;
    private String mParamicon;
    private String mParampos;

    AnimatedVectorDrawableCompat animatedVectorDrawableCompat;
    AnimationDrawable animation;

/*
    private OnFragmentInteractionListener mListener;
*/

    public WeatherFragmentToday() {
        // Required empty public constructor

    }



    // TODO: Rename and change types and number of parameters
    public static WeatherFragmentToday newInstance(String txt_city, String txt_state, String txt_weather_info,
                                                   String txt_curr_temp, String txt_max_temp, String txt_min_temp,
                                                   String txt_todays_report, String humidity, String precipitation,
                                                   String probability, String cloud_cover, String wind_speed, String icon, String pos) {
        WeatherFragmentToday fragment = new WeatherFragmentToday();
        Bundle args = new Bundle();
        args.putString(ARG_CITY,txt_city );
        args.putString(ARG_STATE, txt_state);
        args.putString(ARG_WEATHER_INFO, txt_weather_info);
        args.putString(ARG_CURR_TEMP, txt_curr_temp);
        args.putString(ARG_MAX_TEMP, txt_max_temp);
        args.putString(ARG_MIN_TEMP, txt_min_temp);
        args.putString(ARG_TODAYS_REPORT, txt_todays_report);
        args.putString(ARG_HUMIDITY, humidity);
        args.putString(ARG_PRECIPITATION, precipitation);
        args.putString(ARG_PROBABILITY, probability);
        args.putString(ARG_CLOUD_COVER, cloud_cover);
        args.putString(ARG_WIND_SPEED, wind_speed);
        args.putString(ARG_ICON, icon);
        args.putString(ARG_POS, pos);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamcity = getArguments().getString(ARG_CITY);
            mParamstate = getArguments().getString(ARG_STATE);
            mParamweatherinfo = getArguments().getString(ARG_WEATHER_INFO);
            mParamcurrtemp = getArguments().getString(ARG_CURR_TEMP);
            mParammaxtemp = getArguments().getString(ARG_MAX_TEMP);
            mParammintemp = getArguments().getString(ARG_MIN_TEMP);
            mParamtodaysreport = getArguments().getString(ARG_TODAYS_REPORT);
            mParamhumidity = getArguments().getString(ARG_HUMIDITY);
            mParamprecipitation = getArguments().getString(ARG_PRECIPITATION);
            mParamprobability = getArguments().getString(ARG_PROBABILITY);
            mParamcloudcover = getArguments().getString(ARG_CLOUD_COVER);
            mParamwindspeed = getArguments().getString(ARG_WIND_SPEED);
            mParamicon = getArguments().getString(ARG_ICON);
            mParampos = getArguments().getString(ARG_POS);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_today, container, false);
        txt_city = view.findViewById(R.id.txt_city);
        txt_state = view.findViewById(R.id.txt_state);
        txt_weather_info = view.findViewById(R.id.txt_weather_info);
        txt_curr_temp = view.findViewById(R.id.txt_curr_temp);
        txt_max_temp = view.findViewById(R.id.txt_max_temp);
        txt_min_temp = view.findViewById(R.id.txt_min_temp);
        txt_todays_report = view.findViewById(R.id.txt_todays_report);
        humidity = view.findViewById(R.id.humidity);
        precipitation = view.findViewById(R.id.precipitation);
        probability = view.findViewById(R.id.probability);
        cloud_cover = view.findViewById(R.id.cloud_cover);
        wind_speed = view.findViewById(R.id.wind);
        skycon_view = view.findViewById(R.id.skycon_view);
        skycon_view_next = view.findViewById(R.id.skycon_view_next);
        weather_skycon_layout = view.findViewById(R.id.weather_skycon_layout);
        weather_skycon_layout_next = view.findViewById(R.id.weather_skycon_layout_next);
        
        //skycon_layout = view.findViewById(R.id.skycon_view);

        txt_city.setText(mParamcity);
        txt_state.setText(mParamstate);
        txt_weather_info.setText(mParamweatherinfo);
        txt_curr_temp.setText(mParamcurrtemp);
        txt_max_temp.setText(mParammaxtemp);
        txt_min_temp.setText(mParammintemp);
        txt_todays_report.setText(mParamtodaysreport);
        humidity.setText(mParamhumidity);
        precipitation.setText(mParamprecipitation);
        probability.setText(mParamprobability);
        cloud_cover.setText(mParamcloudcover);
        wind_speed.setText(mParamwindspeed);

        if(mParampos.equals("0")) {
            weather_skycon_layout.setVisibility(View.VISIBLE);
            weather_skycon_layout_next.setVisibility(View.GONE);
            select_skycon(mParamicon,skycon_view);
        }
        else
        {
            weather_skycon_layout.setVisibility(View.GONE);
            weather_skycon_layout_next.setVisibility(View.VISIBLE);
            select_skycon(mParamicon,skycon_view_next);
        }

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
   /* public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/

        this.context = context;

    }
    /*public void passData(String txt_city, String txt_state, String txt_weather_info,
                         String txt_curr_temp,String txt_max_temp,String txt_min_temp,
                         String txt_todays_report, String humidity, String precipitation,
                         String probability,String cloud_cover, String wind_speed) {

        this.txt_city.setText(String.valueOf(txt_city));
        this.txt_state.setText(String.valueOf(txt_state));
        this.txt_weather_info.setText(String.valueOf(txt_weather_info));
        this.txt_curr_temp.setText(String.valueOf(txt_curr_temp));
        this.txt_max_temp.setText(String.valueOf(txt_max_temp));
        this.txt_min_temp.setText(String.valueOf(txt_min_temp));
        this.txt_todays_report.setText(String.valueOf(txt_todays_report));
        this.humidity.setText(String.valueOf(humidity)+" mg/l");
        this.precipitation.setText(String.valueOf(precipitation)+" mm/hr");
        this.probability.setText(String.valueOf(probability)+"% Probability");
        this.cloud_cover.setText(String.valueOf(cloud_cover)+" (0-8)");
        this.wind_speed.setText(String.valueOf(wind_speed)+" km/hr");
    }*/
    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void select_skycon(String icon, ImageView imageView)
    {
        switch (icon)
        {
            case "clear-day":
            {

                imageView.setImageResource(R.drawable.ic_day);
                //animatedVectorDrawableCompat.start();
                break;
            }
            case "clear-night":
            {
                imageView.setImageResource(R.drawable.ic_night);
                //animatedVectorDrawableCompat.start();
                break;
            }
            case "rain":
            {
                imageView.setImageResource(R.drawable.ic_rainy);
                //animatedVectorDrawableCompat.start();
                break;
            }
            case "snow":
            {
                imageView.setImageResource(R.drawable.ic_snow);
                //animatedVectorDrawableCompat.start();
                break;
            }
            case "sleet":
            {
                imageView.setImageResource(R.drawable.ic_sleet);
                //animatedVectorDrawableCompat.start();
                break;
            }
            case "wind":
            {
                imageView.setImageResource(R.drawable.ic_windy);
                //animatedVectorDrawableCompat.start();
                break;
            }
            case "fog":
            {
                imageView.setImageResource(R.drawable.ic_fog);
                //animatedVectorDrawableCompat.start();
                break;
            }
            case "cloudy":
            {
                imageView.setImageResource(R.drawable.ic_cloudy);
                //animatedVectorDrawableCompat.start();
                break;
            }
            case "partly-cloudy-day":
            {
                imageView.setImageResource(R.drawable.ic_cloudy_day_2);
                //animatedVectorDrawableCompat.start();
                break;
            }
            case "partly-cloudy-night":
            {
                imageView.setImageResource(R.drawable.ic_cloudy_night_2);
                //animatedVectorDrawableCompat.start();
                break;
            }
            case "hail":
            {
                imageView.setImageResource(R.drawable.ic_hail);
                //animatedVectorDrawableCompat.start();
                break;
            }
            case "thunderstorm":
            {
                imageView.setImageResource(R.drawable.ic_thunder);
                //animatedVectorDrawableCompat.start();
                break;
            }
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
