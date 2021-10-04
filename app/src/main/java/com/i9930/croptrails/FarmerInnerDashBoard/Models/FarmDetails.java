package com.i9930.croptrails.FarmerInnerDashBoard.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmDetails implements Parcelable {

    @SerializedName("farm_cm_id")
    @Expose
    private String farmCmId;
    @SerializedName("farm_master_num")
    @Expose
    private String farmMasterNum;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("cluster_id")
    @Expose
    private String clusterId;
    @SerializedName("crop_name")
    @Expose
    private String cropName;
    @SerializedName("season_num")
    @Expose
    private String seasonNum;
    @SerializedName("exp_area")
    @Expose
    private String expArea;
    @SerializedName("actual_area")
    @Expose
    private String actualArea;
    @SerializedName("standing_area")
    @Expose
    private String standingArea;
    @SerializedName("irrigation_source_id")
    @Expose
    private String irrigationSourceId;
    @SerializedName("previous_crop")
    @Expose
    private String previousCrop;
    @SerializedName("irrigation_type_id")
    @Expose
    private String irrigationTypeId;
    @SerializedName("soil_type_id")
    @Expose
    private String soilTypeId;
    @SerializedName("sowing_date")
    @Expose
    private String sowingDate;
    @SerializedName("exp_flowering_date")
    @Expose
    private String expFloweringDate;
    @SerializedName("actual_flowering_date")
    @Expose
    private String actualFloweringDate;
    @SerializedName("exp_harvest_date")
    @Expose
    private String expHarvestDate;
    @SerializedName("actual_harvest_date")
    @Expose
    private String actualHarvestDate;
    @SerializedName("area_harvested")
    @Expose
    private String areaHarvested;
    @SerializedName("seed_provided_on")
    @Expose
    private String seedProvidedOn;
    @SerializedName("seed_lot_no")
    @Expose
    private String seedLotNo;
    @SerializedName("qty_seed_provided")
    @Expose
    private String qtySeedProvided;
    @SerializedName("seed_unit_id")
    @Expose
    private String seedUnitId;
    @SerializedName("germination")
    @Expose
    private String germination;
    @SerializedName("population")
    @Expose
    private String population;
    @SerializedName("spacing_rtr")
    @Expose
    private String spacingRtr;
    @SerializedName("spacing_ptp")
    @Expose
    private String spacingPtp;
    @SerializedName("pld_acres")
    @Expose
    private String pldAcres;
    @SerializedName("pld_reason")
    @Expose
    private Object pldReason;
    @SerializedName("pld_receipt_no")
    @Expose
    private String pldReceiptNo;
    @SerializedName("received_qty")
    @Expose
    private String receivedQty;
    @SerializedName("outward_no")
    @Expose
    private String outwardNo;
    @SerializedName("load_no")
    @Expose
    private String loadNo;
    @SerializedName("grade")
    @Expose
    private String grade;
    @SerializedName("agreed_rate")
    @Expose
    private String agreedRate;
    @SerializedName("payment_mode")
    @Expose
    private Object paymentMode;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("dod")
    @Expose
    private Object dod;
    @SerializedName("deleted_by")
    @Expose
    private Object deletedBy;
    @SerializedName("farm_name")
    @Expose
    private String farmName;
    @SerializedName("village_or_city")
    @Expose
    private String villageOrCity;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("addL1")
    @Expose
    private String addL1;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("addL2")
    @Expose
    private String addL2;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("mandal_or_tehsil")
    @Expose
    private String mandalOrTehsil;

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getVillageOrCity() {
        return villageOrCity;
    }

    public void setVillageOrCity(String villageOrCity) {
        this.villageOrCity = villageOrCity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddL1() {
        return addL1;
    }

    public void setAddL1(String addL1) {
        this.addL1 = addL1;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddL2() {
        return addL2;
    }

    public void setAddL2(String addL2) {
        this.addL2 = addL2;
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

    public String getFarmCmId() {
        return farmCmId;
    }

    public void setFarmCmId(String farmCmId) {
        this.farmCmId = farmCmId;
    }

    public String getFarmMasterNum() {
        return farmMasterNum;
    }

    public void setFarmMasterNum(String farmMasterNum) {
        this.farmMasterNum = farmMasterNum;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getSeasonNum() {
        return seasonNum;
    }

    public void setSeasonNum(String seasonNum) {
        this.seasonNum = seasonNum;
    }

    public String getExpArea() {
        return expArea;
    }

    public void setExpArea(String expArea) {
        this.expArea = expArea;
    }

    public String getActualArea() {
        return actualArea;
    }

    public void setActualArea(String actualArea) {
        this.actualArea = actualArea;
    }

    public String getStandingArea() {
        return standingArea;
    }

    public void setStandingArea(String standingArea) {
        this.standingArea = standingArea;
    }

    public String getIrrigationSourceId() {
        return irrigationSourceId;
    }

    public void setIrrigationSourceId(String irrigationSourceId) {
        this.irrigationSourceId = irrigationSourceId;
    }

    public String getPreviousCrop() {
        return previousCrop;
    }

    public void setPreviousCrop(String previousCrop) {
        this.previousCrop = previousCrop;
    }

    public String getIrrigationTypeId() {
        return irrigationTypeId;
    }

    public void setIrrigationTypeId(String irrigationTypeId) {
        this.irrigationTypeId = irrigationTypeId;
    }

    public String getSoilTypeId() {
        return soilTypeId;
    }

    public void setSoilTypeId(String soilTypeId) {
        this.soilTypeId = soilTypeId;
    }

    public String getSowingDate() {
        return sowingDate;
    }

    public void setSowingDate(String sowingDate) {
        this.sowingDate = sowingDate;
    }

    public String getExpFloweringDate() {
        return expFloweringDate;
    }

    public void setExpFloweringDate(String expFloweringDate) {
        this.expFloweringDate = expFloweringDate;
    }

    public String getActualFloweringDate() {
        return actualFloweringDate;
    }

    public void setActualFloweringDate(String actualFloweringDate) {
        this.actualFloweringDate = actualFloweringDate;
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

    public String getAreaHarvested() {
        return areaHarvested;
    }

    public void setAreaHarvested(String areaHarvested) {
        this.areaHarvested = areaHarvested;
    }

    public String getSeedProvidedOn() {
        return seedProvidedOn;
    }

    public void setSeedProvidedOn(String seedProvidedOn) {
        this.seedProvidedOn = seedProvidedOn;
    }

    public String getSeedLotNo() {
        return seedLotNo;
    }

    public void setSeedLotNo(String seedLotNo) {
        this.seedLotNo = seedLotNo;
    }

    public String getQtySeedProvided() {
        return qtySeedProvided;
    }

    public void setQtySeedProvided(String qtySeedProvided) {
        this.qtySeedProvided = qtySeedProvided;
    }

    public String getSeedUnitId() {
        return seedUnitId;
    }

    public void setSeedUnitId(String seedUnitId) {
        this.seedUnitId = seedUnitId;
    }

    public String getGermination() {
        return germination;
    }

    public void setGermination(String germination) {
        this.germination = germination;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getSpacingRtr() {
        return spacingRtr;
    }

    public void setSpacingRtr(String spacingRtr) {
        this.spacingRtr = spacingRtr;
    }

    public String getSpacingPtp() {
        return spacingPtp;
    }

    public void setSpacingPtp(String spacingPtp) {
        this.spacingPtp = spacingPtp;
    }

    public String getPldAcres() {
        return pldAcres;
    }

    public void setPldAcres(String pldAcres) {
        this.pldAcres = pldAcres;
    }

    public Object getPldReason() {
        return pldReason;
    }

    public void setPldReason(Object pldReason) {
        this.pldReason = pldReason;
    }

    public String getPldReceiptNo() {
        return pldReceiptNo;
    }

    public void setPldReceiptNo(String pldReceiptNo) {
        this.pldReceiptNo = pldReceiptNo;
    }

    public String getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(String receivedQty) {
        this.receivedQty = receivedQty;
    }

    public String getOutwardNo() {
        return outwardNo;
    }

    public void setOutwardNo(String outwardNo) {
        this.outwardNo = outwardNo;
    }

    public String getLoadNo() {
        return loadNo;
    }

    public void setLoadNo(String loadNo) {
        this.loadNo = loadNo;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAgreedRate() {
        return agreedRate;
    }

    public void setAgreedRate(String agreedRate) {
        this.agreedRate = agreedRate;
    }

    public Object getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Object paymentMode) {
        this.paymentMode = paymentMode;
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

    public Object getDod() {
        return dod;
    }

    public void setDod(Object dod) {
        this.dod = dod;
    }

    public Object getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Object deletedBy) {
        this.deletedBy = deletedBy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public FarmDetails(Parcel in){
        this.farmCmId = in.readString();
        this.farmMasterNum = in.readString();
        this.compId =  in.readString();
        this.clusterId =  in.readString();
        this.cropName =  in.readString();
        this.seasonNum =  in.readString();
        this.expArea =  in.readString();
        this.actualArea =  in.readString();
        this.standingArea =  in.readString();
        this.irrigationSourceId =  in.readString();
        this.previousCrop =  in.readString();
        this.irrigationTypeId =  in.readString();
        this.soilTypeId =  in.readString();
        this.sowingDate =  in.readString();
        this.expFloweringDate =  in.readString();
        this.actualFloweringDate =  in.readString();
        this.expHarvestDate =  in.readString();
        this.actualHarvestDate =  in.readString();
        this.areaHarvested =  in.readString();
        this.seedProvidedOn =  in.readString();
        this.seedLotNo =  in.readString();
        this.qtySeedProvided =  in.readString();
        this.seedUnitId =  in.readString();
        this.germination =  in.readString();
        this.population =  in.readString();
        this.spacingPtp =  in.readString();
        this.spacingRtr =  in.readString();
        this.pldAcres =  in.readString();
        this.pldReason =  in.readString();
        this.pldReceiptNo =  in.readString();
        this.receivedQty =  in.readString();
        this.outwardNo =  in.readString();
        this.loadNo =  in.readString();
        this.grade =  in.readString();
        this.agreedRate =  in.readString();
        this.paymentMode =  in.readString();
        this.doa =  in.readString();
        this.isActive =  in.readString();
        this.dod =  in.readString();
        this.deletedBy =  in.readString();
        this.farmName=in.readString();
        this.villageOrCity=in.readString();
        this.state=in.readString();
        this.addL1=in.readString();
        this.addL2=in.readString();
        this.district=in.readString();
        this.mandalOrTehsil=in.readString();

    }
}