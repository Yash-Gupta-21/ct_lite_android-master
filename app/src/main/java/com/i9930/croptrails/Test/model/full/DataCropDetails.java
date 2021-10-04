package com.i9930.croptrails.Test.model.full;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataCropDetails {

    @SerializedName("farm_add_info_id")
    @Expose
    private int farmAddInfoId;
    @SerializedName("farm_id")
    @Expose
    private int farmId;
    @SerializedName("season_id")
    @Expose
    private int seasonId;
    @SerializedName("crop_id")
    @Expose
    private int cropId;
    @SerializedName("farm_area")
    @Expose
    private String farmArea;
    @SerializedName("datatype")
    @Expose
    private String datatype;
    @SerializedName("farm_add_info_json")
    @Expose
    private String farmAddInfoJson;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("farm_add_info_type_id")
    @Expose
    private int farmAddInfoTypeId;
    @SerializedName("farm_cmp_id")
    @Expose
    private Integer farmCmpId;


    public int getFarmAddInfoId() {
        return farmAddInfoId;
    }

    public void setFarmAddInfoId(int farmAddInfoId) {
        this.farmAddInfoId = farmAddInfoId;
    }

    public int getFarmId() {
        return farmId;
    }

    public void setFarmId(int farmId) {
        this.farmId = farmId;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public int getCropId() {
        return cropId;
    }

    public void setCropId(int cropId) {
        this.cropId = cropId;
    }

    public String getFarmArea() {
        return farmArea;
    }

    public void setFarmArea(String farmArea) {
        this.farmArea = farmArea;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getFarmAddInfoJson() {
        return farmAddInfoJson;
    }

    public void setFarmAddInfoJson(String farmAddInfoJson) {
        this.farmAddInfoJson = farmAddInfoJson;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getFarmAddInfoTypeId() {
        return farmAddInfoTypeId;
    }

    public void setFarmAddInfoTypeId(int farmAddInfoTypeId) {
        this.farmAddInfoTypeId = farmAddInfoTypeId;
    }

    public Integer getFarmCmpId() {
        return farmCmpId;
    }

    public void setFarmCmpId(Integer farmCmpId) {
        this.farmCmpId = farmCmpId;
    }
}