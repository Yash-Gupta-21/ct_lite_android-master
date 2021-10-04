package com.i9930.croptrails.RoomDatabase.Visit;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "visit")
public class Visit {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int offlineId;

    @ColumnInfo(name = "vrId")
    private String vrId;

    @ColumnInfo(name = "farmId")
    private String farmId;

    @ColumnInfo(name = "visit_json")
    private String visitJson;

    @ColumnInfo(name = "is_uploaded")
    private String isUploaded;

    @ColumnInfo(name = "imageArray")
    private String imageArray;

    @ColumnInfo(name = "is_offline_added")
    private String isOfflineAdded;
    @ColumnInfo(name = "doa")
    private String doa;

    public Visit(String vrId, String visitJson, String isUploaded, String imageArray,String isOfflineAdded,String doa,String farmId) {
        this.vrId = vrId;
        this.doa=doa;
        this.farmId=farmId;
        this.visitJson = visitJson;
        this.isUploaded = isUploaded;
        this.imageArray = imageArray;
        this.isOfflineAdded=isOfflineAdded;
    }

    public String getVrId() {
        return vrId;
    }

    public void setVrId(String vrId) {
        this.vrId = vrId;
    }

    public String getVisitJson() {
        return visitJson;
    }

    public void setVisitJson(String visitJson) {
        this.visitJson = visitJson;
    }

    public String isUploaded() {
        return isUploaded;
    }

    public void setUploaded(String uploaded) {
        isUploaded = uploaded;
    }

    public String getImageArray() {
        return imageArray;
    }

    public void setImageArray(String imageArray) {
        this.imageArray = imageArray;
    }

    public int getOfflineId() {
        return offlineId;
    }

    public void setOfflineId(int offlineId) {
        this.offlineId = offlineId;
    }

    public String getIsOfflineAdded() {
        return isOfflineAdded;
    }

    public void setIsOfflineAdded(String isOfflineAdded) {
        this.isOfflineAdded = isOfflineAdded;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }
}
