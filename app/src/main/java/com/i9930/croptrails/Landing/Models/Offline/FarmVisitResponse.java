package com.i9930.croptrails.Landing.Models.Offline;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.FarmDetails.Model.Activity.DoneActivityImage;

public class FarmVisitResponse {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("visit_report")
    @Expose
    private List<VisitReport> visitReport = null;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public List<VisitReport> getVisitReport() {
        return visitReport;
    }

    public void setVisitReport(List<VisitReport> visitReport) {
        this.visitReport = visitReport;
    }


    public class VisitReport {

        @SerializedName("visit_report_id")
        @Expose
        private long visitReportId;
        @SerializedName("visit_date")
        @Expose
        private String visitDate;

        @SerializedName("comment")
        @Expose
        private String comment;
        
        @SerializedName("visit_number")
        @Expose
        private long visitNumber;
        @SerializedName("effective_area")
        @Expose
        private long effectiveArea;
        @SerializedName("grade")
        @Expose
        private String grade;
        @SerializedName("moisture")
        @Expose
        private long moisture;
        @SerializedName("agri_inputs")
        @Expose
        private List<AgriInput> agriInputs = null;
        @SerializedName("images")
        @Expose
        private List<DoneActivityImage> images = null;

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

        public long getEffectiveArea() {
            return effectiveArea;
        }

        public void setEffectiveArea(long effectiveArea) {
            this.effectiveArea = effectiveArea;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public long getMoisture() {
            return moisture;
        }

        public void setMoisture(long moisture) {
            this.moisture = moisture;
        }

        public List<AgriInput> getAgriInputs() {
            return agriInputs;
        }

        public void setAgriInputs(List<AgriInput> agriInputs) {
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

    

    public class AgriInput {

        @SerializedName("id")
        @Expose
        private long id;
        @SerializedName("quantity")
        @Expose
        private long quantity;
        @SerializedName("agri_id")
        @Expose
        private long agriId;
        @SerializedName("amount")
        @Expose
        private long amount;

        @SerializedName("farm_calendar_activity_id")
        @Expose
        private String farmCalendarActivityId;
        @SerializedName("is_active")
        @Expose
        private String isActive;
        @SerializedName("farm_id")
        @Expose
        private long farmId;
        @SerializedName("vr_id")
        @Expose
        private long vrId;
        @SerializedName("narration")
        @Expose
        private String narration;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("doa")
        @Expose
        private String doa;
        @SerializedName("other_agri_input")
        @Expose
        private String otherAgriInput;
        @SerializedName("name")
        @Expose
        private String name;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getQuantity() {
            return quantity;
        }

        public void setQuantity(long quantity) {
            this.quantity = quantity;
        }

        public long getAgriId() {
            return agriId;
        }

        public void setAgriId(long agriId) {
            this.agriId = agriId;
        }

        public long getAmount() {
            return amount;
        }

        public void setAmount(long amount) {
            this.amount = amount;
        }



        public String getFarmCalendarActivityId() {
            return farmCalendarActivityId;
        }

        public void setFarmCalendarActivityId(String farmCalendarActivityId) {
            this.farmCalendarActivityId = farmCalendarActivityId;
        }

        public String getIsActive() {
            return isActive;
        }

        public void setIsActive(String isActive) {
            this.isActive = isActive;
        }

        public long getFarmId() {
            return farmId;
        }

        public void setFarmId(long farmId) {
            this.farmId = farmId;
        }

        public long getVrId() {
            return vrId;
        }

        public void setVrId(long vrId) {
            this.vrId = vrId;
        }

        public String getNarration() {
            return narration;
        }

        public void setNarration(String narration) {
            this.narration = narration;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDoa() {
            return doa;
        }

        public void setDoa(String doa) {
            this.doa = doa;
        }

        public String getOtherAgriInput() {
            return otherAgriInput;
        }

        public void setOtherAgriInput(String otherAgriInput) {
            this.otherAgriInput = otherAgriInput;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
