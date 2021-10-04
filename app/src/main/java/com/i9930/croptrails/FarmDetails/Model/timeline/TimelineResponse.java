package com.i9930.croptrails.FarmDetails.Model.timeline;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TimelineResponse {


    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("timeline")
    @Expose
    private List<TimelineInnerData> farmCalData = null;


    @SerializedName("expense")
    @Expose
    private String expense;



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

    public List<TimelineInnerData> getFarmCalData() {
        return farmCalData;
    }

    public void setFarmCalData(List<TimelineInnerData> farmCalData) {
        this.farmCalData = farmCalData;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }
}
