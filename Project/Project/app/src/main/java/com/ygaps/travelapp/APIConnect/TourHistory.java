package com.ygaps.travelapp.APIConnect;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TourHistory {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("minCost")
    @Expose
    private String minCost;

    @SerializedName("maxCost")
    @Expose
    private String maxCost;

    @SerializedName("startDate")
    @Expose
    private String startDate;

    @SerializedName("endDate")
    @Expose
    private String endDate;

    @SerializedName("adults")
    @Expose
    private int adults;

    @SerializedName("childs")
    @Expose
    private int childs;

    @SerializedName("isPrivate")
    @Expose
    private boolean isPrivate;

    @SerializedName("avatar")
    @Expose
    private Uri avatar;

    @SerializedName("isHost")
    @Expose
    private boolean isHost;

    @SerializedName("isKicked")
    @Expose
    private boolean isKicked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinCost() {
        return minCost;
    }

    public void setMinCost(String minCost) {
        this.minCost = minCost;
    }

    public String getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(String maxCost) {
        this.maxCost = maxCost;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChilds() {
        return childs;
    }

    public void setChilds(int childs) {
        this.childs = childs;
    }

    public boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Uri getAvatar() {
        return avatar;
    }

    public void setAvatar(Uri avatar) {
        this.avatar = avatar;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public boolean isKicked() {
        return isKicked;
    }

    public void setKicked(boolean kicked) {
        isKicked = kicked;
    }
}
