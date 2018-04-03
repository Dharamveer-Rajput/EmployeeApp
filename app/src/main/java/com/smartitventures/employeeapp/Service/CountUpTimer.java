package com.smartitventures.employeeapp.Service;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * Created by dharamveer on 24/10/17.
 */

public class CountUpTimer extends CountDownTimer {


    int countUpTimer;


    public CountUpTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        countUpTimer=0;

    }

    @Override
    public void onTick(long millisUntilFinished) {

       // myTextView.setText("Seconds:"+countUpTimer);
        countUpTimer = countUpTimer+1;
    }

    @Override
    public void onFinish() {
        countUpTimer=0;

    }



}