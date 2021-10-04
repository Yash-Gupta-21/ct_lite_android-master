package com.i9930.croptrails.SubmitActivityForm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompAgriDatum {

    @SerializedName("id")
    @Expose
    private String id;


    @SerializedName("cost_per_unit")
    @Expose
    private String costPerUnit;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("carbon_FPC")
    @Expose
    private String carbonFPC;
    @SerializedName("is_service")
    @Expose
    private String isService;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("unit")
    @Expose
    private String unit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(String costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getCarbonFPC() {
        return carbonFPC;
    }

    public void setCarbonFPC(String carbonFPC) {
        this.carbonFPC = carbonFPC;
    }

    public String getIsService() {
        return isService;
    }

    public void setIsService(String isService) {
        this.isService = isService;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
