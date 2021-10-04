package com.i9930.croptrails.AssignCalendar.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CalendarDatum implements Parcelable {

    @SerializedName("calendar_name")
    String cycleName;
    @SerializedName("calendar_id")
    String calendarId;
    @SerializedName("comp_id")
    String compId;
    @SerializedName("crop_name")
    String cropName;
    @SerializedName("growing_season")
    String growingSeason;
    @SerializedName("growing_region")
    String growingRegion;
    @SerializedName("soil_type")
    String soilType;

    public CalendarDatum(String cycleName, String cropName, String growingSeason, String growingRegion, String soilType) {
        this.cycleName = cycleName;
        this.cropName = cropName;
        this.growingSeason = growingSeason;
        this.growingRegion = growingRegion;
        this.soilType = soilType;
    }

    public CalendarDatum() {
    }

    protected CalendarDatum(Parcel in) {
        cycleName = in.readString();
        calendarId = in.readString();
        compId = in.readString();
        cropName = in.readString();
        growingSeason = in.readString();
        growingRegion = in.readString();
        soilType = in.readString();
    }

    public static final Creator<CalendarDatum> CREATOR = new Creator<CalendarDatum>() {
        @Override
        public CalendarDatum createFromParcel(Parcel in) {
            return new CalendarDatum(in);
        }

        @Override
        public CalendarDatum[] newArray(int size) {
            return new CalendarDatum[size];
        }
    };

    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getGrowingSeason() {
        return growingSeason;
    }

    public void setGrowingSeason(String growingSeason) {
        this.growingSeason = growingSeason;
    }

    public String getGrowingRegion() {
        return growingRegion;
    }

    public void setGrowingRegion(String growingRegion) {
        this.growingRegion = growingRegion;
    }

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cycleName);
        dest.writeString(calendarId);
        dest.writeString(compId);
        dest.writeString(cropName);
        dest.writeString(growingSeason);
        dest.writeString(growingRegion);
        dest.writeString(soilType);
    }
}
