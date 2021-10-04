package com.i9930.croptrails.SubmitActivityForm.Model.DoneActNew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.FarmDetails.Model.Activity.DoneActivityImage;
import com.i9930.croptrails.SubmitActivityForm.Model.AgriInput.AgriInput;

public class DoneActivityResponseNew {

    @SerializedName("data")
    @Expose
    private List<DoneActInputs> data = null;
    @SerializedName("images")
    @Expose
    private List<DoneActivityImage> images = null;
    @SerializedName("farmer_reply")
    @Expose
    private String textReply;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("status")
    @Expose
    private Integer status;
    public List<DoneActInputs> getData() {
        return data;
    }
    @SerializedName("grade")
    String grade;

    @SerializedName("visit_date")
    String visitDate;

    @SerializedName("moisture")
    String moisture;
    @SerializedName("comment")
    String comment;
    @SerializedName("activity_done_date")
    String activityDoneDate;
    @SerializedName("farm_id")
    @Expose
    private String farmId;

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("farm_cal_act_id")
    @Expose
    private String farmCalActivityId;

    @SerializedName("data1")
    @Expose
    private List<AgriInput> agriInputs = null;
    @SerializedName("data2")
    @Expose
    private List<DoneActivityImage> doneActivityImageList = null;
    @SerializedName("visit_number")
    @Expose
    private String visitNumber;
    String doa;
    String lat="";
    String lon="";
    String [] img_list;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public List<AgriInput> getAgriInputs() {
        return agriInputs;
    }

    public void setAgriInputs(List<AgriInput> agriInputs) {
        this.agriInputs = agriInputs;
    }

    public List<DoneActivityImage> getDoneActivityImageList() {
        return doneActivityImageList;
    }

    public void setDoneActivityImageList(List<DoneActivityImage> doneActivityImageList) {
        this.doneActivityImageList = doneActivityImageList;
    }

    public String getTextReply() {
        return textReply;
    }

    public void setTextReply(String textReply) {
        this.textReply = textReply;
    }

    public String getFarmCalActivityId() {
        return farmCalActivityId;
    }

    public void setFarmCalActivityId(String farmCalActivityId) {
        this.farmCalActivityId = farmCalActivityId;
    }

    public String[] getImg_list() {
        return img_list;
    }

    public void setImg_list(String[] img_list) {
        this.img_list = img_list;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getActivityDoneDate() {
        return activityDoneDate;
    }

    public void setActivityDoneDate(String activityDoneDate) {
        this.activityDoneDate = activityDoneDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }


    public void setData(List<DoneActInputs> data) {
        this.data = data;
    }

    public List<DoneActivityImage> getImages() {
        return images;
    }

    public void setImages(List<DoneActivityImage> images) {
        this.images = images;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMoisture() {
        return moisture;
    }

    public void setMoisture(String moisture) {
        this.moisture = moisture;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }
}
