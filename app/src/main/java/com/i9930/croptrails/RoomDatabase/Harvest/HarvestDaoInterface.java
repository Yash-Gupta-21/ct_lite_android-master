package com.i9930.croptrails.RoomDatabase.Harvest;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface HarvestDaoInterface {

    @Insert
    void insertHarvest(HarvestT... harvestTS);

    @Query("Select * from HarvestT")
    Maybe<List<HarvestT>> getAllHarvest();

    @Query("Select * from harvestT where farmId=:farmId")
    Single<HarvestT> getHarvest(String farmId);

    @Query("DELETE FROM HarvestT")
    void deleteAllHarvest();

    @Update
    void update(HarvestT harvestT);

    @Query("UPDATE harvestT SET farmId = :farmId WHERE farmId=:offlineId")
    void update( String offlineId, String farmId);

}
