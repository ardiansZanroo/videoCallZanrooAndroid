package com.superbank.contactCenter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class VideoCall {
    private static final String TAG_URL = "url";
    private static final String BASE_URL = "https://rfe.kyc-zanroodesk.my.id/client?id=";

    private final Activity activity;

    private VideoCall(Activity activity) {
        this.activity = activity;
    }

    public static VideoCall activity(Activity activity) {
        return new VideoCall(activity);
    }

    public void start(String id) {
        String fullUrl = BASE_URL + id; // Concatenate the base URL with the dynamic part
        startCall(fullUrl);
    }

    // Overloaded start method without id
    public void start() {
        startCall(BASE_URL); // Use the BASE_URL directly
    }

    // Common method to start the call
    private void startCall(String url) {
        Intent intent = new Intent((Context)this.activity, contactCenter.class);
        intent.putExtra(TAG_URL, url); // Use the URL
        this.activity.startActivity(intent);
    }
}