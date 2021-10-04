package com.i9930.croptrails.Maps.ShowArea.Model.geojson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;

public class GeoJsonResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<FarmGeoJsonDatum> data = null;

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

    public List<FarmGeoJsonDatum> getData() {
        return data;
    }

    public void setData(List<FarmGeoJsonDatum> data) {
        this.data = data;
    }
}
