package com.smartitventures.employeeapp.Response.Response.SelectTimeResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dharamveer on 3/11/17.
 */

public class SelectTimePayload {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("punchtime")
    @Expose
    private Integer punchtime;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPunchtime() {
        return punchtime;
    }

    public void setPunchtime(Integer punchtime) {
        this.punchtime = punchtime;
    }

}