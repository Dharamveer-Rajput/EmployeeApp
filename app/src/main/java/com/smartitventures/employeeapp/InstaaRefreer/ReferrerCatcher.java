package com.smartitventures.employeeapp.InstaaRefreer;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.smartitventures.employeeapp.Response.Response.ValidateAccessCode.AccessCodeSuccess;
import com.smartitventures.employeeapp.RetrofitClient.RestClient1;
import com.smartitventures.employeeapp.SharedPreferences.Utility;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dharamveer on 11/10/17.
 */

public class ReferrerCatcher extends BroadcastReceiver {

    private static String referrer = "";
    public static boolean login = false;
    @Override
    public void onReceive(final Context context, Intent intent) {
        referrer = intent.getStringExtra("referrer");
        Bundle extras = intent.getExtras();

        if(extras != null){
            referrer = extras.getString("referrer");


            String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

            HashMap<String, String> stringStringHashMap = new HashMap<>();
            stringStringHashMap.put("deviceId",deviceId);
            stringStringHashMap.put("accessCode", referrer);

            final RestClient1.GitApiInterface restClient = RestClient1.getClient();


            restClient.validateaccessCode(stringStringHashMap).enqueue(new Callback<AccessCodeSuccess>() {
                @Override
                public void onResponse(Call<AccessCodeSuccess> call, Response<AccessCodeSuccess> response) {


                    if (response.body().getIsSuccess().equals(true)) {

                        Utility utility = new Utility(context);
                        utility.setName(response.body().getPayload().getName());
                        utility.setDesignation(response.body().getPayload().getDesignation());
                        utility.setEmail(response.body().getPayload().getEmail());
                        utility.setAddress(response.body().getPayload().getAddress());
                        utility.setMobileNo(response.body().getPayload().getPhone());
                        utility.setImagePath(response.body().getPayload().getPath());

                       } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
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





        Log.w("REFERRER","Referer is: "+referrer);
    }
}
