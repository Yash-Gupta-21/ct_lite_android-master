package com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;

public class NearFarmDatum {

    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("geometry")
    @Expose
    private List<NewFarmLatLang> geometry = null;

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public List<NewFarmLatLang> getGeometry() {
        return geometry;
    }

    public void setGeometry(List<NewFarmLatLang> geometry) {
        this.geometry = geometry;
    }

}
