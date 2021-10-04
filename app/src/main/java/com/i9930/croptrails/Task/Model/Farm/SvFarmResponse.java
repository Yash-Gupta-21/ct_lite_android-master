package com.i9930.croptrails.Task.Model.Farm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.RoomDatabase.Task.SvTask;

public class SvFarmResponse {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<SvFarm> farmList = null;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<SvFarm> getData() {
        return farmList;
    }

    public void setData(List<SvFarm> data) {
        this.farmList = data;
    }

}
