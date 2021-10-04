package com.i9930.croptrails.Maps.Offline.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.i9930.croptrails.R;


public class OfflineMapSuggestionAdapter extends RecyclerView.Adapter<OfflineMapSuggestionAdapter.Holder> {

    Context context;
    List<String>suggestions;

    public OfflineMapSuggestionAdapter(Context context, List<String> suggestions) {
        this.context = context;
        this.suggestions = suggestions;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_layout_offline_location_guidelines,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.suggestionTv.setText(suggestions.get(position));
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView suggestionTv;
        public Holder(@NonNull View itemView) {
            super(itemView);
            suggestionTv=itemView.findViewById(R.id.suggestionTv);
        }
    }
}
