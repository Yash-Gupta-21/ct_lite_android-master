package com.i9930.croptrails.FarmerInnerDashBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.i9930.croptrails.ApiFailDialog.ViewFailDialog;
import com.i9930.croptrails.FarmDetails.Model.LineChartInfo;
import com.i9930.croptrails.FarmerInnerDashBoard.Fragments.FrmrDshbrdFragment;
import com.i9930.croptrails.FarmerInnerDashBoard.Fragments.FrmrDtlsFragment;
import com.i9930.croptrails.FarmerInnerDashBoard.Fragments.FrmrTimelineFragment2;
import com.i9930.croptrails.FarmerInnerDashBoard.Models.ActivityStats;
import com.i9930.croptrails.FarmerInnerDashBoard.Models.FarmDetails;
import com.i9930.croptrails.FarmerInnerDashBoard.Models.FarmerInnerPagerResponse;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.ApiInterface;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.ConnectivityUtils;
import com.i9930.croptrails.Utility.RetrofitClientInstance;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.i9930.croptrails.InternetSpeed.InternetAndErrorData.notifyApiDelay;
import static com.i9930.croptrails.Utility.AppConstants.DELAY_API;

public class FrmrInnrDtlsPagerActivity extends AppCompatActivity {
    Context context;

    @BindView(R.id.toolbar_frmr_dtls)
    Toolbar toolbar_frmr_dtls;
    @BindView(R.id.tabs_frmr_dtls)
    TabLayout tabs_frmr_dtls;
    @BindView(R.id.viewpager_frmr_dtls)
    ViewPager viewpager_frmr_dtls;
    private int[] tabIcons = {
            R.drawable.ic_dashboard_white_24dp,
            R.drawable.ic_timeline,
            R.drawable.ic_details
    };
    String TAG = "FrmrInnrDtlsPagerActivity";
    FarmDetails farmDetails;
    List<LineChartInfo> dateWiseGradeList;
    String crop_age;
    String latestImage;
    Resources resources;
    ActivityStats activityStats;

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
        setContentView(R.layout.activity_frmr_innr_dtls_pager);
        context = this;
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(context, languageCode);
        resources = contextlang.getResources();

