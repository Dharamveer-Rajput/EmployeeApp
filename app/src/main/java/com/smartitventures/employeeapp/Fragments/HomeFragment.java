package com.smartitventures.employeeapp.Fragments;

import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import com.smartitventures.employeeapp.Dialogs.FirstWelcomeDialog;
import com.smartitventures.employeeapp.R;
import com.smartitventures.employeeapp.Response.Response.AttendanceLog.AttendenceLog;
import com.smartitventures.employeeapp.Response.Response.SelectTimeResponse.SelectTimePayload;
import com.smartitventures.employeeapp.Response.Response.SelectTimeResponse.SelectTimeSuccess;
import com.smartitventures.employeeapp.RetrofitClient.RestClient1;
import com.smartitventures.employeeapp.Service.CountdownService;
import com.smartitventures.employeeapp.Service.LocationService;
import com.smartitventures.employeeapp.Service.MyLocationService;
import com.smartitventures.employeeapp.SharedPreferences.Utility;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.smartitventures.employeeapp.Activities.ProfileActivity.stoppedMilliseconds;


public class HomeFragment extends Fragment implements  OnChartValueSelectedListener {


    public static TextView tvTimer,tvCurrentDate;
    public BroadcastReceiver mMessageReceiver;
    public String month,dayOfTheWeek;
    public int date;
    public String hsm;
    public long hoursTomili;
    public static ObjectAnimator animtionProgress,objectAnimatorTue;
    public static ProgressBar monProgress,tueProgress,wedProgress,thuProgress,friProgress,satProgress,sunProgress;
    public static Chronometer chronometer;
    public Utility utility;
    public PieChart mChart;
    public ArrayList<String> xVals;
    public String currentDateTimeString;
    public int minsTotal = 0;
    private ArrayList<SelectTimePayload> selectTimePayloadArrayList;
    public View view;
    public int rotationDeg;
    public static long serverUnixSec;
    public static long lastouttime;
    public  String[] xValues = {"In Time", "Out Time","Pending Time"};
    public static  final int[] MY_COLORSFALSE = {Color.rgb(255,165,0)};
    Integer totalmins;
    public long unixTimeSec;
    public static final String PREFS_NAME = "FirstLaunch";
    public long sum;
    public ArrayList<Integer> returnVal;
    public String statusIn = "In";
    String status;
    public String statusOut = "Out";
    private Button btnIn,btnOut;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        findViewById();


        btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttendanceLogApiIn();
                SelectTimeApi();

            }
        });

        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttendanceLogApiOut();
                SelectTimeApi();

            }
        });
        resourcesSetters();

        pieChartValues();

        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        utility = new Utility(getActivity());

        getActivity().startService(new Intent(getActivity(), MyLocationService.class));



        unixTimeSec = System.currentTimeMillis() / 1000L;

        checkCurrentDate();

        UtilityGetProgress();


        return view;

    }





    private void AttendanceLogApiOut() {
        HashMap<String,String> stringStringHashMap = new HashMap<>();

        stringStringHashMap.put("employeId","1");
        stringStringHashMap.put("status",statusOut);

        final RestClient1.GitApiInterface restClient = RestClient1.getClient();

        restClient.attendanceLog(stringStringHashMap).enqueue(new Callback<AttendenceLog>() {
            @Override
            public void onResponse(Call<AttendenceLog> call, Response<AttendenceLog> response) {
                if(response.body().getIsSuccess()){

                    Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_LONG).show();

                }
                else {

                }
            }

            @Override
            public void onFailure(Call<AttendenceLog> call, Throwable t) {

            }
        });

    }

    private void AttendanceLogApiIn() {

        HashMap<String,String> stringStringHashMap = new HashMap<>();

        stringStringHashMap.put("employeId","1");
        stringStringHashMap.put("status",statusIn);


        final RestClient1.GitApiInterface restClient = RestClient1.getClient();


        restClient.attendanceLog(stringStringHashMap).enqueue(new Callback<AttendenceLog>() {
            @Override
            public void onResponse(Call<AttendenceLog> call, Response<AttendenceLog> response) {
                if(response.body().getIsSuccess()){

                }
                else {

                }
            }

            @Override
            public void onFailure(Call<AttendenceLog> call, Throwable t) {

            }
        });
    }

    private void checkCurrentDate() {

        SharedPreferences sharedPref = getActivity().getSharedPreferences(PREFS_NAME, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());


        if (sharedPref.getString("LAST_LAUNCH_DATE", "nodate").contains(currentDate)) {
            // Date matches. User has already Launched the app once today. So do nothing.

            SelectTimeApi();

        } else {
            // Display dialog text here......
            // Do all other actions for first time launch in the day...
            // Set the last Launched date to today.


            FirstWelcomeDialog firstWelcomeDialog = new FirstWelcomeDialog(getActivity());
            firstWelcomeDialog.show();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("LAST_LAUNCH_DATE", currentDate);
            editor.commit();

            SelectTimeApi();



        }
    }

    private void chronometerStartFromZero() {

        chronometer.start();

    }

    private void UtilityGetProgress() {
        if(Utility.getProgress()>0){

            if(dayOfTheWeek.equals("Monday")){
                tueProgress.setMax(0);
                wedProgress.setMax(0);
                thuProgress.setMax(0);
                friProgress.setMax(0);
                satProgress.setMax(0);
                animtionProgress = ObjectAnimator.ofInt(monProgress, "progress",Utility.getProgress(), 100);
                animtionProgress.setDuration(60000*60*9);
                animtionProgress.setInterpolator(new DecelerateInterpolator());
                animtionProgress.start();

            }
            else if(dayOfTheWeek.equals("Tuesday")){
                wedProgress.setMax(0);
                thuProgress.setMax(0);
                friProgress.setMax(0);
                satProgress.setMax(0);
                objectAnimatorTue = ObjectAnimator.ofInt(tueProgress, "progress",Utility.getProgress(), 100);
                objectAnimatorTue.setDuration(60000*60*9);
                objectAnimatorTue.setInterpolator(new DecelerateInterpolator());
                objectAnimatorTue.start();
            }
            else if(dayOfTheWeek.equals("Wednesday")){
                thuProgress.setMax(0);
                friProgress.setMax(0);
                satProgress.setMax(0);
                animtionProgress = ObjectAnimator.ofInt(wedProgress, "progress",Utility.getProgress(), 100);
                animtionProgress.setDuration(60000*60*9);
                animtionProgress.setInterpolator(new DecelerateInterpolator());
                animtionProgress.start();
            }
            else if(dayOfTheWeek.equals("Thursday")){
                friProgress.setMax(0);
                satProgress.setMax(0);
                animtionProgress = ObjectAnimator.ofInt(thuProgress, "progress",Utility.getProgress(), 100);
                animtionProgress.setDuration(60000*60*9);
                animtionProgress.setInterpolator(new DecelerateInterpolator());
                animtionProgress.start();
            }
            else if(dayOfTheWeek.equals("Friday")){
                satProgress.setMax(0);
                animtionProgress = ObjectAnimator.ofInt(friProgress, "progress",Utility.getProgress(), 100);
                animtionProgress.setDuration(60000*60*9);
                animtionProgress.setInterpolator(new DecelerateInterpolator());
                animtionProgress.start();

            }
            else if(dayOfTheWeek.equals("Saturday")){

                animtionProgress = ObjectAnimator.ofInt(satProgress, "progress",Utility.getProgress(), 100);
                animtionProgress.setDuration(60000*60*9);
                animtionProgress.setInterpolator(new DecelerateInterpolator());
                animtionProgress.start();

            }
            else if(dayOfTheWeek.equals("Sunday")){
                animtionProgress = ObjectAnimator.ofInt(sunProgress, "progress",Utility.getProgress(), 100);
                animtionProgress.setDuration(60000*60*9);
                animtionProgress.setInterpolator(new DecelerateInterpolator());
                animtionProgress.start();
            }

        }
        else{


            checkForStatusDays();

        }

    }


    public void chronometerStartFromPrevious() {


        long unixTimeMilis = System.currentTimeMillis();

        long currentTimeseconds = unixTimeMilis / 1000;

        long choronMili = currentTimeseconds-serverUnixSec;

        long mil = TimeUnit.SECONDS.toMillis(choronMili);

        chronometer.setBase(SystemClock.elapsedRealtime() - mil);

        chronometer.start();



    }

    private void findViewById() {

        btnIn = view.findViewById(R.id.btnIn);
        btnOut = view.findViewById(R.id.btnOut);

        monProgress = view.findViewById(R.id.monProgress);
        tueProgress = view.findViewById(R.id.tueProgress);
        wedProgress = view.findViewById(R.id.wedProgress);
        thuProgress = view.findViewById(R.id.thuProgress);
        friProgress = view.findViewById(R.id.friProgress);
        satProgress = view.findViewById(R.id.satProgress);
        sunProgress = view.findViewById(R.id.sunProgress);

        mChart =  view.findViewById(R.id.piechart);

        tvCurrentDate = view.findViewById(R.id.tvCurrentDate);
        chronometer = view.findViewById(R.id.chronometerTime);

    }


    private void resourcesSetters() {

//        imageViewHome.setBackgroundResource(R.drawable.homecolor);
//        textViewHome.setTextColor(getResources().getColorStateList(R.color.textSelectedColor));
//
//        imageViewTask.setBackgroundResource(R.drawable.taskoff);
//        imageViewAccount.setBackgroundResource(R.drawable.useroff);
//
//        textViewTask.setTextColor(getResources().getColorStateList(R.color.textUnselectedColor));
//        textViewAccount.setTextColor(getResources().getColorStateList(R.color.textUnselectedColor));


        //For calculate the current month
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        month = month_date.format(calendar.getTime());

        //For calculate the current date
        calendar.get(Calendar.YEAR);
        date = calendar.get(Calendar.DATE);

        //For calculate the current day of the week
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);

        tvCurrentDate.setText(dayOfTheWeek + " ," + date + " " + month);
    }

    private void pieChartValues() {

        mChart.setDescription("");

        mChart.setRotationEnabled(false);

        mChart.setHoleRadius(70);

        mChart.setHoleColor(R.color.blueHome);


        mChart.setOnChartValueSelectedListener(this);
    }



    private void countDownStart() {

        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                hsm =  (String)intent.getSerializableExtra("key");

                SimpleDateFormat formatter = new SimpleDateFormat(hsm);
                Date date = new Date(hoursTomili);
                formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

                String returnFormatinMilisec = formatter.format(date); //Final Result.
                String timeMili[] = returnFormatinMilisec.split(":");
                String hours = timeMili[0];

                int hourMil = Integer.parseInt(hours.substring(1));

                int hourmili = hourMil+1;

                hoursTomili = hourmili*60*60*1000;



            }
        };


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mMessageReceiver, new IntentFilter("hms"));
    }

    public void checkForStatusDays() {

        if(dayOfTheWeek.equals("Monday")){
            tueProgress.setMax(0);
            wedProgress.setMax(0);
            thuProgress.setMax(0);
            friProgress.setMax(0);
            satProgress.setMax(0);
            animtionProgress = ObjectAnimator.ofInt(monProgress, "progress",0, 100);
            animtionProgress.setDuration(60000*60);
            animtionProgress.setInterpolator(new DecelerateInterpolator());
            animtionProgress.start();
        }
        else if(dayOfTheWeek.equals("Tuesday")){
            wedProgress.setMax(0);
            thuProgress.setMax(0);
            friProgress.setMax(0);
            satProgress.setMax(0);
            animtionProgress = ObjectAnimator.ofInt(tueProgress, "progress",0, 100);
            animtionProgress.setDuration(60000*60);
            animtionProgress.getDuration();
            animtionProgress.setInterpolator(new DecelerateInterpolator());
            animtionProgress.start();
        }
        else if(dayOfTheWeek.equals("Wednesday")){
            thuProgress.setMax(0);
            friProgress.setMax(0);
            satProgress.setMax(0);
            animtionProgress = ObjectAnimator.ofInt(wedProgress, "progress",0, 100);
            animtionProgress.setDuration(60000*60);
            animtionProgress.setInterpolator(new DecelerateInterpolator());
            animtionProgress.start();
        }
        else if(dayOfTheWeek.equals("Thursday")){
            friProgress.setMax(0);
            satProgress.setMax(0);
            animtionProgress = ObjectAnimator.ofInt(thuProgress, "progress",0, 100);
            animtionProgress.setDuration(60000*30);
            animtionProgress.setInterpolator(new DecelerateInterpolator());
            animtionProgress.start();
        }
        else if(dayOfTheWeek.equals("Friday")){
            animtionProgress = ObjectAnimator.ofInt(friProgress, "progress",0, 100);
            animtionProgress.setDuration(60000*30);
            animtionProgress.setInterpolator(new DecelerateInterpolator());
            animtionProgress.start();

        }
        else if(dayOfTheWeek.equals("Saturday")){

            animtionProgress = ObjectAnimator.ofInt(satProgress, "progress",0, 100);
            animtionProgress.setDuration(60000*30);
            animtionProgress.setInterpolator(new DecelerateInterpolator());
            animtionProgress.start();

        }
        else if(dayOfTheWeek.equals("Sunday")){
            animtionProgress = ObjectAnimator.ofInt(sunProgress, "progress",0, 100);
            animtionProgress.setDuration(60000*60);
            animtionProgress.setInterpolator(new DecelerateInterpolator());
            animtionProgress.start();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        if(dayOfTheWeek.equals("Monday")){
            Utility.setProgress(monProgress.getProgress());

        }
        else if(dayOfTheWeek.equals("Tuesday")){
            Utility.setProgress(tueProgress.getProgress());

        }

        else if(dayOfTheWeek.equals("Wednesday")){
            Utility.setProgress(wedProgress.getProgress());

        }
        else if(dayOfTheWeek.equals("Thursday")){
            Utility.setProgress(thuProgress.getProgress());

        }
        else if(dayOfTheWeek.equals("Friday")){
            Utility.setProgress(friProgress.getProgress());

        }

        else if(dayOfTheWeek.equals("Saturday")){
            Utility.setProgress(satProgress.getProgress());

        }

        chronometerStop();


    }


    private void chronometerStart(){


        int stoppedMilliseconds = 0;

        String chronoText = chronometer.getText().toString();
        String array[] = chronoText.split(":");
        if (array.length == 2) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                    + Integer.parseInt(array[1]) * 1000;
        } else if (array.length == 3) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                    + Integer.parseInt(array[1]) * 60 * 1000
                    + Integer.parseInt(array[2]) * 1000;
        }

        chronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
        chronometer.start();

    }
    public void chronometerStop() {

        stoppedMilliseconds = 0;
        String chronoText = chronometer.getText().toString();
        String array[] = chronoText.split(":");
        if (array.length == 2) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                    + Integer.parseInt(array[1]) * 1000;
        } else if (array.length == 3) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                    + Integer.parseInt(array[1]) * 60 * 1000
                    + Integer.parseInt(array[2]) * 1000;
        }



        utility.setMiliseconds(stoppedMilliseconds);


        chronometer.stop();
    }


    @Override
    public void onResume() {
        super.onResume();

        countDownStart();


        Date currentTime = Calendar.getInstance().getTime();
        int hour =  currentTime.getHours();
        Intent intent = new Intent(getActivity(),CountdownService.class);
        intent.putExtra("time",hour);


        if(!isMyServiceRunning(CountdownService.class))
            getActivity().startService(intent);

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager)getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public void setDataForPieChartIFSuccessFalse() {

        //pie chart values
        int[] yValues = {720};


        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < yValues.length; i++)
            yVals1.add(new Entry(yValues[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xValues.length; i++)
            xVals.add(xValues[i]);

        // create pieDataSet
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setDrawValues(false);


        // adding colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // Added My Own colors
        for (int c : MY_COLORSFALSE)
            colors.add(c);


        dataSet.setColors(colors);

        //  create pie data object and set xValues and yValues and set it to the pieChart
        PieData data = new PieData(xVals, dataSet);

        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.BLACK);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.setDrawSliceText(false);


        // refresh/update pie chart
        mChart.invalidate();

        //  mChart.animateXY(1400, 1400);

        mChart.notifyDataSetChanged();

        // Legends to show on bottom of the graph
        Legend l = mChart.getLegend();

        l.setEnabled(false);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);


    }


    public void setDataForPieChart(ArrayList<Integer> punchList) {

        Integer[] yValues = new Integer[punchList.size()];
        yValues = punchList.toArray(yValues);

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // adding colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        xVals = new ArrayList<String>();

        for (int i = 0; i < yValues.length; i++) {
            yVals1.add(new Entry(yValues[i], i));
            if(i%2==0) {

                colors.add(Color.GREEN);
                xVals.add("In Time");
            }
            else {
                colors.add(Color.RED);
                xVals.add("Out Time");
            }
        }

        yVals1.add(new Entry(720-minsTotal, yVals1.size()));
        colors.add(Color.rgb(255,165,0));
        xVals.add("Pending Time");


        // create pieDataSet
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setDrawValues(false);
        dataSet.setSliceSpace(0f);

        dataSet.setColors(colors);

        //  create pie data object and set xValues and yValues and set it to the pieChart
        PieData data = new PieData(xVals, dataSet);

        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.BLACK);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.setDrawSliceText(false);

        // refresh/update pie chart
        mChart.invalidate();

        //  mChart.animateXY(1400, 1400);
        mChart.notifyDataSetChanged();

        // Legends to show on bottom of the graph
        Legend l = mChart.getLegend();

        l.setEnabled(false);
        l.setForm(Legend.LegendForm.SQUARE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setTextSize(8f);
        l.setTextColor(Color.WHITE);
        l.setXEntrySpace(7f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(7f);

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null)
            return;

        Toast.makeText(getActivity(), xVals.get(e.getXIndex()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {}

    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + ""; // e.g. append a dollar-sign
        }
    }

    private ArrayList<Integer> SelectTimeApi() {

        returnVal = new ArrayList();

        Utility utility = new Utility(getActivity());

        HashMap<String,String> stringStringHashMap = new HashMap<>();

        stringStringHashMap.put("employeId", String.valueOf(utility.getEmployerId()));

        final RestClient1.GitApiInterface restClient = RestClient1.getClient();

        restClient.selectTime(stringStringHashMap).enqueue(new Callback<SelectTimeSuccess>() {
            @Override
            public void onResponse(Call<SelectTimeSuccess> call, Response<SelectTimeSuccess> response) {

                if(response.body().getIsSuccess()){

                    selectTimePayloadArrayList = new ArrayList<>(response.body().getPayload());

                    minsTotal = 0;

                    if(selectTimePayloadArrayList.size()==1){


                        for(int j = 0;j<selectTimePayloadArrayList.size();j++){

                            serverUnixSec = selectTimePayloadArrayList.get(j).getPunchtime();

                            AnglePieChart();

                            long totalSec = unixTimeSec-serverUnixSec;

                            long totalMin = totalSec/60;

                            minsTotal+=totalMin;

                            chronometerStartFromZero();

                            returnVal.add((int) totalMin);
                        }
                    }
                    else {

                        for (int i = 0; i < selectTimePayloadArrayList.size() - 1; i++) {

                            serverUnixSec = selectTimePayloadArrayList.get(0).getPunchtime();

                            int size =selectTimePayloadArrayList.size();

                            status = selectTimePayloadArrayList.get(size-1).getStatus();

                            lastouttime = selectTimePayloadArrayList.get(size-1).getPunchtime();



                            if(status.equalsIgnoreCase("Out")){


                               chronometerStop();



                            }

                            else if(status.equalsIgnoreCase("In")){

                                //chronometerStartFromPrevious();
                                chronometerStart();
                            }

                            AnglePieChart();

                            Integer totalseconds = selectTimePayloadArrayList.get(i + 1).getPunchtime() - selectTimePayloadArrayList.get(i).getPunchtime();
                            totalmins = totalseconds / 60;
                            minsTotal += totalmins;

                            returnVal.add(totalmins);

                        }


                    }

                    setDataForPieChart(returnVal);

                    sumArrayListEtm();

                   // chronometerStartFromPrevious();

                }
                else {


                    setDataForPieChartIFSuccessFalse();

                }
            }
            @Override
            public void onFailure(Call<SelectTimeSuccess> call, Throwable t) {

            }
        });
        return returnVal;
    }



    private void sumArrayListEtm() {

        sum = 0;
        for(int d : returnVal)
            sum += d;

    }

    public void AnglePieChart(){

        java.util.Date dateTime=new java.util.Date((long)serverUnixSec*1000);

        int hours = dateTime.getHours();
        int min = dateTime.getMinutes();

        double timeDegree = (0.5*(60*hours+min)-90);

        String[] arr=String.valueOf(timeDegree).split("\\.");

        rotationDeg= Integer.parseInt(arr[0]);

        mChart.setRotationAngle(rotationDeg);


    }


}




