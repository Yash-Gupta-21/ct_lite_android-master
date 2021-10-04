package com.i9930.croptrails.RoomDatabase.DoneActivities;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Maybe;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;

@Dao
public interface ActivityDaoInterface {

    @Insert
    void insert(DoneActivity doneActivity);

    @Query("DELETE FROM done_activities")
    void deleteAllActivities();

    @Query("Select * from done_activities where activity_id=:activityId")
    Maybe<DoneActivity> getActivity(String activityId);

    @Query("Select * from done_activities")
    Maybe<List<DoneActivity>> getAllActivities();
    @Update
    void update(DoneActivity doneActivity);

    @Query("SELECT COUNT(*) FROM done_activities WHERE is_offline_added = 'Y'")
    Maybe<Integer>getNumberOfRows();

    @Query("UPDATE done_activities SET farm_id = :farmId WHERE farm_id=:offlineId")
    void update( String offlineId, String farmId);
}
