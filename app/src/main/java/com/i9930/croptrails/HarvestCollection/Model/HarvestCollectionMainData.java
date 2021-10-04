package com.i9930.croptrails.HarvestCollection.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hp on 31-08-2018.
 */

public class HarvestCollectionMainData {
    @SerializedName("data1")
    @Expose
    private List<List<HarvestCollectionTotalGrossData>> data1 = null;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<List<HarvestCollectionData>> data = null;

    public List<List<HarvestCollectionTotalGrossData>> getData1() {
        return data1;
    }

    public void setData1(List<List<HarvestCollectionTotalGrossData>> data1) {
        this.data1 = data1;
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

    public List<List<HarvestCollectionData>> getData() {
        return data;
    }

    public void setData(List<List<HarvestCollectionData>> data) {
        this.data = data;
    }
}
