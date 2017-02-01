package com.example.vikash.sales_person_client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Vikash on 1/17/2017.
 */

public class RestartServiceReceiver extends BroadcastReceiver
{

    private static final String TAG = "RestartServiceReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive");
        context.startService(new Intent(context.getApplicationContext(), GPS_Signal.class));
        Intent intent1 = new Intent(context,GPS_Signal.class);
        

    }

}
