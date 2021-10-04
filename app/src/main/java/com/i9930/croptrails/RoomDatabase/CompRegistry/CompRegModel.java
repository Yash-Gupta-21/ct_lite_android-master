package com.i9930.croptrails.RoomDatabase.CompRegistry;



import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comp_registry")
public class CompRegModel {
    @PrimaryKey(autoGenerate = true)
    int id ;
    String feature;
    String data;
    String date;

    public CompRegModel( String data, String date,String feature) {
        this.data = data;
        this.feature=feature;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }
}
