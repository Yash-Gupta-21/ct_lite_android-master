package com.i9930.croptrails.Landing.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FarmCountResponse {
    @SerializedName("farms")
    @Expose
    private List<FarmCountDatum> farms = null;
    @SerializedName("inactive")
    @Expose
    private List<FarmCountDatum> inactive = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<FarmCountDatum> getFarms() {
        return farms;
    }

    public void setFarms(List<FarmCountDatum> farms) {
        this.farms = farms;
    }

    public List<FarmCountDatum> getInactive() {
        return inactive;
    }

    public void setInactive(List<FarmCountDatum> inactive) {
        this.inactive = inactive;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
