package com.i9930.croptrails.Maps.ShowArea.Model.near;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearFarmResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("gps_data")
    @Expose
    private List<GpsDatum> gpsData = null;
    @SerializedName("nearBy")
    @Expose
    private List<NearBy> nearBy = null;

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

    public List<GpsDatum> getGpsData() {
        return gpsData;
    }

    public void setGpsData(List<GpsDatum> gpsData) {
        this.gpsData = gpsData;
    }

    public List<NearBy> getNearBy() {
        return nearBy;
    }

    public void setNearBy(List<NearBy> nearBy) {
        this.nearBy = nearBy;
    }
}
