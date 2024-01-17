package com.superbank.contactCenter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class VideoCall {
    private static final String TAG_URL = "url";
    private static final String BASE_URL = "https://www.innovateasterisk.com/";

    private final Activity activity;

    private VideoCall(Activity activity) {
        this.activity = activity;
    }

    public static VideoCall activity(Activity activity) {
        return new VideoCall(activity);
    }

    public void start(String id) {
        String fullUrl = BASE_URL + id; // Concatenate the base URL with the dynamic part
        Intent intent = new Intent((Context)this.activity, contactCenter.class);
        intent.putExtra("url", fullUrl); // Use the full URL here
        this.activity.startActivity(intent);
    }
}