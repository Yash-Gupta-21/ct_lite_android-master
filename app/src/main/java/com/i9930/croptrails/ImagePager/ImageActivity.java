package com.i9930.croptrails.ImagePager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.i9930.croptrails.DataHandler.DataHandler;
import com.i9930.croptrails.ImagePager.Adapter.ViewPagerAdapter;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Language.LocaleHelper;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Display;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageActivity extends AppCompatActivity {

    ViewPagerAdapter adapter;
    private int page = 0;
    List<Bitmap> imageBitmapList;
    Uri[] imageUriList;
    ArrayList<Uri> imageUriLists;
    private Handler handler_slider;
    private final int delay = 3000;
    CustomViewPager viewPager;
    Context context;
    LinearLayout ll_dots;
    Resources resources;
    Toolbar mActionBarToolbar;
    Runnable runnable = new Runnable() {
        public void run() {
            if (adapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            viewPager.setCurrentItem(page, true);
            handler_slider.postDelayed(this, delay);
        }
    };
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
        setContentView(R.layout.activity_image);

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

        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            onlineModeEvent();
        } else {
            offlineModeEvent();
        }
    }

    private void initVariables() {
        imageBitmapList = new ArrayList<>();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        // viewPager.getLayoutParams().height = (height * 4) / 5;
    }

    private void offlineModeEvent() {

        if (DataHandler.newInstance().getImageBitmapList()!=null) {
            imageBitmapList = DataHandler.newInstance().getImageBitmapList();
            adapter = new ViewPagerAdapter(context, imageBitmapList);
            viewPager.setAdapter(adapter);

            if (imageBitmapList != null && imageBitmapList.size() > 0) {
                addBottomDots(0);
            }

            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    page = position;
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
            Toast.makeText(context, "Bitmap lis null", Toast.LENGTH_SHORT).show();
            //finishAllAndStartLanding();
        }
    }

    private void onlineModeEvent() {
        if (DataHandler.newInstance().getImageUriList()!=null) {
            imageUriList=new Uri[DataHandler.newInstance().getImageUriList().length];
            imageUriLists=new ArrayList<Uri>();
            imageUriList = DataHandler.newInstance().getImageUriList();
//            Toast.makeText(context, imageUriList.length, Toast.LENGTH_SHORT).show();
//            Log.e("img_size",imageUriList.length+"");
            adapter = new ViewPagerAdapter(imageUriList, context);
            viewPager.setAdapter(adapter);

            if (imageUriList != null && imageUriList.length > 0) {
                addBottomDots(0);
            }

            //imageUriList


            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    page = position;
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
            finishAllAndStartLanding();
        }
    }

    private void finishAllAndStartLanding() {
        Intent intent = new Intent(context, LandingActivity.class);
        ActivityOptions options = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
            startActivity(intent, options.toBundle());
            finish();
        } else {
            finishAffinity();
            startActivity(intent);
            finish();
        }
    }

    private void addBottomDots(int currentPage) {
        ll_dots.removeAllViews();
        if (!SharedPreferencesMethod.getBoolean(context, SharedPreferencesMethod.OFFLINE_MODE)) {
            dots = new TextView[imageUriList.length];

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*Intent intent=new Intent(context,LandingActivity.class);
        startActivity(intent);
        finish();*/
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
