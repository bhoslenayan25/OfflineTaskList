package com.nayan.task.offlinetasklist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.nayan.task.offlinetasklist.service.SyncData;

/**
 * Created by nayan on 9/8/17.
 */
public class OfflineBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.w("OfflineBroadcast", "network state change");
        // Make sure it's an event we're listening for ...
        if (!intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION) &&
                !intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION) &&
                !intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            Log.w("OfflineBroadcast", "network state change to inactive");
            return;
        }else {
            Log.w("OfflineBroadcast", "network state change to active");
            ConnectivityManager cm = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

            if (cm == null) {
                return;
            }else {

                // Now to check if we're actually connected
                if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
                    // Start the service to do our thing
                    context.startService(new Intent(context, SyncData.class));

                    Log.w("OfflineBroadcast", "service called");

                }
            }
        }
    }
}