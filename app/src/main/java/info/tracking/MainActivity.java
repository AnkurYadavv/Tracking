package info.tracking;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.tracking.database.TrackingSource;
import info.tracking.gps_tracker.GPS_Tracker;
import info.tracking.list.Activity_LatLngList;
import info.tracking.permission_controler.Permission_Controler;
import info.tracking.prefrence.SharedPrefHelper;
import info.tracking.service.ServiceF;
import info.tracking.service.TrackingService;
import info.tracking.showroute.Map_Activity;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id._stop_tracking) LinearLayout _stop_tracking;
    @Bind(R.id._strt_tracking) LinearLayout _strt_tracking;
    @Bind(R.id._show_route) LinearLayout _show_route;
    @Bind(R.id._list_route) LinearLayout _list_route;
    @Bind(R.id._clear_storage) LinearLayout _clear_storage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setTypeface();
        objectInitialization();
        listners();

    }

    Typeface Montserrat;
    private void setTypeface() {

        Montserrat = Typeface.createFromAsset(MainActivity.this.getAssets(), "Montserrat-Regular.ttf");

    }

    Permission_Controler permission_control;
    SharedPrefHelper session;
    TrackingSource datasource;
    private void objectInitialization() {
        permission_control=new Permission_Controler(MainActivity.this);
        session=new SharedPrefHelper(MainActivity.this);
        datasource = new TrackingSource(getApplicationContext());

        /*initialize alarm manager object for start tracking service*/
        _alarmManager = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), TrackingService.class);
        pendingIntent = PendingIntent.getService(getApplicationContext(), Integer.parseInt(getResources().getString(R.string.id)), intent, PendingIntent.FLAG_UPDATE_CURRENT);


    }

    int service_request_code=0;
    private void listners() {

        /* check tracking service is running */
        if (session.getString("start_service","").equals("true")){
            _strt_tracking.setVisibility(View.GONE);
        }

        _strt_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setString("start_service","");
                service_request_code=1;
                checkPermision();
            }
        });

        _stop_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _alarmManager.cancel(pendingIntent);
                _strt_tracking.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this,"Tracking stoped",Toast.LENGTH_SHORT).show();
                session.setString("start_service","");
            }
        });

        _show_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _alarmManager.cancel(pendingIntent);
                _strt_tracking.setVisibility(View.VISIBLE);
                Intent intent=new Intent(MainActivity.this, Map_Activity.class);
                startActivity(intent);
                session.setString("start_service","");
            }
        });

        _list_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Activity_LatLngList.class);
                startActivity(intent);

            }
        });

        _clear_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this, ServiceF.class);
                startService(intent);
               /* datasource.open();
                if (datasource.getAllLatLng().size()>0){
                    _alarmManager.cancel(pendingIntent);
                    _strt_tracking.setVisibility(View.VISIBLE);
                    datasource.removeAll();
                    Toast.makeText(MainActivity.this,"Route History Cleared",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"No Route History Available",Toast.LENGTH_SHORT).show();
                }*/

            }
        });
    }



    /*start tracking*/
    AlarmManager _alarmManager;
    PendingIntent pendingIntent;
    private void starTracking() {

          long repeatingTime=15*60*1000;
         _alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), repeatingTime, pendingIntent);
    }


    /* check location permission enable or not*/
    int count=0;
    boolean permission;
    private void checkPermision() {

        if (count==0){
            permission=permission_control.getOnlyLocationPermission(MainActivity.this);
            if (permission==true){

                getLocation();
            }
        }else{

            if(PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)){
                getLocation();
                permission=true;
                session.setString("permission","true");

            }else{

                if (count>0){
                    permissionDined();
                }

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            String permission=session.getString("permission","");
            if (permission.equals("")){

            }else {

               getLocation();

            }

        }catch (Exception e){

        }
    }

    /* if user deny the location permission*/
    AlertDialog.Builder dialog;
    Dialog d;
    private void permissionDined() {

        dialog=new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater=MainActivity.this.getLayoutInflater();
        View v=inflater.inflate(R.layout.use_location_dialog, null);

        dialog.setView(v);
        d= dialog.create();

        LinearLayout positive_button=(LinearLayout)v.findViewById(R.id.positive_button);
        LinearLayout negative_button=(LinearLayout)v.findViewById(R.id.negative_button);
        LinearLayout use_gps_layout=(LinearLayout)v.findViewById(R.id.use_gps_layout);
        use_gps_layout.setVisibility(View.GONE);

        TextView title=(TextView)v.findViewById(R.id.title);
        TextView message=(TextView)v.findViewById(R.id.message);
        TextView suggetion=(TextView)v.findViewById(R.id.suggetion);

        TextView negative_txt=(TextView)v.findViewById(R.id.negative_txt);
        TextView positive_txt=(TextView)v.findViewById(R.id.positive_txt);

        title.setText("Permission denied");
        message.setText("Without this permission the tracker system will not work.\n\nWithout access your location we can't track your activities");

        negative_txt.setText("EXIT");
        positive_txt.setText("RETRY");
        title.setTypeface(Montserrat);

        title.setTypeface(Montserrat);
        message.setTypeface(Montserrat);
        suggetion.setTypeface(Montserrat);

        negative_txt.setTypeface(Montserrat);
        positive_txt.setTypeface(Montserrat);


        positive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
                count=0;
                checkPermision();

                // exit=1;
                // onBackPressed();
            }
        });

        negative_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit=1;
                onBackPressed();
            }
        });

        d.show();

    }

    /*exit from app*/
    int exit=0;
    @Override
    public void onBackPressed() {
        if (exit==0){
            super.onBackPressed();
        }else{
            super.onBackPressed();
        }

    }


    /*runtime permission*/
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 1: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    count=1;
                    checkPermision();
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }

                return;
            }
        }
    }


    /*check gps enable or not*/
    GPS_Tracker gps;
    private void getLocation() {
        gps = new GPS_Tracker(MainActivity.this);
        if (gps.canGetLocation()) {

            if (session.getString("start_service","").equals("") && service_request_code==1){

                _strt_tracking.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"Tracking start",Toast.LENGTH_SHORT).show();
                starTracking();
                session.setString("start_service","true");
            }

        } else {

            checkGPS();
        }
    }




    Dialog _d;
    private void checkGPS() {

        try {

            AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater=MainActivity.this.getLayoutInflater();
            View v=inflater.inflate(R.layout.use_location_dialog, null);

            dialog.setView(v);
            if (_d==null){
                _d = dialog.create();
            }

            LinearLayout positive_button=(LinearLayout)v.findViewById(R.id.positive_button);
            LinearLayout negative_button=(LinearLayout)v.findViewById(R.id.negative_button);
            LinearLayout use_gps_layout=(LinearLayout)v.findViewById(R.id.use_gps_layout);


            TextView title=(TextView)v.findViewById(R.id.title);
            TextView message=(TextView)v.findViewById(R.id.message);
            TextView suggetion=(TextView)v.findViewById(R.id.suggetion);

            TextView negative_txt=(TextView)v.findViewById(R.id.negative_txt);
            TextView positive_txt=(TextView)v.findViewById(R.id.positive_txt);

            title.setTypeface(Montserrat);
            title.setTypeface(Montserrat);
            message.setTypeface(Montserrat);
            suggetion.setTypeface(Montserrat);

            negative_txt.setTypeface(Montserrat);
            positive_txt.setTypeface(Montserrat);


            positive_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _d.dismiss();

                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);

                    // exit=1;
                    // onBackPressed();
                }
            });

            negative_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _d.dismiss();
                    permissionDined();
                }
            });

            _d.show();


        }catch (Exception e){

        }

    }

}
