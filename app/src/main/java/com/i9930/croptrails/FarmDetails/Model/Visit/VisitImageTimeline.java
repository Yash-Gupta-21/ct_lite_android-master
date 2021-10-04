package com.i9930.croptrails.FarmDetails.Model.Visit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisitImageTimeline {

    @SerializedName("image_id")
    @Expose
    private String imageId;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("visit_report_id")
    @Expose
    private String visitReportId;

    @SerializedName("pld_id")
    @Expose
    private String pldId;
    @SerializedName("img_link")
    @Expose
    private String imgLink;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("doa")
    @Expose
    private String doa;


    String lat;
    String lon;

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getVisitReportId() {
        return visitReportId;
    }

    public void setVisitReportId(String visitReportId) {
        this.visitReportId = visitReportId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getPldId() {
        return pldId;
    }

    public void setPldId(String pldId) {
        this.pldId = pldId;
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
