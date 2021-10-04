package com.i9930.croptrails.NoticeBoard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.halilibo.adm.core.DownloadManagerPro;
import com.halilibo.adm.report.listener.DownloadManagerListener;
import com.i9930.croptrails.NoticeBoard.Model.NoticeDatum;
import com.i9930.croptrails.R;

import java.util.List;


public class VideoPlayerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NoticeDatum> mediaObjects;
    private RequestManager requestManager;
    Context context;
    DownloadManagerPro dm;



    public VideoPlayerRecyclerAdapter(List<NoticeDatum> mediaObjects, Context context) {
        this.mediaObjects = mediaObjects;
        this.requestManager = initGlide(context);
        this.context=context;
        dm= new DownloadManagerPro(context);
        dm.init("downloadManager/", 12, new DownloadManagerListener() {
            @Override
            public void OnDownloadStarted(long taskId) {

            }

            @Override
            public void OnDownloadPaused(long taskId) {

            }

            @Override
            public void onDownloadProcess(long taskId, double percent, long downloadedLength) {

            }

            @Override
            public void OnDownloadFinished(long taskId) {

            }

            @Override
            public void OnDownloadRebuildStart(long taskId) {

            }

            @Override
            public void OnDownloadRebuildFinished(long taskId) {

            }

            @Override
            public void OnDownloadCompleted(long taskId) {

            }

            @Override
            public void connectionLost(long taskId) {

            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new  com.i9930.croptrails.NoticeBoard.VideoPlayerViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_layout_notice_board, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((com.i9930.croptrails.NoticeBoard.VideoPlayerViewHolder)viewHolder).onBind( dm, mediaObjects.get(i), requestManager, context);
    }

    @Override
    public int getItemCount() {
        return mediaObjects.size();
    }


    private RequestManager initGlide(Context context){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ct_water_mark)
                .error(R.drawable.ct_water_mark);

        return Glide.with(context)
                .setDefaultRequestOptions(options);
    }

}














