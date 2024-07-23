package com.superbank.contactCenter;

public enum VideoCallResult {
    SUCCESS("success"),
    DISCONNECTED("disconnected"),
    CANCELED("canceled");

    private final String result;

    VideoCallResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public static VideoCallResult fromString(String result) {
        for (VideoCallResult videoCallResult : VideoCallResult.values()) {
            if (videoCallResult.result.equals(result)) {
                return videoCallResult;
            }
        }
        return null;
    }
}
