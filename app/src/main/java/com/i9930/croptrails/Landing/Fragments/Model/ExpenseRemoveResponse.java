package com.i9930.croptrails.Landing.Fragments.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenseRemoveResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
//    @SerializedName("data")
//    @Expose
//    private Boolean data;

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

//    public Boolean getData() {
//        return data;
//    }
//
//    public void setData(Boolean data) {
//        this.data = data;
//    }

}
