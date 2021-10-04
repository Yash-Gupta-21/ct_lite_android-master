package com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model;

import com.google.android.gms.maps.model.LatLng;

public class Place{
    //public String name;
    public LatLng latlng;

    public Place(LatLng latlng) {
        this.latlng = latlng;
    }
}