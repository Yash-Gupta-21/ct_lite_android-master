package com.i9930.croptrails.RoomDatabase.ResourceCost;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "resource_cost")
public class ResourceCostT {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "farm_id")
    private String farmId;

    @ColumnInfo(name = "resource_data")
    private String resourceJson;

    @ColumnInfo(name = "resource_data_offline")
    private String resourceJsonOffline;

    @ColumnInfo(name = "is_uploaded")
    private String isUploaded;


    public ResourceCostT(String farmId, String resourceJson, String resourceJsonOffline,String isUploaded) {
        this.farmId = farmId;
        this.resourceJson = resourceJson;
        this.resourceJsonOffline = resourceJsonOffline;
        this.isUploaded=isUploaded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getResourceJson() {
        return resourceJson;
    }

    public void setResourceJson(String resourceJson) {
        this.resourceJson = resourceJson;
    }

    public String getResourceJsonOffline() {
        return resourceJsonOffline;
    }

    public void setResourceJsonOffline(String resourceJsonOffline) {
        this.resourceJsonOffline = resourceJsonOffline;
    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }
}
