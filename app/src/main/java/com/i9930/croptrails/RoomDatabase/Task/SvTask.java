package com.i9930.croptrails.RoomDatabase.Task;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "sv_task")
public class SvTask {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cacheId")
    private int cacheId;

    @SerializedName("id")
    @ColumnInfo(name = "type")
    private int type;

    @SerializedName("date")
    @ColumnInfo(name = "date")
    private String date;

    @SerializedName("data")
    @ColumnInfo(name = "data")
    private String data;

    @SerializedName("sync_date")
    @ColumnInfo(name = "sync_date")
    private String syncDate;

    @SerializedName("is_complete")
    @ColumnInfo(name = "is_complete")
    private String isComplete;
    @SerializedName("complete_date")
    @ColumnInfo(name = "complete_date")
    private String completeDate;

    @SerializedName("comments")
    @ColumnInfo(name = "comments")
    private String comments;

    public SvTask(int type, String date, String data,String syncDate,
                  String isComplete, String completeDate,String comments) {
        this.type = type;
        this.date = date;
        this.data = data;
        this.isComplete = isComplete;
        this.completeDate = completeDate;
        this.comments = comments;
        this.syncDate = syncDate;
    }

    public int getCacheId() {
        return cacheId;
    }

    public void setCacheId(int cacheId) {
        this.cacheId = cacheId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSyncDate() {
        return syncDate;
    }

    public void setSyncDate(String syncDate) {
        this.syncDate = syncDate;
    }

    public String getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
