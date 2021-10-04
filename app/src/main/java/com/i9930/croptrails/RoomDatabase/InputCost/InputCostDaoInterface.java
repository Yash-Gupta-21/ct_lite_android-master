package com.i9930.croptrails.RoomDatabase.InputCost;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;

@Dao
public interface InputCostDaoInterface {

    @Insert
    void insert(InputCostT inputCostT);

    @Query("DELETE FROM input_cost")
    void deleteAllCostData();

    @Query("Select * from input_cost where farm_id=:farmId")
    Single<InputCostT> getInputCost(String farmId);

    @Update
    void update(InputCostT inputCostT);

    @Query("Select * from input_cost")
    Maybe<List<InputCostT>> getAllInputCosts();

    @Query("UPDATE input_cost SET farm_id = :farmId WHERE farm_id=:offlineId")
    void update( String offlineId, String farmId);
}
