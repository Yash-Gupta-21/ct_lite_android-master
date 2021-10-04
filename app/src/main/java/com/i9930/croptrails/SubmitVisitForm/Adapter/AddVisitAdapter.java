package com.i9930.croptrails.SubmitVisitForm.Adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;
import com.i9930.croptrails.CommonClasses.TimelineUnits;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitActivityForm.Adapter.AgriSelectSpinnerAdapter;
import com.i9930.croptrails.SubmitActivityForm.Model.CompAgriDatum;
import com.i9930.croptrails.SubmitVisitForm.Interfaces.VisitFormListener;
import com.i9930.croptrails.SubmitVisitForm.Model.VisitMaterialDynamic;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static java.lang.Double.parseDouble;

public class AddVisitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int CPC = 3, SML = 4, OTHER = 5,AGRI_INPUT = 7;

    Context context;
    List<VisitMaterialDynamic> list;
    VisitFormListener visitFormListener;
    Resources resources;
    List<Integer> positionList = new ArrayList<>();
    List<RecyclerView.ViewHolder> myViewHolderList = new ArrayList<>();
    String TAG = "AddVisitAdapter";

    public AddVisitAdapter(Context context, List<VisitMaterialDynamic> list, VisitFormListener visitFormListener) {
        this.context = context;
        this.list = list;
        this.visitFormListener = visitFormListener;
        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {

            case CPC:
                View v4 = inflater.inflate(R.layout.rv_layout_add_visit_cpc, parent, false);
                viewHolder = new AddVisitAdapter.CpcViewHolder(v4);
                break;
            case SML:
                View v5 = inflater.inflate(R.layout.rv_layout_add_visit_sml, parent, false);
                viewHolder = new AddVisitAdapter.SmlViewHolder(v5);
                break;
            case OTHER:
                View v6 = inflater.inflate(R.layout.rv_layout_add_visit_other, parent, false);
                viewHolder = new AddVisitAdapter.OtherViewHolder(v6);
                break;
            case AGRI_INPUT:
                View v8 = inflater.inflate(R.layout.rv_layout_agri_input, parent, false);
                viewHolder = new AddVisitAdapter.MyViewHolder(v8);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new AddVisitAdapter.StandingViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {

            case OTHER:
                AddVisitAdapter.OtherViewHolder vh6 = (AddVisitAdapter.OtherViewHolder) holder;
                configureOtherViewHolder(vh6, position);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(vh6);
                }
                break;
            case SML:
                AddVisitAdapter.SmlViewHolder vh5 = (AddVisitAdapter.SmlViewHolder) holder;
                configureSmlViewHolder(vh5, position);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(vh5);
                }
                break;
            case AGRI_INPUT:
                AddVisitAdapter.MyViewHolder vh8 = (AddVisitAdapter.MyViewHolder) holder;
                configureAgriInputViewHolder(vh8, position);
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    myViewHolderList.add(vh8);
                }
                break;
            default:
                /*RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) holder;
                configureDefaultViewHolder(vh, position);*/
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
       if (list.get(position).getType().equals(AppConstants.VISIT_ACTIVITY.CPC)) {
            return CPC;
        } else if (list.get(position).getType().equals(AppConstants.VISIT_ACTIVITY.SML)) {
            return SML;
        } else if (list.get(position).getType().equals(AppConstants.VISIT_ACTIVITY.OTHER)) {
            return OTHER;
        }  else if (list.get(position).getType().equals(AppConstants.VISIT_ACTIVITY.AGRI_INPUT)) {
            return AGRI_INPUT;
        }else {
            return 0;
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class CpcViewHolder extends RecyclerView.ViewHolder {
        ImageView removeItemImg;
        TextView cpcTitleTv;
        Spinner cpcSpinner;

        public CpcViewHolder(@NonNull View itemView) {
            super(itemView);
            removeItemImg = itemView.findViewById(R.id.removeItemImg);
            cpcTitleTv = itemView.findViewById(R.id.cpcTitleTv);
            cpcSpinner = itemView.findViewById(R.id.cpcSpinner);

        }
    }

    private class SmlViewHolder extends RecyclerView.ViewHolder {
        ImageView removeItemImg;
        TextView smlTitleTv;
        Spinner smlSpinner;
        RatingBar ratingGrade;

        public SmlViewHolder(@NonNull View itemView) {
            super(itemView);
            removeItemImg = itemView.findViewById(R.id.removeItemImg);
            smlTitleTv = itemView.findViewById(R.id.smlTitleTv);
            ratingGrade = itemView.findViewById(R.id.ratingGrade);
            smlSpinner = itemView.findViewById(R.id.smlSpinner);
        }
    }

    private class OtherViewHolder extends RecyclerView.ViewHolder {
        ImageView removeItemImg;
        TextView otherTitileTv;
        EditText otherCommentEt;

        public OtherViewHolder(@NonNull View itemView) {
            super(itemView);
            removeItemImg = itemView.findViewById(R.id.removeItemImg);
            otherTitileTv = itemView.findViewById(R.id.otherTitileTv);
            otherCommentEt = itemView.findViewById(R.id.otherCommentEt);
        }
    }

    private class StandingViewHolder extends RecyclerView.ViewHolder {
        TextView lastStandingAreaTv, areaUnitLabelTv1, areaUnitLabelTv2;
        ImageView removeItemImg;
        EditText currentStandingAreaEt;

        public StandingViewHolder(@NonNull View itemView) {
            super(itemView);
            removeItemImg = itemView.findViewById(R.id.removeItemImg);
            lastStandingAreaTv = itemView.findViewById(R.id.lastStandingAreaTv);
            areaUnitLabelTv1 = itemView.findViewById(R.id.areaUnitLabelTv1);
            areaUnitLabelTv2 = itemView.findViewById(R.id.areaUnitLabelTv2);
            currentStandingAreaEt = itemView.findViewById(R.id.currentStandingAreaEt);
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        int ref;
        EditText actAmountEt, actQtyEt,otherAgriNameEt;
        TextView expAmountEt, expQtyEt;
        TextView instructionTv;
        LinearLayout expectedLayout;
        SearchableSpinner inputSpinner;
        TextInputLayout otherAgriNameTi;

        public MyViewHolder(View itemView) {
            super(itemView);
            otherAgriNameEt = itemView.findViewById(R.id.otherAgriNameEt);
            otherAgriNameTi = itemView.findViewById(R.id.otherAgriNameTi);
            inputSpinner = itemView.findViewById(R.id.inputSpinner);
            expectedLayout = itemView.findViewById(R.id.expectedLayout);
            actAmountEt = itemView.findViewById(R.id.actAmountEt);
            actQtyEt = itemView.findViewById(R.id.actQtyEt);
            instructionTv = itemView.findViewById(R.id.instructionTv);
            expAmountEt = itemView.findViewById(R.id.expAmountEt);
            expQtyEt = itemView.findViewById(R.id.expQtyEt);
        }
    }

    private void configureOtherViewHolder(AddVisitAdapter.OtherViewHolder vh, int position) {
        vh.otherCommentEt.setText(list.get(position).getComment());
        vh.otherTitileTv.setText(resources.getString(R.string.other_label));
        vh.otherCommentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    list.get(position).setComment(charSequence.toString());
                    Log.e(TAG,"Other Comment "+charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        vh.removeItemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                visitFormListener.onOtherRemoved();
                notifyDataSetChanged();
            }
        });
    }
    private void configureAgriInputViewHolder(AddVisitAdapter.MyViewHolder holder, final int position) {

        holder.actAmountEt.setTag(holder);
        holder.actQtyEt.setTag(holder);
        holder.expAmountEt.setTag(holder);
        holder.expQtyEt.setTag(holder);
        holder.otherAgriNameEt.setTag(holder);
        holder.instructionTv.setTag(holder);
        holder.expectedLayout.setTag(holder);
        holder.inputSpinner.setTag(holder);
        holder.otherAgriNameTi.setTag(holder);
        if (list.get(position).getAgriInput().isManualAdded()) {
            holder.instructionTv.setVisibility(View.GONE);
            holder.expectedLayout.setVisibility(View.GONE);
            holder.inputSpinner.setVisibility(View.VISIBLE);

            holder.otherAgriNameEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    try {
                        list.get(position).getAgriInput().setName(charSequence.toString().trim());
//                    notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else {
            holder.expectedLayout.setVisibility(View.VISIBLE);
            holder.instructionTv.setVisibility(View.VISIBLE);
            holder.inputSpinner.setVisibility(View.GONE);
            holder.otherAgriNameTi.setVisibility(View.GONE);
        }
        if (AppConstants.isValidString(list.get(position).getAgriInput().getAmount()))
            holder.expAmountEt.setText(list.get(position).getAgriInput().getAmount());
        else
            holder.expAmountEt.setText("-");
        if (AppConstants.isValidString(list.get(position).getQuantity()))
            holder.expQtyEt.setText(list.get(position).getQuantity());
        else
            holder.expQtyEt.setText("-");
        if (AppConstants.isValidString(list.get(position).getAgriInput().getName()))
            holder.instructionTv.setText(list.get(position).getAgriInput().getName());
        else
            holder.instructionTv.setText("Unknown");

        if (AppConstants.isValidString(list.get(position).getAgriInput().getCost())) {
            holder.actAmountEt.setText(list.get(position).getAgriInput().getCost());
        } else {
            holder.actAmountEt.setText("");
        }
        if (AppConstants.isValidString(list.get(position).getAgriInput().getUsedQuantity())) {
            holder.actQtyEt.setText(list.get(position).getAgriInput().getUsedQuantity());
        } else {
            holder.actQtyEt.setText("");
        }
        holder.actAmountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    list.get(position).getAgriInput().setCost(charSequence.toString().trim());
//                    list.get(position).setCost(charSequence.toString().trim());
//                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.actQtyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    list.get(position).getAgriInput().setUsedQuantity(charSequence.toString().trim());
//                    list.get(position).setUsedQuantity(charSequence.toString().trim());
//                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (positionList.contains(position)) {
            Log.e("AgriInputAdapter", "Item at " + position + " Already added");
        } else {
            Log.e("AgriInputAdapter", "Item at " + position + " Adding");
            positionList.add(position);
            myViewHolderList.add(holder);
        }


        List<CompAgriDatum> compAgriDatumList = list.get(position).getAgriInput().getCompAgriDatumList();
        if (compAgriDatumList != null && compAgriDatumList.size() > 0) {
            AgriSelectSpinnerAdapter adapter = new AgriSelectSpinnerAdapter(context, compAgriDatumList);
            holder.inputSpinner.setAdapter(adapter);
            try {
                if (list.get(position).getAgriInput().getSelectedIndex() > 0)
                    holder.inputSpinner.setSelectedItem(list.get(position).getAgriInput().getSelectedIndex());
            }catch (Exception e){
                e.printStackTrace();
            }
            holder.inputSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(View view, int i, long id) {
                    try {
                        list.get(position).getAgriInput().setSelectedIndex(i);
                        CompAgriDatum season = adapter.getItem(i);
                        if (season != null) {
                            list.get(position).getAgriInput().setAgriId(season.getId());
                            list.get(position).getAgriInput().setInputId(season.getId());
                            if (AppConstants.isValidString(season.getName()) && season.getName().toLowerCase().equals("other") || season.getName().toLowerCase().equals("others")) {
                                holder.otherAgriNameTi.setVisibility(View.VISIBLE);
                            } else {
                                holder.otherAgriNameTi.setVisibility(View.GONE);
                            }
                            list.get(position).getAgriInput().setName(season.getName());
                        } else {
                            list.get(position).getAgriInput().setAgriId(null);
                            holder.otherAgriNameTi.setVisibility(View.VISIBLE);
                            list.get(position).getAgriInput().setName(null);
                        }
//                            holder.otherAgriNameEt.setText("");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected() {

                }
            });
        } else {
            holder.inputSpinner.setVisibility(View.GONE);
            if (list.get(position).getAgriInput().isManualAdded())
                holder.otherAgriNameTi.setVisibility(View.VISIBLE);
            else
                holder.otherAgriNameTi.setVisibility(View.GONE);
        }

        holder.setIsRecyclable(false);
    }
    public void setErrorOnValidationArea(int position){
        AddVisitAdapter.StandingViewHolder viewHolder = (AddVisitAdapter.StandingViewHolder) myViewHolderList.get(position);
        viewHolder.currentStandingAreaEt.setError(resources.getString(R.string.enter_coorect_standing_area_msg));
        viewHolder.itemView.getParent().requestChildFocus(viewHolder.itemView,viewHolder.itemView);
        Toasty.error(context, resources.getString(R.string.enter_coorect_standing_area_msg), Toast.LENGTH_LONG).show();
    }
    private void configureSmlViewHolder(AddVisitAdapter.SmlViewHolder vh, int position) {
        vh.smlTitleTv.setText(resources.getString(R.string.soil_moisture_level_label));
        vh.smlSpinner.setSelection(list.get(position).getMaterialPosition());
        vh.ratingGrade.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                list.get(position).setMaterial(v+"");
                list.get(position).setMaterialId(v+"");
            }
        });
        vh.removeItemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                visitFormListener.onSmlRemoved();
                notifyDataSetChanged();
            }
        });
    }
}
