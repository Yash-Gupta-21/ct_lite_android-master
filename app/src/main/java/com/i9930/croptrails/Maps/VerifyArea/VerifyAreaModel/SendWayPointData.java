package com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel;

import java.util.List;

public class SendWayPointData {

    String user_id,token,comp_id,farm_id,type,name;
    List<WayPoint>details;

    public SendWayPointData(String user_id, String token, String comp_id, String farm_id, String type, List<WayPoint> details) {
        this.user_id = user_id;
        this.token = token;
        this.comp_id = comp_id;
        this.farm_id = farm_id;
        this.type = type;
        this.details = details;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getToken() {
        return token;
    }

    public String getComp_id() {
        return comp_id;
    }

    public String getFarm_id() {
        return farm_id;
    }

    public String getType() {
        return type;
    }

    public List<WayPoint> getDetails() {
        return details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
