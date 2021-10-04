package com.i9930.croptrails.SubmitActivityForm.Model.AgriInput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AgriInputResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data1")
    @Expose
    private Data1 data1;
    @SerializedName("status")
    @Expose
    private Integer status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data1 getData1() {
        return data1;
    }

    public void setData1(Data1 data1) {
        this.data1 = data1;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
