package com.smartitventures.employeeapp.FirebaseNotification;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.smartitventures.employeeapp.SharedPreferences.Utility;

/**
 * Created by dharamveer on 17/10/17.
 */

public class RegistrationIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private static final String TAG = "RegIntentService";



    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "FCM Registration Token: " + token);


        Utility utility = new Utility(getApplicationContext());
        utility.setToken(token);


    }
}
