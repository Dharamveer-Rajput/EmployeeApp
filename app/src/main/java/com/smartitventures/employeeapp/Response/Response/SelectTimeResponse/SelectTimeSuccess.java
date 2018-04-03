package com.smartitventures.employeeapp.Response.Response.SelectTimeResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SelectTimeSuccess {

    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("isError")
    @Expose
    private Boolean isError;
    @SerializedName("payload")
    @Expose
    private List<SelectTimePayload> payload = null;

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

    public List<SelectTimePayload> getPayload() {
        return payload;
    }

    public void setPayload(List<SelectTimePayload> payload) {
        this.payload = payload;
    }

}