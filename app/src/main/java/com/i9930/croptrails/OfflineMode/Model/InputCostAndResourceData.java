package com.i9930.croptrails.OfflineMode.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.ShowInputCost.Model.InputCostData;
import com.i9930.croptrails.ShowInputCost.Model.ResourceData;

public class InputCostAndResourceData {

    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("resource")
    @Expose
    private List<ResourceData> resourceDataList = null;
    @SerializedName("input_cost")
    @Expose
    private List<InputCostData> inputCostDataList = null;

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public List<ResourceData> getResourceDataList() {
        return resourceDataList;
    }

    public void setResourceDataList(List<ResourceData> resourceDataList) {
        this.resourceDataList = resourceDataList;
    }

    public List<InputCostData> getInputCostDataList() {
        return inputCostDataList;
    }

    public void setInputCostDataList(List<InputCostData> inputCostDataList) {
        this.inputCostDataList = inputCostDataList;
    }
}
