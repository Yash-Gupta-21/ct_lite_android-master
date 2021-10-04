package com.i9930.croptrails.CompSelect.Adapter;

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

import com.i9930.croptrails.CompSelect.Model.CompanyDatum;
import com.i9930.croptrails.R;

public class CompanyRvAdapter extends RecyclerView.Adapter<CompanyRvAdapter.Holder> {

    public interface CompanySelectListener {
        public void onCompanySelected(int index);
    }
    Activity context;
    List<CompanyDatum>companyDatumList;
    CompanySelectListener listener;

    public  int  lastSelectedPosition=-1;

    public CompanyRvAdapter(Activity context, List<CompanyDatum> companyDatumList, CompanySelectListener listener) {
        this.context = context;
        this.companyDatumList = companyDatumList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_layout_company_select,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.compNameTv.setText(companyDatumList.get(position).getCompName());
        if (companyDatumList.get(position).getRole()!=null&&!TextUtils.isEmpty(companyDatumList.get(position).getRole()))
            holder.roleTv.setText("as "+companyDatumList.get(position).getRole());
        holder.radioButton.setChecked(lastSelectedPosition == position);
        holder.radioButton.setClickable(false);
        String roleId=companyDatumList.get(position).getRoleId();
        if (roleId!=null) {
            if (roleId.equals("1") || roleId.equals("2") || roleId.equals("3")) {

            } else {
                holder.radioButton.setClickable(false);
                holder.radioButton.setEnabled(false);
                holder.compNameTv.setClickable(false);
                holder.compNameTv.setEnabled(false);
                holder.roleTv.setClickable(false);
                holder.roleTv.setEnabled(false);
                holder.itemView.setClickable(false);
                holder.itemView.setEnabled(false);
                holder.roleTv.setTextColor(context.getResources().getColor(R.color.faded_text));
                holder.compNameTv.setTextColor(context.getResources().getColor(R.color.faded_text));
            }
        }else {
            holder.radioButton.setClickable(false);
            holder.radioButton.setEnabled(false);
            holder.compNameTv.setClickable(false);
            holder.compNameTv.setEnabled(false);
            holder.roleTv.setClickable(false);
            holder.roleTv.setEnabled(false);
            holder.itemView.setClickable(false);
            holder.itemView.setEnabled(false);

            holder.roleTv.setTextColor(context.getResources().getColor(R.color.faded_text));
            holder.compNameTv.setTextColor(context.getResources().getColor(R.color.faded_text));
        }
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
