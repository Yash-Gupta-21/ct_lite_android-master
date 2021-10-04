package com.i9930.croptrails.Landing.Models.ad;

import com.i9930.croptrails.Landing.Models.FetchFarmResult;
import com.i9930.croptrails.Landing.Models.NewData.FetchFarmResultNew;

public class AdFarmDatum {
    int type;
    FetchFarmResultNew fetchFarmResultNew;
    FetchFarmResult fetchFarmResult;

    public AdFarmDatum(int type) {
        this.type = type;
    }

    public AdFarmDatum(int type, FetchFarmResultNew fetchFarmResultNew) {
        this.type = type;
        this.fetchFarmResultNew = fetchFarmResultNew;
    }

    public AdFarmDatum(int type, FetchFarmResult fetchFarmResult) {
        this.type = type;
        this.fetchFarmResult = fetchFarmResult;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public FetchFarmResultNew getFetchFarmResultNew() {
        return fetchFarmResultNew;
    }

    public void setFetchFarmResultNew(FetchFarmResultNew fetchFarmResultNew) {
        this.fetchFarmResultNew = fetchFarmResultNew;
    }

    public FetchFarmResult getFetchFarmResult() {
        return fetchFarmResult;
    }

    public void setFetchFarmResult(FetchFarmResult fetchFarmResult) {
        this.fetchFarmResult = fetchFarmResult;
    }
}
