package com.smartitventures.employeeapp.Response.Response.ChatMessageDisp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dharamveer on 6/11/17.
 */

public class ChatDisplaySuccess {

    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("isError")
    @Expose
    private Boolean isError;
    @SerializedName("payload")
    @Expose
    private List<ChatDisplayPayload> payload = null;

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

    public List<ChatDisplayPayload> getPayload() {
        return payload;
    }

    public void setPayload(List<ChatDisplayPayload> payload) {
        this.payload = payload;
    }
}
