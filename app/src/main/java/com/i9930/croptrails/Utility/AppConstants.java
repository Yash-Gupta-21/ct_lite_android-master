package com.i9930.croptrails.Utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.geojson.GeoJsonLineString;
import com.google.maps.android.data.geojson.GeoJsonMultiLineString;
import com.google.maps.android.data.geojson.GeoJsonMultiPoint;
import com.google.maps.android.data.geojson.GeoJsonMultiPolygon;
import com.google.maps.android.data.geojson.GeoJsonPoint;
import com.google.maps.android.data.geojson.GeoJsonPolygon;

import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import com.i9930.croptrails.AddFarm.Share;
import com.i9930.croptrails.R;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegModel;

import static java.lang.Double.parseDouble;

public class AppConstants {
    //    Actual Area==Geo Tagged area;
//    exp area= Quoted area;
    public enum FILE_PATH_TYPE {
        VIST, ACTIVITY, PLD,AUDIO,DOCUMENT,OTHER;
    }

    public static String DATE_FORMAT_SHOW_FULL = "hh:mm:ss dd/MM/yyyy";
    public static String  convertDateTimeToLocal(String serverDateTime){
        String dateStr = serverDateTime;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(dateStr);
            df.setTimeZone(TimeZone.getDefault());
            String formattedDate = df.format(date);
            Log.e("convertDateTimeToLocal","From: "+serverDateTime+", To: "+formattedDate);
            return  getShowableDateTime("yyyy-MM-dd hh:mm:ss",formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return serverDateTime;
        }
    }

    private static String getShowableDateTime(String serverFormat,String date) {
        String newDate = date;
        String format = null;
        if (format == null || TextUtils.isEmpty(format))
            format = DATE_FORMAT_SHOW_FULL;
        if (newDate != null && !date.equals("0000-00-00")) {
            SimpleDateFormat submit_sdf = new SimpleDateFormat(format, Locale.US);
            Date exp_date_in_date = null;
            try {
                exp_date_in_date = new SimpleDateFormat(serverFormat).parse(date.trim());
                newDate = submit_sdf.format(exp_date_in_date);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("AppConstants", "Date Ecx " + e.toString());
                newDate = date;
            } catch (Exception e) {
                e.printStackTrace();
                newDate = date;
            }
        } else
            newDate = "-";
        Log.e("AppConstants", "Date " + newDate + " old date " + date);
        return newDate;
    }