        callingApiGetAllPagerData();
        init();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void callingApiGetAllPagerData() {
        String auth=SharedPreferencesMethod.getString(context, SharedPreferencesMethod.AUTHORIZATION);
        ApiInterface apiService = RetrofitClientInstance.getRetrofitInstance(auth).create(ApiInterface.class);
        String farmId = SharedPreferencesMethod.getString(FrmrInnrDtlsPagerActivity.this, SharedPreferencesMethod.SVFARMID);
        String userId = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.USER_ID);
        String token = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.TOKEN);

        JsonObject  jsonObject=new JsonObject();
        jsonObject.addProperty("farm_id",""+farmId);
        jsonObject.addProperty("user_id",""+userId);
        jsonObject.addProperty("token",""+token);
        Log.e(TAG, "farmerInnerPagerResponseCall " + new Gson().toJson(jsonObject));
        Call<FarmerInnerPagerResponse> farmerInnerPagerResponseCall = apiService.getFarmInnerData(jsonObject);
        final boolean[] isLoaded = {false};
        long currMillis=AppConstants.getCurrentMills();
        farmerInnerPagerResponseCall.enqueue(new Callback<FarmerInnerPagerResponse>() {
            @Override
            public void onResponse(Call<FarmerInnerPagerResponse> call, Response<FarmerInnerPagerResponse> response) {
                isLoaded[0] =true;
                String error="";
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 10) {
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(FrmrInnrDtlsPagerActivity.this, response.body().getMsg());
                    } else if (response.body().getStatus()== AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                        //un authorized access
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(FrmrInnrDtlsPagerActivity.this, resources.getString(R.string.unauthorized_access));
                    } else if (response.body().getStatus()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                        // auth token expired
                        ViewFailDialog viewFailDialog = new ViewFailDialog();
                        viewFailDialog.showSessionExpireDialog(FrmrInnrDtlsPagerActivity.this, resources.getString(R.string.authorization_expired));
                    } else {
                        FarmerInnerPagerResponse farmerInnerPagerResponse = response.body();
                        Log.e(TAG, "Farmer Dash " + new Gson().toJson(response.body()));

                        farmDetails = farmerInnerPagerResponse.getFarmDetails();
                        activityStats = farmerInnerPagerResponse.getActivityStats();
                        crop_age = String.valueOf(farmerInnerPagerResponse.getDays());
                        Log.e(TAG, "Crop Age " + crop_age);
                        if (farmerInnerPagerResponse.getImage() != null) {
                            latestImage = farmerInnerPagerResponse.getImage().getImgLink();
                        }
//                    Log.e(TAG,farmerInnerPagerResponse.getImage().getImgLink());
                        dateWiseGradeList = farmerInnerPagerResponse.getDateWiseGrade();
                        setupViewPager(viewpager_frmr_dtls);
                        tabs_frmr_dtls.setupWithViewPager(viewpager_frmr_dtls);
                        setupTabIcons();
                    }
                } else if (response.code()== AppConstants.API_STATUS.API_UNAUTH_ACCESS){
                    //un authorized access
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(FrmrInnrDtlsPagerActivity.this, resources.getString(R.string.unauthorized_access));
                } else if (response.code()==AppConstants.API_STATUS.API_AUTH_EXPIRED){
                    // auth token expired
                    ViewFailDialog viewFailDialog = new ViewFailDialog();
                    viewFailDialog.showSessionExpireDialog(FrmrInnrDtlsPagerActivity.this, resources.getString(R.string.authorization_expired));
                }else {
                    try {
                        error=response.errorBody().string().toString();

                        Log.e(TAG,"farmerInnerPagerResponseCall Errp "+ error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long newMillis=AppConstants.getCurrentMills();
                long diff=newMillis-currMillis;
                if(diff>=DELAY_API||(diff<DELAY_API&&error!=null&&!TextUtils.isEmpty(error))||response.code()==500){
                    notifyApiDelay(FrmrInnrDtlsPagerActivity.this,response.raw().request().url().toString(),
                            ""+diff,internetSPeed,error,response.code());
                }
            }

            @Override
            public void onFailure(Call<FarmerInnerPagerResponse> call, Throwable t) {
                isLoaded[0] =true;
                long newMillis=AppConstants.getCurrentMills();
                long diff=newMillis-currMillis;
                notifyApiDelay(FrmrInnrDtlsPagerActivity.this,call.request().url().toString(),
                        ""+diff,internetSPeed,t.toString(),0);
                Log.e(TAG, t.toString());
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
    private void internetFlow(boolean isLoaded){
        try {

            if (!isLoaded){
                if (!ConnectivityUtils.isConnected(context)){
                    AppConstants.failToast(context,getResources().getString(R.string.check_internet_connection_msg));
                }else {
                    ConnectivityUtils.checkSpeedWithColor(FrmrInnrDtlsPagerActivity.this, new ConnectivityUtils.ColorListener() {
                        @Override
                        public void onColorChanged(int color,String speed) {
                            internetSPeed=speed;
                        }
                    },20);
                }
            }



        }catch (Exception e){

        }

    }

    String internetSPeed="";
    private void init() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_frmr_dtls);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setupTabIcons() {
        tabs_frmr_dtls.getTabAt(0).setIcon(tabIcons[0]);
        tabs_frmr_dtls.getTabAt(1).setIcon(tabIcons[1]);
        tabs_frmr_dtls.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FrmrDshbrdFragment(), resources.getString(R.string.bottom_nav_dashboard));
        adapter.addFragment(new FrmrTimelineFragment2(), resources.getString(R.string.timeline_activity_title));
        adapter.addFragment(new FrmrDtlsFragment(), resources.getString(R.string.details_label));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FrmrDshbrdFragment.newInstance(crop_age, farmDetails, dateWiseGradeList, latestImage, activityStats);

                case 1:
                    return FrmrTimelineFragment2.newInstance("", "");

                default:
                    return FrmrDtlsFragment.newInstance("", "");
            }
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
