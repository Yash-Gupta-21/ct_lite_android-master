package com.i9930.croptrails.AddFarm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmCrop {

    @SerializedName("crop_id")
    @Expose
    private String cropId;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("crop_name")
    @Expose
    private String name;

    public FarmCrop(String cropId, String name) {
        this.cropId = cropId;
        this.name = name;
    }

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
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
}
