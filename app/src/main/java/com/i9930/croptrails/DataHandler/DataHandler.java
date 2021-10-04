package com.i9930.croptrails.DataHandler;

import android.graphics.Bitmap;
import android.net.Uri;


import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.i9930.croptrails.CommonClasses.TimelineUnits;
import com.i9930.croptrails.CompSelect.Model.CompanyDatum;
import com.i9930.croptrails.FarmDetails.Model.timeline.TimelineInnerData;
import com.i9930.croptrails.HarvestPlan.Model.ShowHarvestPlanData;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;
import com.i9930.croptrails.RoomDatabase.Task.SvTask;
import com.i9930.croptrails.Task.Model.TaskDatum;
import com.i9930.croptrails.Test.SatusreModel.BinSet;
import com.i9930.croptrails.Test.SatusreModel.SatsureData;
import com.i9930.croptrails.Test.model.full.FarmFullDetails;

/**
 * Created by hp on 8/28/2017.
 */
public class DataHandler {
    LandingActivity landingActivity;
    TimelineInnerData timelineInnerData;
    Uri[]imageUriList;
    List<Bitmap>imageBitmapList;
    FarmFullDetails farmFullDetails;
    List<JsonObject>imageListDoc;
    JsonObject filterData;
    SvTask taskDatum;
    public List<JsonObject> getImageListDoc() {
        return imageListDoc;
    }

    public void setImageListDoc(List<JsonObject> imageListDoc) {
        this.imageListDoc = imageListDoc;
    }

    List<LatLngFarm> latLngList;

    List<LatLng> latLngsCheckArea;
    List<LatLng> latLngsFarm;

    FetchFarmResult fetchFarmResult;

    List<TimelineUnits>timelineUnits;
    String hcmid;
    ShowHarvestPlanData showHarvestPlanData;
    int harvestDataPosition;
    Bitmap myBitmapForSvProfile;
    Bitmap farmBitmap;
    List<BinSet>binSetListNdvi;
    List<BinSet>binSetListNdwi;
    List<SatsureData>satsureDataList;

    List<CompanyDatum>companyDatumList;

    List<Object>objectList;
    List<TimelineInnerData>innerDataPending;
    public FetchFarmResult getFetchFarmResult() {
        return fetchFarmResult;
    }

    public void setFetchFarmResult(FetchFarmResult fetchFarmResult) {
        this.fetchFarmResult = fetchFarmResult;
    }

    public LandingActivity getLandingActivity() {
        return landingActivity;
    }

    public void setLandingActivity(LandingActivity landingActivity) {
        this.landingActivity = landingActivity;
    }


    public List<Bitmap> getImageBitmapList() {
        return imageBitmapList;
    }

    public void setImageBitmapList(List<Bitmap> imageBitmapList) {
        this.imageBitmapList = imageBitmapList;
    }

    public List<TimelineUnits> getTimelineUnits() {
        return timelineUnits;
    }

    public void setTimelineUnits(List<TimelineUnits> timelineUnits) {
        this.timelineUnits = timelineUnits;
    }

    public TimelineInnerData getTimelineInnerData() {
        return timelineInnerData;
    }

    public void setTimelineInnerData(TimelineInnerData timelineInnerData) {
        this.timelineInnerData = timelineInnerData;
    }


    private static DataHandler datahandler=null;
    public static DataHandler newInstance(){
        if(datahandler==null){
            datahandler=new DataHandler();
        }
        return datahandler;
    }

    public Uri[] getImageUriList() {
        return imageUriList;
    }

    public void setImageUriList(Uri[] imageUriList) {
        this.imageUriList = imageUriList;
    }

    public List<LatLngFarm> getLatLngList() {
        return latLngList;
    }

    public void setLatLngList(List<LatLngFarm> latLngList) {
        this.latLngList = latLngList;
    }


    public String getHcmid() {
        return hcmid;
    }

    public void setHcmid(String hcmid) {
        this.hcmid = hcmid;
    }

    public ShowHarvestPlanData getShowHarvestPlanData() {
        return showHarvestPlanData;
    }

    public void setShowHarvestPlanData(ShowHarvestPlanData showHarvestPlanData) {
        this.showHarvestPlanData = showHarvestPlanData;
    }

    public int getHarvestDataPosition() {
        return harvestDataPosition;
    }

    public void setHarvestDataPosition(int harvestDataPosition) {
        this.harvestDataPosition = harvestDataPosition;
    }

    public Bitmap getMyBitmapForSvProfile() {
        return myBitmapForSvProfile;
    }

    public void setMyBitmapForSvProfile(Bitmap myBitmapForSvProfile) {
        this.myBitmapForSvProfile = myBitmapForSvProfile;
    }

    public Bitmap getFarmBitmap() {
        return farmBitmap;
    }

    public void setFarmBitmap(Bitmap farmBitmap) {
        this.farmBitmap = farmBitmap;
    }

    public List<BinSet> getBinSetListNdvi() {
        return binSetListNdvi;
    }

    public void setBinSetListNdvi(List<BinSet> binSetListNdvi) {
        this.binSetListNdvi = binSetListNdvi;
    }

    public List<BinSet> getBinSetListNdwi() {
        return binSetListNdwi;
    }

    public void setBinSetListNdwi(List<BinSet> binSetListNdwi) {
        this.binSetListNdwi = binSetListNdwi;
    }

    public List<SatsureData> getSatsureDataList() {
        return satsureDataList;
    }

    public void setSatsureDataList(List<SatsureData> satsureDataList) {
        this.satsureDataList = satsureDataList;
    }

    public List<CompanyDatum> getCompanyDatumList() {
        return companyDatumList;
    }

    public void setCompanyDatumList(List<CompanyDatum> companyDatumList) {
        this.companyDatumList = companyDatumList;
    }

    public List<Object> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<Object> objectList) {
        this.objectList = objectList;
    }

    public List<LatLng> getLatLngsCheckArea() {
        return latLngsCheckArea;
    }

    public void setLatLngsCheckArea(List<LatLng> latLngsCheckArea) {
        this.latLngsCheckArea = latLngsCheckArea;
    }

    public static DataHandler getDatahandler() {
        return datahandler;
    }

    public static void setDatahandler(DataHandler datahandler) {
        DataHandler.datahandler = datahandler;
    }

    public List<TimelineInnerData> getInnerDataPending() {
        return innerDataPending;
    }

    public void setInnerDataPending(List<TimelineInnerData> innerDataPending) {
        this.innerDataPending = innerDataPending;
    }

    public FarmFullDetails getFarmFullDetails() {
        return farmFullDetails;
    }

    public void setFarmFullDetails(FarmFullDetails farmFullDetails) {
        this.farmFullDetails = farmFullDetails;
    }

    public JsonObject getFilterData() {
        return filterData;
    }

    public void setFilterData(JsonObject filterData) {
        this.filterData = filterData;
    }

    public List<LatLng> getLatLngsFarm() {
        return latLngsFarm;
    }

    public void setLatLngsFarm(List<LatLng> latLngsFarm) {
        this.latLngsFarm = latLngsFarm;
    }

    public SvTask getTaskDatum() {
        return taskDatum;
    }

    public void setTaskDatum(SvTask taskDatum) {
        this.taskDatum = taskDatum;
    }
}
