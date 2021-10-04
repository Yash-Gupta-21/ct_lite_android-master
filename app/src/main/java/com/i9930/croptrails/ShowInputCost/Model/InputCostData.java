package com.i9930.croptrails.ShowInputCost.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InputCostData {
    @SerializedName("compare_type")
    String typeC;
    int index;

    @SerializedName("farmId")
    @Expose
    private String farmId;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("farm_master_num")
    @Expose
    private String farmMasterNum;
    @SerializedName("cost_type_id")
    @Expose
    private String costTypeId;
    @SerializedName("other_type_name")
    @Expose
    private String otherTypeName;
    @SerializedName("expense")
    @Expose
    private String expense;
    @SerializedName("expense_date")
    @Expose
    private String expenseDate;
    @SerializedName("added_by")
    @Expose
    private String addedBy;
    @SerializedName("added_on")
    @Expose
    private String addedOn;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("activity_type_id")
    @Expose
    private String activityTypeId;
    @SerializedName("other_activity_name")
    @Expose
    private String otherActivityName;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("doa")
    @Expose
    private String doa;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getFarmMasterNum() {
        return farmMasterNum;
    }

    public void setFarmMasterNum(String farmMasterNum) {
        this.farmMasterNum = farmMasterNum;
    }

    public String getCostTypeId() {
        return costTypeId;
    }

    public void setCostTypeId(String costTypeId) {
        this.costTypeId = costTypeId;
    }

    public String getOtherTypeName() {
        return otherTypeName;
    }

    public void setOtherTypeName(String otherTypeName) {
        this.otherTypeName = otherTypeName;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActivityTypeId() {
        return activityTypeId;
    }

    public void setActivityTypeId(String activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public String getOtherActivityName() {
        return otherActivityName;
    }

    public void setOtherActivityName(String otherActivityName) {
        this.otherActivityName = otherActivityName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getTypeC() {
        return typeC;
    }

    public void setTypeC(String typeC) {
        this.typeC = typeC;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }
}
