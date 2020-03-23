package com.example.myapplication_bridge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.mWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());

        String first_addr = getString(R.string.url);
        mWebView.loadUrl(first_addr);

        AndroidBridge ab = new AndroidBridge(mWebView, MainActivity.this);
        mWebView.addJavascriptInterface(ab, "Android");
    }
}
