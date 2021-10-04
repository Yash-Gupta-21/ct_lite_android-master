package com.i9930.croptrails.AddFarm.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import com.i9930.croptrails.AddFarm.AnimalHusbandory;
import com.i9930.croptrails.CommonClasses.Address.CityDatum;
import com.i9930.croptrails.CommonClasses.Address.CountryDatum;
import com.i9930.croptrails.CommonClasses.Address.StateDatum;
import com.i9930.croptrails.CommonClasses.DDNew;
import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormDatum;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.Season;

public class FarmData {
    String doa;
    String dod;
    boolean isFullDetailClicked=false;
    @SerializedName("ownership_doc")
    String ownerShipDoc;
    @SerializedName("farm_photo")
    String farmImage;
    String ownerShipPath;
    String farmImagePath;
    int countryIndex=0;
    int stateIndex=0;
    int cityIndex=0;
    @SerializedName("motor_hp")
    String motorHp;

    @SerializedName("seed_lot_no")
    String seed_lot_no;
    @SerializedName("seed_unit_id")
    String seed_unit_id;
    @SerializedName("germination")
    String germination;
    @SerializedName("population")
    String population;
    @SerializedName("spacing_rtr")
    String spacing_rtr;
    @SerializedName("spacing_ptp")
    String spacing_ptp;
    @SerializedName("pld_acres")
    String pld_acres;
    @SerializedName("pld_reason")
    String pld_reason;
    @SerializedName("pld_receipt_no")
    String pld_receipt_no;
    @SerializedName("received_qty")
    String received_qty;
    @SerializedName("outward_no")
    String outward_no;
    @SerializedName("load_no")
    String load_no;
    @SerializedName("grade")
    String grade;
    @SerializedName("agreed_rate")
    String agreed_rate;
    @SerializedName("payment_mode")
    String payment_mode;

    @SerializedName("seed_provided_on")
    @Expose
    private String seedProvidedOn;

    ArrayList<StateDatum>stateDatumList=new ArrayList<>();
    StateDatum selectedState;
    Season selectedSeason;
    DDNew selectedCrop;
    DDNew selectedPrevCrop;
    DDNew selectedIrriSource;
    DDNew selectedIrriType;
    DDNew selectedSoilType;
    DDNew selectedCroppingPattern;
    DDNew selectedPlantingMethod;
    DDNew selectedLandCategory;
    CountryDatum selectedCountry;
    CityDatum selectedDistrict;
    DDNew selectedMotorHP;
    List<DDNew>motorHpList=new ArrayList<>();

    ArrayList<CityDatum>cityDatumList=new ArrayList<>();
    @SerializedName("crop")
    List<FPOCrop> fpoCropList=new ArrayList<>();

    @SerializedName("is_free")
    private String isFree;

    @SerializedName("sv_id")
    private String svId;

    @SerializedName("is_owned")
    private String isOwned="Y";
    @SerializedName("is_selected")
    private String isSelected;
    @SerializedName("delq_reason")
    private String delqReason;
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
    @SerializedName("actual_flowering_date")
    @Expose
    private String actualFloweringDate;
    @SerializedName("actual_harvest_date")
    @Expose
    private String actualHarvestDate;
    @SerializedName("area_harvested")
    @Expose
    private float areaHarvested;

    @SerializedName("standing_area")
    private String standingArea;
    @SerializedName("actual_area")
    private String actualArea;

    @SerializedName("owner_id")
    private String ownerId;

    @SerializedName("farm_id_offline")
    private String farmIdOffline;

    @SerializedName("farm_id")
    private String farmId;

    @SerializedName("lot_no")
    private String id;
    @SerializedName("farm_name")
    private String name = "";
    @SerializedName("exp_area")
    private String area ;
    @SerializedName("season_num")
    private String season = "";
    @SerializedName("cluster_id")
    private String cluster;
    @SerializedName("previous_crop")
    private String previouscrop = "";
    @SerializedName("irrigation_source_id")
    private String irrigationsource = "";

    @SerializedName("irri_source_name")
    private String irriSourceName;

    @SerializedName("irrigation_type_id")
    private String irrigationtype = "";

    @SerializedName("irri_type_name")
    private String irriTypename = "";

    @SerializedName("soil_type_id")
    private String soiltype = "";

    @SerializedName("soil_type_name")
    private String soiltypeName = "";

