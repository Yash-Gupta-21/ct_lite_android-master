package com.i9930.croptrails.NoticeBoard.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NoticeResponse {

    @SerializedName("data")
    @Expose
    private List<NoticeDatum> data = null;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<NoticeDatum> getData() {
        return data;
    }

    public void setData(List<NoticeDatum> data) {
        this.data = data;
    }

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
}
