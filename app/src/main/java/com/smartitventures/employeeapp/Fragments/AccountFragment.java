package com.smartitventures.employeeapp.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smartitventures.employeeapp.Activities.ProfileActivity;
import com.smartitventures.employeeapp.Activities.WelcomeScreen2;
import com.smartitventures.employeeapp.CircleImageView.CircleImageView;
import com.smartitventures.employeeapp.R;
import com.smartitventures.employeeapp.Response.Response.ValidateAccessCode.AccessCodeSuccess;
import com.smartitventures.employeeapp.RetrofitClient.RestClient1;
import com.smartitventures.employeeapp.SharedPreferences.Utility;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by dharamveer on 21/9/17.
 */

public class AccountFragment extends Fragment {


    public TextView tvemail,tvMobile,tvAddress,tvAccessCode,tvEmpNameBelow;
    public TextView tvEmpName,tvDesignation;
    public CircleImageView imgProfilePictureAccount;
    public View view;
    public String imagePath;
    public String TAG = "Picasso image Loading";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);


        findViewById();

        imgProfilePictureAccount = view.findViewById(R.id.imgProfilePictureAccount);

        Utility utility = new Utility(getActivity());
        String emp_name = utility.getName();
        String designation = utility.getDesignation();
        String email = utility.getEmail();
        String accessCode = utility.getAccessCode();
        String phone = utility.getMobileNo();
        String address = utility.getAddress();
        imagePath=utility.getImagePath();

        tvEmpName.setText(emp_name);
        tvemail.setText(email);
        tvMobile.setText(phone);
        tvAddress.setText(address);
        tvEmpName.setText(emp_name);
        tvEmpNameBelow.setText(emp_name);
        tvDesignation.setText(designation);

        loadImagePicasso();

        return view;


    }


    private void loadImagePicasso() {


        //Loading Image from URL
        Picasso.with(getActivity())
                .load("http://"+imagePath)
                .placeholder(R.drawable.placeholder)   // optional
                .error(R.drawable.error)      // optional
                .into(imgProfilePictureAccount);
    }

    private void findViewById() {

        tvEmpName = view.findViewById(R.id.tvEmpName);
        tvEmpNameBelow = view.findViewById(R.id.tvEmpNameBelow);
        tvDesignation = view.findViewById(R.id.tvDesignation);
        tvemail = view.findViewById(R.id.tvemail);
        tvAccessCode = view.findViewById(R.id.tvAccessCode);
        tvMobile = view.findViewById(R.id.tvMobile);
        tvAddress = view.findViewById(R.id.tvAddress);


    }

    private void AccessCodeApi() {


        String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("deviceId",deviceId);
        stringStringHashMap.put("accessCode", "5464");

        final RestClient1.GitApiInterface restClient = RestClient1.getClient();


        restClient.validateaccessCode(stringStringHashMap).enqueue(new Callback<AccessCodeSuccess>() {
            @Override
            public void onResponse(Call<AccessCodeSuccess> call, Response<AccessCodeSuccess> response) {


                if (response.body().getIsSuccess().equals(true)) {

                    Utility utility = new Utility(getActivity());

                    utility.setName(response.body().getPayload().getName());
                    utility.setDesignation(response.body().getPayload().getDesignation());
                    utility.setEmail(response.body().getPayload().getEmail());
                    utility.setAddress(response.body().getPayload().getAddress());
                    utility.setMobileNo(response.body().getPayload().getPhone());
                    utility.setImagePath(response.body().getPayload().getPath());

                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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


}
