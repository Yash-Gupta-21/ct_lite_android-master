package com.i9930.croptrails.SubmitHealthCard.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HealthCardDDResponse {

    @SerializedName("status")
    @Expose
    int status;
    @SerializedName("msg")
    @Expose
    String msg;

    @SerializedName("data")
    @Expose
    List<HealthCardParams>healthCardParamsList;

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

    public List<HealthCardParams> getHealthCardParamsList() {
        return healthCardParamsList;
    }

    public void setHealthCardParamsList(List<HealthCardParams> healthCardParamsList) {
        this.healthCardParamsList = healthCardParamsList;
    }
}
