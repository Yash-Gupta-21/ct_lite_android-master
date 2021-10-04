package com.i9930.croptrails.RoomDatabase.Pld;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Maybe;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;

@Dao
public interface PldDaoInterface {

    @Insert
    long insertPld(OfflinePld pld);

    @Query("Select * from pld")
    Maybe<List<OfflinePld>> getPldList();

    @Query("DELETE FROM pld")
    void deleteAllPld();

    @Query("Select * from pld where farm_id=:farmId")
    Maybe<OfflinePld> getPld(String farmId);

    @Query("Select * from pld where isOfflineAdded=:isOfflineAdded")
    Maybe<List<OfflinePld>> getOfflineAddedPld(String isOfflineAdded);

    @Query("Select * from pld where isUploaded=:isUploaded")
    Maybe<List<OfflinePld>> getNotUploadedPld(String isUploaded);

    @Update
    void update(OfflinePld farm);

}