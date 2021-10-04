package com.i9930.croptrails.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;
public class SharedPreferencesMethod {
    public static final String TASK_COUNT = "TASK_COUNT";
    public static final String COMP_COUNTRY_ID = "COMP_COUNTRY_ID";
    public static final String SOWING_DATE = "SOWING_DATE";
    public static final String COMP_AUTO_ACCURACY = "COMP_AUTO_ACCURACY";
    public static final String USER_LANG = "USER_LANG";
    public static final String HAS_MULTI_COMP = "HAS_MULTI_COMP";
    public static final String USER_NAME = "USER_NAME";
    public static final String TOKEN = "TOKEN";
    public static final String COMP_NOT_SELECTED = "COMP_NOT_SELECTED";
    public static final String COUNTRY_CODE = "COUNTRY_CODE";
    public static final String AREA_COUNT = "AREA_COUNT";
    public static final String FARM_COUNT = "FARM_COUNT";
    public static Context appContext;
    public static final String SHARED_PREFERENCE_NAME = "FcmsSharedPref";
    public static final String LOGIN = "LOGIN";
    public static final String FARM_LONG= "FARM_LONG";
    public static final String FARM_LAT = "FARM_LAT";
    public static final String LOGIN_TYPE = "LOGIN_TYPE";
    public static final String COMP_ID = "COMP_ID";
    public static final String SEASON_NUM = "SEASON_NUM";
    public static final String PERSON_ID = "PERSON_ID";
    public static final String USER_ID = "USER_ID";
    public static final String FILTER_JSON = "FILTER_JSON";
    public static final String FILTER_NAMES = "FILTER_NAMES";
    public static final String SVCLUSTERID = "SVCLUSTERID";
    public static final String FARM_CLUSTERID = "FARM_CLUSTERID";
    public static final String IS_CLUSTER_SELECTED = "IS_CLUSTER_SELECTED";
    public static final String SVFARMID = "SVFARMID";
    public static final String UNITS = "UNITS";
    public static final String USERNAME = "USERNAME";
    public static final String USER_IMAGE = "USER_IMAGE";
    public static final String USER_NUMBER = "USER_NUMBER";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String PASSWORD = "PASSWORD";
    public static final String EXPECTED_AREA = "EXPECTED_AREA";
    public static final String CLUSTER_NAME = "CLUSTER_NAME";
    public static final String COMP_IMAGE = "COMP_IMAGE";
    public static final String COMP_BANNER = "COMP_BANNER";
    public static final String LAST_BAG_NO = "LAST_BAG_NO";
    public static final String FIRST_LOAD = "FIRST_LOAD";
    public static final String OFFLINE_MODE = "ONLINE_MODE";
    public static final String OFFLINE_MODE_DATE = "OFFLINE_MODE_DATE";
    public static final String CACHE_DATE = "CACHE_DATE";
    public static final String NO_OF_DAYS_OFFLINE = "NO_OF_DAYS_OFFLINE";
    //public static final String ONLINE = "ONLINE";
    public static final String VISIT_COUNT = "VISIT_COUNT";
    public static final String SHOWWELCOMESCREEN = "SHOWWELCOMESCREEN";
    public static final String STANDING_ACRES = "STANDING_ACRES";
    public static final String FARM_VETTING = "FARM_VETTING";
    public static final String ACTUAL_AREA = "ACTUAL_AREA";
    public static final String NOTIFICATION_FIRED = "NOTIFICATION_FIRED";
    public static final String ACTITY_ID = "ACTITY_ID";
    public  static final String FRAGMENT_FIRST_LOAD="FRAGMENT_FIRST_LOAD";
    public static final String DATE_COMPARE="DATE_COMPARE";
    public static final String VR_ID="VR_ID";
    public static final String LANGUAGECODE="LANGUAGECODE";
    public static final String ACT_TYPE_IMG="ACT_TYPE_IMG";
    public static final String ACT_LATEST_IMG="ACT_LATEST_IMG";
    public static final String COMPANY_NAME="COMPANY_NAME";
    public static final String LOT_NO="LOT_NO";
    public static final String FARMER_NAME="FARMER_NAME";
    public static final String KHASRA="KHASRA";
    public static final String NOTI_WV_LINK = "NOTI_WV_LINK";

    public static final String GERMINATION = "GERMINATION";
    public static final String AUTHORIZATION = "AUTHORIZATION";
    public static final String FARMER_FARM_IDS = "FARMER_FARM_IDS";
        /*public static final String SPACING_PTP = "SPACING_PTP";
       public static final String SPACING_RTR = "SPACING_RTR";
        public static final String POPULATION = "POPULATION";*/

    public static final String AREA_UNIT_LABEL = "AREA_UNIT_LABEL";
    public static final String AREA_MULTIPLICATON_FACTOR = "AREA_MULTIPLICATON_FACTOR";


