package com.i9930.croptrails.RoomDatabase.FarmTable;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.i9930.croptrails.RoomDatabase.Farmer.FarmerT;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface FarmDaoInterface {

    @Insert
    long insertFarm(Farm farms);

    @Query("Select * from farm")
    Maybe<List<Farm>> getFarms();

    @Query("DELETE FROM farm")
    void deleteAllFarms();

    @Query("Select * from farm where farm_id=:farmId")
    Maybe<Farm> getFarm(String farmId);

    @Query("Select * from farm where (farmer_id=:farmerId AND is_offline_added=:isOfflineAdded AND isUpdated=:isUpdated) ")
    Maybe<List<Farm>> getFarmerFarms(String farmerId,String isOfflineAdded,String isUpdated);


    @Query("Select * from farm where is_offline_added=:isExistingFarm AND is_uploaded=:isUploaded AND is_offline_added=:isOfflineAdded")
    Maybe<List<Farm>> getExistingFarmerFarm(String isExistingFarm,String isUploaded,String isOfflineAdded);

    @Update
    void update(Farm farm);

    @Query("UPDATE farm SET farm_id = :farmId,is_uploaded='Y',is_offline_added = :isOfflineAdded,farmer_id = :ownerId WHERE farm_id=:offlineId")
    void update( String offlineId, String farmId,String isOfflineAdded,String ownerId);

    @Query("UPDATE farm SET farm_id = :farmId,is_uploaded = :isUploaded,isExistingFarm=:isExistingFarm WHERE farm_id=:offlineId")
    void updateS( String offlineId, String farmId,String isUploaded,String isExistingFarm);


    @Query("Select * from farm where farmer_id=:farmerId")
    Single<Farm> getFarmByFarmerId(String farmerId);


}
