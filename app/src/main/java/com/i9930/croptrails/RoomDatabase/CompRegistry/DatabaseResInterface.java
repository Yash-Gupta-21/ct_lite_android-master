package com.i9930.croptrails.RoomDatabase.CompRegistry;

import java.util.List;

public interface DatabaseResInterface {

    public void onGetAllCompRegData(List<CompRegModel> compRegModelList);

    void onCompRegDataAdded();

    void onAllDataDeleted();

    void onDataNotAdded();

    void onAllDataNotDeleted();

    void onUpdateCompRegData();

    void onUpdateNotSuccess();
}