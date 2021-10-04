package com.i9930.croptrails.Landing.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineInnerData;
import com.i9930.croptrails.GerminationAndSpacing.ShowSampleGermination.Model.SampleGerminationDatum;
import com.i9930.croptrails.Test.PldModel.PldData;
import com.i9930.croptrails.Utility.AppConstants;


public class FetchFarmResult {
    @SerializedName("img_link")
    @Expose
    private String imgLink;
    @SerializedName("compare_type")
    String type= AppConstants.TIMELINE.FARM;
    @SerializedName("farm_name")
    @Expose
    private String farmName;

    @SerializedName("crop_name")
    @Expose
    private String cropName;

    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("lot_no")
    @Expose
    private String lotNo;
    @SerializedName("farmer_id")
    @Expose
    private String farmerId;
    @SerializedName("cluster_id")
    @Expose
    private String clusterId;
    @SerializedName("comp_id")
    @Expose
    private String compId;
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
    @SerializedName("exp_area")
    @Expose
    private String expArea;
    @SerializedName("actual_area")
    @Expose
    private String actualArea;
    @SerializedName("irrigation_source")
    @Expose
    private String irrigationSource;
    @SerializedName("previous_crop")
    @Expose
    private String previousCrop;
    @SerializedName("irrigation_type")
    @Expose
    private String irrigationType;
    @SerializedName("soil_type")
    @Expose
    private String soilType;
    @SerializedName("sowing_date")
    @Expose
    private String sowingDate;
    @SerializedName("spouse_name")
    @Expose
    private String spouseName;
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
    @SerializedName("seed_provided_on")
    @Expose
    private String seedProvidedOn;
    @SerializedName("qty_seed_provided")
    @Expose
    private String qtySeedProvided;
    @SerializedName("seed_unit_id")
    @Expose
    private String seedUnitId;

    @SerializedName("seed_lot_no")
    @Expose
    private String seedLotNo;

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
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("father_name")
    @Expose
    private String fatherName;
    @SerializedName("pld_acres")
    @Expose
    private String pldAcres;
    @SerializedName("pld_reason")
    @Expose
    private String pldReason;

    @SerializedName("standing_acres")
    @Expose
    private String standingAcres;

    private boolean isSelected;
    @SerializedName("added_by")
    @Expose
    private String added_by;
    @SerializedName("mob")
    @Expose
    private String mob;
    @SerializedName("faddL1")
    @Expose
    private String faddL1;
    @SerializedName("faddL2")
    @Expose
    private String faddL2;
    @SerializedName("fvillage_or_city")
    @Expose
    private String fvillage_or_city;
    @SerializedName("fdistrict")
    @Expose
    private String fdistrict;
    @SerializedName("fmandal_or_tehsil")
    @Expose
    private String fmandal_or_tehsil;
    @SerializedName("fstate")
    @Expose
    private String fstate;
    @SerializedName("fcountry")
    @Expose
    private String fcountry;

    @SerializedName("lat_cord")
    @Expose
    String latCord;
    @SerializedName("long_cord")
    @Expose
    String LongCord;

    @SerializedName("season_name")
    @Expose
    String seasonName;

    @SerializedName("season_id")
    @Expose
    String seasonId;

    @SerializedName("calendar")
    @Expose
    private List<TimelineInnerData> calendar = null;

    @SerializedName("crop_density_samples")
    @Expose
    private List<SampleGerminationDatum> germinationList = null;

    @SerializedName("pld")
    @Expose
    private List<PldData> plDataList = null;


    public String getFaddL1() {
        return faddL1;
    }

    public void setFaddL1(String faddL1) {
        this.faddL1 = faddL1;
    }

    public String getFaddL2() {
        return faddL2;
    }

    public void setFaddL2(String faddL2) {
        this.faddL2 = faddL2;
    }

    public String getFvillage_or_city() {
        return fvillage_or_city;
    }

    public void setFvillage_or_city(String fvillage_or_city) {
        this.fvillage_or_city = fvillage_or_city;
    }

    public String getFdistrict() {
        return fdistrict;
    }

    public void setFdistrict(String fdistrict) {
        this.fdistrict = fdistrict;
    }

    public String getFmandal_or_tehsil() {
        return fmandal_or_tehsil;
    }

    public void setFmandal_or_tehsil(String fmandal_or_tehsil) {
        this.fmandal_or_tehsil = fmandal_or_tehsil;
    }

    public String getFstate() {
        return fstate;
    }

    public void setFstate(String fstate) {
        this.fstate = fstate;
    }

    public String getFcountry() {
        return fcountry;
    }

