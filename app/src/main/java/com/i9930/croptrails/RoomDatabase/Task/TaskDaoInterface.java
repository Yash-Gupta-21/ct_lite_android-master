package com.i9930.croptrails.RoomDatabase.Task;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import com.i9930.croptrails.RoomDatabase.Visit.Visit;
@Dao
public interface TaskDaoInterface {

    @Insert
    void insertVisits(SvTask... svTasks);

    @Query("Select * from sv_task  ORDER BY date(date) ASC")
    Maybe<List<SvTask>> getAllTasks();

    @Query("Select * from sv_task where cacheId=:taskId")
    Maybe<SvTask>getTask(String taskId);

    @Query("SELECT * FROM sv_task WHERE  cacheId = (SELECT MAX(cacheId)  FROM sv_task)")
    Single<SvTask> getTask();

    @Query("DELETE FROM sv_task")
    void deleteAllTask();
    @Update
    void update(SvTask svTask);

    @Query("SELECT COUNT(cacheId) FROM sv_task WHERE type = :type")
    int getCount(int type);
/*

    @Query("UPDATE sv_task SET is_complete = :isCompleted WHERE id=:id")
    void update( String id, String isCompleted);
*/

}
