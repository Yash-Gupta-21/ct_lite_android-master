package com.i9930.croptrails.CommonClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusMsgModel {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("Sat_msg")
    @Expose
    private String satMsg;

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


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSatMsg() {
        return satMsg;
    }

    public void setSatMsg(String satMsg) {
        this.satMsg = satMsg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}