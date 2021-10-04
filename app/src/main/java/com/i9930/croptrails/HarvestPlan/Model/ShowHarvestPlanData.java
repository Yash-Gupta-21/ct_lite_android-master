package com.i9930.croptrails.HarvestPlan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 07-09-2018.
 */

public class ShowHarvestPlanData {

    @SerializedName("hcmid")
    @Expose
    private String hcmid;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("vehicle_master_id")
    @Expose
    private String vehicleMasterId;
    @SerializedName("agent_code")
    @Expose
    private String agentCode;
    @SerializedName("net_wt_of_stock")
    @Expose
    private String netWtOfStock;
    @SerializedName("no_of_bags_blk")
    @Expose
    private String noOfBagsBlk;
    @SerializedName("total_bags")
    @Expose
    private String totalBags;
    @SerializedName("avg_moisture")
    @Expose
    private String avgMoisture;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("trans_admin_id")
    @Expose
    private String transAdminId;
    @SerializedName("svid")
    @Expose
    private String svid;
    @SerializedName("plan_pickup_date")
    @Expose
    private String planPickupDate;
    @SerializedName("arrival_timestamp")
    @Expose
    private String arrivalTimestamp;
    @SerializedName("receiving_officer_id")
    @Expose
    private String receivingOfficerId;
    @SerializedName("office_remarks")
    @Expose
    private String officeRemarks;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("is_active")
    @Expose
    private String isActive;


    public String getHcmid() {
        return hcmid;
    }

    public void setHcmid(String hcmid) {
        this.hcmid = hcmid;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getVehicleMasterId() {
        return vehicleMasterId;
    }

    public void setVehicleMasterId(String vehicleMasterId) {
        this.vehicleMasterId = vehicleMasterId;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getPlanPickupDate() {
        return planPickupDate;
    }

    public void setPlanPickupDate(String planPickupDate) {
        this.planPickupDate = planPickupDate;
    }

    public String getNetWtOfStock() {
        return netWtOfStock;
    }

    public void setNetWtOfStock(String netWtOfStock) {
        this.netWtOfStock = netWtOfStock;
    }

    public String getNoOfBagsBlk() {
        return noOfBagsBlk;
    }

    public void setNoOfBagsBlk(String noOfBagsBlk) {
        this.noOfBagsBlk = noOfBagsBlk;
    }

    public String getTotalBags() {
        return totalBags;
    }

    public void setTotalBags(String totalBags) {
        this.totalBags = totalBags;
    }

    public String getAvgMoisture() {
        return avgMoisture;
    }

    public void setAvgMoisture(String avgMoisture) {
        this.avgMoisture = avgMoisture;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTransAdminId() {
        return transAdminId;
    }

    public void setTransAdminId(String transAdminId) {
        this.transAdminId = transAdminId;
    }

    public String getSvid() {
        return svid;
    }

    public void setSvid(String svid) {
        this.svid = svid;
    }

    public String getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public void setArrivalTimestamp(String arrivalTimestamp) {
        this.arrivalTimestamp = arrivalTimestamp;
    }

    public String getReceivingOfficerId() {
        return receivingOfficerId;
    }

    public void setReceivingOfficerId(String receivingOfficerId) {
        this.receivingOfficerId = receivingOfficerId;
    }

    public String getOfficeRemarks() {
        return officeRemarks;
    }

    public void setOfficeRemarks(String officeRemarks) {
        this.officeRemarks = officeRemarks;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
