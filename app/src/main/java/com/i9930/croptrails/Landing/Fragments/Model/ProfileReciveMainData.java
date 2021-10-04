package com.i9930.croptrails.Landing.Fragments.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 31-07-2018.
 */

public class ProfileReciveMainData {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("result")
    @Expose
    private ResultProfile result;

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

    public ResultProfile getResult() {
        return result;
    }

    public void setResult(ResultProfile result) {
        this.result = result;
    }
}
