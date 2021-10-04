package com.i9930.croptrails.Communication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tooltip.Tooltip;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.Communication.Adapter.CaseRvAdapter;
import com.i9930.croptrails.Communication.Adapter.TabAdapter;
import com.i9930.croptrails.Communication.Fragments.ActiveCaseFragment;
import com.i9930.croptrails.Communication.Fragments.AllCaseFragment;
import com.i9930.croptrails.Communication.Model.CaseDatum;
import com.i9930.croptrails.Communication.Model.CaseListResponse;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class ActiveCaseActivity extends AppCompatActivity {

    ActiveCaseActivity context;
    String TAG = "ActiveCaseActivity";
    String auth, userId, token, personId,compId;
    Resources resources;
    Toolbar mActionBarToolbar;
    ViewFailDialog viewFailDialog;
    @BindView(R.id.createNewBtn)
    Button createNewBtn;
    @BindView(R.id.caseRecycler)
    RecyclerView caseRecycler;
    @BindView(R.id.connectivityLine)
    View connectivityLine;
    String internetSPeed = "";
    @BindView(R.id.progressBar)
    GifImageView progressBar;
    List<CaseDatum>caseDatumList=new ArrayList<>();
    CaseRvAdapter caseRvAdapter;
    private final int REQ_CODE=101;

    TabLayout tabLayout;
    ViewPager viewPager;
    TabAdapter adapter;
    FragmentManager fragmentManager;

    ActiveCaseFragment activeCaseFragment;
    AllCaseFragment allCaseFragment;


    @BindView(R.id.helpImage)
    ImageView helpImage;
    Tooltip tooltip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String languageToLoad = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        setContentView(R.layout.activity_active_case);


        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        context = this;
        ButterKnife.bind(this);
        viewFailDialog= new ViewFailDialog();
        auth = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);
        personId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.PERSON_ID);
        compId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.COMP_ID);

//        TextView title = (TextView) findViewById(R.id.tittle);

        helpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SimpleTooltip tooltip = new SimpleTooltip.Builder(context)
                        .anchorView(v)
                        .text("Legends")
                        .gravity(Gravity.BOTTOM)
                        .dismissOnOutsideTouch(true)
                        .dismissOnInsideTouch(true)
                        .modal(true)
                        .animated(true)
//                        .animationDuration(2000)
//                        .animationPadding(SimpleTooltipUtils.pxFromDp(50))
                        .contentView(R.layout.tooltip_custom)
                        .focusable(true)
                        .build();



                tooltip.show();

            }
        });

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
                        ActiveCaseActivity.super.onBackPressed();
                    }
                }
        );
