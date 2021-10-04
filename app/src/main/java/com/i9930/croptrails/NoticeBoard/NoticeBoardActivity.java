package com.i9930.croptrails.NoticeBoard;

import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.NoticeBoard.Model.NoticeDatum;
import com.i9930.croptrails.NoticeBoard.Model.NoticeResponse;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;


@RequiresApi(api = Build.VERSION_CODES.O)
public class NoticeBoardActivity extends AppCompatActivity {
    String TAG = "NoticeBoardActivity";
    NoticeBoardActivity context;
    Toolbar mActionBarToolbar;
    Resources resources;
    List<NoticeDatum> noticeDatumList = new ArrayList<>();

    @BindView(R.id.noticeRecycler)
    RecyclerView noticeRecycler;

    @BindView(R.id.noDataAvailableLayout)
    RelativeLayout noDataAvailableLayout;


    @BindView(R.id.loader)
    GifImageView loader;


    @BindView(R.id.connectivityLine)
    View connectivityLine;

    ViewFailDialog viewFailDialog;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
        ButterKnife.bind(this);
        context = this;
        resources = getResources();
        viewFailDialog = new ViewFailDialog();
        setToolbar();
        initRecyclerView();
        fetchNoticeBoard("onCreate");

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                OFFSET = 0;
                OFFSET_SIZE = 10;
                TOTAL_PAGES = 6000;
                currentPage = 0;
                isLoading = false;
                boolean isLastPage = false;
//                isLastPage = true;
                noticeDatumList.clear();
                fetchNoticeBoard("onRefresh");
            }
        });
    }

    private void setToolbar() {
        TextView title = (TextView) findViewById(R.id.tittle);
        //   Button submit_butt=(Button)findViewById(R.id.submit_test);
        title.setText(R.string.notice_board);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
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
                        NoticeBoardActivity.this.onBackPressed();
                    }
                }
        );
    }

    private void setData(List<NoticeDatum> list) {
//        noticeDatumList.clear();
        noticeDatumList.addAll(list);

//        noticeDatumList.add(new NoticeDatum("CropTrails","https://app.croptrails.farm/assets/userIcon2.png","2021-06-15 11:10:11","Great opportunity!","https://app.croptrails.farm/assets/farm2.jfif"));
//        noticeDatumList.add(new NoticeDatum("CropTrails","https://app.croptrails.farm/assets/userIcon2.png","2021-06-15 11:10:11","Great opportunity!","https://app.croptrails.farm/assets/farm2.jfif"));
//        noticeDatumList.add(new NoticeDatum("CropTrails","https://app.croptrails.farm/assets/userIcon2.png","2021-06-15 11:10:11","Great opportunity!",null));
//        noticeDatumList.add(new NoticeDatum("CropTrails","https://app.croptrails.farm/assets/userIcon2.png","2021-06-15 11:10:11","Great opportunity!","https://app.croptrails.farm/assets/farm2.jfif"));
//        noticeDatumList.add(new NoticeDatum("CropTrails","https://app.croptrails.farm/assets/userIcon2.png","2021-06-15 11:10:11","Great opportunity!",null));
//        noticeDatumList.add(new NoticeDatum("CropTrails","https://app.croptrails.farm/assets/userIcon2.png","2021-06-15 11:10:11","Great opportunity!",null));
//        noticeDatumList.add(new NoticeDatum("CropTrails","https://app.croptrails.farm/assets/userIcon2.png","2021-06-15 11:10:11","Great opportunity!",null));
//        noticeDatumList.add(new NoticeDatum("CropTrails","https://app.croptrails.farm/assets/userIcon2.png","2021-06-15 11:10:11","Great opportunity!","https://app.croptrails.farm/assets/farm2.jfif"));
//        noticeDatumList.add(new NoticeDatum("CropTrails","https://app.croptrails.farm/assets/userIcon2.png","2021-06-15 11:10:11","Great opportunity!","https://app.croptrails.farm/assets/farm2.jfif"));
//        noticeDatumList.add(new NoticeDatum("CropTrails","https://app.croptrails.farm/assets/userIcon2.png","2021-06-15 11:10:11","Great opportunity!","https://app.croptrails.farm/assets/farm2.jfif"));
//        noticeDatumList.add(new NoticeDatum("CropTrails","https://app.croptrails.farm/assets/userIcon2.png","2021-06-15 11:10:11","Great opportunity!",null));
//        noticeDatumList.add(new NoticeDatum("CropTrails","https://app.croptrails.farm/assets/userIcon2.png","2021-06-15 11:10:11","Great opportunity!",null));
//        noticeDatumList.add(new NoticeDatum("CropTrails","https://app.croptrails.farm/assets/userIcon2.png","2021-06-15 11:10:11","Great opportunity!","https://app.croptrails.farm/assets/farm2.jfif"));

        /*adapter=new NoticeBoardRvAdapter(noticeDatumList,this);
        noticeRecycler.setHasFixedSize(true);
        noticeRecycler.setLayoutManager(new LinearLayoutManager(context));
        noticeRecycler.setAdapter(adapter);*/
        adapter.notifyDataSetChanged();

    }


    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        noticeRecycler.setLayoutManager(layoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(5);
        noticeRecycler.addItemDecoration(itemDecorator);

        noticeRecycler.setHasFixedSize(true);
        noticeRecycler.setNestedScrollingEnabled(true);
        adapter = new VideoPlayerRecyclerAdapter(noticeDatumList, context);
        noticeRecycler.setAdapter(adapter);

        noticeRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();


                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount <= TOTAL_PAGES) {

                        isLoading = true;
                        currentPage += 1;
                        fetchNoticeBoard("loadMoreItems");
                    }
                }
            }
        });
    }

    VideoPlayerRecyclerAdapter adapter;
    private int OFFSET_SIZE = 10;
    private int TOTAL_PAGES = 6000;
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;


    private void fetchNoticeBoard(String from) {
        loader.setVisibility(View.VISIBLE);

        String auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        String personId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        String compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("person_id",personId);
        jsonObject.addProperty("comp_id",compId);
        jsonObject.addProperty("pageSize", OFFSET_SIZE);
        jsonObject.addProperty("offset", currentPage);
//        jsonObject.addProperty("person_id", "767");
//        jsonObject.addProperty("comp_id", "1");


        Log.e(TAG, "Request from "+from+" , " + new Gson().toJson(jsonObject));
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        Call<NoticeResponse> expenseDataCall = apiService.getNoticeBoard(jsonObject);
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        expenseDataCall.enqueue(new Callback<NoticeResponse>() {
            @Override
            public void onResponse(Call<NoticeResponse> call, Response<NoticeResponse> response) {
                String error = null;
                isLoaded[0] = true;
                isLoading=false;
                if (response.isSuccessful()) {
                    NoticeResponse noticeResponse = response.body();
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
                        List<NoticeDatum> noticeDatumList = noticeResponse.getData();
                        if (noticeDatumList != null && noticeDatumList.size() > 0) {
                            Log.e(TAG,"Got "+noticeDatumList.size()+" items at "+currentPage);
                            setData(noticeDatumList);
                        } else {
//                            isLoading=false;
                            /*ViewFailDialog viewFailDialog=new ViewFailDialog();
                            viewFailDialog.showDialog(HarvestShowPlanActivity.this,resources.getString(R.string.failed_to_load_harvest_data));*/
                            if (currentPage==0) {
                                noDataAvailableLayout.setVisibility(View.VISIBLE);
                                noticeRecycler.setVisibility(View.GONE);
                            }else{
                                TOTAL_PAGES=currentPage-1;
                                isLastPage = true;
                            }
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
                        error = response.errorBody().string();


                        viewFailDialog.showDialogForFinish(context, "Failed to load notice board content, Please try again later");
                        Log.e(TAG, "Show Planned err " + error);
                        //Toast.makeText(context, "Oops something went wrong. Please Reload", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(context, response.raw().request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }
                loader.setVisibility(View.GONE);
                setSwipeRefresh();
            }

            @Override
            public void onFailure(Call<NoticeResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                isLoaded[0] = true;
                Log.e(TAG, "Show Planned fail " + t.toString());
                notifyApiDelay(context, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);

                viewFailDialog.showDialogForFinish(context, "Failed to load notice board content, Please try again later");
                setSwipeRefresh();
            }
        });
        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    internetFlow(isLoaded[0]);
                }
            }, AppConstants.DELAY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSwipeRefresh() {
        if (swipeRefresh.isRefreshing())
            swipeRefresh.setRefreshing(false);
//        else
//            swipeRefresh.setRefreshing(false);
    }

    String internetSPeed = "";

    private void internetFlow(boolean isLoaded) {
        try {
            ConnectivityUtils.internetFlow(isLoaded, this, connectivityLine, new ConnectivityUtils.InternetSpeedListener() {
                @Override
                public void onSpeedUpdated(String speed) {
                    internetSPeed = speed;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}