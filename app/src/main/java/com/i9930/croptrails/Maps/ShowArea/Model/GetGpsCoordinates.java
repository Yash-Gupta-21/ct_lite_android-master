package com.i9930.croptrails.Maps.ShowArea.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetGpsCoordinates {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("data")
    @Expose
    private List<LatLngFarm> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<LatLngFarm> getData() {
        return data;
    }

    public void setData(List<LatLngFarm> data) {
        this.data = data;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }
}