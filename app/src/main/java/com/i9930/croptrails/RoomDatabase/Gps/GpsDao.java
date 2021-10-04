package com.i9930.croptrails.RoomDatabase.Gps;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Maybe;

@Dao
public interface GpsDao {
    @Insert
    void insertContact(GpsLog... gpsLog);


    @Query("SELECT * FROM gps_log")
    Maybe<List<GpsLog>> getAllContacts();

    @Query("DELETE FROM gps_log")
    void deleteAllGps();


}
