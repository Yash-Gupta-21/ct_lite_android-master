package com.i9930.croptrails.Test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.i9930.croptrails.R;
import com.i9930.croptrails.Test.SatusreModel.BinSet;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

public class SliderAdapter extends PagerAdapter {

    Context context;
    List<BinSet>bitSetList;
    ItemPositionListener listener;


    public SliderAdapter(Context context, List<BinSet> bitSetList, ItemPositionListener listener) {
        this.context = context;
        this.listener=listener;
        this.bitSetList = bitSetList;
    }


    @Override
    public int getCount() {
        if (bitSetList!=null)
        return bitSetList.size();
        else return 0;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (LinearLayout)object);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //Log.e(TAG,"Image Link "+context.getResources().getString(R.string.base_url_img) +top10List.get(position).getImages());
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.date_slider_layout,container,false);
        TextView dateTv=view.findViewById(R.id.dateTv);
        TextView monthTv=view.findViewById(R.id.monthTv);
        TextView yearTv=view.findViewById(R.id.yearTv);
        ImageView image=view.findViewById(R.id.image);
        Log.e("ShowAreaOnMapActivity","Adapter index "+position);
        try {
            DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateView = srcDf.parse(bitSetList.get(position).getDoa());
            DateFormat srcDfView = new SimpleDateFormat("dd MMMM yyyy",new Locale(SharedPreferencesMethod.getString(context, SharedPreferencesMethod2.LANGUAGECODE)));
            String dateForView=srcDfView.format(dateView);

            String[] split = dateForView.split(" ");
            Log.e("ShowAreaOnMapActivity","Adapter Old "+bitSetList.get(position).getDoa()+" New "+dateForView+" And length "+split.length);
            image.setImageBitmap(bitSetList.get(position).getImageBitmap());
            if (split.length>=3){
                dateTv.setText(split[0]);
                monthTv.setText(split[1]);
                yearTv.setText(split[2]);

            }else if (split.length>=2){
                dateTv.setText(split[0]);
                monthTv.setText(split[1]);
                yearTv.setVisibility(View.GONE);
            }else {
                dateTv.setText(split[0]);
                monthTv.setVisibility(View.GONE);
                yearTv.setVisibility(View.GONE);
            }



        } catch (ParseException e) {
            dateTv.setText(bitSetList.get(position).getDoa());
            monthTv.setVisibility(View.GONE);
            yearTv.setVisibility(View.GONE);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            dateTv.setText(bitSetList.get(position).getDoa());
            monthTv.setVisibility(View.GONE);
            yearTv.setVisibility(View.GONE);
        }

        listener.onPositionChanged(position);

        container.addView(view);
        return view;
    }


    public interface ItemPositionListener{
        public void onPositionChanged(int position);
    }

}
