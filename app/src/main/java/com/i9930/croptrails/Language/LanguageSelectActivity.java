package com.i9930.croptrails.Language;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.i9930.croptrails.Landing.LandingActivity;
import com.i9930.croptrails.Language.picker.PickerUI;
import com.i9930.croptrails.Language.picker.PickerUISettings;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.Utility.SharedPreferencesMethod2;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class LanguageSelectActivity extends AppCompatActivity {
    CardView select_english;
    CardView select_hindi;
    CardView select_marathi;
    CardView select_telugu;
    CardView select_kannada;
    CardView select_gujrati;
    TextView next, textView, textView2, nextTv;
    Context context;
    String languageC;


    private PickerUI mPickerUI;
    private int currentPosition = -1;
    private List<String> options;

    List<String> languageCodeLis = new ArrayList<>();

    List<AppLanguage> languageList = new ArrayList<>();

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
        String languageToLoad = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_language_select);

        if (SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE) != null && !TextUtils.isEmpty(SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE)))
            languageC = SharedPreferencesMethod2.getString(this, SharedPreferencesMethod2.LANGUAGECODE);
        else
            languageC = "en";
        loadAds();
        select_english = findViewById(R.id.select_english);
        select_hindi = findViewById(R.id.select_hindi);
        select_marathi = findViewById(R.id.select_marathi);
        select_telugu = findViewById(R.id.select_telugu);
        select_kannada = findViewById(R.id.select_kannada);
        select_gujrati = findViewById(R.id.select_gujrati);
        next = findViewById(R.id.txt_next);
        textView2 = findViewById(R.id.textView2);
        nextTv = findViewById(R.id.nextTv);
        textView = findViewById(R.id.textView);


        languageList.add(new AppLanguage("ENGLISH", "en"));
        languageList.add(new AppLanguage("Hindi", "hi"));
        languageList.add(new AppLanguage("Marathi", "mr"));
        languageList.add(new AppLanguage("Telugu", "te"));
        languageList.add(new AppLanguage("Kannada", "kn"));
        languageList.add(new AppLanguage("Gujarati", "gu"));
        languageList.add(new AppLanguage("Burmese", "my"));
        languageList.add(new AppLanguage("Filipino", "fil"));
        languageList.add(new AppLanguage("En-Filipino", "fil"));

        //Populate list
        options = Arrays.asList(getResources().getStringArray(R.array.irrigation_source_array));
        options = new ArrayList<>();
        options.add("ENGLISH");
        languageCodeLis.add("en");
        options.add("Hindi");
        languageCodeLis.add("hi");
        options.add("Marathi");
        languageCodeLis.add("mr");
        options.add("Telugu");
        languageCodeLis.add("te");
        options.add("Kannada");
        languageCodeLis.add("kn");
        options.add("Gujarati");
        languageCodeLis.add("gu");

        options.add("Burmese");
        languageCodeLis.add("my");
        options.add("Filipino");
        languageCodeLis.add("fil");

        options.add("En-Filipino");
        languageCodeLis.add("phi");

