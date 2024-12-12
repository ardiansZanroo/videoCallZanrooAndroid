package com.superbank.contactCenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;

public class VideoCall extends ActivityResultContract<String, VideoCallResult> {
    private static final String TAG_URL = "url";
    private String baseUrl;

    // Enum to represent different environments
    public enum Environment {
        DEV("https://rfe.kyc-zanroodesk.my.id/client?id="),
        STG("https://ekyc-fe.videocall.stg.super-id.net/client?id="),
        PROD("https://ekyc-fe.videocall.super-id.net/client?id=");

        private final String url;

        Environment(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    // Constructor to initialize with the selected environment
    public VideoCall(Environment environment) {
        this.baseUrl = environment.getUrl();  // Set the base URL according to the selected environment
    }

    @Override
    public Intent createIntent(Context context, String input) {
        String fullUrl = baseUrl + input; // Concatenate the base URL with the dynamic part
        Intent intent = new Intent(context, contactCenter.class);
        intent.putExtra(TAG_URL, fullUrl);
        return intent;
    }

    @Override
    public VideoCallResult parseResult(int resultCode, Intent intent) {
        if (resultCode != Activity.RESULT_OK) {
            return VideoCallResult.CANCELED;
        }
        if (intent != null) {
            String result = intent.getStringExtra("result");
            return VideoCallResult.fromString(result);
        }
        return null;
    }
}
