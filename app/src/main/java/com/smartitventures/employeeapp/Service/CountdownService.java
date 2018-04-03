package com.smartitventures.employeeapp.Service;

import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;



import java.util.concurrent.TimeUnit;


import static com.smartitventures.employeeapp.Fragments.HomeFragment.tvTimer;


public class CountdownService extends Service   {


    CountDownTimer countDownTimer = null;
    String hms;

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent,  int startId) {

        try{

           // startTimer(TimeUnit.MINUTES.toMillis(intent.getIntExtra("time",1)));
           // startTimer(000000);
        }

        catch (Exception e) {e.printStackTrace();}
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void startTimer(final long noOfMinutes) {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {

            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                 hms = String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

                sendMessageToActivity(hms);

                Log.i("running","");
            }
            public void onFinish() {

                sendMessageToActivity("hide");


            }
        }.start();
    }

    private void sendMessageToActivity(String hms) {
        Intent intent = new Intent("hms");
        intent.putExtra("key",hms);
        LocalBroadcastManager.getInstance(CountdownService.this).sendBroadcast(intent);

    }



}