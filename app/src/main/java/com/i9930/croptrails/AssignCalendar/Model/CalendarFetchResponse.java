package com.i9930.croptrails.AssignCalendar.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CalendarFetchResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private List<CalendarDatum> calendarDatumList;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<CalendarDatum> getCalendarDatumList() {
        return calendarDatumList;
    }

    public void setCalendarDatumList(List<CalendarDatum> calendarDatumList) {
        this.calendarDatumList = calendarDatumList;
    }
}
