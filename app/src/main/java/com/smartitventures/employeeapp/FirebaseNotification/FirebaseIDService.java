package com.smartitventures.employeeapp.FirebaseNotification;


import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.smartitventures.employeeapp.Activities.ProfileActivity;
import com.smartitventures.employeeapp.Response.Response.InsertToken.InsertToken;
import com.smartitventures.employeeapp.RetrofitClient.RestClient1;
import com.smartitventures.employeeapp.SharedPreferences.MySharedPreferenceNotification;
import com.smartitventures.employeeapp.SharedPreferences.Utility;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FirebaseIDService extends FirebaseInstanceIdService {


    private static final String TAG = "MyFirebaseIIDService";
    private MySharedPreferenceNotification mySharedPreference;

    String refreshedToken;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);


        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(final String token) {

        Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
        intent.putExtra("token",token);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        Utility utility = new Utility(getApplicationContext());
        utility.setToken(token);

    }
}