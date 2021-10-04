package com.i9930.croptrails.RoomDatabase.Timeline;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.sql.Time;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface TimelineDaoInterface {
    @Insert
    void insertTimeline(Timeline... timelines);

    @Query("Select * from timeline where farm_id=:farmId")
    Single<Timeline> getTimeline(String farmId);

    @Query("DELETE FROM timeline")
    void deleteAllTimelines();


    @Update
    void updateTimeline(Timeline timeline);

    @Query("UPDATE timeline SET farm_id = :farmId WHERE farm_id=:offlineId")
    void updateTimeline( String offlineId, String farmId);
}
