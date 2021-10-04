package com.i9930.croptrails.ImagePager.Document;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.ImagePager.CustomViewPager;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.AppConstants;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

public class DocumentActivity extends AppCompatActivity {

    CustomPagerAdapter adapter;
    List<JsonObject> imageBitmapList;
    CustomViewPager viewPager;
    Context context;
    LinearLayout ll_dots;
    Resources resources;
    Toolbar mActionBarToolbar;
    private TextView[] dots;

    private void initViews() {
        tittle= (TextView) findViewById(R.id.tittle);
        mActionBarToolbar = (Toolbar) findViewById(R.id.confirm_order_toolbar_layout);
        viewPager = findViewById(R.id.vp_slider);
        ll_dots = findViewById(R.id.ll_dots);
    }
    TextView tittle;

    AdView mAdView;
    private void loadAds(){
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        List<String> testDeviceIds = Arrays.asList("1126E5CF85D37A9661950E74DF5A73FE");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
//        mAdView.setAdUnitId("ca-app-pub-5740952327077672/5919581872");
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
//                Log.e(TAG,"Add Errro "+adError.toString());
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.new_theme_status_bar));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        context = this;
        loadAds();
        final String languageCode = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        Context contextlang = LocaleHelper.setLocale(this, languageCode);
        resources = contextlang.getResources();
        initViews();
        tittle.setText(resources.getString(R.string.image_activity_title));
        setSupportActionBar(mActionBarToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        initVariables();
        setDocumentDataToPager();
    }

    private void initVariables() {
        imageBitmapList = new ArrayList<>();

    }

    private void setDocumentDataToPager() {

        if (DataHandler.newInstance().getImageListDoc()!=null) {
            imageBitmapList = DataHandler.newInstance().getImageListDoc();
            adapter = new CustomPagerAdapter(context,imageBitmapList);
            viewPager.setAdapter(adapter);

            if (imageBitmapList != null && imageBitmapList.size() > 0) {
                addBottomDots(0);
            }

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                    page = position;
                }

                @Override
                public void onPageSelected(int position) {
                    addBottomDots(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }else {
            Toast.makeText(context, "No image available", Toast.LENGTH_SHORT).show();
            //finishAllAndStartLanding();
        }
    }


    private void addBottomDots(int currentPage) {
        ll_dots.removeAllViews();
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            dots = new TextView[imageBitmapList.size()];

            // ll_dots.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(this);
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(25);
                dots[i].setTextColor(getResources().getColor(R.color.yellow4));
                ll_dots.addView(dots[i]);
            }

            if (dots.length > 0)
                dots[currentPage].setTextColor(getResources().getColor(R.color.new_theme));
        } else {
            dots = new TextView[imageBitmapList.size()];

            //ll_dots.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(this);
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(25);
                dots[i].setTextColor(getResources().getColor(R.color.yellow4));
                ll_dots.addView(dots[i]);
            }

            if (dots.length > 0)
                dots[currentPage].setTextColor(getResources().getColor(R.color.new_theme));
        }
    }


    public class CustomPagerAdapter extends PagerAdapter {

        private static final String TAG = "ImageViewPage";
        Context mContext;
        LayoutInflater mLayoutInflater;
        List<JsonObject> mResources;

        public CustomPagerAdapter(Context context, List<JsonObject> resources) {
            mContext = context;
            mResources = resources;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResources.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.layout_slider, container, false);
            final ImageView ivPhoto = (ImageView) itemView.findViewById(R.id.photo_view);
            final TextView msgTv =  itemView.findViewById(R.id.msgTv);
            int type = -1;
            String msg = "";
            String link = mResources.get(position).get("link").getAsString();
            type = mResources.get(position).get("type").getAsInt();
            if (AppConstants.isValidString(link)){
                Glide.with(mContext)
                        .load(link)
                        .into(ivPhoto);
            }

            if (type == AppConstants.DOCS.PROFILE) {
                msg = "" + context.getResources().getString(R.string.profile_image);
            } else if (type == AppConstants.DOCS.ID_PROOF) {
                msg = "" + context.getResources().getString(R.string.id_proof);
            } else if (type == AppConstants.DOCS.ADDRESS_PROOF) {
                msg = "" + context.getResources().getString(R.string.address_proof);
            } else if (type == AppConstants.DOCS.FARM) {
                msg = "" + context.getResources().getString(R.string.farm_image);
            } else if (type == AppConstants.DOCS.OWNERSHIP) {
                msg = "" + context.getResources().getString(R.string.ownership_image);
            }
            msgTv.setText(msg);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
