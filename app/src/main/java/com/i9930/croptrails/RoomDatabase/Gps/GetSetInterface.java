package com.i9930.croptrails.RoomDatabase.Gps;

import java.util.List;


public interface GetSetInterface {

    void onContactsLoaded(List<GpsLog> gpsLogList);

    void onContactsAdded();

    void onDataNotAvailable();

}
