package com.i9930.croptrails.HarvestReport.Model;


import com.google.gson.annotations.Expose;

import com.google.gson.annotations.SerializedName;


public class HarvestDetailMaster {
    boolean clicked;
    @SerializedName("harvest_master_id")
    @Expose
    private String harvestMasterId;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("harvest_date")
    @Expose
    private String harvestDate;
    @SerializedName("weighment_date")
    @Expose
    private String weighmentDate;
    @SerializedName("supervisor_id")
    @Expose
    private String supervisorId;
    @SerializedName("total_net_weight")
    @Expose
    private String totalNetWeight;
    @SerializedName("total_gross_weight")
    @Expose

    private String totalGrossWeight;

    @SerializedName("area_harvested")

    @Expose

    private String harvestedArea;

    @SerializedName("standing_acres")

    @Expose

    private String standingArea;

    @SerializedName("doa")

    @Expose

    private String doa;

    @SerializedName("is_active")

    @Expose

    private String isActive;


    public String getHarvestMasterId() {

        return harvestMasterId;

    }


    public void setHarvestMasterId(String harvestMasterId) {

        this.harvestMasterId = harvestMasterId;

    }


    public String getFarmId() {

        return farmId;

    }


    public void setFarmId(String farmId) {

        this.farmId = farmId;

    }


    public String getCompId() {

        return compId;

    }


    public void setCompId(String compId) {

        this.compId = compId;

    }


    public String getHarvestDate() {

        return harvestDate;

    }


    public void setHarvestDate(String harvestDate) {

        this.harvestDate = harvestDate;

    }


    public String getWeighmentDate() {

        return weighmentDate;

    }


    public void setWeighmentDate(String weighmentDate) {

        this.weighmentDate = weighmentDate;

    }


    public String getSupervisorId() {

        return supervisorId;

    }


    public void setSupervisorId(String supervisorId) {

        this.supervisorId = supervisorId;

    }


    public String getTotalNetWeight() {

        return totalNetWeight;

    }


    public void setTotalNetWeight(String totalNetWeight) {

        this.totalNetWeight = totalNetWeight;

    }


    public String getTotalGrossWeight() {

        return totalGrossWeight;

    }


    public void setTotalGrossWeight(String totalGrossWeight) {

        this.totalGrossWeight = totalGrossWeight;

    }


    public String getHarvestedArea() {

        return harvestedArea;

    }


    public void setHarvestedArea(String harvestedArea) {

        this.harvestedArea = harvestedArea;

    }


    public String getStandingArea() {

        return standingArea;

    }


    public void setStandingArea(String standingArea) {

        this.standingArea = standingArea;

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

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}