package com.i9930.croptrails.HarvestCollection.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 31-08-2018.
 */

public class HarvestCollectionTotalGrossData {
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
    private String areaHarvested;
    @SerializedName("standing_acres")
    @Expose
    private String standingAcres;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("lot_no")
    @Expose
    private String lotNo;
    @SerializedName("addL1")
    @Expose
    private String addL1;
    @SerializedName("addL2")
    @Expose
    private String addL2;
    @SerializedName("village_or_city")
    @Expose
    private String villageOrCity;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("mandal_or_tehsil")
    @Expose
    private String mandalOrTehsil;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("name")
    @Expose
    private String name;

    private boolean makeOpen;

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getAddL1() {
        return addL1;
    }

    public void setAddL1(String addL1) {
        this.addL1 = addL1;
    }

    public String getAddL2() {
        return addL2;
    }

    public void setAddL2(String addL2) {
        this.addL2 = addL2;
    }

    public String getVillageOrCity() {
        return villageOrCity;
    }

    public void setVillageOrCity(String villageOrCity) {
        this.villageOrCity = villageOrCity;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getMandalOrTehsil() {
        return mandalOrTehsil;
    }

    public void setMandalOrTehsil(String mandalOrTehsil) {
        this.mandalOrTehsil = mandalOrTehsil;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getAreaHarvested() {
        return areaHarvested;
    }

    public void setAreaHarvested(String areaHarvested) {
        this.areaHarvested = areaHarvested;
    }

    public String getStandingAcres() {
        return standingAcres;
    }

    public void setStandingAcres(String standingAcres) {
        this.standingAcres = standingAcres;
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

    public boolean isMakeOpen() {
        return makeOpen;
    }

    public void setMakeOpen(boolean makeOpen) {
        this.makeOpen = makeOpen;
    }
}

