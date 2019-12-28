package com.example.project.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTPResponse {

    @SerializedName("userId")
    @Expose
    private int userId;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("expiredOn")
    @Expose
    private long expiredOn;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getExpiredOn() {
        return expiredOn;
    }

    public void setExpiredOn(long expiredOn) {
        this.expiredOn = expiredOn;
    }
}
