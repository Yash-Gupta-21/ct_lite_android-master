package com.i9930.croptrails.SubmitVisitForm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.FarmDetails.Model.Activity.DoneActivityImage;
import com.i9930.croptrails.RoomDatabase.Visit.Visit;

public class FetchVisitResponse {
    @SerializedName("added")
    @Expose
    private String added;
    @SerializedName("delq_reason")
    @Expose
    private String delqReason;
    @SerializedName("visit_report_id")
    @Expose
    private String visitReportId;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("approved_method")
    @Expose
    private String approvedMethod;
    @SerializedName("visit_date")
    @Expose
    private String visitDate;
    @SerializedName("visit_number")
    @Expose
    private String visitNumber;
    @SerializedName("effective_area")
    @Expose
    private String effectiveArea;
    @SerializedName("fapp_timestamp")
    @Expose
    private String fappTimestamp;
    @SerializedName("svapp_timestamp")
    @Expose
    private String svappTimestamp;
    @SerializedName("added_by")
    @Expose
    private String addedBy;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("grade")
    @Expose
    private String grade;
    @SerializedName("moisture")
    @Expose
    private String moisture;
    @SerializedName("agri_inputs")
    @Expose
    private List<VisitDatum> agriInputs = null;
    @SerializedName("images")
    @Expose
    private List<DoneActivityImage> images = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getVisitReportId() {
        return visitReportId;
    }

    public void setVisitReportId(String visitReportId) {
        this.visitReportId = visitReportId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getApprovedMethod() {
        return approvedMethod;
    }

    public void setApprovedMethod(String approvedMethod) {
        this.approvedMethod = approvedMethod;
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

    public String getFappTimestamp() {
        return fappTimestamp;
    }

    public void setFappTimestamp(String fappTimestamp) {
        this.fappTimestamp = fappTimestamp;
    }

    public String getSvappTimestamp() {
        return svappTimestamp;
    }

    public void setSvappTimestamp(String svappTimestamp) {
        this.svappTimestamp = svappTimestamp;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public List<VisitDatum> getAgriInputs() {
        return agriInputs;
    }

    public void setAgriInputs(List<VisitDatum> agriInputs) {
        this.agriInputs = agriInputs;
    }

    public List<DoneActivityImage> getImages() {
        return images;
    }

    public void setImages(List<DoneActivityImage> images) {
        this.images = images;
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

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public String getDelqReason() {
        return delqReason;
    }

    public void setDelqReason(String delqReason) {
        this.delqReason = delqReason;
    }
}
