package com.i9930.croptrails.RoomDatabase.ResourceCost;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;

@Dao
public interface ResourceDaoInterface {

    @Insert
    void insert(ResourceCostT resourceCost);


    @Query("DELETE FROM resource_cost")
    void deleteAllResourceData();

    @Query("Select * from resource_cost where farm_id=:farmId")
    Single<ResourceCostT> getResources(String farmId);

    @Update
    void update(ResourceCostT resourceCost);

    @Query("Select * from resource_cost")
    Maybe<List<ResourceCostT>> getResources();

    @Query("UPDATE resource_cost SET farm_id = :farmId WHERE farm_id=:offlineId")
    void update( String offlineId, String farmId);
}
