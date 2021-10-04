package com.i9930.croptrails.Test.model.full;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmCropDetails {

    @SerializedName("farm_cm_id")
    @Expose
    private int farmCmId;
    @SerializedName("farm_master_num")
    @Expose
    private int farmMasterNum;
    @SerializedName("comp_id")
    @Expose
    private int compId;
    @SerializedName("cluster_id")
    @Expose
    private int clusterId;
    @SerializedName("crop_name")
    @Expose
    private String cropName;
    @SerializedName("season_num")
    @Expose
    private int seasonNum;
    @SerializedName("crop_id")
    @Expose
    private int cropId;
    @SerializedName("exp_area")
    @Expose
    private float expArea;
    @SerializedName("actual_area")
    @Expose
    private float actualArea;
    @SerializedName("cropping_pattern")
    @Expose
    private String croppingPattern;
    @SerializedName("standing_area")
    @Expose
    private float standingArea;
    @SerializedName("is_irrigated")
    @Expose
    private String isIrrigated;
    @SerializedName("irrigation_source_id")
    @Expose
    private int irrigationSourceId;
    @SerializedName("irrigation_type_id")
    @Expose
    private int irrigationTypeId;
    @SerializedName("previous_crop")
    @Expose
    private String previousCrop;
    @SerializedName("soil_type_id")
    @Expose
    private int soilTypeId;
    @SerializedName("planting_method")
    @Expose
    private String plantingMethod;
    @SerializedName("exp_sowing_date")
    @Expose
    private String expSowingDate;
    @SerializedName("exp_transplant_date")
    @Expose
    private String expTransplantDate;
    @SerializedName("exp_flowering_date")
    @Expose
    private String expFloweringDate;
    @SerializedName("exp_harvest_date")
    @Expose
    private String expHarvestDate;
    @SerializedName("sowing_date")
    @Expose
    private String sowingDate;
    @SerializedName("transplant_date")
    @Expose
    private String transplantDate;
    @SerializedName("actual_flowering_date")
    @Expose
    private String actualFloweringDate;
    @SerializedName("actual_harvest_date")
    @Expose
    private String actualHarvestDate;
    @SerializedName("area_harvested")
    @Expose
    private float areaHarvested;
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
    private int seedUnitId;
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
    private String pldReason;
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
    private String paymentMode;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("is_active")
    @Expose
    private int isActive;
    @SerializedName("dod")
    @Expose
    private String dod;
    @SerializedName("deleted_by")
    @Expose
    private String deletedBy;

    public int getFarmCmId() {
        return farmCmId;
    }

    public void setFarmCmId(int farmCmId) {
        this.farmCmId = farmCmId;
    }

    public int getFarmMasterNum() {
        return farmMasterNum;
    }

    public void setFarmMasterNum(int farmMasterNum) {
        this.farmMasterNum = farmMasterNum;
    }

    public int getCompId() {
        return compId;
    }

    public void setCompId(int compId) {
        this.compId = compId;
    }

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public int getSeasonNum() {
        return seasonNum;
    }

    public void setSeasonNum(int seasonNum) {
        this.seasonNum = seasonNum;
    }

    public int getCropId() {
        return cropId;
    }

    public void setCropId(int cropId) {
        this.cropId = cropId;
    }

    public float getExpArea() {
        return expArea;
    }

    public void setExpArea(float expArea) {
        this.expArea = expArea;
    }

    public float getActualArea() {
        return actualArea;
    }

    public void setActualArea(float actualArea) {
        this.actualArea = actualArea;
    }

    public String getCroppingPattern() {
        return croppingPattern;
    }

    public void setCroppingPattern(String croppingPattern) {
        this.croppingPattern = croppingPattern;
    }

    public float getStandingArea() {
        return standingArea;
    }

    public void setStandingArea(float standingArea) {
        this.standingArea = standingArea;
    }

    public String getIsIrrigated() {
        return isIrrigated;
    }

    public void setIsIrrigated(String isIrrigated) {
        this.isIrrigated = isIrrigated;
    }

    public int getIrrigationSourceId() {
        return irrigationSourceId;
    }

    public void setIrrigationSourceId(int irrigationSourceId) {
        this.irrigationSourceId = irrigationSourceId;
    }

    public int getIrrigationTypeId() {
        return irrigationTypeId;
    }

    public void setIrrigationTypeId(int irrigationTypeId) {
        this.irrigationTypeId = irrigationTypeId;
    }

    public String getPreviousCrop() {
        return previousCrop;
    }

    public void setPreviousCrop(String previousCrop) {
        this.previousCrop = previousCrop;
    }

    public int getSoilTypeId() {
        return soilTypeId;
    }

    public void setSoilTypeId(int soilTypeId) {
        this.soilTypeId = soilTypeId;
    }

    public String getPlantingMethod() {
        return plantingMethod;
    }

    public void setPlantingMethod(String plantingMethod) {
        this.plantingMethod = plantingMethod;
    }

    public String getExpSowingDate() {
        return expSowingDate;
    }

    public void setExpSowingDate(String expSowingDate) {
        this.expSowingDate = expSowingDate;
    }

    public String getExpTransplantDate() {
        return expTransplantDate;
    }

    public void setExpTransplantDate(String expTransplantDate) {
        this.expTransplantDate = expTransplantDate;
    }

    public String getExpFloweringDate() {
        return expFloweringDate;
    }

    public void setExpFloweringDate(String expFloweringDate) {
        this.expFloweringDate = expFloweringDate;
    }

    public String getExpHarvestDate() {
        return expHarvestDate;
    }

    public void setExpHarvestDate(String expHarvestDate) {
        this.expHarvestDate = expHarvestDate;
    }

    public String getSowingDate() {
        return sowingDate;
    }

    public void setSowingDate(String sowingDate) {
        this.sowingDate = sowingDate;
    }

    public String getTransplantDate() {
        return transplantDate;
    }

    public void setTransplantDate(String transplantDate) {
        this.transplantDate = transplantDate;
    }

    public String getActualFloweringDate() {
        return actualFloweringDate;
    }

    public void setActualFloweringDate(String actualFloweringDate) {
        this.actualFloweringDate = actualFloweringDate;
    }

    public String getActualHarvestDate() {
        return actualHarvestDate;
    }

    public void setActualHarvestDate(String actualHarvestDate) {
        this.actualHarvestDate = actualHarvestDate;
    }

    public float getAreaHarvested() {
        return areaHarvested;
    }

    public void setAreaHarvested(float areaHarvested) {
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

    public int getSeedUnitId() {
        return seedUnitId;
    }

    public void setSeedUnitId(int seedUnitId) {
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

    public String getPldReason() {
        return pldReason;
    }

    public void setPldReason(String pldReason) {
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

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

}