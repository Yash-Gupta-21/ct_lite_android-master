package com.i9930.croptrails.ShowInputCost.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.SubmitInputCost.Model.ResourceDataDD;

public class InputCostResponse {
    @SerializedName("resources")
    @Expose
    private List<ResourceData> resources = null;
    @SerializedName("input_cost")
    @Expose
    private List<InputCostData> inputCost = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<ResourceData> getResourceList() {
        return resources;
    }

    public void setResourcesList(List<ResourceData> resources) {
        this.resources = resources;
    }

    public List<InputCostData> getInputCostList() {
        return inputCost;
    }

    public void setInputCostList(List<InputCostData> inputCost) {
        this.inputCost = inputCost;
    }

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

}
