package com.i9930.croptrails.HarvestPlan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hp on 07-09-2018.
 */

public class ShowHarvestPlanMain {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<ShowHarvestPlanData> data = null;
    @SerializedName("vehicle_details")
    @Expose
    private List<ShowHarvestVehicleData> vehicleDetails = null;

    public List<ShowHarvestVehicleData> getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(List<ShowHarvestVehicleData> vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
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

    public List<ShowHarvestPlanData> getData() {
        return data;
    }

    public void setData(List<ShowHarvestPlanData> data) {
        this.data = data;
    }
}
