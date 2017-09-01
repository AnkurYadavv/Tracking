package info.tracking.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Asus on 21-05-2017.
 */

public class ServiceF extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {

        Log.e(TAG, "onCreate");

        initializeLocationManager();


    }

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    private void initializeLocationManager() {


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(ServiceF.this)
                .addOnConnectionFailedListener(ServiceF.this)
                .build();

        mGoogleApiClient.connect();

    }



    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.e(TAG, "onConnected");
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, ServiceF.this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onsuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onsufailed");
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if (null != mCurrentLocation) {

            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            Toast.makeText(ServiceF.this,lat+" "+lng,Toast.LENGTH_SHORT).show();
        }else {
            Log.e(TAG, "onlnull");
        }
    }
}
