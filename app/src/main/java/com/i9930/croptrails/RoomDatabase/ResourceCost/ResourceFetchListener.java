package com.i9930.croptrails.RoomDatabase.ResourceCost;

import java.util.List;

public interface ResourceFetchListener {

    public void onResourceLoaded(ResourceCostT resourceCost);

    public void onResourceUpdated();

}
