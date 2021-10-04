package com.i9930.croptrails.RoomDatabase.Timeline;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineInnerData;


public class CalendarJSON {
    @SerializedName("calendar")
    List<TimelineInnerData> calendarData;

    public List<TimelineInnerData> getCalendarData() {
        return calendarData;
    }

    public void setCalendarData(List<TimelineInnerData> calendarData) {
        this.calendarData = calendarData;
    }
}
