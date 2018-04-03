package com.smartitventures.employeeapp.Response.Response.ValidateDeviceId;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidateDeviceIdSuccess {

    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("isError")
    @Expose
    private Boolean isError;
    @SerializedName("payload")
    @Expose
    private ValidateDeviceIdPayload payload;

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

    public ValidateDeviceIdPayload getPayload() {
        return payload;
    }

    public void setPayload(ValidateDeviceIdPayload payload) {
        this.payload = payload;
    }

}