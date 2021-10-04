package com.i9930.croptrails.RoomDatabase.FarmTable;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormDatum;
import com.i9930.croptrails.GerminationAndSpacing.ShowSampleGermination.Model.SampleGerminationDatum;
import com.i9930.croptrails.Test.PldModel.PldData;

public class SendOfflineFarm {

    @SerializedName("transplant_date")
    private String transplant_date;

    @SerializedName("farm_name")
    private String farm_name;

    @SerializedName("motor_hp")
    private String motor_hp;

    @SerializedName("ownership_doc")
    private String ownership_doc;

    @SerializedName("farm_photo")
    private String farm_photo;

    @SerializedName("farm_id")
    private String farmId;

    @SerializedName("lot_no")
    private String lotNo;

    @SerializedName("farmer_id")
    private String farmerId;


    @SerializedName("farmer_name")
    private String farmerName;

    @SerializedName("father_name")
    private String fatherName;

    @SerializedName("farmer_mob")
    private String farmerMob;


    @SerializedName("cluster_id")
    private String clusterId;

    @SerializedName("comp_id")
    private String compId;

    @SerializedName("addL1")
    private String addL1;

    @SerializedName("addL2")
    private String addL2;

    @SerializedName("village_or_city")
    private String villageOrCity;

    @SerializedName("district")
    private String district;

    @SerializedName("mandal_or_tehsil")
    private String mandalOrTehsil;

    @SerializedName("state")
    private String state;

    @SerializedName("country")
    private String country;

    @SerializedName("exp_area")
    private String expArea;

    @SerializedName("actual_area")
    private String actualArea;

    @SerializedName("irrigation_source")
    private String irrigationSource;

    @ColumnInfo(name = "previous_crop")
    @SerializedName("previous_crop")
    private String previousCrop;

    @SerializedName("irrigation_type")
    private String irrigationType;

    @SerializedName("soil_type")
    private String soilType;

    @SerializedName("sowing_date")
    private String sowingDate;

    @ColumnInfo(name = "exp_flowering_date")
    private String expFloweringDate;

    @SerializedName("actual_flowering_date")
    private String actualFloweringDate;

    @SerializedName("exp_harvest_date")
    private String expHarvestDate;

    @SerializedName("actual_harvest_date")
    private String actualHarvestDate;

    @SerializedName("area_harvested")
    private String areaHarvested;

    @SerializedName("standing_acres")
    private String standingAcres;


    /*@ColumnInfo(name = "seed_provided_on")
    private String seedProvidedOn;

    @ColumnInfo(name = "seed_lot_no")
    private String seedLotNo;

    @ColumnInfo(name = "qty_seed_provided")
    private String qtySeedProvided;

    @ColumnInfo(name = "seed_unit_id")
    private String seedUnitId;*/

    @SerializedName("germination")
    private String germination;

    @SerializedName("population")
    private String population;

    @SerializedName("spacing_ptp")
    private String spacingPtp;

    @SerializedName("pld_acres")
    private String pldAcres;

    @SerializedName("pld_reason")
    private String pldReason;

   /* @ColumnInfo(name = "pld_receipt_no")
    private String pldReceiptNo;

    @ColumnInfo(name = "received_qty")
    private String receivedQty;

    @ColumnInfo(name = "outward_no")
    private String outwardNo;

    @ColumnInfo(name = "load_no")
    private String loadLo;*/

    @SerializedName("lat_cord")
    private String latCord;

    @SerializedName("long_cord")
    private String longCord;

    @SerializedName("spacing_rtr")
    private String spacingRtr;

    @SerializedName("grade")
    private String grade;

    private String germinationData;

    @SerializedName("germi_data_offline")
    private String germinationDataOffline;

    @SerializedName("irri_source_id")
    private String irriSourceId;

    @SerializedName("irri_type_id")
    private String irriTypeId;

    @SerializedName("soil_type_id")
    private String soilTypeId;

    @SerializedName("season_id")
    private String seasonId;

    @SerializedName("sample_data")
    List<SampleGerminationDatum> germinationList;

    @SerializedName("pld")
    List<PldData> pldDataList;

    @SerializedName("user_id")
    String userId;

   /* @ColumnInfo(name = "doa")
    private String doa;

    @ColumnInfo(name = "is_active")
    private String isActive;
*/

