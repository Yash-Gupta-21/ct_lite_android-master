package com.i9930.croptrails.CropCycle.Create.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.i9930.croptrails.CropCycle.Create.Model.AddCycleAgriDatum;
import com.i9930.croptrails.CropCycle.Create.Model.AddCycleDatum;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitActivityForm.Model.CompAgriDatum;
import com.i9930.croptrails.Utility.AppConstants;

import java.util.ArrayList;
import java.util.List;

import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class CreateCropCycleAdapter extends RecyclerView.Adapter<Holder> {
    Context context;
    List<AddCycleDatum> cycleDatumList;
    LayoutInflater inflater;
    List<CompAgriDatum> agriDatumList;

    public enum ErrorType {
        DAY_NO, ACTIVITY, ACTIVITY_TYPE, PRIORITY
    }

    List<Holder> myViewHolderList = new ArrayList<>();
    List<Integer> positionList = new ArrayList<>();

    public CreateCropCycleAdapter(Context context, List<AddCycleDatum> cycleDatumList, List<CompAgriDatum> agriDatumList) {
        this.agriDatumList = agriDatumList;
        this.context = context;
        this.cycleDatumList = cycleDatumList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_layout_create_crop_cycle_activity, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (positionList.contains(position)) {
            Log.e("Adapter", "Item at " + position + " Already added");
        } else {
            Log.e("Adapter", "Item at " + position + " Adding");
            positionList.add(position);
            myViewHolderList.add(holder);
        }
        holder.activityCountTv.setText(context.getResources().getString(R.string.activity_label) + " " + (position + 1));
        holder.agriInputRecycler.setHasFixedSize(true);
        holder.agriInputRecycler.setNestedScrollingEnabled(false);
        holder.agriInputRecycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        final AgriInputAdapter agriInputAdapter = new AgriInputAdapter(context, cycleDatumList.get(position).getAgriInputs());
        holder.agriInputRecycler.setAdapter(agriInputAdapter);
        holder.arrowAgriInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cycleDatumList.get(position).getAgriInputs() == null)
                    cycleDatumList.get(position).setAgriInputs(new ArrayList<>());
                cycleDatumList.get(position).getAgriInputs().add(new AddCycleAgriDatum(agriDatumList));
                agriInputAdapter.notifyDataSetChanged();
                notifyDataSetChanged();
            }
        });
        holder.etDayNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null)
                    cycleDatumList.get(position).setDayNo(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null)
                    cycleDatumList.get(position).setDescription(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.etActivity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null)
                    cycleDatumList.get(position).setActivity(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.etInstu1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null)
                    cycleDatumList.get(position).setInst1(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.etInstu2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null)
                    cycleDatumList.get(position).setInst2(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.etInstu3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null)
                    cycleDatumList.get(position).setInst3(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.etInstu4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null)
                    cycleDatumList.get(position).setInst4(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.etInstu5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null)
                    cycleDatumList.get(position).setInst5(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ActPriorityAdapter priorityAdapter = new ActPriorityAdapter(context, cycleDatumList.get(position).getPriorityList());
        holder.prioritySpinner.setAdapter(priorityAdapter);
        holder.prioritySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                try {
                    if (priorityAdapter.getItem(holder.prioritySpinner.getSelectedPosition()) != null)
                        cycleDatumList.get(position).setSelectedPriority(priorityAdapter.getItem(holder.prioritySpinner.getSelectedPosition()).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    cycleDatumList.get(position).setSelectedPriority(null);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        ActPrioritySpinnerAdapter actPriorityAdapter = new ActPrioritySpinnerAdapter(context, cycleDatumList.get(position).getActivityTypeList());
        holder.actTypeSpinner.setAdapter(actPriorityAdapter);
        holder.actTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int i, long id) {
                try {
                    cycleDatumList.get(position).setSelectedActivity(actPriorityAdapter.getItem(holder.actTypeSpinner.getSelectedPosition()));
                } catch (Exception e) {
                    e.printStackTrace();
                    cycleDatumList.get(position).setSelectedActivity(null);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        //set data
        AddCycleDatum datum=cycleDatumList.get(position);
        if (AppConstants.isValidString(datum.getDayNo()))
            holder.etDayNo.setText(datum.getDayNo());
        if (AppConstants.isValidString(datum.getActivity()))
            holder.etActivity.setText(datum.getActivity());
        if (AppConstants.isValidString(datum.getDescription()))
            holder.etDescription.setText(datum.getDescription());

        if (AppConstants.isValidString(datum.getInst1()))
            holder.etInstu1.setText(datum.getInst1());
        if (AppConstants.isValidString(datum.getInst2()))
            holder.etInstu2.setText(datum.getInst2());
        if (AppConstants.isValidString(datum.getInst3()))
            holder.etInstu3.setText(datum.getInst3());
        if (AppConstants.isValidString(datum.getInst4()))
            holder.etInstu4.setText(datum.getInst4());
        if (AppConstants.isValidString(datum.getInst5()))
            holder.etInstu5.setText(datum.getInst5());

        if (datum.getSelectedActivity()!=null)
            holder.actTypeSpinner.setSelectedItem(datum.getSelectedActivity());

        if (AppConstants.isValidActualArea(datum.getSelectedPriority()))
            holder.prioritySpinner.setSelectedItem(Integer.valueOf(datum.getSelectedPriority()));
    }

    @Override
    public int getItemCount() {
        return cycleDatumList.size();
    }

    public List<CompAgriDatum> getAgriDatumList() {
        return agriDatumList;
    }

    public void setAgriDatumList(List<CompAgriDatum> agriDatumList) {
        try {
            this.agriDatumList = agriDatumList;
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showError(int index, ErrorType type) {
        if (index > -1 && index < cycleDatumList.size() && type != null) {
            Holder holder = myViewHolderList.get(index);
            if (type == ErrorType.DAY_NO) {
                AppConstants.failToast(context, "Please enter day number");
                holder.etDayNo.requestFocus();
                holder.etDayNo.setError(context.getResources().getString(R.string.required_label));
            } else if (type == ErrorType.ACTIVITY) {
                AppConstants.failToast(context, "Please enter activity");
                holder.etActivity.requestFocus();
                holder.etActivity.setError(context.getResources().getString(R.string.required_label));
            } else if (type == ErrorType.ACTIVITY_TYPE) {
                AppConstants.failToast(context, "Please select activity type");
                holder.actTypeSpinner.requestFocus();
            } else if (type == ErrorType.PRIORITY) {
                AppConstants.failToast(context, "Please select priority");
                holder.prioritySpinner.requestFocus();
            }
        }

    }
}
