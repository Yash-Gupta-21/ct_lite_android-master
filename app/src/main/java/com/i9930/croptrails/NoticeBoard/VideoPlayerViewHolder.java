package com.i9930.croptrails.NoticeBoard;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.halilibo.adm.core.DownloadManagerPro;
import com.i9930.croptrails.NoticeBoard.Model.NoticeDatum;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.AppConstants;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import me.saket.bettermovementmethod.BetterLinkMovementMethod;
import pl.droidsonroids.gif.GifImageView;


public class VideoPlayerViewHolder extends RecyclerView.ViewHolder {

    FrameLayout media_container;
    ImageView thumbnail, volumeControl;
    GifImageView progressBar;
    View parent;

    private CircleImageView authorIV;
    private TextView authorNameTV, timeTV, descTV;
    private ImageView postIV;
    private TextView viewCountTv, contentTv;
    private LinearLayout shareLL;


    RequestManager requestManager;

    public VideoPlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView;
        media_container = itemView.findViewById(R.id.media_container);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        progressBar = itemView.findViewById(R.id.progressBar);
        volumeControl = itemView.findViewById(R.id.volume_control);

        contentTv = itemView.findViewById(R.id.contentTv);
        shareLL = itemView.findViewById(R.id.idLLShare);
        authorIV = itemView.findViewById(R.id.idCVAuthor);
        authorNameTV = itemView.findViewById(R.id.idTVAuthorName);
        timeTV = itemView.findViewById(R.id.idTVTime);
        descTV = itemView.findViewById(R.id.idTVDescription);
        postIV = itemView.findViewById(R.id.idIVPost);
        viewCountTv = itemView.findViewById(R.id.viewCountTv);


    }

    public Uri getLocalBitmapUri(Bitmap imageView) {
        // Extract Bitmap from ImageView drawable
        Bitmap bmp = imageView;

        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private boolean isVideo(NoticeDatum modal) {

        return (AppConstants.isValidString(modal.getExt()) && (modal.getExt().contains("mp4") || modal.getExt().contains("3gp") ||
                modal.getExt().contains("mkv")));
    }

    public void onBind(DownloadManagerPro dm, NoticeDatum modal, RequestManager requestManager, Context context) {
        final boolean isVideo = isVideo(modal);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (AppConstants.isValidString(modal.getExt())&&(modal.getExt().contains("mp4")||modal.getExt().contains("3gp")||
//                        modal.getExt().contains("mkv"))) {

                int prevCount = 0;
                try {
                    prevCount = Integer.valueOf(viewCountTv.getText().toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                prevCount++;
                viewCountTv.setText("" + prevCount);
                modal.setImpression(prevCount);
                Intent intent = new Intent(context, NoticeDetailsActivity.class);
                intent.putExtra("data", modal.getId());
                context.startActivity(intent);


//                }
            }
        });

        this.requestManager = requestManager;
        parent.setTag(this);

       /* this.requestManager
                .load(modal.getLink())
                .into(thumbnail);
*/


        authorNameTV.setText(modal.getName());
        String time = null;
        if (AppConstants.isValidString(modal.getDoa()))
            time = AppConstants.convertDateTimeToLocal(modal.getDoa());
        if (AppConstants.isValidString(time))
            timeTV.setText(time);
        else
            timeTV.setVisibility(View.GONE);

        if (AppConstants.isValidString(modal.getTitle()))
            descTV.setText(modal.getTitle());
        else
            descTV.setVisibility(View.GONE);

        if (AppConstants.isValidString(modal.getContent())) {

            String text = modal.getContent() /*+ " " + RetrofitClientInstance.SELECTED_BASE_URL*/;

            contentTv.setText(text);
            contentTv.setLinkTextColor(context.getResources().getColor(R.color.new_color));
            contentTv.setMovementMethod(BetterLinkMovementMethod.newInstance().setOnLinkClickListener(
                    new BetterLinkMovementMethod.OnLinkClickListener() {
                        @Override
                        public boolean onClick(TextView textView, String url) {
//                                AppConstants.successToast(context,url);
                            return false;
                        }
                    }
            ));
            Linkify.addLinks(contentTv, Linkify.WEB_URLS);


        } else
            contentTv.setVisibility(View.GONE);

        if (AppConstants.isValidString(modal.getExt()))
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);

        viewCountTv.setText("" + modal.getImpression());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(context).load(modal.getProfile())
                .apply(options).into(authorIV);


        if (AppConstants.isValidString(modal.getLink())&&!modal.getLink().equals("0"))
            Glide.with(context).asBitmap().load(modal.getLink()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(final Bitmap bitmap, Transition<? super Bitmap> transition) {
                    postIV.setImageBitmap(bitmap);
                    ;
                    progressBar.setVisibility(View.GONE);
                    shareLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isVideo)
                                new ShareVideo(context, modal, dm).execute();
                            else {
                                String msg = "";
                                if (AppConstants.isValidString(modal.getTitle()))
                                    msg = "*" + modal.getTitle() + "* \n";
                                if (AppConstants.isValidString(modal.getContent()))
                                    msg = msg + " " + modal.getContent() + " \n";

                                Uri bmpUri = getLocalBitmapUri(bitmap);
                                if (bmpUri != null) {
                                    // Construct a ShareIntent with link to image
                                    Intent shareIntent = new Intent();
                                    shareIntent.setAction(Intent.ACTION_SEND);
                                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);

                                    shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
                                    if (!isVideo)
                                        shareIntent.setType("image/*");
                                    else
                                        shareIntent.setType("video/*");

                                    // Launch sharing dialog for image
                                    context.startActivity(Intent.createChooser(shareIntent, "Share Via"));
                                } else {
                                    // ...sharing failed, handle error
                                }
                            }
                        }
                    });
                }

                @Override
                public void onLoadCleared(Drawable placeholder) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    progressBar.setVisibility(View.GONE);
                    thumbnail.setVisibility(View.GONE);
                }
            });
        else thumbnail.setVisibility(View.GONE);

    }

    private class ShareVideo extends AsyncTask<String, String, String> {

        Context context;
        NoticeDatum modal;
        String videourl = "your video url";
        DownloadManagerPro dm;

        public ShareVideo(Context context, NoticeDatum modal, DownloadManagerPro dm) {
            this.context = context;
            Log.e("ShareVideo", "Share async");
            this.modal = modal;
            this.dm = dm;
            videourl = modal.getLink();
        }

        @Override
        protected String doInBackground(String... strings) {

            return sharevideFile(videourl, "video.mp4", dm);
        }

        @Override
        protected void onPostExecute(String s) {

//            Intent sharintent = new Intent("android.intent.action.SEND");
//            sharintent.setType("video/*");
//            sharintent.putExtra("android.intent.extra.STREAM", Uri.parse(s + "/video.mp4"));
//            context.startActivity(Intent.createChooser(sharintent, "share"));

            Log.e("ShareVideo", "Share async " + s + "/video.mp4");
            MediaScannerConnection.scanFile(context, new String[]{s + "/video.mp4"},

                    null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {



                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("video/*");
                            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, modal.getTitle());
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, modal.getContent());
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            context.startActivity(Intent.createChooser(
                                    new Intent().setAction(Intent.ACTION_SEND)
                                            .setType("video/mp4")
                                            .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            .putExtra(
                                                    Intent.EXTRA_STREAM,
                                                    uri)
                                    , "Share")
                            );

                            /*Intent shareIntent = new Intent(
                                    android.content.Intent.ACTION_SEND);
                            shareIntent.setType("video/*");
                            shareIntent.putExtra(
                                    Intent.EXTRA_TEXT, modal.getContent());
//                            shareIntent.putExtra(
//                                    android.content.Intent.EXTRA_TITLE, modal.getTitle());
                            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                            shareIntent
//                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            context.startActivity(Intent.createChooser(shareIntent,
                                    "Share"));*/

                        }
                    });

            super.onPostExecute(s);
        }
    }

    private String sharevideFile(String videourl, String name, DownloadManagerPro dm) {


//        SimpleDateFormat sd = new SimpleDateFormat("yymmhh");
//        String date = sd.format(new Date());
//        String name = "video" + date + ".mp4";
        String rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + File.separator + "Video";
//
        try {

            int c = dm.addTask(name, videourl, 1, rootDir, true, true);

            dm.startDownload(c);
            /*File rootFile = new File(rootDir);
            rootFile.mkdir();
            URL url = new URL(videourl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(rootFile,
                    name));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();*/
        } catch (IOException e) {
            Log.d("Error....", e.toString());
        }


        return rootDir;

    }

}
