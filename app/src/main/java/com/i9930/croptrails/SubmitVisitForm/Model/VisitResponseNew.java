package com.i9930.croptrails.SubmitVisitForm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.FarmDetails.Model.Visit.VisitImageTimeline;
import com.i9930.croptrails.FarmDetails.Model.Visit.VisitReportTimeline;
import com.i9930.croptrails.SubmitActivityForm.Model.AgriInput.AgriInput;
import com.i9930.croptrails.SubmitActivityForm.Model.DoneActNew.DoneActivityResponseNew;

public class VisitResponseNew {

    DoneActivityResponseNew showResponse;
    @SerializedName("visit_number")
    @Expose
    private String visitNumber;

    @SerializedName("effective_area")
    @Expose
    private String effectiveArea;

    @SerializedName("visit_report_id")
    @Expose
    private String visitReportId;
    @SerializedName("farm_grade")
    @Expose
    private String farmGrade;

    @SerializedName("comment")
    @Expose
    private String comment;

    @SerializedName("moisture")
    @Expose
    private String moisture;

    @SerializedName("approved_method")
    @Expose
    private String approvedMethod;
    @SerializedName("comp_id")
    @Expose
    private String compId;

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("added_by")
    @Expose
    private String addedBy;


    @SerializedName("farmId")
    @Expose
    private String farmId;

    @SerializedName("visit_img")
    @Expose
    private List<String> visitImageStr;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("visit_report")
    @Expose
    private VisitReportTimeline visitReport = null;
    @SerializedName("agri_inputs")
    @Expose
    private List<AgriInput> agriInputList = null;
    @SerializedName("visit_images")
    @Expose
    private List<VisitImageTimeline> visitImages = null;

    String [] img_list;
    String doa;

    public VisitReportTimeline getVisitReport() {
        return visitReport;
    }

    public void setVisitReport(VisitReportTimeline visitReport) {
        this.visitReport = visitReport;
    }



    public List<VisitImageTimeline> getVisitImages() {
        return visitImages;
    }

    public void setVisitImages(List<VisitImageTimeline> visitImages) {
        this.visitImages = visitImages;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public List<String> getVisitImageStr() {
        return visitImageStr;
    }

    public String[] getImg_list() {
        return img_list;
    }

    public void setImg_list(String[] img_list) {
        this.img_list = img_list;
    }

    public void setVisitImageStr(List<String> visitImageStr) {
        this.visitImageStr = visitImageStr;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getApprovedMethod() {
        return approvedMethod;
    }

    public void setApprovedMethod(String approvedMethod) {
        this.approvedMethod = approvedMethod;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<AgriInput> getAgriInputList() {
        return agriInputList;
    }

    public void setAgriInputList(List<AgriInput> agriInputList) {
        this.agriInputList = agriInputList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMoisture() {
        return moisture;
    }

    public void setMoisture(String moisture) {
        this.moisture = moisture;
    }

    public String getFarmGrade() {
        return farmGrade;
    }

    public void setFarmGrade(String farmGrade) {
        this.farmGrade = farmGrade;
    }


    public DoneActivityResponseNew getShowResponse() {
        return showResponse;
    }

    public void setShowResponse(DoneActivityResponseNew showResponse) {
        this.showResponse = showResponse;
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

    public String getVisitReportId() {
        return visitReportId;
    }

    public void setVisitReportId(String visitReportId) {
        this.visitReportId = visitReportId;
    }
}

