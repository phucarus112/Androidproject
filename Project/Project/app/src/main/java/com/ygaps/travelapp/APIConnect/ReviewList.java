package com.ygaps.travelapp.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewList {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("avatar")
    @Expose
    private Object avatar;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("point")
    @Expose
    private Integer point;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getCreatedOn() {
        try{
            long t=Long.parseLong(createdOn);
            SimpleDateFormat sdf = new SimpleDateFormat("E, dd MM yyyy ");
            Date date = new Date(t);
            return sdf.format(date).toString();}
        catch (Exception e){}
        return "No start date";
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}