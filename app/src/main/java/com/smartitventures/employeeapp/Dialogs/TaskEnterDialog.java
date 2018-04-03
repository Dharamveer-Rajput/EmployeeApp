package com.smartitventures.employeeapp.Dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.smartitventures.employeeapp.R;
import com.smartitventures.employeeapp.Response.Response.AllTaskTitles.AllTaskTitles;
import com.smartitventures.employeeapp.Response.Response.AllTaskTitles.TitlesPayload;
import com.smartitventures.employeeapp.Response.Response.addTaskManually.AddTask;
import com.smartitventures.employeeapp.RetrofitClient.RestClient1;
import com.smartitventures.employeeapp.SharedPreferences.Utility;
import com.smartitventures.employeeapp.SpinnerKeyValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.smartitventures.employeeapp.Adapters.TodayTaskAdapter.MyViewHolder.cardViewTodayTask;


public class TaskEnterDialog extends Dialog implements AdapterView.OnItemSelectedListener {


    public Button btnSubmitTask;
    public static EditText edEnterDescription;
    public TextView tvDate,tvTime;
    DatePickerDialog datePickerDialog;
    public String date1,time1;
    public String assignDate;
    public Spinner spinTask;
    final String[] task = { "Own Project", "R & D", "Self Study", "Other",  };
    public  String titleTask;
    public  String titleTask2;
    public  int titleId;
    public CardView cardView;
    private ArrayList<TitlesPayload> taskPayloadArrayList;
    public myOnClickListener myListener;
    Context context;
    String description;
    ArrayList<Integer> arrayListIds;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCanceledOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.task_enter_dialog);

        spinTask =  findViewById(R.id.spinTask);

        GetAllTaskTitlesApi();

        spinTask.setOnItemSelectedListener(this);

        cardView = findViewById(R.id.cardView);

        edEnterDescription = findViewById(R.id.edEnterDescription);

        btnSubmitTask = findViewById(R.id.btnSubmitTask);

        edEnterDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    btnSubmitTask.performClick();
                    return true;
                }
                return false;
            }
        });


        btnSubmitTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                description = edEnterDescription.getText().toString();

                if (description.length()==0) {
                    edEnterDescription.setError("Description is required");
                }

                else
                {

                    myListener.onButtonClick(titleTask,description);

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
            }
        });


    }


    private void GetAllTaskTitlesApi() {

        final HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("assignTo","1");


        RestClient1.GitApiInterface restClient = RestClient1.getClient();

        restClient.getAllTaskTitles(stringStringHashMap).enqueue(new Callback<AllTaskTitles>() {
            @Override
            public void onResponse(Call<AllTaskTitles> call, Response<AllTaskTitles> response) {

                if(response.body().getIsSuccess()){

                   // arrayListIds = new ArrayList<Integer>();
                    ArrayList<SpinnerKeyValue> spinnerKeyValueArrayList = new ArrayList();


                    taskPayloadArrayList = new ArrayList<>(response.body().getPayload());

                    for(int i=0;i<taskPayloadArrayList.size();i++)
                    {

                        spinnerKeyValueArrayList.add(new SpinnerKeyValue(taskPayloadArrayList.get(i).getId(),taskPayloadArrayList.get(i).getTitle()));

                    }


                    ArrayAdapter aa = new ArrayAdapter(getContext(),R.layout.spinner_item,spinnerKeyValueArrayList);
                    aa.setDropDownViewResource(R.layout.spinner_dropdown_item);

                    spinTask.setAdapter(aa);


                }
                else {

                    ArrayAdapter aa = new ArrayAdapter(getContext(),R.layout.spinner_item,task);
                    aa.setDropDownViewResource(R.layout.spinner_dropdown_item);

                    spinTask.setAdapter(aa);

                }
            }

            @Override
            public void onFailure(Call<AllTaskTitles> call, Throwable t) {

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

                    cardViewTodayTask.setCardBackgroundColor(Color.parseColor("#bfd774"));

                }
                else {

                }
            }

            @Override
            public void onFailure(Call<AddTask> call, Throwable t) {

            }
        });


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

                    Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_LONG).show();
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<AddTask> call, Throwable t) {

            }
        });


    }


    public TaskEnterDialog( Context context,myOnClickListener myOnClickListener) {
        super(context);
        this.context = context;
        this.myListener = myOnClickListener;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if(taskPayloadArrayList!=null) {

            SpinnerKeyValue spinnerKeyValue = (SpinnerKeyValue) parent.getSelectedItem();

            titleTask = String.valueOf(parent.getItemAtPosition(position));
            titleId = spinnerKeyValue.getId();
        }
        else {
            titleTask2 = (String) parent.getItemAtPosition(position);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
    // This is my interface //
    public interface myOnClickListener {
        void onButtonClick(String title,String desc);
    }


}
