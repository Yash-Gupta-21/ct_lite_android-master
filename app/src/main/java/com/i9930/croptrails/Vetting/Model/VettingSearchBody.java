package com.i9930.croptrails.Vetting.Model;

public class VettingSearchBody {

    public interface FILTER{
        public static final String SELECTED="selected";
        public static final String REJECTED="rejected";
        public static final String PENDING="fresh";
        public static final String DATA_ENTRY="dataEntry";
    }

    String user_id;
    String token  ;
    String comp_id ;
    String filter ;//'selected'   'rejected'     'fresh'      'dataEntry'
    String query;
    String cluster_id;

    public VettingSearchBody(String user_id, String token, String comp_id, String filter, String query, String cluster_id) {
        this.user_id = user_id;
        this.token = token;
        this.comp_id = comp_id;
        this.filter = filter;
        this.query = query;
        this.cluster_id = cluster_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setCluster_id(String cluster_id) {
        this.cluster_id = cluster_id;
    }
}
