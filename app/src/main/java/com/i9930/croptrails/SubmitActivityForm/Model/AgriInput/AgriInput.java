package com.i9930.croptrails.SubmitActivityForm.Model.AgriInput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import com.i9930.croptrails.SubmitActivityForm.Model.CompAgriDatum;

public class AgriInput {
    List<CompAgriDatum> compAgriDatumList;
    boolean isManualAdded;
    int selectedIndex=0;

    @SerializedName("input_id")
    @Expose
    private String inputId;

    @SerializedName("used_quantity")
    @Expose
    private String usedQuantity;

    @SerializedName("cost")
    @Expose
    private String cost;

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
    private String addedBy;
    @SerializedName("farm_calendar_activity_id")
    @Expose
    private String farmCalendarActivityId;
    @SerializedName("is_active")
    @Expose
    private String isActive;
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

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getFarmCalendarActivityId() {
        return farmCalendarActivityId;
    }

    public void setFarmCalendarActivityId(String farmCalendarActivityId) {
        this.farmCalendarActivityId = farmCalendarActivityId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

        public AgriInput(String id, String quantity, String agriId, String amount, String addedBy, String farmCalendarActivityId, String isActive, String name) {
        this.id = id;
        this.quantity = quantity;
        this.agriId = agriId;
        this.amount = amount;
        this.addedBy = addedBy;
        this.farmCalendarActivityId = farmCalendarActivityId;
        this.isActive = isActive;
        this.name = name;
    }

    public boolean isManualAdded() {
        return isManualAdded;
    }

    public void setManualAdded(boolean manualAdded) {
        isManualAdded = manualAdded;
    }

    public String getUsedQuantity() {
        return usedQuantity;
    }

    public void setUsedQuantity(String usedQuantity) {
        this.usedQuantity = usedQuantity;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public List<CompAgriDatum> getCompAgriDatumList() {
        return compAgriDatumList;
    }

    public void setCompAgriDatumList(List<CompAgriDatum> compAgriDatumList) {
        this.compAgriDatumList = compAgriDatumList;
    }

    public AgriInput() {
    }

    public String getInputId() {
        return inputId;
    }

    public void setInputId(String inputId) {
        this.inputId = inputId;
    }
}
