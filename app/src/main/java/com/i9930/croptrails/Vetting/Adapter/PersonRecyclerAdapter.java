package com.i9930.croptrails.Vetting.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.i9930.croptrails.R;
import com.i9930.croptrails.Vetting.Model.OtherPersonDatum;

public class PersonRecyclerAdapter extends RecyclerView.Adapter<PersonRecyclerAdapter.Holder> {
    Activity context;
    List<OtherPersonDatum> list;
    String TAG = "PersonRecyclerAdapter";

    public PersonRecyclerAdapter(Activity context, List<OtherPersonDatum> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PersonRecyclerAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_person_select, parent, false);
        return new PersonRecyclerAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonRecyclerAdapter.Holder holder, int position) {
        holder.checkbox.setText(list.get(position).getName());
        holder.checkbox.setChecked(list.get(position).isChecked());
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                list.get(position).setChecked(b);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        CheckBox checkbox;

        public Holder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }

}

