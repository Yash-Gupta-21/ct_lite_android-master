package com.i9930.croptrails.RoomDatabase.FarmTable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "farm")
public class Farm {
    @PrimaryKey(autoGenerate = true)
    private int offlineId;

    @ColumnInfo(name = "full_data")
    @SerializedName("full_data")
    private String farmFullData;

    @ColumnInfo(name = "motor_hp")
    @SerializedName("motor_hp")
    private String motorHp;

    @ColumnInfo(name = "land_category")
    @SerializedName("land_category")
    private String landCategoty;

    @ColumnInfo(name = "farm_image")
    @SerializedName("farm_image")
    private String farmImage;

    @ColumnInfo(name = "own_proof")
    @SerializedName("own_proof")
    private String ownerShipProof;

    @ColumnInfo(name = "farm_id")
    @SerializedName("farm_id")
    private String farmId;

    @ColumnInfo(name = "lot_no")
    @SerializedName("lot_no")
    private String lotNo;

    @ColumnInfo(name = "farmer_id")
    @SerializedName("farmer_id")
    private String farmerId;


    @ColumnInfo(name = "farmer_name")
    @SerializedName("farmer_name")
    private String farmerName;

    @ColumnInfo(name = "father_name")
    @SerializedName("father_name")
    private String fatherName;

    @ColumnInfo(name = "farmer_mob")
    @SerializedName("farmer_mob")
    private String farmerMob;


    @ColumnInfo(name = "cluster_id")
    @SerializedName("cluster_id")
    private String clusterId;

    @ColumnInfo(name = "comp_id")
    @SerializedName("comp_id")
    private String compId;

    @ColumnInfo(name = "addL1")
    @SerializedName("addL1")
    private String addL1;

    @ColumnInfo(name = "addL2")
    @SerializedName("addL2")
    private String addL2;

    @ColumnInfo(name = "village_or_city")
    @SerializedName("village_or_city")
    private String villageOrCity;

    @ColumnInfo(name = "district")
    @SerializedName("district")
    private String district;

    @ColumnInfo(name = "mandal_or_tehsil")
    @SerializedName("mandal_or_tehsil")
    private String mandalOrTehsil;

    @ColumnInfo(name = "state")
    @SerializedName("state")
    private String state;

    @ColumnInfo(name = "country")
    @SerializedName("country")
    private String country;

    @ColumnInfo(name = "exp_area")
    @SerializedName("exp_area")
    private String expArea;

    @ColumnInfo(name = "actual_area")
    @SerializedName("actual_area")
    private String actualArea;

    @ColumnInfo(name = "irrigation_source")
    @SerializedName("irrigation_source")
    private String irrigationSource;

    @SerializedName("previous_crop")
    @ColumnInfo(name = "previous_crop")
    private String previousCrop;

    @ColumnInfo(name = "irrigation_type")
    @SerializedName("irrigation_type")
    private String irrigationType;

    @ColumnInfo(name = "soil_type")
    @SerializedName("soil_type")
    private String soilType;

    @ColumnInfo(name = "sowing_date")
    @SerializedName("sowing_date")
    private String sowingDate;

    @SerializedName("exp_flowering_date")
    @ColumnInfo(name = "exp_flowering_date")
    private String expFloweringDate;

    @ColumnInfo(name = "actual_flowering_date")
    @SerializedName("actual_flowering_date")
    private String actualFloweringDate;

    @ColumnInfo(name = "exp_harvest_date")
    @SerializedName("exp_harvest_date")
    private String expHarvestDate;

    @ColumnInfo(name = "actual_harvest_date")
    @SerializedName("actual_harvest_date")
    private String actualHarvestDate;

    @ColumnInfo(name = "area_harvested")
    @SerializedName("area_harvested")
    private String areaHarvested;

    @ColumnInfo(name = "standing_acres")
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

    @ColumnInfo(name = "germination")
    @SerializedName("germination")
    private String germination;

    @ColumnInfo(name = "population")
    @SerializedName("population")
    private String population;

    @ColumnInfo(name = "spacing_ptp")
    @SerializedName("spacing_ptp")
    private String spacingPtp;

    @ColumnInfo(name = "pld_acres")
    @SerializedName("pld_acres")
    private String pldAcres;

    @ColumnInfo(name = "pld_reason")
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

