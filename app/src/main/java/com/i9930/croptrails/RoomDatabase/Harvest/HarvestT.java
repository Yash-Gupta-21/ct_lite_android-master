package com.i9930.croptrails.RoomDatabase.Harvest;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "harvestT")
public class HarvestT {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int offlineId;

    @ColumnInfo(name = "farmId")
    private String farmId;


    @ColumnInfo(name = "harvest_json")
    private String harvestJson;

    @ColumnInfo(name = "offline_harvest")
    private String offlineHarvest;

    @ColumnInfo(name = "is_uploaded")
    private String isUploaded;


    public HarvestT(String farmId, String harvestJson, String offlineHarvest,String isUploaded) {
        this.farmId=farmId;
        this.isUploaded=isUploaded;
        this.harvestJson = harvestJson;
        this.offlineHarvest = offlineHarvest;
    }



    public String getHarvestJson() {
        return harvestJson;
    }

    public void setHarvestJson(String harvestJson) {
        this.harvestJson = harvestJson;
    }

    public String getOfflineHarvest() {
        return offlineHarvest;
    }

    public void setOfflineHarvest(String offlineHarvest) {
        this.offlineHarvest = offlineHarvest;
    }

    public int getOfflineId() {
        return offlineId;
    }

    public void setOfflineId(int offlineId) {
        this.offlineId = offlineId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }
}
