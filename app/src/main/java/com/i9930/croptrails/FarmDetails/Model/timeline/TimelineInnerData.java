package com.i9930.croptrails.FarmDetails.Model.timeline;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.SubmitActivityForm.Adapter.AgriInstructionAdapter;
import com.i9930.croptrails.SubmitActivityForm.Model.AgriInput.AgriInput;
import com.i9930.croptrails.SubmitActivityForm.Model.AgriInput.Instruction;

public class TimelineInnerData implements Parcelable {
    List<AgriInput> agriInput;
    List<Instruction> instructionList;
    @SerializedName("compare_type")
    String type;

    @SerializedName("reason")
    String reason;

    @SerializedName("comment")
    String comment;

    @SerializedName("farm_cal_activity_id")
    @Expose
    private String farmCalActivityId;
    @SerializedName("visit_report_id")
    @Expose
    private String visitReportId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("img_link")
    @Expose
    private String img_link;
    @SerializedName("activity")
    @Expose
    private String activity;
    @SerializedName("activity_type_id")
    @Expose
    private String activityTypeId;
    @SerializedName("activity_type")
    @Expose
    private String activityType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("chemical_id")
    @Expose
    private String chemicalId;
    @SerializedName("chemical_qty")
    @Expose
    private String chemicalQty;
    @SerializedName("qty_unit")
    @Expose
    private String qtyUnit;
    @SerializedName("is_done")
    @Expose
    private String isDone;
    @SerializedName("farmer_reply")
    @Expose
    private String farmerReply;
    @SerializedName("chemical")
    @Expose
    private String chemical;
    @SerializedName("activity_img")
    @Expose
    private  String activityImg;

    @SerializedName("unit")
    @Expose
    private  String unit;


    @SerializedName("dataType")
    @Expose
    private String dataType;
    @SerializedName("germination")
    @Expose
    private String germination;
    @SerializedName("population")
    @Expose
    private String population;
    @SerializedName("spacing_rtr")
    @Expose
    private String spacingRtr;
    @SerializedName("spacing_ptp")
    @Expose
    private String spacingPtp;
    @SerializedName("resultType")
    @Expose
    private String resultType;
    String doa;
    int index;

    public TimelineInnerData(String farmCalActivityId,String visitReportId, String farmId, String activity, String activityType, String date, String description, String chemicalId, String chemicalQty, String qtyUnit, String isDone, String farmerReply,String resultType) {
        this.farmCalActivityId = farmCalActivityId;
        this.farmId = farmId;
        this.activity = activity;
        this.activityType = activityType;
        this.date = date;
        this.description = description;
        this.chemicalId = chemicalId;
        this.chemicalQty = chemicalQty;
        this.qtyUnit = qtyUnit;
        this.isDone = isDone;
        this.farmerReply = farmerReply;
        this.visitReportId=visitReportId;
        this.resultType=resultType;
    }

    public TimelineInnerData(String farmCalActivityId,String visitReportId, String farmId, String activity, String activityType, String date, String description, String chemicalId, String chemicalQty, String qtyUnit, String isDone, String farmerReply) {
        this.farmCalActivityId = farmCalActivityId;
        this.farmId = farmId;
        this.activity = activity;
        this.activityType = activityType;
        this.date = date;
        this.description = description;
        this.chemicalId = chemicalId;
        this.chemicalQty = chemicalQty;
        this.qtyUnit = qtyUnit;
        this.isDone = isDone;
        this.farmerReply = farmerReply;
        this.visitReportId=visitReportId;
    }

