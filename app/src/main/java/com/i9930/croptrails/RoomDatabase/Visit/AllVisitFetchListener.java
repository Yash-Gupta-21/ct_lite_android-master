package com.i9930.croptrails.RoomDatabase.Visit;

import java.util.List;

import com.i9930.croptrails.RoomDatabase.VisitStructure.VisitTable;

public interface AllVisitFetchListener {
    public void onAllVisitLoaded(List<Visit> visitTableList);
}
