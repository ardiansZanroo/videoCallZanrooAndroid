package com.superbank.contactCenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;

public class contactCenter extends Activity {
    private static final String TAG_URL = "url";
    private boolean isWebViewStopped = false;

    @SuppressLint({"SetJavaScriptEnabled"})
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_layout);
        String url = getIntent().getStringExtra("url");
        if (url != null) {
            final WebView webView = (WebView) findViewById(R.id.webview_content);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setMediaPlaybackRequiresUserGesture(false);
            webSettings.setDomStorageEnabled(true);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    webView.loadUrl("javascript:(function(result) {" +
                            "window.WebViewApp = {" +
                            "    stopLoading: function() {" +
                            "        window.Interface.stopWebView(result);" +
                            "    }" +
                            "};" +
                            "})()");
                }
            });

            webView.addJavascriptInterface(new Object() {
                @JavascriptInterface
                public void stopWebView(final String result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.stopLoading();
                            isWebViewStopped = true;  // Set the flag to true when the WebView is stopped
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("result", result);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                    });
                }
            }, "Interface");

            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onPermissionRequest(PermissionRequest request) {
                    if (Build.VERSION.SDK_INT >= 21)
                        request.grant(request.getResources());
                }
            });

            webView.loadUrl(url);
        }
    }

    public boolean isWebViewStopped() {
        return isWebViewStopped;
    }
}
