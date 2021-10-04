package com.i9930.croptrails.RoomDatabase.Farmer;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "farmers")
public class FarmerT {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "farmerId")
    private String farmerId;

    @ColumnInfo(name = "data")
    private String data;

    @ColumnInfo(name = "is_uploaded")
    private String isUploaded;

    @ColumnInfo(name = "is_offline_added")
    private String isOfflineAdded;

    @ColumnInfo(name = "is_updated")
    private String isUpdated;


    public FarmerT(String farmerId, String data,String isUploaded,String isOfflineAdded,String isUpdated) {
        this.farmerId = farmerId;
        this.isUpdated=isUpdated;
        this.data = data;
        this.isOfflineAdded=isOfflineAdded;
        this.isUploaded=isUploaded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }

    public String getIsOfflineAdded() {
        return isOfflineAdded;
    }

    public void setIsOfflineAdded(String isOfflineAdded) {
        this.isOfflineAdded = isOfflineAdded;
    }

    public String getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(String isUpdated) {
        this.isUpdated = isUpdated;
    }
}
