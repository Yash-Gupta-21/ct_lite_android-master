package com.i9930.croptrails.SubmitInputCost.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.i9930.croptrails.FarmerInnerDashBoard.Models.Image;
import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitInputCost.Model.InputCostSubmitDatum;

public class InputCostListAdapterSubmit extends RecyclerView.Adapter<InputCostListAdapterSubmit.Holder> {

    Context context;
    List<InputCostSubmitDatum>list;

    public InputCostListAdapterSubmit(Context context, List<InputCostSubmitDatum> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_layout_input_cost_submit,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        int seq=position+1;
        holder.seq_number_tv.setText(""+seq);
        holder.cost_amount_tv.setText(list.get(position).getExpenseAmount());
        holder.cost_name_tv.setText(list.get(position).getName());
        holder.remove_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView seq_number_tv,cost_name_tv,cost_amount_tv;
        ImageView remove_image;
        public Holder(@NonNull View itemView) {
            super(itemView);
            seq_number_tv=itemView.findViewById(R.id.seq_number_tv);
            cost_name_tv=itemView.findViewById(R.id.cost_name_tv);
            cost_amount_tv=itemView.findViewById(R.id.cost_amount_tv);
            remove_image=itemView.findViewById(R.id.remove_image);
        }
    }
}
