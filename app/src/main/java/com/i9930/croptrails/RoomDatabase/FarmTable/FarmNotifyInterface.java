package com.i9930.croptrails.RoomDatabase.FarmTable;

import java.util.List;

public interface FarmNotifyInterface {

    void onFarmLoaded(List<Farm> farmList);

    void onFarmAdded();

    void onFarmInsertError(Throwable e);

    void onNoFarmsAvailable();

    void farmDeleted();
    void farmDeletionFaild();

}
