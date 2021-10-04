package com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class OmitanceDetails {
List<LatLng>latLngList;
String area;

    public OmitanceDetails(List<LatLng> latLngList, String area) {
        this.latLngList = latLngList;
        this.area = area;
    }

    public List<LatLng> getLatLngList() {
        return latLngList;
    }

    public String getArea() {
        return area;
    }
}