    protected TimelineInnerData(Parcel in) {
        farmCalActivityId = in.readString();
        visitReportId = in.readString();
        date = in.readString();
        farmId = in.readString();
        img_link = in.readString();
        activity = in.readString();
        activityTypeId = in.readString();
        activityType = in.readString();
        description = in.readString();
        priority = in.readString();
        chemicalId = in.readString();
        chemicalQty = in.readString();
        qtyUnit = in.readString();
        isDone = in.readString();
        farmerReply = in.readString();
        chemical = in.readString();
        activityImg = in.readString();
        unit = in.readString();
        resultType = in.readString();
        type=in.readString();
        doa=in.readString();
        index=in.readInt();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static final Creator<TimelineInnerData> CREATOR = new Creator<TimelineInnerData>() {
        @Override
        public TimelineInnerData createFromParcel(Parcel in) {
            return new TimelineInnerData(in);
        }

        @Override
        public TimelineInnerData[] newArray(int size) {
            return new TimelineInnerData[size];
        }
    };

    public String getFarmCalActivityId() {
        return farmCalActivityId;
    }

    public void setFarmCalActivityId(String farmCalActivityId) {
        this.farmCalActivityId = farmCalActivityId;
    }

    public String getVisitReportId() {
        return visitReportId;
    }

    public void setVisitReportId(String visitReportId) {
        this.visitReportId = visitReportId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getChemicalId() {
        return chemicalId;
    }

    public void setChemicalId(String chemicalId) {
        this.chemicalId = chemicalId;
    }

    public String getChemicalQty() {
        return chemicalQty;
    }

    public void setChemicalQty(String chemicalQty) {
        this.chemicalQty = chemicalQty;
    }

    public String getQtyUnit() {
        return qtyUnit;
    }

    public void setQtyUnit(String qtyUnit) {
        this.qtyUnit = qtyUnit;
    }

    public String getIsDone() {
        return isDone;
    }

    public void setIsDone(String isDone) {
        this.isDone = isDone;
    }

    public String getFarmerReply() {
        return farmerReply;
    }

    public void setFarmerReply(String farmerReply) {
        this.farmerReply = farmerReply;
    }

    public String getChemical() {
        return chemical;
    }

    public void setChemical(String chemical) {
        this.chemical = chemical;
    }

    public String getActivityImg() {
        return activityImg;
    }

    public void setActivityImg(String activityImg) {
        this.activityImg = activityImg;
    }

    public String getImg_link() {
        return img_link;
    }

    public void setImg_link(String img_link) {
        this.img_link = img_link;
    }

    public String getActivityTypeId() {
        return activityTypeId;
    }

    public void setActivityTypeId(String activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getGermination() {
        return germination;
    }

    public void setGermination(String germination) {
        this.germination = germination;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getSpacingRtr() {
        return spacingRtr;
    }

    public void setSpacingRtr(String spacingRtr) {
        this.spacingRtr = spacingRtr;
    }

    public String getSpacingPtp() {
        return spacingPtp;
    }

    public void setSpacingPtp(String spacingPtp) {
        this.spacingPtp = spacingPtp;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(farmCalActivityId);
        parcel.writeString(visitReportId);
        parcel.writeString(date);
        parcel.writeString(farmId);
        parcel.writeString(img_link);
        parcel.writeString(activity);
        parcel.writeString(activityTypeId);
        parcel.writeString(activityType);
        parcel.writeString(description);
        parcel.writeString(priority);
        parcel.writeString(chemicalId);
        parcel.writeString(chemicalQty);
        parcel.writeString(qtyUnit);
        parcel.writeString(isDone);
        parcel.writeString(farmerReply);
        parcel.writeString(chemical);
        parcel.writeString(activityImg);
        parcel.writeString(unit);
        parcel.writeString(resultType);
        parcel.writeString(type);
        parcel.writeString(doa);
        parcel.writeInt(index);
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<AgriInput> getAgriInput() {
        return agriInput;
    }

    public void setAgriInput(List<AgriInput> agriInput) {
        this.agriInput = agriInput;
    }

    public List<Instruction> getInstructionList() {
        return instructionList;
    }

    public void setInstructionList(List<Instruction> instructionList) {
        this.instructionList = instructionList;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
