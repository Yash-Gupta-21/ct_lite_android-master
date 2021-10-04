package com.i9930.croptrails.OfflineMode.send;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.ShowInputCost.Model.InputCostData;

public class SendOfflineInputCost {
    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("inputs_i2")
    List<InputCostData>inputCostDataList;

    public SendOfflineInputCost(List<InputCostData> inputCostDataList,String userId) {
        this.inputCostDataList = inputCostDataList;
        this.userId=userId;
    }

    public List<InputCostData> getInputCostDataList() {
        return inputCostDataList;
    }

    public void setInputCostDataList(List<InputCostData> inputCostDataList) {
        this.inputCostDataList = inputCostDataList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
