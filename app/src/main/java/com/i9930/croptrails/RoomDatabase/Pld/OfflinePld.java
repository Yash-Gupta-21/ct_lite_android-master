package com.i9930.croptrails.RoomDatabase.Pld;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "pld")
public class OfflinePld {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "farm_id")
    @SerializedName("farm_id")
    private String farmId;


    @ColumnInfo(name = "data")
    @SerializedName("data")
    private String pldData;

    @ColumnInfo(name = "isOfflineAdded")
    @SerializedName("isOfflineAdded")
    private String isOfflineAdded;

    @ColumnInfo(name = "isUploaded")
    @SerializedName("isUploaded")
    private String isUploaded;

    @ColumnInfo(name = "offlineDateTime")
    @SerializedName("offlineDateTime")
    private String offlineDateTime;

    public OfflinePld(String farmId, String pldData, String isOfflineAdded, String isUploaded,String offlineDateTime) {
        this.farmId = farmId;
        this.offlineDateTime=offlineDateTime;
        this.pldData = pldData;
        this.isOfflineAdded = isOfflineAdded;
        this.isUploaded = isUploaded;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getPldData() {
        return pldData;
    }

    public void setPldData(String pldData) {
        this.pldData = pldData;
    }

    public String getIsOfflineAdded() {
        return isOfflineAdded;
    }

    public void setIsOfflineAdded(String isOfflineAdded) {
        this.isOfflineAdded = isOfflineAdded;
    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOfflineDateTime() {
        return offlineDateTime;
    }

    public void setOfflineDateTime(String offlineDateTime) {
        this.offlineDateTime = offlineDateTime;
    }
}
