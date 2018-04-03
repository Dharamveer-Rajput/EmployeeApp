package com.smartitventures.employeeapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.smartitventures.employeeapp.Dialogs.EnterRegionDialog;
import com.smartitventures.employeeapp.Dialogs.InternetConnectivityDialog;
import com.smartitventures.employeeapp.Service.LocationService;


public class FirstScreenImei_1 extends AppCompatActivity {

    String deviceId;
    TelephonyManager telephonyManager;
    private static final float GEOFENCE_RADIUS = 30; // in meters

    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;

    Double latitude, longitude;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isNetworkAvailable()) {
            Intent intent2 = new Intent(FirstScreenImei_1.this, InternetConnectivityDialog.class);
            startActivity(intent2);
            return;
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            Location startPoint = new Location("locationA");
                            startPoint.setLatitude(location.getLatitude());
                            startPoint.setLongitude(location.getLongitude());

                            Location endPoint = new Location("locationB");
                            endPoint.setLatitude(30.9754761);
                            endPoint.setLongitude(76.5247408);

                            double distance = startPoint.distanceTo(endPoint);

                            if (distance < 30) {
                                Intent intent2 = new Intent(getApplicationContext(), EnterRegionDialog.class);
                                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent2);
                            }

                        }
                    }
                });


        //Service is started
        startService(new Intent(getApplicationContext(), LocationService.class));

        fn_permission();


        //telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

       // deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
/*
        if (deviceId != null) {

            Log.e("Current Device deviceId", deviceId);

            final ProgressDialog progressDialog = new ProgressDialog(FirstScreenImei_1.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();

            HashMap<String, String> stringStringHashMap = new HashMap<>();
            stringStringHashMap.put("deviceId", deviceId);

            final RestClient1.GitApiInterface restClient = RestClient1.getClient();

            restClient.ValidateDeviceId(stringStringHashMap).enqueue(new Callback<com.smartitventures.employeeapp.Response.Response.ValidateDeviceId.ValidateDeviceIdSuccess>() {
                @Override
                public void onResponse(Call<com.smartitventures.employeeapp.Response.Response.ValidateDeviceId.ValidateDeviceIdSuccess> call, Response<com.smartitventures.employeeapp.Response.Response.ValidateDeviceId.ValidateDeviceIdSuccess> response) {

                    if (response.body().getIsSuccess().equals(true)) {

                        Utility utility = new Utility(FirstScreenImei_1.this);
                        utility.setDeviceId(deviceId);
                        utility.setEmployerId(response.body().getPayload().getId());
                        utility.setAccessCode(response.body().getPayload().getAccessCode());
                        utility.setName(response.body().getPayload().getName());
                        utility.setDesignation(response.body().getPayload().getDesignation());
                        utility.setEmail(response.body().getPayload().getEmail());
                        utility.setAddress(response.body().getPayload().getAddress());
                        utility.setMobileNo(response.body().getPayload().getPhone());

                        Intent intent = new Intent(FirstScreenImei_1.this, ProfileActivity.class);
                        startActivity(intent);
                    } else {

                        if (response.body().getIsSuccess().equals(false)) {

                            Intent intent = new Intent(FirstScreenImei_1.this, AccessCodeDialog.class);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<com.smartitventures.employeeapp.Response.Response.ValidateDeviceId.ValidateDeviceIdSuccess> call, Throwable t) {

                    progressDialog.dismiss();

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(FirstScreenImei_1.this);
                    builder1.setMessage(t.getMessage());
                    builder1.setCancelable(false);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
            });

        }*/


    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(FirstScreenImei_1.this, android.Manifest.permission.ACCESS_FINE_LOCATION))) {


            } else {
                ActivityCompat.requestPermissions(FirstScreenImei_1.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Location startPoint = new Location("locationA");
                            startPoint.setLatitude(location.getLatitude());
                            startPoint.setLongitude(location.getLongitude());

                            Location endPoint = new Location("locationB");
                            endPoint.setLatitude(30.9754761);
                            endPoint.setLongitude(76.5247408);

                            double distance = startPoint.distanceTo(endPoint);

                            if (distance < 30) {
                                Intent intent2 = new Intent(getApplicationContext(), EnterRegionDialog.class);
                                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent2);
                            }
                        }
                    }
                });
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean_permission = true;

                } else {
                    Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

                }
            }
        }
    }



}