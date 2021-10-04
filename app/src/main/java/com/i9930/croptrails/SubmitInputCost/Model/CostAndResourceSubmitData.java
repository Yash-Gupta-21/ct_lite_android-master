package com.i9930.croptrails.SubmitInputCost.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CostAndResourceSubmitData {

    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;
    @SerializedName("resource_item")
    List<ResourceSubmitDatum> resourceSubmitList ;
    @SerializedName("input_item")
    List<InputCostSubmitDatum> inputCostSubmitList;

    public List<ResourceSubmitDatum> getResourceSubmitList() {
        return resourceSubmitList;
    }

    public void setResourceSubmitList(List<ResourceSubmitDatum> resourceSubmitList) {
        this.resourceSubmitList = resourceSubmitList;
    }

    public List<InputCostSubmitDatum> getInputCostSubmitList() {
        return inputCostSubmitList;
    }

    public void setInputCostSubmitList(List<InputCostSubmitDatum> inputCostSubmitList) {
        this.inputCostSubmitList = inputCostSubmitList;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
