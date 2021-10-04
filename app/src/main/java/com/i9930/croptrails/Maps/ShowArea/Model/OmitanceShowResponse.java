package com.i9930.croptrails.Maps.ShowArea.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel.SendOmtanceArea;

public class OmitanceShowResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<OmiFetchDatum> data = null;

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

    public List<OmiFetchDatum> getData() {
        return data;
    }

    public void setData(List<OmiFetchDatum> data) {
        this.data = data;
    }
}
