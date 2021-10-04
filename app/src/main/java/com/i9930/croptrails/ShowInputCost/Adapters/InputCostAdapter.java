package com.i9930.croptrails.ShowInputCost.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import com.i9930.croptrails.FarmerInnerDashBoard.Models.Image;
import com.i9930.croptrails.R;
import com.i9930.croptrails.ShowInputCost.Model.InputCostData;

public class InputCostAdapter extends RecyclerView.Adapter<InputCostAdapter.Holder> {

    Context context;
    List<InputCostData> inputCostDataList;

    public InputCostAdapter(Context context, List<InputCostData> inputCostDataList) {
        this.context = context;
        this.inputCostDataList = inputCostDataList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_show_input_cost, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String date=inputCostDataList.get(position).getExpenseDate();
    try {
        DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateView = srcDf.parse(inputCostDataList.get(position).getExpenseDate());
        DateFormat srcDfView = new SimpleDateFormat("dd MMM");
        date = srcDfView.format(dateView);
    }catch (Exception e){
        Log.e("InputCostAdapter",e.toString());
    }

        holder.added_by_tv.setText("Added by "+inputCostDataList.get(position).getName());
        holder.cost_date_tv.setText(date);
        holder.cost_tv.setText("Rs. "+inputCostDataList.get(position).getExpense()+"/-");
        if (inputCostDataList.get(position).getType() != null && !TextUtils.isEmpty(inputCostDataList.get(position).getType())&&!inputCostDataList.get(position).getType().equals("0"))
            holder.cost_type_tv.setText("Input cost type "+inputCostDataList.get(position).getType());
        else
            holder.cost_type_tv.setText("Input cost type "+inputCostDataList.get(position).getOtherTypeName());

    }

    @Override
    public int getItemCount() {
        return inputCostDataList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        //ImageView cost_img;
        TextView added_by_tv, cost_tv, cost_type_tv, cost_date_tv;

        public Holder(@NonNull View itemView) {
            super(itemView);
            //cost_img = itemView.findViewById(R.id.cost_img);
            added_by_tv = itemView.findViewById(R.id.added_by_tv);
            cost_type_tv = itemView.findViewById(R.id.cost_type_tv);
            cost_tv = itemView.findViewById(R.id.cost_tv);
            cost_date_tv = itemView.findViewById(R.id.cost_date_tv_);
        }
    }
}
