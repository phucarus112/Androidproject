package com.ygaps.travelapp.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchUserResponse {

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("users")
    @Expose
    private ArrayList<SearchUserObject> userObjects;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<SearchUserObject> getUserObjects() {
        return userObjects;
    }

    public void setUserObjects(ArrayList<SearchUserObject> userObjects) {
        this.userObjects = userObjects;
    }

    public  class SearchUserObject
    {
        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("fullName")
        @Expose
        private String fullName;

        @SerializedName("email")
        @Expose
        private String email;

        @SerializedName("phone")
        @Expose
        private String phone;

        @SerializedName("gender")
        @Expose
        private int gender;

        @SerializedName("dob")
        @Expose
        private String dob;

        @SerializedName("avatar")
        @Expose
        private String avatar;

        @SerializedName("typeLogin")
        @Expose
        private int typeLogin;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
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

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getTypeLogin() {
            return typeLogin;
        }

        public void setTypeLogin(int typeLogin) {
            this.typeLogin = typeLogin;
        }
    }
}
