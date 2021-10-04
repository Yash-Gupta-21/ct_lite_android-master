package com.i9930.croptrails.Utility;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.internet_speed_testing.InternetSpeedBuilder;
import com.example.internet_speed_testing.ProgressionModel;
import com.i9930.croptrails.R;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class ConnectivityUtils {

    private static String TAG = "ConnectivityUtils";

    /**
     * Get the network info
     *
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        if (context!=null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo();
        }else return null;
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        if (context!=null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                boolean isConn = false;
                if (cm != null) {
                    NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());

                    if (capabilities != null) {
                        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            isConn = true;
                        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            isConn = true;
                        }
                    }
                }

                return isConn;
            } else {
                NetworkInfo info = ConnectivityUtils.getNetworkInfo(context);
                if ((info != null && info.isConnected()))
                    return true;
                else return false;
            }

        }else return false;

    }
    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = ConnectivityUtils.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = ConnectivityUtils.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public static boolean isConnectedFast(Context context) {
        NetworkInfo info = ConnectivityUtils.getNetworkInfo(context);
        return (info != null && info.isConnected() && ConnectivityUtils.isConnectionFast(info.getType(), info.getSubtype()));
    }

    public static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }


    public static void checkSpeed(Activity activity, TextView speedTv, boolean mustShow,int count) {
        try {
            InternetSpeedBuilder builder = new InternetSpeedBuilder(activity);
            builder.setOnEventInternetSpeedListener(new InternetSpeedBuilder.OnEventInternetSpeedListener() {
                @Override
                public void onDownloadProgress(int count, final ProgressionModel progressModel) {
                /*Log.d(TAG, "SERVER " + "" + progressModel.getDownloadSpeed());
                java.math.BigDecimal bigDecimal = new java.math.BigDecimal("" + progressModel.getDownloadSpeed());
                float finalDownload = (bigDecimal.longValue() / 1000000);
                Log.d(TAG, "NET_SPEED " + "" + (float) (bigDecimal.longValue() / 1000000));
                java.math.BigDecimal bd = progressModel.getDownloadSpeed();
                final double d = bd.doubleValue();
                String speed = formatFileSize(d);
                Log.d("SHOW_SPEED", "" + speed);
                Log.d(TAG, "ANGLE " + "" + getPositionByRate(finalDownload));
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!mustShow && (speed.contains("mb/s"))) {
                            if (speedTv != null) {
                                speedTv.setVisibility(View.GONE);
                                Log.d(TAG, "1 " + "One");
                            } else
                                Log.d(TAG, "1 " + "Zero");

                        } else if (!mustShow && (speed.contains("kb/s") && Float.valueOf(speed.split(" ")[0]) >= 200.0)) {
                            if (speedTv != null) {
                                speedTv.setVisibility(View.GONE);
                                Log.d(TAG, "2 " + "One");
                            } else
                                Log.d(TAG, "2 " + "Zero");
                        } else if (!mustShow && (speed.contains("kb/s") || Float.valueOf(speed.split(" ")[0]) >= 100.0)) {
                            if (speedTv != null) {
								Log.d(TAG,"3 "  + "One");
                                speedTv.setText("" + speed);
                                speedTv.setTextColor(activity.getColor(android.R.color.holo_orange_light));
                            } else {
								Log.d(TAG,"3 "  + "Zero");
                                Toasty.error(activity, "Poor internet connectivity", Toast.LENGTH_SHORT, false).show();
                            }
                        } else if (!mustShow && (speed.contains("kb/s") || Float.valueOf(speed.split(" ")[0]) <= 50.0)) {
                            if (speedTv != null) {
								Log.d(TAG,"4 "  + "One");
                                speedTv.setText("" + speed);
                                speedTv.setTextColor(activity.getColor(android.R.color.holo_red_dark));
                            } else {
								Log.d(TAG,"4 "  + "Zero");
                                Toasty.error(activity, "Poor internet connectivity", Toast.LENGTH_SHORT, false).show();
                            }
                        } else if ((mustShow && speed.contains("mb/s")) || mustShow && (speed.contains("kb/s") && Float.valueOf(speed.split(" ")[0]) > 200.0)) {
                            if (speedTv != null) {
								Log.d(TAG,"5 "  + "One"+speed);
                                speedTv.setText("" + speed);
                                speedTv.setTextColor(activity.getColor(android.R.color.holo_green_dark));
                                speedTv.setVisibility(View.VISIBLE);
                            }else
								Log.d(TAG,"1 "  + "Zero");
                        } else {
                            if (speedTv != null) {
								Log.d(TAG,"6 "  + "One");
                                speedTv.setText("" + speed);
//									speedTv.setTextColor(android.R.color.holo_green_dark);
                            }
                            else Log.d(TAG,"6 "  + "Zero");
                        }
                    }
                });*/

                }

                @Override
                public void onUploadProgress(int count, final ProgressionModel progressModel) {

                    //double speed = progressModel.getUploadSpeed()/((Double)1000000);
               /* java.math.BigDecimal bigDecimal = new java.math.BigDecimal(""+progressModel.getUploadSpeed());
                float finalDownload = (bigDecimal.longValue()/1000000);
                Log.d(TAG,"UP NET_SPEED " + ""+(float)(bigDecimal.longValue()/1000000));
                java.math.BigDecimal bd = progressModel.getUploadSpeed();
                final double d = bd.doubleValue();
                Log.d(TAG,"UP SHOW_SPEED" + ""+formatFileSize(d));
                Log.d(TAG,"UP ANGLE "+ ""+getPositionByRate(finalDownload));
                position = getPositionByRate(finalDownload);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RotateAnimation rotateAnimation;
                        rotateAnimation = new RotateAnimation(lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        rotateAnimation.setInterpolator(new LinearInterpolator());
                        rotateAnimation.setDuration(1500);
                        barImage.startAnimation(rotateAnimation);
                        uploadSpeed.setText("Upload Speed: "+formatFileSize(d));
                    }
                });

                lastPosition = position;*/
                }

                @Override
                public void onTotalProgress(int count, final ProgressionModel progressModel) {
                /*java.math.BigDecimal downloadDecimal = progressModel.getDownloadSpeed();
                final double downloadFinal = downloadDecimal.doubleValue();
                java.math.BigDecimal uploadDecimal = progressModel.getUploadSpeed();
                final double uploadFinal = uploadDecimal.doubleValue();
                final double totalSpeedCount = (downloadFinal + uploadFinal) / 2;
                float finalDownload = (downloadDecimal.longValue() / 1000000);
                float finalUpload = (uploadDecimal.longValue() / 1000000);
                float totalassumtionSpeed = (finalDownload + finalUpload) / 2;
                Log.d("SHOW_SPEED", "" + formatFileSize(totalSpeedCount));*/


//                    Log.d(TAG, "SERVER " + "" + progressModel.getDownloadSpeed());
                    //double speed = progressModel.getUploadSpeed()/((Double)1000000);
                    java.math.BigDecimal bigDecimal = new java.math.BigDecimal("" + progressModel.getDownloadSpeed());
                    float finalDownload = (bigDecimal.longValue() / 1000000);
//                    Log.d(TAG, "NET_SPEED " + "" + (float) (bigDecimal.longValue() / 1000000));
                    java.math.BigDecimal bd = progressModel.getDownloadSpeed();
                    final double d = bd.doubleValue();
                    String speed = formatFileSize(d);
//                    Log.d("SHOW_SPEED", "" + speed);
//                    Log.d(TAG, "ANGLE " + "" + getPositionByRate(finalDownload));
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//						if (speedTv!=null) {
                            try {
                                if (!mustShow && (speed.contains("mb/s"))) {
                                    if (speedTv != null) {
                                        speedTv.setVisibility(View.GONE);
//                                        Log.d(TAG, "1 " + "One");
                                    } else {
//                                        Log.d(TAG, "1 " + "Zero");
                                    }

                                } else if (!mustShow && (speed.contains("kb/s") && Float.valueOf(speed.split(" ")[0]) >= 200.0)) {
                                    if (speedTv != null) {
                                        speedTv.setVisibility(View.GONE);
//                                        Log.d(TAG, "2 " + "One");
                                    } else {
//                                        Log.d(TAG, "2 " + "Zero");
                                    }
                                } else if (!mustShow && (speed.contains("kb/s") && Float.valueOf(speed.split(" ")[0]) >= 100.0)) {
                                    if (speedTv != null) {
//                                        Log.d(TAG, "3 " + "One");
                                        speedTv.setText("" + speed);
                                        speedTv.setTextColor(activity.getResources().getColor(android.R.color.holo_orange_light));
                                    } else {
//                                        Log.d(TAG, "3 " + "Zero");
                                        Toasty.error(activity, "Poor internet connectivity", Toast.LENGTH_SHORT, false).show();
                                    }
                                } else if (!mustShow && (speed.contains("kb/s") && Float.valueOf(speed.split(" ")[0]) <= 50.0)) {
                                    if (speedTv != null) {
//                                        Log.d(TAG, "4 " + "One");
                                        speedTv.setText("" + speed);
                                        speedTv.setTextColor(activity.getResources().getColor(android.R.color.holo_red_dark));
                                    } else {
//                                        Log.d(TAG, "4 " + "Zero");
                                        Toasty.error(activity, "Poor internet connectivity", Toast.LENGTH_SHORT, false).show();
                                    }
                                } else if ((mustShow && speed.contains("mb/s")) || mustShow && (speed.contains("kb/s") && Float.valueOf(speed.split(" ")[0]) > 200.0)) {
                                    if (speedTv != null) {
//                                        Log.d(TAG, "5 " + "One" + speed);
                                        speedTv.setText("" + speed);
                                        speedTv.setTextColor(activity.getResources().getColor(android.R.color.holo_green_dark));
                                        speedTv.setVisibility(View.VISIBLE);
                                    } else
                                        Log.d(TAG, "1 " + "Zero");
                                } else {
                                    if (speedTv != null) {
//                                        Log.d(TAG, "6 " + "One");
                                        speedTv.setText("" + speed);
//									speedTv.setTextColor(android.R.color.holo_green_dark);
                                    } else {
//                                        Log.d(TAG, "6 " + "Zero");
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            //}
                            //else
                            //	Toasty.error(activity,"Poor internet connectivity", Toast.LENGTH_SHORT,false).show();
                        }
                    });


                }
            });
            builder.start("https://croptrails.farm", count);
        }catch (Exception e){

        }
    }

    public interface ColorListener{
        public void onColorChanged(int color,String speed);
    }

    public interface InternetSpeedListener{
        public void onSpeedUpdated(String speed);
    }

    public static void internetFlow(boolean isLoaded,Activity context,View connectivityLine,InternetSpeedListener internetSpeedListener) {
        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLoaded) {
                        if (!ConnectivityUtils.isConnected(context)) {
                            AppConstants.failToast(context, context.getResources().getString(R.string.check_internet_connection_msg));
                        } else {
                            ConnectivityUtils.checkSpeedWithColor(context, new ConnectivityUtils.ColorListener() {
                                @Override
                                public void onColorChanged(int color, String speed) {
                                    if (color == android.R.color.holo_green_dark) {
                                        connectivityLine.setVisibility(View.GONE);
//                                    connectivityLine.setBackgroundColor(getColor(R.color.blue));
                                    } else {
                                        connectivityLine.setVisibility(View.VISIBLE);
                                        connectivityLine.setBackgroundColor(context.getResources().getColor(color));
                                    }
                                    if (internetSpeedListener!=null)
                                        internetSpeedListener.onSpeedUpdated(speed);
//                                    internetSPeed = speed;
                                }
                            }, 20);
                        }
                    }
                }
            }, AppConstants.DELAY);
        } catch (Exception e) {

        }

    }


    public static void checkSpeedWithColor(Activity activity,ColorListener colorListener,int count) {
        if (activity!=null) {
            count = 1;
            InternetSpeedBuilder builder = new InternetSpeedBuilder(activity);
            builder.setOnEventInternetSpeedListener(new InternetSpeedBuilder.OnEventInternetSpeedListener() {
                @Override
                public void onDownloadProgress(int count, final ProgressionModel progressModel) {

                }

                @Override
                public void onUploadProgress(int count, final ProgressionModel progressModel) {
                }

                @Override
                public void onTotalProgress(int count, final ProgressionModel progressModel) {
//                    Log.d(TAG, "SERVER " + progressModel.getDownloadSpeed());
                    java.math.BigDecimal bigDecimal = new java.math.BigDecimal("" + progressModel.getDownloadSpeed());
                    float finalDownload = (bigDecimal.longValue() / 1000000);
//                    Log.d(TAG, "NET_SPEED " + "" + (float) (bigDecimal.longValue() / 1000000));
                    java.math.BigDecimal bd = progressModel.getDownloadSpeed();
                    final double d = bd.doubleValue();
                    String speed = formatFileSize(d);
//                    Log.d(TAG, "SHOW_SPEED " + speed);
//                    Log.d(TAG, "ANGLE " + getPositionByRate(finalDownload));
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                float conut = Float.valueOf(speed.split(" ")[0].trim());
                                if ((speed.contains(MEASURE.BPS)) || ((speed.contains(MEASURE.KBPS) && conut <= 70.0))) {
                                    colorListener.onColorChanged(android.R.color.holo_red_dark, speed);
//                                   Log.e(TAG, "RED");
                                } else if (speed.contains(MEASURE.KBPS) && conut <= 200.0) {
                                    colorListener.onColorChanged(R.color.yellow, speed);
//                                   Log.e(TAG, "YELLOW");
                                } else if ((speed.contains(MEASURE.TBPS) || speed.contains(MEASURE.GBPS) || speed.contains(MEASURE.MBPS) || ((speed.contains(MEASURE.KBPS)) && conut >= 200.0))) {
                                    colorListener.onColorChanged(android.R.color.holo_green_dark, speed);
//                                   Log.e(TAG, "GREEN");
                                }
                            }catch (Exception e){

                            }
                        }
                    });
                }
            });
            builder.start("https://croptrails.farm", count);
        }
    }


    public interface MEASURE{
        public static final String TBPS="tb/s";
        public static final String GBPS="gb/s";
        public static final String MBPS="mb/s";
        public static final String KBPS="kb/s";
        public static final String BPS="byte/s";
    }

    public static String formatFileSize(double size) {

        String hrSize;
        double b = size;
        double k = size / 1024.0;
        double m = ((size / 1024.0) / 1024.0);
        double g = (((size / 1024.0) / 1024.0) / 1024.0);
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

        NumberFormat dec =NumberFormat.getInstance(Locale.ENGLISH);
        dec.setGroupingUsed(false);
        dec.setMaximumFractionDigits(1);
        if (t > 1) {
            hrSize = dec.format(t).concat(" "+MEASURE.TBPS);
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" "+MEASURE.GBPS);
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" "+MEASURE.MBPS);
        } else if (k > 1) {
            hrSize = dec.format(k).concat(" "+MEASURE.KBPS);
        } else {
            hrSize = dec.format(b).concat(" "+MEASURE.BPS);
        }

        return hrSize;
    }

    /*
    public class customCall extends AsyncTask<Void , Void , Void> {
        SpeedTestSocket speedTestSocket = new SpeedTestSocket();
        @Override
        protected Void doInBackground(Void... voids) {
            speedTestSocket.startFixedDownload("http://www.ovh.net/files/100Mio.dat", 10000);
            //speedTestSocket.startUpload("http://ipv4.ikoula.testdebit.info/", 1000000);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

                @Override
                public void onCompletion(SpeedTestReport report) {
                    // called when download/upload is complete


                    Log.d("SPEED" , "[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                    Log.d("SPEED" , "[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());
                }

                @Override
                public void onError(SpeedTestError speedTestError, String errorMessage) {
                    // called when a download/upload error occur
                }

                @Override
                public void onProgress(float percent, SpeedTestReport report) {
                    // called to notify download/upload progress
                    System.out.println("[PROGRESS] progress : " + percent + "%");
                    System.out.println("[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                    System.out.println("[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());

                    Log.d("SPEED" , "[PROGRESS] progress : " + percent + "%");
                    Log.d("SPEED" , "[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                    Log.d("SPEED" , "[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());

                    @SuppressLint({"NewApi", "LocalSuppress"}) BigDecimal bd = new BigDecimal(report.getTransferRateBit());
                    @SuppressLint({"NewApi", "LocalSuppress"}) long speed = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
                    final long finalSpeed = (speed/8192)/100;

                    Log.d("METER_SPEED", ""+finalSpeed);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pointerSpeed.speedTo(finalSpeed);
                        }
                    });

                }
            });

        }
    }
*/

    public static int getPositionByRate(float rate) {

        if (rate <= 1) {
            return (int) (rate * 30);

        } else if (rate <= 10) {
            return (int) (rate * 6) + 30;

        } else if (rate <= 30) {
            return (int) ((rate - 10) * 3) + 90;

        } else if (rate <= 50) {
            return (int) ((rate - 30) * 1.5) + 150;

        } else if (rate <= 100) {
            return (int) ((rate - 50) * 1.2) + 180;
        }

        return 0;
    }


    public static String getNetworkName(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected())
            return "-"; // not connected
        if (info.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:     // api< 8: replace by 11
                case TelephonyManager.NETWORK_TYPE_GSM:      // api<25: replace by 16
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:   // api< 9: replace by 12
                case TelephonyManager.NETWORK_TYPE_EHRPD:    // api<11: replace by 14
                case TelephonyManager.NETWORK_TYPE_HSPAP:    // api<13: replace by 15
                case TelephonyManager.NETWORK_TYPE_TD_SCDMA: // api<25: replace by 17
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:      // api<11: replace by 13
                case TelephonyManager.NETWORK_TYPE_IWLAN:    // api<25: replace by 18
                case 19: // LTE_CA
                    return "4G";
               /* case TelephonyManager.NETWORK_TYPE_NR:       // api<29: replace by 20
                    return "5G";*/
                default:
                    return "UNKNOWN";
            }
        }
        return "UNKNOWN";
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i(TAG, "***** IP="+ ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        catch (Exception e){
            Log.e(TAG, e.toString());
        }
        return null;
    }
}