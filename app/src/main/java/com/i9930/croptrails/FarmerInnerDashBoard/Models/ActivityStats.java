package com.i9930.croptrails.FarmerInnerDashBoard.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivityStats implements Parcelable {

    @SerializedName("completed_activity")
    @Expose
    private String completedActivity;
    @SerializedName("pending_activity")
    @Expose
    private String pendingActivity;
    @SerializedName("total_activity")
    @Expose
    private Integer totalActivity;
    @SerializedName("complete_date_status")
    @Expose
    private String completeDateStatus;
    @SerializedName("pending_date_status")
    @Expose
    private String pendingDateStatus;
    @SerializedName("total_date_status")
    @Expose
    private Integer totalDateStatus;

    public String getCompletedActivity() {
        return completedActivity;
    }

    public void setCompletedActivity(String completedActivity) {
        this.completedActivity = completedActivity;
    }

    public String getPendingActivity() {
        return pendingActivity;
    }

    public void setPendingActivity(String pendingActivity) {
        this.pendingActivity = pendingActivity;
    }

    public Integer getTotalActivity() {
        return totalActivity;
    }

    public void setTotalActivity(Integer totalActivity) {
        this.totalActivity = totalActivity;
    }

    public String getCompleteDateStatus() {
        return completeDateStatus;
    }

    public void setCompleteDateStatus(String completeDateStatus) {
        this.completeDateStatus = completeDateStatus;
    }

    public String getPendingDateStatus() {
        return pendingDateStatus;
    }

    public void setPendingDateStatus(String pendingDateStatus) {
        this.pendingDateStatus = pendingDateStatus;
    }

    public Integer getTotalDateStatus() {
        return totalDateStatus;
    }

    public void setTotalDateStatus(Integer totalDateStatus) {
        this.totalDateStatus = totalDateStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public ActivityStats(Parcel in){
        this.completedActivity=in.readString();
        this.completeDateStatus=in.readString();
        this.pendingActivity=in.readString();
        this.totalActivity=in.readInt();
        this.completedActivity=in.readString();
        this.totalDateStatus=in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}