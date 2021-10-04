package com.i9930.croptrails.RoomDatabase.VisitStructure;

import java.util.List;

public interface VisitNotifyInterface {

    void onVisitLoaded(List<VisitTable> visitTableList);

    void onVisitAdded();

    void onVisitInsertError(Throwable e);

    void onNoVisitAvailable();

    void visitDeleted();

    void visitDeletionFailed();

}
