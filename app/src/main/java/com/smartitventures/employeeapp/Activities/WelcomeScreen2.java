package com.smartitventures.employeeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smartitventures.employeeapp.R;
import com.smartitventures.employeeapp.SharedPreferences.Utility;


public class WelcomeScreen2 extends AppCompatActivity implements View.OnClickListener {

    Button btnLetsGo;
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen2);

        btnLetsGo = (Button) findViewById(R.id.btnLetsGo);

        tvName = (TextView) findViewById(R.id.tvName);

        Utility utility = new Utility(WelcomeScreen2.this);

        String name = utility.getName();

        tvName.setText(name);

        btnLetsGo.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if(view==btnLetsGo){

            Intent intent = new Intent(WelcomeScreen2.this,ProfileActivity.class);
            startActivity(intent);
        }
    }
}
