package com.i9930.croptrails.SubmitVisitForm.Model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

import com.i9930.croptrails.SubmitActivityForm.Model.AgriInput.AgriInput;

public class AddVisitSendDataNew {

    @SerializedName("reason_id")
    String delQId;
    @SerializedName("delq_param")
    String delQParam;
    @SerializedName("event_type")
    String eventType;
    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;
    List<AgriInput> agri_input_data;
    String  comment;
    long farm_id;
    long comp_id;
    String approved_method;
    String moisture;
    String visit_date;
    int visit_number;
    @SerializedName("effective_area1")
    float effective_area;
    String[] imageList;
    JSONObject jsonObject;
    String farm_grade;
    @SerializedName("effective_area")
    String effective_area2;

    public String getFarm_grade() {
        return farm_grade;
    }

    public void setFarm_grade(String farm_grade) {
        this.farm_grade = farm_grade;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String[] getImageList() {
        return imageList;
    }

    public void setImageList(String[] imageList) {
        this.imageList = imageList;
    }

    public void setVisit_number(int visit_number) {
        this.visit_number = visit_number;
    }

    int added_by;

    public long getFarm_id() {
        return farm_id;
    }

    public void setFarm_id(long farm_id) {
        this.farm_id = farm_id;
    }

    public long getComp_id() {
        return comp_id;
    }

    public void setComp_id(long comp_id) {
        this.comp_id = comp_id;
    }

    public String getApproved_method() {
        return approved_method;
    }

    public void setApproved_method(String approved_method) {
        this.approved_method = approved_method;
    }

    public String getVisit_date() {
        return visit_date;
    }

    public void setVisit_date(String visit_date) {
        this.visit_date = visit_date;
    }

    public int getVisit_number() {
        return visit_number;
    }

    public float getEffective_area() {
        return effective_area;
    }

    public void setEffective_area(float effective_area) {
        this.effective_area = effective_area;
    }

    public int getAdded_by() {
        return added_by;
    }

    public void setAdded_by(int added_by) {
        this.added_by = added_by;
    }

       public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEffective_area2() {
        return effective_area2;
    }

    public void setEffective_area2(String effective_area2) {
        this.effective_area2 = effective_area2;
    }

    public String getUserId() {
        return userId;
    }

    public List<AgriInput> getAgri_input_data() {
        return agri_input_data;
    }

    public void setAgri_input_data(List<AgriInput> agri_input_data) {
        this.agri_input_data = agri_input_data;
    }

    public String getToken() {
        return token;
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDelQId() {
        return delQId;
    }

    public void setDelQId(String delQId) {
        this.delQId = delQId;
    }

    public String getDelQParam() {
        return delQParam;
    }

    public void setDelQParam(String delQParam) {
        this.delQParam = delQParam;
    }
}
