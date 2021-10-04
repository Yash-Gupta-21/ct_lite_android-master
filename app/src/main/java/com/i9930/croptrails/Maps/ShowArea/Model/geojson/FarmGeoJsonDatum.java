package com.i9930.croptrails.Maps.ShowArea.Model.geojson;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FarmGeoJsonDatum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("farm_id")
    @Expose
    private Integer farmId;
    @SerializedName("comp_id")
    @Expose
    private Integer compId;
    @SerializedName("region_id")
    @Expose
    private Integer regionId;
//    @SerializedName("parent_id")
//    @Expose
//    private Object parentId;
    @SerializedName("details")
    @Expose
    private JsonObject details;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFarmId() {
        return farmId;
    }

    public void setFarmId(Integer farmId) {
        this.farmId = farmId;
    }

    public Integer getCompId() {
        return compId;
    }

    public void setCompId(Integer compId) {
        this.compId = compId;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

//    public Object getParentId() {
//        return parentId;
//    }
//
//    public void setParentId(Object parentId) {
//        this.parentId = parentId;
//    }

    public JsonObject getDetails() {
        return details;
    }

    public void setDetails(JsonObject details) {
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
