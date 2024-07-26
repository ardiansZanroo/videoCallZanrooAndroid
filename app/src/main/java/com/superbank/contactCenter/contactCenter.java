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
                            "    }" +
                            "};" +
                            "})()");
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);
                    handleLoadingError();
                }

                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request, android.webkit.WebResourceResponse errorResponse) {
                    super.onReceivedHttpError(view, request, errorResponse);
                    handleLoadingError();
                }

                private void handleLoadingError() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(contactCenter.this, "Failed to load the page. Please try again later.", Toast.LENGTH_SHORT).show();
                            webView.loadUrl("about:blank");
                            isWebViewStopped = true;
                        }
                    });
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

    @Override
    public void onBackPressed() {
        if (webView != null) {
            webView.stopLoading();
            webView.loadUrl("about:blank");
            webView.clearHistory();
            webView.clearCache(true);
            webView.removeAllViews();
            webView.destroy();
        }
        super.onBackPressed();
    }

    public boolean isWebViewStopped() {
        return isWebViewStopped;
    }
}
