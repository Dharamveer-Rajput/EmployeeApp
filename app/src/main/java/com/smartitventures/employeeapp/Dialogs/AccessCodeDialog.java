package com.smartitventures.employeeapp.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.smartitventures.employeeapp.Activities.WelcomeScreen2;
import com.smartitventures.employeeapp.R;
import com.smartitventures.employeeapp.Response.Response.ValidateAccessCode.AccessCodeSuccess;
import com.smartitventures.employeeapp.RetrofitClient.RestClient1;
import com.smartitventures.employeeapp.SharedPreferences.Utility;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccessCodeDialog extends AppCompatActivity implements View.OnClickListener {


    EditText editTextPasscode;
    TextView tvSkip,tvOk;

    public Activity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.access_code_dialog3);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        tvSkip = (TextView) findViewById(R.id.tvSkip);
        tvOk = (TextView) findViewById(R.id.tvOk);
        editTextPasscode = (EditText) findViewById(R.id.editTextPasscode);

        tvSkip.setOnClickListener(this);
        tvOk.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view==tvSkip){

            finish();
        }
        if(view==tvOk){

            final String passcode = editTextPasscode.getText().toString();

            if(passcode.trim().length()==0)
            {
                editTextPasscode.setError("Passcode is not entered");
                editTextPasscode.requestFocus();
            }

            else {

                final ProgressDialog progressDialog = new ProgressDialog(AccessCodeDialog.this);
                progressDialog.setMessage("Loading....");
                progressDialog.show();

                Utility utility = new Utility(AccessCodeDialog.this);
                String deviceId = utility.getDeviceId();

                HashMap<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("deviceId",deviceId);
                stringStringHashMap.put("accessCode", passcode);

                final RestClient1.GitApiInterface restClient = RestClient1.getClient();


                restClient.validateaccessCode(stringStringHashMap).enqueue(new Callback<AccessCodeSuccess>() {
                    @Override
                    public void onResponse(Call<AccessCodeSuccess> call, Response<AccessCodeSuccess> response) {

                        progressDialog.dismiss();

                        if (response.body().getIsSuccess().equals(true)) {

                            Utility utility = new Utility(AccessCodeDialog.this);
                            utility.setName(response.body().getPayload().getName());
                            utility.setDesignation(response.body().getPayload().getDesignation());
                            utility.setEmail(response.body().getPayload().getEmail());
                            utility.setAddress(response.body().getPayload().getAddress());
                            utility.setMobileNo(response.body().getPayload().getPhone());

                            Intent intent = new Intent(AccessCodeDialog.this, WelcomeScreen2.class);
                            mActivity.startActivity(intent);

                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(AccessCodeDialog.this);
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
                        progressDialog.dismiss();

                    }
                });
            }
        }
    }
}