    public void setFcountry(String fcountry) {
        this.fcountry = fcountry;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    String is_from_server;
    String enterDate,time,isFromServer="0";

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIsFromServer() {
        return isFromServer;
    }

    public void setIsFromServer(String isFromServer) {
        this.isFromServer = isFromServer;
    }

    public String getIs_from_server() {
        return is_from_server;
    }

    public void setIs_from_server(String is_from_server) {
        this.is_from_server = is_from_server;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPldReason() {
        return pldReason;
    }

    public void setPldReason(String pldReason) {
        this.pldReason = pldReason;
    }

    public String getPldAcres() {
        return pldAcres;
    }

    public void setPldAcres(String pldAcres) {
        this.pldAcres = pldAcres;
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

    public Date getHarvestCompare()
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date harvest_compare = null;
        try {
            harvest_compare = format.parse(expHarvestDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return harvest_compare;
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

    public String getSeedProvidedOn() {
        return seedProvidedOn;
    }

    public void setSeedProvidedOn(String seedProvidedOn) {
        this.seedProvidedOn = seedProvidedOn;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getStandingAcres() {
        return standingAcres;
    }

    public void setStandingAcres(String standingAcres) {
        this.standingAcres = standingAcres;
    }


    public String getLatCord() {
        return latCord;
    }

    public void setLatCord(String latCord) {
        this.latCord = latCord;
    }

    public String getLongCord() {
        return LongCord;
    }

    public void setLongCord(String longCord) {
        LongCord = longCord;
    }

    public List<TimelineInnerData> getCalendar() {
        return calendar;
    }

    public void setCalendar(List<TimelineInnerData> calendar) {
        this.calendar = calendar;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public List<SampleGerminationDatum> getGerminationList() {
        return germinationList;
    }

    public void setGerminationList(List<SampleGerminationDatum> germinationList) {
        this.germinationList = germinationList;
    }

    public String getSeedLotNo() {
        return seedLotNo;
    }

    public void setSeedLotNo(String seedLotNo) {
        this.seedLotNo = seedLotNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<PldData> getPlDataList() {
        return plDataList;
    }

    public void setPlDataList(List<PldData> plDataList) {
        this.plDataList = plDataList;
    }

    public FetchFarmResult() {
    }

    public FetchFarmResult( String farmName, String farmId, String lotNo, String farmerId, String clusterId,
                            String compId, String addL1, String addL2, String villageOrCity, String district,
                            String mandalOrTehsil, String state, String country, String expArea, String actualArea,
                            String irrigationSource, String previousCrop, String irrigationType, String soilType,
                            String sowingDate, String expFloweringDate, String actualFloweringDate,
                            String expHarvestDate, String actualHarvestDate, String seedProvidedOn, String qtySeedProvided,
                            String seedUnitId, String seedLotNo, String germination, String population, String spacingRtr,
                            String spacingPtp, String doa, String isActive, String name, String fatherName,
                            String pldAcres, String pldReason, String standingAcres, boolean isSelected, String added_by,
                            String mob, String faddL1, String faddL2, String fvillage_or_city, String fdistrict,
                            String fmandal_or_tehsil, String fstate, String fcountry, String latCord, String longCord,
                            String seasonName) {
        this.farmName = farmName;
        this.farmId = farmId;
        this.lotNo = lotNo;
        this.farmerId = farmerId;
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
        this.seedProvidedOn = seedProvidedOn;
        this.qtySeedProvided = qtySeedProvided;
        this.seedUnitId = seedUnitId;
        this.seedLotNo = seedLotNo;
        this.germination = germination;
        this.population = population;
        this.spacingRtr = spacingRtr;
        this.spacingPtp = spacingPtp;
        this.doa = doa;
        this.isActive = isActive;
        this.name = name;
        this.fatherName = fatherName;
        this.pldAcres = pldAcres;
        this.pldReason = pldReason;
        this.standingAcres = standingAcres;
        this.isSelected = isSelected;
        this.added_by = added_by;
        this.mob = mob;
        this.faddL1 = faddL1;
        this.faddL2 = faddL2;
        this.fvillage_or_city = fvillage_or_city;
        this.fdistrict = fdistrict;
        this.fmandal_or_tehsil = fmandal_or_tehsil;
        this.fstate = fstate;
        this.fcountry = fcountry;
        this.latCord = latCord;
        LongCord = longCord;
        this.seasonName = seasonName;
        this.is_from_server = is_from_server;
        this.enterDate = enterDate;
        this.time = time;
        this.isFromServer = isFromServer;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }
}
