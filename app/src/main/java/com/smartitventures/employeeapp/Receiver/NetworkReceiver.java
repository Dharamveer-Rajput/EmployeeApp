package com.smartitventures.employeeapp.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.smartitventures.employeeapp.Dialogs.InternetConnectivityDialog;



public class NetworkReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        if (!isOnline(context)) {
            Intent intent2 = new Intent(context, InternetConnectivityDialog.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            context.startActivity(intent2);
        }
    }
    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
}



