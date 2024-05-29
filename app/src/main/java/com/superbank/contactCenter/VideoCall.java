package com.superbank.contactCenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;

public class VideoCall extends ActivityResultContract<String, String> {
    private static final String TAG_URL = "url";
    private static final String BASE_URL = "https://rfe.kyc-zanroodesk.my.id/client?id=";

    @Override
    public Intent createIntent(Context context, String input) {
        String fullUrl = BASE_URL + input; // Concatenate the base URL with the dynamic part
        Intent intent = new Intent(context, contactCenter.class);
        intent.putExtra(TAG_URL, fullUrl);
        return intent;
    }

    @Override
    public String parseResult(int resultCode, Intent intent) {
        if (resultCode != Activity.RESULT_OK) {
            return null;
        }
        return intent != null ? intent.getStringExtra("result") : null;
    }
}
