package com.smartitventures.employeeapp.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.smartitventures.employeeapp.Activities.ProfileActivity;
import com.smartitventures.employeeapp.Adapters.TodayTaskAdapter;
import com.smartitventures.employeeapp.Dialogs.TaskEnterDialog;
import com.smartitventures.employeeapp.R;
import com.smartitventures.employeeapp.Response.Response.TaskDetails.TaskPayload;
import com.smartitventures.employeeapp.Response.Response.TaskDetails.TaskSuccess;
import com.smartitventures.employeeapp.RetrofitClient.RestClient1;
import com.smartitventures.employeeapp.SharedPreferences.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.smartitventures.employeeapp.Adapters.TodayTaskAdapter.MyViewHolder.cardViewTodayTask;


public class FragmentTaskToday extends Fragment{


    private ArrayList<TaskPayload> taskPayloadArrayList;
    public static TodayTaskAdapter adapter;
    private ProfileActivity mActivity;

    //  public ProgressBar progressBar;
    RecyclerView recyclerView;
    public ImageView notaskImage;
    public FloatingActionButton floatingActionButton;
    public TaskEnterDialog taskEnterDialog;
    public LinearLayout floatingAddTask_layout;
    FragmentManager fragmentManager;
    int empId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (ProfileActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_today_task, container, false);

        recyclerView = view.findViewById(R.id.today_task_rv);
        notaskImage = view.findViewById(R.id.notaskImage);

        floatingActionButton = view.findViewById(R.id.floatingAddTask);
        floatingAddTask_layout = view.findViewById(R.id.floatingAddTask_layout);

        fragmentManager = getFragmentManager();

        Utility utility = new Utility(getActivity());

        empId = utility.getEmployerId();

        ApiGetAllTask();

        return view;

    }

    private void ApiGetAllTask() {


                Timer timer = new Timer();
                timer.schedule(new TimerTask(){
                    @Override
                    public void run(){


                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                        stringStringHashMap.put("employeId", String.valueOf(empId));

                        final RestClient1.GitApiInterface restClient = RestClient1.getClient();

                        restClient.taskSuccess(stringStringHashMap).enqueue(new Callback<TaskSuccess>() {

                            @Override
                            public void onResponse(Call<TaskSuccess> call, final Response<TaskSuccess> response) {

                                if (response.body().getIsSuccess().equals(true))
                                {


                                    notaskImage.setVisibility(View.GONE);

                                    recyclerView.setVisibility(View.VISIBLE);

                                    taskPayloadArrayList = new ArrayList<>(response.body().getPayload());

                                    floatingActionButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            TaskEnterDialog.myOnClickListener myListener;


                                            myListener = new TaskEnterDialog.myOnClickListener() {
                                                @Override
                                                public void onButtonClick(String title, String desc) {
                                                    taskEnterDialog.dismiss();


                                                }
                                            };

                                            taskEnterDialog = new TaskEnterDialog(getActivity(),myListener);
                                            taskEnterDialog.show();

                                        }
                                    });



                                    recyclerView.setHasFixedSize(true);

                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


                                    adapter = new TodayTaskAdapter(getActivity(),taskPayloadArrayList,fragmentManager);

                                    recyclerView.setAdapter(adapter);


                                    adapter.notifyDataSetChanged();
                                }

                                else if(response.body().getIsSuccess().equals(false))
                                {

                                    recyclerView.setVisibility(View.GONE);

                                    notaskImage.setVisibility(View.VISIBLE);


                                    floatingActionButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            TaskEnterDialog.myOnClickListener myListener;

                                            myListener = new TaskEnterDialog.myOnClickListener() {
                                                @Override
                                                public void onButtonClick(String title, String desc) {
                                                    taskEnterDialog.dismiss();
                                                }
                                            };



                                            taskEnterDialog = new TaskEnterDialog(getActivity(),myListener);
                                            taskEnterDialog.show();

                                        }
                                    });


                                }


                            }

                            @Override
                            public void onFailure(Call<TaskSuccess> call, Throwable t) {


                            }
                        });



                    }
               }, 0, 1000);



    }


}

