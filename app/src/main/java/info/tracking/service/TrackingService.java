package info.tracking.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;

import info.tracking.MainActivity;
import info.tracking.database.TrackingSource;
import info.tracking.gps_tracker.GPS_Tracker;

/**
 * Created by Asus on 02-05-2017.
 */

public class TrackingService extends Service {

    GPS_Tracker gps;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.w("in_service",getCurrentTime());
        gps = new GPS_Tracker(getApplicationContext());
        if (gps.canGetLocation()) {

           storeLatLngToDb(gps);

        }
        return Service.START_STICKY;
    }

    private String getCurrentTime() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());

        return currentDateandTime;
    }


    double latitude = 0.0, longitude = 0.0;
    TrackingSource datasource;
    private void storeLatLngToDb(GPS_Tracker gps) {

        datasource = new TrackingSource(getApplicationContext());
        datasource.open();
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
        datasource.storeLatLng(latitude+","+longitude);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
}