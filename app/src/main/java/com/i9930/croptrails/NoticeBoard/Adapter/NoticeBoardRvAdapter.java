package com.i9930.croptrails.NoticeBoard.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.i9930.croptrails.NoticeBoard.Model.NoticeDatum;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NoticeBoardRvAdapter extends RecyclerView.Adapter<NoticeBoardRvAdapter.Holder> {

    List<NoticeDatum> noticeDatumList;
    AppCompatActivity context;
    String profileImage = "";

    public NoticeBoardRvAdapter(List<NoticeDatum> noticeDatumList, AppCompatActivity context) {
        this.noticeDatumList = noticeDatumList;
        this.context = context;
        profileImage = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_IMAGE);
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_notice_board, null, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NoticeBoardRvAdapter.Holder holder, int position) {

        NoticeDatum modal = noticeDatumList.get(position);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(context).load(profileImage)
                .apply(options).into(holder.authorIV);


        holder.authorNameTV.setText(modal.getName());
        String time = null;
        if (AppConstants.isValidString(modal.getDoa()))
            time = AppConstants.convertDateTimeToLocal(modal.getDoa());
        if (AppConstants.isValidString(time))
            holder.timeTV.setText(time);
        else
            holder.timeTV.setVisibility(View.GONE);

        if (AppConstants.isValidString(modal.getTitle()))
            holder.descTV.setText(modal.getTitle());
        else
            holder.descTV.setVisibility(View.GONE);

        if (AppConstants.isValidString(modal.getContent()))
            holder.contentTv.setText(modal.getContent());
        else
            holder.contentTv.setVisibility(View.GONE);

        if (modal.getLink() != null)
            Picasso.with(context).load(modal.getLink()).into(holder.postIV);
        else
            holder.postIV.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return noticeDatumList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private CircleImageView authorIV;
        private TextView authorNameTV, timeTV, descTV;
        private ImageView postIV;
        private TextView  contentTv;
        private LinearLayout shareLL;

        public Holder(@NonNull View itemView) {
            super(itemView);
            // initializing our variables
            contentTv = itemView.findViewById(R.id.contentTv);
            shareLL = itemView.findViewById(R.id.idLLShare);
            authorIV = itemView.findViewById(R.id.idCVAuthor);
            authorNameTV = itemView.findViewById(R.id.idTVAuthorName);
            timeTV = itemView.findViewById(R.id.idTVTime);
            descTV = itemView.findViewById(R.id.idTVDescription);
            postIV = itemView.findViewById(R.id.idIVPost);
        }
    }
}
