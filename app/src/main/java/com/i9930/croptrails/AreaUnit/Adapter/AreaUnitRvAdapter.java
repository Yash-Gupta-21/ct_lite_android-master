package com.i9930.croptrails.AreaUnit.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.i9930.croptrails.CommonClasses.DDNew;
import com.i9930.croptrails.CompSelect.Adapter.CompanyRvAdapter;
import com.i9930.croptrails.R;

public class AreaUnitRvAdapter extends RecyclerView.Adapter<AreaUnitRvAdapter.Holder> {

    Context context;
    List<DDNew> ddNewList;
    public int lastSelectedPosition = -1;
    Resources resources;

    public interface AreaUnitSelectListener {
        public void onUnitSelected(int index, DDNew ddNew);
    }

    AreaUnitSelectListener listener;

    public AreaUnitRvAdapter(Context context, List<DDNew> ddNewList, AreaUnitSelectListener listener) {
        this.context = context;
        this.listener = listener;
        this.ddNewList = ddNewList;
        resources=context.getResources();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_select_area_unit, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        DDNew ddNew = ddNewList.get(position);
        holder.areaName.setText(ddNew.getParameters());
        String name="1 "+ddNew.getParameters();
        String name2="";
        if (ddNew.getValue()!=null&&!TextUtils.isEmpty(ddNew.getValue())){
            try {
                name2=ddNew.getValue().trim()+" Acre";
            }catch (NumberFormatException e){

            }catch (Exception e){

            }
        }
        holder.conversionTv.setText("("+name+" = " + name2 + ")");
        if (lastSelectedPosition==position){
            holder.relNewFarmAnd.setBackgroundResource(R.drawable.selected_option_check_area);
            holder.areaName.setTextColor(resources.getColor(R.color.white));
            holder.conversionTv.setTextColor(resources.getColor(R.color.white));
        }else {
            holder.relNewFarmAnd.setBackgroundResource(R.drawable.selected_option_check_area_not);
            holder.areaName.setTextColor(resources.getColor(R.color.black));
            holder.conversionTv.setTextColor(resources.getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        if (ddNewList == null)
            return 0;
        else return ddNewList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        RelativeLayout relNewFarmAnd;
        TextView areaName, conversionTv;

        public Holder(@NonNull View itemView) {
            super(itemView);
            relNewFarmAnd = itemView.findViewById(R.id.relNewFarmAnd);
            areaName = itemView.findViewById(R.id.areaName);
            conversionTv = itemView.findViewById(R.id.conversionTv);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lastSelectedPosition != -1) {
                        {

                            lastSelectedPosition = getAdapterPosition();
                            notifyDataSetChanged();
                            try {
                                if (lastSelectedPosition!=-1&&lastSelectedPosition<ddNewList.size())
                                    listener.onUnitSelected(lastSelectedPosition,ddNewList.get(lastSelectedPosition));
                            }catch (Exception e){

                            }
                        }
                    } else {
                        lastSelectedPosition = getAdapterPosition();
                        try {
                            if (lastSelectedPosition!=-1&&lastSelectedPosition<ddNewList.size())
                                listener.onUnitSelected(lastSelectedPosition,ddNewList.get(lastSelectedPosition));
                        }catch (Exception e){

                        }
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
}