    //Comp Registry Variables
    public static final String IS_ONLY_SOIL_SENS = "soilsens";
    public static final String IS_SOIL_SENS_ENABLED = "soilsens_data_enabled";
    public static final String IS_WAY_POINT_ENABLED = "way_point_enabled";
    public static final String IS_OMITANCE_AREA_ENABLED = "omitance_area_enabled";
    public static final String COMP_DATE_FORMAT = "date_format";
    public static final String IS_PREVIOUS_CROP_MANDATORY = "prev_crop_compulsory";
    public static final String IS_CROSS_VETTING_ENABLED = "cross_vetting";
    public static final String IS_DELINQUENT_COMP = "Agri-Financer";

    public static final String IS_VETTING_COMP = "Farm Vetting";
    public static final String HARVEST_ALLOWED="HARVEST_ALLOWED";
    public static final String INPUT_COST_ALLOWED="INPUT_COST_ALLOWED";
    public static final String VISIT_ALLOWED="VISIT_ALLOWED";
    public static final String SEED_ALLOWED="SEED_ALLOWED";
    public static final String EXPENSE__ALLOWED="EXPENSE__ALLOWED";
    public static final String SUPERVISOR_ALLOWED_ADD_FARM="SUPERVISOR_ALLOWED_ADD_FARM";
    public static final String FARMER_ALLOWED_ADD_FARM="FARMER_ALLOWED_ADD_FARM";
    public static final String PLD_ALLOWED="PLD_ALLOWED";
    public static final String ADD_HC="ADD_HC";
    public static final String ASSIGN_CALENDAR="ASSIGN_CALENDAR";
    public static final String GERMINATION_ALLOWED="GERMINATION_ALLOWED";
    public static final String FARMER_ALLOWED_ADD_ACTIVITY="FARMER_ALLOWED_ADD_ACTIVITY";
    public static final String IS_INPUT_COST ="Farmer Expense Tracking";
    public static final String FARMER_ALLOWED_UPDATE_FARM_DETAILS="FARMER_ALLOWED_UPDATE_FARM_DETAILS";


    public static final String APPTOUR = "APPTOUR";
    public static final String IS_DATA_CHANGED_FARM = "IS_DATA_CHANGED_FARM";

    public static final String FILTER_SV_TIMELINE_FROM_DATE = "FILTER_SV_TIMELINE_FROM_DATE";
    public static final String FILTER_SV_TIMELINE_TO_DATE = "FILTER_SV_TIMELINE_TO_DATE";
    public static final String SV_TIMELINE_SYNC_1HOUR = "SV_TIMELINE_SYNC_1HOUR";

    public static final String IS_ON_FARM_PAGE = "IS_ON_FARM_PAGE";
    public static final String FARM_AREA_POLYGON = "FARM_AREA_POLYGON";
    public static final String CASE_ID = "CASE_ID";

    public static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        return editor;
    }

    public static SharedPreferences getSharedPreference(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedpreferences;
    }

    public static boolean getBoolean(Context context, String name) {
        if (context!=null) {
            SharedPreferences sharedPreferences = getSharedPreference(context);
            return sharedPreferences.getBoolean(name, false);
        }else  return false;
    }

    public static void setStringSharedPreferencehistory(Context context, String name, Set<String> value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putStringSet(name, value);
        editor.commit();
    }

    public static Set<String> getStringSharedPreferenceshistory(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return settings.getStringSet(name, null);
    }

    public static void setBoolean(Context context, String name, boolean value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static String getString(Context context, String name) {
        if (context!=null) {
            SharedPreferences sharedPreferences = getSharedPreference(context);
            return sharedPreferences.getString(name, "");
        }else return "";
    }

    public static void setString(Context context, String name, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(name, value);
        editor.commit();
    }

    public static int getInt(Context context, String name) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        return sharedPreferences.getInt(name, 0);
    }

    public static void setInt(Context context, String name, int value) {
        //value=value+1;
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(name, value);
        editor.commit();
    }

    // for username string preferences
    public static void setDoubleSharedPreference(Context context, String name, double value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(name, (float) value);
        editor.commit();
    }

    public static Double getDoubleSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return (double) settings.getFloat(name, 0.0f);
    }


    public static void setFloatPref(Context context, String name, float value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(name, (float) value);
        editor.commit();
    }

    public static Float getFloatPref(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return  settings.getFloat(name, 0.0f);
    }
    public static void setLongSharedPreference(Context context, String name, long value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(name, value);
        editor.commit();
    }

    public static long getLongSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return settings.getLong(name, 0l);
    }




    public static void clear(Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.remove(SharedPreferencesMethod.SHARED_PREFERENCE_NAME);
        editor.clear();
        editor.commit();
        context.getSharedPreferences(SharedPreferencesMethod.SHARED_PREFERENCE_NAME, 0).edit().clear().commit();
    }


}