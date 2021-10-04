package com.i9930.croptrails.RoomDatabase.DoneActivities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "done_activities")
public class DoneActivity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "activity_id")
    private String activityId;

    @SerializedName("farm_id")
    @ColumnInfo(name = "farm_id")
    private String farmId;

    @ColumnInfo(name = "activity_data")
    private String activityJson;

    @ColumnInfo(name = "is_offline_added")
    private String isOfflineAdded;

    @ColumnInfo(name = "is_uploaded")
    private String isUploaded;

    @ColumnInfo(name = "doa")
    private String doa;


    public DoneActivity(String activityId,String farmId, String activityJson, String isOfflineAdded,String doa,String isUploaded) {
        this.activityId = activityId;
        this.farmId=farmId;
        this.isUploaded=isUploaded;
        this.doa=doa;
        this.activityJson = activityJson;
        this.isOfflineAdded = isOfflineAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityJson() {
        return activityJson;
    }

    public void setActivityJson(String activityJson) {
        this.activityJson = activityJson;
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

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }
}
