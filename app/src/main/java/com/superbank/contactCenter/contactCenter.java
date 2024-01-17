package com.superbank.contactCenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;
public class contactCenter extends Activity  {
    private static final String TAG_URL = "url";

    @SuppressLint({"SetJavaScriptEnabled"})
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_layout);
        String url = getIntent().getStringExtra("url");
        if (url != null) {
            WebView webView = (WebView)findViewById(R.id.webview_content);
            webView.getSettings().setJavaScriptEnabled(true);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setMediaPlaybackRequiresUserGesture(false);
            webSettings.setDomStorageEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.setWebChromeClient(new WebChromeClient() {
                public void onPermissionRequest(PermissionRequest request) {
                    if (Build.VERSION.SDK_INT >= 21)
                        request.grant(request.getResources());
                }
            });
            webView.loadUrl(url);
        }
    }
}
