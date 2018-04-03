package com.smartitventures.employeeapp.FirebaseNotification;

import android.os.CountDownTimer;

import static com.smartitventures.employeeapp.Fragments.HomeFragment.tvTimer;

/**
 * Created by dharamveer on 24/10/17.
 */

public class TimerCounter extends CountDownTimer {
    int countUpTimer;

    public TimerCounter(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        countUpTimer=0;

    }

    @Override
    public void onTick(long millisUntilFinished) {

        tvTimer.setText("Seconds:"+countUpTimer);
        countUpTimer = countUpTimer+1;
    }

    @Override
    public void onFinish() {
        countUpTimer=0;

    }
}
