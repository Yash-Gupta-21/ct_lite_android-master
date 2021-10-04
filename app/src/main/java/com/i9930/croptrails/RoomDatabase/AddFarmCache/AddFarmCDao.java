package com.i9930.croptrails.RoomDatabase.AddFarmCache;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Maybe;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegModel;
@Dao
public interface AddFarmCDao {

    @Insert
    void insertAddFarmCache(AddFarmCache... addFarmCaches);

    @Query("SELECT * FROM add_farm")
    Maybe<List<AddFarmCache>> getAllFarmCache();

    @Query("DELETE  FROM add_farm")
    void deleteAllData();

    @Update
    void updateData(AddFarmCache... compRegModel);

}
