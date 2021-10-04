package com.i9930.croptrails.RoomDatabase.DropDownData2;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface DropDownDaoInterface2 {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DropDownT2 chemicalUnit);

    /*@Query("REPLACE INTO drop_down (data_type,data) VALUES (:dataType,:data)")
    void insert(String dataType,String data);*/

    @Query("Select * from drop_down_new")
    Maybe<List<DropDownT2>> getChemicalUnits();

    @Query("DELETE FROM drop_down_new")
    void deleteAllChemicals();

    @Query("DELETE FROM drop_down_new where data_type=:type")
    int deleteDD(String type);

    @Query("Select * from drop_down_new where  data_type=:data_type")
    Single<DropDownT2> getFarmerOrcLusterList(String data_type);

    @Query("Select * from drop_down_new where data_type IN (:data_type)")
    Maybe<List<DropDownT2>> getWhere(String[] data_type);


    @Update
    void update(DropDownT2 dropDownT);

}
