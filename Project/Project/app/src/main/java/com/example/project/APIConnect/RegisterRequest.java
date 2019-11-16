package com.example.project.APIConnect;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String username;

    @SerializedName("full_name")
    private String fullname;

    @SerializedName("email")
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    @SerializedName("phone")
    private String phone;

    @SerializedName("emailVerified")
    private boolean emailVerified;

    @SerializedName("phoneVerified")
    private boolean phoneVerified;
}
