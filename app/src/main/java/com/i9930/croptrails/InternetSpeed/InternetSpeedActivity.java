package com.i9930.croptrails.InternetSpeed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.internet_speed_testing.InternetSpeedBuilder;
import com.example.internet_speed_testing.ProgressionModel;
import com.github.anastr.speedviewlib.SpeedView;

import java.text.DecimalFormat;

import com.i9930.croptrails.R;

public class InternetSpeedActivity extends AppCompatActivity {

    SpeedView speedView;
    static int position = 0;
    static int lastPosition = 0;
    ImageView barImage;
    TextView downloadSpeed , uploadSpeed , totalSpeed;
    String TAG="InternetSpeedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_speed);


        barImage = (ImageView) findViewById(R.id.barImageView);
        downloadSpeed = (TextView) findViewById(R.id.download);
        uploadSpeed = (TextView) findViewById(R.id.uplaod);
        totalSpeed = (TextView) findViewById(R.id.total_speed);

        checkSpeed();

    }

    private void checkSpeed(){
        InternetSpeedBuilder builder = new InternetSpeedBuilder(this);
        builder.setOnEventInternetSpeedListener(new InternetSpeedBuilder.OnEventInternetSpeedListener() {
            @Override
            public void onDownloadProgress(int count, final ProgressionModel progressModel) {
                Log.d(TAG,"SERVER " + ""+progressModel.getDownloadSpeed());
                //double speed = progressModel.getUploadSpeed()/((Double)1000000);
/*
                java.math.BigDecimal bigDecimal = new java.math.BigDecimal(""+progressModel.getDownloadSpeed());
                float finalDownload = (bigDecimal.longValue()/1000000);
                Log.d(TAG,"NET_SPEED "  + ""+(float)(bigDecimal.longValue()/1000000));
                java.math.BigDecimal bd = progressModel.getDownloadSpeed();
                final double d = bd.doubleValue();
                Log.d("SHOW_SPEED" , ""+formatFileSize(d));
                Log.d(TAG,"ANGLE " + ""+getPositionByRate(finalDownload));
                position = getPositionByRate(finalDownload);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RotateAnimation rotateAnimation;
                        rotateAnimation = new RotateAnimation(lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        rotateAnimation.setInterpolator(new LinearInterpolator());
                        rotateAnimation.setDuration(1500);
                        barImage.startAnimation(rotateAnimation);
                        downloadSpeed.setText("Download Speed: "+formatFileSize(d));
                    }
                });

                lastPosition = position;
*/
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
                java.math.BigDecimal downloadDecimal = progressModel.getDownloadSpeed();
                final double downloadFinal = downloadDecimal.doubleValue();
                java.math.BigDecimal uploadDecimal = progressModel.getUploadSpeed();
                final double uploadFinal = uploadDecimal.doubleValue();
                final double totalSpeedCount = (downloadFinal+uploadFinal)/2;
                float finalDownload = (downloadDecimal.longValue()/1000000);
                float finalUpload = (uploadDecimal.longValue()/1000000);
                float totalassumtionSpeed = (finalDownload+finalUpload)/2;
                position = getPositionByRate(totalassumtionSpeed);
                Log.d("SHOW_SPEED" , ""+formatFileSize(totalSpeedCount));
                Log.d(TAG,"ANGLE " + ""+position);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RotateAnimation rotateAnimation;
                        rotateAnimation = new RotateAnimation(lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        rotateAnimation.setInterpolator(new LinearInterpolator());
                        rotateAnimation.setDuration(1500);
                        barImage.startAnimation(rotateAnimation);
                        barImage.setRotation(position);
                        totalSpeed.setText("Total Speed: "+formatFileSize(totalSpeedCount));
                        uploadSpeed.setText("Upload Speed: "+formatFileSize(uploadFinal));
                        downloadSpeed.setText("Download Speed: "+formatFileSize(downloadFinal));
                    }
                });
                lastPosition = position;
            }
        });
        builder.start("https://croptrails.farm", 2);
    }


    public static String formatFileSize(double size) {

        String hrSize;
        double b = size;
        double k = size/1024.0;
        double m = ((size/1024.0)/1024.0);
        double g = (((size/1024.0)/1024.0)/1024.0);
        double t = ((((size/1024.0)/1024.0)/1024.0)/1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if ( t>1 ) {
            hrSize = dec.format(t).concat(" ");
        } else if ( g>1 ) {
            hrSize = dec.format(g);
        } else if ( m>1 ) {
            hrSize = dec.format(m).concat(" mb/s");
        } else if ( k>1 ) {
            hrSize = dec.format(k).concat(" kb/s");
        } else {
            hrSize = dec.format(b);
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

    public int getPositionByRate(float rate) {

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


}
