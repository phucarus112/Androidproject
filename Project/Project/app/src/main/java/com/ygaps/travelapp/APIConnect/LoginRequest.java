package com.ygaps.travelapp.APIConnect;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("userId")
    private int userId;

    @SerializedName("emailVerified")
    private boolean emailVerified;

    @SerializedName("phoneVerified")
    private boolean phoneVerified;

    @SerializedName("token")
    private String token;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
