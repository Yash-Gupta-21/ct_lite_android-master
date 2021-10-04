package com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IrrigationSource {

    @SerializedName("irrigation_source_id")
    @Expose
    private String irrigationSourceId;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("added_by")
    @Expose
    private Object addedBy;
    @SerializedName("doa")
    @Expose
    private String doa;

    public IrrigationSource() {
    }

    public IrrigationSource(String irrigationSourceId, String name) {
        this.irrigationSourceId = irrigationSourceId;
        this.name = name;
    }

    public String getIrrigationSourceId() {
        return irrigationSourceId;
    }

    public void setIrrigationSourceId(String irrigationSourceId) {
        this.irrigationSourceId = irrigationSourceId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Object addedBy) {
        this.addedBy = addedBy;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

}