package com.i9930.croptrails.CropCycle.Create.Adapter;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.i9930.croptrails.R;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;

public class Holder extends RecyclerView.ViewHolder {
    RecyclerView agriInputRecycler;
    ImageView arrowAgriInput;
    TextView activityCountTv;
    EditText etDayNo,etActivity,etDescription,etInstu1,etInstu2,etInstu3,etInstu4,etInstu5;
    SearchableSpinner actTypeSpinner,prioritySpinner;
    public Holder(@NonNull View itemView) {
        super(itemView);
        arrowAgriInput=itemView.findViewById(R.id.arrowAgriInput);
        activityCountTv=itemView.findViewById(R.id.activityCountTv);
        agriInputRecycler=itemView.findViewById(R.id.agriInputRecycler);
        etDayNo=itemView.findViewById(R.id.etDayNo);
        etActivity=itemView.findViewById(R.id.etActivity);
        etDescription=itemView.findViewById(R.id.etDescription);
        etInstu1=itemView.findViewById(R.id.etInstu1);
        etInstu2=itemView.findViewById(R.id.etInstu2);
        etInstu3=itemView.findViewById(R.id.etInstu3);
        etInstu4=itemView.findViewById(R.id.etInstu4);
        etInstu5=itemView.findViewById(R.id.etInstu5);
        actTypeSpinner=itemView.findViewById(R.id.actTypeSpinner);
        prioritySpinner=itemView.findViewById(R.id.prioritySpinner);
    }
}
