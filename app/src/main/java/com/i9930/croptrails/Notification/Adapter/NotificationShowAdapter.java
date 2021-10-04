package com.i9930.croptrails.Notification.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Notification.Model.NotificationData;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Setting.SettingActivity;
import com.i9930.croptrails.Test.TestActivity;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.WebView.WebViewActivity;

public class NotificationShowAdapter extends RecyclerView.Adapter<NotificationShowAdapter.Holder> {
    Activity context;
    List<NotificationData> list;
    String TAG = "NotificationShowAdapter";

    public NotificationShowAdapter(Activity context, List<NotificationData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout_show_notifications, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.title_tv.setText(list.get(position).getTitle());
        holder.content_tv.setText(list.get(position).getMessage());

        if (list.get(position).getImageLink() != null && !TextUtils.isEmpty(list.get(position).getImageLink())) {
           /* if (list.get(position).getImageLink().contains("croptrailsimages.s3")){
                ShowAwsImage.getInstance(context).downloadFile(Uri.parse(list.get(position).getImageLink()),holder.imageView,list.get(position).getImageLink());
            }else {*/
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.crp_trls_rounded_icon)
                        .error(R.drawable.crp_trls_rounded_icon);
                Glide.with(context).load(list.get(position).getImageLink()).apply(options).into(holder.imageView);
            //}
        } else {
            holder.imageView.setImageResource(R.drawable.crp_trls_rounded_icon);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(position);
            }
        });

        if (list.get(position).getIsRead().equals("Y")) {
            //holder.relative_lay.setAlpha((float) 0.5);
            holder.relative_lay.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.title_tv.setTextColor(context.getResources().getColor(R.color.black));
            holder.content_tv.setTextColor(context.getResources().getColor(R.color.black));
            holder.date_time_tv.setTextColor(context.getResources().getColor(R.color.black));

        } else {
            //holder.relative_lay.setAlpha((float) 1.0);
            holder.relative_lay.setBackgroundColor(context.getResources().getColor(R.color.notification_unread));
            holder.title_tv.setTextColor(context.getResources().getColor(R.color.white));
            holder.content_tv.setTextColor(context.getResources().getColor(R.color.white));
            holder.date_time_tv.setTextColor(context.getResources().getColor(R.color.white));

        }

        /*if (position%2==0){
            holder.relative_lay.setAlpha((float) 0.5);
        }else {
            holder.relative_lay.setAlpha((float) 1.0);
        }*/
        getTime(position);
        holder.date_time_tv.setText(getTime(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView title_tv, content_tv, date_time_tv;
        CircleImageView imageView;
        RelativeLayout relative_lay, full_rel_lay;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.notification_img);
            title_tv = itemView.findViewById(R.id.title_tv);
            content_tv = itemView.findViewById(R.id.content_tv);
            date_time_tv = itemView.findViewById(R.id.date_time_tv);
            relative_lay = itemView.findViewById(R.id.relative_lay);
            full_rel_lay = itemView.findViewById(R.id.full_rel_lay);
        }
    }

    public void onItemClick(int position) {
        Intent intent = new Intent(context, LandingActivity.class);
        NotificationData notificationData = list.get(position);
        if (notificationData.getKeyType() != null && notificationData.getKeyType().equals(AppConstants.OPEN_IN.IN_APP)) {
            if (notificationData.getActivity() != null) {
                if (notificationData.getActivity().equals(AppConstants.NAVIGATE.TIMELINE)) {
                    intent = new Intent(context, TestActivity.class);
                    SharedPreferencesMethod.setString(context, SharedPreferencesMethod.SVFARMID, notificationData.getFarmId());
                } else if (notificationData.getActivity().equals(AppConstants.NAVIGATE.UPDATE_PROFILE)) {
                    intent = new Intent(context, SettingActivity.class);
                } else if (notificationData.getActivity().equals(AppConstants.NAVIGATE.WEATHER)) {
                    //intent = new Intent(context, WeatherActivity.class);
                }
            }
        } else if (notificationData.getKeyType() != null && notificationData.getKeyType().equals(AppConstants.OPEN_IN.SIMPLE_MSG)) {
            if (list.get(position).getIsRead().equals("N")) {
                setNotificationAsRead(position);
            }
        } else if (notificationData.getKeyType() != null && notificationData.getKeyType().equals(AppConstants.OPEN_IN.LINK)) {
            intent = new Intent(context, WebViewActivity.class);
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.NOTI_WV_LINK, notificationData.getLink());
        } else if (notificationData.getKeyType() != null && notificationData.getKeyType().equals(AppConstants.OPEN_IN.IN_DIALOG)) {
            intent = new Intent(context, WebViewActivity.class);
            SharedPreferencesMethod.setString(context, SharedPreferencesMethod.NOTI_WV_LINK, notificationData.getLink());
        }
        intent.putExtra("id", notificationData.getNotiSchId());
        context.startActivity(intent);
    }

    private String getTime(int i) {

        NotificationData notificationData = list.get(i);
        String startTime = notificationData.getSentOn();
        String fullTIme = "";

        if (startTime!=null) {
            StringTokenizer tk = new StringTokenizer(startTime);
            String date = tk.nextToken();
            String time = tk.nextToken();

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");

            SimpleDateFormat d = new SimpleDateFormat("dd MMM yyyy");
            SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd");

            Date dt;
            Date dtt;

            try {
                dt = sdf.parse(time);
                dtt = d1.parse(date);
                fullTIme = sdfs.format(dt) + " " + d.format(dtt);
                Log.e(TAG, "Full Time: " + fullTIme);

            } catch (ParseException e) {
                e.printStackTrace();
                Log.e(TAG, "Time Display Exc " + e.toString()); // <-- I got result here

            }
        }else {
            fullTIme="";
        }

        return fullTIme;
    }

    private void setNotificationAsRead(int i) {
        NotificationData data = list.get(i);
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        String ids = data.getNotiSchId();
        apiInterface.setNotificationAsRead(ids,userId,token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.e(TAG, "Set As Read Res " + response.body().string().toString());
                        data.setIsRead("Y");
                        notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (response.code()==AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(context, context.getResources().getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(context, context.getResources().getString(R.string.authorization_expired));
                } else {
                    try {
                        Log.e(TAG, "Set As Read Err " + response.errorBody().string().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Set As Read Failure " + t.toString());
            }
        });
    }

}
