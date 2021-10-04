package com.i9930.croptrails.RoomDatabase.DropDownData;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.RoomDatabase.Visit.Visit;

@Dao
public interface DropDownDaoInterface {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DropDownT chemicalUnit);

    /*@Query("REPLACE INTO drop_down (data_type,data) VALUES (:dataType,:data)")
    void insert(String dataType,String data);*/

    @Query("Select * from drop_down")
    Maybe<List<DropDownT>> getChemicalUnits();

    @Query("DELETE FROM drop_down")
    void deleteAllChemicals();

    @Query("DELETE FROM drop_down where data_type=:type")
    int deleteDD(String type);

    @Query("Select * from drop_down where  data_type=:data_type")
    Single<DropDownT> getFarmerOrcLusterList(String data_type);

    @Query("Select * from drop_down where data_type IN (:data_type)")
    Maybe<List<DropDownT>> getWhere(String []data_type);


    @Update
    void update(DropDownT dropDownT);

}
