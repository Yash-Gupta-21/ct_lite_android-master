package com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hp on 08-09-2018.
 */

public class GpsMainDataForSingleHarvest {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<GpsDataForSingleHarvest> data = null;

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

    public List<GpsDataForSingleHarvest> getData() {
        return data;
    }

    public void setData(List<GpsDataForSingleHarvest> data) {
        this.data = data;
    }
}
