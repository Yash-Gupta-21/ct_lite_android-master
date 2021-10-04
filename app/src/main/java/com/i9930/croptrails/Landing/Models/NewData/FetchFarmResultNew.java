package com.i9930.croptrails.Landing.Models.NewData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchFarmResultNew {
    boolean isSelectedd;
    @SerializedName("sv_id")
    @Expose
    private String svId;
    @SerializedName("cluster_id")
    @Expose
    private String clusterId;
    @SerializedName("cluster_name")
    @Expose
    private String clusterName;
    @SerializedName("farm_master_num")
    @Expose
    private String farmMasterNum;
    @SerializedName("farm_name")
    @Expose
    private String farmName;
    @SerializedName("farmerName")
    @Expose
    private String farmerName;
    @SerializedName("farmerId")
    @Expose
    private String farmerId;
    @SerializedName("lot_no")
    @Expose
    private String lotNo;
    @SerializedName("exp_harvest_date")
    @Expose
    private String expHarvestDate;
    @SerializedName("actual_harvest_date")
    @Expose
    private String actualHarvestDate;
    @SerializedName("standing_area")
    @Expose
    private Double standingArea;
    @SerializedName("actual_area")
    @Expose
    private Double actualArea;
    @SerializedName("exp_area")
    @Expose
    private Double expArea;
    @SerializedName("mob")
    @Expose
    private String mob;
    @SerializedName("father_name")
    @Expose
    private String fatherName;
    @SerializedName("img_link")
    @Expose
    private String imgLink;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("isSelected")
    @Expose
    private String vetting;
    @SerializedName("address_id")
    @Expose
    private String addressId;
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
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("spouse_name")
    @Expose
    private String spouseName;
    @SerializedName("farmer_village_or_city")
    @Expose
    private String farmerVillageOrCity;
    @SerializedName("farmer_addL1")
    @Expose
    private String farmerAddL1;
    @SerializedName("farmer_addL2")
    @Expose
    private String farmerAddL2;
    @SerializedName("farmer_district")
    @Expose
    private String farmerDistrict;
    @SerializedName("farmer_mandal_or_tehsil")
    @Expose
    private String farmerMandalOrTehsil;
    @SerializedName("farmer_state")
    @Expose
    private String farmerState;
    @SerializedName("sowing_date")
    @Expose
    private String sowingDate;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getFarmMasterNum() {
        return farmMasterNum;
    }

    public void setFarmMasterNum(String farmMasterNum) {
        this.farmMasterNum = farmMasterNum;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getExpHarvestDate() {
        return expHarvestDate;
    }

    public void setExpHarvestDate(String expHarvestDate) {
        this.expHarvestDate = expHarvestDate;
    }

    public String getActualHarvestDate() {
        return actualHarvestDate;
    }

    public void setActualHarvestDate(String actualHarvestDate) {
        this.actualHarvestDate = actualHarvestDate;
    }

    public Double getStandingArea() {
        return standingArea;
    }

    public void setStandingArea(Double standingArea) {
        this.standingArea = standingArea;
    }

    public Double getActualArea() {
        return actualArea;
    }

    public void setActualArea(Double actualArea) {
        this.actualArea = actualArea;
    }

    public Double getExpArea() {
        return expArea;
    }

    public void setExpArea(Double expArea) {
        this.expArea = expArea;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getVetting() {
        return vetting;
    }

    public void setVetting(String vetting) {
        this.vetting = vetting;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
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

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getFarmerVillageOrCity() {
        return farmerVillageOrCity;
    }

    public void setFarmerVillageOrCity(String farmerVillageOrCity) {
        this.farmerVillageOrCity = farmerVillageOrCity;
    }

    public String getFarmerAddL1() {
        return farmerAddL1;
    }

    public void setFarmerAddL1(String farmerAddL1) {
        this.farmerAddL1 = farmerAddL1;
    }

    public String getFarmerAddL2() {
        return farmerAddL2;
    }

    public void setFarmerAddL2(String farmerAddL2) {
        this.farmerAddL2 = farmerAddL2;
    }

    public String getFarmerDistrict() {
        return farmerDistrict;
    }

    public void setFarmerDistrict(String farmerDistrict) {
        this.farmerDistrict = farmerDistrict;
    }

    public String getFarmerMandalOrTehsil() {
        return farmerMandalOrTehsil;
    }

    public void setFarmerMandalOrTehsil(String farmerMandalOrTehsil) {
        this.farmerMandalOrTehsil = farmerMandalOrTehsil;
    }

    public String getFarmerState() {
        return farmerState;
    }

    public void setFarmerState(String farmerState) {
        this.farmerState = farmerState;
    }

    public String getSowingDate() {
        return sowingDate;
    }

    public void setSowingDate(String sowingDate) {
        this.sowingDate = sowingDate;
    }

    public boolean isSelectedd() {
        return isSelectedd;
    }

    public void setSelectedd(boolean selectedd) {
        isSelectedd = selectedd;
    }

    public String getSvId() {
        return svId;
    }

    public void setSvId(String svId) {
        this.svId = svId;
    }
}
