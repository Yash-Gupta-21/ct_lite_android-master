package com.i9930.croptrails.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.i9930.croptrails.R;
import com.i9930.croptrails.Scan.ScanQRActivity;
import com.i9930.croptrails.Utility.SharedPreferencesMethod;
import com.i9930.croptrails.WebView.client.MyWebViewClient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {
    String TAG = "WebViewActivity";
    Context context;
    @BindView(R.id.webView)
    WebView webView;
    String link;
    boolean isScanned=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        context = this;
        ButterKnife.bind(this);

        String url = getIntent().getStringExtra("url");
        if (url != null && !TextUtils.isEmpty(url)) {
            link = url;
            isScanned=true;
        } else
            link = SharedPreferencesMethod.getString(context, SharedPreferencesMethod.NOTI_WV_LINK);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        MyWebViewClient webViewClient = new MyWebViewClient(this);
        webView.setWebViewClient(webViewClient);
        try {
            if (link != null && !TextUtils.isEmpty(link))
                webView.loadUrl(link);
            else
                webView.loadUrl("https://youtu.be/IfH3lkrgSjk");
        } catch (Exception e) {
            Toast.makeText(context, "Invalid url detected", Toast.LENGTH_SHORT).show();
            finish();

        }


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (isScanned)
        startActivity(new Intent(this, ScanQRActivity.class));
        super.onBackPressed();

    }

}
