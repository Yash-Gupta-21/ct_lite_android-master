package com.i9930.croptrails.Landing.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeoCardsResponse {
    @SerializedName("totalArea")
    @Expose
    private String totalArea;
    @SerializedName("geoTaggedArea")
    @Expose
    private String geoTaggedArea;
    @SerializedName("geoTaggedCount")
    @Expose
    private Integer geoTaggedCount;
    @SerializedName("nonGeoTagged")
    @Expose
    private Integer nonGeoTagged;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(String totalArea) {
        this.totalArea = totalArea;
    }

    public String getGeoTaggedArea() {
        return geoTaggedArea;
    }

    public void setGeoTaggedArea(String geoTaggedArea) {
        this.geoTaggedArea = geoTaggedArea;
    }

    public Integer getGeoTaggedCount() {
        return geoTaggedCount;
    }

    public void setGeoTaggedCount(Integer geoTaggedCount) {
        this.geoTaggedCount = geoTaggedCount;
    }

    public Integer getNonGeoTagged() {
        return nonGeoTagged;
    }

    public void setNonGeoTagged(Integer nonGeoTagged) {
        this.nonGeoTagged = nonGeoTagged;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
