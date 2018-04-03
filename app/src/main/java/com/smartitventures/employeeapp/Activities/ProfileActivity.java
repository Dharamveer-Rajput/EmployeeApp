package com.smartitventures.employeeapp.Activities;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import com.smartitventures.employeeapp.CircleImageView.CircleImageView;
import com.smartitventures.employeeapp.Dialogs.FirstWelcomeDialog;
import com.smartitventures.employeeapp.Dialogs.InternetConnectivityDialog;
import com.smartitventures.employeeapp.FirebaseNotification.Config;

import com.smartitventures.employeeapp.FirebaseNotification.RegistrationIntentService;
import com.smartitventures.employeeapp.Fragments.AccountFragment;
import com.smartitventures.employeeapp.Fragments.HomeFragment;
import com.smartitventures.employeeapp.Fragments.TaskHomeFragment;
import com.smartitventures.employeeapp.R;
import com.smartitventures.employeeapp.Response.Response.AttendanceLog.AttendenceLog;
import com.smartitventures.employeeapp.Response.Response.InsertToken.InsertToken;
import com.smartitventures.employeeapp.Response.Response.ValidateAccessCode.AccessCodeSuccess;
import com.smartitventures.employeeapp.Response.Response.ValidateDeviceId.ValidateDeviceIdSuccess;
import com.smartitventures.employeeapp.RetrofitClient.RestClient1;
import com.smartitventures.employeeapp.Service.LocationService;
import com.smartitventures.employeeapp.Service.MyLocationService;
import com.smartitventures.employeeapp.SharedPreferences.Utility;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.smartitventures.employeeapp.Fragments.HomeFragment.animtionProgress;
import static com.smartitventures.employeeapp.Fragments.HomeFragment.chronometer;
import static com.smartitventures.employeeapp.Fragments.HomeFragment.friProgress;
import static com.smartitventures.employeeapp.Fragments.HomeFragment.monProgress;
import static com.smartitventures.employeeapp.Fragments.HomeFragment.satProgress;
import static com.smartitventures.employeeapp.Fragments.HomeFragment.serverUnixSec;
import static com.smartitventures.employeeapp.Fragments.HomeFragment.sunProgress;
import static com.smartitventures.employeeapp.Fragments.HomeFragment.thuProgress;
import static com.smartitventures.employeeapp.Fragments.HomeFragment.tueProgress;
import static com.smartitventures.employeeapp.Fragments.HomeFragment.wedProgress;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private FusedLocationProviderClient mFusedLocationClient;
    public String dayOfTheWeek;
    public static final String PREFS_NAME = "FirstLaunch";
    public static int stoppedMilliseconds;
    public String statusIn = "In";
    public String statusOut = "Out";
    public String currentDateTimeString;
    public CircleImageView myImage;
    public String imagePath;
    public String deviceId;
    public String accessCode;
    public TextView tvEmpName,tvDesignation;
    public Toolbar topToolBar;
    public String NotificationFlag;
    public Utility utility;
    private static final int PERMISSION_REQUEST_LOCATION = 1 ;

    double distance;
    double lat;
    double longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);

        topToolBar =  findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);


        checkLocationPermission();

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        NavigationDrawerCode();

        InternetConnectionCheck();

        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);

        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        utility = new Utility(getApplicationContext());


        NotificationFlag = getIntent().getStringExtra("flag");

       // startService(new Intent(getApplicationContext(), LocationService.class));
        startService(new Intent(getApplicationContext(), MyLocationService.class));


        LocalBroadcastManager.getInstance(this).registerReceiver(
                statusReceiver, new IntentFilter("intentKey"));


        //Api for notificaton token register
        regTokenApi();

        setOnClickListeners();

        ValidateDeviceId();

        NotificationFlagCheckBlock();

        //checkDistance();

    }


    private void NotificationFlagCheckBlock() {
        if (NotificationFlag != null) {

            if (NotificationFlag.equalsIgnoreCase("true")) {

                TaskHomeFragment taskHomeFragment = new TaskHomeFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.content_frame, taskHomeFragment, "Task Home Fragment").addToBackStack(null).commit();

                displayFirebaseRegId();

                checkPlayServices();



                //FusedGetLastLocationMethod();
                //checkDistance();

            }

        } else {

            displaySelectedScreen(R.id.home_frag);

            displayFirebaseRegId();

            checkPlayServices();


           // FusedGetLastLocationMethod();
            //checkDistance();


        }
    }

    private void InternetConnectionCheck() {
        if (!isNetworkAvailable()) {
            Intent intent2 = new Intent(ProfileActivity.this, InternetConnectivityDialog.class);
            startActivity(intent2);
            return;
        }
    }

    private void NavigationDrawerCode() {
         /*------------------Navigation Drawer------------------------*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, topToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        myImage = hView.findViewById(R.id.myImage);
        tvEmpName = hView.findViewById(R.id.tvEmpName);
        tvDesignation = hView.findViewById(R.id.tvDesignation);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        /*------------------Navigation Drawer------------------------*/
    }


    public void ValidateDeviceId() {

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("deviceId",deviceId);

        final RestClient1.GitApiInterface restClient = RestClient1.getClient();


        restClient.ValidateDeviceId(stringStringHashMap).enqueue(new Callback<ValidateDeviceIdSuccess>() {
            @Override
            public void onResponse(Call<ValidateDeviceIdSuccess> call, Response<ValidateDeviceIdSuccess> response) {

                if(response.body().getIsSuccess()){

                    accessCode = response.body().getPayload().getAccessCode();

                    //Api for validate access code
                    ValidateAccessCodeApi();

                }

            }

            @Override
            public void onFailure(Call<ValidateDeviceIdSuccess> call, Throwable t) {

            }
        });

    }

    private void ValidateAccessCodeApi() {

        //Vivo device id
        //7c6a67fe377c6ed6
        //3f0095c8d3bbf49b lg phone

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("deviceId",deviceId);
        stringStringHashMap.put("accessCode", accessCode);

        final RestClient1.GitApiInterface restClient = RestClient1.getClient();


        restClient.validateaccessCode(stringStringHashMap).enqueue(new Callback<AccessCodeSuccess>() {
            @Override
            public void onResponse(Call<AccessCodeSuccess> call, Response<AccessCodeSuccess> response) {


                if (response.body().getIsSuccess().equals(true)) {

                    Utility utility = new Utility(getApplicationContext());

                    utility.setEmployerId(response.body().getPayload().getId());
                    utility.setName(response.body().getPayload().getName());
                    utility.setDesignation(response.body().getPayload().getDesignation());
                    utility.setEmail(response.body().getPayload().getEmail());
                    utility.setAddress(response.body().getPayload().getAddress());
                    utility.setMobileNo(response.body().getPayload().getPhone());
                    utility.setImagePath(response.body().getPayload().getPath());

                    imagePath = response.body().getPayload().getPath();

                    tvEmpName.setText(response.body().getPayload().getName());
                    tvDesignation.setText(response.body().getPayload().getDesignation());

                    loadImagePicasso();




                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
                    builder1.setMessage("Invalid access code");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }

            @Override
            public void onFailure(Call<AccessCodeSuccess> call, Throwable t) {

            }
        });
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.home_frag:
                fragment = new HomeFragment();
                break;
            case R.id.task_frag:
                fragment = new TaskHomeFragment();
                break;
            case R.id.account_frag:
                fragment = new AccountFragment();
                break;

            case R.id.other_frag:
                fragment = new AccountFragment();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private BroadcastReceiver statusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // distance = intent.getFloatExtra("distance",0);
            lat = intent.getDoubleExtra("lat",0);
            longi = intent.getDoubleExtra("longi",0);

            checkDistance();


        }
    };



    private void checkDistance(){


        final Location startPoint = new Location("locationA");
        startPoint.setLatitude(lat);
        startPoint.setLongitude(longi);

        final Location endPoint = new Location("locationB");
        endPoint.setLatitude(30.9755532);
        endPoint.setLongitude(76.5248539);

        double distance = startPoint.distanceTo(endPoint);

        if(distance<30){
            checkCurrentDate();

        }

        else {
           // AttendanceLogApiOut();
            //chronometer.stop();
        }

    }



    private void FusedGetLastLocationMethod() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            final Location startPoint = new Location("locationA");
                            startPoint.setLatitude(location.getLatitude());
                            startPoint.setLongitude(location.getLongitude());

                            final Location endPoint = new Location("locationB");
                            endPoint.setLatitude(30.9755532);
                            endPoint.setLongitude(76.5248539);

                            float distance = startPoint.distanceTo(endPoint);

                            if (distance < 30) {

                                // AttendanceLogApiIn();

                                checkCurrentDate();

                            }

                            else {

                              //  AttendanceLogApiOut();



                            }

                        }
                    }
                });

    }

    private void checkCurrentDate() {

        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());


        if (sharedPref.getString("LAST_LAUNCH_DATE", "nodate").contains(currentDate)) {
            // Date matches. User has already Launched the app once today. So do nothing.
             // chronometerStartFromPrevious();

        } else {
            // Display dialog text here......
            // Do all other actions for first time launch in the day...
            // Set the last Launched date to today.

           // chronometerStartFromZero();

            FirstWelcomeDialog firstWelcomeDialog = new FirstWelcomeDialog(ProfileActivity.this);
            firstWelcomeDialog.show();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("LAST_LAUNCH_DATE", currentDate);
            editor.commit();

        }
    }


   /* private void AttendanceLogApiOut() {
        HashMap<String,String> stringStringHashMap = new HashMap<>();

        stringStringHashMap.put("employeId","1");
        stringStringHashMap.put("status",statusOut);

        final RestClient1.GitApiInterface restClient = RestClient1.getClient();

        restClient.attendanceLog(stringStringHashMap).enqueue(new Callback<AttendenceLog>() {
            @Override
            public void onResponse(Call<AttendenceLog> call, Response<AttendenceLog> response) {
                if(response.body().getIsSuccess()){

                    Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();

                }
                else {

                }
            }

            @Override
            public void onFailure(Call<AttendenceLog> call, Throwable t) {

            }
        });

    }*/



    private void loadImagePicasso() {

        //Loading Image from URL
        Picasso.with(getApplicationContext())
                .load("http://"+imagePath)
                .placeholder(R.drawable.placeholder)   // optional
                .error(R.drawable.error)      // optional
                .into(myImage);

    }

    public void regTokenApi(){

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);

        //Token
        Utility utility = new Utility(this);
        final String token = utility.getToken();

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("token", token);
        stringStringHashMap.put("assignTo", "1");
        stringStringHashMap.put("deviceOs", "android");

        final RestClient1.GitApiInterface gitApiInterface = RestClient1.getClient();

        gitApiInterface.insertToken(stringStringHashMap).enqueue(new Callback<InsertToken>() {
            @Override
            public void onResponse(Call<InsertToken> call, Response<InsertToken> response) {

                if (response.body().getIsSuccess()) {
                } else {
                }
            }

            @Override
            public void onFailure(Call<InsertToken> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void setOnClickListeners() {

//        homeClick.setOnClickListener(this);
//        taskClick.setOnClickListener(this);
//        accountClick.setOnClickListener(this);

    }


    public void checkForStatus() {

        if (dayOfTheWeek.equals("Monday")) {
            tueProgress.setMax(0);
            wedProgress.setMax(0);
            thuProgress.setMax(0);
            friProgress.setMax(0);
            satProgress.setMax(0);
            animtionProgress = ObjectAnimator.ofInt(monProgress, "progress", 0, 100);
            animtionProgress.setDuration(60000 * 60);
            animtionProgress.setInterpolator(new DecelerateInterpolator());
            animtionProgress.start();
        } else if (dayOfTheWeek.equals("Tuesday")) {
//            thuProgress.setMax(0);
//            friProgress.setMax(0);
//            satProgress.setMax(0);
            animtionProgress = ObjectAnimator.ofInt(tueProgress, "progress", 0, 100);
            animtionProgress.setDuration(60000 * 60);
            animtionProgress.setInterpolator(new DecelerateInterpolator());
            animtionProgress.start();
        } else if (dayOfTheWeek.equals("Wednesday")) {
            thuProgress.setMax(0);
            friProgress.setMax(0);
            satProgress.setMax(0);
            animtionProgress = ObjectAnimator.ofInt(wedProgress, "progress", 0, 100);
            animtionProgress.setDuration(60000 * 60);
            animtionProgress.setInterpolator(new DecelerateInterpolator());
            animtionProgress.start();
        } else if (dayOfTheWeek.equals("Thursday")) {
            friProgress.setMax(0);
            satProgress.setMax(0);
            animtionProgress = ObjectAnimator.ofInt(thuProgress, "progress", 0, 100);
            animtionProgress.setDuration(60000 * 30);
            animtionProgress.setInterpolator(new DecelerateInterpolator());
            animtionProgress.start();
        } else if (dayOfTheWeek.equals("Friday")) {

            animtionProgress = ObjectAnimator.ofInt(friProgress, "progress", 0, 100);
            animtionProgress.setDuration(60000 * 30);
            animtionProgress.setInterpolator(new DecelerateInterpolator());
            animtionProgress.start();

        } else if (dayOfTheWeek.equals("Saturday")) {

            animtionProgress = ObjectAnimator.ofInt(satProgress, "progress", 0, 100);
            animtionProgress.setDuration(60000 * 30);
            animtionProgress.setInterpolator(new DecelerateInterpolator());
            animtionProgress.start();

        } else if (dayOfTheWeek.equals("Sunday")) {
            animtionProgress = ObjectAnimator.ofInt(sunProgress, "progress", 0, 100);
            animtionProgress.setDuration(60000 * 60);
            animtionProgress.setInterpolator(new DecelerateInterpolator());
            animtionProgress.start();
        }

    }

    @Override
    public void onClick(View view) {
       /* if (view == homeClick) {


            HomeFragment homeFragment = new HomeFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.mainFrame, homeFragment, "Fragment Account").commit();

            chronometerStartFromPrevious();

            for (int i = 0; i < manager.getBackStackEntryCount(); ++i) {
                manager.popBackStack();
            }

        }
        if (view == taskClick) {



            TaskHomeFragment taskHomeFragment = new TaskHomeFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.mainFrame, taskHomeFragment, "Task Home Fragment").addToBackStack(null).commit();
            chronometerStop();


        }

        if (view == accountClick) {


            AccountFragment accountFragment = new AccountFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.mainFrame, accountFragment, "Fragment Account").addToBackStack(null).commit();
            chronometerStop();

        }*/
    }

    @Override
    public void onBackPressed() {


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        try {
            if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
                finish();// finish activity if you are at home screen
                return;
            } else {
                getSupportFragmentManager().popBackStack();//will pop previous fragment
            }
        } catch (Exception e) {
            super.onBackPressed();
        }

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();


    }


    public void checkLocationPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle("Location permission needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm location access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.ACCESS_FINE_LOCATION}
                                    , PERMISSION_REQUEST_LOCATION);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_LOCATION);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


            }
        }
        else{
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }
    }


    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);


        if (!TextUtils.isEmpty(regId))
        {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();

        LocalBroadcastManager.getInstance(this).registerReceiver(
                statusReceiver, new IntentFilter("intentKey"));
    }

    @Override
    protected void onPause() {
        super.onPause();

        //chronometerStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //chronometerStop();


    }

/*
    public void chronometerStop() {

        stoppedMilliseconds = 0;
        String chronoText = chronometer.getText().toString();
        String array[] = chronoText.split(":");
        if (array.length == 2) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                    + Integer.parseInt(array[1]) * 1000;
        } else if (array.length == 3) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                    + Integer.parseInt(array[1]) * 60 * 1000
                    + Integer.parseInt(array[2]) * 1000;
        }


        utility.setMiliseconds(stoppedMilliseconds);

//        SharedPreferences  sharedPreferences = getSharedPreferences("YOUR_PREF_NAME", 0);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("miliseconds",stoppedMilliseconds);
//        editor.commit();

        chronometer.stop();

    }
*/




   /* private void chronometerStartFromZero() {

        chronometer.start();

    }*/

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("Profile Activity", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }
}




















