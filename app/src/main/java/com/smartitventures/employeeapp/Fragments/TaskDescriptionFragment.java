package com.smartitventures.employeeapp.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smartitventures.employeeapp.Activities.FirstScreenImei_1;
import com.smartitventures.employeeapp.R;
import com.smartitventures.employeeapp.Response.Response.AllTaskTitles.TitlesPayload;
import com.smartitventures.employeeapp.Response.Response.ChatMessageDisp.ChatDisplayPayload;
import com.smartitventures.employeeapp.Response.Response.ChatMessageDisp.ChatDisplaySuccess;
import com.smartitventures.employeeapp.Response.Response.TaskDetails.TaskSuccess;
import com.smartitventures.employeeapp.Response.Response.TaskStatus.TaskStatus;
import com.smartitventures.employeeapp.Response.Response.chatResponse.Chat;
import com.smartitventures.employeeapp.RetrofitClient.RestClient1;
import com.smartitventures.employeeapp.SharedPreferences.Utility;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.os.Build.ID;
import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_0;


public class TaskDescriptionFragment extends Fragment implements View.OnClickListener {

    public TextView tv_title, tv_desc,tvReply,tvCommentDisplay,tvReplyChild,tvAnswer;
    public Button btnInProgress, btnCompleted;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public EditText edComment,edCommentReply;
    public Button btnCancel,btnComment1,btnCancelReply,btnCommentReply;
    public String commentText,replyChild;
    public LinearLayout replyCommentLayout,linLayTop,runTimeLinearLayout,runTimeLinearLayoutLow;
    public RelativeLayout replyBtnLayout,relaiveBtnAboveLayout;
    public Integer TaskId;
    public int empId;
    private ArrayList<ChatDisplayPayload> chatDisplayArrayList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.task_detail_frag, container, false);

        //TextViews
        tv_title = view.findViewById(R.id.tv_title);
        tv_desc = view.findViewById(R.id.tv_desc);
        tvReply = view.findViewById(R.id.tvReply);
        //tvReplyChild = view.findViewById(R.id.tvReplyChild);
        //tvCommentDisplay = view.findViewById(R.id.tvCommentDisplay);


        //EditTexts
        edComment = view.findViewById(R.id.edComment);
        edCommentReply = view.findViewById(R.id.edCommentReply);


        //Layouts
        replyCommentLayout = view.findViewById(R.id.replyCommentLayout);
        replyBtnLayout = view.findViewById(R.id.replyBtnLayout);
        relaiveBtnAboveLayout = view.findViewById(R.id.relaiveBtnAboveLayout);
        linLayTop = view.findViewById(R.id.linLayTop);
        runTimeLinearLayout = view.findViewById(R.id.runTimeLinearLayout);
        runTimeLinearLayoutLow = view.findViewById(R.id.runTimeLinearLayoutLow);

        //Buttons
        btnCancel = view.findViewById(R.id.btnCancel);
        btnComment1 = view.findViewById(R.id.btnComment1);
        btnCancelReply = view.findViewById(R.id.btnCancelReply);
        btnCommentReply = view.findViewById(R.id.btnCommentReply);
        btnInProgress = view.findViewById(R.id.btnInProgress);
      //  btnPending = view.findViewById(R.id.btnPending);
        btnCompleted = view.findViewById(R.id.btnCompleted);
        tvAnswer = view.findViewById(R.id.tvAnswer);



        Utility utility = new Utility(getActivity());
        empId =utility.getEmployerId();

        String title = getArguments().getString("Title");
        String dis = getArguments().getString("Dis");


        tv_title.setText(title);
        tv_desc.setText(dis);


        TaskId = getArguments().getInt("TaskId");


        chatMessageDisplay();


        //Click listeners
        btnInProgress.setOnClickListener(this);
        btnCompleted.setOnClickListener(this);
        edComment.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnComment1.setOnClickListener(this);
        tvReply.setOnClickListener(this);
        btnCancelReply.setOnClickListener(this);
        linLayTop.setOnClickListener(this);
        edCommentReply.setOnClickListener(this);
        btnCommentReply.setOnClickListener(this);


        edComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                edComment.setFocusable(true);
                edComment.requestFocus();

                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(edComment,InputMethodManager.SHOW_FORCED);

                edComment.setSelection(edComment.getText().length());
                edCommentReply.setBackgroundResource(R.drawable.edbackgroundoff);
                edComment.setBackgroundResource(R.drawable.edbackgroundon);
                relaiveBtnAboveLayout.setVisibility(View.VISIBLE);

                return true;
            }
        });




        edCommentReply.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                edCommentReply.setFocusable(true);
                edCommentReply.requestFocus();

                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(edCommentReply,InputMethodManager.SHOW_FORCED);

                edCommentReply.setSelection(edCommentReply.getText().length());
                edComment.setBackgroundResource(R.drawable.edbackgroundoff);
                edCommentReply.setBackgroundResource(R.drawable.edbackgroundon);


                return true;
            }
        });


        return view;

    }

    private void chatMessageDisplay() {

        HashMap<String,String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("employer_id", String.valueOf(empId));
        stringStringHashMap.put("task_id", String.valueOf(TaskId));

        RestClient1.GitApiInterface restClient = RestClient1.getClient();

        restClient.chatDisplay(stringStringHashMap).enqueue(new Callback<ChatDisplaySuccess>() {
            @Override
            public void onResponse(Call<ChatDisplaySuccess> call, Response<ChatDisplaySuccess> response) {
                if(response.body().getIsSuccess()){

                    ArrayList<String> arrayList = new ArrayList<String>();

                    chatDisplayArrayList = new ArrayList<ChatDisplayPayload>(response.body().getPayload());


                    for(int i = 0;i<chatDisplayArrayList.size();i++){
                        arrayList.add(chatDisplayArrayList.get(i).getMsg());
                    }

                    tvAnswer.setText(arrayList.get(0));

                }
                else {

                }
            }

            @Override
            public void onFailure(Call<ChatDisplaySuccess> call, Throwable t) {

            }
        });


    }

    @Override
    public void onClick(View view) {

//        SharedPreferences.Editor editor = sharedpreferences.edit();

        if(view==btnInProgress){

            taskStatusApi();

            btnInProgress.setBackgroundResource(R.drawable.btnstatusgreen);
            btnCompleted.setBackgroundResource(R.drawable.btnstatusred);

          //  editor.putInt("ON",1);
           // editor.commit();

        }

        if(view==btnCompleted){


            FragmentManager manager=getFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            getFragmentManager().popBackStackImmediate();
            transaction.commit();

            taskStatusApi();

            btnCompleted.setBackgroundResource(R.drawable.btnstatusgreen);
            btnInProgress.setBackgroundResource(R.drawable.btnstatusred);

           // editor.putInt("ON",2);
           // editor.commit();



        }


        if(view==btnCancel)
        {
            relaiveBtnAboveLayout.setVisibility(View.GONE);

        }


        if(view==btnComment1){

            commentText = edComment.getText().toString();

            TaskId = getArguments().getInt("TaskId");

            chatApi();

        }


        if(view==tvReply)
        {
            replyCommentLayout.setVisibility(View.VISIBLE);
            replyBtnLayout.setVisibility(View.VISIBLE);
        }


        if(view==btnCommentReply){

            replyChild = edCommentReply.getText().toString();


            final int N = 1; // total number of textviews to add

            final TextView[] myTextViews = new TextView[N]; // create an empty array;

            for (int i = 0; i < N; i++) {
                // create a new textview
                final TextView rowTextView = new TextView(getActivity());
                rowTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.reply, 0, 0, 0);

                // set some properties of rowTextView or something
                rowTextView.setText(replyChild);

                // add the textview to the linearlayout
                runTimeLinearLayoutLow.addView(rowTextView);

                // save a reference to the textview for later
                myTextViews[i] = rowTextView;
            }
            edCommentReply.setText("");



        }



        if(view==btnCancelReply){
            replyCommentLayout.setVisibility(View.GONE);
            replyBtnLayout.setVisibility(View.GONE);
        }



    }

    private void chatApi() {


        HashMap<String,String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("employer_id", String.valueOf(empId));
        stringStringHashMap.put("task_id", String.valueOf(TaskId));
        stringStringHashMap.put("msg",commentText);

        RestClient1.GitApiInterface restClient = RestClient1.getClient();

        restClient.chat(stringStringHashMap).enqueue(new Callback<Chat>() {
            @Override
            public void onResponse(Call<Chat> call, Response<Chat> response) {
                if(response.body().getIsSuccess().equals(true))
                {

                    relaiveBtnAboveLayout.setVisibility(View.GONE);

                    final int N = 1; // total number of textviews to add

                    final TextView[] myTextViews = new TextView[N]; // create an empty array;

                    for (int i = 0; i < N; i++) {
                        // create a new textview
                        final TextView rowTextView = new TextView(getActivity());
                        rowTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.reply, 0, 0, 0);

                        // set some properties of rowTextView or something

                        rowTextView.setText(commentText);

                        // add the textview to the linearlayout
                        runTimeLinearLayout.addView(rowTextView);
                        // save a reference to the textview for later
                        myTextViews[i] = rowTextView;
                    }
                    edComment.setText("");


                }else {




                }
            }

            @Override
            public void onFailure(Call<Chat> call, Throwable t) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage(t.getMessage());
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getActivity().finish();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getActivity().finish();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

    }


    private void taskStatusApi() {

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("assign_to", String.valueOf(empId));
        stringStringHashMap.put("taskId", String.valueOf(TaskId));
        stringStringHashMap.put("status","completed");


        final RestClient1.GitApiInterface restClient = RestClient1.getClient();


        restClient.taskStatus(stringStringHashMap).enqueue(new Callback<TaskStatus>() {
            @Override
            public void onResponse(Call<TaskStatus> call, Response<TaskStatus> response) {
                if(response.body().getIsSuccess()){

                }
                else {

                }
            }

            @Override
            public void onFailure(Call<TaskStatus> call, Throwable t) {

            }
        });

    }



}