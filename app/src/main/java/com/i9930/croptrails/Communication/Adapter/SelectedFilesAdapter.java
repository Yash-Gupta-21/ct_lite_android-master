package com.i9930.croptrails.Communication.Adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.i9930.croptrails.R;

import static com.i9930.croptrails.Communication.CommunicationActivity.SELECTED_TYPE;

public class SelectedFilesAdapter extends RecyclerView.Adapter<SelectedFilesAdapter.Holder>{
    List<Uri> mSelected = new ArrayList<>();
    Activity context;
    SELECTED_TYPE selectedOption;



    public SelectedFilesAdapter(List<Uri> mSelected, Activity context, SELECTED_TYPE selectedOption) {
        this.selectedOption = selectedOption;
        this.mSelected = mSelected;
        this.context = context;
    }
    public SelectedFilesAdapter(List<Uri> mSelected, Activity context) {
        this.mSelected = mSelected;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.rv_layout_selected_files, parent, false);
        return new Holder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Holder viewHolder, int position) {
        viewHolder.fileNameTv.setText(mSelected.get(position).toString());
        if (selectedOption!=null) {
            int resource = R.drawable.extension_png;
            switch (selectedOption) {
                case CAMERA: {
                    resource = R.drawable.extension_png;
                    break;
                }case GALLERY: {
                    resource = R.drawable.extension_png;
                    break;
                }
                case XL: {
                    resource = R.drawable.extension_xls;
                    break;
                }
                case PDF: {
                    resource = R.drawable.extension_pdf;
                    break;
                }
                case AUDIO: {
                    resource = R.drawable.extension_amr;
                    break;
                }
            }
            viewHolder.extensionImg.setImageResource(resource);
        }else{
            viewHolder.extensionImg.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        if (mSelected == null)
            return 0;
        else return mSelected.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView fileNameTv;
        ImageView extensionImg;

        public Holder(@NonNull View itemView) {
            super(itemView);
            extensionImg = itemView.findViewById(R.id.extensionImg);
            fileNameTv = itemView.findViewById(R.id.fileNameTv);

        }
    }

    public void setSelectedOption(SELECTED_TYPE selectedOption) {
        this.selectedOption = selectedOption;
        notifyDataSetChanged();
    }
}

