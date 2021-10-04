package com.i9930.croptrails.Landing.Models;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Field;

public class FetchFarmSendData {
    String comp_id;
    String farm_id;
    String cluster_id;
    String offset;
    String page_items;
    String sort_by;
    String order;
    String size;
    String filter;
    String query;
    String isSelected;

    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setSort_by(String sort_by) {
        this.sort_by = sort_by;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public void setPage_items(String page_items) {
        this.page_items = page_items;
    }

    public String getCluster_id() {
        return cluster_id;
    }

    public void setCluster_id(String cluster_id) {
        this.cluster_id = cluster_id;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getFarm_id() {
        return farm_id;
    }

    public void setFarm_id(String farm_id) {
        this.farm_id = farm_id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }
}
