package com.i9930.croptrails.RoomDatabase.AddFarmCache;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "add_farm")
public class AddFarmCache {
    @PrimaryKey(autoGenerate = true)
    private int id;


    @ColumnInfo(name = "user_data")
    @SerializedName("userData")
    private String userData;

    public AddFarmCache( String userData) {
        this.userData = userData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }
}
