package com.i9930.croptrails.FarmDetails.Model.Visit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisitReportTimeline {

    @SerializedName("visit_report_id")
    @Expose
    private String visitReportId;
    @SerializedName("visit_date")
    @Expose
    private String visitDate;
    @SerializedName("visit_number")
    @Expose
    private String visitNumber;
    @SerializedName("effective_area")
    @Expose
    private String effectiveArea;

    String lat="";
    String lon="";

    public String getVisitReportId() {
        return visitReportId;
    }

    public void setVisitReportId(String visitReportId) {
        this.visitReportId = visitReportId;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    public String getEffectiveArea() {
        return effectiveArea;
    }

    public void setEffectiveArea(String effectiveArea) {
        this.effectiveArea = effectiveArea;
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
