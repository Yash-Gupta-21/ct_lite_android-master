package com.i9930.croptrails.Test.SatusreModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SatsureResponse {

    /*@SerializedName("data")
    @Expose
    private SatsureData data;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public SatsureData getData() {
        return data;
    }

    public void setData(SatsureData data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
*/

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("SatelliteDataImages")
    @Expose
    List<SatsureData> data;

    @SerializedName("data")
    @Expose
    List<List<SatsureData>> satsureData;

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

    public List<SatsureData> getData() {
        return data;
    }

    public void setData(List<SatsureData> data) {
        this.data = data;
    }


    public List<List<SatsureData>> getSatsureData() {
        return satsureData;
    }

    public void setSatsureData(List<List<SatsureData>> satsureData) {
        this.satsureData = satsureData;
    }
}
