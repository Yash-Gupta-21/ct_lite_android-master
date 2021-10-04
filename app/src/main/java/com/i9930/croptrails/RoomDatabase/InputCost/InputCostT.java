package com.i9930.croptrails.RoomDatabase.InputCost;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "input_cost")
public class InputCostT {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "farm_id")
    private String farmId;

    @ColumnInfo(name = "input_cost_data")
    private String inputCostData;

    @ColumnInfo(name = "input_cost_data_offline")
    private String inputCostJsonOfflineAdded;

    @ColumnInfo(name = "is_uploaded")
    private String isUploaded;


    public InputCostT(String farmId, String inputCostData, String inputCostJsonOfflineAdded,String isUploaded) {
        this.farmId = farmId;
        this.inputCostData = inputCostData;
        this.isUploaded=isUploaded;
        this.inputCostJsonOfflineAdded = inputCostJsonOfflineAdded;
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

    public String getInputCostData() {
        return inputCostData;
    }

    public void setInputCostData(String inputCostData) {
        this.inputCostData = inputCostData;
    }

    public String getInputCostJsonOfflineAdded() {
        return inputCostJsonOfflineAdded;
    }

    public void setInputCostJsonOfflineAdded(String inputCostJsonOfflineAdded) {
        this.inputCostJsonOfflineAdded = inputCostJsonOfflineAdded;
    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }
}
