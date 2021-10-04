package com.i9930.croptrails.ServiceAndBroadcasts;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.i9930.croptrails.Utility.SharedPreferencesMethod;


/**
 * Created by hp on 13-04-2018.
 */

public class SampleBootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("SampleBootReceiver","Coming here in broad");

        if (SharedPreferencesMethod.getBoolean(context,SharedPreferencesMethod.LOGIN)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, GoogleService.class));
            } else {
                context.startService(new Intent(context, GoogleService.class));
            }
        }
      /*  Intent serviceIntent = new Intent(context,GoogleService.class);
        context.startService(serviceIntent);*/
    }

}
