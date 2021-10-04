package com.i9930.croptrails.RoomDatabase.DoneActivities;

public interface ActivityFetchListener {

    public void onActivityLoaded(DoneActivity doneActivity);
    public void onActivityLoadingFail(Throwable e);

}
