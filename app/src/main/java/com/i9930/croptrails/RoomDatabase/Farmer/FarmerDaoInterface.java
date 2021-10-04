package com.i9930.croptrails.RoomDatabase.Farmer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;

@Dao
public interface FarmerDaoInterface {


    @Insert
    long insert(FarmerT farmerT);

    @Query("Select * from farmers where is_offline_added=:isOfflineAdded and is_uploaded=:isUploaded")
    Maybe<List<FarmerT>> getAllFarmerOffline(String isOfflineAdded,String isUploaded);

    @Query("Select * from farmers where is_offline_added=:isOfflineAdded and is_updated=:isUpdated")
    Maybe<List<FarmerT>> getAllFarmer(String isOfflineAdded,String isUpdated);

    @Query("DELETE FROM farmers")
    void deleteAllFarmers();

    @Query("Select * from farmers where farmerId=:farmerId")
    Single<FarmerT> getFarmerByFarmerId(String farmerId);


    @Query("UPDATE farmers SET farmerId = :farmerId,is_uploaded= :isUploaded WHERE farmerId=:farmerIdOffline")
    void update( String farmerIdOffline,String farmerId,String isUploaded);

    @Query("UPDATE farmers SET data = :data WHERE farmerId=:farmerId")
    void updateData( String farmerId,String data);


    @Update
    void update(FarmerT farm);
}
