package com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel;

import com.google.android.gms.maps.model.LatLng;

public class WayPoint {
    String name;
    LatLng point;

    public WayPoint(LatLng point) {
        this.point = point;
    }

    public WayPoint(String name, LatLng point) {
        this.name = name;
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public LatLng getPoint() {
        return point;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoint(LatLng point) {
        this.point = point;
    }
}
