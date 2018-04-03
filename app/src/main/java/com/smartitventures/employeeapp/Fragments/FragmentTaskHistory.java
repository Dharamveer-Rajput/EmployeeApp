package com.smartitventures.employeeapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.smartitventures.employeeapp.Adapters.HistoryTaskAdapter;
import com.smartitventures.employeeapp.R;
import com.smartitventures.employeeapp.Response.Response.TaskHistroy.HistorySuccess;
import com.smartitventures.employeeapp.Response.Response.TaskHistroy.HistoryPayload;
import com.smartitventures.employeeapp.RetrofitClient.RestClient1;
import com.smartitventures.employeeapp.SharedPreferences.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class FragmentTaskHistory extends Fragment {


    private ArrayList<HistoryPayload> historyPayloadArrayList;
    private HistoryTaskAdapter adapter;

    RecyclerView recyclerView;
   // ProgressBar progressBarHistory;
    ImageView noHisIcon;
    int empIdHis;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_history, container, false);

        recyclerView = view.findViewById(R.id.histroy_task_rv);

       // progressBarHistory = view.findViewById(R.id.progressBarHistory);
        noHisIcon = view.findViewById(R.id.noHisIcon);


        Utility utility = new Utility(getActivity());

        empIdHis = utility.getEmployerId();

        taskHistrotyApi();


        return view;


    }

    private void taskHistrotyApi() {


        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run(){




                HashMap<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("employeId", String.valueOf(empIdHis));

                final RestClient1.GitApiInterface restClient = RestClient1.getClient();

                restClient.taskHistory(stringStringHashMap).enqueue(new Callback<HistorySuccess>() {
                    @Override
                    public void onResponse(Call<HistorySuccess> call, Response<HistorySuccess> response) {

                        if (response.body().getIsSuccess().equals(true)) {


                            noHisIcon.setVisibility(View.GONE);

                            recyclerView.setVisibility(View.VISIBLE);

                            historyPayloadArrayList = new ArrayList<HistoryPayload>(response.body().getPayload());

                            recyclerView.setHasFixedSize(true);

                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                            adapter = new HistoryTaskAdapter(getActivity(), historyPayloadArrayList);

                            recyclerView.setAdapter(adapter);

                          //  progressBarHistory.setVisibility(View.INVISIBLE);

                            adapter.notifyDataSetChanged();


                        }
                        else if(response.body().getIsSuccess().equals(false)){

                         //   progressBarHistory.setVisibility(View.GONE);

                            recyclerView.setVisibility(View.GONE);

                            noHisIcon.setVisibility(View.VISIBLE);

                        }

                    }

                    @Override
                    public void onFailure(Call<HistorySuccess> call, Throwable t) {

                    }
                });


            }
        }, 0, 500);



    }
}
