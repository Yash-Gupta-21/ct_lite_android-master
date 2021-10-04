package com.i9930.croptrails.Croppy.Adapter;

import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import com.i9930.croptrails.Croppy.Model.CroppyMessage;
import com.i9930.croptrails.R;

public class CroppyMessageRvAdapter extends RecyclerView.Adapter<CroppyMessageRvAdapter.Holder> {

    Activity context;
    List<CroppyMessage>messageList;
    public CroppyMessageRvAdapter(Activity context, List<CroppyMessage> messageList) {
        this.context = context;
        this.messageList = messageList;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_layout_croppy_message,parent,false);
        return new Holder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        if (messageList.get(position).isSender()){
            holder.messageSend.setVisibility(View.VISIBLE);
            holder.messageReceive.setVisibility(View.GONE);
            holder.messageSend.setText(messageList.get(position).getMessage());
        }else {
           /* String msg=messageList.get(position).getMessage().replace("\\\n", System.getProperty("line.separator"));

            String msgs[]=messageList.get(position).getMessage().split("'\\\n'");


            String text = "- 30016264\\n- 30014837\\n- 30014836\\n";
            String[] split = text.split("\\\\n");

            Log.e("Adapter","Message1 "+messageList.get(position).getMessage());

            Log.e("Adapter","Message "+text+" And msgs "+new Gson().toJson(split));*/

            holder.messageSend.setVisibility(View.GONE);
            holder.messageReceive.setVisibility(View.VISIBLE);
            holder.messageReceive.setText(Html.fromHtml(messageList.get(position).getMessage()));
        }

    }

    @Override
    public int getItemCount() {
        if (messageList==null)
        return 0;
        else return messageList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView messageReceive,messageSend;
        CardView card;

        public Holder(@NonNull View itemView) {
            super(itemView);
            messageReceive=itemView.findViewById(R.id.messageReceive);
            messageSend=itemView.findViewById(R.id.messageSend);
        }
    }
}
