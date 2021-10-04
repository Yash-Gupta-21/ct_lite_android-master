package com.i9930.croptrails.AddFarm.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;
import com.i9930.croptrails.AddFarm.Model.FPOCrop;
import com.i9930.croptrails.AddFarm.Model.FarmCrop;
import com.i9930.croptrails.AddFarm.SpinnerAdapter.SpinnerAdapterNewDD;
import com.i9930.croptrails.CommonAdapters.CropSpinner.CropSpinnerAdapter;
import com.i9930.croptrails.CommonClasses.DDNew;
import com.i9930.croptrails.CommonClasses.DynamicForm.CropFormDatum;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.Season;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

public class AddFarmCropAdapter2 extends RecyclerView.Adapter<AddFarmCropAdapter2.Holder> {

    Context context;
    List<FPOCrop> fpoCropList;
    List<Season> seasonList;
    List<DDNew> cropList;

    Date todayDate;
    Date c;
    SimpleDateFormat df;
    String todayDateStr = "";

    public interface OnLastItemremoved {
        public void onLastItem(boolean isLast, int position);

        public void onDataSetChanged(List<FPOCrop> fpoCropList);
    }

    OnLastItemremoved listener;
    String compId;

    public AddFarmCropAdapter2(Context context, List<FPOCrop> fpoCropList, List<DDNew> crops, List<Season> seasonList, OnLastItemremoved lastItemremoved) {
        this.context = context;
        this.cropList = crops;
        this.seasonList = seasonList;
        this.listener = lastItemremoved;
        this.fpoCropList = fpoCropList;
        compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        c = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("yyyy-MM-dd");
        todayDateStr = df.format(c);
        try {
            todayDate = df.parse(todayDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public AddFarmCropAdapter2.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_add_farm_crop_2, parent, false);
        return new AddFarmCropAdapter2.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFarmCropAdapter2.Holder holder, int position) {
        holder.ref = position;

        SeasonAdapterRad seasonAdapterRad = new SeasonAdapterRad(context, fpoCropList.get(holder.ref).getSeasonId(), seasonList, new SeasonAdapterRad.OnItemClick() {
            @Override
            public void onItemClick(int position, String id) {

                fpoCropList.get(holder.ref).setSeasonId(id);

                holder.isSeasonSelected = true;
                if (compId.equals("100075")) {
                    holder.fpoMessageTv.setVisibility(View.VISIBLE);
                } else {
                    holder.fpoMessageTv.setVisibility(View.GONE);
                }
                try {
                    DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = srcDf.parse(seasonList.get(position).getEnd());
                    if (date.before(todayDate)) {
                        fpoCropList.get(holder.ref).setExpectedOrActual("A");

                        holder.fpoMessageTv.setText(context.getResources().getString(R.string.fpo_msg_if_season_past));
                        holder.formLayout.setVisibility(View.VISIBLE);
                    } else {
                        fpoCropList.get(holder.ref).setExpectedOrActual("E");
                        holder.fpoMessageTv.setText(context.getResources().getString(R.string.fpo_msg_if_season_present));
                        holder.formLayout.setVisibility(View.VISIBLE);
                    }
                    listener.onDataSetChanged(fpoCropList);
                } catch (ParseException e) {
                    e.printStackTrace();
                    fpoCropList.get(holder.ref).setExpectedOrActual("A");
                    holder.fpoMessageTv.setVisibility(View.GONE);
                    listener.onDataSetChanged(fpoCropList);
                    holder.formLayout.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    listener.onDataSetChanged(fpoCropList);
                    fpoCropList.get(holder.ref).setExpectedOrActual("A");
                    holder.fpoMessageTv.setVisibility(View.GONE);
                    e.printStackTrace();
                    holder.formLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        if (compId.equals("100075")) {
            if (holder.isSeasonSelected) {
                holder.formLayout.setVisibility(View.VISIBLE);

            } else {
                holder.formLayout.setVisibility(View.GONE);
                holder.fpoMessageTv.setVisibility(View.GONE);
            }
        } else {
            holder.formLayout.setVisibility(View.VISIBLE);
            holder.fpoMessageTv.setVisibility(View.GONE);
        }
        holder.radioRecycler.setHasFixedSize(true);
        holder.radioRecycler.setLayoutManager(new GridLayoutManager(context, 2));
        holder.radioRecycler.setNestedScrollingEnabled(false);
        holder.radioRecycler.setAdapter(seasonAdapterRad);

        CropFormRvAdapterOuter cropFormRvAdapter = new CropFormRvAdapterOuter(context, fpoCropList.get(position).getCropFormDatumArrayLists());

        holder.recyclerFormCard.setHasFixedSize(true);
        holder.recyclerFormCard.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerFormCard.setNestedScrollingEnabled(false);
        holder.recyclerFormCard.setAdapter(cropFormRvAdapter);

//        SpinnerAdapterNewDD cropSpinnerAdapter=new SpinnerAdapterNewDD(context,R.layout.spinner_layout,cropList);
        SpinnerAdapterNewDD cropSpinnerAdapter = new SpinnerAdapterNewDD(context, cropList, context.getResources().getString(R.string.select_crop_rompt));
        holder.cropSpinner.setAdapter(cropSpinnerAdapter);
        holder.cropSpinner.setSelectedItem(fpoCropList.get(holder.ref).getCropPosition());
        if (fpoCropList.get(holder.ref).getSelectedCrop() != null) {
            try {
            holder.cropSpinner.setSelectedItem(fpoCropList.get(holder.ref).getSelectedCrop());
            } catch (Exception e) {

            }
        }


        holder.cropSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                try {
                    DDNew ddNew = cropSpinnerAdapter.getItem(i);
                    fpoCropList.get(holder.ref).setSelectedCrop(ddNew);
                    if (ddNew != null) {
                        fpoCropList.get(holder.ref).setCropPosition(i);
                        fpoCropList.get(holder.ref).setCropId(ddNew.getId());
                        listener.onDataSetChanged(fpoCropList);
                    } else {
                        fpoCropList.get(holder.ref).setCropPosition(i);
                        fpoCropList.get(holder.ref).setCropId("0");
                        listener.onDataSetChanged(fpoCropList);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        holder.etArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fpoCropList.get(holder.ref).setFarmArea(s.toString());
                listener.onDataSetChanged(fpoCropList);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        try {
            holder.etArea.setText(fpoCropList.get(holder.ref).getFarmArea());
        }catch (Exception e){

        }

        holder.deleteFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fpoCropList.remove(holder.ref);
                if (fpoCropList.size() == 0) {
                    listener.onLastItem(true, holder.ref);
                    notifyDataSetChanged();
                    listener.onDataSetChanged(fpoCropList);
                } else {
                    listener.onLastItem(false, holder.ref);
                    notifyDataSetChanged();
                    listener.onDataSetChanged(fpoCropList);
                }
            }
        });

        try {
            holder.setIsRecyclable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if (fpoCropList != null)
            return fpoCropList.size();
        else return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView deleteFarm;
        int ref;
        boolean isSeasonSelected;
        SearchableSpinner cropSpinner;
        RecyclerView radioRecycler;
        RadioButton expectedRadio, actualRadio;

        EditText etArea;

        TextView fpoMessageTv;
        RecyclerView recyclerFormCard;

        LinearLayout formLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            formLayout = itemView.findViewById(R.id.formLayout);
            fpoMessageTv = itemView.findViewById(R.id.fpoMessageTv);

            deleteFarm = itemView.findViewById(R.id.deleteFarm);
            radioRecycler = itemView.findViewById(R.id.radioRecycler);
            expectedRadio = itemView.findViewById(R.id.expectedRadio);
            actualRadio = itemView.findViewById(R.id.actualRadio);
            {
                recyclerFormCard = itemView.findViewById(R.id.recyclerFormCard);
                etArea = itemView.findViewById(R.id.etArea);
                cropSpinner = itemView.findViewById(R.id.cropSpinner);
            }
        }
    }
}