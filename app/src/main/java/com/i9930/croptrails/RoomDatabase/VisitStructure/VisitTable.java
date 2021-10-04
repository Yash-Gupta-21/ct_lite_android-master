package com.i9930.croptrails.RoomDatabase.VisitStructure;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "visit_structure")
public class VisitTable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int offlineId;

    @ColumnInfo(name = "visit_structure")
    private String visitJson;

    @ColumnInfo(name = "is_uploaded")
    private boolean isUploaded;

    @ColumnInfo(name = "imageArray")
    private String imageArray;

    public VisitTable(String visitJson, boolean isUploaded) {
        this.visitJson = visitJson;
        this.isUploaded = isUploaded;
    }

    public int getOfflineId() {
        return offlineId;
    }

    public void setOfflineId(int offlineId) {
        this.offlineId = offlineId;
    }

    public String getVisitJson() {
        return visitJson;
    }

    public void setVisitJson(String visitJson) {
        this.visitJson = visitJson;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public String getImageArray() {
        return imageArray;
    }

    public void setImageArray(String imageArray) {
        this.imageArray = imageArray;
    }
}
