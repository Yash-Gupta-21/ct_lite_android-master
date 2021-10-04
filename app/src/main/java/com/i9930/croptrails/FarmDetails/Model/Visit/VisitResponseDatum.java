package com.i9930.croptrails.FarmDetails.Model.Visit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.FarmDetails.Model.Activity.DoneActivityImage;
import com.i9930.croptrails.SubmitActivityForm.Model.DoneActNew.DoneActInputs;

public class VisitResponseDatum {

    @SerializedName("images")
    @Expose
    private List<DoneActivityImage> images = null;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("visit_report_id")
    @Expose
    private long visitReportId;

    @SerializedName("visit_date")
    @Expose
    private String visitDate;
    @SerializedName("visit_number")
    @Expose
    private long visitNumber;
    @SerializedName("effective_area")
    @Expose
    private double effectiveArea;
    @SerializedName("grade")
    @Expose
    private String grade;
    @SerializedName("moisture")
    @Expose
    private String moisture;
    @SerializedName("agri_inputs")
    @Expose
    private List<DoneActInputs> agriInputs = null;

    public long getVisitReportId() {
        return visitReportId;
    }

    public void setVisitReportId(long visitReportId) {
        this.visitReportId = visitReportId;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public long getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(long visitNumber) {
        this.visitNumber = visitNumber;
    }

    public double getEffectiveArea() {
        return effectiveArea;
    }

    public void setEffectiveArea(double effectiveArea) {
        this.effectiveArea = effectiveArea;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMoisture() {
        return moisture;
    }

    public void setMoisture(String moisture) {
        this.moisture = moisture;
    }

    public List<DoneActInputs> getAgriInputs() {
        return agriInputs;
    }

    public void setAgriInputs(List<DoneActInputs> agriInputs) {
        this.agriInputs = agriInputs;
    }

    public List<DoneActivityImage> getImages() {
        return images;
    }

    public void setImages(List<DoneActivityImage> images) {
        this.images = images;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
