package com.smartitventures.employeeapp.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dharamveer on 19/9/17.
 */

public class Utility {

    Context context;
    public String deviceId;
    public Integer employerId;
    public Integer taskId;
    private String name;
    private String email;
    public String designation;
    public String mobileNo;
    public String address;
    public String accessCode;
    public String token;
    public String selectedTaskTitle;
    public String edEnterDescriptionWel;
    public static int progressState;
    public static long duration;
    public String imagePath;
    public long miliseconds;
    static SharedPreferences sharedPreferences;


    public long getMiliseconds() {
        miliseconds = sharedPreferences.getLong("miliseconds",miliseconds);
        return miliseconds;
    }

    public void setMiliseconds(long miliseconds) {
        this.miliseconds = miliseconds;
        sharedPreferences.edit().putLong("miliseconds",miliseconds).commit();
    }

    public String getImagePath() {
        imagePath = sharedPreferences.getString("imagePath",imagePath);
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        sharedPreferences.edit().putString("imagePath", imagePath).commit();

    }



   public static long getDuration(){
       duration = sharedPreferences.getLong("duration",duration);
       return duration;
   }


   public static void setDuration(long duration1){
       sharedPreferences.edit().putLong("duration",duration1).commit();

   }
    public static int getProgress(){

        progressState  = sharedPreferences.getInt("progressState", progressState);

        return progressState;
    }

    public static void setProgress(int progress){

        sharedPreferences.edit().putInt("progressState", progress).commit();

    }


    public String getEdEnterDescriptionWel() {
        edEnterDescriptionWel = sharedPreferences.getString("edEnterDescriptionWel", "");

        return edEnterDescriptionWel;
    }

    public void setEdEnterDescriptionWel(String edEnterDescriptionWel) {
        this.edEnterDescriptionWel = edEnterDescriptionWel;
        sharedPreferences.edit().putString("edEnterDescriptionWel", edEnterDescriptionWel).commit();

    }


    public String getSelectedTaskTitle() {
        selectedTaskTitle = sharedPreferences.getString("selectedTaskTitle", "");

        return selectedTaskTitle;
    }

    public void setSelectedTaskTitle(String selectedTaskTitle) {
        this.selectedTaskTitle = selectedTaskTitle;
        sharedPreferences.edit().putString("selectedTaskTitle", selectedTaskTitle).commit();

    }


    public String getToken() {
        token = sharedPreferences.getString("token", "");
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        sharedPreferences.edit().putString("token", token).commit();

    }

    public String getAccessCode() {
        accessCode = sharedPreferences.getString("accessCode", "");
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
        sharedPreferences.edit().putString("accessCode", accessCode).commit();

    }

    public String getMobileNo() {
        mobileNo = sharedPreferences.getString("mobileNo", "");

        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
        sharedPreferences.edit().putString("mobileNo", mobileNo).commit();

    }

    public String getAddress() {
        address = sharedPreferences.getString("address", "");

        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        sharedPreferences.edit().putString("address", address).commit();

    }

    public void removeUser() {
        sharedPreferences.edit().clear().commit();
    }



    public String getEmail() {
        email = sharedPreferences.getString("email", "");
        return email;
    }

    public String getName() {
        name = sharedPreferences.getString("name", "");
        return name;
    }

    public String getDesignation() {
        designation = sharedPreferences.getString("designation", "");

        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
        sharedPreferences.edit().putString("designation", designation).commit();

    }

    public void setName(String name) {
        this.name = name;
        sharedPreferences.edit().putString("name", name).commit();

    }
    public String getDeviceId() {
        deviceId = sharedPreferences.getString("deviceId", "");
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        sharedPreferences.edit().putString("deviceId", deviceId).commit();

    }

    public void setEmail(String email) {
        this.email = email;
        sharedPreferences.edit().putString("email", email).commit();
    }

    public Integer getEmployerId() {
        employerId = sharedPreferences.getInt("employerId",0);
        return employerId;

    }

    public void setEmployerId(Integer employerId) {
        this.employerId = employerId;
        sharedPreferences.edit().putInt("employerId", employerId).commit();

    }


    public Utility(Context context) {
        this.context = context;
        this.email = email;
        this.sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);;
    }
}
