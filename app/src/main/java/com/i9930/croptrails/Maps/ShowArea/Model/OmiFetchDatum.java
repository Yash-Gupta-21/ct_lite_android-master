package com.i9930.croptrails.Maps.ShowArea.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OmiFetchDatum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("region_id")
    @Expose
    private Object regionId;
    @SerializedName("details")
    @Expose
    private OmiFetchDetails details;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("is_active")
    @Expose
    private String isActive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public Object getRegionId() {
        return regionId;
    }

    public void setRegionId(Object regionId) {
        this.regionId = regionId;
    }

    public OmiFetchDetails getDetails() {
        return details;
    }

    public void setDetails(OmiFetchDetails details) {
        this.details = details;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
