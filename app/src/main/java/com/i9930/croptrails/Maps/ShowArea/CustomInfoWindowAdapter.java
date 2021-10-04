package com.i9930.croptrails.Maps.ShowArea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import com.i9930.croptrails.Maps.ShowArea.Model.near.NearBy;
import com.i9930.croptrails.R;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.marker_info_window, null);
    }

    private void rendowWindowText(Marker marker, View view){
        NearBy nearBy=(NearBy)marker.getTag();
        String title = marker.getTitle();

        if (nearBy!=null)
            title=nearBy.getFarmerName();
        else
            title="Title";

        TextView tvTitle = (TextView) view.findViewById(R.id.farmerNameTv);

        if(!title.equals("")){
            tvTitle.setText(title);
        }

       /* String snippet = marker.getSnippet();
        TextView tvSnippet = (TextView) view.findViewById(R.id.snippet);

        if(!snippet.equals("")){
            tvSnippet.setText(snippet);
        }*/
    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }
}