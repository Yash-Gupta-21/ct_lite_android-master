package com.i9930.croptrails.RoomDatabase.VisitStructure;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Maybe;

@Dao
public interface VisitDaoInterface {

    @Insert
    void insertVisits(VisitTable... visitTables);

    @Query("Select * from visit_structure")
    Maybe<List<VisitTable>> getVisits();

    @Query("Select * from visit_structure where id=1")
    Maybe<List<VisitTable>> getVisitStructure();

    @Query("DELETE FROM visit_structure")
    void deleteAllVisits();
}