//        title.setText("Cases");

        mActionBarToolbar.setTitle("Cases");
        mActionBarToolbar.setTitleTextColor(resources.getColor(R.color.white));
        caseRvAdapter=new CaseRvAdapter(context,caseDatumList,true);
        caseRecycler.setHasFixedSize(true);
        caseRecycler.setLayoutManager(new LinearLayoutManager(context));
        caseRecycler.setAdapter(caseRvAdapter);

        createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesMethod.setString(context,SharedPreferencesMethod.CASE_ID,"");
                Intent intent=new Intent(context,CreateCaseActivity.class);
                intent.putExtra("isNewCase",true);
                startActivityForResult(intent,REQ_CODE);
            }
        });
        getCases();



        activeCaseFragment=new ActiveCaseFragment(new ActiveCaseFragment.OnFragmentInteractionListener() {
            @Override
            public void onCaseStatusChanged() {
//                attachViewPager();
                activeCaseFragment.getCases();
                allCaseFragment.getCases();
            }
        });
        allCaseFragment=new AllCaseFragment(new AllCaseFragment.OnFragmentInteractionListener() {
            @Override
            public void onCaseStatusChanged() {
//                attachViewPager();
                activeCaseFragment.getCases();
                allCaseFragment.getCases();
            }
        });

        fragmentManager=getSupportFragmentManager();
        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
        attachViewPager();
    }


    private  void attachViewPager(){

        adapter = new TabAdapter(fragmentManager, context);
        adapter.addFragment(activeCaseFragment, "Active");
        adapter.addFragment(allCaseFragment, "All");
//        adapter.addFragment(rawProductFragment,"Raw\nProducts");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        highLightCurrentTab(0);
        tabLayout.getTabAt(0).select();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
               /* if (position==0){
                    if (activeProductFragment!=null){
                        activeProductFragment.refreshList(isActiveDataChange);
                        isActiveDataChange=false;
                    }
                }else if (position == 1){
                    if (inactiveProductFragment!=null) {
                        inactiveProductFragment.refreshList(isInactiveDataChange);
                        isInactiveDataChange=false;
                    }

                }else{
                    if (rawProductFragment!=null){
                        isRawDataChange = false;
                    }
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void highLightCurrentTab(int position) {

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i));
        }


        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(adapter.getSelectedTabView(position));
    }
    private void getCases() {
        //try {
        //progressDialog.show();
        caseDatumList.clear();
        progressBar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comp_id",  compId);
        jsonObject.addProperty("user_id",  userId);
        jsonObject.addProperty("token", "" + token);
        jsonObject.addProperty("person_id",  personId);
        jsonObject.addProperty("sender_id",  personId);
        jsonObject.addProperty("status", "N" );
        ApiInterface apiInterface= RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        
        Log.e(TAG, "Data Sending  " + new Gson().toJson(jsonObject));
        final boolean[] isLoaded = {false};
        long currMillis = AppConstants.getCurrentMills();
        apiInterface.getCases(jsonObject).enqueue(new Callback<CaseListResponse>() {
            @Override
            public void onResponse(Call<CaseListResponse> call, Response<CaseListResponse> response) {
                isLoaded[0] = true;
                String error = "";
                if (response.isSuccessful()) {
                    CaseListResponse responseBody = response.body();

                    Log.e(TAG, "Data  " + new Gson().toJson(responseBody));
                    if (responseBody.getData()!=null&&responseBody.getData().size()>0)
                    {
                        caseDatumList.addAll(responseBody.getData());
                        caseRvAdapter.notifyDataSetChanged();
                    }
                } else if ( response.code() == AppConstants.API_STATUS.API_UNAUTH_ACCESS) {
                    //un authorized access
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.unauthorized_access));
                } else if ( response.code() == AppConstants.API_STATUS.API_AUTH_EXPIRED) {
                    // auth token expired
                    viewFailDialog.showSessionExpireDialog(context, resources.getString(R.string.authorization_expired));
                } else {
                    //progressDialog.dismiss();
                    progressBar.setVisibility(View.GONE);
//                    isSpinnerDataLoaded = false;
//                    tv_add_new_farm_submit_bt.setVisibility(View.VISIBLE);
                    try {
                        error = response.errorBody().string().toString();
                        Log.e(TAG , "err  " + error);

                            viewFailDialog.showDialogForFinish(context, resources.getString(R.string.failed_to_load_data_msg));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                if (diff >= DELAY_API || (diff < DELAY_API && error != null && !TextUtils.isEmpty(error)) || response.code() == 500) {
                    notifyApiDelay(context, call.request().url().toString(),
                            "" + diff, internetSPeed, error, response.code());
                }

                progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onFailure(Call<CaseListResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG,"Fail "+ t.toString());
                isLoaded[0] = true;
                long newMillis = AppConstants.getCurrentMills();
                long diff = newMillis - currMillis;
                notifyApiDelay(context, call.request().url().toString(),
                        "" + diff, internetSPeed, t.toString(), 0);
                if (t instanceof SocketTimeoutException) {
                    getCases();
                } else {
                
                }

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                internetFlow(isLoaded[0]);
            }
        }, AppConstants.DELAY);
    }

    private void internetFlow(boolean isLoaded) {
        try {

            if (!isLoaded) {
                if (!ConnectivityUtils.isConnected(context)) {
                    AppConstants.failToast(context, resources.getString(R.string.check_internet_connection_msg));
                } else {
                    ConnectivityUtils.checkSpeedWithColor(context, new ConnectivityUtils.ColorListener() {
                        @Override
                        public void onColorChanged(int color, String speed) {
                            if (color == android.R.color.holo_green_dark) {
                                connectivityLine.setVisibility(View.GONE);
//                                    connectivityLine.setBackgroundColor(getColor(R.color.blue));
                            } else {
                                connectivityLine.setVisibility(View.VISIBLE);

                                connectivityLine.setBackgroundColor(resources.getColor(color));
                            }
                            internetSPeed = speed;
                        }
                    }, 20);
                }
            }


        } catch (Exception e) {

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG,"onActivityResult req:"+requestCode+", res:"+resultCode);
        if (requestCode==REQ_CODE&&resultCode==RESULT_OK)
            attachViewPager();
    }
}