    @SerializedName("fAddL1")
    private String addL1 = "";
    @SerializedName("fAddL2")
    private String addL2 = "";
    @SerializedName("fvillage_or_city")
    private String villageOrCity = "";
    @SerializedName("fmandal_or_tehsil")
    private String mandalOrTehsil = "";
    @SerializedName("fdistrict")
    private String district = "";
    @SerializedName("fstate")
    private String state = "";
    @SerializedName("fcountry")
    private String country = "";

    @SerializedName("district_id")
    private String districtId = "";
    @SerializedName("state_id")
    private String stateId = "";
    @SerializedName("country_id")
    private String countryId = "";

    @SerializedName("comp_id")
    private String compId;
    @SerializedName("added_by")
    String addedBy;
    @SerializedName("lat_cord")
    String lat_cord;
    @SerializedName("long_cord")
    String long_cord;
    String zoom;
    @SerializedName("crop_name")
    String currentCropName = "0";
    @SerializedName("area_to_show")
    String areaS;

    @SerializedName("crop_id")
    @Expose
    String cropId;
    @SerializedName("isUploaded")
    @Expose
    String isUploaded;

    @SerializedName("assets")
    @Expose
    List<List<CropFormDatum>> assetsList;
    @SerializedName("qty_seed_provided")
    @Expose
    private String qtySeedProvided;
    String transplant_date;

    String is_irrigated;

    String cropping_pattern;

    String planting_method;


    int irriSourcePosition = 0;
    int seasonPosition = 0;
    int irriTypePosition = 0;
    int soilTypePosition = 0;
    int cropPosition = 0;
    int motorHpPosition = 0;
    int landCategoryPosition = 0;
    int plantingMethodPosition = 0;
    int croppingPatternPosition = 0;
    int cropPositionPrevious = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getPreviouscrop() {
        return previouscrop;
    }

    public void setPreviouscrop(String previouscrop) {
        this.previouscrop = previouscrop;
    }

    public String getIrrigationsource() {
        return irrigationsource;
    }

    public void setIrrigationsource(String irrigationsource) {
        this.irrigationsource = irrigationsource;
    }

    public String getIrrigationtype() {
        return irrigationtype;
    }

    public void setIrrigationtype(String irrigationtype) {
        this.irrigationtype = irrigationtype;
    }

    public String getSoiltype() {
        return soiltype;
    }

