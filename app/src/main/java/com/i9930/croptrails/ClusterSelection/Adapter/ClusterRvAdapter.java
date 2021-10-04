package com.i9930.croptrails.ClusterSelection.Adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.i9930.croptrails.CommonClasses.Cluster;
import com.i9930.croptrails.R;

public class ClusterRvAdapter extends RecyclerView.Adapter<ClusterRvAdapter.Holder> {

    public interface ClusterSelectListener {
        public void onCompanySelected(int index);
    }
    Activity context;
    List<Cluster> companyDatumList;
    ClusterSelectListener listener;

    public  int  lastSelectedPosition=-1;

    public ClusterRvAdapter(Activity context, List<Cluster> companyDatumList, ClusterSelectListener listener) {
        this.context = context;
        this.companyDatumList = companyDatumList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClusterRvAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_layout_company_select,parent,false);
        return new ClusterRvAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClusterRvAdapter.Holder holder, int position) {

        holder.compNameTv.setText(companyDatumList.get(position).getClusterName());

        holder.radioButton.setChecked(lastSelectedPosition == position);
        holder.radioButton.setClickable(false);
        holder.roleTv.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return companyDatumList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        RadioButton radioButton;
        TextView compNameTv,roleTv;

        public Holder(@NonNull View itemView) {
            super(itemView);
            radioButton=itemView.findViewById(R.id.radio);
            compNameTv=itemView.findViewById(R.id.compNameTv);
            roleTv=itemView.findViewById(R.id.roleTv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastSelectedPosition!=-1){
                       /* if (lastSelectedPosition==getAdapterPosition()){
                            lastSelectedPosition = -1;
                            listener.onCompanySelected(-1);
                            notifyDataSetChanged();
                        }else */{

                            lastSelectedPosition = getAdapterPosition();
                            notifyDataSetChanged();
                            listener.onCompanySelected(lastSelectedPosition);
                        }
                    }else {
                        lastSelectedPosition = getAdapterPosition();
                        listener.onCompanySelected(lastSelectedPosition);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
