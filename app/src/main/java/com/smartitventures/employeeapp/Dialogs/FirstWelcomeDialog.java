package com.smartitventures.employeeapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.smartitventures.employeeapp.Fragments.TaskHomeFragment;
import com.smartitventures.employeeapp.R;
import com.smartitventures.employeeapp.Response.Response.AllTaskTitles.AllTaskTitles;
import com.smartitventures.employeeapp.Response.Response.AllTaskTitles.TitlesPayload;
import com.smartitventures.employeeapp.Response.Response.addTaskManually.AddTask;
import com.smartitventures.employeeapp.RetrofitClient.RestClient1;
import com.smartitventures.employeeapp.SharedPreferences.Utility;
import com.smartitventures.employeeapp.SpinnerKeyValue;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dharamveer on 26/10/17.
 */

public class FirstWelcomeDialog extends Dialog implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    Spinner spinTaskWel;
    EditText edEnterDescriptionWel;
    Button btnSubmitTaskWel;
    final String[] taskWel = { "Own Project", "R & D", "Self Study", "Other",  };
    private ArrayList<TitlesPayload> taskPayloadArrayList;
    String description;
    public  String titleTask;
    public  int titleId;
    public  String titleTask2;


    public FirstWelcomeDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        setContentView(R.layout.welcome_task_layout);

        spinTaskWel = findViewById(R.id.spinTaskWel);
        edEnterDescriptionWel = findViewById(R.id.edEnterDescriptionWel);
        btnSubmitTaskWel = findViewById(R.id.btnSubmitTaskWel);



        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("assignTo","1");


        RestClient1.GitApiInterface restClient = RestClient1.getClient();

        restClient.getAllTaskTitles(stringStringHashMap).enqueue(new Callback<AllTaskTitles>() {
            @Override
            public void onResponse(Call<AllTaskTitles> call, Response<AllTaskTitles> response) {



                if (response.body().getIsSuccess()) {

                    ArrayList<String> arrayList = new ArrayList();

                    taskPayloadArrayList = new ArrayList<>(response.body().getPayload());


                    for (int i = 0; i < taskPayloadArrayList.size(); i++) {

                        arrayList.add(taskPayloadArrayList.get(i).getTitle());
                    }



                    ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item, arrayList);
                    arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

                    spinTaskWel.setAdapter(arrayAdapter);

                }



                else {

                    ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item, taskWel);
                    arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

                    spinTaskWel.setAdapter(arrayAdapter);

                }
            }

            @Override
            public void onFailure(Call<AllTaskTitles> call, Throwable t) {

            }
        });

        spinTaskWel.setOnItemSelectedListener(this);

        btnSubmitTaskWel.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if(v==btnSubmitTaskWel){


            description = edEnterDescriptionWel.getText().toString();


            if (description.length()==0) {
                edEnterDescriptionWel.setError("Description is required");
            }

            else
            {


                if(taskPayloadArrayList!=null) {

                    //update the task
                    UpdateTaskApi();

                }
                else {

                    //add the task
                    AddTaskApi();
                }

            }


            Utility utility = new Utility(getContext());

            utility.setSelectedTaskTitle(titleTask2);
            utility.setEdEnterDescriptionWel(description);

            dismiss();


        }
    }

    private void AddTaskApi() {

        HashMap<String,String> stringStringHashMap1 = new HashMap<String, String>();

        stringStringHashMap1.put("taskId", "");
        stringStringHashMap1.put("description",description);
        stringStringHashMap1.put("title",titleTask2);
        stringStringHashMap1.put("assign_to","1");

        final RestClient1.GitApiInterface restClient = RestClient1.getClient();

        restClient.addTask(stringStringHashMap1).enqueue(new Callback<AddTask>() {
            @Override
            public void onResponse(Call<AddTask> call, Response<AddTask> response) {

                if(response.body().getIsSuccess()){

                    Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<AddTask> call, Throwable t) {

            }
        });
    }

    private void UpdateTaskApi() {

        HashMap<String,String> stringStringHashMap1 = new HashMap<String, String>();


        stringStringHashMap1.put("taskId", String.valueOf(titleId));
        stringStringHashMap1.put("description",description);
        stringStringHashMap1.put("title",titleTask);
        stringStringHashMap1.put("assign_to","1");

        final RestClient1.GitApiInterface restClient = RestClient1.getClient();

        restClient.addTask(stringStringHashMap1).enqueue(new Callback<AddTask>() {
            @Override
            public void onResponse(Call<AddTask> call, Response<AddTask> response) {

                if(response.body().getIsSuccess()){

                    Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<AddTask> call, Throwable t) {

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



        if(taskPayloadArrayList!=null) {

            titleTask = String.valueOf(parent.getItemAtPosition(position));
            titleId = position;
        }
        else {
            titleTask2 = (String) parent.getItemAtPosition(position);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
