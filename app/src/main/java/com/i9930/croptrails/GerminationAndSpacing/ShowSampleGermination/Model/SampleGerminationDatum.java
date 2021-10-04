package com.i9930.croptrails.GerminationAndSpacing.ShowSampleGermination.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.i9930.croptrails.AddFarm.Model.FarmData;

public class SampleGerminationDatum {

    @SerializedName("compare_type")
    String type;
    @SerializedName("cd_sample_id")
    @Expose
    private String cdSampleId;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("configured_area")
    @Expose
    private String configuredArea;
    @SerializedName("spacing_ptp")
    @Expose
    private String spacingPtp;
    @SerializedName("spacing_rtr")
    @Expose
    private String spacingRtr;
    @SerializedName("ideal_pop")
    @Expose
    private String idealPop;
    @SerializedName("actual_pop")
    @Expose
    private String actualPop;
    @SerializedName("actual_total_population")
    @Expose
    private String actualTotalPopulation;
    @SerializedName("added_by")
    @Expose
    private String addedBy;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("germination")
    @Expose
    private Double germination;
    @SerializedName("germination_avg")
    @Expose
    private Double germinationAvg;
    @SerializedName("spacing_ptp_avg")
    @Expose
    private Double spacingPtpAvg;
    @SerializedName("spacing_rtr_avg")
    @Expose
    private Double spacingRtrAvg;
    @SerializedName("actual_pop_avg")
    @Expose
    private Double actualPopAvg;

    int index;

    public Double getGermination() {
        return germination;
    }

    public void setGermination(Double germination) {
        this.germination = germination;
    }

    public Double getGerminationAvg() {
        return germinationAvg;
    }

    public void setGerminationAvg(Double germinationAvg) {
        this.germinationAvg = germinationAvg;
    }

    public Double getSpacingPtpAvg() {
        return spacingPtpAvg;
    }

    public void setSpacingPtpAvg(Double spacingPtpAvg) {
        this.spacingPtpAvg = spacingPtpAvg;
    }

    public Double getSpacingRtrAvg() {
        return spacingRtrAvg;
    }

    public void setSpacingRtrAvg(Double spacingRtrAvg) {
        this.spacingRtrAvg = spacingRtrAvg;
    }

    public Double getActualPopAvg() {
        return actualPopAvg;
    }

    public void setActualPopAvg(Double actualPopAvg) {
        this.actualPopAvg = actualPopAvg;
    }

    public String getCdSampleId() {
        return cdSampleId;
    }

    public void setCdSampleId(String cdSampleId) {
        this.cdSampleId = cdSampleId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getConfiguredArea() {
        return configuredArea;
    }

    public void setConfiguredArea(String configuredArea) {
        this.configuredArea = configuredArea;
    }

    public String getSpacingPtp() {
        return spacingPtp;
    }

    public void setSpacingPtp(String spacingPtp) {
        this.spacingPtp = spacingPtp;
    }

    public String getSpacingRtr() {
        return spacingRtr;
    }

    public void setSpacingRtr(String spacingRtr) {
        this.spacingRtr = spacingRtr;
    }

    public String getIdealPop() {
        return idealPop;
    }

    public void setIdealPop(String idealPop) {
        this.idealPop = idealPop;
    }

    public String getActualPop() {
        return actualPop;
    }

    public void setActualPop(String actualPop) {
        this.actualPop = actualPop;
    }

    public String getActualTotalPopulation() {
        return actualTotalPopulation;
    }

    public void setActualTotalPopulation(String actualTotalPopulation) {
        this.actualTotalPopulation = actualTotalPopulation;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}