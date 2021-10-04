package com.i9930.croptrails.SubmitHealthCard.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.i9930.croptrails.R;
import com.i9930.croptrails.SubmitHealthCard.Model.CardParameters;
import com.i9930.croptrails.SubmitHealthCard.Model.HealthCardResponse;
import com.i9930.croptrails.Utility.AppConstants;

public class ShowHealthCardRvAdapter extends RecyclerView.Adapter<ShowHealthCardRvAdapter.Holder> {
    Context context;
    HealthCardResponse cardResponse;
    CardClickListener listener;

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_layout_show_health_card_outer,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.labNameTv.setText(cardResponse.getHealthCardList().get(position).getLaboratoryName());
    holder.cardNo.setText(cardResponse.getHealthCardList().get(position).getHealthCardNum());
    holder.addedOn.setText(AppConstants.getShowableDate(cardResponse.getHealthCardList().get(position).getSampleCollectionDate(),context));
    holder.labNameTvLable.setText("("+context.getResources().getString(R.string.laboratory_name)+")");
        String hint=context.getResources().getString(R.string.health_card_no_hint).substring(1);
        String hintCollectedOn=context.getResources().getString(R.string.sample_collected_on_hint).substring(1);
        holder.cardNoTvLabel.setText("("+hint+")");
        holder.addedOnTvLabel.setText("("+hintCollectedOn+")");




        holder.itemView.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           listener.onClick(position);
       }
   });

    }

    @Override
    public int getItemCount() {
        if (cardResponse!=null){
            if (cardResponse.getHealthCardList()!=null)
                return  cardResponse.getHealthCardList().size();
        else
            return 0;
        }
        else
        return 0;
    }

    public interface CardClickListener{
         void onClick(int position);

    }

    public ShowHealthCardRvAdapter(Context context, HealthCardResponse cardResponse, CardClickListener listener) {
        this.context = context;
        this.cardResponse = cardResponse;
        this.listener=listener;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView addedOn,cardNo,labNameTv,labNameTvLable,cardNoTvLabel,addedOnTvLabel;
        public Holder(@NonNull View itemView) {
            super(itemView);
            labNameTv=itemView.findViewById(R.id.labNameTv);
            cardNo=itemView.findViewById(R.id.cardNoTv);
            addedOn=itemView.findViewById(R.id.addedOnTv);
            labNameTvLable=itemView.findViewById(R.id.labNameTvLable);
            cardNoTvLabel=itemView.findViewById(R.id.cardNoTvLabel);
            addedOnTvLabel=itemView.findViewById(R.id.addedOnTvLabel);
        }
    }
}
