package com.i9930.croptrails.SubmitActivityForm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitActivityForm.Model.AgriInput.Instruction;
import com.i9930.croptrails.SubmitActivityForm.Model.ChemicalDataList;

public class AgriInstructionAdapter  extends RecyclerView.Adapter<AgriInstructionAdapter.MyViewHolder> {

    Context context;
    List<Instruction> list;

    public AgriInstructionAdapter(Context context, List<Instruction> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AgriInstructionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_layout_agri_input_instructions,parent,false);
        return new AgriInstructionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgriInstructionAdapter.MyViewHolder holder, final int position) {
        int serialNo=position+1;
        String isntruction=list.get(position).getInstruction();
        isntruction="Instrucion "+serialNo;
        holder.instructionTv.setText(serialNo+": "+isntruction);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView instructionTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            instructionTv=itemView.findViewById(R.id.instructionTv);
        }
    }
}

