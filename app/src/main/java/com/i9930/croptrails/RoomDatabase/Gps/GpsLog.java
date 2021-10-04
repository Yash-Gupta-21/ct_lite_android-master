package com.i9930.croptrails.RoomDatabase.Gps;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="gps_log")
public class GpsLog {
   @PrimaryKey(autoGenerate = true)
   int id;
   String lat_cord;
   String long_cord;
   String sv_id;
   String time;
   String enter_date;




   public GpsLog(String lat_cord, String long_cord, String sv_id, String enter_date, String time) {
      this.lat_cord = lat_cord;
      this.long_cord = long_cord;
      this.sv_id = sv_id;
      this.enter_date = enter_date;
      this.time = time;
   }


   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getLat_cord() {
      return lat_cord;
   }

   public void setLat_cord(String lat_cord) {
      this.lat_cord = lat_cord;
   }

   public String getLong_cord() {
      return long_cord;
   }

   public void setLong_cord(String long_cord) {
      this.long_cord = long_cord;
   }

   public String getSv_id() {
      return sv_id;
   }

   public void setSv_id(String sv_id) {
      this.sv_id = sv_id;
   }

   public String getEnter_date() {
      return enter_date;
   }

   public void setEnter_date(String enter_date) {
      this.enter_date = enter_date;
   }

   public String getTime() {
      return time;
   }

   public void setTime(String time) {
      this.time = time;
   }


}
