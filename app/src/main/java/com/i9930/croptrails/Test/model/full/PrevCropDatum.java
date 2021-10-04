package com.i9930.croptrails.Test.model.full;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PrevCropDatum {

    @SerializedName("farm_cmp_id")
    @Expose
    private Integer farmCmpId;
    @SerializedName("comp_id")
    @Expose
    private Integer compId;
    @SerializedName("cluster_id")
    @Expose
    private Integer clusterId;
    @SerializedName("farm_master_num")
    @Expose
    private Integer farmMasterNum;
    @SerializedName("season_num")
    @Expose
    private Integer seasonNum;
    @SerializedName("crop_id")
    @Expose
    private Integer cropId;
    @SerializedName("farm_area")
    @Expose
    private Float farmArea;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("dod")
    @Expose
    private String dod;
    @SerializedName("deleted_by")
    @Expose
    private String deletedBy;
    @SerializedName("data")
    @Expose
    private List<DataCropDetails> data = null;

    public Integer getFarmCmpId() {
        return farmCmpId;
    }

    public void setFarmCmpId(Integer farmCmpId) {
        this.farmCmpId = farmCmpId;
    }

    public Integer getCompId() {
        return compId;
    }

    public void setCompId(Integer compId) {
        this.compId = compId;
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public Integer getFarmMasterNum() {
        return farmMasterNum;
    }

    public void setFarmMasterNum(Integer farmMasterNum) {
        this.farmMasterNum = farmMasterNum;
    }

    public Integer getSeasonNum() {
        return seasonNum;
    }

    public void setSeasonNum(Integer seasonNum) {
        this.seasonNum = seasonNum;
    }

    public Integer getCropId() {
        return cropId;
    }

    public void setCropId(Integer cropId) {
        this.cropId = cropId;
    }

    public Float getFarmArea() {
        return farmArea;
    }

    public void setFarmArea(Float farmArea) {
        this.farmArea = farmArea;
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

    public List<DataCropDetails> getData() {
        return data;
    }

    public void setData(List<DataCropDetails> data) {
        this.data = data;
    }
}
