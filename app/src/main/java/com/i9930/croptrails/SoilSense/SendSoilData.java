package com.i9930.croptrails.SoilSense;

import java.util.List;

public class SendSoilData {
    String id;
    String doa;
    String comp_id;
    String user_id;
    String token;
    String farm_id;
    List<Data> data;

    public SendSoilData(String comp_id, String user_id, String token, String farm_id, List<Data> data) {
        this.comp_id = comp_id;
        this.data=data;
        this.user_id = user_id;
        this.token = token;
        this.farm_id = farm_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComp_id() {
        return comp_id;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFarm_id() {
        return farm_id;
    }

    public void setFarm_id(String farm_id) {
        this.farm_id = farm_id;
    }



    public static class Data{
        String latitude;
        String longitude;
        SoilDetails details;
        SoilDetails detail;
        String doa;

        public Data(String latitude, String longitude, SoilDetails details, SoilDetails detail,String doa) {
            this.latitude = latitude;
            this.doa=doa;
            this.longitude = longitude;
//            this.details = details;
            this.detail = detail;
        }

        public String getDoa() {
            return doa;
        }

        public void setDoa(String doa) {
            this.doa = doa;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public SoilDetails getDetails() {
            return details;
        }

        public void setDetails(SoilDetails details) {
            this.details = details;
        }

        public SoilDetails getDetail() {
            return detail;
        }

        public void setDetail(SoilDetails detail) {
            this.detail = detail;
        }
    }
}
