package com.i9930.croptrails.SubmitActivityForm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.CommonClasses.TimelineUnits;

public class ActivityUnitResponse {

    @SerializedName("data")
    @Expose
    private List<TimelineUnits> data = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<TimelineUnits> getData() {
        return data;
    }

    public void setData(List<TimelineUnits> data) {
        this.data = data;
    }

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
}