    public static String COMP_ID="100139";
    public static final int OLD_VERSION_STATUS = 9;
    public static String getCalculatedDate( int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(DATE_FORMAT_SERVER);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    public static String getBeforeDate() {
        return  getCalculatedDate(-7);
    }

    public static String getAfterDate() {
        return  getCalculatedDate(7);
    }

    public interface SV_TIMELINE_CARD_TYPES{
        public static int TASK=1;
        public static int FARM=2;
        public static int EXPENSE=3;
    }

    public static String getFullPath(FILE_PATH_TYPE type, Context activity,String fileName) {
        String storagePath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+Environment.DIRECTORY_PICTURES;
        String path = storagePath+"/" + activity.getResources().getString(R.string.app_name);
        if (type == null) {
            return path;
        } else if (type == FILE_PATH_TYPE.VIST) {
            path = path + "/Visit";
        } else if (type == FILE_PATH_TYPE.ACTIVITY) {
            path = path + "/Activities";
        } else if (type == FILE_PATH_TYPE.PLD) {
            path = path + "/Damage";
        } else if (type == FILE_PATH_TYPE.AUDIO) {
            path = path + "/Audio";
        } else if (type == FILE_PATH_TYPE.DOCUMENT) {
            path = path + "/Document";
        }
        else if (type == FILE_PATH_TYPE.OTHER) {
            path = path + "/Other";
        }
        return path+"/"+fileName;
    }

    public static String getImagePath(FILE_PATH_TYPE type, Activity activity) {
        String path = "/" + activity.getResources().getString(R.string.app_name);
        if (type == null) {
            return path;
        } else if (type == FILE_PATH_TYPE.VIST) {
            path = path + "/visit";
        } else if (type == FILE_PATH_TYPE.ACTIVITY) {
            path = path + "/activities";
        } else if (type == FILE_PATH_TYPE.PLD) {
            path = path + "/damage";
        }
        return path;
    }

    public interface FILE_PATH {
        public static String CT = "/" + R.string.app_name;
        public static final String ACTIVITIES = CT + "/activities";
        public static final String VISIT = CT + "/visit";
        public static final String PLD = CT + "/damage";
    }

    public interface POLYGON {
        public static final int FILL = Color.parseColor("#fbca02");
        public static final int FILL_FARM = Color.parseColor("#330000FF");
        public static final int STROKE = Color.RED;
//        public static final int ADDRESS_PROOF = 3;
//        public static final int ID_PROOF = 4;
    }

    public interface MAP_NAV_MODE {
        public static final int SHOW_AREA = 1;
        public static final int SHOW_NAVIGATION = 2;
    }

    public interface VETTING {
        public static final String FRESH = "0";
        public static final String SELECTED = "1";
        public static final String DATA_ENTRY = "3";
        public static final String REJECTED = "2";
        public static final String DELINQUENT = "4";
    }

    public interface AREA_TYPE {
        public static final String Country = "Y";
        public static final String State = "S";
        public static final String Project = "P";
        public static final String Zone = "Z";
        public static final String Block = "B";

        public static final String District = "D";
        public static final String Tehsil = "T";
        public static final String Village = "V";
        public static final String Chak = "C";
        public static final String Farm = "F";
        public static final String Waypoint = "W";
        public static final String Omittance = "O";
    }

    public interface DOCS {
        public static final int OWNERSHIP = 1;
        public static final int FARM = 2;
        public static final int ADDRESS_PROOF = 3;
        public static final int ID_PROOF = 4;
        public static final int PROFILE = 5;
    }

    public interface GPS3_TYPE {
        public static final String WAY_POINT = "W";
        public static final String OMITANCE_AREA = "O";

    }

    public interface ROLES {

        public static final String FARMER = "farmer";
        public static final String SUPERVISOR = "supervisor";
        public static final String ADMIN = "admin";
        public static final String CLUSTER_ADMIN = "Cluster Admin";
        public static final String SUPER_ADMIN = "Super-Admin";
        public static final String SUB_ADMIN = "Sub-admin";
    }

    public static final String for_date = "1900-01-01 00:00:00";

    //Notification keys to get data from data
    public static final String NOTIFICATION_KEY_SH_ID = "noti_sch_id";
    public static final String NOTIFICATION_KEY_TITLE = "title";
    public static final String NOTIFICATION_KEY_MESSAGE = "message";
    public static final String NOTIFICATION_KEY_EXTRA_MSG = "extra_message";
    public static final String NOTIFICATION_KEY_IMAGE_LINK = "image_link";
    public static final String NOTIFICATION_KEY_CAL_ACTIVITY_ID = "farm_cal_activity_id";
    public static final String NOTIFICATION_KEY_LANDING_INTENT = "landing_intent_id";
    public static final String NOTIFICATION_KEY_COMP_ID = "comp_id";
    public static final String NOTIFICATION_KEY_PERSON_ID = "user_id";

    public static final String NOTIFICATION_KEY_SHEDULE_DATE = "scheduled_date";
    public static final String NOTIFICATION_KEY_IS_SENT = "is_sent";
    public static final String NOTIFICATION_KEY_IS_READ = "is_read";
    public static final String NOTIFICATION_KEY_LINK = "link";
    public static final String NOTIFICATION_KEY_LANDING_TITLE = "landing_title";
    public static final String NOTIFICATION_KEY_DESCRIPTION = "description";
    public static final String NOTIFICATION_KEY_IMAGE_LINK_2 = "img_link";
    public static final String NOTIFICATION_KEY_ACTIVITY = "activity";//Timeline,Landing,Weather
    public static final String NOTIFICATION_KEY_TYPE = "type";//Open in App
    public static final String NOTIFICATION_KEY_KEY_TYPE = "key_type";//in App

    public interface CHEMICAL_UNIT {
        public static final String UNIT = "UNIT";
        //        public static final String VISIT_CHEMICALS = "VISIT_CHEMICALS";
        public static final String COMPANY_CHEMICALS = "COMPANY_CHEMICALS";
        public static final String COMPANY_UNITS = "COMPANY_UNITS";
        public static final String RESOURCE_USED = "RESOURCE_USED";
        public static final String INPUT_COST = "INPUT_COST";
        public static final String FARM_IRRI_SOURCE = "FARM_IRRI_SOURCE";
        public static final String FARM_IRRI_TYPE = "FARM_IRRI_TYPE";
        public static final String FARM_SEASON = "FARM_SEASON";
        public static final String FARM_SEASON_NEW = "FARM_SEASON_NEW";
        public static final String FARM_SOIL_TYPE = "FARM_SOIL_TYPE";
        public static final String COUNTRIES = "COUNTRIES";
        public static final String CROP_LIST = "CROP_LIST";
        public static final String CHAT = "CHAT";


        public static final String FARM_IRRI_TYPE_NEW = "irrigation type";
        public static final String FARM_IRRI_SOURCE_NEW = "irrigation source";
        public static final String DATE_FORMAT = "date_format";
        public static final String AREA_UNIT = "area_units";
        public static final String FARM_SOIL_TYPE_NEW = "soil_type_master";
        public static final String CROP_LIST_NEW = "crop_master";
        public static final String CIVIL_STATUS = "civil_status";
        public static final String CROPPING_PATTERN = "cropping_pattern";
        public static final String PLANTING_METHOD = "planting_method";
        public static final String LAND_CATEGORY = "land_category";
        public static final String PLD_REASON = "PLD_REASON";
        public static final String FARMER_LIST = "FARMER_LIST";
        public static final String SUPERVISOR_LIST = "SUPERVISOR_LIST";
        public static final String CLUSTER_LIST = "CLUSTER_LIST";
        public static final String COMP_AGRI_INPUTS = "COMP_AGRI_INPUTS";
        public static final String FARM_ADD_DYNAMIC = "FARM_ADD_DYNAMIC";
        public static final String DELINQUENT = "delq_reason";
        public static final String CASTE_CATEGORY = "caste_category";
        public static final String CASTE = "caste";
        public static final String RELIGION = "religion ";
        public static final String MOBILE_NETWORK_OPERATOR = "mobile_network_operator";
        public static final String MOTOR_HP = "motor_hp";
        public static final String GPS_ACCURACY = "gps_accuracy";
        public static final String MICRO_IRRI_AWARENESS = "mia";


    }

    public interface NAVIGATE {
        public static final String WEATHER = "Weather";
        public static final String TIMELINE = "Timeline";
        public static final String UPDATE_PROFILE = "UpdateProfile";
        public static final String COMP_REG = "COMP_REG";
    }

    public interface OPEN_IN {
        public static final String IN_APP = "inApp";
        public static final String IN_DIALOG = "dialog";
        public static final String SIMPLE_MSG = "simpleMsg";
        public static final String LINK = "link";
    }

    public interface LOCATION_ALARM {
        public static final int LOCATION_REQ_CODE_FETCH = 938;
        public static final int LOCATION_REQ_CODE_UPLOAD = 940;
    }


    public interface TIMELINE {

        public static String ACT = "Activity";
        public static String SOIL_SENSE = "SOIL_SENSE";
        public static String HR = "Harvest";
        public static String VR = "Visit";
        public static String DELINQUENT = "DELINQUENT";
        public static String GE = "Population";
        public static String IC = "Input Cost";
        public static String RU = "Resource Used";
        public static String HC = "Health Card";
        public static String WR = "Weather";
        public static String FARM = "Farm";
        public static String SAT = "Satellite";
        public static String PLD = "Loss / Damage";
        public static String SELL = "Sell Record";
        public static String ADVERTISE = "ADVERTISE";

    }


    public interface GERMINATION {

        public static final String KEY = "GERMINATION_KEY";

        public static final String FIRST_ADD = "FIRST_ADD";
        public static final String MULTIPLE_ADD = "MULTIPLE_ADD";


    }

    public static String getCurrentDateTime() {
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String datetime = dateformat.format(c.getTime());

            return datetime;
        } catch (Exception e) {
            return null;
        }

    }