/*
        options.add("ENGLISH");
        languageCodeLis.add("en");
        options.add("हिंदी");
        languageCodeLis.add("hi");
        options.add("मराठी");
        languageCodeLis.add("mr");
        options.add("తెలుగు");
        languageCodeLis.add("te");
        options.add("ಕನ್ನಡ್");
        languageCodeLis.add("kn");
        options.add("ગુજરતી");
        languageCodeLis.add("gu");
        options.add("ENGLISH");
        languageCodeLis.add("en");
        options.add("हिंदी");
        languageCodeLis.add("hi");
        options.add("मराठी");
        languageCodeLis.add("mr");
        options.add("తెలుగు");
        languageCodeLis.add("te");
        options.add("ಕನ್ನಡ್");
        languageCodeLis.add("kn");
        options.add("ગુજરતી");
        languageCodeLis.add("gu");*/


        for (int i=0;i<languageCodeLis.size();i++){
            if (languageC.equals(languageCodeLis.get(i).trim())){
                currentPosition=i;
                break;
            }
        }



        mPickerUI = (PickerUI) findViewById(R.id.picker_ui_view);

        //Populate list
        mPickerUI.setItems(this, options);
        //mPickerUI.setItems(this,options,2);
        mPickerUI.setColorTextCenter(R.color.background_picker);
        mPickerUI.setColorTextNoCenter(R.color.background_picker);
        mPickerUI.setBackgroundColorPanel(R.color.background_picker);
        mPickerUI.setLinesColor(R.color.background_picker);
        mPickerUI.setItemsClickables(false);
        mPickerUI.setAutoDismiss(false);
        //currentPosition=3;


        mPickerUI.setOnClickItemPickerUIListener(
                new PickerUI.PickerUIItemClickListener() {

                    @Override
                    public void onItemClickPickerUI( int position, String valueResult) {
                        currentPosition = position;
                        updateViews(languageCodeLis.get(position));
                       // Toast.makeText(LanguageSelectActivity.this, valueResult, Toast.LENGTH_SHORT).show();
                    }
                });


       /* int randomColor = -1;

        randomColor = getRandomColor();
*/
        PickerUISettings pickerUISettings =
                new PickerUISettings.Builder().withItems(options)
                        .withAutoDismiss(false)

                        .withItemsClickables(true)
                        .withUseBlur(true)
                        .build();

        mPickerUI.setSettings(pickerUISettings);

        if (currentPosition == -1) {
            mPickerUI.slide();
        } else {
            mPickerUI.slide(currentPosition);
        }

    }

    private int getRandomColor() {
        // generate the random integers for r, g and b value
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        return Color.rgb(r, g, b);
    }

    @Override
    protected void onStart() {
        super.onStart();


        select_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateViews("en");
                select_english.setCardBackgroundColor(getResources().getColor(R.color.end_color_butt));
            }
        });
        select_hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateViews("hi");
                select_hindi.setCardBackgroundColor(getResources().getColor(R.color.end_color_butt));
            }
        });
        select_marathi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateViews("mr");
                select_marathi.setCardBackgroundColor(getResources().getColor(R.color.end_color_butt));
            }
        });
        select_telugu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateViews("te");
                select_telugu.setCardBackgroundColor(getResources().getColor(R.color.end_color_butt));
            }
        });
        select_kannada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateViews("kn");
                select_kannada.setCardBackgroundColor(getResources().getColor(R.color.end_color_butt));
            }
        });
        select_gujrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateViews("gu");
                select_gujrati.setCardBackgroundColor(getResources().getColor(R.color.end_color_butt));
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LanguageSelectActivity.this, LandingActivity.class);
                SharedPreferencesMethod2.setString(LanguageSelectActivity.this, SharedPreferencesMethod2.LANGUAGECODE, languageC);

                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(LanguageSelectActivity.this, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                    finish();
                    startActivity(intent, options.toBundle());

                } else {
                    finishAffinity();
                    finish();
                    startActivity(intent);

                }
            }
        });


        nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LanguageSelectActivity.this, LandingActivity.class);
                SharedPreferencesMethod2.setString(LanguageSelectActivity.this, SharedPreferencesMethod2.LANGUAGECODE, languageC);

                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeCustomAnimation(LanguageSelectActivity.this, R.anim.fade_in, R.anim.fade_out);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                    finish();
                    startActivity(intent, options.toBundle());

                } else {
                    finishAffinity();
                    finish();
                    startActivity(intent);

                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void updateViews(String languageCode) {
        context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();
        languageC = languageCode;
        /*toolbar.setTitle(resources.getString(R.string.setting_title));
        name_tv.setText(resources.getString(R.string.name_label));
        account_tv.setText(resources.getString(R.string.accountLabel));
        language_tv1.setText(resources.getString(R.string.langiage_label));
        logout_tv.setText(resources.getString(R.string.logout_label));*/
        next.setText(resources.getString(R.string.next));
        textView.setText(resources.getString(R.string.txt_select_a_language));

        nextTv.setText(resources.getString(R.string.next));
        textView2.setText(resources.getString(R.string.txt_select_a_language));

        next.setVisibility(View.VISIBLE);
        next.setTextColor(getResources().getColor(R.color.black));
        deselect_remaining();
        setLocale(languageCode);

        //restartActivity();

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Resources res = getBaseContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }

    public void deselect_remaining() {
        select_english.setCardBackgroundColor(getResources().getColor(R.color.start_color_butt));
        select_hindi.setCardBackgroundColor(getResources().getColor(R.color.start_color_butt));
        select_marathi.setCardBackgroundColor(getResources().getColor(R.color.start_color_butt));
        select_telugu.setCardBackgroundColor(getResources().getColor(R.color.start_color_butt));
        select_kannada.setCardBackgroundColor(getResources().getColor(R.color.start_color_butt));
        select_gujrati.setCardBackgroundColor(getResources().getColor(R.color.start_color_butt));
    }
}
