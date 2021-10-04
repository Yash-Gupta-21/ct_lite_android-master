package com.i9930.croptrails.ServiceAndBroadcasts.Model;

/**
 * Created by hp on 23-07-2018.
 */

public class SaveGpsGetterSetter {
String lati_cord,longi_cord,enter_date;
int id;
String sv_id;
String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public SaveGpsGetterSetter(int id, String lati_cord, String longi_cord, String sv_id, String enter_date) {
        this.sv_id=sv_id;
        this.lati_cord=lati_cord;
        this.longi_cord=longi_cord;
        this.enter_date=enter_date;
        this.id=id;
    }

    public SaveGpsGetterSetter(int id, String lati_cord, String longi_cord, String sv_id,String time, String enter_date) {
        this.sv_id=sv_id;
        this.lati_cord=lati_cord;
        this.longi_cord=longi_cord;
        this.enter_date=enter_date;
        this.id=id;
        this.time=time;
    }

    public SaveGpsGetterSetter(String lati_cord, String longi_cord, String sv_id,String time, String enter_date) {
        this.sv_id=sv_id;
        this.lati_cord=lati_cord;
        this.longi_cord=longi_cord;
        this.enter_date=enter_date;
        this.time=time;
    }


    public String getSv_id() {
        return sv_id;
    }

    public void setSv_id(String sv_id) {
        this.sv_id = sv_id;
    }

    public SaveGpsGetterSetter(String lati_cord, String longi_cord,String sv_id, String enter_date) {
        this.sv_id=sv_id;
        this.lati_cord=lati_cord;
        this.longi_cord=longi_cord;
        this.enter_date=enter_date;
    }

    public SaveGpsGetterSetter() {

    }

    public SaveGpsGetterSetter(String lati_cord, String longi_cord, String formattedDate) {
        this.lati_cord=lati_cord;
        this.longi_cord=longi_cord;
        this.enter_date=formattedDate;
    }

    public String getLati_cord() {
        return lati_cord;
    }

    public void setLati_cord(String lati_cord) {
        this.lati_cord = lati_cord;
    }

    public String getLongi_cord() {
        return longi_cord;
    }

    public void setLongi_cord(String longi_cord) {
        this.longi_cord = longi_cord;
    }

    public String getEnter_date() {
        return enter_date;
    }

    public void setEnter_date(String enter_date) {
        this.enter_date = enter_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