    public SendOfflineFarm(String userId,String farmId, String lotNo, String farmerId, String farmerName,
                String farmerMob, String clusterId, String compId, String addL1,
                String addL2, String villageOrCity, String district, String mandalOrTehsil,
                String state, String country, String expArea, String actualArea,
                String irrigationSource, String previousCrop, String irrigationType,
                String soilType, String sowingDate, String expFloweringDate,
                String actualFloweringDate, String expHarvestDate, String actualHarvestDate,
                String areaHarvested, String standingAcres, String germination, String population,
                String spacingPtp, String pldAcres, String pldReason, String latCord, String longCord,
                String spacingRtr, String grade, String fatherName,String germinationData,String germinationDataOffline,
                           List<SampleGerminationDatum> germinationList,List<PldData> pldDataList) {
        this.farmId = farmId;
        this.lotNo = lotNo;
        this.farmerId = farmerId;
        this.farmerName = farmerName;
        this.farmerMob = farmerMob;
        this.userId=userId;
        this.clusterId = clusterId;
        this.compId = compId;
        this.addL1 = addL1;
        this.addL2 = addL2;
        this.villageOrCity = villageOrCity;
        this.district = district;
        this.mandalOrTehsil = mandalOrTehsil;
        this.state = state;
        this.country = country;
        this.expArea = expArea;
        this.actualArea = actualArea;
        this.irriSourceId = irrigationSource;
        this.previousCrop = previousCrop;
        this.irriTypeId= irrigationType;
        this.soilTypeId = soilType;
        this.sowingDate = sowingDate;
        this.expFloweringDate = expFloweringDate;
        this.actualFloweringDate = actualFloweringDate;
        this.expHarvestDate = expHarvestDate;
        this.actualHarvestDate = actualHarvestDate;
        this.areaHarvested = areaHarvested;
        this.standingAcres = standingAcres;
        this.germination = germination;
        this.population = population;
        this.spacingPtp = spacingPtp;
        this.pldAcres = pldAcres;
        this.pldReason = pldReason;
        this.latCord = latCord;
        this.longCord = longCord;
        this.spacingRtr = spacingRtr;
        this.grade = grade;
        this.fatherName=fatherName;
        this.germinationData=germinationData;
        this.germinationDataOffline=germinationDataOffline;
        this.germinationList=germinationList;
        this.pldDataList=pldDataList;
    }


    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getFarmerMob() {
        return farmerMob;
    }

    public void setFarmerMob(String farmerMob) {
        this.farmerMob = farmerMob;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
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

    public String getIrrigationSource() {
        return irrigationSource;
    }

    public void setIrrigationSource(String irrigationSource) {
        this.irrigationSource = irrigationSource;
    }

    public String getPreviousCrop() {
        return previousCrop;
    }

    public void setPreviousCrop(String previousCrop) {
        this.previousCrop = previousCrop;
    }

    public String getIrrigationType() {
        return irrigationType;
    }

    public void setIrrigationType(String irrigationType) {
        this.irrigationType = irrigationType;
    }

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
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

    public String getStandingAcres() {
        return standingAcres;
    }

    public void setStandingAcres(String standingAcres) {
        this.standingAcres = standingAcres;
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

    public String getLatCord() {
        return latCord;
    }

    public void setLatCord(String latCord) {
        this.latCord = latCord;
    }

    public String getLongCord() {
        return longCord;
    }

    public void setLongCord(String longCord) {
        this.longCord = longCord;
    }

    public String getSpacingRtr() {
        return spacingRtr;
    }

    public void setSpacingRtr(String spacingRtr) {
        this.spacingRtr = spacingRtr;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGerminationData() {
        return germinationData;
    }

    public void setGerminationData(String germinationData) {
        this.germinationData = germinationData;
    }


    public String getGerminationDataOffline() {
        return germinationDataOffline;
    }

    public void setGerminationDataOffline(String germinationDataOffline) {
        this.germinationDataOffline = germinationDataOffline;
    }


    public String getIrriSourceId() {
        return irriSourceId;
    }

    public void setIrriSourceId(String irriSourceId) {
        this.irriSourceId = irriSourceId;
    }

    public String getIrriTypeId() {
        return irriTypeId;
    }

    public void setIrriTypeId(String irriTypeId) {
        this.irriTypeId = irriTypeId;
    }

    public String getSoilTypeId() {
        return soilTypeId;
    }

    public void setSoilTypeId(String soilTypeId) {
        this.soilTypeId = soilTypeId;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }


    public List<SampleGerminationDatum> getGerminationList() {
        return germinationList;
    }

    public void setGerminationList(List<SampleGerminationDatum> germinationList) {
        this.germinationList = germinationList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<PldData> getPldDataList() {
        return pldDataList;
    }

    public void setPldDataList(List<PldData> pldDataList) {
        this.pldDataList = pldDataList;
    }

    public String getOwnership_doc() {
        return ownership_doc;
    }

    public void setOwnership_doc(String ownership_doc) {
        this.ownership_doc = ownership_doc;
    }

    public String getFarm_photo() {
        return farm_photo;
    }

    public void setFarm_photo(String farm_photo) {
        this.farm_photo = farm_photo;
    }

    public String getMotor_hp() {
        return motor_hp;
    }

    public void setMotor_hp(String motor_hp) {
        this.motor_hp = motor_hp;
    }

    public String getTransplant_date() {
        return transplant_date;
    }

    public void setTransplant_date(String transplant_date) {
        this.transplant_date = transplant_date;
    }

    @SerializedName("assets")
    @Expose
    List<List<CropFormDatum>> assetsList;
    String is_irrigated;

    String cropping_pattern;

    String planting_method;
}
