package com.i9930.croptrails.AddFarm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IfscResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private IfscData data;
    @SerializedName("message")
    @Expose
    private Object message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public IfscData getData() {
        return data;
    }

    public void setData(IfscData data) {
        this.data = data;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
