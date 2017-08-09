package com.nayan.task.offlinetasklist;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by nayan on 9/8/17.
 */
public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }

    public static boolean isNetworkConnected() {
        boolean flag = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (SecurityException e) {
            flag = false;
        }
        return flag;
    }
}
