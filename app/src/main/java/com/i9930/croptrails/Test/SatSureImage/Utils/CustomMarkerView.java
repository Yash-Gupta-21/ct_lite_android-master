package com.i9930.croptrails.Test.SatSureImage.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;

import com.i9930.croptrails.R;
import com.i9930.croptrails.Test.SatSureImage.SatsureImageActivity;

public class CustomMarkerView extends MarkerView implements IMarker {

    private TextView tvContent;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
// content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        Log.i("CustomMarkerView", "Entry selected " + e.toString() + ", X=" + e.getX() + ", Y=" + e.getY());
        //Log.i("CustomMarkerView", "Entry selected "+e.toString()+", X="+e.getX()+", Y="+e.getY()+", Data="+new Gson().toJson(e.getData()));
        tvContent.setText("" + e.getY());

        //tvContent.setText("" + Utils.formatNumber(e.getY(), 2, true));
        String color = "#ff5252";
        if (e.getY() >= 0 && e.getY() <= 100) {
            color = "#000000";
            color= SatsureImageActivity.to100Color;
        } else if (e.getY() > 100 && e.getY() <=110) {
            //color = "#6e0000";
            color= SatsureImageActivity.to110Color;
        } else if (e.getY() > 110 && e.getY() <= 120) {
            //color = "#8c4646";
            color= SatsureImageActivity.to120Color;
        } else if (e.getY() > 120 && e.getY() <= 130) {
//            color = "#ff4600";
            color= SatsureImageActivity.to130Color;
        } else if (e.getY() > 130 && e.getY() <= 140) {
            color = "#ff8c00";
            color= SatsureImageActivity.to140Color;
        } else if (e.getY() > 140 && e.getY() <= 150) {
            color = "#ffff14";
            color= SatsureImageActivity.to150Color;
        } else if (e.getY() > 150 && e.getY() <= 160) {
            color = "#00FFFF";
            color= SatsureImageActivity.to160Color;
        } else if (e.getY() > 160 && e.getY() <= 170) {
            color = "#0099FF";
            color= SatsureImageActivity.to170Color;
        } else if (e.getY() > 170 && e.getY() <= 180) {
            color = "#0044FF";
            color= SatsureImageActivity.to180Color;
        } else if (e.getY() > 180 && e.getY() <= 190) {
            color = "#0000FF";
            color= SatsureImageActivity.to190Color;
        } else if (e.getY() > 190 && e.getY() <= 200) {
            color = "#0000FF";
            color= SatsureImageActivity.to200Color;
        }


        Drawable background = tvContent.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(color));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(color));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(color));
        }


        tvContent.setTextColor(getResources().getColor(R.color.white));// set the entry-value as the display text

        super.refreshContent(e, highlight);

    }

    public int getXOffset(float xpos) {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    public int getYOffset(float ypos) {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}