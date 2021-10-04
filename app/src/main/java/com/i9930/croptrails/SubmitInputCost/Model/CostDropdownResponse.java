package com.i9930.croptrails.SubmitInputCost.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CostDropdownResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("expense")
    @Expose
    private List<ExpenseDataDD> expenseDataDDList = null;
    @SerializedName("resource")
    @Expose
    private List<ResourceDataDD> resourceDataDDList = null;

    public List<ExpenseDataDD> getExpenseDataDDList() {
        return expenseDataDDList;
    }

    public void setExpenseDataDDList(List<ExpenseDataDD> expenseDataDDList) {
        this.expenseDataDDList = expenseDataDDList;
    }

    public List<ResourceDataDD> getResourceDataDDList() {
        return resourceDataDDList;
    }

    public void setResourceDataDDList(List<ResourceDataDD> resourceDataDDList) {
        this.resourceDataDDList = resourceDataDDList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
