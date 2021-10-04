package com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IrrigationType {

    @SerializedName("irrigation_type_id")
    @Expose
    private String irrigationTypeId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("added_by")
    @Expose
    private Object addedBy;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("doa")
    @Expose
    private String doa;

    public IrrigationType() {
    }

    public IrrigationType(String irrigationTypeId, String name) {
        this.irrigationTypeId = irrigationTypeId;
        this.name = name;
    }

    public String getIrrigationTypeId() {
        return irrigationTypeId;
    }

    public void setIrrigationTypeId(String irrigationTypeId) {
        this.irrigationTypeId = irrigationTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
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

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

}