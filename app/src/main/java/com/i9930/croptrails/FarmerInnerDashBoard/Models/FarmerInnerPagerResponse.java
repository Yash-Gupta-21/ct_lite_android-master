package com.i9930.croptrails.FarmerInnerDashBoard.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.FarmDetails.Model.LineChartInfo;

public class FarmerInnerPagerResponse implements Parcelable {

    @SerializedName("image")
    @Expose
    private Image image;
    @SerializedName("timeline")
    @Expose
    private List<Timeline> timeline = null;
    @SerializedName("days")
    @Expose
    private Integer days;
    @SerializedName("farm_details")
    @Expose
    private FarmDetails farmDetails;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("date_wise_grade")
    @Expose
    private List<LineChartInfo> dateWiseGrade = null;
    @SerializedName("activity_stats")
    @Expose
    private ActivityStats activityStats;

    public ActivityStats getActivityStats() {
        return activityStats;
    }

    public void setActivityStats(ActivityStats activityStats) {
        this.activityStats = activityStats;
    }

    public List<LineChartInfo> getDateWiseGrade() {
        return dateWiseGrade;
    }

    public void setDateWiseGrade(List<LineChartInfo> dateWiseGrade) {
        this.dateWiseGrade = dateWiseGrade;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Timeline> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<Timeline> timeline) {
        this.timeline = timeline;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public FarmDetails getFarmDetails() {
        return farmDetails;
    }

    public void setFarmDetails(FarmDetails farmDetails) {
        this.farmDetails = farmDetails;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
