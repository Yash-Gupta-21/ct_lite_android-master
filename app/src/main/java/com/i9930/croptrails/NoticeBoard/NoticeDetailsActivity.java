package com.i9930.croptrails.NoticeBoard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.halilibo.adm.core.DownloadManagerPro;
import com.halilibo.adm.report.listener.DownloadManagerListener;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.CommonClasses.StatusMsgModel;
import com.i9930.croptrails.NoticeBoard.Model.NoticeDatum;
import com.i9930.croptrails.NoticeBoard.Model.SinglePostResponse;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.saket.bettermovementmethod.BetterLinkMovementMethod;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NoticeDetailsActivity extends AppCompatActivity {

    String TAG="NoticeDetailsActivity";
    NoticeDetailsActivity context;
    Toolbar mActionBarToolbar;
    Resources resources;

    ViewFailDialog viewFailDialog;

    @BindView(R.id.videoView)
    VideoView videoView;

    @BindView(R.id.loader)
    GifImageView loader;

    @BindView(R.id.viewCountTv)
    TextView viewCountTv;
    DownloadManagerPro dm;
    private boolean isVideo(NoticeDatum modal) {

        return (AppConstants.isValidString(modal.getExt()) && (modal.getExt().contains("mp4") || modal.getExt().contains("3gp") ||
                modal.getExt().contains("mkv")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);

        ButterKnife.bind(this);
        context=this;
        resources=getResources();
        viewFailDialog = new ViewFailDialog();
        setToolbar();
        long datum=getIntent().getLongExtra("data",0);


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
        getData(datum);



    }

    private void setData(NoticeDatum datum){
        onBind(dm,datum);
        if (isVideo(datum)) {
            videoView.setVideoURI(Uri.parse(datum.getLink()));
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            //specify the location of media file
            Uri uri = Uri.parse(datum.getLink());
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(uri);
            videoView.requestFocus();
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (videoView.isPlaying()) {
                        videoView.pause();
                        thumbnail.setVisibility(View.VISIBLE);
                    } else {
                        thumbnail.setVisibility(View.GONE);
                        videoView.start();
                    }
                }
            });
        }else{
            videoView.setVisibility(View.GONE);
            thumbnail.setVisibility(View.VISIBLE);
        }

        increaseViewCount(datum.getId());
    }


    private void setToolbar(){

        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        TextView title = (TextView) findViewById(R.id.tittle);
        //   Button submit_butt=(Button)findViewById(R.id.submit_test);
        title.setText(R.string.notice);
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mActionBarToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NoticeDetailsActivity.this.onBackPressed();
                    }
                }
        );
    }

    FrameLayout media_container;
    ImageView thumbnail, volumeControl;
    ProgressBar progressBar;

    private CircleImageView authorIV;
    private TextView authorNameTV, timeTV, descTV;
    private ImageView postIV;
    private TextView  contentTv;
    private LinearLayout shareLL;

    private RequestManager initGlide(Context context){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ct_water_mark)
                .error(R.drawable.ct_water_mark);

        return Glide.with(context)
                .setDefaultRequestOptions(options);
    }

    public void onBind(DownloadManagerPro dm, NoticeDatum modal) {
        final boolean isVideo = isVideo(modal);
        media_container = findViewById(R.id.media_container);
        thumbnail = findViewById(R.id.thumbnail);
        progressBar = findViewById(R.id.progressBar);
        volumeControl = findViewById(R.id.volume_control);

        contentTv = findViewById(R.id.contentTv);
        shareLL = findViewById(R.id.idLLShare);
        authorIV = findViewById(R.id.idCVAuthor);
        authorNameTV = findViewById(R.id.idTVAuthorName);
        timeTV = findViewById(R.id.idTVTime);
        descTV = findViewById(R.id.idTVDescription);
        postIV = findViewById(R.id.idIVPost);
        viewCountTv = findViewById(R.id.viewCountTv);


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

        long impression=modal.getImpression()+1;

        viewCountTv.setText(""+impression);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(context).load(modal.getProfile())
                .apply(options).into(authorIV);


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
                            new ShareVideo(context, modal,dm).execute();
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
                thumbnail.setImageResource(R.drawable.ct_water_mark);
            }
        });

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
    private class ShareVideo extends AsyncTask<String, String, String> {

        Context context;
        NoticeDatum modal;
        String videourl = "your video url";
        DownloadManagerPro dm;

        public ShareVideo(Context context, NoticeDatum modal ,DownloadManagerPro dm) {
            this.context = context;
            Log.e("ShareVideo", "Share async");
            this.modal = modal;
            this.dm=dm;
            videourl = modal.getLink();
        }

        @Override
        protected String doInBackground(String... strings) {

            return sharevideFile(videourl, "video.mp4",dm);
        }

        @Override
        protected void onPostExecute(String s) {

//            Intent sharintent = new Intent("android.intent.action.SEND");
//            sharintent.setType("video/*");
//            sharintent.putExtra("android.intent.extra.STREAM", Uri.parse(s + "/video.mp4"));
//            context.startActivity(Intent.createChooser(sharintent, "share"));

            Log.e("ShareVideo", "Share async "+s+"/video.mp4");
            MediaScannerConnection.scanFile(context, new String[] { s+"/video.mp4" },

                    null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Intent shareIntent = new Intent(
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
                                    "Share"));

                        }
                    });

            super.onPostExecute(s);
        }
    }

    private String sharevideFile(String videourl, String name,DownloadManagerPro dm) {


//        SimpleDateFormat sd = new SimpleDateFormat("yymmhh");
//        String date = sd.format(new Date());
//        String name = "video" + date + ".mp4";
        String rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + File.separator + "Video";
//
        try {

            int c=dm.addTask(name, videourl,  1,rootDir, true, true);

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


    private void getData(long id) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("post_id", id);
        jsonObject.addProperty("comp_id",  SharedPreferencesMethod.getString(context,SharedPreferencesMethod.COMP_ID));
        jsonObject.addProperty("user_id",  SharedPreferencesMethod.getString(context,SharedPreferencesMethod.USER_ID));
        jsonObject.addProperty("token", "" + SharedPreferencesMethod.getString(context,SharedPreferencesMethod.TOKEN));

        ApiInterface apiInterface= RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);

        Log.e(TAG, "Data Sending  " + new Gson().toJson(jsonObject));

        apiInterface.getPostDetails(jsonObject).enqueue(new Callback<SinglePostResponse>() {
            @Override
            public void onResponse(Call<SinglePostResponse> call, Response<SinglePostResponse> response) {
                if (response.isSuccessful()) {
                    SinglePostResponse noticeResponse = response.body();
                    Log.e(TAG, "Resp " + new Gson().toJson(noticeResponse));
                    if (noticeResponse.getStatus() == 10) {

                        viewFailDialog.showSessionExpireDialog(context, noticeResponse.getMsg());
                    } else if (noticeResponse.getStatus() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                        //un authorized access

                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                    } else if (noticeResponse.getStatus() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                        // auth token expired
                        viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                    } else {
                        NoticeDatum noticeDatumList = noticeResponse.getData();
                        if (noticeDatumList != null) {
                            setData(noticeDatumList);
                        } else {
                            viewFailDialog.showDialogForFinish(context, getString(R.string.failed_to_load_post)+", "+resources.getString(R.string.please_try_again_later_msg));
                        }
                    }

                } else if (response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access

                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                } else if (response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired

                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                } else {
                    try {
                        String error = response.errorBody().string();


                        viewFailDialog.showDialogForFinish(context, getString(R.string.failed_to_load_post)+", "+resources.getString(R.string.please_try_again_later_msg));
                        Log.e(TAG, "Show Planned err " + error);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                loader.setVisibility(View.GONE);

            }



            @Override
            public void onFailure(Call<SinglePostResponse> call, Throwable t) {
                viewFailDialog.showDialogForFinish(context, getString(R.string.failed_to_load_post)+", "+resources.getString(R.string.please_try_again_later_msg));

            }
        });
    }

    private void increaseViewCount(long id) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("user_id",  SharedPreferencesMethod.getString(context,SharedPreferencesMethod.USER_ID));
        jsonObject.addProperty("token", "" + SharedPreferencesMethod.getString(context,SharedPreferencesMethod.TOKEN));

        ApiInterface apiInterface= RetrofitClientInstance.getRetrofitInstance(SharedPreferencesMethod.getString(context,SharedPreferencesMethod.AUTHORIZATION)).create(ApiInterface.class);

        Log.e(TAG, "Data Sending  " + new Gson().toJson(jsonObject));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.increaseViewCount(jsonObject).enqueue(new Callback<StatusMsgModel>() {
            @Override
            public void onResponse(Call<StatusMsgModel> call, Response<StatusMsgModel> response) {

            }


            @Override
            public void onFailure(Call<StatusMsgModel> call, Throwable t) {


            }
        });
    }


}