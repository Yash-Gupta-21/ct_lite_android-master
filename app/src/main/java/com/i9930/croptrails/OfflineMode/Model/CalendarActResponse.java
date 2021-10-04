package com.i9930.croptrails.OfflineMode.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.SubmitActivityForm.Model.DoneActNew.DoneActivityResponseNew;

public class CalendarActResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("timelinedata")
    @Expose
    List<DoneActivityResponseNew> doneActivityList;

    public List<DoneActivityResponseNew> getDoneActivityList() {
        return doneActivityList;
    }

    public void setDoneActivityList(List<DoneActivityResponseNew> doneActivityList) {
        this.doneActivityList = doneActivityList;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
