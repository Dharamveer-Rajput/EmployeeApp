package com.smartitventures.employeeapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartitventures.employeeapp.Fragments.TaskDescriptionFragment;
import com.smartitventures.employeeapp.R;
import com.smartitventures.employeeapp.Response.Response.TaskDetails.TaskPayload;
import com.smartitventures.employeeapp.SharedPreferences.Utility;

import java.util.ArrayList;


public class TodayTaskAdapter extends RecyclerView.Adapter<TodayTaskAdapter.MyViewHolder> {

    private ArrayList<TaskPayload> taskPayloadArrayList;
    private Context context;

    FragmentManager fragmentManager;

    public TodayTaskAdapter(Activity context, ArrayList<TaskPayload> taskPayloadArrayList,FragmentManager fragmentManager)
    {
        this.taskPayloadArrayList = taskPayloadArrayList;
        this.context = context;
        this.fragmentManager = fragmentManager;

    }

    @Override
    public TodayTaskAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_task_row, parent, false);

        context = parent.getContext();



        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(TodayTaskAdapter.MyViewHolder holder, int position) {


        final TaskPayload taskPayload  = taskPayloadArrayList.get(position);


        Utility utility = new Utility(context);

        String taskTitle = utility.getSelectedTaskTitle();


        if(taskTitle.equalsIgnoreCase(taskPayload.getTitle())){

            holder.statusGreen.setVisibility(View.VISIBLE);
            holder.cardViewTodayTask.setCardBackgroundColor(Color.parseColor("#bfd774"));

        }

        holder.tvTitle.setText(taskPayload.getTitle());
        holder.tvAssignDate.setText(taskPayload.getCreatedOn());
        holder.tvDeadline.setText(taskPayload.getDeadline());

        holder.ll_todaytask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new TaskDescriptionFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).addToBackStack(null).commit();

                Bundle bundle = new Bundle();
                bundle.putString("Title",taskPayload.getTitle());
                bundle.putString("Dis",taskPayload.getDescription());
                bundle.putInt("TaskId", taskPayload.getId());

                fragment.setArguments(bundle);
            }
        });


    }

    @Override
    public int getItemCount() {
        return taskPayloadArrayList.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public static TextView  tvTitle,tvAssignDate,tvDeadline;
        LinearLayout ll_todaytask;
        public static CardView cardViewTodayTask;
        public static TextView statusGreen;


        public MyViewHolder(View itemView) {
            super(itemView);


            tvTitle = itemView.findViewById(R.id.tvTitle);
            //tvDesc = itemView.findViewById(R.id.tvDesc);
            tvAssignDate = itemView.findViewById(R.id.tvAssignDate);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
            statusGreen = itemView.findViewById(R.id.statusGreen);


            ll_todaytask = itemView.findViewById(R.id.ll_todaytask);
            cardViewTodayTask = itemView.findViewById(R.id.cardViewTodayTask);


        }




    }
}
