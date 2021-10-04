package com.i9930.croptrails.HarvestPlan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 07-09-2018.
 */

public class ShowHarvestVehicleData {
    private boolean isSortListAdded;

    public boolean isSortListAdded() {
        return isSortListAdded;
    }

    public void setSortListAdded(boolean sortListAdded) {
        isSortListAdded = sortListAdded;
    }
    @SerializedName("vehicle_master_id")
    @Expose
    private String vehicleMasterId;
    @SerializedName("vehicle_no")
    @Expose
    private String vehicleNo;
    @SerializedName("vehicle_net_wt")
    @Expose
    private String vehicleNetWt;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("doa")
    @Expose
    private String doa;

    @SerializedName("pickup_date")
    @Expose
    private String pickupDate;
    @SerializedName("noOfBags")
    @Expose
    private String noOfBags;
    @SerializedName("netWeight")
    @Expose
    private String netWeight;

    public String getVehicleMasterId() {
        return vehicleMasterId;
    }

    public void setVehicleMasterId(String vehicleMasterId) {
        this.vehicleMasterId = vehicleMasterId;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleNetWt() {
        return vehicleNetWt;
    }

    public void setVehicleNetWt(String vehicleNetWt) {
        this.vehicleNetWt = vehicleNetWt;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getNoOfBags() {
        return noOfBags;
    }

    public void setNoOfBags(String noOfBags) {
        this.noOfBags = noOfBags;
    }

    public String getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }
}
