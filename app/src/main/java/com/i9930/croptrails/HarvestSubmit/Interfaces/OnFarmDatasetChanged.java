package com.i9930.croptrails.HarvestSubmit.Interfaces;

import android.widget.EditText;

import java.util.List;

import com.i9930.croptrails.AddFarm.Model.FarmData;

public interface OnFarmDatasetChanged {

    public void isLastItemRemoved(boolean isLatsItem, int position);

    public void onDataSetChanged(List<FarmData>farmData);

    public void onOwnerShipImageClick(int index, EditText editText);
    public void onFarmImageClick(int index,EditText editText);



}