    @ColumnInfo(name = "lat_cord")
    @SerializedName("lat_cord")
    private String latCord;

    @ColumnInfo(name = "long_cord")
    @SerializedName("long_cord")
    private String longCord;

    @ColumnInfo(name = "spacing_rtr")
    @SerializedName("spacing_rtr")
    private String spacingRtr;

    @ColumnInfo(name = "grade")
    @SerializedName("grade")
    private String grade;

    @ColumnInfo(name = "germi_data")
    @SerializedName("germi_data")
    private String germinationData;

    @ColumnInfo(name = "germi_data_offline")
    @SerializedName("germi_data_offline")
    private String germinationDataOffline;

    @ColumnInfo(name = "irri_source_id")
    @SerializedName("irri_source_id")
    private String irriSourceId;

    @ColumnInfo(name = "irri_type_id")
    @SerializedName("irri_type_id")
    private String irriTypeId;

    @ColumnInfo(name = "soil_type_id")
    @SerializedName("soil_type_id")
    private String soilTypeId;

    @ColumnInfo(name = "season_id")
    @SerializedName("season_id")
    private String seasonId;

    @ColumnInfo(name = "crop_id")
    @SerializedName("crop_id")
    private String cropId;


    @ColumnInfo(name = "isUpdated")
    private String isUpdated;

    @ColumnInfo(name = "updated_farm_data")
    private String updatedFarmData;

    @ColumnInfo(name = "is_uploaded")
    private String isUploaded;

    @ColumnInfo(name = "online_pld_json")
    private String onlinePldJson;

    @ColumnInfo(name = "offline_pld_json")
    private String offlinePldJson;

    @ColumnInfo(name = "crop_name")
    private String cropName;


    @ColumnInfo(name = "farm_name")
    private String farmName;

    @ColumnInfo(name = "is_owned")
    private String isOwned;

    @ColumnInfo(name = "isExistingFarm")
    private String isExistingFarm;

   /* @ColumnInfo(name = "doa")
    private String doa;

    @ColumnInfo(name = "is_active")
    private String isActive;
*/

    @ColumnInfo(name = "is_offline_added")
    private String isOfflineAdded;//Y  or N

    @ColumnInfo(name = "fpo_data")
    private String fpoData;//Y  or N

    @ColumnInfo(name = "coordinates")
    private String coords;

    @ColumnInfo(name = "transplant_date")
    String transplant_date;

    @ColumnInfo(name = "is_irrigated")
    String is_irrigated;

    @ColumnInfo(name = "cropping_pattern")
    String cropping_pattern;

    @ColumnInfo(name = "planting_method")
    String planting_method;

    @ColumnInfo(name = "assets")
    String assets;

    @ColumnInfo(name = "sv_id")
    String sv_id;
    @ColumnInfo(name = "sv_name")
    String sv_name;

    public Farm() {

    }

    public Farm(String farmId, String lotNo, String farmerId, String farmerName,
                String farmerMob, String clusterId, String compId, String addL1,
                String addL2, String villageOrCity, String district, String mandalOrTehsil,
                String state, String country, String expArea, String actualArea,
                String irrigationSource, String previousCrop, String irrigationType,
                String soilType, String sowingDate, String expFloweringDate,
                String actualFloweringDate, String expHarvestDate, String actualHarvestDate,
                String areaHarvested, String standingAcres, String germination, String population,
                String spacingPtp, String pldAcres, String pldReason, String latCord, String longCord,
                String spacingRtr, String grade, String fatherName, String germinationData, String germinationDataOffline,
                String updatedFarmData, String isUpdated, String isUploaded, String onlinePldJson, String offlinePldJson,
                String cropName, String isOfflineAdded, String fpoData, String cropId, String farmName, String isOwned,
                String seasonId, String isExistingFarm, String coordsList,

                String transplant_date, String is_irrigated, String cropping_pattern, String planting_method,String sv_id,String sv_name) {

        this.transplant_date = transplant_date;
        this.is_irrigated = is_irrigated;
        this.cropping_pattern=cropping_pattern;
        this.planting_method=planting_method;

        this.farmId = farmId;
        this.lotNo = lotNo;
        this.isExistingFarm=isExistingFarm;
        this.coords=coordsList;
        this.seasonId=seasonId;
        this.farmName=farmName;
        this.fpoData=fpoData;
        this.cropId=cropId;
        this.isOfflineAdded=isOfflineAdded;
        this.farmerId = farmerId;
        this.isUploaded=isUploaded;
        this.farmerName = farmerName;
        this.farmerMob = farmerMob;
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
        this.irrigationSource = irrigationSource;
        this.previousCrop = previousCrop;
        this.irrigationType = irrigationType;
        this.soilType = soilType;
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
        this.isUpdated=isUpdated;
        this.germinationDataOffline=germinationDataOffline;
        this.updatedFarmData=updatedFarmData;
        this.onlinePldJson=onlinePldJson;
        this.offlinePldJson=offlinePldJson;
        this.cropName=cropName;
        this.isOwned=isOwned;
        this.sv_id=sv_id;
        this.sv_name=sv_name;
    }

