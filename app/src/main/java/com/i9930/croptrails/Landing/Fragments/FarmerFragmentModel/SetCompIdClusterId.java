package com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel;

import com.google.gson.annotations.SerializedName;

public class SetCompIdClusterId {
    private String comp_id;
    private String cluster_id;
    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public void setCluster_id(String cluster_id) {
        this.cluster_id = cluster_id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
