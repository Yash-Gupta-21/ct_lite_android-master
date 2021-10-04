package com.i9930.croptrails.SubmitInputCost.Model;

import com.google.gson.annotations.SerializedName;

public class InputCostSubmitDatum {
    @SerializedName("comp_id")
    String compId;
    @SerializedName("farm_id")
    String farmId;
    @SerializedName("cost_type_id")
    String costTypeId;
    @SerializedName("other_type_name")
    String otherTypeName;
    @SerializedName("expense")
    String expenseAmount;
    @SerializedName("expense_date")
    String expenseDate;
    @SerializedName("added_by")
    String addedBy;

    @SerializedName("doa")
    String doaOffline;

    String name;

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
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

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoaOffline() {
        return doaOffline;
    }

    public void setDoaOffline(String doaOffline) {
        this.doaOffline = doaOffline;
    }

    public InputCostSubmitDatum() {
    }

    public InputCostSubmitDatum(String compId, String farmId, String costTypeId,
                                String otherTypeName, String expenseAmount,
                                String expenseDate, String addedBy, String doaOffline, String name) {
        this.compId = compId;
        this.farmId = farmId;
        this.costTypeId = costTypeId;
        this.otherTypeName = otherTypeName;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;
        this.addedBy = addedBy;
        this.doaOffline = doaOffline;
        this.name = name;
    }
}
