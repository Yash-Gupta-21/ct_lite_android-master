package com.i9930.croptrails.SubmitHealthCard.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCardData;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCardParams;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

public class CustomAdapter extends BaseAdapter {
    String TAG = "CustomAdapter";
    Context context;
    List<HealthCardData> paramsList;
    List<HealthCardParams> paramsDD;
    OnDataSetChangeListener listener;
    Resources resources;

    public interface OnDataSetChangeListener {
        public void onRemoveClick(int position);
    }

    List<Integer> positionList = new ArrayList<>();
    List<ViewHolder> myViewHolderList = new ArrayList<>();

    // View lookup cache
    private static class ViewHolder {
        Spinner paramSpinner;
        EditText valueEt, unitEt;
        ImageView removeParamImage;
        int ref;
    }

    public CustomAdapter(Context context, List<HealthCardData> paramsList, OnDataSetChangeListener listener, List<HealthCardParams> paramDD) {
        this.context = context;
        this.listener = listener;
        this.paramsDD = paramDD;
        this.paramsList = paramsList;
        final String languageCode = SharedPreferencesMethod2.getString(context, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();

    }


    private int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder; // view lookup cache stored in tag
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.rv_layout_add_health_card, parent, false);
            holder.paramSpinner = convertView.findViewById(R.id.paramSpinner);
            holder.valueEt = convertView.findViewById(R.id.valueEt);
            holder.removeParamImage = convertView.findViewById(R.id.removeParamImage);
            holder.unitEt = convertView.findViewById(R.id.unitEt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ref=position;
        Log.e(TAG, "Data pos ref" + holder.ref);
        if (positionList.contains(position)) {

        } else {
            positionList.add(position);
            myViewHolderList.add(holder);
        }


        HealthCardSpinnerAdapter arrayAdapter = new HealthCardSpinnerAdapter(context, R.layout.spinner_layout, paramsDD);
        holder.paramSpinner.setAdapter(arrayAdapter);


        if (position == 0) {
            holder.removeParamImage.setVisibility(View.GONE);
        } else {
            holder.removeParamImage.setVisibility(View.VISIBLE);
        }

        /*if (position == paramsList.size() - 1) {
            holder.valueEt.getParent().requestChildFocus(holder.valueEt, holder.valueEt);
        }*/
        //if (paramsList.get(position).getSpinnerPosition() != 0)
        holder.paramSpinner.setSelection(paramsList.get(position).getSpinnerPosition());
//        if (paramsList.get(position).getUnit() != null && !TextUtils.isEmpty(paramsList.get(position).getUnit()))
        holder.unitEt.setText(paramsList.get(position).getUnit());
//        if (paramsList.get(position).getValue() != null && !TextUtils.isEmpty(paramsList.get(position).getValue()))
        holder.valueEt.setText(paramsList.get(position).getValue());

        holder.removeParamImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRemoveClick(holder.ref);
                positionList.remove(holder.ref);
                myViewHolderList.remove(holder.ref);
            }
        });

        holder.paramSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                paramsList.get(holder.ref).setSpinnerPosition(i);
                paramsList.get(holder.ref).setParameterId(paramsDD.get(i).getParameterId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        holder.valueEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    listener.onValueChanged(position, charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {
                paramsList.get(holder.ref).setValue(editable.toString());
                Log.e(TAG, "Value at " + holder.ref + " is " + editable.toString());
            }
        });

        holder.unitEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    listener.onUnitChanged(position, charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {
                paramsList.get(holder.ref).setUnit(editable.toString());
                Log.e(TAG, "Unit at " + holder.ref + " is " + editable.toString());
            }
        });



        return convertView;
    }


    @Override
    public int getCount() {
        return paramsList.size();
    }

    @Override
    public HealthCardData getItem(int position) {
        return paramsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public void setErrorOnValidation(int position, String from) {
        ViewHolder viewHolder = myViewHolderList.get(position);
        if (from.equals("param")) {
            viewHolder.paramSpinner.getParent().requestChildFocus(viewHolder.paramSpinner, viewHolder.paramSpinner);
            Toasty.error(context, resources.getString(R.string.select_parameter_prompt), Toast.LENGTH_LONG).show();
        } else if (from.equals("value")) {
            viewHolder.valueEt.getParent().requestChildFocus(viewHolder.valueEt, viewHolder.valueEt);
            viewHolder.valueEt.setError(resources.getString(R.string.card_please_enter_value_msg));
            Toasty.error(context, resources.getString(R.string.card_please_enter_value_msg), Toast.LENGTH_LONG).show();
        } else if (from.equals("unit")) {
            viewHolder.unitEt.getParent().requestChildFocus(viewHolder.unitEt, viewHolder.unitEt);
            viewHolder.unitEt.setError(resources.getString(R.string.card_please_enter_unit_msg));
            Toasty.error(context, resources.getString(R.string.card_please_enter_unit_msg), Toast.LENGTH_LONG).show();
        }
    }
}