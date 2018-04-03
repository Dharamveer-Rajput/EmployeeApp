package com.smartitventures.employeeapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.smartitventures.employeeapp.R;

import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity {


    private RelativeLayout parent;
    int modeChange;
    private MediaController mc;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        openSplashOnlyOnce();


    }

    private void openSplashOnlyOnce() {

        SharedPreferences settings=getSharedPreferences("prefs",0);
        boolean firstRun=settings.getBoolean("firstRun",false);
        if(firstRun==false)//if running for first time
        //SplashActivity will load for first time
        {
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("firstRun",true);
            editor.commit();

            int orientation = getResources().getConfiguration().orientation;

            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                //modeChange = 1;

            } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                //modeChange = 2;

            }


            VideoView vd =  findViewById(R.id.VideoView);
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/"+R.raw.splash);

            mc = new MediaController(this);


            vd.setMediaController(mc);
            vd.setMediaController(null);
            vd.setVideoURI(uri);
            vd.start();

            // Start long running operation in a background thread
            new Thread() {
                public void run() {

                    try {

                        sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally{
                        Intent intent = new Intent(SplashActivity.this,ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }.start();

        }
        else
        {

            Intent a=new Intent(SplashActivity.this,ProfileActivity.class);
            startActivity(a);
            finish();
        }
    }


}
