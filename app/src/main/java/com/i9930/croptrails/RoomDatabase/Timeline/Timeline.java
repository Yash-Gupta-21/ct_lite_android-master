package com.i9930.croptrails.RoomDatabase.Timeline;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "timeline")
public class Timeline {

    @PrimaryKey(autoGenerate = true)
    private int timelineId;

    @ColumnInfo(name = "farm_id")
    private String farmId;

    @ColumnInfo(name = "jsondata")
    private String jsondata;

    @ColumnInfo(name = "images")
    private String images;


    public Timeline(String farmId, String jsondata, String images) {
        this.farmId = farmId;
        this.jsondata = jsondata;
        this.images = images;
    }


    public int getTimelineId() {
        return timelineId;
    }

    public void setTimelineId(int timelineId) {
        this.timelineId = timelineId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getJsondata() {
        return jsondata;
    }

    public void setJsondata(String jsondata) {
        this.jsondata = jsondata;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
