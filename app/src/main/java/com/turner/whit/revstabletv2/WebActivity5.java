package com.turner.whit.revstabletv2;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

//This is a generic web activity with URL passed in from the intent call

public class WebActivity5 extends AppCompatActivity {

    public WebView webView;
    public ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web5);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#273134")));
        String url = Objects.requireNonNull(super.getIntent().getExtras()).getString("urlString");
        loadUrlInWebView(url);
    }



// This enables the back button to go back one step in web history

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        // Otherwise go back to home screen and clear cache.
        super.onBackPressed();
        webView.clearCache(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Title bar back press triggers onBackPressed()
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadUrlInWebView(String url) {
        progressBar = findViewById(R.id.prg);
        webView = findViewById(R.id.webView5);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                setTitle("Loading...");
            }

//put conditional here to grab car title from adapter if it exists, else use view.getTitle

            @Override public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                progressBar.setVisibility(View.GONE);
                setTitle(view.getTitle());
            }
        });
        WebSettings settings = webView.getSettings();
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setAllowUniversalAccessFromFileURLs(true);

        //The following will delete all old data previously used by webView prior to loading page
        android.webkit.WebStorage.getInstance().deleteAllData();
        webView.loadUrl(url);

        AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
        builder.setTitle("Please wait while content loads");
        builder.setMessage("It may take a few seconds");
        builder.setCancelable(true);

        final AlertDialog closedialog= builder.create();

        closedialog.show();

        final Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            public void run() {
                closedialog.dismiss();
                timer2.cancel(); //this will cancel the timer of the system
            }
        }, 2000); // the timer will count 3 seconds....
    }

    //Clean up cache on abnormal exit

    @Override
    protected void onStop(){
        super.onStop();
        webView.clearCache(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.clearCache(true);
        Runtime.getRuntime().gc();
    }



}
