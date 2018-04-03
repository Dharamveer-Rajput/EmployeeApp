package com.smartitventures.employeeapp.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.smartitventures.employeeapp.CallbackInterace.MyCallBack;
import com.smartitventures.employeeapp.R;



public class ExitRegionDialog extends Dialog{

    TextView tvSkip,tvOk;

    public MyCallBack myCallBack;

    public ExitRegionDialog( Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCanceledOnTouchOutside(false);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.exit_region_layout);

        tvOk =  findViewById(R.id.tvOkEnterRegion);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myCallBack.onResult(true);  // ok click
                dismiss();
            }
        });
    }




}