    public static String getCurrentDate() {
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            String datetime = dateformat.format(c.getTime());

            return datetime;
        } catch (Exception e) {
            return null;
        }

    }

    public static long getCurrentMills() {
        return System.currentTimeMillis();
    }

    public static int DELAY = 3000;
    public static int DELAY_API = 5000;

    public static String DATE_FORMAT_SERVER = "yyyy-MM-dd";
    public static String DATE_FORMAT_SHOW = "dd/MM/yyyy";


    public static String getShowableDate(String date, Context context) {
        String newDate = date;
        String format = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_DATE_FORMAT);
        if (format == null || TextUtils.isEmpty(format))
            format = DATE_FORMAT_SHOW;
        if (newDate != null && !date.equals("0000-00-00")) {
            SimpleDateFormat submit_sdf = new SimpleDateFormat(format, Locale.US);
            Date exp_date_in_date = null;
            try {
                exp_date_in_date = new SimpleDateFormat(DATE_FORMAT_SERVER).parse(date.trim());
                newDate = submit_sdf.format(exp_date_in_date);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("AppConstants", "Date Ecx " + e.toString());
                newDate = date;
            } catch (Exception e) {
                e.printStackTrace();
                newDate = date;
            }
        } else
            newDate = "-";
        Log.e("AppConstants", "Date " + newDate + " old date " + date);
        return newDate;
    }


    public static Date getDateObjFromServerFormat(String date) {
        String newDate = date;

        if (newDate != null && !date.equals("0000-00-00")) {
            SimpleDateFormat submit_sdf = new SimpleDateFormat(DATE_FORMAT_SHOW, Locale.US);
            Date exp_date_in_date = null;
            try {
                exp_date_in_date = new SimpleDateFormat(DATE_FORMAT_SERVER).parse(date.trim());
                return exp_date_in_date;

            } catch (ParseException e) {
                e.printStackTrace();
                return  null;
            } catch (Exception e) {
                e.printStackTrace();
                return  null;
            }
        } else
           return  null;
    }

    public static String getUploadableDate(String date, Context context) {
        String newDate = date;
        SimpleDateFormat submit_sdf = new SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US);
        String format = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_DATE_FORMAT);

        if (format == null || TextUtils.isEmpty(format))
            format = DATE_FORMAT_SHOW;
        Date exp_date_in_date = null;
        try {
            exp_date_in_date = new SimpleDateFormat(format).parse(date.trim());
            newDate = submit_sdf.format(exp_date_in_date);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("getUploadableDate", "d1 " + date + ", server format " + submit_sdf.toPattern() + ", comp format " + format + " new date " + newDate);
        return newDate;
    }

    public static void setDefaultUnit(Context context) {

        if (SharedPreferencesMethod.getDoubleSharedPreferences(context, SharedPreferencesMethod.AREA_MULTIPLICATON_FACTOR) <= 0) {
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.AREA_UNIT_LABEL, "acres");
            SharedPreferencesMethod.setDoubleSharedPreference(context, SharedPreferencesMethod.AREA_MULTIPLICATON_FACTOR, 1.0);
        }
    }

    public interface VISIT_ACTIVITY {
        public static String INM = "INM";
        public static String IWM = "IWM";
        public static String IPM = "IPM";
        public static String CPC = "CPC";
        public static String SML = "SML";
        public static String OTHER = "OTHER";
        public static String STANDING_AREA = "STANDING_AREA";
        public static String AGRI_INPUT = "AGRI_INPUT";
    }

    public interface ADD_FARM {
        public static final String KEY = "KEY";
        public static final String ADD_EXISTING_FARM = "ADD_EXISTING_FARM";
        public static final String NEW_FARM_AND_FARMER = "NEW_FARM_AND_FARMER";
    }

    public static String getShowableArea(String area, Double multiplyFactor) {
        String convertArea = area;
        Double d = parseDouble(area) * multiplyFactor;
        convertArea = String.format(Locale.ENGLISH, "%.4f", d);
        Log.e("Conversion Show", "Acres =" + area + ", converted= " + convertArea);
//        return convertArea;
        return area;
    }

    public static String getShowableArea(String area) {
        String convertArea = area;
        Double d = parseDouble(area);
        convertArea = String.format(Locale.ENGLISH, "%.4f", d);
        Log.e("Conversion Show", "Acres =" + area + ", converted= " + convertArea);
        return convertArea;
    }

    public static String getShowableAreaWithConversion(String area, Double multiplyFactor) {
        String convertArea = area;
        Double d = parseDouble(area) / multiplyFactor;
        convertArea = String.format(Locale.ENGLISH, "%.4f", d);
        Log.e("AREA", "Acres =" + area + ", converted= " + convertArea);
        return convertArea;
//        return area;
    }

    public static String getUploadableArea(String area, Double multiplyFactor) {
        String convertArea = area;
        Double d = parseDouble(area) * multiplyFactor;
        convertArea = String.format(Locale.ENGLISH, "%.4f", d);
        Log.e("Conversion Show", "2 Acres =" + area + ", converted= " + convertArea);
//        return convertArea;
        return area;
    }

    public static String getUploadableArea(String area) {
        String convertArea = area.trim();
        if (convertArea != null && !TextUtils.isEmpty(convertArea)) {
            if (convertArea.charAt(0) == '.') {
                convertArea = "0" + convertArea;
            }

            Double d = parseDouble(convertArea.trim());
            convertArea = String.format(Locale.ENGLISH, "%.4f", d);
            Log.e("Conversion Show", "2 Acres =" + area + ", converted= " + convertArea);
//        return convertArea;
        }
        return convertArea;
    }

   /* public static String getUploadableArea(String area, Double multiplyFactor) {
        if (area != null && !TextUtils.isEmpty(area)) {
            String convertArea = area;
            Double d = parseDouble(area) / multiplyFactor;
            convertArea = String.valueOf(d);
            return convertArea;
        } else {
            return "0.0";
        }
    }*/

    public static String getTodaysDate() {

        String todayDateStr = null;
        try {
            Date c;
            SimpleDateFormat df;
            c = Calendar.getInstance().getTime();
            df = new SimpleDateFormat(DATE_FORMAT_SERVER);
            todayDateStr = df.format(c);
        } catch (Exception e) {
        }

        return todayDateStr;
    }

    public static long getDifference(String fromDate){
        String newDate = fromDate;
        if (newDate != null && !fromDate.equals("0000-00-00")) {
            SimpleDateFormat submit_sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
            Date exp_date_in_date = null;
            try {
                exp_date_in_date = submit_sdf.parse(fromDate.trim());
                Calendar calendar=Calendar.getInstance();
                long miliSeconds = calendar.getTimeInMillis() - exp_date_in_date.getTime();
                long seconds = TimeUnit.MILLISECONDS.toSeconds(miliSeconds);
                long minute = seconds/60;
                long hour = minute/60;
                return hour;

            } catch (ParseException e) {
                e.printStackTrace();
                return 2;
            } catch (Exception e) {
                e.printStackTrace();
                return 2;
            }
        } else
            return 2;

    }

    public static Date getTodaysDateObj() {
        try {
            Date c;
            SimpleDateFormat df;
            c = Calendar.getInstance().getTime();
            return c;
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getDateObj(String date) {
        String newDate = date;
        String format = null;
        if (format == null || TextUtils.isEmpty(format))
            format = DATE_FORMAT_SERVER;
        if (newDate != null && !date.equals("0000-00-00")) {
            SimpleDateFormat submit_sdf = new SimpleDateFormat(format, Locale.US);
            Date exp_date_in_date = null;
            try {
                return exp_date_in_date = new SimpleDateFormat(DATE_FORMAT_SERVER).parse(date.trim());
            } catch (ParseException e) {
                e.printStackTrace();
                return null;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else
            return null;
    }

    public String getAgeFromYOB(String yob) {

        String yb = yob;

        try {
            Calendar c = Calendar.getInstance();

            yb = "" + c.get(Calendar.YEAR) + Integer.valueOf(yob);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return yb;

    }

    public static String getCapsWords(String string) {

        if (string == null) {
            return null;
        }
        String[] strArray = string.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }
        return builder.toString();
    }

    public static String getCapSentence(String name){
        if (isValidString(name)) {
            String captilizedString = "";
            if (!name.trim().equals("")) {
                captilizedString = name.substring(0, 1).toUpperCase() + name.substring(1);
            }
            return captilizedString;
        }else{
            return  "";
        }
    }
    public static boolean isPointInPolygon(LatLng tap, List<LatLng> vertices) {
        int intersectCount = 0;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }
        boolean flag = ((intersectCount % 2) == 1); // odd = inside, even = outside;
//        Log.e("TimelineAddVisitActivit","flag "+flag);

        return flag;
    }

    private static boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {

        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = tap.latitude;
        double pX = tap.longitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY)
                || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or
            // b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX); // Rise over run
        double bee = (-aX) * m + aY; // y = mx + b
        double x = (pY - bee) / m; // algebra is neat!

        return x > pX;
    }

    public interface API_STATUS {

        public static final String KEY = "DeviceKey";
        public static final int SUCCESS = 1;
        public static final int FAIL = 0;
        public static final int SESSION_END = 10;

        public static final int API_SUCCESS = 200;
        public static final int API_BAD_REQUEST = 400;
        public static final int API_UNAUTH_ACCESS = 401;
        public static final int API_AUTH_EXPIRED = 403;
        public static final int API_NOT_FOUND = 404;
        public static final int API_INTERNAL_SERVER = 500;
        public static final int API_TIMEOUT = 504;
        public static final int API_NOT_IMPLEMENTED = 501;

    }

    public interface MAP_AREA_SUBMIT_TYPE {

        public static final String TYPE = "TYPE";
        public static final String OMITANCE = "OMITANCE";
        public static final String WAY_POINT = "WAY_POINT";
        public static final String ACTUAL_AREA = "ACTUAL_AREA";
    }

    public interface COMP_REG {
        public static final boolean DEFAULT = true;
        public static final String CROSS_VETTING = "cross_vetting";
        public static final String AGRI_FINANCER = "Agri-Financer";
        public static final String SOIL_SENS_ONLY = "soilsens";
        public static final String DATE_FORMAT = "date_format";
        public static final String IS_PREVIOUS_CROP_MANDATORY = "prev_crop_compulsory";
        public static final String SOIL_SENS_ENABLED = "soilsens_data_enabled";
        public static final String WAY_POINT_ENABLED = "way_point_enabled";
        public static final String OMITANCE_AREA_ENABLED = "omitance_area_enabled";

        public static final String CROP_CYCLE = "Crop Cycle / Calendar";
        public static final String CROP_TRACING = "Crop Tracing";
        public static final String ECOM_LISTING = "Ecomtrails Listing";
        public static final String IS_VETTING_FARM = "Farm Vetting";
        public static final String FARMER_EXPENSE = "Farmer Expense Tracking";
        public static final String GEO_LOCKING = "GeoLocking";
        public static final String HARVEST_SELL = "Harvest Selling";
        public static final String INPUT_RESOURCE_MANAGEMENT = "Input Resource Management";
        public static final String PROCUREMENT_PLANNING = "Procurement Planning";
        public static final String SOIL_HEALTH_CARD = "Soil Health Card";
        public static final String SUB_ADMIN = "Sub Admin";
        public static final String SUPERVISOR_TRACKING = "Supervisor Tracking";
        public static final String VISIT_REPORT = "Visit Reports";
        public static final String FARMER_PERSONAL_ID = "Farmer Personal ID";
        public static final String FARMER_PERSONAL_INFO = "Farmer Personal Info";
        public static final String FARMER_CONTACT_INFO = "Farmer Contact Info";
        public static final String FARMER_FAMILY_INFO = "Farmer Family Info";
        public static final String FARMER_FINANCIAL_INFO = "Farmer Financial Info";
        public static final String FARMER_SOIL_INFO = "Farmer Social info";
        public static final String FARM_DETAILS = "Farm Details";
        public static final String FARM_DEMOGRAPHICS = "Farm Demographics";
        public static final String FARM_GPS_DATA = "Farm GPS Data";
        public static final String FARM_CROP = "Farm Crop";
        public static final String FARM_CROP_INSURANCE = "Farm Crop Insurance";
        public static final String FARM_CROP_LOAN = "Farm Crop Loan";

        //Add farm dynamic forms
        public static final String ANIMAL_HUSBANDRY = "Animal Husbandry";
        public static final String EQUIPMENT_MACHINERY = "Equipment / Machinery";
        public static final String SEED = "Seed";
        public static final String FERTILIZER_ORGANICS = "Fertilizers / Organic";
        public static final String PESTICIDES = "Pesticides";
        public static final String WORKERS = "Workers";
        public static final String TRACTOR = "Tractor";
        public static final String HARVESTER = "Harvester";
        public static final String PESTILENCE = "Pestilence";
        public static final String PRODUCT_SALES = "Product / Sales";

    }

    public static int getRandomInt(int from, int to) {
        Random random = new Random();
        return random.nextInt(to - from) + from;

    }

    public static void failToast(Context context, String msg) {
        if (context != null)
            Toasty.error(context, msg, Toast.LENGTH_LONG).show();
    }
    public static void successToast(Context context, String msg) {
        if (context != null)
            Toasty.success(context, msg, Toast.LENGTH_LONG).show();
    }


    public static boolean isValidString(String string) {
        if (string == null || TextUtils.isEmpty(string))
            return false;
        else return true;
    }

    public static boolean isValidActualArea(String string) {
        try {
            if (string == null || TextUtils.isEmpty(string))
                return false;
            else if (Float.valueOf(string.trim()) > 0) {
                return true;
            } else return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static double meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
        float pk = (float) (180.f / Math.PI);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }

    public static LatLng getCenterOfPolygon(List<LatLng> latLngList) {
        double[] centroid = {0.0, 0.0};
        for (int i = 0; i < latLngList.size(); i++) {
            centroid[0] += latLngList.get(i).latitude;
            centroid[1] += latLngList.get(i).longitude;
        }
        int totalPoints = latLngList.size();
        return new LatLng(centroid[0] / totalPoints, centroid[1] / totalPoints);
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    //res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public static List<LatLng> getCoordinatesFromGeometry(Geometry geometry) {

        List<LatLng> coordinates = new ArrayList<>();

        // GeoJSON geometry types:
        // http://geojson.org/geojson-spec.html#geometry-objects

        switch (geometry.getGeometryType()) {
            case "Point":
                coordinates.add(((GeoJsonPoint) geometry).getCoordinates());
                break;
            case "MultiPoint":
                List<GeoJsonPoint> points = ((GeoJsonMultiPoint) geometry).getPoints();
                for (GeoJsonPoint point : points) {
                    coordinates.add(point.getCoordinates());
                }
                break;
            case "LineString":
                coordinates.addAll(((GeoJsonLineString) geometry).getCoordinates());
                break;
            case "MultiLineString":
                List<GeoJsonLineString> lines =
                        ((GeoJsonMultiLineString) geometry).getLineStrings();
                for (GeoJsonLineString line : lines) {
                    coordinates.addAll(line.getCoordinates());
                }
                break;
            case "Polygon":
                List<? extends List<LatLng>> lists =
                        ((GeoJsonPolygon) geometry).getCoordinates();
                for (List<LatLng> list : lists) {
                    coordinates.addAll(list);
                }
                break;
            case "MultiPolygon":
                List<GeoJsonPolygon> polygons =
                        ((GeoJsonMultiPolygon) geometry).getPolygons();
                for (GeoJsonPolygon polygon : polygons) {
                    for (List<LatLng> list : polygon.getCoordinates()) {
                        coordinates.addAll(list);
                    }
                }
                break;
        }

        return coordinates;
    }

    public static List<List<LatLng>> getCoordinatesFromGeometryMultiPoly(Geometry geometry) {

        List<List<LatLng>> coordinates = new ArrayList<>();
        switch (geometry.getGeometryType()) {

            case "MultiPolygon":
                List<GeoJsonPolygon> polygons =
                        ((GeoJsonMultiPolygon) geometry).getPolygons();
                for (GeoJsonPolygon polygon : polygons) {
                    if (polygon.getCoordinates() != null)
                        coordinates.addAll(polygon.getCoordinates());

                }
                break;
        }

        return coordinates;
    }

    public static String getCountryId(Context c) {
        TelephonyManager tm = (TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE);
        String networkCountry = tm.getNetworkCountryIso();
        return networkCountry;
    }
}
