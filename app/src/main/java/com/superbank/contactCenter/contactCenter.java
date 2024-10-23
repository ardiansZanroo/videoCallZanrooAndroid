package com.superbank.contactCenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class contactCenter extends Activity {
    private static final String TAG_URL = "url";
    private boolean isWebViewStopped = false;
    private WebView webView;
    private boolean isBackDisabled = false; // Variable to track if back button is disabled

    @SuppressLint({"SetJavaScriptEnabled"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_layout);
        String url = getIntent().getStringExtra("url");
        if (url != null) {
            webView = findViewById(R.id.webview_content);
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
                            "    stopLoading: function(result) {" +
                            "        window.Interface.stopWebView(result);" +
                            "    }," +
                            "    disableBackButton: function() {" +
                            "        window.Interface.disableBack();" +
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
                            isWebViewStopped = true;
                            isBackDisabled = false;
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("result", result);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                    });
                }
                @JavascriptInterface
                public void disableBack() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isBackDisabled = true; // Disable back button
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

     @Override
    public void onBackPressed() {
        if (isBackDisabled) {
//            Toast.makeText(this, "On backPress", Toast.LENGTH_LONG).show();
        } else {
            webView.stopLoading();
            webView.clearHistory();
            webView.clearCache(true);
            webView.removeAllViews();
            webView.destroy();
            super.onBackPressed();
        }
    }

    public boolean isWebViewStopped() {
        return isWebViewStopped;
    }
}
