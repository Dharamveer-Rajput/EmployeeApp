package com.smartitventures.employeeapp.Response.Response.TaskHistroy;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class HistorySuccess {

    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("isError")
    @Expose
    private Boolean isError;
    @SerializedName("payload")
    @Expose
    private List<HistoryPayload> payload = null;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Boolean getIsError() {
        return isError;
    }

    public void setIsError(Boolean isError) {
        this.isError = isError;
    }

    public List<HistoryPayload> getPayload() {
        return payload;
    }

    public void setPayload(List<HistoryPayload> payload) {
        this.payload = payload;
    }

}