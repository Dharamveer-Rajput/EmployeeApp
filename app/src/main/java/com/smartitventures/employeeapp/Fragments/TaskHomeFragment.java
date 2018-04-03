package com.smartitventures.employeeapp.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smartitventures.employeeapp.Dialogs.TaskEnterDialog;
import com.smartitventures.employeeapp.R;



public class TaskHomeFragment extends Fragment implements View.OnClickListener {

    Button btnTodayTask,btnTaskHistory;
    TextView tvTitle,tvDescription;
    public TaskEnterDialog taskEnterDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);


        btnTodayTask = view.findViewById(R.id.btnTodayTask);
        btnTaskHistory =  view.findViewById(R.id.btnTaskHistory);

        tvTitle = view.findViewById(R.id.tvTitle);

        btnTodayTask.setOnClickListener(this);
        btnTaskHistory.setOnClickListener(this);


        if(savedInstanceState==null){
            btnTodayTask.setTextColor(Color.WHITE);
            btnTaskHistory.setTextColor(Color.BLACK);
            btnTodayTask.setBackgroundResource(R.drawable.button_pressed_today_tasks);
            btnTaskHistory.setBackgroundResource(R.drawable.his_btn_right_side_bg);
            insertNestedFragment();
        }

        TaskEnterDialog.myOnClickListener myListener;

        myListener = new TaskEnterDialog.myOnClickListener() {
            @Override
            public void onButtonClick(String title, String desc) {

                tvTitle.setText(title);
                tvDescription.setText(desc);

            }
        };

        taskEnterDialog = new TaskEnterDialog(getActivity(),myListener);

        return view;

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        insertNestedFragment();
    }

    @Override
    public void onClick(View view) {

        if(view==btnTodayTask){

            btnTodayTask.setTextColor(Color.WHITE);
            btnTaskHistory.setTextColor(Color.BLACK);
            btnTodayTask.setBackgroundResource(R.drawable.button_pressed_today_tasks);
            btnTaskHistory.setBackgroundResource(R.drawable.his_btn_right_side_bg);
            insertNestedFragment();


        }
        if(view==btnTaskHistory){

            btnTaskHistory.setTextColor(Color.WHITE);
            btnTodayTask.setTextColor(Color.BLACK);
            btnTaskHistory.setBackgroundResource(R.drawable.button_pressed_hist_task);
            btnTodayTask.setBackgroundResource(R.drawable.task_btn_bg_with_border_right);
            insertNestedFragment2();


        }

    }

    private void insertNestedFragment() {
        FragmentTaskToday fragmentTodayTask = new FragmentTaskToday();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragmentTodayTask, "Fragment Today Task")
                .addToBackStack(null)
                .commit();
    }

    private void insertNestedFragment2() {
        FragmentTaskHistory fragmentTaskHistory = new FragmentTaskHistory();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragmentTaskHistory, "Fragment History Task")
                .addToBackStack(null)
                .commit();
    }



}
