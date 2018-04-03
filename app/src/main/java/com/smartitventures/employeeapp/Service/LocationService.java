package com.smartitventures.employeeapp.Service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.smartitventures.employeeapp.Dialogs.EnterRegionDialog;
import com.smartitventures.employeeapp.Dialogs.ExitRegionDialog;

import java.util.Timer;
import java.util.TimerTask;


public class LocationService extends Service implements LocationListener {


    Context context=this;

    private static final String TAG = "MyLocationService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    Location mLastLocation;


    public LocationService(Context context) {
        this.context = context;
    }

    public LocationService(String provider) {
        Log.e(TAG, "LocationListener " + provider);
        mLastLocation = new Location(provider);
    }


    LocationService[] mLocationListeners = new LocationService[]{
            new LocationService(LocationManager.PASSIVE_PROVIDER)
    };

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e(TAG, "onCreate");


       // android.os.Debug.waitForDebugger();  // this line is key

        initializeLocationManager();

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[0]
            );
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager - LOCATION_INTERVAL: "+ LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


    @Override
    public void onLocationChanged(Location location) {


        Log.e(TAG, "onLocationChanged: " + location);
        mLastLocation.set(location);

        if (location != null) {

            final Location startPoint = new Location("locationA");
            startPoint.setLatitude(location.getLatitude());
            startPoint.setLongitude(location.getLongitude());

            final Location endPoint = new Location("locationB");
            endPoint.setLatitude(30.9755532);
            endPoint.setLongitude(76.5248539);

            float distance = startPoint.distanceTo(endPoint);

           sendMessageToActivity(distance);

        }



    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listener, ignore", ex);
                }
            }
        }
    }

    private void sendMessageToActivity(Float distance) {
        Intent intent = new Intent("intentKey");
    // You can also include some extra data.
        intent.putExtra("distance", distance);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }






}