package com.i9930.croptrails.RoomDatabase.Visit;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import io.reactivex.Maybe;
import com.i9930.croptrails.RoomDatabase.DoneActivities.DoneActivity;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestT;

@Dao
public interface VrDaoInterface {


    @Insert
    void insertVisits(Visit... visits);

    @Query("Select * from visit")
    Maybe<List<Visit>> getAllVisits();

    @Query("Select * from visit where vrId=:vrId")
    Maybe<Visit>getVisit(String vrId);

    @Query("DELETE FROM visit")
    void deleteAllVisits();

    @Query("SELECT * FROM visit ORDER BY vrId DESC LIMIT 1")
    Maybe<Visit>getLastVisit();

    @Query("SELECT COUNT(*) FROM visit WHERE is_offline_added = 'Y'")
    Maybe<Integer>getNumberOfRows();

    @Update
    void update(Visit visit);


    @Query("UPDATE visit SET farmId = :farmId WHERE farmId=:id")
    void update( String id, String farmId);

}
