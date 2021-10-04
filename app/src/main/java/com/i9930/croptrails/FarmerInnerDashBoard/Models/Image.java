package com.i9930.croptrails.FarmerInnerDashBoard.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("img_link")
    @Expose
    private String imgLink;
    @SerializedName("event_type")
    @Expose
    private String eventType;
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("farm_id")
    @Expose
    private String farmId;

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }
}