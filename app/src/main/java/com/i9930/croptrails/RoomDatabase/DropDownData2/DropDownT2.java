package com.i9930.croptrails.RoomDatabase.DropDownData2;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "drop_down_new",
        indices = {@Index(value = {"data_type"},
                unique = true)})
public class DropDownT2 {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "data_type")
    private String dataType;

    @ColumnInfo(name = "data")
    private String data;


    public DropDownT2(String dataType, String data) {
        this.dataType = dataType;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
