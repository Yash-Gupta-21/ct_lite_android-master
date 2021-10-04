package com.i9930.croptrails.Maps.ShowArea.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OmiFetchDetails {
    @SerializedName("area")
    @Expose
    private Double area;
    @SerializedName("latLngList")
    @Expose
    private List<OmiFetchLatLng> latLngList = null;

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public List<OmiFetchLatLng> getLatLngList() {
        return latLngList;
    }

    public void setLatLngList(List<OmiFetchLatLng> latLngList) {
        this.latLngList = latLngList;
    }

}