    public void setSoiltype(String soiltype) {
        this.soiltype = soiltype;
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

    public String getMandalOrTehsil() {
        return mandalOrTehsil;
    }

    public void setMandalOrTehsil(String mandalOrTehsil) {
        this.mandalOrTehsil = mandalOrTehsil;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getLat_cord() {
        return lat_cord;
    }

    public void setLat_cord(String lat_cord) {
        this.lat_cord = lat_cord;
    }

    public String getLong_cord() {
        return long_cord;
    }

    public void setLong_cord(String long_cord) {
        this.long_cord = long_cord;
    }

    public String getCurrentCropName() {
        return currentCropName;
    }

    public void setCurrentCropName(String currentCropName) {
        this.currentCropName = currentCropName;
    }

    public String getAreaS() {
        return areaS;
    }

    public void setAreaS(String areaS) {
        this.areaS = areaS;
    }

    public int getIrriSourcePosition() {
        return irriSourcePosition;
    }

    public void setIrriSourcePosition(int irriSourcePosition) {
        this.irriSourcePosition = irriSourcePosition;
    }

    public int getSeasonPosition() {
        return seasonPosition;
    }

    public void setSeasonPosition(int seasonPosition) {
        this.seasonPosition = seasonPosition;
    }

    public int getIrriTypePosition() {
        return irriTypePosition;
    }

    public void setIrriTypePosition(int irriTypePosition) {
        this.irriTypePosition = irriTypePosition;
    }

    public int getSoilTypePosition() {
        return soilTypePosition;
    }

    public void setSoilTypePosition(int soilTypePosition) {
        this.soilTypePosition = soilTypePosition;
    }

    public int getCropPosition() {
        return cropPosition;
    }

    public void setCropPosition(int cropPosition) {
        this.cropPosition = cropPosition;
    }

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public List<FPOCrop> getFpoCropList() {
        return fpoCropList;
    }

    public void setFpoCropList(List<FPOCrop> fpoCropList) {
        this.fpoCropList = fpoCropList;
    }

    public String getIsOwned() {
        return isOwned;
    }

    public void setIsOwned(String isOwned) {
        this.isOwned = isOwned;
    }


    public String getIrriSourceName() {
        return irriSourceName;
    }

    public void setIrriSourceName(String irriSourceName) {
        this.irriSourceName = irriSourceName;
    }

    public String getIrriTypename() {
        return irriTypename;
    }

    public void setIrriTypename(String irriTypename) {
        this.irriTypename = irriTypename;
    }

    public String getSoiltypeName() {
        return soiltypeName;
    }

    public void setSoiltypeName(String soiltypeName) {
        this.soiltypeName = soiltypeName;
    }


    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getFarmIdOffline() {
        return farmIdOffline;
    }


    public void setFarmIdOffline(String farmIdOffline) {
        this.farmIdOffline = farmIdOffline;
    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
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


    public List<List<CropFormDatum>> getAssetsList() {
        return assetsList;
    }

    public void setAssetsList(List<List<CropFormDatum>> assetsList) {
        this.assetsList = assetsList;
    }

    public ArrayList<StateDatum> getStateDatumList() {
        return stateDatumList;
    }

    public void setStateDatumList(ArrayList<StateDatum> stateDatumList) {
        this.stateDatumList = stateDatumList;
    }

    public ArrayList<CityDatum> getCityDatumList() {
        return cityDatumList;
    }

    public void setCityDatumList(ArrayList<CityDatum> cityDatumList) {
        this.cityDatumList = cityDatumList;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public int getCountryIndex() {
        return countryIndex;
    }

    public void setCountryIndex(int countryIndex) {
        this.countryIndex = countryIndex;
    }

    public int getStateIndex() {
        return stateIndex;
    }

    public void setStateIndex(int stateIndex) {
        this.stateIndex = stateIndex;
    }

    public int getCityIndex() {
        return cityIndex;
    }

    public void setCityIndex(int cityIndex) {
        this.cityIndex = cityIndex;
    }

    public List<DDNew> getMotorHpList() {
        return motorHpList;
    }

    public void setMotorHpList(List<DDNew> motorHpList) {
        this.motorHpList = motorHpList;
    }

    public String getMotorHp() {
        return motorHp;
    }

    public void setMotorHp(String motorHp) {
        this.motorHp = motorHp;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }

    public int getCropPositionPrevious() {
        return cropPositionPrevious;
    }

    public void setCropPositionPrevious(int cropPositionPrevious) {
        this.cropPositionPrevious = cropPositionPrevious;
    }

    public int getCroppingPatternPosition() {
        return croppingPatternPosition;
    }

    public void setCroppingPatternPosition(int croppingPatternPosition) {
        this.croppingPatternPosition = croppingPatternPosition;
    }

    public int getPlantingMethodPosition() {
        return plantingMethodPosition;
    }

    public void setPlantingMethodPosition(int plantingMethodPosition) {
        this.plantingMethodPosition = plantingMethodPosition;
    }

    public int getLandCategoryPosition() {
        return landCategoryPosition;
    }

    public void setLandCategoryPosition(int landCategoryPosition) {
        this.landCategoryPosition = landCategoryPosition;
    }

    public int getMotorHpPosition() {
        return motorHpPosition;
    }

    public void setMotorHpPosition(int motorHpPosition) {
        this.motorHpPosition = motorHpPosition;
    }

    public String getOwnerShipDoc() {
        return ownerShipDoc;
    }

    public void setOwnerShipDoc(String ownerShipDoc) {
        this.ownerShipDoc = ownerShipDoc;
    }

    public String getFarmImage() {
        return farmImage;
    }

    public void setFarmImage(String farmImage) {
        this.farmImage = farmImage;
    }

    public String getOwnerShipPath() {
        return ownerShipPath;
    }

    public void setOwnerShipPath(String ownerShipPath) {
        this.ownerShipPath = ownerShipPath;
    }

    public String getFarmImagePath() {
        return farmImagePath;
    }

    public void setFarmImagePath(String farmImagePath) {
        this.farmImagePath = farmImagePath;
    }

    public boolean isFullDetailClicked() {
        return isFullDetailClicked;
    }

    public void setFullDetailClicked(boolean fullDetailClicked) {
        isFullDetailClicked = fullDetailClicked;
    }

    public StateDatum getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(StateDatum selectedState) {
        this.selectedState = selectedState;
    }

    public Season getSelectedSeason() {
        return selectedSeason;
    }

    public void setSelectedSeason(Season selectedSeason) {
        this.selectedSeason = selectedSeason;
    }

    public DDNew getSelectedCrop() {
        return selectedCrop;
    }

    public void setSelectedCrop(DDNew selectedCrop) {
        this.selectedCrop = selectedCrop;
    }

    public DDNew getSelectedPrevCrop() {
        return selectedPrevCrop;
    }

    public void setSelectedPrevCrop(DDNew selectedPrevCrop) {
        this.selectedPrevCrop = selectedPrevCrop;
    }

    public DDNew getSelectedIrriSource() {
        return selectedIrriSource;
    }

    public void setSelectedIrriSource(DDNew selectedIrriSource) {
        this.selectedIrriSource = selectedIrriSource;
    }

    public DDNew getSelectedIrriType() {
        return selectedIrriType;
    }

    public void setSelectedIrriType(DDNew selectedIrriType) {
        this.selectedIrriType = selectedIrriType;
    }

    public DDNew getSelectedSoilType() {
        return selectedSoilType;
    }

    public void setSelectedSoilType(DDNew selectedSoilType) {
        this.selectedSoilType = selectedSoilType;
    }

    public DDNew getSelectedCroppingPattern() {
        return selectedCroppingPattern;
    }

    public void setSelectedCroppingPattern(DDNew selectedCroppingPattern) {
        this.selectedCroppingPattern = selectedCroppingPattern;
    }

    public DDNew getSelectedPlantingMethod() {
        return selectedPlantingMethod;
    }

    public void setSelectedPlantingMethod(DDNew selectedPlantingMethod) {
        this.selectedPlantingMethod = selectedPlantingMethod;
    }

    public DDNew getSelectedLandCategory() {
        return selectedLandCategory;
    }

    public void setSelectedLandCategory(DDNew selectedLandCategory) {
        this.selectedLandCategory = selectedLandCategory;
    }

    public CountryDatum getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(CountryDatum selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public DDNew getSelectedMotorHP() {
        return selectedMotorHP;
    }

    public void setSelectedMotorHP(DDNew selectedMotorHP) {
        this.selectedMotorHP = selectedMotorHP;
    }

    public CityDatum getSelectedDistrict() {
        return selectedDistrict;
    }

    public void setSelectedDistrict(CityDatum selectedDistrict) {
        this.selectedDistrict = selectedDistrict;
    }

    public String getStandingArea() {
        return standingArea;
    }

    public void setStandingArea(String standingArea) {
        this.standingArea = standingArea;
    }

    public String getActualArea() {
        return actualArea;
    }

    public void setActualArea(String actualArea) {
        this.actualArea = actualArea;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getDelqReason() {
        return delqReason;
    }

    public void setDelqReason(String delqReason) {
        this.delqReason = delqReason;
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

    public String getIsFree() {
        return isFree;
    }

    public void setIsFree(String isFree) {
        this.isFree = isFree;
    }

    public String getSvId() {
        return svId;
    }

    public void setSvId(String svId) {
        this.svId = svId;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public String getSeed_lot_no() {
        return seed_lot_no;
    }

    public void setSeed_lot_no(String seed_lot_no) {
        this.seed_lot_no = seed_lot_no;
    }

    public String getSeed_unit_id() {
        return seed_unit_id;
    }

    public void setSeed_unit_id(String seed_unit_id) {
        this.seed_unit_id = seed_unit_id;
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

    public String getSpacing_rtr() {
        return spacing_rtr;
    }

    public void setSpacing_rtr(String spacing_rtr) {
        this.spacing_rtr = spacing_rtr;
    }

    public String getSpacing_ptp() {
        return spacing_ptp;
    }

    public void setSpacing_ptp(String spacing_ptp) {
        this.spacing_ptp = spacing_ptp;
    }

    public String getPld_acres() {
        return pld_acres;
    }

    public void setPld_acres(String pld_acres) {
        this.pld_acres = pld_acres;
    }

    public String getPld_reason() {
        return pld_reason;
    }

    public void setPld_reason(String pld_reason) {
        this.pld_reason = pld_reason;
    }

    public String getPld_receipt_no() {
        return pld_receipt_no;
    }

    public void setPld_receipt_no(String pld_receipt_no) {
        this.pld_receipt_no = pld_receipt_no;
    }

    public String getReceived_qty() {
        return received_qty;
    }

    public void setReceived_qty(String received_qty) {
        this.received_qty = received_qty;
    }

    public String getOutward_no() {
        return outward_no;
    }

    public void setOutward_no(String outward_no) {
        this.outward_no = outward_no;
    }

    public String getLoad_no() {
        return load_no;
    }

    public void setLoad_no(String load_no) {
        this.load_no = load_no;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAgreed_rate() {
        return agreed_rate;
    }

    public void setAgreed_rate(String agreed_rate) {
        this.agreed_rate = agreed_rate;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
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
}
