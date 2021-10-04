package com.i9930.croptrails.AddFarm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchFarmerResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<FarmerSpinnerData> farmerDataList = null;

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

    public List<FarmerSpinnerData> getFarmerDataList() {
        return farmerDataList;
    }

    public void setFarmerDataList(List<FarmerSpinnerData> farmerDataList) {
        this.farmerDataList = farmerDataList;
    }
}
