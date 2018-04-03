package com.smartitventures.employeeapp.RetrofitClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartitventures.employeeapp.Response.Response.AllTaskTitles.AllTaskTitles;
import com.smartitventures.employeeapp.Response.Response.AllTaskTitles.TitlesPayload;
import com.smartitventures.employeeapp.Response.Response.AttendanceLog.AttendenceLog;
import com.smartitventures.employeeapp.Response.Response.ChatMessageDisp.ChatDisplaySuccess;
import com.smartitventures.employeeapp.Response.Response.GetNotification.GetNotification;
import com.smartitventures.employeeapp.Response.Response.InsertToken.InsertToken;
import com.smartitventures.employeeapp.Response.Response.SelectTimeResponse.SelectTimeSuccess;
import com.smartitventures.employeeapp.Response.Response.TaskStatus.TaskStatus;
import com.smartitventures.employeeapp.Response.Response.ValidateAccessCode.AccessCodeSuccess;
import com.smartitventures.employeeapp.Response.Response.TaskHistroy.HistorySuccess;
import com.smartitventures.employeeapp.Response.Response.TaskDetails.TaskSuccess;
import com.smartitventures.employeeapp.Response.Response.ValidateDeviceId.ValidateDeviceIdSuccess;
import com.smartitventures.employeeapp.Response.Response.addTaskManually.AddTask;
import com.smartitventures.employeeapp.Response.Response.chatResponse.Chat;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class RestClient1 {


    private static GitApiInterface gitApiInterface;


    //url --http://smartit.ventures/employerApi/v1/validateDeviceId
    private static String baseUrl = "http://smartit.ventures/";

    //    http://192.168.0.29/Chefcreation/v1/selectproducts
    public static GitApiInterface getClient() {


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //The logging interceptor will be added to the http client

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //The Retrofit builder will have the client attached, in order to get connection logs
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())

                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .build();


        gitApiInterface = retrofit.create(GitApiInterface.class);

        return gitApiInterface;


    }

    public interface GitApiInterface {

        @POST("employerApi/v1/validateDeviceId")
        Call<ValidateDeviceIdSuccess> ValidateDeviceId(@Body HashMap<String, String> hashMap);

        @POST("employerApi/v1/validateAccessCode")
        Call<AccessCodeSuccess> validateaccessCode(@Body HashMap<String, String> hashMap);

        @POST("employerApi/v1/taskDetails")
        Call<TaskSuccess> taskSuccess(@Body HashMap<String, String> hashMap);


        @POST("employerApi/v1/taskHistory")
        Call<HistorySuccess> taskHistory(@Body HashMap<String, String> hashMap);

        @POST("employerApi/v1/chat")
        Call<Chat> chat(@Body HashMap<String, String> hashMap);


        @POST("employerApi/v1/addTask")
        Call<AddTask> addTask(@Body HashMap<String, String> hashMap);

        @POST("employerApi/v1/insertToken")
        Call<InsertToken> insertToken(@Body HashMap<String, String> hashMap);


        @POST("employerApi/v1/getAllTokens")
        Call<GetNotification> getNotification(@Body HashMap<String, String> hashMap);


        @POST("employerApi/v1/selectAllTaskTitles")
        Call<AllTaskTitles> getAllTaskTitles(@Body HashMap<String, String> hashMap);

        @POST("employerApi/v1/taskStatus")
        Call<TaskStatus> taskStatus(@Body HashMap<String, String> hashMap);


        @POST("employerApi/v1/attendenceLog")
        Call<AttendenceLog> attendanceLog(@Body HashMap<String, String> hashMap);

        @POST("employerApi/v1/selectTime")
        Call<SelectTimeSuccess> selectTime(@Body HashMap<String, String> hashMap);

        @POST("employerApi/v1/messageDisplay")
        Call<ChatDisplaySuccess> chatDisplay(@Body HashMap<String, String> hashMap);

    }
}