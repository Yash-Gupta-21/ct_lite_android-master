package com.i9930.croptrails.HarvestReport.Model;


import com.google.gson.annotations.Expose;

import com.google.gson.annotations.SerializedName;


import java.util.List;


public class ViewHarvestDetails{

    @SerializedName("farmId")
    @Expose
    private String farmId;

    @SerializedName("user_id")
    @Expose
    private String userId;
    
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<List<HarvestDetailInnerData>> data = null;
    @SerializedName("data1")
    @Expose
    private List<HarvestDetailMaster> data1 = null;

    String isUploaded;

    public List<HarvestDetailMaster> getData1() {

        return data1;

    }


    public void setData1(List<HarvestDetailMaster> data1) {
        this.data1 = data1;
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


    public List<List<HarvestDetailInnerData>> getData() {

        return data;

    }


    public void setData(List<List<HarvestDetailInnerData>> data) {

        this.data = data;

    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

