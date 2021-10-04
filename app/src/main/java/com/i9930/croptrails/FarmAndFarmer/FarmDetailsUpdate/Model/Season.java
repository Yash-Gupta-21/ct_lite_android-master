package com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Season {

    @SerializedName("season_id")
    @Expose
    private String seasonId;
    @SerializedName("season_num")
    @Expose
    private String seasonNum;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("weather_type")
    @Expose
    private String weatherType;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("doi")
    @Expose
    private Object doi;
    @SerializedName("inactivated_by")
    @Expose
    private Object inactivatedBy;
    @SerializedName("added_by")
    @Expose
    private Object addedBy;
    @SerializedName("is_active")
    @Expose
    private String isActive;

    boolean isChecked;

    public Season() {
    }

    public Season(String seasonId, String name) {
        this.seasonId = seasonId;
        this.name = name;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getSeasonNum() {
        return seasonNum;
    }

    public void setSeasonNum(String seasonNum) {
        this.seasonNum = seasonNum;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public Object getDoi() {
        return doi;
    }

    public void setDoi(Object doi) {
        this.doi = doi;
    }

    public Object getInactivatedBy() {
        return inactivatedBy;
    }

    public void setInactivatedBy(Object inactivatedBy) {
        this.inactivatedBy = inactivatedBy;
    }

    public Object getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Object addedBy) {
        this.addedBy = addedBy;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}