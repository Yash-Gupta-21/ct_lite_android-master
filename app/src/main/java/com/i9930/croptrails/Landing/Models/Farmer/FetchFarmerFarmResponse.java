package com.i9930.croptrails.Landing.Models.Farmer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.Landing.Models.FetchFarmResult;

public class FetchFarmerFarmResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<FetchFarmResult> farm = null;

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

    public List<FetchFarmResult> getFarm() {
        return farm;
    }

    public void setFarm(List<FetchFarmResult> farm) {
        this.farm = farm;
    }
}
