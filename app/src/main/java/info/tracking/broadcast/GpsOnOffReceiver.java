package info.tracking.broadcast;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import info.tracking.MainActivity;
import info.tracking.R;
import info.tracking.gps_tracker.GPS_Tracker;
import info.tracking.service.ServiceF;
import info.tracking.service.TrackingService;

/**
 * Created by Asus on 03-05-2017.
 */


public class GpsOnOffReceiver extends BroadcastReceiver {

    GPS_Tracker gps;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {

            gps = new GPS_Tracker(context);
            if (gps.canGetLocation()) {
                if(isMyServiceRunning(ServiceF.class,context)){
                    Log.w("serviceRunning","true");
                }else {
                    Log.w("serviceRunning","false");
                    starTracking(context);
                }
            }else {

            }

        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;

    }

    /*start tracking*/
    AlarmManager _alarmManager;
    PendingIntent pendingIntent;
    private void starTracking(Context context) {

        Intent intent = new Intent(context, TrackingService.class);
         pendingIntent = PendingIntent.getService(context, Integer.parseInt(context.getResources().getString(R.string.id)), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        _alarmManager = (AlarmManager)context.getSystemService(Activity.ALARM_SERVICE);
        _alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 1000, pendingIntent);
    }
}