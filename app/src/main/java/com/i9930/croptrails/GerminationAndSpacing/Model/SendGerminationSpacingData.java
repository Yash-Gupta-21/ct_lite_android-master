package com.i9930.croptrails.GerminationAndSpacing.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 20-07-2018.
 */

public class SendGerminationSpacingData {
    String[] configured_area;
    String[] spacing_ptp;
    String[] spacing_rtr;
    String[] ideal_pop;
    String[] actual_pop;
    String[] actual_total_population;

    String added_by;
    String avg_germination;
    String avg_population;
    String avg_spacing_rtr;
    String avg_spacing_ptp;
    String farm_id;

    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;

    public String getFarm_id() {
        return farm_id;
    }

    public void setFarm_id(String farm_id) {
        this.farm_id = farm_id;
    }

    public String[] getConfigured_area() {
        return configured_area;
    }

    public void setConfigured_area(String[] configured_area) {
        this.configured_area = configured_area;
    }

    public String[] getSpacing_ptp() {
        return spacing_ptp;
    }

    public void setSpacing_ptp(String[] spacing_ptp) {
        this.spacing_ptp = spacing_ptp;
    }

    public String[] getSpacing_rtr() {
        return spacing_rtr;
    }

    public void setSpacing_rtr(String[] spacing_rtr) {
        this.spacing_rtr = spacing_rtr;
    }

    public String[] getIdeal_pop() {
        return ideal_pop;
    }

    public void setIdeal_pop(String[] ideal_pop) {
        this.ideal_pop = ideal_pop;
    }

    public String[] getActual_pop() {
        return actual_pop;
    }

    public void setActual_pop(String[] actual_pop) {
        this.actual_pop = actual_pop;
    }

    public String[] getActual_total_population() {
        return actual_total_population;
    }

    public void setActual_total_population(String[] actual_total_population) {
        this.actual_total_population = actual_total_population;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getAvg_germination() {
        return avg_germination;
    }

    public void setAvg_germination(String avg_germination) {
        this.avg_germination = avg_germination;
    }

    public String getAvg_population() {
        return avg_population;
    }

    public void setAvg_population(String avg_population) {
        this.avg_population = avg_population;
    }

    public String getAvg_spacing_rtr() {
        return avg_spacing_rtr;
    }

    public void setAvg_spacing_rtr(String avg_spacing_rtr) {
        this.avg_spacing_rtr = avg_spacing_rtr;
    }

    public String getAvg_spacing_ptp() {
        return avg_spacing_ptp;
    }

    public void setAvg_spacing_ptp(String avg_spacing_ptp) {
        this.avg_spacing_ptp = avg_spacing_ptp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
