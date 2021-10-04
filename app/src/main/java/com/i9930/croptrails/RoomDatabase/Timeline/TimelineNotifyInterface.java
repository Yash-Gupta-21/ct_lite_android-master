package com.i9930.croptrails.RoomDatabase.Timeline;


public interface TimelineNotifyInterface {
    void onTimelineLoaded(Timeline timelineList);

    void onTimelineAdded();

    void onTimelineInsertError(Throwable e);

    void onNoFarmsAvailable();

    void TimelineDeleted();
    void TimelineDeletionFaild();
}