    public String getIsExistingFarm() {
        return isExistingFarm;
    }

    public void setIsExistingFarm(String isExistingFarm) {
        this.isExistingFarm = isExistingFarm;
    }

    public int getOfflineId() {
        return offlineId;
    }

    public void setOfflineId(int offlineId) {
        this.offlineId = offlineId;
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

    public void     setPldAcres(String pldAcres) {
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

    public String getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(String isUpdated) {
        this.isUpdated = isUpdated;
    }

    public String getGerminationDataOffline() {
        return germinationDataOffline;
    }

    public void setGerminationDataOffline(String germinationDataOffline) {
        this.germinationDataOffline = germinationDataOffline;
    }

    public String getUpdatedFarmData() {
        return updatedFarmData;
    }

    public void setUpdatedFarmData(String updatedFarmData) {
        this.updatedFarmData = updatedFarmData;
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

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }

    public String getOnlinePldJson() {
        return onlinePldJson;
    }

    public void setOnlinePldJson(String onlinePldJson) {
        this.onlinePldJson = onlinePldJson;
    }

    public String getOfflinePldJson() {
        return offlinePldJson;
    }

    public void setOfflinePldJson(String offlinePldJson) {
        this.offlinePldJson = offlinePldJson;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public String getIsOfflineAdded() {
        return isOfflineAdded;
    }

    public void setIsOfflineAdded(String isOfflineAdded) {
        this.isOfflineAdded = isOfflineAdded;
    }

    public String getFpoData() {
        return fpoData;
    }

    public void setFpoData(String fpoData) {
        this.fpoData = fpoData;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getIsOwned() {
        return isOwned;
    }

    public void setIsOwned(String isOwned) {
        this.isOwned = isOwned;
    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public String getTransplant_date() {
        return transplant_date;
    }

    public void setTransplant_date(String transplant_date) {
        this.transplant_date = transplant_date;
    }

    public String getIs_irrigated() {
        return is_irrigated;
    }

    public void setIs_irrigated(String is_irrigated) {
        this.is_irrigated = is_irrigated;
    }

    public String getCropping_pattern() {
        return cropping_pattern;
    }

    public void setCropping_pattern(String cropping_pattern) {
        this.cropping_pattern = cropping_pattern;
    }

    public String getPlanting_method() {
        return planting_method;
    }

    public void setPlanting_method(String planting_method) {
        this.planting_method = planting_method;
    }

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public String getSv_id() {
        return sv_id;
    }

    public void setSv_id(String sv_id) {
        this.sv_id = sv_id;
    }

    public String getSv_name() {
        return sv_name;
    }

    public void setSv_name(String sv_name) {
        this.sv_name = sv_name;
    }

    public String getFarmImage() {
        return farmImage;
    }

    public void setFarmImage(String farmImage) {
        this.farmImage = farmImage;
    }

    public String getOwnerShipProof() {
        return ownerShipProof;
    }

    public void setOwnerShipProof(String ownerShipProof) {
        this.ownerShipProof = ownerShipProof;
    }

    public String getMotorHp() {
        return motorHp;
    }

    public void setMotorHp(String motorHp) {
        this.motorHp = motorHp;
    }

    public String getFarmFullData() {
        return farmFullData;
    }

    public void setFarmFullData(String farmFullData) {
        this.farmFullData = farmFullData;
    }

    public String getLandCategoty() {
        return landCategoty;
    }

    public void setLandCategoty(String landCategoty) {
        this.landCategoty = landCategoty;
    }
}
