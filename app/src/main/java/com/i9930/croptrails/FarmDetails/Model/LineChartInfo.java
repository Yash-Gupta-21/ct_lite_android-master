package com.i9930.croptrails.FarmDetails.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LineChartInfo implements Parcelable {
    @SerializedName("visit_date")
    @Expose
    private String visitDate;
    @SerializedName("material_id")
    @Expose
    private String materialId;
    @SerializedName("done_date")
    @Expose
    private String doneDate;
    @SerializedName("activity_template_id")
    @Expose
    private String activityTemplateId;
    @SerializedName("grade")
    @Expose
    private String grade;

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(String doneDate) {
        this.doneDate = doneDate;
    }

    public String getActivityTemplateId() {
        return activityTemplateId;
    }

    public void setActivityTemplateId(String activityTemplateId) {
        this.activityTemplateId = activityTemplateId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public LineChartInfo(Parcel in){
        this.visitDate=in.readString();
        this.materialId=in.readString();
        this.doneDate=in.readString();
        this.activityTemplateId=in.readString();
        this.grade=in.readString();

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
