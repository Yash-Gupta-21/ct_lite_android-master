package com.i9930.croptrails.SubmitVisitForm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisitDatum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("agri_id")
    @Expose
    private String agriId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("added_by")
    @Expose
    private Object addedBy;
    @SerializedName("farm_calendar_activity_id")
    @Expose
    private Object farmCalendarActivityId;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("vr_id")
    @Expose
    private String vrId;
    @SerializedName("narration")
    @Expose
    private Object narration;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("other_agri_input")
    @Expose
    private Object otherAgriInput;
    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAgriId() {
        return agriId;
    }

    public void setAgriId(String agriId) {
        this.agriId = agriId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Object getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Object addedBy) {
        this.addedBy = addedBy;
    }

    public Object getFarmCalendarActivityId() {
        return farmCalendarActivityId;
    }

    public void setFarmCalendarActivityId(Object farmCalendarActivityId) {
        this.farmCalendarActivityId = farmCalendarActivityId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getVrId() {
        return vrId;
    }

    public void setVrId(String vrId) {
        this.vrId = vrId;
    }

    public Object getNarration() {
        return narration;
    }

    public void setNarration(Object narration) {
        this.narration = narration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public Object getOtherAgriInput() {
        return otherAgriInput;
    }

    public void setOtherAgriInput(Object otherAgriInput) {
        this.otherAgriInput = otherAgriInput;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
