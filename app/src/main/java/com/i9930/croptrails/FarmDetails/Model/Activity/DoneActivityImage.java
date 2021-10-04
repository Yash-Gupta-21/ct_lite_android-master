package com.i9930.croptrails.FarmDetails.Model.Activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoneActivityImage {
    @SerializedName("img_link")
    @Expose
    private String imgLink;

    String lat="";
    String lon="";

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}