package com.i9930.croptrails.RoomDatabase.CompRegistry;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Maybe;
import io.reactivex.Single;
import com.i9930.croptrails.RoomDatabase.Timeline.Timeline;

@Dao
public interface CompRegDao {
    @Insert
    void insertCompRegData(CompRegModel... compRegModels);


    @Query("SELECT * FROM comp_registry")
    Maybe<List<CompRegModel>> getAllCompRegData();

    @Query("DELETE  FROM comp_registry")
    void deleteAllData();

    @Update
    void updateData(CompRegModel... compRegModel);

    @Query("Select * from comp_registry where feature=:feature")
    Single<CompRegModel> getFeature(String feature);

}
