package com.i9930.croptrails.Maps.ShowArea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import com.i9930.croptrails.Maps.ShowArea.Model.near.NearBy;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.AppConstants;

public class CustomMarkerInfoWindowView implements GoogleMap.InfoWindowAdapter {
        private final View markerItemView;
        public CustomMarkerInfoWindowView(Context context) {
            markerItemView = LayoutInflater.from(context).inflate(R.layout.marker_info_window, null);  // 1
        }
        @Override
        public View getInfoWindow(Marker marker) { // 2
            NearBy user = (NearBy) marker.getTag();  // 3
            if (user == null) return markerItemView;
            TextView itemNameTextView = markerItemView.findViewById(R.id.farmerNameTv);
            TextView farmIdTv = markerItemView.findViewById(R.id.farmIdTv);
            TextView addressTv = markerItemView.findViewById(R.id.addressTv);
            itemNameTextView.setText(user.getFarmerName());
            farmIdTv.setText(user.getFarmName());

            String address="N/A";
            if (AppConstants.isValidString(user.getAddLine1()))
                address=user.getAddLine1();
            if (AppConstants.isValidString(user.getAddLine2())){
                if (address.trim().equalsIgnoreCase("N/A")){
                    address=user.getAddLine2();
                }else{
                    address=address+", "+user.getAddLine2();
                }
            }

            addressTv.setText(address);
//            itemAddressTextView.setText(user.getAddress());
            return markerItemView;  // 4
        }
        @Override
        public View getInfoContents(Marker marker) {
            NearBy user = (NearBy) marker.getTag();  // 3
            if (user == null) return markerItemView;
            TextView itemNameTextView = markerItemView.findViewById(R.id.farmerNameTv);
//            TextView itemAddressTextView = markerItemView.findViewById(R.id.itemAddressTextView);
            itemNameTextView.setText(user.getFarmerName());
//            itemAddressTextView.setText(user.getAddress());
            return markerItemView;  // 4
        }
    }